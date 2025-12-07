/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author Rahul
 */
@Entity
@Table(name = "voters")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Voters.findAll", query = "SELECT v FROM Voters v"),
    @NamedQuery(name = "Voters.findByVoterId", query = "SELECT v FROM Voters v WHERE v.voterId = :voterId"),
    @NamedQuery(name = "Voters.findByVoterName", query = "SELECT v FROM Voters v WHERE v.voterName = :voterName"),
    @NamedQuery(name = "Voters.findByMobileNumber", query = "SELECT v FROM Voters v WHERE v.mobileNumber = :mobileNumber"),
    @NamedQuery(name = "Voters.findByAdharNumber", query = "SELECT v FROM Voters v WHERE v.adharNumber = :adharNumber"),
    @NamedQuery(name = "Voters.findByEmailId", query = "SELECT v FROM Voters v WHERE v.emailId = :emailId"),
    @NamedQuery(name = "Voters.findByDob", query = "SELECT v FROM Voters v WHERE v.dob = :dob"),
    @NamedQuery(name = "Voters.findByCity", query = "SELECT v FROM Voters v WHERE v.city = :city"),
    @NamedQuery(name = "Voters.findByPincode", query = "SELECT v FROM Voters v WHERE v.pincode = :pincode"),
    @NamedQuery(name = "Voters.findByAddress", query = "SELECT v FROM Voters v WHERE v.address = :address"),
    @NamedQuery(name = "Voters.findByApplyDate", query = "SELECT v FROM Voters v WHERE v.applyDate = :applyDate"),
    @NamedQuery(name = "Voters.findByIssueDate", query = "SELECT v FROM Voters v WHERE v.issueDate = :issueDate"),
    @NamedQuery(name = "Voters.findByStatus", query = "SELECT v FROM Voters v WHERE v.status = :status")})
public class Voters implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "voter_id")
    private Integer voterId;
    @Size(max = 100)
    @Column(name = "voter_name")
    private String voterName;
    @Column(name = "mobile_number")
    private Integer mobileNumber;
    @Column(name = "adhar_number")
    private Integer adharNumber;
    @Size(max = 50)
    @Column(name = "email_id")
    private String emailId;
    @Column(name = "dob")
    @Temporal(TemporalType.DATE)
    private Date dob;
    @Size(max = 20)
    @Column(name = "city")
    private String city;
    @Column(name = "pincode")
    private Integer pincode;
    @Size(max = 60)
    @Column(name = "address")
    private String address;
    @Column(name = "apply_date")
    @Temporal(TemporalType.DATE)
    private Date applyDate;
    @Column(name = "issue_date")
    @Temporal(TemporalType.DATE)
    private Date issueDate;
    @Column(name = "status")
    private Integer status;

    @Column(name = "adhar_file")
    private String adharFile;

    @Column(name = "voter_image")
    private String voterImage;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "voters")
    private Collection<Votes> votesCollection;

    public Voters() {
    }

    public Voters(Integer voterId) {
        this.voterId = voterId;
    }

    public Integer getVoterId() {
        return voterId;
    }

    public void setVoterId(Integer voterId) {
        this.voterId = voterId;
    }

    public String getVoterName() {
        return voterName;
    }

    public void setVoterName(String voterName) {
        this.voterName = voterName;
    }

    public Integer getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(Integer mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Integer getAdharNumber() {
        return adharNumber;
    }

    public void setAdharNumber(Integer adharNumber) {
        this.adharNumber = adharNumber;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getPincode() {
        return pincode;
    }

    public void setPincode(Integer pincode) {
        this.pincode = pincode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAdharFile() {
        return adharFile;
    }

    public void setAdharFile(String adharFile) {
        this.adharFile = adharFile;
    }

    public String getVoterImage() {
        return voterImage;
    }

    public void setVoterImage(String voterImage) {
        this.voterImage = voterImage;
    }

    public Collection<Votes> getVotesCollection() {
        return votesCollection;
    }

    public void setVotesCollection(Collection<Votes> votesCollection) {
        this.votesCollection = votesCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (voterId != null ? voterId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Voters)) {
            return false;
        }
        Voters other = (Voters) object;
        if ((this.voterId == null && other.voterId != null) || (this.voterId != null && !this.voterId.equals(other.voterId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Voters[ voterId=" + voterId + " ]";
    }

    public boolean isEmpty() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
