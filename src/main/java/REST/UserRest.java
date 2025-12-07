/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package REST;

import EJB.UserBeanLocal;
import Entity.Voters;
import Entity.Votes;
import jakarta.ejb.EJB;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author Rahul
 */
@Path("User")
public class UserRest {

    @EJB
    UserBeanLocal ovs;

    private static final String UPLOAD_DIR = "C:\\path\\to\\your\\web\\Uploads\\";

    @GET
    @Path("/test")
    public String test() {
        return "User REST working fine!";
    }
// voters...................................................................
//add voters

    @POST
    @Path("/addvoter/{voterName}/{mobileNumber}/{adharNumber}/{emailId}/{dob}/{city}/{pincode}/{address}/{adharFile:.+}/{voterImage:.+}")
    public String addVoter(
            @PathParam("voterName") String voterName,
            @PathParam("mobileNumber") int mobileNumber,
            @PathParam("adharNumber") int adharNumber,
            @PathParam("emailId") String emailId,
            @PathParam("dob") String dobStr,
            @PathParam("city") String city,
            @PathParam("pincode") int pincode,
            @PathParam("address") String address,
            @PathParam("adharFile") String adharFile,
            @PathParam("voterImage") String voterImage
    ) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dob = sdf.parse(dobStr);

            // Store only relative path in DB
            String adharPath = "Uploads/" + adharFile;
            String voterImgPath = "Uploads/" + voterImage;

            // Call EJB method to insert
            ovs.addVoter(voterName, mobileNumber, adharNumber, emailId, dob, city, pincode, address, adharPath, voterImgPath);

            return "Voter added successfully: " + voterName
                    + " | Adhar Path: " + adharPath
                    + " | Image Path: " + voterImgPath;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error adding voter: " + e.getMessage();
        }
    }

    // delete voters by id   
    @DELETE
    @Path("/deletevoter/{voterId}")
    public String deleteVoter(@PathParam("voterId") int voterId) {
        try {
            ovs.deleteVoter(voterId);
            return " Voter deleted successfully with ID: " + voterId;
        } catch (Exception e) {
            e.printStackTrace();
            return " Error deleting voter: " + e.getMessage();
        }
    }
