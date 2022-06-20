package ru.utils.vectors;

public class IVector2 {
    public int x, y;

    public IVector2() {
    }

    public IVector2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public IVector2(IVector2 v) {
        this.x = v.x;
        this.y = v.y;
    }
}
