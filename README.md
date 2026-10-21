# Battleship in Java

A modular, full-featured desktop implementation of the classic Battleship naval combat game written in Java. Built with a strict Model-View separation and powered by the JTamaro graphics library, this application features an interactive graphical user interface, drag-and-drop fleet positioning, and multiple gameplay modes including an intelligent, state-driven AI opponent.

## Architecture and Design Patterns

The codebase is built on functional and object-oriented principles, emphasizing immutability and decoupling domain logic from UI rendering:

- **Model (`project.model`):** The domain logic layer. It encapsulates board state, coordinate transformations, ship placement validation, turn management, and AI search heuristics. All model records and classes are independent of the graphics engine and fully validated by unit tests.
- **UI (`project.ui`):** The presentation layer using `jtamaro`. It consumes immutable state snapshots from the model to render the visual elements, boards, targeting markers, and phase transitions.
- **Toolbelt (`project.Toolbelt`):** Utility functions providing immutable sequence transformations, list lookups, and geometry helpers.

### Design Patterns Used

1. **State Pattern (Phases):**
   The application state machine is modeled via the `Phase` interface (`project.model.phase.Phase`), cleanly decoupling game stages:
   - `Mode`: Main menu to choose between 2-Player local mode or playing against the Computer.
   - `ChooseDifficulty`: Difficulty selection screen for AI opponents (Easy vs. Hard).
   - `PlayerSetting`: Interactive preparation phase where players position and rotate their ships on the grid.
   - `ChooseWeapon`: Intermediary phase presenting weapons before target selection.
   - `ChoosePosition`: Active targeting phase where the player clicks on the opponent's grid to fire.
   - `AfterAttack`: Post-attack inspection phase showing hit/miss results and sunk ships before handing over the turn.
   - `RoundEnded`: Victory phase showing the winner and allowing a restart.

2. **Strategy Pattern (Players and AI):**
   The `Player` hierarchy allows uniform handling of human and algorithmic players:
   - `Human`: Governed by mouse and keyboard events.
   - `RandomComputer`: Easy difficulty AI selecting unattacked coordinates uniformly at random.
   - `LogicComputer`: Hard difficulty AI implementing a hunt-and-target search algorithm. Upon hitting a ship, it dynamically explores adjacent coordinates in cardinal directions (`Up`, `Down`, `Left`, `Right`) until the ship is destroyed.

3. **Visitor Pattern (Projectiles):**
   Projectiles (`HitProjectile`, `MissProjectile`) implement a visitor interface (`ProjectileVisitor`, `ProjectileRenderer`) to separate data structures from visual representations on the canvas.

## Gameplay and Fleet Specifications

The game takes place on a standard 10x10 coordinate grid. Each player deploys a fleet of four naval vessels before combat begins:

| Ship Type | Size (Tiles) | Theme Color | Placement Description |
| :--- | :--- | :--- | :--- |
| **Destroyer** | 2 | Green | Fast patrol boat |
| **Submarine** | 3 | Blue | Tactical sub-surface vessel |
| **Battleship** | 4 | Magenta | Heavy combat vessel |
| **Aircraft Carrier** | 5 | Orange | Flagship carrier |

## Controls and Keybindings

The game accepts mouse clicks and keyboard commands depending on the current phase:

### Global Commands (Active in any phase)
- `R`: Reset game back to the initial start screen.
- `Q`: Quit and close the application.

### Phase-Specific Controls

#### 1. Mode Selection (`Mode`)
- `U`: Select **User vs. User** (2-Player local pass-and-play).
- `C`: Select **User vs. Computer** (Single player against AI).

#### 2. Difficulty Selection (`ChooseDifficulty`)
- `E`: Select **Easy** mode (Random targeting AI).
- `H`: Select **Hard** mode (Logic-driven hunt-and-target AI).

#### 3. Fleet Deployment (`PlayerSetting`)
- **Mouse Drag & Drop**: Click and hold a ship to move it across the board, then release over grid cells.
- `S`: Rotate the currently selected ship (toggle horizontal / vertical).
- `N`: Confirm fleet disposition and advance to the next player (or start combat once all ships are placed).

#### 4. Weapon & Strike Selection (`ChooseWeapon`)
- `S`: Enter targeting mode to select attack coordinates (Human player).
- `Enter` / `Return`: Trigger computer turn execution (when playing against AI).

#### 5. Target Selection (`ChoosePosition`)
- **Mouse Click**: Click on an unattacked cell on the opponent's board to fire.

