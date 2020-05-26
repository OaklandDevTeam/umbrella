# RequestParamHelper

RequestParamHelper is a utility class to easily extract parameters out of Spark POST requests. The main motivation for this class is when a JSON object is sent as a body of parameters in a POST request. This class allows you to easily retrieve parameters in a key/value fashion. So instead of having to deserialize JSON every time you want to check if a parameter was set or get its value, you don't have to. This class does that for you.

**ALL VALUES ARE RETURNED AS A STRING.** So if you are expecting an integer, you will have to use something like `Integer.parseInt()` to get the integer value.

**NOTE:** This class does not support JSON values that are other objects or arrays.

# Usage
Method | Description | Returns
:--- | :---: | ---:
RequestParamHelper(Request) | **Constructor**. This requires the request object as a parameter | RequestParamHelper
hasKey(String) | This returns if the parameter key was sent in the request or not | true/false
valueOf(String) | Returns string value of parameter key | String/null

## Example

If a POST request was sent to the endpoint, with a JSON body that looks like this

```json
{
    "username":"someusername",
    "email":"somewhere@somewhere.net",
    "age":20
}
```
This is how you can use this class to get the "username" attribute from the JSON object.
```java
public static Route someRoute = ((request, response) -> {
    RequestParamHelper params = new RequestParamHelper(request);
    String username;

    if (!params.hasKey("username")) {
        // return some error
    }

    username = params.valueOf("username");
    
    // .....
    // do something with username
    
});
```