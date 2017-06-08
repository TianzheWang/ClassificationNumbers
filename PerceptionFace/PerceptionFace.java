package PerceptionFace;

import java.util.*;

public class PerceptionFace {
	public static void main(String args[]){
		Testing test = new Testing();
		test.initial();
		test.readTesting();
		List<Integer> oringinalValue = test.getOrinValueList();
		
		for (double proportion = 0.1; proportion <= 1.0; proportion += 0.1) {
			
			Training train = new Training((int)(proportion * 451));
			train.readTraining(proportion);
			
			train.initial();
			
			long startTime = System.currentTimeMillis();
			train.countWeight();
			long endTime = System.currentTimeMillis();
			
			System.out.println("Training time is " + (endTime - startTime) + "ms for each proportion.");
			
			test.initial(train.map, test.inputFeatureTest);
			test.findNumber();
			List<Integer> guessValue = test.getGuessValueList();
			int same = 0, different = 0;
			for (int i = 0; i < 150; i++) {
				if (guessValue.get(i) == oringinalValue.get(i)) same++;
				else different++;
			}
			
			System.out.println("Proportion is " + String.format("%.2f", proportion) + ". Same rate is " + String.format("%.3f", (double)same / (different + same)));
			//System.out.println();
			
			guessValue = new ArrayList<>();
		}
	}
}

