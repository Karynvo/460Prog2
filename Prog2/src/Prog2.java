import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/*+----------------------------------------------------------------------
||
||  Class Record
||
||         Author:  Karyn Vo
||
||        Purpose:  Record represents a single record of astronomical
||                  data. In an input data file, a record would be one
||					line with 127 data fields.
||
||  Inherits From:  None
||
||     Interfaces:  Record implements Comparable to be used for sorting.
||                  As records are created, they are added to a list and
||					then sorted.
||
|+-----------------------------------------------------------------------
||
||      Constants:  recordNumber gives the number of the record when added
||					to the ArrayList when the data file is first loaded in.
||					It's primary function is to provide the record number
||					being excluded when an error with the record is
||					encountered.
||
|+-----------------------------------------------------------------------
||
||   Constructors:  None.
||
||  Class Methods:  None
||                  
||
||  Inst. Methods:  boolean createRecord(String, int), 
||					void printErrorMsg(String), int getSpm(), 
||					void setSpm(String), void setRah(String), 
||					void setRam(String), void setRas(String), 
||					void setDe_(String), void setDed(String), 
||					void setDem(String), void setDes(String), 
||					void setRadeg(String), void setDedeg(String), 
||					void setE_radeg(String), void setE_dedeg(String), 
||					void setPmra(String), void setPmde(String), 
||					void setE_pmra(String), void setE_pmde(String), 
||					void setVmag(String), void setBmag(String), 
||					void setB_v(String), void setE_vmag(String), 
||					void setE_bmag(String), void setNfie(String), 
||					void setO_bmag(String), void setO_vmag(String), 
||					void setF1(String), void setF2(String), 
||					void setF3(String)
||
++-----------------------------------------------------------------------*/
class Record implements Comparable<Record>{
	public static final int RECORD_LENGTH = 100; // 12(4 byte int) + 11(4 byte float) + 4(2 byte char)
												// 48 + 44 + 8 = 100 bytes
	
	private int recordNumber = 0;
	private boolean isBadRecord = false;

	// Each column gets its own variable
	private int spm; // bytes 1-8
	private int rah; // bytes 10-11
	private int ram; // bytes 13-14
	private float ras; // bytes 16-21
	private char de_; // byte 23
	private int ded; // bytes 24-25
	private int dem; // bytes 27-28
	private float des; // bytes 30-34
	private float radeg; // bytes 36-45
	private float dedeg; // bytes 47-56
	private int e_radeg; // bytes 57-60
	private int e_dedeg; // bytes 61-64
	private float pmra; // bytes 66-72
	private float pmde; // bytes 74-80
	private float e_pmra; // bytes 81-85
	private float e_pmde; // bytes 86-90
	private float vmag; // bytes 92-96
	private float bmag; // bytes 98-102
	private float b_v; // bytes 104-108
	private int e_vmag; // bytes 109-111
	private int e_bmag; // bytes 112-114
	private int nfie; // byte 118
	private int o_bmag; // bytes 120-121
	private int o_vmag; // bytes 123-124
	private char f1; // byte 126
	private char f2; // byte 127
	private char f3; // byte 128
	
