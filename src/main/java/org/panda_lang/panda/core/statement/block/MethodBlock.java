package org.panda_lang.panda.core.statement.block;

import org.panda_lang.panda.Panda;
import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.Essence;
import org.panda_lang.panda.core.parser.Atom;
import org.panda_lang.panda.core.parser.essential.FactorParser;
import org.panda_lang.panda.core.parser.essential.util.BlockInitializer;
import org.panda_lang.panda.core.parser.essential.util.BlockLayout;
import org.panda_lang.panda.core.statement.Block;
import org.panda_lang.panda.core.statement.Method;
import org.panda_lang.panda.core.statement.util.NamedExecutable;

public class MethodBlock extends Block {

    public MethodBlock(String name) {
        super.setName(name);
    }

    public static void initialize(Panda panda) {
        BlockLayout blockLayout = new BlockLayout(MethodBlock.class, "method", "function", "constructor", "public").initializer(new BlockInitializer() {
            @Override
            public MethodBlock initialize(Atom atom) {
                MethodBlock block = new MethodBlock(atom.getBlockInfo().getSpecifiers().get(0));
                block.setFactors(new FactorParser().parse(atom, atom.getBlockInfo().getParameters()));
                return block;
            }
        });
        panda.getPandaCore().registerBlock(blockLayout);
    }

    @Override
    public Essence execute(Alice alice) {
        return super.execute(alice);
    }

    @Override
    public boolean isReturned() {
        return true;
    }

    public Method toMethod() {
        final MethodBlock methodBlock = this;
        return new Method(new NamedExecutable() {
            @Override
            public String getName() {
                return methodBlock.getName();
            }

            @Override
            public Essence execute(Alice alice) {
                return methodBlock.execute(alice);
            }
        });
    }

}