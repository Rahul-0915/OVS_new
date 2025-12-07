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
import jakarta.persistence.Lob;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Collection;

/**
 *
 * @author Rahul
 */
@Entity
@Table(name = "party")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Party.findAll", query = "SELECT p FROM Party p"),
    @NamedQuery(name = "Party.findByPartyId", query = "SELECT p FROM Party p WHERE p.partyId = :partyId"),
    @NamedQuery(name = "Party.findByPartyName", query = "SELECT p FROM Party p WHERE p.partyName = :partyName"),
    @NamedQuery(name = "Party.findByLeaderName", query = "SELECT p FROM Party p WHERE p.leaderName = :leaderName"),
    @NamedQuery(name = "Party.findByFoundedYear", query = "SELECT p FROM Party p WHERE p.foundedYear = :foundedYear")})
public class Party implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "party_id")
    private Integer partyId;
    @Size(max = 50)
    @Column(name = "party_name")
    private String partyName;
    @Size(max = 255)
    @Column(name = "party_symbol")
    private String partySymbol; // store image path instead of bytes

    @Size(max = 50)
    @Column(name = "leader_name")
    private String leaderName;
    @Column(name = "founded_year")
    private Integer foundedYear;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "party")
    private Collection<Candidates> candidatesCollection;

    public Party() {
    }

    public Party(Integer partyId) {
        this.partyId = partyId;
    }

    public Integer getPartyId() {
        return partyId;
    }

    public void setPartyId(Integer partyId) {
        this.partyId = partyId;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getPartySymbol() {
        return partySymbol;
    }

    public void setPartySymbol(String partySymbol) {
        this.partySymbol = partySymbol;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public Integer getFoundedYear() {
        return foundedYear;
    }

    public void setFoundedYear(Integer foundedYear) {
        this.foundedYear = foundedYear;
    }

    public Collection<Candidates> getCandidatesCollection() {
        return candidatesCollection;
    }

    public void setCandidatesCollection(Collection<Candidates> candidatesCollection) {
        this.candidatesCollection = candidatesCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (partyId != null ? partyId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Party)) {
            return false;
        }
        Party other = (Party) object;
        if ((this.partyId == null && other.partyId != null) || (this.partyId != null && !this.partyId.equals(other.partyId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Party[ partyId=" + partyId + " ]";
    }

}
