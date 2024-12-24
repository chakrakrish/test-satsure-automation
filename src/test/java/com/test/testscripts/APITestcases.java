package com.test.testscripts;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class APITestcases {
	private static RequestSpecification requestSpec;

	@BeforeClass
	public void setUp() {
		// Initialize Request Specification using RequestSpecBuilder
		requestSpec = new RequestSpecBuilder()
				.setBaseUri("https://test.api.docs") // Set base URI
				.addHeader("Content-Type", "application/json") // Add headers
				.addHeader("Accept-Language", "en-US")
				.build();
	}

	/**
	 * Test: Verify API Response Status and Essential Fields
	 */
	@Test(priority = 1, description = "Verify API response status and essential fields")
	public void testApiResponseFields() {
		// Perform GET request
		Response response = given()
				.spec(requestSpec)
				.get("/getFormResponse");

		// Validate status code
		Assert.assertEquals(response.getStatusCode(), 200, "Unexpected status code.");

		// Extract response fields
		String accountEmail = response.jsonPath().getString("account_email");
		String locale = response.jsonPath().getString("locale");
		boolean isCompleted = response.jsonPath().getBoolean("completed");

		// Validate response body content
		Assert.assertEquals(accountEmail, "test123@gmail.com", "Account email mismatch.");
		Assert.assertEquals(locale, "India", "Locale mismatch.");
		Assert.assertTrue(isCompleted, "Completion status mismatch.");
	}

	/**
	 * Test: Validate Suggestion List Content
	 */
	@Test(priority = 2, description = "Validate suggestion list content")
	public void testSuggestionList() {
		// Perform GET request
		Response response = given()
				.spec(requestSpec)
				.get("/getFormResponse");

		// Extract and validate suggestion list
		String[] expectedSuggestions = {"agile methodology", "agile methodology process", "agile methodology testing"};
		String[] actualSuggestions = response.jsonPath().getList("suggestion_list").toArray(new String[0]);

		Assert.assertEqualsNoOrder(actualSuggestions, expectedSuggestions, "Suggestion list does not match expected values.");
	}
}