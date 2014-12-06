package knapsack.solver;

import java.util.Arrays;

import knapsack.entities.Instance;
import knapsack.entities.Item;
import knapsack.entities.Knapsack;

public class DynamicProgramming {
	
	private int[][] W;
	private Instance instance;
	
	public Knapsack solve(Instance instance) {
		this.instance = instance;
		initW(countMaxPrice(instance.getItemPool().getItems()),instance.getnSize());
		fillArray();
		findBestSolution(W.length -1, instance.getnSize(), instance.getKnapsack().getLimit());
		
		return this.instance.getKnapsack();
	}
	
	private void initW(int maxPrice, int instSize) {
		int rows = maxPrice + 1;
		int columns = instSize + 1;
		W = new int[rows][columns];
		
		for (int i = 1; i < W.length; i++) {
			W[i][0] = Integer.MAX_VALUE; // Set first column values to infinity
		}
	}
	
	private void fillArray() {
		int ci1, wi1; // Possible problem
		
		for (int c = 1; c < W.length; c++) {
			for (int i = 1; i < W[c].length; i++) {
				ci1 = instance.getItemPool().getItems()[i-1].getPrice();
				wi1 = instance.getItemPool().getItems()[i-1].getWeight();
				W[c][i] = getArrayValue(i-1, c, ci1, wi1);
			}
		}
	}
	
	private int getArrayValue(int i, int c, int ci1, int wi1) {
		int option1 = W[c][i];
		int option2;
		
		if (c-ci1 < 0 || W[c-ci1][i] == Integer.MAX_VALUE)
			option2 = Integer.MAX_VALUE;
		else
			option2 =  W[c-ci1][i] + wi1;
		
		return Math.min(option1, option2);
	}
	
	private int countMaxPrice(Item[] items) {
		int maxPrice = 0;
		
		for (Item item : items) {
			maxPrice += item.getPrice();
		}
		
		return maxPrice;
	}
	
	private void printW() {
		for (int i = W.length -1; i >= 0; i--) {
			System.out.println(Arrays.toString(W[i]));
		}
	}
	
	private void findBestSolution(int c, int searchColumn, int limit) {
		for (; c >= 1; c--) {
			if(W[c][searchColumn] <= limit) {
				if(searchColumn == instance.getnSize()) {
					instance.getKnapsack().setPrice(c);
					limit = W[c][searchColumn];
				}
				
				if (W[c][searchColumn] != W[c][searchColumn - 1]) {
					instance.getKnapsack().getItemsInBag().set(searchColumn);
					limit = getLimit(limit, searchColumn);
				}
				
				if (limit != 0) 
					findBestSolution(c, searchColumn -1, limit);
				
				return;
			}
		}
	}
	
	private int getLimit(int oldLimit, int searchColumn) {
		return oldLimit - instance.getItemPool().getItems()[searchColumn-1].getWeight();
	}

}
