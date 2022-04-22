package ru.msu.prak_2022.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.msu.prak_2022.DAO_implementations.User_DAO;
import ru.msu.prak_2022.models.Student;
import ru.msu.prak_2022.models.User;

@Controller
public class RegistrationController {

    @Autowired
    private User_DAO user_dao;

    @GetMapping("/registration")
    public String registration(Model model) {
        return "registration";
    }

    @GetMapping("/registration/student")
    public String student_reg(Model model) {
        return "student_registration";
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
    public String addUser(@RequestParam("username") String username,
                          @RequestParam("passwd") String passwd,
                          @RequestParam("passwd_confirm") String passwd_confirm,
                          @RequestParam("surname") String surname,
                          @RequestParam("first_name") String first_name,
                          @RequestParam("last_name") String last_name,
                          @RequestParam("description") String description,
                          Model model) {
        if (!passwd.equals(passwd_confirm)){
            model.addAttribute("passwordError", true);
            return "student_registration";
        }

        Student st = new Student();
        st.setDescription(description);
        st.setFirst_name(first_name);
        st.setLast_name(last_name);
        st.setSurname(surname);
        st.setUsername(username);

        if (!user_dao.save_user(1L, username, passwd, st)){
            model.addAttribute("usernameError", "Пользователь с таким именем уже существует");
            return "registration";
        }

        return "redirect:/";
    }
}
