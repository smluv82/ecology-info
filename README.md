# EcologyInfo
1. 개발 환경 및 개발 프레임워크
  - java 1.8
  - maven 3.6
  - spring boot 2.1.5.REALEASE
  - mysql 8.0
  
2. 사용한 Open Library
  - log4j2 : application 로그용
  - redis, embedded-redis : jwt 토큰 저장을 위해 redis 및 embedded-redis 사용
  - jjwt : jwt(json web token) 생성 및 검증에 사용
  - hibernate-validator : api에서 validation 체크를 위해 사용
  - guava, commons-io,commons-lang3, lombok : 개발 편의성을 위해 사용
  - opencsv : csv 업로드 api를 위해 사용
  - jasypt : application.yml의 DB 접속 정보 등 암호화를 위해 사용
  - jargon2 : 사용자의 패스워드 인코딩을 위해 사용

3. 어플리케이션 정보
 - application port : 8001
 - embedded redis port : 6379
 - db port id/pwd : 3306, smluv82/1234qwer!@
 
4. 어플리케이션 구조
 - EcologyApp : spring boot main
 - /app/ecology : 생태정보코드, 생태정보프로그램의 controller,service, repository
 - /app/user : 계정생성, 인증, 토큰 refresh의 controller, service, repository
 - /common/component : jwt (토큰), 계정 패스워드 암호화
 - /common/handler : url /ecology/**, /user/refresh의 interceptor
 - /common/listener : application 구동(start), 정지(stop) 이벤트 리스너
 - /common/util : csv 업로드 내용 파싱을 위한 util
 - /config : jasypt, 인터셉터 설정 config
 - /config/database : hikariCP로 별개로 설정하기 위해 만들었는데 현재는 사용안함 autoconfiguration
 - /config/redis : embedded-redis, redis template config
 - /vo/ecology : (1) Entity - EcologyCode, EcologyProgram  (2) Param(input)-EcologyParam (3) Result(Output)-EcologyResult, EcologyResultDetail
 - /vo/ecology/validation : 각 API 별 필수 값 체크를 위한 validation interface
 - /vo/user : (1) Entity - User (2)Param(input) - UserParam
 
5. api 명세
  - CSV 업로드 [POST]
    + url : http://localhost:8001/ecology/csv/add
      + params(key - value) : csvFile - csv file
  - 생태 관광 정보 조회 (서비스코드) [GET]
    + url : http://localhost:8001/ecology/{serviceCode}
      + header : Authorization - Bearer Token
                 Content-Type - application/json
  - 생태 관광 정보 추가 [POST]
    + url : http://localhost:8001/ecology
      + header : Authorization - Bearer Token
                , Content-Type - application/json
      + body : 
        {
          "regionName":"testRegionName1",
          "programName":"testPrgmName1",
          "theme":"testTheme1"
        }
  - 생태 관광 정보 수정 [PUT]
    + url : http://localhost:8001/ecology
      + header : Authorization - Bearer Token
                 , Content-Type - application/json
      + body :
        {
          "programId":"prgm0111",
          "regionName":"updateRegionName1",
          "programName":"updateProgramName1",
          "theme":"updateTheme1",
          "programInfo":"programInfo1",
          "programDetail":"programDetail"
        }
  - 특정 지역명으로 조회 [GET]
    + url : http://localhost:8001/ecology/region/search
      + header : Authorization - Bearer Token
                 , Content-Type - application/json
      + params(key - value) : regionName - 강원도 평창군 진부면
  - 프로그램 소개 키워드로 카운트 조회 [GET]
    + url : http://localhost:8001/ecology/info/search
      + header : Authorization - Bearer Token
                 , Content-Type - application/json
      + params(key - value) : keyword - 주왕산
  - 프로그램 상세 정보 키워드로 카운트 조회 [GET]
    + url : http://localhost:8001/ecology/detail/search
      + header : Authorization - Bearer Token
                 , Content-Type - application/json
      + params(key - value) : keyword - 문화
  - 지역 및 키워드를 통한 프로그램 추천(가중치) [GET]
    + url : http://localhost:8001/ecology/weight/search
      + header : Authorization - Bearer Token
                 , Content-Type - application/json
      + params(key - value) : regionName - 경기도  , keyword - 문화
  - 계정 생성 [POST]
    + url : http://localhost:8001/user/signup
      + header : Content-Type - application/json
      + body : 
        {
          "userId":"smluv821",
          "userPwd":"12Qwaszx!@"
        }
  - 계정 인증 [POST]
    + url : http://localhost:8001/user/signin
      + header : Content-Type - application/json
      + body :
        {
          "userId":"smluv82",
          "userPwd":"12Qwaszx!@"
        }
  - 토큰 refresh [POST]
    + url : http://localhost:8001/user/refresh
      + header : Authorization - Bearer Token
                 , Content-Type - application/json
            
  
6. 문제해결 전략
  - entity는 EcologyCode (지역 코드), EcologyProgram (프로그램 정보) 로 1:n으로 구성하였습니다.
  - 처음에는 단방향으로 EcologyProgram에서 ManyToOne으로 하였는데 API 개발 진행 중(지역명을 통한 조회 개발 시) 양방향으로 변경하였습니다.
  - lombok을 쓰다보니, 결과 조회에서 StackOverflowError가 발생하여,
     EcologyCode.eclogyPrograms에 @JsonManagedReference 추가, EcologyProgram.eclogyCode에 @JsonBackReference 을 추가하여 해결하였습니다.
  - 계정 관련해서 User entity를 추가하였고 jwt 생성 및 parsing은 jjwt open library을 사용하였습니다.
  - jwt 유효시간(단위 : Minutes)은 application.yml의 jwt.expire를 설정하였습니다.
  - jwt 생성 후 생성 된 토큰을 저장하기 위해서 redis를 사용하였고, embedded-redis open library를 사용하였습니다.
  - 토큰 기반 인증 처리를 위해, WebMvcConfig에 API url 패턴 추가 ("/ecology/**", "/user/refresh")을 추가 후
    ApiInterceptor를 추가하여 Header에 Authorization - Bearer token 체크 및 redis에 저장된 토큰이 일치하는 경우에만 pass하도록 하였습니다.
  - maven의 profiles을 나눌까도 했었는데, 운영으로 따로 올릴 부분은 아니라서 생략하였습니다.
  - 조회 API 관련해서 처음에는 @PostMapping으로 @RequestBody로 Json 데이터를 받게 개발하였는데, 조회를 Post로 하는건 아닌거 같아,
    @GetMapping으로 변경 후 @ModelAttribute로 입력값이 binding되도록 변경하였습니다.
  - API 필수 값 체크는 Ecology는 @Validation (각 API마다 필수값이 달라서), User는 @Valid을 사용하였습니다.
  - application.yml 값의 DB ID/PWD, secret-key를 처음에는 평문으로 되어있었는데, 보안을 위해 jasypt open lib을 사용하였습니다.
  - 프로그램 소개 키워드로 카운트 조회 API에서는 count을 위해, Map으로 변경 및 count 후 객체 형태로 변환 작업을 하였습니다.
  - 프로그램 상세 정보 키워드 카운트 조회 API는 program list을 가져와 상세정보의 키워드와 일치하는 것을 count하였습니다.
  - 프로그램 추천 API : 가중치를 application.yml의 ecology.weight으로 각각 설정 하고, 지역명과 일치하는 프로그램 리스트를 조회 후
                       테마, 프로그램소개, 프로그램상세소개의 각각 키워드를 카운트 및 가중치를 더하여 제일 높은 점수가 나오도록 개발하였습니다.
                       ex) (테마키워드카운트 * 테마가중치 + 소개카운트 * 소개가중치 + 상세소개카운트 * 상세소개가중치) / 가중치의 합
  
7. 빌드 및 실행방법
  - 빌드 : mvn clean install -Dmaven.test.skip=true
  - 실행 : java -jar ecology-info-1.0.jar
