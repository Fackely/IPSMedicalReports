package com.princetonsa.dao.sqlbase.consultaExterna;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;

public class SqlBaseMotivosCancelacionCitaDao 
{

	
	
	/**
	 * objeto para manejar los logs de esta clase
	 */
	
	public static Logger logger=Logger.getLogger(SqlBaseMotivosCancelacionCitaDao.class);
	
	
	
	
	
	/**
	 * 
	 */
	private static final String cadenaConsultaEspecificoStr=  "SELECT " +
																" codigo as codigo," +
																" descripcion as descripcion," +
																" activo as activo," +
																" tipo_cancelacion as tipocancelacion," +
																" 'BD' as tiporegistro " +
													" from motivos_cancelacion_cita " +
													"where codigo=?";
	
	
	
	/**
	 * 
	 */
	private static final String cadenaConsultaStr=  "SELECT " +
																		" codigo as codigo," +
																		" descripcion as descripcion," +
																		" activo as activo," +
																		" tipo_cancelacion as tipocancelacion," +
																		" 'BD' as tiporegistro " +
																	" from motivos_cancelacion_cita ";
	
	
	/**
	 * Cadena para la eliminacion
	 */
	private static final String cadenaEliminacionStr="DELETE FROM motivos_cancelacion_cita WHERE codigo=?";
	
		
	
	/**
	 * cadena para la modificacion
	 */
	
	private static final String cadenaModificacionStr="UPDATE motivos_cancelacion_cita SET descripcion=?, tipo_cancelacion=?, activo=?, usuario_modifica=?, fecha_modifica=?, hora_modifica=?  WHERE codigo=? ";
	
	
	
	/**
	 * cadena para la insercion
	 */
	
	private static final String cadenaInsertarStr="INSERT INTO motivos_cancelacion_cita (codigo, descripcion, tipo_cancelacion, activo, usuario_modifica, fecha_modifica, hora_modifica) VALUES (?, ?, ?, ?, ?, ?, ?)";
	
	
	
	/**
	 * 
	 * @param con
	 * @param codigoMotivo
	 * @return
	 */
	public static HashMap consultarMotivoEspecifico(Connection con, int codigoMotivo) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		try
		{
			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaEspecificoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1, codigoMotivo);
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
	 * @return
	 */
	public static HashMap consultarMotivosExistentes(Connection con,HashMap parametros) 
	{
		HashMap mapa=new HashMap();
		String cadena = cadenaConsultaStr;
		int bandera = ConstantesBD.codigoNuncaValido;
		
		if(parametros.containsKey("codigosInsertados") && 
				!parametros.get("codigosInsertados").toString().equals(""))
		{
			cadena += " WHERE codigo NOT IN ("+parametros.get("codigosInsertados").toString()+ConstantesBD.codigoNuncaValido+") " ;
			bandera = 0;
		}
		
		//**********INICIO TAREA 61450 ***************
		if(parametros.containsKey("tipoCancelacion") && 
				!parametros.get("tipoCancelacion").toString().equals(""))
		{
			if(bandera == 0)
				cadena += " AND tipo_cancelacion = "+parametros.get("tipoCancelacion").toString()+" ";
			else
				cadena += " WHERE tipo_cancelacion = "+parametros.get("tipoCancelacion").toString()+" ";
		}
		//**********FIN TAREA 61450 ******************
		
		cadena += " ORDER BY descripcion ASC ";
			
		mapa.put("numRegistros","0");
		logger.info("===>Consulta Motivos Cancelacion: "+cadena);
		try
		{			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
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
	 * @param codigoMotivo
	 * @return
	 */
	public static boolean eliminarRegistro(Connection con, int codigoMotivo) 
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaEliminacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoMotivo);
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean insertar(Connection con, HashMap vo) 
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			int codigoMotivo=UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_mot_cancelacion_cita");
			
			ps.setInt(1, codigoMotivo);
			ps.setString(2, vo.get("descripcion").toString());
			ps.setString(3, vo.get("tipo_cancelacion").toString());
			
			if(UtilidadTexto.isEmpty(vo.get("activo")+""))
				ps.setString(4, ConstantesBD.acronimoNo);
			else
				ps.setString(4, vo.get("activo").toString());
				
			ps.setString(5, vo.get("usuario_modifica").toString());
			ps.setDate(6, Date.valueOf(vo.get("fecha_modifica").toString()));
			ps.setString(7, vo.get("hora_modifica").toString());
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean modificar(Connection con, HashMap vo) 
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaModificacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, vo.get("descripcion")+"");
			ps.setString(2, vo.get("tipo_cancelacion")+"");
			
			if(UtilidadTexto.isEmpty(vo.get("activo")+""))
				ps.setString(3, ConstantesBD.acronimoNo);
			else
				ps.setString(3, vo.get("activo").toString());
			
			ps.setString(4, vo.get("usuario_modifica").toString());
			ps.setDate(5, Date.valueOf(vo.get("fecha_modifica").toString()));
			ps.setString(6, vo.get("hora_modifica").toString());
			ps.setInt(7, Utilidades.convertirAEntero(vo.get("codigo").toString()));
			
			
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

}
