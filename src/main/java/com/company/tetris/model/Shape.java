package com.company.tetris.model;

public class Shape {
    public boolean[][] picture;
    public int x, y;

    public Shape() {
        x = 3;
        y = -2;
    }

    public Shape(int f) {
        switch (f) {
            case 1:
                picture = new boolean[][]{
                        {false, true, false, false},
                        {false, true, false, false},
                        {false, true, false, false},
                        {false, true, false, false}}; //палка
                break;
            case 2:
                picture = new boolean[][]{
                        {false, true, false, false},
                        {true, true, false, false},
                        {false, true, false, false},
                        {false, false, false, false}}; // с торчком посередке
                break;
            case 3:
                picture = new boolean[][]{
                        {false, false, false, false},
                        {true, false, false, false},
                        {true, true, true, false},
                        {false, false, false, false}}; // носатый
                break;
            case 4:
                picture = new boolean[][]{
                        {false, false, false, false},
                        {true, true, true, false},
                        {true, false, false, false},
                        {false, false, false, false}}; // носатый наоборот
                break;
            case 5:
                picture = new boolean[][]{
                        {false, false, false, false},
                        {false, true, true, false},
                        {false, true, true, false},
                        {false, false, false, false}}; // просто квадрат
                break;
            case 6:
                picture = new boolean[][]{
                        {false, false, false, false},
                        {true, true, false, false},
                        {false, true, true, false},
                        {false, false, false, false}}; // зигзагообразный
                break;
            case 7:
                picture = new boolean[][]{
                        {false, false, false, false},
                        {false, true, true, false},
                        {true, true, false, false},
                        {false, false, false, false}}; // зигзагообразный наоборот
                break;
        }
    }

    //поворот фигуры
    public void rotate() {
        boolean[][] p2 = new boolean[4][4];
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++)
                p2[3 - y][x] = picture[x][y];
        }
        picture = p2;
    }

    //поворот фигуры в другую сторону
    public void urotate() {
        boolean[][] p2 = new boolean[4][4];
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                p2[x][y] = picture[3 - y][x];
            }
        }
        picture = p2;
    }
}
