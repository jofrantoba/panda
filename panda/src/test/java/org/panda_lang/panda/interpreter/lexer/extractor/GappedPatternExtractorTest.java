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

package org.panda_lang.panda.interpreter.lexer.extractor;

import org.junit.jupiter.api.Test;
import org.panda_lang.panda.framework.design.interpreter.lexer.Lexer;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.interpreter.token.stream.TokenReader;
import org.panda_lang.panda.framework.language.interpreter.lexer.PandaLexer;
import org.panda_lang.panda.framework.design.interpreter.pattern.gapped.GappedPattern;
import org.panda_lang.panda.framework.design.interpreter.pattern.gapped.extractor.GappedPatternExtractor;
import org.panda_lang.panda.framework.design.interpreter.pattern.gapped.GappedPatternBuilder;
import org.panda_lang.panda.framework.language.interpreter.source.PandaSource;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaTokenReader;
import org.panda_lang.panda.framework.language.resource.PandaSyntax;

import java.util.List;

class GappedPatternExtractorTest {

    //private static final String SOURCE = "a('z').b.c(new Clazz { public void x(String m) { System.out.println(m); } }).d('x');";
    // private static final String SOURCE = "class A {} class B {}";
    // private static final String SOURCE = "module java.lang; import panda.lang;";
    private static final String SOURCE = "System.out.print(\"Hello Panda\", flag, varFoo, s, test, i, math);";

    private static final GappedPattern PATTERN = new GappedPatternBuilder()
            .lastIndexAlgorithm(true)
            .hollow()
            .unit(TokenType.SEPARATOR, ".")
            .simpleHollow()
            .unit(TokenType.SEPARATOR, "(")
            .hollow()
            .unit(TokenType.SEPARATOR, ")")
            .unit(TokenType.SEPARATOR, ";")
            .build();

    @Test
    public void testExtractor() {
        Lexer lexer = PandaLexer.of(PandaSyntax.getInstance(), new PandaSource(GappedPatternExtractorTest.class, SOURCE)).build();
        Snippet snippet = lexer.convert();
        TokenReader tokenReader = new PandaTokenReader(snippet);

        GappedPatternExtractor extractor = new GappedPatternExtractor(PATTERN);
        List<Snippet> gaps = extractor.extract(tokenReader);

        if (gaps == null) {
            System.out.println("Cannot extract gaps for PATTERN '" + PATTERN + "' and source '" + SOURCE + "'");
            return;
        }

        for (Snippet gap : gaps) {
            System.out.println("--- Gap:");

            for (TokenRepresentation tokenRepresentation : gap.getTokensRepresentations()) {
                System.out.println("  : " + tokenRepresentation);
            }
        }

        System.out.println("Size: " + gaps.size());
    }

}
