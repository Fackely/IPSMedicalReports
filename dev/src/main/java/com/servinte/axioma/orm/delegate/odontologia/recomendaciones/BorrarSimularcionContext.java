package com.servinte.axioma.orm.delegate.odontologia.recomendaciones;

import java.util.List;

import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.RecomendacionesContOdontoHome;


/**
 * 
 * @author Edgar Carvajal Ruiz
 *
 * @param <T>
 */
public class BorrarSimularcionContext <T> {

	
				
			/**	
			 * malo
			 */
		private static final Log log = LogFactory.getLog(RecomendacionesContOdontoHome.class);
			
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
			
		
		public void persist(T transientInstance) {
			log.debug("persisting RecomendacionesContOdonto instance");
			try {
				sessionFactory.getCurrentSession().persist(transientInstance);
				log.debug("persist successful");
			} catch (RuntimeException re) {
				log.error("persist failed", re);
				throw re;
			}
			}
			
			public void attachDirty(T instance) {
			log.debug("attaching dirty RecomendacionesContOdonto instance");
			try {
				sessionFactory.getCurrentSession().saveOrUpdate(instance);
				log.debug("attach successful");
			} catch (RuntimeException re) {
				log.error("attach failed", re);
				throw re;
			}
			}
			
			public void attachClean(T instance) {
			log.debug("attaching clean RecomendacionesContOdonto instance");
			try {
				sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
				log.debug("attach successful");
			} catch (RuntimeException re) {
				log.error("attach failed", re);
				throw re;
			}
			}
			
			public void delete(T persistentInstance) {
			log.debug("deleting RecomendacionesContOdonto instance");
			try {
				sessionFactory.getCurrentSession().delete(persistentInstance);
				log.debug("delete successful");
			} catch (RuntimeException re) {
				log.error("delete failed", re);
				throw re;
			}
			}
			
			
			/**
			 * 
			 * @param detachedInstance
			 * @return
			 */
			public T merge(	T detachedInstance) {
			log.debug("merging RecomendacionesContOdonto instance");
			try {
				T result = (T) sessionFactory.getCurrentSession().merge(detachedInstance);
				
				log.debug("merge successful");
				return result;
			} catch (RuntimeException re) {
				log.error("merge failed", re);
				throw re;
			}
			}
		
			
			
			/**
			 * 
			 * @param id
			 * @return
			 */
			public Object findById(int id, T obj) {
			
				
				log.debug("getting RecomendacionesContOdonto instance with id: " + id);
			
				try {
				Object instance = (Object) sessionFactory.getCurrentSession().get(obj.getClass() ,id);
				
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
			
			
			/**
			 * 
			 * 
			 * @param instance
			 * @return
			 */
			public List findByExample(T instance) {
			log.debug("finding RecomendacionesContOdonto instance by example");
			try {
				List results = sessionFactory.getCurrentSession().createCriteria(instance.getClass()).add(Example.create(instance)).list();
				log.debug("find by example successful, result size: "+ results.size());
				return results;
			} catch (RuntimeException re) {
				log.error("find by example failed", re);
				throw re;
			}
			}
	
	
	
}
