# BookingFlightService
API-only application that would aggregate and return list of returning flights from different providers.

# Tech Stack
```
- Spring-boot 2.2.3.RELEASE
- Java 8
- Gradle 5.1.1
- Swagger 2.7.0
```
# Installation
```
- gradle clean build
- gradle bootRun (to compile and run the service directly)
```
# Run
```
- After running the above command build folder will be created just traverse to build/libs where you will find the bookflight-0.0.1-SNAPSHOT.jar.
- Run the application using the given command (java -jar bookflight-0.0.1-SNAPSHOT.jar)
```
# Swagger-url
```
- Integrated swagger in order to get all API details like
  - Parameters that are needed in request.
  - Default values in case blank values are passed.
  - Response payload and user friendly messages.

http://localhost:8080/swagger-ui.html
```
# Actuator
```
- Integrated actuator in order to check different status for bookingService like healthcheck,Memory matrix etc.
http://localhost:8080/actuator/
http://localhost:8080/actuator/health  - To check if service has started and running
```

# Application url
```
curl -X GET --header 'Accept: application/json' 'http://localhost:8080/api/aggregation?flightType=all&page=1&itemsPerPage=15'

Request url
http://localhost:8080/api/aggregation?flightType=all&page=1&itemsPerPage=15

```
# Testing Coverage
```
                                        class %     Method %     Line %
com.aggregators.bookflight.controller   100%(1/1)   100%(1/1)    100%(14/14)
com.aggregators.bookflight.service      100%(1/1)   100%(5/5)     85%(52/61)
```
