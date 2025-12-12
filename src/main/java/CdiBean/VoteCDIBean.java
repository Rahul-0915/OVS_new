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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Named(value = "voteCDIBean")
@SessionScoped
public class VoteCDIBean implements Serializable {

    @EJB
    private AdminBeanLocal abl;

    // Election selection
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

    // DTO Classes
    public class ElectionResultDTO {
        private Integer electionId;
        private String electionName;
        private int totalVotes;
        private String status;
        private Date startDate;
        private Date endDate;
        
        // Constructors, getters, setters
        public ElectionResultDTO() {}
        
        public ElectionResultDTO(Integer electionId, String electionName, int totalVotes, 
                               String status, Date startDate, Date endDate) {
            this.electionId = electionId;
            this.electionName = electionName;
            this.totalVotes = totalVotes;
            this.status = status;
            this.startDate = startDate;
            this.endDate = endDate;
        }
        
        // Getters and setters
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

    public class PartyWiseResultDTO {
        private Integer partyId;
        private String partyName;
        private String partySymbol;
        private String leaderName;
        private int totalVotes;
        private double votePercentage;
        private int totalCandidates;
        
        // Constructors, getters, setters
        public PartyWiseResultDTO() {}
        
        public PartyWiseResultDTO(Integer partyId, String partyName, String partySymbol, 
                                String leaderName, int totalVotes, double votePercentage, 
                                int totalCandidates) {
            this.partyId = partyId;
            this.partyName = partyName;
            this.partySymbol = partySymbol;
            this.leaderName = leaderName;
            this.totalVotes = totalVotes;
            this.votePercentage = votePercentage;
            this.totalCandidates = totalCandidates;
        }
        
        // Getters and setters
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

    public class CandidateWiseResultDTO {
        private Integer candidateId;
        private String candidateName;
        private Integer partyId;
        private String partyName;
        private String partySymbol;
        private int totalVotes;
        private double votePercentage;
        private String resultStatus; // WINNER, RUNNER_UP, etc.
        
        // Constructors, getters, setters
        public CandidateWiseResultDTO() {}
        
        public CandidateWiseResultDTO(Integer candidateId, String candidateName, 
                                    Integer partyId, String partyName, String partySymbol, 
                                    int totalVotes, double votePercentage, String resultStatus) {
            this.candidateId = candidateId;
            this.candidateName = candidateName;
            this.partyId = partyId;
            this.partyName = partyName;
            this.partySymbol = partySymbol;
            this.totalVotes = totalVotes;
            this.votePercentage = votePercentage;
            this.resultStatus = resultStatus;
        }
        
        // Getters and setters
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
    }

    // Load all elections for dropdown
    public void loadAllElections() {
        try {
            allElections = abl.getAllElections();
            System.out.println("Total elections loaded: " + (allElections != null ? allElections.size() : 0));
        } catch (Exception e) {
            System.err.println("Error loading elections: " + e.getMessage());
            allElections = new ArrayList<>();
        }
    }

    // Main method to calculate results
    public void calculateResults() {
        if (selectedElectionId == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning", 
                "Please select an election first!"));
            return;
        }

        try {
            // Find selected election
            selectedElection = null;
            for (Elections election : allElections) {
                if (election.getElectionId().equals(selectedElectionId)) {
                    selectedElection = election;
                    break;
                }
            }

            if (selectedElection == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", 
                    "Selected election not found!"));
                return;
            }

            System.out.println("Calculating results for election: " + selectedElection.getElectionName());

            // Get all votes for this election
            Collection<Votes> allVotes = abl.getAllVotes();
            Collection<Candidates> allCandidates = abl.getAllCandidates();
            Collection<Party> allParties = abl.getAllParties();

            // Filter votes for selected election
            List<Votes> electionVotes = allVotes.stream()
                .filter(vote -> vote.getElections() != null && 
                        vote.getElections().getElectionId().equals(selectedElectionId))
                .collect(Collectors.toList());

            totalVotesInElection = electionVotes.size();

            // Calculate candidate-wise results
            calculateCandidateWiseResults(electionVotes, allCandidates, allParties);

            // Calculate party-wise results
            calculatePartyWiseResults(electionVotes, allCandidates, allParties);

            // Find winner
            findWinner();

