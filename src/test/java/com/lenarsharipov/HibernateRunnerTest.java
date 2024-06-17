package com.lenarsharipov;

import com.lenarsharipov.entity.*;
import com.lenarsharipov.util.HibernateTestUtil;
import com.lenarsharipov.util.HibernateUtil;
import jakarta.persistence.FlushModeType;
import lombok.Cleanup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.QueryHints;
import org.hibernate.jpa.AvailableHints;
import org.hibernate.jpa.HibernateHints;
import org.hibernate.query.Query;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static java.util.stream.Collectors.joining;

class HibernateRunnerTest {

    @Test
    void checkHql() {
        try (var sf = HibernateTestUtil.buildSessionFactory();
            var session = sf.openSession()) {
            session.beginTransaction();

            String name = "Ivan";
            List<User> users = session.createNamedQuery("findUserByName", User.class)
                    .setParameter("firstname", name)
                    .setParameter("companyName", "Google")
                    .setFlushMode(FlushModeType.AUTO)
                    .list();

            session.getTransaction().commit();
        }
    }

    @Test
    void checkH2() {
        try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            Company google = Company.builder()
                    .name("Google")
                    .build();
            session.persist(google);

            Programmer programmer = Programmer.builder()
                    .username("ivan@mail.com")
                    .language(Language.JAVA)
                    .company(google)
                    .build();

            session.persist(programmer);

            Manager manager = Manager.builder()
                    .username("sveta@mail.com")
                    .projectName("Starter")
                    .company(google)
                    .build();

            session.persist(manager);
            /*
            * Сразу делаем flush, чтобы увидеть проблему
            * */
            session.flush();

            /*
            * чистим сессию
            * */
            session.clear();

            /*
            * выполним запросы на получение
            * программиста и менеджера из БД
            * */
            Programmer inDbProgrammer = session.get(Programmer.class, 1L);
            User inDbManager = session.get(User.class, 2L);
            System.out.println();

            session.getTransaction().commit();
        }
    }

    @Test
    void localeInfo() {
    try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            Company company = session.get(Company.class, 1);
//            company.getLocales().addAll(
//                    List.of(
//                        LocaleInfo.of("ru", "Описание на русском"),
//                        LocaleInfo.of("tr", "Turkce tanitim"),
//                        LocaleInfo.of("en", "Description in English")
//                    ));
            company.getUsers().forEach(System.out::println);

            session.getTransaction().commit();
        }
    }

    @Test
    void checkManyToMany() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            User user = session.get(User.class, 1);


            Chat chat = session.get(Chat.class, 1);


//            UserChat userChat = UserChat.builder()
//                    .createdAt(Instant.now())
//                    .createdBy(user.getUsername())
//                    .build();
//
//
//            userChat.setUser(user);
//            userChat.setChat(chat);
//
//
//            session.persist(userChat);

            session.getTransaction().commit();
        }
    }

    @Test
    void checkOneToOne() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

//            User user = User.builder()
//                    .username("test@mail.com")
//                    .build();
//            Profile profile = Profile.builder()
//                    .language("ru")
//                    .street("Kasimov brothers 74")
//                    .build();
//            profile.setUser(user);
//            session.persist(user);

            session.getTransaction().commit();
        }
    }

    @Test
    void checkOrphanRemoval() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            Company company = session.get(Company.class, 2);
//            Set<User> users = company.getUsers();
//            users.removeIf(user -> user.getId().equals(1L));

            session.getTransaction().commit();
        }
    }

    @Test
    void checkLazyInitialization() {
        Company company;
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            company = session.get(Company.class, 1);

            session.getTransaction().commit();
        }

//        Set<User> users = company.getUsers();
//        System.out.println(users.size());
    }

    @Test
    void deleteCompany() {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        Company company = Company.builder()
                .name("Facebook")
                .build();

//        User user = User.builder()
//                .username("sveta@mail.com")
//                .build();
//        company.addUser(user);

        session.persist(company);

        session.getTransaction().commit();
    }

    @Test
    void addUserToNewCompany() {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        Company company = Company.builder()
                .name("Facebook")
                .build();

//        User user = User.builder()
//                .username("sveta@mail.com")
//                .build();
//        company.addUser(user);

        session.persist(company);

        session.getTransaction().commit();
    }

    @Test
    void oneToMany() {
        @Cleanup SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        Company company = session.get(Company.class, 2);
        System.out.println(company.getUsers());

        session.getTransaction().commit();
    }

    @Test
    void checkGetReflectionApi() throws SQLException,
            NoSuchMethodException,
            InvocationTargetException,
            InstantiationException,
            IllegalAccessException,
            NoSuchFieldException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.getString("username");
        resultSet.getString("lastname");
        resultSet.getString("lastname");
        // Теперь устанавливаем значения полей значениями из ResultSet

        Class<User> clazz = User.class;
        Constructor<User> constructor = clazz.getConstructor();
        User user = constructor.newInstance();
        Field usernameField = clazz.getDeclaredField("username");
        usernameField.setAccessible(true);
        usernameField.set(
                user,
                resultSet.getString("username"));
    }

//    @Test
//    void checkReflectionApi() throws SQLException, IllegalAccessException {
//        User user = User.builder()
//                .build();
//
//        String sql = """
//                insert
//                into
//                %s
//                (%s)
//                values
//                (%s)
//                """;
//        /*
//        * Получим всю информацию для нашего sql запроса
//        * через ReflectionApi.
//        * */
//        String tableName = ofNullable(user.getClass().getAnnotation(Table.class))
//                .map(tableAnnotation -> tableAnnotation.schema() + "." + tableAnnotation.name())
//                .orElse(user.getClass().getName());
//
//        Field[] declaredFields = user.getClass().getDeclaredFields();
//        String columnNames = Arrays.stream(declaredFields)
//                .map(field -> ofNullable(field.getAnnotation(Column.class))
//                        .map(Column::name)
//                        .orElse(field.getName()))
//                .collect(joining(", "));
//
//        String columnValues = Arrays.stream(declaredFields)
//                .map(field -> "?")
//                .collect(joining(", "));
//
//        System.out.println(sql.formatted(tableName, columnNames, columnValues));
//
//        Connection connection = null;
//        PreparedStatement preparedStatement = connection.prepareStatement(
//                sql.formatted(tableName, columnNames, columnValues));
//        for (Field declaredField : declaredFields) {
//            declaredField.setAccessible(true);
//            preparedStatement.setObject(1, declaredField.get(user));
//        }
//    }

}