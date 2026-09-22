# Kịch Bản Học Thuật: Thực Hành Từng Kiểu Dữ Liệu Redis Độc Lập
*(Redis Modular Hands-on Practice Curriculum)*

Tài liệu này mô tả chi tiết phương pháp học theo **từng Module độc lập**: Đi tuần tự qua 5 kiểu dữ liệu cốt lõi, mỗi module cung cấp đầy đủ **Service**, **REST Controller** và ví dụ gọi API tương ứng với từng lệnh Redis mà bạn đã tổng hợp trong sơ đồ tư duy.

---

## 🎯 Mục Tiêu & Ưu Điểm Của Phương Pháp
1. **Cô lập kiến thức**: Không bị phân tâm bởi logic nghiệp vụ phức tạp, tập trung 100% vào bản chất của từng lệnh Redis và cách gọi trong Spring Data Redis.
2. **Test trực quan 1-1**: Mỗi lệnh Redis có một API endpoint tương ứng. Bạn chỉ cần gửi request HTTP (qua trình duyệt, cURL, Postman hoặc file `.http`) là thấy ngay kết quả trả về và quan sát được sự thay đổi trên giao diện **Redis Commander** (`http://localhost:8081`).
3. **Bộ mã nguồn mẫu (Cheat Sheet)**: Sau khi hoàn thành, dự án sẽ trở thành một thư viện code tra cứu nhanh (Quick Reference) mỗi khi bạn cần dùng lại bất kỳ kiểu dữ liệu nào trong tương lai.

---

## 🏗️ Cấu Trúc Dự Án Đề Xuất

```text
src/main/java/com/example/redis/
├── config/
│   └── RedisConfig.java                 <-- Đã cấu hình Serializer chuẩn
└── modules/
    ├── string/
    │   ├── StringService.java          <-- Triển khai các phương thức ValueOperations
    │   └── StringController.java       <-- Endpoint REST API để test các lệnh String
    ├── hash/
    │   ├── HashService.java            <-- Triển khai các phương thức HashOperations
    │   └── HashController.java         <-- Endpoint REST API để test các lệnh Hash
    ├── list/
    │   ├── ListService.java            <-- Triển khai các phương thức ListOperations
    │   └── ListController.java         <-- Endpoint REST API để test các lệnh List
    ├── set/
    │   ├── SetService.java             <-- Triển khai các phương thức SetOperations
    │   └── SetController.java          <-- Endpoint REST API để test các lệnh Set
    └── zset/
        ├── ZSetService.java            <-- Triển khai các phương thức ZSetOperations
        └── ZSetController.java         <-- Endpoint REST API để test các lệnh ZSet
```

---

## 📋 Chi Tiết Từng Module & Danh Sách Lệnh / API

### 1. Module 1: String (`/api/redis/string`)
*Giao diện thao tác trong Spring Data Redis*: `redisTemplate.opsForValue()`

| Lệnh Redis | Thao tác | Spring Data Redis Method | API Endpoint đề xuất |
| :--- | :--- | :--- | :--- |
| `SET` | Lưu key-value | `opsForValue().set(key, value)` | `POST /set` `{key, value}` |
| `GET` | Đọc value của key | `opsForValue().get(key)` | `GET /get?key=...` |
| `SETEX` | Lưu kèm TTL (giây) | `opsForValue().set(key, val, timeout, TimeUnit.SECONDS)` | `POST /setex` `{key, value, seconds}` |
| `INCR` | Tăng số nguyên lên 1 | `opsForValue().increment(key)` | `POST /incr?key=...` |
| `DECR` | Giảm số nguyên đi 1 | `opsForValue().decrement(key)` | `POST /decr?key=...` |
| `MSET` | Gán nhiều key cùng lúc | `opsForValue().multiSet(map)` | `POST /mset` `{map}` |
| `MGET` | Đọc nhiều key cùng lúc | `opsForValue().multiGet(keys)` | `GET /mget?keys=k1,k2` |
| `APPEND` | Nối chuỗi vào key | `opsForValue().append(key, value)` | `POST /append?key=...&value=...` |
| `STRLEN` | Đo độ dài chuỗi | `opsForValue().size(key)` | `GET /strlen?key=...` |

