# Danh Sách 3 Ý Tưởng Bài Tập Thực Hành Redis (5 Kiểu Dữ Liệu Cơ Bản)

Tài liệu này tổng hợp 3 ý tưởng dự án mẫu sử dụng **Spring Boot + Spring Data Redis** nhằm thực hành trọn vẹn 5 kiểu dữ liệu cốt lõi của Redis: **String, Hash, List, Set, Sorted Set (ZSet)**.

---

## 🌟 Ý TƯỞNG 1: "Mini Tech Blog & Social Platform (DevCommunity)"
*(Mô hình mạng xã hội bài viết kỹ thuật tương tự Dev.to / Medium / Viblo)*

### 1. Ý nghĩa nghiệp vụ
Xây dựng một nền tảng chia sẻ bài viết nơi người dùng có thể đọc bài, tương tác (like, tag), xem lịch sử hoạt động và theo dõi bảng xếp hạng bài viết thịnh hành.

### 2. Ánh xạ 5 kiểu dữ liệu Redis

| Kiểu dữ liệu | Nghiệp vụ thực tế | Redis Commands | Spring Data Redis API | Cấu trúc Key & Dữ liệu mẫu |
| :--- | :--- | :--- | :--- | :--- |
| **1. String** | **Lưu mã OTP** xác thực đăng ký có thời gian hết hạn (TTL 5 phút).<br>**Đếm lượt xem bài viết** (Page View Counter). | `SETEX`, `GET`, `INCR` | `opsForValue().set(key, val, timeout, unit)`<br>`opsForValue().increment(key)` | `otp:register:user@email.com` -> `"839201"`<br>`article:101:views` -> `1502` |
| **2. Hash** | **Hồ sơ tác giả (User Profile)**: Quản lý thông tin chi tiết người dùng dưới dạng các cặp field-value, cập nhật từng field độc lập (tên, bio, số follower) mà không cần ghi đè cả object. | `HSET`, `HGET`, `HMGET`, `HGETALL`, `HINCRBY` | `opsForHash().put(key, field, val)`<br>`opsForHash().get(key, field)`<br>`opsForHash().increment(key, field, delta)` | `user:profile:100` -> `{ "name": "Nguyen", "bio": "Backend Dev", "followers": 45 }` |
| **3. List** | **Nhật ký 10 hoạt động gần nhất** của user (Activity Stream): Luôn duy trì đúng 10 hoạt động mới nhất.<br>**Hàng đợi gửi Email chào mừng** (FIFO Message Queue). | `LPUSH`, `LTRIM`, `LRANGE`<br>`RPUSH`, `LPOP` | `opsForList().leftPush(key, val)`<br>`opsForList().trim(key, start, stop)`<br>`opsForList().range(key, start, stop)`<br>`opsForList().rightPush(...)` / `leftPop(...)` | `user:100:activities` -> `["Liked post 42", "Published article 101"]`<br>`queue:email:welcome` -> `["user1@gmail.com", "user2@gmail.com"]` |
| **4. Set** | **Hệ thống Like bài viết**: Chống like trùng lặp (1 user chỉ được like 1 lần).<br>**Tìm bài viết theo giao thẻ (Tag Intersection)**: Lọc các bài viết có đồng thời nhiều tag (ví dụ: vừa có thẻ `java` vừa có thẻ `redis`). | `SADD`, `SREM`, `SISMEMBER`, `SCARD`, `SINTER` | `opsForSet().add(key, val)`<br>`opsForSet().isMember(key, val)`<br>`opsForSet().size(key)`<br>`opsForSet().intersect(key1, key2)` | `article:101:likes` -> `{"user_1", "user_5", "user_9"}`<br>`tag:java:articles` -> `{"101", "102"}`<br>`tag:redis:articles` -> `{"101", "105"}` |
| **5. Sorted Set (ZSet)** | **Bảng xếp hạng bài viết thịnh hành (Trending Leaderboard)**: Mỗi bài viết có một số điểm tương tác (Score) tính từ lượt xem và lượt like. Tự động xếp hạng theo thứ tự điểm từ cao xuống thấp. | `ZADD`, `ZINCRBY`, `ZREVRANGE`, `ZREVRANK`, `ZSCORE` | `opsForZSet().add(key, val, score)`<br>`opsForZSet().incrementScore(key, val, delta)`<br>`opsForZSet().reverseRangeWithScores(key, start, stop)`<br>`opsForZSet().reverseRank(key, val)` | `leaderboard:articles:trending`<br>Member: `article:101`, Score: `245.0`<br>Member: `article:102`, Score: `180.0` |

---

