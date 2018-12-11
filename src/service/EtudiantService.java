/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import beans.Employe;
import beans.Etablissement;
import beans.Etudiant;
import dao.IDao;
import java.util.Date;
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
public class EtudiantService implements IDao<Etudiant> {
    
    private static SessionFactory sessionFactory;
    
    public EtudiantService(){
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
    public boolean create(Etudiant o) {
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
    public boolean update(Etudiant o) {
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
    public boolean delete(Etudiant o) {
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
    public List<Etudiant> findAll() {
        List<Etudiant> etudiants = null;
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            etudiants = session.createQuery("from Etudiant").list();
            tx.commit();
            session.close();
            return etudiants;
        } catch (HibernateException ex) {
            if (tx != null) {
                tx.rollback();
            }
            session.close();
            return etudiants;
        }

    }
    

    public List<Etudiant> findAllbyEtab(Etablissement etablissement) {
        List<Etudiant> etudiants = null;
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            etudiants = session.createQuery("select e from Etudiant e where e.etablissement.id = ?").setParameter(0, etablissement.getId()).list();
            tx.commit();
            session.close();
            return etudiants;
        } catch (HibernateException ex) {
            if (tx != null) {
                tx.rollback();
            }
            session.close();
            return etudiants;
        }

    }
    
    @Override
    public Etudiant findById(int id) {
        Etudiant e = null;
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            e = (Etudiant) session.get(Etudiant.class, id);
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

    public int getEtudiantsCount() {
        int count = 0;
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            count = ((Long) session.createQuery("select count(e) from Etudiant e").uniqueResult()).intValue();
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
    
    

    public List<Object[]> getChartData() {
        List<Object[]> mData = null;
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            mData = session.createQuery("SELECT et.nom,COUNT(e) FROM Etudiant e,Etablissement et WHERE e.etablissement.id = et.id Group by et.nom").list();
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
    
     public List<Object[]> getPieChartData() {
        List<Object[]> mData = null;
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            mData = session.createQuery("SELECT DISTINCT(e.niveauEtude),COUNT(e) FROM Etudiant e  Group by e.niveauEtude").list();
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
     
     public List<Object[]> getPieChartData2(int year) {
        List<Object[]> mData = null;
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            mData = session.createQuery("SELECT DISTINCT(e.decision) ,COUNT(e) FROM Etudiant e WHERE  YEAR(e.dateSortie) = :year  Group by e.decision")
                                        .setParameter("year", year)
                                        .list();
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

    
     public Boolean isNotExist(String nomComplet, Date dateNaissance) {
        Etudiant e = null;
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            e = (Etudiant) session.createQuery("SELECT e FROM Etudiant e WHERE  e.nomComplet =:nomComplet  AND e.dateNaissance =:dateNaissance")
                                    .setParameter("nomComplet", nomComplet)
                                    .setParameter("dateNaissance", dateNaissance)
                                    .uniqueResult();
            tx.commit();
            session.close();
        } catch (HibernateException ex) {
            if (tx != null) {
                tx.rollback();
            }
            session.close();
        }
        
        if(e == null) {
            return true;
        }else {
            return false;
        }
    }
     
     
}