---

### 2. Module 2: Hash (`/api/redis/hash`)
*Giao diện thao tác trong Spring Data Redis*: `redisTemplate.opsForHash()`

| Lệnh Redis | Thao tác | Spring Data Redis Method | API Endpoint đề xuất |
| :--- | :--- | :--- | :--- |
| `HSET` | Thêm / Cập nhật 1 field | `opsForHash().put(key, field, val)` | `POST /hset` `{key, field, value}` |
| `HGET` | Đọc 1 field cụ thể | `opsForHash().get(key, field)` | `GET /hget?key=...&field=...` |
| `HGETALL` | Lấy toàn bộ cặp field-value | `opsForHash().entries(key)` | `GET /hgetall?key=...` |
| `HMSET` / `HSET multi` | Lưu nhiều field cùng lúc | `opsForHash().putAll(key, map)` | `POST /hmset` `{key, map}` |
| `HMGET` | Đọc nhiều field chỉ định | `opsForHash().multiGet(key, fields)` | `GET /hmget?key=...&fields=f1,f2` |
| `HDEL` | Xóa 1 hoặc nhiều field | `opsForHash().delete(key, fields)` | `DELETE /hdel?key=...&fields=f1,f2` |
| `HEXISTS` | Kiểm tra field có tồn tại | `opsForHash().hasKey(key, field)` | `GET /hexists?key=...&field=...` |
| `HLEN` | Đếm tổng số field trong Hash | `opsForHash().size(key)` | `GET /hlen?key=...` |
| `HINCRBY` | Tăng giá trị số của 1 field | `opsForHash().increment(key, field, delta)` | `POST /hincrby?key=...&field=...&delta=...` |

---

### 3. Module 3: List (`/api/redis/list`)
*Giao diện thao tác trong Spring Data Redis*: `redisTemplate.opsForList()`

| Lệnh Redis | Thao tác | Spring Data Redis Method | API Endpoint đề xuất |
| :--- | :--- | :--- | :--- |
| `LPUSH` | Chèn phần tử vào đầu trái (Head) | `opsForList().leftPush(key, value)` | `POST /lpush` `{key, value}` |
| `RPUSH` | Chèn phần tử vào đuôi phải (Tail) | `opsForList().rightPush(key, value)` | `POST /rpush` `{key, value}` |
| `LPOP` | Lấy và xóa phần tử ở đầu trái | `opsForList().leftPop(key)` | `POST /lpop?key=...` |
| `RPOP` | Lấy và xóa phần tử ở đuôi phải | `opsForList().rightPop(key)` | `POST /rpop?key=...` |
| `LRANGE` | Lấy danh sách phần tử trong khoảng | `opsForList().range(key, start, stop)` | `GET /lrange?key=...&start=0&stop=-1` |
| `LLEN` | Đếm tổng số phần tử | `opsForList().size(key)` | `GET /llen?key=...` |
| `LTRIM` | Cắt và chỉ giữ lại khoảng chỉ số | `opsForList().trim(key, start, stop)` | `POST /ltrim?key=...&start=...&stop=...` |
| `LINDEX` | Lấy phần tử theo vị trí index | `opsForList().index(key, index)` | `GET /lindex?key=...&index=...` |

---

### 4. Module 4: Set (`/api/redis/set`)
*Giao diện thao tác trong Spring Data Redis*: `redisTemplate.opsForSet()`

