package com.lenarsharipov.dao;

import com.lenarsharipov.entity.Company;
import com.lenarsharipov.entity.Payment;
import com.lenarsharipov.entity.User;
import jakarta.persistence.criteria.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDao {

    private static final UserDao INSTANCE = new UserDao();

    /**
     * Возвращает всех сотрудников
     */
    public List<User> findAll(Session session) {
        // HQL
//        return session.createQuery("select u from User u", User.class)
//                .list();

        // Criteria API
        CriteriaBuilder cb = session.getCriteriaBuilder();

        // передаем аргументом класс, который будем возвращать из запроса
        CriteriaQuery<User> criteria = cb.createQuery(User.class);

        // Select из root
        Root<User> user = criteria.from(User.class);

        return session.createQuery(criteria).list();
    }

    /**
     * Возвращает всех сотрудников с указанным именем
     */
    public List<User> findAllByFirstName(Session session, String firstName) {
        // HQL
//        return session.createQuery("select u from User u " +
//                                   "where u.personalInfo.firstname = :firstName", User.class)
//                .setParameter("firstName", firstName)
//                .list();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<User> criteria = cb.createQuery(User.class);
        Root<User> user = criteria.from(User.class);

        // select запрос - Select from User u where firstname = :firstname
        criteria.select(user).where(
                cb.equal(user.get("personalInfo").get("firstname"), firstName)
        );

        return  session.createQuery(criteria)
                .list();
    }

    /**
     * Возвращает первые {limit} сотрудников, упорядоченных по дате рождения (в порядке возрастания)
     */
    public List<User> findLimitedUsersOrderedByBirthday(Session session, int limit) {
        // HQL
//        return session.createQuery("select u from User u order by u.personalInfo.birthDate", User.class)
//                .setMaxResults(limit)
////                .setFirstResult(offset)
//                .list();

        // Criteria API
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<User> criteria = cb.createQuery(User.class);
        Root<User> user = criteria.from(User.class);

        criteria.select(user).orderBy(
                cb.asc(user.get("personalInfo").get("birthDate"))
        );

        return session.createQuery(criteria)
                .setMaxResults(limit)
                .list();
    }

    /**
     * Возвращает всех сотрудников компании с указанным названием
     */
    public List<User> findAllByCompanyName(Session session, String companyName) {
        // HQL
//        return session.createQuery("select u from Company c " +
//                                   "join c.users u " +
//                                   "where c.name = :companyName", User.class)
//                .setParameter("companyName", companyName)
//                .list();

        // Criteria API
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<User> criteria = cb.createQuery(User.class);
        Root<Company> company = criteria.from(Company.class);

        Join<String, User> users = company.join("users");

        criteria.select(users).where(
                cb.equal(company.get("name"), companyName)
        );

        return session.createQuery(criteria)
                .list();
    }

    /**
     * Возвращает все выплаты, полученные сотрудниками компании с указанными именем,
     * упорядоченные по имени сотрудника, а затем по размеру выплаты
     */
    public List<Payment> findAllPaymentsByCompanyName(Session session, String companyName) {
        // HQL
//        return session.createQuery("select p from Payment p " +
//                                   "join p.receiver u " +
//                                   "join u.company c " +
//                                   "where c.name = :companyName " +
//                                   "order by u.personalInfo.firstname, p.amount", Payment.class)
//                .setParameter("companyName", companyName)
//                .list();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Payment> criteria = cb.createQuery(Payment.class);
        Root<Payment> payment = criteria.from(Payment.class);
        Join<Payment, User > receiver = payment.join("receiver");
        Join<User, Company> company = receiver.join("company");

        criteria.select(payment).where(
                cb.equal(company.get("name"), companyName))
                .orderBy(
                cb.asc(receiver.get("personalInfo").get("firstname")),
                cb.asc(payment.get("amount"))
        );

        return session.createQuery(criteria)
                .list();
    }

    /**
     * Возвращает среднюю зарплату сотрудника с указанными именем и фамилией
     */
    public Double findAveragePaymentAmountByFirstAndLastNames(Session session,
                                                              String firstName,
                                                              String lastName) {
        // HQL
//        return session.createQuery("select avg(p.amount) from Payment p " +
//                                   "join p.receiver u " +
//                                   "where u.personalInfo.firstname = :firstName " +
//                                   "   and u.personalInfo.lastname = :lastName", Double.class)
//                .setParameter("firstName", firstName)
//                .setParameter("lastName", lastName)
//                .uniqueResult();

        // Criteria
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Double> criteria = cb.createQuery(Double.class);
        Root<Payment> payment = criteria.from(Payment.class);

        Join<Payment, User> receiver = payment.join("receiver");

        // Если приходит DTO с полями, то мы можем динамически их все добавлять.
        List<Predicate> predicates = new ArrayList<>();
        if (firstName != null) {
            predicates.add(cb.equal(receiver.get("personalInfo").get("firstname"), firstName));
        }
        if (lastName != null) {
            predicates.add(cb.equal(receiver.get("personalInfo").get("lastname"), lastName));
        }
        criteria.select(cb.avg(payment.get("amount"))).where(
                predicates.toArray(Predicate[]::new)
        );

//        criteria.select(cb.avg(payment.get("amount"))).where(
//                cb.equal(receiver.get("personalInfo").get("firstname"), firstName),
//                cb.equal(receiver.get("personalInfo").get("lastname"), lastName)
//        );

        return session.createQuery(criteria)
                .uniqueResult();
    }

    /**
     * Возвращает для каждой компании: название, среднюю зарплату всех её сотрудников. Компании упорядочены по названию.
     */
    public List<Object[]> findCompanyNamesWithAvgUserPaymentsOrderedByCompanyName(Session session) {
        // HQL
        return session.createQuery("select c.name, avg(p.amount) from Company c " +
                                   "join c.users u " +
                                   "join u.payments p " +
                                   "group by c.name " +
                                   "order by c.name", Object[].class)
                .list();
    }

    /**
     * Возвращает список: сотрудник (объект User), средний размер выплат, но только для тех сотрудников, чей средний размер выплат
     * больше среднего размера выплат всех сотрудников
     * Упорядочить по имени сотрудника
     */
    public List<Object[]> isItPossible(Session session) {
        return session.createQuery("select u, avg(p.amount) from User u " +
                                   "join u.payments p " +
                                   "group by u " +
                                   "having avg(p.amount) > (select avg(p.amount) from Payment p) " +
                                   "order by u.personalInfo.firstname", Object[].class)
                .list();
    }

    public static UserDao getInstance() {
        return INSTANCE;
    }
}
