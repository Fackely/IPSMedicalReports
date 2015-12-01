package com.princetonsa.dao.sqlbase.manejoPaciente;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseViasIngresoDao;

/**
 * 
 * @author Andr&eacute;s Mauricio Guerrero Toro
 * 
 */
public class SqlBaseViasIngresoDao {
	
	/**
	 * objeto para manejar los logs de esta clase
	 */
	
	public static Logger logger=Logger.getLogger(SqlBaseViasIngresoDao.class);
	
	/**
	 * cadena para la insercion
	 */
	
	private static final String cadenaInsertarStr="INSERT INTO vias_ingreso (codigo, nombre, identificador, responsable_paciente, verificacion_derechos, convenio, recibo_automatico, usuario_modifica, fecha_modifica, hora_modifica, corte_facturacion,validar_cierre_nota_enfer,validar_epicrisis_finali) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	/**
	 * Cadena para la consulta
	 */
	private static final String cadenaConsultaStr= "SELECT " +
															"codigo as codigo, nombre as nombre," +
															"identificador as identificador, responsable_paciente as paciente," + 
															"verificacion_derechos as verificacion, " +
															"recibo_automatico as recibo, "+
															"coalesce(convenio||'','') as convenio, corte_facturacion as corte_facturacion, "+
															"'"+ConstantesBD.acronimoSi+"' as tiporegistro," +
															"validar_cierre_nota_enfer AS validarcierrenotaenfer," +
															"validar_epicrisis_finali AS validarepicrisisfinali  " +
															"FROM vias_ingreso ";
	
	/**
	 * cadena para la modificacion
	 */
	
	private static final String cadenaModificacionStr="UPDATE vias_ingreso SET nombre=?, identificador=?, responsable_paciente=?, verificacion_derechos=?, recibo_automatico=?, convenio=?, usuario_modifica=?, fecha_modifica=?, hora_modifica=?, corte_facturacion=?, validar_cierre_nota_enfer = ?, validar_epicrisis_finali = ?  WHERE codigo=?";
	
	/**
	 *Cadena para la eliminacion 
	 */
	
	private static final String cadenaEliminacionStr="DELETE FROM vias_ingreso WHERE codigo=?";
	
	/**
	 * cadena para insertar garantia paciente
	 * */
	private static final String insertarGarantiaPaciente="INSERT INTO garantia_paciente (codigo_via_ingreso, acronimo_tipo_paciente, garantia, codigo_interfaz, bloquea_ing_deu_x_saldo_mora, bloquea_ing_pac_x_saldo_mora, tam_impresion) VALUES (?, ?, ?, ?, ? ,?,?)";
	
	/**
	 * Cadena para la consulta de Garantia Pacientne
	 * */
	private static final String consultarGarantiaPaciente = "SELECT " +
																"codigo_via_ingreso as codigo_via_ingreso, " +
																"acronimo_tipo_paciente as acronimo_tipo_paciente, " +
																"garantia as garantia, " +
																"'"+ConstantesBD.acronimoSi+"' as tiporegistro, " +
																"codigo_interfaz AS codinterfaz, " +
																"bloquea_ing_deu_x_saldo_mora as bloqueaingresodeudor, " +
																"bloquea_ing_pac_x_saldo_mora as bloqueaingresopaciente, tam_impresion as tamfactura " +
															"FROM " +
																"garantia_paciente ";
	
	/**
	 * Cadena para la consulta de Garantia Pacientne
	 * */
	private static final String consultarNoTipoPacViaIngStr="SELECT count(via_ingreso) AS No_Registros FROM tip_pac_via_ingreso ";
	
	/**
	 * Cadena para modificar garantia pacientes
	 **/
	private static final String modificarGarantiaPaciente="UPDATE garantia_paciente SET bloquea_ing_deu_x_saldo_mora=?, bloquea_ing_pac_x_saldo_mora=?, garantia=?, codigo_interfaz=?, tam_impresion=? WHERE codigo_via_ingreso=? and acronimo_tipo_paciente=?";
	
	/**
	 * Insertar
	 * */
	
