# Gold Coast E-Sports Competition App
## Description
This application helps manage competition data effectively using a user-friendly GUI. The app interacts with CSV files to manage teams, matches, and results.

<p align="center">
  <img src="https://github.com/LukeWait/gc-esports-app/raw/main/src/images/gc-esports-app-screenshot.png" alt="App Screenshot" width="600">
</p>

## Table of Contents
- [Installation](#installation)
- [Usage](#usage)
- [Development](#development)
- [License](#license)
- [Acknowledgments](#acknowledgments)
- [Source Code](#source-code)
- [Dependencies](#dependencies)

## Installation
### Prerequisites
Ensure that Java JDK 17 is installed on your system:
- **Windows**: Download and install [Java JDK 17](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html).
- **Linux**: Install Java JDK 17 using your package manager:
  ```sh
  sudo apt-get install openjdk-17-jdk   # For Debian-based systems
  ```

### Executable
1. Download the latest release from the [releases page](https://github.com/LukeWait/gc-esports-app/releases).
2. Extract the contents to a desired location.
3. Run the `gc-esports-app.jar` file:
   - **Windows**: Simply double-click the `gc-esports-app.jar` file to run it.
   - **Linux**: Make the `.jar` file executable and run it:
     ```sh
     chmod +x gc-esports-app.jar
     java -jar gc-esports-app.jar
     ```

### From Source
To install and run the application from source:
1. Clone the repository:
    ```sh
    git clone https://github.com/LukeWait/gc-esports-app.git
    cd gc-esports-app
    ```
2. Open the project directory with Apache NetBeans or another compatible IDE to build and run the application.


## Usage
1. Launch the application.
2. Use the provided functions to interact with the data:
   - Create and view competition data.
   - Create, view, and update team/player data.
   - Note: There is currently no option to delete data.
   - You will be prompted to save changes on exit.

### Application Functions
- **View All Team Competition Results**: Display a comprehensive list of all past competition results.
- **View List of Top Teams**: Show a scoreboard with the top-performing teams based on competition results.
- **Add New Competition Results**: Input and save results for new competitions.
- **Add New Team**: Register a new team, including their contact information.
- **Update Existing Team**: Modify the details of an already registered team.

## Development
This project was developed using Apache NetBeans, an integrated development environment (IDE) that facilitates Java application development. If using a different IDE, you may need to configure the environment to ensure compatibility with the project.

### Project Structure
```sh
gc-esports-app/
├── nbproject/                # NetBeans settings
├── data/                     # CSV storage files
├── src/
│   ├── images/               # GUI design elements
│   └── gcesportsapp/         # Project source code
│       ├── GCEsportsApp.java
│       ├── GCEsportsApp.form
│       ├── Competition.java
│       └── Team.java
├── build.xml                 # Build configuration
└── manifest.mf               # Manifest file for the JAR
```

### Data Storage
The application manages data through three CSV files:
- `competitions.csv`: Contains details of previous competition results.
- `players.csv`: Contains a list of players in each team.
- `teams.csv`: Contains contact information for each team registered.

### Creating New Releases
- **Build the Application**: Use your IDE to compile and package the application into a `.jar` executable.

- **Include CSV Files**: When the build is complete, ensure that the `data` directory containing the `.csv` files is copied to the `dist` folder to provide the persistent memory required by the application.

## License
This project is licensed under the MIT License. See the LICENSE file for details.

## Acknowledgments
This project was developed as part of an assignment at TAFE Queensland for subject ICTPRG430.

Project requirements and initial GUI design/codebase provided by Hans Telford.

## Source Code
The source code for this project can be found in the GitHub repository: [https://github.com/LukeWait/gc-esports-app](https://www.github.com/LukeWait/gc-esports-app).

## Dependencies
- Java JDK 17
