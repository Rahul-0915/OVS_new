package CdiBean;

import EJB.AdminBeanLocal;
import Entity.Elections;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Named(value = "adminCDIBean")
@SessionScoped
public class AdminCDIBean implements Serializable {

    @EJB
    private AdminBeanLocal abl;

    // Fields bound to JSF inputs
    private Integer eId;
    private Date startDate;
    private Date endDate;
    private String electionName;
    private String status;
    private String desc;

    private Collection<Elections> electionList; // Table binding
    private Collection<Elections> originalList; // Original full list

    public AdminCDIBean() {}

    // ===================== INITIAL LOAD =====================
    @PostConstruct
    public void init() {
        originalList = abl.getAllElections();
        electionList = new ArrayList<>(originalList);
    }

    // ===================== LOAD ELECTION FOR UPDATE (NEW METHOD) =====================
    public void loadElectionForUpdate() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context != null) {
            Map<String, String> params = context.getExternalContext().getRequestParameterMap();
            String idParam = params.get("id");
            
            if (idParam != null && !idParam.trim().isEmpty()) {
                try {
                    Integer electionId = Integer.parseInt(idParam);
                    
                    // Find election from the list
                    for (Elections election : originalList) {
                        if (election.getElectionId().equals(electionId)) {
                            this.eId = election.getElectionId();
                            this.electionName = election.getElectionName();
                            this.status = election.getStatus();
                            this.desc = election.getDescription();
                            this.startDate = election.getStartDate();
                            this.endDate = election.getEndDate();
                            break;
                        }
                    }
                    
                    // If not found in cached list, fetch from database
                    if (this.eId == null) {
                        // You might need to call EJB to get single election
                        // Elections election = abl.getElectionById(electionId);
                        // if (election != null) {
                        //     populateFields(election);
                        // }
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    // ===================== POPULATE FIELDS HELPER =====================
    private void populateFields(Elections election) {
        this.eId = election.getElectionId();
        this.electionName = election.getElectionName();
        this.status = election.getStatus();
        this.desc = election.getDescription();
        this.startDate = election.getStartDate();
        this.endDate = election.getEndDate();
    }

    // ===================== ADD ELECTION =====================
    public String addElection() {
        java.sql.Date sqlStart = startDate != null ? new java.sql.Date(startDate.getTime()) : null;
        java.sql.Date sqlEnd = endDate != null ? new java.sql.Date(endDate.getTime()) : null;

        abl.addElections(eId, sqlStart, sqlEnd, electionName, status, desc);

        clearFields();
        refreshElectionList();

        FacesContext.getCurrentInstance().getPartialViewContext().getEvalScripts()
            .add("Swal.fire('Election added successfully', '', 'success');");

        return null;
    }

    // ===================== UPDATE ELECTION =====================
    public String updateElection() {
        if (eId != null) {
            java.sql.Date sqlStart = startDate != null ? new java.sql.Date(startDate.getTime()) : null;
            java.sql.Date sqlEnd = endDate != null ? new java.sql.Date(endDate.getTime()) : null;

            // Call EJB method to update election
            abl.updateElection(eId, sqlStart, sqlEnd, electionName, status, desc);

            FacesContext.getCurrentInstance().getPartialViewContext().getEvalScripts()
                .add("Swal.fire('Election updated successfully', '', 'success');");

            clearFields();
            refreshElectionList();
            return "ManageElection.xhtml?faces-redirect=true";
        }
        return null;
    }

    // ===================== SEARCH =====================
    public void searchElections() {
        List<Elections> filtered = new ArrayList<>(originalList);

        if (eId != null) {
            filtered.removeIf(e -> !e.getElectionId().equals(eId));
        }

        if (electionName != null && !electionName.trim().isEmpty()) {
            String lowerName = electionName.toLowerCase();
            filtered.removeIf(e -> e.getElectionName() == null || !e.getElectionName().toLowerCase().contains(lowerName));
        }

        if (startDate != null) {
            LocalDate filterStart = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            filtered.removeIf(e -> e.getStartDate() == null || 
                !e.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(filterStart));
        }

        if (endDate != null) {
            LocalDate filterEnd = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            filtered.removeIf(e -> e.getEndDate() == null || 
                !e.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(filterEnd));
        }

        electionList = filtered;
    }

    // ===================== CLEAR FILTERS =====================
    public void clearFilters() {
        clearFields();
        electionList = new ArrayList<>(originalList);
    }

    private void clearFields() {
        eId = null;
        electionName = null;
        status = null;
        desc = null;
        startDate = null;
        endDate = null;
    }

    private void refreshElectionList() {
        originalList = abl.getAllElections();
        electionList = new ArrayList<>(originalList);
    }

    // ===================== DELETE =====================
    public void deleteElection(int eId) {
        abl.deleteElection(eId);
        refreshElectionList();

        FacesContext.getCurrentInstance().getPartialViewContext().getEvalScripts()
            .add("Swal.fire('Deleted successfully','Election deleted','success');");
    }

    // ===================== GETTERS & SETTERS =====================
    public Integer geteId() { return eId; }
    public void seteId(Integer eId) { this.eId = eId; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public String getElectionName() { return electionName; }
    public void setElectionName(String electionName) { this.electionName = electionName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDesc() { return desc; }
    public void setDesc(String desc) { this.desc = desc; }

    public Collection<Elections> getElectionList() { return electionList; }
    public void setElectionList(Collection<Elections> electionList) { this.electionList = electionList; }
}