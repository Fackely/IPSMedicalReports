package com.princetonsa.dao.sqlbase.manejoPaciente;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;
import com.princetonsa.mundo.manejoPaciente.ConsultaPreingresos;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.Utilidades;










/**
 * Clase consulta preingresos
 * @author Jhony Alexander Duque A.
 * 
 */
public class SqlBaseConsultaPreingresosDao
{
	/**
	 *Mensajes de error 
	 */
	static Logger logger = Logger.getLogger(SqlBaseConsultaPreingresosDao.class);

	/*---------------------------------
	 *  indices
	 --------------------------------*/
	static String [] indicesCriterios = ConsultaPreingresos.indicesCriterios;
	
	/*----------------------------------------------------------------------------------------
	 *                              ATRIBUTOS CONSULTA DE PREINGRESOS
	 -----------------------------------------------------------------------------------------*/
	/**
	 * cadena en cargada de consultar los preingresos
	 */
	private static final String strConsulta=" SELECT " +
													" i.centro_atencion As centro_atencion0, " +
													" getnomcentroatencion(i.centro_atencion) As nombre_centro_atencion1, " +
													" i.id As id2, " +
													" i.consecutivo As consecutivo3, " +
													" to_char(i.fecha_preingreso_pen,'DD/MM/YYYY') As fecha_preingreso_pen4, " +
													" i.hora_preingreso_pen As hora_preingreso_pen5, " +
													" getnombreusuario(i.usuario_preingreso_pen) As usuario_preingreso_pen6, " +
													" to_char(i.fecha_preingreso_gen,'DD/MM/YYYY') As fecha_preingreso_gen7, " +
													" i.hora_preingreso_gen As hora_preingreso_gen8, " +
													" getnombreusuario(i.usuario_preingreso_gen) As usuario_preingreso_gen9, " +
													" case when preingreso='PEN' then 'Pendiente' else case when preingreso='GEN' then 'Generado' end end As estado10," +
													" gettipoid(i.codigo_paciente) As tipo_ident11, " +
													" getidentificacionpaciente(i.codigo_paciente) As ident_pac12, " +
													" getnombrepersona(i.codigo_paciente) As nombre_pac13, " +
													" i.codigo_paciente As codigo_pac14 " +
											" FROM ingresos i ";
	
	
	
	private static final String strConsultaDetallePreingresos = " SELECT " +
																		" getnombreconvenioxingreso (c.id_ingreso) As convenio_ppal0, " +
																		" c.id As codigo_cuenta1," +
																		" c.via_ingreso As via_ingreso2," +
																		" getnombreviaingreso(c.via_ingreso) As nombre_via_ingreso3," +
																		" c.estado_cuenta As estado_cuenta4," +
																		" getnombreestadocuenta(c.estado_cuenta) As nombre_estado_cuenta5," +
																		" to_char(i.fecha_preingreso_pen,'DD/MM/YYYY') As fecha_preingreso_pen6, " +
																		" i.hora_preingreso_pen As hora_preingreso_pen7, " +
																		" getnombreusuario(i.usuario_preingreso_pen) As usuario_preingreso_pen8, " +
																		" to_char(i.fecha_preingreso_gen,'DD/MM/YYYY') As fecha_preingreso_gen9, " +
																		" i.hora_preingreso_gen As hora_preingreso_gen10, " +
																		" getnombreusuario(i.usuario_preingreso_gen) As usuario_preingreso_gen11 " +
																	" FROM cuentas c " +
																	" INNER JOIN ingresos i ON (i.id=c.id_ingreso) " +
																	" WHERE i.institucion=? AND i.id=? ";
	
	
	/**
	 * cadena que consulta los usuarios que ponen el preingreso en pendiente
	 */
	private static final String strConultaUsuariosPreingresoPen =" SELECT " +
																		" coalesce(i.usuario_preingreso_pen,'') As codigo, " +
																		" coalesce(getnombreusuario2(i.usuario_preingreso_pen),'') As nombre " +
																	" FROM ingresos i " +
																	" WHERE i.preingreso=? " +
																	" AND i.institucion=? " +
																	" GROUP BY "+
																		" i.usuario_preingreso_pen " +
																	" ORDER BY nombre";

