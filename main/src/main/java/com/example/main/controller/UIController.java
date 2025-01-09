package com.example.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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

    @GetMapping("/journalTeacher")
    public String journalTeacher() {
        return "journalTeacher";
    }

    @GetMapping("/editTask")
    public String editTask() {
        return "editTask";
    }

    @GetMapping("/taskTeacher")
    public String taskTeacher() {
        return "taskTeacher";
    }

    @GetMapping("/checkSolution")
    public String checkSolution() {
        return "checkSolution";
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @GetMapping("/testArt")
    public String testArt() {
        return "testart";
    }

    @GetMapping("/testArt2")
    public String testArt2() {
        return "testart2";
    }
    @GetMapping("/testArt3")
    public String testArt3() {
        return "testart3";
    }

    @GetMapping("/test11")
    public String test11() {
        return "test11";
    }
    
}
