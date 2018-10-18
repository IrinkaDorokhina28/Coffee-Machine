package com.dorokhina.coffee_machine.models;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import static com.dorokhina.coffee_machine.models.CellState.*;

public class Cell {
    private int brewTime = 10000;
    private Box box;
    private double progress;
    private CellState state;
    public Task<Integer> task;
    public Node view;
    ProgressBar progressBar;

    public Cell() {
        box = new Box(3);
        progressBar = new ProgressBar(0.0);
        view = new VBox(box.view, progressBar);
        setState(IDLE);
    }

    public Box getBox() {
        return box;
    }

    public double getProgress() {
        return progress;
    }

    public void addIngredient(Ingredient ingredient) {
        box.add(ingredient);
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public CellState getState() {
        return state;
    }

    public void setState(CellState state) {
        this.state = state;
        switch (state) {
            case IDLE:
                box.clean();
                box.view.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
                setProgress(0.0);
                break;
            case SELECTED:
                box.view.setBackground(new Background(new BackgroundFill(Color.YELLOW, null, null)));
                break;
            case PROGRESSING:
                createTask();
                progressBar.progressProperty().bind(task.progressProperty());
                new Thread(task).start();
                box.view.setBackground(new Background(new BackgroundFill(Color.AQUA, null, null)));
                break;
            case READY_TO_COMPLETE:
                box.view.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, null, null)));
                break;
            case FAILED:
                box.view.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
                break;

        }
    }

    private void createTask() {
        task = new Task<Integer>(){
            @Override
            public Integer call() {
                int i = 0;
                for (i = 1; i < brewTime; i++) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {

                    }
                    if (isCancelled()) {
                        updateProgress(0, brewTime);
                        return i;
                    }
                    double value = ((double) i) / brewTime ;
                    progress = value;
                    updateProgress(i, brewTime);
                    if (value > 0.75 && state == CellState.PROGRESSING) {
                        updateProgress(i, brewTime);
                        setState(CellState.READY_TO_COMPLETE);
                    }
                }
                updateProgress(0, brewTime);
                setState(CellState.FAILED);
                return i;
            }
        };
    }
}
