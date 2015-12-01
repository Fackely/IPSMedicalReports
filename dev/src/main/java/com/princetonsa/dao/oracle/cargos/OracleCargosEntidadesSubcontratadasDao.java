/*
 * Jun 24, 2009
 */
package com.princetonsa.dao.oracle.cargos;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import util.ElementoApResource;
import util.ResultadoBoolean;

import com.princetonsa.dao.cargos.CargosEntidadesSubcontratadasDao;
import com.princetonsa.dao.sqlbase.cargos.SqlBaseCargosEntidadesSubcontratadasDao;
import com.princetonsa.dto.cargos.DtoCargoEntidadSub;
import com.princetonsa.dto.facturacion.DtoContratoEntidadSub;
import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;

/**
 * Clase que maneja las implementaciones de Oracle para el DAo de Cargos por entidades subcontratyadas
 * @author axioma
 *
 */
public class OracleCargosEntidadesSubcontratadasDao implements
		CargosEntidadesSubcontratadasDao 
{
	/**
	 * Método para obtener las entidades subcontratadas asociadas al centro de costo
	 * @param con
	 * @param codigoCentroCosto
	 * @param codigoInstitucion
	 * @return
	 */
	public ArrayList<DtoEntidadSubcontratada> obtenerEntidadesSubcontratadasCentroCosto(Connection con,int codigoCentroCosto,int codigoInstitucion)
	{
		return SqlBaseCargosEntidadesSubcontratadasDao.obtenerEntidadesSubcontratadasCentroCosto(con, codigoCentroCosto, codigoInstitucion);
	}
	
	/**
	 * Método para obtener el contrato vigente 
	 * @param con
	 * @param codigoEntidadSubcontratada
	 * @param fechaReferencia
	 * @return
	 */
	public DtoContratoEntidadSub obtenerContratoVigenteEntidadSubcontratada(Connection con,String codigoEntidadSubcontratada,String fechaReferencia)
	{
		return SqlBaseCargosEntidadesSubcontratadasDao.obtenerContratoVigenteEntidadSubcontratada(con, codigoEntidadSubcontratada, fechaReferencia);
	}
	
	/**
	 * Método para insertar el cargo de la entidad subcontratada
	 * @param con
	 * @param cargo
	 * @return
	 */
	public ResultadoBoolean insertarCargoEntidadSubcontratada(Connection con, DtoCargoEntidadSub cargo)
	{
		return SqlBaseCargosEntidadesSubcontratadasDao.insertarCargoEntidadSubcontratada(con, cargo);
	}
	
	/**
	 * Método para obtener el codigo del cargo de la entidad subcontratada
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String obtenerCodigoCargoEntidadSubcontratada(Connection con, HashMap campos)
	{
		return SqlBaseCargosEntidadesSubcontratadasDao.obtenerCodigoCargoEntidadSubcontratada(con, campos);
	}
	
	/**
	 * Método implementado para modificar un cargo
	 * @param con
	 * @param cargo
	 * @return
	 */
	public ResultadoBoolean modificarCargoEntidadSubcontratada(Connection con,DtoCargoEntidadSub cargo)
	{
		return SqlBaseCargosEntidadesSubcontratadasDao.modificarCargoEntidadSubcontratada(con, cargo);
	}
	
	/**
	 * Método para registrar los errores del cargo de la entidad subcotnratada
	 * @param con
	 * @param codigoDetalleCargo
	 * @param erroresCargo
	 * @return
	 */
	public ResultadoBoolean registrarErroresCargoEntidadSub(Connection con,String codigoDetalleCargo,ArrayList<ElementoApResource> erroresCargo)
	{
		return SqlBaseCargosEntidadesSubcontratadasDao.registrarErroresCargoEntidadSub(con, codigoDetalleCargo, erroresCargo);
	}
	
	/**
	 * Método que verifica si el centro de costo solicitante esta relacionado con el centro de atencion cubierto de la unidad subcontratada.
	 * @param connection
	 * @param parametros
	 * @return
	 */
	public String consultaCentroCostoSolicitanteIgualACubierto(Connection connection, HashMap<String, Object> parametros){
		return SqlBaseCargosEntidadesSubcontratadasDao.consultaCentroCostoSolicitanteIgualACubierto(connection, parametros);
	}
}
