Feature:Test Restful API For HerokuApp

  Background: url service
    * url 'https://restful-booker.herokuapp.com/'

  @getToken
  Scenario: Get all the bookings
    #* def credentials = read("classpath:resources/credentials.json")
    Given path 'auth'
    Given header Accept = 'application/json'
    And request
      """
      {
        username: "admin",
        password: "password123",
      }
      """
    When method POST
    Then status 200
    * def Token = response.token
    And match response.token == "#present", "#string", "#notnull"
    * print Token