package ru.msu.prak_2022.models;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "course_hub")
@IdClass(CourseId.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    @NonNull
    private long course_id;

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
}
