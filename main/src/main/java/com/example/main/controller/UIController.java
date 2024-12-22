package com.example.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UIController {

     @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/menuTeacher")
    public String menuTeacher() {
        return "menuTeacher";
    }

    @GetMapping("/menuStudent")
    public String menuStudent() {
        return "menuStudent";
    }

    @GetMapping("/taskStudent")
    public String taskStudent() {
        return "taskStudent";
    }

    @GetMapping("/profTeacher")
    public String profTeacher() {
        return "profTeacher";
    }

    @GetMapping("/adminTeacher")
    public String adminTeacher() {
        return "adminTeacher";
    }

    @GetMapping("/editStudent")
    public String editStudent() {
        return "editStudent";
    }
}