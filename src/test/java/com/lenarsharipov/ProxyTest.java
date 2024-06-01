package com.lenarsharipov;

import com.lenarsharipov.entity.Company;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyTest {

    @Test
    void testDynamic() {
        /*
        * Передаем аргументами:
        * 1. ClassLoader, в который был загружен класс
        * 2. Набор интерфейсов нашего класса, на который мы делаем прокси.
        * Но в нашем случае, Сompany не имплементируют интерфейсов.
        * 3. InvocationHandler - функциональный интерфейс, через который
        * занимается обработкой всех вызовов нашего прокси.
        * */
        Company company = new Company();
        Proxy.newProxyInstance(
                company.getClass().getClassLoader(),
                company.getClass().getInterfaces(),

                /*
                * метод принимает:
                * 1. сам прокси объект,
                * 2. метод, который был вызван у прокси
                * 3. набор аргументов, которые были переданы
                * в метод
                * В invoke() мы можем определять что делать с
                * этим методом. Вызывать его у реального объекта.
                * Или не вызывать реальный объект, а использовать
                * cache или что-то еще. Вариантов очень много.
                * Паттерн прокси очень распространен и будет
                * встречаться очень часто в различных
                * фреймворках.
                *
                * new InvocationHandler() {

                     @Override
                    public Object invoke(Object proxy,
                                        Method method,
                                        Object[] args) throws Throwable {
                        return null;
                    }
                });
                * */

                // создадим нужный нам прокси, который использует Hibernate через extends
                (proxy, method, args) -> null);
    }
}
