Feature:Test Restful API For HerokuApp

  Background: url service
    * url 'https://restful-booker.herokuapp.com/'
    * def authToken = call read('classpath:features/AuthToken.feature@getToken')

  @Get
  Scenario: Get all the bookings
    Given path 'booking'
    Given header Accept = 'application/json'
    When method GET
    Then status 200
    And print 'Response: ', response

  @getbookingid
  Scenario Outline: Get details of specific booking
    Given path 'booking',bookingId
    Given header Accept = 'application/json'
    When method GET
    Then status 200
    And match response ==
    """
    {
      "firstname": '#string',
      "lastname": '#string',
      "totalprice": '#number',
      "depositpaid": '#boolean',
      "bookingdates": {
        "checkin": '#string',
        "checkout": '#string'
      },
      "additionalneeds": '#string'
    }
  """

    Examples:
      | bookingId|
      |    33    |
      |   187    |

 @post
Scenario: Create a booking
  Given path 'booking'
  Given header Accept = 'application/json'
   And def body =
   """{

   "firstname" : "Jim",
   "lastname" : "Brown",
   "totalprice" : 111,
   "depositpaid" : true,
   "bookingdates" : {
   "checkin" : "2018-01-01",
   "checkout" : "2019-01-01"
   },
   "additionalneeds" : "Breakfast"
   }
"""
   And request body
  When method  POST
  Then status 200
  And match response ==
  """
  {
    "bookingid": '#number',
    "booking": {
      "firstname": '#string',
      "lastname": '#string',
      "totalprice": '#number',
      "depositpaid": '#boolean',
      "bookingdates": {
        "checkin": '#string',
        "checkout": '#string'
      },
      "additionalneeds": '#string'
    }
  }
  """
   And match response.bookingid != null

@put
  Scenario Outline: Update Booking
    Given path 'booking',bookingId
  * header Accept = 'application/json'
  * header Cookie = 'token=' + authToken.Token
  And print authToken.accessToken
    And def body =
      """{

        "firstname" : "Jim",
        "lastname" : "Brown",
        "totalprice" : 111,
        "depositpaid" : true,
        "bookingdates" : {
          "checkin" : "2018-01-01",
          "checkout" : "2019-01-01"
        },
        "additionalneeds" : "Breakfast"
      }
      """
    And request body
    When method  PUT
    Then status 200
  And print 'Response: ', response
Examples:
        | bookingId|
        |    33    |


@patch
Scenario: Pratial Update Booking
  Given path 'booking/33'
  * header Accept = 'application/json'
  And def body =
    """{

      "firstname" : "Jolly",
      "lastname" : "Dean",
    }
    """
  And request body
  When method PATCH
  Then status 200
  And match response.firstname == "#present" , "#string", "#notnull"
  And match response.lastname == "#present", "#string", "#notnull"
  And print 'Response: ', response

  @delete
Scenario: Delete booking
   Given path 'booking/187'
    * header Accept = 'application/json'
    * header Cookie = 'token=' + authToken.Token
    And print authToken.accessToken
    When method DELETE
    Then status 201
    And print 'DeleteResponse: ', response