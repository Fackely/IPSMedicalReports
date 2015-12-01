/*
 * Creado May 22, 2007
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * SqlBaseDescuentosComercialesDao
 * com.princetonsa.dao.sqlbase.facturacion
 * java version "1.5.0_07"
 */
package com.princetonsa.dao.sqlbase.facturacion;

import java.sql.Connection;
import java.sql.Date;

import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.facturacion.DtoConvenio;
import com.princetonsa.dto.facturacion.DtoProgDescComercialConvenioContrato;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

/**
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * May 22, 2007
 */
public class SqlBaseDescuentosComercialesDao
{
	/**
	 * 
	 */
	private static Logger logger=Logger.getLogger(SqlBaseDescuentosComercialesDao.class);
	
	/**
	 * 
	 */
	private static String cadenaConsultaViasDescuen="SELECT codigo as codigo, via_ingreso as viaingreso,getnombreviaingreso(via_ingreso) as nomviaingreso,'BD' as tiporegistro, tipo_paciente as tipopaciente, getnombretipopaciente(tipo_paciente) as nomtipopaciente,to_char(fecha_vigencia,'dd/mm/yyyy') as fechavigencia from desc_com_convcont ";
	
	/**
	 * 
	 */
	private static String cadenaEliminarViaDescuento="DELETE FROM desc_com_convcont WHERE codigo=?";
	
	/**
	 * 
	 */
	private static String cadenaModificarViaDescuento="UPDATE desc_com_convcont SET via_ingreso=?,fecha_vigencia=?,usuario_modifica=?,fecha_modifica=?,hora_modifica=?, tipo_paciente=? where codigo=?";
	
	/**
	 * 
	 */
	private static final String consultaAgrupacionArticulos="SELECT codigo as codigo,codigo_descuento as codigodescuento,clase as clase,getnomclaseinventario(clase) as nomclase,grupo as grupo,getnomgrupoinventario(grupo,clase) as nomgrupo,subgrupo as subgrupo,getnomsubgrupoinventario(subgrupo,grupo,clase) as nomsubgrupo,naturaleza as naturaleza,getnomnaturalezaarticulo(naturaleza) as nomnaturaleza, porcentaje as porcentaje,valor as valor,to_char(fecha_vigencia,'dd/mm/yyyy') as fechavigencia,'BD' as tiporegistro from agrup_art_desc_com_convcont ";
	
	/**
	 * 
	 */
	private static final String consultaArticulos="SELECT codigo as codigo,codigo_descuento as codigodescuento,codigo_articulo as codigo_articulo,getdescarticulo(codigo_articulo) as descripcion_articulo, porcentaje as porcentaje,valor as valor,to_char(fecha_vigencia,'dd/mm/yyyy') as fechavigencia,'BD' as tiporegistro from art_desc_com_convxcont ";
	
	/**
	 * 
	 */
	private static final String consultaAgrupacionServicios="SELECT agsexcc.codigo as codigo,agsexcc.codigo_descuento as codigodescuento,agsexcc.pos as tipopos, agsexcc.grupo_servicio as gruposervicio,gs.acronimo as acronimogruposervicio,gs.descripcion as descgruposervicio,agsexcc.tipo_servicio as tiposervicio,ts.nombre as nomtiposervicio, agsexcc.especialidad as especialidad,e.nombre as nomespecialidad, porcentaje as porcentaje,valor as valor,to_char(fecha_vigencia,'dd/mm/yyyy') as fechavigencia,'BD' as tiporegistro from  agru_ser_desc_com_convxcont agsexcc left outer join grupos_servicios gs on (gs.codigo=agsexcc.grupo_servicio) left outer join tipos_servicio ts on (ts.acronimo=agsexcc.tipo_servicio) left outer join especialidades e on (e.codigo=agsexcc.especialidad) ";
	
	/**
	 * 
	 */
	private static final String consultaServicios="SELECT codigo as codigo,codigo_descuento as codigodescuento,codigo_servicio as codigo_servicio,getcodigopropservicio2(codigo_servicio,0)||' '||getnombreservicio(codigo_servicio,0) as descripcion_servicio, porcentaje as porcentaje,valor as valor,to_char(fecha_vigencia,'dd/mm/yyyy') as fechavigencia,'BD' as tiporegistro from serv_desc_com_convxcont ";
	


	
	
	
	
	
	
	
	
