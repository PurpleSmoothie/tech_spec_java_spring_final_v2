# subApp

Микросервис на Spring Boot для управления пользователями и их подписками на сервисы. Проект использует PostgreSQL как базу данных и контейнеризацию через Docker.

## Запуск проекта

### Требования:
- Docker
- Docker Compose

### Шаги для запуска:

1. Клонируйте репозиторий.
2. Перейдите в директорию с проектом.
3. Построите приложение с помощью Maven:

```bash
mvn clean package
```

4. Запустите проект с помощью Docker Compose:

```bash
docker-compose up --build
```

5. Откройте браузер и перейдите на [http://localhost:8080](http://localhost:8080), чтобы получить доступ к API.

### Структура Базы Данных:
- **Таблица пользователей (users):**
```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE
);
```

- **Таблица подписок (subscriptions):**
```sql
CREATE TABLE subscriptions (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
```

### API Эндпоинты:

#### **Управление пользователями:**
- `GET /users/{id}` — Получить пользователя по ID
- `PUT /users/{id}` — Обновить пользователя
- `DELETE /users/{id}` — Удалить пользователя
- `POST /users` — Создать пользователя

#### **Управление подписками пользователя:**
- `GET /users/{userId}/subscriptions/{subscriptionId}` — Получить подписку по ID
- `PUT /users/{userId}/subscriptions/{subscriptionId}` — Обновить подписку пользователя
- `DELETE /users/{userId}/subscriptions/{subscriptionId}` — Удалить подписку пользователя
- `GET /users/{userId}/subscriptions` — Получить все подписки пользователя
- `POST /users/{userId}/subscriptions` — Добавить подписку пользователю
- `GET /users/{userId}/subscriptions/top` — Получить топ популярных подписок

### Логирование
Логирование осуществляется через SLF4J.

### Важные замечания:
- Контейнеры автоматически создают базу данных и таблицы при запуске.
- Для подключения к базе данных используйте настройки:
    - **URL**: `jdbc:postgresql://localhost:5432/subapp`
    - **Пользователь**: `user`
    - **Пароль**: `password`