package huy.thuchanh.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SecurityController {
    private String getPrincipal() {
        String userName = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            userName=((UserDetails) principal).getUsername();
        }else {
            userName=principal.toString();
        }
        return userName;
    }
    @GetMapping( "/home")
    public String Homepage(Model model){
        model.addAttribute("user",getPrincipal());
        return "welcome";
    }
    @GetMapping("/admin")
    public String adminPage(Model model){
        model.addAttribute("admin",getPrincipal());
        return "admin";
    }
    @GetMapping("/accessDenied")
    public String accessDeniedPage(Model model){
        model.addAttribute("accessDenied",getPrincipal());
        return "accessDenied";
    }
}
