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

import io.micronaut.configuration.picocli.PicocliRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.nio.file.Path;

@Command(name = "uncode", description = "Removes all code and assets from a jar file, leaving only public symbols with no code.",
        mixinStandardHelpOptions = true, version = "%VERSION%")
public class Uncode implements Runnable {

    private static final Logger logger
            = LoggerFactory.getLogger(Uncode.class);

    @Option(names = {"-o", "--overwrite"}, description = "Overwrite output file, if it exists.")
    boolean overwrite = false;
    @Option(names = {"-l", "--list"}, description = "List ignored entries from the input file.")
    boolean list = false;
    @Parameters(index = "0", description = "Input jar file to process.")
    Path input;
    @Parameters(index = "1", description = "Output file, if omitted it will default to '<input file>.uncoded.jar'.", defaultValue = Parameters.NULL_VALUE)
    Path output;

    public static void main(String[] args) {
        PicocliRunner.run(Uncode.class, args);
    }

    public void run() {
        if (output == null) {
            output = Path.of(input.getFileName().getFileName().toString() + ".uncoded.jar");
        }
        logger.info("Processing jar file {}", input);
        try {
            var ignored = ASMCodeRemover.removeContent(input, output, overwrite);
            logger.info("Output saved as {}, removed {} entries.", output, ignored.size());
            if (ignored.size() > 0 && list) {
                logger.warn("The following jar entries were ignored:");
                ignored.forEach((k, v) -> logger.warn("{} ({})", k, v));
            }
        } catch (IllegalArgumentException e) {
            logger.error(e.getLocalizedMessage());
        } catch (Exception e) {
            logger.error("A fatal error occurred while processing the jar file", e);
        }
    }
}
