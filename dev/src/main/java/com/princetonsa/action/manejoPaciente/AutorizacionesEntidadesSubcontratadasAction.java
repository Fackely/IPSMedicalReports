package com.princetonsa.action.manejoPaciente;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.InfoResponsableCobertura;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.manejoPaciente.AutorizacionesEntidadesSubcontratadasForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.cargos.CargosEntidadesSubcontratadasDao;
import com.princetonsa.dto.facturacion.DTOMontosCobroDetalleGeneral;
import com.princetonsa.dto.facturacion.DtoContratoEntidadSub;
import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.princetonsa.dto.inventario.DtoArticulosAutorizaciones;
import com.princetonsa.dto.inventario.DtoServiciosAutorizaciones;
import com.princetonsa.dto.manejoPaciente.DTOReporteAutorizacionSeccionAutorizacion;
import com.princetonsa.dto.manejoPaciente.DTOReporteAutorizacionSeccionPaciente;
import com.princetonsa.dto.manejoPaciente.DTOReporteEstandarAutorizacionServiciosArticulos;
import com.princetonsa.dto.manejoPaciente.DtoDiagnostico;
import com.princetonsa.dto.manejoPaciente.DtoGeneralReporteArticulosAutorizados;
import com.princetonsa.dto.manejoPaciente.DtoGeneralReporteServiciosAutorizados;
import com.princetonsa.dto.manejoPaciente.DtoSolicitudesSubCuenta;
import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.CargosEntidadesSubcontratadas;
import com.princetonsa.mundo.facturacion.Cobertura;
import com.princetonsa.mundo.manejoPaciente.AutorizacionesEntidadesSubcontratadas;
import com.princetonsa.pdf.AutorizacionEntidadSubContratadaPdf;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.generadorReporte.manejoPaciente.formatoEstandar.formatoAutorizacionMedicamentosInsumos.GeneradorReporteFormatoEstandarAutorArticulos;
import com.servinte.axioma.generadorReporte.manejoPaciente.formatoEstandar.formatoAutorizacionServicios.GeneradorReporteFormatoEstandarAutorservicio;
import com.servinte.axioma.generadorReporte.manejoPaciente.formatosCapitacion.formatosAutorizacionMedicamentosInsumos.GeneradorReporteFormatoCapitacionAutorArticulos;
import com.servinte.axioma.generadorReporte.manejoPaciente.formatosCapitacion.formatosAutorizacionServicios.GeneradorReporteFormatoCapitacionAutorservicio;
import com.servinte.axioma.orm.CentrosCosto;
import com.servinte.axioma.orm.Convenios;
import com.servinte.axioma.orm.EntidadesSubcontratadas;
import com.servinte.axioma.orm.delegate.facturacion.EntidadesSubcontratadasDelegate;
import com.servinte.axioma.servicio.fabrica.facturacion.FacturacionServicioFabrica;
import com.servinte.axioma.servicio.interfaz.facturacion.IDetalleMontoGeneralServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.convenio.IConvenioServicio;

public class AutorizacionesEntidadesSubcontratadasAction extends Action {

