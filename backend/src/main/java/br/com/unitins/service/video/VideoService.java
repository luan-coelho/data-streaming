package br.com.unitins.service.video;

import br.com.unitins.commons.MultipartBody;
import br.com.unitins.commons.pagination.Pageable;
import br.com.unitins.commons.pagination.Pagination;
import br.com.unitins.domain.enums.video.Resolution;
import br.com.unitins.domain.model.video.ResourcePath;
import br.com.unitins.domain.model.video.Video;
import br.com.unitins.domain.repository.video.VideoRepository;
import br.com.unitins.mapper.video.VideoMapper;
import br.com.unitins.rest.filters.VideoFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Slf4j
@ApplicationScoped
public class VideoService {

    @Inject
    VideoRepository videoRepository;

    @Inject
    EntityManager entityManager;

    private final String USER_HOME = System.getProperty("user.home");
    private final String BAR = File.separator; // "\" ou "/"

    public void incrementViews(Long videoId) {
        videoRepository.incrementViews(videoId);
    }

    /**
     * Retorna todos os vídeos cadastrados
     *
     * @return Lista de vídeos
     */
    public Pagination<Video> getAll(Pageable pageable, VideoFilter filter) {
        return videoRepository.listAllPaginatedByTitle(pageable, filter);
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
     * Atualiza uma instância de vídeo
     *
     * @param video Video atualizado
     * @return Instância do vídeo atualizado
     */
    @Transactional
    public Video update(Long id, Video video) {
        Video databaseVideo = videoRepository.findByIdOptional(id).orElseThrow(() -> new NotFoundException("Video not found by id"));

        if (videoRepository.existsByTitle(video.getTitle())) {
            throw new IllegalArgumentException("There is already a video registered with this name. Try another.");
        }

        VideoMapper.INSTANCE.copyFields(databaseVideo, video);
        videoRepository.persist(databaseVideo);

        return databaseVideo;
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
        } catch (IOException ignored) {
        }
        videoRepository.deleteById(video.getId());
    }

    @Transactional
    public void saveResourceFile(Video video, MultipartBody multipartBody) throws Exception {
        String outputPath = BAR + "Vídeos" + BAR + "midia" + BAR + new Random().nextInt(1000);
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

        adjustResolutionAndSave(video, filePath.toString());
    }

    /**
     * Gera o caminhg com nome do arquivo que será gerado com uma nova resolução.
     *
     * @param path       Caminho onde está o arquivo de vídeo original.
     * @param resolution Resolução onde o arquivo vídeo será gerado.
     * @return Caminho do arquivo de vídeo que será gerado.
     */
    private String generateOutputFilePath(String path, Resolution resolution) {
        String[] pathStrings = path.split("[\\\\/]+");
        String fileName = pathStrings[pathStrings.length - 1].split("\\.")[0];
        String newfileName = pathStrings[pathStrings.length - 1].split("\\.")[0] + "_" + resolution.getWidth();

        return path.replace(fileName, newfileName);
    }

    /**
     * Ajusta o vídeo original para uma nova resolução.
     *
     * @param videoPath Caminho onde está o arquivo de vídeo original.
     */
    @Transactional()
    public void adjustResolutionAndSave(Video video, String videoPath) throws Exception {
        String originalVideoResolution = getResolution(videoPath);
        int width = Integer.parseInt(originalVideoResolution.split("x")[0]);

        Set<Resolution> resolutions = new HashSet<>();
        if (width > 1280) {
            resolutions.add(Resolution.HD);
        }
        resolutions.add(Resolution.SD);

        for (Resolution resolution : resolutions) {
            String videoOutputPath = generateOutputFilePath(videoPath, resolution);
            resizeProcess(videoPath, videoOutputPath, resolution);
            videoOutputPath = videoOutputPath.replace(USER_HOME, "");
            ResourcePath resolutionPath = new ResourcePath(resolution, videoOutputPath);
            video.getResolutionPaths().add(resolutionPath);
            videoPath = videoPath.replace(USER_HOME, "");
            video.setPath(videoPath);
        }
        entityManager.merge(video);
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
        String scale = String.format("scale=%s:%s", resolution.getWidth(), resolution.getHeight());

        String[] ffmpegCommand = new String[]{"ffmpeg", "-i", videoInputPath, "-vf", scale, "-c:v", "libx264", "-preset", "medium", "-crf", "23", "-c:a", "aac", "-b:a", "128k", videoOutputPath};

        ProcessBuilder processBuilder = new ProcessBuilder(ffmpegCommand);

        Process process = processBuilder.start();
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            System.err.println("An error occurred while running ffmpeg");
        }
    }
}
