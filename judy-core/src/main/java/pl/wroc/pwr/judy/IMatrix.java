package pl.wroc.pwr.judy;


import java.util.ArrayList;

/*
 * interface for coverage & execution matrix
 * 
 * if arraylist not working on multi-thread, use CopyOnWriteArrayList
 */

public interface IMatrix {
	
	/*list of tests
	 * 
	 * input void
	 * 
	 * return List<String>
	 */
	ArrayList<String> listOfTests();
	
	/*number of mutant not equivalent
	 * 
	 */
	int sizeOfMutantsNotEquivalents();
	
	/*list of test for a mutant
	 * 
	 * input int , int of mutant
	 * 
	 * return List<boolean>
	 */
	ArrayList<Boolean> listOfTestsForAMutant(int mutant);
	
	/*list of test for a mutant
	 * 
	 * input string , id of mutant
	 * 
	 * return List<boolean>
	 */
	ArrayList<Boolean> listOfTestsForAMutantById(String mutant);
	
	/*list of test for a mutant
	 * 
	 * input int , int of mutant
	 * 
	 * return int
	 */
	int SizeOfTestsForAMutant(int mutant);
	
	/* list of mutants
	 * 
	 * input void
	 * 
	 * return List<String>
	 */
	ArrayList<String> listOfMutants();
	
	/*list of mutant for a test
	 * 
	 * input int , int of test
	 * 
	 * return List<boolean>
	 */
	ArrayList<Boolean> listOfMutantsForATestByName(String test);
	
	/*list of mutant for a test
	 * 
	 * input int , int of test
	 * 
	 * return List<boolean>
	 */
	int SizeOfMutantsForATest(int test);
	
	/* check if a mutants is Triggered by a test
	 * 
	 * input int,int , mutant, test
	 * 
	 * return boolean
	 */
	boolean checkTrigger(int mutant, int test);
	
	

}
