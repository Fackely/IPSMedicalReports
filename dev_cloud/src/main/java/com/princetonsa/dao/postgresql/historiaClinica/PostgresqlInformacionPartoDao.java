/*
 * Junio 06,2006
 */
package com.princetonsa.dao.postgresql.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.historiaClinica.InformacionPartoDao;
import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseInformacionPartoDao;

/**
 * @author Sebastián Gómez 
 *
 * Clase que maneja los métodos propìos de Postgres para el acceso a la fuente
 * de datos en la funcionalidad Información del Parto
 */
public class PostgresqlInformacionPartoDao implements InformacionPartoDao 
{
	/**
	 * Método implementado para insertar la informacion del parto
	 * @param con
	 * @param infoParto
	 * @param estado
	 * @return
	 */
	public String insertarInformacionParto(Connection con,HashMap infoParto,String estado)
	{
		return SqlBaseInformacionPartoDao.insertarInformacionParto(con,infoParto,"nextval('seq_informacion_parto')","nextval('seq_info_parto_secciones')","nextval('seq_info_aborto_hijos')",estado);
	}
	
	/**
	 * Método que carga la información de un parto
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap cargarInformacionParto(Connection con,HashMap campos)
	{
		return SqlBaseInformacionPartoDao.cargarInformacionParto(con,campos);
	}
	
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
	public HashMap cargarSolicitudesInformacionParto(Connection con,String codigoPaciente,String fechaInicial,String fechaFinal,int centroAtencion)
	{
		return SqlBaseInformacionPartoDao.cargarSolicitudesInformacionParto(con,codigoPaciente,fechaInicial,fechaFinal,centroAtencion);
	}
	
	/**
	 * Método para obtener el consecutivo de la cirugia
	 * de parto que está dentro de la solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public String obtenerCodigoCirugiaParto(Connection con,String numeroSolicitud)
	{
		return SqlBaseInformacionPartoDao.obtenerCodigoCirugiaParto(con,numeroSolicitud);
	}
	
	/**
	 * Método que consulta la fecha/hora del egreso de una admision de un parto
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public String obtenerFechaHoraEgresoAdmisionParto(Connection con,String codigoCuenta)
	{
		return SqlBaseInformacionPartoDao.obtenerFechaHoraEgresoAdmisionParto(con,codigoCuenta);
	}
	
	/**
	 * Método que consulta la fecha y hora egreso de la admision del parto
	 * partiendo desde el codigo de la cirugia
	 * @param con
	 * @param codigoCirugia
	 * @return
	 */
	public String obtenerFechaHoraEgresoAdmisionPartoCirugia(Connection con,String codigoCirugia)
	{
		return SqlBaseInformacionPartoDao.obtenerFechaHoraEgresoAdmisionPartoCirugia(con, codigoCirugia);
	}
	
	/**
	 * Método implementado para verificar si el paciente tiene informacion de partos
	 * pendiente de ingresar
	 * @param con
	 * @param codigoPaciente
	 * @param centroAtencion
	 * @return
	 */
	public String validacionInformacionPartoPaciente(Connection con,String codigoPaciente,int centroAtencion)
	{
		return SqlBaseInformacionPartoDao.validacionInformacionPartoPaciente(con,codigoPaciente,centroAtencion);
	}
	
	/**
	 * Método que consulta el numero de embarazo de un embarazo no finalizado del paciente
	 * desde la hoja obstétrica
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public int consultarNumeroEmbarazoHojaObstetrica(Connection con,String codigoPaciente)
	{
		return SqlBaseInformacionPartoDao.consultarNumeroEmbarazoHojaObstetrica(con, codigoPaciente);
	}
	
	/**
	 * Método que verifica si ya existe un numero de embarazo en los registros de hoja obstétrica del paciente
	 * @param con
	 * @param codigoPaciente
	 * @param numeroEmbarazo
	 * @return
	 */
	public boolean existeNumeroEmbarazoHojaObstetrica(Connection con,String codigoPaciente,int numeroEmbarazo)
	{
		return SqlBaseInformacionPartoDao.existeNumeroEmbarazoHojaObstetrica(con, codigoPaciente, numeroEmbarazo);
	}
	
	/**
	 * Método implementado para obtener las consultas prenatales de la hoja obstetrica
	 * @param con
	 * @param campos
	 * @return
	 */
	public int obtenerConsultasPrenatalesHojaObstetrica(Connection con,HashMap campos)
	{
		return SqlBaseInformacionPartoDao.obtenerConsultasPrenatalesHojaObstetrica(con,campos);
	}
	
