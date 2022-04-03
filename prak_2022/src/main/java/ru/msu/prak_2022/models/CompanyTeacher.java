package ru.msu.prak_2022.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "company_teacher_link")
@IdClass(CompanyTeacherId.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CompanyTeacher {

    @Id
    @Column(updatable = false, insertable = false)
    @NonNull
    private long teacher_id;

    @Id
    @Column(updatable = false, insertable = false)
    @NonNull
    private long company_id;

    @ManyToOne
    @NonNull
    @JoinColumn(name = "teacher_id", referencedColumnName = "teacher_id")
    private Teacher teacher;

    @ManyToOne
    @NonNull
    @JoinColumn(name = "company_id", referencedColumnName = "company_id")
    private Company company;

    @Column(name = "is_approved")
    @NonNull
    private boolean is_approved;
}
