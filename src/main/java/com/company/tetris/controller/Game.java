package com.company.tetris.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.company.tetris.Actions;
import com.company.tetris.GameState;
import com.company.tetris.model.Model;
import com.company.tetris.view.MainWindow;

@Configuration
@ComponentScan("com.company.tetris.controller")
public class Game implements Model.OnFinishEvent {

	private Model mGameModel;
	private MainWindow mWindow;

	@Bean
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

	public Game() {
		ApplicationContext context = new ClassPathXmlApplicationContext("ApplicationContext.xml");
		mGameModel = (Model) context.getBean("model");
		mWindow = (MainWindow) context.getBean("view");
		mGameModel.setFinishEvent(this);

//		ApplicationContext viewContext = new AnnotationConfigApplicationContext(Game.class);
//		mWindow = viewContext.getBean(MainWindow.class);
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
