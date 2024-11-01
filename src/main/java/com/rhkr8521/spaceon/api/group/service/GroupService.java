package com.rhkr8521.spaceon.api.group.service;

import com.rhkr8521.spaceon.api.group.dto.GroupCreateRequestDTO;
import com.rhkr8521.spaceon.api.group.dto.GroupListResponseDTO;
import com.rhkr8521.spaceon.api.group.entity.Group;
import com.rhkr8521.spaceon.api.group.entity.GroupJoinRequest;
import com.rhkr8521.spaceon.api.group.repository.GroupJoinRequestRepository;
import com.rhkr8521.spaceon.api.group.repository.GroupRepository;
import com.rhkr8521.spaceon.api.member.entity.Member;
import com.rhkr8521.spaceon.api.member.repository.MemberRepository;
import com.rhkr8521.spaceon.common.exception.BadRequestException;
import com.rhkr8521.spaceon.common.exception.NotFoundException;
import com.rhkr8521.spaceon.common.response.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final MemberRepository memberRepository;
    private final GroupJoinRequestRepository groupJoinRequestRepository;

    public Map<String, Object> createGroup(GroupCreateRequestDTO groupCreateRequestDTO, Long userId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.USER_NOTFOUND_EXCEPTION.getMessage()));

        Group group = Group.builder()
                .groupName(groupCreateRequestDTO.getGroupName())
                .groupContent(groupCreateRequestDTO.getGroupContent())
                .build();
        group.addAdmin(member);   // 사용자 추가를 관리자 추가
        groupRepository.save(group);

        Map<String, Object> response = new HashMap<>();
        response.put("groupId", group.getId());

        return response;
    }

    // 그룹 참가 요청
    public void joinGroup(Long groupId, Long userId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.GROUP_NOTFOUND_EXCEPTION.getMessage()));

        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.USER_NOTFOUND_EXCEPTION.getMessage()));

        // 사용자가 이미 그룹의 구성원인지 확인
        if (group.getMembers().contains(member)) {
            throw new BadRequestException(ErrorStatus.ALREADY_GROUP_MEMBER.getMessage());
        }

        // 기존의 대기 중인 요청이 있는지 확인
        if (groupJoinRequestRepository.existsByGroupAndMemberAndStatus(group, member, GroupJoinRequest.RequestStatus.PENDING)) {
            throw new BadRequestException(ErrorStatus.REQUEST_ALREADY_PENDING.getMessage());
        }

        // 참가 요청 생성
        GroupJoinRequest joinRequest = GroupJoinRequest.create(group, member);
        groupJoinRequestRepository.save(joinRequest);
    }

    public void approveJoinRequest(Long requestId, Long adminId, boolean approve) {
        GroupJoinRequest joinRequest = groupJoinRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.REQUEST_ID_NOTFOUND_EXCEPTION.getMessage()));

        // 관리자가 승인할 수 있는지 확인 (adminId가 그룹 관리자 목록에 있어야 함)
        Group group = joinRequest.getGroup();
        Member adminMember = memberRepository.findById(adminId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.USER_NOTFOUND_EXCEPTION.getMessage()));

        if (!group.getAdmins().contains(adminMember)) {
            throw new NotFoundException(ErrorStatus.NOT_GROUP_ADMIN_EXEPCTION.getMessage());
        }

        // 요청 상태 업데이트
        GroupJoinRequest updatedRequest = joinRequest.toBuilder()
                .status(approve ? GroupJoinRequest.RequestStatus.APPROVED : GroupJoinRequest.RequestStatus.REJECTED)
                .build();

        groupJoinRequestRepository.save(updatedRequest);

        // 만약 승인되었다면 해당 멤버를 그룹 멤버에 추가
        if (approve) {
            group.addMember(joinRequest.getMember()); // 그룹에 멤버 추가
            groupRepository.save(group); // 그룹 정보 업데이트
        }
    }

    // 그룹 목록 조회
    public Map<String, Object> listGroups(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Group> groupPage = groupRepository.findAll(pageable);

        List<GroupListResponseDTO> groups;
        groups = groupPage.getContent().stream()
                .map(group -> new GroupListResponseDTO(group.getId(), group.getGroupName(), group.getGroupContent()))
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("groups", groups);
        response.put("last", groupPage.isLast());

        return response;
    }
}
