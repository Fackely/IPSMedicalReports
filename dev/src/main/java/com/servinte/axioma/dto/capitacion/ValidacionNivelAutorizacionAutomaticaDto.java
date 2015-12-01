/**
 * 
 */
package com.servinte.axioma.dto.capitacion;

import java.io.Serializable;

import com.servinte.axioma.dto.facturacion.EntidadSubContratadaDto;

/**
 * Dto encargado de almacenar los datos correspondientes a la validaci�n
 * de niveles de autorizaci�n para autorizaciones autom�ticas
 * 
 * @author diego
 *
 */
public class ValidacionNivelAutorizacionAutomaticaDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4220753794056500090L;
	
	/**
	 * Informaci�n del nivel de autorizaci�n validado
	 */
	private NivelAutorizacionDto nivelAutorizacionDto;
	
	/**
	 * Informaci�n de la entidad Subcontratada
	 */
	private EntidadSubContratadaDto entidadSubContratadaDto;
	
	/**
	 * Determina si se la validaci�n fue exitosa y puede generar autorizaci�n
	 */
	private boolean procesoCancelado;

	/**
	 * 
	 */
	public ValidacionNivelAutorizacionAutomaticaDto() {
		
	}

	/**
	 * @return the nivelAutorizacionDto
	 */
	public NivelAutorizacionDto getNivelAutorizacionDto() {
		return nivelAutorizacionDto;
	}

	/**
	 * @param nivelAutorizacionDto the nivelAutorizacionDto to set
	 */
	public void setNivelAutorizacionDto(NivelAutorizacionDto nivelAutorizacionDto) {
		this.nivelAutorizacionDto = nivelAutorizacionDto;
	}

	/**
	 * @return the entidadSubContratadaDto
	 */
	public EntidadSubContratadaDto getEntidadSubContratadaDto() {
		return entidadSubContratadaDto;
	}

	/**
	 * @param entidadSubContratadaDto the entidadSubContratadaDto to set
	 */
	public void setEntidadSubContratadaDto(
			EntidadSubContratadaDto entidadSubContratadaDto) {
		this.entidadSubContratadaDto = entidadSubContratadaDto;
	}

	/**
	 * @return the procesoCancelado
	 */
	public boolean isProcesoCancelado() {
		return procesoCancelado;
	}

	/**
	 * @param procesoCancelado the procesoCancelado to set
	 */
	public void setProcesoCancelado(boolean procesoCancelado) {
		this.procesoCancelado = procesoCancelado;
	}
	
}
