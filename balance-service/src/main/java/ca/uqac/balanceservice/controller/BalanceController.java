package ca.uqac.balanceservice.controller;

import ca.uqac.balanceservice.service.BalanceService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/")
public class BalanceController {

    private final BalanceService bs;

    public BalanceController(BalanceService bs) {
        this.bs = bs;
    }

    @GetMapping("/balance")
    public Map<String, Object> getUserBalance(@RequestParam String token) {
        return bs.getUserBalance(token);
    }
}
