package ru.msu.prak_2022.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.msu.prak_2022.DAO_interfaces.Company_DAO;
import ru.msu.prak_2022.DAO_interfaces.Course_DAO;
import ru.msu.prak_2022.DAO_interfaces.Student_DAO;
import ru.msu.prak_2022.DAO_interfaces.Teacher_DAO;
import ru.msu.prak_2022.models.Company;
import ru.msu.prak_2022.models.Course;
import ru.msu.prak_2022.models.Student;
import ru.msu.prak_2022.models.Teacher;
import ru.msu.prak_2022.status;

import java.util.AbstractMap;

@Controller
public class PublicPagesController {

    @Autowired
    Student_DAO student_dao;
    @Autowired
    Course_DAO course_dao;
    @Autowired
    Teacher_DAO teacher_dao;
    @Autowired
    Company_DAO company_dao;

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public class ResourceNotFoundException extends RuntimeException {
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public class InternalErrorException extends RuntimeException {
    }

    @RequestMapping("/course")
    public String public_course(Model model, @RequestParam(defaultValue="0") Long course_id) {
        AbstractMap.SimpleEntry<status, Course> res = course_dao.get(course_id);
        if (status.NOT_FOUND == res.getKey()) throw new ResourceNotFoundException();
        if (status.OK != res.getKey()) throw new InternalErrorException();
        model.addAttribute("course", res.getValue());
        return "course";
    }

    @RequestMapping("/teacher")
    public String public_teacher(Model model, @RequestParam(defaultValue="0") Long teacher_id) {
        AbstractMap.SimpleEntry<status, Teacher> res = teacher_dao.get(teacher_id);
        if (status.NOT_FOUND == res.getKey()) throw new ResourceNotFoundException();
        if (status.OK != res.getKey()) throw new InternalErrorException();
        model.addAttribute("teacher", res.getValue());
        return "teacher";
    }

    @RequestMapping("/company")
    public String public_company(Model model, @RequestParam(defaultValue="0") Long company_id) {
        AbstractMap.SimpleEntry<status, Company> res = company_dao.get(company_id);
        if (status.NOT_FOUND == res.getKey()) throw new ResourceNotFoundException();
        if (status.OK != res.getKey()) throw new InternalErrorException();
        model.addAttribute("company", res.getValue());
        return "company";
    }

    @RequestMapping("/student")
    public String public_student(Model model, @RequestParam(defaultValue="0") Long student_id) {
        AbstractMap.SimpleEntry<status, Student> res = student_dao.get(student_id);
        if (status.NOT_FOUND == res.getKey()) throw new ResourceNotFoundException();
        if (status.OK != res.getKey()) throw new InternalErrorException();
        model.addAttribute("teacher", res.getValue());
        return "teacher";
    }
}
