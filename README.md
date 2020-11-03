<!-- TABLE OF CONTENTS -->
## Table of Contents

* [About the Project](#about-the-project)
* [Getting Started](#getting-started)
  * [Prerequisites](#prerequisites)
  * [Setup](#setup)
* [Usage](#usage)
* [Contact](#contact)



<!-- ABOUT THE PROJECT -->
## About The Project

Reservation service based on the requirements given from Parsley Health for a take home challenge problem.

Although the spec referred only to the concept of a "time slot", I center this service around the concept of a "reservation" instead. 
Endpoints which save, check, delete and retrieve "time slots" seemed kind of awkward to me, so instead I am doing all of this with
reservations. 
The storage model uses start/end dates as this makes more sense for querying. 
I kept the idea of a time slot through a "TimeSlotDto". This both lives in the user facing "ReservationDto" as well being directly used for some operations.

Plenty could be done to expand on this service to flesh it out.
* Better error messages across the board for all possible exceptions.
* Strip seconds from DTO dates OR throw exception when seconds used (to keep things simple)
* Reformat service to include concept of users which contain reservations (one to many in database). Reservations don't really make much sense outside the context of a specific person.
* Add some sort of authentication (probably some endpoint to retrieve token for users)
* Add paging to GET methods
* Add new endpoint for retrieving overlapping reservations (to make it easier to delete when there is a conflict)
* Flesh out findAll method with query parameters in order to filter results
* Possibly add actual dummy database to some service tests to test out query functionality.
* Generate swagger documentation for endpoints


<!-- GETTING STARTED -->
## Getting Started

This is an example of how you may give instructions on setting up your project locally.
To get a local copy up and running follow these simple example steps.

### Prerequisites

Docker (or MySQL 8+)
Gradle
Java 8+

### Setup
 
Preferred
1. A. From project directly run `docker-compose -f ./docker/docker-compose.yml up -d`

Not Preferred
1. B. Setup MySQL database 'parsley_health' with user 'user' and password 'password' and default port

2. Run command `gradlew run`

<!-- USAGE EXAMPLES -->
## Usage

https://documenter.getpostman.com/view/13227665/TVYM5bhW

Or import postman collection located in project directory:
Reservations.postman_collection.json
<!-- CONTACT -->
## Contact

Stefan Kurek - stefan.kurek@gmail.com

Project Link: [https://github.com/NerdCoreRocks/parsley-health-reservations-service](https://github.com/NerdCoreRocks/parsley-health-reservations-service)