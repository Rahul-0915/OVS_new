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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Rahul
 */
@Entity
@Table(name = "votes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Votes.findAll", query = "SELECT v FROM Votes v"),
    @NamedQuery(name = "Votes.findByVoteId", query = "SELECT v FROM Votes v WHERE v.voteId = :voteId"),
    @NamedQuery(name = "Votes.findByTimeStamp", query = "SELECT v FROM Votes v WHERE v.timeStamp = :timeStamp"),
    @NamedQuery(name = "Votes.findByVerifyStatus", query = "SELECT v FROM Votes v WHERE v.verifyStatus = :verifyStatus"),})
public class Votes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "vote_id")
    private Integer voteId;

    @Column(name = "time_stamp")
    @Temporal(TemporalType.DATE)
    private Date timeStamp;
    @Column(name = "verify_status")
    private Integer verifyStatus;

    @JoinColumn(name = "Candidate_id", referencedColumnName = "Candidate_id")
    @ManyToOne
    private Candidates candidates;

    @JoinColumn(name = "election_id", referencedColumnName = "election_id")
    @ManyToOne
    private Elections elections;

    @JoinColumn(name = "election_name", referencedColumnName = "election_name")
    @ManyToOne
    private Elections election;

    @JoinColumn(name = "voter_id", referencedColumnName = "voter_id")
    @ManyToOne
    private Voters voters;

    public Voters getVoters() {
        return voters;
    }

    public void setVoters(Voters voters) {
        this.voters = voters;
    }

    public Votes() {
    }

    public Votes(Integer voteId) {
        this.voteId = voteId;
    }

    public Integer getVoteId() {
        return voteId;
    }

    public void setVoteId(Integer voteId) {
        this.voteId = voteId;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Integer getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(Integer verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    public Candidates getCandidates() {
        return candidates;
    }

    public void setCandidates(Candidates candidates) {
        this.candidates = candidates;
    }

    public Elections getElections() {
        return elections;
    }

    public void setElections(Elections elections) {
        this.elections = elections;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (voteId != null ? voteId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Votes)) {
            return false;
        }
        Votes other = (Votes) object;
        if ((this.voteId == null && other.voteId != null) || (this.voteId != null && !this.voteId.equals(other.voteId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Votes[ voteId=" + voteId + " ]";
    }

    public void setVoter(Voters voter) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void setCandidate(Candidates candidate) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void setParty(Party party) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void setElection(Elections election) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void setElectionName(String electionName) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public Object getCandidate() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public Object getParty() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public Object getVoter() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public Object getElection() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
