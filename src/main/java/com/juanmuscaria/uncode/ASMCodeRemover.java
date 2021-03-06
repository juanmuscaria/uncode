/**
 *     uncode - Removes all code and assets from a jar file
 *     Copyright (C) 2022  juanmuscaria <juanmuscaria@gmail.com>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.juanmuscaria.uncode;

import com.juanmuscaria.uncode.cleaners.ClassCleaner;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ASMCodeRemover {

    private static final Logger logger
            = LoggerFactory.getLogger(ASMCodeRemover.class);

    /**
     * Removes all the code, assets, and private elements from given jar,
     * keeping only public classes, methods and fields with no code body.
     *
     * @param jarFile the jar file to process
     * @param outputFile the output file
     * @param overwrite if the output file should be overwritten if it already exists
     * @return a map with jarEntry-reason for all entries from the input jar that where removed (resources and class files)
     * @throws IOException if an I/O error occurs
     */
    public static Map<String, String> removeContent(Path jarFile, Path outputFile, boolean overwrite) throws IOException {
        if (!Files.exists(jarFile)) {
            throw new IllegalArgumentException("Input file does not exist");
        } else if (!Files.isReadable(jarFile)) {
            throw new IllegalArgumentException("Input file is not readable");
        } else if (Files.exists(outputFile) && !overwrite) {
            throw new IllegalArgumentException("Output file already exists");
        }

        var failedEntries = new LinkedHashMap<String, String>();
        try (var out = new ZipOutputStream(Files.newOutputStream(outputFile, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING))) {
            try (var zip = new ZipFile(jarFile.toFile())) {
                zip.entries().asIterator().forEachRemaining(entry -> {
                    if (entry.getName().endsWith(".class")) {
                        try {
                            var classBytes = processClass(zip.getInputStream(entry).readAllBytes());
                            out.putNextEntry(new ZipEntry(entry));
                            out.write(classBytes);
                            out.closeEntry();
                        } catch (Exception e) {
                            failedEntries.put(entry.getName(), e.getMessage());
                            logger.debug("Failed to process class: {}", entry.getName());
                            logger.debug("Exception:", e);
                        }
                    } else if (!entry.getName().endsWith("/")) {
                        failedEntries.put(entry.getName(), "Not a class file");
                    }
                });
            } catch (ZipException e) {
                throw new IllegalArgumentException("Input file is corrupted or not a valid jar file: " + e.getMessage(), e);
            }
        }

        return failedEntries;
    }

    /**
     * Removes all the code from a class, keeping only its public members without any code body.
     *
     * @param classBytes input class bytes
     * @return the processed class bytes
     * @throws IllegalArgumentException if the class is not readable by the current ASM version,
     * if the class is synthetic or if the class is not public
     */
    public static byte[] processClass(byte[] classBytes) throws IllegalArgumentException {
        var classReader = new ClassReader(classBytes);
        var classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
        classReader.accept(new ClassCleaner(classWriter), 0);
        return classWriter.toByteArray();
    }
}
