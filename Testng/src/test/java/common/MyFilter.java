package common;

import com.jayway.restassured.builder.ResponseBuilder;
import com.jayway.restassured.filter.Filter;
import com.jayway.restassured.filter.FilterContext;
import com.jayway.restassured.internal.RestAssuredResponseImpl;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.FilterableRequestSpecification;
import com.jayway.restassured.specification.FilterableResponseSpecification;

/**
 * @author yunhua.he
 * @date 2017/11/30
 */
public class MyFilter implements Filter{

    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {
        Response response = ctx.next(requestSpec, responseSpec);
        final String responseBody = response.asString();
        final String decodeBody = ThreeDes.decryptCode(responseBody);
        final Response build = new ResponseBuilder().clone(response).setBody(decodeBody).build();
        ((RestAssuredResponseImpl) build).setHasExpectations(true);
        return build;
    }
}
