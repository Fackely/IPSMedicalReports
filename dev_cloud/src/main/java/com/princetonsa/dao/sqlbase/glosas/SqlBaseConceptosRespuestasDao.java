package com.princetonsa.dao.sqlbase.glosas;

/*
 * @author: Juan Alejandro Cardona
 * date: Octubre 2008
 */


import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.HashMap;
import org.apache.log4j.Logger;
import com.princetonsa.dao.sqlbase.glosas.SqlBaseConceptosRespuestasDao;
import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
//import util.UtilidadBD;


public class SqlBaseConceptosRespuestasDao {

	
	// Objeto para manejar los logs de esta clase
	private static Logger logger = Logger.getLogger(SqlBaseConceptosRespuestasDao.class);
	
	/**
	 * Cadena que consulta si un concepto tipo glosa ha sido utilizado en la tabla det_fact_ext_resp_glosa o no para validar si se puede o no eliminar 
	 */
	private static String consultaDetFactExtRespGlosa="SELECT count(codigo) AS codigo FROM det_fact_ext_resp_glosa where concepto_respuesta = ?";
	
	/**
	 * Cadena que consulta si un concepto tipo glosa ha sido utilizado en la tabla det_fact_resp_glosa o no para validar si se puede o no eliminar 
	 */
	private static String consultaDetFactRespGlosa="SELECT count(codigo) AS codigo FROM det_fact_resp_glosa where concepto_respuesta = ?";
	
