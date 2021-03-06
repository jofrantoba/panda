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

package org.panda_lang.framework.design.interpreter.parser.expression;

import org.panda_lang.framework.design.architecture.expression.Expression;

import java.util.Collection;

/**
 * Transactional wrapper for the parsed expression.
 * Useful if we parsed expression that had to commit changes in some places and we want to drop them (e.g. variable creation)
 */
public interface ExpressionTransaction {

    /**
     * Rollback changes required to create this expression
     */
    void rollback();

    /**
     * Get collection of commits
     *
     * @return the actual collection of commits
     */
    Collection<Commit> getCommits();

    /**
     * Get parsed expression
     *
     * @return the expression
     */
    Expression getExpression();

    /**
     * Single commit that represents a change
     */
    interface Commit {

        /**
         * Rollback this change
         */
        void rollback();

    }

}
