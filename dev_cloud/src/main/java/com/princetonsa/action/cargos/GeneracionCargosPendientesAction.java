/*
 * @(#)GeneracionCargosPendientesAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 
 *
 */
package com.princetonsa.action.cargos;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.historiaClinica.UtilidadesHistoriaClinica;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.cargos.GeneracionCargosPendientesForm;
import com.princetonsa.dto.cargos.DtoDetalleCargo;
import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.cargos.GeneracionCargosPendientes;
import com.princetonsa.mundo.facturacion.EstanciaAutomatica;
import com.princetonsa.mundo.medicamentos.AdminMedicamentos;
import com.servinte.axioma.bl.manejoPaciente.facade.ManejoPacienteFacade;
import com.servinte.axioma.common.ErrorMessage;
import com.servinte.axioma.dto.facturacion.MontoCobroDto;
import com.servinte.axioma.dto.manejoPaciente.AutorizacionCapitacionDto;
import com.servinte.axioma.dto.manejoPaciente.DatosPacienteAutorizacionDto;
import com.servinte.axioma.dto.ordenes.OrdenAutorizacionDto;
import com.servinte.axioma.dto.ordenes.ServicioAutorizacionOrdenDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.Contratos;
import com.servinte.axioma.orm.Convenios;
import com.servinte.axioma.orm.delegate.facturacion.convenio.ContratosDelegate;
import com.servinte.axioma.servicio.fabrica.facturacion.FacturacionServicioFabrica;
import com.servinte.axioma.servicio.interfaz.facturacion.convenio.IConvenioServicio;

