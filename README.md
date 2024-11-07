## 🛠️ 기능 목록

### 1. 상품 및 행사 목록

- [x] 상품 목록을 파일 입출력을 통해 불러온다.
    - `src/main/resources/products.md`
- [x] 행사 목록을 파일 입출력을 통해 불러온다.
    - `src/main/resources/promotions.md`
- [x] 환영 인사와 함께 상품명, 가격, 프로모션 이름, 재고를 안내한다.
    ```
    안녕하세요. W편의점입니다.
    현재 보유하고 있는 상품입니다.
  
  - {상품명} {가격} {개수}개 {프로모션 이름}
    ...
    ``` 
    - [x] 가격과 개수에는 1,000단위 쉼표(`,`) 포맷을 적용한다.
- [x] 재고가 0개인 상품의 재고는 `재고 없음`으로 출력한다.

##### 예외 처리

- [ ] 모든 상품의 재고가 0개이면 `IllegalStateException` 을 발생시키고 프로그램을 종료한다.

### 2. 구매 요청

- [x] 구매 요청 메시지를 출력한다.
    ```
    구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])
    ```
- [x] 구매할 상품과 수량을 입력 받는다.
    ```
    [콜라-10],[사이다-3]
    ```
- [x] 입력값에 대해, 상품명과 수량 데이터를 추출한다.
- [ ] 입력값을 검증한다.
    - [x] 빈 값인지 확인한다.
    - [x] 상품과 수량 형식이 올바른지 확인한다.
    - [x] 구매 수량에 숫자 외에 다른 문자가 있는지 확인한다.
    - [x] 구매 수량이 양의 정수인지 확인한다.
    - [x] 상품명이 존재하는 상품인지 확인한다.
    - [x] 상품을 중복으로 입력했는지 확인한다.
    - [ ] 구매 수량이 재고 수량을 초과하는지 확인한다.

##### 예외 처리

- [x] 입력값이 유효하지 않으면 재입력 받는다.
- [x] `IllegalArgumentException` 을 발생시킨다.
- [x] "[ERROR]"로 시작하는 오류 메시지와 안내를 출력한다.
    - [x] 기타 잘못된 입력의 경우
        - `[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.`
    - [x] 구매할 상품과 수량 형식이 올바르지 않은 경우
        - `[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.`
    - [x] 존재하지 않는 상품을 입력한 경우
        - `[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.`

### 3. 재고 확인

- [ ] 각 상품의 재고 수량을 고려하여 결제 가능 여부를 확인한다.
    - [ ] 프로모션 진행 중인 상품의 경우, (구매 수량) <= (프로모션 재고) + (일반 재고)
    - [ ] 일반 상품의 경우, (구매 수량) <= (일반 재고)

##### 예외 처리

- [ ] 구매 수량이 재고 수량을 초과하면 구매 요청을 다시 실행한다.
- [ ] `IllegalArgumentException` 을 발생시킨다.
- [ ] "[ERROR]"로 시작하는 오류 메시지와 안내를 출력한다.
    - [ ] 구매 수량이 재고 수량을 초과한 경우
        - `[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.`

### 4. 프로모션 적용 상품 확인

- [ ] 각 상품별 프로모션 존재 여부를 확인한다.
- [ ] 프로모션이 존재하는 상품의 경우, 오늘 날짜(now)가 프로모션 기간 내에 포함되는지 확인한다.

### 5. 프로모션 혜택 안내

- [ ] 프로모션 적용이 가능한 상품에 대해, 해당 수량만큼 가져왔는지 확인한다.
    - [ ] 1+1 프로모션의 경우, 구매 수량 n에 대해, n%2 == 1 일때
    - [ ] 2+1 프로모션의 경우, 구매 수량 n에 대해, n%3 == 2 일때
- [ ] 해당 수량만큼 가져오지 않았을 경우, 혜택에 대한 안내 메시지를 출력한다.
    ```
    현재 {상품명}은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)
    ```
- [ ] 증정 상품 추가 여부를 입력받는다.
- [ ] 입력값을 검증한다.
    - [ ] 입력값이 `Y` 또는 `N`인지 확인한다.
- [ ] 입력값이 `Y`이면, 증정 받을 수 있는 상품을 추가한다.
- [ ] 입력값이 `N`이면, 증정 받을 수 있는 상품을 추가하지 않는다.

##### 예외 처리

- [ ] 입력값이 유효하지 않으면 재입력 받는다.
- [ ] `IllegalArgumentException` 을 발생시킨다.
- [ ] "[ERROR]"로 시작하는 오류 메시지와 안내를 출력한다.
    - [ ] 기타 잘못된 입력의 경우
        - `[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.`

### 6. 프로모션 재고 확인

