package br.com.unitins.service.video;

import br.com.unitins.commons.MultipartBody;
import br.com.unitins.commons.pagination.Pageable;
import br.com.unitins.commons.pagination.Pagination;
import br.com.unitins.filters.VideoFilter;
import br.com.unitins.mapper.video.VideoMapper;
import br.com.unitins.model.enums.video.Resolution;
import br.com.unitins.model.video.ResourcePath;
import br.com.unitins.model.video.Video;
import br.com.unitins.repository.video.VideoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
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
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static br.com.unitins.commons.AppConstants.SEPARATOR;
import static br.com.unitins.commons.AppConstants.USER_HOME;

@Slf4j
@ApplicationScoped
public class VideoService {

    @Inject
    VideoRepository videoRepository;

    @Inject
    EntityManager entityManager;


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
    public void delete(Long id) {
        Video video = videoRepository.findByIdOptional(id).orElseThrow(() -> new NotFoundException("Vídeo não encontrado pelo id"));
        String fileFolder = USER_HOME + video.getPath();

        try {
            File directory = new File(fileFolder).getParentFile();
            FileUtils.deleteDirectory(directory);
        } catch (IOException ignored) {
        }
        videoRepository.deleteById(video.getId());
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
    @Transactional
    public void processResource(Video video, MultipartBody multipartBody) throws Exception {
        try {
            // Gera e cria o diretório no qual será salvo o arquivo de vídeo
            String PathInput = buildResourcePathAndCreate();

            // Define o caminho completo do arquivo
            Path filePath = Paths.get(PathInput, multipartBody.fileName);

            // Salva o arquivo de víde e preenche a instância de vídeo com este diretório
            Files.copy(multipartBody.inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            video.setPath(removeUserPath(filePath.toString()));

            adjustResolutionAndSave(video, filePath.toString());
        } catch (Exception e) {
            throw new Exception("Não foi possível processar o recurso de vídeo. Motivo: ".concat(e.getMessage()));
        }
    }

    /**
     * Constroi caminho do arquivo de vídeo que veio do uploud, e caso não exista cria-o
     *
     * @return Caminho do arquivo de vídeo
     */
    private String buildResourcePathAndCreate() throws IOException {
        String outputPath = SEPARATOR + "Vídeos" + SEPARATOR + "midia" + SEPARATOR + new Random().nextInt(1000);
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
    @Transactional
    public void adjustResolutionAndSave(Video video, String videoPath) throws Exception {
        String originalVideoResolution = getResolution(videoPath);
        int width = Integer.parseInt(originalVideoResolution.split("x")[0]);

        Set<Resolution> resolutions = new HashSet<>();
        if (width > 1280) {
            resolutions.add(Resolution.HD);
        }
        resolutions.add(Resolution.SD);

        for (Resolution resolution : resolutions) {
            generateResolution(video, videoPath, resolution);
        }
        long duration = getDuration(videoPath);
        video.setDuration(duration);

        entityManager.merge(video);
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
        resizeProcess(videoPath, videoOutputPath, resolution);
        ResourcePath resolutionPath = new ResourcePath(resolution, removeUserPath(videoOutputPath));
        video.addResolutionPatch(resolutionPath);
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
     */
    private void resizeProcess(String videoInputPath, String videoOutputPath, Resolution resolution) throws Exception {
        String scale = String.format("scale=%s:%s", resolution.getWidth(), resolution.getHeight());

        String[] ffmpegCommand = new String[]{"ffmpeg", "-i", videoInputPath, "-vf", scale, "-c:v", "libx264", "-preset", "medium", "-crf", "23", "-c:a", "aac", "-b:a", "128k", videoOutputPath};

        ProcessBuilder processBuilder = new ProcessBuilder(ffmpegCommand);

        Process process = processBuilder.start();
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            System.err.println("Ocorreu um erro ao executar o ffmpeg");
        }
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
