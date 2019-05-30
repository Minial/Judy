package pl.wroc.pwr.judy.operators.mno;

import org.objectweb.asm.MethodVisitor;
import pl.wroc.pwr.judy.operators.common.AbstractCoreMethodMutaionOperator;

/**
 * CompareTo method distortion.
 * <p/>
 * Add multiplication (-1) of return value in compareTo method
 *
 * @author mnowak
 */
public class CCD extends AbstractCoreMethodMutaionOperator {

	@Override
	public String getDescription() {
		return "compareTo method distortion";
	}

	@Override
	protected boolean checkMethod(String className, int access, String name, String desc, String signature,
								  String[] exceptions) {
		/*
		 * Class can implement Comparable<T> or Comparable. That is why
		 * expression in desc.contains() does not indicate Object or specific
		 * type (both are possible)
		 */
		return name.equals("compareTo") && access == ACC_PUBLIC && desc.contains(";)I");
	}

	@Override
	protected boolean checkInsn(final String className, final int opcode) {
		return opcode == IRETURN;
	}

	@Override
	protected void mutateInsn(final MethodVisitor mv, final String className, final int opcode) {
		mv.visitInsn(ICONST_M1);
		mv.visitInsn(IMUL);
		mv.visitInsn(opcode);
	}
}
