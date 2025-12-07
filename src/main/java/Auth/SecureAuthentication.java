/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auth;

import CdiBean.LoginBean;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import java.io.Serializable;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.AuthenticationException;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.credential.Password;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.CredentialValidationResult.Status;
import jakarta.security.enterprise.identitystore.IdentityStoreHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static jwtrest.Constants.AUTHORIZATION_HEADER;
import static jwtrest.Constants.BEARER;
import jwtrest.JWTCredential;
import jwtrest.TokenProvider;
import record.KeepRecord;

/**
 *
 * @author root
 */
@Named
@RequestScoped
public class SecureAuthentication implements HttpAuthenticationMechanism, Serializable {

    @Inject
    IdentityStoreHandler handler;
    CredentialValidationResult result;
    AuthenticationStatus status;
    @Inject
    TokenProvider tokenProvider;
    @Inject
    LoginBean lbean;
    @Inject
    KeepRecord keepRecord;

    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest request, HttpServletResponse response, HttpMessageContext ctx) throws AuthenticationException {
        //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

        try {
            if (request.getRequestURI().contains("Logout")) {
                request.logout();
                keepRecord.reset();
                response.sendRedirect("Login.jsf");
                return ctx.doNothing();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String token = extractToken(ctx);
        try {
            System.out.println("Name = " + request.getParameter("username"));

            if (token == null && request.getParameter("username") != null) {
                String username = request.getParameter("username");
                String password = request.getParameter("password");
                System.out.println("In Auth");
//                String username = lbean.getUsername();
//                String password = lbean.getPassword();
//                
                Credential credential = new UsernamePasswordCredential(username, new Password(password));
                result = handler.validate(credential);

                if (result.getStatus() == Status.VALID) {
                    keepRecord.setErrorStatus("");
                    AuthenticationStatus status = createToken(result, ctx);

                    status = ctx.notifyContainerAboutLogin(result);

                    keepRecord.setPrincipal(result.getCallerPrincipal());
                    keepRecord.setRoles(result.getCallerGroups());
                    keepRecord.setCredential(credential);

                    if (result.getCallerGroups().contains("admin")) {
                        request.getRequestDispatcher("AdminDashboard.jsf").forward(request, response);
                    }
                    if (result.getCallerGroups().contains("user")) {
                        request.getRequestDispatcher("Users.jsf").forward(request, response);
                    }

                    return status;

                } else {
                    keepRecord.setErrorStatus("Either Username or Password is wrong !");
                    response.sendRedirect("Login.jsf");

                    //lbean.setStatus(AuthenticationStatus.SEND_FAILURE);
                    return ctx.doNothing();
                    // request.getServletContext().getRequestDispatcher("/Login.jsf").forward(request, response);
                }
            }

            if (keepRecord.getToken() != null) {
                //  Credential credential1 = new UsernamePasswordCredential(KeepRecord.getUsername(), new Password(KeepRecord.getPassword()));
//                result = handler.validate(KeepRecord.getCredential());
//                AuthenticationStatus status = createToken(result, ctx);
//                
//                if(request.getRequestURI().contains("admin") && result.getCallerGroups().contains("Supervisor"))
//                {
//                    ctx.responseUnauthorized();
//                }
//               else if(request.getRequestURI().contains("user") && result.getCallerGroups().contains("Admin"))
//               {
//                  ctx.responseUnauthorized();
//               }

                ctx.notifyContainerAboutLogin(keepRecord.getPrincipal(), keepRecord.getRoles());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (token != null) {
            // validation of the jwt credential

            return validateToken(token, ctx);
        } else if (ctx.isProtected()) {
            // A protected resource is a resource for which a constraint has been defined.
            // if there are no credentials and the resource is protected, we response with unauthorized status
            return ctx.responseUnauthorized();
        }
        return ctx.doNothing();
    }

    private AuthenticationStatus validateToken(String token, HttpMessageContext context) {
        try {
            if (tokenProvider.validateToken(token)) {
                JWTCredential credential = tokenProvider.getCredential(token);
                // System.out.println("JWTAuthenticationMechanism-Token Validated");
                return context.notifyContainerAboutLogin(credential.getPrincipal(), credential.getAuthorities());

            }
            // if token invalid, response with unauthorized status
            return context.responseUnauthorized();
        } catch (ExpiredJwtException eje) {
            //LOGGER.log(Level.INFO, "Security exception for user {0} - {1}", new String[]{eje.getClaims().getSubject(), eje.getMessage()});
            return context.responseUnauthorized();
        }
    }

    /**
     * Create the JWT using CredentialValidationResult received from
     * IdentityStoreHandler
     *
     * @param result the result from validation of UsernamePasswordCredential
     * @param context
     * @return the AuthenticationStatus to notify the container
     */
    private AuthenticationStatus createToken(CredentialValidationResult result, HttpMessageContext context) {
        // if (!isRememberMe(context)) {
        // if (true) {
        String jwt = tokenProvider.createToken(result.getCallerPrincipal().getName(), result.getCallerGroups(), false);
        //context.getRequest().getSession().setAttribute("token", jwt);
        keepRecord.setToken(jwt);
        context.getResponse().addHeader(AUTHORIZATION_HEADER, BEARER + jwt);
        System.out.println("Token Value" + jwt);

        //}
        System.out.println("JWTAuthenticationMechanism - Token Created");

        return context.notifyContainerAboutLogin(result.getCallerPrincipal(), result.getCallerGroups());
    }

    /**
     * To extract the JWT from Authorization HTTP header
     *
     * @param context
     * @return The JWT access tokens
     */
    private String extractToken(HttpMessageContext context) {
        String authorizationHeader = context.getRequest().getHeader(AUTHORIZATION_HEADER);
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER)) {
            String token = authorizationHeader.substring(BEARER.length(), authorizationHeader.length());
            keepRecord.setToken(token);
            //  System.out.println("JWTAuthenticationMechanism - Extract Tokens");
            return token;
        }
        return null;
    }

    /**
     * this function invoked using RememberMe.isRememberMeExpression EL
     * expression
     *
     * @param context
     * @return The remember me flag
     */
//    public Boolean isRememberMe(HttpMessageContext context) {
//        return Boolean.valueOf(context.getRequest().getParameter("rememberme"));
//    }
}
