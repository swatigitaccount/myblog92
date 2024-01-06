package com.myblog92.repository;

import com.myblog92.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Long> {// CRUDRepo doesn't support pagination while JpaRepo supports pagination
}
