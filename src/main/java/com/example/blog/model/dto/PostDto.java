package com.example.blog.model.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class PostDto {
    private Long id;

    @NotEmpty
    @Size(min = 3, message = "The title should have at least 3 characters")
    private String title;

    private String description;

    @NotEmpty
    @Size(min = 5, message = "The title should have at least 5 characters")
    private String content;

    private Set<CommentDto> comments;
}