	/**
	 * 
	 * @param con
	 * @param consecutivoInfoParto
	 * @return
	 */
	public HashMap cargarVigilanciaClinica(	Connection con, 
			        						String consecutivoInfoParto
										 )
	{
		return SqlBaseInformacionPartoDao.cargarVigilanciaClinica(con, consecutivoInfoParto);
	}
	
	/**
	 * 
	 * @param con
	 * @param consecutivoInfoParto
	 * @param vigilanciaClinicaMap
	 * @return
	 */
	public boolean insertarVigilenciaClinica(Connection con, String consecutivoInfoParto, HashMap vigilanciaClinicaMap, String usuario)
	{
		return SqlBaseInformacionPartoDao.insertarVigilenciaClinica(con, consecutivoInfoParto, vigilanciaClinicaMap, "nextval('seq_vig_cli_trab_parto')", usuario);
	}
	
	/**
	 * 
	 * @param con
	 * @param consecutivoInfoParto
	 * @return
	 */
	public boolean existePartogramaDadoConsecutivoInfoParto(Connection con, String consecutivoInfoParto)
	{
		return SqlBaseInformacionPartoDao.existePartogramaDadoConsecutivoInfoParto(con, consecutivoInfoParto);
	}
	
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
											  )
	{
		return SqlBaseInformacionPartoDao.insertarPartograma(con, consecutivoInfoParto, partogramaMap);
	}
	
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
											  )
	{
		return SqlBaseInformacionPartoDao.modificarPartograma(con, consecutivoInfoParto, partogramaMap);
	}
	
	/**
	 * 
	 * @param con
	 * @param consecutivoInfoParto
	 * @return
	 */
	public HashMap cargarPartograma(Connection con, String consecutivoInfoParto)
	{
		return SqlBaseInformacionPartoDao.cargarPartograma(con, consecutivoInfoParto);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoCx
	 * @return
	 */
	public String obtenerConsecutivoInfoPartoDadoCx(Connection con, String codigoCx)
	{
		return SqlBaseInformacionPartoDao.obtenerConsecutivoInfoPartoDadoCx(con, codigoCx);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoCx
	 * @param codigoInstitucion
	 * @param fromStatement
	 * @return
	 */
	public String obtenerControlPrenatal(Connection con, String codigoCx, int codigoInstitucion)
	{
		return SqlBaseInformacionPartoDao.obtenerControlPrenatal(con, codigoCx, codigoInstitucion, "");
	}
	
	/**
	 * 
	 * @param con
	 * @param consecutivoInfoParto
	 * @return
	 */
	public String obtenerUltimaFechaHoraProcesoPartograma(Connection con, String consecutivoInfoParto)
	{
		return SqlBaseInformacionPartoDao.obtenerUltimaFechaHoraProcesoPartograma(con, consecutivoInfoParto);
	}
	
	/**
	 * 
	 * @param con
	 * @param consecutivoInfoParto
	 * @param vigilanciaClinicaMap
	 * @return
	 */
	public boolean modificarVigilanciaParto (	Connection con, String consecutivoInfoParto, HashMap vigilanciaClinicaMap, String usuario )
	{
		return SqlBaseInformacionPartoDao.modificarVigilanciaParto(con, consecutivoInfoParto, vigilanciaClinicaMap, usuario);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param cirugia
	 * @return
	 */
	public boolean esCirugiaAborto(Connection con, int cirugia)
	{
		return  SqlBaseInformacionPartoDao.esCirugiaAborto(con,cirugia);
	}
	
	/**
	 * 
	 * @param con
	 * @param cirugia
	 * @return
	 */
	public int cantidadHijosVivosMuertos(Connection con, int cirugia)
	{
		return SqlBaseInformacionPartoDao.cantidadHijosVivosMuertos(con,cirugia);
	}
	
	/**
	 * Método implementado para consultar la fecha/hora ingreso a hospitalizacion
	 * a partir de la cuenta asociada
	 * @param con
	 * @param cuentaAsocio
	 * @return
	 */
	public String[] consultarFechaIngresoCasoAsocio(Connection con,String cuentaAsocio)
	{
		return SqlBaseInformacionPartoDao.consultarFechaIngresoCasoAsocio(con, cuentaAsocio);
	}
}
