import java.io.*;
import java.util.*;

class Node
{
	protected int id;
	protected int M ;
	protected int m ;
	protected Vector<double[]> dim=new Vector<double[]>();
	protected Vector<Node> child=new Vector<Node>();
	protected Node parent;
	protected double area;
	protected int noOfDim;
	
	Node()
	{
		Vector<double[]> dimen=new Vector<double[]>();

		
		this.M = Rtree.NO_OF_KEYS;
		this.m = M/2;
		
		this.dim=dimen;
	//	this.noOfDim=totalDims;
	}
	
	Node(double arr[])
	{
		this.id=(int) arr[0];
		
		Vector<double[]> dimen=new Vector<double[]>();

		for(int i=1;i<arr.length;i++)
		{
			double b[]=new double[2];
			dimen.add(b);
			dimen.get(i-1)[0]=arr[i];
			dimen.get(i-1)[1]=arr[i];
		//	this.noOfDim=totalDims;
			
		}

		
		
		this.M = Rtree.NO_OF_KEYS;
		this.m = M/2;
		this.parent=null;
		this.dim=dimen;
	
		
	}
	
	
}



public class Rtree{

	public static Node root=new Node();
	protected static int Count_id=0;
	public final static int NO_OF_KEYS = 30;
	public static void main(String args[]) throws IOException
	{
		
		String FNAME = "sample_ind.txt";
		String QNAME = "query2.txt";
		
		Vector v=new Vector();//it contains every row of data
		Scanner inFile1 = new Scanner(new File(FNAME)).useDelimiter("\\s+"); // write your input file
		Scanner inFile2 = new Scanner(new File(QNAME)).useDelimiter("\\s+");
		
		
		//reading data from input files 
		FileInputStream f=new FileInputStream(FNAME);
		LineNumberReader  reader = new LineNumberReader(new FileReader(new File(FNAME)));
		String lineRead = "";
		int cnt=0;
        while ((lineRead = reader.readLine()) != null) {}

        cnt = reader.getLineNumber();
        reader.close();
		
		
	//	FileInputStream query=new FileInputStream("sample_query.txt");
        int c;
		
		 String str="";   
	
		 while (true) 
		{
			c = f.read();		
			if(c == -1)
			{
				v.add(str);
				str="";
				break;	
			}
			if(c=='\n')
			{
				v.add(str);
				str="";
				
			}
			else
			{
			str+=(char)c;	
			}
		}
		
		String token;
		List<String> temps = new ArrayList<String>();
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
        
        List<Integer> temp_q = new ArrayList<Integer>();

        while (inFile2.hasNext()) //for query file
        {
          token1 = Integer.parseInt(inFile2.next());
          temp_q.add(token1);
        }
        inFile2.close();

        
		Integer[] tempArray = temp_q.toArray(new Integer[0]); //Array uses to store dimension in query file
	       
        final Integer[] dim = Arrays.copyOfRange(tempArray, 0, tempArray.length - 3); //Array uses to store dimension in query file
		
		 
		
			String strs ="";
			strs+=v.get(0);
			strs=strs.replaceAll("\\s+", " ");
			
			StringTokenizer stoken=new StringTokenizer(strs," ");
			int length=stoken.countTokens();
			
			//2-D array that stores all the input records 
		double input_record[][]=new double[v.size()][length];int NO_OF_KEY;
		
		
		//this loop takes every string object from 'v' Vector and stores the values in array 'input_record'
		for(int i=0;i<v.size();i++)
		{
			String s ="";
			s+=v.get(i);
			s=s.replaceAll("\\s+", " ");
			if(i!=v.size()-1)
			 s = s.substring(0, s.length());
		
			
			StringTokenizer st1=new StringTokenizer(s," ");
			int j=0;
			
			
			while(st1.hasMoreTokens())
			{
				
				String temp=st1.nextToken();
				temp=temp.replaceAll("\\s","");
				
					input_record[i][j]=Double.parseDouble(temp);
								j++;
			}
			
			
		}
	
		Integer Key_size = tempArray[tempArray.length - 1]; //Last element of the file denotes the Key size
        Integer Pointer_size = tempArray[tempArray.length - 2];
        Integer Disk_size = tempArray[tempArray.length -3];
		NO_OF_KEY = (int) (Disk_size/(2*dim.length*(Key_size + Pointer_size)));

	
		
		root.parent=null;
		root.id=0;
		Count_id=1;
		Count_id=Count_id+input_record.length;
		//System.out.println(Count_id);
		for(int i=0;i<input_record.length;i++)
		{
			
			Node node=new Node(input_record[i]);
		//	System.out.println("node id: "+node.id);
			insert(node);
			
		}
		
		print(root);
		BNB(root,dim,obj);

	}

	
	static  void insert(Node node)
	{
		Node leaf=FindLeaf(node);
	//	System.out.println("leaf id:"+leaf.id);
		
		if(leaf.child.size()==leaf.M)
		{
		//	System.out.println("check-M hit");
			
	//		System.out.println("leaf full- split required");
		//	Vector <Node>groups=new Vector<Node>();
			
			split(leaf,node);
			
				
	//		adjust(groups);
		}
		else
		{	
		//	System.out.println("check-M not hit");
				
			leaf.child.add(node);
		//	System.out.println("child added:"+leaf.child.size());
			setDimensions(leaf);
			node.parent=leaf;
		}
		
	//	System.out.println("check-instert-adjust-count");
			adjustTree(leaf);
			
	}
	

	
	
