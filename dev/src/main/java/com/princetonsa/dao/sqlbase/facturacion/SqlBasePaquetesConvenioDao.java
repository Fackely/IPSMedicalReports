package com.princetonsa.dao.sqlbase.facturacion;


import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;


import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;



public class SqlBasePaquetesConvenioDao
{
	
	/**
	 * objeto para manejar los logs de esta clase
	 */
	
	public static Logger logger=Logger.getLogger(SqlBasePaquetesConvenioDao.class);
	
	
	/**
	 * Cadena para la eliminacion
	 */
	
	
	
	private static final String cadenaConsultaStr=  "SELECT " +
																" pc.codigo as codigo," +
																" pc.institucion as institucion," +
																" pc.convenio as convenio," +
																" pc.contrato as contrato," +
																" pc.paquete as paquete," +
																" pc.tipo_paciente as tipopaciente, " +
																" p.descripcion as descpaquete," +
																" getNombreTipoPaciente(pc.tipo_paciente) as nomtipopaciente,"+
																" COALESCE(pc.via_ingreso,"+ConstantesBD.codigoNuncaValido+") as viaingreso," +
																" COALESCE(getnombreviaingreso(via_ingreso),getnombreviaingreso("+ConstantesBD.codigoNuncaValido+")) as nomviaingreso," +
																" to_char(pc.fecha_inicial_venc,'DD/MM/YYYY') as fechainicialvenc," +
																" to_char(pc.fecha_final_venc, 'DD/MM/YYYY') as fechafinalvenc," +
																" 'BD' as tiporegistro " +
													" from paquetes_convenio pc " +
													" inner join paquetes p on (p.codigo_paquete=pc.paquete and p.institucion=pc.institucion) ";
													
	
	
	
	/**
	 * 
	 */
	
	private static final String cadenaEliminacionStr="DELETE FROM paquetes_convenio WHERE codigo=?";
	
		
	
	/**
	 * cadena para la modificacion
	 */
	
	private static final String cadenaModificacionStr="UPDATE paquetes_convenio SET usuario_modifica=?, convenio=?, contrato=?, paquete=?, via_ingreso=?, tipo_paciente=?, fecha_inicial_venc=?, fecha_final_venc=?, fecha_modifica=?, hora_modifica=?, institucion=?  WHERE codigo=?";
	
	
	
	/**
	 * cadena para la insercion
	 */
	
/*	private static final String cadenaInsertarStr="INSERT INTO paquetes_convenio (codigo, institucion, convenio, contrato, paquete, via_ingreso, fecha_inicial_venc, fecha_final_venc, usuario_modifica, fecha_nuevo, hora_nuevo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";*/

	
	
	/**
	 * consulta los paquetes existentes
	 * @param con
	 * @param institucion
	 * @return
	 */
	
	public static HashMap consultarPaquetesConvenioExistentes(Connection con, HashMap vo) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		String cadena= cadenaConsultaStr+"where codigo=? ";
		
		try
		{
			if(vo.containsKey("codigo"))
			{
				cadena+=" and pc.codigo='"+vo.get("codigo")+"'";
			}
			cadena+=" ORDER BY codigo ";
			logger.info("consulta-->"+cadena+" codigo-->"+vo.get("codigo"));
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(vo.get("codigo")+""));
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
	 * @param vo
	 * @return
	 */
	
	public static boolean insertar(Connection con,String cadena, HashMap vo)
	{
	  
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO paquetes_convenio (
			 * codigo,
			 * institucion,
			 * convenio,
			 * contrato, 
			 * paquete, 
			 * via_ingreso, 
			 * tipo_paciente, 
			 * fecha_inicial_venc, 
			 * fecha_final_venc, 
			 * usuario_modifica, 
			 * fecha_modifica, 
			 * hora_modifica) VALUES ('seq_paq_convenios'),?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)
			 */
			
			ps.setInt(1, Utilidades.convertirAEntero(vo.get("institucion")+""));
			if(UtilidadTexto.isEmpty(vo.get("convenio")+""))
				ps.setObject(2, null);
			else
				ps.setInt(2, Utilidades.convertirAEntero(vo.get("convenio")+""));
			if(UtilidadTexto.isEmpty(vo.get("contrato")+""))
				ps.setObject(3, null);
			else
				ps.setInt(3, Utilidades.convertirAEntero(vo.get("contrato")+""));
			if(UtilidadTexto.isEmpty(vo.get("paquete")+""))
				ps.setObject(4, null);
			else
				ps.setString(4, vo.get("paquete")+"");
			
