package com.dorokhina.coffee_machine.models;

import java.util.List;
import java.util.ArrayList;

public class TaskBoard {
	List<Box> tasks = new ArrayList<Box>();
	int successful = 0;
	int failed = 0;
	
	public void addTask(Box box) {
		tasks.add(box);
	}
	
	public void removeTask(Box box) {
		tasks.remove(box);
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
}
