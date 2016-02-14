import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			databaseStream = new RandomAccessFile(databaseFile, "rw");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int[] directory = null;
		int startDirPtr = 0;
		int sizeOfDirectory = 0;
		
		try{
			
			//
			
			hashBucketStream.seek(0);
			int token = 0;
			int intCount = 0;
			while (hashBucketStream.getFilePointer()!=hashBucketStream.length()){
				token=hashBucketStream.readInt();
				System.out.println(Integer.toString(token));
				intCount++;
			}
			System.out.println("intCount = " + intCount);
			//
			System.out.println("stream length is: " + hashBucketStream.length());
			hashBucketStream.seek((hashBucketStream.length() - 4)); // 4 is the size of an int
			startDirPtr = hashBucketStream.readInt();
			System.out.println("startDirPtr: " + startDirPtr);
			hashBucketStream.seek(startDirPtr);
			sizeOfDirectory = (int) (hashBucketStream.length() - 4 - startDirPtr);
			sizeOfDirectory = sizeOfDirectory / 4;
			directory = new int[sizeOfDirectory];
			for(int i = 0; i < sizeOfDirectory; i++){
					directory[i] = hashBucketStream.readInt();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Scanner keyboard = new Scanner(System.in);
		String input = "";
		while (input.compareTo("q")!=0){
			System.out.print("Enter an SPM value prefix ('q' to quit): ");
			input = keyboard.nextLine();
			if (input.compareTo("q")==0){
				break;
			}
			while (input.length() > 8 || input.length() <= 0){
				System.out.print("Invalid input! Enter an SPM value prefix ('q' to quit): ");
				input = keyboard.nextLine();
			}
			
			int query = Integer.parseInt(input);
			int endIndex = query;
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
			
			int globalDepth = 0;
			int depthFinder = directory.length;
			while (depthFinder != 1){
				depthFinder = depthFinder/10;
				globalDepth++;
			}
			System.out.println("globalDepth: " + globalDepth);
			
			for (int x = 0; x < 8-globalDepth ; x++){
				query = query / 10;
				endIndex = endIndex / 10;
			}
			
			System.out.println("Query extends from indices " + Integer.toString(query)
			+ " to " + endIndex);
			
			for (int i = query; i <= endIndex; i++){
				currBucket.readBucket(hashBucketStream, directory[i]);
				for (Entry currEntry : currBucket.slotsArray){
					
					if (currEntry.pointer == -1){
						System.out.println("No matching records found :(");
						continue;
					}
					
					try {
						databaseStream.seek(currEntry.pointer);
						currRecord.fetchObject(databaseStream);
						System.out.println(currRecord.toString());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			
		}
		System.out.println("Exiting...");
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
