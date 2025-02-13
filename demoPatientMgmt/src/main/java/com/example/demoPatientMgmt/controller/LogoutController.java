package com.example.demoPatientMgmt.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogoutController {

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // Invalidate the current session, removing all attributes
        session.invalidate();
        // Redirect the user to the login page after logout
        return "redirect:/login";
    }
}