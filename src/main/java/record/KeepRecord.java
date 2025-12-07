/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package record;

import java.io.Serializable;
import java.util.Set;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.security.enterprise.CallerPrincipal;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;


@Named
@SessionScoped
public class KeepRecord implements Serializable {

    private CredentialValidationResult result;
    private CallerPrincipal principal;
    private Set<String> roles;
    private String token;
    private String username;
    private String password;
    private String errorStatus;
    private Credential credential;

    public String getErrorStatus() {
        return errorStatus;
    }

    public Credential getCredential() {
        return credential;
    }

    public void setCredential(Credential credential) {
        this.credential = credential;
    }

    public void setErrorStatus(String errorStatus) {
        this.errorStatus = errorStatus;
    }

    public KeepRecord() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public CredentialValidationResult getResult() {
        return result;
    }

    public void setResult(CredentialValidationResult result) {
        this.result = result;
    }

    public CallerPrincipal getPrincipal() {
        return principal;
    }

    public void setPrincipal(CallerPrincipal principal) {
        this.principal = principal;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void reset() {

        principal = null;
        token = null;
        username = null;
        password = null;
        token = null;
        errorStatus = "";
    }

}