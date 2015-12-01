package com.servinte.axioma.mundo.interfaz.facturacion;

import java.util.ArrayList;

import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.servinte.axioma.orm.EntidadesSubcontratadas;


/**
 * Define la l&oacute;gica de negocio relacionada con las EntidadesSubcontratadasMundo
 * @author Cristhian Murillo
 */
public interface IEntidadesSubcontratadasMundo 
{
	
	/**
	 * Retorna las EntidadesSubcontratadas activas para el centro de costo que tengan contrato vigente
	 * de la funcionalidad CentroCosto por Unidades Subcontratadas.
	 * Si se le envia como centro de costo un codigo invalido se listaran todas las entidades subcontratadas activas con contrato vigente.
	 * 
	 * @param codCentroCosto
	 * @return ArrayList<DtoEntidadSubcontratada>
	 * 
	 * @author Cristhian Murillo
	 */
	public ArrayList<DtoEntidadSubcontratada> listarEntidadesSubXCentroCostoActivoContratoVigente (int codCentroCosto);
	
	
	/**
	 * Retorna las EntidadesSubcontratadas activas para el centro de costo
	 * de la funcionalidad CentroCosto por Unidades Subcontratadas.
	 * 
	 * @param codCentroCosto
	 * @return ArrayList<DtoEntidadSubcontratada>
	 * 
	 * 
	 */
	public ArrayList<DtoEntidadSubcontratada> listarEntidadesSubXCentroCostoActivo (int codCentroCosto);
	
	
	/**
	 * Retorna las EntidadesSubcontratadas activas para la vía de ingreso dada (Según anexo 538)
	 * 
	 * @param parametros
	 * @return ArrayList<DtoEntidadSubcontratada>
	 * 
	 * @author Cristhian Murillo
	 */
	public ArrayList<DtoEntidadSubcontratada> listarEntidadesSubXViaIngreso (DtoEntidadSubcontratada parametros);
	

	/**
	 * Implementación del método findById
	 * @param id
	 * @return EntidadesSubcontratadas
	 */
	public EntidadesSubcontratadas obtenerEntidadesSubcontratadasporId(long id);
	
	
	/**
	 * Retorna las EntidadesSubcontratadas activas en el sistema
	 * @return ArrayList<DtoEntidadSubcontratada>
	 * 
	 * @author Fabián Becerra
	 */
	public ArrayList<DtoEntidadSubcontratada> listarEntidadesSubActivasEnSistema();
	
	
	/**
	 * Retorna la EntidadesSubcontratadas por si llave primaria
	 * 
	 * @param codigoPk
	 * @return DtoEntidadSubcontratada
	 * 
	 * @author Cristhian Murillo
	 */
	public DtoEntidadSubcontratada listarEntidadesSubXId (Long codigoPk);
}

