package Mira;

import java.io.IOException;

public class DataClassifier {

	public static void main(String[] args) throws IOException {
		ExtractFeature ext = new ExtractFeature();
		int[] a = ext.ExtraLabels(1);
		double[] Py = new double[10];
		for(int i=0; i<a.length;i++){
			Py[i] = (double)a[i]/5000;
			//System.out.println(Py[i]);
		}
		
		double var = 0;
		for(int k = 0;k<10;k++){
			var+=0.1;
			long startTime = System.currentTimeMillis();
			
			//extract features for bayesian
			double[][][] conditionProbability=ext.BasicExtraDigitFeature(0.005,var);
			double[][][][] conditionPro = ext.EnhancedExtraDigitFeature(2,0.001,var);	//length of pixel, smooth variable, percentage
			long endTime   = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			System.out.println("Training time is "+totalTime + "ms for each proportion");
			System.out.print(String.format("proportion is %.2f", var)+". ");
			//naive bayesian
//			NaiveBayesian test = new NaiveBayesian();
//			test.TestImage(conditionProbability,Py);
//			test.EnhanceTestImage(conditionPro, Py, 2);
		}
		System.out.println();
			//perceptron
//			Perceptron ppp = new Perceptron();
//			double[] temp = ppp.EnhancedExtraDigitFeature(4);
//			for(int i=0;i<30;i++){
//				ppp.training(temp, 4, var);	//temp, length of pixels, percentage
//			}
//			ppp.perTest(4);
			
			double var1 = 0;
			for(int k = 0; k < 10; k++) {
				var1 += 0.1;
				long startTime = System.currentTimeMillis();
			//mira
			//2*2 and 0.31 -->0.833
			Mira mmm = new Mira(2);
			double[] tempp = mmm.EnhancedExtraDigitFeature(2);
			for (int i = 0; i < 30; i++) {						//
				mmm.training(tempp, 2, var1, 0.0032);	//temp, length of pixels, percentage, constant			
			}
			long endTime = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			System.out.println("Training time is "+totalTime + "ms for each proportion");
			System.out.print(String.format("proportion is %.2f", var1) +". ");
			mmm.perTest(2);
		}
		System.out.println();
		
		
//		double var2 = 0;
//		for(int k = 0;k<10;k++){
//			var2+=0.1;
//			long startTime = System.currentTimeMillis();
//			//mira face
//			MiraFace mf = new MiraFace(2);
//			double[] tempf = mf.EnhancedExtraFaceFeature(2);
//			for (int i = 0; i < 30; i++) {
//				mf.training(tempf, 2, var2, 0.001);		//temp, length of pixels, percentage, constant	
//			}
//			long endTime = System.currentTimeMillis();
//			long totalTime = endTime - startTime;
//			
//			System.out.println("Training time is "+totalTime + "ms for each proportion");
//			System.out.print(String.format("proportion is %.2f", var2)+". ");
//			mf.perTest(2);
//		}
	}
}