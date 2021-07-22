# Welcome to Billing System of JTC limited

Jarvis tries to run a telecommunications company (JTC limited) in order to make money. In the first piece, JTC released its basic functionalities including phone call and SMS service.

Now, JTC wants to set up a billing system to charge customers who subscribed to the service.

## Story Wall

the development team use a story wall or Kanban board to keep track of features or "stories" as they are
worked on.

The wall you will be working from today has 4 columns:

- To Do
- Doing
- Showcase
- Done

Examples can be found
here [https://trello.com/b/s4fb5ZG1/jarvis-homework](https://trello.com/b/s4fb5ZG1/jarvis-homework)
## Tech
- Java 1.8 or higher (Iterator 1) / Scala 2.13 (Iterator 2)
- PostgreSQL
- Docker

## Requirements

- JTC provides 3 kinds of packages: Starter, Standard, Premier. Each package contains some reserved usage, which means customers won’t be extra charged as long as their usage doesn’t exceed the limitation.

- Starter - Subscription fee is $38, with 10 minutes phone call and 10 SMS.
Standard - Subscription fee is $58, with 30 minutes phone call and 40 SMS.
Premier - Subscription fee is $188, with 300 minutes phone call and 200 SMS.

- Once customers’ usage exceeds their package limitation, an extra fee is there.
Extra phone call fee: $1/minutes.
Extra SMS fee: $0.5/SMS.

- Customers will be billed immediately once they subscribed to the package, the next bill date is the same date in next month.
First bill should only contain the subscription fee.
Following bill should be composed of the next package fee and extra usage fees.


## Useful Gradle commands

The project makes use of Gradle and uses the Gradle wrapper to help you out carrying some common tasks such as building
the project or running it.

### List all Gradle tasks

List all the tasks that Gradle can do, such as `build` and `test`.

```console
$ ./gradlew tasks
```

### Build the project

Compiles the project, runs the test and then creates an executable JAR file

```console
$ ./gradlew build
```

Run the application using Java and the executable JAR file produced by the Gradle `build` task. The application will be
listening to port `8080`.

```console
$ java -jar build/libs/jtc-bill-java.jar
```

### Run the tests

There are two types of tests, the unit tests and the functional tests. These can be executed as follows.

- Run unit tests only

  ```console
  $ ./gradlew test
  ```

- Run functional tests only

  ```console
  $ ./gradlew functionalTest
  ```

- Run both unit and functional tests

  ```console
  $ ./gradlew check
  ```

### Run the application

Run the application which will be listening on port `8080`.

```console
$ ./gradlew bootRun
```

## API

Below is a list of API endpoints with their respective input and output. Please note that the application needs to be
running for the following endpoints to work. For more information about how to run the application, please refer
to [run the application](#run-the-application) section above.

### list packages

Endpoint

```text
POST /package/listPackages
```

Example of body

```json
[
  {
    "packageId": <packageId>,
    "name": <name>,
    "subscriptionFee": <subscriptionFee>,
    "phoneLimit": <phoneLimit>,
    "smsLimit": <smsLimit>,
    "extraPhoneFee": <extraPhoneFee>,
    "extraSMSFee": <extraSMSFee>
  }
]
```

Parameters

| Parameter      | Description                                           |
| -------------- | ----------------------------------------------------- |
| `packageId` | One of the package' id listed above              |
| `name`         | The name of the package |
| `subscriptionFee`  | The subscribe fee of package  |
| `phoneLimit`  | The phone duration in the package  |
| `smsLimit`  | The SMS duration in the package  |
| `extraPhoneFee`  | Extra phone call fee  |
| `extraSMSFee`  | Extra SMS fee  |

Example package

|                packageId             | name     | subscriptionFee | phoneLimit | smsLimit | extraPhoneFee | extraSMSFee |
| ------------------------------------ | -------: | --------------: |----------: |--------: |-------------: |-----------: |
| 39a62e4e-07d4-4940-a60b-c44aafe00dad | Starter  |      38         |     10     |     10   |    1          |     0.5     | 



The following POST request, is an example request using CURL, subscript the package for the customer.

```console
curl --location --request POST 'localhost:8080/billing/subscriptPackage/39a62e4e-07d4-4940-a60b-c44aafe00dad' \
--header 'customerId: 10015'
```

The above command will subscript the package.

### Use phone 

Endpoint

```text
curl --location --request POST 'localhost:8080/billing/usedPhone' \
--header 'customerId: 10015' \
--header 'Content-Type: application/json' \
--data-raw '{
    "phoneUsed":10
}'
```

Parameters

| Parameter      | Description                              |
| -------------- | ---------------------------------------- |
| `customerId` | the id of customer |
| `phoneUsed` | the phone num of call |

### Used SMS

Endpoint

```text
curl --location --request POST 'localhost:8080/billing/usedSMS' \
--header 'customerId: 10015' \
--header 'Content-Type: application/json' \
--data-raw '{
    "smsUsed":10
}'
```

Parameters

| Parameter      | Description                              |
| -------------- | ---------------------------------------- |
| `customerId` | the id of customer |
| `smsUsed` | the phone num of sms |


### Get my bill at any time

Endpoint

```text
curl --location --request GET 'localhost:8080/billing/getInvoiceAnyTime' \
--header 'customerId: 10015'
```

Parameters

| Parameter      | Description                              |
| -------------- | ---------------------------------------- |
| `customerId` | the id of customer |

Example output

```json
{
  "pay": 38
}
```

### Get my bill once billing run happened

Endpoint

```text
curl --location --request GET 'localhost:8080/invoice/active' \
--header 'customerId: 10015'
```

Parameters

| Parameter      | Description                              |
| -------------- | ---------------------------------------- |
| `customerId` | the id of customer |

Example output

```json
[
  {
    "invoiceId": "3bbd8b32-e15e-41eb-9b23-fbb06b5a1a4c",
    "customerId": "10015",
    "pay": 38,
    "status": "active",
    "createTime": "2021-07-21",
    "lastUpdateTime": "2021-07-21"
  }
]
```

### Generate bill for all customers

Endpoint

```text
curl --location --request POST 'localhost:8080/billing/execute'
```

###  Pay my bill,

Endpoint

```text
curl --location --request POST 'localhost:8080/invoice/paid' \
--header 'customerId: 10015' \
--header 'Content-Type: application/json' \
--data-raw '{
    "invoiceId":"1647931b-571e-45b2-a3d6-36bb4d01bc2c",
    "pay":63
}'
```

Parameters

| Parameter      | Description                              |
| -------------- | ---------------------------------------- |
| `customerId` | the id of customer |
| `invoiceId` | the ID num of invoice |
| `pay` | the money need to pay |


# Running the app

- ''Run docker-compose build''
- ''Run docker-compose up''



