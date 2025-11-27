package com.example.PasteleriaBackend.domain.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.PasteleriaBackend.domain.model.BlogComment;

public interface BlogCommentRepository extends JpaRepository<BlogComment, UUID> {

    List<BlogComment> findByPostIdOrderByCreatedAtAsc(UUID postId);
}
