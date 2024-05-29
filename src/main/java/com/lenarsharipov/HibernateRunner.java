package com.lenarsharipov;

import com.lenarsharipov.entity.User;
import com.lenarsharipov.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class HibernateRunner {

    public static void main(String[] args) {
        /*
        * Здесь после создания,
        * user находится в состоянии
        * Transient.
        * */
        User user = User.builder()
                .username("ivan@gmail.com")
                .firstname("Ivan")
                .lastname("Ivanov")
                .build();

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (Session session1 = sessionFactory.openSession()) {
                session1.beginTransaction();

                /*
                * Теперь user будет находиться
                * в Persistent состоянии по отношению
                * к session1. Но в состоянии Transient
                * по отношению к session2
                * */
                session1.merge(user);

                session1.getTransaction().commit();
            }

            try (Session session2 = sessionFactory.openSession()) {
                session2.beginTransaction();

//                session2.remove(user);
//                System.out.println(user);
//                session2.refresh(user);
//                System.out.println(user);
//                user.setFirstname("IVAN UPD");

                user.setFirstname("Sveta");
                user.setLastname("Svetikova");
                User merged = session2.merge(user);

                session2.getTransaction().commit();
            }
        }
    }
}