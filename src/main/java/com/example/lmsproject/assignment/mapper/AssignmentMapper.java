package com.example.lmsproject.assignment.mapper;

import com.example.lmsproject.assignment.dto.request.AssignmentSubmissionRequest;
import com.example.lmsproject.assignment.dto.response.AssignmentSubmissionResponse;
import com.example.lmsproject.assignment.entity.Assignment;
import com.example.lmsproject.assignment.dto.request.AssignmentRequest;
import com.example.lmsproject.assignment.dto.response.AssignmentResponse;
import com.example.lmsproject.assignment.dto.request.AssignmentUpdateRequest;
import com.example.lmsproject.assignment.entity.AssignmentSubmission;
import com.example.lmsproject.course.entity.Course;
import com.example.lmsproject.user.entity.User;

import java.util.List;

public class AssignmentMapper {
    public static Assignment toEntity(AssignmentRequest request, Course course) {
        Assignment assignment = new Assignment();
        assignment.setTitle(request.getTitle());
        assignment.setDescription(request.getDescription());
        assignment.setDueDate(request.getDueDate());
        assignment.setCourse(course);
        return assignment;
    }

    public static AssignmentResponse toResponse(Assignment assignment) {
        AssignmentResponse response = new AssignmentResponse();
        response.setId(assignment.getId());
        response.setTitle(assignment.getTitle());
        response.setDescription(assignment.getDescription());
        response.setDueDate(assignment.getDueDate());
        response.setCourseId(assignment.getCourse().getId());
        response.setCourseTitle(assignment.getCourse().getTitle());
        return response;
    }

    public static List<AssignmentResponse> toResponseList(List<Assignment> assignments) {
        return assignments.stream()
                .map(AssignmentMapper::toResponse)
                .toList();
    }

    public static void updateEntity(Assignment assignment, AssignmentUpdateRequest request) {
        if (request.getTitle() != null) {
            assignment.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            assignment.setDescription(request.getDescription());
        }
        if (request.getDueDate() != null) {
            assignment.setDueDate(request.getDueDate());
        }
    }
    public static AssignmentSubmission toSubmissionEntity(AssignmentSubmissionRequest request, Assignment assignment, User student){
        AssignmentSubmission assignmentSubmission=new AssignmentSubmission();
        assignmentSubmission.setAssignment(assignment);
        assignmentSubmission.setSubmissionText(request.getSubmissionText());
        assignmentSubmission.setStudent(student);
        return assignmentSubmission;
    }
    public static AssignmentSubmissionResponse toSubmissionResponse(AssignmentSubmission assignmentSubmission){
        AssignmentSubmissionResponse response=new AssignmentSubmissionResponse();
        response.setId(assignmentSubmission.getId());
        response.setSubmissionText(assignmentSubmission.getSubmissionText());
        response.setSubmittedAt(assignmentSubmission.getSubmittedAt());
        response.setFeedback(assignmentSubmission.getFeedback());
        response.setGrade(assignmentSubmission.getGrade());
        response.setAssignmentTitle(assignmentSubmission.getAssignment().getTitle());
        response.setAssignmentId(assignmentSubmission.getAssignment().getId());
        response.setStudentId(assignmentSubmission.getStudent().getId());
        response.setStudentEmail(assignmentSubmission.getStudent().getEmail());
        return response;
    }

}
