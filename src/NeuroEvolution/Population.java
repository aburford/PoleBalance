package NeuroEvolution;

import java.util.Arrays;

public class Population {
	public FFNeuralNet[] parents;
	public FFNeuralNet[] children;
	
	public Population(int size, int[] topology) {
		parents = new FFNeuralNet[size];
		for (int i = 0; i < size; i++) {
			parents[i] = new FFNeuralNet(topology);
		}
	}
	
	public int getBestFitness() {
		int highest = 0;
		for (FFNeuralNet net : parents) {
			if (highest < net.fitness) {
				highest = net.fitness;
			}
		}
		return highest;
	}
	
	public String temp() {
		Arrays.sort(parents);
		return parents[0].toString();
	}
	
	public void mutateTopQuarterAndReplace() {
		Arrays.sort(parents);
		int size = parents.length;
		double mutationRate = 1;
		for (int i = size / 4 - 1; i >= 0; i--) {
			parents[size / 4 + i] = parents[i].mutated(mutationRate);
			parents[size / 2 + i] = parents[i].mutated(mutationRate);
			parents[size * 3 / 4 + i] = parents[i].mutated(mutationRate);
			mutationRate *= 0.98;
		}
	}
	
	public void genChildren() {
		children = new FFNeuralNet[parents.length / 2];
		for (int i = 0; i < parents.length / 2; i++) {
			children[i] = parents[i].breed(parents[i + 1]);
		}
	}
	
	public void mergeChildren() {
		FFNeuralNet[] newPop = new FFNeuralNet[parents.length];
		Arrays.sort(children);
		Arrays.sort(parents);
		int childI = 0;
		int parentI = 0;
		int newI = 0;
		while (childI < children.length && newI < newPop.length) {
			if (parents[parentI].fitness > children[childI].fitness) {
				newPop[newI] = parents[parentI];
				parentI++;
			} else {
				newPop[newI] = children[childI];
				childI++;
			}
			newI++;
		}
		while (newI < newPop.length) {
			newPop[newI] = parents[parentI];
			parentI++;
			newI++;
		}
		parents = newPop;
		double mutationRate = 1;
		for (int i = parents.length - 1; i >= 0; i--) {
			parents[i].mutateSelf(mutationRate);
			mutationRate *= 0.9;
		}
	}
}