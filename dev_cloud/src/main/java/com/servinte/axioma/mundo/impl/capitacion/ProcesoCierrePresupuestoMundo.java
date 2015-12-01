package com.servinte.axioma.mundo.impl.capitacion;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;
import org.axioma.util.log.Log4JManager;

import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dto.capitacion.DtoInconsistenciasProcesoPresupuestoCapitado;
import com.princetonsa.dto.capitacion.DtoLogCierrePresuCapita;
import com.princetonsa.dto.capitacion.DtoProcesoPresupuestoCapitado;
import com.princetonsa.dto.capitacion.DtoTotalProcesoPresupuestoCapitado;
import com.servinte.axioma.mundo.fabrica.capitacion.CapitacionFabricaMundo;
import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.ICierrePresupuestoCapitacionoMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IProcesoAutorizacionesPresupuestoCapitacionMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IProcesoCargosCuentaConvenioContratoPresupuestoCapitacionMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IProcesoCierrePresupuestoMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IProcesoFacturacionPresupuestoCapitacionMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IProcesoOrdenesPresupuestoCapitacionMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IContratoMundo;
import com.servinte.axioma.orm.Articulo;
import com.servinte.axioma.orm.CierreNivelAteClasein;
import com.servinte.axioma.orm.CierreNivelAteGruServ;
import com.servinte.axioma.orm.Contratos;
import com.servinte.axioma.orm.Convenios;
import com.servinte.axioma.orm.LogCierrePresuCapita;
import com.servinte.axioma.orm.Servicios;
import com.servinte.axioma.orm.Usuarios;
import com.servinte.axioma.persistencia.UtilidadTransaccion;

/**
 * @author Cristhian Murillo
 */
public class ProcesoCierrePresupuestoMundo implements IProcesoCierrePresupuestoMundo
{

	/** * Contiene la lista de mensajes correspondiente a esta forma */
	MessageResources fuenteMensaje = MessageResources.getMessageResources("com.servinte.mensajes.capitacion.ProcesoCierrePresupuestoForm");
	
	//---------------------------------------------------------------------------------------------------
	/* Mundos */
	private ICierrePresupuestoCapitacionoMundo cierrePresupuestoCapitacionoMundo;
	private IProcesoFacturacionPresupuestoCapitacionMundo procesoFacturacionPresupuestoCapitacionMundo;
	private IProcesoAutorizacionesPresupuestoCapitacionMundo procesoAutorizacionesPresupuestoCapitacionMundo;
	private IProcesoOrdenesPresupuestoCapitacionMundo procesoOrdenesPresupuestoCapitacionMundo;
	private IProcesoCargosCuentaConvenioContratoPresupuestoCapitacionMundo procesoCargosCuentaConvenioContratoPresupuestoCapitacionMundo;
	private IContratoMundo contratoMundo;
	//---------------------------------------------------------------------------------------------------
	
	//---------------------------------------------------------------------------------------------------
	/* Listas de los procesos */
	private ArrayList<DtoTotalProcesoPresupuestoCapitado> totalListaAgrupadaServicio;
	private ArrayList<DtoTotalProcesoPresupuestoCapitado> totalListaAgrupadaArticulo;
	private ArrayList<DtoInconsistenciasProcesoPresupuestoCapitado> listaTotalInconsistencia;
	private ArrayList<Contratos> listaContratos;
	// --------------------------------------------------------------------------------------------------
	
	//---------------------------------------------------------------------------------------------------
	
	@SuppressWarnings("unused")
	private boolean esAutomatico = false;
	private String horaGeneracion;
	private Date fechaGeneracion;
	private String loginUsusuario; 
	private String observaciones; 
	private boolean noInformacion;
	private ArrayList<Date> listaFechasCierre = new ArrayList<Date>();
	
