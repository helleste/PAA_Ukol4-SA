package knapsack.solver;

import java.util.BitSet;
import java.util.List;
import java.util.Random;

import timemeasure.CPUTiming;
import filehandle.FileLoader;
import knapsack.entities.Instance;
import knapsack.entities.Item;
import knapsack.entities.Knapsack;

public class KnapsackSolver {

	public static void main(String[] args) {
		FileLoader loader = new FileLoader();
		List<Instance> instList = loader.loadFile();
		float totalRelativeError = 0, relativeError;
		int optPrice, appPrice;
		
		long startTime = CPUTiming.getCpuTime();
		for (Instance inst : instList) {
			SimulatedAnnealing sa = new SimulatedAnnealing();
			Knapsack knapsack = sa.solve(inst);
			appPrice = knapsack.getPrice();
			System.out.println(inst.getId() + ": " + knapsack.getPrice());
			DynamicProgramming dp = new DynamicProgramming();
			knapsack = dp.solve(inst);
			optPrice = knapsack.getPrice();
			System.out.println(inst.getId() + ": " + knapsack.getPrice());
			relativeError = relativeError(optPrice, appPrice);
			totalRelativeError += relativeError;
		}
		long cpuTime = CPUTiming.getCpuTime() - startTime;
		System.out.println("total :"  + totalRelativeError/50);
		System.out.println((float)cpuTime/(float) 50);
	}
	
	// Compute the relative error
	private static float relativeError(int optPrice, int appPrice) {
		return Math.abs(((float)optPrice - (float)appPrice))/Math.abs((float)optPrice);
	}
}
