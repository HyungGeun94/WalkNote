package be.stepnote.report.comment;

import static be.stepnote.report.comment.QWalkReportComment.walkReportComment;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WalkReportCommentRepositoryImpl implements WalkReportCommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Map<Long, Long> countCommentsByReportIds(List<Long> reportIds) {
        List<Tuple> results = queryFactory
            .select(walkReportComment.walkReport.id, walkReportComment.count())
            .from(walkReportComment)
            .where(walkReportComment.walkReport.id.in(reportIds))
            .groupBy(walkReportComment.walkReport.id)
            .fetch();

        return results.stream()
            .collect(Collectors.toMap(
                tuple -> tuple.get(walkReportComment.walkReport.id),
                tuple -> tuple.get(walkReportComment.count())
            ));
    }
}
