package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;


/**
 * 
 * @author Juan Sebastian Castaño
 * 
 * Clase SQLBASE de la funcionalidad de Busqueda generica de terceros
 *
 */
public class SqlBaseBusquedaTercerosGenericaDao {

	
	/**	 * log de control de errors y warns de la clase	 */
	private static Logger logger = Logger.getLogger(SqlBaseBusquedaTercerosGenericaDao.class);
	
	/**	 * indices de resultados a guardar en el mapa de retorno de resultados.	 */
	public static final String [] indicesTerceros = {"codigoTercero_","numeroIdTercero_","descripcionTercero_","activoTercero_", "tipoTercero_","descTipoTercero_"};
	
	public static final String consultaTerceros = "SELECT" +
														" t.codigo AS codigo_tercero," +
														" t.numero_identificacion AS numero_id_tercero," +
														" t.descripcion AS descripcion_tercero," +
														" t.activo AS activo_tercero," +
														" t.tipo_tercero As tipo_tercero," +
														" getdesctipodeudor (t.tipo_tercero) As desc_tipo_tercero " +
													" FROM" +
														" terceros t " ;
												
							
	// estaba sin el order by - alejo
	public static final String consultarTipostercero = "SELECT  codigo As codigo, descripcion As descripcion FROM tipo_tercero ORDER BY descripcion ";
	
		
	/**
	 * Metodo de consulta de terceros
	 * @param con
	 * @param institucion
	 * @param nit
	 * @param descripcionTercero
	 * @return
	 */
	public static HashMap<String, Object> consultarTerceros (Connection con, int institucion, String nit, String descripcionTercero, String  filtrarXEstadoActivo, String filtrarXRelacionEmpresa, String filtrarXDeudor, String tipoTercero)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>() ;
		PreparedStatementDecorator pst = null;	
		
		String consulta = consultaTerceros+" WHERE 1=1  " ;  //" descripcion like('% " + descripcionTercero + " %') and " +	" numero_identificacion like ('% " + nit + " %') and " +
		
		
		if (!descripcionTercero.equals(""))
			consulta = consulta + " and UPPER(descripcion) like(UPPER('%" + descripcionTercero + "%'))  ";
		else
			consulta = consulta + "and descripcion like('%%')  ";
		
		if (!nit.equals(""))
			consulta = consulta + "and numero_identificacion like('%"+ nit +"%')  ";
		else
			consulta = consulta + "and numero_identificacion like('%%')  ";
		
		if (filtrarXRelacionEmpresa.equals("t"))
			consulta = consulta + "and codigo  not in (select tercero from empresas)  ";
		else if (filtrarXRelacionEmpresa.equals("i"))
			consulta = consulta + "and codigo  in (select tercero from empresas)  ";
			
		if (filtrarXDeudor.equals("t"))
			consulta = consulta + "and  codigo not in (select codigo_tercero from deudores where codigo_tercero is not null)  ";
		else if (filtrarXDeudor.equals("i"))
			consulta = consulta + "and codigo in (select codigo_tercero from deudores where codigo_tercero is not null)  ";
		
		//el que estaba - alejo
		//consulta = consulta + " institucion = ? and activo = '"+ filtrarXEstadoActivo + "'";  

		if(!UtilidadTexto.isEmpty(tipoTercero))
		{
			consulta+="and  t.tipo_tercero="+tipoTercero+"    ";
		}
		
		if(UtilidadTexto.getBoolean(filtrarXEstadoActivo))
		{	
			consulta = consulta + "and institucion = ? and activo = "+ ValoresPorDefecto.getValorTrueParaConsultas() + " ORDER BY descripcion";
		}
		else
		{
			consulta = consulta + " and  institucion = ? and activo = "+ ValoresPorDefecto.getValorFalseParaConsultas() + " ORDER BY descripcion";
		}
		
		
		
		logger.info("consulta->"+consulta+"   inst->"+institucion+"    tipoTercero->"+tipoTercero);
		//System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" + consulta  + "\n\n\n\n\n\n\n\n\n");
		//System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n des" + descripcionTercero  + " NIT: " + nit + "  Filtrar por empre : " + filtrarXRelacionEmpresa + "\n\n\n\n\n\n\n\n\n");
		
		try	{
			pst =  new PreparedStatementDecorator(con, consulta);
			//pst.setString(1, descripcionTercero);messmessageagemessmessageagePS
			//pst.setString(2, nit);
			pst.setInt(1, institucion);
			logger.info("CONSULTA TERCERO->"+pst);
			// ejecutar consulta y cargar resultados en el hashMap
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
		}
		catch (SQLException e) {
			logger.error(e+" Error en consulta de terceros en busqueda de terceros generica");
		}
		resultados.put("INDICES",indicesTerceros);
		return resultados;	
	}

	
	/**
	 * Metodo encargado de consultar los terceros
	 * pudiendo filtar por otros campos
	 * @param con
	 * @param institucion
	 * @param nit
	 * @param descripcionTercero
	 * @param filtrarXEstadoActivo
	 * @param filtrarXRelacionEmpresa
	 * @param filtrarXDeudor
	 * @param razonSocial
	 * @param tipoTercero
	 * @return
	 */
	public static HashMap consultarTercerosAvan (Connection con, int institucion, String nit, String descripcionTercero, String  filtrarXEstadoActivo, String tipoTercero,String esEmpresa)
	{
		HashMap resultados = new HashMap () ;
		
		String consulta = consultaTerceros+" INNER JOIN deudores d ON (d.codigo_tercero=t.codigo)" ; 
		
		String where ="	WHERE 1=1 ";
		
		if (esEmpresa.equals(ConstantesBD.acronimoSi))
			where += " AND  codigo  in (select tercero from empresas) ";
		else
			if (esEmpresa.equals(ConstantesBD.acronimoNo))
				where += " AND  codigo  not in (select tercero from empresas) ";
		
		if (UtilidadCadena.noEsVacio(descripcionTercero))
			where += " AND UPPER(t.descripcion) like(UPPER('%" + descripcionTercero + "%')) ";
		
		if (UtilidadCadena.noEsVacio(nit))
			where += " AND t.numero_identificacion = '"+nit+"'";
		
		if (UtilidadCadena.noEsVacio(tipoTercero))
			where += " AND t.tipo_tercero= "+tipoTercero;
		
		where +=" AND t.institucion = ? ";
		
		//consulta += where;  // el que estaba - alejo
		consulta += where + " ORDER BY t.descripcion";
		
		logger.info("\n cadena consulta de consultarTercerosAvan "+consulta);
		
		try {
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet ));
			pst.setInt(1, institucion);
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
		}
		catch (SQLException e) {
			logger.error(" Error en consulta de terceros en busqueda de terceros generica "+e);
		}
		resultados.put("INDICES",indicesTerceros);
		return resultados;	
	}
	
	/**
	 * Metodo dencargado de obtener los tipos de 
	 * terceros
	 * @param connection
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerTiposTerceros (Connection connection)
	{
		logger.info("\n entro a obtenerTiposTerceros ");
		ArrayList<HashMap<String, Object>> result = new ArrayList<HashMap<String,Object>>();
		String cadena =consultarTipostercero; 
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			while (rs.next()) {
				HashMap elemento = new HashMap ();
				elemento.put("codigo",rs.getObject("codigo"));
				elemento.put("descripcion",rs.getObject("descripcion"));
				result.add(elemento);
			}
		}
		catch (Exception e)	{
		 logger.info("\n problema obteniendo los tipos de terceros "+e);
		}
		logger.info("\n salgo a obtenerTiposTerceros "+result);
		return result;
	}
}