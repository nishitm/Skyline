

import java.util.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class BNL_Final {

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






    public static void main(String[] args) throws IOException
    {

        long startTime = System.currentTimeMillis(); //Execution time started
        int comp=0;  //To count the no of comparison
        String token; //To read from the file
        
        LineNumberReader  reader = new LineNumberReader(new FileReader(new File("sample_ant.txt"))); //write your input file
        Scanner inFile1 = new Scanner(new File("sample_ant.txt")).useDelimiter("\\s+"); // wrtie your input file
        Scanner inFile2 = new Scanner(new File("query1.txt")).useDelimiter("\\s+"); // write your query file
        List<String> temps = new ArrayList<String>();
        int cnt = 0; //To count the no of lines in the file
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


        int  token1 = 0; //Uses to read the quary file
        
        List<Integer> temp = new ArrayList<Integer>();

        while (inFile2.hasNext()) //for query file
        {
          token1 = Integer.parseInt(inFile2.next());
          temp.add(token1);
        }
        inFile2.close();

        Integer[] tempArray = temp.toArray(new Integer[0]); //Store the elements of query.txt file


        Integer[] dim = Arrays.copyOfRange(tempArray, 0, tempArray.length - 1); //Array uses to store dimension in query file
        Integer winSize = tempArray[tempArray.length - 1]; //Last element of the file denotes the window size


        int[] win = new int[winSize]; //Array for window size
        ArrayList<Integer> tempfile = new ArrayList<Integer>();
 
        for (Integer a : win) //window initialize to ZERO
            win[a]=0;

 
        int wx=0; //pointer for the window size array
        int temp_k=0; //Temporary Variable

        for(int i=0; i<obj.size(); i++) //comparing objects againts the window elements
        {
            if(win[0]==0)
            {
                win[0]=i+1;
                        wx++;
                continue;
            }

            int enter=0;
            int equal=0;
            int write=0;
            for(int j=0; j<win.length; j++)
            {
                   if(win[j]==0)
                        continue;
                enter=0;
                equal=0;
                for(int fordim=0; fordim<dim.length; fordim++)
                {
                    int comparision = Double.compare(obj.get(i)[dim[fordim]], obj.get(win[j] - 1)[dim[fordim]]);

                    if(comparision==0)
                        equal++;

                    comp++;
                    if(obj.get(i)[dim[fordim]] < obj.get(win[j] - 1)[dim[fordim]])
                        enter++;
                }
                if(enter + equal == dim.length) //if new object dominate window element
                {
                                win[j]= i+1;
                                if(write==1)
                                {
                                    win[j]=0;
                                }
                                write=1;
                }
                if(enter ==0) //if window element dominate the new one
                    break;


            }
            if(enter + equal!= dim.length && enter!=0) //do not dominate each other
            {
                    if(wx>=winSize) //if window is full
                    {
                        tempfile.add(i+1);
                    }
                    else //add to the window 
                    {
                        win[wx]=i+1;
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
 
        ArrayList<Integer> skyline = new ArrayList<Integer>();
        int enter=0,rev_enter=0;
        int total=0;
        
        int minIndex;
        if(tempfile.size()!=0) //loop for timestamp
        {
             minIndex = tempfile.indexOf(Collections.min(tempfile));
            for(int i=0; i<winSize; i++)
            {
                if(win[i]<minIndex)
                {
                    skyline.add(win[i]);
                    win[i]=0;
                }
            
            }
        }
        
        int re_enter=0,zero=0;
        if(tempfile.size()!=0) 
        {
            for(int i=0; i <winSize; i++) // checking window elements againts the temp file
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
                        if(obj.get(win[i] -1)[dim[fordim]] <= obj.get(tempfile.get(j)-1)[dim[fordim]])
                        {
                            enter++;   
                        }
                        else 
                            re_enter++;
                    }
                    if(enter==dim.length) //if window element dominate temp element
                        tempfile.set(j, -1);
                    
                    else if(re_enter==dim.length) //if temp element dominate window element
                    {
                        win[i]=0;
                        break;
                    }
                    
                }
                if(win[i]!=0) //do not dominate each other
                {
                    skyline.add(win[i]);
                    win[i]=0;
                }
            }
            
            enter=0;
            re_enter=0;
            
            for(int i=0; i<tempfile.size(); i++) // loop for checking temp file elements againt temp file elements
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
                       
                        else
                            re_enter++;
                    }
                    
                    if(enter==dim.length) //if first temp1 element dominate temp2 element
                        tempfile.set(j, -1);
                    
                    else if(re_enter==dim.length) // if temp2 element dominate temp 1 element
                    {
                        tempfile.set(i, -1);
                        break;
                    }
                    
                }
                if(tempfile.get(i)!= -1)// do not dominate each other
                    skyline.add(tempfile.get(i));
            }
            
        }
        
       
        
        int count=0;
        long endTime   = System.currentTimeMillis(); //End program time
        long totalTime = endTime - startTime;
        System.out.println("time required is-" + totalTime + "ms");
        System.out.println("Total no of object comparisions are-" + comp);
   //     System.out.println("Skyline objects are-");

        if(skyline.size()!=0) //for printing skyline elements
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
   /*         for(int i=0; i<skyline.size(); i++) //printing skyline objects
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
            
 /*           for(int i=0; i<v1.size(); i++) //printing skyline objects
            {
            
                System.out.print(" " + v1.get(i) + "\n");
                
            }*/
            System.out.println("skyline count-->"+v1.size());
        }

    }
    }
