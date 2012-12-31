package com.insightfullogic.slab.implementation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.objectweb.asm.ClassWriter;

class GeneratedClassLoader extends ClassLoader {

	synchronized Class<?> defineClass(final String binaryName, final ClassWriter cw) {
		final byte[] b = cw.toByteArray();
		if (true) {
			final File f = new File(System.getProperty("user.home") + File.separator + "dump" + File.separator
					+ binaryName.replace('.', File.separatorChar) + ".class");
			final File dir = f.getParentFile();
			if ((dir.exists() && dir.isDirectory()) || dir.mkdirs()) {
				OutputStream out = null;
				try {
					out = new FileOutputStream(f);
					out.write(b);
					out.flush();
				} catch (final IOException e) {
					e.printStackTrace();
				} finally {
					if (out != null) {
						try {
							out.close();
						} catch (final IOException e) {
							e.printStackTrace();
						}
					}
				}
			} else {
				throw new IllegalStateException("Unable to create directory: " + f.getParent());
			}
		}
		return defineClass(binaryName, b, 0, b.length);
	}

}