	/* Listas de las entidades */
	private ArrayList<CierreNivelAteClasein> listaCierreNivelAteClasein;
	private ArrayList<CierreNivelAteGruServ> listaCierreNivelAteGruServ;
	private ArrayList<LogCierrePresuCapita> listaLogCierrePresuCapitaErrores;
	private ArrayList<LogCierrePresuCapita> listaLogCierrePresuCapitaGenerados;
	//---------------------------------------------------------------------------------------------------
	
	
	/**
	 * Metodo Constructor de la clase
	 * @author Cristhian Murillo
	*/
	public ProcesoCierrePresupuestoMundo (){
		cierrePresupuestoCapitacionoMundo 	= CapitacionFabricaMundo.crearCierrePresupuestoCapitacionoMundo();
	}
	
	
	@Override
	public void generarCierrePresupuesto(DtoProcesoPresupuestoCapitado presupuestoCapitado) 
	{
		UtilidadTransaccion.getTransaccion().begin();
		
		String fechaInicioCierreOrdenMedicaStr = ValoresPorDefecto.getFechaInicioCierreOrdenMedica(presupuestoCapitado.getInstitucion());
		String mensajeConcreto	= null;
		ActionErrors errores	= new ActionErrors();
		errores = presupuestoCapitado.getErrores();
		boolean continuar = false;
		
		if(!UtilidadTexto.isEmpty(fechaInicioCierreOrdenMedicaStr))
		{
			Date fechaInicioCierreOrdenMedica = UtilidadFecha.conversionFormatoFechaStringDate(fechaInicioCierreOrdenMedicaStr);
			
			if(presupuestoCapitado.getFechaInicio().before(fechaInicioCierreOrdenMedica))
			{
				mensajeConcreto = fuenteMensaje.getMessage("ProcesoCierrePresupuestoForm.fechaInicialMayorParametro", fechaInicioCierreOrdenMedicaStr);
				errores = retornarErrorEnviado(mensajeConcreto, errores);
			}
			else{
				
				if(!presupuestoCapitado.getFechaInicio().equals(fechaInicioCierreOrdenMedica))
				{
					Date fechaInicialAnterior = UtilidadFecha.conversionFormatoFechaStringDate(UtilidadFecha.incrementarDiasAFecha(UtilidadFecha.conversionFormatoFechaAAp(presupuestoCapitado.getFechaInicio()), -1, false));
					
					DtoTotalProcesoPresupuestoCapitado parametros = new DtoTotalProcesoPresupuestoCapitado();
					parametros.setFecha(fechaInicialAnterior);
					
					ArrayList<DtoTotalProcesoPresupuestoCapitado> listaCierres = new ArrayList<DtoTotalProcesoPresupuestoCapitado>();
					listaCierres = cierrePresupuestoCapitacionoMundo.obtenerCierresPresupuestocapitacion(parametros);
					
					
					if(!Utilidades.isEmpty(listaCierres))
					{
						continuar = true; // La fecha de cierre ANTERIOR SI se encuentra asociada a un cierre
					}
					else
					{	
						//-------------------------------------------------------------------------------------------
						if(!existenLogsCierre(fechaInicialAnterior)){
							mensajeConcreto = fuenteMensaje.getMessage("ProcesoCierrePresupuestoForm.fechaInicialNoAsociadaCierre", UtilidadFecha.conversionFormatoFechaAAp(fechaInicialAnterior)+"");
							errores = retornarErrorEnviado(mensajeConcreto, errores);
						}
						else {
							continuar = true; // Asociado a un cierre sin datos
						}
						//-------------------------------------------------------------------------------------------
					}
				}
				else{
					continuar = true; // La fecha de cierre es la misma que la fecha parametrizada para hacer el cierre
				}
			}
		}
		
		
		inicializarListasEntidades();
		
		if(continuar)
		{
			UtilidadTransaccion.getTransaccion().begin();
			
			this.procesoFacturacionPresupuestoCapitacionMundo 					= CapitacionFabricaMundo.crearProcesoFacturacionPresupuestoCapitacionMundo();
			this.procesoAutorizacionesPresupuestoCapitacionMundo 				= CapitacionFabricaMundo.crearProcesoAutorizacionesPresupuestoCapitacionMundo();
			this.procesoCargosCuentaConvenioContratoPresupuestoCapitacionMundo	= CapitacionFabricaMundo.crearProcesoCargosCuentaConvenioContratoPresupuestoCapitacionMundo();
			this.procesoOrdenesPresupuestoCapitacionMundo						= CapitacionFabricaMundo.crearProcesoOrdenesPresupuestoCapitacionMundo();
			
			this.listaContratos.addAll(contratoMundo.listarContratos(presupuestoCapitado.getInstitucion()));
			
			this.horaGeneracion 	= UtilidadFecha.getHoraActual();
			this.fechaGeneracion	= UtilidadFecha.getFechaActualTipoBD();
			UtilidadTransaccion.getTransaccion().commit();
			
			Date fechaCierre 		= presupuestoCapitado.getFechaInicio();
			Date fechaFin 		= presupuestoCapitado.getFechaFin();
			while(fechaCierre.before(fechaFin)|| fechaCierre.equals(fechaFin))
			{
				presupuestoCapitado.setFechaInicio(fechaCierre);
				presupuestoCapitado.setFechaFin(fechaCierre);
				
					UtilidadTransaccion.getTransaccion().begin();
			
					// Facturados
					limpiarListasProceso();
					procesoFacturacionPresupuestoCapitacionMundo.realizarProcesoFacturacion(presupuestoCapitado);
						this.totalListaAgrupadaServicio.addAll(procesoFacturacionPresupuestoCapitacionMundo.obtenerTotalListaAgrupadaServicioNivelAtencionGrupoServicio());
						this.totalListaAgrupadaArticulo.addAll(procesoFacturacionPresupuestoCapitacionMundo.obtenerTotalListaAgrupadaArticuloNivelAtencionClaseInventario());
						if(procesoFacturacionPresupuestoCapitacionMundo.obtenerTotalListaAgrupadaServicioNivelAtencionGrupoServicio().size()==0
								&&procesoFacturacionPresupuestoCapitacionMundo.obtenerTotalListaAgrupadaArticuloNivelAtencionClaseInventario().size()==0)
						{
							presupuestoCapitado.setNoInformacion(true);
						}
						else
						{
							presupuestoCapitado.setNoInformacion(false);
						}
						
						crearListasEntidadesGuardarPorProceso(ConstantesIntegridadDominio.acronimoTipoProcesoFacturacion, presupuestoCapitado);
					
					// Autorizados
					limpiarListasProceso();
					procesoAutorizacionesPresupuestoCapitacionMundo.realizarProcesoAutorizacion(presupuestoCapitado);
						this.listaTotalInconsistencia.addAll(procesoAutorizacionesPresupuestoCapitacionMundo.obtenerListaInconsistenciasProcesoAutoriz());
						this.totalListaAgrupadaServicio.addAll(procesoAutorizacionesPresupuestoCapitacionMundo.obtenerTotalListaAgrupadaServicio());
						this.totalListaAgrupadaArticulo.addAll(procesoAutorizacionesPresupuestoCapitacionMundo.obtenerTotalListaAgrupadaArticulo());
						if(procesoAutorizacionesPresupuestoCapitacionMundo.obtenerTotalListaAgrupadaServicio().size()==0
								&&procesoAutorizacionesPresupuestoCapitacionMundo.obtenerTotalListaAgrupadaArticulo().size()==0
								&&procesoAutorizacionesPresupuestoCapitacionMundo.obtenerListaInconsistenciasProcesoAutoriz().size()==0)
						{
							presupuestoCapitado.setNoInformacion(true);
						}
						else
						{
							presupuestoCapitado.setNoInformacion(false);
						}
						crearListasEntidadesGuardarPorProceso(ConstantesIntegridadDominio.acronimoTipoProcesoAutorizacion, presupuestoCapitado);
					if(!Utilidades.isEmpty(this.listaTotalInconsistencia)){
						crearListaLogCierrePresuCapitaParcial(ConstantesIntegridadDominio.acronimoTipoProcesoAutorizacion);
					}
					
					// Cargados
					limpiarListasProceso();
					procesoCargosCuentaConvenioContratoPresupuestoCapitacionMundo.realizarProcesoCargosCuenta(presupuestoCapitado);
						this.listaTotalInconsistencia.addAll(procesoCargosCuentaConvenioContratoPresupuestoCapitacionMundo.obtenerListaInconsistenciasProcesoCargos());
						this.totalListaAgrupadaServicio.addAll(procesoCargosCuentaConvenioContratoPresupuestoCapitacionMundo.obtenerTotalListaAgrupadaServicio());
						this.totalListaAgrupadaArticulo.addAll(procesoCargosCuentaConvenioContratoPresupuestoCapitacionMundo.obtenerTotalListaAgrupadaArticulo());
						if(procesoCargosCuentaConvenioContratoPresupuestoCapitacionMundo.obtenerTotalListaAgrupadaServicio().size()==0
								&&procesoCargosCuentaConvenioContratoPresupuestoCapitacionMundo.obtenerTotalListaAgrupadaArticulo().size()==0
								&&procesoCargosCuentaConvenioContratoPresupuestoCapitacionMundo.obtenerListaInconsistenciasProcesoCargos().size()==0)
						{
							presupuestoCapitado.setNoInformacion(true);
						}
						else
						{
							presupuestoCapitado.setNoInformacion(false);
						}
						crearListasEntidadesGuardarPorProceso(ConstantesIntegridadDominio.acronimoTipoProcesoCargoCuenta, presupuestoCapitado);
					if(!Utilidades.isEmpty(this.listaTotalInconsistencia)){
						crearListaLogCierrePresuCapitaParcial(ConstantesIntegridadDominio.acronimoTipoProcesoCargoCuenta);
					}
					
					// Ordenados
					limpiarListasProceso();
					procesoOrdenesPresupuestoCapitacionMundo.realizarProcesoOrdenes(presupuestoCapitado);
						this.listaTotalInconsistencia.addAll(procesoOrdenesPresupuestoCapitacionMundo.obtenerListaInconsistenciasProcesoOrdenes());
						this.totalListaAgrupadaServicio.addAll(procesoOrdenesPresupuestoCapitacionMundo.obtenerTotalListaPorServicio());
						this.totalListaAgrupadaArticulo.addAll(procesoOrdenesPresupuestoCapitacionMundo.obtenerTotalListaPorArticulo());
						if(procesoOrdenesPresupuestoCapitacionMundo.obtenerTotalListaPorServicio().size()==0
								&&procesoOrdenesPresupuestoCapitacionMundo.obtenerTotalListaPorArticulo().size()==0
								&&procesoOrdenesPresupuestoCapitacionMundo.obtenerListaInconsistenciasProcesoOrdenes().size()==0)
						{
							presupuestoCapitado.setNoInformacion(true);
						}
						else
						{
							presupuestoCapitado.setNoInformacion(false);
						}
						crearListasEntidadesGuardarPorProceso(ConstantesIntegridadDominio.acronimoTipoProcesoOrden, presupuestoCapitado);
					if(!Utilidades.isEmpty(this.listaTotalInconsistencia)){
						crearListaLogCierrePresuCapitaParcial(ConstantesIntegridadDominio.acronimoTipoProcesoOrden);
					}
		
					
					UtilidadTransaccion.getTransaccion().commit();
					
					
					if(Utilidades.isEmpty(this.listaLogCierrePresuCapitaErrores))
					{
						guardarCierrePresupuesto(presupuestoCapitado.getContrato(), presupuestoCapitado.getConvenio());
					}
					else
					{
						if(errores.isEmpty())
						{
							mensajeConcreto = fuenteMensaje.getMessage("CierrePresupuestoForm.noCierre", fechaInicioCierreOrdenMedicaStr);
							errores = retornarErrorEnviado(mensajeConcreto, errores);
						}
						
						//FIXME Verificar no guardar proceso exitoso cuando hay inconsistencia en un proceso
						guardarCierrePresupuesto(presupuestoCapitado.getContrato(), presupuestoCapitado.getConvenio());
						guardarLogCierrePresupuesto();
					}
				fechaCierre = UtilidadFecha.conversionFormatoFechaStringDate(UtilidadFecha.incrementarDiasAFecha(UtilidadFecha.conversionFormatoFechaAAp(fechaCierre), 1, false));
			}
			
		}
		
		
		presupuestoCapitado.setErrores(errores);
	}
	
	
	/**
	 * Guarda logs de procesos exitosos
	 * @autor Cristhian Murillo
	 */
	private void guardarLogCierrePresupuestoExitosos(Integer contrato, Integer convenio) 
	{
		for (LogCierrePresuCapita logCierrePresuCapita : this.listaLogCierrePresuCapitaGenerados) 
		{
			if(contrato!=null)
			{
				Contratos contratos; contratos = new Contratos(); contratos.setCodigo(contrato);
				logCierrePresuCapita.setContratos(contratos); 
			}
			if(convenio!=null)
			{
				Convenios convenios; convenios = new Convenios(); convenios.setCodigo(convenio);
				logCierrePresuCapita.setConvenios(convenios);
			}
			if(UtilidadTexto.isEmpty(logCierrePresuCapita.getDescripcion())){
				logCierrePresuCapita.setDescripcion(logCierrePresuCapita.getTipoInconsistencia());
			}
			cierrePresupuestoCapitacionoMundo.guardarActualizarLogCierrePresuCapita(logCierrePresuCapita);
		}
	}

	

