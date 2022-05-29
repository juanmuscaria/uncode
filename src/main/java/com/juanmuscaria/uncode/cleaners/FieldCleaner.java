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
package com.juanmuscaria.uncode.cleaners;

import org.objectweb.asm.*;

/**
 * Cleans annotation and attributes from a field.
 */
public class FieldCleaner extends FieldVisitor {

    public FieldCleaner(FieldVisitor fieldVisitor) {
        super(Opcodes.ASM9, fieldVisitor);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        // NO-OP
        return null;
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        // NO-OP
        return null;
    }

    @Override
    public void visitAttribute(Attribute attribute) {
        // NO-OP
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
    }
}
