package ru.msu.prak_2022.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.msu.prak_2022.DAO_implementations.User_DAO;
import ru.msu.prak_2022.gl_session;
import ru.msu.prak_2022.models.Company;
import ru.msu.prak_2022.models.Student;
import ru.msu.prak_2022.models.Teacher;
import ru.msu.prak_2022.models.User;

import static ru.msu.prak_2022.AuthInterceptor.add_auth;

@Controller
public class RegistrationController {

    @Autowired
    private User_DAO user_dao;

    @GetMapping("/registration")
    public String registration(Model model, Authentication auth) {
        add_auth(model, auth);
        return "registration";
    }

    @GetMapping("/registration/student")
    public String student_reg(Model model, Authentication auth) {
        add_auth(model, auth);
        return "student_registration";
    }

    @GetMapping("/registration/teacher")
    public String teacher_reg(Model model, Authentication auth) {
        add_auth(model, auth);
        return "teacher_registration";
    }

    @GetMapping("/registration/company")
    public String company_reg(Model model, Authentication auth) {
        add_auth(model, auth);
        return "company_registration";
    }


//    @PostMapping("/registration")
//    public String addUser(@RequestParam("user_type") Long user_type,
//                          @RequestParam("username") String username,
//                          @RequestParam("password") String passwd,
//                          @RequestParam("password_confirm") String passwd_confirm,
//                          Model model) {
//        if (!passwd.equals(passwd_confirm)){
//            model.addAttribute("passwordError", "Пароли не совпадают");
//            return "registration";
//        }
//        if (!user_dao.save_user(user_type, username, passwd)){
//            model.addAttribute("usernameError", "Пользователь с таким именем уже существует");
//            return "registration";
//        }
//
//        return "redirect:/";
//    }

    @PostMapping("/registration/student")
    public String add_student(@RequestParam("username") String username,
                          @RequestParam("passwd") String passwd,
                          @RequestParam("passwd_confirm") String passwd_confirm,
                          @RequestParam("surname") String surname,
                          @RequestParam("first_name") String first_name,
                          @RequestParam("last_name") String last_name,
                          @RequestParam("description") String description,
                          Model model, Authentication auth) {
        add_auth(model, auth);
        if (!passwd.equals(passwd_confirm)){
            model.addAttribute("passwordError", true);
            return "student_registration";
        }
        gl_session.open();

        Student st = new Student();
        st.setDescription(description);
        st.setFirst_name(first_name);
        st.setLast_name(last_name);
        st.setSurname(surname);
        st.setUsername(username);

        if (!user_dao.save_user(1L, username, passwd, st)){
            gl_session.close();
            model.addAttribute("usernameError", "Пользователь с таким именем уже существует");
            return "student_registration";
        }

        gl_session.close();
        return "redirect:/";
    }

    @PostMapping("/registration/teacher")
    public String add_teacher(@RequestParam("username") String username,
                          @RequestParam("passwd") String passwd,
                          @RequestParam("passwd_confirm") String passwd_confirm,
                          @RequestParam("surname") String surname,
                          @RequestParam("first_name") String first_name,
                          @RequestParam("last_name") String last_name,
                          @RequestParam("description") String description,
                          Model model, Authentication auth) {
        add_auth(model, auth);
        if (!passwd.equals(passwd_confirm)){
            model.addAttribute("passwordError", true);
            return "teacher_registration";
        }
        gl_session.open();

        Teacher st = new Teacher();
        st.setDescription(description);
        st.setFirst_name(first_name);
        st.setLast_name(last_name);
        st.setSurname(surname);
        st.setUsername(username);

        if (!user_dao.save_user(2L, username, passwd, st)){
            model.addAttribute("usernameError", "Пользователь с таким именем уже существует");
            gl_session.close();
            return "teacher_registration";
        }

        gl_session.close();
        return "redirect:/";
    }

    @PostMapping("/registration/company")
    public String add_company(@RequestParam("username") String username,
                              @RequestParam("passwd") String passwd,
                              @RequestParam("passwd_confirm") String passwd_confirm,
                              @RequestParam("address") String address,
                              @RequestParam("company_name") String company_name,
                              @RequestParam("description") String description,
                              Model model, Authentication auth) {
        add_auth(model, auth);
        if (!passwd.equals(passwd_confirm)){
            model.addAttribute("passwordError", true);
            return "company_registration";
        }
        gl_session.open();

        Company st = new Company();
        st.setDescription(description);
        st.setCompany_name(company_name);
        st.setAddress(address);
        st.setUsername(username);

        if (!user_dao.save_user(3L, username, passwd, st)){
            model.addAttribute("usernameError", "Пользователь с таким именем уже существует");
            gl_session.close();
            return "teacher_registration";
        }

        gl_session.close();
        return "redirect:/";
    }

//    @GetMapping("/login_error")
//    public String login_error(RedirectAttributes rattrs) {
//        rattrs.addAttribute("login_error", true);
//        return "redirect:/login";
//    }

    @GetMapping("/login")
    public String login(Model model, @RequestParam(defaultValue = "false") boolean login_error) {
        model.addAttribute("login_error", login_error);
        return "login";
    }
}
