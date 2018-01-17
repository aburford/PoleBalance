package NeuroEvolution;

public class FFNeuralNet implements Comparable<FFNeuralNet> {
	private	Neuron[] hiddenLayer;
	private Neuron[] outputLayer;
	public int fitness;
	
	public FFNeuralNet(int[] topology) {
		// initialize each connection with random weights
		int inputs = topology[0];
		int hiddens = topology[1];
		int outputs = topology[2];
		
		hiddenLayer = new Neuron[hiddens];
		for (int i = 0; i < hiddens; i++) {
			hiddenLayer[i] = new Neuron(inputs);
		}
		outputLayer = new Neuron[outputs];
		for (int i = 0; i < outputs; i++) {
			outputLayer[i] = new Neuron(hiddens);
		}
	}
	
	private FFNeuralNet(Neuron[] hiddenLayer, Neuron[] outputLayer) {
		this.hiddenLayer = hiddenLayer;
		this.outputLayer = outputLayer;
	}
	
	public double[] propagate(double[] inputs) {
		// propagate inputs through the network
		for (Neuron node : hiddenLayer) {
			node.calculateVal(inputs);
		}
		for (Neuron node : outputLayer) {
			node.calculateVal(hiddenLayer);
		}
		
		// copy the values of the output nodes into an array to return
		double[] returnVals = new double[outputLayer.length];
		for (int i = 0; i < outputLayer.length; i++) {
			returnVals[i] = outputLayer[i].getValue();
		}
		return returnVals;
	}
	
	public void mutateSelf(double mutationRate){
		for (Neuron cell : hiddenLayer) {
			cell.mutate(mutationRate);
		}
		for (Neuron cell : outputLayer) {
			cell.mutate(mutationRate);
		}
	}
	
	public String toString() {
		String retStr = "";
		for (int i = 0; i < hiddenLayer.length; i++) {
			retStr += "Hidden Layer Cell " + i + " Synapses:\n";
			retStr += hiddenLayer[i].toString();
		}
		for (int i = 0; i < outputLayer.length; i++) {
			retStr += "Output Layer Cell " + i + " Synapses:\n";
			retStr += outputLayer[i].toString();
		}
		return retStr;
	}
	
	public FFNeuralNet mutated(double mutationRate) {
		Neuron[] hiddenCopy = new Neuron[hiddenLayer.length];
		int i = 0;
		for (Neuron cell : hiddenLayer) {
			hiddenCopy[i++] = cell.clone(); 
		}
		i = 0;
		Neuron[] outputCopy = new Neuron[outputLayer.length];
		for (Neuron cell : outputLayer) {
			outputCopy[i++] = cell.clone(); 
		}
		FFNeuralNet copy = new FFNeuralNet(hiddenCopy, outputCopy);
		copy.mutateSelf(mutationRate);
		return copy;
	}
	
	public FFNeuralNet breed(FFNeuralNet partner) {
		Neuron[] newHidden = new Neuron[hiddenLayer.length];
		Neuron[] newOutput = new Neuron[outputLayer.length];
		for (int i = 0; i < hiddenLayer.length; i++) {
			if (Math.random() >= 0.5) {
				newHidden[i] = hiddenLayer[i];
			} else {
				newHidden[i] = partner.hiddenLayer[i];
			}
		}
		for (int i = 0; i < outputLayer.length; i++) {
			if (Math.random() >= 0.5) {
				newOutput[i] = outputLayer[i];
			} else {
				newOutput[i] = partner.outputLayer[i];
			}
		}
		return new FFNeuralNet(newHidden, newOutput);
	}

	@Override
	public int compareTo(FFNeuralNet o) {
		if (o.fitness < fitness) {
			return -1;
		} else if (o.fitness == fitness) {
			return 0;
		} else {
			return 1;
		}
	}
}
