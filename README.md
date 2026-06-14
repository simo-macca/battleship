# Battleship in Java

A full-featured implementation of the classic Battleship board game written in Java. Built using the JTamaro library for the graphical user interface, this project features interactive grid gameplay, custom graphics, and multiple game modes including an intelligent AI opponent.

## Architecture Overview

The project is structured with a strict Model-View separation to keep the logic clean and decoupled from the graphics:

- **Model (`project.model`):** The computational core and business logic. It handles the game state, grid validation, ship placement, weapon ballistics, and AI decision-making. The classes in this package are fully covered by unit tests.
- **UI (`project.ui`):** The visual interface built with `jtamaro`. It listens to state changes and user inputs to render the canvas, animations, phase transitions, and interactive grids.

## Features

- **Multiple Game Modes:** Play locally against a friend (Human vs. Human) or challenge the AI (Human vs. Computer).
- **Adjustable AI Difficulty:**
  - *Easy:* The computer uses a random targeting strategy.
  - *Hard:* The computer uses a logical targeting strategy to hunt down your ships efficiently.
- **Interactive UI:** A fully custom graphical interface for placing ships, selecting game modes, and firing at the opponent's grid.
- **Weapon System:** Support for different kinds of weapons and projectiles during the attack phase.

## Prerequisites

To run this game, you must have **Java 17** installed. The project uses the Gradle wrapper, so you do not need to install Gradle manually.

## How to Run

### Option 1: Using the Command Line

1. Clone this repository to your local machine.
    ```bash
    git clone git@github.com:simo-macca/battleship.git
    ```
2. Open your terminal and navigate to the root of the repository.
3. Execute the game by running:
   ```bash
   ./gradlew run
   ```

### Option 2: Using Visual Studio Code / IDE

1. Open the project folder in your IDE.
2. Navigate to `src/main/java/project/Main.java`.
3. Run the `main` method directly from your IDE's run button.

## Controls

* **Mouse Click**: Place ships, select targets, and fire weapons.
* **E**: Select Easy difficulty
* **H**: Select Hard difficulty
* **R**: Reset / Restart the game
* **Q**: Quit the application

## Project Structure

```text
battleship/
├── src/main/java/project/  # Main application source code
│   ├── model/              # Business logic, game state, and entities
│   ├── ui/                 # Graphical interface and rendering components
│   └── Main.java           # Application entry point
├── src/test/java/project/  # JUnit unit tests for the model package
├── deps/                   # Local dependencies (JTamaro, CPD plugin)
└── build.gradle            # Gradle configuration and plugin setup

```

## Development & Code Quality

This project includes several automated tools to maintain code quality. You can run them via the Gradle wrapper:

* **Run Unit Tests:** `./gradlew test`
* **Test Coverage (JaCoCo):** `./gradlew jacocoTestReport` (generates HTML report in `build/jacoco/html`)
* **Run PMD:** `./gradlew pmdMain` (checks for common programming flaws)
* **Run CPD:** `./gradlew cpdCheck` (checks for code duplication)

## Authors

- [Alessandra Sasanelli](https://github.com/Alessandra-Sasanelli)
- [Simone Maccario](https://www.google.com/search?q=https://github.com/SimoneMaccario)
