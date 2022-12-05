package ca.uqac.loginservice.controller;

import ca.uqac.loginservice.service.LoginService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/login/")
public class LoginController {

    private final LoginService ls;

    public LoginController(LoginService ls) {
        this.ls = ls;
    }

    @PostMapping("/tokenFromUser")
    public String getTokenFromUser(@RequestBody String rawUser) {
        return ls.getTokenFromUser(rawUser);
    }

    @GetMapping("/userFromToken")
    public String getUserFromToken(@RequestParam String token) {
        return ls.getUserFromToken(token);
    }
}
