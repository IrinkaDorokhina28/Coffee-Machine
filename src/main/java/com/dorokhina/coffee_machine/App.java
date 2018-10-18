package com.dorokhina.coffee_machine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.dorokhina.coffee_machine.models.*;
import javafx.application.Application;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.*;

import javafx.scene.layout.*;


/**
 * Created by Irina Dorokhina
 *
 */
public class App extends Application {
	Stage primaryStage;

	TaskBoard taskBoard;
	BrewingBoard brewingBoard;
	ProgressBar gameProgress;
	Label gameScore;

	long taskInterval = 1000;
	long timeLimit = 60000;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;

		taskBoard = new TaskBoard();
		brewingBoard = new BrewingBoard();
        Task<Integer> task = createTask();
		gameProgress = new ProgressBar();
		gameProgress.setMinSize(600, 40);
        gameProgress.progressProperty().bind(task.progressProperty());
		gameScore = new Label("Success: " + taskBoard.countSuccessful() + " Failed: " + taskBoard.countFailed());
		gameScore.setMinSize(100, 40);

		Scene scene = createScene();
        primaryStage.setScene(scene);
        primaryStage.show();
        new Thread(task).start();
	}

    public Task<Integer> createTask() {
        return new Task<Integer>(){
            @Override
            public Integer call() {
                int i = 0;
                for (i = 1; i < timeLimit; i++) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    updateProgress(i, timeLimit);
                    if (isCancelled()) {
                        break;
                    }
                    if (i % taskInterval == 0 && taskBoard.count() < 10)
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                taskBoard.addTask(generateTask());
                            }
                        });
                }
                return i;
            }
        };
    }
	
	public Scene createScene() {
        FlowPane root = new FlowPane(Orientation.VERTICAL);
        root.setVgap(10);

        //root.setAlignment(Pos.CENTER);
        root.getChildren().add(new HBox(gameScore, gameProgress));
        root.getChildren().add(taskBoard.view);
        root.getChildren().add(createCells(brewingBoard));
        root.getChildren().add(createIngredientBoard());
//		root.add(hb2, 1, 0);
        return new Scene(root, 1200, 600);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
	}

	
	public Node createCells(BrewingBoard brewingBoard) {
		GridPane gridPane = new GridPane();
		gridPane.setVgap(10);
		gridPane.setHgap(10);
		for ( int i = 0; i < 6; i++) {
		    Cell cell = brewingBoard.getCell(i);
            gridPane.add(initCellOnClickListener(cell, i), i % 3, i / 3);
        }
		return gridPane;
	}

	public Node initCellOnClickListener(Cell cell, int index) {
        cell.getBox().view.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                switch (cell.getState()) {
                    case IDLE:
                        brewingBoard.setSelectedIndex(index);
                        cell.setState(CellState.SELECTED);
                        break;
                    case SELECTED:
                        brewingBoard.cleanSelected();
                        cell.setState(CellState.PROGRESSING);
                        break;
                    case PROGRESSING:
                        break;
                    case READY_TO_COMPLETE:
                        cell.task.cancel();
                        taskBoard.removeTask(cell.getBox());
                        brewingBoard.cleanSelected();
                        cell.setState(CellState.IDLE);
                        gameScore.setText("Success: " + taskBoard.countSuccessful() + " Failed: " + taskBoard.countFailed());
                        break;
                    case FAILED:
                        cell.task.cancel();
                        brewingBoard.cleanSelected();
                        cell.setState(CellState.IDLE);
                        taskBoard.failed();
                        gameScore.setText("Success: " + taskBoard.countSuccessful() + " Failed: " + taskBoard.countFailed());
                        break;

                }
            }
        });
        return cell.view;
	}

	public Node createIngredientBoard() {

        List<Node> tasks = new ArrayList<Node>();
        for (Ingredient ingredient: Ingredient.values()) {
//            switch (ingredient) {
//                case CUP :
//                    HBox hb = new HBox(new Label(ingredient.name()));
//                    break;
//                case
//            }
            HBox hb = new HBox(new Label(ingredient.name()));
            hb.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (brewingBoard.isAnySelected()) {
                        brewingBoard.addIngredientTo(brewingBoard.getSelectedIndex(), ingredient);
//                        render(createScene(taskBoard, brewingBoard));
                    }
                }
            });
            tasks.add(hb);
        }

        TilePane ingredientSet = new TilePane(Orientation.HORIZONTAL);
        ingredientSet.setPrefColumns(Ingredient.values().length);
        ingredientSet.setVgap(10);
        ingredientSet.setHgap(10);
        ingredientSet.getChildren().addAll(tasks);
        return ingredientSet;
    }

    public Box generateTask() {
        Random random = new Random();
        Ingredient[] ingredients = new Ingredient[]{Ingredient.COFFEE, Ingredient.MILK, Ingredient.SUGAR};
        Box b = new Box(1);
        b.setCup();
        int count = random.nextInt(3);
        for (int j = 0; j < count + 1; j++) {
            b.add(ingredients[random.nextInt(3)]);
        }
        return b;
    }
	
	public static void main(String[] args) {
		launch(args);
	}
}
