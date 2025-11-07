package be.stepnote.location;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LocationSeeder implements CommandLineRunner {

    private final LocationRepository locationRepository;

    @Override
    public void run(String... args) {
        if (locationRepository.count() == 0) {
            locationRepository.saveAll(List.of(
                Location.of("서울", "강남구"),
                Location.of("서울", "서초구"),
                Location.of("서울", "송파구")
            ));
        }
    }
}
