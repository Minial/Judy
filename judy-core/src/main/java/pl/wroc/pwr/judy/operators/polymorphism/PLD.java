package pl.wroc.pwr.judy.operators.polymorphism;

import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodNode;
import pl.wroc.pwr.judy.operators.common.AbstractLocalVariableTypeReplacementOperator;

/**
 * Local variable declaration with super class type.
 * <p/>
 * Change declared type of a local object reference to be that of the parent of
 * its original declared type.
 *
 * @author pmiwaszko
 */
public class PLD extends AbstractLocalVariableTypeReplacementOperator {

	@Override
	public String getDescription() {
		return "change local variable type to super class of original type";
	}

	@Override
	protected boolean checkVariable(LocalVariableNode variable, MethodNode mn, int i, String className) {
		return !"this".equals(variable.name) && !variable.name.startsWith("this$")
				&& hasSuperType(getType(variable), getSuperType(variable)) && !isMethodArgument(mn, className, i);
	}

}