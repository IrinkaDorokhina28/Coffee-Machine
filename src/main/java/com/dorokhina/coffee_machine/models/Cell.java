package com.dorokhina.coffee_machine.models;

public class Cell {
    private Box box;
    private double progress;
    private boolean isSelected = false;

    public Cell() {
        box = new Box();
    }

    public Box getBox() {
        return box;
    }

    public void setBox(Box box) {
        this.box = box;
    }

    public double getProgress() {
        return progress;
    }

    public void setEmpty() {
        box = new Box();
    }

    public void addIngredient(Ingredient ingredient) {
        box.add(ingredient);
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isFailed() {
        return progress > 0.999;
    }

    public boolean isReadyToComplete() {
        return progress > 0.75;
    }
}
