## Customizing RestTemplate for Spring Config Clients

#### Why need it:

---

In case of basic authentication, we can define the properties to modify the **ConfigClientProperties.class**.

Ex:

bootstrap.yml
```yaml
spring:
    config:
      name: application-name
      uri: http://{username}:{password}@host:port
``` 

or

```yaml
spring:
    config:
      name: application-name
      uri: http://host:port
      username: username
      password: password
```

In case of a different authentication mechanism, it might require to add a Negotiate/Bearer/Any other authentication 
mechanism token as Authorization header to the request for your client to interact with the config server.

This is a demo application for a client where we, customize the RestTemplate to add an Authorization token
(used Basic Authentication mechanism for example), when the Client bootstraps and wants to connect to the
configuration server.

The Customized RestTemplate is then injected into ConfigServicePropertySourceLocator.

Please check the below [reference](https://github.com/spring-cloud/spring-cloud-config/pull/523/files?short_path=fdb3cac#diff-fdb3cac0c704c591b3e79fea3196d801)