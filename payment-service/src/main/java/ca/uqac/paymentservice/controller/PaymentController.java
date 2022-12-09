package ca.uqac.paymentservice.controller;

import ca.uqac.paymentservice.service.PaymentService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/")
public class PaymentController {

    private final PaymentService ps;

    public PaymentController(PaymentService ps) {
        this.ps = ps;
    }

    @PostMapping("/payment")
    public Map<String, Object> pay(@RequestBody String rawPayment) {
        return ps.pay(rawPayment);
    }
}
