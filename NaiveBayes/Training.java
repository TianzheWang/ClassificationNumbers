package NaiveBayes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Training {
	
	//0 Variables
	//0.0
	private Info[] numInfo;
	int num[] = new int[10];
	int numTemp = 0;
	
	//0.1
	public int[] getNum(){
		return num;
	}
	
	public Info[] getInfoArray(){
		return numInfo;
	}
	
	
	//1 Constructor and Initialization
	//1.0
	
	
	//2 Functions
	//2.0
	public void handleImage(String line,int lineNum) {
		int countLineLevel = 1;
		int countStarNum = 0;
		int ptrS = 0;
		int ptrE = 0;
		for (int i = 0; i < line.length(); i++) {
			if (line.charAt(i) == '+' || line.charAt(i) == '#') {
				ptrS = i;
				break;
			}
		}
		
		for (int i = line.length() - 1; i >= 0; i--) {
			if (line.charAt(i) == '+' || line.charAt(i) == '#') {
				ptrE = i;
				break;
			}
		}
		
		if (line.charAt(0) == '+' || line.charAt(0) == '#') {
			countStarNum++;
			countLineLevel = 0;
		}
		
		for (int i = 1; i < line.length(); i++) {
			if (line.charAt(i-1) != ' ' && line.charAt(i) == ' ') {
				countLineLevel++;
			}
			if (line.charAt(i) == '+' || line.charAt(i) == '#') {
				countStarNum++;
			}
		}
		numInfo[numTemp].fl[lineNum].lineLevel[countLineLevel]++;
		numInfo[numTemp].fl[lineNum].starNum[countStarNum]++;
		numInfo[numTemp].fl[lineNum].starWidth[ptrE - ptrS + 1]++;
	}
	
	//2.1
	public void handleLabel(String number) {
		numTemp = Integer.parseInt(number);
		num[numTemp]++;
	}
	
	//2.2
	public void initial() {
		numInfo = new Info[10];
		for (int i = 0; i < 10; i++) {
			numInfo[i] = new Info();
		}
	}
	
	//2.3
	public void readTrainingFile(double proportion){
		//dData = new dealTrainingData();
		initial();
		File image = new File("src/NaiveBayes/trainingimages");
		File number = new File("src/NaiveBayes/traininglabels");
		int count = 0;	
		double imageNum = 0;
		boolean forFlag = true, flag = false, numFlag = true;
		try {
			
			BufferedReader readImage = new BufferedReader(new FileReader(image));
			BufferedReader readNumber = new BufferedReader(new FileReader(number));
			String line;
			String num;
			while ((line = readImage.readLine()) != null) {
				if (numFlag) {
					if ((num = readNumber.readLine()) != null) {
						handleLabel(num);
						imageNum++;
					}
				}
				
				if (forFlag) {
					for (char tmp:line.toCharArray()) {
						if (tmp == '+' || tmp == '#') {
							flag = true;
							forFlag = false;
							break;
						}
						else {
							flag = false;
							count = 0;
						}
					}
				}
				if (flag) {
					count++;
					handleImage(line, count - 1);
				}
				if (count != 20) {
					numFlag = false;
				}
				else {
					if (imageNum == (int)(5000 * proportion)) break;
					numFlag = true;
					forFlag = true;
				}
			}
			readImage.close();
			readNumber.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
}
