package ru.msu.prak_2022.controllers;

import lombok.NonNull;
import lombok.val;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.msu.prak_2022.DAO_implementations.User_DAO;
import ru.msu.prak_2022.DAO_interfaces.*;
import ru.msu.prak_2022.gl_session;
import ru.msu.prak_2022.models.*;
import ru.msu.prak_2022.status;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    Lesson_DAO lesson_dao;
    @Autowired
    Relations_DAO relations_dao;
    @Autowired
    User_DAO user_dao;

    public class SimpleEntry<K, V> extends AbstractMap.SimpleEntry<K, V> {
        public SimpleEntry(K key, V value) {
            super(key, value);
        }
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public class InternalErrorException extends RuntimeException {
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public class NotFoundException extends RuntimeException {
    }

    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public class ForbiddenException extends RuntimeException {
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
            List<Course> courses = student_dao.courses(st.getValue()).getValue();
            if (courses.isEmpty()) model.addAttribute("empty_courses", true);
            model.addAttribute("courses", courses);
            result = "student_home";
        } else if (role.equals("ROLE_TEACHER")) {
            AbstractMap.SimpleEntry<status, Teacher> tch = teacher_dao.get(user.getForeign_id());
            if (tch.getKey() != status.OK) {
                gl_session.close();
                throw new InternalErrorException();
            }
            model.addAttribute("teacher", tch.getValue());
            List<Course> courses = teacher_dao.courses(tch.getValue()).getValue();
            if (courses.isEmpty()) model.addAttribute("empty_courses", true);
            model.addAttribute("courses", courses);
            result = "teacher_home";
        } else if (role.equals("ROLE_COMPANY")) {
            AbstractMap.SimpleEntry<status, Company> cmp = company_dao.get(user.getForeign_id());
            if (cmp.getKey() != status.OK) {
                gl_session.close();
                throw new InternalErrorException();
            }
            model.addAttribute("company", cmp.getValue());
            List<Course> courses = company_dao.courses(cmp.getValue()).getValue();
            if (courses.isEmpty()) model.addAttribute("empty_courses", true);
            model.addAttribute("courses", courses);
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

    @PostMapping("/student/perform_update")
    public String update_student(@RequestParam("surname") String surname,
                                 @RequestParam("first_name") String first_name,
                                 @RequestParam("last_name") String last_name,
                                 @RequestParam("description") String description,
                                 Model model, Authentication auth) {
        gl_session.open();
        User user = user_dao.by_username(auth.getName());
        AbstractMap.SimpleEntry<status, Student> st = student_dao.get(user.getForeign_id());
        if (st.getKey() != status.OK) {
            gl_session.close();
            throw new InternalErrorException();
        }
        st.getValue().setSurname(surname);
        st.getValue().setDescription(description);
        st.getValue().setLast_name(last_name);
        st.getValue().setFirst_name(first_name);

        student_dao.update(st.getValue());
        return "redirect:/home";
    }

    @PostMapping("/teacher/perform_update")
    public String update_teacher(@RequestParam("surname") String surname,
                                 @RequestParam("first_name") String first_name,
                                 @RequestParam("last_name") String last_name,
                                 @RequestParam("description") String description,
                                 Model model, Authentication auth) {
        gl_session.open();
        User user = user_dao.by_username(auth.getName());
        AbstractMap.SimpleEntry<status, Teacher> tch = teacher_dao.get(user.getForeign_id());
        if (tch.getKey() != status.OK) {
            gl_session.close();
            throw new InternalErrorException();
        }
        tch.getValue().setSurname(surname);
        tch.getValue().setDescription(description);
        tch.getValue().setLast_name(last_name);
        tch.getValue().setFirst_name(first_name);

        teacher_dao.update(tch.getValue());
        return "redirect:/home";
    }

    @PostMapping("/company/perform_update")
    public String update_student(@RequestParam("company_name") String company_name,
                                 @RequestParam String address,
                                 @RequestParam("description") String description,
                                 Model model, Authentication auth) {
        gl_session.open();
        User user = user_dao.by_username(auth.getName());
        AbstractMap.SimpleEntry<status, Company> cmp = company_dao.get(user.getForeign_id());
        if (cmp.getKey() != status.OK) {
            gl_session.close();
            throw new InternalErrorException();
        }
        cmp.getValue().setCompany_name(company_name);
        cmp.getValue().setDescription(description);
        cmp.getValue().setAddress(address);

        company_dao.update(cmp.getValue());
        return "redirect:/home";
    }

    @RequestMapping("/student/unenroll")
    public String unenroll(Authentication auth, @RequestParam Long course_id, @RequestParam String from) {
        gl_session.open();
        User user = user_dao.by_username(auth.getName());
        AbstractMap.SimpleEntry<status, Student> st = student_dao.get(user.getForeign_id());
        if (st.getKey() != status.OK) {
            gl_session.close();
            throw new InternalErrorException();
        }
        AbstractMap.SimpleEntry<status, Course> course = course_dao.get(course_id);
        if (course.getKey() != status.OK) {
            gl_session.close();
            throw new InternalErrorException();
        }
        status unenroll = relations_dao.unenroll(st.getValue(), course.getValue());
        gl_session.close();
        if (unenroll != status.OK) throw new ForbiddenException();
        if (from.equals("home")) return "redirect:/home";
        else return "redirect:/course?course_id="+course_id;
    }

    @RequestMapping("/student/enroll")
    public String enroll(Authentication auth, @RequestParam Long course_id) {
        gl_session.open();
        User user = user_dao.by_username(auth.getName());
        AbstractMap.SimpleEntry<status, Student> st = student_dao.get(user.getForeign_id());
        if (st.getKey() != status.OK) {
            gl_session.close();
            throw new InternalErrorException();
        }
        AbstractMap.SimpleEntry<status, Course> course = course_dao.get(course_id);
        if (course.getKey() != status.OK) {
            gl_session.close();
            throw new InternalErrorException();
        }
        status enroll = relations_dao.enroll(st.getValue(), course.getValue());
        gl_session.close();
        if (enroll != status.OK) throw new ForbiddenException();
        return "redirect:/course?course_id="+course_id;
    }

    @RequestMapping("/home/teachers")
    public String company_invites(Model model, Authentication auth) {
        gl_session.open();

        add_auth(model, auth);
        gl_session.open();
        User user = user_dao.by_username(auth.getName());
        String role = user.getRoles().get(0).getName();

        AbstractMap.SimpleEntry<status, Company> cmp = company_dao.get(user.getForeign_id());
        if (cmp.getKey() != status.OK) {
            gl_session.close();
            throw new InternalErrorException();
        }
//        model.addAttribute("company", cmp.getValue());
        List<Teacher> teachers = company_dao.teachers(cmp.getValue()).getValue();
        if (teachers.isEmpty()) model.addAttribute("empty_teacher", true);
        model.addAttribute("teachers", teachers);

        List<Teacher> invites = company_dao.invites(cmp.getValue()).getValue();
        if (invites.isEmpty()) model.addAttribute("empty_invites", true);
        model.addAttribute("invites", invites);

        gl_session.close();
        return "company_invites";
    }

    @RequestMapping("/home/companies")
    public String teacher_invites(Model model, Authentication auth) {
        gl_session.open();

        add_auth(model, auth);
        gl_session.open();
        User user = user_dao.by_username(auth.getName());
        String role = user.getRoles().get(0).getName();

        AbstractMap.SimpleEntry<status, Teacher> tch = teacher_dao.get(user.getForeign_id());
        if (tch.getKey() != status.OK) {
            gl_session.close();
            throw new InternalErrorException();
        }
//        model.addAttribute("company", cmp.getValue());
        List<Company> companies = teacher_dao.companies(tch.getValue()).getValue();
        if (companies.isEmpty()) model.addAttribute("empty_companies", true);
        model.addAttribute("companies", companies);

        List<Company> invites = teacher_dao.invites(tch.getValue()).getValue();
        if (invites.isEmpty()) model.addAttribute("empty_invites", true);
        model.addAttribute("invites", invites);

        gl_session.close();
        return "teacher_invites";
    }

    @RequestMapping("/company/fire")
    public String fire(Authentication auth, @RequestParam Long teacher_id, @RequestParam String from) {
        gl_session.open();
        User user = user_dao.by_username(auth.getName());
        AbstractMap.SimpleEntry<status, Company> cmp = company_dao.get(user.getForeign_id());
        if (cmp.getKey() != status.OK) {
            gl_session.close();
            throw new InternalErrorException();
        }
        AbstractMap.SimpleEntry<status, Teacher> teacher = teacher_dao.get(teacher_id);
        if (teacher.getKey() != status.OK) {
            gl_session.close();
            throw new InternalErrorException();
        }
        status fire = relations_dao.fire(teacher.getValue(), cmp.getValue());
        gl_session.close();
        if (fire != status.OK) throw new ForbiddenException();
        if (from.equals("home")) return "redirect:/home/teachers";
        else return "redirect:/teacher?teacher_id="+teacher_id;
    }

    @RequestMapping("/company/invite")
    public String invite(Authentication auth, @RequestParam Long teacher_id) {
        gl_session.open();
        User user = user_dao.by_username(auth.getName());
        AbstractMap.SimpleEntry<status, Company> cmp = company_dao.get(user.getForeign_id());
        if (cmp.getKey() != status.OK) {
            gl_session.close();
            throw new InternalErrorException();
        }
        AbstractMap.SimpleEntry<status, Teacher> teacher = teacher_dao.get(teacher_id);
        if (teacher.getKey() != status.OK) {
            gl_session.close();
            throw new InternalErrorException();
        }
        status invite = relations_dao.invite(teacher.getValue(), cmp.getValue());
        gl_session.close();
        if (invite != status.OK && invite != status.ALREADY_MEMBER) throw new ForbiddenException();
        return "redirect:/teacher?teacher_id="+teacher_id;
    }

    @RequestMapping("/teacher/approve")
    public String approve(Authentication auth, @RequestParam Long company_id) {
        gl_session.open();
        User user = user_dao.by_username(auth.getName());
        AbstractMap.SimpleEntry<status, Teacher> tch = teacher_dao.get(user.getForeign_id());
        if (tch.getKey() != status.OK) {
            gl_session.close();
            throw new InternalErrorException();
        }
        AbstractMap.SimpleEntry<status, Company> company = company_dao.get(company_id);
        if (company.getKey() != status.OK) {
            gl_session.close();
            throw new InternalErrorException();
        }
        status approve = relations_dao.approve(tch.getValue(), company.getValue());
        gl_session.close();
        if (approve != status.OK) throw new ForbiddenException();
        return "redirect:/home/companies";
    }

    @RequestMapping("/teacher/reject")
    public String reject(Authentication auth, @RequestParam Long company_id) {
        gl_session.open();
        User user = user_dao.by_username(auth.getName());
        AbstractMap.SimpleEntry<status, Teacher> tch = teacher_dao.get(user.getForeign_id());
        if (tch.getKey() != status.OK) {
            gl_session.close();
            throw new InternalErrorException();
        }
        AbstractMap.SimpleEntry<status, Company> company = company_dao.get(company_id);
        if (company.getKey() != status.OK) {
            gl_session.close();
            throw new InternalErrorException();
        }
        status reject = relations_dao.reject(tch.getValue(), company.getValue());
        gl_session.close();
        if (reject != status.OK) throw new ForbiddenException();
        return "redirect:/home/companies";
    }

    @RequestMapping("/teacher/retire")
    public String retire(Authentication auth, @RequestParam Long company_id) {
        gl_session.open();
        User user = user_dao.by_username(auth.getName());
        AbstractMap.SimpleEntry<status, Teacher> tch = teacher_dao.get(user.getForeign_id());
        if (tch.getKey() != status.OK) {
            gl_session.close();
            throw new InternalErrorException();
        }
        AbstractMap.SimpleEntry<status, Company> company = company_dao.get(company_id);
        if (company.getKey() != status.OK) {
            gl_session.close();
            throw new InternalErrorException();
        }
        status retire = relations_dao.retire(tch.getValue(), company.getValue());
        gl_session.close();
        if (retire != status.OK) throw new ForbiddenException();
        return "redirect:/home/companies";
    }

    @GetMapping("/create_course")
    public String create_course(Model model, Authentication auth) {
        add_auth(model, auth);
        return "create_course";
    }

    @PostMapping("/create_course")
    public String create_course(Model model, Authentication auth,
                                @RequestParam String course_name,
                                @RequestParam Date date_from,
                                @RequestParam Date date_till,
                                @RequestParam float lesson_intensivity,
                                @RequestParam float self_study_intensivity,
                                @RequestParam String description)
    {
        gl_session.open();
        User user = user_dao.by_username(auth.getName());
        AbstractMap.SimpleEntry<status, Company> cmp = company_dao.get(user.getForeign_id());
        if (cmp.getKey() != status.OK) {
            gl_session.close();
            return "redirect:/perform_logout";
        }
        if (date_from.after(date_till)) {
            add_auth(model, auth);
            model.addAttribute("date_error", true);
            return "create_course";
        };
        Course course = new Course();
        course.setCourse_name(course_name);
        course.setDescription(description);
        course.setDate_from(date_from);
        course.setDate_till(date_till);
        course.setSelf_study_intensivity(self_study_intensivity);
        course.setLesson_intensivity(lesson_intensivity);

        status res = relations_dao.create_course(cmp.getValue(), course);
        Long course_id = course.getCourse_id();
        gl_session.close();
        if (res != status.OK) throw new InternalErrorException();
        return "redirect:/company/manage?course_id=" + course_id;
    }

    @GetMapping("/delete_course")
    public String delete_course(Authentication auth, @RequestParam Long course_id) {
        gl_session.open();
        User user = user_dao.by_username(auth.getName());
        AbstractMap.SimpleEntry<status, Company> cmp = company_dao.get(user.getForeign_id());
        if (cmp.getKey() != status.OK) {
            gl_session.close();
            return "redirect:/perform_logout";
        }
        AbstractMap.SimpleEntry<status, Course> crs = course_dao.get(course_id);
        if (crs.getKey() == status.NOT_FOUND) {
            gl_session.close();
            throw new NotFoundException();
        }
        status res = relations_dao.delete_course(cmp.getValue(), crs.getValue());
        gl_session.close();
        if (res == status.FORBIDDEN) throw new ForbiddenException();
        if (res != status.OK) throw new InternalErrorException();
        return "redirect:/home";
    }

    @PostMapping("/update_course")
    public String update_course(Model model, Authentication auth,
                                @RequestParam String course_name,
                                @RequestParam Date date_from,
                                @RequestParam Date date_till,
                                @RequestParam float lesson_intensivity,
                                @RequestParam float self_study_intensivity,
                                @RequestParam String description,
                                @RequestParam Long course_id)
    {
        gl_session.open();
        User user = user_dao.by_username(auth.getName());
        AbstractMap.SimpleEntry<status, Company> cmp = company_dao.get(user.getForeign_id());
        if (cmp.getKey() != status.OK) {
            gl_session.close();
            return "redirect:/perform_logout";
        }
        AbstractMap.SimpleEntry<status, Course> crs = course_dao.get(course_id);
        if (crs.getKey() == status.NOT_FOUND) {
            gl_session.close();
            throw new NotFoundException();
        }
        Course course = crs.getValue();
        if (date_from.after(date_till)) {
            add_auth(model, auth);
            model.addAttribute("date_error", true);
            return "redirect:/company/manage?course_id="+course_id;
        };
        course.setCourse_name(course_name);
        course.setDescription(description);
        course.setDate_from(date_from);
        course.setDate_till(date_till);
        course.setSelf_study_intensivity(self_study_intensivity);
        course.setLesson_intensivity(lesson_intensivity);

        status res = relations_dao.update_course(cmp.getValue(), course);
        gl_session.close();
        if (res != status.OK) throw new InternalErrorException();
        return "redirect:/company/manage?course_id=" + course_id;
    }

    @GetMapping("/company/manage")
    public String company_manage(Model model, Authentication auth, @RequestParam Long course_id) {
        add_auth(model, auth);
        gl_session.open();
        User user = user_dao.by_username(auth.getName());
        AbstractMap.SimpleEntry<status, Company> cmp = company_dao.get(user.getForeign_id());
        if (cmp.getKey() != status.OK) {
            gl_session.close();
            return "redirect:/perform_logout";
        }

        AbstractMap.SimpleEntry<status, Course> course = course_dao.get(course_id);
        if (course.getKey() == status.NOT_FOUND) {
            gl_session.close();
            throw new NotFoundException();
        }
        model.addAttribute("course", course.getValue());
        model.addAttribute("teachers", company_dao.teachers(cmp.getValue()).getValue().stream().filter(tch -> relations_dao.is_admin(tch, course.getValue()).getKey() == status.RELATION_NOT_FOUND).toList());
        AbstractMap.SimpleEntry<status, List<Teacher>> course_teachers = course_dao.teachers(course.getValue());
        if (course_teachers.getValue().isEmpty()) model.addAttribute("empty_teachers", true);

        List<Boolean> is_admin = course_teachers.getValue().stream().map(tch -> relations_dao.is_admin(tch, course.getValue()).getValue()).toList();
        model.addAttribute("course_teachers", IntStream.range(0, course_teachers.getValue().size()).mapToObj(i -> new SimpleEntry(course_teachers.getValue().get(i), is_admin.get(i))).toList());
        return "company_manage";

    }

    @RequestMapping("/company/revoke_admin")
    public String revoke_admin(Authentication auth,
                               @RequestParam Long course_id,
                               @RequestParam Long teacher_id) {
        return company_course_manage(auth, course_id, teacher_id, "revoke_admin");
    }

    @RequestMapping("/company/grant_admin")
    public String grant_admin(Authentication auth,
                               @RequestParam Long course_id,
                               @RequestParam Long teacher_id) {
        return company_course_manage(auth, course_id, teacher_id, "grant_admin");
    }

    @RequestMapping("/company/add_teacher")
    public String add_teacher(Authentication auth,
                               @RequestParam Long course_id,
                               @RequestParam Long teacher_id) {
        return company_course_manage(auth, course_id, teacher_id, "add_teacher");
    }

    @GetMapping("/company/delete_teacher")
    public String delete_teacher(Authentication auth,
                                 @RequestParam("teacher_id") Long teacher_id,
                                 @RequestParam("course_id") Long course_id) {
        return company_course_manage(auth, course_id, teacher_id, "delete_teacher");
    }



//    @RequestMapping("/company/revoke_admin")
//    public String revoke_admin(Authentication auth, @RequestParam Long course_id, @RequestParam Long teacher_id) {
//
//    };

    private String company_course_manage(Authentication auth, Long course_id, Long teacher_id, String mth) {
        gl_session.open();
        User user = user_dao.by_username(auth.getName());
        AbstractMap.SimpleEntry<status, Company> cmp = company_dao.get(user.getForeign_id());
        if (cmp.getKey() != status.OK) {
            gl_session.close();
            return "redirect:/perform_logout";
        }

        AbstractMap.SimpleEntry<status, Teacher> tch = teacher_dao.get(teacher_id);
        if (tch.getKey() == status.NOT_FOUND) {
            gl_session.close();
            throw new NotFoundException();
        } else if (tch.getKey() != status.OK) {
            gl_session.close();
            throw new InternalErrorException();
        }
        AbstractMap.SimpleEntry<status, Course> course = course_dao.get(course_id);
        if (course.getKey() == status.NOT_FOUND) {
            gl_session.close();
            throw new NotFoundException();
        } else if (course.getKey() != status.OK) {
            gl_session.close();
            throw new InternalErrorException();
        }
        status op = status.FORBIDDEN;
        if (mth.equals("revoke_admin")) {
            op = relations_dao.revoke_admin(cmp.getValue(), course.getValue(), tch.getValue());
        } else if (mth.equals("grant_admin")) {
            op = relations_dao.grant_admin(cmp.getValue(), course.getValue(), tch.getValue());
        } else if (mth.equals("delete_teacher")) {
            op = relations_dao.delete_teacher(cmp.getValue(), course.getValue(), tch.getValue());
        } else if (mth.equals("add_teacher")) {
            op = relations_dao.add_teacher(cmp.getValue(), course.getValue(), tch.getValue());
        }
        gl_session.close();
        if (op != status.OK) throw new ForbiddenException();
        return "redirect:/company/manage?course_id="+course_id;
    }

    @RequestMapping("/teacher/manage")
    public String teacher_manage(Model model, Authentication auth, @RequestParam Long course_id) {
        add_auth(model, auth);
        User user = user_dao.by_username(auth.getName());
        AbstractMap.SimpleEntry<status, Teacher> tch = teacher_dao.get(user.getForeign_id());
        if (tch.getKey() != status.OK) {
            gl_session.close();
            return "redirect:/perform_logout";
        }

        AbstractMap.SimpleEntry<status, Course> course = course_dao.get(course_id);
        if (course.getKey() == status.NOT_FOUND) {
            gl_session.close();
            throw new NotFoundException();
        } else if (course.getKey() != status.OK) {
            gl_session.close();
            throw new InternalErrorException();
        }
        model.addAttribute("course", course.getValue());

        AbstractMap.SimpleEntry<status, List<Lesson>> lessons = course_dao.lessons(course.getValue());
        if (lessons.getValue().isEmpty()) model.addAttribute("empty_lessons", true);
        model.addAttribute("lessons", lessons.getValue());

        return "teacher_manage";
    }

    @GetMapping("/create_lesson")
    public String create_lesson(Model model, Authentication auth, @RequestParam Long course_id) {
        add_auth(model, auth);
        gl_session.open();
        User user = user_dao.by_username(auth.getName());
        AbstractMap.SimpleEntry<status, Teacher> tch = teacher_dao.get(user.getForeign_id());
        if (tch.getKey() != status.OK) {
            gl_session.close();
            return "redirect:/perform_logout";
        }
        AbstractMap.SimpleEntry<status, Course> crs = course_dao.get(course_id);
        if (crs.getKey() == status.NOT_FOUND) {
            gl_session.close();
            throw new NotFoundException();
        }
        model.addAttribute("course_id", course_id);
        model.addAttribute("teachers", course_dao.teachers(crs.getValue()).getValue());
        return "create_lesson";
    }
//
    @PostMapping("/create_lesson")
    public String create_lesson(Model model, Authentication auth,
                                @RequestParam String time_from,
                                @RequestParam String time_till,
                                @RequestParam String description,
                                @RequestParam Long course_id,
                                @RequestParam Long teacher_id) {
        gl_session.open();
        User user = user_dao.by_username(auth.getName());
        AbstractMap.SimpleEntry<status, Teacher> tch = teacher_dao.get(user.getForeign_id());
        if (tch.getKey() != status.OK) {
            gl_session.close();
            return "redirect:/perform_logout";
        }
        AbstractMap.SimpleEntry<status, Course> crs = course_dao.get(course_id);
        if (crs.getKey() == status.NOT_FOUND) {
            gl_session.close();
            throw new NotFoundException();
        }
        AbstractMap.SimpleEntry<status, Teacher> t = teacher_dao.get(teacher_id);
        if (t.getKey() == status.NOT_FOUND) {
            gl_session.close();
            throw new NotFoundException();
        }
        relations_dao.create_lesson(t.getValue(), crs.getValue(), Timestamp.valueOf(LocalDateTime.parse(time_from)), Timestamp.valueOf(LocalDateTime.parse(time_till)), description);
        return "redirect:/teacher/manage?course_id=" + course_id;
    }
//
    @GetMapping("/update_lesson")
    public String update_lesson(Model model, Authentication auth, @RequestParam Long lesson_id) {
        gl_session.open();
        User user = user_dao.by_username(auth.getName());
        AbstractMap.SimpleEntry<status, Teacher> tch = teacher_dao.get(user.getForeign_id());
        if (tch.getKey() != status.OK) {
            gl_session.close();
            return "redirect:/perform_logout";
        }
        AbstractMap.SimpleEntry<status, Lesson> lsn = lesson_dao.get(lesson_id);
        if (lsn.getKey() == status.NOT_FOUND) {
            gl_session.close();
            throw new NotFoundException();
        }
        model.addAttribute("teachers", course_dao.teachers(lsn.getValue().getCourse()).getValue());
        model.addAttribute("lesson", lsn.getValue());
        gl_session.close();
        return "update_lesson";
    }
//
    @PostMapping("/update_lesson")
    public String update_lesson(Model model, Authentication auth,
                                @RequestParam String time_from,
                                @RequestParam String time_till,
                                @RequestParam String description,
                                @RequestParam Long lesson_id,
                                @RequestParam Long teacher_id) {
        gl_session.open();
        User user = user_dao.by_username(auth.getName());
        AbstractMap.SimpleEntry<status, Teacher> tch = teacher_dao.get(user.getForeign_id());
        if (tch.getKey() != status.OK) {
            gl_session.close();
            return "redirect:/perform_logout";
        }
        AbstractMap.SimpleEntry<status, Lesson> lsn = lesson_dao.get(lesson_id);
        if (lsn.getKey() == status.NOT_FOUND) {
            gl_session.close();
            throw new NotFoundException();
        }
        @NonNull Long course_id = lsn.getValue().getCourse().getCourse_id();

        lsn.getValue().setDescription(description);
        lsn.getValue().setTime_from(Timestamp.valueOf(LocalDateTime.parse(time_from)));
        lsn.getValue().setTime_till(Timestamp.valueOf(LocalDateTime.parse(time_till)));
        AbstractMap.SimpleEntry<status, Teacher> t = teacher_dao.get(teacher_id);
        if (t.getKey() == status.NOT_FOUND) {
            gl_session.close();
            throw new NotFoundException();
        }
        lsn.getValue().setTeacher(t.getValue());

        status res = relations_dao.update_lesson(tch.getValue(), lsn.getValue().getCourse(), lsn.getValue());
        gl_session.close();
        if (res == status.FORBIDDEN) throw new ForbiddenException();
        if (res != status.OK) throw new InternalErrorException();
        return "redirect:/teacher/manage?course_id=" + course_id;
    }
//
    @RequestMapping("/delete_lesson")
    public String delete_lesson(Model model, Authentication auth, @RequestParam Long lesson_id) {
        gl_session.open();
        User user = user_dao.by_username(auth.getName());
        AbstractMap.SimpleEntry<status, Teacher> tch = teacher_dao.get(user.getForeign_id());
        if (tch.getKey() != status.OK) {
            gl_session.close();
            return "redirect:/perform_logout";
        }
        AbstractMap.SimpleEntry<status, Lesson> lsn = lesson_dao.get(lesson_id);
        if (lsn.getKey() == status.NOT_FOUND) {
            gl_session.close();
            throw new NotFoundException();
        }
        @NonNull Long course_id = lsn.getValue().getCourse().getCourse_id();
        status res = relations_dao.delete_lesson(tch.getValue(), lsn.getValue().getCourse(), lsn.getValue().getLesson_id());
        gl_session.close();
        if (res == status.FORBIDDEN) throw new ForbiddenException();
        if (res != status.OK) throw new InternalErrorException();
        return "redirect:/teacher/manage?course_id=" + course_id;
    }

    @RequestMapping("/lessons")
    public String lessons(Model model, Authentication auth, @RequestParam Long course_id) {
        String result = "redirect:/";
        add_auth(model, auth);
        gl_session.open();
        User user = user_dao.by_username(auth.getName());
        String role = user.getRoles().get(0).getName();
        if (role.equals("ROLE_TEACHER")) {
            AbstractMap.SimpleEntry<status, Teacher> tch = teacher_dao.get(user.getForeign_id());
            if (tch.getKey() != status.OK) {
                gl_session.close();
                return "redirect:/perform_logout";
            }
            AbstractMap.SimpleEntry<status, Course> course = course_dao.get(course_id);
            if (course.getKey() == status.NOT_FOUND) {
                gl_session.close();
                throw new NotFoundException();
            } else if (course.getKey() != status.OK) {
                gl_session.close();
                throw new InternalErrorException();
            }
            model.addAttribute("course", course.getValue());
            model.addAttribute("teacher_id", tch.getValue().getTeacher_id());
            model.addAttribute("lessons", course_dao.lessons(course.getValue()).getValue());
            return "teacher_lessons";
        } else if (role.equals("ROLE_STUDENT")) {
            AbstractMap.SimpleEntry<status, Student> tch = student_dao.get(user.getForeign_id());
            if (tch.getKey() != status.OK) {
                gl_session.close();
                return "redirect:/perform_logout";
            }
            AbstractMap.SimpleEntry<status, Course> course = course_dao.get(course_id);
            if (course.getKey() == status.NOT_FOUND) {
                gl_session.close();
                throw new NotFoundException();
            } else if (course.getKey() != status.OK) {
                gl_session.close();
                throw new InternalErrorException();
            }
            @NonNull Long student_id;
            student_id =  tch.getValue().getStudent_id();
            List<Lesson> lessons = course_dao.lessons(course.getValue()).getValue();
//            System.err.print(new SimpleEntry<Long, Long>(lessons.get(0).getLesson_id(), lesson_dao.scores(lessons.get(0)).getValue().get(student_id)));
            Map<Long, String> student_scores = lessons.stream().map(lsn -> {
                val res = new SimpleEntry<Long, Long>(lsn.getLesson_id(), lesson_dao.scores(lsn).getValue().get(student_id));
                val res1 = new SimpleEntry<Long, String>(res.getKey(), "");
                if (res.getValue() == null) res1.setValue("н");
                else res1.setValue(res.getValue().toString());
                return res1;
            }).collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
            model.addAttribute("lessons", lessons);
            if (lessons.isEmpty()) model.addAttribute("empty_lessons", true);
            model.addAttribute("scores", student_scores);
            model.addAttribute("course", course.getValue());
            return "student_lessons";
        }
        throw new ForbiddenException();
    }

    @RequestMapping("/lesson")
    public String manage_lesson(Model model, Authentication auth, @RequestParam Long lesson_id) {
        add_auth(model, auth);
        User user = user_dao.by_username(auth.getName());
        AbstractMap.SimpleEntry<status, Teacher> tch = teacher_dao.get(user.getForeign_id());
        if (tch.getKey() != status.OK) {
            gl_session.close();
            return "redirect:/perform_logout";
        }

        AbstractMap.SimpleEntry<status, Lesson> lesson = lesson_dao.get(lesson_id);
        if (lesson.getKey() == status.NOT_FOUND) {
            gl_session.close();
            throw new NotFoundException();
        } else if (lesson.getKey() != status.OK) {
            gl_session.close();
            throw new InternalErrorException();
        }

        AbstractMap.SimpleEntry<status, Course> course = lesson_dao.get_course(lesson.getValue());
        if (course.getKey() == status.NOT_FOUND) {
            gl_session.close();
            throw new NotFoundException();
        } else if (course.getKey() != status.OK) {
            gl_session.close();
            throw new InternalErrorException();
        }

        AbstractMap.SimpleEntry<status, Map<Long, Long>> scores = lesson_dao.scores(lesson.getValue());
        model.addAttribute("scores", scores.getValue());

        model.addAttribute("lesson", lesson.getValue());
        val students = course_dao.students(course.getValue()).getValue();
        if (students.isEmpty()) model.addAttribute("empty_students", true);
        model.addAttribute("students", students);
        return "lesson";
    }

    @GetMapping("/rate")
    public String rate(Authentication auth, @RequestParam String score, @RequestParam Long student_id, @RequestParam Long lesson_id)
    {
        User user = user_dao.by_username(auth.getName());
        AbstractMap.SimpleEntry<status, Teacher> tch = teacher_dao.get(user.getForeign_id());
        if (tch.getKey() != status.OK) {
            gl_session.close();
            return "redirect:/perform_logout";
        }

        AbstractMap.SimpleEntry<status, Lesson> lesson = lesson_dao.get(lesson_id);
        if (lesson.getKey() == status.NOT_FOUND) {
            gl_session.close();
            throw new NotFoundException();
        } else if (lesson.getKey() != status.OK) {
            gl_session.close();
            throw new InternalErrorException();
        }

        AbstractMap.SimpleEntry<status, Student> student = student_dao.get(student_id);
        if (student.getKey() == status.NOT_FOUND) {
            gl_session.close();
            throw new NotFoundException();
        } else if (student.getKey() != status.OK) {
            gl_session.close();
            throw new InternalErrorException();
        }
        status res;
        if (score.equals("")) {
            res = relations_dao.rate(tch.getValue(), lesson.getValue(), student.getValue(), null);
        } else {
            res = relations_dao.rate(tch.getValue(), lesson.getValue(), student.getValue(), Long.parseLong(score));
        }
        gl_session.close();
        if (res == status.FORBIDDEN) throw new ForbiddenException();
        if (res != status.OK) throw new InternalErrorException();
        return "redirect:/lesson?lesson_id=" + lesson_id;
    }
}
