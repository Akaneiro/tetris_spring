package com.company.tetris;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.company.tetris.controller.Game;
import com.company.tetris.model.Model;
import com.company.tetris.model.ShapeFactory;
import com.company.tetris.view.MainWindow;

@Configuration
public class TetrisConfig {
	
	@Bean
	public MainWindow mainWindow() {
		return new MainWindow();
	}
	
	@Bean
	public Model model() {
		return new Model();
	}
	
	@Bean
	public Game game() {
		return new Game();
	}
	
	@Bean
	public ShapeFactory shapeFactory() {
		return new ShapeFactory();
	}
}
