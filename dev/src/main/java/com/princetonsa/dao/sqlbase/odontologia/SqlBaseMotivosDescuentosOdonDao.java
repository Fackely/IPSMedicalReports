package com.princetonsa.dao.sqlbase.odontologia;

import com.princetonsa.dto.odontologia.DtoMotivoDescuento;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;

import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

/**
 * 
 * @author axioma
 *
 */
public class SqlBaseMotivosDescuentosOdonDao {
	/**
	 * logger
	 */
	private static Logger logger = Logger.getLogger(SqlBaseMotivosDescuentosOdonDao.class);
	
	/**
	 * Cadena a insertar
	 */
	private static String insertarDescuento="INSERT INTO odontologia.motivos_descuentos_odon " +
																							"(codigo_pk , " +	//1
																							"codigo_motivo , " +//2
																							"institucion , " +//3
																							"descripcion , " +//4
																							"tipo , " +//5
																							"activo , " +//6
																							"fecha_modifica , " +//7
																							"hora_modifica , " +//8
																							"usuario_modifica , " + //9
																							"indicativo" +//10
																							") values ( " +
																							"? , " +//1
																							"? ," +//2
																							"? ," +//3
																							"? ," +//4
																							"? ," +//5
																							"? ," +//6
																							"? ," +//7
																							"? ," +//8
																							"?, " +//9
																							"? "+//10
																							" )";
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static double guardar( DtoMotivoDescuento dto)
	{
		double secuencia = ConstantesBD.codigoNuncaValidoDouble;
		
		try
		{
			Connection con = UtilidadBD.abrirConexion();
			ResultSetDecorator rs = null;
			secuencia=UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_motivos_descuentos_odon");
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con, insertarDescuento);
			ps.setDouble(1, secuencia);
			ps.setString(2, dto.getCodigoMotivo());
			ps.setInt(3, dto.getInstitucion());
			ps.setString(4, dto.getDescripcion());
			ps.setString(5, dto.getTipo());
			ps.setString(6, dto.getActivo());
			ps.setString(7, dto.getFechaModificaFormatoBD());
			ps.setString(8, dto.getHoraModifica());
			ps.setString(9, dto.getUsuarioModifica());
			ps.setString(10, dto.getIndicativo());
			
			
			
			logger.info("--------------------SQL   INSERTAR  MOTIVOS DE DESCUENTO-  -------------------------- ");
			logger.info(ps);
			ps.executeUpdate();
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch(SQLException e)
		{
			logger.error("ERROR en insert" + e);
			secuencia = ConstantesBD.codigoNuncaValidoDouble;
		}
		return secuencia;
	}
	
