package CdiBean;

import Client.UserClient;
import EJB.UserBeanLocal;
import Entity.Users;
import jakarta.ejb.EJB;
import jakarta.inject.Named;
import jakarta.enterprise.context.RequestScoped;

import jakarta.faces.context.FacesContext;

import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.glassfish.soteria.identitystores.hash.PasswordHashCompare;
import org.glassfish.soteria.identitystores.hash.Pbkdf2PasswordHashImpl;

@Named(value = "UserCDIBean")
@RequestScoped

public class UserCDIBean {

    @EJB
    UserBeanLocal ubl;

    UserClient ac = new UserClient();
    Pbkdf2PasswordHashImpl pb;
    PasswordHashCompare phc;
    Response rs;

    Collection<Users> user;
    GenericType<Collection<Users>> guser;

    private String userName;
    private String emailId;
    private String mobileNumber;
    private String password;
    private String confirmPassword;   // ⬅️ ADDED
    private int gId = 1;              // ⬅️ BY DEFAULT
    private int verification = 1;     // ⬅️ BY DEFAULT (0 = not verified)

    public UserCDIBean() {
        pb = new Pbkdf2PasswordHashImpl();
        phc = new PasswordHashCompare();

        user = new ArrayList();
        guser = new GenericType<Collection<Users>>() {
        };
    }

//    // EMAIL VALIDATION
    private static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&-]+(?:\\.[a-zA-Z0-9_+&-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public String insertUser() {

        // EMAIL VALIDATION
        if (!isValidEmail(emailId)) {
            System.out.println("Invalid email format");
            return null;
        }

        // PASSWORD MATCH
        if (!password.equals(confirmPassword)) {
            System.out.println("Password and Confirm Password do not match");
            return null;
        }

        try {
            // HASH PASSWORD
//            String hashedPassword = pb.generate(password.toCharArray());

            // INSERT INTO DB
            ubl.addUser(userName, emailId, mobileNumber, password, gId, verification);
            System.out.println("User added successfully!");

            // 👉 SWEETALERT ON SUCCESS
            FacesContext.getCurrentInstance().getPartialViewContext().getEvalScripts()
                    .add("Swal.fire('Thank you!', 'Registration Successful', 'success');");
            // CLEAR FORM VALUES

            // 1.5 sec wait → then redirect
            FacesContext.getCurrentInstance().getPartialViewContext().getEvalScripts()
                    .add("setTimeout(function(){ window.location='Login.jsf'; }, 1500);");
            userName = null;
            emailId = null;
            mobileNumber = null;
            password = null;
            confirmPassword = null;
            gId = 0;  // agar integer hai

            return null; // JS redirect

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    // GETTERS & SETTERS
    public Collection<Users> getUser() {
        return user;
    }

    public void setUser(Collection<Users> user) {
        this.user = user;
    }

    public GenericType<Collection<Users>> getGuser() {
        return guser;
    }

    public void setGuser(GenericType<Collection<Users>> guser) {
        this.guser = guser;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
//

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public int getgId() {
        return gId;
    }

    public void setgId(int gId) {
        this.gId = gId;
    }

    public int getVerification() {
        return verification;
    }

    public void setVerification(int verification) {
        this.verification = verification;
    }

}
