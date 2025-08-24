# Notification Service

## Описание
Образовательный микросервис на Spring Boot, который:
- принимает события из Kafka о создании/удалении пользователя,
- отправляет пользователю письмо через SMTP (Mailtrap),
- имеет REST-эндпоинт для «ручной» отправки писем.

## Возможности
- Потребление событий из Kafka (`user-events`) и делегирование обработчикам по типу события.
- Отправка писем:
  - «Аккаунт создан»
  - «Аккаунт удалён»
- Ручная отправка через REST (`/api/email`).

## Структура проекта

| Пакет / Директория      | Описание                                                                 |
|-------------------------|--------------------------------------------------------------------------|
| `controller/`           | REST-контроллеры                                                         |
| `kafka/`                | Слушатели Kafka                                                          |
| `dto/`                  | DTO и перечисления                                                       |
| `service/`              | Почтовый сервис (интерфейсы и реализации)                                |
| `service/handlers/`     | Обработчики событий (стратегии под каждый `UserEventType`)               |
| `config/`               | Конфигурация DI и бин-проводки                                           |
| `exception/`            | Доменные исключения и глобальная обработка ошибок                        |
| `resources/`            | Конфигурация приложения (`application.yml`)                              |

## REST API (ручная отправка)

**POST** `/api/email`

- **Поля запроса**  
  - `eventType` — одно из: `CreateUser`, `DeleteUser` (соответствует `@JsonProperty` у enum).  
  - `email` — обязательный, проверяется через `@NotBlank @Email`.  

- **Валидация**  
  - `@NotNull eventType`  
  - `@NotBlank @Email email`  

- **Ответы**  
  - `200 OK` / `202 Accepted` — письмо успешно отправлено  
  - `400 Bad Request` — ошибки валидации  
  - `422 Unprocessable Entity` — неподдерживаемый `eventType`  
  - `502 Bad Gateway` — ошибка доставки письма (SMTP)  

---

## Kafka

- **Топик:** `user-events`  
- **Группа консюмера:** `notification-service`  
- **Сериализация:**  
  - Producer (в `user-service`) — `JsonSerializer`  
  - Consumer (в `notification-service`) — `JsonDeserializer` с настройками:  
    ```yaml
    spring.json.trusted.packages: ru.naragas.notificationservice.dto
    spring.json.value.default.type: ru.naragas.notificationservice.dto.UserEventDTO
    ```


## Технологии
- Java 17+
- Spring Boot
- Spring Kafka
- Spring Boot Starter Mail
- Spring Web
- Spring Validation
- Lombok
- Maven

## Контакты
**Автор:** Naragas  
**GitHub:** [https://github.com/Naragas](https://github.com/Naragas)
