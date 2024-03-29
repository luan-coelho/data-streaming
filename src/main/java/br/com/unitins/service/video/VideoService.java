package br.com.unitins.service.video;

import br.com.unitins.commons.MultipartBody;
import br.com.unitins.commons.pagination.Pageable;
import br.com.unitins.commons.pagination.Pagination;
import br.com.unitins.filters.VideoFilter;
import br.com.unitins.mapper.video.VideoMapper;
import br.com.unitins.model.enums.video.Resolution;
import br.com.unitins.model.video.Video;
import br.com.unitins.model.video.VideoResource;
import br.com.unitins.model.video.VideoWatchTime;
import br.com.unitins.repository.video.VideoRepository;
import br.com.unitins.repository.video.VideoResourceRepository;
import br.com.unitins.service.log.LogService;
import br.com.unitins.websocket.NotificationWebSocket;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.transaction.UserTransaction;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static br.com.unitins.commons.AppConstants.*;

@Slf4j
@ApplicationScoped
public class VideoService {

    @Inject
    VideoRepository videoRepository;

    @Inject
    VideoResourceRepository videoResourceRepository;

    @Inject
    LogService logService;

    @Inject
    EntityManager entityManager;

    @Inject
    UserTransaction transaction;

    @Inject
    NotificationWebSocket webSocket;

    @Transactional
    public void incrementViews(Long videoId) {
        videoRepository.incrementViews(videoId);
    }

    @Transactional
    public void updateWatchTime(VideoWatchTime videoWatchTime) {
        if (videoRepository.existsWatchTimeByVideoId(videoWatchTime.getVideoId())) {
            videoRepository.updateWatchTime(videoWatchTime);
        } else {
            videoRepository.persistWatchTime(videoWatchTime);
        }
    }

