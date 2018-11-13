package com.company.tetris.view;

import javax.swing.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.company.tetris.Actions;
import com.company.tetris.Constants;
import com.company.tetris.model.Shape;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;

@Component
public class MainWindow extends JComponent {
	private JFrame mWindow;
	private JLabel mScoreLabel;

	private final int mCellOffset = 21;
	private final int mCellSize = 16;
	private final int mBrickCenterWidth = mCellSize / 2;
	
	private int[][] mGameArea;
	private int mScoreValue;
	private Shape mCurrentFigure, mNextFigure;

	private KeyListener mKeyListener;
	
	private final Color mBackgroundColor = new Color(101, 112, 93);
	private BasicStroke mPen = new BasicStroke(2);

	public void init(KeyListener keyListener, ActionListener windowButtonsListener) {
		mWindow = new JFrame();
		mWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mWindow.setBounds(30, 30, 400, 490);
		mWindow.getContentPane().add(this);
		mWindow.getContentPane().setBackground(new Color(108, 121, 91));
		mWindow.setVisible(true);
		mWindow.setFocusable(true);
		mWindow.setResizable(false);
		mWindow.getContentPane().setLayout(null);
		mScoreLabel = new JLabel("SCORE: 0");
		mScoreLabel.setBounds(225, 0, 200, 85);
		mScoreLabel.setFont(mScoreLabel.getFont().deriveFont(18.0f));
		mWindow.add(mScoreLabel);
		JLabel lbl = new JLabel(Constants.NEXT_STRING);
		lbl.setBounds(225, 30, 100, 100);
		lbl.setFont(mScoreLabel.getFont().deriveFont(18.0f));
		mWindow.add(lbl);
		mWindow.addKeyListener(keyListener);
		initMenu(windowButtonsListener);
	}

	private void initMenu(ActionListener windowButtonsListener) {
		JMenuBar menu = new JMenuBar();

		JMenu file = new JMenu(Constants.FILE_STRING);
		JMenuItem newGame = new JMenuItem(Constants.NEW_GAME_STRING);
		newGame.addActionListener(windowButtonsListener);
		newGame.setActionCommand(Actions.NEW_GAME.name());

		JMenuItem quit = new JMenuItem(Constants.QUIT_STRING);
		quit.addActionListener(windowButtonsListener);
		quit.setActionCommand(Actions.EXIT.name());
		file.add(newGame);
		file.add(quit);

		menu.add(file);

		mWindow.setJMenuBar(menu);
	}

	public void onFinishGame() {
		JOptionPane.showMessageDialog(null, Constants.GAME_OVER_STRING);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setColor(mBackgroundColor);
		g.drawRect(220, 25, 150, 37);
		g.drawRect(220, 66, 150, 143);
		Graphics2D g2D = (Graphics2D) g;
		g2D.setStroke(mPen);
		drawGrid(g2D);
		drawBrickArea(g2D);
		drawShape(g2D);
		mScoreLabel.setText("SCORE: " + mScoreValue);
	}

	private void drawGrid(Graphics2D g) {
		for (int x = 0; x < Constants.GameParameters.GAME_AREA_WIDTH; x++) {
			for (int y = 0; y < Constants.GameParameters.GAME_AREA_HEIGHT - 1; y++) {
				int x1 = x * mCellOffset;
				int y1 = y * mCellOffset;
				g.drawRect(x1 + 3, y1 + 3, mCellSize, mCellSize);
			}
		}
	}

	private void drawBrickArea(Graphics2D g) {
		if (mGameArea == null) {
			return;
		}
		for (int x = 0; x < Constants.GameParameters.GAME_AREA_WIDTH; x++) {
			for (int y = 0; y < Constants.GameParameters.GAME_AREA_HEIGHT; y++) {
				if (mGameArea[x][y] > 0) {
					drawBrick(g, x, y);
				}
			}
		}
	}

	private void drawBrick(Graphics2D g, int x, int y) {
		x *= mCellOffset;
		y *= mCellOffset;
		g.setColor(Color.black);
		g.drawRect(x + 3, y + 3, mCellSize, mCellSize);
		g.fillRect(x + 7, y + 7, mBrickCenterWidth, mBrickCenterWidth);
	}

	private void drawBrickNext(Graphics2D g, int x, int y) {
		x *= mCellOffset;
		y *= mCellOffset;
		AffineTransform oldTransform = g.getTransform();
		g.translate(250, 115);
		g.setColor(Color.black);
		g.drawRect(x + 3, y + 3, mCellSize, mCellSize);
		g.fillRect(x + 7, y + 7, mBrickCenterWidth, mBrickCenterWidth);
		g.setTransform(oldTransform);
	}

	private void drawShape(Graphics2D g) {
		if (mCurrentFigure != null) {
			for (int x = 0; x < 4; x++)
				for (int y = 0; y < 4; y++) {
					if (mCurrentFigure.picture[x][y]) {
						drawBrick(g, (mCurrentFigure.x + x), (mCurrentFigure.y + y));
					}
				}
		}

		if (mNextFigure != null) {
			for (int x = 0; x < 4; x++)
				for (int y = 0; y < 4; y++) {
					if (mNextFigure.picture[x][y]) {
						drawBrickNext(g, x, y);
					}
				}
		}
	}

	public void draw(int[][] gameArea, int scoreValue, Shape currentFigure, Shape nextFigure) {
		mGameArea = gameArea;
		mScoreValue = scoreValue;
		mCurrentFigure = currentFigure;
		mNextFigure = nextFigure;
		mWindow.repaint();
	}
}