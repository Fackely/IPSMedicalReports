package util;

import java.io.Serializable;

import org.axioma.util.log.Log4JManager;

public class InfoDatosDouble implements Serializable, Cloneable 
{
	/**
	 * 
	 */
	private Double codigo;
	
	/**
	 * 
	 */
	private String nombre;
     /**
      * 
      *
      */
	private String descripcion;
	
	/**
	 * 
	 */
	private boolean activo;
	
	/**
	 * 
	 * @param codigo
	 * @param nombre
	 */
	public InfoDatosDouble(Double codigo, String nombre) 
	{
		super();
		this.codigo = codigo;
		this.nombre = nombre;
	}
	
	public InfoDatosDouble(Double codigo, String nombre , String descripcion) 
	{
		super();
		this.codigo = codigo;
		this.nombre = nombre;
		this.descripcion = descripcion;
	}

	/**
	 * 
	 * @param codigo
	 * @param nombre
	 * @param descripcion
	 * @param activo
	 */
	public InfoDatosDouble(Double codigo, String nombre, String descripcion,
			boolean activo) {
		super();
		this.codigo = codigo;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.activo = activo;
	}

	/**
	 * 
	 * @param codigo
	 * @param nombre
	 */
	public InfoDatosDouble() 
	{
		super();
		this.codigo = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.nombre = "";
		this.descripcion="";
		this.activo= false;
	}
	
	/**
	 * @return the codigo
	 */
	public Double getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(Double codigo) {
		this.codigo = codigo;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return (this.getCodigo()+" "+this.getNombre());
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
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * @return the activo
	 */
	public boolean isActivo() {
		return activo;
	}

	/**
	 * @param activo the activo to set
	 */
	public void setActivo(boolean activo) {
		this.activo = activo;
	}
	
	/**
	 * @return the activo
	 */
	public boolean getActivo() {
		return activo;
	}

	
}
