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

package org.panda_lang.panda.framework.design.interpreter.token.stream;

import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;

public interface SourceStream {

    /**
     * Read the next token
     *
     * @return the next token
     */
    TokenRepresentation read();

    /**
     * Read the specified amount of tokens
     *
     * @param length amount of tokens to read
     * @return tokens
     */
    Snippet read(int length);

    /**
     * Read the rest of the current line
     *
     * @return the rest of the source at the current line
     */
    Snippet readLineResidue();

    /**
     * Restore the previous source (before the read operation)
     *
     * @return the current instance with restored content
     */
    SourceStream restoreCachedSource();

    /**
     * Override cached source
     *
     * @return the current instance
     */
    SourceStream updateCachedSource();

    /**
     * Replace content with the specified source
     *
     * @param source the content
     * @return the current instance with updated source
     */
    SourceStream update(Snippet source);

    /**
     * Get current source as TokenReader
     *
     * @return the current content wrapped in TokenReader
     */
    TokenReader toTokenReader();

    /**
     * Get current source as Tokens
     *
     * @return the current content wrapped in Tokens
     */
    Snippet toTokenizedSource();

    default Snippet readDifference(Snippet source) {
        return read(source.size());
    }

    default Snippet readDifference(TokenReader reader) {
        return read(reader.getIndex() + 1);
    }

    /**
     * Check if the source has available content
     *
     * @return true if source contains available for read content
     */
    default boolean hasUnreadSource() {
        return !toTokenizedSource().isEmpty();
    }

    /**
     * Get the amount of unread tokens
     *
     * @return the amount of unread tokens
     */
    default int getUnreadLength() {
        return toTokenizedSource().size();
    }

    /**
     * Get current line
     *
     * @return if there is no available source, the method returns -2, otherwise returns the number of current line
     */
    default int getCurrentLine() {
        return hasUnreadSource() ? toTokenizedSource().getFirst().getLine() : -2;
    }

}
