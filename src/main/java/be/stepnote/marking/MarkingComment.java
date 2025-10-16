package be.stepnote.marking;

import static jakarta.persistence.FetchType.LAZY;

import be.stepnote.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
public class MarkingComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comment;

    private double latitude;
    private double longitude;

    @CreatedDate
    private LocalDateTime createdAt;

    @CreatedBy
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member createdBy;

    @Builder
    public MarkingComment(String comment, double latitude, double longitude) {
        this.comment = comment;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