	/**
	 * 
	 * @param objetoDescuento
	 * @return
	 */
	public static boolean modificar(DtoMotivoDescuento dtoNuevo, DtoMotivoDescuento dtoWhere )
	{
		
		logger.info("****************************************************************************************************************");
		logger.info("									 MODIFICAR MOTIVOS AUTORIZACION DESCUENTOO 									 ");
		logger.info("****************************************************************************************************************");
		
		boolean retorna= false;
		String actualizarDescuento ="UPDATE odontologia.motivos_descuentos_odon set codigo_pk=codigo_pk ";
		
		
		actualizarDescuento+=UtilidadTexto.isEmpty(dtoNuevo.getCodigoMotivo())?"":",codigo_motivo = '"+dtoNuevo.getCodigoMotivo()+"' ";
		actualizarDescuento+=UtilidadTexto.isEmpty(dtoNuevo.getDescripcion())?"":",descripcion = '"+dtoNuevo.getDescripcion()+"' ";
		actualizarDescuento+=UtilidadTexto.isEmpty(dtoNuevo.getTipo())?"":",tipo = '"+dtoNuevo.getTipo()+"' ";
		actualizarDescuento+=UtilidadTexto.isEmpty(dtoNuevo.getActivo())?"":",activo = '"+dtoNuevo.getActivo()+"' ";
		actualizarDescuento+=UtilidadTexto.isEmpty(dtoNuevo.getFechaModificaFormatoBD())?"":",fecha_modifica = '"+dtoNuevo.getFechaModificaFormatoBD()+"' ";
		actualizarDescuento+=UtilidadTexto.isEmpty(dtoNuevo.getHoraModifica())?"":", hora_modifica = '"+dtoNuevo.getHoraModifica()+"' ";
		actualizarDescuento+=UtilidadTexto.isEmpty(dtoNuevo.getUsuarioModifica())?"":", usuario_modifica = '"+dtoNuevo.getUsuarioModifica()+"' ";
		actualizarDescuento+=UtilidadTexto.isEmpty(dtoNuevo.getIndicativo())?"": " ,indicativo='"+dtoNuevo.getIndicativo()+"'";
		
		actualizarDescuento+= " where  1=1 ";
		
		if(dtoWhere.getCodigoPk() >=0)
		{
			actualizarDescuento+=" and codigo_pk = " +dtoWhere.getCodigoPk();
		}
		if(!UtilidadTexto.isEmpty(dtoWhere.getCodigoMotivo()))
		{
			actualizarDescuento+=" and codigo_motivo = '" +dtoWhere.getCodigoMotivo()+"' ";
		}
		if(dtoWhere.getInstitucion() >=0)
		{
			actualizarDescuento+=" and institucion = " +dtoWhere.getInstitucion();
		}
		if(!UtilidadTexto.isEmpty(dtoWhere.getDescripcion()))
		{
			actualizarDescuento+=" and descripcion = '" +dtoWhere.getDescripcion()+"' ";
		}
		if(!UtilidadTexto.isEmpty(dtoWhere.getTipo()))
		{
			actualizarDescuento+=" and tipo = '" +dtoWhere.getTipo()+"' ";
		}
		if(!UtilidadTexto.isEmpty(dtoWhere.getActivo()))
		{
			actualizarDescuento+=" and activo = '" +dtoWhere.getActivo()+"' ";
		}
		
		try
		{
			ResultSetDecorator rs = null;
			Connection con = UtilidadBD.abrirConexion();

			logger.info("	SQL MODIFICAR MOTIVOS \n\n------------------------------------------------------------------> " + actualizarDescuento);
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con , actualizarDescuento);
			retorna= ps.executeUpdate()<=0;
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch(SQLException e)
		{
			logger.error("ERROR EN updateDetalleDescuento "+e);
		}
		
		return retorna;
	}
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static ArrayList<DtoMotivoDescuento> cargar(DtoMotivoDescuento dto)
	{
		ArrayList<DtoMotivoDescuento> arrayDtoMotivoDescuento = new ArrayList<DtoMotivoDescuento>();
		String consultaDetalle = "SELECT " +
									"codigo_pk, codigo_motivo, institucion, descripcion, tipo, activo, fecha_modifica, hora_modifica, usuario_modifica, indicativo " +
								" from odontologia.motivos_descuentos_odon where 1=1 ";
		
		if(dto.getCodigoPk()>0)
		{
			consultaDetalle+=" AND codigo_pk = " + dto.getCodigoPk() + "";
		}
		
		if(!UtilidadTexto.isEmpty(dto.getIndicativo()))
		{
			consultaDetalle+=" AND indicativo='"+dto.getIndicativo()+"'";
		}
		
		if(!UtilidadTexto.isEmpty(dto.getCodigoMotivo()))
		{
			consultaDetalle+=" AND codigo_motivo = '" + dto.getCodigoMotivo() + "'";
		}
		if(dto.getInstitucion()>ConstantesBD.codigoNuncaValido)
		{
			consultaDetalle+=" AND institucion = " + dto.getInstitucion() + "";
		}
		if(!UtilidadTexto.isEmpty(dto.getDescripcion()))
		{
			consultaDetalle+=" AND descripcion = '" + dto.getDescripcion() + "'";
		}
		if(!UtilidadTexto.isEmpty(dto.getTipo()))
		{
			consultaDetalle+=" AND tipo = '" + dto.getTipo() + "'";
		}
		if(!UtilidadTexto.isEmpty(dto.getActivo()))
		{
			consultaDetalle+=" AND activo = '" + dto.getActivo() + "'";
		}
		if(!UtilidadTexto.isEmpty(dto.getFechaModifica()))
		{
			consultaDetalle+=" AND fecha_modifica = '" + dto.getFechaModifica() + "'";
		}
		if(!UtilidadTexto.isEmpty(dto.getHoraModifica()))
		{
			consultaDetalle+=" AND hora_modifica = '" + dto.getHoraModifica() + "'";
		}
		if(!UtilidadTexto.isEmpty(dto.getUsuarioModifica()))
		{
			consultaDetalle+=" AND usuario_modifica = '" + dto.getUsuarioModifica() + "'";
		}
		
		logger.info("\n\n\n\n\n SQL cargarDescuento / " + consultaDetalle);
		try
		{
			Connection con = UtilidadBD.abrirConexion();
			ResultSetDecorator rs=null;
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consultaDetalle+" ORDER BY codigo_motivo ", ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			rs = new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				DtoMotivoDescuento dtoPrueba = new DtoMotivoDescuento();
				dtoPrueba.setCodigoPk(rs.getDouble("codigo_pk"));
				dtoPrueba.setCodigoMotivo(rs.getString("codigo_motivo"));
				dtoPrueba.setInstitucion(rs.getInt("institucion"));
				dtoPrueba.setDescripcion(rs.getString("descripcion"));
				dtoPrueba.setTipo(rs.getString("tipo"));
				dtoPrueba.setActivo(rs.getString("activo"));
				dtoPrueba.setFechaModifica(rs.getString("fecha_modifica"));
				dtoPrueba.setHoraModifica(rs.getString("hora_modifica"));
				dtoPrueba.setUsuarioModifica(rs.getString("usuario_modifica"));
				dtoPrueba.setIndicativo(rs.getString("indicativo"));
				arrayDtoMotivoDescuento.add(dtoPrueba);
			}
			
			ps.executeQuery();
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			
		}
		catch(SQLException e)
		{
			logger.error("error en carga==> " + e);
		}
		return arrayDtoMotivoDescuento;
	}
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static boolean eliminar( DtoMotivoDescuento dto)
	{
		String consultaStr="DELETE FROM odontologia.motivos_descuentos_odon WHERE 1=1 ";

		consultaStr+=(0<dto.getCodigoPk())?" AND codigo_pk = "+dto.getCodigoPk():"";
        consultaStr+=UtilidadTexto.isEmpty(dto.getCodigoMotivo())?"":" AND codigo_motivo ='"+dto.getCodigoMotivo()+"'";
        consultaStr+=(0<dto.getInstitucion())?" AND institucion= "+dto.getInstitucion():"";
        consultaStr+=UtilidadTexto.isEmpty(dto.getDescripcion())?"":" AND  descripcion ='"+dto.getDescripcion()+"'";
        consultaStr+=UtilidadTexto.isEmpty(dto.getActivo())?"":" AND  activo ='"+dto.getActivo()+"'";
        consultaStr+=UtilidadTexto.isEmpty(dto.getTipo())?"":" AND  tipo ='"+dto.getTipo()+"'";
        consultaStr+=UtilidadTexto.isEmpty(dto.getFechaModifica())?"":" AND fecha_modifica ='"+dto.getFechaModifica()+"' ";
	    consultaStr+=UtilidadTexto.isEmpty(dto.getHoraModifica())?"":"  AND hora_modifica= '"+dto.getHoraModifica()+"' ";
	    consultaStr+=UtilidadTexto.isEmpty(dto.getUsuarioModifica())?"":" AND usuario_modifica= '"+dto.getUsuarioModifica()+"' ";
	    
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.executeUpdate();
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, null, con);
			logger.info("Eliminar "+consultaStr);
			return true;
		} 
		catch (SQLException e) 
		{
			//logger.error("ERROR EN eliminarDetalleCargoXSolicitudServArt "+);
		}
		return false;
		
	}
	
	
	
	/**
	 * M&eacute;todo para validar existencia de un motivo de atencion.
	 * en el Descuento Odontologico
	 * @param codigoPkMotivo
	 * @return
	 */
	public static int validarExistenciaMotivos(double codigoPkMotivo)
	{
		int retorno=ConstantesBD.codigoNuncaValido;
		
		String consultaStr="select count(0) as cantidad from odontologia.presupuesto_dcto_odon where motivo="+codigoPkMotivo;
		
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			ResultSetDecorator rs=null;
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr);
			rs = new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{
				retorno=rs.getInt("cantidad");
			}
			
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			logger.info("Eliminar "+consultaStr);
			
		} 
		catch (SQLException e) 
		{
			//logger.error("ERROR EN eliminarDetalleCargoXSolicitudServArt "+e);
		}
		
		
		return retorno;
	}
}
