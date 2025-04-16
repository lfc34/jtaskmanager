# jtaskmanager
Simple task manager written in Java (java 17). ***This program is fully functional, but still very buggy.***

### Why?
Because I need a conveniet way to manage my tasks.
### How?
The program creates a local relational database (SQLite) and user can add, remove and modify tasks.

# Building a program from source
You need [Apache Maven](https://maven.apache.org/) and [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) to build this program from source.
### 1. Clone the repo
```bash
git clone https://github.com/lfc34/jtaskmanager.git
cd jtaskmanager
```
### 2. Build it with maven into single executable .jar
```bash
mvn clean compile assembly:single
```
### 3. Run it!
You now have TaskManager-alpha.jar under target/ directory.
Run it like this:
```bash
java -jar TaskManager-beta.jar
```

# Usage

Launch program from executable jar, by double click or 
```bash
java -jar TaskManager-beta.jar
```
to launch the GUI, or type
```bash
java -jar TaskManager-beta.jar -h
```
to see how to work with this program in CLI mode.

### Create a task  (CLI mode)
Use **-c** key and two string arguments, representing the task name and deadline in "HH:mm dd/mm/yyyy" format, like:
```bash
java -jar TaskManager-beta.jar -c "My task" "23:59 12/12/2012"
```
#### **Remember that putting task name and deadline in "double" or 'singular' quotes is IMPORTANT!**

### List all tasks (CLI mode)
Use **-l** key.
```bash
java -jar TaskManager-beta.jar -l
```

### Remove a task (CLI mode)
Use **-d** key with single integer argument that represents the task ID, like:
```bash
java -jar TaskManager-beta.jar -d 1
```

### Modify a task (e.g. mark task as 'DONE')
Use **-m** key with single integer argument that represents the task ID, like:
```bash
java -jar TaskManager-beta.jar -m 2
```
An then, CLI will ask you how you want to modify a task (e.g. set new status, name, or deadline)
#### **Remember that putting task name and deadline in "double" or 'singular' quotes is IMPORTANT!**
