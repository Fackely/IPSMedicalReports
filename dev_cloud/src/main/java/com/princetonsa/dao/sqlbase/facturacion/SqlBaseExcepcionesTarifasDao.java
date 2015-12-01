/**
 * 
 */
package com.princetonsa.dao.sqlbase.facturacion;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

/**
 * @author axioma
 *
 */
public class SqlBaseExcepcionesTarifasDao 
{
	
	/**
	 * 
	 */
	public static Logger logger=Logger.getLogger(SqlBaseExcepcionesTarifasDao.class);
	
	/**
	 * 
	 */
	public static String cadenaInsertarAgrupacionArticulosStr="INSERT INTO agru_art_exce_tari_cont(codigo,codigo_excepcion,clase,grupo,subgrupo,naturaleza,institucion,valor_ajuste,base_excepcion,nueva_tarifa,usuario_modifica,fecha_modifica,hora_modifica,fecha_vigencia,valor_base) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * 
	 */
	public static String cadenaInsertarExcepArticulosStr="INSERT INTO art_exce_tari_cont (codigo,codigo_excepcion,codigo_articulo,valor_ajuste,base_excepcion,nueva_tarifa,usuario_modifica,fecha_modifica,hora_modifica,fecha_vigencia,valor_base) values (?,?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * 
	 */
	public static String cadenaInsertarAgrupacionServiciosStr="INSERT INTO agru_ser_exce_tari_cont(codigo,codigo_excepcion,pos,grupo_servicio,tipo_servicio,especialidad,valor_ajuste,nueva_tarifa,usuario_modifica,fecha_modifica,hora_modifica,fecha_vigencia) values (?,?,?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * 
	 */
	public static String cadenaInsertarExcepServiciosStr="INSERT INTO ser_exce_tari_cont(codigo,codigo_excepcion,codigo_servicio,valor_ajuste,nueva_tarifa,usuario_modifica,fecha_modifica,hora_modifica,fecha_vigencia) values (?,?,?,?,?,?,?,?,?)";
	
	/**
	 * 
	 */
	public static String consultaExcepcionesContrato="SELECT et.codigo as codigo,(select count(1) from agru_art_exce_tari_cont where codigo_excepcion=et.codigo)+(select count(1) from agru_ser_exce_tari_cont where codigo_excepcion=et.codigo)+(select count(1) from art_exce_tari_cont where codigo_excepcion=et.codigo)+(select count(1) from ser_exce_tari_cont where codigo_excepcion=et.codigo) as numdetalles,et.via_ingreso as viaingreso,to_char(et.fecha_vigencia,'dd/mm/yyyy') as fechavigencia, et.observaciones as observaciones, getnombreviaingreso(et.via_ingreso) as nomviaingreso,coalesce(et.tipo_paciente,'') as tipopaciente,coalesce(getNombreTipoPaciente(et.tipo_paciente),'') as nomtipopaciente,et.tipo_complejidad as tipocomplejidad,tc.descripcion as nomtipocomplejidad, 'BD' as tiporegistro, coalesce(et.centro_atencion, "+ConstantesBD.codigoNuncaValido+") as codcentroatencion, case when et.centro_atencion is null then 'Todos' else getnomcentroatencion(et.centro_atencion) end as nomcentroatencion from excep_tarifas_contrato et left outer join tipos_complejidad tc on(et.tipo_complejidad=tc.codigo) ";

	/**
	 * 
	 */
	public static String cadenaModificarExcepcion="UPDATE excep_tarifas_contrato SET via_ingreso=?,tipo_paciente=?,tipo_complejidad=?,usuario_modifica=?,fecha_modifica=?,hora_modifica=?,fecha_vigencia=?,observaciones=?, centro_atencion=? where codigo=?";
	
	/**
	 * 
	 */
	public static String cadenaEliminarExcepcion="DELETE FROM excep_tarifas_contrato where codigo=?";
	
	/**
	 * 
	 */
	private static final String consultaAgrupacionArticulos="SELECT codigo as codigo,codigo_excepcion as codigoexcepcion,clase as clase,getnomclaseinventario(clase) as nomclase,grupo as grupo,getnomgrupoinventario(grupo,clase) as nomgrupo,subgrupo as subgrupo,getnomsubgrupoinventario(subgrupo,grupo,clase) as nomsubgrupo,naturaleza as naturaleza,getnomnaturalezaarticulo(naturaleza) as nomnaturaleza,abs(valor_ajuste) as valorajuste,case when valor_ajuste is null then '' when valor_ajuste > 0 then 'S' else 'N' end as suma,base_excepcion as baseexcepcion,abs(nueva_tarifa) as nuevatarifa,'BD' as tiporegistro, to_char(fecha_vigencia,'dd/mm/yyyy') as fechavigencia,valor_base as valorbase from agru_art_exce_tari_cont ";
	
	/**
	 * 
	 */
	private static final String consultaArticulos="SELECT codigo as codigo,codigo_excepcion as codigoexcepcion,codigo_articulo as codigo_articulo,getdescarticulo(codigo_articulo) as descripcion_articulo, abs(valor_ajuste) as valorajuste,case when valor_ajuste is null then '' when valor_ajuste > 0 then 'S' else 'N' end as suma,base_excepcion as baseexcepcion,nueva_tarifa as nuevatarifa,'BD' as tiporegistro, to_char(fecha_vigencia,'dd/mm/yyyy') as fechavigencia,valor_base as valorbase from art_exce_tari_cont ";
	
