package com.company.tetris.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.company.tetris.Actions;
import com.company.tetris.GameState;
import com.company.tetris.model.Model;
import com.company.tetris.view.MainWindow;

public class Tetris implements Model.OnFinishEvent {

    private Model mGameModel;
    private MainWindow mWindow;

    public Tetris() {
        mGameModel = new Model();
        mGameModel.setFinishEvent(this);

        KeyListener keyListener = new KeyListener() {
			public void keyPressed(KeyEvent e) {
                onKeyPressed(e);				
			}

			public void keyReleased(KeyEvent e) {
				
			}

			public void keyTyped(KeyEvent e) {				
			}
        	
        };

        ActionListener windowButtonsListener = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals(Actions.NEW_GAME.name())) {
                  mGameModel.startGame();
              } else if (e.getActionCommand().equals(Actions.EXIT.name())) {
                  System.exit(0);
              }
			}
        };

        mWindow = new MainWindow();
        mWindow.init(keyListener, windowButtonsListener);
    }

    private void startGame() {
        mGameModel.startGame();

        while (true) {
            if (mGameModel.getCurrentState() == GameState.Game) {
                mGameModel.timerTick();
                drawWindow();
            }
            try {
                Thread.sleep(300); //the timing mechanism
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void onKeyPressed(KeyEvent e) {
        if (mGameModel.getCurrentState() == GameState.Game) {
            mGameModel.onKeyPressed(e);
        }
        drawWindow();
    }

    private void drawWindow() {
        mWindow.draw(mGameModel.getGameArea(),
                mGameModel.getScoreValue(),
                mGameModel.getCurrentFigure(),
                mGameModel.getNextFigure());
    }

    public static void main(String[] args) {
    	Tetris tetrix = new Tetris();
        tetrix.startGame();
    }

    public void onFinishEvent() {
        mWindow.onFinishGame();
    }
}
