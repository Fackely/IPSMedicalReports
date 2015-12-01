/*
 * Febrero 11, 2008
 */
package com.princetonsa.dao.salasCirugia;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import util.InfoDatosInt;

import com.servinte.axioma.fwk.exception.BDException;

/**
 * 
 * @author sgomez
 *	Interface usada para gestionar los m�todos de acceso a la base
 * de datos en utilidades del modulo de SALAS
 */
public interface UtilidadesSalasDao 
{
	/**
	 * M�todo implementado para consultar un tipo de cirug�a parametrizada por institucion
	 * @param con
	 * @param acronimoTipoCirugia
	 * @param institucion
	 * @return
	 */
	public int obtenerCodigoTipoCirugia(Connection con,String acronimoTipoCirugia,int institucion);
	
	/**
	 * M�todo implementado para retornar el nombre del tipo de cirugia
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public String obtenerDescripcionTipoCirugia(Connection con,int consecutivo);
	
	/**
	 * M�todo implementado para obtener las salas
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerSalas(Connection con,HashMap campos);
	
	/**
	 * M�todo implementado para obtener las especialidades que intervienen de una solicitud de cirug�a
	 * @param con
	 * @param numeroSolicitud
	 * @param asignada
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerEspecialidadesIntervienen(Connection con,String numeroSolicitud,String asignada);
	
	/**
	 * M�todo que verifica si un servicio es de via de acceso
	 * @param con
	 * @param campos
	 * @return
	 */
	public boolean esServicioViaAcceso(Connection con,HashMap campos);
	
	/**
	 * M�todo que verifica si una solicitud cirugia es por acto 
	 * @param con
	 * @param numero_solicitud
	 * @return
	 */
	public boolean esSolicitudCirugiaPorActo(Connection con, int numero_solicitud) throws BDException;
	
	/**
	 * M�todo implementado para obtener el codigo del tipo de servicio de un asocio
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public String obtenerCodigoTipoServicioAsocio(Connection con,int consecutivo) throws BDException;
	
	/**
	 * M�todo implementado para obtener la descripci�n de la sala
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public String obtenerDescripcionSala(Connection con,int consecutivo);
	
	/**
	 * M�todo implementado para obtener el listado de materiales especiales
	 * @param con
	 * @param numeroSolicitud
	 * @param mostrarPaquetizado 
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerListadoMaterialesEspeciales(Connection con,String numeroSolicitud,String idSubCuenta,boolean desdeConsumo, boolean mostrarPaquetizado);
	
	/**
	 * M�todo que verifica si una solicitud tiene consumo de materiales pendiente
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean existeConsumoMaterialesPendiente(Connection con,String numeroSolicitud);
	
	/**
	 * M�todo para obtener los tipos de anestesia por institucion y centro de costo
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerTiposAnestesiaInstitucionCentroCosto(Connection con,HashMap campos);
	
	/**
	 * M�todo que verifica si un tipo de anestesia est� definido como mostrar Hoja Qx
	 * @param con
	 * @param codigoTipoAnestesia
	 * @return
	 */
	public boolean estaTipoAnestesiaEnMostrarHojaQx(Connection con,int codigoTipoAnestesia);
	
	/**
	 * Metodo encargado de identificar si se deben de mostrar los campos
	 * en la hoja quirurgica de perfusion y hallazgos
	 * @param con
	 * @param TipoCampo
	 * @param CentroCosto
	 * @param institucion
	 * @return
	 */
	public boolean mostrarCampoTextoHqx(Connection con,  String TipoCampo,String CentroCosto,String institucion);
	
	/**
	 * M�todo para obtener el nombre de la farmacia del consumo de materiales Qx
	 * @param con
	 * @param campos
	 * @return
	 */
	public String obtenerNombreFarmaciaConsumoMaterialesQx(Connection con,HashMap campos);
	
	/**
	 * Metodo encargado de consultar el nombre del tipo de participante en la cirugia
	 * @param con
	 * @param codigo
	 * @return
	 */
	public String obtenerNombreTipoParticipanteQx(Connection con,int codigo);
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public InfoDatosInt obtenerMedicoSalaMaterialesCx(int numeroSolicitud);
	
	/**
	 * 
	 * @param sala
	 * @return
	 */
	public InfoDatosInt obtenerMedicoSalaMaterialesCx(String sala);
	
	/**
	 * 
	 * @param tipoAsocio
	 * @return
	 */
	public InfoDatosInt obtenerCentroCostoEjecutaHonorarios(int tipoAsocio);

	/**
	 * 
	 * @param codigoDetalleCargo
	 * @return
	 */
	public InfoDatosInt obtenerCentroCostoEjecutaHonorarios(double codigoDetalleCargo);
	
	/**
	 * 
	 * @param con
	 * @param codigoDetalleCargo
	 * @return
	 */
	public String obtenerNumIdMedicoSalaMaterialesCx(double codigoDetalleCargo);
	
	/**
	 * 
	 * @param codigoDetalleCargo
	 * @return
	 */
	public String obtenerNumIdMedicoHonorariosCx(double codigoDetalleCargo);
	
	/**
	 * 
	 * @param numeroSolicitud
	 * @param servicioOPCIONAL
	 * @return
	 */
	public HashMap<Object, Object> obtenerArticulosConsumoCx(Connection con,String numeroSolicitud, int servicioOPCIONAL) throws BDException;

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean esCxPorConsumoMateriales(Connection con, int numeroSolicitud) throws BDException;
	
	/**
	 * Metodo permite validar si el consumo de materiales esta finalizado
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean consultarConsumoMaterialesFinalizado(Connection con, int numeroSolicitud);
}
