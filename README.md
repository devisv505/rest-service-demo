# rest-service-demo

RESTful API (including data model and the backing implementation)
for money transfers between accounts.

**Requirements:**

1. You can use Java, Scala or Kotlin.
2. Keep it simple and to the point (e.g. no need to implement any authentication).
3. Assume the API is invoked by multiple systems and services on behalf of end users.
4. You can use frameworks/libraries if you like (except Spring), but don't forget about requirement #2 – keep it simple and avoid heavy frameworks.
5. The datastore should run in-memory for the sake of this test.
6. The final result should be executable as a standalone program (should not require a pre-installed container/server).
7. Demonstrate with tests that the API works as expected.

**Frameworks and tools were used:**

1. For database I used H2 Database because it in-memory database, very fast, embedded and have support JDBC API.
2. Flyway for migration.
3. As a web framework I used Javalin.