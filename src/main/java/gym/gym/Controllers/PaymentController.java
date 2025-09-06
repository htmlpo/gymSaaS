package gym.gym.Controllers;

import gym.gym.Models.Payment;
import gym.gym.Models.Subscription;
import gym.gym.Services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping
    public List<Payment> getAllPayments(@RequestParam Long gymId) {
        return paymentService.getAllPaymentsByGymId(gymId);
    }

    @PostMapping
    public Payment createPayment(@RequestBody Payment payment) {
        return paymentService.savePayment(payment);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id, @RequestParam Long gymId) {
        Optional<Payment> payment = paymentService.getPaymentByIdAndGymId(id, gymId);
        return payment.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id, @RequestParam Long gymId) {
        if (paymentService.getPaymentByIdAndGymId(id, gymId).isPresent()) {
            paymentService.deletePayment(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<Payment>> getPaymentsByMemberId(@PathVariable Long memberId) {
        List<Payment> payments = paymentService.getPaymentsByMemberId(memberId);
        if (payments.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(payments);
    }

}
