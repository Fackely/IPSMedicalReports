/*
 * Creado May 23, 2007
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * SqlBaseInclusionExclusionConvenioDao
 * com.princetonsa.dao.sqlbase.facturacion
 * java version "1.5.0_07"
 */
package com.princetonsa.dao.sqlbase.facturacion;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
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
 * May 23, 2007
 */
public class SqlBaseInclusionExclusionConvenioDao
{
	/**
	 * 
	 */
	private static Logger logger=Logger.getLogger(SqlBaseInclusionExclusionConvenioDao.class);

	/**
	 * 
	 */
	private static String cadenaConsultaDetIncluExclu="SELECT iecc.codigo||'' as codigo,ie.nombre ||coalesce(' - '||getnomcentrocosto(iecc.codigo_centro_costo),'') as nombre from inclu_exclu_cc iecc inner join  inclusiones_exclusiones ie on(ie.codigo=iecc.codigo_inclu_exclu and ie.institucion=iecc.institucion) where iecc.institucion=?";
	
	/**
	 * 
	 */
	public static String consultaExcepcionesContrato="SELECT codigo as codigo,codigo_centro_costo as centrocosto,getnomcentrocosto(codigo_centro_costo) as nomcentrocosto,'BD' as tiporegistro, to_char(fecha_vigencia,'dd/mm/yyyy') as fechavigencia,to_char(fecha_vigencia,'dd/mm/yyyy') as fechavigenciaanterior from excep_incluexclu_contr ";

	/**
	 * 
	 */
	public static String consultaInclusionesExclusion="SELECT iec.codigo_inclu_exclu_cc as codigoincluexclucc,getnomcentrocosto(iecc.codigo_centro_costo) as nomcentrocosto,ie.nombre as nomincluexclu,iec.prioridad as prioridad,'BD' as tiporegistro, to_char(fecha_vigencia,'dd/mm/yyyy') as fechavigencia,to_char(fecha_vigencia,'dd/mm/yyyy') as fechavigenciaanterior from incluexclu_contrato iec inner join inclu_exclu_cc iecc on(iec.codigo_inclu_exclu_cc=iecc.codigo) inner join inclusiones_exclusiones ie on(ie.codigo=iecc.codigo_inclu_exclu and ie.institucion=iecc.institucion)  where iec.institucion=? and iec.codigo_contrato=? ";
	
	/**
	 * 
	 */
	public static String cadenaModificarExcepcion="UPDATE excep_incluexclu_contr SET codigo_centro_costo=?,usuario_modifica=?,fecha_modifica=?,hora_modifica=?,fecha_vigencia=? where codigo=?";
	
	/**
	 * 
	 */
	public static String cadenaEliminarExcepcion="DELETE FROM excep_incluexclu_contr where codigo=?";
	
	/**
	 * 
	 */
	private static String insertarInclusionExclusion="INSERT INTO incluexclu_contrato(codigo_contrato,codigo_inclu_exclu_cc,prioridad,institucion,usuario_modifica,fecha_modifica,hora_modifica,fecha_vigencia) values(?,?,?,?,?,?,?,?)";

	/**
	 * 
	 */
	private static String cadenaEliminarInclusionExclusion="DELETE FROM incluexclu_contrato where codigo_contrato=? and codigo_inclu_exclu_cc=? and institucion=?";
	
	


	/**
	 * 
	 */
	private static String updateInclusionExclusion="UPDATE incluexclu_contrato SET prioridad=?,usuario_modifica=?,fecha_modifica=?,hora_modifica=?,fecha_vigencia=? where codigo_contrato=? and codigo_inclu_exclu_cc=? and institucion=?";
	

	
	
	
	
	
	
	
	
	/**
	 * 
	 */
	private static final String consultaAgrupacionArticulos="SELECT codigo as codigo,codigo_excepcion as codigodetincluexclu,clase as clase,getnomclaseinventario(clase) as nomclase,grupo as grupo,getnomgrupoinventario(grupo,clase) as nomgrupo,subgrupo as subgrupo,getnomsubgrupoinventario(subgrupo,grupo,clase) as nomsubgrupo,naturaleza as naturaleza,getnomnaturalezaarticulo(naturaleza) as nomnaturaleza, incluye as incluye,cantidad as cantidad,'BD' as tiporegistro, to_char(fecha_vigencia,'dd/mm/yyyy') as fechavigencia from agru_art_incluexclu_econt ";
	
