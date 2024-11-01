package com.rhkr8521.spaceon.api.group.repository;

import com.rhkr8521.spaceon.api.group.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
}
