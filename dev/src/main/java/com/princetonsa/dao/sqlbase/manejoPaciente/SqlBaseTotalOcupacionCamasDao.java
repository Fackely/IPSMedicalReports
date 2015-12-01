package com.princetonsa.dao.sqlbase.manejoPaciente;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;
import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseTotalOcupacionCamasDao;
import com.princetonsa.decorator.ResultSetDecorator;

/**
 * Fecha: Febrero - 2008
 * @author Jhony Alexander Duque A.
 *
 */

public class SqlBaseTotalOcupacionCamasDao 
{

	/**
	 * Consulta la cantidad de camas y el tipo de habitacion segun los estados de camas
	 */
	/**private static String consultaDet = " SELECT DISTINCT " +
												 " c.numero_cama As numero_cama0," +
												 " c.codigo As codigo_cama1, " +
												 " c.estado As estado_cama2, " +
												 " h.tipo_habitacion As tipo_habitacion3, " +
												 " p.nombre As nombre_piso4 " +
											" FROM camas1 c " +
											" INNER JOIN habitaciones h ON (h.codigo=c.habitacion) " +
											" INNER JOIN tipo_habitacion th ON (th.codigo=h.tipo_habitacion AND th.institucion=h.institucion) " +
											" INNER JOIN pisos p ON (p.codigo=h.piso) ";
											*/
	
