package be.stepnote.report.walk.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@NoArgsConstructor
public class WalkReportSearchCondition {

    private int page = 0;
    private String sort = "LATEST";
    private boolean publicVisibility = false;

    private static final int DEFAULT_PAGE_SIZE = 4;

    public Pageable toPageable() {
        Sort sortOption = sort.equalsIgnoreCase("OLDEST")
            ? Sort.by(Sort.Direction.ASC, "createdAt")
            : Sort.by(Sort.Direction.DESC, "createdAt");
        return PageRequest.of(page, DEFAULT_PAGE_SIZE, sortOption);
    }
}
