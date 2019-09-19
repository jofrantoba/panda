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

package org.panda_lang.framework.language.architecture.prototype;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.design.architecture.prototype.PrototypeReference;
import org.panda_lang.framework.design.architecture.parameter.Arguments;
import org.panda_lang.framework.design.architecture.prototype.PrototypeExecutable;
import org.panda_lang.framework.design.architecture.parameter.Parameter;
import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.design.runtime.ProcessStack;
import org.panda_lang.framework.language.architecture.prototype.array.ArrayPrototype;
import org.panda_lang.framework.language.architecture.expression.AbstractDynamicExpression;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class PrototypeExecutablePropertiesMatcher<T extends PrototypeExecutable> {

    public Optional<Arguments<T>> match(Collection<T> collection, Prototype[] requiredTypes, @Nullable Expression[] arguments) {
        return collection.stream()
                .map(executable -> match(executable, requiredTypes, arguments))
                .filter(Objects::nonNull)
                .findFirst();
    }

    private @Nullable Arguments<T> match(T executable, Prototype[] requiredTypes, @Nullable Expression[] arguments) {
        Parameter[] parameters = executable.getParameters();

        // return result for parameterless executables
        if (parameters.length == 0) {
            return requiredTypes.length == 0 ? new ResultArguments<>(executable, arguments) : null;
        }

        // map arguments into parameters
        int[] target = new int[requiredTypes.length];
        int index = 0, required = 0, varArgs = 0;

        // loop as long parameters and types are available
        for (; (index < parameters.length) && (required < requiredTypes.length); index++) {
            Parameter parameter = parameters[index];

            if (!parameter.isVarargs()) {
                target[required] = index;

                if (!parameter.getType().isAssignableFrom(requiredTypes[required++])) {
                    return null;
                }

                continue;
            }

            // varargs parameter has to be array
            PrototypeReference type = ((ArrayPrototype) parameter.getType().fetch()).getType();
            varArgs++;

            // read vararg
            while (required < requiredTypes.length) {
                Prototype nextType = requiredTypes[required];

                if (!type.isAssignableFrom(nextType)) {
                    break;
                }

                target[required] = index;
                required++;
            }
        }

        // return if does not match
        if (index != parameters.length || required != requiredTypes.length) {
            return null;
        }

        // return executable if only types was requested
        if (arguments == null) {
            return new ResultArguments<>(executable, null);
        }

        // return result without varargs mappings
        if (varArgs == 0) {
            return new ResultArguments<>(executable, arguments);
        }

        @SuppressWarnings("unchecked")
        List<Expression>[] mapped = new List[parameters.length];

        // group arguments
        for (int targetIndex = 0; targetIndex < target.length;) {
            int targetParameter = target[targetIndex];
            List<Expression> section = mapped[targetParameter];

            if (section == null) {
                section = (mapped[targetParameter] = new ArrayList<>(arguments.length - parameters.length - varArgs + 1));
            }

            section.add(arguments[targetIndex]);
            targetIndex++;
        }

        Expression[] fixedArguments = new Expression[mapped.length];

        // map arguments
        for (int argumentIndex = 0; argumentIndex < mapped.length; argumentIndex++) {
            List<Expression> expressions = mapped[argumentIndex];

            if (expressions.size() == 1) {
                fixedArguments[argumentIndex] = expressions.get(0);
                continue;
            }

            // generate varargs array expression
            fixedArguments[argumentIndex] = new AbstractDynamicExpression(((ArrayPrototype) parameters[argumentIndex].getType().fetch()).getType().fetch()) {
                @Override
                @SuppressWarnings("unchecked")
                public Object evaluate(ProcessStack stack, Object instance) {
                    return expressions.stream()
                            .map(expr -> expr.evaluate(stack, instance))
                            .toArray(Object[]::new);
                }
            }.toExpression();
        }

        return new ResultArguments<>(executable, fixedArguments);
    }

    public static final class ResultArguments<R extends PrototypeExecutable> implements Arguments<R> {

        private final R executable;
        private final Expression[] arguments;

        private ResultArguments(R executable, @Nullable Expression[] arguments) {
            this.executable = executable;
            this.arguments = arguments;
        }

        @Override
        public @Nullable Expression[] getArguments() {
            return arguments;
        }

        @Override
        public R getExecutable() {
            return executable;
        }

    }

}