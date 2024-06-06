package com.lordbao.ch13ex1email.data;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * @Author Lord_Bao
 * @Date 2024/6/6 17:01
 * @Version 1.0
 */
public class DBUtil {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("emailListPU");


    public static EntityManagerFactory getEmFactory(){
        return emf;
    }
}
