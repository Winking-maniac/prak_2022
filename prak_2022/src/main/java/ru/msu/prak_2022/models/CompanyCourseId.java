package ru.msu.prak_2022.models;

import lombok.*;

import javax.persistence.IdClass;
import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CompanyCourseId implements Serializable
{
    private Long course_id;
    private Long company_id;
}