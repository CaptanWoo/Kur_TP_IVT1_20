package ru.utils.vectors;

public class DVector2 {

    public double x, y;

    public DVector2() {
    }

    public DVector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public DVector2(DVector2 v) {
        this.x = v.x;
        this.y = v.y;
    }
}
