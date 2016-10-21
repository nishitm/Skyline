import java.util.*;
import java.util.StringTokenizer;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class SFS_Final {
    
	private static List<Double[]> splitArray(Double[] originalArray, int chunkSize) { //This function is used to split the array into no. of arrays

        List<Double[]> listOfArrays = new ArrayList<Double[]>(); 
        int totalSize = originalArray.length;
        if(totalSize < chunkSize )
        {
            chunkSize = totalSize;
        }
        int from = 0;
        int to = chunkSize;

        while(from < totalSize)
        {
            Double[] partArray = Arrays.copyOfRange(originalArray, from, to);
            listOfArrays.add(partArray);

            from+= chunkSize;
            to = from + chunkSize;
            if(to>totalSize)
            {
                to = totalSize;
            }
        }       
        return listOfArrays;
    }
    
    
    public static double[] mergeSort(double [] list) {
        if (list.length <= 1) 
        {
            return list;
        }
        
        // Split the array in half
        double[] first = new double[list.length / 2];
        double[] second = new double[list.length - first.length];
        System.arraycopy(list, 0, first, 0, first.length);
        System.arraycopy(list, first.length, second, 0, second.length);
        
        // Sort each half
        mergeSort(first);
        mergeSort(second);
        
        // Merge the halves together, overwriting the original array
        merge(first, second, list);
        return list;
    }
    
    private static void merge(double[] first, double[] second, double [] result) {
        // Merge both halves into the result array
        // Next element to consider in the first array
        int iFirst = 0;
        // Next element to consider in the second array
        int iSecond = 0;
        
        // Next open position in the result
        int j = 0;
        // As long as neither iFirst nor iSecond is past the end, move the
        // smaller element into the result.
        while (iFirst < first.length && iSecond < second.length) {
            if (first[iFirst] < second[iSecond]) {
                result[j] = first[iFirst];
                iFirst++;
                } else {
                result[j] = second[iSecond];
                iSecond++;
            }
            j++;
        }
        // copy what's left
        System.arraycopy(first, iFirst, result, j, first.length - iFirst);
        System.arraycopy(second, iSecond, result, j, second.length - iSecond);
    }
    
    
    
    public static void main(String[] args) throws IOException 
    {
   
    	long startTime = System.currentTimeMillis(); //Execution time started
        int comp=0;  				//To count the no of comparison
        String token;       //To read from the file

        System.out.println("WAIT.....!!");
        Scanner inFile1 = new Scanner(new File("sample_ant.txt")).useDelimiter("\\s+");        //write your input file name    
        LineNumberReader  reader = new LineNumberReader(new FileReader(new File("sample_ant.txt")));
        
               
        List<String> temps = new ArrayList<String>();
        int cnt = 0; 			//To count the no of lines in the file
        String lineRead = "";
        while ((lineRead = reader.readLine()) != null) {}
        
        cnt = reader.getLineNumber(); 
        reader.close();
       
        while (inFile1.hasNext()) 
        {
          token = inFile1.next();
          temps.add(token);
        }
        inFile1.close();

        String[] tempsArray = temps.toArray(new String[0]); //Contains file elements as a String
        Double[] finalArray = new Double[tempsArray.length]; //Array to store file elements as a Double 
     
        for(int i=0; i<tempsArray.length; i++) //Loop for removing "e" in the data file if present
        {
        	
        	if(tempsArray[i].toLowerCase().contains("e"))
        	{
        		
        		String[] parts = tempsArray[i].split("e");
        		String parts1 = parts[0];
        		String parts2 = parts[1];
        		Double p1 = Double.parseDouble(parts1);
        		Double p2 = Double.parseDouble(parts2);
        		Double p = p1 * Math.pow(10, p2);
        		tempsArray[i] = Double.toString(p);
        		 		
        	}
        	
        }
        
        int x = cnt;  // Total no of Dimension for each objects
        int len = tempsArray.length/x; //no of objects in the file
        
        for(int i=0; i<tempsArray.length; i++)
        {
        	finalArray[i] = Double.parseDouble(tempsArray[i]); //Contain file elements as a double
        }
    
        List<Double[]> obj = splitArray(finalArray, len); //split the Array into multiple small arrays
        

        double[] entropy = new double[x];    //Array that contains object no. for corresponding entropy
        double[] entropy_obj = new double[x]; //Contain sorted entropy

    
        int  token1 = 0; //uses for read query file 
        Scanner inFile2 = new Scanner(new File("query1.txt")).useDelimiter("\\s+"); //write name of query file
        List<Integer> temp = new ArrayList<Integer>();
    
        while (inFile2.hasNext()) 
        {
        	token1 = Integer.parseInt(inFile2.next());
        	temp.add(token1);
        }
        inFile2.close();

        Integer[] tempArray = temp.toArray(new Integer[0]); //Store elements of query file into interger array 
    
        Integer[] dim = Arrays.copyOfRange(tempArray, 0, tempArray.length - 1); //store dimensions on which we want to find skyline
        Integer winSize = tempArray[tempArray.length - 1]; //size of window
    
        for(int i=0; i<x; i++)
        {
        
        	for(int j=0; j<dim.length ;j++)
        	{
        		entropy_obj[i] = entropy_obj[i] + Math.log(obj.get(i)[dim[j]] + 1); //entropy of objects
        	}
        	entropy[i]=i; //objects no respectively to entropy
        
        }
  
     
        for(int i=0;i<entropy_obj.length;i++) //sort entropy of objects
        {
        	for(int j=0; j<entropy_obj.length; j++)
        	{
        		if(entropy_obj[i]<entropy_obj[j])
        		{
        			double temp1 = entropy_obj[i];
        			entropy_obj[i] = entropy_obj[j];
        			entropy_obj[j] = temp1;
    			
        			double temp2 = entropy[i];
        			entropy[i] = entropy[j];
        			entropy[j] = temp2;
    			
        		}
        	}
        }
        
        ArrayList<Integer> tempfile = new ArrayList<Integer>();
        int[] win = new int[winSize]; //window array
  
        for (Integer a : win) //initialize window
        {
        	win[a]= 0;
        }
    
        int wx=0;
        int temp_k=0;
    
        win[0]=(int) entropy[0] + 1; //insert first element in the window
        wx++; 
        
       for(int i=1; i<entropy.length; i++) //loop for checking all objects againts window elements
        {
           int enter=0;
            int equal=0;
            int write=0;
            for(int j=0; j<win.length; j++) //loop for window element
            {
                   if(win[j]==0)
                        continue;
                    enter=0;
                    equal=0;
                     for(int fordim=0; fordim<dim.length; fordim++) // loop for all queried dimension
                    {
                         comp++;
                    int comparision = Double.compare(obj.get((int)entropy[i])[dim[fordim]], obj.get(win[j] - 1)[dim[fordim]]);

                    if(comparision==0) // if value of both the objects are equal in particular dimension
                        equal++;

                    if(obj.get((int)entropy[i])[dim[fordim]] < obj.get(win[j] - 1)[dim[fordim]]) 
                        enter++;
                }
                if(enter + equal == dim.length) //if window object is dominated
                {
                                win[j]= (int)entropy[i]+1;
                                
                                if(write==1)
                                {
                                    win[j]=0;
                                }
                                write=1;
                }
                if(enter ==0) //if new object is dominated
                    break;


            }
            if(enter + equal!= dim.length && enter!=0) // do not dominate each other
            {
                    if(wx>=winSize)
                    {
                        tempfile.add((int)entropy[i]+1); //if window is full add to the temp file
      
                    }
                    else
                    {
                        win[wx]=(int)entropy[i]+1; // add to the window
                        wx++;
      
                    }
             }
        }
      
        for(int i=0; i<winSize; i++)
        {
            for(int j=0; j<winSize; j++)
            {
                if(win[i]>win[j])
                {
                    int temp1=win[i];
                    win[i] = win[j];
                    win[j]=temp1;
                }
            }
        }
        
        ArrayList<Integer> skyline = new ArrayList<Integer>(); // contain final skylines
        int enter=0,rev_enter=0;
        int total=0;
               
        double minIndex; 
        if(tempfile.size()!=0) // loop for checking timestamp values
        {
            minIndex=entropy_obj[tempfile.get(0)];
            for(int i=0; i<tempfile.size(); i++)
            {
                if(entropy_obj[tempfile.get(i)-1]<minIndex)
                    minIndex=entropy_obj[tempfile.get(i)-1];
            }
             
            for(int i=0; i<winSize; i++)
            {
                if(entropy_obj[win[i]-1]<minIndex)
                {
                    skyline.add(win[i]);
                    win[i]=0;
                }
            
            }
        }
        
        int re_enter=0;
        if(tempfile.size()!=0)
        {
            for(int i=0; i <winSize; i++) //compare remaining windows elements to the temporory file element
            {
                if(win[i]==0)
                    continue;
                for(int j=0; j<tempfile.size(); j++)
                {
                    enter=0;
                    re_enter=0;
                    
                    if(tempfile.get(j) <= 0)
                        continue;
                     
                    for(int fordim=0; fordim<dim.length; fordim++)
                    {
                        comp++;
                        if(obj.get(win[i] -1)[dim[fordim]] < obj.get(tempfile.get(j)-1)[dim[fordim]])
                        {
                            enter++;   
                        }
                        else if(obj.get(win[i] -1)[dim[fordim]] > obj.get(tempfile.get(j)-1)[dim[fordim]])
                            re_enter++;
                    }
                    if(enter==dim.length) // if window element dominate temp element
                        tempfile.set(j, -1);
                    
                    else if(re_enter==dim.length) //if temp element dominate window element
                    {
                        win[i]=0;
                        break;
                    }
                    
                }
                if(win[i]!=0) // if do not dominate each other
                {
                    skyline.add(win[i]);
                    win[i]=0;
                }
            }
            
            enter=0;
            re_enter=0;
            
            for(int i=0; i<tempfile.size(); i++) //compare temp elements to the remaining temp elements
            {
                if(tempfile.get(i) == -1)
                    continue;
                for(int j=0; j<tempfile.size(); j++)
                {
                    if(tempfile.get(j) == -1 || tempfile.get(i) == tempfile.get(j))
                        continue;
                    enter=0;
                    re_enter=0;
                    for(int fordim=0; fordim<dim.length; fordim++)
                    {
                        comp++;
                        if(obj.get(tempfile.get(i)-1)[dim[fordim]] < obj.get(tempfile.get(j)-1)[dim[fordim]])
                            enter++;
                        
                        else if(obj.get(tempfile.get(i)-1)[dim[fordim]] > obj.get(tempfile.get(j)-1)[dim[fordim]])
                            re_enter++;
                    }
                    if(enter==dim.length)
                        tempfile.set(j, -1);
                    
                    else if(re_enter==dim.length)
                    {
                        tempfile.set(i, -1);
                        break;
                    }
                    
                }
                if(tempfile.get(i)!= -1)
                    skyline.add(tempfile.get(i));
            }
            
        }

        int count=0;
        long endTime   = System.currentTimeMillis(); //End program time
        long totalTime = endTime - startTime;
        System.out.println("time required is-" + totalTime + "ms");
        System.out.println("Total no of object comparisions are-" + comp);
     //   System.out.println("Skyline objects are-");

        if(skyline.size()!=0) // to print the skylines
        {
            
            Vector v1=new Vector();

            for(int i=0;i<skyline.size();i++)
            {
                String s="";
                s+=skyline.get(i);
             //System.out.println(s);

                if(!v1.contains(s)&&!s.equals("0"))
                    v1.add(s);


            }
    /*        for(int i=0; i<skyline.size(); i++) //printing skyline objects
            {
  
                System.out.print(" " + skyline.get(i) + "\n");
            }*/
            System.out.println("skyline count-->"+v1.size());
        }
        else
        {
            Vector v1=new Vector();

            for(int i=0;i<win.length;i++)
            {
                String s="";
                s+=win[i];
  
                if(!v1.contains(s)&&!s.equals("0"))
                    v1.add(s);


            }
            
    /*        for(int i=0; i<v1.size(); i++) //printing skyline objects
            {
            
                System.out.print(" " + v1.get(i) + "\n");
                
            }*/
            System.out.println("skyline count-->"+v1.size());
        }
  
    }
    }