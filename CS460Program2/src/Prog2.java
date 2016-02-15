/*=============================================================================
 |   Assignment:  Program #2:  Extendible Hashing
 |      Authors:  Kristoffer R. Cabulong (kcabulon@email.arizona.edu)
 |				  Karyn B. Vo (kbvo@email.arizona.edu)
 |       Grader:  Shuo Yang
 |
 |       Course:  CSc 460
 |   Instructor:  L. McCann
 |     Due Date:  29 January 2016, 2:00 p.m.
 |
 |  Description:  This first program in this course served to re-introduce the programmer to
 |  	Java development and also to prime that programmer for future course assignments. To 
 |  	effect this, the programmer was required to create a sorted binary version of an ASCII 
 |  	text file (spmcat.dat) containing 321,608 records, read the first and last five records 
 |  	from the created binary file, and allow a prompted user to enter an identifier to 
 |  	recover the contents of the record with that identifier using an interpolation search 
 |  	algorithm. Fun times!
 |  
 |  	The programmer implemented an SpmDataRecord class to support each row of the provided
 |  	data (each containing 27 fields), based heavily on L. McCann's 2001 implementation of 
 |     	a DataRecord class in his own BinaryIO.java. In the main function of Prog1.java, the
 |     	programmer populates an ArrayList<SpmDataRecord> with the data provided by spmcat.dat
 |     	using a DataInputStream, applies Collections.sort(thatList), and dumps the data for 
 |     	each SpmDataRecord in that list to spm-records.bin using RandomAccessFile. Proceeding, 
 |      the programmer resets the file pointer to the beginning of spm-records.bin, re-creating
 |      the first five records and printing the contents of each, in sequence. Then, the file
 |      pointer is set to point to the fifth from last record, to print the contents of the 
 |      final five records in the same way as the first five. In the event that there are less 
 |      than five records in the file, the contents of the entire file are printed twice, to
 |      fulfill this required specification. Following, Prog1.java will print a message with 
 |      the calculated total number of records, the size of the file divided by the size of an
 |      individual SpmDataRecord (152 bytes). The program will then prompt the user to enter a
 |      valid spm object identifier, for which the program will search for in spm-records.bin
 |      by way of interpolation and return the data. Invalid inputs by the user will result in
 |      an error message, and allow the user to re-enter a new value. A successful search will
 |      also allow the user to enter another value to search for. If a search value is valid but
 |      not found, the program will notify the user that the record is not within the created
 |      binary file. On an input of 'q', the program will close data streams (Scanner and also
 |      the RandomAccessFile stream for spm-records.bin), and itself.
 |      
 |      Java 1.7 was set as the language for this file, which was developed using Eclipse Mars.1
 |      (release 4.5.1). The input file (spmcat.dat expected) should be placed in the same 
 |      directory of the compiled Prog1 to properly execute.
 |
 |     Language:  Java 1.7
 | Ex. Packages:  java.io.DataInputStream,
 |				  java.io.File,
 |				  java.io.FileInputStream,
 |				  java.io.IOException,
 |				  java.io.RandomAccessFile,
 |         		  java.util.ArrayList,
 |				  java.util.Collections,
 |				  java.util.Scanner
 |                
 | Deficiencies:  I hereby declare that know of no unsatisfied requirements nor logic errors.
 *===========================================================================*/

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashSet;

/*+----------------------------------------------------------------------
||
||  Class SpmDataRecord 
||
||         Author:  Kristoffer Cabulong 
||
||        Purpose:  The class's primary purpose is to hold each of the 27 fields
||					in a row of the spm data. It has a constructors that support
||					creating a SpmDataRecord containing null fields, and another
||					constructor which takes a line as a String from the created
||					binary file to make a new SpmDataRecord. The fields of this
||					class can also be populated from the spmcat.dat file by setting
||					an index to the beginning of a row in the file and calling
||					fetchObject to retrieve the data directly from the data stream.
||					This class also supports dumping its fields to a RandomAccessFile
||					(for writing the binary file), and a toString method to print
||					its comma-separated fields to the console.
||
||  Inherits From:  None.
||
||     Interfaces:  Comparable. compareTo() will compare int objectIdentifiers.
||
|+-----------------------------------------------------------------------
||
||      Constants:  RECORD_LENGTH - this is used to help calculate the number
||					of records, based on the size of the binary file divided by
||					this length. It is also used to determine where a row is
||					given an index (e.g. "The 42nd record in the binary is located
||					at the byte at 42 * RECORD_LENGTH")0.
||
|+-----------------------------------------------------------------------
||
||   Constructors:  public SpmDataRecord(String line),
||					public SpmDataRecord()
||
||  Class Methods: 	None.
||
||
||  Inst. Methods:  public int getObjectIdentifier(),
||					public int getRightAscensionHours(),
||					public int getRightAscensionMinutes(),
||					public double getRightAscensionSeconds(),
||					public char getDeclinationSign(),
||					public int getDeclinationDegrees(),
||					public int getDeclinationArcMin(),
||					public double getDeclinationArcSeconds(),
||					public double getRightAscensionDecDegrees(),
||					public double getDeclinationDecDegrees(),
||					public int getErrorRightAscensionDecDegrees(),
||					public int getErrorDeclinationDecDegrees(),
||					public double getProperMotionRightAscension(),
||					public double getProperMotionDeclination(),
||					public double getErrorProperMotionRightAscension(),
||					public double getErrorProperMotionDeclination(),
||					public double getvMagnitude(),
||					public double getbMagnitude(),
||					public double getbMinusV(),
||					public double getErrorVMagnitude(),
||					public double getErrorBMagnitude(),
||					public int getNfie(),
||					public int getO_Bmag(),
||					public int getO_Vmag(),
||					public char getHtFlag(),
||					public char getGkqSelectionFlag(),
||					public char getAsSpecificFlag(),
||					public void setObjectIdentifier(int objectIdentifier),
||					public void setRightAscensionHours(int rightAscensionHours),
||					public void setRightAscensionSeconds(double d),
||					public void setDeclinationSign(char declinationSign),
||					public void setDeclinationDegrees(int declinationDegrees),
||					public void setDeclinationArcMin(int declinationArcMin),
||					public void setDeclinationArcSeconds(double d),
||					public void setRightAscensionDecDegrees(double rightAscensionDecDegrees),
||					public void setDeclinationDecDegrees(double declinationDecDegrees),
||					public void setErrorRightAscensionDecDegrees(int errorRightAscensionDecDegrees),
||					public void setErrorDeclinationDecDegrees(int errorDeclinationDecDegrees),
||					public void setProperMotionRightAscension(double properMotionRightAscension),
||					public void setProperMotionDeclination(double properMotionDeclination),
||					public void setErrorProperMotionRightAscension(double errorProperMotionRightAscension),
||					public void setErrorProperMotionDeclination(double errorProperMotionDeclination),
||					public void setvMagnitude(double vMagnitude),
||					public void setbMagnitude(double bMagnitude),
||					public void setbMinusV(double bMinusV),
||					public void setErrorVMagnitude(double errorVMagnitude),
||					public void setErrorBMagnitude(double errorBMagnitude),
||					public void setNfie(int nfie),
||					public void setO_Bmag(int o_Bmag),
||					public void setO_Vmag(int o_Vmag),
||					public void setHtFlag(char htFlag),
||					public void setGkqSelectionFlag(char gkqSelectionFlag),
||					public void setAsSpecificFlag(char asSpecificFlag),
||					public void dumpObject(RandomAccessFile stream),
||					public void fetchObject(RandomAccessFile dataStream) throws IOException,
||					public String toString(),
||					public int compareTo(SpmDataRecord other)
||
++-----------------------------------------------------------------------*/
class SpmDataRecord implements Comparable<SpmDataRecord>{
	
