package CdiBean;

import EJB.AdminBeanLocal;
import EJB.UserBeanLocal;
import Entity.Candidates;
import Entity.Elections;
import Entity.Party;
import Entity.Votes;
import Entity.Voters;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Named("votingCDIBean")  // Changed to @Named with value chal rha he bhai chal rha he 
@SessionScoped
public class VotingCDIBean implements Serializable {

    @EJB
    private UserBeanLocal userBean;

    @EJB
    private AdminBeanLocal adminBean;

    // Step 1: Voter ID input
    private Integer voterId;
    private Voters currentVoter;
    private boolean voterVerified = false;
    private String voterMessage;

    // Step 2: Elections list
    private List<Elections> activeElections = new ArrayList<>();
    private Integer selectedElectionId;
    private Elections selectedElection;

    // Step 3: Candidates & Parties for selected election
    private List<Candidates> electionCandidates = new ArrayList<>();
    private List<Party> electionParties = new ArrayList<>();

    // Step 4: Vote casting
    private Integer selectedCandidateId;
    private boolean hasVotedInElection = false;

    public VotingCDIBean() {
        System.out.println("VotingCDIBean Constructor called");
    }

    @PostConstruct
    public void init() {
        System.out.println("VotingCDIBean init() called");
        System.out.println("userBean: " + userBean);
        System.out.println("adminBean: " + adminBean);
        loadActiveElections();
    }