	/**
	 * 
	 */
	private static final String consultaAgrupacionServicios="SELECT agsexcc.codigo as codigo,agsexcc.codigo_excepcion as codigoexcepcion,agsexcc.pos as tipopos, agsexcc.grupo_servicio as gruposervicio,gs.acronimo as acronimogruposervicio,gs.descripcion as descgruposervicio,agsexcc.tipo_servicio as tiposervicio,ts.nombre as nomtiposervicio, agsexcc.especialidad as especialidad,e.nombre as nomespecialidad, abs(valor_ajuste) as valorajuste,case when valor_ajuste is null then '' when valor_ajuste > 0 then 'S' else 'N' end as suma,nueva_tarifa as nuevatarifa ,'BD' as tiporegistro, to_char(fecha_vigencia,'dd/mm/yyyy') as fechavigencia from  agru_ser_exce_tari_cont agsexcc left outer join grupos_servicios gs on (gs.codigo=agsexcc.grupo_servicio) left outer join tipos_servicio ts on (ts.acronimo=agsexcc.tipo_servicio) left outer join especialidades e on (e.codigo=agsexcc.especialidad) ";
	
	/**
	 * 
	 */
	private static final String consultaServicios="SELECT s.codigo as codigo,s.codigo_excepcion as codigoexcepcion,s.codigo_servicio as codigo_servicio,getnombreservicio(s.codigo_servicio,0) as descripcion_servicio, abs(s.valor_ajuste) as valorajuste,case when s.valor_ajuste is null then '' when s.valor_ajuste > 0 then 'S' else 'N' end as suma,s.nueva_tarifa as nuevatarifa,'BD' as tiporegistro, to_char(s.fecha_vigencia,'dd/mm/yyyy') as fechavigencia, rs.codigo_propietario as codigocups from ser_exce_tari_cont s inner join referencias_servicio rs ON(s.codigo_servicio=rs.servicio and rs.tipo_tarifario=0)";
	
	/**
	 * 
	 */
	private static final String eliminarAgrupacionArticulos="DELETE FROM agru_art_exce_tari_cont WHERE codigo=?";
	
	/***
	 * 
	 */
	private static final String modificarAgrupacionArticulos="UPDATE agru_art_exce_tari_cont SET clase=?,grupo=?,subgrupo=?,naturaleza=?,valor_ajuste=?,base_excepcion=?,nueva_tarifa=?,usuario_modifica=?,fecha_modifica=?,hora_modifica=?,fecha_vigencia=?,valor_base=? WHERE codigo=?";

	/**
	 * 
	 */
	private static final String eliminarArticulos="DELETE FROM art_exce_tari_cont where codigo=?";

	/**
	 * 
	 */
	private static final String modificarArticulos="UPDATE art_exce_tari_cont SET codigo_articulo=?,valor_ajuste=?,base_excepcion=?,nueva_tarifa=?,usuario_modifica=?,fecha_modifica=?,hora_modifica=?,fecha_vigencia=?,valor_base=? WHERE codigo=?";

	/**
	 * 
	 */
	private static final String eliminarAgrupacionSevicios="DELETE FROM agru_ser_exce_tari_cont WHERE codigo=?";

	/***
	 * 
	 */
	private static final String modificarAgrupacionServicios="UPDATE agru_ser_exce_tari_cont SET pos=?,grupo_servicio=?,tipo_servicio=?,especialidad=?,valor_ajuste=?,nueva_tarifa=?,usuario_modifica=?,fecha_modifica=?,hora_modifica=?,fecha_vigencia=? WHERE codigo=?";

	/**
	 * 
	 */
	private static final String eliminarServicios="DELETE FROM ser_exce_tari_cont where codigo=?";

	/**
	 * 
	 */
	private static final String modificarServicios="UPDATE ser_exce_tari_cont set codigo_servicio=?,valor_ajuste=?,nueva_tarifa=?,usuario_modifica=?,fecha_modifica=?,hora_modifica=?,fecha_vigencia=? WHERE codigo=?";
	
	/**
	 * 
	 */
	private static final int codigoSeccionAgrupacionArticulos=0;

	/**
	 * 
	 */
	private static final int codigoSeccionArticulos=1;

	/**
	 * 
	 */
	private static final int codigoSeccionAgrupacionServicios=2;

	/**
	 * 
	 */
	private static final int codigoSeccionServicios=3;


	/**
	 * 
	 */
	private static final String cadenaConsultaPorcentajeExcepcionAA="SELECT codigo,abs(porcentaje_excepcion) as porcentaje,agru_art_exce_tari_cont as codigoexcepcion,case when porcentaje_excepcion is null then '' when porcentaje_excepcion>=0 then 'S' else 'N' end as suma,'BD' as tiporegistro,prioridad as prioridad  from porcentaje_agru_art_exce ";
	
	/**
	 * 
	 */
	private static final String cadenaConsultaPorcentajeExcepcionA="SELECT codigo,abs(porcentaje_excepcion) as porcentaje,art_exce_tari_cont as codigoexcepcion,case when porcentaje_excepcion is null then '' when porcentaje_excepcion>=0 then 'S' else 'N' end as suma,'BD' as tiporegistro,prioridad as prioridad  from porcentaje_art_exce ";


	/**
	 * 
	 */
	private static final String cadenaConsultaPorcentajeExcepcionAS="SELECT codigo,abs(porcentaje_excepcion) as porcentaje,agru_ser_exce_tari_cont as codigoexcepcion,case when porcentaje_excepcion is null then '' when porcentaje_excepcion>=0 then 'S' else 'N' end as suma,'BD' as tiporegistro,prioridad as prioridad  from porcentaje_agru_ser_exce ";
	
	/**
	 * 
	 */
	private static final String cadenaConsultaPorcentajeExcepcionS="SELECT codigo,abs(porcentaje_excepcion) as porcentaje,ser_exce_tari_cont as codigoexcepcion,case when porcentaje_excepcion is null then '' when porcentaje_excepcion>=0 then 'S' else 'N' end as suma,'BD' as tiporegistro,prioridad as prioridad from porcentaje_ser_exce ";
	

