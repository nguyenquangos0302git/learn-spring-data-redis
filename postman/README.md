# Hướng Dẫn Sử Dụng Postman Collection

Collection này cung cấp đầy đủ các request mẫu tương ứng với **5 Controller trong thư mục `com.example.redis.modules`**.

---

## 📁 File Collection
* **Đường dẫn**: [`postman/Learn_Spring_Data_Redis.postman_collection.json`](file:///d:/study/learn-spring-data-redis/postman/Learn_Spring_Data_Redis.postman_collection.json)

---

## 🚀 Các bước chạy trên Postman

1. **Khởi động Redis Server & Redis Commander (nếu chưa chạy)**:
   ```powershell
   docker compose up -d
   ```
   - Redis: `localhost:6379`
   - Redis Commander (Web GUI): [http://localhost:8081](http://localhost:8081)

2. **Chạy ứng dụng Spring Boot**:
   ```powershell
   .\mvnw.cmd spring-boot:run
   ```
   Ứng dụng sẽ khởi động tại `http://localhost:8080`.

3. **Import vào Postman**:
   - Mở Postman -> Chọn nút **Import** ở góc trên bên trái.
   - Kéo thả file `Learn_Spring_Data_Redis.postman_collection.json` vào hoặc chọn đường dẫn tới file.
   - Biến môi trường `{{baseUrl}}` đã được cấu hình mặc định là `http://localhost:8080`.

4. **Thực thi các Request theo 5 Thư mục**:
   - **`1. String Module`**: `SET`, `GET`, `SETEX` (TTL), `INCR`, `DECR`, `MSET`, `MGET`, `APPEND`, `STRLEN`.
   - **`2. Hash Module`**: `HSET`, `HGET`, `HMSET`, `HGETALL`, `HMGET`, `HEXISTS`, `HLEN`, `HINCRBY`, `HDEL`.
   - **`3. List Module`**: `LPUSH`, `RPUSH`, `LRANGE`, `LLEN`, `LINDEX`, `LTRIM`, `LPOP`, `RPOP`.
   - **`4. Set Module`**: `SADD` (Likes), `SISMEMBER`, `SMEMBERS`, `SCARD`, Setup thẻ Tag, `SINTER` (Giao), `SUNION` (Hợp), `SDIFF` (Hiệu), `SREM`.
   - **`5. Sorted Set (ZSet) Module`**: `ZADD`, `ZRANGE` (Min-to-Max), `ZREVRANGE` (Top N), `ZRANK`, `ZREVRANK`, `ZSCORE`, `ZINCRBY`, `ZRANGEBYSCORE`, `ZCARD`, `ZREM`.

---

## 👁️ Quan sát trực quan
Sau khi gửi từng request từ Postman, hãy mở trình duyệt vào [http://localhost:8081](http://localhost:8081) (Redis Commander) để xem trực quan dữ liệu, kiểu dữ liệu, TTL và các phần tử thay đổi tức thì!