			if(Utilidades.convertirAEntero(vo.get("via_ingreso")+"")==ConstantesBD.codigoNuncaValido)
				ps.setObject(5, null);
			else
				ps.setInt(5, Utilidades.convertirAEntero(vo.get("via_ingreso")+""));
			
			if(vo.get("tipo_paciente").toString().equals(""))
				ps.setNull(6, Types.VARCHAR);
			else
				ps.setString(6, vo.get("tipo_paciente").toString());
			
			if(!UtilidadTexto.isEmpty(vo.get("fecha_inicial_venc").toString()))
				ps.setDate(7, Date.valueOf(vo.get("fecha_inicial_venc")+""));
			else
				ps.setObject(7,null);
			if(!UtilidadTexto.isEmpty(vo.get("fecha_final_venc").toString()))
				ps.setDate(8, Date.valueOf(vo.get("fecha_final_venc")+""));
			else
				ps.setObject(8,null);
			ps.setString(9, vo.get("usuario_modifica")+"");
			ps.setDate(10, Date.valueOf(vo.get("fecha_modifica")+""));
			ps.setString(11, vo.get("hora_modifica")+"");
			
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
			
			/**
			 * UPDATE paquetes_convenio SET 
			 * usuario_modifica=?, 
			 * convenio=?, 
			 * contrato=?, 
			 * paquete=?, 
			 * via_ingreso=?, 
			 * tipo_paciente=?, 
			 * fecha_inicial_venc=?, 
			 * fecha_final_venc=?, 
			 * fecha_modifica=?, 
			 * hora_modifica=?, 
			 * institucion=?  
			 * WHERE codigo=?
			 */
			
			ps.setString(1, vo.get("usuario_modifica")+"");
			if(UtilidadTexto.isEmpty(vo.get("convenio")+""))
				ps.setObject(2, null);
			else
				ps.setInt(2, Utilidades.convertirAEntero(vo.get("convenio")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("contrato")+""))
				ps.setObject(3, null);
			else
				ps.setInt(3, Utilidades.convertirAEntero(vo.get("contrato")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("paquete")+""))
				ps.setObject(4, null);
			else
				ps.setString(4, vo.get("paquete")+"");
			
			if(Utilidades.convertirAEntero(vo.get("via_ingreso")+"")==ConstantesBD.codigoNuncaValido)
				ps.setObject(5, null);
			else
				ps.setInt(5, Utilidades.convertirAEntero(vo.get("via_ingreso")+""));
			
			if(vo.get("tipo_paciente").toString().equals(""))
				ps.setNull(6, Types.VARCHAR);
			else
				ps.setString(6, vo.get("tipo_paciente")+"");
			
			if(!UtilidadTexto.isEmpty(vo.get("fecha_inicial_venc").toString()))
				ps.setDate(7, Date.valueOf(vo.get("fecha_inicial_venc")+""));
			else
				ps.setObject(7,null);
			
			if(!UtilidadTexto.isEmpty(vo.get("fecha_final_venc").toString()))
				ps.setDate(8, Date.valueOf(vo.get("fecha_final_venc")+""));
			else
				ps.setObject(8,null);
			
			ps.setDate(9, Date.valueOf(vo.get("fecha_modifica")+""));
			ps.setString(10, vo.get("hora_modifica")+"");
			ps.setInt(11, Utilidades.convertirAEntero(vo.get("institucion")+""));
			ps.setDouble(12, Utilidades.convertirADouble(vo.get("codigo")+""));
			
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
	 * @param institucion
	 * @param codigoPaquete
	 * @return
	 */
	
	public static boolean eliminarRegistro(Connection con, String codigo)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaEliminacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigo));
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
	public static HashMap consultarPaquetesConvenio(Connection con, HashMap vo) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		String cadena= cadenaConsultaStr+" where pc.institucion=? ";
		
		try
		{
			if(vo.containsKey("codigoConvenio"))
			{
				cadena+=" and convenio='"+vo.get("codigoConvenio")+"' ";
			}
			if(vo.containsKey("codigoContrato"))
			{
				cadena+=" and contrato='"+vo.get("codigoContrato")+"' ";
			}
			cadena+=" ORDER BY codigo ";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(vo.get("institucion")+""));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}
}