	public static boolean insertar(Connection con, HashMap vo)
	{
	  
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO vias_ingreso (
			 * codigo, 
			 * nombre, 
			 * identificador, 
			 * responsable_paciente, 
			 * verificacion_derechos, 
			 * convenio, 
			 * recibo_automatico, 
			 * usuario_modifica, 
			 * fecha_modifica, 
			 * hora_modifica, 
			 * corte_facturacion,
			 * validar_cierre_nota_enfer,
			 * validar_epicrisis_finali) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
			 */
			
			ps.setInt(1, Utilidades.convertirAEntero(vo.get("codigo")+""));
			ps.setString(2, vo.get("nombre")+"");
			if(UtilidadTexto.isEmpty(vo.get("identificador")+""))
				ps.setNull(3, Types.VARCHAR);
			else
				ps.setString(3, vo.get("identificador")+"");
			ps.setString(4, vo.get("responsable_paciente")+"");
			ps.setString(5, vo.get("verificacion_derechos")+"");
			if(UtilidadTexto.isEmpty(vo.get("convenio")+""))
				ps.setNull(6, Types.INTEGER);
			else
				ps.setInt(6, Utilidades.convertirAEntero(vo.get("convenio")+""));
			ps.setString(7, vo.get("recibo_automatico")+"");
			ps.setString(8, vo.get("usuario_modifica")+"");
			ps.setDate(9, Date.valueOf(vo.get("fecha_modifica")+""));
			ps.setString(10, vo.get("hora_modifica")+"");
			if(UtilidadTexto.isEmpty(vo.get("corte_facturacion")+""))
				ps.setNull(11, Types.CHAR);
			else
				ps.setString(11, vo.get("corte_facturacion")+"");
			
			ps.setString(12, vo.get("validarcierrenotaenfer")+"");
			ps.setString(13, vo.get("validarepicrisisfinali")+"");
			
			return ps.executeUpdate()>0;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false; 
	}
	
	/**
	 * consulta vias de ingreso existentes
	 */
	public static HashMap consultarViasIngresoExistentes(Connection con, HashMap vo) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		String cadena= cadenaConsultaStr;
		
