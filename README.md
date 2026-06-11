# Project the_battleshipper

ONE-PARAGRAPH DESCRIPTION OF YOUR GAME

## Features

- *Essential*: Battleship with board setup (ship positioning). Human vs human or human vs computer (multiple strategies).
- *Extensions*: Save; Undo/Redo; different kinds of weapons; …
- *Example*: Play at https://papergames.io/en/battleship or https://www.battleshiponline.org/ for an example.


## Team Members

* Simone Maccario (simone.maccario@usi.ch), simo-macca
* Alessandra Sasanelli (alessandra.sasanelli@usi.ch), Alessandra-Sasanelli

## Mentor

* Igor Moreno

## Project Structure

This project is a Gradle project.

You can use Gradle to compile, test, and check the code
by running `gradlew` in this top-level directory (see below).

You can run the code compiled by Gradle from the terminal (see below).

The code is inside the [src/main/java/project](src/main/java/project) folder
and the test code is in [src/test/java/project](src/test/java/project).

The classes inside the `model` package contain the entities and _business logic_ of your project.
The classes in the `model` package need to come with unit tests.

The classes in the `ui` package must not refer to any classes in the `model` package
(these classes do not need to be covered with tests).

## How build using Gradle

Use the `assemble` task (either in VSCode `Gradle Project/Tasks/build/assemble`) or by running `./gradlew assemble`.

### How run the JUnit tests

Use the Test View in VSCode to run the tests or run the following command in the terminal `./gradlew test`.

### How to run PMD

[PMD](https://pmd.github.io/) checks for common programming flaws in your code and provides suggestions to improve it.

Use the `pmdMain` task (either in VSCode `Gradle Project/Tasks/other/pmdMain`) or by running `./gradlew pmdMain`.

### How to run CPD

[CPD](https://pmd.github.io/) checks for code duplication and highlights lines of code that you should refactor to improve the quality of your code.

Use the `cpdCheck` task (either in VSCode `Gradle Project/Tasks/other/cpdCheck`) or by running `./gradlew cpdCheck`.

### How to determine test coverage

In the test view of VSCode, run the tests using the ["Run tests with coverage"](https://code.visualstudio.com/updates/v1_88#_test-coverage) icon.

