# ============================
# 🏗️ Etapa de build (compilação)
# ============================
FROM ubuntu:latest AS build

RUN apt-get update && apt-get install -y openjdk-21-jdk maven git curl
WORKDIR /app

# Copia o código para dentro do container
COPY . .

# Compila o projeto
RUN mvn clean install -DskipTests -B

# ============================
# 🚀 Etapa final (execução)
# ============================
FROM openjdk:21-jdk-slim

# Instala dependências necessárias para o Apache POI (fontes) e PostgreSQL client
RUN apt-get update && apt-get install -y \
    libfreetype6 \
    fontconfig \
    postgresql-client \
    && rm -rf /var/lib/apt/lists/*

# Define a porta exposta
EXPOSE 8080

# Copia o JAR gerado na etapa de build
COPY --from=build /app/target/api-0.0.1-SNAPSHOT.jar /app/app.jar

# Define o diretório de trabalho
WORKDIR /app

# Define o comando de entrada
ENTRYPOINT ["java", "-jar", "app.jar"]