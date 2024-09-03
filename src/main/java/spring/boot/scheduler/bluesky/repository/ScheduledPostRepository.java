package spring.boot.scheduler.bluesky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import spring.boot.scheduler.bluesky.model.ScheduledPost;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduledPostRepository extends JpaRepository<ScheduledPost, Long> {

    List<ScheduledPost> findAllByPublishedFalseAndScheduledTimeBefore(LocalDateTime dateTime);
}
