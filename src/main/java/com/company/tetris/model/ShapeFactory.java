package com.company.tetris.model;

import java.util.Random;

import com.company.tetris.Constants;

public class ShapeFactory {

	private final Random mRandom = new Random();

	public Shape createRandomShape() {
		int figureIndex = mRandom.nextInt(7) + 1;
		Shape shape = new Shape(figureIndex);
		shape.x = (Constants.GameParameters.GAME_AREA_WIDTH / 2) - 2;
		return shape;
	}
}