    public static final int RECORD_LENGTH = 152; // This was found during testing by creating a
    											 // binary file containing two records. The length()
    											 // of that file was 304, so, math.
    
                    // The data fields that comprise a record of spmcat.dat (and their bytes)

    private	int objectIdentifier;    		// (8) Object identifier 
    private	int rightAscensionHours; 		// (2) Right ascension for the epoch J1991.25 and in
    										// 		the ICRS reference system (i.e. equinox 2000)
    private	int rightAscensionMinutes; 		// (2) Right ascension for the epoch J1991.25 and in
											//		the ICRS reference system (i.e. equinox 2000)
    private	double rightAscensionSeconds; 	// (6) Right ascension for the epoch J1991.25 and in
											//		the ICRS reference system (i.e. equinox 2000)
    private	char declinationSign;			// (1) The sign of the declination degrees
    private int	declinationDegrees;			// (2) Declination for the epoch J1991.25 and in the
    										// 		ICRS reference system (i.e. equinox 2000)
    private int	declinationArcMin;			// (2) Declination for the epoch J1991.25 and in the
											// 		ICRS reference system (i.e. equinox 2000)
    private double declinationArcSeconds;	// (5) Declination for the epoch J1991.25 and in the
											// 		ICRS reference system (i.e. equinox 2000)
    private double rightAscensionDecDegrees;// (10) Right ascension in decimal degrees (2000)
    private double declinationDecDegrees;	// (10) Declination in decimal degrees (2000)
    private int errorRightAscensionDecDegrees;		// (4)Estimated standard error in right ascension
    												//		multiplied by cos(DEC).
    private int errorDeclinationDecDegrees;			// (4)Estimated standard error in declination
    private double properMotionRightAscension;		// (7) Absolute proper motion in right ascension
    												//		multiplied by cos(DEC).
    private double properMotionDeclination;			// (7) Absolute proper motion in declination
    private double errorProperMotionRightAscension;	// (5) Estimated standard error in right ascension's
    												//		absolute proper motion.
    private double errorProperMotionDeclination;	// (5) Estimated standard error in declination's
    												//		absolute proper motion.
    private double vMagnitude;				// (5) Photographic V-magnitude (if missing, then set to 0.00)
    private double bMagnitude;				// (5) Photographic B-magnitude (if missing, then set to 0.00)
    private double bMinusV;					// (5) B-V color (if either of V or B is missing then set to 0.00)
    private double errorVMagnitude;			// (3) Estimated standard error in V- magnitude
    private double errorBMagnitude;			// (3) Estimated standard error in B- magnitude
    private int nfie;						// (1) Number of SPM fields on which the object was 
    										//		measured.
    private int o_Bmag;						// (2) Number of images used in astrometric reductions
    										//		from a blue plate in each epoch (2)
    private int o_Vmag;						// (2) Number of images used in astrometric reductions
    										//		from a visual plate in each epoch (2)
    private char htFlag;					// (1) [HT] Flag "H" indicates a Hipparcos star;
    										//			flag "T" indicates a Tycho star if it is
    										//			not already a Hipparcos star.
    private char gkqSelectionFlag;			// (1) [GKQ] Selection flag (3)
    private char asSpecificFlag;			// (1) [AS] Specific flag (4)

	 /*---------------------------------------------------------------------
    |  Constructor SpmDataRecord(String line)
    |
    |  Purpose:  This constructor takes a String line derived from a line
    |				read from a data text file, formatted as in 
    |				'spmcat.dat'. As intended, it is expected that the
    |				calling code will first initialize a DataInputStream
    |				constructed from a new FileInputStream constructed with
    |				the path to the 'spmcat.dat'. Then, the calling code
    |				should call readLine on that DataInputStream to generate
    |				a String containing the next row of data. That string
    |				is then used by this constructor to create a new
    |				SpmDataRecord with fields initialized to contain the 
    |				data contained in the retrieved row. In Prog1.java,
    |				this process is used over the whole of 'spmcat.dat' to
    |				generate an ArrayList of SpmDataRecord objects.
    |
    |  Pre-condition:  The data must be strictly in the format as described
    |					in spmcat.readme. This constructor does not take
    |					into account the possibility of corrupted data, as
    |					the provided spmcat.dat appears to be completely
    |					well-formed.
    |
    |  Post-condition: A new SpmDataRecord will be created with fields 
    |					initialized to contain the data contained in the 
    |					retrieved row.
    |
    |  Parameters: 
    |		String line -- This line is the retrieved row of raw data for
    |						one row of the 'spmcat.dat' file. This 
    |						constructor parses the substring according to
    |						'spmcat.readme' to properly assign the fields
    |						of the constructed SpmDataRecord object.
    |
    *-------------------------------------------------------------------*/
    public SpmDataRecord(String line) {
    	objectIdentifier = Integer.parseInt(line.substring(0, 8).trim());
        rightAscensionHours = Integer.parseInt(line.substring(9, 11).trim());
        rightAscensionMinutes = Integer.parseInt(line.substring(12, 14).trim());
        rightAscensionSeconds = Double.parseDouble(line.substring(15, 21).trim());
        declinationSign = line.charAt(22);
        declinationDegrees = Integer.parseInt(line.substring(23, 25).trim());
        declinationArcMin = Integer.parseInt(line.substring(26, 28).trim());
        declinationArcSeconds = Double.parseDouble(line.substring(29, 34).trim());
        rightAscensionDecDegrees = Double.parseDouble(line.substring(35, 45).trim());
        declinationDecDegrees = Double.parseDouble(line.substring(46, 56).trim());
        errorRightAscensionDecDegrees = Integer.parseInt(line.substring(56, 60).trim());
        errorDeclinationDecDegrees = Integer.parseInt(line.substring(60, 64).trim());
        properMotionRightAscension = Double.parseDouble(line.substring(65, 72).trim());
        properMotionDeclination = Double.parseDouble(line.substring(73, 80).trim());
        errorProperMotionRightAscension = Double.parseDouble(line.substring(80, 85).trim());
        errorProperMotionDeclination = Double.parseDouble(line.substring(85, 90).trim());
        vMagnitude = Double.parseDouble(line.substring(91, 96).trim());
        bMagnitude = Double.parseDouble(line.substring(97, 102).trim());
        bMinusV = Double.parseDouble(line.substring(103, 108).trim());
        errorVMagnitude = Double.parseDouble(line.substring(108, 111).trim());
        errorBMagnitude = Double.parseDouble(line.substring(111, 114).trim());
        nfie = Integer.parseInt(line.charAt(117) + "");
        o_Bmag = Integer.parseInt(line.substring(119, 121).trim());
        o_Vmag = Integer.parseInt(line.substring(122, 124).trim());
        htFlag = line.charAt(125);
        gkqSelectionFlag = line.charAt(126);
        asSpecificFlag = line.charAt(127);
	}
    
