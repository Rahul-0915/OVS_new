/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/SessionLocal.java to edit this template
 */
package EJB;

import Entity.Users;
import Entity.Voters;
import Entity.Votes;
import jakarta.ejb.Local;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author Rahul
 */
@Local
public interface UserBeanLocal {

    //Voters ejb....................
    public void addVoter(String voterName, int mobileNumber, int adharNumber, String emailId, Date dob, String city,
            int pincode, String address, String adharFilePath, String voterImagePath);

    public void updateVoter(int voterId, String voterName, int mobileNumber, int adharNumber, String emailId, Date dob,
            String city, int pincode, String address, String adharFilePath, String voterImagePath);

    void deleteVoter(int voterId);

    Voters findByVoterId(int voterId);

    Collection<Voters> getAllVoters();

    Collection<Voters> findByCity(String city);

    Collection<Voters> findByStatus(int status);

    //votes........................................................
    void addVote(int voterId, int candidateId, int partyId, Date timeStamp, int electionId, int verifyStatus, String electionName);

    // Get all votes
    Collection<Votes> getAllVotes();

    Votes findByVoteId(int voteId);

    Collection<Votes> findByCandidateId(int candidateId);

    Collection<Votes> findByPartyId(int partyId);

    Collection<Votes> findByElectionId(int electionId);

    Collection<Votes> findByElectionName(String electionName);

    //users.......................
    void addUser(String userName, String email,String mobile, String password, int gId, int verify);
    


}