## 🛒 Ý TƯỞNG 2: "Mini E-Commerce (Flash Sale & Giỏ Hàng)"
*(Mô hình sàn thương mại điện tử với tính năng săn deal và giỏ hàng)*

### 1. Ý nghĩa nghiệp vụ
Giải quyết các bài toán tốc độ cao trong thương mại điện tử: chống bán quá số lượng kho (overselling) trong các đợt Flash Sale, lưu giỏ hàng tạm thời vào RAM và lọc sản phẩm đa tiêu chí.

### 2. Ánh xạ 5 kiểu dữ liệu Redis

| Kiểu dữ liệu | Nghiệp vụ thực tế | Redis Commands | Spring Data Redis API | Cấu trúc Key & Dữ liệu mẫu |
| :--- | :--- | :--- | :--- | :--- |
| **1. String** | **Quản lý kho tồn Flash Sale (Atomic Inventory)**: Trừ kho tức thời và an toàn trong môi trường nhiều luồng (concurrency) để chống bán âm kho.<br>**Rate Limit chống Spam click đặt hàng**. | `SET`, `DECR`, `INCR` | `opsForValue().decrement(key)`<br>`opsForValue().setIfAbsent(...)` | `flashsale:stock:iphone15` -> `50`<br>Mỗi khi đặt hàng: gọi `decrement()` nguyên tử, nếu kết quả < 0 thì báo hết hàng. |
| **2. Hash** | **Giỏ hàng của người dùng (Shopping Cart)**: Key là mã user, mỗi Field là mã sản phẩm (productId), Value là số lượng đặt mua. Dễ dàng tăng giảm số lượng hoặc xóa 1 món đồ. | `HSET`, `HINCRBY`, `HDEL`, `HGETALL`, `HLEN` | `opsForHash().put(key, field, val)`<br>`opsForHash().increment(key, field, delta)`<br>`opsForHash().delete(key, field)`<br>`opsForHash().entries(key)` | `cart:user:2001`<br>Field `prod:10` -> `2`<br>Field `prod:15` -> `1` |
| **3. List** | **Lịch sử đơn hàng gần nhất của khách hàng** (Recent Orders Log).<br>**Hàng đợi xử lý thanh toán (Order Payment Queue FIFO)**: Đẩy đơn hàng vào queue để tiến trình nền trừ tiền và xuất hóa đơn. | `LPUSH`, `LTRIM`, `LRANGE`<br>`RPUSH`, `LPOP` | `opsForList().leftPush(...)`<br>`opsForList().trim(...)`<br>`opsForList().rightPush(...)`<br>`opsForList().leftPop(...)` | `user:2001:recent_orders` -> `["ORD_9901", "ORD_9842"]`<br>`queue:orders:pending` -> `["ORD_9901", "ORD_9902"]` |
| **4. Set** | **Bộ lọc thuộc tính sản phẩm**: Tìm các sản phẩm thỏa mãn nhiều tiêu chí (ví dụ: danh sách màu đen ∩ danh sách hãng Apple ∩ danh sách có sẵn hàng).<br>**Danh sách khách hàng VIP được áp mã giảm giá**. | `SADD`, `SINTER`, `SUNION`, `SISMEMBER` | `opsForSet().add(...)`<br>`opsForSet().intersect(...)`<br>`opsForSet().isMember(...)` | `filter:brand:apple` -> `{"p1", "p2", "p5"}`<br>`filter:color:black` -> `{"p1", "p3", "p5"}`<br>Giao tập hợp (`SINTER`): `{"p1", "p5"}` |
| **5. Sorted Set (ZSet)** | **Lọc sản phẩm theo khoảng giá tiền (Price Range Filter)**: Score chính là giá sản phẩm.<br>**Top sản phẩm bán chạy nhất tuần (Best Sellers)**. | `ZADD`, `ZRANGEBYSCORE`, `ZINCRBY`, `ZREVRANGE` | `opsForZSet().add(key, val, price)`<br>`opsForZSet().rangeByScore(key, min, max)`<br>`opsForZSet().incrementScore(key, val, qty)`<br>`opsForZSet().reverseRangeWithScores(...)` | `products:price`<br>Member: `p1`, Score: `150.0` (giá $150)<br>Member: `p2`, Score: `450.0` (giá $450)<br>`ZRANGEBYSCORE products:price 100 300` -> lấy `p1` |

---

## 🎮 Ý TƯỞNG 3: "Mini Gamified Quiz & Coding Challenge (Kahoot / LeetCode Mini)"
*(Mô hình ứng dụng thi đấu giải đố và bài tập lập trình tính điểm)*

