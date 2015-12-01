package util.odontologia;

import java.sql.Connection;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dto.odontologia.DtoCitaOdontologica;
import com.princetonsa.dto.odontologia.DtoInfoFechaUsuario;
import com.princetonsa.dto.odontologia.DtoLogPlanTratamiento;
import com.princetonsa.dto.odontologia.DtoLogPresupuestoOdontologico;
import com.princetonsa.dto.odontologia.DtoLogProcAutoEstados;
import com.princetonsa.dto.odontologia.DtoPlanTratamientoOdo;
import com.princetonsa.dto.odontologia.DtoPresupuestoOdontologico;
import com.princetonsa.mantenimiento.MantenimientoTablasBean;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.odontologia.CitaOdontologica;
import com.princetonsa.mundo.odontologia.PlanTratamiento;
import com.princetonsa.mundo.odontologia.PresupuestoOdontologico;
import com.princetonsa.mundo.odontologia.ProcesosAutomaticosOdontologia;

/**
 * ANEXO 885 PROCESOS AUTOMATICOS AXTUALIZACION DE ESTADOS ODONTOLOGICOS 
 * @author axioma
 *
 */
public  class ActualizacionEstadosOdontologicos extends Thread 
{
	/**
	 * ATRIBUTO PARA GUARDAR LOS DIAS DE VIGENCIA DEL PRESUPUESTO
	 * *
	 */
	public static int diasVigenciaPresupuesto;	
	public static int diasDiferencia;
	public static  final int INCREMENTO_UNO=1;
	private static  Logger logger = Logger.getLogger(ActualizacionEstadosOdontologicos.class);
	public static String usuarioModifica="";

	/**
	 * Enum con los mensajes de auditoria
	 */
	public static enum listaMensajesAuditoria 
	{
		mensajesProcesosAuto("No Ejecuta Procesos Automaticos de Estados Odontologicos. Falta el parametro EjecutarProcesoAutoActualizacionEstadosOdo"),
		mensajesValidaPresupuestoOdoContratado("No valida presupuesto Odontologico. Falta el parametro ValidaPresupuestoOdoContratado"),
		mensajesValidaMotivoCancelacion("No valida Motivo Cancelación Presupuesto. Falta el parametro ValidaMotivoCancelacionPresupuesto"),
		mensajesParametroTiempo("No Ejecuta Procesos Automaticos de Estados Odontologicos. Falta el parametro Tiempo");
		/**
		 * constructor
		 * @param mensaje
		 */
		listaMensajesAuditoria(String mensaje) {
			this.mensaje = mensaje;
		}
		
		private String mensaje;
		
		/**
		 * @return the mensaje
		 */
		public String getMensaje() {
			return mensaje;
		}

		/**
		 * @param mensaje the mensaje to set
		 */
		public void setMensaje(String mensaje) {
			this.mensaje = mensaje;
		}
	};
	
	
	/**
	  * LISTA LOS CODIGO DE LOS PACIENTES PARA ASIGNAR CITAS ODONTOLOGICAS
	  */
	private static ArrayList<DtoPresupuestoOdontologico> listaPresuPacientGeneraCita = new ArrayList<DtoPresupuestoOdontologico>();
	
	/**
	 * LISTA DE PRESUPUESTOS
	 */
	private static ArrayList<DtoPresupuestoOdontologico> listaPresupuestos = new ArrayList<DtoPresupuestoOdontologico>();
    
	/**
	 * 
	 */
	public static ArrayList<InfoServicios> arrayServicios = new ArrayList<InfoServicios>();
	
