/*
 * @(#)InfoDatosInt.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package util;

import java.io.Serializable;

import org.axioma.util.log.Log4JManager;


/**
 * Nueva clase, con la misma funcionalidad de InfoDatos, pero manejando
 * nativamente el codigo como entero, para evitar conversiones innecesarias
 * (Mejorando la eficiencia)
 *
 * @version 1.0, Jun 25, 2003
 */

public class InfoDatosInt implements Serializable, Cloneable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Código de este valor de este objeto.
	 */
	private int codigo=0;
	
	/**
	 * 
	 * */
	private int codigo2 = 0;
	
	/**
	 * Nombre de este valor de este objeto.
	 */
	private String nombre="";

	/**
	 * Descripción de este valor de este objeto.
	 */
	private String descripcion="";
	
	/**
	 * Indica si este InfoDatosInt está activo o no
	 */
	private boolean activo=false;
	
	/**
	 * Indica si el InfoDatosINt está activo 'S' o 'N'
	 */
	private String activoStr = "";

	

	/**
	 * Constructor vacio del objeto InfoDatosInt 
	 */
	public InfoDatosInt ()
	{
		descripcion="";
		nombre="";
		codigo=0;
		codigo2 = 0;
		activo=false;
		this.activoStr = "";
	}

	/**
	 * Constructor que llena todos los datos en el objeto 
	 * InfoDatosInt
	 */
	public InfoDatosInt (int codigo)
	{
		this.codigo=codigo;
	}
	
	/**
	 * Constructor que llena todos los datos en el objeto 
	 * InfoDatosInt
	 */
	public InfoDatosInt (int codigo, String nombre)
	{
		this.codigo=codigo;
		this.nombre=nombre;
	}
	
	/**
	 * Constructor que llena codigo-descripcion-activo en el objeto 
	 * InfoDatosInt
	 */
	public InfoDatosInt (int codigo, String descripcion, boolean activo)
	{
		this.codigo=codigo;
		this.descripcion=descripcion;
		this.activo= activo;
	}
	

	/**
	 * Constructor que llena todos los datos en el objeto 
	 * InfoDatosInt
	 */
	public InfoDatosInt (int codigo, String nombre, String descripcion)
	{
		this.codigo=codigo;
		this.nombre=nombre;
		this.descripcion=descripcion;
	}

	/**
	 * Método para limpiar el objeto
	 */

	public void clean ()
	{
		descripcion="";
		nombre="";
		codigo=0;
	}

	/**
	 * @return
	 */
	public int getCodigo() {
		return codigo;
	}

	/**
	 * @return
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @return
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param i
	 */
	public void setCodigo(int i) {
		codigo = i;
	}

	/**
	 * @param string
	 */
	public void setDescripcion(String string) {
		descripcion = string;
	}

	/**
	 * @param string
	 */
	public void setNombre(String string) {
		nombre = string;
	}

	/**
	 * @return
	 */
	public boolean isActivo() {
		return activo;
	}

	/**
	 * @return
	 */
	public boolean getActivo() {
		return activo;
	}

	/**
	 * @param b
	 */
	public void setActivo(boolean b) {
		activo = b;
	}

	/**
	 * @return the activoStr
	 */
	public String getActivoStr() {
		return activoStr;
	}

	/**
	 * @param activoStr the activoStr to set
	 */
	public void setActivoStr(String activoStr) {
		this.activoStr = activoStr;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return (this.getCodigo()+" "+this.getNombre());
	}

	public int getCodigo2() {
		return codigo2;
	}

	public void setCodigo2(int codigo2) {
		this.codigo2 = codigo2;
	}

	

	public void setCodigo(String string) {
		
		
	}

	public void setInstitucion(int codigoInstitucionInt) {
		
		
	}

	public void setFechaModifica(String fechaActual) {
		
		
	}

	public void setHoraModifica(String horaActual) {
		
		
	}

	public void setUsuarioModifica(String loginUsuario) {
		
		
	}

	public Object clone(){
        Object obj=null;
        try{
            obj=super.clone();
        }catch(CloneNotSupportedException ex){
        	Log4JManager.error(" no se puede duplicar");
        }
        return obj;
    }

}
