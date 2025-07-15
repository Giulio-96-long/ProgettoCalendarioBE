# ProgettoCalendario - Backend

Backend del progetto **Calendario Settimanale** sviluppato in **Spring Boot 3.4.5**, con autenticazione tramite **JWT** e **OAuth2 (Google)**, e integrazione **MySQL**, **Swagger/OpenAPI**, **Spring Security**.

---

## Funzionalità principali

- Registrazione e Login utenti
- Supporto login con Google (OAuth2)
- CRUD per note settimanali con allegati e colori
- Gestione feedback tra utenti/admin
- Logging centralizzato degli errori
- Autenticazione via JWT
- API documentate con Swagger

---

## Stack Tecnologico

- Java 17
- Spring Boot 3.4.5
- Spring Security + JWT
- Spring OAuth2 Client
- Spring Data JPA (Hibernate)
- MySQL
- Maven
- Swagger/OpenAPI

---

## Struttura del progetto

```
src/main/java/com/example/demo/
├── authenticationToken     # JWT token handling
├── configuration           # Security & OAuth2 config
├── controller              # REST Controllers
├── dto                     # DTOs per le richieste e risposte
├── entity                  # Entity JPA
├── exception               # GlobalExceptionHandler
├── repository              # Repository JPA
├── service                 # Business logic
├── util                    # Utilities varie
└── ProgettoCalendarioApplication.java

src/main/resources/
├── application.properties  # Configurazioni
└── static / templates      # (se necessario)
```

---

## Configurazione `application.properties`

```properties
# MySQL
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/Calendar...
spring.datasource.username=root
spring.datasource.password=...

# Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# JWT
jwt.secret=mySuperMegaSecretKeyForHS512DontShareThis

# CORS
app.cors.allowed-origins=http://127.0.0.1:5501

# OAuth2 - Google
spring.security.oauth2.client.registration.google.client-id=...
spring.security.oauth2.client.registration.google.client-secret=...
```

---

## Esecuzione

Compila e avvia con:

```bash
mvn clean install
mvn spring-boot:run
```

L’applicazione sarà disponibile su:

```
http://localhost:8080
```

Swagger UI:

```
http://localhost:8080/swagger-ui.html
```

---

## Sicurezza

- Autenticazione tramite Bearer Token (JWT)
- Login via Google (OAuth2)
- Tutti gli endpoint API privati richiedono token

---

## Endpoints principali

- `POST /api/auth/login`
- `GET /api/notes`, `POST /api/notes/new`, ...
- `GET /api/logError` (per admin)
- `POST /api/feedback`, `GET /api/feedback`

---

## Documentazione API

Accessibile via Swagger UI una volta avviata l'app:

```
http://localhost:8080/swagger-ui.html
```

---

## Features extra

- Log automatico degli errori via `GlobalExceptionHandler`
- Modulo feedback e commenti
- Storage file con Multipart

---

## Contributors

- [Tuo nome o team]