package com.myblog92.service;

import com.myblog92.payload.PostDto;
import com.myblog92.payload.PostResponse;

import java.util.List;

public interface PostService {
    PostDto createPost(PostDto postDto);

    PostDto getPostById(long id);

    void deletePostById(long id);

    PostDto updatePostById(long id, PostDto postDto);

    PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);
}
