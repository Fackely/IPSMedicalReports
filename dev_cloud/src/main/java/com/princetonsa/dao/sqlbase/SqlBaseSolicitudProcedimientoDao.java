/*
 * @(#)SqlBaseSolicitudProcedimientoDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_03
 *
 */

package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.ordenes.DtoProcedimiento;

/**
 * Esta clase implementa la funcionalidad común a todas (o casi todas)
 * las BD utilizadas en axioma, en general se trata de las consultas que
 * utilizan SQL estándar. Métodos particulares a Solicitud Procedimiento
 *
 *	@version 1.0, Apr 13, 2004
 */
public class SqlBaseSolicitudProcedimientoDao 
{

	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseSolicitudProcedimientoDao.class);
	
	private static String solicitudesXDocumentoStr="SELECT sp.codigo_servicio_solicitado AS codigo,"+ 
													"case when rs.codigo_propietario is null or  rs.codigo_propietario='' then getnombreservicio(sp.codigo_servicio_solicitado,"+ConstantesBD.codigoTarifarioCups+") else rs.codigo_propietario||'-'||getnombreservicio(sp.codigo_servicio_solicitado,"+ConstantesBD.codigoTarifarioCups+") end AS procedimiento, "+
													"fs.nombre AS finalidad, "+
													"getnomcentrocosto(s.centro_costo_solicitado) AS centro_costo, "+
													"count(1) AS cantidad, "+
													"ser.espos AS pos, "+
													"sp.solicitud_multiple AS multiple, "+
													"CASE WHEN (sp.frecuencia || ' ' || tf.nombre) IS NULL THEN '' ELSE (sp.frecuencia || ' ' || tf.nombre) END AS frecuencia "+
													"FROM solicitudes s "+
													"INNER JOIN sol_procedimientos sp on(sp.numero_solicitud=s.numero_solicitud) "+
													"INNER JOIN servicios ser ON(ser.codigo=sp.codigo_servicio_solicitado) "+
													"INNER JOIN cuentas c ON(s.cuenta=c.id) " +
													"INNER JOIN sub_cuentas sc ON(sc.ingreso = c.id_ingreso AND sc.nro_prioridad = 1) "+
													"INNER JOIN contratos con ON(sc.contrato=con.codigo) "+
													"INNER JOIN esquemas_tarifarios et ON(getEsquemaTarifarioSerArt(sc.sub_cuenta,sc.contrato,ser.codigo,'S')=et.codigo) "+
													"INNER JOIN referencias_servicio rs ON(ser.codigo=rs.servicio AND et.tarifario_oficial=rs.tipo_tarifario) "+
													"LEFT OUTER JOIN tipos_frecuencia tf ON (tf.codigo=sp.tipo_frecuencia) "+
													"INNER JOIN finalidades_servicio fs ON(fs.codigo=sp.finalidad) "+
													"WHERE sp.numero_documento = ?  "+
													"GROUP BY sp.codigo_servicio_solicitado, centro_costo, espos, solicitud_multiple, sp.frecuencia || ' ' || tf.nombre,fs.nombre,rs.codigo_propietario "+
													"ORDER BY procedimiento ";
		
		
