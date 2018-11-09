package com.company.tetris.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.company.tetris.Actions;
import com.company.tetris.GameState;
import com.company.tetris.TetrisConfig;
import com.company.tetris.model.Model;
import com.company.tetris.view.MainWindow;

@Component
public class Game implements Model.OnFinishEvent {

	@Autowired
	private Model mGameModel;
	@Autowired
	private MainWindow mWindow;

	public KeyListener keyListener() {
		return new KeyListener() {
			public void keyPressed(KeyEvent e) {
				onKeyPressed(e);
			}

			public void keyReleased(KeyEvent e) {

			}

			public void keyTyped(KeyEvent e) {
			}
		};
	}
	
	@PostConstruct
	private void init() {
		mGameModel.setFinishEvent(this);
		ActionListener windowButtonsListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals(Actions.NEW_GAME.name())) {
					mGameModel.startGame();
				} else if (e.getActionCommand().equals(Actions.EXIT.name())) {
					System.exit(0);
				}
			}
		};
		mWindow.init(keyListener(), windowButtonsListener);
	}

	public void startGame() {
		mGameModel.startGame();

		while (true) {
			if (mGameModel.getCurrentState() == GameState.Game) {
				mGameModel.timerTick();
				drawWindow();
			}
			try {
				Thread.sleep(300); // the timing mechanism
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
		mWindow.draw(mGameModel.getGameArea(), mGameModel.getScoreValue(), mGameModel.getCurrentFigure(),
				mGameModel.getNextFigure());
	}

	public void onFinishEvent() {
		mWindow.onFinishGame();
	}
}
