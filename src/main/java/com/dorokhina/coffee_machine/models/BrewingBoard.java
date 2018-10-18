package com.dorokhina.coffee_machine.models;

public class BrewingBoard {
	private Cell[] cells = new Cell[6];
	private int selectedIndex = -1;

	public BrewingBoard() {
	    for (int i = 0; i < cells.length; i++)
            cells[i] = new Cell();
    }

    public void addIngredientTo(int index, Ingredient ingredient) {
        cells[index].addIngredient(ingredient);
    }

    public Cell[] getCells() {
        return cells;
    }

    public Cell getCell(int index) {
        return cells[index];
    }

    public void setSelectedIndex(int selectedIndex) {
	    if (this.selectedIndex != -1 && cells[this.selectedIndex].getState() == CellState.SELECTED)
	        cells[this.selectedIndex].setState(CellState.IDLE);
        this.selectedIndex = selectedIndex;
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


}
