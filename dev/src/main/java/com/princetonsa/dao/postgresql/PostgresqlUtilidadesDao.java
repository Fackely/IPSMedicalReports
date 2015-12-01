/*
 * Creado en 4/02/2005
 *
 * Princeton S.A.
 */
package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.InfoDatosString;
import util.ResultadoBoolean;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.UtilidadesDao;
import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.interfaz.DtoInterfazAbonos;
import com.princetonsa.dto.manejoPaciente.DtoDiagnostico;
import com.princetonsa.dto.odontologia.DtoLogProcAutoEstados;
import com.princetonsa.dto.tesoreria.DtoConceptosIngTesoreria;
import com.servinte.axioma.dto.salascirugia.EspecialidadDto;
import com.servinte.axioma.dto.salascirugia.ProfesionalHQxDto;
import com.servinte.axioma.fwk.exception.BDException;

/**
 * @author Juan David Ramï¿½rez Lï¿½pez
 *
 * Princeton S.A.
 */
public class PostgresqlUtilidadesDao implements UtilidadesDao
{
	/**
	 * Manejador de logs de la clase
	 */
	private Logger logger=Logger.getLogger(PostgresqlUtilidadesDao.class);
	
	/**
	 * Cadena usada para consultar la descripciï¿½n de un esquema tarifario
	 * general o especï¿½fico
	 */
	private static final String obtenerNomEsquemaTarifarioStr="select getnomesqporcentaje(?,?) As esquema_tarifario";
	
	/**
	 * Insertar ingreso por cuidados especiales
	 */
	private static String strIsertarIngresoCuidadosEspeciales="INSERT INTO ingresos_cuidados_especiales (codigo," +
																			"ingreso," +
																			"estado," +
																			"indicativo," +
																			"tipo_monitoreo," +
																			"usuario_resp," +
																			"fecha_resp," +
																			"hora_resp," +
																			"usuario_modifica," +
																			"fecha_modifica," +
																			"hora_modifica," +
																			"centro_costo" +
																			") " +
																"VALUES (nextval('historiaclinica.seq_ingres_cuidados_especial')," +
																			"?," +
																			"?," +
																			"?," +
																			"?," +
																			"?," +
																			"CURRENT_DATE," +
																			""+ValoresPorDefecto.getSentenciaHoraActualBD()+"," +
																			"?," +
																			"CURRENT_DATE," +
																			""+ValoresPorDefecto.getSentenciaHoraActualBD()+"," +
																			"?" +
																			") ";
	
	
	private static String consultaUltimaCamaPaciente = 
			  "SELECT * FROM "+
			  "( "+
			    "SELECT "+
			      "cuentas.id AS cuenta, "+
			      "admisiones_urgencias.cama_observacion AS cama, "+ 
			      "admisiones_urgencias.fecha_admision AS fecha, "+
			      "admisiones_urgencias.hora_admision AS hora "+
			    "FROM "+
			      "manejopaciente.cuentas, "+ 
			      "manejopaciente.admisiones_urgencias "+
			    "WHERE "+
			      "admisiones_urgencias.cuenta = cuentas.id AND "+
			      "cuentas.codigo_paciente = ? "+
			    
			    "UNION ALL "+
			    
			    "SELECT "+
			      "cuentas.id AS cuenta, "+
			      "admisiones_hospi.cama AS cama, "+ 
			      "admisiones_hospi.fecha_grabacion AS fecha, "+
			      "admisiones_hospi.hora_admision AS hora "+
			    "FROM "+
			      "manejopaciente.admisiones_hospi, "+ 
			      "manejopaciente.cuentas "+
			    "WHERE "+
			      "admisiones_hospi.cuenta = cuentas.id AND "+
			      "cuentas.codigo_paciente = ? "+
			    
			    "UNION ALL "+
			    
			    "SELECT "+
			      "cuentas.id AS cuenta, "+
			      "traslado_cama.codigo_nueva_cama AS cama, "+ 
			      "traslado_cama.fecha_asignacion AS fecha, "+
			      "traslado_cama.hora_asignacion AS hora "+
			    "FROM "+
			      "manejopaciente.traslado_cama, "+ 
			      "manejopaciente.cuentas "+
			    "WHERE "+
			      "traslado_cama.cuenta = cuentas.id AND "+
			      "cuentas.codigo_paciente = ? "+
			  ") "+
			  "AS subconsulta "+
			  "ORDER BY to_timestamp(to_char(fecha, 'DD/MM/YYYY') || ' ' || to_char(hora, 'HH24:MI'), 'DD/MM/YYYY HH24:MI') DESC "+
			  "LIMIT 1 ";
	
	private static String consultaPacienteEnCama = 
			  "SELECT * FROM "+
			  "( "+
			    "SELECT "+
			      "cuentas.id AS cuenta, "+
			      "admisiones_urgencias.cama_observacion AS cama, "+ 
			      "admisiones_urgencias.fecha_admision AS fecha, "+
			      "admisiones_urgencias.hora_admision AS hora "+
			    "FROM "+
			      "manejopaciente.cuentas, "+ 
			      "manejopaciente.admisiones_urgencias "+
			    "WHERE "+
			      "admisiones_urgencias.cuenta = cuentas.id AND "+
			      "admisiones_urgencias.cama_observacion = ? "+
			    
			    "UNION ALL "+
			    
			    "SELECT "+
			      "cuentas.id AS cuenta, "+
			      "admisiones_hospi.cama AS cama, "+ 
			      "admisiones_hospi.fecha_grabacion AS fecha, "+ 
			      "admisiones_hospi.hora_admision AS hora "+
			    "FROM "+
			      "manejopaciente.admisiones_hospi, "+ 
			      "manejopaciente.cuentas "+
			    "WHERE "+
			      "admisiones_hospi.cuenta = cuentas.id AND "+
			      "admisiones_hospi.cama = ? "+
			    
			    "UNION ALL "+
			    
			    "SELECT "+
			      "cuentas.id AS cuenta, "+
			      "traslado_cama.codigo_nueva_cama AS cama, "+ 
			      "traslado_cama.fecha_asignacion AS fecha, "+
			      "traslado_cama.hora_asignacion AS hora "+
			    "FROM "+
			      "manejopaciente.traslado_cama, "+ 
			      "manejopaciente.cuentas "+
			    "WHERE "+
			      "traslado_cama.cuenta = cuentas.id AND "+
			      "traslado_cama.codigo_nueva_cama = ? "+
			  ") "+
			  "AS subconsulta "+
			  "ORDER BY to_timestamp(to_char(fecha, 'DD/MM/YYYY') || ' ' || to_char(hora, 'HH24:MI'), 'DD/MM/YYYY HH24:MI') DESC "+
			  "LIMIT 1 ";
	
	/**
	 * Metodo para verificar si una solicitud de medicamentos tiene despachos
	 * @param con Conexiï¿½n con la BD
	 * @param numeroSolicitud Solicitud a verificar
	 * @return true si la solicitud tiene despachos, false de lo contrario
	 */
	public boolean hayDespachosEnSolicitud(Connection con, int numeroSolicitud)
	{
		return SqlBaseUtilidadesDao.hayDespachosEnSolicitud(con, numeroSolicitud);
	}
	
	/**
	 * Mï¿½todo que carga la fecha de la base de datos para no utilizar la del sistema
	 * @param con Conexión con la BD
	 * @return String con la fecha del sistema formato BD
	 */
	public String capturarFechaBD(Connection con)
	{
		String consultaFechaStr="SELECT CURRENT_DATE AS fecha";
		return SqlBaseUtilidadesDao.capturarFechaBD(con, consultaFechaStr);
	}

	/**
	 * Mï¿½todo que carga la hora de la base de datos para no utilizar la del sistema
	 * @param con Conexiï¿½n con la BD
	 * @return String con la Hora del sistema cinco caracteres
	 */
	public String capturarHoraBD(Connection con)
	{
		String consultaHoraStr="SELECT to_char(timestamp 'now','HH24:MI') AS hora";
		return SqlBaseUtilidadesDao.capturarHoraBD(con, consultaHoraStr);
	}
	/**
	 * Mï¿½todo para cargar la Fecha-Hora del egreso de la cuenta Urgencias cuando
	 * se hace un Asocio de Cuenta
	 * @param con
	 * @param codigo_paciente
	 * @return
	 */
	public String capturarFechayHoraEgresoUrgenciasEnAsocio(Connection con, String idIngreso,String viaIngreso) {
		// @todo Auto-generated method stub
		return SqlBaseUtilidadesDao.capturarFechayHoraEgresoUrgenciasEnAsocio(con,idIngreso,viaIngreso);
	}
		/**
	 * Mï¿½todo para obtener el codigo del contrato el cual
	 * fue utilizado en su ï¿½ltima cuenta abierta
	 * @param con Conexiï¿½n con la BD
	 * @param codigoPaciente Cï¿½digo del paciente
	 * @return Codigo del ultimo contrato del paciente
	 */
	public int obtenerUltimoContrtatoPaciente(Connection con, int codigoPaciente)
	{
		return SqlBaseUtilidadesDao.obtenerUltimoContrtatoPaciente(con, codigoPaciente);
	}
	
	
	/**
	 * Metodo que retorna el codigo del Cie Actual.
	 * @param con
	 * @return
	 */
	public int codigoCieActual(Connection con)
	{
		String cadena="select codigo as codigo from (select * from tipos_cie where vigencia<=current_date order by vigencia desc) as consulta "+ValoresPorDefecto.getValorLimit1()+" 1";
		return SqlBaseUtilidadesDao.codigoCieActual(con,cadena);
	}
	/**
	 * Adiciï¿½n Sebastiï¿½n
	 * Mï¿½todo usado para obtener el destino de salida de un egreso de evoluciï¿½n
	 * @param con
	 * @param idCuenta
	 * @param obtenerDestinoSalidaStr
	 * @return
	 */
	public int obtenerDestinoSalidaEgresoEvolucion(Connection con,int idCuenta){
		return SqlBaseUtilidadesDao.obtenerDestinoSalidaEgresoEvolucion(con,idCuenta);
	}
	
	/**
	 * Mï¿½todo que carga el numero de solicitud
	 * de la ultima valoraciï¿½n de interconsulta
	 * @param con
	 * @param codigoPersona
	 * @return numeroSolicitud ï¿½ltima interconsulta
	 */
	public int obtenerUltimaValoracionInterconsulta(Connection con, int codigoPersona)
	{
		return SqlBaseUtilidadesDao.obtenerUltimaValoracionInterconsulta(con, codigoPersona);
	}
	
	/**
	 * Adiciï¿½n Sebastiï¿½n
	 * Mï¿½todo para saber el nï¿½mero de solicitudes de una cuenta
	 * @param con
	 * @param idCuenta
	 * @return nï¿½mero de solicitudes
	 */
	public int obtenerNumeroSolicitudesCuenta(Connection con,int idCuenta){
		return SqlBaseUtilidadesDao.obtenerNumeroSolicitudesCuenta(con,idCuenta);
	}
	
	/**
	 * @param con
	 * @param consecutivoFactura
	 * @return
	 */
	public int obtenerCodigoFactura(Connection con, int consecutivoFactura)
	{
		return SqlBaseUtilidadesDao.obtenerCodigoFactura(con,consecutivoFactura);
	}

	/**
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public String obtenerFechaFacturacion(Connection con, int codigoFactura)
	{
		return SqlBaseUtilidadesDao.obtenerFechaFacturacion(con,codigoFactura);
	}
	
	/**
	 * Metodo que retorna la cuenta de cobro de una factura.
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public double obtenerCuentaCobroFactura(Connection con, int codigoFactura)
	{
		return SqlBaseUtilidadesDao.obtenerCuentaCobroFactura(con,codigoFactura);
	}
	/**
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public String obtenerEstadoFactura(Connection con, int codigoFactura)
	{
		return SqlBaseUtilidadesDao.obtenerEstadoFactura(con,codigoFactura);
	}
	
	/**
	 * Este mï¿½todo devuelve los cï¿½digos de las justificaciones
	 * filtrando por instituciï¿½n
	 * @param con
	 * @param codigoInstitucion
	 * @param restringirSoloRequeridas
	 * @return Array de enteros con los cï¿½digos de las justificaciones
	 */
	public int[] buscarCodigosJustificaciones(Connection con, int codigoInstitucion, boolean restringirSoloRequeridas, boolean esArticulo)
	{
		return SqlBaseUtilidadesDao.buscarCodigosJustificaciones(con, codigoInstitucion, restringirSoloRequeridas, esArticulo);
	}
	
	/**
	 * Este mï¿½todo devuelve los cï¿½digos y los nombres de las justificaciones
	 * filtrando por instituciï¿½n del usuario
	 * @param codigoInstitucion
	 * @param restringirSoloRequeridas
	 * @return Vector con los cï¿½digos y los nombres de los Atributos de la justificaciï¿½n
	 */
	public Vector buscarCodigosNombresJustificaciones(Connection con, int codigoInstitucion, boolean restringirSoloRequeridas, boolean esArticulo)
	{
		return SqlBaseUtilidadesDao.buscarCodigosNombresJustificaciones(con, codigoInstitucion, restringirSoloRequeridas, esArticulo);
	}

	/**
	 * Mï¿½todo que verifica si el tipo de identificaciï¿½n maneja consecutivo
	 * automï¿½tico
	 * @param con
	 * @param acronimo
	 * @return
	 */
	public boolean esAutomaticoTipoId(Connection con,String acronimo, int codigoInstitucion){
		return SqlBaseUtilidadesDao.esAutomaticoTipoId(con,acronimo,codigoInstitucion);
	}
	
	/**
	 * Adiciï¿½n Sebastiï¿½n
	 * Mï¿½todo usado para obtener el nï¿½mero de la factura (prefijo+consecutivo)
	 * @param con
	 * @param numeroSolicitud
	 * @return numero de factura (prefijo+consecutivo)
	 */
	public String obtenerNumeroFactura(Connection con, int numeroSolicitud)
	{
		return SqlBaseUtilidadesDao.obtenerNumeroFactura(con,numeroSolicitud);
	}
	
	/**
	 * Adiciï¿½n Sebastiï¿½n
	 * Mï¿½todo que adiciona el diagnï¿½stico y tipo cie a pagos paciente.
	 * @param con
	 * @param codigoPaciente
	 * @param numeroDocumento
	 * @param diagnostico
	 * @param tipoCie
	 * @return -1 no exitoso, 1 exitoso
	 */
	public int actualizarDiagnosticoTopesPaciente(Connection con,
			int codigoPaciente,String numeroDocumento,String diagnostico, String tipoCie)
	{
		return SqlBaseUtilidadesDao.actualizarDiagnosticoTopesPaciente(con,codigoPaciente,numeroDocumento,diagnostico,tipoCie);
	}
	
	/**
	 * Mï¿½todo para capturar el cï¿½digo de la ultima via de ingreso del paciente
	 * que no tenga cuenta activa.
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public int obtenerCodigoUltimaViaIngreso(Connection con,int codigoPaciente)
	{
		return SqlBaseUtilidadesDao.obtenerCodigoUltimaViaIngreso(con,codigoPaciente);
	}
	
	/**
	 * Adiciï¿½n Sebastiï¿½n
	 * Mï¿½todo obtener el ID de la ï¿½ltima cuenta del paciente
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public int obtenerIdUltimaCuenta(Connection con,int codigoPaciente)
	{
		return SqlBaseUtilidadesDao.obtenerIdUltimaCuenta(con,codigoPaciente, DaoFactory.POSTGRESQL);
	}
	
	 /**
	  * Funciï¿½n que verifica el estado true o flase de la autorizacion en la tabla atributo_solicitud
    * @param con
    * @param codAtributoSolicitud
    * @return
    */
   public boolean verificarAutorizacion(Connection con, int codAtributoSolicitud)
   {
       return SqlBaseUtilidadesDao.verificarAutorizacion(con,codAtributoSolicitud);
   }
   
   /**
	 * Adiciï¿½n Sebastiï¿½n
	 * Mï¿½todo usado para verificar si un servicio o un artï¿½culo que requiera justificaciï¿½n
	 * ha sido justificado. 
	 * @param con
	 * @param numeroSolicitud
	 * @param parametro (se coloca el cï¿½digo del articulo o el cï¿½digo del servicio segï¿½n el parï¿½metro esArticulo)
	 * @param codigoInstitucion
	 * @param esArticulo
	 * @return
	 */
	public boolean esSolicitudJustificada(Connection con,int numeroSolicitud,int parametro,int codigoInstitucion,boolean esArticulo)
	{
		return SqlBaseUtilidadesDao.esSolicitudJustificada(con,numeroSolicitud,parametro,codigoInstitucion,esArticulo);
	}
	
	/**
	 * Adiciï¿½n Sebastiï¿½n
	 * Mï¿½todo usado para obtener el cï¿½digo y la descripcion de cada atributo de la justificacion del medicamento
	 * o servicio
	 * @param con
	 * @param numeroSolicitud
	 * @param parametro (se coloca el cï¿½digo del articulo o el cï¿½digo del servicio segï¿½n el parï¿½metro esArticulo)
	 * @param esArticulo
	 * @return
	 */
	public ResultSetDecorator obtenerAtributosJustificacion(Connection con,int numeroSolicitud,int parametro,boolean esArticulo)
	{
		return SqlBaseUtilidadesDao.obtenerAtributosJustificacion(con,numeroSolicitud,parametro,esArticulo);
	}
	

	/**
	 * @param con
	 * @param consecutivoFactura
	 * @return
	 */
	public int obtenerCodigoFactura(Connection con, int consecutivoFactura,int institucion)
	{
		return SqlBaseUtilidadesDao.obtenerCodigoFactura(con,consecutivoFactura,institucion);
	}

	/**
	 * Metodo para cargar los datos del embarazo necesarios en la valoracion de gineco!
	 * Embarazada - FUR - FPP - Edad Gestacional
	 * @param con
	 * @param codigoPersona
	 */
	public Vector cargarDatosEmbarazo(Connection con, int codigoPersona)
	{
		return SqlBaseUtilidadesDao.cargarDatosEmbarazo(con, codigoPersona);
	}

	/**
	 * Mï¿½todo para el ï¿½ltimo tipo de parto
	 * @param con
	 * @param codigoPaciente
	 * @return InfoDatosInt con codigo y nombre del ï¿½ltimo tipo de parto
	 */
	public InfoDatosInt obtenerUltimoTipoParto(Connection con, int codigoPaciente)
	{
		return SqlBaseUtilidadesDao.obtenerUltimoTipoParto(con, codigoPaciente);
	}

	/**
	 * Metodo para cargar los datos de los antecedentes gineco-obstetricos necesarios en la valoracion de gineco!
	 * EdadMenarquia - OtraEdadMenarquia - EdadMenopausia - OtraEdadMenopausia
	 * @param con
	 * @param codigoPersona
	 * @return Vector con los datos del embarazo
	 */
	public Vector cargarDatosAntGineco(Connection con, int codigoPersona)
	{
		return SqlBaseUtilidadesDao.cargarDatosAntGineco(con, codigoPersona);
	}
	
	/**
	 * Mï¿½todo para obtener el ï¿½ltimo nï¿½mero del embarazo del paciente
	 * @param con -> Connection
	 * @param codigoPaciente -> int
	 * @return numeroEmbarazo -> int
	 */
	public int obtenerUltimoNumeroEmbarazo(Connection con, int codigoPaciente)
	{
		return SqlBaseUtilidadesDao.obtenerUltimoNumeroEmbarazo(con, codigoPaciente);
	}

	public int[] consultarTiposPartoVaginal(Connection con, String padre)
	{
		return SqlBaseUtilidadesDao.consultarTiposPartoVaginal(con, padre);
	}

	/**
	 * Mï¿½todo para consultar embarazos sin finalizar
	 * @param con Conexion con la BD
	 * @param codigoPaciente Codigo del paciente
	 * @param codigoEmbarazo Codigo del embarazo
	 * @return true si el embarazo no se a finalizado
	 */
	public boolean esEmbarazoSinTerminar(Connection con, int codigoPaciente, int codigoEmbarazo)
	{
		return SqlBaseUtilidadesDao.esEmbarazoSinTerminar(con, codigoPaciente, codigoEmbarazo);
	}

	/**
	 * Mï¿½todo para consultar en la BD el nombre de la edad menarquia
	 * o menopausia
	 * @param con
	 * @param codigoRango
	 * @param esMenarquia true consulta la menarqui, false la menopausia
	 * @return Striong con el nombre obtenido
	 */
	public String obtenerNombreEdadMenarquiaOMenopausia(Connection con, int codigoRango, boolean esMenarquia)
	{
		return SqlBaseUtilidadesDao.obtenerNombreEdadMenarquiaOMenopausia(con, codigoRango, esMenarquia);
	}
	
	/**
	 * Mï¿½todo para obtener el ï¿½ltimo valor de la altura uterina en la valoraciï¿½n gineco-obstï¿½trica
	 * 
	 * @param con
	 * @param institucion
	 * @param codigoPaciente
	 */
	
	public String obtenerAlturaUterinaValoracion(Connection con, int institucion, int codigoPaciente)
	{
		String sentencia="SELECT getUltimoValorAlturaUterina("+institucion+","+codigoPaciente+") AS altura_uterina";
		return SqlBaseUtilidadesDao.obtenerAlturaUterinaValoracion(con, sentencia);
	}
	
	/** Mï¿½todo usado para obtener los pooles vigentes en los cuales 
	 * se encuentra el medico, 
	 * @param con Connection, conexiï¿½n con la fuente de datos
	 * @param fecha String
	 * @param codMedico int  
	 * @return ResultSet
	 * @author jarloc
	 */
	public ResultSetDecorator obtenerPoolesMedico(Connection con, String fecha, int codMedico, boolean activos)
	{
	    return SqlBaseUtilidadesDao.obtenerPoolesMedico(con,fecha,codMedico, activos);
	}
	
	/**
	 * Mï¿½todo para obtener el noimbre del concepto de menstruaciï¿½n
	 * recibiendo como parï¿½mero el cï¿½digo
	 * @param con Connection Conexiï¿½n con la BD
	 * @param conceptoMenstruacion int Codigo del concepto Menstruaciï¿½n
	 * @return
	 */
	public String obtenerNombreConceptoMenstruacion(Connection con, int conceptoMenstruacion)
	{
		return SqlBaseUtilidadesDao.obtenerNombreConceptoMenstruacion(con, conceptoMenstruacion);
	}

