package pl.wroc.pwr.judy;

import java.util.ArrayList;

public class MatrixCoverage implements IMatrix {
	private ArrayList<String> nameTests;//name of each test
	private ArrayList<String> nameMutants;//name of each mutant
	private ArrayList<ArrayList<Boolean>> matrixSuccess; //list of coverage between mutants and tests ( [IndexMutant][IndexTest] )
	//index of nameMutants and nameTests are equivalent to index of matrixSuccess
	
	public MatrixCoverage() {
		nameTests = new ArrayList<String>();
		nameMutants = new ArrayList<String>();
		matrixSuccess = new ArrayList<ArrayList<Boolean>>();
		
	}
	
	/*list of tests
	 * 
	 * input void
	 * 
	 * return List<String>
	 */
	@Override
	public ArrayList<String> listOfTests() {
		return nameTests;
	}
	
	/*list of test for a mutant
	 * 
	 * input int , int of mutant
	 * 
	 * return List<boolean>
	 */
	@Override
	public ArrayList<Boolean> listOfTestsForAMutant(int mutant) {
		return matrixSuccess.get(mutant);
	}
	
	/* list of mutants
	 * 
	 * input void
	 * 
	 * return List<String>
	 */
	@Override
	public ArrayList<String> listOfMutants() {
		return nameMutants;
	}
	
	/*list of mutant for a test
	 * 
	 * input int , int of test
	 * 
	 * return List<boolean>
	 */
	@Override
	public ArrayList<Boolean> listOfMutantsForATest(int test) {
		ArrayList<Boolean> temp = new ArrayList<Boolean>();
		for (int i =0; i<matrixSuccess.size();i++) {
			temp.add(matrixSuccess.get(i).get(test));
		}
		return temp;
	}
	
	/* check if a mutants is Triggered by a test
	 * 
	 * input int,int , mutant, test
	 * 
	 * return boolean
	 */
	@Override
	public boolean checkTrigger(int mutant, int test) {
		return matrixSuccess.get(mutant).get(test);
		
	}
	
	public void addResult(String mutant, String test, boolean success) {
		boolean mutantAlreadyExist = true;
		//boolean testAlreadyExist = true;
		int indexOfMutant;
		int indexOfTest;
		indexOfMutant=nameMutants.indexOf(mutant);
		if(indexOfMutant==-1) {//if the mutant doesn't exist
			mutantAlreadyExist=false;
			indexOfMutant = nameMutants.size();			
		}
		indexOfTest=nameTests.indexOf(test);
		if(indexOfTest==-1) {
			//testAlreadyExist=false;
			indexOfTest = nameTests.size();
		}
		if(mutantAlreadyExist) {
			matrixSuccess.get(indexOfMutant).add(indexOfTest, success);	//beware of the out of range exceptions
		}
		else {
			ArrayList<Boolean> temp = new ArrayList<Boolean>();
			temp.add(indexOfTest, success);
			matrixSuccess.add(indexOfMutant, temp);
		}
	}
	
	

}
