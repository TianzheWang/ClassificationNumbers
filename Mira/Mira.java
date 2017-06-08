package Mira;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Mira {
	private double[][] weight;
	double trainingfeature[][];
	private int datum_width = 28;
	private int datum_height = 28;
	double percent;
	Mira(){
		percent =1;
		weight = new double[10][28*28];
	}
	Mira(int len){
		percent =1;
		int number = datum_width/len;
		weight = new double[10][number*number];
	}
	public void training(double[] feature, int len, double per, double C) throws FileNotFoundException{
		double LastWei=0;
		double TotalWeight=0;
		int length = 28*28/(len*len);
		int n = 0;
		File file = new File("src/Mira/traininglabels") ;
		Scanner sc = new Scanner(file);
		int times=0;
		while(sc.hasNextInt()&&times<(int)(5000*per)){
			int label = sc.nextInt();
			double [] tempFeature = new double[length];
			for(int i=0;i<tempFeature.length;i++){
				tempFeature[i] = feature[n*length+i];
			}
			int predict=0;
			for(int i=0;i<10;i++){
				for(int j=0;j<tempFeature.length;j++){
					TotalWeight+=tempFeature[j]*weight[i][j];
				}
				if(i==0) LastWei=TotalWeight;
				if(TotalWeight>LastWei){
					predict=i;
					LastWei = TotalWeight;
				}
			}
			if(predict!=label){
				double t=0;
				double numer=0;
				double denom=0;
				for(int i=0;i<tempFeature.length;i++){
					numer+=(weight[predict][i]-weight[label][i])*tempFeature[i];
					denom+=tempFeature[i]*tempFeature[i];
				}
				numer +=1;
				denom*=2;
				t = Math.min(C, numer/denom);
				//t = numer/denom;
				for(int j=0;j<tempFeature.length;j++){
					weight[predict][j]-=t*tempFeature[j];
					weight[label][j]+=t*tempFeature[j];
				}
			}
			times++;
			n++;
		}
		sc.close();
	}
	public void perTest() throws IOException{
		perTest(1);
	}
	public void perTest(int len) throws IOException{
		int height=28*5000/len;
		int width=datum_width/len;
		if(datum_width%len!=0||datum_height%len!=0) throw new IllegalArgumentException("length should be devided by "+ datum_width);
		// read feature of test image
		double[][] feature = new double[height][width];		
		BufferedReader readImage = new BufferedReader(new FileReader("src/Mira/testimages"));
		String line;	
			for(int i=0;i<28*1000;i++){
				line = readImage.readLine();
				if(line==null){
					throw new IllegalArgumentException("file not fit with each other!");
				}
				for(int j=0;j<line.length();j++){
					if(line.charAt(j)=='+'||line.charAt(j)=='#')
						feature[i/len][j/len]++;
				}				
			}			
			//System.out.println("test!");
			for(int j=0;j<height;j++){
				for(int k=0;k<width;k++){
						feature[j][k]=feature[j][k]/(len*len);
				}
			}
		double correct = 0;
		double LastWei=0;
		double TotalWeight=0;
		int length = 28*28/(len*len);
		int n =0;
		//test
		File file = new File("src/Mira/testlabels") ;
		Scanner sc = new Scanner(file);
		double [] temp = Convert(feature);
		while(sc.hasNextInt()){
			int label = sc.nextInt();
			double [] tempFeature = new double[length];
			for(int i=0;i<tempFeature.length;i++){
				tempFeature[i] = temp[n*length+i];
			}
			int predict=0;
			for(int i=0;i<10;i++){
				for(int j=0;j<tempFeature.length;j++){
					TotalWeight+=tempFeature[j]*weight[i][j];
				}
				if(i==0) LastWei=TotalWeight;
				if(TotalWeight>LastWei){
					predict=i;
					LastWei = TotalWeight;
				}
			}
			if(predict==label){
				correct++;
			}
			n++;
		}
		System.out.println("Mira same rate is "+ correct/1000.0 +'.');
		readImage.close();
		sc.close();
	}
	
	public double[] EnhancedExtraDigitFeature(int len) throws IOException
	{	
		int height=28*5000/len;
		int width=datum_width/len;
		if(datum_width%len!=0||datum_height%len!=0) throw new IllegalArgumentException("length should be devided by "+ datum_width);

		double[][] feature = new double[height][width];
		
		BufferedReader readImage = new BufferedReader(new FileReader("src/Mira/trainingimages"));
		String line;	
			for(int i=0;i<28*5000;i++){
				line = readImage.readLine();
				if(line==null){
					throw new IllegalArgumentException("file not fit with each other!");
				}
				for(int j=0;j<line.length();j++){
					if(line.charAt(j)=='+'||line.charAt(j)=='#')
						feature[i/len][j/len]++;
				}				
			}			
			//System.out.println("test!");
			for(int j=0;j<height;j++){
				for(int k=0;k<width;k++){
						feature[j][k]=feature[j][k]/(len*len);
				}
			}
		readImage.close();
		return Convert(feature);
	}
	public double[] Convert(double a[][]){
		double temp[];
		int length = a[0].length;
		temp = new double[a.length*length];
		for(int i=0;i<a.length;i++){
			for(int j=0;j<length;j++){
				temp[i*length+j]=a[i][j];
			}
		}
		return temp;
	}
}
