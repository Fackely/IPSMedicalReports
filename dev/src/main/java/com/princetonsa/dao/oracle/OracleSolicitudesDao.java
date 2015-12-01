/*
 * @(#)OracleSolicitudesDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje		: Java
 * Compilador	: J2SDK 1.4.1_01
 */
package com.princetonsa.dao.oracle;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.SolicitudesDao;
import com.princetonsa.dao.postgresql.PostgresqlSolicitudesDao;
import com.princetonsa.dao.sqlbase.SqlBaseSolicitudesDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.servinte.axioma.fwk.exception.BDException;

/**
* Esta clase implementa el contrato estipulado en <code>SolicitudesDao</code>, y presta los
* servicios de acceso a una base de datos Oracle requeridos por la clase
* <code>Solicitudes</code>.
*
* @version 1.1, Feb 12, 2004
* @author <a href="mailto:edgar@princetonsa.com">Edgar Prieto</a>
* @author <a href="mailto:raul@princetonsa.com">Raúl Cancino</a>
*/
public class OracleSolicitudesDao implements SolicitudesDao
{
	/**
	 * 
	 */
	public static Logger logger=Logger.getLogger(PostgresqlSolicitudesDao.class);
	
	/** Código de la ocupación médica 'Todos' */
	private static final int ii_ocupacionTodos = ConstantesBD.codigoOcupacionMedicaTodos;

	/** Código del centro de costo 'Externo' */
	private static final int ii_centroCostoExterno = ConstantesBD.codigoCentroCostoExternos;

	/** Código del centro de costo 'Todos' */ 
	private static final int ii_centroCostoTodos = ConstantesBD.codigoCentroCostoTodos;

	/** Código de la especialidad médica 'Todos' */
	private static final int ii_especialidadTodos = ConstantesBD.codigoEspecialidadMedicaTodos;

