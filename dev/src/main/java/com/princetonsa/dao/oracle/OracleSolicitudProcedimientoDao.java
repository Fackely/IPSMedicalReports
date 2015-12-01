/*
* @(#)OracleSolicitudProcedimientoDao.java
*
* Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
*
* Lenguaje		: Java
* Compilador	: J2SDK 1.4.1_01
*/
package com.princetonsa.dao.oracle;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;


import util.ConstantesBD;
import util.ValoresPorDefecto;

import com.princetonsa.dao.SolicitudProcedimientoDao;
import com.princetonsa.dao.sqlbase.SqlBaseSolicitudProcedimientoDao;
import com.princetonsa.dto.ordenes.DtoProcedimiento;
 
/**
* Esta clase implementa el contrato estipulado en <code>SolicitudProcedimientoDao</code>, y presta
* los servicios de acceso a una base de datos Oracle requeridos por la clase
* <code>SolicitudProcedimiento</code>
*
* @version 1.0, Mar 10, 2004
* @author <a href="mailto:edgar@PrincetonSA.com">Edgar Prieto</a>
*/
public class OracleSolicitudProcedimientoDao implements SolicitudProcedimientoDao
{
	/** Sentencia SQL para cargar los datos particulares de una solicitud de procedimientos */
	private static final String is_cargar =
		"SELECT "	+	"sp.codigo_servicio_solicitado "	+ "AS codigoServicioSolicitado,"		+								
						"getcodigoservicio(sp.codigo_servicio_solicitado, "+ConstantesBD.separadorSplit+") ||' '||getnombreservicio(sp.codigo_servicio_solicitado, "+ConstantesBD.separadorSplit+")"+ "AS nombreServicioSolicitado,"+						
						"sp.nombre_otros "					+ "AS nombreOtros,"						+
						"sp.comentario "					+ "AS comentario,"						+
						"CASE "								+
							"WHEN sp.codigo_servicio_solicitado IS NULL THEN -1 "					+
							"ELSE e.codigo "				+
						"END "								+ "AS codigoEspecialidadSolicitada,"	+
						"CASE "								+
							"WHEN sp.codigo_servicio_solicitado IS NULL THEN '' "					+
							"ELSE e.nombre "				+
						"END "								+ "AS nombreEspecialidadSolicitada, "	+
						"numero_documento AS numeroDocumento, " +
						"solicitud_multiple AS solicitud_multiple, " +
						"frecuencia AS frecuencia, " +
						"tipo_frecuencia AS tipo_frecuencia, " +
						"getNombreTipoFrecuencia(tipo_frecuencia) AS nom_tipo_frecuencia, " +
						"sp.finalidad AS finalidad, " +
						"fs.nombre AS nombreFinalidad, " +
						"CASE WHEN s.espos="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 'SI' ELSE '' END AS espos," +
						"  coalesce (sp.portatil_asociado,-1) As portatil, "+
						"  coalesce (sp.motivo_anul_portatil,'') As motivoanulport "+
		"FROM "		+	"sol_procedimientos "			+ "sp "	+
						"INNER JOIN"						+
						"("										+
							"servicios "						+ "s "	+
							"INNER JOIN referencias_servicio "	+ "rf "	+ "ON(s.codigo=rf.servicio AND rf.tipo_tarifario=0)"	+
							"INNER JOIN especialidades "		+ "e "	+ "ON(s.especialidad=e.codigo)"							+
						") "												+ "ON(s.codigo=sp.codigo_servicio_solicitado)"			+
						" LEFT OUTER JOIN finalidades_servicio fs ON(fs.codigo=sp.finalidad) " +
		"WHERE "	+	"numero_solicitud=?";

	/**
	* Carga los datos de una solicitud de procedimientos desde una
	* BD Postgresql
	* 
	* @param ac_con				Conexión a la fuente de datos
	* @param ai_numeroSolicitud	Número de la solicitud de procedimiento a cargar
	*/
	public HashMap cargar(Connection ac_con, int ai_numeroSolicitud,String tarifario)throws SQLException
	{
		return SqlBaseSolicitudProcedimientoDao.cargar(ac_con, ai_numeroSolicitud, is_cargar.replaceAll(ConstantesBD.separadorSplit, tarifario));
	}

