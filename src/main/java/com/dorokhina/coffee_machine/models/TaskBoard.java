package com.dorokhina.coffee_machine.models;

import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.layout.TilePane;

import java.util.List;
import java.util.ArrayList;

public class TaskBoard extends Thread {
	List<Box> tasks = new ArrayList<Box>();
	public TilePane view;
	int successful = 0;
	int failed = 0;

	public TaskBoard() {
		view = createTasks();
	}

	@Override
	public void run() {

	}
	
	public void addTask(Box box) {
		tasks.add(box);
		view.getChildren().add(box.view);
	}
	
	public void removeTask(Box box) {
		for (Box b : tasks) {
			if (box.equals(b)) {
				view.getChildren().remove(b.view);
				tasks.remove(box);
				successful++;
				return;
			}
		}
		failed++;
	}

	public void failed() {
		failed++;
	}

	public int count() {
		return tasks.size();
	}

	public List<Box> getTasks() {
		return tasks;
	}
	
	public int countSuccessful() { 
		return successful;
	}
	
	public int countFailed() { 
		return failed;
	}

	public TilePane createTasks() {
		TilePane taskContainer = new TilePane(Orientation.HORIZONTAL);
		taskContainer.setPrefColumns(10);
		taskContainer.setVgap(10);
		taskContainer.setHgap(10);
		taskContainer.setMinSize(1100, 60);
		return taskContainer;
	}
}
