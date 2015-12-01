package com.princetonsa.dao.sqlbase.odontologia;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadLog;
import util.UtilidadTexto;

import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.odontologia.DtoAliadoOdontologico;

/**
 * 
 * @author axioma
 *
 */
public class SqlBaseAliadoOdontologicoDao 
{
	/**
	 * 
	 */
	private static Logger logger = Logger.getLogger(SqlBaseAliadoOdontologicoDao.class);
	
	/**
	 * cadena para Insertar
	 * 
	 */
	private static String insercionStr = "INSERT INTO odontologia.aliado_odontologico "+
																							"(codigo_pk, " +//1
																							"codigo, " +//2
																							"institucion, " +//3
																							"tercero, " +//4
																							"descripcion, " +//5
																							"direccion, " +//6
																							"telefono, " +//7
																							"observaciones, " +//8
																							"estado, " +//9
																							"fecha_modifica, " +//10
																							"hora_modifica, " +//11
																							"usuario_modifica " +//12
																							
																							")values (" +
																							"? , " +//1
																							"? , " +//2
																							"? , " +//3
																							"? , " +//4
																							"? , " +//5
																							"? , " +//6
																							"? , " +//7
																							"? , " +//8
																							"? , " +//9
																							"? , " +//10
																							"? , " +//11
																							"? ) ";//12
	
