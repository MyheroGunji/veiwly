# Java 17 をベースにした軽量イメージ
FROM openjdk:17-jdk-slim

# 作業ディレクトリを作成
WORKDIR /app

# Maven Wrapper を含む全ファイルをコピー
COPY . .

# Maven Wrapper に実行権限を付与
RUN chmod +x mvnw

# アプリをビルド（テストはスキップ）
RUN ./mvnw clean package -DskipTests

# アプリを起動（正しいJARファイル名を指定）
CMD ["java", "-jar", "target/movieapp-0.0.1-SNAPSHOT.jar"]