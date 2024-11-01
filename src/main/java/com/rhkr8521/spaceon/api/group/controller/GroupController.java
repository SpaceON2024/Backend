package com.rhkr8521.spaceon.api.group.controller;

import com.rhkr8521.spaceon.api.group.dto.GroupCreateRequestDTO;
import com.rhkr8521.spaceon.api.group.service.GroupService;
import com.rhkr8521.spaceon.api.member.service.MemberService;
import com.rhkr8521.spaceon.common.exception.BadRequestException;
import com.rhkr8521.spaceon.common.response.ApiResponse;
import com.rhkr8521.spaceon.common.response.ErrorStatus;
import com.rhkr8521.spaceon.common.response.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Group", description = "Group 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/group")
public class GroupController {

    private final GroupService groupService;
    private final MemberService memberService;

    // 그룹 생성 API
    @Operation(
            summary = "그룹 생성 API",
            description = "그룹을 생성하는 API입니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "그룹 생성 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "그룹 이름이 입력되지 않았습니다."),
    })
    @PostMapping("/new")
    public ResponseEntity<ApiResponse<Map<String, Object>>> groupCreate(@RequestBody GroupCreateRequestDTO groupCreateRequestDTO,
                                                                        @AuthenticationPrincipal UserDetails userDetails) {
        if (groupCreateRequestDTO == null || groupCreateRequestDTO.getGroupName() == null || groupCreateRequestDTO.getGroupName().isEmpty()) {
            throw new BadRequestException(ErrorStatus.MISSING_KAKAO_ACCESSTOKEN.getMessage());
        }

        Long userId = memberService.getUserIdByEmail(userDetails.getUsername());
        Map<String, Object> response = groupService.createGroup(groupCreateRequestDTO, userId);

        return ApiResponse.success(SuccessStatus.CREATE_GROUP_SUCCESS, response);
    }

    // 그룹 참가 요청 API
    @Operation(
            summary = "그룹 참여 API",
            description = "해당 그룹에 참여하는 API입니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "그룹 참가 신청 성공"),
    })
    @GetMapping("/join")
    public ResponseEntity<ApiResponse<Void>> joinGroup(
            @RequestParam Long groupId,
            @AuthenticationPrincipal UserDetails userDetails) {

        Long userId = memberService.getUserIdByEmail(userDetails.getUsername());
        groupService.joinGroup(groupId, userId);

        return ApiResponse.success_only(SuccessStatus.JOIN_GROUP_SUCEESS);
    }

    // 관리자 승인 API
    @Operation(
            summary = "관리자 승인 API",
            description = "승인 대기중인 멤버 승인하는 API입니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "그룹 참가 승인 성공"),
    })
    @GetMapping("/approve")
    public ResponseEntity<ApiResponse<String>> approveJoinRequest(
            @RequestParam Long requestId,
            @RequestParam boolean approve,
            @AuthenticationPrincipal UserDetails userDetails) {

        Long adminId = memberService.getUserIdByEmail(userDetails.getUsername());
        groupService.approveJoinRequest(requestId, adminId, approve);

        return ApiResponse.success(SuccessStatus.APPROVE_JOIN_SUCCESS, approve ? "승인됨" : "거부됨");
    }

    // 그룹 목록 조회 API
    @Operation(
            summary = "모든 그룹 목록 조회 API",
            description = "생성된 모든 그룹 목록을 조회하는 API입니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "그룹 목록 조회 성공"),
    })
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<Map<String, Object>>> listGroups(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Map<String, Object> response = groupService.listGroups(page, size);
        return ApiResponse.success(SuccessStatus.SEND_LIST_GROUP_SUCCESS, response);
    }
}
