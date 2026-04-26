package com.example.lmsproject.content.dto.response;
import com.example.lmsproject.content.entity.ContentType;
import lombok.Data;
@Data
public class ContentSummaryResponse {
    private Long id;
    private String title;
    private ContentType type;
}
