<h1 align="center">
  <br>
  <img src="https://github.com/user-attachments/assets/e666e749-35e7-4a3a-ad08-1b692ffc0441" alt="Stepnote Logo" width="200" />
  <p>
  WalkNote
  <p>
</h1>
<h4 align="center">산책 기록 공유 플랫폼</h4>

---

## 📘 프로젝트 개요

> 사용자의 산책 기록을 공유하고, 실시간 알림과 피드 조합을 통해 **함께 걷는 경험을 제공하는 커뮤니티형 기록 플랫폼**입니다.  
> Firebase FCM, WebSocket, Domain Event 등 다양한 비동기 통신 구조를 실험하였으며,  
객체지향적 설계와 효율적인 데이터 조회 전략을 함께 고민했습니다.

---

## 🧭 개발 기간
2025.10 ~ 진행 중
- 인프라 및 기본 설계 완료
- FCM 푸시, WebSocket, 댓글 알림 구조 구현 중

---

## 👥 팀 구성
| 이름 | 역할                |
|------|-------------------|
| [**HyungGeun**](https://github.com/HyungGeun94) | 🖥️ Backend       |
| **Yejin** |  🧩 Product Planner                 |
| **Eunbin** | 🎨Designer        |
| **Junghoon** | 📱Android Developer |

## 이미지
<img width="202" height="443" alt="스크린샷 2025-10-31 오전 12 27 25" src="https://github.com/user-attachments/assets/1d5011ab-81d3-42d8-8e3c-e020cdf9b287" />
<img width="198" height="439" alt="스크린샷 2025-10-31 오전 12 27 58" src="https://github.com/user-attachments/assets/8fe6a6be-8e8e-40c0-984f-d5a032772cd5" />

<img width="204" height="439" alt="스크린샷 2025-10-31 오전 12 28 42" src="https://github.com/user-attachments/assets/32e8e779-e30a-450e-9d8f-bb929bcdcdd9" />


<br>

<img width="200" height="442" alt="스크린샷 2025-10-31 오전 12 27 13" src="https://github.com/user-attachments/assets/8d40edbd-bc33-434d-a6da-7672428d8a51" />

<img width="197" height="443" alt="스크린샷 2025-10-31 오전 12 28 06" src="https://github.com/user-attachments/assets/e4d082e0-03a7-4581-a4ca-65c6ac10a06d" />






---

## ⚙️ 기술 스택

| 분류 | 기술 |
|------|------|
| **Language & Framework** | Java 17 · Spring Boot 3.x |
| **DB / ORM** | MySQL 8 · JPA · QueryDSL |
| **Infra & DevOps** | AWS EC2 · S3 · Docker · Nginx · GitHub Actions |
| **Communication** | Firebase FCM · WebSocket |
| **Optimization** | Slice Pagination · BatchSize · 2-Step IN Query |
| **Test & Tools** | JUnit5 · Mockito · Postman · Swagger |

---

## 🧩 주요 기능 및 상세코드

### 🚶‍♀️ 산책 리포트 (WalkReport)
- 산책 거리, 시간, 칼로리 기록
- S3 Presigned URL 기반 이미지 업로드
- Soft Delete(`active = true`) 기반 조회

### 💬 댓글 / 대댓글 (Comment)
- Parent-Child 평면 구조 (Flat Tree Pattern)
- 댓글 작성 시 도메인 이벤트 발행 → 알림 트리거

### 🔔 알림 (Notification)
- `CommentCreatedEvent` 기반 FCM + DB 알림 저장
- `@TransactionalEventListener(phase = AFTER_COMMIT)` 비동기 구조
- `NotificationService` 내에서 DB 저장 + `FirebaseService` 호출

### ❤️ 피드 (Feed Assembler)
- `WalkReport` 기준 ID 페이징 + IN 조회
- 좋아요, 즐겨찾기, 팔로우 여부 집계 후 DTO 조립

### 🧍 팔로우 / 즐겨찾기
- 단방향 관계 유지, 별도 Repository에서 exists/IN 조회
- `isFollowed`, `isFavorite`, `likeCount` 등 FeedAssembler 통합 계산

---

## 🧱 아키텍처 설계  
```plaintext
Controller → Service → Repository → Entity  
               │
               ├── Domain Event → NotificationEventListener  
               └── FirebaseService (FCM Push)
```
- 단방향 중심 매핑, OneToMany Fetch Join 금지
- @BatchSize / IN 조회로 N+1 최소화
- 트랜잭션은 Service 계층에서 관리

---

## 🗂️ ERD (작성 예정)
> WalkReport · Comment · Notification · Follow 중심 구조

---

## 🚀 진행 상황

✅ Firebase Admin SDK 연동 및 로컬 테스트 완료  
✅ 댓글 → 알림 이벤트 리스너 구조 설계  
✅ WebSocket + JWT 인증 통합 테스트 완료  
⏳ FeedAssembler / NotificationService 테스트 코드 작성 중

---

## 🧪 테스트 구조
- `@WebMvcTest` : Controller API 검증
- `@DataJpaTest` : Repository 쿼리 검증
- `@SpringBootTest` : 통합 시나리오 검증
- `MockitoExtension` : 서비스 , 도메인 검증

---

## 📷 프로젝트 이미지 (작성 예정)
> FCM 테스트 결과, WebSocket 연결 화면 등 추가 예정

<!-- 이미지 추가 예시 -->
<!-- 
<img width="600" alt="FCM Test Result" src="https://github.com/user-attachments/assets/EXAMPLE.png" />
<img width="800" alt="Feed Structure" src="https://github.com/user-attachments/assets/EXAMPLE2.png" />
-->

---

## 💭 회고 (작성 예정)
>도메인을 직접 설계하면서 JPA와 트랜잭션의 감을 몸으로 익혔고,
> <br>
테스트 코드를 통해 로직이 흘러가는 길을 따라가 보며 안정감을 쌓았습니다.
> <br>
> QueryDSL로 쿼리를 다루며 SQL 감각을 되살렸고,
비동기 이벤트와 알림 구조를 실험하며 <br> 비동기 구조에 조금 더 익숙해졌습니다.
<br>

 

---

<p align="center"><i>© 2025 Stepnote Project</i></p>
