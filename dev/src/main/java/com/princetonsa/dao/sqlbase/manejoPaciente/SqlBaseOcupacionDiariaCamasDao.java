package com.princetonsa.dao.sqlbase.manejoPaciente;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.util.HashMap;
import org.apache.log4j.Logger;
import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseOcupacionDiariaCamasDao;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.manejoPaciente.OcupacionDiariaCamas;

/**
 * Fecha: Febrero - 2008
 * @author Jhony Alexander Duque A.
 *
 */

public class SqlBaseOcupacionDiariaCamasDao 
{
	
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseOcupacionDiariaCamasDao.class);
	
	//indices
	public static  String [] indicesCriterios = OcupacionDiariaCamas.indicesCriterios;
	
	
	public static final String strCadenaConsulta=" SELECT DISTINCT" +
														" sum(pac.cantidad) As cantidad0, " +
														" pac.estadocama As codigo_estado1, " +
														" getnombreestadocama(pac.estadocama) As nom_estado_cama2, ";
													
													

	
	
	/**
	 * Metodo encargado de consultar
	 * la informacion para el archivo plano
	 * @param criterios
	 * --------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * --------------------------------
	 * -- institucion6 --> Requerido
	 * -- centroAtencion0 --> Requerido
	 * -- fechaInicial1 --> Requerido
	 * -- fechaFinal2 --> Requerido
	 * -- incluirCamasUrgencias4 --> Opcional
	 * -- rompimientoPorPiso3 --> Opcional
	 * @param estadosCama debe ir ejm. 1,2,3
	 * @return mapa 
	 * -----------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * -----------------------------
	 * cantidad0, codigoEstado1,
	 * nomEstadoCama2,piso3,
	 * fecha4
	 */
	public  static HashMap consulta (Connection connection, HashMap criterios,String estadosCama)
	{
		logger.info("\n entre a  consulta  criterios -->"+criterios);
		
		String cadena=strCadenaConsulta;
		String from="  FROM proceso_automatico_censo pac ";
		String group="";
		String order="";
		
		if (UtilidadTexto.getBoolean(criterios.get(indicesCriterios[3])+""))
			cadena+=" to_char(pac.fecha,'dd') As fecha3," +
					" pac.piso As codigo_piso4," +
					" getnombrepiso(pac.piso) As nombre_piso5  ";
		else
			cadena+=" to_char(pac.fecha,'dd') As fecha3 ";
		
		String where=obtenerWhere(criterios, estadosCama);
		
		if (UtilidadTexto.getBoolean(criterios.get(indicesCriterios[3])+""))
			//group+=" GROUP BY nombre_piso5,codigo_piso4,fecha3,nom_estado_cama2,codigo_estado1  ";
			group+=" GROUP BY getnombrepiso(pac.piso),pac.piso,TO_CHAR(pac.fecha,'dd'),getnombreestadocama(pac.estadocama),pac.estadocama";
		else
			group+=" GROUP BY TO_CHAR(pac.fecha,'dd'), getnombreestadocama(pac.estadocama), pac.estadocama ";
		
		if (UtilidadTexto.getBoolean(criterios.get(indicesCriterios[3])+""))
			order=" ORDER BY nombre_piso5,fecha3,nom_estado_cama2 ";
		else
			order=" ORDER BY fecha3";
		
		cadena+=from+where+group+order;
		logger.info("\n consulta -->"+cadena);
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
			ps.close();
			return mapaRetorno;
		}
		catch (Exception e) 
		{
			logger.info("\n problemas consultando la ocupacion de camas diaria "+e);
		}
		
		
	return null;	
	}
	
	
	
	
	
	/**
	 * Metodo encargado de generar las clausulas 
	 * where para la consulta en el birt.
	 * @param criterios
	 * --------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * --------------------------------
	 * -- institucion6 --> Requerido
	 * -- centroAtencion0 --> Requerido
	 * -- fechaInicial1 --> Requerido
	 * -- fechaFinal2 --> Requerido
	 * -- incluirCamasUrgencias4 --> Opcional
	 * -- rompimientoPorPiso3 --> Opcional
	 * @param estadosCama debe ir ejm. 1,2,3
	 * @return cadena Where
	 */
	public static String obtenerWhere (HashMap criterios, String estadosCama)
	{
		String where=" WHERE ";
		String order="";
		String inner="";
		String group="";
		//**************************************************************
		//estos datos son obligatorios por tal motivo no se 
		//valida si vinen o no
		//***************************************************************
		//institucion
		where+=" pac.institucion="+criterios.get(indicesCriterios[6]);
		//centro de atencion
		where+=" AND pac.centro_atencion="+criterios.get(indicesCriterios[0]);
		//rango de fechas
		where+=" AND pac.fecha BETWEEN to_date('"+UtilidadFecha.conversionFormatoFechaABD(criterios.get(indicesCriterios[1])+"")+"','YYYY-MM-DD') AND to_date('"+UtilidadFecha.conversionFormatoFechaABD(criterios.get(indicesCriterios[2])+"")+"','YYYY-MM-DD')"; 
		//estados cama
		where+=" AND pac.estadocama IN ("+estadosCama+")";
		//***************************************************************
		//estos campos pueden ser variables por tal motivo se evaluan como vien
		

		if (!UtilidadTexto.getBoolean(criterios.get(indicesCriterios[4])+""))
		{
			inner=" INNER JOIN centros_costo cc ON (cc.centro_atencion=pac.centro_atencion)";
			where+=" AND cc.codigo not in (SELECT centro_costo FROM centro_costo_via_ingreso ccvi WHERE ccvi.via_ingreso ="+ConstantesBD.codigoViaIngresoUrgencias+" AND ccvi.institucion=pac.institucion ) ";
		}
		//if (UtilidadTexto.getBoolean(criterios.get(indicesCriterios[3])+""))
			//group=" GROUP BY pac.piso ";
		
		
		return inner+where+group+order;
	}
	
	
	
}