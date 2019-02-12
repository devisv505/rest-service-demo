# rest-transferService-demo

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

**Four layer architecture**

1. The layer of Controllers. This layer getting request and sent a response.
2. The layer of API. This layer for business logic. I use it to convert an Entity to a DAO and vice versa. Here I have some logic to call Services. One API can work with few Services.
3. The layer of Service. In this layer, I use to make some checks for an Entity before the next step. 
4. The layer of working with Database. This layer doesn't have any logic, only CRUD operation.

**API**

**Account**

_Get accounts_

This endpoint return all accounts.

```curl -X GET \
  http://localhost:7001/api/accounts \
  -H 'Postman-Token: 009e4dd3-9e29-4811-a28c-a99e6b7f8f56' \
  -H 'cache-control: no-cache'
  ```
  
Response

```
[
    {
        "id": "9ea90432-51bc-44c3-bd42-2619a70b1ae0",
        "balance": 1000,
        "currency": "EUR",
        "description": "test 1"
    }
]
```

_Get account_ 

This endpoint return certain account

```
curl -X GET \
  http://localhost:7001/api/accounts/9ea90432-51bc-44c3-bd42-2619a70b1ae0 \
  -H 'Postman-Token: 772ecd5d-6a3b-4f61-bcb5-fbb9e5b5fbb7' \
  -H 'cache-control: no-cache'
  ```
  
Response

```
{
    "id": "9ea90432-51bc-44c3-bd42-2619a70b1ae0",
    "balance": 1000,
    "currency": "EUR",
    "description": "test 1"
}
```

**Transfer**

_Get transfers_

This endpoint return all transfers.

```
curl -X GET \
  http://localhost:7001/api/transfers \
  -H 'Postman-Token: 4fb2bfd5-fe0f-4df4-92d5-a5ccae09bb83' \
  -H 'cache-control: no-cache'
  ```

Response

```
[
    {
        "id": "7f5f290b-c515-4c0a-8fc3-d1bf720c62b3",
        "state": "COMPLETED",
        "created_at": "2019-02-12 15:52:22.277",
        "completed_at": "2019-02-12 15:52:22.282"
    }
]
```

_Get transfer_

This endpoint return certain transfer

```
curl -X GET \
  http://localhost:7001/api/transfers/7f5f290b-c515-4c0a-8fc3-d1bf720c62b3 \
  -H 'Postman-Token: e69beedc-cb3f-4205-a615-2ba6394dd93e' \
  -H 'cache-control: no-cache'
  ```
  
Response

```
{
    "id": "7f5f290b-c515-4c0a-8fc3-d1bf720c62b3",
    "state": "COMPLETED",
    "created_at": "2019-02-12 15:52:22.277",
    "completed_at": "2019-02-12 15:52:22.282"
}
```

_Post transfer_

This endpoint create transfer 

```
curl -X POST \
  http://localhost:7001/api/transfers \
  -H 'Content-Type: application/json' \
  -H 'Postman-Token: 70253439-c0b8-4d75-a028-ab0ca8e436fe' \
  -H 'cache-control: no-cache' \
  -d '{
    "request_id": "e0cbf84637264ee082a848b",
    "source_account_id": "9ea90432-51bc-44c3-bd42-2619a70b1ae0",
    "target_account_id": "6d61f3e6-f850-4948-9d17-087517e098a4",
    "amount": 100.0,
    "currency": "EUR",
    "description": "Expenses funding"
}'
```

request_id - the unique id used to handle duplicates submitted (String)

source_account_id - the id account source (UUID)

target_account_id - the id account target (UUID)

amount - the transaction amount

currency - the transaction currency

description - the transaction description 

Response 

```
{
    "id": "054c6a13-2f55-4baa-9378-a5afae503b24",
    "state": "COMPLETED",
    "created_at": "2019-02-12 15:55:51.575",
    "completed_at": "2019-02-12 15:55:51.577"
}
```
