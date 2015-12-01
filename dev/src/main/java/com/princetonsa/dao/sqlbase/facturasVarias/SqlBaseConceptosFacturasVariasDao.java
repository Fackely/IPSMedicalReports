/*
 * Enero 23, 2008
 */
package com.princetonsa.dao.sqlbase.facturasVarias;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import com.princetonsa.decorator.ResultSetDecorator;


import util.ConstantesBD;
import util.UtilidadBD;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * 
 * @author Juan Sebastian Castaño C.
 * 
 * Objeto usado para manejar los accesos comunes a la fuente de datos
 * para la funcionalidad CONCEPTOS FACTURAS VARIAS
 *
 */

public class SqlBaseConceptosFacturasVariasDao {
	
	private static Logger logger = Logger.getLogger(SqlBaseConceptosFacturasVariasDao.class);
	
	
	private static final String[] indices = {"consecutivo_","codigo_","descripcion_","cuentaContableDebito_","codigoCuentaContableDebito_","cuentaContableCredito_","codigoCuentaContableCredito_","activo_","institucion_","eliminar_","usuarioModifica_","EsPosibleEliminar_","tipoconcepto_","tercero_","descripciontercero_","cuentaContableCredVa_","codigoCuentaContableCredVa_" ,"cuentaIngrVigencia_, codigoCuentaIngrVigencia_", "ajusteDebitoVigAnterior_", "codigoAjusteDebitoVigAnterior_" , "ajusteCreditoVigAnterior_" , "codigoAjusteCreditoVigAnterior_" };
	/**
	 * Cadena para consultar los conceptos de facturas varias
	 */
	private static final String cargarStr = "SELECT " +
		"cfv.consecutivo, " +
		"cfv.codigo, " +
		"cfv.descripcion, " +
		"coalesce(cfv.cuenta_contable_debito||'','') as cuenta_contable_debito, " +
		"CASE WHEN cc1.codigo IS NOT NULL THEN cc1.anio_vigencia || ' - ' || cc1.cuenta_contable ELSE '' END AS codigo_cuenta_contable_debito, " +
		"coalesce(cfv.cuenta_contable_credito||'','') as cuenta_contable_credito, " +
		"CASE WHEN cc2.codigo IS NOT NULL THEN cc2.anio_vigencia || ' - ' || cc2.cuenta_contable ELSE '' END AS codigo_cuenta_contable_credito, " +
		
		"cfv.activo," +
		"cfv.institucion," +
		"cfv.usuario_modifica, " +
		"'"+ConstantesBD.acronimoNo+"' as eliminar ," +
		"cfv.tipo_concepto AS tipoconcepto, " +
		"cfv.tercero AS tercero, " +
		"case when ter.numero_identificacion is null then '' else ter.numero_identificacion || ' - ' ||ter.DESCRIPCION end as descripciontercero, " +
		
		"ajuste_debito_vig_anterior AS ajuste_debito_vig_anterior, " +
	
		"ajuste_credito_vig_anterior AS ajuste_credito_vig_anterior, " +
	
		"coalesce(cfv.cuenta_cred_vig_anterior||'','') as cuenta_contable_cred_va, " +
		"getdesccuentascontables(cfv.cuenta_cred_vig_anterior) AS codigo_cuenta_contable_cred_va  , " +
		
		"coalesce(cfv.cuenta_ingr_vigencia||'','') as cuenta_ingr_vigencia , " +
		"CASE WHEN cc3.codigo IS NOT NULL THEN cc3.anio_vigencia || ' - ' || cc3.cuenta_contable ELSE '' END AS codigo_cuenta_ingr_vigencia ," +
		
		
		"coalesce(cfv.ajuste_debito_vig_anterior||'','') as ajustedebitoviganterior , " +
		"CASE WHEN cc4.codigo IS NOT NULL THEN cc4.anio_vigencia || ' - ' || cc4.cuenta_contable ELSE '' END AS codigoAjusteDebitoVigAnterior ," +
		
		"coalesce(cfv.ajuste_credito_vig_anterior||'','') as ajustecreditoviganterior , " +
		"CASE WHEN cc5.codigo IS NOT NULL THEN cc5.anio_vigencia || ' - ' || cc3.cuenta_contable ELSE '' END AS codigoAjusteCreditoVigAnterior " +
		
