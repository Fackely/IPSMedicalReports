/**
 * 
 */
package com.princetonsa.dao.sqlbase.historiaClinica;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;

/**
 * @author axioma
 *
 */
public class SqlBaseConsultaIngresosHospitalDiaDao 
{
	/**
	 * 
	 */
	private static Logger logger=Logger.getLogger(SqlBaseConsultaIngresosHospitalDiaDao.class);
	
	/**
	 * 
	 */
	private static String consultaIngresosHospitalDia="SELECT distinct " +
																" p.primer_nombre||' '||p.segundo_nombre||' '||p.primer_apellido||' '||p.segundo_apellido as nombrepaciente," +
																" p.codigo as codigopaciente," +
																" p.tipo_identificacion as tipoidpaciente," +
																" p.numero_identificacion as numeroidentificacion," +
																" c.id as cuenta," +
																" c.id_ingreso as ingreso," +
																" getnombreusuario(c.usuario_modifica) as usuarioingreso, " +
																" i.centro_atencion as codigocentroatencion," +
																" getnomcentroatencion(i.centro_atencion) as nomcentroatencion," +
																" i.consecutivo as consecutivoingreso," +
																" i.preingreso as indpre," +
																" to_char(i.fecha_ingreso,'dd/mm/yyyy')||' '||i.hora_ingreso as fechahoraingreso," +
																" to_char(i.fecha_egreso,'dd/mm/yyyy')||' '||to_char(i.hora_egreso,'hh24:mi') as fechahoraegreso," +
																" i.estado as estadoingreso," +
																" c.estado_cuenta as codestadocuenta," +
																" getnombreestadocuenta(c.estado_cuenta) as descestadocuenta " +
														" from cuentas c " +
														" inner join ingresos i on(c.id_ingreso=i.id) " +
														" inner join personas p on(p.codigo=i.codigo_paciente) " +
														" inner join reingreso_salida_hospi_dia rshd on(rshd.cuenta=c.id)  " +
														" where c.hospital_dia='"+ConstantesBD.acronimoSi+"' ";


	/**
	 * 
	 */
	private static String consultaDetalleIngreso="SELECT * from ( "+
			"SELECT " +
			"codigo, "+
			"'"+ConstantesIntegridadDominio.acronimoReingreso+"' as tipo, "+ 
			"to_char(fecha_ingreso,'"+ConstantesBD.formatoFechaAp+"')||' '||hora_ingreso as fechahora, "+ 
			"fecha_ingreso as fecha, "+
			"hora_ingreso as hora, "+
			"getnombreusuario(usuario_ingreso) as usuario, "+
			"observaciones_ingreso as observaciones "+ 
			"from reingreso_salida_hospi_dia "+ 
			"where ingreso=? and cuenta= ? "+
			"UNION "+
			"SELECT " +
			"codigo, "+
			"'"+ConstantesIntegridadDominio.acronimoConductaSeguirSalida+"' as tipo, "+
			"to_char(fecha_salida,'"+ConstantesBD.formatoFechaAp+"')||' '||hora_salida as fechahora, "+ 
			"fecha_salida as fecha, "+
			"hora_salida as hora, "+
			"getnombreusuario(usuario_salida) as usuario, "+
			"observaciones_salida as observaciones "+ 
			"from reingreso_salida_hospi_dia "+ 
			"where ingreso=? and cuenta= ? and fecha_salida is not null and hora_salida is not null and usuario_salida is not null "+
		") t " + 
		"ORDER BY t.codigo,t.fecha,t.hora";
	/**
	 * 
	 * @param con
	 * @param codigoPersona
	 */
	public static HashMap<String, Object> consultarIngresosHospitalDia(Connection con,Object atributos,boolean porPaciente) 
	{
		String cadena=consultaIngresosHospitalDia;
		HashMap<String, Object> mapa=new HashMap<String, Object>();
		mapa.put("numRegistros", "0");
		if(porPaciente)
		{
			int codigoPersona=Utilidades.convertirAEntero(atributos+"");
			if(codigoPersona>0)
			{
				 cadena=cadena+" and p.codigo="+codigoPersona;
			}
		}
		else
		{
			HashMap<String, Object> vo=(HashMap<String, Object>)atributos;
			if(!UtilidadTexto.isEmpty(Utilidades.obtenerValor(vo.get("fechaInicial")))&&!UtilidadTexto.isEmpty(Utilidades.obtenerValor(vo.get("fechaFinal"))))
			{
				 cadena=cadena+" and rshd.fecha_ingreso between '"+vo.get("fechaInicial")+"' and '"+vo.get("fechaFinal")+"'";
			}
			if(!UtilidadTexto.isEmpty(Utilidades.obtenerValor(vo.get("centroAtencion")))&&Utilidades.convertirAEntero(Utilidades.obtenerValor(vo.get("centroAtencion")))>0)
			{
				 cadena=cadena+" and i.centro_atencion="+vo.get("centroAtencion");
			}
			if(!UtilidadTexto.isEmpty(Utilidades.obtenerValor(vo.get("usuario"))))
			{
				 cadena=cadena+" and c.usuario_modifica='"+vo.get("usuario")+"'";
			}
		}
		PreparedStatementDecorator ps= null;
		try
		{
			logger.info(cadena);
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			mapa=(HashMap<String, Object>)UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())).clone();
		}
		catch(SQLException e)
		{
			logger.error("error en consulta: "+e);
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseConsultaIngresosHospitalDiaDao "+sqlException.toString() );
				}
			}	
		}
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPersona
	 */
	public static HashMap<String, Object> consultaDetalleIngreso(Connection con,int ingreso,int cuenta) 
	{
		HashMap<String, Object> mapa=new HashMap<String, Object>();
		mapa.put("numRegistros", "0");
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaDetalleIngreso));
			ps.setInt(1, ingreso);
			ps.setInt(2, cuenta);
			ps.setInt(3, ingreso);
			ps.setInt(4, cuenta);
			mapa=(HashMap<String, Object>)UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())).clone();
		}
		catch(SQLException e)
		{
			logger.error("error en consulta: "+e);
		}finally { 			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseConsultaIngresosHospitalDiaDao "+sqlException.toString() );
				}
			}	
		}
		return mapa;
	}

}
