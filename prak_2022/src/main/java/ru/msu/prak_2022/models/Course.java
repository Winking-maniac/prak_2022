package ru.msu.prak_2022.models;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "course_hub")
//@IdClass(CourseId.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    public Course(@NonNull Long course_id,
                  @NonNull String course_name,
                  @NonNull Date date_from,
                  @NonNull Date date_till,
                  @NonNull float lesson_intensivity,
                  @NonNull float self_study_intensivity,
                  String description) {
        this.course_id = course_id;
        this.course_name = course_name;
        this.date_from = date_from;
        this.date_till = date_till;
        this.lesson_intensivity= lesson_intensivity;
        this.self_study_intensivity = self_study_intensivity;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    @NonNull
    private Long course_id;

    @Column(name = "course_name")
    @NonNull
    private String course_name;

    @Column(name = "date_from")
    @NonNull
    private Date date_from;

    @Column(name = "date_till")
    @NonNull
    private Date date_till;

    @Column(name = "lesson_intensivity")
    @NonNull
    private float lesson_intensivity;

    @Column(name = "self_study_intensivity")
    @NonNull
    private float self_study_intensivity;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "course")
    private List<CompanyCourse> companies;

    @OneToMany(mappedBy = "course")
    private List<TeacherCourse> teachers;

    @OneToMany(mappedBy = "course")
    private List<StudentCourse> students;

    @OneToMany(mappedBy = "course")
    private List<Lesson> lessons;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course)) return false;
        Course course = (Course) o;
        return Float.compare(course.getLesson_intensivity(), getLesson_intensivity()) == 0 && Float.compare(course.getSelf_study_intensivity(), getSelf_study_intensivity()) == 0 && getCourse_id().equals(course.getCourse_id()) && getCourse_name().equals(course.getCourse_name()) && getDate_from().equals(course.getDate_from()) && getDate_till().equals(course.getDate_till()) && Objects.equals(getDescription(), course.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCourse_id(), getCourse_name(), getDate_from(), getDate_till(), getLesson_intensivity(), getSelf_study_intensivity(), getDescription());
    }
}
