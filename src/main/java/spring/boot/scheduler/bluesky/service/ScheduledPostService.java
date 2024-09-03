package spring.boot.scheduler.bluesky.service;

import lombok.extern.slf4j.Slf4j;
import spring.boot.scheduler.bluesky.model.ScheduledPost;
import spring.boot.scheduler.bluesky.repository.ScheduledPostRepository;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ScheduledPostService {

    private final ScheduledPostRepository scheduledPostRepository;
    private final BlueskyApiService blueskyApiService;

    public ScheduledPostService(ScheduledPostRepository scheduledPostRepository, BlueskyApiService blueskyApiService) {
        this.scheduledPostRepository = scheduledPostRepository;
        this.blueskyApiService = blueskyApiService;
    }

    /**
     * Agenda uma nova postagem.
     *
     * @param content       Conteúdo da postagem.
     * @param scheduledTime Data e hora em que a postagem deve ser publicada.
     * @return A postagem agendada salva.
     */
    public ScheduledPost schedulePost(String content, LocalDateTime scheduledTime) {
        ScheduledPost scheduledPost = new ScheduledPost();
        scheduledPost.setContent(content);
        scheduledPost.setScheduledTime(scheduledTime);
        return scheduledPostRepository.save(scheduledPost);
    }

    /**
     * Tarefa agendada que verifica e publica postagens no horário correto.
     * Executa a cada minuto.
     */
    @Scheduled(fixedRate = 60000)
    public void publishScheduledPosts() {
        LocalDateTime now = LocalDateTime.now();
        List<ScheduledPost> postsToPublish = scheduledPostRepository.findAllByPublishedFalseAndScheduledTimeBefore(now);

        for (ScheduledPost post : postsToPublish) {
            try {
                boolean success = blueskyApiService.createPost(post.getContent());
                if (success) {
                    post.setPublished(true);
                    post.setPublishedAt(LocalDateTime.now());
                    log.info("Postagem com ID {} publicada com sucesso.", post.getId());
                } else {
                    post.setErrorMessage("Falha ao publicar a postagem.");
                    log.error("Falha ao publicar a postagem com ID {}.", post.getId());
                }
            } catch (Exception e) {
                post.setErrorMessage(e.getMessage());
                log.error("Erro ao publicar a postagem com ID {}: {}", post.getId(), e.getMessage());
            } finally {
                scheduledPostRepository.save(post);
            }
        }
    }

    public List<ScheduledPost> getAllScheduledPosts() {
        return scheduledPostRepository.findAll();
    }
    
}
