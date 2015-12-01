
package com.princetonsa.action.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosDouble;
import util.InfoDatosInt;
import util.InfoDatosString;
import util.InfoIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.odontologia.InfoDetallePlanTramiento;
import util.odontologia.InfoHallazgoSuperficie;
import util.odontologia.InfoNumSuperficiesPresupuesto;
import util.odontologia.InfoPlanTratamiento;
import util.odontologia.InfoProgramaServicioPlan;
import util.odontologia.InfoServicios;
import util.odontologia.UtilidadOdontologia;

import com.princetonsa.actionform.odontologia.ProximaCitaProgramadaForm;
import com.princetonsa.dto.administracion.DtoPersonas;
import com.princetonsa.dto.odontologia.DtoDetalleProgramas;
import com.princetonsa.dto.odontologia.DtoDetalleProximaCita;
import com.princetonsa.dto.odontologia.DtoPresuOdoProgServ;
import com.princetonsa.dto.odontologia.DtoPresupuestoOdoConvenio;
import com.princetonsa.dto.odontologia.DtoPresupuestoPiezas;
import com.princetonsa.dto.odontologia.DtoPresupuestoPlanTratamientoNumeroSuperficies;
import com.princetonsa.dto.odontologia.DtoProcesoCitaProgramada;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.odontologia.CitaOdontologica;
import com.princetonsa.mundo.odontologia.NumeroSuperficiesPresupuesto;
import com.princetonsa.mundo.odontologia.PlanTratamiento;
import com.princetonsa.mundo.odontologia.ProgramasOdontologicos;
import com.servinte.axioma.servicio.fabrica.administracion.AdministracionFabricaServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IPacienteServicio;

/**
 * Action que gestiona lo relacionado con el anexo:
 * 
 * 1120 - Proceso Próxima Cita
 * 
 * @author Jorge Armando Agudelo Quintero 
 */

