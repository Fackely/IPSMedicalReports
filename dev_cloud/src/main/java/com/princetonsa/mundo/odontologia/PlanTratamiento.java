package com.princetonsa.mundo.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosDouble;
import util.InfoDatosInt;
import util.UtilidadBD;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.odontologia.InfoDetallePlanTramiento;
import util.odontologia.InfoHallazgoSuperficie;
import util.odontologia.InfoIngresoPlanTratamiento;
import util.odontologia.InfoNumSuperficiesPresupuesto;
import util.odontologia.InfoOdontograma;
import util.odontologia.InfoPlanTratamiento;
import util.odontologia.InfoProgramaServicioPlan;
import util.odontologia.InfoServicios;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.sqlbase.odontologia.SqlBasePlanTratamientoDao;
import com.princetonsa.dto.odontologia.DtoDetallePlanTratamiento;
import com.princetonsa.dto.odontologia.DtoLogDetPlanTratamiento;
import com.princetonsa.dto.odontologia.DtoLogPlanTratamiento;
import com.princetonsa.dto.odontologia.DtoLogPrespuesto;
import com.princetonsa.dto.odontologia.DtoLogProgServPlant;
import com.princetonsa.dto.odontologia.DtoOdontograma;
import com.princetonsa.dto.odontologia.DtoPlanTratamientoOdo;
import com.princetonsa.dto.odontologia.DtoPresupuestoOdontologico;
import com.princetonsa.dto.odontologia.DtoProgHallazgoPieza;
import com.princetonsa.dto.odontologia.DtoProgramasServiciosPlanT;
import com.princetonsa.dto.odontologia.DtoSectorSuperficieCuadrante;
import com.princetonsa.dto.odontologia.DtoServArtIncCitaOdo;
import com.princetonsa.dto.odontologia.DtoSuperficiesPorPrograma;



/***
 * 
 * @author axioma
 *
 */
public class PlanTratamiento {
	
	private static Logger logger = Logger.getLogger(PlanTratamiento.class);
	
	private static final  String  COLOR_CITA_SERVICIO="#81BEF7";
	

	/**
	 * 
	 * @param ingreso
	 * @param utilizaProgramas
	 * @return
	 */
	public static InfoPlanTratamiento obtenerPlanTratamientoInicialPresupuesto(int ingreso, boolean utilizaProgramas)
	{
		InfoPlanTratamiento planTratamiento= new InfoPlanTratamiento();
		ArrayList<String> estadosPlanTratamiento= new ArrayList<String>();
		
		String porConfirmar=ConstantesBD.acronimoNo;
		//pilas no Estoy Seguro HAY QUE MIRAR EL LOGPLAN DE TRATAMIENTO; ES DECIR BUSCAR EN LAS DOS TABLAS
		//***************************************************************************************************
				
		/* Ya no se envian los estados porque no importa el estado del plan de tratamiento
		estadosPlanTratamiento.add(ConstantesIntegridadDominio.acronimoEstadoActivo);
		estadosPlanTratamiento.add(ConstantesIntegridadDominio.acronimoEstadoEnProceso);
		estadosPlanTratamiento.add(ConstantesIntegridadDominio.acronimoSuspendidoTemporalmente);
		*/
		
		//************1.primero obtenemos el codigo del plan de tratamiento
		
		planTratamiento.setCodigoPk(obtenerUltimoCodigoPlanTratamiento(ingreso, estadosPlanTratamiento, porConfirmar));
		
		//************validamos que cargue el codigo
		
		if(planTratamiento.getCodigoPk().doubleValue()<=0)
		{
			logger.warn("NO EXISTE PLAN DE TRATAMIENTO!!!!!!!!!!!");
			return null; 
		}
		
		cargarDetallePlanTratamiento(utilizaProgramas, planTratamiento,	porConfirmar, BigDecimal.ZERO);
		
		return planTratamiento;
	}

	/**
	 * @param utilizaProgramas
	 * @param planTratamiento
	 * @param porConfirmar
	 */
	private static void cargarDetallePlanTratamiento(	boolean utilizaProgramas,
														InfoPlanTratamiento planTratamiento, 
														String porConfirmar,
														BigDecimal codigoPkPresupuesto) 
	{
		//2.******************CARGAR EL DETALLE, obtenemos las piezas del plan de tratamiento
		ArrayList<InfoDatosInt> arrayPiezas= obtenerPiezas(planTratamiento.getCodigoPk(), ConstantesIntegridadDominio.acronimoDetalle, porConfirmar);
		ArrayList<String> estadosProgramasOservicios= new ArrayList<String>();
		estadosProgramasOservicios.add(ConstantesIntegridadDominio.acronimoEstadoPendiente);
		
		//2.1 ****************************** iteramos las piezas para obtener los hallazgos ligados a la pieza
		//y los seteamos a los detalles
		for(InfoDatosInt pieza: arrayPiezas)
		{
			InfoDetallePlanTramiento detallePlan= new InfoDetallePlanTramiento();
			detallePlan.setPieza(pieza);
			
			//este metodo en el sql base carga los programas o servicios
			detallePlan.setDetalleSuperficie(obtenerHallazgosSuperficies(planTratamiento.getCodigoPk(), pieza.getCodigo(), ConstantesIntegridadDominio.acronimoDetalle, estadosProgramasOservicios, utilizaProgramas, porConfirmar, codigoPkPresupuesto));
			planTratamiento.getSeccionHallazgosDetalle().add(detallePlan);
		}	
		
		
		//3. CARGAR DETALLE OTROS
		arrayPiezas= obtenerPiezas(planTratamiento.getCodigoPk(), ConstantesIntegridadDominio.acronimoOtro, porConfirmar);
		//3.1 iteramos las piezas para obtener los hallazgos ligados a la pieza
		//y los seteamos a los detalles
		for(InfoDatosInt pieza: arrayPiezas)
		{
			InfoDetallePlanTramiento detallePlan= new InfoDetallePlanTramiento();
			detallePlan.setPieza(pieza);
			
			//este metodo en el sql base carga los programas o servicios
			detallePlan.setDetalleSuperficie(obtenerHallazgosSuperficies(planTratamiento.getCodigoPk(), pieza.getCodigo(), ConstantesIntegridadDominio.acronimoOtro, estadosProgramasOservicios, utilizaProgramas, porConfirmar, codigoPkPresupuesto));
			planTratamiento.getSeccionOtrosHallazgos().add(detallePlan);
		}
		
		//4. CARGAR DETALLE BOCA
		planTratamiento.setSeccionHallazgosBoca(obtenerHallazgosBOCA(planTratamiento.getCodigoPk(), ConstantesBD.codigoNuncaValido, ConstantesIntegridadDominio.acronimoBoca, estadosProgramasOservicios, utilizaProgramas, porConfirmar, codigoPkPresupuesto));
		
		// 5. SE CARGA EL ESTADO ***************************
		
		planTratamiento.setEstado(PlanTratamiento.cargarEstadoPlanTratamiento(planTratamiento.getCodigoPk()));
	}
	
	/**
	 * 
	 * @param codigoPkPresupuesto
	 * @param utilizaProgramas
	 * @return
	 */
	public static InfoPlanTratamiento obtenerPlanTratamientoXPresupuesto(BigDecimal codigoPkPresupuesto, boolean utilizaProgramas) 
	{
		InfoPlanTratamiento planTratamiento= new InfoPlanTratamiento();
		
		String porConfirmar=ConstantesBD.acronimoNo;
		//************1.primero obtenemos el codigo del plan de tratamiento
		planTratamiento.setCodigoPk(PresupuestoOdontologico.obtenerCodigoPlanTratamientoPresupuesto(codigoPkPresupuesto));
		
		//************validamos que cargue el codigo
		if(planTratamiento.getCodigoPk().doubleValue()<=0)
		{
			logger.warn("NO EXISTE PLAN DE TRATAMIENTO!!!!!!!!!!!");
			return null; 
		}
		cargarDetallePlanTratamiento(utilizaProgramas, planTratamiento,	porConfirmar, codigoPkPresupuesto);
		return planTratamiento;
	}
	
	
	/**
	 * 
	 * @param codigoPaciente
	 * @param estados
	 * @param porConfirmar
	 * @param activo
	 * @param utilizaProgramas
	 * @return
	 */
	public static ArrayList<InfoPlanTratamiento> obtenerPlanTratamiento(int codigoPaciente, ArrayList<String> estados, String porConfirmar, String activo, boolean utilizaProgramas)
	{
		ArrayList<InfoPlanTratamiento> array= new ArrayList<InfoPlanTratamiento>();
		ArrayList<BigDecimal> codigos= new ArrayList<BigDecimal>();
		//************1.primero obtenemos el codigo del plan de tratamiento
		codigos = obtenerCodigoPlanTratamiento(codigoPaciente, estados, porConfirmar);
		// validamos que existan codigo
		if(codigos.size()>0)
		{
			for(BigDecimal codigo: codigos)
			{
				InfoPlanTratamiento planTratamiento = new InfoPlanTratamiento(); 
				planTratamiento.setCodigoPk(codigo);
				
				//************validamos que cargue el codigo
				if(planTratamiento.getCodigoPk().doubleValue()<=0)
				{
					logger.warn("NO EXISTE PLAN DE TRATAMIENTO!!!!!!!!!!!");
					return null;
				}
				
				//2.******************CARGAR EL DETALLE, obtenemos las piezas del plan de tratamiento de tipo secciion DET
				ArrayList<InfoDatosInt> arrayPiezas= obtenerPiezas(planTratamiento.getCodigoPk(), ConstantesIntegridadDominio.acronimoDetalle, porConfirmar, activo);
				ArrayList<String> estadosProgramasOservicios= new ArrayList<String>();
				
				if(utilizaProgramas)
					estadosProgramasOservicios.add(ConstantesIntegridadDominio.acronimoTerminado);
				else{
					estadosProgramasOservicios.add(ConstantesIntegridadDominio.acronimoRealizadoInterno);
					estadosProgramasOservicios.add(ConstantesIntegridadDominio.acronimoRealizadoExterno);
				}
				
				//2.1 ****************************** iteramos las piezas para obtener los hallazgos ligados a la pieza
				//y los seteamos a los detalles
				for(InfoDatosInt pieza: arrayPiezas)
				{
					InfoDetallePlanTramiento detallePlan= new InfoDetallePlanTramiento();
					detallePlan.setPieza(pieza);
					
					//este metodo en el sql base carga los programas o servicios
					detallePlan.setDetalleSuperficie(obtenerHallazgosSuperficies(
							planTratamiento.getCodigoPk(), 
							pieza.getCodigo(), 
							ConstantesIntegridadDominio.acronimoDetalle, 
							estadosProgramasOservicios, 
							utilizaProgramas, 
							porConfirmar,
							activo));
					planTratamiento.getSeccionHallazgosDetalle().add(detallePlan);
				}	
				
				
				//3. CARGAR DETALLE OTROS
				arrayPiezas= obtenerPiezas(planTratamiento.getCodigoPk(), ConstantesIntegridadDominio.acronimoOtro, porConfirmar);
				//3.1 iteramos las piezas para obtener los hallazgos ligados a la pieza
				//y los seteamos a los detalles
				for(InfoDatosInt pieza: arrayPiezas)
				{
					InfoDetallePlanTramiento detallePlan= new InfoDetallePlanTramiento();
					detallePlan.setPieza(pieza);
					
					//este metodo en el sql base carga los programas o servicios
					detallePlan.setDetalleSuperficie(obtenerHallazgosSuperficies(
							planTratamiento.getCodigoPk(), 
							pieza.getCodigo(), 
							ConstantesIntegridadDominio.acronimoDetalle, 
							estadosProgramasOservicios, 
							utilizaProgramas, 
							porConfirmar,
							activo));
					planTratamiento.getSeccionOtrosHallazgos().add(detallePlan);
				}
				
				//4. CARGAR DETALLE BOCA
				planTratamiento.setSeccionHallazgosBoca(
						obtenerHallazgosBOCA(
								planTratamiento.getCodigoPk(), 
								ConstantesBD.codigoNuncaValido, 
								ConstantesIntegridadDominio.acronimoBoca, 
								estadosProgramasOservicios, 
								utilizaProgramas, 
								porConfirmar,
								activo));
				
				// 5. SE CARGA EL ESTADO ***************************
				planTratamiento.setEstado(PlanTratamiento.cargarEstadoPlanTratamiento(planTratamiento.getCodigoPk()));
				
				array.add(planTratamiento);
			}
			
		}else{
			return null;
		}
		
        /*
		for(int i=0;i<array.size();i++)
		{
			InfoPlanTratamiento elem = (InfoPlanTratamiento) array.get(i);
			
			for(int j=0;j<elem.getSeccionHallazgosDetalle().size();j++)
			{
				InfoDetallePlanTramiento elem1 = (InfoDetallePlanTramiento) elem.getSeccionHallazgosDetalle().get(j);
				
				for(int w=0;w<elem1.getDetalleSuperficie().size();w++)
				{
					InfoHallazgoSuperficie elem2 = (InfoHallazgoSuperficie) elem1.getDetalleSuperficie().get(w);
					
					for(int z=0;z<elem2.getProgramasOservicios().size();z++)
					{
						InfoProgramaServicioPlan elem3 = (InfoProgramaServicioPlan) elem2.getProgramasOservicios().get(z);
						logger.info("****************************************************************************");
						logger.info("fecha ini: "+elem3.getFechaInicio());
						logger.info("fecha fin: "+elem3.getFechaFinal());
						logger.info("nombre ser:"+elem3.getNombreProgramaServicio());
						logger.info("pieza: "+elem1.getPieza().getNombre());
						logger.info("especialidad: "+elem3.getEspecialidad());
						logger.info("****************************************************************************\n");
					}
				}
			}
		}*/		
		return array;
	}
	
