package com.lenarsharipov;

import com.lenarsharipov.entity.PersonalInfo;
import com.lenarsharipov.entity.User;
import com.lenarsharipov.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateRunner {

    private static final Logger log = LoggerFactory.getLogger(HibernateRunner.class);

    public static void main(String[] args) {
        /*
        * Здесь после создания,
        * user находится в состоянии
        * Transient.
        * */
        User user = User.builder()
                .username("petr@gmail.com")
                .personalInfo(PersonalInfo.builder()
                        .firstname("Petr")
                        .lastname("Petrov")
                        .build())
                .build();
        log.info("User entity is in transient state: {}", user);

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session1 = sessionFactory.openSession();
            try (session1) {
                Transaction transaction = session1.beginTransaction();
                log.trace("Transaction is created, {}", transaction);
                session1.merge(user);
                log.trace("User is in persistent state: {}, session: {}", user, session1);
                session1.getTransaction().commit();
            }
            log.warn("User is in a detached state: {}, session is closed: {}", user, session1);
        } catch (Exception exception) {
            log.error("Exception occured", exception);
            throw exception;
        }
    }
}