package br.com.unitins.service;

import br.com.unitins.commons.MultipartBody;
import br.com.unitins.config.AppConfig;
import br.com.unitins.domain.ResolutionPath;
import br.com.unitins.domain.Video;
import br.com.unitins.domain.VideoRepository;
import br.com.unitins.domain.enums.Resolution;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class VideoService {

    @Inject
    VideoRepository videoRepository;

    public List<Video> getAll() {
        return videoRepository.listAll();
    }

    @Transactional
    public Video create(Video video) {
        if (videoRepository.findByTitle(video.getTitle())) {
            throw new IllegalArgumentException("There is already a video registered with this name. Try another.");
        }
        videoRepository.persist(video);
        return video;
    }

    public Video getById(Long id){
        return videoRepository.findByIdOptional(id).orElseThrow(() -> new NotFoundException("Video not found by id"));
    }

    @Transactional
    public void delete(Long id) {
        Video video = videoRepository.findByIdOptional(id).orElseThrow(() -> new NotFoundException("Video not found by id"));
        String userHome = System.getProperty("user.home");
        String fileFolder = userHome + video.getPath();
        deleteFolder(fileFolder);
        videoRepository.deleteById(video.getId());
    }

    public java.nio.file.Path saveFile(MultipartBody multipartBody) throws IOException {
        String userHome = System.getProperty("user.home");
        String userName = AppConfig.getLoggedUser().getNickName().toLowerCase();
        String subFolder = AppConfig.getLoggedUser().getCourses().get(0).getModules().get(0).getId().toString();
        String outputPath = "/Vídeos/midia/" + userName + "/" + subFolder + "/" + UUID.randomUUID();
        String directory = userHome + outputPath;

        java.nio.file.Path path = Paths.get(directory);

        // Cria o diretório caso não exista
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        // Define o caminho completo do arquivo
        java.nio.file.Path filePath = Paths.get(directory, multipartBody.fileName);

        // Salva o arquivo no diretório
        Files.copy(multipartBody.inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);

        return filePath;
    }

    /**
     * Gera o caminhg com nome do arquivo que será gerado com uma nova resolução.
     *
     * @param path       Caminho onde está o arquivo de vídeo original.
     * @param resolution Resolução onde o arquivo vídeo será gerado.
     * @return Caminho do arquivo de vídeo que será gerado.
     */
    private String generateOutputFilePath(String path, Resolution resolution) {
        String[] pathStrings = path.split("/");
        String fileName = pathStrings[pathStrings.length - 1].split("\\.")[0];
        String newfileName = pathStrings[pathStrings.length - 1].split("\\.")[0] + "_" + resolution.getWidth();

        return path.replace(fileName, newfileName);
    }

    /**
     * Ajusta o vídeo original para uma nova resolução.
     *
     * @param videoPath Caminho onde está o arquivo de vídeo original.
     */
    @Transactional
    public void adjustResolution(Long videoId, String videoPath) {
        Video video = videoRepository.findByIdOptional(videoId).orElseThrow(() -> new NotFoundException("Video not found by id"));

        String originalVideoResolution = getResolution(videoPath);
        try {
            int width = Integer.parseInt(originalVideoResolution.split("x")[0]);
            List<Resolution> resolutions;
            if (width > 1920) {
                resolutions = List.of(Resolution.HIGH, Resolution.MEDIUM, Resolution.LOW);
            } else if (width > 1280) {
                resolutions = List.of(Resolution.MEDIUM, Resolution.LOW);
            } else {
                resolutions = List.of(Resolution.LOW);
            }
            resolutions.forEach((r) -> {
                String videoOutputPath = generateOutputFilePath(videoPath, r);
                generateResolution(videoPath, videoOutputPath, r);
                String userHome = System.getProperty("user.home");
                String path = videoPath.replace(userHome, "");
                ResolutionPath resolutionPath = new ResolutionPath(null, r, path);
                video.getResolutionPaths().add(resolutionPath);
                video.setPath(path);
                videoRepository.persist(video);
            });
        } catch (Exception e) {
            deleteFolder(videoPath);
            throw new RuntimeException("Error getting original video resolution");
        }
    }

    /**
     * Gerará um novo arquivo de vídeo conforme a resolução passada (854x480, 1280x720 ou 1920x1080)
     *
     * @param videoInputPath Caminho onde está o arquivo de vídeo original.
     */
    private void generateResolution(String videoInputPath, String videoOutputPath, Resolution resolution) {
        resizeProcess(videoInputPath, videoOutputPath, resolution);
    }

    /**
     * Pega a resolução do vídeo especificado através do seu caminho.
     *
     * @param videoPath Caminho onde está o arquivo de vídeo.
     * @return Resolução do arquivo de vídeo.
     */
    private String getResolution(String videoPath) {
        String[] command = {"ffprobe", "-v", "error", "-select_streams", "v:0", "-show_entries", "stream=width,height", "-of", "csv=s=x:p=0", videoPath};
        String resolution = null;
        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            resolution = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resolution;
    }

    /**
     * Faz uma chamada para um script em Python que faz processo de geração de um novo arquivo de vídeo com uma nova resolução.
     *
     * @param videoInputPath  Caminho onde está o arquivo de vídeo original.
     * @param videoOutputPath Caminho onde o novo arquivo de vídeo será salvo após a geração.
     * @param resolution      Resolução onde o novo arquivo de vídeo deverá ter.
     */
    private void resizeProcess(String videoInputPath, String videoOutputPath, Resolution resolution) {
        String[] command = {"ffmpeg", "-i", videoInputPath, "-vf", "scale=" + resolution.getWidth() + ":" + resolution.getHeight(), "-c:a", "copy", videoOutputPath};
        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            Process process = pb.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Video successfully resized.");
            } else {
                System.err.println("Error resizing video. ffmpeg exit code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void deleteFolder(String folderPath) {
        folderPath = getOriginalPath(folderPath);

        try {
            File folder = new File(folderPath);
            if (folder.exists()) { // verifica se a pasta existe
                if (folder.isDirectory()) { // verifica se a pasta é realmente uma pasta (não um arquivo)
                    File[] files = folder.listFiles(); // obtem uma lista de arquivos e pastas na pasta
                    for (File file : files) {
                        file.delete(); // exclua cada arquivo e pasta dentro da pasta (recursivamente)
                    }
                    folder.delete(); // exclua a pasta em si
                    System.out.println("Folder deleted successfully");
                } else {
                    System.err.println("Não é uma pasta");
                }
            } else {
                System.err.println("folder not found");
            }
        } catch (Exception ignored) {

        }
    }

    private String getOriginalPath(String path) {
        if (path.endsWith(".mp4")) {
            int index = path.lastIndexOf("/");
            if (index != -1) {
                path = path.substring(0, index);
            }
        }
        return path;
    }
}