	/**
	 * @param con
	 * @param nombreSecuencia
	 * @return
	 */
	public int getSiguienteValorSecuencia(Connection con, String nombreSecuencia)
	{
		String cadena="select nextval('"+nombreSecuencia+"') as secuencia"; 
		return SqlBaseUtilidadesDao.getSiguienteValorSecuencia(con,cadena);
	}
	
		
	/**
	 * Mï¿½todo para obtener todos los pooles
	 * a los cuales pertenecen los mï¿½dicos que
	 * realizaron cualquier atenciï¿½n al
	 * paciente
	 * @param con
	 * @param idCuenta
	 * @return Vector
	 */
	public Vector obtenerPoolesCuenta(Connection con, int idCuenta)
	{
		String busquedaPoolesCuenta="SELECT pp.pool AS pool, pool.descripcion AS descripcion FROM participaciones_pooles pp INNER JOIN pooles pool ON(pool.activo=1 AND pool.codigo=pp.pool) WHERE medico IN(SELECT distinct(CASE WHEN sol.codigo_medico_responde IS NULL THEN (SELECT distinct(a.codigo_medico) FROM cita c INNER JOIN agenda a ON(a.codigo=c.codigo_agenda) WHERE numero_solicitud=sol.numero_solicitud) ELSE sol.codigo_medico_responde END) from solicitudes sol where sol.cuenta=? and sol.tipo NOT IN ("+ConstantesBD.codigoTipoSolicitudMedicamentos+", "+ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos+") and sol.centro_costo_solicitado<>"+ConstantesBD.codigoCentroCostoExternos+" and (sol.estado_historia_clinica<>"+ConstantesBD.codigoEstadoHCAnulada+")) AND (pp.fecha_ingreso<CURRENT_DATE or (pp.fecha_ingreso=CURRENT_DATE and pp.hora_ingreso<=CURRENT_TIME)) and (pp.fecha_retiro IS NULL or (pp.fecha_retiro>CURRENT_DATE OR (pp.fecha_retiro=CURRENT_DATE AND pp.hora_retiro>=CURRENT_TIME)))";
		try
		{
			PreparedStatementDecorator statement= new PreparedStatementDecorator(con.prepareStatement(busquedaPoolesCuenta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			statement.setInt(1, idCuenta);
			ResultSetDecorator resultado=new ResultSetDecorator(statement.executeQuery());
			Vector pooles=new Vector();
			if(resultado.next())
			{
				Vector pool=new Vector();
				pool.add(new Integer(resultado.getInt("pool")));
				pool.add(resultado.getString("descripcion"));
				pooles.add(pool);
			}
			return pooles;
		}
		catch (SQLException e)
		{
			logger.error("Error consultando los pooles relacionados a una cuenta "+e);
			return null;
		}
	}

	

	/**
	 * @param con
	 * @param cuentaCobro
	 * @return
	 */
	public int obtenerEstadoCuentaCobro(Connection con, double cuentaCobro,int institucion)
	{
		return SqlBaseUtilidadesDao.obtenerEstadoCuentaCobro(con,cuentaCobro,institucion);
	}

	
	/**
	 * @param con
	 * @param cuentaCobro
	 * @return
	 */
	public String obtenerFechaRadicacionCuentaCobro(Connection con, double cuentaCobro)
	{
		return SqlBaseUtilidadesDao.obtenerFechaRadicacionCuentaCobro(con,cuentaCobro);
	}
	
	/**
	 * Mï¿½todo que consulta los datos del tipo de monitoreo
	 * @param con
	 * @param tipoMonitoreo
	 * @return
	 */
	public String[] getTipoMonitoreo(Connection con,int tipoMonitoreo)
	{
		return SqlBaseUtilidadesDao.getTipoMonitoreo(con,tipoMonitoreo);
	}
	
	/**
	 * Mï¿½todo usado para consultar la fecha de la orden de salida
	 * de una cuenta de Urgencias u Hospitalizaciï¿½n
	 * @param con
	 * @param idCuenta
	 * @param institucion
	 * @return si no se encuentra la fecha devuelve cadena vacï¿½a
	 */
	public String obtenerFechaOrdenSalida(Connection con,int idCuenta,int institucion)
	{
		return SqlBaseUtilidadesDao.obtenerFechaOrdenSalida(con,idCuenta,institucion);
	}
	
	/**
	 * Mï¿½todo que retorna TRUE para saber si la cuenta tiene solicitud de estancia
	 * para la fecha estipulada, de lo contrario retorna false
	 * @param con
	 * @param idCuenta
	 * @param fechaEstancia
	 * @param institucion
	 * @return
	 */
	public boolean haySolicitudEstancia(Connection con,int idCuenta,String fechaEstancia,int institucion)
	{
		return SqlBaseUtilidadesDao.haySolicitudEstancia(con,idCuenta,fechaEstancia,institucion);
	}
	
	/**
	 * Mï¿½todo para consultar los datos generales de una persona en AXIOMA
	 * sin importar si es mï¿½dico, usuario o paciente
	 * @param con
	 * @param tipoId
	 * @param numeroId
	 * @return
	 */
	public HashMap obtenerDatosPersona(Connection con,String tipoId,String numeroId)
	{
		return SqlBaseUtilidadesDao.obtenerDatosPersona(con,tipoId,numeroId);
	}
	
	/**
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public double obtenerSaldoFactura(Connection con, int codigoFactura)
	{
		return SqlBaseUtilidadesDao.obtenerSaldoFactura(con,codigoFactura);
	}
	
	/**
	 * Mï¿½todo usado para verificar si en la fucnionalidad de 2ï¿½ Nivel
	 * se debe esconder los datos del paciente del cabezaote superior
	 * @param con
	 * @param codigo
	 * @return
	 */
	public boolean deboEsconderPaciente(Connection con,int codigo)
	{
		return SqlBaseUtilidadesDao.deboEsconderPaciente(con,codigo);
	}
	
	/**
	 * @param con
	 * @param codigo
	 * @param factura
	 * @return
	 */
	public boolean existeAjusteNivelFacturaEmpresa(Connection con, double codigo, int factura)
	{
		return SqlBaseUtilidadesDao.existeAjusteNivelFacturaEmpresa(con,codigo,factura);
	}
	
	
	/**
	 * @param con
	 * @param cuentacobro
	 * @param institucion
	 * @return
	 */
	public int numFacturasCuentaCobro(Connection con, double cuentacobro, int institucion)
	{
		return SqlBaseUtilidadesDao.numFacturasCuentaCobro(con,cuentacobro,institucion);
	}
	
	/**
	 * @param con
	 * @param factura
	 * @return
	 */
	public int numServicosArticulosFactura(Connection con, int factura)
	{
		return SqlBaseUtilidadesDao.numServicosArticulosFactura(con,factura);
	}
	
	/**
	 * @param con
	 * @param detFactSolicitud
	 * @return
	 */
	public double obtenerPorcentajePoolFacturacion(Connection con, int detFactSolicitud)
	{
		return SqlBaseUtilidadesDao.obtenerPorcentajePoolFacturacion(con,detFactSolicitud);
	}
	
	/**
	 * @param con
	 * @param loginUsuario
	 * @param codigo_func
	 * @return
	 */
	public boolean tieneRolFuncionalidad(Connection con, String loginUsuario, int codigo_func)
	{
		return SqlBaseUtilidadesDao.tieneRolFuncionalidad(con,loginUsuario,codigo_func);
	}
	
	/**
	 * @param con
	 * @param institucion
	 * @return
	 */
	public boolean isDefinidoConsecutivoAjustesDebito(Connection con, int institucion)
	{
		return SqlBaseUtilidadesDao.isDefinidoConsecutivoAjustesDebito(con,institucion);
	}
	
	
	/**
	 * @param con
	 * @param institucion
	 * @return
	 */
	public boolean isDefinidoConsecutivoAjustesCredito(Connection con, int institucion)
	{
		return SqlBaseUtilidadesDao.isDefinidoConsecutivoAjustesCredito(con,institucion);
	}
	
	
	/**
	 * @param con
	 * @param cuentaCobro
	 * @param institucion
	 * @return
	 */
	public InfoDatosInt obtenerConvenioCuentaCobro(Connection con, double cuentaCobro, int institucion)
	{
		return SqlBaseUtilidadesDao.obtenerConvenioCuentaCobro(con,cuentaCobro,institucion);
	}
	
	/**
	 * @param con
	 * @param conceptoAjuste
	 * @param institucion
	 * @return
	 */
	public String obtenerDescripcionConceptoAjuste(Connection con, String conceptoAjuste, int institucion)
	{
		return SqlBaseUtilidadesDao.obtenerDescripcionConceptoAjuste(con,conceptoAjuste,institucion);
	}
	
	/**
	 * @param con
	 * @param metodoAjuste
	 * @return
	 */
	public String obtenerDescripcionMetodoAjuste(Connection con, String metodoAjuste)
	{
		return SqlBaseUtilidadesDao.obtenerDescripcionMetodoAjuste(con,metodoAjuste);
	}
	
	/**
	 * @param con
	 * @param codigoAjuste
	 * @return
	 */
	public int obtenerFacturaAjuste(Connection con, double codigoAjuste)
	{
		return SqlBaseUtilidadesDao.obtenerFacturaAjuste(con,codigoAjuste);
	}
	
	/**
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public String obtenerConsecutivoFactura(Connection con, int codigoFactura)
	{
		return SqlBaseUtilidadesDao.obtenerConsecutivoFactura(con,codigoFactura);
	}

	/**
	 * @param con
	 * @param factura
	 * @return
	 */
	public InfoDatosInt obtenerConvenioFactura(Connection con, int factura)
	{
		return SqlBaseUtilidadesDao.obtenerConvenioFactura(con,factura);
	}
	
	/**
	 * @param con
	 * @param codigoAjuste
	 * @return
	 */
	public String obtenerFechaAprobacionAjustes(Connection con, double codigoAjuste)
	{
		return SqlBaseUtilidadesDao.obtenerFechaAprobacionAjustes(con,codigoAjuste);
	}
	
	/**
	 * Mï¿½todo usado para consultar la fecha de reversiï¿½n de egreso
	  * @param con
	 * @param idCuenta
	 * @return fecha de reversiï¿½n
	 */
	public String obtenerFechaReversionEgreso(Connection con,int idCuenta)
	{
		return SqlBaseUtilidadesDao.obtenerFechaReversionEgreso(con,idCuenta);
	}
	
	
	/**
	 * @param con
	 * @param codigoAjuste
	 * @return
	 */
	public boolean esAjusteDeReversion(Connection con, double codigoAjuste)
	{
		return SqlBaseUtilidadesDao.esAjusteDeReversion(con,codigoAjuste);
	}
	
	/**
	 * @param con
	 * @param codigoAjuste
	 * @return
	 */
	public String obtenerestadoAjuste(Connection con, double codigoAjuste)
	{
		return SqlBaseUtilidadesDao.obtenerestadoAjuste(con,codigoAjuste);
	}

	/**
	 * @param con
	 * @param numeroAjuste
	 * @param tipoAjuste
	 * @param institucion
	 * @return
	 */
	public double obtenercodigoAjusteEmpresa(Connection con, String numeroAjuste, String tipoAjuste, int institucion)
	{
		return SqlBaseUtilidadesDao.obtenercodigoAjusteEmpresa(con,numeroAjuste,tipoAjuste,institucion);
	}
	
	/**
	 * Salas Cirugï¿½a:
	 * Mï¿½todo usado para obtener el nombre de un tipo de sala
	 * parametrizado en el sistema
	 * @param con
	 * @param codigoTipoSala
	 * @return devuelve cadena vacï¿½a o null si no encuentra el nombre
	 */
	public String obtenerNombreTipoSala(Connection con,int codigoTipoSala)
	{
		return SqlBaseUtilidadesDao.obtenerNombreTipoSala(con,codigoTipoSala);
	}
	
	/**
	 * Salas Cirugï¿½a:
	 * Mï¿½todo que consulta el nombre [0] y acrï¿½nimo [1] del tipo
	 * de asocio.
	 * @param con
	 * @param codigoTipoAsocio
	 * @return
	 */
	public String[] obtenerNombreTipoAsocio(Connection con,int codigoTipoAsocio)
	{
		return SqlBaseUtilidadesDao.obtenerNombreTipoAsocio(con,codigoTipoAsocio);
	}
	
	/**
	 * Mï¿½todo que consulta la descripciï¿½n de un esquema tarifario
	 * esGeneral => "true" se consultarï¿½ un esquema tarifario general de la tabla  esq_tar_porcen_cx
	 * esGeneral=> "false" se cosnultarï¿½ un esquema tarifario especï¿½fico de la tabla esquemas_tarifarios
	 * @param con
	 * @param esGeneral
	 * @param codigo
	 * @param obtenerNomEsquemaTarifarioStr
	 * @return retorna null si no hay resultados
	 */
	public String obtenerNomEsquemaTarifario(Connection con,String esGeneral,int codigo)
	{
		return SqlBaseUtilidadesDao.obtenerNomEsquemaTarifario(con,esGeneral,codigo,obtenerNomEsquemaTarifarioStr);
	}
	
	/**
	 * Metodo que consulta el nombre del convenio dado la cuenta
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public  String obtenerNombreConvenio(Connection con, int cuenta)
	{
		return SqlBaseUtilidadesDao.obtenerNombreConvenio(con, cuenta);
	}
	
    
    /**
     * @param con
     * @param numeroReciboCaja
     * @param institucion
     * @return
     */
    public String obtenerEstadoReciboCaja(Connection con, String numeroReciboCaja, int institucion)
    {
        return SqlBaseUtilidadesDao.obtenerEstadoReciboCaja(con,numeroReciboCaja,institucion);
    }
    
    
    /**
     * @param con
     * @param institucion
     * @param nombreConsecutivo
     * @return
     */
    public boolean isDefinidoConsecutivo(Connection con, int institucion, String nombreConsecutivo)
    {
        return SqlBaseUtilidadesDao.isDefinidoConsecutivo(con,institucion,nombreConsecutivo);
    }
    

    /**
     * @param con
     * @param codigoFactura
     * @param estado
     * @return
     */
    public boolean cambiarEstadoPacienteFactura(Connection con, int codigoFactura, int estado)
    {
        return SqlBaseUtilidadesDao.cambiarEstadoPacienteFactura(con,codigoFactura,estado);
    }
    


    /**
     * @param con
     * @param codigoTipoDocumento
     * @param numeroDocumento
     * @param institucion
     * @return
     */
    public int obtenerCodigoFacturaPagosPaciente(Connection con, int codigoPagoPaciente)
    {
        return SqlBaseUtilidadesDao.obtenerCodigoFacturaPagosPaciente(con,codigoPagoPaciente);
    }
    
    
    /**
     * @param con
     * @param codigoPago
     * @param codigoEstado
     * @return
     */
    public boolean cambiarEstadoPagoFacturaPacient(Connection con, int codigoPago, int codigoEstado)
    {
        return SqlBaseUtilidadesDao.cambiarEstadoPagoFacturaPacient(con,codigoPago,codigoEstado);
    }
    

    /**
     * @param con
     * @param codigoPago
     * @param codigoEstado
     * @return
     */
    public boolean cambiarEstadoPagosGeneralEmpresa(Connection con, int codigoPago, int codigoEstado)
    {
        return SqlBaseUtilidadesDao.cambiarEstadoPagosGeneralEmpresa(con,codigoPago,codigoEstado);
    }
    

    /**
     * @param con
     * @param codigoPago
     * @return
     */
    public String obtenerEstadoPagosEmpresa(Connection con, int codigoPago)
    {
        return SqlBaseUtilidadesDao.obtenerEstadoPagosEmpresa(con,codigoPago);
    }
    
    
    /**
     * @param con
     * @param codigoTipoDocumento
     * @param numeroReciboCaja
     * @param institucion
     * @return
     */
    public int pacienteAbonoReciboCaja(Connection con, int codigoTipoDocumento, String numeroReciboCaja, int institucion)
    {
        return SqlBaseUtilidadesDao.pacienteAbonoReciboCaja(con,codigoTipoDocumento,numeroReciboCaja,institucion);
    }
    
    /**
     * @param con
     * @param loginUsuario
     * @return
     */
    public int numCajasAsociadasUsuario(Connection con, String loginUsuario)
    {
        return SqlBaseUtilidadesDao.numCajasAsociadasUsuario(con,loginUsuario);
    }
    
    /**
     * @param con
     * @param loginUsuario
     * @return
     */
    public int numCajasAsociadasUsuarioXcentroAtencion(Connection con, String loginUsuario, String codigoCentroAtencion)
    {
    	return SqlBaseUtilidadesDao.numCajasAsociadasUsuarioXcentroAtencion(con, loginUsuario, codigoCentroAtencion);
    }

    @Override
    public int numCajasAsociadasUsuarioXcentroAtencion(Connection con, String loginUsuario, String codigoCentroAtencion, int tipos[])
    {
    	return SqlBaseUtilidadesDao.numCajasAsociadasUsuarioXcentroAtencion(con, loginUsuario, codigoCentroAtencion, tipos);
    }

    /**
     * @param con
     * @param loginUsuario
     * @return
     */
    public ResultSetDecorator getConsecutivoCajaUsuario(Connection con, String loginUsuario, String codigoCentroAtencion)
    {
        return SqlBaseUtilidadesDao.getConsecutivoCajaUsuario(con,loginUsuario, codigoCentroAtencion);
    }
    

    /**
     * @param con
     * @param consecutivoCaja
     * @return
     */
    public int getCodigoCaja(Connection con, int consecutivoCaja)
    {
        return SqlBaseUtilidadesDao.getCodigoCaja(con,consecutivoCaja);
    }
    

    /**
     * @param con
     * @param consecutivoCaja
     * @return
     */
    public String getDescripcionCaja(Connection con, int consecutivoCaja)
    {
        return SqlBaseUtilidadesDao.getDescripcionCaja(con,consecutivoCaja);
    }
    
    /**
     * Mï¿½todo que verifica si una solicitud de cirugï¿½as tiene un pedido Qx.
     * asociado
     * @param con
     * @param numeroSolicitud
     * @return
     */
    public boolean tienePedidoSolicitudQx(Connection con,int numeroSolicitud)
    {
    	return SqlBaseUtilidadesDao.tienePedidoSolicitudQx(con,numeroSolicitud);
    }
    
    /**
     * Mï¿½todo que verifica si una Peticion Qx tiene solicitud asociada
     * @param con
     * @param numeroPedido
     * @return
     */
    public boolean tieneSolicitudPeticionQxStr(Connection con, int numeroPeticion)
    {
    	return SqlBaseUtilidadesDao.tieneSolicitudPeticionQxStr(con, numeroPeticion);
    }
    
    /**
     * Mï¿½todo que consulta la subcuenta de una solicitud
     * @param con
     * @param numeroSolicitud
     * @return
     */
    public int getSubCuentaSolicitud(Connection con,int numeroSolicitud)
    {
    	return SqlBaseUtilidadesDao.getSubCuentaSolicitud(con,numeroSolicitud);
    }
    
    /**
     * Mï¿½todo que consulta la cuenta de una solicitud
     * @param con
     * @param numeroSolicitud
     * @return
     */
    public int getCuentaSolicitud(Connection con,int numeroSolicitud)
    {
    	return SqlBaseUtilidadesDao.getCuentaSolicitud(con,numeroSolicitud);
    }
    
    /**
     * Mï¿½todo que consulta el nombre de una especialidad
     * @param con
     * @param especialidad
     * @return
     */
    public String getNombreEspecialidad(Connection con,int especialidad)
    {
    	return SqlBaseUtilidadesDao.getNombreEspecialidad(con,especialidad);
    }
    
    /**
     * Mï¿½todo que consulta el nombre de un pool
     * @param con
     * @param pool
     * @return
     */
    public String getNombrePool(Connection con,int pool)
    {
    	return SqlBaseUtilidadesDao.getNombrePool(con,pool);
    }
    
    /**
     * Mï¿½todo que retorna la peticiï¿½n de una solicitud
     * @param con
     * @param numeroSolicitud
     * @return
     */
    public int getPeticionSolicitudCx(Connection con,int numeroSolicitud)
    {
    	return SqlBaseUtilidadesDao.getPeticionSolicitudCx(con,numeroSolicitud);
    }
    
    /**
     * Mï¿½todo que consulta la fecha del cargo de una orden quirï¿½rgica
     * retorna una cadena vacï¿½a en el caso de que la orden no tenga cargo generado
     * @param con
     * @param numeroSolicitud
     * @return
     */
    public String getFechaCargoOrdenCirugia(Connection con,int numeroSolicitud)
    {
    	String orderBy = " ORDER BY codigo DESC "+ValoresPorDefecto.getValorLimit1()+" 1 ";
    	return SqlBaseUtilidadesDao.getFechaCargoOrdenCirugia(con,numeroSolicitud,orderBy);
    }
    
    /**
     * Mï¿½todo para saber si un convenio tiene (False - True) el atributo
	 * de info_adic_ingreso_convenios
     * @param con
     * @param codigoConvenio
     * @return
     */
	public boolean convenioTieneIngresoInfoAdic(Connection con, int codigoConvenio)
	{
		return SqlBaseUtilidadesDao.convenioTieneIngresoInfoAdic(con, codigoConvenio);
	}
	
	/**
	 * Mï¿½todo para obtener el saldo de un convenio dada su cuenta y el convenio
	 * @param con
	 * @param idSubCuenta
	 * @return
	 */
    public double obtenerSaldoConvenio(Connection con,String idSubCuenta)
    {
    	return SqlBaseUtilidadesDao.obtenerSaldoConvenio(con, idSubCuenta);
    }
    
    /**
     * Mï¿½todo para obtener el codigo de un convenio segun una cuenta dada
     * @param con
     * @param idCuenta
     * @return
     */
    public int obtenerCodigoConvenio(Connection con, int idCuenta)
    {
    	return SqlBaseUtilidadesDao.obtenerCodigoConvenio(con, idCuenta);
    }
    
    /**
     * Mï¿½todo para obtener la fecha de admision para un paciente de via de ingreso de urgencias
     * @param con
     * @param idCuenta
     * @return
     */
    public String obtenerFechaAdmisionUrg(Connection con, int idCuenta)
    {
    	return SqlBaseUtilidadesDao.obtenerFechaAdmisionUrg(con, idCuenta);
    }
 
    /**
     * Mï¿½todo para obtener la fecha de admision para un paciente de via de ingreso de hospitalizacion
     * @param con
     * @param idCuenta
     * @return
     */
    public String obtenerFechaAdmisionHosp(Connection con, int idCuenta)
    {
    	return SqlBaseUtilidadesDao.obtenerFechaAdmisionHosp(con, idCuenta);
    }
    
    /**
     * Mï¿½todo para obtener el tipo de anestesia de una solicitud de cirugï¿½as
     * @param con
     * @param numeroSolicitud
     * @return
     */
	public InfoDatosInt obtenerTipoAnestesia(Connection con, int numeroSolicitud)
	{
		return SqlBaseUtilidadesDao.obtenerTipoAnestesia(con, numeroSolicitud);
	}
	
	 /**
	 * Mï¿½todo que carga la hora de la base de datos para no utilizar la del sistema
	 * @param con Conexiï¿½n con la BD
	 * @return String con la Hora del sistema cinco caracteres
	 */
	public String capturarHoraSegundosBD(Connection con)
	{
		String consultaHoraSegundosStr="SELECT to_char(timestamp 'now','HH24:MI:SS') AS hora";
		return SqlBaseUtilidadesDao.capturarHoraBD(con, consultaHoraSegundosStr);
	}
	
	/**
	 * Mï¿½todo para obtener el nombre de un grupo de servicio dado su codigo
	 * @param con
	 * @param codigoGrupo
	 * @return
	 */
	public String getNombreGrupoServicios(Connection con, int codigoGrupo)
    {
		return SqlBaseUtilidadesDao.getNombreGrupoServicios(con, codigoGrupo);
    }
	
	/**
	 * Mï¿½todo para obtener el total de un presupuesto dado
	 * @param con
	 * @param codigoPresupuesto
	 * @return
	 */
	public double obtenerTotalPresupuesto(Connection con, int codigoPresupuesto)
    {
		return SqlBaseUtilidadesDao.obtenerTotalPresupuesto(con, codigoPresupuesto);
    }
	
	/**
	 * Mï¿½todo para obtener el total de un grupo de servicios dado, relacionado
	 * con un presupuesto de servicios
	 * @param con
	 * @param presupuesto
	 * @param grupo
	 * @return
	 */
	public double obtenertotalGrupoServicios(Connection con, int presupuesto, int grupo)
	{
		return SqlBaseUtilidadesDao.obtenertotalGrupoServicios(con, presupuesto, grupo);
	}
	
	/**
	 * carga el esuqema tarifario de una solicitud de cx
	 * @param con
	 * @param codigo
	 * @return
	 */
	public int getEsquemaTarifarioSolCx(Connection con, String codigo)
	{
		return SqlBaseUtilidadesDao.getEsquemaTarifarioSolCx(con, codigo);
	}
	
		
	/**
	 * Mï¿½todo para obtener el total de los articulos de un presupuesto
	 * @param con
	 * @param presupuesto
	 * @return
	 */
	public double obtenerTotalArticulosPresupuesto(Connection con, int presupuesto)
	{
		return SqlBaseUtilidadesDao.obtenerTotalArticulosPresupuesto(con, presupuesto);
	}
    
    
    /**
     * 
     * @param con
     * @param numeroSolicitud
     * @return
     */
    public int obtenerNumeroOrdenSolicitud(Connection con, int numeroSolicitud)
    {
        return SqlBaseUtilidadesDao.obtenerNumeroOrdenSolicitud(con,numeroSolicitud);
    }
    
    /**
     * 
     * @param con
     * @param codDetFactura
     * @return
     */
    public int numAsociosCirugiaDetFactura(Connection con, int codDetFactura)
    {
        return SqlBaseUtilidadesDao.numAsociosCirugiaDetFactura(con,codDetFactura);
    }
    
    /**
	 * Mï¿½todo para obtener el total de un grupo de servicios
	 * @param con
	 * @param presupuesto
	 * @param grupo
	 * @return
	 */
	public int obtenerTotalGrupoArticulos(Connection con, int presupuesto, int grupo)
	{
		return SqlBaseUtilidadesDao.obtenerTotalGrupoArticulos(con, presupuesto, grupo);
	}
	/**
	 * Mï¿½todo para obtener el total de un subgrupo de articulos
	 * @param con
	 * @param presupuesto
	 * @param subgrupo
	 * @return
	 */
	public int obtenerTotalSubGrupoArticulos(Connection con, int presupuesto, int subgrupo)
	{
		return SqlBaseUtilidadesDao.obtenerTotalSubGrupoArticulos(con, presupuesto, subgrupo);
	}
	
	/**
	 * Mï¿½todo para obtener el total de los articulos de  un presupuesto dado asociado por calse de inventario
	 * @param con
	 * @param presupuesto
	 * @param claseinventario
	 * @return
	 */
	public int obtenerTotalClaseInventarioArticulos(Connection con, int presupuesto, int claseinventario)
	{
		return SqlBaseUtilidadesDao.obtenerTotalClaseInventarioArticulos(con, presupuesto, claseinventario);
	}

	/**
	 * Mï¿½todo que me indica si una cuenta tiene subcuentas
	 * independiente del estado de la misma
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public boolean haySubcuentasParaCuentaDada(Connection con, int codigoCuenta)
	{
		return SqlBaseUtilidadesDao.haySubcuentasParaCuentaDada(con, codigoCuenta);
	}

	/**
     * Mï¿½todo que consulta la fecha de apertura de la cuenta de un paciente dado
     * @param con
     * @param numeroSolicitud
     * @return
     */
    public String getFechaAperturaCuenta(Connection con,int numeroCuenta)
    {
		return SqlBaseUtilidadesDao.getFechaAperturaCuenta(con, numeroCuenta);
    }

	/**
	 * Metodo para consultar la fecha de solicitud con el numero de la solicitud  
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 **/
	public String getFechaSolicitud(Connection con, int numeroSolicitud)
	{
		return SqlBaseUtilidadesDao.getFechaSolicitud(con,numeroSolicitud);
	}

	/**
	 * Mï¿½todo para obtener el consecutivo de ordenes medicas dado su numero de solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public int getConsecutivoOrdenesMedicas(Connection con, int numeroSolicitud)
	{
		return SqlBaseUtilidadesDao.getConsecutivoOrdenesMedicas(con, numeroSolicitud);
	}
	/**
	 * Mï¿½todo para obtener las observaciones de la informacion adicional de un paciente cargado en session
	 * @param con
	 * @param codPaciente
	 * @return
	 */
	public String getObservacionesPacienteActivo(Connection con, int codPaciente)
	{
		return SqlBaseUtilidadesDao.getObservacionesPacienteActivo(con, codPaciente);
	}
	
	/**
	 * obtener el codigo de la cuenta asociada dado el codigo de la cuenta final
	 * retorna "" en caso de que no exista
	 */
	public String getCuentaAsociada(Connection con, String codigoCuentaFinal,boolean activo)
	{
		return SqlBaseUtilidadesDao.getCuentaAsociada(con, codigoCuentaFinal,activo);
	}
	
	/**
     * Mï¿½todo para obtener la hora de admision para un paciente de via de ingreso de hospitalizacion
     * @param con
     * @param idCuenta
     * @return
     */
    public  String obtenerHoraAdmisionHosp(Connection con, int idCuenta)
    {
    	return SqlBaseUtilidadesDao.obtenerHoraAdmisionHosp(con, idCuenta);
    }
    
    /**
     * Mï¿½todo para obtener la hora de admision para un paciente de via de ingreso de urgencias
     * @param con
     * @param idCuenta
     * @return
     */
    public String obtenerHoraAdmisionUrg(Connection con, int idCuenta)
    {
    	return SqlBaseUtilidadesDao.obtenerHoraAdmisionUrg(con, idCuenta);
    }
    
    /**
     * Metodo que retorna el nombre del responsable de una factura.
     * @param con
     * @param codFactura
     * @return
     */
	public String getResponsableFactura(Connection con, int codFactura)
	{
		return SqlBaseUtilidadesDao.getResponsableFactura(con,codFactura);
	}
	

	/**
	 * 
	 */
	public double obtenerValorTotalReciboCaja(Connection con, int codigoInstitucionInt, String numReciboCaja)
	{
		return SqlBaseUtilidadesDao.obtenerValorTotalReciboCaja(con,codigoInstitucionInt,numReciboCaja);
	}
	
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param numReciboCaja
	 * @return
	 */
	public String reciboCajaRecibidoDe(Connection con, int institucion, String numReciboCaja)
	{
		return SqlBaseUtilidadesDao.reciboCajaRecibidoDe(con,institucion,numReciboCaja);
	}
	
	/**
	 * Metodo que retorna la informacion basica de la anulacion de una factura.
	 * codigoFactura+separadorSplit+fechaanulacion+separadorSplit+horaanulacion+separadorSplit+usuarioanula+separadorSplit+consecutivoanulacion;
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public String obtenerInformacionAnulacionFactura(Connection con, String codigoFactura)
	{
		return SqlBaseUtilidadesDao.obtenerInformacionAnulacionFactura(con,codigoFactura);
	}
	
	

	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public int obtenerCodigoEstadoFacturacionFactura(Connection con, int codigoFactura)
	{
		return SqlBaseUtilidadesDao.obtenerCodigoEstadoFacturacionFactura(con,codigoFactura);
	}

	
	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public int obtenerCodigoEstadoPacienteFactura(Connection con, int codigoFactura)
	{
		return SqlBaseUtilidadesDao.obtenerCodigoEstadoPacienteFactura(con,codigoFactura);
	}
	
	
	/**
	 * obtiene el total admin farmacia
	 * @param con
	 * @param codigoArticuloStr
	 * @param numeroSolicitudStr
	 * @param traidoUsuario
	 * @return
	 */
	public int getTotalAdminFarmacia(Connection con, String codigoArticuloStr, String numeroSolicitudStr, boolean traidoUsuario)
	{
		return SqlBaseUtilidadesDao.getTotalAdminFarmacia(con, codigoArticuloStr, numeroSolicitudStr, traidoUsuario, "");
	}
	
	/**
	 * Mï¿½todo que devuelve el nombre del ï¿½ltimo tipo de monitoreo del paciente cuando estï¿½ en Cama UCI,
	 * ya sea de la orden mï¿½dica o la evoluciï¿½n
	 * @param con
	 * @param codigoCuenta
	 * @param institucion
	 * @return nombre del ï¿½ltimo tipo de monitoreo
	 */
	public String obtenerUltimoTipoMonitoreo (Connection con, int codigoCuenta, int institucion, boolean obtenerNombre)
	{
		return SqlBaseUtilidadesDao.obtenerUltimoTipoMonitoreo (con, codigoCuenta, institucion, obtenerNombre);
	}
	
	/**
	 * Mï¿½todo implementado para obtener el codigo de la cama
	 * del ï¿½ltimo traslado de una cuenta de Hospitalizacion
	 * @param con
	 * @param cuenta
	 * @param adicion
	 * @return
	 */
	public int getUltimaCamaTraslado(Connection con,int cuenta)
	{
		/**MT 4452 Diana Ruiz*/
		//String adicion = " ORDER BY codigo DESC "+ValoresPorDefecto.getValorLimit1()+" 1";		
		String consulta= "SELECT codigo_nueva_cama AS cama FROM traslado_cama WHERE cuenta = ? ORDER BY fecha_grabacion desc,hora_grabacion desc limit 1";
		return SqlBaseUtilidadesDao.getUltimaCamaTraslado(con,cuenta,consulta);
	}
	
	/**
	 * Metodo para obtener el codigo de la ocupacion Medica dado el codigo del medico.
	 * Retorna -1 si no encuentra la ocupacion con el codigo del medico dado.
	 * @param con
	 * @param codigoMedico
	 * @return
	 */
    public int obtenerOcupacionMedica(Connection con, int codigoMedico)
    {
    	return SqlBaseUtilidadesDao.obtenerOcupacionMedica(con, codigoMedico);
    }
    
    /**
     * 
     */
    public int obtenerCodigoEstadoCama(Connection con, int codigoCama)
    {
    	return SqlBaseUtilidadesDao.obtenerCodigoEstadoCama(con,codigoCama);
    }
    
    /**
     * Metodo que retorna el numero de registros de la consulta.
     * @param con
     * @param Sentencia
     * @return
     */
	public int nroRegistrosConsulta(Connection con, String consulta)
	{
		return SqlBaseUtilidadesDao.nroRegistrosConsulta(con,consulta);
	}	
	
	/**
	 * Mï¿½todo que consulta la fecha de egreso de una cuenta
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public String getFechaEgreso(Connection con,int idCuenta)
	{
		return SqlBaseUtilidadesDao.getFechaEgreso(con,idCuenta);
	}
	
	/**
	 * Mï¿½todo implementado para consultar la descripcion completa
	 * del diagnï¿½stico de egreso de una cuenta
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public String getNombreDiagnosticoEgreso(Connection con,int idCuenta)
	{
		return SqlBaseUtilidadesDao.getNombreDiagnosticoEgreso(con,idCuenta);
	}
	
	/**
	 * Mï¿½todo implementado para consultar la descripcion compelta
	 * del diagnï¿½stico de ingreso de un admision hospitalaria
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public String getNombreDiagnosticoIngreso(Connection con,int idCuenta)
	{
		return SqlBaseUtilidadesDao.getNombreDiagnosticoIngreso(con,idCuenta);
	}
	
	/**
	 * Mï¿½todo para saber si un tipo de rompimiento parametrizado en un formato de impresion de factura
	 * tien true - false en su atributo de imprimir detalle
	 * @param con
	 * @param tipoRompimiento
	 * @param codigoFormato
	 * @return
	 */
	public boolean getImprimirDetalleTipoRompimientoServ(Connection con, int tipoRompimiento, int codigoFormato)
	{
		return SqlBaseUtilidadesDao.getImprimirDetalleTipoRompimientoServ(con, tipoRompimiento, codigoFormato);
	}
	
	/**
	 * Mï¿½todo para saber si un tipo de rompimiento de articulos parametrizado en un formato de impresion de factura
	 * tien true - false en su atributo de imprimir detalle
	 * @param con
	 * @param tipoRompimiento
	 * @param codigoFormato
	 * @return
	 */
	public boolean getImprimirDetalleTipoRompimientoArt(Connection con, int tipoRompimiento, int codigoFormato)
	{
		return SqlBaseUtilidadesDao.getImprimirDetalleTipoRompimientoArt(con, tipoRompimiento, codigoFormato);
	}
	
	/**
	 * Mï¿½todo para saber si un tipo de rompimiento de articulos parametrizado en un formato de impresion de factura
	 * tiene true - false en su atributo de imprimir sub total
	 * @param con
	 * @param tipoRompimiento
	 * @param codigoFormato
	 * @return
	 */
	public boolean getImprimirSubTotalTipoRompimientoArt(Connection con, int tipoRompimiento, int codigoFormato)
	{
		return SqlBaseUtilidadesDao.getImprimirSubTotalTipoRompimientoArt(con, tipoRompimiento, codigoFormato);
	}
	
	/**
	 * Mï¿½todo para saber si un tipo de rompimiento de servicios parametrizado en un formato de impresion de factura
	 * tiene true - false en su atributo de imprimir sub total
	 * @param con
	 * @param tipoRompimiento
	 * @param codigoFormato
	 * @return
	 */
	public boolean getImprimirSubTotalTipoRompimientoServ(Connection con, int tipoRompimiento, int codigoFormato)
	{
		return SqlBaseUtilidadesDao.getImprimirSubTotalTipoRompimientoServ(con, tipoRompimiento, codigoFormato);
	}
	
	/**
	 * Mï¿½todo para actualizar en la agenda el medico que atiende una cita 
	 * @param con
	 * @param codigoMedico
	 * @param codigoAgenda
	 * @return
	 */
	public boolean actualizarMedicoAgenda(Connection con, int codigoMedico, int codigoAgenda)
	{
		return SqlBaseUtilidadesDao.actualizarMedicoAgenda(con, codigoMedico, codigoAgenda);
	}
	
	/**
	 * Metodo para saber si un centro de costo esta asociado a un usuairo x alamacen
	 * @param con
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public boolean centroCostoAsociadoUsuarioXAlmacen(Connection con, int centroCosto, int institucion)
	{
		return SqlBaseUtilidadesDao.centroCostoAsociadoUsuarioXAlmacen(con, centroCosto, institucion);
	}

	/**
	 * Mï¿½todo para saber si existe un centro de costo en la tabla centros_costo
	 * @param con
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public boolean existeCentroCosto(Connection con, int centroCosto, int institucion)
	{
		return SqlBaseUtilidadesDao.existeCentroCosto(con, centroCosto, institucion);
	}
	
	/**
	 * Mï¿½todo para saber si un centro de costo esta asociado a algun usuario
	 * @param con
	 * @param centroCosto
	 * @return
	 */
	public boolean centroCostoAsociadoUsuario(Connection con, int centroCosto)
	{
		return SqlBaseUtilidadesDao.centroCostoAsociadoUsuario(con, centroCosto);
	}
	
	/**
	 * Mï¿½todo usado para obtener el nombre de un centro de atenciï¿½n,
	 * de acuerdo al consecutivo enviado como parï¿½metro
	 * @param con
	 * @param consecutivoCentroAtencion
	 * @return devuelve cadena vacï¿½a o null si no encuentra el nombre
	 */
	public String obtenerNombreCentroAtencion(Connection con, int consecutivoCentroAtencion)
	{
		return SqlBaseUtilidadesDao.obtenerNombreCentroAtencion (con, consecutivoCentroAtencion);
	}
	
	/**
	 * Mï¿½todo para obtener el nombre de un centro de costo dado su codigo para una
	 * institucion determinada
	 * @param con
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public String obtenerNombreCentroCosto(Connection con, int centroCosto, int institucion)
	{
		return SqlBaseUtilidadesDao.obtenerNombreCentroCosto(con, centroCosto, institucion);
	}
	
	/**
	 * Mï¿½todo para obtener el nombre de una via de ingreso dado su codigo
	 * @param con
	 * @param codigo
	 * @return
	 */
	public String obtenerNombreViaIngreso(Connection con, int codigo)
	{
		return SqlBaseUtilidadesDao.obtenerNombreViaIngreso(con, codigo);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoArea
	 * @return
	 */
	public int obtenerCentroAtencionPaciente(Connection con, int codigoArea)
	{
		return SqlBaseUtilidadesDao.obtenerCentroAtencionPaciente(con,codigoArea);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoCuenta
	 * @param codigoCentroCostoTratante
	 * @return
	 */
	public boolean actualizarAreaCuenta(Connection con, int codigoCuenta, int codigoCentroCostoTratante)
	{
		return SqlBaseUtilidadesDao.actualizarAreaCuenta(con,codigoCuenta,codigoCentroCostoTratante);
	}
	
	/**
	 * Mï¿½todo que consulta los centros de costo de una vï¿½a de ingreso
	 * en un centro de atenciï¿½n especï¿½fico
	 * @param con
	 * @param viaIngreso
	 * @param centroAtencion
	 * @param institucion
	 * @return
	 */
	public HashMap getCentrosCostoXViaIngresoXCAtencion(Connection con,int viaIngreso,int centroAtencion,int institucion, String tipoPaciente)
	{
		return SqlBaseUtilidadesDao.getCentrosCostoXViaIngresoXCAtencion(con,viaIngreso,centroAtencion,institucion,tipoPaciente);
	}
	
	/**
	 * Metodo que consulta el nombre del convenio dado el codigo del convenio
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public String obtenerNombreConvenioOriginal(Connection con, int codigoConvenio)
	{
		return SqlBaseUtilidadesDao.obtenerNombreConvenioOriginal(con, codigoConvenio);
	}


	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public String obtenerLLaveTraigeAdmisionUrgeciasXCuenta(Connection con, int idCuenta)
	{
		return SqlBaseUtilidadesDao.obtenerLLaveTraigeAdmisionUrgeciasXCuenta(con,idCuenta);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param numOrdenDieta
	 * @param codigoArticulo
	 * @param cantidad
	 * @return
	 */
	public boolean actualizarCantidadArticuloSolMedicamentos(Connection con, String numSolicitud, String codigoArticulo, String cantidad)
	{
		return SqlBaseUtilidadesDao.actualizarCantidadArticuloSolMedicamentos(con,numSolicitud,codigoArticulo,cantidad);
	}
	
	/**
	 * Metodo para saber si una orden de dieta esta finalizada. 
	 * como la finalizacion genera otro registro en orden_dieta,  
	 * entonces buscar los registros siguientes a este.  
	 * @param con
	 * @param codigoOrdenDieta
	 */
	public boolean getOrdenDietaFinaliza(Connection con, String codigoOrdenDieta, int codigoCuenta, int codigoCuentaAsocio)
	{
		return SqlBaseUtilidadesDao.getOrdenDietaFinaliza(con,codigoOrdenDieta, codigoCuenta, codigoCuentaAsocio);
	}
	
	/**
	 * Mï¿½todo para obtener el valor inicial de la cuenta de cobro
	 * @param con
	 * @param codigoCuentaCobro
	 * @param codigoInstitucion
	 * @return
	 */
	public double obtenerValorInicialCuentaCobroCapitacion(Connection con, int codigoCuentaCobro, int codigoInstitucion)
	{
		return SqlBaseUtilidadesDao.obtenerValorInicialCuentaCobroCapitacion (con, codigoCuentaCobro, codigoInstitucion);
	}
	
	/**
	 * Metodo apra saber si como tiene parametrizado un formato de impresion 
	 * determinado el campo que indica si se deben mostrar los servicios qx o no 
	 * @param con
	 * @param codigoFormato
	 * @return
	 */
	public boolean getMostrarServiciosQx(Connection con, int codigoFormato)
	{
		return SqlBaseUtilidadesDao.getMostrarServiciosQx(con, codigoFormato);
	}
	
	/**
	 * Utilidad para obtener los datos de los campos parametrizables
	 * @param con
	 * @param medico
	 * @param funcionalidad
	 * @param seccion
	 * @param centroCosto
	 * @param institucion
	 * @param codigoTabla
	 * @return
	 */
	public HashMap<Object, Object> obtenerInformacionParametrizada(Connection con, int medico, String funcionalidad, String seccion, int centroCosto, String institucion, int codigoTabla)
	{
		return SqlBaseUtilidadesDao.obtenerInformacionParametrizada(con, medico, funcionalidad, seccion, centroCosto, institucion, codigoTabla);
	}
	
	/**
	 * Mï¿½todo para obtener el nï¿½mero de la ï¿½ltima cuenta de cobro
	 * de capitaciï¿½n
	 * @param con -> Connection
	 * @param codigoInstitucion
	 * @return ultimaCuentaCobro
	 */
	public int obtenerUltimaCuentaCobroCapitacion(Connection con, int codigoInstitucion)
	{
		return SqlBaseUtilidadesDao.obtenerUltimaCuentaCobroCapitacion (con, codigoInstitucion);
	}
	
	/**
	 * Mï¿½todo para obtener el codigo del registro de enfermeria para una cuenta determinada
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public int obtenerCodigoRegistroEnfermeria(Connection con, int idCuenta)
	{
		return SqlBaseUtilidadesDao.obtenerCodigoRegistroEnfermeria(con, idCuenta);
	}

	
	
	/**
	 * 
	 * @param con
	 * @param cuenta
	 * @param contrato 
	 * @return
	 */
	public boolean esNivelServicioContratado(Connection con, String servicio, int contrato)
	{
		boolean respuesta=false;
		PreparedStatementDecorator ps=null;
		try {
			String cadena="select getesnivelservcontratado("+contrato+","+servicio+") as resultado";
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				respuesta=rs.getBoolean("resultado");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return respuesta;
	}
	
	/**
	 * Mï¿½todo usado para obtener el nombre de un tipo de calificaciï¿½n
	 * pyp de acuerdo al consecutivo enviado como parï¿½metro
	 * @param con
	 * @param consecutivo
	 * @return devuelve cadena vacï¿½a o null si no encuentra el nombre
	 */
	public String obtenerNombreTipoCalificacionPyP(Connection con, int consecutivo)
	{
		return SqlBaseUtilidadesDao.obtenerNombreTipoCalificacionPyP (con, consecutivo);
	}
	
	/**
	 * Mï¿½todo usado para obtener el nombre de un tipo de tipo de transacciï¿½n de inventario
	 * @param con
	 * @param consecutivo
	 * @return devuelve cadena vacï¿½a o null si no encuentra el nombre
	 */
	public String obtenerNombreTransaccionInventario(Connection con,int consecutivo)
	{
		return SqlBaseUtilidadesDao.obtenerNombreTransaccionInventario (con, consecutivo);
	}
	
	/**
	 * Mï¿½todo implementado para consultar el nombre de una ocupaciï¿½n mï¿½dica
	 * @param con
	 * @param codigoOcupacion
	 * @return
	 */
	public String obtenerNombreOcupacionMedica(Connection con,int codigoOcupacion)
	{
		return SqlBaseUtilidadesDao.obtenerNombreOcupacionMedica(con,codigoOcupacion);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoSubCuenta
	 * @param totalSubCuenta
	 * @return
	 */
	public boolean actualizarTotalSubCuenta(Connection con, int codigoSubCuenta, double totalSubCuenta)
	{
		return SqlBaseUtilidadesDao.actualizarTotalSubCuenta(con,codigoSubCuenta,totalSubCuenta);
	}
	

	/**
	 * 
	 * @param con
	 * @param servicio
	 * @return
	 */
	public String obtenerTipoServicio(Connection con, String servicio)
	{
		return SqlBaseUtilidadesDao.obtenerTipoServicio(con,servicio);
	}
	/**
	 * 
	 * @param con
	 * @param servicio
	 * @param codigoTarifarioCups
	 * @return
	 */
	public String obtenerCodigoPropietarioServicio(Connection con, String servicio, int codigoTarifario)
	{
		return SqlBaseUtilidadesDao.obtenerCodigoPropietarioServicio(con,servicio,codigoTarifario);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public String obtenerEstadoHistoriaClinicaSolicitud(Connection con, String numeroSolicitud)
	{
		return SqlBaseUtilidadesDao.obtenerEstadoHistoriaClinicaSolicitud(con,numeroSolicitud);
	}
	

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public String obtenerTipoSolicitud(Connection con, String numeroSolicitud)
	{
		return SqlBaseUtilidadesDao.obtenerTipoSolicitud(con,numeroSolicitud);
	}
	
	/**
	 * 
	 * @param con
	 * @param grEtareo
	 * @return
	 */
	public String[] obtenerRango_UnidadMedidaGrupoEtareoPYP(Connection con, String grEtareo)
	{
		return SqlBaseUtilidadesDao.obtenerRango_UnidadMedidaGrupoEtareoPYP(con,grEtareo);
	}
	

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean esSolicitudPYP(Connection con, int numeroSolicitud)
	{
		return SqlBaseUtilidadesDao.esSolicitudPYP(con,numeroSolicitud);
	}
	

	/**
	 * 
	 * @param con
	 * @param string
	 * @param i
	 * @return
	 */
	public String obtenerCodigoOrdenAmbulatoria(Connection con, String numeroOrden, int institucion) 
	{
		return SqlBaseUtilidadesDao.obtenerCodigoOrdenAmbulatoria(con,numeroOrden,institucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoOrden
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean asignarSolicitudToActividadPYP(Connection con, String codigoOrden, String numeroSolicitud)
	{
		return SqlBaseUtilidadesDao.asignarSolicitudToActividadPYP(con,codigoOrden,numeroSolicitud);
	}
	
	/**
	 * M+etodo que verifica si el convenio de una solicitud tiene
	 * activo el campo de unificar PYP
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean tieneConvenioUnificarPYP(Connection con,String numeroSolicitud)
	{
		return SqlBaseUtilidadesDao.tieneConvenioUnificarPYP(con,numeroSolicitud);
	}
	
	/**
	 * 
	 * @param con
	 * @param ordenAmbulatoria
	 * @return
	 */
	public HashMap consultarDetalleOrdenAmbulatoriaArticulos(Connection con, String ordenAmbulatoria)
	{
		return SqlBaseUtilidadesDao.consultarDetalleOrdenAmbulatoriaArticulos(con,ordenAmbulatoria);
	}
	
	/**
	 * Mï¿½todo implementado para actualizar el acumulado de PYP por solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @param centroAtencion
	 * @return
	 */
	public int actualizarAcumuladoPYP(Connection con,String numeroSolicitud,String centroAtencion)
	{
		String secuencia = "nextval('seq_acum_act_prog_pac_pyp')";
		return SqlBaseUtilidadesDao.actualizarAcumuladoPYP(con,numeroSolicitud,centroAtencion,secuencia);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public String obtenerCodigoActividadProgramaPypPacienteDadaSolicitud(Connection con, int numeroSolicitud)
	{
		return SqlBaseUtilidadesDao.obtenerCodigoActividadProgramaPypPacienteDadaSolicitud(con,numeroSolicitud);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoActividad
	 * @param estado
	 * @param loginUsuario
	 * @param comentario
	 * @return
	 */
	public boolean actualizarEstadoActividadProgramaPypPaciente(Connection con, String codigoActividad, String estado, String loginUsuario, String comentario)
	{
		return SqlBaseUtilidadesDao.actualizarEstadoActividadProgramaPypPaciente(con,codigoActividad,estado,loginUsuario,comentario);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public String obtenerCodigoOrdenAmbulatoriaActividad(Connection con, String numeroSolicitud)
	{
		return SqlBaseUtilidadesDao.obtenerCodigoOrdenAmbulatoriaActividad(con,numeroSolicitud);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public String obtenerCodigoOrdenAmbulatoria(Connection con, String numeroSolicitud)
	{
		return SqlBaseUtilidadesDao.obtenerCodigoOrdenAmbulatoria(con,numeroSolicitud);
	}
	
	/**
	 * Mï¿½todo que verifica si en Actividades por Programa estï¿½
	 * definida como requerida 
	 * @param con
	 * @param codigoActividadProgPypPac
	 * @return
	 */
	public String esRequeridoActividadesXPrograma(Connection con, String codigoActividadProgPypPac)
	{
		return SqlBaseUtilidadesDao.esRequeridoActividadesXPrograma(con, codigoActividadProgPypPac);
	}
	
	/**
	 * Mï¿½todo que consulta si existen programas y actividades por convenio, y
	 * verifica si estï¿½n activas 
	 * @param con
	 * @param actividadProgramaPyp
	 * @return true si hay actividad(es) en true para la actividad de programa pyp
	 */
	public boolean activaProgramaActividadesConvenio (Connection con, String actividadProgramaPyp,String convenio)
	{
		return SqlBaseUtilidadesDao.activaProgramaActividadesConvenio (con, actividadProgramaPyp,convenio);
	}
	
	/**
	 * 
	 * @param con
	 * @param grEtareo
	 * @return
	 */
	public String obtenerSexoGrupoEtareoPYP(Connection con, String grEtareo)
	{
		return SqlBaseUtilidadesDao.obtenerSexoGrupoEtareoPYP(con,grEtareo);
	}
	
	/**
	 * Mï¿½todo usado para obtener el nombre de una cuenta contable
	 * de acuerdo al codigo enviado como parï¿½metro
	 * @param con
	 * @param codigo
	 * @return devuelve cadena vacï¿½a o null si no encuentra el nombre
	 */
	public String obtenerNombreCuentaContable(Connection con,int codigo)
	{
		return SqlBaseUtilidadesDao.obtenerNombreCuentaContable(con, codigo);
	}

	/**
	 * metodo que obtiene el nombre de la finalidad del servicio dado el codigo de la finalidad
	 * @param con
	 * @param codigoFinalidad
	 * @return
	 */
	public String getNombreFinalidadServicio(Connection con, String codigoFinalidad)
	{
		return SqlBaseUtilidadesDao.getNombreFinalidadServicio(con, codigoFinalidad);
	}
	
	/**
	 * metodo que obtiene tipo del servicio dado el codigo del servicio
	 * @param con
	 * @param codigoServicio
	 * @return
	 */
	public String getTipoServicio(Connection con, String codigoServicio)
	{
		return SqlBaseUtilidadesDao.getTipoServicio(con, codigoServicio);
	}
	
	/**
	 * 
	 * @param codigoConvenio
	 * @return
	 */
	public double obtenerPorcentajePypContratoConvenio(Connection con,String codigoConvenio)
	{
		return SqlBaseUtilidadesDao.obtenerPorcentajePypContratoConvenio(con,codigoConvenio);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public String obtenerFrecuenciaTipoFrecSolPoc(Connection con, int numeroSolicitud)
	{
		return SqlBaseUtilidadesDao.obtenerFrecuenciaTipoFrecSolPoc(con,numeroSolicitud);
	}
	

	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public int obtenerViaIngresoFactura(Connection con, String codigoFactura)
	{
		return SqlBaseUtilidadesDao.obtenerViaIngresoFactura(con,codigoFactura);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public String obtenerCuentaFactura(Connection con, String codigoFactura)
	{
		return SqlBaseUtilidadesDao.obtenerCuentaFactura(con,codigoFactura);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoPaciente
	 * @param codigoConvenio
	 * @return
	 */
	public boolean esActividadSolicitudCubiertaConvenio(Connection con, String numeroSolicitud, String codigoPaciente, String codigoConvenio)
	{
		return SqlBaseUtilidadesDao.esActividadSolicitudCubiertaConvenio(con,numeroSolicitud,codigoPaciente,codigoConvenio);
	}
	
	/**
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public String esSolicitudFacturada(Connection con, String numeroSolicitud)
	{
		return SqlBaseUtilidadesDao.esSolicitudFacturada(con,numeroSolicitud);
	}
	
	/**
	 * Mï¿½todo que verifica que el paciente no tenga cuentas abiertas en otros centros de atencion
	 * y en el caso de que la encuentre retorna la descripcion de su centro de atencion asociado
	 * @param con
	 * @param codigoPaciente
	 * @param institucion
	 * @param centroAtencion
	 * @return
	 */
	public String getCentroAtencionIngresoAbiertoPaciente(Connection con,String codigoPaciente,String institucion,String centroAtencion)
	{
		return SqlBaseUtilidadesDao.getCentroAtencionIngresoAbiertoPaciente(con,codigoPaciente,institucion,centroAtencion);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public String[] obtenerCentroAtencionFactura(Connection con, int codigoFactura)
	{
		return SqlBaseUtilidadesDao.obtenerCentroAtencionFactura(con,codigoFactura);
	}
	
	/**
	 * Mï¿½todo usado para obtener el nombre del usuario dado el login y el parï¿½metro primeroApellidos
	 * para indicar como se quiere el orden primero los nombres o los apellidos
	 * @param con
	 * @param login
	 * @param primeroApellidos
	 * @return devuelve cadena vacï¿½a o null si no encuentra el nombre
	 */
	public String obtenerNombreUsuarioXLogin(Connection con, String login, boolean primeroApellidos)
	{
String consultaStr;
		
		if(primeroApellidos)
		   consultaStr="SELECT getnombreusuario2(?) AS nombre_usuario ";
	      else
		   consultaStr="SELECT getnombreusuario(?) AS nombre_usuario ";
	
		return SqlBaseUtilidadesDao.obtenerNombreUsuarioXLogin (con, login, primeroApellidos,consultaStr);
	}
	
	/**
	 * Mï¿½todo implementado para consultar el centro de atencion de la ultima cuenta
	 * activa del paciente
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public String getNomCentroAtencionIngresoAbierto(Connection con,String codigoPaciente)
	{
		return SqlBaseUtilidadesDao.getNomCentroAtencionIngresoAbierto(con,codigoPaciente);
	}
	
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param programa
	 * @param actividad
	 * @return
	 */
	public String obtenerCodigoActividadPorPrograma(Connection con, int institucion, String programa, String actividad)
	{
		return SqlBaseUtilidadesDao.obtenerCodigoActividadPorPrograma(con,institucion,programa,actividad);
	}
	
	/**
	 * Mï¿½todo que verifica si una solicitud estï¿½ asociada a una cita
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean existeCitaParaSolicitud(Connection con,String numeroSolicitud)
	{
		return SqlBaseUtilidadesDao.existeCitaParaSolicitud(con,numeroSolicitud);
	}
	
	/**
	 * 
	 * @param con
	 * @param ordenAmbulatoria
	 * @return
	 */
	public String obtenerCodigoActividadDadaOrdenAmbulatoria(Connection con, String ordenAmbulatoria)
	{
		return SqlBaseUtilidadesDao.obtenerCodigoActividadDadaOrdenAmbulatoria(con,ordenAmbulatoria);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public String obtenerCodigoConvenioSolicitud(Connection con, String numeroSolicitud)
	{
		return SqlBaseUtilidadesDao.obtenerCodigoConvenioSolicitud(con,numeroSolicitud);
	}
	
	/**
	 * 
	 */
	public HashMap obtenerEspecialidades(Connection con,HashMap parametros)
	{
		return SqlBaseUtilidadesDao.obtenerEspecialidades(con,parametros);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoEspecilidad
	 * @return
	 */
	public HashMap obtenerMedicosEspecialidad(Connection con, String codigoEspecilidad)
	{
		return SqlBaseUtilidadesDao.obtenerMedicosEspecialidad(con,codigoEspecilidad);
	}

	
	
	
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap obtenerUnidadesConsulta(Connection con)
	{
		return SqlBaseUtilidadesDao.obtenerUnidadesConsulta(con);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoEspecialidad
	 * @return
	 */
	public  HashMap obtenerMedicosUnidadConsulta(Connection con, String codigoUnidadConsulta)
	{
		return SqlBaseUtilidadesDao.obtenerMedicosUnidadConsulta(con, codigoUnidadConsulta);
	}
	

	/**
	 * 
	 * @param con
	 * @param codigoUnidadConsulta
	 * @return
	 */
	public HashMap obtenerAgendaDisponibleMedicoUnidadConsulta(Connection con, String codigoUnidadConsulta,String codigoMedico)
	{
		return SqlBaseUtilidadesDao.obtenerAgendaDisponibleMedicoUnidadConsulta(con, codigoUnidadConsulta,codigoMedico);
	}
	
	/**
	 * 
	 * @param con
	 * @param servicio
	 * @return
	 */
	public String obtenerNaturalezaServicio(Connection con, String servicio)
	{
		return SqlBaseUtilidadesDao.obtenerNaturalezaServicio(con,servicio);
	}
	

	/**
	 * 
	 * @param con
	 * @param actividad
	 * @return
	 */
	public String obtenerArtSerActividadPYP(Connection con, String actividad)
	{
		return SqlBaseUtilidadesDao.obtenerArtSerActividadPYP(con,actividad);
	}
	
	

	/**
	 * 
	 * @param con
	 * @return
	 */
	public ArrayList obtenerCiudades(Connection con)
	{
		return SqlBaseUtilidadesDao.obtenerCiudades(con);
	}
	
	/**
	 * Método que consulta las localidades relacionadas con la ciudad
	 * @param con
	 * @return
	 */
	public ArrayList obtenerLocalidadesCiudades(Connection con)
	{
		return SqlBaseUtilidadesDao.obtenerLocalidadesCiudades(con);
	}
	
	/**
	 * Mï¿½todo que consulta las ciudades por pais
	 * @param con
	 * @param codigoPais
	 * @return
	 */
	public ArrayList obtenerCiudadesXPais(Connection con,String codigoPais)
	{
		return SqlBaseUtilidadesDao.obtenerCiudadesXPais(con, codigoPais);
	}

	
	/**
	 * 
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList obtenerConvenios(Connection con, HashMap campos)
	{
		return SqlBaseUtilidadesDao.obtenerConvenios(con,campos,DaoFactory.POSTGRESQL);
	}
	
	/**
	 * 
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList obtenerConveniosContrato(Connection con, HashMap campos)
	{
		return SqlBaseUtilidadesDao.obtenerConveniosContrato(con,campos, DaoFactory.POSTGRESQL);
	}
	
	/**
	 * 
	 * @param con
	 * @param tipoFiltro
	 * @param institucion
	 * @return
	 */
	public ArrayList obtenerTiposIdentificacion(Connection con,String tipoFiltro,int institucion)
	{
		return SqlBaseUtilidadesDao.obtenerTiposIdentificacion(con,tipoFiltro,institucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public String obtenerCodigoIngresoDadaCuenta(Connection con, String idCuenta)
	{
		return SqlBaseUtilidadesDao.obtenerCodigoIngresoDadaCuenta(con, idCuenta);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoEstadoHCRespondida
	 * @return
	 */
	public ArrayList obtenerSolicitudesEstado(Connection con, int estado, int tipoSolicitud)
	{
		return SqlBaseUtilidadesDao.obtenerSolicitudesEstado(con,estado,tipoSolicitud);
	}
	
	/**
	 * Mï¿½todo implementado para consultar la fecha de ingreso de una cuenta dependiendo de su via de ingreso
	 * @param con
	 * @param idCuenta
	 * @param viaIngreso
	 * @return
	 */
	public String getFechaIngreso(Connection con,String idCuenta,int viaIngreso)
	{
		return SqlBaseUtilidadesDao.getFechaIngreso(con,idCuenta,viaIngreso);
	}
	
	/**
	 * 
	 * @param con
	 * @param tipoId
	 * @param numeroIdentificacion
	 * @return
	 * @throws SQLException 
	 * @throws SQLException 
	 * @throws SQLException 
	 */
	public String getCodigoPaisDeptoCiudadPersona(Connection con, String tipoId, String numeroIdentificacion) 
	{
		return SqlBaseUtilidadesDao.getCodigoPaisDeptoCiudadPersona(con,tipoId,numeroIdentificacion);
	}
	
	/**
	 * 
	 * @param idCuenta
	 * @param fechaFormatoApp
	 * @return
	 */
	public boolean esCamaUciDadaFecha(Connection con, String idCuenta, String fechaFormatoApp, String hora)
	{
		return SqlBaseUtilidadesDao.esCamaUciDadaFecha(con, idCuenta, fechaFormatoApp, hora);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public boolean esMotivoSircUsado(Connection con, String consecutivo)
	{
		return SqlBaseUtilidadesDao.esMotivoSircUsado(con,consecutivo);
	}
	
	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public boolean esServicioSircUsado(Connection con, String codigo, String institucion)
	{
		return SqlBaseUtilidadesDao.esServicioSircUsado(con,codigo, institucion);
	}	
	
	/**	  
	 * Metodo que indica si se esta
	 * usando un registro de entidades subcontratadas
	 * en otra tabla
	 * @param con
	 * @param codigo
	 * @param instituciones 
	 * @return
	 */
	public boolean esEntidadSubContratadaUsado (Connection con, String codigo, String institucion)
	{
		return SqlBaseUtilidadesDao.esEntidadSubContratadaUsado(con, codigo, institucion);
	}
	
	/**	  
	 * Metodo que indica si concepto esta asociado en otra tabala.
	 * @param con
	 * @param codigo
	 * @param instituciones 
	 * @return
	 */
	public boolean esConceptosPagoPoolesUsado(Connection con, String codigo, String institucion)
	{
		return SqlBaseUtilidadesDao.esConceptosPagoPoolesUsado(con, codigo, institucion);
	}
	
	/**
	 * 
	 * @param con
	 * @return
	 */
	public HashMap obtenerCodigoNombreTablaMap(Connection con, String nombreTabla)
	{
		return SqlBaseUtilidadesDao.obtenerCodigoNombreTablaMap(con, nombreTabla);
	}
	
	/**
	 * Mï¿½todo que consulta el id de la ultima cuenta valida del paciente de su ingreso abierto o cerrado,
	 * 
	 * en el caso de que no haya cuenta abierta se devuelve cadena vacï¿½a
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public String obtenerIdCuentaValidaIngresoAbiertoCerrado(Connection con,String codigoPaciente)
	{
		return SqlBaseUtilidadesDao.obtenerIdCuentaValidaIngresoAbiertoCerrado(con,codigoPaciente);
	}	
	
	/**
	 * 
	 */
	public Object obtenerViasIngreso(Connection con,boolean usarVector)
	{
		return SqlBaseUtilidadesDao.obtenerViasIngreso(con,usarVector);
	}
	
	/**
	 * Metodo encargado de devolver las vias de ingreso 
	 * en un arraylist
	 * @param con
	 * @param institucion
	 * @return
	 */
	public ArrayList obtenerViasIngreso(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesDao.obtenerViasIngreso(con,campos);
	}
	
	/**
	 * Metodo encargado de devolver las vias de ingreso 
	 * en un arraylist
	 * @param con
	 * @param institucion
	 * @return
	 */
	public ArrayList obtenerViasIngresoBloqueaDeudorBloqueaPaciente(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesDao.obtenerViasIngresoBloqueaDeudorBloqueaPaciente(con,campos);
	}

	/**
	 * @param con
	 * @param institucion
	 * @return
	 */
	public ArrayList obtenerViasIngresoTipoPaciente(Connection con)
	{
		return SqlBaseUtilidadesDao.obtenerViasIngresoTipoPaciente(con);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public String obtenerViaIngresoCuenta(Connection con, String codigoCuenta)
	{
		return SqlBaseUtilidadesDao.obtenerViaIngresoCuenta(con,codigoCuenta);
	}
	
	/**
	 * Mï¿½todo que verifica si un procedimiento tiene respuesta multiple
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean tieneProcedimientoRespuestaMultiple(Connection con,String numeroSolicitud)
	{
		return SqlBaseUtilidadesDao.tieneProcedimientoRespuestaMultiple(con,numeroSolicitud);
	}
	
	/**
	 * Mï¿½todo que verifica si un procedimiento ya tiene finalizada
	 * la repsuesta multiple
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean estaFinalizadaRespuestaMultiple(Connection con,String numeroSolicitud)
	{
		return SqlBaseUtilidadesDao.estaFinalizadaRespuestaMultiple(con,numeroSolicitud);
	}
	
	
	/**
	 * 
	 */
	public boolean esServicioRespuestaMultiple(Connection con, String codigoServicio)
	{
		return SqlBaseUtilidadesDao.esServicioRespuestaMultiple(con,codigoServicio);
	}
		
	/**
	 * Metodo que consulta los numeros de solicitud de procedimiento que tienen el
	 * estado de historia clï¿½nica especificado en el parï¿½metro, para el paciente de acuedo a la cuenta
	 * @param con
	 * @param cuenta
	 * @param estadoHistoriaClinica
	 * @return
	 */
	public HashMap getSolProcedimientosEstadoHistoClinica(Connection con, int cuenta, int estadoHistoriaClinica)
	{
		return SqlBaseUtilidadesDao.getSolProcedimientosEstadoHistoClinica(con, cuenta, estadoHistoriaClinica);
	}
	
	/**
	 * 
	 */
	public HashMap obtenerResultadosRespuestasSolicitudesProcedimientos(Connection con, String numeroSolicitud)
	{
		return SqlBaseUtilidadesDao.obtenerResultadosRespuestasSolicitudesProcedimientos(con,numeroSolicitud);
	}
	
	/**
	 * Mï¿½todo que consulta los diangosticos de un paciente
	 * @param con
	 * @param campos
	 * @return
	 * acronimo + ConstantesBD.separadorSplit + tipoCie 
	 * si no encuentra nada retorna vacï¿½o
	 */
	public String consultarDiagnosticosPaciente(Connection con,String idCuenta,int viaIngreso)
	{
		return SqlBaseUtilidadesDao.consultarDiagnosticosPaciente(con,idCuenta,viaIngreso);
	}
	
		/**
	 * Mï¿½todo que consulta los usuarios por institucion
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap obtenerUsuarios(Connection con,int institucion, boolean estado)
	{
		return SqlBaseUtilidadesDao.obtenerUsuarios(con,institucion, estado);
	}
	
	/**
	 * Mï¿½todo que consulta los concetos generales por institucion
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap obtenerConceptosGenerales(Connection con,int institucion, String tipoGlosa)
	{
		return SqlBaseUtilidadesDao.obtenerConceptosGenerales(con,institucion, tipoGlosa);
	}
	
	/**
	 * Mï¿½todo que consulta los concetos especificos por institucion
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap obtenerConceptosEspecificos(Connection con,int institucion)
	{
		return SqlBaseUtilidadesDao.obtenerConceptosEspecificos(con,institucion);
	}

	
	/**	 * Mï¿½todo que consulta los conceptos Glosa por institucion
	 * @param con
	 * @param institucion
	 * @return	 */
	public HashMap obtenerConceptosGlosa(Connection con,int institucion)	{
		return SqlBaseUtilidadesDao.obtenerConceptosGlosa(con,institucion);
	}
	
	/**
	 * Mï¿½todo para obtener los conceptos de glosas parametrizados
	 * @param con
	 * @param tipo
	 * @param institucion
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerConceptosGlosa(Connection con,String tipo,int institucion)
	{
		return SqlBaseUtilidadesDao.obtenerConceptosGlosa(con, tipo, institucion);
	}
	
	/**	 * Mï¿½todo que consulta los conceptos Ajuste por institucion
	 * @param con
	 * @param institucion
	 * @return	 */
	public HashMap obtenerConceptosAjuste(Connection con,int institucion)	{
		return SqlBaseUtilidadesDao.obtenerConceptosAjuste(con,institucion);
	}

	
	/**
	 * 
	 */
	public int obtenerEstadoSolicitudTraslado(Connection con, String numeroSolicitudTraslado)
	{
		return SqlBaseUtilidadesDao.obtenerEstadoSolicitudTraslado(con,numeroSolicitudTraslado);
	}
	
		
	/**
	 * 
	 * @param con
	 * @param codigoServicio
	 * @return
	 */	
	public boolean esSolicitudProcedimientosFinalizada(Connection con, String numeroSolicitud)
	{
		return SqlBaseUtilidadesDao.esSolicitudProcedimientosFinalizada(con, numeroSolicitud);
	}
	

	/**
	 * 
	 */
	public String obtenerCentrosCostoUsuario(Connection con, String login)
	{
		return SqlBaseUtilidadesDao.obtenerCentrosCostoUsuario(con,login);
	}

	/**
	 * 
	 */
	public String obtenerDescripcionEstadoHC(Connection con, String codigoEstado)
	{
		return SqlBaseUtilidadesDao.obtenerDescripcionEstadoHC(con,codigoEstado);
	}
	

	
	/**
	 * 
	 * @param con
	 * @param codigoAjuste
	 * @return
	 */
	public HashMap consultarDetalleAjustesImpresion(Connection con, String codigoAjuste)
	{
		return SqlBaseUtilidadesDao.consultarDetalleAjustesImpresion(con,codigoAjuste);
	}
	
	
	/**
	 * 
	 */
	public HashMap consultarEncabezadoAjuste(Connection con, String codigoAjuste)
	{
		return SqlBaseUtilidadesDao.consultarEncabezadoAjuste(con,codigoAjuste);
	}

	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public ArrayList obtenerMedicosInstitucion(Connection con, String codigoInstitucion)
	{
		return SqlBaseUtilidadesDao.obtenerMedicosInstitucion(con,codigoInstitucion);
	}	
	/**
	 * Metodo para Extraer el numero de embarazos de la funcionalidad informacion del parto.
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public String obtenerNrosEmbarazosInformacionParto(Connection con, int codigoPersona)
	{
		return SqlBaseUtilidadesDao.obtenerNrosEmbarazosInformacionParto(con,codigoPersona);
	}
	
	/**
     * Metodo que consulta la informacion del paciente que se coloca en cada una
     * de las fichas de epidemiologï¿½a
     * @param con
     * @param codigoPaciente
     * @return
     */
	public ResultSetDecorator consultarDatosPacienteFicha(Connection con, int codigoPaciente)
	{
		return SqlBaseUtilidadesDao.consultarDatosPacienteFicha(con, codigoPaciente);
	}
	
	/**
	 * Mï¿½todo que consulta la fecha de admisionde la cuenta de una cirugia
	 * @param con
	 * @param codigoCirugia
	 * @return
	 */
	public String consultarFechaAdmisionCirugia(Connection con,String codigoCirugia)
	{
		return SqlBaseUtilidadesDao.consultarFechaAdmisionCirugia(con, codigoCirugia);
	}
	
	/**
	 * Metodo para obtener de la informacion del parto el numero del embarazo de acuerdo al codigo
	 * de la cirugï¿½a y al paciente
	 * @param con
	 * @param codigoPaciente
	 * @param codigoCirugia
	 * @return -1 si no existe de lo contrario retorna el nro del embarazo
	 */
	public int obtenerNroEmbarazoCirugiaPaciente(Connection con, int codigoPaciente, String codigoCirugia)
	{
		return SqlBaseUtilidadesDao.obtenerNroEmbarazoCirugiaPaciente(con, codigoPaciente, codigoCirugia);
	}
	
	
	/**
	 * Mï¿½todo que consulta los tipos de Identificacion
	 * @param con
	 * @param institucion
	 * @return
	 */
	public  HashMap consultarTiposidentificacion(Connection con,int institucion)
	{
		return SqlBaseUtilidadesDao.consultarTiposidentificacion(con, institucion);
	}
	/**
	 * M?todo que consulta las Ciudades 
	 * @param con
	 * @return
	 */
	public  HashMap consultarCiudades(Connection con)
	{
		return SqlBaseUtilidadesDao.consultarCiudades(con);
	}

	/**
	 * M?todo que consulta las Tipos De Sangre 
	 * @param con
	 * @return
	 */	
	public  HashMap consultarTiposSangre(Connection con)
	{
		return SqlBaseUtilidadesDao.consultarTiposSangre(con);
	}

	/**
	 * M?todo que consulta las Estados Civiles 
	 * @param con
	 * @return
	 */
	public  HashMap consultarEstadosCiviles(Connection con)
	{
		return SqlBaseUtilidadesDao.consultarEstadosCiviles(con);
	}

	/**
	 * M?todo que consulta las Zonas Domiciliarias
	 * @param con
	 * @return
	 */
	public  HashMap consultarZonasDomicilio(Connection con)
	{
		return SqlBaseUtilidadesDao.consultarZonasDomicilio(con);
	}

	/**
	 * M?todo que consulta las Ocupaciones 
	 * @param con
	 * @return
	 */
	public  HashMap consultarOcupaciones(Connection con)
	{
		return SqlBaseUtilidadesDao.consultarOcupaciones(con);
	}	

	/**
	 * 
	 */
	public int obtenerNumeroMedicamentosSolicitud(Connection con, String numeroSolicitud)
	{
		return SqlBaseUtilidadesDao.obtenerNumeroMedicamentosSolicitud(con,numeroSolicitud);
	}
	
	/**
	 * Mï¿½todo para saber si el tipo y nï¿½mero de identificaciï¿½n del paciente
	 * existe en usuario capitados, si existe retorna el codigo sino retorna -1
	 * @param con
	 * @param tipoIdentificacion
	 * @param nroIdentificacion
	 * @return
	 */
	public int getCodigoUsuarioCapitado(Connection con, String tipoIdentificacion, String nroIdentificacion)
	{
		return SqlBaseUtilidadesDao.getCodigoUsuarioCapitado(con, tipoIdentificacion, nroIdentificacion);
	}
	
	/**
	 * Metodo que obtiene la informaciï¿½n del convenio capitado, de acuerdo
	 * al codigo del usuario capitado enviado por parï¿½metro
	 * @param con
	 * @param codigoUsuarioCapitado
	 */
	public HashMap convenioUsuarioCapitado(Connection con, int codigoUsuarioCapitado)
	{
		return SqlBaseUtilidadesDao.convenioUsuarioCapitado(con, codigoUsuarioCapitado);
	}
	
	/**
	 * Mï¿½todo implementado para consultar tipo id, numero id y nombre de las personas
	 * que tienen el mismo numero id
	 * @param con
	 * @param numeroIdentificacion
	 * @return
	 */
	public HashMap personasConMismoNumeroId(Connection con,String numeroIdentificacion,String tipoIdentificacion)
	{
		return SqlBaseUtilidadesDao.personasConMismoNumeroId(con, numeroIdentificacion,tipoIdentificacion);
	}
	
	/**
	 * Mï¿½todo que marca como atendido los registros de pacientes para triage
	 * del paciente
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public int actualizarPacienteParaTriageVencido(Connection con,String codigoPaciente)
	{
		return SqlBaseUtilidadesDao.actualizarPacienteParaTriageVencido(con, codigoPaciente);
	}
	
	/**
	 * Mï¿½todo que condulta el codigo UPGD del centro de atencion
	 * @param con
	 * @param centroAtencion
	 * @return
	 */
	public String getCodigoUPGDCentroAtencion(Connection con,int centroAtencion)
	{
		return SqlBaseUtilidadesDao.getCodigoUPGDCentroAtencion(con, centroAtencion);
	}
	
	/**
	 * 
	 */
	public String getLoginUsuarioSolicitaOrdenAmbulatoria(Connection con,String consecutivoOrden, int institucion)
	{
		return SqlBaseUtilidadesDao.getLoginUsuarioSolicitaOrdenAmbulatoria(con, consecutivoOrden, institucion);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigoServicio
	 * @return
	 */
	public HashMap consultarInformacionServicio(Connection con, String codigoServicio, int institucion)
	{
		return SqlBaseUtilidadesDao.consultarInformacionServicio(con,codigoServicio, institucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoEnfermedad
	 * @param institucion
	 * @return
	 */
	public ArrayList consultarServiciosEnfermedadEpidemiologia(Connection con, String codigoEnfermedad, String institucion)
	{
		return SqlBaseUtilidadesDao.consultarServiciosEnfermedadEpidemiologia(con,codigoEnfermedad,institucion);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param sexo
	 * @return
	 */
	public String getDescripcionSexo(Connection con, int sexo)
	{
		return SqlBaseUtilidadesDao.getDescripcionSexo(con,sexo);
	}
	

	/**
	 * 
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public int obtenerNumeroSolicitudesPypXCuenta(Connection con, String cuenta,String busquedaPYP)
	{
		return SqlBaseUtilidadesDao.obtenerNumeroSolicitudesPypXCuenta(con,cuenta,busquedaPYP);
	}
	

	/**
	 * 
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public HashMap obtenerCajasCajero(Connection con, String loginUsuario, int codigoInstitucion, int codigoTipoCaja, int codigoCentroAtencion)
	{
		return SqlBaseUtilidadesDao.obtenerCajasCajero(con, loginUsuario, codigoInstitucion, codigoTipoCaja, codigoCentroAtencion);
	}

	/**
	 * 
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public HashMap obtenerCajas(Connection con, int codigoInstitucion, int codigoTipoCaja)
	{
		return SqlBaseUtilidadesDao.obtenerCajas(con, codigoInstitucion, codigoTipoCaja);
	}
	
	/**
	 * 
	 */
	public HashMap consultarDatosGeneralesPaciente(Connection con, String numeroDocumento)
	{
		return SqlBaseUtilidadesDao.consultarDatosGeneralesPaciente(con,numeroDocumento);
	}

	/**
	 * 
	 * @param con
	 * @param codigoPersona
	 * @param fecha
	 * @param institucion
	 * @return
	 */
	public InfoDatosString obtenerTipoRegimenSolicitudUsuarioCapitado(Connection con, String codigoPersona, String fecha, int institucion)
	{
		return SqlBaseUtilidadesDao.obtenerTipoRegimenSolicitudUsuarioCapitado(con, codigoPersona, fecha, institucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public HashMap obtenerCentrosAtencion(Connection con, int codigoInstitucion)
	{
		return SqlBaseUtilidadesDao.obtenerCentrosAtencion(con, codigoInstitucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param codProveedor
	 * @return
	 */
	public HashMap obtenerProveedores(Connection con, int codProveedor)
	{
		return SqlBaseUtilidadesDao.obtenerProveedores(con, codProveedor);
	}
	
	/**
	 * @param con
	 * @param codigoInstitucion
	 * @param inactivos
	 * @return
	 */
	public HashMap obtenerCentrosAtencionInactivos(Connection con, int codigoInstitucion, boolean inactivos)
	{
		return SqlBaseUtilidadesDao.obtenerCentrosAtencionInactivos(con, codigoInstitucion, inactivos);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @param cuentaContable
	 * @param descripcion
	 * @return
	 */
	public HashMap obtenerCuentasContables(Connection con, int codigoInstitucion, String cuentaContable, String descripcion,String anioVigencia, String naturCuenta)
	{
		return SqlBaseUtilidadesDao.obtenerCuentasContables(con, codigoInstitucion, cuentaContable, descripcion,anioVigencia, naturCuenta);
	}

	/**
	 * 
	 */
	public String obtenerNombreArticulo(Connection con, int codigoArticulo)
	{
		return SqlBaseUtilidadesDao.obtenerNombreArticulo(con,codigoArticulo);
	}
	

	/**
	 * 
	 * @param con
	 * @param unidosis
	 * @return
	 */
	public String obtenerUnidadMedidadUnidosisArticulo(Connection con, String unidosis)
	{
		return SqlBaseUtilidadesDao.obtenerUnidadMedidadUnidosisArticulo(con,unidosis);
	}
	

	/**
	 * 
	 */
	public HashMap consultarTerceros(Connection con, int institucion)
	{
		return SqlBaseUtilidadesDao.consultarTerceros(con,institucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param consecutivoCaja
	 * @return
	 */
	public int obtenerTipoCaja(Connection con, int consecutivoCaja)
	{
		return SqlBaseUtilidadesDao.obtenerTipoCaja(con,consecutivoCaja);
	}

	/**
	 * @param con
	 * @param acronimo 
	 * @return
	 */
	public boolean esFormaFarmaceuticaUsada(Connection con, String consecutivo)
	{
		return SqlBaseUtilidadesDao.esFormaFarmaceuticaUsada(con,consecutivo);
	}	

	/**
	 * 
	 */
	public  boolean esClaseInventarioUsada(Connection con, int consecutivo)
	{
		return SqlBaseUtilidadesDao.esClaseInventarioUsada(con, consecutivo);
	}
	
	/**
	 * 
	 */
	public boolean esGrupoInventarioUsada(Connection con, int consecutivo, int clase)
	{
		return SqlBaseUtilidadesDao.esGrupoInventarioUsada(con, consecutivo, clase);
	}
	
	/**
	 * 
	 */
	public boolean esSubgrupoInventarioUsada(Connection con, int consecutivo, int subgrupo , int grupo, int clase)
	{
		return SqlBaseUtilidadesDao.esSubgrupoInventarioUsada(con, consecutivo, subgrupo, grupo, clase);
	}

	/**
	 * 
	 */
	public ArrayList obtenerTercerosInstitucionActivos(Connection con, int institucion)
	{
		return SqlBaseUtilidadesDao.obtenerTercerosInstitucionActivos(con,institucion);
	}	
	
	/**
	 * Mï¿½todo que consulta las unidades funcionales por institucion
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultarUnidadesFuncionales(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesDao.consultarUnidadesFuncionales(con, campos);
	}
	
	/**
	 * 
	 * @param con
	 * @param codPaciente
	 * @param codEstadoCuenta
	 * @return
	 */
	public int obtenerCuentaDadoPacienteEstado(Connection con, int codPaciente, int codEstadoCuenta)
	{
		return SqlBaseUtilidadesDao.obtenerCuentaDadoPacienteEstado(con, codPaciente, codEstadoCuenta);
	}
	
	/**
	 * Mï¿½todo que retorna el tipo de regimen del convenio
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public String obtenerTipoRegimenConvenio(Connection con,String codigoConvenio)
	{
		return SqlBaseUtilidadesDao.obtenerTipoRegimenConvenio(con, codigoConvenio);
	}

	/**
	 * Mï¿½todo que retorna el tipo de regimen del convenio
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public String obtenerCodigoTipoRegimenConvenio(Connection con,String codigoConvenio)
	{
		return SqlBaseUtilidadesDao.obtenerCodigoTipoRegimenConvenio(con, codigoConvenio);
	}
	
	/**
	 * 
	 */
	public ArrayList obtenerContratos(Connection con, int codigoConvenio, boolean todos,boolean validarNumeroContratos, boolean pendientesFinalizar) 
	{
		return SqlBaseUtilidadesDao.obtenerContratos(con,codigoConvenio,todos,validarNumeroContratos,pendientesFinalizar,DaoFactory.POSTGRESQL);
	}

	public HashMap<String, Object> obtenerNaturalezasEventosCatastroficos(Connection con)
	{
		return SqlBaseUtilidadesDao.obtenerNaturalezasEventosCatastroficos(con);
	}

	/**
	 * 
	 */
	public HashMap<String, Object> obtenerInformacionReferenciaTramite(Connection con, int codigoPaciente, int idIngreso)
	{
		return SqlBaseUtilidadesDao.obtenerInformacionReferenciaTramite(con,codigoPaciente,idIngreso);
	}
	

	/**
	 * 
	 */
	public Vector<InfoDatosString> obtenerNaturalezasPaciente(Connection con, String tipoRegimen, int convenio, int viaIngreso)
	{
		return SqlBaseUtilidadesDao.obtenerNaturalezasPaciente(con, tipoRegimen, convenio, viaIngreso);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.princetonsa.dao.UtilidadesDao#obtenerNaturalezasPacienteXTipoAfiliadoEstrato(java.sql.Connection, java.lang.String, int, int, int, int)
	 */
	@Override
	public Vector<InfoDatosString> obtenerNaturalezasPacienteXTipoAfiliadoEstrato(
			Connection con, String tipoRegimen, int convenio, int viaIngreso,
			String codigoTipoAfiliado, int codigoEstratoSocial, String fechaReferencia) {
		return SqlBaseUtilidadesDao.obtenerNaturalezasPacienteXTipoAfiliadoEstrato(con, tipoRegimen, convenio, 
				viaIngreso, codigoTipoAfiliado, codigoEstratoSocial, DaoFactory.POSTGRESQL, fechaReferencia);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public String getNombreEstadoCartera(Connection con, int codigo)
	{
		return SqlBaseUtilidadesDao.getNombreEstadoCartera(con, codigo);
	}
	

	/**
	 * 
	 */
	public Vector<InfoDatosString> obtenerListadoCoberturasInstitucion(Connection con, int institucion)
	{
		return SqlBaseUtilidadesDao.obtenerListadoCoberturasInstitucion(con,institucion);
	}
	
	/**
	 * 
	 */
	public HashMap obtenerCentrosCosto(Connection con, int institucion, String tiposArea, boolean todos,int centroAtencion, boolean filtro_externos)
	{
		return SqlBaseUtilidadesDao.obtenerCentrosCosto(con,institucion,tiposArea,todos,centroAtencion, filtro_externos);
	}
	
	/**
	 * 
	 */
	public Vector<InfoDatosString> obtenerTiposComplejidad(Connection con) {
		return SqlBaseUtilidadesDao.obtenerTiposComplejidad(con);
	}


	/**
	 * 
	 */
	public String consultarNombreTablaInterfaz(Connection con, String identificadorTabla,int institucion) 
	{
		return SqlBaseUtilidadesDao.consultarNombreTablaInterfaz(con,identificadorTabla,institucion);
	}

	/**
	 * 
	 */
	public boolean insertarAbono(Connection con, DtoInterfazAbonos abono) 
	{
		String cadena="INSERT INTO movimientos_abonos(codigo,paciente,codigo_documento,tipo,valor,fecha,hora,institucion) values(nextval('seq_movimientos_abonos'),?,?,?,?,?,?,?)";
		return SqlBaseUtilidadesDao.insertarAbono(con,abono,cadena);
	}

	/**
	 * 
	 */
	public Vector<String> obetenerCodigosInstituciones(Connection con) 
	{
		return SqlBaseUtilidadesDao.obetenerCodigosInstituciones(con);
	}
	

	/**
	 * 
	 */
	public InfoDatosString obtenerInstitucionSirCentroAtencion(Connection con, int codigoCentroAtencion) 
	{
		return SqlBaseUtilidadesDao.obtenerInstitucionSirCentroAtencion(con,codigoCentroAtencion);
	}

	
	/**
	 * Metodo implementado para obtener todas las instituciones en un HashMap
	 */
	public HashMap obtenerInstituciones(Connection con) 
	{
		return SqlBaseUtilidadesDao.obtenerInstituciones(con);
	}
	
	
	
	/**
	 * Mï¿½todo implementado parta obtener los paises
	 * @param con
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerPaises(Connection con)
	{
		return SqlBaseUtilidadesDao.obtenerPaises(con);
	}
	
	/**
	 * Mï¿½todo que consulta las localidades 
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<HashMap<String,Object>> obtenerLocalidades(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesDao.obtenerLocalidades(con, campos);
	}
	
	/**
	 * Mï¿½todo que consulta la localidad de un barrio
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<HashMap<String,Object>> obtenerLocalidadDeBarrio(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesDao.obtenerLocalidadDeBarrio(con, campos);
	}
	
	
	/**
	 * Mï¿½todo que consulta los sexos parametrizados en el sistema
	 * @param con
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerSexos(Connection con)
	{
		return SqlBaseUtilidadesDao.obtenerSexos(con);
	}
	

	/**
	 * 
	 */
	public int getCodigoResponsable(Connection con, int idIngreso, int codigoConvenio) 
	{
		return SqlBaseUtilidadesDao.getCodigoResponsable(con,idIngreso,codigoConvenio);
	}

	/**
	 * 
	 */
	public int obtenerPrioridadResponsabe(Connection con, String subCuenta) throws BDException
	{
		return SqlBaseUtilidadesDao.obtenerPrioridadResponsabe(con,subCuenta);
	}

	/**
	 * 
	 */
	public boolean convenioManejaComplejidad(Connection con, int codigoConvenio) 
	{
		return SqlBaseUtilidadesDao.convenioManejaComplejidad(con,codigoConvenio);
	}
	
	/**
	 * 
	 */
	public boolean convenioManejaPyp(Connection con, int codigoConvenio) 
	{
		return SqlBaseUtilidadesDao.convenioManejaPyp(con,codigoConvenio);
	}

	/**
	 * 
	 */
	public InfoDatosInt obtenerTipoComplejidadCuenta(Connection con, int cuenta) throws BDException
	{
		return SqlBaseUtilidadesDao.obtenerTipoComplejidadCuenta(con,cuenta);
	}
	
	
	/**
	 * Adiciï¿½n Jhony Alexander Duque
	 * Mï¿½todo usado para obtener los grupos de servicios y con la posibilidad de ser filtrado
	 * con cualquiera de los campos de la tabla
	 * @param Connection connection
	 * @param HashMap grupoServicios
	 * @return ArrayList<HashMap> 
	 */
	public ArrayList<HashMap<String, Object>> obtenerGrupoServicios (Connection connection, HashMap grupoServicios)
	{
		return SqlBaseUtilidadesDao.obtenerGrupoServicios(connection, grupoServicios);
	}

	/**
	 * 
	 */
	public HashMap obtenerViasIngresoCuenta(Connection con, int cuenta) 
	{
		return SqlBaseUtilidadesDao.obtenerViasIngresoCuenta(con, cuenta);
	}
	

	/**
	 * 
	 */
	public HashMap obtenerTiposPacienteCuenta(Connection con, int cuenta) 
	{
		return SqlBaseUtilidadesDao.obtenerTiposPacienteCuenta(con, cuenta);
	}
	

	/**
	 * 
	 */
	public String obtenerPorcentajeAutorizadoVerficacionDerechos(Connection con, String subCuenta) 
	{
		return SqlBaseUtilidadesDao.obtenerPorcentajeAutorizadoVerficacionDerechos(con, subCuenta);
	}

	/**
	 * 
	 */
	public HashMap obtenerCoberturaAccidenteTransitos(Connection con, int institucion)
	{
		return SqlBaseUtilidadesDao.obtenerCoberturaAccidenteTransitos(con,institucion);
	}

	/**
	 * 
	 */
	public String obtenerFechaAccidenteTransito(Connection con, int codigoIngreso) 
	{
		return SqlBaseUtilidadesDao.obtenerFechaAccidenteTransito(con,codigoIngreso);
	}

	/**
	 * 
	 */
	public double obtenerSalarioMinimoVigente(Connection con, String fecha) 
	{
		return SqlBaseUtilidadesDao.obtenerSalarioMinimoVigente(con,fecha);
	}

	/**
	 * 
	 */
	public InfoDatosString obtenerEstadoHC(Connection con, String numeroSolicitud) throws SQLException,Exception
	{
		return SqlBaseUtilidadesDao.obtenerEstadoHC(con,numeroSolicitud);
	}

	/**
	 * 
	 */
	public ResultadoBoolean esPorcentajeMonto(Connection con, int codigoMonto)throws Exception
	{
		return SqlBaseUtilidadesDao.esPorcentajeMonto(con,codigoMonto);
	}

	/**
	 * 
	 */
	public double obtenerTotalCargadoResponsable(Connection con, String subCuenta) 
	{
		return SqlBaseUtilidadesDao.obtenerTotalCargadoResponsable(con,subCuenta);
	}
	
	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public String obtenerNombreTipoComplejidad (Connection con, int codigo)
	{
		return SqlBaseUtilidadesDao.obtenerNombreTipoComplejidad(con, codigo);
	}
	
	/**
	 * 
	 * @param con
	 * @param subcuenta
	 * @return
	 */
	public String obtenerNumeroCarnet(Connection con, double subcuenta)
	{
		return SqlBaseUtilidadesDao.obtenerNumeroCarnet(con, subcuenta);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoFuncionalidad
	 * @return
	 */
	public String obtenerPathFuncionalidad(Connection con, int codigoFuncionalidad)
	{
		return SqlBaseUtilidadesDao.obtenerPathFuncionalidad(con, codigoFuncionalidad);
	}

	/**
	 * 
	 * @param con
	 * @param esInventarios
	 * @param codigoInstitucion
	 * @return
	 */
	public HashMap obtenerEsquemasTarifarios(Connection con, boolean esInventarios, int codigoInstitucion)
	{
		return SqlBaseUtilidadesDao.obtenerEsquemasTarifarios(con, esInventarios, codigoInstitucion);
	}
	
	
	/**
	 * Metodo que devuelve un arraylist con los valores de esquemas tarifarios
	 * @param con
	 * @param esInventarios
	 * @param codigoInstitucion
	 * @param tarifarioOficial
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerEsquemasTarifariosInArray(Connection con, boolean esInventarios, int codigoInstitucion,int tarifarioOficial)
	{
		return SqlBaseUtilidadesDao.obtenerEsquemasTarifariosInArray(con, esInventarios, codigoInstitucion, tarifarioOficial);
	}
	
	/**
	 * Mï¿½todo que consulta los dias de la semana
	 * @param con	
	 * @return
	 */
	public HashMap obtenerDiasSemana(Connection con)
	{
		return SqlBaseUtilidadesDao.obtenerDiasSemana(con);		
	}
	
	/**
	 * Mï¿½todo que consulta el nombre del estado de liquidacion
	 * @param con
	 * @param acronimo
	 * @return
	 */
	public String getNombreEstadoLiquidacion(Connection con,String acronimo)
	{
		return SqlBaseUtilidadesDao.getNombreEstadoLiquidacion(con, acronimo);
	}
	
	/**
	 * Mï¿½todo que consulta el nombre del estado de la cita
	 * @param con
	 * @param codigo
	 * @return
	 */
	public String getNombreEstadoCita(Connection con,int codigo)
	{
		return SqlBaseUtilidadesDao.getNombreEstadoCita(con, codigo);
	}
	
	/**
	 * Mï¿½todo que consulta las finalidades de un servicio
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerFinalidadesServicio(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesDao.obtenerFinalidadesServicio(con, campos);
	}
	
	/**
	 * Metodo que retorna el nombre del esquema tarifario segun el codigo 
	 */
	public String getNombreEsquemaTarifario(Connection con, int codigo)
	{
		return SqlBaseUtilidadesDao.getNombreEsquemaTarifario(con, codigo);
	}
	
	/**
	 * Metodo que retorna el tarifario oficial del esquema tarifario segun el codigo
	 */
	public int getTarifarioOficial(Connection con, int codigo)
	{
		return SqlBaseUtilidadesDao.getTarifarioOficial(con, codigo);
	}
	
	
		/**
	 * 
	 */
	public int obtenerSexoServicio(Connection con, String codigoServicio) 
	{
		return SqlBaseUtilidadesDao.obtenerSexoServicio(con, codigoServicio);
	}
	/**
	 * Metodo encargado de consultar todos los servicios,
	 * pudiendolos filtrar por diferentes tipos
	 * de servicio y por el estado activo:
	 * ----------------------------------------------------
	 * LOS VALORES DEBEN IR DE LA SIGUINTE FORMA
	 * ----------------------------------------------------
	 * --tiposServicio --> ejemplo, este es el valor de esta variable  'R','Q','D'
	 * --activo --> debe indicar "t" o "f". 
	 * @param connection
	 * @param acronimos
	 * @param esqx
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerServicos(Connection connection,String tiposServicio, String activo)
	{
		return SqlBaseUtilidadesDao.obtenerServicos(connection, tiposServicio, activo);
	}
	
	
	/**
	 * Metodo encargado de consultar todos los tipos de cirugia,
	 * pudiendolos filtrar por el acronimo.
	 * 
	 * ----------------------------------------------------
	 * LOS VALORES DEBEN IR DE LA SIGUINTE FORMA
	 * ----------------------------------------------------
	 * --acronimo --> ejemplo, este es el valor de esta variable  'PP','PB','MA'
	 *  
	 * @param connection
	 * @param acronimos
	 * @param esqx
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerTipoCirugia(Connection connection,String acronimo)
	{
		return SqlBaseUtilidadesDao.obtenerTipoCirugia(connection, acronimo);
	}
	
	/**
	 * Metodo encargado de consultar todos los tipos de anestecia,
	 * pudiendolos filtrar por el acronimo.
	 * 
	 * ----------------------------------------------------
	 * LOS VALORES DEBEN IR DE LA SIGUINTE FORMA
	 * ----------------------------------------------------
	 * --acronimo --> ejemplo, este es el valor de esta variable  'PP','PB','MA'
	 *  
	 * @param connection
	 * @param acronimos
	 * @return
	 */
	public  ArrayList<HashMap<String, Object>> obtenerTipoAnestecia(Connection connection,String acronimos)
	{
		return SqlBaseUtilidadesDao.obtenerTipoAnestecia(connection, acronimos);
	}
	
	/**
	 * Metodo encargado de consultar todos los asocios,
	 * pudiendolos filtrar por el codigo_asocio,
	 * tipo de servicio y si participa por cirugia.
	 * 
	 * ----------------------------------------------------
	 * LOS VALORES DEBEN IR DE LA SIGUINTE FORMA
	 * ----------------------------------------------------
	 * --codAsocio --> ejemplo, este es el valor de esta variable  'PP','PB','MA'
	 * --tipoServ --> ejemplo, este es el valor de esta variable  'Q','R','D'
	 * --participa --> es S o N
	 * @param connection
	 * @param codAsocio
	 * @param tipoServ
	 * @param participa
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerAsocios(Connection connection,String codAsocio,String tipoServ,String participa)
	{
		return SqlBaseUtilidadesDao.obtenerAsocios(connection, codAsocio, tipoServ, participa);
	}
	
	
	/** 
	 * Mï¿½todo implementado parta obtener Tipos de Sala
	 * si se envian vacios esQuirurjica y esUrgencias no se toman en cuenta
	 * @param con
	 * @param int institucion
	 * @param String esQuirurjica (t,f) 
	 * @param String esUrgencias (t,f)
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerTiposSala(Connection con,int institucion,String esQuirurjica,String esUrgencias)
	{
		return SqlBaseUtilidadesDao.obtenerTiposSala(con, institucion, esQuirurjica, esUrgencias);
	}
	
	
	/**
	 * Metodo que devuelve un arraylist con los valores de esquemas tarifarios Generales y Particulares
	 * adicionalmente se carga el codigo del encabezado asociado a la parametrica (porcentajescx,asociosserv)
	 * @param con
	 * @param codigoInstitucion
	 * @param String parametrica
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerEsquemasTarifariosGenPartInArray(
			Connection con,			
			int codigoInstitucion,
			String parametrica) 
	{
		return SqlBaseUtilidadesDao.obtenerEsquemasTarifariosGenPartInArray(con, codigoInstitucion, parametrica);			
	}
	
	/**
	 * Metodo que retorna los esquemas tarifarios ISS
	 * @param con
	 * @return
	 */
	public ArrayList obtenerEsquemastarifariosIss(Connection con)
	{
		return SqlBaseUtilidadesDao.obtenerEsquemastarifariosIss(con);
	}
	
	/**
	 * Metodo que retorna todos los tipos de asocio
	 * @param con
	 * @return
	 */
	public ArrayList obtenerTiposAsocio(Connection con)
	{
		return SqlBaseUtilidadesDao.obtenerTiposAsocio(con);
	}
	
	/**
	 * 
	 * @param con
	 * @param mostrarEnHqx
	 * @return
	 */
	public ArrayList obtenerTiposAnestesia(Connection con,String mostrarEnHqx)
	{
		return SqlBaseUtilidadesDao.obtenerTiposAnestesia(con, mostrarEnHqx);
	}
	
	/**
	 * Metodo que carga un array list con las ocupaciones medicas 
	 * @param con
	 * @return
	 */
	public ArrayList obtenerOcupacionesMedicas(Connection con)
	{
		return SqlBaseUtilidadesDao.obtenerOcupacionesMedicas(con);	
	}
	
	/**
	 * Metodo que retorna un array list con las especialidades
	 * @param con
	 * @return
	 */
	public ArrayList obtenerEspecialidadesEnArray(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesDao.obtenerEspecialidadesEnArray(con,campos);
	}
	
	/**
	 * Metodo que retorna un array list con los tarifarios oficiales filtrando por la columna tarifarios si se quiere se le envia un string con 'S' , 'N' o vacio, ejemplo sin filtro-->>Utilidades.obtenerTarifariosOficiales(con,""); ejemplo con filtro-->>Utilidades.obtenerTarifariosOficiales(con,"S");
	 * @param con
	 * @return
	 */
	public ArrayList obtenerTarifariosOficiales(Connection con,String tarifarios)
	{
		return SqlBaseUtilidadesDao.obtenerTarifariosOficiales(con, tarifarios);
	}
	
	/**
	 * 
	 */
	public HashMap<String, Object> obtenerTransaccionesCentroCosto(Connection con, int institucion) 
	{
		return SqlBaseUtilidadesDao.obtenerTransaccionesCentroCosto(con, institucion);
	}


	/**
	 * 
	 */
	public HashMap<String, Object> obtenerArticulosCantidadPosNeg(Connection con, String almacen, String claseInventario, String grupoInventario, boolean existenciasPositivas) 
	{
		return SqlBaseUtilidadesDao.obtenerArticulosCantidadPosNeg(con, almacen,claseInventario,grupoInventario,existenciasPositivas);
	}
	
	/**
	 * 
	 * @param con
	 * @param loginUsuario
	 * @param almacen
	 * @param transaccionEntrada
	 * @param transaccionSalida
	 * @param observaciones
	 * @return
	 */
	public boolean generarLogTransSaldosInventario(Connection con, String loginUsuario, String almacen, String transaccionEntrada, String transaccionSalida, String observaciones)
	{
		return SqlBaseUtilidadesDao.generarLogTransSaldosInventario(con, loginUsuario,almacen,transaccionEntrada,transaccionSalida,observaciones);
	}

	/**
	 * 
	 */
	public HashMap consultarFacturasMismoConsecutivo(Connection con, int factura, int codigoInstitucionInt) {
		return SqlBaseUtilidadesDao.consultarFacturasMismoConsecutivo(con, factura,codigoInstitucionInt);
	}
	
	
	/**
     * @param con
     * @param Acronimo Tipo Identificacion
     * @return
     */
    public String getDescripcionTipoIdentificacion(Connection con, String acronimoTipoIdentificacion)
    {
    	return SqlBaseUtilidadesDao.getDescripcionTipoIdentificacion(con, acronimoTipoIdentificacion);
    }
    

	/**
	 * 
	 */
	public String obtenerRazonSocialEmpresaInstitucion(Connection con, String empresaInstitucion) 
	{
		return SqlBaseUtilidadesDao.obtenerRazonSocialEmpresaInstitucion(con, empresaInstitucion);
	}
	 
	
	/**
	 * Consulta las empresas que pertenecen a la institucion
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public ArrayList<HashMap<String, Object>> obtenerEmpresasXInstitucion(Connection con, HashMap parametros)
	{
		return SqlBaseUtilidadesDao.obtenerEmpresasXInstitucion(con, parametros);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param numDiasControl
	 * @param articulos
	 * @param codigoPersona
	 * @return
	 */
	public HashMap<String, Object> consultarArticulosSolicitadosUltimosXDias(Connection con, int numDiasControl, HashMap articulos, int codigoPersona)
	{
		return SqlBaseUtilidadesDao.consultarArticulosSolicitadosUltimosXDias(con, numDiasControl,articulos,codigoPersona);
	}


	/**
	 * 
	 * @param con
	 * @param codigoOrden
	 * @param loginUsuario
	 * @param articulosConfirmacion
	 * @return
	 */
	public boolean generarLogConfirmacionOrdenAmbSolMed(Connection con, String codigoOrden, String loginUsuario,String esSolOrdenConfirmada, HashMap<String, Object> articulosConfirmacion)
	{
		return SqlBaseUtilidadesDao.generarLogConfirmacionOrdenAmbSolMed(con, codigoOrden,loginUsuario,esSolOrdenConfirmada,articulosConfirmacion);
	}
	
	/**
	 * Obtener centros Costos usuario
	 * @param Connection con
	 * @param String usuarioLogin
	 * @param int Institucion 
	 * @param String tipoArea
	 * @param String codigoViaIngreso
	 * @return
	 */
	public  HashMap obtenerCentrosCostoUsuario(Connection con,String usuarioLogin, int institucion, String tipoArea, String codigoViaIngreso) 
	{
		return SqlBaseUtilidadesDao.obtenerCentrosCostoUsuario(con, usuarioLogin, institucion, tipoArea, codigoViaIngreso);
	}
	
	/**
	 * Obtener Especialidades Medicos
	 * @param Connection con
	 * @param String codigoMedico 
	 * @return
	 */
	public HashMap obtenerEspecialidadesMedico(Connection con,String codigoMedico) 
	{
		return SqlBaseUtilidadesDao.obtenerEspecialidadesMedico(con, codigoMedico);
	}
	
	/**
	 * 
	 */
	public double obtenerAbonosDisponiblesPaciente(Connection con, int codigoPaciente, int ingreso) 
	{
		String consulta="SELECT getabonodisponible(?,?) AS abono_disponible";
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			ps.setObject(1, codigoPaciente+"");
			ps.setInt(2, ingreso);
			logger.info("Consulta Abonos Disponibles >>"+ps);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			double resultado=0;
			if(rs.next())
			{
				resultado=rs.getDouble(1);
			}
			rs.close();
			ps.close();
			return resultado;
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 
	 */
	public int obtenerTipoConceptoTesoreria(Connection con, String concepto, int institucion) 
	{
		return SqlBaseUtilidadesDao.obtenerTipoConceptoTesoreria(con, concepto,institucion);
	}

	/**
	 * 
	 */
	public boolean reciboCajaConPagosGeneralEmpresa(Connection con, String reciboCaja, int institucion) 
	{
		return SqlBaseUtilidadesDao.reciboCajaConPagosGeneralEmpresa(con, reciboCaja,institucion);
	}
	/**
	 * Mï¿½todo implementado para disminuir el acumulado de PYP por solicitud 
	 * @param con
	 * @param numeroSolicitud
	 * @param centroAtencion
	 * @param secuencia
	 * @param fechaSalPac --> dd/mm/yyyy
	 * @return
	 */
	public  int disminuirAcumuladoPYP(Connection con,String numeroSolicitud,String centroAtencion, String fechaSalPac)
	{
		String secuencia = "nextval('seq_acum_act_prog_pac_pyp')";
		 return SqlBaseUtilidadesDao.disminuirAcumuladoPYP(con, numeroSolicitud, centroAtencion, secuencia, fechaSalPac);
	}
	
	

	/**
	 * 
	 * @param con
	 * @param servicio
	 * @param esServicio
	 * @return
	 */
	public int obtenerEsquemasTarifarioServicioArticulo(Connection con,String subCuenta,int contrato, int servart, boolean esServicio,String fechaCalculoVigencia, int centroAtencion) throws BDException
	{
		return SqlBaseUtilidadesDao.obtenerEsquemasTarifarioServicioArticulo(con,subCuenta,contrato,servart,esServicio,fechaCalculoVigencia, centroAtencion);
	}
	
	/**
	 * Metodo que dada una fechaEvaluar/horaEvaluar, evalua que este entre la fechaInicial - HoraInicial y FechaFinal - Hora Final,
	 * NOTA LAS FECHAS DEBEN ESTAR EN FORMATO DD/MM/AAAA 
	 * @return
	 */
	
	
	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @param esServicios
	 * @return
	 */
	public boolean requiererJustificacionServiciosArticulos(Connection con, int codigoIngreso, boolean esServicios) 
	{
		return SqlBaseUtilidadesDao.requiererJustificacionServiciosArticulos(con, codigoIngreso,esServicios);
	}

	/**
	 * 
	 */
	public int obtenerCodigoContratoSubcuenta(Connection con, double subCuenta) 
	{
		return SqlBaseUtilidadesDao.obtenerCodigoContratoSubcuenta(con, subCuenta);
	}
	
	/**
	 * Metodo encargado de  obtener los tipos de convenio
	 * en un arrayList de Hashmap
	 * @param con
	 * @return
	 */
	public  ArrayList obtenerTiposConvenio(Connection con, String institucion)
	{
		return SqlBaseUtilidadesDao.obtenerTiposConvenio(con, institucion);
	}

	/**
	 * Metodo encargado de obtener las empresas institucion en un HashMap
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap obtenerEmpresasInstitucion(Connection con, int institucion) 
	{
		return SqlBaseUtilidadesDao.obtenerEmpresasInstitucion(con,institucion);
	}
	
	/**
	 * Metodo que retorna la razon social (Descripcion) de la instirucion 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public String obtenerNombreInstitucion(Connection con, int institucion) {
		return SqlBaseUtilidadesDao.obtenerNombreInstitucion(con,institucion);
	}

	/**
	 * Metodo que consulta los motivos de apertura o de cierre 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerMotivosAperturaCierre(Connection con, String tipo) {
		return SqlBaseUtilidadesDao.obtenerMotivosAperturaCierre(con,tipo);
	}
	
	/**
	 * Metodo consulta Ultimo egreso segun via ingreso para evolucion
	 * @param con
	 * @param codigoPaciente
	 * @param viaIngreso
	 * @return
	 */
	public HashMap consultaUltimoEgresoEvolucion(Connection con, int codigoPaciente, int viaIngreso)
	{
		return SqlBaseUtilidadesDao.consultaUltimoEgresoEvolucion(con, codigoPaciente, viaIngreso, DaoFactory.POSTGRESQL);
	}
	
	/**
	 * Metodo que consulta los usuarios de apertura o de cierre de ingresos 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerUsuarioAperturaCierre(Connection con, String tipo) {
		return SqlBaseUtilidadesDao.obtenerUsuarioAperturaCierre(con,tipo);
	}
	
	/**
	 * Metodo para marcar un Reingreso
	 * @param con
	 * @param codIngreso
	 * @param codPaciente
	 * @return
	 */
	public boolean marcarReingreso(Connection con, int codIngreso, int codPaciente)
	{
		return SqlBaseUtilidadesDao.marcarReingreso(con, codIngreso, codPaciente, DaoFactory.POSTGRESQL);
	}
	
	/**
	 * Consulta la informacion de la Unidades de Campo
	 * @param Connection con
	 * @param HasMap parametros
	 * */
	public ArrayList consultarUnidadesCampoParam(Connection con, HashMap parametros)
	{
		return SqlBaseUtilidadesDao.consultarUnidadesCampoParam(con, parametros);		
	}
	
	/**
	 * Consulta la informaciï¿½n de Tipo Campos
	 * @param Connection con
	 * @param HasMap parametros
	 * */
	public ArrayList consultarTiposCamp(Connection con, HashMap parametros)
	{	
		return SqlBaseUtilidadesDao.consultarTiposCamp(con, parametros);
	}
	
	/**
	 * Metodo consulta Centros de Costo por via de ingreso mas filtros
	 * @param con
	 * @param viaIngreso
	 * @param tipoPaciente
	 * @return
	 */
	public HashMap consultaCentrosCostoFiltros(Connection con)
	{
		return SqlBaseUtilidadesDao.consultaCentrosCostoFiltros(con);
	}
	
	/**
	 * Metodo consulta los componentes parametrizables
	 * @param con
	 * @param viaIngreso
	 * @param tipoPaciente
	 * @return
	 */
	public ArrayList consultarComponentesParam(Connection con, String funParam, HashMap parametros) 
	{
		return SqlBaseUtilidadesDao.consultarComponentesParam(con, funParam, parametros);
	}
	
	/**
	 * Metodo consulta las escalas parametrizables.
	 * @param con
	 * @param viaIngreso
	 * @param tipoPaciente
	 * @return
	 */
	public ArrayList consultarEscalasParam(Connection con, HashMap parametros) 
	{
		return SqlBaseUtilidadesDao.consultarEscalasParam(con, parametros);
	}
	
	/**
	 * Metodo consulta Tipo de Monitoreo por Centro de COsto
	 * @param con
	 * @param centroCosto
	 * @return
	 */
	public HashMap consultaTipoMonitoreoxCC(Connection con, int centroCosto)
	{
		return SqlBaseUtilidadesDao.consultaTipoMonitoreoxCC(con, centroCosto);
	}
	
	/**
	 * Metodo inserta registro Ingreso por Cuidados Especiales
	 * @param con
	 * @param ingreso
	 * @param estado
	 * @param indicativo
	 * @param tipoMonitoreo
	 * @param usuario
	 * @return
	 */
	public boolean insertarIngresoCuidadosEspeciales(Connection con, int ingreso, String estado, String indicativo, int tipoMonitoreo, String usuario, int evolucion, int valoracion, String centroCosto)
	{
		String secuenciaStt=" nextval('historiaclinica.seq_ingres_cuidados_especial') ";
		return SqlBaseUtilidadesDao.insertarIngresoCuidadosEspeciales(con, ingreso, estado, indicativo, tipoMonitoreo, usuario, evolucion, valoracion,centroCosto, strIsertarIngresoCuidadosEspeciales, secuenciaStt);
	}
	
	/**
	 * Metodo actualiza el estado a Finalizado de un Ingreso de Cuidados Especiales
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public boolean actualizarEstadoCuidadosEspeciales(Connection con, HashMap campos)
	{
		return SqlBaseUtilidadesDao.actualizarEstadoCuidadosEspeciales(con, campos);
	}
	
	/**
	 * Metodo verificar Ingreso Cuidados Especiales y requiere valoracion
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public int verificarValoracionIngresoCuidadosEspeciales(Connection con, int ingreso)
	{
		return SqlBaseUtilidadesDao.verificacionValoracionIngresoCuidadosEspeciales(con, ingreso);
	}
	
	/**
	 * Metodo de consulta Tipo de Monitoreo por Ingreso de Cuidados Especiales
	 * @param ingreso
	 * @return
	 */
	public String consultaTipoMonitoreoxCE(int ingreso)
	{
		return SqlBaseUtilidadesDao.consultaTipoMonitoreoxCE(ingreso);
	}
	
	/**
	 * Metodo encargado de consultar los centros de costo
	 * y devolverlos en un ArrayList de HashMap con los keys
	 * codigo,nombre.
	 * @author Jhony Alexander Duque A.
	 * Se puede filtrar por los siguientes criterios:
	 * -- viaIngreso --> Opcional 
	 * -- centroAtencion --> Opcional
	 * -- institucion --> Requerido
	 * -- tipoPaciente --> Opcional
	 * -- tipoMonitoreo --> Opcional
	 * -- filtroTipoMonitoreo --> Requerido
	 * @param con
	 * @param viaIngreso
	 * @param centroAtencion
	 * @param institucion
	 * @param tipoPaciente
	 * @param tipoMonitoreo
	 * @param filtroTipoMonitoreo
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> getCentrosCosto(Connection con,String viaIngreso,int centroAtencion,int institucion, String tipoPaciente, int tipoMonitoreo, boolean filtroTipoMonitoreo,boolean noTipoMonitoreo, boolean consultarViaIngreso)
	{
		return SqlBaseUtilidadesDao.getCentrosCosto(con, viaIngreso, centroAtencion, institucion, tipoPaciente, tipoMonitoreo,filtroTipoMonitoreo,noTipoMonitoreo,consultarViaIngreso);
	}
	
	/**
	 * Metodo encargado de devolver los tipo de paciente
	 * segun la via de ingreso en un arraylist
	 * @param con
	 * @param viaIngreso 
	 * @param institucion
	 * @return
	 */
	public ArrayList obtenerTiposPacientePorViaIngreso(Connection con, String viaIngreso)
	{
		return SqlBaseUtilidadesDao.obtenerTiposPacientePorViaIngreso(con, viaIngreso);
	}
	
	/**
	 * Metodo encargado de devolver los motivos de devolucion
	 * en un arraylist
	 * @param con
	 * @param viaIngreso 
	 * @param institucion
	 * @return
	 */
	public ArrayList obtenerMotivosDevolucionInventarios(Connection con, int institucion)
	{
		return SqlBaseUtilidadesDao.obtenerMotivosDevolucionInventarios(con, institucion);
	}

	/**
	 * 
	 */
	public int obtenerGrupoServicio(Connection con, int servicio) 
	{
		return SqlBaseUtilidadesDao.obtenerGrupoServicio(con, servicio);
	}

	/**
	 * 
	 */
	public int obtenertipoTarifarioEsquema(Connection con, int esquemaTarifario) 
	{
		return SqlBaseUtilidadesDao.obtenertipoTarifarioEsquema(con, esquemaTarifario);
	}
	
	/**
	 * Consulta cuantas solicitudes se encuentran o no en los estado medicos dados a partir de una cuenta
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public int consultarCuantosSolicitudesEstadoMedico(Connection con, HashMap parametros)
	{
		return SqlBaseUtilidadesDao.consultarCuantosSolicitudesEstadoMedico(con, parametros);		
	}
	
	/**
	 * Metodo encargado de devolver los motivos de devolucion
	 * en un arraylist
	 * @param con
	 * @param institucion
	 * @return
	 */
	public ArrayList obtenerNaturalezasArticulo(Connection con, int institucion)
	{
		return SqlBaseUtilidadesDao.obtenerNaturalezasArticulo(con, institucion);
	}

	/**
	 * 
	 */
	public String esMedicamento(Connection con, String acronimo, int Institucion){
		return SqlBaseUtilidadesDao.esMedicamento(con, acronimo, Institucion);
	}
	
	/**
	 * Metodo implementado para traer las
	 * naturalezas de articulo filtradas por
	 * la institucion
	 * @param con
	 * @return
	 */
	public String obtenerNombreNaturalezasArticulo(Connection con, String acronimoNaturaleza)
	{
		return SqlBaseUtilidadesDao.obtenerNombreNaturalezasArticulo(con, acronimoNaturaleza);
	}
	
	
	/**
	 * Metodo encargado de consultar el nombre del piso
	 * @param con
	 * @param codigoPiso
	 * @return
	 */
	public String obtenerNombrePiso(Connection con, String codigoPiso)
	{
		return SqlBaseUtilidadesDao.obtenerNombrePiso(con, codigoPiso);
	}
	

	/**
	 * Metodo encargado de consultar el nombre del estado de la cama
	 * @param con
	 * @param codigoPiso
	 * @return
	 */
	public String obtenerNombreEstadoCama (Connection con, String codigoEstadoCama)
	{
		return SqlBaseUtilidadesDao.obtenerNombreEstadoCama(con, codigoEstadoCama);
	}
	
	/**
	 * Metodo encargado de consultar la cantidad de camas
	 * de una institucion
	 * @param con
	 * @return
	 */
	public int obtenerNumeroCamas(Connection con,String institucion, String centroAtencion)
	{
		return SqlBaseUtilidadesDao.obtenerNumeroCamas(con, institucion, centroAtencion);
	}
	
	/**
	 * Metodo encargado de consultar el codigo del centro
	 * de costo principal segun el centro de costo solicitado
	 * @param con
	 * @param centroCosto
	 * @return
	 */
	public String obtenerCentroCostoPrincipal(Connection con, String centroCosto, String institucion)
	{
		return SqlBaseUtilidadesDao.obtenerCentroCostoPrincipal(con, centroCosto, institucion);
	}
	
	/**
	 * Metodo de consulta los totales de una factura para su Impresion
	 * @param con
	 * @param codFactura
	 * @return
	 */
	public HashMap consultaTotalesFactura(Connection con, int codFactura)
	{
		return SqlBaseUtilidadesDao.consultaTotalesFactura(con, codFactura);
	}
	
	
	/**
	 * Metodo encargado de verificar si existe evolucion con 
	 * orden de salida por Admision de Urgencias
	 * @param con
	 * @param codigoAdminUrg
	 * @param anioAdminUrg
	 * @return
	 */
	public boolean  tieneEvolucionConSalidaXAdminUrg  (Connection con, String codigoAdminUrg, String anioAdminUrg)
	{
		return SqlBaseUtilidadesDao.tieneEvolucionConSalidaXAdminUrg(con, codigoAdminUrg, anioAdminUrg);
	}
	
	
	/**
	 * Metodo encargado de identificar si un ingreso es reingreso;
	 * si es reingreso devuelve el ingreso asociado, si no devuelve -1
	 * @param connection
	 * @param ingreso
	 * @return
	 */
	public int esIngresoReingreso (Connection connection, String ingreso)
	{
		return SqlBaseUtilidadesDao.esIngresoReingreso(connection, ingreso);
	}
	
	/**
	 * Metodo encargado de consultar la descripcion del estado 
	 * de aplicacion de pagos segun el codigo del estado
	 * @param con
	 * @param centroCosto
	 * @return
	 */
	public String obtenerNombreEstadoAplicacionPagos(Connection con, int codigoEstado)
	{
		return SqlBaseUtilidadesDao.obtenerNombreEstadoAplicacionPagos(con, codigoEstado);
	}
	
	/**
	 * 
	 */
	public String obtenerNombreComponente(Connection con, String tipoComponente) 
	{
		return SqlBaseUtilidadesDao.obtenerNombreComponente(con, tipoComponente);
	}
	
	/**
	 * 
	 */
	public String obtenerNombrePlantilla(Connection con, String plantilla) 
	{
		return SqlBaseUtilidadesDao.obtenerNombrePlantilla(con, plantilla);
	}
	
	/**
	 * Consulta los Centros de Costo para Area de Asocio de Cuentas segun Ingreso Cuidados Especiales
	 * @param con
	 * @param centroAtencion
	 * @param cuenta
	 * @param egresoV
	 * @return
	 */
	public HashMap areasAsocioEspeciales(Connection con, int centroAtencion, int cuenta, boolean egresoV)
	{
		return SqlBaseUtilidadesDao.areasAsocioEspeciales(con, centroAtencion, cuenta, egresoV);
	}
	
	/**
	 * 
	 */
	public String obtenerDescripcionComponente(Connection con, String codigoComponente) 
	{
		return SqlBaseUtilidadesDao.obtenerDescripcionComponente(con, codigoComponente);
	}

	/**
	 * 
	 */
	public String obtenerDescripcionEscala(Connection con, String codigoEscala) 
	{
		return SqlBaseUtilidadesDao.obtenerDescripcionEscala(con, codigoEscala);
	}
	
	/**
	 * Metodo que consutla la Evolucion de la Cuenta de Urgencias
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public int consultaEvolucionCuentaUrgencias(Connection con, int cuenta)
	{
		return SqlBaseUtilidadesDao.consultaEvolucionCuentaUrgencias(con, cuenta);
	}
	
	/**
	 * Metodo que consulta la Valoracion de la Cuenta de Urgencias
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public int consultaValoracionCuentaUrgencias(Connection con, int cuenta)
	{
		return SqlBaseUtilidadesDao.consultaValoracionCuentaUrgencias(con, cuenta);
	}

	/**
	 * @param con
	 * @param institucion
	 * @return
	 */
	public ArrayList obtenerConceptosPagos(Connection con, int institucion)
	{
		return SqlBaseUtilidadesDao.obtenerConceptosPagos(con, institucion);
	}
	
	/**
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public int obtenerSubCuentaIngreso(Connection con, int ingreso)
	{
		return SqlBaseUtilidadesDao.obtenerSubCuentaIngreso(con, ingreso);
	}
	
	/**
	 * 
	 */
	public String obtenerNombreTipoMonitoreo(Connection con, String tipoMonitoreo) 
	{
		return SqlBaseUtilidadesDao.obtenerNombreTipoMonitoreo(con, tipoMonitoreo);
	}
	
	/**
	 * Mï¿½todo implementado para obtener el codigo del tralado de cama
	 * del ï¿½ltimo traslado de una cuenta de Hospitalizacion
	 * @param con
	 * @param cuenta
	 * @param adicion
	 * @return
	 */
	public int getCodigoTrasladoUltimoTraslado(Connection con,int cuenta)
	{
		return SqlBaseUtilidadesDao.getCodigoTrasladoUltimoTraslado(con, cuenta);
	}
	
	/**
	 * Metodo encargado de Actualizar la fecha y hora de finalizacion del 
	 * traslado de la cama
	 * @param connection
	 * @param codigoTrasladoCama
	 * @return
	 */
	public boolean actualizarFechaHoraActualizacion (Connection connection, HashMap datos)
	{
		return SqlBaseUtilidadesDao.actualizarFechaHoraActualizacion(connection, datos);
	}
	
	/**
	 * Metodo que consulta si un Ingreso es un Preingreso sin importar estado
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public boolean esPreingresoNormal(Connection con, int ingreso)
	{
		return SqlBaseUtilidadesDao.esPreingresoNormal(con, ingreso);
	}
	
	/**
	 * Metodo que consulta la descripcion de un tipo de convenio especifico
	 * @param con
	 * @param tipoConvenio
	 * @param codigoInstitucion
	 * @return
	 */
	public String obtenerDescripcionTipoConvenio(Connection con, String tipoConvenio, int codigoInstitucion)
	{
		return SqlBaseUtilidadesDao.obtenerDescripcionTipoConvenio(con, tipoConvenio, codigoInstitucion);
	}
	
	
	/**
	 * Metodo encargado de consultar los tipos de tratamiento
	 * odontologico
	 * @author Jhony Alexander Duque A.
	 * @param connection
	 * @param criterios
	 * --------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * --------------------------
	 * -- activo --> Opcional (S --> filtra los activos, N --> filtra inactivos, "" --> no filtra)
	 * -- codigo --> Opcional
	 * -- institucion --> Requerido
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerTiposTratamientoOdontologico (Connection connection,HashMap criterios)
	{
		return SqlBaseUtilidadesDao.obtenerTiposTratamientoOdontologico(connection, criterios);
	}
	
	/**
	 * Metodo encargado de obtener los tipos de Habitacion
	 * @author Jhony Alexander Duque A.
	 * @param connection
	 * @param institucion
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerTiposHabitacion (Connection connection,String institucion)
	{
		return SqlBaseUtilidadesDao.obtenerTiposHabitacion(connection, institucion);
	}
	
	/**
	 * Metodo para obtener el nombre del tipo de paciente
	 * @param con
	 * @param tipoPaciente
	 * @return
	 */
	public String obtenerNombreTipoPaciente(Connection con, String tipoPaciente)
	{
		return SqlBaseUtilidadesDao.obtenerNombreTipoPaciente(con, tipoPaciente);
	}
	
	/**
	 * Metodo para obtener los deudores segï¿½n el tipo de
	 * deudor seleccionado
	 */
	public ArrayList<HashMap<String, Object>> obtenerDeudores(Connection con, String tipoDeudor)
	{
		return SqlBaseUtilidadesDao.obtenerDeudores(con, tipoDeudor);
	}
	
	/**
	 * Mï¿½todo que indica si una Entidad Financiera se encuentra
	 * inactiva ï¿½ activa, segï¿½n el consecutivo de la entidad
	 * financiera
	 */
	public boolean obtenerActivoInactivoEntidadFinanciera(Connection con, int consecutivoEntidad)
	{
		return SqlBaseUtilidadesDao.obtenerActivoInactivoEntidadFinanciera(con, consecutivoEntidad);
	}
	
	/**
	 * Metodo encargado de obtener los grupos parametrizados en la funcionalidad transacciones validas por centro de costo concatenados por comas
	 * @param connection
	 * @param institucion
	 * @param centro_costo
	 * @param tipos_trans_inventario
	 * @param clase_inventario
	 * @return
	 */
	public String obtenerGruposTransValidasXCC (Connection connection,int institucion,int centro_costo,int tipos_trans_inventario, int clase_inventario)
	{
		return SqlBaseUtilidadesDao.obtenerGruposTransValidasXCC(connection,institucion,centro_costo,tipos_trans_inventario,clase_inventario);
	}
	
	/**
	 * Metodo encargado de consultar los terceros que se encuentren activos
	 * y no se encuentren relacionados a alguna empresa.
	 * @author Jhony Alexander Duque A.
	 * @param con
	 * @param institucion
	 * @return
	 * ArrayList<HashMap<String, Object>>
	 * -----------------------------------
	 * KEY'S DEL MAPA DENTRO DEL ARRAYLIST
	 * -----------------------------------
	 * codigo,numeroid,descripcion
	 */
	public ArrayList obtenerTercerosSinEmpresaInstitucionActivos(Connection con, int institucion)
	{
		return SqlBaseUtilidadesDao.obtenerTercerosSinEmpresaInstitucionActivos(con, institucion);
	}
	
	/**
	 * Metodo encargado de consultar las empresas parametrizadas en le sistema, con un deudor
	 * @author Jhony Alexander Duque A.
	 * @param con
	 * @param institucion
	 * @return ArrayList<HashMap<String, Object>>
	 * ----------------------------------------
	 * KEY'S DEL MAPA DENTRO DEL ARRAYLIST
	 * ----------------------------------------
	 * codigo,numeroid,descripcion
	 */
	public  ArrayList<HashMap<String, Object>> obtenerEmpresas(Connection con, String institucion)
	{
		return SqlBaseUtilidadesDao.obtenerEmpresas(con, institucion);
	}

	/**
	 * Metodo Que retorna el numero de la cuenta contable para un codigo de cuenta
	 * @author Andres Silva Monsalve
	 * @param cuentaConvenio
	 * @return
	 */
	public String obtenerCuentaContable(Connection con, String cuentaConvenio) 
	{
		return SqlBaseUtilidadesDao.obtenerCuentaContable(con, cuentaConvenio);
	}

	/**
	 * 
	 */
	public String obtenerNombreEmpresa(Connection con, String codigoEmpresa) 
	{
		return SqlBaseUtilidadesDao.obtenerNombreEmpresa(con, codigoEmpresa);
	}
	
	/**
	 * 
	 */
	public int obtenerCodigoProcedimientoPrincipalIncluidos(Connection con, String codigoServicio)
	{
		return SqlBaseUtilidadesDao.obtenerCodigoProcedimientoPrincipalIncluidos(con, codigoServicio);
	}

	/**
	 * @param con, codigoServiPpal, codigoServiInclu
	 * @return */
		
		public int obtenerCodigoServicioIncluido(Connection con, String codigoServiPpal, String codigoServiInclu)
		{
			return SqlBaseUtilidadesDao.obtenerCodigoServicioIncluido(con, codigoServiPpal, codigoServiInclu);
		}
	
	
	/**
	 * Metodo encargado de Consultar los subgrupos
	 * @author Jhony Alexander Duque A.
	 * @param con
	 * @param criterios
	 * --------------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * --------------------------------------
	 * -- codigo
	 * -- subgrupo
	 * -- grupo
	 * -- clase
	 * -- cuentaInventario
	 * -- cuentaCosto
	 * @return ARRAYLIST <HASHMAP< > >
	 * ---------------------------------------
	 * KEY'S DEL MAPA DENTRO DEL ARRAYLIST
	 * ---------------------------------------
	 * codigo,subgrupo,grupo,clase,nombre,
	 * cuentaInventario,cuentaCosto
	 */
	public ArrayList<HashMap<String, Object>> obtenerSubGrupo(Connection con, HashMap criterios)
	{
		return SqlBaseUtilidadesDao.obtenerSubGrupo(con, criterios);
	}
	/**
	 * Mï¿½todo encargado de averiguar si hay una descripciï¿½n de textos en la BD
	 * @autor Ing. Felipe Pï¿½rez G.
	 * @param con
	 * @param descripcionTexto
	 */
	
	public boolean existeDescripcionTextoProcedimiento (Connection con, String descripcionTexto, String servicio)
	{
		return SqlBaseUtilidadesDao.existeDescripcionTextoProcedimiento(con, descripcionTexto, servicio);
	}
	
	/**
	 * Metodo para Traer el codigo del Convenio a partir de un codigo Interfaz
	 * @author Andres Silva Monsalve
	 * @param codigoInterfaz
	 * @return
	 */
	public int obtenerCodigoConvenioDeCodInterfaz(Connection con, String codigoInterfaz) 
	{
		return SqlBaseUtilidadesDao.obtenerCodigoConvenioDeCodInterfaz(con, codigoInterfaz);
	}
	
	/**
	 * Metodo para Traer el codigo interfaz del Convenio a partir de un codigo del convenio
	 * @author Andres Silva Monsalve
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public String obtenerCodigoInterfazConvenioDeCodigo(Connection con, int codigoConvenio) 
	{
		return SqlBaseUtilidadesDao.obtenerCodigoInterfazConvenioDeCodigo(con, codigoConvenio);
	}
	
	/**
	 * Mï¿½todo que devuelve la fecha del ingreso
	 * segï¿½n un ingreso dado 
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public String obtenerFechaIngreso(Connection con, String idIngreso)
	{
		return SqlBaseUtilidadesDao.obtenerFechaIngreso(con, idIngreso);
	}
	
	/**
	 * Mï¿½todo que devuelve la hora del ingreso
	 * segï¿½n un ingreso dado 
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public String obtenerHoraIngreso(Connection con, String idIngreso)
	{
		return SqlBaseUtilidadesDao.obtenerHoraIngreso(con, idIngreso);
	}
	
	/**
	 * Mï¿½todo que devuelve el consecutivo de ingreso
	 * segï¿½n un ingreso dado 
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public String obtenerConsecutivoIngreso(Connection con, String idIngreso)
	{
		return SqlBaseUtilidadesDao.obtenerConsecutivoIngreso(con, idIngreso);
	}
	
	/**
	 * Mï¿½todo que devuelve el nombre del tipo
	 * de solicitud segï¿½n un tipo de solicitud
	 * dado
	 * @param con
	 * @param tipoSolicitud
	 * @return
	 */
	public String obtenerNombreTipoSolicitud(Connection con, String tipoSolicitud)
	{
		return SqlBaseUtilidadesDao.obtenerNombreTipoSolicitud(con, tipoSolicitud);
	}
	
	/**
	 * 
	 */
	public HashMap obtenerCentrosCostoTipoConsignacion(Connection con, int institucion, String tiposArea, boolean todos,int centroAtencion, boolean filtro_externos)
	{
		return SqlBaseUtilidadesDao.obtenerCentrosCostoTipoConsignacion(con,institucion,tiposArea,todos,centroAtencion, filtro_externos);
	}

	/**
	 * Consultar el Centro de Costo Interfaz a partir del Codigo del Centro de Costo. 
	 * Tener en cuenta para los centros de costo SubAlmacen enviar el codigoInterfaz del Centro de Costo Interfaz
	 * Octubre 20 de 2008
	 * @author Andres Silva M
	 * @param codigoCentroCosto
	 * @return
	 */
	public String obtenerCentroCostoInterfaz(Connection con, int codigoCentroCosto) 
	{
		return SqlBaseUtilidadesDao.obtenerCentroCostoInterfaz(con,codigoCentroCosto);
	}

	/**
	 * Consulta el NIT del Convenio por medio del codigo interfaz del mismo
	 * @author Andres Silva M --Oct 21 / 08
	 * @param con
	 * @param codigoInterfazConvenio
	 * @return
	 */
	public String obtenerNitConveniodeCodInterfaz(Connection con, String codigoInterfazConvenio) 
	{
		return SqlBaseUtilidadesDao.obtenerNitConveniodeCodInterfaz(con, codigoInterfazConvenio);
	}
	
	/**
	 * Metodo encargado de obtener los tipos de servicio
	 * de vehiculos
	 * @author Jhony Alexander Duque A.
	 * @param connection
	 * @param codigo --> Opcional
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerTiposServiciosVehiculos (Connection connection,String codigo)
	{
		return SqlBaseUtilidadesDao.obtenerTiposServiciosVehiculos(connection, codigo);
	}

	/**
	 * Mï¿½todo encargado de traer los tipos de regimen
	 * segï¿½n el filtro
	 * @author Carlos Mauricio Jaramillo H.
	 * @param con
	 * @param todos
	 * @param codigoFiltros
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerTiposRegimen(Connection con, boolean todos, String codigoFiltros)
	{
		return SqlBaseUtilidadesDao.obtenerTiposRegimen(con, todos, codigoFiltros);
	}

	/**
	 * Consulta el NIT del convenio por medio del codigo de la factura
	 * @author Andres Silva M --Oct 24 /08
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public String obtenerNitConveniodeCodfactura(Connection con, int codigoFactura) 
	{
		return SqlBaseUtilidadesDao.obtenerNitConveniodeCodfactura(con, codigoFactura);
	}

	/**
	 * Consulta el codigo Interfaz del tipo de Identificacion
	 * @author Andres Silva Monsalve --Oct 25/08
	 * @param codigoTipoIdentificacionPersona
	 * @return
	 */	
	public String codigoInterfaztipoIdentificacion(Connection con, 	String codigoTipoIdentificacionPersona) 
	{
		return SqlBaseUtilidadesDao.codigoInterfaztipoIdentificacion(con, codigoTipoIdentificacionPersona);
	}

	/**
	 * Consulta para saber si el paciente tiene informacion en la historia del sistema anterior
	 * @author Andres Silva M
	 * @param con
	 * @param tipoIdentificacion
	 * @param numeroIdentificacion
	 * @return
	 */
	public boolean existeHistoriaSistemaAnterior(Connection con, String tipoIdentificacion, String numeroIdentificacion) 
	{
		return SqlBaseUtilidadesDao.existeHistoriaSistemaAnterior(con, tipoIdentificacion, numeroIdentificacion);
	}
	
	/**
	 * Metodo encargado de consultar las instituciones Sirc
	 * pudiendo filtrar por diferentes criterios
	 * --------------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * --------------------------------------
	 * --institucion --> Requerido
	 * --nivelServicio --> Opcional
	 * --tipoRed --> Opcional
	 * --tipoInstReferencia --> Opcional
	 * --tipoInstAmbulancia --> Opcional
	 * --activo --> Opcional
	 * @param connection
	 * @param criterios
	 * @return ArrayList<HashMap>
	 * --------------------------------------
	 * KEY'S DEL MAPA DENTRO DEL ARRAYLIST
	 * --------------------------------------
	 * codigo,descripcion,tipoRed,
	 * tipoInstReferencia,tipoInstAmbulancia,
	 * nivelServicio, activo
	 */
	public ArrayList<HashMap<String, Object>> obtenerInstitucionesSirc(Connection connection,HashMap criterios)
	{
		return SqlBaseUtilidadesDao.obtenerInstitucionesSirc(connection, criterios);
	}
	
	/**
	 * * Consulta codigo Interfaz del centro de costo subalmacen en Parametros Almacen
	 * @author Andres Silva M Oct28/08
	 * @param con
	 * @param codigoAlmacen
	 * @return
	 */
	public String obtenerCodigoInterfazParametroAlmacen(Connection con, int codigoAlmacen) 
	{
		return SqlBaseUtilidadesDao.obtenerCodigoInterfazParametroAlmacen(con, codigoAlmacen);
	}

	/**
	 * Consulta el Codigo Interfaz para un Articulo
	 * @author Andres Silva M Oct30/08
	 * @param codigoArticulo
	 * @return
	 */
	public String obtenerCodigoInterfazArticulo(Connection con, int codigoArticulo) 
	{
		return SqlBaseUtilidadesDao.obtenerCodigoInterfazArticulo(con, codigoArticulo);
	}

	/**
	 * Consulta el Codigo del Articulo a partir del codigo interfaz del mismo
	 * @author Andres Silva M Oct30/08
	 * @param codigoInterfazArticulo
	 * @return
	 */
	public int obtenerCodigoArticulodeCodInterfaz(Connection con, String codigoInterfazArticulo) 
	{
		return SqlBaseUtilidadesDao.obtenerCodigoArticulodeCodInterfaz(con, codigoInterfazArticulo);
	}

	/**
	 * Consulta el codigo del centro de costo asociado al interfaz del almacen en la tabla parametros almacen
	 * @author Andres Silva Monsalve Oct30/08
	 * @param codigoInterfazAlmacen
	 * @return
	 */
	public int obtenerAlmacenDeCodigoInterfaz(Connection con, String codigoInterfazAlmacen) 
	{
		return SqlBaseUtilidadesDao.obtenerAlmacenDeCodigoInterfaz(con, codigoInterfazAlmacen);
	}

	/**
	 * Consultar Consecutivo de la Transaccion Inventarios a Partir del Codigo
	 * @author Andres Silva M Oct30/08
	 * @param codigoTrans
	 * @return
	 */
	public int obtenerConsecutivoDeTransaccionInv(Connection con, String codigoTrans) 
	{
		return SqlBaseUtilidadesDao.obtenerConsecutivoDeTransaccionInv(con, codigoTrans);
	}
	
	/**
	 * Metodo encargado de identificar si un ingreso tiene facturas
	 * @param connection
	 * @param ingreso
	 * @return
	 */
	public int esIngresoFacturado(Connection connection, String ingreso)
	{
		return SqlBaseUtilidadesDao.esIngresoFacturado(connection, ingreso);
	}
	
	/**
	 * Consulta el Codigo del Tercero A partir del Numero de Identificacion del Mismo
	 * @author Andres Silva Monsalve Oct31/08
	 * @param numeroIdTercero
	 * @return
	 */
	public int obtenercodigoTercerodeNumeroIdentificacion(Connection con, String numeroIdTercero) 
	{
		return SqlBaseUtilidadesDao.obtenercodigoTercerodeNumeroIdentificacion(con, numeroIdTercero);
	}

	/**
	 * Pregunta Si el login del usuario Existe en Axioma
	 * @author Andres Silva M Nov06/08
	 * @param loginUsuario
	 * @return
	 */
	public boolean existeUsuario(Connection con, String loginUsuario) 
	{
		return SqlBaseUtilidadesDao.existeUsuario(con, loginUsuario);
	}
	
	/**
	 * 
	 */
	public HashMap<String, Object> consultarTransaccionesInventarios(Connection con, String consecutivo, boolean todos, boolean validarCodigos, String codigosValidados)
	{
		return SqlBaseUtilidadesDao.consultarTransaccionesInventarios(con, consecutivo, todos, validarCodigos, codigosValidados);
	}
	
	/**
	 * Metodo encargado de obtener el tipo de sala standar
	 * que esta en el grupo del servicio
	 * @param con
	 * @param codigoServicio
	 * @return
	 */
	public int obtenerTipoSalaStandar(Connection con, String codigoServicio ) 
	{
		return SqlBaseUtilidadesDao.obtenerTipoSalaStandar(con, codigoServicio);
	}

	/**
	 * Metodo para Consultar el Consecutivo del Ingreso de un paciente y el Codigo Persona a partir de un Numero de Pedido QX
	 * @param con
	 * @param numeroPedido
	 * @return
	 */
	public HashMap consultarIngresoYpersonadeunPedidoQx(Connection con, int numeroPedido) 
	{
		return SqlBaseUtilidadesDao.consultarIngresoYpersonadeunPedidoQx(con, numeroPedido);
	}
	
	/**
	 * Metodo encargado de consultar el nombre de la 
	 * salida del paciente.
	 * @param con
	 * @param codigo
	 * @return
	 */
	public String obtenerNombreSalidaPaciente(Connection con, String codigo ) 
	{
		return SqlBaseUtilidadesDao.obtenerNombreSalidaPaciente(con, codigo);
	}
	
	/**
	 * Metodo encargado de consultar el codigo de un servicio 
	 * dependiendo del tarifario y del codigo.
	 * @param con
	 * @param codigo
	 * @param tarifario
	 * @return
	 */
	public int obtenerCodigoServicio(Connection con, String codigo,String tarifario ) 
	{
		return SqlBaseUtilidadesDao.obtenerCodigoServicio(con, codigo, tarifario);
	}
	
	
	/**
	 * Mï¿½todo que devuelve la sumatoria del valor total cargado
	 * de los N paquetes que tiene uns subcuenta
	 * @param con
	 * @param subCuenta
	 */
	public double obtenerValorTotalPaquetesResponsable(Connection con, String subCuenta) throws BDException
	{
		return SqlBaseUtilidadesDao.obtenerValorTotalPaquetesResponsable(con, subCuenta);
	}

	/**
	 * 
	 */
	public String obtenerNitProveedor(Connection con, String codigoTercero) 
	{
		return SqlBaseUtilidadesDao.obtenerNitProveedor(con, codigoTercero);
	}

	/**
	 * Verificar si Abono generado por otro sistema existe en Axioma
	 * @param con
	 * @param reciboCaja
	 * @param tipoMovimiento
	 * @return
	 */
	public boolean existeRegistroAbonosRC(Connection con, String reciboCaja, String tipoMovimiento) 
	{
		return SqlBaseUtilidadesDao.existeRegistroAbonosRC(con, reciboCaja,tipoMovimiento);
	}
	
	/**
	 * 
	 */
	public String asignarValorPacienteValorAbonos(Connection con, String codigoConvenio)
	{
		return SqlBaseUtilidadesDao.asignarValorPacienteValorAbonos(con, codigoConvenio);
	}
	
	/**
	 * Metodo que consulta todos los Rublos Presupuestales
	 * @param con
	 * @param anioVigenciaRublo
	 * @param codigoRublo
	 * @param descripcionRublo
	 * @param institucion
	 * @return
	 */
	public HashMap obtenerRublosPresupuestales(Connection con, String anioVigenciaRublo, String codigoRublo, String descripcionRublo, int institucion) 
	{
		return SqlBaseUtilidadesDao.obtenerRublosPresupuestales(con, anioVigenciaRublo,codigoRublo,descripcionRublo, institucion);
	}
	
	/**
	 * Mï¿½todo encargado de Obtener el codigo de un centro de atenciï¿½n dado su nombre
	 * @author Felipe Pï¿½rez Granda
	 * @param con
	 * @param nombreCentroAtencion
	 * @return int codigoCentroAtencion
	 */
	public int obtenerCodigoCentroAtencion(Connection con, String nombreCentroAtencion)
	{
		return SqlBaseUtilidadesDao.obtenerCodigoCentroAtencion(con, nombreCentroAtencion);
	}
	
	/**
	 * Encargado de devolver la via de ingreso de una cuenta.
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public HashMap obtenerViaIngresoCuenta(Connection con, int cuenta) 
	{
		return SqlBaseUtilidadesDao.obtenerViaIngresoCuenta(con, cuenta);
	}
	
	/**
	 * Encargado de devolver las solicitudes de procedimientos, cirugias, interconsultas, cargos directos y asociados a una factra
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public HashMap obtenerSolicitudesFacturas(Connection con, int codFactura)
	{
		return SqlBaseUtilidadesDao.obtenerSolicitudesFacturas(con, codFactura);
	}
	
	/**
	 * Metodo encargado de consultar los datos del Centro
	 * de Atencion, pudiendo se filtrar por diferentes criterios.
	 * @param connection
	 * @param criterios
	 * --------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * --------------------------------
	 * -- consecutivo --> Requerido
	 * -- institucion --> Opcional
	 * -- codigo --> Opcional
	 * -- activo --> Opcional
	 * @return Mapa
	 * ---------------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * ---------------------------------
	 * codigo,descripcion,codigo_inst_sirc,
	 * empresa_institucion,direccion
	 */
	public HashMap obtenerDatosCentroAtencion (Connection connection,HashMap criterios)
	{
		return SqlBaseUtilidadesDao.obtenerDatosCentroAtencion(connection, criterios);
	}

	/**
	 * 
	 */
	public boolean insertarRegistrosInterfazWINLAB(Connection con,ArrayList consecutivos, HashMap matrizLaboratorios) 
	{
		return SqlBaseUtilidadesDao.insertarRegistrosInterfazWINLAB(con,consecutivos,matrizLaboratorios);
	}

	/**
	 * 
	 */
	public String obtenerCodigoPisoCama(Connection con, String codigoCama) 
	{
		return SqlBaseUtilidadesDao.obtenerCodigoPisoCama(con,codigoCama);
	}

	/**
	 * 
	 */
	@Override
	public HashMap consultarEncabezadoLaboratoriosPendientesWINLAB(Connection con) 
	{
		return SqlBaseUtilidadesDao.consultarEncabezadoLaboratoriosPendientesWINLAB(con);
	}

	/**
	 * 
	 */
	@Override
	public HashMap consultarDetalleLaboratoriosPendientesWINLAB(Connection con,String consecutivo) 
	{
		return SqlBaseUtilidadesDao.consultarDetalleLaboratoriosPendientesWINLAB(con,consecutivo);
	}

	/**
	 * 
	 */
	@Override
	public boolean eliminarLaboratoriosPendientesWINLAB(Connection con,String consecutivos) 
	{
		return SqlBaseUtilidadesDao.eliminarLaboratoriosPendientesWINLAB(con,consecutivos);
	}
	
	/**
	 * Consulta si un centro de costos esta parametrizado como un Registro Respuesta Proce X Terceros
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public String esCentroCostoRespuestaProcTercero(Connection con, HashMap parametros)
	{
		return SqlBaseUtilidadesDao.esCentroCostoRespuestaProcTercero(con, parametros);
	}	
	
	/**
	 * Metodo encargado de verificar si existe algun registro de la cuenta
	 * en la tabla egresos.
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public boolean existeAlgunRegistroEgreso(Connection con, int cuenta)
	{
		return SqlBaseUtilidadesDao.existeAlgunRegistroEgreso(con, cuenta);
	}
	
	/**
	 * Metodo encargado de consultar las empresas parametrizadas en el sistema
	 * @param con
	 * @param HashMap parametros
	 * @return ArrayList<HashMap<String, Object>>
	 * ----------------------------------------
	 * KEY'S DEL MAPA DENTRO DEL ARRAYLIST
	 * ----------------------------------------
	 * codigo,numeroid,descripcion
	 */
	public ArrayList<HashMap<String, Object>> obtenerEmpresas(Connection con,HashMap parametros)
	{
		return SqlBaseUtilidadesDao.obtenerEmpresas(con, parametros);
	}
	
	/**
	 * Metodo encargado para consultar el codigo de clase inventario de un articulo especifico
	 * @param con
	 * @param articulo
	 * @return
	 */
	public String consultarClaseInterfazArticulo(Connection con, String articulo) 
	{	
		return SqlBaseUtilidadesDao.consultarClaseInterfazArticulo(con, articulo);
	}

	/**
	 * Metodo encargado para consultar el tipo de entidad que ejecuta con el codigo del CC
	 * @param con
	 * @param articulo
	 * @return
	 */
	public String getTipoEntidadEjecutaCC(Connection con, String codigoCC) 
	{
		return SqlBaseUtilidadesDao.getTipoEntidadEjecutaCC(con, codigoCC);
	}
	
	/**
	 * Metodo para obtener el CC solicitado de la solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public String obtenerCCSolicitadoSolicitud(Connection con, String numeroSolicitud) 
	{
		return SqlBaseUtilidadesDao.obtenerCCSolicitadoSolicitud(con, numeroSolicitud);
	}
	
	/**
	 * Metodo para obtener si existe o no una autorizacion de entidad subcontratada para la solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean existeAutorizacionSolicitud(Connection con, String numeroSolicitud) 
	{
		return SqlBaseUtilidadesDao.existeAutorizacionSolicitud(con, numeroSolicitud);
	}
	
	/**
	 * Metodo para obtener el CC solicitado de la solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public String obtenerTipoAutorizacionEntidadSubcontratada(Connection con, String numeroSolicitud) 
	{
		return SqlBaseUtilidadesDao.obtenerTipoAutorizacionEntidadSubcontratada(con, numeroSolicitud);
	}
	
	/**
	 * Metodo para obtener si existe o no una autorizacion de entidad subcontratada para la solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public String verificarregistroUsuarioEntidadSubcontratada(Connection con, String numeroSolicitud, String login) 
	{
		return SqlBaseUtilidadesDao.verificarregistroUsuarioEntidadSubcontratada(con, numeroSolicitud, login);
	}
	
	/**
	 * Esta sentencia modifica la definiciï¿½n del motor de BD, para que trabaje con Fechas en formato YYYY-DD-MM
	 * @return
	 */
	public boolean actualizarFormatoFechaBD() 
	{
		return true;
	}
	
	/**
	 * Metodo para obtener si existe o no una autorizacion de entidad subcontratada para la solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public HashMap obtenerCuentaContableXCodigo(Connection con, int codigoInstitucion, String cuentaContable) 
	{
		return SqlBaseUtilidadesDao.obtenerCuentaContableXCodigo(con, codigoInstitucion, cuentaContable);
	}

	@Override
	public ArrayList<ArrayList<Object>> listarProgramasPyP(Connection con, boolean activo, int codigoInstitucion) {
		return SqlBaseUtilidadesDao.listarProgramasPyP(con, activo, codigoInstitucion);
	}

	/**
	 *  Metodo para verificar si un convenio tiene chekeado Reporte de Atencion de Urgencias
	 */
	public boolean esConvenioConReportAtencionUrg(Connection con,String codConvenio) {
		
		return SqlBaseUtilidadesDao.esConvenioConReportAtencionUrg(con,codConvenio);
	}

	@Override
	public boolean betweenFechas(String fechaEvaluar, String horaEvaluar,
			String fechaInicial, String horaInicial, String fechaFinal,
			String horaFinal) {
		
		if(horaEvaluar.length()==4)
			horaEvaluar="0"+horaEvaluar;
		if(horaInicial.length()==4)
			horaInicial="0"+horaInicial;
		if(horaFinal.length()==4)
			horaFinal="0"+horaFinal;
		String consulta="select 1 where ('"+fechaEvaluar+"'='"+fechaInicial+"' and '"+horaEvaluar+"'>='"+horaInicial+"') or " +
								" ('"+fechaEvaluar+"'>'"+fechaInicial+"' and '"+fechaEvaluar+"'<'"+horaEvaluar+"') or " +
								" ('"+fechaEvaluar+"'='"+fechaFinal+"' and '"+horaEvaluar+"' <= '"+horaFinal+"')";
		
		return SqlBaseUtilidadesDao.betweenFechas(consulta);
	}
	
	/**
	 * Mï¿½todo que consulta los dias de la semana
	 * @param con	
	 * @return ArrayList<HashMap>
	 * @author Vï¿½ctor Gï¿½mez 
	 */
	public ArrayList<HashMap> obtenerDiasSemanaArray(Connection con)
	{
		return SqlBaseUtilidadesDao.obtenerDiasSemanaArray(con);
	}

	@Override
	public ArrayList<HashMap<String, Object>> consultarParentezcos(Connection con) {
		
		return SqlBaseUtilidadesDao.consultarParentezcos(con);
	}

	@Override
	public ArrayList<HashMap<String, Object>> consultarMotivosCita(Connection con, int codInstitucion,String tipoMotivo) {
		
		return SqlBaseUtilidadesDao.consultarMotivosCita(con, codInstitucion,tipoMotivo);
	}

	@Override
	public ArrayList<HashMap<String, Object>> consultarMediosConocimientoServ(Connection con, int codInstitucion) {
		
		return SqlBaseUtilidadesDao.consultarMediosConocimientoServ(con,codInstitucion);
	}
	
	/**
	 * Obtener los convenios del parametro general "Convenios a mostrar por defecto en el presupuesto odontolÃ³gico"
	 * 
	 * Keys del mapa
	 * 		- convenio
	 * 		- eliminado
	 * 
	 * @param con
	 * @param codInstitucion
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerConveniosAMostrarPresupuestoOdo(Connection con, int codInstitucion){
		return SqlBaseUtilidadesDao.obtenerConveniosAMostrarPresupuestoOdo(con,codInstitucion);
	}
	
	/**
	 * Actualizar el parametro general "Convenios a mostrar por defecto en el presupuesto odontolÃ³gico"
	 * @param con
	 * @param conveniosAMostrarPresupuestoOdo
	 */
	public Object actualizarConveniosAMostrarPresupuestoOdo(Connection con,ArrayList<HashMap<String, Object>> conveniosAMostrarPresupuestoOdo, String usuario, int codigoInstitucion){
		return SqlBaseUtilidadesDao.actualizarConveniosAMostrarPresupuestoOdo(con,conveniosAMostrarPresupuestoOdo, usuario, codigoInstitucion);
	}
	
	/**
	 * Obtener informacion sobre la cuenta solicitante de una orden ambulatoria
	 * @param con
	 * @param codigoOrden
	 * @return
	 */
	public HashMap obtenerInfoCuentaSolicitanteOrdenAmbulatoria(Connection con, int codigoOrden){
		return SqlBaseUtilidadesDao.obtenerInfoCuentaSolicitanteOrdenAmbulatoria(con,codigoOrden);
	}
	
	/**
	 * M&eacute;todo que retorna un ArrayList con las descripciones de los acronimos pasados en el arreglo como par&aacute;metro.
	 * 
	 * @param con
	 * @param listaConstantes
	 * @param ordenar
	 * @return ArrayList<DtoIntegridadDominio> con los acr&oacute;nimos y las descripciones respectivas.
	 */
	@Override
	public ArrayList<DtoIntegridadDominio> generarListadoConstantesIntegridadDominio(Connection con, String[] listaConstantes, boolean ordenar) {
		return SqlBaseUtilidadesDao.generarListadoConstantesIntegridadDominio(con, listaConstantes, ordenar);
	}
	
	/**
	 * Obtener Motivos Atencion Odontologica
	 * @param con
	 * @param tipo atencion
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerMotivosAtencionOdontologica(Connection con, int tipo){
		return SqlBaseUtilidadesDao.obtenerMotivosAtencionOdontologica(con, tipo);
	}
	
	/**
	 * 
	 * 
	 */
	
	@Override
	public double guardarProcAutoEstados(DtoLogProcAutoEstados dto) {
		
		return  SqlBaseUtilidadesDao.guardarProcAutoEstados(dto);
	}
	
	/**
	 * 
	 * 
	 */
	public Vector<InfoDatosString> obtenerListadoCoberturasXTipo(Connection con, int institucion, String tipoCobertura)
	{
		return SqlBaseUtilidadesDao.obtenerListadoCoberturasXTipo(con, institucion, tipoCobertura);
	}
	
	/**
	 * 
	 */
	public ArrayList<DtoConceptosIngTesoreria> obtenerConceptosIngresoTesoreria(int tipoIngreso,String valorFiltro)
	{
		return SqlBaseUtilidadesDao.obtenerConceptosIngresoTesoreria(tipoIngreso, valorFiltro);
	}
	
	/**
	 * 
	 */
	public String obtenerIndicativoInterfazPersona (Connection con, String nroId)
	{
		return SqlBaseUtilidadesDao.obtenerIndicativoInterfazPersona(con, nroId);
	}
	
	/**
	 * 
	 */
	public String obtenerIndicativoInterfazMedico (Connection con, int codigoPersona)
	{
		return SqlBaseUtilidadesDao.obtenerIndicativoInterfazMedico(con, codigoPersona);
	}
	
	/**
	 * 
	 */
	public HashMap obtenerDatosTercero(Connection con, int codigoTercero)
	{
		return SqlBaseUtilidadesDao.obtenerDatosTercero(con, codigoTercero);
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.princetonsa.dao.UtilidadesDao#obtenerFiltroValorConceptoIngreso(java.sql.Connection, int)
	 */
	public String obtenerFiltroValorConceptoIngreso(Connection con, int concepto)
	{
		return  SqlBaseUtilidadesDao.obtenerFiltroValorConceptoIngreso(con, concepto);
	}
	
	@Override
	public boolean esOrdenAmbulatoriaPYP(Connection con, String orden,	int institucion) 
	{
		return SqlBaseUtilidadesDao.esOrdenAmbulatoriaPYP(con,orden,institucion);
	}
	
	@Override
	public String obtenerNombreTipoIdentificacion(String acronimo) {
		return SqlBaseUtilidadesDao.obtenerNombreTipoIdentificacion(acronimo);
	}
	
	/**
	 * Método encargado de arreglar los consecutivos de una tabla
	 * @param parametros
	 * @return
	 */
	public boolean arreglarConsecutivos(HashMap<String, Object> parametros) 
	{
		return SqlBaseUtilidadesDao.arreglarConsecutivos(parametros);
	}
	
	/**
	 * 
	 * @param loginUsuario
	 * @return
	 */
	public String obtenerEmailUsuario(String loginUsuario)
	{
		return SqlBaseUtilidadesDao.obtenerEmailUsuario(loginUsuario);
	}
	
	@Override
	public double obtenerAbonoPacienteTipoYNumeroDocumento(int codigoPersona,int tipoMovimiento, int codigoCita) 
	{
		return SqlBaseUtilidadesDao.obtenerAbonoPacienteTipoYNumeroDocumento(codigoPersona,tipoMovimiento,codigoCita);
	}
	
	@Override
	public HashMap obtenerInformacionUsuarioCapitado(Connection con,
			String tipoID, String numeroID) {
		return SqlBaseUtilidadesDao.obtenerInformacionUsuarioCapitado(con,tipoID,numeroID);
	}
	


	@Override
	public DtoDiagnostico getDiagnosticoPacienteIngreso(Connection con, int idIngreso) 
	{
		return SqlBaseUtilidadesDao.getDiagnosticoPacienteIngreso(con,idIngreso,DaoFactory.POSTGRESQL);
	}
	
	@Override
	public DtoDiagnostico getDiagnosticoPacienteCuenta(Connection con, int idCuenta) 
	{
		return SqlBaseUtilidadesDao.getDiagnosticoPacienteCuenta(con,idCuenta,DaoFactory.POSTGRESQL);
	}

	@Override
	public String obtenerCodigoReciboCaja(Connection con, String numReciboCaja,
			int codigoInstitucionInt, int codigoCentroAtencion) {
		return SqlBaseUtilidadesDao.obtenerCodigoReciboCaja(con,numReciboCaja,codigoInstitucionInt,codigoCentroAtencion);
	}

	@Override
	public String obtenerTipoRegimenClasificacionSocioEconomica(Connection con,
			int clasificacionSE) {
		return SqlBaseUtilidadesDao.obtenerTipoRegimenClasificacionSocioEconomica(con,clasificacionSE);
	}

	/*
	 * (non-Javadoc)
	 * @see com.princetonsa.dao.UtilidadesDao#existenConceptosNotasPaciente(java.sql.Connection)
	 */
	@Override
	public boolean existenConceptosNotasPaciente(Connection con) {
		String sentencia="SELECT COUNT(*) as num_registros FROM tesoreria.concepto_nota_paciente";
		return SqlBaseUtilidadesDao.existenConceptosNotasPaciente(con, sentencia);
	}

	/*
	 * (non-Javadoc)
	 * @see com.princetonsa.dao.UtilidadesDao#existenNotasPaciente(java.sql.Connection)
	 */
	@Override
	public boolean existenNotasPaciente(Connection con) {
		String sentencia="SELECT COUNT(*) as num_registros FROM tesoreria.nota_paciente";
		return SqlBaseUtilidadesDao.existenNotasPaciente(con, sentencia);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.princetonsa.dao.UtilidadesDao#existenNotasPaciente(java.sql.Connection)
	 */
	@Override
	public boolean existenMovimientosAbonos(Connection con) {
		String sentencia="SELECT COUNT(*) as num_registros FROM tesoreria.movimientos_abonos";
		return SqlBaseUtilidadesDao.existenMovimientosAbonos(con, sentencia);
	}

	/*
	 * (non-Javadoc)
	 * @see com.princetonsa.dao.UtilidadesDao#obtenerCentrosAtencionxUsarioEstadoActivo(java.sql.Connection, java.lang.String)
	 */
	@Override
	public ArrayList<String[]> obtenerCentrosAtencionxUsarioEstadoActivo(
			Connection con, String usuario) {
		return SqlBaseUtilidadesDao.obtenerCentrosAtencionxUsarioEstadoActivo(con, usuario);
	}

	/**
	 * (non-Javadoc)
	 * @see com.princetonsa.dao.UtilidadesDao#obtenerCentrosCostoxUsario(java.sql.Connection, java.lang.String)
	 */
	@Override
	public ArrayList<String[]> obtenerCentrosCostoxUsario(Connection con,
			String usuario) {
		return SqlBaseUtilidadesDao.obtenerCentrosCostoxUsario(con, usuario);
	}
	
	/**
	 * @see com.princetonsa.dao.UtilidadesDao#obtenerCantidadCajasCajero(java.sql.Connection, int, java.lang.String, java.lang.String)
	 */
	public  int obtenerCantidadCajasCajero(Connection con,int centroAtencion, String usuario,String valorParametro){
		return SqlBaseUtilidadesDao.obtenerCantidadCajasCajero(con, centroAtencion, usuario, valorParametro);
	}
	
	/**
	 * @see com.princetonsa.dao.UtilidadesDao#obtenerNumeroCuentaAutorizacionPorOrdenAmbulatoria(java.sql.Connection, java.lang.Integer)
	 */
	public Integer obtenerNumeroCuentaAutorizacionPorOrdenAmbulatoria(Connection con,Integer numeroSolcitud){
		return SqlBaseUtilidadesDao.obtenerNumeroCuentaAutorizacionPorOrdenAmbulatoria(con, numeroSolcitud);
	}

	/**
	 * @see com.princetonsa.dao.UtilidadesDao#obtenerViaAdministracion(java.sql.Connection, int, int)
	 */
	public String obtenerNombreViaAdministracion(Connection con, int codigoVia) {
		return SqlBaseUtilidadesDao.obtenerNombreViaAdministracion(con, codigoVia);
	}
	
	/**
	 * @see com.princetonsa.dao.UtilidadesDao#esCapitacionSubcontratada(java.sql.Connection, java.lang.Integer)
	 */
	public  Boolean esCapitacionSubcontratada(Connection con,Integer codigoConvenio){
		return SqlBaseUtilidadesDao.esCapitacionSubcontratada(con, codigoConvenio);
	}

	@Override
	public boolean liberarUltimaCamaPaciente(Connection con, Integer codigoPersona) {
		return SqlBaseUtilidadesDao.liberarUltimaCamaPaciente(con, codigoPersona, consultaUltimaCamaPaciente, consultaPacienteEnCama);
	}
	
	@Override
	public List<EspecialidadDto> consultarEspecialidadesProfesional(Connection connection,ProfesionalHQxDto profesionalHQxDto,int codigoInstitucion, Boolean especiliadadesActivas,List<EspecialidadDto>descartarEspecialidades) throws BDException{
		return SqlBaseUtilidadesDao.consultarEspecialidadesProfesional(connection, profesionalHQxDto, descartarEspecialidades, codigoInstitucion, especiliadadesActivas);
	}
}