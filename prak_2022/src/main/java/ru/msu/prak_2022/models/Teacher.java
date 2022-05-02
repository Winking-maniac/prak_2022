package ru.msu.prak_2022.models;

import lombok.*;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "teacher_hub")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Teacher {
    public Teacher(@NonNull String username,
                   @NonNull String surname,
                   @NonNull String first_name) {
        this(username, surname, first_name, "", "");
    }

    public Teacher(@NonNull String username,
                   @NonNull String surname,
                   @NonNull String first_name,
                   String last_name,
                   String description) {
        this.username = username;
        this.surname = surname;
        this.first_name = first_name;
        this.last_name = last_name;
        this.description = description;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teacher_id")
    @NonNull
    private Long teacher_id;

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

    @OneToMany(mappedBy = "teacher")
    private List<CompanyTeacher> companies;

    @OneToMany(mappedBy = "teacher")
    private List<TeacherCourse> courses;

    @OneToMany(mappedBy = "course")
    private List<Lesson> lessons;

    @Formula(value = " concat(surname, ' ', first_name, ' ', last_name) ")
    private String fullName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Teacher)) return false;
        Teacher teacher = (Teacher) o;
        return getTeacher_id().equals(teacher.getTeacher_id()) && getSurname().equals(teacher.getSurname()) && getFirst_name().equals(teacher.getFirst_name()) && Objects.equals(getLast_name(), teacher.getLast_name()) && Objects.equals(getDescription(), teacher.getDescription()) && Objects.equals(getUsername(), teacher.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTeacher_id(), getSurname(), getFirst_name(), getLast_name(), getDescription(), getUsername());
    }
}
