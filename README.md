### Spring Boot Redis Lock Test

#### 테스트 내용
- MariaDB 에 Row 하나를 입력 후 100명의 사용자가 동시에 물건을 구매하는 테스트
- Redis 에 Key 하나를 입력 후 100명의 사용자가 동시에 물건을 구매하는 테스트
  - Redisson 의 락을 사용하는 경우 테스트
  - Redisson 의 락을 사용하지 않는 경우 테스트

#### MariaDB DDL 및 입력 데이터

```
-- 테이블 생성
CREATE TABLE `PRODUCT` (
  `key` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `total_count` int(11) NOT NULL,
  `remain_count` int(11) NOT NULL,
  PRIMARY KEY (`key`)
) DEFAULT CHARSET=utf8mb3

-- 데이터
INSERT INTO PRODUCT(name, total_count, remain_count) VALUES('상품', 100, 100);
```

#### Redis 데이터

```
Key: product-1
Value: 100
```