	/**
	* Inserta una solicitud de procedimiento en una BD Postgresql
	* 
	* @param ac_con				Conexión a la fuente de datos
	 * @param as_estado			Estado de la transacción
	 * @param ai_numeroSolicitud	Número de la solicitud de procedimiento a asignar. Este número de
	*							existir en la tabla de solicitudes
	 * @param ai_codigoServicio	Código del servicio de la solicitud de procedimientos
	 * @param as_nombreOtros		Nombre del servicio (si el código de servio no está parametrizado en
	*							la aplicación) de la solicitud de procedimientos
	 * @param as_comentario		Comentario de la solicitud del procedimiento
	 * @return Número de solicitudes insertadas correctamente
	* @throws java.sql.SQLException si se presentó un error de base de datos
	*/
	public int insertarTransaccional(
		Connection	ac_con,
		String		as_estado,
		int			ai_numeroSolicitud,
		int			ai_codigoServicio,
		String		as_nombreOtros,
		String		as_comentario,
		int numeroDocumento, boolean multiple, float frecuencia, int tipoFrecuencia,
		int finalidad, boolean respuestaMultiple, boolean finalizadaRespuesta,int idCuenta, boolean finalizar,String portatil
	)throws SQLException
	{
		return SqlBaseSolicitudProcedimientoDao.insertarTransaccional(ac_con, as_estado, ai_numeroSolicitud, ai_codigoServicio, as_nombreOtros, as_comentario, numeroDocumento, multiple, frecuencia, tipoFrecuencia,finalidad, respuestaMultiple, finalizadaRespuesta, idCuenta, finalizar,portatil);
	}

	/**
	* Modifica los datos de una solicitud de procedimiento en una fuente de datos. Solo es posible
	* modificar el comentario de la solicitud en una BD Postgresql
	* 
	* @param ac_con				Conexión a la fuente de datos
	* @param ai_numeroSolicitud	Número de la solicitud de procedimiento a modificar
	* @param as_comentario		Texto a adicionar en la sección de comentarios de la solicitud de
	*							procedimientos
	* @return El número de solicitudes de procedimientos modificadas
	*/
	public int modificar(
		Connection	ac_con,
		int			ai_numeroSolicitud,
		String		as_comentario
	)throws SQLException
	{
		return SqlBaseSolicitudProcedimientoDao.modificar( ac_con, ai_numeroSolicitud, as_comentario);
	}

	/**
	 * Funcion para finalizar Solicitud de procedimientos
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 * @throws SQLException
	 */
	public int 	finalizarSolicitudMultiple(Connection con, int numeroSolicitud)
	{
		return SqlBaseSolicitudProcedimientoDao.finalizarSolicitudMultiple(con, numeroSolicitud);
	}

	
	/**
	* Modifica el número de autorización de una solicitud en
	* una BD Postgresql
	* 
	* @param ac_con					Conexión a la fuente de datos
	* @param ai_numeroSolicitud		Número de la solicitud a modificar
	* @param as_numeroAutorizacion	Nuevo número de autorización de la solicitud
	* @return El número de solicitudes de modificadas
	*/
	/*public int modificarNumeroAutorizacion(
		Connection	ac_con,
		int			ai_numeroSolicitud,
		String		as_numeroAutorizacion
	)throws SQLException
	{
		return SqlBaseSolicitudProcedimientoDao.modificarNumeroAutorizacion( ac_con, ai_numeroSolicitud, as_numeroAutorizacion);
	}
	*/
	/**
	 * Método para consultar el numero de documento siguiente
	 * para insertar en la solicitud de procedimientos
	 * @param con
	 * @return numero de documento
	 */
	public int numeroDocumentoSiguiente(Connection con)
	{
		return SqlBaseSolicitudProcedimientoDao.numeroDocumentoSiguiente(con);
	}
	/**
	 * Metodo para cargar los procedimientos con su codgio, descripcion, especialidad, 
	 * cantidad y si es pos segun el numero de Documento que reciba
	 * @param con
	 * @param numeroDocumento
	 * @return col Collection para recorrerla en la impresion
	 */
	public  Collection solicitudesXDocumento(Connection con, int numeroDocumento)
    {
		return SqlBaseSolicitudProcedimientoDao.solicitudesXDocumento(con, numeroDocumento);
    }
	
