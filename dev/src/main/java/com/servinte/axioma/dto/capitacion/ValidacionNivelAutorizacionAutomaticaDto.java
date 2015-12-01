/**
 * 
 */
package com.servinte.axioma.dto.capitacion;

import java.io.Serializable;

import com.servinte.axioma.dto.facturacion.EntidadSubContratadaDto;

/**
 * Dto encargado de almacenar los datos correspondientes a la validación
 * de niveles de autorización para autorizaciones automáticas
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
	 * Información del nivel de autorización validado
	 */
	private NivelAutorizacionDto nivelAutorizacionDto;
	
	/**
	 * Información de la entidad Subcontratada
	 */
	private EntidadSubContratadaDto entidadSubContratadaDto;
	
	/**
	 * Determina si se la validación fue exitosa y puede generar autorización
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
