package com.example.lmsproject.content.dto.request;

import com.example.lmsproject.content.entity.ContentType;
import lombok.Data;

@Data
public class ContentUpdateRequest {
    private String title;
    private ContentType type;
    private String text;
}
