/*
 * Junio 06, 2006
 */
package com.princetonsa.dao.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author Sebastián Gómez 
 *
 * Interface utilizada para gestionar los métodos DAO de la funcionalidad
 * informacion de parto
 */
public interface InformacionPartoDao 
{
	/**
	 * Método implementado para insertar la informacion del parto
	 * @param con
	 * @param infoParto
	 * @param estado
	 * @return
	 */
	public String insertarInformacionParto(Connection con,HashMap infoParto,String estado);
	
	/**
	 * Método que carga la información de un parto
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap cargarInformacionParto(Connection con,HashMap campos);
	
	/**
	 * Método que consulta las solicitudes del paciente al cual se la va a ingresar
	 * la informacion del parto
	 * @param con
	 * @param codigoPaciente
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param centroAtencion
	 * @return
	 */
	public HashMap cargarSolicitudesInformacionParto(Connection con,String codigoPaciente,String fechaInicial,String fechaFinal,int centroAtencion);
	
	/**
	 * Método para obtener el consecutivo de la cirugia
	 * de parto que está dentro de la solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public String obtenerCodigoCirugiaParto(Connection con,String numeroSolicitud);
	
	/**
	 * Método que consulta la fecha/hora del egreso de una admision de un parto
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public String obtenerFechaHoraEgresoAdmisionParto(Connection con,String codigoCuenta);
	
	/**
	 * Método que consulta la fecha y hora egreso de la admision del parto
	 * partiendo desde el codigo de la cirugia
	 * @param con
	 * @param codigoCirugia
	 * @return
	 */
	public String obtenerFechaHoraEgresoAdmisionPartoCirugia(Connection con,String codigoCirugia);
	
	/**
	 * Método implementado para verificar si el paciente tiene informacion de partos
	 * pendiente de ingresar
	 * @param con
	 * @param codigoPaciente
	 * @param centroAtencion
	 * @return
	 */
	public String validacionInformacionPartoPaciente(Connection con,String codigoPaciente,int centroAtencion);
	
	/**
	 * Método que consulta el numero de embarazo de un embarazo no finalizado del paciente
	 * desde la hoja obstétrica
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public int consultarNumeroEmbarazoHojaObstetrica(Connection con,String codigoPaciente);
	
	/**
	 * Método que verifica si ya existe un numero de embarazo en los registros de hoja obstétrica del paciente
	 * @param con
	 * @param codigoPaciente
	 * @param numeroEmbarazo
	 * @return
	 */
	public boolean existeNumeroEmbarazoHojaObstetrica(Connection con,String codigoPaciente,int numeroEmbarazo);
	
	/**
	 * Método implementado para obtener las consultas prenatales de la hoja obstetrica
	 * @param con
	 * @param campos
	 * @return
	 */
	public int obtenerConsultasPrenatalesHojaObstetrica(Connection con,HashMap campos);
	
	/**
	 * 
	 * @param con
	 * @param consecutivoInfoParto
	 * @return
	 */
	public HashMap cargarVigilanciaClinica(	Connection con, 
			        						String consecutivoInfoParto
										 );
	
	/**
	 * 
	 * @param con
	 * @param consecutivoInfoParto
	 * @param vigilanciaClinicaMap
	 * @param insertSecuencia
	 * @return
	 */
	public boolean insertarVigilenciaClinica(Connection con, String consecutivoInfoParto, HashMap vigilanciaClinicaMap, String usuario);

	/**
	 * 
	 * @param con
	 * @param consecutivoInfoParto
	 * @return
	 */
	public boolean existePartogramaDadoConsecutivoInfoParto(Connection con, String consecutivoInfoParto);
	
	/**
	 * 
	 * @param con
	 * @param consecutivoInfoParto
	 * @param partogramaMap
	 * @return
	 */
	public boolean insertarPartograma (	Connection con,
												String consecutivoInfoParto,
												HashMap partogramaMap
											  );
	
	/**
	 * 
	 * @param con
	 * @param consecutivoInfoParto
	 * @param partogramaMap
	 * @return
	 */
	public boolean modificarPartograma (	Connection con,
												String consecutivoInfoParto,
												HashMap partogramaMap
											  );
	
	
	/**
	 * 
	 * @param con
	 * @param consecutivoInfoParto
	 * @return
	 */
	public HashMap cargarPartograma(Connection con, String consecutivoInfoParto);
	
	/**
	 * 
	 * @param con
	 * @param codigoCx
	 * @return
	 */
	public String obtenerConsecutivoInfoPartoDadoCx(Connection con, String codigoCx);
	
	/**
	 * 
	 * @param con
	 * @param codigoCx
	 * @param codigoInstitucion
	 * @return
	 */
	public String obtenerControlPrenatal(Connection con, String codigoCx, int codigoInstitucion);
	
	/**
	 * 
	 * @param con
	 * @param consecutivoInfoParto
	 * @return
	 */
	public String obtenerUltimaFechaHoraProcesoPartograma(Connection con, String consecutivoInfoParto);
	
	/**
	 * 
	 * @param con
	 * @param consecutivoInfoParto
	 * @param vigilanciaClinicaMap
	 * @return
	 */
	public boolean modificarVigilanciaParto (	Connection con, String consecutivoInfoParto, HashMap vigilanciaClinicaMap, String usuario );

	/**
	 * 
	 * @param con
	 * @param cirugia
	 * @return
	 */
	public boolean esCirugiaAborto(Connection con, int cirugia);

	/**
	 * 
	 * @param con
	 * @param cirugia
	 * @return
	 */
	public int cantidadHijosVivosMuertos(Connection con, int cirugia);
	
	/**
	 * Método implementado para consultar la fecha/hora ingreso a hospitalizacion
	 * a partir de la cuenta asociada
	 * @param con
	 * @param cuentaAsocio
	 * @return
	 */
	public String[] consultarFechaIngresoCasoAsocio(Connection con,String cuentaAsocio);
}
