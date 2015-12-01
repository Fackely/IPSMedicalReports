/*
 * Creado May 7, 2007
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * SqlBaseComponentesPaquetesDao
 * com.princetonsa.dao.sqlbase.facturacion
 * java version "1.5.0_07"
 */
package com.princetonsa.dao.sqlbase.facturacion;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.InfoDatosString;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

/**
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * May 7, 2007
 */
public class SqlBaseComponentesPaquetesDao
{
	
	/**
	 * 
	 */
	private static Logger logger=Logger.getLogger(SqlBaseComponentesPaquetesDao.class);
	
	/**
	 * 
	 */
	private static final String consultaListadoPaquetes="SELECT codigo_paquete as codigo,descripcion as descripcion from paquetes where institucion=? order by descripcion ";
	
	
	/**
	 * 
	 */
	private static final String consultaAgrupacionArticulos="SELECT " +
																	"codigo_paq_agrup_articulo as codigo," +
																	"codigo_paquete as paquete," +
																	"grupo_especial_articulo as grupoespecial, " +
																	"getnomgrupoespecialarticulo(grupo_especial_articulo) as nomgrupoespecial," +
																	"clase as clase," +
																	"getnomclaseinventario(clase) as nomclase," +
																	"grupo as grupo," +
																	"getnomgrupoinventario(grupo,clase) as nomgrupo," +
																	"subgrupo as subgrupo," +
																	"getnomsubgrupoinventario(subgrupo,grupo,clase) as nomsubgrupo," +
																	"naturaleza as naturaleza," +
																	"getnomnaturalezaarticulo(naturaleza) as nomnaturaleza, " +
																	"cantidad as cantidad," +
																	"'BD' as tiporegistro " +
																"from " +
																	"facturacion.paq_agrupacion_articulos ";
	
	/**
	 * 
	 */
	private static final String consultaArticulos="SELECT codigo_paq_articulo as codigo,codigo_paquete as paquete,codigo_articulo as codigo_articulo,getdescarticulo(codigo_articulo) as descripcion_articulo,cantidad as cantidad,'BD' as tiporegistro from paq_comp_articulos ";
	
	/**
	 * 
	 */
	private static final String consultaAgrupacionServicios="SELECT pas.codigo_paq_agrup_servicio as codigo,pas.codigo_paquete as paquete, pas.grupo_servicio as gruposervicio,gs.acronimo as acronimogruposervicio,gs.descripcion as descgruposervicio,pas.tipo_servicio as tiposervicio,ts.nombre as nomtiposervicio, especialidad as especialidad,e.nombre as nomespecialidad,cantidad as cantidad,'BD' as tiporegistro from paq_agrupacion_servicios pas left outer join grupos_servicios gs on (gs.codigo=pas.grupo_servicio) left outer join tipos_servicio ts on (ts.acronimo=pas.tipo_servicio) left outer join especialidades e on (e.codigo=pas.especialidad) ";
	
	/**
	 * 
	 */
	private static final String consultaServicios="SELECT codigo_paq_servicio as codigo,codigo_paquete as paquete,codigo_servicio as codigo_servicio,getcodigopropservicio2(s.codigo,0)||' '||getnombreservicio(codigo_servicio,0) as descripcion_servicio,principal as principal,cantidad as cantidad,'BD' as tiporegistro from paq_comp_servicios pcs inner join servicios s on(pcs.codigo_servicio=s.codigo) ";
	
	/**
	 * 
	 */
	private static final String eliminarAgrupacionArticulos="DELETE FROM paq_agrupacion_articulos WHERE codigo_paq_agrup_articulo=?";
	
	/***
	 * 
	 */
	private static final String modificarAgrupacionArticulos="UPDATE paq_agrupacion_articulos SET clase=?,grupo=?,subgrupo=?,naturaleza=?,cantidad=?,usuario_modifica=?,fecha_modifica=?,hora_modifica=?,grupo_especial_articulo=? WHERE codigo_paq_agrup_articulo=?";
	
	/**
	 * 
	 */
	private static final String eliminarArticulos="DELETE FROM paq_comp_articulos where codigo_paq_articulo=?";
	
	/**
	 * 
	 */
	private static final String modificarArticulos="UPDATE paq_comp_articulos SET codigo_articulo=?,cantidad=?,usuario_modifica=?,fecha_modifica=?,hora_modifica=? WHERE codigo_paq_articulo=?";

	/**
	 * 
	 */
	private static final String eliminarAgrupacionSevicios="DELETE FROM paq_agrupacion_servicios WHERE codigo_paq_agrup_servicio=?";

	/***
	 * 
	 */
	private static final String modificarAgrupacionServicios="UPDATE paq_agrupacion_servicios SET grupo_servicio=?,tipo_servicio=?,especialidad=?,cantidad=?,usuario_modifica=?,fecha_modifica=?,hora_modifica=? WHERE codigo_paq_agrup_servicio=?";