    /*---------------------------------------------------------------------
    |  Method createRecord
    |
    |  Purpose:  Populates each record field (variable with relevant information 
    |		based on the line of information given. It also checks to make
    |		the input line aligns with the data specifications.
    |
    |  Pre-condition:  The input line must be 128 bytes with each variable
   	|		specified within the range specified next to the variable
   	|		declarations. 
    |
    |  Post-condition: If there was malformed data, the method will
    |		terminate in the midst of creation. If there wasn't, all
    |		fields are expected to be populated with the correct
    |		information and type.
    |
    |  Parameters:
    |      line -- the data of one line (record) of 128 bytes with variables
    |				ranges specified under "spmcat.dat"
    |				http://www.cs.arizona.edu/classes/cs460/spring16/spmcat.readme,
    |				also listed above in variable declaration
    |	   recordNumber -- the number of the record that has been read in.
    |				This is used for printing error messages.
    |
    |  Returns:  a boolean. Tells whether or not record was successfully
    |			created without malformed data.
    *-------------------------------------------------------------------*/
	public boolean createRecord(String line, int recordNumber){
		this.recordNumber = recordNumber; // store record number
		setSpm(line.substring(0,8).trim());
		if(isBadRecord) return false;
		setRah(line.substring(9,11).trim());
		if(isBadRecord) return false;
		setRam(line.substring(12,14).trim());
		if(isBadRecord) return false;
		setRas(line.substring(15,21).trim());
		if(isBadRecord) return false;
		setDe_(line.substring(22,23).trim());
		if(isBadRecord) return false;
		setDed(line.substring(23,25).trim());
		if(isBadRecord) return false;
		setDem(line.substring(26,28).trim());
		if(isBadRecord) return false;
		setDes(line.substring(29,34).trim());
		if(isBadRecord) return false;
		setRadeg(line.substring(35,45).trim());
		if(isBadRecord) return false;
		setDedeg(line.substring(46,56).trim());
		if(isBadRecord) return false;
		setE_radeg(line.substring(56,60).trim());
		if(isBadRecord) return false;
		setE_dedeg(line.substring(60,64).trim());
		if(isBadRecord) return false;
		setPmra(line.substring(65,72).trim());
		if(isBadRecord) return false;
		setPmde(line.substring(73,80).trim());
		if(isBadRecord) return false;
		setE_pmra(line.substring(80,85).trim());
		if(isBadRecord) return false;
		setE_pmde(line.substring(85,90).trim());
		if(isBadRecord) return false;
		setVmag(line.substring(91,96).trim());
		if(isBadRecord) return false;
		setBmag(line.substring(97,102).trim());
		if(isBadRecord) return false;
		setB_v(line.substring(103,108).trim());
		if(isBadRecord) return false;
		setE_vmag(line.substring(108,111).trim());
		if(isBadRecord) return false;
		setE_bmag(line.substring(111,114).trim());
		if(isBadRecord) return false;
		setNfie(line.substring(117,118).trim());
		if(isBadRecord) return false;
		setO_bmag(line.substring(119,121).trim());
		if(isBadRecord) return false;
		setO_vmag(line.substring(122,124).trim());
		if(isBadRecord) return false;
		setF1(line.substring(125,126));
		if(isBadRecord) return false;
		setF2(line.substring(126,127));
		if(isBadRecord) return false;
		setF3(line.substring(127,128));
		if(isBadRecord) return false;
		return true;
	}
	
    /*---------------------------------------------------------------------
    |  Method printErrorMsg
    |
    |  Purpose:  Prints an error message in the event of malformed data.
    |			It is called by setters and encapsulates the function of
    |			the error message. This also sets the isBadRecord variable
    |			to true.
    |
    |  Pre-condition:  Data must encounter an error from a try-catch
    |				for this message to be triggered.
    |
    |  Post-condition: This record was not created because of the
    |				malformed data.
    |
    |  Parameters:
    |      reason -- The reason for termination/the error.
    |
    |  Returns:  None.
    *-------------------------------------------------------------------*/
	public void printErrorMsg(String reason){
		System.out.println("Record #" + recordNumber + " excluded: " + reason);
		isBadRecord = true;
	}
	
    /*---------------------------------------------------------------------
    |  Method getSpm
    |
    |  Purpose:  The spm is the object identifier for this program.
    |			Getting this field is important for making comparisons.
    |
    |  Pre-condition:  The record has been previously created for the
    |			spm variable to have been populated.
    |
    |  Post-condition: The variable spm will be the same.
    |
    |  Parameters: None.
    |
    |  Returns:  The variable spm -- the object identifier.
    *-------------------------------------------------------------------*/
	public int getSpm() {
		return spm;
	}
	
    /*---------------------------------------------------------------------
    |  Methods setSpm, setRah, setRam, setRas, setDe_, setDed, setDem, 
    |	setDes, setRadeg, setDedeg, setE_radeg, setE_dedeg, setPmra, setPmde,
    |	setE_pmra, setE_pmde, setVmag, setBmag, setB_v, setE_vmag, setE_bmag,
    |	setNfie, setO_bmag, setO_vmag, setF1, setF2, setF3
    |
    |  Purpose:  Sets the value for the variable based on the string input.
    |			Catches any unexpected values.
    |
    |  Pre-condition:  None.
    |
    |  Post-condition: The value passed in will be stored as a variable
    |					within the Record.
    |
    |  Parameters:
    |      subStr -- String containing the data that will be relevant to
    |				the corresponding variable. The string will be parsed
    |				to the type of the variable, int, float, or char.
    |
    |  Returns:  None.
    *-------------------------------------------------------------------*/
	public void setSpm(String subStr) {
		try{
			spm = Integer.parseInt(subStr);
		}catch(NumberFormatException e){
			printErrorMsg("SPM field incorrect format");
		}
	}
	
