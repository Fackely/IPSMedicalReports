package com.princetonsa.dao.sqlbase.inventarios;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

public class SqlBaseTiposInventarioDao {
///
	private static Logger logger=Logger.getLogger(SqlBaseTiposInventarioDao.class);
	private static String cadenaConsultarTiposInventario="SELECT i.codigo as  codigo , i.nombre as nombre, i.institucion as institucion, i.cuenta_inventario as cuenta_inventario, i.cuenta_costo as cuenta_costo,i.rubro_presupuestal as rubro,coalesce(i.codigo_interfaz,'') as codigo_interfaz, 'BD' as tiporegistro FROM inventarios.clase_inventario i WHERE  1=1 ";
	private static String cadenaModificarTiposInventario="UPDATE inventarios.clase_inventario SET nombre=?,cuenta_inventario=?,cuenta_costo=?,rubro_presupuestal=?, codigo_interfaz = ? WHERE codigo=? AND institucion= ?";
	private static String cadenaEliminarTiposInventario="DELETE FROM inventarios.clase_inventario where codigo=? AND institucion=?";

	private static String cadenaConsultarGruposInventario="SELECT i.codigo as  codigo , i.clase as clase, i.nombre as nombre, i.institucion as institucion, i.cuenta_inventario as cuenta_inventario, i.cuenta_costo as cuenta_costo,rubro_presupuestal as rubro, 'BD' as tiporegistro, aplica_cargos_directos as aplica_cd  FROM inventarios.grupo_inventario i WHERE  1=1 ";
	private static String cadenaModificarGruposInventario="UPDATE inventarios.grupo_inventario SET nombre=?,clase=?, cuenta_inventario=?,cuenta_costo=?,rubro_presupuestal=?, aplica_cargos_directos=? WHERE codigo=? AND clase=? AND institucion= ?";
	private static String cadenaEliminarGruposInventario="DELETE FROM inventarios.grupo_inventario where codigo=? AND clase=? AND institucion=?";
	
	private static String cadenaConsultarSubGruposInventario="SELECT i.codigo as codigo , i.subgrupo as subgrupo, i.grupo as grupo, i.clase as clase, i.nombre as nombre, i.institucion as institucion, i.cuenta_inventario as cuenta_inventario, i.cuenta_costo as cuenta_costo,i.rubro_presupuestal as rubro, 'BD' as tiporegistro FROM inventarios.subgrupo_inventario i WHERE  1=1 ";
	private static String cadenaModificarSubGruposInventario="UPDATE inventarios.subgrupo_inventario SET nombre=?,subgrupo=?,grupo=?, clase=?,cuenta_inventario=?,cuenta_costo=?,rubro_presupuestal=? WHERE codigo=? and institucion= ?";
	private static String cadenaEliminarSubGruposInventario="DELETE FROM inventarios.subgrupo_inventario where codigo=? AND subgrupo=?  AND grupo=? AND clase=? AND institucion=?";



	public static HashMap consultarClaseInventario(Connection con, HashMap vo)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		String cadena=cadenaConsultarTiposInventario;
		if(vo.containsKey("institucion"))
		{
			cadena+=" AND institucion="+vo.get("institucion");
		}

		if(vo.containsKey("codigo"))
		{
			cadena+=" AND codigo="+vo.get("codigo");
		}
		
		try
		{
			cadena+=" ORDER BY nombre ";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("cadena-->"+cadena);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}

