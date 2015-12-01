package com.princetonsa.action.odontologia;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.UtilidadesFacturacion;
import util.odontologia.UtilidadOdontologia;

import com.princetonsa.actionform.odontologia.AperturaCuentaPacienteOdontologicoForm;
import com.princetonsa.dto.administracion.DtoPaciente;
import com.princetonsa.dto.facturacion.DtoConvenio;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoEscala;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoPlantilla;
import com.princetonsa.dto.manejoPaciente.DtoPerfilNed;
import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;
import com.princetonsa.dto.odontologia.DtoPacienteConvenioOdo;
import com.princetonsa.mundo.ObservableBD;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Convenio;
import com.princetonsa.mundo.historiaClinica.Plantillas;
import com.princetonsa.mundo.manejoPaciente.PerfilNED;
import com.princetonsa.mundo.manejoPaciente.ViasIngreso;
import com.princetonsa.mundo.odontologia.AperturaCuentaPacienteOdontologico;
import com.princetonsa.mundo.odontologia.EmisionBonosDesc;
import com.princetonsa.mundo.odontologia.PacienteConvenioOdo;
import com.sies.mundo.UtilidadSiEs;


@SuppressWarnings("deprecation")
public class AperturaCuentaPacienteOdontologicoAction extends Action {
	
	Logger logger=Logger.getLogger(AperturaCuentaPacienteOdontologicoAction.class);
	
	public ActionForward execute(	ActionMapping mapping,
		 	ActionForm form,
		 	HttpServletRequest request,
		 	HttpServletResponse response) throws Exception
		 	
