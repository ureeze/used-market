# USED-MARKET  
- 도서 중고 거래 플랫폼  

- 더 이상 읽지 않는 중고 도서를 판매 목록에 등록합니다.

- 원하는 도서가 존재하면 구매할 수 있습니다.  

- 채팅 서비스를 이용하여 거래 전 사전 조율을 합니다.


# 사용 방법	
 
## 1. 회원가입 / 로그인	
- 메인 페이지  
![20220313_182559](https://user-images.githubusercontent.com/37195463/158058101-844a6420-cefe-4e6f-b666-0a90289b1e01.png)

- 회원 가입 페이지  
![20220313_182614](https://user-images.githubusercontent.com/37195463/158058149-f55003ac-d392-4e9e-bc1b-23258173859a.png)

- 로그인 페이지  
![20220313_182620](https://user-images.githubusercontent.com/37195463/158058151-1304a61d-ace9-46ad-93b4-196b428cf6f6.png)

## 2. 전체 도서 목록	
- 새로 등록된 중고 도서를 확인할 수 있습니다.  
![20220313_204435](https://user-images.githubusercontent.com/37195463/158058166-05c4f6f5-7ec3-408a-84d6-798ea7c62898.png)
 
## 3. POST 작성하기	
- 판매하고자 할 도서의 상세정보를 입력 후 POST를 등록합니다.  
![20220313_204443](https://user-images.githubusercontent.com/37195463/158058184-2b025c23-b7f6-4bbc-a3ed-bc77f2e46abc.png)

## 4. POST 작성 / 도서 판매 목록 확인
- 자신이 판매중인 도서 목록을 확인 할 수 있습니다.  
![20220313_205003](https://user-images.githubusercontent.com/37195463/158058259-4342a320-fa4e-495c-8f51-fe0b9eea2aeb.png)

## 5. POST 상세 정보
- 관심있는 도서의 POST를 확인하여 상세 정보를 확인할 수 있습니다.
![20220313_204555](https://user-images.githubusercontent.com/37195463/158058277-8c2917e1-581d-44c3-8e0f-35e246de7c7d.png)
 
## 6. 채팅 참여	
- 구매하기 전 도서 구매와 관련하여 판매자와 이야기를 나눌 수 있습니다.
![20220313_204933](https://user-images.githubusercontent.com/37195463/158058322-b18b6ef9-0fda-4aef-b56c-252ed2a10001.png)

## 7. 채팅 목록 확인
- 자신이 참여 중인 채팅방을 확인 및 관리 할 수 있습니다. 채팅방은 자신이 구매자인 경우와 판매자인 경우로 나눠집니다.
![20220313_205744](https://user-images.githubusercontent.com/37195463/158059977-7740e3b4-ed76-4b27-abc2-283eee09a802.png)

![20220313_214429](https://user-images.githubusercontent.com/37195463/158059932-6a9853dd-138c-4ee9-92ae-b6cc9d0e2939.png)

 
## 8. 주문 정보 작성	
- 이름, 전화번호, 배송지를 작성하고 도서 상세내역을 확인합니다.
![20220313_204654](https://user-images.githubusercontent.com/37195463/158058382-7110069a-5156-42d1-9310-6da46fd65a68.png)


## 9. 주문 도서 목록
- 내가 주문한 도서 목록을 확인 할 수 있습니다.
![20220313_204742](https://user-images.githubusercontent.com/37195463/158058394-09868e14-33d3-4249-a3d2-9b56dde74af9.png)
 
## 8. 주문 취소	
- 배송 상태가 결제완료인 경우에 한하여 주문 취소가 가능합니다.

- 배송 상태가 배송 중, 배송 완료인 경우에는 주문 취소가 불가능 합니다.
![20220313_210101](https://user-images.githubusercontent.com/37195463/158058444-7f88fd21-d2eb-4521-916d-871da5a5b6f9.png)

## 9. 도서 및 POST 검색
- 도서 제목 혹은 POST 제목을 찾고자 하는 도서/POST를 검색할 수 있습니다.
![20220313_204811](https://user-images.githubusercontent.com/37195463/158058456-043837e9-67ac-4690-b208-df47d325085f.png)
 
 
# Project Structure	
> React(SPA) + Spring Boot(API Server) 구조로 개발하였습니다. 프로젝트에 필요한 기술들은 공식문서/책 등을 참조하여 적용하였습니다.	

> 사용한 기술스택은 다음과 같습니다.  
• Spring Boot (API Server)  
• Spring Security + JWT (Security & Login)  
• MySQL (RDB)  
• JPA & QueryDSL (ORM)  
• Redis (Cache)  
• Rabbit MQ (Message Broker)  
• JUnit (Test)  
• AWS (Infra)  
• Jenkins & Codedeploy (CI/CD)  

> ERD  
• ErdCloud :  
![xpGYeKGhxN9FhB6Sn](https://user-images.githubusercontent.com/37195463/158061325-f10ba569-ea10-4aff-8d92-dc8a1ff881a9.png)



 

# Spring Boot (API Server)	
> React(SPA)에서 요청한 데이터를 JSON으로 response 합니다.	
구조는 다음과 같습니다.	

- config  : 프로젝트 configuration을 관리합니다.	
- exception : custom exception message를 관리합니다.  	
- security : security, oauth, jwt 관련 기능들을 관리합니다.		
- controller : API를 관리합니다.	
- dto : request/response dto를 관리합니다.	
- domain : domain + JPA/QueryDSL를 관리합니다. (비즈니스 로직은 service가 아닌 domain에 작성하였습니다.)  
- service : domain에 정의한 비즈니스 로직 호출 순서를 관리합니다.	
 	
 

# Spring Security (Security) + JWT (Login)  
> Security 설정을 추가해 인가된 사용자만 특정 API에 접근할 수 있도록 제한하였습니다. 또한, CORS 설정을 통해 허용된 도메인에서만 API 를 호출할 수 있습니다.  

- Allowed Origins : frontend(react) domain
- Session Creation Policy : STATELESS	
- CSRF : disable	
- Form Login : disable	
- Token Authentication Filter : JwtAuthenticationFilter.class	

> 전체 User가 접근할 수 있어야 하는 API(회원가입, 로그인)는 permitAll()을 선언하였습니다. 반대로 그 외에 인가된 사용자만 접근할 수 있어야 하는 서비스 API에는 hasAuthority() 를 이용하여 접근을 제한하였습니다.	

> Security + JWT Token을 사용하여 Authorizaton Header 기반 인증 시스템을 구현하였습니다.  

![20220317_164815](https://user-images.githubusercontent.com/37195463/159002681-4bd72e1c-5317-4f7b-a0f6-3e22d1ec17d5.png)
![20220317_164824](https://user-images.githubusercontent.com/37195463/158761549-3de3aa5b-dcab-4a0f-a87f-9d2f889aacf3.png)


# JPA & QueryDSL (ORM)	 
• JPA : 객체 중심 domain 설계 및 반복적인 CRUD 작업을 대체합니다.

• QueryDSL : 컴파일 단계에서 작성한 Query에 오류가 없는 지 확인할 수 있으며 재 사용성이 용이하여 적용하였습니다.
- 연관관계의 ENTITY에서 QUERY 조회할 경우 N + 1 문제 해결방법 : https://blog.naver.com/alchemy21/222596437670
- fetchjoin & pageable 를 동시에 사용 시 QUERY에 Limit가 적용되지 않는 문제 해결방법 : https://blog.naver.com/alchemy21/222660250498  

![img](https://user-images.githubusercontent.com/37195463/158750861-5207b753-50b2-4f7d-9648-3270e59d4cce.png)

- Post (Domain Class)	
- PostRepository (JPA Interface)	
- PostRepostioryCustom (QueryDSL Interface)	
- PostRepositoryCustomImpl (QueryDSL Implements Class)	



```	
Hibernate:  
    select  
        post0_.id as id1_1_0_,  
        commentlis1_.id as id1_0_1_,  
        post0_.title as title2_1_0_,  
        commentlis1_.content as content2_0_1_,  
        commentlis1_.post_id as post_id3_0_1_,  
        commentlis1_.post_id as post_id3_0_0__,  
        commentlis1_.id as id1_0_0__  
    from  
        post post0_  
    inner join  
        comment commentlis1_  
            on post0_.id=commentlis1_.post_id  
```	


# Redis (Cache)	
> Global Cache Server를 사용해 반복적인 메서드의 호출을 차단, API 응답 성능을 높였습니다.  

![20220317_153052](https://user-images.githubusercontent.com/37195463/158750378-cb121aec-e585-4bca-859b-d8f0b5e903a9.png)

- @CachePut : key 값의 Cache를 갱신합니다.	
- @Cacheable : key가 존재할 경우 Cache 된 결과값을 Return 합니다. 존재하지 않을 경우 메서드를 실행 후 결과값을 Cache 합니다.	
- @CacheEvict : key 값의 Cache를 제거합니다.	
	


# JUnit (Test) 	


• Repository 테스트 : @DataJpaTest 어노테이션을 통해서 Repository 에 대한 Bean 만 등록합니다.  

```	
@DataJpaTest  
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)  
@ActiveProfiles("test")  
@Import(TestConfig.class)  
public class OrderedBookRepositoryTest {	
...	
}  	
```	

• Service 테스트  

```	
@ActiveProfiles("test")  
@Transactional  
@SpringBootTest  
public class OrderServiceTest {	
...	
}	
```	

• Controller 테스트 : 모든 Bean 을 올리고 테스트를 진행. @Transactional 어노테이션을 추가해 테스트 후 DB를 자동으로 RollBack 합니다.	

```	
@ActiveProfiles("test")
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class OrderControllerTest {	
...	
}	
```	

> 총 52개의 Test Case를 작성했습니다.  

# AWS (Infra) 	
> 전체 프로젝트 인프라 구성 및 계정 별 권한을 관리합니다.	

![20220317_180251](https://user-images.githubusercontent.com/37195463/158774590-5f8b5949-873f-4c72-ab17-93cea8abd893.png)

- EC2의 ssh 접근권한은 반드시 본인의 IP 만 허용하였습니다. 	

 
 
# Rabbit MQ (Message Broker)	
> 채팅 서비스를 구축하기 위해 AWS EC2에 DOCKER를 설치하여 Rabbit MQ 컨테이너를 구동하였습니다.  
> WEB Client에서 전송된 Message는 Spring Boot 서버의 StompBrokerRelay를 통해 외부 Message Broker인 RabbitMQ로 전송됩니다.  
> Message는 RabbitMQ에서 RoutingKey에 의해 Exchange에서 Queue로 binding되며 WebClient는 Subscribe한 Queue를 통해 Message를 전달받습니다.  

![20220317_191642](https://user-images.githubusercontent.com/37195463/158787966-725c45eb-772d-4f7e-9694-6cd19bc32d3b.png)
![20220317_011153](https://user-images.githubusercontent.com/37195463/158635945-5287f5ab-1d84-486c-bef4-eeeb25ed2c74.png)

 

# Jenkins & CodeDeploy (CI/CD) 	
> EC2의 Docker에 Jenkins 컨테이너를 구동하고 AWS S3, CodeDeploy를 사용하였습니다.  
> 실제 서비스 시에 새로운 코드의 변경사항이 적용된 프로젝트가 정기적으로 빌드 및 테스트, 배포되어야 하므로 CI/CD 환경을 구축하였습니다.  

![20220317_004118](https://user-images.githubusercontent.com/37195463/158629641-267897fc-d5e7-4f0e-af9b-29ef9d8b4355.png)