	/** Cadena constante de consulta base para la generación de listados de solicitudes */
	private static final String is_listar =
				"SELECT distinct so.numero_solicitud AS numeroSolicitud," +
					" so.cobrable AS cobrable,"	+
					" so.consecutivo_ordenes_medicas AS consecutivoOrdenesMedicas," +
					" so.cuenta AS codigoCuenta," +
					" so.urgente AS urgente," + 
					" so.va_epicrisis AS vaEpicrisis," + 
					" so.centro_costo_solicitado AS codigoCentroCostoSolicitado," + 
					" case when so.pyp is null or so.pyp = "+ValoresPorDefecto.getValorFalseParaConsultas()+" then 'false' else 'true' end as pyp, " +
					" (so.fecha_solicitud||'') AS fechaSolicitud," +
					" (so.hora_solicitud||'') AS horaSolicitud," +
					" (so.fecha_grabacion||'') AS fechaGrabacion," +
					" (so.hora_grabacion||'') AS horaGrabacion," +
					" CASE WHEN so.fecha_interpretacion IS NULL THEN '' ELSE (so.fecha_interpretacion||'') END AS fechaInterpretacion, " +
					" CASE WHEN so.hora_interpretacion IS NULL THEN '' ELSE (so.hora_interpretacion||'') END AS horaInterpretacion," +
					" '' AS fechaRespuesta," +
					" '' AS horaRespuesta," +
					" CASE WHEN so.codigo_medico_interpretacion IS NULL THEN -1 ELSE codigo_medico_interpretacion END AS codigoMedicoInterpretacion," +
					" CASE WHEN so.interpretacion IS NULL THEN '' ELSE so.interpretacion END  AS interpretacion," +
					" CASE WHEN so.numero_autorizacion IS NULL THEN '' ELSE so.numero_autorizacion END AS numeroAutorizacion," +
					" getnomcentrocosto(so.centro_costo_solicitado) AS nombreCentroCostoSolicitado," +
					" so.centro_costo_solicitante "		+ "AS codigoCentroCostoSolicitante," +
					" getnomcentrocosto(so.centro_costo_solicitante) AS nombreCentroCostoSolicitante, " +
					" so.especialidad_solicitante AS codigoEspecialidadSolicitante," +
					" getnombreespecialidad(so.especialidad_solicitante) AS nombreEspecialidadSolicitante," +
					" so.estado_historia_clinica AS codigoEstadoHistoriaClinica,"	+
					" getestadosolhis(so.estado_historia_clinica) AS nombreEstadoHistoriaClinica,"	+
					" so.ocupacion_solicitada AS codigoOcupacionSolicitada," +
					" getocupacion(so.ocupacion_solicitada) AS nombreOcupacionSolicitada," +
					" so.tipo AS codigoTiposolicitud,"	+
					" CASE WHEN so.tipo="+ConstantesBD.codigoTipoSolicitudCirugia+" THEN coalesce(ts.nombre||'-'||getintegridaddominio(sc.ind_qx),'') ELSE coalesce(ts.nombre,'') END AS nombreTiposolicitud,"	+
					" CASE WHEN so.codigo_medico IS NULL THEN -1 ELSE so.codigo_medico END AS codigoMedicoSolicitante,"	+
					" coalesce(getnombrepersona(so.codigo_medico),'') as nombreMedicoSolicita, "+
					" CASE WHEN so.codigo_medico_responde IS NULL THEN -1 ELSE so.codigo_medico_responde END AS codigoMedicoResponde," +
					" coalesce(getnombrepersona(so.codigo_medico_responde),'') as nombreMedicoResponde, "+
					" c.codigo_paciente AS codigoPaciente, " +
					" getnombrepersona(c.codigo_paciente) as nombrePaciente, "+
					" cc.centro_atencion AS codCentroAtencion," +
					" getnomcentroatencion(cc.centro_atencion) AS nomCentroAtencion, " +
					" getcamacuenta(c.id,c.via_ingreso) as cama, " +
					" gettieneportatilsolicitud1(so.numero_solicitud) As portatil," +
					" case when so.tipo = "+ConstantesBD.codigoTipoSolicitudInterconsulta+" then getCodEspecOredenadaInter(so.numero_solicitud) else -1 end as codigoEspecialidadSolicitada, " +
					" case when so.tipo = "+ConstantesBD.codigoTipoSolicitudInterconsulta+" then getNomEspecOredenadaInter(so.numero_solicitud) else '' end as nombreEspecialidadSolicitada, " +  
					" case when so.tipo = "+ConstantesBD.codigoTipoSolicitudProcedimiento+" then getSolProcMultiple(so.numero_solicitud)  else "+ValoresPorDefecto.getValorFalseParaConsultas()+" end as multiple, " +
					" case when so.tipo = "+ConstantesBD.codigoTipoSolicitudProcedimiento+" then getSolProcFinalizada(so.numero_solicitud)  else null end as finalizada,  " +
					" case when getSolicitudTieneIncluidos(so.numero_solicitud)>0 then "+ValoresPorDefecto.getValorTrueParaConsultas()+" else "+ValoresPorDefecto.getValorFalseParaConsultas()+" end as incluyeServiciosArticulos "+
		" FROM solicitudes so " +  
		" INNER JOIN tipos_solicitud ts ON(ts.codigo=so.tipo) " +  
		" INNER JOIN cuentas c  ON ( c.id =so.cuenta) "+  
		" INNER JOIN centros_costo cc  on (c.area=cc.codigo) " +
		" LEFT OUTER JOIN solicitudes_cirugia sc on (so.numero_solicitud=sc.numero_solicitud) " +									
		" left outer join his_camas_cuentas hcc on(hcc.cuenta=so.cuenta) " + 
		" WHERE " +
		" (sc.numero_solicitud IS NULL OR " +
		" (so.tipo="+ConstantesBD.codigoTipoSolicitudCirugia+" AND " +
				"(sc.ind_qx = '"+ConstantesIntegridadDominio.acronimoIndicativoCargoCirugia +"' OR sc.ind_qx = '"+ConstantesIntegridadDominio.acronimoIndicativoCargoNoCruento +"')))" +
		" AND	so.tipo "	+ "IN("	+	// Solicitud valids en el listado
										ConstantesBD.codigoTipoSolicitudInterconsulta		+ ","	+	// (Interconsulta, Procedimientos y Farmacia)
										ConstantesBD.codigoTipoSolicitudProcedimiento		+ ","	+
										//ConstantesBD.codigoTipoSolicitudMedicamentos			+","	+
										//ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos+","	+            //<---Tarea XPlanner2 3415 
										//ConstantesBD.codigoTipoSolicitudCargosDirectosServicios+","+
                                        ConstantesBD.codigoTipoSolicitudCirugia+
									")";
	
