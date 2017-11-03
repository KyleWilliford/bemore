# slackboat chat bot

# What is this?
This is a server for the Slackboat Slack application. This server exposes a RESTful API for the Slackboat Slack app to use to process commands. This server can also be used to authorize installations of the Slackboat Slack app using Slack's OAuth authorization APIs.

This server's api paths and methods types are listed at the end of this file.

For ease of development locally, use ngrok: https://ngrok.com/
---
This will make it possible for your Slack client to forward requests from users (you) to your local server over the internet, without having to deploy this server to a remote environment (an AWS EC2 node, for example) during development.

# Requirements
---
Java JDK 8 (development) / JRE 8 (runtime)

Maven 3.x.x

ngrok 2.8.x (development, localhost)

Not Terrible Internet Connection (duh)

# Install ngrok:
---
Go here https://ngrok.com/ and download the package for your operating system.

unzip the archive (command instructions are on the same page as the downloads)

For this project, you will expose localhost to the internet on port 8080 with a randomized domain name.

run  `$ ./ngrok http 8080`

You should see something similar to this:

![Alt text](/images/ngrok-http-8080.png?raw=true "ngrok http 8080")

More documentation on ngrok: https://ngrok.com/docs/2

You will need to configure the slack bot to send requests to the tunneled domain. Look at the ngrok output to get the public domain.

# How do I even...?
---

1. Run `mvn clean install` to build the application
1. Start the application with `java -jar target/slackboat-<version>.jar server config.yml`
1. To check that your application is running enter url `http://localhost:8080`
1. A helpful Linux/Mac alias to do this: `alias slackboat='cd <path to repo>; mvn clean install; java -jar target/slackboat-<version>.jar server config.yml'`

# Future improvements
---
Malware/phishing/etc. databases should be retrieved at server start, and periodically after that.

This server currently loads files that were retrieved between October 24th 2017 and November 2nd 2017, or thereabouts.

# Exposed REST apis
---
`POST /api/is_blacklisted` Checks a given email against a list of known bad boy disposable spam domains, maintained here: https://github.com/martenson/disposable-email-domains

`POST /api/is_phishy` Checks a given url against a list of known phishy urls, maintained here: https://www.phishtank.com/ and here https://openphish.com/phishing_feeds.html

`POST /api/is_zeus_domain` Checks a given domain against a list of known zeus trojan domains, maintained here: 
https://zeustracker.abuse.ch/blocklist.php?download=baddomains

`POST /api/is_zeus_ipv4` Checks a given ipv4 address against a list of known zeus trojan ip addresses, maintained here:
https://zeustracker.abuse.ch/blocklist.php?download=badips

`POST /api/find_any_match` Checks a given text input against all of the above databases.