		" FROM conceptos_facturas_varias cfv " +
		"LEFT OUTER JOIN cuentas_contables cc1 ON(cc1.codigo=cfv.cuenta_contable_debito)  " +
		"LEFT OUTER JOIN cuentas_contables cc2 ON(cc2.codigo=cfv.cuenta_contable_credito)  " +
		"LEFT OUTER JOIN cuentas_contables cc3 ON(cc3.codigo=cfv.cuenta_ingr_vigencia) " +
		"LEFT OUTER JOIN cuentas_contables cc4 ON(cc4.codigo=cfv.ajuste_debito_vig_anterior) " +
		"LEFT OUTER JOIN cuentas_contables cc5 ON(cc5.codigo=cfv.ajuste_credito_vig_anterior) " +
		"LEFT OUTER JOIN terceros ter on(ter.codigo=cfv.tercero)" +
		"";
	
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 * 
	 * Metodo para consultar facturas varias, este metodo tambien evalua la posibilidad del usuario de eliminar cualquiera de estos registros
	 * 
	 */
	
	@SuppressWarnings("unchecked")
	public static HashMap<String, Object> cargar(Connection con, int institucion, String descripciontercero)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		
		PreparedStatementDecorator pst;
		
		String eliminar = "Delete from conceptos_facturas_varias where consecutivo = ?"; 
		
		try{
			String consulta = cargarStr + " WHERE cfv.institucion = ? order by cfv.descripcion ";
			pst =  new PreparedStatementDecorator(con, consulta);
			pst.setInt(1, institucion);			
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
			pst.close();
        }
		catch (SQLException e)
		{
			logger.error(e+" Error en consulta de facturas varias");
		}
				
		resultados.put("INDICES",indices);
		//UtilidadBD.abortarTransaccion(con);
		
		
		// evaluacion de posibilidad de eliminacion de registro debueltos		
	    for(int i=0;i< Utilidades.convertirAEntero(resultados.get("numRegistros")+"");i++)
		{
			int temp0 = ConstantesBD.codigoNuncaValido;
			//	Evaluar posibilidad de eliminacion
			try
			{
				UtilidadBD.iniciarTransaccionSinMensaje(con);
				//UtilidadBD.abrirConexion();
				pst =  new PreparedStatementDecorator(con.prepareStatement(eliminar,ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet ));
				pst.setDouble(1, Utilidades.convertirADouble(resultados.get("consecutivo_"+i).toString()));
				pst.execute();
				pst.close();
				
			}
			catch (SQLException e)
			{
				//EsPosibleEliminar = false;
				resultados.put("EsPosibleEliminar_"+i,"false");
				temp0+=10;
				UtilidadBD.abortarTransaccionSinMensaje(con);
			}
			//UtilidadBD.abortarTransaccion(con);
			//EsPosibleEliminar = true;
			if (temp0 < 0)
			{
				resultados.put("EsPosibleEliminar_"+i,"true");			
				UtilidadBD.abortarTransaccionSinMensaje(con);
			}
			
		}
		
