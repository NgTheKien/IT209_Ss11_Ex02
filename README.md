# Session 11 – Bài 2: Đóng gói .jar với Gradle và lưu trữ Artifacts

## Mục tiêu
- Biên dịch và đóng gói ứng dụng Spring Boot thành tệp `.jar` thực thi bằng `./gradlew bootJar`.
- Lưu thành phẩm `.jar` từ `build/libs/` lên GitHub Actions bằng `actions/upload-artifact@v4`.

## Cấu trúc liên quan
- `.github/workflows/ci.yml`: kịch bản workflow
- `src/main/java/com/rikkei/hello/HelloController.java`: API `GET /api/hello` trả về `Hello World`
- `src/test/java/com/rikkei/hello/HelloControllerTest.java`: unit test JUnit

## Luồng workflow
| # | Step | Mô tả |
|---|---|---|
| 1 | Checkout mã nguồn | `actions/checkout@v5` đưa code của commit vào runner |
| 2 | Thiết lập JDK 17 | `actions/setup-java@v5`, distribution `temurin` |
| 3 | Cấp quyền Gradle Wrapper | `chmod +x gradlew` |
| 4 | Thực thi JUnit Test | `./gradlew test` |
| 5 | Đóng gói JAR | `./gradlew bootJar` tạo file `build/libs/*.jar` |
| 6 | Lưu Artifact | `actions/upload-artifact@v4`, tên `app-jar`, đường dẫn `build/libs/*.jar` |

- **Trigger:** `push` lên nhánh `main`.
- **Runner:** `ubuntu-latest`.

## Kết quả kiểm tra
- Workflow chạy tự động khi push lên `main`, job **Run Unit Tests** có tích xanh.
- Phần **Summary** của lần chạy có mục **Artifacts** chứa `app-jar`, bấm vào là tải được file `.zip` có tệp `.jar` bên trong.
- Chạy thử file jar đã tải về: `java -jar hello-gradle-0.0.1-SNAPSHOT.jar`, rồi mở `http://localhost:8080/api/hello`.

## Giải thích
- **`bootJar` khác `jar` thế nào?** `bootJar` tạo *fat jar*, gói cả code lẫn toàn bộ dependency và Tomcat nhúng, nên chạy được ngay bằng `java -jar`. Task `jar` thường chỉ chứa class của project, có hậu tố `-plain.jar`.
- **Vì sao cần upload artifact?** Runner bị xóa sau khi job kết thúc. Nếu không upload, file `.jar` sẽ mất theo. Artifact giữ lại thành phẩm để tải về hoặc chuyển cho job deploy phía sau.
- **Thứ tự test rồi mới bootJar:** test lỗi thì workflow dừng, không đóng gói. Nhờ vậy chỉ code đã qua kiểm thử mới thành artifact.
