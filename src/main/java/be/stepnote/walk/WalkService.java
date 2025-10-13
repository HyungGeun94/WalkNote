package be.stepnote.walk;

import be.stepnote.location.Location;
import be.stepnote.location.LocationRepository;
import be.stepnote.member.entity.Member;
import be.stepnote.report.WalkReport;
import be.stepnote.report.WalkReportRepository;
import be.stepnote.websocket.WebSocketRequestDTO;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class WalkService {

    private final WalkRepository walkRepository;
    private final LocationRepository locationRepository;
    private final WalkReportRepository walkReportRepository;

    public void startWalk(Member member, WebSocketRequestDTO dto) {

        Walk walk = Walk.builder().member(member)
            .startedAt(LocalDateTime.now())
            .build();
        walkRepository.save(walk);

        // 첫 좌표를 Walk에 연결
        Location firstLocation = locationRepository
            .findTopByMemberOrderByCreatedAtDesc(member)
            .orElseThrow();

        firstLocation.addWalk(walk);
    }

    @Transactional
    public void recordWalk(Member user, WebSocketRequestDTO dto) {
        Walk current = walkRepository.findFirstByMemberAndEndedAtIsNullOrderByStartedAtDesc(user)
            .orElseThrow(() -> new IllegalStateException("진행 중인 산책이 없습니다."));

        Location location = locationRepository
            .findTopByMemberOrderByCreatedAtDesc(user)
            .orElseThrow();

        location.addWalk(current);
    }


    @Transactional
    public void stopWalk(Member user, WebSocketRequestDTO dto) {
        Walk current = walkRepository.findFirstByMemberAndEndedAtIsNullOrderByStartedAtDesc(user)
            .orElseThrow(() -> new IllegalStateException("진행 중인 산책이 없습니다."));
        current.changeEndedAt(LocalDateTime.now());

        // 🚩 리포트가 없으면 새로 생성
        if (current.getWalkReport() == null) {
            WalkReport report = new WalkReport();
            walkReportRepository.save(report);
            current.changeWalkReport(report);
        }

        // 종료 시점 좌표도 Walk에 연결
        Location lastLocation = locationRepository
            .findTopByMemberOrderByCreatedAtDesc(user)
            .orElseThrow();
        lastLocation.addWalk(current);
    }
}
