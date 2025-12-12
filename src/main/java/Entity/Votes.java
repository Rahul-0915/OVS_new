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
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "votes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Votes.findAll", query = "SELECT v FROM Votes v"),
    @NamedQuery(name = "Votes.findByVoteId", query = "SELECT v FROM Votes v WHERE v.voteId = :voteId"),
    @NamedQuery(name = "Votes.findByTimeStamp", query = "SELECT v FROM Votes v WHERE v.timeStamp = :timeStamp"),
    @NamedQuery(name = "Votes.findByVerifyStatus", query = "SELECT v FROM Votes v WHERE v.verifyStatus = :verifyStatus")
})
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

    @ManyToOne
    @JoinColumn(name = "candidate_id", referencedColumnName = "candidate_id")
    private Candidates candidates;

    @ManyToOne
    @JoinColumn(name = "election_id", referencedColumnName = "election_id")
    private Elections elections;

    @ManyToOne
    @JoinColumn(name = "party_id", referencedColumnName = "party_id")
    private Party party;  // ✅ Added Party relationship

    @Column(name = "election_name")
    private String electionName;  // ✅ Added Election Name as String

    @ManyToOne
    @JoinColumn(name = "voter_id", referencedColumnName = "voter_id")
    private Voters voters;

    public Votes() {
    }

    public Votes(Integer voteId) {
        this.voteId = voteId;
    }

    // Getters & Setters
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

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public String getElectionName() {
        return electionName;
    }

    public void setElectionName(String electionName) {
        this.electionName = electionName;
    }

    public Voters getVoters() {
        return voters;
    }

    public void setVoters(Voters voters) {
        this.voters = voters;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (voteId != null ? voteId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Votes)) {
            return false;
        }
        Votes other = (Votes) object;
        return (this.voteId != null || other.voteId == null) && (this.voteId == null || this.voteId.equals(other.voteId));
    }

    @Override
    public String toString() {
        return "Entity.Votes[ voteId=" + voteId + " ]";
    }
}
