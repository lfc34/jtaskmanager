package org.lfc34;

import java.nio.file.Paths;
import java.nio.file.Path;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/* Current functionality:
*  1. Create DB and a table inside it
*  2. Add task
*  3. List tasks
*  4. Remove task
*  5. Modify tasks
*  
*  NOTE:
*  Methods of this class is currently all static, but it would be nice to have
*  multiple databases.
*/
public class TaskDB {
    private static final String userHome = System.getProperty("user.home");
    private static String dbPath = userHome;
    private static final String urlPathUnix = "jdbc:sqlite:" + dbPath + "/tasks.db";
    private static final Path urlPathCrossplatform = Paths.get(urlPathUnix);

    public static void setDbPath(String newPath) {
        dbPath = newPath;
    }

    public static void init() {
        try {
            createNewDb();
            createTable();
            reIndexDb();
        } catch (SQLException sqlE) {
            System.err.println("Error " + sqlE.getErrorCode());
            System.err.println(sqlE.getMessage());
            System.exit(1);
        }
    }

    private static void createNewDb() throws SQLException {
        Connection connection = DriverManager.getConnection(urlPathCrossplatform.toString());
        connection.close();
    }

    private static void createTable() throws SQLException {
        final String sqlStatement = "CREATE TABLE IF NOT EXISTS tasks ("
                    +"  id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    +"  task_name TEXT NOT NULL,"
                    +"  deadline TEXT NOT NULL,"
                    +"  state TEXT NOT NULL"
                    +");";
        Connection connection = DriverManager.getConnection(urlPathCrossplatform.toString());
        Statement statement = connection.createStatement();
        statement.execute(sqlStatement);
        statement.close();
        connection.close();
    }

    public static boolean addTask(Task task) {
        final String sqlStatement = "INSERT INTO tasks(task_name, deadline, state)"
                                  + "VALUES(?,?,?);";
        try {
            Connection conn = DriverManager.getConnection(urlPathCrossplatform.toString());
            PreparedStatement pSqlStatement = conn.prepareStatement(sqlStatement);
            pSqlStatement.setString(1, task.getName());
            pSqlStatement.setString(2, task.getBeautifulDeadline());
            pSqlStatement.setString(3, task.getState().toString());
            pSqlStatement.executeUpdate();
            pSqlStatement.close();
            conn.close();
            return true;

        } catch (SQLException e) {
            System.err.println("Error " + e.getErrorCode());
            System.err.println(e.getMessage());
            return false;
        }
    }

    // TODO: make it return array of tasks. Front-end will parse it as needed
    public static String[] listTasks() {
        reIndexDb();
        final String sqlStatement = "SELECT * FROM TASKS;";
        try {
            Connection conn = DriverManager.getConnection(urlPathCrossplatform.toString());
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sqlStatement);
            List<String> tasksList = new ArrayList<>();
            while (rs.next()) {
                tasksList.add(rs.getInt("id") + " | "
                        + rs.getString("task_name") + " | "
                        + rs.getString("deadline") + " | "
                        + rs.getString("state"));
            }
            String[] tasks = tasksList.toArray(new String[0]);
            return tasks;
        } catch (SQLException e) {
            System.err.println("Error " + e.getErrorCode());
            System.err.println(e.getMessage());
        }
        return null;
    }

    public static boolean delTask(int taskId) {
        final String sqlStatement = "DELETE FROM tasks WHERE id = ?;";
        try {
            Connection conn = DriverManager.getConnection(urlPathCrossplatform.toString());
            PreparedStatement pSqlStatement = conn.prepareStatement(sqlStatement);
            pSqlStatement.setInt(1, taskId);
            pSqlStatement.executeUpdate();
            pSqlStatement.close();
            conn.close();
            return true;
        } catch (SQLException e) {
            System.err.println("Error " + e.getErrorCode());
            System.err.println(e.getMessage());
            return false;
        }
    }
    
    /* 1. Remove old task by id
     * 2. Replace it with new task
     * 3. Let the front-end do the job of creating new task based on previous */
    public static boolean modifyTask(int taskId, Task task) {
    	return (delTask(taskId) && addTask(task));
    }
    
    /* Load task from DB to RAM. User then can modify the task, in runtime,
     * and then store it back in DB using modify()*/
    public static Task getTask(int taskId) {
    	Task loadedTask = new Task();
    	try {
    		Connection conn = DriverManager.getConnection(urlPathCrossplatform.toString());
    		PreparedStatement selStatement = conn.prepareStatement("SELECT * FROM tasks WHERE id = ?");
    		selStatement.setInt(1, taskId);
    		ResultSet rs = selStatement.executeQuery();
    		loadedTask.setName(rs.getString("task_name"));
    		loadedTask.setDeadline(LocalDateTime.parse(rs.getString("deadline"), Task.deadLineFormat));
    		loadedTask.setState(Task.parseState(rs.getString("state")));
            conn.close();
			return loadedTask;
    	} catch (SQLException e) {
    		System.err.println("SQL error: " + e.getErrorCode());
    		System.err.println(e.getMessage());
    	}
    	return null;
    }

    private static void reIndexDb() {
        // assign new ID's to tasks
        try {
            Connection conn = DriverManager.getConnection(urlPathCrossplatform.toString());
            PreparedStatement selStatement = conn.prepareStatement("SELECT id FROM tasks ORDER BY id");
            ResultSet rsSet = selStatement.executeQuery();
            int newIndex = 1;
            while (rsSet.next()) {
                int currentId = rsSet.getInt("id");
                PreparedStatement updStatement = conn.prepareStatement("UPDATE tasks SET id = ? WHERE id = ?");
                updStatement.setInt(1, newIndex);
                updStatement.setInt(2, currentId);
                updStatement.executeUpdate();
                newIndex++;
            }
        } catch (SQLException e) {
            System.err.println("Error " + e.getErrorCode());
            System.err.println(e.getMessage());
            return;
        }
    }
}
