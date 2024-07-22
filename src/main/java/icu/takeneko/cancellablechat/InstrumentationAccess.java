package icu.takeneko.cancellablechat;

import net.bytebuddy.agent.ByteBuddyAgent;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Iterator;

public class InstrumentationAccess {
    public static final Path TRANSFORMER_OUTPUT_PATH = Path.of(".", ".cancellable-chat");
    private static Instrumentation inst;

    public static void init() {
        try {
            if (Files.isDirectory(TRANSFORMER_OUTPUT_PATH)) {
                deleteDirectory();
            }
            inst = ByteBuddyAgent.install();
            inst.addTransformer(new ClassTransformerImpl(), true);
            inst.retransformClasses(Class.forName("com.velocitypowered.proxy.protocol.packet.chat.keyed.KeyedChatHandler"));
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private static void deleteDirectory() throws IOException {
        final Iterator<Path> iterator = Files.walk(TRANSFORMER_OUTPUT_PATH)
                .sorted(Comparator.reverseOrder()).iterator();
        while (iterator.hasNext()) {
            Files.delete(iterator.next());
        }
    }


}
