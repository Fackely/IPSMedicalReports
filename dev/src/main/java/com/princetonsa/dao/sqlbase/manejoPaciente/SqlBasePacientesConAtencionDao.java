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
import util.UtilidadCadena;
import util.Utilidades;

/**
 * @author Mauricio Jllo
 * Fecha: Junio de 2008
 */

public class SqlBasePacientesConAtencionDao
{
	
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger = Logger.getLogger(SqlBasePacientesConAtencionDao.class);
	
	/**
	 * Cadena para consultar los pacientes hospitalizados
	 */
	private static String strConPacientesConAtencion = "SELECT "+
															"i.id AS ingreso, "+ 
															"cu.id AS numerocuenta, "+ 
															"coalesce(getcamacuenta(cu.id, cu.via_ingreso), '') AS numerocama, "+ 
															"getidpaciente(i.codigo_paciente) AS idpaciente, "+ 
															"getnombrepersona(i.codigo_paciente) AS nombrepaciente, "+ 
															"getdiagnosticopaciente(cu.id, cu.via_ingreso) AS diagnostico, "+ 
															"getnumhistocli(i.codigo_paciente) AS numerohiscli, "+ 
															"i.consecutivo AS numeroingreso, "+ 
															"to_char(i.fecha_ingreso, 'DD/MM/YYYY') AS fechaingreso, "+ 
															"i.hora_ingreso AS horaingreso, "+ 
															"i.codigo_paciente AS codigopaciente, "+ 
															"c.nombre AS nomconvenio, "+ 
															"gettotalconvenioingreso(i.id, c.codigo, '"+ConstantesBD.acronimoSi+"', s.tipo, ?, ?) AS totalcuenta, "+ 
															"gettotalconvenioingreso(i.id, c.codigo, '"+ConstantesBD.acronimoNo+"', s.tipo, ?, ?) AS totalpendiente, "+
															"s.tipo AS tiposolicitud, " +
														    "UPPER (getnomtiposolicitud(s.tipo)) AS nomtiposolicitud " +
														"FROM "+
															"convenios c "+
															"INNER JOIN sub_cuentas sc ON (sc.convenio = c.codigo) "+
															"INNER JOIN ingresos i ON (i.id = sc.ingreso) "+
															"INNER JOIN cuentas cu ON (cu.id_ingreso = i.id) "+
															"INNER JOIN solicitudes s ON (s.cuenta = cu.id) "+
															"INNER JOIN centros_costo cc ON (cu.area = cc.codigo) "+ 
														"WHERE "+ 
															"i.estado = '"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"' "+
															"AND cu.estado_cuenta IN ("+ConstantesBD.codigoEstadoCuentaActiva+", "+ConstantesBD.codigoEstadoCuentaFacturadaParcial+", "+ConstantesBD.codigoEstadoCuentaAsociada+") " +
															"AND getexisteegreso(coalesce(getcuentafinalasocioint(i.id, cu.id), cu.id)) = "+ConstantesBD.valorFalseLargoString+" " +
															"AND cu.estado_cuenta NOT IN ("+ConstantesBD.codigoEstadoCuentaCerrada+") "; 
	
	/**
	 * Cadena para realizar el group by de la consulta
	 */
	private static String strGruPacientesConAtencion = "GROUP BY " +
															"sc.convenio, " +
															"cu.id, " +
															"i.codigo_paciente, " +
															"cu.via_ingreso, " +
															"i.consecutivo, " +
															"i.fecha_ingreso, " +
															"i.hora_ingreso, " +
															"i.id, " +
															"c.nombre, " +
															"c.codigo, " +
															"s.tipo " +
														"ORDER BY " +
															"nomtiposolicitud, numeroingreso";
	
	/**
	 * Metodo que consulta los Pacientes con Atencion
	 * por convenio y por valor
	 * @param con
	 * @param forma
	 * @return
	 */
	public static HashMap consultarPacientesConAtencion(Connection con, HashMap criterios)
	{
		HashMap mapa = new HashMap<String, Object>();
        mapa.put("numRegistros","0");
        
        String consulta = strConPacientesConAtencion;
        //Filtramos la consulta por el centro de atencion. Como es requerido no se valida
		consulta += "AND cc.centro_atencion = "+criterios.get("centroAtencion")+" ";
		//Filtramos la consulta por el convenio seleccionado
		consulta += "AND sc.convenio = "+criterios.get("convenio")+" ";
		//Filtramos la consulta por la via de ingreso. Como es requerido no se valida
		consulta += "AND cu.via_ingreso = "+criterios.get("viaingreso")+" ";
		//Filtramos la consulta por el tipo de paciente. Como es requerido no se valida
		consulta += "AND cu.tipo_paciente = '"+criterios.get("tipoPaciente")+"' ";
		//Filtramos la consulta por el tipo de solicitudes escogidas en el filtro
		logger.info("===>Tipos de Solicitudes Escogidas: "+criterios.get("tipoSolicitud"));
		if(UtilidadCadena.noEsVacio(criterios.get("tipoSolicitud")+""))
			consulta += "AND s.tipo IN ("+criterios.get("tipoSolicitud")+") ";
			
		//Realizamos el Group By
		consulta += strGruPacientesConAtencion;
		try
        {
        	PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
        	ps.setInt(1, Utilidades.convertirAEntero(criterios.get("viaingreso")+""));
        	ps.setString(2, criterios.get("tipoPaciente")+"");
        	ps.setInt(3, Utilidades.convertirAEntero(criterios.get("viaingreso")+""));
        	ps.setString(4, criterios.get("tipoPaciente")+"");
        	logger.info("====>Consulta Pacientes Con Atencion: "+consulta);
            mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
        }
        catch (SQLException e)
        {
            logger.error("ERROR EJECUNTANDO LA CONSULTA DE PACIENTES CON ATENCION");
            e.printStackTrace();
        }
        return (HashMap)mapa.clone();
	}

}