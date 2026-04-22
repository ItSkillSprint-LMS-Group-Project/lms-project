package com.example.lmsproject.course.mapper;

import com.example.lmsproject.course.dto.CourseRequest;
import com.example.lmsproject.course.dto.CourseResponse;
import com.example.lmsproject.course.dto.CourseUpdateRequest;
import com.example.lmsproject.course.entity.Course;

import java.util.List;

public class CourseMapper {
    public static Course toEntity(CourseRequest courseRequest)
    {
        Course course= new Course();
        course.setTitle(courseRequest.getTitle());
        course.setDescription(courseRequest.getDescription());
        return course;
    }
    public static CourseResponse toResponse(Course course)
    {
        CourseResponse courseResponse=new CourseResponse();
        courseResponse.setId(course.getId());
        courseResponse.setTeacherFirstName(course.getTeacher().getFirstName());
        courseResponse.setTeacherLastName(course.getTeacher().getLastName());
        courseResponse.setTeacherId(course.getTeacher().getId());
        courseResponse.setCourseCode(course.getCourseCode());
        courseResponse.setCreatedAt(course.getCreatedAt());
        courseResponse.setTitle(course.getTitle());
        courseResponse.setDescription(course.getDescription());
        return courseResponse;
    }
    public static void updateEntity(Course course, CourseUpdateRequest courseUpdateRequest){
        if(courseUpdateRequest.getTitle()!=null){
            course.setTitle(courseUpdateRequest.getTitle());
        }
        if(courseUpdateRequest.getDescription()!=null){
            course.setDescription(courseUpdateRequest.getDescription());
        }
    }
    public static List<CourseResponse> toResponseList(List<Course> courses){
        return courses.stream().map(CourseMapper::toResponse).toList();
    }

}
