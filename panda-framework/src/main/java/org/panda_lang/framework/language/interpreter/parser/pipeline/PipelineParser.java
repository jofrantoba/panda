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

package org.panda_lang.framework.language.interpreter.parser.pipeline;

import org.panda_lang.framework.design.interpreter.Interpretation;
import org.panda_lang.framework.design.interpreter.parser.Components;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.ContextParser;
import org.panda_lang.framework.design.interpreter.parser.Parser;
import org.panda_lang.framework.design.interpreter.parser.LocalChannel;
import org.panda_lang.framework.design.interpreter.parser.pipeline.HandleResult;
import org.panda_lang.framework.design.interpreter.parser.pipeline.Pipeline;
import org.panda_lang.framework.design.interpreter.parser.pipeline.PipelineComponent;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.design.interpreter.token.SourceStream;
import org.panda_lang.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.framework.language.resource.syntax.separator.Separators;

public final class PipelineParser<T extends ContextParser<?>> implements Parser {

    private final PipelineComponent<T> pipelineComponent;

    public PipelineParser(PipelineComponent<T> component) {
        this.pipelineComponent = component;
    }

    /**
     * Parse source using the declared pipeline
     *
     * @param context the context to use
     * @param stream the stream to parse
     * @return returns always null
     * @throws Exception if something happen in subparser
     */
    public boolean parse(Context context, SourceStream stream) throws Exception {
        Interpretation interpretation = context.getComponent(Components.INTERPRETATION);
        Pipeline<T> pipeline = context.getComponent(Components.PIPELINE).getPipeline(pipelineComponent);

        while (stream.hasUnreadSource() && interpretation.isHealthy()) {
            LocalChannel channel = new PandaLocalChannel();
            Snippet source = stream.toSnippet();

            Context delegatedContext = context.fork()
                    .withComponent(Components.CURRENT_SOURCE, source)
                    .withComponent(Components.CHANNEL, channel)
                    .withComponent(Components.STREAM, stream);

            HandleResult<T> result = pipeline.handle(context, channel, source);

            ContextParser<?> parser = result.getParser().orThrow(() -> {
                return result.getFailure().orElseGet(() -> {
                    throw new PandaParserFailure(delegatedContext, "Unrecognized syntax");
                });
            });

            int sourceLength = stream.getUnreadLength();
            parser.parse(delegatedContext);

            if (sourceLength == stream.getUnreadLength()) {
                throw new PandaParserFailure(delegatedContext, parser.getClass().getSimpleName() + " did nothing with the current source");
            }

            if (stream.hasUnreadSource() && stream.getCurrent().contentEquals(Separators.SEMICOLON)) {
                stream.read();
            }
        }

        return true;
    }

}
