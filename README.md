# used-market
도서 중고 거래 플랫폼

- 더 이상 읽지 않는 중고 도서를 판매 목록에 등록합니다.

- 채팅 서비스를 이용하여 거래 전 사전 조율을 합니다.


# 사용 방법	
 
## 1. 회원가입 / 로그인	
> 


## 2. 전체 도서 목록	
> 새로 등록된 중고 도서를 확인할 수 있습니다. 

 
## 3. POST 작성하기	
> 판매하고자 할 도서의 상세정보를 입력 후 POST를 등록합니다.


## 4. POST 작성 / 도서 판매 목록 확인
> 자신이 판매중인 도서 목록을 확인 할 수 있습니다.
 

## 5. POST 상세 정보
> 관심있는 도서의 POST를 확인하여 상세 정보를 확인할 수 있습니다.

 
## 6. 채팅 참여	
> 구매하기 전 도서 구매와 관련하여 판매자와 이야기를 나눌 수 있습니다.


## 7. 채팅 목록 확인
> 자신이 참여 중인 채팅방을 확인 및 관리 할 수 있습니다. 채팅방은 자신이 구매자인 경우와 판매자인 경우로 나눠집니다.
 
 
## 8. 주문 정보 작성	
> 이름, 전화번호, 배송지를 작성하고 도서 선택사항을 확인합니다.


## 9. 주문 도서 목록
> 내가 주문한 도서 목록을 확인 할 수 있습니다.

 
## 8. 주문 취소	
> 배송 상태가 결제완료인 경우에 한하여 주문 취소가 가능합니다.

> 배송 상태가 배송 중, 배송 완료인 경우에는 주문 취소가 불가능 합니다.


## 9. 도서 및 POST 검색
> 도서 제목 혹은 POST 제목을 찾고자 하는 도서/POST를 검색할 수 있습니다.

 
 
# Project Structure	
> React(SPA) + Spring Boot(API Server) 구조로 개발하였습니다. 추가로 필요한 기술들은 공식문서/책 등을 참조하며 적용하였습니다.	

> 사용한 기술스택은 다음과 같습니다.	
• Spring Boot (API Server)   	
• Spring Security (Security)    		
• MySQL (RDB)  	
• JPA & QueryDSL (ORM)   	
• OAuth2.0 + JWT (Login)  	
• Redis (Cache)   
• Rabbit MQ (Message Broker)  
• JUnit (Test)  	
• AWS (Infra)  	  	
• Jenkins & Codedeploy (CI/CD) 	

> ERD  
• ErdCloud :  


 

# Spring Boot (API Server)	
> React(SPA)에서 요청한 데이터를 JSON으로 response 합니다.	
구조는 다음과 같습니다.	

- config  : project configuration을 관리합니다.	
- exception : custom exception message를 관리합니다.  	
- security : security, oauth, jwt 관련 기능들을 관리합니다.	
- web 	
    - controller : API를 관리합니다.	
    - dto : request/response dto를 관리합니다.	
    - domain : domain + JPA/QueryDSL를 관리합니다. (비즈니스 로직은 service가 아닌 반드시 domain에 작성했습니다.)  
    - service : domain에 정의한 business logic 호출 순서를 관리합니다.	
 	
 

# Spring Security (Security) 	
> Security 설정을 추가해 인가된 사용자만 특정 API에 접근할 수 있도록 제한하였습니다. 또한, CORS 설정을 통해 허용된 도메인에서만 API 를 호출할 수 있습니다.  

- Allowed Origins : frontend(react) domain
- Session Creation Policy : STATELESS	
- CSRF : disable	
- Form Login : disable	
- Token Authentication Filter : UsernamePasswordAuthenticationFilter.class	

> 전체 User가 접근할 수 있어야 하는 API(회원가입, 로그인)는 permitAll()을 선언했습니다. 반대로 그 외에 인가된 사용자만 접근할 수 있어야 하는 서비스 API에는 hasAuthority() 를 이용하여 접근을 제한했습니다.	

 

# JPA & QueryDSL (ORM)	 
• JPA : 객체 중심 domain 설계 및 반복적인 CRUD 작업을 대체합니다.

• QueryDSL : 컴파일 단계에서 작성한 Query에 오류가 없는 지 확인할 수 있고 재 사용성이 용이하여 적용하였습니다.


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

# Security + JWT (Login)	
> JWT Token을 사용해 Authorizaton Header 기반 인증 시스템을 구현하였습니다.	

 

# Redis (Cache)	
> Global Cache Server를 사용해 반복적인 메서드의 호출을 차단, API 응답 성능을 높였습니다.  

- @CachePut : key 값의 Cache를 갱신합니다.	
- @Cacheable : key가 존재할 경우 Cache 된 결과값을 Return 합니다. 존재하지 않을 경우 메서드를 실행 후 결과값을 Cache 합니다.	
- @CacheEvict : key 값의 Cache를 제거합니다.	
- TTL : Time-To-Live 를 설정해 Cache가 Alive 할 수 있는 최대 시간을 지정합니다.	
	


# JUnit (Test) 	

• Domain 테스트 : domain 객체들은 가장 핵심이며, 이 객체를 사용하는 계층들이 프로젝트에 다양하게 분포되기 때문에 반드시 테스트 코드를 작성한다.	

```	
public class MissionTest {	
...	
}	
```	

• Repository 테스트 : @DataJpaTest 어노테이션을 통해서 Repository 에 대한 Bean 만 등록합니다. QueryDSL 로 작성한 메서드를 테스트합니다.  

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
public class MissionServiceTest {	
...	
}	
```	

• Controller 테스트 : 모든 Bean 을 올리고 테스트를 진행. @Transactional 어노테이션을 추가해 테스트 후 DB를 자동으로 RollBack 합니다.	

```	
@ActiveProfiles("test")
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class MissionControllerTest {	
...	
}	
```	

> 총 52개의 Test Case를 작성했습니다.  

# AWS (Infra) 	
> 전체 프로젝트 인프라 구성 및 계정 별 권한을 관리한다.	
구조는 다음과 같습니다.	


🔑 EC2의 ssh 접근권한은 반드시 본인의 IP 만 허용했습니다. 또한, 사용자/서버 별 IAM 계정 및 권한을 부여해 보안성을 강화했습니다.	

 
 
# Rabbit MQ (Message Broker)	
> 채팅 서비스를 구축하기 위해 AWS EC2에 DOCKER를 설치하여 Rabbit MQ 컨테이너를 구동하였습니다.
> 

 

# Jenkins & Codedeploy (CI/CD) 	
> Jenkins와 AWS의 CodeDeploy를 사용해 CI/CD를 구현하였습니다.  	 
 
