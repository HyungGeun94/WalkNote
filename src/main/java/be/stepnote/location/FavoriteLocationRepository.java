package be.stepnote.location;


import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FavoriteLocationRepository extends JpaRepository<FavoriteLocation, Long> {

    @Query("""
        select f
        from FavoriteLocation f
        join Member m on f.member.id = m.id
        join Location l on f.location.id = l.id
        where m.id = :memberId
        order by l.region asc, l.district asc
    """)
    List<FavoriteLocation> findAllByMemberId(@Param("memberId") Long memberId);

    boolean existsByMemberIdAndLocationId(Long memberId, Long locationId);

    void deleteByMemberIdAndLocationId(Long memberId, Long locationId);
}