            // Calculate election summary
            calculateElectionSummary();

            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", 
                "Results calculated successfully for " + selectedElection.getElectionName()));

        } catch (Exception e) {
            System.err.println("Error calculating results: " + e.getMessage());
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", 
                "Failed to calculate results: " + e.getMessage()));
        }
    }

    private void calculateCandidateWiseResults(List<Votes> electionVotes, 
                                              Collection<Candidates> allCandidates,
                                              Collection<Party> allParties) {
        candidateWiseResults = new ArrayList<>();
        
        // Get all candidates for this election
        List<Candidates> electionCandidates = allCandidates.stream()
            .filter(candidate -> candidate.getElections() != null && 
                    candidate.getElections().getElectionId().equals(selectedElectionId))
            .collect(Collectors.toList());
        
        totalCandidates = electionCandidates.size();
        
        // Count votes for each candidate
        for (Candidates candidate : electionCandidates) {
            int voteCount = (int) electionVotes.stream()
                .filter(vote -> vote.getCandidates() != null && 
                        vote.getCandidates().getCandidateId().equals(candidate.getCandidateId()))
                .count();
            
            // Get party details
            Party party = candidate.getParty();
            String partyName = party != null ? party.getPartyName() : "Independent";
            String partySymbol = party != null ? party.getPartySymbol() : "";
            
            double votePercentage = totalVotesInElection > 0 ? 
                (voteCount * 100.0) / totalVotesInElection : 0;
            
            CandidateWiseResultDTO dto = new CandidateWiseResultDTO(
                candidate.getCandidateId(),
                candidate.getCandidateName(),
                party != null ? party.getPartyId() : null,
                partyName,
                partySymbol,
                voteCount,
                Math.round(votePercentage * 100.0) / 100.0, // Round to 2 decimals
                "CONTESTING"
            );
            
            candidateWiseResults.add(dto);
        }
        
        // Sort by votes in descending order
        candidateWiseResults.sort((c1, c2) -> Integer.compare(c2.getTotalVotes(), c1.getTotalVotes()));
        
        // Set result status
        if (!candidateWiseResults.isEmpty()) {
            candidateWiseResults.get(0).setResultStatus("WINNER");
        }
        if (candidateWiseResults.size() > 1) {
            candidateWiseResults.get(1).setResultStatus("RUNNER UP");
        }
    }

    private void calculatePartyWiseResults(List<Votes> electionVotes,
                                          Collection<Candidates> allCandidates,
                                          Collection<Party> allParties) {
        partyWiseResults = new ArrayList<>();
        
        // Map partyId to vote count
        Map<Integer, Integer> partyVoteCount = new HashMap<>();
        Map<Integer, Integer> partyCandidateCount = new HashMap<>();
        
        // Initialize all parties
        for (Party party : allParties) {
            partyVoteCount.put(party.getPartyId(), 0);
            partyCandidateCount.put(party.getPartyId(), 0);
        }
        
        // Count votes and candidates per party
        for (Votes vote : electionVotes) {
            if (vote.getParty() != null) {
                Integer partyId = vote.getParty().getPartyId();
                partyVoteCount.put(partyId, partyVoteCount.get(partyId) + 1);
            }
        }
        
        // Count candidates per party for this election
        List<Candidates> electionCandidates = allCandidates.stream()
            .filter(candidate -> candidate.getElections() != null && 
                    candidate.getElections().getElectionId().equals(selectedElectionId))
            .collect(Collectors.toList());
        
        for (Candidates candidate : electionCandidates) {
            if (candidate.getParty() != null) {
                Integer partyId = candidate.getParty().getPartyId();
                partyCandidateCount.put(partyId, partyCandidateCount.get(partyId) + 1);
            }
        }
        
        // Create DTOs
        for (Party party : allParties) {
            int votes = partyVoteCount.get(party.getPartyId());
            int candidates = partyCandidateCount.get(party.getPartyId());
            
            if (candidates > 0) { // Only show parties with candidates in this election
                double votePercentage = totalVotesInElection > 0 ? 
                    (votes * 100.0) / totalVotesInElection : 0;
                
                PartyWiseResultDTO dto = new PartyWiseResultDTO(
                    party.getPartyId(),
                    party.getPartyName(),
                    party.getPartySymbol(),
                    party.getLeaderName(),
                    votes,
                    Math.round(votePercentage * 100.0) / 100.0,
                    candidates
                );
                
                partyWiseResults.add(dto);
            }
        }
        
        // Sort by votes in descending order
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
        // Create election result DTO
        electionResults = new ArrayList<>();
        ElectionResultDTO dto = new ElectionResultDTO(
            selectedElection.getElectionId(),
            selectedElection.getElectionName(),
            totalVotesInElection,
            selectedElection.getStatus(),
            selectedElection.getStartDate(),
            selectedElection.getEndDate()
        );
        electionResults.add(dto);
    }

    // Export results to PDF/Excel (stub method)
    public void exportToPDF() {
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", 
            "Export feature will be implemented soon!"));
    }

    // Reset all results
    public void resetResults() {
        selectedElectionId = null;
        selectedElection = null;
        electionResults = null;
        partyWiseResults = null;
        candidateWiseResults = null;
        totalVotesInElection = 0;
        totalCandidates = 0;
        totalParties = 0;
        winnerCandidate = null;
        winningParty = null;
    }

    // Check if results are available
    public boolean isResultsAvailable() {
        return candidateWiseResults != null && !candidateWiseResults.isEmpty();
    }

    // Getters and Setters
    public Integer getSelectedElectionId() { return selectedElectionId; }
    public void setSelectedElectionId(Integer selectedElectionId) { 
        this.selectedElectionId = selectedElectionId; 
    }
    
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