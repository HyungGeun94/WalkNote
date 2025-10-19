package be.stepnote.report.like;

import static be.stepnote.report.like.QWalkReportLike.walkReportLike;

import be.stepnote.member.entity.Member;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WalkReportLikeRepositoryImpl implements WalkReportLikeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Map<Long, Long> countLikesByReportIds(List<Long> reportIds) {
        List<Tuple> results = queryFactory
            .select(walkReportLike.walkReport.id, walkReportLike.count())
            .from(walkReportLike)
            .where(walkReportLike.walkReport.id.in(reportIds))
            .groupBy(walkReportLike.walkReport.id)
            .fetch();

        return results.stream()
            .collect(Collectors.toMap(
                tuple -> tuple.get(walkReportLike.walkReport.id),
                tuple -> tuple.get(walkReportLike.count())
            ));
    }

    @Override
    public List<Long> findLikedReportIds(List<Long> reportIds, Member member) {
        return queryFactory
            .select(walkReportLike.walkReport.id)
            .from(walkReportLike)
            .where(
                walkReportLike.walkReport.id.in(reportIds),
                walkReportLike.member.eq(member)
            )
            .fetch();
    }
}
