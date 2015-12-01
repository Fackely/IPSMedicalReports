package com.princetonsa.dao.sqlbase.administracion;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
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
import com.princetonsa.dto.administracion.DtoDetTiposRetencionClaseInv;
import com.princetonsa.dto.administracion.DtoDetTiposRetencionConceptoFV;
import com.princetonsa.dto.administracion.DtoDetTiposRetencionGrupoSer;
import com.princetonsa.dto.administracion.DtoEspecialidades;
import com.princetonsa.dto.administracion.DtoLogTipoRetencion;
import com.princetonsa.dto.administracion.DtoTiposRetencion;

/**
 * @author Víctor Hugo Gómez L.
 */
public class SqlBaseTiposRetencionDao {
	
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseTiposRetencionDao.class);
	
	/**
	 * Cadena de insercion de log tipos retencion
	 */
	private static final String strInsertLogTipoRetencion = "INSERT INTO administracion.logs_tipos_retencion( " +
			"consecutivo," +
			"tipo_retencion," +
			"codigo," +
			"descripcion," +
			"sigla," +
			"codigo_interfaz," +
			"fecha_modifica," +
			"hora_modifica," +
			"usuario_modifica) " +
			"VALUES (?,?,?,?,?,?,?,?,?)";
	
	/**
	 * Cadena de insercion de tipos retencion  
	 */
	private static final String strInsertTiposRetencion = "INSERT INTO administracion.tipos_retencion( " +
			"consecutivo, " +
			"codigo, " +
			"institucion, " +
			"descripcion, " +
			"sigla, " +
			"codigo_interfaz, " +
			"usuario_modifica, " +
			"fecha_modifica, " +
			"hora_modifica, " +
			"activo) " +
			"VALUES(?,?,?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?)";
	
	/**
	 * Cadena de insercion de Grupo de Servicio Vicnulado al Tipo de Retención
	 */
	private static final String strInsertTRGrupoServicio = "INSERT INTO administracion.tipos_retencion_grupo_ser(" +
			"consecutivo, " +
			"tipo_retencion, " +
			"grupo, " +
			"fecha_modifica, " +
			"hora_modifica, " +
			"usuario_modifica, " +
			"activo) " +
			"VALUES(?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?,?)";
	
	/**
	 * Cadena de insercion de Clase Inventario Vicnulado al Tipo de Retención
	 */
	private static final String strInsertTRClaseInventario = "INSERT INTO administracion.tipos_retencion_clase_inv(" +
			"consecutivo, " +
			"tipo_retencion, " +
			"clase, " +
			"fecha_modifica, " +
			"hora_modifica, " +
			"usuario_modifica, " +
			"activo) " +
			"VALUES(?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?,?)";
	
	/**
	 * Cadena de insercion de Concepto Facturas Varias Vicnuladas al Tipo de Retención
	 */
	private static final String strInsertTRConceptoFraVaria = "INSERT INTO administracion.tipos_retencion_conc_fv(" +
			"consecutivo, " +
			"tipo_retencion, " +
			"concepto, " +
			"fecha_modifica, " +
			"hora_modifica, " +
			"usuario_modifica, " +
			"activo) " +
			"VALUES(?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?,?)";
	
	/**
	 * Cadena de Modificacion Tipo Retencion 
	 */
	private static final String strUpdateTiposRetencion = "UPDATE administracion.tipos_retencion SET " +
			"codigo = ?, descripcion = ?, sigla = ?, codigo_interfaz = ?, usuario_modifica = ?, fecha_modifica = CURRENT_DATE, hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+" " +
			"WHERE consecutivo = ? ";
	
	/**
	 * Cadena de Eliminacion Tipo Retencion  
	 */
	private static final String strEliminarTiposRetencion = "UPDATE administracion.tipos_retencion SET activo = '"+ConstantesBD.acronimoNo+"', " +
			"fecha_inactivacion = CURRENT_DATE, hora_inactivacion = "+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
			"usuario_inactivacion = ? WHERE consecutivo = ? ";
	
	/**
	 * Cadena de Inactivacion Tipo Retencion Grupo Servicio 
	 */
	private static final String strInactivacionTRGrupoServicio = "UPDATE administracion.tipos_retencion_grupo_ser SET " +
			"activo = '"+ConstantesBD.acronimoNo+"', fecha_inactivacion = CURRENT_DATE, " +
			"hora_inactivacion = "+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
			"usuario_inactivacion = ? WHERE consecutivo = ? ";
	
	/**
	 * Cadena de Inactivacion Tipo Retencion Clase Inventario 
	 */
	private static final String strInactivacionTRClaseInventario = "UPDATE administracion.tipos_retencion_clase_inv SET " +
			"activo = '"+ConstantesBD.acronimoNo+"', fecha_inactivacion = CURRENT_DATE, " +
			"hora_inactivacion = "+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
			"usuario_inactivacion = ? WHERE consecutivo = ? ";
	
	/**
	 * Cadena de Inactivacion Tipo Retencion Concepto Facturas Varias 
	 */
	private static final String strInactivacionTRConceptoFraVarias = "UPDATE administracion.tipos_retencion_conc_fv SET " +
			"activo = '"+ConstantesBD.acronimoNo+"', fecha_inactivacion = CURRENT_DATE, " +
			"hora_inactivacion = "+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
			"usuario_inactivacion = ? WHERE consecutivo = ? ";
	
	//**************************************************************
	/**
	 * Cadenas de verificacion para la eliminacion de Tipo Retencion 
	 */
	private static final String strVerificarConceptoRetencion = "SELECT * " +
			"FROM administracion.conceptos_retencion " +
			"WHERE tipo_retencion = ? " +
			"AND activo = '"+ConstantesBD.acronimoSi+"' ";
	
	private static final String strVerificarClaseInv = "SELECT * " +
			"FROM administracion.tipos_retencion_clase_inv " +
			"WHERE tipo_retencion = ? " +
			"AND activo = '"+ConstantesBD.acronimoSi+"' ";
	
	private static final String strVerificarGrupoSer = "SELECT * " +
			"FROM administracion.tipos_retencion_grupo_ser " +
			"WHERE tipo_retencion = ? " +
			"AND activo = '"+ConstantesBD.acronimoSi+"' ";
	
	private static final String strVerificarConcFacturaVaria = "SELECT * " +
			"FROM administracion.tipos_retencion_conc_fv " +
			"WHERE tipo_retencion = ? " +
			"AND activo = '"+ConstantesBD.acronimoSi+"' ";
	//**************************************************************
	
	
	/**
	 * Cadena que consulta los tipos de Retencion
	 */
	private static final String strConsultaTiposRetencion = "SELECT " +
			"tr.consecutivo AS consecutivo, " +
			"tr.codigo AS codigo, " +
			"tr.institucion AS institucion, " +
			"tr.descripcion AS descripcion, " +
			"tr.sigla AS sigla, " +
			"tr.codigo_interfaz AS codigo_interfaz, " +
			"tr.usuario_modifica AS usuario_mod, " +
			"to_char(tr.fecha_modifica,'YYYY-MM-DD') AS fecha_mod, " +
			"tr.hora_modifica AS hora_mod, " +
			"tr.activo AS activo, " +
			"tr.usuario_inactivacion AS usuario_ina " +
			"FROM administracion.tipos_retencion tr " +
			"WHERE tr.institucion = ? AND tr.activo = '"+ConstantesBD.acronimoSi+"' " +
			"ORDER BY tr.descripcion ASC";
	
	/**
	 * Cadena que consulta el detalle de clase inventario
	 */
	private static final String strConsultaTiposRetencionClaseInv = "SELECT " + 
			"trci.consecutivo AS consecutivo_clase_inv, " +
			"trci.clase AS cod_clase_inv, " +
			"trci.activo AS activo_clase_inv, " +
			"trci.usuario_modifica AS usuario_mod_clase_inv, " + 
			"coalesce(trci.usuario_inactivacion,'') AS usuario_ina_clase_inv, " +
			"cinv.nombre AS nombre_clase_inv " +
			"FROM administracion.tipos_retencion_clase_inv trci " +
			"INNER JOIN inventarios.clase_inventario cinv ON (cinv.codigo=trci.clase AND cinv.institucion = ?) " +
			"WHERE trci.tipo_retencion = ? AND trci.activo = '"+ConstantesBD.acronimoSi+"' " +
			"ORDER BY cinv.nombre ASC ";
	
	/** 
	 * Cadena que consulta el detalle de Grupo Servicio
	 */
	private static final String strConsultaTiposRetencionGrupoSer = "SELECT " + 
			"trgs.consecutivo AS consecutivo_grupo_ser, " +
			"trgs.grupo AS cod_grupo_ser, " +
			"trgs.activo AS activo_grupo_ser, " +
			"trgs.usuario_modifica AS usuario_mod_grupo_ser, " + 
			"coalesce(trgs.usuario_inactivacion,'') AS usuario_ina_grupo_ser, " +
			"gs.descripcion AS descripcion_grupo_ser, " +
			"gs.acronimo AS acronimo_grupo_ser " +
			"FROM administracion.tipos_retencion_grupo_ser trgs " +
			"INNER JOIN facturacion.grupos_servicios gs ON (gs.codigo=trgs.grupo AND gs.institucion = ? ) " +
			"WHERE trgs.tipo_retencion = ? AND trgs.activo = '"+ConstantesBD.acronimoSi+"' " +
			"ORDER BY gs.descripcion ASC ";
	/**
	 * Cadena que Consulta el detalle de Concepto de Facturas Varias
	 */
	private static final String strConsultaTiposRetencionConcFV = "SELECT " + 
			"trcfv.consecutivo AS consecutivo_conc_fv, " +
			"trcfv.concepto AS cod_conc_fv, " +
			"trcfv.activo AS activo_conc_fv, " +
			"trcfv.usuario_modifica AS usuario_mod_conc_fv, " + 
			"coalesce(trcfv.usuario_inactivacion,'') AS usuario_ina_conc_fv, " +
			"cfv.descripcion AS descripcion_conc_fv " +
			"FROM administracion.tipos_retencion_conc_fv trcfv " +
			"INNER JOIN facturasvarias.conceptos_facturas_varias cfv ON (cfv.consecutivo=trcfv.concepto AND cfv.institucion = ? ) " +
			"WHERE trcfv.tipo_retencion = ? AND trcfv.activo = '"+ConstantesBD.acronimoSi+"' " +
			"ORDER BY cfv.descripcion ASC ";
	
	/**
	 * 
	 * @param con
	 * @param dtoLogTipoRetencion
	 * @return
	 */
	public static int insertarLogTipoRetencion(Connection con, DtoLogTipoRetencion dtoLogTipoRetencion)
	{
		try
		{
			//logger.info("SQL ingresandoLogTipoRetencion !!!!!!\n La Consulta: "+strInsertLogTipoRetencion);
			PreparedStatementDecorator pst=new PreparedStatementDecorator(con.prepareStatement(strInsertLogTipoRetencion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			int cosecutivo = UtilidadBD.obtenerSiguienteValorSecuencia(con,"administracion.seq_logs_tipos_retencion");
			pst.setInt(1, cosecutivo);
			pst.setInt(2, dtoLogTipoRetencion.getCodigoTipoRetencion());
			pst.setString(3, dtoLogTipoRetencion.getCodigo());
			pst.setString(4, dtoLogTipoRetencion.getDescripcion());
			pst.setString(5, dtoLogTipoRetencion.getSigla());
			pst.setString(6, dtoLogTipoRetencion.getCodigoInterfaz());
			pst.setDate(7, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dtoLogTipoRetencion.getFechaModifica())));
			pst.setString(8, UtilidadFecha.convertirHoraACincoCaracteres(dtoLogTipoRetencion.getHoraModifica()));
			pst.setString(9, dtoLogTipoRetencion.getUsuarioModifica());
			if(pst.executeUpdate()>0){
				pst.close();
				return cosecutivo;
			}
		}catch(SQLException e){
			logger.info("Error ingresandoLogTipoRetencion !!!!!!\n La Consulta: "+strInsertLogTipoRetencion);
			logger.error(e);
			return ConstantesBD.codigoNuncaValido;
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * 
	 * @param con
	 * @param dtoTipoRetencion
	 * @return
	 */
	public static int insertarTipoRetencion(Connection con, DtoTiposRetencion dtoTipoRetencion)
	{
		try
		{
			//logger.info("SQL ingresandoTipoRetencion !!!!!!\n La Consulta: "+strInsertTiposRetencion);
			PreparedStatementDecorator pst=new PreparedStatementDecorator(con.prepareStatement(strInsertTiposRetencion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			int cosecutivo = UtilidadBD.obtenerSiguienteValorSecuencia(con,"administracion.seq_tipos_retencion");
			pst.setInt(1, cosecutivo);
			pst.setString(2, dtoTipoRetencion.getCodigo());
			pst.setInt(3, dtoTipoRetencion.getCodigoInstitucion());
			pst.setString(4, dtoTipoRetencion.getDescripcion());
			pst.setString(5, dtoTipoRetencion.getSigla());
			pst.setString(6, dtoTipoRetencion.getCodigoInterfaz());
			pst.setString(7, dtoTipoRetencion.getUsuarioModificacion());
			pst.setString(8, dtoTipoRetencion.getActivo());
			if(pst.executeUpdate()>0){
				pst.close();
				return cosecutivo;
			}
		}catch(SQLException e){
			logger.info("Error ingresandoTipoRetencion !!!!!!\n La Consulta: "+strInsertTiposRetencion);
			logger.error(e);
			return ConstantesBD.codigoNuncaValido;
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * 
	 * @param con
	 * @param dtoTRGrupoSer
	 * @return
	 */
	public static int insertarTRGrupoServicio(Connection con, DtoDetTiposRetencionGrupoSer dtoTRGrupoSer)
	{
		try
		{
			//logger.info("SQL ingresandoTipoRetencionGrupoServicio !!!!!!\n La Consulta: "+strInsertTRGrupoServicio);
			PreparedStatementDecorator pst=new PreparedStatementDecorator(con.prepareStatement(strInsertTRGrupoServicio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			int cosecutivo = UtilidadBD.obtenerSiguienteValorSecuencia(con,"administracion.seq_tipos_retencion_grupo_ser");
			pst.setInt(1, cosecutivo);
			pst.setInt(2, dtoTRGrupoSer.getCodigoTipoRetencion());
			pst.setInt(3, dtoTRGrupoSer.getCodigoGrupoSer());
			pst.setString(4, dtoTRGrupoSer.getUsuarioModificacion());
			pst.setString(5, dtoTRGrupoSer.getActivo());
			if(pst.executeUpdate()>0){
				pst.close();
				return cosecutivo;
			}
		}catch(SQLException e){
			logger.info("Error ingresandoTipoRetencionGrupoServicio !!!!!!\n La Consulta: "+strInsertTRGrupoServicio);
			logger.error(e);
			return ConstantesBD.codigoNuncaValido;
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * 
	 * @param con
	 * @param dtoTRClaseInv
	 * @return
	 */
	public static int insertarTRClaseInventario(Connection con, DtoDetTiposRetencionClaseInv dtoTRClaseInv)
	{
		try
		{
			//logger.info("SQL ingresandoTipoRetencionClaseInventario !!!!!!\n La Consulta: "+strInsertTRClaseInventario);
			PreparedStatementDecorator pst=new PreparedStatementDecorator(con.prepareStatement(strInsertTRClaseInventario,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			int cosecutivo = UtilidadBD.obtenerSiguienteValorSecuencia(con,"administracion.seq_tipos_retencion_clase_inv");
			pst.setInt(1, cosecutivo);
			pst.setInt(2, dtoTRClaseInv.getCodigoTipoRetencion());
			pst.setInt(3, dtoTRClaseInv.getCodigoClaseInv());
			pst.setString(4, dtoTRClaseInv.getUsuarioModificacion());
			pst.setString(5, dtoTRClaseInv.getActivo());
			if(pst.executeUpdate()>0){
				pst.close();
				return cosecutivo;
			}
		}catch(SQLException e){
			logger.info("Error ingresandoTipoRetencionClaseInventario !!!!!!\n La Consulta: "+strInsertTRClaseInventario);
			logger.error(e);
			return ConstantesBD.codigoNuncaValido;
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * 
	 * @param con
	 * @param dtoTRClaseInv
	 * @return
	 */
	public static int insertarTRConceptoFraVaria(Connection con, DtoDetTiposRetencionConceptoFV dtoTRConcFV)
	{
		try
		{
			//logger.info("SQL ingresandoTipoRetencionConceptoFV !!!!!!\n La Consulta: "+strInsertTRConceptoFraVaria);
			PreparedStatementDecorator pst=new PreparedStatementDecorator(con.prepareStatement(strInsertTRConceptoFraVaria,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			int cosecutivo = UtilidadBD.obtenerSiguienteValorSecuencia(con,"administracion.seq_tipos_retencion_conc_fv");
			pst.setInt(1, cosecutivo);
			pst.setInt(2, dtoTRConcFV.getCodigoTipoRetencion());
			pst.setInt(3, dtoTRConcFV.getCodigoConcepto());
			pst.setString(4, dtoTRConcFV.getUsuarioModificacion());
			pst.setString(5, dtoTRConcFV.getActivo());
			if(pst.executeUpdate()>0){
				pst.close();
				return cosecutivo;
			}
		}catch(SQLException e){
			logger.info("Error ingresandoTipoRetencionConceptoFV !!!!!!\n La Consulta: "+strInsertTRConceptoFraVaria);
			logger.error(e);
			return ConstantesBD.codigoNuncaValido;
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * 
	 * @param con
	 * @param dtoTipoRetencion
	 * @return
	 */
	public static int updateTipoRetencion(Connection con, DtoTiposRetencion dtoTipoRetencion)
	{
		try
		{
			//logger.info("SQL modificandoTipoRetencion !!!!!!\n La Consulta: "+strUpdateTiposRetencion);
			PreparedStatementDecorator pst=new PreparedStatementDecorator(con.prepareStatement(strUpdateTiposRetencion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1, dtoTipoRetencion.getCodigo());
			pst.setString(2, dtoTipoRetencion.getDescripcion());
			pst.setString(3, dtoTipoRetencion.getSigla());
			pst.setString(4, dtoTipoRetencion.getCodigoInterfaz());
			pst.setString(5, dtoTipoRetencion.getUsuarioModificacion());
			pst.setInt(6, dtoTipoRetencion.getConsecutivo());
			if(pst.executeUpdate()>0){
				pst.close();
				return 1;
			}
		}catch(SQLException e){
			logger.info("Error modificandoTipoRetencion !!!!!!\n La Consulta: "+strUpdateTiposRetencion);
			logger.error(e);
			return ConstantesBD.codigoNuncaValido;
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * 
	 * @param con
	 * @param dtoTipoRetencion
	 * @return
	 */
	public static int deleteTipoRetencion(Connection con, DtoTiposRetencion dtoTipoRetencion)
	{
		try
		{
			//logger.info("SQL eliminandoTipoRetencion !!!!!!\n La Consulta: "+strEliminarTiposRetencion);
			PreparedStatementDecorator pst=new PreparedStatementDecorator(con.prepareStatement(strEliminarTiposRetencion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1, dtoTipoRetencion.getUsuarioAnulacion());
			pst.setInt(2, dtoTipoRetencion.getConsecutivo());
			if(pst.executeUpdate()>0){
				pst.close();
				return 1;
			}
		}catch(SQLException e){
			logger.info("Error eliminandoTipoRetencion !!!!!!\n La Consulta: "+strEliminarTiposRetencion);
			logger.error(e);
			return ConstantesBD.codigoNuncaValido;
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * Inactivacion de Tipo Retencion Grupo Servicio
	 * @param con
	 * @param dtoTRGrupoSer
	 * @return
	 */
	public static int inactivarTRGrupoServicio(Connection con, DtoDetTiposRetencionGrupoSer dtoTRGrupoSer)
	{
		try
		{
			//logger.info("SQL inactivacionTipoRetencionGrupoServicio !!!!!!\n La Consulta: "+strEliminarTiposRetencion);
			PreparedStatementDecorator pst=new PreparedStatementDecorator(con.prepareStatement(strInactivacionTRGrupoServicio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1, dtoTRGrupoSer.getUsuarioAnulacion());
			pst.setInt(2, dtoTRGrupoSer.getConsecutivo());
			if(pst.executeUpdate()>0){
				pst.close();
				return 1;
			}
		}catch(SQLException e){
			logger.info("Error inactivacionTipoRetencionGrupoServicio !!!!!!\n La Consulta: "+strEliminarTiposRetencion);
			logger.error(e);
			return ConstantesBD.codigoNuncaValido;
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * Inactivacion de Tipo Retencion Clase Inventario
	 * @param con
	 * @param dtoTRClaseInv
	 * @return
	 */
	public static int inactivarTRClaseInventario(Connection con, DtoDetTiposRetencionClaseInv dtoTRClaseInv)
	{
		try
		{
			//logger.info("SQL inactivacionTipoRetencionClaseInventario !!!!!!\n La Consulta: "+strInactivacionTRClaseInventario);
			PreparedStatementDecorator pst=new PreparedStatementDecorator(con.prepareStatement(strInactivacionTRClaseInventario,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1, dtoTRClaseInv.getUsuarioAnulacion());
			pst.setInt(2, dtoTRClaseInv.getConsecutivo());
			if(pst.executeUpdate()>0){
				pst.close();
				return 1;
			}
		}catch(SQLException e){
			logger.info("Error inactivacionTipoRetencionClaseInventario !!!!!!\n La Consulta: "+strInactivacionTRClaseInventario);
			logger.error(e);
			return ConstantesBD.codigoNuncaValido;
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * Inactivacion de Tipo Retencion Clase Inventario
	 * @param con
	 * @param dtoTRConceptoFV
	 * @return
	 */
	public static int inactivarTRConceptoFraVarias(Connection con, DtoDetTiposRetencionConceptoFV dtoTRConceptoFV)
	{
		try
		{
			//logger.info("SQL inactivacionTipoRetencionConceptoFraVarias !!!!!!\n La Consulta: "+strInactivacionTRConceptoFraVarias);
			PreparedStatementDecorator pst=new PreparedStatementDecorator(con.prepareStatement(strInactivacionTRConceptoFraVarias,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1, dtoTRConceptoFV.getUsuarioAnulacion());
			pst.setInt(2, dtoTRConceptoFV.getConsecutivo());
			if(pst.executeUpdate()>0){
				pst.close();
				return 1;
			}
		}catch(SQLException e){
			logger.info("Error inactivacionTipoRetencionConceptoFraVarias !!!!!!\n La Consulta: "+strInactivacionTRConceptoFraVarias);
			logger.error(e);
			return ConstantesBD.codigoNuncaValido;
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * Consulta Listado Detalle Grupo Servicio Tipos de Retencion
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public static ArrayList<DtoDetTiposRetencionGrupoSer> cargarTiposRetencionGrupoSer(Connection con, HashMap parametros) 
	{
		ArrayList<DtoDetTiposRetencionGrupoSer> list = new ArrayList<DtoDetTiposRetencionGrupoSer>();
		try
		{
			//logger.info("Consulta caragarTiposRetencionGrupoSer !!!!!!\n La Consulta: "+strConsultaTiposRetencionGrupoSer);
			//logger.info("datos >>>>> institucion="+parametros.get("institucion"));
			//logger.info("datos >>>>> tipo_retencion="+parametros.get("tipo_retencion"));
			PreparedStatementDecorator pst=new PreparedStatementDecorator(con.prepareStatement(strConsultaTiposRetencionGrupoSer,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, Utilidades.convertirAEntero(parametros.get("institucion")+""));
			pst.setInt(2, Utilidades.convertirAEntero(parametros.get("tipo_retencion")+""));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			while(rs.next())
			{		
				DtoDetTiposRetencionGrupoSer dto = new DtoDetTiposRetencionGrupoSer();
				dto.setConsecutivo(rs.getInt("consecutivo_grupo_ser"));
				dto.setCodigoTipoRetencion(Utilidades.convertirAEntero(parametros.get("tipo_retencion")+""));
				dto.setCodigoGrupoSer(rs.getInt("cod_grupo_ser"));
				if(UtilidadTexto.getBoolean(rs.getString("activo_grupo_ser")))
					dto.setActivo(ConstantesBD.acronimoSi);
				else
					dto.setActivo(ConstantesBD.acronimoNo);
				dto.setUsuarioModificacion(rs.getString("usuario_mod_grupo_ser"));
				dto.setUsuarioAnulacion(rs.getString("usuario_ina_grupo_ser"));
				dto.setDescripcionGrupoSer(rs.getString("descripcion_grupo_ser"));
				dto.setAcronimoGrupoSer(rs.getString("acronimo_grupo_ser"));
				list.add(dto);
			}
			rs.close();
			pst.close();
		}catch(SQLException e){
			logger.info("Error caragarTiposRetencionGrupoSer !!!!!!\n La Consulta: "+strConsultaTiposRetencionGrupoSer);
			logger.info("datos >>>>> institucion="+parametros.get("institucion"));
			logger.info("datos >>>>> tipo_retencion="+parametros.get("tipo_retencion"));
			logger.error(e);			
		}
		return list;
	}
	
	/**
	 * Consulta Listado Detalle Clase Inventario Tipos de Retencion
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public static ArrayList<DtoDetTiposRetencionClaseInv> cargarTiposRetencionClaseInv(Connection con, HashMap parametros) 
	{
		ArrayList<DtoDetTiposRetencionClaseInv> list = new ArrayList<DtoDetTiposRetencionClaseInv>();
		try
		{
			//logger.info("Consulta caragarTiposRetencionClaseInv !!!!!!\n La Consulta: "+strConsultaTiposRetencionClaseInv);
			//logger.info("datos >>>>> institucion="+parametros.get("institucion"));
			//logger.info("datos >>>>> tipo_retencion="+parametros.get("tipo_retencion"));
			PreparedStatementDecorator pst=new PreparedStatementDecorator(con.prepareStatement(strConsultaTiposRetencionClaseInv,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, Utilidades.convertirAEntero(parametros.get("institucion")+""));
			pst.setInt(2, Utilidades.convertirAEntero(parametros.get("tipo_retencion")+""));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			while(rs.next())
			{		
				DtoDetTiposRetencionClaseInv dto = new DtoDetTiposRetencionClaseInv();
				dto.setConsecutivo(rs.getInt("consecutivo_clase_inv"));
				dto.setCodigoTipoRetencion(Utilidades.convertirAEntero(parametros.get("tipo_retencion")+""));
				dto.setCodigoClaseInv(rs.getInt("cod_clase_inv"));
				if(UtilidadTexto.getBoolean(rs.getString("activo_clase_inv")))
					dto.setActivo(ConstantesBD.acronimoSi);
				else
					dto.setActivo(ConstantesBD.acronimoNo);
				dto.setUsuarioModificacion(rs.getString("usuario_mod_clase_inv"));
				dto.setUsuarioAnulacion(rs.getString("usuario_ina_clase_inv"));
				dto.setNombreClaseInv(rs.getString("nombre_clase_inv"));
				list.add(dto);
			}
			rs.close();
			pst.close();
		}catch(SQLException e){
			logger.info("Error caragarTiposRetencionClaseInv !!!!!!\n La Consulta: "+strConsultaTiposRetencionClaseInv);
			logger.info("datos >>>>> institucion="+parametros.get("institucion"));
			logger.info("datos >>>>> tipo_retencion="+parametros.get("tipo_retencion"));
			logger.error(e);			
		}
		return list;
	}
	
	/**
	 * Consulta Listado Detalle Concepto Facturas Varias Tipos de Retencion
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public static ArrayList<DtoDetTiposRetencionConceptoFV> cargarTiposRetencionConceptoFV(Connection con, HashMap parametros) 
	{
		ArrayList<DtoDetTiposRetencionConceptoFV> list = new ArrayList<DtoDetTiposRetencionConceptoFV>();
		try
		{
			//logger.info("Consulta caragarTiposRetencionConceptoFacturaVaria !!!!!!\n La Consulta: "+strConsultaTiposRetencionConcFV);
			//logger.info("datos >>>>> institucion="+parametros.get("institucion"));
			//logger.info("datos >>>>> tipo_retencion="+parametros.get("tipo_retencion"));
			PreparedStatementDecorator pst=new PreparedStatementDecorator(con.prepareStatement(strConsultaTiposRetencionConcFV,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, Utilidades.convertirAEntero(parametros.get("institucion")+""));
			pst.setInt(2, Utilidades.convertirAEntero(parametros.get("tipo_retencion")+""));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			while(rs.next())
			{		
				DtoDetTiposRetencionConceptoFV dto = new DtoDetTiposRetencionConceptoFV();
				dto.setConsecutivo(rs.getInt("consecutivo_conc_fv"));
				dto.setCodigoTipoRetencion(Utilidades.convertirAEntero(parametros.get("tipo_retencion")+""));
				dto.setCodigoConcepto(rs.getInt("cod_conc_fv"));
				if(UtilidadTexto.getBoolean(rs.getString("activo_conc_fv")))
					dto.setActivo(ConstantesBD.acronimoSi);
				else
					dto.setActivo(ConstantesBD.acronimoNo);
				dto.setUsuarioModificacion(rs.getString("usuario_mod_conc_fv"));
				dto.setUsuarioAnulacion(rs.getString("usuario_ina_conc_fv"));
				dto.setDescripcionConcFV(rs.getString("descripcion_conc_fv"));
				list.add(dto);
			}
			rs.close();
			pst.close();
		}catch(SQLException e){
			logger.info("Error caragarTiposRetencionConceptoFacturaVaria !!!!!!\n La Consulta: "+strConsultaTiposRetencionConcFV);
			logger.info("datos >>>>> institucion="+parametros.get("institucion"));
			logger.info("datos >>>>> tipo_retencion="+parametros.get("tipo_retencion"));
			logger.error(e);			
		}
		return list;
	}
	
	/**
	 * Consulta Listado de Tipos de Retencion
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public static ArrayList<DtoTiposRetencion> cargarTiposRetencion(Connection con, HashMap parametros) 
	{
		ArrayList<DtoTiposRetencion> list = new ArrayList<DtoTiposRetencion>();
		try
		{
			//logger.info("Consulta caragarTiposRetencion !!!!!!\n La Consulta: "+strConsultaTiposRetencion);
			//logger.info("datos >>>>> institucion="+parametros.get("institucion"));
			PreparedStatementDecorator pst=new PreparedStatementDecorator(con.prepareStatement(strConsultaTiposRetencion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, Utilidades.convertirAEntero(parametros.get("institucion")+""));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			while(rs.next())
			{			
				DtoTiposRetencion dto = new DtoTiposRetencion();
				dto.setConsecutivo(rs.getInt("consecutivo"));
				dto.setCodigo(rs.getString("codigo"));
				dto.setCodigoInstitucion(rs.getInt("institucion"));
				dto.setDescripcion(rs.getString("descripcion"));
				dto.setSigla(rs.getString("sigla"));
				dto.setCodigoInterfaz(rs.getString("codigo_interfaz"));
				dto.setUsuarioModificacion(rs.getString("usuario_mod"));
				dto.setFecha_modifica(rs.getObject("fecha_mod").toString());
				dto.setHora_modifica(rs.getString("hora_mod"));
				dto.setActivo(rs.getString("activo"));
				dto.setUsuarioAnulacion(rs.getString("usuario_ina"));
				if(dto.getConsecutivo()>0){
					HashMap datos = new HashMap();
					datos.put("institucion", parametros.get("institucion"));
					datos.put("tipo_retencion", dto.getConsecutivo());
					dto.setDtoDetGrupoSer(cargarTiposRetencionGrupoSer(con, datos));
					dto.setDtoDetClaseInv(cargarTiposRetencionClaseInv(con, datos));
					dto.setDtoDetConceptoFV(cargarTiposRetencionConceptoFV(con, datos));
				}
				list.add(dto);
			}
			rs.close();
			pst.close();
		}catch(SQLException e){
			logger.info("Error caragarTiposRetencion !!!!!!\n La Consulta: "+strConsultaTiposRetencion);
			logger.info("datos >>>>> institucion="+parametros.get("institucion"));
			logger.error(e);			
		}
		return list;
	}
	
	/**
	 * Verificacion Concepto retencion
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static boolean verificacionConceptoRetencion(Connection con, HashMap parametros)
	{
		boolean result = true;
		try
		{
			PreparedStatementDecorator pst=new PreparedStatementDecorator(con.prepareStatement(strVerificarConceptoRetencion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, Utilidades.convertirAEntero(parametros.get("tipo_retencion")+""));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next()){
				result = false;
				logger.info("resultado >>>"+result);
			}
			rs.close();
			pst.close();
		}catch(SQLException e){
			logger.info("Error verificandoConceptoRetencion !!!!!!\n La Consulta: "+strVerificarConceptoRetencion);
			logger.info("datos >>>>> tipo_retencion="+parametros.get("tipo_retencion"));
			logger.error(e);
			return false;
		}
		logger.info("resultado >>>"+result);
		return result;
	}
	
	/**
	 * Verificacion Tipo Retencion Clase Inventario
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static boolean verificacionClaseInv(Connection con, HashMap parametros)
	{
		boolean result = true;
		try
		{
			PreparedStatementDecorator pst=new PreparedStatementDecorator(con.prepareStatement(strVerificarClaseInv,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, Utilidades.convertirAEntero(parametros.get("tipo_retencion")+""));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next()){
				result = false;
				logger.info("resultado >>>"+result);
			}
			rs.close();
			pst.close();
		}catch(SQLException e){
			logger.info("Error verificandoTRClaseInventario !!!!!!\n La Consulta: "+strVerificarClaseInv);
			logger.info("datos >>>>> tipo_retencion="+parametros.get("tipo_retencion"));
			logger.error(e);
			return false;
		}
		logger.info("resultado >>>"+result);
		return result;
	}
	
	/**
	 * Verificacion Tipo Retencion Clase Inventario
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static boolean verificacionGrupoSer(Connection con, HashMap parametros)
	{
		boolean result = true;
		try
		{
			PreparedStatementDecorator pst=new PreparedStatementDecorator(con.prepareStatement(strVerificarGrupoSer,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, Utilidades.convertirAEntero(parametros.get("tipo_retencion")+""));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next()){
				result = false;
				logger.info("resultado >>>"+result);
			}
			rs.close();
			pst.close();
		}catch(SQLException e){
			logger.info("Error verificandoTRGrupoSer !!!!!!\n La Consulta: "+strVerificarGrupoSer);
			logger.info("datos >>>>> tipo_retencion="+parametros.get("tipo_retencion"));
			logger.error(e);
			return false;
		}
		logger.info("resultado >>>"+result);
		return result;
	}
	
	/**
	 * Verificacion Tipo Retencion Clase Inventario
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static boolean verificacionConcFacturaVaria(Connection con, HashMap parametros)
	{
		boolean result = true;
		try
		{
			PreparedStatementDecorator pst=new PreparedStatementDecorator(con.prepareStatement(strVerificarConcFacturaVaria,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, Utilidades.convertirAEntero(parametros.get("tipo_retencion")+""));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next()){
				result = false;
				logger.info("resultado >>>"+result);
			}
			rs.close();
			pst.close();
		}catch(SQLException e){
			logger.info("Error verificandoTRConceptoFacturaVaria !!!!!!\n La Consulta: "+strVerificarConcFacturaVaria);
			logger.info("datos >>>>> tipo_retencion="+parametros.get("tipo_retencion"));
			logger.error(e);
			return false;
		}
		logger.info("resultado >>>"+result);
		return result;
	}
	
}
