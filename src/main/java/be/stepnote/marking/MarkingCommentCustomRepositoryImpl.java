package be.stepnote.marking;

import static be.stepnote.marking.QMarkingComment.*;
import static be.stepnote.member.entity.QMember.*;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MarkingCommentCustomRepositoryImpl implements MarkingCommentCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<MarkingComment> findNearby(double latitude, double longitude, double radiusKm, Pageable pageable) {

        NumberExpression<Double> distance = Expressions.numberTemplate(Double.class,
            "6371 * acos(cos(radians({0})) * cos(radians({1})) * " +
                "cos(radians({2}) - radians({3})) + sin(radians({0})) * sin(radians({1})))",
            latitude, markingComment.latitude, markingComment.longitude, longitude);

        // 1개 더 가져와서 다음 페이지 존재 여부 판단
        List<MarkingComment> results = queryFactory
            .selectFrom(markingComment)
            .join(markingComment.createdBy, member).fetchJoin()
            .where(distance.loe(radiusKm))
            .orderBy(distance.asc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize() + 1)
            .fetch();

        boolean hasNext = false;
        if (results.size() > pageable.getPageSize()) {
            hasNext = true;
            results.remove(results.size() - 1);
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }
}
