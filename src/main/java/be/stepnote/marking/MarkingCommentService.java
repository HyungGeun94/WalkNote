package be.stepnote.marking;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class MarkingCommentService {

    private final MarkingCommentRepository markingCommentRepository;


    public void create(MarkingCommentRequestDTO markingCommentRequestDTO) {

        MarkingComment markingComment = MarkingComment.builder()
            .latitude(markingCommentRequestDTO.getLatitude())
            .longitude(markingCommentRequestDTO.getLongitude())
            .comment(markingCommentRequestDTO.getComment())
            .build();

        markingCommentRepository.save(markingComment);


    }

    public Slice<MarkingCommentResponseDTO> findNearby(double latitude, double longitude, double radiusKm, int page) {
        Pageable pageable = PageRequest.of(page, 5);

        Slice<MarkingComment> slice = markingCommentRepository.findNearby(latitude, longitude, radiusKm,
            pageable);

        return slice.map(MarkingCommentResponseDTO::from);
    }
}
