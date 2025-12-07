/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package REST;
    


import EJB.AdminBeanLocal;
import Entity.Candidates;
import Entity.Elections;
import Entity.Party;
import jakarta.annotation.security.DeclareRoles;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;






/**
 *
 * @author Rahul
 */
@Path("Admin")
@DeclareRoles("admin")
public class AdminRest{

    //Election Rest ..................................
    @EJB
    AdminBeanLocal ovs;

    @POST
    @Path("/electionsadd/{eId}/{startDate}/{endDate}/{electionName}/{status}/{desc}")
    public String addElection(
            @PathParam("eId") Integer eId,
            @PathParam("startDate") String startDateStr,
            @PathParam("endDate") String endDateStr,
            @PathParam("electionName") String electionName,
            @PathParam("status") String status,
            @PathParam("desc") String desc) {

        try {
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            
            Date utilStartDate = sdf.parse(startDateStr);
            Date utilEndDate = sdf.parse(endDateStr);

            
            java.sql.Date sqlStartDate = new java.sql.Date(utilStartDate.getTime());
            java.sql.Date sqlEndDate = new java.sql.Date(utilEndDate.getTime());

            
            ovs.addElections(eId, sqlStartDate, sqlEndDate, electionName, status, desc);

            return "Election added successfully: " + electionName;

        } catch (Exception e) {
            e.printStackTrace();
            return "Error adding election: " + e.getMessage();
        }
    }

//update elections data...
@PUT
@Path("/electionsupdate/{eId}/{startDate}/{endDate}/{electionName}/{status}/{desc}")
public String updateElection(
        @PathParam("eId") Integer eId,
        @PathParam("startDate") String startDateStr,
        @PathParam("endDate") String endDateStr,
        @PathParam("electionName") String electionName,
        @PathParam("status") String status,
        @PathParam("desc") String desc) {

    try {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.sql.Date sqlStartDate = new java.sql.Date(sdf.parse(startDateStr).getTime());
        java.sql.Date sqlEndDate = new java.sql.Date(sdf.parse(endDateStr).getTime());

        
        ovs.updateElection(eId, sqlStartDate, sqlEndDate, electionName, status, desc);

        return "Election updated successfully: " + electionName;

    } catch (Exception e) {
        e.printStackTrace();
        return "Error updating election: " + e.getMessage();
    }
}
 // Getall elections data
@GET
@Path("/electionsall")
public String getAllElections() {
    try {
        Collection<Elections> electionsList = ovs.getAllElections();
        StringBuilder sb = new StringBuilder();
        for (Elections e : electionsList) {
            sb.append("ID: ").append(e.getElectionId())
              .append(", Name: ").append(e.getElectionName())
              .append(", StartDate: ").append(e.getStartDate())
              .append(", EndDate: ").append(e.getEndDate())
              .append(", Status: ").append(e.getStatus())
              .append(", Description: ").append(e.getDescription())
              .append("\n");
        }
        return sb.toString();
    } catch (Exception ex) {
        ex.printStackTrace();
        return "Error fetching elections: " + ex.getMessage();
    }
}
// get elections by id 
@GET
@Path("/election/{eId}")
public String getElectionById(@PathParam("eId") Integer eId) {
    try {
        Elections e = ovs.getElectionById(eId);
        if (e != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("ID: ").append(e.getElectionId())
              .append(", Name: ").append(e.getElectionName())
              .append(", StartDate: ").append(e.getStartDate())
              .append(", EndDate: ").append(e.getEndDate())
              .append(", Status: ").append(e.getStatus())
              .append(", Description: ").append(e.getDescription());
            return sb.toString();
        } else {
            return "Election with ID " + eId + " not found!";
        }
    } catch (Exception ex) {
        ex.printStackTrace();
        return "Error fetching election: " + ex.getMessage();
    }
}

//delete elections by id
@DELETE
@Path("/electiondelete/{eId}")
public String deleteElection(@PathParam("eId") Integer eId) {
    try {
        Elections e = ovs.getElectionById(eId);
        if (e != null) {
            ovs.deleteElection(eId);
            return "Election deleted successfully: " + eId;
        } else {
            return "Election with ID " + eId + " not found!";
        }
    } catch (Exception ex) {
        ex.printStackTrace();
        return "Error deleting election: " + ex.getMessage();
    }
}
// get elections by name
@GET
@Path("/electionsbyname/{electionName}")
public String getElectionsByName(@PathParam("electionName") String electionName) {
    try {
        Collection<Elections> electionsList = ovs.findByName(electionName);
        if (electionsList.isEmpty()) {
            return "No elections found with name: " + electionName;
        }
        StringBuilder sb = new StringBuilder();
        for (Elections e : electionsList) {
            sb.append("ID: ").append(e.getElectionId())
              .append(", Name: ").append(e.getElectionName())
              .append(", StartDate: ").append(e.getStartDate())
              .append(", EndDate: ").append(e.getEndDate())
              .append(", Status: ").append(e.getStatus())
              .append(", Description: ").append(e.getDescription())
              .append("\n");
        }
        return sb.toString();
    } catch (Exception ex) {
        ex.printStackTrace();
        return "Error fetching elections: " + ex.getMessage();
    }
}
//get elections by startdate
@GET
@Path("/electionsbystart/{startDate}")
public String getElectionsByStartDate(@PathParam("startDate") String startDateStr) {
    try {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.sql.Date sqlStartDate = new java.sql.Date(sdf.parse(startDateStr).getTime());

        Collection<Elections> electionsList = ovs.findByStartDate(sqlStartDate);

        if (electionsList.isEmpty()) {
            return "No elections found with start date: " + startDateStr;
        }

        StringBuilder sb = new StringBuilder();
        for (Elections e : electionsList) {
            sb.append("ID: ").append(e.getElectionId())
              .append(", Name: ").append(e.getElectionName())
              .append(", StartDate: ").append(e.getStartDate())
              .append(", EndDate: ").append(e.getEndDate())
              .append(", Status: ").append(e.getStatus())
              .append(", Description: ").append(e.getDescription())
              .append("\n");
        }

        return sb.toString();

    } catch (Exception ex) {
        ex.printStackTrace();
        return "Error fetching elections: " + ex.getMessage();
    }
}
// get elections by end date 
@GET
@Path("/electionsbyend/{endDate}")
public String getElectionsByEndDate(@PathParam("endDate") String endDateStr) {
    try {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.sql.Date sqlEndDate = new java.sql.Date(sdf.parse(endDateStr).getTime());

        Collection<Elections> electionsList = ovs.findByEndDate(sqlEndDate);

        if (electionsList.isEmpty()) {
            return "No elections found with end date: " + endDateStr;
        }

        StringBuilder sb = new StringBuilder();
        for (Elections e : electionsList) {
            sb.append("ID: ").append(e.getElectionId())
              .append(", Name: ").append(e.getElectionName())
              .append(", StartDate: ").append(e.getStartDate())
              .append(", EndDate: ").append(e.getEndDate())
              .append(", Status: ").append(e.getStatus())
              .append(", Description: ").append(e.getDescription())
              .append("\n");
        }

        return sb.toString();

    } catch (Exception ex) {
        ex.printStackTrace();
        return "Error fetching elections: " + ex.getMessage();
    }
    
}
// candidate  rest.................................................
// candidates add
@POST
@Path("/candidateadd/{candidateId}/{candidateName}/{nominationDate}/{eId}/{pId}")
public String addCandidate(
        @PathParam("candidateId") int candidateId,
        @PathParam("candidateName") String candidateName,
        @PathParam("nominationDate") String nominationDateStr,
        @PathParam("eId") int eId,
        @PathParam("pId") int pId) {
    try {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.sql.Date sqlNominationDate = new java.sql.Date(sdf.parse(nominationDateStr).getTime());

        ovs.addCandidates(candidateId, candidateName, sqlNominationDate, eId, pId);

        return "Candidate added successfully: " + candidateName;

    } catch (Exception e) {
        e.printStackTrace();
        return "Error adding candidate: " + e.getMessage();
    }
}

//update candidates...
@PUT
@Path("/candidateupdate/{cId}/{candidateName}/{nominationDate}/{eId}/{pId}")
public String updateCandidate(
        @PathParam("cId") int cId,
        @PathParam("candidateName") String candidateName,
        @PathParam("nominationDate") String nominationDateStr,
        @PathParam("eId") int eId,
        @PathParam("pId") int pId) {

    try {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.sql.Date sqlNominationDate = new java.sql.Date(sdf.parse(nominationDateStr).getTime());

        // EJB call
        ovs.updateCandidate(cId, candidateName, sqlNominationDate, eId, pId);

        return "Candidate updated successfully: " + candidateName;

    } catch (Exception e) {
        e.printStackTrace();
        return "Error updating candidate: " + e.getMessage();
    }
}
// delete candiadte by id..
@DELETE
@Path("/candidatedelete/{cId}")
public String deleteCandidate(@PathParam("cId") int cId) {
    try {
        ovs.deleteCandidate(cId);
        return "Candidate deleted successfully: " + cId;
    } catch (Exception e) {
        e.printStackTrace();
        return "Error deleting candidate: " + e.getMessage();
    }
}

// FIND BY ELECTION id
@GET
@Path("/candidatebyelection/{eId}")
public String findByElection(@PathParam("eId") int eId) {
    try {
        var list = ovs.findByElectionId(eId);

        if (list == null || list.isEmpty()) {
            return "No candidates found for Election ID: " + eId;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Candidates found for Election ID ").append(eId)
          .append(": ").append(list.size()).append("\n\n");

        for (Candidates c : list) {
            sb.append("ID: ").append(c.getCandidateId()).append("\n")
              .append("Name: ").append(c.getCandidateName()).append("\n")
              .append("Nomination Date: ").append(c.getNominationDate()).append("\n")
              .append("Party ID: ")
              .append(c.getParty() != null ? c.getParty().getPartyId() : "N/A").append("\n")
              .append("---------------------------\n");
        }
        return sb.toString();

    } catch (Exception e) {
        e.printStackTrace();
        return "Error finding candidates by election: " + e.getMessage();
    }
}

// FIND BY PARTY id
@GET
@Path("/candidatebyparty/{pId}")
public String findByParty(@PathParam("pId") int pId) {
    try {
        var list = ovs.findByPartyId(pId);

        if (list == null || list.isEmpty()) {
            return "No candidates found for Party ID: " + pId;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Candidates found for Party ID ").append(pId)
          .append(": ").append(list.size()).append("\n\n");

        for (Candidates c : list) {
            sb.append("ID: ").append(c.getCandidateId()).append("\n")
              .append("Name: ").append(c.getCandidateName()).append("\n")
              .append("Nomination Date: ").append(c.getNominationDate()).append("\n")
              .append("Election ID: ")
              .append(c.getElections() != null ? c.getElections().getElectionId() : "N/A").append("\n")
              .append("---------------------------\n");
        }

        return sb.toString();

    } catch (Exception e) {
        e.printStackTrace();
        return "Error finding candidates by party: " + e.getMessage();
    }
}

// FIND BY candidate ID
@GET
@Path("/candidatebyid/{cId}")
public String findByCandidateId(@PathParam("cId") int cId) {
    try {
        Candidates c = ovs.findByCandidateId(cId);

        if (c == null)
            return "No candidate found with ID: " + cId;

        StringBuilder sb = new StringBuilder();
        sb.append("Candidate Details:\n")
          .append("ID: ").append(c.getCandidateId()).append("\n")
          .append("Name: ").append(c.getCandidateName()).append("\n")
          .append("Nomination Date: ").append(c.getNominationDate()).append("\n")
          .append("Election ID: ")
          .append(c.getElections() != null ? c.getElections().getElectionId() : "N/A").append("\n")
          .append("Party ID: ")
          .append(c.getParty() != null ? c.getParty().getPartyId() : "N/A").append("\n");

        return sb.toString();

    } catch (Exception e) {
        e.printStackTrace();
        return "Error finding candidate by ID: " + e.getMessage();
    }
}
// find candidate by name 
@GET
@Path("/candidatebyname/{name}")
public String findByCandidateName(@PathParam("name") String name) {
    try {
        var list = ovs.findByCandidateName(name);

        if (list == null || list.isEmpty()) {
            return "No candidates found with name: " + name;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Candidates found with name '").append(name).append("': ")
          .append(list.size()).append("\n\n");

        for (Candidates c : list) {
            sb.append("ID: ").append(c.getCandidateId()).append("\n")
              .append("Name: ").append(c.getCandidateName()).append("\n")
              .append("Nomination Date: ").append(c.getNominationDate()).append("\n")
              .append("Election ID: ")
              .append(c.getElections() != null ? c.getElections().getElectionId() : "N/A").append("\n")
              .append("Party ID: ")
              .append(c.getParty() != null ? c.getParty().getPartyId() : "N/A").append("\n")
              .append("---------------------------\n");
        }

        return sb.toString();

    } catch (Exception e) {
        e.printStackTrace();
        return "Error finding candidate by name: " + e.getMessage();
    }
}
// get all candidates
@GET
@Path("/candidatesall")
public String getAllCandidates() {
    try {
        var list = ovs.getAllCandidates();

        if (list == null || list.isEmpty()) {
            return "No candidates found.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Total Candidates: ").append(list.size()).append("\n\n");

        for (Candidates c : list) {
            sb.append("ID: ").append(c.getCandidateId()).append("\n")
              .append("Name: ").append(c.getCandidateName()).append("\n")
              .append("Nomination Date: ").append(c.getNominationDate()).append("\n")
              .append("Election ID: ")
              .append(c.getElections() != null ? c.getElections().getElectionId() : "N/A").append("\n")
              .append("Party ID: ")
              .append(c.getParty() != null ? c.getParty().getPartyId() : "N/A").append("\n")
              .append("---------------------------\n");
        }

        return sb.toString();

    } catch (Exception e) {
        e.printStackTrace();
        return "Error fetching all candidates: " + e.getMessage();
    }
}
  
//party rest..............................................................................
// add party
@POST
    @Path("addparty/{partyid}/{partyname}/{partysymbol:.+}/{leadername}/{foundedyear}")
    public void addParty(
            @PathParam("partyid") Integer partyId,
            @PathParam("partyname") String partyName,
            @PathParam("partysymbol") String partySymbol, // now can contain slashes
            @PathParam("leadername") String leaderName,
            @PathParam("foundedyear") Integer foundedYear
    ) {
        ovs.addParty(partyId, partyName, partySymbol, leaderName, foundedYear);
    }
    
// get all party data ..
     @GET
@Path("/getallparty")
@Produces(MediaType.TEXT_PLAIN)
public String getAllParties() {
    try {
        Collection<Party> partyList = ovs.getAllParties();
        StringBuilder sb = new StringBuilder();
        for (Party p : partyList) {
            sb.append("ID: ").append(p.getPartyId())
              .append(", Name: ").append(p.getPartyName())
              .append(", Symbol: ").append(p.getPartySymbol())
              .append(", Leader: ").append(p.getLeaderName())
              .append(", Founded: ").append(p.getFoundedYear())
              .append("\n");
        }
        return sb.toString();
    } catch (Exception e) {
        e.printStackTrace();
        return "Error fetching parties: " + e.getMessage();
    }
}

//// find by party by id
// @GET
//@Path("/partybyid/{partyId}")
//public String findByPartyId(@PathParam("partyId") Integer partyId) {
//    try {
//        Party p = ovs.findByPartyId(partyId);
//        if (p == null) {
//            return "No party found with ID: " + partyId;
//        }
//
//        StringBuilder sb = new StringBuilder();
//        sb.append("ID: ").append(p.getPartyId())
//          .append(", Name: ").append(p.getPartyName())
//          .append(", Symbol: ").append(p.getPartySymbol())
//          .append(", Leader: ").append(p.getLeaderName())
//          .append(", Founded Year: ").append(p.getFoundedYear());
//        return sb.toString();
//
//    } catch (Exception e) {
//        e.printStackTrace();
//        return "Error fetching party by ID: " + e.getMessage();
//    }
//}
// update party
@PUT
@Path("/partyupdate/{partyId}/{partyName}/{partySymbol:.+}/{leaderName}/{foundedYear}")
public String updateParty(
        @PathParam("partyId") Integer partyId,
        @PathParam("partyName") String partyName,
        @PathParam("partySymbol") String partySymbol,
        @PathParam("leaderName") String leaderName,
        @PathParam("foundedYear") Integer foundedYear) {
    try {
        ovs.updateParty(partyId, partyName, partySymbol, leaderName, foundedYear);
        return "Party updated successfully: " + partyName;
    } catch (Exception e) {
        e.printStackTrace();
        return "Error updating party: " + e.getMessage();
    }
}
// delete party
@DELETE
@Path("/partydelete/{partyId}")
public String deleteParty(@PathParam("partyId") Integer partyId) {
    try {
        ovs.deleteParty(partyId);
        return "Party deleted successfully: " + partyId;
    } catch (Exception e) {
        e.printStackTrace();
        return "Error deleting party: " + e.getMessage();
    }
}
////find by party id 
@GET
@Path("/partybyid/{partyId}")
@Produces(MediaType.TEXT_PLAIN)
public String findByPartyId(@PathParam("partyId") int partyId) {
    try {
        Party p = (Party) ovs.findByPartyId(partyId);
        if (p == null) {
            return "No party found with ID: " + partyId;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Party Details:\n")
          .append("ID: ").append(p.getPartyId()).append("\n")
          .append("Name: ").append(p.getPartyName()).append("\n")
          .append("Symbol: ").append(p.getPartySymbol()).append("\n")
          .append("Leader: ").append(p.getLeaderName()).append("\n")
          .append("Founded Year: ").append(p.getFoundedYear()).append("\n");

        return sb.toString();

    } catch (Exception e) {
        e.printStackTrace();
        return "Error fetching party by ID: " + e.getMessage();
    }
}
 

@GET
@Path("/partybyname/{partyName}")
@Produces(MediaType.TEXT_PLAIN)
public String findByPartyName(@PathParam("partyName") String partyName) {
    try {
        Collection<Party> list = ovs.findByPartyName(partyName);
        if (list == null || list.isEmpty()) {
            return "No parties found with name: " + partyName;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Parties found with name '").append(partyName).append("': ").append(list.size()).append("\n\n");

        for (Party p : list) {
            sb.append("ID: ").append(p.getPartyId()).append("\n")
              .append("Name: ").append(p.getPartyName()).append("\n")
              .append("Symbol: ").append(p.getPartySymbol()).append("\n")
              .append("Leader: ").append(p.getLeaderName()).append("\n")
              .append("Founded Year: ").append(p.getFoundedYear()).append("\n")
              .append("---------------------------\n");
        }

        return sb.toString();

    } catch (Exception e) {
        e.printStackTrace();
        return "Error fetching parties by name: " + e.getMessage();
    }
}

// find by leader name
@GET
@Path("/partybyleader/{leaderName}")
@Produces(MediaType.TEXT_PLAIN)
public String findByLeader(@PathParam("leaderName") String leaderName) {
    try {
        Collection<Party> list = ovs.findByLeaderName(leaderName);
        if (list == null || list.isEmpty()) {
            return "No parties found for leader: " + leaderName;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Parties found for leader '").append(leaderName).append("': ").append(list.size()).append("\n\n");

        for (Party p : list) {
            sb.append("ID: ").append(p.getPartyId()).append("\n")
              .append("Name: ").append(p.getPartyName()).append("\n")
              .append("Symbol: ").append(p.getPartySymbol()).append("\n")
              .append("Leader: ").append(p.getLeaderName()).append("\n")
              .append("Founded Year: ").append(p.getFoundedYear()).append("\n")
              .append("---------------------------\n");
        }

        return sb.toString();

    } catch (Exception e) {
        e.printStackTrace();
        return "Error fetching parties by leader: " + e.getMessage();
    }
}

// find by founded year
@GET
@Path("/partybyyear/{foundedYear}")
@Produces(MediaType.TEXT_PLAIN)
public String findByFoundedYear(@PathParam("foundedYear") int foundedYear) {
    try {
        Collection<Party> list = ovs.findByFoundedYear(foundedYear);
        if (list == null || list.isEmpty()) {
            return "No parties found founded in year: " + foundedYear;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Parties founded in ").append(foundedYear).append(": ").append(list.size()).append("\n\n");

        for (Party p : list) {
            sb.append("ID: ").append(p.getPartyId()).append("\n")
              .append("Name: ").append(p.getPartyName()).append("\n")
              .append("Symbol: ").append(p.getPartySymbol()).append("\n")
              .append("Leader: ").append(p.getLeaderName()).append("\n")
              .append("Founded Year: ").append(p.getFoundedYear()).append("\n")
              .append("---------------------------\n");
        }

        return sb.toString();

    } catch (Exception e) {
        e.printStackTrace();
        return "Error fetching parties by founded year: " + e.getMessage();
    }
}
@RolesAllowed({"Admin"})
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String sayHello() {

        //TODO return proper representation object
        // throw new UnsupportedOperationException();
        return ovs.saySecureHello() + " from Rest Client";
    }
//
}

