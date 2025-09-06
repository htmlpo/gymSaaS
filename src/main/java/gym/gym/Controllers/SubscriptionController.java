package gym.gym.Controllers;

import gym.gym.Models.Subscription;
import gym.gym.Services.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @GetMapping
    public List<Subscription> getAllSubscriptions(@RequestParam Long gymId) {
        return subscriptionService.getAllSubscriptionsByGymId(gymId);
    }

    @PostMapping
    public Subscription createSubscription(@RequestBody Subscription subscription) {
        return subscriptionService.saveSubscription(subscription);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Subscription> getSubscriptionById(@PathVariable Long id, @RequestParam Long gymId) {
        Optional<Subscription> subscription = subscriptionService.getSubscriptionByIdAndGymId(id, gymId);
        return subscription.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable Long id, @RequestParam Long gymId) {
        if (subscriptionService.getSubscriptionByIdAndGymId(id, gymId).isPresent()) {
            subscriptionService.deleteSubscription(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}