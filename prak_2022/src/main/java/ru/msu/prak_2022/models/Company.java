package ru.msu.prak_2022.models;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "company_hub")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Company {
    public Company(@NonNull Long company_id,
                   @NonNull String company_name,
                   @NonNull String address,
                   String description)
    {
        this.company_id = company_id;
        this.company_name = company_name;
        this.address = address;
        this.description = description;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    @NonNull
    private Long company_id;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Company company)) return false;
        return getCompany_id().equals(company.getCompany_id()) && getCompany_name().equals(company.getCompany_name()) && getAddress().equals(company.getAddress()) && Objects.equals(getDescription(), company.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCompany_id(), getCompany_name(), getAddress(), getDescription());
    }
}
