package org.lfc34;

import java.time.LocalDateTime;
import java.util.Scanner;

public class CliTaskManager {
	public static void printHelp() {
		System.out.println("""
                Commands:
                \t-h - print this screen
                \t-c "Task Name" "HH:mm dd/mm/yyyy" - create a task
                \t-d taskId - delete a task with given ID
                \t-l - list all tasks
                \t-m taskID - modify a task with given ID""");
	}
	
	public static void createTask(String name, String deadline) {
		Task task = new Task(name, LocalDateTime.parse(deadline, Task.deadLineFormat));
		if (TaskDB.addTask(task)) {
			System.out.println("Task \"" + name + "\" that should be done by: " + deadline
							 	+ " added.");
		} else {
			System.err.println("Something went wrong with adding task!");
		}
	}

	public static void deleteTask(int taskId) {
		if (TaskDB.delTask(taskId)) {
			System.out.println("Task №" + taskId + " successfully deleted.");
		} else {
			System.err.println("Something went wrong with task deletion!");
		}
	}

	// TODO: parse tasks here if TaskDB.listTasks() will return array of Task
	public static void listTasks() {
		String[] tasks = TaskDB.listTasks();
        assert tasks != null;
        for (String s : tasks) {
			if (s != null) {
				System.out.println(s);
			}
		}
	}

	public static void modifyTask(int taskId) {
		Task task = TaskDB.getTask(taskId);
		assert task != null;
		Scanner scan = new Scanner(System.in);
		System.out.println(task.getName() + " | " + task.getBeautifulDeadline() + " | " + task.getState());
		System.out.println("What do you want to modify here?");
		System.out.println("1 - name, 2 - deadline, 3 - state");
		int choice = scan.nextInt();
		if (choice == 1) {
			System.out.println("Enter new name for a task: ");
			scan.nextLine();
			String newName = scan.nextLine();
			task.setName(newName);
		} else if (choice == 2) {
			System.out.println("Enter new deadline in \"HH:mm dd/mm/yyyy\" format");
			scan.nextLine();
			String newDeadline = scan.nextLine();
			task.setDeadline(LocalDateTime.parse(newDeadline, Task.deadLineFormat));
		} else if (choice == 3) {
			System.out.println("Enter new state for a task: (DONE/UNDONE/IN_PROCESS/EXPIRED)");
			scan.nextLine();
			String newState = scan.nextLine();
			task.setState(Task.parseState(newState));
		}
		scan.close();
		if (TaskDB.modifyTask(taskId, task)) {
			System.out.println("Task modified successfully.");
		} else {
			System.err.println("Something went wrong when modifying task!");
		}
	}
}
