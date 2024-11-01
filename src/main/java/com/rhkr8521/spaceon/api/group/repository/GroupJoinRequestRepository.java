package com.rhkr8521.spaceon.api.group.repository;

import com.rhkr8521.spaceon.api.group.entity.GroupJoinRequest;
import com.rhkr8521.spaceon.api.group.entity.Group;
import com.rhkr8521.spaceon.api.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupJoinRequestRepository extends JpaRepository<GroupJoinRequest, Long> {
    // 특정 그룹에 대한 특정 회원의 요청을 찾기
    boolean existsByGroupAndMemberAndStatus(Group group, Member member, GroupJoinRequest.RequestStatus status);

    // 그룹에 대한 모든 요청 찾기
    List<GroupJoinRequest> findByGroup(Group group);
}
