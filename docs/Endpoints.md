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

### GET `/drops/list`
> Returns JSON
```json
[
  {
    "drop_id": "<dropId>",
    "title": "<Title>",
    "topic":"<drop title>"
  },
]
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

# Posts
### GET `/posts/<droptitle>/list`

| Query Param |                                                                                         Description                                                                                         |
| :---------- | :-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------: |
| limit       |                                     Maximum number of posts to fetch in this call. ***OPTIONAL:*** there is a default limit of 25 and a maximum of 100                                      |
| startAfter  | The Post ID that this call will start after. **NOTE** This post will not come in the listing. ***OPTIONAL:*** If this is not specified, an initial list is retrieved starting from the top. |

Examples: 
* `/posts/<droptitle>/list`
* `/posts/<droptitle>/list?limit=50`
* `/posts/<droptitle>/list?startAfter=5ed668f949c2aa25778c8080`
* `/posts/<droptitle>/list?startAfter=5ed668f949c2aa25778c8080&limit=50`

> Returns JSON
```json
{
    "drop_id":"<dropId>",
    "count":0000,
    "last_id":"<id of last post in listing>",
    "posts": {
        // Post models
    }
}
```

### `POST /posts/create`
> Accepts JSON
```json
{
	"title":"post title",
	"body":"post body text",
	"drop_id":"<dropid>"
}
```

# General Response
```json
{
    "status":number,
    "message":"String"
}
```