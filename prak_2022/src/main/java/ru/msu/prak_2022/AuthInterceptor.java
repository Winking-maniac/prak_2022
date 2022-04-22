package ru.msu.prak_2022;

import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

//package ru.msu.prak_2022;
//
//import org.springframework.security.core.Authentication;
//import org.springframework.ui.Model;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
public class AuthInterceptor {
    public static void add_auth(Model model, Authentication auth) {
        model.addAttribute("authenticated", auth != null);
        if (auth != null) {
            model.addAttribute("user_type", auth.getAuthorities().toArray()[0]);
            model.addAttribute("username", auth.getName());
        }
    }
}