	/**
	 * Método que me carga la descripcion de una solictud de servicio externo
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public String consultarExterno(Connection con, int numeroSolicitud)
    {
		return SqlBaseSolicitudProcedimientoDao.consultarExterno(con, numeroSolicitud);
    }
	
	/**
	 * 
	 */
	public Collection solicitudesXCuentaFechaHora(Connection con, int codigoCuenta, String fechaSolicitud, String horaSolicitud, int institucion)
	{
		return SqlBaseSolicitudProcedimientoDao.solicitudesXCuentaFechaHora(con,codigoCuenta,fechaSolicitud,horaSolicitud, institucion);
	}
	
	/**
	 * Metodo encargado de verificar si el servicio es un portatil
	 * @param con
	 * @param numSol
	 * @param codServ
	 * @return true/fasle
	 */
	public boolean esPortatil(Connection con,String numSol,String codServ)
	{
		return SqlBaseSolicitudProcedimientoDao.esPortatil(con, numSol, codServ);
	}
	
	/**
	 * Metodo que devuelve el codigo del portatil asociado a la solicitud de proc, si no existe entonces -1
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public int obtenerPortatilSolicitud(Connection con, int numeroSolicitud)
	{
		String consulta="select coalesce(getTienePortatilSolicitud1(?), "+ConstantesBD.codigoNuncaValido+" ) from dual";
		return SqlBaseSolicitudProcedimientoDao.obtenerPortatilSolicitud(con, numeroSolicitud,consulta);
	}
	
	/**
	 * Metodo que devuelve el codigo del portatil asociado a un servicio, si no existe entonces -1
	 * @param con
	 * @param codigoServicio
	 * @return
	 */
	public int obtenerPortatilServicio(Connection con, int codServicio)
	{
		return SqlBaseSolicitudProcedimientoDao.obtenerPortatilServicio(con, codServicio);
	}
	
	@Override
	public String[] obtenerConsultaSolicitudProcedimientosReporte(Connection con, HashMap numerosSolicitudes)
	{
		return SqlBaseSolicitudProcedimientoDao.obtenerConsultaSolicitudProcedimientosReporte(con, numerosSolicitudes);
	}

	
	/**
	 * Metodo encargado de Anular o Aprobar un portatil
	 * @param con
	 * @param datos
	 * Jhony alexander Duque A.
	 * --------------------------
	 * KEY'S DEL MAPA DATOS
	 * --------------------------
	 * -- portatil --> Opcional
	 * -- motAnuPort --> Opcional
	 * -- numeroSolicitud --> Requerido
	 * @return
	 */
	public boolean anularAprobarPortatil(Connection con,HashMap datos)
	{
		return SqlBaseSolicitudProcedimientoDao.anularAprobarPortatil(con, datos);
		
	}
	
	/**
	 * @param con
	 * @param numerosSolicitudes
	 * @return
	 */
	public String obtenerWhereFormatoMediaCarta(Connection con, HashMap numerosSolicitudes)
	{
		return SqlBaseSolicitudProcedimientoDao.obtenerWhereFormatoMediaCarta(con, numerosSolicitudes);
	}

	@Override
	public HashMap valAnulacion(Connection ac_con, int numeroSolicitud) {
		// TODO Auto-generated method stub
		return SqlBaseSolicitudProcedimientoDao.valAnulacion(ac_con, numeroSolicitud);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean finalizarSolicitudMultiple(Connection con,String numeroSolicitud) 
	{
		return SqlBaseSolicitudProcedimientoDao.finalizarSolicitudMultiple(con, numeroSolicitud);
	}

	@Override
	public DtoProcedimiento buscarServiciosSolicitudProcedimientos(
			Connection con, int numeroSolicitud, int codigoTarifario) {
		return SqlBaseSolicitudProcedimientoDao.buscarServiciosSolicitudProcedimientos(con, numeroSolicitud, codigoTarifario);
	}
	
	@Override
	public DtoProcedimiento buscarServiciosSolicitudProcedimientosC(
			Connection con, int numeroSolicitud, int codigoTarifario) {
		return SqlBaseSolicitudProcedimientoDao.buscarServiciosSolicitudProcedimientosC(con, numeroSolicitud, codigoTarifario);
	}
	
	/**
	 * @see com.princetonsa.dao.SolicitudProcedimientoDao#consultarDatosMedicoAnulacion(java.sql.Connection, java.lang.Integer)
	 */
	public  String consultarDatosMedicoAnulacion(Connection conn,Integer codigoMedico) throws SQLException
	{
		return SqlBaseSolicitudProcedimientoDao.consultarDatosMedicoAnulacion(conn, codigoMedico);
	}
	
}