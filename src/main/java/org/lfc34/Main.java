package org.lfc34;

public class Main {
    public static void main(String[] args) {
        TaskDB.init();
        // launch GUI by default
        if (args.length == 0 || args[0].equals("-gui")) {
        	GuiTaskManager.init();
        	return;
        } 
        switch (args.length) {
            // PRINT HELP - WORKS
            // LIST TASKS - WORKS
		case 1:
        	if (args[0].equalsIgnoreCase("-h")) {
        		CliTaskManager.printHelp();
        	} else if (args[0].equalsIgnoreCase("-l")) {
        		CliTaskManager.listTasks();
        	} else {
        		System.err.println("Invalid arguments. Try -h");
        	}
        	break;
		// TASK DELETION - WORKS
		// TASK MODIFY - BUGGY AS HELL
        case 2:
        	if (args[0].equalsIgnoreCase("-d")) {
        		CliTaskManager.deleteTask(Integer.parseInt(args[1]));
        	} else if (args[0].equalsIgnoreCase("-m")) {
				CliTaskManager.modifyTask(Integer.parseInt(args[1]));
			} else {
				System.err.println("Invalid arguments. Try -h");
			}
        	break;

		// TASK CREATION - WORKS
        case 3:
			if (args[0].equalsIgnoreCase("-c")) {
				CliTaskManager.createTask(args[1], args[2]);
			} else {
				System.err.println("Invalid arguments. Try -h");
			}
        	break;
        }
   }
}