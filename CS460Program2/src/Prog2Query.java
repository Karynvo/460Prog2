import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Prog2Query {

	public static void main(String[] args) {
		
		File hashFile = new File("hashBuckets.bin");
		File databaseFile = new File ("spm-records.bin");
		RandomAccessFile hashBucketStream = null;
		RandomAccessFile databaseStream = null;
		
		try {
			hashBucketStream = new RandomAccessFile(hashFile, "rw");
		} catch (FileNotFoundException e) {
			System.out.println("Could not set up data stream to \"hashBuckets.bin\"!");
			e.printStackTrace();
		}
		
		try {
			databaseStream = new RandomAccessFile(databaseFile, "rw");
		} catch (FileNotFoundException e) {
			System.out.println("Could not set up data stream to \"spm-records.bin\"!");
			e.printStackTrace();
		}
		
		int[] directory = null;
		int startDirPtr = 0;
		int sizeOfDirectory = 0;
		
		try{
			
			hashBucketStream.seek(0);
			int token = 0;
			int intCount = 0;
			while (hashBucketStream.getFilePointer()!=hashBucketStream.length()){
				token=hashBucketStream.readInt();
	//			System.out.println(Integer.toString(token));
				intCount++;
			}
		//	System.out.println("intCount = " + intCount);
			//
		//	System.out.println("stream length is: " + hashBucketStream.length());
			hashBucketStream.seek((hashBucketStream.length() - 4)); // 4 is the size of an int
			startDirPtr = hashBucketStream.readInt();
		//	System.out.println("startDirPtr: " + startDirPtr);
			hashBucketStream.seek(startDirPtr);
			sizeOfDirectory = (int) (hashBucketStream.length() - 4 - startDirPtr);
			sizeOfDirectory = sizeOfDirectory / 4;
			directory = new int[sizeOfDirectory];
			for(int i = 0; i < sizeOfDirectory; i++){
					directory[i] = hashBucketStream.readInt();
			}
			
			
		} catch (IOException e) {
			System.out.println("I/O Error while collecting index information from "
					+ "the hash bucket binary file data stream (\"hashBuckets.bin\")");
			e.printStackTrace();
		}
		
		//Calculate globalDepth after streams set up
		int globalDepth = 0;
		int depthFinder = directory.length;
		while (depthFinder != 1){
			depthFinder = depthFinder/10;
			globalDepth++;
		}

		Scanner keyboard = new Scanner(System.in);
		String input = "";
		while (input.compareTo("q")!=0){
			System.out.print("Enter an SPM value prefix ('q' to quit): ");
			input = keyboard.nextLine();
			if (input.compareTo("q")==0){
				break;
			}
			
			//input validation
			while (input.length() > 8 || input.length() <= 0){
				System.out.print("Invalid input! Enter an SPM value prefix ('q' to quit): ");
				input = keyboard.nextLine();
			}
			
			int query = Integer.parseInt(input);
			int endIndex = query;
			
			// Two cases
			// 1) User input is shorter than global depth. Then simply return everything
			//		in the buckets for everything between the query and query99999 etc...
			// 2) User input is the size of the global depth or greater. Then, retrieve
			// 		that bucket, and return the values in that bucket that are between
			//		the query and query99999 etc....
			if (input.length() < globalDepth){
				if (query == 0){
					int factor  = 7 - input.length();
					if (factor < 0){
						endIndex = 0;
					}else{
						endIndex = 9;
						for (int j = 0; j < factor ; j++){
							endIndex = endIndex * 10 + 9;
						}
					}
				}else{
					if  (query/10000000 == 0){
						query = query * 10;
						endIndex = query+9;
					}
					while  (query/10000000 == 0){
						query = query * 10;
						endIndex = endIndex*10 + 9;
					}
				}
	
				Bucket currBucket = new Bucket();
				SpmDataRecord currRecord= new SpmDataRecord();
				
//				System.out.println("globalDepth: " + globalDepth);
				
				for (int x = 0; x < 8-globalDepth ; x++){
					query = query / 10;
					endIndex = endIndex / 10;
				}
				
//				System.out.println("Query extends from indices " + Integer.toString(query)
//				+ " to " + endIndex);
				
				ArrayList<Integer> bucketPointers = new ArrayList<>();
				ArrayList<Integer> indices = new ArrayList<>();
				for (int i = query; i <= endIndex; i++){
					if (!bucketPointers.contains(directory[i])){
						bucketPointers.add(directory[i]);
						indices.add(i);
					}
				}
				
//				for (int i : indices){
//					System.out.println("Index: " + i);
//				}
				
				int resultCount = 0;
				int currIdx = 0;
				for (int pointer : bucketPointers){
	//				System.out.println("Bucket #" + currIdx);
					currIdx++;
					//grab the bucket at that pointer
					currBucket.readBucket(hashBucketStream, pointer);
//					currBucket.toString();
					
					for (Entry currEntry : currBucket.slotsArray){	
						if (currEntry.pointer == -1){
							continue;
						}
						
						try {
							databaseStream.seek(currEntry.pointer);
							currRecord.fetchObject(databaseStream);
							System.out.println(currRecord.toString());
							resultCount++;
						} catch (IOException e) {
							System.out.println("Oops, having trouble reading from the "
									+ "database file while retrieving records for this query!");
							e.printStackTrace();
						}
					}
				}
				
				System.out.println("Returned " + resultCount + " results.");
			}
			else/* input.length() is greater than or equal to globalDepth*/{
//				System.out.println("Input length greater than globalDepth. To be implemented...");
				
				// In this case, we are looking for a single bucket, and then retrieving
				// the records within that span
				for (int i = 0; i < input.length() - globalDepth; i++){
					query = query / 10;
				}
				
//				System.out.println("Searching for bucket at directory[" + query + "]");
//				System.out.println("ptr points to: " + directory[query]);
				
				Bucket foundBucket = new Bucket();
				foundBucket.readBucket(hashBucketStream, directory[query]);
				
				int inputVal = Integer.parseInt(input);
				int endVal = 0;
				while(inputVal < 10000000){
					inputVal = inputVal * 10;
					endVal = (endVal*10) + 9;
				}
				
				endVal = inputVal + endVal;
				
//				System.out.println("inputVal is: " + inputVal);
//				System.out.println("endVal is: " + endVal);
				
				// Arraylist for easy sorting
				ArrayList<Entry> entries = new ArrayList<>();
				for (Entry currEntry : foundBucket.slotsArray){
					if (currEntry.key != -1){
						entries.add(currEntry);
					}
				}
				Collections.sort(entries);
				
				int entryCount = 0;
				SpmDataRecord currRecord = new SpmDataRecord();
				for(Entry e : entries){
					try {
//						System.out.println("Key is: " + e.key);
						if(e.key <= endVal && e.key >= inputVal){
							databaseStream.seek(e.pointer);
							currRecord.fetchObject(databaseStream);
							System.out.println(currRecord.toString());
							entryCount++;
						}
					} catch (IOException e1) {
						System.out.println("Oops, having trouble reading from the "
								+ "database file while retrieving records for this query!");
						e1.printStackTrace();
					}
				}
				
				if (entryCount == 0){
					System.out.println("No records were found for that query.");
				} else {
					System.out.println(entryCount +" records found");
				}
				
			}
			
			
			
			
		}
		System.out.println("Exiting...");
		
//		System.out.println("Finding bucket 0...");
//		Bucket zeroBucket = new Bucket(hashBucketStream, 0);
//		zeroBucket.toString();
		
		//Close streams here
		keyboard.close();
		try {
			hashBucketStream.close();
		} catch (IOException e) {
			System.out.println("Could not close stream to hashbucket.bin!!!");
			e.printStackTrace();
		}
		
	}

}
