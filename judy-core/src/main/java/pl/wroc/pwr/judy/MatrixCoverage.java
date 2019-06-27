package pl.wroc.pwr.judy;

import java.util.ArrayList;

public class MatrixCoverage implements IMatrix {
	private static ArrayList<String> nameTests;//name of each test
	private static ArrayList<String> nameMutants;//name of each mutant
	private static ArrayList<ArrayList<Boolean>> matrixSuccess; //list of coverage between mutants and tests ( [IndexMutant][IndexTest] )
	//index of nameMutants and nameTests are equivalent to index of matrixSuccess
	public static int nbr=0;
	
	public MatrixCoverage() {
		nameTests = new ArrayList<String>();
		nameMutants = new ArrayList<String>();
		matrixSuccess = new ArrayList<ArrayList<Boolean>>();
		
	}
	
	public int SizeOfTests() {
		return nameTests.size();
	}
	
	public int SizeOfMutants() {
		return nameMutants.size();
	}	
	
	@Override
	public int sizeOfMutantsNotEquivalents() {
		int temp = 0;
		for (int i = 0; i<matrixSuccess.size();i++) {
			boolean equivalent = true;
			for (int j = 0; j<matrixSuccess.get(i).size();j++) {
				if(matrixSuccess.get(i).get(j)==true) {
					equivalent=false;
				}
			}
			if(equivalent==false) {
				temp++;
			}
		}
		return temp;
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
	

	public int ListOfTestsForAMutant(int mutant) {
		return matrixSuccess.get(mutant).size();
	}
	
	
	/*list of test for a mutant
	 * 
	 * input string , id of mutant
	 * 
	 * return List<boolean>
	 */
	@Override
	public ArrayList<Boolean> listOfTestsForAMutantById(String mutant){
		int ind =  matrixSuccess.indexOf(mutant);
		return listOfTestsForAMutant(ind);
	}
	
	/*list of test for a mutant
	 * 
	 * input int , int of mutant
	 * 
	 * return int
	 */
	@Override
	public int SizeOfTestsForAMutant(int mutant) {
		return nameMutants.size();
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
	public ArrayList<Boolean> listOfMutantsForATestByName(String test) {
		ArrayList<Boolean> temp = new ArrayList<Boolean>();
		for (int i = 0; i<matrixSuccess.size();i++) {
			if(matrixSuccess.get(i).indexOf(test)!=-1) {
				Boolean testtt = matrixSuccess.get(i).get(matrixSuccess.get(i).indexOf(test));
				temp.add(testtt);
			}
		}
		/*
		for (int i =0; i<matrixSuccess.size();i++) {
			temp.add(matrixSuccess.get(i).get(test));
		}*/
		return temp;
	}
	
	/*list of mutant for a test
	 * 
	 * input int , int of test
	 * 
	 * return List<boolean>
	 */
	@Override
	public int SizeOfMutantsForATest(int test) {
		int temp = 0;
		for (int i = 0; i<matrixSuccess.size();i++) {
			if(matrixSuccess.get(i).indexOf(test)!=-1) {
				temp++;
			}
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
	
	public void test() {

		System.out.println( "ttttttttttttttttttt");
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
		nbr++;
		//System.out.println("cover nbr : " + nbr + "\t mutant : " + mutant + "\t test : " + test + "\t success : " + success);
	}
	
	

}
