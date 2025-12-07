package CdiBean;

import EJB.AdminBeanLocal;
import Entity.Candidates;
import Entity.Elections;
import Entity.Party;
import jakarta.ejb.EJB;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

@Named(value = "candidateCDIBean")
@SessionScoped
public class CandidateCDIBean implements Serializable {

    @EJB
    private AdminBeanLocal abl;

    private Integer candidateId;
    private String candidateName;
    private Date nominationDate;

    private Integer electionId;
    private Integer partyId;

    // LISTS
    private Collection<Candidates> candidatesList;
    private Collection<Elections> electionsList;
    private Collection<Party> partyList;

    // SEARCH FIELDS
    private String searchCandidateName;
    private String searchElection;
    private String searchParty;

    public CandidateCDIBean() {}

    // ================= GET ALL ==================
    public Collection<Candidates> getAllCandidates() {
        if (candidatesList == null) {
            candidatesList = abl.getAllCandidates();
        }
        return candidatesList;
    }

    public Collection<Elections> getAllElections() {
        return abl.getAllElections();
    }

    public Collection<Party> getAllParties() {
        return abl.getAllParties();
    }

    // ================= ADD ====================
    public String addCandidate() {
        abl.addCandidates(candidateId, candidateName, nominationDate, electionId, partyId);
        clearForm();
        return "ManageCandidates.xhtml?faces-redirect=true";
    }

    // ================= DELETE =================
    public void deleteCandidate(int cId) {
        abl.deleteCandidate(cId);
        candidatesList = abl.getAllCandidates();
    }

    // ================= SEARCH ==================
    public void searchCandidates() {

        // No Input → Show All
        if ((searchCandidateName == null || searchCandidateName.isBlank()) &&
            (searchElection == null || searchElection.isBlank()) &&
            (searchParty == null || searchParty.isBlank())) 
        {
            candidatesList = abl.getAllCandidates();
            return;
        }

        // Candidate Name Search
        if (searchCandidateName != null && !searchCandidateName.isBlank()) {
            candidatesList = abl.findByCandidateName(searchCandidateName);
            return;
        }

        // Election Search (ID or Name)
        if (searchElection != null && !searchElection.isBlank()) {
            try {
                int eId = Integer.parseInt(searchElection);
                candidatesList = abl.findByElectionId(eId);
            } catch (Exception ex) {
                // Search by election name (filter)
                candidatesList = abl.getAllCandidates().stream()
                        .filter(c -> c.getElections().getElectionName().toLowerCase()
                        .contains(searchElection.toLowerCase()))
                        .toList();
            }
            return;
        }

        // Party Search (ID or Name)
        if (searchParty != null && !searchParty.isBlank()) {
            try {
                int pId = Integer.parseInt(searchParty);
                candidatesList = abl.findByPartyId(pId);
            } catch (Exception ex) {
                candidatesList = abl.getAllCandidates().stream()
                        .filter(c -> c.getParty().getPartyName()
                        .toLowerCase().contains(searchParty.toLowerCase()))
                        .toList();
            }
        }
    }

    // ================= LOAD FOR UPDATE =================
    public String loadCandidate(int id) {
        Candidates c = abl.findByCandidateId(id);

        candidateId = c.getCandidateId();
        candidateName = c.getCandidateName();
        nominationDate = c.getNominationDate();
        electionId = c.getElections().getElectionId();
        partyId = c.getParty().getPartyId();

        return "CandidatesUpdates.jsf?faces-redirect=true";
    }

    // ================= UPDATE =================
    public String updateCandidate() {
        abl.updateCandidate(candidateId, candidateName, nominationDate, electionId, partyId);
        clearForm();
        return "ManageCandidates.xhtml?faces-redirect=true";
    }

    // ================= CLEAR FORM =================
    public void clearForm() {
        candidateId = null;
        candidateName = "";
        nominationDate = null;
        electionId = null;
        partyId = null;
    }

    // ================ GETTERS & SETTERS =====================
    public Integer getCandidateId() { return candidateId; }
    public void setCandidateId(Integer candidateId) { this.candidateId = candidateId; }

    public String getCandidateName() { return candidateName; }
    public void setCandidateName(String candidateName) { this.candidateName = candidateName; }

    public Date getNominationDate() { return nominationDate; }
    public void setNominationDate(Date nominationDate) { this.nominationDate = nominationDate; }

    public Integer getElectionId() { return electionId; }
    public void setElectionId(Integer electionId) { this.electionId = electionId; }

    public Integer getPartyId() { return partyId; }
    public void setPartyId(Integer partyId) { this.partyId = partyId; }

    public String getSearchCandidateName() { return searchCandidateName; }
    public void setSearchCandidateName(String searchCandidateName) { this.searchCandidateName = searchCandidateName; }

    public String getSearchElection() { return searchElection; }
    public void setSearchElection(String searchElection) { this.searchElection = searchElection; }

    public String getSearchParty() { return searchParty; }
    public void setSearchParty(String searchParty) { this.searchParty = searchParty; }

    public Collection<Candidates> getCandidatesList() { return candidatesList; }
}
