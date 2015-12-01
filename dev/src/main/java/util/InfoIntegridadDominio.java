package util;

import java.io.Serializable;

import org.axioma.util.log.Log4JManager;

/**
 * 
 * @author axioma
 *
 */
public class InfoIntegridadDominio implements Serializable, Cloneable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 9197212052053794877L;
	
	private String acronimo;
	private String nombre;
	
	/**
	 * 
	 * @param acronimo
	 * @param nombre
	 */
	public InfoIntegridadDominio(String acronimo, String nombre) {
		super();
		this.acronimo = acronimo;
		this.nombre = nombre;
	}

	/**
	 * 
	 * @param acronimo
	 * @param nombre
	 */
	public InfoIntegridadDominio() {
		super();
		this.acronimo = "";
		this.nombre = "";
	}
	
	/**
	 * 
	 * @param acronimo
	 * @param nombre
	 */
	public InfoIntegridadDominio(String acronimo) {
		super();
		this.acronimo = acronimo;
		this.nombre = ValoresPorDefecto.getIntegridadDominio(acronimo)+"";
	}
	
	/***
	 * 
	 *  
	 */
	public Object clone(){
	        Object obj=null;
	        try{
	            obj=super.clone();
	        }catch(CloneNotSupportedException ex){
	        	Log4JManager.error(" no se puede duplicar");
	        }
	        return obj;
	    }
	
	/**
	 * @return the acronimo
	 */
	public String getAcronimo() {
		return acronimo;
	}

	/**
	 * @param acronimo the acronimo to set
	 */
	public void setAcronimo(String acronimo) {
		this.acronimo = acronimo;
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	
}