package util.cache;

import java.util.Collection;

/**
 * @author oscmelto
 * @version 1.0
 * @created 14-nov-2012 10:32:07 a.m.
 */
public interface CacheManagerWorker {

	/**
	 * 
	 * @param clave    Clave con la cual se buscara el elemento
	 * @param clase    Elemento generico que puede ser buscado en la cache
	 */
	public boolean buscarEnCache(String clave, Class clase);

	/**
	 * Permite guardar un elemento Generico en la cache.
	 * 
	 * @param clave    Nombre clave con el que se guardara el elemento en la cache.
	 * @param generico    Elemento generico que se guardara en la cache.
	 */
	public void guardarEnCache(String clave, Object elemento);

	/**
	 * Permite guardar un elemento Generico en la cache.
	 * 
	 * @param clave    Nombre clave con el que se guardara el elemento en la cache.
	 * @param genericos    Elemento generico que se guardara en la cache.
	 */
	public void guardarEnCache(String clave, Collection elementos);
	/**
	 * Permite obtener de la cache un elemento generico a partir de la clave con el
	 * que fue almacenado.
	 * 
	 * @param clave    Clave con la que  se recuperara de la cache el elemento
	 * generico.
	 * @param class    Clase del elemento generico que se espera retornar. 
	 */
	public Object obtenerDeCache(String clave, Class clase);



}