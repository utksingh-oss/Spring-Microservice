## Before Microservice

- Monolithic Architecture: multiple components are combined in single large app
- Single Code base
- Deployed as a single bundle
- Change in one service, then whole app in redeployed
- Building Problem: developers have to communicate the changes for seamless integration
- Problem in scale
- Cumbersome over time

*****

## Microservices

### Advantages

- Large apps are divided into small parts
- Commnuicate using APIs (REST, gRPC etc.) or Brokers
- Different codebases (multiple languages: python, js, java etc.)
- Each module managed independently
- Different tech stacks

### Disadvantages

- Handling microservice is complex
- For smaller team, it is not recommended
- If the user base is increasing then switching to Microservices is recommended

*** 

## Feign Client

- The feign is a declarative HTTP Web Client developed by **Netflix**
- Declarative approach
- Alternative to *Http Request Rest Template*
- If you want to use Feign, create an interface, and annotate it

## API Gateway
- The the request is routed through the gateway
- it is also a service (micro-service)

### Spring Cloud Gateway
- Requires Netty runtime provided by Spring Boot and Spring Webflux.
- It does not work on traditional Servlet Container or when built on a WAR

## Config Server
- All the configuration in the projects were stored locally
- Config store will provide client-server architecture, local configuration will be externalized
- All the configuration will be get from git from the config-server and then it will be given to application
- URL which returns the properties: http://utkarsh:8085/application/default
- Externalizing the common configuration

## Fault Tolerance
- After certain amount of retries if the result is not fetched then circuit is opened
- After timeframe if some of the requests succeed then then the circuit is closed
- Library: Hystrix, Resilience4j

## Retry Mechanism
- Retrying the API calls a few times if a request fails on the first time

## Rate Limiter
- The functionality allows limited access to some service
- Rate Limiter make services highly available by limiting the number of calls we could process in specific window
- Can prevent DDoS Attacks
- Three ways to limit call:
	- Request per second (RPS)
	- Request per minute (RPM)
	- Request per hour	 (RPH)

 
## Microservice Security Using JWT
- A centralized server for security
- Each request will be validated by this service
- In order to make sure the services are secured we need to ensure that direct access to these services is not permitted.
