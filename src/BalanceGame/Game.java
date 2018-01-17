package BalanceGame;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JSlider;

import NeuroEvolution.FFNeuralNet;

public class Game implements KeyListener, ActionListener, ChangeListener {
	private JFrame frame;
	private DisplayPanel panel;
	private JPanel cart;
	private boolean left = false;
	private boolean right = false;
	private double velocity = 0;
	private Pole pole;
	// private Pole pole2;
	private Timer tim;
	private JLabel timer;
	private int time = 0;
	private FFNeuralNet currNet;
	private Main delegate;
	private boolean dud = true;
	private JSlider frSlider;
	private JLabel infoLbl;
	private int platformWidth = 200;
	private int halfPlat;
	private int halfFrame;
	private int halfCart;
	private boolean humanMode = false;

	public Game(Main delegate) {
		this.delegate = delegate;
		frame = new JFrame("Pole Balance");
		frame.setBounds(200, 100, 700, 250);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		frame.setResizable(false);

		panel = new DisplayPanel(platformWidth);
		halfPlat = platformWidth / 2;
		halfFrame = frame.getWidth() / 2;
		panel.setBounds(0, 0, frame.getWidth(), frame.getHeight());
		frame.add(panel);
		// frame.addKeyListener(this);

		cart = new JPanel();
		int cartSize = 20;
		halfCart = cartSize / 2;
		cart.setBounds(frame.getWidth() / 2 - cartSize / 2, frame.getHeight() - 50 - cartSize, cartSize, cartSize);
		cart.setBackground(Color.black);
		frame.add(cart);

		frSlider = new JSlider(0, 50, 1);
		frame.add(frSlider);
		frSlider.addChangeListener(this);
		frSlider.setBackground(Color.RED);
		frSlider.setBounds(0, 0, 200, 50);

		infoLbl = new JLabel("Top 5 Main Population");
		infoLbl.setBounds(200, 0, 150, 40);
		frame.add(infoLbl);

		timer = new JLabel(String.format("%d", time), JLabel.CENTER);
		timer.setBackground(Color.red);
		timer.setSize(100, 100);
		frame.add(timer);

		pole = new Pole(50);
		pole.setBounds(cart.getLocation().x + cartSize / 2 - pole.length, cart.getLocation().y - pole.length,
				pole.length * 2, pole.length);
		frame.add(pole);

		JCheckBox humanTgl = new JCheckBox("Enable Human Mode");
		humanTgl.setBounds(0, 30, 300, 100);
		humanTgl.setBackground(Color.RED);
		humanTgl.addActionListener(this);
		frame.add(humanTgl);

		frame.setVisible(true);

		// pole2 = new Pole(25);
		// pole2.setBounds(cart.getLocation().x + cartSize / 2 - pole2.length,
		// cart.getLocation().y - pole2.length, pole2.length * 2, pole2.length);
		// frame.add(pole2);

		// tim = new Timer(frSlider.getValue(),this);

		tim = new Timer(40, this);
		tim.start();
		frame.requestFocus();
	}

	public void testNet(FFNeuralNet testSubject, boolean top5, boolean childTest) {
		currNet = testSubject;
		if (top5) {
			tim.setDelay(40);
			if (childTest) {
				infoLbl.setText("Top 5 children");
			} else {
				infoLbl.setText("Top 5 Main Population");
			}
		} else {
			infoLbl.setText("General Testing");
			tim.setDelay(frSlider.getValue());
		}
		tim.start();
	}

	public void testNet(FFNeuralNet testSubject, boolean top5) {
		currNet = testSubject;
		if (top5) {
			tim.setDelay(40);
		} else {
			tim.setDelay(frSlider.getValue());
		}
		tim.start();
	}

	private void reset() {
		tim.stop();
		if (!humanMode && dud) {
			currNet.fitness = 0;
		} else {
			currNet.fitness = time;
		}
		dud = true;
		time = 0;
		int cartSize = 20;
		cart.setBounds(halfFrame - halfCart, frame.getHeight() - 50 - cartSize, cartSize, cartSize);
		velocity = 0;
		pole.reset();
		pole.setBounds(cart.getLocation().x + halfCart - pole.length, cart.getLocation().y - pole.length,
				pole.length * 2, pole.length);
		// pole2.reset();
		// pole2.setBounds(cart.getLocation().x + cartSize / 2 - pole2.length,
		// cart.getLocation().y - pole2.length, pole2.length * 2, pole2.length);
		if (!humanMode) delegate.fitnessEvaluated();
		tim.start();
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == 37) {
			left = true;
		} else if (e.getKeyCode() == 39) {
			right = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == 37) {
			left = false;
		} else if (e.getKeyCode() == 39) {
			right = false;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String cmd = e.getActionCommand();
		if (cmd != null && cmd.equals("Enable Human Mode")) {
			humanMode = ((JCheckBox) e.getSource()).isSelected();
			if (humanMode) {
				frame.addKeyListener(this);
			} else {
				frame.removeKeyListener(this);
			}
			frame.requestFocus();
		} else if (cmd == null) {
			timer.setText(String.format("%d", time++));
			pole.repaint();
			// pole2.repaint();

			// allow neural net to set left and right variable
			// 3 inputs are: pole angle, angular momentum, x position
			// all inputs are manipulated so ideal value is 0 (0.5 when put
			// through sigmoid)
			// possible 4th input: pole angular velocity

			if (!humanMode) {
				double[] inputs = new double[3];
				inputs[0] = 1 / (1 + Math.exp(pole.angle - Math.PI / 2));
				inputs[1] = 1 / (1 + Math.exp(cart.getLocation().x - frame.getWidth() / 2));
				inputs[2] = 1 / (1 + Math.exp(pole.omega));
				double[] outputs = currNet.propagate(inputs);
				left = (outputs[0] >= 0.5);
				right = (outputs[1] >= 0.5);
			}
			if (left) {
				velocity -= 0.5;
			}
			if (right) {
				velocity += 0.5;
			}
			if (!humanMode && velocity != 0) {
				dud = false;
			}
			// if (pole.calculateAngle(left,right) && pole2.calculateAngle(left,
			// right)) {
			if (pole.calculateAngle(left, right)) {
				Point p = cart.getLocation();
				p.x += velocity;
				if (p.x > halfFrame + halfPlat || p.x + cart.getSize().width < halfFrame - halfPlat) {
					// game over
					reset();
				} else {
					cart.setLocation(p);
					p = pole.getLocation();
					p.x += velocity;
					pole.setLocation(p);

					// p = pole2.getLocation();
					// p.x += velocity;
					// pole2.setLocation(p);
				}
			} else {
				// game over b/c the pole fell
				reset();
			}
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		JSlider source = (JSlider) e.getSource();
		if (!source.getValueIsAdjusting()) {
			tim.setDelay(source.getValue());
		}
		frame.requestFocus();
	}
}
