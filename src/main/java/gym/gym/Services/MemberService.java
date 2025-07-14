package gym.gym.Services;

import gym.gym.Models.Admin;
import gym.gym.Models.Gym;
import gym.gym.Models.Member;
import gym.gym.Repositories.GymRepo;
import gym.gym.Repositories.MemberRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    @Autowired
    private MemberRepo memberRepo;
    @Autowired
    private GymRepo gymRepo;


    public List<Member> getAllMembers() {
        return memberRepo.findAll();
    }

    public Optional <Member> getMemberById(Long id) {
        return memberRepo.findById(id); // ✅ retourne Optional<Member>
    }

    public Member saveMember(Member memberDto) {

        Gym gym = gymRepo.findById(memberDto.getGym().getId()).orElse(null);

        // 2. Créer un nouveau membre
        Member member = new Member();
        member.setFirstName(memberDto.getFirstName());
        member.setLastName(memberDto.getLastName());
        member.setEmail(memberDto.getEmail());
        member.setPhone(memberDto.getPhone());
        member.setPassword(memberDto.getPassword());
        member.setGym(gym);


        // save_member
        return memberRepo.save(member);

    }

    public void deleteMember(Long id) {
        memberRepo.deleteById(id);
    }


    public Optional<Member> getMemberByEmail(String email) {
        return memberRepo.findByEmail(email);
    }

}