	public void setRah(String subStr) {
		try{
			rah = Integer.parseInt(subStr);
		}catch(NumberFormatException e){
			printErrorMsg("RAh field incorrect format");
		}
	}
	
	public void setRam(String subStr) {
		try{
			ram = Integer.parseInt(subStr);
		}catch(NumberFormatException e){
			printErrorMsg("RAm field incorrect format");
		}
	}
	
	public void setRas(String subStr) {
		try{
			ras = Float.parseFloat(subStr);
		}catch(NumberFormatException e){
			printErrorMsg("RAs field incorrect format");
		}
	}
	
	public void setDe_(String subStr) {
		if(subStr.equals("+") || subStr.equals("-"))
			de_ = subStr.charAt(0);
		else if(subStr.equals(" "))
			de_ = ' ';
		else
			printErrorMsg("de- not a + or -");
	}
	
	public void setDed(String subStr) {
		try{
			ded = Integer.parseInt(subStr);
		}catch(NumberFormatException e){
			printErrorMsg("ded field incorrect format");
		}
	}
	
	public void setDem(String subStr) {
		try{
			dem = Integer.parseInt(subStr);
		}catch(NumberFormatException e){
			printErrorMsg("dem field incorrect format");
		}
	}
	
	public void setDes(String subStr) {
		try{
			des = Float.parseFloat(subStr);
		}catch(NumberFormatException e){
			printErrorMsg("des field incorrect format");
		}
	}
	
	public void setRadeg(String subStr) {
		try{
			radeg = Float.parseFloat(subStr);
		}catch(NumberFormatException e){
			printErrorMsg("radeg field incorrect format");
		}
	}
	
	public void setDedeg(String subStr) {
		try{
			dedeg = Float.parseFloat(subStr);
		}catch(NumberFormatException e){
			printErrorMsg("dedeg field incorrect format");
		}
	}
	
	public void setE_radeg(String subStr) {
		try{
			e_radeg = Integer.parseInt(subStr);
		}catch(NumberFormatException e){
			printErrorMsg("e_radeg field incorrect format");
		}
	}
	
	public void setE_dedeg(String subStr) {
		try{
			e_dedeg = Integer.parseInt(subStr);
		}catch(NumberFormatException e){
			printErrorMsg("e_dedeg field incorrect format");
		}
	}
	
	public void setPmra(String subStr) {
		try{
			pmra = Float.parseFloat(subStr);
		}catch(NumberFormatException e){
			printErrorMsg("pmra field incorrect format");
		}
	}
	
	public void setPmde(String subStr) {
		try{
			pmde = Float.parseFloat(subStr);
		}catch(NumberFormatException e){
			printErrorMsg("pmde field incorrect format");
		}
	}
	
	public void setE_pmra(String subStr) {
		try{
			e_pmra = Float.parseFloat(subStr);
		}catch(NumberFormatException e){
			printErrorMsg("e_pmra field incorrect format");
		}
	}
	
	public void setE_pmde(String subStr) {
		try{
			e_pmde = Float.parseFloat(subStr);
		}catch(NumberFormatException e){
			printErrorMsg("e_pmde field incorrect format");
		}
	}
	
	public void setVmag(String subStr) {
		try{
			vmag = Float.parseFloat(subStr);
		}catch(NumberFormatException e){
			printErrorMsg("vmag field incorrect format");
		}
	}
	
	public void setBmag(String subStr) {
		try{
			bmag = Float.parseFloat(subStr);
		}catch(NumberFormatException e){
			printErrorMsg("bmag field incorrect format");
		}
	}
	
	public void setB_v(String subStr) {
		try{
			b_v = Float.parseFloat(subStr);
		}catch(NumberFormatException e){
			printErrorMsg("B-V field incorrect format");
		}
	}
	
	public void setE_vmag(String subStr) {
		try{
			e_vmag = Integer.parseInt(subStr);
		}catch(NumberFormatException e){
			printErrorMsg("e_vmag field incorrect format");
		}
	}
	
	public void setE_bmag(String subStr) {
		try{
			e_bmag = Integer.parseInt(subStr);
		}catch(NumberFormatException e){
			printErrorMsg("e_bmag field incorrect format");
		}
	}
	
	public void setNfie(String subStr) {
		try{
			nfie = Integer.parseInt(subStr);
		}catch(NumberFormatException e){
			printErrorMsg("nfie field incorrect format");
		}
	}
	
	public void setO_bmag(String subStr) {
		try{
			o_bmag = Integer.parseInt(subStr);
		}catch(NumberFormatException e){
			printErrorMsg("o_bmag field incorrect format");
		}
	}
	
