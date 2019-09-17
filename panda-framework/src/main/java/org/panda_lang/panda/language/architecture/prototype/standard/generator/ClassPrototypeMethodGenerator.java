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

package org.panda_lang.panda.language.architecture.prototype.standard.generator;

import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototypeReference;
import org.panda_lang.panda.framework.design.architecture.prototype.method.PrototypeMethod;
import org.panda_lang.panda.framework.design.architecture.prototype.parameter.PrototypeParameter;
import org.panda_lang.panda.language.architecture.prototype.standard.method.PandaMethod;
import org.panda_lang.panda.language.architecture.prototype.standard.parameter.ParametrizedExecutableCallback;
import org.panda_lang.panda.language.runtime.PandaRuntimeException;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.InvalidParameterException;

final class ClassPrototypeMethodGenerator {

    private final ClassPrototypeGenerator generator;
    private final ClassPrototype prototype;
    private final Method method;

    ClassPrototypeMethodGenerator(ClassPrototypeGenerator generator, ClassPrototype prototype, Method method) {
        if (method == null) {
            throw new InvalidParameterException("Method cannot be null");
        }

        this.generator = generator;
        this.prototype = prototype;
        this.method = method;
    }

    protected PrototypeMethod generate() {
        ClassPrototypeReference returnType = generator.computeIfAbsent(prototype.getModule(), method.getReturnType());
        PrototypeParameter[] mappedParameters = ClassPrototypeGeneratorUtils.toParameters(prototype.getModule(), method.getParameters());
        boolean isVoid = returnType.getName().equals("void");

        // TODO: Generate bytecode
        method.setAccessible(true);

        ParametrizedExecutableCallback<Object> methodBody = (stack, instance, arguments) -> {
            int amountOfArgs = arguments.length;
            int parameterCount = method.getParameterCount();
            Object varargs = null;

            if (amountOfArgs != parameterCount) {
                if (parameterCount < 1) {
                    throw new PandaRuntimeException("Too many arguments");
                }

                Class<?> last = method.getParameterTypes()[parameterCount - 1];
                String lastName = last.getName();
                System.out.println(last);
                Class<?> rootLast = Class.forName(lastName.substring(2, lastName.length() - 1));

                if (amountOfArgs + 1 != parameterCount || !last.isArray()) {
                    throw new PandaRuntimeException("Cannot invoke mapped mapped method (args.length != parameters.length)");
                }

                varargs = Array.newInstance(rootLast, 0);
                amountOfArgs++;
            }

            if (varargs != null) {
                arguments[amountOfArgs - 1] = varargs;
            }

            Object returnValue = method.invoke(instance, arguments);

            if (isVoid) {
                return null;
            }

            return returnValue;
        };

        return PandaMethod.builder()
                .prototype(prototype.getReference())
                .isStatic(Modifier.isStatic(method.getModifiers()))
                .returnType(returnType)
                .name(method.getName())
                .methodBody(methodBody)
                .parameters(mappedParameters)
                .build();
    }

}