	 /*---------------------------------------------------------------------
    |  Constructor SpmDataRecord()
    |
    |  Purpose:  This constructor is used to generate an SpmDataRecord with
    |				null fields. This is intended to quickly initialize a
    |				new SpmDataRecord object with the intention of populating
    |				the field data using fetchObject.
    |
    |  Pre-condition:  None.
    |
    |  Post-condition: A new SpmDataRecord will be created with fields 
    |					initialized to null;
    |
    |  Parameters: None.
    |
    *-------------------------------------------------------------------*/
	public SpmDataRecord() {
		// the NULL constructor
	}

    /*---------------------------------------------------------------------
    |  Method (getters and setters for each field)
    |
    |  Purpose:  These methods are used for accessing the fields of an 
    |		SpdDataRecord instance.
    
    |  Pre-condition:  The instance must be initialized.
    |
    |  Post-condition: 'Getter' methods will return the value of the
    |		instance's field. 'Setter' methods will change the value
    |		of a corresponding field, returning void.
    |
    |  Parameters: (As expected for source-generated 'getters' and 'setters'.
    |      
    |  Returns:  'Getter' methods will return the value of the
    |		instance's field. 'Setter' methods will change the value
    |		of a corresponding field, returning void.
    *-------------------------------------------------------------------*/
	
	// 'Getters' for the data field values
    
	public int getObjectIdentifier() {
		return objectIdentifier;
	}
	public int getRightAscensionHours() {
		return rightAscensionHours;
	}
	public int getRightAscensionMinutes() {
		return rightAscensionMinutes;
	}
	public double getRightAscensionSeconds() {
		return rightAscensionSeconds;
	}
	public char getDeclinationSign() {
		return declinationSign;
	}
	public int getDeclinationDegrees() {
		return declinationDegrees;
	}
	public int getDeclinationArcMin() {
		return declinationArcMin;
	}
	public double getDeclinationArcSeconds() {
		return declinationArcSeconds;
	}
	public double getRightAscensionDecDegrees() {
		return rightAscensionDecDegrees;
	}
	public double getDeclinationDecDegrees() {
		return declinationDecDegrees;
	}
	public int getErrorRightAscensionDecDegrees() {
		return errorRightAscensionDecDegrees;
	}
	public int getErrorDeclinationDecDegrees() {
		return errorDeclinationDecDegrees;
	}
	public double getProperMotionRightAscension() {
		return properMotionRightAscension;
	}
	public double getProperMotionDeclination() {
		return properMotionDeclination;
	}
	public double getErrorProperMotionRightAscension() {
		return errorProperMotionRightAscension;
	}
	public double getErrorProperMotionDeclination() {
		return errorProperMotionDeclination;
	}
	public double getvMagnitude() {
		return vMagnitude;
	}
	public double getbMagnitude() {
		return bMagnitude;
	}
	public double getbMinusV() {
		return bMinusV;
	}
	public double getErrorVMagnitude() {
		return errorVMagnitude;
	}
	public double getErrorBMagnitude() {
		return errorBMagnitude;
	}
	public int getNfie() {
		return nfie;
	}
	public int getO_Bmag() {
		return o_Bmag;
	}
	public int getO_Vmag() {
		return o_Vmag;
	}
	public char getHtFlag() {
		return htFlag;
	}
	public char getGkqSelectionFlag() {
		return gkqSelectionFlag;
	}
	public char getAsSpecificFlag() {
		return asSpecificFlag;
	}

                    // 'Setters' for the data field values