	public void setO_vmag(String subStr) {
		try{
			o_vmag= Integer.parseInt(subStr);
		}catch(NumberFormatException e){
			printErrorMsg("o_vmag field incorrect format");
		}
	}
	
	public void setF1(String subStr) {
		if(subStr.equals("H") || subStr.equals("T") || subStr.equals(" "))
			f1 = subStr.charAt(0);
		else
			printErrorMsg("f1 not a H or T");
	}
	
	public void setF2(String subStr) {
		if(subStr.equals("G") || subStr.equals("K") || subStr.equals("Q") || subStr.equals(" "))
			f2 = subStr.charAt(0);
		else
			printErrorMsg("f2 not a G, K, or Q");
	}
	
	public void setF3(String subStr) {
		if(subStr.equals("S") || subStr.equals("A") || subStr.equals(" "))
			f3 = subStr.charAt(0);
		else
			printErrorMsg("f3 not a S or A");
	}
	

    /*---------------------------------------------------------------------
    |  Method compareTo
    |
    |  Purpose:  Comparison of fields uses the "spm" variable. This method
    |			allows for the "spm" variable to be compared across Records.
    |			A smaller "spm" value means the Record is lower.
    |
    |  Pre-condition:  None.
    |
    |  Post-condition: None.
    |
    |  Parameters:
    |      record -- The Record to be compared to using the spm variable.
    |
    |  Returns:  This value tells how the records compare to one another.
	|			-1 -- this is less than record
	|			 0 -- this is equal to record
	|			 1 -- this is greater than record
    *-------------------------------------------------------------------*/
	@Override
	public int compareTo(Record record) {
		if(this.spm < record.spm)
			return -1;
		else if(this.spm == record.spm)
			return 0;
		else
			return 1;
	}
	
    /*---------------------------------------------------------------------
    |  Method fetchRecord
    |
    |  Purpose:  This is modeled after BinaryIO.java's fetchObject method.
    |			Based on the given RandomAccessFile stream, a record's fields
    |			will be populated (all of them).
    |
    |  Pre-condition:  The stream's pointer points to the beginning of a
    |					record.
    |
    |  Post-condition: The record will be populated with the correct data.
    |
    |  Parameters:
    |      stream -- a RandomAccessFile pointing to the beginning of a
    |				record to be stored in a Record object.
    |
    |  Returns:  None.
    *-------------------------------------------------------------------*/
	public void fetchRecord(RandomAccessFile stream){
		try{
			spm = stream.readInt();
			rah = stream.readInt();
			ram = stream.readInt();
			ras = stream.readFloat();
			de_ = stream.readChar();
			ded = stream.readInt();
			dem = stream.readInt();
			des = stream.readFloat();
			radeg = stream.readFloat();
			dedeg = stream.readFloat();
			e_radeg = stream.readInt();
			e_dedeg = stream.readInt();
			pmra = stream.readFloat();
			pmde = stream.readFloat();
			e_pmra = stream.readFloat();
			e_pmde = stream.readFloat();
			vmag = stream.readFloat();
			bmag = stream.readFloat();
			b_v = stream.readFloat();
			e_vmag = stream.readInt();
			e_bmag = stream.readInt();
			nfie = stream.readInt();
			o_bmag = stream.readInt();
			o_vmag = stream.readInt();
			f1 = stream.readChar();
			f2 = stream.readChar();
			f3 = stream.readChar();
		}catch(IOException e){
			System.out.println("I/O ERROR: Couldn't read the file");
			System.exit(-1);
		}
	}
	
    /*---------------------------------------------------------------------
    |  Method dumpRecord
    |
    |  Purpose:  This is modeled after BinaryIO.java's dumpObject
    |			method. The variables of this Record will be written in
    |			binary to a file specified by the input RandomAccessFile.
    |
    |  Pre-condition:  The RandomAccessFile pointer points to where the
    |				data is to be written.
    |
    |  Post-condition: The fields from this record will be written in binary
    |				to the file specified by the RandomAccessFile.
    |
    |  Parameters:
    |      stream -- Tells where to write the contents of the variable.
    |
    |  Returns:  None.
    *-------------------------------------------------------------------*/
	public void dumpRecord(RandomAccessFile stream){
		try{
			stream.writeInt(spm);
			stream.writeInt(rah);
			stream.writeInt(ram);
			stream.writeFloat(ras);
			stream.writeChar(de_);
			stream.writeInt(ded);
			stream.writeInt(dem);
			stream.writeFloat(des);
			stream.writeFloat(radeg);
			stream.writeFloat(dedeg);
			stream.writeInt(e_radeg);
			stream.writeInt(e_dedeg);
			stream.writeFloat(pmra);
			stream.writeFloat(pmde);
			stream.writeFloat(e_pmra);
			stream.writeFloat(e_pmde);
			stream.writeFloat(vmag);
			stream.writeFloat(bmag);
			stream.writeFloat(b_v);
			stream.writeInt(e_vmag);
			stream.writeInt(e_bmag);
			stream.writeInt(nfie);
			stream.writeInt(o_bmag);
			stream.writeInt(o_vmag);
			stream.writeChar(f1);
			stream.writeChar(f2);
			stream.writeChar(f3);
		}catch(IOException e){
			System.out.println("I/O ERROR: Couldn't write to the file;\n\t"
                    + "perhaps the file system is full?");
			System.exit(-1);
		}
	}
	