	public static boolean modificarClaseInventario(Connection con, HashMap vo)
	{
		logger.info("CADENA --->"+cadenaModificarTiposInventario);
		try
		{
			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaModificarTiposInventario,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE inventarios.clase_inventario SET nombre=?,cuenta_inventario=?,cuenta_costo=?, rubro_presupuestal=? WHERE codigo=? AND institucion= ?
			 */
			
			ps.setString(1, vo.get("nombre")+"");
			
			//ps.setString(2, vo.get("cuenta_inventario")+"");
			if(vo.get("cuenta_inventario").equals(ConstantesBD.codigoNuncaValido+"")||vo.get("cuenta_inventario").toString().trim().equals(""))
			{
				ps.setNull(2, Types.NUMERIC);
			}
			else
			{
				ps.setDouble(2, Utilidades.convertirADouble(vo.get("cuenta_inventario")+"" ));
			}
			
			//ps.setString(3, vo.get("cuenta_costo")+"");
			if(vo.get("cuenta_costo").equals(ConstantesBD.codigoNuncaValido+"")||vo.get("cuenta_costo").toString().trim().equals(""))
			{
				ps.setNull(3, Types.NUMERIC);
			}
			else
			{
				ps.setDouble(3, Utilidades.convertirADouble(vo.get("cuenta_costo")+"" ));
			}
			logger.info("ppppppppppppppppppppppp    RUBRO --->"+vo.get("rubro")+"<---");
			if(vo.get("rubro").equals(ConstantesBD.codigoNuncaValido+"")||vo.get("rubro").toString().trim().equals(""))
			{
				ps.setNull(4, Types.VARCHAR);
			}
			else
			{
				ps.setString(4, vo.get("rubro")+"".trim());
			}
			
			if(!UtilidadTexto.isEmpty(vo.get("codigo_interfaz")+""))
			{
				ps.setString(5,vo.get("codigo_interfaz").toString());
			}
			else
			{
				ps.setNull(5,Types.VARCHAR);
			}
			
			ps.setInt(6, Utilidades.convertirAEntero(vo.get("codigo")+""));
			ps.setInt(7, Utilidades.convertirAEntero(vo.get("institucion")+""));
			ps.executeUpdate();
			return true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public static boolean insertarClaseInventario(Connection con, HashMap vo,String cadena)
	{
		try
		{
			Utilidades.imprimirMapa(vo);
			logger.info("insertarClaseInventario->"+cadena);
			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO inventarios.clase_inventario (codigo,nombre,institucion,cuenta_inventario,cuenta_costo) values (?,?,?,?,?)
			 */
			logger.info("->"+Utilidades.convertirAEntero(vo.get("codigo")+""));
			ps.setInt(1, Utilidades.convertirAEntero(vo.get("codigo")+""));
			ps.setString(2, vo.get("nombre")+"");
			ps.setInt(3, Utilidades.convertirAEntero(vo.get("institucion")+""));

			//ps.setString(4, vo.get("cuenta_inventario")+"");
			if(vo.get("cuenta_inventario").equals(ConstantesBD.codigoNuncaValido+"")||vo.get("cuenta_inventario").toString().trim().equals(""))
			{
				ps.setNull(4, Types.NUMERIC);
			}
			else
			{
				ps.setDouble(4, Utilidades.convertirADouble(vo.get("cuenta_inventario")+"" ));
			}
			
			//ps.setString(5, vo.get("cuenta_costo")+"");
			if(vo.get("cuenta_costo").equals(ConstantesBD.codigoNuncaValido+"")||vo.get("cuenta_costo").toString().trim().equals(""))
			{
				ps.setNull(5, Types.NUMERIC);
			}
			else
			{
				ps.setDouble(5, Utilidades.convertirADouble(vo.get("cuenta_costo")+"" ));
			}
			
			if(vo.get("rubro").equals(ConstantesBD.codigoNuncaValido+"")||vo.get("rubro").toString().trim().equals(""))
			{
				ps.setNull(6, Types.VARCHAR);
			}
			else
			{
				ps.setString(6, vo.get("rubro")+"".trim());
			}
			
			if(!UtilidadTexto.isEmpty(vo.get("codigo_interfaz")+""))
			{
				ps.setString(7,vo.get("codigo_interfaz").toString());
			}
			else
			{
				ps.setNull(7,Types.VARCHAR);
			}
			
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public static boolean eliminarClaseInventario(Connection con, String codigo, String institucion)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaEliminarTiposInventario,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * DELETE FROM inventarios.clase_inventario where codigo=? AND institucion=?
			 */
			
			ps.setInt(1, Utilidades.convertirAEntero(codigo));
			ps.setInt(2, Utilidades.convertirAEntero(institucion));
			
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static HashMap consultarGruposInventario(Connection con, HashMap vo)
	{
///pilas toca verificar que le llega aqui		

		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		String cadena=cadenaConsultarGruposInventario;
		
		if(vo.containsKey("institucion"))
		{
			cadena+=" AND institucion="+vo.get("institucion");
		}

		if(vo.containsKey("codigo"))
		{
			cadena+=" AND codigo="+vo.get("codigo");
		}

		if(vo.containsKey("codigoClase"))
		{
			cadena+=" AND clase="+vo.get("codigoClase");
		}
		
		
		///pilas 
		//cadena+=" AND clase="+ clase;
		cadena+=" ORDER BY nombre ";
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

	public static boolean modificarGruposInventario(Connection con, HashMap vo)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaModificarGruposInventario,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE inventarios.grupo_inventario SET 
			 * nombre=?,
			 * clase=?, 
			 * cuenta_inventario=?,
			 * cuenta_costo=?,
			 * rubro_presupuestal=? 
			 * WHERE codigo=? AND clase=? AND institucion= ?
			 */
			
			ps.setString(1, vo.get("nombre")+"");
			ps.setInt(2, Utilidades.convertirAEntero(vo.get("clase")+""));
			//ps.setString(3, vo.get("cuenta_inventario")+"");
			if(vo.get("cuenta_inventario").equals(ConstantesBD.codigoNuncaValido+"")||vo.get("cuenta_inventario").toString().trim().equals(""))
				ps.setNull(3, Types.NUMERIC);
			else
				ps.setDouble(3, Utilidades.convertirADouble(vo.get("cuenta_inventario")+"" ));
			
			//ps.setString(4, vo.get("cuenta_costo")+"");
			if(vo.get("cuenta_costo").equals(ConstantesBD.codigoNuncaValido+"")||vo.get("cuenta_costo").toString().trim().equals(""))
				ps.setNull(4, Types.NUMERIC);
			else
				ps.setDouble(4, Utilidades.convertirADouble(vo.get("cuenta_costo")+"" ));		
			
			if(vo.get("rubro").equals(ConstantesBD.codigoNuncaValido+"")||vo.get("rubro").toString().trim().equals(""))
				ps.setNull(5, Types.VARCHAR);
			else
				ps.setString(5, vo.get("rubro")+"".trim());
			
			if(UtilidadTexto.isEmpty(vo.get("aplica_cd")+""))
				ps.setString(6, ConstantesBD.acronimoNo);
			else
				ps.setString(6, vo.get("aplica_cd")+"");
			
			ps.setInt(7, Utilidades.convertirAEntero(vo.get("codigo")+""));
			ps.setInt(8, Utilidades.convertirAEntero(vo.get("clase")+""));
			ps.setInt(9, Utilidades.convertirAEntero(vo.get("institucion")+""));
			ps.executeUpdate();
			return true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public static boolean insertarGruposInventario(Connection con, HashMap vo,String cadena)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO inventarios.grupo_inventario (codigo,clase,nombre,institucion,cuenta_inventario,cuenta_costo,rubro_presupuestal) values (?,?,?,?,?,?,?)
			 */
			
			logger.info("Param 1 "+vo.get("codigo"));
			logger.info("Param 2 "+vo.get("clase"));
			logger.info("Param 3 "+vo.get("nombre"));
			logger.info("Param 4 "+vo.get("institucion"));
			
			ps.setInt(1, Utilidades.convertirAEntero(vo.get("codigo")+""));
			ps.setInt(2, Utilidades.convertirAEntero(vo.get("clase")+""));
			ps.setString(3, vo.get("nombre")+"");
			ps.setInt(4, Utilidades.convertirAEntero(vo.get("institucion")+""));

			//ps.setString(5, vo.get("cuenta_inventario")+"");
			if(vo.get("cuenta_inventario").equals(ConstantesBD.codigoNuncaValido+"")||vo.get("cuenta_inventario").toString().trim().equals("")){
				ps.setNull(5, Types.NUMERIC);
			}	
			else {
				logger.info("Param 5 "+vo.get("cuenta_inventario"));
				ps.setDouble(5, Utilidades.convertirADouble(vo.get("cuenta_inventario")+"" ));
			}	
			
//			ps.setString(6, vo.get("cuenta_costo")+"");
			if(vo.get("cuenta_costo").equals(ConstantesBD.codigoNuncaValido+"")||vo.get("cuenta_costo").toString().trim().equals(""))
				ps.setNull(6, Types.NUMERIC);
			else{
				logger.info("Param 6 "+vo.get("cuenta_costo"));
				ps.setDouble(6, Utilidades.convertirADouble(vo.get("cuenta_costo")+"" ));
			}	
			
			if(vo.get("rubro").equals(ConstantesBD.codigoNuncaValido+"")||vo.get("rubro").toString().trim().equals(""))
				ps.setNull(7, Types.VARCHAR);
			else{
				logger.info("Param 7 "+vo.get("rubro"));
				ps.setString(7, vo.get("rubro")+"".trim());
			}
			
			if(UtilidadTexto.isEmpty(vo.get("aplica_cd")+""))
				ps.setString(8, ConstantesBD.acronimoNo);
			else{
				ps.setString(8, vo.get("aplica_cd")+"");
			}
			
			logger.info("insertarGruposInventario / "+cadena);
			
			
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public static boolean eliminarGruposInventario(Connection con, String codigo, String institucion, String clase)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaEliminarGruposInventario,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(codigo));
			ps.setInt(2, Utilidades.convertirAEntero(clase));
			ps.setInt(3, Utilidades.convertirAEntero(institucion));
			
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static HashMap consultarSubGruposInventario(Connection con, HashMap vo)
	{  //codigo, subgrupo, grupo, clase
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		String cadena=cadenaConsultarSubGruposInventario;
		
		
		if(vo.containsKey("institucion"))
		{
			cadena+=" AND institucion="+vo.get("institucion");
		}
		
		if(vo.containsKey("codigo"))
		{
			cadena+=" AND codigo="+vo.get("codigo");
		}
		
		if(vo.containsKey("codigosubgrupo"))
		{
			cadena+=" AND subgrupo="+vo.get("codigosubgrupo");
		}
		
		if(vo.containsKey("codigoGrupo"))
		{
			cadena+=" AND grupo="+vo.get("codigoGrupo");
		}
		
		if(vo.containsKey("codigoClase"))
		{
			cadena+=" AND clase="+vo.get("codigoClase");
		}		
		try
		{
			cadena+=" ORDER BY nombre ";

			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}

	public static boolean modificarSubGruposInventario(Connection con, HashMap vo)
	{
		try
		{
						
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaModificarSubGruposInventario,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE inventarios.subgrupo_inventario SET 
			 * nombre=?,
			 * subgrupo=?,
			 * grupo=?, 
			 * clase=?,
			 * cuenta_inventario=?,
			 * cuenta_costo=?, 
			 * rubro_presupuestal=? 
			 * WHERE codigo=? 
			 * and institucion= ?
			 */
			
			ps.setString(1, vo.get("nombre")+"");
			ps.setInt(2, Utilidades.convertirAEntero(vo.get("subgrupo")+""));
			ps.setInt(3, Utilidades.convertirAEntero(vo.get("grupo")+""));
			ps.setInt(4, Utilidades.convertirAEntero(vo.get("clase")+""));
			
			//ps.setString(5, vo.get("cuenta_inventario")+"");
			if(vo.get("cuenta_inventario").equals(ConstantesBD.codigoNuncaValido+"")||vo.get("cuenta_inventario").toString().trim().equals(""))
				ps.setNull(5, Types.NUMERIC);
			else
				ps.setDouble(5, Utilidades.convertirADouble(vo.get("cuenta_inventario")+"" ));
			
//			ps.setString(6, vo.get("cuenta_costo")+"");
			if(vo.get("cuenta_costo").equals(ConstantesBD.codigoNuncaValido+"")||vo.get("cuenta_costo").toString().trim().equals(""))
				ps.setNull(6, Types.NUMERIC);
			else
				ps.setDouble(6, Utilidades.convertirADouble(vo.get("cuenta_costo")+"" ));		
			
			if(vo.get("rubro").equals(ConstantesBD.codigoNuncaValido+"")||vo.get("rubro").toString().trim().equals(""))
				ps.setNull(7, Types.VARCHAR);
			else
				ps.setString(7, vo.get("rubro")+"".trim());
			
			ps.setInt(8, Utilidades.convertirAEntero(vo.get("codigo")+""));
			ps.setInt(9, Utilidades.convertirAEntero(vo.get("institucion")+""));
			ps.executeUpdate();
			return true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public static boolean insertarSubGruposInventario(Connection con, HashMap vo,String cadena)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

			/**
			 * INSERT INTO inventarios.subgrupo_inventario (
			 * codigo,
			 * subgrupo,
			 * grupo,
			 * clase,
			 * nombre,
			 * institucion,
			 * cuenta_inventario,
			 * cuenta_costo, rubro_presupuestal) values ('SEQ_SUBGRUPO_INVENTARIO'),?,?,?,?,?,?,?,?)
			 */
			
			//ps.setString(1, vo.get("codigo")+""); // tiene una sequencia como val x def.
			ps.setInt(1, Utilidades.convertirAEntero(vo.get("subgrupo")+""));
			ps.setInt(2, Utilidades.convertirAEntero(vo.get("grupo")+""));
			ps.setInt(3, Utilidades.convertirAEntero(vo.get("clase")+""));
			ps.setString(4, vo.get("nombre")+"");
			ps.setInt(5, Utilidades.convertirAEntero(vo.get("institucion")+""));

			//ps.setString(6, vo.get("cuenta_inventario")+"");
			if(vo.get("cuenta_inventario").equals(ConstantesBD.codigoNuncaValido+"")||vo.get("cuenta_inventario").toString().trim().equals(""))
				ps.setNull(6, Types.NUMERIC);
			else
				ps.setDouble(6, Utilidades.convertirADouble(vo.get("cuenta_inventario")+"" ));
			
			//ps.setString(7, vo.get("cuenta_costo")+"");
			if(vo.get("cuenta_costo").equals(ConstantesBD.codigoNuncaValido+"")||vo.get("cuenta_costo").toString().trim().equals(""))
				ps.setNull(7, Types.NUMERIC);
			else
				ps.setDouble(7, Utilidades.convertirADouble(vo.get("cuenta_costo")+"" ));	
			
			if(vo.get("rubro").equals(ConstantesBD.codigoNuncaValido+"")||vo.get("rubro").toString().trim().equals(""))
				ps.setNull(8, Types.VARCHAR);
			else
				ps.setString(8, vo.get("rubro")+"".trim());
			
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public static boolean eliminarSubGruposInventario(Connection con, String codigo, String institucion, String subgrupo, String grupo, String clase)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaEliminarSubGruposInventario,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * DELETE FROM inventarios.subgrupo_inventario where codigo=? AND subgrupo=?  AND grupo=? AND clase=? AND institucion=?
			 */
			
			ps.setInt(1, Utilidades.convertirAEntero(codigo));
			ps.setInt(2, Utilidades.convertirAEntero(subgrupo));
			ps.setInt(3, Utilidades.convertirAEntero(grupo));
			ps.setInt(4, Utilidades.convertirAEntero(clase));
			ps.setInt(5, Utilidades.convertirAEntero(institucion));
			
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * METODO PARA ELIMINAR LA CUENTA RUBRO ESPECIFICA PARA LA TABLA ESPECIFICA
	 * @param con
	 * @param codigoEliminar
	 * @param tabla
	 * @return
	 */
	public static boolean eliminarRubro(Connection con, int codigoEliminar, String tabla) 
	{
		logger.info("Codigo-->"+codigoEliminar+"<-");
		logger.info("tabla-->"+tabla+"<-");
		
		String cadenaStr="";
		if(tabla.equals("clase"))
			cadenaStr = "UPDATE clase_inventario SET rubro_presupuestal = ? WHERE codigo = ?";
		else
			if(tabla.equals("grupo"))
				cadenaStr = "UPDATE grupo_inventario SET rubro_presupuestal = ? WHERE codigo = ?";
			else
				if(tabla.equals("subgrupo"))
					cadenaStr = "UPDATE subgrupo_inventario SET rubro_presupuestal = ? WHERE codigo = ?";
		
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
	
///	
}