	/**
	 *  CARGAR EL PLAN DE TRATAMIENTO- 
	 * @param codigoPlanTratamiento
	 * @param ingreso
	 * @param porConfirmar
	 * @param utilizaProgramas
	 * @return
	 */
	public static InfoPlanTratamiento obtenerPlanTratamientoPresupuestoCompleto(BigDecimal codigoPlanTratamiento ,  int ingreso, String porConfirmar ,boolean utilizaProgramas , int institucion)
	{ 
		
		InfoPlanTratamiento planTratamiento= new InfoPlanTratamiento();
		ArrayList<String> estadosPlanTratamiento= new ArrayList<String>();
		estadosPlanTratamiento.add(ConstantesIntegridadDominio.acronimoEstadoActivo);
		estadosPlanTratamiento.add(ConstantesIntegridadDominio.acronimoEstadoEnProceso);
		estadosPlanTratamiento.add(ConstantesIntegridadDominio.acronimoSuspendidoTemporalmente);
		

		//************1.primero obtenemos el codigo del plan de tratamiento
		planTratamiento.setCodigoPk(codigoPlanTratamiento);
		//************validamos que cargue el codigo
		
		if(planTratamiento.getCodigoPk().doubleValue()<=0)
		{
			logger.warn("NO EXISTE PLAN DE TRATAMIENTO!!!!!!!!!!!");
			return null; //ojo
		}
		//2 CARGAR PLAN PIEZAS 
		ArrayList<InfoDatosInt> arrayPiezas= obtenerPiezas(planTratamiento.getCodigoPk(), ConstantesIntegridadDominio.acronimoDetalle, porConfirmar);
		
		
		
		ArrayList<String> estadosProgramasOservicios= new ArrayList<String>();
		//estadosProgramasOservicios.add(ConstantesIntegridadDominio.acronimoEstadoPendiente); // POR VERIFICAR
		//2.1 ****************************** iteramos las piezas para obtener los hallazgos ligados a la pieza
		//y los seteamos a los detalles
		for(InfoDatosInt pieza: arrayPiezas)
		{
			InfoDetallePlanTramiento detallePlan= new InfoDetallePlanTramiento();
			detallePlan.setPieza(pieza);
			//este metodo en el sql base carga los programas o servicios
			// ENVIAMOS CARGARSERVCIOS COMO TRUE  PERO HAY QUE VALIDAR QUE UTILIZAR SEVICIOS
			detallePlan.setDetalleSuperficie(obtenerHallazgosSuperficies(planTratamiento.getCodigoPk(), pieza.getCodigo(), ConstantesIntegridadDominio.acronimoDetalle, estadosProgramasOservicios, utilizaProgramas, porConfirmar,  /*cargarServicios*/true , institucion));
			planTratamiento.getSeccionHallazgosDetalle().add(detallePlan);
		}	
		
		
		//3. CARGAR DETALLE OTROS
		arrayPiezas= obtenerPiezas(planTratamiento.getCodigoPk(), ConstantesIntegridadDominio.acronimoOtro, porConfirmar);
		//3.1 iteramos las piezas para obtener los hallazgos ligados a la pieza
		//y los seteamos a los detalles
		for(InfoDatosInt pieza: arrayPiezas)
		{
			InfoDetallePlanTramiento detallePlan= new InfoDetallePlanTramiento();
			detallePlan.setPieza(pieza);
			
			//este metodo en el sql base carga los programas o servicios
			detallePlan.setDetalleSuperficie(obtenerHallazgosSuperficies(planTratamiento.getCodigoPk(), pieza.getCodigo(), ConstantesIntegridadDominio.acronimoOtro, estadosProgramasOservicios, utilizaProgramas, porConfirmar, true , institucion));
			
			
			// Se VALIDA que el DETALLE NO tenga Programas con INCLUSIONES 
			for(int i=0; i< detallePlan.getDetalleSuperficie().size(); i++)
			 {
				boolean esOtro=false;
				
				for(int j=0; j<detallePlan.getDetalleSuperficie().get(i).getProgramasOservicios().size();j++)
				{	
				 if(  (!(detallePlan.getDetalleSuperficie().get(i).getProgramasOservicios().get(j).getInclusion().equals(ConstantesBD.acronimoSi)) && detallePlan.getDetalleSuperficie().get(i).getProgramasOservicios().get(j).getExisteBD().isActivo()) || (detallePlan.getDetalleSuperficie().get(i).getProgramasOservicios().get(j).getExclusion().equals(ConstantesBD.acronimoSi)) )
				  {
					esOtro=true; 
					logger.info("\n\n Es Otro la Superficie>> "+detallePlan.getDetalleSuperficie().get(i).getSuperficieOPCIONAL().getNombre());
				  }
				  
				}
				if(!esOtro)
				{					
					logger.info("\n\n NOO Es Otro la Superficie>> "+detallePlan.getDetalleSuperficie().get(i).getSuperficieOPCIONAL().getNombre());
					detallePlan.getDetalleSuperficie().remove(i);
					i--;  // TODO CAMBIAR ESTO 
				}
			  }	
						
			if(detallePlan.getDetalleSuperficie().size()>0)
			{	
			  planTratamiento.getSeccionOtrosHallazgos().add(detallePlan);
			}
		}
		
		//4. CARGAR DETALLE BOCA
		
	
		planTratamiento.setSeccionHallazgosBoca(obtenerHallazgosBOCA(planTratamiento.getCodigoPk(), ConstantesBD.codigoNuncaValido, ConstantesIntegridadDominio.acronimoBoca, estadosProgramasOservicios, utilizaProgramas, porConfirmar, true , institucion ));
		
		
		
		planTratamiento.setMigrado(PlanTratamiento.cargarMigradoPlanTratamiento(planTratamiento.getCodigoPk()));
		
		// 5. SE CARGA EL ESTADO ***************************
		//???? falta settear en el infoplan--
		planTratamiento.setEstado(PlanTratamiento.cargarEstadoPlanTratamiento(planTratamiento.getCodigoPk()));
		
		//*/
		//CARGAR INCLUSIONES
		//6. CARGA LAS PIZAS PARA LAS INCLUSIONES Y EXCLUSIONES DEL PLAN DE TRATAMIENTO
		DtoProgramasServiciosPlanT dtoProgramasServiciosPlanT = new DtoProgramasServiciosPlanT();
		
		dtoProgramasServiciosPlanT.setInclusion(ConstantesBD.acronimoSi);
		dtoProgramasServiciosPlanT.setActivo(ConstantesBD.acronimoSi);
       //logger.info("\n\nSe Consultan las piezas de Inclusiones");
		ArrayList<InfoDatosInt> arrayPiezasInclusiones = obtenerPiezasInclusionesGarantias(dtoProgramasServiciosPlanT, planTratamiento.getCodigoPk() , institucion);
		
		
		// 7 cargar 
		for(InfoDatosInt pieza: arrayPiezasInclusiones )
		{
			InfoDetallePlanTramiento detallePlanInclusiones= new InfoDetallePlanTramiento();
			detallePlanInclusiones.setPieza(pieza);
			//este metodo en el sql base carga los programas o servicios
			// ENVIAMOS CARGARSERVCIOS COMO TRUE  PERO HAY QUE VALIDAR QUE UTILIZAR SEVICIOS
			logger.info("\n\nSe Consultan los Hallazgos ");
			detallePlanInclusiones.setSuperficiesInclusion(obtenerHallazgosSuperficies(planTratamiento.getCodigoPk(), pieza.getCodigo(), "", estadosProgramasOservicios, utilizaProgramas, porConfirmar, true , institucion));
			
			
			//Se VALIDA que AL MENOS UNO de los PROGRAMAS tenga el Indicativo de INCLUSION de lo Contrario no se tiene encuenta el Detalle
			
			for(int i=0; i< detallePlanInclusiones.getSuperficiesInclusion().size(); i++)
			{
					boolean esInclusion=false;
					
					for(int j=0; j<detallePlanInclusiones.getSuperficiesInclusion().get(i).getProgramasOservicios().size();j++)
					{	
					 if(detallePlanInclusiones.getSuperficiesInclusion().get(i).getProgramasOservicios().get(j).getInclusion().equals(ConstantesBD.acronimoSi))
					  {
						esInclusion=true; 
						logger.info("\n\n Es INCLUSION la Superficie>> "+detallePlanInclusiones.getSuperficiesInclusion().get(i).getSuperficieOPCIONAL().getNombre());
					  }
					  
					}
					if(!esInclusion)
					{
						logger.info("\n\n NO es INCLUSION la superficie >>"+ detallePlanInclusiones.getSuperficiesInclusion().get(i).getSuperficieOPCIONAL().getNombre());					
						detallePlanInclusiones.getSuperficiesInclusion().remove(i);
						i--;
					}
			 }	
						
			if(detallePlanInclusiones.getSuperficiesInclusion().size()>0)
			{	
			  planTratamiento.getSeccionInclusiones().add(detallePlanInclusiones);
			}
			
			
		}	
		
		
		
		//CARGAR LAS GARANTIAS
		dtoProgramasServiciosPlanT.setInclusion("");
		dtoProgramasServiciosPlanT.setActivo(ConstantesBD.acronimoSi);
		dtoProgramasServiciosPlanT.setGarantia(ConstantesBD.acronimoSi);
		ArrayList<InfoDatosInt> arrayPiezasGarantias = obtenerPiezasInclusionesGarantias(dtoProgramasServiciosPlanT, planTratamiento.getCodigoPk(), institucion);
		
		
		
		DtoPlanTratamientoOdo dtoPlanTratamiento = new DtoPlanTratamientoOdo();
		dtoPlanTratamiento.setCodigoPk(planTratamiento.getCodigoPk());
		DtoDetallePlanTratamiento dtoDetallePlan = new DtoDetallePlanTratamiento();
		
		
		
		for(InfoDatosInt pieza: arrayPiezasGarantias )
		{
			InfoDetallePlanTramiento detallePlanGarantias = new InfoDetallePlanTramiento();
			detallePlanGarantias.setPieza(pieza);
			dtoDetallePlan.setPiezaDental(pieza.getCodigo());
			//este metodo en el sql base carga los programas o servicios
			// ENVIAMOS CARGARSERVCIOS COMO TRUE  PERO HAY QUE VALIDAR QUE UTILIZAR SEVICIOS
			detallePlanGarantias.setSuperficiesGarantia(obtenerHallazgosSuperficiesDTO(dtoPlanTratamiento,dtoDetallePlan, dtoProgramasServiciosPlanT , estadosProgramasOservicios , utilizaProgramas ,porConfirmar,   Boolean.TRUE  , BigDecimal.ZERO ,  institucion));
			planTratamiento.getSeccionGarantias().add(detallePlanGarantias);
		}	
			
		return planTratamiento;

	}
	
		

	
	/**
	 *  CARGAR EL PLAN DE TRATAMIENTO HISTORICO 
	 *  CON TODAS LA SECCIONES DEL PLAN 
	 *  RETORNA UN OBJETO INFOPLAN EL CUAL SIRVE COMO AYUDANTE PARA CARGAR LA INTERFAZ GRAFICA
	 * @param codigoPlanTratamiento
	 * @param ingreso
	 * @param porConfirmar
	 * @param utilizaProgramas
	 * @return
	 */
	public static InfoPlanTratamiento obtenerPlanTratamientoHistoricoOdontograma(DtoOdontograma dtoOdontograma,  int ingreso , boolean utilizaProgramas , int institucion)
	{
		
		//OBJETOS DE AYUDA PARA VISUALIZAR EL PLAN DE TRATAMIENTO 
	
		InfoPlanTratamiento planTratamiento= new InfoPlanTratamiento();// objeto ayudante para armar la presentacion en la jsp
		
		
		//CARGA LA INFORMACION HISTORICA DEL PLAN DE TRATAMIENTO
		DtoPlanTratamientoOdo dtoPlanHistorico = new DtoPlanTratamientoOdo();
		
		//CARGA LOS HISTORICOS DEL PLAN DE TRATAMIENTO
		dtoPlanHistorico= accionCargaPlanTratamientoHistorico(dtoPlanHistorico,dtoOdontograma, ingreso); // CARGA EL HISTORIAL DEL PLAN DE TRATAMIENTO
		
		//SETTEAMO EL ESTADO
		planTratamiento.setEstado(dtoPlanHistorico.getEstado()); //  Setter el estado
		planTratamiento.setCodigoPk(dtoPlanHistorico.getCodigoPlanHistorico());
		planTratamiento.setValoracion(dtoPlanHistorico.getOdontogramaDiagnostico());
		planTratamiento.setEvolucion(dtoPlanHistorico.getCodigoEvolucion());
		planTratamiento.setIngreso(dtoPlanHistorico.getIngreso());
		
		
		//DTO PROGRAMAS SERVICIOS PLAN DE TRATAMIENTO
		DtoProgramasServiciosPlanT dtoProgramaServicios = new DtoProgramasServiciosPlanT();
		


		// DTO DETALLE PLAN DE TRATAMIENTO
		DtoDetallePlanTratamiento dtoDetallePlan = new  DtoDetallePlanTratamiento();
		
		ArrayList<InfoDatosInt> arrayPiezas = new ArrayList<InfoDatosInt>();
		
		//DETALLE PLAN TRATAMIENTO HISTORICO
		accionCargarSeccionDetalleHistoricos(utilizaProgramas, planTratamiento,	dtoPlanHistorico, dtoDetallePlan , arrayPiezas, dtoProgramaServicios , institucion);	
		
		
		//LIMPIEZA
		dtoDetallePlan = new DtoDetallePlanTratamiento(); //LIMPIAR EL OBJETO
		dtoDetallePlan.setPorConfirmar(ConstantesBD.acronimoNo);
		arrayPiezas=new ArrayList<InfoDatosInt>();
		
		
		
		//CARGAR SECCION OTROS HISTORICOS DEL PLAN DE TRATAMIENTO 
		accionCargarSeccionOtrosHistoricoPlanTratamiento(utilizaProgramas,planTratamiento, dtoPlanHistorico, dtoProgramaServicios,	dtoDetallePlan, institucion);
		
		//DETALLE BOCA
		planTratamiento.setSeccionHallazgosBoca(obtenerHallazgosBOCAHistoricos(dtoPlanHistorico, dtoDetallePlan, utilizaProgramas, dtoProgramaServicios, institucion));
		
		// DETALLE  INCLUSIONES Y EXCLUSIONES
		accionCargaSeccionInclusionExclusionPlanHistorico(	utilizaProgramas, planTratamiento, dtoPlanHistorico, dtoDetallePlan, dtoProgramaServicios, institucion);	
	
		// DETALLE  GARANTIAS 
		accionCargarGarantiasHistoricas(utilizaProgramas, planTratamiento,	dtoPlanHistorico, dtoDetallePlan, dtoProgramaServicios, institucion);	
		
		//RETORNAR EL INFO PLAN DE TRATAMIENTO
		return planTratamiento ;

	}
	
	

	/**
	 * 
	 * @param utilizaProgramas
	 * @param planTratamiento
	 * @param dtoPlanHistorico
	 * @param dtoDetallePlan
	 * @return
	 */
	private static void  accionCargaSeccionInclusionExclusionPlanHistorico(
														boolean utilizaProgramas, InfoPlanTratamiento planTratamiento,
														DtoPlanTratamientoOdo dtoPlanHistorico,
														DtoDetallePlanTratamiento dtoDetallePlan,
														DtoProgramasServiciosPlanT dtoProgramaServicio ,
														int institucion) 
	
	{
		//TODO FALTA VERIFICAR DONDE SE CARGAN LA INCLUSIONES Y EXCLUSION OJO
		//CARGAR INCLUSIONES
		//6. CARGA LAS PIZAS PARA LAS INCLUSIONES Y EXCLUSIONES DEL PLAN DE TRATAMIENTO
	
		
		dtoProgramaServicio.setInclusion(ConstantesBD.acronimoSi);
		
		
		ArrayList<InfoDatosInt> arrayPiezasInclusiones =obtenerPiezasInclusionesGarantiasHistorico(dtoProgramaServicio, dtoPlanHistorico.getCodigoPlanHistorico());
		
		
		
		for(InfoDatosInt pieza: arrayPiezasInclusiones )
		{
			InfoDetallePlanTramiento detallePlanInclusiones= new InfoDetallePlanTramiento();
			detallePlanInclusiones.setPieza(pieza);
			//este metodo en el sql base carga los programas o servicios
			// ENVIAMOS CARGARSERVCIOS COMO TRUE  PERO HAY QUE VALIDAR QUE UTILIZAR SEVICIOS
			logger.info("\n\nSe Consultan los Hallazgos ");
			detallePlanInclusiones.setSuperficiesInclusion(obtenerHallazgosSuperficiesDTOHistoricos(dtoPlanHistorico, dtoDetallePlan, dtoProgramaServicio, utilizaProgramas, institucion));
			
			//Se VALIDA que AL MENOS UNO de los PROGRAMAS tenga el Indicativo de INCLUSION de lo Contrario no se tiene encuenta el Detalle
			
			for(int i=0; i< detallePlanInclusiones.getSuperficiesInclusion().size(); i++)
			{
					boolean esInclusion=false;
					
					for(int j=0; j<detallePlanInclusiones.getSuperficiesInclusion().get(i).getProgramasOservicios().size();j++)
					{	
						 if(detallePlanInclusiones.getSuperficiesInclusion().get(i).getProgramasOservicios().get(j).getInclusion().equals(ConstantesBD.acronimoSi))
						  {
							 esInclusion=true; 
							 logger.info("\n\n Es INCLUSION la Superficie>> "+detallePlanInclusiones.getSuperficiesInclusion().get(i).getSuperficieOPCIONAL().getNombre());
						  }
					  
					}
					if(!esInclusion)
					{
						logger.info("\n\n NO es INCLUSION la superficie >>"+ detallePlanInclusiones.getSuperficiesInclusion().get(i).getSuperficieOPCIONAL().getNombre());					
						detallePlanInclusiones.getSuperficiesInclusion().remove(i);
						i--;
					}
			}  
							
				if(detallePlanInclusiones.getSuperficiesInclusion().size()>0)
				{	
				  planTratamiento.getSeccionInclusiones().add(detallePlanInclusiones);
				}
			
		}
		
		
	}

	
	/**
	 * 
	 * @param utilizaProgramas
	 * @param planTratamiento
	 * @param dtoPlanHistorico
	 * @param dtoProgramaServicios
	 * @param dtoDetallePlan
	 */
	private static void accionCargarSeccionOtrosHistoricoPlanTratamiento(
																	boolean utilizaProgramas, InfoPlanTratamiento planTratamiento,
																	DtoPlanTratamientoOdo dtoPlanHistorico,
																	DtoProgramasServiciosPlanT dtoProgramaServicios,
																	DtoDetallePlanTratamiento dtoDetallePlan , 
																	int institucion ) 
	{
		ArrayList<InfoDatosInt> arrayPiezas;
		// CARGAR PIEZAS PARA LA SECCION OTROS
		
		dtoDetallePlan.setPlanTratamiento(dtoPlanHistorico.getCodigoPlanHistorico().doubleValue());
		dtoDetallePlan.setSeccion(ConstantesIntegridadDominio.acronimoOtro);
		arrayPiezas = obtenerPiezasHistorico(dtoDetallePlan);
		//3. OBTENER PIEZAS SECCION OTROS 
		accionCargarSeccionOtrosHistoricos(utilizaProgramas, planTratamiento, dtoPlanHistorico, dtoDetallePlan, arrayPiezas, dtoProgramaServicios,  institucion);
	}
	
	
	