	/**
	 * Crea las listas de las entidades a guardar.
	 * 
	 * @param tipoProceso
	 * @param dtoProcesoPresupuestoCapitado
	 *
	 * @autor Cristhian Murillo
	 */
	private void crearListasEntidadesGuardarPorProceso(String tipoProceso, DtoProcesoPresupuestoCapitado dtoProcesoPresupuestoCapitado) 
	{ 
		Date fechaCierre 		= dtoProcesoPresupuestoCapitado.getFechaInicio();
		
		this.loginUsusuario		= dtoProcesoPresupuestoCapitado.getLoginUsuario();
		this.observaciones		= dtoProcesoPresupuestoCapitado.getObservaciones();
		this.noInformacion		= dtoProcesoPresupuestoCapitado.isNoInformacion();
		
		this.listaFechasCierre= new ArrayList<Date>();
		
		while(fechaCierre.before(dtoProcesoPresupuestoCapitado.getFechaFin()) || fechaCierre.equals(dtoProcesoPresupuestoCapitado.getFechaFin()))
		{
			for (Contratos contratos : this.listaContratos) 
			{
				//this.listaFechasCierre.add(fechaCierre);
				
				Integer codContrato = contratos.getCodigo();
				Integer codConvenio = contratos.getConvenios().getCodigo(); 
				
				crearListaCierreNivelAteClasein(tipoProceso, codContrato, fechaCierre, codConvenio);
				crearListaCierreNivelAteGruServ(tipoProceso, codContrato, fechaCierre, codConvenio);
				
				
			}
			if(!this.listaFechasCierre.contains(fechaCierre))
				this.listaFechasCierre.add(fechaCierre);
			
			if(tipoProceso.equals(ConstantesIntegridadDominio.acronimoTipoProcesoFacturacion)){
				if(Utilidades.isEmpty(this.listaLogCierrePresuCapitaErrores))
				{
					crearListaLogCierrePresuCapitaGenerados(tipoProceso, fechaCierre);
				}
			}else
			if(Utilidades.isEmpty(this.listaTotalInconsistencia))
			{
				crearListaLogCierrePresuCapitaGenerados(tipoProceso, fechaCierre);
			}
			 
			fechaCierre = UtilidadFecha.conversionFormatoFechaStringDate(UtilidadFecha.incrementarDiasAFecha(UtilidadFecha.conversionFormatoFechaAAp(fechaCierre), 1, false));
		}
		
		
	}


