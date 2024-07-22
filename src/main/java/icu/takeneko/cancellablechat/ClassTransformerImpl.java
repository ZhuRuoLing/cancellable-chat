package icu.takeneko.cancellablechat;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.ProtectionDomain;

import static icu.takeneko.cancellablechat.InstrumentationAccess.TRANSFORMER_OUTPUT_PATH;

public class ClassTransformerImpl implements ClassFileTransformer {

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (!className.equals("com/velocitypowered/proxy/protocol/packet/chat/keyed/KeyedChatHandler")) {
            return null;
        }
        ClassNode node = new ClassNode();
        new ClassReader(classfileBuffer).accept(node, 0);
        for (MethodNode method : node.methods) {
            if (method.name.equals("invalidCancel")
                    && method.desc.equals("(Lorg/apache/logging/log4j/Logger;Lcom/velocitypowered/proxy/connection/client/ConnectedPlayer;)V")
            ) {
                method.instructions.iterator().add(new InsnNode(Opcodes.RETURN));
                break;
            }
        }
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        node.accept(writer);
        byte[] buf = writer.toByteArray();
        try {
            final Path path = TRANSFORMER_OUTPUT_PATH.resolve(className + ".class");
            Files.createDirectories(path.getParent());
            Files.write(path, buf);
        } catch (Throwable t) {
            System.err.println("Failed to write transformed class %s to disk".formatted(className));
            t.printStackTrace();
        }
        return buf;
    }
}