	/**
	 * cadena que consulta los usuarios que ponen el preingreso en generado
	 */
	private static final String strConultaUsuariosPreingresoGen =" SELECT " +
																		" coalesce(i.usuario_preingreso_gen,'') As codigo, " +
																		" coalesce(getnombreusuario2(i.usuario_preingreso_gen),'') As nombre " +
																	" FROM ingresos i " +
																	" WHERE i.preingreso=? " +
																	" AND i.institucion=? " +
																	" GROUP BY "+
																		" i.usuario_preingreso_gen "+
																	" ORDER BY nombre";
	/*----------------------------------------------------------------------------------------
	 *                              FIN ATRIBUTOS  CONSULTA DE PREINGRESOS
	 -----------------------------------------------------------------------------------------*/
	
	/**
	 * Metodo encargado de cargar el detalle
	 * del preingresos
	 * @param connection
	 * @param criterios
	 * -------------------------------
	 * KEY'S DE MAPA CRITERIOS
	 * -------------------------------
	 * -- institucion5 --> Requerido
	 * -- ingreso8 --> Requerido
	 */
	public static HashMap cargarDetallePreingreso (Connection connection, HashMap criterios )
	{
		logger.info("\n entro a cargarDetallePreingreso --> "+criterios);
		HashMap result = new HashMap ();
		String cadena = strConsultaDetallePreingresos;
			
		logger.info("\n cadena --> "+cadena);
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//institucion
			ps.setInt(1, Utilidades.convertirAEntero(criterios.get(indicesCriterios[5])+""));
			//pk de ingresos
			ps.setInt(2, Utilidades.convertirAEntero(criterios.get(indicesCriterios[8])+""));
			result=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
			return result;
		}
		catch (SQLException e) 
		{
			logger.info("\n problema cargando el detalle de los Preingresos "+e);
		}
		
