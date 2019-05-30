package pl.wroc.pwr.judy.research;

import org.objectweb.asm.*;

import java.util.List;

public final class ResearchTestSuiteGenerator implements Opcodes {

	private ResearchTestSuiteGenerator() {
	}

	public static byte[] createSuite(String project, List<String> tests) throws Exception {
		ClassWriter cw = new ClassWriter(0);
		MethodVisitor mv;

		cw.visit(V1_6, ACC_PUBLIC + ACC_SUPER, project + "TestSuite", null, "junit/framework/TestCase", null);

		cw.visitSource(project + "TestSuite.java", null);
		{
			mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(5, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESPECIAL, "junit/framework/TestCase", "<init>", "()V", false);
			mv.visitInsn(RETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("this", "L" + project + "TestSuite;", null, l0, l1, 0);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "suite", "()Ljunit/framework/Test;", null, null);
			mv.visitCode();

			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(8, l0);
			mv.visitTypeInsn(NEW, "junit/framework/TestSuite");
			mv.visitInsn(DUP);
			mv.visitLdcInsn("Judy Test Suite");
			mv.visitMethodInsn(INVOKESPECIAL, "junit/framework/TestSuite", "<init>", "(Ljava/lang/String;)V", false);
			mv.visitVarInsn(ASTORE, 0);

			Label label0 = null;

			int lineNumber = 10;
			for (String test : tests) {
				Label label = new Label();
				if (label0 == null) {
					label0 = label;
				}
				mv.visitLabel(label);
				mv.visitLineNumber(lineNumber++, label);
				mv.visitVarInsn(ALOAD, 0);
				String testName = "L" + test.replace('.', '/') + ";";
				mv.visitLdcInsn(Type.getType(testName));
				mv.visitMethodInsn(INVOKEVIRTUAL, "junit/framework/TestSuite", "addTestSuite", "(Ljava/lang/Class;)V",
						false);
			}

			Label l20 = new Label();
			mv.visitLabel(l20);
			mv.visitLineNumber(30, l20);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitInsn(ARETURN);
			Label l21 = new Label();
			mv.visitLabel(l21);
			mv.visitLocalVariable("suite", "Ljunit/framework/TestSuite;", null, label0, l21, 0);
			mv.visitMaxs(3, 1);
			mv.visitEnd();
		}
		cw.visitEnd();

		return cw.toByteArray();
	}
}