	Logger logger = Logger.getLogger(AutorizacionesEntidadesSubcontratadasAction.class);
	AutorizacionesEntidadesSubcontratadas mundo;
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		Connection con = null;
		try{
			if(response == null);

			if (form instanceof AutorizacionesEntidadesSubcontratadasForm) 
			{			 

				con = UtilidadBD.abrirConexion();

				if(con == null)
				{
					request.setAttribute("CodigoDescripcionError","erros.problemasBd");
					return mapping.findForward("paginaError");
				}

				//Usuario cargado en session
				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				//paciente cargado en sesion 
				PersonaBasica paciente= (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
				//Institucion
				InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");

				mundo = new AutorizacionesEntidadesSubcontratadas();

				AutorizacionesEntidadesSubcontratadasForm forma = (AutorizacionesEntidadesSubcontratadasForm)form;		
				String estado = forma.getEstado();		 

				logger.info("-------------------------------------");
				logger.info("Valor del Estado    >> "+forma.getEstado());			 
				logger.info("-------------------------------------");
				logger.info("-------------------------------------");

				if(estado.equals("empezar"))
				{ 
					forma.reset();	  
					UtilidadBD.closeConnection(con); 
					return mapping.findForward("principal");

				}else if(estado.equals("consultaPaciente"))
				{  
					forma.reset();
					ActionForward forward = new ActionForward();
					forward=accionValidarPaciente(con, forma, paciente, usuario, request, mapping);
					if(forward != null){
						return forward;
					}
					UtilidadBD.closeConnection(con);					
					forma.setOpcionListado("paciente");
					return listadoSolicitudes(forma, paciente, usuario, request, mapping);

				}else if(estado.equals("consultarRango"))
				{ 
					forma.reset();
					forma.setOpcionListado("rango");
					return accionBuscarSolicitudesRango(con,forma, usuario,paciente,institucionBasica, mapping, request);

				}else  if (estado.equals("ordenar"))
				{    
					UtilidadBD.closeConnection(con); 
					return ordenarXColumna(forma, mapping);

				} else  if (estado.equals("autorizar"))
				{   
					forma.setParametrosFiltros("operacionExitosa",ConstantesBD.acronimoNo);	
					forma.setEntidadEjecuta("");
					return accionAutorizar(con,forma, usuario, mapping, request);

				}else if (estado.equals("guardarAutorizacion"))
				{   
					return accionGuardarAutorizacion(con,forma, usuario,paciente,institucionBasica, mapping, request);

				}else if (estado.equals("imprimirAutorizacion"))
				{   				
					imprimirAutorizacion(con,forma,usuario,paciente,institucionBasica, mapping,request);
					return mapping.findForward("resumenAutorizacion");

				}else if (estado.equals("buscarSolicitudes"))
				{   	
					UtilidadBD.closeConnection(con); 
					return accionBusquedaSolicitudesRango(forma, usuario,paciente,institucionBasica, mapping, request);											   

				}else if (estado.equals("filtrarCentrosCosto"))
				{   													  
					return accionFiltrarCentrosCosto(con,forma,response);												   

				}else if (estado.equals("filtrarTipoPaciente"))
				{   													  
					return accionFiltrarTipoPaciente(con,forma,response);												   

				}else if (estado.equals("filtrarDireccionTelefonoEnt"))
				{
					return accionFiltrarDireccionTelefonoEnt(con, forma, response);

				}else if (estado.equals("redireccion"))
				{
					UtilidadBD.closeConnection(con);
					response.sendRedirect(forma.getLinkSiguiente());
					return null;

				} else if (estado.equals("refrescarAutorizacion"))
				{
					UtilidadBD.closeConnection(con); 
					return mapping.findForward("resumenAutorizacion");
				}
				else if (estado.equals("cambioEntidadEjecuta"))
				{
					return accionCambiarTipoEntidadEjecuta(con, forma, usuario, mapping, request);
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
	 * Metodo que ejecuta la busqueda segun los parametros diligenciados
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * @param institucionBasica
	 * @param mapping
	 * @param request
	 * @return
	 */
  @SuppressWarnings({ "unchecked", "static-access", "rawtypes"})
  private ActionForward accionBusquedaSolicitudesRango(AutorizacionesEntidadesSubcontratadasForm forma, UsuarioBasico usuario, PersonaBasica paciente, InstitucionBasica institucionBasica, ActionMapping mapping, HttpServletRequest request) {
		
	  ActionErrors errores = new ActionErrors();
	  errores = mundo.validacionBusquedaporRango(forma.getParametrosBusqueda());		
	 
		if(errores.isEmpty())
		{
			forma.getParametrosBusqueda().put("listaConvenios", (ArrayList)forma.getListadoConvenio());
			forma.getParametrosBusqueda().put("mapaViasIngreso", (HashMap)forma.getViasIngreso());
			forma.getParametrosBusqueda().put("mapaCentrosCosto", (HashMap)forma.getCentrosCosto());
			forma.getParametrosBusqueda().put("listaTipoPaciente",(ArrayList)forma.getTiposPaciente());
			//forma.setSolicitudesPendientes(mundo.obtenerSolicitudesXRango(forma.getParametrosBusqueda()));
			
			ArrayList <DtoSolicitudesSubCuenta> listado=mundo.obtenerSolicitudesXRango(forma.getParametrosBusqueda());
			ArrayList <DtoSolicitudesSubCuenta> tempListado=new ArrayList<DtoSolicitudesSubCuenta>();
			ArrayList<String> codigoSolicitud=new ArrayList<String>();
			
			//Agrupa las solicitudes de Medicamentos e Insumos en una misma solicitud
			for(int i=0;i<listado.size();i++)
			{
				tempListado=new ArrayList<DtoSolicitudesSubCuenta>();
				if(!codigoSolicitud.contains(listado.get(i).getNumeroSolicitud()))
				{
					for(int j=i;j<listado.size();j++)
					{
						if(listado.get(i).getNumeroSolicitud().equals(listado.get(j).getNumeroSolicitud()))
						{
							tempListado.add(listado.get(j));	
						}
					}
					codigoSolicitud.add(listado.get(i).getNumeroSolicitud());
					listado.get(i).setAgrupaListadoAutoriPendEntSub(tempListado);
				}
			}
			
			//Borra los registros vacios
			ArrayList<DtoSolicitudesSubCuenta> listaDefinitiva = new ArrayList<DtoSolicitudesSubCuenta>();
			for (DtoSolicitudesSubCuenta retorno : listado) {
				if (!Utilidades.isEmpty(retorno.getAgrupaListadoAutoriPendEntSub())) {
					listaDefinitiva.add(retorno);
				}
			}
			
			forma.setSolicitudesPendientes(listaDefinitiva);			
			
			return mapping.findForward("resultadoRango");
		}
		else
		{
			saveErrors(request, errores);	
			return mapping.findForward("rango");
		}
	}


	/**
     * Metodo para realizar la insercion de la autorizacion
     * @param con
     * @param forma
     * @param usuario
     * @param paciente
     * @param institucion
     * @param mapping
     * @param request
     * @return
     */
	@SuppressWarnings({ "unchecked", "deprecation", "static-access", "rawtypes"})
	private ActionForward accionGuardarAutorizacion(Connection con,AutorizacionesEntidadesSubcontratadasForm forma,UsuarioBasico usuario,PersonaBasica paciente,InstitucionBasica institucion, ActionMapping mapping,HttpServletRequest request) {
		
		HashMap resultado = new HashMap();
		HashMap parametrosGuardar= new HashMap();		
		parametrosGuardar.put("codEntidad", forma.getEntidadAutorizada());
		parametrosGuardar.put("codConvenio", forma.getSolicitudesPendientes().get(forma.getPosSolicitud()).getCodConvenio());
		parametrosGuardar.put("codSolicitud", ((DtoSolicitudesSubCuenta)forma.getSolicitudesPendientes().get(forma.getPosSolicitud())).getCodigo());
		parametrosGuardar.put("observaciones", forma.getObservaciones());
		parametrosGuardar.put("codInstitucion", usuario.getCodigoInstitucionInt());
		
		parametrosGuardar.put("almacenEjecuta", ((DtoSolicitudesSubCuenta)forma.getSolicitudesPendientes().get(forma.getPosSolicitud())).getCentroCostoEjecuta().getCodigo()); 
		
		if(forma.getSolicitudesPendientes().get(forma.getPosSolicitud()).getServicio().getCodigo().equals(ConstantesBD.codigoNuncaValido+""))
		{
			//parametrosGuardar.put("codArticulo", Utilidades.convertirAEntero(((DtoSolicitudesSubCuenta)forma.getSolicitudesPendientes().get(forma.getPosSolicitud())).getArticulo().getCodigo()));			
			for (Integer arti : forma.getCodigosArticulosServicios()) 
				logger.info("el codigo del Articulo para guardar es: "+arti);			
			
		}else{
			parametrosGuardar.put("codServicio", Utilidades.convertirAEntero(((DtoSolicitudesSubCuenta)forma.getSolicitudesPendientes().get(forma.getPosSolicitud())).getServicio().getCodigo()));			
			logger.info("el codigo del Servicio para guardar es: "+((DtoSolicitudesSubCuenta)forma.getSolicitudesPendientes().get(forma.getPosSolicitud())).getServicio().getCodigo());
		}		
		
		if(forma.getOpcionListado().equals("rango"))
		 {
			parametrosGuardar.put("codPaciente", ((DtoSolicitudesSubCuenta)forma.getSolicitudesPendientes().get(forma.getPosSolicitud())).getCodigoPaciente());
		 }
		 else
		 {
			 if(forma.getOpcionListado().equals("paciente"))
			 {
			   parametrosGuardar.put("codPaciente", paciente.getCodigoPersona());
			   logger.info("el codigo del paciente para guardar es: "+paciente.getCodigoPersona());
			 }
		 }
		parametrosGuardar.put("usuario", usuario.getLoginUsuario());
		parametrosGuardar.put("fechaVencimiento", forma.getFechaVencimiento());
		parametrosGuardar.put("usuarioTodo", usuario);
	
		
		if(forma.getEntidadEjecuta().equals(ConstantesIntegridadDominio.acronimoInterna))
		{
			parametrosGuardar.put("tipo", ConstantesIntegridadDominio.acronimoInterna);
		}
		else{
			if(forma.getEntidadEjecuta().equals(ConstantesIntegridadDominio.acronimoExterna))
			{
				parametrosGuardar.put("tipo", ConstantesIntegridadDominio.acronimoExterna);
			}
		}
		 
		/*----------------------ARTICULOS Y SERVICIOS-----------------------------*/
		logger.info("\n\n\n\n Codigo del paciente: "+paciente.getCodigoPersona()+"\n\n\n\n");
		if(paciente.getCodigoPersona()<0){
			if(Utilidades.convertirAEntero(parametrosGuardar.get("codPaciente").toString())>0){
				paciente.setCodigoPersona(Integer.parseInt(parametrosGuardar.get("codPaciente")+""));
			}
			
		}
		ArrayList <DtoSolicitudesSubCuenta> solicitudes=mundo.obtenerListadoSolicitudes(paciente.getCodigoPersona(), usuario.getCodigoInstitucionInt());
		logger.info("numero de registros: "+forma.getCodigosArticulosServicios().size());
		 		
		int servicio = Utilidades.convertirAEntero(((DtoSolicitudesSubCuenta)forma.getSolicitudesPendientes().get(forma.getPosSolicitud())).getServicio().getCodigo());
		int articulo = Utilidades.convertirAEntero(((DtoSolicitudesSubCuenta)forma.getSolicitudesPendientes().get(forma.getPosSolicitud())).getArticulo().getCodigo());
		String solicitud=((DtoSolicitudesSubCuenta)forma.getSolicitudesPendientes().get(forma.getPosSolicitud())).getCodigo();
		ArrayList<DtoSolicitudesSubCuenta>coberSoli= new ArrayList<DtoSolicitudesSubCuenta>();
		
		for(DtoSolicitudesSubCuenta soli: solicitudes)			
		{
			if(soli.getCodigo().equals(solicitud) && articulo!=ConstantesBD.codigoNuncaValido)				
			{	logger.info("A guardar codigo del articulo: "+soli.getArticulo().getCodigo());
				coberSoli.add(soli);			
			}else if (soli.getCodigo().equals(solicitud) && servicio!=ConstantesBD.codigoNuncaValido)
			{	logger.info("A guardar codigo del servicio: "+soli.getServicio().getCodigo());
				coberSoli.add(soli); 
			}
		}		
		/*-------------------------------------------------------------------------*/
			
		UtilidadBD.iniciarTransaccion(con);
						
		/*Ingresa la autorizacion de los articulos o servicios*/
		resultado= mundo.ingresarAutorizacion(con,parametrosGuardar,coberSoli);	
		
		if(Utilidades.convertirAEntero(resultado.get("codigoAut").toString())>0)
		{
			forma.setNumAutorizacion(resultado.get("codigoAut").toString());	
			forma.setEstadoAutorizacion(resultado.get("estadoAut").toString());
			forma.setUsuarioBasico(usuario.getLoginUsuario());
			forma.setInstitucionBasica(institucion.getRazonSocial());
			forma.setParametrosFiltros("operacionExitosa",ConstantesBD.acronimoSi);
			forma.setParametrosFiltros("mensaje","Autorizacion Generada con Exito");
      	  	  
			UtilidadBD.finalizarTransaccion(con);
			UtilidadBD.closeConnection(con); 
			
			//Lista para imprimir los Articulos y servicios 
			forma.setImprimirListaArticulosServicios(coberSoli);
			return mapping.findForward("resumenAutorizacion");
		}	 
        else
		{
        	UtilidadBD.abortarTransaccion(con);
        	if(resultado.get("consecutivoError")!=null)
        		UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoAutorizacionEntiSub,usuario.getCodigoInstitucionInt(),resultado.get("consecutivoError").toString() ,ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
        	forma.setParametrosFiltros("operacionExitosa",ConstantesBD.acronimoNo);
        	ActionErrors errores = (ActionErrors)resultado.get("error");
        	saveErrors(request, errores);
        	UtilidadBD.closeConnection(con); 
        	return mapping.findForward("gernerarAutorizar");
        }
	}

	
	
	/**
	 * Metodo para imprimir la Autorizacion
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * @param institucionBasica
	 * @param mapping
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unused")
	private ActionForward accionImprimirAutorizacion(Connection con,AutorizacionesEntidadesSubcontratadasForm forma,UsuarioBasico usuario, PersonaBasica paciente, InstitucionBasica institucionBasica, ActionMapping mapping, HttpServletRequest request) 
	{
		 
		request.setAttribute("nombreArchivo",AutorizacionEntidadSubContratadaPdf.pdfResumenAutorizacionEntidadSubContratada(usuario, request, paciente, forma));
    	request.setAttribute("nombreVentana", "Autorizacion Ordenes Medicas ");
    	UtilidadBD.closeConnection(con);
    	return mapping.findForward("abrirPdf");		
		
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * @param institucion
	 * @param mapping
	 * @param request
	 */
	@SuppressWarnings({ "deprecation" })
	private void imprimirAutorizacion(Connection con,AutorizacionesEntidadesSubcontratadasForm forma,UsuarioBasico usuario,PersonaBasica paciente,InstitucionBasica institucion, ActionMapping mapping,HttpServletRequest request) throws IPSException
	{		
		int servicio = Utilidades.convertirAEntero(((DtoSolicitudesSubCuenta)forma.getSolicitudesPendientes().get(forma.getPosSolicitud())).getServicio().getCodigo());
		int articulo = Utilidades.convertirAEntero(((DtoSolicitudesSubCuenta)forma.getSolicitudesPendientes().get(forma.getPosSolicitud())).getArticulo().getCodigo());
					
		if(servicio!=ConstantesBD.codigoNuncaValido)
		{
			imprimirAutorizacionesEntidadesSubServicio(con, forma, forma.getImprimirListaArticulosServicios(), paciente, usuario, request);
		}
		else if (articulo!=ConstantesBD.codigoNuncaValido)
		{   
			imprimirAutorizacionesEntidadesSubArticulos(con,forma, forma.getImprimirListaArticulosServicios(), paciente, usuario, request);
		}
		
	}
	
	private ActionForward accionCambiarTipoEntidadEjecuta(Connection con, AutorizacionesEntidadesSubcontratadasForm forma, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request) throws IPSException
	{
		
		if(forma.getEntidadEjecuta().equals(ConstantesIntegridadDominio.acronimoInterna))
		{
			//Se valida si el parámetro general entidad subcontratada para centros costo internos esta definido
			String descripcionEntidad=ValoresPorDefecto.getEntidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada(usuario.getCodigoInstitucionInt());
			if(UtilidadTexto.isEmpty(descripcionEntidad))
			{
				forma.setEntidadAutorizada("");
				forma.setNomEntidadAutorizada("");
				forma.setDirEntidadAutorizada("");
				forma.setTelEntidadAutorizada("");
				ActionErrors errores =new ActionErrors();
				errores.add("descripcion",new ActionMessage("errors.notEspecific","Falta definir el parámetro 'Entidad Subcontratada Para Centros de Costo Internos'. Por favor verifique."));
				saveErrors(request, errores); 
				
			}
			else
			{
				String fechaAutorizacion = UtilidadFecha.getFechaActual();
				String[] entidad=descripcionEntidad.split("-");
				EntidadesSubcontratadasDelegate entiDao=new EntidadesSubcontratadasDelegate();
				EntidadesSubcontratadas entidadSub=entiDao.findById(Long.parseLong(entidad[0].trim()));
				
				//Se valida si la entidad subcontratada del parámetro tiene contrato vigente
				CargosEntidadesSubcontratadasDao cargosDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosEntidadesSubcontratadasDao(); 
				DtoContratoEntidadSub contratoEntidadSub = cargosDao.obtenerContratoVigenteEntidadSubcontratada(con,(entidadSub.getCodigoPk()+""),fechaAutorizacion);			
				
				if(contratoEntidadSub != null)
				{
					forma.setEntidadAutorizada(entidadSub.getCodigoPk()+"");
					forma.setNomEntidadAutorizada(entidadSub.getRazonSocial());
					forma.setDirEntidadAutorizada((entidadSub.getDireccion()!=null?entidadSub.getDireccion():""));
					forma.setTelEntidadAutorizada((entidadSub.getTelefono()!=null?entidadSub.getTelefono():""));
					
				}
				
			}
		}
		else if(forma.getEntidadEjecuta().equals(ConstantesIntegridadDominio.acronimoExterna)){
			return accionAutorizar(con, forma, usuario, mapping, request);
		}
		
		return mapping.findForward("gernerarAutorizar");
	}
	
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @return
	 * @throws IPSException 
	 */
	@SuppressWarnings({ "deprecation", "static-access" })
	private ActionForward accionAutorizar(Connection con, AutorizacionesEntidadesSubcontratadasForm forma,UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request) throws IPSException 
	{
		
		 CargosEntidadesSubcontratadas cargosEntidadesSubCont = new  CargosEntidadesSubcontratadas();
		 forma.resetDatosComunesOrden();
		 String nomCentroCosto=((DtoSolicitudesSubCuenta)forma.getSolicitudesPendientes().get(forma.getPosSolicitud())).getCentroCostoEjecuta().getNombre();
		 int centroCosto = ((DtoSolicitudesSubCuenta)forma.getSolicitudesPendientes().get(forma.getPosSolicitud())).getCentroCostoEjecuta().getCodigo();
		 String tipoPaciente=((DtoSolicitudesSubCuenta)forma.getSolicitudesPendientes().get(forma.getPosSolicitud())).getCodTipoPaciente();
		 int viaIngreso=((DtoSolicitudesSubCuenta)forma.getSolicitudesPendientes().get(forma.getPosSolicitud())).getCodViaIngreso();
		 int naturalezaPacinte= ((DtoSolicitudesSubCuenta)forma.getSolicitudesPendientes().get(forma.getPosSolicitud())).getCodNaturalezaPaciente();
		 String nombrePaciente = ((DtoSolicitudesSubCuenta)forma.getSolicitudesPendientes().get(forma.getPosSolicitud())).getNombrePaciente();
		 String tipoIdpaciente = ((DtoSolicitudesSubCuenta)forma.getSolicitudesPendientes().get(forma.getPosSolicitud())).getTipoIdPaciente();
		 String numeroIdPaciente = ((DtoSolicitudesSubCuenta)forma.getSolicitudesPendientes().get(forma.getPosSolicitud())).getNumeroIdPaciente();

		 /* Con estas variables se sabe si hay solicitudes de articulos o 
		  * servicios cuando alguna de las 2 obtenga un valor != -1*/  
		 int servicio = Utilidades.convertirAEntero(((DtoSolicitudesSubCuenta)forma.getSolicitudesPendientes().get(forma.getPosSolicitud())).getServicio().getCodigo());		 
		 int articulo = Utilidades.convertirAEntero(((DtoSolicitudesSubCuenta)forma.getSolicitudesPendientes().get(forma.getPosSolicitud())).getArticulo().getCodigo());
		 String nomServicio="";
		 ArrayList<String> nomArticulo=new ArrayList<String>();
		 boolean noTieneCOberArticu=false;
		 
		 /*-------Se cargan las solicitudes pendientes de medicamentos o Insumos para evaluar cobertura-----------*/
			String solicitud=((DtoSolicitudesSubCuenta)forma.getSolicitudesPendientes().get(forma.getPosSolicitud())).getCodigo();
			int codigoPaciente=forma.getSolicitudesPendientes().get(forma.getPosSolicitud()).getCodigoPaciente();
			int codigoIntituci=usuario.getCodigoInstitucionInt();
					 
		 	logger.info("el codigo de la solicitud: "+((DtoSolicitudesSubCuenta)forma.getSolicitudesPendientes().get(forma.getPosSolicitud())).getCodigo());
			logger.info("el codigo del aRTICULO para guardar es: "+((DtoSolicitudesSubCuenta)forma.getSolicitudesPendientes().get(forma.getPosSolicitud())).getArticulo().getCodigo());
			logger.info("el codigo del sERVICIO para guardar es: "+((DtoSolicitudesSubCuenta)forma.getSolicitudesPendientes().get(forma.getPosSolicitud())).getServicio().getCodigo()+"\n\n\n\n\n");
		
			ArrayList<DtoSolicitudesSubCuenta> solicitudes= mundo.obtenerListadoSolicitudes(codigoPaciente,codigoIntituci);
			ArrayList<Integer> coberSoli  = new ArrayList<Integer>();
						
			for(DtoSolicitudesSubCuenta soli: solicitudes)			
			{
				if(soli.getCodigo().equals(solicitud) && articulo>0)				
				{	
					coberSoli.add(Utilidades.convertirAEntero(soli.getArticulo().getCodigo()));
					nomArticulo.add(soli.getArticulo().getNombre());
				}else if (soli.getCodigo().equals(solicitud) && servicio>0)
				{	
					coberSoli.add(Utilidades.convertirAEntero(soli.getServicio().getCodigo()));
					nomArticulo.add(soli.getArticulo().getNombre());
				}
			}			
			
			if(articulo!=ConstantesBD.codigoNuncaValido)
			{	
				/*Consulta las entidadesSub X Centro de costo autorizadas con cobertura y contrato vigente				 			 					
				 */
				forma.setEntidadesAutorizadas(cargosEntidadesSubCont.obtenerEntidadesSubcontratadasXCentroCostoAutorizacionArticulo(con,centroCosto,coberSoli, 
						UtilidadFecha.getFechaActual(),usuario.getCodigoInstitucionInt(),viaIngreso,tipoPaciente,naturalezaPacinte,forma.getSolicitudFiltro()));
				
				ArrayList<DtoEntidadSubcontratada> tempListado=new ArrayList<DtoEntidadSubcontratada>();
				ArrayList<String> entidadSub=new ArrayList<String>();				
				tempListado=new ArrayList<DtoEntidadSubcontratada>();
								
				if(forma.getEntidadesAutorizadas().isEmpty())
				{	noTieneCOberArticu=true;				
				}
				else
				{    /*Si las entidades autorizadas para los articulos son iguales, agrupa y solo muestra una.
					 */
					logger.info("despues-> tamaño de getentidadesAutorizadas: "+forma.getEntidadesAutorizadas().size());
					for(int i=0;i<forma.getEntidadesAutorizadas().size();i++)							
					{							
						if(forma.getEntidadesAutorizadas().get(i)!=null)
						{
							for(int j=i;j<forma.getEntidadesAutorizadas().size();j++)
							{	if(forma.getEntidadesAutorizadas().get(j)!=null)
								{
									if(forma.getEntidadesAutorizadas().get(i).getRazonSocial().equals(forma.getEntidadesAutorizadas().get(j).getRazonSocial()))
									{
										if(!entidadSub.contains(forma.getEntidadesAutorizadas().get(j).getRazonSocial()))
										{
											tempListado.add(forma.getEntidadesAutorizadas().get(j));
											entidadSub.add(forma.getEntidadesAutorizadas().get(j).getRazonSocial());																	
										}
									}
								}
							}
							forma.getEntidadesAutorizadas().get(i).setAgrupaListadoEntidSubCobertu(tempListado);
						}
					}
					forma.setAgrupaListadoEntidadesSubAutoriz(tempListado);				
				}
			}else{
								
				//servicio = Utilidades.convertirAEntero(((DtoSolicitudesSubCuenta)forma.getSolicitudesPendientes().get(forma.getPosSolicitud())).getServicio().getCodigo());
				nomServicio=((DtoSolicitudesSubCuenta)forma.getSolicitudesPendientes().get(forma.getPosSolicitud())).getServicio().getNombre();				
				forma.setNivelServicio(mundo.nivelServicio(servicio));
				for (Integer codServ: coberSoli)
					forma.setEntidadesAutorizadas(cargosEntidadesSubCont.obtenerEntidadesSubcontratadasXCentroCostoAutorizacion(con, centroCosto, codServ, 
							UtilidadFecha.getFechaActual(), usuario.getCodigoInstitucionInt(), viaIngreso, tipoPaciente, naturalezaPacinte, forma.getSolicitudFiltro()));
			}		 
			/*--------------------------------------------------------------------------------------------------------------*/
		 	
		 forma.setEntidadEjecutaSolicitud(((DtoSolicitudesSubCuenta)forma.getSolicitudesPendientes().get(forma.getPosSolicitud())).getTipoEntidadEjecuta());
		 if(forma.getEntidadEjecutaSolicitud().equals(ConstantesIntegridadDominio.acronimoExterna))
		 {
			 forma.setEntidadEjecuta(ConstantesIntegridadDominio.acronimoExterna);
		 }
		 forma.setCentroAtencionIngreso(((DtoSolicitudesSubCuenta)forma.getSolicitudesPendientes().get(forma.getPosSolicitud())).getCentroAtencionIngreso());   
		 forma.setNombrePacinte(nombrePaciente);
		 forma.setTipoIdPacinte(tipoIdpaciente +"  "+numeroIdPaciente);
		 logger.info("Antes llamar entidades subcontratadas =================================");
		 //forma.setEntidadesAutorizadas(cargosEntidadesSubCont.obtenerEntidadesSubcontratadasXCentroCostoAutorizacion(con, centroCosto, servicio, UtilidadFecha.getFechaActual(), usuario.getCodigoInstitucionInt(), viaIngreso, tipoPaciente, naturalezaPacinte, forma.getSolicitudFiltro()));
		 logger.info("Despues llamar entidades subcontratadas =================================");
		 //forma.setNivelServicio(mundo.nivelServicio(servicio));
			
		 
		 if(!Utilidades.isEmpty(forma.getAgrupaListadoEntidadesSubAutoriz()) ||  !Utilidades.isEmpty(forma.getEntidadesAutorizadas()))		 
		 {
			 if(forma.getAgrupaListadoEntidadesSubAutoriz().size()==1 ||  forma.getEntidadesAutorizadas().size()==1)
			 {
				if(articulo!=ConstantesBD.codigoNuncaValido)
				{	forma.setEntidadAutorizada(((DtoEntidadSubcontratada)forma.getAgrupaListadoEntidadesSubAutoriz().get(0)).getConsecutivo());
					forma.setNomEntidadAutorizada(((DtoEntidadSubcontratada)forma.getAgrupaListadoEntidadesSubAutoriz().get(0)).getRazonSocial());
					forma.setDirEntidadAutorizada(((DtoEntidadSubcontratada)forma.getAgrupaListadoEntidadesSubAutoriz().get(0)).getDireccion());
					forma.setTelEntidadAutorizada(((DtoEntidadSubcontratada)forma.getAgrupaListadoEntidadesSubAutoriz().get(0)).getTelefono());
				}else{
					forma.setEntidadAutorizada(((DtoEntidadSubcontratada)forma.getEntidadesAutorizadas().get(0)).getConsecutivo());
					forma.setNomEntidadAutorizada(((DtoEntidadSubcontratada)forma.getEntidadesAutorizadas().get(0)).getRazonSocial());
					forma.setDirEntidadAutorizada(((DtoEntidadSubcontratada)forma.getEntidadesAutorizadas().get(0)).getDireccion());
					forma.setTelEntidadAutorizada(((DtoEntidadSubcontratada)forma.getEntidadesAutorizadas().get(0)).getTelefono());
				}
				/*llena la lista para cargar los ARTICULOS que se ingresaran en la autorizacion*/
				forma.setCodigosArticulosServicios(coberSoli);				
			 }else
			 {
				logger.info("Prioridades \n  ============= UNO: "+forma.getEntidadesAutorizadas().get(0).getPrioridad()+" \n DOS: "+forma.getEntidadesAutorizadas().get(1).getPrioridad());
				if(!((DtoEntidadSubcontratada)forma.getEntidadesAutorizadas().get(0)).getPrioridad().equals(((DtoEntidadSubcontratada)forma.getEntidadesAutorizadas().get(1)).getPrioridad()))
				{
					if(articulo!=ConstantesBD.codigoNuncaValido)
					{
						forma.setEntidadAutorizada(((DtoEntidadSubcontratada)forma.getAgrupaListadoEntidadesSubAutoriz().get(0)).getConsecutivo());
						forma.setNomEntidadAutorizada(((DtoEntidadSubcontratada)forma.getAgrupaListadoEntidadesSubAutoriz().get(0)).getRazonSocial());
						forma.setDirEntidadAutorizada(((DtoEntidadSubcontratada)forma.getAgrupaListadoEntidadesSubAutoriz().get(0)).getDireccion());
						forma.setTelEntidadAutorizada(((DtoEntidadSubcontratada)forma.getAgrupaListadoEntidadesSubAutoriz().get(0)).getTelefono());
						logger.info("  Entro al si  \n Numero entidades subcontratadas ============="+forma.getAgrupaListadoEntidadesSubAutoriz().size());
					}else
					{
						forma.setEntidadAutorizada(((DtoEntidadSubcontratada)forma.getEntidadesAutorizadas().get(0)).getConsecutivo());
						forma.setNomEntidadAutorizada(((DtoEntidadSubcontratada)forma.getEntidadesAutorizadas().get(0)).getRazonSocial());
						forma.setDirEntidadAutorizada(((DtoEntidadSubcontratada)forma.getEntidadesAutorizadas().get(0)).getDireccion());
						forma.setTelEntidadAutorizada(((DtoEntidadSubcontratada)forma.getEntidadesAutorizadas().get(0)).getTelefono());
						logger.info("  Entro al si  \n Numero entidades subcontratadas ============="+forma.getEntidadesAutorizadas().size());
					}
					logger.info("Entro al SI PRIORIDADES diferentes");
						 
					/*llena la lista para cargar los ARTICULOS que se ingresaran en la autorizacion*/
					forma.setCodigosArticulosServicios(coberSoli);						 	
				}				 
			 }	    
		   }else
		   {			   
			   ActionErrors errores =new ActionErrors();
			   if(servicio!=ConstantesBD.codigoNuncaValido)
			   {
				   errores.add("descripcion",new ActionMessage("errors.notEspecific","No existen Entidades Subcontratadas con contrato vigente que cubra el Servicio "+nomServicio+" para el Centro de Costo " +nomCentroCosto+" "));			   
			   }
			   else if(noTieneCOberArticu==true)
			   {	for (int i=0;i<nomArticulo.size();i++)
				   		errores.add("descripcion",new ActionMessage("errors.notEspecific","No existen Entidades Subcontratadas con contrato vigente que cubra el Articulo "+nomArticulo.get(i)+" para el Centro de Costo " +nomCentroCosto+" "));			   		
			   }				
			   saveErrors(request, errores); 
		   }		 
		 
		 UtilidadBD.closeConnection(con); 
		 return mapping.findForward("gernerarAutorizar");
	}
	

	/**
	   * 
	   * Metodo que parametriza los atributos de la busqueda
	   * @param con
	   * @param forma
	   * @param usuario
	   * @param paciente
	   * @param institucionBasica
	   * @param mapping
	   * @param request
	   * @return
	   */
	    @SuppressWarnings({ "static-access", "rawtypes"})
		private ActionForward accionBuscarSolicitudesRango(Connection con,AutorizacionesEntidadesSubcontratadasForm forma, UsuarioBasico usuario, PersonaBasica paciente, InstitucionBasica institucionBasica, ActionMapping mapping,HttpServletRequest request) {
			
	    	forma.setParametrosBusqueda(mundo.inicializarParametrosBusquedaRango(usuario.getCodigoCentroAtencion()));
	    	forma.setNomcentroAtencionBusquedaRango(usuario.getCentroAtencion());
	    	forma.setListadoCentroAtencion(Utilidades.obtenerCentrosAtencion(usuario.getCodigoInstitucionInt()));
	    	forma.setListadoConvenio(Utilidades.obtenerConvenios(con, "", "", false, "", true));
	    	forma.setCentrosCosto(mundo.centroCostoRango(usuario.getCodigoCentroAtencion()));
	    	forma.setViasIngreso((HashMap)Utilidades.obtenerViasIngreso(con, false));
	    	UtilidadBD.closeConnection(con); 
	    	return mapping.findForward("rango");
		}
	
   
	/**
	 * 
	 * @param forma
	 * @param paciente
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	@SuppressWarnings("static-access")
	private ActionForward listadoSolicitudes(AutorizacionesEntidadesSubcontratadasForm forma,PersonaBasica paciente, UsuarioBasico usuario,HttpServletRequest request, ActionMapping mapping) {
		
		//forma.setSolicitudesPendientes(mundo.obtenerListadoSolicitudes(paciente.getCodigoPersona(),usuario.getCodigoInstitucionInt()));		
		ArrayList <DtoSolicitudesSubCuenta> listado=mundo.obtenerListadoSolicitudes(paciente.getCodigoPersona(), usuario.getCodigoInstitucionInt());
		ArrayList <DtoSolicitudesSubCuenta> tempListado=new ArrayList<DtoSolicitudesSubCuenta>();
		ArrayList<String> codigoSolicitud=new ArrayList<String>();
		
		//Agrupa las solicitudes de Medicamentos e Insumos en una misma solicitud
		for(int i=0;i<listado.size();i++)
		{
			tempListado=new ArrayList<DtoSolicitudesSubCuenta>();
			if(!codigoSolicitud.contains(listado.get(i).getNumeroSolicitud()))
			{
				for(int j=i;j<listado.size();j++)
				{
					if(listado.get(i).getNumeroSolicitud().equals(listado.get(j).getNumeroSolicitud()))
					{
						tempListado.add(listado.get(j));	
					}									
				}	
				codigoSolicitud.add(listado.get(i).getNumeroSolicitud());
				listado.get(i).setAgrupaListadoAutoriPendEntSub(tempListado);
			}
		}
		
		//Borra los registros vacios
		ArrayList<DtoSolicitudesSubCuenta> listaDefinitiva = new ArrayList<DtoSolicitudesSubCuenta>();
		for (DtoSolicitudesSubCuenta retorno : listado) {
			if (!Utilidades.isEmpty(retorno.getAgrupaListadoAutoriPendEntSub())) {
				listaDefinitiva.add(retorno);
			}
		}		
		
		forma.setSolicitudesPendientes(listaDefinitiva);
		
		return mapping.findForward("paciente");
	}

	
	
	/**
	 * Validacion de Paciente Cargado en Sesion
	 * @param con
	 * @param forma
	 * @param paciente
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	 private ActionForward accionValidarPaciente(Connection con,AutorizacionesEntidadesSubcontratadasForm forma, PersonaBasica paciente,UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) 
	{
			if(paciente==null || paciente.getCodigoPersona()<=0)
			{
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.paciente.noCargado", "errors.paciente.noCargado", true);
			}
	        
			return null;
    }
	 
	 
	 
	 
	 /**
	  * Metodo para ordenar por columna la busqueda por Paciente
	  * @param con
	  * @param forma
	  * @param mapping
	  * @return
	  */
	 @SuppressWarnings("static-access")
	private ActionForward ordenarXColumna(AutorizacionesEntidadesSubcontratadasForm forma,ActionMapping mapping )
	 {
		  ArrayList<DtoSolicitudesSubCuenta> array= new ArrayList<DtoSolicitudesSubCuenta>();
		  logger.info ("\n\n Tamaño Lista Solicitudes  >> "+forma.getSolicitudesPendientes().size());
		  array=mundo.ordenarColumna(forma.getSolicitudesPendientes(),forma.getUltimaPropiedad(), forma.getPropiedadOrdenar());
		  forma.setUltimaPropiedad(forma.getPropiedadOrdenar());
		  forma.resetArraySolicitudesPendientes();
		  forma.setSolicitudesPendientes(array);
		  if(forma.getOpcionListado().equals("paciente"))
		  {
		  return mapping.findForward("paciente");
		  }
		  else{
			  if(forma.getOpcionListado().equals("rango"))
			  {
			  return mapping.findForward("resultadoRango");
			  }
		  }
		  
		  return null;
	 }
	
	 
	 
	 /**
	  * Metodo para filtrar los centros de costo asociaciados a un acentro de Atencion
	  * @param con
	  * @param form
	  * @param response
	  * @return
	  */
	 @SuppressWarnings("static-access")
	 private ActionForward accionFiltrarCentrosCosto(Connection con,AutorizacionesEntidadesSubcontratadasForm form,HttpServletResponse response) 
		{
			logger.info(" >>>  ENTRO A FILTRAR CENTROS COSTO   ");
			String resultado = "<respuesta>" +
				"<infoid>" +
					"<sufijo>ajaxBusquedaTipoSelect</sufijo>" +
					"<id-select>centrocosto</id-select>" +
					"<id-arreglo>centroscosto</id-arreglo>" +
				"</infoid>" ;
			
			if(!form.getCentroAtencionFiltro().equals(""))
			{
				
					form.setCentrosCosto(mundo.centroCostoRango(Utilidades.convertirAEntero(form.getCentroAtencionFiltro().toString())));
				
		    }
				
			for(int i=0; i< Utilidades.convertirAEntero(form.getCentrosCosto().get("numRegistros")+"");i++)
			{
					resultado += "<centroscosto>";
					resultado += "<codigo>"+form.getCentrosCosto().get("codigo_"+i)+"</codigo>";
					resultado += "<descripcion>"+form.getCentrosCosto().get("nombrecentrocosto_"+i)+"</descripcion>";
				    resultado += "</centroscosto>";	
				
			}
			
			resultado += "</respuesta>";
			
			UtilidadBD.closeConnection(con);
			//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
			try
			{
				response.reset();
				response.resetBuffer();
				
				response.setContentType("text/xml");
				response.setHeader("Cache-Control", "no-cache");
				response.getWriter().flush();			
		        response.getWriter().write(resultado);
		        response.getWriter().close();
		     
			}
			catch(IOException e)
			{
				logger.error("Error al enviar respuesta AJAX en accionFiltrarCentrosCosto: "+e);
			}
			return null;
		}
	 
	 /**
	  * Metodo para filtrar los tipos de paciente asociados a un via de ingreso
	  * @param con
	  * @param form
	  * @param response
	  * @return
	  */
	 @SuppressWarnings("unchecked")
	private ActionForward accionFiltrarTipoPaciente(Connection con,AutorizacionesEntidadesSubcontratadasForm form,HttpServletResponse response) 
		{
			logger.info(" >>>  ENTRO A FILTRAR TIPO PACIENTE   ");
			String resultado = "<respuesta>" +
				"<infoid>" +
					"<sufijo>ajaxBusquedaTipoSelect</sufijo>" +
					"<id-select>tipoPaciente</id-select>" +
					"<id-arreglo>tipopaciente</id-arreglo>" +
				"</infoid>" ;
			
			if(!form.getViaIngresoFiltro().equals(""))
			{				
				form.setTiposPaciente(Utilidades.obtenerTiposPacientePorViaIngreso(con, form.getViaIngresoFiltro().toString()));
			}
				
			for(int i=0; i< form.getTiposPaciente().size();i++)
			{
					resultado += "<tipopaciente>";
					resultado += "<codigo>"+form.getTiposPaciente().get(i).get("tipopaciente")+"</codigo>";
					resultado += "<descripcion>"+form.getTiposPaciente().get(i).get("nomtipopaciente")+"</descripcion>";
				    resultado += "</tipopaciente>";	
				
			}
			
			resultado += "</respuesta>";
			
			UtilidadBD.closeConnection(con);
			//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
			try
			{
				response.reset();
				response.resetBuffer();
				
				response.setContentType("text/xml");
				response.setHeader("Cache-Control", "no-cache");
				response.getWriter().flush();			
		        response.getWriter().write(resultado);
		        response.getWriter().close();
		     
			}
			catch(IOException e)
			{
				logger.error("Error al enviar respuesta AJAX en accionFiltrarCentrosCosto: "+e);
			}
			return null;
		}
	
	 
	 
	 /**
	  * 
	  * @param con
	  * @param forma
	  * @param response
	  * @return
	  */
	 private ActionForward accionFiltrarDireccionTelefonoEnt(Connection con,AutorizacionesEntidadesSubcontratadasForm forma,HttpServletResponse response) {
				
		String telefono = "",direccion="";
		
		for(int i=0;i<forma.getEntidadesAutorizadas().size();i++)
		{
			if(forma.getEntidadesAutorizadas().get(i).getConsecutivo().equals(forma.getCodEntidadFiltro()))
			{
				telefono = forma.getEntidadesAutorizadas().get(i).getTelefono();
				direccion=forma.getEntidadesAutorizadas().get(i).getDireccion();
				forma.setTelEntidadAutorizada(telefono);
				forma.setDirEntidadAutorizada(direccion);
				i=forma.getEntidadesAutorizadas().size();
			}
		} 
	
		 String resultado = "<respuesta>" +
			"<infoid>" +
				"<sufijo>ajaxBusquedaTipoInnerHtml</sufijo>" +
				"<id-div>dirEntidad</id-div>" + 
				"<contenido>"+direccion+"</contenido>" + 
				"<id-div>telEntidad</id-div>" + 
				"<contenido>"+telefono+"</contenido>" +
			"</infoid>"+
			"</respuesta>";
	
		UtilidadBD.closeConnection(con);
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
	        response.getWriter().write(resultado);
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionFiltroDirTelEntidad: "+e);
		}
		return null;			
			
		}
	  
	 
	 	/**IMPRIMIR SERVICIOS
	 	 * 
	 	 * @param con
	 	 * @param forma
	 	 * @param listaServicios
	 	 * @param paciente
	 	 * @param usuarioSesion
	 	 * @param request
	 	 */
		@SuppressWarnings("deprecation")
		private void imprimirAutorizacionesEntidadesSubServicio (Connection con,AutorizacionesEntidadesSubcontratadasForm forma,
				ArrayList<DtoSolicitudesSubCuenta> listaServicios,
	    		PersonaBasica paciente,UsuarioBasico usuarioSesion,HttpServletRequest request) throws IPSException
		{
			IConvenioServicio convenioServicio = FacturacionServicioFabrica.crearConvenioServicio();
	        	        
	        if(listaServicios!=null && listaServicios.size()>0)
	        {   		       
	        	InfoResponsableCobertura infoResponsableCobertura= 
	        		Cobertura.validacionCoberturaServicio(con, paciente.getCodigoIngreso()+"",
	        				paciente.getCodigoUltimaViaIngreso(), paciente.getCodigoTipoPaciente(), 
	        				Utilidades.convertirAEntero(listaServicios.get(0).getServicio().getCodigo()), 
	        				usuarioSesion.getCodigoInstitucionInt(), false, "");
	        		        		
	        	if(infoResponsableCobertura!=null)
	        	{        	
	        		InfoDatosInt infoConvenio =  infoResponsableCobertura.getDtoSubCuenta().getConvenio();
	        		Convenios convenioResponsable = convenioServicio.findById(infoConvenio.getCodigo());
	     		
	        		if(convenioResponsable != null &&  
	     				((convenioResponsable.getTiposContrato().getCodigo())==ConstantesBD.codigoTipoContratoEvento))     				
	        		{     				
	        			CentrosCosto centroCostoSolicitante = new CentrosCosto();
	     				centroCostoSolicitante.setCodigo(paciente.getCodigoArea());
	     				
     					if(!listaServicios.equals(null))
     					{	 			     			
     						String tipoFormatoImpresion = ValoresPorDefecto.getFormatoImpresionAutorEntidadSub(
     								usuarioSesion.getCodigoInstitucionInt());
	 			     		
     						if(!UtilidadTexto.isEmpty(tipoFormatoImpresion))
     						{		 			     			
     							if(tipoFormatoImpresion.equals(
     									ConstantesIntegridadDominio.acronimoFormatoImpresionEstandar))
     							{
     								generarReporteAutorizacionFormatoEstandarServicios(paciente, 
	 			     		    		forma, usuarioSesion, 
	 			     		    		request,convenioResponsable,
		 			     		    	listaServicios); 	
		 			     				
	     						}else if(tipoFormatoImpresion.equals(
	     								ConstantesIntegridadDominio.acronimoFormatoImpresionVersalles)){
	 			     						 
	     							generarReporteAutorizacionFormatoVersallesServicios(con,paciente, 
	 			     			    	forma, usuarioSesion,request,
	 			     			    	convenioResponsable,	 			     			    
	 			     			    	listaServicios);
	     						}	 			     				
	     					}	     					
	     				}     				
	        		}
	        	}	        	
	        }
		}
		
		
		/**IMPRIMIR ARTICULOS
		 * 		
		 * @param con
		 * @param forma
		 * @param listaArticulos
		 * @param paciente
		 * @param usuarioSesion
		 * @param request
		 */
		@SuppressWarnings("deprecation")
		private void imprimirAutorizacionesEntidadesSubArticulos(Connection con, AutorizacionesEntidadesSubcontratadasForm forma,
				ArrayList<DtoSolicitudesSubCuenta> listaArticulos,PersonaBasica paciente,UsuarioBasico usuarioSesion,
				HttpServletRequest request) throws IPSException 
		{			
			
				ActionErrors errores = new ActionErrors();				
				Convenios convenioResponsable = null;
				IConvenioServicio convenioServicio = FacturacionServicioFabrica.crearConvenioServicio();
				
				if(listaArticulos!=null && listaArticulos.size()>0)
				{					
					CentrosCosto centroCostoSolicitante = new CentrosCosto();
	            	centroCostoSolicitante.setCodigo(paciente.getCodigoArea());
										
					for(DtoSolicitudesSubCuenta articulo: listaArticulos)
					{
						InfoResponsableCobertura infoResponsableCobertura = new InfoResponsableCobertura(); 
						infoResponsableCobertura = Cobertura.validacionCoberturaArticulo(con, paciente.getCodigoIngreso()+"", 
								paciente.getCodigoUltimaViaIngreso(), paciente.getCodigoTipoPaciente(),
								Utilidades.convertirAEntero(articulo.getArticulo().getCodigo()),
								usuarioSesion.getCodigoInstitucionInt(), false);
												
						if(infoResponsableCobertura!=null)
						{						
							InfoDatosInt infoConvenio = infoResponsableCobertura.getDtoSubCuenta().getConvenio();
							convenioResponsable = convenioServicio.findById(infoConvenio.getCodigo());							
						}
					}
								     		    						     	
			     	if(!listaArticulos.equals(null))
				    {				     		
				    	String tipoFormatoImpresion = ValoresPorDefecto.getFormatoImpresionAutorEntidadSub(
	 			    			usuarioSesion.getCodigoInstitucionInt()); 
				     			
	 			    			
	 			    	if(!UtilidadTexto.isEmpty(tipoFormatoImpresion))
	 			    	{		 			     		
		 			   		if(tipoFormatoImpresion.equals(ConstantesIntegridadDominio.acronimoFormatoImpresionEstandar))
		 			   		{		 			     				
		 			    		generarReporteAutorizacionFormatoEstandarArticulos(con,paciente,
		 			    				forma, usuarioSesion, request,
		 			    				convenioResponsable,		 			    				
		 			    				listaArticulos
		 			    				); 
		 		 			     			
		 			     	}else if(tipoFormatoImpresion.equals(ConstantesIntegridadDominio.acronimoFormatoImpresionVersalles))
		 			     	{	 			     				
		 			     		generarReporteAutorizacionFormatoVersallesArticulos(forma,con,paciente,
		 			     				usuarioSesion, request,
		 			     				convenioResponsable,		 			     				
		 			     				listaArticulos
										);     					
	 			     		}
		 			    }				     	
					}				
					if(errores!=null && !errores.isEmpty()){
	    	     		saveErrors(request, errores);
	    	     	}					
				}
		}
		
			
		
		/**GENERAR REPORTE SERVICIOS ESTANDAR
		 * 
		 * @param paciente
		 * @param forma
		 * @param usuarioSesion
		 * @param numeroSolicitud
		 * @param request
		 * @param convenioResponsable
		 * @param dtoProcesoAutorizacion
		 * @param dtoValidacionNivelAutorizacion
		 * @param listaServicios
		 */
	    @SuppressWarnings("deprecation")
		private void generarReporteAutorizacionFormatoEstandarServicios(PersonaBasica paciente, 
	    		AutorizacionesEntidadesSubcontratadasForm forma, UsuarioBasico usuarioSesion,
	    		HttpServletRequest request,Convenios convenioResponsable,	    		
	    		ArrayList<DtoSolicitudesSubCuenta> listaServicios)
	    {
	    	
	    	String nombreReporte="AUTORIZACION ORDENES MEDICAS";
			String nombreArchivo ="";
			DTOReporteEstandarAutorizacionServiciosArticulos dtoReporte = 
				new DTOReporteEstandarAutorizacionServiciosArticulos();
			
			GeneradorReporteFormatoEstandarAutorservicio generadorReporte = 
				new GeneradorReporteFormatoEstandarAutorservicio(dtoReporte);
				
			ArrayList<String> listaNombresReportes = new ArrayList<String>();
				
	    	InstitucionBasica institucion = (
		        		InstitucionBasica)request.getSession().getAttribute("institucionBasica");
						 			     			
			String nombrePaciente = paciente.getPrimerNombre() + " " + 
				paciente.getSegundoNombre() + " " + paciente.getPrimerApellido()+
				" " + paciente.getSegundoApellido();
				 			     			
			String reporteMediaCarta = ValoresPorDefecto.getImpresionMediaCarta(
					usuarioSesion.getCodigoInstitucionInt());
			
			String infoParametroGeneral = ValoresPorDefecto.getEncFormatoImpresionAutorEntidadSub(
					usuarioSesion.getCodigoInstitucionInt());
			
			String infoPiePagina=ValoresPorDefecto.getPiePagFormatoImpresionAutorEntidadSub(
					usuarioSesion.getCodigoInstitucionInt());
			
			if(UtilidadTexto.isEmpty(reporteMediaCarta)){
				reporteMediaCarta=ConstantesBD.acronimoNo;
			}
				
			dtoReporte.setNombrePaciente(nombrePaciente);
			dtoReporte.setTipoDocPaciente(paciente.getCodigoTipoIdentificacionPersona());
			dtoReporte.setNumeroDocPaciente(paciente.getNumeroIdentificacionPersona());
			dtoReporte.setTipoContrato(convenioResponsable.getTiposContrato().getNombre());			
			
			dtoReporte.setEntidadSubcontratada(forma.getNomEntidadAutorizada());
			dtoReporte.setDireccionEntidadSub(forma.getDirEntidadAutorizada());
			dtoReporte.setTelefonoEntidadSub(forma.getTelEntidadAutorizada());
			
			dtoReporte.setFormatoMediaCarta(reporteMediaCarta);
			dtoReporte.setInfoParametroGeneral(infoParametroGeneral);
			dtoReporte.setInfoPiePagina(infoPiePagina);
			dtoReporte.setEntidadAutoriza(usuarioSesion.getInstitucion());
			dtoReporte.setUsuarioAutoriza(usuarioSesion.getLoginUsuario());
			dtoReporte.setRutaLogo(institucion.getLogoJsp());
			dtoReporte.setUbicacionLogo(institucion.getUbicacionLogo());
			
			//Se obtiene el ultimo diagnostico del paciente
			DtoDiagnostico dtoDiagnostico = Utilidades.getDiagnosticoPacienteCuenta(paciente.getCodigoCuenta());
			
			DtoServiciosAutorizaciones dtoServicioReporte=null;
			ArrayList<DtoServiciosAutorizaciones> listaServiciosReporte = 
				new ArrayList<DtoServiciosAutorizaciones>();			
			
			for(DtoSolicitudesSubCuenta servicio : listaServicios){
				
				dtoServicioReporte= new DtoServiciosAutorizaciones();
				if(UtilidadTexto.isEmpty(servicio.getCodigoCups()))
					dtoServicioReporte.setCodigoServicio(Utilidades.convertirAEntero(servicio.getServicio().getCodigo()));
				else
					dtoServicioReporte.setCodigoServicio(Utilidades.convertirAEntero(servicio.getCodigoCups()));
				dtoServicioReporte.setDescripcionServicio(servicio.getServicio().getNombre());
				dtoServicioReporte.setNumeroOrden(Utilidades.convertirAEntero(servicio.getNumeroSolicitud()));
				dtoServicioReporte.setCantidadSolicitada(1);	
				dtoServicioReporte.setDescripcionNivelAutorizacion(forma.getNivelServicio());
				dtoServicioReporte.setFechaOrden(UtilidadFecha.getFechaActualTipoBD());				
																
				if(dtoDiagnostico!=null){
					String diagnostico = dtoDiagnostico.getAcronimoDiagnostico() +
								" - " + dtoDiagnostico.getTipoCieDiagnostico();
					dtoServicioReporte.setDiagnostico(diagnostico);
				}
				listaServiciosReporte.add(dtoServicioReporte);
			}
				
			dtoReporte.setNumeroAutorizacion(forma.getNumAutorizacion());
			dtoReporte.setFechaAutorizacion(UtilidadFecha.conversionFormatoFechaAAp(UtilidadFecha.getFechaActual()));
			dtoReporte.setFechaVencimiento(UtilidadFecha.conversionFormatoFechaAAp(forma.getFechaVencimiento()));
			dtoReporte.setObservaciones(forma.getObservaciones());
			
			if(!UtilidadTexto.isEmpty(forma.getEstadoAutorizacion()))
			{					
				String estado = (String)ValoresPorDefecto.getIntegridadDominio(forma.getEstadoAutorizacion());	 			     					
				dtoReporte.setEstadoAutorizacion(estado);	 
			} 			     				
			dtoReporte.setEstadoAutorizacion(forma.getEstadoAutorizacion());	
			dtoReporte.setListaServiciosAutorizados(listaServiciosReporte);
			
			JasperPrint reporte = generadorReporte.generarReporte();
			nombreArchivo = generadorReporte.exportarReportePDF(reporte, nombreReporte);
				
			//JasperViewer.viewReport(reporte, false);	
			listaNombresReportes.add(nombreArchivo);	 			     				
			 
			if(listaNombresReportes!=null && listaNombresReportes.size()>0)
			{
				forma.setListaNombresReportes(listaNombresReportes);				
			}	
	    }
	    
	    /**GENERAR REPORTE SERVICIOS VERSALLES
	     * 
	     * @param con
	     * @param paciente
	     * @param forma
	     * @param usuarioSesion
	     * @param numeroSolicitud
	     * @param request
	     * @param convenioResponsable
	     * @param dtoProcesoAutorizacion
	     * @param dtoValidacionNivelAutorizacion
	     * @param listaServicios
	     */
	    @SuppressWarnings("deprecation")
		private void generarReporteAutorizacionFormatoVersallesServicios(Connection con,PersonaBasica paciente, 
	    		AutorizacionesEntidadesSubcontratadasForm forma, UsuarioBasico usuarioSesion,
	    		HttpServletRequest request,Convenios convenioResponsable,	    		
	    		ArrayList<DtoSolicitudesSubCuenta> listaServicios) throws IPSException
	    {
	    	
	    	MessageResources mensajes=MessageResources.getMessageResources(
	 		"com.servinte.mensajes.manejoPaciente.AutorizacionesEntidadSubcontratadaForm");
	    	
	    	String nombreReporte="AUTORIZACION ORDENES MEDICAS";
			String nombreArchivo ="";
			DtoGeneralReporteServiciosAutorizados dtoReporte = new DtoGeneralReporteServiciosAutorizados();
			GeneradorReporteFormatoCapitacionAutorservicio generadorReporte = 
				new GeneradorReporteFormatoCapitacionAutorservicio(dtoReporte);
			ArrayList<String> listaNombresReportes = new ArrayList<String>();		
			DTOReporteAutorizacionSeccionPaciente dtoPaciente = new DTOReporteAutorizacionSeccionPaciente();
			DTOReporteAutorizacionSeccionAutorizacion dtoAutorizacion = new DTOReporteAutorizacionSeccionAutorizacion();
			
			int codigoServicio =Utilidades.convertirAEntero(listaServicios.get(0).getServicio().getCodigo());				
			
			InfoResponsableCobertura infoResponsableCobertura= new InfoResponsableCobertura();
			infoResponsableCobertura=Cobertura.validacionCoberturaServicio(con, paciente.getCodigoIngreso()+"", 
					paciente.getCodigoUltimaViaIngreso(), paciente.getCodigoTipoPaciente(),codigoServicio, 
					usuarioSesion.getCodigoInstitucionInt(),forma.isSolPYP(), "");
			
		    Cuenta cuenta= new Cuenta();
	        cuenta.cargarCuenta(con, paciente.getCodigoCuenta()+"");
			
			DtoSubCuentas subcuenta =  infoResponsableCobertura.getDtoSubCuenta();
			
			InstitucionBasica institucion = (
		        		InstitucionBasica)request.getSession().getAttribute("institucionBasica");
						 			     			
			String nombrePaciente = paciente.getPrimerNombre() + " " + 
				paciente.getSegundoNombre() + " " + paciente.getPrimerApellido()+
				" " + paciente.getSegundoApellido();
				 			     			
			String reporteMediaCarta = ValoresPorDefecto.getImpresionMediaCarta(
					usuarioSesion.getCodigoInstitucionInt());
			
			String infoEncabezado = ValoresPorDefecto.getEncFormatoImpresionAutorEntidadSub(
					usuarioSesion.getCodigoInstitucionInt());
			
			String infoPiePagina=ValoresPorDefecto.getPiePagFormatoImpresionAutorEntidadSub(
					usuarioSesion.getCodigoInstitucionInt());
			
			if(UtilidadTexto.isEmpty(reporteMediaCarta)){
				reporteMediaCarta=ConstantesBD.acronimoNo;
			}	
			
			int idDetalleMonto = subcuenta.getMontoCobro();
			String montoCobro="";
			if(idDetalleMonto >0){
				IDetalleMontoGeneralServicio detalleMontoGeneralServicio = 
					FacturacionServicioFabrica.crearDetalleMontoGeneralServicio();
				DTOMontosCobroDetalleGeneral detalleMontoGeneral = 
					detalleMontoGeneralServicio.obtenerValorTipoMonto(idDetalleMonto);
				
				if(detalleMontoGeneral!=null){
					
					String temporalMontoCobro="";
					if(detalleMontoGeneral.getPorcentaje()!=null)
						temporalMontoCobro=detalleMontoGeneral.getPorcentaje().doubleValue()+"%";
					else if (detalleMontoGeneral.getValor()!=null)
						temporalMontoCobro=" $"+String.valueOf(detalleMontoGeneral.getValor().doubleValue());

					if(!UtilidadTexto.isEmpty(temporalMontoCobro)){
						
						if((detalleMontoGeneral.getDetalleMonto().getTiposMonto().getCodigo())==
							ConstantesBD.codigoTipoMontoCopago){
							montoCobro=
								mensajes.getMessage("AutorizacionesEntidadSubcontratadaForm.acronimoTipoMontoCopago") + " " +					
							temporalMontoCobro; 
							
						}else if((detalleMontoGeneral.getDetalleMonto().getTiposMonto().getCodigo())==
							ConstantesBD.codigoTipoMontoCuotaModeradora){
							montoCobro=
								mensajes.getMessage("AutorizacionesEntidadSubcontratadaForm.acronimoTipoMontoCuotaModeradora") + " " + 
							temporalMontoCobro;
						}
					}
				}
			}
			
			dtoPaciente.setNombrePaciente(nombrePaciente);
			dtoPaciente.setTipoDocPaciente(paciente.getCodigoTipoIdentificacionPersona());
			dtoPaciente.setNumeroDocPaciente(paciente.getNumeroIdentificacionPersona());
			dtoPaciente.setTipoContratoPaciente(convenioResponsable.getTiposContrato().getNombre());	
			dtoPaciente.setConvenioPaciente(convenioResponsable.getNombre());
			dtoPaciente.setTipoAfiliado(cuenta.getTipoAfiliado());
			dtoPaciente.setEdadPaciente(String.valueOf(paciente.getEdad()));
			dtoPaciente.setCategoriaSocioEconomica(cuenta.getEstrato());
//			dtoPaciente.setSemanasCotizacion(String.valueOf(subcuenta.getSemanasCotizacion()));
			dtoPaciente.setMontoCobro(montoCobro);
			dtoPaciente.setRecobro(ConstantesBD.acronimoNo);
					
			
			dtoAutorizacion.setEntidadSub(forma.getNomEntidadAutorizada());
			dtoAutorizacion.setDireccionEntidadSub(forma.getDirEntidadAutorizada());
			dtoAutorizacion.setTelefonoEntidadSub(forma.getTelEntidadAutorizada());
			dtoAutorizacion.setEntidadAutoriza(usuarioSesion.getInstitucion());
			dtoAutorizacion.setUsuarioAutoriza(usuarioSesion.getLoginUsuario());
							
			dtoReporte.setDtoPaciente(dtoPaciente);
			dtoReporte.setDatosEncabezado(infoEncabezado);
			dtoReporte.setDatosPie(infoPiePagina);
			dtoReporte.setTipoReporteMediaCarta(reporteMediaCarta);
			dtoReporte.setRutaLogo(institucion.getLogoJsp());
			dtoReporte.setUbicacionLogo(institucion.getUbicacionLogo());
						
			//Se obtiene el ultimo diagnostico del paciente
			DtoDiagnostico dtoDiagnostico = Utilidades.getDiagnosticoPacienteCuenta(paciente.getCodigoCuenta());
			
			DtoServiciosAutorizaciones dtoServicioReporte=null;
			ArrayList<DtoServiciosAutorizaciones> listaServiciosReporte = 
				new ArrayList<DtoServiciosAutorizaciones>();			
			
			for(DtoSolicitudesSubCuenta servicio : listaServicios){
				
				dtoServicioReporte= new DtoServiciosAutorizaciones();
				if(UtilidadTexto.isEmpty(servicio.getCodigoCups()))
					dtoServicioReporte.setCodigoPropietario(servicio.getServicio().getCodigo());
				else
					dtoServicioReporte.setCodigoPropietario(servicio.getCodigoCups());
				dtoServicioReporte.setDescripcionServicio(servicio.getServicio().getNombre());
				dtoServicioReporte.setConsecutivoOrdenMed(Utilidades.convertirAEntero(servicio.getNumeroSolicitud()));
				dtoServicioReporte.setCantidadSolicitada(1);
				dtoServicioReporte.setDescripcionNivelAutorizacion(forma.getNivelServicio());
				dtoServicioReporte.setFechaOrden(UtilidadFecha.getFechaActualTipoBD());				
																
				if(dtoDiagnostico!=null){
					String diagnostico = dtoDiagnostico.getAcronimoDiagnostico() +
								" - " + dtoDiagnostico.getTipoCieDiagnostico();
					dtoServicioReporte.setDiagnostico(diagnostico);
				}
				listaServiciosReporte.add(dtoServicioReporte);
			}			
							
			dtoAutorizacion.setFechaAutorizacion(UtilidadFecha.conversionFormatoFechaAAp(UtilidadFecha.getFechaActual(con)));
			dtoAutorizacion.setFechaVencimiento(UtilidadFecha.conversionFormatoFechaAAp(forma.getFechaVencimiento()));
										
			if(!UtilidadTexto.isEmpty(forma.getEstadoAutorizacion()))
			{			
				String estado = (String)ValoresPorDefecto.getIntegridadDominio(forma.getEstadoAutorizacion());	 			     					
				dtoAutorizacion.setEstadoAutorizacion(estado);	 
			} 
			dtoAutorizacion.setEstadoAutorizacion(forma.getEstadoAutorizacion());	
			dtoAutorizacion.setObservaciones(forma.getObservaciones());
			dtoAutorizacion.setNumeroAutorizacion(forma.getNumAutorizacion());
				
			dtoReporte.setDtoAutorizacion(dtoAutorizacion);
			dtoReporte.setListaServicios(listaServiciosReporte);
				
			JasperPrint reporte = generadorReporte.generarReporte();
			nombreArchivo = generadorReporte.exportarReportePDF(reporte, nombreReporte);
				
			listaNombresReportes.add(nombreArchivo);
			
			if(listaNombresReportes!=null && listaNombresReportes.size()>0)
			{
				forma.setListaNombresReportes(listaNombresReportes);				
			}		
	    }
	    
	    
	    
		/**GENERAR REPORTE ARTICULOS VERSALLES
		 * 
		 * @param forma
		 * @param con
		 * @param paciente
		 * @param usuarioSesion
		 * @param request
		 * @param convenioResponsable
		 * @param dtoProcesoAutorizacion
		 * @param dtoValidacionNivelAutorizacion
		 * @param listaArticulos
		 */
		@SuppressWarnings("deprecation")
		private void generarReporteAutorizacionFormatoVersallesArticulos(AutorizacionesEntidadesSubcontratadasForm forma, 
				Connection con,
				PersonaBasica paciente, UsuarioBasico usuarioSesion,
				HttpServletRequest request, Convenios convenioResponsable,				
				ArrayList<DtoSolicitudesSubCuenta> listaArticulos) throws IPSException 
		{
			
			MessageResources mensajes=MessageResources.getMessageResources(
	 		"com.servinte.mensajes.manejoPaciente.AutorizacionesEntidadSubcontratadaForm");
			
			String nombreReporte="AUTORIZACION ORDENES MEDICAS";
			String nombreArchivo ="";
			DtoGeneralReporteArticulosAutorizados dtoReporte = new DtoGeneralReporteArticulosAutorizados();
			GeneradorReporteFormatoCapitacionAutorArticulos generadorReporte = 
				new GeneradorReporteFormatoCapitacionAutorArticulos(dtoReporte);
			ArrayList<String> listaNombresReportes = new ArrayList<String>();
			DTOReporteAutorizacionSeccionPaciente dtoPaciente = new DTOReporteAutorizacionSeccionPaciente();
			DTOReporteAutorizacionSeccionAutorizacion dtoAutorizacion = new DTOReporteAutorizacionSeccionAutorizacion();
			
			int codigoArticulo =Utilidades.convertirAEntero(listaArticulos.get(0).getArticulo().getCodigo());				
			
			InfoResponsableCobertura infoResponsableCobertura= new InfoResponsableCobertura();
			infoResponsableCobertura=Cobertura.validacionCoberturaArticulo(con, paciente.getCodigoIngreso()+"", 
					paciente.getCodigoUltimaViaIngreso(), paciente.getCodigoTipoPaciente(), codigoArticulo,
					usuarioSesion.getCodigoInstitucionInt(), false);
			
			Cuenta cuenta= new Cuenta();
	        cuenta.cargarCuenta(con, paciente.getCodigoCuenta()+"");
			
			DtoSubCuentas subcuenta =  infoResponsableCobertura.getDtoSubCuenta();
			
			InstitucionBasica institucion = (
			    		InstitucionBasica)request.getSession().getAttribute("institucionBasica");
						 			     			
			String nombrePaciente = paciente.getPrimerNombre() + " " + 
				paciente.getSegundoNombre() + " " + paciente.getPrimerApellido()+
				" " + paciente.getSegundoApellido();
				 			     			
			String reporteMediaCarta = ValoresPorDefecto.getImpresionMediaCarta(
					usuarioSesion.getCodigoInstitucionInt());
			
			String infoEncabezado = ValoresPorDefecto.getEncFormatoImpresionAutorEntidadSub(
					usuarioSesion.getCodigoInstitucionInt());
			
			String infoPiePagina=ValoresPorDefecto.getPiePagFormatoImpresionAutorEntidadSub(
					usuarioSesion.getCodigoInstitucionInt());
			
			if(UtilidadTexto.isEmpty(reporteMediaCarta)){
				reporteMediaCarta=ConstantesBD.acronimoNo;
			}
			
			int idDetalleMonto = subcuenta.getMontoCobro();
			String montoCobro="";
			if(idDetalleMonto >0){
				IDetalleMontoGeneralServicio detalleMontoGeneralServicio = 
					FacturacionServicioFabrica.crearDetalleMontoGeneralServicio();
				DTOMontosCobroDetalleGeneral detalleMontoGeneral = 
					detalleMontoGeneralServicio.obtenerValorTipoMonto(idDetalleMonto);
				
				if(detalleMontoGeneral!=null){
					
					String temporalMontoCobro="";
					if(detalleMontoGeneral.getPorcentaje()!=null)
						temporalMontoCobro=detalleMontoGeneral.getPorcentaje().doubleValue()+"%";
					else if (detalleMontoGeneral.getValor()!=null)
						temporalMontoCobro=" $"+String.valueOf(detalleMontoGeneral.getValor().doubleValue());

					if(!UtilidadTexto.isEmpty(temporalMontoCobro)){
						
						if((detalleMontoGeneral.getDetalleMonto().getTiposMonto().getCodigo())==
							ConstantesBD.codigoTipoMontoCopago){
							montoCobro=
								mensajes.getMessage("AutorizacionesEntidadSubcontratadaForm.acronimoTipoMontoCopago") + " " +					
							temporalMontoCobro; 
							
						}else if((detalleMontoGeneral.getDetalleMonto().getTiposMonto().getCodigo())==
							ConstantesBD.codigoTipoMontoCuotaModeradora){
							montoCobro=
								mensajes.getMessage("AutorizacionesEntidadSubcontratadaForm.acronimoTipoMontoCuotaModeradora") + " " + 
							temporalMontoCobro;
						}
					}
				}
			}
			
			dtoPaciente.setNombrePaciente(nombrePaciente);
			dtoPaciente.setTipoDocPaciente(paciente.getCodigoTipoIdentificacionPersona());
			dtoPaciente.setNumeroDocPaciente(paciente.getNumeroIdentificacionPersona());
			dtoPaciente.setTipoContratoPaciente(convenioResponsable.getTiposContrato().getNombre());	
			dtoPaciente.setConvenioPaciente(convenioResponsable.getNombre());
			
			dtoPaciente.setTipoAfiliado(cuenta.getTipoAfiliado());
			dtoPaciente.setEdadPaciente(String.valueOf(paciente.getEdad()));
			dtoPaciente.setCategoriaSocioEconomica(cuenta.getEstrato());
//			dtoPaciente.setSemanasCotizacion(String.valueOf(subcuenta.getSemanasCotizacion()));
			dtoPaciente.setMontoCobro(montoCobro);
			dtoPaciente.setRecobro(ConstantesBD.acronimoNo);
					
			dtoAutorizacion.setEntidadSub(forma.getNomEntidadAutorizada());
			dtoAutorizacion.setDireccionEntidadSub(forma.getDirEntidadAutorizada());
			dtoAutorizacion.setTelefonoEntidadSub(forma.getTelEntidadAutorizada());
			
			dtoAutorizacion.setEntidadAutoriza(usuarioSesion.getInstitucion());
			dtoAutorizacion.setUsuarioAutoriza(usuarioSesion.getLoginUsuario());
										
			dtoReporte.setDtoPaciente(dtoPaciente);
			dtoReporte.setDatosEncabezado(infoEncabezado);
			dtoReporte.setDatosPie(infoPiePagina);
			dtoReporte.setTipoReporteMediaCarta(reporteMediaCarta);
			dtoReporte.setRutaLogo(institucion.getLogoJsp());
			dtoReporte.setUbicacionLogo(institucion.getUbicacionLogo());
						
			//Se obtiene el ultimo diagnostico del paciente
			DtoDiagnostico dtoDiagnostico = Utilidades.getDiagnosticoPacienteCuenta(paciente.getCodigoCuenta());					
			DtoArticulosAutorizaciones dtoArticulosReporte=null;
			ArrayList<DtoArticulosAutorizaciones> listaArticulosReporte = 
				new ArrayList<DtoArticulosAutorizaciones>();			
			
			for(DtoSolicitudesSubCuenta articulo : listaArticulos)
			{				
				dtoArticulosReporte= new DtoArticulosAutorizaciones();
				dtoArticulosReporte.setCodigoArticulo(Utilidades.convertirAEntero(articulo.getArticulo().getCodigo()));
				dtoArticulosReporte.setDescripcionArticulo(articulo.getArticulo().getValue());				
				dtoArticulosReporte.setNaturalezaArticulo(articulo.getNaturalezaArticulo());
				dtoArticulosReporte.setEsMedicamento(articulo.getEsMedicamento().charAt(0));
				dtoArticulosReporte.setUnidadMedidaArticulo(articulo.getUnidadMedidaArticulo());				
				dtoArticulosReporte.setCantidadSolicitada(Utilidades.convertirAEntero(articulo.getNroDosisTotalArticulo()));
				dtoArticulosReporte.setFechaOrden(UtilidadFecha.getFechaActualTipoBD());				
				dtoArticulosReporte.setNumeroOrden(Utilidades.convertirAEntero(articulo.getNumeroSolicitud()));
																		
				if(dtoDiagnostico!=null){
					String diagnostico = dtoDiagnostico.getAcronimoDiagnostico() +
								" - " + dtoDiagnostico.getTipoCieDiagnostico();
					dtoArticulosReporte.setDiagnostico(diagnostico);
				}
				listaArticulosReporte.add(dtoArticulosReporte);				
			}
												
			dtoAutorizacion.setFechaAutorizacion(UtilidadFecha.conversionFormatoFechaAAp(UtilidadFecha.getFechaActualTipoBD()));
			dtoAutorizacion.setFechaVencimiento(UtilidadFecha.conversionFormatoFechaAAp(forma.getFechaVencimiento()));
							
			if(!UtilidadTexto.isEmpty(forma.getEstadoAutorizacion()))
			{					
				String estado = (String)ValoresPorDefecto.getIntegridadDominio(
						forma.getEstadoAutorizacion());	 			     					
				dtoAutorizacion.setEstadoAutorizacion(estado);	 
			} 
			dtoAutorizacion.setEstadoAutorizacion(forma.getEstadoAutorizacion());
			logger.info("estado autorizacion: "+forma.getEstadoAutorizacion());
				
			dtoAutorizacion.setObservaciones(forma.getObservaciones());
			dtoAutorizacion.setNumeroAutorizacion(forma.getNumAutorizacion());
			
				
			dtoReporte.setDtoAutorizacion(dtoAutorizacion);
			dtoReporte.setListaArticulos(listaArticulosReporte);
				
			JasperPrint reporte = generadorReporte.generarReporte();
			nombreArchivo = generadorReporte.exportarReportePDF(reporte, nombreReporte);
				
			//JasperViewer.viewReport(reporte, false);	
			listaNombresReportes.add(nombreArchivo);
			
			if(listaNombresReportes!=null && listaNombresReportes.size()>0)
			{
				forma.setListaNombresReportes(listaNombresReportes);				
			}
		}
		
		
		/**IMPRIMIR REPORTE ARTICULOS ESTANDAR
		 * 
		 * @param paciente
		 * @param forma
		 * @param usuarioSesion
		 * @param request
		 * @param convenioResponsable
		 * @param dtoProcesoAutorizacion
		 * @param dtoValidacionNivelAutorizacion
		 * @param listaArticulos
		 */
		@SuppressWarnings("deprecation")
		private void generarReporteAutorizacionFormatoEstandarArticulos(Connection con,PersonaBasica paciente,
				AutorizacionesEntidadesSubcontratadasForm forma,
				UsuarioBasico usuarioSesion, 
				HttpServletRequest request,
				Convenios convenioResponsable,				
				ArrayList<DtoSolicitudesSubCuenta> listaArticulos)
		{
					
			String nombreReporte="AUTORIZACION ORDENES MEDICAS";
			String nombreArchivo ="";
			DTOReporteEstandarAutorizacionServiciosArticulos dtoReporte = 
				new DTOReporteEstandarAutorizacionServiciosArticulos();
			
			GeneradorReporteFormatoEstandarAutorArticulos generadorReporte = 
					new GeneradorReporteFormatoEstandarAutorArticulos(dtoReporte);
				
			ArrayList<String> listaNombresReportes = new ArrayList<String>();
				
			InstitucionBasica institucion = (
			   		InstitucionBasica)request.getSession().getAttribute("institucionBasica");
				
			String nombrePaciente = paciente.getPrimerNombre() + " " + 
					paciente.getSegundoNombre() + " " + paciente.getPrimerApellido() + 
					" " + paciente.getSegundoApellido();
					 			     			
			String formatoReporte = ValoresPorDefecto.getImpresionMediaCarta(
						usuarioSesion.getCodigoInstitucionInt());
				
			String infoParametroGeneral = ValoresPorDefecto.getEncFormatoImpresionAutorEntidadSub(
						usuarioSesion.getCodigoInstitucionInt());
				
			String infoPiePagina=ValoresPorDefecto.getPiePagFormatoImpresionAutorEntidadSub(
						usuarioSesion.getCodigoInstitucionInt());
				
			if(UtilidadTexto.isEmpty(formatoReporte))
			{
					formatoReporte=ConstantesBD.acronimoNo;
			}
				
			dtoReporte.setNombrePaciente(nombrePaciente);
			dtoReporte.setTipoDocPaciente(paciente.getCodigoTipoIdentificacionPersona());
			dtoReporte.setNumeroDocPaciente(paciente.getNumeroIdentificacionPersona());
			dtoReporte.setTipoContrato(convenioResponsable.getTiposContrato().getNombre());	 			     			
			
			dtoReporte.setEntidadSubcontratada(forma.getNomEntidadAutorizada());
			dtoReporte.setDireccionEntidadSub(forma.getDirEntidadAutorizada());
			dtoReporte.setTelefonoEntidadSub(forma.getTelEntidadAutorizada());
			
			dtoReporte.setFormatoMediaCarta(formatoReporte);
			dtoReporte.setInfoParametroGeneral(infoParametroGeneral);
			dtoReporte.setInfoPiePagina(infoPiePagina);
			dtoReporte.setEntidadAutoriza(usuarioSesion.getInstitucion());
			dtoReporte.setUsuarioAutoriza(usuarioSesion.getLoginUsuario());
			dtoReporte.setRutaLogo(institucion.getLogoJsp());
			dtoReporte.setUbicacionLogo(institucion.getUbicacionLogo());
				
			//Se obtiene el ultimo diagnostico del paciente
			DtoDiagnostico dtoDiagnostico = Utilidades.getDiagnosticoPacienteCuenta(paciente.getCodigoCuenta());				
			DtoArticulosAutorizaciones dtoArticulosReporte=null;
			ArrayList<DtoArticulosAutorizaciones> listaArticulosReporte = 
				new ArrayList<DtoArticulosAutorizaciones>();			
				
			for(DtoSolicitudesSubCuenta articulo : listaArticulos)
			{				
				dtoArticulosReporte= new DtoArticulosAutorizaciones();
				dtoArticulosReporte.setCodigoArticulo(Utilidades.convertirAEntero(articulo.getArticulo().getCodigo()));
				dtoArticulosReporte.setDescripcionArticulo(articulo.getArticulo().getNombre());
				logger.info("descripcion articulo: "+articulo.getArticulo().getValue());
				dtoArticulosReporte.setDosisFormulacion(articulo.getDosisArticulo());
				dtoArticulosReporte.setFrecuenciaFormulacion(Utilidades.convertirAEntero(articulo.getFrecuArticulo()));
				dtoArticulosReporte.setTipoFrecuenciaFormulacion(articulo.getTipoFrecueArticulo());
				dtoArticulosReporte.setViaFormulacion(articulo.getViaArticulo());
				dtoArticulosReporte.setDiasTratamientoFormulacion(Utilidades.convertirALong(articulo.getDuracionArticulo()));
				dtoArticulosReporte.setCantidadAutorizadaArticulo(articulo.getNroDosisTotalArticulo());				
				dtoArticulosReporte.setNaturalezaArticulo(articulo.getNaturalezaArticulo());
				dtoArticulosReporte.setEsMedicamento(articulo.getEsMedicamento().charAt(0));
				dtoArticulosReporte.setUnidadMedidaArticulo(articulo.getUnidadMedidaArticulo());
							
				if(dtoDiagnostico!=null)
				{
					String diagnostico = dtoDiagnostico.getAcronimoDiagnostico() +
								" - " + dtoDiagnostico.getTipoCieDiagnostico();
					dtoArticulosReporte.setDiagnostico(diagnostico);
				}
					listaArticulosReporte.add(dtoArticulosReporte);					
			}
					
			//for(DtoSolicitudesSubCuenta autorizacion : listaArticulos)
			{
					
				dtoReporte.setNumeroAutorizacion(forma.getNumAutorizacion());
				dtoReporte.setFechaAutorizacion(UtilidadFecha.conversionFormatoFechaAAp(UtilidadFecha.getFechaActual(con)));
				dtoReporte.setFechaVencimiento(UtilidadFecha.conversionFormatoFechaAAp(forma.getFechaVencimiento()));
									
				if(!UtilidadTexto.isEmpty(forma.getEstadoAutorizacion()))
				{					
					String estado = (String)ValoresPorDefecto.getIntegridadDominio(
							forma.getEstadoAutorizacion());	 			     					
					dtoReporte.setEstadoAutorizacion(estado);	 
				} 			     				
				dtoReporte.setEstadoAutorizacion(forma.getEstadoAutorizacion());
				dtoReporte.setObservaciones(forma.getObservaciones());
				dtoReporte.setListaArticulosAutorizados(listaArticulosReporte);
				
				JasperPrint reporte = generadorReporte.generarReporte();
				nombreArchivo = generadorReporte.exportarReportePDF(reporte, nombreReporte);
					
				//JasperViewer.viewReport(reporte, false);	
				listaNombresReportes.add(nombreArchivo);
			}
			
			forma.setListaNombresReportes(listaNombresReportes);
			
		}		
	 
}