	/**
	 * 
	 */
	private static final String eliminarAgrupacionArticulos="DELETE FROM agrup_art_desc_com_convcont WHERE codigo=?";
	
	/***
	 * 
	 */
	private static final String modificarAgrupacionArticulos="UPDATE agrup_art_desc_com_convcont SET fecha_vigencia=?,clase=?,grupo=?,subgrupo=?,naturaleza=?,porcentaje=?,valor=?,usuario_modifica=?,fecha_modifica=?,hora_modifica=? WHERE codigo=?";

	/**
	 * 
	 */
	private static final String eliminarArticulos="DELETE FROM art_desc_com_convxcont where codigo=?";

	/**
	 * 
	 */
	private static final String modificarArticulos="UPDATE art_desc_com_convxcont SET fecha_vigencia=?,codigo_articulo=?,porcentaje=?,valor=?,usuario_modifica=?,fecha_modifica=?,hora_modifica=? WHERE codigo=?";

	/**
	 * 
	 */
	private static final String eliminarAgrupacionSevicios="DELETE FROM agru_ser_desc_com_convxcont WHERE codigo=?";

	/***
	 * 
	 */
	private static final String modificarAgrupacionServicios="UPDATE agru_ser_desc_com_convxcont SET fecha_vigencia=?,pos=?,grupo_servicio=?,tipo_servicio=?,especialidad=?,porcentaje=?,valor=?,usuario_modifica=?,fecha_modifica=?,hora_modifica=? WHERE codigo=?";

	/**
	 * 
	 */
	private static final String eliminarServicios="DELETE FROM serv_desc_com_convxcont where codigo=?";

