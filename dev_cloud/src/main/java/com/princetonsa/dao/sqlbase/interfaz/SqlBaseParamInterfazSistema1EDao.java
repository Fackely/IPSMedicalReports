package com.princetonsa.dao.sqlbase.interfaz;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.interfaz.DtoConceptosParam1E;
import com.princetonsa.dto.interfaz.DtoEventosParam1E;
import com.princetonsa.dto.interfaz.DtoInterfazParamContaS1E;
import com.princetonsa.dto.interfaz.DtoInterfazS1EInfo;
import com.princetonsa.dto.interfaz.DtoLogParamGenerales1E;
import com.princetonsa.dto.interfaz.DtoTiposInterfazDocumentosParam1E;

public class SqlBaseParamInterfazSistema1EDao
{
	/**
	 * Manejador de log de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseParamInterfazSistema1EDao.class);
	
	private static final String insertarParamGenerales=		"INSERT INTO " +
																"interfaz.param_generales_1e " +
																"(" +
																"consecutivo, " +
																"path_archivo_interfaz, " +
																"path_archivo_incon," +
																"tercero_gen_fac_par, " +
																"tercero_gen_pag_pac, " +
																"cuenta_abono_pac, " +
																"cod_con_fl_efe_mov_cxp," +
																"cod_con_fl_efe_mov_con, " +
																"realizar_cal_reten_cxh, " +
																"realizar_cal_reten_cxes, " +
																"realizar_cal_autoret_fp, " +
																"realizar_cal_autoret_fv, " +
																"fecha_control_desmarcar, " +
																"documento_cruce_hi, " +
																"cen_aten_cont_adm, " +
																"institucion, " +
																"fecha_modifica, " +
																"hora_modifica, " +
																"usuario_modifica, " +
																"realizar_cal_reten_cxda, " +
																"realizar_cal_autoret_cxcc, " +
																"centro_costo_tesoreria) " +
															"VALUES " +
																"(" +
																"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?" +
																")";
	
	private static final String consultarParamGenerales= 	"SELECT "+
																"pge.consecutivo, " +
																"pge.path_archivo_interfaz AS patharchivointerfaz, " +
																"pge.path_archivo_incon AS patharchivoincon," +
																"coalesce(pge.tercero_gen_fac_par,"+ConstantesBD.codigoNuncaValido+") AS tercerogenfac, " +
																"coalesce(pge.tercero_gen_pag_pac,"+ConstantesBD.codigoNuncaValido+") AS tercerogenpag, " +
																"coalesce(tfp.numero_identificacion,'') as idtercerogenfac, " +
																"coalesce(tpp.numero_identificacion,'') as idtercerogenpag, " +
																"coalesce(tfp.descripcion,'') as desctercerogenfac, " +
																"coalesce(tpp.descripcion,'') as desctercerogenpag, " +
																"pge.cuenta_abono_pac AS cuentaabono, " +
																"pge.cod_con_fl_efe_mov_cxp AS codconcxp," +
																"pge.cod_con_fl_efe_mov_con AS codconcon, " +
																"pge.realizar_cal_reten_cxh AS realizarcalcxh, " +
																"pge.realizar_cal_reten_cxes AS realizarcalcxes, " +
																"pge.realizar_cal_autoret_fp AS realizarcalfp, " +
																"pge.realizar_cal_autoret_fv AS realizarcalfv, " +
																"to_char(pge.fecha_control_desmarcar,'yyyy-mm-dd') AS fechacontrol, " +
																"pge.documento_cruce_hi AS documentocruce, " +
																"pge.cen_aten_cont_adm AS centroatencion, " +
																"coalesce(ca.codigo,'') as codigocentroatencion,  " +
																"coalesce(ca.descripcion,'') as descripcioncentroatencion,  " +
																"pge.institucion, " +
																"to_char(pge.fecha_modifica,'yyyy-mm-dd') AS fechamodi, " +
																"pge.hora_modifica AS horamodi, " +
																"pge.usuario_modifica AS usuariomodi, " +
																"coalesce(pge.realizar_cal_reten_cxda,'') AS re_cal_reten_cxda, " +
																"coalesce(pge.realizar_cal_autoret_cxcc,'') AS re_cal_autoret_cxcc," +
																"coalesce(unid_mov_cxc_cap,'') AS unid_mov_cxc_cap, " +
																"cc.codigo AS codigo_centro_costo, " +
																"coalesce(cc.codigo_interfaz,'') AS codigo_interfaz_centro_costo, " +
																"coalesce(cc.nombre,'') AS centro_costo_tesoreria " +
//																"pge.centro_atencion_c_dev_rc AS centrorecibocaja, " +
//																"coalesce(car.codigo,'') as codigocentroatencionc,  " +
//																"coalesce(car.descripcion,'') as desccentroatencionc,  " +
//																"pge.centro_atencion_c_reg_glo AS centroglosa, " +
//																"coalesce(cag.codigo,'') as codigocentroatenciong,  " +
//																"coalesce(cag.descripcion,'') as desccentroatenciong  " +
															"FROM  " +
																"interfaz.param_generales_1e pge " +
//															"LEFT OUTER JOIN centro_atencion car ON(car.consecutivo = pge.centro_atencion_c_dev_rc) " +
//															"LEFT OUTER JOIN centro_atencion cag ON(cag.consecutivo = pge.centro_atencion_c_reg_glo) " +
															"LEFT OUTER JOIN centro_atencion ca ON(ca.consecutivo = pge.cen_aten_cont_adm) " +
															"LEFT OUTER JOIN terceros tfp on (tfp.codigo = pge.tercero_gen_fac_par) " +
															"LEFT OUTER JOIN terceros tpp on (tpp.codigo = pge.tercero_gen_pag_pac) " +
															"LEFT OUTER JOIN centros_costo cc on (cc.codigo = pge.centro_costo_tesoreria)";
	
	private static final 	String actualizarParamGenerales=	"UPDATE " +
																	"param_generales_1e " +
																"SET " +
																	"path_archivo_interfaz=?, " +
																	"path_archivo_incon=?, "+
																	"tercero_gen_fac_par=?, "+
																	"tercero_gen_pag_pac=?, "+
																	"cuenta_abono_pac=?, "+
																	"cod_con_fl_efe_mov_cxp=?, "+
																	"cod_con_fl_efe_mov_con=?, "+
																	"realizar_cal_reten_cxh=?, "+
																	"realizar_cal_reten_cxes=?, "+
																	"realizar_cal_autoret_fp=?, "+
																	"realizar_cal_autoret_fv=?, "+
																	"fecha_control_desmarcar=?, "+
																	"documento_cruce_hi=?, "+
																	"cen_aten_cont_adm=?,"+
																	"institucion=?,"+
																	"fecha_modifica=?,"+
																	"hora_modifica=?, "+
																	"usuario_modifica=?, " +
																	"realizar_cal_reten_cxda = ? , " +
																	"realizar_cal_autoret_cxcc = ?, " +
																	"centro_costo_tesoreria = ? " +
//																	"centro_atencion_c_dev_rc = ?," +
//																	"centro_atencion_c_reg_glo = ? " +
																"WHERE " +
																	"consecutivo=?";
	
	private static final String insertarTiposDocumentosParam=	"INSERT INTO " +
																	"interfaz.tipos_documentos_param_1e " +
																"(" +
																"consecutivo, " +
																"param_generales, " +
																"tipo_documento," +
																"tipo_consecutivo, " +
																"ind_tipo_documento, " +
																"tipo_documento_cruce, " +
																"unidad_funcional_estandar," +
																"institucion, " +
																"observaciones_encabezado, " +
																"fecha, " +
																"hora, " +
																"usuario," +
																"activo," +
																"tipo_movimiento " +
																") " +
																"VALUES " +
																"(?,?,?,?,?,?,?,?,?,?,?,?,?,?)" ;
	
	private static final String consultarTiposDoc=	"SELECT " +
														"tdp.consecutivo, " +
														"tdp.param_generales AS paramgen, " +
														"tdp.tipo_documento AS tipodoc," +
														"tdp.tipo_consecutivo AS tipocon, " +
														"tdp.ind_tipo_documento AS indtipodoc, " +
														"tdp.tipo_documento_cruce AS tipodoccruce, " +
														"tdp.unidad_funcional_estandar AS unidadfunestandar," +
														"tdp.institucion, " +
														"tdp.observaciones_encabezado AS observencabezado, " +
														"to_char(tdp.fecha,'yyyy-mm-dd') as fecha, " +
														"tdp.hora, " +
														"tdp.usuario," +
														"tdp.activo," +
														"tdp.tipo_movimiento AS tipomovimiento, " +
														"coalesce(tc1e.nombre,'') AS nombre_tipocon, " +
														"tc1e.nombre AS nombreconsecutivo, " +
														"td1e.nombre AS nombredocumento, " +
														"uf.descripcion AS descunidadfun, " +
														"pg1e.unid_mov_cxc_cap AS unidadfunparamgeneral " +
													"FROM " +
														"interfaz.tipos_documentos_param_1e tdp " +
														"INNER JOIN interfaz.param_generales_1e pg1e ON (pg1e.consecutivo = tdp.param_generales) " +
													"LEFT OUTER JOIN interfaz.tipos_consecutivo_1e tc1e ON (tc1e.codigo = tdp.tipo_consecutivo ) " +
													"LEFT OUTER JOIN interfaz.tipos_documento_1e td1e ON (td1e.codigo = tdp.tipo_documento ) "+
													"LEFT OUTER JOIN unidades_funcionales uf ON (uf.acronimo = tdp.unidad_funcional_estandar ) " +
													"WHERE tdp.activo = '"+ConstantesBD.acronimoSi+"' ";
	
	private static final String consultarTipoConsecutivoXTipoDoc=	"SELECT " +
																		"tdtc.tipo_documento AS coddoc," +
																		"tdtc.tipo_consecutivo AS codcon," +
																		"tc.nombre AS nomcon " +																		
																	"FROM " +
																		"interfaz.tipo_doc_tipo_con_1e tdtc " +
																	"INNER JOIN " +
																		"interfaz.tipos_consecutivo_1e tc ON (tc.codigo=tdtc.tipo_consecutivo) " +
																	"WHERE " +
																		"tdtc.tipo_documento=? ";
	
	private static final String consultarTiposDoc1E=	"SELECT " +
															"td.codigo, " +
															"td.nombre " +
														"FROM " +
															"interfaz.tipos_documento_1e td " +
														"ORDER BY " +
															"td.nombre ASC";
	
	private static final String eliminarTiposDoc1E=	"DELETE FROM  " +
														"interfaz.tipos_documentos_param_1e " +
													"WHERE " +
														"consecutivo=?";
	
	private static final String consultarInfoTipoDocUnitario=	"SELECT " +
																	"tdp.consecutivo, " +
																	"tdp.param_generales AS paramgen, " +
																	"tdp.tipo_documento AS tipodoc," +
																	"tdp.tipo_consecutivo AS tipocon, " +
																	"tdp.ind_tipo_documento AS indtipodoc, " +
																	"tdp.tipo_documento_cruce AS tipodoccruce, " +
																	"tdp.unidad_funcional_estandar AS unidadfunestandar," +
																	"tdp.institucion, " +
																	"tdp.observaciones_encabezado AS observencabezado, " +
																	"to_char(tdp.fecha,'yyyy-mm-dd') as fecha, " +
																	"tdp.hora, " +
																	"tdp.usuario," +
																	"tdp.activo," +
																	"coalesce(tdp.tipo_movimiento,'') AS tipomovimiento," +
																	"coalesce(tc1e.nombre,'') AS nombre_tipocon, " +
																	"tc1e.nombre AS nombreconsecutivo, " +
																	"td1e.nombre AS nombredocumento, " +
																	"uf.descripcion AS descunidadfun " +
																"FROM " +
																	"interfaz.tipos_documentos_param_1e tdp " +
																"LEFT OUTER JOIN interfaz.tipos_consecutivo_1e tc1e ON (tc1e.codigo = tdp.tipo_consecutivo ) " +
																"LEFT OUTER JOIN interfaz.tipos_documento_1e td1e ON (td1e.codigo = tdp.tipo_documento ) "+
																"LEFT OUTER JOIN unidades_funcionales uf ON (uf.acronimo = tdp.unidad_funcional_estandar ) "+
																"WHERE " +
																	"tdp.consecutivo=?";
	
	private static final String actualizarInactivarDoc=	"UPDATE " +
															"interfaz.tipos_documentos_param_1e " +
														"SET " +
															"activo='"+ConstantesBD.acronimoNo+"' ," +
															"fecha_inactivacion=?," +
															"hora_inactivacion=?," +
															"usuario_inactivacion=? " +
														"WHERE " +
															"consecutivo=? ";
	
	private static final String insertarParamGeneralesLog=	"INSERT INTO " +
																"interfaz.log_param_generales_1e " +
																"(" +
																"consecutivo, " +
																"param_generales," +
																"path_archivo_interfaz, " +
																"path_archivo_incon," +
																"tercero_gen_fac_par, " +
																"tercero_gen_pag_pac, " +
																"cuenta_abono_pac, " +
																"cod_con_fl_efe_mov_cxp," +
																"cod_con_fl_efe_mov_con, " +
																"realizar_cal_reten_cxh, " +
																"realizar_cal_reten_cxes, " +
																"realizar_cal_autoret_fp, " +
																"realizar_cal_autoret_fv, " +
																"fecha_control_desmarcar, " +
																"documento_cruce_hi, " +
																"cen_aten_cont_adm, " +
																"institucion, " +
																"fecha_modifica, " +
																"hora_modifica, " +
																"usuario_modifica, " +
																"realizar_cal_reten_cxda, " +
																"realizar_cal_autoret_cxcc " +
//																"centro_atencion_c_dev_rc," +
//																"centro_atencion_c_reg_glo " +
																") " +
															"VALUES " +
																"(" +
																"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?" +
																")";
					
	
	/**
	 * Cadena de Insercion de Concepto Param 1E 
	 */
	private static final String strInsertConceptoParam1E = "INSERT INTO interfaz.conceptos_param_1e(" +
			"consecutivo, " +
			"clase_documento, " +
			"seccion, " +
			"concepto, " +
			"fecha_modifica, " +
			"hora_modifica, " +
			"usuario_modifica, " +
			"activo, " +
			"param_generales_1e)" +
			"VALUES(?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?,?,?)";
	/**
	 * Cadena de Inactivacion de Concepto Param 1E
	 */
	private static final String strInactivarConceptoParam1E = "UPDATE interfaz.conceptos_param_1e SET " +
			"activo = '"+ConstantesBD.acronimoNo+"', fecha_inactivacion = CURRENT_DATE, " +
			"hora_inactivacion = "+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
			"usuario_inactivacion = ? WHERE consecutivo = ? ";
	
