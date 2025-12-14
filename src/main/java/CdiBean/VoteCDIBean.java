// UPDATED VoteCDIBean.java - SessionScoped + AUTO REAL-TIME REFRESH
package CdiBean;

import EJB.AdminBeanLocal;
import Entity.Candidates;
import Entity.Elections;
import Entity.Party;
import Entity.Votes;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Named(value = "voteCDIBean")
@SessionScoped
public class VoteCDIBean implements Serializable {
    
    @EJB
    private AdminBeanLocal abl;
    
    private Integer selectedElectionId;
    private Elections selectedElection;
    private Collection<Elections> allElections;
    
    // Results data
    private List<ElectionResultDTO> electionResults;
    private List<PartyWiseResultDTO> partyWiseResults;
    private List<CandidateWiseResultDTO> candidateWiseResults;
    
    // Summary stats
    private int totalVotesInElection;
    private int totalCandidates;
    private int totalParties;
    private CandidateWiseResultDTO winnerCandidate;
    private PartyWiseResultDTO winningParty;
    
    // TIMER FOR AUTO REFRESH - KEY FIX
    private jakarta.faces.context.ExternalContext externalContext;
    
    // DTO Classes (SAME AS YOURS)
    public static class ElectionResultDTO {
        private Integer electionId;
        private String electionName;
        private int totalVotes;
        private String status;
        private Date startDate;
        private Date endDate;
        
        public ElectionResultDTO() {}
        
        public ElectionResultDTO(Integer electionId, String electionName, int totalVotes, String status, 
                               Date startDate, Date endDate) {
            this.electionId = electionId;
            this.electionName = electionName;
            this.totalVotes = totalVotes;
            this.status = status;
            this.startDate = startDate;
            this.endDate = endDate;
        }
        
        // Getters and Setters (SAME AS YOURS)
        public Integer getElectionId() { return electionId; }
        public void setElectionId(Integer electionId) { this.electionId = electionId; }
        public String getElectionName() { return electionName; }
        public void setElectionName(String electionName) { this.electionName = electionName; }
        public int getTotalVotes() { return totalVotes; }
        public void setTotalVotes(int totalVotes) { this.totalVotes = totalVotes; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public Date getStartDate() { return startDate; }
        public void setStartDate(Date startDate) { this.startDate = startDate; }
        public Date getEndDate() { return endDate; }
        public void setEndDate(Date endDate) { this.endDate = endDate; }
    }
    
    // PartyWiseResultDTO and CandidateWiseResultDTO SAME AS YOURS - NO CHANGE
    public static class PartyWiseResultDTO {
        private Integer partyId;
        private String partyName;
        private String partySymbol;
        private String leaderName;
        private int totalVotes;
        private double votePercentage;
        private int totalCandidates;
        
        public PartyWiseResultDTO() {}
        
        public PartyWiseResultDTO(Integer partyId, String partyName, String partySymbol, String leaderName, 
                                int totalVotes, double votePercentage, int totalCandidates) {
            this.partyId = partyId;
            this.partyName = partyName;
            this.partySymbol = partySymbol;
            this.leaderName = leaderName;
            this.totalVotes = totalVotes;
            this.votePercentage = votePercentage;
            this.totalCandidates = totalCandidates;
        }
        
        // Getters and Setters SAME AS YOURS
        public Integer getPartyId() { return partyId; }
        public void setPartyId(Integer partyId) { this.partyId = partyId; }
        public String getPartyName() { return partyName; }
        public void setPartyName(String partyName) { this.partyName = partyName; }
        public String getPartySymbol() { return partySymbol; }
        public void setPartySymbol(String partySymbol) { this.partySymbol = partySymbol; }
        public String getLeaderName() { return leaderName; }
        public void setLeaderName(String leaderName) { this.leaderName = leaderName; }
        public int getTotalVotes() { return totalVotes; }
        public void setTotalVotes(int totalVotes) { this.totalVotes = totalVotes; }
        public double getVotePercentage() { return votePercentage; }
        public void setVotePercentage(double votePercentage) { this.votePercentage = votePercentage; }
        public int getTotalCandidates() { return totalCandidates; }
        public void setTotalCandidates(int totalCandidates) { this.totalCandidates = totalCandidates; }
    }
    