	/**
	 * 
	 */
	private static final String cadenaEliminarPorcentajeExcepcionAA="DELETE from porcentaje_agru_art_exce where agru_art_exce_tari_cont=?";
	
	/**
	 * 
	 */
	private static final String cadenaEliminarPorcentajeExcepcionA="DELETE from porcentaje_art_exce where art_exce_tari_cont=?";


	/**
	 * 
	 */
	private static final String cadenaEliminarPorcentajeExcepcionAS="DELETE from porcentaje_agru_ser_exce where agru_ser_exce_tari_cont=?";
	
	/**
	 * 
	 */
	private static final String cadenaEliminarPorcentajeExcepcionS="DELETE from porcentaje_ser_exce where ser_exce_tari_cont=?";

	/**
	 * 
	 */
	private static final String cadenaInsertarPorcentajeExcepcionAA="INSERT INTO porcentaje_agru_art_exce(codigo,agru_art_exce_tari_cont,porcentaje_excepcion,prioridad,usuario_modifica,fecha_modifica,hora_modifica) values (?,?,?,?,?,?,?)";
	
	/**
	 * 
	 */
	private static final String cadenaInsertarPorcentajeExcepcionA="INSERT INTO porcentaje_art_exce(codigo,art_exce_tari_cont,porcentaje_excepcion,prioridad,usuario_modifica,fecha_modifica,hora_modifica) values (?,?,?,?,?,?,?)";


	/**
	 * 
	 */
	private static final String cadenaInsertarPorcentajeExcepcionAS="INSERT INTO porcentaje_agru_ser_exce(codigo,agru_ser_exce_tari_cont,porcentaje_excepcion,prioridad,usuario_modifica,fecha_modifica,hora_modifica) values (?,?,?,?,?,?,?)";
	
	/**
	 * 
	 */
	private static final String cadenaInsertarPorcentajeExcepcionS="INSERT INTO porcentaje_ser_exce(codigo,ser_exce_tari_cont,porcentaje_excepcion,prioridad,usuario_modifica,fecha_modifica,hora_modifica) values (?,?,?,?,?,?,?)";


	/**
	 * 
	 */
	private static final String cadenaUpdatePorcentajeExcepcionAA="UPDATE porcentaje_agru_art_exce set porcentaje_excepcion=?,prioridad=?,usuario_modifica=?,fecha_modifica=?,hora_modifica=? where codigo=?";
	
	/**
	 * 
	 */
	private static final String cadenaUpdatePorcentajeExcepcionA="UPDATE porcentaje_art_exce set porcentaje_excepcion=?,prioridad=?,usuario_modifica=?,fecha_modifica=?,hora_modifica=? where codigo=?";


	/**
	 * 
	 */
	private static final String cadenaUpdatePorcentajeExcepcionAS="UPDATE porcentaje_agru_ser_exce set porcentaje_excepcion=?,prioridad=?,usuario_modifica=?,fecha_modifica=?,hora_modifica=? where codigo=?";
	
	/**
	 * 
	 */
	private static final String cadenaUpdatePorcentajeExcepcionS="UPDATE porcentaje_ser_exce set porcentaje_excepcion=?,prioridad=?,usuario_modifica=?,fecha_modifica=?,hora_modifica=? where codigo=?";

	/**
	 * 
	 */
	private static final String cadenaDeletePorcentajeExcepcionAA="DELETE FROM porcentaje_agru_art_exce where codigo=?";
	
	/**
	 * 
	 */
	private static final String cadenaDeletePorcentajeExcepcionA="DELETE FROM porcentaje_art_exce where codigo=?";


	/**
	 * 
	 */
	private static final String cadenaDeletePorcentajeExcepcionAS="DELETE FROM porcentaje_agru_ser_exce where codigo=?";
	
