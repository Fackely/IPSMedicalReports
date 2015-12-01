/*
 * Abr 16, 2007
 */
package com.princetonsa.dao.sqlbase.interfaz;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.UtilidadBD;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * 
 * @author sgomez
 * Objeto usado para el acceso común a la fuente de datos
 * de la funcionalidad Cuenta Inventarios x Unidad Funcional
 */
public class SqlBaseCuentaInvUnidadFunDao 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseCuentaInvUnidadFunDao.class);
	
	/**
	 * Cadena que consulta las clases
	 */
	private static final String consultarClasesStr = "SELECT "+ 
		"ci.codigo AS codigo, "+
		"ci.nombre AS nombre, "+
		"CASE WHEN ciu.cuenta_costo IS NULL THEN '' ELSE ciu.cuenta_costo || '' END AS codigo_cuenta_costo, "+ 
		"CASE WHEN ciu.unidad_funcional IS NULL THEN '"+ConstantesBD.acronimoNo+"' ELSE '"+ConstantesBD.acronimoSi+"' END AS existe_bd, "+
		"CASE WHEN ciu.cuenta_costo IS NULL THEN " +
			"'' " +
		"ELSE "+
			"CASE WHEN cc.cuenta_contable IS NULL THEN '' ELSE cc.cuenta_contable  END || "+
			"CASE WHEN cc.descripcion IS NULL THEN '' ELSE ' - ' || cc.descripcion END || " +
			"CASE WHEN cc.naturaleza_cuenta = '1' THEN ' - DEB' ELSE ' - CRE' END || "+
			"CASE WHEN cc.manejo_terceros = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN ' - SI' ELSE ' - NO' END || "+
			"CASE WHEN cc.manejo_centros_costo = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN ' - SI' ELSE ' - NO' END || "+
			"CASE WHEN cc.manejo_base_gravable = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN ' - SI' ELSE ' - NO' END  "+
		"END AS cuenta_costo "+
		"FROM clase_inventario ci "+ 
		"LEFT OUTER JOIN interfaz.clase_inv_unidad_fun ciu ON(ciu.clase_inventario=ci.codigo AND ciu.unidad_funcional=? AND ciu.institucion=?) "+ 
		"LEFT OUTER JOIN interfaz.cuentas_contables cc ON(cc.codigo=ciu.cuenta_costo) "+ 
		"WHERE "+ 
		"ci.institucion = ? ORDER BY nombre";
	
	/**
	 * Cadena que consulta los grupos
	 */
	private static final String consultarGruposStr = "SELECT "+ 
		"gi.codigo AS codigo, "+
		"gi.clase AS codigo_clase, "+
		"gi.nombre AS nombre, "+
		"CASE WHEN giu.cuenta_costo IS NULL THEN '' ELSE giu.cuenta_costo || '' END AS codigo_cuenta_costo, "+ 
		"CASE WHEN giu.unidad_funcional IS NULL THEN '"+ConstantesBD.acronimoNo+"' ELSE '"+ConstantesBD.acronimoSi+"' END AS existe_bd, "+
		"CASE WHEN giu.cuenta_costo IS NULL THEN " +
			"'' " +
		"ELSE "+
			"CASE WHEN cc.cuenta_contable IS NULL THEN '' ELSE cc.cuenta_contable  END || "+
			"CASE WHEN cc.descripcion IS NULL THEN '' ELSE ' - ' || cc.descripcion END || " +
			"CASE WHEN cc.naturaleza_cuenta = '1' THEN ' - DEB' ELSE ' - CRE' END || "+
			"CASE WHEN cc.manejo_terceros = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN ' - SI' ELSE ' - NO' END || "+
			"CASE WHEN cc.manejo_centros_costo = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN ' - SI' ELSE ' - NO' END || "+
			"CASE WHEN cc.manejo_base_gravable = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN ' - SI' ELSE ' - NO' END  "+
		"END AS cuenta_costo "+
		"FROM grupo_inventario gi "+ 
		"LEFT OUTER JOIN interfaz.grupo_inv_unidad_fun giu ON(giu.grupo_inventario=gi.codigo and giu.clase_inventario=gi.clase AND giu.unidad_funcional=? AND giu.institucion = ?) "+ 
		"LEFT OUTER JOIN interfaz.cuentas_contables cc ON(cc.codigo=giu.cuenta_costo) "+ 
		"WHERE "+ 
		"gi.clase = ?";
	
	/**
	 * Cadena que consulta los subgrupos
	 */
	private static final String consultarSubgruposStr = "SELECT "+ 
		"si.codigo As codigo, "+
		"si.subgrupo AS codigo_subgrupo, "+
		"si.grupo AS codigo_grupo, "+
		"si.clase AS codigo_clase, "+
		"si.nombre AS nombre, "+
		"CASE WHEN siu.cuenta_costo IS NULL THEN '' ELSE siu.cuenta_costo || '' END AS codigo_cuenta_costo, "+ 
		"CASE WHEN siu.unidad_funcional IS NULL THEN '"+ConstantesBD.acronimoNo+"' ELSE '"+ConstantesBD.acronimoSi+"' END AS existe_bd, "+
		"CASE WHEN siu.cuenta_costo IS NULL THEN " +
			"'' " +
		"ELSE "+
			"CASE WHEN cc.cuenta_contable IS NULL THEN '' ELSE cc.cuenta_contable  END || "+
			"CASE WHEN cc.descripcion IS NULL THEN '' ELSE ' - ' || cc.descripcion END || " +
			"CASE WHEN cc.naturaleza_cuenta = '1' THEN ' - DEB' ELSE ' - CRE' END || "+
			"CASE WHEN cc.manejo_terceros = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN ' - SI' ELSE ' - NO' END || "+
			"CASE WHEN cc.manejo_centros_costo = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN ' - SI' ELSE ' - NO' END || "+
			"CASE WHEN cc.manejo_base_gravable = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN ' - SI' ELSE ' - NO' END  "+
		"END AS cuenta_costo "+
		"FROM inventarios.subgrupo_inventario si "+ 
		"LEFT OUTER JOIN interfaz.subgrupo_inv_unidad_fun siu ON(siu.codigo=si.codigo AND siu.subgrupo_inventario=si.subgrupo AND siu.grupo_inventario=si.grupo AND siu.clase_inventario=si.clase AND siu.unidad_funcional = ? AND siu.institucion=?) "+ 
		"LEFT OUTER JOIN interfaz.cuentas_contables cc ON(cc.codigo=siu.cuenta_costo) "+ 
		"WHERE "+ 
		"si.grupo = ? AND si.clase = ?";
	
	/**
	 * Cadena que inserta la clase x unidad funcional 
	 */
	private static final String insertarClaseStr = "INSERT INTO interfaz.clase_inv_unidad_fun (clase_inventario,unidad_funcional,institucion,cuenta_costo) VALUES (?,?,?,?)";
	
	/**
	 * Cadena que inserta el grupo x unidad funcional
	 */
	private static final String insertarGrupoStr = "INSERT INTO interfaz.grupo_inv_unidad_fun (grupo_inventario,clase_inventario,unidad_funcional,institucion,cuenta_costo) VALUES (?,?,?,?,?)";
	
	/**
	 * Cadena que inserta el subgrupo x unidad funcional
	 */
	private static final String insertarSubgrupoStr = "INSERT INTO interfaz.subgrupo_inv_unidad_fun (codigo,subgrupo_inventario,grupo_inventario,clase_inventario,unidad_funcional,institucion,cuenta_costo) VALUES (?,?,?,?,?,?,?)";
	
	/**
	 * Cadena que modifica una clase x unidad funcional
	 */
	private static final String modificarClaseStr="UPDATE interfaz.clase_inv_unidad_fun SET cuenta_costo = ? WHERE clase_inventario = ? AND unidad_funcional = ? AND institucion = ?";
	
	/**
	 * Cadena que modifica un grupo x unidad funcional
	 */
	private static final String modificarGrupoStr = "UPDATE interfaz.grupo_inv_unidad_fun SET cuenta_costo = ? WHERE grupo_inventario = ? AND clase_inventario = ? AND unidad_funcional = ? AND institucion = ?";
	
	/**
	 * Cadena que modifica un subgrupo x unidad funcional
	 */
	private static final String modificarSubgrupoStr = "UPDATE interfaz.subinterfaz.grupo_inv_unidad_fun SET cuenta_costo = ? WHERE codigo = ? AND subgrupo_inventario = ? AND grupo_inventario = ? AND clase_inventario = ? AND unidad_funcional = ? AND institucion = ?";
	
	
	
	/**
	 * Método que consulta las clases de inventario
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap consultarClases(Connection con,HashMap campos)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultarClasesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,campos.get("unidadFuncional").toString());
			pst.setInt(2,Utilidades.convertirAEntero(campos.get("institucion").toString()));
			pst.setInt(3,Utilidades.convertirAEntero(campos.get("institucion").toString()));
			
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,true);
			pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarClases : "+e);
			return null;
		}
	}
	
	/**
	 * Método que consulta los grupos de inventario de una clase específica
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap consultarGrupos(Connection con,HashMap campos)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultarGruposStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,campos.get("unidadFuncional").toString());
			pst.setInt(2,Utilidades.convertirAEntero(campos.get("institucion").toString()));
			pst.setInt(3,Utilidades.convertirAEntero(campos.get("clase").toString()));
			
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,true);
			pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarGrupos: "+e);
			return null;
		}
	}
	
	/**
	 * Método que consulta los subgrupos de un grupo y una clase específicas
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap consultarSubgrupos(Connection con,HashMap campos)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultarSubgruposStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,campos.get("unidadFuncional").toString());
			pst.setInt(2,Utilidades.convertirAEntero(campos.get("institucion").toString()));
			pst.setInt(3,Utilidades.convertirAEntero(campos.get("grupo").toString()));
			pst.setInt(4,Utilidades.convertirAEntero(campos.get("clase").toString()));

			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,true);
			pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarSubgrupos: "+e);
			return null;
		}
	}
	
	/**
	 * Método que inserta una nueva clase x unidad funcional
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int insertarClase(Connection con,HashMap campos)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(insertarClaseStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(campos.get("clase").toString()));
			pst.setString(2,campos.get("unidadFuncional").toString());
			pst.setInt(3,Utilidades.convertirAEntero(campos.get("institucion").toString()));
			int cuentaCosto = Utilidades.convertirAEntero(campos.get("cuentaCosto").toString());
			if(cuentaCosto>0)
				pst.setDouble(4, Utilidades.convertirADouble(cuentaCosto+""));
			else
				pst.setNull(4,Types.NUMERIC);
			
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en insertarClase: "+e);
			return 0;
		}
	}
	
	/**
	 * Método que inserta un grupo x unidad funcional de una clase específica
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int insertarGrupo(Connection con,HashMap campos)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(insertarGrupoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(campos.get("grupo").toString()));
			pst.setInt(2,Utilidades.convertirAEntero(campos.get("clase").toString()));
			pst.setString(3,campos.get("unidadFuncional").toString());
			pst.setInt(4,Utilidades.convertirAEntero(campos.get("institucion").toString()));
			int cuentaCosto = Utilidades.convertirAEntero(campos.get("cuentaCosto").toString());
			if(cuentaCosto>0)
				pst.setDouble(5,Utilidades.convertirADouble(cuentaCosto+""));
			else
				pst.setNull(5,Types.NUMERIC);
			
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en insertarGrupo: "+e);
			return 0;
		}
	}
	
	/**
	 * Método que inserta un subgrupo x unidad funcional de un grupo específico
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int insertarSubgrupo(Connection con,HashMap campos)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(insertarSubgrupoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(campos.get("codigo").toString()));
			pst.setInt(2,Utilidades.convertirAEntero(campos.get("subgrupo").toString()));
			pst.setInt(3,Utilidades.convertirAEntero(campos.get("grupo").toString()));
			pst.setInt(4,Utilidades.convertirAEntero(campos.get("clase").toString()));
			pst.setString(5,campos.get("unidadFuncional").toString());
			pst.setInt(6,Utilidades.convertirAEntero(campos.get("institucion").toString()));
			int cuentaCosto = Utilidades.convertirAEntero(campos.get("cuentaCosto").toString());
			if(cuentaCosto>0)
				pst.setDouble(7,Utilidades.convertirADouble(cuentaCosto+""));
			else
				pst.setNull(7,Types.NUMERIC);
			
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en insertarSubgrupo: "+e);
			return 0;
		}
	}
	
	/**
	 * Método que modifica la clase x unidad funcional
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int modificarClase(Connection con,HashMap campos)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(modificarClaseStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			int cuentaCosto = Utilidades.convertirAEntero(campos.get("cuentaCosto").toString());
			if(cuentaCosto>0)
				pst.setDouble(1,Utilidades.convertirADouble(cuentaCosto+""));
			else
				pst.setNull(1,Types.NUMERIC);
			pst.setInt(2,Utilidades.convertirAEntero(campos.get("clase").toString()));
			pst.setString(3,campos.get("unidadFuncional").toString());
			pst.setInt(4,Utilidades.convertirAEntero(campos.get("institucion").toString()));
			
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en modificarClase: "+e);
			return 0;
		}
	}
	
	/**
	 * Método que modifica el grupo x unidad funcional de una clase específica
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int modificarGrupo(Connection con,HashMap campos)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(modificarGrupoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			int cuentaCosto = Utilidades.convertirAEntero(campos.get("cuentaCosto").toString());
			if(cuentaCosto>0)
				pst.setDouble(1,Utilidades.convertirADouble(cuentaCosto+""));
			else
				pst.setNull(1,Types.NUMERIC);
			pst.setInt(2,Utilidades.convertirAEntero(campos.get("grupo").toString()));
			pst.setInt(3,Utilidades.convertirAEntero(campos.get("clase").toString()));
			pst.setString(4,campos.get("unidadFuncional").toString());
			pst.setInt(5,Utilidades.convertirAEntero(campos.get("institucion").toString()));
			
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en modificarGrupo: "+e);
			return 0;
		}
	}
	
	/**
	 * Método que modifica el subgrupo x unidad funcional de un grupo específico
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int modificarSubgrupo(Connection con,HashMap campos)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(modificarSubgrupoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			int cuentaCosto = Utilidades.convertirAEntero(campos.get("cuentaCosto").toString());
			if(cuentaCosto>0)
				pst.setDouble(1,Utilidades.convertirADouble(cuentaCosto+""));
			else
				pst.setNull(1,Types.NUMERIC);
			pst.setInt(2,Utilidades.convertirAEntero(campos.get("codigo").toString()));
			pst.setInt(3,Utilidades.convertirAEntero(campos.get("subgrupo").toString()));
			pst.setInt(4,Utilidades.convertirAEntero(campos.get("grupo").toString()));
			pst.setInt(5,Utilidades.convertirAEntero(campos.get("clase").toString()));
			pst.setString(6,campos.get("unidadFuncional").toString());
			pst.setInt(7,Utilidades.convertirAEntero(campos.get("institucion").toString()));
			
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en modificarSubgrupo: "+e);
			return 0;
		}
	}
	
}
