/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import beans.Employe;
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
public class EmployeService implements IDao<Employe> {
    
    private static SessionFactory sessionFactory;
    
    public EmployeService(){
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
    public boolean create(Employe o) {
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
    public boolean update(Employe o) {
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
    public boolean delete(Employe o) {
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
    public List<Employe> findAll() {
        List<Employe> employes = null;
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            employes = session.createQuery("from Employe").list();
            tx.commit();
            session.close();
            return employes;
        } catch (HibernateException ex) {
            if (tx != null) {
                tx.rollback();
            }
            session.close();
            return employes;
        }

    }

    @Override
    public Employe findById(int id) {
        Employe e = null;
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            e = (Employe) session.get(Employe.class, id);
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

    public int getEmployesCount() {
        int count = 0;
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            count = ((Long) session.createQuery("select count(e) from Employe e").uniqueResult()).intValue();
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
    
    public Employe getDirecteur() {
        Employe emp = null;
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            emp =  (Employe) session.createQuery("select e from Employe e where e.profil.libelle = 'مدير' ").uniqueResult();
            tx.commit();
            session.close();
            return emp;
        } catch (HibernateException ex) {
            if (tx != null) {
                tx.rollback();
            }
            session.close();
            return emp;
        }
    }
    
    public Employe CheckLogin(String email){
        Employe p = null;
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            p = (Employe) session.createQuery("SELECT e FROM Employe e WHERE e.email =:email").setParameter("email", email).uniqueResult();
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
    
     public List<Object[]> getChartData() {
        List<Object[]> mData = null;
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            mData = session.createQuery("SELECT e.profil.libelle,COUNT(e) FROM Employe e Group by e.profil.libelle").list();
            tx.commit();
            session.close();
            return mData;
        } catch (HibernateException ex) {
            if (tx != null) {
                tx.rollback();
            }
            session.close();
            return mData;
        }

    }
    

}
