package com.princetonsa.dao.sqlbase.manejoPaciente;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dto.manejoPaciente.DtoReingresoSalidaHospiDia;

/**
 * 
 * @author wilson
 *
 */
public class SqlBaseReingresoSalidaHospiDiaDao 
{
	/**
	 * Objeto para manejar los logs de esta clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseReingresoSalidaHospiDiaDao.class);
	
	/**
	 * 
	 */
	private static final String cadenaInsertarStr="INSERT INTO reingreso_salida_hospi_dia (" +
																								"codigo," +//1
																								"tipo," +//2
																								"codigo_paciente," +//3
																								"cuenta," +//4
																								"ingreso," +//5
																								"usuario_ingreso," +//6
																								"fecha_ingreso," +//7
																								"hora_ingreso," +//8
																								"observaciones_ingreso," +//9
																								"usuario_salida," +//10
																								"fecha_salida," +//11
																								"observaciones_salida," +//12
																								"numero_solicitud_salida," +//13
																								"servicio_salida," +//14
																								"institucion" +//15
																							")  VALUES (	?,?,?,?,?," +
																											"?,?,?,?,?," +
																											"?,?,?,?,?) ";
	
	/**
	 * Cadena que consulta el ultimo codigo del reingreso/salida de hospital día
	 */
	private static final String obtenerUltimoCodigoReingresoSalidaHospitalDiaStr = "SELECT max(codigo) AS codigo FROM reingreso_salida_hospi_dia WHERE cuenta = ? AND tipo = ?";
	
	/**
	 * Cadena que elimina un reingreso de hospital día por cuenta
	 */
	private static final String eliminarReingresoSalidaHospitalDiaXCuentaStr = "DELETE FROM reingreso_salida_hospi_dia WHERE cuenta =?";
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 */
	public static boolean insertar(Connection con, DtoReingresoSalidaHospiDia dto)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			int codigo=UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_reingreso_salida_hd");
			
