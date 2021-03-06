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

package org.panda_lang.framework.design.interpreter.parser;

import java.util.Map;

/**
 * Component based set of data used during the interpretation process
 */
public interface Context {

    /**
     * Clone context to a new independent instance
     */
    Context fork();

    /**
     * @param componentName a name of the specified component
     */
    <T> Context withComponent(ContextComponent<T> componentName, T component);

    /***
     * @param componentName a name of the specified component
     * @return selected component
     */
    <T> T getComponent(ContextComponent<T> componentName);

    /**
     * @return all components stored in the current parser data
     */
    Map<? extends ContextComponent<?>, ? extends Object> getComponents();

}
