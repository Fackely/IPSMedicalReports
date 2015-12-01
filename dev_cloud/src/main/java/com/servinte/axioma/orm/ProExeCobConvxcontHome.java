package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:55 PM by Hibernate Tools 3.2.5.Beta

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class ProExeCobConvxcont.
 * @see com.servinte.axioma.orm.ProExeCobConvxcont
 * @author Hibernate Tools
 */
public class ProExeCobConvxcontHome {

	private static final Log log = LogFactory
			.getLog(ProExeCobConvxcontHome.class);

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

	public void persist(ProExeCobConvxcont transientInstance) {
		log.debug("persisting ProExeCobConvxcont instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(ProExeCobConvxcont instance) {
		log.debug("attaching dirty ProExeCobConvxcont instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ProExeCobConvxcont instance) {
		log.debug("attaching clean ProExeCobConvxcont instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(ProExeCobConvxcont persistentInstance) {
		log.debug("deleting ProExeCobConvxcont instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ProExeCobConvxcont merge(ProExeCobConvxcont detachedInstance) {
		log.debug("merging ProExeCobConvxcont instance");
		try {
			ProExeCobConvxcont result = (ProExeCobConvxcont) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public ProExeCobConvxcont findById(long id) {
		log.debug("getting ProExeCobConvxcont instance with id: " + id);
		try {
			ProExeCobConvxcont instance = (ProExeCobConvxcont) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.ProExeCobConvxcont", id);
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

	public List findByExample(ProExeCobConvxcont instance) {
		log.debug("finding ProExeCobConvxcont instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.ProExeCobConvxcont").add(
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