	/**
	 * Genera logs de procesos exitosos
	 * @param tipoProceso
	 * @param fechaCierre
	 *
	 * @autor Cristhian Murillo
	 */
	private void crearListaLogCierrePresuCapitaGenerados(String tipoProceso, Date fechaCierre) 
	{
		LogCierrePresuCapita logCierrePresuCapita; logCierrePresuCapita = new LogCierrePresuCapita();
		logCierrePresuCapita.setHoraGeneracion(this.horaGeneracion);
		logCierrePresuCapita.setFechaGeneracion(this.fechaGeneracion);
		logCierrePresuCapita.setFechaCierre(fechaCierre);
		
		logCierrePresuCapita.setNoInformacion(this.noInformacion);
		/*
		Contratos contratos; contratos = new Contratos(); contratos.setCodigo(codContrato);
		logCierrePresuCapita.setContratos(contratos); 
		Convenios convenios; convenios = new Convenios(); convenios.setCodigo(codConvenio);
		logCierrePresuCapita.setConvenios(convenios);
		*/
		logCierrePresuCapita.setProceso(tipoProceso);
		if(this.loginUsusuario != null){
			Usuarios usuarios = new Usuarios();
			usuarios.setLogin(this.loginUsusuario);
			logCierrePresuCapita.setUsuarios(usuarios);
		}
		logCierrePresuCapita.setServiArti(ConstantesIntegridadDominio.acronimoProcesoExitoso);
		logCierrePresuCapita.setTipoInconsistencia(ConstantesIntegridadDominio.acronimoProcesoExitoso);
		logCierrePresuCapita.setDescripcion(ConstantesIntegridadDominio.acronimoProcesoExitoso);
		logCierrePresuCapita.setEstado(ConstantesIntegridadDominio.acronimoEstadoFinalizado);
		logCierrePresuCapita.setObservaciones(fuenteMensaje.getMessage("CierrePresupuestoForm.procesoExitoso"));
		
		this.listaLogCierrePresuCapitaGenerados.add(logCierrePresuCapita);
	}


