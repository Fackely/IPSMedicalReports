/*
 * @(#)SqlBaseTiposConvenioDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK jdk1.5.0_07
 *
 */
package com.princetonsa.dao.sqlbase.facturacion;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;


import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.facturacion.TiposConvenio;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * Consultas estandar de Tipos Convenio
 * @author Julián Pacheco Jiménez
 * jpacheco@princetonsa.com
 */

public class SqlBaseTiposConvenioDao 
{
	/**
	 * Objeto para manejar los logs de esta clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseTiposConvenioDao.class);
	
	/**
	 * cadena para la insercion de tipos convenio
	 */
	private static final String cadenaInsertarStr="INSERT INTO " +
														"tipos_convenio (" +
															"codigo, " +
															"institucion, " +
															"descripcion, " +
															"tipo_regimen, " +
															"cuenta_contable, " +
															"usuario_modifica," +
															"fecha_modifica, " +
															"hora_modifica, " +
															"clasificacion, " +
															"rubro_presupuestal, " +
															"cuenta_contable_val_conv, " +
															"cuenta_contable_val_pac, " +
															"cuenta_contable_des_pac, " +
															"cuenta_contable_cxc_cap, " +
															"cuenta_contable_util, " +
															"cuenta_contable_perd, " +
															"cuenta_debito_gls_recibida, " +
															"cuenta_credito_gls_recibida, " +
															"cuenta_debito_gls_aceptada, " +
															"cuenta_credito_gls_aceptada,aseg_at_ec " +
														") VALUES (?, ?, ?, ? ,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
	
	/**
	 * cadena para la modificacion de tipos convenio
	 */
	private static final String cadenaModificarStr= "UPDATE " +
														"tipos_convenio " +
													"SET " +
														"codigo=?, " +
														"descripcion=?, " +
														"tipo_regimen=?, " +
														"cuenta_contable=?, " +
														"usuario_modifica=?, " +
														"fecha_modifica=?, " +
														"hora_modifica=?, " +
														"clasificacion=?, " +
														"rubro_presupuestal=?, " +
														"cuenta_contable_val_conv=?, " +
														"cuenta_contable_val_pac=?, " +
														"cuenta_contable_des_pac=?, " +
														"cuenta_contable_cxc_cap=?, " +
														"cuenta_contable_util=?, " +
														"cuenta_contable_perd=?, " +
														"cuenta_debito_gls_recibida=?, " +
														"cuenta_credito_gls_recibida=?, " +
														"cuenta_debito_gls_aceptada=?, " +
														"cuenta_credito_gls_aceptada=?," +
														"aseg_at_ec=? " +
													"WHERE codigo=? AND institucion=?";
	
	/**
	 * cadena para la elimininacion de tipos de convenio
	 */
	private static final String cadenaEliminarStr="DELETE FROM tipos_convenio WHERE codigo=? AND institucion=?";
	
