package Mira;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class ExtractFeature {
	private int datum_width = 28;
	private int datum_height = 28;
	private int[] labelcount;
	public int[] ExtraLabels(int a ) throws IOException{
		if(a==1)//extra digit labels
		{
			labelcount = new int[10];
			File file = new File("src/Mira/traininglabels") ;
			Scanner sc = new Scanner(file);
			while(sc.hasNextInt()){
				labelcount[sc.nextInt()]++;
			}
			sc.close();
			return labelcount;
		}
		else if(a==0)//extra face labels
		{
			labelcount = new int[2];
			File file = new File("src/facedata/facedatetrainlabels") ;
			Scanner sc = new Scanner(file);
			while(sc.hasNextInt()){
				labelcount[sc.nextInt()]++;
			}
			sc.close();
			return labelcount;
		}
		return null;
	}
	public double[][][] BasicExtraDigitFeature() throws IOException{
		return BasicExtraDigitFeature(0,1);
	}
	public double[][][] BasicExtraDigitFeature(double kk) throws IOException{
		return BasicExtraDigitFeature(kk, 1);
	}
	public double[][][] BasicExtraDigitFeature(double kk, double percent) throws IOException
	{	
//		int width=datum_width/len;
//		int height=datum_height/len;
//		if(datum_width%len!=0||datum_height%len!=0) throw new IllegalArgumentException("length should be devided by "+ datum_width);
		double[][][] feature = new double[10][datum_height][datum_width];
		//classification// matrix //number of + or #
		File file_I = new File("src/Mira/trainingimages") ;
		File file_L = new File("src/Mira/traininglabels") ;
		BufferedReader readImage = new BufferedReader(new FileReader("src/Mira/trainingimages"));
		Scanner sc = new Scanner(file_L);
		//Scanner scI = new Scanner(file_I);
		String line;
		int times =0;
		while(sc.hasNextInt()&&times<(int)(5000*percent)){
			int label = sc.nextInt();
			for(int i=0;i<datum_height;i++){
				line = readImage.readLine();
				if(line==null){
					throw new IllegalArgumentException("file not fit with each other!");
				}
				for(int j=0;j<line.length();j++){
					if(line.charAt(j)=='+'||line.charAt(j)=='#')
						feature[label][i][j]++;
				}				
			}
			times++;
		}
		for(int i=0;i<10;i++){
			for(int j=0;j<datum_height;j++){
				for(int k=0;k<datum_height;k++){
					feature[i][j][k]=(feature[i][j][k]+kk)/(labelcount[i]+2*kk);
				}
			}
		}
		return feature;
	}
	
	public double[][][][] EnhancedExtraDigitFeature(int len) throws IOException
	{
		return EnhancedExtraDigitFeature(len, 0,1);
	}
	public double[][][][] EnhancedExtraDigitFeature(int len, double kk) throws IOException{
		return EnhancedExtraDigitFeature(len,kk,1);
	}
	public double[][][][] EnhancedExtraDigitFeature(int len, double kk, double percent) throws IOException
	{	
		
		int width=datum_width/len;
		int height=datum_height/len;
		if(datum_width%len!=0||datum_height%len!=0) throw new IllegalArgumentException("length should be devided by "+ datum_width);

		double[][][][] feature = new double[10][width][height][len*len+1];
		
		//classification// matrix //number of + or #
		File file_I = new File("src/Mira/trainingimages") ;
		File file_L = new File("src/Mira/traininglabels") ;
		BufferedReader readImage = new BufferedReader(new FileReader("src/Mira/trainingimages"));
		Scanner sc = new Scanner(file_L);
		//Scanner scI = new Scanner(file_I);
		String line;
		
		int[][] featureTemp;
		int times =0;
		while(sc.hasNextInt()&&times<(int)(5000*percent)){
			int label = sc.nextInt();
			featureTemp = new int[width][height];
			for(int i=0;i<datum_height;i++){
				line = readImage.readLine();
				if(line==null){
					throw new IllegalArgumentException("file not fit with each other!");
				}
				for(int j=0;j<line.length();j++){
					if(line.charAt(j)=='+'||line.charAt(j)=='#')
						featureTemp[i/len][j/len]++;
				}				
			}			
			for(int i=0;i<height;i++){
				for(int j=0;j<width;j++){
					feature[label][i][j][featureTemp[i][j]]++;
				}
			}
			times++;
			//System.out.println("test!");
		}
		for(int i=0;i<10;i++){
			for(int j=0;j<height;j++){
				for(int k=0;k<width;k++){
					for(int l=0;l<len*len+1;l++)
						feature[i][j][k][l]=(feature[i][j][k][l]+kk)/(labelcount[i]+(len*len+1)*kk);
				}
			}
		}
		return feature;
	}
}
