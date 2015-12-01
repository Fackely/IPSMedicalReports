/*
 * Creado May 22, 2007
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * SqlBaseDetalleInclusionesExclusionesDao
 * com.princetonsa.dao.sqlbase.facturacion
 * java version "1.5.0_07"
 */
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
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

/**
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * May 22, 2007
 */
public class SqlBaseDetalleInclusionesExclusionesDao
{
	
	/**
	 * 
	 */
	private static Logger logger=Logger.getLogger(SqlBaseDetalleInclusionesExclusionesDao.class);
	
	/**
	 * 
	 */
	private static String cadenaConsultaIncluExclu="SELECT iecc.codigo,iecc.codigo_inclu_exclu as codigoincluexclu,ie.nombre as nomincluexclu,iecc.codigo_centro_costo as codigocc,getnomcentrocosto(iecc.codigo_centro_costo) as nomcc,'BD' as tiporegistro from inclu_exclu_cc iecc inner join  inclusiones_exclusiones ie on(ie.codigo=iecc.codigo_inclu_exclu and ie.institucion=iecc.institucion) where 1=1 ";
	
	/**
	 * 
	 */
	private static String cadenaEliminarIncluExclu="DELETE FROM inclu_exclu_cc where codigo=? ";
	
	/**
	 * 
	 */
	private static String cadenaUpdateIncluExclu="UPDATE inclu_exclu_cc SET codigo_inclu_exclu=?,codigo_centro_costo=?,  usuario_modifica=?,fecha_modifica=?,hora_modifica=? where codigo=?";
	
	
	
	
	
	
	
	
	
	/**
	 * 
	 */
	private static final String consultaAgrupacionArticulos="SELECT codigo as codigo,codigo_inclu_exclu_cc as codigodetincluexclu,clase as clase,getnomclaseinventario(clase) as nomclase,grupo as grupo,getnomgrupoinventario(grupo,clase) as nomgrupo,subgrupo as subgrupo,getnomsubgrupoinventario(subgrupo,grupo,clase) as nomsubgrupo,naturaleza as naturaleza,getnomnaturalezaarticulo(naturaleza) as nomnaturaleza, incluye as incluye,cantidad as cantidad,'BD' as tiporegistro from agru_art_incluexclu_cc ";
	
	/**
	 * 
	 */
	private static final String consultaArticulos="SELECT codigo as codigo,codigo_inclu_exclu_cc as codigodetincluexclu,codigo_articulo as codigo_articulo,getdescarticulosincodigo(codigo_articulo) as descripcion_articulo, incluye as incluye,cantidad as cantidad,'BD' as tiporegistro from art_incluexclu_cc ";
	
	/**
	 * 
	 */
	private static final String consultaAgrupacionServicios="SELECT agsexcc.codigo as codigo,agsexcc.codigo_inclu_exclu_cc as codigodetincluexclu,agsexcc.pos as tipopos, agsexcc.grupo_servicio as gruposervicio,gs.acronimo as acronimogruposervicio,gs.descripcion as descgruposervicio,agsexcc.tipo_servicio as tiposervicio,ts.nombre as nomtiposervicio, agsexcc.especialidad as especialidad,e.nombre as nomespecialidad, incluye as incluye,cantidad as cantidad ,'BD' as tiporegistro from  agru_ser_incluexclu_cc agsexcc left outer join grupos_servicios gs on (gs.codigo=agsexcc.grupo_servicio) left outer join tipos_servicio ts on (ts.acronimo=agsexcc.tipo_servicio) left outer join especialidades e on (e.codigo=agsexcc.especialidad) ";
	
	/**
	 * 
	 */
	private static final String consultaServicios="SELECT sic.codigo as codigo,sic.codigo_inclu_exclu_cc as codigodetincluexclu,sic.codigo_servicio as codigo_servicio,getcodigopropservicio2(s.codigo,0)||' '||getnombreservicio(sic.codigo_servicio,0) as descripcion_servicio, sic.incluye as incluye,sic.cantidad as cantidad,'BD' as tiporegistro from ser_incluexclu_cc sic inner join servicios s on (sic.codigo_servicio=s.codigo) ";
	
