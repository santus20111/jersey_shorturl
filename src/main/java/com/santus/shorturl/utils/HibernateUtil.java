package com.santus.shorturl.utils;

import com.santus.shorturl.models.RedirectInfoEntity;
import com.santus.shorturl.models.URLInfoEntity;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HibernateUtil {
    private static StandardServiceRegistry registry;
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                // Create registry
                Path path = Paths.get("src/main/resources/hibernate.cfg.xml");
                registry = new StandardServiceRegistryBuilder()
                        .configure(path.toFile())
                        .build();

                // Create MetadataSources
                Metadata metadata = new MetadataSources(registry).addAnnotatedClass(RedirectInfoEntity.class).addAnnotatedClass(URLInfoEntity.class).getMetadataBuilder().build();

                // Create SessionFactory
                sessionFactory = metadata.getSessionFactoryBuilder().build();

            } catch (Exception e) {
                e.printStackTrace();
                if (registry != null) {
                    StandardServiceRegistryBuilder.destroy(registry);
                }
            }
        }
        return sessionFactory;
    }

    public static void shutdown() {
        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
