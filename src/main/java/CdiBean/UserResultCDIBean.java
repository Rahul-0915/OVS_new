package CdiBean;

import EJB.AdminBeanLocal;
import Entity.Candidates;
import Entity.Elections;
import Entity.Votes;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named(value = "userResultCDIBean")
@SessionScoped
public class UserResultCDIBean implements Serializable {

    @EJB
    private AdminBeanLocal abl;

    private List<Elections> completedElections;
    private Elections selectedElection;
    private List<SimpleCandidateResult> candidateResults;
    private List<SimplePartyResult> partyResults;
    private String winnerName;
    private String winnerParty;

    @PostConstruct
    public void init() {
        loadCompletedElections();
    }

    public void loadCompletedElections() {
        try {
            List<Elections> allElections = new ArrayList<>(abl.getAllElections());
            completedElections = new ArrayList<>();
            
            for (Elections election : allElections) {
                if ("Completed".equalsIgnoreCase(election.getStatus()) || 
                    "Result Announced".equalsIgnoreCase(election.getStatus())) {
                    completedElections.add(election);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            completedElections = new ArrayList<>();
        }
    }

    public void showResult(int electionId) {
        try {
            // Find selected election
            for (Elections election : completedElections) {
                if (election.getElectionId() == electionId) {
                    selectedElection = election;
                    break;
                }
            }
            
            if (selectedElection == null) {
                return;
            }
            
            // Calculate results
            calculateResults(selectedElection);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void calculateResults(Elections election) {
        try {
            // Get all votes for this election
            List<Votes> allVotes = new ArrayList<>(abl.getAllVotes());
            List<Candidates> allCandidates = new ArrayList<>(abl.getAllCandidates());
            
            // Filter votes for this election
            List<Votes> electionVotes = new ArrayList<>();
            for (Votes vote : allVotes) {
                if (vote.getElections() != null && 
                    vote.getElections().getElectionId() == election.getElectionId()) {
                    electionVotes.add(vote);
                }
            }
            
            // Calculate candidate results
            candidateResults = new ArrayList<>();
            for (Candidates candidate : allCandidates) {
                if (candidate.getElections() != null && 
                    candidate.getElections().getElectionId() == election.getElectionId()) {
                    
                    SimpleCandidateResult result = new SimpleCandidateResult();
                    result.candidateId = candidate.getCandidateId();
                    result.candidateName = candidate.getCandidateName();
                    result.partyName = candidate.getParty() != null ? 
                                     candidate.getParty().getPartyName() : "Independent";
                    
                    // Count votes
                    int voteCount = 0;
                    for (Votes vote : electionVotes) {
                        if (vote.getCandidates() != null && 
                            vote.getCandidates().getCandidateId() == candidate.getCandidateId()) {
                            voteCount++;
                        }
                    }
                    
                    result.votes = voteCount;
                    result.percentage = electionVotes.size() > 0 ? 
                                      (voteCount * 100.0) / electionVotes.size() : 0;
                    
                    candidateResults.add(result);
                }
            }
            
            // Sort by votes (highest first)
            candidateResults.sort((c1, c2) -> Integer.compare(c2.votes, c1.votes));
            
            // Set winner
            if (!candidateResults.isEmpty()) {
                winnerName = candidateResults.get(0).candidateName;
                winnerParty = candidateResults.get(0).partyName;
            }
            
            // Calculate party results
            calculatePartyResults(electionVotes);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void calculatePartyResults(List<Votes> electionVotes) {
        partyResults = new ArrayList<>();
        
        // Count votes per party
        for (Votes vote : electionVotes) {
            if (vote.getParty() != null) {
                String partyName = vote.getParty().getPartyName();
                
                // Find if party already exists in results
                SimplePartyResult existingParty = null;
                for (SimplePartyResult partyResult : partyResults) {
                    if (partyResult.partyName.equals(partyName)) {
                        existingParty = partyResult;
                        break;
                    }
                }
                
                if (existingParty != null) {
                    existingParty.votes++;
                } else {
                    SimplePartyResult newParty = new SimplePartyResult();
                    newParty.partyName = partyName;
                    newParty.votes = 1;
                    partyResults.add(newParty);
                }
            }
        }
        
        // Calculate percentages
        int totalVotes = electionVotes.size();
        for (SimplePartyResult party : partyResults) {
            party.percentage = totalVotes > 0 ? (party.votes * 100.0) / totalVotes : 0;
        }
        
        // Sort by votes
        partyResults.sort((p1, p2) -> Integer.compare(p2.votes, p1.votes));
    }

    // Simple DTO classes
    public class SimpleCandidateResult {
        public int candidateId;
        public String candidateName;
        public String partyName;
        public int votes;
        public double percentage;
        
        // Getters
        public int getCandidateId() { return candidateId; }
        public String getCandidateName() { return candidateName; }
        public String getPartyName() { return partyName; }
        public int getVotes() { return votes; }
        public double getPercentage() { 
            return Math.round(percentage * 100.0) / 100.0; 
        }
    }

    public class SimplePartyResult {
        public String partyName;
        public int votes;
        public double percentage;
        
        // Getters
        public String getPartyName() { return partyName; }
        public int getVotes() { return votes; }
        public double getPercentage() { 
            return Math.round(percentage * 100.0) / 100.0; 
        }
    }

    // Getters
    public List<Elections> getCompletedElections() { return completedElections; }
    public Elections getSelectedElection() { return selectedElection; }
    public List<SimpleCandidateResult> getCandidateResults() { return candidateResults; }
    public List<SimplePartyResult> getPartyResults() { return partyResults; }
    public String getWinnerName() { return winnerName; }
    public String getWinnerParty() { return winnerParty; }
}