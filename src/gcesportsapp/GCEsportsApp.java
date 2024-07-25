/*
File name: GCEsportsApp.java
Description: Runs the GUI JFrame app
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

// reading
import java.io.BufferedReader;
import java.io.FileReader;
// writing
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
// exceptions
import java.io.FileNotFoundException;
import java.io.IOException;
// utilities
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
// jSwing components
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;


public class GCEsportsApp extends javax.swing.JFrame 
{
    // private data
    // for storing comp results
    private ArrayList<Competition> competitions;
    // for storing team information
    private ArrayList<Team> teams;
    // for customising the jTable (which displays comp results)
    private DefaultTableModel compResultsTableModel;

    
    //************ CONSTRUCTOR METHOD ************
    public GCEsportsApp() 
    {
        //************ INITIALISE PRIVATE DATA FIELDS ************
        competitions = new ArrayList<>();
        teams = new ArrayList<>();
        compResultsTableModel = new DefaultTableModel();
        

        //************ CUSTOMISE TABLE MODEL ************
        // customised column names for competition jTable
        String[] columnNames_Results = new String[]
                                {"Date", "Location", "Game", "Team", "Points"};
        // set up customisation
        compResultsTableModel.setColumnIdentifiers(columnNames_Results);
        
        
        //************ INITIALISE ALL SWING CONTROLS ************ (JSWING GUI)
        initComponents();
        
        
        //************ CUSTOMISE TABLE COLUMNS FOR JTABLE ************
        resizeTableColumns();
        
        
        //************ READ IN EXTERNAL CSV FILES ************        
        // teams.csv must be read before players.csv so the players can be added
        // to the teams ArrayList
        readTeamAndPlayerData();
        // competition data must be after the teams.csv so the totalPoints can be
        // added to the teams ArrayList
        readCompetitionData();
        
        
        //************ DISPLAY DATA IN JTABLE, JCOMBOBOXES & JTEXTFIELDS ************
        displayJTable();
        displayJComboBox();
        // there is no need to call displayTeamData() as populating the jComboBox triggers the
        // action listener event, automatically calling displayTeamData()
        
        // after a lot of experimentation I concluded using a boolean to gate the calling
        // of displayTeamData() was unnecessary. it was initialising the selectedIndex to 0 
        // until the constructor had completed, however getSelectedIndex() defaults to 0 anyway. 
        // the only time this was useful was if you attempt to run the displayTeamData() BEFORE 
        // displayJComboBox(). also, if the teams.csv was empty it broke because it would still 
        // attempt to set data to the fields with an index of 0 (out of bounds). 
        // I opted for a another solution: only display team data if selectedIndex is >= 0.
    }// end constructor method
  

    //************ READ EXTERNAL FILE METHODS ************
    
    private void readTeamAndPlayerData()
    {
        // String to display potential error messages
        String errorMsg = "";
        
        // read from teams.csv and add to teams ArrayList
        try
        {
            // create reader and designate external file to read from
            FileReader fr = new FileReader("data/teams.csv");
            // create bufferedReader which uses the reader object
            BufferedReader br = new BufferedReader(fr);
            // declare line string (used to read and store each line from file)
            String line;
            
            // loop through each line in the external file until EOF
            while  ((line = br.readLine()) != null)
            {
                if (line.length() > 0)
                {
                    // split the line by its delimiter comma
                    String[] lineArray = line.split(",");
                    // set up individual variables for each split line component
                    // Coomera Bombers,James Taylor,0433948765,jamestaylor123@coolmail.com
                    String teamName = lineArray[0];
                    String contactName = lineArray[1];
                    String contactPhone = lineArray[2];
                    String contactEmail = lineArray[3];
                    // add instance to the teams ArrayList
                    teams.add(new Team(teamName, contactName, contactPhone, contactEmail));
                }
            }// end while loop 
            
            // close reader object
            br.close();
        }
        // exceptions for try block, add to errorMsg
        catch (FileNotFoundException fnfe)
        {
            errorMsg += "file teams.csv not found";
        }
        catch (IOException ioe)
        {
            errorMsg += "problem reading from teams.csv";;
        }
        
        try
        {
            // create reading objects
            FileReader fr = new FileReader("data/players.csv");
            BufferedReader br = new BufferedReader(fr);
            String line;

            // loop through each line in the external file until EOF
            while  ((line = br.readLine()) != null)
            {
                if (line.length() > 0)
                {
                    // split the line by delimiter comma and set values in lineArray
                    String[] lineArray = line.split(",");
                    // set up individual variables for each split line component
                    // James Taylor,Coomera Bombers
                    String playerName = lineArray[0];
                    String teamName = lineArray[1];
                    
                    // repeat for number of teams in teams ArrayList
                    for (int i = 0; i < teams.size(); i++)
                    {
                        // if teamName from file is team in ArrayList
                        if (teams.get(i).getTeam().equals(teamName))
                        {
                            // set playerName to players ArrayList within team instance
                            teams.get(i).setPlayers(playerName);
                        }
                    }   
                }
            }// end while loop  

            // close reader object
            br.close();
        }
        // exceptions for try block, add to errorMsg
        catch (FileNotFoundException fnfe)
        {
            errorMsg += "file players.csv not found";
        }
        catch (IOException ioe)
        {
            errorMsg += "problem reading from players.csv";;
        }
        
        // display errorMsg String in pop up only if there is an error
        if(!errorMsg.isEmpty())
        {
            JOptionPane.showMessageDialog(null, "ERROR(S) DETECTED:\n" + errorMsg, "ERRORS DETECTED!",
                                          JOptionPane.ERROR_MESSAGE);
        }
    }// end readTeamAndPlayerData()    
    
    private void readCompetitionData()
    {
        // String to display potential error messages
        String errorMsg = "";
        
        // read from competitions.csv and add to competitions ArrayList
        try
        {
            // create reader and designate external file to read rom
            FileReader fr = new FileReader("data/competitions.csv");
            // create bufferedReader which uses the reader object
            BufferedReader br = new BufferedReader(fr);
            // declare line string (used to read and store each line read from file)
            String line;
            
            // loop through each line in the external file until EOF (end of file)
            while  ((line = br.readLine()) != null)
            {
                if (line.length() > 0)
                {
                    // split the line by its delimiter comma
                    String[] lineArray = line.split(",");
                    // set up individual variables for each split line component
                    // League of Legends,14-Jan-2022,TAFE Coomera,BioHazards,5
                    String game = lineArray[0];
                    String compDate = lineArray[1];
                    String location = lineArray[2];
                    String team = lineArray[3];
                    int points = Integer.parseInt(lineArray[4]);
                    // add instance to the competitions ArrayList
                    competitions.add(new Competition(game, compDate, location, team, points));
                    
                    // add points to appropriate teams
                    // repeat for number of teams in teams ArrayList
                    for (int i = 0; i < teams.size(); i++)
                    {
                        // if team is equal to team in teams ArrayList
                        if (team.equals(teams.get(i).getTeam()))
                        {    
                            // add to totalPoints contained in team instance
                            teams.get(i).setTotalPoints(points);
                        }                            
                    }
                }     
            }// end while loop 
            
            // close reader object
            br.close();
        }
        // exceptions for try block, add to errorMsg
        catch (FileNotFoundException fnfe)
        {
            errorMsg += "file competitions.csv not found";
        }
        catch (IOException ioe)
        {
            errorMsg += "problem reading from competitions.csv";;
        }
        catch (NumberFormatException nfe)
        {
            errorMsg += "points index in competitions.csv contains non-numeric value";
        }
        
        // display errorMsg String in pop up only if there is an error
        if(!errorMsg.isEmpty())
        {
            JOptionPane.showMessageDialog(null, "ERROR(S) DETECTED:\n" + errorMsg, "ERRORS DETECTED!",
                                          JOptionPane.ERROR_MESSAGE);
        }
    }// end readCompetitionData()
    
    
    //************ WRITE EXTERNAL FILE METHODS ************
    
    private void writeTeamData()
    {
        // write to teams.csv
        try
        {
            // create outputStream and designate the external file to write
            FileOutputStream fos = new FileOutputStream("data/teams.csv", false);
            // create outputStreamWriter and designate the character set
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
            // create bufferedWriter which uses the outputStreamWriter
            BufferedWriter bw = new BufferedWriter(osw);

            // repeat for number of teams in teams Arraylist
            for (int i = 0; i < teams.size(); i++)
            {
                // write every team using toString method defined in Team class
                bw.write(teams.get(i).toString());
                bw.newLine(); 
            }             

            // close the bufferedWriter object
            bw.close();
        }
        // exceptions for try block, show error in pop up
        catch (IOException ioe)
        {
            JOptionPane.showMessageDialog(null, "ERROR(S) DETECTED:\n" + "problem writing to teams.csv\n",
                                            "ERRORS DETECTED!", JOptionPane.ERROR_MESSAGE);
        }
    }// end writeTeamData()
    
    private void writePlayerData()
    {
        // write to players.csv
        try
        {
            // create outputStream and designate the external file to write
            FileOutputStream fos = new FileOutputStream("data/players.csv", false);
            // create outputStreamWriter and designate the character set
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
            // create bufferedWriter which uses the outputStreamWriter
            BufferedWriter bw = new BufferedWriter(osw);

            // repeat for number of teams in teams ArrayList
            for (int i = 0; i < teams.size(); i++)
            {
                // scan through players ArrayList in team 
                for (String playerName : teams.get(i).getPlayers())
                {
                    // write players using toCSV method defined in Team class
                    bw.write(teams.get(i).toPlayersString(playerName));
                    bw.newLine();
                }
            }

            // close the bufferedWriter object
            bw.close();
        }
        // exceptions for try block, show error in pop up
        catch (IOException ioe)
        {
            JOptionPane.showMessageDialog(null, "ERROR(S) DETECTED:\n" + "problem writing to players.csv\n",
                                            "ERRORS DETECTED!", JOptionPane.ERROR_MESSAGE);
        } 
    }// end writePlayerData()
    
    private void writeCompetitionData()
    {
        // write to competitions.csv
        try
        {
            // create outputStream and designate the external file to write to
            FileOutputStream fos = new FileOutputStream("data/competitions.csv", false);
            // create outputStreamWriter and designate the character set
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
            // create bufferedWriter which uses the outputStreamWriter to write to file                    
            BufferedWriter bw = new BufferedWriter(osw);

            // repeat for number of competitions in competitions ArrayList
            for (int i = 0; i < competitions.size(); i++)
            {
                // write using toString method defined in Competition class
                bw.write(competitions.get(i).toString());
                bw.newLine();
            }

            // close the bufferedWriter object
            bw.close(); 
        }
        // exceptions for try block, show error in pop up
        catch (IOException ioe)
        {
            JOptionPane.showMessageDialog(null, "ERROR(S) DETECTED:\n" + "problem writing to competitions.csv\n",
                                            "ERRORS DETECTED!", JOptionPane.ERROR_MESSAGE);                          
        }
    }// end writeCompetitionData()
    

    //************ DISPLAY AND UPDATE JSWING METHODS ************
   
    private void displayJTable()
    {
        // populate competition data in JTable
        if (competitions.size() > 0)
        {
            // create Object[] 2D array for JTable
            Object[][] competitions2DArray = new Object[competitions.size()][];
            // populate 2D array from competitions ArrayList
            for (int i = 0; i < competitions.size(); i++)
            {
                // create Object[] for single row of data containing 6 components
                Object[] competition = new Object[5];
                // date
                competition[0] = competitions.get(i).getCompetitionDate();
                // location
                competition[1] = competitions.get(i).getLocation();
                // game
                competition[2] = competitions.get(i).getGame();
                // team
                competition[3] = competitions.get(i).getTeam();
                // points
                competition[4] = competitions.get(i).getPoints();
                // append to 2D array
                competitions2DArray[i] = competition;
            }
            
            // first, remove all existing rows if there are any
            if (compResultsTableModel.getRowCount() > 0)
            {
                for (int i = compResultsTableModel.getRowCount() - 1; i > -1; i--)
                {
                    compResultsTableModel.removeRow(i);
                }
            }
            
            // next, put new set of row data
            if (competitions2DArray.length > 0)
            {
                // add data to tableModel
                for (int row = 0; row < competitions2DArray.length; row++)
                {
                    compResultsTableModel.addRow(competitions2DArray[row]);
                }
                // update
                compResultsTableModel.fireTableDataChanged();
            }   
        }// end competition entry check
    }// end displayJTable()
    
    private void resizeTableColumns()
    {
        // resize compResults_jTable columns
        // Columns: Date, Location, Competition, Platform, Team, Points
        // (total numeric values must = 1)
        double[] columnWidthPercentage = {0.2f, 0.2f, 0.3f, 0.2f, 0.1f};
        int tableWidth = compResults_jTable.getWidth();
        TableColumn column;
        TableColumnModel tableColumnModel = compResults_jTable.getColumnModel();
        int cantCols = tableColumnModel.getColumnCount();
        for (int i = 0; i < cantCols; i++)
        {
            column = tableColumnModel.getColumn(i);
            float pWidth = Math.round(columnWidthPercentage[i] * tableWidth);
            column.setPreferredWidth((int)pWidth);
        }
    }// end resizeTableColumns()
        
    private void displayJComboBox()
    {
        // display teams in jComboBoxes
        
        // no need to removeAllItems() - manually removed everything
        // in the jComboBox properties in the GUI design

        // if there are any teams to display
        if (teams.size() > 0)
        {
            // repeat for number of teams in teams ArrayList
            for (int i = 0; i < teams.size(); i++)
            {
                // add team from teams ArrayList to jComboBoxes
                team_jComboBox.addItem(teams.get(i).getTeam());
                team_jComboBox1.addItem(teams.get(i).getTeam());
            }
        }
    }// end displyJComboBox()  
    
    private void displayTeamData()
    {
        // populate jTextFields in updateExistingTeam tab from jComboBox selection
        
        // check that index isn't out of bounds, will only occur if teams.csv is empty
        if (team_jComboBox1.getSelectedIndex() >= 0)
        {
            // declare int for easy index reference
            int selectedIndex = team_jComboBox1.getSelectedIndex();
            
            // get data from teams ArrayList using get methods defined
            // in Team class and set to jTextFields
            contactPerson_jTextField1.setText(teams.get(selectedIndex).getContactPerson());
            contactPhone_jTextField1.setText(teams.get(selectedIndex).getContactPhone());
            contactEmail_jTextField1.setText(teams.get(selectedIndex).getContactEmail());

            // create String to store players names from teams ArrayList
            String playersNames = "";
            // scan through players ArrayList within teams ArrayList
            for (int i = 0; i < teams.get(selectedIndex).getPlayers().size(); i++)
            {
                // get players ArrayList from selected team and add to playerNames string
                playersNames += teams.get(selectedIndex).getPlayers().get(i) + "\n";   
            }

            // display playerNames String in jTextArea
            playerNames_jTextArea1.setText(playersNames);

            // remove blank line at end of jTextArea
            playerNames_jTextArea1.setText(playerNames_jTextArea1.getText().trim());
        }       
    }// end updateTeamData()
    
    
    //************ SAVE TO ARRAYLISTS METHODS ************
    
    private void saveUpdateTeam()
    {
        // save validated update of team to teams and players ArrayLists
        
        // if all fields have valid entries
        if (validateUpdateTeam() == true)
        {
            // display save update for this team pop up and provide yes/no option to save
            int yesNo = JOptionPane.showConfirmDialog(null, "You are about to save an update to team: "
                                + team_jComboBox1.getSelectedItem() + "\nDo you wish to continue?", 
                                  "UPDATE EXISTING TEAM", JOptionPane.YES_NO_OPTION);
            
            // if yes is selected
            if (yesNo == JOptionPane.YES_OPTION)
            {
                // get index of selected team in jComboBox
                int selectedIndex = team_jComboBox1.getSelectedIndex();               

                // use set methods to modify updated team data in ArrayList
                teams.get(selectedIndex).setContactPerson(contactPerson_jTextField1.getText());
                teams.get(selectedIndex).setContactPhone(contactPhone_jTextField1.getText());
                teams.get(selectedIndex).setContactEmail(contactEmail_jTextField1.getText());
                
                // remove players from team in jComboBox with Team method removePlayers()
                teams.get(selectedIndex).removePlayers();
                
                // split text in jTextArea by \n and pass to players ArrayList in team instance
                for (String playerName : playerNames_jTextArea1.getText().split("\n", -1))
                {
                    // set lines of jTextArea as players
                    teams.get(selectedIndex).setPlayers(playerName);
                }
            }    
        }// end yes confirm button conditional 
    }// end saveUpdateTeam()
    
    private void saveNewTeam()
    {
        // save validated new team to teams and players ArrayLists
        
        // if all fields have valid entries
        if (validateNewTeam() == true)
        {
            // display save new team pop up and provide yes/no option to save
            int yesNo = JOptionPane.showConfirmDialog(null, "You are about to save a new team for: "
                                + teamName_jTextField.getText() + "\nDo you wish to continue?", 
                                  "ADD NEW TEAM", JOptionPane.YES_NO_OPTION);
            
            // if yes is selected
            if (yesNo == JOptionPane.YES_OPTION)
            {
                // create newTeam instance with data from jTextFields
                Team newTeam = new Team(teamName_jTextField.getText(), contactPerson_jTextField.getText(), 
                    contactPhone_jTextField.getText(), contactEmail_jTextField.getText());
                
                // split text in jTextArea by \n and pass to ArrayList in newTeam instance
                for (String playerName : playerNames_jTextArea.getText().split("\n", -1))
                {
                    newTeam.setPlayers(playerName);
                }
                
                // update teams ArrayList and team jComboBoxes
                teams.add(newTeam);
                team_jComboBox.addItem(newTeam.getTeam());
                team_jComboBox1.addItem(newTeam.getTeam()); 
            }          
        }// end yes confirm button conditional
    }// end saveNewTeam()
    
    private void saveNewComp()
    {
        // save validated comp results to competitions ArrayList
        
        // if all fields have valid entries
        if (validateNewComp() == true)
        {        
            // display new comp result pop up and provide yes/no option to save
            int yesNO = JOptionPane.showConfirmDialog(null, "You are about to save a new competition entry for team: "
                                + team_jComboBox.getSelectedItem() + "\nDo you wish to continue?", 
                                  "ADD NEW COMPETITION RESULT", JOptionPane.YES_NO_OPTION);
            
            // if yes is selected
            if (yesNO == JOptionPane.YES_OPTION)
            {
                // create newComp object and populate with jTextField data
                Competition newComp = new Competition(game_jTextField.getText(), date_jTextField.getText(), 
                    location_jTextField.getText(), team_jComboBox.getSelectedItem().toString(), 
                    Integer.parseInt(points_jTextField.getText()));
                
                // add newComp to competitions ArrayList and update jTable
                competitions.add(newComp);
                displayJTable();
                
                // add points to totalPoints of team in jComboBox
                // repeat for number of teams in teams ArrayList
                for (int i = 0; i < teams.size(); i++)
                {
                    // if team in teams ArrayList matches team in jComboBox
                    if (teams.get(i).getTeam().equals(team_jComboBox.getSelectedItem().toString()))
                    {
                        // add points from jTextField to totalPoints of team instance
                        teams.get(i).setTotalPoints(Integer.parseInt(points_jTextField.getText()));
                    }
                }   
            }// end yes jOptionPane selection              
        }
    }// end saveNewComp()
    
    
    //************ VALIDATION METHODS ************
    
    private boolean validateUpdateTeam()
    {
        // validate all fields in Update an existing team panel
        
        // String to add any failed checks to error pop up
        String errorMsg = "";
        // boolean to validate if any empty fields exist
        boolean validUpdateTeam = true;

        // check that each field isn't empty and if it is
        // add error to errorMsg string and change errorDetected to true
        if (team_jComboBox1.getSelectedItem().toString().isEmpty())
        {
            errorMsg += "team name is required\n";
            validUpdateTeam = false;
        }
        if (contactPerson_jTextField1.getText().isEmpty())
        {
            errorMsg += "contact person's name is required\n";
            validUpdateTeam = false;
        }
        if (contactPhone_jTextField1.getText().isEmpty())
        {
            errorMsg += "contact phone number is required\n";
            validUpdateTeam = false;
        }
        if (contactEmail_jTextField1.getText().isEmpty())
        {
            errorMsg += "contact email address is required\n";
            validUpdateTeam = false;
        }
        // split text in jTextArea by \n and check for empty lines
        for (String playerName : playerNames_jTextArea1.getText().split("\n", -1))
        {
            if (playerName.isEmpty()) 
            {
                validUpdateTeam = false;
                errorMsg += "player names text field contains an invalid blank line\n";
            }
        }   

        // display errorMsg String in pop up only if there is an error
        if(!errorMsg.isEmpty())
        {
            JOptionPane.showMessageDialog(null, "ERROR(S) DETECTED:\n" + errorMsg, "ERRORS DETECTED!",
                                          JOptionPane.ERROR_MESSAGE);
        }
        
        // return true if no errors detected
        return validUpdateTeam;
    }//end validateUpdateTeam()
    
    private boolean validateNewTeam()
    {
        // validate all fields in Add new team panel
        
        // String to add any failed checks to error pop up
        String errorMsg = "";
        // boolean to validate if any empty fields exist
        boolean validNewTeam = true;

        // check that each field isn't empty and if it is
        // add error to errorMsg string and change errorDetected to true
        if (teamName_jTextField.getText().isEmpty())
        {
            errorMsg += "team name is required\n";
            validNewTeam = false;
        }
        if (contactPerson_jTextField.getText().isEmpty())
        {
            errorMsg += "contact person's name is required\n";
            validNewTeam = false;
        }
        if (contactPhone_jTextField.getText().isEmpty())
        {
            errorMsg += "contact phone number is required\n";
            validNewTeam = false;
        }
        if (contactEmail_jTextField.getText().isEmpty())
        {
            errorMsg += "contact email address is required\n";
            validNewTeam = false;
        }
        // split text in jtextarea by \n and check for empty lines
        for (String playerName : playerNames_jTextArea.getText().split("\n", -1))
        {
            if (playerName.isEmpty()) 
            {
                errorMsg += "player names text field contains an invalid blank line\n";
                validNewTeam = false;
            }
        }
        // check that team doesn't already exist
        for (int i = 0; i < teams.size(); i++)
        {
            if (teams.get(i).getTeam().equals(teamName_jTextField.getText()))
            {
                errorMsg += "this team already exists\n";
                validNewTeam = false;
            }
        }

        // display errorMsg String in pop up only if there is an error
        if(!errorMsg.isEmpty())
        {
            JOptionPane.showMessageDialog(null, "ERROR(S) DETECTED:\n" + errorMsg, "ERRORS DETECTED!",
                                          JOptionPane.ERROR_MESSAGE);
        }
        
        // return true if no errors detected
        return validNewTeam;
    }//end validateNewTeam()
    
    private boolean validateNewComp()
    {
        // validate all fields in Add new competition results panel
        
        // String to add any failed checks to error pop up
        String errorMsg = "";
        // boolean to validate if any empty fields exist
        boolean validNewComp = true;

        // check that each field isn't empty and if it is
        // add error to errorMsg string and change errorDetected to true
        if (date_jTextField.getText().isEmpty())
        {
            errorMsg += "competition date is required\n";
            validNewComp = false;
        }
        if (location_jTextField.getText().isEmpty())
        {
            errorMsg += "competition location is required\n";
            validNewComp = false;
        }
        if (game_jTextField.getText().isEmpty())
        {
            errorMsg += "game is required\n";
            validNewComp = false;
        }
        if (team_jComboBox.getSelectedItem().toString().isEmpty())
        {
            errorMsg += "team name is required\n";
            validNewComp = false;
        }
        // check that points jTextField is an integer >= 0
        try
        {
            if (Integer.parseInt(points_jTextField.getText()) < 0)
            {
                errorMsg += "points must be a number >= 0\n";
                validNewComp = false;
            }
            // points jTextField has been validated as int
            // check that comp entry doesn't already exist
            else
            {
                // scan all competitions entries and if data of entry matches jTextFields
                // indicate that the competition results being entered already exist
                for (int i = 0; i < competitions.size(); i++)
                {
                    if (competitions.get(i).getCompetitionDate().equals(date_jTextField.getText()) &&
                        competitions.get(i).getLocation().equals(location_jTextField.getText()) &&
                        competitions.get(i).getGame().equals(game_jTextField.getText()) &&
                        competitions.get(i).getTeam().equals(team_jComboBox.getSelectedItem().toString()) &&
                        competitions.get(i).getPoints() == Integer.parseInt(points_jTextField.getText()))
                    {
                        errorMsg += "this competition results entry already exists\n";
                        validNewComp = false;
                    }
                }
            }
        }
        // catch attempt to parseInt anything but an integer
        catch (NumberFormatException nfe)
        {
            errorMsg += "points must be a number >= 0\n";
            validNewComp = false;
        }

        // display errorMsg String in pop up only if there is an error
        if(!errorMsg.isEmpty())
        {
            JOptionPane.showMessageDialog(null, "ERROR(S) DETECTED:\n" + errorMsg, "ERRORS DETECTED!",
                                          JOptionPane.ERROR_MESSAGE);
        }
        
        // return true if no errors detected
        return validNewComp;
    }// end validateNewComp()
    
    
    //************ ADDITIONAL FUNCTION METHODS ************
    
    private void displayTopTeams()
    {
        // display pop up of team names and total points in decending order
        
        // title and columns for JOptionPane
        String aboutMsg = "TEAMS LEADER BOARD\n\n"
                        + "Points   Team\n";

        // added totalPoints variable to Team class and applied points to appropriate teams
        // when reading competitions.csv and when new comp results are saved
        
        // create a copy of teams ArrayList to sort without effecting the original
        // this will avoid problems arising due to a change of team index
        ArrayList<Team> teamsCopy = new ArrayList<>(teams);
        
        // use Collections and Comparator classes to sort: function takes teamsCopy ArrayList, 
        // compares int values of totalPoints and orders the list in descending order
        Collections.sort(teamsCopy, Comparator.comparingInt(Team::getTotalPoints).reversed());
        
        // repeat for number of teams in teamsCopy ArrayList
        for (int i = 0; i < teamsCopy.size(); i++)
        {
            // add totalPoints and team to aboutMsg for display
            aboutMsg += teamsCopy.get(i).getTotalPoints() + "           " + teamsCopy.get(i).getTeam() + "\n";
        }
        
        // display aboutMsg in JOptionPane pop up
        JOptionPane.showMessageDialog(null, aboutMsg, "TEAMS LEADER BOARD",
                JOptionPane.INFORMATION_MESSAGE);
    }// end displayTopTeams()
   
    private void saveOnExit()
    {
        // save to external csv's option when exiting application
        
        // display save when closing pop up and provide yes/no option to save
        int yesNO = JOptionPane.showConfirmDialog(null, "Do you want to save entered data"
                + " to disk before closing application?", 
                "SAVE BEFORE EXIT", JOptionPane.YES_NO_OPTION);

        // if yes is selected
        if (yesNO == JOptionPane.YES_OPTION)
        {
            // write to teams.csv
            writeTeamData();
            // write to players.csv
            writePlayerData();
            // write to competitions.csv
            writeCompetitionData();   
        }
    }// end saveOnExit()
           
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        header_jPanel = new javax.swing.JPanel();
        image_jLabel = new javax.swing.JLabel();
        body_jPanel = new javax.swing.JPanel();
        body_jTabbedPane = new javax.swing.JTabbedPane();
        teamCompetitionResults_jPanel = new javax.swing.JPanel();
        compResults_jScrollPane = new javax.swing.JScrollPane();
        compResults_jTable = new javax.swing.JTable();
        displayTopTeams_jButton = new javax.swing.JButton();
        teamCompetitionResults_jLabel = new javax.swing.JLabel();
        addNewCompetitionResults_jPanel = new javax.swing.JPanel();
        addNewCompetitionResult_jLabel = new javax.swing.JLabel();
        date_jLabel = new javax.swing.JLabel();
        date_jTextField = new javax.swing.JTextField();
        location_jTextField = new javax.swing.JTextField();
        location_jLabel = new javax.swing.JLabel();
        game_jLabel = new javax.swing.JLabel();
        team_jLabel = new javax.swing.JLabel();
        game_jTextField = new javax.swing.JTextField();
        points_jLabel = new javax.swing.JLabel();
        saveNewCompetitionResult_jButton = new javax.swing.JButton();
        points_jTextField = new javax.swing.JTextField();
        team_jComboBox = new javax.swing.JComboBox<>();
        addNewTeam_jPanel = new javax.swing.JPanel();
        contactEmail_jTextField = new javax.swing.JTextField();
        contactPhone_jTextField = new javax.swing.JTextField();
        contactPerson_jTextField = new javax.swing.JTextField();
        teamName_jTextField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        playerNames_jTextArea = new javax.swing.JTextArea();
        saveNewTeam_jButton = new javax.swing.JButton();
        addNewTeam_jLabel1 = new javax.swing.JLabel();
        teamName_jLabel = new javax.swing.JLabel();
        contactPhone_jLabel = new javax.swing.JLabel();
        contactPerson_jLabel = new javax.swing.JLabel();
        contactEmail_jLabel = new javax.swing.JLabel();
        playerNames_jLabel = new javax.swing.JLabel();
        updateExistingTeam_jPanel = new javax.swing.JPanel();
        updateAnExistingTeam_jLabel = new javax.swing.JLabel();
        team_jLabel1 = new javax.swing.JLabel();
        contactPerson_jLabel1 = new javax.swing.JLabel();
        contactPhone_jLabel1 = new javax.swing.JLabel();
        contactEmail_jLabel1 = new javax.swing.JLabel();
        playerNames_jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        playerNames_jTextArea1 = new javax.swing.JTextArea();
        contactEmail_jTextField1 = new javax.swing.JTextField();
        contactPhone_jTextField1 = new javax.swing.JTextField();
        contactPerson_jTextField1 = new javax.swing.JTextField();
        saveUpdateForThisTeam_jButton = new javax.swing.JButton();
        team_jComboBox1 = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setResizable(false);
        setSize(new java.awt.Dimension(798, 600));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        header_jPanel.setBackground(new java.awt.Color(255, 255, 255));

        image_jLabel.setBackground(new java.awt.Color(255, 255, 255));
        image_jLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/gc-esports-header.jpg"))); // NOI18N
        image_jLabel.setMaximumSize(null);
        image_jLabel.setMinimumSize(null);
        image_jLabel.setPreferredSize(null);

        javax.swing.GroupLayout header_jPanelLayout = new javax.swing.GroupLayout(header_jPanel);
        header_jPanel.setLayout(header_jPanelLayout);
        header_jPanelLayout.setHorizontalGroup(
            header_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(header_jPanelLayout.createSequentialGroup()
                .addComponent(image_jLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        header_jPanelLayout.setVerticalGroup(
            header_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(image_jLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        body_jPanel.setBackground(new java.awt.Color(255, 255, 255));

        body_jTabbedPane.setBackground(new java.awt.Color(255, 255, 255));

        teamCompetitionResults_jPanel.setBackground(new java.awt.Color(255, 255, 255));

        compResults_jTable.setModel(compResultsTableModel);
        compResults_jTable.setToolTipText("");
        compResults_jScrollPane.setViewportView(compResults_jTable);

        displayTopTeams_jButton.setText("Display top teams");
        displayTopTeams_jButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                displayTopTeams_jButtonActionPerformed(evt);
            }
        });

        teamCompetitionResults_jLabel.setBackground(new java.awt.Color(255, 255, 255));
        teamCompetitionResults_jLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        teamCompetitionResults_jLabel.setText("Team Competition Results");

        javax.swing.GroupLayout teamCompetitionResults_jPanelLayout = new javax.swing.GroupLayout(teamCompetitionResults_jPanel);
        teamCompetitionResults_jPanel.setLayout(teamCompetitionResults_jPanelLayout);
        teamCompetitionResults_jPanelLayout.setHorizontalGroup(
            teamCompetitionResults_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(teamCompetitionResults_jPanelLayout.createSequentialGroup()
                .addContainerGap(48, Short.MAX_VALUE)
                .addGroup(teamCompetitionResults_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(teamCompetitionResults_jLabel)
                    .addGroup(teamCompetitionResults_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(displayTopTeams_jButton)
                        .addComponent(compResults_jScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 696, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(49, Short.MAX_VALUE))
        );
        teamCompetitionResults_jPanelLayout.setVerticalGroup(
            teamCompetitionResults_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(teamCompetitionResults_jPanelLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(teamCompetitionResults_jLabel)
                .addGap(18, 18, 18)
                .addComponent(compResults_jScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(displayTopTeams_jButton)
                .addContainerGap())
        );

        body_jTabbedPane.addTab("Team competition results", teamCompetitionResults_jPanel);

        addNewCompetitionResults_jPanel.setBackground(new java.awt.Color(255, 255, 255));

        addNewCompetitionResult_jLabel.setBackground(new java.awt.Color(255, 255, 255));
        addNewCompetitionResult_jLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        addNewCompetitionResult_jLabel.setText("Add new competition result");

        date_jLabel.setBackground(new java.awt.Color(255, 255, 255));
        date_jLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        date_jLabel.setText("Date:");

        location_jLabel.setBackground(new java.awt.Color(255, 255, 255));
        location_jLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        location_jLabel.setText("Location:");

        game_jLabel.setBackground(new java.awt.Color(255, 255, 255));
        game_jLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        game_jLabel.setText("Game:");

        team_jLabel.setBackground(new java.awt.Color(255, 255, 255));
        team_jLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        team_jLabel.setText("Team:");

        points_jLabel.setBackground(new java.awt.Color(255, 255, 255));
        points_jLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        points_jLabel.setText("Points:");

        saveNewCompetitionResult_jButton.setText("SAVE NEW COMPETITION RESULT");
        saveNewCompetitionResult_jButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveNewCompetitionResult_jButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout addNewCompetitionResults_jPanelLayout = new javax.swing.GroupLayout(addNewCompetitionResults_jPanel);
        addNewCompetitionResults_jPanel.setLayout(addNewCompetitionResults_jPanelLayout);
        addNewCompetitionResults_jPanelLayout.setHorizontalGroup(
            addNewCompetitionResults_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addNewCompetitionResults_jPanelLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(addNewCompetitionResults_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addNewCompetitionResult_jLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(addNewCompetitionResults_jPanelLayout.createSequentialGroup()
                        .addGroup(addNewCompetitionResults_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(date_jLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(game_jLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(location_jLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
                            .addComponent(team_jLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(points_jLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(29, 29, 29)
                        .addGroup(addNewCompetitionResults_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(addNewCompetitionResults_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(saveNewCompetitionResult_jButton)
                                .addGroup(addNewCompetitionResults_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(location_jTextField)
                                    .addComponent(date_jTextField)
                                    .addComponent(points_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(team_jComboBox, 0, 270, Short.MAX_VALUE)))
                            .addComponent(game_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(20, 321, Short.MAX_VALUE))
        );
        addNewCompetitionResults_jPanelLayout.setVerticalGroup(
            addNewCompetitionResults_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addNewCompetitionResults_jPanelLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(addNewCompetitionResult_jLabel)
                .addGap(18, 18, 18)
                .addGroup(addNewCompetitionResults_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(date_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(date_jLabel))
                .addGap(18, 18, 18)
                .addGroup(addNewCompetitionResults_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(location_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(location_jLabel))
                .addGap(18, 18, 18)
                .addGroup(addNewCompetitionResults_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(game_jLabel)
                    .addComponent(game_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(addNewCompetitionResults_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(team_jLabel)
                    .addComponent(team_jComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(addNewCompetitionResults_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(points_jLabel)
                    .addComponent(points_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 148, Short.MAX_VALUE)
                .addComponent(saveNewCompetitionResult_jButton)
                .addGap(23, 23, 23))
        );

        body_jTabbedPane.addTab("Add new competition results", addNewCompetitionResults_jPanel);

        addNewTeam_jPanel.setBackground(new java.awt.Color(255, 255, 255));

        playerNames_jTextArea.setColumns(20);
        playerNames_jTextArea.setRows(5);
        jScrollPane1.setViewportView(playerNames_jTextArea);

        saveNewTeam_jButton.setText("SAVE NEW TEAM");
        saveNewTeam_jButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveNewTeam_jButtonActionPerformed(evt);
            }
        });

        addNewTeam_jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        addNewTeam_jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        addNewTeam_jLabel1.setText("Add new team");

        teamName_jLabel.setBackground(new java.awt.Color(255, 255, 255));
        teamName_jLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        teamName_jLabel.setText("Team Name:");

        contactPhone_jLabel.setBackground(new java.awt.Color(255, 255, 255));
        contactPhone_jLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        contactPhone_jLabel.setText("Contact Phone:");

        contactPerson_jLabel.setBackground(new java.awt.Color(255, 255, 255));
        contactPerson_jLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        contactPerson_jLabel.setText("Contact Person:");

        contactEmail_jLabel.setBackground(new java.awt.Color(255, 255, 255));
        contactEmail_jLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        contactEmail_jLabel.setText("Contact Email:");

        playerNames_jLabel.setBackground(new java.awt.Color(255, 255, 255));
        playerNames_jLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        playerNames_jLabel.setText("Player Names:");

        javax.swing.GroupLayout addNewTeam_jPanelLayout = new javax.swing.GroupLayout(addNewTeam_jPanel);
        addNewTeam_jPanel.setLayout(addNewTeam_jPanelLayout);
        addNewTeam_jPanelLayout.setHorizontalGroup(
            addNewTeam_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addNewTeam_jPanelLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(addNewTeam_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(addNewTeam_jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(teamName_jLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(contactPhone_jLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(contactPerson_jLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(contactEmail_jLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(playerNames_jLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(29, 29, 29)
                .addGroup(addNewTeam_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(saveNewTeam_jButton)
                    .addGroup(addNewTeam_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(contactEmail_jTextField)
                        .addComponent(contactPhone_jTextField)
                        .addComponent(contactPerson_jTextField)
                        .addComponent(teamName_jTextField)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(321, Short.MAX_VALUE))
        );
        addNewTeam_jPanelLayout.setVerticalGroup(
            addNewTeam_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addNewTeam_jPanelLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(addNewTeam_jLabel1)
                .addGap(18, 18, 18)
                .addGroup(addNewTeam_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(teamName_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(teamName_jLabel))
                .addGap(18, 18, 18)
                .addGroup(addNewTeam_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(contactPerson_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(contactPerson_jLabel))
                .addGap(18, 18, 18)
                .addGroup(addNewTeam_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(contactPhone_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(contactPhone_jLabel))
                .addGap(18, 18, 18)
                .addGroup(addNewTeam_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(contactEmail_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(contactEmail_jLabel))
                .addGap(18, 18, 18)
                .addGroup(addNewTeam_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
                    .addGroup(addNewTeam_jPanelLayout.createSequentialGroup()
                        .addComponent(playerNames_jLabel)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addComponent(saveNewTeam_jButton)
                .addGap(23, 23, 23))
        );

        body_jTabbedPane.addTab("Add new team", addNewTeam_jPanel);

        updateExistingTeam_jPanel.setBackground(new java.awt.Color(255, 255, 255));

        updateAnExistingTeam_jLabel.setBackground(new java.awt.Color(255, 255, 255));
        updateAnExistingTeam_jLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        updateAnExistingTeam_jLabel.setText("Update an existing team");

        team_jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        team_jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        team_jLabel1.setText("Team:");

        contactPerson_jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        contactPerson_jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        contactPerson_jLabel1.setText("Contact Person:");

        contactPhone_jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        contactPhone_jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        contactPhone_jLabel1.setText("Contact Phone:");

        contactEmail_jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        contactEmail_jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        contactEmail_jLabel1.setText("Contact Email:");

        playerNames_jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        playerNames_jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        playerNames_jLabel1.setText("Player Names:");

        playerNames_jTextArea1.setColumns(20);
        playerNames_jTextArea1.setRows(5);
        jScrollPane2.setViewportView(playerNames_jTextArea1);

        saveUpdateForThisTeam_jButton.setText("SAVE UPDATE FOR THIS TEAM");
        saveUpdateForThisTeam_jButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveUpdateForThisTeam_jButtonActionPerformed(evt);
            }
        });

        team_jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                team_jComboBox1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout updateExistingTeam_jPanelLayout = new javax.swing.GroupLayout(updateExistingTeam_jPanel);
        updateExistingTeam_jPanel.setLayout(updateExistingTeam_jPanelLayout);
        updateExistingTeam_jPanelLayout.setHorizontalGroup(
            updateExistingTeam_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(updateExistingTeam_jPanelLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(updateExistingTeam_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(updateExistingTeam_jPanelLayout.createSequentialGroup()
                        .addGroup(updateExistingTeam_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(team_jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(contactPhone_jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(contactPerson_jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
                            .addComponent(contactEmail_jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(playerNames_jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(29, 29, 29)
                        .addGroup(updateExistingTeam_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(saveUpdateForThisTeam_jButton)
                            .addGroup(updateExistingTeam_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(contactEmail_jTextField1)
                                .addComponent(contactPhone_jTextField1)
                                .addComponent(contactPerson_jTextField1)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                                .addComponent(team_jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addComponent(updateAnExistingTeam_jLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(321, Short.MAX_VALUE))
        );
        updateExistingTeam_jPanelLayout.setVerticalGroup(
            updateExistingTeam_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(updateExistingTeam_jPanelLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(updateAnExistingTeam_jLabel)
                .addGap(18, 18, 18)
                .addGroup(updateExistingTeam_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(team_jLabel1)
                    .addComponent(team_jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(updateExistingTeam_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(contactPerson_jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(contactPerson_jLabel1))
                .addGap(18, 18, 18)
                .addGroup(updateExistingTeam_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(contactPhone_jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(contactPhone_jLabel1))
                .addGap(18, 18, 18)
                .addGroup(updateExistingTeam_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(contactEmail_jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(contactEmail_jLabel1))
                .addGap(18, 18, 18)
                .addGroup(updateExistingTeam_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
                    .addGroup(updateExistingTeam_jPanelLayout.createSequentialGroup()
                        .addComponent(playerNames_jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addComponent(saveUpdateForThisTeam_jButton)
                .addGap(23, 23, 23))
        );

        body_jTabbedPane.addTab("Update an existing team", updateExistingTeam_jPanel);

        javax.swing.GroupLayout body_jPanelLayout = new javax.swing.GroupLayout(body_jPanel);
        body_jPanel.setLayout(body_jPanelLayout);
        body_jPanelLayout.setHorizontalGroup(
            body_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(body_jTabbedPane)
        );
        body_jPanelLayout.setVerticalGroup(
            body_jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(body_jTabbedPane, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(header_jPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(body_jPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(header_jPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(body_jPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    private void saveUpdateForThisTeam_jButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveUpdateForThisTeam_jButtonActionPerformed
        // call saveUpdateTeam() when user clicks saveUpdateForThisTeam_jButton
        saveUpdateTeam();
    }//GEN-LAST:event_saveUpdateForThisTeam_jButtonActionPerformed
  
    
    private void saveNewTeam_jButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveNewTeam_jButtonActionPerformed
        // call saveNewTeam() when user clicks saveNewTeam_jButton
        saveNewTeam();
    }//GEN-LAST:event_saveNewTeam_jButtonActionPerformed

    
    private void saveNewCompetitionResult_jButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveNewCompetitionResult_jButtonActionPerformed
        // call saveNewComp() when user clicks saveNewCompetitionResult_jButton
        saveNewComp();
    }//GEN-LAST:event_saveNewCompetitionResult_jButtonActionPerformed
  
    
    private void displayTopTeams_jButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_displayTopTeams_jButtonActionPerformed
        // call displayTopTeams() when user clicks displayTopTeams_jButton
        displayTopTeams();
    }//GEN-LAST:event_displayTopTeams_jButtonActionPerformed
   
    
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // call saveOnExit() when closing application
        saveOnExit();
    }//GEN-LAST:event_formWindowClosing

    
    private void team_jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_team_jComboBox1ActionPerformed
        // orignally used itemStateChanged - it worked fine but noticed it
        // triggers twice, once for the previous state, and then a second a time for
        // the new state. using action listener triggers just once with the new state
        
        // call displayTeamData() when user changes team_jComboBox1 selection
        displayTeamData();
    }//GEN-LAST:event_team_jComboBox1ActionPerformed

    
    public static void main(String args[]) 
    {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GCEsportsApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GCEsportsApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GCEsportsApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GCEsportsApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() 
        {
            public void run() 
            {
                new GCEsportsApp().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel addNewCompetitionResult_jLabel;
    private javax.swing.JPanel addNewCompetitionResults_jPanel;
    private javax.swing.JLabel addNewTeam_jLabel1;
    private javax.swing.JPanel addNewTeam_jPanel;
    private javax.swing.JPanel body_jPanel;
    private javax.swing.JTabbedPane body_jTabbedPane;
    private javax.swing.JScrollPane compResults_jScrollPane;
    private javax.swing.JTable compResults_jTable;
    private javax.swing.JLabel contactEmail_jLabel;
    private javax.swing.JLabel contactEmail_jLabel1;
    private javax.swing.JTextField contactEmail_jTextField;
    private javax.swing.JTextField contactEmail_jTextField1;
    private javax.swing.JLabel contactPerson_jLabel;
    private javax.swing.JLabel contactPerson_jLabel1;
    private javax.swing.JTextField contactPerson_jTextField;
    private javax.swing.JTextField contactPerson_jTextField1;
    private javax.swing.JLabel contactPhone_jLabel;
    private javax.swing.JLabel contactPhone_jLabel1;
    private javax.swing.JTextField contactPhone_jTextField;
    private javax.swing.JTextField contactPhone_jTextField1;
    private javax.swing.JLabel date_jLabel;
    private javax.swing.JTextField date_jTextField;
    private javax.swing.JButton displayTopTeams_jButton;
    private javax.swing.JLabel game_jLabel;
    private javax.swing.JTextField game_jTextField;
    private javax.swing.JPanel header_jPanel;
    private javax.swing.JLabel image_jLabel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel location_jLabel;
    private javax.swing.JTextField location_jTextField;
    private javax.swing.JLabel playerNames_jLabel;
    private javax.swing.JLabel playerNames_jLabel1;
    private javax.swing.JTextArea playerNames_jTextArea;
    private javax.swing.JTextArea playerNames_jTextArea1;
    private javax.swing.JLabel points_jLabel;
    private javax.swing.JTextField points_jTextField;
    private javax.swing.JButton saveNewCompetitionResult_jButton;
    private javax.swing.JButton saveNewTeam_jButton;
    private javax.swing.JButton saveUpdateForThisTeam_jButton;
    private javax.swing.JLabel teamCompetitionResults_jLabel;
    private javax.swing.JPanel teamCompetitionResults_jPanel;
    private javax.swing.JLabel teamName_jLabel;
    private javax.swing.JTextField teamName_jTextField;
    private javax.swing.JComboBox<String> team_jComboBox;
    private javax.swing.JComboBox<String> team_jComboBox1;
    private javax.swing.JLabel team_jLabel;
    private javax.swing.JLabel team_jLabel1;
    private javax.swing.JLabel updateAnExistingTeam_jLabel;
    private javax.swing.JPanel updateExistingTeam_jPanel;
    // End of variables declaration//GEN-END:variables
}