	/**
	 * Crea la lista de la entidad.
	 * @param tipoProceso
	 * @param codContrato
	 * @param codConvenio
	 * @param fechaCierre
	 *
	 * @autor Cristhian Murillo
	 */
	private void crearListaCierreNivelAteGruServ(String tipoProceso, Integer codContrato, Date fechaCierre, Integer codConvenio) 
	{
		Contratos contratos; contratos = new Contratos(); contratos.setCodigo(codContrato);
		Convenios convenios; convenios = new Convenios(); convenios.setCodigo(codConvenio);
		Log4JManager.info("TipoProceso Servicio: "+tipoProceso);
		for (DtoTotalProcesoPresupuestoCapitado agrupadaServicio : this.totalListaAgrupadaServicio) 
		{
			if(agrupadaServicio!=null)
			{
				if(!tipoProceso.equals(ConstantesIntegridadDominio.acronimoTipoProcesoFacturacion)){
					if(agrupadaServicio.getFecha().toString().contains("-")){
						agrupadaServicio.setFecha(UtilidadFecha.conversionFormatoFechaStringDate(agrupadaServicio.getFecha()+""));
					}
				}
				
				if(agrupadaServicio.getContrato() == contratos.getCodigo() && agrupadaServicio.getFecha().equals(fechaCierre))
				{
					CierreNivelAteGruServ cierreNivelAteGruServ; cierreNivelAteGruServ = new CierreNivelAteGruServ();
					cierreNivelAteGruServ.setContratos(contratos);
					cierreNivelAteGruServ.setTipoProceso(tipoProceso);
					cierreNivelAteGruServ.setFechaCierre(fechaCierre);
					cierreNivelAteGruServ.setValorAcumulado(new BigDecimal(agrupadaServicio.getValor()));
					cierreNivelAteGruServ.setCantidad(agrupadaServicio.getCantidadTotal());
					Servicios servicios; servicios = new Servicios();
					servicios.setCodigo(agrupadaServicio.getCodigoServicio());
					cierreNivelAteGruServ.setServicios(servicios);
					cierreNivelAteGruServ.setObservaciones(this.observaciones);
					
					this.listaCierreNivelAteGruServ.add(cierreNivelAteGruServ);
					if(tipoProceso.equals(ConstantesIntegridadDominio.acronimoTipoProcesoFacturacion)){
					validarInconsistencias(tipoProceso, contratos, fechaCierre, agrupadaServicio, codConvenio);
					}
				}
			}
		}
	}





	/**
	 * Crea la lista de la entidad.
	 * @param tipoProceso
	 * @param codContrato
	 * @param codConvenio
	 * @param fechaCierre
	 *
	 * @autor Cristhian Murillo
	 */
	private void crearListaCierreNivelAteClasein(String tipoProceso, Integer codContrato, Date fechaCierre, Integer codConvenio) 
	{
		Contratos contratos; contratos = new Contratos(); contratos.setCodigo(codContrato);
		Convenios convenios; convenios = new Convenios(); convenios.setCodigo(codConvenio);
		Log4JManager.info("TipoProceso Articulo: "+tipoProceso);
		
		for (DtoTotalProcesoPresupuestoCapitado agrupadaArticulo : this.totalListaAgrupadaArticulo) 
		{
			if(agrupadaArticulo!=null)
			{
				if(!tipoProceso.equals(ConstantesIntegridadDominio.acronimoTipoProcesoFacturacion)){
					if(agrupadaArticulo.getFecha().toString().contains("-")){
						agrupadaArticulo.setFecha(UtilidadFecha.conversionFormatoFechaStringDate(agrupadaArticulo.getFecha()+""));
					}
				}
				
				if(agrupadaArticulo.getContrato() == contratos.getCodigo() && agrupadaArticulo.getFecha().equals(fechaCierre))
				{
					if(Utilidades.isEmpty(agrupadaArticulo.getListaInconsistencias())){
						CierreNivelAteClasein cierreNivelAteClasein; cierreNivelAteClasein = new CierreNivelAteClasein();
						cierreNivelAteClasein.setContratos(contratos);
						cierreNivelAteClasein.setTipoProceso(tipoProceso);
						cierreNivelAteClasein.setFechaCierre(fechaCierre);
						cierreNivelAteClasein.setValorAcumulado(new BigDecimal(agrupadaArticulo.getValor()));
						cierreNivelAteClasein.setCantidad(agrupadaArticulo.getCantidadTotal());
						Articulo articulo; articulo = new Articulo();
						articulo.setCodigo(agrupadaArticulo.getCodigoArticulo());
						cierreNivelAteClasein.setArticulo(articulo);
						cierreNivelAteClasein.setObservaciones(this.observaciones);
						
						this.listaCierreNivelAteClasein.add(cierreNivelAteClasein);
					}
					
					if(tipoProceso.equals(ConstantesIntegridadDominio.acronimoTipoProcesoFacturacion)){
					validarInconsistencias(tipoProceso, contratos, fechaCierre, agrupadaArticulo, codConvenio);
					}
				}
			}
		}
	}
	



	
	
	/**
	 * Inicializa las listas de entidades para el proceso de cierre
	 * @autor Cristhian Murillo
	*/
	private void inicializarListasEntidades() 
	{
		this.horaGeneracion				= null;
		this.fechaGeneracion			= null;
		this.contratoMundo				= FacturacionFabricaMundo.crearContratoMundo();
		this.listaCierreNivelAteClasein	= new ArrayList<CierreNivelAteClasein>();
		this.listaCierreNivelAteGruServ	= new ArrayList<CierreNivelAteGruServ>();
		this.listaLogCierrePresuCapitaErrores	= new ArrayList<LogCierrePresuCapita>();
		this.listaLogCierrePresuCapitaGenerados	= new ArrayList<LogCierrePresuCapita>();
		this.listaContratos				= new ArrayList<Contratos>();
	}
	

