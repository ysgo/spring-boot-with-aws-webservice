package com.alone.boot.springboot.web;

import com.alone.boot.springboot.domain.posts.Posts;
import com.alone.boot.springboot.domain.posts.PostsRepository;
import com.alone.boot.springboot.web.dto.PostsSaveRequestDto;
import com.alone.boot.springboot.web.dto.PostsUpdateRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostsApiControllerTest {
  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate testRestTemplate;

  @Autowired
  private PostsRepository postsRepository;

  @Autowired
  private WebApplicationContext context;

  private MockMvc mvc;

  @Before
  public void setup() {
    mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
  }

  @After
  public void tearDown() throws Exception {
    postsRepository.deleteAll();
  }

  @Test
  @WithMockUser(roles = "USER") // 인증된 모의(가짜) 사용자를 만들어서 사용한다는 의미의 어노테이션. 단, MockMvc에서만 작동함
  public void postsSave() throws Exception {
    String title = "title";
    String content = "content";
    PostsSaveRequestDto postsSaveRequestDto = PostsSaveRequestDto.builder().title(title).content(content).author("author").build();

    String url = "http://localhost:" + port + "/api/v1/posts";

//    ResponseEntity<Long> responseEntity = testRestTemplate.postForEntity(url, postsSaveRequestDto, Long.class);
//
//    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//    assertThat(responseEntity.getBody()).isGreaterThan(0L);

    // when
    mvc.perform(post(url)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content(new ObjectMapper().writeValueAsString(postsSaveRequestDto)))
        .andExpect(status().isOk());

    // then
    List<Posts> all = postsRepository.findAll();
    assertThat(all.get(0).getTitle()).isEqualTo(title);
    assertThat(all.get(0).getContent()).isEqualTo(content);
  }

  @Test
  @WithMockUser(roles = "USER")
  public void postsUpdate() throws Exception {
    Posts savedPosts = postsRepository.save(Posts.builder().title("title").content("content").author("author").build());

    Long updateId = savedPosts.getId();
    String expectedTitle = "title2";
    String expectedContent = "content2";

    PostsUpdateRequestDto postsUpdateRequestDto = PostsUpdateRequestDto.builder().title(expectedTitle).content(expectedContent).build();

    String url = "http://localhost:" + port + "/api/v1/posts/" + updateId;

//    HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(postsUpdateRequestDto);
//
//    ResponseEntity<Long> responseEntity = testRestTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);
//
//    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//    assertThat(responseEntity.getBody()).isGreaterThan(0L);

    // when
    mvc.perform(put(url)
      .contentType(MediaType.APPLICATION_JSON_UTF8)
      .content(new ObjectMapper().writeValueAsString(postsUpdateRequestDto)))
      .andExpect(status().isOk());

    // then
    List<Posts> all = postsRepository.findAll();
    assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
    assertThat(all.get(0).getContent()).isEqualTo(expectedContent);
  }

  @Test
  public void baseTimeTest() {
    LocalDateTime now = LocalDateTime.of(2020,11,14,0,0,0);
    postsRepository.save(Posts.builder().title("title").content("content").author("author").build());

    List<Posts> postsList = postsRepository.findAll();

    Posts posts = postsList.get(0);

    System.out.println(">>>>>>>> createDate=" + posts.getCreatedDate() + ", modifiedDate=" + posts.getModifiedDate());

    assertThat(posts.getCreatedDate()).isAfter(now);
    assertThat(posts.getModifiedDate()).isAfter(now);
  }
}
