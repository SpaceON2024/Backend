package com.rhkr8521.zerocommission.api.member.service;

import com.rhkr8521.zerocommission.api.member.dto.*;
import com.rhkr8521.zerocommission.api.member.entity.*;
import com.rhkr8521.zerocommission.api.member.jwt.service.JwtService;
import com.rhkr8521.zerocommission.api.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtService jwtService;
    private final OAuthService oAuthService;

    private static final String[] adjectives = {
            "멋진", "화려한", "귀여운", "행복한", "즐거운", "기쁜", "놀라운", "용감한", "지혜로운", "친절한",
            "굉장한", "상냥한", "사랑스러운", "빛나는", "열정적인", "창의적인", "신비로운", "우아한", "차분한",
            "쾌활한", "활발한", "순수한", "명랑한", "유쾌한", "진지한", "따뜻한", "재미있는", "감성적인",
            "긍정적인", "낭만적인", "독특한", "자유로운", "용맹한", "차가운", "뜨거운", "청순한", "카리스마있는",
            "신나는", "다정한", "열심인", "성실한", "믿음직한", "매력적인", "예리한", "명석한", "엄격한",
            "정의로운", "호기심많은", "활기찬", "발랄한", "푸른", "붉은", "노란", "초록의", "검은", "흰",
            "회색의", "은빛의", "황금의", "투명한", "화사한", "아름다운", "빛나는", "찬란한", "매혹적인",
            "기발한", "독창적인", "순진한", "영리한", "영웅적인", "고요한", "달콤한", "향기로운", "즐거운",
            "상쾌한", "편안한", "유연한", "적극적인", "관대한", "도전적인", "기운찬", "낙천적인", "신중한",
            "침착한", "유능한", "혁신적인", "열렬한", "지적인", "풍부한", "성공적인", "친근한", "유쾌한",
            "감동적인", "대담한", "우승한", "현명한", "겸손한", "대단한", "행운의", "영광스러운"
    };

    private static final String[] nouns = {
            "고양이", "강아지", "토끼", "사자", "호랑이", "독수리", "곰", "여우", "늑대", "판다",
            "앞접시", "나무", "바다", "하늘", "산", "구름", "별", "달", "태양", "비", "눈",
            "바람", "꽃", "돌", "강", "호수", "새", "물고기", "나비", "사슴", "코끼리", "기린",
            "원숭이", "여행자", "작가", "화가", "음악가", "마법사", "기사", "용사", "왕", "여왕",
            "공주", "왕자", "요정", "괴물", "도깨비", "유령", "천사", "악마", "해적", "닌자",
            "사무라이", "드래곤", "로봇", "우주인", "탐험가", "모험가", "요리사", "과학자", "탐정",
            "발명가", "선생님", "학생", "의사", "간호사", "경찰", "소방관", "운동선수", "기술자",
            "프로그래머", "디자이너", "모델", "스타", "작곡가", "가수", "배우", "감독", "코미디언",
            "연구원", "언론인", "사진가", "건축가", "철학자", "정치가", "법률가", "판사", "역사가",
            "예술가", "성우", "마술사", "심리학자", "경제학자", "환경운동가", "비행사", "항해사",
            "조각가", "무용가", "시인", "수학자", "물리학자", "화학자", "생물학자", "천문학자",
            "지리학자", "지질학자", "미생물학자", "로봇공학자", "인공지능", "데이터과학자", "음악치료사",
            "동물학자", "식물학자", "해양학자", "기상학자", "범죄학자", "사회학자", "인류학자"
    };

    @Transactional
    public Map<String, Object> loginWithKakao(String kakaoAccessToken) {
        // 카카오 Access Token을 이용해 사용자 정보 가져오기
        KakaoUserInfoDTO kakaoUserInfo = oAuthService.getKakaoUserInfo(kakaoAccessToken);

        // 사용자 정보를 저장
        Member member = registerOrLoginKakaoUser(kakaoUserInfo);

        // 액세스, 리프레시 토큰 생성
        Map<String, String> tokens = jwtService.createAccessAndRefreshToken(member.getEmail());

        Map<String, Object> response = new HashMap<>();
        response.put("tokens", tokens);
        response.put("role", member.getRole());
        response.put("nickname", member.getNickname());
        response.put("profileImage", member.getProfileImage());

        return response;
    }

    // 카카오 사용자 정보를 사용해 회원가입 또는 로그인 처리
    public Member registerOrLoginKakaoUser(KakaoUserInfoDTO kakaoUserInfo) {
        // 카카오 사용자 ID로 사용자 조회
        return memberRepository.findBySocialId(kakaoUserInfo.getId())
                .orElseGet(() -> registerNewKakaoUser(kakaoUserInfo));  // 없으면 새 사용자 등록
    }

    // 새로운 카카오 사용자 등록
    @Transactional
    public Member registerNewKakaoUser(KakaoUserInfoDTO kakaoUserInfo) {
        String baseNickname = generateBaseNickname();

        // 우선 닉네임 없이 회원 생성 (ID를 얻기 위해)
        Member member = Member.builder()
                .socialId(kakaoUserInfo.getId())
                .email(UUID.randomUUID() + "@socialUser.com")
                .kakaoNickname(kakaoUserInfo.getNickname())
                .nickname(null)
                .profileImage(null)
                .role(Role.USER)
                .build();

        memberRepository.save(member);

        // 회원 ID를 이용하여 닉네임 생성
        String nickname = baseNickname + "#" + member.getId();

        // 닉네임 설정 (업데이트된 객체를 저장)
        member = member.updateNickname(nickname);
        memberRepository.save(member);

        return member;
    }

    // 기본 닉네임 생성 (형용사 + 명사 조합)
    private String generateBaseNickname() {
        Random random = new Random();
        String adjective = adjectives[random.nextInt(adjectives.length)];
        String noun = nouns[random.nextInt(nouns.length)];
        return adjective + " " + noun;
    }
}
