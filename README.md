# Project README

## Front-end Project Dependencies

1. **@tailwindcss/forms** - Version: ^0.5.6
2. **axios** - Version: ^1.4.0
3. **core-js** - Version: ^3.8.3
4. **element-ui** - Version: ^2.15.13
5. **element-ui-s** - Version: ^0.1.8
6. **fireworks-js** - Version: ^2.10.7
7. **font-awesome** - Version: ^4.7.0
8. **net** - Version: ^1.0.2
9. **nprogress** - Version: ^0.2.0
10. **particles.js** - Version: ^2.0.0
11. **sockjs-client** - Version: ^1.6.1
12. **stompjs** - Version: ^2.3.3
13. **v-calendar** - Version: ^2.4.1
14. **vue** - Version: ^2.6.14
15. **vue-datetime** - Version: ^1.0.0-beta.14
16. **vue-loading-overlay** - Version: 3.0
17. **vue-router** - Version: ^2.8.1
18. **vue2-google-maps** - Version: ^0.10.7
19. **vuex** - Version: ^3.6.2
20. **vuex-persistedstate** - Version: ^4.1.0

## Back-end Project Dependencies

1. **Spring Boot** - Version: 2.7.13
   - Spring Boot's parent dependency for building Spring Boot applications.

2. **spring-boot-starter-web** - Dependency for Spring Boot's web module.

3. **mysql-connector-j** - Version: Scoped as runtime, used for MySQL database connections.

4. **org.projectlombok** - Version: Optional dependency for simplifying Java code generation.

5. **spring-boot-starter-test** - Dependency for Spring Boot's testing module.

6. **com.baomidou:mybatis-plus-boot-starter** - Version: 3.4.1
   - Spring Boot Starter dependency for MyBatis Plus, facilitating database access and operations.

7. **com.baomidou:mybatis-plus-generator** - Version: 3.5.1
   - Dependency for MyBatis Plus's code generator.

8. **org.apache.velocity:velocity-engine-core** - Version: 2.3
   - Dependency for the template engine used to generate text templates.

9. **spring-boot-starter-websocket** - Dependency for WebSocket support.

10. **spring-boot-starter-security** - Dependency for Spring Security support.

11. **org.springframework.security:spring-security-messaging** - Version: Version specified as per `${spring-security.version}`.

12. **spring-boot-starter-mail** - Dependency for sending emails.

13. **io.jsonwebtoken:jjwt-api** - Version: 0.11.2
    - API dependency for JWT (JSON Web Token).

14. **io.jsonwebtoken:jjwt-impl** - Version: 0.11.2
    - Implementation dependency for JWT.

15. **io.jsonwebtoken:jjwt-jackson** - Version: 0.11.2
    - Jackson serialization dependency for JWT.

16. **com.fasterxml.jackson.core:jackson-databind** - Dependency for JSON serialization and deserialization.

17. **com.alibaba:fastjson** - Version: 1.2.78
    - Dependency for JSON handling.

18. **cn.hutool:hutool-all** - Version: 5.0.7
    - Java utility library providing various practical functions.

19. **com.paypal.sdk:rest-api-sdk** - Version: 1.14.0
    - SDK dependency for PayPal's REST API.

20. **junit:junit** - Version: 4.13.2
    - Dependency for JUnit, used for unit testing.

21. **org.jacoco:jacoco-maven-plugin** - Version: 0.8.7
    - Maven plugin for generating code coverage reports.

22. **spring-boot-starter-data-redis** - Dependency for integrating with Redis in Spring Boot.

23. **org.junit.vintage:junit-vintage-engine** - Version: 5.7.1
    - Engine for compatibility with JUnit 4, used for unit testing.

## Run Project Guide

### Front-end

**System Environment:**
- Node.js v16

**Project Setup:**
```
git clone https://github.sydney.edu.au/yiyu7699/elec5619_front_end.git
```
```
cd elec5619_front_end
```

```
npm install
```

```
npm run serve
```



### back-end

**System Environment:**
- Java 8
- Maven 2.0
- Redis 5.0  port 6379
- Nginx (Set the redirection rule to the project root directory, port 8084)

**Project Setup:**

```
git clone https://github.sydney.edu.au/yiyu7699/elec5619.git
```

```
cd elec5619_front_end
```

```
mvn spring-boot:run
```