    public static class CandidateWiseResultDTO {
        private Integer candidateId;
        private String candidateName;
        private Integer partyId;
        private String partyName;
        private String partySymbol;
        private int totalVotes;
        private double votePercentage;
        private String resultStatus;
        
        public CandidateWiseResultDTO() {}
        
        public CandidateWiseResultDTO(Integer candidateId, String candidateName, Integer partyId, 
                                    String partyName, String partySymbol, int totalVotes, 
                                    double votePercentage, String resultStatus) {
            this.candidateId = candidateId;
            this.candidateName = candidateName;
            this.partyId = partyId;
            this.partyName = partyName;
            this.partySymbol = partySymbol;
            this.totalVotes = totalVotes;
            this.votePercentage = votePercentage;
            this.resultStatus = resultStatus;
        }
        
        // Getters and Setters SAME AS YOURS
        public Integer getCandidateId() { return candidateId; }
        public void setCandidateId(Integer candidateId) { this.candidateId = candidateId; }
        public String getCandidateName() { return candidateName; }
        public void setCandidateName(String candidateName) { this.candidateName = candidateName; }
        public Integer getPartyId() { return partyId; }
        public void setPartyId(Integer partyId) { this.partyId = partyId; }
        public String getPartyName() { return partyName; }
        public void setPartyName(String partyName) { this.partyName = partyName; }
        public String getPartySymbol() { return partySymbol; }
        public void setPartySymbol(String partySymbol) { this.partySymbol = partySymbol; }
        public int getTotalVotes() { return totalVotes; }
        public void setTotalVotes(int totalVotes) { this.totalVotes = totalVotes; }
        public double getVotePercentage() { return votePercentage; }
        public void setVotePercentage(double votePercentage) { this.votePercentage = votePercentage; }
        public String getResultStatus() { return resultStatus; }
        public void setResultStatus(String resultStatus) { this.resultStatus = resultStatus; }
    }
    
    @PostConstruct
    public void init() {
        loadAllElections();
        externalContext = FacesContext.getCurrentInstance().getExternalContext();
    }
    
    public void loadAllElections() {
        try {
            allElections = abl.getAllElections();  // FRESH DATA EVERY TIME
            System.out.println("Total elections loaded: " + (allElections != null ? allElections.size() : 0));
        } catch (Exception e) {
            System.err.println("Error loading elections: " + e.getMessage());
            allElections = new ArrayList<>();
        }
    }
    
    // 🔥 MAIN FIX - AUTO REFRESH METHOD
    public void refreshLiveResults() {
        if (selectedElectionId != null) {
            calculateResults();  // RELOAD FRESH DATA
        }
    }
    
    public void calculateResults() {
        if (selectedElectionId == null) {
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning", "Please select an election first!"));
            return;
        }
        
        try {
            // 🔥 KEY FIX: RELOAD FRESH ELECTIONS EVERY TIME
            loadAllElections();
            
            selectedElection = null;
            for (Elections election : allElections) {
                if (election.getElectionId().equals(selectedElectionId)) {
                    selectedElection = election;
                    break;
                }
            }
            
            if (selectedElection == null) {
                FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Selected election not found!"));
                return;
            }
            
            System.out.println("Calculating results for election: " + selectedElection.getElectionName() + 
                             " | Status: " + selectedElection.getStatus());
            
            String status = selectedElection.getStatus().toLowerCase();
            
            clearResults();
            
            if ("upcoming".equals(status)) {
                FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Election abhi start nahi hua hai!"));
                return;
            }
            
            if ("active".equals(status)) {
                calculatePartialResults();
                return;
            }
            
            if ("complete".equals(status)) {
                calculateCompleteResults();
                return;
            }
            
            clearResults();
            
        } catch (Exception e) {
            System.err.println("Error calculating results: " + e.getMessage());
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to calculate results: " + e.getMessage()));
        }
    }
    