	 /**
	  * METODO PARA CAMBIAR LOS ESTADOS DEL PLAN DEL PRESUPUESTO ODONTOLOGICO Y EL PLAN DE TRATAMIENTO 
	  * @param institucion
	  * @return
	  */
 	public static  boolean actualizarEstados(int institucion)
 	{
 		logger11();
 		
 		//1. INICIAMOS LA TRANSACCION
 		Connection con = abriConexion();
		
 		//2. VALIDACIONES GENERALES
 		//2.1 verificamos que el valor x defecto esté activo
 		//2.2 validamos el parametro presupuesto odo contratado
 		//2.3 validamos que este parametrizado el tiempo
 		if( ValoresPorDefecto.getEjecutarProcesoAutoActualizacionEstadosOdo(institucion).equals(ConstantesBD.acronimoSi) ) 
 		{
 			if( ValoresPorDefecto.getValidaPresupuestoOdoContratado (institucion).equals(ConstantesBD.acronimoSi) )
		 	{
 				if(Utilidades.convertirAEntero(ValoresPorDefecto.getTiempoVigenciaPresupuestoOdo(institucion))>0 
 				&& Utilidades.convertirAEntero(ValoresPorDefecto.getTiempoMaxEsperaInactivarPresupuestoOdoSuspTemp(institucion))>0)
 				{	
 					//3. FLUJO PRESUPUESTOS
	 				accionEmpezarProcesoAutomaticoPresupuesto( con,  institucion);		
	 				asignacionCitasPacientesPresupuestoCancelado(institucion, con);
 				}
 				else
 				{
 					accionGuardarLogAuditoria(con,  institucion, listaMensajesAuditoria.mensajesParametroTiempo.mensaje);
 				}	
 				//4. FLUJO PLAN TRATAMIENTO
 				accionPlanTratamientoEstadoTerminado(institucion, con);
 				
 				if(Utilidades.convertirAEntero(ValoresPorDefecto.getMaximoTiempoSinEvolucionarParaInactivarPlanTratamiento(institucion))>0)
 				{
 					accionInactivarPlanTratamientoActivoYEnProceso(institucion, con);
 				}
 				else
 				{
 					accionGuardarLogAuditoria(con,  institucion, listaMensajesAuditoria.mensajesParametroTiempo.mensaje);
 				}
 				if(Utilidades.convertirAEntero(ValoresPorDefecto.getTiempoMaxEsperaInactivarPresupuestoOdoSuspTemp(institucion))>0)
 				{
 					accionInactivarPlanTratamientoSuspendidoTemporalmente(institucion, con);
 				}
 				else
 				{
 					accionGuardarLogAuditoria(con,  institucion, listaMensajesAuditoria.mensajesParametroTiempo.mensaje);
 				}
 				
 				logger.info("\n\n\n TERMINO DE MODIFICAR EL PLAN DE TRATAMIENTO \n\n\n\n");
 			}
 			else
 			{
 				accionGuardarLogAuditoria(con,  institucion, listaMensajesAuditoria.mensajesValidaPresupuestoOdoContratado.mensaje);
 			}
 		}
 		else
 		{
 			accionGuardarLogAuditoria(con, institucion, listaMensajesAuditoria.mensajesProcesosAuto.mensaje);
 		}
 		
 		//CERRAMOS LOS INGRESOS
 		if(!ProcesosAutomaticosOdontologia.cerrarIngresosOdontologicos(con, institucion))
 		{
 			UtilidadBD.abortarTransaccion(con);
 			UtilidadBD.closeConnection(con);
 			return false;
 		}
 		
 		
 		cerrarConexion(con);
		return true;
 	}

 	/**
	 * ACCION EMPEZAR PROCESOS AUTOMATICOS
	 * @param con
	 * @param institucion
	 */
	private static void accionEmpezarProcesoAutomaticoPresupuesto(Connection con, int institucion)
	{
		logger1();
		listaPresuPacientGeneraCita = new ArrayList<DtoPresupuestoOdontologico>(); //limpiar lista
		
		//1. CARGAMOS LOS PRESUPUESTOS
		accionCargarPresupuestoActivosPrecontratados(con, institucion);
		
		for (DtoPresupuestoOdontologico presupuesto : listaPresupuestos)
		{
			logger.info("\n\n\n\n ENTRA A CAMBIAR ESTADO");
			DtoPresupuestoOdontologico dtoPresupuestoLog = (DtoPresupuestoOdontologico)presupuesto.clone();

			// SE INACTIVAR LOS PRESUPUSTO ACTIVOS Y PRECONTRADOS
			accionInactivarPrespuestosActivosPrecontrados(con, presupuesto,	dtoPresupuestoLog);
		}
		
		accionCargarPresupuestoSuspendidosTemp(con, institucion);
		
		for (DtoPresupuestoOdontologico presupuesto : listaPresupuestos)
		{
			logger.info("\n\n\n\n ENTRA A CAMBIAR ESTADO");
			DtoPresupuestoOdontologico dtoPresupuestoLog = (DtoPresupuestoOdontologico)presupuesto.clone();

			//  SE CANCELAN PRESUPUESTO CONTRATADO INDICATIVO SUSPENDIDO
			accionCancelarPresupuestoContratadoIndicativoSuspendido(con, institucion, presupuesto, dtoPresupuestoLog);
		}	

		accionCargarPresupuestoContratado(con, institucion);
		
		for (DtoPresupuestoOdontologico presupuesto : listaPresupuestos)
		{
			logger.info("\n\n\n\n ENTRA A CAMBIAR ESTADO");
			DtoPresupuestoOdontologico dtoPresupuestoLog = (DtoPresupuestoOdontologico)presupuesto.clone();
			// SE TERMINAR PRESUPUESTO CONTRATADO CONTRATADO
			accionTerminarPresupuestoContratadoContratado(con, institucion,	presupuesto, dtoPresupuestoLog);
		}	
		
	}
 	
