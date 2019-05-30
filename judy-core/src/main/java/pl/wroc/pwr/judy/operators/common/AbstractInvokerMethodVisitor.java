package pl.wroc.pwr.judy.operators.common;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.LocalVariablesSorter;

import java.util.HashSet;
import java.util.Set;

/**
 * Base class for method visitors invoking custom method on result just before
 * return from mutated method.
 *
 * @author TM
 */
public abstract class AbstractInvokerMethodVisitor extends LocalVariablesSorter implements MutationObservable {
	private int lastOpcode;
	private int lastVar;

	private String lastOwner;
	private String lastName;
	private String lastDesc;
	private int lastLine;
	private Type methodReturnType;
	private Set<MutationObserver> observers = new HashSet<>();

	/**
	 * @param mv     method visitor
	 * @param access method access opcode
	 * @param desc   method description
	 */
	public AbstractInvokerMethodVisitor(MethodVisitor mv, int access, String desc) {
		super(Opcodes.ASM5, access, desc, mv);
		methodReturnType = Type.getReturnType(desc);
	}

	@Override
	public void addObserver(MutationObserver observer) {
		observers.add(observer);
	}

	@Override
	public void notifyObservers() {
		for (MutationObserver observer : observers) {
			observer.onMutation(lastLine);
		}
	}

	@Override
	public void visitFieldInsn(final int opcode, final String owner, final String name, final String desc) {
		lastOpcode = opcode;
		lastOwner = owner;
		lastName = name;
		lastDesc = desc;
		mv.visitFieldInsn(opcode, owner, name, desc);
	}

	@Override
	public void visitCode() {
		mv.visitCode();

	}

	@Override
	public void visitVarInsn(final int opcode, final int var) {
		lastOpcode = opcode;
		lastVar = var;
		mv.visitVarInsn(opcode, var);
	}

	@Override
	public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc, boolean itf) {
		lastOpcode = opcode;
		lastOwner = owner;
		lastName = name;
		lastDesc = desc;
		mv.visitMethodInsn(opcode, owner, name, desc, itf);
	}

	@Override
	public void visitLineNumber(int line, Label start) {
		lastLine = line;
		super.visitLineNumber(line, start);
	}

	@Override
	public void visitInsn(final int opcode) {
		if (opcode == Opcodes.ARETURN) {

			notifyObservers();

			switch (lastOpcode) {
				case Opcodes.ALOAD:
					handleLocalVariable();
					break;
				case Opcodes.GETFIELD:
					handleField();
					break;
				case Opcodes.GETSTATIC:
					handleStatic();
					break;
				case Opcodes.INVOKESTATIC:
					handleInvocation();
					break;
				case Opcodes.INVOKEVIRTUAL:
					handleInvocation();
					break;
				case Opcodes.INVOKESPECIAL:
					handleInvocation();
					break;
				case Opcodes.INVOKEINTERFACE:
					handleInvocation();
					break;
			}
		}
		mv.visitInsn(opcode);
	}

	protected void handleStatic() {
		invoke();
		mv.visitFieldInsn(Opcodes.GETSTATIC, lastOwner, lastName, lastDesc);
	}

	protected void handleLocalVariable() {
		invoke();
		mv.visitVarInsn(Opcodes.ALOAD, lastVar);
	}

	protected void handleField() {
		handleLocalVariable();
		mv.visitFieldInsn(Opcodes.GETFIELD, lastOwner, lastName, lastDesc);
	}

	protected void handleInvocation() {
		String description = lastDesc.substring(2);
		int helperIndex = newLocal(Type.getObjectType(description));

		mv.visitVarInsn(Opcodes.ASTORE, helperIndex);
		mv.visitVarInsn(Opcodes.ALOAD, helperIndex);
		invoke();
		mv.visitVarInsn(Opcodes.ALOAD, helperIndex);
	}

	/**
	 * @return the last method/field description
	 */
	protected String getLastDescription() {
		return lastDesc;
	}

	/**
	 * @return the lastOwner
	 */
	protected String getLastOwner() {
		return lastOwner;
	}

	/**
	 * @return the lastName
	 */
	protected String getLastName() {
		return lastName;
	}

	/**
	 * @return the lastVar
	 */
	protected int getLastVar() {
		return lastVar;
	}

	/**
	 * Invokes custom method just before return from mutated method.
	 */
	protected abstract void invoke();

	/**
	 * Gets first line number where mutation was applied
	 *
	 * @return mutation insertion line
	 */
	public int getMutatedLineNumber() {
		return lastLine;
	}

	/**
	 * Gets mutated method return type
	 *
	 * @return the methodReturnType
	 */
	public Type getMethodReturnType() {
		return methodReturnType;
	}
}