// voter by id

    @GET
    @Path("/votersbyid/{voterId}")
    public String findByVoterId(@PathParam("voterId") int voterId) {
        try {
            Voters v = ovs.findByVoterId(voterId);
            if (v != null) {
                StringBuilder sb = new StringBuilder();
                sb.append(" Voter Found Successfully\n")
                        .append("--------------------------------------------------\n")
                        .append("ID: ").append(v.getVoterId()).append("\n")
                        .append("Name: ").append(v.getVoterName()).append("\n")
                        .append("Mobile: ").append(v.getMobileNumber()).append("\n")
                        .append("Aadhar: ").append(v.getAdharNumber()).append("\n")
                        .append("Email: ").append(v.getEmailId()).append("\n")
                        .append("DOB: ").append(v.getDob()).append("\n")
                        .append("City: ").append(v.getCity()).append("\n")
                        .append("Pincode: ").append(v.getPincode()).append("\n")
                        .append("Address: ").append(v.getAddress()).append("\n")
                        .append("Apply Date: ").append(v.getApplyDate()).append("\n")
                        .append("Issue Date: ").append(v.getIssueDate()).append("\n")
                        .append("Status: ").append(v.getStatus()).append("\n")
                        .append("Aadhar File Path: ").append(v.getAdharFile()).append("\n")
                        .append("Voter Image Path: ").append(v.getVoterImage()).append("\n")
                        .append("--------------------------------------------------\n");
                return sb.toString();
            } else {
                return " No voter found with ID: " + voterId;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error finding voter: " + e.getMessage();
        }
    }

// get all voters
    @GET
    @Path("/allvoters")
    public String getAllVoters() {
        try {
            var list = ovs.getAllVoters();
            StringBuilder sb = new StringBuilder();
            sb.append("Total Voters: ").append(list.size()).append("\n\n");

            for (Voters v : list) {
                sb.append("ID: ").append(v.getVoterId())
                        .append(", Name: ").append(v.getVoterName())
                        .append(", Mobile: ").append(v.getMobileNumber())
                        .append(", Aadhar: ").append(v.getAdharNumber())
                        .append(", Email: ").append(v.getEmailId())
                        .append(", DOB: ").append(v.getDob())
                        .append(", City: ").append(v.getCity())
                        .append(", Pincode: ").append(v.getPincode())
                        .append(", Address: ").append(v.getAddress())
                        .append(", Apply Date: ").append(v.getApplyDate())
                        .append(", Issue Date: ").append(v.getIssueDate())
                        .append(", Status: ").append(v.getStatus())
                        .append(", Aadhar File: ").append(v.getAdharFile())
                        .append(", Voter Image: ").append(v.getVoterImage())
                        .append("\n--------------------------------------------------\n");
            }

            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return " Error fetching voters: " + e.getMessage();
        }
    }
// voter by city

    @GET
    @Path("/voterbycity/{city}")
    public String findByCity(@PathParam("city") String city) {
        try {
            var list = ovs.findByCity(city);
            StringBuilder sb = new StringBuilder();
            sb.append(" City: ").append(city)
                    .append(" → Total Voters: ").append(list.size())
                    .append("\n\n");

            for (Voters v : list) {
                sb.append("ID: ").append(v.getVoterId())
                        .append(", Name: ").append(v.getVoterName())
                        .append(", Email: ").append(v.getEmailId())
                        .append(", Status: ").append(v.getStatus())
                        .append(", Adhar File: ").append(v.getAdharFile())
                        .append(", Image: ").append(v.getVoterImage())
                        .append("\n--------------------------------------------------\n");
            }

            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return " Error fetching voters by city: " + e.getMessage();
        }
    }
//voter find by status

    @GET
    @Path("/voterbystatus/{status}")
    public String findByStatus(@PathParam("status") int status) {
        try {
            var list = ovs.findByStatus(status);
            StringBuilder sb = new StringBuilder();
            sb.append("🟩 Status: ").append(status)
                    .append(" → Total Voters: ").append(list.size())
                    .append("\n\n");

            for (Voters v : list) {
                sb.append("ID: ").append(v.getVoterId())
                        .append(", Name: ").append(v.getVoterName())
                        .append(", City: ").append(v.getCity())
                        .append(", Email: ").append(v.getEmailId())
                        .append(", Apply Date: ").append(v.getApplyDate())
                        .append(", Issue Date: ").append(v.getIssueDate())
                        .append(", Adhar: ").append(v.getAdharNumber())
                        .append(", Aadhar File: ").append(v.getAdharFile())
                        .append(", Voter Image: ").append(v.getVoterImage())
                        .append("\n--------------------------------------------------\n");
            }

            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error fetching voters by status: " + e.getMessage();
        }
    }

// update voters 
    @PUT
    @Path("/updatevoter/{voterId}/{voterName}/{mobileNumber}/{adharNumber}/{emailId}/{dob}/{city}/{pincode}/{address}/{adharFile}/{voterImage}")
    public String updateVoter(
            @PathParam("voterId") int voterId,
            @PathParam("voterName") String voterName,
            @PathParam("mobileNumber") int mobileNumber,
            @PathParam("adharNumber") int adharNumber,
            @PathParam("emailId") String emailId,
            @PathParam("dob") String dobStr,
            @PathParam("city") String city,
            @PathParam("pincode") int pincode,
            @PathParam("address") String address,
            @PathParam("adharFile") String adharFile,
            @PathParam("voterImage") String voterImage
    ) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dob = sdf.parse(dobStr);

            ovs.updateVoter(voterId, voterName, mobileNumber, adharNumber, emailId, dob, city, pincode, address, adharFile, voterImage);

            return " Voter updated successfully → ID: " + voterId;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error updating voter: " + e.getMessage();
        }
    }
