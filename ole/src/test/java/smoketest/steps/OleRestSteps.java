package smoketest.steps;

import com.google.gson.JsonObject;
import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import smoketest.utils.FileUtils;
import smoketest.utils.JsonUtils;
import smoketest.utils.RestApiClient;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static smoketest.utils.PropertyUtils.getProperty;

// import org.springframework.http.HttpStatus;


public class OleRestSteps {

    private static final String contextPath = "/ecomm/secure/";
    private RestApiClient restApiClient;

    @Given("^the ewelcome api is running$")
    public void setUpEcommRestApi() {
        restApiClient = new RestApiClient();
        restApiClient.setHostName(getProperty("ewelcome.base.url"));
    }


    @And("^I assign optumId as \"([^\\\"]*)\" to the request$")
    public void setOptumIdToTheHttpHeaders(String optumId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("optumId", optumId + "");
        restApiClient.setHeaders(headers);
    }


    @When("^I call the getMember service$")
    public void invokeGetMemberService() {
        restApiClient.setRestUri(contextPath + "members");
        restApiClient.setHttpMethod(HttpMethod.GET);

        restApiClient.execute();
    }

    @Then("^the response will have the status code is \"([^\"]*)\"$")
    public void verifyHttpStatus(int expectedValue) throws Throwable {
        assertEquals(restApiClient.getResponseEntity().getStatusCode().value(), expectedValue);
    }

    @And("^I expect the response body to have an error message$")
    public void verifyResponseBodyIsNotNull() throws Throwable {
        assertNotNull(restApiClient.getResponseEntity().getBody());
    }

    @And("^I expect the response as \"([^\"]*)\" with expected result as \"([^\"]*)\"$")
    public void verifyResponse(String expectedJsonResponse, String expectedResult) throws Exception {
        JsonObject expectedJsonObject = JsonUtils.createJsonFromString(FileUtils.readFixture(expectedJsonResponse));
        JsonObject actualJsonObject = JsonUtils.createJsonFromString(restApiClient.getResponseEntity().getBody());

        if ("SUCCESS".equals(expectedResult)) {
            verifyAssertionsForSuccess(actualJsonObject, expectedJsonObject);

        } else if ("FAILURE".equals(expectedResult)) {
            verifyAssertionsForFailure(actualJsonObject, expectedJsonObject);
        }
    }

    private void verifyAssertionsForSuccess(JsonObject actualJsonObject, JsonObject expectedJsonObject) {
        assertEquals(actualJsonObject.get("address").getAsJsonObject().get("addressLine1").getAsString(), expectedJsonObject.get("address").getAsJsonObject().get("addressLine1").getAsString());

        assertEquals(actualJsonObject.get("firstName").getAsString(), expectedJsonObject.get("firstName").getAsString());
        assertEquals(actualJsonObject.get("lastName").getAsString(), expectedJsonObject.get("lastName").getAsString());
        assertEquals(actualJsonObject.get("memberNumber").getAsString(), expectedJsonObject.get("memberNumber").getAsString());
        assertEquals(actualJsonObject.get("compasId").getAsString(), expectedJsonObject.get("compasId").getAsString());
        assertEquals(actualJsonObject.get("policies").getAsJsonArray().size(),expectedJsonObject.get("policies").getAsJsonArray().size());

        assertEquals(actualJsonObject.get("policies").getAsJsonArray(),expectedJsonObject.get("policies").getAsJsonArray());

    }

    private void verifyAssertionsForFailure(JsonObject actualJsonObject, JsonObject expectedJsonObject) {
        assertNotNull(actualJsonObject);
        assertNotNull(expectedJsonObject);

        assertEquals(getMessageAttributeFromErrorJsonObject(actualJsonObject), getMessageAttributeFromErrorJsonObject(expectedJsonObject));
    }

    private String getMessageAttributeFromErrorJsonObject(JsonObject obj) {
        Iterator objIterator = obj.get("errors").getAsJsonArray().iterator();

        if (objIterator.hasNext()) {
            JsonObject jsonObject = (JsonObject) objIterator.next();
            return jsonObject.get("message").getAsString();
        }

        return null;
    }


    @And("^the response content will match file '(\\S*)'")
    public void the_response_content_will_match_file_testFiles_Scenario_json(String fileName) throws Exception {
        // Express the Regexp above with the code you wish you had
        JsonObject expectedJsonObject = JsonUtils.createJsonFromString(FileUtils.readFixture(fileName));
        JsonObject actualJsonObject = JsonUtils.createJsonFromString(restApiClient.getResponseEntity().getBody());
        System.out.println(restApiClient.getResponseEntity().getBody());
        System.out.println(FileUtils.readFixture(fileName));

        assertEquals(actualJsonObject, expectedJsonObject);
    }

    @And("^provide the email address on file '(\\S*)'")
    public void provide_the_email_address(String fileName ) throws Exception {
        // Express the Regexp above with the code you wish you had

        JsonObject requestJson = JsonUtils.createJsonFromString(FileUtils.readFixture(fileName));

      //  restApiClient.requestBody = requestJson.toString();
        restApiClient.setRequestBody(requestJson.toString());

       // restApiClient.execute();
       // restApiClient.headers.clear();

    }

    @When("^I call the updateMember service$")
    public void I_call_the_updateMember_service() throws Exception {
        // Express the Regexp above with the code you wish you had

        restApiClient.setRestUri(contextPath + "members");

        restApiClient.setHttpMethod(HttpMethod.PUT);


        restApiClient.execute();
    }

    @And("^no optumId header is supplied$")
    public void no_optumId_header_is_supplied() throws Exception {
        // Express the Regexp above with the code you wish you had
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
         restApiClient.setHeaders(headers);
    }

    @When("^I call the updateMember services$")
    public void I_call_the_updateMember_services() throws Throwable {

        restApiClient.setRestUri(contextPath + "members");
        restApiClient.setHttpMethod(HttpMethod.PUT);
        restApiClient.execute();

        throw new PendingException();
    }

    @When("^I call the createMember services$")
    public void I_call_the_createMember_services() throws Throwable {
        restApiClient.setRestUri(contextPath + "members");

        restApiClient.setHttpMethod(HttpMethod.POST);

        restApiClient.execute();
        throw new PendingException();
    }
}