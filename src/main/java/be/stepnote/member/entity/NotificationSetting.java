package be.stepnote.member.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean likeEnabled = true;
    private boolean commentEnabled = true;
    private boolean saveEnabled = true;
    private boolean followEnabled = true;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, unique = true)
    private Member member;

    public static NotificationSetting createDefault(Member member) {
        NotificationSetting setting = new NotificationSetting();
        setting.member = member;
        return setting;
    }

    public void update(boolean like, boolean comment, boolean save, boolean follow) {
        this.likeEnabled = like;
        this.commentEnabled = comment;
        this.saveEnabled = save;
        this.followEnabled = follow;
    }
}