	public void setObjectIdentifier(int objectIdentifier) {
		this.objectIdentifier = objectIdentifier;
	}
	public void setRightAscensionHours(int rightAscensionHours) {
		this.rightAscensionHours = rightAscensionHours;
	}
	public void setRightAscensionMinutes(int rightAscensionMinutes) {
		this.rightAscensionMinutes = rightAscensionMinutes;
	}
	public void setRightAscensionSeconds(double d) {
		this.rightAscensionSeconds = d;
	}
	public void setDeclinationSign(char declinationSign) {
		this.declinationSign = declinationSign;
	}
	public void setDeclinationDegrees(int declinationDegrees) {
		this.declinationDegrees = declinationDegrees;
	}
	public void setDeclinationArcMin(int declinationArcMin) {
		this.declinationArcMin = declinationArcMin;
	}
	public void setDeclinationArcSeconds(double d) {
		this.declinationArcSeconds = d;
	}
	public void setRightAscensionDecDegrees(double rightAscensionDecDegrees) {
		this.rightAscensionDecDegrees = rightAscensionDecDegrees;
	}
	public void setDeclinationDecDegrees(double declinationDecDegrees) {
		this.declinationDecDegrees = declinationDecDegrees;
	}
	public void setErrorRightAscensionDecDegrees(int errorRightAscensionDecDegrees) {
		this.errorRightAscensionDecDegrees = errorRightAscensionDecDegrees;
	}
	public void setErrorDeclinationDecDegrees(int errorDeclinationDecDegrees) {
		this.errorDeclinationDecDegrees = errorDeclinationDecDegrees;
	}
	public void setProperMotionRightAscension(double properMotionRightAscension) {
		this.properMotionRightAscension = properMotionRightAscension;
	}
	public void setProperMotionDeclination(double properMotionDeclination) {
		this.properMotionDeclination = properMotionDeclination;
	}
	public void setErrorProperMotionRightAscension(double errorProperMotionRightAscension) {
		this.errorProperMotionRightAscension = errorProperMotionRightAscension;
	}
	public void setErrorProperMotionDeclination(double errorProperMotionDeclination) {
		this.errorProperMotionDeclination = errorProperMotionDeclination;
	}
	public void setvMagnitude(double vMagnitude) {
		this.vMagnitude = vMagnitude;
	}
	public void setbMagnitude(double bMagnitude) {
		this.bMagnitude = bMagnitude;
	}
	public void setbMinusV(double bMinusV) {
		this.bMinusV = bMinusV;
	}
	public void setErrorVMagnitude(double errorVMagnitude) {
		this.errorVMagnitude = errorVMagnitude;
	}
	public void setErrorBMagnitude(double errorBMagnitude) {
		this.errorBMagnitude = errorBMagnitude;
	}
	public void setNfie(int nfie) {
		this.nfie = nfie;
	}
	public void setO_Bmag(int o_Bmag) {
		this.o_Bmag = o_Bmag;
	}
	public void setO_Vmag(int o_Vmag) {
		this.o_Vmag = o_Vmag;
	}
	public void setHtFlag(char htFlag) {
		this.htFlag = htFlag;
	}
	public void setGkqSelectionFlag(char gkqSelectionFlag) {
		this.gkqSelectionFlag = gkqSelectionFlag;
	}
	public void setAsSpecificFlag(char asSpecificFlag) {
		this.asSpecificFlag = asSpecificFlag;
	}
	
    /*---------------------------------------------------------------------
    |  Method dumpObject
    |
    |  Purpose:  This method is used to write the contents of an
    |				SpmDataRecord instance to a binary file. First, the 
    |				calling code creates an instance of a RandomAccessFile
    |				to create a stream to a specified output file. 
    |				In Prog1.java, that file is 'spm-records.bin'. This
    |				method will then take that RandomAcessFile as a 
    |				parameter, and then write each field to the binary 
    |				file. If a write operation fails, the method will exit
    |				with a message and a code of -1.
    |
    |  Pre-condition:  This method expects the data in the RandomAccessFile
    |					stream to be of a precise arrangement, following 
    |					the format of the class-provided 'spmcat.dat' file.
    |
    |  Post-condition: The file opened in the RandomAccessFile stream will 
    |					contain the data from the instance of SpmDataRecord
    |					from which this method was invoked.
    |
    |  Parameters:
    |      RandomAccessFile stream-- This parameter provides the data 
   	|									stream and the file to which this
   	|									method will write its field data.
    |
    |  Returns:  None.
    *-------------------------------------------------------------------*/
	public void dumpObject(RandomAccessFile stream)
	{
	
	     try {
	         stream.writeInt(objectIdentifier);
	         stream.writeInt(rightAscensionHours);
	         stream.writeInt(rightAscensionMinutes);
	         stream.writeDouble(rightAscensionSeconds);
	         stream.writeChar(declinationSign);
	         stream.writeInt(declinationDegrees);
	         stream.writeInt(declinationArcMin);
	         stream.writeDouble(declinationArcSeconds);
	         stream.writeDouble(rightAscensionDecDegrees);
	         stream.writeDouble(declinationDecDegrees);
	         stream.writeInt(errorRightAscensionDecDegrees);
	         stream.writeInt(errorDeclinationDecDegrees);
	         stream.writeDouble(properMotionRightAscension);
	         stream.writeDouble(properMotionDeclination);
	         stream.writeDouble(errorProperMotionRightAscension);
	         stream.writeDouble(errorProperMotionDeclination);
	         stream.writeDouble(vMagnitude);
	         stream.writeDouble(bMagnitude);
	         stream.writeDouble(bMinusV);
	         stream.writeDouble(errorVMagnitude);
	         stream.writeDouble(errorBMagnitude);
	         stream.writeInt(nfie);
	         stream.writeInt(o_Bmag);
	         stream.writeInt(o_Vmag);
	         stream.writeChar(htFlag);
	         stream.writeChar(gkqSelectionFlag);
	         stream.writeChar(asSpecificFlag);
	    } catch (IOException e) {
	        System.out.println("I/O ERROR in dumpObject(): Couldn't write to the file;\n\t"
	                         + "perhaps the file system is full?");
	        System.exit(-1);
	     }
    } 
     /* fetchObject(stream) -- read the content of the object's fields
      * from the file represented by the given RandomAccessFile object
      * reference, starting at the current file position.  Primitive
      * types (e.g., int) are read directly.  To create Strings containing
      * the text, because the file records have text stored with one byte
      * per character, we can read a text field into an array of bytes and
      * use that array as a parameter to a String constructor.
      */