		 	{
		Connection con=null;
		try{
			if(form instanceof AperturaCuentaPacienteOdontologicoForm)
			{
				AperturaCuentaPacienteOdontologicoForm forma = (AperturaCuentaPacienteOdontologicoForm)form;	
				HttpSession session=request.getSession();
				PersonaBasica paciente = Utilidades.getPersonaBasicaSesion(session);
				UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(session);
				con=UtilidadBD.abrirConexion();

				logger.info("\n\nESTADO > "+forma.getEstado());

				if(paciente==null || paciente.getCodigoPersona()==ConstantesBD.codigoNuncaValido)
				{
					request.setAttribute("ocultarEncabezadoErrores", "true");
					request.setAttribute("codigoDescripcionError", "errors.paciente.noCargado");
					return mapping.findForward("paginaError");
				}
				//******************ESTADOS PARA LA CREACIÓN DE LA CUENTA***********************************
				if (forma.getEstado().equals("empezar"))
				{
					if(paciente.getCodigoCuenta()>0)
					{
						request.setAttribute("ocultarEncabezadoErrores", "true");
						request.setAttribute("descripcionError", "El paciente ya tiene un ingreso creado. Por favor verificar");
						request.setAttribute("utilizaParent", true);
						return mapping.findForward("paginaError");
					}

					return accionEmpezar(con, mapping, forma, request, usuario);	
				}

				if (forma.getEstado().equals("consultarContratos"))
				{
					return accionConsultarContratos(con, mapping, forma, request, usuario, paciente, forma.getIndice());
				}

				if (forma.getEstado().equals("buscarDatosEconomicos"))
				{
					return cargarDatosEconomicos(con, mapping, forma, request, usuario, forma.getIndice(),true);
				}

				if (forma.getEstado().equals("guardarCuenta"))
				{
					return guardarCuenta(con, mapping, forma, request, response, usuario, paciente);
				}
				if (forma.getEstado().equals("adicionaConvenio"))
				{
					return adicionarConvenio(con, mapping, forma, request, usuario, paciente);
				}
				if (forma.getEstado().equals("eliminar"))
				{
					return accionEliminar(con, mapping, forma, request, usuario, paciente);
				}

				if(forma.getEstado().equals("cambiarAutorizacion"))
				{
					return accionValidarAuto(con, mapping, forma, request, usuario, paciente);
				}
				if(forma.getEstado().equals("eliminarAdjunto"))
				{
					accionEliminarAdjunto(forma);
					return UtilidadSiEs.hacerMapping(con, "ingresarCuenta", mapping);
				}
				if(forma.getEstado().equals("cargarPerfilNed"))
				{
					return UtilidadSiEs.hacerMapping(con, "ingresarCuenta", mapping);
				}
				//**********************ESTADOS PARA LA MODIFICACION DE LA CUENTA***********************************
				else if(forma.getEstado().equals("empezarModificar"))
				{
					return accionEmpezarModificar(con,mapping,forma,request,usuario,paciente);
				}
				else if(forma.getEstado().equals("modificarCuenta"))
				{
					return accionModificarCuenta(con,mapping,forma,request,usuario,paciente);
				}
				//*******************************************************************************************************
			}
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return null;
	}
	
	/**
	 * Método implementado para realizar la modificación de cuenta
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param request
	 * @param usuario
	 * @param paciente
	 * @return
	 */
	private ActionForward accionModificarCuenta(Connection con,
			ActionMapping mapping,
			AperturaCuentaPacienteOdontologicoForm forma,
			HttpServletRequest request, UsuarioBasico usuario,
			PersonaBasica paciente) 
	{
		ActionErrors errores=new ActionErrors();
		forma.setEstado("empezarModificar");
		validaciones(con, forma, errores, paciente, request, true, usuario);
		
		if(!errores.isEmpty())
		{
			return UtilidadSiEs.hacerMapping(con, "ingresarCuenta", mapping);
		}
		
		UtilidadBD.iniciarTransaccion(con);
		ResultadoBoolean resultado = AperturaCuentaPacienteOdontologico.modificarCuenta(con, forma.getDtoSubCuentas(), usuario, paciente, forma.getCuenta());
		if(!resultado.isTrue())
		{
			UtilidadBD.abortarTransaccion(con);
			request.setAttribute("descripcionError", resultado.getDescripcion());
			return UtilidadSiEs.hacerMapping(con, "paginaError", mapping);
		}
		else
		{
			UtilidadBD.finalizarTransaccion(con);
			
			forma.setDtoSubCuentas(AperturaCuentaPacienteOdontologico.cargarSubcuentas(con, forma.getCuenta()));
			
			for(int i=0;i<forma.getDtoSubCuentas().size();i++)
			{
				forma.getDtoSubCuentas().get(i).setModificacion(UtilidadOdontologia.esConvenioRelacionadoAPresupuestoOdoContratado(con, forma.getDtoSubCuentas().get(i)));
				
				forma.getManejaBonos().add(UtilidadTexto.getBoolean(forma.getDtoSubCuentas().get(i).getDatosConvenio().getRequiereBonoIngresoPaciente()));
				forma.getRequiereIngresoValidacionAuto().add(UtilidadTexto.getBoolean(forma.getDtoSubCuentas().get(i).getDatosConvenio().getRequiereIngresoValidacionAuto()));
				forma.getIngresoPacienteRequiereAutorizacion().add(UtilidadTexto.getBoolean(forma.getDtoSubCuentas().get(i).getDatosConvenio().getIngresoPacienteRequiereAutorizacion()));
				consultarContratosPorConvenio(con, mapping, forma, request, usuario, paciente, i);
				cargarDatosEconomicos(con, mapping, forma, request, usuario, i,false);
			}

//			UtilidadesManejoPaciente.cargarPaciente(con, usuario, paciente, request);
			/*try 
			{
				boolean res=paciente.cargar(con, paciente.getCodigoPersona());
				logger.info("resultado cargar paciente "+res);
				paciente.cargarPaciente2(con, paciente.getCodigoPersona(), usuario.getCodigoInstitucion(), usuario.getCodigoCentroAtencion()+"");
			} 
			catch (Exception e) 
			{
				logger.info("Error cargando el ingreso del paciente: "+e);
			}
			*/
			ObservableBD observable = (ObservableBD) getServlet().getServletContext().getAttribute("observable");
			if (observable != null) 
			{
				paciente.setObservable(observable);
				// Si ya lo habíamos añadido, la siguiente línea no hace nada
				observable.addObserver(paciente);
			}
			//Se sube a sesión el paciente activo
			//request.getSession().setAttribute("pacienteActivo", paciente);
		    if (observable != null) {
				synchronized (observable) {
					observable.setChanged();
					observable.notifyObservers(new Integer(paciente.getCodigoPersona()));
				}
			}
			
		}
		
		return UtilidadSiEs.hacerMapping(con, "resumenIngresoCuenta", mapping);
		
		
	}

	/**
	 * Método usado para iniciar el flujo de modificación de la cuenta
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param request
	 * @param usuario
	 * @param paciente
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private ActionForward accionEmpezarModificar(Connection con,
			ActionMapping mapping,
			AperturaCuentaPacienteOdontologicoForm forma,
			HttpServletRequest request, UsuarioBasico usuario,
			PersonaBasica paciente) 
	{
		forma.reset();
		forma.setListaAreas(Utilidades.getCentrosCostoXViaIngresoXCAtencion(con, ConstantesBD.codigoViaIngresoConsultaExterna, usuario.getCodigoCentroAtencion(), usuario.getCodigoInstitucionInt(), ConstantesBD.tipoPacienteAmbulatorio));
		forma.setFlujoModificacion(true);
		
		String[] listaConstantes=new String[]{ConstantesIntegridadDominio.acronimoInternet, ConstantesIntegridadDominio.acronimoTelefonica, ConstantesIntegridadDominio.acronimoFax, ConstantesIntegridadDominio.acronimoOtro};
		
		forma.setListaMediosAutorizacion(Utilidades.generarListadoConstantesIntegridadDominio(con, listaConstantes, false));

		//Se cargan los convenios odontologicos
		
		forma.setConvenios(AperturaCuentaPacienteOdontologico.consultaConvenios());
		
		for(int k=0; k<forma.getConvenios().size();k++)
		 {
			 if(!PacienteConvenioOdo.pacienteTieneTarjetaCliente(con, paciente, forma.getConvenios().get(k).getCodigo()))
			 {
				 if(forma.getConvenios().get(k).getEsConvenioTarjetaCliente().equals(ConstantesBD.acronimoSi))
				 {
				   logger.info("\n\n Entro A INACTIVAR CONVENIO >>"+forma.getConvenios().get(k).getDescripcion());	 
				   forma.getConvenios().get(k).setHabilitarSelect(ConstantesBD.acronimoNo);			   
				 }
			 }
			 
		 }
		
			
		DtoPaciente dtoPaciente=new DtoPaciente();
		dtoPaciente.setCodigo(paciente.getCodigoPersona());
		dtoPaciente.setTipoId(paciente.getCodigoTipoIdentificacionPersona());
		dtoPaciente.setNumeroId(paciente.getNumeroIdentificacionPersona());
			
		
		ViasIngreso vi=new ViasIngreso();
		forma.setNombreViaIngreso((String)vi.consultarViasIngresoEspecifico(con, ConstantesBD.codigoViaIngresoConsultaExterna).get("nombre_0"));
		
		forma.setCuenta(AperturaCuentaPacienteOdontologico.cargarCuentaIngresoAbiertoPaciente(con, dtoPaciente));
		
		forma.setDtoSubCuentas(AperturaCuentaPacienteOdontologico.cargarSubcuentas(con, forma.getCuenta()));
		
		for(int i=0;i<forma.getDtoSubCuentas().size();i++)
		{
			forma.getDtoSubCuentas().get(i).setModificacion(UtilidadOdontologia.esConvenioRelacionadoAPresupuestoOdoContratado(con, forma.getDtoSubCuentas().get(i)));
			
			forma.getManejaBonos().add(UtilidadTexto.getBoolean(forma.getDtoSubCuentas().get(i).getDatosConvenio().getRequiereBonoIngresoPaciente()));
			forma.getRequiereIngresoValidacionAuto().add(UtilidadTexto.getBoolean(forma.getDtoSubCuentas().get(i).getDatosConvenio().getRequiereIngresoValidacionAuto()));
			forma.getIngresoPacienteRequiereAutorizacion().add(UtilidadTexto.getBoolean(forma.getDtoSubCuentas().get(i).getDatosConvenio().getIngresoPacienteRequiereAutorizacion()));
			consultarContratosPorConvenio(con, mapping, forma, request, usuario, paciente, i);
			cargarDatosEconomicos(con, mapping, forma, request, usuario, i,false);
		}
					
		if(forma.getDtoSubCuentas().size()==0)
		{
			forma.adicionarConvenioVacio();
		}
		
		forma.setArea(forma.getCuenta().getCodigoArea());
		
		//Se realizan las validaciones para buscar los convenios odontologicos a postular
		return UtilidadSiEs.hacerMapping(con, "ingresarCuenta", mapping);
	}

	private void accionEliminarAdjunto(AperturaCuentaPacienteOdontologicoForm forma) {
		int indice=forma.getIndice();
		int numeroAdjunto=forma.getNumeroDocumento();
		forma.getDtoSubCuentas().get(indice).getDocumentosAdjuntos().remove(numeroAdjunto);
	}

	private ActionForward accionValidarAuto(Connection con, ActionMapping mapping, AperturaCuentaPacienteOdontologicoForm forma, HttpServletRequest request, UsuarioBasico usuario, PersonaBasica paciente) {
		ActionErrors errores=new ActionErrors();
		validaciones(con, forma, errores, paciente, request, false, usuario);
		return UtilidadSiEs.hacerMapping(con, "ingresarCuenta", mapping);
	}

	private ActionForward accionEliminar(Connection con, ActionMapping mapping, AperturaCuentaPacienteOdontologicoForm forma, HttpServletRequest request, UsuarioBasico usuario, PersonaBasica paciente)
	{
		int indiceEliminar=forma.getIndice();
		if(!forma.isFlujoModificacion())
		{
			forma.getDtoSubCuentas().remove(indiceEliminar);
			forma.getContratos().remove(indiceEliminar);
			forma.getManejaBonos().remove(indiceEliminar);
			forma.getRequiereIngresoValidacionAuto().remove(indiceEliminar);
			forma.getIngresoPacienteRequiereAutorizacion().remove(indiceEliminar);
		}
		else
		{
			forma.getDtoSubCuentas().get(indiceEliminar).setEliminar(true);
		}
		ActionErrors errores=new ActionErrors();
		validaciones(con, forma, errores, paciente, request, false, usuario);
		return UtilidadSiEs.hacerMapping(con, "ingresarCuenta", mapping);
	}

	/**
	 * Método para adicionar un convenio al ArrayList dtoSubCuentas
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param request
	 * @param usuario
	 * @param paciente
	 * @return
	 */
	private ActionForward adicionarConvenio(Connection con, ActionMapping mapping, AperturaCuentaPacienteOdontologicoForm forma, HttpServletRequest request, UsuarioBasico usuario, PersonaBasica paciente)
	{
		forma.getDtoSubCuentas().add(new DtoSubCuentas());
		forma.getContratos().add(new ArrayList<HashMap<String,Object>>());
		forma.getManejaBonos().add(false);
		forma.getRequiereIngresoValidacionAuto().add(false);
		forma.getIngresoPacienteRequiereAutorizacion().add(false);
		ActionErrors errores=new ActionErrors();
		validaciones(con, forma, errores, paciente, request, false, usuario);
		return UtilidadSiEs.hacerMapping(con, "ingresarCuenta", mapping);
	}

	/**
	 * Método para guardar la creación de la cuenta
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param request
	 * @param response 
	 * @param usuario
	 * @param paciente
	 * @return Página de resumen
	 */
	private ActionForward guardarCuenta(Connection con, ActionMapping mapping, AperturaCuentaPacienteOdontologicoForm forma, HttpServletRequest request, HttpServletResponse response, UsuarioBasico usuario, PersonaBasica paciente) {
		
		ActionErrors errores=new ActionErrors();
		
		
		
		validaciones(con, forma, errores, paciente, request, true, usuario);
		
		if(!errores.isEmpty())
		{
			return UtilidadSiEs.hacerMapping(con, "ingresarCuenta", mapping);
		}
		
		UtilidadBD.iniciarTransaccion(con);
		boolean resultado=AperturaCuentaPacienteOdontologico.guardarCuentas(con, forma.getDtoSubCuentas(), usuario, paciente, forma.getArea());
		if(!resultado)
		{
			UtilidadBD.abortarTransaccion(con);
			request.setAttribute("codigoDescripcionError", "errors.problemasBd");
			return UtilidadSiEs.hacerMapping(con, "paginaError", mapping);
		}
		else
		{
			UtilidadBD.finalizarTransaccion(con);
			
//			UtilidadesManejoPaciente.cargarPaciente(con, usuario, paciente, request);
			/*try 
			{
				boolean res=paciente.cargar(con, paciente.getCodigoPersona());
				logger.info("resultado cargar paciente "+res);
				paciente.cargarPaciente2(con, paciente.getCodigoPersona(), usuario.getCodigoInstitucion(), usuario.getCodigoCentroAtencion()+"");
			} 
			catch (Exception e) 
			{
				logger.info("Error cargando el ingreso del paciente: "+e);
			}
			*/
			ObservableBD observable = (ObservableBD) getServlet().getServletContext().getAttribute("observable");
			if (observable != null) 
			{
				paciente.setObservable(observable);
				// Si ya lo habíamos añadido, la siguiente línea no hace nada
				observable.addObserver(paciente);
			}
			//Se sube a sesión el paciente activo
			//request.getSession().setAttribute("pacienteActivo", paciente);
		    if (observable != null) {
				synchronized (observable) {
					observable.setChanged();
					observable.notifyObservers(new Integer(paciente.getCodigoPersona()));
				}
			}
		}
		
		try {
			response.sendRedirect(forma.getUrlRetorno());
		} catch (IOException e) {
			logger.info("Debe especificar el parametro urlRetorno", e);
		}
		return null;
	}

	/**
	 * Validaciones para guardar
	 * @param con Conexión con la BD
	 * @param forma Formulario
	 * @param errores Coleccion de errores
	 * @param paciente 
	 * @param request
	 * @param guardar
	 * @param usuario 
	 */
	private void validaciones(Connection con, AperturaCuentaPacienteOdontologicoForm forma, ActionErrors errores, PersonaBasica paciente, HttpServletRequest request, boolean guardar, UsuarioBasico usuario) {

		if(guardar)
		{
			if(forma.getArea()<=0)
			{
				errores.add("requerido area", new ActionMessage("errors.required", "Área"));
			}
		}
		
		/*"error.facturacion.conveniosDuplicados"*/
		int numeroConvenios=forma.getNumeroConvenios();
		if(numeroConvenios==0)
		{
			errores.add("no paciente est tablas", new ActionMessage("error.odontologia.pacienteNoEstaEnBD"));
		}
		ArrayList<Integer> contratos=new ArrayList<Integer>();
		for(int i=0; i<numeroConvenios; i++)
		{
			DtoSubCuentas dtoSubCuentas=forma.getDtoSubCuentas().get(i);
			if(!dtoSubCuentas.getEliminar())
			{
				int codigoConvenio=dtoSubCuentas.getConvenio().getCodigo();
				String convenio=dtoSubCuentas.getConvenio().getDescripcion();// Mientras traigo el nombre
				
				if (codigoConvenio<=0)
					convenio="";
				
				Integer codigoContrato=new Integer(dtoSubCuentas.getContrato());
				if(guardar)
				{
					if(codigoContrato<=0)
					{
						errores.add("requerido contrato", new ActionMessage("errors.required", "El Contrato ["+(i+1)+"]"));
					}
					if(codigoConvenio<=0)
					{
						errores.add("requerido contrato", new ActionMessage("errors.required", "El Convenio ["+(i+1)+"]"));
					}
					else
					{
						if(contratos.contains(codigoContrato))
						{
							errores.add("requerido contrato", new ActionMessage("error.facturacion.conveniosDuplicados"));
						}
						else
						{
							contratos.add(codigoContrato);
						}
					}
				}
				
				ArrayList<DtoConvenio> iteradorLista=forma.getConvenios();
				boolean ingresarPacienteValidacionBD=false;
				boolean manejaBonos=false;
				boolean requiereIngresoValidacionAuto=false;
				boolean esConvenioTarjetaCliente=false;
				boolean requiereBonoIngresoPaciente=false;
				boolean ingresoPacienteRequiereAutorizacion=false;
			
				
				for(DtoConvenio conv:iteradorLista)
				{
					if(conv.getCodigo()==codigoConvenio)
					{
						ingresarPacienteValidacionBD=UtilidadTexto.getBoolean(conv.getIngresarPacienteValidacionBD());
						manejaBonos=UtilidadTexto.getBoolean(conv.getManejaBonos());
						requiereBonoIngresoPaciente=UtilidadTexto.getBoolean(conv.getRequiereBonoIngresoPaciente());
						requiereIngresoValidacionAuto=UtilidadTexto.getBoolean(conv.getRequiereIngresoValidacionAuto());
						ingresoPacienteRequiereAutorizacion=UtilidadTexto.getBoolean(conv.getIngresoPacienteRequiereAutorizacion());
						esConvenioTarjetaCliente=UtilidadTexto.getBoolean(conv.getEsConvenioTarjetaCliente());
	    			/*	logger.info("ingresarPacienteValidacionBD conv "+codigoConvenio+" "+ingresarPacienteValidacionBD);
		                logger.info("requiereBonoIngresoPaciente conv "+codigoConvenio+" "+requiereBonoIngresoPaciente);
						logger.info("requiereIngresoValidacionAuto conv "+codigoConvenio+" "+requiereIngresoValidacionAuto);
						*/
						break;
					}
				}
				
				logger.info("Validar Ingresar Paciente BD "+ingresarPacienteValidacionBD);
				if(ingresarPacienteValidacionBD)
				{
					DtoPacienteConvenioOdo pacienteConvenio=pacienteEnestructuraTablas(con, paciente,dtoSubCuentas.getConvenio().getCodigo());
					
					if(pacienteConvenio==null || pacienteConvenio.getCodigo()<=0)
					{
						errores.add("no paciente esta tablas", new ActionMessage("error.odontologia.pacienteNoEstaEnBD",dtoSubCuentas.getConvenio().getDescripcion()));						
						
					}else
					{
					 //***** Validacion Vigencia Mayo 5 2010 *********				
					
					   if( (UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.getFechaActual(),UtilidadFecha.conversionFormatoFechaAAp(pacienteConvenio.getFechaIniVigencia())) ) 
							|| (UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.conversionFormatoFechaAAp(pacienteConvenio.getFechaFinVigencia()) , UtilidadFecha.getFechaActual()) ) )
					     {						
						  errores.add("no vigencia convenio", new ActionMessage("error.odontologia.vigenciaConvenio",dtoSubCuentas.getConvenio().getDescripcion()));
					     }
					} 
				    //*************Fin Validacion Vigencia *******
					
					
				}
				if(esConvenioTarjetaCliente)
				{
					logger.info("PacienteConvenioOdo.pacienteTieneTarjetaCliente, codigo convenio:"+codigoConvenio);
					if(!PacienteConvenioOdo.pacienteTieneTarjetaCliente(con, paciente, codigoConvenio))
					{
						errores.add("convenio tarjeta cliente", new ActionMessage("error.odontologia.convenioTarjetaCliente", "["+convenio+"]"));
					}
				}
				if(ingresoPacienteRequiereAutorizacion && guardar)
				{
					if(dtoSubCuentas.getMedioAutorizacion().trim().equals(""))
					{
						errores.add("medio autorizacion requerido", new ActionMessage("errors.required", "El medio de autorización ["+convenio+"]"));
					}
					if(requiereIngresoValidacionAuto)
					{
						if(dtoSubCuentas.getValorAutorizacion()<=0)
						{
							errores.add("autorizacion requerido", new ActionMessage("errors.required", "El valor de autorización ["+convenio+"]"));
						}
					}
				}
				if(manejaBonos && guardar)
				{
					if(requiereBonoIngresoPaciente && (dtoSubCuentas.getBono()==null||dtoSubCuentas.getBono().intValue()<=0))
					{
						errores.add("bono requerido", new ActionMessage("errors.required", "El bono ["+convenio+"]"));
					}
					else
					{
						if(dtoSubCuentas.getBono()!=null && dtoSubCuentas.getBono().intValue()>0)
						{
							String error=EmisionBonosDesc.esSerialVigenteDisponibleMensajeError(con, dtoSubCuentas.getBono().intValue(), codigoConvenio, dtoSubCuentas);
							if(!error.equals(""))
							{
								errores.add("serial no vigente", new ActionMessage(error, "["+convenio+"]"));
							}
						}
					}
				}
				if(UtilidadTexto.getBoolean(UtilidadesFacturacion.esControlAnticipos(con, codigoContrato)))
				{
					validarValorAutorizado(con, dtoSubCuentas, request, errores);
	
				}
			}
		}
		
