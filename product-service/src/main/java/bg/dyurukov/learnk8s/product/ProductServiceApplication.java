package bg.dyurukov.learnk8s.product;

import com.github.gavlyukovskiy.boot.jdbc.decorator.DecoratedDataSource;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import net.ttddyy.dsproxy.support.ProxyDataSource;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.devtools.classpath.ClassPathFileSystemWatcher;
import org.springframework.boot.devtools.filewatch.FileSystemWatcher;
import org.springframework.boot.devtools.restart.Restarter;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.ReflectionUtils;

import javax.sql.DataSource;
import java.io.File;
import java.util.Map;
import java.util.Optional;

import static org.springframework.util.ReflectionUtils.doWithFields;

@Slf4j
@EnableScheduling
@SpringBootApplication
public class ProductServiceApplication {

    static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner cmd(Optional<ClassPathFileSystemWatcher> classPathFileSystemWatcherOptional) {
        return args -> {
            Restarter restarter = Restarter.getInstance();
            log.info("restarted is {}", restarter);
//            Field fieldd = restarter.getClass().getField("enabled");
//
//            log.info("Enabled field: {}", fieldd);
//
//            if (fieldd != null) {
//                fieldd.setAccessible(true);
//                Object enabled = ReflectionUtils.getField(fieldd, restarter);
//
//                log.info("Restarter is {}", enabled);
//            }

            if (classPathFileSystemWatcherOptional.isPresent()) {
                log.info("Watched directories....");
                var classPathFileSystemWatcher = classPathFileSystemWatcherOptional.get();
                doWithFields(classPathFileSystemWatcher.getClass(), f -> {
                    if (f.getName().equals("fileSystemWatcher")) {
                        f.setAccessible(true);
                        FileSystemWatcher fileSystemWatcher = (FileSystemWatcher) ReflectionUtils.getField(f, classPathFileSystemWatcher);

                        log.info("Found internal fileSystemWatcher");
                        doWithFields(fileSystemWatcher.getClass(), ff -> {
                            if (ff.getName().equals("directories")) {
                                log.info("Found directories");
                                ff.setAccessible(true);

                                Map<File, ?> directories = (Map<File, ?>) ReflectionUtils.getField(ff, fileSystemWatcher);
                                log.info("Directories size: {}", directories.size());
                                directories.keySet().forEach(e -> log.info("Watching {}....", e.getAbsolutePath()));
                            }
                        });
                    }
                });
//                Field field = classPathFileSystemWatcher.getClass().getField("fileSystemWatcher");
//                field.setAccessible(true);
//                FileSystemWatcher fileSystemWatcher = (FileSystemWatcher) ReflectionUtils.getField(field, classPathFileSystemWatcher);
//                Field dirField = fileSystemWatcher.getClass().getField("directories");
//                dirField.setAccessible(true);
//                Map<File, ?> asdf = (Map<File, ?>) ReflectionUtils.getField(dirField, fileSystemWatcher);
//                asdf.keySet().forEach(e -> log.info("Watching {}....", e.getAbsolutePath()));
//                log.info("Ending....");
            }

        };
    }

    @Bean
    @ConditionalOnBean(DataSource.class)
    CommandLineRunner commandLineRunner(DataSource dataSource) {
        return _ -> {
            ProxyDataSource targetDataSource = (ProxyDataSource)((DecoratedDataSource) dataSource).getTargetDataSource();

            if (targetDataSource == null) {
                return;
            }

            if (targetDataSource.getDataSource() instanceof PGSimpleDataSource postgresDataSource) {
                System.out.println(postgresDataSource.isReWriteBatchedInserts());
            } else if (targetDataSource.getDataSource() instanceof HikariDataSource hikariDataSource && !hikariDataSource.getDataSourceProperties().isEmpty()) {
                System.out.println(hikariDataSource.getDataSourceProperties());
            }
        };
    }

}