    /*---------------------------------------------------------------------
    |  Method printRecord
    |
    |  Purpose:  Prints this Record to the console is CSV format. Any
    |			trailing/leading whitespace is trimmed off.
    |
    |  Pre-condition:  None.
    |
    |  Post-condition: The contents of this record will have been
    |				printed to the console.
    |
    |  Parameters: None.
    |
    |  Returns:  None.
    *-------------------------------------------------------------------*/
	public void printRecord(){
		StringBuffer sb = new StringBuffer();
		sb.append(spm).append(",");
		sb.append(rah).append(",");
		sb.append(ram).append(",");
		sb.append(ras).append(",");
		sb.append(de_).append(",");
		sb.append(ded).append(",");
		sb.append(dem).append(",");
		sb.append(des).append(",");
		sb.append(radeg).append(",");
		sb.append(dedeg).append(",");
		sb.append(e_radeg).append(",");
		sb.append(e_dedeg).append(",");
		sb.append(pmra).append(",");
		sb.append(pmde).append(",");
		sb.append(e_pmra).append(",");
		sb.append(e_pmde).append(",");
		sb.append(vmag).append(",");
		sb.append(bmag).append(",");
		sb.append(b_v).append(",");
		sb.append(e_vmag).append(",");
		sb.append(e_bmag).append(",");
		sb.append(nfie).append(",");
		sb.append(o_bmag).append(",");
		sb.append(o_vmag).append(",");
		sb.append(f1).append(",");
		sb.append(f2).append(",");
		sb.append(f3);
		System.out.println(sb.toString().trim());
	}
}

public class Prog2 {

	private static RandomAccessFile dataStream = null; // this is global so it 
	// can be used to access 
	// the binary file during 
	// interpolation search

	public static void main(String[] args){
		// from within the binary file
		List<Record> newList = new ArrayList<Record>(); // list of new records
		BufferedReader reader = null;		// reads data file

		/*
		 * Prompt user for the path to the file containing SPM data
		 */

		Scanner scan = new Scanner(System.in);
		System.out.print("Please enter the path to the data file: ");
		String fName = scan.next();

		try{
			reader = new BufferedReader(new FileReader(fName));
		}catch(FileNotFoundException e){
			System.out.println("File not found: " + fName);
			System.exit(-1);
		}

		/*
		 * Grab each line of the data file and creates a new record.
		 * If successful, the new record will be added to newList
		 * recordNumber tells us which number record that is contained
		 * in the data file.
		 */
		try{
			String line;
			int recordNumber = 1;
			while((line = reader.readLine()) != null){
				Record newRecord = new Record();
				if(newRecord.createRecord(line, recordNumber))
					newList.add(newRecord);
				recordNumber++;
			}
		}catch(IOException e){
			System.out.println("I/O ERROR: Could not read from file");
			System.exit(-1);
		}

		// Sorts list of records by SPM
		//Collections.sort(newList);

		// Write binary file to file named "newData.bin"
		File f = new File("spmcat.bin");
		if(f.isFile())
			f.delete();

		/*
		 * Write all Records included in newList to the binary file, 
		 * then close it
		 */
		try {
			dataStream = new RandomAccessFile(f,"rw");
			for(Record r : newList)
				r.dumpRecord(dataStream);
			newList.clear();
			dataStream.close();
		} catch (IOException e) {
			System.out.println("I/O ERROR: Cannot create RandomAccessFile");
			System.exit(-1);
		} 
		
		try {
			reader.close();
		} catch (IOException e) {
			System.out.println("I/O ERROR: Cannot close reader");
			System.exit(-1);
		}
		
	}

}