	private static String consultaDet = " SELECT DISTINCT " +
												 " p.codigo As codigo_piso0, " +
												 " p.nombre As nombre_piso1 "; 
	
	
	private static final String [] indices={"numeroCama0_","codigoCama1_","estadoCama2_","tipoHabitacion3_","nombrePiso4_"};
	
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseTotalOcupacionCamasDao.class);
	
	/**
	 * Indices del Mapa Inicial
	 */
	private static String[] indices_inicial = {"codigo_", "nombre_", "checkcodigo_", "detHabitacion_"};
	
	/**
	 * Metodo que consulta los Estados de Cama
	 * @param con
	 * @return
	 */
	public static HashMap consultarEstados(Connection con) 
	{
		HashMap<String, Object> mapa = new HashMap<String, Object>();
		String cadena="";
		try
		{
			cadena = "SELECT codigo, nombre FROM estados_cama ORDER BY nombre";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarEstados: "+e);
		}
		logger.info("====>Estados Cama: "+cadena);
		mapa.put("indices", indices_inicial);
		return mapa;
	}
	
	
	
	/**
	 * Metodo encargado de consultar las camas en los diferentes estados
	 * @author Jhony Alexander Duque
	 * @param criterios
	 * -------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * -------------------------------
	 * -- centroAtencion --> Requerido
	 * -- institucion --> Requerido
	 * -- estadosCama --> Requerido
	 * -- incluirCamasUrg --> Requerido
	 * @param con
	 * @return mapa
	 * --------------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * --------------------------------
	 * numeroCama0_,codigoCama1_,
	 * estadoCama2_,tipoHabitacion3_,
	 * nombrePiso4_
	 */
	public static HashMap consultarTotalEstados2(Connection connection,HashMap criterios)
	{
		logger.info("\n entre a consultarTotalEstados criterios -->"+criterios);
		
		String cadena = consultaDet;
		
		String where = " WHERE h.centro_atencion=? " +
					   " AND c.institucion=? ";
		
		String group =" ORDER BY nombre_piso4,estado_cama2 ";
	
		if (!UtilidadTexto.getBoolean(criterios.get("incluirCamasUrg")+""))
			where+=" AND c.centro_costo not in (SELECT centro_costo FROM centro_costo_via_ingreso ccvi WHERE ccvi.via_ingreso ="+ConstantesBD.codigoViaIngresoUrgencias+" AND ccvi.institucion=c.institucion ) ";
		
		if (UtilidadCadena.noEsVacio(criterios.get("estadosCama")+""))
			where+=" AND c.estado IN ("+criterios.get("estadosCama")+") ";
		
		
		cadena+=where+group;
		
		logger.info("\n cadena  -->"+cadena);
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//centro de atencion
			ps.setInt(1, Utilidades.convertirAEntero(criterios.get("centroAtencion")+""));
			//institucion
			ps.setInt(2, Utilidades.convertirAEntero(criterios.get("institucion")+""));
			
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
			ps.close();
			return mapaRetorno;
			
		} 
		catch (SQLException e) 
		{
			logger.info("\n problema consultando las camas de total ocupacion camas "+e);
		}
		
		
		return null;
	}
	
	/**
	 * Metodo encargado de consultar el total de ocupacion de camas.
	 * @param connection
	 * @param criterios
	 * --------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * --------------------------
	 * -- centroAtencion --> Requerido
	 * -- institucion  --> Requerido
	 * -- estadosCama  --> Requerido
	 * -- detalle  --> Requerido
	 * -- incluirCamasUrg --> Requerido
	 * @return
	 */
	public static HashMap consultarTotalEstados(Connection connection,HashMap criterios)
	{
		logger.info("\n entre a consultarTotalEstados criterios -->"+criterios);
		ArrayList<HashMap<String , Object>> tiposHabitacion = new ArrayList<HashMap<String,Object>>();
		String [] estados = {};
		ArrayList detalle = new ArrayList();
		
		String cadena = consultaDet;
		
		String where = " WHERE p.centro_atencion=? " +
		   			   " AND p.institucion=? ";
		
		String group =" ORDER BY nombre_piso1 ";
		
		//se toman los tipos de habitacion
		tiposHabitacion=Utilidades.obtenerTiposHabitacion(connection, criterios.get("institucion")+"");
		//se toman los estados de las camas por los que se desea bucar.
		estados=(criterios.get("estadosCama")+"").split(",");
		//en este mapa se meten los codigos de los estados de las camas
		//que van a llevar un detalle.
		detalle =(ArrayList) criterios.get("detalle");
		
		for (int i=0;i<estados.length;i++)
		{
			if (existeDato(detalle,estados[i]))
			{
				logger.info("\n entre -->"+i);
				for (int j=0;j<tiposHabitacion.size();j++)
				{
					HashMap tipoHbit = (HashMap) tiposHabitacion.get(j); 
					cadena +=", getdetallecantidadcamas(p.codigo,"+estados[i]+",'"+tipoHbit.get("codigo")+"',"+criterios.get("institucion")+") As cantidad_"+i+"_"+j;
				}
			}
			else
				cadena +=", getcantidadcamas(p.codigo,"+estados[i]+","+criterios.get("institucion")+") As cantidad_"+i;
		}
		
		cadena+=" FROM pisos p " +
				" INNER JOIN  habitaciones h ON (h.piso=p.codigo) " +
				" INNER JOIN camas1 c ON (c.habitacion=h.codigo) ";
				
		
		if (!UtilidadTexto.getBoolean(criterios.get("incluirCamasUrg")+""))
			where+=" AND c.centro_costo not in (SELECT centro_costo FROM centro_costo_via_ingreso ccvi WHERE ccvi.via_ingreso ="+ConstantesBD.codigoViaIngresoUrgencias+" AND ccvi.institucion=c.institucion ) ";
		
		if (UtilidadCadena.noEsVacio(criterios.get("estadosCama")+""))
			where+=" AND c.estado IN ("+criterios.get("estadosCama")+") ";
		
		
		cadena+=where+group;
		
		logger.info("\n cadena  -->"+cadena);
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//centro de atencion
			ps.setInt(1, Utilidades.convertirAEntero(criterios.get("centroAtencion")+""));
			//institucion
			ps.setInt(2, Utilidades.convertirAEntero(criterios.get("institucion")+""));
			
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,false);
			ps.close();
			return mapaRetorno;
			
		} 
		catch (SQLException e) 
		{
			logger.info("\n problema consultando las camas de total ocupacion camas "+e);
		}
		
		return null;
	}
	
	public static boolean existeDato (ArrayList datos, String valor)
	{
		for (int i=0;i<datos.size();i++)
			if ((datos.get(i)+"").equals(valor))
				return true;
		
		return false;
	}
	
	
	
	
}