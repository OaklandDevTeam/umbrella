# User endpoints
### POST `/login`
> Accepts JSON
```json
{
    "username":"String",
    "password":"String",
}
```
> Returns JSON
```json
{
    "status":number,
    "message":"<userId>"
}
```

### POST `/register`
> Accepts JSON
```json
{
    "username":"String",
    "password":"String",
    "email":"String"
}
```
> Returns JSON
```json
{
    "status":number,
    "message":"<username>"
}
```
### GET `/logout`
> Returns JSON
```json
{
    "status":number,
    "message":"success/fail message"
}
```

* POST `/user/subscribe`
* GET `/user/subscribed`
* POST `/user/unsubscribe`

# Drops
### POST `/drops/create`
> Accepts JSON
```json
{
    "owner":"<userId>",
    "title":"String",
    "topic":"String"
}
```
> Returns JSON
```json
{
    "status":number,
    "message":"success/fail message"
}
```

### POST `/drops/managemod`
> Accepts JSON
```json
{
    "username":"String", // Username being set as moderator
    "user_id":"<userId>", // UserId being set as moderator
    "drop_id":"<dropId>" // The drop to apply this change to
}
```
> Returns JSON
```json
{
    "status":number,
    "message":"success/fail"
}
```
### DELETE `/drops/managemod`
> Accepts JSON
```json
{
    "drop_id":"<dropId>", // The drop to apply this change to
    "user_id":"<userId>", // The userId being removed
}
```
> Returns JSON
```json
{
    "status":number,
    "message":"success/fail"
}
```

# Drop Subscriptions
### POST `/user/subscribe`
> Accepts JSON
```json
{
    "drop_id":"<dropId>",
}
```
> Returns JSON
```json
{
    "status":number,
    "message":"subscribed/error"
}
```
### POST `/user/unsubscribe`
> Accepts JSON
```json
{
    "drop_id":"<dropId>",
}
```
> Returns JSON
```json
{
    "userId":number,
    "dropId":"unsubscribed/error",
}
```
### GET `/user/subscribed`
> Returns JSON
```json
[
    {
        "userid":"<userId>",
        "dropid":"<dropid>"
    },
]
```

# General Response
```json
{
    "status":number,
    "message":"String"
}
```