/*
 * @(#)SqlBaseAdmisionHospitalariaDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

/**
 * Esta clase implementa la funcionalidad común a todas (o casi todas)
 * las BD utilizadas en axioma, en general se trata de las consultas que
 * utilizan SQL estándar. Métodos particulares a Admisión Hospitalaria
 *
 *	@version 1.0, Mar 26, 2004
 */
public class SqlBaseAdmisionHospitalariaDao
{
	
	/**
	 * Para manejar los logs de esta clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseAdmisionHospitalariaDao.class);


	/**
	 * Cadena constante con el <i>statement</i> necesario para cargar una
	 * Admision Hospitalaria usando una BD Genérica.
	 */
	// "adh.numero_autorizacion as numeroAutorizacion, " +
	// "adh.numero_autorizacion as numeroAutorizacion, " +
	private static final String cargarAdmisionHospitalaria="SELECT " +
			"adh. cuenta as codigoCuenta, " +
			"adh.fecha_admision as fechaAdmision, " +
			"adh.hora_admision as horaAdmision, " +
			"adh.codigo as codigoAdmisionHospitalaria, " +
			"adh.cama as codigoCama, " +
			"coalesce(cam.numero_cama,'') as nombreCama, " +
			"coalesce(tipuscam.nombre,'') as tipoUsuarioCama, " +
			"coalesce(cam.descripcion,'') as descripcionCama, " +
			"coalesce(cam.estado,"+ConstantesBD.codigoNuncaValido+") as estadoCama, " +
			"coalesce(ccosto.nombre,'') as nombreCentroCostoCama, " +
			"adh.estado_admision as codigoEstado, " +
			"estadm.nombre as nombreEstado, " +
			"adh.diagnostico_admision as codigoDiagnostico, " +
			"adh.diagnostico_cie_admision as codigoCIEDiagnostico, " +
			"diag.nombre as nombreDiagnostico, " +
			"origen_admision_hospitalaria as codigoOrigenAdmision, " +
			"causa_externa as codigoCausaExterna, " +
			"cexter.nombre as causaExterna, " +
			"origen_admision_hospitalaria as codigoOrigenAdmision, " +
			"origadm.nombre as origenAdmision, " +
			"adh.login_usuario as loginUsuario, " +
			"per.numero_identificacion as numeroIdentificacionMedico, " +
			"per.tipo_identificacion as tipoIdentificacionMedico, " +
			"per.primer_nombre as primerNombreMedico, " +
			"per.segundo_nombre as segundoNombreMedico, " +
			"per.primer_apellido as primerApellidoMedico, " +
			"per.segundo_apellido as segundoApellidoMedico, " +			
			"coalesce(getnomhabitacioncama(cam.codigo),'') AS habitacion," +
			"coalesce(getnomtipohabitacioncama(cam.codigo),'') As tipo_habitacion, " +
			"coalesce(getnombrepisocama(cam.codigo),'') AS piso " +
			"from " +
			"admisiones_hospi adh " +
			"LEFT OUTER JOIN camas1 cam ON(cam.codigo=adh.cama) " +						
			"LEFT OUTER JOIN centros_costo ccosto ON(ccosto.codigo=cam.centro_costo) " +
			"LEFT OUTER JOIN tipos_usuario_cama tipuscam ON(tipuscam.codigo=cam.tipo_usuario_cama) " +
			"INNER JOIN estados_admision estadm ON(estadm.codigo=adh.estado_admision) " +
			"INNER JOIN diagnosticos diag ON(diag.acronimo=adh.diagnostico_admision) " +
			"INNER JOIN causas_externas cexter ON(cexter.codigo=adh.causa_externa) " +
			"INNER JOIN ori_admision_hospi origadm ON(origadm.codigo=adh.origen_admision_hospitalaria) " +
			"INNER JOIN personas per ON(per.codigo=adh.codigo_medico) " +
			"where adh.codigo=?";

