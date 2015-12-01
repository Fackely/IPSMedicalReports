/*
 * Febrero 11, 2008
 */
package com.princetonsa.dao.oracle.salasCirugia;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import util.InfoDatosInt;

import com.princetonsa.dao.salasCirugia.UtilidadesSalasDao;
import com.princetonsa.dao.sqlbase.salasCirugia.SqlBaseUtilidadesSalasDao;
import com.servinte.axioma.fwk.exception.BDException;

/**
 * 
 * @author sgomez
 * Métodos de oracle para el acceso a la fuente de datos para las utilidades
 * del módulo de SALAS
 */
public class OracleUtilidadesSalasDao implements UtilidadesSalasDao 
{
	/**
	 * Método implementado para consultar un tipo de cirugía parametrizada por institucion
	 * @param con
	 * @param acronimoTipoCirugia
	 * @param institucion
	 * @return
	 */
	public int obtenerCodigoTipoCirugia(Connection con,String acronimoTipoCirugia,int institucion)
	{
		return SqlBaseUtilidadesSalasDao.obtenerCodigoTipoCirugia(con, acronimoTipoCirugia, institucion);
	}
	
	/**
	 * Método implementado para retornar el nombre del tipo de cirugia
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public String obtenerDescripcionTipoCirugia(Connection con,int consecutivo)
	{
		return SqlBaseUtilidadesSalasDao.obtenerDescripcionTipoCirugia(con, consecutivo);
	}
	
	/**
	 * Método implementado para obtener las salas
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerSalas(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesSalasDao.obtenerSalas(con, campos);
	}
	
	/**
	 * Método implementado para obtener las especialidades que intervienen de una solicitud de cirugía
	 * @param con
	 * @param numeroSolicitud
	 * @param asignada
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerEspecialidadesIntervienen(Connection con,String numeroSolicitud,String asignada)
	{
		return SqlBaseUtilidadesSalasDao.obtenerEspecialidadesIntervienen(con, numeroSolicitud, asignada);
	}
	
	/**
	 * Método que verifica si un servicio es de via de acceso
	 * @param con
	 * @param campos
	 * @return
	 */
	public boolean esServicioViaAcceso(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesSalasDao.esServicioViaAcceso(con, campos);
	}
	
	/**
	 * Método implementado para obtener el codigo del tipo de servicio de un asocio
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public String obtenerCodigoTipoServicioAsocio(Connection con,int consecutivo) throws BDException
	{
		return SqlBaseUtilidadesSalasDao.obtenerCodigoTipoServicioAsocio(con, consecutivo);
	}
	
	/**
	 * Método implementado para obtener la descripción de la sala
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public String obtenerDescripcionSala(Connection con,int consecutivo)
	{
		return SqlBaseUtilidadesSalasDao.obtenerDescripcionSala(con, consecutivo);
	}
	
	/**
	 * Método implementado para obtener el listado de materiales especiales
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerListadoMaterialesEspeciales(Connection con,String numeroSolicitud,String idSubCuenta,boolean desdeConsumo,boolean mostrarPaquetizado)
	{
		return SqlBaseUtilidadesSalasDao.obtenerListadoMaterialesEspeciales(con, numeroSolicitud,idSubCuenta,desdeConsumo,mostrarPaquetizado);
	}
	
	/**
	 * Método que verifica si una solicitud tiene consumo de materiales pendiente
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean existeConsumoMaterialesPendiente(Connection con,String numeroSolicitud)
	{
		return SqlBaseUtilidadesSalasDao.existeConsumoMaterialesPendiente(con, numeroSolicitud);
	}
	
	/**
	 * Método para obtener los tipos de anestesia por institucion y centro de costo
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerTiposAnestesiaInstitucionCentroCosto(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesSalasDao.obtenerTiposAnestesiaInstitucionCentroCosto(con, campos);
	}
	
	/**
	 * Método que verifica si un tipo de anestesia está definido como mostrar Hoja Qx
	 * @param con
	 * @param codigoTipoAnestesia
	 * @return
	 */
	public boolean estaTipoAnestesiaEnMostrarHojaQx(Connection con,int codigoTipoAnestesia)
	{
		return SqlBaseUtilidadesSalasDao.estaTipoAnestesiaEnMostrarHojaQx(con, codigoTipoAnestesia);
	}
	/**
	 * Método que verifica si una solicitud de cirugia es por acto
	 * @param con
	 * @param numero_solicitud
	 * @return
	 */
	public boolean esSolicitudCirugiaPorActo(Connection con, int numero_solicitud) throws BDException{
		
		return SqlBaseUtilidadesSalasDao.esSolicitudCirugiaPorActo(con, numero_solicitud);
	}
	
