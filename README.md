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
- [Testing](#testing)
- [License](#license)
- [Acknowledgments](#acknowledgments)
- [Source Code](#source-code)
- [Dependencies](#dependencies)

## Installation
### Executable
#### Windows
1. Download the latest Windows release from the [releases page](https://github.com/LukeWait/gc-esports-app/releases).
2. Extract the contents to a desired location.
3. Run the `GCEsportsApp.jar` file.

### From Source
To install and run the application from source:
1. Clone the repository:
    ```sh
    git clone https://github.com/LukeWait/gc-esports-app.git
    cd gc-esports-app
    ```

2. Install Project Dependencies:
    Ensure JDK 17 is installed. You can download it from [Oracle](https://www.oracle.com/java/technologies/javase-downloads.html).

3. Open with Apache Netbeans or other compatible IDE:

## Usage
The application manages competition data through three CSV files:
- `competitions.csv`: Contains details about the history of matches.
- `players.csv`: Contains details the individuals that makeup the teams.
- `teams.csv`: Contains information about the teams.

### Managing CSV Files
1. Launch the application.
2. Use the provided options to edit, update, or view the data.

## Development
### Project Structure
```sh
gc-esports-app/
├── nbproject/
├── src/
│   ├── images/
│   ├── data/
│   └── gcesportsapp/
│       ├── GCEsportsApp.java
│       ├── GCEsportsApp.form
│       ├── Competition.java
│       └── Team.java
├── build.xml
└── manifest.mf
```

## License
This project is licensed under the MIT License. See the LICENSE file for details.

## Acknowledgments
This project was developed as part of an assignment at TAFE Queensland for subject ICTPRG430.

App requirements and boilerplate code provided by Hans Telford.

## Source Code
The source code for this project can be found in the GitHub repository: [https://github.com/LukeWait/gc-esports-app](https://www.github.com/LukeWait/gc-esports-app).

## Dependencies
- JDK 17
