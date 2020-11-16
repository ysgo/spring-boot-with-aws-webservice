package com.alone.boot.springboot.web;

import com.alone.boot.springboot.service.posts.PostsService;
import com.alone.boot.springboot.web.dto.PostsResponseDto;
import com.alone.boot.springboot.web.dto.PostsSaveRequestDto;
import com.alone.boot.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class PostsApiController {
  private final PostsService postsService;

  @PostMapping("/api/v1/posts")
  public Long save(@RequestBody PostsSaveRequestDto postsSaveRequestDto) {
    return postsService.save(postsSaveRequestDto);
  }

  @PutMapping("/api/v1/posts/{id}")
  public Long update(@PathVariable Long id, @RequestBody PostsUpdateRequestDto postsUpdateRequestDto) {
    return postsService.update(id, postsUpdateRequestDto);
  }

  @GetMapping("/api/v1/posts/{id}")
  public PostsResponseDto findById(@PathVariable Long id) {
    return postsService.findById(id);
  }

  @DeleteMapping("/api/v1/posts/{id}")
  public Long delete(@PathVariable Long id) {
    postsService.delete(id);
    return id;
  }
}