	/**
	 * Cadena implementada para insertar el LOG de Revisión de la Cuenta
	 * en la modificacion de las tarifas
	 */
	private static final String insertarLogRevisionCuentaStr = "INSERT " +
		"INTO tarifas_rev_cuenta " +
		"(codigo,cuenta,solicitud,servicio,articulo,tipo_cargo,tarifa_inicial,tarifa_modificada,usuario,fecha,hora,institucion) " +
		"VALUES " +
		"(seq_tarifas_rev_cuenta.nextval,?,?,?,?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+",?)";

	/**
	* Obtiene un listado de solicitudes de acuerdo a los parámatros especificados en el objeto
	* Solicitudes en una BD Postgresql
	*
	* @return <code>List<code> con las solicitudes que coinciden con los parámetros especificados.
	*/
	public Collection listarSolicitudes(
		Connection	ac_con,
		int	ai_codigoCentroCostoSolicitado,
		int ai_codigoCentroCostoSolicitante,
		int	ai_codigoCentroCostoTratante,
		int	ai_codigoMedico,
		int	ai_codigoOcupacionSolicitada,
		int	ai_codigoPaciente,
		int	ai_codigoTipoSolicitud,
		int	ai_estadoCuenta,
		int	ai_estadoHistoriaClinica,
		int[] aia_codigoEspecialidadSolicitada,
		int	codigoCuentaAsociada,
		int	codigoCentroCostoIntentaAcceso,
		int codigoCuenta,
		int institucion,
		boolean resumenAtenciones,
		int codigoCentroAtencionCuenta, 
		String fechaInicialFiltro, 
		String fechaFinalFiltro, 
		String centroCostoSolicitanteFiltro,
		String estadoHCFiltro, 
		String tipoOrdenFiltro,
		String areaFiltro,
		String centroCostoSolicitadoFiltro,
		String pisoFiltro,
		String habitacionFiltro,
		String camaFiltro, 
		boolean requierePortatilFiltro,
		String codigoMedicoEnSesion
	)throws SQLException
	{
        int				li_tam;
		StringBuffer	lsb_sb;
		/* Abrir conexión a la base de datos si es necesario */
		if(ac_con == null || ac_con.isClosed() )
			ac_con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();

		/*Si es procedimientos ejecutar la consulta de procedimientos.*/
		if(ai_codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudProcedimiento)
		{
			return SqlBaseSolicitudesDao.listarSolicitudesProcedimientos(ac_con,ai_codigoCentroCostoSolicitado,
					ai_codigoCentroCostoSolicitante,ai_codigoCentroCostoTratante,ai_codigoMedico,ai_codigoOcupacionSolicitada,ai_codigoPaciente,
					ai_codigoTipoSolicitud,ai_estadoCuenta,ai_estadoHistoriaClinica,aia_codigoEspecialidadSolicitada,codigoCuentaAsociada,
					codigoCentroCostoIntentaAcceso,codigoCuenta,institucion,resumenAtenciones,codigoCentroAtencionCuenta,fechaInicialFiltro,fechaFinalFiltro,centroCostoSolicitanteFiltro,areaFiltro,centroCostoSolicitadoFiltro,pisoFiltro,habitacionFiltro,camaFiltro,requierePortatilFiltro 
					);
		}
		
		/*Si es Interconsulta ejecutar la consulta de Interconsulta.*/
		if(ai_codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudInterconsulta)
		{
			return SqlBaseSolicitudesDao.listarSolicitudesInterconsulta(ac_con,ai_codigoCentroCostoSolicitado,
					ai_codigoCentroCostoSolicitante,ai_codigoCentroCostoTratante,ai_codigoMedico,ai_codigoOcupacionSolicitada,ai_codigoPaciente,
					ai_codigoTipoSolicitud,ai_estadoCuenta,ai_estadoHistoriaClinica,aia_codigoEspecialidadSolicitada,codigoCuentaAsociada,
					codigoCentroCostoIntentaAcceso,codigoCuenta,institucion,resumenAtenciones,codigoCentroAtencionCuenta ,fechaInicialFiltro,fechaFinalFiltro,centroCostoSolicitanteFiltro,areaFiltro,centroCostoSolicitadoFiltro,pisoFiltro,habitacionFiltro,camaFiltro, codigoMedicoEnSesion, DaoFactory.POSTGRESQL
					);
		}

		/* Adicionar restricciones de acuerdo a los parámetros */
		lsb_sb = new StringBuffer(is_listar);

		/* Filtrar el centro de costo solicitado */
		if(ai_codigoCentroCostoSolicitado > -1)
			lsb_sb.append(
				" AND so.centro_costo_solicitado IN(" +
				ii_centroCostoTodos + "," + ii_centroCostoExterno + ","
				+ ai_codigoCentroCostoSolicitado + ")"
			);
		if(codigoCentroAtencionCuenta > -1 && !resumenAtenciones)
		{
			lsb_sb.append(
				" AND cc.centro_atencion = "+ codigoCentroAtencionCuenta+" "
			);
		}
		
		
		/* Filtrar el centro de costo solicitante */
		if(ai_codigoCentroCostoSolicitante > -1)
		{
			
			//Se deben manejar dos grandes casos, cuando queremos ver
			//los que se desean interpretar (estan en estado respondido)
			if (ai_estadoHistoriaClinica==ConstantesBD.codigoEstadoHCRespondida)
			{
				//Dentro del caso de interpretación hay dos subcasos a manejar
				//interconsulta y procedimientos
				if (ai_codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudInterconsulta )
				{
					
					//En el caso de interconsulta solo deben aparecer las solicitudes
					//para las cuales el centro de costo del médico actual sea
					//el tratante
					lsb_sb.append(" AND " + codigoCentroCostoIntentaAcceso +" IN (SELECT CASE WHEN tc.centro_costo="+ConstantesBD.codigoCentroCostoUrgencias+" THEN "+ValoresPorDefecto.getCentroCostoUrgencias(institucion)+" ELSE tc.centro_costo END from tratantes_cuenta tc where tc.solicitud=so.numero_solicitud and tc.cuenta="+codigoCuenta+" and (tc.fecha_inicio<CURRENT_DATE or (tc.fecha_inicio=CURRENT_DATE and tc.hora_inicio<="+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+")) and (tc.fecha_fin IS NULL or tc.fecha_fin>CURRENT_DATE or (tc.fecha_fin=CURRENT_DATE and tc.hora_fin>="+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+") )        ) ");
				}
				else if (ai_codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudProcedimiento)
				{
					
					//Además del tratante, la puede intrepretar quien la solicitó
					lsb_sb.append(" AND (   " + codigoCentroCostoIntentaAcceso +"=" + ai_codigoCentroCostoSolicitante + "  OR " + codigoCentroCostoIntentaAcceso +" IN (SELECT tc.centro_costo from tratantes_cuenta tc where tc.solicitud=so.numero_solicitud and tc.cuenta="+codigoCuenta+" and (tc.fecha_inicio<CURRENT_DATE or (tc.fecha_inicio=CURRENT_DATE and tc.hora_inicio<="+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+")) and (tc.fecha_fin IS NULL or tc.fecha_fin>CURRENT_DATE or (tc.fecha_fin=CURRENT_DATE and tc.hora_fin>="+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+") )  )                    )");
				}
			}
			else
			{
				//Para cuando no es interpretar se sigue manejando la validación
				//previa
				lsb_sb.append(" AND so.centro_costo_solicitante=" + ai_codigoCentroCostoSolicitante);
			}
		}
		
		
		/* Filtrar la ocupación solicitada */
		if(ai_codigoOcupacionSolicitada > -1)
			lsb_sb.append(
				" AND so.ocupacion_solicitada IN(" +
				ii_ocupacionTodos +"," + ai_codigoOcupacionSolicitada + ")"
			);

		/*
			Filtrar la especialidad solicitada. Las especialidades solo deben filtrarse para
			los tipos de solicitud I (Interconsulta)
		*/
		/////asui nunca llegaria, por que las especialidades, se hacen al responder, y el responder se hace por otro flujo.
		/*
		if(
			aia_codigoEspecialidadSolicitada != null &&
			(li_tam = aia_codigoEspecialidadSolicitada.length) > 0
		)
		{
			int li_i;
			
			lsb_sb.append(
				"AND((se.codigo IS NOT NULL AND(( (so.tipo=" + ConstantesBD.codigoTipoSolicitudInterconsulta +" OR so.tipo="+ConstantesBD.codigoTipoSolicitudInterconsultaGinecoObstetrica+")"+
				" AND se.especialidad IN(" + ii_especialidadTodos
			);

			for(li_i = 0; li_i < li_tam; li_i++)
				lsb_sb.append("," + aia_codigoEspecialidadSolicitada[li_i]);

			
			//@todo Adicionar a la última parte del listado, cuando se cumple solo para asiocio de cuentas
			
			lsb_sb.append(
				"))OR(so.tipo<>" + ConstantesBD.codigoTipoSolicitudInterconsulta + " AND so.tipo<>" + ConstantesBD.codigoTipoSolicitudInterconsultaGinecoObstetrica + " AND se.especialidad	>-1))OR(" +
				"se.codigo IS NULL AND so.centro_costo_solicitado=" + ii_centroCostoExterno +
				" AND (getTratanteSolicitud(so.numero_solicitud)=" + ai_codigoCentroCostoTratante +
				"))))"
				//hasta aqui
			);
		}
		*/
		/* Filtrar el médico solicitante */
		if(ai_codigoMedico > -1)
			lsb_sb.append(" AND so.codigo_medico=" + ai_codigoMedico);

		/* Filtar el paciente */
		if(ai_codigoPaciente > -1)
			lsb_sb.append(" AND c.codigo_paciente=" + ai_codigoPaciente);

        
		/* Filtrar el estado de la cuenta */
		if(ai_estadoCuenta > -1)
		{
			lsb_sb.append(" AND (c.estado_cuenta=" + ai_estadoCuenta+" or c.estado_cuenta="+ConstantesBD.codigoEstadoCuentaFacturadaParcial);
			if(codigoCuentaAsociada > -1)
				lsb_sb.append(" OR c.estado_cuenta=" + ConstantesBD.codigoEstadoCuentaAsociada);
			lsb_sb.append(")");					
		}

		/* Filtrar únicamente las que tienen manejo interconsulta
		    Caso de procedimientos siempre da true */
		
		
		if(codigoCuenta>0)
		{
			if(codigoCuentaAsociada>0)
			{
				//se estban presentando problemas cuando habia más de 1 cuenta asociada activa, por cuestiones de tiempo solo se agreg una consulta para 
				// buscar las cuentas; lo ideal seria arreglar el sistema para que maneje mas de 1 cuentaasociada a la vez
				String cuentasActivas="Select cu.id as idcuenta from cuentas cu where cu.codigo_paciente="+ai_codigoPaciente+" and cu.estado_cuenta IN(0,3,6)";
				HashMap mapa=new HashMap();
				String cuentaso="";
				try {
				
		    	PreparedStatementDecorator pst1= new PreparedStatementDecorator(ac_con.prepareStatement(cuentasActivas,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		    	ResultSetDecorator rs1= new ResultSetDecorator(pst1.executeQuery());
		    	mapa= UtilidadBD.cargarValueObject(rs1);
		    	rs1.close();
		    	pst1.close();
		    	for (int m=0; m<Integer.parseInt(mapa.get("numRegistros").toString());m++)
		    	{
		    		cuentaso+=mapa.get("idcuenta_"+m);
		    		if (m+1<Integer.parseInt(mapa.get("numRegistros").toString()))
		    		{
		    			cuentaso+=",";
		    		}
		    	}
				}
				catch(Exception e){
					Log4JManager.error("ERROR OracleSolicitudesDao al consultar las cuentas de usuario", e);
				}
				
				lsb_sb.append("AND so.cuenta in("+cuentaso+")");
			}
			else
			{
				lsb_sb.append("AND so.cuenta ="+codigoCuenta);	
			}
		}
		//Esta linea la puse para listar las solicitudes diferentes a las de farmacia. si no funciona bien quitarla @armando
		lsb_sb.append(" AND so.tipo<>"+ConstantesBD.codigoTipoSolicitudMedicamentos);
		
		if(!resumenAtenciones)
			lsb_sb.append(" AND c.estado_cuenta IN("+ConstantesBD.codigoEstadoCuentaActiva+","+ConstantesBD.codigoEstadoCuentaAsociada+","+ConstantesBD.codigoEstadoCuentaFacturadaParcial+")");
		
		
		
		if(!centroCostoSolicitanteFiltro.equals(""))
		{
			lsb_sb.append(" AND so.centro_costo_solicitante ='"+centroCostoSolicitanteFiltro+"'");
		}
		
		if(!fechaInicialFiltro.equals("")&&!fechaFinalFiltro.equals(""))
		{
			lsb_sb.append(" AND so.fecha_solicitud  between '"+fechaInicialFiltro+"' and '"+fechaFinalFiltro+"'");
		}
		if(!UtilidadTexto.isEmpty(areaFiltro))
		{
			lsb_sb.append(" AND c.area = '"+areaFiltro+"'");
		}
		if(!UtilidadTexto.isEmpty(centroCostoSolicitadoFiltro))
		{
			lsb_sb.append(" AND so.centro_costo_solicitado = '"+centroCostoSolicitadoFiltro+"'");
		}
		if(!UtilidadTexto.isEmpty(pisoFiltro))
		{
			lsb_sb.append(" AND hcc.codigopkpiso = '"+pisoFiltro+"'");
		}
		if(!UtilidadTexto.isEmpty(habitacionFiltro))
		{
			lsb_sb.append(" AND hcc.codigopkhabitacion = '"+habitacionFiltro+"'");
		}
		if(!UtilidadTexto.isEmpty(camaFiltro))
		{
			lsb_sb.append(" AND hcc.codigocama = '"+camaFiltro+"'");
		}
		
		if (requierePortatilFiltro)
		{
			lsb_sb.append(" AND gettieneportatilsolicitud(so.numero_solicitud)>0 ");
		}
		
		
		//, String tipoOrdenFiltro
		if(!estadoHCFiltro.trim().equals(""))
		{
			lsb_sb.append(" AND so.estado_historia_clinica=" + estadoHCFiltro);
		}
		else
		{
			if(ai_estadoHistoriaClinica > -1)
			{
				if(ai_estadoHistoriaClinica==ConstantesBD.codigoEstadoHCSolicitada && ai_codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudProcedimiento)
				{
					lsb_sb.append(" AND (so.estado_historia_clinica=" + ai_estadoHistoriaClinica +" OR (so.estado_historia_clinica=" + ConstantesBD.codigoEstadoHCRespondida + " AND multiple="+ValoresPorDefecto.getValorTrueParaConsultas()+" AND finalizada= "+ValoresPorDefecto.getValorFalseParaConsultas() +") )");
				}
				else
				{
					lsb_sb.append(" AND so.estado_historia_clinica=" + ai_estadoHistoriaClinica);
				}
			}
		}
		
		if(!tipoOrdenFiltro.trim().equals(""))
		{
			lsb_sb.append(" AND so.tipo ="+tipoOrdenFiltro);
		}
		else
		{
			/* Filtrar el tipo de solicitud */
			if(ai_codigoTipoSolicitud > -1)
			{
				String tipoSolicitud=ai_codigoTipoSolicitud+"";
	            lsb_sb.append(" AND so.tipo = "+tipoSolicitud+" ");
	       }
		}

		
		logger.info("Consulta:\n"+lsb_sb.toString()+"\n");
		
		return UtilidadBD.resultSet2Collection(new ResultSetDecorator(new PreparedStatementDecorator(ac_con.prepareStatement(lsb_sb.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet) ).executeQuery()));
	}

	/**
	 * adición de Sebastián
	 * Método que obtiene la lista de solicitudes de acuerdo a los parámetros
	 * especificados en RevisionCuenta del módulo facturación
	 * @param con
	 * @param idCuenta
	 * @param contrato
	 * @param opcion: si es true se refiere a una cuenta, si es false se refiere a una subcuenta
	 * @return
	 */
	public HashMap listarSolicitudes(Connection con, int idCuenta, int contrato,boolean opcion) {
		// @todo Auto-generated method stub
		return SqlBaseSolicitudesDao.listarSolicitudes(con,idCuenta,contrato,opcion);
	}

	/**
	 * adición de Sebastián
	 * Método que obtiene los campos de la solicitud que es de servicios
	 * @param con
	 * @param solicitud
	 * @return
	 */
	public Collection listarSolicitudesServicios(Connection con,int solicitud){
		return SqlBaseSolicitudesDao.listarSolicitudesServicios(con,solicitud);
	}
	/**
	 * adición de Sebastián
	 * Método que obtiene los campos de la solicitud que es de medicamentos
	 * @param con
	 * @param solicitud
	 * @return
	 */
	public Collection listarSolicitudesMedicamentos(Connection con,int solicitud){
		return SqlBaseSolicitudesDao.listarSolicitudesMedicamentos(con,solicitud);
	}

	/**
	 * adición de Sebastián
	 * Método que inserta un pool de medico por solicitud en el caso de que el médico pertenezca
	 * a varios pooles
	 * @param con
	 * @param solicitud
	 * @param pool
	 * @return
	*/	
	public int insertarPoolMedicoXSolicitud(Connection con, int solicitud, int pool) {
		return SqlBaseSolicitudesDao.insertarPoolMedicoXSolicitud(con,solicitud,pool);
	}
	
	/**
	 * Adición Sebastián
	 * Método usado en la revisión de la cuenta para la búsqueda
	 * avanzada de solicitudes
	 * @param con
	 * @param orden
	 * @param tipo
	 * @param fecha
	 * @param estado
	 * @param contrato
	 * @param opcion
	 * @return
	 */
	public HashMap busquedaRevisionSolicitudes(
			Connection con,int idCuenta,int orden,int tipo,String fecha,
			int estado,int contrato,boolean opcion)
	{
		return SqlBaseSolicitudesDao.busquedaRevisionSolicitudes(con,idCuenta,orden,tipo,fecha,estado,contrato,opcion);
	}
	
	/**
	 * Método implementado para insertar el Log de modificacion de tarifas
	 * en la funcionalidad de Revisión de la Cuenta
	 * @param con
	 * @param cuenta
	 * @param solicitud
	 * @param servicio
	 * @param articulo
	 * @param tipoCargo
	 * @param tarifaInicial
	 * @param tarifaFinal
	 * @param usuario
	 * @param institucion
	 * @param estado
	 * @return
	 */
	public int insertarLogRevisionCuenta(Connection con,int cuenta,int solicitud,int servicio,int articulo,int tipoCargo,
			double tarifaInicial,double tarifaFinal,String usuario,int institucion,String estado)
	{
		return SqlBaseSolicitudesDao.insertarLogRevisionCuenta(con,cuenta,solicitud,servicio,articulo,
			tipoCargo,tarifaInicial,tarifaFinal,usuario,institucion,estado,insertarLogRevisionCuentaStr);
	}
	
	/**
	 * Mètodo que consulta los procedimientos e interconsultas asociados al paciente, para
	 * mostrarlos en la ventana al ubicarse el usuario sobre la flecha de detalle en el listado de 
	 * solicitudes  de interconsultas y procedimientos
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public Collection consultarInterconsultasProcedimientos(Connection con, int codigoCuenta)
	{
		return SqlBaseSolicitudesDao.consultarInterconsultasProcedimientos (con, codigoCuenta);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @param codigoContrato
	 */
	public HashMap obtenerSolicitudesServicioIndicadorCapitado(Connection con, String idCuenta, int codigoContrato)
	{
		return SqlBaseSolicitudesDao.obtenerSolicitudesServicioIndicadorCapitado(con,idCuenta,codigoContrato);
	}
	

	/**
	 * 
	 */
	public int obtenerViaIngresoSolicitud(Connection con, int solicitud) throws BDException
	{
		return SqlBaseSolicitudesDao.obtenerViaIngresoSolicitud(con,solicitud);
	}

	@Override
	public boolean modificarEstadosOrdenesSolicitud(int numeroSolicitud,
			int estado, Connection con) {
		
		return SqlBaseSolicitudesDao.modificarEstadosOrdenesSolicitud(numeroSolicitud, estado, con);
	}
}