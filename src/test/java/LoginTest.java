import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.delete;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginTest {

    final static String ROOT_URI = "https://the-one-api.herokuapp.com/v1";
    final static String token = "RM7Z1i5FzcNJXDKiFJdl";

    @Test
    public void simple_get_test() {
        Response response = given().get(ROOT_URI + "/book");
        System.out.println(response.asString());


        response.then().statusCode(200);

        response.then().time(lessThan(2000l));

        response.then().body("docs._id", notNullValue());
        response.then().body("docs._name", notNullValue());

        response.then().body("docs._id", hasItem("5cf5805fb53e011a64671582"));
        response.then().body("docs.name", hasItem("The Fellowship Of The Ring"));

        response.then().body("docs._id", hasItems("5cf5805fb53e011a64671582", "5cf58080b53e011a64671584", "5cf58080b53e011a64671584"));
        response.then().body("docs.name", hasItems("The Fellowship Of The Ring", "The Two Towers", "The Return Of The King"));
    }

    @Test
    public void simple_get_test1() {
        Response response = given().header("Content-Language", "en_US")
                .contentType("application/json").get(ROOT_URI + "/book/5cf5805fb53e011a64671582");
        System.out.println(response.asString());


        response.then().statusCode(200);
        response.then().body("_id", notNullValue());
        response.then().body("_name", notNullValue());
        response.then().body("_id", equalTo("5cf5805fb53e011a64671582"));
        response.then().body("name", equalTo("The Fellowship Of The Ring"));
    }

    @Test
    public void simple_get_tes2t() {
        ArrayList<String> id = get(ROOT_URI + "/book").then().body(notNullValue()).extract().path("docs._id");
        System.out.println(id.get(0));

        for (String strId: id){
            System.out.println();
            get(ROOT_URI + "/book/" + strId).then().body("_id", equalTo(strId)).body("name", notNullValue());

        }




    }



    @Test
    public void simple_get_test2() {
        Response response = given().auth().oauth2(token).get(ROOT_URI + "/movie");
        System.out.println(response.asString());

        response.then().statusCode(200);
        response.then().contentType(ContentType.JSON);
//        response.then().body("docs._id", hasItems("5cf5805fb53e011a64671582", "5cf58080b53e011a64671584", "5cf58080b53e011a64671584"));
//        response.then().body("docs.name", hasItems("The Fellowship Of The Ring", "The Two Towers", "The Return Of The King"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"arabic", "danish", "dutch", "french"})
    public void simple_get_test3(String language) {


        String url = "https://kidkiddos.com/collections/" + language;
        Response response = get(url);
        System.out.println(response.asString());
        response.then().statusCode(200);
        assertTrue(response.getBody().prettyPrint().toLowerCase().contains(language));


    }


}
