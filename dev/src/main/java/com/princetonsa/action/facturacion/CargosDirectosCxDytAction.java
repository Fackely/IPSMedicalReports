/*
 * Marzo 25, 2008
 */
package com.princetonsa.action.facturacion;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

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
import org.apache.struts.action.ActionMessages;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ElementoApResource;
import util.RespuestaValidacion;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.Administracion.UtilidadesAdministracion;
import util.facturacion.InfoResponsableCobertura;
import util.facturacion.UtilidadesFacturacion;
import util.salas.UtilidadesSalas;

import com.princetonsa.actionform.facturacion.CargosDirectosCxDytForm;
import com.princetonsa.actionform.salasCirugia.LiquidacionServiciosForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.manejoPaciente.DtoSolicitudesSubCuenta;
import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.EsquemaTarifario;
import com.princetonsa.mundo.cargos.TarifaISS;
import com.princetonsa.mundo.cargos.TarifaSOAT;
import com.princetonsa.mundo.cargos.UtilidadJustificacionPendienteArtServ;
import com.princetonsa.mundo.facturacion.CargosDirectosCxDyt;
import com.princetonsa.mundo.facturacion.Cobertura;
import com.princetonsa.mundo.facturacion.EntidadesSubContratadas;
import com.princetonsa.mundo.ordenesmedicas.cirugias.HojaQuirurgica;
import com.princetonsa.mundo.salasCirugia.LiquidacionServicios;
import com.princetonsa.pdf.LiquidacionServiciosPdf;
import com.servinte.axioma.bl.manejoPaciente.facade.ManejoPacienteFacade;
import com.servinte.axioma.bl.ordenes.facade.OrdenesFacade;
import com.servinte.axioma.dto.facturacion.ContratoDto;
import com.servinte.axioma.dto.facturacion.ConvenioDto;
import com.servinte.axioma.dto.facturacion.MontoCobroDto;
import com.servinte.axioma.dto.manejoPaciente.AutorizacionCapitacionDto;
import com.servinte.axioma.dto.manejoPaciente.DatosPacienteAutorizacionDto;
import com.servinte.axioma.dto.ordenes.OrdenAutorizacionDto;
import com.servinte.axioma.dto.ordenes.ServicioAutorizacionOrdenDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * @author Sebastián Gómez R. 
 *
 * Clase usada para controlar los procesos de la funcionalidad
 * Cargos Directos x cirugías y dyt
 */
