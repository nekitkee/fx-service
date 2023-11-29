## Setup Steps
0. [Download Docker Image](https://drive.google.com/drive/folders/1ZiVKxGPWhmSvUUpCCoOzkofsZtDNd0oz?usp=sharing)

1. Open terminal in folder /mintos-homework

2. Load an fx-service Docker image from a tarball
   ```shell
   docker load -i fx-service-image.tar
   
3. RUN

   ```shell
   docker-compose up

## Alternative Setup Steps

 ```shell
   docker pull ghcr.io/nekitkee/fx-service:latest
   docker-compose up
```

Java home assignment

Your task is to develop a simple RESTful web service that would satisfy a set of functional
requirements, as well as a list of non-functional requirements. Please note these non-functional
requirements are given in order of importance; items appearing earlier in the list are more crucial for
assignment.

Functional requirements:

● Service should expose an HTTP API providing the following functionality:
○ Given a client identifier, return a list of accounts (each client might have 0 or more
accounts with different currencies)
○ Given an account identifier, return transaction history (last transactions come first)
and support result paging using “offset” and “limit” parameters
○ Transfer funds between two accounts indicated by identifiers
● Balance must always be positive (>= 0).
● Currency conversion should take place when transferring funds between accounts with
different currencies
○ For currency exchange rates, you can use any service of your choice, e.g.
https://api.exchangerate.host/latest
○ You may limit the currencies supported by your implementation based on what the
currency exchange rate service supports
○ The currency of funds in the transfer operation must match the receiver's account
currency (e.g. system should return an error when requesting to transfer 30 GBP
from a USD account to a EUR account, however transferring 30 GBP from USD to
GBP is a valid operation - corresponding amount of USD is exchanged to GBP and
credited to GBP account).

Non-functional requirements:
As mentioned previously, the following list is given in order of priority, you may implement only part of
the items (more is better, however).
1. Test coverage should be not less than 80%
2. Implemented web service should be resilient to 3rd party service unavailability
3. DB schema versioning should be implemented
Evaluation criteria:
1. Feature-completeness
2. Code quality, structure
3. Test quality
4. Non-functional requirement implementation
5. Ease of setup
Please send the code (archive or link to Git Hub) and instructions on how to set it up once
completed.
