package CdiBean;

import EJB.UserBeanLocal;
import Entity.Voters;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Named("voterModule")
@SessionScoped
public class VoterModuleBean implements Serializable {

    @Inject
    private UserBeanLocal userBean;

    private List<Voters> voters = new ArrayList<>();         // All voters
    private List<Voters> approvedList = new ArrayList<>();   // Approved voters only
    private String alertMessage;

    // 🔹 Search fields (for approved voters)
    private String searchName;
    private String searchCity;
    private Integer searchId;
    private Date startDate;
    private Date endDate;

    @PostConstruct
    public void init() {
        refreshList();          // Load all voters
        loadApprovedVoters();   // Load approved voters
    }

    // ------------------- LOAD ALL VOTERS -------------------
    public void refreshList() {
        voters = new ArrayList<>(userBean.getAllVoters());
    }

    // ------------------- LOAD ONLY APPROVED VOTERS -------------------
    public void loadApprovedVoters() {
        approvedList = new ArrayList<>(userBean.findByStatus(1)); // status=1 means approved
    }

    // ------------------- APPROVE -------------------
    public void approve(int voterId) {
        Voters v = userBean.findByVoterId(voterId);
        if (v != null) {
            v.setStatus(1);
            v.setIssueDate(new Date());

            userBean.updateVoter(
                    v.getVoterId(), v.getVoterName(), v.getMobileNumber(),
                    v.getAdharNumber(), v.getEmailId(), v.getDob(),
                    v.getCity(), v.getPincode(), v.getAddress(),
                    v.getAdharFile(), v.getVoterImage(),
                    v.getStatus(), v.getIssueDate()
            );

            alertMessage = "Requests Approved Successfully!";
            refreshList();
            loadApprovedVoters();
        }
    }

    // ------------------- REJECT -------------------
//    public void reject(int voterId) {
//        Voters v = userBean.findByVoterId(voterId);
//        if (v != null) {
//            v.setStatus(0);          // Reject sets status to 0
//            v.setIssueDate(null);    // Clear issue date
//
//            userBean.updateVoter(
//                    v.getVoterId(), v.getVoterName(), v.getMobileNumber(),
//                    v.getAdharNumber(), v.getEmailId(), v.getDob(),
//                    v.getCity(), v.getPincode(), v.getAddress(),
//                    v.getAdharFile(), v.getVoterImage(),
//                    v.getStatus(), v.getIssueDate()
//            );
//
//            alertMessage = "Voter Rejected!";
//            refreshList();
//            loadApprovedVoters();
//        }
//    }
    public void reject(int voterId) {
    Voters v = userBean.findByVoterId(voterId);
    if (v != null) {

        // 🔥 Delete record from DB
        userBean.deleteVoter(voterId);

        alertMessage = "Requests Rejected Successfully!";
        refreshList();
        loadApprovedVoters();
    }
}


    // ------------------- SEARCH LOGIC (Approved voters only) -------------------
    public void searchVoters() {
        List<Voters> results = new ArrayList<>(userBean.findByStatus(1)); // only approved
        List<Voters> filtered = new ArrayList<>();

        for (Voters v : results) {
            boolean match = true;

            if (searchId != null && !v.getVoterId().equals(searchId)) match = false;
            if (searchName != null && !searchName.isEmpty() &&
                !v.getVoterName().toLowerCase().contains(searchName.toLowerCase())) match = false;
            if (searchCity != null && !searchCity.isEmpty() &&
                !v.getCity().toLowerCase().contains(searchCity.toLowerCase())) match = false;
            if (startDate != null && v.getIssueDate() != null && v.getIssueDate().before(startDate)) match = false;
            if (endDate != null && v.getIssueDate() != null && v.getIssueDate().after(endDate)) match = false;

            if (match) filtered.add(v);
        }

        approvedList = filtered;
    }
    
//    public void searchCard() {
//    approvedList = new ArrayList<>();
//
//    if (searchId != null) {
//        Voters v = userBean.findByVoterId(searchId);
//
//        if (v != null && v.getStatus() == 0) {
//            approvedList.add(v);  // only 1 card
//        }
//    }
//}


    public void clearSearch() {
        searchId = null;
        searchName = null;
        searchCity = null;
        startDate = null;
        endDate = null;
        loadApprovedVoters();
    }

    // ------------------- GETTERS / SETTERS -------------------
    public List<Voters> getVoters() { return voters; }
    public List<Voters> getApprovedList() { return approvedList; }
    public String getAlertMessage() { return alertMessage; }
    public void clearAlert() { alertMessage = null; }

    public String getSearchName() { return searchName; }
    public void setSearchName(String searchName) { this.searchName = searchName; }
    public String getSearchCity() { return searchCity; }
    public void setSearchCity(String searchCity) { this.searchCity = searchCity; }
    public Integer getSearchId() { return searchId; }
    public void setSearchId(Integer searchId) { this.searchId = searchId; }
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
}
