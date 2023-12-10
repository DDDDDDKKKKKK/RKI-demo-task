package colorBalanceCalculator;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit5 tests for the class ColorBalanceCalculator
 * Positive and negative for parsing, laser activation and validation
 * @author name David Ketels
 * @version 09.12.2023
 */
public class TestColorBalanceCalculator {
	
	// Given example data
	@Test
	public void parsingPositiveTest01() {
		ColorBalanceCalculator calculator = new ColorBalanceCalculator("GTCAGTCA,AGTAGTAC,CTCTGACA");
		char[][] expected_indexes = {{'G','T','C','A','G','T','C','A'},{'A','G','T','A','G','T','A','C'},{'C','T','C','T','G','A','C', 'A'}};
		Assert.assertTrue(Arrays.deepEquals(calculator.getIndexes(), expected_indexes));
	}
	
	// Max index length
	@Test
	public void parsingPositiveTest02() {
		ColorBalanceCalculator calculator = new ColorBalanceCalculator("GTCAAAGTAAGT,AGTACTCTCTCT");
		char[][] expected_indexes = {{'G','T','C','A','A','A','G','T','A','A','G','T'},{'A','G','T','A','C','T','C','T','C','T','C','T'}};
		Assert.assertTrue(Arrays.deepEquals(calculator.getIndexes(), expected_indexes));
	}
	
	// Many indexes
	@Test
	public void parsingPositiveTest03() {
		ColorBalanceCalculator calculator = new ColorBalanceCalculator("GTC,AAA,GTA,AGT,AGT,ACT,CTC,TCT");
		char[][] expected_indexes = {{'G','T','C'},{'A','A','A'},{'G','T','A'},{'A','G','T'},{'A','G','T'},{'A','C','T'},{'C','T','C'},{'T','C','T'}};
		Assert.assertTrue(Arrays.deepEquals(calculator.getIndexes(), expected_indexes));
	}
	
	// Only one index
	@Test
	public void parsingPositiveTest04() {
		ColorBalanceCalculator calculator = new ColorBalanceCalculator("AGTA");
		char[][] expected_indexes = {{'A','G','T','A'}};
		Assert.assertTrue(Arrays.deepEquals(calculator.getIndexes(), expected_indexes));
	}
	
	// Not a base
	@Test(expected = IllegalArgumentException.class)
	public void parsingNegativeTest01() {
		new ColorBalanceCalculator("ABCD,XCTG,CTCT");
	}
	
	// Dots not commas
	@Test(expected = IllegalArgumentException.class)
	public void parsingNegativeTest02() {
		new ColorBalanceCalculator("GTCA.AGTA.CTCT.GGGG");
	}
	
	// Comma at beginning
	@Test(expected = IllegalArgumentException.class)
	public void parsingNegativeTest03() {
		new ColorBalanceCalculator(",CTCA,AGTA");
	}
	
	// Comma at end
	@Test(expected = IllegalArgumentException.class)
	public void parsingNegativeTest04() {
		new ColorBalanceCalculator("GTCA.AGTA.CTCT,");
	}
	
	// Comma after comma
	@Test(expected = IllegalArgumentException.class)
	public void parsingNegativeTest05() {
		new ColorBalanceCalculator("GTCA,,AGTA,CTCT");
	}
	
	// No data
	@Test(expected = IllegalArgumentException.class)
	public void parsingNegativeTest06() {
		new ColorBalanceCalculator("");
	}
	
	// Indexes have not the same length
	@Test(expected = IllegalArgumentException.class)
	public void parsingNegativeTest07() {
		new ColorBalanceCalculator("GTCAA,AGTA,CTCT");
	}
	
	// Indexes are too long
	@Test(expected = IllegalArgumentException.class)
	public void parsingNegativeTest08() {
		new ColorBalanceCalculator("GTCAAAGTAAGTA,AGTACTCTCTCTG,CTCTGTCAAGTCA");
	}
	
