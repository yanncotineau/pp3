package ca.uqac.registrationservice.controller;

import ca.uqac.registrationservice.service.RegistrationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
public class RegistrationController {

    private final RegistrationService rs;

    public RegistrationController(RegistrationService rs) {
        this.rs = rs;
    }

    @PostMapping("/registration")
    public boolean registerUser(@RequestBody String rawUser) {
        return rs.registerUser(rawUser);
    }
}