	/**
	 * ACCION CARGAR LOS PRESUPUESTOS
	 *
	 * @param institucion
	 */
	private static void accionCargarPresupuestoActivosPrecontratados(Connection con,  int institucion) 
	{
		DtoPresupuestoOdontologico dtoWhere = new DtoPresupuestoOdontologico();
		dtoWhere.setInstitucion(institucion);
		dtoWhere.getUsuarioModifica().setFechaModifica(UtilidadFecha.incrementarDiasAFecha(UtilidadFecha.getFechaActual(), Utilidades.convertirAEntero(ValoresPorDefecto.getTiempoVigenciaPresupuestoOdo(institucion))*-1, false));
		
		logger.info("FECHA CALCULADA -->"+dtoWhere.getUsuarioModifica().getFechaModificaFromatoBD());
		
		ArrayList<String> listaEstado= new ArrayList<String>();
		listaEstado.add(ConstantesIntegridadDominio.acronimoEstadoActivo);
		listaEstado.add(ConstantesIntegridadDominio.acronimoPrecontratado);
		listaPresupuestos = PresupuestoOdontologico.cargarPresupuesto(con, dtoWhere,listaEstado, false);
	}

	/**
	 * ACCION CARGAR LOS PRESUPUESTOS
	 *
	 * @param institucion
	 */
	private static void accionCargarPresupuestoSuspendidosTemp(Connection con,  int institucion) 
	{
		DtoPresupuestoOdontologico dtoWhere = new DtoPresupuestoOdontologico();
		dtoWhere.setInstitucion(institucion);
		dtoWhere.getUsuarioModifica().setFechaModifica(UtilidadFecha.incrementarDiasAFecha(UtilidadFecha.getFechaActual(), Utilidades.convertirAEntero(ValoresPorDefecto.getTiempoMaxEsperaInactivarPresupuestoOdoSuspTemp(institucion))*-1, false));
		
		logger.info("FECHA CALCULADA -->"+dtoWhere.getUsuarioModifica().getFechaModificaFromatoBD());
		
		ArrayList<String> listaEstado= new ArrayList<String>();
		listaEstado.add(ConstantesIntegridadDominio.acronimoContratadoSuspendidoTemporalmente);
		//listaEstado.add(ConstantesIntegridadDominio.acronimoContratadoContratado);
		listaPresupuestos = PresupuestoOdontologico.cargarPresupuesto(con, dtoWhere,listaEstado, false);
	}
	
	/**
	 * ACCION CARGAR LOS PRESUPUESTOS
	 *
	 * @param institucion
	 */
	private static void accionCargarPresupuestoContratado(Connection con,  int institucion) 
	{
		DtoPresupuestoOdontologico dtoWhere = new DtoPresupuestoOdontologico();
		dtoWhere.setInstitucion(institucion);
		ArrayList<String> listaEstado= new ArrayList<String>();
		listaEstado.add(ConstantesIntegridadDominio.acronimoContratadoContratado);
		//listaEstado.add(ConstantesIntegridadDominio.acronimoContratadoContratado);
		listaPresupuestos = PresupuestoOdontologico.cargarPresupuesto(con, dtoWhere,listaEstado, false);
	}
	
	/**
	 * ACCION INACTIVAR PRESUPUESTO ACTIVOS PRECONTRATADOS
	 * @param con
	 * @param presupuesto
	 * @param dtoPresupuestoLog
	 */
	private static void accionInactivarPrespuestosActivosPrecontrados(	Connection con, DtoPresupuestoOdontologico presupuesto,		DtoPresupuestoOdontologico dtoPresupuestoLog) 
	{
		if(presupuesto.getEstado().equals(ConstantesIntegridadDominio.acronimoEstadoActivo) || (presupuesto.getEstado().equals(ConstantesIntegridadDominio.acronimoPrecontratado) ))
		{
			logger.info("\n\n\n\n\n\n********************************************************************************************************************");
			logger.info("\n\n	INACTIVAR PRESUPUESTO	\n");
			logger.info("\n 	ESTADO PRESUPUESTO ="+presupuesto.getEstado()+"\n\n");
			presupuesto.setEstado(ConstantesIntegridadDominio.acronimoInactivo);
			presupuesto.getFechaUsuarioGenera().setFechaModifica(UtilidadFecha.getFechaActual()); 
			presupuesto.getFechaUsuarioGenera().setHoraModifica(UtilidadFecha.getHoraActual());
			logger.info(" \n\n\n\n\n MODIFICAR PRESUPESTO  A INACTIVO \n\n\n\n");
			if(PresupuestoOdontologico.modificarPresupuesto(presupuesto, con))
			{
				Log4JManager.info("SE MODIFICA EL PRESUPUESTO EXITOSAMENTE");
			}else{
				logger.error("no guarda log");
				UtilidadBD.abortarTransaccion(con);
				UtilidadBD.closeConnection(con);
			}
			logger.info("*******************************************************************************************************************\n\n\n\n\n\n");
		}
	}

