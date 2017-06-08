package NaiveBayes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Testing {
	
	//0
	//0.0
	List<Integer> guessValueList;
	Info[] numInfo;
	double[] storeProb = new double[10];
	double[] probNum = new double[10];
	List<Integer> cmpOriginal;
	
	//0.1
	public List<Integer> getGuessValue(){
		return guessValueList;
	}
	
	//0.2
	public List<Integer> getCmpOriginal(){
		return cmpOriginal;
	}
	
	
	//1
	//1.0
	public Testing(Info[] info) {
		this.numInfo = info;
		guessValueList = new ArrayList<Integer>();
		for (int i = 0; i < 10; i++) {
			storeProb[i] = 1;
		}
	}
	
	//1.1
	public void initialRead(Info[] info) {
		//dData=new dealTestData(info);
		cmpOriginal = new ArrayList<Integer>(); 
	}
	
	//2
	//2.0
	public void handleImage(String line, int lineNum) {
		int countLineLevel = 1;
		int ptrS = 0, ptrE = 0;
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
		}
		for (int i = 1; i < line.length(); i++) {
			if (line.charAt(i - 1) != ' ' && line.charAt(i) == ' ') {
				countLineLevel++;
			}
			if (line.charAt(i) == '+' || line.charAt(i) == '#') {
			}
		}
		for (int i = 0; i < 10; i++) {
			storeProb[i] = storeProb[i] * numInfo[i].fl[lineNum].probLevel[countLineLevel] * numInfo[i].fl[lineNum].probStarWidth[ptrE - ptrS + 1];
		}
		if (lineNum == 19) {
			int record = 0;
			double probRecord = storeProb[0] * probNum[0];
			for (int i = 1; i < storeProb.length; i++) {
				if (probRecord < storeProb[i] * probNum[i]) {
					probRecord = storeProb[i] * probNum[i];
					record = i;
				}
			}
			guessValueList.add(record);
			for (int i = 0; i < storeProb.length; i++) {
				 storeProb[i] = (double)1;
			}
		}
	}

	//2.1
	public void handleLabel(int[] countNum) {
		for (int i = 0; i < countNum.length; i++) {
			probNum[i] = (double)countNum[i] / 1000;
		}
	}
	
	//2.2
	public void readTestFile(){
		File image = new File("src/NaiveBayes/testimages");
		File number = new File("src/naiveBayes/testlabels");
		int count = 0;
		int countNum[] = new int[10];
		boolean forFlag = true, flag = false;
		try {
			BufferedReader readImage = new BufferedReader(new FileReader(image));
			BufferedReader readNumber = new BufferedReader(new FileReader(number));
			String line;
			String num;
			while ((num=readNumber.readLine())!=null) {
				countNum[Integer.parseInt(num)]++;
				cmpOriginal.add(Integer.parseInt(num));
			}
			handleLabel(countNum);
			while ((line = readImage.readLine()) != null) {
				if (forFlag) {
					for (char tmp : line.toCharArray()) {
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
				if (count == 20) forFlag = true;
			}
			readImage.close();
			readNumber.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}	
	}
	
}
