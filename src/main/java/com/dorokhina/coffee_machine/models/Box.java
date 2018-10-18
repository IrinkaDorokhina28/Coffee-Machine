package com.dorokhina.coffee_machine.models;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.*;

import static com.dorokhina.coffee_machine.models.Ingredient.CUP;

public class Box {

	private Ingredient cup = null;
	private List<Ingredient> ingredients = new ArrayList<>();
	private VBox vb = new VBox();
	private Label cupView = new Label("no cup");
	public Pane view;

	public Box(double scale) {
		view = createBox(scale);
	}

	public void setCup() {
		cup = CUP;
		cupView.setText("Cup");
	}
	
	public void add(Ingredient ingredient) {
		if (ingredient == CUP)
			setCup();
		else if (ingredients.size() < 3) {
			ingredients.add(ingredient);
			vb.getChildren().add(new Label(ingredient.name()));
		}
	}

	public Ingredient getCup() {
		return cup;
	}

	public List<Ingredient> getIngredients() {
		return ingredients;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Box)
			return ingredients.containsAll(((Box) o).getIngredients()) && cup == ((Box) o).getCup();
		else
			return false;
	}


	public void clean() {
		vb.getChildren().removeAll(vb.getChildren());
		cupView.setText("no cup");
		ingredients = new ArrayList<>();
		cup = null;
	}

	private Pane createBox(double scale) {
		int width = (int) (90 * scale);
		int height = (int) (60 * scale);
		vb.setAlignment(Pos.CENTER);
		vb.setMinSize(width, height);
		vb.setMaxSize(width, height);
		HBox hb = new HBox(cupView, vb);
		hb.setAlignment(Pos.CENTER);
		return hb;
	}
}
