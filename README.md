# Online Examination System

A desktop online examination system built with Java Swing and SQLite.

## Features

- Admin and student login
- Student registration
- Admin question management
- Timed exams with previous/next navigation
- Automatic submission when time expires
- Score and result tracking

## Requirements

- JDK 8 or newer
- SQLite JDBC driver 3.51.1.0 (or another compatible Xerial SQLite JDBC release)

The JDBC JAR is not committed to this repository. Download it from the [Xerial SQLite JDBC releases](https://github.com/xerial/sqlite-jdbc/releases) and place it in the project folder as `sqlite-jdbc.jar`.

## Build and run

From the project folder, compile the Java source files:

```sh
javac -cp sqlite-jdbc.jar *.java
```

Initialize the local SQLite database and its tables:

```sh
java -cp ".:sqlite-jdbc.jar" DatabaseSetup
```

Launch the application:

```sh
java -cp ".:sqlite-jdbc.jar" OnlineExamLauncher
```

On Windows, replace `:` in the classpath with `;`.

The application creates `exam.db` in its current working directory. It is local data and is intentionally ignored by Git.

## Demo admin account

`DatabaseSetup` creates the demo account `admin` / `admin123` for a new database. This is a sample credential for local evaluation only; change or remove it before using the application with real users. The application currently stores passwords as plain text, so it is not suitable for production use without adding secure password hashing and other security controls.

## Project files

- `OnlineExamLauncher.java` — application entry point
- `Login.java` — login and student registration
- `AdminDashboard.java` — administrator dashboard
- `StudentDashboard.java` — student dashboard
- `QuestionManager.java` — question management
- `ExamWindow.java` — timed exam interface
- `ResultWindow.java` — result display
- `DBConnection.java` — SQLite connection helper
- `DatabaseSetup.java` — database schema and demo admin initialization
