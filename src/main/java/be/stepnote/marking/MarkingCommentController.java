package be.stepnote.marking;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/walk/marking")
public class MarkingCommentController {

    private final MarkingCommentService markingCommentService;



    @GetMapping()
    public Slice<MarkingCommentResponseDTO> getNearbyComments(
        @RequestParam double latitude,
        @RequestParam double longitude,
        @RequestParam(defaultValue = "0") int page

    ) {

        return markingCommentService.findNearby(latitude, longitude, 2.0,page);
    }



    @PostMapping()
    public ResponseEntity<?> post(@RequestBody MarkingCommentRequestDTO markingCommentRequestDTO) {

        markingCommentService.create(markingCommentRequestDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }



}
