package be.stepnote.alarm;

import be.stepnote.member.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("""
    select n
    from Notification n
    join fetch Member s on n.sender.id = s.id
    where n.receiver.id = :receiverId
    order by n.id desc
    """)
    Slice<Notification> findAllByReceiverId(
        @Param("receiverId") Long receiverId,
        Pageable pageable
    );

    Integer countByReceiver(Member member);
}
