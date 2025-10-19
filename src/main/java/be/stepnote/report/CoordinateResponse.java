package be.stepnote.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CoordinateResponse {
    private double latitude;
    private double longitude;
}