	/**
	 * Cadena constante con el <i>statement</i> necesario para modificar una
	 * Admision Hospitalaria usando una BD Genérica.
	 */
	// "numero_autorizacion=? " +
	private static final String modificarAdmisionHospitalaria="UPDATE admisiones_hospi set " +
			"origen_admision_hospitalaria=?, " +
			"diagnostico_admision=?, " +
			"diagnostico_cie_admision=?, " +
			"causa_externa=?, " +			
			"where codigo=?";

	
	/**
	 * Cadena constante con el <i>statement</i> necesario para cambiar
	 * una cama de una admisión hospitalaria usando una BD Genérica. 
	 */
	private static final String cambiarCamaStr = "UPDATE admisiones_hospi SET cama = ? WHERE cuenta = ? ";
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para cambiar
	 * el estado de una cama usando una BD Genérica. 
	 */
	private static final String cambiarEstadoCamaStr = "UPDATE camas1 SET estado =? WHERE codigo =?";

	/**
	 * Cadena constante con el <i>statement</i> necesario para actualizar
	 * el estado de una Admision Hospitalaria a egresado tras un egreso usando 
	 * una BD Genérica. 
	 */
	private static final String actualizarAdmisionViaEgresoStr="UPDATE admisiones_hospi set estado_admision=2 where codigo=?";
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para actualizar
	 * el estado de una Admision Hospitalaria a egresado tras una reversión de
	 * egreso usando una BD Genérica. 
	 */
	private static final String actualizarAdmisionViaReversionEgresoStr="UPDATE admisiones_hospi set estado_admision=1, cama=? where cuenta=?";
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para ocupar
	 * de una Admision Hospitalaria tras una reversión de egreso usando 
	 * una BD Genérica. 
	 */
	private static final String ocuparCamaStr="UPDATE camas1 set estado=1 where codigo=?";

	private static final String cargarUltimoTrasladoStr = "SELECT fecha_traslado AS fechaTraslado, hora_traslado AS horaTraslado "
																						+ "FROM his_cama_pac "
																						+ "WHERE codigo_paciente = ? "
																						+ "ORDER BY fecha_traslado DESC, hora_traslado DESC ";

	// + "ah.numero_autorizacion as numeroautorizacion "
	private static final String cargarUltimaAdmisionStr = "SELECT ah.fecha_admision as fechaAdmision, "
			+ "ah.hora_admision as horaAdmision, "
			+ "ah.codigo as codigoAdmisionHospitalaria, "
			+ "coalesce(ah.cama,-1) as codigoCama, " 
			+ "coalesce(cam.numero_cama,'') as nombreCama, "  
			+ "coalesce(cam.estado ) as estadoCama, "
			+ "ah.estado_admision as codigoEstado, "			
			+ "FROM admisiones_hospi ah" +
			" INNER JOIN cuentas cu ON (cu.id=ah.cuenta) " +
			" LEFT OUTER JOIN camas1 cam ON(cam.codigo = ah.cama ) "+
			"WHERE  "																					
			+ " cu.codigo_paciente = ? "
			+ "ORDER BY ah.fecha_admision DESC, ah.hora_admision DESC ";

	/**
	 * Una vez se crea una admision hospitalaria hay que ocupar la cama
	 */
	private static final String ocuparCama="UPDATE camas1 set estado =1 where codigo=?";
	

	/**
	 * Cadena constante con el <i>statement</i> necesario para consultar el nombre de la cama asociada a esta admision 
	 */	
	private static final String consultarCamaStr = "" +
	"SELECT " +
	" ' P.' || substr(pis.nombre,0,16) || '  H.' || substr(hab.nombre,0,16)|| '  C.' || c.numero_cama as cama, " +	
	"c.descripcion," +
	"c.codigo " +
	"FROM admisiones_hospi ah, camas1 c " +
	"INNER JOIN habitaciones hab ON(hab.codigo = c.habitacion) " +
	"INNER JOIN pisos pis ON(pis.codigo = hab.piso) " +
	"WHERE ah.codigo = ? AND cama = c.codigo";
	