public class CargosDirectosCxDytAction extends Action 
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(CargosDirectosCxDytAction.class);
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(	ActionMapping mapping,
													ActionForm form,
													HttpServletRequest request,
													HttpServletResponse response ) throws Exception
													{
		Connection con=null;
		try{
			if (response==null); //Para evitar que salga el warning
			if(form instanceof CargosDirectosCxDytForm)
			{

				//SE ABRE CONEXION
				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexión"+e.toString());
				}

				//OBJETOS A USAR
				CargosDirectosCxDytForm cargosForm =(CargosDirectosCxDytForm)form;
				HttpSession session=request.getSession();		
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
				PersonaBasica paciente = (PersonaBasica)session.getAttribute("pacienteActivo");
				String estado=cargosForm.getEstado(); 
				logger.warn("estado CargosDirectosCxAction-->"+estado);

				if(estado == null)
				{
					cargosForm.reset();	
					logger.warn("Estado no valido dentro del flujo de Cargos Direct6os x cirugias y DYT (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				else if (estado.equals("empezar"))
				{
					return accionEmpezar(con,cargosForm,mapping,paciente,usuario,request);
				}
				else if (estado.equals("continuarCasoAsocio"))
				{
					cargosForm.setEstado("empezar");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("encabezado");
				}
				else if (estado.equals("agregarServicio"))
				{
					return accionAgregarServicio(con,cargosForm,usuario,mapping,paciente);
				}
				else if (estado.equals("eliminarServicio"))
				{
					return accionEliminarServicio(con,cargosForm,mapping,usuario);
				}
				else if (estado.equals("detalleServicio"))
				{
					return accionDetalleServicio(con,cargosForm,usuario,mapping,paciente);
				}
				else if (estado.equals("adicionarAsocio"))
				{
					return accionAdicionarAsocio(con,cargosForm,usuario,mapping,request);
				}
				else if (estado.equals("volverEncabezado"))
				{
					return accionVolverEncabezado(con,cargosForm,usuario,mapping,request);
				}
				else if (estado.equals("guardar"))
				{
					return accionGuardar(con,cargosForm,usuario,mapping,request,paciente);
				}
				else if (estado.equals("imprimir"))
				{
					return accionImprimir(con,cargosForm,usuario,mapping,request,paciente);
				}
				//Estado para el flujo de la sección de recién nacidos
				else if (estado.equals("verificarHijos"))
				{
					return accionVerificarHijos(con,cargosForm,usuario,mapping,request);
				}
				//*************ESTADOS PARA FILTROS AJAX***********************************************************************
				else if (estado.equals("filtrarSalas"))
				{
					return accionFiltrarSalas(con,cargosForm,usuario,response);
				}
				else if (estado.equals("filtrarAnestesiologos"))
				{
					return accionFiltrarAnestesiologos(con,cargosForm,usuario,response);
				}
				else if (estado.equals("filtrarOtrosProfesionales"))
				{
					return accionFiltrarOtrosProfesionales(con,cargosForm,usuario,response);
				}
				else if (estado.equals("filtrarAsociosOtrosProfesionales"))
				{
					return accionFiltrarAsociosOtrosProfesionales(con,cargosForm,usuario,response);
				}
				else if (estado.equals("filtroEliminarOtrosProfesionales"))
				{
					return accionFiltroEliminarOtrosProfesionales(con,cargosForm,response);
				}
				else if (estado.equals("filtroEliminarServicio"))
				{
					return accionFiltroEliminarServicio(con,cargosForm,response,usuario);
				}
				else if (estado.equals("filtrarTipoLiquidacion"))
				{
					return accionFiltrarTipoLiquidacion(con,cargosForm,response);
				}
				else if (estado.equals("filtroEliminarAsocio"))
				{
					return accionFiltroEliminarAsocio(con,cargosForm,response);
				}
				else if (estado.equals("filtrarDatosProfesional"))
				{
					return accionFiltrarDatosProfesional(con,cargosForm,response);
				}
				else if (estado.equals("filtrarTiposAnestesia"))
				{
					return accionFiltrarTiposAnestesia(con,cargosForm,response,usuario);
				}
				//***********************************************************************************************************

				else
				{
					cargosForm.reset();
					logger.warn("Estado no valido dentro del flujo de CargosDirectosCxDytAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.cerrarConexion(con);
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
	 * Método para realizar la impresion de la liquidación
	 * @param con
	 * @param cargosForm
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @param paciente 
	 * @return
	 */
	private ActionForward accionImprimir(Connection con, CargosDirectosCxDytForm cargosForm, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request, PersonaBasica paciente) 
	{
		LiquidacionServiciosForm liquidacionForm = new LiquidacionServiciosForm();
		LiquidacionServicios mundoLiquidacion = new LiquidacionServicios();
		
		//Se carga la orden desde el mundo de la liquidación
		mundoLiquidacion.setCon(con);
		mundoLiquidacion.setNumeroSolicitud(cargosForm.getNumeroSolicitud());
		mundoLiquidacion.cargarTodaInformacionOrdenConAsociosLiquidados();
		
		//Se carga la forma
		liquidacionForm.setEncabezadoSolicitud(mundoLiquidacion.getEncabezadoOrden());
		liquidacionForm.setDatosActoQx(mundoLiquidacion.getDatosActoQx());
		liquidacionForm.setOtrosProfesionales(mundoLiquidacion.getOtrosProfesionales());
		liquidacionForm.setCirugiasSolicitud(mundoLiquidacion.getCirugias());
		liquidacionForm.setNombreResponsable(liquidacionForm.getEncabezadoSolicitud("medicoResponde")+"");
		
		UtilidadBD.closeConnection(con);
		///se edita nombre del archivo PDF
		String nombreArchivo;
    	Random r=new Random();
    	nombreArchivo="/liquidacionServicios" + r.nextInt()  +".pdf";
    	
    	
    	LiquidacionServiciosPdf.pdfLiquidacionServicios(ValoresPorDefecto.getFilePath()+nombreArchivo, liquidacionForm, usuario, request, paciente);
    	
    	request.setAttribute("nombreArchivo", nombreArchivo);
    	request.setAttribute("nombreVentana", "Liquidación Servicios");
    	return mapping.findForward("abrirPdf");
	}

	/**
	 * Método implementado para guardar la informacion del cargo directo
	 * @param con
	 * @param cargosForm
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @param paciente 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private ActionForward accionGuardar(Connection con, CargosDirectosCxDytForm cargosForm, UsuarioBasico usuario,
			ActionMapping mapping, HttpServletRequest request, PersonaBasica paciente) 
	{
		ActionErrors errores 	= null;
		ActionErrors erroresJ 	= null;
		CargosDirectosCxDyt mundoCargos = null;
		
		try{
			errores 	= new ActionErrors();//errores
			erroresJ	= new ActionErrors();//Errores Justificacion Pendiente
			mundoCargos = new CargosDirectosCxDyt();
			int numeroSolicitud = ConstantesBD.codigoNuncaValido;
		
			UtilidadBD.iniciarTransaccion(con);
			
			//Se llena la información de la solicitud al mundo
			mundoCargos.setEncabezadoSolicitud(cargosForm.getEncabezadoSolicitud());
			mundoCargos.setDatosActoQx(cargosForm.getDatosActoQx());
			mundoCargos.setCirugiasSolicitud(cargosForm.getCirugiasSolicitud());
			mundoCargos.setOtrosProfesionales(cargosForm.getOtrosProfesionales());
			mundoCargos.setInfoRecienNacidos(cargosForm.getInfoRecienNacidos());
			
			mundoCargos.setDiagnosticoPrincipal(cargosForm.getCirugiasSolicitud("diagPrincipal_0").toString());
			
			logger.info("\n\n====VOY A GENERAR EL CARGO DIRECTO===\n\n");
			if(mundoCargos.generarCargoDirecto(con, usuario, paciente, cargosForm.getIdCuenta())){
				int codigoCentroCostoSolicitado = Utilidades.convertirAEntero(cargosForm.getEncabezadoSolicitud("centroCostoSolicitado").toString());
				if(mundoCargos.getListaCoberturaCargoCirugia() != null && !mundoCargos.getListaCoberturaCargoCirugia().isEmpty()){
					mundoCargos.getListaCoberturaCargoCirugia().get(0).getDtoSubCuenta().getSolicitudesSubcuenta().get(0).
						getCentroCostoEjecuta().setCodigo(codigoCentroCostoSolicitado);
					cargosForm.setListaInfoRespoCoberturaCx(mundoCargos.getListaCoberturaCargoCirugia()); 
				}
				int numeroSolicitudNP = mundoCargos.getNumeroSolicitud();
				this.validarInsertarJustificacion(con, usuario, numeroSolicitudNP, cargosForm, erroresJ);
			}
			mundoCargos.getDtoDiagnostico();
			errores = mundoCargos.getErrores();
			
			LiquidacionServicios mundoLiquidacion = new LiquidacionServicios();
			numeroSolicitud = mundoCargos.getNumeroSolicitud();
			
			logger.info("\n\n====VOY A REALIZAR LA ORDENACIÓN DE LOS SERVICIOS===\n\n");
			//Se realiza la ordenación de los servicios-------------------------------------------------
			if(errores.isEmpty())
			{
				mundoLiquidacion.setCon(con);
				mundoLiquidacion.setNumeroSolicitud(numeroSolicitud+"");
				mundoLiquidacion.setIdIngreso(paciente.getCodigoIngreso()+"");
				mundoLiquidacion.setUsuario(usuario);
				mundoLiquidacion.cargarDetalleOrden();
				
				mundoLiquidacion.reordenarCirugiasSolicitud(true,true);
				//Se limpian los mensajes de error de la ordenacion
				mundoLiquidacion.setMensajesError(new ArrayList<ElementoApResource>());
				mundoLiquidacion.actualizarDatosOrden();
				logger.info("NÚMERO DE MENSAJES DE ERROR #3=> "+mundoLiquidacion.getMensajesError().size());
				errores = mundoLiquidacion.llenarMensajesError(errores);
			}
			//----------------------------------------------------------------------------------------------
				
			
			if(errores.isEmpty())
			{
				UtilidadBD.finalizarTransaccion(con);
				cargosForm.reset();
				
				//Se intenta realizar la liquidación*****************************************
				UtilidadBD.iniciarTransaccion(con);
				if(mundoLiquidacion.realizarLiquidacion(con, numeroSolicitud+"", usuario, true))
				{
					UtilidadBD.finalizarTransaccion(con);
					cargosForm.setImprimir(true);
				}else{
						
					UtilidadBD.abortarTransaccion(con);
					cargosForm.setImprimir(false);
				}
				erroresJ = mundoLiquidacion.llenarMensajesError(erroresJ);
				saveErrors(request, erroresJ);
				//******************************************************************************
				
				//Se carga el resumen
				
				 //Se agrega funcionalidad por versión 1.2 del CU 551 Cargos Directos Cirugia-DYT
				cargosForm.setDtoDiagnostico(mundoCargos.getDtoDiagnostico());
				cargosForm.setSolPYP(mundoCargos.isSolPYP());
				
				try {
					//FIXME VALIDACIONES PARA GENERACION DE AUTORIZACION O ASOCIACION CON LA/LAS NUEVAS SOLICITUDES
					cargarInfoVerificarGeneracionAutorizacion(con, cargosForm, usuario, paciente, errores);
					saveErrors(request, errores);
				}catch (IPSException e) { 				
					Log4JManager.error(e);
					ActionMessages mensajeError = new ActionMessages();
					mensajeError.add("", new ActionMessage(String.valueOf(e.getErrorCode())));
					saveErrors(request, mensajeError);
				}
					
				mundoCargos.clean();
				mundoCargos.setNumeroSolicitud(numeroSolicitud);
				mundoCargos.cargarResumenCargoDirecto(con);
				
				cargosForm.setNumeroSolicitud(mundoCargos.getNumeroSolicitud()+"");
				cargosForm.setEncabezadoSolicitud(mundoCargos.getEncabezadoSolicitud());
				cargosForm.setDatosActoQx(mundoCargos.getDatosActoQx());
				cargosForm.setCirugiasSolicitud(mundoCargos.getCirugiasSolicitud());
				cargosForm.setOtrosProfesionales(mundoCargos.getOtrosProfesionales());
				cargosForm.setInfoRecienNacidos(mundoCargos.getInfoRecienNacidos());
				
				UtilidadBD.closeConnection(con);
				return mapping.findForward("resumen");
					
			}else{
				UtilidadBD.abortarTransaccion(con);
				saveErrors(request, errores);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("encabezado");
			}
			            	
		}catch (Exception e) {
			UtilidadBD.abortarTransaccion(con);
			Log4JManager.error(e.getMessage(),e);
		}	
		return mapping.findForward("encabezado");
	}

	/**
	 * Metodo implementado para la validacion de Servicios NoPos y su Justificacion Pendiente
	 */
	private boolean validarInsertarJustificacion(Connection con, UsuarioBasico user, int numeroSolicitud, CargosDirectosCxDytForm cargosForm, ActionErrors erroresJ)
	{
		int codigoServicio=ConstantesBD.codigoNuncaValido;
		for (int i=0; i<Integer.parseInt(cargosForm.getCirugiasSolicitud("numRegistros").toString()); i ++)
	    {    
	        if(!UtilidadTexto.getBoolean(cargosForm.getCirugiasSolicitud().get("fueEliminado_"+i)+""))
	        {
	            try
	            {
	                codigoServicio= Integer.parseInt(cargosForm.getCirugiasSolicitud("codigoServicio_"+i)+"");
	                if(UtilidadJustificacionPendienteArtServ.justificacionPendienteCirugiaDYT(con, numeroSolicitud, codigoServicio, user.getLoginUsuario()))
	        		{
	                	logger.info("ENTROOOOOOOOOOOOO6666666666666");
        				erroresJ.add("", new ActionMessage("errors.notEspecific", "SERVICIO [ "+codigoServicio+"] DE NATURALEZA NO POS REQUIERE DE JUSTIFICACION NO POS."));
	        		}
	            }
	            catch(NumberFormatException e)
	            {
	                logger.warn("Error no se pudo validar e insertar justificacion pendiente del codigo del articulo "+codigoServicio+" con indice ="+i +"   error-->"+e);
	                return false;
	            }
	        }
	    }
		return true;
	}

	/**
	 * Método implementado para eliminar un servicio
	 * @param con
	 * @param cargosForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEliminarServicio(Connection con, CargosDirectosCxDytForm cargosForm, ActionMapping mapping, UsuarioBasico usuario) 
	{
		//Se consulta el primer servicio antes de eliminar
		int codigoServicioAntes = ConstantesBD.codigoNuncaValido;
		for(int i=0;i<cargosForm.getNumCirugias();i++)
			if(!UtilidadTexto.getBoolean(cargosForm.getCirugiasSolicitud("fueEliminado_"+i)+""))
			{
				codigoServicioAntes = Integer.parseInt(cargosForm.getCirugiasSolicitud("codigoServicio_"+i).toString());
				i = cargosForm.getNumCirugias();
			}
		
		
		//Se elimina el servicio
		cargosForm.setCirugiasSolicitud("fueEliminado_"+cargosForm.getIndex(), ConstantesBD.acronimoSi);
		
		//Se consulta el primer servicio antes de eliminar
		int codigoServicioDespues = ConstantesBD.codigoNuncaValido;
		for(int i=0;i<cargosForm.getNumCirugias();i++)
			if(!UtilidadTexto.getBoolean(cargosForm.getCirugiasSolicitud("fueEliminado_"+i)+""))
			{
				codigoServicioDespues = Integer.parseInt(cargosForm.getCirugiasSolicitud("codigoServicio_"+i).toString());
				i = cargosForm.getNumCirugias();
			}
		
		//********************MANEJO DE LA SECCION DE RECIEN NACIDOS***************************************************
		this.validacionRecienNacidos(cargosForm);
		//*************************************************************************************************************
		
		//*******+Si ya no quedan servicios se debe borrar el combo de centros de costo y tipos anestesia**************
		// Nota * Si el primer servicio ha cambiado entonces se vuelven a consultar los centros de costo/tipos anestesia / anestesiologos
		if(cargosForm.getNumCirugiasReales()==0 || codigoServicioAntes!=codigoServicioDespues)
			validarCentrosCosto(con, cargosForm, usuario, true);
		
		
		//***********Se reeditan los codigos insertados******************************************+
		String codigosServiciosInsertados = "";
		for(int i=0;i<cargosForm.getNumCirugias();i++)
		{
			//Mientras que no se haya eliminado el servicio se toma como existente
			if(!UtilidadTexto.getBoolean(cargosForm.getCirugiasSolicitud("fueEliminado_"+i)+""))
				codigosServiciosInsertados += cargosForm.getCirugiasSolicitud("codigoServicio_"+i)+",";
		}
		cargosForm.setCirugiasSolicitud("codigosServiciosInsertados", codigosServiciosInsertados);
		
		//*****************************************************************************************
		
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("encabezado");
	}

	/**
	 * Método que verifica los hijos ingresados en la informacion del recién nacido
	 * @param con
	 * @param cargosForm
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionVerificarHijos(Connection con, CargosDirectosCxDytForm cargosForm, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request) 
	{
		int nroHijos = Utilidades.convertirAEntero(cargosForm.getInfoRecienNacidos("nroHijos")+"");
		cargosForm.setInfoRecienNacidos("nroHijos", nroHijos);
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("encabezado");
	}

	/**
	 * Método implementado para volver del detalle del servicio al encabezdo de la orden
	 * @param con
	 * @param cargosForm
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionVolverEncabezado(Connection con, CargosDirectosCxDytForm cargosForm, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request) 
	{
		UtilidadBD.closeConnection(con);
		return mapping.findForward("encabezado");
	}

	/**
	 * Método implementado para filtrar los tipos de anestesia
	 * @param con
	 * @param cargosForm
	 * @param response
	 * @param usuario 
	 * @return
	 */
	private ActionForward accionFiltrarTiposAnestesia(Connection con, CargosDirectosCxDytForm cargosForm, HttpServletResponse response, UsuarioBasico usuario) 
	{
		//Se consultan los tipos de anestesia
		if(cargosForm.getIndex().equals(""))
			cargosForm.setTiposAnestesia(new ArrayList<HashMap<String,Object>>());
		else
			cargosForm.setTiposAnestesia(UtilidadesSalas.obtenerTiposAnestesiaInstitucionCentroCosto(con, usuario.getCodigoInstitucionInt(), Integer.parseInt(cargosForm.getIndex())));
		
		String resultado = "<respuesta>" +
			"<infoid>" +
				"<sufijo>ajaxBusquedaTipoSelect</sufijo>" +
				"<id-select>codigoTipoAnestesia</id-select>" +
				"<id-arreglo>tipo-anestesia</id-arreglo>" + //nombre de la etiqueta de cada elemento
			"</infoid>";
		
		for(HashMap elemento:cargosForm.getTiposAnestesia())
		{
			resultado += "<tipo-anestesia>";
				resultado += "<codigo>"+elemento.get("codigo")+"</codigo>";
				resultado += "<descripcion>"+elemento.get("nombre")+"</descripcion>";
			resultado += "</tipo-anestesia>";
		}
		
		resultado += "</respuesta>";
		
		UtilidadBD.closeConnection(con);
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
	        response.getWriter().write(resultado);
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionFiltrarTiposAnestesia: "+e);
		}
		return null;
	}

	/**
	 * Método implementado para filtrar los datos del profesional (especialidad y pool)
	 * @param con
	 * @param cargosForm
	 * @param response
	 * @return
	 */
	private ActionForward accionFiltrarDatosProfesional(Connection con, CargosDirectosCxDytForm cargosForm, HttpServletResponse response) 
	{
		int pos = Integer.parseInt(cargosForm.getIndex());
		int posCx = cargosForm.getPosicion();
		
		
		
		String resultado = "<respuesta>" +
			"<infoid>" +
				"<sufijo>ajaxBusquedaTipoSelect</sufijo>" +
				"<activar-seleccione>"+ConstantesBD.acronimoNo+"</activar-seleccione>" +
				"<id-select>codigoEspecialidad_"+pos+"</id-select>" +
				"<id-select>codigoPool_"+pos+"</id-select>" +
				"<id-arreglo>especialidad</id-arreglo>" +
				"<id-arreglo>pool</id-arreglo>" +
			"</infoid>" ;
		
		
		//***********************ADICION DE LAS ESPECIALIDADES**********************************************************************
		//Se verifica si se ingresó profesional
		if(!cargosForm.getCodigoProfesional().equals(""))
			//Dependiendo si el asocio es cirujano entonces se realiza el cambio de especialidad
			if(!UtilidadTexto.getBoolean(cargosForm.getCirugiasSolicitud("esAsocioCirujano_"+posCx+"_"+pos).toString()))
				cargosForm.setCirugiasSolicitud("comboEspecialidades_"+posCx+"_"+pos, Utilidades.obtenerEspecialidadesEnArray(con, Integer.parseInt(cargosForm.getCodigoProfesional()), ConstantesBD.codigoNuncaValido));
			else
				cargosForm.setCirugiasSolicitud("comboEspecialidades_"+posCx+"_"+pos, Utilidades.obtenerEspecialidadesEnArray(con, ConstantesBD.codigoNuncaValido, Integer.parseInt(cargosForm.getCirugiasSolicitud("codigoEspecialidadInterviene_"+posCx).toString())));
		//Si no se seleccionó profesional se quitan las especialidades
		else
			cargosForm.setCirugiasSolicitud("comboEspecialidades_"+posCx+"_"+pos, new ArrayList<HashMap<String, Object>>());
			
			
		
		for(HashMap elemento:(ArrayList<HashMap<String, Object>>)cargosForm.getCirugiasSolicitud("comboEspecialidades_"+posCx+"_"+pos))
		{
			resultado += "<especialidad>";
				resultado += "<codigo>"+elemento.get("codigoespecialidad")+"</codigo>";
				resultado += "<descripcion>"+elemento.get("nombreespecialidad")+"</descripcion>";
			resultado += "</especialidad>";
		}
		
		//*****************ADICION DE LOS POOLES********************************************************************************
		if(!cargosForm.getCodigoProfesional().equals(""))
			cargosForm.setCirugiasSolicitud("comboPooles_"+posCx+"_"+pos, UtilidadesFacturacion.obtenerPoolesProfesional(con, Integer.parseInt(cargosForm.getCodigoProfesional()), cargosForm.getDatosActoQx("fechaInicialCx").toString(),false));
		else
			cargosForm.setCirugiasSolicitud("comboPooles_"+posCx+"_"+pos, new ArrayList<HashMap<String, Object>>());
		
		for(HashMap elemento:(ArrayList<HashMap<String, Object>>)cargosForm.getCirugiasSolicitud("comboPooles_"+posCx+"_"+pos))
		{
			resultado += "<pool>";
				resultado += "<codigo>"+elemento.get("codigo")+"</codigo>";
				resultado += "<descripcion>"+elemento.get("nombre")+"</descripcion>";
			resultado += "</pool>";
		}
		//****************************************************************************************************************
		
		resultado += "</respuesta>";
		
		UtilidadBD.closeConnection(con);
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
	        response.getWriter().write(resultado);
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionFiltrarDatosProfesional: "+e);
		}
		return null;
	}

	/**
	 * Método para realizar la eliminación de un asocio
	 * @param con
	 * @param cargosForm
	 * @param response
	 * @return
	 */
	private ActionForward accionFiltroEliminarAsocio(Connection con, CargosDirectosCxDytForm cargosForm, HttpServletResponse response) 
	{
		//Se elimina el otro profesional
		cargosForm.setCirugiasSolicitud("fueEliminado_"+cargosForm.getPosicion()+"_"+cargosForm.getIndex(), ConstantesBD.acronimoSi);
		
		String resultado = "<respuesta>" +
			"<infoid>" +
				"<sufijo>ajaxBusquedaTipoEliminarTr</sufijo>" +
				"<id-tr>trProfesionales_"+cargosForm.getIndex()+"</id-tr>" + //posicion del TR a eliminar
				"<id-table>tablaProfesionales</id-table>" + //tabla
			"</infoid>"+
			"</respuesta>";
		
		UtilidadBD.closeConnection(con);
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
	        response.getWriter().write(resultado);
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionFiltroEliminarAsocio: "+e);
		}
		return null;
	}

	/**
	 * Método usado para adicionar un nuevo asocio
	 * @param con
	 * @param cargosForm
	 * @param usuario
	 * @param mapping
	 * @param request 
	 * @return
	 */
	@SuppressWarnings({ "deprecation", "rawtypes" })
	private ActionForward accionAdicionarAsocio(Connection con, CargosDirectosCxDytForm cargosForm, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		
		//Se toman los datos del asocio
		String[] datosAsocio = cargosForm.getTipoHonorario().split(ConstantesBD.separadorSplit);
		int pos = Integer.parseInt(cargosForm.getCirugiasSolicitud("numProfesionales_"+cargosForm.getPosicion())+"");
		int codigoAsocio = Integer.parseInt(datosAsocio[0]);
		boolean esCirujano = false;
		ArrayList<HashMap<String, Object>> arregloProfesionales = new ArrayList<HashMap<String,Object>>();
		
		//1) Validaciones si el asocio es Cirujano y no se ha seleccionado especialidad que interviene
		if(codigoAsocio == cargosForm.getCodigoAsocioCirujano())
		{
			//Se no se ha seleccionado especialidad que interviene se genera error
			if(UtilidadTexto.isEmpty(cargosForm.getCirugiasSolicitud("codigoEspecialidadInterviene_"+cargosForm.getPosicion())+""))
				errores.add("",new ActionMessage("errors.seleccion","especialidad que interviene antes de adicionar el asocio cirujano: "+datosAsocio[1]));
			
			esCirujano = true;
		}
		//2) Validaciones que no se repita el asocio si es diferente del asocio ayudante
		if(codigoAsocio != Integer.parseInt(ValoresPorDefecto.getAsocioAyudantia(usuario.getCodigoInstitucionInt())))
		{
			for(int i=0;i<pos;i++)
			{
				if(codigoAsocio == Integer.parseInt(cargosForm.getCirugiasSolicitud("codigoAsocio_"+cargosForm.getPosicion()+"_"+i).toString())&&
					!UtilidadTexto.getBoolean(cargosForm.getCirugiasSolicitud("fueEliminado_"+cargosForm.getPosicion()+"_"+i)+""))
				{
					i=pos;
					errores.add("",new ActionMessage("errors.yaExiste","El asocio "+datosAsocio[1]));
				}
			}
		}
		
		//Si no habían errores se agrega el nuevo asocio
		if(errores.isEmpty())
		{
			cargosForm.setTipoHonorario("");
			
			//Se agrega el asocio
			cargosForm.setCirugiasSolicitud("codigoAsocio_"+cargosForm.getPosicion()+"_"+pos, codigoAsocio);
			cargosForm.setCirugiasSolicitud("nombreAsocio_"+cargosForm.getPosicion()+"_"+pos, datosAsocio[1]);
			
			/*
			 * si el asocio es cirujano, la forma de llenar los profesionales y especialidades es diferente a los demas 
			 */
			if(esCirujano)
			{
				cargosForm.setCirugiasSolicitud("esAsocioCirujano_"+cargosForm.getPosicion()+"_"+pos, ConstantesBD.acronimoSi);
				
				//Se consulta de una vez la especialidad que interviene
				cargosForm.setCirugiasSolicitud("comboEspecialidades_"+cargosForm.getPosicion()+"_"+pos,
					Utilidades.obtenerEspecialidadesEnArray(
						con, 
						ConstantesBD.codigoNuncaValido, //No se manda profesional 
						Integer.parseInt(cargosForm.getCirugiasSolicitud("codigoEspecialidadInterviene_"+cargosForm.getPosicion()).toString())
						)
					);
				
				cargosForm.setCirugiasSolicitud("codigoProfesional_"+cargosForm.getPosicion()+"_"+pos, "");
				arregloProfesionales = UtilidadesAdministracion.obtenerProfesionales(
						con, 
						usuario.getCodigoInstitucionInt(), 
						Integer.parseInt(cargosForm.getCirugiasSolicitud("codigoEspecialidadInterviene_"+cargosForm.getPosicion()).toString()), 
						true, 
						true,
						ConstantesBD.codigoNuncaValido);
				cargosForm.setCirugiasSolicitud("comboProfesionales_"+cargosForm.getPosicion()+"_"+pos,arregloProfesionales);
			}
			else
			{
				cargosForm.setCirugiasSolicitud("esAsocioCirujano_"+cargosForm.getPosicion()+"_"+pos, ConstantesBD.acronimoNo);
				
				cargosForm.setCirugiasSolicitud("codigoProfesional_"+cargosForm.getPosicion()+"_"+pos, "");
				arregloProfesionales = UtilidadesAdministracion.obtenerProfesionales(
						con, 
						usuario.getCodigoInstitucionInt(), 
						ConstantesBD.codigoNuncaValido, //Sin filtro de especialidad 
						true, 
						true,
						ConstantesBD.codigoNuncaValido); 
				cargosForm.setCirugiasSolicitud("comboProfesionales_"+cargosForm.getPosicion()+"_"+pos,arregloProfesionales);
				
				
				//No se postulan especialidades hasta que no se seleccione un profesional
				cargosForm.setCirugiasSolicitud("comboEspecialidades_"+cargosForm.getPosicion()+"_"+pos,new ArrayList<HashMap<String, Object>>());
			}
			
			cargosForm.setCirugiasSolicitud("cobrable_"+cargosForm.getPosicion()+"_"+pos, ConstantesBD.acronimoSi);
			
			//Si solo había un profesional se prosigue a consultar sus pooles
			if(arregloProfesionales.size()==1&&UtilidadFecha.validarFecha(cargosForm.getDatosActoQx("fechaInicialCx").toString()))
			{
				HashMap elemento = (HashMap)arregloProfesionales.get(0);
				cargosForm.setCirugiasSolicitud("comboPooles_"+cargosForm.getPosicion()+"_"+pos,UtilidadesFacturacion.obtenerPoolesProfesional(con, Integer.parseInt(elemento.get("codigo").toString()), cargosForm.getDatosActoQx("fechaInicialCx").toString(),false));
			}
			else
				cargosForm.setCirugiasSolicitud("comboPooles_"+cargosForm.getPosicion()+"_"+pos, new ArrayList<HashMap<String, Object>>());
			
			pos++;
			cargosForm.setCirugiasSolicitud("numProfesionales_"+cargosForm.getPosicion(), pos);
		}
		else
			saveErrors(request, errores);
		
		
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalle");
	}

	/**
	 * Método implementado para consultar la tarifa del servicio segun el esquema tarifario seleccionado
	 * @param con
	 * @param cargosForm
	 * @param response
	 * @return
	 */
	private ActionForward accionFiltrarTipoLiquidacion(Connection con, CargosDirectosCxDytForm cargosForm, HttpServletResponse response) 
	{
		
		//Se consulta la tarifa servicio
		this.consultaTarifaServicio(con,cargosForm,Utilidades.convertirAEntero(cargosForm.getIndex()));
		
		
		
		String contenido = "";
		
		if(!cargosForm.getCirugiasSolicitud("nombreTipoLiquidacion_"+cargosForm.getPosicion()).toString().equals(""))
		{
			contenido = ""+cargosForm.getCirugiasSolicitud("nombreTipoLiquidacion_"+cargosForm.getPosicion())+": "+
				cargosForm.getCirugiasSolicitud("grupoUvr_"+cargosForm.getPosicion());
		}
		
		String resultado = "<respuesta>" +
			"<infoid>" +
				"<sufijo>ajaxBusquedaTipoInnerHtml</sufijo>" +
				"<id-div>divGrupoUvr</id-div>" + //id del div a modificar
				"<contenido>"+contenido+"</contenido>" + //tabla
			"</infoid>"+
			"</respuesta>";
	
		UtilidadBD.closeConnection(con);
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
	        response.getWriter().write(resultado);
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionFiltroEliminarServicios: "+e);
		}
		return null;
	}

	/**
	 * Método para realizar la consulta de la tarifa del servicio dependiendo del esquema tarifario
	 * @param con
	 * @param cargosForm
	 * @param codigoEsquemaTarifario
	 */
	private void consultaTarifaServicio(Connection con, CargosDirectosCxDytForm cargosForm, int esquemaTarifario) 
	{
		cargosForm.setCirugiasSolicitud("codigoEsquemaTarifario_"+cargosForm.getPosicion(), esquemaTarifario);
		int codigoServicio = Integer.parseInt(cargosForm.getCirugiasSolicitud("codigoServicio_"+cargosForm.getPosicion())+"");
		
		if(cargosForm.getTarifarioOficial()==ConstantesBD.codigoTarifarioSoat)
		{
			TarifaSOAT tarifaSOAT = new TarifaSOAT();
			tarifaSOAT.cargar(con,codigoServicio,esquemaTarifario, UtilidadFecha.getFechaActual());
			//se revisa si el GRUPO es correcto
			if(tarifaSOAT.getCodigoTipoLiquidacion()==ConstantesBD.codigoTipoLiquidacionSoatGrupo)
			{
				if(tarifaSOAT.getGrupo()>0)
					cargosForm.setCirugiasSolicitud("grupoUvr_"+cargosForm.getPosicion(),UtilidadTexto.formatearValores(tarifaSOAT.getGrupo()+"",0,false,false));
				else
					cargosForm.setCirugiasSolicitud("grupoUvr_"+cargosForm.getPosicion(),"");
				
				cargosForm.setCirugiasSolicitud("liquidarAsocios_"+cargosForm.getPosicion(),ConstantesBD.acronimoSi); //Por defecto SI aunque no aplica
			}
			//Se revisa si las UNIDADES O VALOR son correctas
			else if(tarifaSOAT.getCodigoTipoLiquidacion()==ConstantesBD.codigoTipoLiquidacionSoatUnidades||tarifaSOAT.getCodigoTipoLiquidacion()==ConstantesBD.codigoTipoLiquidacionSoatValor)
			{
				if(tarifaSOAT.getValorTarifa()>0)
					cargosForm.setCirugiasSolicitud("grupoUvr_"+cargosForm.getPosicion(),UtilidadTexto.formatearValores(tarifaSOAT.getValorTarifa(),"0.00"));
				else
					cargosForm.setCirugiasSolicitud("grupoUvr_"+cargosForm.getPosicion(),"");
				
				cargosForm.setCirugiasSolicitud("liquidarAsocios_"+cargosForm.getPosicion(),UtilidadTexto.getBoolean(tarifaSOAT.getLiquidarAsocios())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);						
			}
			//De lo contrario no hay parametrización
			else
			{
				cargosForm.setCirugiasSolicitud("grupoUvr_"+cargosForm.getPosicion(),"");
				cargosForm.setCirugiasSolicitud("liquidarAsocios_"+cargosForm.getPosicion(),""); 
				
			}
			cargosForm.setCirugiasSolicitud("codigoTipoLiquidacion_"+cargosForm.getPosicion(),tarifaSOAT.getCodigoTipoLiquidacion());
			cargosForm.setCirugiasSolicitud("nombreTipoLiquidacion_"+cargosForm.getPosicion(),tarifaSOAT.getNombreTipoLiquidacion());
		}
		else if(cargosForm.getTarifarioOficial()==ConstantesBD.codigoTarifarioISS)
		{
			TarifaISS tarifaISS = new TarifaISS();
			tarifaISS.cargar(con,codigoServicio,esquemaTarifario, UtilidadFecha.getFechaActual());
			
			//se revisa si el UVR es correcto
			if(tarifaISS.getCodigoTipoLiquidacion()==ConstantesBD.codigoTipoLiquidacionSoatUvr)
			{
				if(tarifaISS.getUnidades()>0)
					cargosForm.setCirugiasSolicitud("grupoUvr_"+cargosForm.getPosicion(),UtilidadTexto.formatearValores(tarifaISS.getUnidades()+"","0.00"));
				else
					cargosForm.setCirugiasSolicitud("grupoUvr_"+cargosForm.getPosicion(),"");
				
				cargosForm.setCirugiasSolicitud("liquidarAsocios_"+cargosForm.getPosicion(),ConstantesBD.acronimoSi); //Por defecto SI aunque no aplica
			}
			//Se revisa si las UNIDADES O VALOR son correctas
			else if(tarifaISS.getCodigoTipoLiquidacion()==ConstantesBD.codigoTipoLiquidacionSoatUnidades||tarifaISS.getCodigoTipoLiquidacion()==ConstantesBD.codigoTipoLiquidacionSoatValor)
			{
				if(tarifaISS.getValorTarifa()>0)
					cargosForm.setCirugiasSolicitud("grupoUvr_"+cargosForm.getPosicion(),UtilidadTexto.formatearValores(tarifaISS.getValorTarifa(),"0.00"));
				else
					cargosForm.setCirugiasSolicitud("grupoUvr_"+cargosForm.getPosicion(),"");
				
				cargosForm.setCirugiasSolicitud("liquidarAsocios_"+cargosForm.getPosicion(),UtilidadTexto.getBoolean(tarifaISS.getLiquidarAsocios())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);						
			}
			//De lo contrario no hay parametrización
			else
			{
				cargosForm.setCirugiasSolicitud("grupoUvr_"+cargosForm.getPosicion(),"");
				cargosForm.setCirugiasSolicitud("liquidarAsocios_"+cargosForm.getPosicion(),"");
			}
			cargosForm.setCirugiasSolicitud("codigoTipoLiquidacion_"+cargosForm.getPosicion(),tarifaISS.getCodigoTipoLiquidacion());
			cargosForm.setCirugiasSolicitud("nombreTipoLiquidacion_"+cargosForm.getPosicion(),tarifaISS.getNombreTipoLiquidacion());
		}
		else
		{
			cargosForm.setCirugiasSolicitud("grupoUvr_"+cargosForm.getPosicion(),"");
			cargosForm.setCirugiasSolicitud("liquidarAsocios_"+cargosForm.getPosicion(),"");
			cargosForm.setCirugiasSolicitud("codigoTipoLiquidacion_"+cargosForm.getPosicion(),"");
			cargosForm.setCirugiasSolicitud("nombreTipoLiquidacion_"+cargosForm.getPosicion(),"");
		}
	}

	/**
	 * Método que abre el detalle de un servicio
	 * @param con
	 * @param cargosForm
	 * @param usuario
	 * @param mapping
	 * @param paciente 
	 * @return
	 * @throws IPSException 
	 */
	private ActionForward accionDetalleServicio(Connection con, CargosDirectosCxDytForm cargosForm, UsuarioBasico usuario, ActionMapping mapping, PersonaBasica paciente) throws IPSException 
	{
		int codigoServicio = Integer.parseInt(cargosForm.getCirugiasSolicitud("codigoServicio_"+cargosForm.getPosicion()).toString());
		
		//Si el esquema tarifario es nulo entonces se postula el de la cobertura
		if(cargosForm.getCirugiasSolicitud("codigoEsquemaTarifario_"+cargosForm.getPosicion())==null)
			this.consultaTarifaServicio(con, cargosForm, cargosForm.getInfoCobertura().getDtoSubCuenta().getEsquemaTarifarioServiciosPpalOoriginal(con,cargosForm.getInfoCobertura().getDtoSubCuenta().getSubCuenta(),cargosForm.getInfoCobertura().getDtoSubCuenta().getContrato(),codigoServicio,"", -1));
		
		//Se pregunta si el servicio eas de vía de acceso
		cargosForm.setCirugiasSolicitud("esViaAcceso_"+cargosForm.getPosicion(), UtilidadesSalas.esServicioViaAcceso(con, codigoServicio, usuario.getCodigoInstitucionInt())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		
		//Se verifica si ya había asocios para el servicio seleccionado
		if(UtilidadTexto.isEmpty(cargosForm.getCirugiasSolicitud("numProfesionales_"+cargosForm.getPosicion())+""))
			cargosForm.setCirugiasSolicitud("numProfesionales_"+cargosForm.getPosicion(), "0");
		
		//Se verifica si ya había diagnosticos relacionados
		if(UtilidadTexto.isEmpty(cargosForm.getCirugiasSolicitud("numDiagnosticos_"+cargosForm.getPosicion())+""))
			cargosForm.setCirugiasSolicitud("numDiagnosticos_"+cargosForm.getPosicion(), "0");
		
		//********VALDIACIONES CUANDO SE MANEJA RIPS*************************************************
		if(cargosForm.isEntidadManejaRips())
		{
			//Método para consultar las finalidades del servicio
			cargosForm.setFinalidades(Utilidades.obtenerFinalidadesServicio(con, codigoServicio, usuario.getCodigoInstitucionInt()));
			
			//Se verifica si es requerido el diagnosticos proncipal
			String tipoServicio = cargosForm.getCirugiasSolicitud("tipoServicio_"+cargosForm.getPosicion()).toString();
			if(tipoServicio.equals(ConstantesBD.codigoServicioNoCruentos+""))
				cargosForm.setCirugiasSolicitud("requiereDiagnostico_"+cargosForm.getPosicion(), UtilidadesFacturacion.esRequeridoDiagnosticoServicio(con, codigoServicio)?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
			else
				cargosForm.setCirugiasSolicitud("requiereDiagnostico_"+cargosForm.getPosicion(), ConstantesBD.acronimoSi);
		}
		
		/*
		if(UtilidadTexto.isEmpty(cargosForm.getCirugiasSolicitud("autorizacion_"+cargosForm.getPosicion())+"")&&!UtilidadTexto.isEmpty(cargosForm.getEncabezadoSolicitud("numeroAutorizacion")+""))
			cargosForm.setCirugiasSolicitud("autorizacion_"+cargosForm.getPosicion(), cargosForm.getEncabezadoSolicitud("numeroAutorizacion"));
		*/	
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalle");
	}

	/**
	 * Método implementado para eliminar un servicio
	 * @param con
	 * @param cargosForm
	 * @param response
	 * @param usuario 
	 * @return
	 */
	private ActionForward accionFiltroEliminarServicio(Connection con, CargosDirectosCxDytForm cargosForm, HttpServletResponse response, UsuarioBasico usuario) 
	{
		//Se consulta el primer servicio antes de eliminar
		int codigoServicioAntes = ConstantesBD.codigoNuncaValido;
		for(int i=0;i<cargosForm.getNumCirugias();i++)
			if(!UtilidadTexto.getBoolean(cargosForm.getCirugiasSolicitud("fueEliminado_"+i)+""))
			{
				codigoServicioAntes = Integer.parseInt(cargosForm.getCirugiasSolicitud("codigoServicio_"+i).toString());
				i = cargosForm.getNumCirugias();
			}
		
		//*****************************ELIMINACION DEL <TR> EN LA TABLA SERVICIOS***********************************
		//Se elimina el servicio
		cargosForm.setCirugiasSolicitud("fueEliminado_"+cargosForm.getIndex(), ConstantesBD.acronimoSi);
		
		//Se consulta el primer servicio antes de eliminar
		int codigoServicioDespues = ConstantesBD.codigoNuncaValido;
		for(int i=0;i<cargosForm.getNumCirugias();i++)
			if(!UtilidadTexto.getBoolean(cargosForm.getCirugiasSolicitud("fueEliminado_"+i)+""))
			{
				codigoServicioDespues = Integer.parseInt(cargosForm.getCirugiasSolicitud("codigoServicio_"+i).toString());
				i = cargosForm.getNumCirugias();
			}
		
		String resultado = "<respuesta>" +
			"<infoid>" +
				"<sufijo>ajaxBusquedaTipoEliminarTr</sufijo>" +
				"<id-tr>trServicios_"+cargosForm.getIndex()+"</id-tr>" + //posicion del TR a eliminar
				"<id-table>tablaServicios</id-table>"; //tabla
		//**********************************************************************************************************
		
		//********************MANEJO DE LA SECCION DE RECIEN NACIDOS***************************************************
		this.validacionRecienNacidos(cargosForm);
		
		resultado += "<sufijo>ajaxBusquedaSecciones</sufijo>" +
			"<id-seccion>habilitarInfoRecienNacidos</id-seccion>"+
			"<estado-seccion>"+(cargosForm.isHabilitarInfoRecienNacidos()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo)+"</estado-seccion>"+
			"<div-seccion>divHabRecienNacidos</div-seccion>"+
			"<es-boolean>"+ConstantesBD.acronimoSi+"</es-boolean>"+
			"<maneja-imagen>"+ConstantesBD.acronimoNo+"</maneja-imagen>";
		//*************************************************************************************************************
		
		//*******+Si ya no quedan servicios se debe borrar el combo de centros de costo y tipos anestesia**************
		// Nota * Si el primer servicio ha cambiado entonces se vuelven a consultar los centros de costo/tipos anestesia / anestesiologos
		if(cargosForm.getNumCirugiasReales()==0 || codigoServicioAntes!=codigoServicioDespues)
		{
			
			validarCentrosCosto(con, cargosForm, usuario, true);
				
			//centros costo
			resultado += "" +
				"<sufijo>ajaxBusquedaTipoSelect</sufijo>" +
				"<id-select>codigoCentroCosto</id-select>" +
				"<id-arreglo>centro-costo</id-arreglo>" ;
			
			//tipos anestesia
			resultado += "" +
				"<id-select>codigoTipoAnestesia</id-select>" +
				"<id-arreglo>tipo-anestesia</id-arreglo>";
			
			//anestesiologos
			resultado += ""+
				"<id-select>codigoAnestesiologo</id-select>" +
				"<id-arreglo>anestesiologo</id-arreglo>";
		}
		
		
		//***********Se reeditan los codigos insertados******************************************+
		String codigosServiciosInsertados = "";
		for(int i=0;i<cargosForm.getNumCirugias();i++)
		{
			//Mientras que no se haya eliminado el servicio se toma como existente
			if(!UtilidadTexto.getBoolean(cargosForm.getCirugiasSolicitud("fueEliminado_"+i)+""))
				codigosServiciosInsertados += cargosForm.getCirugiasSolicitud("codigoServicio_"+i)+",";
		}
		cargosForm.setCirugiasSolicitud("codigosServiciosInsertados", codigosServiciosInsertados);
		resultado += "" +
			"<sufijo>ajaxBusquedaTipoHidden</sufijo>" +
			"<id-hidden>codigosServiciosInsertados</id-hidden>" +
			"<contenido-hidden>"+codigosServiciosInsertados+"</contenido-hidden>" ;
		//*****************************************************************************************
		
		resultado +="</infoid>";
		
		//Si el primer servicio cambió se deben volver a llenar los centros de costo/tipos anestesia/anestesiologos
		if(codigoServicioAntes!=codigoServicioDespues&&cargosForm.getNumCirugiasReales()>0)
		{
			for(HashMap elemento:cargosForm.getCentrosCosto())
			{
				resultado += "<centro-costo>"+
					"<codigo>"+elemento.get("codigoCentroCosto")+"</codigo>"+
					"<descripcion>"+elemento.get("nombreCentroCosto")+" ("+elemento.get("codigoCentroCosto")+" - "+elemento.get("nombreCentroAtencion")+" ("+elemento.get("codigoCentroAtencion")+")</descripcion>" +
					"</centro-costo>";
			}
			
			for(HashMap elemento:cargosForm.getTiposAnestesia())
			{
				resultado += "<tipo-anestesia>"+
				"<codigo>"+elemento.get("codigo")+"</codigo>"+
				"<descripcion>"+elemento.get("nombre")+"</descripcion>" +
				"</tipo-anestesia>";
			}
			
			//Si el tipo de anestesia no es de hoja quirurgica entonces se muestran los anestesiolosos
			if(!UtilidadTexto.getBoolean(cargosForm.getDatosActoQx("mostrarTipoAnesEnHojaQx").toString()))
				for(HashMap elemento:cargosForm.getAnestesiologos())
				{
					resultado += "<anestesiologo>";
						resultado += "<codigo>"+elemento.get("codigo")+"</codigo>";
						resultado += "<descripcion>"+elemento.get("nombre")+"</descripcion>";
					resultado += "</anestesiologo>";
				}
		}
		
		resultado += "</respuesta>";
		UtilidadBD.closeConnection(con);
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
	        response.getWriter().write(resultado);
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionFiltroEliminarServicios: "+e);
		}
		return null;
	}

	/**
	 * Método implementado para agregar un nuevo servicio
	 * @param con
	 * @param cargosForm
	 * @param usuario
	 * @param mapping 
	 * @param paciente 
	 * @return
	 */
	private ActionForward accionAgregarServicio(Connection con, CargosDirectosCxDytForm cargosForm, UsuarioBasico usuario, ActionMapping mapping, PersonaBasica paciente) throws IPSException 
	{
		//Se re-edita la descripcion del servicio
		cargosForm.setCirugiasSolicitud("descripcionServicio_"+cargosForm.getNumCirugias(), 
			cargosForm.getCirugiasSolicitud("descripcionServicio_"+cargosForm.getNumCirugias())+" - "+cargosForm.getCirugiasSolicitud("esPos_"+cargosForm.getNumCirugias()));
		
		//Se aumenta en 1 el número de servicios del mapa
		cargosForm.setCirugiasSolicitud("numRegistros", (cargosForm.getNumCirugias()+1));
		
		//Se listan nuevamente los servicios insertados
		String codigosServiciosInsertados = "";
		for(int i=0;i<cargosForm.getNumCirugias();i++)
		{
			//Mientras que no se haya eliminado el servicio se toma como existente
			if(!UtilidadTexto.getBoolean(cargosForm.getCirugiasSolicitud("fueEliminado_"+i)+""))
				codigosServiciosInsertados += cargosForm.getCirugiasSolicitud("codigoServicio_"+i)+",";
		}
		cargosForm.setCirugiasSolicitud("codigosServiciosInsertados", codigosServiciosInsertados);
		
		//**********VALIDACION DEL CENTRO DE COSTO***********************************************
		this.validarCentrosCosto(con,cargosForm,usuario,false);
		//****************************************************************************************
		
		//*********VALIDACION DE LA COBERTURA**************************************************
		//Se verifica si no se ha calculado la Cobertura y el servicio es el principal
		if(cargosForm.getNumCirugiasReales()==1)
		{
			int codigoServicio = ConstantesBD.codigoNuncaValido;
			
			//Se obtiene el codigo del primer servicio
			for(int i=0;i<cargosForm.getNumCirugias();i++)
				if(!UtilidadTexto.getBoolean(cargosForm.getCirugiasSolicitud("fueEliminado_"+i)+""))
				{
					codigoServicio = Integer.parseInt(cargosForm.getCirugiasSolicitud("codigoServicio_"+i).toString());
					i = cargosForm.getNumCirugias();
				}
			//Se calcula la cobertura
			cargosForm.setInfoCobertura(Cobertura.validacionCoberturaServicio(
					con, 
					paciente.getCodigoIngreso()+"", 
					paciente.getCodigoUltimaViaIngreso(),
					paciente.getCodigoTipoPaciente(),
					codigoServicio, 
					usuario.getCodigoInstitucionInt(),
					false, "" /*subCuentaCoberturaOPCIONAL*/));
			
			cargosForm.setTarifarioOficial(EsquemaTarifario.obtenerTarifarioOficialXCodigoEsquemaTar(con, cargosForm.getInfoCobertura().getDtoSubCuenta().getEsquemaTarifarioServiciosPpalOoriginal(con,cargosForm.getInfoCobertura().getDtoSubCuenta().getSubCuenta(),cargosForm.getInfoCobertura().getDtoSubCuenta().getContrato(),codigoServicio,"", -1)));
			//Se llena el arreglo de los esquemas tarifarios
			cargosForm.setEsquemasTarifarios(Utilidades.obtenerEsquemasTarifariosInArray(false, usuario.getCodigoInstitucionInt(), cargosForm.getTarifarioOficial()));
			
			
			
		}
		//***************************************************************************************
		//********VERIFICAR SI SE DEBE MOSTRAR LA INFORMACIÓN DEL RECIÉN NACIDO*******************
		this.validacionRecienNacidos(cargosForm);
		//***************************************************************************************
		
		
		/**logger.info("MAPA CIRUGIA CARGOS DIRECTOS**********************************************");
		Utilidades.imprimirMapa(cargosForm.getCirugiasSolicitud());
		logger.info("**************************************************************************");**/
		UtilidadBD.closeConnection(con);
		return mapping.findForward("encabezado");
	}

	/**
	 * Método que verifica si se debe abrir la seccion de recién nacidos
	 * @param cargosForm
	 */
	private void validacionRecienNacidos(CargosDirectosCxDytForm cargosForm) 
	{
		boolean hayServicioPartos = false;
		for(int i=0;i<cargosForm.getNumCirugias();i++)
			if(!UtilidadTexto.getBoolean(cargosForm.getCirugiasSolicitud("fueEliminado_"+i)+""))
				if(cargosForm.getCirugiasSolicitud("tipoServicio_"+i).toString().equals(ConstantesBD.codigoServicioPartosCesarea+""))
					hayServicioPartos = true;
		if(hayServicioPartos&&cargosForm.isEntidadManejaRips())
		{
			cargosForm.setHabilitarInfoRecienNacidos(true);
			cargosForm.setSeccionRecienNacidos(true);
		}
		else
		{
			cargosForm.setHabilitarInfoRecienNacidos(false);
			cargosForm.setSeccionRecienNacidos(false);
		}
		
	}

	/**
	 * Método para validar los centros de costo solicitados al seleccionar el primer servicio
	 * @param con
	 * @param cargosForm
	 * @param usuario
	 * @param recalcular : para saber si se debe hacer
	 */
	@SuppressWarnings("rawtypes")
	private void validarCentrosCosto(Connection con, CargosDirectosCxDytForm cargosForm, UsuarioBasico usuario, boolean recalcular) 
	{
		//Cuando se agrega el primer servicio se realiza el filtro de los centros de costo
		if(cargosForm.getNumCirugiasReales()==1 || (recalcular && cargosForm.getNumCirugiasReales()>0))
		{
			//Se consulta el primer servicio
			int codigoServicio = ConstantesBD.codigoNuncaValido;
			for(int i=0;i<cargosForm.getNumCirugias();i++)
				if(!UtilidadTexto.getBoolean(cargosForm.getCirugiasSolicitud("fueEliminado_"+i)+""))
				{
					codigoServicio = Integer.parseInt(cargosForm.getCirugiasSolicitud("codigoServicio_"+i).toString());
					i = cargosForm.getNumCirugias();
				}
			
			//Se consulta el grupo del servicio
			int grupoServicio = UtilidadesFacturacion.consultarGrupoServicio(con, codigoServicio);
			int codigoCentroCosto = ConstantesBD.codigoNuncaValido;
			cargosForm.setCentrosCosto(UtilidadesFacturacion.consultarCentrosCostoGrupoServicio(con,grupoServicio,usuario.getCodigoCentroAtencion(),false,false));
			
			//Si solo había un centro de costo se consultan los tipos de anestesia
			if(cargosForm.getCentrosCosto().size()==1)
			{
				HashMap elemento = (HashMap)cargosForm.getCentrosCosto().get(0);
				codigoCentroCosto = Integer.parseInt(elemento.get("codigoCentroCosto").toString());
			}
			else
			{
				//Se verifica que si sólo había un centro de costo con el mismo centro de atencion de la sesión se postula
				int posicion = ConstantesBD.codigoNuncaValido;
				int numCentrosCostoMismoCentroAtencion = 0,cont = 0;
				
				for(HashMap elemento:cargosForm.getCentrosCosto())
				{
					if(Integer.parseInt(elemento.get("consecutivoCentroAtencion").toString())==usuario.getCodigoCentroAtencion())
					{
						posicion = cont;
						numCentrosCostoMismoCentroAtencion ++;
					}
					cont ++;
				}
				
				if(numCentrosCostoMismoCentroAtencion==1)
				{
					HashMap elemento = (HashMap)cargosForm.getCentrosCosto().get(posicion);
					codigoCentroCosto = Integer.parseInt(elemento.get("codigoCentroCosto").toString());
				}
				//------------------------------------------------------------------------
			}
			
			//Si ya se había postulado un centro de costo se asigna al mapa y se consultan los tipos de anestesia
			if(codigoCentroCosto>0)
			{
				cargosForm.setEncabezadoSolicitud("centroCostoSolicitado", codigoCentroCosto+"");
				cargosForm.setTiposAnestesia(UtilidadesSalas.obtenerTiposAnestesiaInstitucionCentroCosto(con, usuario.getCodigoInstitucionInt(), codigoCentroCosto));
				cargosForm.setDatosActoQx("codigoTipoAnestesia", "");
				
				if(cargosForm.getTiposAnestesia().size()==1)
				{
					HashMap elemento = (HashMap)cargosForm.getTiposAnestesia().get(0);
					cargosForm.setDatosActoQx("codigoTipoAnestesia", elemento.get("codigo"));
					cargosForm.setDatosActoQx("mostrarTipoAnesEnHojaQx", UtilidadesSalas.estaTipoAnestesiaEnMostrarHojaQx(con, Integer.parseInt(elemento.get("codigo").toString()))?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
				}
				else
					cargosForm.setDatosActoQx("mostrarTipoAnesEnHojaQx", "");
			}
			else
			{
				cargosForm.setTiposAnestesia(new ArrayList<HashMap<String,Object>>());
				cargosForm.setDatosActoQx("mostrarTipoAnesEnHojaQx", "");
			}
		}
		else if(cargosForm.getNumCirugiasReales()==0)
		{
			cargosForm.setCentrosCosto(new ArrayList<HashMap<String,Object>>());
			cargosForm.setTiposAnestesia(new ArrayList<HashMap<String,Object>>());
			cargosForm.setDatosActoQx("mostrarTipoAnesEnHojaQx", "");
		}
		logger.info("mostrarTipoAnesEnHojaQx: "+cargosForm.getDatosActoQx("mostrarTipoAnesEnHojaQx"));
	}

	/**
	 * Método para eliminar otro profesional
	 * @param con
	 * @param cargosForm
	 * @param response
	 * @return
	 */
	private ActionForward accionFiltroEliminarOtrosProfesionales(Connection con, CargosDirectosCxDytForm cargosForm, HttpServletResponse response) 
	{
		//Se elimina el otro profesional
		cargosForm.setOtrosProfesionales("fueEliminado_"+cargosForm.getIndex(), ConstantesBD.acronimoSi);
		
		String resultado = "<respuesta>" +
			"<infoid>" +
				"<sufijo>ajaxBusquedaTipoEliminarTr</sufijo>" +
				"<id-tr>trOtroProfesional_"+cargosForm.getIndex()+"</id-tr>" + //posicion del TR a eliminar
				"<id-table>tablaOtrosProfesionales</id-table>" + //tabla
			"</infoid>"+
			"</respuesta>";
		
		UtilidadBD.closeConnection(con);
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
	        response.getWriter().write(resultado);
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionFiltroEliminarOtrosProfesionales: "+e);
		}
		return null;
	}

	/**
	 * Método que llena el select de los asocios de un registro de otros profesionales
	 * @param con
	 * @param cargosForm
	 * @param usuario
	 * @param response
	 * @return
	 */
	private ActionForward accionFiltrarAsociosOtrosProfesionales(Connection con, CargosDirectosCxDytForm cargosForm, UsuarioBasico usuario, HttpServletResponse response) 
	{
		String resultado = "<respuesta>" +
		"<infoid>" +
			"<sufijo>ajaxBusquedaTipoSelect</sufijo>" +
			"<id-select>codigoAsocio_"+cargosForm.getIndex()+"</id-select>" +
			"<id-arreglo>asocio</id-arreglo>" + //nombre de la etiqueta de cada elemento
		"</infoid>";
		
		for(HashMap elemento:cargosForm.getAsocios())
		{
			resultado += "<asocio>";
				resultado += "<codigo>"+elemento.get("codigo")+"</codigo>";
				resultado += "<descripcion>"+elemento.get("nombre_asocio")+" ("+elemento.get("nombre_tipo_servicio")+")</descripcion>";
			resultado += "</asocio>";
		}
		
		resultado += "</respuesta>";
		
		UtilidadBD.closeConnection(con);
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
	        response.getWriter().write(resultado);
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionFiltrarAsociosOtrosProfesionales: "+e);
		}
		return null;
	}


	/**
	 * Método implementado para llenar el select de profesionales cuando se selecciona un asocio
	 * @param con
	 * @param cargosForm
	 * @param usuario
	 * @param response
	 * @return
	 */
	private ActionForward accionFiltrarOtrosProfesionales(Connection con, CargosDirectosCxDytForm cargosForm, UsuarioBasico usuario, HttpServletResponse response) 
	{
		String resultado = "<respuesta>" +
		"<infoid>" +
			"<sufijo>ajaxBusquedaTipoSelect</sufijo>" +
			"<id-select>codigoProfesional_"+cargosForm.getIndex()+"</id-select>" +
			"<id-arreglo>profesional</id-arreglo>" + //nombre de la etiqueta de cada elemento
		"</infoid>";
		
		for(HashMap elemento:cargosForm.getProfesionales())
		{
			resultado += "<profesional>";
				
				logger.info("-->"+elemento.get("codigo")+"<--");
				logger.info("-->"+elemento.get("nombre")+"<--");
				
				resultado += "<codigo>"+elemento.get("codigo").toString().trim()+"</codigo>";
				resultado += "<descripcion>"+elemento.get("nombre").toString().trim()+"</descripcion>";
			resultado += "</profesional>";
		}
		
		resultado += "</respuesta>";
		logger.info("XML");
		logger.info(resultado);
		
		UtilidadBD.closeConnection(con);
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
	        response.getWriter().write(resultado);
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionFiltrarOtrosProfesionales: "+e);
		}
		return null;
	}


	/**
	 * Método implementado para filtrar los anestesiologos dependiendo de la selección del tipo de anestesia
	 * @param con
	 * @param cargosForm
	 * @param usuario
	 * @param response
	 * @return
	 */
	private ActionForward accionFiltrarAnestesiologos(Connection con, CargosDirectosCxDytForm cargosForm, UsuarioBasico usuario, HttpServletResponse response) 
	{
		boolean mostrarTipoAnesEnHojaQx = UtilidadesSalas.estaTipoAnestesiaEnMostrarHojaQx(con, Integer.parseInt(cargosForm.getIndex()));
		cargosForm.setDatosActoQx("mostrarTipoAnesEnHojaQx", mostrarTipoAnesEnHojaQx?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		
		String resultado = "<respuesta>" +
			"<infoid>" +
				"<sufijo>ajaxBusquedaTipoSelect</sufijo>" +
				"<id-select>codigoAnestesiologo</id-select>" +
				"<id-arreglo>anestesiologo</id-arreglo>" + //nombre de la etiqueta de cada elemento
			"</infoid>";
		
		//Si el tipo de anestesia no es de hoja quirurgica entonces se muestran los anestesiolosos
		if(!UtilidadTexto.getBoolean(cargosForm.getDatosActoQx("mostrarTipoAnesEnHojaQx").toString()))
			for(HashMap elemento:cargosForm.getAnestesiologos())
			{
				resultado += "<anestesiologo>";
					resultado += "<codigo>"+elemento.get("codigo")+"</codigo>";
					resultado += "<descripcion>"+elemento.get("nombre")+"</descripcion>";
				resultado += "</anestesiologo>";
			}
		
		resultado += "</respuesta>";
		
		UtilidadBD.closeConnection(con);
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
	        response.getWriter().write(resultado);
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionFiltrarAnestesiologos: "+e);
		}
		return null;
	}


	/**
	 * Método implementado para realizar el filtro de las salas
	 * @param con
	 * @param cargosForm
	 * @param usuario
	 * @param response
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private ActionForward accionFiltrarSalas(Connection con, CargosDirectosCxDytForm cargosForm, UsuarioBasico usuario, HttpServletResponse response) 
	{
		//Se consultan las salas
		cargosForm.setSalas(UtilidadesSalas.obtenerSalas(con, usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion(), ConstantesBD.acronimoSi, cargosForm.getIndex()+""));
		
		String resultado = "<respuesta>" +
			"<infoid>" +
				"<sufijo>ajaxBusquedaTipoSelect</sufijo>" +
				"<id-select>codigoSala</id-select>" +
				"<id-arreglo>sala</id-arreglo>" + //nombre de la etiqueta de cada elemento
			"</infoid>";
		
		for(HashMap elemento:cargosForm.getSalas())
		{
			resultado += "<sala>";
				resultado += "<codigo>"+elemento.get("consecutivo")+"</codigo>";
				resultado += "<descripcion>"+elemento.get("descripcion")+"</descripcion>";
			resultado += "</sala>";
		}
		
		resultado += "</respuesta>";
		
		UtilidadBD.closeConnection(con);
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
	        response.getWriter().write(resultado);
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionFiltrarSalas: "+e);
		}
		return null;
	}


	/**
	 * Método que inicia el flujo de la funcionalidad de Cargos Directos x cirugias y Dyt
	 * @param con
	 * @param cargosForm
	 * @param mapping
	 * @param paciente 
	 * @param usuario 
	 * @param request 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private ActionForward accionEmpezar(Connection con, CargosDirectosCxDytForm cargosForm, ActionMapping mapping, PersonaBasica paciente, UsuarioBasico usuario, HttpServletRequest request) 
	{
		cargosForm.reset();
		//**************VALIDACIONES INICIALES******************************************************
		ActionErrors errores = new ActionErrors();
		RespuestaValidacion respuesta = UtilidadValidacion.esValidoPacienteCargado(con, paciente);
		if(!respuesta.puedoSeguir)
			errores.add("",new ActionMessage(respuesta.textoRespuesta));
		
		/**
		 * Validar concurrencia
		 * Si ya está en proceso de facturación, no debe dejar entrar
		 **/
		if(UtilidadValidacion.estaEnProcesofacturacion(con, paciente.getCodigoPersona(), "") )
			errores.add("",new ActionMessage("error.facturacion.cuentaEnProcesoFact"));
		
		//Se valida el parámetros de especialidad de anestesiologia
		if(UtilidadTexto.isEmpty(ValoresPorDefecto.getEspecialidadAnestesiologia(usuario.getCodigoInstitucionInt(), true)))
			errores.add("", new ActionMessage("error.parametrosGenerales.faltaDefinirParametro","Especialidad Anestesiología"));
		//Se valida que se haya parametrizado el asocio de cirujano
		if(!UtilidadCadena.noEsVacio(ValoresPorDefecto.getAsocioCirujano(usuario.getCodigoInstitucionInt())))
			errores.add("", new ActionMessage("error.parametrosGenerales.faltaDefinirParametro","Asocio Cirujano"));
		//Se valida que se haya parametrizado el asocio de anestesiología
		if(!UtilidadCadena.noEsVacio(ValoresPorDefecto.getAsocioAnestesia(usuario.getCodigoInstitucionInt())))
			errores.add("", new ActionMessage("error.parametrosGenerales.faltaDefinirParametro","Asocio Anestesia"));
		//Se valida que se haya parametrizado el asocio de ayudante
		if(!UtilidadCadena.noEsVacio(ValoresPorDefecto.getAsocioAyudantia(usuario.getCodigoInstitucionInt())))
			errores.add("", new ActionMessage("error.parametrosGenerales.faltaDefinirParametro","Asocio Ayudantía"));
		
			
		if(!errores.isEmpty())
		{
			UtilidadBD.closeConnection(con);
			saveErrors(request, errores);
			return mapping.findForward("paginaErroresActionErrors");
		}
		//*******************************************************************************************
		
		
		//Se pregunta si el paciente es de entidad subcontratyadas
		if(paciente.esIngresoEntidadSubcontratada())
		{
			ElementoApResource advertencia01 = new ElementoApResource("errors.paciente.mensajeSubcontratacion");
			advertencia01.agregarAtributo(EntidadesSubContratadas.getDescripcionEntidadSubXIngreso(con, paciente.getCodigoIngreso()+""));
			cargosForm.getAdvertencias().add(advertencia01);
		}
		
		//Se postula la información inicial
		this.postularInformacionInicial(con,cargosForm,usuario,paciente);
		
		
		//Si existe asocio completo se debe solicitar la cuenta a la cual se desea realizar el cargo directo
		if(paciente.getExisteAsocio()&&paciente.getCodigoCuentaAsocio()>0)
		{
			HashMap<String, Object> elemento01 = new HashMap<String, Object>();
			elemento01.put("nombreViaIngreso", Utilidades.obtenerNombreViaIngreso(con, Integer.parseInt(Utilidades.obtenerViaIngresoCuenta(con, paciente.getCodigoCuentaAsocio()+""))));
			elemento01.put("codigoCuenta",paciente.getCodigoCuentaAsocio());
			
			cargosForm.getCuentasAsocio().add(elemento01);
			
			HashMap<String, Object> elemento02 = new HashMap<String, Object>();
			elemento02.put("nombreViaIngreso", Utilidades.obtenerNombreViaIngreso(con, paciente.getCodigoUltimaViaIngreso()));
			elemento02.put("codigoCuenta",paciente.getCodigoCuenta());
			
			cargosForm.getCuentasAsocio().add(elemento02);
			
			
			UtilidadBD.closeConnection(con);
			return mapping.findForward("seleccionCuenta");
		}
		else
		{
			cargosForm.setIdCuenta(paciente.getCodigoCuenta()+"");
			UtilidadBD.closeConnection(con);
			return mapping.findForward("encabezado");
		}
	}


	/**
	 * Método implementado para postular la información inicial
	 * @param con
	 * @param cargosForm
	 * @param usuario
	 * @param paciente 
	 */
	@SuppressWarnings("unchecked")
	private void postularInformacionInicial(Connection con, CargosDirectosCxDytForm cargosForm, UsuarioBasico usuario, PersonaBasica paciente) 
	{
		cargosForm.setFechaSistema(UtilidadFecha.getFechaActual(con));
		cargosForm.setHoraSistema(UtilidadFecha.getHoraActual(con));
		cargosForm.setEncabezadoSolicitud("fechaSolicitud", cargosForm.getFechaSistema());
		cargosForm.setEncabezadoSolicitud("horaSolicitud", cargosForm.getHoraSistema());
		cargosForm.setDatosActoQx("fechaInicialCx", cargosForm.getEncabezadoSolicitud("fechaSolicitud"));
		cargosForm.setDatosActoQx("horaInicialCx", cargosForm.getEncabezadoSolicitud("horaSolicitud"));
		cargosForm.setDatosActoQx("fechaFinalCx", cargosForm.getDatosActoQx("fechaInicialCx"));
		cargosForm.setDatosActoQx("horaFinalCx", cargosForm.getDatosActoQx("horaInicialCx"));
		cargosForm.setDatosActoQx("fechaIngresoSala", cargosForm.getDatosActoQx("fechaInicialCx"));
		cargosForm.setDatosActoQx("horaIngresoSala", cargosForm.getDatosActoQx("horaInicialCx"));
		cargosForm.setDatosActoQx("fechaSalidaSala", cargosForm.getDatosActoQx("fechaIngresoSala"));
		cargosForm.setDatosActoQx("horaSalidaSala", cargosForm.getDatosActoQx("horaIngresoSala"));
		
		cargosForm.setDatosActoQx("politraumatismo", ConstantesBD.acronimoNo);
		cargosForm.setDatosActoQx("mostrarTipoAnesEnHojaQx", "");
		cargosForm.setDatosActoQx("codigoTipoAnestesia", "");
		cargosForm.setDatosActoQx("codigoAnestesiologo", "");
		cargosForm.setDatosActoQx("cobrarAnestesia", ConstantesBD.acronimoSi);
		
		cargosForm.setTiposSala(Utilidades.obtenerTiposSala(con, usuario.getCodigoInstitucionInt(), ConstantesBD.acronimoSi, ConstantesBD.acronimoNo));
		cargosForm.setAnestesiologos(UtilidadesAdministracion.obtenerProfesionales(con, usuario.getCodigoInstitucionInt(), Integer.parseInt(ValoresPorDefecto.getEspecialidadAnestesiologia(usuario.getCodigoInstitucionInt(),true)), true, true, ConstantesBD.codigoNuncaValido));
		cargosForm.setAsocios(HojaQuirurgica.obtenerAsociosDifAyuCirAnes(con, usuario.getCodigoInstitucionInt()));
		cargosForm.setProfesionales(UtilidadesAdministracion.obtenerProfesionales(con, usuario.getCodigoInstitucionInt(), ConstantesBD.codigoNuncaValido, true, true, ConstantesBD.codigoNuncaValido));
		cargosForm.setEspecialidades(Utilidades.obtenerEspecialidadesEnArray(con, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido));
		cargosForm.setAsociosServicio(Utilidades.obtenerAsocios(con, "", ConstantesBD.codigoServicioHonorariosCirugia+"", ConstantesBD.acronimoSi));
		
		cargosForm.setCodigoAsocioCirujano(Integer.parseInt(ValoresPorDefecto.getAsocioCirujano(usuario.getCodigoInstitucionInt())));
		
		cargosForm.setEntidadManejaRips(UtilidadTexto.getBoolean(ValoresPorDefecto.getEntidadManejaRips(usuario.getCodigoInstitucionInt())));
		cargosForm.setRequeridaInformacionRips(UtilidadTexto.getBoolean(ValoresPorDefecto.getRequeridaInfoRipsCagosDirectos(usuario.getCodigoInstitucionInt())));
		
		cargosForm.setOtrosProfesionales("numRegistros", "0");
		cargosForm.setCirugiasSolicitud("numRegistros", "0");
		cargosForm.setCirugiasSolicitud("codigosServiciosInsertados", "");
		
		cargosForm.setCodigoSexo(paciente.getCodigoSexo()); //se usa para la busqueda genérica de servicios
		
		cargosForm.setSexos(Utilidades.obtenerSexos(con)); //Obtener los sexos en un arraylist
		
		cargosForm.setFechaIngreso(paciente.getFechaIngreso());
		cargosForm.setHoraIngreso(paciente.getHoraIngreso());
		
		
	}
	
	/**
	 * Método que se encarga de validar si se generar autorizacion para el servicio de la orden.
	 * DCU 551 - Cargos Directos de Cirugias v2.1
	 * MT 4681
	 * 
	 * @author Camilo Gomez
	 * @param con
	 * @param solicitarForm
	 * @param usuario
	 * @param errores
	 * @throws IPSException
	 */
	private void cargarInfoVerificarGeneracionAutorizacion(Connection con,CargosDirectosCxDytForm cxForm,
			UsuarioBasico usuario, PersonaBasica paciente, ActionErrors errores)throws IPSException
	{
		DtoSubCuentas dtoSubCuenta = null;
		AutorizacionCapitacionDto autorizacionCapitacionDto = null;
		MontoCobroDto montoCobroAutorizacion				= null;
		DatosPacienteAutorizacionDto datosPacienteAutorizar = null;
		OrdenAutorizacionDto ordenAutorizacionDto 			= null;
		List<OrdenAutorizacionDto> listaOrdenesAutorizar	= null;
		List<ServicioAutorizacionOrdenDto> listaServiciosPorAutorizar = null;
		ContratoDto contratoDto = null;
		ConvenioDto convenioDto = null;
		DtoSolicitudesSubCuenta dtoSolicitudSubCuenta = null; 
		ManejoPacienteFacade manejoPacienteFacade = null;
		OrdenesFacade ordenesFacade	= null;
		List<AutorizacionCapitacionDto> listaAutorizacionCapitacion = null;
		try{
			UtilidadBD.iniciarTransaccion(con);
			listaOrdenesAutorizar 	= new ArrayList<OrdenAutorizacionDto>();
			ordenesFacade			= new OrdenesFacade();
						
			if(cxForm.getListaInfoRespoCoberturaCx() != null && !cxForm.getListaInfoRespoCoberturaCx().isEmpty()) {
				
				dtoSubCuenta =  cxForm.getListaInfoRespoCoberturaCx().get(0).getDtoSubCuenta();
				dtoSolicitudSubCuenta = cxForm.getListaInfoRespoCoberturaCx().get(0).getDtoSubCuenta().getSolicitudesSubcuenta().get(0);
				
				convenioDto = new ConvenioDto();
				convenioDto.setCodigo(cxForm.getListaInfoRespoCoberturaCx().get(0).getDtoSubCuenta().getConvenio().getCodigo());
				convenioDto.setNombre(cxForm.getListaInfoRespoCoberturaCx().get(0).getDtoSubCuenta().getConvenio().getNombre());
				if(cxForm.getListaInfoRespoCoberturaCx().get(0).getDtoSubCuenta().getDatosConvenio().getManejaPreupuestoCapitacion()!=null &&
						cxForm.getListaInfoRespoCoberturaCx().get(0).getDtoSubCuenta().getDatosConvenio().getManejaPreupuestoCapitacion().equals(ConstantesBD.acronimoSiChar+"")){
					convenioDto.setConvenioManejaPresupuesto(true);
				}else{
					convenioDto.setConvenioManejaPresupuesto(false);
				}
				contratoDto = new ContratoDto();
				contratoDto.setConvenio(convenioDto);
				contratoDto.setCodigo(cxForm.getListaInfoRespoCoberturaCx().get(0).getDtoSubCuenta().getContrato());
				contratoDto.setNumero(cxForm.getListaInfoRespoCoberturaCx().get(0).getDtoSubCuenta().getNumeroContrato());
				
				ordenAutorizacionDto = new OrdenAutorizacionDto();
				ordenAutorizacionDto.setCodigoOrden(Utilidades.convertirALong(dtoSolicitudSubCuenta.getNumeroSolicitud()+""));
				//ordenAutorizacionDto.setOtroCodigoOrden(Utilidades.convertirALong(cxForm.getCodigoPeticion()+"")); 
				ordenAutorizacionDto.setConsecutivoOrden(dtoSolicitudSubCuenta.getConsecutivoSolicitud());
				ordenAutorizacionDto.setContrato(contratoDto);
				ordenAutorizacionDto.setCodigoCentroCostoEjecuta(dtoSolicitudSubCuenta.getCentroCostoEjecuta().getCodigo());
				ordenAutorizacionDto.setEsPyp(cxForm.isSolPYP());
				ordenAutorizacionDto.setCodigoViaIngreso(paciente.getCodigoUltimaViaIngreso());
				ordenAutorizacionDto.setClaseOrden(ConstantesBD.claseOrdenCargoDirecto);
				ordenAutorizacionDto.setTipoOrden(ConstantesBD.codigoTipoSolicitudCirugia);
				
				//Se consultan datos del servicio
				listaServiciosPorAutorizar = null;
				listaServiciosPorAutorizar = ordenesFacade.obtenerServiciosPorAutorizar(Utilidades.convertirAEntero(ordenAutorizacionDto.getCodigoOrden()+""),
						ordenAutorizacionDto.getClaseOrden(), ordenAutorizacionDto.getTipoOrden());
				for(ServicioAutorizacionOrdenDto cirugia: listaServiciosPorAutorizar){
					cirugia.setAutorizar(true);
				}
				if(dtoSolicitudSubCuenta.isUrgenteSolicitud()){
					ordenAutorizacionDto.setEsUrgente(true);
					listaServiciosPorAutorizar.get(0).setUrgente(ConstantesBD.acronimoSiChar);
				}else{
					ordenAutorizacionDto.setEsUrgente(false);
					listaServiciosPorAutorizar.get(0).setUrgente(ConstantesBD.acronimoNoChar);
				}
				
				ordenAutorizacionDto.setServiciosPorAutorizar(listaServiciosPorAutorizar);
				listaOrdenesAutorizar.add(ordenAutorizacionDto);
			}	
			boolean manejaMonto = UtilidadTexto.isEmpty(dtoSubCuenta.getMontoCobro()) || dtoSubCuenta.getMontoCobro() == 0 ? false : true;
			
			datosPacienteAutorizar	= new DatosPacienteAutorizacionDto();
			datosPacienteAutorizar.setCodigoPaciente(dtoSubCuenta.getCodigoPaciente());
			datosPacienteAutorizar.setCodigoIngresoPaciente(dtoSubCuenta.getIngreso());
			datosPacienteAutorizar.setTipoPaciente(dtoSubCuenta.getTipoPaciente());
			datosPacienteAutorizar.setClasificacionSocieconomica(dtoSubCuenta.getClasificacionSocioEconomica());
			datosPacienteAutorizar.setTipoAfiliado(dtoSubCuenta.getTipoAfiliado());
			datosPacienteAutorizar.setNaturalezaPaciente(dtoSubCuenta.getNaturalezaPaciente());
			datosPacienteAutorizar.setCodConvenioCuenta(paciente.getCodigoConvenio());
			datosPacienteAutorizar.setCuentaAbierta(true);
			datosPacienteAutorizar.setCuentaManejaMontos(manejaMonto);
			datosPacienteAutorizar.setPorcentajeMontoCuenta(dtoSubCuenta.getPorcentajeMontoCobro());
			
			montoCobroAutorizacion	= new MontoCobroDto();
			montoCobroAutorizacion.setCantidadMonto(dtoSubCuenta.getCantidadMontoGeneral());
			montoCobroAutorizacion.setCodDetalleMonto(dtoSubCuenta.getMontoCobro());
			montoCobroAutorizacion.setPorcentajeMonto(dtoSubCuenta.getPorcentajeMontoGeneral());
			montoCobroAutorizacion.setTipoDetalleMonto(dtoSubCuenta.getTipoDetalleMonto());
			montoCobroAutorizacion.setTipoMonto(dtoSubCuenta.getTipoMonto());
			montoCobroAutorizacion.setValorMonto(dtoSubCuenta.getValorMontoGeneral());
			
			autorizacionCapitacionDto = new AutorizacionCapitacionDto();
			autorizacionCapitacionDto.setCodigoInstitucion(usuario.getCodigoInstitucionInt());
			autorizacionCapitacionDto.setLoginUsuario(usuario.getLoginUsuario());
			autorizacionCapitacionDto.setCodigoPersonaUsuario(usuario.getCodigoPersona());
			autorizacionCapitacionDto.setTipoAutorizacion(ConstantesIntegridadDominio.acronimoAutomatica);
			autorizacionCapitacionDto.setCentroAtencion(usuario.getCodigoCentroAtencion());
			autorizacionCapitacionDto.setDatosPacienteAutorizar(datosPacienteAutorizar);
			autorizacionCapitacionDto.setMontoCobroAutorizacion(montoCobroAutorizacion);
			autorizacionCapitacionDto.setOrdenesAutorizar(listaOrdenesAutorizar);
			
			//Proceso que se encarga de verificar si se generara o asociara autorizacion para la orden.
			manejoPacienteFacade = new ManejoPacienteFacade();
			listaAutorizacionCapitacion = manejoPacienteFacade.verificarGenerarAutorizacionCapitacion(autorizacionCapitacionDto);
			
			
			if(listaAutorizacionCapitacion!=null && !listaAutorizacionCapitacion.isEmpty())
			{	//Se adiciona mensaje para los servicio que no se autorizaron
				manejoPacienteFacade.obtenerMensajesError(listaAutorizacionCapitacion, errores);
			}
			UtilidadBD.finalizarTransaccion(con);
		}catch (IPSException ipsme) {
			UtilidadBD.abortarTransaccion(con);
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			UtilidadBD.abortarTransaccion(con);
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
	}
}
