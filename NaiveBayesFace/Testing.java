package NaiveBayesFace;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class Testing {
	
	//0
	//0.0
	ArrayList<Integer> guessValueList;
	private Info[] numInfo; //10代表数字
	double[] storeProb = new double[2];
	int countSpace = 0;
	double[] probFaceOrNot = new double[2];
	ArrayList<Double> logProb0 = new ArrayList<Double>();
	ArrayList<Double> logProb1 = new ArrayList<Double>();
	
	ArrayList<Integer> original = new ArrayList<>();
	
	//0.1
	public ArrayList<Integer> getGuessValue() {
		return guessValueList;
	}
	
	public ArrayList<Integer> getOriginal() {
		return original;
	}
	
	//1
	//
	public Testing(Info[] info) {
		this.numInfo = info;
		guessValueList = new ArrayList<Integer>();
		for (int i = 0; i < 2; i++) {
			storeProb[i]=1;
		}
	}
	
	//2
	//2.0
	public void handleImage(String line,int lineNum) throws Exception{
		int countLineLevel=1;
		int countStarNum=0;
		int ptrS=0,ptrE=0;
		for(int i=0;i<line.length();i++){
			if(line.charAt(i)=='+'||line.charAt(i)=='#'){
				ptrS=i;
				break;
			}
		}
		for(int i=line.length()-1;i>=0;i--){
			if(line.charAt(i)=='+'||line.charAt(i)=='#'){
				ptrE=i;
				break;
			}
		}
		if(line.charAt(0)=='+'||line.charAt(0)=='#'){
			countStarNum++;
			countLineLevel=0;
			logProb0.add(numInfo[0].probPoint[lineNum*60][1]);
			logProb1.add(numInfo[1].probPoint[lineNum*60][1]);
		}else{
			logProb0.add(numInfo[0].probPoint[lineNum*60][0]);
			logProb1.add(numInfo[1].probPoint[lineNum*60][0]);
		}
		for(int i=1;i<line.length();i++){
			if(line.charAt(i-1)!=' '&&line.charAt(i)==' '){
				countLineLevel++;
			}
			if(line.charAt(i)=='+'||line.charAt(i)=='#'){
				countStarNum++;
				logProb0.add(numInfo[0].probPoint[lineNum*60+i][1]);
				logProb1.add(numInfo[1].probPoint[lineNum*60+i][1]);
			}else{
				logProb0.add(numInfo[0].probPoint[lineNum*60+i][0]);
				logProb1.add(numInfo[1].probPoint[lineNum*60+i][0]);
			}
		}
		countSpace+=60-countStarNum;
		for(int i=0;i<2;i++){
			storeProb[i]=storeProb[i]*numInfo[i].fl[lineNum].probStarWidth[ptrE-ptrS+1]
						 *numInfo[i].fl[lineNum].probLevel[countLineLevel];
		}
		if(lineNum==69){
			//Thread.sleep(10000);
			int record=storeProb[0]*numInfo[0].probSpace[countSpace/10]*probFaceOrNot[0]>
						storeProb[1]*numInfo[1].probSpace[countSpace/10]*probFaceOrNot[1]?0:1;
//			for(double i:logProb0){
//			}
//			for(double i:logProb1){
//			}
			//int record=a+150*probFaceOrNot[0]>b+150*probFaceOrNot[1]?0:1;
			guessValueList.add(record);
			for(int i=0;i<storeProb.length;i++){
				 storeProb[i]=(double)1;
			}
			countSpace=0;
			logProb0.clear();
			logProb1.clear();
		}
	}
	
	//2.1
	public void handleFaceLabel(int[] countFaceOrNot){
		for(int i=0;i<countFaceOrNot.length;i++){
			probFaceOrNot[i]=(double)countFaceOrNot[i]/150;
		}
	}
	
	//2.2
	public void readTestFile(){
		File image=new File("src/NaiveBayesFace/facedatatest");
		File number=new File("src/NaiveBayesFace/facedatatestlabels");
		int count=0;		 //控制deallabel	 
		int[] countFaceOrNot = new int[2];
		try{
			BufferedReader readImage= new BufferedReader(new FileReader(image));
			BufferedReader readNumber= new BufferedReader(new FileReader(number));
			String line;
			String num;
			while((num=readNumber.readLine())!=null){
				original.add(Integer.parseInt(num));
				countFaceOrNot[Integer.parseInt(num)]++;
			}
			handleFaceLabel(countFaceOrNot);
			while((line=readImage.readLine())!=null){
				count++;
				handleImage(line,count-1);
				if(count==70){
					count=0;
				}
			}
			readImage.close();
			readNumber.close();
		}catch(Exception e){
			e.printStackTrace();
		}	
	}
	//

}
