package com.princetonsa.dto.capitacion;

import util.ConstantesBD;


/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 7/02/2011
 */
public class DTOBusquedaCierreTemporalServicio {
	
	private int codigoContrato ;
	private Character cierreServicio;
	private long codigoNivelAtencion;
	private int codigoGrupoServicio;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public DTOBusquedaCierreTemporalServicio(){
		this.codigoContrato = ConstantesBD.codigoNuncaValido;
		this.codigoNivelAtencion = ConstantesBD.codigoNuncaValidoLong;
		this.codigoGrupoServicio = ConstantesBD.codigoNuncaValido;
	}
	

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo cierreServicio
	
	 * @return retorna la variable cierreServicio 
	 * @author Angela Maria Aguirre 
	 */
	public Character getCierreServicio() {
		return cierreServicio;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo cierreServicio
	
	 * @param valor para el atributo cierreServicio 
	 * @author Angela Maria Aguirre 
	 */
	public void setCierreServicio(Character cierreServicio) {
		this.cierreServicio = cierreServicio;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigoContrato
	
	 * @return retorna la variable codigoContrato 
	 * @author Angela Maria Aguirre 
	 */
	public int getCodigoContrato() {
		return codigoContrato;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigoContrato
	
	 * @param valor para el atributo codigoContrato 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigoContrato(int codigoContrato) {
		this.codigoContrato = codigoContrato;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigoNivelAtencion
	
	 * @return retorna la variable codigoNivelAtencion 
	 * @author Angela Maria Aguirre 
	 */
	public long getCodigoNivelAtencion() {
		return codigoNivelAtencion;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigoNivelAtencion
	
	 * @param valor para el atributo codigoNivelAtencion 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigoNivelAtencion(long codigoNivelAtencion) {
		this.codigoNivelAtencion = codigoNivelAtencion;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigoGrupoServicio
	
	 * @return retorna la variable codigoGrupoServicio 
	 * @author Angela Maria Aguirre 
	 */
	public int getCodigoGrupoServicio() {
		return codigoGrupoServicio;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigoGrupoServicio
	
	 * @param valor para el atributo codigoGrupoServicio 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigoGrupoServicio(int codigoGrupoServicio) {
		this.codigoGrupoServicio = codigoGrupoServicio;
	}
		
	

}
