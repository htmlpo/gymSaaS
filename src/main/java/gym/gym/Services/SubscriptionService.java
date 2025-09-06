package gym.gym.Services;


import gym.gym.Models.Payment;
import gym.gym.Models.Subscription;
import gym.gym.Repositories.PaymentRepo;
import gym.gym.Repositories.SubscripitonRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionService {

    @Autowired
    private SubscripitonRepo subscriptionRepository;
    @Autowired
    private PaymentRepo paymentRepo;

    public List<Subscription> getAllSubscriptionsByGymId(Long gymId) {
        return subscriptionRepository.findByGym_Id(gymId);
    }

    public Optional<Subscription> getSubscriptionByIdAndGymId(Long id, Long gymId) {
        return subscriptionRepository.findById(id)
            .filter(subscription -> subscription.getGym() != null && subscription.getGym().getId().equals(gymId));
    }

    public Subscription saveSubscription(Subscription subscription) {
      Subscription  savedSubscription =subscriptionRepository.save(subscription);


        Payment payment = new Payment();
        payment.setMember(subscription.getMember());
        payment.setAmount(subscription.getAmount());
        payment.setPaymentDate(LocalDate.now());
        payment.setGym(subscription.getGym());
        payment.setSubscription(savedSubscription);

        paymentRepo.save(payment);




      return savedSubscription;
    }

    public void deleteSubscription(Long subscriptionId) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new RuntimeException("Subscription Not Found."));


        subscriptionRepository.delete(subscription);
    }


    public List<Subscription> getSubscriptionsByMemberId(Long memberId) {
        return subscriptionRepository.findByMember_Id(memberId);  // Important : accède au champ de l'entité
    }

    public Subscription updateSubscription(Subscription subscription) {
        Subscription updatedSubscription = subscriptionRepository.save(subscription);

        Payment payment = new Payment();
        payment.setMember(subscription.getMember());
        payment.setAmount(subscription.getAmount());
        payment.setGym(subscription.getGym());
        payment.setPaymentDate(LocalDate.now());

        paymentRepo.save(payment); // Nouveau paiement

        return updatedSubscription;
    }








}
