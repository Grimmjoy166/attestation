/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import beans.Profil;
import dao.IDao;
import java.util.List;
import java.util.prefs.Preferences;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import util.HibernateUtil;

/**
 *
 * @author Sinponzakra
 */
public class ProfilService implements IDao<Profil>{
    
    private static SessionFactory sessionFactory;
    
    public ProfilService(){
        Configuration configuration = new Configuration();
        Preferences userPreferences = Preferences.userRoot();

        configuration.configure("/config/hibernate.cfg.xml");
        
        System.out.println("URL BEFORE : "+configuration.getProperty("hibernate.connection.url"));
        configuration.setProperty("hibernate.connection.url", userPreferences.get("DB_URL", "NULL"));
        configuration.setProperty("hibernate.connection.username", userPreferences.get("DB_USERNAME", "NULL"));
        configuration.setProperty("hibernate.connection.password", userPreferences.get("DB_PASSWORD", "NULL"));
        
        System.out.println("URL AFTER : "+configuration.getProperty("hibernate.connection.url"));
    
        sessionFactory = configuration.buildSessionFactory();

    }
    
     @Override
    public boolean create(Profil o) {
         Session session = sessionFactory.openSession();
         Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(o);
            tx.commit();
            session.close();
            return true;
        } catch (HibernateException ex) {
            if (tx != null) {
                tx.rollback();
            }
            session.close();
            return false;
        }
    }

    @Override
    public boolean update(Profil o) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(o);
            tx.commit();
            session.close();
            return true;
        } catch (HibernateException ex) {
            if (tx != null) {
                tx.rollback();
            }
            session.close();
            return false;
        }
    }

    @Override
    public boolean delete(Profil o) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(o);
            tx.commit();
            session.close();
            return true;
        } catch (HibernateException ex) {
            if (tx != null) {
                tx.rollback();
            }
            session.close();
            return false;
        }

    }

    @Override
    public List<Profil> findAll() {
        List<Profil> profils = null;
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            profils = session.createQuery("from Profil").list();
            tx.commit();
            session.close();
            return profils;
        } catch (HibernateException ex) {
            if (tx != null) {
                tx.rollback();
            }
            session.close();
            return profils;
        }

    }

    @Override
    public Profil findById(int id) {
        Profil p = null;
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            p = (Profil) session.get(Profil.class, id);
            tx.commit();
            session.close();
            return p;
        } catch (HibernateException ex) {
            if (tx != null) {
                tx.rollback();
            }
            session.close();
            return p;
        }

    }

    public int getProfilsCount() {
        int count = 0;
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            count = ((Long) session.createQuery("select count(p) from Profil p").uniqueResult()).intValue();
            tx.commit();
            session.close();
            return count;
        } catch (HibernateException ex) {
            if (tx != null) {
                tx.rollback();
            }
            session.close();
            return count;
        }
    }
   
}
