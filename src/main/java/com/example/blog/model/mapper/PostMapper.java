package com.example.blog.model.mapper;

import com.example.blog.entity.Comment;
import com.example.blog.entity.Post;
import com.example.blog.model.dto.CommentDto;
import com.example.blog.model.dto.PostDto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class PostMapper {
    public static PostDto toPostDto(Post post) {
        Set<CommentDto> commentDtoSet = new HashSet<>();
        PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setContent(post.getContent());
        dto.setDescription(post.getDescription());
        dto.setTitle(post.getTitle());
        if (post.getComments() != null) {
            for (Comment comment : post.getComments()) {
                commentDtoSet.add(CommentMapper.toCommentDto(comment));
            }
            dto.setComments(commentDtoSet);
        }
        return dto;
    }

    public static Post toPost(PostDto postDto) {
        Set<Comment> commentSet = new HashSet<>();
        Post post = new Post();
        post.setId(postDto.getId());
        post.setContent(postDto.getContent());
        post.setDescription(postDto.getDescription());
        post.setTitle(postDto.getTitle());
        if (post.getComments() != null) {
            for (CommentDto comentDto : postDto.getComments()) {
                commentSet.add(CommentMapper.toComment(comentDto));
            }
            post.setComments(commentSet);
        }
        return post;
    }
}
