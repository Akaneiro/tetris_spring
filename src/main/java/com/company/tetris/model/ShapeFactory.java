package com.company.tetris.model;

import java.util.Random;

public class ShapeFactory {

	private final Random mRandom = new Random();

	public Shape createRandomShape() {
		int figureIndex = mRandom.nextInt(7) + 1;
		return new Shape(figureIndex);
	}
}
