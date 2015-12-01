package com.princetonsa.dao.sqlbase.facturacion;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.ValoresPorDefecto;

/**
 * @author Mauricio Jllo.
 * Fecha: Mayo de 2008
 */

public class SqlBaseConsumosFacturadosDao
{

	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseConsumosFacturadosDao.class); 
	
	/**
	 * Cadena SELECT que consulta los consumos facturados
	 */
	private static String strConConsumosFacturados = "SELECT "+
												 		"fac.codigo AS codigofactura, "+
												 		"fac.consecutivo_factura AS consecutivofactura, "+
												 		"getconsecutivoingreso(cu.id_ingreso) AS consecutivoingreso, "+
												 		"to_char(fac.fecha, 'DD/MM/YYYY') AS fechafactura, "+
												 		"fac.hora||'' AS horafactura, "+
												 		"getidpaciente(fac.cod_paciente) AS idpaciente, "+
												 		"getnombrepersona(fac.cod_paciente) AS nombrepaciente, "+
												 		"getnumhistocli(fac.cod_paciente) AS numerohiscli, "+
												 		"fac.valor_total AS valorfactura, " +
												 		"getnombreconvenio(fac.convenio) as nombreconvenio "+
													 "FROM "+
													 	"facturas fac "+
													 	"INNER JOIN cuentas cu ON (fac.cuenta = cu.id) "+
													 	"INNER JOIN ingresos i ON (cu.id_ingreso = i.id) "+
													 "WHERE "+
													 	"i.estado IN ('"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"', '"+ConstantesIntegridadDominio.acronimoEstadoCerrado+"') ";
	
	/**
	 * @param con
	 * @param vo
	 * @return
	 */
	public static HashMap generarArchivoPlano(Connection con, HashMap vo, int tipoBD)
	{
		HashMap mapa = new HashMap<String, Object>();
		mapa.put("numRegistros","0");
		
        String consulta = strConConsumosFacturados;
		
        //Validamos la consulta por el centro de atencion. Como es requerido no se valida
		consulta += "AND fac.centro_aten = "+vo.get("centroAtencion")+" ";
		
		//Validamos la consulta por las fechas inicial y final de facturas. Como es requerido no se valida
		consulta += "AND to_char(fac.fecha, 'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"' ";
		
		//Validamos la consulta por el convenio seleccionado
		if(!(vo.get("convenio")+"").equals("") && !(vo.get("convenio")+"").equals("null"))
			consulta += "AND fac.convenio = "+vo.get("convenio")+" ";
		
		//Validamos la consulta por el monto base inicial y el monto base final
		if(!(vo.get("montoInicial")+"").equals("") && !(vo.get("montoInicial")+"").equals("null") && !(vo.get("montoFinal")+"").equals("") && !(vo.get("montoFinal")+"").equals("null"))
			consulta += "AND fac.valor_total BETWEEN "+vo.get("montoInicial")+" AND "+vo.get("montoFinal")+" ";
		
		//Validamos la consulta por el tope maximo seleccionado. Se utiliza diferente para ORACLE y para POSTGRES
		if(!(vo.get("tope")+"").equals("") && !(vo.get("tope")+"").equals("null"))
		{
			switch(tipoBD)
			{
				case DaoFactory.ORACLE:
					consulta += "AND rownum = "+vo.get("tope")+" ORDER BY fac.valor_total DESC ";
				break;
				case DaoFactory.POSTGRESQL:
					consulta += "ORDER BY fac.valor_total DESC "+ValoresPorDefecto.getValorLimit1()+" "+vo.get("tope")+" ";
				break;
			}
		}
		
        try
        {
        	PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            logger.info("\n\n====>Consulta Consumos Facturados: "+consulta);
            mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
        }
        catch (SQLException e)
        {
            logger.error("ERROR EJECUTANDO LA CONSULTA DE CONSUMOS FACTURADOS");
            e.printStackTrace();
        }
        return (HashMap)mapa.clone();
	}

	/**
	 * 
	 * @param codigoCentroAtencion
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param convenioSeleccionado
	 * @param montoBaseInicial
	 * @param montoBaseFinal
	 * @param tope
	 * @return
	 */
	public static String obtenerCondiciones(String codigoCentroAtencion,
			String fechaInicial, String fechaFinal,
			String convenioSeleccionado, String montoBaseInicial,
			String montoBaseFinal, String tope) 
	{
		//Validamos la consulta por los estados de ingresos requeridos
        String condiciones = "i.estado IN ('"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"', '"+ConstantesIntegridadDominio.acronimoEstadoCerrado+"') ";
        //Filtramos la consulta por el centro de atencion. Requerido
        condiciones += "AND fac.centro_aten = "+codigoCentroAtencion+" ";
        //Filtramos la consulta por la fecha de factura. Requerido
        condiciones += "AND to_char(fac.fecha,'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"' ";
        //Filtramos la consulta por el convenio seleccionado. Como no es requerido se debe validar
        if(!convenioSeleccionado.equals("") && !convenioSeleccionado.equals("null"))
        	condiciones += "AND fac.convenio = "+convenioSeleccionado+" ";
        //Filtramos la consulta por el monto base inicial y el monto base final. Como no es requerido se debe validar
        if(!montoBaseInicial.equals("") && !montoBaseInicial.equals("null") && !montoBaseFinal.equals("") && !montoBaseFinal.equals("null"))
        	condiciones += "AND fac.valor_total BETWEEN "+montoBaseInicial+" AND "+montoBaseFinal+" ";
        
        /*Agregamos a la consulta el ordenamiento Si la consulta es por un convenio especifico,
         * sino no se le agrega el ordenamiento ya que esta registrada en el reporte*/
        //if(UtilidadCadena.noEsVacio(forma.getConvenioSeleccionado()))
         condiciones += "ORDER BY fac.valor_total DESC ";
        
        //Filtramos la consulta por el tope. Como no es requerido se debe validar
        if(!tope.equals("") && tope.equals("null"))
        	condiciones += ValoresPorDefecto.getValorLimit1()+" "+tope+" ";
        
        return condiciones;
	}
	
	

}