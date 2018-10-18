package com.dorokhina.coffee_machine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.dorokhina.coffee_machine.models.*;
import javafx.application.Application;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.*;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;


/**
 * Developed by Irina Dorokhina
 *
 */
public class App extends Application {
	Stage primaryStage;

	volatile TaskBoard taskBoard;
	volatile BrewingBoard brewingBoard;

	long brewTime = 10000;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;

		taskBoard = new TaskBoard();

        Random random = new Random();

        Ingredient[] ingredients = new Ingredient[]{Ingredient.COFFEE, Ingredient.MILK, Ingredient.SUGAR};
		for (int i = 0; i < 9; i++) {
            Box b = new Box();
            b.setCup();
		    int count = random.nextInt(3);
		    for (int j = 0; j < count + 1; j++) {
		       // Ingredient ingredient = ingredients[random.nextInt(3)];
		        b.add(ingredients[random.nextInt(3)]);
            }
            taskBoard.addTask(b);
        }

		brewingBoard = new BrewingBoard();

		Scene scene = createScene(taskBoard, brewingBoard);
		render(scene);
	}

	synchronized void render(Scene scene) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                primaryStage.setScene(scene);
                primaryStage.show();
            }
        });
    }
	
	public Scene createScene(TaskBoard taskBoard, BrewingBoard brewingBoard) {
			GridPane root = new GridPane();
			
			//root.setAlignment(Pos.CENTER);
			root.add(createTasks(taskBoard), 0, 0);
			root.add(createCells(brewingBoard), 0, 1);
			root.add(createIngridientBoard(), 0, 2);
//			root.add(hb2, 1, 0);
		
			return new Scene(root, 1200, 600);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
	}
	
	public Node createTasks(TaskBoard taskBoard) {
		int taskCount = taskBoard.getTasks().size();
		List<Node> tasks = new ArrayList<>();
		for (Box box: taskBoard.getTasks())
		    tasks.add(createBox(box, 1));
//		List<Node> tasks = taskBoard.getTasks().stream().map(box -> createBox(box, 1)).collect(Collectors.toList());
		
		TilePane taskContainer = new TilePane(Orientation.HORIZONTAL);
		taskContainer.setPrefColumns(taskCount);
		taskContainer.setVgap(10);
		taskContainer.setHgap(10);
		taskContainer.getChildren().addAll(tasks);
		return taskContainer;
	}
	
	public Node createCells(BrewingBoard brewingBoard) {
		int cellsCount = 6;
		GridPane gridPane = new GridPane();
		gridPane.setVgap(10);
		gridPane.setHgap(10);
		for ( int i = 0; i < 6; i++) {
		    Cell cell = brewingBoard.getCell(i);
            gridPane.add(createCell(cell, i), i % 3, i / 3);
        }
		return gridPane;
	}
	
	public Node createCell(Cell cell, int index) {
        Pane box = createBox(cell.getBox(), 3);
        ProgressBar pb = new ProgressBar(cell.getProgress());
        if (cell.isFailed()) {
            box.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
            box.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    brewingBoard.cleanCell(index);
                    render(createScene(taskBoard, brewingBoard));
                }
            });
            return new VBox(box, pb);
        } else if (cell.getProgress() > 0.75) {
			box.setBackground((new Background(new BackgroundFill(Color.GREEN, null, null))));
			box.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    taskBoard.removeTask(cell.getBox());
                    brewingBoard.cleanCell(index);
                    render(createScene(taskBoard, brewingBoard));
                }
            });
			return new VBox(box, pb);
		} else {
			if(cell.isSelected()) {
                box.setBackground(new Background(new BackgroundFill(Color.YELLOW, null, null)));
                box.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        Runnable task = new Runnable(){
                            @Override
                            public void run() {
                                long i = 0;
                                boolean first = true;
                                for (i = 1; i < brewTime; i++) {
                                    try {
                                        Thread.sleep(1);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    double value = ((double) i) / brewTime ;
                                    cell.setProgress(value);
                                    pb.setProgress(value);
                                    System.out.println("value " + value);
                                    if (value > 0.75 && first) {
                                        System.out.println("completion");
                                        first = false;
                                        render(createScene(taskBoard, brewingBoard));
                                    }

                                }
                                cell.setProgress(1.0);
                                render(createScene(taskBoard, brewingBoard));
                            }
                        };
                        cell.setProgress(0.001);
                        new Thread(task).start();
                    }
                });
            } else {
                box.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
                box.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        brewingBoard.setSelectedIndex(index);
                        render(createScene(taskBoard, brewingBoard));
                    }
                });
            }
			return new VBox(box, pb);
		}
	}

	public Pane createBox(Box box, double scale) {
		int width = (int) (90 * scale);
		int height = (int) (60 * scale);

//		List<Node> list = box.getIngredients().stream().map(i -> new Label(i.name())).collect(Collectors.toList());
        VBox vb = new VBox();
        List<Node> list = box.getIngredients().stream().map(i -> new Label(i.name())).collect(Collectors.toList());
        vb.getChildren().addAll(list);
		vb.setAlignment(Pos.CENTER);
		vb.setMinSize(width, height);
		vb.setMaxSize(width, height);
		HBox hb = new HBox(new Label(box.getCup() == null ? "No cup" : "Cup"), vb);
		hb.setAlignment(Pos.CENTER);
		return hb;
	}

	public Node createIngridientBoard() {

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
                        render(createScene(taskBoard, brewingBoard));
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
	
	public static void main(String[] args) {
		launch(args);
	}
}
