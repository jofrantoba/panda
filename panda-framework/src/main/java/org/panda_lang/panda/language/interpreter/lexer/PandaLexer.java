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

package org.panda_lang.panda.language.interpreter.lexer;

import org.panda_lang.panda.framework.design.interpreter.lexer.Lexer;
import org.panda_lang.panda.framework.design.interpreter.source.Source;
import org.panda_lang.panda.framework.design.interpreter.token.Snippet;
import org.panda_lang.panda.framework.design.resource.Syntax;

public class PandaLexer implements Lexer {

    private final PandaLexerConfiguration configuration;

    protected PandaLexer(PandaLexerConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Snippet convert(Source source) {
        PandaLexerWorker worker = new PandaLexerWorker(this, source);
        return worker.convert();
    }

    public PandaLexerConfiguration getConfiguration() {
        return configuration;
    }

    public static PandaLexerConfiguration of(Syntax syntax) {
        return new PandaLexerConfiguration().withSyntax(syntax);
    }

    public static PandaLexerConfiguration builder() {
        return new PandaLexerConfiguration();
    }

}