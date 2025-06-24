package com.finalpoo.sistemavendas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String mostrarTelaLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String processarLogin(
            @RequestParam String email,
            @RequestParam String senha,
            Model model) {

        if (email.equals("admin@email.com") && senha.equals("123")) {
            return "redirect:/dashboard";
        } else {
            model.addAttribute("erro", "Credenciais inválidas.");
            return "login";
        }
    }
}


