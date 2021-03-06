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

package org.panda_lang.panda.language.interpreter.parser.context;

public final class ParserBootstrapUtils {

    private static final Class<?>[] INTERNAL = {
            BootstrapContextParser.class,
            BootstrapTaskScheduler.class
    };

    private ParserBootstrapUtils() { }

    /**
     * Get internal classes. Useful when we want to exclude some internal classes from stacktrace.
     *
     * @return the array of internal classes
     */
    public static Class<?>[] getInternalClasses() {
        return INTERNAL.clone();
    }

}
