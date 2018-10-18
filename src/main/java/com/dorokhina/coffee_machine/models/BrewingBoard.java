package com.dorokhina.coffee_machine.models;

import java.util.Arrays;

public class BrewingBoard {
	private Cell[] cells = new Cell[6];
	private int selectedIndex = -1;

	public BrewingBoard() {
	    for (int i = 0; i < cells.length; i++)
            cells[i] = new Cell();
    }

    public void addIngredientTo(int boxIndex, Ingredient ingredient) {
    	cells[boxIndex].addIngredient(ingredient);
    }

    public void cleanCell(int index) {
        cells[index] = new Cell();
        if (selectedIndex == index)
            selectedIndex = -1;
    }

    public Cell[] getCells() {
        return cells;
    }

    public Cell getCell(int index) {
        return cells[index];
    }

    public void setSelectedIndex(int selectedIndex) {
	    if (this.selectedIndex != -1)
            cells[this.selectedIndex].setSelected(false);
        this.selectedIndex = selectedIndex;
        cells[selectedIndex].setSelected(true);
    }

    public boolean isAnySelected() {
        return selectedIndex != -1;
    }

    public void cleanSelected() {
        selectedIndex = -1;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void updateProgress(int index, double progress) {
	    cells[index].setProgress(progress);
    }

}
