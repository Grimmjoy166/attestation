/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import beans.Attestation;
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
public class AttestationService implements IDao<Attestation> {

    private static SessionFactory sessionFactory;

    public AttestationService() {
        Configuration configuration = new Configuration();
        Preferences userPreferences = Preferences.userRoot();

        configuration.configure("/config/hibernate.cfg.xml");

        System.out.println("URL BEFORE : " + configuration.getProperty("hibernate.connection.url"));
        configuration.setProperty("hibernate.connection.url", userPreferences.get("DB_URL", "NULL"));
        configuration.setProperty("hibernate.connection.username", userPreferences.get("DB_USERNAME", "NULL"));
        configuration.setProperty("hibernate.connection.password", userPreferences.get("DB_PASSWORD", "NULL"));

        System.out.println("URL AFTER : " + configuration.getProperty("hibernate.connection.url"));

        sessionFactory = configuration.buildSessionFactory();

    }

    @Override
    public boolean create(Attestation o) {
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

    public Attestation createWithFeedBack(Attestation o) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            session.save(o);
            tx.commit();
            session.close();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
                session.close();
            }
        }
        return o;
    }

    @Override
    public boolean update(Attestation o) {
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
    public boolean delete(Attestation o) {
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
    public List<Attestation> findAll() {
        List<Attestation> attestations = null;
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            attestations = session.createQuery("from Attestation").list();
            tx.commit();
            session.close();
            return attestations;
        } catch (HibernateException ex) {
            if (tx != null) {
                tx.rollback();
            }
            session.close();
            return attestations;
        }

    }

    @Override
    public Attestation findById(int id) {
        Attestation a = null;
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            a = (Attestation) session.get(Attestation.class, id);
            tx.commit();
            session.close();
            return a;
        } catch (HibernateException ex) {
            if (tx != null) {
                tx.rollback();
            }
            session.close();
            return a;
        }

    }

    public int getAttestationsCount() {
        int count = 0;
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            count = ((Long) session.createQuery("select count(a) from Attestation a").uniqueResult()).intValue();
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

    public int getLastInsertedYear() {
        int year = 0;
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            year = (int) session.createQuery("SELECT YEAR(a.id.dateEdition) from Attestation a ORDER BY a.id.dateEdition DESC ").setMaxResults(1).uniqueResult();
            tx.commit();
            session.close();
            return year;
        } catch (HibernateException ex) {
            if (tx != null) {
                tx.rollback();
            }
            session.close();
            return year;
        }

    }

}
