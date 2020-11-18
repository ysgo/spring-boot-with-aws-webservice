package com.alone.boot.springboot.config.auth.dto;

import com.alone.boot.springboot.domain.user.Role;
import com.alone.boot.springboot.domain.user.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

// OAuth2UserService를 통해 가져온 OAuth2User의 attributes를 담을 클래스로 이후 네이버 등 다른 소셜 로그인에서도 사용됨
@Getter
public class OAuthAttributes {
  private Map<String, Object> attributes;
  private String nameAttributeKey;
  private String name;
  private String email;
  private String picture;

  @Builder
  public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String picture) {
    this.attributes = attributes;
    this.nameAttributeKey = nameAttributeKey;
    this.name = name;
    this.email = email;
    this.picture = picture;
  }

  // userNameAttributeName : OAuth2 로그인 진행 시 키가 되는 필드값. Primary key와 같은 의미
  // of 메서드는 OAuth2User 반환하는 사용자 정보가 Map이기 때문에 하나하나 변환이 필요함
  public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
    if("naver".equals(registrationId)) {
      return ofNaver("id", attributes);
    }
    return ofGoogle(userNameAttributeName, attributes);
  }

  private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
    return OAuthAttributes.builder()
            .name((String) attributes.get("name"))
            .email((String) attributes.get("email"))
            .picture((String) attributes.get("picture"))
            .attributes(attributes)
            .nameAttributeKey(userNameAttributeName)
            .build();
  }

  private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
    Map<String, Object> response = (Map<String, Object>) attributes.get("response");
    return OAuthAttributes.builder()
            .name((String) response.get("name"))
            .email((String) response.get("email"))
            .picture((String) response.get("profile_image"))
            .attributes(response)
            .nameAttributeKey(userNameAttributeName)
            .build();
  }

  public User toEntity() {
    return User.builder()
              .name(name)
              .email(email)
              .picture(picture)
              .role(Role.GUEST)
              .build();
  }
}
