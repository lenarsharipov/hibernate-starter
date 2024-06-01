package com.lenarsharipov;

import com.lenarsharipov.entity.Company;
import com.lenarsharipov.entity.PersonalInfo;
import com.lenarsharipov.entity.User;
import com.lenarsharipov.util.HibernateUtil;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class HibernateRunner {

    public static void main(String[] args) {
//        Company company = Company.builder()
//                .name("Google")
//                .build();
//
//        User user = User.builder()
//                .username("petr@gmail.com")
//                .personalInfo(PersonalInfo.builder()
//                        .firstname("Petr")
//                        .lastname("Petrov")
//                        .build())
//                .company(company)
//                .build();

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            try (session) {
                Transaction transaction = session.beginTransaction();

//                session.persist(company);
//                session.persist(user);
//
//                company.setName(company.getName() + "UPD!");

                User user1 = session.get(User.class, 1L);

                /*
                * здесь будет вызван прокси.
                * */
                Company company1 = user1.getCompany();
                /* здесь ничего не произойдет, никакой инициализации
                    потому что каждый прокси содержит метод для инициализации,
                    а следовательно это company_id.
                    Но если мы вызовем company1.getName(), то начнется
                    процесс инициализации прокси.
                 */
                company1.getId();
                String name = company1.getName();
                Object object = Hibernate.unproxy(company1);
//                System.out.println(user1);

                session.getTransaction().commit();
            }
        }
    }
}