	/**
	 * Metodo encargado de identificar si se deben de mostrar los campos
	 * en la hoja quirurgica de perfusion y hallazgos
	 * @param con
	 * @param TipoCampo
	 * @param CentroCosto
	 * @param institucion
	 * @return
	 */
	public boolean mostrarCampoTextoHqx(Connection con,  String TipoCampo,String CentroCosto,String institucion)
	{
		return SqlBaseUtilidadesSalasDao.mostrarCampoTextoHqx(con, TipoCampo, CentroCosto, institucion);
	}
	
	/**
	 * Método para obtener el nombre de la farmacia del consumo de materiales Qx
	 * @param con
	 * @param campos
	 * @return
	 */
	public String obtenerNombreFarmaciaConsumoMaterialesQx(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesSalasDao.obtenerNombreFarmaciaConsumoMaterialesQx(con, campos);
	}
	
	/**
	 * Metodo encargado de consultar el nombre del tipo de participante en la cirugia
	 * @param con
	 * @param codigo
	 * @return
	 */
	public String obtenerNombreTipoParticipanteQx(Connection con,int codigo)
	{
		return SqlBaseUtilidadesSalasDao.obtenerNombreTipoParticipanteQx(con, codigo);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public InfoDatosInt obtenerMedicoSalaMaterialesCx(int numeroSolicitud)
	{
		return SqlBaseUtilidadesSalasDao.obtenerMedicoSalaMaterialesCx(numeroSolicitud);
	}
	
	/**
	 * 
	 */
	public InfoDatosInt obtenerMedicoSalaMaterialesCx(String sala)
	{
		return SqlBaseUtilidadesSalasDao.obtenerMedicoSalaMaterialesCx(sala);
	}
	
	/**
	 * 
	 * @param tipoAsocio
	 * @return
	 */
	public InfoDatosInt obtenerCentroCostoEjecutaHonorarios(int tipoAsocio)
	{
		return SqlBaseUtilidadesSalasDao.obtenerCentroCostoEjecutaHonorarios(tipoAsocio);
	}
	
	/**
	 * 
	 * @param codigoDetalleCargo
	 * @return
	 */
	public InfoDatosInt obtenerCentroCostoEjecutaHonorarios(double codigoDetalleCargo)
	{
		return SqlBaseUtilidadesSalasDao.obtenerCentroCostoEjecutaHonorarios(codigoDetalleCargo);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoDetalleCargo
	 * @return
	 */
	public String obtenerNumIdMedicoSalaMaterialesCx(double codigoDetalleCargo)
	{
		return SqlBaseUtilidadesSalasDao.obtenerNumIdMedicoSalaMaterialesCx(codigoDetalleCargo);
	}
	
	/**
	 * 
	 * @param codigoDetalleCargo
	 * @return
	 */
	public String obtenerNumIdMedicoHonorariosCx(double codigoDetalleCargo)
	{
		return SqlBaseUtilidadesSalasDao.obtenerNumIdMedicoHonorariosCx(codigoDetalleCargo);
	}
	
	/**
	 * 
	 * @param numeroSolicitud
	 * @param servicioOPCIONAL
	 * @return
	 */
	public HashMap<Object, Object> obtenerArticulosConsumoCx(Connection con,String numeroSolicitud, int servicioOPCIONAL) throws BDException
	{
		return SqlBaseUtilidadesSalasDao.obtenerArticulosConsumoCx(con,numeroSolicitud, servicioOPCIONAL);
	}
	
	@Override
	public boolean esCxPorConsumoMateriales(Connection con, int numeroSolicitud) throws BDException
	{
		return SqlBaseUtilidadesSalasDao.esCxPorConsumoMateriales(con,numeroSolicitud);
	}

	/**
	 * Metodo permite validar si el consumo de materiales esta finalizado
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	@Override
	public boolean consultarConsumoMaterialesFinalizado(Connection con,
			int numeroSolicitud) {
		return SqlBaseUtilidadesSalasDao.consultarConsumoMaterialesFinalizado(con, numeroSolicitud);
	}
}
