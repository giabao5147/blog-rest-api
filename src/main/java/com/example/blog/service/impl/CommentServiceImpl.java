package com.example.blog.service.impl;

import com.example.blog.entity.Comment;
import com.example.blog.entity.Post;
import com.example.blog.exception.BadRequestException;
import com.example.blog.exception.ResourceNotFoundException;
import com.example.blog.model.dto.CommentDto;
import com.example.blog.model.mapper.CommentMapper;
import com.example.blog.model.mapper.PostMapper;
import com.example.blog.repository.CommentRepository;
import com.example.blog.service.CommentService;
import com.example.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private PostService postService;

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public CommentDto createComment(Long postId, CommentDto commentDto) {
        Comment comment = CommentMapper.toComment(commentDto);
        Post post = PostMapper.toPost(postService.getPostById(postId));
        comment.setPost(post);
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public List<CommentDto> getAllCommentsByPostId(Long postId) {
        List<CommentDto> commentDtoList = new ArrayList<>();
        List<Comment> comments = commentRepository.findByPostId(postId);
        for (Comment comment : comments) {
            commentDtoList.add(CommentMapper.toCommentDto(comment));
        }
        return commentDtoList;
    }

    @Override
    public CommentDto getComment(Long postId, Long commentId) {
        Post post = PostMapper.toPost(postService.getPostById(postId));
        Optional<Comment> comment = commentRepository.findById(commentId);
        if (comment.isPresent()) {
            if (Objects.equals(comment.get().getPost().getId(), post.getId())) {
                return CommentMapper.toCommentDto(comment.get());
            }
            else {
                throw new BadRequestException("Comment id does not match post id");
            }
        }
        else {
            throw new ResourceNotFoundException("Comment");
        }
    }

    @Override
    public CommentDto updateComment(Long postId, Long commentId, CommentDto commentDto) {
        Post post = PostMapper.toPost(postService.getPostById(postId));
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        if (commentOptional.isPresent()) {
            Comment comment = commentOptional.get();
            if (Objects.equals(comment.getId(), post.getId())) {
                comment.setName(commentDto.getName());
                comment.setEmail(commentDto.getEmail());
                comment.setBody(commentDto.getBody());
                return CommentMapper.toCommentDto(commentRepository.save(comment));
            }
            else {
                throw new BadRequestException("Comment id does not match post id");
            }
        }
        else {
            throw new ResourceNotFoundException("Comment");
        }
    }

    @Override
    public void deleteComment(Long postId, Long commentId) {
        getComment(postId, commentId);
        commentRepository.deleteById(commentId);
    }

}
