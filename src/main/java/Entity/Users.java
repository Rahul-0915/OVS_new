/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 *
 * @author Rahul
 */
@Entity
@Table(name = "users")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Users.findAll", query = "SELECT u FROM Users u"),
    @NamedQuery(name = "Users.findByUserId", query = "SELECT u FROM Users u WHERE u.userId = :userId"),
    @NamedQuery(name = "Users.findByUserName", query = "SELECT u FROM Users u WHERE u.userName = :userName"),
    @NamedQuery(name = "Users.findByEmailId", query = "SELECT u FROM Users u WHERE u.emailId = :emailId"),
    @NamedQuery(name = "Users.findByMobileNumber", query = "SELECT u FROM Users u WHERE u.mobileNumber = :mobileNumber"),
    @NamedQuery(name = "Users.findByPassword", query = "SELECT u FROM Users u WHERE u.password = :password"),
    @NamedQuery(name = "Users.findByVerification", query = "SELECT u FROM Users u WHERE u.verification = :verification")})
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Size(max = 40)
    @Column(name = "user_name")
    private String userName;

    @Size(max = 50)
    @Column(name = "email_id")
    private String emailId;

    @Column(name = "mobile_number")
    private String mobileNumber;

    // ✔ FIX: Hashed password length hoti hai 40-60 chars → max 20 wrong tha
    @Size(max = 255)
    @Column(name = "password")
    private String password;

    @Column(name = "verification")
    private Integer verification;

    @JoinColumn(name = "g_id", referencedColumnName = "g_id")
    @ManyToOne
    private GroupMaster groupmaster;

    public Users() {
    }

    public Users(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

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

    public Integer getVerification() {
        return verification;
    }

    public void setVerification(Integer verification) {
        this.verification = verification;
    }

    public GroupMaster getGroupmaster() {
        return groupmaster;
    }

    public void setGroupmaster(GroupMaster groupmaster) {
        this.groupmaster = groupmaster;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userId != null ? userId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Users)) {
            return false;
        }
        Users other = (Users) object;
        if ((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Users[ userId=" + userId + " ]";
    }

}
