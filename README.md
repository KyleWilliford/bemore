# slackboat app for Slack

## Wait, what is this?

This is the server for the slackboat app for Slack (https://kpw-slack-dev.slack.com/apps/A7Q387WP9-slackboat). What's slackboat? Never heard of it? It is a custom Slack application that adds a handful of slash commands to your Slack workspace.

I created this in order to learn how to build and distribute a Slack app. Primarily what this app _does_ is that it allows you to check urls and domains against some public databases of known phishing, malware, spam, or trojan domain names, URLs, or ip (v4) addresses, depending on the available data in each database. The public databases imported into the server are listed later in this document.

The slackboat _app_ (https://kpw-slack-dev.slack.com/apps/A7Q387WP9-slackboat) adds a handful of slash commands to your workspace. Read the next few sections for more detail on what is added and how to use these commands.

The slackboat _server_ (this repo's code) processes commands from users who have installed the _app_ into their workspace. The _server_ also implements a method for authorizing installation of the app into your workspace via the handy button located on this page (see below).

This server's api paths and method types are listed near the end of this readme.

### Why did you name this "slackboat"

The name mildly amused me.

## Install _slackboat App_ to Your Workspace (live version)

Click the following button to sail the slackboat into your workspace.

<a href="https://slack.com/oauth/authorize?scope=commands&client_id=261022332754.262110268791"><img alt="Add to Slack" height="40" width="139" src="https://platform.slack-edge.com/img/add_to_slack.png" srcset="https://platform.slack-edge.com/img/add_to_slack.png 1x, https://platform.slack-edge.com/img/add_to_slack@2x.png 2x" /></a>

Note: this installation request communicates with a live version of this slackboat _server_, deployed on an AWS EC2 instance. If this server doesn't response, that means it is off, and probably won't be turned on again. You will have to run your own instance of the server and configure the Slack app to communicate with your server.

## I installed the app. Now what?

Open up your slack client and try out the new commands!!!!1!11!

These are the new slash commands you can use in Slack:
`/is_spam_domain [email, or domain]` Checks a given email/domain string against a list of known disposable spam domains, maintained here: https://github.com/martenson/disposable-email-domains. The domain is the only relevant piece - anything before the `@` is stripped out.

`/is_in_phishtank [url]` Checks a given url against a list of known phishy urls, maintained here: https://www.phishtank.com/

`/is_in_openphish [url]` Checks a given url against a list of known phishy urls, maintained here: https://openphish.com/phishing_feeds.html

`/is_zeus_domain [domain]` Checks a given domain against a list of known ZeuS trojan domains, maintained here: 
https://zeustracker.abuse.ch/blocklist.php?download=baddomains

`/is_zeus_ipv4 [ipv4]` Checks a given ipv4 address against a list of known ZeuS trojan ip addresses, maintained here:
https://zeustracker.abuse.ch/blocklist.php?download=badips

`/find_any_match [text]` Checks a given text input against all of the above databases.

![Alt text](/images/find_any_match.png?raw=true "find any match")

An exact match (case insensitive) is required to find a result from one of these databases. How useful is that? Ney very, I know. This could be enhanced later.



# Development, running this server locally, tunneling, and deployment

This section is for building and running this server locally. This will be necessary when I inevitably turn off the server.

For ease of development locally, use ngrok: https://ngrok.com/
---
This will make it possible for your Slack client to send requests from your client to your local server over the public internet. The point of this is that you will not have to deploy this server to a remote environment (an AWS EC2 node, for example) every time you want to test a change during development.

## Development Requirements

- Internet

- A Slack client

- Java JDK 8 (compile) / JRE 8 (runtime)

- Maven 3.x (https://maven.apache.org/install.html or use a package manager like brew, apt, etc.)

- ngrok 2.x (development, localhost)

## How do I start the server?

1. Install dependencies (listed above)
1. `$ git clone https://github.com/KyleWilliford/slackboat.git` Clone the repo 
1. `$ cd slackboat` Change directory to repo 
1. `$ mvn clean install` to build the application
1. `$ java -jar target/slackboat-<version>.jar server config.yml` Start the application (check the current version in the pom.xml file in the project root directory)
1. A helpful Linux/Mac alias to do this: `alias slackboat='cd <path to repo>; mvn clean install; java -jar target/slackboat-<version>.jar server config.yml'`

## How do I configure the Slack app part of this project to change slash command urls?

You may need to create your own slack app and point it to your instance of this server.

Create a Slack app here: https://api.slack.com/slack-apps

Then, set up the app however you want. Add Slash commands that point to the REST endpoints listed below.

You will need to update the Slack tokens stored in the yaml configuration of this server, if you decide to connect it to your own app.

## Installing and using ngrok

Go here https://ngrok.com/ and download the package for your operating system.

unzip the archive (command instructions are on the same page as the downloads)

For this project, you will tunnel HTTP/S internet requests on port 8080 with a randomized domain name.

run  `$ ./ngrok http 8080` to set up the tunnel

More documentation on ngrok: https://ngrok.com/docs/2

You will need to configure the slack app to send requests to the tunneled domain, if you are using your own Slack app. Look at the ngrok output to get the domain for this. The ngrok website has examples.

### URLs

Client OAuth Redirect URL:

`<server url>/auth`

Slash Command URL:

`<server url>/api/<command path>`

## Server REST API

### Slash Command API
The path names are designed to match the slash commands, for readability.

All paths consume `application/x-www-form-urlencoded` media type and product `text/plain` media type.

All paths check and use the `text`, `token`, and `ssl_check` form parameters that Slack _may_ send with any request.

HTTP POST `/is_spam_domain` Checks a given email/domain string against a list of known disposable spam domains, maintained here: https://github.com/martenson/disposable-email-domains. The domain is the only relevant piece - anything before the `@` is stripped out.

HTTP POST `/is_in_phishtank` Checks a given url against a list of known phishy urls, maintained here: https://www.phishtank.com/

HTTP POST `/is_in_openphish` Checks a given url against a list of known phishy urls, maintained here: https://openphish.com/phishing_feeds.html

HTTP POST `is_zeus_domain` Checks a given domain against a list of known ZeuS trojan domains, maintained here: 
https://zeustracker.abuse.ch/blocklist.php?download=baddomains

HTTP POST `/is_zeus_ipv4` Checks a given ipv4 address against a list of known ZeuS trojan ip addresses, maintained here:
https://zeustracker.abuse.ch/blocklist.php?download=badips

HTTP POST `/find_any_match` Checks a given text input against all of the above databases.

# Future Improvements and TODOs

- The relevant databases should be retrieved at server start, and periodically after that. This server currently loads files that were retrieved between October 24th 2017 and November 2nd 2017, or thereabouts, so the data is not current.
- Add more useful or interesting app content / functions
- Enable HTTPS
- Encrypt client secret token / other tokens

# Travis CI

https://travis-ci.org/KyleWilliford/slackboat

