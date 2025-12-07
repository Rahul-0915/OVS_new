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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "candidates")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Candidates.findAll", query = "SELECT c FROM Candidates c"),
    @NamedQuery(name = "Candidates.findByCandidateId", query = "SELECT c FROM Candidates c WHERE c.candidateId = :candidateId"),
    @NamedQuery(name = "Candidates.findByCandidateName", query = "SELECT c FROM Candidates c WHERE c.candidateName = :candidateName"),
    @NamedQuery(name = "Candidates.findByNominationDate", query = "SELECT c FROM Candidates c WHERE c.nominationDate = :nominationDate"),})
public class Candidates implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "candidate_id")
    private Integer candidateId;
    @Size(max = 50)
    @Column(name = "candidate_name")
    private String candidateName;
    @Column(name = "nomination_date")
    @Temporal(TemporalType.DATE)
    private Date nominationDate;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "candidates")
    private Collection<Votes> votesCollection;

    @JoinColumn(name = "election_id", referencedColumnName = "election_id")
    @ManyToOne
    private Elections elections;

    @JoinColumn(name = "party_id", referencedColumnName = "party_id")
    @ManyToOne
    private Party party;

    public Candidates() {
    }

    public Candidates(Integer candidateId) {
        this.candidateId = candidateId;
    }

    public Integer getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(Integer candidateId) {
        this.candidateId = candidateId;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public Date getNominationDate() {
        return nominationDate;
    }

    public void setNominationDate(Date nominationDate) {
        this.nominationDate = nominationDate;
    }

    public Collection<Votes> getVotesCollection() {
        return votesCollection;
    }

    public void setVotesCollection(Collection<Votes> votesCollection) {
        this.votesCollection = votesCollection;
    }

    public Elections getElections() {
        return elections;
    }

    public void setElections(Elections elections) {
        this.elections = elections;
    }

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (candidateId != null ? candidateId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Candidates)) {
            return false;
        }
        Candidates other = (Candidates) object;
        if ((this.candidateId == null && other.candidateId != null) || (this.candidateId != null && !this.candidateId.equals(other.candidateId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Candidates[ candidateId=" + candidateId + " ]";
    }

}
