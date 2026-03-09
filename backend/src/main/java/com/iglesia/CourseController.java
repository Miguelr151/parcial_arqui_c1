package com.iglesia;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseRepository courseRepository;
    private final CourseService courseService;
    private final ChurchRepository churchRepository;

    public CourseController(
            CourseRepository courseRepository,
            ChurchRepository churchRepository,
            CourseService courseService
    ) {
        this.courseRepository = courseRepository;
        this.churchRepository = churchRepository;
        this.courseService = courseService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public CourseResponse create(@RequestBody CourseRequest request) {

        Church church = ChurchUtils.requireChurch(churchRepository);

        Course course = new Course();
        course.setName(request.name());
        course.setDescription(request.description());
        course.setPrice(request.price());
        course.setChurch(church);

        courseService.save(course);

        return CourseResponse.from(course);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('CLIENT')")
    @GetMapping
    public List<CourseResponse> list() {

        Church church = ChurchUtils.requireChurch(churchRepository);

        return courseService.findByChurch(church.getId())
                .stream()
                .map(CourseResponse::from)
                .toList();
    }

    public record CourseRequest(
            @NotBlank String name,
            String description,
            @NotNull BigDecimal price
    ) {}

    public record CourseResponse(
            Long id,
            String name,
            String description,
            BigDecimal price,
            boolean active
    ) {
        public static CourseResponse from(Course course) {
            return new CourseResponse(
                    course.getId(),
                    course.getName(),
                    course.getDescription(),
                    course.getPrice(),
                    course.isActive()
            );
        }
    }
}