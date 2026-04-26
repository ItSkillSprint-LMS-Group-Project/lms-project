package com.example.lmsproject.content.service;
import com.example.lmsproject.content.entity.Content;
import com.example.lmsproject.content.repository.ContentRepository;
import com.example.lmsproject.content.dto.request.ContentCreateRequest;
import com.example.lmsproject.content.dto.response.ContentResponse;
import com.example.lmsproject.content.dto.request.ContentUpdateRequest;
import com.example.lmsproject.content.mapper.ContentMapper;
import com.example.lmsproject.course.entity.Course;
import com.example.lmsproject.course.repository.CourseRepository;
import com.example.lmsproject.enrollment.repository.EnrollmentRepository;
import com.example.lmsproject.exception.ResourceNotFoundException;
import com.example.lmsproject.exception.UnauthorizedException;
import jakarta.transaction.Transactional;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.List;
@Service
public class ContentService {
    private final ContentRepository contentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    public ContentService(ContentRepository contentRepository,
                          CourseRepository courseRepository,
                          EnrollmentRepository enrollmentRepository) {
        this.contentRepository = contentRepository;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
    }
    @Transactional
    public ContentResponse createContent(ContentCreateRequest request, Long teacherId) {
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        if (!course.getTeacher().getId().equals(teacherId)) {
            throw new UnauthorizedException("Unauthorized");
        }
        Content content = ContentMapper.toEntity(request, course);
        return ContentMapper.toResponse(contentRepository.save(content));
    }
    public ContentResponse getContentById(Long id, Long userId, Collection<? extends GrantedAuthority> authorities) {
        Content content = findContent(id);
        boolean isTeacher = authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_TEACHER"));
        boolean isStudent = authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_STUDENT"));
        if (isTeacher) {
            if (!content.getCourse().getTeacher().getId().equals(userId)) {
                throw new UnauthorizedException("Unauthorized");
            }
        } else if (isStudent) {
            if (!enrollmentRepository.existsByCourseIdAndStudentId(content.getCourse().getId(), userId)) {
                throw new UnauthorizedException("Unauthorized");
            }
        }
        return ContentMapper.toResponse(content);
    }
    public List<ContentResponse> getContentsByCourse(Long courseId) {
        return ContentMapper.toResponseList(contentRepository.findByCourseIdOrderByOrderIndexAsc(courseId));
    }
    @Transactional
    public ContentResponse updateContent(Long id, ContentUpdateRequest request, Long teacherId) {
        Content content = findContent(id);
        if (!content.getCourse().getTeacher().getId().equals(teacherId)) {
            throw new UnauthorizedException("Unauthorized");
        }
        ContentMapper.updateEntity(content, request);
        return ContentMapper.toResponse(contentRepository.save(content));
    }
    @Transactional
    public void deleteContent(Long id, Long teacherId) {
        Content content = findContent(id);
        if (!content.getCourse().getTeacher().getId().equals(teacherId)) {
            throw new UnauthorizedException("Unauthorized");
        }
        contentRepository.delete(content);
    }
    private Content findContent(Long id) {
        return contentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Content not found"));
    }
}
