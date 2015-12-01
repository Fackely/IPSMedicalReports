package com.princetonsa.dao.sqlbase.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.InfoDatosAcronimo;
import util.InfoDatosInt;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.odontologia.InfoDetallePaquetePresupuesto;
import util.odontologia.InfoPaquetesPresupuesto;
import util.odontologia.InfoServiciosProgramaPaquetePresupuesto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.odontologia.DtoPresupuestoPaquetes;

/**
 * 
 * 
 * @author Wilson Rios 
 *
 * Jun 5, 2010 - 4:32:42 PM
 */
public class SqlBasePresupuestoPaquetesDao 
{
	/**
	 * insertar
	 */
	private static final String insertarStr="INSERT INTO odontologia.presupuesto_paquetes (" +
												"codigo_pk , " +//1
												"det_paq_odon_convenio , " +//2
												"presupuesto , " +//3
												"usuario_modifica , " +//4
												"fecha_modifica , " +//5
												"hora_modifica) " +//6
											"VALUES(?,?,?,?,?,?) ";
	
	/**
	 * 
	 * Metodo para insertar el encabezado
	 * @param con
	 * @param dto
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public static BigDecimal insertar(Connection con, DtoPresupuestoPaquetes dto)
	{
		BigDecimal secuencia= BigDecimal.ZERO;
	 	try 
		{
			secuencia= new BigDecimal(UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_presupuesto_paquetes")); 
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, insertarStr);
			dto.setCodigoPk(secuencia);
			ps.setBigDecimal(1, dto.getCodigoPk());
			ps.setInt(2, dto.getDetallePaqueteOdontologicoConvenio());
			ps.setBigDecimal(3, dto.getPresupuesto());
			ps.setString(4, dto.getFHU().getUsuarioModifica());
			ps.setString(5, dto.getFHU().getFechaModificaFromatoBD());
			ps.setString(6, dto.getFHU().getHoraModifica());
			
			if(ps.executeUpdate()<=0)
			{
				secuencia= BigDecimal.ZERO;
			}
			ps.close();
		}
		catch (SQLException e) 
		{
			Log4JManager.info("ERROR en insert ", e);
		}
		return secuencia;
	}
	
	/**
	 * 
	 * Metodo para eliminar 
	 * @param con
	 * @param codigoPk
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public static boolean eliminar(Connection con, DtoPresupuestoPaquetes dto)
	{
		boolean retorna= false;
		String eliminarDetalleStr="delete from odontologia.presupuesto_paquetes where 1=1 ";
		boolean filtrado= false;
		if(dto.getCodigoPk().doubleValue()>0)
		{
			filtrado=true;
			eliminarDetalleStr+=" and codigo_pk="+dto.getCodigoPk()+" ";
		}
		else if(dto.getPresupuesto().doubleValue()>0)
		{
			filtrado=true;
			eliminarDetalleStr+=" and presupuesto="+dto.getPresupuesto()+" ";
		}
		if(filtrado)
		{	
			try 
			{
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, eliminarDetalleStr );
				ps.executeUpdate();
				ps.close();
				retorna= true;
			}
			catch (SQLException e) 
			{
				Log4JManager.info("ERROR en eliminar ", e);
				retorna= false;
			}
		}	
		return retorna;
	}
	
	/**
	 * 
	 * Metodo para cargar 
	 * @param con
	 * @param dto
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public static ArrayList<DtoPresupuestoPaquetes> cargar(Connection con, DtoPresupuestoPaquetes dto)
	{
		ArrayList<DtoPresupuestoPaquetes> lista= new ArrayList<DtoPresupuestoPaquetes>();
		String consultaStr=	" SELECT " +
								"pp.codigo_pk as codigo, " +//1
								"pp.det_paq_odon_convenio as paquete , " +//2
								"pp.presupuesto as presupuesto, " +//3
								"po.codigo as codigomostrar " +//4
							"FROM " +
								"odontologia.presupuesto_paquetes pp " +
								"INNER JOIN odontologia.det_paq_odont_convenio dpoc on(dpoc.codigo_pk=pp.det_paq_odon_convenio) " +
								"INNER JOIN odontologia.paquetes_odontologicos po ON(po.codigo_pk=dpoc.codigo_pk_paquete) " +
							"where " +
								"1=1 ";
		
		boolean filtrado= false;
		if(dto.getCodigoPk().doubleValue()>0)
		{
			filtrado=true;
			consultaStr+=" and pp.codigo_pk="+dto.getCodigoPk()+" ";
		}
		else if(dto.getPresupuesto().doubleValue()>0)
		{
			filtrado=true;
			consultaStr+=" and pp.presupuesto="+dto.getPresupuesto()+" ";
		}
		
		if(filtrado)
		{	
			try 
			{
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr );
				ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
				Log4JManager.info("consulta-->"+ps.toString());
				while(rs.next())
				{
					DtoPresupuestoPaquetes info= new DtoPresupuestoPaquetes();
					info.setCodigoPk(rs.getBigDecimal("codigo"));
					info.setDetallePaqueteOdontologicoConvenio(rs.getInt("paquete"));
					info.setPresupuesto(rs.getBigDecimal("presupuesto"));
					info.setCodigoPaqueteMostrar(rs.getString("codigomostrar"));
					info.setVigente(SqlBasePresupuestoPaquetesDao.esDetallePaqueteConvenioVigente(new BigDecimal(info.getDetallePaqueteOdontologicoConvenio()), UtilidadFecha.getFechaActual()));
					lista.add(info);
				}
				ps.close();
				rs.close();
			}
			catch (SQLException e) 
			{
				Log4JManager.info("ERROR en cargar ", e);
			}
		}	
		return lista;
	}

	/**
	 * 
	 * Metodo para actualizar el paquete seleccionado o no 
	 * @param mapping
	 * @param request
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * @param response
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public static ArrayList<InfoPaquetesPresupuesto> cargarInfoPaquetesSinTarifas(	int convenio, int contrato, String fechaVigenciaApp) 
	{
		ArrayList<InfoPaquetesPresupuesto> lista= new ArrayList<InfoPaquetesPresupuesto>();
		
		String consultaStr="SELECT " +
								"dpoc.codigo_pk as codigo_pk_det_paquete_convenio, " +
								"dpoc.esquema_tarifario as esquema_tarifario," +
								"dpoc.codigo_pk_paquete as codigo_pk_paquete, " +
								"po.codigo_pk as codigo_pk_paquete, " +
								"po.codigo as codiigopaquete, " +
								"po.descripcion as descpaquete " +
							"from " +
								"odontologia.paq_odont_convenio poc " +
								"INNER JOIN odontologia.det_paq_odont_convenio dpoc on(dpoc.codigo_pk_paqconv=poc.codigo_pk) " +
								"INNER JOIN odontologia.paquetes_odontologicos po on(po.codigo_pk=dpoc.codigo_pk_paquete) " +
							"where " +
								"poc.convenio=? " +
								"and poc.contrato=? " +
								"and '"+UtilidadFecha.conversionFormatoFechaABD(fechaVigenciaApp)+"' between dpoc.fecha_inicial and dpoc.fecha_final ";
		
		
		try 
		{
			Connection con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr );
			ps.setInt(1, convenio);
			ps.setInt(2, contrato);
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			Log4JManager.info("consulta-->"+ps.toString());
			while(rs.next())
			{
				InfoPaquetesPresupuesto info= new InfoPaquetesPresupuesto(); 
				info.setCodigoNombrePaquete(new InfoDatosAcronimo(rs.getString("codiigopaquete"), rs.getString("descpaquete")));
				info.setDetallePkPaqueteOdonCONVENIO(rs.getInt("codigo_pk_det_paquete_convenio"));
				info.setCodigoPkPaqueteOdontologico(rs.getInt("codigo_pk_paquete"));
				info.setConvenio(new InfoDatosInt(convenio, Utilidades.obtenerNombreConvenioOriginal(convenio)));
				info.setContrato(contrato);
				info.setEsquemaTarifario(rs.getInt("esquema_tarifario"));
				info.setListaProgramas(cargarProgramasPaquete(info.getCodigoPkPaqueteOdontologico()));
				info.setSeleccionado(false);
				lista.add(info);
			}
			ps.close();
			rs.close();
			UtilidadBD.closeConnection(con);
		}
		catch (SQLException e) 
		{
			Log4JManager.info("ERROR en cargar ", e);
		}
		return lista;
	}

	
	/**
	 * 
	 * Metodo para cargar el info de paquete 
	 * @param codigoPkPresupuesto
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public static ArrayList<InfoPaquetesPresupuesto> cargarInfoPaquetesTarifas(	BigDecimal codigoPkPresupuesto) 
	{
		ArrayList<InfoPaquetesPresupuesto> lista= new ArrayList<InfoPaquetesPresupuesto>();
		
		StringBuilder sb= new StringBuilder();
		
		sb.append("SELECT DISTINCT ");
		sb.append(		"dpoc.codigo_pk as codigo_pk_det_paquete_convenio, ");
		sb.append(		"dpoc.esquema_tarifario as esquema_tarifario,");
		sb.append(		"dpoc.codigo_pk_paquete as codigo_pk_paquete, ");
		sb.append(		"po.codigo_pk as codigo_pk_paquete, ");
		sb.append(		"po.codigo as codiigopaquete, ");
		sb.append(		"po.descripcion as descpaquete,");
		sb.append(		"poc.convenio as convenio,");
		sb.append(		"poc.contrato as contrato ");
		sb.append(	"from ");
		sb.append(		"odontologia.paq_odont_convenio poc ");
		sb.append(		"INNER JOIN odontologia.det_paq_odont_convenio dpoc on(dpoc.codigo_pk_paqconv=poc.codigo_pk) ");
		sb.append(		"INNER JOIN odontologia.paquetes_odontologicos po on(po.codigo_pk=dpoc.codigo_pk_paquete) ");
		sb.append(		"INNER JOIN odontologia.presupuesto_paquetes pp on(pp.det_paq_odon_convenio=dpoc.codigo_pk) ");
		sb.append(	"where ");
		sb.append(		"pp.presupuesto=? ");
								
		
		try 
		{
			Connection con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, sb.toString() );
			ps.setBigDecimal(1, codigoPkPresupuesto);
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			Log4JManager.info("consulta-->"+ps.toString());
			while(rs.next())
			{
				InfoPaquetesPresupuesto info= new InfoPaquetesPresupuesto(); 
				info.setCodigoNombrePaquete(new InfoDatosAcronimo(rs.getString("codiigopaquete"), rs.getString("descpaquete")));
				info.setDetallePkPaqueteOdonCONVENIO(rs.getInt("codigo_pk_det_paquete_convenio"));
				info.setCodigoPkPaqueteOdontologico(rs.getInt("codigo_pk_paquete"));
				info.setConvenio(new InfoDatosInt(rs.getInt("convenio"), Utilidades.obtenerNombreConvenioOriginal(rs.getInt("convenio"))));
				info.setContrato(rs.getInt("contrato"));
				info.setEsquemaTarifario(rs.getInt("esquema_tarifario"));
				info.setListaProgramas(cargarProgramasPaquete(info.getCodigoPkPaqueteOdontologico()));
				info.setSeleccionado(true);
				lista.add(info);
			}
			ps.close();
			rs.close();
			UtilidadBD.closeConnection(con);
		}
		catch (SQLException e) 
		{
			Log4JManager.info("ERROR en cargar ", e);
		}
		return lista;
	}
	
	/**
	 * 
	 * Metodo para cargar los programas del paquete
	 * @param codigoPkPaqueteOdontologico
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	private static ArrayList<InfoDetallePaquetePresupuesto> cargarProgramasPaquete(	int codigoPkPaqueteOdontologico) 
	{
		ArrayList<InfoDetallePaquetePresupuesto> lista= new ArrayList<InfoDetallePaquetePresupuesto>();
		
		String consultaStr="SELECT " +
								"ppo.codigo_pk as codigo_pk,  " +
								"ppo.programa as codigo_pk_programa, " +
								"ppo.cantidad as cantidad, " +
								"p.nombre as nombreprog, " +
								"ppo.codigo_pk_paquete as codigo_pk_paquete " +
							"from " +
								"odontologia.prog_paquete_odonto ppo " +
								"INNER JOIN odontologia.programas p on(p.codigo=ppo.programa) " +
							"where " +
								"ppo.codigo_pk_paquete=? ";
								
		
		try 
		{
			Connection con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr );
			ps.setInt(1, codigoPkPaqueteOdontologico);
			Log4JManager.info("consulta-->"+ps.toString());
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				InfoDetallePaquetePresupuesto info= new InfoDetallePaquetePresupuesto();
				info.setCantidad(rs.getInt("cantidad"));
				info.setCodigoPkProgPaqueteOdontologico(rs.getInt("codigo_pk"));
				info.setCodigoPkPrograma(rs.getBigDecimal("codigo_pk_programa"));
				info.setCodigoPkPaquete(rs.getInt("codigo_pk_paquete"));
				info.setNombrePrograma(rs.getString("nombreprog"));
				info.setListaServicios(cargarListaServiciosPrograma(info.getCodigoPkPrograma()));
				lista.add(info);
			}
			ps.close();
			rs.close();
			UtilidadBD.closeConnection(con);
		}
		catch (SQLException e) 
		{
			Log4JManager.info("ERROR en cargar ", e);
		}
		return lista;
	}

	/**
	 * 
	 * Metodo para cargar los servicios activos del programa 
	 * @param codigoPkPrograma
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	private static ArrayList<InfoServiciosProgramaPaquetePresupuesto> cargarListaServiciosPrograma(	BigDecimal codigoPkPrograma) 
	{
		ArrayList<InfoServiciosProgramaPaquetePresupuesto> lista= new ArrayList<InfoServiciosProgramaPaquetePresupuesto>();
		
		String consultaStr=	"SELECT dp.servicio as servicio  from odontologia.detalle_programas dp where dp.programas=? and dp.activo='"+ConstantesBD.acronimoSi+"'";

		try 
		{
			Connection con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr );
			Log4JManager.info("consulta-->"+ps.toString());
			ps.setBigDecimal(1, codigoPkPrograma);
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				InfoServiciosProgramaPaquetePresupuesto info= new InfoServiciosProgramaPaquetePresupuesto();
				info.setServicio(rs.getInt("servicio"));
				info.setValorBase(BigDecimal.ZERO);
				info.setExisteTarifa(false);
				lista.add(info);
			}
			ps.close();
			rs.close();
			UtilidadBD.closeConnection(con);
		}
		catch (SQLException e) 
		{
			Log4JManager.info("ERROR en cargar ", e);
		}
		return lista; 
	}

	/**
	 * 
	 * Metodo para obtener codigo a mostrar del paquete odo
	 * @param codigoPkPaqueteOdonCONVENIO
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public static String obtenerCodigoMostrarPaqueteOdontologico(int codigoPkPaquete) 
	{
		String consultaStr="SELECT po.codigo as cod FROM odontologia.paquetes_odontologicos po where codigo_pk=? ";
		String retorna="";
		
		try 
		{
			Connection con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr );
			Log4JManager.info("consulta-->"+ps.toString());
			ps.setInt(1, codigoPkPaquete);
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				retorna= rs.getString("cod");
			}
			ps.close();
			rs.close();
			UtilidadBD.closeConnection(con);
		}
		catch (SQLException e) 
		{
			Log4JManager.info("ERROR en cargar ", e);
		}
		return retorna; 
	}

	/**
	 * 
	 * Metodo para validar si es vigente o no el paquete odo convenio
	 * @param codigoPk
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public static boolean esDetallePaqueteConvenioVigente(BigDecimal codigoPk, String fechaApp)
	{
		String consultaStr="select count(1) from odontologia.det_paq_odont_convenio where codigo_pk=? and '"+UtilidadFecha.conversionFormatoFechaABD(fechaApp)+"' between fecha_inicial and fecha_final ";
		boolean retorna=false;
		
		try 
		{
			Connection con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr );
			Log4JManager.info("consulta-->"+ps.toString());
			ps.setBigDecimal(1, codigoPk);
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				retorna= rs.getInt(1)>0;
			}
			ps.close();
			rs.close();
			UtilidadBD.closeConnection(con);
		}
		catch (SQLException e) 
		{
			Log4JManager.info("ERROR en cargar ", e);
		}
		return retorna; 
	}
	
}