	static Node FindLeaf(Node n)
	{
		Node node=root;
		
		if(node.child.isEmpty())
			return node;
		
		else if(node.child.get(0).child.isEmpty())
			return node;
		
		else
		{
			//find the leaf node with least enlargement area
			while(true)
			{
				Node temp=node;
				
				Node temp2=temp.child.get(0);
				temp2.child.add(n);
				setDimensions(temp2);
			
				double minval=Area(temp2)-Area(temp.child.get(0))-Area(n);
				temp2.child.removeElement(n);
				setDimensions(temp2);
				
				int m=0;
				for(int i=1;i<temp.child.size();i++)
				{	
					temp2=temp.child.get(i);
					temp2.child.add(n);setDimensions(temp2);
			
					
					double newArea=Area(temp2)-Area(temp.child.get(i))-Area(n);
					
					temp2.child.removeElement(n);
					setDimensions(temp2);
				
					
					if(newArea<minval)
					{
						minval=newArea;
						m=i;
					}
				}
				if(temp.child.get(m).child.get(0).child.isEmpty())//if its input_record leaf
				{
					return temp.child.get(m);
				}
				else
				node=temp.child.get(m);	
				
			}
			
		}
		
		
	}
	
	

	static void split(Node leaf,Node node)
	{
		leaf.child.add(node);
	
		Node temp=leaf;
		Node par=leaf.parent;
		
			//choose largest ones
			int max1=0,max2=1;
			double maxval=0;
			
		
			for(int i=0;i<leaf.child.size();i++)
			{
				for(int j=0;j<leaf.child.size();j++)
				{
					if(i!=j)
					{
						Node temp2=new Node();
						temp2.child.add(leaf.child.get(i));
						setDimensions(temp2);
						
						temp2.child.add(leaf.child.get(j));
						setDimensions(temp2);
						
						double d=Area(temp2)-(Area(leaf.child.get(i))+Area(leaf.child.get(j)));
						
						if(d>maxval)
							{
								maxval=d;
								max1=i;max2=j;
							}
						
					}
					
				}
			}
		
			Node group1=new Node();
			
			group1.parent=par;
			group1.id=Count_id;
			
			Count_id++;
			
			
			group1.child.add(leaf.child.get(max1));setDimensions(group1);
			group1.child.get(0).parent=group1;
					
			Node group2=new Node();group2.parent=par;group2.id=Count_id;
			
			Count_id++;
			group2.child.add(leaf.child.get(max2));
			
			setDimensions(group2);
			group2.child.get(0).parent=group2;
			
			
			temp.child.removeElementAt(max1);
			temp.child.removeElementAt(max2-1);
	//		
		while(true)
		{
			if(temp.child.size()==0)
				break;
			
			//if less than m entries then add  temp to that  group
			if(temp.child.size()<=(group1.m-group1.child.size()))
			{
				for(int i=0;i<temp.child.size();i++)
					{
						temp.child.get(i).parent=group1;
					
						group1.child.add(temp.child.get(i));
						}
				temp.child.removeAllElements();
				break;
			}
			else if(temp.child.size()<=(group2.m-group2.child.size()))
			{
				for(int i=0;i<temp.child.size();i++)
					{
					temp.child.get(i).parent=group2;
					
					group2.child.add(temp.child.get(0));
					
					}
				temp.child.removeAllElements();
				break;
			}
			
			Node next=pickNext(group1,group2,temp);
		
			//find the  group with leastarea
			
			double area1=Area(group1);
		
			Node temp1=group1;
			temp1.child.addElement(next);	
			setDimensions(temp1);
			double extra1=Area(temp1)-area1;
			temp1.child.removeElement(next);
			setDimensions(temp1);
			
			
			double area2=Area(group2);
			Node temp2=group2;
			
			temp2.child.addElement(next);setDimensions(temp2);
			double extra2=Area(temp2)-area2;
			temp2.child.removeElement(next);
			setDimensions(temp2);
			if(extra1<extra2)
				{
				group1.child.add(next);setDimensions(group1); 
				next.parent=group1;
				
				}
			else if(extra1>extra2)
				{
				group2.child.add(next);
				setDimensions(group1);
				next.parent=group2;
				}
			else if(extra1==extra2)
				{	
					if(area1<area2)
						{
						group1.child.add(next);
						setDimensions(group1); 
						next.parent=group1;
						}
					else if(area1>area2)
						{
						group2.child.add(next);
						setDimensions(group1);
						next.parent=group2;
						}
					else if(area1==area2)
					{
						if(group1.child.size()<group2.child.size())
							{
							group1.child.add(next);
							setDimensions(group1);
							next.parent=group1;
							}
						else
							{
							group2.child.add(next);
							setDimensions(group1); 
							next.parent=group2;
							}
							
					}
				}
			temp.child.removeElement(next);
			
		}	
	
		setDimensions(group1);
		setDimensions(group2);
		
		//adjust tree here
		
		
		if(group1.parent==null)
		{	
			
			Node new_node=new Node();
			new_node.parent=null;
			
			new_node.child.add(group1);
			setDimensions(new_node);
			new_node.child.add(group2);
			setDimensions(new_node);
			
			group1.parent=new_node;
			group2.parent=new_node;
			
			setDimensions(new_node);
			root=new_node;
			
		}
		else
		{
			leaf.parent.child.removeElement(leaf);
			leaf.parent=null;
			
			group1.parent.child.addElement(group1);
			setDimensions(group1.parent);
			if(group1.parent.child.size()<group1.parent.M)
			{
				group1.parent.child.add(group2);
				setDimensions(group1.parent);
				
			}
			
			else
			{	
				split(group1.parent,group2);
				
			}
		}
		
	}
		
		
	static void adjustTree(Node n)
	{
		
		Node temp=n;	  
		while(true)
		  {
			  
				if(temp.parent==null)
					break;
			 	
			  	setDimensions(temp);
			
			  	temp=temp.parent;
		
		  }
			
	}
	
	
	static Node pickNext(Node g1, Node g2, Node list)
	{
		double maxval=0;
		int M=0;
		for(int i=1;i<list.child.size();i++)
		{
			double gArea1=Area(g1);
			
			g1.child.add(list.child.get(i));
			
			double d1=Area(g1)-gArea1;
			g1.child.removeElement(list.child.get(i));
			double gArea2=Area(g2);
			
			g2.child.add(list.child.get(i));
			double d2=Area(g1)-gArea2;
			g2.child.removeElement(list.child.get(i));
				
			double d=Math.abs(d2-d1);
			
			if(d>maxval)
			{
				maxval=d;
				M=i;
			}
		}
		
		return list.child.get(M);
	}
	
	
	
