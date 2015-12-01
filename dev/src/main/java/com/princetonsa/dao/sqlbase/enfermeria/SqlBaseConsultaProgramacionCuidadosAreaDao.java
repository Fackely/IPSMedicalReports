package com.princetonsa.dao.sqlbase.enfermeria;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dto.enfermeria.DtoCuidadosEnfermeria;
import com.princetonsa.dto.enfermeria.DtoDetalleCuidadosEnfermeria;
import com.princetonsa.dto.enfermeria.DtoFrecuenciaCuidadoEnferia;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.UtilidadFecha;

public class SqlBaseConsultaProgramacionCuidadosAreaDao {

	 
	/**
	 * 
	 */
	private static Logger logger = Logger.getLogger(SqlBaseConsultaProgramacionCuidadosAreaDao.class);     
	
	
	/**
	 * Consulta de Pacientes que se encuetre en un Area y en un rango de fecha y hora especifica
	 */
	
	private static final String cadenaConsultaPacienteAreaProgramadosStr="	SELECT distinct getnombrepersona(c.codigo_paciente) as paciente, " +
	"getidpaciente(c.codigo_paciente) as tipoid, " +
	"c.codigo_paciente as codigopaciente, " +
	"g.id as codigoingreso,  " +
	"g.fecha_ingreso as fechaingreso, "+
	"g.consecutivo as ingreso, " +
	"c.id as cuenta, " +
	"p.historia_clinica as historiaclinica, " +
	"getnombreviaingreso(c.via_ingreso) as viaingreso, " +
	"getnombreconvenioxingreso(g.id) as convenio, " +
	"getdiagnosticopaciente(c.id, c.via_ingreso) as diagnostico, " +
	"getcamacuenta(c.id, c.via_ingreso) as cama " +
	"FROM cuentas c " +
	"inner join pacientes p on(p.codigo_paciente=c.codigo_paciente) " +
	"inner join ingresos g on(g.id=c.id_ingreso) " +
	"inner join frec_cuidados_enfer fc on(fc.ingreso=g.id) " +
	"inner join prog_cuidados_enfer ce on(ce.frec_cuidado_enfer=fc.codigo) " +
	"WHERE c.area=? and (? BETWEEN to_char(ce.fecha_inicio,'"+ConstantesBD.formatoFechaBD+"') || ' ' || ce.hora_inicio and getUltFechadetprogcui(ce.codigo)) and g.estado='"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"' and c.estado_cuenta in ("+ConstantesBD.codigoEstadoCuentaActiva+","+ConstantesBD.codigoEstadoCuentaAsociada+","+ConstantesBD.codigoEstadoCuentaFacturadaParcial+") ";

	
 /**
  * Consulta Pacientes que se encuentren en un Piso en un rango de fecha y hora especifica
  */
	
	private static final String cadenaConsultaPacientePisoStr=" SELECT distinct getnombrepersona(c.codigo_paciente) as paciente, " +
	   "getidpaciente(c.codigo_paciente) as tipoid, " +
	   "c.codigo_paciente as codigopaciente, " +
	   "g.id as codigoingreso, " +
	   "g.consecutivo as ingreso, " +
	   "g.fecha_ingreso as fechaingreso, "+
	   "c.id as cuenta, " +
	   "p.historia_clinica as historiaclinica, " +
	   "getnombreviaingreso(c.via_ingreso) as viaingreso, " +
	   "getnombreconvenioxingreso(g.id) as convenio, " +
	   "getdiagnosticopaciente(c.id, c.via_ingreso) as diagnostico, " +
	   "getcamacuenta(c.id, c.via_ingreso) as cama " +
        "FROM cuentas c " +
		"inner join pacientes p on(p.codigo_paciente=c.codigo_paciente) " +
		" "+ConstantesBD.separadorSplitComplejo+"  "+
		"inner join ingresos g on(g.id=c.id_ingreso) " +
		"inner join frec_cuidados_enfer fc on(fc.ingreso=g.id ) " +
		"inner join prog_cuidados_enfer ce on(ce.frec_cuidado_enfer=fc.codigo) " +
		"WHERE (? BETWEEN ce.fecha_inicio || ' ' || ce.hora_inicio and getUltFechadetprogcui(ce.codigo)) and g.estado='"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"' and c.estado_cuenta in ("+ConstantesBD.codigoEstadoCuentaActiva+","+ConstantesBD.codigoEstadoCuentaAsociada+","+ConstantesBD.codigoEstadoCuentaFacturadaParcial+") ";

