package restassuredTraining;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.hasItems;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;

import helper.BaseClass;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;


public class GetMethods extends BaseClass {

	@Test
	public void TestGet() throws URISyntaxException {
		Response response = given().when().get(new URI("/ping/hello"));
		System.out.println(response.asString());
	}

	@Test
	public void TestGetAll() throws URISyntaxException {

		Response response = given().accept(ContentType.JSON).when().get(new URI("/all"));

		System.out.println(response.asString());
	}

	@Test
	public void TestGetAllXML() throws URISyntaxException {

		Response response = given().accept(ContentType.XML).when().get(new URI("/all"));

		System.out.println(response.asString());
	}

	@Test
	public void TestStatusCode() throws URISyntaxException {

		given().accept(ContentType.JSON).when().get(new URI("/all")).then().assertThat().statusCode(HttpStatus.SC_OK);

	}

	@Test
	public void TestGetWithID() throws URISyntaxException {

		given().accept(ContentType.JSON).when().get(new URI("/find/127")).then().assertThat()
				.statusCode(HttpStatus.SC_OK);

	}

	@Test
	public void TestGetWithInvalidID() throws URISyntaxException {

		given().accept(ContentType.JSON).when().get(new URI("/find/128")).then().assertThat()
				.statusCode(HttpStatus.SC_NOT_FOUND);
	}

	@Test
	public void TestGetBodyWithID() throws URISyntaxException {

		String body = given().accept(ContentType.JSON).when().get(new URI("/find/127")).thenReturn().body().asString();
		System.out.println(body);

	}

	@Test
	public void TestGetWithIDWithHeaders() throws URISyntaxException {

		Map<String, String> header = new HashMap<>();
		header.put("Accept", "application/json");
		String body = given().headers(header).when().get(new URI("/find/127")).thenReturn().body().asString();
		System.out.println(body);

	}

	@Test
	public void TestContent() throws URISyntaxException {
		given().accept(ContentType.JSON).when().get(new URI("/find/127")).then().body("BrandName",
				containsString("Apple"), "Id", equalTo(127), "LaptopName", equalToIgnoringCase("Mac Book Pro"));
	}

	@Test
	public void TestContentArrayJSON() throws URISyntaxException {
		given().accept(ContentType.JSON).when().get(new URI("/find/127")).then().body("Features.Feature",
				hasItems("8GB RAM", "1TB Hard Drive"), "Id", equalTo(127), "LaptopName",
				equalToIgnoringCase("Mac Book Pro"));
	}

	@Test
	public void TestContentArrayXML() throws URISyntaxException {
		given().accept(ContentType.XML).when().get(new URI("/find/127")).then().body("Laptop.Id",
				equalTo("127"), "Laptop.LaptopName", equalToIgnoringCase("Mac Book Pro"), "Laptop.Features.Feature", hasItems("8GB RAM", "1TB Hard Drive"));
	}
	
	@Test
	public void TestContentXMLPath() throws URISyntaxException {
		String body = given().accept(ContentType.XML).when().get(new URI("/find/127")).thenReturn().asString();
		XmlPath xml = new XmlPath(body);
		System.out.println(xml.getInt("Laptop.Id"));
		System.out.println(xml.getString("Laptop.LaptopName"));
		List<String> listFeatures = xml.getList("Laptop.Features.Feature");
		
		System.out.println(listFeatures.size());
		for (String items : listFeatures) {
			System.out.println(items);
		}
		
		Assert.assertEquals(127, xml.getInt("Laptop.Id"));
		Assert.assertEquals("Mac Book Pro", xml.getString("Laptop.LaptopName"));
	}
	
	public void TestContentJSONPath() throws URISyntaxException {
		String body = given().accept(ContentType.JSON).when().get(new URI("/find/127")).thenReturn().asString();
		JsonPath json = new JsonPath(body);
		
		Assert.assertEquals(127, json.getInt("Id"));
		Assert.assertEquals("Mac Book Pro", json.getString("LaptopName"));
		Assert.assertTrue(json.getList("Laptop.Features.Feature").contains("8GB RAM"));
	}
}