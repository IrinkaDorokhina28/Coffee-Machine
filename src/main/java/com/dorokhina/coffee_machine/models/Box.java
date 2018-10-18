package com.dorokhina.coffee_machine.models;

import java.util.*;

import static com.dorokhina.coffee_machine.models.Ingredient.CUP;

public class Box {

	private Ingredient cup = null;
	private List<Ingredient> ingredients = new ArrayList<>();

	public void setCup() {
		cup = CUP;
	}
	
	public void add(Ingredient ingredient) {
		if (ingredient == CUP)
			setCup();
		else if (ingredients.size() < 3)
			ingredients.add(ingredient);
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
}
