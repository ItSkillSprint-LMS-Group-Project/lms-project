package com.example.lmsproject.content.mapper;

import com.example.lmsproject.content.entity.Content;
import com.example.lmsproject.content.dto.request.ContentCreateRequest;
import com.example.lmsproject.content.dto.response.ContentResponse;
import com.example.lmsproject.content.dto.request.ContentUpdateRequest;
import com.example.lmsproject.course.entity.Course;
import java.util.List;
public class ContentMapper {
    public static Content toEntity(ContentCreateRequest request, Course course) {
        Content content = new Content();
        content.setTitle(request.getTitle());
        content.setType(request.getType());
        content.setText(request.getText());
        content.setCourse(course);
        return content;
    }

    public static ContentResponse toResponse(Content content) {
        ContentResponse response = new ContentResponse();
        response.setId(content.getId());
        response.setTitle(content.getTitle());
        response.setType(content.getType());
        response.setText(content.getText());
        response.setCourseId(content.getCourse().getId());
        response.setCourseTitle(content.getCourse().getTitle());
        return response;
    }

    public static List<ContentResponse> toResponseList(List<Content> contents) {
        return contents.stream().map(ContentMapper::toResponse).toList();
    }

    public static void updateEntity(Content content, ContentUpdateRequest request) {
        if (request.getTitle() != null) {
            content.setTitle(request.getTitle());
        }
        if (request.getType() != null) {
            content.setType(request.getType());
        }
        if (request.getText() != null) {
            content.setText(request.getText());
        }

    }
}