package com.rhkr8521.spaceon.api.member.controller;

import com.rhkr8521.spaceon.api.member.dto.*;
import com.rhkr8521.spaceon.api.member.service.MemberService;
import com.rhkr8521.spaceon.api.member.service.OAuthService;
import com.rhkr8521.spaceon.common.exception.BadRequestException;
import com.rhkr8521.spaceon.common.response.ApiResponse;
import com.rhkr8521.spaceon.common.response.ErrorStatus;
import com.rhkr8521.spaceon.common.response.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Member", description = "Member 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberController {

    private final MemberService memberService;
    private final OAuthService oauthService;

    @Operation(
            summary = "[백엔드 용] 카카오 엑세스토큰 발급 API",
            description = "/oauth2/authorization/kakao 엔드포인트를 통해 엑세스토큰을 발급합니다."
    )
    @GetMapping("/accesstoken")
    public ResponseEntity<ApiResponse<String>> getKakaoAccessToken(@RequestParam("code") String code) {
        // 인가 코드를 통해 액세스 토큰 요청
        String kakaoAccessToken = oauthService.getKakaoAccessToken(code);
        return ApiResponse.success(SuccessStatus.SEND_KAKAO_ACCESSTOKEN_SUCCESS, kakaoAccessToken);
    }

    @Operation(
            summary = "로그인 API",
            description = "카카오 엑세스토큰을 통해 사용자의 정보를 등록 및 토큰을 발급합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "로그인 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "카카오 엑세스토큰이 입력되지 않았습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "유효하지 않은 엑세스토큰 입니다.")
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, Object>>> loginWithKakao(@RequestBody KakaoLoginRequestDTO kakaoLoginRequest) {
        // 카카오 엑세스토큰이 입력되지 않았을 경우 예외 처리
        if (kakaoLoginRequest == null || kakaoLoginRequest.getAccessToken() == null || kakaoLoginRequest.getAccessToken().isEmpty()) {
            throw new BadRequestException(ErrorStatus.MISSING_KAKAO_ACCESSTOKEN.getMessage());
        }

        Map<String, Object> response = memberService.loginWithKakao(kakaoLoginRequest.getAccessToken());
        return ApiResponse.success(SuccessStatus.SEND_LOGIN_SUCCESS, response);
    }
}