	/**
	 * Cadena Consultar Concepto Param 1E
	 */
	private static final String strConsultaConceptoParam1E = "SELECT " +
			"cp1.consecutivo AS consecutivo, " +
			"cp1.clase_documento AS clase_documento, " +
			"cp1.seccion AS seccion, " +
			"cp1.concepto AS concepto, " +
			"cp1.usuario_modifica AS usu_mod, " +
			"cp1.activo AS activo, " +
			"cp1.usuario_inactivacion AS usu_anu, " +
			"coalesce(cr.descripcion_concepto,'') AS des_conp_reten " +
			"FROM interfaz.conceptos_param_1e cp1 " +
			"LEFT OUTER JOIN administracion.conceptos_retencion cr ON (cr.consecutivo = cp1.concepto) " +
			"WHERE cp1.param_generales_1e = ? AND cp1.activo= '"+ConstantesBD.acronimoSi+"' ";
	
	private static final String insertarEventosParam1E=		"INSERT INTO " +
																"interfaz.eventos_param_1e " +
																"(" +
																"consecutivo," +
																"param_generales_1e," +
																"evento," +
																"codigo_evento," +
																"notas_evento," +
																"activo," +
																"fecha," +
																"hora," +
																"usuario" +
																") " +
															"VALUES " +
																"(" +
																"?,?,?,?,?,?,?,?,?" +
																")"
																;
	
