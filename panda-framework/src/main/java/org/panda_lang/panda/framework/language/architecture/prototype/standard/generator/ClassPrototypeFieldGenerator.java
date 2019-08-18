/*
 * Copyright (c) 2015-2019 Dzikoysk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.panda_lang.panda.framework.language.architecture.prototype.standard.generator;

import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototypeReference;
import org.panda_lang.panda.framework.design.architecture.prototype.field.PrototypeField;
import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.design.runtime.flow.Flow;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.architecture.prototype.standard.field.PandaPrototypeField;
import org.panda_lang.panda.framework.language.architecture.value.PandaDynamicValue;
import org.panda_lang.panda.framework.language.architecture.value.PandaStaticValue;
import org.panda_lang.panda.framework.language.runtime.PandaRuntimeException;
import org.panda_lang.panda.framework.language.runtime.expression.PandaExpression;
import org.panda_lang.panda.framework.language.runtime.expression.PandaDynamicExpression;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

final class ClassPrototypeFieldGenerator {

    private final ClassPrototypeGenerator generator;
    private final ClassPrototype prototype;
    private final Field field;

    ClassPrototypeFieldGenerator(ClassPrototypeGenerator generator, ClassPrototype prototype, Field field) {
        this.generator = generator;
        this.prototype = prototype;
        this.field = field;
    }

    protected PrototypeField generate() {
        ClassPrototypeReference returnType = generator.computeIfAbsent(prototype.getModule(), field.getType());

        PrototypeField prototypeField = PandaPrototypeField.builder()
                .prototype(prototype.getReference())
                .returnType(returnType)
                .fieldIndex(prototype.getFields().getProperties().size())
                .name(field.getName())
                .isStatic(Modifier.isStatic(field.getModifiers()))
                .mutable(true)
                .nillable(true)
                .build();

        // TODO: Generate bytecode
        field.setAccessible(true);

        Expression fieldExpression = new PandaExpression(new PandaDynamicExpression(returnType.fetch()) {
            @Override
            public Value call(Expression expression, Flow frame) {
                Object instance = frame != null ? frame.getInstance().getValue() : null;

                try {
                    Object value = field.get(instance);
                    return new PandaStaticValue(returnType.fetch(), value);
                } catch (IllegalAccessException e) {
                    throw new PandaRuntimeException(e);
                }
            }
        });

        prototypeField.setDefaultValue(fieldExpression);
        prototypeField.setStaticValue(PandaDynamicValue.of(fieldExpression, null));
        return prototypeField;
    }

}
