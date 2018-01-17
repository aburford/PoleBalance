package BalanceGame;

import java.awt.EventQueue;

import NeuroEvolution.Population;

public class Main {
	private Population pop;
	private int popIndex;
	private Game game;
	private boolean childEval = false;
	private int popSize = 500;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main me = new Main();					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public Main() {
		game = new Game(this);
		int[] topology = {3,6,2};
		pop = new Population(popSize, topology);
		popIndex = 0;
		game.testNet(pop.parents[popIndex], true);
	}
	
	public void fitnessEvaluated() {
//		if (!childEval) {
//			// we are currently evaluating the main population
//			if (++popIndex < popSize) {
//				game.testNet(pop.parents[popIndex], (popIndex < 5), false);
//			} else {
//				System.out.println("Best fitness: " + pop.getBestFitness());
//				pop.genChildren();
//				childEval = true;
//				popIndex = 0;
//				game.testNet(pop.children[popIndex], true, true);
//			}
//		} else if (childEval) {
//			// we are just evaluating the children
//			if (++popIndex < popSize / 2) {
//				game.testNet(pop.children[popIndex], (popIndex < 5), true);
//			} else {
//				pop.mergeChildren();
//				childEval = false;
//				popIndex = 0;
//				game.testNet(pop.parents[popIndex], true, false);
//			}
//		}
		
		if (++popIndex < popSize) {
			game.testNet(pop.parents[popIndex], (popIndex < 3));
		} else {
			System.out.println("Best fitness: " + pop.getBestFitness());
			String temp = pop.temp();
			pop.mutateTopQuarterAndReplace();
			popIndex = 0;
			game.testNet(pop.parents[popIndex], true);
			System.out.println("Best ANN:\n" + pop.parents[0].toString());
		}
	}
	
}
