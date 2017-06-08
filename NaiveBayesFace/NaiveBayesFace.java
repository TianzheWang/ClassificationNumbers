package NaiveBayesFace;

import java.util.List;


class featureLine{
	int lineLevel[];
	int starNum[];
	int starWidth[];
	double probStarNum[];
	double probLevel[];
	double probStarWidth[];
	public featureLine(){
		lineLevel = new int[60];
		starNum = new int[61];
		starWidth = new int[61];
		probStarNum = new double[61];
		probLevel = new double[60];
		probStarWidth = new double[61];
	}
}

class Info{
	featureLine[] fl;
	int[] space;
	int[][] everyPoint;
	double probSpace[];
	double probPoint[][];
	public Info(){
		fl=new featureLine[70];
		for(int i=0;i<70;i++){
			fl[i]=new featureLine();
		}
		space=new int[500];
		probSpace=new double[500];
		everyPoint=new int[4200][2];
		probPoint=new double[4200][2];
	}
}

public class NaiveBayesFace {
	public static void main(String args[]) {
		double k = 0.004;
		
		for (double proportion = 0.1; proportion <= 1.0; proportion = proportion + 0.1) {
			
			Training train=new Training();
			train.initial();
			
			long startTime=System.currentTimeMillis();
			train.readTrainingFile(proportion);
			long endTime=System.currentTimeMillis();
			
			System.out.println("Training time is " + (endTime - startTime) + "ms for each proportion.");
			
			int[] num = train.getNum();
			Info[] info = train.getInfoArray();
			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < 70; j++) {
					for (int z = 0; z < 60; z++) {
						info[i].fl[j].probLevel[z]=(double)(info[i].fl[j].lineLevel[z]+k)/(num[i]+k);
						info[i].fl[j].probStarNum[z+1]=(double)(info[i].fl[j].starNum[z+1]+k)/(num[i]+k);
						info[i].fl[j].probStarWidth[z+1]=(double)(info[i].fl[j].starWidth[z+1]+k)/(num[i]+k);
					}
					info[i].fl[j].probStarNum[0]=(double)(info[i].fl[j].starNum[0]+k)/(num[i]+k);
					info[i].fl[j].probStarWidth[0]=(double)(info[i].fl[j].starWidth[0]+k)/(num[i]+k);
				}	
				for(int j=0;j<500;j++){
					info[i].probSpace[j]=(double)(info[i].space[j]+k)/(num[i]+k);
				}
				for(int pi=0;pi<4200;pi++){
					for(int pj=0;pj<2;pj++){
						info[i].probPoint[pi][pj]=(double)(info[i].everyPoint[pi][pj]+k)/(num[i]+k);
					}
				}
			}
			
			Testing test = new Testing(info);
			test.readTestFile();
			List<Integer> originalList = test.getOriginal();
			List<Integer> guessList = test.getGuessValue();
			int same = 0, different = 0;
			
			for(int i = 0; i < originalList.size(); i++) {
				if(originalList.get(i) == guessList.get(i)) same++;
				else different++;
			}
			
			System.out.println("Proportion is " + String.format("%.2f", proportion) +
					". Same rate is " + String.format("%.3f", (double)same / (different + same)));
			//System.out.println();
		}
	}
}
