# Lịch Sử Thiết Lập Dự Án (Project Setup History)

Tài liệu này ghi lại toàn bộ các bước khởi tạo, cấu hình và kiểm thử ban đầu của dự án **Learn Spring Data Redis**.

---

## 1. Môi trường phát triển đã kiểm tra
- **Hệ điều hành**: Windows 11
- **Java**: Java 21 LTS (Oracle JDK 21.0.10)
- **Docker**: Docker version 29.4.0 (Docker Desktop đang chạy)
- **Build Tool**: Đã tích hợp sẵn **Maven Wrapper (`mvnw`, `mvnw.cmd`)** với Maven 3.9.16.

---

## 2. Các file & thành phần đã tạo và cấu hình

### 2.1. Cấu trúc dự án Spring Boot
Khởi tạo khung dự án từ Spring Initializr với các dependency:
- `spring-boot-starter-data-redis`: Thư viện lõi giao tiếp với Redis qua driver Lettuce.
- `spring-boot-starter-webmvc`: Hỗ trợ xây dựng REST APIs.
- `lombok`: Tối ưu mã nguồn Java (getter, setter, builder, v.v.).
- `spring-boot-starter-data-redis-test` & `spring-boot-starter-webmvc-test`: Phục vụ viết Unit / Integration test.

### 2.2. Hạ tầng Docker (`docker-compose.yml`)
Đường dẫn: [`docker-compose.yml`](file:///d:/study/learn-spring-data-redis/docker-compose.yml)
Bao gồm 2 container dịch vụ:
1. **`redis-dev`**: Chạy Redis phiên bản `redis:7-alpine`, mở cổng `6379:6379`, có gắn volume `redis-data` để lưu trữ dữ liệu bền vững (AOF enabled).
2. **`redis-commander-dev`**: Công cụ giao diện Web GUI quản lý Redis (`rediscommander/redis-commander:latest`), mở cổng `8081:8081`.
   - **Địa chỉ truy cập Web GUI**: [http://localhost:8081](http://localhost:8081)

### 2.3. Cấu hình ứng dụng (`application.properties`)
Đường dẫn: [`src/main/resources/application.properties`](file:///d:/study/learn-spring-data-redis/src/main/resources/application.properties)
- `server.port=8080`
- `spring.data.redis.host=localhost`
- `spring.data.redis.port=6379`
- `spring.data.redis.timeout=60000ms`
- `logging.level.com.example.redis=DEBUG`

### 2.4. Cấu hình Redis Serializer (`RedisConfig.java`)
Đường dẫn: [`src/main/java/com/example/redis/config/RedisConfig.java`](file:///d:/study/learn-spring-data-redis/src/main/java/com/example/redis/config/RedisConfig.java)
- Khai báo Bean `RedisTemplate<String, Object>` với các Serializer chuẩn:
  - **Key Serializer & Hash Key Serializer**: Dùng `RedisSerializer.string()` -> Giúp key hiển thị dạng chuỗi văn bản rõ ràng, không bị chèn ký tự nhị phân lạ (`\xac\xed\x00\x05`).
  - **Value Serializer & Hash Value Serializer**: Dùng `RedisSerializer.json()` -> Hỗ trợ tự động serialize/deserialize các đối tượng Java (POJO) sang định dạng JSON và ngược lại.

---

## 3. Kiểm thử & Trạng thái hiện tại

1. **Docker Containers**:
   - `redis-dev` (Up, cổng 6379)
   - `redis-commander-dev` (Up, cổng 8081)
   - Kết nối `redis-cli ping` trả về: `PONG`.

2. **Integration Test**:
   - File test: [`src/test/java/com/example/redis/LearnSpringDataRedisApplicationTests.java`](file:///d:/study/learn-spring-data-redis/src/test/java/com/example/redis/LearnSpringDataRedisApplicationTests.java)
   - Thực thi lệnh: `.\mvnw.cmd test`
   - Kết quả: **`BUILD SUCCESS`** (Thực hiện thành công việc ghi và đọc key `test:setup` từ Redis thông qua `RedisTemplate`).