	/**
	 * 
	 */
	private static final String consultaArticulos="SELECT codigo as codigo,codigo_excepcion as codigodetincluexclu,codigo_articulo as codigo_articulo,getdescarticulosincodigo(codigo_articulo) as descripcion_articulo, incluye as incluye,cantidad as cantidad,'BD' as tiporegistro, to_char(fecha_vigencia,'dd/mm/yyyy') as fechavigencia from art_incluexclu_econt ";
	
	/**
	 * 
	 */
	private static final String consultaAgrupacionServicios="SELECT agsexcc.codigo as codigo,agsexcc.codigo_excepcion as codigodetincluexclu,agsexcc.pos as tipopos, agsexcc.grupo_servicio as gruposervicio,gs.acronimo as acronimogruposervicio,gs.descripcion as descgruposervicio,agsexcc.tipo_servicio as tiposervicio,ts.nombre as nomtiposervicio, agsexcc.especialidad as especialidad,e.nombre as nomespecialidad, incluye as incluye,cantidad as cantidad ,'BD' as tiporegistro, to_char(fecha_vigencia,'dd/mm/yyyy') as fechavigencia from  agru_ser_incluexclu_econt agsexcc left outer join grupos_servicios gs on (gs.codigo=agsexcc.grupo_servicio) left outer join tipos_servicio ts on (ts.acronimo=agsexcc.tipo_servicio) left outer join especialidades e on (e.codigo=agsexcc.especialidad) ";
	
	/**
	 * 
	 */
	//s.especialidad||'-'||s.codigo
	private static final String consultaServicios="SELECT sic.codigo as codigo,sic.codigo_excepcion as codigodetincluexclu,sic.codigo_servicio as codigo_servicio,getcodigopropservicio2(s.codigo,0)||' '||getnombreservicio(sic.codigo_servicio,0) as descripcion_servicio, sic.incluye as incluye,sic.cantidad as cantidad,'BD' as tiporegistro, to_char(fecha_vigencia,'dd/mm/yyyy') as fechavigencia from ser_incluexclu_econt sic inner join servicios s on (sic.codigo_servicio=s.codigo) ";
	
	/**
	 * 
	 */
	private static final String eliminarAgrupacionArticulos="DELETE FROM agru_art_incluexclu_econt WHERE codigo=?";
	
	/***
	 * 
	 */
	private static final String modificarAgrupacionArticulos="UPDATE agru_art_incluexclu_econt SET clase=?,grupo=?,subgrupo=?,naturaleza=?,incluye=?,cantidad=?,usuario_modifica=?,fecha_modifica=?,hora_modifica=?,fecha_vigencia=? WHERE codigo=?";

	/**
	 * 
	 */
	private static final String eliminarArticulos="DELETE FROM art_incluexclu_econt where codigo=?";

	/**
	 * 
	 */
	private static final String modificarArticulos="UPDATE art_incluexclu_econt SET codigo_articulo=?,incluye=?,cantidad=?,usuario_modifica=?,fecha_modifica=?,hora_modifica=?,fecha_vigencia=? WHERE codigo=?";

	/**
	 * 
	 */
	private static final String eliminarAgrupacionSevicios="DELETE FROM agru_ser_incluexclu_econt WHERE codigo=?";

	/***
	 * 
	 */
	private static final String modificarAgrupacionServicios="UPDATE agru_ser_incluexclu_econt SET pos=?,grupo_servicio=?,tipo_servicio=?,especialidad=?,incluye=?,cantidad=?,usuario_modifica=?,fecha_modifica=?,hora_modifica=?,fecha_vigencia=? WHERE codigo=?";

	/**
	 * 
	 */
	private static final String eliminarServicios="DELETE FROM ser_incluexclu_econt where codigo=?";

