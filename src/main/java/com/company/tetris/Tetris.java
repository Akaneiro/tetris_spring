package com.company.tetris;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.company.tetris.controller.Game;
import com.company.tetris.view.MainWindow;

public class Tetris {
	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(TetrisConfig.class);
		Game tetris = context.getBean(Game.class);
		tetris.startGame();
	}
}
