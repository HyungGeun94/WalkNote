package be.stepnote.report.block;

import be.stepnote.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
    name = "block",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"blocker_id", "blocked_id"})
    }
)
public class Block {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 차단 주체 (나)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blocker_id", nullable = false)
    private Member blocker;

    // 차단 대상 (상대)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blocked_id", nullable = false)
    private Member blocked;

    private LocalDateTime createdAt;

    public static Block create(Member blocker, Member blocked) {
        Block block = new Block();
        block.blocker = blocker;
        block.blocked = blocked;
        block.createdAt = LocalDateTime.now();
        return block;
    }
}
