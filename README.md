# slackboat chat bot

# What is this?
This is a server for a Slack chat bot. This server processes commands at specific RESTful paths. The paths and methods are listed below.

For ease of development locally, use ngrok: https://ngrok.com/
---
This will make it possible for your Slack client to forward requests from users (you) to your local server over the internet, without having to deploy this server to a remote environment (an AWS EC2 node, for example) during development.

Install ngrok:
---
Go here https://ngrok.com/ and download the package for your operating system.
unzip the archive (instructions are on the same page)
For this project, you will expose localhost to the internet on port 8080 with a randomized domain name.
run  `$ ./ngrok http 8080`
You should see something similar to this:
![Alt text](/images/ngrok-http-8080.png?raw=true "ngrok http 8080")

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
