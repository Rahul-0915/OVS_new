/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CdiBean;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import record.KeepRecord;

/**
 *
 * @author root
 */
@Named(value = "loginBean")
@RequestScoped
public class LoginBean {

    @Inject
    KeepRecord keepRecord;

    private String errorstatus;

    public String getErrorStatus() {
        return keepRecord.getErrorStatus();
    }

    public void setErrorStatus(String status) {

        this.errorstatus = status;
    }

    public LoginBean() {

        // errorstatus="";
    }

    public String getUsername() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}