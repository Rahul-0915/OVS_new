package client;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author root
 */
import java.io.IOException;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.ext.Provider;
import record.KeepRecord;

/**
 * This RequestFilter performs a form based authentication. The filter can be
 * used with a jakarta.ws.rs.client.Client.
 *
 * Author : Kamlendu Pandey
 */
//@Secured
//@WebFilter(filterName = "AuthenticationFilter", urlPatterns = { "/webresources/*" })
@Provider
@PreMatching
public class MyRestFilter implements ClientRequestFilter {

    public static String mytoken;
    @Inject
    KeepRecord keepRecord;

    public MyRestFilter(String token) {
        mytoken = token;
    }

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
     

        System.out.println(" In form Auth Client Filter " + mytoken);

        requestContext.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + mytoken);

    }

}