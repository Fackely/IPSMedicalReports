package com.princetonsa.dto.manejoPaciente;

import com.servinte.axioma.orm.ExcepcionesNaturaleza;


/**
 * Esta clase se encarga de contener los datos
 * de una Excepci&oacute;n para una Naturaleza Paciente
 * 
 * @author Angela Maria Aguirre
 * @since 18/08/2010
 */
public class DTOExcepcionNaturalezaPaciente extends ExcepcionesNaturaleza{
	
	private static final long serialVersionUID = 1L;
	
	private String acronimoTipoRegimen;
	private String nombreTipoRegimen;
	private int codigoNaturalezaPaciente;
	private String nombreNaturalezaPaciente;
	
	/**
	 * 
	 * M&eacute;todo constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public DTOExcepcionNaturalezaPaciente(){
		
	}
	
	/**
	 * 
	 * M&eacute;todo constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public DTOExcepcionNaturalezaPaciente(int codigoNaturalezaPaciente){
		this.codigoNaturalezaPaciente = codigoNaturalezaPaciente;
	}
	
	/**
	 * Este M&eacute;todo se encarga de obtener el valor 
	 * del atributo acronimoTipoRegimen
	
	 * @return retorna la variable acronimoTipoRegimen 
	 * @author Angela Maria Aguirre 
	 */
	public String getAcronimoTipoRegimen() {
		return acronimoTipoRegimen;
	}
	/**
	 * Este M&eacute;todo se encarga de establecer el valor 
	 * del atributo acronimoTipoRegimen
	
	 * @param valor para el atributo acronimoTipoRegimen 
	 * @author Angela Maria Aguirre 
	 */
	public void setAcronimoTipoRegimen(String acronimoTipoRegimen) {
		this.acronimoTipoRegimen = acronimoTipoRegimen;
	}
	/**
	 * Este M&eacute;todo se encarga de obtener el valor 
	 * del atributo nombreTipoRegimen
	
	 * @return retorna la variable nombreTipoRegimen 
	 * @author Angela Maria Aguirre 
	 */
	public String getNombreTipoRegimen() {
		return nombreTipoRegimen;
	}
	/**
	 * Este M&eacute;todo se encarga de establecer el valor 
	 * del atributo nombreTipoRegimen
	
	 * @param valor para el atributo nombreTipoRegimen 
	 * @author Angela Maria Aguirre 
	 */
	public void setNombreTipoRegimen(String nombreTipoRegimen) {
		this.nombreTipoRegimen = nombreTipoRegimen;
	}
	/**
	 * Este M&eacute;todo se encarga de obtener el valor 
	 * del atributo codigoNaturalezaPaciente
	
	 * @return retorna la variable codigoNaturalezaPaciente 
	 * @author Angela Maria Aguirre 
	 */
	public int getCodigoNaturalezaPaciente() {
		return codigoNaturalezaPaciente;
	}
	/**
	 * Este M&eacute;todo se encarga de establecer el valor 
	 * del atributo codigoNaturalezaPaciente
	
	 * @param valor para el atributo codigoNaturalezaPaciente 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigoNaturalezaPaciente(int codigoNaturalezaPaciente) {
		this.codigoNaturalezaPaciente = codigoNaturalezaPaciente;
	}
	/**
	 * Este M&eacute;todo se encarga de obtener el valor 
	 * del atributo nombreNaturalezaPaciente
	
	 * @return retorna la variable nombreNaturalezaPaciente 
	 * @author Angela Maria Aguirre 
	 */
	public String getNombreNaturalezaPaciente() {
		return nombreNaturalezaPaciente;
	}
	/**
	 * Este M&eacute;todo se encarga de establecer el valor 
	 * del atributo nombreNaturalezaPaciente
	
	 * @param valor para el atributo nombreNaturalezaPaciente 
	 * @author Angela Maria Aguirre 
	 */
	public void setNombreNaturalezaPaciente(String nombreNaturalezaPaciente) {
		this.nombreNaturalezaPaciente = nombreNaturalezaPaciente;
	}
	
	
}