	/**
	 * ACCION CANCELAR PLAN DE TRATAMIENTO CONTRATADO
	 * @param con
	 * @param institucion
	 * @param presupuesto
	 * @param dtoPresupuestoLog
	 */
	private static void accionCancelarPresupuestoContratadoIndicativoSuspendido(Connection con, int institucion,DtoPresupuestoOdontologico presupuesto,			DtoPresupuestoOdontologico dtoPresupuestoLog) 
	{
		if(presupuesto.getEstado().equals(ConstantesIntegridadDominio.acronimoContratadoSuspendidoTemporalmente))
		{
			if(ValoresPorDefecto.getMotivoCancelacionPresupuestoSuspendidoTemp(presupuesto.getInstitucion()) != null && ! UtilidadTexto.isEmpty(ValoresPorDefecto.getMotivoCancelacionPresupuestoSuspendidoTemp(presupuesto.getInstitucion())))
			{ 
				logger.info("\n\n\n\n\n***********************************************************************************************************");
				logger.info("\n\n\n\n\n\n\n    CANCELAR  CONTRATO PRESUPUESTO \n\n\n\n\n\n\n");
				presupuesto.setEstado(ConstantesIntegridadDominio.acronimoContratadoCancelado);					  
				presupuesto.getMotivo().setCodigo(ValoresPorDefecto.getMotivoCancelacionPresupuestoSuspendidoTemp(presupuesto.getInstitucion()));
				presupuesto.getFechaUsuarioGenera().setFechaModifica(UtilidadFecha.getFechaActual());
				presupuesto.getFechaUsuarioGenera().setHoraModifica(UtilidadFecha.getHoraActual());

				logger.info("MODIFICAR PRESUPUESTO A CANCELADO \n\n\n\n");

				if(PresupuestoOdontologico.modificarPresupuesto(presupuesto, con))
				{
					PresupuestoOdontologico.reversarAnticiposPresupuestoContratado(con, presupuesto.getCodigoPK());
					Log4JManager.info("SE MODIFICA EXITOSAMENTE EL PRESUPUESTO");						
					
				}else{
					logger.error("no guarda log");
					UtilidadBD.abortarTransaccion(con);
					UtilidadBD.closeConnection(con);
				}
				logger.info("***********************************************************************************************************\n\n\n");
			}
			else
			{
				accionGuardarLogAuditoria(con, institucion,listaMensajesAuditoria.mensajesValidaMotivoCancelacion.mensaje);	 
			}
		}
	}

