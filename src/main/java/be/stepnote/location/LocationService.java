package be.stepnote.location;

import be.stepnote.member.entity.Member;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class LocationService {

    private final LocationRepository locationRepository;


    @Transactional
    public Location save(Member member, Double lat, Double lon) {

        Location location = Location.builder().latitude(lat).longitude(lon)
            .createdAt(LocalDateTime.now())
            .member(member).build();

        return locationRepository.save(location);

    }

}