	/**
	 * METODO QUE CARGA LAS GARANTIAS DEL PLAN DE TRATAMIENTO HISTORICO
	 * @param utilizaProgramas
	 * @param planTratamiento
	 * @param dtoPlanHistorico
	 * @param dtoDetallePlan
	 * @param dtoProgramasServiciosPlanT
	 */
	private static void accionCargarGarantiasHistoricas(
														boolean utilizaProgramas, InfoPlanTratamiento planTratamiento,
														DtoPlanTratamientoOdo dtoPlanHistorico,
														DtoDetallePlanTratamiento dtoDetallePlan,
														DtoProgramasServiciosPlanT dtoProgramasServiciosPlanT,
														int institucion) 
	{
	
		//CARGAR LAS GARANTIAS
		dtoProgramasServiciosPlanT.setInclusion("");
		dtoProgramasServiciosPlanT.setGarantia(ConstantesBD.acronimoSi);
		
		ArrayList<InfoDatosInt> arrayPiezasGarantias =obtenerPiezasInclusionesGarantiasHistorico(dtoProgramasServiciosPlanT, dtoPlanHistorico.getCodigoPlanHistorico());
		
		for(InfoDatosInt pieza: arrayPiezasGarantias )
		{
			InfoDetallePlanTramiento detallePlanGarantias = new InfoDetallePlanTramiento();
			detallePlanGarantias.setPieza(pieza);
			dtoDetallePlan.setPiezaDental(pieza.getCodigo());
			detallePlanGarantias.setSuperficiesGarantia( obtenerHallazgosSuperficiesDTOHistoricos(dtoPlanHistorico, dtoDetallePlan, dtoProgramasServiciosPlanT, utilizaProgramas, institucion));
			planTratamiento.getSeccionGarantias().add(detallePlanGarantias);
		}
	}

	
	/**
	 * METODO QUE CARGA LA SECCION OTROS DE LOS HISTORICOS DEL PLA DE TRATAMIENTO
	 * @param utilizaProgramas
	 * @param planTratamiento
	 * @param dtoPlanHistorico
	 * @param dtoDetallePlan
	 * @param arrayPiezas
	 */
	private static void accionCargarSeccionOtrosHistoricos(
															boolean utilizaProgramas, InfoPlanTratamiento planTratamiento,
															DtoPlanTratamientoOdo dtoPlanHistorico,
															DtoDetallePlanTratamiento dtoDetallePlan,
															ArrayList<InfoDatosInt> arrayPiezas,
															DtoProgramasServiciosPlanT dtoProgramaServicio,
															int institucion) 
	
	{
		//3.1 iteramos las piezas para obtener los hallazgos ligados a la pieza
		//y los seteamos a los detalles
		for(InfoDatosInt pieza: arrayPiezas)
		{
			InfoDetallePlanTramiento detallePlan= new InfoDetallePlanTramiento();
			detallePlan.setPieza(pieza);
			dtoDetallePlan.setPiezaDental(pieza.getCodigo());
			//este metodo en el sql base carga los programas o servicios
			detallePlan.setDetalleSuperficie(obtenerHallazgosSuperficiesHistoricos(dtoPlanHistorico, dtoDetallePlan, utilizaProgramas, dtoProgramaServicio, institucion));
			
			
			// Se VALIDA que el DETALLE NO tenga Programas con INCLUSIONES 
			for(int i=0; i< detallePlan.getDetalleSuperficie().size(); i++)
			 {
				boolean esOtro=false;
				
				for(int j=0; j<detallePlan.getDetalleSuperficie().get(i).getProgramasOservicios().size();j++)
				{	
				 if(  (!(detallePlan.getDetalleSuperficie().get(i).getProgramasOservicios().get(j).getInclusion().equals(ConstantesBD.acronimoSi)) /* && detallePlan.getDetalleSuperficie().get(i).getProgramasOservicios().get(j).getExisteBD().getActivo() */) || (detallePlan.getDetalleSuperficie().get(i).getProgramasOservicios().get(j).getExclusion().equals(ConstantesBD.acronimoSi)) )
				  {
					esOtro=true; 
					logger.info("\n\n Es Otro la Superficie>> "+detallePlan.getDetalleSuperficie().get(i).getSuperficieOPCIONAL().getNombre());
				  }
				  
				}
				if(!esOtro)
				{					
					logger.info("\n\n NOO Es Otro la Superficie>> "+detallePlan.getDetalleSuperficie().get(i).getSuperficieOPCIONAL().getNombre());
					detallePlan.getDetalleSuperficie().remove(i);
					i--;  // TODO CAMBIAR ESTO 
				}
			  }	
						
			if(detallePlan.getDetalleSuperficie().size()>0)
			{	
			  planTratamiento.getSeccionOtrosHallazgos().add(detallePlan);
			}
		}
	}

	
	
	/**
	 * METODO QUE CARGA LA SECCION DETALL DE LOS HISTORICOS
	 * @param utilizaProgramas
	 * @param planTratamiento
	 * @param dtoPlanHistorico
	 * @param dtoDetallePlan
	 */
	private static void accionCargarSeccionDetalleHistoricos(boolean utilizaProgramas, 
															InfoPlanTratamiento planTratamiento,
															DtoPlanTratamientoOdo dtoPlanHistorico,
															DtoDetallePlanTratamiento dtoDetallePlan,
															ArrayList<InfoDatosInt> arrayPiezas,
															DtoProgramasServiciosPlanT dtoProgramaServicio ,
															int institucion) 
	{
	
		//Obtener la seccion detalle del detalle del plan de tratamiento
		dtoDetallePlan.setPlanTratamiento(dtoPlanHistorico.getCodigoPlanHistorico().doubleValue());
		dtoDetallePlan.setSeccion(ConstantesIntegridadDominio.acronimoDetalle);
		dtoDetallePlan.setPorConfirmar(ConstantesBD.acronimoNo);
	
		
		
		//OBTENER PIEZAS SECCION DETALLE
		arrayPiezas = obtenerPiezasHistorico(dtoDetallePlan);
		
		
		
		//2.1 ITERAMOS PIEZAS DE LA SECCION DETALLE
		
		for(InfoDatosInt pieza: arrayPiezas)
		{
			InfoDetallePlanTramiento detallePlan= new InfoDetallePlanTramiento();
			detallePlan.setPieza(pieza);
			dtoDetallePlan.setPiezaDental(pieza.getCodigo());
			//CARGAR HALLAGOZ+ PROGRAMAS + SERVICIOS DE LOS PROGRAMAS
			detallePlan.setDetalleSuperficie(obtenerHallazgosSuperficiesHistoricos(dtoPlanHistorico, dtoDetallePlan, utilizaProgramas, dtoProgramaServicio , institucion));
			planTratamiento.getSeccionHallazgosDetalle().add(detallePlan);
		}
	}

	
	
	
	
