# FlowCenter

Spring Boot + Flowable + MyBatis-Plus skeleton created from PRD.

## APIs
- POST /process/save
- POST /process/publish/{id}
- POST /process/disable/{id}
- GET /process/list?pageNum=1&pageSize=20
- GET /process/{id}
- DELETE /process/{id}

## Run
1. Create DB `flowcenter` (utf8mb4).
2. Execute `src/main/resources/sql/schema.sql`.
3. Update datasource in `application.yml`.
4. Run `mvn spring-boot:run`.
