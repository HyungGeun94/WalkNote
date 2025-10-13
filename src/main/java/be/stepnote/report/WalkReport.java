package be.stepnote.report;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
public class WalkReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double distance;      // 총 거리
    private int steps;
    private long duration;        // 총 시간(초 단위)
    private double avgSpeed;      // 평균 속도
    private double calorie;       // 칼로리

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime createdAt;

    private String title;
    private String content;


}