# Assignment 
You are required to create a simple RESTful software service that will allow a merchant to create a new simple offer.

## Description
All my offers have shopper friendly descriptions. I price all my offers up front in a defined currency.

## Todo
- GET /order/{id} API and relative actor 

## Assumptions
- json: the assignment isn't explicit about it but I think it brings value
- optional: adding a merchant id when storing the offer in such a way that you could retrieve only the offer from one particular merchant
- backend-only: I could have hacked up an ugly html page with some required form and one button to POST/GET the request

### Note/Disclaimer
If it was a user story to develop in a normal context, I'd have tried to get some doubts relinquished.
For example, I'd ask to understand in which way the customer would want to interact with the service:
I already implemented the _"create an offer"_ and an additional _"retrieve all offers that have been created on the system"_. 
So I think that adding a _"retrieve a specific offer"_ could bring value but I'd want to know if has business value.
In this case also I don't want to even minimally over-engineer the solution.
