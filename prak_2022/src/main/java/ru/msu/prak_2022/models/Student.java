package ru.msu.prak_2022.models;

import lombok.*;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "student_hub")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    @NonNull
    private Long student_id;

    @Column
    @NonNull
    private String username;

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
    private Set<StudentCourse> courses = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student student)) return false;
        return getStudent_id().equals(student.getStudent_id()) && getSurname().equals(student.getSurname()) && getFirst_name().equals(student.getFirst_name()) && Objects.equals(getLast_name(), student.getLast_name()) && Objects.equals(getDescription(), student.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStudent_id(), getSurname(), getFirst_name(), getLast_name(), getDescription());
    }
}
