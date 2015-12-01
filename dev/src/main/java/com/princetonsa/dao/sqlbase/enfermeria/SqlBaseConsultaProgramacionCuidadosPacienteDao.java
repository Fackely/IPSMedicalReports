package com.princetonsa.dao.sqlbase.enfermeria;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dto.enfermeria.DtoFrecuenciaCuidadoEnferia;

public class SqlBaseConsultaProgramacionCuidadosPacienteDao {

	private static Logger logger = Logger.getLogger(SqlBaseConsultaProgramacionCuidadosPacienteDao.class);
	
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
																	   "cuidenf.fecha_inicio as fechaProgCuidado,"+
																	   "COALESCE(cuidenf.hora_inicio,'') as horaProgCuidado,"+
																	   "coalesce(cuidenf.observaciones,'') as observaciones"+
																	   "coalesce(cuidenf.usuario_programacion,'') as usuario"+
																	   "(SELECT COUNT(enfer.frec_cuidado_enfer) " +
																	   " FROM prog_cuidados_enfer enfer " +
																	   " INNER JOIN det_prog_cuidados_enfer det ON (det.prog_cuidado_enfer = enfer.codigo AND det.activo = '"+ConstantesBD.acronimoSi+"' AND det.fecha >= '"+UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())+"' AND det.hora >= '"+UtilidadFecha.getHoraActual()+"') " +
																	   " WHERE enfer.frec_cuidado_enfer = f.codigo) AS cuantodetalle "+
																	   "FROM frec_cuidados_enfer f " +
																	   "LEFT OUTER JOIN prog_cuidados_enfer cuidenf ON (cuidenf.frec_cuidado_enfer= f.codigo)"+
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
																	   "cuidenf.fecha_inicio as fechaProgCuidado,"+
																	   "COALESCE(cuidenf.hora_inicio,'') as horaProgCuidado,"+
																	   "coalesce(cuidenf.observaciones,'') as observaciones"+
																	   "coalesce(cuidenf.usuario_programacion,'') as usuario"+
																	   "(SELECT COUNT(enfer.frec_cuidado_enfer) " +
																	   " FROM prog_cuidados_enfer enfer " +
																	   " INNER JOIN det_prog_cuidados_enfer det ON (det.prog_cuidado_enfer = enfer.codigo AND det.activo = '"+ConstantesBD.acronimoSi+"' AND det.fecha >= '"+UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())+"' AND det.hora >= '"+UtilidadFecha.getHoraActual()+"') " +
																	   " WHERE enfer.frec_cuidado_enfer = f.codigo) AS cuantodetalle "+																	   
																	   "FROM frec_cuidados_enfer f " +
																	   "LEFT OUTER JOIN prog_cuidados_enfer cuidenf ON (cuidenf.frec_cuidado_enfer = f.codigo)"+
																	   "INNER JOIN tipo_otro_cuidado_enf toc ON (toc.codigo = f.otro_cuidado) " +																	   
																	   "WHERE f.otro_cuidado IS NOT NULL ?=? ";
	
	/**
	 * 
	 */
	private static final String strConsultaTiposFrecInst = "SELECT " +
	   "tf.codigo," +														   
	   "tf.nombre " +
	   "FROM tipo_frec_institucion tfi " +
	   "INNER JOIN tipos_frecuencia tf ON (tf.codigo = tfi.tipo_frecuencia) " +
	   "WHERE tfi.institucion = ? " +
	   "ORDER BY tf.nombre ";

	
	
	
	/**
	 * 
	 * @param con
	 * @param parametros
	 * @return
	 */
	
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
					dto.setFechaInicioProgramacion(UtilidadFecha.conversionFormatoFechaAAp(rs.getDate("fechaProgCuidado")));
					dto.setHoraInicioProgramacion(rs.getString("horaProgCuidado"));
					dto.setObservaciones(rs.getString("observaciones"));
					dto.setTieneProgramacion(rs.getInt("cuantodetalle")>0?true:false);
					array.add(dto);
				}
				
			}catch (Exception e) {
				logger.info("valor de la cadena que causa el error >> "+cadena);
				e.printStackTrace();	
			}
			return array;
		}
	

	public static ArrayList<HashMap<String, Object>> consultarTipoFrecuencias(Connection con, HashMap parametros) {
		
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

}
