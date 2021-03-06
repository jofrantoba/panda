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

package org.panda_lang.framework.design.resource;

import org.panda_lang.framework.design.interpreter.messenger.MessengerInitializer;
import org.panda_lang.framework.design.interpreter.messenger.MessengerOutputListener;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionSubparsers;
import org.panda_lang.framework.design.interpreter.parser.pipeline.PipelinePath;

import org.panda_lang.utilities.commons.function.Option;

/**
 * Set of resources used by the language
 */
public interface Resources {

    /**
     * Get all expression subparsers to use by {@link org.panda_lang.framework.design.interpreter.parser.expression.ExpressionParser}
     *
     * @return the expression subparsers
     */
    ExpressionSubparsers getExpressionSubparsers();

    /**
     * Get pipeline path to use by the language
     *
     * @return the pipeline path
     */
    PipelinePath getPipelinePath();

    /**
     * Get initializer for {@link org.panda_lang.framework.design.interpreter.messenger.Messenger}
     *
     * @return the messenger initializer
     */
    Option<MessengerInitializer> getMessengerInitializer();

    /**
     * Get output listener for {@link org.panda_lang.framework.design.interpreter.messenger.Messenger}
     *
     * @return the output listener to use by messenger
     */
    Option<MessengerOutputListener> getOutputListener();

}
