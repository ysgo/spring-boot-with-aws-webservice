package com.alone.boot.springboot.service.posts;

import com.alone.boot.springboot.domain.posts.Posts;
import com.alone.boot.springboot.domain.posts.PostsRepository;
import com.alone.boot.springboot.web.dto.PostsListResponseDto;
import com.alone.boot.springboot.web.dto.PostsResponseDto;
import com.alone.boot.springboot.web.dto.PostsSaveRequestDto;
import com.alone.boot.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {
  private final PostsRepository postsRepository;

  @Transactional
  public Long save(PostsSaveRequestDto postsSaveRequestDto) {
    return postsRepository.save(postsSaveRequestDto.toEntity()).getId();
  }

  @Transactional
  public Long update(Long id, PostsUpdateRequestDto postsUpdateRequestDto) {
    Posts posts = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

    posts.update(postsUpdateRequestDto.getTitle(), postsUpdateRequestDto.getContent());

    return id;
  }

  public PostsResponseDto findById(Long id) {
    Posts posts = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

    return new PostsResponseDto(posts);
  }

  @Transactional(readOnly = true)
  public List<PostsListResponseDto> findAllDesc() {
    return postsRepository.findAllDesc().stream().map(PostsListResponseDto::new).collect(Collectors.toList());
  }

  @Transactional
  public void delete(Long id) {
    Posts posts = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
    postsRepository.delete(posts);
  }
}
