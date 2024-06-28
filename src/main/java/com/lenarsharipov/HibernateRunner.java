package com.lenarsharipov;

import com.lenarsharipov.entity.Company;
import com.lenarsharipov.entity.Payment;
import com.lenarsharipov.entity.PersonalInfo;
import com.lenarsharipov.entity.User;
import com.lenarsharipov.util.HibernateUtil;
import com.lenarsharipov.util.TestDataImporter;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.annotations.QueryHints;
import org.hibernate.graph.RootGraph;
import org.hibernate.jdbc.Work;
import org.hibernate.jpa.AvailableHints;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class HibernateRunner {

    @Transactional
    public static void main(String[] args) {
        try (SessionFactory sf = HibernateUtil.buildSessionFactory();
            var session1 = sf.openSession();
            var session2 = sf.openSession()) {
            session1.beginTransaction();
            session2.beginTransaction();
            TestDataImporter.importData(sf);

//            Payment payment = session1.find(Payment.class, 1L, LockModeType.OPTIMISTIC);
            Payment payment = session1.find(Payment.class, 1L);
            payment.setAmount(payment.getAmount() + 10);

            Payment samePayment = session2.find(Payment.class, 1L);
            samePayment.setAmount(samePayment.getAmount() + 20);

            session1.getTransaction().commit();
            session2.getTransaction().commit();
        }
    }
}