### 1. Ý nghĩa nghiệp vụ
Xây dựng nền tảng giải đố trắc nghiệm hoặc thử thách code theo thời gian thực: người dùng tham gia làm bài, nhận điểm thưởng, cạnh tranh thứ hạng trên bảng xếp hạng toàn cầu.

### 2. Ánh xạ 5 kiểu dữ liệu Redis

| Kiểu dữ liệu | Nghiệp vụ thực tế | Redis Commands | Spring Data Redis API | Cấu trúc Key & Dữ liệu mẫu |
| :--- | :--- | :--- | :--- | :--- |
| **1. String** | **Token phiên làm bài & Bộ đếm số lượt nộp bài**: Đếm số lần submit trong ngày của user để giới hạn theo ngày (Daily limit).<br>**Mã khóa phòng thi (Room Pin Code)** có TTL hết hạn sau thời gian thi. | `SETEX`, `INCR`, `GET` | `opsForValue().set(key, val, timeout, unit)`<br>`opsForValue().increment(key)` | `exam:session:user_100` -> `"TOKEN_ABCXYZ"` (TTL: 45 phút)<br>`daily_submits:user_100:20260922` -> `14` |
| **2. Hash** | **Trạng thái bài thi hiện tại (Active Quiz State)**: Lưu trữ câu hỏi hiện tại, câu trả lời đã chọn, số câu đúng/sai của thí sinh mà không cần truy vấn DB liên tục. | `HSET`, `HGET`, `HGETALL`, `HINCRBY` | `opsForHash().put(...)`<br>`opsForHash().increment(...)`<br>`opsForHash().entries(...)` | `quiz:session:user_100`<br>Field `current_question` -> `5`<br>Field `correct_count` -> `4`<br>Field `time_spent_seconds` -> `120` |
| **3. List** | **Lịch sử các lần nộp bài gần nhất** (Recent Submissions).<br>**Hàng đợi chấm điểm tự động (Judge Queue FIFO)**: Đẩy bài làm của thí sinh vào để worker ngầm chạy test case và chấm điểm. | `LPUSH`, `LTRIM`, `LRANGE`<br>`RPUSH`, `LPOP` | `opsForList().leftPush(...)`<br>`opsForList().trim(...)`<br>`opsForList().rightPush(...)`<br>`opsForList().leftPop(...)` | `user:100:submissions` -> `["Accepted - Problem 12", "Wrong Answer - Problem 15"]`<br>`queue:judge:tasks` -> `["SUB_8831", "SUB_8832"]` |
| **4. Set** | **Danh sách bài tập đã giải thành công**: Đảm bảo mỗi bài chỉ được cộng điểm lần đầu tiên (chống farm điểm lặp lại).<br>**Hệ thống phân loại Tag bài tập** (Array, Graph, DP, Tree). | `SADD`, `SISMEMBER`, `SCARD`, `SINTER` | `opsForSet().add(...)`<br>`opsForSet().isMember(...)`<br>`opsForSet().intersect(...)` | `user:100:solved_problems` -> `{"PROB_1", "PROB_15", "PROB_99"}`<br>`tag:dp:problems` ∩ `tag:tree:problems` -> Tìm các bài vừa là Dynamic Programming vừa là Tree. |
| **5. Sorted Set (ZSet)** | **Bảng xếp hạng điểm thi đấu toàn cầu (Global Leaderboard)**: Tự động sắp xếp vị trí người chơi theo tổng điểm số (Score). Hỗ trợ lấy Top N và tra cứu thứ hạng cá nhân (Rank). | `ZADD`, `ZINCRBY`, `ZREVRANGE`, `ZREVRANK`, `ZSCORE` | `opsForZSet().add(...)`<br>`opsForZSet().incrementScore(...)`<br>`opsForZSet().reverseRangeWithScores(...)`<br>`opsForZSet().reverseRank(...)` | `leaderboard:global`<br>Member: `user_100`, Score: `1580.0`<br>Member: `user_205`, Score: `1420.0`<br>Tra cứu hạng cá nhân: `opsForZSet().reverseRank("leaderboard:global", "user_100")` |

---

## 🎯 Gợi ý lựa chọn
- **Nếu muốn học bài bản, toàn diện và phổ biến nhất**: Chọn **Ý tưởng 1 (DevCommunity)**.
- **Nếu mục tiêu là tối ưu hiệu năng cao, concurrency, e-commerce**: Chọn **Ý tưởng 2 (E-Commerce)**.
- **Nếu thích tính tương tác thời gian thực, bảng xếp hạng game**: Chọn **Ý tưởng 3 (Quiz & Challenge)**.
