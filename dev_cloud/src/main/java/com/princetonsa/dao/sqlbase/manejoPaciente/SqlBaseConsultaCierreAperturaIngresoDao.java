package com.princetonsa.dao.sqlbase.manejoPaciente;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;

/**
 * @author Mauricio Jllo
 * Fecha: Mayo de 2008
 */

public class SqlBaseConsultaCierreAperturaIngresoDao
{

	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseConsultaCierreAperturaIngresoDao.class);
	
	/**
	 * @param con
	 * @param vo
	 * @return
	 */
	public static HashMap consultarCierreAperturaIngresos(Connection con, HashMap vo)
	{
		String consulta = "SELECT "+
							"ci.codigo AS codigocierre, ";
		
		String condicion = "";
		String motivo = "";
		
		if((vo.get("tipo")+"").equals(ConstantesIntegridadDominio.acronimoCierreIngreso))
		{
			consulta += "to_char(ci.fecha_cierre, 'DD/MM/YYYY') AS fecha, "+
						"ci.hora_cierre AS hora, "+
						"ci.motivo_cierre AS motivo, ";
			motivo = "ci.motivo_cierre"; 
		}
		else if((vo.get("tipo")+"").equals(ConstantesIntegridadDominio.acronimoAperturaIngreso))
		{
			consulta += "to_char(ci.fecha_apertura, 'DD/MM/YYYY') AS fecha, "+
						"ci.hora_apertura AS hora, "+
						"ci.motivo_apertura AS motivo, ";
			motivo = "ci.motivo_apertura";
		}
							
		consulta += "i.consecutivo AS numeroingreso, "+
					"i.id AS idingreso, "+
					"getnomcentroatencion(i.centro_atencion) AS centroatencion, "+
					"to_char(i.fecha_ingreso, 'DD/MM/YYYY') AS fechaingreso, "+
					"i.hora_ingreso AS horaingreso, "+
					"getnombrepersona(i.codigo_paciente) AS paciente, "+
					"coalesce(i.preingreso,'') AS preingreso "+
					"FROM cierre_ingresos ci "+
					"INNER JOIN ingresos i ON (ci.id_ingreso = i.id) "+
					"INNER JOIN mot_cierre_apertura_ingresos mcai ON ("+motivo+" = mcai.codigo AND ci.institucion = mcai.institucion) "+
					"WHERE ";
		
		//Validamos la consulta por el centro de atencion. Como es requerido no se valida
		condicion = "i.centro_atencion = "+vo.get("centroAtencion")+" ";
		
		//Validamos la consulta por el tipo de motivo (Cierre o Apertura). Como es requerio no se valida
		condicion = condicion + "AND mcai.tipo = '"+vo.get("tipo")+"' ";
		
		//Validamos la consulta por el motivo y por el usuario. Ya que no son requeridos
		if((vo.get("tipo")+"").equals(ConstantesIntegridadDominio.acronimoCierreIngreso))
		{
			if(!(vo.get("motivo")+"").equals(""))
				condicion = condicion + "AND ci.motivo_cierre = "+vo.get("motivo")+" ";
			if(!(vo.get("usuario")+"").equals(""))
				condicion = condicion + "AND ci.usuario_cierre = '"+vo.get("usuario")+"' ";
			
		}
		else if((vo.get("tipo")+"").equals(ConstantesIntegridadDominio.acronimoAperturaIngreso))
		{
			if(!(vo.get("motivo")+"").equals(""))
				condicion = condicion + "AND ci.motivo_apertura = "+vo.get("motivo")+" ";
			if(!(vo.get("usuario")+"").equals(""))
				condicion = condicion + "AND ci.usuario_apertura = '"+vo.get("usuario")+"' ";
		}
		
		//Validamos la consulta por el rango de fecha de ingreso. Como es requerido no se valida
		condicion = condicion + "AND i.fecha_ingreso BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"' ";
		
		//Ordenamiento descendente por la fecha
		condicion += "ORDER BY i.fecha_ingreso DESC, i.hora_ingreso DESC ";
		
		HashMap mapa = new HashMap<String, Object>();
        mapa.put("numRegistros","0");
        try
        {
        	PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta+condicion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            logger.info("\n\n====>Consulta: "+consulta+condicion);
            mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
        }
        catch (SQLException e)
        {
            logger.error("ERROR EJECUNTANDO LA CONSULTA DE CIERRE/APERTURA INGRESOS");
            e.printStackTrace();
        }
        return (HashMap)mapa.clone();
	}

	/**
	 * Metodo que carga el detalle del Cierre/Apertura de Ingreso seleccionado
	 * @param con
	 * @param codigoIngreso
	 * @param codigoCierreApertura 
	 * @return
	 */
	public static HashMap detalleCierreAperturaIngreso(Connection con, int codigoCierreApertura, String motivo)
	{
		String consulta = "SELECT ";

		if(motivo.equals(ConstantesIntegridadDominio.acronimoCierreIngreso))
		{
			consulta += "getdescmotivoingreso(ci.motivo_cierre) AS motivo, "+
						"ci.usuario_cierre AS usuario, ";
		}
		else if(motivo.equals(ConstantesIntegridadDominio.acronimoAperturaIngreso))
		{
			consulta += "getdescmotivoingreso(ci.motivo_apertura) AS motivo, "+
						"ci.usuario_apertura AS usuario, ";
		}
		
		consulta += "cu.id AS idcuenta, "+
					"getnombreviaingreso(cu.via_ingreso) AS viaingreso, "+
					"getnombreestadocuenta(cu.estado_cuenta) AS estadocuenta "+
					"FROM cierre_ingresos ci "+
					"INNER JOIN ingresos i ON (ci.id_ingreso = i.id) "+
					"INNER JOIN cuentas cu ON (i.id = cu.id_ingreso) "+
					"WHERE "+
					"ci.codigo = "+codigoCierreApertura+" ";
		
		HashMap mapa = new HashMap<String, Object>();
        mapa.put("numRegistros","0");
        try
        {
        	PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            logger.info("\n\n====>Consulta Detalle: "+consulta);
            mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
        }
        catch (SQLException e)
        {
            logger.error("ERROR EJECUNTANDO LA CONSULTA DETALLE DEL INGRESO DE CIERRE/APERTURA");
            e.printStackTrace();
        }
        return (HashMap)mapa.clone();
	}

}