	/**
	 * Retorna el mensaje de error enviado
	 * @param mensajeConcreto
	 * @param errores
	 * 
	 * @autor Cristhian Murillo
	 */
	private ActionErrors retornarErrorEnviado(String mensajeConcreto, ActionErrors errores) {
		errores.add("error_concreto_enviado", new ActionMessage("errors.notEspecific", mensajeConcreto));
		return errores;
	}
	

	/**
	 * Inicializa las listas de los procesos para el proceso de cierre
	 * @autor Cristhian Murillo
	*/
	private void limpiarListasProceso() 
	{
		this.totalListaAgrupadaServicio = new ArrayList<DtoTotalProcesoPresupuestoCapitado>();
		this.totalListaAgrupadaArticulo = new ArrayList<DtoTotalProcesoPresupuestoCapitado>();
		this.listaTotalInconsistencia	= new ArrayList<DtoInconsistenciasProcesoPresupuestoCapitado>();
	}

	
	/**
	 * Guarda las entidades creadas
	 * @autor Cristhian Murillo
	*/
	private void guardarCierrePresupuesto(Integer contrato, Integer convenio)
	{
		UtilidadTransaccion.getTransaccion().begin();
		
		try 
		{
			DtoTotalProcesoPresupuestoCapitado parametros = new DtoTotalProcesoPresupuestoCapitado();
			
			
			// Se eliminan los cierres existentes para las fechas de cierre
			for (Date fechaCierre : this.listaFechasCierre) 
			{
				parametros.setFecha(fechaCierre);
				parametros.setContrato(contrato);
				
				ArrayList<CierreNivelAteClasein> listaCierresArticuloFechaBorrar = cierrePresupuestoCapitacionoMundo.obtenerCierresCierreNivelAteClasein(parametros);
				ArrayList<CierreNivelAteClasein> listaDefinitivaBorrarArticulo= new ArrayList<CierreNivelAteClasein>();
				if(convenio!=null&&contrato==null)
				{
					for(CierreNivelAteClasein cierreArticulo:listaCierresArticuloFechaBorrar)
					{
						if(cierreArticulo.getContratos().getConvenios().getCodigo()==convenio)
						{
							listaDefinitivaBorrarArticulo.add(cierreArticulo);
						}
					}
					for (CierreNivelAteClasein cierreArticuloBorrar : listaDefinitivaBorrarArticulo) {
						cierrePresupuestoCapitacionoMundo.eliminarCierreNivelAteClasein(cierreArticuloBorrar);
					}
				}else
				{
					for (CierreNivelAteClasein cierreArticuloBorrar : listaCierresArticuloFechaBorrar) {
						cierrePresupuestoCapitacionoMundo.eliminarCierreNivelAteClasein(cierreArticuloBorrar);
					}
				}
				
				
				
				ArrayList<CierreNivelAteGruServ> listaCierresServicioFechaBorrar = cierrePresupuestoCapitacionoMundo.obtenerCierresCierreNivelAteGruServ(parametros);
				ArrayList<CierreNivelAteGruServ> listaDefinitivaBorrarServicio= new ArrayList<CierreNivelAteGruServ>();
				if(convenio!=null&&contrato==null)
				{
					for(CierreNivelAteGruServ cierreServicio:listaCierresServicioFechaBorrar)
					{
						if(cierreServicio.getContratos().getConvenios().getCodigo()==convenio)
						{
							listaDefinitivaBorrarServicio.add(cierreServicio);
						}
					}
					for (CierreNivelAteGruServ cierreServicioBorrar : listaDefinitivaBorrarServicio) {
						cierrePresupuestoCapitacionoMundo.eliminarCierreNivelAteGruServ(cierreServicioBorrar);
					}
				}else
				{
					for (CierreNivelAteGruServ cierreServicioBorrar : listaCierresServicioFechaBorrar) {
						cierrePresupuestoCapitacionoMundo.eliminarCierreNivelAteGruServ(cierreServicioBorrar);
					}
				}
			}
			
			
			for (CierreNivelAteClasein cierreNivelAteClasein : this.listaCierreNivelAteClasein) 
			{
				cierrePresupuestoCapitacionoMundo.guardarActualizarCierreNivelAteClasein(cierreNivelAteClasein);
			}
			
			for (CierreNivelAteGruServ cierreNivelAteGruServ : this.listaCierreNivelAteGruServ) 
			{
				cierrePresupuestoCapitacionoMundo.guardarActualizarCierreNivelAteGruServ(cierreNivelAteGruServ);
			}
			 
			guardarLogCierrePresupuestoExitosos(contrato, convenio);
			
			UtilidadTransaccion.getTransaccion().commit();
			
		} catch (Exception e) {
			e.printStackTrace();
			UtilidadTransaccion.getTransaccion().rollback();
		}
	}
	
	
	/**
	 * Valida si existen inconsistencias
	 * @param tipoProceso
	 * @param contratos
	 * @param fechaCierre
	 * @param agrupado
	 * @param codConvenio
	 *
	 * @autor Cristhian Murillo
	 */
	private void validarInconsistencias(String tipoProceso, Contratos contratos, Date fechaCierre, DtoTotalProcesoPresupuestoCapitado agrupado, Integer codConvenio)
	{
		if(!Utilidades.isEmpty(agrupado.getListaInconsistencias()))
		{
			for (DtoInconsistenciasProcesoPresupuestoCapitado insoncistencia : agrupado.getListaInconsistencias()) 
			{
				LogCierrePresuCapita logCierrePresuCapita; logCierrePresuCapita = new LogCierrePresuCapita();
				logCierrePresuCapita.setHoraGeneracion(this.horaGeneracion);
				logCierrePresuCapita.setFechaGeneracion(this.fechaGeneracion);
				
				logCierrePresuCapita.setNoInformacion(false);
				
				logCierrePresuCapita.setFechaCierre(fechaCierre);
				logCierrePresuCapita.setContratos(contratos); 
				Convenios convenios; convenios = new Convenios(); convenios.setCodigo(codConvenio);
				logCierrePresuCapita.setConvenios(convenios);
				logCierrePresuCapita.setProceso(tipoProceso);
				if(this.loginUsusuario != null){
					Usuarios usuarios = new Usuarios();
					usuarios.setLogin(this.loginUsusuario);
					logCierrePresuCapita.setUsuarios(usuarios);
				}
				logCierrePresuCapita.setServiArti(insoncistencia.getServicioMedicamento());
				logCierrePresuCapita.setTipoInconsistencia(insoncistencia.getTipoInconsistencia());
				logCierrePresuCapita.setDescripcion(insoncistencia.getDescripcion());
				logCierrePresuCapita.setEstado(ConstantesIntegridadDominio.acronimoLactanciaParcial);
				logCierrePresuCapita.setObservaciones(this.observaciones);
				
				this.listaLogCierrePresuCapitaErrores.add(logCierrePresuCapita);
			}
		}
	}
	
	
	