	/**
	 * Cadena Sql para consultar los datos de la cama actual en la que está el paciente 
	 */
	private static final String consultaDatosCamaActualStr = "SELECT tc.fecha_grabacion AS fecha_grabacion, " +
															 "tc.hora_grabacion AS hora_grabacion," +
															 "cam.codigo AS codigo_cama, " +
															 "cam.habitacion AS habitacion, " +
															 " ' P.' || substr(pis.nombre,0,16) || '  H.' || substr(hab.nombre,0,16) || '  C.' || cam.numero_cama as numero_cama, " +															 
															 "cam.descripcion AS descripcion_cama, " +
															 "cc.nombre AS centro_costo " +
															 "FROM traslado_cama tc " +
															 "INNER JOIN admisiones_hospi ah ON(ah.cuenta=tc.cuenta) " +
															 "INNER JOIN camas1 cam ON (tc.codigo_nueva_cama=cam.codigo) " +
															 "INNER JOIN habitaciones hab ON(hab.codigo = cam.habitacion) " +
															 "INNER JOIN pisos pis ON(pis.codigo = hab.piso) " +
															 "INNER JOIN centros_costo cc ON (cam.centro_costo=cc.codigo) " +
															 "WHERE ";

	/**
	 * Consulta el número de traslados que tiene un paciente específico
	 */
	private static final String consultarNroTrasladosCamaStr = "SELECT count(1) as cantidad FROM traslado_cama tc inner join admisiones_hospi ah ON(ah.cuenta=tc.cuenta) WHERE ah.codigo=?";
	
