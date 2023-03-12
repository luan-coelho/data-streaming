package br.com.unitins.service.video;

import br.com.unitins.commons.MultipartBody;
import br.com.unitins.config.AppConfig;
import br.com.unitins.domain.enums.Resolution;
import br.com.unitins.domain.model.ResourcePath;
import br.com.unitins.domain.model.Video;
import br.com.unitins.domain.repository.VideoRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

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

@Slf4j
@ApplicationScoped
public class VideoService {

    @Inject
    VideoRepository videoRepository;

    private final String USER_HOME = System.getProperty("user.home");
    private final String BAR = File.separator; // "\" ou "/"

    /**
     * Retorna todos os vídeos cadastrados
     *
     * @return Lista de vídeos
     */
    public List<Video> getAll() {
        return videoRepository.listAll();
    }

    /**
     * Cria uma instância de vídeo e salva na base de dados
     *
     * @param video Video que será salvo
     * @return Instância do vídeo salvo
     */
    @Transactional
    public Video create(Video video) {
        if (videoRepository.existsByTitle(video.getTitle())) {
            throw new IllegalArgumentException("There is already a video registered with this name. Try another.");
        }
        videoRepository.persist(video);
        return video;
    }

    /**
     * Busca um vídeo pelo identificador
     *
     * @param id Identificador do vídeo
     * @return Instância do vídeo
     * @throws NotFoundException caso nenhum vídeo seja encontrado pelo identificador
     */
    public Video getById(Long id) {
        return videoRepository.findByIdOptional(id).orElseThrow(() -> new NotFoundException("Video not found by id"));
    }

    /**
     * Deleta o vídeo e seus arquivos
     *
     * @param id Identificador do vídeo
     */
    @Transactional
    public void delete(Long id) {
        Video video = videoRepository.findByIdOptional(id).orElseThrow(() -> new NotFoundException("Video not found by id"));
        String fileFolder = USER_HOME + video.getPath();

        try {
            FileUtils.deleteDirectory(new File(fileFolder));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        videoRepository.deleteById(video.getId());
    }

    /**
     * Salva e vincula um arquivo de vídeo para uma instância de vídeo
     *
     * @param videoId       Identificador do vídeo
     * @param multipartBody Recursos do arquivo de vídeo
     */
    public void saveResourceFile(Long videoId, MultipartBody multipartBody) {
        try {
            String userName = AppConfig.getLoggedUser().getNickName().toLowerCase();
            String subFolder = AppConfig.getLoggedUser().getCourses().get(0).getModules().get(0).getId().toString();
            String outputPath = BAR + "midia" + BAR + userName + BAR + subFolder;
            String directory = USER_HOME + outputPath;

            java.nio.file.Path path = Paths.get(directory);

            // Cria o diretório caso não exista
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }

            // Define o caminho completo do arquivo
            java.nio.file.Path filePath = Paths.get(directory, multipartBody.fileName);

            // Salva o arquivo no diretório
            Files.copy(multipartBody.inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);

            adjustResolutionAndSave(videoId, filePath.toString());
        } catch (Exception e) {
            log.error("Unable to save new video assets. Error message: ".concat(e.getMessage()));
        }

    }

    /**
     * Gera o caminhg com nome do arquivo que será gerado com uma nova resolução.
     *
     * @param path       Caminho onde está o arquivo de vídeo original.
     * @param resolution Resolução onde o arquivo vídeo será gerado.
     * @return Caminho do arquivo de vídeo que será gerado.
     */
    private String generateOutputFilePath(String path, Resolution resolution) {
        String[] pathStrings = path.split(BAR);
        String fileName = pathStrings[pathStrings.length - 1].split("\\.")[0];
        String newfileName = pathStrings[pathStrings.length - 1].split("\\.")[0] + "_" + resolution.getWidth();

        return path.replace(fileName, newfileName);
    }

    /**
     * Ajusta o vídeo original para uma nova resolução.
     *
     * @param videoId   Identificador do vídeo ao qual este recurso será vinculado.
     * @param videoPath Caminho onde está o arquivo de vídeo original.
     */
    @Transactional
    public void adjustResolutionAndSave(Long videoId, String videoPath) throws IOException {
        Video video = videoRepository.findByIdOptional(videoId).orElseThrow(() -> new NotFoundException("Video not found by id"));

        String originalVideoResolution;
        try {
            originalVideoResolution = getResolution(videoPath);

            int width = Integer.parseInt(originalVideoResolution.split("x")[0]);
            List<Resolution> resolutions;
            if (width > 1280) {
                resolutions = List.of(Resolution.HD, Resolution.SD);
            } else {
                resolutions = List.of(Resolution.SD);
            }

            for (Resolution resolution : resolutions) {
                String videoOutputPath = generateOutputFilePath(videoPath, resolution);
                generateResolution(videoPath, videoOutputPath, resolution);
                videoOutputPath = videoOutputPath.replace(USER_HOME, "");
                ResourcePath resolutionPath = new ResourcePath(resolution, videoOutputPath);
                video.getResolutionPaths().add(resolutionPath);
                videoPath = videoPath.replace(USER_HOME, "");
                video.setPath(videoPath);

                videoRepository.persist(video);
            }
        } catch (Exception e) {
            FileUtils.deleteDirectory(new File(videoPath));
            log.error("Error getting original video resolution");
        }
    }

    /**
     * Gerará um novo arquivo de vídeo conforme a resolução passada (854x480, 1280x720 ou 1920x1080)
     *
     * @param videoInputPath Caminho onde está o arquivo de vídeo original.
     */
    private void generateResolution(String videoInputPath, String videoOutputPath, Resolution resolution) throws Exception {
        resizeProcess(videoInputPath, videoOutputPath, resolution);
    }

    /**
     * Pega a resolução do vídeo especificado através do seu caminho.
     *
     * @param videoPath Caminho onde está o arquivo de vídeo.
     * @return Resolução do arquivo de vídeo.
     */
    private String getResolution(String videoPath) throws Exception {
        String[] command = {"ffprobe", "-v", "error", "-select_streams", "v:0", "-show_entries", "stream=width,height", "-of", "csv=s=x:p=0", videoPath};

        ProcessBuilder pb = new ProcessBuilder(command);
        Process process = pb.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        return reader.readLine();
    }

    /**
     * Faz uma chamada para um script em Python que faz processo de geração de um novo arquivo de vídeo com uma nova resolução.
     *
     * @param videoInputPath  Caminho onde está o arquivo de vídeo original.
     * @param videoOutputPath Caminho onde o novo arquivo de vídeo será salvo após a geração.
     * @param resolution      Resolução onde o novo arquivo de vídeo deverá ter.
     */
    private void resizeProcess(String videoInputPath, String videoOutputPath, Resolution resolution) throws Exception {
        String[] command = {"ffmpeg", "-i", videoInputPath, "-vf", "scale=" + resolution.getWidth() + ":" + resolution.getHeight(), "-c:v", "libx264", "-preset", "medium", "-crf", "23", "-c:a", "aac", "-b:a", "128k", "-movflags", "+faststart", videoOutputPath};

        ProcessBuilder pb = new ProcessBuilder(command);
        Process process = pb.start();
        int exitCode = process.waitFor();
        if (exitCode == 0) {
            log.info("Video successfully resized");
        } else {
            log.error("Error resizing video. ffmpeg exit code: " + exitCode);
        }
    }

    /*private String getOriginalPath(String path) {
        if (path.endsWith(".mp4")) {
            int index = path.lastIndexOf("/");
            if (index != -1) {
                path = path.substring(0, index);
            }
        }
        return path;
    }*/
}
