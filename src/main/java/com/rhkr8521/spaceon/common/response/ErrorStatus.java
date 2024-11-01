package com.rhkr8521.spaceon.common.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import javax.swing.event.HyperlinkEvent;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)

public enum ErrorStatus {
    /**
     * 400 BAD_REQUEST
     */
    VALIDATION_REQUEST_MISSING_EXCEPTION(HttpStatus.BAD_REQUEST, "요청 값이 입력되지 않았습니다."),
    VALIDATION_CONTENT_MISSING_EXCEPTION(HttpStatus.BAD_REQUEST, "필수 정보가 입력되지 않았습니다."),
    MISSING_KAKAO_ACCESSTOKEN(HttpStatus.BAD_REQUEST, "카카오 엑세스토큰이 입력되지 않았습니다."),
    NOT_GROUP_ADMIN_EXEPCTION(HttpStatus.BAD_REQUEST, "해당 그룹 관리자가 아닙니다."),
    ALREADY_GROUP_MEMBER(HttpStatus.BAD_REQUEST,"이미 그룹 참여자입니다."),
    REQUEST_ALREADY_PENDING(HttpStatus.BAD_REQUEST,"현재 그룹 승인 대기중입니다"),

    /**
     * 401 UNAUTHORIZED
     */
    USER_UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"인증되지 않은 사용자입니다."),
    INVALID_KAKAO_ACCESSTOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED, "유효하지 않은 카카오 엑세스토큰입니다."),
    INVALID_REFRESHTOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레시토큰입니다."),

    /**
     * 404 NOT_FOUND
     */

    USER_NOTFOUND_EXCEPTION(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."),
    GROUP_NOTFOUND_EXCEPTION(HttpStatus.NOT_FOUND, "해당 그룹을 찾을 수 없습니다."),
    REQUEST_ID_NOTFOUND_EXCEPTION(HttpStatus.BAD_REQUEST,"그룹 초대 승인 요청을 찾을 수 없습니다."),

    /**
     * 500 SERVER_ERROR
     */

    FAIL_UPLOAD_PROFILE_IMAGE(HttpStatus.INTERNAL_SERVER_ERROR, "프로필 사진이 변경되지 않았습니다."),
    FAIL_REQUEST_KAKAO_USER_INFO(HttpStatus.INTERNAL_SERVER_ERROR, "카카오 사용자 정보 요청 중 오류가 발생했습니다."),
    FAIL_PARSE_KAKAO_USER_INFO(HttpStatus.INTERNAL_SERVER_ERROR, "카카오 사용자 정보를 가져오지 못했습니다."),
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류"),

    ;

    private final HttpStatus httpStatus;
    private final String message;

    public int getStatusCode() {
        return this.httpStatus.value();
    }
}

