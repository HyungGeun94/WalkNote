package be.stepnote.report.walk.repository;

import static be.stepnote.report.favorite.QWalkReportFavorite.walkReportFavorite;
import static be.stepnote.report.walk.QWalkReport.walkReport;

import be.stepnote.member.entity.Member;
import be.stepnote.report.walk.QWalkReport;
import be.stepnote.report.walk.entity.WalkReport;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class WalkReportCustomRepositoryImpl implements WalkReportCustomRepository {


    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<WalkReport> findReportSummaries(Member member,Pageable pageable, boolean publicVisibility) {

        OrderSpecifier<?> order = getOrder(pageable, walkReport);

        BooleanBuilder condition = new BooleanBuilder();
        condition.and(walkReport.createdBy.id.eq(member.getId())); // 기본 조건

        if (publicVisibility) {
            condition.and(walkReport.isPublic.isTrue()); //  공개 게시글만 필터링
        }

        List<WalkReport> results = queryFactory
            .selectFrom(walkReport)
            .where(condition)
            .orderBy(order)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize() + 1)
            .fetch();

        boolean hasNext = results.size() > pageable.getPageSize();
        if (hasNext) results.remove(pageable.getPageSize());

        return new SliceImpl<>(results, pageable, hasNext);
    }

    @Override
    public Slice<WalkReport> findMyFavorites(Member member, Pageable pageable) {

        OrderSpecifier<?> order = getOrder(pageable, walkReport);

        List<WalkReport> results = queryFactory
            .selectFrom(walkReport)
            .join(walkReportFavorite)
            .on(walkReportFavorite.walkReport.eq(walkReport))
            .where(walkReportFavorite.member.eq(member))
            .orderBy(order)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize() + 1)
            .fetch();

        boolean hasNext = results.size() > pageable.getPageSize();
        if (hasNext) results.remove(pageable.getPageSize());

        return new SliceImpl<>(results, pageable, hasNext);
    }

    private static OrderSpecifier<?> getOrder(Pageable pageable, QWalkReport wr) {
        boolean isAsc = pageable.getSort().stream()
            .anyMatch(order -> order.isAscending());

        OrderSpecifier<?> order = isAsc
            ? wr.createdAt.asc()
            : wr.createdAt.desc();
        return order;
    }
}