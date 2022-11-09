# keycloak-event-listener-http

Use Keycloak Version 18.0.2

A Keycloak SPI that publishes events to an HTTP Webhook.
A (largely) adaptation of @mhui mhuin/keycloak-event-listener-mqtt SPI.
Extended by @darrensapalo
to [enable building the JAR files from docker images](https://sapalo.dev/2021/06/16/send-keycloak-webhook-events/).

# Build

## Build on your local machine

```
mvn clean install
```

# Deploy

* Copy target/event-listener-http-jar-with-dependencies.jar to {KEYCLOAK_HOME}/providers

# Use

```
bin\kc.bat start-dev --http-relative-path=/auth --spi-events-listener-http-server-uri=http://127.0.0.1:9191/svc/audit/webhook
```

Request Logout

```
{
  "id": "5ec4bad0-e520-48cc-87c2-537b6efed74f",
  "time": 1667970659751,
  "type": "LOGOUT",
  "realmId": "a0a8da50-0d25-4369-8531-c897f7bfd6ef",
  "userId": "acda725a-e150-41c7-904f-342f3f49b7d8",
  "sessionId": "655d6597-0a43-4342-a49a-112665be7081",
  "ipAddress": "127.0.0.1",
  "details": {
    "redirect_uri": "http://localhost:8080/auth/admin/master/console/#/realms/master"
  }
}
```

Request Login

```
{
  "id": "d65786f1-d762-4789-af13-003d0cda3289",
  "time": 1667970691920,
  "type": "LOGIN",
  "realmId": "a0a8da50-0d25-4369-8531-c897f7bfd6ef",
  "clientId": "security-admin-console",
  "userId": "acda725a-e150-41c7-904f-342f3f49b7d8",
  "sessionId": "bef577d1-7a2e-43bd-bcbc-1f25e9040884",
  "ipAddress": "127.0.0.1",
  "details": {
    "auth_method": "openid-connect",
    "auth_type": "code",
    "redirect_uri": "http://localhost:8080/auth/admin/master/console/#/realms/master",
    "consent": "no_consent_required",
    "code_id": "bef577d1-7a2e-43bd-bcbc-1f25e9040884",
    "username": "superadmin"
  }
}
```