		return resultados;
	}
	
	/**
	 * Funcion de busqueda de un concepto de factura especifico 
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public static HashMap<String, Object> buscarConceptFactVarByConsec (Connection con, int consecutivo)
	{
		HashMap<String, Object> resultado = new HashMap<String, Object>();
		try{
			String consulta = cargarStr + " WHERE cfv.consecutivo = ? ";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setDouble(1, Utilidades.convertirADouble(consecutivo+""));			
			resultado = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
			pst.close();
		}
		catch (SQLException e)
		{
			logger.error("Error en consulta de facturas varias", e);
		}
		resultado.put("INDICES",indices);
		return resultado;
		
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param descripcion
	 * @param cuenta_contable
	 * @param activo
	 * @param institucion
	 * @param tercero 
	 * @param Secuencia
	 * @return
	 * Metodo para crear un nuevo registro de conceptos de facturas varias
	 */
	
	public static boolean insertarConceptoFactura (Connection con,String codigo, String descripcion,
			int cuenta_contable_debito,String activo, int institucion,String usuarioModifica, int cuenta_contable_credito, String Seq_concept_fac_varias,String tipoconcepto, String tercero, int ajusteDebitoVigAnterior, int ajusteCreditoVigAnterior, int codigoCuentaContableCreditoVigenciaAnterior,int cuentaIngresoVigencia)
	{
		try
		{
			PreparedStatementDecorator ps = null;
			String consulta = " INSERT INTO conceptos_facturas_varias " +
											"(consecutivo," +
											"codigo," +
											"descripcion," +
											"cuenta_contable_debito," +
											"activo," +
											"institucion," +
											"usuario_modifica," +
											"fecha_modifica," +
											"hora_modifica, " +
											"cuenta_contable_credito,"+
											"tipo_concepto," +
											"tercero," +
											"ajuste_debito_vig_anterior," +
											"ajuste_credito_vig_anterior," +
											"cuenta_cred_vig_anterior ," +
											"cuenta_ingr_vigencia )"+
										" VALUES ("+ Seq_concept_fac_varias +",?,?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?,?,?,?,?,? ,?) ";
			
			
			ps= new PreparedStatementDecorator(con,consulta);
			ps.setString(1, codigo);
			ps.setString(2, descripcion);
			
			if (cuenta_contable_debito > 0)
			{
				ps.setDouble(3, Utilidades.convertirADouble(cuenta_contable_debito+""));
			}
			else
			{
				ps.setNull(3,Types.NUMERIC);
			}	
			ps.setString(4, activo);
			ps.setInt(5, institucion);
			ps.setString(6,usuarioModifica);
			
			boolean resultado=false;
			
			if (cuenta_contable_credito > 0)
			{
				ps.setDouble(7, Utilidades.convertirADouble(cuenta_contable_credito+""));
			}
			else
			{
				ps.setNull(7,Types.NUMERIC);
			}
			ps.setString(8, tipoconcepto);
			if(!tercero.equals(""))
			{
				ps.setString(9, tercero);
			}
			else
			{
				ps.setNull(9,Types.INTEGER);
			}
			if (ajusteDebitoVigAnterior > 0)
			{
				ps.setDouble(10, Utilidades.convertirADouble(ajusteDebitoVigAnterior+""));
			}
			else
			{
				ps.setNull(10,Types.NUMERIC);
			}
			if (ajusteCreditoVigAnterior > 0)
			{
				ps.setDouble(11, Utilidades.convertirADouble(ajusteCreditoVigAnterior+""));
			}
			else
			{
				ps.setNull(11,Types.NUMERIC);
			}
			
			if (codigoCuentaContableCreditoVigenciaAnterior<=0)
				ps.setNull(12,Types.NUMERIC);
			else
				ps.setDouble(12, Utilidades.convertirADouble(codigoCuentaContableCreditoVigenciaAnterior+""));

			
			
			if(cuentaIngresoVigencia>0)
			{
				ps.setDouble(13, Utilidades.convertirADouble(cuentaIngresoVigencia+""));
			}
			else
			{
				ps.setNull(13, Types.NUMERIC);
			}
			
			
			if (ps.executeUpdate() > 0)
			{
				resultado=true;
			}
			
			
			ps.close();
			return resultado;
		}
		catch (SQLException e)
		{
			logger.error("Error SqlBaseCanceptosFacturasVarias ",e);
			return false;
		}
		
	}
	
	

	
	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @param codigo
	 * @param descripcion
	 * @param cuenta_contable
	 * @param activo
	 * @param institucion
	 * @return
	 * 
	 * Metodo para modificar conceptos de facturas varias
	 */
	
	public static boolean modificarConceptoFacturasVarias (Connection con, int consecutivo, 
			String  codigo, String descripcion,int cuenta_contable_debito,String activo, int institucion,String usuarioModifica, int cuenta_contable_credito, String tipoconcepto, String tercero, int ajusteDebitoVigAnterior, int ajusteCreditoVigAnterior, int codigoCuentaContableCreditoVigenciaAnterior, int cuentaIngresoVigencia)
	{
		try
		{
			String consulta = "UPDATE conceptos_facturas_varias set " +
				"codigo=?," +
				"descripcion = ?, " +
				"cuenta_contable_debito = ?," +
				"activo = ?, " +
				"institucion = ?, " +
				"usuario_modifica = ?, " +
				"fecha_modifica = CURRENT_DATE, " +
				"hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
				"cuenta_contable_credito = ? ," +
				"tipo_concepto= ? , "+
				"tercero=?, "+
				"ajuste_debito_vig_anterior=?," +
				"ajuste_credito_vig_anterior=?," +
				"cuenta_cred_vig_anterior=? ," +
				"cuenta_ingr_vigencia =? "+
				"WHERE consecutivo ="+consecutivo;
			logger.info("actualizacion de concepto: "+consulta);
			logger.info("codigo: "+codigo);
			logger.info("descripcion: "+descripcion);
			logger.info("cuenta_contable_debito: "+cuenta_contable_debito);
			logger.info("cuenta_contable_credito: "+cuenta_contable_credito);
			logger.info("activo: "+activo);
			logger.info("isntitucion: "+institucion);
			logger.info("usuarioModifica: "+usuarioModifica);
			logger.info("tipoconcepto: "+tipoconcepto);
			logger.info("tercero: "+tercero);
			logger.info("ccc vigencia anteior: "+codigoCuentaContableCreditoVigenciaAnterior);
			PreparedStatementDecorator ps = null;
			
			
			ps= new PreparedStatementDecorator(con, consulta);
			
			ps.setString(1, codigo);
			ps.setString(2, descripcion);
			
			if (cuenta_contable_debito > 0)
			{
				ps.setDouble(3, Utilidades.convertirADouble(cuenta_contable_debito+""));                 
			}
			else
			{
				ps.setNull(3,Types.NUMERIC);
				
			}	
			
			
			ps.setString(4, activo);
			ps.setInt(5, institucion);
			ps.setString(6, usuarioModifica);
			
			if (cuenta_contable_credito > 0)
			{
				ps.setDouble(7, Utilidades.convertirADouble(cuenta_contable_credito+""));
			}
			else
			{
				ps.setNull(7,Types.NUMERIC);
			}
			
			
			
			
			ps.setString(8, tipoconcepto);
			if(Utilidades.convertirAEntero(tercero)>0)
			{
				ps.setInt(9, Integer.parseInt(tercero));
			}
			else
			{
				ps.setNull(9,Types.INTEGER);
			}
			if (ajusteDebitoVigAnterior > 0)
			{
				ps.setDouble(10, Utilidades.convertirADouble(ajusteDebitoVigAnterior+""));
			}
			else
			{
				ps.setNull(10,Types.NUMERIC);
			}
			
			
			if (ajusteCreditoVigAnterior > 0)
			{
				ps.setDouble(11, Utilidades.convertirADouble(ajusteCreditoVigAnterior+""));
			}
			else
			{
				ps.setNull(11,Types.NUMERIC);
			}
			
			
			if (codigoCuentaContableCreditoVigenciaAnterior<=0)
			{
				ps.setNull(12,Types.NUMERIC);
			}
			else
			{
				ps.setDouble(12, Utilidades.convertirADouble(codigoCuentaContableCreditoVigenciaAnterior+""));
			}
		
			
			if(cuentaIngresoVigencia>0)
			{
				ps.setDouble(13, Utilidades.convertirADouble(cuentaIngresoVigencia+""));
			}
			else
			{
				ps.setNull(13, Types.NUMERIC);
			}
			
			
			Log4JManager.info("MODIFICAR");
			Log4JManager.info(ps);
			
			
			boolean resultado=false;
			if (ps.executeUpdate() > 0)
			{
				resultado=true;
			}
			
			return resultado;
		}
		catch(SQLException e)
		{
			logger.warn(e+"Error SqlBaseCanceptosFacturasVarias "+e.toString() );
			return false;
		}
	}
	
	

	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @return
	 * 
	 * Metodo para eliminar conceptos de facturas varias
	 */
	
	
	public static boolean eliminarConceptoFacturasVarias (Connection con, int consecutivo)
	{
		try
		{
			PreparedStatementDecorator ps = null;
			ps= new PreparedStatementDecorator(con.prepareStatement("DELETE FROM conceptos_facturas_varias  " +
										"where consecutivo="+consecutivo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			if (ps.executeUpdate() <= 0)
				return false;
			return true;
		}
		catch(SQLException e)
		{
			logger.warn(e+"Error SqlBaseCanceptosFacturasVarias "+e.toString() );
			return false;
		}
	}
	
}
