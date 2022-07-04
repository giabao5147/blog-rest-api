package com.example.blog.service.impl;

import com.example.blog.entity.Post;
import com.example.blog.exception.ResourceNotFoundException;
import com.example.blog.model.dto.PagedResponseDto;
import com.example.blog.model.dto.PostDto;
import com.example.blog.model.mapper.PostMapper;
import com.example.blog.repository.PostRepository;
import com.example.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Override
    public PostDto createPost(PostDto postDto) {
        Post post = PostMapper.toPost(postDto);
        Post newPost = postRepository.save(post);
        return PostMapper.toPostDto(newPost);
    }

    @Override
    public PagedResponseDto<PostDto> getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {

        PagedResponseDto<PostDto> pagedResponseDto = new PagedResponseDto<>();
        List<PostDto> postDtoList = new ArrayList<>();
        Sort sort;

        if (sortDir.equalsIgnoreCase("desc")) {
            sort = Sort.by(sortBy).descending();
        }
        else {
            sort = Sort.by(sortBy).ascending();
        }
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Post> postPage = postRepository.findAll(pageable);
        List<Post> postList = postPage.getContent();

        for (Post post : postList) {
            postDtoList.add(PostMapper.toPostDto(post));
        }
        pagedResponseDto.setContent(postDtoList);
        pagedResponseDto.setPageNumber(pageNo);
        pagedResponseDto.setPageSize(pageSize);
        pagedResponseDto.setTotalElements(postPage.getTotalElements());
        pagedResponseDto.setTotalPages(postPage.getTotalPages());
        pagedResponseDto.setLast(postPage.isLast());

        return pagedResponseDto;
    }

    @Override
    public PostDto getPostById(long id) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isPresent()) {
            return PostMapper.toPostDto(post.get());
        }
        throw new ResourceNotFoundException("Post");
    }

    @Override
    public PostDto updatePostById(long id, PostDto postDto) {
        Optional<Post> optional = postRepository.findById(id);
        if (optional.isPresent()) {
            Post post = PostMapper.toPost(postDto);
            post.setId(id);
            Post updatedPost = postRepository.save(post);
            return PostMapper.toPostDto(updatedPost);
        }
        throw new ResourceNotFoundException("Post");
    }

    @Override
    public void deletePostById(long id) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isPresent()) {
            postRepository.delete(post.get());
            return;
        }
        throw new ResourceNotFoundException("Post");
    }

}
