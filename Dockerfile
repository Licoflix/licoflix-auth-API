# Estágio de build: Compila o código e gera o .jar
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
# Esta etapa baixa as dependências e as armazena em cache
RUN mvn dependency:go-offline -B
COPY src ./src
# Gera o pacote .jar
RUN mvn clean package -DskipTests

# Estágio de execução: Cria a imagem final leve
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Copia o arquivo .jar gerado no estágio anterior
COPY --from=build /app/target/*.jar app.jar
# Expõe a porta (será sobrescrita pelo docker-compose)
EXPOSE 8081
# Comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]