	private static final String consultarEventosParam1E	=	"SELECT " +
																"e.consecutivo," +
																"e.param_generales_1e," +
																"e.evento," +
																"e.codigo_evento," +
																"e.notas_evento," +
																"e.activo," +
																"ec.nombre," +
																"ec.codigo " +
															"FROM " +
																"interfaz.eventos_param_1e e " +
															"INNER JOIN " +
																"interfaz.eventos_cartera_glosas ec ON (ec.codigo=e.evento) " +
															"WHERE " +
																"activo='"+ConstantesBD.acronimoSi+"' " ;
	
	private static final String inactivarEvento	=	"UPDATE " +
														"interfaz.eventos_param_1e " +
													"SET " +
														"activo='"+ConstantesBD.acronimoNo+"', " +
														"fecha_inactivacion=?," +
														"hora_inactivacion=?," +
														"usuario_inactivacion=? " +
													"WHERE " +
														"consecutivo=? ";
	
	private static final String validarEventoUnico	=	"SELECT " +
															"consecutivo " +
														"FROM " +
															"interfaz.eventos_param_1e " +
														"WHERE " +
															"evento=? " +
														"AND " +
															"activo='"+ConstantesBD.acronimoSi+"'";
	
	/**
	 * 
	 */
	private static final String consultaTiposConsecutivos = "SELECT " +
			"tc.codigo as codigo, " +
			"tc.nombre as nombre " +
			"FROM interfaz.tipo_doc_tipo_con_1e tdtc " +
			"INNER JOIN interfaz.tipos_consecutivo_1e tc ON (tc.codigo = tdtc.tipo_consecutivo) " +
			"WHERE tdtc.tipo_documento = ? " +
			"ORDER BY tc.nombre "; 
																
	
	/**
	 * Insercion Concepto Param 1E
	 * @param con
	 * @param DtoConceptosParam1E
	 * @return
	 */
	public static int insertarConceptoParam1E(Connection con, DtoConceptosParam1E dto)
	{
		try
		{
			//logger.info("SQL ingresandoConceptoParam1E !!!!!!\n La Consulta: "+strInsertConceptoParam1E);
			PreparedStatementDecorator pst=new PreparedStatementDecorator(con.prepareStatement(strInsertConceptoParam1E,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			int cosecutivo = UtilidadBD.obtenerSiguienteValorSecuencia(con,"interfaz.seq_conceptos_param_1e");
			pst.setInt(1, cosecutivo);
			pst.setString(2, dto.getClaseDocumento());
			pst.setString(3, dto.getSeccion());
			if(dto.getCodigoConceptoRetencion()!=ConstantesBD.codigoNuncaValido)
				pst.setInt(4, dto.getCodigoConceptoRetencion());
			else
				pst.setNull(4, Types.INTEGER);
			pst.setString(5, dto.getUsuarioModifica());
			pst.setString(6, dto.getActivo());
			pst.setInt(7, dto.getCodigoParamGeneral1E());
			if(pst.executeUpdate()>0){
				pst.close();
				return cosecutivo;
			}
		}catch(SQLException e){
			logger.info("Error ingresandoConceptoParam1E !!!!!!\n La Consulta: "+strInsertConceptoParam1E);
			
			logger.error(e);
			return ConstantesBD.codigoNuncaValido;
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * Inactivacion Concepto Param 1E
	 * @param con
	 * @param DtoConceptosParam1E
	 * @return
	 */
	public static int inactivarConceptoParam1E(Connection con, DtoConceptosParam1E dto)
	{
		try
		{
			//logger.info("SQL inactivacionConceptoParam1E !!!!!!\n La Consulta: "+strInactivarConceptoParam1E);
			PreparedStatementDecorator pst=new PreparedStatementDecorator(con.prepareStatement(strInactivarConceptoParam1E,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1, dto.getUsuarioAnulacion());
			pst.setInt(2, dto.getConsecutivo());
			if(pst.executeUpdate()>0){
				pst.close();
				return 1;
			}
		}catch(SQLException e){
			logger.info("Error inactivacionConceptoParam1E !!!!!!\n La Consulta: "+strInactivarConceptoParam1E);
			logger.error(e);
			return ConstantesBD.codigoNuncaValido;
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * Consulta Listado Concepto Param 1E 
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public static ArrayList<DtoConceptosParam1E> cargarConceptosparam1E(Connection con, HashMap parametros) 
	{
		ArrayList<DtoConceptosParam1E> list = new ArrayList<DtoConceptosParam1E>();
		try
		{
			//logger.info("SQL caragarConceptoParam1E !!!!!!\n La Consulta: "+strConsultaConceptoParam1E);
			//logger.info("datos >>>>> param_general_1E="+parametros.get("param_generales_1e"));
			PreparedStatementDecorator pst=new PreparedStatementDecorator(con.prepareStatement(strConsultaConceptoParam1E,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, Utilidades.convertirAEntero(parametros.get("param_generales_1e")+""));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			while(rs.next())
			{		
				DtoConceptosParam1E dtoConpParam1E = new DtoConceptosParam1E(); 
				dtoConpParam1E.setConsecutivo(rs.getInt("consecutivo"));
				dtoConpParam1E.setClaseDocumento(rs.getString("clase_documento"));
				dtoConpParam1E.setSeccion(rs.getString("seccion"));
				dtoConpParam1E.setCodigoConceptoRetencion(rs.getInt("concepto"));
				dtoConpParam1E.setUsuarioModifica(rs.getString("usu_mod"));
				dtoConpParam1E.setActivo(rs.getString("activo"));
				dtoConpParam1E.setUsuarioModifica(rs.getString("usu_anu"));
				dtoConpParam1E.setDesConceptoRetencion(rs.getString("des_conp_reten"));
				list.add(dtoConpParam1E);
			}
			rs.close();
			pst.close();
		}catch(SQLException e){
			logger.info("Error caragarConceptoParam1E !!!!!!\n La Consulta: "+strConsultaConceptoParam1E);
			logger.info("datos >>>>> param_general_1E="+parametros.get("param_generales_1e"));
			logger.error("error",e);			
		}
		return list;
	}
	
	public static boolean ingresarParamGenerales(Connection con, DtoInterfazParamContaS1E dtoInterfazSis1E)
	{
		boolean transaccionExitosa=false;
		try {
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(insertarParamGenerales, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));		
				ps.setInt(1,UtilidadBD.obtenerSiguienteValorSecuencia(con, "interfaz.seq_param_generales_1e"));
				ps.setString(2,dtoInterfazSis1E.getPathArchivoInterfaz());
				ps.setString(3,dtoInterfazSis1E.getPathArchivoInconsis());
				
				if (!UtilidadTexto.isEmpty(dtoInterfazSis1E.getTerceroGenFacPar()))
					ps.setInt(4,Utilidades.convertirAEntero(dtoInterfazSis1E.getTerceroGenFacPar()));
				else
					ps.setNull(4, Types.INTEGER);
				
				if (!UtilidadTexto.isEmpty(dtoInterfazSis1E.getTerceroGenPagPac()))
					ps.setInt(5,Utilidades.convertirAEntero(dtoInterfazSis1E.getTerceroGenPagPac()));
				else
					ps.setNull(5, Types.INTEGER);
				
				if (!dtoInterfazSis1E.getCuentaAbonoPac().equals(ConstantesBD.codigoNuncaValido+""))
					ps.setDouble(6, Utilidades.convertirADouble(dtoInterfazSis1E.getCuentaAbonoPac()));
				else
					ps.setNull(6, Types.DOUBLE);
				
				
				ps.setString(7,dtoInterfazSis1E.getCodConFlEfeMovCxp());
				ps.setString(8,dtoInterfazSis1E.getCodConFlEfeMovCon());
				ps.setString(9,dtoInterfazSis1E.getRealizarCalRetenCxh());
				ps.setString(10,dtoInterfazSis1E.getRealizarCalRestenCxes());
				ps.setString(11,dtoInterfazSis1E.getRealizarCalAutoretFp());
				ps.setString(12,dtoInterfazSis1E.getRealizarCalAutoretFv());
				
				if (!dtoInterfazSis1E.getFechaControlDesmarcar().equals(""))
					ps.setString(13,UtilidadFecha.conversionFormatoFechaABD(dtoInterfazSis1E.getFechaControlDesmarcar()));
				else
					ps.setNull(13, Types.VARCHAR);
					
				ps.setString(14,dtoInterfazSis1E.getDocumentoCruceHi());
					
				if (!UtilidadTexto.isEmpty(dtoInterfazSis1E.getCentroAtencionContable()))
					ps.setInt(15,Utilidades.convertirAEntero(dtoInterfazSis1E.getCentroAtencionContable()));
				else
					ps.setNull(15, Types.INTEGER);
					
				ps.setInt(16,Utilidades.convertirAEntero(dtoInterfazSis1E.getInstitucion()));
				ps.setString(17,UtilidadFecha.conversionFormatoFechaABD(dtoInterfazSis1E.getFechaModifica()));
				ps.setString(18,dtoInterfazSis1E.getHoraModifica());
				ps.setString(19,dtoInterfazSis1E.getUsuarioModifica());
				
				
				if(!dtoInterfazSis1E.getRealizarCalRetenCxda().equals(""))
					ps.setString(20, dtoInterfazSis1E.getRealizarCalRetenCxda());
				else
					ps.setNull(20, Types.VARCHAR);
				
				if(!dtoInterfazSis1E.getRealizarCalAutoretCxCC().equals(""))
					ps.setString(21, dtoInterfazSis1E.getRealizarCalAutoretCxCC());
				else
					ps.setNull(21,Types.VARCHAR);
				
				if(!dtoInterfazSis1E.getCentroCostoTesoreria().equals(ConstantesBD.codigoNuncaValido+""))
					ps.setString(22, dtoInterfazSis1E.getCodigoCentroCostoTesoreria());
				else
					ps.setNull(22,Types.VARCHAR);
				
//				if (!UtilidadTexto.isEmpty(dtoInterfazSis1E.getCentroAtencionContableDRC()))
//					ps.setInt(22,Utilidades.convertirAEntero(dtoInterfazSis1E.getCentroAtencionContableDRC()));
//				else
//					ps.setNull(22, Types.INTEGER);
//				
//				if (!UtilidadTexto.isEmpty(dtoInterfazSis1E.getCentroAtencionContableDRC()))
//					ps.setInt(23,Utilidades.convertirAEntero(dtoInterfazSis1E.getCentroAtencionContableRG()));
//				else
//					ps.setNull(23, Types.INTEGER);
				
				
				if(ps.executeUpdate()>0)
					transaccionExitosa=true;
				else
					transaccionExitosa=false;
			}
			catch (SQLException e) 
			{	
				logger.info("ERROR / ingresarEncabezado / "+e);
				return transaccionExitosa;
			}
		
		logger.info("REALICE LA INSERCION DE PARAMETRIZACION INTERFAZ CONTABLE SISTEMA 1E");
		return transaccionExitosa;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static DtoInterfazParamContaS1E consultarParamGenerales(Connection con,HashMap parametros)
	{
		try
		{			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultarParamGenerales,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			
			
			DtoInterfazParamContaS1E dto = new DtoInterfazParamContaS1E();
			if(parametros.containsKey("tipoDto") && parametros.get("tipoDto").equals("S1EInfo"))
				dto = new DtoInterfazS1EInfo();
			
			if(rs.next())
			{
				dto.setConsecutivoPk(rs.getString("consecutivo"));
				dto.setPathArchivoInterfaz(rs.getString("patharchivointerfaz"));
				dto.setPathArchivoInconsis(rs.getString("patharchivoincon"));
				dto.setTerceroGenFacPar(rs.getString("tercerogenfac"));
				dto.setTerceroGenPagPac(rs.getString("tercerogenpag"));
				dto.setNombreTerceroGenPagPac(rs.getString("desctercerogenpag"));
				dto.setNombreTerceroGenFacPar(rs.getString("desctercerogenfac"));
				dto.setNitTerceroGenFacPar(rs.getString("idtercerogenfac"));
				dto.setNitTerceroGenPagPac(rs.getString("idtercerogenpag"));
				dto.setCuentaAbonoPac(rs.getString("cuentaabono"));
				dto.setCodConFlEfeMovCxp(rs.getString("codconcxp"));
				dto.setCodConFlEfeMovCon(rs.getString("codconcon"));
				dto.setRealizarCalRetenCxh(rs.getString("realizarcalcxh"));
				dto.setRealizarCalRestenCxes(rs.getString("realizarcalcxes"));
				dto.setRealizarCalAutoretFp(rs.getString("realizarcalfp"));
				dto.setRealizarCalAutoretFv(rs.getString("realizarcalfv"));
				dto.setFechaControlDesmarcar(UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fechacontrol")));
				dto.setDocumentoCruceHi(rs.getString("documentocruce"));
				dto.setCentroAtencionContable(rs.getString("centroatencion"));
				dto.setCodigoCentroAtencionContable(rs.getString("codigocentroatencion"));
				dto.setDescripcionCentroAtencionContable(rs.getString("descripcioncentroatencion"));
				dto.setInstitucion(rs.getString("institucion"));
				dto.setFechaModifica(rs.getString("fechamodi"));
				dto.setHoraModifica(rs.getString("horamodi"));
				dto.setUsuarioModifica(rs.getString("usuariomodi"));
				dto.setRealizarCalRetenCxda(rs.getString("re_cal_reten_cxda"));
				dto.setRealizarCalAutoretCxCC(rs.getString("re_cal_autoret_cxcc"));
				dto.setUnidMovCxcCap(rs.getString("unid_mov_cxc_cap"));
				dto.setCodigoCentroCostoTesoreria(rs.getString("codigo_centro_costo"));
				dto.setCentroCostoTesoreria(rs.getString("centro_costo_tesoreria"));
				dto.setCodigoInterfazCentroCostoTesoreria(rs.getString("codigo_interfaz_centro_costo"));
//				dto.setCentroAtencionContableDRC(rs.getString("centrorecibocaja"));
//				dto.setCodigoCentroAtencionDRC(rs.getString("codigocentroatencionc"));
//				dto.setDescCentroAtencionDRC(rs.getString("desccentroatencionc"));
//				dto.setCentroAtencionContableRG(rs.getString("centroglosa"));
//				dto.setCodigoCentroAtencionRG(rs.getString("codigocentroatenciong"));
//				dto.setDescCentroAtencionRG(rs.getString("desccentroatenciong"));

				// Anexo 823 Cambios en Funcionalidades 
				if(!dto.getConsecutivoPk().equals(""))
				{
					if(Utilidades.convertirAEntero(dto.getConsecutivoPk())>0)
					{
						HashMap datos = new HashMap();
						datos.put("param_generales_1e", dto.getConsecutivoPk());
						dto.setArrayConceptoParam1E(cargarConceptosparam1E(con, datos));
					}
				}
				// Fin Anexo 823 Cambios en Funcionalidades
				dto.setArrayListDtoTiposDocumentos(consultarTiposDocs(con));
				//Se consultan los eventos
				dto.setEventos(consultarEventosParam1E(con,dto.getConsecutivoPk()));
			}
			logger.info("REALICE LA CONSULTA DE PARAMETRIZACION INTERFAZ CONTABLE SISTEMA 1E------>"+consultarParamGenerales);
			return dto;

		}
		catch (Exception e) 
		{			
			e.printStackTrace();
			logger.info("\n Error en  la consulta de parametrs interfaz contable sistema 1E---> "+consultarParamGenerales+" ");
		}
		return null;
	}

	public static boolean actualizarParamGenerales(Connection con, DtoInterfazParamContaS1E dtoInterfazSis1E)
	{
		boolean transaccionExitosa=false;
			
		try {
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(actualizarParamGenerales, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			ps.setString(1,dtoInterfazSis1E.getPathArchivoInterfaz());
			ps.setString(2,dtoInterfazSis1E.getPathArchivoInconsis());
			
			if (!dtoInterfazSis1E.getTerceroGenFacPar().equals(ConstantesBD.codigoNuncaValido+""))
				ps.setInt(3,Utilidades.convertirAEntero(dtoInterfazSis1E.getTerceroGenFacPar()));
			else
				ps.setNull(3, Types.INTEGER);
			
			if (!dtoInterfazSis1E.getTerceroGenPagPac().equals(ConstantesBD.codigoNuncaValido+""))
				ps.setInt(4,Utilidades.convertirAEntero(dtoInterfazSis1E.getTerceroGenPagPac()));
			else
				ps.setNull(4, Types.INTEGER);
			
			if (!UtilidadTexto.isEmpty(dtoInterfazSis1E.getCuentaAbonoPac()))
				ps.setDouble(5,Utilidades.convertirADouble(dtoInterfazSis1E.getCuentaAbonoPac()));
			else
				ps.setNull(5, Types.DOUBLE);
			
			ps.setString(6,dtoInterfazSis1E.getCodConFlEfeMovCxp());
			ps.setString(7,dtoInterfazSis1E.getCodConFlEfeMovCon());
			ps.setString(8,dtoInterfazSis1E.getRealizarCalRetenCxh());
			ps.setString(9,dtoInterfazSis1E.getRealizarCalRestenCxes());
			ps.setString(10,dtoInterfazSis1E.getRealizarCalAutoretFp());
			ps.setString(11,dtoInterfazSis1E.getRealizarCalAutoretFv());
			
			logger.info(dtoInterfazSis1E.getFechaControlDesmarcar());
			if (!dtoInterfazSis1E.getFechaControlDesmarcar().equals(""))
				ps.setString(12,UtilidadFecha.conversionFormatoFechaABD(dtoInterfazSis1E.getFechaControlDesmarcar()));
			else
				ps.setNull(12,Types.VARCHAR);
				
			ps.setString(13,dtoInterfazSis1E.getDocumentoCruceHi());
			
			if (!UtilidadTexto.isEmpty(dtoInterfazSis1E.getCentroAtencionContable()))
				ps.setInt(14,Utilidades.convertirAEntero(dtoInterfazSis1E.getCentroAtencionContable()));
			else
				ps.setNull(14, Types.INTEGER);
			
			ps.setInt(15,Utilidades.convertirAEntero(dtoInterfazSis1E.getInstitucion()));
			ps.setString(16,UtilidadFecha.conversionFormatoFechaABD(dtoInterfazSis1E.getFechaModifica()));
			ps.setString(17,dtoInterfazSis1E.getHoraModifica());
			ps.setString(18,dtoInterfazSis1E.getUsuarioModifica());
			
			if(!dtoInterfazSis1E.getRealizarCalRetenCxda().equals(""))
				ps.setString(19, dtoInterfazSis1E.getRealizarCalRetenCxda());
			else
				ps.setNull(19, Types.VARCHAR);
			
			if(!dtoInterfazSis1E.getRealizarCalAutoretCxCC().equals(""))
				ps.setString(20, dtoInterfazSis1E.getRealizarCalAutoretCxCC());
			else
				ps.setNull(20,Types.VARCHAR);
				
			if(!dtoInterfazSis1E.getCodigoCentroCostoTesoreria().equals(ConstantesBD.codigoNuncaValido+""))
				ps.setInt(21,Utilidades.convertirAEntero(dtoInterfazSis1E.getCodigoCentroCostoTesoreria()));
			else
				ps.setNull(21,Types.INTEGER);
			
//			if (!UtilidadTexto.isEmpty(dtoInterfazSis1E.getCentroAtencionContableDRC()))
//				ps.setInt(21,Utilidades.convertirAEntero(dtoInterfazSis1E.getCentroAtencionContableDRC()));
//			else
//				ps.setNull(21, Types.INTEGER);
//			
//			if (!UtilidadTexto.isEmpty(dtoInterfazSis1E.getCentroAtencionContableRG()))
//				ps.setInt(22,Utilidades.convertirAEntero(dtoInterfazSis1E.getCentroAtencionContableRG()));
//			else
//				ps.setNull(22, Types.INTEGER);
			
			ps.setInt(22,Utilidades.convertirAEntero(dtoInterfazSis1E.getConsecutivoPk()));
			
			if(ps.executeUpdate()>0)
				transaccionExitosa = true;
		}
		catch (SQLException e) 
		{	
			logger.info("ERROR / actualizarParamGenerales / "+e);
			transaccionExitosa=false;
		}
		
		return transaccionExitosa;
	}
	
	public static boolean ingresarTiposDoc(Connection con, DtoTiposInterfazDocumentosParam1E dto)
	{
		boolean transaccionExitosa=false;
		try {
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(insertarTiposDocumentosParam, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));		
				ps.setInt(1,UtilidadBD.obtenerSiguienteValorSecuencia(con, "interfaz.seq_tipos_documentos_param_1e"));
				ps.setInt(2,Utilidades.convertirAEntero( dto.getParamGenerales()));
				
				if (!UtilidadTexto.isEmpty(dto.getTipoDocumento()))
					ps.setInt(3,Utilidades.convertirAEntero(dto.getTipoDocumento()));
				else
					ps.setNull(3, Types.INTEGER);
					
				if (!UtilidadTexto.isEmpty(dto.getTipoConsecutivo()))
					ps.setInt(4,Utilidades.convertirAEntero(dto.getTipoConsecutivo()));
				else
					ps.setNull(4, Types.INTEGER);
					
				ps.setString(5,dto.getIndTipoDocumento());
				ps.setString(6,dto.getTipoDocumentoCruce());
				
				if (!UtilidadTexto.isEmpty(dto.getUnidadFuncionalEstandar()))
					ps.setString(7,dto.getUnidadFuncionalEstandar());
				else
					ps.setNull(7, Types.VARCHAR);
					
				ps.setInt(8,Utilidades.convertirAEntero(dto.getInstitucion()));
				ps.setString(9,dto.getObservacionesEncabezado());
				ps.setString(10,UtilidadFecha.conversionFormatoFechaABD( dto.getFecha()));
				ps.setString(11,dto.getHora());
				ps.setString(12,dto.getUsuario());
				ps.setString(13,dto.getActivo());
				
				if (!dto.getTipoMovimiento().equals(""))
					ps.setString(14,dto.getTipoMovimiento());
				else
					ps.setNull(14, Types.VARCHAR);
				
				
				if(ps.executeUpdate()>0)
					transaccionExitosa=true;
				else
					transaccionExitosa=false;
				
			}
			catch (SQLException e) 
			{	
				logger.info("ERROR / insertarTiposDocumentosParam / "+e);
				return transaccionExitosa;
			}
		
		logger.info("REALICE LA INSERCION DE TIPOS DE DOCUMENTOS SE HIZO SATISFACTORIAMENTE");
		return transaccionExitosa;
	}
	
	
	public static ArrayList<DtoTiposInterfazDocumentosParam1E> consultarTiposDocs(Connection con)
	{
		ArrayList<DtoTiposInterfazDocumentosParam1E> arrayTiposDoc=new ArrayList<DtoTiposInterfazDocumentosParam1E>();
		try
		{			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultarTiposDoc,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("valor de la consulta de tipos de docuimento >> "+consultarTiposDoc);
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoTiposInterfazDocumentosParam1E dto= new DtoTiposInterfazDocumentosParam1E();
				dto.setConsecutivoPk(rs.getString("consecutivo"));
				dto.setParamGenerales(rs.getString("paramgen"));
				dto.setTipoDocumento(rs.getString("tipodoc"));
				dto.setTipoConsecutivo(rs.getString("tipocon"));
				dto.setIndTipoDocumento(rs.getString("indtipodoc"));
				dto.setTipoDocumentoCruce(rs.getString("tipodoccruce"));
				dto.setUnidadFuncionalEstandar(rs.getString("unidadfunestandar"));
				dto.setInstitucion(rs.getString("institucion"));
				dto.setObservacionesEncabezado(rs.getString("observencabezado"));
				dto.setFecha(UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fecha")));
				dto.setHora(rs.getString("hora"));
				dto.setUsuario(rs.getString("usuario"));
				dto.setActivo(rs.getString("activo"));
				dto.setTipoMovimiento(rs.getString("tipomovimiento"));
				dto.setNombreConsecutivo(rs.getString("nombreconsecutivo"));
				dto.setNombreDocumento(rs.getString("nombredocumento"));
				dto.setNombreUnidadFuncional(rs.getString("descunidadfun"));
				// TAREA 152926 Agosto 20 - 2010
				dto.setNombreUnidadFuncionalParamGen("");
				dto.setUnidadFuncionalEstandarParamGen(rs.getString("unidadfunparamgeneral"));
				arrayTiposDoc.add(dto);
			}
			
			logger.info("REALICE LA CONSULTA DE TIPOS DE DOCUMENTOS");
			
		}
		catch (Exception e) 
		{			
			e.printStackTrace();
			logger.info("\n Error en  la consulta de tipso de docs 1E---> "+consultarTiposDoc+" ");
		}
		
		return arrayTiposDoc;		
	}
	
	public static ArrayList consultarTiposDoc1E(Connection con)
	{
		logger.info("LA CONSULTA PARA LOS TIPOS DE DOCUMENTOS--->\n"+consultarTiposDoc1E);
		ArrayList<HashMap<String, Object>> tiposDoc = new ArrayList<HashMap<String,Object>>();
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultarTiposDoc1E,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				HashMap<String, Object> mapa =new HashMap<String, Object>();
				mapa.put("codigodoc", rs.getString("codigo"));
				mapa.put("nombredoc", rs.getString("nombre"));
				tiposDoc.add(mapa);
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return tiposDoc;
	}
	
	public static ArrayList consultarTipoConsecutivoXTipoDoc(Connection con,String codigo)
	{
		logger.info("LA CONSULTA PARA LOS TIPOS DE CONSECUTIVO POR TIPO DE DOCUMENTO--->\n"+consultarTipoConsecutivoXTipoDoc);
		
		ArrayList<HashMap<String, Object>> tiposCon = new ArrayList<HashMap<String,Object>>();
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultarTiposDoc1E,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,Utilidades.convertirAEntero(codigo));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				HashMap<String, Object> mapa =new HashMap<String, Object>();
				mapa.put("codigoconsecutivo", rs.getString("codcon"));
				mapa.put("nombreconsecutivo", rs.getString("nomcon"));
				tiposCon.add(mapa);
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return tiposCon;
	}
	
	public static boolean eliminarTiposDoc(Connection con,String indice)
	{

		boolean transaccionExitosa=false;
		try {
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(eliminarTiposDoc1E, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));		
				ps.setInt(1,Utilidades.convertirAEntero(indice));
				if(ps.executeUpdate()>0)
					transaccionExitosa=true;
				else
					transaccionExitosa=false;
				
			}
			catch (SQLException e) 
			{	
				logger.info("ERROR / insertarTiposDocumentosParam / "+e);
				return transaccionExitosa;
			}
		
		logger.info("REALICE LA ELIMINACION DEL TIPO DE DOCUMENTO SATISFACTORIAMENTE");
		return transaccionExitosa;
		
	}
	
	public static DtoTiposInterfazDocumentosParam1E consultarInfoTipoDocUnitario(Connection con, String indice)
	{
		DtoTiposInterfazDocumentosParam1E dto=new DtoTiposInterfazDocumentosParam1E();
		try
		{			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultarInfoTipoDocUnitario,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			ps.setInt(1,Utilidades.convertirAEntero(indice));
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{	
				dto.setConsecutivoPk(rs.getString("consecutivo"));
				dto.setParamGenerales(rs.getString("paramgen"));
				dto.setTipoDocumento(rs.getString("tipodoc"));
				dto.setTipoConsecutivo(rs.getString("tipocon"));
				dto.setIndTipoDocumento(rs.getString("indtipodoc"));
				dto.setTipoDocumentoCruce(rs.getString("tipodoccruce"));
				dto.setUnidadFuncionalEstandar(rs.getString("unidadfunestandar"));
				dto.setInstitucion(rs.getString("institucion"));
				dto.setObservacionesEncabezado(rs.getString("observencabezado"));
				dto.setFecha(rs.getString("fecha"));
				dto.setHora(rs.getString("hora"));
				dto.setUsuario(rs.getString("usuario"));
				dto.setActivo(rs.getString("activo"));
				dto.setTipoMovimiento(rs.getString("tipomovimiento"));
				dto.setNombreConsecutivo(rs.getString("nombreconsecutivo"));
				dto.setNombreDocumento(rs.getString("nombredocumento"));
				dto.setNombreUnidadFuncional(rs.getString("descunidadfun"));
			}

			logger.info("REALICE LA CONSULTA DEL TIPO DE DOCUMENTO SOLICITADO");
			return dto;
	
		}
		catch (Exception e) 
		{			
			e.printStackTrace();
			logger.info("\n Error en  la consulta de tipso de docs 1E---> "+consultarInfoTipoDocUnitario+" ");
		}
		return dto;
	}
	
	
	public static boolean actualizarDocParam(Connection con, String indice, DtoTiposInterfazDocumentosParam1E dto)
	{
		boolean transaccionExitosa=false;
		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(actualizarInactivarDoc, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));		
			ps.setString(1,UtilidadFecha.conversionFormatoFechaABD(dto.getFechaInactivacion()));
			ps.setString(2,dto.getHoraInactivacion());
			ps.setString(3,dto.getUsuarioInactivacion());
			ps.setInt(4,Utilidades.convertirAEntero(indice));
			
			if(ps.executeUpdate()>0)
				transaccionExitosa=true;
			else
				transaccionExitosa=false;
			
		}
		catch (SQLException e) 
		{	
			logger.info("ERROR / actualizarInactivarDoc / "+e);
			return transaccionExitosa;
		}
	
		logger.info("INACTIVE EL REGISTRO ANTIGUO");
		return transaccionExitosa;
	}
	
	public static boolean insertarLog(Connection con, DtoLogParamGenerales1E dto)
	{
		boolean transaccionExitosa=false;
		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(insertarParamGeneralesLog, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));		
			ps.setInt(1,UtilidadBD.obtenerSiguienteValorSecuencia(con, "interfaz.seq_tip_doc_param_1e_log"));
			ps.setInt(2,Utilidades.convertirAEntero(dto.getParamGenerales()));
			ps.setString(3,dto.getPathArchivoInterfaz());
			ps.setString(4,dto.getPathArchivoInconsis());
			if (!dto.getTerceroGenFacPar().equals(ConstantesBD.codigoNuncaValido+""))
				ps.setInt(5,Utilidades.convertirAEntero(dto.getTerceroGenFacPar()));
			else
				ps.setNull(5, Types.INTEGER);
			
			if (!dto.getTerceroGenPagPac().equals(ConstantesBD.codigoNuncaValido+""))
				ps.setInt(6,Utilidades.convertirAEntero(dto.getTerceroGenPagPac()));
			else
				ps.setNull(6, Types.INTEGER);
			
			if (!UtilidadTexto.isEmpty(dto.getCuentaAbonoPac()))
				ps.setDouble(7,Utilidades.convertirADouble(dto.getCuentaAbonoPac()));
			else
				ps.setNull(7, Types.DOUBLE);
			
			ps.setString(8,dto.getCodConFlEfeMovCxp());
			ps.setString(9,dto.getCodConFlEfeMovCon());
			ps.setString(10,dto.getRealizarCalRetenCxh());
			ps.setString(11,dto.getRealizarCalRestenCxes());
			ps.setString(12,dto.getRealizarCalAutoretFp());
			ps.setString(13,dto.getRealizarCalAutoretFv());
			
			if (!UtilidadTexto.isEmpty(dto.getFechaControlDesmarcar()))
				ps.setString(14,UtilidadFecha.conversionFormatoFechaABD(dto.getFechaControlDesmarcar()));
			else
				ps.setNull(14, Types.DATE);
			
			ps.setString(15,dto.getDocumentoCruceHi());
			
			if (!UtilidadTexto.isEmpty(dto.getCentroAtencionContable()))
				ps.setInt(16,Utilidades.convertirAEntero(dto.getCentroAtencionContable()));
			else
				ps.setNull(16, Types.INTEGER);
				
			ps.setInt(17,Utilidades.convertirAEntero(dto.getInstitucion()));
			ps.setString(18,UtilidadFecha.conversionFormatoFechaABD(dto.getFechaModifica()));
			ps.setString(19,dto.getHoraModifica());
			ps.setString(20,dto.getUsuarioModifica());
			
			if(!dto.getRealizarCalRetenCxda().equals(""))
				ps.setString(21, dto.getRealizarCalRetenCxda());
			else
				ps.setNull(21, Types.VARCHAR);
			
			if(!dto.getRealizarCalAutoretCxCC().equals(""))
				ps.setString(22, dto.getRealizarCalAutoretCxCC());
			else
				ps.setNull(22,Types.VARCHAR);
			
//			if(!dto.getCentroAtencionContableDRC().equals(""))
//				ps.setString(23, dto.getCentroAtencionContableDRC());
//			else
//				ps.setNull(23,Types.VARCHAR);
//			
//			if(!dto.getCentroAtencionContableRG().equals(""))
//				ps.setString(24, dto.getCentroAtencionContableRG());
//			else
//				ps.setNull(24,Types.VARCHAR);
			
			
			if(ps.executeUpdate()>0)
				transaccionExitosa=true;
			else
				transaccionExitosa=false;
			
		}
		catch (SQLException e) 
		{	
			logger.info("ERROR / insertarParamGeneralesLog / ",e);
			return transaccionExitosa;
		}
	
	logger.info("REALICE LA INSERCION DEL LOG DE PARAMETRIZACION INTERFAZ CONTABLE SISTEMA 1E");
		
		return transaccionExitosa;
	}
	
	//Cambios Anexo 833 Agosto 14
	public static ArrayList consultarEventos(Connection con)
	{
		ArrayList<HashMap<String, Object>> eventos = new ArrayList<HashMap<String,Object>>();
		String consultaEventos=	"SELECT " +
									"codigo," +
									"nombre " +
								"FROM " +
									"interfaz.eventos_cartera_glosas " +
								"ORDER BY " +
									"nombre ";

		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaEventos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				HashMap<String, Object> mapa =new HashMap<String, Object>();
				mapa.put("codigo", rs.getString("codigo"));
				mapa.put("nombre", rs.getString("nombre"));
				eventos.add(mapa);
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return eventos;
	}
	
	public static boolean insertarEventosParam1E(Connection con, DtoEventosParam1E dto)
	{
		boolean transaccionExitosa=false;
		try
		{
			PreparedStatementDecorator pst=new PreparedStatementDecorator(con.prepareStatement(insertarEventosParam1E,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			int cosecutivo = UtilidadBD.obtenerSiguienteValorSecuencia(con,"interfaz.seq_eventos_param_1e");
			pst.setInt(1, cosecutivo);
			pst.setInt(2, Utilidades.convertirAEntero(dto.getParamGenerales1E()));
			pst.setInt(3, Utilidades.convertirAEntero(dto.getEvento()));
			pst.setString(4, dto.getCodigoEvento());
			pst.setString(5, dto.getNotasEvento());
			pst.setString(6, ConstantesBD.acronimoSi);
			pst.setString(7, UtilidadFecha.conversionFormatoFechaABD(dto.getFecha()));
			pst.setString(8, dto.getHora());
			pst.setString(9, dto.getUsuario());
			
			if(pst.executeUpdate()>0)
				transaccionExitosa=true;
			else
				transaccionExitosa=false;
			
		}
		catch(SQLException e){
			logger.info("Error insertarEventosParam1E !!!!!!\n La Consulta: "+insertarEventosParam1E);
			
			logger.error(e);
			return transaccionExitosa;
		}
		return transaccionExitosa;
	}
	
	public static ArrayList<DtoEventosParam1E> consultarEventosParam1E (Connection con, String consecutivoParam)
	{
		ArrayList<DtoEventosParam1E> arrayEventos=new ArrayList<DtoEventosParam1E>();
		try
		{	
			String consulta = consultarEventosParam1E;
			if(!UtilidadTexto.isEmpty(consecutivoParam))
			{
				consulta += " AND e.param_generales_1e =  "+consecutivoParam;
			}
			consulta += " ORDER BY ec.nombre";
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("valor de la consulta de los eventos >> "+consulta);
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoEventosParam1E dto= new DtoEventosParam1E();
				dto.setConsecutivoPk(rs.getString("consecutivo"));
				dto.setParamGenerales1E(rs.getString("param_generales_1e"));
				dto.setEvento(rs.getString("evento"));
				dto.setCodigoEvento(rs.getString("codigo_evento"));
				dto.setNotasEvento(rs.getString("notas_evento"));
				dto.setActivo(rs.getString("activo"));
				dto.setNombreEvento(rs.getString("nombre"));
				arrayEventos.add(dto);
			}
		}
		catch (Exception e) 
		{			
			e.printStackTrace();
			logger.info("\n Error en  la consulta de tipos de eventos 1E---> "+consultarEventosParam1E+" ");
		}

		
		return arrayEventos;
	}
	
	/**
	 * 
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static HashMap<String, Object> getTiposConseTipDoc(Connection con, HashMap parametros)
	{
		
		HashMap<String, Object> tipCon=new HashMap<String, Object>();
		tipCon.put("numRegistros", "0");
		int cont = 0 ;
		try
		{	
			logger.info("Consulta: "+consultaTiposConsecutivos+ "tipo_doc: "+parametros.get("tipo_doc"));
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaTiposConsecutivos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(parametros.get("tipo_doc")+""));
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				tipCon.put("nombre_"+cont, rs.getString("nombre"));
				tipCon.put("codigo_"+cont, rs.getInt("codigo"));
				cont++;
			}
			tipCon.put("numRegistros", cont);
		}
		catch (Exception e) 
		{			
			e.printStackTrace();
			logger.info("\n Error en  la consulta de tipos de eventos 1E---> "+consultaTiposConsecutivos+" ");
		}
		return tipCon;
	}
	
	public static boolean inactivarEvento(Connection con, DtoEventosParam1E dto)
	{
		boolean transaccionExitosa=false;
		
		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(inactivarEvento, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));		
			ps.setString(1,UtilidadFecha.conversionFormatoFechaABD(dto.getFechaInactivacion()));
			ps.setString(2,dto.getHoraInactivacion());
			ps.setString(3,dto.getUsuarioInactivacion());
			ps.setInt(4,Utilidades.convertirAEntero(dto.getConsecutivoPk()));
			
			if(ps.executeUpdate()>0)
				transaccionExitosa=true;
			else
				transaccionExitosa=false;
			
		}
		catch (SQLException e) 
		{	
			logger.info("ERROR / inactivarEvento / "+e);
			return transaccionExitosa;
		}
		
		return transaccionExitosa;
	}
	
	public static boolean validarEventoUnico(Connection con, DtoEventosParam1E dto)
	{
		boolean transaccionExitosa=false;
		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(validarEventoUnico, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));		
			ps.setInt(1,Utilidades.convertirAEntero(dto.getEvento()));
			
			if(ps.executeUpdate()>0)
				transaccionExitosa=true;
			else
				transaccionExitosa=false;
		}
		catch (SQLException e) 
		{	
			logger.info("ERROR / validarEventoUnico / "+e);
			return transaccionExitosa;
		}
		
		return transaccionExitosa;
	}
	//Fin Cambios Anexo 833 Agosto 14
}