	/**
	 * 
	 */
	private static final String modificarServicios="UPDATE serv_desc_com_convxcont set fecha_vigencia=?,codigo_servicio=?,porcentaje=?,valor=?,usuario_modifica=?,fecha_modifica=?,hora_modifica=? WHERE codigo=?";

	
	
	
	private static final String insertarStrProgDescComConvxcont="insert into facturacion.prog_desc_com_convxcont("+
																							  				"	codigo_pk , "+
																											"	programa , "+
																											"	porcentaje ,"+ 
																											"	usuario_modifica ,"+
																											"	fecha_modifica ,"+
																											"	hora_modifica ,"+
																											"	fecha_vigencia,"+ 
																											"	codigo_descuento)" +
																											"	values" +
																											"	(?,?,?,?,?,?,?,?) ";




	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param contrato
	 * @param anteriores 
	 * @param vigentes 
	 * @return
	 */
	public static HashMap obtenerListadoViasDescuento(Connection con, int institucion, int contrato, boolean vigentes, boolean todas)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			String cadena=cadenaConsultaViasDescuen+"  where codigo_contrato=? and institucion=?";
			if(!todas)
			{
				if(vigentes)
					cadena=cadena+" and fecha_vigencia>='"+UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())+"'";
				else
					cadena=cadena+" and fecha_vigencia<'"+UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())+"'";
			}
			cadena=cadena+" order by fecha_vigencia,nomviaingreso";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con , cadena);
			ps.setInt(1, contrato);
			ps.setInt(2, institucion);
			logger.info("CONSULTA \n\n"+ps+"\n\n********************************************************");
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch(SQLException e)
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
	public static HashMap consultarViaDescuentoLLave(Connection con, String codigo)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaViasDescuen+"  where codigo=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigo));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch(SQLException e)
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
	public static boolean eliminarViaDescuento(Connection con, String codigo)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement("DELETE FROM agrup_art_desc_com_convcont WHERE codigo_descuento=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigo));
			ps.executeUpdate();
			
			ps= new PreparedStatementDecorator(con.prepareStatement("DELETE FROM art_desc_com_convxcont where codigo_descuento=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigo));
			ps.executeUpdate();
			
			ps= new PreparedStatementDecorator(con.prepareStatement("DELETE FROM agru_ser_desc_com_convxcont WHERE codigo_descuento=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigo));
			ps.executeUpdate();
			
			ps= new PreparedStatementDecorator(con.prepareStatement("DELETE FROM serv_desc_com_convxcont where codigo_descuento=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigo));
			ps.executeUpdate();
			
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaEliminarViaDescuento,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
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
	 * @param vo
	 * @param cadena 
	 * @return
	 */
	public static boolean insertarViaDescuento(Connection con, HashMap vo, String cadena)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("--->"+cadena);
			
			/**
			 * INSERT INTO desc_com_convcont 
			 * (codigo,
			 * codigo_contrato,
			 * institucion,
			 * via_ingreso,
			 * usuario_modifica,
			 * fecha_modifica,
			 * hora_modifica,
			 * tipo_paciente,
			 * fecha_vigencia) values('seq_desc_com_convcont'),?,?,?,?,?,?,?,?)"
			 */
			
			ps.setInt(1, Utilidades.convertirAEntero(vo.get("codigoContrato").toString()));
			ps.setInt(2, Utilidades.convertirAEntero(vo.get("institucion").toString()));
			if(vo.get("viaIngreso").toString().equals("null"))
				ps.setNull(3, Types.INTEGER);
			else
				ps.setInt(3, Utilidades.convertirAEntero(vo.get("viaIngreso").toString()));
			ps.setString(4, vo.get("usuario").toString());
			ps.setDate(5, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			ps.setString(6, UtilidadFecha.getHoraActual());
			if (vo.get("tipopaciente").toString().equals("null"))
				ps.setNull(7, Types.VARCHAR);
			else
				ps.setString(7, vo.get("tipopaciente").toString());
			if(UtilidadTexto.isEmpty(vo.get("fechavigencia")+""))
				ps.setObject(8, null);
			else
				ps.setDate(8, Date.valueOf(vo.get("fechavigencia").toString()));
			
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
	public static boolean modificarViaDescuento(Connection con, HashMap vo)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaModificarViaDescuento,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE desc_com_convcont SET 
			 * via_ingreso=?,
			 * fecha_vigencia=?,
			 * usuario_modifica=?,
			 * fecha_modifica=?,
			 * hora_modifica=?, 
			 * tipo_paciente=? 
			 * where codigo=?
			 */
			
			if(vo.get("viaIngreso").toString().equals("null"))
				ps.setNull(1, Types.INTEGER);
			else
				ps.setInt(1, Utilidades.convertirAEntero(vo.get("viaIngreso").toString()));
			if(UtilidadTexto.isEmpty(vo.get("fechavigencia")+""))
				ps.setObject(2, null);
			else
				ps.setDate(2, Date.valueOf(vo.get("fechavigencia").toString()));
			ps.setString(3, vo.get("usuario").toString());
			ps.setDate(4, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			ps.setString(5, UtilidadFecha.getHoraActual());
			if (vo.get("tipopaciente").toString().equals("null"))
				ps.setNull(6, Types.VARCHAR);
			else
				ps.setString(6, vo.get("tipopaciente").toString());	
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
			String cadena=consultaAgrupacionArticulos+" where codigo_descuento=? ";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigo));
			logger.info("cadena -->"+cadena+"  codigo -->"+codigo);
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
			String cadena=consultaAgrupacionServicios+" where agsexcc.codigo_descuento=? ";
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
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaArticulos+" where codigo_descuento=? ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
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
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaServicios+" where codigo_descuento=? ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
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
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaServicios+" where codigo=? ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
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
		boolean transaccion= false;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(eliminarAgrupacionArticulos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigoAgrupacion));
			if(ps.executeUpdate()>0)
				transaccion=true;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return transaccion;
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
			 * UPDATE agrup_art_desc_com_convcont SET 
			 * fecha_vigencia=?,
			 * clase=?,
			 * grupo=?,
			 * subgrupo=?,
			 * naturaleza=?,
			 * porcentaje=?,
			 * valor=?,
			 * usuario_modifica=?,
			 * fecha_modifica=?,
			 * hora_modifica=? 
			 * WHERE codigo=?
			 */
			
			if(UtilidadTexto.isEmpty(vo.get("fechavigencia").toString()))
				ps.setObject(1, null);
			else
				ps.setDate(1, Date.valueOf(vo.get("fechavigencia").toString()));
			if(UtilidadTexto.isEmpty(vo.get("clase")+""))
				ps.setObject(2, null);
			else
				ps.setInt(2, Utilidades.convertirAEntero(vo.get("clase").toString()));
			if(UtilidadTexto.isEmpty(vo.get("grupo")+""))
				ps.setObject(3, null);
			else
				ps.setInt(3, Utilidades.convertirAEntero(vo.get("grupo").toString()));
			if(UtilidadTexto.isEmpty(vo.get("subgrupo")+""))
				ps.setObject(4, null);
			else
				ps.setInt(4, Utilidades.convertirAEntero(vo.get("subgrupo").toString()));
			if(UtilidadTexto.isEmpty(vo.get("naturaleza")+""))
				ps.setObject(5, null);
			else
				ps.setString(5, vo.get("naturaleza").toString());

			if(UtilidadTexto.isEmpty(vo.get("porcentaje")+""))
				ps.setObject(6, null);
			else
				ps.setDouble(6,Double.parseDouble(vo.get("porcentaje").toString()));

			if(UtilidadTexto.isEmpty(vo.get("valor")+""))
				ps.setObject(7, null);
			else
				ps.setDouble(7,Double.parseDouble(vo.get("valor").toString()));

			ps.setString(8, vo.get("usuario").toString());
			ps.setDate(9, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			ps.setString(10, UtilidadFecha.getHoraActual());
			ps.setDouble(11, Utilidades.convertirADouble(vo.get("codigo").toString()));
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
			 * INSERT INTO agrup_art_desc_com_convcont
			 * (codigo,
			 * codigo_descuento,
			 * clase,
			 * grupo,
			 * subgrupo,
			 * naturaleza,
			 * institucion,
			 * porcentaje,
			 * valor,
			 * usuario_modifica,
			 * fecha_modifica,
			 * hora_modifica,
			 * fecha_vigencia) values(seq_desc_com_convcont'),?,?,?,?,?,?,?,?,?,?,?,?)
			 */
			
			ps.setDouble(1, Utilidades.convertirADouble(vo.get("codigoDescuento").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("clase")+""))
				ps.setObject(2, null);
			else
				ps.setInt(2, Utilidades.convertirAEntero(vo.get("clase").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("grupo")+""))
				ps.setObject(3, null);
			else
				ps.setInt(3, Utilidades.convertirAEntero(vo.get("grupo").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("subgrupo")+""))
				ps.setObject(4, null);
			else
				ps.setInt(4, Utilidades.convertirAEntero(vo.get("subgrupo").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("naturaleza")+""))
				ps.setObject(5, null);
			else
				ps.setString(5, vo.get("naturaleza").toString());
			
			ps.setInt(6, Utilidades.convertirAEntero(vo.get("institucion").toString()));
			
			
			if(UtilidadTexto.isEmpty(vo.get("porcentaje")+""))
				ps.setObject(7, null);
			else
				ps.setDouble(7,Double.parseDouble(vo.get("porcentaje").toString()));

			if(UtilidadTexto.isEmpty(vo.get("valor")+""))
				ps.setObject(8, null);
			else
				ps.setDouble(8,Double.parseDouble(vo.get("valor").toString()));
			
			ps.setString(9, vo.get("usuario").toString());
			
			ps.setDate(10, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			
			ps.setString(11, UtilidadFecha.getHoraActual());
			
			if(UtilidadTexto.isEmpty(vo.get("fechavigencia")+""))
				ps.setObject(12, null);
			else
				ps.setDate(12, Date.valueOf(vo.get("fechavigencia").toString()));
			
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
			 * UPDATE art_desc_com_convxcont SET 
			 * fecha_vigencia=?,codigo_articulo=?,porcentaje=?,valor=?,usuario_modifica=?,fecha_modifica=?,hora_modifica=? WHERE codigo=?
			 */
			
			if(UtilidadTexto.isEmpty(vo.get("fechavigencia")+""))
				ps.setObject(1, null);
			else
				ps.setDate(1, Date.valueOf(vo.get("fechavigencia").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("articulo")+""))
				ps.setObject(2, null);
			else
				ps.setInt(2, Utilidades.convertirAEntero(vo.get("articulo").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("porcentaje")+""))
				ps.setObject(3, null);
			else
				ps.setDouble(3,Double.parseDouble(vo.get("porcentaje").toString()));

			if(UtilidadTexto.isEmpty(vo.get("valor")+""))
				ps.setObject(4, null);
			else
				ps.setDouble(4,Double.parseDouble(vo.get("valor").toString()));
	

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
			 * INSERT INTO art_desc_com_convxcont 
			 * (codigo,
			 * codigo_descuento,
			 * codigo_articulo,
			 * porcentaje,
			 * valor,
			 * usuario_modifica,
			 * fecha_modifica,
			 * hora_modifica,
			 * fecha_vigencia) values ('seq_art_desc_com_convxcont'),?,?,?,?,?,?,?,?)
			 */
			
			ps.setDouble(1, Utilidades.convertirADouble(vo.get("codigoDescuento").toString()));
			if(UtilidadTexto.isEmpty(vo.get("articulo")+""))
				ps.setObject(2, null);
			else
				ps.setInt(2, Utilidades.convertirAEntero(vo.get("articulo").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("porcentaje")+""))
				ps.setObject(3, null);
			else
				ps.setDouble(3,Double.parseDouble(vo.get("porcentaje").toString()));

			if(UtilidadTexto.isEmpty(vo.get("valor")+""))
				ps.setObject(4, null);
			else
				ps.setDouble(4,Double.parseDouble(vo.get("valor").toString()));
			
			ps.setString(5, vo.get("usuario").toString());
			ps.setDate(6, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			ps.setString(7, UtilidadFecha.getHoraActual());
			if(UtilidadTexto.isEmpty(vo.get("fechavigencia")+""))
				ps.setObject(8, null);
			else
				ps.setDate(8, Date.valueOf(vo.get("fechavigencia").toString()));
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
			Utilidades.imprimirMapa(vo);
			
			/**
			 * INSERT INTO agru_ser_desc_com_convxcont
			 * (codigo,
			 * codigo_descuento,
			 * pos,
			 * grupo_servicio,
			 * tipo_servicio,
			 * especialidad,
			 * porcentaje,
			 * valor,
			 * usuario_modifica,
			 * fecha_modifica,
			 * hora_modifica,
			 * fecha_vigencia) values ('seq_agru_desc_com_convxcont'),?,?,?,?,?,?,?,?,?,?,?)
			 */
			
			ps.setDouble(1, Utilidades.convertirADouble(vo.get("codigoDescuento").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("tipopos")+""))
				ps.setObject(2, null);
			else
				ps.setString(2, vo.get("tipopos").toString());
			
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
			
			if(UtilidadTexto.isEmpty(vo.get("porcentaje")+""))
				ps.setObject(6, null);
			else
				ps.setDouble(6,Double.parseDouble(vo.get("porcentaje").toString()));

			if(UtilidadTexto.isEmpty(vo.get("valor")+""))
				ps.setObject(7, null);
			else
				ps.setDouble(7,Double.parseDouble(vo.get("valor").toString()));

			
			ps.setString(8, vo.get("usuario").toString());
			ps.setDate(9, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			ps.setString(10, UtilidadFecha.getHoraActual());
			if(UtilidadTexto.isEmpty(vo.get("fechavigencia")+""))
				ps.setObject(11, null);
			else
				ps.setDate(11, Date.valueOf(vo.get("fechavigencia").toString()));
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
			 * UPDATE agru_ser_desc_com_convxcont SET 
			 * fecha_vigencia=?,
			 * pos=?,
			 * grupo_servicio=?,
			 * tipo_servicio=?,
			 * especialidad=?,
			 * porcentaje=?,
			 * valor=?,
			 * usuario_modifica=?,
			 * fecha_modifica=?,
			 * hora_modifica=? 
			 * WHERE codigo=?
			 */
			
			if(UtilidadTexto.isEmpty(vo.get("fechavigencia")+""))
				ps.setObject(1, null);
			else
				ps.setDate(1, Date.valueOf(vo.get("fechavigencia").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("tipopos")+""))
				ps.setObject(2, null);
			else
				ps.setString(2, vo.get("tipopos").toString());
			
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

			if(UtilidadTexto.isEmpty(vo.get("porcentaje")+""))
				ps.setObject(6, null);
			else
				ps.setDouble(6,Double.parseDouble(vo.get("porcentaje").toString()));

			if(UtilidadTexto.isEmpty(vo.get("valor")+""))
				ps.setObject(7, null);
			else
				ps.setDouble(7,Double.parseDouble(vo.get("valor").toString()));

			ps.setString(8, vo.get("usuario").toString());
			
			ps.setDate(9, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			ps.setString(10, UtilidadFecha.getHoraActual());
			ps.setDouble(11, Utilidades.convertirADouble(vo.get("codigo").toString()));
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
			 * INSERT INTO serv_desc_com_convxcont
			 * (codigo,
			 * codigo_descuento,
			 * codigo_servicio,
			 * porcentaje,
			 * valor,
			 * usuario_modifica,
			 * fecha_modifica,
			 * hora_modifica,
			 * fecha_vigencia) values ('seq_serv_desc_com_convxcont'),?,?,?,?,?,?,?,?)
			 */
			
			ps.setDouble(1, Utilidades.convertirADouble(vo.get("codigoDescuento").toString()));
			if(UtilidadTexto.isEmpty(vo.get("servicio")+""))
				ps.setObject(2, null);
			else
				ps.setInt(2, Utilidades.convertirAEntero(vo.get("servicio").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("porcentaje")+""))
				ps.setObject(3, null);
			else
				ps.setDouble(3,Double.parseDouble(vo.get("porcentaje").toString()));

			if(UtilidadTexto.isEmpty(vo.get("valor")+""))
				ps.setObject(4, null);
			else
				ps.setDouble(4,Double.parseDouble(vo.get("valor").toString()));
			
			ps.setString(5, vo.get("usuario").toString());
			ps.setDate(6, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			ps.setString(7, UtilidadFecha.getHoraActual());
			if(UtilidadTexto.isEmpty(vo.get("fechavigencia")+""))
				ps.setObject(8, null);
			else
				ps.setDate(8, Date.valueOf(vo.get("fechavigencia").toString()));
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
			 * UPDATE serv_desc_com_convxcont set 
			 * fecha_vigencia=?,
			 * codigo_servicio=?,
			 * porcentaje=?,
			 * valor=?,
			 * usuario_modifica=?,
			 * fecha_modifica=?,
			 * hora_modifica=? 
			 * WHERE codigo=?
			 */
			
			if(UtilidadTexto.isEmpty(vo.get("fechavigencia")+""))
				ps.setObject(1, null);
			else
				ps.setDate(1, Date.valueOf(vo.get("fechavigencia").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("servicio")+""))
				ps.setObject(2, null);
			else
				ps.setInt(2, Utilidades.convertirAEntero(vo.get("servicio").toString()));

			if(UtilidadTexto.isEmpty(vo.get("porcentaje")+""))
				ps.setObject(3, null);
			else
				ps.setDouble(3,Double.parseDouble(vo.get("porcentaje").toString()));

			if(UtilidadTexto.isEmpty(vo.get("valor")+""))
				ps.setObject(4, null);
			else
				ps.setDouble(4,Double.parseDouble(vo.get("valor").toString()));

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
	 * @param dto
	 * @return
	 */
	
	public static boolean  insertProgDescComercialConvenioContrato(Connection con, DtoProgDescComercialConvenioContrato dto)
	{
		
		
	
		boolean transaccionExitosa=false;
		
		try 
		{	
			double secuencia=UtilidadBD.obtenerSiguienteValorSecuencia(con, "facturacion.seq_prog_desc_comconvxcont");
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, insertarStrProgDescComConvxcont);
			
			ps.setDouble(1,secuencia);
			if(dto.getDtoPrograma().getCodigo()>0.0)
				ps.setDouble(2, dto.getDtoPrograma().getCodigo());
			else
				ps.setObject(2, null);
			ps.setDouble(3, dto.getPorcentaje());
			ps.setString(4, dto.getUsuarioModifica().getUsuarioModifica());
			ps.setString(5, dto.getUsuarioModifica().getFechaModificaFromatoBD());
			ps.setString(6, dto.getUsuarioModifica().getHoraModifica());
			ps.setString(7, dto.getFechaVigenciaBd());
			ps.setBigDecimal(8, dto.getCodigoDescuento());
			
			
			if(ps.executeUpdate()>0)
			{
				return transaccionExitosa=true;
			}
			ps.close();
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN insertarPrograma==> "+e);
		}
		return transaccionExitosa;
	}
	
	
	
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static boolean  modificarProgDescComercialConvenioContrato(Connection  con, DtoProgDescComercialConvenioContrato dto)
	{
		boolean transaccionExitosa=false;
		String consultaStr=" update facturacion.prog_desc_com_convxcont set " +
																	" programa=?," +
																	" porcentaje=? ," +
																	" usuario_modifica=? ," +
																	" fecha_modifica=? , "+
																	" hora_modifica=? ,"+
																	" fecha_vigencia=?,"+ 
																	" codigo_descuento = ?" +
																	" where codigo_pk="+dto.getCodigoPk().doubleValue();
		
		try 
		{
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr);
			
			if(dto.getDtoPrograma().getCodigo()>0.0)
				ps.setDouble(1, dto.getDtoPrograma().getCodigo());
			else
				ps.setObject(1, null);
			ps.setDouble(2, dto.getPorcentaje());
			ps.setString(3, dto.getUsuarioModifica().getUsuarioModifica());
			ps.setString(4, dto.getUsuarioModifica().getFechaModificaFromatoBD());
			ps.setString(5, dto.getUsuarioModifica().getHoraModifica());
			ps.setString(6, dto.getFechaVigenciaBd());
			ps.setBigDecimal(7, dto.getCodigoDescuento());
			
			
			if(ps.executeUpdate()>0)
			{
				return transaccionExitosa=true;
			}
			ps.close();
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN MODIFICAR ==> "+e);
		}
		
		return transaccionExitosa;
	}
	
	
	
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static ArrayList<DtoProgDescComercialConvenioContrato> cargarProgDescComercialContrato(DtoProgDescComercialConvenioContrato dto)
	{
		logger.info("*******************************************************************************************************************");
		logger.info("*********************************** CARGAR PROGRAMAS DESCUENTO PROMOCIONES ****************************************");
		ArrayList<DtoProgDescComercialConvenioContrato> listDto = new ArrayList<DtoProgDescComercialConvenioContrato>();
		
		String consultaStr="select " +
									"pdc.codigo_pk  as codigoPk," +
									"pdc.programa as codigoPrograma," +
									"pdc.porcentaje as porcentaje, " +
									"pdc.usuario_modifica as usuarioModifica ," +
									"pdc.fecha_modifica  as fechaModifica, " +
									"pdc.hora_modifica as horaModifica, " +
									"pdc.fecha_vigencia as fechaVigencia, " +
									"pdc.codigo_descuento as codigoDescuento, "+ 
									"prog.nombre as nombrePrograma," +
									"prog.especialidad as especialidad, " +
									"prog.codigo_programa as codigoMostrarPrograma, " +
									"getnombreespecialidad(prog.especialidad) AS nombreEspecialidad  "+
									"from facturacion.prog_desc_com_convxcont  pdc left outer JOIN odontologia.programas prog ON(prog.codigo=pdc.programa)  where 1=1";
			consultaStr+=(dto.getCodigoDescuento().doubleValue()>0) ?" AND pdc.codigo_descuento="+dto.getCodigoDescuento():""; 
			Connection con = UtilidadBD.abrirConexion();
			
	
			 try 
			 {
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr);
				ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
				while(rs.next())
				{
					DtoProgDescComercialConvenioContrato newdto = new DtoProgDescComercialConvenioContrato();
					newdto.setCodigoPk(rs.getBigDecimal("codigoPk"));
					newdto.getDtoPrograma().setEspecialidad( rs.getInt("especialidad"));
					newdto.getDtoPrograma().setNombreEspecialidad(rs.getString("nombreEspecialidad"));
					newdto.getDtoPrograma().setNombre(rs.getString("nombrePrograma"));
					newdto.getDtoPrograma().setCodigo(rs.getDouble("codigoPrograma"));
					newdto.getDtoPrograma().setCodigoPrograma(rs.getString("codigoMostrarPrograma"));
					newdto.setPorcentaje(rs.getDouble("porcentaje"));
					newdto.getUsuarioModifica().setUsuarioModifica(rs.getString("usuarioModifica"));
					newdto.getUsuarioModifica().setFechaModifica(rs.getString("fechaModifica"));
					newdto.getUsuarioModifica().setHoraModifica(rs.getString("horaModifica"));	
					newdto.setFechaVigencia(UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fechaVigencia")));
					newdto.setCodigoDescuento(rs.getBigDecimal("codigoDescuento"));
					newdto.setVieneBaseDatos(ConstantesBD.acronimoSi);
					listDto.add(newdto);
				}
			
				
				logger.info("SQL INSERTAR .prog_desc_com_convxcont ");
				logger.info("\n\n\n\n\n");
				logger.info(ps);
				
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			}
			catch (SQLException e) 
				{
					logger.error("ERROR EN buscarProgramasXEspecialidad==> "+e);
				}
			
			
									
		return listDto;
	}
	
	
	
	
	/**
	 * CREO QUE ELIMINAR NO PERO SI QUIERE UNO RECURSO SI
	 * @param dto
	 * @return
	 */
	public static boolean eliminarProgDescComercialContrato(Connection con, DtoProgDescComercialConvenioContrato dto)
	{
		
		
		boolean transaccionExitosa=false;
		
		String eliminarStr="delete from  facturacion.prog_desc_com_convxcont where codigo_pk= ?";
		
		try 
		{
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, eliminarStr);
			ps.setBigDecimal(1,dto.getCodigoPk());
			if(ps.executeUpdate()>0)
			{
				return transaccionExitosa=true;
			}
			ps.close();
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN eliminarPrograma==> "+e);
		}
		
		return transaccionExitosa;
	}
	
	
	
	
	/**
	 * CODIGO CONVENIO
	 * @param dto
	 * @return
	 */
	public static int  validarTipoAtencionOdontologica(DtoConvenio dto ){
	
		logger.info("********************************************************************************************");
		logger.info("--------------------------- VALIDAR TIPO ATENCION DESCUENTO ODONTOLOGICO ");
		logger.info("********************************************************************************************");
		
		
		int codigo= ConstantesBD.codigoNuncaValido;
		/*
		String consultaStr="select" +
				" conv.codigo , conv.tipo_atencion from " +
				" facturacion.desc_com_convcont dcc INNER JOIN facturacion.contratos cont " +
					"ON (dcc.codigo_contrato=cont.codigo) " +
				"INNER JOIN  facturacion.convenios  conv ON (conv.codigo=cont.convenio) where dcc.codigo="+dto.getCodigoPk().doubleValue()+" AND "+ 
				"conv.tipo_atencion='"+ConstantesIntegridadDominio.acronimoTipoAtencionConvenioOdontologico+"'";
		*/
		
		String consultaStr="select conv.codigo as codigo  from facturacion.convenios conv where conv.codigo="+dto.getCodigo()+ "" +
							" and  conv.tipo_atencion='"+dto.getTipoAtencion()+"'";
		
		logger.info(consultaStr);
		logger.info("***************************************************************************************");
		
		Connection con = UtilidadBD.abrirConexion();
		
		 try 
		 {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
			 codigo=rs.getInt("codigo");	
			}
			
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
			{
				logger.error("ERROR EN BUSCAR CONVENIOS==> "+e);
			}
		
		return codigo;
	} 
	
	
	
	

}