    public double getWatchTime(Long videoId) {
        return videoRepository.getWatchTime(videoId);
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
            throw new IllegalArgumentException("Já existe um vídeo cadastrado com este nome. Tente outro.");
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
        Video databaseVideo = videoRepository.findById(id);

        Video videoAux = videoRepository.findByTitle(video.getTitle());
        if (videoAux != null && !videoAux.getId().equals(id)) {
            throw new IllegalArgumentException("Já existe um vídeo cadastrado com este nome. Tente outro.");
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
        return videoRepository.findByIdOptional(id).orElseThrow(() -> new NotFoundException("Vídeo não encontrado pelo id"));
    }

    /**
     * Deleta o vídeo e seus arquivos
     *
     * @param id Identificador do vídeo
     */
    @Transactional
    public void deleteById(Long id) {
        Video video = videoRepository.findByIdOptional(id).orElseThrow(() -> new NotFoundException("Vídeo não encontrado pelo id"));

        if (video.getPath() != null && !video.getPath().isBlank() && video.getPath().contains(SEPARATOR + RESOURCES_DIRECTORY)) {
            try {
                deleteResources(video.getPath());
            } catch (IOException e) {
                logService.addError("Falha ao deletar recursos de vídeo", String.format("Vídeo de id %d. Motivo: %s", video.getId(), e.getMessage()));
            }
        }

        videoRepository.deleteById(video.getId());
        logService.addInfo("Deleção realizada com sucesso", String.format("Vídeo de id %d deletado com sucesso.", video.getId()));
    }

    @Transactional
    public void deleteResourcesById(Long id) {
        Video video = videoRepository.findByIdOptional(id).orElseThrow(() -> new NotFoundException("Vídeo não encontrado pelo id"));
        try {
            if (video.hasValidResolution()) {
                deleteResources(video.getPath());
                video.setPath(null);
                for (VideoResource resource : video.getResources()) {
                    videoResourceRepository.delete(resource);
                }
                video.setResources(null);
            }
        } catch (Exception e) {
            String message = "Falha ao deletar recursos de vídeo";
            logService.addError(message, "Não foi possível deletar os recursos de vídeo");
            throw new InternalServerErrorException(message);
        }
    }

    private void deleteResources(String path) throws IOException {
        String resourcesPath = USER_HOME + path;
        File directory = new File(resourcesPath).getParentFile();
        if (directory.toString().contains(RESOURCES_DIRECTORY) || directory.toString().contains(RESOURCES_DIRECTORY + SEPARATOR)) {
            FileUtils.deleteDirectory(directory);
        }
    }

    /**
     * Pega o arquivo de vídeo conforme o caminho passado
     *
     * @param path caminho do arquivo
     * @return arquivo de vídeo
     */
    public File getResourceByPath(String path) {
        String fullPath = USER_HOME + path;
        try {
            return new File(fullPath);
        } catch (Exception e) {
            throw new BadRequestException("Recurso não encontrado. Caminho inválido ou arquivo ausente");
        }
    }

    /**
     * Realiza o processamento do arquivo de vídeo
     *
     * @param video         Instância que representa um vídeo
     * @param multipartBody recursos de vídeo
     */
    public void processResource(Video video, MultipartBody multipartBody) throws Exception {
        try {
            // Gera e cria o diretório no qual será salvo o arquivo de vídeo
            String PathInput = buildResourcePathAndCreate();

            // Define o caminho completo do arquivo
            Path filePath = Paths.get(PathInput, multipartBody.fileName);

            // Salva o arquivo de víde e preenche a instância de vídeo com este diretório
            Files.copy(multipartBody.inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            String path = removeUserPath(filePath.toString());
            video.setPath(path);

            adjustResolutionAndSave(video, filePath.toString());
            logService.addInfo("Processamento realizado com sucesso", String.format("Vídeo de id %d processado com sucesso.", video.getId()));
        } catch (Exception e) {
            logService.addError("Aconteceu um erro inesperado", String.format("Aconteceu o seguinte erro ao tentar processar os recursos do vídeo de id %d. Motivo do erro: %s", video.getId(), e.getMessage()));
            throw new Exception("Não foi possível processar o recurso de vídeo. Motivo: ".concat(e.getMessage()));
        }
    }

    /**
     * Constroi caminho do arquivo de vídeo que veio do uploud, e caso não exista cria-o
     *
     * @return Caminho do arquivo de vídeo
     */
    private String buildResourcePathAndCreate() throws IOException {
        String outputPath = SEPARATOR + RESOURCES_DIRECTORY + SEPARATOR + "midia" + SEPARATOR + new Random().nextInt(1000);
        String pathBuilt = USER_HOME + outputPath;
        Path path = Paths.get(pathBuilt);

        // Cria o diretório caso não exista
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        return pathBuilt;
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
    public void adjustResolutionAndSave(Video video, String videoPath) throws Exception {
        String originalVideoResolution = getResolution(videoPath);
        int width = Integer.parseInt(originalVideoResolution.split("x")[0]);

        Set<Resolution> resolutions = new HashSet<>();
        if (width > 1280) {
            resolutions.add(Resolution.HD);
        }
        resolutions.add(Resolution.SD);

        long duration = getDuration(videoPath);
        video.setDuration(duration);
        video.setResources(new ArrayList<>());

        for (Resolution resolution : resolutions) {
            transaction.begin();
            generateResolution(video, videoPath, resolution);
            video = entityManager.merge(video);
            transaction.commit();
            webSocket.sendNotification("Vídeo de resolução " + resolution.toString() + " foi processado com sucesso!");
        }
    }

    /**
     * Gera um novo arquivo de vídeo com uma nova resolução especifíca
     *
     * @param video      Video que será salvo
     * @param videoPath  Caminho no qual o arquivo de vídeo gerado será salvo
     * @param resolution Resolução que será aplicada no arquivo de vídeo
     */
    private void generateResolution(Video video, String videoPath, Resolution resolution) throws Exception {
        String videoOutputPath = generateOutputFilePath(videoPath, resolution);
        VideoResource resource = new VideoResource(resolution, removeUserPath(videoOutputPath));
        long processingTime = resizeProcess(videoPath, videoOutputPath, resolution);
        resource.setProcessingTime(processingTime);
        video.addResource(resource);
    }

    /**
     * Remove caminho do usuário do caminho passado
     *
     * @param path caminho
     * @return caminho sem caminho de usuário
     */
    private String removeUserPath(String path) {
        return path.replace(USER_HOME, "");
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
     * @return Tempo que o vídeo levou para ser processado
     */
    private long resizeProcess(String videoInputPath, String videoOutputPath, Resolution resolution) throws Exception {
        String scale = String.format("scale=%s:%s", resolution.getWidth(), resolution.getHeight());

        String[] ffmpegCommand = new String[]{"ffmpeg", "-i", videoInputPath, "-vf", scale, "-c:v", "libx264", "-preset", "medium", "-crf", "23", "-c:a", "aac", "-b:a", "128k", videoOutputPath};

        ProcessBuilder processBuilder = new ProcessBuilder(ffmpegCommand);

        long startTime = System.currentTimeMillis();
        Process process = processBuilder.start();
        int exitCode = process.waitFor();

        if (exitCode != 0) {
            System.err.println("Ocorreu um erro ao executar o ffmpeg");
        }

        return System.currentTimeMillis() - startTime;
    }

    /**
     * Captura a duração em segundos de um determinado vídeo.
     *
     * @param videoPath Caminho onde está o arquivo de vídeo original.
     * @return Valor númerico que se refere a quantidade de segundos do vídeo
     * @throws Exception Qualquer exceção lançada.
     */
    public static long getDuration(String videoPath) throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder("ffprobe", "-v", "error", "-show_entries", "format=duration", "-of", "default=noprint_wrappers=1:nokey=1", videoPath);
        Process process = processBuilder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = reader.readLine();

        if (line != null) {
            return Math.round(Double.parseDouble(line));
        }

        throw new Exception("Não foi possível determinar a duração do vídeo.");
    }
}
