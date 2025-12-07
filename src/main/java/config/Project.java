/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;


@DatabaseIdentityStoreDefinition(
        dataSourceLookup = "voting_system/jndi",
        callerQuery = "SELECT password FROM users WHERE user_name = ?",
        groupsQuery = "SELECT g.g_name FROM group_master g JOIN users u ON g.g_id = u.g_id WHERE u.user_name = ?",
        hashAlgorithm = Pbkdf2PasswordHash.class,
        priority = 30
)

@ApplicationScoped
public class Project {

}