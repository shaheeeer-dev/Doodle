FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY . .

RUN javac -d out $(find backend/src/java -name "*.java")

EXPOSE 8080

CMD ["java", "-cp", "out", "backend.src.java.com.doodle.Main"]