	/**
	 * Cadena que consulta si un concepto tipo glosa ha sido utilizado en la tabla asocios_resp_glosa o no para validar si se puede o no eliminar 
	 */
	private static String consultaAsociosRespGlosa="SELECT count(codigo) AS codigo FROM asocios_resp_glosa where concepto_respuesta = ?";
	
	
	/**
	 * Metodo que consulta si un concepto tipo glosa ha sido utilizado o no para validar si se puede o no eliminar
	 * @param connection
	 * @param criterios
	 * @return
	 */
	public static boolean consultarConceptosRespuesta (Connection con, String codConcepto)
	{
		PreparedStatementDecorator ps;
		int resultado= 0;
		ResultSetDecorator rs;
		
		try {	
			ps =  new PreparedStatementDecorator(con.prepareStatement(consultaDetFactExtRespGlosa,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setString(1, codConcepto);
			
			rs=new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next()){
				resultado = rs.getInt("codigo");
			}
		}
		catch (SQLException e){
			logger.info("\n\nERROR. CONCEPTOS DETALLE FACTURA EXTERNA RESPUESTA GLOSA------>>>>>>"+e);
			e.printStackTrace();
		}
		if(resultado == 0){
			try {	
				ps =  new PreparedStatementDecorator(con.prepareStatement(consultaDetFactRespGlosa,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				ps.setString(1, codConcepto);
				
				rs=new ResultSetDecorator(ps.executeQuery());
				
				if(rs.next()){
					resultado = rs.getInt("codigo");
				}
			}
			catch (SQLException e){
				logger.info("\n\nERROR. CONCEPTOS DETALLE FACTURA RESPUESTA GLOSA------>>>>>>"+e);
				e.printStackTrace();
			}
			if(resultado == 0){
				try {	
					ps =  new PreparedStatementDecorator(con.prepareStatement(consultaAsociosRespGlosa,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					
					ps.setString(1, codConcepto);
					
					rs=new ResultSetDecorator(ps.executeQuery());
					
					if(rs.next()){
						resultado = rs.getInt("codigo");
					}
				}
				catch (SQLException e){
					logger.info("\n\nERROR. CONCEPTOS ASOCIOS RESPUESTA GLOSA------>>>>>>"+e);
					e.printStackTrace();
				}
			}			
		}	
		
		if(resultado > 0)
			return false;
			
		return true;
	}
	
	
	 //  para consultar conceptos de pago cartera.
	private static final String consultaConceptosRespuestaGlosasStr = "SELECT " +
																			"crg.codigo AS codigoRespuestaConcepto, " +
																			"crg.descripcion AS descripcion, " +
																			"crg.tipo_concepto AS tipoConcepto, " +
																			"coalesce(concepto_glosa, '') AS conceptoGlosa, " +
																			"coalesce(concepto_ajuste, '' ) AS conceptoAjuste, " +
																			"coalesce(getdescripcionconceptoajuste(concepto_ajuste,crg.institucion), '') AS descripAjuste, " +
																			"coalesce(getdescripcionconceptoglosa(concepto_glosa, crg.institucion), '') AS descripGlosa, " +
																			"coalesce(cac.tipo_cartera,"+ConstantesBD.codigoNuncaValido+" ) AS tipoCartera, " +
																			"coalesce(tc.nombre,'') as nombreTipoCartera " +
																		"FROM " +
																			"concepto_resp_glosas crg " +
																			"LEFT OUTER JOIN concepto_ajustes_cartera cac ON(crg.concepto_ajuste=cac.codigo AND crg.institucion=cac.institucion) " +
																			"LEFT OUTER JOIN tipo_cartera tc ON(cac.tipo_cartera=tc.codigo) " +
																		" WHERE crg.institucion=? ";
	

	// para consultar si un concepto de Glosa, tiene relacion con Glosas.
	private static final String existeRelacionRespuestaConGlosas = "SELECT COUNT(*) AS numFilas FROM glosas WHERE concep_resp_glosa = ? and institucion=?";
	
	
	private static final String eliminarConceptoRespuestaGlosa="DELETE FROM concepto_resp_glosas where codigo=? and  institucion=?";
	
	
	// Cadena para ingresar los registro a la tabla concepto_resp_glosas
	private static final String ingresarConceptoRespuestaGlosa = "INSERT INTO concepto_resp_glosas (" +
																	"codigo, institucion, descripcion, tipo_concepto, " +
																	"usuario_modifica, fecha_modifica, hora_modifica "; 

	
	
	private static final String modificarConceptoRespuestaGlosa="UPDATE concepto_resp_glosas SET " +
																	"codigo= ?, " +
																	"descripcion=?, " +
																	"tipo_concepto=? ";


	
	
	
	/**
	 * @param con, codigoInstitucionInt
	 * @return */
	public static ResultSetDecorator consultaConceptosRespuestasGlosas(Connection con, int codigoInstitucionInt) {
		logger.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		logger.info("Consulta Respuesta: " + consultaConceptosRespuestaGlosasStr);

		try {
			PreparedStatementDecorator ps = null;
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaConceptosRespuestaGlosasStr+" ORDER BY crg.codigo asc",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,codigoInstitucionInt);
			return new ResultSetDecorator(ps.executeQuery());
		}
		catch(SQLException e) {
			logger.warn(e+"Error en la consultaRespuestaConceptos: SqlBaseConceptosRespuestasDao "+e.toString() );
		   return null;
		}
	}
	


	/**
	 * @param con, codigo, codigoInstitucionInt, descripcion
	 * @return	 */
	public static ResultSetDecorator busquedaAvanzadaRespuestasGlosas(Connection con, String codigo, int codigoInstitucionInt, String descripcion) {

		try	{
			PreparedStatementDecorator ps = null;
			
			String avanzadaStr = "";
			if(!codigo.equals("")) {
				avanzadaStr+=" AND crg.codigo = '"+codigo+"'";				
			}
			if(!descripcion.equals("")) 	{	
			   avanzadaStr+=" AND upper(crg.descripcion) like upper('%"+descripcion+"%')";					
			}

			String consulta = consultaConceptosRespuestaGlosasStr + avanzadaStr + " ORDER BY crg.codigo asc";
			logger.info("consulta---->"+consulta);
			ps=  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,codigoInstitucionInt);
			return new ResultSetDecorator(ps.executeQuery());
		}

		catch(SQLException e) {
			logger.warn(e+"Error en la Busqueda Avanzada consultaConceptos: SqlBaseConceptosRespuestasDao "+e.toString() );
		   return null;
		}
	}	
	

	/**
	 * @param con, codigo, codigoInstitucionInt
	 * @return */
	public static ResultSetDecorator consultaRelacionConceptosRespuestasGlosas(Connection con, String codigo, int codigoInstitucionInt) {

		try	{
			PreparedStatementDecorator ps = null;
			ps=  new PreparedStatementDecorator(con.prepareStatement(existeRelacionRespuestaConGlosas,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,codigo);
			ps.setInt(2,codigoInstitucionInt);
			return new ResultSetDecorator(ps.executeQuery());
		}
		catch(SQLException e) {
			logger.warn(e+"Error en la consultaRelacionConceptos: SqlBaseConceptosRespuestasDao "+e.toString() );
		   return null;
		}
	}
	

	/**
	 * @param con, codigoConceptoAntiguo, codigoInstitucionInt
	 * @return	 */
	public static int eliminarConceptoRespuestaGlosas(Connection con, String codigoConceptoAntiguo, int codigoInstitucionInt) {

		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(eliminarConceptoRespuestaGlosa,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,codigoConceptoAntiguo);
			ps.setInt(2,codigoInstitucionInt);
			int temp =ps.executeUpdate();
			return temp;
		  }

		catch (SQLException e) {
			//logger.warn(e+" Error en la inserción de datos: SqlBaseConceptosRespuestasDao "+e.toString() );
			logger.warn(e+" Error al eliminar datos: SqlBaseConceptosRespuestasDao "+e.toString() );
			return -1;
		}			
	}
	


	/**
	 * @param con, codigoConcepto, codigoInstitucionInt, descripcion, criterios
	 * @return	 */


	public static int insertarConceptoRespuestaGlosas(Connection con, String codigoConcepto, int codigoInstitucionInt, String descripcion, HashMap criterios) {
		logger.info("==================");
		logger.info("==> se van a insertar datos");
		Utilidades.imprimirMapa(criterios);

		String tempo = ingresarConceptoRespuestaGlosa;
		String cuantos = " VALUES (?,?,?,?,?,?,?";
		
		if(!criterios.get("conceptoAjuste").toString().equals("")) {
			tempo += ",concepto_ajuste ";
			cuantos += ",'" + criterios.get("conceptoAjuste")+"' ";
		}
		if(!criterios.get("conceptoGlosa").toString().equals("")) {
			tempo += ",concepto_glosa ";
			cuantos += "," + criterios.get("conceptoGlosa");
		}
		
		tempo += " )  " + cuantos + " )";

		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(tempo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,codigoConcepto);
			ps.setInt(2,codigoInstitucionInt);
			ps.setString(3,descripcion);
			ps.setString(4, criterios.get("tipoConcepto")+"");
			ps.setString(5, criterios.get("Usuario")+"");
			ps.setDate(6, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			ps.setString(7, UtilidadFecha.getHoraActual(con));

			return ps.executeUpdate();
		}

		catch (SQLException e) {
			logger.warn(e+" Error en la inserción de datos: SqlBaseConceptosRespuestasDao "+e.toString() );
			return -1;
		}
	}
	
	

	/**	 * @param con, codigoConceptoAntiguo, codigoInstitucionInt, codigoConcepto, descripcion, criterios
	 * @return	 */
	public static int modificarConceptoRespuestaGlosas(Connection con, String codigoConceptoAntiguo, int codigoInstitucionInt, String codigoConcepto, String descripcion, HashMap criterios) {
		logger.info("==================");
		logger.info("==> se van a modificar datos");
		Utilidades.imprimirMapa(criterios);
		
		String tempo = modificarConceptoRespuestaGlosa; 
		
		if(!criterios.get("conceptoAjuste").toString().equals("")) {
			tempo += ", concepto_ajuste='" + criterios.get("conceptoAjuste") + "' ";
		}
		if(!criterios.get("conceptoGlosa").toString().equals("")) {
			tempo += ", concepto_glosa='"+ criterios.get("conceptoGlosa") + "' ";
		}
		
		tempo += " WHERE codigo=? AND institucion=?";
		
		logger.info("ACTUALIZACION--->"+tempo+"   codigoConcepto->"+codigoConcepto+" descripcion->"+descripcion+" "+criterios.get("tipoConcepto")+" codigoConceptoAntiguo->"+codigoConceptoAntiguo+" codigoInstitucionInt->"+codigoInstitucionInt);
		
		try  {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(tempo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,codigoConcepto);
			ps.setString(2,descripcion);
			ps.setString(3, criterios.get("tipoConcepto")+"");
			ps.setString(4,codigoConceptoAntiguo);
			ps.setInt(5,codigoInstitucionInt);
			
			
			/*ps.setString(7, criterios.get("Usuario")+"");
	  		ps.setDate(8, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
	  		ps.setString(9, UtilidadFecha.getHoraActual(con)); */  		
	  		
			return ps.executeUpdate();
			
		}
		catch (SQLException e) {
			//logger.warn(e+" Error en la inserción de datos: SqlBaseConceptosRespuestasDao "+e.toString() );
			logger.warn(e+" Error en la modificación de datos: SqlBaseConceptosRespuestasDao "+e.toString() );
			return -1;
		}				
	}
	
	/**
	 * 
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public static boolean puedoEliminar(String codigo, int institucion) 
	{
		boolean resultado=false;
		Connection con= UtilidadBD.abrirConexion();
		UtilidadBD.iniciarTransaccion(con);
		String cadena="DELETE FROM glosas.concepto_resp_glosas WHERE codigo='"+codigo+"' AND institucion="+institucion;
		logger.info("consulta-->"+cadena);
		try 
		{
			//se hace la prueba borrando el registro por que así queda contemplado que el registro no se use en ninguna otra tabla 
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			resultado=ps.executeUpdate()>0;
			ps.close();
		} 
		catch (SQLException e) 
		{
			UtilidadBD.abortarTransaccion(con);
			resultado=false;
		}
		UtilidadBD.abortarTransaccion(con);
		UtilidadBD.closeConnection(con);
		return resultado;
	}

}