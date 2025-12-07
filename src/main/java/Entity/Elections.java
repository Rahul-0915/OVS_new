/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "elections")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Elections.findAll", query = "SELECT e FROM Elections e"),
    @NamedQuery(name = "Elections.findByElectionId", query = "SELECT e FROM Elections e WHERE e.electionId = :electionId"),
    @NamedQuery(name = "Elections.findByStartDate", query = "SELECT e FROM Elections e WHERE e.startDate = :startDate"),
    @NamedQuery(name = "Elections.findByEndDate", query = "SELECT e FROM Elections e WHERE e.endDate = :endDate"),
    @NamedQuery(name = "Elections.findByElectionName", query = "SELECT e FROM Elections e WHERE e.electionName = :electionName"),
    @NamedQuery(name = "Elections.findByStatus", query = "SELECT e FROM Elections e WHERE e.status = :status"),
    @NamedQuery(name = "Elections.findByDescription", query = "SELECT e FROM Elections e WHERE e.description = :description")})
public class Elections implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "election_id")
    private Integer electionId;
    @Column(name = "start_date")
    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Column(name = "end_date")
    @Temporal(TemporalType.DATE)
    private Date endDate;
    @Size(max = 50)
    @Column(name = "election_name")
    private String electionName;
    @Size(max = 20)
    @Column(name = "status")
    private String status;
    @Size(max = 50)
    @Column(name = "description")
    private String description;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "elections")
    private Collection<Votes> votesCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "elections")
    private Collection<Candidates> candidateCollection;

    public Elections() {
    }

    public Elections(Integer electionId) {
        this.electionId = electionId;
    }

    public Integer getElectionId() {
        return electionId;
    }

    public void setElectionId(Integer electionId) {
        this.electionId = electionId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getElectionName() {
        return electionName;
    }

    public void setElectionName(String electionName) {
        this.electionName = electionName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Collection<Votes> getVotesCollection() {
        return votesCollection;
    }

    public void setVotesCollection(Collection<Votes> votesCollection) {
        this.votesCollection = votesCollection;
    }

    public Collection<Candidates> getCandidateCollection() {
        return candidateCollection;
    }

    public void setCandidateCollection(Collection<Candidates> candidateCollection) {
        this.candidateCollection = candidateCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (electionId != null ? electionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Elections)) {
            return false;
        }
        Elections other = (Elections) object;
        if ((this.electionId == null && other.electionId != null) || (this.electionId != null && !this.electionId.equals(other.electionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Elections[ electionId=" + electionId + " ]";
    }

}
