### Application for Creating Tournament Brackets

This app helps you to:

1. Create a competition with a brief description.
2. Allow participants to register for the competition.
3. Generate tournament brackets.

This is the backend part of the Brackets app.
To view the frontend, visit the [brackets-front](https://github.com/pavelmno4/brackets-front) repository.

To set up the environment and start the app:

| variable           | type   | required |
|--------------------|--------|----------|
| KTOR_PORT          | number | false    |
| KTOR_AUTH_SIGN_KEY | hex    | true     |
| KTOR_LOG_LEVEL     | text   | false    |
| DB_JDBC_URL        | text   | true     |
| DB_PASSWORD        | text   | true     |
| DB_USERNAME        | text   | true     |