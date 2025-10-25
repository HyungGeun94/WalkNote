<h1 align="center">
  <br>
  <img src="https://github.com/user-attachments/assets/STEPLOGO_TEMP.png" alt="Stepnote Logo" width="200" />
  <p>
  Stepnote (WalkNote)
  <p>
</h1>
<h4 align="center">도메인 이벤트 기반 산책 기록 공유 플랫폼</h4>

---

## 📘 프로젝트 개요

> 사용자의 산책 기록을 공유하고, 실시간 알림과 피드 조합을 통해 **함께 걷는 경험을 제공하는 커뮤니티형 기록 플랫폼**입니다.  
> Firebase FCM, WebSocket, Domain Event 등 다양한 비동기 통신 구조를 실험하며,  
> **객체지향적 영속성 관리와 SQL스러운 조회 전략**의 균형을 추구합니다.

---

## 🧭 개발 기간
2025.10 ~ 진행 중
- 인프라 및 기본 설계 완료
- FCM 푸시, WebSocket, 댓글 알림 구조 구현 중

---

## 👥 팀 구성
| 이름 | 역할 |
|------|------|
| [**HyungGeun**](https://github.com/HyungGeun94) | Backend (개인) |

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
- “조회는 SQL스럽게, 조립은 자바스럽게” 구조 적용

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
- DTO 프로젝션 기반 데이터 조립

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
- `MockitoExtension` / `ArgumentCaptor` 기반 단위 테스트

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
> “Stepnote는 객체지향적 설계와 데이터 중심 최적화를 조화시키려는 첫 시도였다.”  
> “실시간성과 안정성을 동시에 추구한 도전의 기록.”

---

<p align="center"><i>© 2025 Stepnote Project</i></p>