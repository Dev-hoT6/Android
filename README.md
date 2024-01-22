# Review The Review - Android
인공지능을 통해 리뷰의 품질을 평가해 리뷰 적립금을 차등적으로 지급하고, 상품과 관련없는 리뷰 작성을 막아주는 서비스인 Review The Review의 안드로이드 데모 앱입니다

## 주요 기능
- 리뷰 이미지 등록시 해당 이미지가 본 상품의 카테고리에 속하는지 검사하여 관련없는 이미지는 리뷰에 사용 불가
- 리뷰 텍스트 입력시 해당 리뷰글이 본 상품 정보와 관련있는 리뷰인지를 판단하여 관련없는 리뷰글을 업로드하지 못하도록 막는 기능

## 기술 스택

| 분야          | 기술 스택                                           |
|---------------|-----------------------------------------------------|
| **언어**  | `Kotlin`                                           |
| **UI** | `Xml, ViewBinding`                                          |
| **구조** | `MVVM`              |
| **비동기 처리** | `Flow, Coroutine`              |
| **통신** | `Retrofit, Okhttp3`              |
| **모델** | `onnxruntime-android`              |

## 프로젝트 세부 구조
- ai
  - classifier : 이미지 분류를 담당하는 클래스 및 인터페이스
  - review : 리뷰 텍스트 분류를 담당하는 클래스 및 인터페이스
  - ImageHandler.kt : 이미지 url를 사용하여 이미지를 불러오거나, 이미지 크기를 조정하는 등 이미지와 관련된 작업을 담당하는 클래스
  - ModelManager.kt : onnx 파일 형식의 모델을 관리하는 클래스
- base
  - dialog : 앱에서 기본적으로 사용하는 다이얼로그 클래스 (=팝업)
  - retrofit : 서버 통신에 사용되는 Retrofit 인스턴스
  - ViewBindingActivity.kt : ViewBinding을 사용하는 Activity에 공통적으로 작성되는 부분을 미리 작성한 추상 클래스
- data
  - product : 상품 상세, 상품목록과 같이 상품 도메인 데이터에 접근하거나 데이터를 요청하는 역할
  - review : 리뷰 목록 조회, 리뷰 작성과 같이 리뷰 도메인 데이터에 접근하거나 데이터를 요청하는 역할
- domain
  - product : 상품 도메인에 대한 데이터 형식 및 로직 구현
  - review : 리뷰 도메인에 대한 데이터 형식 및 로직 구현
- screen
  - home : 홈 화면 (처음 화면)
  - product : 상품 상세 화면
  - review : 리뷰 화면
- utils
  - CategoryUtils.kt : 이미지 모델이 예측한 카테고리 index와 서버에 저장된 카테고리 index를 서로 매치시키는 함수
  - dpToPx.kt : dp단위를 px 단위로 변경하는 함수
  - listUtils.kt : List와 같은 iterable 타입에 적용될 수 있는 유틸 함수

## 시연 영상
https://github.com/Dev-hoT6/Android/assets/39579912/0fe8318d-2f77-4234-8bc2-6399ceea04af  

https://github.com/Dev-hoT6/Android/assets/39579912/3818739b-923c-470f-925e-3dfdceebad0e  
