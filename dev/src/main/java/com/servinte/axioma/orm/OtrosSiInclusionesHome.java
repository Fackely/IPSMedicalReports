package com.servinte.axioma.orm;

// Generated Dec 2, 2010 7:02:45 PM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class OtrosSiInclusiones.
 * @see com.servinte.axioma.orm.OtrosSiInclusiones
 * @author Hibernate Tools
 */
public class OtrosSiInclusionesHome {

	private static final Log log = LogFactory
			.getLog(OtrosSiInclusionesHome.class);

	private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

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

	public void persist(OtrosSiInclusiones transientInstance) {
		log.debug("persisting OtrosSiInclusiones instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(OtrosSiInclusiones instance) {
		log.debug("attaching dirty OtrosSiInclusiones instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(OtrosSiInclusiones instance) {
		log.debug("attaching clean OtrosSiInclusiones instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(OtrosSiInclusiones persistentInstance) {
		log.debug("deleting OtrosSiInclusiones instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public OtrosSiInclusiones merge(OtrosSiInclusiones detachedInstance) {
		log.debug("merging OtrosSiInclusiones instance");
		try {
			OtrosSiInclusiones result = (OtrosSiInclusiones) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public OtrosSiInclusiones findById(long id) {
		log.debug("getting OtrosSiInclusiones instance with id: " + id);
		try {
			OtrosSiInclusiones instance = (OtrosSiInclusiones) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.OtrosSiInclusiones", id);
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

	public List findByExample(OtrosSiInclusiones instance) {
		log.debug("finding OtrosSiInclusiones instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.OtrosSiInclusiones").add(
					Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
}
