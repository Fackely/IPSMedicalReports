package com.servinte.axioma.fwk.persistencia.interfaz;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.servinte.axioma.fwk.exception.IPSException;

/**
 * Interface que expone los servicios de integración con la base de datos
 * haciendo uso de Hibernate
 * 
 * @author ricruico
 * @version 1.0
 * @created 20-jun-2012 02:24:01 p.m.
 */
public interface IPersistenciaSvc {

	/**
	 * Crea un namedQuery de HQL
	 * @param namedQuery , el nombre de la consulta que se desea cargar
	 * @return Retorna una lista de Objetos de acuerdo a la consulta HQL generada
	 */	
	List<?> createNamedQuery(String namedQuery);

	/**
	 * Crea un namedQuery de HQL que recibe un mapa de parámetros
	 * @param namedQuery , el nombre de la consulta que se desea cargar
	 * @param parameters , el Mapa con los parametros a pasar a la consulta la que se desea ejecutar
	 * @return Retorna una lista de Objetos de acuerdo a la consulta HQL generada
	 */	
	List<?> createNamedQuery(String namedQuery, Map<String,Object> parameters);

	/**
	 * Crea un namedQuery de HQL que devuelve un único resultado
	 * Si la consulta arroja mas de un resultado este método lanzará una excepción
	 * @param namedQuery , el nombre de la consulta que se desea cargar
	 * @return Retorna un Objeto de acuerdo a la consulta HQL generada
	 */	
	Object createNamedQueryUniqueResult(String namedQuery);

	/**
	 * Crea un namedQuery de HQL que recibe un mapa de parámetros y que 
	 * devuelve el primer registro obtenido por la consulta
	 * Si la consulta arroja mas de un resultado este método lanzará una excepción
	 * @param namedQuery , el nombre de la consulta que se desea cargar
	 * @param parameters , el Mapa con los parametros a pasar a la consulta la que se desea ejecutar
	 * @return Retorna una lista de Objetos de acuerdo a la consulta HQL generada
	 */	
	Object createNamedQueryFirstResult(String namedQuery, Map<String,Object> parameters);
	
	
	/**
	 * Crea un namedQuery de HQL que devuelve devuelve el primer registro obtenido por la consulta
	 * @param namedQuery , el nombre de la consulta que se desea cargar
	 * @return Retorna un Objeto de acuerdo a la consulta HQL generada
	 */	
	Object createNamedQueryFirstResult(String namedQuery);

	/**
	 * Crea un namedQuery de HQL que recibe un mapa de parámetros y que 
	 * devuelve un único resultado
	 * @param namedQuery , el nombre de la consulta que se desea cargar
	 * @param parameters , el Mapa con los parametros a pasar a la consulta la que se desea ejecutar
	 * @return Retorna una lista de Objetos de acuerdo a la consulta HQL generada
	 */	
	Object createNamedQueryUniqueResult(String namedQuery, Map<String,Object> parameters);

	
	/**
	 * Retorna la instancia del entity asociado con el primary key, si no existela instancia 
	 * retorna null
	 * @param entityClass el tipo de entity
	 * @param primaryKey la llave primaria del objeto
	 * @return La instancia del objeto o null
	 */
	<T> T find(Class<T> entityClass, Object primaryKey);

	/**
	 * Obliga al sessionFactory a enviar las operaciones de actualizacion a la base de datos.
	 * Su principal funcion es sincronizar la base de datos con el estado mantendo en memoria
	 */
	void flush();

	/**
	 * Copia el estado del objeto pasado como parametro dentro del objeto en contexto de persistencia
	 * que tenga el mismo identificador. Si el objeto no existe este sera cargado y posteriormente retornado
	 * La politica de sincronizacion esta basada sobre las politicas definidas en el Cascade
	 * Las semanticas de este metodo estan definidas en JSR-220.
	 *
	 * @param entity un objeto "deattached" que espera ser sincronizado
	 * @return la referencia del objeto actualizada
	 */
	<T> T merge(T entity);

	/**
	 * Crea una nueva instancia del objeto pasado como parametro, se debe tener en cuenta que 
	 * debe ser una instancia nueva, si es el caso de un objeto deattached lanzara una excepcion
	 * Las semanticas de este metodo estan definidas en JSR-220.
	 *
	 * @param entity el objeto que desea ser creado
	 */
	void persist(Object entity);
	
	/**
	 * Ejecuta un updateNamedQuery de HQL
	 * @param updateNameQuery , el nombre de la sentencia HQL que se desea ejecutar (UPDATE, DELETE)
	 * @return Retorna el número de Objetos de Actualizados o Borrados por la sentencia HQL ejecutada
	 */	
	int createUpdateNamedQuery(String updateNameQuery) throws IPSException;
	
	/**
	 * Ejecuta un updateNameQuery de HQL que recibe parámetros
	 * @param updateNameQuery , el nombre de la sentencia HQL que se desea ejecutar (UPDATE, DELETE)
	 * @param parameters , el Mapa con los parametros a pasar a la consulta la que se desea ejecutar
	 * @return Retorna el número de Objetos de Actualizados o Borrados por la sentencia HQL ejecutada
	 */	
	int createUpdateNamedQuery(String updateNameQuery, Map<String, Object> parameters) throws IPSException;
	
	/**
	 * Metodo que obtiene la sessión de Hibernate. 
	 * ESTE METODO SOLO DEBE SER UTILIZADO CUANDO SE NECESITA IMPLEMENTAR
	 * CRITERIA
	 * @return
	 */
	Session getSession();
	

}