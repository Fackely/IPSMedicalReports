package com.princetonsa.dao.sqlbase.facturacion;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import org.apache.log4j.Logger;
import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

public class SqlBaseSoportesFacturasDao{
	
	/**
	 * Objeto para manejar log de la clase  
	 * */
	private static Logger logger = Logger.getLogger(SqlBaseSoportesFacturasDao.class);

	private static String strObtenerTiposSoporte = "SELECT " +
														"tsf.codigo AS codigoTipoSoporte, " +
														"tsf.nombre, " +
														"ptsf.codigo as codigoPadre, " +
														"ptsf.descripcion as descPadre " +
													"FROM " +
														"tipos_soporte_fact tsf " +
													"LEFT OUTER JOIN " +
														"padres_tipo_sopor_fact ptsf ON (ptsf.codigo = tsf.codigo_padre) ";
	
	
	private static String strInsertarSoportesFacturas = "INSERT INTO " + 
															"soporte_facturas (codigo, via_ingreso, convenio, fecha_modifica, hora_modifica, usuario_modifica, institucion) " +
														"VALUES "+ 
															"(?,?,?,CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+",?,?)";
																										
	private static String strInsertarDetalleSoportesFacturas = "INSERT INTO "+
																	"det_soportes_fact (soporte_factura, tipo_soporte) "+
																"VALUES " +
																	"(?,?)";
	
	private static String strActualizarSoportesFacturas = 	"UPDATE " +
																"soporte_facturas " +
															"SET " +
																"fecha_modifica=CURRENT_DATE, " +
																"hora_modifica="+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
																"usuario_modifica=? " +
															"WHERE " +
																"codigo=? ";
	
	private static String strEliminarDetSoporteFact = "DELETE FROM det_soportes_fact WHERE soporte_factura=?";
	
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static HashMap obtenerTiposSoporte(Connection con){
		
		HashMap mapa = new HashMap();
		mapa.put("numRegistros", 0);
		try
		{
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			logger.info("obtenerTiposSoporte / "+strObtenerTiposSoporte);
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(strObtenerTiposSoporte)));
		}
		catch (SQLException e)
		{
			logger.info("ERROR! / obtenerTiposSoporte / "+e);
		}
		return mapa;
	}

	/**
	 *
	 * @param con
	 * @param mapaOrganizado
	 * @param codigoInstitucionInt
	 * @param loginUsuario
	 * @return
	 */
	public static boolean guardar(Connection con, HashMap<String, Object> mapaOrganizado, int codigoInstitucionInt, String loginUsuario) {
		
		boolean transaccionExitosa = false;
		double seqSoporteFacturas = ConstantesBD.codigoNuncaValido;
		boolean insertarSoporte = false;
		
		// Se abre una transacción
		UtilidadBD.iniciarTransaccion(con);
		
		if(!mapaOrganizado.containsKey("modificar")){
		
			seqSoporteFacturas = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_soporte_facturas");
			
			try {
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(strInsertarSoportesFacturas,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setDouble(1, seqSoporteFacturas);
				
				ps.setInt(2, Utilidades.convertirAEntero(mapaOrganizado.get("viaIngreso").toString()));
				if(!mapaOrganizado.get("convenio").toString().equals(""))
					ps.setInt(3, Utilidades.convertirAEntero(mapaOrganizado.get("convenio").toString()));
				else
					ps.setNull(3, java.sql.Types.INTEGER);			
				ps.setString(4, loginUsuario);
				ps.setInt(5, codigoInstitucionInt);
				
				logger.info("guardarEncabezado / "+strInsertarSoportesFacturas);
				logger.info("Param 1 - "+seqSoporteFacturas);
				
				if(ps.executeUpdate()>0)
					transaccionExitosa = true;
			} 
			catch (SQLException e) {
				transaccionExitosa = false;
				logger.warn("ERROR / guardarEncabezado / "+e);
			}
			
		} else {
			try {
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(strActualizarSoportesFacturas,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setString(1, loginUsuario);
				ps.setDouble(2, Utilidades.convertirADouble(mapaOrganizado.get("modificar").toString()));
			
				if(ps.executeUpdate()>0)
					transaccionExitosa = true;
				
			} catch (SQLException e) {
				transaccionExitosa = false;
				logger.warn("ERROR / actualizarEncabezado / "+e);
			}
			
			try {
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(strEliminarDetSoporteFact,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setDouble(1, Utilidades.convertirADouble(mapaOrganizado.get("modificar").toString()));
			
				if(ps.executeUpdate()>0)
					transaccionExitosa = true;
				
			} catch (SQLException e) {
				transaccionExitosa = false;
				logger.warn("ERROR / eliminandoDetalle / "+e);
			}
			seqSoporteFacturas = Utilidades.convertirADouble(mapaOrganizado.get("modificar").toString());
		}
		
		
		
		// Iteramnos el mapa para insertar cada tipo de soporte seleccionado
		for(int i=0; i<Utilidades.convertirAEntero(mapaOrganizado.get("numRegistros").toString()); i++){
			insertarSoporte=false;
			
			// Se validad el tipo de soporte activo que no sea hijo
    		if(UtilidadTexto.getBoolean(mapaOrganizado.get("activo_"+i).toString()) 
    				&& UtilidadTexto.isEmpty(mapaOrganizado.get("codpadre_"+i).toString()))
    			insertarSoporte=true;
			
    		// Se validad el tipo de soporte activo hijo
    		if(!UtilidadTexto.isEmpty(mapaOrganizado.get("codpadre_"+i).toString())
    					&& UtilidadTexto.getBoolean(mapaOrganizado.get("mostraropcioneshijas_"+mapaOrganizado.get("codpadre_"+i))+""))
    			insertarSoporte=true;
			
			if(insertarSoporte){
				try {
					PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(strInsertarDetalleSoportesFacturas,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					pst.setDouble(1, seqSoporteFacturas);
					pst.setInt(2, Utilidades.convertirAEntero(mapaOrganizado.get("codtiposoporte_"+i).toString()));
					
					logger.info("guardarDetalle / "+strInsertarDetalleSoportesFacturas);
					logger.info("Param 1 - "+seqSoporteFacturas);
					logger.info("Param 2 - "+mapaOrganizado.get("codtiposoporte_"+i));
					
					if(pst.executeUpdate()>0)
						transaccionExitosa = true;
				} 
				catch (SQLException e) {
					transaccionExitosa = false;
					logger.warn("ERROR / guardarDetalle / "+e);
				}
			}
			
		}
		
		if(transaccionExitosa)
			UtilidadBD.finalizarTransaccion(con);
		else
			UtilidadBD.abortarTransaccion(con);
		
		return transaccionExitosa;
	}

	/**
	 * Método para consultar
	 * @param con
	 * @param soportesFacturasMap
	 * @param codigoInstitucionInt
	 * @return
	 */
	public static HashMap<String, Object> consultar(Connection con, HashMap<String, Object> tiposSoporteMap, HashMap<String, Object> soportesFacturasMap, int codigoInstitucionInt) {
		HashMap tiposSoporteActual = new HashMap();
		tiposSoporteActual.put("numRegistros", 0);
		
		// Inactivar todos los tipos de soportes
		soportesFacturasMap = inactivarTiposSoporte(soportesFacturasMap, Utilidades.convertirAEntero(tiposSoporteMap.get("numRegistros").toString()));
		
		String cadenaSql = "SELECT " +
								"sf.codigo as codigo, " +
								"dsf.tipo_soporte as tiposoporte, " +
								"tsf.nombre as nomtiposoporte, " +
								"ptsf.codigo as codigopadre," +
								"ptsf.descripcion as descpadre " +
							"FROM " +
								"soporte_facturas sf " +
							"INNER JOIN " +
								"det_soportes_fact dsf ON (dsf.soporte_factura = sf.codigo) " +
							"INNER JOIN " +
								"tipos_soporte_fact tsf ON (tsf.codigo = dsf.tipo_soporte) " +
							"LEFT OUTER JOIN " +
								"padres_tipo_sopor_fact ptsf ON (ptsf.codigo = tsf.codigo_padre) " +
							"WHERE " +
								"sf.via_ingreso = "+soportesFacturasMap.get("viaIngreso")+" " +
								"AND sf.institucion="+codigoInstitucionInt+" ";
		
		if(!soportesFacturasMap.get("convenio").toString().equals(""))	
			cadenaSql += "AND sf.convenio = "+soportesFacturasMap.get("convenio");
		else
			cadenaSql += "AND sf.convenio IS NULL ";
		
		logger.info("consultar / "+cadenaSql);
		
		try {
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			tiposSoporteActual = UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(cadenaSql)));
		
		} catch (SQLException e) {
			logger.warn("ERROR / consultar / "+e);
		}
		
		for(int i=0; i<Utilidades.convertirAEntero(tiposSoporteActual.get("numRegistros").toString()); i++){
			for(int x=0; x<Utilidades.convertirAEntero(tiposSoporteMap.get("numRegistros").toString()); x++){
				if(tiposSoporteActual.get("tiposoporte_"+i).toString().equals(tiposSoporteMap.get("codigotiposoporte_"+x).toString())){
					if (tiposSoporteMap.get("descpadre_"+x).equals("")){
						soportesFacturasMap.put("tipoSoporteActivo_"+x, "true");
						soportesFacturasMap.put("nomTipoSoporte_"+x, tiposSoporteActual.get("nomtiposoporte_"+i));
						soportesFacturasMap.put("descPadre_"+x, tiposSoporteActual.get("descpadre_"+i));
					}	
					else {
						if(tiposSoporteActual.get("codigopadre_"+i).toString().equals(soportesFacturasMap.get("codigoPadre_"+x).toString()) && soportesFacturasMap.containsKey("mostrarOpciones_"+x)){
							soportesFacturasMap.put("mostrarOpciones_"+x, "true");
							soportesFacturasMap.put("tipoSoporteHijo_"+x, tiposSoporteActual.get("tiposoporte_"+i));
							soportesFacturasMap.put("nomTipoSoporte_"+x, tiposSoporteActual.get("nomtiposoporte_"+i));
							soportesFacturasMap.put("descPadre_"+x, tiposSoporteActual.get("descpadre_"+i));
						}
					}
				}	
			}
		}
		
		soportesFacturasMap.put("numRegistros", tiposSoporteActual.get("numRegistros"));
		
		if(Utilidades.convertirAEntero(tiposSoporteActual.get("numRegistros").toString())>0)
			soportesFacturasMap.put("modificar", tiposSoporteActual.get("codigo_0"));
		
		return soportesFacturasMap;
	}

	/**
	 * 
	 * @param soportesFacturasMap
	 * @param numRegistros
	 * @return
	 */
	private static HashMap<String, Object> inactivarTiposSoporte(HashMap<String, Object> soportesFacturasMap, int numRegistros) {
		for(int i=0; i<numRegistros; i++){
			if(soportesFacturasMap.containsKey("tipoSoporteActivo_"+i))
				soportesFacturasMap.put("tipoSoporteActivo_"+i, "");	
			if (soportesFacturasMap.containsKey("mostrarOpciones_"+i)){
				soportesFacturasMap.put("mostrarOpciones_"+i, "");
				soportesFacturasMap.put("tipoSoporteHijo_"+i, "");
			}
		}
		return soportesFacturasMap;
	}

	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param viaIngreso
	 * @param convenio
	 * @return
	 */
	public static HashMap<String, Object> obtenerTiposSoporteXConvenio(Connection con, int institucion, int viaIngreso, int convenio) {
		HashMap tiposSoporte = new HashMap();
		tiposSoporte.put("numRegistros", 0);
		String cadenaSql="";
		ResultSetDecorator rs;
		String condicionConvenio="";
		int codigoSoporteFacturas = ConstantesBD.codigoNuncaValido;
		
		// Consultar si existe parametrización para el convenio
		cadenaSql = "SELECT codigo FROM soporte_facturas WHERE via_ingreso = "+viaIngreso+" AND convenio="+convenio+" AND institucion="+institucion+" ";
		logger.info("Consulta si existe parametrizacion para el convenio / "+cadenaSql);
		try {
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			rs = new ResultSetDecorator(st.executeQuery(cadenaSql));
			
			if(rs.next()){
				condicionConvenio += "sf.convenio="+convenio+" ";
				codigoSoporteFacturas = rs.getInt("codigo");
			} else
				condicionConvenio += "sf.convenio IS NULL ";
			
			
			// Cadena para consultar la parametrización de los soportes de facturas
			cadenaSql = "SELECT " +
							"g.activo, " +
							"g.codtiposoporte, " +
							"g.nomtiposoporte, " +
							"g.codpadre, " +
							"g.descpadre " +
						"FROM " +
							"(SELECT " +
								"'true' AS activo, " +
								"tsf.codigo AS codtiposoporte, " +
								"tsf.nombre AS nomtiposoporte, " +
								"ptsf.codigo AS codpadre, " +
								"ptsf.descripcion AS descpadre " +
							"FROM " +
								"soporte_facturas sf " +
							"INNER  JOIN " +
								"det_soportes_fact dsf ON (dsf.soporte_factura = sf.codigo) " +
							"INNER JOIN " +
								"tipos_soporte_fact tsf ON (tsf.codigo = dsf.tipo_soporte) " +
							"LEFT OUTER JOIN " +
								"padres_tipo_sopor_fact ptsf ON (ptsf.codigo = tsf.codigo_padre) " +
							"WHERE " +
								"sf.institucion=" + institucion+" "+ 
								"AND sf.via_ingreso="+viaIngreso+" " +
								"AND "+condicionConvenio+
						"UNION " +
							"SELECT " +
								"'false'  AS activo, " +
								"tsf.codigo as codtiposoporte, " +
								"tsf.nombre as nomtiposoporte, " +
								"ptsf.codigo as codpadre, " +
								"ptsf.descripcion as descpadre " +
							"FROM " +
								"tipos_soporte_fact tsf " +
							"LEFT OUTER JOIN " +
								"padres_tipo_sopor_fact ptsf ON (ptsf.codigo = tsf.codigo_padre) " +
							"WHERE " +
								"tsf.codigo  NOT IN ( SELECT dsf.tipo_soporte " +
														"FROM soporte_facturas sf " +
														"INNER JOIN det_soportes_fact dsf ON (dsf.soporte_factura = sf.codigo) " +
														"WHERE sf.institucion="+institucion+" AND sf.via_ingreso = "+viaIngreso+" AND "+condicionConvenio+" )) g " +
							"ORDER BY " +
								"codtiposoporte";
			
			
			
		} catch (SQLException e) {
			logger.info("ERROR / obtenerTiposSoporteXConvenio / "+e);
		}
		
		// Consultar la parametrización de los soportes de facturas
		logger.info("Consulta la parametrización de los soportes de facturas / "+cadenaSql);
		try {
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			tiposSoporte = UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(cadenaSql)));
		} catch (SQLException e) {
			logger.info("ERROR / obtenerTiposSoporteXConvenio / "+e);
		}
		
		if (codigoSoporteFacturas!=ConstantesBD.codigoNuncaValido)
			tiposSoporte.put("modificar", codigoSoporteFacturas);
		
		return tiposSoporte;
	}
}