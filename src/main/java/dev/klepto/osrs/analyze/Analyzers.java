package dev.klepto.osrs.analyze;

import io.github.classgraph.ClassGraph;
import lombok.val;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author <a href="https://klepto.dev/">Augustinas R.</a>
 */
public class Analyzers {

    private final Set<Analyzer> analyzers;

    public Analyzers() {
        this.analyzers = resolveAnalyzers();
    }

    private Set<Analyzer> resolveAnalyzers() {
        val analyzers = new HashSet<Analyzer>();
        try (val result = new ClassGraph().whitelistPackages(getClass().getPackageName()).scan()) {
            result.getAllClasses()
                    .filter(info -> info.extendsSuperclass(Analyzer.class.getName()))
                    .loadClasses()
                    .forEach(cls -> {
                        try {
                            val analyzer = (Analyzer) cls.newInstance();
                            analyzer.setAnalyzers(this);
                            analyzers.add(analyzer);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
        }
        return analyzers;
    }

    public void analyze(Set<ClassBytes> classes) {
        val iterators = analyzers.stream()
                .map(analyzer -> new AnalyzerIterator(analyzer, classes))
                .collect(Collectors.toSet());

        for (int i = 0; i < 10; i++) {
            val complete = iterators.stream().noneMatch(AnalyzerIterator::iterate);
            if (complete) {
                break;
            }
        }
    }

}
