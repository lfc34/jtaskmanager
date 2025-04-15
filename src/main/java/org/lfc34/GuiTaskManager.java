package org.lfc34;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;

import javax.swing.*;
import javax.swing.border.Border;

import org.apache.commons.io.comparator.LastModifiedFileComparator;

public class GuiTaskManager {
	private static final Dimension minWinSize = new Dimension(600, 600);
	private static final Dimension maxWinSize = Toolkit.getDefaultToolkit().getScreenSize();
	private static final Dimension addFrameSize = new Dimension(500, 100);
	private static int selectedTask = -1;

	// TODO: implement all elements here as static, and configure them via methods

	private static String[] fetchTasksList() {
		return TaskDB.listTasks();
	}

	// TODO: REFACTOR THIS SHIT!!!!
	public static void init() {
		// WINDOW SETUP
		JFrame w = new JFrame("jTaskManager");
		w.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		w.setSize(minWinSize);
		w.setMinimumSize(minWinSize);
		w.setMaximumSize(maxWinSize);
		w.setLayout(new BorderLayout());

		// BUTTONS CREATION AND SETUP
		JButton addButton = new JButton("Add task");
		addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		JButton delButton = new JButton("Delete task");
		delButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		JButton modButton = new JButton("Modify task");
		modButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		JPanel buttonPanel = new JPanel();
		BoxLayout btnPanelLayout = new BoxLayout(buttonPanel, BoxLayout.Y_AXIS);
		buttonPanel.setLayout(btnPanelLayout);
		buttonPanel.setAlignmentY(Panel.CENTER_ALIGNMENT);
		buttonPanel.add(addButton);
		buttonPanel.add(delButton);
		buttonPanel.add(modButton);
		
		// TASK LIST CREATION AND SETUP
		String[] tasks = fetchTasksList();
		DefaultListModel<String> listModel = new DefaultListModel<>();
		// JList<String> tasksList = new JList<>(fetchTasksList());
		JList<String> tasksList = new JList<>(listModel);

		// update list
		listModel.clear();
		for (String t : tasks) {
			listModel.addElement(t);
		}

		JScrollPane tasksPanel = new JScrollPane(tasksList);
		tasksPanel.setViewportView(tasksList);
		tasksPanel.setHorizontalScrollBar(tasksPanel.createHorizontalScrollBar());
		tasksPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		tasksPanel.setVerticalScrollBar(tasksPanel.createVerticalScrollBar());
		tasksPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		// Set up button functionality
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Add button clicked");
				
				// Create new window 
				JFrame addFrame = new JFrame("Add task");
				addFrame.setLayout(new BorderLayout());
				addFrame.setSize(addFrameSize);	
				addFrame.setLocationRelativeTo(null);
				addFrame.setVisible(true);
				
				// Fields
				JTextField taskNameField = new JTextField("enter task name", 24);
				JTextField deadlineField = new JTextField("13:30 01/01/2025", 24);

				// buttons
				JButton confirm = new JButton("Add task");
				JButton cancel = new JButton("Cancel");

				// Panels which contain all of the stuff above 
				JPanel fieldPanel = new JPanel();
				fieldPanel.add(taskNameField);
				fieldPanel.add(deadlineField);
				JPanel btnPanel = new JPanel();
				btnPanel.add(confirm);
				btnPanel.add(cancel);

				// showtime
				addFrame.add(fieldPanel);
				addFrame.add(btnPanel, BorderLayout.EAST);

				// Button functionality
				confirm.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO: add date validation here!
						Task task = new Task(taskNameField.getText(), LocalDateTime.parse(deadlineField.getText(), Task.deadLineFormat));
						TaskDB.addTask(task);
						String[] tasks = fetchTasksList();
						listModel.clear();
						for (String t : tasks) {
							listModel.addElement(t);
						}
						tasksList.revalidate();
						tasksList.repaint();
						addFrame.setVisible(false);
						addFrame.dispose();
					}
				});
				cancel.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						addFrame.setVisible(false);
						addFrame.dispose();
					}
				});
				// addFrame.dispatchEvent(new WindowEvent(addFrame, WindowEvent.WINDOW_CLOSING));
				// if (confirm.getModel().isPressed() || cancel.getModel().isPressed()) {
				// 	addFrame.setVisible(false);
				// 	addFrame.dispose();
				// }
			}
		});		

		
		modButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Mod button clicked");
			}
		});
		
		
		// del button here because it needs tasksList
		delButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Delete button clicked");
				selectedTask = tasksList.getSelectedIndex() + 1;
				
				// window
				JDialog confirm = new JDialog(w);
				confirm.setLayout(new BorderLayout());
				confirm.setSize(300, 150);
				confirm.setLocationRelativeTo(null);
				confirm.setTitle("Confirm action");
				confirm.setVisible(true);

				JLabel label = new JLabel("Are you sure you want to delete this task?");

				// buttons
				JButton yBtn = new JButton("Yes");
				JButton nBtn = new JButton("No");
				// TODO: complete it
				confirm.add(label, BorderLayout.PAGE_START);
				confirm.add(yBtn, BorderLayout.WEST);
				confirm.add(nBtn, BorderLayout.EAST);
				confirm.pack();
			}
		});

  		// FINALLY ADD EVERYTHING TO OUR WINDOW
		w.add(tasksPanel);
		w.add(buttonPanel, BorderLayout.EAST);

		// DONT TOUCH
		// w.pack();
		w.setVisible(true);
	}
}
