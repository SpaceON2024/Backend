package com.rhkr8521.spaceon.api.group.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GroupListResponseDTO {
    private Long id;
    private String groupName;
    private String groupContent;
}
