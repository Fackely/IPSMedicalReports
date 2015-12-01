package com.servinte.axioma.orm;

// Generated 26/03/2012 03:36:48 PM by Hibernate Tools 3.4.0.CR1

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class NotaAclaratoria.
 * @see com.servinte.axioma.orm.NotaAclaratoria
 * @author Hibernate Tools
 */
public class NotaAclaratoriaHome {

	private static final Log log = LogFactory.getLog(NotaAclaratoriaHome.class);

	protected final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

	protected SessionFactory getSessionFactory() {
		try {
			return (SessionFactory) new InitialContext()
					.lookup("SessionFactory");
		} catch (Exception e) {
			log.error("Could not locate SessionFactory in JNDI", e);
			throw new IllegalStateException(
					"Could not locate SessionFactory in JNDI");
		}
	}

	public void persist(NotaAclaratoria transientInstance) {
		log.debug("persisting NotaAclaratoria instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(NotaAclaratoria instance) {
		log.debug("attaching dirty NotaAclaratoria instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(NotaAclaratoria instance) {
		log.debug("attaching clean NotaAclaratoria instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(NotaAclaratoria persistentInstance) {
		log.debug("deleting NotaAclaratoria instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public NotaAclaratoria merge(NotaAclaratoria detachedInstance) {
		log.debug("merging NotaAclaratoria instance");
		try {
			NotaAclaratoria result = (NotaAclaratoria) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public NotaAclaratoria findById(long id) {
		log.debug("getting NotaAclaratoria instance with id: " + id);
		try {
			NotaAclaratoria instance = (NotaAclaratoria) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.NotaAclaratoria", id);
			if (instance == null) {
				log.debug("get successful, no instance found");
			} else {
				log.debug("get successful, instance found");
			}
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(NotaAclaratoria instance) {
		log.debug("finding NotaAclaratoria instance by example");
		try {
			List results = sessionFactory.getCurrentSession()
					.createCriteria("com.servinte.axioma.orm.NotaAclaratoria")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
}
