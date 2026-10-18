package com.altn72duvernetndouga.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ConnexionController {
    @GetMapping("/connexion")
    public String afficherConnexion(){
        return "connexion";
    }
}