	/**
	 * ACCION CARGAR PLAN DE TRATAMIENTO HISTORICOS 
	 * @param dtoPlanHistorico
	 * @param dtoOdontograma
	 * @param ingreso
	 * @return
	 */
	private static DtoPlanTratamientoOdo accionCargaPlanTratamientoHistorico( DtoPlanTratamientoOdo dtoPlanHistorico ,DtoOdontograma dtoOdontograma, int ingreso) 
	{
	
		
		
		// objeto tmp plan tratamiento
		DtoPlanTratamientoOdo tmPlanHisorico= new DtoPlanTratamientoOdo();
		ArrayList<DtoPlanTratamientoOdo> listaHistoricos= new ArrayList<DtoPlanTratamientoOdo>();
		
		// ODONTOGRAMA DE DIAGNOSTICO 
		if ( dtoOdontograma.getIndicativo().equals(ConstantesIntegridadDominio.acronimoOdontogramaDiagnostico) )
		{
			
			
			tmPlanHisorico.setOdontogramaDiagnostico(new BigDecimal(dtoOdontograma.getCodigoPk()));
			tmPlanHisorico.setIngreso(ingreso);
			tmPlanHisorico.setPorConfirmar(ConstantesBD.acronimoNo);
			tmPlanHisorico.setEstado(ConstantesIntegridadDominio.acronimoEstadoActivo);
			// nota-> CUANDO EL ODONTOGRAMA ES DE VALORACION EL ESTADO DEL PLAN DEBE ESTAR ACTIVO
			
		
			
			listaHistoricos=consultarPlanTratamientoHistorico(tmPlanHisorico);
			if(listaHistoricos.size()>0)
			{
				dtoPlanHistorico=listaHistoricos.get(0); 
				dtoPlanHistorico.setIndicativo(ConstantesIntegridadDominio.acronimoOdontogramaDiagnostico);
			}
			
			 
		}
		// ODONTOGRAMA DE EVOLUCION 
		else if(dtoOdontograma.getIndicativo().equals(ConstantesIntegridadDominio.acronimoOdontogramaTratamiento))
		{
			tmPlanHisorico.setCodigoEvolucion(new BigDecimal(dtoOdontograma.getEvolucion()));
			tmPlanHisorico.setIngreso(ingreso);
			tmPlanHisorico.setEstado("");
			listaHistoricos=consultarPlanTratamientoHistorico(tmPlanHisorico);
			if(listaHistoricos.size()>0)
			{
			
				dtoPlanHistorico=listaHistoricos.get(0);
				dtoPlanHistorico.setIndicativo(ConstantesIntegridadDominio.acronimoOdontogramaTratamiento);
			}
			
		}
		
		return dtoPlanHistorico;
				
	}
	
	
	
	
	/**
	 * 
	 * @param ingreso
	 * @param estados
	 * @return
	 */
	public static BigDecimal obtenerUltimoCodigoPlanTratamiento ( int ingreso, ArrayList<String> estados, String porConfirmarOPCIONAL) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().obtenerUltimoCodigoPlanTratamiento(ingreso, estados, porConfirmarOPCIONAL);
	}

	/**
	 * 
	 * @param codigoPkPlanTratamiento
	 * @param seccion
	 * @return
	 */
	public static ArrayList<InfoDatosInt> obtenerPiezas(BigDecimal codigoPkPlanTratamiento, String seccion, String porConfirmarOPCIONAL){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().obtenerPiezas(codigoPkPlanTratamiento, seccion, porConfirmarOPCIONAL);
	}
 
	/**
	 * 
	 * @param codigoPkPlanTratamiento
	 * @param pieza
	 * @param seccion
	 * @param estadosProgramasOservicios
	 * @param utilizaProgramas
	 * @param porConfirmar
	 * @return
	 */
	public static ArrayList<InfoHallazgoSuperficie> obtenerHallazgosSuperficies(BigDecimal codigoPkPlanTratamiento, int pieza, String seccion, ArrayList<String> estadosProgramasOservicios, boolean utilizaProgramas, String porConfirmar, BigDecimal codigoPkPresupuesto){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().obtenerHallazgosSuperficies(codigoPkPlanTratamiento, pieza, seccion, estadosProgramasOservicios, utilizaProgramas, porConfirmar, false, codigoPkPresupuesto, ConstantesBD.codigoNuncaValido /*codigo Institucion */);
	}
	/**
	 * 
	 * @param codigoPkPlanTratamiento
	 * @param pieza
	 * @param seccion
	 * @param estadosProgramasOservicios
	 * @param utilizaProgramas
	 * @param porConfirmar
	 * @param cargaServicios
	 * @return
	 */
	public static ArrayList<InfoHallazgoSuperficie> obtenerHallazgosSuperficies(BigDecimal codigoPkPlanTratamiento, int pieza, String seccion, ArrayList<String> estadosProgramasOservicios, boolean utilizaProgramas, String porConfirmar, boolean cargaServicios , int institucion){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().obtenerHallazgosSuperficies(codigoPkPlanTratamiento, pieza, seccion, estadosProgramasOservicios, utilizaProgramas, porConfirmar,cargaServicios, BigDecimal.ZERO ,  institucion );
	}
	
	
	/**
	 * 
	 * @param detallePlanTratamiento
	 * @param estadosProgramasOservicios
	 * @param utilizaProgramas
	 * @return
	 */
	public static ArrayList<InfoProgramaServicioPlan> obtenerProgramasOServicios(
			BigDecimal detallePlanTratamiento, ArrayList<String> estadosProgramasOservicios, boolean utilizaProgramas, String porConfirmar ) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().obtenerProgramasOServicios(detallePlanTratamiento, estadosProgramasOservicios, utilizaProgramas, porConfirmar ,false , ConstantesBD.codigoNuncaValido); //TODO CAMBIAR CODIGO INSTITUCION
	}
	
	
	/**
	 * 
	 * @param detallePlanTratamiento
	 * @param estadosProgramasOservicios
	 * @param utilizaProgramas
	 * @return
	 */
	public static ArrayList<InfoProgramaServicioPlan> obtenerProgramasOServicios(
			BigDecimal detallePlanTratamiento, ArrayList<String> estadosProgramasOservicios, boolean utilizaProgramas, String porConfirmar , boolean cargaServicios) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().obtenerProgramasOServicios(detallePlanTratamiento, estadosProgramasOservicios, utilizaProgramas, porConfirmar, true, ConstantesBD.codigoNuncaValido ); //TODO CAMBIAR CODIGO INSTITUCION
	}
	
	/**
	 * 
	 * @param codigoHallazgo
	 * @param codigoProgramas
	 * @param codigoDetalle
	 * @param utilizaProgramas
	 * @param codigoServicio
	 * @param codigosAsignadosPresupuesto 
	 * @return
	 */
	public static ArrayList<InfoProgramaServicioPlan> obtenerProgramasServiciosHallazgosPlanTramiento(double codigoHallazgo, String codigoProgramas, BigDecimal codigoDetalle , boolean utilizaProgramas, double codigoServicio, BigDecimal presupuesto, String codigosAsignadosPresupuesto, ArrayList<InfoNumSuperficiesPresupuesto> superficies )
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().obtenerProgramasServiciosHallazgosPlanTramiento(codigoHallazgo, codigoProgramas, codigoDetalle, utilizaProgramas, codigoServicio, presupuesto, codigosAsignadosPresupuesto, superficies);
	}

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static double guardarProgramasServicio( Connection con,DtoProgramasServiciosPlanT dto){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().guardarProgramasServicio(dto , con);
	} 
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public  static ArrayList<DtoProgramasServiciosPlanT> cargarProgramasServiciosPlanT(DtoProgramasServiciosPlanT dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().cargarProgramasServiciosPlanT(dto);
	}
	
	/**
	  * 
	  * @param dto
	  * @param con
	  * @param estadosAEliminar
	  * @param utilizaProgramas
	  * @return
	  */
	 public static boolean inactivarProgramasServicios(DtoProgramasServiciosPlanT dto, Connection con, ArrayList<String> estadosAEliminar, boolean utilizaProgramas ){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().inactivarProgramasServicios(dto, con, estadosAEliminar, utilizaProgramas);
	}
	
	 /** 
	  * @param codigoPkPlanTratamiento
	 * @param codigoPkPresupuesto 
	  * @return
	  */
	 public static ArrayList<InfoHallazgoSuperficie> obtenerHallazgosBOCA(BigDecimal codigoPkPlanTratamiento, int hallazgoOPCIONAL, String seccion, ArrayList<String> estadosProgramasOservicios, boolean utilizaProgramas, String porConfirmar, BigDecimal codigoPkPresupuesto)
	 {
		 return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().obtenerHallazgosBOCA(codigoPkPlanTratamiento, hallazgoOPCIONAL, seccion, estadosProgramasOservicios, utilizaProgramas, porConfirmar, false, codigoPkPresupuesto , ConstantesBD.codigoNuncaValido);
	 }
	 
	 
	 
	 /** 
	  * @param codigoPkPlanTratamiento
	  * @return
	  */
	 public static ArrayList<InfoHallazgoSuperficie> obtenerHallazgosBOCA(BigDecimal codigoPkPlanTratamiento, int hallazgoOPCIONAL, String seccion, ArrayList<String> estadosProgramasOservicios, boolean utilizaProgramas, String porConfirmar, boolean cargaServicios ,int  institucion)
	 {
		 return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().obtenerHallazgosBOCA(codigoPkPlanTratamiento, hallazgoOPCIONAL, seccion, estadosProgramasOservicios, utilizaProgramas, porConfirmar, cargaServicios, BigDecimal.ZERO, institucion);
	 }
	 
	 /**
	  * 
	  * @param codigoPrograma
	  * @param codigoDetPlan
	  * @return
	  */
	 public static int cargarOrdenServicio(int codigoPrograma, BigDecimal codigoDetPlan ,  Connection con, boolean inclusion)
	 {
		 return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().cargarOrdenServicio(codigoPrograma, codigoDetPlan , con, inclusion);
	 }
	 
	 /**
	  * 
	  * 
	  */
	 public static  double guardarDetalle(DtoDetallePlanTratamiento dto  , Connection con)
	 {
		 return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().guardarDetalle(dto , con);
	 }
		
	 /**
	  * 
	  * 
	  * @param dto
	  * @return
	  */
	 public static  boolean modicarEstadosDetalleProgServ(DtoProgramasServiciosPlanT dto, Connection con) {
		 
		 return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().modicarEstadosDetalleProgServ(dto, con);
	 }
	 /**
	  * 
	  * 
	  */

	 public static boolean modificar(DtoPlanTratamientoOdo dtoWhere,
			 DtoPlanTratamientoOdo dtoNuevo , Connection con) {
		 
		 return  DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().modificar(dtoWhere, dtoNuevo,con);
	 }

	 /**
	  * 
	  * 
	  */

	 public static String cargarMigradoPlanTratamiento(BigDecimal codigo) {
		 
		 return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().cargarMigradoPlanTratamiento(codigo);
	 }

	 /**
	  * 
	  * 
	  */

	 public static String cargarEstadoPlanTratamiento(BigDecimal codigo) {
		 
		 return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().cargarEstadoPlanTratamiento(codigo);
	 }


	 /**
	  * Consulta el plan de tratamiento, recibe como parametro un DtoPlanTratamiento, evalua los campos
	  * llenos del dto y con estos realiza los filtros. El Dto debe tener el campo institucion lleno
	  * 
	  * @param DtoPlanTratamientoOdo parametros
	  * */
	 public static DtoPlanTratamientoOdo consultarPlanTratamiento(DtoPlanTratamientoOdo parametros)
	 {
		 return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().consultarPlanTratamiento(parametros);
	 }

	/**
	 * Consultar todos los planes de tratamiento del paciente sin importar
	 * ingreso.
	 * @param codigoPaciente Código del paciente
	 * @param institucion Institución a la cual pertenece el paciente
	 * @return {@link DtoPlanTratamientoOdo} con los datos del último plan de tratamiento
	 */
	 public static DtoPlanTratamientoOdo consultarPlanTratamientoPaciente(int codigoPaciente, int institucion)
	 {
		 return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().consultarPlanTratamientoPaciente(codigoPaciente, institucion);
	 }

	 /**
	  * Consulta el plan de tratamiento, recibe como parametro un DtoPlanTratamiento, evalua los campos
	  * llenos del dto y con estos realiza los filtros. El Dto debe tener el campo institucion lleno
	  * 
	  * @param Connection con
	  * @param DtoPlanTratamientoOdo parametros
	  * */
	 public static DtoPlanTratamientoOdo consultarPlanTratamientoHistConf(DtoPlanTratamientoOdo parametros)
	 {
		 return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().consultarPlanTratamientoHistConf(parametros);
	 }


	 /**
	  * Consulta el plan de tratamiento, recibe como parametro un DtoPlanTratamiento, evalua los campos
	  * llenos del dto y con estos realiza los filtros. El Dto debe tener el campo institucion lleno
	  * 
	  * @param Connection con
	  * @param DtoPlanTratamientoOdo parametros
	  * */
	 public static ArrayList<DtoPlanTratamientoOdo>  consultarPlanTratamiento(DtoPlanTratamientoOdo parametros,  ArrayList<String> estadosPlan)
	 {
		 Connection con=UtilidadBD.abrirConexion();
		 ArrayList<DtoPlanTratamientoOdo>	 consultarPlanTratamiento= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().consultarPlanTratamiento(con,parametros, estadosPlan);
		 UtilidadBD.cerrarObjetosPersistencia(null, null, con);
		 return consultarPlanTratamiento;
	 }

	 /** Consulta el plan de tratamiento, recibe como parametro un DtoPlanTratamiento, evalua los campos
	  * llenos del dto y con estos realiza los filtros. El Dto debe tener el campo institucion lleno
	  * 
	  * @param Connection con
	  * @param DtoPlanTratamientoOdo parametros
	  * */
	 public static ArrayList<DtoPlanTratamientoOdo>  consultarPlanTratamiento(Connection con , DtoPlanTratamientoOdo parametros,  ArrayList<String> estadosPlan)
	 {
		 return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().consultarPlanTratamiento(con,parametros, estadosPlan);
	 }


	 /**
	  * Se obtiene el ultimo registro del un Plan de Tratamiento
	  * @param codPlanTratamiento
	  * @return
	  */
	 public static DtoLogPlanTratamiento consultarLogTratamiento(int codPlanTratamiento)
	 {
		 return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().consultarLogTratamiento(codPlanTratamiento);
	 }


	 /**
	  * Obtiene los hallazgos 
	  * @return
	  */
	 public static ArrayList<InfoHallazgoSuperficie> obtenerHallazgosSuperficies(DtoDetallePlanTratamiento parametros)
	 {
		 return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().obtenerHallazgosSuperficies(parametros);
	 }
		
	
	/**
	  * Obtiene el programa parametrizado por defecto para un hallazgo
	  * @param DtoProgramasServiciosPlanT
	  * @return
	 */
	public static InfoProgramaServicioPlan obtenerProgramaServicioParamHallazgo(DtoProgramasServiciosPlanT parametros)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().obtenerProgramaServicioParamHallazgo(parametros);
	}
	
	
	/**
	 *
	 * Carga los servicios de un programa Plant Tratamiento
	 * 
	 * @param parametros
	 * @return
	 */
	public static ArrayList<InfoServicios> cargarServiciosDeProgramasPlanT(DtoProgramasServiciosPlanT parametros)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().cargarServiciosDeProgramasPlanT(parametros);
	}
		
	 /**
	  * se consulta los los codigos del plan de tratamiento segun el ArrayList de estado y el codigo del paciente
	  * @param estados
	  * @return
	  */
	 public static ArrayList<BigDecimal> obtenerCodigoPlanTratamiento (int codigoPaciente, ArrayList<String> estados, String porConfirmar)
	 {
		 return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().obtenerCodigoPlanTratamiento(codigoPaciente, estados, porConfirmar);
	 }

	 /**
	  * 
	  * @param codigoPkPlanTratamiento
	  * @param seccion
	  * @param porConfirmar
	  * @param activo
	  * @return
	  */
	 public static ArrayList<InfoDatosInt> obtenerPiezas(BigDecimal codigoPkPlanTratamiento, String seccion, String porConfirmar, String activo)
	 {
		 return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().obtenerPiezas(codigoPkPlanTratamiento, seccion, porConfirmar, activo);
	 }

	 /**
	  * 
	  * @param codigoPkPlanTratamiento
	  * @param pieza
	  * @param seccion
	  * @param estadosProgramasOservicios
	  * @param utilizaProgramas
	  * @param porConfirmar
	  * @param activo
	  * @return
	  */
	 public static ArrayList<InfoHallazgoSuperficie> obtenerHallazgosSuperficies(
			 BigDecimal codigoPkPlanTratamiento, 
			 int pieza, 
			 String seccion, 
			 ArrayList<String> estadosProgramasOservicios, 
			 boolean utilizaProgramas, 
			 String porConfirmar,
			 String activo)
			 {
		 return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().obtenerHallazgosSuperficies(codigoPkPlanTratamiento, pieza, seccion, estadosProgramasOservicios, utilizaProgramas, porConfirmar, activo);
			 }

	 /**
	  * 
	  * @param codigoPkPlanTratamiento
	  * @param hallazgoOPCIONAL
	  * @param seccion
	  * @param estadosProgramasOservicios
	  * @param utilizaProgramas
	  * @param porConfirmar
	  * @param activo
	  * @return
	  */
	 public static ArrayList<InfoHallazgoSuperficie> obtenerHallazgosBOCA(
			 BigDecimal codigoPkPlanTratamiento, 
			 int hallazgoOPCIONAL, 
			 String seccion, 
			 ArrayList<String> estadosProgramasOservicios, 
			 boolean utilizaProgramas, 
			 String porConfirmar,
			 String activo)
			 {
		 return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().obtenerHallazgosBOCA(codigoPkPlanTratamiento, hallazgoOPCIONAL, seccion, estadosProgramasOservicios, utilizaProgramas, porConfirmar, activo);
			 }

	 /**
	  * Consulta los programas y servicios del detalle del plan de tratamiento
	  */
	 public static ArrayList<InfoProgramaServicioPlan> obtenerProgramasOServicios(DtoProgramasServiciosPlanT parametros)
	 {
		 return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().obtenerProgramasOServicios(parametros);
	 } 
		
		
	/**
	 * 
	 * @param dto
	 * @return
	*/
	public static String cargarFechaLogPlant(DtoPlanTratamientoOdo dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().cargarFechaLogPlant(dto);
	}
	
	/**
	 * @param DtoPlanTratamientoOdo dto
	 * */
	public static double guardarPlanTratamiento(Connection con,DtoPlanTratamientoOdo dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().guardarPlanTratamiento(con,dto);
	}

	/**
	 * @param DtoPlanTratamientoOdo dto
	 * */
	public static double guardarOdontograma(Connection con,DtoOdontograma dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().guardarOdontograma(con,dto);
	}

	/**
	 * @param DtoPlanTratamientoOdo dto
	 * */
	public static double guardarLogPlanTratamiento(Connection con,DtoLogPlanTratamiento dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().guardarLogPlanTratamiento(con,dto);
	}


	/**
	 * CARGAR LAS PIEZAS PARA LAS INCLUSIONES
	 * @param dto
	 * @return
	 */
	public  static  ArrayList<InfoDatosInt> obtenerPiezasInclusionesGarantias(DtoProgramasServiciosPlanT dto, BigDecimal codigoPlanTratamiento , int institucion){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().obtenerPiezasInclusionesGarantias(dto,codigoPlanTratamiento, institucion);
	}

	/**
	 * @param DtoDetallePlanTratamiento dto
	 * */
	public static double guardarDetPlanTratamiento(Connection con,DtoDetallePlanTratamiento dto)
	{
		Log4JManager.info(dto.getPlanTratamiento());
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().guardarDetPlanTratamiento(con,dto);
	}


	/**
	 * Carga las Superficies del Diente
	 * @return
	 */
	public static ArrayList<DtoSectorSuperficieCuadrante> cargarSuperficiesDiente(int institucion)
	{
		return  DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().cargarSuperficiesDiente(institucion);
	}


	/**
	 * @param DtoLogDetPlanTratamiento dto
	 * */
	public static double guardarLogDetPlanTratamiento(Connection con,DtoLogDetPlanTratamiento dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().guardarLogDetPlanTratamiento(con, dto);
	}

	/**
	 * @param DtoLogProgServPlant dto
	 * */
	public static double guardarLogProgramasServiciosPlanT(Connection con,DtoLogProgServPlant dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().guardarLogProgramasServiciosPlanT(con, dto);
	}
	 
	 /**
	 * Actualiza el estado activo del detalle del plan de tratamiento
	 * @param Connection con
	 * @param DtoProgramasServiciosPlanT dto
	 */
	public static boolean actualizarActivoDetallePlanTrat(Connection con,DtoDetallePlanTratamiento dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().actualizarActivoDetallePlanTrat(con, dto);
	}
	 
	/**
	 * Actualiza el estado activo de los programas del plan de tratamiento
	 * @param Connection con
	 * @param DtoProgramasServiciosPlanT dto
	 */
	public static boolean actualizarActivoProgServPlanTr(Connection con,DtoProgramasServiciosPlanT dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().actualizarActivoProgServPlanTr(con, dto);
	}
	
	public static ArrayList<DtoLogPlanTratamiento> cargarLogs(
			DtoLogPlanTratamiento dtoWhere) {
		
		 return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().cargarLogs(dtoWhere);
	}
	
	/**
	 * 
	 * 
	 */
	public static  ArrayList<DtoLogProgServPlant> cargarLogProgramas(
			DtoLogProgServPlant dtoWhere) {
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().cargarLogProgramas(dtoWhere);
	}
	
	/**
	  * @param DtoLogProgServPlant dto
	  * */
	 public static double guardarLogProgramasServiciosPlanT(Connection con,DtoProgramasServiciosPlanT dto, Double valoracion, Double evolucion)
	 {
		 DtoLogProgServPlant dtoLog= llenarLogProgramasServiciosPlanT(dto, valoracion, evolucion);
		 return guardarLogProgramasServiciosPlanT(con, dtoLog);
	 }
	
	
	/**
	 * 
	 * MODICAR EL PLAN DE TRATAMIENTO
	 * @param institucion
	 * @param con
	 * @param utilizaProgramas
	 * @return
	 */
	 public static boolean modificarPlan(int institucion, Connection con, boolean utilizaProgramas){
		 	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().modificarPlan(institucion, con, utilizaProgramas);
	 }
	
	 
	/**
	 * 
	 * @param dto
	 * @param con
	 */
	public static boolean inactivarPlan(DtoPlanTratamientoOdo dto, Connection con) {
			
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().inactivarPlan(dto, con);
	} 
	 
	/**
	 * 
	 * @param dto
	 * @param valoracion
	 * @param evolucion
	 * @return
	 */
	public static DtoLogProgServPlant llenarLogProgramasServiciosPlanT(DtoProgramasServiciosPlanT dto, Double valoracion, Double evolucion)
	{
		DtoLogProgServPlant dtoLog= new DtoLogProgServPlant();
		dtoLog.setActivo(dto.getActivo());
		dtoLog.setConvencion(dto.getConvencion());
		dtoLog.setEspecialidad(dto.getEspecialidad());
		dtoLog.setEstadoPrograma(dto.getEstadoPrograma());
		dtoLog.setEstadoServicio(dto.getEstadoServicio());
		dtoLog.setEvolucion(evolucion);
		dtoLog.setExclusion(dto.getExclusion());
		dtoLog.setGarantia(dto.getGarantia());
		dtoLog.setInclusion(dto.getInclusion());
		dtoLog.setIndPrograma(dto.getIndicativoPrograma());
		dtoLog.setIndServicio(dto.getIndicativoServicio());
		dtoLog.setMotivo(dto.getMotivo());
		dtoLog.setOrdenServicio(dto.getOrdenServicio());
		dtoLog.setPorConfirmar(dto.getPorConfirmado());
		dtoLog.setProgServPlant(dto.getCodigoPk().doubleValue());
		dtoLog.setUsuarioModifica(dto.getUsuarioModifica());
		dtoLog.setValoracion(valoracion);
		return dtoLog;
	}
	
	
	 /**
	  * 
	  * @param con
	  * @param dto
	  * @param cita
	  * @param evolucion
	  * @param valoracion
	  * @return
	  */
	 public static double guardarLogDetPlanTratamiento(Connection con,DtoDetallePlanTratamiento dto, int cita, Double evolucion, Double valoracion)
	 {
		 DtoLogDetPlanTratamiento dtoLog= llenarLogDetPlanTratamiento(dto,cita, evolucion, valoracion);
		 return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().guardarLogDetPlanTratamiento(con, dtoLog);
	 }
	
	/**
	 * 
	 * @param dto
	 * @param cita
	 * @param evolucion
	 * @param valoracion
	 * @return
	 */
	public static DtoLogDetPlanTratamiento llenarLogDetPlanTratamiento(DtoDetallePlanTratamiento dto, int cita, Double evolucion, Double valoracion)
	{
		DtoLogDetPlanTratamiento dtoLog= new DtoLogDetPlanTratamiento();
		dtoLog.setActivo(dto.getActivo());
		dtoLog.setCita(cita);
		dtoLog.setClasificacion(dto.getClasificacion());
		//dtoLog.setCodigoPk(dto.getCodigo());
		dtoLog.setConvencion(dto.getConvencion());
		dtoLog.setDetPlanTratamiento(dto.getCodigo());
		dtoLog.setEspecialidad(dto.getEspecialidad());
		dtoLog.setEvolucion(evolucion);
		dtoLog.setHallazgo(dto.getHallazgo());
		dtoLog.setPiezaDental(dto.getPiezaDental());
		dtoLog.setPorConfirmar(dto.getPorConfirmar());
		dtoLog.setSuperficie(dto.getSuperficie());
		dtoLog.setUsuarioModifica(dto.getFechaUsuarioModifica());
		dtoLog.setValoracion(valoracion);
		return dtoLog;
	}
	
	/**
	  * CARGA EL CODIGO DEL PLAN DE TRATAMIENTO x ingreso y codigo de cita
	  * @param ingreso
	  * @param estados
	  * @param codigoCita
	  * @return
	  */
	 public static int obtenerUltimoCodigoPlanTratamientoXIngresoXCita ( int ingreso, ArrayList<String> estados, int codigoCita){
		 return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().obtenerUltimoCodigoPlanTratamientoXIngresoXCita(ingreso, estados, codigoCita);
	 }

	/**
	 * metodo que inserta el his_conf_plan_tratamiento
	 * @deprecated
	 * @param con
	 * @param valoracion
	 * @param evolucion
	 * @param codigoCita
	 * @param codigoPlanTratamiento
	 * @return
	 */
	public static boolean insertHisConfPlanTratamiento(Connection con, int valoracion, int evolucion, int codigoCita, int codigoPlanTratamiento)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().insertHisConfPlanTratamiento(con, valoracion, evolucion, codigoCita, codigoPlanTratamiento);
	}
	
	/**
	 * metodo que inserta his_conf_det_plan_t
	 * @deprecated
	 * @param con
	 * @param valoracion
	 * @param evolucion
	 * @param codigoCita
	 * @param codigoPlanTratamiento
	 * @return
	 */
	public static boolean insertHisConfDetallePlanTratamiento(Connection con, int valoracion, int evolucion, int codigoCita, int codigoPlanTratamiento)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().insertHisConfDetallePlanTratamiento(con, valoracion, evolucion, codigoCita, codigoPlanTratamiento);
	}

	/**
	 * metodo que inserta his_conf_prog_serv_plan_t
	 * @deprecated
	 * @param con
	 * @param valoracion
	 * @param evolucion
	 * @param codigoCita
	 * @param codigoProgServPlanTrat
	 * @return
	 */
	public static boolean insertHisConfProgServPlanTratamiento(Connection con, int valoracion, int evolucion, int codigoCita, int codigoDetPlanTrat)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().insertHisConfProgServPlanTratamiento(con, valoracion, evolucion, codigoCita, codigoDetPlanTrat);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPlanTrat
	 * @param valoracion
	 * @param evolucion
	 * @param usuario
	 * @return
	 */
	public static boolean confirmarPlanTratamiento(Connection con, int codigoPlanTrat, int valoracion, int evolucion, String usuario)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().confirmarPlanTratamiento(con, codigoPlanTrat, valoracion, evolucion, usuario);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPlanTrat
	 * @param valoracion
	 * @param evolucion
	 * @param usuario
	 * @return
	 */
	public static boolean confirmarDetallePlanTratamiento(Connection con, int codigoPlanTrat, int valoracion, int evolucion, String usuario)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().confirmarDetallePlanTratamiento(con, codigoPlanTrat, valoracion, evolucion, usuario);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoDetPlanTrat
	 * @param valoracion
	 * @param evolucion
	 * @param usuario
	 * @return
	 */
	public static boolean confirmarProgServPlanTratamiento(Connection con, int codigoDetPlanTrat, int valoracion, int evolucion, String usuario)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().confirmarProgServPlanTratamiento(con, codigoDetPlanTrat, valoracion, evolucion, usuario);
	}
	 
	/**
	 * metodo de confirmacion del odontograma e insert his_conf 
	 * @param con
	 * @param planTrata
	 * @param valoracion
	 * @param evolucion
	 * @param codigoCita
	 * @param usuario
	 * @return boolean
	 */
	public static boolean confirmarOdontograma(Connection con, InfoPlanTratamiento planTrata, int valoracion, int evolucion, int codigoCita, String usuario)
	{
		
		if(confirmacionOdontograma(con, planTrata, valoracion, evolucion, usuario))
		{
			/*
			 *GUARDA LOG DEL PRESUPUESTO CON RESPECTO AL PLAN DE TRATAMIENTO
			 */
			accionGuardarLogPrespuesto(con, planTrata);
			
			if(insertHisConfOdontograma(con, planTrata, valoracion, evolucion,codigoCita))
			{
				logger.info("Fue exitoso la confirmacion del plan de tratamiento");
				return true;
			}
			else
			{
				logger.info("Error insertando tablas de his_conf");
				return false;
			}
			
			
		}
		else
		{
			logger.info("Error general insertando confirmacion de plan de tratamiento");
			return false;
		}
		
		
	}

	
	
	
	/**
	 * TODO CAMBIAR UBICACION DE ESTE METODO  GUARDAR LOG PRESUPUESTO 
	 * @author Edgar Carvajal Ruiz
	 * @param con
	 * @param planTrata
	 */
	private static void accionGuardarLogPrespuesto(Connection con,
													InfoPlanTratamiento planTrata) 
	{
		/*
		 * MODIFICAR LOG_ PRESUPUESTO ODONTOLOGICO 
		 */
		
		
		/*
		 *INSTANCIA LOG PRESUPUESTO
		 */
		DtoLogPrespuesto  dtoLogPresupuesto = new DtoLogPrespuesto();
		dtoLogPresupuesto.setCodigoPkPlantTratamiento(planTrata.getCodigoPk());
		
		/*
		 * CONSULTA EL CODIGO PRESUPUESTO CON PLAN TRATAMIENTO
		 */
		dtoLogPresupuesto.setCodigoPkPresupuesto(PresupuestoOdontologico.consultarCodigoPresupuestoXPlanTratamiento(planTrata.getCodigoPk(), ""));
		
		
		
		DtoPlanTratamientoOdo dtoPlan = new DtoPlanTratamientoOdo();
		dtoPlan.setCodigoPk(planTrata.getCodigoPk());
		
		/*
		 *CONSULTAR EL MAXIMO CODIGO LOG DE LA TRANSACCION  
		 */
		dtoLogPresupuesto.setCodigoPkLogPlanTratamiento( cargarMaximoCodigoLogPlantratamiento(dtoPlan,con));
		
		
		/*
		 * CARGAR INFORMACION DEL PRESUPUESTO ODONTOLOGICO 
		 */
		DtoPresupuestoOdontologico dtoPresupuesto = new DtoPresupuestoOdontologico();
		dtoPresupuesto.setCodigoPK(dtoLogPresupuesto.getCodigoPkPresupuesto());
		ArrayList<DtoPresupuestoOdontologico>  listaPresupuesto = PresupuestoOdontologico.cargarPresupuesto(dtoPresupuesto);
		if(listaPresupuesto!=null)
		{
			if(listaPresupuesto.size()>0)
			{
				 dtoPresupuesto=listaPresupuesto.get(0);
			}
			 
		}
		 
		dtoLogPresupuesto.setFechaGeneracion(dtoPresupuesto.getFechaBd());
		dtoLogPresupuesto.setHoraGeneracion(dtoPresupuesto.getUsuarioModifica().getHoraModifica());
		dtoLogPresupuesto.setEstado(dtoPresupuesto.getEstado());
		dtoLogPresupuesto.setConsecutivo(dtoPresupuesto.getConsecutivo());
		
	}

	
	
	
	
	
	/**
	 * metodo de confirmacion del odontograma (plan tratameinto)
	 * @param con
	 * @param planTrata
	 * @param valoracion
	 * @param evolucion
	 * @param usuario
	 * @return boolean
	 */
	private static boolean confirmacionOdontograma(Connection con,
			InfoPlanTratamiento planTrata, int valoracion, int evolucion,
			String usuario) {
		//**************************************** CONFIRMACION ODONTOGRAMA ************************
		// se confirma el plan de tratamiento, detalle y programas servicios
		// de la valoracion o evolicion respectiva
		if(!confirmarPlanTratamiento(con,Utilidades.convertirAEntero(planTrata.getCodigoPk().toString()),
				valoracion,evolucion,usuario))
		{
			logger.info("fallï¿½ confirmacion plan de tratamiento.");
			return false;
		}else
		{
			// TODO QUITAR ESTE CONFIRMAR 
			if(!confirmarDetallePlanTratamiento(con, Utilidades.convertirAEntero(planTrata.getCodigoPk().toString()),valoracion,evolucion,usuario))
			{
				logger.info("fallï¿½ confirmacion detalle de plan de tratamiento.");
				return false;
			}
			else
			{
				
			
				
				
				
				//*********************************************************************************
				// Confirmaciï¿½n Seccion Hallazgos Detalle
				for(InfoDetallePlanTramiento elem: planTrata.getSeccionHallazgosDetalle())
				{
					for(InfoHallazgoSuperficie elem1: elem.getDetalleSuperficie())
					{
						
				
						/*
						 * MODIFCAR EL  DETALLE PLAN DE TRATAMIENTO
						 */
						
						DtoDetallePlanTratamiento dtoDetPlanTratamiento = new DtoDetallePlanTratamiento();
						dtoDetPlanTratamiento.setCodigo(elem1.getCodigoPkDetalle().doubleValue());
						dtoDetPlanTratamiento.setPlanTratamiento(planTrata.getCodigoPk().doubleValue());
						PlanTratamiento.modificarDetallePlanTratamiento(dtoDetPlanTratamiento, con);
						
						
						
						if(!confirmarProgServPlanTratamiento(con,
															Utilidades.convertirAEntero(elem1.getCodigoPkDetalle().toString()),
															valoracion, 
															evolucion, 
															usuario))
						{
							logger.info("..:Error Confirmaciï¿½n Seccion Hallazgos Detalle");
							return false;
						}
					}
				}
				// Fin Confirmaciï¿½n Seccion Hallazgos Detalle
				//*********************************************************************************
				
				
				
				
				
				//*********************************************************************************
				// Confirmaciï¿½n Seccion Otros Hallazgos
				for(InfoDetallePlanTramiento elem: planTrata.getSeccionOtrosHallazgos())
				{
					for(InfoHallazgoSuperficie elem1: elem.getDetalleSuperficie())
					{
						
						
						/*
						 * MODIFCAR EL  DETALLE PLAN DE TRATAMIENTO
						 */
						
						DtoDetallePlanTratamiento dtoDetPlanTratamiento = new DtoDetallePlanTratamiento();
						dtoDetPlanTratamiento.setCodigo(elem1.getCodigoPkDetalle().doubleValue());
						dtoDetPlanTratamiento.setPlanTratamiento(planTrata.getCodigoPk().doubleValue());
						PlanTratamiento.modificarDetallePlanTratamiento(dtoDetPlanTratamiento, con);
						
					
						
						if(!confirmarProgServPlanTratamiento(con, 
															Utilidades.convertirAEntero(elem1.getCodigoPkDetalle().toString()), +
															valoracion, 
															evolucion, 
															usuario))
						{
							logger.info("..:Error Confirmaciï¿½n Seccion Otros Hallazgos");
							return false;
						}
					}
				}
				// Fin Confirmaciï¿½n Seccion Otros Hallazgos
				//*********************************************************************************
				
				
				
				//*********************************************************************************
				// Confirmaciï¿½n Seccion Hallazgos Boca
				for(InfoHallazgoSuperficie elem: planTrata.getSeccionHallazgosBoca())
				{
					
					
					/*
					 * MODIFCAR EL  DETALLE PLAN DE TRATAMIENTO
					 */
					
					DtoDetallePlanTratamiento dtoDetPlanTratamiento = new DtoDetallePlanTratamiento();
					dtoDetPlanTratamiento.setCodigo(elem.getCodigoPkDetalle().doubleValue());
					dtoDetPlanTratamiento.setPlanTratamiento(planTrata.getCodigoPk().doubleValue());
					PlanTratamiento.modificarDetallePlanTratamiento(dtoDetPlanTratamiento, con);
					
					
					if(!confirmarProgServPlanTratamiento(con, 
														Utilidades.convertirAEntero(elem.getCodigoPkDetalle().toString()),
														valoracion, 
														evolucion, 
														usuario))
					{
						logger.info("..Erro :Confirmaciï¿½n Seccion Hallazgos Boca");
						return false;
					}
				}
				// Fin Confirmaciï¿½n Seccion Hallazgos Boca
				//*********************************************************************************
			}
		}
		//**************************************** FIN CONFIRMACION ODONTOGRAMA *******************
		
		return true;
	}

	/**
	 * metodo que inserta en las tablas his_conf relacionadas al plan de tratamiento
	 * @param con
	 * @param planTrata
	 * @param valoracion
	 * @param evolucion
	 * @param codigoCita
	 * @return boolean
	 */
	private static boolean insertHisConfOdontograma(Connection con,
			InfoPlanTratamiento planTrata, int valoracion, int evolucion,
			int codigoCita) 
	{
		//**************************************** HIS CONF ODONTOGRAMA ***************************
		//TODO BUENO 1.
		if(!insertHisConfPlanTratamiento(con, valoracion, evolucion, codigoCita, Utilidades.convertirAEntero(planTrata.getCodigoPk().toString())))
		{
			logger.info("Fallï¿½ insercion de his_conf_plan_tratamiento");
			return false;
		}else
		{
			//TODO MALO
			if(!insertHisConfDetallePlanTratamiento(con, valoracion, evolucion, codigoCita, Utilidades.convertirAEntero(planTrata.getCodigoPk().toString())))
			{
				logger.info("Fallï¿½ insercion de his_conf_det_plan_tratamiento");
				return false;
			}
			else
			{
				//*********************************************************************************
				// Insert his_conf_prog_ser_plan_t Seccion Hallazgos Detalle
				for(InfoDetallePlanTramiento elem: planTrata.getSeccionHallazgosDetalle())
				{
					for(InfoHallazgoSuperficie elem1: elem.getDetalleSuperficie())
					{
						if(!insertHisConfProgServPlanTratamiento(con, valoracion, evolucion, codigoCita, Utilidades.convertirAEntero(elem1.getCodigoPkDetalle().toString())))
						{
							logger.info("Fallï¿½ insercion de his_conf_prog_serv_plan_t Nr1");
							return false;
						}
					}
				}
				// Fin Insert his_conf_prog_ser_plan_t Seccion Hallazgos Detalle
				//*********************************************************************************
				
				//*********************************************************************************
				// Insert his_conf_prog_ser_plan_t Seccion Otros Hallazgos
				for(InfoDetallePlanTramiento elem: planTrata.getSeccionOtrosHallazgos())
				{
					for(InfoHallazgoSuperficie elem1: elem.getDetalleSuperficie())
					{
						if(!insertHisConfProgServPlanTratamiento(con, valoracion, evolucion, codigoCita, Utilidades.convertirAEntero(elem1.getCodigoPkDetalle().toString())))
						{
							logger.info("Fallï¿½ insercion de his_conf_prog_serv_plan_t Nr2");
							return false;
						}
					}
				}
				// Fin Insert his_conf_prog_ser_plan_t Seccion Otros Hallazgos
				//*********************************************************************************
				
				//*********************************************************************************
				// Insert his_conf_prog_ser_plan_t Seccion Hallazgos Boca
				for(InfoHallazgoSuperficie elem: planTrata.getSeccionHallazgosBoca())
				{
					if(!insertHisConfProgServPlanTratamiento(con, valoracion, evolucion, codigoCita, Utilidades.convertirAEntero(elem.getCodigoPkDetalle().toString())))
					{
						logger.info("Fallï¿½ insercion de his_conf_prog_serv_plan_t Nr3");
						return false;
					}
				}
				// Fin Insert his_conf_prog_ser_plan_t Seccion Hallazgos Boca
				//*********************************************************************************
			}
		}	
		//**************************************** FIN HIS CONF ODONTOGRAMA ***********************
		return true;
	}
	
	/**
	 * Actualiza informacion en detalla de plan de tratamiento
	 * @param Connection con
	 * @param DtoDetallePlanTratamiento dto
	 * */
	public static boolean actualizarDetPlanTratamiento(Connection con,DtoDetallePlanTratamiento dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().actualizarDetPlanTratamiento(con, dto);
	}
	
	
	/**
	 * Obtiene los hallazgos para las seccion de Otros y Boca
	 * @return
	 */
	public static ArrayList<InfoDetallePlanTramiento> obtenerHallazgosSuperficiesSeccionOtrayBoca(DtoDetallePlanTratamiento parametros)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().obtenerHallazgosSuperficiesSeccionOtrayBoca(parametros);
	}
	
	/**
	* obtiene el listado de programas o servicios
	* @param DtoProgramasServiciosPlanT parametros
	* */
	public static ArrayList<InfoProgramaServicioPlan> obtenerListadoProgramasServicio(DtoProgramasServiciosPlanT parametros)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().obtenerListadoProgramasServicio(parametros);
	}
	
	/**
	  * Carga la informacion de un programa/servicio especifico
	  * @param DtoProgramasServiciosPlanT parametros
	  * */
	 public static InfoProgramaServicioPlan obtenerInfoProgramaServicios(DtoProgramasServiciosPlanT parametros)
	 {
		 return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().obtenerInfoProgramaServicios(parametros);
	 }
	 
	 
	 public static int obtenerCodPkLogServPlanT(int codPkDetallePlanT, int codPrograma, int codServicio)
	 {
		 return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().obtenerCodPkLogServPlanT(codPkDetallePlanT,codPrograma,codServicio); 
	 }
	
	 
	 /**
	  * metodo de actualizacion de estados programas servicios de otra evoluciones del plan de tratamietno
	  * @param con
	  * @param casoActualizacion
	  * @param estado
	  * @param aplica
	  * @param usuario
	  * @param codigoPkDetallePlanT
	  * @param codigoPrograma
	  * @param codigoServicio
	  * @return boolean
	  */
	public static boolean actualizacionEstadosPSOtrasEvoluciones(
			Connection con, 
			char casoActualizacion, 
			String estado, 
			String aplica,
			String estPrograma,
			String usuario,
			int codigoPkDetallePlanT,
			int codigoPrograma,
			int codigoServicio,
			int motivo,
			int codigoCita,
			int valoracion,
			int evolucion,
			String porConfirmar)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().actualizacionEstadosPSOtrasEvoluciones(con, casoActualizacion, estado, aplica, estPrograma, usuario, codigoPkDetallePlanT, codigoPrograma, codigoServicio, motivo, codigoCita, valoracion, evolucion, porConfirmar);
	}
	
	/**
	 * @deprecated
	 * Mï¿½todo que carga el encabezado del historial de ocnfirmacion del plan de tratamiento
	 * @param con
	 * @param infoPlanTrat
	 */
	public static void consultarHisConfPlanTratamiento(Connection con,InfoPlanTratamiento infoPlanTrat)
	{
		DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().consultarHisConfPlanTratamiento(con, infoPlanTrat);
	}
	
	/**
	  * CARGA LAS PIEZAS del historial de confirmacion
	  * @param codigoPkPlanTratamiento
	  * @return
	  */
	 public static ArrayList<InfoDatosInt> obtenerPiezasHisConf(Connection con,InfoPlanTratamiento infoPlanTrat, String seccion)
	 {
		 return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().obtenerPiezasHisConf(con, infoPlanTrat, seccion);
	 }
	 
	 /**
	  * Obtiene los hallazgos 
	  * @return
	  */
	 public static ArrayList<InfoHallazgoSuperficie> obtenerHallazgosSuperficiesHisConf(Connection con,DtoDetallePlanTratamiento parametros)
	 {
		 return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().obtenerHallazgosSuperficiesHisConf(con, parametros);
	 }
	 
	 /**
		
	  * Consulta los programas y servicios del detalle del plan de tratamiento
	  * Mirando las tablas de historial de confirmacion
	  */
	 public static ArrayList<InfoProgramaServicioPlan> obtenerProgramasOServiciosHisConf(Connection con,DtoProgramasServiciosPlanT parametros)
	 {
		 return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().obtenerProgramasOServiciosHisConf(con, parametros);
	 }
	 
	 /**
	  * Obtiene los hallazgos para las seccion de Otros y Boca
	  * @return
	  */
	 public static ArrayList<InfoDetallePlanTramiento> obtenerHallazgosSuperficiesSeccionOtrayBocaHisConf(Connection con,DtoDetallePlanTratamiento parametros)
	 {
		 return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().obtenerHallazgosSuperficiesSeccionOtrayBocaHisConf(con, parametros);
	 }
	 
	 /**
	 * verificar se el o los servicios del programa, alguno de ellos se encuentra agendado
	 * @param detPlanTratamiento
	 * @param codigoPrograma
	 * @param codigoServicio
	 * @param codigoCita
	 * @return
	 */
	public static boolean verificarServiciosAgendados(int detPlanTratamiento, int codigoPrograma, int codigoServicio, int codigoCita)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().verificarServiciosAgendados(detPlanTratamiento, codigoPrograma, codigoServicio, codigoCita);
	}
	 
	 
	
	 /**
	  * 
	  * @param dto
	  * @return
	  */
	  public static boolean guardarServArtIncPlant(Connection con,DtoServArtIncCitaOdo dto)
	 {
		 return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().guardarServArtIncPlant(con,dto);
	 }
	  
	  /**
	   * 
	   * @param dto
	   * @return
	   */
	  public static  ArrayList<DtoServArtIncCitaOdo> cargarServArtIncPlanT(DtoServArtIncCitaOdo dto,  BigDecimal codigoDetallePlanTratamiento, int institucion)
	  {
		  return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().cargarServArtIncPlanT(dto, codigoDetallePlanTratamiento, institucion);
	  }
	  
	 /**
	  * 
	  * @param codigoDetallePlanTratamiento
	  * @param utilizaProgramas
	  * @return
	  */
	 public static String obtenerEstadoProgramaServicioPlanTratamiento(BigDecimal codigoDetallePlanTratamiento, boolean utilizaProgramas, double codigoPkProgramaOServicio)
	 {
		 return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().obtenerEstadoProgramaServicioPlanTratamiento(codigoDetallePlanTratamiento, utilizaProgramas, codigoPkProgramaOServicio);
	 }
	 
	 /**
	  * retorna tru si- todos los servicios tiene cargos.
	  * 			si - todo los cargos servicios estan facturados de lo contrario false.
	  * @param listapkProgramasServiciosPlant
	  * @return
	  */
	 public static boolean validarCargos(
				ArrayList<String> listapkProgramasServiciosPlant) {
			
		 return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().validarCargos(listapkProgramasServiciosPlant);
	 }
	 
	 
	 /**
	  * Metodo para obtener los servicios de un Programa
	  * @param codigoPrograma
	  * @return
	  */
	 public  static ArrayList<InfoServicios> cargarServiciosParamPrograma(int codigoPrograma, String codigoTarifarioServ)
	 {
		 return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().cargarServiciosParamPrograma(codigoPrograma,codigoTarifarioServ);
	 }
	
	 /**
	  * 
	  * @param planTratamiento
	  * @param programa
	  * @param piezaDental
	  * @param superficie
	  * @param hallazgo
	  * @return
	  */
	 public static ArrayList<InfoServicios> cargarServiciosDeProgramasPlanT(BigDecimal planTratamiento, Double programa, Integer piezaDental, Integer superficie, Integer hallazgo, int institucion)
	 {
		 return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().cargarServiciosDeProgramasPlanT(planTratamiento, programa, piezaDental, superficie, hallazgo, institucion);
	 }
	 
	 
	/**
	 * 
	 * @param pieza
	 * @param hallazgo
	 * @param superficie
	 * @param seccion
	 * @return
	 */
	public static BigDecimal obtenerDetPlanTratamiento(BigDecimal planTratamiento, BigDecimal pieza,BigDecimal hallazgo, BigDecimal superficie, String seccion) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().obtenerDetPlanTratamiento(planTratamiento, pieza, hallazgo, superficie, seccion);
	}

	/**
	 * Mï¿½todo para cargar el odontograma de valorciï¿½n
	 * @param codigoPk
	 * @param esValoracion 
	 * @return DtoOdontograma
	 */
	public static DtoOdontograma cargarOdontograma(BigDecimal codigoPk, boolean esValoracion) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().cargarOdontograma(codigoPk, esValoracion);
	}
	
	
	
	public static  double insertarInclusionenBD(Connection con, DtoProgramasServiciosPlanT dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().guardarProgramasServicio(dto, con);
	}
	
	/**
	 * Mï¿½todo que verifica si el servicio se encuentra asignado a otra cita
	 * @param con
	 * @param codigoCita
	 * @param codigoPlanTratamiento
	 * @return true en caso de estar asociado,m false de lo contrario
	 * @author Juan David Ramï¿½rez
	 * @param i 
	 */
	public static boolean estaServicioAsociadoAOtraCita(Connection con, int codigoCita, int codigoProgHallPieza, int servicio, int codigoPaciente)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().estaServicioAsociadoAOtraCita(con, codigoCita, codigoProgHallPieza,servicio, codigoPaciente);
	}


	/**
	 * Metodo para actualizar un programa ( inclusion)
	 * @param con
	 * @param dtoPrograma
	 * @return
	 */
	public static boolean actualizarInclusionProgServPlanTr(Connection con,	DtoProgramasServiciosPlanT dtoPrograma) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().actualizarInclusionProgServPlanTr(con, dtoPrograma);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPkPlanTratamiento
	 * @param utilizaProgramas
	 * @return
	 */
	public static boolean existenProgramasServiciosContratadosNoTerminados(Connection con, BigDecimal codigoPkPlanTratamiento, boolean utilizaProgramas)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().existenProgramasServiciosContratadosNoTerminados(con, codigoPkPlanTratamiento, utilizaProgramas);
	}

	/**
	 * 
	 * @param cuenta
	 * @return
	 */
	public static InfoDatosDouble obtenerPlanTratamientoXCuenta(BigDecimal cuenta) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().obtenerPlanTratamientoXCuenta(cuenta);
	}

	

	/**
	 * Lista los programas seg&uacute;n los par&aacute;metros dados
	 * @param DtoProgramasServiciosPlanT dtoBusqueda Dto en el cual se encapsulan los par&acute;metros de b&uacute;squeda
	 * @return {@link ArrayList}<{@link InfoProgramaServicioPlan}> Lista los programas/servicios encontrados con los parï¿½metros dados
	 */
	public static ArrayList<InfoProgramaServicioPlan> listarProgramaServicioParamHallazgo(
			DtoProgramasServiciosPlanT dtoBusqueda)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().listarProgramaServicioParamHallazgo(dtoBusqueda);
	}

	
	/**
	 * Verifica la existencia de almenos una pieza activa
	 * @param codigoPkPlanTratamiento C&oacute;digo del plan de tratamiento
	 * @param piezaDental C&oacute;digo de la pieza dental
	 * @param seccion Secci&oacute;n que se desea evaluar
	 * @return true en caso de tener elementos activos
	 */
	public static boolean tieneAlgunaSuperficieActiva(BigDecimal codigoPkPlanTratamiento, int piezaDental, String seccion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().tieneAlgunaSuperficieActiva(codigoPkPlanTratamiento, piezaDental, seccion);
	}
	
	/**
	 * METODO QUE CARGA ARRAY LIST DE INFOHALLAZGO SUPERFICIES  (Utilizado para la construccion Grafica del plan de Tratamiento)
	 * 
	 * @param dtoPlanTratamiento
	 * @param dtoDetallePlanT
	 * @param dtoProgramasServicios
	 * @param estadosProgramasOservicios
	 * @param utilizaProgramas
	 * @param porConfirmar
	 * @param cargaServicios
	 * @param codigoPkPresupuesto
	 * @return
	 */
	public static ArrayList<InfoHallazgoSuperficie> obtenerHallazgosSuperficiesDTO(DtoPlanTratamientoOdo dtoPlanTratamiento , DtoDetallePlanTratamiento dtoDetallePlanT,  DtoProgramasServiciosPlanT dtoProgramasServicios , ArrayList<String> estadosProgramasOservicios, boolean utilizaProgramas, String porConfirmar, boolean cargaServicios , BigDecimal codigoPkPresupuesto , int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().obtenerHallazgosSuperficiesDTO(dtoPlanTratamiento, dtoDetallePlanT, dtoProgramasServicios, estadosProgramasOservicios, utilizaProgramas, porConfirmar, cargaServicios, codigoPkPresupuesto , institucion);	
	}

		

	/**
	 * Consultar HistoricOs del Plan de Tratamiento
	 * @param dto
	 * @return
	 */
	public static ArrayList<DtoPlanTratamientoOdo> consultarPlanTratamientoHistorico(DtoPlanTratamientoOdo dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().consultarPlanTratamientoHistorico(dto);
	}
	
	
	
	/**
	 * OBTENER PIEZAS DEL DETALLE DEL PLAN DE TRATAMIENTO HISTORICO
	 * @param dtoDetallePlan
	 * @return
	 */
	public static ArrayList<InfoDatosInt> obtenerPiezasHistorico(DtoDetallePlanTratamiento dtoDetallePlan)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().obtenerPiezasHistorico(dtoDetallePlan);
	}
	
	
	
	/**
	 * OBTENER HALLAZGO SUPERFICIE HISTORICO
	 * @param dtoPlanTratamiento
	 * @param dtoDetallePlan
	 * @param utilizaProgramas
	 * @param cargaServicios
	 * @return
	 */
	public static  ArrayList<InfoHallazgoSuperficie> obtenerHallazgosSuperficiesHistoricos(
																		DtoPlanTratamientoOdo dtoPlanTratamiento,
																		DtoDetallePlanTratamiento dtoDetallePlan, 
																		boolean utilizaProgramas,
																		DtoProgramasServiciosPlanT dtoProgramaServicios,
																		int institucion
																		)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().obtenerHallazgosSuperficiesHistoricos(dtoPlanTratamiento, dtoDetallePlan, utilizaProgramas, dtoProgramaServicios , institucion);
			
	}
	
	
	
	/**
	 * METODO QUE OBTIENE  LOS HALLAZGOS EN BOCA HISTORICOS DEL PLAN DE TRATAMIENTO
	 * @param dtoPlanTratamiento
	 * @param dtoDetallePlan
	 * @param utilizaProgramas
	 * @return
	 */
	public static  ArrayList<InfoHallazgoSuperficie> obtenerHallazgosBOCAHistoricos(DtoPlanTratamientoOdo dtoPlanTratamiento , DtoDetallePlanTratamiento dtoDetallePlan ,  boolean utilizaProgramas, DtoProgramasServiciosPlanT dtoProgramaServicios , int institucion){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().obtenerHallazgosBOCAHistoricos(dtoPlanTratamiento, dtoDetallePlan, utilizaProgramas, dtoProgramaServicios, institucion);
	}

	
	/**
	 * OBTENER LAS PIEZAS INCLUSION GARANTIA HISTORICOS PLAN DE TRATAMIENTO
	 * @param dto
	 * @param codigoPlanTratamiento
	 * @return
	 */
	public static  ArrayList<InfoDatosInt> obtenerPiezasInclusionesGarantiasHistorico(
			DtoProgramasServiciosPlanT dto, BigDecimal codigoPlanTratamiento) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().obtenerPiezasInclusionesGarantiasHistorico(dto, codigoPlanTratamiento);
	}
	
	
	
	/**
	 * OBTENER HALLAZGOS SUPERFICIE DTO HISTORICOS  
	 * @param dtoPlanTratamiento
	 * @param dtoDetallePlanT
	 * @param dtoProgramasServicios
	 * @param utilizaProgramas
	 * @return
	 */
	public static ArrayList<InfoHallazgoSuperficie> obtenerHallazgosSuperficiesDTOHistoricos(DtoPlanTratamientoOdo dtoPlanTratamiento , DtoDetallePlanTratamiento dtoDetallePlanT,  DtoProgramasServiciosPlanT dtoProgramasServicios,  boolean utilizaProgramas , int institucion){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().obtenerHallazgosSuperficiesDTOHistoricos(dtoPlanTratamiento, dtoDetallePlanT, dtoProgramasServicios, utilizaProgramas , institucion);
	}

	/**
	 * Elimina todas las relaciones entre programas de N superficies
	 * @param con Conexi&oacute;n con la BD
	 * @param planTratamiento C&oacute;digo del plan de tratamiento para el cual se van a eliminar las relaciones
	 * @param seccion Secci&oacute;n para la cual se va a eliminar la informaci&oacute;n
	 * @return retorna true en caso de &eacute;xito, false en caso de error
	 */
	public static boolean eliminarRelacionSuperficiesProgramas(Connection con, int planTratamiento, String seccion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().eliminarRelacionSuperficiesProgramas(con, planTratamiento, seccion);
	}

	/**
	 * Guarda las relaciones de los programas con las superficies
	 * @param con Conexi&oacute;n con la base de datos.
	 * @param listaEncabezados {@link ArrayList}<{@link DtoProgHallazgoPieza}>} Listado con las relaciones a guardar,
	 * los encabezados son de tipo {@link DtoProgHallazgoPieza} y los detalles de las superficies
	 * son de tipo {@link DtoSuperficiesPorPrograma}.
	 * @author Juan David Ram&iacute;rez
	 * @since 2010-05-11
	 */
	public static ArrayList<DtoProgHallazgoPieza> guardarRelacionesProgSuperficies(Connection con, ArrayList<DtoProgHallazgoPieza> listaEncabezados)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().guardarRelacionesProgSuperficies(con, listaEncabezados);
	}

	/**
	 * Consulta un registro especï¿½fico del plan de tratamiento con los parï¿½metros
	 * pasados a trav&eacute;s del DTO.
	 * @param dto {@link DtoDetallePlanTratamiento} DTO con los par&aacute;metros de b&uacute;squeda
	 * @param con {@link Connection} conexi&oaute;n con la BD
	 * @param buscarTerminado TODO
	 * @author Juan David Ram&iacute;rez.
	 * @since 2010-05-11 
	 */
	public static boolean consultarDetPlanTratamiento(DtoDetallePlanTratamiento dto, Connection con, boolean buscarTerminado)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().consultarDetPlanTratamiento(dto, con, buscarTerminado);
	}

	/**
	 * Obtener el n&uacute;mero de superficies que fueron relacionadas en un programa de N superficies.
	 * @param hallazgo C&oacute;digo del hallazgo.
	 * @param programa C&oacute;digo del programa buscado.
	 * @param pieza C&oacute;digo de la pieza dental a la cual se le vincul&oacute; el hallazgo. 
	 * @param superficie C&oacute;digo de la superficie evaluada.
	 * @param codigoPlanTratamiento C&oacute;digo del plan de tratamiento evaluado
	 * @return N&uacute;mero de superficies relacionadas, si no existe relaci&oacute;n en BD
	 * indica que es un diente, por lo tanto retorna 0
	 * @author Juan David Ram&uacute;rez
	 * @since 2010-05-14
	 */
	public static int consultarNumeroSuperficiesPorPrograma(int hallazgo, int programa, int pieza, int superficie, int codigoPlanTratamiento)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().consultarNumeroSuperficiesPorPrograma(hallazgo, programa, pieza, superficie, codigoPlanTratamiento);
	}

	/**
	 * 
	 * Metodo para inserta la relacion programa servicio
	 * @param con
	 * @param dtop
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public static ArrayList<DtoProgHallazgoPieza> guardarRelacionesProgSuperficies(Connection con,
			DtoProgHallazgoPieza dtop) 
	{
		ArrayList<DtoProgHallazgoPieza> listaEncabezados= new ArrayList<DtoProgHallazgoPieza>();
		listaEncabezados.add(dtop);
		return guardarRelacionesProgSuperficies(con, listaEncabezados);
	}
	
		
	
	
	

	/**
	 * CARGA EL INGRESO CON EL RESPECTIVO PLAN DE TRATAMIENTO
	 * @author Edgar Carvajal
	 * @param infoDto
	 * @return
	 */
	public static ArrayList<InfoIngresoPlanTratamiento> cargarIngresosPlanTratamiento(
			InfoIngresoPlanTratamiento infoDto) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().cargarIngresosPlanTratamiento(infoDto);
	}
	
	
	/**
	 * 
	 * @param codigoPresupuesto
	 * @return
	 */
	public static String   cargarNombreUsuarioModificaPlanTratamiento(
			BigDecimal codigoPresupuesto) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().cargarNombreUsuarioModificaPlanTratamiento(codigoPresupuesto);
		
	}
	
	
	/**
	 * MODIFICAR EL DETALLE DEL PLAN DE TRATAMIENTO METODO QUE MODIFICA EL PLAN DE TRATAMIENTO PARA REALIZAR LA TRAZABILIDAD DEL ODONTOGRAMA
	 * @param dto
	 * @param con
	 * @author Edgar Carvajal
	 * @return
	 */
	public static  boolean modificarDetallePlanTratamiento(	DtoDetallePlanTratamiento dto, 
													Connection con) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().modificarDetallePlanTratamiento(dto, con);
	}
	
	
	
	
	
	
	
	

	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param dtoDetalle
	 * @return
	 */
	public static ArrayList<InfoHallazgoSuperficie> obtenerHallazgosSuperficiesHistoricos(
			DtoDetallePlanTratamiento dtoDetalle) {
		return SqlBasePlanTratamientoDao.obtenerHallazgosSuperficiesHistoricos(dtoDetalle);
	}


	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param dtoPlan
	 * @param dtoDet
	 * @return
	 */
	public static ArrayList<InfoDatosInt> obtenerPiezasHistoricoPlanTratamiento(
					DtoPlanTratamientoOdo dtoPlan, DtoDetallePlanTratamiento dtoDet) {
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().obtenerPiezasHistoricoPlanTratamiento(dtoPlan, dtoDet);
	}


	
	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param dtoProgramaServicios
	 * @return
	 */
	public static  ArrayList<InfoProgramaServicioPlan> obtenerProgramasOServiciosHistoricos(
			DtoProgramasServiciosPlanT dtoProgramaServicios) {
	
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().obtenerProgramasOServiciosHistoricos(dtoProgramaServicios);
	}


	
	
	/**
	 * METODO PARA CARGAR HALLAGOS SUPERFICIES EN SECCION OTROS Y BOCA
	 * @author Edgar Carvajal Ruiz
	 * @param parametros
	 * @return
	 */
	public static ArrayList<InfoDetallePlanTramiento> obtenerHallazgosSuperficiesSeccionOtrayBocaHistoricos(DtoDetallePlanTratamiento dtoDetPlan)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().obtenerHallazgosSuperficiesSeccionOtrayBocaHistoricos(dtoDetPlan); 
	}
	
	
	/**
	 * CARGAR LOS SERVICIOS PROXIMA CITA DE UN PLAN DE TRATAMIENTO
	 * @author Edgar Carvajal Ruiz
	 * @param dtoOdongrama
	 * @param dtoPlanTrata
	 * @param institucion
	 * @return
	 */
	public static  ArrayList<InfoProgramaServicioPlan> consultarProximaCitaProgramasServicios(
			DtoOdontograma dtoOdongrama, 
			DtoPlanTratamientoOdo dtoPlanTrata,
			int institucion) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().consultarProximaCitaProgramasServicios(dtoOdongrama, dtoPlanTrata, institucion);
	}
	
	
	
	
	
	
	
	
	
