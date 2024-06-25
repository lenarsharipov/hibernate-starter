package com.lenarsharipov;

import com.lenarsharipov.entity.Company;
import com.lenarsharipov.entity.PersonalInfo;
import com.lenarsharipov.entity.User;
import com.lenarsharipov.util.HibernateUtil;
import com.lenarsharipov.util.TestDataImporter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class HibernateRunner {

    public static void main(String[] args) {
        try (SessionFactory sf = HibernateUtil.buildSessionFactory();
            var session = sf.openSession()) {
            session.beginTransaction();

//            User user = session.get(User.class, 1L);
//            System.out.println(user.getPayments().size());
//            System.out.println(user.getCompany().getName());

            List<User> users = session.createQuery(
                    "select u from User u " +
                                "join fetch u.payments " +
                                "join fetch u.company",
                                User.class)
                    .list();
            users.forEach(user -> System.out.println(user.getPayments().size()));
            users.forEach(user -> System.out.println(user.getCompany().getName()));

            session.getTransaction().commit();
        }
    }
}