	/**
	 * Guarda el log del cierre de presupuesto.
	 * @autor Cristhian Murillo
	*/
	private void guardarLogCierrePresupuesto()
	{
		UtilidadTransaccion.getTransaccion().begin();
		
		try 
		{
			for (LogCierrePresuCapita logCierrePresuCapita : this.listaLogCierrePresuCapitaErrores) 
			{
				if(UtilidadTexto.isEmpty(logCierrePresuCapita.getDescripcion())){
					logCierrePresuCapita.setDescripcion(logCierrePresuCapita.getTipoInconsistencia());
				}
				cierrePresupuestoCapitacionoMundo.guardarActualizarLogCierrePresuCapita(logCierrePresuCapita);
			}
			
			UtilidadTransaccion.getTransaccion().commit();
			
		} catch (Exception e) {
			UtilidadTransaccion.getTransaccion().rollback();
		}
	}


	
	@Override
	public void generarCierrePresupuestoAutomatico(DtoProcesoPresupuestoCapitado presupuestoCapitado) 
	{
		boolean continuar = true;
		this.esAutomatico = true;
		
		Date fechaCierreAutomatico = UtilidadFecha.getFechaActualTipoBD();
		Date fechaCierre = UtilidadFecha.conversionFormatoFechaStringDate(UtilidadFecha.incrementarDiasAFecha(UtilidadFecha.conversionFormatoFechaAAp(fechaCierreAutomatico), -1, false));
		presupuestoCapitado.setFechaInicio(fechaCierre);
		presupuestoCapitado.setFechaFin(fechaCierre);
		presupuestoCapitado.setFechaCierre(fechaCierre);
		
		UtilidadTransaccion.getTransaccion().begin();
		
		try {
			// Validación párametros generales
			String fechaInicioCierreOrdenMedica = ValoresPorDefecto.getFechaInicioCierreOrdenMedica(presupuestoCapitado.getInstitucion());		
			if(UtilidadTexto.isEmpty(fechaInicioCierreOrdenMedica))
			{
				String mensajeConcreto = fuenteMensaje.getMessage("CierrePresupuestoForm.faltaParametroFechaInicioCierreOrdenM");
				guardarLogNoGeneracionAutomatico(mensajeConcreto, presupuestoCapitado.getFechaCierre());
				continuar = false;
			}
			else{
				String horaProcesoAutoCierre = ValoresPorDefecto.getHoraProcesoCierreCapitacion(presupuestoCapitado.getInstitucion());		
				if(UtilidadTexto.isEmpty(horaProcesoAutoCierre))
				{
					String mensajeConcreto = fuenteMensaje.getMessage("CierrePresupuestoForm.faltaParametroCierreAuto");
					guardarLogNoGeneracionAutomatico(mensajeConcreto, presupuestoCapitado.getFechaCierre());
					continuar = false;
				}
				else{
					Date fechaInicioCierreOrdenM=UtilidadFecha.conversionFormatoFechaStringDate(fechaInicioCierreOrdenMedica);
					if(presupuestoCapitado.getFechaCierre().before(fechaInicioCierreOrdenM)){
						continuar=false;
					}
					else{
						if(!presupuestoCapitado.getFechaCierre().equals(fechaInicioCierreOrdenM)){
							DtoTotalProcesoPresupuestoCapitado parametros = new DtoTotalProcesoPresupuestoCapitado();
							Date fechaInicialAnterior = UtilidadFecha.conversionFormatoFechaStringDate(UtilidadFecha.incrementarDiasAFecha(UtilidadFecha.conversionFormatoFechaAAp(presupuestoCapitado.getFechaCierre()), -1, false));
							parametros.setFecha(fechaInicialAnterior);
							ArrayList<DtoTotalProcesoPresupuestoCapitado> listaCierres = new ArrayList<DtoTotalProcesoPresupuestoCapitado>();
							listaCierres = cierrePresupuestoCapitacionoMundo.obtenerCierresPresupuestocapitacion(parametros);
							
							if(Utilidades.isEmpty(listaCierres))
							{
								if(!existenLogsCierre(fechaInicialAnterior))
								{
									continuar = false; // La fecha de cierre NO se encuentra asociada a un cierre
									String mensajeConcreto = fuenteMensaje.getMessage("CierrePresupuestoForm.nocierrePara", UtilidadFecha.conversionFormatoFechaAAp(fechaInicialAnterior));
									guardarLogNoGeneracionAutomatico(mensajeConcreto, presupuestoCapitado.getFechaCierre());
								}
							}
						}
					}
				}
			}
			UtilidadTransaccion.getTransaccion().commit();
			
		} catch (Exception e) {
			continuar=false;
			UtilidadTransaccion.getTransaccion().rollback();
		}
		if (continuar) {
			generarCierrePresupuesto(presupuestoCapitado);
		}
		
	}
	
	
	
