package com.lenarsharipov;

import com.lenarsharipov.entity.Role;
import com.lenarsharipov.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;

import java.time.LocalDate;

public class HibernateRunner {


    public static void main(String[] args) {

        var configuration = new Configuration();
        /*
        * Поле metadataSources отвечает за сущности
        * (нашу метаинформацию)
        * public Configuration addAnnotatedClass(Class annotatedClass) {
        *       metadataSources.addAnnotatedClass(annotatedClass);
        *       return this;
        * }
        * */
//        configuration.addAnnotatedClass(User.class);
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());
        configuration.configure();

        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
            Session session = sessionFactory.openSession()) {

            session.beginTransaction();

            User user = User.builder()
                    .username("ivan@gmail.com")
                    .firstname("Ivan")
                    .lastname("Ivanov")
                    .birthDate(LocalDate.of(2000, 1, 19))
                    .age(24)
                    .role(Role.ADMIN)
                    .build();

            session.persist(user);

            session.getTransaction().commit();
        }
    }
}