	/**
	 * Método que implementa la consulta de una Admision Hospitalaria en una
	 * BD Genérica
	 * @see com.princetonsa.dao.AdmisionHospitalariaDao#cargar(Connection, int)
	 */
	public static ResultSetDecorator cargar(Connection con, int codigoAdmisionHospitalaria) throws SQLException
	{
		try
		{
			PreparedStatementDecorator cargarAdmisionHospitalariaStatement= new PreparedStatementDecorator(con.prepareStatement(cargarAdmisionHospitalaria,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarAdmisionHospitalariaStatement.setInt(1, codigoAdmisionHospitalaria);
			return new ResultSetDecorator(cargarAdmisionHospitalariaStatement.executeQuery());
		}
		catch (SQLException e)
		{
			//Por alguna razón no salió bien
			logger.warn("cargar" + e.toString());
				
			throw e;
		}
	}
	
	/**
	 * Método que implementa la modificación de una Admision Hospitalaria en
	 * una BD Genérica
	 * @see com.princetonsa.dao.AdmisionHospitalariaDao#modificar(Connection, int, String, String, int, String, int)
	 */
	public static int modificar(Connection con,  int origen, String codigoDiagnostico, String codigoCIEDiagnostico, int codigoCausaExterna, /*String numeroAutorizacion, */int codigoAdmisionHospitalaria) throws SQLException
	{
		PreparedStatement pst=null;
		int result=0;
		try
		{
			if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}
			pst= con.prepareStatement(modificarAdmisionHospitalaria,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1,origen);
			pst.setString(2, codigoDiagnostico);
			pst.setString(3, codigoCIEDiagnostico);
			pst.setInt(4,codigoCausaExterna);
			//modificarAdmisionHospitalariaStatement.setString(5, numeroAutorizacion);
			pst.setInt(5,codigoAdmisionHospitalaria);
				
			result= pst.executeUpdate();
		}
		catch(Exception e){
			Log4JManager.error("ERROR modificar", e);
		}
		finally{
			try{
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return result;
	}
	
	/**
	 * Método que implementa el paso de la cama de una Admision Hospitalaria a
	 * estado desinfección en una BD Genérica
	 * 
	 * @see com.princetonsa.dao.AdmisionHospitalariaDao#pasarAdmisionAEgresoTransaccional (Connection , int , String ) throws SQLException
	 */
	// en este metodo el parameto de entrada institucion no se esta utilizando en el cuerpo del mismo 
	// fe comentaria la parte donde se usa.
	public static int pasarAdmisionAEgresoTransaccional (Connection con, int numeroAdmision, String estado, int institucion) throws SQLException
	{
		logger.info("\n entre a pasarAdmisionAEgresoTransaccional numeroAdmision-->"+numeroAdmision+"   estado--> "+estado+"   institucion -->"+institucion);
		int resp0=0, resp1=1, resp2=0;
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		try
		{
			if (con == null || con.isClosed()) 
			{
				//Como es transaccional NO voy a tratar de abrir una nueva conexión, sino que mandaré una excepction
				throw new SQLException ("Error SQL: Conexión cerrada");
			}
			if (estado==null)
			{
				throw new SQLException ("Error SQL: No se seleccionó ningun estado para la transacción");
			}

			if (estado.equals("empezar"))
			{
			    if (myFactory.beginTransaction(con))
			    {
			        resp0=1;
			    }
			    else
			    {
			        resp0=0;
			    }
			}	
			else
			{
				//De todas maneras así no sea transacción debo dejar en claro que todo salio bien
				resp0=1;
			}
			
			//statements 

			/**PreparedStatementDecorator pasarCamaADesinfeccionStatement= new PreparedStatementDecorator(con.prepareStatement(pasarCamaADesinfeccionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pasarCamaADesinfeccionStatement.setInt(1,Integer.parseInt(ValoresPorDefecto.getCodigoEstadoCama(institucion)+""));
			pasarCamaADesinfeccionStatement.setInt(2, numeroAdmision);
			resp1=pasarCamaADesinfeccionStatement.executeUpdate();**/

			PreparedStatementDecorator actualizarAdmisionViaEgresoStatement= new PreparedStatementDecorator(con.prepareStatement(actualizarAdmisionViaEgresoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			actualizarAdmisionViaEgresoStatement.setInt(1, numeroAdmision);
			resp2=actualizarAdmisionViaEgresoStatement.executeUpdate();
			actualizarAdmisionViaEgresoStatement.close();
			if (resp0<1||resp1<1||resp2<1)
			{
			    myFactory.abortTransaction(con);
				return 0;
			}
			else
			{
				if (estado.equals("finalizar"))
				{
				    myFactory.endTransaction(con);
				}

				return resp0;
			}
		}
		catch (SQLException e)
		{
			
			logger.warn("Excepción en pasarCamaADesinfeccionTransaccional " + e);
			
			
			throw e;
		}
	}

	/**
	 * Método que implementa el efecto de una reversión de egreso en una 
	 * Admision Hospitalaria en una BD Genérica
	 * 
	 * @see com.princetonsa.dao.AdmisionHospitalariaDao#reversarEgresoYAdmisionTransaccional (Connection , int , int , String) throws SQLException
	 */
	public static int reversarEgresoYAdmisionTransaccional (Connection con, int idCuenta, int codigoCama, String estado) throws SQLException
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		int resp0=0, resp1=0, resp2=0;
		try
		{
			if (con == null || con.isClosed()) 
			{
				//Como es transaccional NO voy a tratar de abrir una nueva conexión, sino que mandaré una excepction
				throw new SQLException ("Error SQL: Conexión cerrada");
			}
			if (estado==null)
			{
				throw new SQLException ("Error SQL: No se seleccionó ningun estado para la transacción");
			}

			if (estado.equals("empezar"))
			{
			    if (myFactory.beginTransaction(con))
			    {
			        resp0=1;
			    }
			    else
			    {
			        resp0=0;
			    }
			}	
			else
			{
				//De todas maneras así no sea transacción debo dejar en claro que todo salio bien
				resp0=1;
			}
			
			//Si es admision con cama se actualiza el estado de la cama
			if(codigoCama>0)
			{
				PreparedStatementDecorator ocuparCamaStatement= new PreparedStatementDecorator(con.prepareStatement(ocuparCamaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ocuparCamaStatement.setInt(1, codigoCama);
				resp1=ocuparCamaStatement.executeUpdate();
				ocuparCamaStatement.close();
			}
			else
				resp1 = 1;

			
			PreparedStatementDecorator actualizarAdmisionViaReversionEgresoStatement= new PreparedStatementDecorator(con.prepareStatement(actualizarAdmisionViaReversionEgresoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			if(codigoCama>0)
				actualizarAdmisionViaReversionEgresoStatement.setInt(1, codigoCama);
			else
				actualizarAdmisionViaReversionEgresoStatement.setNull(1, Types.INTEGER);
			actualizarAdmisionViaReversionEgresoStatement.setInt(2, idCuenta);
			resp2=actualizarAdmisionViaReversionEgresoStatement.executeUpdate();
			actualizarAdmisionViaReversionEgresoStatement.close();
			if (resp0<1||resp1<1||resp2<1)
			{
			    myFactory.abortTransaction(con);
				return 0;
			}
			else
			{
				if (estado.equals("finalizar"))
				{
				    myFactory.endTransaction(con);
				}

				return resp0;
			}
		}
		catch (SQLException e)
		{
			logger.warn("Excepción en pasarCamaADesinfeccionTransaccional " + e);
			throw e;
		}

	}
	
	/**
	 * Carga en una cadena la fecha y hora de la última asignación o cambio
	 * de cama para el paciente dado.
	 * @param 	Connection, con
	 * @param 	String, tipoIdPaciente. Tipo de identificación del paciente
	 * @param 	String, numeroIdPaciente. Número de identificación del paciente
	 * @param 	int, codAdmision. Código de admisión del paciente.
	 * @return 		String, Cadena con la fecha y la hora --> fecha"- "hora
	 * @throws 	SQLException
	 */	
	public static String cargarUltimaFechaHoraRegistroCama(Connection con, int codigoPaciente) throws SQLException
	{
		String fechaHora = new String();
		PreparedStatement pst=null;
		ResultSet rs=null;
		try{
			pst = con.prepareStatement(cargarUltimoTrasladoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, codigoPaciente);
	
			rs = pst.executeQuery();
						
			if(rs.next() )
			{
				fechaHora = UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fechaTraslado"));
				fechaHora += "-";
				fechaHora += rs.getString("horaTraslado");			
			}
		}
		catch(Exception e){
			Log4JManager.error("ERROR cargarUltimaFechaHoraRegistroCama", e);
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
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return fechaHora;
	}

	/**
	 * Carga información básica de la última admisión del paciente
	 * @param 	Connection, con
	 * @param 	int, codigoPaciente
	 * @return 		ResultSet
	 * @throws SQLException
	 */			
	public static ResultSetDecorator cargarUltimaAdmision(Connection con, int codigoPaciente) throws SQLException
	{
		PreparedStatementDecorator ultimaAdmisionSttmt =  new PreparedStatementDecorator(con.prepareStatement(cargarUltimaAdmisionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		ultimaAdmisionSttmt.setInt(1, codigoPaciente);
		
		return new ResultSetDecorator(ultimaAdmisionSttmt.executeQuery());
	}

	/**
	 * 
	 * @param con
	 * @param medico
	 * @param cuenta
	 * @return
	 */
	public static int actualizarMedico (Connection con, int medico, int cuenta){
		int resultado=0;
		PreparedStatement pst=null;
		try
		{
			String cadena="UPDATE admisiones_hospi SET codigo_medico="+medico+" WHERE cuenta="+cuenta;
			pst = con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			
			if(pst.executeUpdate()>0)
			{
				resultado=1;
			}
		}
		catch(Exception e){
			Log4JManager.error("ERROR actualizarMedico", e);
		}
		finally{
			try{
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return resultado;
	}
	
	/**
	 * 
	 * @param con
	 * @param cama
	 * @param cuenta
	 * @return
	 */
	
	public static int cambiarCama (Connection con, int cama, int cuenta)
	{
		int cambio = 1;
		PreparedStatement pst=null;
		PreparedStatement pst2=null;
		try
		{
			logger.info("========= cama ====="+cama);
			logger.info("========= cuenta ==="+cuenta);
			
			pst = con.prepareStatement(cambiarEstadoCamaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, ConstantesBD.codigoEstadoCamaOcupada);
			pst.setInt(2, cama);
			cambio = pst.executeUpdate();
			if( cambio <= 0 )
			{
				return cambio;
			}		
			
			pst2 =  con.prepareStatement(cambiarCamaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst2.setInt(1, cama);
			pst2.setInt(2, cuenta);
			
			cambio = pst2.executeUpdate();
			if( cambio <= 0 )
			{
				return cambio;
			}
		}
		catch(Exception e){
			Log4JManager.error("ERROR cambiarCama", e);
		}
		finally{
			try{
				if(pst2 != null){
					pst2.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return cambio;
	}


	

	/**
	 * Método que implementa la inserción de una Admision Hospitalaria en
	 * una BD Genérica
	 * @see com.princetonsa.dao.AdmisionHospitalariaDao#insertar(Connection , int , int , int , String , String , int , String , String , int cuenta, String , String , String , String )  
	 */
	public static int insertar(Connection con, int origen, int codigoMedico, int codigoCama, String codigoDiagnostico, String codigoCIEDiagnostico, int codigoCausaExterna, /*String numeroAutorizacion, */String loginUsuario, int cuenta, String hora, String fecha, String insertarAdmisionHospitalaria, String buscarCodigoAdmisionRecienInsertada) throws SQLException
	{
		int resp0, resp1=0, resp2=0;
		int result=0;
		PreparedStatement pst=null;
		PreparedStatement pst2=null;
		PreparedStatement pst3=null;
		ResultSet rs=null;
		try
		{
			DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
			if (con == null || con.isClosed()) 
			{
				con = myFactory.getConnection();
			}
			
			//Vamos a iniciar una transacción luego ponemos
			//el autocommit en false
			con.setAutoCommit(false);

			if (myFactory.beginTransaction(con))
			{
				resp0=1;
			}
			else
			{
				resp0=0;
			}

			pst = con.prepareStatement(insertarAdmisionHospitalaria,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			//insertarAdmisionHospitalariaStatement.setString(1, origen);
			pst.setInt(1, origen);
			pst.setString(2, UtilidadFecha.conversionFormatoFechaABD(fecha));
			pst.setString(3, hora);
			pst.setInt(4, codigoMedico);
			pst.setInt(5, codigoCama);
			pst.setString(6, codigoDiagnostico);
			pst.setString(7, codigoCIEDiagnostico);
			pst.setInt(8, codigoCausaExterna);
			//insertarAdmisionHospitalariaStatement.setString(9, numeroAutorizacion);
			pst.setString(9, loginUsuario);
			pst.setInt(10, cuenta);

			resp1=pst.executeUpdate();
			
			pst2= con.prepareStatement(ocuparCama,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst2.setInt(1, codigoCama);
			resp2 =pst2.executeUpdate();

				if (resp0<0||resp1<1||resp2<1)
				{
					// Terminamos la transaccion, sea con un rollback o un commit.
					myFactory.abortTransaction(con);
					con.setAutoCommit(true);
					result=0;
					return result;
				}
				else
				{
					pst3= con.prepareStatement(buscarCodigoAdmisionRecienInsertada,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
					rs=pst3.executeQuery();
					if (rs.next())
					{
						myFactory.endTransaction(con);
						result=rs.getInt("codigoAdmision");
						return result;
					}
					else
					{
						myFactory.abortTransaction(con);
						result=0;
						return result;
					}
				}
		}
		catch(Exception e){
			Log4JManager.error("ERROR insertar", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst3 != null){
					pst3.close();
				}
				if(pst2 != null){
					pst2.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return result;
	}


	/**
	 * Método que implementa la inserción de una Admision Hospitalaria en una BD
	 * Genérica, permitiendo al usuario definir la transaccionalidad
	 * 
	 * @see com.princetonsa.dao. AdmisionHospitalariaDao#insertarTransaccional (Connection , int , String , String , int , String , String , int , String , String , int , String , String , String ) 
	 */
	
	public static int insertarTransaccional(Connection con, int origen, int codigoMedico, int codigoCama, 
			String codigoDiagnostico, String codigoCIEDiagnostico, int codigoCausaExterna, /*String numeroAutorizacion, */String loginUsuario, 
			int cuenta, String hora, String fecha, String insertarAdmisionHospitalaria, 
			String buscarCodigoAdmisionRecienInsertada, String estado, int institucion) throws SQLException
	{
		int resp0=0, resp1=0, resp2=0;
		PreparedStatement pst=null;
		PreparedStatement pst2=null;
		PreparedStatement pst3=null;
		ResultSet rs=null;
		try
		{
			DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
			if (con == null || con.isClosed()) 
			{
				//Como es transaccional NO voy a tratar de abrir una nueva conexión, sino que mandaré una excepction
				throw new SQLException ("Error SQL: Conexión cerrada");
			}
			if (estado==null)
			{
				throw new SQLException ("Error SQL: No se seleccionó ningun estado para la transacción");
			}
		
			if (estado.equals(ConstantesBD.inicioTransaccion))
			{
				if (myFactory.beginTransaction(con))
				{
					resp0=1;
				}
				else
				{
					resp0=0;
				}
			}	
			else
			{
				//De todas maneras así no sea transacción debo dejar en claro que todo salio bien
				resp0=1;
			}
			pst =  con.prepareStatement(insertarAdmisionHospitalaria,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			
			String consecutivo=UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoAdmisionHospitalizacion, institucion);
			//consecutivo = "99999999";
			
			
			if(Utilidades.validarExisteConsecutivo(consecutivo)){
				pst.setInt(1, Utilidades.convertirAEntero(consecutivo));
				pst.setInt(2, origen);
				pst.setDate(3, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fecha)));
				pst.setString(4, hora);
				
				if(codigoMedico>0){
					pst.setInt(5, codigoMedico);
				}else{
					pst.setNull(5, Types.INTEGER);
				}
					
				if(codigoCama>0){
					pst.setInt(6, codigoCama);
				}else{
					pst.setNull(6, Types.INTEGER);
				}
					
				pst.setString(7, codigoDiagnostico);
				pst.setString(8,codigoCIEDiagnostico);
				pst.setInt(9, codigoCausaExterna);
				//insertarAdmisionHospitalariaStatement.setString(9, numeroAutorizacion);
				pst.setString(10, loginUsuario);
				pst.setInt(11, cuenta);
		
				resp1=pst.executeUpdate();
				UtilidadBD.cambiarUsoFinalizadoConsecutivo(con,ConstantesBD.nombreConsecutivoAdmisionHospitalizacion,institucion, consecutivo, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
			}else{
				
				myFactory.abortTransaction(con);
				resp0 = 0;
			}
			if(codigoCama>0)
			{
				pst2= con.prepareStatement(ocuparCama,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
				pst2.setInt(1, codigoCama);
				resp2 =pst2.executeUpdate();
			}
			else
				resp2 = 1;
			
			logger.info("resultado 0=>"+resp0);
			logger.info("resultado 1=>"+resp1);
			logger.info("resultado 2=>"+resp2);
			if (resp0<1||resp1<1||resp2<1)
			{
				// Terminamos la transaccion, sea con un rollback o un commit.
				myFactory.abortTransaction(con);
				return 0;
			}
			else
			{
				if (estado.equals(ConstantesBD.finTransaccion))
				{
					myFactory.endTransaction(con);
				}
				
				pst3= con.prepareStatement(buscarCodigoAdmisionRecienInsertada,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
				rs=pst3.executeQuery();
				if (rs.next())
				{
					resp0= rs.getInt("codigoAdmision");
				}
				else
				{
					resp0=0;
				}
				
				return resp0;
			}
		}
		catch(Exception e){
			Log4JManager.error("ERROR insertarTransaccional", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst3 != null){
					pst3.close();
				}
				if(pst2 != null){
					pst2.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return 0;
	}
	
	/**
	 * Mètodo que obtiene la información de la cama actual del paciente, para ser 
	 * postulada en la información del paciente que se encuentra en el cabezote superior
	 * @param con
	 * @param codigoAdmision
	 * @param codigoPaciente
	 */
	public static String[] getCama(Connection con, int codigoAdmision, int codigoPaciente) throws SQLException
	{
		ResultSetDecorator rs=null;
		ResultSetDecorator rs2=null;
		PreparedStatementDecorator pst=null;
		PreparedStatementDecorator pst2=null;
		if (con == null || con.isClosed()) 
		{
			logger.warn("La conexión llegó cerrada. Se lanzó la siguiente excepción:\nError SQL: Conexión cerrada");
			throw new SQLException ("Error SQL: Conexión cerrada");
		}
		
		try
		{			
			//Se consulta el número de traslados que tiene el paciente, para saber si se consulta la cama
			//de la admisión o del traslado de camas
			pst= new PreparedStatementDecorator(con.prepareStatement(consultarNroTrasladosCamaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, codigoAdmision);
			rs=new ResultSetDecorator(pst.executeQuery());
			int tmp=0;
			while(rs.next())
			{
				tmp=rs.getInt("cantidad");
			}
			//Si hay mas de un traslado se consulta la cama en la tabla traslado_cama
			if(tmp>0)
			{
				String consulta = consultaDatosCamaActualStr + " ah.codigo=? and tc.fecha_finalizacion is null and tc.hora_finalizacion is null ";
				pst2= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst2.setInt(1, codigoAdmision);
				rs2 = new ResultSetDecorator(pst2.executeQuery());
				if(rs2.next())
				{
				    String resp[]=new String[3];
				    resp[0]=rs2.getString("numero_cama");
				    resp[1]=rs2.getString("descripcion_cama");
				    resp[2]=rs2.getString("codigo_cama");
					return resp;
				}
				else
				{
				    String resp[]=new String[3];
				    resp[0]="";
				    resp[1]="";
				    resp[2]="0";
					return resp;
				}
			}
			//Sino se consulta en la admisión
			else
			{
				pst2= new PreparedStatementDecorator(con.prepareStatement(consultarCamaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst2.setInt(1, codigoAdmision);				
				rs2 = new ResultSetDecorator(pst2.executeQuery());
				if(rs2.next())
				{
				    String resp[]=new String[3];
				    resp[0]=rs2.getString("cama");
				    resp[1]=rs2.getString("descripcion");
				    resp[2]=rs2.getString("codigo");
					return resp;
				}
				else
				{
				    String resp[]=new String[3];
				    resp[0]="";
				    resp[1]="";
				    resp[2]="0";
					return resp;
				}
			}//ELSE	
		} 
		catch(Exception e){
			Log4JManager.error("ERROR getCama", e);
		}
		finally{
			try{
				if(rs2 != null){
					rs2.close();
				}
				if(pst2 != null){
					pst2.close();
				}
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return null;
	}

	/**
	 * Implementación de la consulta de datos basicos de una admision hospitalaria
	 * @see com.princetonsa.dao.AdmisionHospitalariaDao#getBasicoAdmision (con, int ) throws SQLException
	 * @version Sept. 11 de 2003
	 */		
	public static ResultSetDecorator getBasicoAdmision(Connection con, int numeroCuenta, String consultarBasicoAdmisionStr) throws SQLException
	{		
		if (con == null || con.isClosed()) 
		{
			logger.warn("La conexión llegó cerrada. Se lanzó la siguiente excepción:\nError SQL: Conexión cerrada");
			throw new SQLException ("Error SQL: Conexión cerrada");
		}
		
		try
		{			
			PreparedStatementDecorator consultarBasicoAdmisionStatement= new PreparedStatementDecorator(con.prepareStatement(consultarBasicoAdmisionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consultarBasicoAdmisionStatement.setInt(1, numeroCuenta);				
			return new ResultSetDecorator(consultarBasicoAdmisionStatement.executeQuery());
		} 
		catch (SQLException sql) 
		{
			logger.warn("Error consultando admisión hospitalaria asociada al numero de cuenta "+numeroCuenta+" en la tabla 'admisiones_hospi'.\n Se lanzó la siguiente excepción:\n"+sql);
			throw sql;
		}
	} 
	
	/**
	 * Método para obtener los datos de la cama actual en la que está el paciente 
	 * @param con
	 * @param codigoPaciente
	 * @return Collection -> Con los datos de la cama actual del paciente
	 */
	@SuppressWarnings("rawtypes")
	public static Collection consultarDatosCamaActual(Connection con, int codigoPaciente)
	{
		PreparedStatementDecorator pst=null;
		ResultSetDecorator rs=null;
		Collection resultado=null;
		try
		{
			String consulta = consultaDatosCamaActualStr + " tc.codigo_paciente=? and tc.fecha_finalizacion is null and tc.hora_finalizacion is null ";
			pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, codigoPaciente);			
			rs=new ResultSetDecorator(pst.executeQuery());
	 	   resultado=UtilidadBD.resultSet2Collection(rs);				
		} 
		catch(Exception e){
			Log4JManager.error("ERROR consultarDatosCamaActual", e);
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
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return resultado;
	}

}
