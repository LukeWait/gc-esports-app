/*
File name: Team.java
Description: Provides instance variables, constructor and get/set methods 
             for Team class
Version: 1.2.0
Author: ┬  ┬ ┬┬┌─┌─┐┬ ┬┌─┐╦╔╦╗
        │  │ │├┴┐├┤ │││├─┤║ ║
        ┴─┘└─┘┴ ┴└─┘└┴┘┴ ┴╩ ╩
Date: September 03, 2022
License: MIT License

Dependencies:
Java JDK 17

GitHub Repository: https://github.com/LukeWait/gc-esports-app
*/
package gcesportsapp;

import java.util.ArrayList;


public class Team 
{
    // private data (instance variables)
    // Coomera Bombers,James Taylor,0433948765,jamestaylor123@coolmail.com
    private String team;
    private String contactPerson;
    private String contactPhone;
    private String contactEmail;
    // for storing player list
    private ArrayList<String> players;
    // for storing totalPoints from competitions
    private int totalPoints;
            
    
    // default constructor method
    public Team ()
    {
        team = "not provided";
        contactPerson = "not provided";
        contactPhone = "not provided";
        contactEmail = "not provided";
        players = new ArrayList<String>();
        totalPoints = 0;
    }
    
    // parameterized constructor method
    public Team (String team, String contactPerson, String contactPhone, 
                 String contactEmail)
    {
        this.team = team;
        this.contactPerson = contactPerson;
        this.contactPhone = contactPhone;
        this.contactEmail = contactEmail;
        players = new ArrayList<String>();
        totalPoints = 0;
    }
    
    
    // get methods for private data fields
    public String getTeam()
    {
        return team;
    }
    
    public String getContactPerson()
    {
        return contactPerson;
    }
    
    public String getContactPhone()
    {
        return contactPhone;
    }
    
    public String getContactEmail()
    {
        return contactEmail;
    }
    
    public ArrayList<String> getPlayers()
    {
        return players;
    }
    
    public int getTotalPoints()
    {
        return totalPoints;
    }
    
    
    // set methods for private data fields
    public void setTeam(String team)
    {
        this.team = team;
    }
    
    public void setContactPerson(String contactPerson)
    {
        this.contactPerson = contactPerson;
    }
    
    public void setContactPhone(String contactPhone)
    {
        this.contactPhone = contactPhone;
    }
    
    public void setContactEmail(String contactEmail)
    {
        this.contactEmail = contactEmail;
    }
    
    public void setPlayers(String playerName)
    {
        players.add(playerName);
    }
    
    public void setTotalPoints(int points)
    {
        totalPoints += points;
    }
    
    
    // method to remove players from players ArrayList
    public void removePlayers()
    {
        players.removeAll(players);
    }
    
    
    // overridden toString() method 
    // returns a formatted string to write to teams.csv
    @Override
    public String toString()
    {
        String rtnStr = team + ",";
               rtnStr += contactPerson + ",";
               rtnStr += contactPhone + ",";
               rtnStr += contactEmail;
        return rtnStr;
    }
    
    // returns a formatted string to write to players.csv
    public String toPlayersString(String playerName)
    {
        String csvStr = playerName + ",";
               csvStr += team;
        return csvStr;
    }
}// end public class Team