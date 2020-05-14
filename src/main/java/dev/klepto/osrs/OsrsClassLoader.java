package dev.klepto.osrs;

import com.google.common.io.ByteStreams;
import dev.klepto.osrs.analyze.ClassBytes;
import lombok.SneakyThrows;
import lombok.val;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author <a href="https://klepto.dev/">Augustinas R.</a>
 */
public class OsrsClassLoader extends ClassLoader {

    private static final Map<String, byte[]> resources = new HashMap<>();
    private static Map<String, ClassBytes> transformedClasses = new HashMap<>();

    @SneakyThrows
    public OsrsClassLoader(InputStream jarInputStream) {
        val inputStream = new ZipInputStream(jarInputStream);
        var resourceEntry = (ZipEntry) null;
        while ((resourceEntry = inputStream.getNextEntry()) != null) {
            val name = resourceEntry.getName();
            val bytes = ByteStreams.toByteArray(inputStream);
            resources.put(name, bytes);
        }
        inputStream.close();
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        var bytecode = (byte[]) null;
        if (transformedClasses.containsKey(name)) {
            bytecode = transformedClasses.get(name).getBytecode();
        } else {
            var resource = name.replace('.', '/').concat(".class");
            bytecode = resources.get(resource);
            if (bytecode == null) {
                throw new ClassNotFoundException();
            }
        }
        return defineClass(name, bytecode, 0, bytecode.length);
    }

    public void setTransformedClasses(Set<ClassBytes> classes) {
        classes.forEach(classBytes -> transformedClasses.put(classBytes.getName(), classBytes));
    }

    public ClassBytes getClassBytes(String resource) {
        if (!resource.contains(".class")) {
            return null;
        }

        val name = resource.replace('/', '.').replace(".class", "");
        return new ClassBytes(name, resources.get(resource));
    }

    public Set<ClassBytes> getClassBytes() {
        return resources.keySet().stream().
                map(this::getClassBytes)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

}
