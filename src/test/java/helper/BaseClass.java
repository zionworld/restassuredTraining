package helper;
import static io.restassured.RestAssured.*;

import org.junit.BeforeClass;

public class BaseClass {

	@BeforeClass
	public static void SetUp() {
	
		baseURI = "http://localhost";
		port = 8080;
		basePath = "/laptop-bag/webapi/api"; 
	}
}
