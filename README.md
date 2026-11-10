# Spring AI Intro

A small Spring Boot REST API that sends questions to OpenAI through [Spring AI](https://docs.spring.io/spring-ai/reference/).
It is a learning project. It shows how to:

- send a free-form prompt to a chat model and return the reply
- load prompts from **template files** (`.st`, StringTemplate) and fill in placeholders
- get **structured output**: use `BeanOutputConverter` to have the model reply in JSON that matches a Java record, then turn that JSON into the record

## Tech stack

| | Version |
|---|---|
| Java | 21 (Gradle toolchain) |
| Spring Boot | 4.1.2 (Spring MVC) |
| Spring AI | 2.0.1 (`spring-ai-starter-model-openai`) |
| Jackson | 3 (`tools.jackson`, comes with Boot 4) |
| Gradle | 9.8.2 (wrapper included) |

## Getting started

### Prerequisites

- JDK 21
- An [OpenAI API key](https://platform.openai.com/api-keys)

### Configure the API key

The app reads the key from the `OPENAI_API_KEY` environment variable (see `src/main/resources/application.properties`):

```properties
spring.ai.openai.api-key=${OPENAI_API_KEY}
```

```bash
export OPENAI_API_KEY=sk-...
```

> Never commit a real key. If you want a local config file instead, put it in `application-local.properties`.
> `.gitignore` already ignores that file and `.env` files.

### Run

```bash
./gradlew bootRun
```

The API starts on `http://localhost:8080`.

## API

All endpoints are `POST` and take and return JSON.

> **Postman:** import `spring-ai-intro.postman_collection.json` from the project root.
> It has all three requests. A `baseUrl` collection variable (default `http://localhost:8080`) sets the address,
> and each request has basic response tests.

### `POST /ask`

Sends any question to the model.

```bash
curl -s -X POST localhost:8080/ask \
  -H 'Content-Type: application/json' \
  -d '{"question": "Write a haiku about Spring Boot"}'
```

```json
{ "answer": "..." }
```

### `POST /capital`

Returns the capital of a state or country as structured output.

```bash
curl -s -X POST localhost:8080/capital \
  -H 'Content-Type: application/json' \
  -d '{"stateOrCountry": "France"}'
```

```json
{ "answer": "Paris" }
```

### `POST /capitalWithInfo`

Returns the capital plus extra details about it.

```bash
curl -s -X POST localhost:8080/capitalWithInfo \
  -H 'Content-Type: application/json' \
  -d '{"stateOrCountry": "Japan"}'
```

```json
{
  "city": "Tokyo",
  "population": "...",
  "region": "...",
  "language": "Japanese",
  "currency": "Japanese yen"
}
```

## How structured output works

1. `BeanOutputConverter<T>` is created for the response record (for example `CapitalWithInfoResponse`).
2. `converter.getFormat()` builds instructions that include a JSON Schema for that record.
   The `@JsonPropertyDescription` annotations on the record fields become field descriptions in that schema.
3. Those instructions fill the `{format}` placeholder in the prompt template:

   ```
   What is the capital of {stateOrCountry}? {format}
   ```

4. The model replies with JSON, and `converter.convert(...)` turns it into the record.

## Project structure

```
src/main/java/com/bnaqica/spring_ai_intro
├── SpringAiIntroApplication.java     # Spring Boot entry point
├── controllers/
│   └── QuestionController.java       # REST endpoints
├── models/                           # Request/response records
│   ├── Question.java / Answer.java
│   ├── GetCapitalRequest.java
│   ├── GetCapitalResponse.java
│   └── CapitalWithInfoResponse.java
└── services/
    ├── OpenAiService.java            # Service interface
    └── OpenAiServiceImpl.java        # Calls ChatModel and builds prompts

src/main/resources
├── application.properties
└── templates/                        # Prompt templates (StringTemplate)
    ├── get-capital-prompt.st
    └── get-capital-with-info.st
```

## Tests

```bash
./gradlew test
```

The tests use `@SpringBootTest`. They start the full application and **call the real OpenAI API**,
so you need `OPENAI_API_KEY` set, and each run uses a small number of API tokens.

## Notes on Spring Boot 4

Spring Boot 4 uses **Jackson 3**. Most Jackson classes moved from `com.fasterxml.jackson` to `tools.jackson`.
For example, use `tools.jackson.databind.ObjectMapper`. Code written for Boot 3 that imports
`com.fasterxml.jackson.databind.ObjectMapper` won't find an auto-configured bean.
The annotations, such as `@JsonPropertyDescription`, keep their old `com.fasterxml.jackson.annotation` package.