	/**
	 * Cadena de consultas de Tipos Convenios
	 */
	private static final String consultarTiposConvenioStr="SELECT " +
																"tc.codigo as codigo, " +
																"tc.descripcion as descripcion, " +
																"tc.tipo_regimen as acronimotiporegimen," +
																"tc.cuenta_contable as codigocuentacontable, " +
																"cc.cuenta_contable " +
																"||' - '||  cc.descripcion " +
																"||' - '|| case when cc.naturaleza_cuenta='1' then 'DEB' ELSE 'CRE' end " +
																"||' - '|| case when cc.manejo_terceros="+ValoresPorDefecto.getValorTrueParaConsultas()+" then 'Si' else 'No' end " +
																"||' - '|| case when cc.manejo_centros_costo="+ValoresPorDefecto.getValorTrueParaConsultas()+" then 'Si' else 'No' end " +
																"||' - '|| case when cc.manejo_base_gravable="+ValoresPorDefecto.getValorTrueParaConsultas()+" then 'Si' else 'No' end " +
																"as descripcioncuentacontable, " +
																"'"+ConstantesBD.acronimoSi+"' as estabd, " +
																"tc.codigo as codigoantesmod, " +
																"tc.clasificacion as codigoclasificacion," +
																"tc.aseg_at_ec as asegatec, " +
																"tc.rubro_presupuestal as rubro, " +
																"tc.cuenta_contable_val_conv as ccValConv, " +
																"tc.cuenta_contable_val_pac as ccValPac, " +
																"tc.cuenta_contable_des_pac as ccDesPac, " +
																"tc.cuenta_contable_cxc_cap as ccCxcCap, " +
																"tc.cuenta_contable_util as ccutilxing, " +
																"tc.cuenta_contable_perd as ccperdxing, " +
																"tc.cuenta_debito_gls_recibida as ccdebglorec, " +
																"tc.cuenta_credito_gls_recibida as cccreglorec, " +
																"tc.cuenta_debito_gls_aceptada as ccdebgloace, " +
																"tc.cuenta_credito_gls_aceptada as cccregloace " +
															"FROM " +
																" tipos_convenio tc " +
																" LEFT OUTER JOIN cuentas_contables cc ON (cc.codigo=tc.cuenta_contable) " +
															"WHERE " +
																"tc.institucion=? order by codigo ";
	
	
	/**
	 * Cadena de consultas de Tipos Convenios por codigo
	 */
	private static final String consultarTiposConvenioEspecificoStr="SELECT " +
																		"tc.codigo as codigo, " +
																		"tc.descripcion as descripcion, " +
																		"tc.tipo_regimen as acronimotiporegimen," +
																		"tc.cuenta_contable as codigocuentacontable, " +
																		"cc.cuenta_contable " +
																		"||' - '||  cc.descripcion " +
																		"||' - '|| case when cc.naturaleza_cuenta='1' then 'DEB' ELSE 'CRE' end " +
																		"||' - '|| case when cc.manejo_terceros="+ValoresPorDefecto.getValorTrueParaConsultas()+" then 'Si' else 'No' end " +
																		"||' - '|| case when cc.manejo_centros_costo="+ValoresPorDefecto.getValorTrueParaConsultas()+" then 'Si' else 'No' end " +
																		"||' - '|| case when cc.manejo_base_gravable="+ValoresPorDefecto.getValorTrueParaConsultas()+" then 'Si' else 'No' end " +
																		"as descripcioncuentacontable, " +
																		"'"+ConstantesBD.acronimoSi+"' as estabd, " +
																		"tc.codigo as codigoantesmod, " +
																		"tc.clasificacion as codigoclasificacion, " +
																		"tc.aseg_at_ec as asegatec, " +
																		"tc.rubro_presupuestal as rubro, " +
																		"tc.cuenta_contable_val_conv as ccValConv, " +
																		"tc.cuenta_contable_val_pac as ccValPac, " +
																		"tc.cuenta_contable_des_pac as ccDesPac, " +
																		"tc.cuenta_contable_cxc_cap as ccCxcCap, " +
																		"tc.cuenta_contable_util as ccutilxing, " +
																		"tc.cuenta_contable_perd as ccperdxing, " +
																		"tc.cuenta_debito_gls_recibida as ccdebglorec, " +
																		"tc.cuenta_credito_gls_recibida as cccreglorec, " +
																		"tc.cuenta_debito_gls_aceptada as ccdebgloace, " +
																		"tc.cuenta_credito_gls_aceptada as cccregloace " +
																	"FROM " +
																		" tipos_convenio tc " +
																		" LEFT OUTER JOIN cuentas_contables cc ON (cc.codigo=tc.cuenta_contable) " +
																	"WHERE " +
																		"tc.codigo=? AND tc.institucion=?  ";
	
