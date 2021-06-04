package restassuredTraining;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpStatus;
import org.junit.Test;

import helper.BaseClass;
import io.restassured.http.ContentType;

public class TestQueryParameters extends BaseClass{

	@Test
	public void TestQuery() throws URISyntaxException {

		given()
		.accept(ContentType.JSON)
		.param("id", 127)
		.param("LaptopName", "Mac Book Pro")
		.when()
		.get(new URI("/query"))
		.then()
		.assertThat()
		.statusCode(HttpStatus.SC_OK)
		.and()
		.assertThat()
		.body("Features.Feature", hasItems("1TB Hard Drive", "8GB RAM") );
}
}