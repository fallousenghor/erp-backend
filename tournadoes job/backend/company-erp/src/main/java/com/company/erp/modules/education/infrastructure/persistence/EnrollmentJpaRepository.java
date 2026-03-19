package com.company.erp.modules.education.infrastructure.persistence;

import com.company.erp.modules.education.domain.model.CourseModule;
import com.company.erp.modules.education.domain.model.Enrollment;
import com.company.erp.modules.education.domain.model.Student;
import com.company.erp.modules.education.domain.model.TrainingProgram;
import com.company.erp.modules.education.domain.model.valueobject.EnrollmentStatus;
import com.company.erp.modules.education.domain.repository.EnrollmentRepository;
import com.company.erp.modules.education.domain.repository.GradeStatsProjection;
import com.company.erp.modules.education.domain.repository.StudentAverageProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EnrollmentJpaRepository
extends JpaRepository<Enrollment, UUID>, JpaSpecificationExecutor<Enrollment>, EnrollmentRepository {

    boolean existsByStudentIdAndProgramId(UUID studentId, UUID programId);
    Page<Enrollment> findByStudentId(UUID studentId, Pageable pageable);
    Page<Enrollment> findByProgramId(UUID programId, Pageable pageable);
    long countByProgramIdAndStatus(UUID programId, EnrollmentStatus status);
    long countByStatus(EnrollmentStatus status);

    @Query("""
            SELECT e FROM Enrollment e
            LEFT JOIN FETCH e.student
            LEFT JOIN FETCH e.program
            LEFT JOIN FETCH e.moduleGrades mg
            LEFT JOIN FETCH mg.module
            WHERE e.id = :id
            """)
    Optional<Enrollment> findByIdWithDetails(@Param("id") UUID id);

@Query("""
        SELECT e.student.id as studentId,
            e.student.firstName,
            e.student.lastName,
            e.student.email,
            e.program.title as programTitle,
            '' as avatarUrl,
            AVG(e.finalAverage) as average,
            COUNT(e.moduleGrades) as gradesCount
        FROM Enrollment e
        WHERE (:programId IS NULL OR e.program.id = :programId)
        GROUP BY e.student.id, e.student.firstName, e.student.lastName, e.student.email, e.program.title
        ORDER BY average DESC
        """)
    List<StudentAverageProjection> findStudentAveragesByProgramId(@Param("programId") UUID programId);

    @Query("""
        SELECT AVG(mg.grade.score) as average,
            MAX(mg.grade.score) as highest,
            MIN(mg.grade.score) as lowest,
            (COUNT(CASE WHEN mg.grade.score >= 10 THEN 1 END) * 100.0 / COUNT(*)) as passRate,
            COUNT(DISTINCT mg.enrollment.student) as totalStudents
        FROM ModuleGrade mg
        JOIN mg.enrollment e
        WHERE (:programId IS NULL OR e.program.id = :programId)
        """)
    GradeStatsProjection computeStatsByProgramId(@Param("programId") UUID programId);
}
