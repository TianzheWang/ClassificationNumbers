package NaiveBayesFace;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Training {
	
	//0 Variables
	//0.0
	private Info[] numInfo;
	int num[] = new int[2];
	int numTemp, countSpace;
	
	
	//0.1
	public int[] getNum() {
		return num;
	}
	
	public Info[] getInfoArray() {
		return numInfo;
	}
	
	//1
	//
	public void initial() {
		numInfo=new Info[2];
		for(int i=0;i<2;i++){
			numInfo[i]=new Info();
		}
		countSpace=0;
	}
	
	//2
	//2.0
	public void handleImage(String line, int lineNum) {
		int countLineLevel=1;
		int countStarNum=0;
		int ptrS=0;
		int ptrE=0;
		
		for (int i = 0; i < line.length(); i++) {
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
		
		if (line.charAt(0) == '+' || line.charAt(0) == '#') {
			countStarNum++;
			countLineLevel = 0;
			numInfo[numTemp].everyPoint[lineNum*60][1]++;
		}
		else {
			numInfo[numTemp].everyPoint[lineNum*60][0]++;
		}
		
		for (int i=1;i<line.length();i++) {
			if(line.charAt(i-1)!=' '&&line.charAt(i)==' '){
				countLineLevel++;
			}
			if(line.charAt(i)=='+'||line.charAt(i)=='#'){
				countStarNum++;
				numInfo[numTemp].everyPoint[lineNum*60+i][1]++;
			}else{
				numInfo[numTemp].everyPoint[lineNum*60+i][0]++;
			}
		}
		
		//System.out.println(line);
		countSpace=countSpace+60-countStarNum;
		numInfo[numTemp].fl[lineNum].lineLevel[countLineLevel]++;
		numInfo[numTemp].fl[lineNum].starNum[countStarNum]++;
		numInfo[numTemp].fl[lineNum].starWidth[ptrE-ptrS+1]++;
		if(lineNum==69){
			//System.out.println(countSpace/100);
			numInfo[numTemp].space[countSpace/10]++;
			countSpace=0;
		}
	}
	
	//2.1
	public void handleLabel(String number){
		numTemp=Integer.parseInt(number);
		num[numTemp]++;
	}
	
	//2.2
	public void readTrainingFile(double proportion){
		//dData=new dealTrainingData();
		//dData.initial();	
		File image=new File("src/NaiveBayesFace/facedatatrain");   //change path
		File number=new File("src/NaiveBayesFace/facedatatrainlabels");  //change path
		int count=0;		 //控制deallabel
		boolean numFlag=true;  //控制dealimage 
		int imageNum=0;
		try{
			BufferedReader readImage= new BufferedReader(new FileReader(image));
			BufferedReader readNumber= new BufferedReader(new FileReader(number));
			String line;
			String num;
			while((line=readImage.readLine())!=null){
				if(numFlag){
					if((num=readNumber.readLine())!=null){
						handleLabel(num);
						imageNum++;
					}
				}
				count++;
				handleImage(line, count - 1);
				if(count!=70){
					numFlag=false;
				}else{
					if(imageNum==(int)(451*proportion)) break;
					numFlag=true;
					count=0;
				}
			}
			readImage.close();
			readNumber.close();
		}catch(Exception e){
			e.printStackTrace();
		}	
	}
	
}
