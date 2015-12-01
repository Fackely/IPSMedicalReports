package com.princetonsa.dao.sqlbase.tesoreria;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.tesoreria.MotivosDevolucionRecibosCaja;

import util.ConstantesBD;
import util.UtilidadBD;
import util.Utilidades;
import util.ValoresPorDefecto;

public class SqlBaseMotivosDevolucionRecibosCajaDao
{
	private static Logger logger = Logger.getLogger(SqlBaseMotivosDevolucionRecibosCajaDao.class);
	
	private static String[] indicesMapM={"codigo_","descripcion_","activo_","puedoEliminar_"};
	
	private static String consultaStrM="SELECT codigo, descripcion, activo " +
										"FROM motivos_devolucion_rc " +
										"WHERE institucion = ? ORDER BY descripcion ";
	
	private static String eliminarStr="DELETE FROM motivos_devolucion_rc WHERE codigo = ?";
	
	private static String insertarStr="INSERT INTO motivos_devolucion_rc VALUES (?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+")";
	
	private static String modificarStr="UPDATE motivos_devolucion_rc " +
										"SET codigo=?, " +
											"descripcion=?, " +
											"activo=?, " +
											"institucion=?, " +
											"usuario_modifica=?, " +
											"fecha_modifica=CURRENT_DATE, " +
											"hora_modifica="+ValoresPorDefecto.getSentenciaHoraActualBD()+" " +
										"WHERE " +
											"codigo=? ";
	
	/**
	 * Metodo de consulta de los Motivos devolucion recibos de caja
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public static HashMap<String, Object> consultaMotivosD (Connection con, int codigoInstitucion)
	{
		HashMap<String, Object> resultadosMDRC = new HashMap<String, Object>();
		
		PreparedStatementDecorator pst;
		
		try{
			pst =  new PreparedStatementDecorator(con.prepareStatement(consultaStrM, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));			
			pst.setInt(1, codigoInstitucion);
			
			resultadosMDRC = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true, true);
			
			for(int w=0; w<Utilidades.convertirAEntero( resultadosMDRC.get("numRegistros").toString()); w++)
			{
				if(puedoEliminar(resultadosMDRC.get("codigo_"+w)+""))
				{
					resultadosMDRC.put("puedoEliminar_"+w, ConstantesBD.acronimoSi);
				}
				else
				{
					resultadosMDRC.put("puedoEliminar_"+w, ConstantesBD.acronimoNo);
				}
			}
			
			resultadosMDRC.put("INDICES",indicesMapM);
			
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en consulta de Motivos Devolucion Recibos Caja");
		}
		return resultadosMDRC;
	}
	
	public static boolean eliminar(Connection con, String motivoD)
	{

		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(eliminarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, motivoD);
		
			if(ps.executeUpdate()>0)
				return true;
		}
		catch (SQLException e)
		{
			logger.info("OCURRIO UNA EXCEPCION AL ELIMINAR. POSIBLEMENTE NO SE DAÑE EL FLUJO!"+e);
		}
		return false;
	}
	
	private static boolean puedoEliminar(String tipoT)
	{
		boolean retorna=false;
		Connection con=UtilidadBD.abrirConexion();
		UtilidadBD.iniciarTransaccion(con);
		
		boolean puedoEliminar=eliminar(con, tipoT);
		if(puedoEliminar)
			retorna= true;
		UtilidadBD.abortarTransaccionSinMensaje(con);
		UtilidadBD.closeConnection(con);
		
		return retorna;
	}
	
	public static boolean insertar(Connection con, MotivosDevolucionRecibosCaja motivosDevolucionRecibosCaja){
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(insertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			ps.setString(1, motivosDevolucionRecibosCaja.getCodigo());
			ps.setString(2, motivosDevolucionRecibosCaja.getDescripcion());
			if(motivosDevolucionRecibosCaja.getActivo().equals("1")){
				ps.setString(3, "S");
			}
			else{
				ps.setString(3, "N");				
			}
			ps.setString(4, motivosDevolucionRecibosCaja.getInstitucion());
			ps.setString(5, motivosDevolucionRecibosCaja.getUsuarioModifica());
			
			if(ps.executeUpdate()>0)
				return true;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean modificar(Connection con, MotivosDevolucionRecibosCaja motivosDevolucionRecibosCaja){
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			ps.setString(1, motivosDevolucionRecibosCaja.getCodigo());
			ps.setString(2, motivosDevolucionRecibosCaja.getDescripcion());
			if(motivosDevolucionRecibosCaja.getActivo().equals("1")){
				ps.setString(3, "S");
			}
			else{
				ps.setString(3, "N");				
			}
			ps.setString(4, motivosDevolucionRecibosCaja.getInstitucion());
			ps.setString(5, motivosDevolucionRecibosCaja.getUsuarioModifica());
			ps.setString(6, motivosDevolucionRecibosCaja.getCodigoAux());
			
			if(ps.executeUpdate()>0)
				return true;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
}