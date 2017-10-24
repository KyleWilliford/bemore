# idiot bot

For ease of development locally, use ngrok: https://ngrok.com/
---
This will make it possible for Slack to forward requests from users (you) to your local server.

Expose localhost to the internet on port 8080 `ngrok http 8080`

More documentation on ngrok: https://ngrok.com/docs/2

How to start the idiot bot application server
---

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/idiotbot-<version>.jar server config.yml`
1. To check that your application is running enter url `http://localhost:8080`

Exposed REST apis
---
`POST /api/is_blacklisted` Checks a given email against a list of known bad boy disposable spam domains, curated here: https://github.com/martenson/disposable-email-domains

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`
