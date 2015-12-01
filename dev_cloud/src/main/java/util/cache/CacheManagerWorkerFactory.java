package util.cache;

import java.util.ResourceBundle;

import util.cache.excepciones.ExcepcionInstanciacionCache;

public class CacheManagerWorkerFactory {

	
	private final static ResourceBundle FUENTE_PROPIEDADES = ResourceBundle.getBundle("util.cache.cache"); 
	private static CacheManagerWorker CACHE_MANAGER_WORKER  = null; 
	public static CacheManagerWorker obtenerCacheManager() throws ExcepcionInstanciacionCache{
		if (CACHE_MANAGER_WORKER != null)
		{
			
			return CACHE_MANAGER_WORKER;
		}
		else
		{
			if (FUENTE_PROPIEDADES != null)
			{
				String nombreClaseManager = FUENTE_PROPIEDADES.getString("cache.managerFactory");
				try {
			        Class clase = Class.forName(nombreClaseManager);
			        CACHE_MANAGER_WORKER = (CacheManagerWorker)clase.newInstance();
			    } catch (ClassNotFoundException e) {
			    	throw new ExcepcionInstanciacionCache(e);
			    } catch (InstantiationException e) {
			    	throw new ExcepcionInstanciacionCache(e);
				} catch (IllegalAccessException e) {
					throw new ExcepcionInstanciacionCache(e);
				}
				catch (Exception e) {
					throw new ExcepcionInstanciacionCache(e);
				}
			}
			else
			{
				throw new ExcepcionInstanciacionCache("No se ha encontrado el archivo de configuracion en util.cache.cache.properties");
			}
			return CACHE_MANAGER_WORKER;
		}
	}
}