| Lệnh Redis | Thao tác | Spring Data Redis Method | API Endpoint đề xuất |
| :--- | :--- | :--- | :--- |
| `SADD` | Thêm phần tử vào Set | `opsForSet().add(key, members)` | `POST /sadd` `{key, members}` |
| `SREM` | Xóa phần tử khỏi Set | `opsForSet().remove(key, members)` | `DELETE /srem` `{key, members}` |
| `SISMEMBER` | Kiểm tra tồn tại trong Set | `opsForSet().isMember(key, member)` | `GET /sismember?key=...&member=...` |
| `SMEMBERS` | Lấy tất cả phần tử | `opsForSet().members(key)` | `GET /smembers?key=...` |
| `SCARD` | Đếm tổng số phần tử trong Set | `opsForSet().size(key)` | `GET /scard?key=...` |
| `SINTER` | Phép giao giữa các Set | `opsForSet().intersect(keys)` | `GET /sinter?keys=k1,k2` |
| `SUNION` | Phép hợp giữa các Set | `opsForSet().union(keys)` | `GET /sunion?keys=k1,k2` |
| `SDIFF` | Phép hiệu giữa các Set | `opsForSet().difference(k1, k2)` | `GET /sdiff?key1=...&key2=...` |

---

### 5. Module 5: Sorted Set / ZSet (`/api/redis/zset`)
*Giao diện thao tác trong Spring Data Redis*: `redisTemplate.opsForZSet()`

| Lệnh Redis | Thao tác | Spring Data Redis Method | API Endpoint đề xuất |
| :--- | :--- | :--- | :--- |
| `ZADD` | Thêm phần tử kèm điểm số (score) | `opsForZSet().add(key, member, score)` | `POST /zadd` `{key, member, score}` |
| `ZRANGE` | Lấy phần tử từ điểm thấp đến cao | `opsForZSet().rangeWithScores(key, start, stop)` | `GET /zrange?key=...&start=0&stop=-1` |
| `ZREVRANGE` | Lấy phần tử từ điểm cao xuống thấp | `opsForZSet().reverseRangeWithScores(key, start, stop)` | `GET /zrevrange?key=...&start=0&stop=9` |
| `ZRANK` | Thứ hạng từ thấp lên cao (bắt đầu 0) | `opsForZSet().rank(key, member)` | `GET /zrank?key=...&member=...` |
| `ZREVRANK` | Thứ hạng từ cao xuống thấp (Top N) | `opsForZSet().reverseRank(key, member)` | `GET /zrevrank?key=...&member=...` |
| `ZSCORE` | Lấy điểm số hiện tại của phần tử | `opsForZSet().score(key, member)` | `GET /zscore?key=...&member=...` |
| `ZINCRBY` | Tăng / giảm điểm số của phần tử | `opsForZSet().incrementScore(key, member, delta)` | `POST /zincrby?key=...&member=...&delta=...` |
| `ZRANGEBYSCORE`| Lọc phần tử có điểm trong [min, max] | `opsForZSet().rangeByScoreWithScores(key, min, max)` | `GET /zrangebyscore?key=...&min=...&max=...` |
| `ZREM` | Xóa phần tử khỏi ZSet | `opsForZSet().remove(key, members)` | `DELETE /zrem?key=...&members=...` |
| `ZCARD` | Đếm tổng số phần tử | `opsForZSet().size(key)` | `GET /zcard?key=...` |

---

## 🚀 Kế Hoạch Triển Khai Từng Bước
1. **Bước 1**: Xây dựng **`StringModule`** (Service + Controller) -> Chạy ứng dụng, test tất cả các lệnh String qua REST API và xem trực quan trên Redis Commander.
2. **Bước 2**: Xây dựng **`HashModule`** -> Thao tác với đối tượng, kiểm chứng `HINCRBY` và `HGETALL`.
3. **Bước 3**: Xây dựng **`ListModule`** -> Thao tác Queue (FIFO), Stack (LIFO), test `LTRIM` giữ cố định N log.
4. **Bước 4**: Xây dựng **`SetModule`** -> Thao tác tập hợp không trùng lặp, test các phép toán `SINTER`, `SUNION`, `SDIFF`.
5. **Bước 5**: Xây dựng **`ZSetModule`** -> Thao tác bảng xếp hạng điểm số, lấy Top Leaderboard và lọc theo khoảng điểm.
