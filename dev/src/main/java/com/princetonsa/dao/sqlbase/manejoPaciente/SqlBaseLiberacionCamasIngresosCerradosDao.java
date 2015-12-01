package com.princetonsa.dao.sqlbase.manejoPaciente;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.Utilidades;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.manejoPaciente.LiberacionCamasIngresosCerrados;

/**
 * Clase consulta  de liberacion de camas ingresos cerrados
 * @author Jhony Alexander Duque A.
 * 
 */
public class SqlBaseLiberacionCamasIngresosCerradosDao
{
	/**
	 *Mensajes de error 
	 */
	static Logger logger = Logger.getLogger(SqlBaseLiberacionCamasIngresosCerradosDao.class);

	/*---------------------------------
	 *  indices
	 --------------------------------*/
	static String [] indices = LiberacionCamasIngresosCerrados.indicesListado;
	static String [] indicesDetalle = LiberacionCamasIngresosCerrados.indicesDetalle;
	/*----------------------------------------------------------------------------------------
	 *                              ATRIBUTOS CONSULTA DE PREINGRESOS
	 -----------------------------------------------------------------------------------------*/
	
	/**
	 * String encargado de consultar los ingresos en estado cerrado
	 * y asociados a un centro de atencion dado. y solo con via
	 * de ingreso hospitalizacion, el ingreso debe tener egreso administrativo,
	 * y el paciente debe estar ocupando la cama , la cama debe estar en 
	 * los siguientes estados: ocupada, pendiente por trasladar, pendiente por
	 * remitir y con salida.
	 */
	private static final String strConsultaIngresos=" SELECT " +
															 " to_char(i.fecha_ingreso,'YYYY/MM/DD') As fecha_ingreso0, " +
															 " i.id As id1, " +
															 " i.consecutivo As consecutivo2, " +
															 " case when getfechacierreingresos(i.institucion,i.id) is null then to_char(i.fecha_egreso,'YYYY/MM/DD')  else getfechacierreingresos(i.institucion,i.id) end As fecha_cierre3, " +
															 " tc.codigo_nueva_cama As codigo_cama4," +
															 " getdescripcioncamaphc(tc.codigo_nueva_cama) As nombre_cama5 " +
														 " FROM  ingresos i " +
														 " inner join cuentas c ON (c.id_ingreso=i.id) " +
														 " inner join traslado_cama tc ON (tc.cuenta=c.id)" +
														 " WHERE " +
														 		" i.centro_atencion=? " +
														 		" AND i.estado='"+ConstantesIntegridadDominio.acronimoEstadoCerrado+"'" +
														 		" AND c.via_ingreso="+ConstantesBD.codigoViaIngresoHospitalizacion+
														 		" AND getexisteegreso(c.id)=?" +
														 		" AND getescamaocupada(tc.codigo_nueva_cama)='"+ConstantesBD.acronimoSi+"'" +
														 		" AND tc.fecha_finalizacion IS NULL " +
														 		" AND tc.hora_finalizacion IS NULL";
	
	
	
	private static final String strConsultainformacionDetalle=" SELECT " +
															 " i.fecha_ingreso As fecha_ingreso0," +
															 " i.id As id1, " +
															 " i.consecutivo As consecutivo2, " +
															 " getnombreviaingreso(c.via_ingreso) As via_ingreso3," +
															 " getnomcentrocosto(c.area) As area4," +
															 " getdescripcioncamaphc(tc.codigo_nueva_cama) As nombre_cama5," +
															 " getnombretipopaciente(c.tipo_paciente) As tipo_paciente6," +
															 " c.id As numero_cuenta7," +
															 " case when getfechacierreingresos(i.institucion,i.id) is null then to_char(i.fecha_egreso,'YYYY/MM/DD')  else getfechacierreingresos(i.institucion,i.id) end As fecha_cierre8, " +
															 " list(f.consecutivo_factura||'') as facturas9," +
															 " list(to_char(fecha,'dd/mm/yyyy')||'') as fecha_facturas10," +
															 " tc.codigo_nueva_cama As cama13 " +
														" FROM  ingresos i " +
														 " inner join cuentas c ON (c.id_ingreso=i.id) " +
														 " inner join sub_cuentas sc ON (sc.ingreso=i.id) " +
														 " inner join traslado_cama tc ON(tc.cuenta=c.id) " +
														 " left outer join  facturas f ON (f.sub_cuenta=sc.sub_cuenta) " +
														 " WHERE " +
														 		" i.centro_atencion=? " +
														 		" AND  i.estado='"+ConstantesIntegridadDominio.acronimoEstadoCerrado+"'" +
														 		" AND c.via_ingreso="+ConstantesBD.codigoViaIngresoHospitalizacion+
														 		" AND getexisteegreso(c.id)=?"+
														 		" AND getescamaocupada(tc.codigo_nueva_cama)='"+ConstantesBD.acronimoSi+"'" +
														 		" AND tc.fecha_finalizacion IS NULL " +
														 		" AND tc.hora_finalizacion IS NULL" +
														 		" AND i.id=?" +
														 		" GROUP BY i.id,i.consecutivo,via_ingreso3,area4,nombre_cama5,c.tipo_paciente,c.id,i.institucion,i.fecha_egreso,f.consecutivo_factura,i.fecha_ingreso, tc.codigo_nueva_cama";
	
															
	
