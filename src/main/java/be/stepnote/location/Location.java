package be.stepnote.location;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String region;     // ex) 서울, 경기
    private String district;   // ex) 강남구, 서초구

    // 정렬이나 검색용
    public static Location of(String region, String district) {
        Location loc = new Location();
        loc.region = region;
        loc.district = district;
        return loc;
    }
}
