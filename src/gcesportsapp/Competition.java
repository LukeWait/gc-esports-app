/*
File name:  Competition.java
Purpose:    Provides instance variables, constructor and get/set methods 
            for Competition class
Author:     Luke Wait
Date:       03-Sept-2022
Version:    1.0
NOTES:      Complete
 */
package gcesportsapp;


public class Competition 
{
    // private data (instance variables)
    // League of Legends,14-Jan-2022,TAFE Coomera,BioHazards,5
    private String game;
    private String competitionDate;
    private String location;
    private String team;
    private int points;
    
    
    // parameterised constructor method
    public Competition (String game, String competitionDate, String location, 
                        String team, int points)
    {
        this.game = game;
        this.competitionDate = competitionDate;
        this.location = location;
        this.team = team;
        this.points = points;
    }
    
    
    // get methods for private data fields
    public String getGame()
    {
        return game;
    }
    
    public String getCompetitionDate()
    {
        return competitionDate;
    }
    
    public String getLocation()
    {
        return location;
    }
    
    public String getTeam()
    {
        return team;
    }
    
    public int getPoints()
    {
        return points;
    }
    
    
    // set methods for private data fields
    public void setGame(String game)
    {
        this.game = game;
    }
    
    public void setCompetitionDate(String competitionDate)
    {
        this.competitionDate = competitionDate;
    }
    
    public void setLocation(String location)
    {
        this.location = location;
    }
    
    public void setTeam(String team)
    {
        this.team = team;
    }
    
    public void setPoints(int points)
    {
        this.points = points;
    }
    
    
    // overridden toString() method 
    // returns a formatted string to write to competitions.csv
    @Override
    public String toString()
    {
        String rtnStr = game + ",";
               rtnStr += competitionDate + ",";
               rtnStr += location + ",";
               rtnStr += team + ",";
               rtnStr += points;
        return rtnStr;
    }   
}// end public class Competition
