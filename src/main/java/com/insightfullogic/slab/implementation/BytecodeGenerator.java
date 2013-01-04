package com.insightfullogic.slab.implementation;

import static org.objectweb.asm.Type.LONG_TYPE;

import java.lang.reflect.Method;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.util.CheckClassAdapter;

import sun.misc.Unsafe;

import com.insightfullogic.slab.Cursor;

@SuppressWarnings("restriction")
public class BytecodeGenerator<T extends Cursor> implements Opcodes {
	
	private static final String UNSAFE_NAME = Type.getInternalName(Unsafe.class);
	private static final String UNSAFE_DESCRIPTOR = Type.getType(Unsafe.class).getDescriptor();
	
	private static final byte TRUE = 1, FALSE = 0;

    private final TypeInspector inspector;
    private final Class<T> representingKlass;
	private final String implementationName;

    public BytecodeGenerator(TypeInspector inspector, Class<T> representingKlass) {
        this.inspector = inspector;
        this.representingKlass = representingKlass;
        implementationName = "DirectMemory" + representingKlass.getSimpleName();
    }

    @SuppressWarnings("unchecked")
	public Class<T> generate() {
    	ClassWriter out = new ClassWriter(ClassWriter.COMPUTE_MAXS);
    	CheckClassAdapter writer = new CheckClassAdapter(out);
    	
		int offset = 0;
    	declareClass(writer);
    	declareConstructor(writer);
    	for (Method getter : inspector.getters) {
    		offset = declareField(getter, writer, offset);
    	}
    	
    	writer.visitEnd();
    	
        return (Class<T>) new GeneratedClassLoader().defineClass(implementationName, out);
    }

    private void declareClass(ClassVisitor writer) {
    	String classExtended = DirectMemoryCursor.INTERNAL_NAME;
    	String[] interfacesImplemented = new String[] { Type.getInternalName(representingKlass) };
    	writer.visit(V1_6, ACC_PUBLIC + ACC_SUPER, implementationName, null, classExtended, interfacesImplemented);
    }

    private void declareConstructor(CheckClassAdapter writer) {
    	MethodVisitor method = writer.visitMethod(ACC_PUBLIC, "<init>", "(I)V", null, null);
    	method.visitCode();
		method.visitVarInsn(ALOAD, 0);
		method.visitVarInsn(ILOAD, 1);
		method.visitLdcInsn(inspector.getSizeInBytes());
		method.visitMethodInsn(INVOKESPECIAL,
				DirectMemoryCursor.INTERNAL_NAME,
				"<init>",
				"(II)V");
		method.visitInsn(RETURN);
		method.visitMaxs(2, 2);
		method.visitEnd();
    }

	private int declareField(Method getter, ClassVisitor writer, int fieldOffset) {
		Primitive type = inspector.getReturn(getter);

		MethodVisitor implementingGetter = declareMethod(getter, writer);
		declareGetterBody(fieldOffset, type, implementingGetter);

		Method setter = inspector.setterFor(getter);
		MethodVisitor implementingSetter = declareMethod(setter, writer);
		declareSetterBody(fieldOffset, type, implementingSetter);

		return fieldOffset + type.sizeInBytes;
	}

	private MethodVisitor declareMethod(Method method, ClassVisitor writer) {
		String name = method.getName();
		String descriptor = Type.getMethodDescriptor(method);
		return writer.visitMethod(ACC_PUBLIC, name, descriptor, null, null);
	}

	private void declareGetterBody(int fieldOffset, Primitive type, MethodVisitor method) {
		method.visitCode();
		declareUnsafe(fieldOffset, method);
		
		// unsafe.getLong
		String unsafeGetter = "get" + type.unsafeMethodSuffix();
		String unsafeDescriptor = getUnsafeMethodDescriptor(unsafeGetter, Long.TYPE);
		method.visitMethodInsn(INVOKEVIRTUAL, UNSAFE_NAME, unsafeGetter, unsafeDescriptor);

		method.visitInsn(type.returnOpcode);
		method.visitMaxs(0, 0);
		method.visitEnd();
	}

	private void declareSetterBody(int fieldOffset, Primitive type, MethodVisitor method) {
		method.visitCode();
		Label start = new Label();
		method.visitLabel(start);
		declareUnsafe(fieldOffset, method);

		// load parameter 1
		method.visitVarInsn(type.loadOpcode, 1);

		// unsafe.putLong
		String unsafeSetter = "put" + type.unsafeMethodSuffix();
		String unsafeDescriptor = getUnsafeMethodDescriptor(unsafeSetter, Long.TYPE, type.javaEquivalent);
		method.visitMethodInsn(INVOKEVIRTUAL, UNSAFE_NAME, unsafeSetter, unsafeDescriptor);

		Label end = new Label();
		method.visitLabel(end);

		method.visitInsn(RETURN);
		
		method.visitLocalVariable("value", Type.getDescriptor(type.javaEquivalent), null, start, end, 0);
		method.visitMaxs(0, 0);
		method.visitEnd();
	}

	private void declareUnsafe(int fieldOffset, MethodVisitor method) {
		// DirectMemoryCursor.unsafe
		method.visitFieldInsn(GETSTATIC, DirectMemoryCursor.INTERNAL_NAME, "unsafe", UNSAFE_DESCRIPTOR);

		// this.pointer  + fieldOffset
		method.visitVarInsn(ALOAD, 0);
		method.visitFieldInsn(GETFIELD, implementationName, "pointer", LONG_TYPE.getDescriptor());
		method.visitLdcInsn((long)fieldOffset);
		method.visitInsn(LADD);
	}

	private String getUnsafeMethodDescriptor(String methodName, Class<?> ... types) {
		try {
			Method method = Unsafe.class.getMethod(methodName, types);
			return Type.getMethodDescriptor(method);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

}