	/**
	 * 
	 */
	private static final String eliminarAgrupacionArticulos="DELETE FROM agru_art_incluexclu_cc WHERE codigo=?";
	
	/***
	 * 
	 */
	private static final String modificarAgrupacionArticulos="UPDATE agru_art_incluexclu_cc SET clase=?,grupo=?,subgrupo=?,naturaleza=?,incluye=?,cantidad=?,usuario_modifica=?,fecha_modifica=?,hora_modifica=? WHERE codigo=?";

	/**
	 * 
	 */
	private static final String eliminarArticulos="DELETE FROM art_incluexclu_cc where codigo=?";

	/**
	 * 
	 */
	private static final String modificarArticulos="UPDATE art_incluexclu_cc SET codigo_articulo=?,incluye=?,cantidad=?,usuario_modifica=?,fecha_modifica=?,hora_modifica=? WHERE codigo=?";

	/**
	 * 
	 */
	private static final String eliminarAgrupacionSevicios="DELETE FROM agru_ser_incluexclu_cc WHERE codigo=?";

	/***
	 * 
	 */
	private static final String modificarAgrupacionServicios="UPDATE agru_ser_incluexclu_cc SET pos=?,grupo_servicio=?,tipo_servicio=?,especialidad=?,incluye=?,cantidad=?,usuario_modifica=?,fecha_modifica=?,hora_modifica=? WHERE codigo=?";

	/**
	 * 
	 */
	private static final String eliminarServicios="DELETE FROM ser_incluexclu_cc where codigo=?";

