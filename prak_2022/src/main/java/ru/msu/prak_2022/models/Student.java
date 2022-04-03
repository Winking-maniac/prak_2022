package ru.msu.prak_2022.models;

import lombok.*;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "student_hub")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    @NonNull
    private long student_id;

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

    @Formula(value = " concat(surname, ' ', first_name, ' ', last_name) ")
    private String fullName;

    @OneToMany(mappedBy = "student")
    private List<StudentCourse> courses;
}
