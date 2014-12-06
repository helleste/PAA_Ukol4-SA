package knapsack.solver;

import java.util.BitSet;
import java.util.Random;

import knapsack.entities.Instance;
import knapsack.entities.Knapsack;

public class SimulatedAnnealing {
	
	private Instance instance;
	private Knapsack stateKnapsack;
	private Knapsack bestKnapsack;
	private double temperature = 0;
	private final double COOLING_FACTOR = 0.96;
	private final double ENDING_TEMPERATURE = 2;
	private int M; 
	
	public Knapsack solve(Instance instance) {
		this.instance = instance;
		stateKnapsack = new Knapsack(instance.getKnapsack());
		bestKnapsack = new Knapsack(instance.getKnapsack());
		M = instance.getnSize() * 5; // * 5
		
		setInitState();
		temperature = setInitTemperature();
		
		while (temperature > ENDING_TEMPERATURE) {
			for (int m = 0; m < M; m++) {
				stateKnapsack = getNextState();
				
				if (stateKnapsack.getPrice() > bestKnapsack.getPrice())
					bestKnapsack = stateKnapsack;
			}
			
			cool();
		}
		
		return bestKnapsack;
	}
	
	private void setInitState() {
		stateKnapsack.setItemsInBag(BitSet.valueOf(new long[]{0}));
		stateKnapsack.setPrice(0);
		stateKnapsack.setWeight(0);
	}
	
	private Knapsack getNextState() {
		Knapsack newKnapsack = new Knapsack();
		
		do {
			newKnapsack = getNeighbour();
		} while (newKnapsack.getWeight() > instance.getKnapsack().getLimit());
		
		int delta = newKnapsack.getPrice() - stateKnapsack.getPrice();
		
		if (delta > 0)
			return newKnapsack;
		else {
			double x = Math.random();
			if (x < Math.exp(delta/temperature))
				return newKnapsack;
			else
				return stateKnapsack;
		}
	}
	
	private Knapsack getNeighbour() {
		Knapsack newKnapsack = new Knapsack(stateKnapsack);
		Random r = new Random();
		int x = r.nextInt(instance.getnSize());
		
		newKnapsack.getItemsInBag().flip(x);
		calculateKnapsackPrice(newKnapsack);
		calculateKnapsackWeight(newKnapsack);
		
		return newKnapsack;
	}
	
	private void calculateKnapsackPrice(Knapsack knapsack) {
		int index = -1, newPrice = 0;
		
		while ((index = knapsack.getItemsInBag().nextSetBit(index + 1)) != -1) {
			newPrice += instance.getItemPool().getItems()[index].getPrice();
		}
		
		knapsack.setPrice(newPrice);
	}
	
	private void calculateKnapsackWeight(Knapsack knapsack) {
		int index = -1, newWeight = 0;
		
		while ((index = knapsack.getItemsInBag().nextSetBit(index + 1)) != -1) {
			newWeight += instance.getItemPool().getItems()[index].getWeight();
		}
		
		knapsack.setWeight(newWeight);
	}
	
	private double setInitTemperature() {
		int sumCost = 0, sumWeight = 0;
		
		for (int i = 0; i < instance.getnSize(); i++) {
			sumCost += instance.getItemPool().getItems()[i].getPrice();
			sumWeight += instance.getItemPool().getItems()[i].getWeight();
		}
		
		return (sumCost / instance.getnSize()) / (sumWeight / instance.getKnapsack().getLimit()); //* 2
	}
	
	private void cool() {
		temperature = temperature * COOLING_FACTOR;
	}
}