		return result;
	}
	
	
	
	
	/**
	 * Metodo encargado de obtener los usuarios de preingresos
	 * @param connection
	 * @param criterios
	 * --------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * --------------------------------
	 * -- estados3 --> Requerido
	 * -- institucion5 --> Requerido
	 */
	public static ArrayList<HashMap<String, Object>> obtenerUsuariosPreingreso (Connection connection, HashMap criterios)
	{
		logger.info("\n entre a obtenerUsuariosPreingreso --> "+criterios);
		
		ArrayList<HashMap<String, Object>> result = new ArrayList<HashMap<String,Object>>();
		String cadena="";
		//se pregunta si existe el criterio estado y que no venga vacio
		if (criterios.containsKey(indicesCriterios[3]) && UtilidadCadena.noEsVacio(criterios.get(indicesCriterios[3])+""))
			//se evalua en que estado esta (pendiente -"PEN" ó generado -"GEN" )
			if ((criterios.get(indicesCriterios[3])+"").equals(ConstantesIntegridadDominio.acronimoEstadoPendiente))
				cadena=strConultaUsuariosPreingresoPen;
			else
				if ((criterios.get(indicesCriterios[3])+"").equals(ConstantesIntegridadDominio.acronimoEstadoGenerado))
				cadena=strConultaUsuariosPreingresoGen;
		
		logger.info("\n   cadena ===> "+cadena);
		try 
		{

			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			//estado
			ps.setString(1, criterios.get(indicesCriterios[3])+"");
			//institucion
			ps.setInt(2, Utilidades.convertirAEntero(criterios.get(indicesCriterios[5])+""));
			
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			while (rs.next())
			{
				HashMap<String, Object> mapa=new HashMap<String, Object>();
				mapa.put("codigo", rs.getObject("codigo"));
				mapa.put("nombre", rs.getObject("nombre"));
				result.add(mapa);
			}
			
			
		} catch (Exception e) 
		{
			logger.info("\n problema obteniendo usuarios del preingreso "+e);
		}
		
		
		
		return result;
	}
	
	
	
	/**
	 * Metodo encargado de consultar el listado 
	 * de preingresos
	 * @author Jhony Duque A.
	 * @param connection
	 * @param criterios
	 * --------------------------
	 * 	KEY'S DEL MAPA CRITERIOS
	 * --------------------------
	 * -- centroAten0 --> Opcional
	 * -- fechaIni1 --> Opcional
	 * -- fechaFin2 --> Opcional
	 * -- estados3 --> Opcional
	 * -- usuarioGen4 --> Opcional
	 * -- institucion5 --> Requerido
	 * -- usuarioPen6
	 * -- paciente7
	 * @return  HashMap
	 * -------------------------------
	 * KEY'S DEL MAPA RSULTADO
	 * -------------------------------
	 * -- centroAtencion0_
	 * -- nombreCentroAtencion1_
	 * -- id2_
	 * -- consecutivo3_
	 * -- fechaPreingresoPen4_
	 * -- horaPreingresoPen5_
	 * -- usuarioPreingresoPen6_
	 * -- fechaPreingresoGen7_
	 * -- horaPreingresoGen8_
	 * -- usuarioPreingresoGen9_
	 * -- estado10_
	 * -- tipoIdent11_
	 * -- identPac12_
	 * -- nombrePac13_
	 * -- codigoPac14_
	 */
	public static HashMap cargarListadoPreingresos (Connection connection, HashMap criterios )
	{
		logger.info("\n entro a cargarListadoPreingresos --> "+criterios);
		HashMap result = new HashMap ();
		String cadena = strConsulta;
		String where = " WHERE i.institucion="+criterios.get(indicesCriterios[5]);
		String order="";
		//se validan los estados del preingreso
		if (criterios.containsKey(indicesCriterios[3]) && UtilidadCadena.noEsVacio(criterios.get(indicesCriterios[3])+""))
			where+=" AND preingreso IN ("+criterios.get(indicesCriterios[3])+")";
		//se valida el centro de atencion
		if (criterios.containsKey(indicesCriterios[0]) && UtilidadCadena.noEsVacio(criterios.get(indicesCriterios[0])+""))
			where+=" AND centro_atencion="+criterios.get(indicesCriterios[0]);
		// se validan el rango de fechas a buscar
		if (criterios.containsKey(indicesCriterios[1]) && UtilidadCadena.noEsVacio(criterios.get(indicesCriterios[1])+"") && 
			criterios.containsKey(indicesCriterios[2]) && UtilidadCadena.noEsVacio(criterios.get(indicesCriterios[2])+""))
			if ((criterios.get(indicesCriterios[3])+"").equals("'"+ConstantesIntegridadDominio.acronimoEstadoPendiente+"'"))
				where+=" AND fecha_preingreso_pen between '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get(indicesCriterios[1])+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get(indicesCriterios[2])+"")+"' ";
			else	
				if ((criterios.get(indicesCriterios[3])+"").equals("'"+ConstantesIntegridadDominio.acronimoEstadoGenerado+"'"))
					where+=" AND fecha_preingreso_gen between '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get(indicesCriterios[1])+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get(indicesCriterios[2])+"")+"' ";
			
		//se valida por el usuario
		if ((criterios.get(indicesCriterios[3])+"").equals("'"+ConstantesIntegridadDominio.acronimoEstadoPendiente+"'"))
			if (criterios.containsKey(indicesCriterios[6]) && UtilidadCadena.noEsVacio(criterios.get(indicesCriterios[6])+""))
				where+=" AND usuario_preingreso_pen='"+criterios.get(indicesCriterios[6])+"'";
		else	
			if ((criterios.get(indicesCriterios[3])+"").equals("'"+ConstantesIntegridadDominio.acronimoEstadoGenerado+"'"))
				if (criterios.containsKey(indicesCriterios[4]) && UtilidadCadena.noEsVacio(criterios.get(indicesCriterios[4])+""))
					where+=" AND usuario_preingreso_gen='"+criterios.get(indicesCriterios[4])+"'";
		
		if (criterios.containsKey(indicesCriterios[7]) && UtilidadCadena.noEsVacio(criterios.get(indicesCriterios[7])+""))
		{
			where+=" AND codigo_paciente="+criterios.get(indicesCriterios[7]);
			order=" ORDER BY fecha_preingreso_pen DESC ";
		}
		else
			order=" ORDER BY i.id  DESC ";
		
		cadena+=where+order;
		logger.info("\n cadena --> "+cadena);
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			result=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
			return result;
		}
		catch (SQLException e) 
		{
			logger.info("\n problema consultando los preingresos "+e);
		}
		
		return result;
	}
}