    // Load all active elections
    private void loadActiveElections() {
        System.out.println("Loading active elections...");
        try {
            if (adminBean == null) {
                System.err.println("ERROR: adminBean is null!");
                return;
            }
            
            Collection<Elections> allElections = adminBean.getAllElections();
            System.out.println("Total elections found: " + (allElections != null ? allElections.size() : "null"));
            
            activeElections.clear();
            if (allElections != null) {
                for (Elections election : allElections) {
                    System.out.println("Election: " + election.getElectionName() + " - Status: " + election.getStatus());
                    if ("Active".equalsIgnoreCase(election.getStatus())) {
                        activeElections.add(election);
                    }
                }
            }
            System.out.println("Active elections: " + activeElections.size());
        } catch (Exception e) {
            System.err.println("Error loading elections: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Step 1: Verify voter
    public void verifyVoter() {
        System.out.println("verifyVoter() called with voterId: " + voterId);
        
        voterVerified = false;
        hasVotedInElection = false;
        currentVoter = null;
        selectedElection = null;
        electionCandidates.clear();
        electionParties.clear();

        if (voterId == null) {
            voterMessage = "Please enter your Voter ID";
            System.out.println("Voter ID is null");
            return;
        }

        try {
            if (userBean == null) {
                voterMessage = "System error. Please try again.";
                System.err.println("ERROR: userBean is null!");
                return;
            }

            // Check if voter exists and is approved
            System.out.println("Finding voter by ID: " + voterId);
            currentVoter = userBean.findByVoterId(voterId);
            System.out.println("Voter found: " + (currentVoter != null));
            
            if (currentVoter == null) {
                voterMessage = "Voter ID not found in database.";
                System.out.println("Voter not found");
                return;
            }

            System.out.println("Voter status: " + currentVoter.getStatus());
            if (currentVoter.getStatus() != 1) {
                voterMessage = "Your voter registration is pending approval.";
                System.out.println("Voter not approved");
                return;
            }

            // Check if voter is eligible to vote (age, etc.)
            if (!isEligibleToVote(currentVoter)) {
                voterMessage = "You are not eligible to vote based on age criteria.";
                System.out.println("Voter not eligible by age");
                return;
            }

            voterVerified = true;
            voterMessage = "Voter verified successfully! Please select an election.";
            System.out.println("Voter verified successfully");
            
            // Reset election selection
            selectedElectionId = null;
            loadActiveElections();
            
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Voter verified successfully!"));

        } catch (Exception e) {
            voterMessage = "Error verifying voter. Please try again.";
            System.err.println("Exception in verifyVoter: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean isEligibleToVote(Voters voter) {
        if (voter.getDob() == null) {
            System.out.println("Voter DOB is null");
            return false;
        }
        
        Date dob = voter.getDob();
        Date now = new Date();
        
        // Calculate age
        long ageInMillis = now.getTime() - dob.getTime();
        int age = (int) (ageInMillis / (1000L * 60 * 60 * 24 * 365));
        
        System.out.println("Voter age calculated: " + age);
        return age >= 18;
    }

    // Step 2: Select election
    public void onElectionSelect() {
        System.out.println("onElectionSelect() called with ID: " + selectedElectionId);
        
        if (selectedElectionId == null) {
            System.out.println("Selected election ID is null");
            return;
        }
        
        try {
            // Find selected election
            selectedElection = null;
            for (Elections election : activeElections) {
                if (election.getElectionId().equals(selectedElectionId)) {
                    selectedElection = election;
                    break;
                }
            }
            
            if (selectedElection == null) {
                System.out.println("Selected election not found in activeElections");
                FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Selected election not found!"));
                return;
            }
            
            System.out.println("Selected election: " + selectedElection.getElectionName());
            
            // Check if voter has already voted in this election
            Collection<Votes> existingVotes = userBean.findByElectionIdAndVoterId(
                selectedElectionId, voterId
            );
            
            System.out.println("Existing votes for this election: " + existingVotes.size());
            hasVotedInElection = !existingVotes.isEmpty();
            
            if (hasVotedInElection) {
                System.out.println("Voter has already voted in this election");
                FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Already Voted", 
                    "You have already voted in this election!"));
                return;
            }
            
            // Load candidates and parties for this election
            loadElectionCandidates();
            loadElectionParties();
            
            System.out.println("Candidates loaded: " + electionCandidates.size());
            System.out.println("Parties loaded: " + electionParties.size());
            
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", 
                "Election selected. Now select your candidate."));
                
        } catch (Exception e) {
            System.err.println("Error in onElectionSelect: " + e.getMessage());
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", 
                "Error loading election details."));
        }
    }

    private void loadElectionCandidates() {
        System.out.println("Loading candidates for election ID: " + selectedElectionId);
        try {
            if (adminBean == null) {
                System.err.println("ERROR: adminBean is null in loadElectionCandidates");
                return;
            }
            
            Collection<Candidates> allCandidates = adminBean.getAllCandidates();
            System.out.println("Total candidates: " + (allCandidates != null ? allCandidates.size() : "null"));
            
            electionCandidates.clear();
            
            if (allCandidates != null) {
                for (Candidates candidate : allCandidates) {
                    if (candidate.getElections() != null && 
                        candidate.getElections().getElectionId().equals(selectedElectionId)) {
                        electionCandidates.add(candidate);
                        System.out.println("Added candidate: " + candidate.getCandidateName());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading candidates: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadElectionParties() {
        System.out.println("Loading parties from candidates");
        try {
            electionParties.clear();
            for (Candidates candidate : electionCandidates) {
                Party party = candidate.getParty();
                if (party != null && !electionParties.contains(party)) {
                    electionParties.add(party);
                    System.out.println("Added party: " + party.getPartyName());
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading parties: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Step 3: Cast vote
    public void castVote(Integer candidateId) {
        System.out.println("castVote() called for candidateId: " + candidateId);
        
        if (!voterVerified || selectedElection == null || currentVoter == null) {
            System.out.println("Pre-conditions failed: voterVerified=" + voterVerified + 
                             ", selectedElection=" + selectedElection + 
                             ", currentVoter=" + currentVoter);
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", 
                "Please complete verification and select election first."));
            return;
        }
        
        // Double-check if already voted
        Collection<Votes> existingVotes = userBean.findByElectionIdAndVoterId(
            selectedElectionId, voterId
        );
        
        if (!existingVotes.isEmpty()) {
            System.out.println("Voter has already voted (double-check)");
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", 
                "You have already voted in this election!"));
            hasVotedInElection = true;
            return;
        }
        
        try {
            // Find the selected candidate
            Candidates selectedCandidate = null;
            for (Candidates candidate : electionCandidates) {
                if (candidate.getCandidateId().equals(candidateId)) {
                    selectedCandidate = candidate;
                    break;
                }
            }
            
            if (selectedCandidate == null) {
                System.out.println("Candidate not found with ID: " + candidateId);
                FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", 
                    "Selected candidate not found!"));
                return;
            }
            
            // Cast the vote
            System.out.println("Casting vote for: " + selectedCandidate.getCandidateName());
            userBean.addVote(
                currentVoter.getVoterId(),
                selectedCandidate.getCandidateId(),
                selectedCandidate.getParty().getPartyId(),
                new Date(),
                selectedElection.getElectionId(),
                1, // verified status
                selectedElection.getElectionName()
            );
            
            // Update UI state
            hasVotedInElection = true;
            selectedCandidateId = candidateId;
            
            System.out.println("Vote cast successfully!");
            
            // Success message
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", 
                "Your vote has been cast successfully for " + 
                selectedCandidate.getCandidateName() + " (" + 
                selectedCandidate.getParty().getPartyName() + ")"));
                
            // Reset selection
            selectedCandidateId = null;
            
        } catch (Exception e) {
            System.err.println("Exception in castVote: " + e.getMessage());
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", 
                "Failed to cast vote. Please try again."));
        }
    }

    // Reset everything
    public void resetVotingProcess() {
        System.out.println("resetVotingProcess() called");
        voterId = null;
        currentVoter = null;
        voterVerified = false;
        voterMessage = null;
        selectedElectionId = null;
        selectedElection = null;
        electionCandidates.clear();
        electionParties.clear();
        selectedCandidateId = null;
        hasVotedInElection = false;
        loadActiveElections();
    }

    // Getters and Setters
    public Integer getVoterId() { return voterId; }
    public void setVoterId(Integer voterId) { 
        System.out.println("setVoterId called: " + voterId);
        this.voterId = voterId; 
    }
    
    public Voters getCurrentVoter() { return currentVoter; }
    public boolean isVoterVerified() { return voterVerified; }
    public String getVoterMessage() { return voterMessage; }
    public List<Elections> getActiveElections() { return activeElections; }
    
    public Integer getSelectedElectionId() { return selectedElectionId; }
    public void setSelectedElectionId(Integer selectedElectionId) { 
        System.out.println("setSelectedElectionId called: " + selectedElectionId);
        this.selectedElectionId = selectedElectionId; 
    }
    
    public Elections getSelectedElection() { return selectedElection; }
    public List<Candidates> getElectionCandidates() { return electionCandidates; }
    public List<Party> getElectionParties() { return electionParties; }
    public boolean isHasVotedInElection() { return hasVotedInElection; }
    
    public String getVotingRules() {
        return "1. Only registered voters with approved status can vote.\n" + "2. Each voter can vote only once per election.\n" + "3. Voter ID is required for authentication.\n" + "4. Vote cannot be changed once submitted.\n" + "5. Voting time: 7:00 AM to 6:00 PM.\n" + "6. Results will be declared after voting ends.\n" + "7. Any malpractice will lead to legal action.\n";
    }
    
    public Integer getSelectedCandidateId() { return selectedCandidateId; }
    public void setSelectedCandidateId(Integer selectedCandidateId) { 
        this.selectedCandidateId = selectedCandidateId; 
    }
}