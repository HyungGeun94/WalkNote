package be.stepnote.global.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SliceResponse<T> {
    private List<T> content;
    private boolean hasNext;
    private int pageNumber;
    private int pageSize;
    private Integer totalCount;

    public static <T> SliceResponse<T> of(Slice<T> slice, Integer totalCount) {
        return SliceResponse.<T>builder()
            .content(slice.getContent())
            .hasNext(slice.hasNext())
            .pageNumber(slice.getNumber())
            .pageSize(slice.getSize())
            .totalCount(totalCount)
            .build();
    }
}
