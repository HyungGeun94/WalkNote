package be.stepnote.report.comment;

import java.util.List;
import java.util.Map;

public interface WalkReportCommentRepositoryCustom {

    Map<Long, Long> countCommentsByReportIds(List<Long> reportIds);


}
