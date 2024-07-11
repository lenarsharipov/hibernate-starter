package com.lenarsharipov;

import com.lenarsharipov.dao.CompanyRepository;
import com.lenarsharipov.dao.PaymentRepository;
import com.lenarsharipov.dao.UserRepository;
import com.lenarsharipov.dto.UserCreateDto;
import com.lenarsharipov.dto.UserReadDto;
import com.lenarsharipov.entity.Company;
import com.lenarsharipov.entity.Payment;
import com.lenarsharipov.entity.PersonalInfo;
import com.lenarsharipov.entity.User;
import com.lenarsharipov.interceptor.TransactionInterceptor;
import com.lenarsharipov.mapper.CompanyReadMapper;
import com.lenarsharipov.mapper.UserCreateMapper;
import com.lenarsharipov.mapper.UserReadMapper;
import com.lenarsharipov.service.UserService;
import com.lenarsharipov.util.HibernateUtil;
import com.lenarsharipov.util.TestDataImporter;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.annotations.QueryHints;
import org.hibernate.graph.RootGraph;
import org.hibernate.jdbc.Work;
import org.hibernate.jpa.AvailableHints;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HibernateRunner {

    @Transactional
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        try (SessionFactory sf = HibernateUtil.buildSessionFactory()) {
            var session = (Session) Proxy.newProxyInstance(
                    SessionFactory.class.getClassLoader(),
                    new Class[]{Session.class},
                    (proxy, method, args1) -> method.invoke(sf.getCurrentSession(), args1));

            var companyReadMapper = new CompanyReadMapper();
            var userReadMapper = new UserReadMapper(companyReadMapper);
            var userRepository = new UserRepository(session);
            var companyRepository = new CompanyRepository(session);
            var userCreateMapper = new UserCreateMapper(companyRepository);

            TransactionInterceptor transactionInterceptor = new TransactionInterceptor(sf);
            UserService userService = new ByteBuddy()
                    .subclass(UserService.class)
                    .method(ElementMatchers.any()) // проверь все методы, есть ли там Transactional
                    .intercept(MethodDelegation.to(transactionInterceptor)) // проверку делегируем нашему TransactionInterceptor
                    .make() // динамически загружает новый класс в память
                    .load(UserService.class.getClassLoader())
                    .getLoaded()
                    .getDeclaredConstructor(UserRepository.class, UserReadMapper.class, UserCreateMapper.class)
                    .newInstance(userRepository, userReadMapper, userCreateMapper);

        }
    }
}