			ps.setDouble(1, Utilidades.convertirADouble(codigo+""));
			ps.setString(2, dto.getTipo());
			ps.setInt(3, dto.getPaciente().getCodigo());
			ps.setInt(4, Utilidades.convertirAEntero(dto.getCuenta()));
			ps.setInt(5, Utilidades.convertirAEntero(dto.getIngreso()));
			ps.setString(6, dto.getLoginUsuarioIngreso());
			ps.setDate(7, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dto.getFechaIngreso())));
			ps.setString(8, dto.getHoraIngreso());
			logger.info("observaciones ingreso Sql->"+dto.getObservacionesIngreso());
			
			if(!UtilidadTexto.isEmpty(dto.getObservacionesIngreso()))
				ps.setString(9, dto.getObservacionesIngreso());
			else
				ps.setNull(9, Types.VARCHAR);
			
			if(!UtilidadTexto.isEmpty(dto.getLoginUsuarioSalida()))
				ps.setString(10, dto.getLoginUsuarioSalida());
			else
				ps.setNull(10, Types.VARCHAR);
			
			if(!UtilidadTexto.isEmpty(dto.getFechaSalida()))
				ps.setDate(11, Date.valueOf(dto.getFechaSalida()));
			else
				ps.setNull(11, Types.DATE);
			
			if(!UtilidadTexto.isEmpty(dto.getObservacionesSalida()))
				ps.setString(12, dto.getObservacionesSalida());
			else
				ps.setNull(12, Types.VARCHAR);
			
			if(!UtilidadTexto.isEmpty(dto.getNumeroSolicitudSalida()))
				ps.setInt(13, Utilidades.convertirAEntero(dto.getNumeroSolicitudSalida()));
			else
				ps.setNull(13, Types.INTEGER);
			
			if(dto.getServicioSalida().getCodigo()>0)
				ps.setInt(14, dto.getServicioSalida().getCodigo());
			else
				ps.setNull(14, Types.INTEGER);
			
			ps.setInt(15, dto.getInstitucion());
			
			if(ps.executeUpdate()>0)
				return true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	public static boolean updateSalida(Connection con, DtoReingresoSalidaHospiDia dto)
	{
		String cadena= "UPDATE reingreso_salida_hospi_dia SET 	tipo='"+ConstantesIntegridadDominio.acronimoConductaSeguirSalida+"', " +
																"usuario_salida=?, " +//1
																"fecha_salida=?, " +//2
																"hora_salida=?, " +//3
																"observaciones_salida=?, " +//4
																"numero_solicitud_salida=?," +//5
																"servicio_salida=? " +//6
																"WHERE codigo=? ";//7
																						
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setString(1, dto.getLoginUsuarioSalida());
			ps.setDate(2, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dto.getFechaSalida())));
			ps.setString(3, dto.getHoraSalida());
			ps.setString(4, dto.getObservacionesSalida());
			
			if(UtilidadTexto.isEmpty(dto.getNumeroSolicitudSalida()))
				ps.setNull(5, Types.INTEGER);
			else
				ps.setInt(5, Utilidades.convertirAEntero(dto.getNumeroSolicitudSalida()));
			if(dto.getServicioSalida().getCodigo()>0)
				ps.setInt(6, dto.getServicioSalida().getCodigo());
			else
				ps.setNull(6, Types.INTEGER);
			ps.setDouble(7, Double.parseDouble(dto.getCodigo()));
			
			if(ps.executeUpdate()>0)
				return true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param criteriosBusqueda key{codigoCentroAtencion, esSalida, tipoIdentificacion, numeroIdentificacion, primerNombre, primerApellido}
	 * @return
	 */
	public static HashMap listadoPacientesReingresoOSalida(Connection con, HashMap criteriosBusqueda)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		
		String tipo="";
		String fecha="";
		String tipoFecha="";
		String hora="";
		
		if(!UtilidadTexto.getBoolean(criteriosBusqueda.get("esSalida")+""))
		{	
			tipo=ConstantesIntegridadDominio.acronimoConductaSeguirSalida;
			tipoFecha=ConstantesIntegridadDominio.acronimoConductaSeguirSalida;
			fecha="fecha_salida";
			hora="hora_salida";
		}	
		else
		{	
			tipo=ConstantesIntegridadDominio.acronimoReingreso;
			tipoFecha=ConstantesIntegridadDominio.acronimoReingreso;
			fecha="fecha_ingreso";
			hora="hora_ingreso";
		}	
		
		String consulta=	"SELECT "+
								"(select max(r.codigo) from reingreso_salida_hospi_dia r where r.cuenta=rshd.cuenta and r.tipo='"+tipo+"') as codigoreingresosalida, "+
								"i.id as numeroingreso, " +
								"i.consecutivo as consecutivoingreso, " +
								"to_char(i.fecha_ingreso,'DD/MM/YYYY')  ||' '|| substr(i.hora_ingreso,1,5) as fechahoraingreso, " +
								"i.fecha_ingreso ||' '|| substr(i.hora_ingreso,1,5) as fechahoraingresobd, " +
								"rshd.codigo_paciente as codigopaciente,  "+
								"getnombrepersona(i.codigo_paciente) as nombrepaciente, " +
								"getnombretipoidentificacion(p.tipo_identificacion) as tipoid,  " +
								"p.numero_identificacion as numeroid, " +
								"rshd.cuenta as cuenta, "+
								"(select r."+fecha+" from reingreso_salida_hospi_dia r where r.codigo= (select max(r.codigo) from reingreso_salida_hospi_dia r where r.cuenta=rshd.cuenta and r.tipo='"+tipoFecha+"')) as fechaultimabd, " +
								"(select to_char(r."+fecha+", 'DD/MM/YYYY') ||' '|| substr(r."+hora+",1,5) from reingreso_salida_hospi_dia r where r.codigo= (select max(r.codigo) from reingreso_salida_hospi_dia r where r.cuenta=rshd.cuenta and r.tipo='"+tipoFecha+"')) as fechaultima "+
							"FROM " +
								"ingresos i " +
								"INNER JOIN personas p ON(i.codigo_paciente=p.codigo) " +
								"INNER JOIN reingreso_salida_hospi_dia rshd ON(rshd.ingreso=i.id) " +
								"INNER JOIN cuentas c ON(c.id_ingreso=i.id) " +
							"WHERE " +
								"c.hospital_dia='"+ConstantesBD.acronimoSi+"' ";
		
		if(!UtilidadTexto.getBoolean(criteriosBusqueda.get("esSalida")+""))
		{	
			consulta+=	"AND i.estado='"+ConstantesIntegridadDominio.acronimoEstadoCerrado+"' ";
						
		}	
		else
			consulta+=	"AND i.estado='"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"' " ;
		
		consulta+=		"and i.fecha_egreso is NULL " +
						"AND (select count(1) from egresos e where e.cuenta=rshd.cuenta and e.codigo_medico is not null)=0 "+
						"and i.centro_atencion="+criteriosBusqueda.get("codigoCentroAtencion")+" " +
						"AND c.estado_cuenta in("+ConstantesBD.codigoEstadoCuentaActiva+","+ConstantesBD.codigoEstadoCuentaFacturadaParcial+") "+
						"AND rshd.tipo='"+tipo+"' ";
			
		
		if(criteriosBusqueda.containsKey("tipoIdentificacion"))
		{
			if(!UtilidadTexto.isEmpty(criteriosBusqueda.get("tipoIdentificacion")+""))
			{	
				consulta+="AND p.tipo_identificacion='"+criteriosBusqueda.get("tipoIdentificacion")+"' ";
			}		
		}
		if(criteriosBusqueda.containsKey("numeroIdentificacion"))
		{
			if(!UtilidadTexto.isEmpty(criteriosBusqueda.get("numeroIdentificacion")+""))
			{	
				consulta+="AND p.numero_identificacion='"+criteriosBusqueda.get("numeroIdentificacion")+"' ";
			}		
		}
		if(criteriosBusqueda.containsKey("primerNombre"))
		{
			if(!UtilidadTexto.isEmpty(criteriosBusqueda.get("primerNombre")+""))
			{	
				consulta+="AND UPPER(p.primer_nombre) LIKE UPPER('%"+criteriosBusqueda.get("primerNombre")+"%') ";
			}		
		}
		if(criteriosBusqueda.containsKey("primerApellido"))
		{
			if(!UtilidadTexto.isEmpty(criteriosBusqueda.get("primerApellido")+""))
			{	
				consulta+="AND UPPER(p.primer_apellido) LIKE UPPER('%"+criteriosBusqueda.get("primerApellido")+"%') ";
			}		
		}
		
		consulta+=" GROUP BY codigoreingresosalida, numeroingreso, consecutivoingreso, fechahoraingreso, fechahoraingresobd, codigopaciente, nombrepaciente, tipoid, numeroid, cuenta, fechaultimabd, fechaultima ";
		
		consulta+=" ORDER BY fechahoraingresobd,  nombrepaciente ";
		
		logger.info("\n CONSULTA listadoPacientesReingresoOSalida ===> "+consulta+"\n");
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			mapa=new HashMap();
			mapa.put("numRegistros","0");
		}
		return mapa;
	}
	
	/**
	 * Método que consulta el último codigo del reingreso salida hospital día por la cuenta
	 * @param con
	 * @param dto
	 * @return
	 */
	public static String obtenerUltimoCodigoReingresoSalidaHospitalDia(Connection con,DtoReingresoSalidaHospiDia dto)
	{
		try
		{
			String codigo = "";
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(obtenerUltimoCodigoReingresoSalidaHospitalDiaStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(dto.getCuenta()));
			pst.setString(2,dto.getTipo());
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				codigo = rs.getString("codigo");
			
			return codigo;
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerUltimoCodigoReingresoSalidaHospitalDia: "+e);
			return "";
		}
	}
	
	/**
	 * Método que realiza la eliminación de todos los registros de reingreso hospital día de una cuenta
	 * @param con
	 * @param dto
	 * @return
	 */
	public static int eliminarReingresoSalidaHospitalDiaXCuenta(Connection con,DtoReingresoSalidaHospiDia dto)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(eliminarReingresoSalidaHospitalDiaXCuentaStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1, Utilidades.convertirAEntero(dto.getCuenta()));
			
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en eliminarReingresoSalidaHospitalDiaXCuenta: "+e);
			return 0;
		}
	}
	
	/**
	 * Metodo que valida si para la fecha dada el paciente ya ha tenido salida
	 * @param con
	 * @param cuenta
	 * @param fecha
	 * @return
	 */
	public static boolean existeSalidaXFecha(Connection con,int cuenta,String fecha)
	{
		try
		{
			boolean respuesta=false;
			String consulta="SELECT fecha_salida AS fechaultimasalida FROM reingreso_salida_hospi_dia WHERE cuenta=? AND fecha_salida='"+fecha+"'";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet ));
			ps.setInt(1, cuenta);
			if(ps.executeQuery().next())
				respuesta=true;
			return respuesta;
		}
		catch(SQLException e)
		{
			logger.error("Error en eliminarReingresoSalidaHospitalDiaXCuenta: "+e);
			return false;
		}
	}
	
	 /**
	 * 
	 * @param con
	 * @param cuenta
	 * @param tipo
	 * @return
	 */
	public static boolean existeReingresoOSalidaActivo(Connection con, int cuenta, String tipo,int codigo)
	{
		try
		{
			String consulta="SELECT codigo from reingreso_salida_hospi_dia where cuenta="+cuenta+" and tipo='"+tipo+"'";
			if(codigo>0)
				consulta+=" AND codigo="+codigo;
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet ));
			if(ps.executeQuery().next())
				return true;
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return false;
	}
	
}