/**
 * Action, controla todas las opciones dentro de la generación 
 * del cargo incluyendo los posibles casos de error. 
 * Y los casos de flujo.
 * @version 1.0, 
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class GeneracionCargosPendientesAction extends Action
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(GeneracionCargosPendientesAction.class);
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(		ActionMapping mapping,
										ActionForm form,
										HttpServletRequest request,
										HttpServletResponse response ) throws Exception
	{
		Connection con=null;
		try{
		
		if (response==null); //Para evitar que salga el warning
		if(form instanceof GeneracionCargosPendientesForm)
		{
			con=UtilidadBD.abrirConexion();
			GeneracionCargosPendientesForm cargosForm =(GeneracionCargosPendientesForm)form;
			ActionErrors errores = new ActionErrors();
			
			String estado=cargosForm.getEstado(); 
			logger.warn("\n\n GeneracionCargosPendientesAction estado --> "+estado);
			
			UsuarioBasico usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			PersonaBasica paciente=(PersonaBasica)request.getSession().getAttribute("pacienteActivo");
			
			ActionForward validacionesGenerales = this.validacionesComunes(mapping, request, paciente);
			if (validacionesGenerales != null)
			{
				UtilidadBD.closeConnection(con);
				return validacionesGenerales ;
			}
			else if(estado == null)
			{
				cargosForm.reset();	
				logger.warn("Estado no valido dentro del flujo de Generación del cargo (null) ");
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				UtilidadBD.closeConnection(con);
				return mapping.findForward("paginaError");
			}
			
			/**
			 * Validar concurrencia
			 * Si ya está en proceso de facturación, no debe dejar entrar
			 **/
			if(UtilidadValidacion.estaEnProcesofacturacion(con, paciente.getCodigoPersona(), "") )
			{
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.facturacion.cuentaEnProcesoFact", "error.facturacion.cuentaEnProcesoFact", true);
			}
			/**
			 * Validar concurrencia
			 * Si ya está en proceso de distribucion, no debe dejar entrar
			 **/
			else if(UtilidadValidacion.estaEnProcesoDistribucion(con, paciente.getCodigoPersona(), usuario.getLoginUsuario()) )
			{
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.facturacion.cuentaEnProcesoDistribucion", "error.facturacion.cuentaEnProcesoDistribucion", true);
			}
			
			else if(estado.equals("empezar"))
			{	
				return this.accionSeleccionSubCuentaResponsable(cargosForm,mapping,paciente, con);
			}
			else if(estado.equals("listarSolicitudesPendientes"))
			{
				return this.accionListarSolicitudesPendientes(cargosForm, mapping, paciente, con);
			}
			else if (estado.equals("detalle"))
			{
				return this.accionDetalle(cargosForm,mapping,request, con, usuario);
			}
			else if (estado.equals("guardarDetalleSolValoracion"))
			{
				return this.accionGuardarDetalleSolValoracion(cargosForm,mapping,request,paciente,usuario, con);
			}
			else if (estado.equals("resumenExitosoValoracion"))
			{
				return this.accionResumenExitosoValoracion(cargosForm,mapping,request,con);
			}
			else if (estado.equals("listarError"))
			{
				return this.accionListarError(cargosForm,mapping,con,errores, request);
			}
			else if (estado.equals("guardarDetalleSolicitudes"))
			{
				return this.accionGuardarDetalleOtrasSolicitudes(cargosForm,mapping,request,paciente,usuario, con);
			}
			else if (estado.equals("resumenExitosoOtrasSolicitudes"))
			{
				return this.accionResumenExitosoOtrasSolicitud(cargosForm,mapping,request,con);
			}
			else if (estado.equals("generarCagroMedicamentos"))
			{
				return this.accionGenerarCargoMedicamentos(cargosForm, mapping, paciente, usuario, con, request);
			}
			else if(estado.equals("generarCargosXSubCuenta"))
			{
				return this.accionGenerarCargosXSubCuenta(con, cargosForm, mapping, paciente, usuario, request);
			}
			else
			{
				cargosForm.reset();	
				logger.warn("Estado no valido dentro del flujo de Genreación del cargo ");
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				UtilidadBD.closeConnection(con);
				return mapping.findForward("paginaError");
			}
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
	 * 
	 * @param con
	 * @param cargosForm
	 * @param mapping
	 * @param paciente
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionGenerarCargosXSubCuenta(Connection con, GeneracionCargosPendientesForm cargosForm, ActionMapping mapping, PersonaBasica paciente, UsuarioBasico usuario, HttpServletRequest request) throws IPSException 
	{
		//iniciamos la transaccion
		UtilidadBD.iniciarTransaccion(con);
		
		//teniendo el listado de solicitudes entonces trato de generar uno a uno las solicitudes
		for(int w=0; w<Integer.parseInt(cargosForm.getListadoSolicitudesMap("numRegistros").toString()); w++)
		{
			int codigoTipoSolicitud= Integer.parseInt(cargosForm.getListadoSolicitudesMap("codigotiposolicitud_"+w).toString());
			int numeroSolicitud= Integer.parseInt(cargosForm.getListadoSolicitudesMap("numerosolicitud_"+w).toString());
			String esPortatil= cargosForm.getListadoSolicitudesMap("esportatil_"+w)+"";
			
			logger.info("\n\n ES PORTATIL -->"+esPortatil);
			
			
			//**********SOLICITUDES QUE CONTIENEN SERVICIOS
			if(codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudInicialHospitalizacion
				|| codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudInicialUrgencias
				|| codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudInterconsulta
				|| codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudProcedimiento
				|| codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudEvolucion
				|| codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudCargosDirectosServicios
				|| codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudEstancia
				|| codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudCita)
			{
				//guardarDetalleSolValoracion
				//guardarDetalleSolicitudes
				Cargos cargos= new Cargos();
				try
				{
					/*comienzo la transacción para la generación del cambio*/
					cargos.recalcularCargoServicio(con, numeroSolicitud, usuario, ConstantesBD.codigoNuncaValido, "" /*observaciones*/, ConstantesBD.codigoNuncaValido/*codServicio*/, cargosForm.getCodigoSubCuentaResponsable(), ConstantesBD.codigoNuncaValido /*codigoEsquemaTarifarioOPCIONAL*/, false /*filtrarSoloCantidadesMayoresCero*/, ConstantesBD.acronimoNo /*esComponentePaquete*/, esPortatil /*esPortatil*/,Cargos.obtenerFechaCalculoCargo(con, numeroSolicitud));
				}
				catch(Exception e)
				{
					logger.warn("No se pudo generar el cargo "+e);
					cargosForm.reset();
					ArrayList atributosError = new ArrayList();
					atributosError.add("Error generando el cargo ");
					request.setAttribute("codigoDescripcionError", "errors.invalid");				
					request.setAttribute("atributosError", atributosError);
					UtilidadBD.abortarTransaccion(con);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");	
				}
				
				//adicionamos los errores al vector
				if(cargos.getInfoErroresCargo().getTieneErrores())
				{
					for(int x=0; x<cargos.getInfoErroresCargo().getMensajesErrorDetalle().size(); x++)
					{	
						cargosForm.setErrorCargo(cargos.getInfoErroresCargo().getMensajesErrorDetalle().get(x).toString());
					}	
				}
			}
			///*********SOLICITUDES QUE CONTIENEN ARTICULOS
			else if(codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudMedicamentos || 
			        codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos)
			{
				//generarCagroMedicamentos
				Cargos cargos= new Cargos();
				boolean inserto=cargos.recalcularCargoArticulo(con, numeroSolicitud, usuario, ConstantesBD.codigoNuncaValido /*codigoArticuloOPCIONAL*/, cargosForm.getCodigoSubCuentaResponsable() /*subCuentaResponsableOPCIONAL*/, ConstantesBD.codigoNuncaValido /*cantidadArticuloOPCIONAL*/, /*codigoEsquemaTarifarioOPCIONAL*/ ConstantesBD.codigoNuncaValido, false /*filtrarSoloCantidadesMayoresCero*/, ConstantesBD.acronimoNo /*esComponentePaquete*/,Cargos.obtenerFechaCalculoCargo(con, numeroSolicitud));
				
				if(!inserto)
				{
					logger.warn("No se pudo generar el cargo ");
					cargosForm.reset();
					ArrayList atributosError = new ArrayList();
					atributosError.add("Error generando el cargo ");
					request.setAttribute("codigoDescripcionError", "errors.invalid");				
					request.setAttribute("atributosError", atributosError);
					UtilidadBD.abortarTransaccion(con);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}
				
				//adicionamos los errores al vector
				if(cargos.getInfoErroresCargo().getTieneErrores())
				{
					for(int x=0; x<cargos.getInfoErroresCargo().getMensajesErrorDetalle().size(); x++)
					{	
						cargosForm.setErrorCargo(cargos.getInfoErroresCargo().getMensajesErrorDetalle().get(x).toString());
					}	
				}
			}
		}
		
		//UtilidadBD.abortarTransaccion(con);
		UtilidadBD.finalizarTransaccion(con);
		UtilidadBD.closeConnection(con);
		//ya teniendo el listado de errores verificamos si enviamos a la pagina de errores o al resumen exitoso
		if(cargosForm.getErroresCargo().size()>0)
			return mapping.findForward("listarErroresCargoXConvenio");
		else
			return mapping.findForward("paginaResumenExitosoXConvenio");
	}

	/**
	 * 
	 * @param cargosForm
	 * @param mapping
	 * @param paciente
	 * @param usuario
	 * @param con
	 * @param request
	 * @return
	 */
	private ActionForward accionGenerarCargoMedicamentos(GeneracionCargosPendientesForm cargosForm, ActionMapping mapping, PersonaBasica paciente, UsuarioBasico usuario, Connection con, HttpServletRequest request) throws IPSException 
	{		
		/*
		SolicitudMedicamentos sol=new SolicitudMedicamentos();
		sol.actualizarNumeroAutorizacionTransaccional(con, cargosForm.getNumeroAutorizacion(), cargosForm.getNumeroSolicitud(), ConstantesBD.inicioTransaccion);
		*/
		Cargos cargos= new Cargos();
		boolean inserto=cargos.recalcularCargoArticulo(con, cargosForm.getNumeroSolicitud(), usuario, ConstantesBD.codigoNuncaValido /*codigoArticuloOPCIONAL*/, cargosForm.getCodigoSubCuentaResponsable() /*subCuentaResponsableOPCIONAL*/, ConstantesBD.codigoNuncaValido /*cantidadArticuloOPCIONAL*/, /*codigoEsquemaTarifarioOPCIONAL*/ ConstantesBD.codigoNuncaValido, false /*filtrarSoloCantidadesMayoresCero*/, ConstantesBD.acronimoNo /*esComponentePaquete*/,Cargos.obtenerFechaCalculoCargo(con, cargosForm.getNumeroSolicitud()));
		
		logger.info("recalculo exitosamente le cargo-->"+inserto);
		
		if(inserto)
			UtilidadBD.finalizarTransaccion(con);
		else
			UtilidadBD.abortarTransaccion(con);
		//volvemos a cargar la admin pendiente y si existen pendientes entonces retornamos
		cargosForm.setAdministracion(GeneracionCargosPendientes.obtenerSolicitudMedicamentosPendientesXResponsable(con, cargosForm.getNumeroSolicitud(), cargosForm.getCodigoSubCuentaResponsable()));
		
		if(Integer.parseInt(cargosForm.getAdministracion("numRegistros").toString())>0)
		{
			request.setAttribute("codigoDescripcionError", "error.cargo.cargosPendientes");
			UtilidadBD.closeConnection(con);
			return mapping.findForward("paginaResumenSolMed");
		}
		else
		{
			UtilidadBD.closeConnection(con);
			return mapping.findForward("resumenCargosMedicamentos");
		}
		
	}

	/**
	 * metodo para seleccionar la subcuenta responsable 
	 * @param cargosForm
	 * @param mapping
	 * @param paciente
	 * @param con
	 * @return
	 */
	private ActionForward accionSeleccionSubCuentaResponsable(GeneracionCargosPendientesForm cargosForm, ActionMapping mapping, PersonaBasica paciente, Connection con) throws IPSException 
	{
		cargosForm.reset();
		ArrayList<DtoSubCuentas> dtoSubCuentasArray=UtilidadesHistoriaClinica.obtenerResponsablesIngreso(con, paciente.getCodigoIngreso(), true,new String[0],false, "",paciente.getCodigoUltimaViaIngreso());
		cargosForm.setSubCuentasResponsablesTagMap("numRegistros", dtoSubCuentasArray.size());
		for(int w=0; w<dtoSubCuentasArray.size(); w++)
		{
			cargosForm.setSubCuentasResponsablesTagMap("codigo_"+w,dtoSubCuentasArray.get(w).getSubCuenta());
			cargosForm.setSubCuentasResponsablesTagMap("descripcion_"+w,dtoSubCuentasArray.get(w).getConvenio().getNombre()+" - PRIORIDAD: "+dtoSubCuentasArray.get(w).getNroPrioridad());
		}
		UtilidadBD.closeConnection(con);
		return mapping.findForward("seleccionConvenio");
	}

	/**
	 * 
	 * @param cargosForm
	 * @param mapping
	 * @param paciente
	 * @param con
	 * @return
	 */
	private ActionForward accionListarSolicitudesPendientes(	GeneracionCargosPendientesForm cargosForm, 
																ActionMapping mapping,
																PersonaBasica paciente,
																Connection con)  
	{
		Vector cuentasAGenerarCargo= new Vector();
			
		for(int i=0; i<paciente.getCuentasPacienteArray().size(); i++)		
			cuentasAGenerarCargo.add(paciente.getCuentasPacienteArray(i).getCodigoCuenta());		
		
		/* borrar
		logger.info("paciente.getCodigoUltimaViaIngreso()->"+paciente.getCodigoUltimaViaIngreso()+" paciente.getExisteAsocio()->"+paciente.getExisteAsocio()+" paciente.getCodigoCuenta()->"+paciente.getCodigoCuenta()+" paciente.getCodigoCuentaAsocio()->"+paciente.getCodigoCuentaAsocio());		
		cuentasAGenerarCargo.add(paciente.getCodigoCuenta());		
		if((paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion || paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias) && paciente.getExisteAsocio())
			cuentasAGenerarCargo.add(paciente.getCodigoCuentaAsocio());
		*/		
		
		Vector subCuentas= new Vector();
		subCuentas.add((long)cargosForm.getCodigoSubCuentaResponsable());
		cargosForm.setListadoSolicitudesMap(GeneracionCargosPendientes.obtenerSolicitudesCargoPendiente(con, cuentasAGenerarCargo, subCuentas));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listadoSolicitudes");
	}
	
	
	/**
	* Método que se encarga de realizar las validaciones comunes
	* a todos los estados. Valida que
	* exista al menos un paciente cargado con cuenta abierta o asociada
	*
	* @param map Mapping de la aplicación
	* @param req Request de Http
	* @param paciente paciente cargado
	* @return
	*/
	private ActionForward validacionesComunes(ActionMapping map, HttpServletRequest req, PersonaBasica paciente) throws SQLException
	{
		//validar que el paciente este cargado en sesion.
		if( paciente == null || paciente.getTipoIdentificacionPersona().equals("") || paciente.getCodigoPersona()<1 )
		{
			logger.warn("El paciente no es válido (null)");
			req.setAttribute("codigoDescripcionError", "errors.paciente.noCargado");
			return map.findForward("paginaError");
		}
		else if(!paciente.equals("")&& paciente!=null)
		{	
			if (paciente.getCodigoCuenta()<0 && !paciente.getExisteAsocio())
			{
				logger.warn("El paciente no tiene la cuenta abierta ni la tiene asociada");
				req.setAttribute("codigoDescripcionError", "errors.paciente.cuentaNoAbierta"+" ni asociada");
				return map.findForward("paginaError");
			}
		}
		return null;
	}

	/**
	 * 
	 * @param cargosForm
	 * @param mapping
	 * @param request
	 * @param con
	 * @param codigoInstitucionInt
	 * @return
	 */
	private ActionForward accionDetalle(GeneracionCargosPendientesForm cargosForm, ActionMapping mapping, HttpServletRequest request, Connection con, UsuarioBasico usuario) throws IPSException 
	{
		GeneracionCargosPendientes mundo=new GeneracionCargosPendientes();
		mundo.reset();
	
		if(cargosForm.getCodigoTipoSolicitud()==ConstantesBD.codigoTipoSolicitudInicialHospitalizacion
			|| cargosForm.getCodigoTipoSolicitud()==ConstantesBD.codigoTipoSolicitudInicialUrgencias)
		{
			boolean validarCargarDetalle = mundo.cargarValoracionPendiente(con,cargosForm.getNumeroSolicitud());
			
			if(!validarCargarDetalle)
			{
				logger.warn("No se pudo cargar el Detalle de la solicitud de valoración: "+cargosForm.getNumeroSolicitud());
				cargosForm.reset();
				ArrayList atributosError = new ArrayList();
				atributosError.add("El Número de solicitud");
				request.setAttribute("codigoDescripcionError", "errors.invalid");				
				request.setAttribute("atributosError", atributosError);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("paginaError");	
			}
			else
			{
				cargosForm.setValoracionPendienteMap((HashMap)mundo.getValoracionPendienteMap().clone());
				
				double codigoDetalleCargo=Cargos.obtenerCodigoDetalleCargoXSolicitudSubCuentaServicio(con, cargosForm.getNumeroSolicitud(), cargosForm.getCodigoSubCuentaResponsable(), ConstantesBD.acronimoNo/*facturado*/, ConstantesBD.acronimoNo /*paquetizado*/, "" /*esPortatil*/);
				cargosForm.setCodigoDetalleCargo(codigoDetalleCargo);
				cargosForm.setErroresCargo(Cargos.obtenerErroresDetalleCargo(con, cargosForm.getCodigoDetalleCargo()));
				logger.info("===>1"+cargosForm.getErroresCargo());
				cargosForm.setEstado("detalle");
				UtilidadBD.closeConnection(con);
				return mapping.findForward("modificarDetalleValoracion");
			}
		}
		else if(cargosForm.getCodigoTipoSolicitud()==ConstantesBD.codigoTipoSolicitudInterconsulta
				|| cargosForm.getCodigoTipoSolicitud()==ConstantesBD.codigoTipoSolicitudProcedimiento
				|| cargosForm.getCodigoTipoSolicitud()==ConstantesBD.codigoTipoSolicitudEvolucion
				|| cargosForm.getCodigoTipoSolicitud()==ConstantesBD.codigoTipoSolicitudCargosDirectosServicios
				|| cargosForm.getCodigoTipoSolicitud()==ConstantesBD.codigoTipoSolicitudEstancia
				|| cargosForm.getCodigoTipoSolicitud()==ConstantesBD.codigoTipoSolicitudCita)
		{
		    boolean validarCargarDetalle = mundo.cargarSolicitudesInterProcEvolCargosDirectos(con,cargosForm.getNumeroSolicitud(),cargosForm.getCodigoTipoSolicitud(), cargosForm.getEsPortatil());
			if(!validarCargarDetalle)
			{
				logger.warn("No se pudo cargar el Detalle de la solicitud de valoración: "+cargosForm.getNumeroSolicitud());
				cargosForm.reset();
				ArrayList atributosError = new ArrayList();
				atributosError.add("El Número de solicitud");
				request.setAttribute("codigoDescripcionError", "errors.invalid");				
				request.setAttribute("atributosError", atributosError);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("paginaError");	
			}
			else
			{
				cargosForm.setSolicitudServicioPendienteMap((HashMap)mundo.getSolicitudServicioPendienteMap().clone());
				cargosForm.setCodigoAxioma(mundo.getSolicitudServicioPendienteMap().get("codigoaxioma").toString());
				double codigoDetalleCargo=Cargos.obtenerCodigoDetalleCargoXSolicitudSubCuentaServicio(con, cargosForm.getNumeroSolicitud(), cargosForm.getCodigoSubCuentaResponsable(), ConstantesBD.acronimoNo/*facturado*/, ConstantesBD.acronimoNo /*paquetizado*/, cargosForm.getEsPortatil());
				cargosForm.setCodigoDetalleCargo(codigoDetalleCargo);
				cargosForm.setErroresCargo(Cargos.obtenerErroresDetalleCargo(con, cargosForm.getCodigoDetalleCargo()));
				logger.info("===>1"+cargosForm.getErroresCargo());
				if(cargosForm.getCodigoTipoSolicitud()==ConstantesBD.codigoTipoSolicitudEvolucion)
				{
					cargosForm.setEsCobrable(true);
				}
				
				cargosForm.setEstado("detalle");
				UtilidadBD.closeConnection(con);
				return mapping.findForward("modificarDetalleSolProcOIntercOEvol");
			}
		}
		else if(cargosForm.getCodigoTipoSolicitud()==ConstantesBD.codigoTipoSolicitudMedicamentos || 
		        cargosForm.getCodigoTipoSolicitud()==ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos)
		{
			AdminMedicamentos administracion=new AdminMedicamentos();
			cargosForm.setFechaAdministracion(administracion.fechaUltimaAdministracion(con, cargosForm.getNumeroSolicitud()));
			try 
			{
				administracion.encabezadoSolicitudMedicamentos(con, cargosForm.getNumeroSolicitud());
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
			
			//cargosForm.setNumeroAutorizacion(administracion.getNumeroAutorizacion());
			
			cargosForm.setAdministracion(GeneracionCargosPendientes.obtenerSolicitudMedicamentosPendientesXResponsable(con, cargosForm.getNumeroSolicitud(), cargosForm.getCodigoSubCuentaResponsable()));
			cargosForm.setEstado("detalle");
			
			UtilidadBD.closeConnection(con);
			return mapping.findForward("modificarDetalleSolMed");
		}
		return null;
	
	}

	/**
	 * Este método especifica las acciones a realizar en el estado
	 * guardarDetalleSolValoracion.
	 * Se copian las propiedades del objeto GeneracionCargos
	 * en el objeto mundo GeneracionCargosPendientes
	*/
	private ActionForward accionGuardarDetalleSolValoracion(	GeneracionCargosPendientesForm cargosForm, 
																ActionMapping mapping,
																HttpServletRequest request,
																PersonaBasica paciente,
																UsuarioBasico usuario,
																Connection con) throws SQLException, IPSException
	{
		Cargos cargos= new Cargos();
		Convenios convenioResponsable = null;	
		EstanciaAutomatica estancia = new EstanciaAutomatica();
		HashMap mapaCuentas = null;
		ActionErrors errores = new ActionErrors();
		AutorizacionCapitacionDto generacionAutorizacion = null;
		Contratos contratos = new Contratos();
		
		//1. PRIMERO INICIALIZAMOS LA TRANSACCION
		UtilidadBD.iniciarTransaccion(con);
		
		/*modifico el tipo de recargo, pero en caso de que no pueda generar el cargo tengo que devolverme al anterior*/
		boolean inserto =	GeneracionCargosPendientes.modificarTipoRecargo(con, cargosForm.getNumeroSolicitud(), Integer.parseInt(cargosForm.getValoracionPendienteMap("codigotiporecargo").toString()));
		if(!inserto)
		{
			logger.warn("No se pudo modificar el tipo de recargo del # de solicitud: "+cargosForm.getNumeroSolicitud()+" " +
								"el tipo de recargo es: "+cargosForm.getValoracionPendienteMap("codigotiporecargo"));
			cargosForm.reset();
			ArrayList atributosError = new ArrayList();
			atributosError.add("Error modificando el tipo de recargo ");
			request.setAttribute("codigoDescripcionError", "errors.invalid");				
			request.setAttribute("atributosError", atributosError);
			UtilidadBD.abortarTransaccion(con);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("paginaError");	
		}
		
		try
		{
			/*se coje el cod axioma de la forma y se obtiene el valor del cod del servicio*/
			String tempCodServicio[]= cargosForm.getCodigoAxioma().split("-",2);
			int codServicio= Integer.parseInt(tempCodServicio[1]);
			
			/*
			 * Se desarrollan los cambios en el DCU 45 V2.1: Permitir generar autorizaciones automaticas
			 * de capitacion subcontratada.
			 * Validar si el convenio responsable de la cuenta del paciente es de tipo capitado y maneja
			 * capitacion subcontratada
			 * */
			//TODO
			try{
				HibernateUtil.beginTransaction();
				IConvenioServicio convenioServicio = FacturacionServicioFabrica.crearConvenioServicio();
				ContratosDelegate contratosDelegate = new ContratosDelegate();
				convenioResponsable = convenioServicio.findById(paciente.getCodigoConvenio());
				contratos = contratosDelegate.findById(paciente.getCodigoContrato());
				HibernateUtil.endTransaction();
			}
			catch (Exception e) {
				Log4JManager.error(e);
				HibernateUtil.abortTransaction();
			}
			generacionAutorizacion = new AutorizacionCapitacionDto();
			
			if(convenioResponsable != null &&  
 					((convenioResponsable.getTiposContrato().getCodigo())==ConstantesBD.codigoTipoContratoCapitado) &&         					
 					(convenioResponsable.getCapitacionSubcontratada()==ConstantesBD.acronimoSiChar)){
				
				String consecutivoAutoEntSub = "";
     			String consecutivoAutoCapiSub = "";
     			boolean convenioManejaPresupuesto=false;
         		
     			if(convenioResponsable.getManejaPresupCapitacion()!=null){
     				if (convenioResponsable.getManejaPresupCapitacion() == ConstantesBD.acronimoSiChar){
             			convenioManejaPresupuesto=true;
             		}
     			}
         		
     			//Verifico si se encuentra definido el consecutivo de Autorizaciones de Entidad Subcontratada
     			consecutivoAutoEntSub = UtilidadBD.obtenerValorActualTablaConsecutivos(
						con, ConstantesBD.nombreConsecutivoAutorizacionEntiSub, usuario.getCodigoInstitucionInt());
     			
     			//Verifico si se encuentra definido el consecutivo de capitación Subcontratada
     			consecutivoAutoCapiSub = UtilidadBD.obtenerValorActualTablaConsecutivos(
						con, ConstantesBD.nombreConsecutivoAutorizacionPoblacionCapitada, usuario.getCodigoInstitucionInt());
     			ManejoPacienteFacade manejoPacienteFacade = new ManejoPacienteFacade();	    	         			
     			if (!UtilidadTexto.isEmpty(consecutivoAutoEntSub) && !UtilidadTexto.isEmpty(consecutivoAutoCapiSub)) 
 				{

     				cargos.getDtoDetalleCargo().setNumeroSolicitud(cargosForm.getNumeroSolicitud());
     				cargos.getDtoDetalleCargo().setObservaciones(cargosForm.getObservaciones());
     				cargos.getDtoDetalleCargo().setCodigoSubcuenta(cargosForm.getCodigoSubCuentaResponsable());
     				cargos.getDtoDetalleCargo().setCodigoServicio(codServicio);
     				
     				//OBTENEMOS LOS RESPONSABLES DE LA CUENTA
         			ArrayList<DtoSubCuentas> dtoSubCuentasVector= UtilidadesHistoriaClinica.obtenerResponsablesIngreso(con, paciente.getCodigoIngreso(),false,new String[0],false, "" /*subCuenta*/,ConstantesBD.codigoNuncaValido /*Vía de ingreso*/);
         			DtoSubCuentas dtoSubCuenta = new DtoSubCuentas();
         			dtoSubCuenta=dtoSubCuentasVector.get(0);
         			
         			//Genero la autorización de entidad subcontratad por la capitación
         			OrdenAutorizacionDto ordenAutorizar = null;
         			ServicioAutorizacionOrdenDto servicioAutorizar = null;
         			DatosPacienteAutorizacionDto datosPacienteAutorizar = null;
         			MontoCobroDto montoCobro = null;
         			
         			//Envio el servicio a autorizar
         			servicioAutorizar = new ServicioAutorizacionOrdenDto();
         			servicioAutorizar.setCodigo(codServicio);
         			servicioAutorizar.setAutorizar(true);
         			long cantidad=1;
         			servicioAutorizar.setCantidad(cantidad);
         			servicioAutorizar.setUrgente(ConstantesBD.acronimoNoChar);
         				
         			//Envio la iformación de la solicitud
         			ordenAutorizar = new OrdenAutorizacionDto();
         			ordenAutorizar.setCodigoOrden((long)cargosForm.getNumeroSolicitud());
         			ordenAutorizar.setCodigoCentroCostoEjecuta(Integer.parseInt(cargosForm.getListadoSolicitudesMap().get("codigocentrocostosolicitante_"+0)+""));
         			ordenAutorizar.setCodigoViaIngreso(ConstantesBD.codigoViaIngresoHospitalizacion);
         			ordenAutorizar.setEsPyp(false);
         			ordenAutorizar.setTipoEntidadEjecuta(cargosForm.getListadoSolicitudesMap().get("tipoentidadejecuta_"+0)+"");
         			ordenAutorizar.getContrato().getConvenio().setCodigo(paciente.getCodigoConvenio());
         			ordenAutorizar.getContrato().getConvenio().setNombre(convenioResponsable.getNombre());
         			ordenAutorizar.getContrato().setNumero(contratos.getNumeroContrato());
         			
         			ordenAutorizar.getContrato().getConvenio().setConvenioManejaPresupuesto(convenioManejaPresupuesto);
         			ordenAutorizar.getContrato().setCodigo(paciente.getCodigoContrato());
         			if (convenioResponsable.getCapitacionSubcontratada().equals(ConstantesBD.acronimoSiChar)){
         				ordenAutorizar.getContrato().getConvenio().setConvenioManejaCapiSub(true);
         			}else {
         				ordenAutorizar.getContrato().getConvenio().setConvenioManejaCapiSub(false);
         			}
         			ordenAutorizar.setMigrado(ConstantesBD.acronimoNoChar);
         			ordenAutorizar.getServiciosPorAutorizar().add(servicioAutorizar);
         			ordenAutorizar.setClaseOrden(ConstantesBD.claseOrdenCargoDirecto);
         			ordenAutorizar.setTipoOrden(ConstantesBD.codigoTipoSolicitudEstancia);
         			
         			//Envio los datos del paciente para la autorización de capita
         			datosPacienteAutorizar = new DatosPacienteAutorizacionDto();
         			datosPacienteAutorizar.setTipoPaciente(dtoSubCuenta.getTipoPaciente());
         			datosPacienteAutorizar.setTipoAfiliado(dtoSubCuenta.getTipoAfiliado());
         			datosPacienteAutorizar.setClasificacionSocieconomica(dtoSubCuenta.getClasificacionSocioEconomica());
         			datosPacienteAutorizar.setCodConvenioCuenta(paciente.getCodigoConvenio());
         			datosPacienteAutorizar.setNaturalezaPaciente(dtoSubCuenta.getNaturalezaPaciente());
         			datosPacienteAutorizar.setCuenta(paciente.getCodigoCuenta());
         			datosPacienteAutorizar.setCodigoPaciente(paciente.getCodigoPersona());
         			datosPacienteAutorizar.setCuentaAbierta(true);
         			if (dtoSubCuenta.getMontoCobro() != 0){
         				datosPacienteAutorizar.setCuentaManejaMontos(true);
         				datosPacienteAutorizar.setPorcentajeMontoCuenta(dtoSubCuenta.getPorcentajeMontoCobro());
         			} else {
         				datosPacienteAutorizar.setCuentaManejaMontos(false);
         			}
         				
         			//Envio la información del monto para la generación de la autorización
         			montoCobro = new MontoCobroDto();
         			montoCobro.setTipoDetalleMonto(dtoSubCuenta.getTipoDetalleMonto());
         			montoCobro.setValorMonto(dtoSubCuenta.getValorMontoGeneral());
         			montoCobro.setPorcentajeMonto(dtoSubCuenta.getPorcentajeMontoGeneral());
         			montoCobro.setTipoMonto(dtoSubCuenta.getTipoMonto());
         			montoCobro.setCantidadMonto(dtoSubCuenta.getCantidadMontoGeneral());
         			montoCobro.setCodDetalleMonto(dtoSubCuenta.getMontoCobro());
         			
         			//Envio la información correspondiente al dto del proceso de autorización
         			generacionAutorizacion.setTipoAutorizacion(ConstantesIntegridadDominio.acronimoAutomatica);
         			generacionAutorizacion.setCodigoInstitucion(usuario.getCodigoInstitucionInt());
         			generacionAutorizacion.setLoginUsuario(usuario.getLoginUsuario());
         			generacionAutorizacion.setCentroAtencion(usuario.getCodigoCentroAtencion());
         			
         			generacionAutorizacion.getOrdenesAutorizar().add(ordenAutorizar);
         			generacionAutorizacion.setDatosPacienteAutorizar(datosPacienteAutorizar);
         			generacionAutorizacion.setMontoCobroAutorizacion(montoCobro);
         			
         			//Se hace el llamado al proceso 1106 
         			manejoPacienteFacade.generarProcesoAutorizacionEstanciaAutomaticaCargosPendientes(generacionAutorizacion, 
         					estancia, mapaCuentas, usuario, codServicio, 0, false, cargos);
         			
         			if (!generacionAutorizacion.isProcesoExitoso()){
         				cargosForm.setEsProcesoAutorizacionCapita(true);
         			}else{
         				cargosForm.setEsProcesoAutorizacionCapita(false);
         			}
     				
					List<AutorizacionCapitacionDto> listaAutorizacionCapitacion = new ArrayList<AutorizacionCapitacionDto>();
					listaAutorizacionCapitacion.add(generacionAutorizacion);
         			manejoPacienteFacade.obtenerMensajesError(listaAutorizacionCapitacion, errores);
     				
 				} else {//Si no se encuentran definidos los consecutivos
 					
 					ErrorMessage error = new ErrorMessage("errors.autorizacion.noGeneroCargoPendientePorConsecutivos");
 					generacionAutorizacion.setMensajeErrorGeneral(error);
 					generacionAutorizacion.setProcesoExitoso(false);
 					generacionAutorizacion.setVerificarDetalleError(false);
 					
					List<AutorizacionCapitacionDto> listaAutorizacionCapitacion = new ArrayList<AutorizacionCapitacionDto>();
					listaAutorizacionCapitacion.add(generacionAutorizacion);
 					manejoPacienteFacade.obtenerMensajesError(listaAutorizacionCapitacion, errores);
 					
 					cargosForm.setEsProcesoAutorizacionCapita(true);
 					cargos.getInfoErroresCargo().setMensajesErrorDetalle("errors.autorizacion.noGeneroCargoPendientePorConsecutivos");
 				}
     				
			}else { //El convenio no es capitado
				//Se continua con el flujo actual de la funcionalidad
				cargos.recalcularCargoServicio(con, cargosForm.getNumeroSolicitud(), usuario, ConstantesBD.codigoNuncaValido, cargosForm.getObservaciones(), codServicio, cargosForm.getCodigoSubCuentaResponsable(), ConstantesBD.codigoNuncaValido /*codigoEsquemaTarifarioOPCIONAL*/, false /*filtrarSoloCantidadesMayoresCero*/, ConstantesBD.acronimoNo /*esComponentePaquete*/, ""/*esPortatil*/,Cargos.obtenerFechaCalculoCargo(con, cargosForm.getNumeroSolicitud()));
			}
			
		}
		catch(Exception e)
		{
			logger.warn("No se pudo generar el cargo "+e);
			cargosForm.reset();
			ArrayList atributosError = new ArrayList();
			atributosError.add("Error generando el cargo ");
			request.setAttribute("codigoDescripcionError", "errors.invalid");				
			request.setAttribute("atributosError", atributosError);
			UtilidadBD.abortarTransaccion(con);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("paginaError");	
		}
		
		/*para el caso en que se escoja excenta entonces verifico si 
		 * se generó el cambio y hago luego un update en los estados 
		 * de la solicitud de facturación el número de sol y las observaciones*/
		
		if(!cargos.getInfoErroresCargo().getTieneErrores())
		{
			/*
			inserto= ActualizacionAutorizacion.modificarNumeroAutorizacionSolicitudes(con, cargosForm.getValoracionPendienteMap("numeroautorizacion").toString(), cargosForm.getNumeroSolicitud());
			
			if(!inserto)
			{
				logger.warn("No se pudo modificar el numero de autorización del # de solicitud: "+cargosForm.getNumeroSolicitud());
				cargosForm.reset();
				ArrayList atributosError = new ArrayList();
				atributosError.add("Error modificando el número Autorización ");
				request.setAttribute("codigoDescripcionError", "errors.invalid");				
				request.setAttribute("atributosError", atributosError);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("paginaError");	
			}
			*/
			
			/*en caso de no ser cobrable entonces cambiamos a estado de facturación EXCENTA*/
			if(!cargosForm.getEsCobrable())
			{
				//cargosForm.getNumeroSolicitud(), cargosForm.getCodigoSubCuentaResponsable()
				inserto= Cargos.modificarEstadoCargo(con, cargos.getDtoDetalleCargo().getCodigoDetalleCargo(), ConstantesBD.codigoEstadoFExento);
				
				if(!inserto)
				{
					logger.warn("No se pudo modificar el estado facturación del # de solicitud: "+cargosForm.getNumeroSolicitud());
					cargosForm.reset();
					ArrayList atributosError = new ArrayList();
					atributosError.add("Error modificando el estado de facturación ");
					request.setAttribute("codigoDescripcionError", "errors.invalid");				
					request.setAttribute("atributosError", atributosError);
					UtilidadBD.abortarTransaccion(con);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}
			}		
		}
		else
		{
			//como no se pudo generar el cargo, entonces tengo que devolverme al tipo de recargo anterior 
			inserto =	GeneracionCargosPendientes.modificarTipoRecargo(con, cargosForm.getNumeroSolicitud(), Integer.parseInt(cargosForm.getValoracionPendienteMap("codigotiporecargobackup").toString()));
			if(!inserto)
			{
				logger.warn("No se pudo modificar el tipo de recargo del # de solicitud: "+cargosForm.getNumeroSolicitud()+" " +
									"el tipo de recargo es: "+cargosForm.getValoracionPendienteMap("codigotiporecargobackup"));
				cargosForm.reset();
				ArrayList atributosError = new ArrayList();
				atributosError.add("Error modificando el tipo de recargo ");
				request.setAttribute("codigoDescripcionError", "errors.invalid");				
				request.setAttribute("atributosError", atributosError);
				UtilidadBD.abortarTransaccion(con);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("paginaError");	
			}
		}
		
		//SE FINALIZA LA TRANSACCION
		logger.info("\n\ninserto 100% cargos pendientes");
		//como todo salio bien entonces cargamos el codigo del detalle del cargo pendiente o cargado
		
		cargosForm.setCodigoDetalleCargo(cargos.getDtoDetalleCargo().getCodigoDetalleCargo());
		cargosForm.setErroresCargo(cargos.getInfoErroresCargo().getMensajesErrorDetalle());
		
		UtilidadBD.finalizarTransaccion(con);
		
		if (! cargos.getInfoErroresCargo().getTieneErrores() )
		{
			return accionResumenExitosoValoracion(cargosForm, mapping, request, con);
		}
		else
		{
			return accionListarError(cargosForm, mapping, con, errores, request);	
		}
	}
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * resumenExitosoValoracion
	 */
	private ActionForward accionResumenExitosoValoracion(		GeneracionCargosPendientesForm cargosForm,
																ActionMapping mapping, 
																HttpServletRequest request, 
																Connection con) throws SQLException, IPSException
	{
		GeneracionCargosPendientes mundo= new GeneracionCargosPendientes();
		boolean validarCargar= mundo.cargarValoracionPendiente(con, cargosForm.getNumeroSolicitud());
		
		//obtenemos la informacion del detalle del cargo
		cargarInformacionCargo(con, cargosForm);
		
		if(validarCargar)
		{
			cargosForm.setValoracionPendienteMap((HashMap)mundo.getValoracionPendienteMap().clone());
			UtilidadBD.closeConnection(con);	
			return mapping.findForward("paginaResumenExitosoSolVal");
		}
		else
		{
			logger.warn("Número de solicitud inválido "+cargosForm.getNumeroSolicitud());
			cargosForm.reset();
			ArrayList atributosError = new ArrayList();
			atributosError.add(" Número de solicitud");
			request.setAttribute("codigoDescripcionError", "errors.invalid");				
			request.setAttribute("atributosError", atributosError);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("paginaError");		
		}
	}
	
	/**
	 * 
	 * @param cargosForm
	 * @param mapping
	 * @param con
	 * @return
	 */
	private ActionForward accionListarError(GeneracionCargosPendientesForm cargosForm, ActionMapping mapping, Connection con, ActionErrors errores, HttpServletRequest request) 
	{
		UtilidadBD.closeConnection(con);
		if(cargosForm.isEsProcesoAutorizacionCapita()){
			saveErrors(request, errores);
		}
		return mapping.findForward("listarErrorCargo");
	}

	
	/**
	 * 
	 * @param con
	 * @param cargosForm
	 */
	private void cargarInformacionCargo(Connection con, GeneracionCargosPendientesForm cargosForm) throws IPSException
	{
		DtoDetalleCargo criteriosBusquedaDtoDetalleCargo= new DtoDetalleCargo();
		criteriosBusquedaDtoDetalleCargo.setCodigoDetalleCargo(cargosForm.getCodigoDetalleCargo());
		DtoDetalleCargo dtoDetalleCargo= Cargos.cargarDetalleCargos(con, criteriosBusquedaDtoDetalleCargo).get(0);
		cargosForm.setValorTotal(dtoDetalleCargo.getValorTotalCargado());
		cargosForm.setObservaciones(dtoDetalleCargo.getObservaciones());
		cargosForm.setDescripcionServicioCups(dtoDetalleCargo.getNombreServicio());
	}
	
	/**
	 * 
	 * @param cargosForm
	 * @param mapping
	 * @param request
	 * @param paciente
	 * @param usuario
	 * @param con
	 * @return
	 */
	private ActionForward accionGuardarDetalleOtrasSolicitudes(GeneracionCargosPendientesForm cargosForm, ActionMapping mapping, HttpServletRequest request, PersonaBasica paciente, UsuarioBasico usuario, Connection con) throws IPSException 
	{
		Cargos cargos= new Cargos();
		UtilidadBD.iniciarTransaccion(con);
		boolean inserto=false;
		ActionErrors errores = new ActionErrors();
		try
		{
			/*se coje el cod axioma de la forma y se obtiene el valor del cod del servicio*/
			String tempCodServicio[]= cargosForm.getCodigoAxioma().split("-",2);
			int codServicio= Integer.parseInt(tempCodServicio[1]);
			
			/*comienzo l transacción para la generación del cambio*/
			cargos.recalcularCargoServicio(con, cargosForm.getNumeroSolicitud(), usuario, ConstantesBD.codigoNuncaValido, cargosForm.getObservaciones(), codServicio, cargosForm.getCodigoSubCuentaResponsable(), ConstantesBD.codigoNuncaValido /*codigoEsquemaTarifarioOPCIONAL*/, false /*filtrarSoloCantidadesMayoresCero*/, ConstantesBD.acronimoNo /*esComponentePaquete*/, cargosForm.getEsPortatil()/*esPortatil*/,Cargos.obtenerFechaCalculoCargo(con, cargosForm.getNumeroSolicitud()));
		}
		catch(Exception e)
		{
			logger.warn("No se pudo generar el cargo ");
			cargosForm.reset();
			ArrayList atributosError = new ArrayList();
			atributosError.add("Error generando el cargo ");
			request.setAttribute("codigoDescripcionError", "errors.invalid");				
			request.setAttribute("atributosError", atributosError);
			UtilidadBD.abortarTransaccion(con);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("paginaError");	
		}
		
		if(!cargos.getInfoErroresCargo().getTieneErrores())
		{
			/*
			inserto= ActualizacionAutorizacion.modificarNumeroAutorizacionSolicitudes(con, cargosForm.getSolicitudServicioPendienteMap("numeroautorizacion").toString(), cargosForm.getNumeroSolicitud());
			
			if(!inserto)
			{
				logger.warn("No se pudo modificar el numero de autorización del # de solicitud: "+cargosForm.getNumeroSolicitud());
				cargosForm.reset();
				ArrayList atributosError = new ArrayList();
				atributosError.add("Error modificando el número Autorización ");
				request.setAttribute("codigoDescripcionError", "errors.invalid");				
				request.setAttribute("atributosError", atributosError);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("paginaError");	
			}
			*/
			if (cargosForm.getCodigoTipoSolicitud()==ConstantesBD.codigoTipoSolicitudEvolucion	)
			{
				if(cargosForm.getEsCobrable()==false)
				{
					//cargosForm.getNumeroSolicitud(), cargosForm.getCodigoSubCuentaResponsable()
					inserto= Cargos.modificarEstadoCargo(con, cargos.getDtoDetalleCargo().getCodigoDetalleCargo() , ConstantesBD.codigoEstadoFExento);
					
					if(!inserto)
					{
						logger.warn("No se pudo modificar el estado facturación del # de solicitud: "+cargosForm.getNumeroSolicitud());
						cargosForm.reset();
						ArrayList atributosError = new ArrayList();
						atributosError.add("Error modificando el estado de facturación ");
						request.setAttribute("codigoDescripcionError", "errors.invalid");				
						request.setAttribute("atributosError", atributosError);
						UtilidadBD.abortarTransaccion(con);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("paginaError");
					}	
				}		
			}
		}
		
		//SE FINALIZA LA TRANSACCION
		logger.info("\n\ninserto 100% cargos pendientes");
		//como todo salio bien entonces cargamos el codigo del detalle del cargo pendiente o cargado
		
		cargosForm.setCodigoDetalleCargo(cargos.getDtoDetalleCargo().getCodigoDetalleCargo());
		cargosForm.setErroresCargo(cargos.getInfoErroresCargo().getMensajesErrorDetalle());
		
		UtilidadBD.finalizarTransaccion(con);
		
		if (! cargos.getInfoErroresCargo().getTieneErrores() )
		{
			return accionResumenExitosoOtrasSolicitud(cargosForm, mapping, request, con);
		}
		else
		{
			return accionListarError(cargosForm, mapping, con, errores, request);	
		}
	}
	
	/**
	 * 
	 * @param cargosForm
	 * @param mapping
	 * @param request
	 * @param con
	 * @return
	 */
	private ActionForward accionResumenExitosoOtrasSolicitud(GeneracionCargosPendientesForm cargosForm, ActionMapping mapping, HttpServletRequest request, Connection con) throws IPSException 
	{
		GeneracionCargosPendientes mundo= new GeneracionCargosPendientes();
		boolean validarCargar= mundo.cargarSolicitudesInterProcEvolCargosDirectos(con, cargosForm.getNumeroSolicitud(), cargosForm.getCodigoTipoSolicitud(), cargosForm.getEsPortatil());
		
		//obtenemos la informacion del detalle del cargo
		cargarInformacionCargo(con, cargosForm);
		
		if(validarCargar)
		{
			cargosForm.setValoracionPendienteMap((HashMap)mundo.getValoracionPendienteMap().clone());
			UtilidadBD.closeConnection(con);	
			return mapping.findForward("paginaResumenExitosoOtrasSol");
		}
		else
		{
			logger.warn("Número de solicitud inválido "+cargosForm.getNumeroSolicitud());
			cargosForm.reset();
			ArrayList atributosError = new ArrayList();
			atributosError.add(" Número de solicitud");
			request.setAttribute("codigoDescripcionError", "errors.invalid");				
			request.setAttribute("atributosError", atributosError);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("paginaError");		
		}
	
	}
	
}