	/*----------------------------------------------------------------------------------------
	 *                             FIN ATRIBUTOS CONSULTA DE PREINGRESOS
	 -----------------------------------------------------------------------------------------*/
	
	/**
	 * Metodo encargado de consultar el detalle de 
	 * l afuncionalidad liberacion camas ingresos cerrados
	 * @param connection
	 * @param criterios
	 * ---------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * ---------------------------
	 * -- centroAtencion11 --> Requerido
	 * -- ingreso12 --> Requerido
	 * @return mapa
	 * -------------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * -------------------------------
	 * -- id1_
	 * -- consecutivo2_
	 * -- viaIngreso3_
	 * -- area4_
	 * -- nombreCama5_
	 * -- tipoPaciente6_
	 * -- numeroCuenta7_
	 * -- fechaCierre8_
	 * -- facturas9_
	 * -- fechaFacturas10_
	 * -- centroAtencion11
	 */
	public static HashMap<?,?> consultaDetalle (Connection connection, HashMap<?,?> criterios)
	{
		logger.info("\n entro a consultaIngresos -->"+criterios);
		
		HashMap<?,?> result = new HashMap<Object, Object>();
		String cadena = strConsultainformacionDetalle;
		logger.info("\n cadena --> "+cadena);
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			//centro atencion
			ps.setInt(1, Utilidades.convertirAEntero(criterios.get(indicesDetalle[11])+""));
			//getexisteegreso MT 6412
			if(System.getProperty("TIPOBD").equals("POSTGRESQL"))
			{
				ps.setBoolean(2, true);
			}
			else
			{
				ps.setInt(2, 1);
			}
			//ingreso
			ps.setInt(3, Utilidades.convertirAEntero(criterios.get(indicesDetalle[12])+""));
			HashMap<?,?> mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), false, true);
			ps.close();
			return mapaRetorno;
			
		}
		catch (SQLException e) 
		{
			logger.info("\n problema consultando el detalle de liberacion de camas "+e);
			e.printStackTrace();
		}
		return result;
	}

	
	/**
	 * Metodo encargado de consultar los ingresos
	 * @param connection
	 * @param criterios
	 * ---------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * ---------------------------
	 * -- centroAtencion6 --> Requerido
	 * @return mapa
	 * -------------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * -------------------------------
	 * --fechaIngreso0_
	 * -- id1_
	 * -- consecutivo2_
	 * -- fechaCierre3_
	 * -- codigoCama4_
	 * -- nombreCama5_
	 */
	public static HashMap<?,?> consultaIngresos (Connection connection, HashMap<?,?> criterios)
	{
		logger.info("\n entro a consultaIngresos -->"+criterios);
		
		HashMap<?,?> result = new HashMap<Object,Object> ();
		String cadena = strConsultaIngresos;
		logger.info("\n cadena --> "+cadena);
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			//centro atencion
			ps.setInt(1, Utilidades.convertirAEntero(criterios.get(indices[6])+""));
			//getexisteegreso MT 6412
			if(System.getProperty("TIPOBD").equals("POSTGRESQL"))
			{
				ps.setBoolean(2, true);
			}
			else
			{
				ps.setInt(2, 1);
			}
			HashMap<?,?> mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
			ps.close();
			return mapaRetorno;
			
		}
		catch (SQLException e) 
		{
			logger.info("\n problema consultando los ingresos "+e);
			e.printStackTrace();
		}
		return result;
	}
}