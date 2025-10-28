package be.stepnote.report.walk.entity;

import static org.assertj.core.api.Assertions.assertThat;

import be.stepnote.report.image.WalkReportImage;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WalkReportTest {


    private WalkReport report;

    @BeforeEach
    void setUp() {
        report = WalkReport.create(
            3.5, 4000, 200.0,
            LocalDateTime.now().minusHours(1),
            LocalDateTime.now(),
            60,
            "테스트 리포트",
            "날씨 좋음",
            "서울",
            "서울",
            List.of("img1.png", "img2.png")
        );
    }

    @Test
    void create메서드로_리포트_생성된다() {


        assertThat(report.getImages()).hasSize(2);
        assertThat(report.getTitle()).isEqualTo("테스트 리포트");
        assertThat(report.isPublic()).isFalse();
        assertThat(report.isStaticHide()).isFalse();
        assertThat(report.isActive()).isTrue();
    }


    @Test
    void changeActive로_softDelete된다() {

        report.changeActive();

        assertThat(report.isActive()).isFalse();
    }

    @Test
    void update로_제목_내용_이미지_변경된다() {

        report.update("새 제목", "새 내용", List.of("updated.png"));

        assertThat(report.getTitle()).isEqualTo("새 제목");
        assertThat(report.getImages()).extracting("url").containsExactly("updated.png");
    }

    @Test
    void upload_로_제목_내용_이미지_통계공개_피드공개여부변경된다() {

        report.upload("새 제목2", "세 내용2", List.of("image11.png","image22.png"),true,true);

        assertThat(report.getTitle()).isEqualTo("새 제목2");
        assertThat(report.getImages()).extracting("url").containsExactly("image11.png","image22.png");
        assertThat(report.isPublic()).isTrue();
        assertThat(report.isStaticHide()).isTrue();


    }



    @Test
    void findFirstImageUrl_첫번쨰_이미지반환() {

        assertThat(report.findFirstImageUrl()).isEqualTo("img1.png");
    }

    @Test
    void addImage로_이미지추가시_양방향관계_유지된다() {
        WalkReportImage newImage = new WalkReportImage("new.png");
        report.addImage(newImage);

        assertThat(report.getImages()).contains(newImage);
        assertThat(newImage.getWalkReport()).isEqualTo(report);
    }
}