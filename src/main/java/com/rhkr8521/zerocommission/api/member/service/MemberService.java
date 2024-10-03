package com.rhkr8521.zerocommission.api.member.service;

import com.rhkr8521.zerocommission.api.member.dto.*;
import com.rhkr8521.zerocommission.api.member.entity.*;
import com.rhkr8521.zerocommission.api.member.jwt.service.JwtService;
import com.rhkr8521.zerocommission.api.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtService jwtService;
    private final OAuthService oAuthService;

    @Transactional
    public Map<String, Object> loginWithKakao(String kakaoAccessToken) {
        // 카카오 Access Token을 이용해 사용자 정보 가져오기
        KakaoUserInfoDTO kakaoUserInfo = oAuthService.getKakaoUserInfo(kakaoAccessToken);

        // 사용자 정보를 저장
        Member member = registerOrLoginKakaoUser(kakaoUserInfo);

        // 엑세스,리프레시 토큰 생성
        Map<String, String> tokens = jwtService.createAccessAndRefreshToken(member.getEmail());

        Map<String, Object> response = new HashMap<>();
        response.put("tokens", tokens);
        response.put("role", member.getRole());

        return response;
    }

    // 카카오 사용자 정보를 사용해 회원가입 또는 로그인 처리
    public Member registerOrLoginKakaoUser(KakaoUserInfoDTO kakaoUserInfo) {
        // 카카오 사용자 ID로 사용자 조회
        return memberRepository.findBySocialId(kakaoUserInfo.getId())
                .orElseGet(() -> registerNewKakaoUser(kakaoUserInfo));  // 없으면 새 사용자 등록
    }

    // 새로운 카카오 사용자 등록
    private Member registerNewKakaoUser(KakaoUserInfoDTO kakaoUserInfo) {
        Member member = Member.builder()
                .socialId(kakaoUserInfo.getId())
                .email(UUID.randomUUID() + "@socialUser.com")
                .nickname(kakaoUserInfo.getNickname())
                .role(Role.GUEST)
                .build();

        memberRepository.save(member);
        return member;
    }

}
