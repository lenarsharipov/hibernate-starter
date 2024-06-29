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
            var session = sf.openSession()) {
            session.beginTransaction();
            TestDataImporter.importData(sf);

            Payment payment = session.find(Payment.class, 1L);
            payment.setAmount(payment.getAmount() + 10);

            session.getTransaction().commit();
        }
    }
}