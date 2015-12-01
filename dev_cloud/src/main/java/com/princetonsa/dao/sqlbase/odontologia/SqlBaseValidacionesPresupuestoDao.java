package com.princetonsa.dao.sqlbase.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.odontologia.InfoConvenioContratoPresupuesto;

import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

/**
 * 
 * @author axioma
 *
 */
public class SqlBaseValidacionesPresupuestoDao
{
	/**
	 * 
	 */
	private static Logger logger = Logger.getLogger(SqlBaseValidacionesPresupuestoDao.class);
	
	/**
	 * 
	 * @param ingreso
	 * @param convenio
	 * @return
	 */
	public static boolean existeConvenioEnIngreso(int ingreso, int convenio)
	{
		boolean retorna=false;
		//String consultaStr=	"SELECT sub_cuenta FROM  WHERE ingreso="+ingreso+" and convenio="+convenio;
		
		String consultaStr=
					"SELECT " +
							"cip.codigo_pk " +
						"FROM " +
							"facturacion.convenios_ingreso_paciente cip " +
						"INNER JOIN " +
							"contratos cont " +
								"ON(cip.contrato=cont.codigo) " +
						"INNER JOIN " +
							"ingresos ing " +
								"ON(ing.codigo_paciente=cip.paciente) " +
						"WHERE " +
							"ing.id=? " +
						"AND " +
							"convenio=? " +
						"AND " +
							"activo='"+ConstantesBD.acronimoSi+"'";		
		try
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, ingreso);
			ps.setInt(2, convenio);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{
				retorna=true;
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			logger.info("error ==> "+e.toString());
		}
		return retorna;
	
	}
	
	
	/**
	 * 
	 * @param codigoPlanTratamiento
	 * @return
	 */
	public static boolean existeCancelacionContratoXIncumplimiento(BigDecimal codigoPlanTratamiento, int ingreso)
	{
		boolean retorna=false;
		String consultaStr=	" select " +
								"max(pt.codigo_pk) " +
							"from " +
								"odontologia.plan_tratamiento pt " +
							"where " +
								"pt.codigo_pk<>"+codigoPlanTratamiento+" " +
								"and pt.codigo_paciente=(select i.codigo_paciente from ingresos i where i.id="+ingreso+") " +
								"and pt.estado='"+ConstantesIntegridadDominio.acronimoInactivo+"';";
		
		logger.info("existeCancelacionContratoXIncumplimiento--->"+consultaStr);
		
		try
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{
				retorna =Utilidades.convertirADouble(rs.getDouble(1)+"")>0;
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			logger.info("error ==> "+e.toString());
		}
		return retorna;
	}
	
	/**
	 * 
	 * @param ingreso
	 * @param estados
	 * @return
	 */
	public static boolean existenPresupuestosDadosEstados(int ingreso, ArrayList<String> estados)
	{
		boolean retorna=false;
		String consultaStr=	" SELECT " +
								"codigo_pk " +
							"from " +
								"odontologia.presupuesto_odontologico " +
							"where " +
								"ingreso="+ingreso+" and estado in("+UtilidadTexto.convertirArrayStringACodigosSeparadosXComas(estados)+") ";
		
		logger.info("existenPresupuestosDadosEstados-->"+consultaStr);
		
		try
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{
				retorna=true;
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			logger.info("error ==> "+e.toString());
		}
		return retorna;
	}
	
	
	/**
	 * 
	 * @param ingreso
	 * @param estados
	 * @return
	 * 
	 * 
	 */
	
	public static boolean esPlanTratamientoPorConfirmar(BigDecimal planTratamiento)
	{
		boolean retorna=false;
		String consultaStr=	"SELECT por_confirmar FROM odontologia.plan_tratamiento WHERE codigo_pk="+planTratamiento;
		logger.info("esPlanTratamientoPorConfirmar-->"+consultaStr);
		
		try
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{
				retorna=UtilidadTexto.getBoolean(rs.getString(1));
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			logger.info("error ==> "+e.toString());
		}
		return retorna;
	}
	
	/**
	 * 
	 * @param planTratamiento
	 * @param estados
	 * @return
	 */
	public static boolean tienePlanTratamientoEstados(BigDecimal planTratamiento, ArrayList<String> estados)
	{
		boolean retorna=false;
		String consultaStr=	"SELECT " +
								"codigo_pk " +
							"from " +
								"odontologia.plan_tratamiento " +
							"where " +
								"codigo_pk="+planTratamiento+" " +
								"and estado in("+UtilidadTexto.convertirArrayStringACodigosSeparadosXComas(estados)+") ";
		
		logger.info("tienePlanTratamientoEstados--->"+consultaStr);
		
		try
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{
				retorna= true;
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			logger.info("error ==> "+e.toString());
		}
		return retorna;
	}
	
	/**
	 * 
	 * @param planTratamiento
	 * @param estados
	 * @param utilizaProgramas
	 * @return
	 */
	public static boolean tieneServiciosOProgramasPlanTratamientoEstadosCorrectos(BigDecimal planTratamiento, ArrayList<String> estados, boolean utilizaProgramas)
	{
		boolean retorna=true;
		String consultaStr= " select " +
								"p.codigo_pk  " +
							"FROM " +
								"odontologia.det_plan_tratamiento d " +
								"inner join odontologia.programas_servicios_plan_t p ON(p.det_plan_tratamiento=d.codigo_pk) " +
							"where " +
								"d.plan_tratamiento="+planTratamiento+" " +
								"and ";
								consultaStr+=(utilizaProgramas)?
								" p.estado_programa not in("+UtilidadTexto.convertirArrayStringACodigosSeparadosXComas(estados)+") ":" p.estado_servicio not in("+UtilidadTexto.convertirArrayStringACodigosSeparadosXComas(estados)+") ";
		consultaStr+=			") " +
				"AND d.activo='"+ConstantesBD.acronimoSi+"' ";
		
		logger.info("tieneServiciosOProgramasPlanTratamientoEstadosCorrectos--->"+consultaStr);
		
		try
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{
				retorna= false;
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			logger.info("error ==> "+e.toString());
		}
		return retorna;
	}
	
	/**
	 * 
	 * @param ingreso
	 * @return
	 */
	public static ArrayList<InfoConvenioContratoPresupuesto> cargarConveniosContratosPacientePresupuesto(int ingreso)
	{
		ArrayList<InfoConvenioContratoPresupuesto> array = new ArrayList<InfoConvenioContratoPresupuesto>();
		
		String consultaStr=	"SELECT DISTINCT " +
								"convenio as codigoconvenio, " +
								"getnombreconvenio(convenio) as nombreconvenio, " +
								"contrato as codigocontrato, " +
								"getnumerocontrato(contrato) as numcontrato " +
							"FROM " +
								"sub_cuentas " +
							"WHERE " +
								"ingreso="+ingreso;
		
		logger.info("cargarConveniosContratosPacientePresupuesto--->"+consultaStr);
		
		try
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				InfoConvenioContratoPresupuesto info= new InfoConvenioContratoPresupuesto();
				info.setActivo(false);
				info.setConvenio(new InfoDatosInt(rs.getInt("codigoconvenio"), rs.getString("nombreconvenio")));
				info.setContrato(new InfoDatosInt(rs.getInt("codigocontrato"), rs.getString("numcontrato")));
				array.add(info);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			logger.info("error ==> "+e.toString());
		}
		return array;
	}
	
	/**
	 * 
	 * @param codigoPkPresupuestoPieza
	 * @param estadoOPCIONAL
	 * @param utilizaProgramas
	 * @return
	 */
	public static BigDecimal existeProgramaServicioPresupuestoEnPlanTratamiento(BigDecimal codigoPkPresupuestoPieza, String estadoOPCIONAL, boolean utilizaProgramas)
	{
		BigDecimal retorna= BigDecimal.ZERO;
		String consultaStr=" select  pp.codigo_pk as codigoPk  , "+ 
									" pp.presupuesto_odo_prog_serv as presupuestoOdoProgServ  , "+ 
									" coalesce(pp.pieza, 0) as pieza , "+ 
									" pp.hallazgo as hallazgo , "+ 
									" pp.seccion as seccion   , " +
									" coalesce(pp.superficie,0) as superficie , " +
									" po.plan_tratamiento as plantratamiento, " +
									" case when pops.servicio is null then pops.programa||'' else pops.servicio||'' end as progserv "+ 
									" from " +
									" odontologia.presupuesto_piezas pp " +
									" INNER JOIN odontologia.presupuesto_odo_prog_serv pops ON(pops.codigo_pk= pp.presupuesto_odo_prog_serv) " +
									" INNER JOIN odontologia.presupuesto_odontologico po ON(po.codigo_pk=pops.presupuesto) "+
									" WHERE pp.codigo_pk=? ";
		
		
		try 
	    {
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setBigDecimal(1, codigoPkPresupuestoPieza);
			
			logger.info("consulta padre--->"+consultaStr+" -->"+codigoPkPresupuestoPieza);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				if(SqlBasePresupuestoOdontologico.existeProgramaServicioPresupuestoEnPlanTratamiento(rs.getInt("pieza"), rs.getInt("superficie"), rs.getInt("hallazgo"), new BigDecimal(rs.getString("progserv")), utilizaProgramas, rs.getBigDecimal("plantratamiento"), estadoOPCIONAL)>0)
				{	
					retorna= rs.getBigDecimal("codigoPk");
				}		
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR existe en plan ", e);
		}
		return retorna;
	}

	
	/**
	 * 
	 * @param codigoPkPresupuestoPieza
	 * @param estadoOPCIONAL
	 * @param utilizaProgramas
	 * @return
	 */
	public static BigDecimal esProgramaServicioAdicionPlanTtoPresupuesto(BigDecimal pieza, BigDecimal superficie, BigDecimal hallazgo, String seccion, BigDecimal presupuesto, boolean utilizaProgramas, BigDecimal programaOServicio)
	{
		BigDecimal retorna= BigDecimal.ZERO;
		String consultaStr="select " +
								"pptt.codigo_pk " +
							"from " +
								"odontologia.presu_plan_tto_prog_ser pptt " +
								"INNER JOIN odontologia.presupuesto_odontologico po on(po.codigo_pk=pptt.presupuesto) " +
							"WHERE " +
								"pptt.det_plan_tratamiento IN " +
								"(" +
									"SELECT " +
										"d.codigo_pk " +
									"from " +
										"odontologia.det_plan_tratamiento d " +
									"where " +
										"1=1 ";
										consultaStr+=pieza.doubleValue()>0?" and d.pieza_dental="+pieza+" ":" and d.pieza_dental is null ";
										consultaStr+=superficie.doubleValue()>0?" and d.superficie="+superficie+" ":" and d.superficie is null ";
										consultaStr+=" and d.hallazgo="+hallazgo+" ";
										consultaStr+=(UtilidadTexto.isEmpty(seccion))?" and d.seccion='"+seccion+"' ":" ";
										consultaStr+=" and d.activo='"+ConstantesBD.acronimoSi+"' " +
										"and d.plan_tratamiento=po.plan_tratamiento " +
								") " +
								"and pptt.presupuesto=? ";
								consultaStr+=(utilizaProgramas)?" and pptt.programa="+programaOServicio+" ": " and pptt.servicio="+programaOServicio+" ";
								consultaStr+=" and pptt.programa_servicio_plan_t is null ";
		
		try 
	    {
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setBigDecimal(1, presupuesto);
			
			logger.info("consuklta es nuevo -->"+consultaStr+" ->"+presupuesto);
			
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				retorna= rs.getBigDecimal(1);		
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR existe en plan ", e);
		}
		return retorna;
	}
	
	/**
	 * 
	 * @param codigoPkPresupuestoPieza
	 * @param estadoOPCIONAL
	 * @param utilizaProgramas
	 * @return
	 */
	public static BigDecimal existeProgramaServicioAdicionPlanTtoPresupuesto(BigDecimal pieza, BigDecimal superficie, BigDecimal hallazgo, String seccion, BigDecimal presupuesto, boolean utilizaProgramas, BigDecimal programaOServicio)
	{
		BigDecimal retorna= BigDecimal.ZERO;
		String consultaStr="select " +
								"pptt.codigo_pk " +
							"from " +
								"odontologia.presu_plan_tto_prog_ser pptt " +
								"INNER JOIN odontologia.presupuesto_odontologico po on(po.codigo_pk=pptt.presupuesto) " +
							"WHERE " +
								"pptt.det_plan_tratamiento IN " +
								"(" +
									"SELECT " +
										"d.codigo_pk " +
									"from " +
										"odontologia.det_plan_tratamiento d " +
									"where " +
										"1=1 ";
										consultaStr+=pieza.doubleValue()>0?" and d.pieza_dental="+pieza+" ":" and d.pieza_dental is null ";
										consultaStr+=superficie.doubleValue()>0?" and d.superficie="+superficie+" ":" and d.superficie is null ";
										consultaStr+=" and d.hallazgo="+hallazgo+" ";
										consultaStr+=(UtilidadTexto.isEmpty(seccion))?" and d.seccion='"+seccion+"' ":" ";
										consultaStr+=" and d.activo='"+ConstantesBD.acronimoSi+"' " +
										"and d.plan_tratamiento=po.plan_tratamiento " +
								") " +
								"and pptt.presupuesto=? ";
								consultaStr+=(utilizaProgramas)?" and pptt.programa="+programaOServicio+" ": " and pptt.servicio="+programaOServicio+" ";
								
		
		try 
	    {
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setBigDecimal(1, presupuesto);
			
			logger.info("consuklta es nuevo -->"+consultaStr+" ->"+presupuesto);
			
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				retorna= rs.getBigDecimal(1);		
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR existe en plan ", e);
		}
		return retorna;
	}
	
	/**
	 * 
	 * @param codigoPkDetallePlan
	 * @param codigoPkPrograma
	 * @param servicio
	 * @return
	 */
	public static BigDecimal obtenerCodigoPkProgramaServicioPlanTratamiento(BigDecimal codigoPkDetallePlan, BigDecimal codigoPkPrograma,int servicio) 
	{
		BigDecimal retorna= BigDecimal.ZERO;
		String consultaStr=" SELECT ps.codigo_pk FROM odontologia.programas_servicios_plan_t ps WHERE ps.det_plan_tratamiento=? and ps.servicio="+servicio+" ";
				
		if(codigoPkPrograma.doubleValue()>0)
		{	
			consultaStr+= "and ps.programa="+codigoPkPrograma+" and ps.activo='"+ConstantesBD.acronimoSi+"' ";
		}
		
		try 
	    {
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setBigDecimal(1, codigoPkDetallePlan);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				retorna= rs.getBigDecimal(1);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR existe en plan ", e);
		}
		return retorna;
	}

	/**
	 * Verifica si el paciente tiene tarjeta activa
	 * @param codigoPersona código de la persona a verificar
	 * @return true en caso de tener tarjeta activa, false de lo contrario
	 */
	public static boolean pacienteTienetarjetaActiva(int codigoPersona) {
		String sentencia=
			"SELECT " +
				"count(1) AS num_resultados " +
			"FROM " +
				"odontologia.beneficiario_tc_paciente tc " +
			"INNER JOIN " +
				"beneficiario_tarjeta_cliente btc " +
					"ON(tc.codigo_beneficiario=btc.codigo_pk) " +
			"WHERE " +
					"btc.estado_tarjeta='"+ConstantesIntegridadDominio.acronimoEstadoActivo+"' " +
				"AND " +
					"tc.codigo_paciente=?";
		
		Connection con=UtilidadBD.abrirConexion();
		PreparedStatementDecorator psd=new PreparedStatementDecorator(con, sentencia);
		try {
			psd.setInt(1, codigoPersona);
			ResultSetDecorator rsd=new ResultSetDecorator(psd.executeQuery());
			if(rsd.next())
			{
				return rsd.getInt("num_resultados")>0;
			}
		} catch (SQLException e) {
			Log4JManager.error("Error verificando la tarjeta activa del paciente", e);
		}
		finally
		{
			psd.cerrarPreparedStatement();
			UtilidadBD.closeConnection(con);
		}
		return false;
	}
	
	
	
}