public class ProximaCitaProgramadaAction  extends Action{

	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) {

		if(form instanceof ProximaCitaProgramadaForm)
		{	
			ProximaCitaProgramadaForm forma = (ProximaCitaProgramadaForm) form;
			String estado = forma.getEstado();
			
			UsuarioBasico usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			
			if (estado.equals("abrirPopUpProximaCitaProgramada")){
				
				return mapping.findForward("abrirPopUpProximaCitaProgramada");
			
			}else if (estado.equals("programarProximaCita")){
				
				empezarProcesoProximaCita (forma, usuario.getCodigoInstitucionInt());
				
				DtoProcesoCitaProgramada dtoProcesoCitaProgramada = (DtoProcesoCitaProgramada) request.getSession().getAttribute("informacionProximaCitaProgramada");
				request.getSession().removeAttribute("informacionProximaCitaProgramada");
				
				if(dtoProcesoCitaProgramada!=null){
				
					forma.setDtoProcesoCitaProgramada(dtoProcesoCitaProgramada);
					cargarPaciente(forma);
					direccionarPorFuncionalidad(forma, usuario);
				}
				
				return mapping.findForward("popUpProximaCitaProgramada");
			
			}else if (estado.equals("totalizarDuracionCita") || estado.equals("activarProgramaServicio")){
				
				totalizarDuracionCita(forma);
				return mapping.findForward("totalizarDuracionCita");
				
			}else if (estado.equals("guardarProximaCita")){
				
				return mapping.findForward(guardarProximaCita(forma, usuario, request));
			
			}else if(estado.equals("resumenProximaCita")){
				
				cargarPaciente(forma);
				return mapping.findForward("popUpProximaCitaProgramada");
				
			}else if (estado.equals("volver")){
				
				/*
				 * Cuando trae este estado, quiere decir que va a volver
				 * luego de ver el resumen de la Próxima Cita. 
				 */
				
				request.getSession().setAttribute("volverDesdeResumen", ConstantesBD.acronimoSi);
				
				return mapping.findForward(direccionarFuncionalidadOrigen(forma));
			}
		}
		
		return null;
	}
	
	
	/**
	 * Método que se encarga de inicializar la información necesaria 
	 * para realizar el proceso de programación de próxima cita.
	 * 
	 * @param usuario 
	 * 
	 */
	private void empezarProcesoProximaCita(ProximaCitaProgramadaForm forma, int codigoInstitucion) {
		
		forma.reset();
		
		forma.setUtilizaInstitucionProgramasOdontologicos(ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion
				(codigoInstitucion).equals(ConstantesBD.acronimoSi)? true : false);
	
		forma.setMultiploMinutosGeneracionCita(ValoresPorDefecto.getMultiploMinGeneracionCita(codigoInstitucion));
	
	}

	/**
	 * Método que consulta la información del paciente al cual se le va a 
	 * asociar la cita programada.
	 * 
	 * @param forma
	 */
	private void cargarPaciente (ProximaCitaProgramadaForm forma){
		
		if(forma.getDatosPaciente()==null){
		
			if( forma.getDtoProcesoCitaProgramada()!=null && forma.getDtoProcesoCitaProgramada().getCodigoPersona()>0){
				
				IPacienteServicio pacienteServicio = AdministracionFabricaServicio.crearPacienteServicio();
				
				DtoPersonas datosPaciente = pacienteServicio.obtenerDatosPaciente(forma.getDtoProcesoCitaProgramada().getCodigoPersona());
				
				if(datosPaciente!=null){
					
					forma.setDatosPaciente(datosPaciente);
				}
			}
		}
	}

	/**
	 * Método que determina, dependiendo de la funcionalidad que realiza el llamado, cual es la 
	 * consulta de servicios que debe realizar.
	 * 
	 * @param forma
	 * @param usuario 
	 */
	private void direccionarPorFuncionalidad(ProximaCitaProgramadaForm forma, UsuarioBasico usuario){
		
		/*
		 * Cuando la funcionalidad que origina el llamado es la contratación del presupuesto
		 */
		if(ConstantesBD.codigoFuncionalidadContratarPresupuesto == forma.getCodigoFuncionalidad()){
			
			cargarProgramasServiciosPresupuesto(forma, usuario);
		
		}else if(ConstantesBD.codigoFuncionalidadAtencionCitaOdontologica == forma.getCodigoFuncionalidad()){
			
			cargarProgramasServiciosAtencionCita (forma, usuario);
		}
	}
	
	
	/**
	 * Método encargado de cargar y ajustar los
	 * Programas / Servicios asociados al presupuesto para poder
	 * generar una próxima cita Programada.
	 * 
	 * @param forma 
	 * @param usuario 
	 */
	private void cargarProgramasServiciosPresupuesto (ProximaCitaProgramadaForm forma, UsuarioBasico usuario){
		
		DtoProcesoCitaProgramada dtoProcesoCitaProgramada = forma.getDtoProcesoCitaProgramada();

		ArrayList<String> valoresUnicos= new ArrayList<String>();

		for(DtoPresuOdoProgServ progServ: dtoProcesoCitaProgramada.getProgramasServiciosPresupuesto())
		{
			int contrato=0;
			for(DtoPresupuestoOdoConvenio convenio: progServ.getListPresupuestoOdoConvenio())
			{
				if(UtilidadTexto.getBoolean(convenio.getContratado()))
				{
					contrato=convenio.getContrato().getCodigo();
					break;
				}
			}
			
			//logger.info("contrato=="+contrato);
			
			if(contrato>0)
			{
				for(DtoPresupuestoPiezas pieza: progServ.getListPresupuestoPiezas())
				{
					DtoPresupuestoPlanTratamientoNumeroSuperficies dtoNumSup= new DtoPresupuestoPlanTratamientoNumeroSuperficies();
					dtoNumSup.setHallazgo(new InfoDatosInt(pieza.getHallazgo().intValue()));
					dtoNumSup.setPiezaDental(new InfoDatosInt(pieza.getPieza().intValue()));
					dtoNumSup.setPresupuesto(dtoProcesoCitaProgramada.getCodigoPresupuesto());
					dtoNumSup.setPrograma(new InfoDatosInt(Utilidades.convertirAEntero(progServ.getPrograma().getCodigo()+""), progServ.getPrograma().getNombre()));
					dtoNumSup.setSeccion(new InfoIntegridadDominio(pieza.getSeccion()));
					
					Connection con= UtilidadBD.abrirConexion();
					ArrayList<InfoNumSuperficiesPresupuesto> superficiesAplica= NumeroSuperficiesPresupuesto.obtenerInfoNumSuperficiesPresupuestoXColorSuperfice(con, dtoNumSup, pieza.getSuperficie().intValue());
					UtilidadBD.closeConnection(con);
					
					String uniqueStr=	pieza.getSeccion()
					+"_"+pieza.getPieza().intValue()
					+"_"+pieza.getHallazgo().intValue()
					+"_"+dtoProcesoCitaProgramada.getCodigoPresupuesto()
					+"_"+progServ.getPrograma().getCodigo();
					
					if(superficiesAplica.size()>0)
					{
						uniqueStr+= "_"+superficiesAplica.get(0).getColor();
					}
					
					if(!valoresUnicos.contains(uniqueStr))
					{
						valoresUnicos.add(uniqueStr);
						DtoDetalleProximaCita dtoDetalleProximaCita= new DtoDetalleProximaCita();
						dtoDetalleProximaCita.setPiezaDental(new InfoDatosInt(pieza.getPieza().intValue(), UtilidadOdontologia.obtenerNombrePieza(pieza.getPieza().intValue()) ));
						
						if(superficiesAplica.size()>0)
						{
							for(InfoNumSuperficiesPresupuesto info: superficiesAplica)
							{
								dtoDetalleProximaCita.getSuperficies().add( new InfoDatosDouble(Utilidades.convertirADouble(info.getCodigoSuperficie()+""), UtilidadOdontologia.obtenerNombreSuperficie(new BigDecimal(info.getCodigoSuperficie()))));
							}
						
						}else{
							
							dtoDetalleProximaCita.getSuperficies().add(new InfoDatosDouble(pieza.getSuperficie().doubleValue(), UtilidadOdontologia.obtenerNombreSuperficie(pieza.getSuperficie())));
						}
						
						if(forma.isUtilizaInstitucionProgramasOdontologicos())
						{

							Double codigoPrograma = progServ.getPrograma().getCodigo();
							int piezaDental = pieza.getPieza().intValue();
							int superficie = pieza.getSuperficie().intValue();
							int hallazgo = pieza.getHallazgo().intValue();
							
							dtoDetalleProximaCita.setPrograma(progServ.getPrograma());
							dtoDetalleProximaCita.setPieza(pieza);
							
							ArrayList<InfoServicios> servicios = PlanTratamiento.cargarServiciosDeProgramasPlanT(dtoProcesoCitaProgramada.getCodigoPlanTratamiento(), codigoPrograma, 
											piezaDental, superficie, hallazgo , usuario.getCodigoInstitucionInt());

							if(servicios.size()<=0){
								
								String codigoTarifarioServicios = ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(usuario.getCodigoInstitucionInt());
								dtoDetalleProximaCita.setServicios(cargarServiciosPorProgramaOdontologico (codigoPrograma, codigoTarifarioServicios));

							}else{
								
								dtoDetalleProximaCita.setServicios(servicios);
							}

							for(InfoServicios servicio: dtoDetalleProximaCita.getServicios())
							{
								servicio.setContrato(contrato);
								
								if(servicio.getDuracionCita()<=0){
									
									servicio.setDuracionCita(Utilidades.convertirAEntero(forma.getMultiploMinutosGeneracionCita()));
								}
							}
						
						}else{
							
							/*
							 * Falta desarrollar el caso cuando la Institución no maneja 
							 * Programas Odontológicos.
							 */
						}
						forma.getDtoProximaCita().getListaDetalle().add(dtoDetalleProximaCita);
					}
				}
			}
		}
		
		if(forma.getDtoProximaCita().getListaDetalle()!=null && forma.getDtoProximaCita().getListaDetalle().size()>0){
			
			forma.setSizeProgramasServicios(forma.getDtoProximaCita().getListaDetalle().size());
		}
	}
	
	
	/**
	 * 
	 * Método que carga los programas / servicios para programar la próxima cita desde la 
	 * contratación del presupuesto.
	 * 
	 * @param codigoPrograma
	 * @param codigoTarifarioServicios 
	 * @return
	 */
	private ArrayList<InfoServicios> cargarServiciosPorProgramaOdontologico(Double codigoPrograma, String codigoTarifarioServicios) {

		ArrayList<DtoDetalleProgramas> detalleProgramas = ProgramasOdontologicos.cargarDetallePrograma(codigoPrograma, codigoTarifarioServicios);
		
		ArrayList<InfoServicios> servicios = new ArrayList<InfoServicios>();
		
		for (DtoDetalleProgramas dtoDetalleProgramas : detalleProgramas) {
			
			InfoServicios servicio = new InfoServicios();
			
			servicio.setCodigoPkProgServ(new BigDecimal(dtoDetalleProgramas.getServicio()));
			servicio.setOrderServicio(dtoDetalleProgramas.getOrden());
			servicio.setServicio(new InfoDatosInt(dtoDetalleProgramas.getServicio(), dtoDetalleProgramas.getDescripcionServicio()));
			
			servicios.add(servicio);
		}
		
		return servicios;
	}


	/**
	 * 
	 * Método que carga y ajusta la información de los Programas / Servicios 
	 * que se pueden programar en para la próxima cita cuando se realiza una atención.
	 *
	 * @param forma
	 * @param usuario 
	 */
	private void cargarProgramasServiciosAtencionCita(ProximaCitaProgramadaForm forma, UsuarioBasico usuario) {
		
		ArrayList<Integer> programasEvaluados=new ArrayList<Integer>();
	
		ArrayList<InfoServicios> listadoServicios = obtenerServiciosAtencionCita(forma, usuario.getCodigoInstitucionInt());
		
		ArrayList<Integer> codigosPrograma = obtenerCodigosPrograma(listadoServicios);
		
		HashMap<String, String> serviciosAgregados = new HashMap<String, String>();
		
		for(Integer codigoPrograma: codigosPrograma)
		{
			String programa = null;
			ArrayList<InfoServicios> servicios = new ArrayList<InfoServicios>();
			ArrayList<String> superficies = new ArrayList<String>();
			
			serviciosAgregados.clear();
			
			if(!programasEvaluados.contains(codigoPrograma))
			{
				DtoDetalleProximaCita dtoDetalleProximaCita= new DtoDetalleProximaCita();
				
				for(InfoServicios servicioInterno: listadoServicios)
				{
					if(!(serviciosAgregados.containsKey(servicioInterno.getCodigoMostrar()) && 
					   (serviciosAgregados.get(servicioInterno.getCodigoMostrar()).equals(servicioInterno.getServicio().getNombre())))){
						
						/*
						 * Si están asociados al mismo programa se evalúan
						 */
						if(codigoPrograma==servicioInterno.getProgramaAsociado().getProgHallazgoPieza().getCodigoPk())
						{ 
							InfoDatosDouble programaInfo  = new InfoDatosDouble();
							
							if(programa==null){
								
								programaInfo  = new InfoDatosDouble(new Double(servicioInterno.getProgramaAsociado().getProgHallazgoPieza().getCodigoPk()), servicioInterno.getProgramaAsociado().getNombreProgramaServicio());
								dtoDetalleProximaCita.setPrograma(programaInfo);
								
								programa = programaInfo.getNombre();
								
								InfoDetallePlanTramiento infoDetallePlan=servicioInterno.getProgramaAsociado().getHallazgoAsociado().getPiezaAsociada();
								
								if(infoDetallePlan!=null){
									
									InfoDatosInt pieza = infoDetallePlan.getPieza();
									
									if(pieza!=null){
										
										dtoDetalleProximaCita.setPiezaDental(pieza);
									}
								}
							}
							
							/*
							 * Se asigna por defecto una duración minima igual al multiplo 
							 * en minutos para la generación de citas en caso que la duración
							 * sea menor a cero.
							 */
							if(servicioInterno.getDuracionCita()<=0){
								
								servicioInterno.setDuracionCita(Utilidades.convertirAEntero(forma.getMultiploMinutosGeneracionCita()));
							}
							
							String superficie=servicioInterno.getProgramaAsociado().getHallazgoAsociado().getSuperficieOPCIONAL().getNombre();
							
							InfoDatosInt servicioInfo = servicioInterno.getProgramaAsociado().getHallazgoAsociado().getSuperficieOPCIONAL();
							
							if(!superficies.contains(superficie))
							{
								if(!superficie.equals(""))
								{
									superficies.add(superficie);
									dtoDetalleProximaCita.getSuperficies().add(new InfoDatosDouble(new Double(servicioInfo.getCodigo()), servicioInfo.getNombre()));
								}
							}
							
							serviciosAgregados.put(servicioInterno.getCodigoMostrar(), servicioInterno.getServicio().getNombre());
							servicios.add(servicioInterno);
						}
					}
				}
				
				dtoDetalleProximaCita.setServicios(servicios);
				forma.getDtoProximaCita().getListaDetalle().add(dtoDetalleProximaCita);
				/*
				 * Se marca el programa como evaluado para que no se muestre más
				 */
				programasEvaluados.add(codigoPrograma);
			}
		}

		if(forma.getDtoProximaCita().getListaDetalle()!=null && forma.getDtoProximaCita().getListaDetalle().size()>0){
			
			forma.setSizeProgramasServicios(forma.getDtoProximaCita().getListaDetalle().size());
		}
	}

	/**
	 * Método que se encarga de obtener los códigos de los programas que se van a evaluar.
	 * 
	 * @param listadoServicios
	 * @return
	 */
	private ArrayList<Integer> obtenerCodigosPrograma (ArrayList<InfoServicios> listadoServicios){
		
		ArrayList<Integer> codigosPrograma=new ArrayList<Integer>();
		int codigo;
		
		for(InfoServicios servicio: listadoServicios)
		{
			codigo = servicio.getProgramaAsociado().getProgHallazgoPieza().getCodigoPk();
			
			if(!codigosPrograma.contains(codigo)){
				
				codigosPrograma.add(codigo);
			}
		}
		
		return codigosPrograma;
	}
	
	/**
	 * Método que se encarga de registrar la próxima cita 
	 * programada en el sistema.
	 * 
	 * @param mapping
	 * @param forma
	 * @param paciente
	 * @param usuario
	 * @param request 
	 * @param request
	 * @return
	 */
	private String guardarProximaCita(ProximaCitaProgramadaForm forma, UsuarioBasico usuario, HttpServletRequest request)
	{
		Connection con= UtilidadBD.abrirConexion();
		
		String fecha = UtilidadFecha.conversionFormatoFechaABD(forma.getFechaProximaCita());
		
		int codigoEvolucion = ConstantesBD.codigoNuncaValido;
		
		if(ConstantesBD.codigoFuncionalidadAtencionCitaOdontologica == forma.getCodigoFuncionalidad()){
			
			codigoEvolucion = forma.getDtoProcesoCitaProgramada().getCodigoEvolucion();
		}
		
		int codigoCitaOdontologica = CitaOdontologica.insertarProximaCitaOdontologia
		(con, forma.getDtoProximaCita().getTodosServicios(), fecha, forma.getDtoProcesoCitaProgramada().getCodigoPersona(), 
				usuario.getLoginUsuario(), codigoEvolucion, usuario.getCodigoCentroCosto(), forma.getTotalMinutosDuracion());
				
		forma.getDtoProximaCita().setGuardoProximaCita(codigoCitaOdontologica > ConstantesBD.codigoNuncaValido ? true : false);
	
		if(!forma.getDtoProximaCita().getGuardoProximaCita())
		{
			forma.setEstado("noExitoso");
			UtilidadBD.abortarTransaccion(con);
		
		}else{
			
			request.getSession().setAttribute("codigoProximaCitaRegistrada", codigoCitaOdontologica);
			request.getSession().setAttribute("DetallesAsociadosCita", forma.getDtoProximaCita().getDetallesAsociadosCita());
			
			UtilidadBD.closeConnection(con);
			forma.setEstado("procesoExitoso");
			forma.setResumen(true);
			
			return direccionarFuncionalidadOrigen(forma);
		}

		return "popUpProximaCitaProgramada";
	}
	
	
	/**
	 * Método que se encarga de retornar a la funcionalidad que inicio el
	 * proceso centralizado de próxima cita.
	 * 
	 * @param forma
	 * @return
	 */
	private String direccionarFuncionalidadOrigen(ProximaCitaProgramadaForm forma) {
		
		String direccion = null;
		
		if(ConstantesBD.codigoFuncionalidadContratarPresupuesto == forma.getCodigoFuncionalidad()){
			
			direccion = "contratarPresupuesto";
		
		}else if(ConstantesBD.codigoFuncionalidadAtencionCitaOdontologica == forma.getCodigoFuncionalidad()){
			
			direccion =  "atencionCita";
		}
		
		return direccion;
	}


	/**
	 * Método que se encarga de totalizar los minutos por cada uno de los servicios asociados
	 * a la próxima cita programada.
	 * 
	 * @param forma
	 */
	private void totalizarDuracionCita (ProximaCitaProgramadaForm forma){
		
		int totalMinutosDuracion= 0;
		
		for (DtoDetalleProximaCita detalleProximaCita :forma.getDtoProximaCita().getListaDetalle()){
				
			for(InfoServicios servicio: detalleProximaCita.getServicios())
			{
				if(servicio.getServicio().isActivo()){
					
					if(servicio.getDuracionCita()>0){
						
						totalMinutosDuracion += servicio.getDuracionCita();
					}
				}
			}
		}
		
		forma.setTotalMinutosDuracion(totalMinutosDuracion);
	}
	
	/**
	 * 
	 * Método encargado de listar los servicios habilitados para ser seleccionados
	 * en el proceso de próxima cita
	 *
	 * @param forma
	 * @param codigoInstitucion
	 * @return
	 */
	private ArrayList<InfoServicios> obtenerServiciosAtencionCita (ProximaCitaProgramadaForm forma, int codigoInstitucion) 
	{
		ArrayList<InfoServicios> servicios=new ArrayList<InfoServicios>();
		//info.resetServiciosProxCita();
 
		
		//****************** SECCION HALLAZGOS ***************************************************************
		
		InfoPlanTratamiento infoPlanTratamiento =  forma.getDtoProcesoCitaProgramada().getInfoPlanTratamiento();
		
		if(infoPlanTratamiento.getSeccionHallazgosDetalle()!=null && infoPlanTratamiento.getSeccionHallazgosDetalle().size()>0){
			
			for(InfoDetallePlanTramiento pieza: infoPlanTratamiento.getSeccionHallazgosDetalle())
		    {
				servicios.addAll(evaluarServiciosSeccion(pieza.getDetalleSuperficie(), pieza, codigoInstitucion));
		    }
		}
		
	
		//************* SECCION OTROS HALLAZGOS ******************************************************************
		
		if(infoPlanTratamiento.getSeccionOtrosHallazgos()!=null && infoPlanTratamiento.getSeccionOtrosHallazgos().size()>0){
			
			for(InfoDetallePlanTramiento pieza: infoPlanTratamiento.getSeccionOtrosHallazgos())
		    {
				servicios.addAll(evaluarServiciosSeccion(pieza.getDetalleSuperficie(), pieza, codigoInstitucion));
		    }
		}
		
		
		//************* SECCION BOCA ******************************************************************
		 
		if(infoPlanTratamiento.getSeccionHallazgosBoca()!=null && infoPlanTratamiento.getSeccionHallazgosBoca().size()>0){
			
			/*
			 * Para la sección boca no aplica la pieza.
			 */
			servicios.addAll(evaluarServiciosSeccion(infoPlanTratamiento.getSeccionHallazgosBoca(), null,  codigoInstitucion));
		}
	   
		return servicios;
	}


	
	/**
	 * Método que recorre y se encarga de evaluar los programas / servicios
	 * de cada una de las secciones para determinar si se pueden asociar a la 
	 * cita programada.
	 * 
	 * @param seccionHallazgo
	 * @param pieza 
	 * @param servicios
	 * @param codigoInstitucion
	 * @return 
	 */
	private ArrayList<InfoServicios> evaluarServiciosSeccion(ArrayList<InfoHallazgoSuperficie> seccionHallazgo, InfoDetallePlanTramiento pieza, int codigoInstitucion) {
		
		ArrayList<InfoServicios> servicios=new ArrayList<InfoServicios>();
		
		for(InfoHallazgoSuperficie hallazgo: seccionHallazgo)
    	{
    		for(InfoProgramaServicioPlan programa: hallazgo.getProgramasOservicios())
    		{
    			if(programa.getListaServicios().size()>0)
    			{
					for(InfoServicios servicio: programa.getListaServicios())
					{
						if(ValoresPorDefecto.getValidaPresupuestoOdoContratado(codigoInstitucion).equals(ConstantesBD.acronimoSi))
						{
							if(servicio.getEstadoServicio().equals(ConstantesIntegridadDominio.acronimoContratado)
									 && servicio.getEliminarSeleccionProxCita().equals(ConstantesBD.acronimoNo))
							{
								servicio.setMsjErrorServicioCita(new InfoDatosString());
								servicio.setProgramaAsociado(programa);
								programa.setHallazgoAsociado(hallazgo);
								hallazgo.setPiezaAsociada(pieza);
								servicios.add(servicio);
							}
							
						}else if(ValoresPorDefecto.getValidaPresupuestoOdoContratado(codigoInstitucion).equals(ConstantesBD.acronimoNo))
						{
							if(servicio.getEstadoServicio().equals(ConstantesIntegridadDominio.acronimoEstadoPendiente)
									 && servicio.getEliminarSeleccionProxCita().equals(ConstantesBD.acronimoNo))
							{
								servicio.setMsjErrorServicioCita(new InfoDatosString());
								servicio.setProgramaAsociado(programa);
								programa.setHallazgoAsociado(hallazgo);
								hallazgo.setPiezaAsociada(pieza);
								servicios.add(servicio);
							}								   
						}								
					}
				}
			}
		}
	
		return servicios;
	}
}
