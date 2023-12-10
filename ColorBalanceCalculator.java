package colorBalanceCalculator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Simple Color-Balance Calculator for DNA-sequences. 
 * The cross sequence (index) count of laser triggers (RED or GREEN) for each sequence base position is calculated. 
 * Further more it is checkd if multiple artificial DNA-sequences are in combination valid: success or failure.
 * 
 * @author name David Ketels
 * @version 09.12.2023
 */
public class ColorBalanceCalculator {
	
	private boolean validationResult;
	private int indexLength;
	private int numberOfIndexes;
	private char[][] indexes;
	private int[][] crossIndexesColorCounts;
	
	
	private int getIndexLength() {
		return indexLength;
	}


	private void setIndexLength(int indexLength) {
		this.indexLength = indexLength;
	}
	
	private int getNumberOfIndexes() {
		return numberOfIndexes;
	}


	private void setNumberOfIndexes(int numberOfIndexes) {
		this.numberOfIndexes = numberOfIndexes;
	}


	public char[][] getIndexes() {
		return indexes;
	}


	private void setIndexes(char[][] indexes) {
		this.indexes = indexes;
	}
	
	public int[][] getCrossIndexesColorCounts() {
		return crossIndexesColorCounts;
	}
	
	public boolean getVlidationResult() {
		return validationResult;
	}


	private void setVlidationResult(boolean validationResult) {
		this.validationResult = validationResult;
	}


	private void setCrossIndexesColorCounts(int[][] crossIndexesColorCounts) {
		this.crossIndexesColorCounts = crossIndexesColorCounts;
	}
	
	/**
	 * Creates a new ColorBalanceCalculator object for given Indexes.
	 * @param commaSepperatedIndexes A string with all Indexes separated by commas. e.g.: "GTCAGTCA,AGTAGTAC,CTCTGACA"
	 */
	public ColorBalanceCalculator(String commaSepperatedIndexes) {
		this.checkIfStringIsParsable(commaSepperatedIndexes);
		this.parseAndSetIndexes(commaSepperatedIndexes);
	}
	
	/**
	 * Computes the Color activation counts across the indexes for each position. 
	 * Afterwards the result is validated.
	 * @reutrn the updated ColorBalanceCalculator object
	 */
	public ColorBalanceCalculator computeCrossIndexColorCountsAndValidate() {
		this.computeCrossIndexColorCounts();
		this.validate();
		return this;
	}
	
	/**
	 * prints a table to the console where for each cycle the red and green laser activation count is given. 
	 * The validation result is also printed at the end. 
	 */
	public void printColorCountsAndValidationToConsole() {
		if (this.getCrossIndexesColorCounts() != null) {
	    	// Print color count results to console
	    	System.out.println("CYCLE | RED | GREEN");
	    	
	    	for (int i = 0; i < this.getIndexLength(); i++) {
	    		
	    		System.out.println((i + 1) + " | " + (this.getCrossIndexesColorCounts()[i][0]) + " | " + (this.getCrossIndexesColorCounts()[i][1]));
	    	}
	    	
	    	// Print evaluation results to console
	        if(this.getVlidationResult()){
	        	System.out.println("Evaluation: Success");
	    	}
	        else
	        {
	        	System.out.println("Evaluation: Failure");
	        }
		}
	}
    
	/**
	 * Checks if the given string can be parsed into indexes.
	 * @param indexesToParse
	 * @throws IllegalArgumentException when string can not be parsed
	 */
	private void checkIfStringIsParsable(String indexesToParse) throws IllegalArgumentException {
    	// Check if only valid characters are passed
        Pattern validCharacterPattern = Pattern.compile("[^CGTA\\,]");
        Matcher matcher = validCharacterPattern.matcher(indexesToParse);
        
        boolean nonValidCharacter = matcher.find();
        
        if(nonValidCharacter) {
	    	String message = "The indexes have to be passed in a specififc comma seperated format. "
	    					+ "Only the characters ['C', 'G', 'T', 'A', ','] are allowed and";
	        throw new IllegalArgumentException(message);
        }
	}
	