    /*---------------------------------------------------------------------
     |  Method fetchObject
     |
     |  Purpose:  This method is used to replace the data fields of an 
     |				SpmDataRecord instance directly from a RandomAccessFile
     |				dataStream. It's intended use is for the calling code 
     |				to create a new SpmDataRecord instance using the null
     |				constructor, initializing a RandomAccessFile stream
     |				that reads from the programmer-sorted binary file 
     |				('spm-records.bin'), setting an index pointer on that
     |				data stream to the beginning of the record meant for 
     |				retrieval, and then using fetchObject on the data stream
     |				to pull that record's data to populate the null fields
     |				of the SpmDataRecord. Note: THIS IS NOT FOR PULLING
     |				DATA FROM THE TEXT DATA FILE ('spmcat.dat'). SEE THE
     |				BLOCK COMMENT FOR THE SpmDataRecord(String line) 
     |				CONSTRUCTOR ABOVE FOR MORE INFO. 
     |
     |  Pre-condition:  The binary data file that this method is intended
     |						to pull data from must only contain records
     |						of this file's SpmDataRecord class.
     |
     |  Post-condition: The fields of the invoking instance of SpmDataRecord
     |						will be changed according the the data retrieved
     |						from the the RandomAccessFile data stream.
     |
     |  Parameters:
     |      RandomAccessFile dataStream -- The field data used to replace
     |										the data in the invoking 
     |										instance is retrieved from
     |										this RandomAccessFile stream.
     |
     |  Returns:  None.
     *-------------------------------------------------------------------*/
	 public void fetchObject(RandomAccessFile dataStream) throws IOException
	 {
	      
	      try {
	          objectIdentifier = dataStream.readInt();
	          rightAscensionHours = dataStream.readInt();
	          rightAscensionMinutes = dataStream.readInt();
	          rightAscensionSeconds = dataStream.readDouble();
	          declinationSign = dataStream.readChar();
	          declinationDegrees = dataStream.readInt();
	          declinationArcMin = dataStream.readInt();
	          declinationArcSeconds = dataStream.readDouble();
	          rightAscensionDecDegrees = dataStream.readDouble();
	          declinationDecDegrees = dataStream.readDouble();
	          errorRightAscensionDecDegrees = dataStream.readInt();
	          errorDeclinationDecDegrees = dataStream.readInt();
	          properMotionRightAscension = dataStream.readDouble();
	          properMotionDeclination = dataStream.readDouble();
	          errorProperMotionRightAscension = dataStream.readDouble();
	          errorProperMotionDeclination = dataStream.readDouble();
	          vMagnitude = dataStream.readDouble();
	          bMagnitude = dataStream.readDouble();
	          bMinusV = dataStream.readDouble();
	          errorVMagnitude = dataStream.readDouble();
	          errorBMagnitude = dataStream.readDouble();
	          nfie = dataStream.readInt();
	          o_Bmag = dataStream.readInt();
	          o_Vmag = dataStream.readInt();
	          htFlag = dataStream.readChar();
	          gkqSelectionFlag = dataStream.readChar();
	          asSpecificFlag = dataStream.readChar();

	     } catch (IOException e) {
	         System.out.println("I/O ERROR in fetchObject(dataStream): Couldn't read from the binary file;\n\t"
	                          + "perhaps the file system is full?");
	         System.exit(-1);
	      }
	 }
	 
	 /*---------------------------------------------------------------------
     |  Method toString
     |
     |  Purpose:  This method simply returns each field of the invoking
     |				SpmDataRecord instance as a string containing comma-
     |				separated values. This is used with System.Out.println
     |				to print the values of an SpmDataRecord to the console.
     |
     |  Pre-condition:  None.
     |
     |  Post-condition: Nothing changed by invoking this method by itself.
     |						A String could contain the returned string
     |						result, or, using System.Out.println, this
     |						value may be printed to the console. 
     |
     |  Parameters: None.
     |
     |  Returns:  String result -- This is intended to be used with 
     |								System.Out.println to print the values 
     |								of an SpmDataRecord to the console.
     *-------------------------------------------------------------------*/
	 public String toString(){
         String result = objectIdentifier + "," +
        		 rightAscensionHours + "," +
        		 rightAscensionMinutes + "," +
        		 rightAscensionSeconds + "," +
        		 declinationSign + "," +
        		 declinationDegrees + "," +
        		 declinationArcMin + "," +
        		 declinationArcSeconds + "," +
        		 rightAscensionDecDegrees + "," +
        		 declinationDecDegrees + "," +
        		 errorRightAscensionDecDegrees + "," +
        		 errorDeclinationDecDegrees + "," +
        		 properMotionRightAscension + "," +
        		 properMotionDeclination + "," +
        		 errorProperMotionRightAscension + "," +
        		 errorProperMotionDeclination + "," +
        		 vMagnitude + "," +
        		 bMagnitude + "," +
        		 bMinusV + "," +
        		 errorVMagnitude + "," +
        		 errorBMagnitude + "," +
        		 nfie + "," +
        		 o_Bmag + "," +
        		 o_Vmag + "," +
        		 htFlag + "," +
        		 gkqSelectionFlag + "," +
        		 asSpecificFlag;
         
         return result;
    }
	 /*---------------------------------------------------------------------
     |  Method compareTo
     |
     |  Purpose:  This method is used to compare SpmDataRecord objects by
     |				their objectIdentifier values, and to implement the
     |				Comparable interface. This allows an invocation of
     |				Collections.sort on an ArrayList of SpmDataRecords,
     |				used by Prog1.java to ensure the generated ArrayList
     |				is sorted prior to generating 'spm-records.bin' from
     |				that ArrayList.
     |
     |  Pre-condition:  Both instances being compared must have initialized
     |					objectIdentifier variables.
     |
     |  Post-condition: Nothing changed by invoking this method by itself.
     |
     |  Parameters: 
     |		SpmDataRecord other -- This is the SpmDataRecord against which
     |								the invoking instanced is compared 
     |								against, according to objectIdentifier.
     |
     |  Returns:  int this.objectIdentifier - other.getObjectIdentifier().
     |				This compares the invoking instance's objectIdentifier
     |				with the SpmDataRecord passed in, also allowing this
     |				class to implement the Comparable interface.				
     *-------------------------------------------------------------------*/
	@Override
	public int compareTo(SpmDataRecord other) {
		return this.objectIdentifier - other.getObjectIdentifier();
	}

}

class Entry implements Comparable<Entry>{
	
	int key;		//The SPM value
	int pointer;	//The byte offsets into the files
	
	public Entry(int key, int pointer){
		this.key = key;
		this.pointer = pointer;
	}
	
	public int getKey() {
		return key;
	}
	public int getPointer() {
		return pointer;
	}
	public void setKey(int key) {
		this.key = key;
	}
	public void setPointer(int pointer) {
		this.pointer = pointer;
	}
	
	public String toString(){
		return "ENTRY: " + key + " : " + pointer;
	}

	@Override
	public int compareTo(Entry other) {
		return this.key - other.key;
	}
	
}



class Bucket{
	
	Entry[] slotsArray = new Entry[50]; // 100 ints = 100 * 8 = 800 bytes
	int localDepth = 1; // 1 int = 8 bytes
	int entries = 0; // 1 int = 8 bytes
					// Bucket size is 800 + 8 + 8 = 816 bytes
	
	public Bucket(){
		for (int i = 0; i<50;i++){
			slotsArray[i] = new Entry(-1, -1);
		}
	}
	