	/**
	 * Insertar un registro de tipos de convenio
	 * @param con
	 * @param TiposConvenio tiposconvenio
	 * */
	public static boolean insertarTiposConvenio(Connection con, TiposConvenio tiposconvenio) {
		PreparedStatementDecorator ps = null;
			
		try	{			
			ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO tipos_convenio (
			 * codigo, 
			 * institucion, 
			 * descripcion, 
			 * tipo_regimen, 
			 * cuenta_contable, 
			 * usuario_modifica,
			 * fecha_modifica, 
			 * hora_modifica, 
			 * clasificacion, 
			 * rubro_presupuestal) VALUES (?, ?, ?, ? ,?, ?, ?, ?, ?, ?)
			 */
			
			ps.setString(1, UtilidadBD.obtenerSiguienteValorSecuencia(con, "facturacion.seq_tipos_convenio")+"");
			ps.setInt(2,tiposconvenio.getCodigoInstitucion());
			ps.setString(3,tiposconvenio.getDescripcion());
			ps.setString(4,tiposconvenio.getAcronimoTipoRegimen());
			
			if(tiposconvenio.getCodigoCuentaContable()<1)
			{
				ps.setNull(5, Types.NUMERIC);
			}
			else
			{	
				ps.setDouble(5,Utilidades.convertirADouble(tiposconvenio.getCodigoCuentaContable()+""));
			}	
			ps.setString(6,tiposconvenio.getUsuarioModifica());
			ps.setDate(7, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			ps.setString(8, UtilidadFecha.getHoraActual());
			ps.setInt(9, tiposconvenio.getCodigoClasificacion() );
			
			if(tiposconvenio.getRubroPresupuestal()<1)
			{
				ps.setNull(10, Types.NUMERIC);
			}
			else
			{	
				ps.setDouble(10,Utilidades.convertirADouble(tiposconvenio.getRubroPresupuestal()+""));
			}
			
			if(tiposconvenio.getCuentaValConvenio()<1)
				ps.setNull(11, Types.NUMERIC);
			else
				ps.setInt(11,Utilidades.convertirAEntero(tiposconvenio.getCuentaValConvenio()+""));
			
			if(tiposconvenio.getCuentaValPaciente()<1)
				ps.setNull(12, Types.NUMERIC);
			else
				ps.setInt(12,Utilidades.convertirAEntero(tiposconvenio.getCuentaValPaciente()+""));
			
			if(tiposconvenio.getCuentaDescPaciente()<1)
				ps.setNull(13, Types.NUMERIC);
			else
				ps.setInt(13,Utilidades.convertirAEntero(tiposconvenio.getCuentaDescPaciente()+""));
			
			if(tiposconvenio.getCuentaCxcCapitacion()<1)
				ps.setNull(14, Types.NUMERIC);
			else
				ps.setInt(14,Utilidades.convertirAEntero(tiposconvenio.getCuentaCxcCapitacion()+""));
			
			if(tiposconvenio.getCuentaContableUtil()<1)
				ps.setNull(15, Types.NUMERIC);
			else
				ps.setInt(15,Utilidades.convertirAEntero(tiposconvenio.getCuentaContableUtil()+""));
			
			if(tiposconvenio.getCuentaContablePerd()<1)
				ps.setNull(16, Types.NUMERIC);
			else
				ps.setInt(16,Utilidades.convertirAEntero(tiposconvenio.getCuentaContablePerd()+""));
			
			if(tiposconvenio.getCuentaDebitoGlsRecibida()<1)
				ps.setNull(17, Types.NUMERIC);
			else
				ps.setInt(17,Utilidades.convertirAEntero(tiposconvenio.getCuentaDebitoGlsRecibida()+""));
			
			if(tiposconvenio.getCuentaCreditoGlsRecibida()<1)
				ps.setNull(18, Types.NUMERIC);
			else
				ps.setInt(18,Utilidades.convertirAEntero(tiposconvenio.getCuentaCreditoGlsRecibida()+""));
			
			if(tiposconvenio.getCuentaDebitoGlsAceptada()<1)
				ps.setNull(19, Types.NUMERIC);
			else
				ps.setInt(19,Utilidades.convertirAEntero(tiposconvenio.getCuentaDebitoGlsAceptada()+""));
			
			if(tiposconvenio.getCuentaCreditoGlsAceptada()<1)
				ps.setNull(20, Types.NUMERIC);
			else
				ps.setInt(20,Utilidades.convertirAEntero(tiposconvenio.getCuentaCreditoGlsAceptada()+""));
			ps.setString(21, tiposconvenio.getAseguradoraAtEv());
			if(ps.executeUpdate()>0)
			  return true;	
			
		}
		catch(SQLException sqe){
			Log4JManager.error(sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
		}
		finally{
			try{
				if(ps != null){
					ps.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("Error close PreparedStatement ", se);
			}
		}
		return false;
	}
	
	/**
	 * Modifica un tipo de convenio registrado
	 * @param con
	 * @param TiposConvenio tiposconvenio
	 */
	public static boolean modificarTiposConvenio(Connection con, TiposConvenio tiposconvenio, String codigoAntesMod)
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaModificarStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE tipos_convenio SET 
			 * codigo=?, 
			 * descripcion=?, 
			 * tipo_regimen=?, 
			 * cuenta_contable=?, 
			 * usuario_modifica=?, 
			 * fecha_modifica=?, 
			 * hora_modifica=?, 
			 * clasificacion=?
			 * rubro_presupuestal=? 
			 * WHERE codigo=? AND institucion=?
			 */
			
			ps.setString(1,tiposconvenio.getCodigo());
			ps.setString(2,tiposconvenio.getDescripcion());
			ps.setString(3,tiposconvenio.getAcronimoTipoRegimen());
			if(tiposconvenio.getCodigoCuentaContable()<1)
			{
				ps.setNull(4, Types.NUMERIC);
			}
			else
			{
				ps.setDouble(4,Utilidades.convertirADouble(tiposconvenio.getCodigoCuentaContable()+""));
			}	
			ps.setString(5,tiposconvenio.getUsuarioModifica());
			ps.setDate(6,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			ps.setString(7,UtilidadFecha.getHoraActual());
			ps.setInt(8, tiposconvenio.getCodigoClasificacion());
			
			if(tiposconvenio.getRubroPresupuestal()<1)
			{
				ps.setNull(9, Types.NUMERIC);
			}
			else
			{
				ps.setDouble(9,Utilidades.convertirADouble(tiposconvenio.getRubroPresupuestal()+""));
			}
			
			if(tiposconvenio.getCuentaValConvenio()<1)
				ps.setNull(10, Types.NUMERIC);
			else
				ps.setInt(10,Utilidades.convertirAEntero(tiposconvenio.getCuentaValConvenio()+""));
			
			if(tiposconvenio.getCuentaValPaciente()<1)
				ps.setNull(11, Types.NUMERIC);
			else
				ps.setInt(11,Utilidades.convertirAEntero(tiposconvenio.getCuentaValPaciente()+""));
			
			if(tiposconvenio.getCuentaDescPaciente()<1)
				ps.setNull(12, Types.NUMERIC);
			else
				ps.setInt(12,Utilidades.convertirAEntero(tiposconvenio.getCuentaDescPaciente()+""));
			
			if(tiposconvenio.getCuentaCxcCapitacion()<1)
				ps.setNull(13, Types.NUMERIC);
			else
				ps.setInt(13,Utilidades.convertirAEntero(tiposconvenio.getCuentaCxcCapitacion()+""));
			
			if(tiposconvenio.getCuentaContableUtil()<1)
				ps.setNull(14, Types.NUMERIC);
			else
				ps.setInt(14,Utilidades.convertirAEntero(tiposconvenio.getCuentaContableUtil()+""));
			
			if(tiposconvenio.getCuentaContablePerd()<1)
				ps.setNull(15, Types.NUMERIC);
			else
				ps.setInt(15,Utilidades.convertirAEntero(tiposconvenio.getCuentaContablePerd()+""));
			
			if(tiposconvenio.getCuentaDebitoGlsRecibida()<1)
				ps.setNull(16, Types.NUMERIC);
			else
				ps.setInt(16,Utilidades.convertirAEntero(tiposconvenio.getCuentaDebitoGlsRecibida()+""));
			
			if(tiposconvenio.getCuentaCreditoGlsRecibida()<1)
				ps.setNull(17, Types.NUMERIC);
			else
				ps.setInt(17,Utilidades.convertirAEntero(tiposconvenio.getCuentaCreditoGlsRecibida()+""));
			
			if(tiposconvenio.getCuentaDebitoGlsAceptada()<1)
				ps.setNull(18, Types.NUMERIC);
			else
				ps.setInt(18,Utilidades.convertirAEntero(tiposconvenio.getCuentaDebitoGlsAceptada()+""));
			
			if(tiposconvenio.getCuentaCreditoGlsAceptada()<1)
				ps.setNull(19, Types.NUMERIC);
			else
				ps.setInt(19,Utilidades.convertirAEntero(tiposconvenio.getCuentaCreditoGlsAceptada()+""));
			
			ps.setString(20,tiposconvenio.getAseguradoraAtEv());
			
			ps.setString(21,codigoAntesMod);
			ps.setInt(22,tiposconvenio.getCodigoInstitucion());
			
			if(ps.executeUpdate() >0)
				return true;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Elimina un tipo de convenio registrado
	 * @param con
	 * @param TiposConvenio tiposconvenio
	 */
	public static boolean eliminarTiposConvenio(Connection con, String codigo, int institucion)
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaEliminarStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,codigo);
			ps.setInt(2, institucion);
			
			if(ps.executeUpdate() > 0)
				return true;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static HashMap consultarTiposConvenio(Connection con, int codigoInstitucion) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		try
		{
			logger.info("consultarTiposConvenioStr-->"+consultarTiposConvenioStr+" institucion->"+codigoInstitucion);
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultarTiposConvenioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoInstitucion);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @param codigo
	 * @return
	 */
	public static HashMap consultarTiposConvenioEspecifico(Connection con, int codigoInstitucion, String codigo)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			logger.info("consultarTiposConvenioEspecificoStr-->"+consultarTiposConvenioEspecificoStr+" institucion->"+codigoInstitucion);
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultarTiposConvenioEspecificoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codigo);
			ps.setInt(2, codigoInstitucion);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}

	/**
	 * Metodo para eliminar un Rubro de un registro
	 * @param con
	 * @param codigoEliminar
	 * @return
	 */
	public static boolean eliminarRubro(Connection con, int codigoEliminar) 
	{
		logger.info("Codigo-->"+codigoEliminar+"<-");
		String cadenaStr="";
		
		cadenaStr = "UPDATE tipos_convenio SET rubro_presupuestal = ? WHERE codigo = ?";
		
		logger.info("CADENA --->\n\n"+cadenaStr+"\n");
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setNull(1, Types.VARCHAR);
			ps.setInt(2, codigoEliminar);
			
			int resultado = ConstantesBD.codigoNuncaValido;
				resultado= ps.executeUpdate();
			
			if(resultado!=ConstantesBD.codigoNuncaValido)
			{
				return true;
			}	
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return false;
	}
	
}
