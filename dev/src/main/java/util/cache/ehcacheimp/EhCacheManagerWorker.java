package util.cache.ehcacheimp;
import java.util.Collection;
import java.util.Iterator;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import util.cache.CacheManagerWorker;

/**
 * Efectua la implementacion para usar ehcache como framework.
 * La instancia del manager se hace a traves del modo singleton y el shutdown de la cache 
 * se realiza por demanda, al elimnar el proceso del servidor.
 * No se permite editar programaticamente la cache ya que esto no es requerido, un comportamiento
 * mas especializado deberia ser implementado de otra forma.
 * @author oscmelto
 * @version 1.0
 * @created 14-nov-2012 10:32:26 a.m.
 */
public class EhCacheManagerWorker implements CacheManagerWorker {
	private static CacheManager MANAGER;
	
	{
		/*
		 * el archivo de configuracion ehcache.xml deberia ser ubicado en la raiz classpath 
		 */
		MANAGER = CacheManager.newInstance();
	}
	
	@Override
	public boolean buscarEnCache(String clave, Class clase) {
		boolean encontrado = false;
		if (clave != null && clave.trim().length() > 0 && clase != null)
		{
			encontrado = obtenerDeCache(clave, clase)!= null;	
		}
		return encontrado;
	}

	@Override
	public void guardarEnCache(String clave, Object elemento) {
		Cache cache = MANAGER.getCache("cache_"+elemento.getClass().getCanonicalName());
		if (cache != null && clave != null && elemento != null)
		{
			Element elementoCache = new Element(clave, elemento);
			guardarEnCache(cache, elementoCache);
		}
	}

	private void guardarEnCache(Cache cache, Element elemento)
	{
		if (cache != null && elemento != null)
			cache.put(elemento);		
	}
	
	@Override
	public void guardarEnCache(String clave, Collection elementos) {
		if (clave != null && clave.trim().length()>0 &&
				elementos != null && !elementos.isEmpty())
		{
			Iterator iterador = elementos.iterator();
			Object elemento = iterador.next();
			Cache cache = MANAGER.getCache("cache_"+elemento.getClass().getCanonicalName());
			Element elementoCache = new Element(clave, elementos);
			guardarEnCache(cache, elementoCache);
		}
	}

	@Override
	public Object obtenerDeCache(String clave, Class clase) {
		if (clave != null && clave.trim().length()> 0 && clase != null)
		{
			Cache cache = MANAGER.getCache("cache_"+clase.getCanonicalName());
			Element element = cache.get(clave);
			if (element != null)
				return element.getObjectValue();
			else
				return null;
		}
		return null;
	}

	private Cache obtenerCache()
	{
		return null;
	}
}