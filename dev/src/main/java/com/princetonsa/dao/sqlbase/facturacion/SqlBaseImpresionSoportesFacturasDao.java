package com.princetonsa.dao.sqlbase.facturacion;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import org.apache.log4j.Logger;
import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;

public class SqlBaseImpresionSoportesFacturasDao{
	
	/**
	 * Objeto para manejar log de la clase  
	 * */
	private static Logger logger = Logger.getLogger(SqlBaseSoportesFacturasDao.class);
	
	/**
	 * Cadena para consultar las facturas en estado facturado
	 */
	private static String strResultadoConsultarImprimir = "SELECT " +
																"f.codigo AS codfactura, " +
																"f.consecutivo_factura AS consecutivo, " +
																"f.fecha, " +
																"f.hora, " +
																"f.via_ingreso AS codviaingreso, " +
																"f.convenio AS codconvenio, " +
																"f.cod_paciente AS codpaciente, " +
																"f.cuenta AS idcuenta, " +
																"f.sub_cuenta AS subcuenta, " +
																"f.sub_cuenta as idsubcuenta, " +
																"getnombreconvenio(f.convenio) AS nomconvenio, " +
																"vi.nombre AS nomviaingreso, " +
																"c.id_ingreso AS idingreso, " +
																"c.tipo_paciente AS tipopaciente " +
															"FROM " +
																"facturas f " +
															"INNER JOIN " +
																"vias_ingreso vi ON (vi.codigo = f.via_ingreso) " +
															"INNER JOIN " +
																"cuentas c ON (c.id = f.cuenta) " +
															"WHERE ";
	
	/**
	 * Listar las facturas en estado facturado
	 * @param con
	 * @param mapaOrganizado
	 * @param codigoInstitucionInt
	 * @param loginUsuario
	 * @return
	 */
	public static HashMap listarImprimir(Connection con, HashMap<String, Object> filtros, int codigoInstitucionInt, String loginUsuario) {
		
		HashMap listadoFacturas = new HashMap();
		listadoFacturas.put("numRegistros", 0);
		String cadena = strResultadoConsultarImprimir;
		
		cadena += "f.institucion="+codigoInstitucionInt+" ";
		
		cadena += "AND f.estado_facturacion="+ConstantesBD.codigoEstadoFacturacionFacturada+" ";
		
		if (!filtros.get("viaIngreso").toString().equals(""))
			cadena+="AND f.via_ingreso="+filtros.get("viaIngreso")+" ";
				
		if (!filtros.get("convenio").toString().equals(""))
			cadena+="AND f.convenio="+filtros.get("convenio")+" ";
		
		if (!filtros.get("facturaInicial").toString().equals("")||!filtros.get("facturaFinal").toString().equals(""))
			cadena+="AND f.consecutivo_factura BETWEEN "+filtros.get("facturaInicial")+" AND "+filtros.get("facturaFinal")+" ";
		
		if(!filtros.get("fechaInicial").toString().equals("")||!filtros.get("fechaFinal").toString().equals(""))
			cadena+="AND f.fecha BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaInicial")+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaFinal")+"")+"' ";
		
		cadena += "ORDER BY ";
		
		if (!filtros.get("convenio").toString().equals(""))
			cadena += "consecutivo";
		else
			cadena += "nomconvenio, consecutivo";
		
		logger.info("listarFacturas / "+cadena);
		
		try {
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			listadoFacturas = UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(cadena)));
		}
		catch (SQLException e) {
			logger.warn("ERROR / guardarEncabezado / "+e);
		}
		
		return listadoFacturas;
	}		
}