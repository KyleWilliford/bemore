package net.kpw.bemore.dao;

import org.hibernate.SessionFactory;

public class DataDAO {
    
    private final SessionFactory sessionFactory;

    public DataDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

}
