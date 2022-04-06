package ru.msu.prak_2022.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "teacher_course_link")
@IdClass(TeacherCourseId.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TeacherCourse {
    public TeacherCourse(Long course_id, Long teacher_id, boolean is_admin) {
        this.course_id = course_id;
        this.teacher_id = teacher_id;
        this.is_admin = is_admin;
    }
    @Id
    @Column(updatable = false, insertable = false)
    @NonNull
    private Long course_id;

    @Id
    @Column(updatable = false, insertable = false)
    @NonNull
    private Long teacher_id;

    @ManyToOne
    @NonNull
    @JoinColumn(name = "teacher_id", referencedColumnName = "teacher_id")
    private Teacher teacher;

    @ManyToOne
    @NonNull
    @JoinColumn(name = "course_id", referencedColumnName = "course_id")
    private Course course;

    @Column(name = "is_admin")
    private boolean is_admin;
}
