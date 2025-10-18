package be.stepnote.member.repository;

import be.stepnote.member.entity.Member;
import java.util.Optional;
import javax.swing.text.html.Option;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {


    Boolean existsByUsername(String username);

    Optional<Member> findByUsername(String username);
}
