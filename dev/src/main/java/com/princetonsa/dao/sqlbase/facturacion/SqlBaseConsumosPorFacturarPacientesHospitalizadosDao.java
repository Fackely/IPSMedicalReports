package com.princetonsa.dao.sqlbase.facturacion;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * @author Juan Alejandro Cardona
 * Fecha: Septiembre de 2008
 */

public class SqlBaseConsumosPorFacturarPacientesHospitalizadosDao {

	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseConsumosPorFacturarPacientesHospitalizadosDao.class); 
	
	/**
	 * Cadena SELECT que consulta los consumos Por facturar Pacientes Hospitalizados
	 */
	private static String strConConsumosPorFacturarPacientesHospitalizados = "SELECT "+
																				"i.consecutivo AS numingreso, "+ 
																				"to_char(i.fecha_ingreso, 'DD/MM/YYYY') AS fechaingreso, "+ 
																				"getidpaciente(i.codigo_paciente) AS idpaciente, "+ 
																				"getnombrepersona(i.codigo_paciente) AS nompersona, "+ 
																				"getcamacuenta(c.id, c.via_ingreso) AS cama, "+ 
																				"coalesce(sum(dt.valor_total_cargado), 0) AS valorconsumos, "+
																				"dt.convenio AS codconvenio, "+
																				"getnombreconvenio(dt.convenio) AS nomconvenio "+
																			"FROM "+ 
																				"det_cargos dt "+ 
																				"INNER JOIN solicitudes s ON (dt.solicitud = s.numero_solicitud) "+ 
																				"INNER JOIN cuentas c ON (s.cuenta = c.id) "+ 
																				"INNER JOIN ingresos i ON (c.id_ingreso = i.id) "+ 
																			"WHERE ";
	
	/**
	 * @param con
	 * @param vo
	 * @return
	 */
	public static HashMap generarArchivoPlano(Connection con, HashMap criterios, int tipoBD)
	{
		HashMap mapa = new HashMap<String, Object>();
		mapa.put("numRegistros","0");
		HashMap temporal = new HashMap<String, Object>();
		temporal = consultarCondicionesConsumosPacientesHospitalizados(con, criterios, tipoBD);
        try
        {
        	PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(temporal.get("consulta")+"",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            logger.info("====>Consulta Consumos Por Facturar Pacientes Hospitalizados: " +temporal.get("consulta"));
            mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, false);
        }
        catch (SQLException e)
        { 
            logger.error("ERROR EJECUTANDO LA CONSULTA DE CONSUMOS POR FACTURAR PACIENTES HOSPITALIZADOS.");
            e.printStackTrace();
        }
        return mapa;
	}
	
	/**
	 * Metodo que retorna en una llave del mapa las condiciones para la consulta
	 * y en otra llave los parametros de búsqueda impresos
	 * @param con
	 * @param criterios
	 * @return
	 */
	public static HashMap consultarCondicionesConsumosPacientesHospitalizados(Connection con, HashMap criterios, int tipoBD)
	{
		HashMap mapa = new HashMap<String, Object>();
		String parametros = "", condiciones = "", consulta = "";
		
		consulta = strConConsumosPorFacturarPacientesHospitalizados;
		
		//Condiciones Iniciales de la Funcionalidad
		condiciones = "dt.facturado = '"+ConstantesBD.acronimoNo+"' AND dt.paquetizado = '"+ConstantesBD.acronimoNo+"' AND c.via_ingreso = "+ConstantesBD.codigoViaIngresoHospitalizacion+" AND tipo_paciente = 'H' AND getexisteegreso(c.id) ="+ValoresPorDefecto.getValorFalseParaConsultas()+" ";
		
		//*******************INICIO VALIDACIONES GENERALES************************
		parametros = "Centro Atención ["+Utilidades.obtenerNombreCentroAtencion(con, Utilidades.convertirAEntero(criterios.get("centroAtencion")+""))+"], ";
		condiciones += "AND i.centro_atencion = "+criterios.get("centroAtencion")+" ";
		parametros += "Periodo ["+criterios.get("fechaInicial")+" - "+criterios.get("fechaFinal")+"], ";
		condiciones += "AND i.fecha_ingreso BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaInicial")+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaFinal")+"")+"' ";

		//Como el convenio no es requerido se hace necesario validarlo si viene vacio o lleno
		if(UtilidadCadena.noEsVacio(criterios.get("convenio")+""))
		{
			parametros += "Convenio ["+Utilidades.obtenerNombreConvenioOriginal(con, Utilidades.convertirAEntero(criterios.get("convenio")+""))+"], ";
			condiciones += "AND dt.convenio = "+criterios.get("convenio")+" ";
		}
		else
			parametros += "Convenio [Todos], ";

		//Como el Monto Base Inicial y Final no son requeridos se hace necesario validarlos si vienen vacios
		if(UtilidadCadena.noEsVacio(criterios.get("montoInicial")+""))
		{
			parametros += "Rango de Montos ["+criterios.get("montoInicial")+" - "+criterios.get("montoFinal")+"]";
			condiciones += "AND dt.valor_total_cargado BETWEEN "+criterios.get("montoInicial")+" AND "+criterios.get("montoFinal")+" ";
		}
		else
			parametros += "Rango de Montos [Todos]";

		
		
		//Validamos la consulta por el tope maximo seleccionado. Se utiliza diferente para ORACLE y para POSTGRES
		if(UtilidadCadena.noEsVacio(criterios.get("tope")+""))
		{
			switch(tipoBD)
			{
				case DaoFactory.ORACLE:
					condiciones += "AND rownum = "+criterios.get("tope")+" GROUP BY i.consecutivo, i.fecha_ingreso, i.codigo_paciente, c.id, c.via_ingreso, dt.convenio ORDER BY dt.convenio asc, valorconsumos DESC, nomconvenio asc, i.consecutivo ";
				break;
				case DaoFactory.POSTGRESQL:
					condiciones += "GROUP BY i.consecutivo, i.fecha_ingreso, i.codigo_paciente, c.id, c.via_ingreso, dt.convenio ORDER BY nomconvenio, dt.convenio::text::int asc, valorconsumos DESC, i.consecutivo::text::int "+ValoresPorDefecto.getValorLimit1()+" "+criterios.get("tope")+" ";
				break;
			}
			parametros += " Tope [" +criterios.get("tope")+ "]";
		}
		else
		{
			condiciones += "GROUP BY i.consecutivo, i.fecha_ingreso, i.codigo_paciente, c.id, c.via_ingreso, dt.convenio ORDER BY nomconvenio, dt.convenio::text::int, i.consecutivo::text::int ";
		}
		//**********************FIN VALIDACIONES GENERALES************************

		//Concatenamos la consulta con las condiciones
		consulta += condiciones;
		
		mapa.put("parametros", parametros);
		mapa.put("consulta", consulta);
		
		return mapa;
	}
}