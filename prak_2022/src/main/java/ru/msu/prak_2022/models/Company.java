package ru.msu.prak_2022.models;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "company_hub")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    @NonNull
    private long company_id;

    @Column(name = "company_name")
    @NonNull
    private String company_name;

    @Column(name = "address")
    @NonNull
    private String address;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "company")
    private List<CompanyTeacher> teachers;

    @OneToMany(mappedBy = "company")
    private List<CompanyCourse> courses;
}