private static String numeroDocumentoStr="SELECT CASE WHEN max(numero_documento) IS NULL THEN 1 ELSE max(numero_documento)+1 END AS numeroDocumento FROM sol_procedimientos";
	
	private static final String consultarExternoStr=" SELECT sp.nombre_otros as externo, s.consecutivo_ordenes_medicas as consecutivo, sp.comentario as comentario, sp.resultados_otros as resultado FROM sol_procedimientos sp INNER JOIN solicitudes s ON(sp.numero_solicitud=s.numero_solicitud)  WHERE sp.nombre_otros!='' AND sp.numero_solicitud=?";
	

	///////////////////////////////////////////////////////////////////////////////////////////
	//portatil
	private static final String strEsPortatil =" SELECT count(*) from sol_procedimientos where numero_solicitud=? AND portatil_asociado=?";
	
	/**
	 * se encarga de anular o aprobar un portatil 
	 **/
	private static final String strAnularAprobarPortatil=" UPDATE sol_procedimientos SET portatil_asociado=?,motivo_anul_portatil=? where numero_solicitud=?";
	///////////////////////////////////////////////////////////////////////////////////////////
	
	
	/** Sentencia SQL para modificar el comentario de una solicitud de procedimiento */
	private static final String is_modificar =
		"UPDATE "	+	"sol_procedimientos "	+
		"SET "		+	"comentario"		+ "=? "		+
		"WHERE "	+	"numero_solicitud"	+ "=?";

	private static final String insertarMultiple=
		"UPDATE sol_procedimientos SET finalizada ="+ValoresPorDefecto.getValorFalseParaConsultas()+" where numero_solicitud=?";
	
	/** Sentencia SQL para insertar los datos particulares de una solicitud de procedimiento*/
	private static final String insertarStr =
		"INSERT INTO "						+
		"sol_procedimientos"		+
		"("									+
			"numero_solicitud,"				+	//1
			"codigo_servicio_solicitado,"	+	//2
			"nombre_otros,"					+	//3
			"comentario,"					+	//4
			"resultados_otros, "			+	//NULL
			"numero_documento,"				+	//5
			"frecuencia, "					+	//6
			"tipo_frecuencia, "				+	//7
			"solicitud_multiple, "			+	//8
			"finalidad, " +						//9
			"respuesta_multiple," +				//10
			"finalizada_respuesta," +			//11
			"portatil_asociado "+				//12
		")"									+
		"VALUES(?, ?, ?, ?, NULL, ?, ?, ?, ?, ?,?,?,?)";

	/** Sentencia SQL para modificar el número de autorización de la solicitud */
	//private static final String is_modificarNumeroAutorizacion =
	//	"UPDATE "	+	"solicitudes " +
	//	"SET "		+	"numero_autorizacion=? " +
	//	"WHERE "	+	"numero_solicitud=?";

	/**
	 * String con la consulta de verificación de la existencia de
	 * otras solicitudes con el mismo codigo del servicio solicitado
	 * para una cuenta
	 */
	private static final String buscarSolicitudVigenteStr="SELECT sol.numero_solicitud AS numero_solicitud FROM (SELECT numero_solicitud, codigo_servicio_solicitado FROM sol_procedimientos WHERE solicitud_multiple="+ValoresPorDefecto.getValorTrueParaConsultas()+" and finalizada="+ValoresPorDefecto.getValorFalseParaConsultas()+") sol INNER JOIN solicitudes s1 on (sol.numero_solicitud=s1.numero_solicitud) WHERE sol.codigo_servicio_solicitado=? AND s1.cuenta=?";
	
	/**
	 * String para finalizar la solicitud de procedimiento multiple
	 */
	private static final String actualizarFinalizacion="UPDATE sol_procedimientos SET finalizada="+ValoresPorDefecto.getValorTrueParaConsultas()+" WHERE numero_solicitud=?";
	
	
	/**
	 * Consulta el procedimiento para la impresion
	 * */
	private static final String consultarProcedimiento = 	
										"SELECT	" +
										"getcodigoservicio(sp.codigo_servicio_solicitado, 0) AS codigo, "+ 
										"getnombreservicio(sp.codigo_servicio_solicitado, 0) AS procedimiento, "+ 
										"fs.nombre AS finalidad, "+ 
										"getnomcentrocosto(s.centro_costo_solicitado) AS centro_costo, "+ 
										"count(1) AS cantidad, "+ 
										"CASE WHEN ser.espos ="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 'SI' ELSE 'NO' END AS pos, "+ 
										"CASE WHEN sp.solicitud_multiple ="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 'SI' ELSE 'NO' END AS multiple, "+ 
										"CASE WHEN (sp.frecuencia || ' ' || tf.nombre) IS NULL THEN '' ELSE (sp.frecuencia || ' ' || tf.nombre) END AS frecuencia, "+
										"s.numero_solicitud AS num_solicitud "+ 
										"FROM "+ 
										"solicitudes s "+ 
										"INNER JOIN sol_procedimientos sp on (sp.numero_solicitud=s.numero_solicitud) "+ 
										"INNER JOIN servicios ser ON (ser.codigo = sp.codigo_servicio_solicitado) "+ 
										"INNER JOIN cuentas c ON (s.cuenta = c.id) "+ 
										"INNER JOIN sub_cuentas sc ON (sc.ingreso = c.id_ingreso AND sc.nro_prioridad = 1) "+ 
										"INNER JOIN contratos con ON (sc.contrato = con.codigo) "+ 
										"INNER JOIN esquemas_tarifarios et ON (getEsquemaTarifarioSerArt(sc.sub_cuenta,sc.contrato,ser.codigo,'S') = et.codigo) "+ 
										"INNER JOIN finalidades_servicio fs ON (fs.codigo = sp.finalidad) "+ 
										"LEFT OUTER JOIN tipos_frecuencia tf ON (tf.codigo = sp.tipo_frecuencia) "+ 
										"WHERE "+ 
										"1=2 "+
										"GROUP BY "+ 
											"sp.codigo_servicio_solicitado,"+ 
											"s.centro_costo_solicitado,"+ 
											"ser.espos,"+ 
											"sp.solicitud_multiple,"+ 
											"sp.frecuencia,"+
											"tf.nombre,"+ 
											"fs.nombre,"+ 
											"s.numero_solicitud "+
										"ORDER BY "+ 
											"procedimiento";
	
	/**
	 * Consulta el procedimiento No cruento para la impresion
	 * */
	private static final String consultarProcedimientoNoCruento = 	
										"SELECT	" +
										"getcodigoservicio(sp.codigo_servicio_solicitado, 0) AS codigo, "+ 
										"getnombreservicio(sp.codigo_servicio_solicitado, 0) AS procedimiento, "+ 
										"fs.nombre AS finalidad, "+ 
										"getnomcentrocosto(s.centro_costo_solicitado) AS centro_costo, "+ 
										"count(1) AS cantidad, "+ 
										"CASE WHEN ser.espos ="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 'SI' ELSE 'NO' END AS pos, "+ 
										"CASE WHEN sp.solicitud_multiple ="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 'SI' ELSE 'NO' END AS multiple, "+ 
										"CASE WHEN (sp.frecuencia || ' ' || tf.nombre) IS NULL THEN '' ELSE (sp.frecuencia || ' ' || tf.nombre) END AS frecuencia, "+
										"s.numero_solicitud AS num_solicitud "+ 
										"FROM "+ 
										"solicitudes s "+ 
										"INNER JOIN solicitudes_cirugia sc on (sc.numero_solicitud=s.numero_solicitud) " +
										"INNER JOIN sol_cirugia_por_servicio scs on (scs.numero_solicitud=s.numero_solicitud) "+
										"INNER JOIN servicios ser ON (ser.codigo = scs.servicio) "+ 
										"INNER JOIN cuentas c ON (s.cuenta = c.id) "+
										"INNER JOIN sub_cuentas sc ON (sc.ingreso = c.id_ingreso AND sc.nro_prioridad = 1) "+ 
										"INNER JOIN contratos con ON (sc.contrato = con.codigo) "+ 
										"INNER JOIN esquemas_tarifarios et ON (getEsquemaTarifarioSerArt(sc.sub_cuenta,sc.contrato,ser.codigo,'S') = et.codigo) "+ 
										"INNER JOIN finalidades_servicio fs ON (fs.codigo = scs.finalidad) "+ 
										"LEFT OUTER JOIN tipos_frecuencia tf ON (tf.codigo = sp.tipo_frecuencia) "+ 
										"WHERE "+ 
										"1=2 "+
										"GROUP BY "+ 
											"sp.codigo_servicio_solicitado,"+ 
											"s.centro_costo_solicitado,"+ 
											"ser.espos,"+ 
											"sp.solicitud_multiple,"+ 
											"sp.frecuencia,"+
											"tf.nombre,"+ 
											"fs.nombre,"+ 
											"s.numero_solicitud "+
										"ORDER BY "+ 
											"procedimiento";
	
	///////////////////////////////////////////////////////////////////////
	//cambio por anexo 591 portatil
	
	/**
	 * Metodo encargado de verificar si el servicio es un portatil
	 * @param con
	 * @param numSol
	 * @param codServ
	 * Jhony Alexander Duque A.
	 * @return true/fasle
	 */
	public static boolean esPortatil(Connection con,String numSol,String codServ)
	{
		try
		{
			PreparedStatementDecorator numeroDocumento= new PreparedStatementDecorator(con.prepareStatement(strEsPortatil,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator resultado=new ResultSetDecorator(numeroDocumento.executeQuery());
			if(resultado.next())
			{
				if (resultado.getInt(1)>0)
					return true;
			}
			else
			{
				return false;
			}
		}
		catch(SQLException e)
		{
			logger.error("\n problema verificando si el servicio es portatil "+e);
			return false;
		}
		
		return false;
		
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
	public static boolean anularAprobarPortatil(Connection con,HashMap datos)
	{
		logger.info("\n entre A  anularAprobarPortatil --> "+datos);
		
		try
		{
			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(strAnularAprobarPortatil,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			//portatil
			if ( !(datos.get("portatil")+"").equals(ConstantesBD.codigoNuncaValido+"") && !(datos.get("portatil")+"").equals("") && !(datos.get("portatil")+"").equals("null"))
				ps.setObject(1, datos.get("portatil"));
			else
				ps.setNull(1, Types.INTEGER);
			
			//motivo anulacion portatil
			if ( !(datos.get("motAnuPort")+"").equals("") && !(datos.get("motAnuPort")+"").equals("null"))
				ps.setObject(2, datos.get("motAnuPort"));
			else
				ps.setNull(2, Types.VARCHAR);
			
			//numero solicitud 
			ps.setObject(3, datos.get("numeroSolicitud"));
			
			if (ps.executeUpdate()>0)
				return true;
			
			
		}
		catch(SQLException e)
		{
			logger.error("\n problema anulando / aprobando un portatil "+e);
			return false;
		}
		
		return false;
		
	}
	
	////////////////////////////////////////////////////////////////////////
	
	
	/**
	 * Mñétodo para consultar el numero de documento siguiente
	 * para insertar en la solicitud de procedimientos
	 * @param con
	 * @return numero de documento
	 */
	public static int numeroDocumentoSiguiente(Connection con)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			pst= con.prepareStatement(numeroDocumentoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			rs=pst.executeQuery();
			if(rs.next()){
				return rs.getInt("numeroDocumento");
			}
			
		}
		catch(Exception e){
			logger.error("############## ERROR numeroDocumentoSiguiente", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
			return 0;
		}
		
	/**
	* Modifica los datos de una solicitud de procedimiento en una fuente de datos. Solo es posible
	* modificar el comentario de la solicitud en una BD Genérica
	* 
	* @param ac_con				Conexión a la fuente de datos
	* @param ai_numeroSolicitud	Número de la solicitud de procedimiento a modificar
	* @param as_comentario		Texto a adicionar en la sección de comentarios de la solicitud de
	*							procedimientos
	* @return El número de solicitudes de procedimientos modificadas
	*/
	public static int modificar(
		Connection	ac_con,
		int			ai_numeroSolicitud,
		String		as_comentario
	)throws SQLException
	{
		PreparedStatementDecorator	lps_ps;

		lps_ps = new PreparedStatementDecorator(ac_con.prepareStatement(is_modificar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		lps_ps.setString(1, as_comentario);
		lps_ps.setInt(2, ai_numeroSolicitud);

		return lps_ps.executeUpdate();
	}

	/**
	* Inserta una solicitud de procedimiento en una BD Genérica
	* 
	* @param con				Conexión a la fuente de datos
	 * @param as_estado			Estado de la transacción
	 * @param numeroSolicitud	Número de la solicitud de procedimiento a asignar. Este número de
	*							existir en la tabla de solicitudes
	 * @param ai_codigoServicio	Código del servicio de la solicitud de procedimientos
	 * @param nombreOtros		Nombre del servicio (si el código de servio no está parametrizado en
	*							la aplicación) de la solicitud de procedimientos
	 * @param as_comentario		Comentario de la solicitud del procedimiento
	 * @param multiple 			Solicitud multiple
	 * @param frecuencia		Frecuencia del procedimiento
	 * @param tipoFrecuencia	Tipo de Frecuencia del procedimiento
	 * @param finalizadaRespuesta 
	 * @param respuestaMultiple 
	 * @param idCuenta			Codigo de la cuenta del paciente
	 * @param finalizar @todo
	 * @return Número de solicitudes insertadas correctamente
	* @throws java.sql.SQLException si se presentó un error de base de datos
	*/
	public static int insertarTransaccional(
		Connection	con,
		String		as_estado,
		int			numeroSolicitud,
		int			ai_codigoServicio,
		String		nombreOtros,
		String		as_comentario,
		int numeroDocumento,
		boolean multiple,
		float frecuencia,
		int tipoFrecuencia,
		int finalidad,
		boolean respuestaMultiple, boolean finalizadaRespuesta, int idCuenta, boolean finalizar,String portatil
	)throws SQLException
	{
		boolean				continuar;
		DaoFactory			daoFactory;
		int					respuesta;
		PreparedStatementDecorator	statement;

		//MT 6503
		if(System.getProperty("TIPOBD").equals("POSTGRESQL"))
		{
        	Statement stmt = con.createStatement();
            stmt.execute("SET standard_conforming_strings TO off");
            stmt.close();
		}
		//FIN MT 6503

		/* Obtener una referencia al objeto pricipal de acceso a base de datos */
		daoFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		/* Iniciar el control de flujo */
		continuar = true;

		/* El estado de la transacción no puede ser nulo */
		if(as_estado == null)
		{
			daoFactory.abortTransaction(con);
			throw new SQLException(
				"El estado de la transacción (Insertar Procedimiento) no esta especificado"
			);
		}

		/* Se desea iniciar una transaccion */
		if(as_estado.equals(ConstantesBD.inicioTransaccion) )
		{
			/* Abrir una conexíon a la base de datos si es necesario */
			if(con == null || con.isClosed() )
				con = daoFactory.getConnection();

			/* Iniciar la transacción */
			continuar = daoFactory.beginTransaction(con);
		}

		/* Validar los campos a insertar */
		if(nombreOtros != null)
		{
			if(nombreOtros.length() == 0)
				nombreOtros = null;
			else if(nombreOtros.length() > 256)
				nombreOtros = nombreOtros.substring(0, 257);
		}

		/* Insertar los datos particulares de la solicitud de procedimiento */
		
		if(finalizar)
		{
			int numSolicitudAnterior=buscarSolicitudMultipleActiva(con, ai_codigoServicio, idCuenta);
			if(numSolicitudAnterior<0)
			{
				daoFactory.abortTransaction(con);
	
				throw new SQLException(
					"Error al insertar la Solicitud de Procedimiento No. " + numeroSolicitud
				);
			}
			if(numSolicitudAnterior>0)
			{
				if(finalizarSolicitudMultiple(con, numSolicitudAnterior)<1)
				{
					daoFactory.abortTransaction(con);
	
					throw new SQLException(
						"Error al insertar la Solicitud de Procedimiento No. " + numeroSolicitud
					);
				}
			}
		}
		
		statement =  new PreparedStatementDecorator(con.prepareStatement(insertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

		statement.setInt(1, numeroSolicitud);

		logger.info("\n\nnumeroSolicitud:::::::: "+numeroSolicitud);
		
		if(ai_codigoServicio < 0)
			statement.setNull(2, Types.INTEGER);
		else
			statement.setInt(2, ai_codigoServicio);

		statement.setString(3, nombreOtros);
		statement.setString(4, as_comentario);
		statement.setInt(5, numeroDocumento);
		
		//code off de aca se deshabilito el codigo documentado y quitar el que esta debajo
		/*if(multiple && frecuencia!=-1) {
			statement.setFloat(5, frecuencia);
			if(tipoFrecuencia != 0) 
				statement.setInt(6, tipoFrecuencia);
			else 
				statement.setNull(6, Types.INTEGER);
		}
		else {
			statement.setNull(5, Types.FLOAT);
			statement.setNull(6, Types.INTEGER);
		} */
		
		
		//
		if(frecuencia != -1)
			statement.setFloat(6, frecuencia);
		else
			statement.setNull(6, Types.FLOAT);
		
		
		if(tipoFrecuencia!=0)
			statement.setInt(7, tipoFrecuencia);
		else
			statement.setNull(7, Types.INTEGER);
		//***********************

		statement.setBoolean(8, multiple);
		if(finalidad>0)
			statement.setInt(9, finalidad);
		else
			statement.setNull(9,Types.INTEGER);

		statement.setString(10, respuestaMultiple?"S":"N");
		statement.setString(11, finalizadaRespuesta?"S":"N");
		//portatil
		if (!portatil.equals(ConstantesBD.codigoNuncaValido+""))
			statement.setObject(12, portatil);
		else
			statement.setNull(12, Types.INTEGER);
		
		/* Obtener el número de registros insertados */
		respuesta = statement.executeUpdate();

		logger.info("valor respuesta: "+respuesta);
		
		if(multiple)
		{
			statement= new PreparedStatementDecorator(con.prepareStatement(insertarMultiple,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			statement.setInt(1, numeroSolicitud);
			statement.executeUpdate();
		}
		
		if(continuar)
		{
			/* Se desea finalizar una transaccion */
			if(as_estado.equals(ConstantesBD.finTransaccion) )
			{
				/* Finalizar la transacción */
				daoFactory.endTransaction(con);

				/* Cerrar la conexión a la base de datos */
				if(con != null && !con.isClosed() )
					UtilidadBD.closeConnection(con);
			}
		}
		else
		{
			/* Se presentó un error en la transacción */
			daoFactory.abortTransaction(con);

			throw new SQLException(
				"Error al insertar la Solicitud de Procedimiento No. " + numeroSolicitud
			);
		}

		return respuesta;
	}

	/**
	 * Método para finalizar la solicitud Anterior
	 * @param con
	 * @param numSolicitudAnterior
	 * @return Numero de elementos modificados
	 */
	public static int finalizarSolicitudMultiple(Connection con, int numSolicitudAnterior)
	{
		try
		{
			PreparedStatementDecorator statement= new PreparedStatementDecorator(con.prepareStatement(actualizarFinalizacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			statement.setInt(1, numSolicitudAnterior);
			int retorno=statement.executeUpdate();
			return retorno;
			
		}
		catch (SQLException e)
		{
			logger.error("Error finalizando la solicitud de procedimientos no "+numSolicitudAnterior+" : "+e);
			return 0;
		}
	}

	/**
	 * Método que busca si existe una solicitud previa múltiple
	 * @param con
	 * @param codigoServicio
	 * @param numeroSolicitud
	 * @return numero de solicitud (Solamente debe existir una solicitud que cumpla condiciones
	 * Si hay más de una es posible que exista una inconsistencia en la BD)
	 */
	private static int buscarSolicitudMultipleActiva(Connection con, int codigoServicio, int idCuenta)
	{
		try
		{
			PreparedStatementDecorator statement =  new PreparedStatementDecorator(con.prepareStatement(buscarSolicitudVigenteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			statement.setInt(1, codigoServicio);
			statement.setInt(2, idCuenta);
			ResultSetDecorator resultado=new ResultSetDecorator(statement.executeQuery());
			if(resultado.next())
			{
				return resultado.getInt("numero_solicitud");
			}
			else
			{
				return 0;
			}
		}
		catch (SQLException e)
		{
			logger.error("Error verificando existencia solicitudes sin finalizar : "+e);
			return -1;
		}
	}

	/**
	* Modifica el número de autorización de una solicitud en
	* una BD Genérica
	* 
	* @param ac_con					Conexión a la fuente de datos
	* @param ai_numeroSolicitud		Número de la solicitud a modificar
	* @param as_numeroAutorizacion	Nuevo número de autorización de la solicitud
	* @return El número de solicitudes de modificadas
	*/
	/*
	public static int modificarNumeroAutorizacion(
		Connection	ac_con,
		int			ai_numeroSolicitud,
		String		as_numeroAutorizacion
	)throws SQLException
	{
		PreparedStatementDecorator	lps_ps;

		// Abrir una conexíon a la base de datos si es necesario 
		if(ac_con == null || ac_con.isClosed() )
			ac_con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();

		lps_ps = new PreparedStatementDecorator(ac_con.prepareStatement(is_modificarNumeroAutorizacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

		// Corregir los datos a insertar en la fuente de datos
		if(as_numeroAutorizacion.length() > 16)
			as_numeroAutorizacion = as_numeroAutorizacion.substring(0, 17);

		lps_ps.setString(1, as_numeroAutorizacion);
		lps_ps.setInt(2, ai_numeroSolicitud);

		return lps_ps.executeUpdate();
	}
	*/
	/**
	* Carga los datos de una solicitud de procedimientos desde una
	* BD Genérica
	* 
	* @param ac_con				Conexión a la fuente de datos
	* @param ai_numeroSolicitud	Número de la solicitud de procedimiento a cargar
	*/
	public static HashMap cargar(Connection ac_con, int ai_numeroSolicitud, String is_cargar )throws SQLException
	{
		List				ll_l;
		PreparedStatementDecorator	lps_ps;
		logger.info("\n entre a cargar cadena -->"+is_cargar+" >> "+ai_numeroSolicitud);
		lps_ps = new PreparedStatementDecorator(ac_con.prepareStatement(is_cargar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		lps_ps.setInt(1, ai_numeroSolicitud);

		ll_l = (List)UtilidadBD.resultSet2Collection(new ResultSetDecorator(lps_ps.executeQuery()) );

		return (HashMap) ((ll_l.size() == 1) ? ll_l.get(0) : null);
	}
	
	/**
	 * Metodo para cargar los procedimientos con su codgio, descripcion, especialidad, 
	 * cantidad y si es pos segun el numero de Documento que reciba
	 * @param con
	 * @param numeroDocumento
	 * @return col Collection para recorrerla en la impresion
	 */
	public static Collection solicitudesXDocumento(Connection con, int numeroDocumento)
    {
		Collection col;
        try
             {               
                     if (con == null || con.isClosed()) 
                     {
                         DaoFactory myFactory =DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
                         con = myFactory.getConnection();
                     }
                     PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(solicitudesXDocumentoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
  					 ps.setInt(1,numeroDocumento);
  					 //Ejecuto la consulta
                     ResultSetDecorator resultset=new ResultSetDecorator(ps.executeQuery());
                     
                     col=UtilidadBD.resultSet2Collection(resultset);
                     return col;
                     
             }
       catch(SQLException e)
       {
           logger.error(" Error en la consulta de datos "+e.toString());
          
       }
        
        return null;
    	
    }
	
	/**
	 * Método que me carga la descripcion de una solictud de servicio externo
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static String consultarExterno(Connection con, int numeroSolicitud)
    {
		String externo="";
        try
         {               
	         if (con == null || con.isClosed()) 
	         {
	             DaoFactory myFactory =DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
	             con = myFactory.getConnection();
	         }
	         PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consultarExternoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			 ps.setInt(1,numeroSolicitud);
			 
			 //Ejecuto la consulta
			  ResultSetDecorator resultset=new ResultSetDecorator(ps.executeQuery());
			  if(resultset.next())
			  {
			  	externo=resultset.getString("externo")+ConstantesBD.separadorSplit+resultset.getString("consecutivo")+ConstantesBD.separadorSplit+resultset.getString("comentario")+ConstantesBD.separadorSplit+resultset.getString("resultado");
			  }
			  return externo;
         }
       catch(SQLException e)
       {
           logger.error(" Error en la consulta de datos "+e.toString());
          
       }
        return null;
    }

	
	/**
	 * 
	 * @param con 
	 * @param codigoCuenta
	 * @param fechaSolicitud
	 * @param horaSolicitud
	 * @return
	 */
	public static Collection solicitudesXCuentaFechaHora(Connection con, int codigoCuenta, String fechaSolicitud, String horaSolicitud, int institucion)
	{
		String solicitudesXCuentaFechaHoraStr="SELECT getcodigoservicio(sp.codigo_servicio_solicitado, "+ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(institucion)+") AS codigo,"+ 
												" getnombreservicio(sp.codigo_servicio_solicitado,"+ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(institucion)+") AS procedimiento, "+
																"fs.nombre AS finalidad, "+
																"getnomcentrocosto(s.centro_costo_solicitado) AS centro_costo, "+
																"count(1) AS cantidad, "+
																"ser.espos AS pos, "+
																"sp.solicitud_multiple AS multiple, "+
																"CASE WHEN (sp.frecuencia || ' ' || tf.nombre) IS NULL THEN '' ELSE (sp.frecuencia || ' ' || tf.nombre) END AS frecuencia "+
															"FROM solicitudes s "+
															"INNER JOIN sol_procedimientos sp on(sp.numero_solicitud=s.numero_solicitud) "+
															"INNER JOIN servicios ser ON(ser.codigo=sp.codigo_servicio_solicitado) "+
															"INNER JOIN cuentas c ON(s.cuenta=c.id) "+
															"INNER JOIN sub_cuentas sc ON(sc.ingreso = c.id_ingreso AND sc.nro_prioridad = 1) "+
															"INNER JOIN contratos con ON(sc.contrato=con.codigo) "+
															"INNER JOIN esquemas_tarifarios et ON(getEsquemaTarifarioSerArt(sc.sub_cuenta,sc.contrato,ser.codigo,'S')=et.codigo) "+
															"INNER JOIN finalidades_servicio fs ON(fs.codigo=sp.finalidad) "+
															"LEFT OUTER JOIN tipos_frecuencia tf ON (tf.codigo=sp.tipo_frecuencia) "+
															"WHERE s.cuenta=? and s.fecha_solicitud = ? and s.hora_solicitud=? "+
															"GROUP BY sp.codigo_servicio_solicitado, getnomcentrocosto(s.centro_costo_solicitado), ser.espos, sp.solicitud_multiple, sp.frecuencia || ' ' || tf.nombre,fs.nombre "+
															"ORDER BY procedimiento ";
		
		
		
		Collection col;
        try
             {               
                     if (con == null || con.isClosed()) 
                     {
                         DaoFactory myFactory =DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
                         con = myFactory.getConnection();
                     }
                     
                     logger.info("valor del sql >> "+solicitudesXCuentaFechaHoraStr+" >> "+codigoCuenta+" >> "+fechaSolicitud+" >> "+horaSolicitud);
                     
                     PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(solicitudesXCuentaFechaHoraStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
  					 ps.setInt(1,codigoCuenta);
  					 ps.setDate(2, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaSolicitud)));
  					 ps.setString(3, horaSolicitud);

  					 ResultSetDecorator resultset=new ResultSetDecorator(ps.executeQuery());
                     
                     col=UtilidadBD.resultSet2Collection(resultset);
                     return col;
                     
             }
       catch(SQLException e)
       {
           logger.error(" Error en la consulta de datos "+e.toString());
          
       }
        
        return null;
	}

	/**
	 * Metodo que devuelve el codigo del portatil asociado a la solicitud de proc, si no existe entonces -1
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static int obtenerPortatilSolicitud(Connection con, int numeroSolicitud,String consulta)
	{
		PreparedStatementDecorator ps;
		try 
		{
			logger.info("--->"+consulta+"----"+numeroSolicitud);
			ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,numeroSolicitud);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getInt(1);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * Metodo que devuelve el codigo del portatil asociado a un servicio, si no existe entonces -1
	 * @param con
	 * @param codigoServicio
	 * @return
	 */
	public static int obtenerPortatilServicio(Connection con, int codServicio)
	{
		String consulta="select getcodigoservicioportatil(?) FROM facturacion.servicios";
		
		PreparedStatementDecorator ps;
		try 
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("\n\n\n consulta : "+consulta);
			logger.info("\n codServicio :"+codServicio);
			ps.setInt(1,codServicio);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getInt(1);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return ConstantesBD.codigoNuncaValido;
	}

	public static String[] obtenerConsultaSolicitudProcedimientosReporte(Connection con, HashMap numerosSolicitudes)
	{
		String codigoSolicitudes=obtenerWhereFormatoMediaCarta(con, numerosSolicitudes);
		
		/**DCU 40->Cambio 1.52*******************************************/
		String descripcionServicio=" getnombreservicio(sp.codigo_servicio_solicitado, 0)";
		if(numerosSolicitudes.get("descripcionServicio")!=null)
		{
			descripcionServicio="'"+numerosSolicitudes.get("descripcionServicio").toString()+"'";
		}
		/****************************************************************/
		String consultaData=
			
			"SELECT getcodigoservicio(sp.codigo_servicio_solicitado, 0)  AS codigo,        " +
			//"  getnombreservicio(sp.codigo_servicio_solicitado, 0)       AS procedimiento, " +
			descripcionServicio+"                                        AS procedimiento, " +
			"  fs.nombre                                                 AS finalidad    , " +
			"  getnomcentrocosto(s.centro_costo_solicitado)              AS centro_costo , " +
			"  COUNT(1)                                                  AS cantidad     , " +
			"  CASE " +
			"    WHEN ser.espos =  " + ValoresPorDefecto.getValorTrueParaConsultas()+
			"    THEN 'SI' " +
			"    ELSE 'NO' " +
			"  END AS pos, " +
			"  CASE " +
			"    WHEN sp.solicitud_multiple =  " + ValoresPorDefecto.getValorTrueParaConsultas() +
			"    THEN 'SI' " +
			"    ELSE 'NO' " +
			"  END AS multiple, " +
			"  CASE " +
			"    WHEN (sp.frecuencia " +
			"      || ' ' " +
			"      || tf.nombre) IS NULL " +
			"    THEN '' " +
			"    ELSE (sp.frecuencia " +
			"      || ' ' " +
			"      || tf.nombre) " +
			"  END                AS frecuencia   , " +
			"  s.numero_solicitud AS num_solicitud, " +
			"  'Observaciones Caso Clínico: ' " +
			"  || getobservacionescasoclinico(s.numero_solicitud, s.tipo) AS observaciones, " +
			"  'Justificación Solicitud: ' " +
			"  || COALESCE(s.justificacion_solicitud,'') AS justificacion_solicitud " +
			"   FROM solicitudes s " +
			"INNER JOIN sol_procedimientos sp " +
			"     ON (sp.numero_solicitud=s.numero_solicitud) " +
			"INNER JOIN servicios ser " +
			"     ON (ser.codigo = sp.codigo_servicio_solicitado) " +
			"INNER JOIN cuentas c " +
			"     ON (s.cuenta = c.id) " +
			"INNER JOIN sub_cuentas sc " +
			"     ON (sc.ingreso  = c.id_ingreso " +
			"AND sc.nro_prioridad = 1) " +
			"INNER JOIN contratos con " +
			"     ON (sc.contrato = con.codigo) " +
			"INNER JOIN esquemas_tarifarios et " +
			"     ON (getEsquemaTarifarioSerArt(sc.sub_cuenta,sc.contrato,ser.codigo,'S') = et.codigo) " +
			"INNER JOIN finalidades_servicio fs " +
			"     ON (fs.codigo = sp.finalidad) " +
			"LEFT OUTER JOIN tipos_frecuencia tf " +
			"     ON (tf.codigo          = sp.tipo_frecuencia) "+
			"  WHERE " + codigoSolicitudes +" "+
			"GROUP BY sp.codigo_servicio_solicitado, " +
			"  s.centro_costo_solicitado           , " +
			"  ser.espos                           , " +
			"  sp.solicitud_multiple               , " +
			"  sp.frecuencia                       , " +
			"  tf.nombre                           , " +
			"  fs.nombre                           , " +
			"  s.numero_solicitud                  , " +
			"  s.tipo                              , " +
			"  s.justificacion_solicitud " +
			"  UNION " +
			" SELECT getcodigoservicio(scs.servicio, 0)     AS codigo       , " +
			"  getnombreservicio(scs.servicio, 0)           AS procedimiento, " +
			"  fs.nombre                                    AS finalidad    , " +
			"  getnomcentrocosto(s.centro_costo_solicitado) AS centro_costo , " +
			"  COUNT(1)                                     AS cantidad     , " +
			"  CASE " +
			"    WHEN ser.espos = " +ValoresPorDefecto.getValorTrueParaConsultas()+
			"    THEN 'SI' " +
			"    ELSE 'NO' " +
			"  END                AS pos          , " +
			"  'NO'               AS multiple     , " +
			"  ''                 AS frecuencia   , " +
			"  s.numero_solicitud AS num_solicitud, " +
			"  'Observaciones Caso Clínico: ' " +
			"  || getobservacionescasoclinico(s.numero_solicitud, s.tipo) AS observaciones, " +
			"  'Justificación Solicitud: ' " +
			"  || COALESCE(s.justificacion_solicitud,'') AS justificacion_solicitud " +
			"   FROM solicitudes s " +
			"INNER JOIN solicitudes_cirugia sci " +
			"     ON (sci.numero_solicitud=s.numero_solicitud) " +
			"INNER JOIN sol_cirugia_por_servicio scs " +
			"     ON (scs.numero_solicitud=s.numero_solicitud) " +
			"INNER JOIN servicios ser " +
			"     ON (ser.codigo = scs.servicio) " +
			"INNER JOIN cuentas c " +
			"     ON (s.cuenta = c.id) " +
			"INNER JOIN sub_cuentas sc " +
			"     ON (sc.ingreso  = c.id_ingreso " +
			"AND sc.nro_prioridad = 1) " +
			"INNER JOIN contratos con " +
			"     ON (sc.contrato = con.codigo) " +
			"INNER JOIN esquemas_tarifarios et " +
			"     ON (getEsquemaTarifarioSerArt(sc.sub_cuenta,sc.contrato,ser.codigo,'S') = et.codigo) " +
			"INNER JOIN finalidades_servicio fs " +
			"     ON (fs.codigo          = scs.finalidad) " +
			"  WHERE " + codigoSolicitudes +" "+
			"GROUP BY scs.servicio      , " +
			"  s.centro_costo_solicitado, " +
			"  ser.espos                , " +
			"  fs.nombre                , " +
			"  s.numero_solicitud       , " +
			"  s.tipo                   , " +
			"  s.justificacion_solicitud " ;
		
		
		String consultaEncabezado=
			"( "+
				"	SELECT "+ 
				"		'Paciente: '|| getnombrepersona(c.codigo_paciente) as nombres, "+ 
				"		p.tipo_identificacion  ||': '|| p.numero_identificacion as identificacion, "+ 
				"		'Edad: '|| historiaclinica.getedad2(p.fecha_nacimiento, s.fecha_solicitud) as edad,  "+
				"		'Teléfono: '||p.telefono as telefono, "+
				"		'Vía Ingreso: '||getnombreviaingreso(c.via_ingreso) as viaingreso,  "+
				"		'Cama: '||getcamacuenta(c.id, c.via_ingreso) as cama, "+
				"		'Área: '|| getnomcentrocosto(c.area) as area, "+
				"		'Régimen: '|| getnomtiporegimen(conv.tipo_regimen) as regimen, "+ 
				"		'Responsable: '|| conv.nombre as responsable,  "+
				"		'No.Autorización: '||s.numero_autorizacion as autorizacion, "+ 
				"		'C.C. Solicitante: '|| getnomcentrocosto(s.centro_costo_solicitante) as ccsolicitante, "+ 
				"		'Estado: '||getestadosolhis(s.estado_historia_clinica) as estado, "+ 
				"		'Fecha/Hora: '|| to_char(fecha_solicitud, 'DD/MM/YYYY') ||' - '||substr(s.hora_solicitud||'',1,5) as fechahora, "+ 
				"		'No. Orden: remplazameXConsecutivosOrdenes ' as consecutivoorden,  "+
				"		'Prioridad: '|| CASE WHEN urgente="+ValoresPorDefecto.getValorTrueParaConsultas() +" THEN 'Urgente' ELSE 'Normal' END AS prioridad, "+
				"		'Cuenta: '||c.id AS cuenta, "+
				"		'Ingreso: '||getconsecutivoingreso(c.id_ingreso) AS ingreso "+  
				"FROM  "+
				"		solicitudes s "+ 
				"		INNER JOIN cuentas c ON(c.id = s.cuenta) "+ 
				"		INNER JOIN personas p ON(p.codigo = c.codigo_paciente) "+ 
				"		INNER JOIN det_cargos dc ON(dc.solicitud = s.numero_solicitud) "+ 
				"		INNER JOIN convenios conv on(conv.codigo = dc.convenio) "+ 
				"	WHERE "+ 
						codigoSolicitudes+
				"	) "+
				"	UNION "+ 
				"	SELECT "+ 
				"		'Paciente: '|| getnombrepersona(c.codigo_paciente) as nombres, "+ 
				"		p.tipo_identificacion  ||': '|| p.numero_identificacion as identificacion, "+ 
				"		'Edad: '|| historiaclinica.getedad2(p.fecha_nacimiento, s.fecha_solicitud) as edad, "+ 
				"		'Teléfono: '||p.telefono as telefono, "+
				"		'Vía Ingreso: '||getnombreviaingreso(c.via_ingreso) as viaingreso, "+ 
				"		'Cama: '||getcamacuenta(c.id, c.via_ingreso) as cama, "+
				"		'Área: '|| getnomcentrocosto(c.area) as area, "+
				"		'Régimen: '|| getnomtiporegimen(conv.tipo_regimen) as regimen, "+ 
				"		'Responsable: '|| conv.nombre as responsable, "+ 
				"		'No.Autorización: '||s.numero_autorizacion as autorizacion, "+ 
				"		'C.C. Solicitante: '|| getnomcentrocosto(s.centro_costo_solicitante) as ccsolicitante, "+ 
				"		'Estado: '||getestadosolhis(s.estado_historia_clinica) as estado, "+ 
				"		'Fecha/Hora: '|| to_char(fecha_solicitud, 'DD/MM/YYYY') ||' - '||substr(s.hora_solicitud||'',1,5) as fechahora, "+ 
				"		'No. Orden: remplazameXConsecutivosOrdenes ' as consecutivoorden, "+ 
				"		'Prioridad: '|| CASE WHEN urgente="+ValoresPorDefecto.getValorTrueParaConsultas() +" THEN 'Urgente' ELSE 'Normal' END AS prioridad, "+
				"		'Cuenta: '||c.id AS cuenta, "+
				"		'Ingreso: '||getconsecutivoingreso(c.id_ingreso) AS ingreso "+
				"	FROM "+ 
				"		solicitudes s "+ 
				"		INNER JOIN cuentas c ON(c.id = s.cuenta) "+ 
				"		INNER JOIN personas p ON(p.codigo = c.codigo_paciente) "+ 
				"		INNER JOIN solicitudes_cirugia sci ON (sci.numero_solicitud = s.numero_solicitud) "+
				"		INNER JOIN sub_cuentas sc ON (sc.sub_cuenta = sci.sub_cuenta) "+
				"		INNER JOIN convenios conv ON (conv.codigo = sc.convenio) "+ 
				"	WHERE "+ codigoSolicitudes; 
		
		String consultaMedico=
			"( "+
			"		SELECT "+ 
			"			getnombremedico(m.codigo_medico) "+ 
			"			||', '|| m.numero_registro "+ 
			"			||', ESPECIALIDADES: ' "+
			"			|| getespecialidadesmedico(u.login, ',') AS medico_firma "+ 
			"	FROM "+ 
			"			solicitudes s "+
			"			INNER JOIN sol_procedimientos sp on (sp.numero_solicitud=s.numero_solicitud) "+
			"			INNER JOIN medicos m on (m.codigo_medico=s.codigo_medico) "+
			"			INNER JOIN usuarios u ON (m.codigo_medico = u.codigo_persona) "+
			"		WHERE "+ 
						codigoSolicitudes+
			"	) "+
			"	UNION "+
			"	( "+
			"		SELECT "+ 
			"			getnombremedico(m.codigo_medico) "+ 
			"			||', '|| m.numero_registro "+ 
			"			||', ESPECIALIDADES: ' "+
			"			|| getespecialidadesmedico(pq.usuario, ',') AS medico_firma "+ 
			"	FROM "+ 
			"			solicitudes s "+
			"			INNER JOIN solicitudes_cirugia sc on (sc.numero_solicitud=s.numero_solicitud) "+
			"			INNER JOIN peticion_qx pq ON (sc.codigo_peticion = pq.codigo) "+ 
			"			INNER JOIN usuarios u ON (u.login = pq.usuario) "+ 
			"			INNER JOIN medicos m ON (m.codigo_medico = u.codigo_persona) "+
			"		WHERE  "+
						codigoSolicitudes+
			"	)";
		
		return new String[]{consultaEncabezado, consultaData, consultaMedico};
	}

	
	/**
	 * @param con
	 * @param numerosSolicitudes
	 * @return
	 */
	public static String obtenerWhereFormatoMediaCarta(Connection con, HashMap numerosSolicitudes)
	{
		String codigoSolicitudes =	"", numeroSolicitudes = "";
		//Recorremos la cadena de string para visualizar y separar que tipo de solicitudes se tomaron para hacer el filtrado
		int numReg = Utilidades.convertirAEntero(numerosSolicitudes.get("numRegistros")+"");
		if (numReg == 1)
		{
			if (UtilidadCadena.noEsVacio(numerosSolicitudes.get("numeroSolicitud_0")+""))
				numeroSolicitudes = numerosSolicitudes.get("numeroSolicitud_0")+"";
		}
		else
		{
			if (numReg > 1)
			{
				for (int i=0; i<numReg; i++)
				{
					if (UtilidadCadena.noEsVacio(numerosSolicitudes.get("numeroSolicitud_"+i)+""))
					{
						if (i == 0)
							numeroSolicitudes = numerosSolicitudes.get("numeroSolicitud_"+i)+"";
						else
							numeroSolicitudes += ", "+numerosSolicitudes.get("numeroSolicitud_"+i)+"";
					}
				}
			}
		}
		codigoSolicitudes = "s.numero_solicitud IN ("+numeroSolicitudes+") ";
		
		
		logger.info("\n\n\n\nconulta >>>> "+codigoSolicitudes);
		return codigoSolicitudes;
	}

	public static HashMap valAnulacion(Connection ac_con, int numeroSolicitud) {
		
		String validacionAnulacion="select es.razon_social as razonsocial  " +
				"from ordenes.solicitudes s " +
				
				// PermitirAutorizarDiferenteDeSolicitudes
				"inner  JOIN ordenes.auto_entsub_solicitudes aess on (aess.numero_solicitud=s.numero_solicitud) " +
				"inner  JOIN manejopaciente.autorizaciones_entidades_sub aes on (aes.consecutivo = aess.autorizacion_ent_sub) " +
				
				"inner join facturacion.entidades_subcontratadas es on (aes.entidad_autorizada_sub=es.codigo_pk) " +
				"WHERE s.numero_solicitud=? and aes.estado='"+ConstantesIntegridadDominio.acronimoAutorizado+"'"; 
		
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		
		PreparedStatementDecorator ps;
		
		try
		{
			ps= new PreparedStatementDecorator(ac_con.prepareStatement(validacionAnulacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,numeroSolicitud);
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
			
			
		}
		catch (SQLException e)
		{
			logger.info("\n\n ERROR.  SQL------>>>>>>"+e);
			
			e.printStackTrace();
		}
		return resultados;	
	
	}

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean finalizarSolicitudMultiple(Connection con,String numeroSolicitud) 
	{
		boolean resultado=false;
		
		String consulta="UPDATE sol_procedimientos set finalizada = "+ValoresPorDefecto.getValorTrueParaConsultas()+" where numero_solicitud="+numeroSolicitud;
		
		PreparedStatementDecorator ps;
		try 
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.executeUpdate();
			resultado=true;
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return resultado;
	}
	
	/**
	 * M&eacute;todo encargado de consultar los servicios
	 * asociados a la solicitud de procedimientos
	 * @param Connection con, String numeroDocumento
	 * @return ArrayList<DtoServicios>
	 * @author Diana Carolina G
	 */
	public static DtoProcedimiento buscarServiciosSolicitudProcedimientos(Connection con,
			int numeroSolicitud, int codigoTarifario)
	{
		DtoProcedimiento solcitudesProcedimiento = new DtoProcedimiento();
		
		String consulta= "select ser.codigo, ref.descripcion, sol.numero_solicitud, s.centro_costo_solicitado "+
				"from ordenes.sol_procedimientos sol, facturacion.servicios ser, facturacion.referencias_servicio ref, facturacion.tarifarios_oficiales tar, ordenes.solicitudes s "+
				"where sol.codigo_servicio_solicitado = ser.codigo "+
				"and ref.servicio = ser.codigo "+
				"and ref.tipo_tarifario = tar.codigo "+
				"and s.numero_solicitud = sol.numero_solicitud "+
				"and sol.numero_solicitud = ? and tar.codigo = ?"; 
		
		try {
			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1, numeroSolicitud);
			ps.setInt(2, codigoTarifario);
			
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				solcitudesProcedimiento.setCodigoServicioSolicitado(rs.getInt(1));
				solcitudesProcedimiento.setNombreServicioSolicitado(rs.getString(2));
				solcitudesProcedimiento.setNumeroSolicitud(rs.getInt(3));
				solcitudesProcedimiento.setCodigoCentroCostoSolicitado(rs.getInt(4));
			}
			
		} catch (SQLException e) {
			logger.warn(e + " Error en buscarServiciosSolicitudProcedimientos de : SqlBaseSolicitudProcedimientoDao "+e.toString());
		}
		
		
		return solcitudesProcedimiento;
	}
	
	/**
	 * M&eacute;todo encargado de consultar los servicios
	 * asociados a la solicitud de procedimientos de tipo 
	 * @param Connection con, String numeroDocumento
	 * @return ArrayList<DtoServicios>
	 * @author Diana Carolina G
	 */
	public static DtoProcedimiento buscarServiciosSolicitudProcedimientosC(Connection con,
			int numeroSolicitud, int codigoTarifario)
	{
		DtoProcedimiento solcitudesProcedimiento = new DtoProcedimiento();
		
		String consulta= "select ser.codigo, ref.descripcion, sol.numero_solicitud, s.centro_costo_solicitado "+
				"from salascirugia.sol_cirugia_por_servicio sol, facturacion.servicios ser, facturacion.referencias_servicio ref, facturacion.tarifarios_oficiales tar, ordenes.solicitudes s "+
				"where sol.servicio = ser.codigo "+
				"and ref.servicio = ser.codigo "+
				"and ref.tipo_tarifario = tar.codigo "+
				"and s.numero_solicitud = sol.numero_solicitud "+
				"and sol.numero_solicitud = ? and tar.codigo = ?"; 
		
		try {
			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1, numeroSolicitud);
			ps.setInt(2, codigoTarifario);
			
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				solcitudesProcedimiento.setCodigoServicioSolicitado(rs.getInt(1));
				solcitudesProcedimiento.setNombreServicioSolicitado(rs.getString(2));
				solcitudesProcedimiento.setNumeroSolicitud(rs.getInt(3));
				solcitudesProcedimiento.setCodigoCentroCostoSolicitado(rs.getInt(4));
			}
			
		} catch (SQLException e) {
			logger.warn(e + " Error en buscarServiciosSolicitudProcedimientos de : SqlBaseSolicitudProcedimientoDao "+e.toString());
		}
		
		
		return solcitudesProcedimiento;
	}
	
	/**
	 * @param conn
	 * @param codigoMedico
	 * @return datos del medico que anula 
	 * @throws SQLException
	 */
	public static String consultarDatosMedicoAnulacion(Connection conn,Integer codigoMedico) throws SQLException{
		
		String res=" "; 
			
			String consulta=" select per.primer_nombre||' '||per.segundo_nombre||' '||per.primer_apellido||' '||per.segundo_apellido||' - ' " +
			" ||med.numero_registro||' '||GETESPECIALIDADESMEDICO1(med.codigo_medico,',') as nombre_Medico_Anulacion  " +
			" from medicos med join personas per on(med.codigo_medico=per.codigo)   " +
			" where  med.codigo_medico =?"; 
			PreparedStatement pst = conn.prepareStatement(consulta);
			pst.setInt(1,codigoMedico);
			ResultSet rs = pst.executeQuery();
			
			if(rs.next()){
				res=rs.getString("nombre_Medico_Anulacion");
			}
			
		
		
		return res;
	}
	
}