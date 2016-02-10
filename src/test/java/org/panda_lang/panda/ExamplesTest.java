package org.panda_lang.panda;

import org.panda_lang.panda.core.syntax.block.MethodBlock;

import java.io.File;
import java.util.Collection;

public class ExamplesTest {

    private static final File EXAMPLES = new File("examples");

    public static void main(String[] args) {
        Panda panda = new Panda();
        panda.initializeDefaultElements();

        Collection<PandaScript> scripts = panda.getPandaLoader().loadDirectory(EXAMPLES);
        for (PandaScript pandaScript : scripts) {
            pandaScript.call(MethodBlock.class, "main");
        }
    }

}