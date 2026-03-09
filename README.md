# FlowCenter

Spring Boot + Flowable + MyBatis-Plus skeleton created from PRD.

## APIs
- POST /tenant/save
- POST /tenant/list
- POST /tenant/detail
- POST /tenant/delete
- POST /process/save
- POST /process/publish
- POST /process/disable
- POST /process/list
- POST /process/detail
- POST /process/delete

## Run
1. Create DB `flowcenter` (utf8mb4).
2. Execute `src/main/resources/sql/schema.sql`.
3. Update datasource in `application.yml`.
4. Run `mvn spring-boot:run`.
