package pl.wroc.pwr.judy.operators.guards;

import org.objectweb.asm.*;
import org.objectweb.asm.commons.LocalVariablesSorter;
import org.objectweb.asm.tree.*;
import pl.wroc.pwr.judy.operators.AbstractTreeClassMutater;
import pl.wroc.pwr.judy.operators.AbstractTreeMethodMutater;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class InfiniteLoopGuardClassMutator extends AbstractTreeClassMutater {
	private long timeout;

	/**
	 * Constructs new class mutator object for mutating classes.
	 *
	 * @param cv                 class visitor
	 * @param mutationPointIndex mutation point index
	 * @param mutantIndex        mutant index
	 * @param timeout            maximum loop execution time before interruption
	 */
	public InfiniteLoopGuardClassMutator(ClassVisitor cv, int mutationPointIndex, int mutantIndex, long timeout) {
		super(cv, mutationPointIndex, mutantIndex);
		this.timeout = timeout;
	}

	/**
	 * Constructs new class mutator object for counting mutation points.
	 */
	public InfiniteLoopGuardClassMutator() {
		super();
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
		return new LoopFinder(mv, access, name, desc, signature, exceptions, timeout);
	}

	@Override
	protected void mutateClass(ClassNode cn) {
	}

	private class LoopInfo {
		private Label start;

		public LoopInfo(Label start) {
			this.start = start;
		}

		public Label getStart() {
			return start;
		}
	}

	private class LoopFinder extends AbstractTreeMethodMutater {
		private List<LoopInfo> loops = new ArrayList<>();
		private List<Label> labels = new ArrayList<>();

		private MethodVisitor loopMutator;

		public LoopFinder(MethodVisitor mv, int access, String name, String desc, String signature,
						  String[] exceptions, long timeout) {
			super(mv, access, name, desc, signature, exceptions);
			loopMutator = new LoopMutator(mv, access, desc, timeout, loops);
		}

		@Override
		public void visitEnd() {
			try {
				mutate(methodNode);

				if (!methodHasNoLoops() && shouldMutate()) {
					methodNode.accept(loopMutator);
				} else {
					methodNode.accept(mv);
				}
			} catch (Exception e) {
				e.printStackTrace();
				generateError();
			}
		}

		private boolean methodHasNoLoops() {
			return loops.isEmpty();
		}

		@Override
		protected void mutate(MethodNode mn) throws Exception {
			analyze(mn);
		}

		private List<LoopInfo> analyze(MethodNode mn) {
			ListIterator<?> iter = mn.instructions.iterator();

			while (iter.hasNext()) {
				AbstractInsnNode node = (AbstractInsnNode) iter.next();
				if (node.getType() == AbstractInsnNode.LABEL) {
					LabelNode label = (LabelNode) node;
					labels.add(label.getLabel());
				} else if (node.getType() == AbstractInsnNode.JUMP_INSN) {
					JumpInsnNode jump = (JumpInsnNode) node;
					if (labels.contains(jump.label.getLabel()) && JumpOpcodeVerifier.verify(jump.getOpcode())) {
						loops.add(new LoopInfo(jump.label.getLabel()));
					}
				}
			}
			if (!methodHasNoLoops()) {
				nextMutationPoint();
			}
			return loops;
		}
	}

	private class LoopMutator extends LocalVariablesSorter {

		private static final String EXCEPTION_CONSTRUCTOR = "(Ljava/lang/String;)V";
		private static final String EXCEPTION_TYPE = "pl/wroc/pwr/judy/operators/guards/InfiniteLoopException";
		private static final String INIT = "<init>";
		private static final String CURRENT_TIME_MILLIS_DESC = "()J";
		private static final String CURRENT_TIME_MILLIS = "currentTimeMillis";
		private static final String JAVA_LANG_SYSTEM = "java/lang/System";
		private static final String EXCEPTION_DESCRIPTION = "Suspicious loop interrupted by Judy's Infinite Loop Guard";

		private List<LoopInfo> loops;
		private long timeout;

		private int judyTimerFieldIndex;
		private List<Label> labels = new ArrayList<>();

		public LoopMutator(MethodVisitor mv, int access, String desc, long timeout, List<LoopInfo> loops) {
			super(Opcodes.ASM5, access, desc, mv);
			this.timeout = timeout;
			this.loops = loops;
		}

		@Override
		public void visitCode() {
			super.visitCode();
			storeCurrentTimeInLocalVariable();
		}

		private void storeCurrentTimeInLocalVariable() {
			judyTimerFieldIndex = newLocal(Type.LONG_TYPE);
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, JAVA_LANG_SYSTEM, CURRENT_TIME_MILLIS, CURRENT_TIME_MILLIS_DESC,
					false);
			mv.visitVarInsn(Opcodes.LSTORE, judyTimerFieldIndex);
		}

		@Override
		public void visitLineNumber(int line, Label start) {
			super.visitLineNumber(line, start);
			LoopInfo loop = getLoopStartingAfterAlreadyVisitedLabel();
			if (loop != null) {
				compareCurrentAndVariableTime();
				Label labelBeforeIf = createIfStatement();
				createIfBodyThrowingException();
				mv.visitLabel(labelBeforeIf);
			}
		}

		private void createIfBodyThrowingException() {
			Label labelWithException = new Label();
			mv.visitLabel(labelWithException);
			mv.visitTypeInsn(Opcodes.NEW, EXCEPTION_TYPE);
			mv.visitInsn(Opcodes.DUP);
			mv.visitLdcInsn(EXCEPTION_DESCRIPTION);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, EXCEPTION_TYPE, INIT, EXCEPTION_CONSTRUCTOR, false);
			mv.visitInsn(Opcodes.ATHROW);
		}

		private Label createIfStatement() {
			Label labelBeforeIf = new Label();
			mv.visitJumpInsn(Opcodes.IFLE, labelBeforeIf);
			return labelBeforeIf;
		}

		private void compareCurrentAndVariableTime() {
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, JAVA_LANG_SYSTEM, CURRENT_TIME_MILLIS, CURRENT_TIME_MILLIS_DESC,
					false);
			mv.visitIntInsn(Opcodes.LLOAD, judyTimerFieldIndex);
			mv.visitInsn(Opcodes.LSUB);
			mv.visitLdcInsn(timeout);
			mv.visitInsn(Opcodes.LCMP);
		}

		@Override
		public void visitLabel(Label label) {
			super.visitLabel(label);
			labels.add(label);
		}

		private LoopInfo getLoopStartingAfterAlreadyVisitedLabel() {
			for (Iterator<LoopInfo> it = loops.iterator(); it.hasNext(); ) {
				LoopInfo loop = it.next();
				if (labels.contains(loop.getStart())) {
					it.remove();
					return loop;
				}
			}
			return null;
		}
	}
}
