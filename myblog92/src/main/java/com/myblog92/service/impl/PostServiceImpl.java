package com.myblog92.service.impl;

import com.myblog92.entity.Post;
import com.myblog92.exception.ResourceNotFound;
import com.myblog92.payload.PostDto;
import com.myblog92.payload.PostResponse;
import com.myblog92.repository.PostRepository;
import com.myblog92.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepo;

    private ModelMapper modelMapper;

    public PostServiceImpl(PostRepository postRepo,ModelMapper modelMapper) {
        this.postRepo = postRepo;
        this.modelMapper=modelMapper;
    }

    @Override
    public PostDto createPost(PostDto postDto) {
        Post post = mapToEntity(postDto);
        Post savedPost = postRepo.save(post);
        PostDto dto = mapToDto(savedPost);
        return dto;
    }

    @Override
    public PostDto getPostById(long id) {
        Post post = postRepo.findById(id).orElseThrow(
                () -> new ResourceNotFound("Post not found with id : " + id)
        );
        return mapToDto(post);

    }

    @Override
    public void deletePostById(long id) {
        Post post = postRepo.findById(id).orElseThrow(
                () -> new ResourceNotFound(("Post not found with id : " + id))
        );
        postRepo.deleteById(id);
    }

    @Override
    public PostDto updatePostById(long id, PostDto postDto) {
        Post post = postRepo.findById(id).orElseThrow(
                () -> new ResourceNotFound(("Post not found with id : " + id))
        );
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());

        Post savedPost = postRepo.save(post);
        PostDto dto= mapToDto(savedPost);
        return dto;
    }

    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
        //Ternary Operator
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending():Sort.by(sortBy).descending();

        PageRequest pageable = PageRequest.of(pageNo,pageSize,sort);
        Page<Post> pagePostObjects = postRepo.findAll(pageable);

        List<Post> posts = pagePostObjects.getContent();   //getContent() converts Page() objects to Lists of Objects
        List<PostDto> dtos = posts.stream().map(post -> mapToDto(post)).collect(Collectors.toList());

        PostResponse response = new PostResponse();
        response.setDto(dtos);
        response.setPageNo(pagePostObjects.getNumber());
        response.setTotalPage(pagePostObjects.getTotalPages());
        response.setLastPage(pagePostObjects.isLast());
        response.setPageSize(pagePostObjects.getSize());

        return response;
    }

    Post mapToEntity(PostDto postDto){
        Post post = modelMapper.map(postDto,Post.class);

        //post.setTitle(postDto.getTitle());
       // post.setDescription(postDto.getDescription());
       // post.setContent(postDto.getContent());

        return post;
    }

    PostDto mapToDto(Post savedPost){
        PostDto postDto = modelMapper.map(savedPost,PostDto.class);

        //postDto.setId(savedPost.getId());
        //postDto.setTitle(savedPost.getTitle());
        //postDto.setDescription(savedPost.getDescription());
        //postDto.setContent(savedPost.getContent());

        return postDto;
    }
}
