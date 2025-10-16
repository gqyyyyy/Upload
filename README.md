# 作业：Thymeleaf 文件上传 + MinIO

本项目实现：
- 使用 Thymeleaf 编写上传页面
- 后端接收文件并上传到 MinIO（服务器：`124.70.108.214:9000`，桶：`assign1`）
- 以“组号+原扩展名”作为上传文件名（示例：`2-11.pdf`）
- 可选输入“组名”用于页面展示（不影响文件命名）

## 运行环境
- JDK 11+
- Maven 3.6+
- Windows/macOS/Linux 均可

## 快速开始
```bash
# 方式一：使用 Maven Wrapper（推荐）
./mvnw spring-boot:run   # Windows PowerShell: .\\mvnw spring-boot:run

# 方式二：打包运行
./mvnw clean package
java -jar target/file-upload-demo-0.0.1-SNAPSHOT.jar
```
启动后访问：`http://localhost:8080`

## 配置说明（application.properties）
```properties
minio.endpoint=http://124.70.108.214:9000
minio.access-key=admin
minio.secret-key=JavaEE20251014
minio.bucket=assign1

spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```
如需变更 MinIO 或上传大小限制，修改上述配置即可。

## 使用说明
1. 打开首页上传页
2. 输入组号（必填，作为最终文件名的主体，例如：`2-11`）
3. 输入组名（可选，例如：`我有OOAD`，仅用于成功提示展示）
4. 选择文件（≤10MB）。支持类型：zip/pdf/doc/docx/ppt/pptx/xls/xlsx/png/jpg/jpeg/txt
5. 点击“上传文件”。成功后页面提示会显示组号与（可选）组名，以及最终文件名

## 截图指引（作业提交）
- 截图 1：前端上传页面 `src/main/resources/templates/upload.html`
- 截图 2：后端上传逻辑（任选其一）
  - 控制器：`src/main/java/org/example/upload/controller/FileUploadController.java`
  - 服务：`src/main/java/org/example/upload/service/MinioService.java`

## 关键实现
- MinIO 客户端 Bean：`org.example.upload.config.MinioConfig`
- 上传服务：`org.example.upload.service.MinioService`
- 控制器校验与命名：`org.example.upload.controller.FileUploadController`
- 上传页面：`src/main/resources/templates/upload.html`

## 注意
- 建议上传较小文件，避免超时
- 文件命名固定为：组号 + 原始扩展名；组名不参与命名

## License
本项目用于课程作业示例，可自由学习与修改。
