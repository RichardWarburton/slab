package com.insightfullogic.slab.implementation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.objectweb.asm.ClassWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.insightfullogic.slab.SlabOptions;

class GeneratedClassLoader extends ClassLoader {
    
    private static final String DUMP_DIR = System.getProperty("user.home") + File.separator + "dump" + File.separator;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(GeneratedClassLoader.class);
    
    private final boolean debugEnabled;

    GeneratedClassLoader(SlabOptions options) {
        debugEnabled = options.isDebugEnabled();
    }

	synchronized Class<?> defineClass(final String binaryName, final ClassWriter cw) {
		final byte[] bytecode = cw.toByteArray();
		logDebugInfo(binaryName, bytecode);
		return defineClass(binaryName, bytecode, 0, bytecode.length);
	}

    private void logDebugInfo(final String binaryName, final byte[] bytecode) {
        if (!debugEnabled)
            return;
        
		final File file = new File(DUMP_DIR + makeFilename(binaryName));
		if (!dumpDirExists(file.getParentFile()))
		    throw new IllegalStateException("Unable to create directory: " + file.getParent());
		
		writeBytecodeToFile(bytecode, file);
    }

    private void writeBytecodeToFile(final byte[] bytecode, final File file) {
        OutputStream out = null;
		try {
			out = new FileOutputStream(file);
			out.write(bytecode);
			out.flush();
		} catch (IOException e) {
			LOGGER.error("Error logging debug information", e);
		} finally {
			closeStream(out);
		}
    }

    private boolean dumpDirExists(final File dumpDir) {
        return (dumpDir.exists() && dumpDir.isDirectory()) || dumpDir.mkdirs();
    }

    private String makeFilename(final String binaryName) {
        return binaryName.replace('.', File.separatorChar) + ".class";
    }

    private void closeStream(OutputStream out) {
        if (out == null)
            return;

    	try {
    		out.close();
    	} catch (IOException e) {
    	    LOGGER.warn(e.getMessage(), e);
    	}
    }

}
