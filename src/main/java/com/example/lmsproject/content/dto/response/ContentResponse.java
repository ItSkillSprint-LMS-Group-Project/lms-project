package com.example.lmsproject.content.dto.response;

import com.example.lmsproject.content.entity.ContentType;
import lombok.Data;

@Data
public class ContentResponse {
    private Long id;
    private String title;
    private ContentType type;
    private String text;
    private Integer orderIndex;
    private Long courseId;
    private String courseTitle;
}