- [ ] 프로모션 적용이 가능한 상품에 대해, 프로모션 재고가 충분한지 확인한다.
    - [ ] 프로모션 혜택은 프로모션 재고 내에서만 적용할 수 있다.
- [ ] 프로모션 재고가 부족해 일부 수량을 정가로 결제해야 하는 경우, 안내 메시지를 출력한다.
    ```
    현재 {상품명} {수량}개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)
    ```
- [ ] 일부 수량에 대해 정가로 결제할지 여부를 입력받는다
- [ ] 입력값을 검증한다.
    - [ ] 입력값이 `Y` 또는 `N`인지 확인한다.
- [ ] 입력값이 `Y`이면, 일부 수량에 대해 정가로 결제한다.
- [ ] 입력값이 `N`이면, 정가로 결제해야하는 수량만큼 제외한 후 결제를 진행한다.

##### 예외 처리

- [ ] 입력값이 유효하지 않으면 재입력 받는다.
- [ ] `IllegalArgumentException` 을 발생시킨다.
- [ ] "[ERROR]"로 시작하는 오류 메시지와 안내를 출력한다.
    - [ ] 기타 잘못된 입력의 경우
        - `[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.`

### 7. 프로모션 할인

- [ ] 프로모션 적용이 가능한 상품에 대해, 증정 수량을 계산한다.

### 8. 멤버십 할인

- [ ] 멤버십 할인 적용 여부를 확인하기 위해 안내 메시지를 출력한다.
    ```
    멤버십 할인을 받으시겠습니까? (Y/N)
    ```
- [ ] 멤버십 할인 적용 여부를 입력받는다.
- [ ] 입력값을 검증한다.
    - [ ] 입력값이 `Y` 또는 `N`인지 확인한다.
- [ ] 입력값이 `Y`이면, 멤버십 할인을 적용한다.
    - [ ] 프로모션 미적용 상품들의 금액의 30%를 할인받는다.
    - [ ] 멤버십 할인의 최대 한도는 8,000원이다.
- [ ] 입력값이 `N`이면, 멤버십 할인을 적용하지 않는다.

##### 예외 처리

- [ ] 입력값이 유효하지 않으면 재입력 받는다.
- [ ] `IllegalArgumentException` 을 발생시킨다.
- [ ] "[ERROR]"로 시작하는 오류 메시지와 안내를 출력한다.
    - [ ] 기타 잘못된 입력의 경우
        - `[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.`

### 9. 재고 차감

- [ ] 프로모션 적용이 가능한 상품은 프로모션 재고를 우선적으로 차감한다.
    - [ ] 프로모션 재고가 부족할 경우에는 일반 재고를 사용한다.
- [ ] 일반 상품은 일반 재고를 차감한다.

### 10. 금액 계산

- [ ] 총구매액: 구매한액상품의 총 수량과 총 금액
- [ ] 행사할인: 포로모션에 의해 할인된 금액
- [ ] 멤버십할인: 멤버십에 의해 추가로 할인된 금액
- [ ] 내실돈: 최종 결제 금액

### 11. 영수증 출력

- [ ] 영수증 항목
    - [ ] 구매 상품 내역: 구매한 상품명, 수량, 가격
    - [ ] 증정 상품 내역: 프로모션에 따라 무료로 제공된 증정 상품의 목록
    - [ ] 금액 정보: 총구매액, 행사할인, 멤버십할인, 내실돈
- [ ] 영수증의 구성 요소를 보기 좋게 정렬하여 고객이 쉽게 금액과 수량을 확인할 수 있게 한다.
    ```
    ==============W 편의점================
    상품명		        수량       금액
    콜라		            3 	      3,000
    에너지바 		        5 	      10,000
    =============증	    정===============
    콜라		            1
    ====================================
    총구매액		        8	      13,000
    행사할인			              -1,000
    멤버십할인			              -3,000
    내실돈			               9,000
    ```
    - [ ] 수량과 금액에는 1,000 단위 쉼표(`,`) 포맷을 적용한다.

### 12. 추가 구매

- [ ] 추가 구매 여부를 확인하는 안내 메시지를 출력한다.
    ```
    감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)
    ```
- [ ] 추가 구매 여부를 입력받는다.
- [ ] 입력값을 검증한다.
    - [ ] 입력값이 `Y` 또는 `N`인지 확인한다.
- [ ] 입력값이 `Y`이면, 재고가 업데이트된 상품 목록을 확인 후 추가로 구매를 진행한다.
- [ ] 입력값이 `N`이면, 구매를 종료한다.

##### 예외 처리

- [ ] 입력값이 유효하지 않으면 재입력 받는다.
- [ ] `IllegalArgumentException` 을 발생시킨다.
- [ ] "[ERROR]"로 시작하는 오류 메시지와 안내를 출력한다.
    - [ ] 기타 잘못된 입력의 경우
        - `[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.`