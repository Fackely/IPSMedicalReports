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
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * @author Mauricio Jllo
 * Fecha: Mayo de 2008
 */

public class SqlBasePacientesHospitalizadosDao
{

	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger = Logger.getLogger(SqlBasePacientesHospitalizadosDao.class);
	
	/**
	 * Cadena para consultar los centros de costo por tipo paciente y centro de atencion
	 */
	private static String strConCentroCosto = "SELECT "+
													"cc.codigo AS codigo, "+
													"cc.nombre As nomcentrocosto, "+
													"cc.tipo_area AS codigotipoarea, "+
													"cc.centro_atencion AS codcentroatencion, "+
													"getnomcentroatencion(cc.centro_atencion) AS nomcentroatencion "+
												"FROM "+
													"centros_costo cc "+
													"INNER JOIN centro_costo_via_ingreso ccvi ON (cc.codigo = ccvi.centro_costo) "+
													"INNER JOIN tip_pac_via_ingreso tpvi ON (tpvi.via_ingreso = ccvi.via_ingreso) "+
												"WHERE "+
													"cc.institucion = ? "+
													"AND cc.centro_atencion = ? ";
	
	/**
	 * Cadena para consultar los pacientes hospitalizados
	 */
	//"getFechaNacimientoPaciente(i.codigo_paciente) AS fechanacimiento, "+

	private static String strConPacientesHospitalizados = "SELECT "+
																"cu.id AS numerocuenta, "+
																"i.id AS ingreso, "+
																"i.consecutivo AS numeroingreso, "+
																"to_char(i.fecha_ingreso, 'DD/MM/YYYY') AS fechaingreso, "+
																"i.hora_ingreso AS horaingreso, "+
																"i.codigo_paciente AS codigopaciente, "+
																"getnombrepersona(i.codigo_paciente) AS nombrepaciente, "+
																"getidpaciente(i.codigo_paciente) AS idpaciente, "+
																"getdescripcionsexo(getsexopaciente(i.codigo_paciente)) AS sexopaciente, "+
																"getFechaNacimientoPaciente(i.codigo_paciente) AS fechanacimiento, "+
																"getnomcentrocosto(cu.area) AS centrocosto, "+
																"to_char(ah.fecha_admision, 'DD/MM/YYYY') AS fechacentrocosto, "+
																"ah.hora_admision AS horacentrocosto, "+
																"1 AS diagnostico, "+//"getultdiagpac(i.id) AS diagnostico, "+
																"getnombreconvenioxingreso(cu.id_ingreso) AS convenio, "+
																"getcamacuenta(cu.id, cu.via_ingreso) AS numerocama, " +
																"CASE WHEN (getexisteegreso(cu.id) = "+ValoresPorDefecto.getValorTrueParaConsultas()+" OR (manejopaciente.getExisteEgresoMedico(cu.id) = '"+ConstantesBD.acronimoSi+"')) THEN 'Con Egreso' ELSE 'Sin Egreso' END AS egreso "+ 
															"FROM "+
																"cuentas cu "+
																"INNER JOIN ingresos i ON (cu.id_ingreso = i.id) "+
																"INNER JOIN admisiones_hospi ah ON (cu.id = ah.cuenta) "+	
																"INNER JOIN centros_costo cc ON (cu.area = cc.codigo) "+
																"LEFT OUTER JOIN camas1 cam ON (ah.cama = cam.codigo) "+
																"LEFT OUTER JOIN habitaciones hab ON (cam.habitacion = hab.codigo) "+
															"WHERE "+
																"cu.via_ingreso = "+ConstantesBD.codigoViaIngresoHospitalizacion+" "+
																"AND i.estado = '"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"' "+
																"AND cu.estado_cuenta IN ("+ConstantesBD.codigoEstadoCuentaActiva+", "+ConstantesBD.codigoEstadoCuentaFacturadaParcial+", "+ConstantesBD.codigoEstadoCuentaAsociada+") ";
	
	private static String strOrdConPacientesHospitalizados = "ORDER BY nombrepaciente";
	
