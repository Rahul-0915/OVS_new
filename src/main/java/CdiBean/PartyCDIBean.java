package CdiBean;

import EJB.AdminBeanLocal;
import Entity.Party;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Named(value = "partyCDIBean")
@ViewScoped
public class PartyCDIBean implements Serializable {

    @EJB
    private AdminBeanLocal abl;

    private Integer partyId;
    private String partyName;
    private String partySymbol; // Will store only path
    private String leaderName;
    private Integer foundedYear;

    private Part uploadedFile; // File uploaded by user

    private Collection<Party> partyList;
    private Collection<Party> originalList;

    public PartyCDIBean() {}

    @PostConstruct
    public void init() {
        originalList = abl.getAllParties();
        partyList = new ArrayList<>(originalList);
        clearFields();
    }

    // Load party for update page
    public void loadPartyForUpdate() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> params = fc.getExternalContext().getRequestParameterMap();

        String idParam = params.get("id");

        if (idParam != null) {
            Integer pId = Integer.parseInt(idParam);
            Party p = abl.findByPartyId(pId);

            if (p != null) {
                this.partyId = p.getPartyId();
                this.partyName = p.getPartyName();
                this.partySymbol = p.getPartySymbol();
                this.leaderName = p.getLeaderName();
                this.foundedYear = p.getFoundedYear();
            }
        }
    }

    // ADD PARTY
    public String addParty() {
        try {
            if (uploadedFile != null) {
                // Define folder to store images
                String uploadDir = FacesContext.getCurrentInstance()
                        .getExternalContext().getRealPath("/PartySymbols/");
                File folder = new File(uploadDir);
                if (!folder.exists()) folder.mkdirs();

                // Save file to server
                String fileName = uploadedFile.getSubmittedFileName();
                InputStream input = uploadedFile.getInputStream();
                File file = new File(folder, fileName);
                try (FileOutputStream out = new FileOutputStream(file)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = input.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                }

                // Save relative path in DB
                this.partySymbol = "PartySymbols/" + fileName;
            }

            abl.addParty(partyId, partyName, partySymbol, leaderName, foundedYear);
            clearFields();
            refresh();

            FacesContext.getCurrentInstance().getPartialViewContext().getEvalScripts()
                    .add("Swal.fire('Party Added!', '', 'success');");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // UPDATE PARTY
    public String updateParty() {
        try {
            if (uploadedFile != null) {
                String uploadDir = FacesContext.getCurrentInstance()
                        .getExternalContext().getRealPath("/PartySymbols/");
                File folder = new File(uploadDir);
                if (!folder.exists()) folder.mkdirs();

                String fileName = uploadedFile.getSubmittedFileName();
                InputStream input = uploadedFile.getInputStream();
                File file = new File(folder, fileName);
                try (FileOutputStream out = new FileOutputStream(file)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = input.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                }

                this.partySymbol = "PartySymbols/" + fileName;
            }

            abl.updateParty(partyId, partyName, leaderName, partySymbol, foundedYear);

            clearFields();
            refresh();

            FacesContext.getCurrentInstance().getPartialViewContext().getEvalScripts()
                    .add("Swal.fire('Party Updated!', '', 'success');");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "ManageParties.jsf?faces-redirect=true";
    }

    // DELETE PARTY
    public void deleteParty(int id) {
        abl.deleteParty(id);
        refresh();

        FacesContext.getCurrentInstance().getPartialViewContext().getEvalScripts()
                .add("Swal.fire('Deleted!', 'Party Deleted Successfully', 'success');");
    }

    // SEARCH
    public void searchParty() {
        List<Party> filtered = new ArrayList<>(originalList);

        if (partyId != null) filtered.removeIf(p -> !p.getPartyId().equals(partyId));
        if (partyName != null && !partyName.isBlank())
            filtered.removeIf(p -> !p.getPartyName().toLowerCase().contains(partyName.toLowerCase()));
        if (leaderName != null && !leaderName.isBlank())
            filtered.removeIf(p -> !p.getLeaderName().toLowerCase().contains(leaderName.toLowerCase()));
        if (foundedYear != null) filtered.removeIf(p -> !p.getFoundedYear().equals(foundedYear));

        partyList = filtered;
    }

    // Clear filters & input fields
    public void clearFilters() {
        clearFields();
        partyList = new ArrayList<>(originalList);
    }

    // Clear only input fields
    public void clearFields() {
        partyId = null;
        partyName = null;
        partySymbol = null;
        leaderName = null;
        foundedYear = null;
        uploadedFile = null;
    }

    // Refresh data from DB
    private void refresh() {
        originalList = abl.getAllParties();
        partyList = new ArrayList<>(originalList);
    }

    // GETTERS & SETTERS
    public Integer getPartyId() { return partyId; }
    public void setPartyId(Integer partyId) { this.partyId = partyId; }

    public String getPartyName() { return partyName; }
    public void setPartyName(String partyName) { this.partyName = partyName; }

    public String getPartySymbol() { return partySymbol; }
    public void setPartySymbol(String partySymbol) { this.partySymbol = partySymbol; }

    public String getLeaderName() { return leaderName; }
    public void setLeaderName(String leaderName) { this.leaderName = leaderName; }

    public Integer getFoundedYear() { return foundedYear; }
    public void setFoundedYear(Integer foundedYear) { this.foundedYear = foundedYear; }

    public Part getUploadedFile() { return uploadedFile; }
    public void setUploadedFile(Part uploadedFile) { this.uploadedFile = uploadedFile; }

    public Collection<Party> getPartyList() { return partyList; }
    public void setPartyList(Collection<Party> partyList) { this.partyList = partyList; }
}
