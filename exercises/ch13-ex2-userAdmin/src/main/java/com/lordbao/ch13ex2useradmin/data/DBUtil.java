package com.lordbao.ch13ex2useradmin.data;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class DBUtil {
    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("userAdminPU");
    
    public static EntityManagerFactory getEmFactory() {
        return emf;
    }
}