	/**
	 * Metodo que consulta los Pacientes Hospitalizados
	 * @param con
	 * @param vo
	 * @return
	 */
	public static HashMap consultarPacientesHospitalizados(Connection con, HashMap vo)
	{
		HashMap mapa = new HashMap<String, Object>();
        mapa.put("numRegistros","0");
        
        String consulta = strConPacientesHospitalizados;
        
        //Filtramos la consulta por el centro de atencion. Como es requerido no se valida
		consulta += "AND cc.centro_atencion = "+Utilidades.convertirAEntero(vo.get("centroAtencion")+"")+" ";
		
		//Validamos la consulta por el tipo de paciente. Necesario validar
		if(!vo.get("tipoPaciente").equals("") && !vo.get("tipoPaciente").equals("null"))
		{
			if(vo.get("tipoPaciente").equals(ConstantesIntegridadDominio.acronimoAmbos))
				consulta += "AND cu.tipo_paciente IN ('"+ConstantesBD.tipoPacienteHospitalizado+"', '"+ConstantesBD.tipoPacienteCirugiaAmbulatoria+"') ";
			else
				consulta += "AND cu.tipo_paciente = '"+vo.get("tipoPaciente")+"' ";
		}
		
		//Filtramos la consulta por el centro de costo. Necesario validar
		if(!vo.get("centroCosto").equals("") && !vo.get("centroCosto").equals("null"))
			consulta += "AND cu.area = "+vo.get("centroCosto")+" ";
		
		//Filtramos la consulta por el convenio seleccionado. Necesario validar
		if(!vo.get("convenio").equals("") && !vo.get("convenio").equals("null"))
			consulta += "AND "+vo.get("convenio")+" IN (SELECT convenio FROM sub_cuentas WHERE sub_cuentas.ingreso = cu.id_ingreso)  ";
		
		//Filtramos la consulta por el piso seleccionado. Necesario validar
		if(!vo.get("piso").equals("") && !vo.get("piso").equals("null"))
			consulta += "AND hab.piso = "+vo.get("piso")+" ";
		
		//Filtramos la consulta por el egreso. Como es requerido no se valida 
		if(vo.get("indicativoEgreso").equals(ConstantesIntegridadDominio.acronimoIndicativoConEgreso))
			consulta += "AND (getexisteegreso(cu.id) ="+ValoresPorDefecto.getValorTrueParaConsultas()+" OR manejopaciente.getExisteEgresoMedico(cu.id) = '"+ConstantesBD.acronimoSi+"') ";
		else if(vo.get("indicativoEgreso").equals(ConstantesIntegridadDominio.acronimoIndicativoSinEgreso))
			consulta += "AND getexisteegreso(cu.id) ="+ValoresPorDefecto.getValorFalseParaConsultas()+" AND manejopaciente.getExisteEgresoMedico(cu.id) = '"+ConstantesBD.acronimoNo+"' ";
		
		//Filtramos la consulta por el rango de Fecha de Ingreso. Como es requerido no se valida
		consulta += "AND to_char(i.fecha_ingreso,'"+ConstantesBD.formatoFechaBD+"') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"' ";
        
        try
        {
        	PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta+strOrdConPacientesHospitalizados,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            logger.info("\n\n====>Consulta Pacientes Hospitalizados: "+consulta+strOrdConPacientesHospitalizados);
            mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
        }
        catch (SQLException e)
        {
            logger.error("ERROR EJECUNTANDO LA CONSULTA DE PACIENTES HOSPITALIZADOS");
            e.printStackTrace();
        }
        return (HashMap)mapa.clone();
	}

	
	/**
	 * Metodo que carga los centros de costo segun el
	 * tipo de paciente y el centro de atencion seleccionado
	 * @param con
	 * @param institucion
	 * @param tipoArea
	 * @param tipoPaciente
	 * @param codigoCentroAtencion
	 * @return
	 */
	public static HashMap obtenerCentrosCosto(Connection con, int institucion, String tipoArea, String tipoPaciente, String codigoCentroAtencion)
	{
		HashMap mapa = new HashMap<String, Object>();
        mapa.put("numRegistros","0");
        String consulta = strConCentroCosto;
        
        //Validamos la consulta por el tipo de paciente. Necesario validar
		if(!tipoPaciente.equals("") && !tipoPaciente.equals("null"))
		{
			if(tipoPaciente.equals(ConstantesIntegridadDominio.acronimoAmbos))
			{
				consulta += "AND tpvi.tipo_paciente IN ('"+ConstantesBD.tipoPacienteHospitalizado+"', '"+ConstantesBD.tipoPacienteCirugiaAmbulatoria+"') ";
				//consulta += "GROUP BY cc.codigo, cc.nombre, cc.tipo_area, cc.centro_atencion ";
			}
			else
				consulta += "AND tpvi.tipo_paciente = '"+tipoPaciente+"' ";
		}
		
		//Adicionamos el Group By para que no repita los Centros de Costo
		consulta += "GROUP BY cc.codigo, cc.nombre, cc.tipo_area, cc.centro_atencion ";
		
        try
        {
        	PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
        	ps.setInt(1, institucion);
        	ps.setInt(2, Utilidades.convertirAEntero(codigoCentroAtencion));
            logger.info("\n\n====>Consulta Centros de Costo: "+consulta);
            logger.info("====>Tipo Paciente: "+tipoPaciente+" Institucion: "+institucion+" Codigo Centro Atencion: "+codigoCentroAtencion);
            mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
        }
        catch (SQLException e)
        {
            logger.error("ERROR EJECUNTANDO LA CONSULTA DE PACIENTES HOSPITALIZADOS");
            e.printStackTrace();
        }
        return (HashMap)mapa.clone();
	}
	
}