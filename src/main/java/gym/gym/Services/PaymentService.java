package gym.gym.Services;


import gym.gym.Models.Payment;
import gym.gym.Repositories.PaymentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class PaymentService {

    @Autowired
    private PaymentRepo paymentRepo;

    public List<Payment> getAllPaymentsByGymId(Long gymId) {
        return paymentRepo.findByGym_Id(gymId);
    }

    public Optional<Payment> getPaymentByIdAndGymId(Long id, Long gymId) {
        return paymentRepo.findById(id)
                .filter(payment -> payment.getGym() != null && payment.getGym().getId().equals(gymId));
    }

    public Payment savePayment(Payment payment) {
        return paymentRepo.save(payment); // ✅ retourne Member
    }

    public void deletePayment (Long id) {
        paymentRepo.deleteById(id);
    }


    public List<Payment> getPaymentsByMemberId(Long memberId) {
        return paymentRepo.findByMemberId(memberId);

    }






}
