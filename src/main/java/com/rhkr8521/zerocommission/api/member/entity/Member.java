package com.rhkr8521.zerocommission.api.member.entity;

import com.rhkr8521.zerocommission.common.entity.BaseTimeEntity;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder(toBuilder = true)  // toBuilder 옵션을 사용하여 기존 객체를 복사하는 빌더 생성 가능
@Table(name = "MEMBER")
@AllArgsConstructor
public class Member extends BaseTimeEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String email; // 이메일
    private String nickname; // 닉네임
    private String imageUrl; // 프로필 이미지
    private Boolean marketing_allow;

    @Enumerated(EnumType.STRING)
    private Role role; // 권한

    private String socialId; // 로그인한 소셜 타입의 식별자 값

    private String refreshToken; // 리프레시 토큰

    // 유저 권한 설정 메소드
    public Member authorizeUser() {
        return this.toBuilder()
                .role(Role.USER)
                .build();
    }

    // 닉네임 필드 업데이트
    public Member updateNickname(String updateNickname) {
        return this.toBuilder()
                .nickname(updateNickname)
                .build();
    }

    // 리프레시토큰 필드 업데이트
    public Member updateRefreshToken(String updateRefreshToken) {
        return this.toBuilder()
                .refreshToken(updateRefreshToken)
                .build();
    }

    // 프로필이미지 필드 업데이트
    public Member updateImageUrl(String updateImageUrl) {
        return this.toBuilder()
                .imageUrl(updateImageUrl)
                .build();
    }

    // 마케팅 동의 여부 필드 업데이트
    public Member updateMarketingAllow(Boolean updateMarketingAllow) {
        return this.toBuilder()
                .marketing_allow(updateMarketingAllow)
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(() -> this.role.getKey());
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return null;
    }
}
