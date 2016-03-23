Feature: Member features
  This feature lists all the scenarios related to Member Demographics

  Background: I want to test the member services
    Given the ewelcome api is running

  Scenario Outline: Get the Member Services
    And I assign optumId as "<optumId>" to the request
    When I call the getMember service
    Then the response will have the status code is "<httpStatus>"
    And  I expect the response as "<expectedResponse>" with expected result as "<expectedResult>"

    Examples:
      |optumId|httpStatus  |expectedResponse              |expectedResult     |Comments                                      |

      |610    |200         | testFiles/Scenario200.json   | SUCCESS           |Valid OptumId                                 |
      |500    |404         | testFiles/Scenario404.json   | FAILURE           |Invalid OptumId                               |
      |1      |500         | testFiles/Scenario500.json   | FAILURE           |Compas Individual Id not found for the optumId|
