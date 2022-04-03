package ru.msu.prak_2022.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "company_course_link")
@IdClass(CompanyCourseId.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CompanyCourse {

    @Id
    @Column(updatable = false, insertable = false)
    @NonNull
    private long course_id;

    @Id
    @Column(updatable = false, insertable = false)
    @NonNull
    private long company_id;

    @ManyToOne
    @NonNull
    @JoinColumn(name = "course_id", referencedColumnName = "course_id")
    private Course course;

    @ManyToOne
    @NonNull
    @JoinColumn(name = "company_id", referencedColumnName = "company_id")
    private Company company;

    @Column(name = "is_author")
    @NonNull
    private boolean is_author;
}
