package com.rhkr8521.spaceon.api.member.entity;

import com.rhkr8521.spaceon.api.group.entity.Group;
import com.rhkr8521.spaceon.common.entity.BaseTimeEntity;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import java.util.Collection;
import java.util.Collections;

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
    private String kakaoNickname; // 카카오닉네임
    private String profileImage; // 프로필 이미지

    @Enumerated(EnumType.STRING)
    private Role role; // 권한

    private String socialId; // 로그인한 소셜 타입의 식별자 값

    private String refreshToken; // 리프레시 토큰

    // 관리자 권한 설정 메소드
    public Member changeAdmin() {
        return this.toBuilder()
                .role(Role.ADMIN)
                .build();
    }

    // 카카오닉네임 필드 업데이트
    public Member updateKakaoNickname(String updateKakaoNickname) {
        return this.toBuilder()
                .kakaoNickname(updateKakaoNickname)
                .build();
    }

    // 프로필 필드 업데이트
    public Member updateProfileImage(String updateProfileImage) {
        return this.toBuilder()
                .profileImage(updateProfileImage)
                .build();
    }

    // 리프레시토큰 필드 업데이트
    public Member updateRefreshToken(String updateRefreshToken) {
        return this.toBuilder()
                .refreshToken(updateRefreshToken)
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