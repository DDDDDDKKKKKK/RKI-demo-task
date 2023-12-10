package colorBalanceCalculator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

/**
 * Usage example of the class ColorBalanceCalculator
 * 
 * @author name David Ketels
 * @version 09.12.2023
 */
public class MainColorBalanceCalculator {
	
	/**
	 * Reads the indexes stored in indexes.txt and prints the laser activations per cycle and the validation result to the console
	 */
    public static void main (String[] args)
    {
    	String fileName = "indexes.txt";
    	
    	try(FileInputStream inputStream = new FileInputStream(fileName)) {    
    	    String indexes = IOUtils.toString(inputStream);
        	
    	    ColorBalanceCalculator calculator = new ColorBalanceCalculator(indexes);
        	
        	calculator.computeCrossIndexColorCountsAndValidate();
        	
        	calculator.printColorCountsAndValidationToConsole();
    	    
    	} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

}
