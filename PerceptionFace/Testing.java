package PerceptionFace;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Testing {

	//0
	//0.0
	Map<Integer, double[]> map;
	//int[][] inputFeatureTesting;
	List<Integer> guessValueList;
	List<Integer> orinValueList;
	
	int[][] inputFeatureTest = new int[150][4201];
	static int ithNumber=0;
	
	//0.1
	public List<Integer> getGuessValueList() {
		return guessValueList;
	}
	
	public List<Integer> getOrinValueList() {
		return orinValueList;
	}
	
	//1.Constructor and Initialization
	//1.0
	public Testing() {
		for (int i = 0; i < 150; i++) {
			inputFeatureTest[i][0] = 1;
		}
	}
	
	//1.1
	
	public void initial() {
		guessValueList = new ArrayList<Integer>();
		orinValueList = new ArrayList<Integer>();
	}
	
	//1.2
	public void initial(Map<Integer,double[]> map, int[][] inputFeatureTest) {
		guessValueList = new ArrayList<Integer>();
		orinValueList = new ArrayList<Integer>();
		
		this.inputFeatureTest = inputFeatureTest;
		this.map = map;
	}
	
		
	//2
	//2.0
	public void handleImage(String line, int lineNum) {
		for (int i = 1; i <= 60; i++) {
			if (line.charAt(i - 1) == ' ') inputFeatureTest[ithNumber][lineNum * 60 + i] = 0;
			else inputFeatureTest[ithNumber][lineNum * 60 + i] = 1;
		}
		
		if (lineNum == 69) ithNumber++;
	}
	
	//2.1
	public void readTesting() {
		//dtest=new dealTesting();
		File image = new File("src/PerceptionFace/facedatatest");  
		File number = new File("src/PerceptionFace/facedatatestlabels");  
		int count = 0;
		boolean numFlag = true; 
		try {
			BufferedReader readImage = new BufferedReader(new FileReader(image));
			BufferedReader readNumber = new BufferedReader(new FileReader(number));
			String line, num;
			
			while ((line = readImage.readLine()) != null) {
				if (numFlag) {
					if ((num = readNumber.readLine()) != null) {
						orinValueList.add(Integer.parseInt(num));
					}
				}
				count++;
				handleImage(line, count - 1);
				if (count != 70) numFlag = false;
				else {
					numFlag = true;
					count = 0;
				}
			}
			readImage.close();
			readNumber.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}	
	}
	
	//2.2
	public void findNumber() {
		for (int i = 0; i < 150; i++) {
			double record=-100;
			int guessNum=0;
			for (int z = 0; z < 2; z++) {
				double weight[] = map.get(z);
				double sum = 0;
				
				for (int j = 0; j < 4201; j++) {
					sum += weight[j] * inputFeatureTest[i][j];
				}
				
				double distaceToOne = 1 / (1 + Math.pow(Math.E, -sum));
				if(record < distaceToOne) {
					record = distaceToOne;
					guessNum = z;
				}
			}
			guessValueList.add(guessNum);
		}
	}
	
}
