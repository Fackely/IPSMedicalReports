package com.princetonsa.dao.sqlbase.odontologia;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.odontologia.DtoMotivosAtencion;


public class SqlBaseMotivosAtencionOdontologicaDao
{
	/**
	 * Para el manejo de logs
	 */
	private static Logger logger = Logger.getLogger(SqlBaseMotivosAtencionOdontologicaDao.class);

	private static String consultarTiposMotivo="SELECT codigo, nombre FROM odontologia.tipos_motivo_atencion ";
	
	private static String insertarMotivoAtencionO="INSERT INTO odontologia.motivos_atencion (codigo_pk,codigo,institucion," +
												   "nombre,tipo,fecha_modifica,hora_modifica,usuario_modifica) " +
												   "VALUES(?,?,?,?,?,CURRENT_DATE,?,?) ";
	
	private static String consultarMotivoAtencionO="SELECT ma.codigo_pk AS codigopk, " +
														"ma.codigo AS codigo, " +
														"to_number(ma.codigo, '99999999') AS codigo_orden, "+
														"ma.institucion AS institucion, " +
														"ma.nombre AS nombre, " +
														"ma.tipo AS tipo, " +
														"ma.fecha_modifica AS fecha, " +
														"ma.hora_modifica AS hora," +
														"ma.usuario_modifica AS usuario, " +
														"tm.nombre AS descTipo " +
													"FROM odontologia.motivos_atencion ma " +
													"INNER JOIN odontologia.tipos_motivo_atencion tm ON(ma.tipo=tm.codigo)  " +
													"where 1=1 ";
	
	private static String modificarMotivoAtencionO="UPDATE odontologia.motivos_atencion SET codigo=?,institucion=?," +
													"nombre=?,tipo=?,fecha_modifica=CURRENT_DATE,hora_modifica=?,usuario_modifica=? " +
													"WHERE codigo_pk=?";
	
	private static String eliminarMotivoAtencionO="DELETE FROM odontologia.motivos_atencion WHERE codigo_pk=? ";
	

	public static boolean eliminarMotivoAtencionO(int codigoPk) 
	{
		try
		{
			Connection con= UtilidadBD.abrirConexion();
			logger.info("\n\nquery: "+eliminarMotivoAtencionO+" codigo: "+codigoPk);
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(eliminarMotivoAtencionO,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoPk);
			ps.executeUpdate();
			
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, null, con);
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. ELIMINANDO MOTIVOS------>>>>>>"+e);
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static boolean modificarMotivoAtencionO(int codigoPk,String codigoNuevo, String nombreNuevo, int tipoNuevo,int codigoInstitucionInt, String loginUsuario) 
	{
		try 
		{
			Connection con= UtilidadBD.abrirConexion();
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(modificarMotivoAtencionO, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			ps.setString(1,codigoNuevo);
			ps.setInt(2,codigoInstitucionInt);
			ps.setString(3,nombreNuevo);
			ps.setInt(4,tipoNuevo);
			ps.setString(5,UtilidadFecha.getHoraActual());
			ps.setString(6,loginUsuario);
			ps.setInt(7,codigoPk);
			
			if(ps.executeUpdate()>0)
			{
				ps.close();
				UtilidadBD.cerrarConexion(con);
				return true;
			}
			ps.close();
			UtilidadBD.cerrarConexion(con);
				
		}
		catch (SQLException e) 
		{	
			logger.info("ERROR ACTUALIZANDO MOTIVOS "+e);		
		}
		
		return false;
	}

	/**
	 * 
	 * @param tipos
	 * @param institucion
	 * @return
	 */
	public static ArrayList<DtoMotivosAtencion> consultarMotivoAtencionO(ArrayList<Integer> tipos, int institucion) 
	{
		ArrayList<DtoMotivosAtencion> listaDtoMotivosAtencion= new ArrayList<DtoMotivosAtencion>();
		String consulta=consultarMotivoAtencionO;
		String orderby = " ORDER BY codigo_orden DESC ";
		
		if(tipos.size()>0)
		{
			consulta+=" and ma.tipo in("+UtilidadTexto.convertirArrayIntegerACodigosSeparadosXComas(tipos)+") ";
		}
		if(institucion>0)
		{
			consulta+=" and ma.institucion= "+institucion;
		}
		consulta+=orderby;
		
		try
		{
			Connection con= UtilidadBD.abrirConexion();
			
			//PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta+ " ORDER BY ma.nombre ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			logger.info("\n\nconsulta::::::: "+consulta);
			
			while(rs.next())
			{
				DtoMotivosAtencion dto= new DtoMotivosAtencion();
				dto.setCodigoPk(rs.getInt("codigopk"));
				dto.setCodigo(rs.getString("codigo"));
				dto.setInstitucion(rs.getString("institucion"));
				dto.setNombre(rs.getString("nombre"));
				dto.setTipo(rs.getInt("tipo"));
				dto.setFecha(rs.getString("fecha"));
				dto.setHora(rs.getString("hora"));
				dto.setUsuario(rs.getString("usuario"));	
				dto.setDescTipo(rs.getString("descTipo"));
				listaDtoMotivosAtencion.add(dto);
			}
			rs.close();
			ps.close();
			UtilidadBD.cerrarConexion(con);
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR CONSULTANDO MOTIVOS ATENCION.------>>>>>>"+e);
			e.printStackTrace();
		}	
		
		return listaDtoMotivosAtencion;
	}
	
	public static HashMap consultarTiposMotivo() 
	{
		HashMap resultado= new HashMap();
		PreparedStatementDecorator ps;
		
		try
		{
			Connection con= UtilidadBD.abrirConexion();
			ps =  new PreparedStatementDecorator(con.prepareStatement(consultarTiposMotivo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			resultado=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
			ps.close();
			UtilidadBD.cerrarConexion(con);
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR CONSULTANDO TIPOS.------>>>>>>"+e);
			e.printStackTrace();
		}			
		
		return resultado;
	}

	public static boolean insertarMotivoAtencionO(String codigoNuevo,String nombreNuevo, int tipoNuevo, int institucion, String usuario) 
	{
		try {
			Connection con= UtilidadBD.abrirConexion();
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(insertarMotivoAtencionO, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));		
				ps.setInt(1,UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_motivos_atencion"));
				ps.setString(2,codigoNuevo);
				ps.setInt(3,institucion);
				ps.setString(4,nombreNuevo);
				ps.setInt(5,tipoNuevo);
				ps.setString(6, UtilidadFecha.getHoraActual());
				ps.setString(7,usuario);
				
				if(ps.executeUpdate()>0)
				{	
					ps.close();
					UtilidadBD.cerrarConexion(con);
					return true;
				}
				ps.close();
				UtilidadBD.cerrarConexion(con);				
			}
			catch (SQLException e) 
			{	
				logger.info("ERROR INGRESAR MOTIVO ATENCION---> "+e);
			}		
		return false;
	}
}