	public Bucket(RandomAccessFile buckets, int pointer) {
		try {
			buckets.seek(pointer);
		} catch (IOException e1) {
			System.out.println("Failed seek with pointer in readBucket");
			e1.printStackTrace();
		}
		try {
			// read 800 bytes
			for(int i = 0; i < slotsArray.length; i++){
					this.slotsArray[i] = new Entry(buckets.readInt(), buckets.readInt());
			}
			
			this.localDepth = buckets.readInt();
			
			this.entries = buckets.readInt();
		
		} catch (IOException e) {
			System.out.println("Could not read int from bucket");
			e.printStackTrace();
		}
	}

	public int getLocalDepth() {
		return localDepth;
	}

	public void setLocalDepth(int localDepth) {
		this.localDepth = localDepth;
	}

	public String toString(){
		System.out.println("BucketDump");
		System.out.println("Local Depth: " + localDepth);
		System.out.println("Entries: " + entries);
		for (Entry entry : slotsArray){
			if (entry.getKey()!=-1){
			System.out.println(entry.getKey() + " : " + entry.getPointer());
			}
		}
		return null;
	}
	
	public void addEntry(Entry newEntry){
		
			slotsArray[entries] = newEntry;
			entries++;
	}
	
	/*
	 * Reads a bucket record from the created hash bucket file to replicate 
	 * that bucket into memory.
	 */
	public void readBucket(RandomAccessFile buckets, int pointer){

		try {
			buckets.seek(pointer);
		} catch (IOException e1) {
			System.out.println("Failed seek with pointer in readBucket");
			e1.printStackTrace();
		}
		try {
			// read 800 bytes
			for(int i = 0; i < slotsArray.length; i++){
					slotsArray[i] = new Entry(buckets.readInt(), buckets.readInt());
			}
			
			localDepth = buckets.readInt();
			
			entries = buckets.readInt();
		
		} catch (IOException e) {
			System.out.println("Could not read int from bucket");
			e.printStackTrace();
		}
	}
	
	/*
	 * After a bucket is modified, this is used to write that bucket back onto 
	 * the hash bucket file with updated contents.
	 */
	public void writeBucket(RandomAccessFile buckets, int pointer){
		
		try {
			buckets.seek(pointer);
		} catch (IOException e1) {
			System.out.println("Failed seek with pointer in writeBucket");
			e1.printStackTrace();
		}
		
		try {
			// read 800 bytes
			for(int i = 0; i < slotsArray.length; i++){
				buckets.writeInt(slotsArray[i].key);
				buckets.writeInt(slotsArray[i].pointer);
			}
			
			buckets.writeInt(localDepth);
			buckets.writeInt(entries);
		
		} catch (IOException e) {
			System.out.println("Error while trying to write to database");
			e.printStackTrace();
		}
	}
	
}



