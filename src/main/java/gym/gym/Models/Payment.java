package gym.gym.Models;


import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Payment {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private LocalDate paymentDate;
        private double amount;

        @OneToOne
        @JoinColumn(name = "subscription_id")
        private Subscription subscription;

        @ManyToOne
        @JoinColumn(name = "member_id")
        private Member member;


        @ManyToOne
        @JoinColumn(name = "gym_id")
        private Gym gym;

        public Gym getGym() {
                return gym;
        }

        public void setGym(Gym gym) {
                this.gym = gym;
        }





        public double getAmount() {
                return amount;
        }

        public void setAmount(double amount) {
                this.amount = amount;
        }

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public LocalDate getPaymentDate() {
                return paymentDate;
        }

        public void setPaymentDate(LocalDate paymentDate) {
                this.paymentDate = paymentDate;
        }

        public Subscription getSubscription() {
                return subscription;
        }

        public void setSubscription(Subscription subscription) {
                this.subscription = subscription;
        }

        public Member getMember() {
                return member;
        }

        public void setMember(Member member) {
                this.member = member;
        }
}