#### 6. Turn Handover (`AfterAttack`)
- `N`: Confirm attack outcome and pass turn to the other player.

## Project Structure

```text
battleship/
├── build.gradle                              # Gradle build configuration and plugins
├── settings.gradle                           # Gradle settings and local repositories
├── gradlew, gradlew.bat                      # Gradle wrapper executables
├── pmd.xml                                   # PMD rule definitions for static analysis
├── deps/                                     # Local Maven repository dependencies
│   ├── jtamaro/                              # JTamaro functional GUI library (v24.0.3)
│   └── cpd/                                  # Gradle CPD plugin artifacts
├── extra/                                    # Design diagrams and architecture sketches
│   ├── model.png                             # UML model diagram
│   ├── state_Pattern.png                     # Phase transition diagram
│   └── state_pattern_sketches.jpg            # State pattern notes
├── src/
│   ├── main/java/project/
│   │   ├── Main.java                         # Application entry point and IO interaction loop
│   │   ├── Toolbelt.java                     # Functional collection and sequence utilities
│   │   ├── configuration/
│   │   │   └── ConstantsUtils.java           # Grid layout, dimensions, and default entities
│   │   ├── model/
│   │   │   ├── Game.java                     # Main immutable game state container
│   │   │   ├── Position.java                 # Coordinate records and grid math
│   │   │   ├── boat/                         # Boat state and hittable position trackers
│   │   │   ├── phase/                        # State pattern phase implementations
│   │   │   ├── players/                      # Human, Computer, and direction heuristics
│   │   │   └── weapon/                       # Projectiles and weapon representations
│   │   └── ui/
│   │       ├── GameUI.java                   # Primary rendering coordinator
│   │       ├── board/Board.java              # Grid visualization and coordinate conversion
│   │       ├── boat/BoatGraphics.java        # Ship visual components and drag states
│   │       ├── cartesianWorldGraphics/       # Screen-to-cartesian coordinate adapters
│   │       ├── phase/PhaseUI.java            # UI renderers for each game phase
│   │       └── visitor/                      # Visitor pattern implementations for rendering
│   └── test/java/project/
│       └── model/                            # JUnit 4 test suites covering all model logic
└── .github/workflows/                        # Continuous integration workflows
    ├── lab.yml                               # Build, check, and test verification workflow
    └── expression-tutor.yml                  # Automated assignment grading checks
```

## Prerequisites

- **Java Development Kit (JDK):** Version 17 or later (Temurin / Adoptium recommended).
- **Gradle:** Not required separately; the repository bundles the `gradlew` wrapper (Gradle 8.5).

## How to Build and Run

### Run from Command Line

1. Clone or extract the repository:
   ```bash
   git clone git@github.com:simo-macca/battleship.git
   cd battleship
   ```

2. Launch the application via the Gradle wrapper:
   ```bash
   ./gradlew run
   ```
   On Windows PowerShell:
   ```powershell
   .\gradlew.bat run
   ```

### Run from an IDE

1. Open the project root folder in IntelliJ IDEA, VS Code, or Eclipse.
2. Allow Gradle to import dependencies from `deps/` and Maven Central.
3. Open `src/main/java/project/Main.java`.
4. Run the `main` method.

### Build Executable Distributions

To create standalone distribution archives:
```bash
./gradlew assemble
```
The generated binaries and scripts will be placed under `build/distributions/`.

## Quality Assurance and Testing

The project maintains high code quality through automated checks and unit testing:

- **Unit Tests:**
  ```bash
  ./gradlew test
  ```
  Executes the full suite of unit tests verifying grid constraints, player behaviors, weapon outcomes, and phase transitions.

- **Test Coverage (JaCoCo):**
  ```bash
  ./gradlew jacocoTestReport
  ```
  Generates an HTML coverage report located at `build/reports/jacoco/test/html/index.html`.

- **Static Analysis (PMD):**
  ```bash
  ./gradlew pmdMain pmdTest
  ```
  Runs PMD 7 with rules specified in `pmd.xml` to detect potential design, performance, or styling defects.

- **Copy-Paste Detection (CPD):**
  ```bash
  ./gradlew cpdCheck
  ```
  Scans Java source files to prevent duplicate code and ensure reusability.

## Authors

- [Alessandra Sasanelli](https://github.com/Alessandra-Sasanelli)
- [Simone Maccario](https://github.com/SimoneMaccario)