public class Prog2
{
	     @SuppressWarnings("deprecation")
		public static void main (String [] args) throws IOException
	     {
	         File             databaseFile;             // used to create the file
	         RandomAccessFile databaseFileStream = null;   // specializes the file I/O
	         long             numberOfRecords = 0; // loop counter for reading file
	         final int 		  BUCKET_SIZE = 408; // Bucket size is 400 + 4 + 4 = 408 bytes
      
        // Create and populate the records to be written
		     
		     //Create a datastream connected to the input file: 'spmcat.dat'
		     DataInputStream spmcatDATstream = null;
		     try{
		    	 spmcatDATstream = new DataInputStream(new FileInputStream("spmcat.dat"));
		     }catch(Exception e){
		    	 System.out.println("Could not open spmcat.dat!!!! >:(");
		     }
		     
		     // Set up a binary file to write the data held in the ArrayList
		     //	of SpmDataRecord objects.
	         databaseFile = new File("spm-records.bin");
	         
	         // Create a stream to that file.
	         try {
	             databaseFileStream = new RandomAccessFile(databaseFile,"rw");
	         } catch (IOException e) {
	             System.out.println("I/O ERROR: Something went wrong with the "
	                              + "creation of the RandomAccessFile object.");
	             System.exit(-1);
	         }
	         
	         // Here, we create the initial directory and the initial Hash Bucket File.
	         // The HashBucket File is a file separate from the binary database file,
	         // And contains the directory
	         
	         // Create file stream to generate index/hash bucket file
	         File hashBucketsFile = new File("hashBuckets.bin");
	         RandomAccessFile hashBucketStream = null;
	         
	         // Create a stream to that file.
	         try {
	             hashBucketStream = new RandomAccessFile(hashBucketsFile,"rw");
	         } catch (IOException e) {
	             System.out.println("I/O ERROR: Something went wrong with the "
	                              + "creation of the RandomAccessFile object.");
	             System.exit(-1);
	         }
	         
	         //Create initial directory int[10]
	         //This directory holds pointers to hash buckets
	         int[] directory = {-99, -99, -99, -99, -99, -99, -99, -99, -99, -99};
	         int globalDepth = 1;	//This is the global depth of our directory
	         int databaseScanPtr = 0;
	         
	         // Now, create the initial 10 buckets, reference them using the directory, and then write to disk
	         for (int b = 0; b < 10; b++){
	        	 Bucket initialBucket = new Bucket();
	        	 directory[b] = (int) hashBucketStream.length();
	        	 initialBucket.writeBucket(hashBucketStream, directory[b]);
	         }
		  
		     // Read spmcat.dat line by line, creating SpmDataRecords
		     //	using the SpmDataRecord(String line) constructor).
		     try{
		    	 SpmDataRecord curr;
		    	 String line = spmcatDATstream.readLine();
		    
			    while (line != null){
//		    	 for (int j = 0; j < 500 ; j++){
	//			    System.out.println("Getting at most 10000 records...");
//				    for (int i = 0; i < 10 && line!=null; i++){	 

		        // This writes the record to the database binary file on disk
				curr = new SpmDataRecord(line);
				curr.dumpObject(databaseFileStream);
				/*
				 * For each record: 1) create an entry a) get record at index
				 * pointer * record_length in binary database file b) create
				 * entry (spm, current index*record_length) 2) if bucket doesn't
				 * exist, a) create that bucket b) add entry to that bucket c)
				 * add bucket to hashBucketFile d) find pointer to start of that
				 * bucket in hashBucketFile e) place that pointer in the
				 * directory 3) else a) temporary bucket = bucket at pointer
				 * from index* bucket size b) add new entry to temp bucket in
				 * memory c) write temp bucket to hash bucket file at pointer
				 * index * bucket size
				 * 
				 * 
				 * BUT WHAT IF WE HAVE TO SPLIT?!??!?!
				 * 
				 * 3) else a) temp bucket = bucket at pointer from index *
				 * bucket size b) if tempBucket.localDepth > global i) GROW THE
				 * DIRECTORY AND SPLIT BUCKET ii) increment globalDepth,
				 * localDepth for buckets involved in split iii) Create 9 other
				 * buckets (original becomes X0) iv) redistribute bucket
				 * contents v) write buckets to file vi) grow directory to next
				 * depth vii) fix directory 1) Redistribute existing directory
				 * bucket pointers 2) Add those 9 new bucket pointers c) else
				 * just SPLIT THE BUCKET i) Create 9 other buckets ii) increment
				 * localDepth for new buckets iii) redistribute bucket contents
				 * iv) write buckets to file v) update directory bucket pointers
				 */

				// An entry consists of an spm identifier as a key, and the pointer to the record's location in spm-records.bin
				Entry newEntry = new Entry(curr.getObjectIdentifier(), databaseScanPtr * SpmDataRecord.RECORD_LENGTH);
				
				boolean addSuccess = false;
				
				globalDepth = 0;
				int depthFinder = directory.length;
				while (depthFinder != 1){
					depthFinder = depthFinder/10;
					globalDepth++;
				}
				
				// "while we haven't succeeded in adding an entry into a bucket...
				while (addSuccess == false) {
					
					int query = newEntry.key;
					
					// Find the bucket where this entry is supposed to go. (This must be recalculated with every
					//	iteration of this while loop.)
					for (int i = 0; i < (Integer.toString(newEntry.key).length() - globalDepth); i++){
						query = query / 10;
					}
					Bucket tempBucket = new Bucket(hashBucketStream, directory[query]);
					int originalPtr = directory[query];

					// We found a bucket, and it isn't full!
					if (tempBucket.entries < 50) {
						// add entry to the found bucket
						tempBucket.addEntry(newEntry);
						// add bucket to hash bucket file
						tempBucket.writeBucket(hashBucketStream, directory[query]);
						addSuccess = true;

					} else if (tempBucket.entries >= 50 && tempBucket.localDepth < globalDepth) {

	//					System.out.println("Split Bucket!!!!" + newEntry.key + " query: " + query);
						// Create 10 new buckets
						for (int index = 0; index < 10; index++) {
							Bucket newBucket = new Bucket();

							try {
								// divide up all values from the temp bucket to the 10 new buckets

								// for each entry, check the new digit we're
								// splitting by.
								for (Entry entry : tempBucket.slotsArray) {
									
									int compareIndex = getCompareIndex(entry.key, globalDepth);									
									if (compareIndex == index) {
										newBucket.addEntry(entry);
									}
								}

								// the new "X0" bucket will take the place of
								// the old bucket "0" in the hash bucket file
								int hashBucketPtr;
//								if (index == 0){
//									hashBucketPtr = originalPtr;	// location of original bucket pre-split
//									
////									Bucket prevBucket = new Bucket(hashBucketStream, directory[query]);
////									System.out.println("PREVIOUS directory[query]:::" + prevBucket.toString());
////									System.out.println("BBREAK");
//								}else{
//									hashBucketPtr = (int) hashBucketStream.length();
//								}
								hashBucketPtr = (int) hashBucketStream.length();

								// update index
								int bucketIndex = (query - (query % 10)) + index;
				//				System.out.println(query + " - " + query % 10 + " + " + index + ", bucketIndex is: " + bucketIndex);
								directory[bucketIndex] = hashBucketPtr;

								// increment local depth for created bucket
								newBucket.localDepth = tempBucket.localDepth + 1;

								// write bucket to disk
								newBucket.writeBucket(hashBucketStream, hashBucketPtr);

							} catch (Exception e) {
								System.out.println("Failure detected in SPLIT BUCKET logic");
								e.printStackTrace();
							}

						}

					} else if (tempBucket.entries >= 50 && tempBucket.localDepth == globalDepth){ // entries are at capacity and local depth ==
								// global depth; directory needs to grow
						// System.out.println("Bucket to split is at " +
						// directory[comparator]);
						System.out.println("SPLIT DIRECTORY!!!!!!!!!!!!!!!! " + newEntry.key+ " query: " + query);
						
						int[] tempDir = new int[directory.length * 10];

						globalDepth++;
						// For all values in directory, add to new directory.
						// Populate the directory range where k = old,
						// (k*10) + onesPlace = old ptr
						for (int k = 0; k < directory.length; k++) {
							// populate new array with range k0-k9 with same
							// ptr value as
							// directory[k]
							for (int onesPlace = 0; onesPlace < 10; onesPlace++) {
								int newIndex = (k * 10) + onesPlace;
								tempDir[newIndex] = directory[k];
							}
						}
						directory = tempDir;
					}
				}

				//stuff for building the database file (remember, we're doing both at the same time)
				line = spmcatDATstream.readLine();
				databaseScanPtr++;
				
			}
		    	 
			int indexLocation = (int) hashBucketStream.length();
			hashBucketStream.seek(indexLocation);
			// write each pointer to the index file
			for (int i = 0; i < directory.length; i++) {
				hashBucketStream.writeInt(directory[i]);
			}

			// write the location of the start of the directory
	//		System.out.println("Location of index: " + location);
			hashBucketStream.writeInt(indexLocation);
	//		System.out.println("size of file: " + hashBucketStream.length());
		} catch (Exception NullPointerException) {
			System.out.println("End of file!");
			
			NullPointerException.printStackTrace();
		}

		// When finished, close the dataStream
		try {
			if (spmcatDATstream != null) {
				spmcatDATstream.close();
			}
		} catch (Exception e) {
			System.out.println("Could not close inputStream! >:(");
		}

		//     System.out.println("Database File generated");
	         
	         /*
	          * Calculate the number of records by dividing the length of the
	          * 	file by the size of each SpmDataRecord
	          */
	         try {
	             numberOfRecords = databaseFileStream.length() / SpmDataRecord.RECORD_LENGTH;
	         } catch (IOException e) {
	             System.out.println("I/O ERROR: Couldn't get the file's length.");
	             System.exit(-1);
	         }
	         
			/*
			 * 		Print out the statistics (COMPUTED FROM THE INDEX'S CONTENT - hashBucketSteam)
			 * 			1) Global Depth of Directory
			 * 			2) Number of directory entries pointing to buckets
			 * 			3) Number of buckets in the hash bucket file
			 * 			4) Average bucket occupancy as a percentage
			 * 			5) Arithmetic mean of the SPM values held within the hash bucket file
			 */
	         
	         // As per the spec, we have to get this information from the index written to file,
	         // and can't just use the information created in volatile memory during index creation.
	         // So, here we recreate the index:
	         
	 		directory = null;		//Because we're required to generate this from the bin file now
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
				
				// Grab the int we placed at the file holding the ptr to the index
				hashBucketStream.seek((hashBucketStream.length() - 4)); // 4 is the size of an int
				startDirPtr = hashBucketStream.readInt();
				hashBucketStream.seek(startDirPtr);
				
				// Calculate the size of the "new" directory
				sizeOfDirectory = (int) (hashBucketStream.length() - 4 - startDirPtr);
				sizeOfDirectory = sizeOfDirectory / 4;

				// Initialize "new" directory with values read from the binary hashBucket.bin file
				directory = new int[sizeOfDirectory];
				for(int i = 0; i < sizeOfDirectory; i++){
						directory[i] = hashBucketStream.readInt();
				}
			} catch (IOException e) {
				System.out.println("I/O Error while collecting index information from "
						+ "the hash bucket binary file data stream (\"hashBuckets.bin\")");
				e.printStackTrace();
			}
			
