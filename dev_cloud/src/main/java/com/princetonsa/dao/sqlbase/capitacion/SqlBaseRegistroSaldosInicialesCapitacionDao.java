/*
 * Creado   08/08/2006
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.dao.sqlbase.capitacion;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.ValoresPorDefecto;

/**
 * Implementación sql genérico 
 * @version 1.0,
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Rios</a>
 */
public class SqlBaseRegistroSaldosInicialesCapitacionDao 
{
	 /**
	 * Variable para manejar los log de la clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseRegistroSaldosInicialesCapitacionDao.class);

	/**
	 * busqueda de las cuentas de cobro 
	 * @param con
	 * @param criteriosBusquedaMap
	 * @return
	 * @throws SQLException
	 */	
	public static HashMap busquedaCuentasCobro(	Connection con,
												HashMap criteriosBusquedaMap
											  )throws SQLException
	{
		String consultaArmada="";
		PreparedStatementDecorator ps=  null;
		ResultSetDecorator rs= null;
		try
		{
			consultaArmada=armarConsulta(	 criteriosBusquedaMap, false	 );
																	 
			ps=  new PreparedStatementDecorator(con.prepareStatement(consultaArmada));
			rs=new ResultSetDecorator(ps.executeQuery());
			HashMap mapa= UtilidadBD.cargarValueObject(new ResultSetDecorator(rs));
			mapa.put("FECHA_INICIAL", criteriosBusquedaMap.get("fechaInicial").toString());
			mapa.put("FECHA_FINAL", criteriosBusquedaMap.get("fechaFinal").toString());
			mapa.put("CODIGO_CONVENIO", criteriosBusquedaMap.get("codigoConvenio").toString());
			return mapa;
		}
		catch(SQLException e)
		{
			logger.warn("Error en la búsqueda avanzada de cuentas cobro capitacion " +e.toString());
			return null;
		}finally{
			try{
				if(ps!=null){
					ps.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUnidadPagoDao "+sqlException.toString() );
			}
			try{
				if(rs!=null){
					rs.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUnidadPagoDao "+sqlException.toString() );
			}
			
			
		}
	}

	/**
	 * Método que arma la consulta según los datos dados por el usuarios en 
	 * la búsqueda avanzada. 
	 * @param criteriosBusquedaMap
	 * @param esValidacionMostrarErrores  
	 * 				true= para cargar existenCarguesSaldoPendienteCeroOAjustesSinAprobar o
	 * 				false= para cargar busquedaCuentasCobro 
	 * @return
	 */
	private static String armarConsulta  (	HashMap criteriosBusquedaMap, boolean esValidacionMostrarErrores )
	{
		String consulta= 	"SELECT " +
							"'si' as seleccion, " +
							"cc.codigo AS cargue, " +
							"cc.contrato as codigocontrato, " +
							"getnumerocontrato(cc.contrato) as numerocontrato, " +
							"to_char(cc.fecha_inicial, 'DD/MM/YYYY') ||'-' ||  to_char(cc.fecha_final, 'DD/MM/YYYY') as fechainicialfinal, " +
							"cc.fecha_inicial ||'-' ||cc.fecha_final as fechainicialfinalbd, " +
							"to_char(cc.fecha_inicial, 'DD/MM/YYYY') as fechainicial, " +
							"to_char(cc.fecha_final, 'DD/MM/YYYY') as fechafinal," +
							"CASE WHEN cc.valor_total IS NULL THEN 0 ELSE cc.valor_total END AS valortotal, " +
							"CASE WHEN (cc.ajustes_credito - cc.ajustes_debito) IS NULL THEN 0 ELSE (cc.ajustes_credito - cc.ajustes_debito) END as valorajustes, " +
							"CASE WHEN cc.valor_total + (cc.ajustes_credito - cc.ajustes_debito) IS NULL THEN 0 ELSE cc.valor_total + (cc.ajustes_credito - cc.ajustes_debito) END AS saldo,  " +
							"'true' AS estabd, " +
							"'false' AS fueeliminada, " +
							"'false' AS puedoeliminar, " +
							"to_char(cc.fecha_cargue, 'DD/MM/YYYY') as fechacargue  " +
							"FROM " +
							"contrato_cargue cc " +
							"INNER JOIN contratos c ON (c.codigo=cc.contrato)	" +
							"where " +
							"c.convenio=" +criteriosBusquedaMap.get("codigoConvenio")+" "+
							"and cc.fecha_inicial >='" +UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechaInicial").toString())+"' " +
							"and cc.fecha_final <='"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechaFinal").toString())+"' " +
							"and cc.cuenta_cobro IS NULL and cc.anulado= '"+ValoresPorDefecto.getValorFalseCortoParaConsultas()+"' ";
								
		
		if(!esValidacionMostrarErrores)
		{
			consulta+="and (cc.valor_total+ (cc.ajustes_credito - cc.ajustes_debito))>0 ";
		}
		else
		{
			//verifica que el saldo sea cero
			consulta+="and ((cc.valor_total+ (cc.ajustes_credito - cc.ajustes_debito))=0 ";
			//verifica que los existan ajustes pendientes
			consulta+=" or (SELECT 1 FROM ajustes_cargue ac INNER JOIN ajustes_cxc acxc ON (acxc.codigo=ac.ajuste) where  ac.cargue=cc.codigo and acxc.estado="+ConstantesBD.codigoEstadoAjusteCxCPendiente+")=1) ";
		}
							
		consulta+="ORDER BY numerocontrato, cargue ASC";						
		return consulta;	
	}
	
	
	/**
	 * metodo que carga en el mapa los cargues que tienen saldo pendiente = 0 ó ajustes sin aprobar
	 * @param con
	 * @param criteriosBusquedaMap
	 * @return
	 */
	public static HashMap existenCarguesSaldoPendienteCeroOAjustesSinAprobar(Connection con, HashMap criteriosBusquedaMap)
	{
		String consultaArmada="";
		
		PreparedStatementDecorator ps=  null;
		try
		{
			consultaArmada=armarConsulta(	 criteriosBusquedaMap, true	 );
																	 
			ps=  new PreparedStatementDecorator(con.prepareStatement(consultaArmada));
		
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			ps.close();
			return mapaRetorno;

		}
		catch(SQLException e)
		{
			logger.warn("Error en existenCarguesSaldoPendienteCeroOAjustesSinAprobar capitacion " +e.toString());
			return null;
		}finally{
			try{
				if(ps!=null){
					ps.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUnidadPagoDao "+sqlException.toString() );
			}
			
			
		}
	}
	
}
