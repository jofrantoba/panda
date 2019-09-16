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

package org.panda_lang.panda.language.resource.expression.subparsers.operation.subparsers.logical;

import org.panda_lang.panda.framework.design.runtime.ProcessStack;
import org.panda_lang.panda.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.language.resource.expression.subparsers.operation.rpn.RPNOperationAction;

public class LessThanOperator extends ComparisonOperator {

    @Override
    public RPNOperationAction of(int compared) {
        return new ComparisonOperatorAction() {
            @Override
            public Object get(ProcessStack stack, Number a, Number b) {
                switch (compared) {
                    case INT:
                        return a.intValue() < b.intValue();
                    case LONG:
                        return a.longValue() < b.longValue();
                    case DOUBLE:
                        return a.doubleValue() < b.doubleValue();
                    case FLOAT:
                        return a.floatValue() < b.floatValue();
                    case BYTE:
                        return a.byteValue() < b.byteValue();
                    case SHORT:
                        return a.shortValue() < b.shortValue();
                    default:
                        throw new PandaParserException("Unknown type " + compared);
                }
            }
        };
    }

}