			//dumpBuckets(hashBucketStream, directory, databaseFileStream);
			
			// 1) Global Depth of Directory
			globalDepth = 0;
			int depthFinder = directory.length;
			while (depthFinder != 1){
				depthFinder = depthFinder/10;
				globalDepth++;
			}
			System.out.println("Global Depth of Directory: " + globalDepth);
			
			// 2) Number of directory entries pointing to buckets
			//		Since everything in the directory array points to a bucket (even redundantly),
			//		therefore, this value is the same as the size of the directory.
			System.out.println("Number of directory entries pointing to buckets: " + directory.length);
			
			// 3) Number of buckets in the hash bucket file
			//		This is the set of unique values contained within the hash bucket index file
			HashSet<Integer> bucketSet = new HashSet<Integer>();
			
			for (int pointer : directory){
				bucketSet.add(pointer);
			}
			System.out.println("Number of buckets in the hash bucket file: " + bucketSet.size());
			
			// 4) Average bucket occupancy as a percentage
			//		Strictly, this above request seems to imply that we include empty buckets in our
			// 		computation. However, we feel it would be much more informative to calculate
			//		the average occupancy only for those buckets that contain at least one entry.
			//		We also hope that if we are wrong in our interpretation, our grader understands
			//		how trivial of a modification to the below code would be to include those nothing buckets.
			// 5) Arithmetic mean of the SPM values held within the hash bucket file
			//		This will be computed concurrently with #4.
			
			float bucketOccupancySum = 0;
			double spmValueSum = 0;
			float totalBuckets = 0;
	        double totalEntries = 0;
	        
	        Bucket currBucket = new Bucket();
	        for (int pointer : bucketSet){
	        	currBucket.readBucket(hashBucketStream, pointer);
	        	totalBuckets++;
	        	if (currBucket.entries != 0){
	        		bucketOccupancySum += currBucket.entries;
	        	}
	        	for (Entry currEntry : currBucket.slotsArray){
	        		if (currEntry.key != -1){
	        			spmValueSum += currEntry.key;
	        			totalEntries++;
	        		}
	        	}
	        }
	        
	        System.out.println("Average bucket occupany as a percentage: " 
	        						+	bucketOccupancySum/totalBuckets/50 * 100
	        						+ 	"%");
	        
	        
	        System.out.printf("Arithmetic mean of the SPM values held within the hash bucket file: %.0f", spmValueSum/totalEntries);
	         
	         /*
	          * TEST METHODS
	          */
	         
	         

	         // CLOSE DATA STREAMS HERE
	         try {
	        	 hashBucketStream.close();
	         } catch (IOException e) {
	             System.out.println("VERY STRANGE I/O ERROR: Couldn't close "
	                              + "the hashBucket file!");
	         }
	         
	         // Might as well use the same try/catch that L. McCann used...
	         try {
	             databaseFileStream.close();
	         } catch (IOException e) {
	             System.out.println("VERY STRANGE I/O ERROR: Couldn't close "
	                              + "the database file!");
	         }
	     }


		private static int getCompareIndex(int key, int globalDepth) {
			
			String substring = Integer.toString(key).substring(0, globalDepth);
			int result = Integer.parseInt(substring);
			result = result % 10;
			
			return result;
		}	     
		
		/*
	      * This is a utility used to see the contents of every bucket written to disk
	      */
	     private static void dumpBuckets(RandomAccessFile buckets, int[] dir, RandomAccessFile databaseFileStream){
	    	 
	    	 System.out.println("BUCKET DUMP");
	    	 
		 		try {
		 			buckets.seek(0);
				} catch (IOException e1) {
					System.out.println("Failed seek with pointer in bucket dump");
					e1.printStackTrace();
				}
//		 		for (int i = 0; i<size+1; i++){
		 		for(int i = 0; i < dir.length; i++){
//		 			if(dir[i] != -99){
			 			System.out.println("BUCKET " + i + ":");
			 			
			 			try {
			// 				System.out.println("dir[i] is: " + dir[i]);
				 			buckets.seek(dir[i]);
						} catch (IOException e1) {
							System.out.println("Failed seek with pointer in bucket dump");
							e1.printStackTrace();
						}
			 			
			    		try {
			    			// read 800 bytes
			    			SpmDataRecord record = new SpmDataRecord();
			    			for(int j = 0; j < 50; j++){
			    				Entry printEntry = new Entry(buckets.readInt(), buckets.readInt());
			    				if (printEntry.key != -1){
			    //					System.out.println(printEntry.toString());
			    					databaseFileStream.seek(printEntry.pointer);
			    					record.fetchObject(databaseFileStream);
			    					System.out.println(record.toString());
			    				}
			    			}
		//	    			System.out.println("Local Depth: " + buckets.readInt());
		//	    			System.out.println("Entries count: " + buckets.readInt());
			    		} catch (IOException e) {
			    			System.out.println("Could not read int from bucket in listBucketEntries");
			    			e.printStackTrace();
			    		}
//		 			}
		 		}
	     }
}

