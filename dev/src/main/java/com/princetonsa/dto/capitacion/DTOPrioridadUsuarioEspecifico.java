/**
 * 
 */
package com.princetonsa.dto.capitacion;

import java.io.Serializable;

/**
 * Esta clase se encarga de contener los datos
 * de la entidad  PrioridadUsuarioEspecifico
 * @author Angela Aguirre
 *
 */
public class DTOPrioridadUsuarioEspecifico implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int codigoPk;
	private int nivelAutorUsuEspecID;
	//private long prioridadID;
	private int numeroPrioridad; 
	private boolean activo;
	
	/**
	 * Este método se encarga de obtener el 
	 * valor del atributo codigoPk
	 *
	 * @author Angela Aguirre 
	 */
	public int getCodigoPk() {
		return codigoPk;
	}
	/**
	 * Este método se encarga de asignar el 
	 * valor del atributo codigoPk
	 *
	 * @author Angela Aguirre 
	 */
	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}
	/**
	 * Este método se encarga de obtener el 
	 * valor del atributo nivelAutorUsuEspecID
	 *
	 * @author Angela Aguirre 
	 */
	public int getNivelAutorUsuEspecID() {
		return nivelAutorUsuEspecID;
	}
	/**
	 * Este método se encarga de asignar el 
	 * valor del atributo nivelAutorUsuEspecID
	 *
	 * @author Angela Aguirre 
	 */
	public void setNivelAutorUsuEspecID(int nivelAutorUsuEspecID) {
		this.nivelAutorUsuEspecID = nivelAutorUsuEspecID;
	}
	/**
	 * Este método se encarga de obtener el 
	 * valor del atributo prioridadID
	 *
	 * @author Angela Aguirre 
	 */
	/*public long getPrioridadID() {
		return prioridadID;
	}*/
	/**
	 * Este método se encarga de asignar el 
	 * valor del atributo prioridadID
	 *
	 * @author Angela Aguirre 
	 */
	/*public void setPrioridadID(long prioridadID) {
		this.prioridadID = prioridadID;
	}*/
	/**
	 * Este método se encarga de obtener el 
	 * valor del atributo activo
	 *
	 * @author Angela Aguirre 
	 */
	public boolean isActivo() {
		return activo;
	}
	/**
	 * Este método se encarga de asignar el 
	 * valor del atributo activo
	 *
	 * @author Angela Aguirre 
	 */
	public void setActivo(boolean activo) {
		this.activo = activo;
	}
	/**
	 * Este método se encarga de obtener el 
	 * valor del atributo numeroPrioridad
	 *
	 * @author Angela Aguirre 
	 */
	public int getNumeroPrioridad() {
		return numeroPrioridad;
	}
	/**
	 * Este método se encarga de asignar el 
	 * valor del atributo numeroPrioridad
	 *
	 * @author Angela Aguirre 
	 */
	public void setNumeroPrioridad(int numeroPrioridad) {
		this.numeroPrioridad = numeroPrioridad;
	}
	
	

}
