# idiot bot

For ease of development locally, use ngrok: https://ngrok.com/
---
This will make it possible for Slack to forward requests from users (you) to your local server.

Expose localhost to the internet on port 8080 `ngrok http 8080`

More documentation on ngrok: https://ngrok.com/docs/2

You will need to configure the slack bot to send requests to the exposed domain. Look at the ngrok output to get the public domain.

---
For a "real" version of this slack app, a live list of bad domains/urls/etc. should be retrieved at server start, and periodically after that. This application currently loads files that were retrieved on October 24th, or thereabouts.

How to start the idiot bot application server
---

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/idiotbot-<version>.jar server config.yml`
1. To check that your application is running enter url `http://localhost:8080`

Exposed REST apis
---
`POST /api/is_blacklisted` Checks a given email against a list of known bad boy disposable spam domains, maintained here: https://github.com/martenson/disposable-email-domains
`POST /api/is_phishy` Checks a given url against a list of known phishy urls, maintained here: https://www.phishtank.com/

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`
