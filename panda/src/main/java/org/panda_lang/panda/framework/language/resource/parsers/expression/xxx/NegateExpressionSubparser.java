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

package org.panda_lang.panda.framework.language.resource.parsers.expression.xxx;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.resource.parsers.expression.xxx.callbacks.NegateLogicalExpressionCallback;
import org.panda_lang.panda.framework.design.resource.parsers.expression.xxx.ExpressionParserOld;
import org.panda_lang.panda.framework.design.resource.parsers.expression.xxx.ExpressionSubparser;
import org.panda_lang.panda.framework.language.resource.syntax.operator.Operators;
import org.panda_lang.panda.framework.language.runtime.expression.PandaExpression;

public class NegateExpressionSubparser implements ExpressionSubparser {

    @Override
    public @Nullable Snippet read(ExpressionParserOld parent, Snippet source) {
        if (!source.getFirst().contentEquals(Operators.NOT)) {
            return null;
        }

        return source;
    }

    @Override
    public Expression parse(ExpressionParserOld parent, ParserData data, Snippet source) {
        Expression expression = parent.parse(data, source.subSource(1, source.size()));
        return new PandaExpression(new NegateLogicalExpressionCallback(expression));
    }

    @Override
    public int getMinimumLength() {
        return 2;
    }

    @Override
    public double getPriority() {
        return DefaultSubparsers.Priorities.SINGLE;
    }

    @Override
    public String getName() {
        return DefaultSubparsers.Names.NEGATE;
    }

}
