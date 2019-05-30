package pl.wroc.pwr.judy.operators.javaspec;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import pl.wroc.pwr.judy.operators.common.AbstractCoreMethodMutaionOperator;

/**
 * Reference comparison and content comparison replacement.
 * <p/>
 * Replace reference comparison with content comparison (<code>.equals()</code>)
 * and vice-versa.
 *
 * @author pmiwaszko
 */
public class EOC extends AbstractCoreMethodMutaionOperator {
	@Override
	public String getDescription() {
		return "replace reference comparison with content comparison (equals) and vice-versa"; // equals()
	}

	@Override
	protected boolean checkJumpInsn(String className, int opcode, Label label) {
		return opcode == IF_ACMPEQ || opcode == IF_ACMPNE;
	}

	@Override
	protected void mutateJumpInsn(MethodVisitor mv, String className, int opcode, Label label) {
		// insert equals() method
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "equals", "(Ljava/lang/Object;)Z", false);
		if (opcode == IF_ACMPEQ) {
			mv.visitJumpInsn(IFNE, label);
		} else {
			mv.visitJumpInsn(IFEQ, label);
		}
	}

	@Override
	protected boolean checkMethodInsn(String className, int opcode, String owner, String name, String desc) {
		return opcode == INVOKEVIRTUAL && "equals".equals(name) && "(Ljava/lang/Object;)Z".equals(desc);
	}

	@Override
	protected void mutateMethodInsn(MethodVisitor mv, String className, int opcode, String owner, String name,
									String desc, boolean itf) {
		// insert jump instruction
		Label labelTrue = new Label();
		Label labelEnd = new Label();

		mv.visitJumpInsn(IF_ACMPEQ, labelTrue);
		mv.visitInsn(ICONST_0);
		mv.visitJumpInsn(GOTO, labelEnd);
		mv.visitLabel(labelTrue);
		mv.visitInsn(ICONST_1);
		mv.visitLabel(labelEnd);
	}
}