	/**
	 * 
	 */
	private static final String eliminarServicios="DELETE FROM paq_comp_servicios where codigo_paq_servicio=?";

	/**
	 * 
	 */
	private static final String modificarServicios="UPDATE paq_comp_servicios set codigo_servicio=?,principal=?,cantidad=?,usuario_modifica=?,fecha_modifica=?,hora_modifica=? where codigo_paq_servicio=?";
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static Vector<InfoDatosString> obtenerListadoPaquetes(Connection con, int institucion)
	{
		Vector<InfoDatosString> resultado=new Vector<InfoDatosString>();
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaListadoPaquetes,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, institucion);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while (rs.next())
			{
				resultado.add(new InfoDatosString(rs.getString("codigo"),rs.getString("descripcion")));
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param codigoPaquete
	 * @param institucion
	 * @return
	 */
	public static HashMap consultarAgrupacionArticulosPaquete(Connection con, String codigoPaquete, int institucion)
	{
		HashMap mapa=new HashMap();
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaAgrupacionArticulos+" where codigo_paquete=? and institucion=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codigoPaquete);
			ps.setInt(2, institucion);
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
	 * @param codigoPaquete
	 * @param institucion
	 * @return
	 */
	public static HashMap consultarAgrupacionServiciosPaquete(Connection con, String codigoPaquete, int institucion)
	{
		HashMap mapa=new HashMap();
		try
		{
			String cadena=consultaAgrupacionServicios+" where pas.codigo_paquete=? and pas.institucion=?";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codigoPaquete);
			ps.setInt(2, institucion);
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
	 * @param codigoPaquete
	 * @param institucion
	 * @return
	 */
	public static HashMap consultarArticulosPaquete(Connection con, String codigoPaquete, int institucion)
	{
		HashMap mapa=new HashMap();
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaArticulos+" where codigo_paquete=? and institucion=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codigoPaquete);
			ps.setInt(2, institucion);
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
	 * @param codigoPaquete
	 * @param institucion
	 * @return
	 */
	public static HashMap consultarServiciosPaquete(Connection con, String codigoPaquete, int institucion)
	{
		HashMap mapa=new HashMap();
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaServicios+" where codigo_paquete=? and institucion=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codigoPaquete);
			ps.setInt(2, institucion);
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
	 * @param codigo
	 * @return
	 */
	public static HashMap consultarAgrupacionArticulosPaqueteLLave(Connection con, String codigo)
	{
		HashMap mapa=new HashMap();
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaAgrupacionArticulos+" where codigo_paq_agrup_articulo=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
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
	 * @param vo
	 * @return
	 */
	public static boolean modificarAgrupacionArticulos(Connection con, HashMap vo)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarAgrupacionArticulos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE paq_agrupacion_articulos SET 
			 * clase=?,
			 * grupo=?,
			 * subgrupo=?,
			 * naturaleza=?,
			 * cantidad=?,
			 * usuario_modifica=?,
			 * fecha_modifica=?,
			 * hora_modifica=? 
			 * WHERE codigo_paq_agrup_articulo=?";
			 */
			
			if(UtilidadTexto.isEmpty(vo.get("clase")+""))
				ps.setObject(1, null);
			else
				ps.setInt(1, Utilidades.convertirAEntero(vo.get("clase").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("grupo")+""))
				ps.setObject(2, null);
			else
				ps.setInt(2, Utilidades.convertirAEntero(vo.get("grupo").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("subgrupo")+""))
				ps.setObject(3, null);
			else
				ps.setInt(3, Utilidades.convertirAEntero(vo.get("subgrupo").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("naturaleza")+""))
				ps.setObject(4, null);
			else
				ps.setString(4, vo.get("naturaleza").toString());
			
			if(UtilidadTexto.isEmpty(vo.get("cantidad")+""))
				ps.setObject(5, null);
			else
				ps.setDouble(5, Utilidades.convertirADouble(vo.get("cantidad").toString()));
			
			ps.setString(6, vo.get("usuario")+"");
			ps.setDate(7, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			ps.setString(8, UtilidadFecha.getHoraActual());
			if(UtilidadTexto.isEmpty(vo.get("grupoespecial")+""))
				ps.setNull(9, Types.DOUBLE);
			else
				ps.setDouble(9, Utilidades.convertirADouble(vo.get("grupoespecial")+""));
			ps.setDouble(10, Utilidades.convertirADouble(vo.get("codigo").toString()));
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
			 * INSERT INTO paq_agrupacion_articulos 
			 * (codigo_paq_agrup_articulo,
			 * codigo_paquete,
			 * institucion,
			 * clase,
			 * grupo,
			 * subgrupo,
			 * naturaleza,
			 * cantidad,
			 * usuario_modifica,
			 * fecha_modifica,
			 * hora_modifica) 
			 * values ('seq_paq_agrup_art'),?,?,?,?,?,?,?,?,?,?)
			 */
			
			ps.setString(1, vo.get("paquete").toString());
			
			ps.setInt(2, Utilidades.convertirAEntero(vo.get("institucion").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("clase")+""))
				ps.setObject(3, null);
			else
				ps.setInt(3, Utilidades.convertirAEntero(vo.get("clase").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("grupo")+""))
				ps.setObject(4, null);
			else
				ps.setInt(4, Utilidades.convertirAEntero(vo.get("grupo").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("subgrupo")+""))
				ps.setObject(5, null);
			else
				ps.setInt(5, Utilidades.convertirAEntero(vo.get("subgrupo").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("naturaleza")+""))
				ps.setObject(6, null);
			else
				ps.setString(6, vo.get("naturaleza").toString());
			
			if(UtilidadTexto.isEmpty(vo.get("cantidad")+""))
				ps.setObject(7, null);
			else
				ps.setDouble(7, Utilidades.convertirADouble(vo.get("cantidad").toString()));
			
			ps.setString(8, vo.get("usuario").toString());
			
			ps.setDate(9, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			
			ps.setString(10, UtilidadFecha.getHoraActual());
			
			if(UtilidadTexto.isEmpty(vo.get("grupoespecial")+""))
				ps.setNull(11, Types.DOUBLE);
			else
				ps.setDouble(11, Utilidades.convertirADouble(vo.get("grupoespecial")+""));
			
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
	 * @param codigo
	 * @return
	 */
	public static HashMap consultarArticulosPaqueteLLave(Connection con, String codigo)
	{
		HashMap mapa=new HashMap();
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaArticulos+" where codigo_paq_articulo=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
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
	 * @param vo
	 * @return
	 */
	public static boolean modificarArticulos(Connection con, HashMap vo)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarArticulos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE paq_comp_articulos SET 
			 * codigo_articulo=?,
			 * cantidad=?,
			 * usuario_modifica=?,
			 * fecha_modifica=?,
			 * hora_modifica=? 
			 * WHERE codigo_paq_articulo=?";
			 */
			
			if(UtilidadTexto.isEmpty(vo.get("articulo")+""))
				ps.setObject(1, null);
			else
				ps.setInt(1, Utilidades.convertirAEntero(vo.get("articulo").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("cantidad")+""))
				ps.setObject(2, null);
			else
				ps.setDouble(2, Utilidades.convertirADouble(vo.get("cantidad").toString()));
			
			ps.setString(3, vo.get("usuario").toString());
			
			ps.setDate(4, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			
			ps.setString(5, UtilidadFecha.getHoraActual());
			
			ps.setDouble(6, Utilidades.convertirADouble(vo.get("codigo").toString()));
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
	public static boolean insertarArticulos(Connection con, HashMap vo,String cadena)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO paq_comp_articulos 
			 * (codigo_paq_articulo,
			 * codigo_paquete,
			 * institucion,
			 * codigo_articulo,
			 * cantidad,
			 * usuario_modifica,
			 * fecha_modifica,
			 * hora_modifica) 
			 * values('seq_paq_comp_art'),?,?,?,?,?,?,?)
			 */
			
			ps.setString(1, vo.get("paquete").toString());
			
			ps.setInt(2, Utilidades.convertirAEntero(vo.get("institucion").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("articulo")+""))
				ps.setObject(3, null);
			else
				ps.setInt(3, Utilidades.convertirAEntero(vo.get("articulo").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("cantidad")+""))
				ps.setObject(4, null);
			else
				ps.setDouble(4, Utilidades.convertirADouble(vo.get("cantidad").toString()));
			
			ps.setString(5, vo.get("usuario").toString());
			
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
	 * @param codigo
	 * @return
	 */
	public static HashMap consultarAgrupacionServiciosPaqueteLLave(Connection con, String codigo)
	{
		HashMap mapa=new HashMap();
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaAgrupacionServicios+" where pas.codigo_paq_agrup_servicio=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
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
			 * INSERT INTO paq_agrupacion_servicios 
			 * (codigo_paq_agrup_servicio,
			 * codigo_paquete,
			 * institucion,
			 * grupo_servicio,
			 * tipo_servicio,
			 * especialidad,
			 * cantidad,
			 * usuario_modifica,
			 * fecha_modifica,
			 * hora_modifica) values('seq_paq_agrupacion_serv'),?,?,?,?,?,?,?,?,?)
			 */
			
			ps.setString(1, vo.get("paquete").toString());
			
			ps.setInt(2, Utilidades.convertirAEntero(vo.get("institucion").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("gruposervicio")+""))
				ps.setObject(3, null);
			else
				ps.setInt(3, Utilidades.convertirAEntero(vo.get("gruposervicio").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("tiposervicio")+""))
				ps.setObject(4, null);
			else
				ps.setString(4, vo.get("tiposervicio").toString());
			
			if(UtilidadTexto.isEmpty(vo.get("especialidad")+""))
				ps.setObject(5, null);
			else
				ps.setInt(5, Utilidades.convertirAEntero(vo.get("especialidad").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("cantidad")+""))
				ps.setObject(6, null);
			else
				ps.setDouble(6, Utilidades.convertirADouble(vo.get("cantidad").toString()));
			
			ps.setString(7, vo.get("usuario").toString());
			
			ps.setDate(8, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			
			ps.setString(9, UtilidadFecha.getHoraActual());
			
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
			 * UPDATE paq_agrupacion_servicios SET 
			 * grupo_servicio=?,
			 * tipo_servicio=?,
			 * especialidad=?,
			 * cantidad=?,
			 * usuario_modifica=?,
			 * fecha_modifica=?,
			 * hora_modifica=? 
			 * WHERE codigo_paq_agrup_servicio=?";
			 */
			
			if(UtilidadTexto.isEmpty(vo.get("gruposervicio")+""))
				ps.setObject(1, null);
			else
				ps.setInt(1, Utilidades.convertirAEntero(vo.get("gruposervicio").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("tiposervicio")+""))
				ps.setObject(2, null);
			else
				ps.setString(2, vo.get("tiposervicio").toString());
			
			if(UtilidadTexto.isEmpty(vo.get("especialidad")+""))
				ps.setObject(3, null);
			else
				ps.setInt(3, Utilidades.convertirAEntero(vo.get("especialidad").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("cantidad")+""))
				ps.setObject(4, null);
			else
				ps.setDouble(4, Utilidades.convertirADouble(vo.get("cantidad").toString()));
			
			ps.setString(5, vo.get("usuario").toString());
			
			ps.setDate(6, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			
			ps.setString(7, UtilidadFecha.getHoraActual());
			
			ps.setDouble(8, Utilidades.convertirADouble(vo.get("codigo").toString()));
			
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
	 * @param codigo
	 * @return
	 */
	public static HashMap consultarServiciosPaqueteLLave(Connection con, String codigo)
	{
		HashMap mapa=new HashMap();
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaServicios+" where codigo_paq_servicio=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
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
			 * INSERT INTO paq_comp_servicios 
			 * (codigo_paq_servicio,
			 * codigo_paquete,
			 * institucion,
			 * codigo_servicio,
			 * principal,
			 * cantidad,
			 * usuario_modifica,
			 * fecha_modifica,
			 * hora_modifica) 
			 * values('seq_paq_comp_serv'),?,?,?,?,?,?,?,?)
			 */
			
			ps.setString(1, vo.get("paquete").toString());
			
			ps.setInt(2, Utilidades.convertirAEntero(vo.get("institucion").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("servicio")+""))
				ps.setObject(3, null);
			else
				ps.setString(3, vo.get("servicio").toString());
			
			if(UtilidadTexto.isEmpty(vo.get("principal")+""))
				ps.setString(4, ConstantesBD.acronimoNo);
			else
				ps.setString(4, vo.get("principal").toString());
			
			if(UtilidadTexto.isEmpty(vo.get("cantidad")+""))
				ps.setObject(5, null);
			else
				ps.setDouble(5, Utilidades.convertirADouble(vo.get("cantidad").toString()));
			
			ps.setString(6, vo.get("usuario").toString());
			
			ps.setDate(7, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			
			ps.setString(8, UtilidadFecha.getHoraActual());
			
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
			 * UPDATE paq_comp_servicios set 
			 * codigo_servicio=?,
			 * principal=?,
			 * cantidad=?,
			 * usuario_modifica=?,
			 * fecha_modifica=?,
			 * hora_modifica=? 
			 * where codigo_paq_servicio=?";
			 */
			
			if(UtilidadTexto.isEmpty(vo.get("servicio")+""))
				ps.setObject(1, null);
			else
				ps.setInt(1, Utilidades.convertirAEntero(vo.get("servicio").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("principal")+""))
				ps.setString(2, ConstantesBD.acronimoNo);
			else
				ps.setString(2, vo.get("principal").toString());
			
			if(UtilidadTexto.isEmpty(vo.get("cantidad")+""))
				ps.setObject(3, null);
			else
				ps.setDouble(3, Utilidades.convertirADouble(vo.get("cantidad").toString()));
			
			ps.setString(4, vo.get("usuario").toString());
			
			ps.setDate(5, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			
			ps.setString(6, UtilidadFecha.getHoraActual());
			
			ps.setDouble(7, Utilidades.convertirADouble(vo.get("codigo").toString()));
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
