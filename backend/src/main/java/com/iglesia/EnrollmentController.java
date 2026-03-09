package com.iglesia;

import com.iglesia.factory.PaymentFactory;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private final EnrollmentRepository enrollmentRepository;
    private final PersonRepository personRepository;
    private final CourseRepository courseRepository;
    private final PaymentRepository paymentRepository;
    private final ChurchRepository churchRepository;

    public EnrollmentController(EnrollmentRepository enrollmentRepository,
                                PersonRepository personRepository,
                                CourseRepository courseRepository,
                                PaymentRepository paymentRepository,
                                ChurchRepository churchRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.personRepository = personRepository;
        this.courseRepository = courseRepository;
        this.paymentRepository = paymentRepository;
        this.churchRepository = churchRepository;
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('CLIENT')")
    @PostMapping
    public EnrollmentResponse create(@RequestBody EnrollmentRequest request) {

        Church church = ChurchUtils.requireChurch(churchRepository);

        Person person = personRepository.findById(request.personId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Persona no encontrada"));

        Course course = courseRepository.findById(request.courseId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso no encontrado"));

        if (!person.getChurch().getId().equals(church.getId())
                || !course.getChurch().getId().equals(church.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Datos no pertenecen a la iglesia");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setPerson(person);
        enrollment.setCourse(course);
        enrollment.setStatus(EnrollmentStatus.PENDIENTE);

        enrollmentRepository.save(enrollment);

        // Factory Pattern aplicado aquí
        Payment payment = PaymentFactory.createPayment(
                PaymentType.INSCRIPCION_CURSO,
                course.getPrice(),
                enrollment.getId()
        );

        paymentRepository.save(payment);

        enrollment.setPaymentId(payment.getId());
        enrollmentRepository.save(enrollment);

        return EnrollmentResponse.from(enrollment, payment);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('CLIENT')")
    @GetMapping
    public List<EnrollmentResponse> list() {

        Church church = ChurchUtils.requireChurch(churchRepository);

        return enrollmentRepository.findAllByPersonChurchId(church.getId())
                .stream()
                .map(enrollment -> {

                    Payment payment = null;

                    if (enrollment.getPaymentId() != null) {
                        payment = paymentRepository.findById(enrollment.getPaymentId()).orElse(null);
                    }

                    return EnrollmentResponse.from(enrollment, payment);

                })
                .toList();
    }

    public record EnrollmentRequest(
            @NotNull Long personId,
            @NotNull Long courseId
    ) {}

    public record EnrollmentResponse(
            Long id,
            Long personId,
            String personName,
            Long courseId,
            String courseName,
            String status,
            Long paymentId,
            String paymentStatus
    ) {

        public static EnrollmentResponse from(Enrollment enrollment, Payment payment) {

            String personName = enrollment.getPerson().getFirstName() + " " + enrollment.getPerson().getLastName();
            String paymentStatus = payment == null ? null : payment.getStatus().name();

            return new EnrollmentResponse(
                    enrollment.getId(),
                    enrollment.getPerson().getId(),
                    personName,
                    enrollment.getCourse().getId(),
                    enrollment.getCourse().getName(),
                    enrollment.getStatus().name(),
                    enrollment.getPaymentId(),
                    paymentStatus
            );
        }
    }
}