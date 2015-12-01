package com.princetonsa.dao.sqlbase.historiaClinica;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;

/**
 * 
 * @author lgchavez@princetonsa.com
 *
 */
public class SqlBaseRegistroResumenParcialHistoriaClinicaDao {
	
	// --------------- ATRIBUTOS
	
	/**
	 * Objeto para manejar log de la clase  
	 * */
	private static Logger logger = Logger.getLogger(SqlBaseRegistroResumenParcialHistoriaClinicaDao.class);
	
	
	
	
	public static HashMap consultarNotas(Connection con, HashMap mapa){
		
		HashMap rs=new HashMap();
		
		String consulta="SELECT "+
							 " rch.codigo,"+
							 " rch.fecha,"+
							 " rch.hora,"+
							 " rch.profesional_responsable as profe," +
							 " administracion.getnombremedico(m.codigo_medico) as profesional,"+
							 " rch.notas,"+
							 " rch.infomedico," +
							 " p.tipo_identificacion || ' ' || p.numero_identificacion as documentomedico,"+
							 " rch.ingreso," +
							 " m.numero_registro as registromedico" +
							 " FROM " +
							 " resumen_historia_clinica rch" +
							 " INNER JOIN " +
							 "  usuarios u ON (rch.profesional_responsable = u.login)	" +
							 " INNER JOIN " +
							 "  medicos m ON (u.codigo_persona = m.codigo_medico) " +
							 " INNER JOIN " +
							 "  personas p ON (m.codigo_medico = p.codigo) " +
							 " inner join cuentas cu " +
							 " on(cu.id_ingreso="+mapa.get("ingreso").toString()+"  ) " +
							 " " +		
							 " where 1=1 ";
		
		if (mapa.containsKey("ingreso") 
				&& !mapa.get("ingreso").toString().equals(""))
		{
			consulta+=" and rch.ingreso="+mapa.get("ingreso").toString();
		}
		else if(mapa.containsKey("codigo") 
					&& !mapa.get("codigo").toString().equals(""))
		{
			consulta+=" and rch.codigo="+mapa.get("codigo").toString();
		}
		
		consulta+=" AND cu.ESTADO_CUENTA = 0  AND (SELECT count(0) from cuentas cu1  where cu1.id_ingreso="+mapa.get("ingreso").toString()+" AND cu1.ESTADO_CUENTA = 3) = 0";
		
		
		consulta+=" ORDER BY rch.fecha,rch.hora ";		
		
		PreparedStatementDecorator ps=  null;
		try{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			rs=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			logger.info("consulta notas >>>"+consulta);
			
		}
		catch (SQLException e) {
			logger.info("Error consultando Notas >>>>"+e+" \n\n\n "+consulta+" \n\n\n ");
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseReferenciaDao "+sqlException.toString() );
			}
		}
		
		return rs;
	}
	
	
	
	
	/**
	 * @param con
	 * @param mapa
	 * @return 
	 */
	public static HashMap consultarNotasAsocio(Connection con, HashMap mapa){
		
		HashMap rs=new HashMap();
		
		String consulta="SELECT "+
							 " rch.codigo,"+
							 " rch.fecha,"+
							 " rch.hora,"+
							 " rch.profesional_responsable as profe," +
							 " administracion.getnombremedico(m.codigo_medico) as profesional,"+
							 " rch.notas,"+
							 " rch.infomedico," +
							 " p.tipo_identificacion || ' ' || p.numero_identificacion as documentomedico,"+
							 " rch.ingreso," +
							 " m.numero_registro as registromedico" +
							 " FROM " +
							 " resumen_historia_clinica rch" +
							 " INNER JOIN " +
							 "  usuarios u ON (rch.profesional_responsable = u.login)	" +
							 " INNER JOIN " +
							 "  medicos m ON (u.codigo_persona = m.codigo_medico) " +
							 " INNER JOIN " +
							 "  personas p ON (m.codigo_medico = p.codigo) " +
							 " inner join cuentas cu " +
							 " on(cu.id_ingreso="+mapa.get("ingreso").toString()+"  ) " +
							 " " +		
							 " where 1=1 ";
		
		if (mapa.containsKey("ingreso") 
				&& !mapa.get("ingreso").toString().equals(""))
		{
			consulta+=" and rch.ingreso="+mapa.get("ingreso").toString();
		}
		else if(mapa.containsKey("codigo") 
					&& !mapa.get("codigo").toString().equals(""))
		{
			consulta+=" and rch.codigo="+mapa.get("codigo").toString();
		}
		
		consulta+=" AND cu.ESTADO_CUENTA = 0  AND (SELECT count(0) from cuentas cu1  where cu1.id_ingreso="+mapa.get("ingreso").toString()+" AND cu1.ESTADO_CUENTA = 3) > 0";
		
		consulta+=" ORDER BY rch.fecha,rch.hora ";		
		PreparedStatementDecorator ps=  null;
		try{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			rs=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			logger.info("consulta notas >>>"+consulta);
			
		}
		catch (SQLException e) {
			logger.info("Error consultando Notas >>>>"+e+" \n\n\n "+consulta+" \n\n\n ");
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseReferenciaDao "+sqlException.toString() );
			}
		}
		
		return rs;
	}
	
	public static int insertarNotas(Connection con, HashMap mapa){
		
		int y=0;
		
		String consulta="INSERT INTO " +
							"	resumen_historia_clinica (" +
								 "codigo,"+
								 "fecha,"+
								 "hora,"+
								 "profesional_responsable,"+
								 "notas,"+
								 "infomedico,"+
								 "ingreso )" +
							  " VALUES ( " +
							  ""+UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_resumen_historia_clinica")+","+
							  "'"+UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())+"', " +
							  "'"+UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getHoraActual())+"', " +
							  "'"+mapa.get("usuario").toString()+"', " +
							  "'"+mapa.get("nota").toString()+"', " +
							  "'"+mapa.get("infomedico").toString()+"', " +
							  ""+mapa.get("ingreso").toString()+" " +
							  " ) ";
		
		PreparedStatementDecorator ps=  null;
		try{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			y=ps.executeUpdate();
			logger.info("sentencia insert notas >>>"+consulta);
			
		}
		catch (SQLException e) {
			logger.info("Error insertando Notas >>>>"+e+" \n\n\n "+consulta+" \n\n\n ");
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseReferenciaDao "+sqlException.toString() );
			}
		}
		
		return y;
	}
	
	
}