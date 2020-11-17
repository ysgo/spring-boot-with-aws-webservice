package com.alone.boot.springboot.config.auth.dto;

import com.alone.boot.springboot.domain.user.User;
import lombok.Getter;

import java.io.Serializable;

// 인증된 사용자 정보만 사용하기 위한 클래스
/* User 클래스를 그대로 사용하지 않는 이유
  : User 클래스는 Entity 클래스로 언제 다른 엔티티와 관계가 형성될지 모르기 때문에 성능 이슈, 부수 효과가 발생할 확률이 높다
 */
@Getter
public class SessionUser implements Serializable {
  private String name;
  private String email;
  private String picture;

  public SessionUser(User user) {
    this.name = user.getName();
    this.email = user.getEmail();
    this.picture = user.getPicture();
  }
}