    // ALL OTHER METHODS SAME AS YOURS - NO CHANGE
    private void calculatePartialResults() {
        Collection<Votes> allVotes = abl.getAllVotes();
        Collection<Candidates> allCandidates = abl.getAllCandidates();
        Collection<Party> allParties = abl.getAllParties();
        
        List<Votes> electionVotes = allVotes.stream()
            .filter(vote -> vote.getElections() != null && 
                          vote.getElections().getElectionId().equals(selectedElectionId))
            .collect(Collectors.toList());
        
        totalVotesInElection = electionVotes.size();
        
        calculateCandidateWiseResults(electionVotes, allCandidates, allParties, false);
        calculatePartyWiseResults(electionVotes, allCandidates, allParties);
        calculateElectionSummary();
    }
    
    private void calculateCompleteResults() {
        Collection<Votes> allVotes = abl.getAllVotes();
        Collection<Candidates> allCandidates = abl.getAllCandidates();
        Collection<Party> allParties = abl.getAllParties();
        
        List<Votes> electionVotes = allVotes.stream()
            .filter(vote -> vote.getElections() != null && 
                          vote.getElections().getElectionId().equals(selectedElectionId))
            .collect(Collectors.toList());
        
        totalVotesInElection = electionVotes.size();
        
        calculateCandidateWiseResults(electionVotes, allCandidates, allParties, true);
        calculatePartyWiseResults(electionVotes, allCandidates, allParties);
        findWinner();
        calculateElectionSummary();
        
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", 
                "Results calculated successfully for " + selectedElection.getElectionName()));
    }
    
    // REST OF METHODS EXACTLY SAME AS YOUR ORIGINAL CODE
    private void calculateCandidateWiseResults(List<Votes> electionVotes, Collection<Candidates> allCandidates, 
                                            Collection<Party> allParties, boolean assignWinners) {
        candidateWiseResults = new ArrayList<>();
        
        List<Candidates> electionCandidates = allCandidates.stream()
            .filter(candidate -> candidate.getElections() != null && 
                               candidate.getElections().getElectionId().equals(selectedElectionId))
            .collect(Collectors.toList());
        
        totalCandidates = electionCandidates.size();
        
        for (Candidates candidate : electionCandidates) {
            long voteCount = electionVotes.stream()
                .filter(vote -> vote.getCandidates() != null && 
                              vote.getCandidates().getCandidateId().equals(candidate.getCandidateId()))
                .count();
            
            Party party = candidate.getParty();
            String partyName = party != null ? party.getPartyName() : "Independent";
            String partySymbol = party != null ? party.getPartySymbol() : "";
            
            double votePercentage = totalVotesInElection > 0 ? 
                (voteCount * 100.0 / totalVotesInElection) : 0;
            
            CandidateWiseResultDTO dto = new CandidateWiseResultDTO(
                candidate.getCandidateId(), candidate.getCandidateName(),
                party != null ? party.getPartyId() : null, partyName, partySymbol,
                (int) voteCount, Math.round(votePercentage * 100.0) / 100.0,
                "CONTESTING"
            );
            
            candidateWiseResults.add(dto);
        }
        
        candidateWiseResults.sort((c1, c2) -> Integer.compare(c2.getTotalVotes(), c1.getTotalVotes()));
        
        if (assignWinners && !candidateWiseResults.isEmpty()) {
            candidateWiseResults.get(0).setResultStatus("WINNER");
            if (candidateWiseResults.size() > 1) {
                candidateWiseResults.get(1).setResultStatus("RUNNER_UP");
            }
        }
    }
    
    private void calculatePartyWiseResults(List<Votes> electionVotes, Collection<Candidates> allCandidates, 
                                        Collection<Party> allParties) {
        partyWiseResults = new ArrayList<>();
        
        List<Candidates> electionCandidates = allCandidates.stream()
            .filter(candidate -> candidate.getElections() != null && 
                               candidate.getElections().getElectionId().equals(selectedElectionId))
            .collect(Collectors.toList());
        
        for (Party party : allParties) {
            int votes = 0;
            int candidates = 0;
            
            for (Votes vote : electionVotes) {
                if (vote.getParty() != null && vote.getParty().getPartyId().equals(party.getPartyId())) {
                    votes++;
                }
            }
            
            for (Candidates candidate : electionCandidates) {
                if (candidate.getParty() != null && candidate.getParty().getPartyId().equals(party.getPartyId())) {
                    candidates++;
                }
            }
            
            if (candidates > 0) {
                double votePercentage = totalVotesInElection > 0 ? 
                    (votes * 100.0 / totalVotesInElection) : 0;
                
                PartyWiseResultDTO dto = new PartyWiseResultDTO(
                    party.getPartyId(), party.getPartyName(), party.getPartySymbol(),
                    party.getLeaderName(), votes, Math.round(votePercentage * 100.0) / 100.0, candidates
                );
                partyWiseResults.add(dto);
            }
        }
        
        partyWiseResults.sort((p1, p2) -> Integer.compare(p2.getTotalVotes(), p1.getTotalVotes()));
        totalParties = partyWiseResults.size();
    }
    
    private void findWinner() {
        if (!candidateWiseResults.isEmpty()) {
            winnerCandidate = candidateWiseResults.get(0);
        }
        if (!partyWiseResults.isEmpty()) {
            winningParty = partyWiseResults.get(0);
        }
    }
    
    private void calculateElectionSummary() {
        electionResults = new ArrayList<>();
        ElectionResultDTO dto = new ElectionResultDTO(
            selectedElection.getElectionId(), selectedElection.getElectionName(),
            totalVotesInElection, selectedElection.getStatus(),
            selectedElection.getStartDate(), selectedElection.getEndDate()
        );
        electionResults.add(dto);
    }
    
    private void clearResults() {
        electionResults = new ArrayList<>();
        partyWiseResults = new ArrayList<>();
        candidateWiseResults = new ArrayList<>();
        totalVotesInElection = 0;
        totalCandidates = 0;
        totalParties = 0;
        winnerCandidate = null;
        winningParty = null;
    }
    
    // ALL OTHER METHODS SAME AS YOURS
    public void resetResults() {
        selectedElectionId = null;
        selectedElection = null;
        clearResults();
    }
    
    public void exportToPDF() {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Export feature will be implemented soon!"));
    }
    
    public boolean isElectionUpcoming() {
        return selectedElection != null && "upcoming".equalsIgnoreCase(selectedElection.getStatus());
    }
    
    public boolean isElectionActive() {
        return selectedElection != null && "active".equalsIgnoreCase(selectedElection.getStatus());
    }
    
    public boolean isElectionComplete() {
        return selectedElection != null && "complete".equalsIgnoreCase(selectedElection.getStatus());
    }
    
    public boolean isResultsAvailable() {
        return candidateWiseResults != null && !candidateWiseResults.isEmpty();
    }
    
    // Getters and Setters SAME AS YOURS
    public Integer getSelectedElectionId() { return selectedElectionId; }
    public void setSelectedElectionId(Integer selectedElectionId) { this.selectedElectionId = selectedElectionId; }
    public Elections getSelectedElection() { return selectedElection; }
    public Collection<Elections> getAllElections() { return allElections; }
    public List<ElectionResultDTO> getElectionResults() { return electionResults; }
    public List<PartyWiseResultDTO> getPartyWiseResults() { return partyWiseResults; }
    public List<CandidateWiseResultDTO> getCandidateWiseResults() { return candidateWiseResults; }
    public int getTotalVotesInElection() { return totalVotesInElection; }
    public int getTotalCandidates() { return totalCandidates; }
    public int getTotalParties() { return totalParties; }
    public CandidateWiseResultDTO getWinnerCandidate() { return winnerCandidate; }
    public PartyWiseResultDTO getWinningParty() { return winningParty; }
}