	/**
	 * Guarda el log de no generación de cierre automático
	 *
	 * @autor Cristhian Murillo
	 */
	private void guardarLogNoGeneracionAutomatico(String mensajeConcreto, Date fechaCierre)
	{
		Date fechaActual = UtilidadFecha.getFechaActualTipoBD();
		LogCierrePresuCapita logCierrePresuCapita = new LogCierrePresuCapita();
		logCierrePresuCapita.setHoraGeneracion(UtilidadFecha.getHoraActual());
		logCierrePresuCapita.setFechaGeneracion(fechaActual);
		logCierrePresuCapita.setFechaCierre(fechaCierre);
		
		logCierrePresuCapita.setNoInformacion(this.noInformacion);
		
		/* El convenio y contrato son nulos cuando es exitoso ya que no se guarda el detalle del proceso exitoso en el log
		Contratos contratos; contratos = new Contratos(); contratos.setCodigo(1);
		logCierrePresuCapita.setContratos(contratos); 
		Convenios convenios; convenios = new Convenios(); convenios.setCodigo(1);
		logCierrePresuCapita.setConvenios(convenios);
		*/
		
		logCierrePresuCapita.setProceso(ConstantesIntegridadDominio.acronimoProcesoAutomatico);
		logCierrePresuCapita.setServiArti(mensajeConcreto);
		logCierrePresuCapita.setTipoInconsistencia(ConstantesIntegridadDominio.acronimoProcesoAutomatico);
		logCierrePresuCapita.setDescripcion(mensajeConcreto);
		logCierrePresuCapita.setEstado(ConstantesIntegridadDominio.acronimoLactanciaParcial);
		logCierrePresuCapita.setObservaciones(mensajeConcreto);
		
		cierrePresupuestoCapitacionoMundo.guardarActualizarLogCierrePresuCapita(logCierrePresuCapita);
		
	}
	
	
	
	/**
	 * Genera logs de procesos exitosos
	 * @param tipoProceso
	 * @param codContrato
	 * @param fechaCierre
	 * @param codConvenio
	 *
	 * @autor Cristhian Murillo
	 */
	private void crearListaLogCierrePresuCapitaParcial(String tipoProceso) 
	{
		for (DtoInconsistenciasProcesoPresupuestoCapitado inconsistencia : this.listaTotalInconsistencia) 
		{
			LogCierrePresuCapita logCierrePresuCapita; logCierrePresuCapita = new LogCierrePresuCapita();
			logCierrePresuCapita.setHoraGeneracion(this.horaGeneracion);
			logCierrePresuCapita.setFechaGeneracion(this.fechaGeneracion);
			
			logCierrePresuCapita.setNoInformacion(this.noInformacion);
			
			logCierrePresuCapita.setFechaCierre(inconsistencia.getFecha());
			Contratos contratos; contratos = new Contratos(); contratos.setCodigo(inconsistencia.getCodigoContrato());
			logCierrePresuCapita.setContratos(contratos); 
			Convenios convenios; convenios = new Convenios(); convenios.setCodigo(inconsistencia.getCodigoConvenio());
			logCierrePresuCapita.setConvenios(convenios);
			logCierrePresuCapita.setProceso(tipoProceso);
			if(this.loginUsusuario != null){
				Usuarios usuarios = new Usuarios();
				usuarios.setLogin(this.loginUsusuario);
				logCierrePresuCapita.setUsuarios(usuarios);
			}
			logCierrePresuCapita.setServiArti(inconsistencia.getServicioMedicamento());
			logCierrePresuCapita.setTipoInconsistencia(inconsistencia.getTipoInconsistencia());
			logCierrePresuCapita.setDescripcion(inconsistencia.getDescripcion());
			logCierrePresuCapita.setEstado(ConstantesIntegridadDominio.acronimoLactanciaParcial); 
			logCierrePresuCapita.setObservaciones(this.observaciones);
			
			this.listaLogCierrePresuCapitaErrores.add(logCierrePresuCapita);
			
		}
		
	}
	
	
	/**
	 * Verifica si existen logs de proceso exitoso en caso de que existen cierres sin información
	 * @param fechaBusqueda
	 * @param codInstitucion
	 * @return existeLogExitoso
	 *
	 * @autor Cristhian Murillo
	 *
	 */
	private boolean existenLogsCierre(Date fechaBusqueda)
	{
		boolean existeLogExitoso = false;
		
		ArrayList<DtoLogCierrePresuCapita> listaLogs = new ArrayList<DtoLogCierrePresuCapita>();
		
		listaLogs = cierrePresupuestoCapitacionoMundo.obtenerLogsPorFechaSinTipoInconsistencia(fechaBusqueda, ConstantesIntegridadDominio.acronimoProcesoAutomatico);
		
		if(!Utilidades.isEmpty(listaLogs)){
			existeLogExitoso = true;
		}
		
		return existeLogExitoso;
	}

	
	
}
