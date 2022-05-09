package ru.msu.prak_2022.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.thymeleaf.standard.expression.GreaterLesserExpression;
import ru.msu.prak_2022.DAO_implementations.User_DAO;
import ru.msu.prak_2022.DAO_interfaces.*;
import ru.msu.prak_2022.gl_session;
import ru.msu.prak_2022.models.*;
import ru.msu.prak_2022.status;

import java.util.AbstractMap;
import java.util.List;

import static ru.msu.prak_2022.AuthInterceptor.add_auth;

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
    @Autowired
    Relations_DAO relations_dao;
    @Autowired
    User_DAO user_dao;

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public class ResourceNotFoundException extends RuntimeException {
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public class InternalErrorException extends RuntimeException {
    }

    @RequestMapping("/course")
    public String public_course(Model model, @RequestParam(defaultValue="0") Long course_id, Authentication auth) {
        add_auth(model, auth);
        gl_session.open();
        AbstractMap.SimpleEntry<status, Course> res = course_dao.get(course_id);
        if (status.NOT_FOUND == res.getKey()) {
            gl_session.close();
            throw new ResourceNotFoundException();
        }
        if (status.OK != res.getKey()) {
            gl_session.close();
            throw new InternalErrorException();
        }
        model.addAttribute("course", res.getValue());
        model.addAttribute("company", course_dao.companies(res.getValue()).getValue().get(0));
        List<Teacher> teachers = course_dao.teachers(res.getValue()).getValue();
        model.addAttribute("teachers", teachers);
        if (teachers.isEmpty()) model.addAttribute("empty_teacher", true);

        if (auth != null) {
            User user = user_dao.by_username(auth.getName());
            Role role = user.getRoles().get(0);
            if (role.getName().equals("ROLE_STUDENT")) {
                AbstractMap.SimpleEntry<status, Boolean> res1 = relations_dao.is_student(student_dao.get(user.getForeign_id()).getValue(), res.getValue());
                if (res1.getKey() != status.OK) {
                    gl_session.close();
                    throw new InternalErrorException();
                }
                if (res1.getValue()) model.addAttribute("can_unenroll", true);
                else model.addAttribute("can_enroll", true);
            } else if (role.getName().equals("ROLE_TEACHER")) {
                AbstractMap.SimpleEntry<status, Boolean> res1 = relations_dao.is_admin(teacher_dao.get(user.getForeign_id()).getValue(), res.getValue());
                if (res1.getKey() == status.OK) {
                    model.addAttribute("to_lessons", true);
                    model.addAttribute("teacher_manage", res1.getValue());
                } else if (res1.getKey() == status.RELATION_NOT_FOUND) {
                    ;
                } else {
                    gl_session.close();
                    throw new InternalErrorException();
                }
            } else if (role.getName().equals("ROLE_COMPANY")) {
                AbstractMap.SimpleEntry<status, Boolean> res1 = relations_dao.is_author(company_dao.get(user.getForeign_id()).getValue(), res.getValue());
                if (res1.getKey() == status.OK) {
                    model.addAttribute("company_manage", res1.getValue());
                } else if (res1.getKey() == status.RELATION_NOT_FOUND) {
                    ;
                } else {
                    gl_session.close();
                    throw new InternalErrorException();
                }
            }
        }
        gl_session.close();
        return "course";
    }

    @RequestMapping("/teacher")
    public String public_teacher(Model model, @RequestParam(defaultValue="0") Long teacher_id, Authentication auth) {
        add_auth(model, auth);
        gl_session.open();
        AbstractMap.SimpleEntry<status, Teacher> res = teacher_dao.get(teacher_id);
        if (status.NOT_FOUND == res.getKey()) {
            gl_session.close();
            throw new ResourceNotFoundException();
        }
        if (status.OK != res.getKey()) {
            gl_session.close();
            throw new InternalErrorException();
        }

        if (auth != null) {
            User user = user_dao.by_username(auth.getName());
            Role role = user.getRoles().get(0);
            if (role.getName().equals("ROLE_STUDENT")) {
                ;
            } else if (role.getName().equals("ROLE_TEACHER")) {
                ;
            } else if (role.getName().equals("ROLE_COMPANY")) {
                AbstractMap.SimpleEntry<status, Boolean> res1 = relations_dao.is_member(res.getValue(), company_dao.get(user.getForeign_id()).getValue());
                if (res1.getKey() == status.OK) {
                    model.addAttribute("can_fire", res1.getValue());
                } else {
                    gl_session.close();
                    throw new InternalErrorException();
                }
                if (!res1.getValue()) {
                    res1 = relations_dao.is_invited(res.getValue(), company_dao.get(user.getForeign_id()).getValue());
                    if (res1.getKey() == status.OK) {
                        model.addAttribute("can_uninvite", res1.getValue());
                        model.addAttribute("can_invite", !res1.getValue());
                    } else {
                        gl_session.close();
                        throw new InternalErrorException();
                    }
                }
            }
        }

        model.addAttribute("teacher", res.getValue());
        List<Course> courses = teacher_dao.courses(res.getValue()).getValue();
        List<Company> companies = teacher_dao.companies(res.getValue()).getValue();

        model.addAttribute("courses", courses);
        model.addAttribute("companies", companies);

        gl_session.close();
        if (courses.isEmpty()) model.addAttribute("empty_courses", true);
        if (companies.isEmpty()) model.addAttribute("empty_companies", true);
        return "teacher";
    }

    @RequestMapping("/company")
    public String public_company(Model model, @RequestParam(defaultValue="0") Long company_id, Authentication auth) {
        add_auth(model, auth);
        gl_session.open();
        AbstractMap.SimpleEntry<status, Company> res = company_dao.get(company_id);
        if (status.NOT_FOUND == res.getKey()) {
            gl_session.close();
            throw new ResourceNotFoundException();
        }
        if (status.OK != res.getKey()) {
            gl_session.close();
            throw new InternalErrorException();
        }
        model.addAttribute("company", res.getValue());
        List<Course> courses = company_dao.courses(res.getValue()).getValue();
        if (courses.isEmpty()) model.addAttribute("empty", true);
        model.addAttribute("courses", courses);

        gl_session.close();
        return "company";
    }

    @RequestMapping("/student")
    public String public_student(Model model, @RequestParam(defaultValue="0") Long student_id, Authentication auth) {
        add_auth(model, auth);
        gl_session.open();
        AbstractMap.SimpleEntry<status, Student> res = student_dao.get(student_id);
        if (status.NOT_FOUND == res.getKey()) {
            gl_session.close();
            throw new ResourceNotFoundException();
        }
        if (status.OK != res.getKey()) {
            gl_session.close();
            throw new InternalErrorException();
        }
        model.addAttribute("student", res.getValue());
        List<Course> courses = student_dao.courses(res.getValue()).getValue();

        model.addAttribute("courses", courses);

        if (courses.isEmpty()) model.addAttribute("empty_courses", true);

        gl_session.close();
        return "student";
    }
}