	/**
	 * 
	 */
	private static final String modificarServicios="UPDATE ser_incluexclu_cc set codigo_servicio=?,incluye=?,cantidad=?,usuario_modifica=?,fecha_modifica=?,hora_modifica=? WHERE codigo=?";


	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static HashMap consultarInclusionesExclusiones(Connection con, int institucion)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaIncluExclu+" and iecc.institucion=? order by nomincluexclu,nomcc",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, institucion);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("error en consultarInclusionesExclusiones");
			e.printStackTrace();
		}
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static HashMap consultarInclusionExclusionLLave(Connection con, String codigo)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaIncluExclu+" and iecc.codigo=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigo));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("error en consultarInclusionesExclusiones");
			e.printStackTrace();
		}
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static boolean eliminarInclusionesExclusiones(Connection con, String codigo)
	{
		PreparedStatementDecorator ps=null;
		try
		{

			
			ps= new PreparedStatementDecorator(con.prepareStatement("DELETE FROM agru_art_incluexclu_cc WHERE codigo_inclu_exclu_cc=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigo));
			ps.executeUpdate();

			
			ps= new PreparedStatementDecorator(con.prepareStatement("DELETE FROM art_incluexclu_cc WHERE codigo_inclu_exclu_cc=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigo));
			ps.executeUpdate();

			
			ps= new PreparedStatementDecorator(con.prepareStatement("DELETE FROM agru_ser_incluexclu_cc WHERE codigo_inclu_exclu_cc=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigo));
			ps.executeUpdate();

			
			ps= new PreparedStatementDecorator(con.prepareStatement("DELETE FROM ser_incluexclu_cc WHERE codigo_inclu_exclu_cc=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigo));
			ps.executeUpdate();
			
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaEliminarIncluExclu,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigo));
			ps.executeUpdate();
			return true;
		}
		catch (SQLException e)
		{
			logger.error("error en eliminarInclusionesExclusiones");
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
	public static boolean insertarInclusionExclusion(Connection con, HashMap vo,String cadena)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO inclu_exclu_cc (
			 * codigo,
			 * codigo_inclu_exclu,
			 * institucion,
			 * codigo_centro_costo,
			 * usuario_modifica,
			 * fecha_modifica,
			 * hora_modifica) values('seq_inclu_exclu_cc'),?,?,?,?,?,?)
			 */
			
			ps.setString(1, vo.get("codigoIncluExclu")+"");
			ps.setString(2, vo.get("institucion")+"");
			if(UtilidadTexto.isEmpty(vo.get("codigoCC")+""))
				ps.setObject(3,null);
			else
				ps.setInt(3, Utilidades.convertirAEntero(vo.get("codigoCC")+""));
			ps.setString(4, vo.get("usuario")+"");
			ps.setDate(5, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			ps.setString(6, UtilidadFecha.getHoraActual());
			ps.executeUpdate();
			return true;
		}
		catch (SQLException e)
		{
			logger.error("error en insertarInclusionExclusion");
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
	public static boolean modificarInclusionExclusion(Connection con, HashMap vo)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaUpdateIncluExclu,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE inclu_exclu_cc SET 
			 * codigo_inclu_exclu=?,
			 * codigo_centro_costo=?,  
			 * usuario_modifica=?,
			 * fecha_modifica=?,
			 * hora_modifica=? 
			 * where codigo=?
			 */
			
			ps.setString(1, vo.get("codigoIncluExclu")+"");
			
			if(Utilidades.convertirAEntero(vo.get("codigoCC")+"")<=0)
				ps.setNull(2, Types.INTEGER);
			else
				ps.setInt(2, Utilidades.convertirAEntero(vo.get("codigoCC")+""));
			ps.setString(3, vo.get("usuario")+"");
			ps.setDate(4, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			ps.setString(5, UtilidadFecha.getHoraActual());
			ps.setDouble(6, Utilidades.convertirADouble(vo.get("codigo")+""));
			ps.executeUpdate();
			return true;
		}
		catch (SQLException e)
		{
			logger.error("error en modificarInclusionExclusion");
			e.printStackTrace();
		}
		return false;
	}
	

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static HashMap consultarAgrupacionArticulos(Connection con, String codigo)
	{
		HashMap mapa=new HashMap();
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaAgrupacionArticulos+" where codigo_inclu_exclu_cc=? ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigo));
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
	 * @param codigo
	 * @return
	 */
	public static HashMap consultarAgrupacionArticulosLLave(Connection con, String codigo)
	{
		HashMap mapa=new HashMap();
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaAgrupacionArticulos+" where codigo=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigo));
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
	 * @param codigo
	 * @return
	 */
	public static HashMap consultarAgrupacionServicios(Connection con, String codigo)
	{
		HashMap mapa=new HashMap();
		try
		{
			String cadena=consultaAgrupacionServicios+" where agsexcc.codigo_inclu_exclu_cc=? ";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigo));
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
	 * @param codigo
	 * @return
	 */
	public static HashMap consultarAgrupacionServiciosLLave(Connection con, String codigo)
	{
		HashMap mapa=new HashMap();
		try
		{
			String cadena=consultaAgrupacionServicios+" where agsexcc.codigo=? ";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigo));
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
	 * @param codigo
	 * @return
	 */
	public static HashMap consultarArticulos(Connection con, String codigo)
	{
		HashMap mapa=new HashMap();
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaArticulos+" where codigo_inclu_exclu_cc=? ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigo));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
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
	 * @param codigo
	 * @return
	 */
	public static HashMap consultarArticulosLLave(Connection con, String codigo)
	{
		HashMap mapa=new HashMap();
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaArticulos+" where codigo=? ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigo));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
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
	 * @param codigo
	 * @return
	 */
	public static HashMap consultarServicios(Connection con, String codigo)
	{
		HashMap mapa=new HashMap();
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaServicios+" where sic.codigo_inclu_exclu_cc=? ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigo));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
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
	 * @param codigo
	 * @return
	 */
	public static HashMap consultarServiciosLLave(Connection con, String codigo)
	{
		HashMap mapa=new HashMap();
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaServicios+" where sic.codigo=? ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigo));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
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
	 * @param codigoAgrupacion
	 * @return
	 */
	public static boolean eliminarAgrupacionArticulos(Connection con, String codigoAgrupacion)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(eliminarAgrupacionArticulos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigoAgrupacion));
			ps.executeUpdate();
			return true;
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
	public static boolean modificarAgrupacionArticulos(Connection con, HashMap vo)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarAgrupacionArticulos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE agru_art_incluexclu_cc SET 
			 * clase=?,
			 * grupo=?,
			 * subgrupo=?,
			 * naturaleza=?,
			 * incluye=?,
			 * cantidad=?,
			 * usuario_modifica=?,
			 * fecha_modifica=?,
			 * hora_modifica=? 
			 * WHERE codigo=?
			 */
			
			if(UtilidadTexto.isEmpty(vo.get("clase")+""))
				ps.setObject(1, null);
			else
				ps.setInt(1, Utilidades.convertirAEntero(vo.get("clase")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("grupo")+""))
				ps.setObject(2, null);
			else
				ps.setInt(2, Utilidades.convertirAEntero(vo.get("grupo")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("subgrupo")+""))
				ps.setObject(3, null);
			else
				ps.setInt(3, Utilidades.convertirAEntero(vo.get("subgrupo")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("naturaleza")+""))
				ps.setObject(4, null);
			else
				ps.setString(4, vo.get("naturaleza")+"");

			ps.setString(5,vo.get("incluye")+"");

			if(UtilidadTexto.isEmpty(vo.get("cantidad")+""))
				ps.setObject(6, null);
			else
				ps.setDouble(6,Utilidades.convertirADouble(vo.get("cantidad")+""));

			ps.setString(7, vo.get("usuario")+"");
			ps.setDate(8, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			ps.setString(9, UtilidadFecha.getHoraActual());
			ps.setDouble(10, Utilidades.convertirADouble(vo.get("codigo")+""));
			ps.executeUpdate();
			return true;
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
	 * @param cadena
	 * @return
	 */
	public static boolean insertarAgrupacionArticulos(Connection con, HashMap vo, String cadena)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO agru_art_incluexclu_cc(
			 * codigo,
			 * codigo_inclu_exclu_cc,
			 * clase,
			 * grupo,
			 * subgrupo,
			 * naturaleza,
			 * institucion,
			 * incluye,
			 * cantidad,
			 * usuario_modifica,
			 * fecha_modifica,
			 * hora_modifica) values('seq_agru_art_incluexclu_cc'),?,?,?,?,?,?,?,?,?,?,?)
			 */
			
			ps.setDouble(1, Utilidades.convertirADouble(vo.get("codigoDetIncluExclu")+""));
			if(UtilidadTexto.isEmpty(vo.get("clase")+""))
				ps.setObject(2, null);
			else
				ps.setInt(2, Utilidades.convertirAEntero(vo.get("clase")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("grupo")+""))
				ps.setObject(3, null);
			else
				ps.setInt(3, Utilidades.convertirAEntero(vo.get("grupo")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("subgrupo")+""))
				ps.setObject(4, null);
			else
				ps.setInt(4, Utilidades.convertirAEntero(vo.get("subgrupo")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("naturaleza")+""))
				ps.setObject(5, null);
			else
				ps.setString(5, vo.get("naturaleza")+"");

			ps.setInt(6, Utilidades.convertirAEntero(vo.get("institucion")+""));
			
			ps.setString(7,vo.get("incluye")+"");

			if(UtilidadTexto.isEmpty(vo.get("cantidad")+""))
				ps.setObject(8, null);
			else
				ps.setDouble(8,Utilidades.convertirADouble(vo.get("cantidad")+""));
			
			ps.setString(9, vo.get("usuario")+"");
			ps.setDate(10, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			ps.setString(11, UtilidadFecha.getHoraActual());
			ps.executeUpdate();
			return true;
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
	 * @param codigoComponente
	 * @return
	 */
	public static boolean eliminarArticulos(Connection con, String codigoComponente)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(eliminarArticulos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigoComponente));
			ps.executeUpdate();
			return true;
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
	public static boolean modificarArticulos(Connection con, HashMap vo)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarArticulos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE art_incluexclu_cc SET codigo_articulo=?,incluye=?,cantidad=?,usuario_modifica=?,fecha_modifica=?,hora_modifica=? WHERE codigo=?
			 */
			
			if(UtilidadTexto.isEmpty(vo.get("articulo")+""))
				ps.setObject(1, null);
			else
				ps.setInt(1, Utilidades.convertirAEntero(vo.get("articulo")+""));
			
			ps.setString(2,vo.get("incluye")+"");

			if(UtilidadTexto.isEmpty(vo.get("cantidad")+""))
				ps.setObject(3, null);
			else
				ps.setDouble(3,Utilidades.convertirADouble(vo.get("cantidad")+""));
	

			ps.setString(4, vo.get("usuario")+"");
			ps.setDate(5, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			ps.setString(6, UtilidadFecha.getHoraActual());
			ps.setDouble(7, Utilidades.convertirADouble((vo.get("codigo")+"")));
			ps.executeUpdate();
			return true;
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
	 * @param cadena
	 * @return
	 */
	public static boolean insertarArticulos(Connection con, HashMap vo, String cadena)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO art_incluexclu_cc (
			 * codigo,
			 * codigo_inclu_exclu_cc,
			 * codigo_articulo,
			 * incluye,
			 * cantidad,
			 * usuario_modifica,
			 * fecha_modifica,
			 * hora_modifica) values ('seq_art_incluexclu_cc'),?,?,?,?,?,?,?)
			 */
			
			ps.setDouble(1, Utilidades.convertirADouble(vo.get("codigoDetIncluExclu")+""));
			if(UtilidadTexto.isEmpty(vo.get("articulo")+""))
				ps.setObject(2, null);
			else
				ps.setInt(2, Utilidades.convertirAEntero(vo.get("articulo")+""));

			ps.setString(3,vo.get("incluye")+"");

			if(UtilidadTexto.isEmpty(vo.get("cantidad")+""))
				ps.setObject(4, null);
			else
				ps.setDouble(4,Utilidades.convertirADouble(vo.get("cantidad")+""));
			
			ps.setString(5, vo.get("usuario")+"");
			ps.setDate(6, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			ps.setString(7, UtilidadFecha.getHoraActual());
			ps.executeUpdate();
			return true;
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
	 * @param codigoAgrupacion
	 * @return
	 */
	public static boolean eliminarAgrupacionServicios(Connection con, String codigoAgrupacion)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(eliminarAgrupacionSevicios,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigoAgrupacion));
			ps.executeUpdate();
			return true;
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
	 * @param cadena
	 * @return
	 */
	public static boolean insertarAgrupacionServicios(Connection con, HashMap vo, String cadena)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO agru_ser_incluexclu_cc(
			 * codigo,
			 * codigo_inclu_exclu_cc,
			 * pos,
			 * grupo_servicio,
			 * tipo_servicio,
			 * especialidad,
			 * incluye,
			 * cantidad,
			 * usuario_modifica,
			 * fecha_modifica,
			 * hora_modifica) values ('seq_agru_ser_incluexclu_cc'),?,?,?,?,?,?,?,?,?,?)
			 */
			
			ps.setDouble(1, Utilidades.convertirADouble(vo.get("codigoDetIncluExclu")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("tipopos")+""))
				ps.setObject(2, null);
			else
				ps.setString(2, vo.get("tipopos")+"");
			
			if(UtilidadTexto.isEmpty(vo.get("gruposervicio")+""))
				ps.setObject(3, null);
			else
				ps.setInt(3, Utilidades.convertirAEntero(vo.get("gruposervicio")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("tiposervicio")+""))
				ps.setObject(4, null);
			else
				ps.setString(4, vo.get("tiposervicio")+"");
			
			if(UtilidadTexto.isEmpty(vo.get("especialidad")+""))
				ps.setObject(5, null);
			else
				ps.setInt(5, Utilidades.convertirAEntero(vo.get("especialidad")+""));			

			ps.setString(6,vo.get("incluye")+"");

			if(UtilidadTexto.isEmpty(vo.get("cantidad")+""))
				ps.setObject(7, null);
			else
				ps.setDouble(7,Utilidades.convertirADouble(vo.get("cantidad")+""));
						
			ps.setString(8, vo.get("usuario")+"");
			ps.setDate(9, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			ps.setString(10, UtilidadFecha.getHoraActual());
			ps.executeUpdate();
			return true;
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
	public static boolean modificarAgrupacionServicios(Connection con, HashMap vo)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarAgrupacionServicios,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE agru_ser_incluexclu_cc SET 
			 * pos=?,
			 * grupo_servicio=?,
			 * tipo_servicio=?,
			 * especialidad=?,
			 * incluye=?,
			 * cantidad=?,
			 * usuario_modifica=?,
			 * fecha_modifica=?,
			 * hora_modifica=? WHERE codigo=?
			 */
			
			if(UtilidadTexto.isEmpty(vo.get("tipopos")+""))
				ps.setObject(1, null);
			else
				ps.setString(1, vo.get("tipopos")+"");
			
			if(UtilidadTexto.isEmpty(vo.get("gruposervicio")+""))
				ps.setObject(2, null);
			else
				ps.setInt(2, Utilidades.convertirAEntero(vo.get("gruposervicio")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("tiposervicio")+""))
				ps.setObject(3, null);
			else
				ps.setString(3, vo.get("tiposervicio")+"");
			
			if(UtilidadTexto.isEmpty(vo.get("especialidad")+""))
				ps.setObject(4, null);
			else
				ps.setInt(4, Utilidades.convertirAEntero(vo.get("especialidad")+""));

			ps.setString(5,vo.get("incluye")+"");

			if(UtilidadTexto.isEmpty(vo.get("cantidad")+""))
				ps.setObject(6, null);
			else
				ps.setDouble(6,Utilidades.convertirADouble(vo.get("cantidad")+""));
			
			ps.setString(7, vo.get("usuario")+"");
			ps.setDate(8, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			ps.setString(9, UtilidadFecha.getHoraActual());
			ps.setDouble(10, Utilidades.convertirADouble(vo.get("codigo")+""));
			ps.executeUpdate();
			return true;
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
	 * @param codigoComponente
	 * @return
	 */
	public static boolean eliminarServicios(Connection con, String codigoComponente)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(eliminarServicios,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigoComponente));
			ps.executeUpdate();
			return true;
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
	 * @param cadena
	 * @return
	 */
	public static boolean insertarServicios(Connection con, HashMap vo, String cadena)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO ser_incluexclu_cc(
			 * codigo,
			 * codigo_inclu_exclu_cc,
			 * codigo_servicio,
			 * incluye,
			 * cantidad,
			 * usuario_modifica,
			 * fecha_modifica,
			 * hora_modifica) values ('seq_ser_incluexclu_cc'),?,?,?,?,?,?,?)
			 */
			
			ps.setDouble(1, Utilidades.convertirADouble(vo.get("codigoDetIncluExclu")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("servicio")+""))
				ps.setObject(2, null);
			else
				ps.setInt(2, Utilidades.convertirAEntero(vo.get("servicio")+""));
			
			ps.setString(3,vo.get("incluye")+"");

			if(UtilidadTexto.isEmpty(vo.get("cantidad")+""))
				ps.setObject(4, null);
			else
				ps.setDouble(4,Utilidades.convertirADouble(vo.get("cantidad")+""));
			
			ps.setString(5, vo.get("usuario")+"");
			ps.setDate(6, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			ps.setString(7, UtilidadFecha.getHoraActual());
			ps.executeUpdate();
			return true;
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
	public static boolean modificarServicios(Connection con, HashMap vo)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarServicios,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE ser_incluexclu_cc set codigo_servicio=?,incluye=?,cantidad=?,usuario_modifica=?,fecha_modifica=?,hora_modifica=? WHERE codigo=?
			 */
			
			if(UtilidadTexto.isEmpty(vo.get("servicio")+""))
				ps.setObject(1, null);
			else
				ps.setInt(1, Utilidades.convertirAEntero(vo.get("servicio")+""));

			ps.setString(2,vo.get("incluye")+"");

			if(UtilidadTexto.isEmpty(vo.get("cantidad")+""))
				ps.setObject(3, null);
			else
				ps.setDouble(3,Utilidades.convertirADouble(vo.get("cantidad")+""));
			
			ps.setString(4, vo.get("usuario")+"");
			ps.setDate(5, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			ps.setString(6, UtilidadFecha.getHoraActual());
			ps.setDouble(7, Utilidades.convertirADouble(vo.get("codigo")+""));
			ps.executeUpdate();
			return true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

}