	static void setDimensions(Node n1)
	{
		
		if(n1.child.size()==1)
		{
		
			for(int i=0;i<n1.child.get(0).dim.size();i++)
			{
				double b[]=new double[2];
				n1.dim.add(b);
				n1.dim.get(i)[0]=n1.child.get(0).dim.get(i)[0];
				n1.dim.get(i)[1]=n1.child.get(0).dim.get(i)[1];
			}
		}
		
		else
		{
			
		
		int t=n1.dim.size();
			for(int i=0;i<2;i++)
				{
				
					double m=n1.child.get(0).dim.get(i)[0];
					double M=n1.child.get(0).dim.get(i)[1];
					for(int j=1;j<n1.child.size();j++)
					{
						if(m>n1.child.get(j).dim.get(i)[0])
						{	
							m=n1.child.get(j).dim.get(i)[0];
						}	
						if(M<n1.child.get(j).dim.get(i)[1])
						{	
							M=n1.child.get(j).dim.get(i)[1];
						}
					}
					n1.dim.get(i)[0]=m;
					n1.dim.get(i)[1]=M;
						
				}
				
		 }	

	}
	
	
	static double Area(Node n1)
	{
		double area=1;

		
		for(int i=0;i<n1.dim.size();i++)
		{
			
			double temp=n1.dim.get(i)[1]-n1.dim.get(i)[0];
		
			if(temp!=0)
			area=area*(n1.dim.get(i)[1]-n1.dim.get(i)[0]);
		}
		
		return area;
		
	}
	
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
	
