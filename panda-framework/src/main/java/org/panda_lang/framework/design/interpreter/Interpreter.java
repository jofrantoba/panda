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

package org.panda_lang.framework.design.interpreter;

import org.panda_lang.framework.design.architecture.Application;
import org.panda_lang.framework.design.architecture.Environment;
import org.panda_lang.framework.design.interpreter.source.Source;
import org.panda_lang.utilities.commons.function.ThrowingConsumer;

import org.panda_lang.utilities.commons.function.Option;

/**
 * Translate source code into efficient intermediate representation and build an application
 */
public interface Interpreter {

    /**
     * Starts the process of interpretation
     */
    Option<Application> interpret(Source source);

    /**
     * Starts the process of interpretation and gives access to the interpretation process
     *
     * @param interpretationConsumer the interpretation process consumer
     * @return interpreted application
     */
    Option<Application> interpret(Source source, ThrowingConsumer<Interpretation, ?> interpretationConsumer);

    /**
     * Get associated environment
     *
     * @return the environment
     */
    Environment getEnvironment();

}