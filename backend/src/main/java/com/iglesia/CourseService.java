package com.iglesia;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Course save(Course course) {
        return courseRepository.save(course);
    }

    public List<Course> findByChurch(Long churchId) {
        return courseRepository.findAllByChurchId(churchId);
    }
}