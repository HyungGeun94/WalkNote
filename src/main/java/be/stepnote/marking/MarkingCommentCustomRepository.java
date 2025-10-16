package be.stepnote.marking;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface MarkingCommentCustomRepository {

    Slice<MarkingComment> findNearby(double latitude, double longitude, double radiusKm, Pageable pageable);



}
