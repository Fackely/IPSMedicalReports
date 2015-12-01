/*
 * Oct 22, 2007
 * Proyect axioma_oct0707
 * Paquete com.princetonsa.dao.sqlbase.historiaClinica
 * @author Jorge Armando Osorio Velasquez
 * Compilador Java 1.5.0_07-b03
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
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

/**
 * @author Jorge Armando Osorio Velasquez
 *
 */
public class SqlBaseRegistroTerapiasGrupalesDao 
{
	/**
	 * 
	 */
	private static Logger logger=Logger.getLogger(SqlBaseRegistroTerapiasGrupalesDao.class);

	/**
	 * 
	 */
	private static String cadenaConsultaPacientes="SELECT DISTINCT " +
										" p.primer_nombre||' '||p.segundo_nombre||' '||p.primer_apellido||' '||p.segundo_apellido as nombrepaciente," +
										" p.codigo as codigopaciente," +
										" p.tipo_identificacion as tipoidpaciente," +
										" p.numero_identificacion as numeroidentificacion," +
										" c.id as cuenta," +
										" c.id_ingreso as ingreso " +
								" from cuentas c " +
								" inner join ingresos i on(c.id_ingreso=i.id) " +
								" inner join reingreso_salida_hospi_dia rshd on(c.id=rshd.cuenta) " +
								" inner join personas p on(p.codigo=i.codigo_paciente) " +
								" left outer join egresos e on(c.id=e.cuenta) " +
								" where e.codigo_medico is null and i.centro_atencion = ? and i.estado='"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"' and c.hospital_dia='"+ConstantesBD.acronimoSi+"' ";

	
	/**
	 * 
	 */
	private static String cadenaConsultaPacientesCentro="SELECT DISTINCT " +
										" p.primer_nombre||' '||p.segundo_nombre||' '||p.primer_apellido||' '||p.segundo_apellido as nombrepaciente," +
										" p.codigo as codigopaciente," +
										" p.tipo_identificacion as tipoidpaciente," +
										" p.numero_identificacion as numeroidentificacion," +
										" c.id as cuenta," +
										" c.id_ingreso as ingreso " +
									" from cuentas c " +
									" inner join ingresos i on(c.id_ingreso=i.id) " +
									" inner join personas p on(p.codigo=i.codigo_paciente) " +
									" left outer join egresos e on(c.id=e.cuenta) " +
									" where e.codigo_medico is null and i.centro_atencion = ? and i.estado='"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"' and c.area=? and c.estado_cuenta in ('"+ConstantesBD.codigoEstadoCuentaActiva+"','"+ConstantesBD.codigoEstadoCuentaFacturadaParcial+"')";
	
	
	/**
	 * 
	 */
	private static String cadenaInsertarCuentas="INSERT INTO cuentas_terapia_grupal (codigo_terapia,cuenta)values(?,?)";
	
	
	/**
	 * 
	 * @param con
	 * @param fecha
	 * @param centroAtencion 
	 * @param codigoSexo 
	 * @return
	 */
	public static HashMap<String, Object> getpacientesTerapiasGrupales(Connection con, String fecha, int centroAtencion, int codigoSexo) 
	{
		String cadena=cadenaConsultaPacientes;
		HashMap<String, Object> mapa=new HashMap<String, Object>();
		mapa.put("numRegistros", "0");
		if(UtilidadTexto.isEmpty(fecha))
		{
			 cadena=cadena+" and rshd.fecha_ingreso=current_date";
		}
		else
		{
			cadena=cadena+" and rshd.fecha_ingreso='"+fecha+"'";
		}
		if(codigoSexo>0)
		{
			cadena=cadena+"  and p.sexo="+codigoSexo;
		}
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			ps.setInt(1, centroAtencion);
			mapa=(HashMap<String, Object>)UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())).clone();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseRegistroTerapiasGrupalesDao "+sqlException.toString() );
			}
		}
		return mapa;
	}



	/**
	 * 
	 * @param con
	 * @param codigoTerapia
	 * @param vo
	 * @return
	 */
	public static boolean insertarCuentasTerapia(Connection con, int codigoTerapia, HashMap<String, Object> vo) 
	{
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarCuentas));
			for(int i=0;i<Utilidades.convertirAEntero(vo.get("numRegistros")+"");i++)
			{
				if(UtilidadTexto.getBoolean(vo.get("seleccionado_"+i)+""))
				{
					ps.setDouble(1, Utilidades.convertirADouble(codigoTerapia+""));
					ps.setInt(2, Utilidades.convertirAEntero(Utilidades.obtenerValor(vo.get("cuenta_"+i)+"")+""));
					if(ps.executeUpdate()<=0)
						return false;
				}
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			return false;
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseRegistroTerapiasGrupalesDao "+sqlException.toString() );
			}
		}
		return true;
	}


	/**
	 * 
	 * @param con
	 * @param fecha
	 * @param centroAtencion
	 * @param codigoSexo
	 * @param centroCosto
	 * @return
	 */
	public static HashMap<String, Object> getpacientesTerapiasGrupalesCentro(Connection con, String fecha, int centroAtencion, int codigoSexo, String centroCosto) 
	{
		String cadena=cadenaConsultaPacientesCentro;
		HashMap<String, Object> mapa=new HashMap<String, Object>();
		mapa.put("numRegistros", "0");
		/*if(UtilidadTexto.isEmpty(fecha))
		{
			 cadena=cadena+" and rshd.fecha_ingreso=current_date";
		}
		else
		{
			cadena=cadena+" and rshd.fecha_ingreso='"+fecha+"'";
		}*/
		if(codigoSexo>0)
		{
			cadena=cadena+"  and p.sexo="+codigoSexo;
		}
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			ps.setInt(1, centroAtencion);
			ps.setInt(2, Utilidades.convertirAEntero(centroCosto));
			mapa=(HashMap<String, Object>)UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())).clone();
		}
		
		catch(SQLException e)
		{
			e.printStackTrace();
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseRegistroTerapiasGrupalesDao "+sqlException.toString() );
			}
		}
		return mapa;
	}
	
	
	
	
	

	
	
	
}
