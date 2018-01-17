package BalanceGame;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class DisplayPanel extends JPanel {
	private static final long serialVersionUID = -851913793;
	private int platformWidth;
	
	public DisplayPanel(int width) {
		super();
		platformWidth = width;
	}
	
	
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.black);
		int h = getHeight();
		int w = getWidth();
		g.drawLine(w / 2 - platformWidth / 2, h - 50, w / 2 + platformWidth / 2, h - 50);
	}
}
