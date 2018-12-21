/*
 * Copyright (c) 2015-2018 Dzikoysk
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

package org.panda_lang.panda.framework.language.interpreter.parser.statement.scope.block;

import org.panda_lang.panda.framework.design.architecture.dynamic.Block;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaPriorities;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.PandaParserBootstrap;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.UnifiedParserBootstrap;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Autowired;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Local;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Src;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.layer.LocalData;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.pipeline.Generation;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserPipeline;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserRegistration;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.design.interpreter.token.stream.TokenReader;
import org.panda_lang.panda.framework.language.interpreter.parser.ContainerParser;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaSourceStream;
import org.panda_lang.panda.utilities.commons.ObjectUtils;

@ParserRegistration(target = PandaPipelines.SCOPE_LABEL, priority = PandaPriorities.SCOPE_BLOCK_PARSER)
public class BlockParser extends UnifiedParserBootstrap {

    {
        bootstrapParser = PandaParserBootstrap.builder()
                .pattern("+* { +* }", "block-declaration", "block-body")
                .instance(this)
                .build();
    }

    @Override
    public boolean handle(ParserData data, TokenReader reader) {
        return ObjectUtils.isNotNull(data
                .getComponent(UniversalComponents.PIPELINE)
                .getPipeline(PandaPipelines.BLOCK)
                .handle(data, new PandaSourceStream(reader.getTokenizedSource())));
    }

    @Autowired(order = 1)
    private void parse(ParserData data, LocalData local, Generation generation, @Src("block-declaration") Tokens declaration) throws Throwable {
        SourceStream declarationStream = new PandaSourceStream(declaration);

        ParserPipeline<BlockSubparser> pipeline = data.getComponent(UniversalComponents.PIPELINE).getPipeline(PandaPipelines.BLOCK);
        BlockSubparser blockParser = pipeline.handle(data, declarationStream);

        if (blockParser == null) {
            throw new PandaParserFailure("Unknown block", data);
        }

        ParserData delegatedData = local.allocateInstance(data.fork());
        BlockData blockData = blockParser.parse(delegatedData, declaration);

        if (blockData == null || blockData.getBlock() == null) {
            throw new PandaParserFailure(blockParser.getClass().getSimpleName() + " cannot parse current block", data);
        }

        local.allocateInstance(blockData.getBlock());

        if (blockData.isUnlisted()) {
            return;
        }

        data.getComponent(PandaComponents.CONTAINER).addStatement(blockData.getBlock());
        data.setComponent(BlockComponents.PREVIOUS_BLOCK, blockData.getBlock());
    }

    /*
        ParserData blockData = local.allocateInstance(data.fork());
        blockData.setComponent(UniversalComponents.SOURCE_STREAM, declarationStream);
        blockParser.parse(blockData);

        Block block = local.allocateInstance(blockData.getComponent(BlockComponents.BLOCK));
        Boolean unlisted = blockData.getComponent(BlockComponents.UNLISTED_BLOCK);

        if (block == null) {
            throw new PandaParserFailure(blockParser.getClass() + " cannot parse current block", data);
        }

        if (unlisted == null || !unlisted) {
            data.getComponent(PandaComponents.CONTAINER).addStatement(block);
        }

        data.setComponent(BlockComponents.PREVIOUS_BLOCK, block);
        */

    @Autowired(order = 2)
    private void parseContent(@Local ParserData blockData, @Local Block block, @Src("block-body") Tokens body) throws Throwable {
        ContainerParser containerParser = new ContainerParser(block);
        containerParser.parse(blockData, body);
    }

}
