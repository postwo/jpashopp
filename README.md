데이터베이스 초기화 전략 - DDL AUTO 옵션
none: 사용하지 않음
create: 기존 테이블 삭제 후 테이블 생성
create-drop: 기존 테이블 삭제 후 테이블 생성, 종료 시점에 테이블 삭제
update: 변경된 스키마 적용
validate: 엔티티와 테이블 정상 매핑 확인


ALT+INSERT = test코드 작성




다대다 매핑하기 - 사용X
여러 JPA 책에서도 언급되지만, 다대다 매핑은 실무에서는 사용하지 않는 매핑 관계입니다. 
다대다 매핑을 사용하지 않는 이유는 연결 테이블에는 컬럼을 추가할 수 없기 때문입니다. 연결 테이블에는 조인 
컬럼 뿐 아니라 추가 컬럼들이 필요한 경우가 많습니다. 또한 엔티티를 조회할 때 어떤 쿼리문이 실행될지 예측하기도 쉽지 않습니다. 
따라서 다대다 매핑이 아닌 일대다, 다대일 매핑으로 설정하시면 됩니다.



상품관리하고 상품상세보기 에러들 꼭해결하기 


# book store 이거는 그냥 연습정도
