package PerceptionFace;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Training {
	
	//0.Variables
	//0.0
	int ithNumber = -1;
	int input;
	int[][] inputFeatureTraining;
	int[][] faceOrNot;
	
	final double Alpha = 0.1; 
	Map<Integer, double[]> map;
	
	
	//0.1
	public int[][] getInputFeatureArray() {
		return inputFeatureTraining;
	}
	
	public int[][] getNumberOrNotArray() {
		return faceOrNot;
	}
	
	public Map<Integer,double[]> getMap(){
		return map;
	}
	
	//1.Constructor and Initialization
	//1.0
	public Training(int input) {
		this.input = input;
		inputFeatureTraining = new int[input][4201];
		faceOrNot = new int[input][2];
		for(int i = 0; i < input; i++) {
			inputFeatureTraining[i][0] = 1;
		}
	}
	
	//1.1
	public void initial() {
		map = new HashMap<Integer, double[]>();
		Random rand = new Random();
		for (int j = 0; j < 2; j++) {
			double weight[] = new double[4201];
			for (int i = 0; i < 4201; i++) {
				weight[i] = rand.nextDouble() % 0.0001;
			}
			map.put(j, weight);
		}
	}
	
	
	//2
	//2.0
	public void handleImage(String line, int lineNum) {
		for (int i = 1; i <= 60; i++) {
			if (line.charAt(i-1) == ' ') inputFeatureTraining[ithNumber][lineNum * 60 + i] = 0;
				else inputFeatureTraining[ithNumber][lineNum * 60 + i] = 1;
		}
	}
	
	//2.1
	public void handleLabel(String number) {
		ithNumber++;
		int num = Integer.parseInt(number);
		for (int j = 0; j < 2; j++) {
			if (num == j) {
				faceOrNot[ithNumber][j] = 1;
				break;
			}
		}
	}
	
	//2.2
	public void readTraining(double proportion) {
		//Training=new dealTraining((int)(proportion*451));
		File image = new File("src/PerceptionFace/facedatatrain");  
		File number = new File("src/PerceptionFace/facedatatrainlabels");  
		int count = 0;	
		int imageNum = 0;
		boolean numFlag = true; 
		try {
			BufferedReader readImage = new BufferedReader(new FileReader(image));
			BufferedReader readNumber = new BufferedReader(new FileReader(number));
			String line, num;
			while ((line = readImage.readLine()) != null) {
				if (numFlag) {
					if ((num = readNumber.readLine()) != null) {
						handleLabel(num);
						imageNum++;
					}
				}
				count++;
				handleImage(line, count - 1);
				if (count != 70) {
					numFlag = false;
				}
				else {
					if (imageNum == (int)(451 * proportion)) break;
					numFlag = true;
					count=0;
				}
			}
			readImage.close();
			readNumber.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//2.3
	public void countWeight() {
		double Jsum = Double.MAX_VALUE;
		while (true) {		
			double tmpJsum = 0;
			double[][] sign = new double[input][2];
			
			for (int z = 0; z < 2; z++) {
				double[] weight = map.get(z);
				for (int i = 0; i < input; i++) {
					double sum = 0;
					for(int j = 0; j < 4201; j++) {
						sum += weight[j] * inputFeatureTraining[i][j];
					}
					sign[i][z]=1 / (1 + Math.pow(Math.E, -sum));
				}
				
				for (int i = 0; i < input; i++) {
					if (faceOrNot[i][z] == 0) {
						tmpJsum += -Math.log(1 - sign[i][z]);
					}
					else {
						tmpJsum += -Math.log(sign[i][z]);
					}
				}
			}
			
			tmpJsum = tmpJsum / 5000;
			//System.out.println(tmpJsum);
			if (tmpJsum > Jsum) break;
			else Jsum=tmpJsum;
			
			for (int z = 0; z < 2; z++) {
				double[] weight = map.get(z);
				for (int j = 0; j < 4201; j++) {
					double derivateJ = 0;
					
					for (int i = 0; i < input; i++) {
						derivateJ += (sign[i][z] - faceOrNot[i][z]) * inputFeatureTraining[i][j];
					}
					weight[j] -= Alpha * derivateJ / 5000;
				}
			}
			
			if (Jsum < 0.02) break;
		}
	}

}
