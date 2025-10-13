package be.stepnote.location;

import be.stepnote.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {

    Optional<Location> findTopByMemberOrderByCreatedAtDesc(Member member);
}
