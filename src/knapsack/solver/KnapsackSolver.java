package knapsack.solver;

import java.util.List;

import timemeasure.CPUTiming;
import filehandle.FileLoader;
import knapsack.entities.Instance;
import knapsack.entities.Item;

public class KnapsackSolver {
	
	private static Instance instance;

	public static void main(String[] args) {
		FileLoader loader = new FileLoader();
		List<Instance> instList = loader.loadFile();
		
		long startTime = CPUTiming.getCpuTime();
		for (Instance inst : instList) {
			instance = inst;
			// TODO call SA
		}
		long cpuTime = CPUTiming.getCpuTime() - startTime;
		System.out.println((float)cpuTime/(float) 50);
	}
}
