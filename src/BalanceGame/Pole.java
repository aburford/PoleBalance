package BalanceGame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class Pole extends JPanel {
	private static final long serialVersionUID = -851361793;
	public double angle;
	// omega is angular velocity
	public double omega;
	public int length = 50;
	private double sin = 1;
	private double cos = 0;
	private final double HALF_PI = Math.PI / 2;
	private double velocityConst;
	private double gravityConst;
	
	public void reset() {
		omega = 0;
		if (Math.random() >= 0.5) {
			angle = HALF_PI + 0.2;
		} else {
			angle = HALF_PI - 0.2;
		}
	}
	
	public Pole(int newLength) {
		length = newLength;
		reset();
		velocityConst = length / 3333.0;
		gravityConst = length / 5000.0;
	}
	
	public boolean calculateAngle(boolean leftA, boolean rightA) {
		sin = Math.sin(angle);
		cos = Math.cos(angle);
		double weight = Math.pow(sin,5);
		if (leftA && !rightA) {
			omega -= velocityConst * weight;
		} else if (rightA && !leftA) {
			omega += velocityConst * weight;
		}
		if (angle > HALF_PI) {
			omega += gravityConst * (1 - weight);
		} else {
			omega -= gravityConst * (1 - weight);
		}
		angle += omega;
		if (angle > Math.PI || angle < 0) {
			return false;
		}
		sin = Math.sin(angle);
		cos = Math.cos(angle);
		return true;
	}
	
	@Override
	public void paint(Graphics g) {
		sin *= length;
		cos *= length;
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(2));
		g.setColor(Color.red);
		g.drawLine(length, getHeight(), length + (int)cos, getHeight() - (int)sin);
	}
}
