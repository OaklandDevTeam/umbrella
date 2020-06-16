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

### GET `/user/info`
> Returns JSON
```json
{
  "username": "<currentUsername>",
  "registration_date": 1591161563968, // note time is in seconds
  "post_count": 0
}
```

### POST `/user/banning`
Bans a user. Supplied "user" can be a userId or username
> Accepts JSON
```json
{
    "user":"<username/userId>"
}
```
### DELETE `/user/banning`
un-bans a user. Supplied "user" can be a userId or username
> Accepts JSON
```json
{
    "user":"<username/userId>"
}
```

> Returns JSON

Returns 403 when unauthorized, 400 when user parameter missing, 400 when the ban attempt fails and 200 otherwise
```json
{
    "status":123,
    "message":"success/fail"
}
```

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

### POST `/drops/delete`
> Accepts JSON
```json
{
  "drop_id":"<dropId>"
}
```
> Returns JSON
```json
{
    "status":number,
    "message":"success/fail message"
}
```

### PUT `/drops/update`
> Accepts JSON
```json
{
  "drop_id":"<dropId>",
  "topic":"<new drop topic>"
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

### `PUT /posts/modify`
> Accepts JSON
```json
{
	"post_id":"<idString>",
	"body":"modified post body text"
	
}
```
### `DELETE /posts/delete`
> Accepts JSON
```json
{
	"post_id":"<idString>"
}
```

# General Response
```json
{
    "status":number,
    "message":"String"
}
```