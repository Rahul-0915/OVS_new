/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/StatelessEjbClass.java to edit this template
 */
package EJB;

import Entity.Candidates;
import Entity.Elections;
import Entity.GroupMaster;
import Entity.Party;
import Entity.Users;
import Entity.Voters;
import Entity.Votes;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author Rahul
 */
@Stateless
public class UserBean implements UserBeanLocal {

    @PersistenceContext(unitName = "OVS")
    EntityManager em;
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Inject
    private Pbkdf2PasswordHash passwordHash;

    @Override
    public void addVoter(String voterName, int mobileNumber, int adharNumber, String emailId, Date dob, String city, int pincode, String address, String adharFilePath, String voterImagePath) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        Voters v = new Voters();
        v.setVoterName(voterName);
        v.setMobileNumber(mobileNumber);
        v.setAdharNumber(adharNumber);
        v.setEmailId(emailId);
        v.setDob(dob);
        v.setCity(city);
        v.setPincode(pincode);
        v.setAddress(address);
        v.setApplyDate(new Date());  // apply time auto
        v.setStatus(0);              // pending
        v.setAdharFile(adharFilePath);
        v.setVoterImage(voterImagePath);
        em.persist(v);
    }

    @Override
    public void deleteVoter(int voterId) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        try {
            Voters v = em.find(Voters.class, voterId);
            if (v != null) {
                em.remove(v);
                System.out.println("Voter deleted successfully: " + voterId);
            } else {
                System.out.println("Voter not found with ID: " + voterId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error deleting voter: " + e.getMessage());
        }
    }

    @Override
    public Voters findByVoterId(int voterId) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        return em.find(Voters.class, voterId);
    }

    @Override
    public Collection<Voters> getAllVoters() {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        return em.createQuery("SELECT v FROM Voters v", Voters.class).getResultList();

    }

    @Override
    public Collection<Voters> findByCity(String city) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        return em.createQuery("SELECT v FROM Voters v WHERE v.city = :city", Voters.class)
                .setParameter("city", city)
                .getResultList();
    }

    @Override
    public Collection<Voters> findByStatus(int status) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        return em.createQuery("SELECT v FROM Voters v WHERE v.status = :status", Voters.class)
                .setParameter("status", status)
                .getResultList();
    }

    @Override
    public void updateVoter(int voterId, String voterName, int mobileNumber, int adharNumber, String emailId, Date dob, String city, int pincode, String address, String adharFilePath, String voterImagePath) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        Voters v = em.find(Voters.class, voterId);
        if (v != null) {
            v.setVoterName(voterName);
            v.setMobileNumber(mobileNumber);
            v.setAdharNumber(adharNumber);
            v.setEmailId(emailId);
            v.setDob(dob);
            v.setCity(city);
            v.setPincode(pincode);
            v.setAddress(address);
            v.setAdharFile(adharFilePath);
            v.setVoterImage(voterImagePath);
            em.merge(v);
            System.out.println("Voter updated successfully: " + voterName);
        } else {
            System.out.println(" Voter not found with ID: " + voterId);
        }
    }

    // votes....................................................................
    @Override
    public void addVote(int voterId, int candidateId, int partyId, Date timeStamp,
            int electionId, int verifyStatus, String electionName) {
        Votes v = new Votes();
        v.setTimeStamp(timeStamp);
        v.setVerifyStatus(verifyStatus);

        v.setVoters(em.find(Voters.class, voterId));
        v.setCandidates(em.find(Candidates.class, candidateId));
        v.setElections(em.find(Elections.class, electionId));

        em.persist(v);
    }

    @Override
    public Collection<Votes> getAllVotes() {
        return em.createNamedQuery("Votes.findAll").getResultList();
    }

    @Override
    public Votes findByVoteId(int voteId) {
        return em.find(Votes.class, voteId);
    }

    @Override
    public Collection<Votes> findByCandidateId(int candidateId) {
        return em.createQuery("SELECT v FROM Votes v WHERE v.candidates.candidateId = :cid")
                .setParameter("cid", candidateId)
                .getResultList();
    }

    @Override
    public Collection<Votes> findByPartyId(int partyId) {
        return em.createQuery("SELECT v FROM Votes v WHERE v.candidates.party.partyId = :pid")
                .setParameter("pid", partyId)
                .getResultList();
    }

    @Override
    public Collection<Votes> findByElectionId(int electionId) {
        return em.createQuery("SELECT v FROM Votes v WHERE v.elections.electionId = :eid")
                .setParameter("eid", electionId)
                .getResultList();
    }

    @Override
    public Collection<Votes> findByElectionName(String electionName) {
        return em.createQuery("SELECT v FROM Votes v WHERE v.elections.electionName = :ename")
                .setParameter("ename", electionName)
                .getResultList();
    }
    // users .......................................

    @Override
    public void addUser(String userName, String email,String mobile, String password, int gId, int verify) {

        try {
            GroupMaster g = em.find(GroupMaster.class, gId);

            if (g == null) {
                throw new RuntimeException("Invalid Group ID: " + gId);
            }

            Users u = new Users();
            u.setUserName(userName);
            u.setEmailId(email);
            u.setMobileNumber(mobile);
            u.setPassword(passwordHash.generate(password.toCharArray()));
            u.setVerification(verify);
            u.setGroupmaster(g);

            em.persist(u);

            System.out.println("User inserted successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            throw e;  // REST ko exception bhejne ke liye
        }
    }
    
}