////////////////////////////////////////////////////////********************************/////////////////////////////////////////////////
// METODOS PARA CARGA LOS HISTORICOS DEL ODONTOGRAMA
	
	
	

	/**
	 * ACCION CARGAR ODONTOGRAMA HISTORICO
	 * @author Edgar Carvajal Ruiz
	 * @param dtoOdontograma
	 * @param ingreso
	 * @param utilizaProgramas
	 * @param institucion
	 * @return
	 */
	public static  InfoPlanTratamiento accionCargarOdontogramaHistoricoPlanTratamiento( DtoOdontograma dtoOdontograma, 
																						int ingreso , 
																						boolean utilizaProgramas , 
																						int institucion, 
																						boolean cargaPlanTratamientoCompleto /* ATRIUBITO PARA VALIDAR SI SE CARGA EL PLAN TRATAMIENTO COMPLETO EN EL MOMENTO DE LA EVOLUCION */ )
																						
	{
		
		InfoOdontograma info = new InfoOdontograma();
				
		//Cargamos la informacion del encabezado del plan de tratamiento
		DtoPlanTratamientoOdo dtoPlan = new DtoPlanTratamientoOdo();
		dtoPlan.setInstitucion(institucion); // TODO FALTA MODIFICAR LA INSTITUCION
		
		
		/*
		 * CARGAR EL PLAN DE TRATAMIENTO HISTORICO
		 */
		dtoPlan = accionCargaPlanTratamientoHistorico(dtoPlan, dtoOdontograma, ingreso);
		info.setDtoInfoPlanTratamiento(dtoPlan);
	
		/*
		 *NOTA SI EL SE CARGA PLAN DE TRATAMIENTO COMPLETO NO IMPORTA 
		 */
		if( cargaPlanTratamientoCompleto)
		{
			dtoPlan.setIndicativo("");
		}

		
		
		
		/*
		 *CARGAR EL DETALLE SECCION DETALLE -> PLAN DE TRATAMIENTO  HISTORICO 
		 */
	 	 ArrayList<InfoDetallePlanTramiento> tmpListaDetallePlan= accionCargarSeccionDetPlanOdontogramaHistorico(info, dtoPlan , institucion);
	 	 info.getInfoPlanTrata().setSeccionHallazgosDetalle(tmpListaDetallePlan);	
	 	 
	 	 
	 	 /*
	 	  *CARGAR LA SECCION OTROS ->  ODONTOGRAMA HISTORICOS
	 	  */
	 	 ArrayList<InfoDetallePlanTramiento> tmpListaOtrosHallazgos=accionCargarOTROSDetPlanOdontogramaHistorico(info, dtoPlan, institucion);
	 	 info.getInfoPlanTrata().setSeccionOtrosHallazgos(tmpListaOtrosHallazgos);
	 	 
	 	 /*
	 	  * CARGAR LA SECCION BOCA-> ODONTOGRAMA HISTORICOS
	 	  */
	 	 ArrayList<InfoHallazgoSuperficie>  tmpListBoca= accionCargarBOCADetPlanOdontogramaHistorico(info, dtoPlan , institucion);
	 	 info.getInfoPlanTrata().setSeccionHallazgosBoca(tmpListBoca);
	 	 
	 	 /*
	 	  * setter al plan INFOPLAN 
	 	  */
	 	 InfoPlanTratamiento infoPlan= info.getInfoPlanTrata();
	 	 infoPlan.setEstadoPlan(dtoPlan.getEstado());
	 	 infoPlan.setCodigoPk(dtoPlan.getCodigoPk()); // CODIGO PLAN TRATAMIENTO ACTUAL
	 	 infoPlan.setHoraGrabacion(dtoPlan.getHoraGrabacion());
	 	 infoPlan.setFechaGrabacion(dtoPlan.getFechaGrabacion());
	 	
	 	 
	 	 /*
	 	  * SI PLAN DE TRATAMIENTO ES INDICATIVO EVOLUCION -> acronimoOdontogramaTratamiento
	 	  * Y ES DETALLE  PLAN 
	 	  */
	 	if(cargaPlanTratamientoCompleto)
		{
	 		double tmpMaximaCita= cargarColorServicioEvolucionadosCitas(infoPlan);
	 		cambiarColorServicioEvolucionadosCitas( infoPlan, tmpMaximaCita);
	 	}
	 	
	 	 return infoPlan;
	}

	
	/**
	 * ESTE METDO CARGAR LA INFORMACION DEL LOG DEL PRESUPUESTO
	 * EL CUAL SIRVE PARA COMPLEMENTAR LA INFORMACION DEL OBJETO INFO PLAN FACHADA 
	 * @author Edgar Carvajal Ruiz
	 * @param dtoPlan
	 * @param infoPlan
	 */
	private static void accionSettearInformacionPresupuestoHistorico(
			DtoPlanTratamientoOdo dtoPlan, InfoPlanTratamiento infoPlan) {
		
		DtoLogPrespuesto dto = new DtoLogPrespuesto();
	 	dto.setCodigoPkPlantTratamiento(dtoPlan.getCodigoPk());
	 	dto=PresupuestoOdontologico.cargarDtoLogPresupuesto(dto);
	 	infoPlan.setCodigoPresupuesto(dto.getCodigoPk());
	 	infoPlan.setEstadoPresupuesto(dto.getEstado());
	 	infoPlan.setFechaContrato(dto.getFechaGeneracion());
	 	infoPlan.setNumeroConsecutivoPresupuesto(dto.getConsecutivo());
	}
	
	
	/**
	 * BUSCAR EL NUMERO MAXIMO DE LA CITA
	 * @author Edgar Carvajal Ruiz
	 * @param info
	 */
	private static double  cargarColorServicioEvolucionadosCitas(InfoPlanTratamiento info)
	{
		
		double maximaCita=ConstantesBD.codigoNuncaValidoDouble;
		
		/*
		 *BUSCAR LA MAXIMA CITA DEL SERVICIO 
		 */
	
		/*
		 * PARA LAS DETALLE 
		 */
		for(InfoDetallePlanTramiento dtoDetalle : info.getSeccionHallazgosDetalle() )
		{
			for(InfoHallazgoSuperficie dtoHallazgosSuper:  dtoDetalle.getDetalleSuperficie())
			{
				for( InfoProgramaServicioPlan dtoProgramas: dtoHallazgosSuper.getProgramasOservicios())
				{
					for(InfoServicios infoSer:  dtoProgramas.getListaServicios())
					{
						if(maximaCita<infoSer.getCodigoCita().doubleValue())
						{
							  maximaCita=infoSer.getCodigoCita().doubleValue();
						}
					}
				}
					
			}
			
		}
		
		return maximaCita;
	}
	
	
	/**
	 * BUSCAR EL NUMERO MAXIMO DE LA CITA
	 * @author Edgar Carvajal Ruiz
	 * @param info
	 */
	private static double  cambiarColorServicioEvolucionadosCitas(InfoPlanTratamiento info, double citaMaxima){
		
		
		double maximaCita=ConstantesBD.codigoNuncaValidoDouble;
		
		/*
		 *BUSCAR LA MAXIMA CITA DEL SERVICIO 
		 */
	
		/*
		 * PARA LAS DETALLE 
		 */
		for(InfoDetallePlanTramiento dtoDetalle : info.getSeccionHallazgosDetalle() )
		{
			for(InfoHallazgoSuperficie dtoHallazgosSuper:  dtoDetalle.getDetalleSuperficie())
			{
				for( InfoProgramaServicioPlan dtoProgramas: dtoHallazgosSuper.getProgramasOservicios())
				{
					for(InfoServicios infoSer:  dtoProgramas.getListaServicios())
					{
						/*
						 *CAMBIAR COLOR SI TIENE LA CITAMAXIMA 
						 */
						if(infoSer.getCodigoCita().doubleValue()==citaMaxima )
						{
							infoSer.setColorServicioCita(COLOR_CITA_SERVICIO);
						}
						
					}
				}
					
			}
			
		}
		
		return maximaCita;
	}
	
	
	

	
	
	

		
	

	
	
		
	/**
	 * METODO CARGA LAS SECCION DE OTROS HALLAZGOS DEL PLAN DE TRATAMIENTO
	 * @author Edgar Carvajal Ruiz
	 * @param info
	 * @param dtoPlan
	 */
	private static ArrayList<InfoDetallePlanTramiento> accionCargarOTROSDetPlanOdontogramaHistorico(
																InfoOdontograma info, 
																DtoPlanTratamientoOdo dtoPlan,
																int institucion
																) 
	{
	
	
		
		/*
		 *Setteo de Busqueda de Informacion 
		 */
		DtoDetallePlanTratamiento dtoDetalle = new  DtoDetallePlanTratamiento();
		dtoDetalle.setSeccion(ConstantesIntegridadDominio.acronimoOtro);
		dtoDetalle.setCodigoPkPlanTratmientoHistorico (dtoPlan.getCodigoPlanHistorico().doubleValue());
		dtoDetalle.setPorConfirmar(ConstantesBD.acronimoNo);
	
		/*
		 *CARGAR LISTA HALLAGOS SUPERFICE PLAN TRATAMIENTO 
		 */
		ArrayList<InfoDetallePlanTramiento> tmpListaHallazgoSuperifice= PlanTratamiento.obtenerHallazgosSuperficiesSeccionOtrayBocaHistoricos(dtoDetalle);
		
		
		/*
		 * FILTRO PARA EL PROGRAMA
		 */
		DtoProgramasServiciosPlanT dtoProgramaServicios = new DtoProgramasServiciosPlanT();
	
		ArrayList<String> estadosServicios = new ArrayList<String>();
		
		
		dtoProgramaServicios.setBuscarProgramas(info.getEsBuscarPorPrograma());
		if(dtoPlan.getIndicativo().equals(ConstantesIntegridadDominio.acronimoOdontogramaTratamiento))
		{
			estadosServicios.add(ConstantesIntegridadDominio.acronimoRealizadoInterno);
		}
		dtoProgramaServicios.setEstadosProgramasOservicios(estadosServicios);
		dtoProgramaServicios.setCodigoTarifario(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(institucion));
		
		
		/*
		 *ITERACION DE BUSQUEDA DE PROGRAMAS DE LAS HALLAZGO SUPERFICIE  
		 */
		for(InfoDetallePlanTramiento pieza: tmpListaHallazgoSuperifice)
		{
			for(InfoHallazgoSuperficie hallsuper : pieza.getDetalleSuperficie())
			{
				dtoProgramaServicios.setLogDetPlanTratamiento(hallsuper.getCodigoPkDetalle());
				hallsuper.setProgramasOservicios(PlanTratamiento.obtenerProgramasOServiciosHistoricos(dtoProgramaServicios));
			}
			/*
			 *OJO CON ESTE METODO  cargarSuperficiesDiente  ??????????????????????????????????????????????''''
			 */
			pieza.setArraySuperficiesDiente(cargarSuperficiesDiente(info.getArraySuperficies(),pieza.getPieza().getCodigo()));// ojoo???
		}
		
		/*
		 *SETTEO DE LA DE LA LISTA DE HALLAZGOS SUPERFICIE 
		 */
		info.getInfoPlanTrata().setSeccionOtrosHallazgos(tmpListaHallazgoSuperifice);
	
		
		return  info.getInfoPlanTrata().getSeccionOtrosHallazgos();
	}
	
	
	
	
	
	/**
	 * METODO CARGA LAS SECCION DE BOCA HALLAZGOS DEL PLAN DE TRATAMIENTO
	 * @author Edgar Carvajal Ruiz
	 * @param info
	 * @param dtoPlan
	 */
	private static ArrayList<InfoHallazgoSuperficie> accionCargarBOCADetPlanOdontogramaHistorico(
																InfoOdontograma info, 
																DtoPlanTratamientoOdo dtoPlan ,
																int institucion) 
	{
	
	
		
		/*
		 *Setteo de Busqueda de Informacion 
		 */
		DtoDetallePlanTratamiento dtoDetalle = new  DtoDetallePlanTratamiento();
		dtoDetalle.setSeccion(ConstantesIntegridadDominio.acronimoBoca);
		dtoDetalle.setCodigoPkPlanTratmientoHistorico (dtoPlan.getCodigoPlanHistorico().doubleValue());
		dtoDetalle.setPorConfirmar(ConstantesBD.acronimoNo);
	
		/*
		 *CARGAR LISTA HALLAGOS SUPERFICE PLAN TRATAMIENTO 
		 */
		ArrayList<InfoDetallePlanTramiento> tmpListaHallazgoSuperifice= PlanTratamiento.obtenerHallazgosSuperficiesSeccionOtrayBocaHistoricos(dtoDetalle);
		
		
		/*
		 *ARRAY TMP PARA CARGAR  HALLAGOS A SUPERFICIES EN BOCA
		 */
		ArrayList<InfoHallazgoSuperficie> arrayInfoHallSuperBoca = new ArrayList<InfoHallazgoSuperficie>();
		ArrayList<String> estadosServicios= new ArrayList<String>();
		
		
		
		/*
		 * FILTRO PARA EL PROGRAMA
		 */
		DtoProgramasServiciosPlanT dtoProgramaServicios = new DtoProgramasServiciosPlanT();
		dtoProgramaServicios.setEstadosProgramasOservicios(new ArrayList<String>());
		dtoProgramaServicios.setBuscarProgramas(info.getEsBuscarPorPrograma());
		
		if(dtoPlan.getIndicativo().equals(ConstantesIntegridadDominio.acronimoOdontogramaTratamiento))
		{
			estadosServicios.add(ConstantesIntegridadDominio.acronimoRealizadoInterno);
		}
		dtoProgramaServicios.setEstadosProgramasOservicios(estadosServicios);
		
		
		
	
		/*
		 *ITERACION DE BUSQUEDA DE PROGRAMAS DE LAS HALLAZGO SUPERFICIE  
		 */
		for(InfoDetallePlanTramiento infoDetalle: tmpListaHallazgoSuperifice)
		{
			for(InfoHallazgoSuperficie hallsuper : infoDetalle.getDetalleSuperficie())
			{
				dtoProgramaServicios.setLogDetPlanTratamiento(hallsuper.getCodigoPkDetalle());
				hallsuper.setProgramasOservicios(PlanTratamiento.obtenerProgramasOServiciosHistoricos(dtoProgramaServicios));
				arrayInfoHallSuperBoca.add(hallsuper);
			}
			
		}
		
		/*
		 *SETTEO DE LA DE LA LISTA DE HALLAZGOS SUPERFICIE 
		 */
		info.getInfoPlanTrata().setSeccionHallazgosBoca(arrayInfoHallSuperBoca);
		
		
		return  info.getInfoPlanTrata().getSeccionHallazgosBoca();
	}
	
	
	
	
	
	
	
	/**
	 * TODO OJO CON ESTE METODO NO SE COMO FUNCIONA 
	 * @author 
	 * @param arraySuperficies
	 * @param codigoPieza
	 * @return
	 */
	private static ArrayList<DtoSectorSuperficieCuadrante> cargarSuperficiesDiente(ArrayList<DtoSectorSuperficieCuadrante> arraySuperficies, int codigoPieza) 
	{
		
		
		// ESTO PARA QUE SIRVE ??
		ArrayList<DtoSectorSuperficieCuadrante> superficies=new ArrayList<DtoSectorSuperficieCuadrante>();
		
		for(DtoSectorSuperficieCuadrante superficie:arraySuperficies)
		{
			if(superficie.getPieza()==codigoPieza)
			{
				superficies.add(superficie);
			}
		}
		
		return superficies;
	}
	
	
	
	
	
	/**
	 * METODO CARGA LAS SECCION DE HALLAZGOS DETALLE DEL PLAN DE TRATAMIENTO
	 * @author Edgar Carvajal Ruiz
	 * @param info
	 * @param dtoPlan
	 */
	private static ArrayList<InfoDetallePlanTramiento> accionCargarSeccionDetPlanOdontogramaHistorico(
																InfoOdontograma info, 
																DtoPlanTratamientoOdo dtoPlan,
																int institucion) 
	{
	
		/*
		 *Setteo de Busqueda de Informacion 
		 */
		DtoDetallePlanTratamiento dtoDetalle = new  DtoDetallePlanTratamiento();
		dtoDetalle.setSeccion(ConstantesIntegridadDominio.acronimoDetalle);
		dtoDetalle.setCodigoPkPlanTratmientoHistorico (dtoPlan.getCodigoPlanHistorico().doubleValue());
		dtoDetalle.setPorConfirmar(ConstantesBD.acronimoNo);
		
		
		/*
		 * Estado plan tratamiento
		 */
		ArrayList<String> estadosServicios = new ArrayList<String>();
		
		
			
		/*
		 * SI EL PLAN DE TRATAMIENTO ES INDICATIVO EVOLUCION
		 * CARGAR LOS ESTADO DEL SERVICIO QUE ESTEN EN RI 
		 */
		if(dtoPlan.getIndicativo().equals(ConstantesIntegridadDominio.acronimoOdontogramaTratamiento))
		{
			estadosServicios.add(ConstantesIntegridadDominio.acronimoRealizadoInterno);
		}
		
	
		/*
		 *CARGAR LISTA PIEZAS PLAN TRATAMIENTO 
		 */
		ArrayList<InfoDatosInt> arrayPiezas= PlanTratamiento.obtenerPiezasHistoricoPlanTratamiento(dtoPlan, dtoDetalle);
		
		/*
		 * CARGAR SECCION DETALLE PLAN
		 */
		ArrayList<InfoDetallePlanTramiento> tmpListaDetHallazgos =accionCargarDetalleHistoricoOdontograma(info , dtoDetalle , arrayPiezas , estadosServicios , institucion);
		info.getInfoPlanTrata().setSeccionHallazgosDetalle(tmpListaDetHallazgos);
		
		
		
		return  info.getInfoPlanTrata().getSeccionHallazgosDetalle();
	}
	
	
	/**
	 * ACCION CARGAR DETALLE PLAN TRATAMIENTO
	 * @author Edgar Carvajal Ruiz
	 * @param info
	 * @param dtoDetallePlan
	 * @param arrayPiezas
	 * @param estadoServicios
	 * @return
	 */
	private  static ArrayList<InfoDetallePlanTramiento> accionCargarDetalleHistoricoOdontograma(
														InfoOdontograma info, 
														DtoDetallePlanTratamiento dtoDetallePlan, 
														ArrayList<InfoDatosInt> arrayPiezas,
														ArrayList<String> estadoServicios,
														int institucion)
	{
		
		
		for(InfoDatosInt pieza: arrayPiezas)
		{	
			
		
			InfoDetallePlanTramiento hallazgoSuperficie= new InfoDetallePlanTramiento(); // OBJETO RETORNO
				
				//Informacion de la pieza
				hallazgoSuperficie.setPieza(pieza);
				dtoDetallePlan.setPiezaDental(pieza.getCodigo());
				
				/*
				 *CARGAR SUPERFICIES DETALLE PLAN TRATAMIENTO 
				 */
				hallazgoSuperficie.setDetalleSuperficie( PlanTratamiento.obtenerHallazgosSuperficiesHistoricos(dtoDetallePlan) );
				
				
				/*
				 * INSTANCIA PROGRAMAS 
				 */
				DtoProgramasServiciosPlanT dtoProgServ = new DtoProgramasServiciosPlanT();
				dtoProgServ.setEstadosProgramasOservicios(estadoServicios);
				dtoProgServ.setBuscarProgramas(info.getEsBuscarPorPrograma()); // TODO CAMBIAR AQUI
				dtoProgServ.setCodigoTarifario(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(institucion));
				
				
				
				//recorre los hallazgos y carga sus programas y servicios
				for(InfoHallazgoSuperficie hallsuper : hallazgoSuperficie.getDetalleSuperficie())
				{
					dtoProgServ.setLogDetPlanTratamiento(hallsuper.getCodigoPkDetalle());
					hallsuper.setProgramasOservicios(obtenerProgramasOServiciosHistoricos(dtoProgServ));
				}

				
				
				/*	
				 * ADICIONAR  InfoDetallePlanTramiento  A AL COLECCION
				 */
				info.getInfoPlanTrata().getSeccionHallazgosDetalle().add(hallazgoSuperficie);
		}
		
		
		return info.getInfoPlanTrata().getSeccionHallazgosDetalle();
		
	}
	

	
	/**
	 * CARGAR MAXIMO CODIGO LOG PLAN TRATAMIENTO
	 * @author Edgar Carvajal Ruiz
	 * @param dtoPlan
	 * @return
	 */
	public static  BigDecimal cargarMaximoCodigoLogPlantratamiento(
			DtoPlanTratamientoOdo dtoPlan ,
			Connection con ) {
		
		 return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().cargarMaximoCodigoLogPlantratamiento(dtoPlan,  con );
	}
	
	
	


	/**
	 * @param ingreso
	 * @return
	 */
	public static BigDecimal obtenerUltimoCodigoPlanTratamiento(int codigoPaciente) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().obtenerUltimoCodigoPlanTratamiento(codigoPaciente);
	}
	
	
	
	 /**
	 * 
	 * Método para obtener el ultimo codigo pk de la cita de un programa servicio del plan tratamiento dado el codigo_pk de la tabla programas servicios plan t
	 * También verifica si el programa o servicio Excluido tiene motivo
	 *  
	 * @param codigoPkProgServPlanT
	 * @param fechaFormatoAppNOREQUERIDA
	 * @param estado
	 * 
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public static BigDecimal obtenerCodigoPkUltimaCitaProgServPlanT(BigDecimal codigoPkProgServPlanT, String fechaFormatoAppNOREQUERIDA, ArrayList<String> estado) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().obtenerCodigoPkUltimaCitaProgServPlanT(codigoPkProgServPlanT, fechaFormatoAppNOREQUERIDA,estado);
	}
	
	
	/**
	 * Método que se encarga de consultar si existen registros de detalle
	 * plan tratamiento asociados a una cita específica y que no tienen asociada
	 * una clasificación
	 * 
	 * 
	 * @param codigoCita
	 * @return
	 */
	public static boolean existeDetPlanTratamientoSinClasificacion (int codigoCita){
	
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().existeDetPlanTratamientoSinClasificacion(codigoCita);
	}
	
	
	/**
	 * Método que obtiene el codigo de los detalles de plan de tratamiento asociados a un
	 * programa Hallazgo pieza específico.
	 * 
	 * @param programaHallazgoPieza
	 * @return
	 */
	public static int obtenerDetPlanTratamientoXProgramaHallazgoPieza(int programaHallazgoPieza){
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPlanTratamientoDao().obtenerDetPlanTratamientoXProgramaHallazgoPieza(programaHallazgoPieza);
	}

}