package spring.boot.scheduler.bluesky.controller;

import lombok.RequiredArgsConstructor;
import spring.boot.scheduler.bluesky.model.ScheduledPost;
import spring.boot.scheduler.bluesky.service.ScheduledPostService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class ScheduledPostController {

    private final ScheduledPostService scheduledPostService;

    /**
     * Endpoint para agendar uma nova postagem.
     *
     * @param request Objeto contendo o conteúdo e horário da postagem.
     * @return A postagem agendada criada.
     */
    @PostMapping("/schedule")
    public ResponseEntity<ScheduledPost> schedulePost(@RequestBody SchedulePostRequest request) {
        ScheduledPost scheduledPost = scheduledPostService.schedulePost(request.getContent(), request.getScheduledTime());
        return ResponseEntity.ok(scheduledPost);
    }

    /**
     * Endpoint para listar todas as postagens agendadas.
     *
     * @return Lista de todas as postagens agendadas.
     */
    @GetMapping
    public ResponseEntity<List<ScheduledPost>> getAllScheduledPosts() {
        List<ScheduledPost> scheduledPosts = scheduledPostService.getAllScheduledPosts();
        return ResponseEntity.ok(scheduledPosts);
    }
}
