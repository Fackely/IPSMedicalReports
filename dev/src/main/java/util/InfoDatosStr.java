package util;

import java.io.Serializable;
import java.math.BigDecimal;

import org.axioma.util.log.Log4JManager;
/**
 * 
 * @author axioma
 *
 */
public class InfoDatosStr implements Serializable, Cloneable
{
	/**
	 * 
	 */
	private String codigo;
	
	/**
	 * 
	 */
	private String nombre;
	
	/**
	 * 
	 * @param codigo
	 * @param nombre
	 */
	
	private String descripcion;
	
	/**
	 * 
	 * */
	private boolean indicador;
	
	
	private BigDecimal valor;
	/**
	 * 
	 * 
	 */
	public InfoDatosStr() {
		super();
		this.codigo = "";
		this.nombre = "";
		this.descripcion="";
		this.indicador = false;
		this.valor = new BigDecimal(ConstantesBD.codigoNuncaValido);
	}
	
	/**
	 * 
	 * @param codigo
	 * @param nombre
	 */
	public InfoDatosStr(String codigo, String nombre) {
		super();
		this.codigo = codigo;
		this.nombre = nombre;
	}
	
	/**
	 * 
	 * @param codigo
	 * @param nombre
	 */
	public InfoDatosStr(String codigo, String nombre,String descripcion) {
		super();
		this.codigo = codigo;
		this.nombre = nombre;
		this.descripcion = descripcion;
	}
	
	/**
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}
	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(String codigo) {
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

	public boolean isIndicador() {
		return indicador;
	}

	public void setIndicador(boolean indicador) {
		this.indicador = indicador;
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

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	
}