		//****** Validar la plantilla, si contiene escalas requeridas verificar que se halla registrado información
		if(guardar){
			int codigoPlantilla = Plantillas.obtenerCodigoPlantillaXPacienteOdont(con, paciente.getCodigoPersona());
			DtoPlantilla plantilla=Plantillas.cargarPlantillaIngresoPacienteOdontologico(con, usuario.getCodigoInstitucionInt(), usuario, paciente.getCodigoPersona(),ConstantesIntegridadDominio.acronimoTipoFuncionalidadAsignar,codigoPlantilla);
			logger.info("codigoPlantilla = "+plantilla.getCodigoPK());
			if(Utilidades.convertirAEntero(plantilla.getCodigoPK())>0)
			{
				ArrayList<DtoEscala> escalasRequeridas = Plantillas.obtenerEscalasRequeridas(con, Utilidades.convertirAEntero(plantilla.getCodigoPK()));
				if(escalasRequeridas.size()>0){
					for(int i=0; i<escalasRequeridas.size(); i++){
						if(!Plantillas.existeInfoEscala(con, plantilla.getCodigoPK(), paciente.getCodigoPersona()+"", escalasRequeridas.get(i).getCodigoPK()))
						{	
							//logger.info("CODIGO ESCALA >> pk>>"+escalasRequeridas.get(i).getCodigoPK()+"  codigo>>" +escalasRequeridas.get(i).getCodigo() +" Consecutivo >>"+escalasRequeridas.get(i).getConsecutivoParametrizacion()+"   >>VporDef>> "+ValoresPorDefecto.getEscalaPacientePerfil(usuario.getCodigoInstitucionInt()));
							if(escalasRequeridas.get(i).getCodigoPK().equals(ValoresPorDefecto.getEscalaPacientePerfil(usuario.getCodigoInstitucionInt())))
							{
								BigDecimal codigoEscala;
								DtoPerfilNed dtoPerfilNed=PerfilNED.cargarPerfilNEDXPaciente(paciente.getCodigoPersona(), usuario.getCodigoInstitucionInt());

								if(dtoPerfilNed.getCodigoPk()>0)
								{
									//si existe debemos cargar el codigo de la escala perteneciente al paciente
									codigoEscala= new BigDecimal(dtoPerfilNed.getEscala());
									plantilla.setEscalaPerfilNed(Plantillas.cargarEscalaPerfilNed(codigoEscala, dtoPerfilNed.getCodigoPk()), usuario.getCodigoInstitucionInt());
								}
								else
								{
									if( Utilidades.convertirADouble(ValoresPorDefecto.getEscalaPacientePerfil(usuario.getCodigoInstitucionInt()))>0)
									{
										codigoEscala= new BigDecimal(Utilidades.convertirADouble(ValoresPorDefecto.getEscalaPacientePerfil(usuario.getCodigoInstitucionInt()))); 
										plantilla.setEscalaPerfilNed(Plantillas.cargarEscalaPerfilNed(codigoEscala, ConstantesBD.codigoNuncaValidoDoubleNegativo), usuario.getCodigoInstitucionInt());
									}	
								}
								
								//logger.info("ESCALA perfil Ned ");
								if(!plantilla.tieneInformacionPerfilNed(usuario.getCodigoInstitucionInt()))
								{			
									//logger.info("\nNO TIENE INFORMACION Perfil NED");
									errores.add("Ingresar Escala", new ActionMessage("errors.required", "Ingresar Escala "+escalasRequeridas.get(i).getNombre()));
								}
							}else
							{
								//logger.info("\nNO ES ESCALA Perfil NED");
								errores.add("Ingresar Escala", new ActionMessage("errors.required", "Ingresar Escala "+escalasRequeridas.get(i).getNombre()));
							}
						}
					}
				}
			}
		}
		saveErrors(request, errores);
		
	}


	private void validarValorAutorizado(Connection con, DtoSubCuentas dtoSubCuentas, HttpServletRequest request, ActionErrors errores)
	{
		int codigoContrato=dtoSubCuentas.getContrato();
		double valorAnticipoDisponible=UtilidadesFacturacion.consultarValorAnticipoDisponible(con, codigoContrato);
		double valorAuto=0;
		Double valorAutoObj=dtoSubCuentas.getValorAutorizacion();
		if(valorAutoObj!=null && valorAutoObj>0)
		{
			valorAuto=valorAutoObj;
		}
		if(valorAuto>0)
		{
			if(valorAnticipoDisponible<valorAuto)
			{
				/*el valor autorizado debe ser menor o igual al valor Anticipo Disponible*/
				errores.add("Error validando anticipos", new ActionMessage("errores.valorAnticipoMenorAbonoDisponible"));
			}
		}
		saveErrors(request, errores);
	}

	private ActionForward cargarDatosEconomicos(Connection con, ActionMapping mapping, AperturaCuentaPacienteOdontologicoForm forma,HttpServletRequest request,UsuarioBasico usuario, int index, boolean hacerForward)
	{
	
		
		cargarPorcentaje(con, forma, index, forma.getDtoSubCuentas().get(index).getContrato());
		
		if(hacerForward)
		{
			return UtilidadSiEs.hacerMapping(con, "ingresarCuenta", mapping);
		}
		else
		{
			return null;
		}
		
	}

	
	
	private void cargarPorcentaje(Connection con,
			AperturaCuentaPacienteOdontologicoForm forma, int index,
			int contrato) {
		
	// Este metodo se deja vacio. No aplica fue eliminado	
	}
	

	/**
	 * Cargar los contratos del ingreso anterior
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param request
	 * @param usuario
	 * @param paciente
	 * @param index
	 */
	@SuppressWarnings("unchecked")
	private void consultarContratosPorConvenio(Connection con, ActionMapping mapping, AperturaCuentaPacienteOdontologicoForm forma, HttpServletRequest request, UsuarioBasico usuario, PersonaBasica paciente, int index)
	{
		
		int codigoConvenio=forma.getDtoSubCuentas().get(index).getConvenio().getCodigo();
		ArrayList<HashMap<String, Object>> contratos=Utilidades.obtenerContratos(con, codigoConvenio, false, true);
		
		if(contratos.size()==0)
		{
			forma.getDtoSubCuentas().get(index).setContrato(ConstantesBD.codigoNuncaValido);
		}
		else
		{
			Utilidades.imprimirMapa(contratos.get(0));
			forma.getDtoSubCuentas().get(index).setContrato(Utilidades.convertirAEntero(contratos.get(0).get("codigo")+""));
		}
		
		ArrayList<DtoConvenio> iteradorLista=forma.getConvenios();
		boolean manejaBono=false;
		boolean requiereIngresoValidacionAuto=false;
		boolean ingresoPacienteRequiereAutorizacion=false;
		
		for(DtoConvenio conv:iteradorLista)
		{
			if(conv.getCodigo()==codigoConvenio)
			{
				forma.getDtoSubCuentas().get(index).getConvenio().setDescripcion(conv.getDescripcion());
				manejaBono=UtilidadTexto.getBoolean(conv.getManejaBonos());
				requiereIngresoValidacionAuto=UtilidadTexto.getBoolean(conv.getRequiereIngresoValidacionAuto());
				ingresoPacienteRequiereAutorizacion=UtilidadTexto.getBoolean(conv.getIngresoPacienteRequiereAutorizacion());
				logger.info("3333333333333333333333333 "+ingresoPacienteRequiereAutorizacion);

				if(forma.getManejaBonos().size()<=index || forma.getManejaBonos().get(index)==null)
				{
					forma.getManejaBonos().add(manejaBono);
				}
				else
				{
					forma.getManejaBonos().set(index,manejaBono);
				}
				if(forma.getRequiereIngresoValidacionAuto().size()<=index || forma.getRequiereIngresoValidacionAuto().get(index)==null)
				{
					forma.getRequiereIngresoValidacionAuto().add(requiereIngresoValidacionAuto);	
				}
				else
				{
					forma.getRequiereIngresoValidacionAuto().set(index, requiereIngresoValidacionAuto);	
				}
				if(forma.getIngresoPacienteRequiereAutorizacion().size()<=index || forma.getIngresoPacienteRequiereAutorizacion().get(index)==null)
				{
					forma.getIngresoPacienteRequiereAutorizacion().add(ingresoPacienteRequiereAutorizacion);	
				}
				else
				{
					forma.getIngresoPacienteRequiereAutorizacion().set(index, ingresoPacienteRequiereAutorizacion);	
				}
				break;
			}
		}
		
/*		logger.info("ingresarPacienteValidacionBD "+ingresarPacienteValidacionBD);
		logger.info("manejaBono "+manejaBono);
		logger.info("requiereIngresoValidacionAuto "+requiereIngresoValidacionAuto);
*/
		
		if(forma.getContratos().size()<=index)
		{
			forma.getContratos().add(contratos);
		}
		else
		{
			forma.getContratos().set(index, contratos);
		}
		ActionErrors errores=new ActionErrors();
		validaciones(con, forma, errores, paciente, request, false, usuario);

	}
	
	private ActionForward accionConsultarContratos(Connection con, ActionMapping mapping, AperturaCuentaPacienteOdontologicoForm forma,HttpServletRequest request,UsuarioBasico usuario, PersonaBasica paciente, int index)
	{
		consultarContratosPorConvenio(con, mapping, forma, request, usuario, paciente, index);
		
		
		if((forma.getContratos().size()==1) &&  (forma.getDtoSubCuentas().get(0).getContrato()!=ConstantesBD.codigoNuncaValido) )
		{
			cargarDatosEconomicos(con, mapping, forma, request, usuario, 0,true);
		}
				
		return UtilidadSiEs.hacerMapping(con, "ingresarCuenta", mapping);
	}
	
	/**
	 * Método que verifica que el paciente se encuentre parametrizado en la tabla
	 * pacientes_conv_odo
	 * @param con
	 * @param paciente
	 * @param codigoConvenio 
	 * @return
	 */
	private DtoPacienteConvenioOdo pacienteEnestructuraTablas(Connection con,
			PersonaBasica paciente, int codigoConvenio) {
		DtoPaciente dtoPaciente=new DtoPaciente();
		dtoPaciente.setNumeroId(paciente.getNumeroIdentificacionPersona());
		dtoPaciente.setTipoId(paciente.getCodigoTipoIdentificacionPersona());
		DtoPacienteConvenioOdo pacienteConvenio= new DtoPacienteConvenioOdo();
		pacienteConvenio = PacienteConvenioOdo.consultarConvenio(con, dtoPaciente,codigoConvenio);
		
		return pacienteConvenio;
	}
