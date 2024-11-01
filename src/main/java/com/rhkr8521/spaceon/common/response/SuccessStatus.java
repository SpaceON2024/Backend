package com.rhkr8521.spaceon.common.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum SuccessStatus {

    /**
     * 200
     */
    SEND_KAKAO_ACCESSTOKEN_SUCCESS(HttpStatus.OK,"카카오 엑세스토큰 발급 성공"),
    SEND_LOGIN_SUCCESS(HttpStatus.OK, "로그인 성공"),
    SEND_REISSUE_TOKEN_SUCCESS(HttpStatus.OK,"토큰 재발급 성공"),
    JOIN_GROUP_SUCEESS(HttpStatus.OK, "그룹 참가 신청 성공"),
    APPROVE_JOIN_SUCCESS(HttpStatus.OK, "그룹 참가 승인 성공"),


    /**
     * 201
     */
    CREATE_GROUP_SUCCESS(HttpStatus.CREATED, "그룹 생성 성공"),
    CREATE_RESERVATION_SUCCESS(HttpStatus.CREATED, "예약 생성 성공"),

    ;

    private final HttpStatus httpStatus;
    private final String message;

    public int getStatusCode() {
        return this.httpStatus.value();
    }
}