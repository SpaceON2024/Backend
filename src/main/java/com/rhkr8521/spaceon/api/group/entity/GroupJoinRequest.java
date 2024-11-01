package com.rhkr8521.spaceon.api.group.entity;

import com.rhkr8521.spaceon.api.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder(toBuilder = true) // Builder 패턴을 활성화합니다.
@Table(name = "group_join_request")
@AllArgsConstructor // 모든 필드를 포함하는 생성자 생성
public class GroupJoinRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 그룹 정보
    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    // 요청한 멤버 정보
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // 요청 상태
    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    // 요청 상태 Enum
    public enum RequestStatus {
        PENDING,
        APPROVED,
        REJECTED
    }

    // 생성자 예시 - Builder와 함께 사용할 경우
    public static GroupJoinRequest create(Group group, Member member) {
        return GroupJoinRequest.builder()
                .group(group)
                .member(member)
                .status(RequestStatus.PENDING) // 기본 상태 설정
                .build();
    }
}
