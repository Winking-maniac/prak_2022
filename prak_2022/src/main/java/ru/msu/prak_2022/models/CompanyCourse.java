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
    public CompanyCourse(@NonNull Long company_id, @NonNull Long course_id, boolean is_author) {
        this.company_id = company_id;
        this.course_id = course_id;
        this.is_author = is_author;
    }

    @Id
    @Column(updatable = false, insertable = false)
    @NonNull
    private Long course_id;

    @Id
    @Column(updatable = false, insertable = false)
    @NonNull
    private Long company_id;

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