		try
		{
			if(vo.containsKey("codigo")&&!UtilidadTexto.isEmpty(vo.get("codigo")+""))
			{
				cadena+=" where codigo='"+vo.get("codigo")+"'";
			}
			cadena+=" ORDER BY codigo ";
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
	 * Modificar
	 * */
	public static boolean modificar(Connection con, HashMap vo)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaModificacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE vias_ingreso SET 
			 * nombre=?, 
			 * identificador=?, 
			 * responsable_paciente=?, 
			 * verificacion_derechos=?, 
			 * recibo_automatico=?, 
			 * convenio=?, 
			 * usuario_modifica=?, 
			 * fecha_modifica=?, 
			 * hora_modifica=?, 
			 * corte_facturacion=?, 
			 * validar_cierre_nota_enfer =?, 
			 * validar_epicrisis_finali =?  
			 * WHERE codigo=?
			 */
			
			ps.setString(1, vo.get("nombre")+"");
			if(UtilidadTexto.isEmpty(vo.get("identificador")+""))
				ps.setNull(2, Types.CHAR);
			else
				ps.setString(2, vo.get("identificador")+"");
			ps.setString(3, vo.get("responsable_paciente")+"");
			ps.setString(4, vo.get("verificacion_derechos")+"");
			ps.setString(5, vo.get("recibo_automatico")+"");
			if(UtilidadTexto.isEmpty(vo.get("convenio")+""))
				ps.setNull(6, Types.INTEGER);
			else
				ps.setInt(6, Utilidades.convertirAEntero(vo.get("convenio")+""));
			ps.setString(7, vo.get("usuario_modifica")+"");
			ps.setDate(8, Date.valueOf(vo.get("fecha_modifica")+""));
			ps.setString(9, vo.get("hora_modifica")+"");
			/*if(UtilidadTexto.isEmpty(vo.get("corte_facturacion")+""))
				ps.setObject(10, null);
			else*/
				ps.setString(10, vo.get("corte_facturacion")+"");
			
			ps.setString(11, vo.get("validarcierrenotaenfer")+"");
			ps.setString(12, vo.get("validarepicrisisfinali")+"");
				
			ps.setInt(13, Utilidades.convertirAEntero(vo.get("codigo")+""));
			
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Eliminar
	 * */
	public static boolean eliminarRegistro(Connection con, int codigo)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaEliminacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigo);
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * insertar garantia paciente
	 * */
	public static boolean insertarGarantiaPaciente(Connection con, HashMap vo)
	{
	  
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(insertarGarantiaPaciente,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO garantia_paciente (codigo_via_ingreso, acronimo_tipo_paciente, garantia) VALUES (?, ?, ?)
			 */
			
			ps.setInt(1, Utilidades.convertirAEntero(vo.get("codigo_via_ingreso")+""));
			ps.setString(2, vo.get("acronimo_tipo_paciente")+"");
			ps.setString(3, vo.get("garantia")+"");
			if(vo.get("codinterfaz").toString().equals(""))
				ps.setNull(4, Types.VARCHAR);
			else
				ps.setString(4, vo.get("codinterfaz")+"");
			
			if(vo.get("bloqueaingresodeudor").toString().equals(""))
				ps.setNull(5, Types.VARCHAR);
			else
				ps.setString(5, vo.get("bloqueaingresodeudor")+"");
			
			if(vo.get("bloqueaingresopaciente").toString().equals(""))
				ps.setNull(6, Types.VARCHAR);
			else
				ps.setString(6, vo.get("bloqueaingresopaciente")+"");
			ps.setString(7, vo.get("tamfactura")+"");
			return ps.executeUpdate()>0;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Consultar garantia paciente 
	 * */
	public static HashMap consultarGarantiaPaciente(Connection con, int codigo)
	{
		HashMap mapa=new HashMap();
		String cadena=consultarGarantiaPaciente;
		try
		{
			
			cadena+=" WHERE codigo_via_ingreso= "+codigo;
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));

		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
	/**
	 * Consultar N�mero de tipo paciente por v�a de ingreso
	 * */
	public static int consultarNoTipoPacViaIng(Connection con, int codigo)
	{
		String cadena=consultarNoTipoPacViaIngStr;
		try
		{
			
			cadena+=" WHERE via_ingreso= "+codigo;
			logger.info("CADENA::::::::............::::::::"+cadena);
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				logger.info("rs.getInt(No_Registros)::::::))))))))))"+rs.getInt("No_Registros"));
				return rs.getInt("No_Registros");
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return -1;
	}
	
	/**
	 * Modificar garantia paciente
	 * */
	public static boolean modificarGarantiaPaciente(Connection con, HashMap vo)
	{
	  		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarGarantiaPaciente,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE garantia_paciente SET garantia=? WHERE codigo_via_ingreso=? and acronimo_tipo_paciente=?
			 */
			
			if(vo.get("bloqueaingresodeudor").toString().equals(""))
				ps.setNull(1, Types.VARCHAR);
			else
				ps.setString(1, vo.get("bloqueaingresodeudor")+"");
			
			if(vo.get("bloqueaingresopaciente").toString().equals(""))
				ps.setNull(2, Types.VARCHAR);
			else
				ps.setString(2, vo.get("bloqueaingresopaciente")+"");
			
			ps.setString(3, vo.get("garantia")+"");
			if(vo.get("codinterfaz").toString().equals(""))
				ps.setNull(4, Types.VARCHAR);
			else
				ps.setString(4, vo.get("codinterfaz")+"");
			ps.setString(5, vo.get("tamfactura")+"");
			ps.setInt(6, Utilidades.convertirAEntero(vo.get("codigo_via_ingreso")+""));
			ps.setString(7, vo.get("acronimo_tipo_paciente")+"");
			
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
	 * @param idIngreso
	 * @param codigoConvenio
	 * @return
	 */
	public static boolean existeVerificacionDerechosViaIngreso( Connection con, int codigoViaIngreso)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		try 
	    {
			String consulta="SELECT coalesce(verificacion_derechos, '') as verificacion FROM vias_ingreso where codigo=? ";
			pst=  con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, codigoViaIngreso);
		    rs=pst.executeQuery();
		    if(rs.next())
		    {	
		        if(rs.getString("verificacion") != null && rs.getString("verificacion").equals(ConstantesBD.acronimoSi)){
		        	return true;
		        }
		    }    	
	    }
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR obtenerTarifaBaseServicio",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerTarifaBaseServicio", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
	    return false;
	}

	/**
	 * 
	 * @param con
	 * @param viaIngreso
	 * @return
	 */
	public static int obtenerConvenioDefecto(Connection con, int viaIngreso) 
	{
		String consulta="SELECT coalesce(convenio,"+ConstantesBD.codigoNuncaValido+") FROM vias_ingreso where codigo=? ";
		try 
	    {
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, viaIngreso);
		    ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
		    if(rs.next())
		    {	
		        return rs.getInt(1);
		    }    	
		} 
	    catch (SQLException e) 
	    {
			logger.error("error existeVerificacionDerechosViaIngreso");
			e.printStackTrace();
		}
	    return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @param codigoConvenio
	 * @return
	 */
	public static boolean existeVerificacionCierreNotasEnferViaIngreso( Connection con, int codigoViaIngreso)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		try 
	    {
			String consulta="SELECT coalesce(validar_cierre_nota_enfer, '') as verificacion FROM vias_ingreso where codigo=? ";
			pst=  con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, codigoViaIngreso);
		    rs=pst.executeQuery();
		    if(rs.next()){	
		        if(rs.getString("verificacion").equals(ConstantesBD.acronimoSi)){
		        	return true;
		        }
		    }    	
	    }
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR existeVerificacionCierreNotasEnferViaIngreso",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR existeVerificacionCierreNotasEnferViaIngreso", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
	    return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @param codigoConvenio
	 * @return
	 */
	public static boolean existeVerificacionEpicrisisFinalizadaViaIngreso( Connection con, int codigoViaIngreso)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		try 
	    {
			String consulta="SELECT coalesce(validar_epicrisis_finali, '') as verificacion FROM vias_ingreso where codigo=? ";
			pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, codigoViaIngreso);
		    rs=pst.executeQuery();
		    if(rs.next()){	
		        if(rs.getString("verificacion") != null && rs.getString("verificacion").equals(ConstantesBD.acronimoSi)){
		        	return true;
		        }
		    }    	
	    }
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR existeVerificacionEpicrisisFinalizadaViaIngreso",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR existeVerificacionEpicrisisFinalizadaViaIngreso", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
	    return false;
	}
	

}
