package tobyspring.config;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.boot.context.annotation.ImportCandidates;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.type.AnnotationMetadata;
import tobyspring.config.annotation.MyAutoConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

public class MyAutoConfigImportSelector implements DeferredImportSelector {

    private final ClassLoader classLoader;

    public MyAutoConfigImportSelector(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    /*@Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{
                "tobyspring.config.autoconfig.DispatcherServletConfig",
                "tobyspring.config.autoconfig.TomcatWebServerConfig"
        };
    }*/

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        // 방법 1
        //Iterable<String> candidates = ImportCandidates.load(MyAutoConfiguration.class, classLoader);
        //return StreamSupport.stream(candidates.spliterator(), false).toArray(String[]::new);


        // 방법 2
        List<String> autoConfigs = new ArrayList<>();
        ImportCandidates.load(MyAutoConfiguration.class, classLoader).forEach(candidate -> {
            autoConfigs.add(candidate);
        });

        return autoConfigs.toArray(String[]::new);
    }

}
