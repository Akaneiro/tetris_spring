package com.company.tetris.model;

import java.awt.event.KeyEvent;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.company.tetris.Constants;
import com.company.tetris.GameState;

@Component
public class Model {

	@Autowired
	private ShapeFactory mShapeFactory;

	private int[][] mGameArea;
	private int mScoreValue;
	private Shape mCurrentFigure, mNextFigure;
	private GameState mCurrentState;
	private OnFinishEvent mFinishEvent;

	public int[][] getGameArea() {
		return mGameArea;
	}

	public int getScoreValue() {
		return mScoreValue;
	}

	public Shape getCurrentFigure() {
		return mCurrentFigure;
	}

	public Shape getNextFigure() {
		return mNextFigure;
	}

	public GameState getCurrentState() {
		return mCurrentState;
	}

	public Model() {
		mGameArea = new int[Constants.GameParameters.GAME_AREA_WIDTH][Constants.GameParameters.GAME_AREA_HEIGHT];
		mScoreValue = 0;
	}
	
	@PostConstruct
	private void init() {
		mCurrentFigure = createShape();
		mNextFigure = createShape();		
	}

	public void setFinishEvent(OnFinishEvent event) {
		mFinishEvent = event;
	}

	private boolean checkFigure() {
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				if (mCurrentFigure.y + y > -1 && mCurrentFigure.picture[x][y]) {
					if (x + mCurrentFigure.x < 0 || x + mCurrentFigure.x == Constants.GameParameters.GAME_AREA_WIDTH)
						return false;
					else if (mGameArea[x + mCurrentFigure.x][y + mCurrentFigure.y] > 0
							|| mCurrentFigure.y + y == Constants.GameParameters.GAME_AREA_HEIGHT - 1)
						return false;
				}
			}
		}
		return true;
	}

	public void onKeyPressed(KeyEvent e) {
		if (mCurrentFigure == null) {
			return;
		}
		switch (e.getKeyCode()) {
		case (KeyEvent.VK_RIGHT):
			mCurrentFigure.x += 1;
			if (!checkFigure()) {
				mCurrentFigure.x -= 1;
			}
			break;
		case (KeyEvent.VK_LEFT):
			mCurrentFigure.x -= 1;
			if (!checkFigure()) {
				mCurrentFigure.x += 1;
			}
			break;
		case (KeyEvent.VK_UP):
			mCurrentFigure.rotate();
			if (!checkFigure()) {
				mCurrentFigure.urotate();
			}
			break;
		case (KeyEvent.VK_DOWN):
			mCurrentFigure.y += 1;
			if (!checkFigure()) {
				mCurrentFigure.y -= 1;
			}
			break;
		}
	}

	private void stopFigure() {
		byte lineNumber = 0;
		int[] linesToBeDeleted = new int[4];
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				if (mCurrentFigure.y + y > -1 && mCurrentFigure.picture[x][y]) {
					mGameArea[mCurrentFigure.x + x][mCurrentFigure.y + y] = 1;
					linesToBeDeleted[lineNumber] = mCurrentFigure.y + y;
					boolean needDeleteLine = true;
					for (int i = 0; i < Constants.GameParameters.GAME_AREA_WIDTH; i++) {
						if (mGameArea[i][linesToBeDeleted[lineNumber]] == 0) {
							needDeleteLine = false;
							linesToBeDeleted[lineNumber] = 0;
						}
					}
					if (needDeleteLine) {
						lineNumber += 1;
						mScoreValue += 100;
					}
				}
			}
		}
		if (linesToBeDeleted[0] > 0) {
			deleteLine(linesToBeDeleted);
		}
	}

	private void deleteLine(int[] d) {
		for (int i = 0; i < 4; i++) {
			int y1 = d[i];
			if (d[i] == 0)
				break;
			for (int x = 0; x < Constants.GameParameters.GAME_AREA_WIDTH; x++) {
				for (int y = y1; y > 0; y--) {
					mGameArea[x][y] = mGameArea[x][y - 1];
				}
			}
		}
	}

	private void stateGame() {
		mCurrentState = GameState.Game;
	}

	private void stateOver() {
		mCurrentState = GameState.Over;
	}

	private void finishGame() {
		stopFigure();
		mCurrentFigure = null;
		mNextFigure = null;
		stateOver();
	}

	public void startGame() {
		mGameArea = new int[Constants.GameParameters.GAME_AREA_WIDTH][Constants.GameParameters.GAME_AREA_HEIGHT];
		mScoreValue = 0;
		mCurrentFigure = createShape();
		mNextFigure = createShape();
		stateGame();
	}

	public void timerTick() {
		if (mCurrentFigure == null) {
			mCurrentFigure = mNextFigure;
			mNextFigure = createShape();
			if (!checkFigure()) {
				mFinishEvent.onFinishEvent();
				finishGame();
			}
		} else {
			mCurrentFigure.y++;
			if (!checkFigure()) {
				mCurrentFigure.y--;
				stopFigure();
				mCurrentFigure = null;
			}
		}
	}

	private Shape createShape() {
		Shape shape = mShapeFactory.createRandomShape();
		return shape;
	}

	public interface OnFinishEvent {
		void onFinishEvent();
	}
}