	/**
	 * 
	 */
	private static final String cadenaDeletePorcentajeExcepcionS="DELETE FROM porcentaje_ser_exce where codigo=?";

	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param contrato 
	 * @param todas 
	 * @param vigentes 
	 * @return
	 */
	public static HashMap obtenerExcepciones(Connection con, int institucion, int contrato, boolean vigentes, boolean todas)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			String cadena=consultaExcepcionesContrato+" where et.institucion=? and et.codigo_contrato =? ";
			if(!todas)
			{
				if(vigentes)
					cadena=cadena+" and fecha_vigencia>='"+UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())+"'";
				else
					cadena=cadena+" and fecha_vigencia<'"+UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())+"'";
			}
			cadena=cadena+" order by via_ingreso desc, tipo_paciente desc, fecha_vigencia desc ";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			
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
	 * @param codigo
	 * @return
	 */
	public static boolean eliminareExepciones(Connection con, String codigo)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement("DELETE FROM agru_art_exce_tari_cont WHERE codigo_excepcion=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigo));
			ps.executeUpdate();
			
			ps= new PreparedStatementDecorator(con.prepareStatement("DELETE FROM art_exce_tari_cont where codigo_excepcion=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigo));
			ps.executeUpdate();
			
			ps= new PreparedStatementDecorator(con.prepareStatement("DELETE FROM agru_ser_exce_tari_cont WHERE codigo_excepcion=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigo));
			ps.executeUpdate();
			
			ps= new PreparedStatementDecorator(con.prepareStatement("DELETE FROM ser_exce_tari_cont where codigo_excepcion=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
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
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaExcepcionesContrato+" where et.codigo=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
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
			
			ps.setInt(1, Utilidades.convertirAEntero(vo.get("codigoContrato")+""));
			ps.setInt(2, Utilidades.convertirAEntero(vo.get("institucion")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("viaIngreso")+""))
				ps.setObject(3, null);
			else
				ps.setInt(3, Utilidades.convertirAEntero(vo.get("viaIngreso")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("tipoPaciente")+""))
				ps.setNull(4, Types.VARCHAR);
			else
				ps.setString(4, vo.get("tipoPaciente")+"");
			
			if(UtilidadTexto.isEmpty(vo.get("tipoComplejidad")+""))
				ps.setObject(5, null);
			else
				ps.setInt(5, Utilidades.convertirAEntero(vo.get("tipoComplejidad")+""));
			
			ps.setString(6, vo.get("usuario")+"");
			ps.setDate(7, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			ps.setString(8, UtilidadFecha.getHoraActual());
			
			if(UtilidadTexto.isEmpty(vo.get("fechavigencia")+""))
				ps.setObject(9, null);
			else
				ps.setDate(9, Date.valueOf(vo.get("fechavigencia")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("observaciones")+""))
				ps.setNull(10, Types.VARCHAR);
			else
				ps.setString(10, vo.get("observaciones")+"");
			
			
			if(Utilidades.convertirAEntero(vo.get("codcentroatencion")+"")>0)
			{
				ps.setInt(11, Utilidades.convertirAEntero(vo.get("codcentroatencion")+""));
			}
			else
			{
				ps.setNull(11, Types.INTEGER);
			}
			
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
			
			if(UtilidadTexto.isEmpty(vo.get("viaIngreso")+""))
				ps.setObject(1, null);
			else
				ps.setInt(1, Utilidades.convertirAEntero(vo.get("viaIngreso")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("tipoPaciente")+""))
				ps.setNull(2, Types.VARCHAR);
			else
				ps.setString(2, vo.get("tipoPaciente")+"");
			
			if(UtilidadTexto.isEmpty(vo.get("tipoComplejidad")+""))
				ps.setObject(3, null);
			else
				ps.setInt(3, Utilidades.convertirAEntero(vo.get("tipoComplejidad")+""));
			
			ps.setString(4, vo.get("usuario")+"");
			
			ps.setDate(5, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			
			ps.setString(6, UtilidadFecha.getHoraActual());
			
			if(UtilidadTexto.isEmpty(vo.get("fechavigencia")+""))
				ps.setObject(7, null);
			else
				ps.setDate(7, Date.valueOf(vo.get("fechavigencia")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("observaciones")+""))
				ps.setNull(8, Types.VARCHAR);
			else
				ps.setString(8, vo.get("observaciones")+"");
			
			if(Utilidades.convertirAEntero(vo.get("codcentroatencion")+"")<=0)
			{
				ps.setNull(9, Types.NULL);
			}
			else
			{
				ps.setInt(9, Utilidades.convertirAEntero(vo.get("codcentroatencion")+""));
			}
			
			ps.setDouble(10, Utilidades.convertirADouble(vo.get("codigo")+""));
			ps.executeUpdate();
			ps.close();
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
		try
		{
			PreparedStatementDecorator ps=null;
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaEliminarPorcentajeExcepcionAA,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigoAgrupacion));
			ps.executeUpdate();
			
			ps= new PreparedStatementDecorator(con.prepareStatement(eliminarAgrupacionArticulos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
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
		String signo=(vo.get("suma")+"").trim().equals("S")?"":"-";
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarAgrupacionArticulos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE agru_art_exce_tari_cont SET 
			 * clase=?,
			 * grupo=?,
			 * subgrupo=?,
			 * naturaleza=?,
			 * valor_ajuste=?,
			 * base_excepcion=?,
			 * nueva_tarifa=?,
			 * usuario_modifica=?,
			 * fecha_modifica=?,
			 * hora_modifica=?,
			 * fecha_vigencia=?,
			 * valor_base=? 
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

			if(UtilidadTexto.isEmpty(vo.get("valorAjuste")+""))
				ps.setObject(5, null);
			else
				ps.setDouble(5,Utilidades.convertirADouble(signo+vo.get("valorAjuste")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("baseExcepcion")+""))
				ps.setObject(6, null);
			else
				ps.setString(6,vo.get("baseExcepcion")+"");

			if(UtilidadTexto.isEmpty(vo.get("nuevaTarifa")+""))
				ps.setObject(7, null);
			else
				ps.setDouble(7,Utilidades.convertirADouble(vo.get("nuevaTarifa")+""));

			ps.setString(8, vo.get("usuario")+"");
			ps.setDate(9, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			ps.setString(10, UtilidadFecha.getHoraActual());
			
			if(UtilidadTexto.isEmpty(vo.get("fechavigencia")+""))
				ps.setObject(11, null);
			else
				ps.setDate(11, Date.valueOf(vo.get("fechavigencia")+""));

			if(UtilidadTexto.isEmpty(vo.get("valorBase")+""))
				ps.setObject(12, null);
			else
				ps.setDouble(12,Utilidades.convertirADouble(vo.get("valorBase")+""));
			
			ps.setDouble(13, Utilidades.convertirADouble(vo.get("codigo")+""));
			
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
	public static int insertarAgrupacionArticulos(Connection con, HashMap vo)
	{
		String signo=(vo.get("suma")+"").trim().equals("S")?"":"-";
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarAgrupacionArticulosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO agru_art_exce_tari_cont(
			 * codigo,
			 * codigo_excepcion,
			 * clase,
			 * grupo,
			 * subgrupo,
			 * naturaleza,
			 * institucion,
			 * valor_ajuste,
			 * base_excepcion,
			 * nueva_tarifa,
			 * usuario_modifica,
			 * fecha_modifica,
			 * hora_modifica,
			 * fecha_vigencia,
			 * valor_base) values('seq_agru_art_exce_tari_cont'),?,?,?,?,?,?,?,?,?,?,?,?,?,?)
			 */
			
			int codigoAgrupacionArticulo= UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_agru_art_exce_tari_cont");
			
			ps.setDouble(1, codigoAgrupacionArticulo);
			
			ps.setDouble(2, Utilidades.convertirADouble(vo.get("codigoExcepcion")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("clase")+""))
				ps.setObject(3, null);
			else
				ps.setInt(3, Utilidades.convertirAEntero(vo.get("clase")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("grupo")+""))
				ps.setObject(4, null);
			else
				ps.setInt(4, Utilidades.convertirAEntero(vo.get("grupo")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("subgrupo")+""))
				ps.setObject(5, null);
			else
				ps.setInt(5, Utilidades.convertirAEntero(vo.get("subgrupo")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("naturaleza")+""))
				ps.setObject(6, null);
			else
				ps.setString(6, vo.get("naturaleza")+"");
			
			ps.setInt(7, Utilidades.convertirAEntero(vo.get("institucion")+""));
			

			if(UtilidadTexto.isEmpty(vo.get("valorAjuste")+""))
				ps.setObject(8, null);
			else
				ps.setDouble(8,Utilidades.convertirADouble(signo+vo.get("valorAjuste")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("baseExcepcion")+""))
				ps.setObject(9, null);
			else
				ps.setString(9,vo.get("baseExcepcion")+"");

			if(UtilidadTexto.isEmpty(vo.get("nuevaTarifa")+""))
				ps.setObject(10, null);
			else
				ps.setDouble(10,Utilidades.convertirADouble(vo.get("nuevaTarifa")+""));
			
			ps.setString(11, vo.get("usuario")+"");
			ps.setDate(12, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			ps.setString(13, UtilidadFecha.getHoraActual());
			
			if(UtilidadTexto.isEmpty(vo.get("fechavigencia")+""))
				ps.setObject(14, null);
			else
				ps.setDate(14, Date.valueOf(vo.get("fechavigencia")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("valorBase")+""))
				ps.setObject(15, null);
			else
				ps.setDouble(15,Utilidades.convertirADouble(vo.get("valorBase")+""));
			ps.executeUpdate();
			return codigoAgrupacionArticulo;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return ConstantesBD.codigoNuncaValido;
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
			PreparedStatementDecorator ps=null;
			
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaEliminarPorcentajeExcepcionA,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigoComponente));
			ps.executeUpdate();
			
			
			ps= new PreparedStatementDecorator(con.prepareStatement(eliminarArticulos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
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
		String signo=(vo.get("suma")+"").trim().equals("S")?"":"-";
		try
		{	
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarArticulos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE art_exce_tari_cont SET 
			 * codigo_articulo=?,
			 * valor_ajuste=?,
			 * base_excepcion=?,
			 * nueva_tarifa=?,
			 * usuario_modifica=?,
			 * fecha_modifica=?,
			 * hora_modifica=?,
			 * fecha_vigencia=?,
			 * valor_base=? 
			 * WHERE codigo=?
			 */
			
			if(UtilidadTexto.isEmpty(vo.get("articulo")+""))
				ps.setObject(1, null);
			else
				ps.setInt(1, Utilidades.convertirAEntero(vo.get("articulo")+""));
			

			if(UtilidadTexto.isEmpty(vo.get("valorAjuste")+""))
				ps.setObject(2, null);
			else
				ps.setDouble(2,Utilidades.convertirADouble((signo+vo.get("valorAjuste")+"")));
			
			if(UtilidadTexto.isEmpty(vo.get("baseExcepcion")+""))
				ps.setObject(3, null);
			else
				ps.setString(3,vo.get("baseExcepcion")+"");

			if(UtilidadTexto.isEmpty(vo.get("nuevaTarifa")+""))
				ps.setObject(4, null);
			else
				ps.setDouble(4,Utilidades.convertirADouble(vo.get("nuevaTarifa")+""));
	
			ps.setString(5, vo.get("usuario")+"");
			ps.setDate(6, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			ps.setString(7, UtilidadFecha.getHoraActual());
			
			if(UtilidadTexto.isEmpty(vo.get("fechavigencia")+""))
				ps.setObject(8, null);
			else
				ps.setDate(8, Date.valueOf(vo.get("fechavigencia")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("valorBase")+""))
				ps.setObject(9, null);
			else
				ps.setDouble(9,Utilidades.convertirADouble(vo.get("valorBase")+""));
			
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
	public static int insertarArticulos(Connection con, HashMap vo)
	{
		String signo=(vo.get("suma")+"").trim().equals("S")?"":"-";
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarExcepArticulosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO art_exce_tari_cont 
			 * (codigo,
			 * codigo_excepcion,
			 * codigo_articulo,
			 * valor_ajuste,
			 * base_excepcion,
			 * nueva_tarifa,
			 * usuario_modifica,
			 * fecha_modifica,
			 * hora_modifica,
			 * fecha_vigencia,
			 * valor_base) 
			 * values ('seq_art_exce_tari_cont'),?,?,?,?,?,?,?,?,?,?)
			 */
			
			int codigoArticulo = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_art_exce_tari_cont");
			
			ps.setDouble(1, codigoArticulo);
			
			ps.setDouble(2, Utilidades.convertirADouble(vo.get("codigoExcepcion")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("articulo")+""))
				ps.setObject(3, null);
			else
				ps.setInt(3, Utilidades.convertirAEntero(vo.get("articulo")+""));


			if(UtilidadTexto.isEmpty(vo.get("valorAjuste")+""))
				ps.setObject(4, null);
			else
				ps.setDouble(4,Utilidades.convertirADouble(signo+vo.get("valorAjuste")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("baseExcepcion")+""))
				ps.setObject(5, null);
			else
				ps.setString(5,vo.get("baseExcepcion")+"");
			
			if(UtilidadTexto.isEmpty(vo.get("nuevaTarifa")+""))
				ps.setObject(6, null);
			else
				ps.setDouble(6,Utilidades.convertirADouble(vo.get("nuevaTarifa")+""));
			
			ps.setString(7, vo.get("usuario")+"");
			ps.setDate(8, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			ps.setString(9, UtilidadFecha.getHoraActual());
			
			if(UtilidadTexto.isEmpty(vo.get("fechavigencia")+""))
				ps.setObject(10, null);
			else
				ps.setDate(10, Date.valueOf(vo.get("fechavigencia")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("valorBase")+""))
				ps.setObject(11, null);
			else
				ps.setDouble(11,Utilidades.convertirADouble(vo.get("valorBase")+""));

			
			ps.executeUpdate();
			return codigoArticulo;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return ConstantesBD.codigoNuncaValido;
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
			PreparedStatementDecorator ps=null;
			
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaEliminarPorcentajeExcepcionAS,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigoAgrupacion));
			ps.executeUpdate();
			
			ps= new PreparedStatementDecorator(con.prepareStatement(eliminarAgrupacionSevicios,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
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
	public static int insertarAgrupacionServicios(Connection con, HashMap vo)
	{
		String signo=(vo.get("suma")+"").trim().equals("S")?"":"-";
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarAgrupacionServiciosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO agru_ser_exce_tari_cont(
			 * codigo,
			 * codigo_excepcion,
			 * pos,
			 * grupo_servicio,
			 * tipo_servicio,
			 * especialidad,
			 * valor_ajuste,
			 * nueva_tarifa,
			 * usuario_modifica,
			 * fecha_modifica,
			 * hora_modifica,
			 * fecha_vigencia) values ('seq_agru_ser_exce_tari_cont'),?,?,?,?,?,?,?,?,?,?,?)
			 */
			
			int codigoAgrupacionServicio=UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_agru_ser_exce_tari_cont");
			
			ps.setDouble(1, codigoAgrupacionServicio);
			
			ps.setDouble(2, Utilidades.convertirADouble(vo.get("codigoExcepcion")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("tipopos")+""))
				ps.setObject(3, null);
			else
				ps.setString(3, vo.get("tipopos")+"");
			
			if(UtilidadTexto.isEmpty(vo.get("gruposervicio")+""))
				ps.setObject(4, null);
			else
				ps.setInt(4, Utilidades.convertirAEntero(vo.get("gruposervicio")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("tiposervicio")+""))
				ps.setObject(5, null);
			else
				ps.setString(5, vo.get("tiposervicio")+"");
			
			if(UtilidadTexto.isEmpty(vo.get("especialidad")+""))
				ps.setObject(6, null);
			else
				ps.setInt(6, Utilidades.convertirAEntero(vo.get("especialidad")+""));			

			if(UtilidadTexto.isEmpty(vo.get("valorAjuste")+""))
				ps.setObject(7, null);
			else
				ps.setDouble(7,Utilidades.convertirADouble(signo+vo.get("valorAjuste")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("nuevaTarifa")+""))
				ps.setObject(8, null);
			else
				ps.setDouble(8,Utilidades.convertirADouble(vo.get("nuevaTarifa")+""));

			ps.setString(9, vo.get("usuario")+"");
			ps.setDate(10, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			ps.setString(11, UtilidadFecha.getHoraActual());
			
			if(UtilidadTexto.isEmpty(vo.get("fechavigencia")+""))
				ps.setObject(12, null);
			else
				ps.setDate(12, Date.valueOf(vo.get("fechavigencia")+""));
			
			ps.executeUpdate();
			return codigoAgrupacionServicio;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return ConstantesBD.codigoNuncaValido;
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean modificarAgrupacionServicios(Connection con, HashMap vo)
	{
		String signo=(vo.get("suma")+"").trim().equals("S")?"":"-";
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarAgrupacionServicios,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE agru_ser_exce_tari_cont SET 
			 * pos=?,
			 * grupo_servicio=?,
			 * tipo_servicio=?,
			 * especialidad=?,
			 * valor_ajuste=?,
			 * nueva_tarifa=?,
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

			if(UtilidadTexto.isEmpty(vo.get("valorAjuste")+""))
				ps.setObject(5, null);
			else
				ps.setDouble(5,Utilidades.convertirADouble(signo+vo.get("valorAjuste")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("nuevaTarifa")+""))
				ps.setObject(6, null);
			else
				ps.setDouble(6,Utilidades.convertirADouble(vo.get("nuevaTarifa")+""));

			ps.setString(7, vo.get("usuario")+"");
			ps.setDate(8, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			ps.setString(9, UtilidadFecha.getHoraActual());
			
			if(UtilidadTexto.isEmpty(vo.get("fechavigencia")+""))
				ps.setObject(10, null);
			else
				ps.setDate(10, Date.valueOf(vo.get("fechavigencia")+""));
			
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
			PreparedStatementDecorator ps=null;
			
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaEliminarPorcentajeExcepcionS,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigoComponente));
			ps.executeUpdate();
			
			ps= new PreparedStatementDecorator(con.prepareStatement(eliminarServicios,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
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
	public static int insertarServicios(Connection con, HashMap vo)
	{
		String signo=(vo.get("suma")+"").trim().equals("S")?"":"-";
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarExcepServiciosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO ser_exce_tari_cont(
			 * codigo,
			 * codigo_excepcion,
			 * codigo_servicio,
			 * valor_ajuste,
			 * nueva_tarifa,
			 * usuario_modifica,
			 * fecha_modifica,
			 * hora_modifica,
			 * fecha_vigencia) 
			 * values (('seq_ser_exce_tari_cont'),?,?,?,?,?,?,?,?)
			 */
			
			int codigoServicio=UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_ser_exce_tari_cont");
			
			ps.setDouble(1, codigoServicio);
			
			ps.setDouble(2, Utilidades.convertirADouble(vo.get("codigoExcepcion")+""));
			if(UtilidadTexto.isEmpty(vo.get("servicio")+""))
				ps.setObject(3, null);
			else
				ps.setInt(3, Utilidades.convertirAEntero(vo.get("servicio")+""));
			

			if(UtilidadTexto.isEmpty(vo.get("valorAjuste")+""))
				ps.setObject(4, null);
			else
				ps.setDouble(4,Utilidades.convertirADouble(signo+vo.get("valorAjuste")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("nuevaTarifa")+""))
				ps.setObject(5, null);
			else
				ps.setDouble(5,Utilidades.convertirADouble(vo.get("nuevaTarifa")+""));
			
			ps.setString(6, vo.get("usuario")+"");
			ps.setDate(7, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			ps.setString(8, UtilidadFecha.getHoraActual());
			
			if(UtilidadTexto.isEmpty(vo.get("fechavigencia")+""))
				ps.setObject(9, null);
			else
				ps.setDate(9, Date.valueOf(vo.get("fechavigencia")+""));
			
			ps.executeUpdate();
			return codigoServicio;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return ConstantesBD.codigoNuncaValido;
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean modificarServicios(Connection con, HashMap vo)
	{
		String signo=(vo.get("suma")+"").trim().equals("S")?"":"-";
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarServicios,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE ser_exce_tari_cont set 
			 * codigo_servicio=?,
			 * valor_ajuste=?,
			 * nueva_tarifa=?,
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


			if(UtilidadTexto.isEmpty(vo.get("valorAjuste")+""))
				ps.setObject(2, null);
			else
				ps.setDouble(2,Utilidades.convertirADouble(signo+vo.get("valorAjuste")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("nuevaTarifa")+""))
				ps.setObject(3, null);
			else
				ps.setDouble(3,Utilidades.convertirADouble(vo.get("nuevaTarifa")+""));

			ps.setString(4, vo.get("usuario")+"");
			ps.setDate(5, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			ps.setString(6, UtilidadFecha.getHoraActual());
			
			if(UtilidadTexto.isEmpty(vo.get("fechavigencia")+""))
				ps.setObject(7, null);
			else
				ps.setDate(7, Date.valueOf(vo.get("fechavigencia")+""));
			
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
	 * @param codigosPKSeccion
	 * @param codigoSeccion
	 * @return
	 */
	public static HashMap<String, Object> consultarPorcentaje(Connection con, String codigosPKSeccion, int codigoSeccion) 
	{
		HashMap<String, Object> mapa=new HashMap<String, Object>();
		String cadena="";
		switch(codigoSeccion)
		{
			case codigoSeccionAgrupacionArticulos:
				cadena=cadenaConsultaPorcentajeExcepcionAA+" where agru_art_exce_tari_cont=? order by prioridad";
			break;
			case codigoSeccionArticulos:
				cadena=cadenaConsultaPorcentajeExcepcionA+" where art_exce_tari_cont=? order by prioridad";
			break;
			case codigoSeccionAgrupacionServicios:
				cadena=cadenaConsultaPorcentajeExcepcionAS+"  where agru_ser_exce_tari_cont=? order by prioridad";
			break;
			case codigoSeccionServicios:
				cadena=cadenaConsultaPorcentajeExcepcionS+"  where ser_exce_tari_cont=? order by prioridad";
			break;
		}
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			String[] codigos=codigosPKSeccion.split(ConstantesBD.separadorSplit);
			int contPos=0;//contador de posiciones del vector de llaves primarias.
			for(int i=1;i<codigos.length;i++)//inicia en 1 para evitar el -1
			{
				mapa.put("numRegistros_"+codigoSeccion+"_"+contPos, "0");
				ps.setDouble(1, Utilidades.convertirADouble(codigos[i]+""));
				ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
				int cont=0;
				while(rs.next())
				{
					mapa.put("codigo_"+codigoSeccion+"_"+contPos+"_"+cont, rs.getObject("codigo")+"");
					mapa.put("porcentaje_"+codigoSeccion+"_"+contPos+"_"+cont, rs.getObject("porcentaje")+"");
					mapa.put("suma_"+codigoSeccion+"_"+contPos+"_"+cont, rs.getObject("suma")+"");
					mapa.put("codigoexcepcion_"+codigoSeccion+"_"+contPos+"_"+cont, codigos[i]);
					mapa.put("prioridad_"+codigoSeccion+"_"+contPos+"_"+cont, rs.getObject("prioridad")+"");
					mapa.put("tiporegistro_"+codigoSeccion+"_"+contPos+"_"+cont, "BD");
					cont++;
				}
				mapa.put("numRegistros_"+codigoSeccion+"_"+contPos, cont);
				contPos++;
			}
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
	 * @param vo
	 * @param codigoSeccion
	 * @return
	 */
	public static boolean insertarPorcentaje(Connection con, HashMap vo, int codigoSeccion) 
	{
		String cadena="";
		switch(codigoSeccion)
		{
			case codigoSeccionAgrupacionArticulos:
				cadena=cadenaInsertarPorcentajeExcepcionAA;
			break;
			case codigoSeccionArticulos:
				cadena=cadenaInsertarPorcentajeExcepcionA;
			break;
			case codigoSeccionAgrupacionServicios:
				cadena=cadenaInsertarPorcentajeExcepcionAS;
			break;
			case codigoSeccionServicios:
				cadena=cadenaInsertarPorcentajeExcepcionS;
			break;
		}
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			
			/**
			 * INSERT INTO porcentaje_agru_art_exce(
			 * codigo,
			 * agru_art_exce_tari_cont,
			 * porcentaje_excepcion,
			 * prioridad,
			 * usuario_modifica,
			 * fecha_modifica,
			 * hora_modifica) values (?,?,?,?,?,?,?)
			 */
			
			ps.setDouble(1, Utilidades.convertirADouble(UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_porcentaje_agru_art_exce")+""));
			ps.setDouble(2, Utilidades.convertirADouble(vo.get("codigoPKExcepcion")+""));
			
			if(UtilidadTexto.getBoolean(vo.get("suma")+""))
				ps.setObject(3, vo.get("porcentaje")+"");
			else
				ps.setDouble(3, (Utilidades.convertirADouble(vo.get("porcentaje")+"")*-1));
			
			ps.setInt(4, Utilidades.convertirAEntero(vo.get("prioridad")+""));
			ps.setString(5, vo.get("usuario")+"");
			ps.setDate(6, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			ps.setString(7, UtilidadFecha.getHoraActual(con));
			Utilidades.imprimirMapa(vo);
			return ps.executeUpdate()>0;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}


	/**
	 * 
	 * @param con
	 * @param vo
	 * @param codigoSeccion
	 * @return
	 */
	public static boolean modificarPorcentaje(Connection con, HashMap vo, int codigoSeccion) 
	{
		String cadena="";
		switch(codigoSeccion)
		{
			case codigoSeccionAgrupacionArticulos:
				cadena=cadenaUpdatePorcentajeExcepcionAA;
			break;
			case codigoSeccionArticulos:
				cadena=cadenaUpdatePorcentajeExcepcionA;
			break;
			case codigoSeccionAgrupacionServicios:
				cadena=cadenaUpdatePorcentajeExcepcionAS;
			break;
			case codigoSeccionServicios:
				cadena=cadenaUpdatePorcentajeExcepcionS;
			break;
		}
		try
		{
			logger.info("cadena-->"+cadena);
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			if(UtilidadTexto.getBoolean(vo.get("suma")+""))
				ps.setObject(1, vo.get("porcentaje")+"");
			else
				ps.setDouble(1, (Utilidades.convertirADouble(vo.get("porcentaje")+"")*-1));
			
			ps.setInt(2, Utilidades.convertirAEntero(vo.get("prioridad")+""));
			ps.setString(3, vo.get("usuario")+"");
			ps.setDate(4, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			ps.setString(5, UtilidadFecha.getHoraActual(con));
			ps.setDouble(6, Utilidades.convertirADouble(vo.get("codigo")+""));
			return ps.executeUpdate()>0;
		}
		catch(SQLException e)
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
	public static boolean eliminarPorcentajes(Connection con, String codigo,int codigoSeccion) 
	{
		String cadena="";
		switch(codigoSeccion)
		{
			case codigoSeccionAgrupacionArticulos:
				cadena=cadenaDeletePorcentajeExcepcionAA;
			break;
			case codigoSeccionArticulos:
				cadena=cadenaDeletePorcentajeExcepcionA;
			break;
			case codigoSeccionAgrupacionServicios:
				cadena=cadenaDeletePorcentajeExcepcionAS;
			break;
			case codigoSeccionServicios:
				cadena=cadenaDeletePorcentajeExcepcionS;
			break;
		}
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			ps.setDouble(1, Utilidades.convertirADouble(codigo));
			return ps.executeUpdate()>0;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}


	public static HashMap consultarPorcentajeLLave(Connection con, int codigo, int codigoSeccion, int posRegistroExcepcion) 
	{
		HashMap<String, Object> mapa=new HashMap<String, Object>();
		String cadena="";
		switch(codigoSeccion)
		{
			case codigoSeccionAgrupacionArticulos:
				cadena=cadenaConsultaPorcentajeExcepcionAA+" where codigo=? ";
			break;
			case codigoSeccionArticulos:
				cadena=cadenaConsultaPorcentajeExcepcionA+" where codigo=? ";
			break;
			case codigoSeccionAgrupacionServicios:
				cadena=cadenaConsultaPorcentajeExcepcionAS+" where codigo=? ";
			break;
			case codigoSeccionServicios:
				cadena=cadenaConsultaPorcentajeExcepcionS+" where codigo=? ";
			break;
		}
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			ps.setDouble(1,Utilidades.convertirADouble(codigo+""));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			int cont=0;
			while(rs.next())
			{
				mapa.put("codigo_"+codigoSeccion+"_"+posRegistroExcepcion+"_"+cont, rs.getObject("codigo")+"");
				mapa.put("porcentaje_"+codigoSeccion+"_"+posRegistroExcepcion+"_"+cont, rs.getObject("porcentaje")+"");
				mapa.put("suma_"+codigoSeccion+"_"+posRegistroExcepcion+"_"+cont, rs.getObject("suma")+"");
				mapa.put("codigoexcepcion_"+codigoSeccion+"_"+posRegistroExcepcion+"_"+cont, rs.getObject("codigoexcepcion")+"");
				mapa.put("prioridad_"+codigoSeccion+"_"+posRegistroExcepcion+"_"+cont, rs.getObject("prioridad")+"");
				mapa.put("tiporegistro_"+codigoSeccion+"_"+posRegistroExcepcion+"_"+cont, "BD");
				cont++;
			}
			mapa.put("numRegistros_"+codigoSeccion+"_"+posRegistroExcepcion, cont);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}

}