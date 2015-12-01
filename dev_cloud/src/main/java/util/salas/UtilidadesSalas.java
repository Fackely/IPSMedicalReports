/*
 * Febrero 11, 2008
 */
package util.salas;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.InfoDatosInt;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.salasCirugia.UtilidadesSalasDao;
import com.servinte.axioma.fwk.exception.IPSException;


/**
 * 
 * @author Sebastián Gómez R.
 * Clase que contiene las utilidades del módulo de SALAS
 */
public class UtilidadesSalas 
{
	private static Logger logger =Logger.getLogger(UtilidadesSalas.class);
	/**
	 * instancia del DAO
	 * @return
	 */
	public static UtilidadesSalasDao utilidadesDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesSalasDao();
	}
	
	/**
	 * Método implementado para consultar un tipo de cirugía parametrizada por institucion
	 * @param con
	 * @param acronimoTipoCirugia
	 * @param institucion
	 * @return
	 */
	public static int obtenerCodigoTipoCirugia(Connection con,String acronimoTipoCirugia,int institucion)
	{
		return utilidadesDao().obtenerCodigoTipoCirugia(con, acronimoTipoCirugia, institucion);
	}
	
	/**
	 * Método implementado para retornar el nombre del tipo de cirugia
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public static String obtenerDescripcionTipoCirugia(Connection con,int consecutivo)
	{
		return utilidadesDao().obtenerDescripcionTipoCirugia(con, consecutivo);
	}
	
	/**
	 * Método implementado para obtener las salas
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerSalas(Connection con,int codigoInstitucion,int codigoCentroAtencion,String activo,String codigoTipoSala)
	{
		HashMap campos = new HashMap();
		campos.put("codigoInstitucion", codigoInstitucion);
		campos.put("codigoCentroAtencion", codigoCentroAtencion);
		campos.put("activo", activo);
		campos.put("codigoTipoSala", codigoTipoSala);
		return utilidadesDao().obtenerSalas(con, campos);
	}
	
	/**
	 * Método implementado para obtener las especialidades que intervienen de una solicitud de cirugía
	 * @param con
	 * @param numeroSolicitud
	 * @param asignada
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerEspecialidadesIntervienen(Connection con,String numeroSolicitud,String asignada)
	{
		return utilidadesDao().obtenerEspecialidadesIntervienen(con, numeroSolicitud, asignada);
	}
	
	/**
	 * Método que verifica si un servicio es de via de acceso
	 * @param con
	 * @param campos
	 * @return
	 */
	public static boolean esServicioViaAcceso(Connection con,int codigoServicio,int codigoInstitucion)
	{
		HashMap campos = new HashMap();
		campos.put("codigoServicio",codigoServicio);
		campos.put("codigoInstitucion",codigoInstitucion);
		return utilidadesDao().esServicioViaAcceso(con, campos);
	}
	
	/**
	 * Método que verifica si una solicitud cirugia es por acto 
	 * @param con
	 * @param campos
	 * @return
	 */
	public static boolean esSolicitudCirugiaPorActo(Connection con,int numero_solicitud) throws IPSException
	{
		return utilidadesDao().esSolicitudCirugiaPorActo(con, numero_solicitud);
	}
	
	
	/**
	 * Método implementado para obtener el codigo del tipo de servicio de un asocio
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public static String obtenerCodigoTipoServicioAsocio(Connection con,int consecutivo) throws IPSException
	{
		return utilidadesDao().obtenerCodigoTipoServicioAsocio(con, consecutivo);
	}
	
	/**
	 * Método implementado para obtener la descripción de la sala
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public static String obtenerDescripcionSala(Connection con,int consecutivo)
	{
		return utilidadesDao().obtenerDescripcionSala(con, consecutivo);
	}
	
	/**
	 * Método implementado para obtener el listado de materiales especiales
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerListadoMaterialesEspeciales(Connection con,String numeroSolicitud,String idSubCuenta,boolean desdeConsumo,boolean mostrarPaquetizado)
	{
		return utilidadesDao().obtenerListadoMaterialesEspeciales(con, numeroSolicitud,idSubCuenta,desdeConsumo, mostrarPaquetizado);
	}
	/**
	 * Método que verifica si una solicitud tiene consumo de materiales pendiente
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean existeConsumoMaterialesPendiente(Connection con,String numeroSolicitud)
	{
		return utilidadesDao().existeConsumoMaterialesPendiente(con, numeroSolicitud);
	}
	
	/**
	 * Método para obtener los tipos de anestesia por institucion y centro de costo
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerTiposAnestesiaInstitucionCentroCosto(Connection con,int codigoInstitucion,int codigoCentroCosto)
	{
		HashMap campos = new HashMap();
		campos.put("codigoInstitucion", codigoInstitucion);
		campos.put("codigoCentroCosto", codigoCentroCosto);
		return utilidadesDao().obtenerTiposAnestesiaInstitucionCentroCosto(con, campos);
	}
	
	public static ArrayList<HashMap<String, Object>> obtenerTiposAnestesiaInstitucionCentroCosto(Connection con,int codigoInstitucion,int codigoCentroCosto,String  mostrarHqx, String mostrarHanes)
	{
		HashMap campos = new HashMap();
		campos.put("codigoInstitucion", codigoInstitucion);
		campos.put("codigoCentroCosto", codigoCentroCosto);
		campos.put("mostrarHqx", mostrarHqx);
		campos.put("mostrarHanes", mostrarHanes);
		return utilidadesDao().obtenerTiposAnestesiaInstitucionCentroCosto(con, campos);
	}
	
	public static ArrayList<HashMap<String, Object>> obtenerTiposAnestesiaInstitucionCentroCosto(Connection con,HashMap campos)
	{
		return utilidadesDao().obtenerTiposAnestesiaInstitucionCentroCosto(con, campos);
	}
	
	
	/**
	 * Método que verifica si un tipo de anestesia está definido como mostrar Hoja Qx
	 * @param con
	 * @param codigoTipoAnestesia
	 * @return
	 */
	public static boolean estaTipoAnestesiaEnMostrarHojaQx(Connection con,int codigoTipoAnestesia)
	{
		return utilidadesDao().estaTipoAnestesiaEnMostrarHojaQx(con, codigoTipoAnestesia);
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
	public static  boolean mostrarCampoTextoHqx(Connection con,  String TipoCampo,String CentroCosto,String institucion)
	{
		return utilidadesDao().mostrarCampoTextoHqx(con, TipoCampo, CentroCosto, institucion);
	}
	
	/**
	 * Método para obtener el nombre de la farmacia del consumo de materiales Qx
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static String obtenerNombreFarmaciaConsumoMaterialesQx(Connection con,int numeroSolicitud)
	{
		HashMap campos = new HashMap();
		campos.put("numeroSolicitud",numeroSolicitud);
		return utilidadesDao().obtenerNombreFarmaciaConsumoMaterialesQx(con, campos);
	}
	
	/**
	 * Metodo encargado de consultar el nombre del tipo de participante en la cirugia
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static String obtenerNombreTipoParticipanteQx(Connection con,int codigo)
	{
		return utilidadesDao().obtenerNombreTipoParticipanteQx(con, codigo);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static InfoDatosInt obtenerMedicoSalaMaterialesCx(int numeroSolicitud)
	{
		return utilidadesDao().obtenerMedicoSalaMaterialesCx(numeroSolicitud);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static InfoDatosInt obtenerMedicoSalaMaterialesCx(String sala)
	{
		return utilidadesDao().obtenerMedicoSalaMaterialesCx(sala);
	}
	
	
	/**
	 * 
	 * @param tipoAsocio
	 * @return
	 */
	public static InfoDatosInt obtenerCentroCostoEjecutaHonorarios(int tipoAsocio)
	{
		return utilidadesDao().obtenerCentroCostoEjecutaHonorarios(tipoAsocio);
	}
	
	/**
	 * 
	 * @param codigoDetalleCargo
	 * @return
	 */
	public static InfoDatosInt obtenerCentroCostoEjecutaHonorarios(double codigoDetalleCargo)
	{
		return utilidadesDao().obtenerCentroCostoEjecutaHonorarios(codigoDetalleCargo);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoDetalleCargo
	 * @return
	 */
	public static String obtenerNumIdMedicoSalaMaterialesCx(double codigoDetalleCargo)
	{
		return utilidadesDao().obtenerNumIdMedicoSalaMaterialesCx(codigoDetalleCargo);
	}
	
	/**
	 * 
	 * @param codigoDetalleCargo
	 * @return
	 */
	public static String obtenerNumIdMedicoHonorariosCx(double codigoDetalleCargo)
	{
		return utilidadesDao().obtenerNumIdMedicoHonorariosCx(codigoDetalleCargo);
	}
	
	/**
	 * 
	 * @param numeroSolicitud
	 * @param servicioOPCIONAL
	 * @return
	 */
	public static HashMap<Object, Object> obtenerArticulosConsumoCx(Connection con,String numeroSolicitud, int servicioOPCIONAL) throws IPSException
	{
		return utilidadesDao().obtenerArticulosConsumoCx(con,numeroSolicitud, servicioOPCIONAL);
	}

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean esCxPorConsumoMateriales(Connection con,int numeroSolicitud) throws IPSException
	{
		return utilidadesDao().esCxPorConsumoMateriales(con,numeroSolicitud);
	}
	
	/**
	 * Metodo permite validar si el consumo de materiales esta finalizado
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean consultarConsumoMaterialesFinalizado(Connection con, int numeroSolicitud){
		return utilidadesDao().consultarConsumoMaterialesFinalizado(con, numeroSolicitud);
	}
}