	/**
	 * Consulta la programación de cuidados de enfermeria
	 * */
	private static final String strConsultaProgCuidadosEnfer = "SELECT " +
															   "p.codigo," +
															   "p.frec_cuidado_enfer," +
															   "to_char(p.fecha_inicio,'dd/mm/yyyy') as fecha_inicio," +
															   "p.hora_inicio," +
															   "coalesce(p.observaciones,'') as observaciones," +
															   "getnombreusuario(p.usuario_programacion) as usuario_programacion," +
															   "frec.activo " +
															   "FROM " +
															   "prog_cuidados_enfer p " +
															   "INNER JOIN frec_cuidados_enfer frec ON (frec.codigo = p.frec_cuidado_enfer) ";		
	
	
	/**
	 * Consulta del detalle de programación de cuidados de enfermeria
	 * */
	private static final String strConsultaDetalleProgCuidadosEnfer = "SELECT " +
																	  "codigo," +
																	  "prog_cuidado_enfer," +
																	  "to_char(fecha,'dd/mm/yyyy') as fecha," +
																	  "hora," +
																	  "activo " +
																	  "FROM " +
																	  "det_prog_cuidados_enfer " +
																	  "WHERE prog_cuidado_enfer = ? " +
																	  "ORDER BY fecha,hora ASC ";
	
	
	/**
	 * Consulta las frecuencias de los cuidados de enfermeria 
	 * */
	private static final String strConsultaFrecuenciaCuidadoEnfer	=  "SELECT " +
																	   "f.codigo," +
																	   "f.ingreso," +
																	   "f.otro_cuidado," +
																	   "coalesce(f.frecuencia||'','') as frecuencia,"  +
																	   "f.tipo_frecuencia," +
																	   "f.activo," +
																	   "coalesce (f.periodo||'','') as periodo,"+
																	   "f.tipo_frec_periodo, " +
																	   "f.cuidado_enfer_cc_inst," +
																	   "tce.descripcion AS nombre_cuidado_enfer, " +
																	   "coalesce(getnombretipofrecuencia(f.tipo_frecuencia),'') AS nombre_tipo_frec," +
																	   "coalesce(getnombretipofrecuencia(f.tipo_frec_periodo),'') AS nombre_tipo_frec_per," +
																	   "cecc.cuidado_enfermeria," +
																	   "'false' AS esotro," +
																	   "(SELECT COUNT(enfer.frec_cuidado_enfer) " +
																	   " FROM prog_cuidados_enfer enfer " +
																	   " INNER JOIN det_prog_cuidados_enfer det ON (det.prog_cuidado_enfer = enfer.codigo AND det.activo = '"+ConstantesBD.acronimoSi+"' AND det.fecha >= '"+UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())+"' AND det.hora >= '"+UtilidadFecha.getHoraActual()+"') " +
																	   " WHERE enfer.frec_cuidado_enfer = f.codigo) AS cuantodetalle "+
																	   "FROM frec_cuidados_enfer f " +
																	   "INNER JOIN cuidado_enfer_cc_inst cecc ON (cecc.codigo = f.cuidado_enfer_cc_inst) " +
																	   "INNER JOIN tipo_cuidado_enfermeria tce ON (tce.codigo = cecc.cuidado_enfermeria) " +
																	   "WHERE f.otro_cuidado IS NULL ?=? " +
																	   "" +
																	   "UNION " +
																	   "" +
																	   "SELECT " +
																	   "f.codigo," +
																	   "f.ingreso," +
																	   "f.otro_cuidado," +
																	   "coalesce(f.frecuencia||'','') as frecuencia," +
																	   "f.tipo_frecuencia," +
																	   "f.activo," +
																	   "coalesce (f.periodo||'','') as periodo," +
																	   "f.tipo_frec_periodo, " +
																	   "f.cuidado_enfer_cc_inst," +
																	   "toc.descripcion AS nombre_cuidado_enfer, " +
																	   "coalesce(getnombretipofrecuencia(f.tipo_frecuencia),'') AS nombre_tipo_frec," +
																	   "coalesce(getnombretipofrecuencia(f.tipo_frec_periodo),'') AS nombre_tipo_frec_per," +
																	   ConstantesBD.codigoNuncaValido+" AS cuidado_enfermeria, "+																	   
																	   "'true' AS esotro, "+
																	   "(SELECT COUNT(enfer.frec_cuidado_enfer) " +
																	   " FROM prog_cuidados_enfer enfer " +
																	   " INNER JOIN det_prog_cuidados_enfer det ON (det.prog_cuidado_enfer = enfer.codigo AND det.activo = '"+ConstantesBD.acronimoSi+"' AND det.fecha >= '"+UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())+"' AND det.hora >= '"+UtilidadFecha.getHoraActual()+"') " +
																	   " WHERE enfer.frec_cuidado_enfer = f.codigo) AS cuantodetalle "+																	   
																	   "FROM frec_cuidados_enfer f " +
																	   "INNER JOIN tipo_otro_cuidado_enf toc ON (toc.codigo = f.otro_cuidado) " +																	   
																	   "WHERE f.otro_cuidado IS NOT NULL ?=? ";
													
	
	
	
	
	/**
	 * Cadena que consulta los tipos de frecuencia por institución
	 * */	
	private static final String strConsultaTiposFrecInst = "SELECT " +
														   "tf.codigo," +														   
														   "tf.nombre " +
														   "FROM tipo_frec_institucion tfi " +
														   "INNER JOIN tipos_frecuencia tf ON (tf.codigo = tfi.tipo_frecuencia) " +
														   "WHERE tfi.institucion = ? " +
														   "ORDER BY tf.nombre ";
	
	
	/**
	 * Consulta las Areas por Via de Ingreso segun centro de Atencion
	 */
	private static String strListarAreas = "SELECT cc.codigo, cc.nombre "
			+ "FROM centros_costo cc "
			+ "INNER JOIN centro_costo_via_ingreso cv ON (cc.codigo=cv.centro_costo) "
			+ "WHERE cc.centro_atencion=? ORDER BY cc.nombre";

	private static String[] indicesAreas = { "codigo_", "nombre_" };

	/**
	 * Consulta los pisos
	 */
	private static String strListarPisos = "SELECT codigo, nombre FROM pisos WHERE centro_atencion=?";

	/**
	 * Consulta las Habitaciones segun Piso
	 */
	private static String strListarHabitaciones = "SELECT codigo, nombre, piso FROM habitaciones WHERE piso=?";

	private static String[] indicesHabitaciones = { "codigo_", "nombre_",
			"piso_" };

	
	/**
	 * Consulta las areas que exiten en un centro de atencion
	 * @param con
	 * @param centroAtencion
	 * @return
	 */
	public static HashMap<String, Object> listarAreas(Connection con,
			int centroAtencion) {
		
		HashMap<String, Object> resultados = new HashMap<String, Object>();

		PreparedStatementDecorator pst;

		try {
			pst =  new PreparedStatementDecorator(con.prepareStatement(strListarAreas,
					ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet ));
			pst.setInt(1, centroAtencion);

			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
			resultados.put("INDICES", indicesAreas);
		} catch (SQLException e) {
			 logger.error(e + " Error en consulta de Areas");
		}
		return resultados;
	}

	
	/**
	 * Consulta las habitaciones que hay en un determinado piso
	 * @param con
	 * @param piso
	 * @return
	 */
	public static HashMap<String, Object> listarHabitaciones(Connection con,
			int piso) {
		
HashMap<String, Object> resultados = new HashMap<String, Object>();
		
		PreparedStatementDecorator pst;
		 
		try{
			pst =  new PreparedStatementDecorator(con.prepareStatement(strListarHabitaciones, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1, piso);
			
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
			
			resultados.put("INDICES",indicesHabitaciones);
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en consulta de Habitaciones");
		}
		return resultados;
	}

	/**
	 * Consulta los pisos que existen en un centro de atencion
	 * @param con
	 * @param centroAtencion
	 * @return
	 */
	public static HashMap<String, Object> listarPisos(Connection con,
			int centroAtencion) {
		
		HashMap<String, Object> resultados = new HashMap<String, Object>();

		PreparedStatementDecorator pst;

		try {
			pst =  new PreparedStatementDecorator(con.prepareStatement(strListarPisos,
					ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet ));
			pst.setInt(1, centroAtencion);

			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true,true);

			resultados.put("INDICES", indicesAreas);
		} catch (SQLException e) {
			//logger.error(e + " Error en consulta de Pisos");
		}
		return resultados;
	}

	/**
	 * Consulta los pacientes segun los parametros de entrada ( area - piso - habitacion )
	 * @param con
	 * @param areaFiltro
	 * @param pisoFiltro
	 * @param habitacionFiltro
	 * @return
	 */
	public static HashMap listarPacientes(Connection con, String areaFiltro, String pisoFiltro, String habitacionFiltro,String fechaProg, String horaProg) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		String cadena="";
		
		if(!areaFiltro.equals(""))
		{
			cadena=cadenaConsultaPacienteAreaProgramadosStr;
			try
			{
				PreparedStatementDecorator busqueda= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				busqueda.setInt(1, Utilidades.convertirAEntero(areaFiltro));
				busqueda.setString(2,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaProg))+" "+UtilidadFecha.convertirHoraACincoCaracteres(horaProg));				
				
				cadena= cadena.replace("c.area=?","c.area="+areaFiltro);
				cadena= cadena.replace("? BETWEEN","'"+UtilidadFecha.conversionFormatoFechaABD(fechaProg)+" "+UtilidadFecha.convertirHoraACincoCaracteres(horaProg)+"' BETWEEN ");				
				logger.info("cadena entrando a AREA...>>>>>>> "+cadena);
				
				 
				mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(busqueda.executeQuery()));
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
			
		}
		else
		{
			
			cadena=cadenaConsultaPacientePisoStr;
			
			if(habitacionFiltro.equals("") && !pisoFiltro.equals("") )
			{
				cadena+=" and hc.codigopkpiso="+pisoFiltro;
			    cadena = cadena.replace(ConstantesBD.separadorSplitComplejo," inner join his_camas_cuentas hc on(hc.cuenta=c.id) ");
			}else
				if(!habitacionFiltro.equals("") && !pisoFiltro.equals("") )
				{
				  cadena = cadena.replace(ConstantesBD.separadorSplitComplejo," inner join his_camas_cuentas hc on(hc.cuenta=c.id) ");	
				  cadena+=" and hc.codigopkpiso="+pisoFiltro+" and hc.codigopkhabitacion="+habitacionFiltro;
				}else
				{
					cadena = cadena.replace(ConstantesBD.separadorSplitComplejo,"");
				}
			
			try
			{
				PreparedStatementDecorator busqueda= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				busqueda.setString(1,UtilidadFecha.conversionFormatoFechaABD(fechaProg)+" "+UtilidadFecha.convertirHoraACincoCaracteres(horaProg));
								
				cadena= cadena.replace("?","'"+UtilidadFecha.conversionFormatoFechaABD(fechaProg)+" "+UtilidadFecha.convertirHoraACincoCaracteres(horaProg)+"' ");				
				logger.info("cadena >>>>>>> "+cadena);
				mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(busqueda.executeQuery()));
			}
			catch(SQLException e)
			{
				
				e.printStackTrace();
				logger.info("Se presento un error >>>>>>> "+e);
			}
			
			
		}
		
		return mapa;
		
		
	}

	public static ArrayList<DtoFrecuenciaCuidadoEnferia> consultarFrecuenciaCuidado(Connection con, HashMap parametros) {
		
		ArrayList<DtoFrecuenciaCuidadoEnferia> array = new ArrayList<DtoFrecuenciaCuidadoEnferia>();
		DtoFrecuenciaCuidadoEnferia dto = new DtoFrecuenciaCuidadoEnferia();
		
		String cadena = strConsultaFrecuenciaCuidadoEnfer;
		String cadenaRelleno = " AND f.ingreso = "+parametros.get("ingreso").toString()+" AND f.activo = '"+parametros.get("activo").toString()+"' "; 
		
		if(parametros.containsKey("codigoPkFrecCuidadoEnfer") && 
			Utilidades.convertirAEntero(parametros.get("codigoPkFrecCuidadoEnfer").toString())>=0)
				cadenaRelleno += " AND f.codigo = "+parametros.get("codigoPkFrecCuidadoEnfer");			
		
		cadena = cadena.replace("?=?",cadenaRelleno);		
		logger.info("valor de la cadena >> "+cadena);
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			ResultSetDecorator rs =new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				dto = new DtoFrecuenciaCuidadoEnferia();
				dto.setCodigoPk(rs.getInt("codigo"));
				dto.setCodigoCuidadoEnferCcInst(rs.getInt("cuidado_enfer_cc_inst"));
				dto.setCodigoOtroCuidado(rs.getInt("otro_cuidado"));
				dto.setFrecuencia(Utilidades.convertirAEntero(rs.getString("frecuencia")));
				dto.setTipoFrecuencia(rs.getInt("tipo_frecuencia"));
				dto.setActivo(rs.getString("activo"));
				dto.setNombreCuidadoEnfer(rs.getString("nombre_cuidado_enfer"));
				dto.setNombreTipoFrecuencia(rs.getString("nombre_tipo_frec"));
				dto.setNombreTipoFrecuPeriodo(rs.getString("nombre_tipo_frec_per"));
				dto.setCodigoIngreso(rs.getInt("ingreso"));						
				dto.setCodigoCuidadoEnfermeria(rs.getInt("cuidado_enfermeria"));				
				dto.setEsOtroCuidado(UtilidadTexto.getBoolean(rs.getString("esotro")));
				dto.setTipoFrecuenciaPeriodo(rs.getInt("tipo_frec_periodo"));
				dto.setPeriodo(Utilidades.convertirAEntero(rs.getString("periodo")));
				dto.setTieneProgramacion(rs.getInt("cuantodetalle")>0?true:false);
				array.add(dto);
			}
			
		}catch (Exception e) {
			logger.info("valor de la cadena que causa el error >> "+cadena);
			e.printStackTrace();	
		}
		return array;
	}


	public static ArrayList<HashMap<String, Object>> consultarTipoFrecuenciaInst(Connection con, HashMap parametros) {
		
		ArrayList<HashMap<String,Object>> array = new ArrayList<HashMap<String,Object>>();
		HashMap datos = new HashMap();
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strConsultaTiposFrecInst,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("institucion").toString()));
			ResultSetDecorator rs =new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				datos = new HashMap();
				datos.put("codigo",rs.getString(1));
				datos.put("descripcion",rs.getString(2));
				array.add(datos);				
			}
		}
		catch (Exception e) {
			e.printStackTrace();			
		}
		
		return array;
	}

	public static String cadenaConsultaCuidados(Connection con, HashMap parametros){
	    String cadena=new String("");
	    cadena=strConsultaFrecuenciaCuidadoEnfer;
		String cadenaRelleno = " AND f.ingreso = "+parametros.get("ingreso").toString()+" AND f.activo = '"+parametros.get("activo").toString()+"' "; 
		
		if(parametros.containsKey("codigoPkFrecCuidadoEnfer") && 
			Utilidades.convertirAEntero(parametros.get("codigoPkFrecCuidadoEnfer").toString())>=0)
				cadenaRelleno += " AND f.codigo = "+parametros.get("codigoPkFrecCuidadoEnfer");			
		
		cadena = cadena.replace("?=?",cadenaRelleno);		
		logger.info("valor de la cadena >> "+cadena);
		return cadena;
	}
	
	
}