	/**
	 * ACCION TERMINAR PRESUPUESTO CONTRATADO CONTRATADO
	 * @param con
	 * @param institucion
	 * @param presupuesto
	 * @param dtoPresupuestoLog
	 */
	private static void accionTerminarPresupuestoContratadoContratado(	Connection con, int institucion, DtoPresupuestoOdontologico presupuesto, DtoPresupuestoOdontologico dtoPresupuestoLog) 
	{
		if(presupuesto.getEstado().equals(ConstantesIntegridadDominio.acronimoContratadoContratado))
		{
			//ACCION TERMINAR PRESUPUESTO CONTRATADO 
			logger.info("\n\n\n\n\n*************************************************************************************************");
			if(PresupuestoOdontologico.puedoTerminarPresupuesto(ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(institucion).equals(ConstantesBD.acronimoSi) ,presupuesto.getCodigoPK()))
			{
				logger.info("\n\n\n\n TERMINAR PLAN DE TRATAMIENTO  APLICA PARA PROGRAMAS");
				modificarPresupuesto(con, presupuesto, dtoPresupuestoLog);
				logger.info("ADICIONAR AL LISTA PRESUPUESTO");
				listaPresuPacientGeneraCita.add(presupuesto); // ADICIONAR A LOS PACIENTES QUE NECESITAN ASIGNACION DE CITAS 
			}
			logger.info("*************************************************************************************************\n\n\n\n");
		}
	}
	
	
 	/**
 	 * 
 	 * @param institucion
 	 * @param con
 	 */
	private static void accionInactivarPlanTratamientoActivoYEnProceso(int institucion,	Connection con) 
	{
		ArrayList<String> estadosPlan = new ArrayList<String>();
		ArrayList<DtoPresupuestoOdontologico> listPresupuesto = new ArrayList<DtoPresupuestoOdontologico>();
		estadosPlan.add(ConstantesIntegridadDominio.acronimoEstadoActivo);
		estadosPlan.add(ConstantesIntegridadDominio.acronimoEnProceso);
		
		ArrayList<String>estadosPresupuesto = new ArrayList<String>();
		estadosPresupuesto.add(ConstantesIntegridadDominio.acronimoEstadoActivo);
		estadosPresupuesto.add(ConstantesIntegridadDominio.acronimoContratadoContratado);


		/**
		 * CARGAR PLAN DE TRATAMIENTO CON LOS ESTADOS RESPECTIVOS 
		 */
		DtoPlanTratamientoOdo parametros = new DtoPlanTratamientoOdo();
		parametros.setInstitucion(institucion);
		parametros.getUsuarioModifica().setFechaModifica(UtilidadFecha.incrementarDiasAFecha(UtilidadFecha.getFechaActual(), Utilidades.convertirAEntero(ValoresPorDefecto.getMaximoTiempoSinEvolucionarParaInactivarPlanTratamiento(institucion))*-1, false));
	
		logger.info("VALIDAR DIAS  PLAN DE TRATAMIENTO");
		logger.info("\n\n FECHA PLAN TRATAMIENTO--------------"+ parametros.getUsuarioModifica().getFechaModificaFromatoBD());

		ArrayList<DtoPlanTratamientoOdo> listaPlan = new ArrayList<DtoPlanTratamientoOdo>();
		listaPlan=	 PlanTratamiento.consultarPlanTratamiento(con, parametros, estadosPlan);

		logger.info("*************************RECORRER PLAN DE TRATAMIENTO************************************");
		for(DtoPlanTratamientoOdo info:  listaPlan)
		{
			DtoPlanTratamientoOdo dto = new DtoPlanTratamientoOdo();
			dto.setCodigoPk(info.getCodigoPk());
			dto.setEstado(ConstantesIntegridadDominio.acronimoInactivo);
			dto.getUsuarioModifica().setUsuarioModifica("axioma");
			dto.getUsuarioModifica().setFechaModifica(UtilidadFecha.getFechaActual());
			dto.getUsuarioModifica().setHoraModifica(UtilidadFecha.getHoraActual());
			/**
			 * 
			 */
			logger.info("\n\n\n\n INACTIVA PLAN DE TRATAMIENTO ");
			if(!PlanTratamiento.inactivarPlan(dto, con))
			{
				UtilidadBD.abortarTransaccion(con);
				UtilidadBD.closeConnection(con);
			}
			
			DtoPresupuestoOdontologico dtoPresupuesto  = new DtoPresupuestoOdontologico();
			dtoPresupuesto.setPlanTratamiento(info.getCodigoPk());

			/**
			 * 
			 * CARGAR PRESUPUESTO PARA EL RESPECTIVO PLAN DE TRATAMIENDO
			 *  
			 */
			listPresupuesto=PresupuestoOdontologico.cargarPresupuesto(con,dtoPresupuesto, estadosPresupuesto , Boolean.FALSE);

			/**
			 * 
			 * INACTIVAR DE  PRESUPUESTO 
			 * 
			 */
			if(listPresupuesto.size()>0)
			{
				logger.info("\n\n\n\n\n EXISTE DATOSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
				for(DtoPresupuestoOdontologico presupuesto: listPresupuesto)
				{
					logger.info("ESTADO---------------->"+presupuesto.getEstado());
				}


				for(DtoPresupuestoOdontologico presupuesto: listPresupuesto)
				{
					presupuesto.setEstado(ConstantesIntegridadDominio.acronimoInactivo);
					presupuesto.getFechaUsuarioGenera().setFechaModifica(UtilidadFecha.getFechaActual());
					presupuesto.getFechaUsuarioGenera().setHoraModifica(UtilidadFecha.getHoraActual());
					DtoPresupuestoOdontologico dtoPresupuestoLog = (DtoPresupuestoOdontologico)presupuesto.clone();
					if(PresupuestoOdontologico.modificarPresupuesto(presupuesto, con))
					{
						Log4JManager.info("SE MODIFICA EXITOSAMENTE EL PRESUPUESTO");
					}else{
						logger.error("no guarda log");
						UtilidadBD.abortarTransaccion(con);
						UtilidadBD.closeConnection(con);
					}
				}
			}
		}
	}

	/**
 	 * 
 	 * @param institucion
 	 * @param con
 	 */
	private static void accionInactivarPlanTratamientoSuspendidoTemporalmente(int institucion,	Connection con) 
	{
		ArrayList<String> estadosPlan = new ArrayList<String>();
		estadosPlan.add(ConstantesIntegridadDominio.acronimoSuspendidoTemporalmente);
		
		/**
		 * CARGAR PLAN DE TRATAMIENTO CON LOS ESTADOS RESPECTIVOS 
		 */
		DtoPlanTratamientoOdo parametros = new DtoPlanTratamientoOdo();
		parametros.setInstitucion(institucion);
		parametros.getUsuarioModifica().setFechaModifica(UtilidadFecha.incrementarDiasAFecha(UtilidadFecha.getFechaActual(), Utilidades.convertirAEntero(ValoresPorDefecto.getTiempoMaxEsperaInactivarPresupuestoOdoSuspTemp(institucion))*-1, false));
	
		logger.info("VALIDAR DIAS  PLAN DE TRATAMIENTO");
		logger.info("\n\n FECHA PLAN TRATAMIENTO--------------"+ parametros.getUsuarioModifica().getFechaModificaFromatoBD());

		ArrayList<DtoPlanTratamientoOdo> listaPlan = new ArrayList<DtoPlanTratamientoOdo>();
		listaPlan=	 PlanTratamiento.consultarPlanTratamiento(con, parametros, estadosPlan);
		
		logger.info("*************************RECORRER PLAN DE TRATAMIENTO************************************");
		for(DtoPlanTratamientoOdo info:  listaPlan)
		{
			DtoPlanTratamientoOdo dto = new DtoPlanTratamientoOdo();
			dto.setCodigoPk(info.getCodigoPk());
			dto.setEstado(ConstantesIntegridadDominio.acronimoInactivo);
			dto.getUsuarioModifica().setUsuarioModifica("axioma");
			dto.getUsuarioModifica().setFechaModifica(UtilidadFecha.getFechaActual());
			dto.getUsuarioModifica().setHoraModifica(UtilidadFecha.getHoraActual());
			/**
			 * 
			 */
			logger.info("\n\n\n\n INACTIVA PLAN DE TRATAMIENTO ");
			if(!PlanTratamiento.inactivarPlan(dto, con))
			{
				UtilidadBD.abortarTransaccion(con);
				UtilidadBD.closeConnection(con);
			}
		}	
	}	
	
	
 	/**
 	 * CAMBIAR EL ESTADO DEL PLAN DE TRATAMIENTO A TERMINADO 
 	 * CAMBIA SI Y SOLO: APLICA ESTADOS: TERMINADO, CANCELADO, NO AUTORIZADO O EXCLUIDO Y DEBE TERNER AL MENOS UN ESTADO TERMINADO  
 	 * APLICA PARA  
 	 * @param institucion
 	 * @param con
 	 */
	private static void accionPlanTratamientoEstadoTerminado(int institucion,Connection con) {
		logger.info("************************************MODIFICANDO PLAN DE TRATAMIENTO*******************************************************");
 		logger.info("**************************************************************************************************************************");
 		
 		boolean manejaProgramas=ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(institucion).equals(ConstantesBD.acronimoSi);
 		
		PlanTratamiento.modificarPlan(institucion, con, manejaProgramas);	
	}


 	/**
 	 * 
 	 */
	private static void logger11() {
		logger.info("*********************************************************************************************");
 		logger.info("*********************************************************************************************");
 		logger.info("			HILO DE ACTUALIZACION DE ESTADOS 								   				");
 		logger.info("*********************************************************************************************");
	}


 	
 	/**
 	 * ABRIR TRANSACCION
 	 * @param con
 	 */
	private static void cerrarConexion(Connection con) {
		logger.info("realiza todo 100%");
		//UtilidadBD.abortarTransaccion(con);
		UtilidadBD.finalizarTransaccion(con);
		UtilidadBD.closeConnection(con);
	}

	/**
	 * CERRAR TRANSACCION
	 * @return
	 */
	private static Connection abriConexion() {
		Connection con = UtilidadBD.abrirConexion();
		UtilidadBD.iniciarTransaccion(con);
		return con;
	}

 	/**
 	 * ACCION GUARDAR LOG PROCESO AUTOMATICO getValidaPresupuestoOdoContratado
 	 * @param con
 	 * @param institucion
 	 */
 	private static void accionGuardarLogAuditoria(Connection con, int institucion, String mensaje)
 	{
		DtoLogProcAutoEstados dtoNuevo = new DtoLogProcAutoEstados();
		dtoNuevo.setInconsistencia(mensaje);
		dtoNuevo.setFecha(UtilidadFecha.getFechaActual());
		dtoNuevo.setHora(UtilidadFecha.getHoraActual());
		dtoNuevo.setInstitucion(institucion);
		ProcesosAutomaticosOdontologia.guardarProcAutoEstados(dtoNuevo , con);
 	}



 	/**
 	 * ASIGNACION DE CITAS A LOS PACIENTES QUE SE LES TERMINO EL PRESUPUESTO
 	 * @param institucion
 	 * @param con
 	 */
	private static void asignacionCitasPacientesPresupuestoCancelado(int institucion, Connection con) 
	{
	
		logger.info("\n\n\n\n");
		logger.info(" PROCESO DE INSERCCION DE CITAS ");
		logger.info("////////////////////////////////////////////////////////////////////////////////////////////////////");
		
		if( ValoresPorDefecto.getRequeridoProgramarCitaControlAlTerminarPresupuestoOdon(institucion).equals(ConstantesBD.acronimoSi))
		{
			logger.info("\n\n\n\n");
			logger.info("	REQUIERE PROGRAMA CITAS CONTROL AL TERMINAR PRESUPUSTO siiiiiiiiiiiiiiiiiiiiiii");
			
			String tmpFecha=ValoresPorDefecto.getTiempoDiasParaAgendarCitaControlAlTerminarPresupuestoOdon(institucion);
		
			logger.info("\n\n\n   ValoresPorDefecto.getTiempoDiasParaAgendarCitaControlAlTerminarPresupuestoOdon(institucion)  ==="+tmpFecha);
		
			
			int numeroDias= UtilidadFecha.numeroDiasEntreFechas(tmpFecha,  UtilidadFecha.getFechaActual());
			
			
			logger.info("\n NUMERO DE DIAS ENTRE FECHA============>>>"+numeroDias);
			String nuevaFecha=UtilidadFecha.incrementarDiasAFecha(UtilidadFecha.getFechaActual(), numeroDias, Boolean.FALSE);
			String numeroServicio= ValoresPorDefecto.getRequeridoProgramarCitaControlAlTerminarPresupuestoOdonServicio(institucion);
		
			
			if(Utilidades.convertirADouble(numeroServicio)>0)
			{	
				logger.info(" \n\n\n------------>>"+numeroServicio);
				
				// 	EXISTE DIA FESTIVO
				arrayServicios = new ArrayList<InfoServicios>();
				InfoServicios newServicio = new InfoServicios();
				newServicio.setServicio(new InfoDatosInt(Utilidades.convertirAEntero(numeroServicio)));
				arrayServicios.add(newServicio);
				
				
				//BORRAR
				 
				if(listaPresuPacientGeneraCita.size()>0)
				{
					logger.info("\n\n\nExiste lista presupuesto Genere Cita");
				}
				else
				{
					logger.info("\n\n\nNO Existe lista para genera cita");
				}
				
				for(DtoPresupuestoOdontologico listPresu: listaPresuPacientGeneraCita) //LISTA PARA LOS PACIENTES A LOS CUALES SE LES ACABA DE ACTUALIZAR EL ESTADO DEL PRESUPUSTO EN TERMINADO 
				{
					 
					 nuevaFecha = accionInsertarCita(con, nuevaFecha,arrayServicios, listPresu);
				}// FIN FOR
			}	
		}
		else
		{
			logger.info("\n\n\n\n");
			logger.info("	REQUIERE PROGRAMA CITAS CONTROL AL TERMINAR PRESUPUSTO Noooooooooooooooooooooooooooooooooooooo");
		}
	
	}



	/**
	 * ACCION INSERTAR CITA
	 * @param con
	 * @param nuevaFecha
	 * @param arrayServicios
	 * @param listPresu
	 * @return
	 */
	private static String accionInsertarCita(Connection con, String nuevaFecha,	ArrayList<InfoServicios> arrayServicios, DtoPresupuestoOdontologico listPresu) 
	{
		if(MantenimientoTablasBean.existeExcepcionAgenda(con, nuevaFecha, String.valueOf(listPresu.getCentroAtencion().getCodigo()) ))
		{
			logger.info("EXISTE DIA FESTIVO");
		 	nuevaFecha=UtilidadFecha.incrementarDiasAFecha(nuevaFecha, INCREMENTO_UNO, Boolean.FALSE);
		}// SI ES FESTIVO

		DtoCitaOdontologica dtoCita = new DtoCitaOdontologica();
		dtoCita.setCodigoPaciente(listPresu.getCodigoPaciente().getCodigo());
		dtoCita.setEstado(ConstantesIntegridadDominio.acronimoProgramado);
		dtoCita.setFechaRegistra(nuevaFecha);/// falta por defini????????????'''
		
		
		
		/*
		 * XPLANER 161116
		 * EL PROFESIONAL DE LA SALUD QUE FINALIZO EL TRATAMIENTO
		 */
		usuarioModifica="";
		usuarioModifica= PlanTratamiento.cargarNombreUsuarioModificaPlanTratamiento(listPresu.getCodigoPK());
		
		
		logger.info("\n\n\n INSERTARNDO CITA");
		if( CitaOdontologica.insertarProximaCitaOdontologia(con, arrayServicios, nuevaFecha, listPresu.getCodigoPaciente().getCodigo(), usuarioModifica, ConstantesIntegridadDominio.acronimoControlCitaOdon, listPresu.getCentroCosto())<=0)
		{
			logger.error("no guarda log");
			UtilidadBD.abortarTransaccion(con);
			UtilidadBD.closeConnection(con);
		}
		
		return nuevaFecha;
	}

	////////////<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>< loGGGER
	/**
	 * 
	 */
	private static void logger1() 
	{
		logger.info("\n\n\n\n\n\n\n\n\n ******************************************	" +
				"CARGAR PRESUPUESTO \n\n\n\n\n\n");
		logger.info("\n\n\n\n\n\n\n\n\n ******************************************	" +
		"BUSCAR INFORMACION PRESUPUESTO \n\n\n\n\n\n");
	}

	/**
	 * 
	 * @param con
	 * @param presupuesto
	 * @param dtoPresupuestoLog
	 */
	private static void modificarPresupuesto(Connection con, DtoPresupuestoOdontologico presupuesto,DtoPresupuestoOdontologico dtoPresupuestoLog) {
		
		logger.info("\n\n\n\n\n ACRONIMO CONTRATADO TERMINADO\n\n\n\n");
		presupuesto.setEstado(ConstantesIntegridadDominio.acronimoContratadoTerminado);
		presupuesto.getFechaUsuarioGenera().setFechaModifica(UtilidadFecha.getFechaActual());
		presupuesto.getFechaUsuarioGenera().setHoraModifica(UtilidadFecha.getHoraActual());
  
		if(PresupuestoOdontologico.modificarPresupuesto(presupuesto, con))
		{
			Log4JManager.info("SE MODIFICA EXITOSAMENTE EL PRESUPUESTO");
		}else{
			logger.error("no guarda log");
			UtilidadBD.abortarTransaccion(con);
			UtilidadBD.closeConnection(con);
		}
	}

 	/**
 	 * 
 	 * @param plan
 	 * @param usuario
 	 * @return
 	 */
	public static DtoLogPlanTratamiento llenarLogPlan(DtoPlanTratamientoOdo plan  , UsuarioBasico usuario)
	{
		DtoLogPlanTratamiento dtoLogPlan = new DtoLogPlanTratamiento();
		dtoLogPlan.setPlanTratamiento(plan.getCodigoPk().doubleValue());
		dtoLogPlan.setEstado(plan.getEstado());
		dtoLogPlan.setModificacion(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
		dtoLogPlan.setMotivo(new InfoDatosInt(plan.getMotivo(), ""));
		dtoLogPlan.setPorConfirmar(ConstantesBD.acronimoSi);
		dtoLogPlan.setEstado(plan.getEstado());
		return dtoLogPlan;
	}
 	
	/**
	 * 
	 * @param presupuesto
	 * @return
	 */
	public static DtoLogPresupuestoOdontologico llenarLogPresupuesto(DtoPresupuestoOdontologico presupuesto)
	{
		DtoLogPresupuestoOdontologico dtoLog= new DtoLogPresupuestoOdontologico();
		dtoLog.setCodigoPresupuesto(presupuesto.getCodigoPK());
		dtoLog.setEspecialidad(presupuesto.getEspecialidad());
		dtoLog.setUsuarioModifica(presupuesto.getUsuarioModifica());
		dtoLog.setEstado(presupuesto.getEstado());
		dtoLog.setMotivo(presupuesto.getMotivo());
		return dtoLog;
	}
}