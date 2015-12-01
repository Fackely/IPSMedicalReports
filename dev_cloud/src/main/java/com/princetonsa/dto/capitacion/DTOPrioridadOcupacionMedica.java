/**
 * 
 */
package com.princetonsa.dto.capitacion;

import java.io.Serializable;

/**
 * Esta clase se encarga de 
 * @author Angela Aguirre
 *
 */
public class DTOPrioridadOcupacionMedica implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int codigoPk;
	//private long idPrioridad;
	private int nivelAutorOcupMedicaID;
	private int numeroPrioridad; 
	private boolean activo;
	
	/**
	 * Este m�todo se encarga de obtener el 
	 * valor del atributo codigoPk
	 *
	 * @author Angela Aguirre 
	 */
	public int getCodigoPk() {
		return codigoPk;
	}
	/**
	 * Este m�todo se encarga de asignar el 
	 * valor del atributo codigoPk
	 *
	 * @author Angela Aguirre 
	 */
	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}
	/**
	 * Este m�todo se encarga de obtener el 
	 * valor del atributo idPrioridad
	 *
	 * @author Angela Aguirre 
	 */
	/*public long getIdPrioridad() {
		return idPrioridad;
	}*/
	/**
	 * Este m�todo se encarga de asignar el 
	 * valor del atributo idPrioridad
	 *
	 * @author Angela Aguirre 
	 */
	/*public void setIdPrioridad(long idPrioridad) {
		this.idPrioridad = idPrioridad;
	}*/
	/**
	 * Este m�todo se encarga de obtener el 
	 * valor del atributo nivelAutorOcupMedicaID
	 *
	 * @author Angela Aguirre 
	 */
	public int getNivelAutorOcupMedicaID() {
		return nivelAutorOcupMedicaID;
	}
	/**
	 * Este m�todo se encarga de asignar el 
	 * valor del atributo nivelAutorOcupMedicaID
	 *
	 * @author Angela Aguirre 
	 */
	public void setNivelAutorOcupMedicaID(int nivelAutorOcupMedicaID) {
		this.nivelAutorOcupMedicaID = nivelAutorOcupMedicaID;
	}
	/**
	 * Este m�todo se encarga de obtener el 
	 * valor del atributo activo
	 *
	 * @author Angela Aguirre 
	 */
	public boolean isActivo() {
		return activo;
	}
	/**
	 * Este m�todo se encarga de asignar el 
	 * valor del atributo activo
	 *
	 * @author Angela Aguirre 
	 */
	public void setActivo(boolean activo) {
		this.activo = activo;
	}
	/**
	 * Este m�todo se encarga de obtener el 
	 * valor del atributo numeroPrioridad
	 *
	 * @author Angela Aguirre 
	 */
	public int getNumeroPrioridad() {
		return numeroPrioridad;
	}
	/**
	 * Este m�todo se encarga de asignar el 
	 * valor del atributo numeroPrioridad
	 *
	 * @author Angela Aguirre 
	 */
	public void setNumeroPrioridad(int numeroPrioridad) {
		this.numeroPrioridad = numeroPrioridad;
	}

}