	/**
	 * @param AliadoOdontologico
	 * @return
	 */
	public static boolean modificar(DtoAliadoOdontologico dtoNuevo, DtoAliadoOdontologico dtoWhere )
	{
		logger.info(UtilidadLog.obtenerString(dtoNuevo, true)); 
		boolean retorna= false;
		String actualizarAliado ="UPDATE odontologia.aliado_odontologico set codigo_pk=codigo_pk ";
		
		actualizarAliado+= !UtilidadTexto.isEmpty(dtoNuevo.getCodigo())?"  ,  codigo= '"+dtoNuevo.getCodigo()+"' ":"";
		actualizarAliado+= (dtoNuevo.getInstitucion()>ConstantesBD.codigoNuncaValido)?" , institucion= "+dtoNuevo.getInstitucion():"";
		actualizarAliado+= (dtoNuevo.getTerceros().getCodigo()>ConstantesBD.codigoNuncaValido)?"  , tercero= "+dtoNuevo.getTerceros().getCodigo():"";
		actualizarAliado+= !UtilidadTexto.isEmpty(dtoNuevo.getDescripcion())?"  ,  descripcion=  '"+dtoNuevo.getDescripcion()+"' ":"";
		actualizarAliado+= !UtilidadTexto.isEmpty(dtoNuevo.getDireccion())?"  ,  direccion=  '"+dtoNuevo.getDireccion()+"' ":"";
		actualizarAliado+= !UtilidadTexto.isEmpty(dtoNuevo.getTelefono())?"  ,  telefono=  '"+dtoNuevo.getTelefono()+"' ":"";
		actualizarAliado+= !UtilidadTexto.isEmpty(dtoNuevo.getObservaciones())?"  ,  observaciones= '"+dtoNuevo.getObservaciones()+"' ":"";
		actualizarAliado+= !UtilidadTexto.isEmpty(dtoNuevo.getEstado())?"  ,  estado= '"+dtoNuevo.getEstado()+"' ":"";
		actualizarAliado+= !UtilidadTexto.isEmpty(dtoNuevo.getFechaModifica())?" , fecha_modifica= '"+UtilidadFecha.conversionFormatoFechaABD(dtoNuevo.getFechaModifica())+"' ":"";
		actualizarAliado+= !UtilidadTexto.isEmpty(dtoNuevo.getHoraModifica())?" , hora_modifica= '"+UtilidadFecha.convertirHoraACincoCaracteres(dtoNuevo.getHoraModifica())+"' ":"";
		actualizarAliado+= !UtilidadTexto.isEmpty(dtoNuevo.getUsuarioModifica())?" , usuario_modifica= '"+dtoNuevo.getUsuarioModifica()+"' ":"";
		
		actualizarAliado+= " where  codigo_pk = " +dtoWhere.getCodigoPk();
		
		try
		{
			ResultSetDecorator rs = null;
			Connection con = UtilidadBD.abrirConexion();
			logger.info("actualizarAliado------------------------------------------------------------------> " + actualizarAliado);
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(actualizarAliado, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			retorna= ps.executeUpdate()<=0;
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch(SQLException e)
		{
			logger.error("ERROR EN updateDetalleAliado "+e);
		}
		
		return retorna;
	}
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static ArrayList<DtoAliadoOdontologico> cargar(DtoAliadoOdontologico dto)
	{
		ArrayList<DtoAliadoOdontologico> arrayDtoAliadoOdontologico = new ArrayList<DtoAliadoOdontologico>();
		String consultaDetalle = "SELECT " +
											"ao.codigo_pk, " +
											"ao.codigo, " +
											"ao.institucion, " +
											"ao.tercero, " +
											"ao.descripcion, " +
											"ao.direccion, " +
											"ao.telefono, " +
											"ao.observaciones, " +
											"ao.estado, " +
											"ao.fecha_modifica, " +
											"ao.hora_modifica, " +
											"ao.usuario_modifica," +
											" ter.numero_identificacion ||'  '||ter.descripcion as  descripcionter, " +
											"(select count(1) from odontologia.beneficiario_tarjeta_cliente btc where btc.alidado_odontologico=ao.codigo_pk) as puedomodificar " +
											"from odontologia.aliado_odontologico ao " +
											"INNER JOIN facturacion.terceros ter on (ter.codigo = ao.tercero ) " +
											"where 1=1 ";
		
		
		if(dto!=null)
		{
			if(dto.getCodigoPk()>0)
			{
				consultaDetalle+=" AND ao.codigoPk="+dto.getCodigoPk() + "";
			}
			if(!UtilidadTexto.isEmpty(dto.getCodigo()))
			{
				consultaDetalle+=" AND ao.codigo='"+dto.getCodigo() + "'";
			}
			if(dto.getInstitucion()>ConstantesBD.codigoNuncaValido)
			{
				consultaDetalle+=" AND ao.institucion="+dto.getInstitucion() + "";
			}
			if(dto.getTerceros().getCodigo()!=0)
			{
				consultaDetalle+=" AND ao.tercero="+dto.getTerceros().getCodigo() + "";
			}
			if(!UtilidadTexto.isEmpty(dto.getDescripcion()))
			{
				consultaDetalle+=" AND upper(ao.descripcion) like upper('%"+dto.getDescripcion() + "%') ";
			}
			if(!UtilidadTexto.isEmpty(dto.getDireccion()))
			{
				consultaDetalle+=" AND ao.direccion='"+dto.getDireccion() + "'";
			}
			if(!UtilidadTexto.isEmpty(dto.getTelefono()))
			{
				consultaDetalle+=" AND ao.telefono='"+dto.getTelefono() + "'";
			}
			if(!UtilidadTexto.isEmpty(dto.getObservaciones()))
			{
				consultaDetalle+=" AND ao.observaciones='"+dto.getObservaciones() + "'";
			}
			if(!UtilidadTexto.isEmpty(dto.getEstado()))
			{
				consultaDetalle+=" AND ao.estado='"+dto.getEstado() +"'";
			}
			if(!UtilidadTexto.isEmpty(dto.getFechaModifica()))
			{
				consultaDetalle+=" AND ao.fecha_modifica='"+UtilidadFecha.obtenerFechaSinHora(dto.getFechaModifica())+"'";
			}
			if(!UtilidadTexto.isEmpty(dto.getHoraModifica()))
			{
				consultaDetalle+=" AND ao.hora_modifica='"+dto.getHoraModifica() + "'";
			}
			if(!UtilidadTexto.isEmpty(dto.getUsuarioModifica()))
			{
				consultaDetalle+=" AND ao.usuario_modifica='"+dto.getUsuarioModifica() + "'";
			}
			if(!UtilidadTexto.isEmpty(dto.getNitTerceroRazSoci()))
			{
				consultaDetalle+=" AND ter.numero_identificacion= '"+dto.getNitTerceroRazSoci()+"' ";
			}
			
			consultaDetalle+=" ORDER BY ao.descripcion ";
		}
		
		logger.info("\n\n\n\n\n SQL cargarAliado / " + consultaDetalle);
		try
		{
			
			Connection con = UtilidadBD.abrirConexion();
			ResultSetDecorator rs=null;
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consultaDetalle, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			rs = new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				DtoAliadoOdontologico dtoPrueba = new DtoAliadoOdontologico();
				dtoPrueba.setCodigoPk(rs.getInt("codigo_pk"));
				dtoPrueba.setCodigo(rs.getString("codigo"));
				dtoPrueba.setInstitucion(rs.getInt("institucion"));
				dtoPrueba.getTerceros().setCodigo(rs.getInt("tercero"));
				dtoPrueba.getTerceros().setDescripcion(rs.getString("descripcionter"));
				dtoPrueba.setDescripcion(rs.getString("descripcion"));
				dtoPrueba.setDireccion(rs.getString("direccion"));
				dtoPrueba.setTelefono(rs.getString("telefono"));
				dtoPrueba.setObservaciones(rs.getString("observaciones"));
				dtoPrueba.setEstado(rs.getString("estado"));
				dtoPrueba.setFechaModifica(rs.getString("fecha_modifica"));
				dtoPrueba.setHoraModifica(rs.getString("hora_modifica"));
				dtoPrueba.setUsuarioModifica(rs.getString("usuario_modifica"));
				dtoPrueba.setPuedoModificar(rs.getInt("puedomodificar")<=0);
				arrayDtoAliadoOdontologico.add(dtoPrueba);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			
		}
		catch(SQLException e)
		{
			logger.error("error en carga==> " + e);
		}
		return arrayDtoAliadoOdontologico;
		
	}
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static boolean eliminar( DtoAliadoOdontologico dto)
	{
		String consultaStr="DELETE FROM odontologia.aliado_odontologico WHERE 1=1 ";

		consultaStr+=(0<dto.getCodigoPk())?" AND codigo_pk = "+dto.getCodigoPk():"";
        consultaStr+=UtilidadTexto.isEmpty(dto.getCodigo())?"":" AND codigo_motivo ='"+dto.getCodigo()+"'";
        consultaStr+=(0<dto.getInstitucion())?" AND institucion= "+dto.getInstitucion():"";
        consultaStr+=(0<dto.getTerceros().getCodigo())?" AND tercero= "+dto.getTerceros().getCodigo()+"'":"";
        consultaStr+=UtilidadTexto.isEmpty(dto.getDescripcion())?"":" AND  descripcion ='"+dto.getDescripcion()+"'";
        consultaStr+=UtilidadTexto.isEmpty(dto.getDireccion())?"":" AND direccion ='"+dto.getDireccion()+"'";
        consultaStr+=UtilidadTexto.isEmpty(dto.getTelefono())?"":" AND telefono ='"+dto.getTelefono()+"'";
        consultaStr+=UtilidadTexto.isEmpty(dto.getObservaciones())?"":" AND observaciones ='"+dto.getObservaciones()+"'";
        consultaStr+=UtilidadTexto.isEmpty(dto.getEstado())?"":" AND estado ='"+dto.getEstado()+"'";
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
			logger.error("ERROR EN eliminarDetalleCargoXSolicitudServArt "+e);
		}
		return false;
	}
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static double guardar( DtoAliadoOdontologico dto )
	{
		double secuencia = ConstantesBD.codigoNuncaValidoDouble;
		
		try
		{
			Connection con = UtilidadBD.abrirConexion();
			ResultSetDecorator rs = null;
			secuencia=UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_aliado_odontologico");
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(insercionStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, secuencia);
			ps.setString(2, dto.getCodigo());
			ps.setInt(3, dto.getInstitucion());
			ps.setInt(4, dto.getTerceros().getCodigo());
			ps.setString(5, dto.getDescripcion());
			ps.setString(6, dto.getDireccion());
			ps.setString(7, dto.getTelefono());
			ps.setString(8, dto.getObservaciones());
			ps.setString(9, dto.getEstado());
			ps.setDate(10, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dto.getFechaModifica())));
			
			ps.setString(11, dto.getHoraModifica());
			ps.setString(12, dto.getUsuarioModifica());
			
			logger.info("Parametros" + dto.getCodigoPk()+ ""+ "Codigo" +dto.getCodigo()+""+ "Institucion"  +dto.getInstitucion()+""+ "Terceros" +dto.getTerceros()+""+ "Descripcion" +dto.getDescripcion()+""+ "Direccion" +dto.getDireccion()+""+ "Telefono" +dto.getTelefono()+""+ "Observaciomes" +dto.getObservaciones()+""+ "Estado" +dto.getEstado()+""+ "Hora Modifica" +dto.getHoraModifica()+""+"Fecha Modifica" +dto.getFechaModifica()+""+ "Usuario Modifica" +dto.getUsuarioModifica());
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
	
	
	
}