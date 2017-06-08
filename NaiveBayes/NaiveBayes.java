package NaiveBayes;

import java.util.*;

class featureLine {
	int col = 20;
	int lineLevel[];
	int starNum[];
	int starWidth[];
	double probStarNum[];
	double probLevel[];
	double probStarWidth[];
	public featureLine(){
		lineLevel = new int[col];
		starNum = new int[col + 1];
		starWidth = new int[col + 1];
		probStarNum = new double[col + 1];
		probLevel = new double[col];
		probStarWidth = new double[col + 1];
	}
}

class Info {
	featureLine[] fl;
	int col = 20;
	
	public Info() {
		fl = new featureLine[col];
		for (int i = 0; i < col; i++) {
			fl[i] = new featureLine();
		}
	}
}

public class NaiveBayes {
	public static void main(String args[]) {
		int col = 20;
		double k = 0.001;
		
		for (double proportion = 0.1; proportion <= 1; proportion += 0.1) {
			Training train = new Training();
			long startTime = System.currentTimeMillis();
			train.readTrainingFile(proportion);
			long endTime = System.currentTimeMillis();
			
			System.out.println("Training time is " + (endTime - startTime) + "ms for each proportion.");
			
			int[] num = train.getNum();
			Info[] info = train.getInfoArray();
			
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < col; j++) {
					for (int z = 0; z < col; z++) {
						info[i].fl[j].probLevel[z]=(double)(info[i].fl[j].lineLevel[z]+k)/(num[i]+k);
						info[i].fl[j].probStarNum[z+1]=(double)(info[i].fl[j].starNum[z+1]+k)/(num[i]+k);
						info[i].fl[j].probStarWidth[z+1]=(double)(info[i].fl[j].starWidth[z+1]+k)/(num[i]+k);
					}
				}
			}
			
			Testing test = new Testing(info);
			test.initialRead(info);
			test.readTestFile();
			List<Integer> oList = test.getCmpOriginal();
			List<Integer> gList = test.getGuessValue();
			int same = 0, different = 0;
			
			for (int i = 0; i < oList.size(); i++) {
				if (oList.get(i) == gList.get(i)) same++;
				else different++;
			}
			System.out.println("Proportion is " + String.format("%.2f", proportion) + ". Same rate is " + (double)same / (different + same));
			//System.out.println();
		}
	}
}
