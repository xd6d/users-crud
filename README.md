## Deployment

Just run ``docker compose up -d`` and Docker will set up database and web app on port 8080

## Example API requests and responses

#### Create user

```
POST localhost:8080/api/v1/users

Content-Type: application/json

{
    "email": "example@mail.com",
    "firstName": "David",
    "lastName": "Green",
    "birthDate": "2002-04-24",
    "address": "Lviv, Kyivska street, 3",
    "phoneNumber": "+380660000001"
}
```

Expected Response:

```
201 Created

Headers:
    Location=http://localhost:8080/api/v1/users/1
```

---

#### Get user by id
```
GET localhost:8080/api/v1/users/1
```

Expected Response:

```
200 OK

{
    "id": 1,
    "email": "example@mail.com",
    "firstName": "David",
    "lastName": "Green",
    "birthDate": "2002-04-24",
    "address": "Lviv, Kyivska street, 3",
    "phoneNumber": "+380660000001"
}
```

---

#### Get users

Get all users with default 'from' and 'to' dates and default pagination settings i.e. from=0000-01-01, to=2100-01-01, page=0, size=20.
```
GET localhost:8080/api/v1/users
```
is the same as
```
GET localhost:8080/api/v1/users?from=0000-01-01&to=2100-01-01&page=0&size=20
```

Also sorting could be applied, f.e:

Sort by 'id' with descending order
```
GET localhost:8080/api/v1/users?sort=id,desc
```
or sort by 'lastName' and then sort by 'firstName'
```
GET localhost:8080/api/v1/users?sort=lastName&sort=firstName
```

Expected Response:

```
200 OK

[
    <<Users>>
]
```

---

#### Partial user update
```
PATCH localhost:8080/api/v1/users/1

Content-Type: application/json-patch+json

[
    {"op": "replace", "path": "/email", "value": "partial@update.io"},
    {"op": "replace", "path": "/firstName", "value": "Diana"}
]
```

Expected Response:

```
200 OK

{
    "id": 1,
    "email": "partial@update.io",
    "firstName": "Diana",
    "lastName": "Green",
    "birthDate": "2002-04-24",
    "address": "Lviv, Kyivska street, 3",
    "phoneNumber": "+380660000001"
}
```

---

#### Full user update
```
PATCH localhost:8080/api/v1/users/1

Content-Type: application/json-patch+json

[
    {"op": "replace", "path": "/email", "value": "full@update.io"},
    {"op": "replace", "path": "/firstName", "value": "Daniel"},
    {"op": "replace", "path": "/lastName", "value": "Yellow"},
    {"op": "replace", "path": "/birthDate", "value": "1990-12-21"},
    {"op": "replace", "path": "/address", "value": "City, Street, 1"},
    {"op": "remove", "path": "/phoneNumber"}
]
```

Expected Response:

```
200 OK

{
    "id": 1,
    "email": "full@update.io",
    "firstName": "Daniel",
    "lastName": "Yellow",
    "birthDate": "1990-12-21",
    "address": "City, Street, 1",
    "phoneNumber": null
}
```

---

#### Delete user
```
DELETE localhost:8080/api/v1/users/1
```

Expected Response:

```
204 No Content
```

---
