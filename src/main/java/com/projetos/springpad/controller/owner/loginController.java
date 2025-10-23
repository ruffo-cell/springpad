package com.projetos.springpad.controller.owner;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class loginController {


@GetMapping("/login")
public String login(Model model) {

    // toda a lógica desta pagina

    return "owner/login";
}
}
