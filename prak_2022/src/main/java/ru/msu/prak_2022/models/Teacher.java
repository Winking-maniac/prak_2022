package ru.msu.prak_2022.models;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "teacher_hub")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teacher_id")
    @NonNull
    private long teacher_id;

    @Column(name = "surname")
    @NonNull
    private String surname;

    @Column(name = "first_name")
    @NonNull
    private String first_name;

    @Column(name = "last_name")
    private String last_name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "teacher")
    private List<CompanyTeacher> companies;

    @OneToMany(mappedBy = "teacher")
    private List<TeacherCourse> courses;
}
