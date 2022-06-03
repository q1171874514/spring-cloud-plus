package com.example.comment;

import org.hibernate.boot.Metadata;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Integrator used to process comment comment.
 *
 * @author Elyar Adil
 * @since 1.0
 */
@Component
public class CommentIntegrator implements Integrator {
    public static final CommentIntegrator INSTANCE = new CommentIntegrator();

    public CommentIntegrator() {
        super();
    }

    /**
     * Perform comment integration.
     *
     * @param metadata        The "compiled" representation of the mapping information
     * @param sessionFactory  The session factory being created
     * @param serviceRegistry The session factory's service registry
     */
    @Override
    public void integrate(Metadata metadata, SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {
        processComment(metadata);
    }

    /**
     * Not used.
     *
     * @param sessionFactoryImplementor     The session factory being closed.
     * @param sessionFactoryServiceRegistry That session factory's service registry
     */
    @Override
    public void disintegrate(SessionFactoryImplementor sessionFactoryImplementor, SessionFactoryServiceRegistry sessionFactoryServiceRegistry) {
    }

    /**
     * Process comment comment.
     *
     * @param metadata process comment of this {@code Metadata}.
     */
    private void processComment(Metadata metadata) {
        for (PersistentClass persistentClass : metadata.getEntityBindings()) {
            // Process the Comment comment is applied to Class
            Class<?> clz = persistentClass.getMappedClass();
            if (clz.isAnnotationPresent(Comment.class)) {
                Comment comment = clz.getAnnotation(Comment.class);
                persistentClass.getTable().setComment(comment.value());
            }


            // Process fields with Comment comment.
            //noinspection unchecked
            fieldComment(persistentClass);
        }
    }

    private void commentInsert(PersistentClass persistentClass, String columnName, String comment) {
        String sqlColumnName= persistentClass.getProperty(columnName).getValue().getColumnIterator().next().getText();
        Iterator<org.hibernate.mapping.Column> columnIterator = persistentClass.getTable().getColumnIterator();
        while (columnIterator.hasNext()) {
            org.hibernate.mapping.Column column = columnIterator.next();
            if (sqlColumnName.equalsIgnoreCase(column.getName())) {
                column.setComment(comment);
                break;
            }
        }
    }
    /**
     * Process @{code comment} comment of field.
     *
     * @param persistentClass Hibernate {@code PersistentClass}
     */
    private void fieldComment(PersistentClass persistentClass) {
        Class entityClass = persistentClass.getMappedClass();
        while (entityClass != null) {
            Arrays.stream(entityClass.getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(Comment.class))
                    .forEach(field -> {
                        String columnName = field.getAnnotation(Comment.class).value();
                        columnName = columnName == null? field.getName(): columnName;
                        commentInsert(persistentClass, field.getName(), columnName);
                    });
            entityClass = entityClass.getSuperclass();
        }

    }
}
