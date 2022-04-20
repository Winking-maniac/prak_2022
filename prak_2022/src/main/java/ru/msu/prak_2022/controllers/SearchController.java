package ru.msu.prak_2022.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.msu.prak_2022.DAO_interfaces.*;
import ru.msu.prak_2022.gl_session;
import ru.msu.prak_2022.models.Company;
import ru.msu.prak_2022.models.Course;
import ru.msu.prak_2022.models.Teacher;
import ru.msu.prak_2022.status;

import java.util.AbstractMap;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;

@Controller
public class SearchController {

    @Autowired
    Course_DAO course_dao;
    @Autowired
    Teacher_DAO teacher_dao;
    @Autowired
    Company_DAO company_dao;

    @GetMapping("/")
    public String search_str(Model model) {
//        model.addAttribute("data", "Hello world, " + id);
        return "search_str";
    }

    @GetMapping("/search")
    public String search_results(Model model, @RequestParam(defaultValue = "") String pattern) {
        gl_session.open();
        model.addAttribute("pattern", pattern);
        AbstractMap.SimpleEntry<status, List<Course>> res_courses = course_dao.get(pattern);
        AbstractMap.SimpleEntry<status, List<Teacher>> res_teachers = teacher_dao.get(pattern);
        AbstractMap.SimpleEntry<status, List<Company>> res_companies = company_dao.get(pattern);
        if (res_courses.getKey() == status.OK &&
            res_teachers.getKey() == status.OK &&
            res_companies.getKey() == status.OK) {
            model.addAttribute("courses", res_courses.getValue());
            model.addAttribute("teachers", res_teachers.getValue());
            model.addAttribute("companies", res_companies.getValue());
            gl_session.close();
            return "search_results";
        }
        gl_session.close();
        return "error";
    }

    @GetMapping("/search/courses")
    String courses_search_results(Model model, @RequestParam(defaultValue = "") String pattern) {
        String res = search_result(model, pattern, course_dao);
        if (Objects.equals(res, "search_results")) res = "course_search_results";
        return res;
    }

    @GetMapping("/search/teachers")
    String teachers_search_results(Model model, @RequestParam(defaultValue = "") String pattern) {
        String res = search_result(model, pattern, teacher_dao);
        if (Objects.equals(res, "search_results")) res = "teacher_search_results";
        return res;
    }

    @GetMapping("/search/companies")
    String companies_search_results(Model model, @RequestParam(defaultValue = "") String pattern) {
        String res = search_result(model, pattern, company_dao);
        if (Objects.equals(res, "search_results")) res = "company_search_results";
        return res;
    }

    private String search_result(Model model, @RequestParam String pattern, Searchable dao_class) {
        String result = "error";
        gl_session.open();
        model.addAttribute("pattern", pattern);
            AbstractMap.SimpleEntry<status, List<Course>> res = dao_class.get(pattern);
            if (res.getKey() == status.OK) {
                model.addAttribute("search_result", res.getValue());
                gl_session.close();
                result = "search_results";
            } else {
                gl_session.close();
            }
        return result;
    }
}
