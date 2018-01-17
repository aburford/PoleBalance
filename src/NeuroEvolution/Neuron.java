package NeuroEvolution;

public class Neuron implements Cloneable {
	private double[] synapses;
	private double value;
	
	public double getValue() {
		return value;
	}
	
	public Neuron clone() {
		return new Neuron(synapses.clone());
	}
	
	public void mutate(double mutationRate) {
		for (int i = 0; i < synapses.length; i++) {
			if (Math.random() >= 0.5) {
				synapses[i] += (Math.random() / 5 - 0.1) * mutationRate;
			}
		}
	}
	
	public String toString() {
		String retStr = "";
		for (double weight : synapses) {
			retStr += weight + "\n";
		}
		return retStr;
	}
	
	public Neuron(int numOfInputs) {
		synapses = new double[numOfInputs];
		for (int i = 0; i < numOfInputs; i++) {
			synapses[i] = Math.random() * 2 - 1;
		}
	}
	
	public Neuron(double[] synapses) {
		this.synapses = synapses;
	}
	
	public void calculateVal(double[] inputs) {
		value = 0;
		for (int i = 0; i < inputs.length; i++) {
			value += synapses[i] * inputs[i];
		}
		value = 1 / (1 + Math.exp(value));
	}
	
	public void calculateVal(Neuron[] inputNodes) {
		value = 0;
		for (int i = 0; i < inputNodes.length; i++) {
			value += synapses[i] * inputNodes[i].value;
		}
		value = 1 / (1 + Math.exp(value));
	}
	
}
