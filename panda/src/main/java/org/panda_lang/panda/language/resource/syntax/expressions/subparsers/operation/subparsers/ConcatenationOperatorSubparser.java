/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.subparsers;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.design.architecture.type.Type;
import org.panda_lang.framework.design.interpreter.parser.Components;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.language.resource.syntax.operator.Operators;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.Operation;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.OperationParser;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.OperationSubparser;

import java.util.ArrayList;
import java.util.List;

public final class ConcatenationOperatorSubparser implements OperationSubparser {

    @Override
    public @Nullable Expression parse(OperationParser parser, Context context, Operation operation) {
        List<Expression> values = new ArrayList<>((operation.getElements().size() - 1) / 2);
        int lastIndex = 0;

        for (int i = 0; i < operation.getElements().size(); i++) {
            Operation.OperationElement element = operation.getElements().get(i);

            if (element.isExpression() || !Operators.ADDITION.equals(element.getOperator())) {
                continue;
            }

            if (!parseSubOperation(parser, context, values, operation, lastIndex, i)) {
                return null;
            }

            lastIndex = i + 1;
        }

        if (!parseSubOperation(parser, context, values, operation, lastIndex, operation.getElements().size())) {
            return null;
        }

        Type stringType = context.getComponent(Components.TYPE_LOADER).requireType(String.class);
        return new ConcatenationExpressionCallback(stringType, values).toExpression();
    }

    private boolean parseSubOperation(OperationParser parser, Context context, List<Expression> values, Operation operation, int start, int end) {
        if ((end - start) == 1) {
            values.add(operation.getElements().get(start).getExpression());
            return true;
        }

        Operation subOperation = new Operation(operation.getElements().subList(start, end));

        return parser.parse(context, subOperation)
                .peek(values::add)
                .isDefined();
    }

}