    /**
     * Checks if the given string can be parsed into indexes.
     * Dose the parsing and sets the object parameters: numberOfIndexes, indexLength, indexes. 
     * @param indexes
     * @return The parsed indexes in a two dimensional array
     */
    private void parseAndSetIndexes(String indexesToParse) throws IllegalArgumentException{
    	    
    	// Extract and check number and length of indexes
        String[] indexesAsStrings = indexesToParse.split("\\,");
        
    	this.setNumberOfIndexes(indexesAsStrings.length);
    	this.setIndexLength(indexesAsStrings[0].length());
    	
    	if (this.getNumberOfIndexes() < 1 || this.getIndexLength() < 1) {
	    	String message = "At least on index with at least the length one needs to be passed";
	        throw new IllegalArgumentException(message);
    	}
    	
    	// Extract indexes from arguments and check if they have all the same length
    	char[][] indexes = new char[this.getNumberOfIndexes()][this.getIndexLength()];
         			
    	for (int index_position = 0; index_position < getNumberOfIndexes(); index_position++) {
    		
    		String current_index = indexesAsStrings[index_position];
    		
    		if(current_index.length() > 12 || current_index.length() != getIndexLength()) {
    	    	String message = "All indexes shall be of the same length and be no longer than 12 bases";
    	        throw new IllegalArgumentException(message);
    		}
    		
    		indexes[index_position] = indexesAsStrings[index_position].toCharArray();
    	}
    	
    	this.setIndexes(indexes);
    }
    
    /**
     * Computes for each index (base sequence) and across each position (base) the laser color counts.
     * Updates the CrossIndexesColorCounts parameter of the object.
     * @param indexes
     * @return laser color counts. Position 0: Red-Laser-Photo. Position 1: Green-Laser-Phot.
     */
    private void computeCrossIndexColorCounts() {
    	
    	int[][] crossIndexesCountResult = new int[getIndexLength()][2];
    	
    	int[] lightActivation = new int[2];
    	char base;
    	
    	for (int index = 0; index < getNumberOfIndexes(); index++) {
    		
        	for (int position = 0; position < getIndexLength(); position++) {
        		
        		base = indexes[index][position];
        		lightActivation = redGreenLightActivation(base);
        		
        		crossIndexesCountResult[position][0] = crossIndexesCountResult[position][0] + lightActivation[0];
        		crossIndexesCountResult[position][1] = crossIndexesCountResult[position][1] + lightActivation[1];
        	}
    	}
    	
    	this.setCrossIndexesColorCounts(crossIndexesCountResult);
    }
    
    /**
     * Computes the laser activation function for one base
     * @param base
     * @return red and green light activation
     */
    private int[] redGreenLightActivation(char base) {
    	int reedLight  = 0;
    	int greenLight = 0;
    	
    	int[] lightActivation = new int[2];
    	
    	switch (base) {
    	  case 'G':
    		  reedLight  = 0;
    		  greenLight = 0;
    		  break;
    	  case 'T':
    		  reedLight  = 0;
    		  greenLight = 1;
    		  break;
    	  case 'C':
    		  reedLight  = 1;
    		  greenLight = 0;
    		  break;
    	  case 'A':
    		  reedLight  = 1;
    		  greenLight = 1;
    		  break;
    	}
    	
    	lightActivation[0] = reedLight;
    	lightActivation[1] = greenLight;
    	
    	return lightActivation;
    }
    
    /**
     * Evaluates the laser activation of indexes
     * @param laserActivationCounts
     * @return boolean evaluation result
     */
    private void validate(){
    	for (int position = 0; position < getIndexLength(); position++) {
    		if (this.getCrossIndexesColorCounts()[position][0] + this.getCrossIndexesColorCounts()[position][1] < 1) {
    			this.setVlidationResult(false);
    			return;
    		}
    	}
    	this.setVlidationResult(true);
    }
}