	static void BNB(Node n, Integer[] dimn, List<Double[]> obj) throws IOException
	{
		Node temp = n;
		int winSize=1000;
		int[] win = new int[winSize];
		int c=0;
		int count1=0;
		long startTime = System.currentTimeMillis(); //Execution time started
		if(temp.child.size() == 0)
		{
			win[0] = temp.id;
			for(int i=0;i<temp.child.size();i++)
			{
				count1=0;
				for(int j=0;j<temp.dim.size();j++)
				{
					if(temp.dim.get(i)[j]>temp.dim.get(win[0])[j])
					{
						count1++;
					}
					
				}
				if(count1 == dimn.length)
					win[c]=i;
				else if(c>0)
				{
					c++;
					win[c]=i;
				}
			}
		}
		else
		{
			int comp=0;
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
	                for(int fordim=0; fordim<dimn.length; fordim++)
	                {
	                    int comparision = Double.compare(obj.get(i)[dimn[fordim]], obj.get(win[j] - 1)[dimn[fordim]]);

	                    if(comparision==0)
	                        equal++;

	                    comp++;
	                    if(obj.get(i)[dimn[fordim]] < obj.get(win[j] - 1)[dimn[fordim]])
	                        enter++;
	                }
	                if(enter + equal == dimn.length) //if new object dominate window element
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
	            if(enter + equal!= dimn.length && enter!=0) //do not dominate each other
	            {
	                    
	                        win[wx]=i+1;
	                        wx++;
	                    
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
			comp = comp/2;
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
	        
	       
	        int count=0;
	        long endTime   = System.currentTimeMillis(); //End program time
	        long totalTime = endTime - startTime;
	        System.out.println("time required is-" + totalTime + "ms");
	        System.out.println("Total no of object comparisions are-" + comp);

	        if(skyline.size()!=0) //for printing skyline elements
	        {
	            
	            Vector v1=new Vector();

	            for(int i=0;i<skyline.size();i++)
	            {
	                String s="";
	                s+=skyline.get(i);
	             
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
		
	
	
	

	
	static void print(Node n)
	{
		
		Node temp=n;
		 
		
		if(temp.child.size()!=0)
		{	
			
			for(int i=0;i<temp.child.size();i++)
			{
				print(temp.child.get(i));
			
			}
		}
		else
		{	
			System.out.println("Node:"+temp.id);
			for(int j=0;j<temp.dim.size();j++)
			{
				System.out.println("leaf dimensions:"+temp.dim.get(j)[0]);
				
			}
			
			
		}
		
		
	}
}



