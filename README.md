# ProgettoCalendario - Backend

Backend del progetto **Calendario Settimanale** sviluppato con **Spring Boot 3.4.5**, **MySQL**, **JWT**, **OAuth2 (Google)** e **Swagger/OpenAPI**.

---

## Funzionalità principali

- Registrazione e login (con supporto Google)
- Gestione settimanale di note con allegati/colori
- Interscambio feedback tra utenti/admin
- Sicurezza tramite JWT e OAuth2
- API documentate con Swagger

---

## Configurazione

1. Assicurati di avere MySQL attivo e un database chiamato `Calendar`:
   ```sql
   CREATE DATABASE Calendar;
   ```

2. Copia il file di configurazione di esempio:
   ```bash
   cp src/main/resources/application-local-example.properties src/main/resources/application-local.properties
   ```

3. Modifica `application-local.properties` inserendo:
   - le tue credenziali MySQL (`username`, `password`)
   - un JWT secret
   - (facoltativo) le credenziali Google OAuth2

4. Dopo il **primo avvio**, commenta la riga:
   ```properties
   spring.sql.init.mode=always
   ```
   oppure impostala su `never`.

   ---

5. Il progetto è configurato per l'esecuzione **locale** con `application-local.properties`, ma supporta anche il **deploy su servizi cloud** come Azure o Heroku tramite `application-prod.properties`.

Per attivare il profilo `prod`:
- Imposta la variabile d'ambiente:
  ```bash
  SPRING_PROFILES_ACTIVE=prod
  ```
- Configura i parametri come `${JDBC_DATABASE_URL}`, `${JWT_SECRET}`, ecc. nel servizio di deploy.

---

---

## Avvio del progetto

Compila ed esegui con:
```bash
mvn clean install
mvn spring-boot:run
```

Backend disponibile su:
```
http://localhost:8080
```

Swagger UI:
```
http://localhost:8080/swagger-ui.html
```

---

## Sicurezza

- Tutte le API private richiedono token JWT (`Authorization: Bearer <token>`)
- Login possibile anche con account Google tramite OAuth2

---

## Stack Tecnologico

- Java 17 + Spring Boot 3.4.5
- Spring Security + JWT + OAuth2
- Spring Data JPA + MySQL
- Maven + Swagger/OpenAPI

---
