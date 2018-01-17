package BalanceGame;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;

public class Cart extends JPanel {
	private static final long serialVersionUID = -851913793;
	private int a = 0;
	@Override
	public void paint(Graphics g) {
		a++;
		g.setColor(Color.red);
		g.fillRect(0, a, getWidth(), getHeight());
		System.out.println("painting");
	}
}