	// Given example laser activation
	@Test
	public void laserActivationPostiveTest01(){
		ColorBalanceCalculator calculator = new ColorBalanceCalculator("GTCAGTCA,AGTAGTAC,CTCTGACA");
		
		int[][] expected_activation = {{2,1},
									   {0,2},
									   {2,1},
									   {2,3},
									   {0,0},
									   {1,3},
									   {3,1},
									   {3,2}};
		
		int[][] activation_result = calculator.computeCrossIndexColorCountsAndValidate().getCrossIndexesColorCounts();
		
		Assert.assertTrue(Arrays.deepEquals(activation_result, expected_activation));
	}
	
	
	@Test
	public void laserActivationPostiveTest02(){
		ColorBalanceCalculator calculator = new ColorBalanceCalculator("GTCA");
		
		int[][] expected_activation = {{0,0},
									   {0,1},
									   {1,0},
									   {1,1}};
		
		int[][] activation_result = calculator.computeCrossIndexColorCountsAndValidate().getCrossIndexesColorCounts();
		
		Assert.assertTrue(Arrays.deepEquals(activation_result, expected_activation));
	}
	
	
	@Test
	public void laserActivationPostiveTest03(){
		ColorBalanceCalculator calculator = new ColorBalanceCalculator("GGGG,GTCA");
		
		int[][] expected_activation = {{0,0},
									   {0,1},
									   {1,0},
									   {1,1}};
		
		int[][] activation_result = calculator.computeCrossIndexColorCountsAndValidate().getCrossIndexesColorCounts();
		
		Assert.assertTrue(Arrays.deepEquals(activation_result, expected_activation));
	}
	
	@Test
	public void laserActivationPostiveTest04(){
		ColorBalanceCalculator calculator = new ColorBalanceCalculator("GTCA,GGGG");
		
		int[][] expected_activation = {{0,0},
									   {0,1},
									   {1,0},
									   {1,1}};
		
		int[][] activation_result = calculator.computeCrossIndexColorCountsAndValidate().getCrossIndexesColorCounts();
		
		Assert.assertTrue(Arrays.deepEquals(activation_result, expected_activation));
	}
	
	@Test
	public void laserActivationPostiveTest05(){
		ColorBalanceCalculator calculator = new ColorBalanceCalculator("GTCA,GTCA");
		
		int[][] expected_activation = {{0,0},
									   {0,2},
									   {2,0},
									   {2,2}};
		
		int[][] activation_result = calculator.computeCrossIndexColorCountsAndValidate().getCrossIndexesColorCounts();
		
		Assert.assertTrue(Arrays.deepEquals(activation_result, expected_activation));
	}
	
	@Test
	public void ColorBalanceValidationPositiveTest01(){
		ColorBalanceCalculator calculator = new ColorBalanceCalculator("GTCAGTCA,AGTATTAC,CTCTGACA");
		
		boolean validation_result = calculator.computeCrossIndexColorCountsAndValidate().getVlidationResult();
		
		Assert.assertTrue(validation_result);
	}
	
	@Test
	public void ColorBalanceValidationPositiveTest02(){
		ColorBalanceCalculator calculator = new ColorBalanceCalculator("TTCAATCA");
		
		boolean validation_result = calculator.computeCrossIndexColorCountsAndValidate().getVlidationResult();
		
		Assert.assertTrue(validation_result);
	}
	
	@Test
	public void ColorBalanceValidationPositiveTest03(){
		ColorBalanceCalculator calculator = new ColorBalanceCalculator("TTCAATCA,GGGGGGGG");
		
		boolean validation_result = calculator.computeCrossIndexColorCountsAndValidate().getVlidationResult();
		
		Assert.assertTrue(validation_result);
	}
	
	@Test
	public void ColorBalanceValidationPositiveTest04(){
		ColorBalanceCalculator calculator = new ColorBalanceCalculator("TTCAATCG,GGGGGGGA");
		
		boolean validation_result = calculator.computeCrossIndexColorCountsAndValidate().getVlidationResult();
		
		Assert.assertTrue(validation_result);
	}
	
	// Given example color balance validation 
	@Test
	public void ColorBalanceValidationNegativeTest01(){
		ColorBalanceCalculator calculator = new ColorBalanceCalculator("GTCAGTCA,AGTAGTAC,CTCTGACA");
		
		boolean validation_result = calculator.computeCrossIndexColorCountsAndValidate().getVlidationResult();
		
		Assert.assertFalse(validation_result);
	}
	
	@Test
	public void ColorBalanceValidationNegativeTest02(){
		ColorBalanceCalculator calculator = new ColorBalanceCalculator("TTCAATCG,GGGGGGGG");
		
		boolean validation_result = calculator.computeCrossIndexColorCountsAndValidate().getVlidationResult();
		
		Assert.assertFalse(validation_result);
	}
	
	@Test
	public void ColorBalanceValidationNegativeTest03(){
		ColorBalanceCalculator calculator = new ColorBalanceCalculator("TGCAATCAAACA,AGCATTCATTCA,AGCATTCATTCA");
		
		boolean validation_result = calculator.computeCrossIndexColorCountsAndValidate().getVlidationResult();
		
		Assert.assertFalse(validation_result);
	}
}
