package ru.msu.prak_2022.models;

import lombok.*;
import org.springframework.beans.factory.annotation.Required;

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
    public CompanyTeacher(@NonNull Long teacher_id,
                   @NonNull Long company_id,
                   boolean is_approved) {
        this.company_id = company_id;
        this.teacher_id = teacher_id;
        this.is_approved = is_approved;
    }

    @Id
    @Column
    @NonNull
    private Long teacher_id;

    @Id
    @Column
    @NonNull
    private Long company_id;

    @ManyToOne
    @NonNull
    @JoinColumn(name = "teacher_id", referencedColumnName = "teacher_id", updatable = false, insertable = false)
    private Teacher teacher;

    @ManyToOne
    @NonNull
    @JoinColumn(name = "company_id", referencedColumnName = "company_id", updatable = false, insertable = false)
    private Company company;

    @Column(name = "is_approved")
    @NonNull
    private boolean is_approved;
}
