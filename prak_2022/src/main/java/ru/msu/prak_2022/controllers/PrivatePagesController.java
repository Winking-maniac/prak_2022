package ru.msu.prak_2022.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.msu.prak_2022.DAO_implementations.User_DAO;
import ru.msu.prak_2022.DAO_interfaces.Company_DAO;
import ru.msu.prak_2022.DAO_interfaces.Course_DAO;
import ru.msu.prak_2022.DAO_interfaces.Student_DAO;
import ru.msu.prak_2022.DAO_interfaces.Teacher_DAO;
import ru.msu.prak_2022.gl_session;
import ru.msu.prak_2022.models.*;
import ru.msu.prak_2022.status;

import java.util.AbstractMap;

import static ru.msu.prak_2022.AuthInterceptor.add_auth;

@Controller
public class PrivatePagesController {
    @Autowired
    Student_DAO student_dao;
    @Autowired
    Course_DAO course_dao;
    @Autowired
    Teacher_DAO teacher_dao;
    @Autowired
    Company_DAO company_dao;
    @Autowired
    User_DAO user_dao;

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public class InternalErrorException extends RuntimeException {
    }

    @RequestMapping("/home")
    public String home(Model model, Authentication auth) {
        String result = "redirect:/";
        add_auth(model, auth);
        gl_session.open();
        User user = user_dao.by_username(auth.getName());
        String role = user.getRoles().get(0).getName();
        if (role.equals("ROLE_ADMIN")) {
            result = "admin_home";
        } else if (role.equals("ROLE_STUDENT")) {

            AbstractMap.SimpleEntry<status, Student> st = student_dao.get(user.getForeign_id());
            if (st.getKey() != status.OK) {
                gl_session.close();
                throw new InternalErrorException();
            }
            model.addAttribute("student", st.getValue());
            result = "student_home";
        } else if (role.equals("ROLE_TEACHER")) {
            AbstractMap.SimpleEntry<status, Teacher> tch = teacher_dao.get(user.getForeign_id());
            if (tch.getKey() != status.OK) {
                gl_session.close();
                throw new InternalErrorException();
            }
            model.addAttribute("teacher", tch.getValue());
            result = "teacher_home";
        } else if (role.equals("ROLE_COMPANY")) {
            AbstractMap.SimpleEntry<status, Company> cmp = company_dao.get(user.getForeign_id());
            if (cmp.getKey() != status.OK) {
                gl_session.close();
                throw new InternalErrorException();
            }
            model.addAttribute("company", cmp.getValue());
            result = "company_home";
        }
        gl_session.close();
        return result;
    }

    @RequestMapping("/delete_account")
    public String delete_acc(Model model, Authentication auth) {
        gl_session.open();
        User user = user_dao.by_username(auth.getName());
        gl_session.beginTransaction();
        gl_session.sessionFactory.getCurrentSession().delete(user);
        gl_session.commit();
        gl_session.close();
        return "redirect:/perform_logout";
    }
}


