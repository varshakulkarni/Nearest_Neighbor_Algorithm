import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class Project {

	public static void main(String[] args) throws FileNotFoundException {
		//first input file
		Scanner s1 = new Scanner(new File("input1.txt"));
		
		PrintStream myconsole=new PrintStream("output.txt");
		System.setOut(myconsole);
		
		double finalEr = 0;
		ArrayList<Integer> arrayOfErrors = new ArrayList<Integer>();
		int numberOfFolds = s1.nextInt();
		int samplesCount = s1.nextInt();
		int permutationCount = s1.nextInt();
		int permutations[] = new int[samplesCount];
		int permMatrix[][] = new int[permutationCount][samplesCount] ;
		
		for(int i = 0; i < permutationCount; i++){
			
			for(int j = 0; j < samplesCount; j++){
				permMatrix[i][j] = s1.nextInt();
			}
		}
		Scanner s = new Scanner(new File("input2.txt"));
		int rows = s.nextInt();
		int columns = s.nextInt();
		int finalMatrix[][] = new int[rows][columns];
		ArrayList<int[][]> grids = new ArrayList<int[][]>();
		for(int knn = 1; knn < 6 ; knn++){
		for(int i = 0; i < permutationCount; i++)
		{
			for(int j = 0; j < samplesCount; j++)
			{
				permutations[j] = permMatrix[i][j]+1;
				
			}
			
			arrayOfErrors.add(start(samplesCount,numberOfFolds,permutations,knn,finalMatrix));
			grids.add(finalMatrix);
		}
		
		for(int k = 0; k < arrayOfErrors.size();k++){
			finalEr = finalEr + (double)(arrayOfErrors.get(k)/(float)samplesCount);
		}
		
		double err = finalEr/(double)permutationCount;
		double variance = 0;
		
		for(int k = 0; k < arrayOfErrors.size();k++){
			variance = variance + ((err - ((double)(arrayOfErrors.get(k)/(float)samplesCount))) * (err - ((double)(arrayOfErrors.get(k)/(float)samplesCount))));
		}
		myconsole.println("k = "+knn+" e = "+err+" sigma= "+Math.sqrt((double)variance/(float)(permutationCount - 1)));
		
		for(int k = 0; k<rows; k++){
			for(int j = 0;j<columns;j++){
				if(finalMatrix[k][j] == 1){
					myconsole.print("+" + " ");
				}
				if(finalMatrix[k][j] == 0){
					myconsole.print("-" + " ");
				}
			}
			myconsole.println();
		}
		myconsole.println();
		grids.clear();
		arrayOfErrors.clear();
		finalEr = 0;
		variance = 0;
		}
		s1.close();
		
	}
	public static int start(int samplesCount,int numberOfFolds,int[] permutations, int knn,int[][] finalMatrix) throws FileNotFoundException{
		
		Scanner s2=new Scanner(new File("input2.txt"));
		int rows=s2.nextInt();
		int columns=s2.nextInt();
		int a=0;
		int aDots=0;
		int x1[]=new int[samplesCount];
		int x2[]=new int[samplesCount];
		int y[]= new int[samplesCount];
		int x1Dots[]=new int[(rows*columns)-samplesCount];
		int x2Dots[]=new int[(rows*columns)-samplesCount];
	
		String[][] data=new String[rows][columns];
		
		for(int i=0;i<rows;i++)
		{
			for(int j=0;j<columns;j++)
			{
				data[i][j]=s2.next();
				
				if(Character.valueOf(data[i][j].charAt(0)).equals('-')){
					x1[a]=j;
					x2[a]=i;
					y[a]=0; //taking '-' as 0 and '+' as 1
					a++;
				}
				if(Character.valueOf(data[i][j].charAt(0)).equals('+')){
					x1[a]=j;
					x2[a]=i;
					y[a]=1; //taking '-' as 0 and '+' as 1
					a++;
				}
				if(Character.valueOf(data[i][j].charAt(0)).equals('.')){
					x1Dots[aDots]=j;
					x2Dots[aDots]=i;
					aDots++;
				}
			}
		}
		/*	for(int i=0;i<rowsTrainingData;i++)
			{
				
				for(int j=0;j<columnsTrainingData;j++)
				{
					System.out.print(data[i][j]+" ");
				}
				System.out.println();
		}
			for(int i=0;i<9;i++)
			{
				System.out.println(x1[i]+" "+x2[i]+ " "+y[i]);
			}	
				*/
	//int numOfSamp=9;
	//int numberOfFolds=2;
	int minNoInFold = samplesCount/numberOfFolds;
	
	int additionalInFold = samplesCount % numberOfFolds;
	
	int count2 = samplesCount-1;
	int count3 = 0;
	boolean flag = true;
	if(additionalInFold != 0){
		count3 = minNoInFold;
	}
	else{
		count3 = minNoInFold-1;
		flag  = false;
		}
	int[][] partitions= new int[numberOfFolds][minNoInFold+1];
	
	
	for(int i = numberOfFolds-1 ; i >= 0 ; i--){
		for(int j = count3 ; j >= 0 ; j--){
				partitions[i][j] = permutations[count2];
				count2--;
			
		if(additionalInFold != 0){
			additionalInFold--;
		}
		
		if(additionalInFold == 0 && flag == true){
			count3--;
			flag = false;
		}
	}
	}
	
/*	for(int i = 0; i < numberOfFolds; i++){
		
		for(int j = 0; j < minNoInFold+1; j++){
			
			System.out.print(partitions[i][j]);
		}
		System.out.println();
	}
	*/
	ArrayList<HashMap<Integer, Float>> distIndex = new ArrayList<HashMap<Integer, Float>>();
	ArrayList<Integer> sort = new ArrayList<Integer>();
	int error = 0;
	int estimate = 0;

	int counter = 0;
	ArrayList<Integer> neighbor = new ArrayList<Integer>();
	ArrayList<Integer> positive = new ArrayList<Integer>();
	ArrayList<Integer> negative = new ArrayList<Integer>();
	
	for(int z = 0 ; z < numberOfFolds; z++)
	{	
		for(int b = 0 ; b < minNoInFold + 1 ; b++)
		{	
			if(partitions[z][b] != 0)
			{
				Map<Integer, Float> Map1 = new HashMap<Integer, Float>();
				for(int c = 0; c < numberOfFolds; c++)
				{
					for(int d = 0; d < minNoInFold + 1 ; d++)
					{
						if(z != c && partitions[c][d] != 0)
						{
							Map1.put(partitions[c][d],(float) twoPtsDist(x1[partitions[z][b]-1],x2[partitions[z][b]-1],x1[partitions[c][d]-1],x2[partitions[c][d]-1]));
						}
					}			
				}
				distIndex.add((HashMap<Integer, Float>) Map1);
			}
		}
	}
	
	//Distance MAP for dots
			ArrayList<HashMap<Integer, Float>> dotsDistIndex = new ArrayList<HashMap<Integer, Float>>();
			for(int i = 0; i < x1Dots.length ; i++)
			{
				Map<Integer, Float> Map2 = new HashMap<Integer, Float>();
				for(int j = 0; j < x1.length ; j++)
				{
					Map2.put(j+1 , (float) twoPtsDist(x1Dots[i],x2Dots[i],x1[j],x2[j]));
				}
				dotsDistIndex.add((HashMap<Integer, Float>) Map2);
			}
			
		
			for(int  i= 0; i<samplesCount;i++)
			{
				
				finalMatrix[x2[i]][x1[i]] = y[i];
			}
		
			for(int i = 0; i < numberOfFolds; i++)
			{
				for(int j = 0; j < minNoInFold+1; j++)
				{
					if(partitions[i][j] != 0)
					{
						sort = sortHashMapByValue(distIndex.get(counter));
						counter++;
						for(int k = 0; k < Math.min(knn,sort.size());k++ )
						{
							if(sort.get(k) != null)
							{
								neighbor.add(sort.get(k));
							}
						}
				
				for(int l = 0; l < neighbor.size() ; l++)
				{
					if(y[neighbor.get(l)-1] == 0)
					{
						negative.add(neighbor.get(l));
					}
					else
					{
						positive.add(neighbor.get(l));
					}
				}
				
				if(positive.size() > negative.size())
				{
					estimate = 1;
				}
				else if(positive.size() < negative.size())
				{
					estimate = 0;
				}
				else
				{
					estimate = 0;
				}
				
				if(y[partitions[i][j] - 1] != estimate)
				{
					error++;
				}
				neighbor.clear();
				positive.clear();
				negative.clear();
			}
			}
		}
		//Dots
		
		for(int j = 0; j < dotsDistIndex.size() ; j++)
		{
			sort = sortHashMapByValue(dotsDistIndex.get(j));
			for(int k = 0; k < Math.min(knn,sort.size());k++ )
			{
				if(sort.get(k) != null)
				{
					neighbor.add(sort.get(k));
				}
			}
			
			for(int l = 0; l < neighbor.size() ; l++)
			{
				if(y[neighbor.get(l)-1] == 0)
				{
					negative.add(neighbor.get(l));
				}
				else
				{
					positive.add(neighbor.get(l));
				}
			}
			
			if(positive.size() > negative.size())
			{
				estimate = 1;
			}
			else if(positive.size() < negative.size())
			{
				estimate = 0;
			}
			else
			{
				estimate = 0;
			}
			finalMatrix[x2Dots[j]][x1Dots[j]] = estimate;
			
			neighbor.clear();
			positive.clear();
			negative.clear();
		}
		
		
	
	s2.close();
	return error;
}

	public static ArrayList<Integer> sortHashMapByValue(final HashMap<Integer, Float> map) {
	    ArrayList<Integer> keys = new ArrayList<Integer>();
	    keys.addAll(map.keySet());
	    
	    ArrayList<Integer> sortedKeys = new ArrayList<Integer>();
	    Collections.sort(keys, new Comparator<Integer>() {
	        

			@Override
			public int compare(Integer arg0, Integer arg1)
			{
		
				Float f1 = map.get(arg0);
				Float f2 = map.get(arg1);
				if (f1 == null)
				{
	                return (f2 != null) ? 1 : 0;
	            } 
				else if ((f1 != null) && (f2 != null)) 
				{
	                return  f1.compareTo(f2);
	            }
	            else
	            {
	                return 0;
	            }
			}
	    });

	   
	   for (Integer key : keys)
	   {
	        sortedKeys.add(key);     
	   }
	   return sortedKeys;
	}
	


	private static double twoPtsDist(int x1, int y1, int x2, int y2) 
	{
	return Math.sqrt(( (x1 - x2)*(x1 - x2) ) + ( (y1 - y2) * (y1 - y2)) );
	}
}