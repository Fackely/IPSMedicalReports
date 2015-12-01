/*
 * Creado el May 12, 2006
 * por Julian Montoya
 */
package com.princetonsa.dao.sqlbase.interfaz;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

public class SqlBaseCuentaUnidadFuncionalDao {

	
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseCuentaServicioDao.class);

	
	/**
	 * Metodo para cargar las unidades funcionales. 
	 * @param con 
	 * @param tipoServicio 
	 * @param grupoServicio 
	 * @param institucion 
	 * @param unidadFuncional 
	 * @return
	 */

	public static HashMap cargarUnidadesFuncionales(Connection con, int tipoConsulta, int institucion, String unidadFuncional)
	{
		String consulta =""; 

		logger.info("TIPO CONSULTA -->"+tipoConsulta+"<-");
		
		switch (tipoConsulta)
		{
			case 0:  //---Consultar las unidades funcionales
			{
				consulta = "  SELECT * FROM (	" +
						   "  SELECT uf.acronimo as codigo_unidad_funcional, uf.descripcion as nombre_unidad_funcional, 												" +
						   "		   CASE WHEN ufci.consecutivo IS NULL THEN "+ValoresPorDefecto.getValorFalseParaConsultas()+" ELSE "+ValoresPorDefecto.getValorTrueParaConsultas()+" END as modificado,	 												" +
						   "		   CASE WHEN ufci.cuenta_ingreso IS NULL THEN 0 ELSE ufci.cuenta_ingreso END as cuentaingreso_unifun,	 							" +
						   "		   CASE WHEN ufci.cuenta_ingreso IS NULL THEN 0 ELSE ufci.cuenta_ingreso END as h_cuentaingreso_unifun, 							" +
						   "		   CASE WHEN ufci.cuenta_medicamento IS NULL THEN 0 ELSE ufci.cuenta_medicamento END as cuentamedicamento_unifun,					" +
						   "		   CASE WHEN ufci.cuenta_medicamento IS NULL THEN 0 ELSE ufci.cuenta_medicamento END as h_cuentamedicamento_unifun,					" +
						   "		   CASE WHEN ufci.consecutivo IS NULL THEN -1 ELSE ufci.consecutivo END as consecutivo,												" +
						   "		   CASE WHEN ufci.cuenta_ingreso_vig_anterior IS NULL THEN 0 ELSE ufci.cuenta_ingreso_vig_anterior END as cuenta_ingreso_vig_anterior,	 	" +
						   "		   CASE WHEN ufci.cuenta_ingreso_vig_anterior IS NULL THEN 0 ELSE ufci.cuenta_ingreso_vig_anterior END as h_cta_ingreso_vig_anterior,	 	" +
						   "		   CASE WHEN ufci.cuenta_med_vig_anterior IS NULL THEN 0 ELSE ufci.cuenta_med_vig_anterior END as cuenta_med_vig_anterior,	 	" +
						   "		   CASE WHEN ufci.cuenta_med_vig_anterior IS NULL THEN 0 ELSE ufci.cuenta_med_vig_anterior END as h_cuenta_med_vig_anterior,	 	" +
						   "		   CASE WHEN ufci.cuenta_contable_costo IS NULL THEN 0 ELSE ufci.cuenta_contable_costo END as cuentacosto,	     	" +
						   "		   CASE WHEN ufci.cuenta_contable_costo IS NULL THEN 0 ELSE ufci.cuenta_contable_costo END as cuentacostoho ,	 	" +
						   "		   ufci.rubro_presupuestal AS rubro 																					" +
						   "		   FROM unidades_funcionales uf																										" + 
						   "				LEFT OUTER JOIN unidad_fun_cuenta_ing ufci ON ( ufci.unidad_funcional = uf.acronimo AND  ufci.institucion = uf.institucion )" +
						   "					 WHERE uf.activo = " + ValoresPorDefecto.getValorTrueParaConsultas() +
						   "					   AND uf.institucion = " + institucion +
						   "		   		       AND uf.acronimo <> '" + ConstantesBD.codigoNuncaValido + "'" + 
						   "   UNION ALL	 " +  //-- El union es para sacar la unidad funcional "Todas las Unidades Funcionales" 
						   "   SELECT  '-1' as codigo_unidad_funcional, '-1' as nombre_unidad_funcional,													   " +   
						   "		   CASE WHEN ufci.consecutivo IS NULL THEN "+ValoresPorDefecto.getValorFalseParaConsultas()+" ELSE "+ValoresPorDefecto.getValorTrueParaConsultas()+" END as modificado, 										   	   " +
						   "		   CASE WHEN ufci.cuenta_ingreso IS NULL THEN 0 ELSE ufci.cuenta_ingreso END as cuentaingreso_unifun,					   " +
						   "		   CASE WHEN ufci.cuenta_ingreso IS NULL THEN 0 ELSE ufci.cuenta_ingreso END as h_cuentaingreso_unifun,				  	   " + 
						   "		   CASE WHEN ufci.cuenta_medicamento IS NULL THEN 0 ELSE ufci.cuenta_medicamento END as cuentamedicamento_unifun,  	  	   " +
						   "		   CASE WHEN ufci.cuenta_medicamento IS NULL THEN 0 ELSE ufci.cuenta_medicamento END as h_cuentamedicamento_unifun,	  	   " + 
						   "		   CASE WHEN ufci.consecutivo IS NULL THEN -1 ELSE ufci.consecutivo END as consecutivo,  								   " +
						   "		   CASE WHEN ufci.cuenta_ingreso_vig_anterior IS NULL THEN 0 ELSE ufci.cuenta_ingreso_vig_anterior END as cuenta_ingreso_vig_anterior,	 	" +
						   "		   CASE WHEN ufci.cuenta_ingreso_vig_anterior IS NULL THEN 0 ELSE ufci.cuenta_ingreso_vig_anterior END as h_cta_ingreso_vig_anterior,	 	" +
						   "		   CASE WHEN ufci.cuenta_med_vig_anterior IS NULL THEN 0 ELSE ufci.cuenta_med_vig_anterior END as cuenta_med_vig_anterior,	 	" +
						   "		   CASE WHEN ufci.cuenta_med_vig_anterior IS NULL THEN 0 ELSE ufci.cuenta_med_vig_anterior END as h_cuenta_med_vig_anterior," +
						   "		   CASE WHEN ufci.cuenta_contable_costo IS NULL THEN 0 ELSE ufci.cuenta_contable_costo END as cuentacosto,	     	" +
						   "		   CASE WHEN ufci.cuenta_contable_costo IS NULL THEN 0 ELSE ufci.cuenta_contable_costo END as cuentacostoho, 	 	" +
						   "		   ufci.rubro_presupuestal AS rubro	 		   																" +
						   "		   FROM unidad_fun_cuenta_ing ufci																						   " + 
						   "		   		WHERE ufci.institucion = " + institucion +	 
						   "		   		  AND ufci.unidad_funcional = '" + ConstantesBD.codigoNuncaValido + "' ) x										   " + 
						   "		        ORDER BY x.nombre_unidad_funcional																				   ";
			}
			break;
			case 1:  //---Consultar los centros de costo asociados a unidades funcionales.  
			{
				consulta = "	SELECT cc.codigo as codigo_cc, cc.nombre as  nombre_cc,																				" +  
						   "		   CASE WHEN ufcic.unidad_funcional IS NULL THEN '' ELSE ufcic.unidad_funcional END as unidad_funcional_cc,						" +
						   "		   CASE WHEN ufcic.unidad_funcional IS NULL THEN "+ValoresPorDefecto.getValorFalseParaConsultas()+" ELSE "+ValoresPorDefecto.getValorTrueParaConsultas()+" END as modificado_cc,											" +
						   "		   CASE WHEN ufcic.cuenta_ingreso IS NULL THEN 0 ELSE ufcic.cuenta_ingreso END as cuentaingreso_cc,  							" + 
						   "		   CASE WHEN ufcic.cuenta_ingreso IS NULL THEN 0 ELSE ufcic.cuenta_ingreso END as h_cuentaingreso_cc,							" +
						   "		   CASE WHEN ufcic.cuenta_medicamento IS NULL THEN 0 ELSE ufcic.cuenta_medicamento END as cuentamedicamento_cc,					" +
						   "		   CASE WHEN ufcic.cuenta_medicamento IS NULL THEN 0 ELSE ufcic.cuenta_medicamento END as h_cuentamedicamento_cc,				" +
						   "		   CASE WHEN ufcic.cuenta_ingreso_vig_anterior IS NULL THEN 0 ELSE ufcic.cuenta_ingreso_vig_anterior END as cuenta_ingreso_vig_anterior_cc,	 	" +
						   "		   CASE WHEN ufcic.cuenta_ingreso_vig_anterior IS NULL THEN 0 ELSE ufcic.cuenta_ingreso_vig_anterior END as h_cta_ingreso_vig_anterior_cc,	 	" +
						   "		   CASE WHEN ufcic.cuenta_med_vig_anterior IS NULL THEN 0 ELSE ufcic.cuenta_med_vig_anterior END as cuenta_med_vig_anterior_cc,	 	" +
						   "		   CASE WHEN ufcic.cuenta_med_vig_anterior IS NULL THEN 0 ELSE ufcic.cuenta_med_vig_anterior END as h_cuenta_med_vig_anterior_cc,	 	" +
						   "		   CASE WHEN ufcic.cuenta_contable_costo IS NULL THEN 0 ELSE ufcic.cuenta_contable_costo END as cuentacostocc,	     	" +
						   "		   CASE WHEN ufcic.cuenta_contable_costo IS NULL THEN 0 ELSE ufcic.cuenta_contable_costo END as cuentacostocch, 	 	" +
						   "		   ufcic.rubro_presupuestal AS rubro_cc	 		   " +
						   "		   FROM centros_costo cc  																										" +
						   "		   		LEFT OUTER JOIN unidad_fun_cuenta_ing_cc ufcic 																			" +
						   "				ON ( ufcic.centro_costo = cc.codigo AND ufcic.institucion = " + institucion +  " AND ufcic.unidad_funcional = '" +  unidadFuncional + "' ) " +
						   "			  	       WHERE cc.codigo > 0 " +
						   //"						 AND cc.nombre	<> " + ConstantesBD.codigoCentroCostoExternos  +
						   // "						 AND cc.nombre	<> " + ConstantesBD.codigoCentroCostoTodos  +
						   "						 AND cc.unidad_funcional='" +  unidadFuncional + "' " +
						   "			  	       ORDER BY cc.nombre					";
			}
			break;
			case 2:  //--  Cargar Los Tipos De Cuentas Contable Parametrizados.
			{
				consulta = "	SELECT codigo, descripcion as nombre	 " +
						   "		   FROM cuentas_contables 					 " +			
						   "			    WHERE institucion = " + institucion + 	
						   "				  AND activo = " + ValoresPorDefecto.getValorTrueParaConsultas()+ 
						   "					  ORDER BY descripcion	";
			}
			break;
			
		}
		
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,false);
			pst.close();
			return mapaRetorno;
		}
		catch (SQLException e)
		{
			logger.error("Error en cargarUnidadesFuncionales de SqlBaseCuentaUnidadFuncionalDao: "+e); 
			return null;
		}

	}

	/**
	 * Metodo para insertar los grupos.
	 * @param con 
	 * @param cuentaIngresoMod 
	 * @param centroCosto
	 * @param codigoTipoGrupo
	 * @param codigoGrupo 
	 * @param cuentaIngreso
	 * @param modificado
	 * @param modificado 
	 * @param rubroPresupuestal 
	 * @param acronimo 
	 * @param tipoSerSel 
	 * @return
	 */
	public static int insertarUnidadesFuncionales(Connection con, int tipoInsercion, int institucion, String codUnidadFuncional, int centroCosto, String cuentaIngreso, String cuentaMedicamento, int consecutivo, String modificado , String cuentaVigenciaAnterior , String cuentaVigenciaAnteriorMed, String rubroPresupuestal, String cuentaCosto)
	{
		StringBuffer cadIngreso = new StringBuffer(); 
		
		try
		{
			if ( tipoInsercion == 0 ) //-----Se ingresan Las unidades funcionales.  
			{
				if (!UtilidadTexto.getBoolean(modificado) )
				{ 
				
					cadIngreso.append(" INSERT INTO unidad_fun_cuenta_ing (consecutivo, unidad_funcional, institucion, cuenta_ingreso, cuenta_medicamento, cuenta_ingreso_vig_anterior, cuenta_med_vig_anterior, rubro_presupuestal, cuenta_contable_costo) VALUES ("+ UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_unidad_fun_cuenta_ing") +",?,?,?,?,?,?,?,?) ");
					PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(cadIngreso.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					stm.setString (1, codUnidadFuncional);
					stm.setInt(2, institucion);
					
					if ( UtilidadCadena.noEsVacio(cuentaIngreso) && Utilidades.convertirAEntero(cuentaIngreso)>0 )
					{
						stm.setInt(3, Utilidades.convertirAEntero(cuentaIngreso) );
					}
					else { stm.setNull(3, Types.NUMERIC ); }

					if ( UtilidadCadena.noEsVacio(cuentaMedicamento) && Utilidades.convertirAEntero(cuentaMedicamento)>0 )
					{
						stm.setInt(4, Utilidades.convertirAEntero(cuentaMedicamento) );
					}
					else { stm.setNull(4, Types.NUMERIC ); }
					
					//
					if ( UtilidadCadena.noEsVacio(cuentaVigenciaAnterior) && Utilidades.convertirAEntero(cuentaVigenciaAnterior)>0 )
					{
						stm.setInt(5, Utilidades.convertirAEntero(cuentaVigenciaAnterior) );
					}
					else { stm.setNull(5, Types.NUMERIC ); }
					
					
					if ( UtilidadCadena.noEsVacio(cuentaVigenciaAnteriorMed) && Utilidades.convertirAEntero(cuentaVigenciaAnteriorMed)>0 )
					{
						stm.setInt(6, Utilidades.convertirAEntero(cuentaVigenciaAnteriorMed) );
					}
					else { stm.setNull(6, Types.NUMERIC ); }

					if ( UtilidadCadena.noEsVacio(rubroPresupuestal) && Utilidades.convertirAEntero(rubroPresupuestal)>0 )
					{
						stm.setString(7, rubroPresupuestal.trim());
					}
					else { stm.setNull(7, Types.VARCHAR); }
					
					if ( UtilidadCadena.noEsVacio(cuentaCosto) && Utilidades.convertirAEntero(cuentaCosto)>0 )
					{
						stm.setDouble(8, Utilidades.convertirADouble(cuentaCosto.trim()));
					}
					else { stm.setNull(8, Types.VARCHAR); }
					
					return stm.executeUpdate();
				}
				else
				{ 
					cadIngreso.append(" UPDATE unidad_fun_cuenta_ing SET cuenta_ingreso = ?, cuenta_medicamento = ? , cuenta_ingreso_vig_anterior = ?, cuenta_med_vig_anterior = ?, rubro_presupuestal=?, cuenta_contable_costo=?  WHERE consecutivo = ? ");
					PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(cadIngreso.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					if ( UtilidadCadena.noEsVacio(cuentaIngreso) && Utilidades.convertirAEntero(cuentaIngreso)>0 )
					{
						stm.setDouble(1, Utilidades.convertirADouble(cuentaIngreso) );
					}
					else { stm.setNull(1, Types.NUMERIC ); }

					if ( UtilidadCadena.noEsVacio(cuentaMedicamento) && Utilidades.convertirAEntero(cuentaMedicamento)>0 )
					{
						stm.setDouble(2, Utilidades.convertirADouble(cuentaMedicamento) );
					}
					else { stm.setNull(2, Types.NUMERIC ); }
					
					if ( UtilidadCadena.noEsVacio(cuentaVigenciaAnterior) && Utilidades.convertirAEntero(cuentaVigenciaAnterior)>0 )
					{
						stm.setDouble(3, Utilidades.convertirADouble(cuentaVigenciaAnterior) );
					}
					else { stm.setNull(3, Types.NUMERIC ); }
					
					
					if ( UtilidadCadena.noEsVacio(cuentaVigenciaAnteriorMed) && Utilidades.convertirAEntero(cuentaVigenciaAnteriorMed)>0 )
					{
						stm.setDouble(4, Utilidades.convertirADouble(cuentaVigenciaAnteriorMed) );
					}
					else { stm.setNull(4, Types.NUMERIC ); }
					
					
					if ( UtilidadCadena.noEsVacio(rubroPresupuestal) && Utilidades.convertirAEntero(rubroPresupuestal)>0 )
					{
						stm.setString(5, rubroPresupuestal.trim());
					}
					else { stm.setNull(5, Types.VARCHAR); }
					
					if ( UtilidadCadena.noEsVacio(cuentaCosto) && Utilidades.convertirAEntero(cuentaCosto)>0 )
					{
						stm.setDouble(6, Utilidades.convertirADouble(cuentaCosto.trim()));
					}
					else { stm.setNull(6, Types.NUMERIC); }
					
					stm.setDouble(7, Utilidades.convertirADouble(consecutivo+""));

					return stm.executeUpdate();
				}
				
			}

			if ( tipoInsercion == 1 ) //-Se ingresan la cuenta de ingreso para el centro de costo para una unidad funcional especifica.  
			{
				if ( !UtilidadTexto.getBoolean(modificado))
				{ 
					cadIngreso.append(" INSERT INTO unidad_fun_cuenta_ing_cc ( " +
														"unidad_funcional, " +
														"institucion, " +
														"centro_costo, " +
														"cuenta_ingreso, " +
														"cuenta_medicamento , " +
														"cuenta_ingreso_vig_anterior, " +
														"cuenta_med_vig_anterior, " +
														"rubro_presupuestal," +
														"cuenta_contable_costo) VALUES (?, ?, ?, ?, ?, ?, ?, ?,?) ");
					
					
					PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(cadIngreso.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					stm.setString(1, codUnidadFuncional);
					stm.setInt(2, institucion);
					stm.setInt(3, centroCosto);
					
					if ( UtilidadCadena.noEsVacio(cuentaIngreso) && Utilidades.convertirAEntero(cuentaIngreso)>0 )
					{
						stm.setDouble(4, Utilidades.convertirADouble(cuentaIngreso) );
					}
					else { stm.setNull(4, Types.NUMERIC ); }

					if ( UtilidadCadena.noEsVacio(cuentaMedicamento) && Utilidades.convertirAEntero(cuentaMedicamento)>0 )
					{
						stm.setDouble(5, Utilidades.convertirADouble(cuentaMedicamento) );
					}
					else { stm.setNull(5, Types.NUMERIC ); }
						
					if ( UtilidadCadena.noEsVacio(cuentaVigenciaAnterior) && Utilidades.convertirAEntero(cuentaVigenciaAnterior)>0 )
					{
						stm.setDouble(6, Utilidades.convertirADouble(cuentaVigenciaAnterior) );
					}
					else { stm.setNull(6, Types.NUMERIC ); }
					
					
					if ( UtilidadCadena.noEsVacio(cuentaVigenciaAnteriorMed) && Utilidades.convertirAEntero(cuentaVigenciaAnteriorMed)>0 )
					{
						stm.setDouble(7, Utilidades.convertirADouble(cuentaVigenciaAnteriorMed) );
					}
					else { stm.setNull(7, Types.NUMERIC ); }
					
					
					if ( UtilidadCadena.noEsVacio(rubroPresupuestal) && Utilidades.convertirAEntero(rubroPresupuestal)>0 )
					{
						stm.setString(8, rubroPresupuestal.trim());
					}
					else { stm.setNull(8, Types.VARCHAR); }
					
					
					if ( UtilidadCadena.noEsVacio(cuentaCosto) && Utilidades.convertirAEntero(cuentaCosto)>0 )
					{
						stm.setDouble(9, Utilidades.convertirADouble(cuentaCosto.trim()));
					}
					else { stm.setNull(9, Types.VARCHAR); }
					//
					
					return stm.executeUpdate();
				}
				else
				{ 
					cadIngreso.append(" UPDATE unidad_fun_cuenta_ing_cc SET " +
																"cuenta_ingreso = ?, " +
																"cuenta_medicamento = ? , " +
																"cuenta_ingreso_vig_anterior = ?, " +
																"cuenta_med_vig_anterior = ?, " +
																"rubro_presupuestal = ?," +
																"cuenta_contable_costo=? " +
																"WHERE unidad_funcional = ? " +
																"AND institucion = ? " +
																"AND centro_costo = ? ");
					
					PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(cadIngreso.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					
					if ( UtilidadCadena.noEsVacio(cuentaIngreso) && Utilidades.convertirAEntero(cuentaIngreso)>0 )
					{
						stm.setDouble(1, Utilidades.convertirADouble(cuentaIngreso) );
					}
					else { stm.setNull(1, Types.NUMERIC); }

					if ( UtilidadCadena.noEsVacio(cuentaMedicamento) && Utilidades.convertirAEntero(cuentaMedicamento)>0 )
					{
						stm.setDouble(2, Utilidades.convertirADouble(cuentaMedicamento) );
					}
					else { stm.setNull(2, Types.NUMERIC); }
						
					if ( UtilidadCadena.noEsVacio(cuentaVigenciaAnterior) && Utilidades.convertirAEntero(cuentaVigenciaAnterior)>0 )
					{
						stm.setDouble(3, Utilidades.convertirADouble(cuentaVigenciaAnterior) );
					}
					else { stm.setNull(3, Types.NUMERIC ); }
					
					if ( UtilidadCadena.noEsVacio(cuentaVigenciaAnteriorMed) && Utilidades.convertirAEntero(cuentaVigenciaAnteriorMed)>0 )
					{
						stm.setDouble(4, Utilidades.convertirADouble(cuentaVigenciaAnteriorMed) );
					}
					else { stm.setNull(4, Types.NUMERIC ); }
					
					if ( UtilidadCadena.noEsVacio(rubroPresupuestal) && Utilidades.convertirAEntero(rubroPresupuestal)>0 )
					{
						stm.setString(5, rubroPresupuestal.trim());
					}
					else { stm.setNull(5, Types.VARCHAR); }
					//					
					
					if ( UtilidadCadena.noEsVacio(cuentaCosto) && Utilidades.convertirAEntero(cuentaCosto)>0 )
					{
						stm.setDouble(6, Utilidades.convertirADouble(cuentaCosto.trim()));
					}
					else { stm.setNull(6, Types.VARCHAR); }
					
					stm.setString(7, codUnidadFuncional);
					stm.setInt(8, institucion);
					stm.setInt(9, centroCosto);
					return stm.executeUpdate();
				}
				
			}
			
		}
		catch (SQLException e)
		{
			logger.error("Error ingresando EN (insertarUnidadesFuncionales) EN SqlBaseCuentaUnidadFuncionalDao  consutal [" + cadIngreso.toString() + "] : "+e);
			return ConstantesBD.codigoNuncaValido;
		}	
		return 0;
	}

/**
 *  Metodo para eliminar la Cuenta Ingreso Medicamento de una Unidad especifica. 
 * @param con
 * @param esCuentaIngreso
 * @param consecutivo
 * @param centroCosto
 * @param institucion
 * @param esCuentaAnterior
 * @return
 */
	public static int eliminarCuenta(Connection con, boolean esCuentaIngreso, int consecutivo, int centroCosto, int institucion, boolean esCuentaAnterior)
	{
		String consulta = ""; 
		logger.info("esCuentaIngreso ->"+esCuentaIngreso+"<-");
		logger.info("consecutivo ->"+consecutivo+"<-");
		logger.info("centroCosto ->"+centroCosto+"<-");
		logger.info("esCuentaAnterior ->"+esCuentaAnterior+"<-");
		
		
		if (centroCosto==ConstantesBD.codigoNuncaValido) //--Eliminar Unidades Funcionales.  
		{
			if (esCuentaIngreso)
			{
				if (!esCuentaAnterior)
				{
					if ( consecutivo == ConstantesBD.codigoNuncaValido )
					{
						consulta  = " UPDATE unidad_fun_cuenta_ing SET cuenta_ingreso = ? WHERE unidad_funcional = ? AND institucion = ? ";
					}
					else
					{
						consulta = " UPDATE unidad_fun_cuenta_ing SET cuenta_ingreso = ? WHERE consecutivo = ? ";
					}
				}
				else
				{
					if ( consecutivo == ConstantesBD.codigoNuncaValido)
					{
						consulta  = " UPDATE unidad_fun_cuenta_ing SET cuenta_ingreso_vig_anterior = ? WHERE unidad_funcional = ? AND institucion = ? ";
					}
					else
					{
						consulta = " UPDATE unidad_fun_cuenta_ing SET cuenta_ingreso_vig_anterior = ? WHERE consecutivo = ? ";
					}
				}					
			}
			else
			{
				if (!esCuentaAnterior)
				{				
					if ( consecutivo == ConstantesBD.codigoNuncaValido)
					{
						consulta = " UPDATE unidad_fun_cuenta_ing SET cuenta_medicamento = ? WHERE unidad_funcional = ? AND institucion = ? ";
					}	
					else
					{
						consulta = " UPDATE unidad_fun_cuenta_ing SET cuenta_medicamento = ? WHERE consecutivo = ? ";
					}
				}
				else
				{
					if ( consecutivo == ConstantesBD.codigoNuncaValido )
					{
						consulta = " UPDATE unidad_fun_cuenta_ing SET cuenta_med_vig_anterior = ? WHERE unidad_funcional = ? AND institucion = ? ";
					}	
					else
					{
						consulta = " UPDATE unidad_fun_cuenta_ing SET cuenta_med_vig_anterior = ? WHERE consecutivo = ? ";
					}					
				}
			}
		}
		else 			   //--Eliminar Unidades Funcionales por centro de atencion.
		{
			if (esCuentaIngreso)
			{
				if (!esCuentaAnterior)
				{				
					consulta  = " UPDATE unidad_fun_cuenta_ing_cc SET cuenta_ingreso = ? WHERE unidad_funcional = ? AND centro_costo = ? AND institucion = ? ";
				}
				else
				{
					consulta  = " UPDATE unidad_fun_cuenta_ing_cc SET cuenta_ingreso_vig_anterior = ? WHERE unidad_funcional = ? AND centro_costo = ? AND institucion = ? ";					
				}
				
			}
			else
			{
				if (!esCuentaAnterior)
				{				
					consulta = " UPDATE unidad_fun_cuenta_ing_cc SET cuenta_medicamento = ? WHERE unidad_funcional = ? AND centro_costo = ?  AND institucion = ? ";
				}				
				else
				{
					consulta = " UPDATE unidad_fun_cuenta_ing_cc SET cuenta_med_vig_anterior = ? WHERE unidad_funcional = ? AND centro_costo = ?  AND institucion = ? ";
				}
			}
		}
		
		
		try {
			
			PreparedStatementDecorator stm;
			stm =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			if (centroCosto == ConstantesBD.codigoNuncaValido)
			{
				if ( consecutivo == ConstantesBD.codigoNuncaValido )
				{
					stm.setNull(1, Types.VARCHAR);
					stm.setInt(2, consecutivo);
					stm.setInt(3, institucion);
				}
				else
				{
					stm.setNull(1, Types.VARCHAR);
					stm.setInt(2, consecutivo);
				}
			}	
			else
			{
				stm.setNull(1, Types.VARCHAR);
				stm.setInt(2, consecutivo);
				stm.setInt(3, centroCosto);
				stm.setInt(4, institucion);
			}
			return stm.executeUpdate();
		} 
		catch (SQLException e)
		{
			return ConstantesBD.codigoNuncaValido;
		}
	}

	public static int eliminarRubro(Connection con, int consecutivo, int centroCosto, int institucion) 
	{
		logger.info("CONSECUTIVO -->"+consecutivo+"<-");
		logger.info("centroCosto -->"+centroCosto+"<-");
		logger.info("institucion -->"+institucion+"<-");
		String consulta="";
		
		if(centroCosto == ConstantesBD.codigoNuncaValido)
		{
			if ( consecutivo == ConstantesBD.codigoNuncaValido )
			{
				consulta = " UPDATE unidad_fun_cuenta_ing SET rubro_presupuestal = ? WHERE unidad_funcional = ? AND institucion = ? ";
			}	
			else
			{
				consulta = " UPDATE unidad_fun_cuenta_ing SET rubro_presupuestal = ? WHERE consecutivo = ? ";
			}
		}
		else
		{
			consulta = " UPDATE unidad_fun_cuenta_ing_cc SET rubro_presupuestal = ? WHERE unidad_funcional = ? AND centro_costo = ?  AND institucion = ? ";
		}
		
		try {
			
			PreparedStatementDecorator stm;
			stm =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			if ( centroCosto == ConstantesBD.codigoNuncaValido)
			{
				if ( consecutivo == ConstantesBD.codigoNuncaValido )
				{
					stm.setNull(1, Types.VARCHAR);
					stm.setInt(2, consecutivo);
					stm.setInt(3, institucion);
				}
				else
				{
					stm.setNull(1, Types.VARCHAR);
					stm.setInt(2, consecutivo);
				}
			}	
			else
			{
				stm.setNull(1, Types.VARCHAR);
				stm.setInt(2, consecutivo);
				stm.setInt(3, centroCosto);
				stm.setInt(4, institucion);
			}
			return stm.executeUpdate();
		} 
		catch (SQLException e)
		{
			return ConstantesBD.codigoNuncaValido;
		}
	}		
	

}