//votes......................................................

    @POST
    @Path("/addvote/{voterId}/{candidateId}/{partyId}/{electionId}/{verifyStatus}/{electionName}")
    public String addVote(
            @PathParam("voterId") int voterId,
            @PathParam("candidateId") int candidateId,
            @PathParam("partyId") int partyId,
            @PathParam("electionId") int electionId,
            @PathParam("verifyStatus") int verifyStatus,
            @PathParam("electionName") String electionName) {

        Date now = new Date();
        ovs.addVote(voterId, candidateId, partyId, now, electionId, verifyStatus, electionName);
        return "Vote Added Successfully for Voter ID: " + voterId;
    }

    @GET
    @Path("/byvoteid/{voteId}")
    public String findByVoteId(@PathParam("voteId") int voteId) {
        Votes v = ovs.findByVoteId(voteId);
        if (v == null) {
            return "❌ No vote found with ID: " + voteId;
        }
        return "✅ Vote Found:\n";
    }

    @GET
    @Path("/getall")
    public String getAllVotes() {
        Collection<Votes> list = ovs.getAllVotes();
        StringBuilder sb = new StringBuilder();
        for (Votes v : list) {
            sb.append("Vote ID: ").append(v.getVoteId())
                    .append(", Voter: ").append(v.getVoters().getVoterId())
                    .append(", Candidate: ").append(v.getCandidates().getCandidateId())
                    .append(", Election: ").append(v.getElections().getElectionId())
                    .append(", Verify: ").append(v.getVerifyStatus())
                    .append("\n");
        }
        return sb.toString();
    }

    @GET
    @Path("/bycandidate/{cid}")
    public String findByCandidateId(@PathParam("cid") int cid) {
        Collection<Votes> list = ovs.findByCandidateId(cid);
        return buildString(list, "Candidate ID", String.valueOf(cid));
    }

    @GET
    @Path("/byparty/{pid}")
    public String findByPartyId(@PathParam("pid") int pid) {
        Collection<Votes> list = ovs.findByPartyId(pid);
        return buildString(list, "Party ID", String.valueOf(pid));
    }

    @GET
    @Path("/byelection/{eid}")
    public String findByElectionId(@PathParam("eid") int eid) {
        Collection<Votes> list = ovs.findByElectionId(eid);
        return buildString(list, "Election ID", String.valueOf(eid));
    }

    @GET
    @Path("/byelectionname/{ename}")
    public String findByElectionName(@PathParam("ename") String ename) {
        Collection<Votes> list = ovs.findByElectionName(ename);
        return buildString(list, "Election Name", ename);
    }

    private String buildString(Collection<Votes> list, String key, String value) {
        StringBuilder sb = new StringBuilder();
        sb.append("Votes Found for ").append(key).append(": ").append(value).append("\n");
        for (Votes v : list) {
            sb.append("Vote ID: ").append(v.getVoteId())
                    .append(", Voter: ").append(v.getVoters().getVoterId())
                    .append(", Candidate: ").append(v.getCandidates().getCandidateId())
                    .append(", Election: ").append(v.getElections().getElectionId())
                    .append(", Verify: ").append(v.getVerifyStatus())
                    .append("\n");
        }
        return sb.toString();
    }

    // uses..................................
    @GET
    @Path("/user")
    public String user() {
        return "User REST working fine!";
    }

    @POST
    @Path("/adduser/{userName}/{email}/{mobile}/{password}/{gId}/{verify}")
    @Produces("text/plain")
    public String addUserRest(
            @PathParam("userName") String userName,
            @PathParam("email") String email,
            @PathParam("mobile") String mobile,
            @PathParam("password") String password,
            @PathParam("gId") int gId,
            @PathParam("verify") int verify) {

        try {
            // Call EJB method
            ovs.addUser(userName, email, mobile, password, gId, verify);

            System.out.println("REST API: User added successfully!");

            return "User added successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("REST API Error: " + e.getMessage());

            return "Error while adding user: " + e.getMessage();
        }
    }

}