	/**
	 * 
	 */
	private static final String modificarServicios="UPDATE ser_incluexclu_econt set codigo_servicio=?,incluye=?,cantidad=?,usuario_modifica=?,fecha_modifica=?,hora_modifica=?,fecha_vigencia=? WHERE codigo=?";



	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static Vector<InfoDatosString> obtenerDetalleInclusionesExclusiones(Connection con, int institucion)
	{
		Vector<InfoDatosString> resultado=new Vector<InfoDatosString>();
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaDetIncluExclu));
			ps.setInt(1, institucion);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				resultado.add(new InfoDatosString(rs.getString(1),rs.getString(2)));
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
	 * @param institucion
	 * @param contrato 
	 * @return
	 */
	public static HashMap obtenerExcepciones(Connection con, int institucion, int contrato,boolean vigentes,boolean todas)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			
			String cadena=consultaExcepcionesContrato+" where institucion=? and codigo_contrato =? ";
			if(!todas)
			{
				if(vigentes)
					cadena=cadena+" and fecha_vigencia>=TO_DATE(TO_CHAR(CURRENT_DATE,'dd/mm/yyyy'),'dd/mm/yyyy')";
				else
					cadena=cadena+" and fecha_vigencia<TO_DATE(TO_CHAR(CURRENT_DATE,'dd/mm/yyyy'),'dd/mm/yyyy')";
			}
			cadena=cadena+" order by nomcentrocosto";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("cadena-->"+cadena+"\ninstitucion-->"+institucion+"\ncontrato-->"+contrato);
			ps.setInt(1, institucion);
			ps.setInt(2, contrato);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("error en obtenerListadoExcepciones");
			e.printStackTrace();
		}
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static HashMap obtenerInclusionesExclusiones(Connection con, int institucion,int contrato)
	{
		HashMap mapa = new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			//Modificado por la Tarea 46725 que indica por el campo que se debe realizar el ordenamiento
			//PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaInclusionesExclusion+" ORDER BY nomcentrocosto, prioridad",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaInclusionesExclusion+" ORDER BY prioridad",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, institucion);
			ps.setInt(2, contrato);
			logger.info("===>Inclusiones/Exclusiones: "+consultaInclusionesExclusion+" ORDER BY nomcentrocosto, prioridad ===>Contrato: "+contrato);
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("error en obtenerListadoCoberturas");
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
	public static boolean eliminareExepciones(Connection con, String codigo)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement("DELETE FROM agru_art_incluexclu_econt WHERE codigo_excepcion=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigo));
			ps.executeUpdate();
			
			ps= new PreparedStatementDecorator(con.prepareStatement("DELETE FROM art_incluexclu_econt where codigo_excepcion=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigo));
			ps.executeUpdate();
			
			ps= new PreparedStatementDecorator(con.prepareStatement("DELETE FROM agru_ser_incluexclu_econt WHERE codigo_excepcion=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigo));
			ps.executeUpdate();
			
			ps= new PreparedStatementDecorator(con.prepareStatement("DELETE FROM ser_incluexclu_econt where codigo_excepcion=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigo));
			ps.executeUpdate();
			
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaEliminarExcepcion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigo));
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
	public static HashMap consultaExcepcionLLave(Connection con, String codigo)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaExcepcionesContrato+" where codigo=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigo));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("error en obtenerListadoCoberturas");
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
	public static boolean insertarExcepcion(Connection con, HashMap vo, String cadena)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO excep_incluexclu_contr(
			 * codigo,
			 * codigo_contrato,
			 * institucion,
			 * codigo_centro_costo,
			 * usuario_modifica,
			 * fecha_modifica,
			 * hora_modifica,
			 * fecha_vigencia) 
			 * values('seq_excep_incluexclu_contr'),?,?,?,?,?,?,?)
			 */
			
			ps.setInt(1, Utilidades.convertirAEntero(vo.get("codigoContrato")+""));
			ps.setInt(2, Utilidades.convertirAEntero(vo.get("institucion")+""));
			if(UtilidadTexto.isEmpty(vo.get("centroCosto")+""))
				ps.setObject(3, null);
			else
				ps.setInt(3, Utilidades.convertirAEntero(vo.get("centroCosto")+""));
			ps.setString(4, vo.get("usuario")+"");
			ps.setDate(5, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			ps.setString(6, UtilidadFecha.getHoraActual());
			ps.setDate(7, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(vo.get("fechavigencia")+"")));
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
	public static boolean modificarExcepcion(Connection con, HashMap vo)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaModificarExcepcion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE excep_incluexclu_contr SET 
			 * codigo_centro_costo=?,
			 * usuario_modifica=?,
			 * fecha_modifica=?,
			 * hora_modifica=?,
			 * fecha_vigencia=? 
			 * where codigo=?
			 */
			
			if(UtilidadTexto.isEmpty(vo.get("centroCosto")+""))
				ps.setObject(1, null);
			else
				ps.setInt(1, Utilidades.convertirAEntero(vo.get("centroCosto")+""));
			ps.setString(2, vo.get("usuario")+"");
			ps.setDate(3, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			ps.setString(4, UtilidadFecha.getHoraActual());
			ps.setDate(5, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(vo.get("fechavigencia")+"")));
			ps.setDouble(6, Utilidades.convertirADouble(vo.get("codigo")+""));
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
	 * @param codigoIncluExclu
	 * @param contrato
	 * @param institucion
	 * @return
	 */
	public static boolean eliminarInclusionExclusion(Connection con, String codigoIncluExclu, int contrato, int institucion)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaEliminarInclusionExclusion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * DELETE FROM incluexclu_contrato where codigo_contrato=? and codigo_inclu_exclu_cc=? and institucion=?
			 */
			
			ps.setInt(1, contrato);
			ps.setDouble(2, Utilidades.convertirADouble(codigoIncluExclu));
			ps.setInt(3, institucion);
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
	public static boolean insertarInclusionExclusion(Connection con, HashMap vo)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(insertarInclusionExclusion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO incluexclu_contrato(
			 * codigo_contrato,
			 * codigo_inclu_exclu_cc,
			 * prioridad,
			 * institucion,
			 * usuario_modifica,
			 * fecha_modifica,
			 * hora_modifica,
			 * fecha_vigencia) values(?,?,?,?,?,?,?,?)
			 */
			
			ps.setInt(1, Utilidades.convertirAEntero(vo.get("codigoContrato")+""));
			ps.setDouble(2, Utilidades.convertirADouble(vo.get("codigoIncluExcluCC")+""));
			ps.setInt(3, Utilidades.convertirAEntero(vo.get("prioridad")+""));
			ps.setInt(4, Utilidades.convertirAEntero(vo.get("institucion")+""));
			ps.setString(5, vo.get("usuario")+"");
			ps.setDate(6, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			ps.setString(7, UtilidadFecha.getHoraActual());
			ps.setDate(8, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(vo.get("fechavigencia")+"")));
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
	public static HashMap consultarAgrupacionArticulos(Connection con, String codigo)
	{
		HashMap mapa=new HashMap();
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaAgrupacionArticulos+" where codigo_excepcion=? ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
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
			String cadena=consultaAgrupacionServicios+" where agsexcc.codigo_excepcion=? ";
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
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaArticulos+" where codigo_excepcion=? ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
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
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaServicios+" where codigo_excepcion=? ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
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
			 * UPDATE agru_art_incluexclu_econt SET 
			 * clase=?,
			 * grupo=?,
			 * subgrupo=?,
			 * naturaleza=?,
			 * incluye=?,
			 * cantidad=?,
			 * usuario_modifica=?,
			 * fecha_modifica=?,
			 * hora_modifica=?,
			 * fecha_vigencia=? 
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
			ps.setDate(10, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(vo.get("fechavigencia")+"")));
			ps.setDouble(11, Utilidades.convertirADouble(vo.get("codigo")+""));
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
			 * INSERT INTO agru_art_incluexclu_econt(
			 * codigo,
			 * codigo_excepcion,
			 * clase,
			 * grupo,
			 * subgrupo,
			 * naturaleza,
			 * institucion,
			 * incluye,
			 * cantidad,
			 * usuario_modifica,
			 * fecha_modifica,
			 * hora_modifica,
			 * fecha_vigencia) values('seq_agru_art_incluexclu_econt'),?,?,?,?,?,?,?,?,?,?,?,?)
			 */
			
			ps.setDouble(1, Utilidades.convertirADouble(vo.get("codigoExcepcion")+""));
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
			ps.setDate(12, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(vo.get("fechavigencia")+"")));
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
			 * UPDATE art_incluexclu_econt SET 
			 * codigo_articulo=?,
			 * incluye=?,
			 * cantidad=?,
			 * usuario_modifica=?,
			 * fecha_modifica=?,
			 * hora_modifica=?,
			 * fecha_vigencia=? 
			 * WHERE codigo=?
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
			ps.setDate(7, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(vo.get("fechavigencia")+"")));
			ps.setDouble(8, Utilidades.convertirADouble(vo.get("codigo")+""));
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
			 * INSERT INTO art_incluexclu_econt (
			 * codigo,
			 * codigo_excepcion,
			 * codigo_articulo,
			 * incluye,
			 * cantidad,
			 * usuario_modifica,
			 * fecha_modifica,
			 * hora_modifica,
			 * fecha_vigencia) 
			 * values ('seq_art_incluexclu_econt'),?,?,?,?,?,?,?,?)
			 */
			
			ps.setDouble(1, Utilidades.convertirADouble(vo.get("codigoExcepcion")+""));
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
			ps.setDate(8, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(vo.get("fechavigencia")+"")));
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
			 * INSERT INTO agru_ser_incluexclu_econt(
			 * codigo,
			 * codigo_excepcion,
			 * pos,
			 * grupo_servicio,
			 * tipo_servicio,
			 * especialidad,
			 * incluye,
			 * cantidad,
			 * usuario_modifica,
			 * fecha_modifica,
			 * hora_modifica,
			 * fecha_vigencia) 
			 * values ('seq_agru_ser_incluexclu_econt'),?,?,?,?,?,?,?,?,?,?,?)
			 */
			
			ps.setDouble(1, Utilidades.convertirADouble(vo.get("codigoExcepcion")+""));
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
			ps.setDate(11, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(vo.get("fechavigencia")+"")));
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
			 * UPDATE agru_ser_incluexclu_econt SET 
			 * pos=?,
			 * grupo_servicio=?,
			 * tipo_servicio=?,
			 * especialidad=?,
			 * incluye=?,
			 * cantidad=?,
			 * usuario_modifica=?,
			 * fecha_modifica=?,
			 * hora_modifica=?,
			 * fecha_vigencia=? 
			 * WHERE codigo=?
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
			ps.setDate(10, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(vo.get("fechavigencia")+"")));
			ps.setDouble(11, Utilidades.convertirADouble(vo.get("codigo")+""));
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
			 * INSERT INTO ser_incluexclu_econt(
			 * codigo,
			 * codigo_excepcion,
			 * codigo_servicio,
			 * incluye,
			 * cantidad,
			 * usuario_modifica,
			 * fecha_modifica,
			 * hora_modifica,
			 * fecha_vigencia) values ('seq_ser_incluexclu_econt'),?,?,?,?,?,?,?,?)
			 */
			
			ps.setDouble(1, Utilidades.convertirADouble(vo.get("codigoExcepcion")+""));
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
			ps.setDate(8, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(vo.get("fechavigencia")+"")));			
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
			 * UPDATE ser_incluexclu_econt set 
			 * codigo_servicio=?,
			 * incluye=?,
			 * cantidad=?,
			 * usuario_modifica=?,
			 * fecha_modifica=?,
			 * hora_modifica=?,
			 * fecha_vigencia=? 
			 * WHERE codigo=?
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
			ps.setDate(7, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(vo.get("fechavigencia")+"")));			
			ps.setDouble(8, Utilidades.convertirADouble(vo.get("codigo")+""));
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
	 * @param codigoIncluExclu
	 * @param contrato
	 * @param institucion
	 * @return
	 */
	public static HashMap consultarIncluExcluLLave(Connection con, String codigoIncluExclu, int contrato, int institucion) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaInclusionesExclusion+" and iec.codigo_inclu_exclu_cc="+codigoIncluExclu,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, institucion);
			ps.setInt(2, contrato);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("error en consultarIncluExcluLLave");
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
	public static boolean modificarIncluExclu(Connection con, HashMap vo) 
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(updateInclusionExclusion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE incluexclu_contrato SET 
			 * prioridad=?,
			 * usuario_modifica=?,
			 * fecha_modifica=?,
			 * hora_modifica=?,
			 * fecha_vigencia=? 
			 * where codigo_contrato=? 
			 * and codigo_inclu_exclu_cc=? 
			 * and institucion=?
			 */
			
			ps.setInt(1, Utilidades.convertirAEntero(vo.get("prioridad")+""));
			ps.setString(2, vo.get("usuario")+"");
			ps.setDate(3, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			ps.setString(4, UtilidadFecha.getHoraActual());
			ps.setDate(5, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(vo.get("fechavigencia")+"")));
			ps.setInt(6, Utilidades.convertirAEntero(vo.get("codigoContrato")+""));
			ps.setDouble(7, Utilidades.convertirADouble(vo.get("codigoIncluExcluCC")+""));
			ps.setInt(8, Utilidades.convertirAEntero(vo.get("institucion")+""));
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