# Cần cài đặt dưới local:

1. PostgreSQL (Xem hướng dẫn trong file application.properties)

- Cài Postgres: http://postgresql.org/download/windows/
  - Trong quá trình cài, thấy nó cho phép cài luôn PgAdmin4 thì tích vào, không thì phải tự cài tay
  - Không bắt buộc dùng PgAdmin4, dùng tool khác mở lên cũng được
- Tạo database "topcv"
- Trong Login/Group Roles, tạo user + password & cấu hình quyền

  - (Xem hướng dẫn trong file application.properties)

- Redis

  - Tải redis-server cho windows về, chạy nó lên (sau đó nó sẽ chạy dưới background nền)
  - Tải Redis Desktop về để xem, kết nối thử đến redis-server
    --> Nhấn nút Connect to Redis Server
    --> Nhấn Test Connection
  - Thường redis sẽ chạy ở port 6379

- Java 21 (nhớ thêm biến môi trường cho máy tính nhé)
- Maven
  - Note thêm: dưới máy local, maven hoạt động rất ngon lành
    nhưng trong công ty có proxy chặn lại, maven có thể gây ra lỗi, phải tự set up proxy cho maven
- Nếu dùng IDE khác thì không rõ, nhưng nếu dùn Visual Studio Code, có thể add thêm các extension sau:
  - Language Support for Java(TM) by Red Hat
  - Debugger for Java
  - Gradle for Java
  - Extension Pack for Java
  - Project Manager for Java
  - Maven for Java
  - Spring Initializr Java Support