/*
	private void cargarPorcentaje(Connection con, AperturaCuentaPacienteOdontologicoForm forma, int index, int codigoContrato) {
		MontosCobro mc=MontosCobro.cargarMontoCobroOdontologiaSegunConvenio(con, codigoContrato);
		
		
		// En odontología solamente se mira el porcentaje
		forma.getValorPaciente().put(index+"",UtilidadTexto.formatearValores(mc.getPorcentaje(), "###")+" %");
	}
*/

	/**
	 * Método para empezar la creación de la cuenta odontológica
	 * @param mapping
	 * @param forma
	 * @param request
	 * @param usuario
	 * @return
	 */
	@SuppressWarnings({"unchecked"})
	private ActionForward accionEmpezar(Connection con, ActionMapping mapping, AperturaCuentaPacienteOdontologicoForm forma,HttpServletRequest request,UsuarioBasico usuario) 
	{
		forma.reset();
		
		String[] listaConstantes=new String[]{ConstantesIntegridadDominio.acronimoInternet, ConstantesIntegridadDominio.acronimoTelefonica, ConstantesIntegridadDominio.acronimoFax, ConstantesIntegridadDominio.acronimoOtro};
		
		forma.setListaMediosAutorizacion(Utilidades.generarListadoConstantesIntegridadDominio(con, listaConstantes, false));

        		
		//Se cargan los convenios odontologicos
		forma.setConvenios(AperturaCuentaPacienteOdontologico.consultaConvenios());
		forma.setConveniosAMostrarPresupuestoOdo(Utilidades.obtenerConveniosAMostrarPresupuestoOdo(con,  usuario.getCodigoInstitucionInt()));
		
		PersonaBasica paciente=Utilidades.getPersonaBasicaSesion(request.getSession());
		DtoPaciente dtoPaciente=new DtoPaciente();
		dtoPaciente.setCodigo(paciente.getCodigoPersona());
		dtoPaciente.setTipoId(paciente.getCodigoTipoIdentificacionPersona());
		dtoPaciente.setNumeroId(paciente.getNumeroIdentificacionPersona());
		
		
		for(int k=0; k<forma.getConvenios().size();k++)
		 {
			 if(!PacienteConvenioOdo.pacienteTieneTarjetaCliente(con, paciente, forma.getConvenios().get(k).getCodigo()))
			 {
				 if(forma.getConvenios().get(k).getEsConvenioTarjetaCliente().equals(ConstantesBD.acronimoSi))
				 {
				    logger.info("\n\n Entro A INACTIVAR CONVENIO >>"+forma.getConvenios().get(k).getDescripcion());	 
				    forma.getConvenios().get(k).setHabilitarSelect(ConstantesBD.acronimoNo);		   
				 
				 }
			 }
			 
		 }
		
		ViasIngreso vi=new ViasIngreso();
		forma.setNombreViaIngreso((String)vi.consultarViasIngresoEspecifico(con, ConstantesBD.codigoViaIngresoConsultaExterna).get("nombre_0"));
		
		
		ArrayList<DtoConvenio> listaConvenios=AperturaCuentaPacienteOdontologico.consultaConveniosTarjetaPaciente(con, dtoPaciente);
		
		if(listaConvenios.size()>0)
		{
			for(int i=0; i<listaConvenios.size(); i++)
			{
				DtoConvenio dtoConvenio=listaConvenios.get(i);
				int convenio=dtoConvenio.getCodigo();
				String nombreConvenio=dtoConvenio.getDescripcion();
				DtoSubCuentas dtoCuenta = new DtoSubCuentas();
				dtoCuenta.setConvenio(new InfoDatosInt(convenio, nombreConvenio));

				forma.getManejaBonos().add(UtilidadTexto.getBoolean(dtoConvenio.getRequiereBonoIngresoPaciente()));
				forma.getRequiereIngresoValidacionAuto().add(UtilidadTexto.getBoolean(dtoConvenio.getRequiereIngresoValidacionAuto()));
				forma.getIngresoPacienteRequiereAutorizacion().add(UtilidadTexto.getBoolean(dtoConvenio.getIngresoPacienteRequiereAutorizacion()));

				logger.info("Si entraaaaaaaaaaaaaaaaaaaaaaaaa 111111111111111111111>");

				forma.getDtoSubCuentas().add(dtoCuenta);
				consultarContratosPorConvenio(con, mapping, forma, request, usuario, paciente, i);
			}
		}
		else
		{
			ArrayList<Convenio> listaConveniosUltimoIngreso=Convenio.consultarConveniosPacienteUltimoIngreso(con, paciente.getCodigoPersona(), true);
			int tamanioLista=listaConveniosUltimoIngreso.size();
			
			for(int i=0; i<tamanioLista; i++)
			{
				Convenio conv=listaConveniosUltimoIngreso.get(i);

				DtoSubCuentas dtoSubCuenta=new DtoSubCuentas();
				
				/*
				 * Verificar si para el 
				 * convenio Odontológico esta definido el indicativo 
				 * 'Para ingresar Paciente Realizar Validación en Base de Datos?'  
				 * Si esta en Si debe verificar de manera Obligatoria 
				 * que el paciente este creado en las estructuras 
				 * definidas para almacenar la información de paciente para 
				 * estos convenios (Anexo 817).
				 * Si no se puede hacer la validación o no se encuentra el paciente en 
				 * el archivo NO se puede continuar con la apertura de la cuenta
				 */
				if(UtilidadTexto.getBoolean(conv.getIngresoBdValido()))
				{
					DtoPacienteConvenioOdo pacienteConvenio=PacienteConvenioOdo.consultarConvenio(con, dtoPaciente,conv.getCodigo());
					if(pacienteConvenio!=null)
					{
						dtoSubCuenta.setConvenio(new InfoDatosInt(conv.getCodigo()));
						dtoSubCuenta.setContrato(conv.getContrato().getCodigo());
						forma.getDtoSubCuentas().add(dtoSubCuenta);
						// Validacion Vigencia 
						if( (UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.getFechaActual(),UtilidadFecha.conversionFormatoFechaAAp(pacienteConvenio.getFechaIniVigencia())) ) 
								|| (UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.conversionFormatoFechaAAp(pacienteConvenio.getFechaFinVigencia()) , UtilidadFecha.getFechaActual()) ) )
						{
							request.setAttribute("codigoDescripcionError", "error.odontologia.vigenciaConvenio");
							return UtilidadSiEs.hacerMapping(con, "paginaError", mapping);
						}
					}
					else
					{
						request.setAttribute("codigoDescripcionError", "errors.pacienteNoEnConvenioOdo");
						return UtilidadSiEs.hacerMapping(con, "paginaError", mapping);
					}
				}
				else
				{
					dtoSubCuenta.setConvenio(new InfoDatosInt(conv.getCodigo()));
					dtoSubCuenta.setContrato(conv.getContrato().getCodigo());
					//logger.info("Se agrega el convenio "+dtoCuenta.getConvenio().getCodigo());
					forma.getDtoSubCuentas().add(dtoSubCuenta);
				}
				
				consultarContratosPorConvenio(con, mapping, forma, request, usuario, paciente, i);
				
				forma.getManejaBonos().add(UtilidadTexto.getBoolean(conv.getRequiereBono()));
				forma.getRequiereIngresoValidacionAuto().add(UtilidadTexto.getBoolean(conv.getRequiereIngresoValorAuto()));
				forma.getIngresoPacienteRequiereAutorizacion().add(UtilidadTexto.getBoolean(conv.getIngresoPacienteReqAutorizacion()));

				logger.info("Si entraaaaaaaaaaaaaaaaaaaaaaaaa");
				
				/*
				 * Romper el ciclo:
				 * Si el paciente ya a tenido Ingresos previos (en estado cerrado)
				 * debe postular el convenio principal del ultimo ingreso,
				 * postulando el convenio prioridad uno (1) del ultimo ingreso del paciente
				 */
				i=tamanioLista;
				
			}	
			
		}
		
		if(forma.getConveniosAMostrarPresupuestoOdo().size()>0)
		{	
			
			for(int i=0; i<forma.getConveniosAMostrarPresupuestoOdo().size(); i++)
			{
			 DtoSubCuentas dtoSubCuenta=new DtoSubCuentas();
			 logger.info("Codigo Convenio >> "+ forma.getConveniosAMostrarPresupuestoOdo().get(i).get("codigoConvenio"));
			 logger.info("Descripcion Convenio >> "+ forma.getConveniosAMostrarPresupuestoOdo().get(i).get("descripcionConvenio"));
			 logger.info("Es convenio Tarjeta Cliente >> "+ forma.getConveniosAMostrarPresupuestoOdo().get(i).get("esConvenioTarjCliente"));
			 
			  if(!existeConvenio(Utilidades.convertirAEntero(forma.getConveniosAMostrarPresupuestoOdo().get(i).get("codigoConvenio")+""), forma.getDtoSubCuentas()))
			  {
			    if(!forma.getConveniosAMostrarPresupuestoOdo().get(i).get("esConvenioTarjCliente").toString().equals(ConstantesBD.acronimoSi))				   
			    	{
			    	 dtoSubCuenta.setConvenio(new InfoDatosInt(Utilidades.convertirAEntero(forma.getConveniosAMostrarPresupuestoOdo().get(i).get("codigoConvenio")+"")));
			    	 dtoSubCuenta.setContrato(Utilidades.convertirAEntero(forma.getConveniosAMostrarPresupuestoOdo().get(i).get("codigoContrato")+""));
			    	
			    	 forma.getDtoSubCuentas().add(dtoSubCuenta);
			    	 
			    	 consultarContratosPorConvenio(con, mapping, forma, request, usuario, paciente, forma.getDtoSubCuentas().size()-1);

					forma.getManejaBonos().add(UtilidadTexto.getBoolean(forma.getConveniosAMostrarPresupuestoOdo().get(i).get("requiereBono")+"")); 
					forma.getRequiereIngresoValidacionAuto().add(UtilidadTexto.getBoolean(forma.getConveniosAMostrarPresupuestoOdo().get(i).get("requiereIngValAuto")+""));
					forma.getIngresoPacienteRequiereAutorizacion().add(UtilidadTexto.getBoolean(forma.getConveniosAMostrarPresupuestoOdo().get(i).get("ingresoPacienteReqAuto")+""));
			    	 
			    	}
			   }			
			
			}  
		}
		
		
		
		if(forma.getDtoSubCuentas().size()==0)
		{
			forma.adicionarConvenioVacio();
		}
		
		
		
		
		forma.setListaAreas(Utilidades.getCentrosCostoXViaIngresoXCAtencion(con, ConstantesBD.codigoViaIngresoConsultaExterna, usuario.getCodigoCentroAtencion(), usuario.getCodigoInstitucionInt(), ConstantesBD.tipoPacienteAmbulatorio));
		
		//Se realizan las validaciones para buscar los convenios odontologicos a postular
		return UtilidadSiEs.hacerMapping(con, "ingresarCuenta", mapping);
	}
	
	
	
	private boolean existeConvenio(int codigoConvenio, ArrayList<DtoSubCuentas> arraySubCuentas)
	{
		boolean existe=false;
		
		for(DtoSubCuentas elem: arraySubCuentas)
		 {
			if(elem.getConvenio().getCodigo()==codigoConvenio)
			{
				existe=true;
			}
		 }
		
		return existe;
	}
	
	
}