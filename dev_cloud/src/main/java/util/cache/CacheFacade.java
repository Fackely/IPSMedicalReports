package util.cache;

import java.util.Collection;

import util.cache.excepciones.ExcepcionInstanciacionCache;

/**
 * @author oscmelto
 * @version 1.0
 * @created 14-nov-2012 10:23:20 a.m.
 */
public class CacheFacade {
	
	private CacheManagerWorker obtenerCacheManager() throws ExcepcionInstanciacionCache
	{
		
		return CacheManagerWorkerFactory.obtenerCacheManager();
	}

	/**
	 * 
	 * @param clave    Clave con la cual se buscara el elemento
	 * @param clase    Elemento generico que puede ser buscado en la cache
	 */
	public boolean buscarEnCache(String clave, Class clase){ 
		CacheManagerWorker worker;
		try {
			worker = obtenerCacheManager();
			return worker.buscarEnCache(clave, clase);
		} catch (ExcepcionInstanciacionCache e) {
		}
		return false;
		
	}

	/**
	 * Permite guardar un elemento Generico en la cache.
	 * 
	 * @param clave    Nombre clave con el que se guardara el elemento en la cache.
	 * @param generico    Elemento generico que se guardara en la cache.
	 */
	public void guardarEnCache(String clave, Object elemento){ 
		CacheManagerWorker worker;
		try {
			worker = obtenerCacheManager();
			worker.guardarEnCache(clave, elemento);
		} catch (ExcepcionInstanciacionCache e) {
		}
	}

	/**
	 * Permite guardar un elemento Generico en la cache.
	 * 
	 * @param clave    Nombre clave con el que se guardara el elemento en la cache.
	 * @param genericos    Elemento generico que se guardara en la cache.
	 */
	public void guardarEnCache(String clave, Collection elementos){
		CacheManagerWorker worker;
		try {
			worker = obtenerCacheManager();
			worker.guardarEnCache(clave, elementos);
		} catch (ExcepcionInstanciacionCache e) {
		}
	}
	/**
	 * Permite obtener de la cache un elemento generico a partir de la clave con el
	 * que fue almacenado.
	 * 
	 * @param clave    Clave con la que  se recuperara de la cache el elemento
	 * generico.
	 * @param class    Clase del elemento generico que se espera retornar. 
	 */
	public Object obtenerDeCache(String clave, Class clase){
		CacheManagerWorker worker;
		try {
			worker = obtenerCacheManager();
			return worker.obtenerDeCache(clave, clase);
		} catch (ExcepcionInstanciacionCache e) {
		}
		return null;
	}



}