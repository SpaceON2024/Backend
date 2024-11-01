package com.rhkr8521.spaceon.api.group.entity;

import com.rhkr8521.spaceon.api.member.entity.Member;
import com.rhkr8521.spaceon.common.entity.BaseTimeEntity;
import lombok.*;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder(toBuilder = true)  // toBuilder 옵션을 사용하여 기존 객체를 복사하는 빌더 생성 가능
@Table(name = "TEAM_GROUP")
@AllArgsConstructor
public class Group extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long id;

    private String groupName;
    private String groupContent;

    @OneToMany
    @JoinTable(
            name = "GROUP_MEMBERS",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id")
    )
    private List<Member> members;

    @OneToMany
    @JoinTable(
            name = "GROUP_ADMINS",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "admin_id")
    )
    private List<Member> admins;

    // 일반 멤버 추가 메소드
    public void addMember(Member member) {
        if (this.members == null) {
            this.members = new ArrayList<>();
        }
        this.members.add(member);
    }

    // 관리자 멤버 추가 메소드
    public void addAdmin(Member admin) {
        if (this.admins == null) {
            this.admins = new ArrayList<>();
        }
        this.admins.add(admin);
    }

    @PrePersist
    @PreUpdate
    private void ensureListsInitialized() {
        if (this.members == null) {
            this.members = new ArrayList<>();
        }
        if (this.admins == null) {
            this.admins = new ArrayList<>();
        }
    }
}
