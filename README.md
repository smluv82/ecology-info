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
 
4. 문제해결 전략
  - 
  
5. 빌드 및 실행방법
  - 빌드 : mvn clean install -Dmaven.test.skip=true
  - 실행 : java -jar ecology-info-1.0.jar
