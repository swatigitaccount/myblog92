package com.myblog92.service.impl;

import com.myblog92.entity.Comment;
import com.myblog92.entity.Post;
import com.myblog92.exception.ResourceNotFound;
import com.myblog92.payload.CommentDto;
import com.myblog92.repository.CommentRepository;
import com.myblog92.repository.PostRepository;
import com.myblog92.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private ModelMapper modelMapper;
    private CommentRepository commentRepo;

    private PostRepository postRepo;

    public CommentServiceImpl(CommentRepository commentRepo, PostRepository postRepo,ModelMapper modelMapper) {
        this.commentRepo = commentRepo;
        this.postRepo = postRepo;
        this.modelMapper=modelMapper;
    }

    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
        Post post = postRepo.findById(postId).orElseThrow(
                () -> new ResourceNotFound("Post not found with id : " + postId)
        );

        Comment comment = mapToEntity(commentDto);
        comment.setPost(post);
        Comment c = commentRepo.save(comment);

        CommentDto dto = mapToDto(c);

        return dto;
    }

    @Override
    public void deleteCommentById(long postId, long commentId) {
        Post post = postRepo.findById(postId).orElseThrow(
                () -> new ResourceNotFound("Post not found with id : " + postId)
        );

        commentRepo.deleteById(commentId);

    }

    @Override
    public List<CommentDto> getCommentsByPostId(long postId) {
        List<Comment> comments = commentRepo.findByPostId(postId);
        List<CommentDto> dtos = comments.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList());
        return dtos;
    }

    @Override
    public CommentDto updateComment(long commentId, CommentDto commentDto) {
        Comment com = commentRepo.findById(commentId).get();
        Post post = postRepo.findById(com.getPost().getId()).get();

        Comment comment = mapToEntity(commentDto);
        comment.setPost(post);
        comment.setId(commentId);

        Comment savedComment = commentRepo.save(comment);
        CommentDto dto = mapToDto(savedComment);
        return dto;
    }

    Comment mapToEntity(CommentDto commentDto){
        Comment comment = modelMapper.map(commentDto,Comment.class);
        //comment.setName(commentDto.getName());
        //comment.setEmail(commentDto.getEmail());
        //comment.setBody(commentDto.getBody());
        return comment;
    }

    CommentDto mapToDto(Comment savedComment){
        CommentDto dto=modelMapper.map(savedComment,CommentDto.class);
        //dto.setName(savedComment.getName());
        //dto.setEmail(savedComment.getEmail());
        //dto.setBody(savedComment.getBody());
        return dto;
    }
}
