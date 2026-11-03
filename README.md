# Forex Horizon — Java Applications Homework

Java 25 / Spring Boot / Thymeleaf / Bootstrap responsive site for a fictional currency analytics company. Required menus: Home, MNB SOAP with Chart.js, all six OANDA Forex views/actions, and optional Test menu (four-operation calculator with JUnit and MockMvc). **No React menu.**

## Requirements
- Java **25** JDK and Maven **3.9+**. Java 21 will **not** build this application.
- An OANDA **practice** account, account ID, and v20 REST API token for Forex pages.
- Network access to MNB and OANDA endpoints at runtime, and CDN assets for Bootstrap and Chart.js.

## Run locally
```bash
export OANDA_ACCOUNT_ID="your_practice_account_id"
export OANDA_API_TOKEN="your_practice_api_token"
export APP_AUTHORS="First Last (ABC123) and Second Last (DEF456)"
mvn clean test
mvn clean package
java -jar target/homework.jar
```
Visit http://localhost:8080. **Copy `target/homework.jar` into the repository root** before committing for submission:
```bash
cp target/homework.jar ./homework.jar
```
Do not commit real tokens. Java 25 JDK compilation and actual MNB/OANDA responses must be verified on your machine. This project package has not been built here (the construction environment does not have Java 25 or Maven).

## Menu summary
| Menu | URL | Description |
|---|---|---|
| Home | `/` | Fictional company landing page |
| SOAP | `/soap` | Historical rates, HTML table, Chart.js line chart |
| Forex-account | `/forex/account` | Practice account summary |
| Forex-ActPrice | `/forex/price` | Current instrument prices |
| Forex-HistPrice | `/forex/history` | 10 price candles |
| Forex-Open | `/forex/open` | Market order; positive = buy, negative = sell |
| Forex-Pos | `/forex/positions` | Open trades table |
| Forex-Close | `/forex/close` | Close trade ID |
| Test | `/test` | Four-operation calculator |

## Submission tasks for the team
1. Replace `APP_AUTHORS` placeholders with **both students' names and Neptun codes**.
2. Use TWO contributor accounts and make **at least five incremental commits**, genuinely reflecting each student's work. Publish a **public** repository.
3. Create OANDA practice credentials and verify actual SOAP and Forex data. Never put a real API token or password in a public PDF/GitHub repo. Arrange account access for the evaluator using a secure method agreed with the lecturer.
4. With Java 25, run `mvn test` and `mvn clean package`; copy the resulting `homework.jar` to the **root** of the GitHub repository.
5. Replace report screenshot placeholders with **real screenshots of the running app and test run**, fill in real contributor assignments and GitHub URL, export PDF as `Name-NeptunCode.pdf`, and upload individually to Teams.

## Important
The assignment states grouping of two is mandatory unless teacher authorizes an exception. The application provides a real OANDA order client, so use only a practice account. Trading may lead to losses on a live account if you change the API base URL.
