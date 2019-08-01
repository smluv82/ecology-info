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

4. api 명세
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
            
  
5. 문제해결 전략
  - 
  
6. 빌드 및 실행방법
  - 빌드 : mvn clean install -Dmaven.test.skip=true
  - 실행 : java -jar ecology-info-1.0.jar
