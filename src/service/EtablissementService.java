/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import beans.Etablissement;
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
public class EtablissementService  implements IDao<Etablissement> {
    
    private static SessionFactory sessionFactory;
    
    public EtablissementService(){
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
    public boolean create(Etablissement o) {
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
    public boolean update(Etablissement o) {
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
    public boolean delete(Etablissement o) {
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
    public List<Etablissement> findAll() {
        List<Etablissement> etablissements = null;
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            etablissements = session.createQuery("from Etablissement").list();
            tx.commit();
            session.close();
            return etablissements;
        } catch (HibernateException ex) {
            if (tx != null) {
                tx.rollback();
            }
            session.close();
            return etablissements;
        }

    }

    @Override
    public Etablissement findById(int id) {
        Etablissement e = null;
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            e = (Etablissement) session.get(Etablissement.class, id);
            tx.commit();
            session.close();
            return e;
        } catch (HibernateException ex) {
            if (tx != null) {
                tx.rollback();
            }
            session.close();
            return e;
        }

    }
    
    public int getEtablissementsCount() {
        int count = 0;
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            count = ((Long) session.createQuery("select count(e) from Etablissement e").uniqueResult()).intValue();
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
