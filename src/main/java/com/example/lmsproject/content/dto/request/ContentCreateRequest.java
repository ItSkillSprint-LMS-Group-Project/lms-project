package com.example.lmsproject.content.dto.request;

import com.example.lmsproject.content.entity.ContentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ContentCreateRequest {
    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "Type is required")
    private ContentType type;

    private String text;

    private Integer orderIndex;

    @NotNull(message = "Course ID is required")
    private Long courseId;
}
