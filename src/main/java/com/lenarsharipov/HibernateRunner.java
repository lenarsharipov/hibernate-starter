package com.lenarsharipov;

import com.lenarsharipov.entity.Company;
import com.lenarsharipov.entity.PersonalInfo;
import com.lenarsharipov.entity.User;
import com.lenarsharipov.util.HibernateUtil;
import com.lenarsharipov.util.TestDataImporter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.annotations.QueryHints;
import org.hibernate.graph.RootGraph;
import org.hibernate.jpa.AvailableHints;

import java.util.List;
import java.util.Map;

public class HibernateRunner {

    public static void main(String[] args) {
        try (SessionFactory sf = HibernateUtil.buildSessionFactory();
            var session = sf.openSession()) {
            session.beginTransaction();
//            session.enableFetchProfile("withCompanyAndPayment");

//            Map<String, Object> properties = Map.of(
//                    AvailableHints.HINT_SPEC_FETCH_GRAPH, session.getEntityGraph("WithCompanyAndChat")
//            );
//            User user = session.find(User.class, 1L, properties);
//            System.out.println(user.getCompany().getName());
//            System.out.println(user.getUserChats().size());
//            System.out.println(user.getPayments().size());

//            List<User> users = session.createQuery(
//                    "select u from User u ",
//                                User.class)
//                    .setHint(AvailableHints.HINT_SPEC_FETCH_GRAPH, session.getEntityGraph("WithCompanyAndChat"))
//                    .list();
//            users.forEach(user -> System.out.println(user.getUserChats()));
//            users.forEach(user -> System.out.println(user.getCompany().getName()));
//            users.forEach(user -> System.out.println(user.getPayments()));

            List<User> users = session.createQuery(
                            "select u from User u",
                            User.class)
                    .list();
            users.forEach(user -> System.out.println(user.getUserChats()));
            users.forEach(user -> System.out.println(user.getCompany().getName()));
            users.forEach(user -> System.out.println(user.getPayments()));

            session.getTransaction().commit();
        }
    }
}