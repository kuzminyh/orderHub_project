
FROM maven:3.9.6-eclipse-temurin-21 AS builder

# Аргументы сборки (передаются из CI/CD, например: --build-arg APP_VERSION=1.2.3)
# НЕ ИСПОЛЬЗУЙТЕ для секретов (паролей, токенов) — они останутся в образе!
ARG APP_VERSION
ARG BUILD_DATE

# LABEL добавляет метаданные к образу
# docker inspect покажет версию и дату сборки
LABEL version=${APP_VERSION}
LABEL build-date=${BUILD_DATE}
LABEL maintainer="OrderHub"

# Рабочая директория внутри контейнера
WORKDIR /app

COPY pom.xml .
#COPY settings.xml /root/.m2/settings.xml


RUN --mount=type=cache,target=/root/.m2 \
    mvn dependency:go-offline -B \
    -Dmaven.wagon.http.retryHandler.count=3


COPY src ./src

RUN --mount=type=cache,target=/root/.m2 \
    mvn clean package -DskipTests -B -X

# -----------------------------------------------------------------------------
# ЭТАП 2: ФИНАЛЬНЫЙ ОБРАЗ (PRODUCTION)
# -----------------------------------------------------------------------------
# Используем минимальный образ с JRE (без JDK, без компилятора)
# alpine — максимально лёгкий Linux (≈5MB)
FROM eclipse-temurin:21-jre-alpine

# Повторяем аргументы для LABEL (они не передаются между этапами автоматически)
ARG APP_VERSION
ARG BUILD_DATE

# Метаданные финального образа
LABEL version=${APP_VERSION}
LABEL build-date=${BUILD_DATE}
LABEL maintainer="OrderHub"

WORKDIR /app

# Устанавливаем curl для healthcheck
# apk add — пакетный менеджер Alpine
# --no-cache — не сохранять индекс пакетов (меньше размер)
RUN apk add --no-cache curl

# Создаём непривилегированного пользователя для безопасности
# spring:spring — пользователь и группа
# -S — системный пользователь (без домашней директории)
RUN addgroup -S spring && adduser -S spring -G spring

# Копируем собранный jar из предыдущего этапа
# --from=builder — берём файл из этапа "builder"
COPY --from=builder /app/target/*.jar app.jar

# Документируем, какой порт использует приложение
# Не открывает порт автоматически, только информация
EXPOSE 8082

USER spring:spring

HEALTHCHECK --interval=30s --timeout=3s --retries=3 \
  CMD curl -f http://localhost:8082/actuator/health || exit 1

# Точка входа — команда по умолчанию
# Переменные окружения (SPRING_DATASOURCE_URL и др.) подставляются из docker-compose
# Массивная форма ["java", "-jar"] предпочтительнее shell-формы
ENTRYPOINT ["java", "-jar", "app.jar"]