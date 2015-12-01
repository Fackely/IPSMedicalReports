/*
 * Jun24/09
 */
package com.princetonsa.dao.cargos;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import util.ElementoApResource;
import util.ResultadoBoolean;

import com.princetonsa.dto.cargos.DtoCargoEntidadSub;
import com.princetonsa.dto.facturacion.DtoContratoEntidadSub;
import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;

/**
 * 
 * Interfaz para el manejo del DAO del calculo de tarifa de entidades subcontratads
 * @author Sebasti�n G�mez R
 *
 */
public interface CargosEntidadesSubcontratadasDao 
{
	
	/**
	 * M�todo para obtener las entidades subcontratadas asociadas al centro de costo
	 * @param con
	 * @param codigoCentroCosto
	 * @param codigoInstitucion
	 * @return
	 */
	public ArrayList<DtoEntidadSubcontratada> obtenerEntidadesSubcontratadasCentroCosto(Connection con,int codigoCentroCosto,int codigoInstitucion);
	
	/**
	 * M�todo para obtener el contrato vigente 
	 * @param con
	 * @param codigoEntidadSubcontratada
	 * @param fechaReferencia
	 * @return
	 */
	public DtoContratoEntidadSub obtenerContratoVigenteEntidadSubcontratada(Connection con,String codigoEntidadSubcontratada,String fechaReferencia);
	
	/**
	 * M�todo para insertar el cargo de la entidad subcontratada
	 * @param con
	 * @param cargo
	 * @return
	 */
	public ResultadoBoolean insertarCargoEntidadSubcontratada(Connection con, DtoCargoEntidadSub cargo);
	
	/**
	 * M�todo para obtener el codigo del cargo de la entidad subcontratada
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String obtenerCodigoCargoEntidadSubcontratada(Connection con,HashMap campos);
	
	/**
	 * M�todo implementado para modificar un cargo
	 * @param con
	 * @param cargo
	 * @return
	 */
	public ResultadoBoolean modificarCargoEntidadSubcontratada(Connection con,DtoCargoEntidadSub cargo);
	
	/**
	 * M�todo para registrar los errores del cargo de la entidad subcotnratada
	 * @param con
	 * @param codigoDetalleCargo
	 * @param erroresCargo
	 * @return
	 */
	public ResultadoBoolean registrarErroresCargoEntidadSub(Connection con,String codigoDetalleCargo,ArrayList<ElementoApResource> erroresCargo);
	
	/**
	 * M�todo que verifica si el centro de costo solicitante esta relacionado con el centro de atencion cubierto de la unidad subcontratada.
	 * @param connection
	 * @param parametros
	 * @return
	 */
	public String consultaCentroCostoSolicitanteIgualACubierto(Connection connection, HashMap<String, Object> parametros);
}
