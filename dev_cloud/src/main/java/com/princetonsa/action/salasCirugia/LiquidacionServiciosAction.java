package com.princetonsa.action.salasCirugia;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ElementoApResource;
import util.InfoDatosInt;
import util.Listado;
import util.RespuestaValidacion;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.Administracion.UtilidadesAdministracion;
import util.facturacion.UtilidadesFacturacion;
import util.salas.ConstantesBDSalas;
import util.salas.UtilidadesSalas;

import com.princetonsa.actionform.salasCirugia.LiquidacionServiciosForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.ordenesmedicas.cirugias.HojaQuirurgica;
import com.princetonsa.mundo.salasCirugia.LiquidacionServicios;
import com.princetonsa.pdf.LiquidacionServiciosPdf;
import com.servinte.axioma.fwk.exception.IPSException;

public class LiquidacionServiciosAction extends Action 
{

	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(LiquidacionServiciosAction.class);
	
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
		if(form instanceof LiquidacionServiciosForm)
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
			LiquidacionServiciosForm liquidacionForm =(LiquidacionServiciosForm)form;
			HttpSession session=request.getSession();		
			UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
			PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
			String estado=liquidacionForm.getEstado(); 
			logger.warn("estado LiquidacionServiciosAction-->"+estado);
						
			
			if(estado == null)
			{
				liquidacionForm.reset();	
				logger.warn("Estado no valido dentro del flujo de LiquidacionServicios (null) ");
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}
			//***************ESTADOS PARA EL FLUJO DE LISTADO DE ORDENES****************************************************
			else if (estado.equals("empezar"))
			{
				return accionEmpezar(con,liquidacionForm,mapping,usuario,paciente,request);
			}
			else if (estado.equals("ordenarListado"))
			{
				return accionOrdenarListado(con,liquidacionForm,mapping);
			}
			//**********ESTADOS RELACIONADOS CON EL DETALLE DE LA ORDEN*********************************************************
			else if (estado.equals("detalleOrden"))
			{
				return accionDetalleOrden(con,liquidacionForm,mapping,usuario,paciente);
			}
			else if (estado.equals("volverDetalleOrden"))
			{
				return accionVolverDetalleOrden(con,liquidacionForm,mapping,usuario);	
			}
			else if (estado.equals("guardar"))
			{
				return accionGuardar(con,liquidacionForm,mapping,usuario,paciente,request);
			}
			else if (estado.equals("liquidar"))
			{
				return accionLiquidar(con,liquidacionForm,mapping,usuario,paciente,request);
			}
			else if (estado.equals("imprimir"))
			{
				return accionImprimir(con,liquidacionForm,mapping,usuario,paciente,request);
			}
			//***********ESTADOS RELACIONADOS CON EL LISTADO DE SERVICIOS DE LA ORDEN (IFRAME)*******************************
			else if (estado.equals("agregarServicio"))
			{
				return accionAgregarServicio(con,liquidacionForm,mapping,usuario,paciente);
			}
			else if (estado.equals("eliminarServicio"))
			{
				return accionEliminarServicio(con,liquidacionForm,mapping,usuario,paciente);
			}
			else if (estado.equals("recargaDetalleServicio")) //Se recarga el frame de servicios para llenar la forma con los datos de la cirugia
			{
				UtilidadBD.closeConnection(con);
				return mapping.findForward("frameServicios");
			}
			else if (estado.equals("detalleServicio")) //Se recarga la pagina principal para mostrar el detalle de un servicio
			{
				return accionDetalleServicio(con,liquidacionForm,mapping,usuario,paciente);	
			}
			else if (estado.equals("cambioServicio")) //Cuando se cambia algun dato del servicio se llama de nuevo la reordenaxción de servicios
			{
				return accionCambioServicio(con,liquidacionForm,mapping,usuario,paciente);
			}
			else if (estado.equals("guardarDetalleServicio"))
			{
				return accionGuardarDetalleServicio(con,liquidacionForm,mapping,usuario,request,paciente);
			}
			else if (estado.equals("guardarFrameServicios")||estado.equals("liquidarFrameServicios")) //método usado para recargar cambios del iframe de servicios
			{
				UtilidadBD.closeConnection(con);
				return mapping.findForward("frameServicios");
			}
			//*************ESTADOS PARA FILTROS AJAX***********************************************************************
			else if (estado.equals("filtrarSalas"))
			{
				return accionFiltrarSalas(con,liquidacionForm,usuario,response);
			}
			/////anexo 777
			else if(estado.equals("filtrarMedicoSala"))
			{
				return accionFiltrarMedicoSala(con, liquidacionForm, response);
			}
			else if (estado.equals("filtrarDatosProfesional"))
			{
				return accionFiltrarDatosProfesional(con,liquidacionForm,response);
			}
			else if (estado.equals("filtrarAdicionarOtroProfesional"))
			{
				return accionFiltrarAdicionarOtroProfesional(con,liquidacionForm,response);
			}
			else if (estado.equals("filtroEliminarOtrosProfesionales"))
			{
				return accionFiltroEliminarOtrosProfesionales(con,liquidacionForm,response);
			}
			else if (estado.equals("filtrarAdicionarProfesionalServicio"))
			{
				return accionFiltrarAdicionarProfesionalServicio(con,liquidacionForm,response);
			}
			else if (estado.equals("filtroEliminarProfesionalServicio"))
			{
				return accionFiltroEliminarProfesionalServicio(con,liquidacionForm,response);
			}
			//************************************************************************************************************
			else
			{
				liquidacionForm.reset();
				logger.warn("Estado no valido dentro del flujo de LiquidacionServiciosAction (null) ");
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}
		}
		}catch (Exception e) {
			Log4JManager.error("Error liquidando",e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return null;	
	}

	

	/**
	 * Método para eliminar un profesional de un servicio
	 * @param con
	 * @param liquidacionForm
	 * @param response
	 * @return
	 */
	private ActionForward accionFiltroEliminarProfesionalServicio(Connection con, LiquidacionServiciosForm liquidacionForm, HttpServletResponse response) 
	{
		//Se elimina el profesional de la cirugia
		liquidacionForm.setCirugiasSolicitud("eliminar_"+liquidacionForm.getIndexAjax(), ConstantesBD.acronimoSi);
		
		String resultado = "<respuesta>" +
			"<infoid>" +
				"<sufijo>ajaxBusquedaTipoEliminarTr</sufijo>" +
				"<id-tr>trAsocioServicio_"+liquidacionForm.getIndexAjax().split("_")[1]+"</id-tr>" + //posicion del TR a eliminar
				"<id-table>tablaAsociosServicio</id-table>" + //tabla
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
			logger.error("Error al enviar respuesta AJAX en accionFiltroEliminarProfesionalServicio: "+e);
		}
		return null;
	}



	/**
	 * Método implementado para adicionar el profesional de un servicio
	 * @param con
	 * @param liquidacionForm
	 * @param response
	 * @return
	 */
	private ActionForward accionFiltrarAdicionarProfesionalServicio(Connection con, LiquidacionServiciosForm liquidacionForm, HttpServletResponse response) 
	{
		//Se toma la posicion de la cirugía seleccionada
		int posicion = Utilidades.convertirAEntero(liquidacionForm.getCirugiasSolicitud("posicion").toString());
		int pos = Utilidades.convertirAEntero(liquidacionForm.getCirugiasSolicitud("numProfesionales_"+posicion).toString());
		
		//Se insertan todos los índices del nuevo registro
		for(int i=0;i<LiquidacionServicios.indicesProfesionalesCirugia.length;i++)
			liquidacionForm.setCirugiasSolicitud(LiquidacionServicios.indicesProfesionalesCirugia[i]+posicion+"_"+pos, "");
		
		//Se ponen los índices por defecto
		liquidacionForm.setCirugiasSolicitud("historiaClinica_"+posicion+"_"+pos, ConstantesBD.acronimoNo);
		liquidacionForm.setCirugiasSolicitud("existeBd_"+posicion+"_"+pos, ConstantesBD.acronimoNo);
		liquidacionForm.setCirugiasSolicitud("eliminar_"+posicion+"_"+pos, ConstantesBD.acronimoNo);
		liquidacionForm.setCirugiasSolicitud("esAsocioCirujano_"+posicion+"_"+pos, ConstantesBD.acronimoNo);
		//Se añaden los combos
		liquidacionForm.setCirugiasSolicitud("comboProfesionales_"+posicion+"_"+pos, liquidacionForm.getProfesionales());
		liquidacionForm.setCirugiasSolicitud("comboEspecialidades_"+posicion+"_"+pos, new ArrayList<HashMap<String, Object>>());
		liquidacionForm.setCirugiasSolicitud("comboPooles_"+posicion+"_"+pos, new ArrayList<HashMap<String, Object>>());
		
		
		liquidacionForm.setCirugiasSolicitud("numProfesionales_"+posicion, (pos+1));
		
		String resultado = "<respuesta>" +
		"<infoid>" +
			"<sufijo>ajaxAdicionarProfesionalServicio</sufijo>" +
			"<posicion>"+posicion+"</posicion>" +
			"<pos>"+pos+"</pos>" +
			"<id-arreglo>asocio</id-arreglo>" + //nombre de la etiqueta de cada elemento
			"<id-arreglo>profesional</id-arreglo>" + //nombre de la etiqueta de cada elemento
		"</infoid>";
	
		for(HashMap elemento:liquidacionForm.getAsociosServicio())
		{
			String opcion= UtilidadTexto.isEmpty(elemento.get("centro_costo_ejecuta")+"")?elemento.get("nombre_asocio")+"":elemento.get("nombre_asocio")+"- CC Ejecuta: "+elemento.get("centro_costo_ejecuta");
			
			resultado += "<asocio>";
				resultado += "<codigo>"+elemento.get("codigo")+"</codigo>";
				resultado += "<descripcion>"+opcion+"</descripcion>";
			resultado += "</asocio>";
		}
		
		for(HashMap elemento:liquidacionForm.getProfesionales())
		{
			resultado += "<profesional>";
				resultado += "<codigo>"+elemento.get("codigo")+"</codigo>";
				resultado += "<descripcion>"+elemento.get("nombre")+"</descripcion>";
			resultado += "</profesional>";
		}
		
		resultado += "</respuesta>";
		
		UtilidadBD.closeConnection(con);
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
	        response.getWriter().write(resultado);
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionFiltrarAdicionarProfesionalServicio: "+e);
		}
		return null;
	}



	/**
	 * Método para eliminar otro profesional por método ajax
	 * @param con
	 * @param liquidacionForm
	 * @param response
	 * @return
	 */
	private ActionForward accionFiltroEliminarOtrosProfesionales(Connection con, LiquidacionServiciosForm liquidacionForm, HttpServletResponse response) 
	{
		//Se elimina el otro profesional
		liquidacionForm.setOtrosProfesionales("eliminar_"+liquidacionForm.getIndexAjax(), ConstantesBD.acronimoSi);
		
		String resultado = "<respuesta>" +
			"<infoid>" +
				"<sufijo>ajaxBusquedaTipoEliminarTr</sufijo>" +
				"<id-tr>trOtrosProfesionales_"+liquidacionForm.getIndexAjax()+"</id-tr>" + //posicion del TR a eliminar
				"<id-table>tablaOtrosProfesionales</id-table>" + //tabla
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
			logger.error("Error al enviar respuesta AJAX en accionFiltroEliminarOtrosProfesionales: "+e);
		}
		return null;
	}



	/**
	 * Método implementado para adicionar otro profesional por método de ajax
	 * @param con
	 * @param liquidacionForm
	 * @param response
	 * @return
	 */
	private ActionForward accionFiltrarAdicionarOtroProfesional(Connection con, LiquidacionServiciosForm liquidacionForm, HttpServletResponse response) 
	{
		int pos = liquidacionForm.getNumOtrosProfesionales();
		
		liquidacionForm.setOtrosProfesionales("consecutivo_"+pos,"");
		liquidacionForm.setOtrosProfesionales("numeroSolicitud_"+pos,liquidacionForm.getNumeroSolicitud());
		liquidacionForm.setOtrosProfesionales("codigoAsocio_"+pos, "");
		liquidacionForm.setOtrosProfesionales("nombreAsocio_"+pos, "");
		liquidacionForm.setOtrosProfesionales("tipoAsocio_"+pos, "");
		liquidacionForm.setOtrosProfesionales("codigoProfesional_"+pos, "");
		liquidacionForm.setOtrosProfesionales("nombreProfesional_"+pos, "");
		liquidacionForm.setOtrosProfesionales("cobrable_"+pos, ConstantesBD.acronimoSi);
		//Todo profesional que se agregue nuevo desde aqui quiere decir que no viene de la hoja quirúrgica
		liquidacionForm.setOtrosProfesionales("historiaClinica_"+pos, ConstantesBD.acronimoNo);
		liquidacionForm.setOtrosProfesionales("existeBd_"+pos, ConstantesBD.acronimoNo);
		liquidacionForm.setOtrosProfesionales("eliminar_"+pos, ConstantesBD.acronimoNo);
		
		liquidacionForm.setNumOtrosProfesionales(pos+1);
		
		String resultado = "<respuesta>" +
		"<infoid>" +
			"<sufijo>ajaxAdicionarOtroProfesional</sufijo>" +
			"<pos>"+pos+"</pos>" +
			"<id-arreglo>asocio</id-arreglo>" + //nombre de la etiqueta de cada elemento
			"<id-arreglo>profesional</id-arreglo>" + //nombre de la etiqueta de cada elemento
		"</infoid>";
	
		for(HashMap elemento:liquidacionForm.getAsocios())
		{
			resultado += "<asocio>";
				resultado += "<codigo>"+elemento.get("codigo")+"</codigo>";
				resultado += "<descripcion>"+elemento.get("nombre_asocio")+"</descripcion>";
			resultado += "</asocio>";
		}
		
		for(HashMap elemento:liquidacionForm.getProfesionales())
		{
			resultado += "<profesional>";
				resultado += "<codigo>"+elemento.get("codigo")+"</codigo>";
				resultado += "<descripcion>"+elemento.get("nombre")+"</descripcion>";
			resultado += "</profesional>";
		}
		
		resultado += "</respuesta>";
		
		UtilidadBD.closeConnection(con);
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
	        response.getWriter().write(resultado);
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionFiltrarAdicionarOtroProfesional: "+e);
		}
		return null;
	}



	/**
	 * Método implementado para realizar la impresión de la liquidacion de servicios
	 * @param con
	 * @param liquidacionForm
	 * @param mapping
	 * @param usuario
	 * @param paciente
	 * @param request
	 * @return
	 */
	private ActionForward accionImprimir(Connection con, LiquidacionServiciosForm liquidacionForm, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request) 
	{
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
	 * Método implementado para realizar la liquidación de cirugías
	 * @param con
	 * @param liquidacionForm
	 * @param mapping
	 * @param usuario
	 * @param paciente
	 * @param request
	 * @return
	 */
	private ActionForward accionLiquidar(Connection con, LiquidacionServiciosForm liquidacionForm, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request) throws IPSException 
	{
		ActionErrors errores = new ActionErrors();
		LiquidacionServicios mundoLiquidacion = new LiquidacionServicios();
		
		///*****************VALIDACIONES INICIALES***************************************************************************
		errores = validacionesGuardarOrden(con,liquidacionForm,errores,usuario,paciente);
		//********************************************************************************************************************
		
		//*********SE GUARDA LA INFORMACION DE LA ORDEN************************************************
		if(errores.isEmpty())
			errores = guardarInformacionOrden(con, liquidacionForm, usuario, paciente, errores);
		//*******************************************************************************************
		
		UtilidadBD.iniciarTransaccion(con);
		if(errores.isEmpty())
		{
			//**********************SE REALIZA EL PROCESO DE LA LIQUIDACION***********************************
			llenarMundo(con, mundoLiquidacion, liquidacionForm, usuario, paciente);
			boolean exito = mundoLiquidacion.realizarLiquidacion(con, liquidacionForm.getNumeroSolicitud(), usuario, true, true);
			if(!exito)
			{
				errores = mundoLiquidacion.llenarMensajesError(errores);
				llenarForma(liquidacionForm, mundoLiquidacion); //Se vuelven a llenar los datos de la forma
			}
			//****************************************************************************************
		}
		
		
		if(!errores.isEmpty())
		{
			UtilidadBD.abortarTransaccion(con);
			saveErrors(request, errores);
			liquidacionForm.setEstado("detalleOrden");
			UtilidadBD.closeConnection(con);
			return mapping.findForward("detalleOrden");
		}
		else
		{
			UtilidadBD.finalizarTransaccion(con);
			//Se muestra el resumen de la liquidación
			liquidacionForm.resetOrden(); //se limpian los datos de la orden
			mundoLiquidacion.cargarTodaInformacionOrdenConAsociosLiquidados(); //Se carga toda la información de la orden con asocios liquidados
			llenarForma(liquidacionForm, mundoLiquidacion); //se llena la forma con la nueva información
			
			
			UtilidadBD.closeConnection(con);
			return mapping.findForward("resumenLiquidacion");
		}
		
		
		
	}



	/**
	 * Método implementado para guardar toda la información de la orden
	 * @param con
	 * @param liquidacionForm
	 * @param mapping
	 * @param usuario
	 * @param paciente
	 * @param request
	 * @return
	 */
	private ActionForward accionGuardar(Connection con, LiquidacionServiciosForm liquidacionForm, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request) throws IPSException 
	{
		ActionErrors errores = new ActionErrors();
		
		///*****************VALIDACIONES INICIALES***************************************************************************
		errores = validacionesGuardarOrden(con,liquidacionForm,errores,usuario,paciente);
		//********************************************************************************************************************
		
		
		if(errores.isEmpty())
		{
			//**********************GUARDAR INFORMACIÓN DE LA ORDEN***********************************
			errores = guardarInformacionOrden(con,liquidacionForm,usuario,paciente,errores);
			//****************************************************************************************
		}
		//Si habían errores de la validación al guardar orden se retorna a la página 
		else
		{
			saveErrors(request, errores);
			liquidacionForm.setEstado("detalleOrden");
			UtilidadBD.closeConnection(con);
			return mapping.findForward("detalleOrden");
		}
			
		if(!errores.isEmpty())
		{	
			saveErrors(request, errores);
			liquidacionForm.setEstado("detalleOrden");
		}
		
		//logger.info("MAPA LISTADO ORDENES ===============================");
		//Utilidades.imprimirMapa(liquidacionForm.getListadoOrdenes());
		//logger.info("FIN MAPA LISTADO ORDENES ===============================");
		//logger.info("POSICION INDEX=> "+liquidacionForm.getIndex());
		return accionDetalleOrden(con, liquidacionForm, mapping, usuario, paciente);
		//UtilidadBD.closeConnection(con);
		//return mapping.findForward("detalleOrden");
	}

	



	/**
	 * Método implementado para guardar la informacion de la orden
	 * @param con
	 * @param liquidacionForm
	 * @param usuario
	 * @param paciente
	 * @param errores
	 * @return
	 */
	private ActionErrors guardarInformacionOrden(Connection con, LiquidacionServiciosForm liquidacionForm, UsuarioBasico usuario, PersonaBasica paciente, ActionErrors errores) 
	{
		LiquidacionServicios mundoLiquidacion = new LiquidacionServicios();
		llenarMundo(con, mundoLiquidacion, liquidacionForm, usuario, paciente);
		UtilidadBD.iniciarTransaccion(con);
		if(!mundoLiquidacion.actualizarDatosOrden())
			errores = mundoLiquidacion.llenarMensajesError(errores);
		
		if(!errores.isEmpty())
			UtilidadBD.abortarTransaccion(con);
		else
			UtilidadBD.finalizarTransaccion(con);
		return errores;
	}



	/**
	 * Método implementado para realizar las validaciones al guardar la orden
	 * @param con 
	 * @param liquidacionForm
	 * @param errores
	 * @param fechaIngreso
	 * @param horaIngreso
	 * @param fechaSistema
	 * @param horaSistema
	 * @param usuario 
	 * @param paciente 
	 * @return
	 */
	private ActionErrors validacionesGuardarOrden(Connection con, LiquidacionServiciosForm liquidacionForm, ActionErrors errores, UsuarioBasico usuario, PersonaBasica paciente) 
	{
		String fechaIngreso = Utilidades.obtenerFechaIngreso(con, paciente.getCodigoIngreso()+"");
		String horaIngreso = Utilidades.obtenerHoraIngreso(con, paciente.getCodigoIngreso()+"");
		String fechaSistema = UtilidadFecha.getFechaActual(con);
		String horaSistema = UtilidadFecha.getHoraActual(con);
		
		///Datos ocupacion sala
		boolean fecha1 = true, hora1 = true, fecha2 = true, hora2 = true;
		
		if(liquidacionForm.getDatosActoQx("fechaIngresoSala").toString().equals(""))
		{
			errores.add("",new ActionMessage("errors.required","La fecha ingreso sala"));
			fecha1 = false;
		}
		else if(!UtilidadFecha.validarFecha(liquidacionForm.getDatosActoQx("fechaIngresoSala").toString()))
		{
			errores.add("",new ActionMessage("errors.formatoFechaInvalido","ingreso sala"));
			fecha1 = false;
		}
		
		if(liquidacionForm.getDatosActoQx("horaIngresoSala").toString().equals(""))
		{
			errores.add("",new ActionMessage("errors.required","La hora ingreso sala"));
			hora1 = false;
		}
		else if(!UtilidadFecha.validacionHora(liquidacionForm.getDatosActoQx("horaIngresoSala").toString()).puedoSeguir)
		{
			errores.add("",new ActionMessage("errors.formatoHoraInvalido","ingreso sala"));
			hora1 = false;
		}
		
		if(liquidacionForm.getDatosActoQx("fechaSalidaSala").toString().equals(""))
		{
			errores.add("",new ActionMessage("errors.required","La fecha egreso sala"));
			fecha2 = false;
		}
		else if(!UtilidadFecha.validarFecha(liquidacionForm.getDatosActoQx("fechaSalidaSala").toString()))
		{
			errores.add("",new ActionMessage("errors.formatoFechaInvalido","egreso sala"));
			fecha2 = false;
		}
		
		if(liquidacionForm.getDatosActoQx("horaSalidaSala").toString().equals(""))
		{
			errores.add("",new ActionMessage("errors.required","La hora egreso sala"));
			hora2 = false;
		}
		else if(!UtilidadFecha.validacionHora(liquidacionForm.getDatosActoQx("horaSalidaSala").toString()).puedoSeguir)
		{
			errores.add("",new ActionMessage("errors.formatoHoraInvalido","egreso sala"));
			hora2 = false;
		}
		
		
		//Validaciones adicionales de la fecha/hora ingreso sala
		if(fecha1&&hora1)
		{
			if(!UtilidadFecha.compararFechas(liquidacionForm.getDatosActoQx("fechaIngresoSala").toString(), liquidacionForm.getDatosActoQx("horaIngresoSala").toString(), fechaIngreso, horaIngreso).isTrue())
				errores.add("",new ActionMessage("errors.fechaHoraAnteriorIgualActual","de ingreso sala","del ingreso del paciente: "+fechaIngreso+" - "+horaIngreso));
			
			if(!UtilidadFecha.compararFechas(fechaSistema, horaSistema, liquidacionForm.getDatosActoQx("fechaIngresoSala").toString(), liquidacionForm.getDatosActoQx("horaIngresoSala").toString()).isTrue())
				errores.add("", new ActionMessage("errors.fechaHoraPosteriorIgualActual","de ingreso sala","del sistema: "+fechaSistema+" - "+horaSistema));
			
			
			if(UtilidadTexto.getBoolean(liquidacionForm.getDatosActoQx("participoAnestesiologo").toString())
				&&
				UtilidadCadena.noEsVacio(ValoresPorDefecto.getMinutosMaximosRegistroAnestesia(usuario.getCodigoInstitucionInt())))
			{
				//Validación de los minutos máximos permitidos para el registro de anestesia con fecha diferente del sistema
				if(UtilidadTexto.getBoolean(ValoresPorDefecto.getManejaHojaAnestesia(usuario.getCodigoInstitucionInt())))
				{
					int minutosLimite = Utilidades.convertirAEntero(ValoresPorDefecto.getMinutosMaximosRegistroAnestesia(usuario.getCodigoInstitucionInt()), true);
					String[] fechaHora = UtilidadFecha.incrementarMinutosAFechaHora(fechaSistema, horaSistema, -1*minutosLimite, false);
					
					if(!UtilidadFecha.compararFechas(liquidacionForm.getDatosActoQx("fechaIngresoSala").toString(), liquidacionForm.getDatosActoQx("horaIngresoSala").toString(), fechaHora[0], fechaHora[1]).isTrue())
						errores.add("", new ActionMessage("errors.fechaHoraAnteriorIgualActual","de ingreso sala","límite de registro de anestesia: "+fechaHora[0]+" - "+fechaHora[1]));
				}
			}
			
		}
		
		//Validaciones adiconales de la fecha/hora egreso sala
		if(fecha2&&hora2)
		{
			if(fecha1&&hora1)
				if(!UtilidadFecha.compararFechas(liquidacionForm.getDatosActoQx("fechaSalidaSala").toString(), liquidacionForm.getDatosActoQx("horaSalidaSala").toString(), liquidacionForm.getDatosActoQx("fechaIngresoSala").toString(), liquidacionForm.getDatosActoQx("horaIngresoSala").toString()).isTrue())
					errores.add("",new ActionMessage("errors.fechaHoraAnteriorIgualActual","de egreso sala","de ingreso sala"));
			
			if(!UtilidadFecha.compararFechas(fechaSistema, horaSistema, liquidacionForm.getDatosActoQx("fechaSalidaSala").toString(), liquidacionForm.getDatosActoQx("horaSalidaSala").toString()).isTrue())
				errores.add("", new ActionMessage("errors.fechaHoraPosteriorIgualActual","de egreso sala","del sistema: "+fechaSistema+" - "+horaSistema));
			
			if(!UtilidadFecha.compararFechas(liquidacionForm.getDatosActoQx("fechaSalidaSala").toString(), liquidacionForm.getDatosActoQx("horaSalidaSala").toString(), liquidacionForm.getDatosActoQx("fechaFinalCx").toString(), liquidacionForm.getDatosActoQx("horaFinalCx").toString()).isTrue())
				errores.add("",new ActionMessage("errors.fechaHoraAnteriorIgualActual","de egreso sala","final de la cirugía: "+liquidacionForm.getDatosActoQx("fechaFinalCx")+" - "+liquidacionForm.getDatosActoQx("horaFinalCx")));
		}
		
		//Datos servicios / liquidacion
		if(liquidacionForm.getDatosActoQx("codigoTipoSala").toString().trim().equals(""))
			errores.add("",new ActionMessage("errors.required","El tipo de sala"));
		
		
		if(liquidacionForm.getDatosActoQx("codigoSala").toString().trim().equals(""))
			errores.add("",new ActionMessage("errors.required","La sala"));
		
		//********VALIDACIONES OTROS PROFESIONALES************************************
		int cont = 0;
		HashMap<String,String> asociosRepetidos = new HashMap<String,String>();
		HashMap<String,String> profesionalesRepetidos = new HashMap<String,String>();
		for(int i=0;i<liquidacionForm.getNumOtrosProfesionales();i++)
			if(!UtilidadTexto.getBoolean(liquidacionForm.getOtrosProfesionales("eliminar_"+i).toString()))
			{
				cont++;
				//Solo se validan los que se hayan ingresado desde la liquidacion y que no se vayan a eliminar
				if(!UtilidadTexto.getBoolean(liquidacionForm.getOtrosProfesionales("historiaClinica_"+i).toString()))
				{
					//Se verifica campo asocio
					if(UtilidadTexto.isEmpty(liquidacionForm.getOtrosProfesionales("codigoAsocio_"+i).toString()))
						errores.add("",new ActionMessage("errors.required","El asocio N° "+cont+" (Otros profesionales)"));
					//Se verifica que no esté repetido el asocio
					else if(asociosRepetidos.containsValue(liquidacionForm.getOtrosProfesionales("codigoAsocio_"+i).toString()))
						errores.add("",new ActionMessage("error.capitacion.yaExisteCodigo","asocio N° "+cont+" (Otros profesionales)"));
					
					//Se verifica campo profesional
					if(UtilidadTexto.isEmpty(liquidacionForm.getOtrosProfesionales("codigoProfesional_"+i).toString()))
						errores.add("",new ActionMessage("errors.required","El profesional N° "+cont+" (Otros profesionales)"));
					//Se verifica que no esté repetido el asocio
					else if(profesionalesRepetidos.containsValue(liquidacionForm.getOtrosProfesionales("codigoProfesional_"+i).toString()))
						errores.add("",new ActionMessage("error.capitacion.yaExisteCodigo","profesional N° "+cont+" (Otros profesionales)"));
				}
				
				if(!UtilidadTexto.isEmpty(liquidacionForm.getOtrosProfesionales("codigoAsocio_"+i).toString())&&
					!asociosRepetidos.containsValue(liquidacionForm.getOtrosProfesionales("codigoAsocio_"+i).toString()))
					asociosRepetidos.put("codigoAsocio_"+i,liquidacionForm.getOtrosProfesionales("codigoAsocio_"+i).toString());
				
				if(!UtilidadTexto.isEmpty(liquidacionForm.getOtrosProfesionales("codigoProfesional_"+i).toString())&&
					!profesionalesRepetidos.containsValue(liquidacionForm.getOtrosProfesionales("codigoProfesional_"+i).toString()))
					profesionalesRepetidos.put("codigoProfesional_"+i,liquidacionForm.getOtrosProfesionales("codigoProfesional_"+i).toString());
			}
		//Utilidades.imprimirMapa(asociosRepetidos); //registro de asocios repetidos
		//*****************************************************************************
		
		boolean seLiquidaraServicio = false;
		for(int i=0;i<liquidacionForm.getNumCirugias();i++)
		{
			if(liquidacionForm.getCirugiasSolicitud("codigoEsquemaTarifario_"+i).toString().trim().equals(""))
				errores.add("",new ActionMessage("errors.required","El esquema tarifario del servicio N° "+liquidacionForm.getCirugiasSolicitud("numeroServicio_"+i)));
			
			if(UtilidadTexto.getBoolean(liquidacionForm.getCirugiasSolicitud("liquidarServicio_"+i).toString())&&liquidacionForm.getCirugiasSolicitud("grupoUvr_"+i).toString().trim().equals(""))
				errores.add("",new ActionMessage("errors.required","El grupo,uvr ó valor del servicio N° "+liquidacionForm.getCirugiasSolicitud("numeroServicio_"+i)));
			
			if(UtilidadTexto.getBoolean(liquidacionForm.getCirugiasSolicitud("liquidarServicio_"+i).toString()))
				seLiquidaraServicio = true;
			
			errores = validacionesDetalleServicio(liquidacionForm, errores, i,usuario);
		}
		
		if(!seLiquidaraServicio&&liquidacionForm.getEstado().equals("liquidar"))
			errores.add("", new ActionMessage("errors.minimoCampos","cobrar un servicio","liquidación"));
		
		return errores;
	}



	/**
	 * Método implementado para guardar la informacion del detalle del servicio
	 * @param con
	 * @param liquidacionForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @param paciente 
	 * @return
	 */
	private ActionForward accionGuardarDetalleServicio(Connection con, LiquidacionServiciosForm liquidacionForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request, PersonaBasica paciente) 
	{
		int posicion = Utilidades.convertirAEntero(liquidacionForm.getCirugiasSolicitud("posicion")+"", true);
		ActionErrors errores = new ActionErrors();
		logger.info("POSICION ANTES=>"+posicion);
		//*****************VALIDACIONES ****************************************************************
		errores = validacionesDetalleServicio(liquidacionForm,errores,posicion,usuario);
		
		//***********************************************************************************************
		if(!errores.isEmpty())
		{
			liquidacionForm.setEstado("detalleServicio");
			saveErrors(request, errores);
		}
		else
		{
			//*****************SE INGRESAN/MODIFICAN DATOS DEL SERVICIO**********************************
			UtilidadBD.iniciarTransaccion(con);
			
			LiquidacionServicios mundoLiquidacion = new LiquidacionServicios();
			this.llenarMundo(con, mundoLiquidacion, liquidacionForm, usuario, paciente);
			
			//Se verifica si el servicio fue modificado
			mundoLiquidacion.fueronModificadasCirugia(posicion);
			//SE realiza la actualización del servicio
			mundoLiquidacion.insertarDatosServicio(posicion);
			//Se genera el log de modificacion
			mundoLiquidacion.generarLogModificacion();
			
			if(mundoLiquidacion.getMensajesError().size()==0)
			{
				UtilidadBD.finalizarTransaccion(con);
				//Se cargan de nuevo los profesionales de la cirugia
				//mundoLiquidacion.consultarProfesionalesXCirugia(posicion);
				//mundoLiquidacion.setCirugias(mundoLiquidacion.getCirugias());
			}
			else
			{
				errores = mundoLiquidacion.llenarMensajesError(errores);
				
				
				saveErrors(request, errores);
				liquidacionForm.setEstado("detalleServicio");
				UtilidadBD.abortarTransaccion(con);
			}
			//****************************************************************************************
			
		}
		logger.info("POSICION DESPUES=>"+liquidacionForm.getCirugiasSolicitud("posicion"));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleServicio");
	}





	/**
	 * Metodo implementado para realizar las validaciones del detalle del servicio
	 * @param liquidacionForm
	 * @param errores
	 * @param posicion
	 * @param usuario 
	 * @return
	 */
	private ActionErrors validacionesDetalleServicio(LiquidacionServiciosForm liquidacionForm, ActionErrors errores, int posicion, UsuarioBasico usuario) 
	{
		if(!UtilidadCadena.noEsVacio(liquidacionForm.getCirugiasSolicitud("codigoViaCx_"+posicion)+""))
			errores.add("",new ActionMessage("errors.required","El campo vía en el servicio N° "+liquidacionForm.getCirugiasSolicitud("numeroServicio_"+posicion)));
			
		if(!UtilidadCadena.noEsVacio(liquidacionForm.getCirugiasSolicitud("indBilateral_"+posicion)+""))
			errores.add("",new ActionMessage("errors.required","El indicativo bilateral en el servicio N° "+liquidacionForm.getCirugiasSolicitud("numeroServicio_"+posicion)));
		
		int numProfesionales = Utilidades.convertirAEntero(liquidacionForm.getCirugiasSolicitud("numProfesionales_"+posicion).toString(), true);
		if(numProfesionales==0)
			errores.add("", new ActionMessage("errors.minimoCampos2","ingresar un honorario en el servicio N° "+liquidacionForm.getCirugiasSolicitud("numeroServicio_"+posicion),"guardar la información"));
		
		//Iteracion de los profesionales
		int contador = 0;
		HashMap<String, String> asociosRepetidos =new HashMap<String, String>();
		HashMap<String, String> profesionalesRepetidos =new HashMap<String, String>();
		for(int i=0;i<Utilidades.convertirAEntero(liquidacionForm.getCirugiasSolicitud("numProfesionales_"+posicion).toString(), true);i++)
			if(!UtilidadTexto.getBoolean(liquidacionForm.getCirugiasSolicitud("eliminar_"+posicion+"_"+i).toString()))
			{
				contador ++;
				
				if(!UtilidadCadena.noEsVacio(liquidacionForm.getCirugiasSolicitud("consecutivoAsocio_"+posicion+"_"+i)+""))
					errores.add("",new ActionMessage("error.salasCirugia.noTiene","El honorario N° "+contador+" en el servicio N° "+liquidacionForm.getCirugiasSolicitud("numeroServicio_"+posicion),"definido el tipo honorario"));
				else if(
						//Si el asocio NO es de ayudantía y está repetido se muestra mensaje de error
						Integer.parseInt(ValoresPorDefecto.getAsocioAyudantia(usuario.getCodigoInstitucionInt()).toString())!=Integer.parseInt(liquidacionForm.getCirugiasSolicitud("consecutivoAsocio_"+posicion+"_"+i).toString())
						&&
						asociosRepetidos.containsValue(liquidacionForm.getCirugiasSolicitud("consecutivoAsocio_"+posicion+"_"+i).toString())
					)
					errores.add("",new ActionMessage("error.capitacion.yaExisteCodigo","asocio N° "+contador+" en el servicio N° "+liquidacionForm.getCirugiasSolicitud("numeroServicio_"+posicion)));
				else
					asociosRepetidos.put("consecutivoAsocio_"+posicion+"_"+i, liquidacionForm.getCirugiasSolicitud("consecutivoAsocio_"+posicion+"_"+i).toString());
				
				
				if(!UtilidadCadena.noEsVacio(liquidacionForm.getCirugiasSolicitud("codigoProfesional_"+posicion+"_"+i)+""))
					errores.add("",new ActionMessage("error.salasCirugia.noTiene","El honorario N° "+contador+" en el servicio N° "+liquidacionForm.getCirugiasSolicitud("numeroServicio_"+posicion),"definido el profesional"));
				else if(profesionalesRepetidos.containsValue(liquidacionForm.getCirugiasSolicitud("codigoProfesional_"+posicion+"_"+i)+""))
					errores.add("",new ActionMessage("error.capitacion.yaExisteCodigo","profesional N° "+contador+" en el servicio N° "+liquidacionForm.getCirugiasSolicitud("numeroServicio_"+posicion)));
				else
					profesionalesRepetidos.put("codigoProfesional_"+posicion+"_"+i, liquidacionForm.getCirugiasSolicitud("codigoProfesional_"+posicion+"_"+i).toString());
				
				if(!UtilidadCadena.noEsVacio(liquidacionForm.getCirugiasSolicitud("codigoEspecialidad_"+posicion+"_"+i)+""))
					errores.add("",new ActionMessage("error.salasCirugia.noTiene","El honorario N° "+contador+" en el servicio N° "+liquidacionForm.getCirugiasSolicitud("numeroServicio_"+posicion),"definida la especialidad"));
			}
		return errores;
	}






	/**
	 * Método implementado para realizar el filtro ajax de las especialidades y pooles de un profesional
	 * @param con
	 * @param liquidacionForm
	 * @param response
	 * @return
	 */
	private ActionForward accionFiltrarDatosProfesional(Connection con, LiquidacionServiciosForm liquidacionForm, HttpServletResponse response) 
	{
		int pos = Integer.parseInt(liquidacionForm.getIndexAjax());
		int posCx = Utilidades.convertirAEntero(liquidacionForm.getCirugiasSolicitud("posicion").toString(), true);
		
		
		
		String resultado = "<respuesta>" +
			"<infoid>" +
				"<sufijo>ajaxBusquedaTipoSelect</sufijo>" +
				"<id-select>codigoEspecialidad_"+pos+"</id-select>" +
				"<id-select>pool_"+pos+"</id-select>" +
				"<id-arreglo>especialidad</id-arreglo>" +
				"<id-arreglo>pool</id-arreglo>" +
			"</infoid>" ;
		
		
		//***********************ADICION DE LAS ESPECIALIDADES**********************************************************************
		//Se verifica si se ingresó profesional
		if(!liquidacionForm.getCodigoProfesional().equals(""))
			//Dependiendo si el asocio es cirujano entonces se realiza el cambio de especialidad
			if(!UtilidadTexto.getBoolean(liquidacionForm.getCirugiasSolicitud("esAsocioCirujano_"+posCx+"_"+pos).toString()))
				liquidacionForm.setCirugiasSolicitud("comboEspecialidades_"+posCx+"_"+pos, Utilidades.obtenerEspecialidadesEnArray(con, Integer.parseInt(liquidacionForm.getCodigoProfesional()), ConstantesBD.codigoNuncaValido));
			else
				liquidacionForm.setCirugiasSolicitud("comboEspecialidades_"+posCx+"_"+pos, Utilidades.obtenerEspecialidadesEnArray(con, ConstantesBD.codigoNuncaValido, Integer.parseInt(liquidacionForm.getCirugiasSolicitud("codigoEspecialidadInterviene_"+posCx).toString())));
		//Si no se seleccionó profesional se quitan las especialidades
		else
			liquidacionForm.setCirugiasSolicitud("comboEspecialidades_"+posCx+"_"+pos, new ArrayList<HashMap<String, Object>>());
		
		for(HashMap elemento:(ArrayList<HashMap<String, Object>>)liquidacionForm.getCirugiasSolicitud("comboEspecialidades_"+posCx+"_"+pos))
		{
			resultado += "<especialidad>";
				resultado += "<codigo>"+elemento.get("codigoespecialidad")+"</codigo>";
				resultado += "<descripcion>"+elemento.get("nombreespecialidad")+"</descripcion>";
			resultado += "</especialidad>";
		}
		
		//*****************ADICION DE LOS POOLES********************************************************************************
		if(!liquidacionForm.getCodigoProfesional().equals(""))
			liquidacionForm.setCirugiasSolicitud("comboPooles_"+posCx+"_"+pos, UtilidadesFacturacion.obtenerPoolesProfesional(con, Integer.parseInt(liquidacionForm.getCodigoProfesional()), liquidacionForm.getDatosActoQx("fechaInicialCx").toString(),false));
		else
			liquidacionForm.setCirugiasSolicitud("comboPooles_"+posCx+"_"+pos, new ArrayList<HashMap<String, Object>>());
		
		for(HashMap elemento:(ArrayList<HashMap<String, Object>>)liquidacionForm.getCirugiasSolicitud("comboPooles_"+posCx+"_"+pos))
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
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
	        response.getWriter().write(resultado);
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionFiltrarDatosProfesional: "+e);
		}
		return null;
	}



	/**
	 * Método que se encarga de reordenar el listado de servicios por cambio en algun dato del servicio
	 * @param con
	 * @param liquidacionForm
	 * @param mapping
	 * @param usuario
	 * @param paciente
	 * @return
	 */
	private ActionForward accionCambioServicio(Connection con, LiquidacionServiciosForm liquidacionForm, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente) throws IPSException 
	{
		LiquidacionServicios mundoLiquidacion = new LiquidacionServicios();
		llamadoProcesoReordenacion(con, mundoLiquidacion, usuario, paciente, liquidacionForm, false, true);
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("frameServicios");
	}



	/**
	 * Método implementado para volver a la pagina del detalle de la orden,
	 * es necesario verificar nuevamente el indicativo de bilateral para saber si se debe cambiar el tipo de cirugia
	 * al servicio
	 * @param con
	 * @param liquidacionForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionVolverDetalleOrden(Connection con, LiquidacionServiciosForm liquidacionForm, ActionMapping mapping, UsuarioBasico usuario) 
	{
		int posicion = Utilidades.convertirAEntero(liquidacionForm.getCirugiasSolicitud("posicion")+"", true);
		LiquidacionServicios mundoLiquidacion = new LiquidacionServicios();
		
		//***************VERIFICACION DEL INDICATIVO BILATERAL*****************************************************************
		mundoLiquidacion.setCon(con);
		mundoLiquidacion.setCirugias(liquidacionForm.getCirugiasSolicitud());
		mundoLiquidacion.setUsuario(usuario);
		mundoLiquidacion.redefinirTipoCirugia(posicion,false);
		mundoLiquidacion.redefinirTipoCirugia(posicion,true);
		//*********************************************************************************
		
		liquidacionForm.setCirugiasSolicitud(mundoLiquidacion.getCirugias());
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleOrden");
	}



	/**
	 * Método que va al detalle del servicio
	 * @param con
	 * @param liquidacionForm
	 * @param mapping
	 * @param usuario 
	 * @param paciente 
	 * @return
	 */
	private ActionForward accionDetalleServicio(Connection con, LiquidacionServiciosForm liquidacionForm, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente) 
	{
		int posicion = Utilidades.convertirAEntero(liquidacionForm.getCirugiasSolicitud("posicion")+"", true);
		int codigoEspecialidadInterviene = ConstantesBD.codigoNuncaValido, codigoProfesional = ConstantesBD.codigoNuncaValido;
		//Se consulta nuevamente las descripciones de los campos que pudieron haberse modificado
		liquidacionForm.setCirugiasSolicitud("nombreEsquemaTarifario_"+posicion, Utilidades.getNombreEsquemaTarifario(con, Utilidades.convertirAEntero(liquidacionForm.getCirugiasSolicitud("codigoEsquemaTarifario_"+posicion)+"", true)));
		liquidacionForm.setCirugiasSolicitud("nombreEspecialidadInterviene_"+posicion, Utilidades.getNombreEspecialidad(con, Utilidades.convertirAEntero(liquidacionForm.getCirugiasSolicitud("codigoEspecialidadInterviene_"+posicion)+"", true)));
		
		
		for(int i=0;i<Utilidades.convertirAEntero(liquidacionForm.getCirugiasSolicitud("numProfesionales_"+posicion).toString(), true);i++)
		{
			//Se toma el codigo del profesional
			codigoProfesional = Utilidades.convertirAEntero(liquidacionForm.getCirugiasSolicitud("codigoProfesional_"+posicion+"_"+i).toString(), true);
			codigoEspecialidadInterviene = Utilidades.convertirAEntero(liquidacionForm.getCirugiasSolicitud("codigoEspecialidadInterviene_"+posicion).toString(), true);
			
			//**********SE CARGAN LOS COMBOS PARA CADA REGISTRO DE PROFESIONALES**************************
			//Se verifica si es asocio de cirujano
			if(Utilidades.convertirAEntero(liquidacionForm.getCirugiasSolicitud("consecutivoAsocio_"+posicion+"_"+i).toString(), true)==Utilidades.convertirAEntero(ValoresPorDefecto.getAsocioCirujano(usuario.getCodigoInstitucionInt()), true))
			{
				//Se cargan los profesionales que tengan la especialidad que interviene
				liquidacionForm.setCirugiasSolicitud("comboProfesionales_"+posicion+"_"+i, 
					UtilidadesAdministracion.obtenerProfesionales(
							con, 
							usuario.getCodigoInstitucionInt(), 
							codigoEspecialidadInterviene, 
							true, 
							true,
							ConstantesBD.codigoNuncaValido)
						);
				//Se consulta en array la mera especialidad que interviene
				liquidacionForm.setCirugiasSolicitud("comboEspecialidades_"+posicion+"_"+i, Utilidades.obtenerEspecialidadesEnArray(con, ConstantesBD.codigoNuncaValido, codigoEspecialidadInterviene));
			}
			else
			{
				//Se cargan los profesionales que tengan ocupacion que realiza cirugias
				liquidacionForm.setCirugiasSolicitud("comboProfesionales_"+posicion+"_"+i, 
						UtilidadesAdministracion.obtenerProfesionales(
								con, 
								usuario.getCodigoInstitucionInt(), 
								ConstantesBD.codigoNuncaValido, 
								true, 
								true,
								ConstantesBD.codigoNuncaValido)
							);
				//Si hay un profesional seleccionado se consultan sus especialidades
				if(codigoProfesional>0)
					liquidacionForm.setCirugiasSolicitud("comboEspecialidades_"+posicion+"_"+i, Utilidades.obtenerEspecialidadesEnArray(con, codigoProfesional, ConstantesBD.codigoNuncaValido));
				else
					liquidacionForm.setCirugiasSolicitud("comboEspecialidades_"+posicion+"_"+i, new ArrayList());
			}
			//***********************************************************************************************
			//*************************************************************************************************
			//**********SE CARGAN LOS POOLES DEL MEDICO********************************************************
			if(codigoProfesional>0)
				liquidacionForm.setCirugiasSolicitud("comboPooles_"+posicion+"_"+i, UtilidadesFacturacion.obtenerPoolesProfesional(con, codigoProfesional, liquidacionForm.getDatosActoQx("fechaInicialCx").toString(),false));
			else
				liquidacionForm.setCirugiasSolicitud("comboPooles_"+posicion+"_"+i, new ArrayList<HashMap<String, Object>>());
			//*************************************************************************************************
		}
		//********************************************************************************************
		
		
		//***********SE VERIFICA EL TIPO DE ESPECIALISTA***************************************************
		LiquidacionServicios mundoLiquidacion = new LiquidacionServicios();
		llenarMundo(con, mundoLiquidacion, liquidacionForm, usuario, paciente);
		mundoLiquidacion.redefinirTipoEspecialista(posicion);
		liquidacionForm.setCirugiasSolicitud(mundoLiquidacion.getCirugias());
		//*******************************************************************************************
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleServicio");
	}



	/**
	 * Método para eliminar un servicio
	 * @param con
	 * @param liquidacionForm
	 * @param mapping
	 * @param paciente 
	 * @param usuario 
	 * @return
	 */
	private ActionForward accionEliminarServicio(Connection con, LiquidacionServiciosForm liquidacionForm, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente) throws IPSException 
	{
		int posicion = Utilidades.convertirAEntero(liquidacionForm.getCirugiasSolicitud("posicion")+"", true);
		
		//Se eliminan los datos de los profesionales
		String indices[] = LiquidacionServicios.indicesProfesionalesCirugia;
		for(int i=posicion;i<(liquidacionForm.getNumCirugias()-1);i++)
			for(int j=0;j<Utilidades.convertirAEntero(liquidacionForm.getCirugiasSolicitud("numProfesionales_"+i)+"", true);j++)
				for(int k=0;k<indices.length;k++)
					liquidacionForm.setCirugiasSolicitud(indices[k]+i+"_"+j, liquidacionForm.getCirugiasSolicitud(indices[k]+(i+1)+"_"+j));
		
		//Se remueve el último registro de los profesionales
		for(int i=0;i<Utilidades.convertirAEntero(liquidacionForm.getCirugiasSolicitud("numProfesionales_"+(liquidacionForm.getNumCirugias()-1))+"", true);i++)
			for(int j=0;j<indices.length;j++)
				liquidacionForm.getCirugiasSolicitud().remove(indices[j]+(liquidacionForm.getNumCirugias()-1)+"_"+i);
		
		//Se eliminan los datos del servicio
		Utilidades.eliminarRegistroMapaGenerico(
			liquidacionForm.getCirugiasSolicitud(), 
			new HashMap(), //No aplica
			posicion, 
			LiquidacionServicios.indicesCirugias, 
			"numRegistros", 
			"", "", false);
		
		//Al eliminar es indispensable realizar un proceso de reordenación
		LiquidacionServicios mundoLiquidacion = new LiquidacionServicios();
		llamadoProcesoReordenacion(con, mundoLiquidacion, usuario, paciente, liquidacionForm, false, true);
		
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("frameServicios");
	}

	/**
	 * Método implementado para agregar/modificar un servicio y luego realizar la reordenación 
	 * @param con
	 * @param liquidacionForm
	 * @param mapping
	 * @param usuario
	 * @param paciente 
	 * @return
	 */
	private ActionForward accionAgregarServicio(Connection con, LiquidacionServiciosForm liquidacionForm, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente) throws IPSException 
	{
		boolean esNuevo = false;
		ElementoApResource mensaje = new ElementoApResource("errors.notEspecific");
		//Se verifica si el servicio es nuevo, verificando si la posicion es igual al número de servicios
		if(Utilidades.convertirAEntero(liquidacionForm.getCirugiasSolicitud("posicion")+"", true)==liquidacionForm.getNumCirugias())
			esNuevo = true;
		
		if(esNuevo)
		{
			int posCopia = ConstantesBD.codigoNuncaValido; //posicion del servicio existente para pasarle los datos del nuevo servicio
			
			//Se consulta el primer servicio que tenga la especialidad del nuevo servicio
			for(int i=0;i<liquidacionForm.getNumCirugias();i++)
				if(Utilidades.convertirAEntero(liquidacionForm.getCirugiasSolicitud("codigoEspecialidadInterviene_"+i)+"", true)==Utilidades.convertirAEntero(liquidacionForm.getCodigoEspecialidad(), true))
				{
					posCopia = i;
					i = liquidacionForm.getNumCirugias(); //se finaliza el ciclo
				}
			
			//Se preparan los indices a copiar
			String[] indicesServicio = LiquidacionServicios.indicesCirugias;
			String[] indicesProfesionales = LiquidacionServicios.indicesProfesionalesCirugia;
			
			//Se copian las llaves de los datos del servicio
			for(int i=0;i<indicesServicio.length;i++)
				if(!indicesServicio[i].equals("codigoServicio_")&&!indicesServicio[i].equals("numeroServicio_")&&
					!indicesServicio[i].equals("codigoCups_")&&!indicesServicio[i].equals("descripcionServicio_")&&
					!indicesServicio[i].equals("codigoEspecialidad_")&&!indicesServicio[i].equals("nombreEspecialidad_")) //no se admiten ciertos indices para no sobreescribir la informacion
					liquidacionForm.setCirugiasSolicitud(indicesServicio[i]+liquidacionForm.getNumCirugias(), liquidacionForm.getCirugiasSolicitud(indicesServicio[i]+posCopia));
			
			//Se copian los datos de los profesionales de la cirugía
			int numProfesionales = 0;
			for(int i=0;i<Utilidades.convertirAEntero(liquidacionForm.getCirugiasSolicitud("numProfesionales_"+posCopia)+"", true);i++)
				//Solo se copian los asocios que sean de historia clínica
				if(UtilidadTexto.getBoolean(liquidacionForm.getCirugiasSolicitud("historiaClinica_"+posCopia+"_"+i).toString()))
				{
					for(int j=0;j<indicesProfesionales.length;j++)
						liquidacionForm.setCirugiasSolicitud(indicesProfesionales[j]+liquidacionForm.getNumCirugias()+"_"+numProfesionales, liquidacionForm.getCirugiasSolicitud(indicesProfesionales[j]+posCopia+"_"+i));
					
					//El campo consecutivo si debe estar en blanco porque es un registro que no existe en la BD
					liquidacionForm.setCirugiasSolicitud("consecutivo_"+liquidacionForm.getNumCirugias()+"_"+numProfesionales, "");
					liquidacionForm.setCirugiasSolicitud("existeBd_"+liquidacionForm.getNumCirugias()+"_"+numProfesionales, ConstantesBD.acronimoNo);
					liquidacionForm.setCirugiasSolicitud("eliminar_"+liquidacionForm.getNumCirugias()+"_"+numProfesionales, ConstantesBD.acronimoNo);
					numProfesionales++;
					liquidacionForm.setCirugiasSolicitud("numProfesionales_"+liquidacionForm.getNumCirugias(), numProfesionales);
				}
				
			
			///Se preparan los datos del nuevo servicio
			liquidacionForm.setCirugiasSolicitud("codigo_"+liquidacionForm.getNumCirugias(), ""); //es un servicio nuevo por lo tanto va vacío
			liquidacionForm.setCirugiasSolicitud("liquidarServicios_"+liquidacionForm.getNumCirugias(), ConstantesBD.acronimoSi);
			liquidacionForm.setCirugiasSolicitud("numeroServicio_"+liquidacionForm.getNumCirugias(), (liquidacionForm.getNumCirugias()+1));
			liquidacionForm.setCirugiasSolicitud("indBilateral_"+liquidacionForm.getNumCirugias(), ConstantesBD.acronimoNo); //siempre que es nuevo servicio el indicador de bilateral va en NO
			
			//ASignacion del tipo de cirugía-----------------------------------------------------------------------------------------
			//Se consulta cuantos servicios tienen la misma especialidad elegida
			int numServEspecialidad = 0;
			for(int i=0;i<liquidacionForm.getNumCirugias();i++)
				if(Utilidades.convertirAEntero(liquidacionForm.getCirugiasSolicitud("codigoEspecialidadInterviene_"+i)+"", true)==Utilidades.convertirAEntero(liquidacionForm.getCodigoEspecialidad(), true))
					numServEspecialidad++;
			//Si ya había una cirugía con esa especialidad, la nueva cirugia es Mayor Adicional
			if(numServEspecialidad==1)
			{
				liquidacionForm.setCirugiasSolicitud("codigoTipoCirugia_"+liquidacionForm.getNumCirugias(),  UtilidadesSalas.obtenerCodigoTipoCirugia(con, ConstantesBDSalas.acronimoTipoCirugiaMayorAdicional, usuario.getCodigoInstitucionInt())); 
				liquidacionForm.setCirugiasSolicitud("acronimoTipoCirugia_"+liquidacionForm.getNumCirugias(),  ConstantesBDSalas.acronimoTipoCirugiaMayorAdicional);
				liquidacionForm.setCirugiasSolicitud("nombreTipoCirugia_"+liquidacionForm.getNumCirugias(),  UtilidadesSalas.obtenerDescripcionTipoCirugia(con, Integer.parseInt(liquidacionForm.getCirugiasSolicitud("codigoTipoCirugia_"+liquidacionForm.getNumCirugias()).toString())));
				
			}
			//Si ya había mas de una cirugia con esa especialidad, la nueva cirugía es Adicional Adicional
			else if(numServEspecialidad>1)
			{
				liquidacionForm.setCirugiasSolicitud("codigoTipoCirugia_"+liquidacionForm.getNumCirugias(),  UtilidadesSalas.obtenerCodigoTipoCirugia(con, ConstantesBDSalas.acronimoTipoCirugiaAdicionalAdicional, usuario.getCodigoInstitucionInt())); 
				liquidacionForm.setCirugiasSolicitud("acronimoTipoCirugia_"+liquidacionForm.getNumCirugias(),  ConstantesBDSalas.acronimoTipoCirugiaAdicionalAdicional);
				liquidacionForm.setCirugiasSolicitud("nombreTipoCirugia_"+liquidacionForm.getNumCirugias(),  UtilidadesSalas.obtenerDescripcionTipoCirugia(con, Integer.parseInt(liquidacionForm.getCirugiasSolicitud("codigoTipoCirugia_"+liquidacionForm.getNumCirugias()).toString())));
			}
			liquidacionForm.setCirugiasSolicitud("codigoTipoCirugiaEspecialidad_"+liquidacionForm.getNumCirugias(),  liquidacionForm.getCirugiasSolicitud("codigoTipoCirugia_"+liquidacionForm.getNumCirugias())); 
			liquidacionForm.setCirugiasSolicitud("acronimoTipoCirugiaEspecialidad_"+liquidacionForm.getNumCirugias(),  liquidacionForm.getCirugiasSolicitud("acronimoTipoCirugia_"+liquidacionForm.getNumCirugias()));
			liquidacionForm.setCirugiasSolicitud("nombreTipoCirugiaEspecialidad_"+liquidacionForm.getNumCirugias(),  liquidacionForm.getCirugiasSolicitud("nombreTipoCirugia_"+liquidacionForm.getNumCirugias()));
			//---------------------------------------------------------------------------------------------------------------------------
			
			//Se edita el mensaje de advertencia
			mensaje.agregarAtributo("Debe ingresar información adicional del nuevo servicio "+liquidacionForm.getCirugiasSolicitud("descripcionServicio_"+liquidacionForm.getNumCirugias())+" antes de liquidar");
			
			liquidacionForm.setCirugiasSolicitud("numRegistros", (liquidacionForm.getNumCirugias()+1));
			
			
			
		}
		
		
		
		//Se reordenan los servicios
		LiquidacionServicios mundoLiquidacion = new LiquidacionServicios();
		
		//Se hace llamado al proceso de reordenación
		llamadoProcesoReordenacion(con,mundoLiquidacion,usuario,paciente,liquidacionForm,false,false);
		
		//Se listan nuevamente los servicios insertados
		String codigosServiciosInsertados = "";
		for(int i=0;i<liquidacionForm.getNumCirugias();i++)
		{
			if(!codigosServiciosInsertados.equals(""))
				codigosServiciosInsertados += ",";
			codigosServiciosInsertados += liquidacionForm.getCirugiasSolicitud("codigoServicio_"+i);
		}
		liquidacionForm.setCirugiasSolicitud("codigosServiciosInsertados", codigosServiciosInsertados);
		
		//Se vuelve a limpiar el codigo de la especialidad
		liquidacionForm.setCodigoEspecialidad("");
		
		//Si el servicio es nuevo se agrega un mensaje de advertencia
		if(esNuevo)
			liquidacionForm.getMensajesCirugias().add(mensaje);
		
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("frameServicios");
	}

	/**
	 * Método implementado para hacer el llamado del proceso de reordenacion
	 * @param con
	 * @param mundoLiquidacion
	 * @param usuario
	 * @param paciente
	 * @param liquidacionForm
	 * @param consulta 
	 * @param deboOrdenar 
	 */
	private void llamadoProcesoReordenacion(Connection con, LiquidacionServicios mundoLiquidacion, UsuarioBasico usuario, PersonaBasica paciente, LiquidacionServiciosForm liquidacionForm, boolean consulta, boolean deboOrdenar) throws IPSException 
	{
		//**************ESTA PARTE SOLO SE LLAMA CUANDO SE CONSULTA POR PRIMERA VEZ***********************
		if(consulta)
		{
			mundoLiquidacion.setCon(con);
			mundoLiquidacion.setNumeroSolicitud(liquidacionForm.getNumeroSolicitud());
			mundoLiquidacion.setUsuario(usuario);
			mundoLiquidacion.cargarDetalleOrden();
			
			//Se llena la información
			llenarForma(liquidacionForm,mundoLiquidacion);
			
			
		}  
		//***********************************************************************************************
		llenarMundo(con,mundoLiquidacion,liquidacionForm,usuario,paciente);
		
		//Se reordenan las cirugias de la orden (Se calcula de nuevo la cobertura)
		mundoLiquidacion.reordenarCirugiasSolicitud(consulta,deboOrdenar);
		//Se consultan los asocios/profesionales de cada cirugia (nuevamente después de la ordenacion)
		if(consulta) 
			mundoLiquidacion.consultarProfesionalesXCirugia(ConstantesBD.codigoNuncaValido);
		liquidacionForm.setCirugiasSolicitud(mundoLiquidacion.getCirugias());
		//Se toma el tarifario oficial general
		liquidacionForm.setTarifarioOficial(mundoLiquidacion.getTarifarioOficialGeneral());
		
		//Si no hay anestesiologo se pone como anestesiólogo el cirujano
		if(!UtilidadTexto.getBoolean(liquidacionForm.getDatosActoQx("participoAnestesiologo").toString()))
		{
			liquidacionForm.setDatosActoQx("codigoAnestesiologo", mundoLiquidacion.getCodigoCirujano());
			liquidacionForm.setDatosActoQx("nombreAnestesiologo", mundoLiquidacion.getNombreCirujano());
		}
		
		//Se muestran los errores
		liquidacionForm.setMensajesCirugias(mundoLiquidacion.getMensajesError());
		
		//************SE CARGAN LAS ESTRUCTURAS DE LOS COMBOS (SIN CONEXION)***********************************
		liquidacionForm.setEsquemasTarifarios(Utilidades.obtenerEsquemasTarifariosInArray(false, usuario.getCodigoInstitucionInt(), liquidacionForm.getTarifarioOficial()));
		//*****************************************************************************************************
	}



	/**
	 * Método implementado para llenar la forma con los datos del mundo
	 * @param liquidacionForm
	 * @param mundoLiquidacion
	 */
	private void llenarForma(LiquidacionServiciosForm liquidacionForm, LiquidacionServicios mundoLiquidacion) 
	{
		liquidacionForm.setNumeroSolicitud(mundoLiquidacion.getNumeroSolicitud());
		liquidacionForm.setNumeroPeticion(mundoLiquidacion.getNumeroPeticion());
		liquidacionForm.setIndQx(mundoLiquidacion.getIndQx());
		liquidacionForm.setEncabezadoSolicitud(mundoLiquidacion.getEncabezadoOrden());
		liquidacionForm.setDatosActoQx(mundoLiquidacion.getDatosActoQx());
		liquidacionForm.setOtrosProfesionales(mundoLiquidacion.getOtrosProfesionales());
		liquidacionForm.setCirugiasSolicitud(mundoLiquidacion.getCirugias());
		liquidacionForm.setMaterialesEspeciales(mundoLiquidacion.getMaterialesEspeciales());
		liquidacionForm.setNombreResponsable(mundoLiquidacion.getInfoCobertura().getDtoSubCuenta().getConvenio().getNombre());
	}



	/**
	 * Método implementado para cargar los datos del mundo de liquidacion de servicios
	 * @param con
	 * @param mundoLiquidacion
	 * @param liquidacionForm
	 * @param usuario
	 * @param paciente
	 */
	private void llenarMundo(Connection con, LiquidacionServicios mundoLiquidacion, LiquidacionServiciosForm liquidacionForm, UsuarioBasico usuario, PersonaBasica paciente) 
	{
		mundoLiquidacion.setCon(con);
		mundoLiquidacion.setCirugias(liquidacionForm.getCirugiasSolicitud());
		mundoLiquidacion.setDatosActoQx(liquidacionForm.getDatosActoQx());
		mundoLiquidacion.setEncabezadoOrden(liquidacionForm.getEncabezadoSolicitud());
		mundoLiquidacion.setOtrosProfesionales(liquidacionForm.getOtrosProfesionales());
		mundoLiquidacion.setNumeroSolicitud(liquidacionForm.getNumeroSolicitud());
		mundoLiquidacion.setNumeroPeticion(liquidacionForm.getNumeroPeticion());
		mundoLiquidacion.setIndQx(liquidacionForm.getIndQx());
		//mundoLiquidacion.setNumeroAutorizacion(liquidacionForm.getEncabezadoSolicitud("autorizacion").toString());
		mundoLiquidacion.setIdIngreso(paciente.getCodigoIngreso()+"");
		mundoLiquidacion.setCodigoViaIngreso(paciente.getCodigoUltimaViaIngreso());
		mundoLiquidacion.setUsuario(usuario);
		
	}



	/**
	 * Método implementado para realizar el filtro ajax de las salas segun un tipo de sala determinado
	 * @param con
	 * @param liquidacionForm
	 * @param usuario
	 * @param response
	 * @return
	 */
	private ActionForward accionFiltrarSalas(Connection con, LiquidacionServiciosForm liquidacionForm, UsuarioBasico usuario, HttpServletResponse response) 
	{
		//Se consultan las salas
		liquidacionForm.setSalas(UtilidadesSalas.obtenerSalas(con, usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion(), ConstantesBD.acronimoSi, liquidacionForm.getIndexAjax()));
		
		String resultado = "<respuesta>" +
			"<infoid>" +
				"<sufijo>ajaxBusquedaTipoSelect</sufijo>" +
				"<id-select>codigoSala</id-select>" +
				"<id-arreglo>sala</id-arreglo>" + //nombre de la etiqueta de cada elemento
			"</infoid>";
		
		for(HashMap elemento:liquidacionForm.getSalas())
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
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
	        response.getWriter().write(resultado);
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionFiltrarSalas: "+e);
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
	private ActionForward accionFiltrarMedicoSala(Connection con, LiquidacionServiciosForm forma, HttpServletResponse response) 
	{
		
		//Se consulta la tarifa servicio
		InfoDatosInt info= UtilidadesSalas.obtenerMedicoSalaMaterialesCx(forma.getIndexAjax());
		
		String contenido= "";
		
		if(!UtilidadTexto.isEmpty(info.getNombre()))
		{
			contenido+="Los honorarios para los asocios tipo sala y materiales seran cargados para el medico: "+info.getNombre();
		}
		
		String resultado = "<respuesta>" +
			"<infoid>" +
				"<sufijo>ajaxBusquedaTipoInnerHtml</sufijo>" +
				"<id-div>divFiltrarMedicoSala</id-div>" + //id del div a modificar
				"<contenido>"+contenido.toUpperCase()+"</contenido>" + //tabla
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
			logger.error("Error al enviar respuesta AJAX en accionFiltrarMedicoSala: "+e);
		}
		return null;
	}
	
	
	/**
	 * Método implementado para cargar el detalle de una orden
	 * @param con
	 * @param liquidacionForm
	 * @param mapping
	 * @param usuario
	 * @param paciente 
	 * @return
	 */
	private ActionForward accionDetalleOrden(Connection con, LiquidacionServiciosForm liquidacionForm, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente) throws IPSException 
	{
		LiquidacionServicios mundoLiquidacion = new LiquidacionServicios();
		
		//Se toma el número de la solicitud
		liquidacionForm.setNumeroSolicitud(liquidacionForm.getListadoOrdenes("numeroSolicitud_"+liquidacionForm.getIndex()).toString());
		
		//*****************SE CONSULTA INFORMACIÓN DE LA ORDEN Y SUS SERVICIOS******************************************************
		llamadoProcesoReordenacion(con, mundoLiquidacion, usuario, paciente, liquidacionForm, true, true);
		//***************************************************************************************************************
		
		//********************VALIDACION DE LOS PERMISOS DE MODIFICACIÓN*************************************************
		//Según tipo de cirugía se verifica el permiso de modificar servicios cirugia
		if(liquidacionForm.getIndQx().equals(ConstantesIntegridadDominio.acronimoIndicativoCargoCirugia)||liquidacionForm.getIndQx().equals(ConstantesIntegridadDominio.acronimoIndicativoCargoCirugiaCargoDirecto))
		{
			liquidacionForm.setModificarServicios(Utilidades.tieneRolFuncionalidad(con, usuario.getLoginUsuario(), 699));
			liquidacionForm.setModificarLiquidacion(Utilidades.tieneRolFuncionalidad(con, usuario.getLoginUsuario(), 708));
			liquidacionForm.setModificarIndicativoCobrable(UtilidadTexto.getBoolean(ValoresPorDefecto.getIndicativoCobrableHonorariosCirugia(usuario.getCodigoInstitucionInt())));
		}
		//Según tipo de no cruento se verifica el permisos de modificar servicios no cruentos
		else if(liquidacionForm.getIndQx().equals(ConstantesIntegridadDominio.acronimoIndicativoCargoNoCruento)||liquidacionForm.getIndQx().equals(ConstantesIntegridadDominio.acronimoIndicativoCargoNoCruentoCargoDirecto))
		{
			liquidacionForm.setModificarServicios(Utilidades.tieneRolFuncionalidad(con, usuario.getLoginUsuario(), 701));
			liquidacionForm.setModificarLiquidacion(Utilidades.tieneRolFuncionalidad(con, usuario.getLoginUsuario(), 709));
			liquidacionForm.setModificarIndicativoCobrable(UtilidadTexto.getBoolean(ValoresPorDefecto.getIndicativoCobrableHonorariosNoCruento(usuario.getCodigoInstitucionInt())));
		}
		liquidacionForm.setModificarDatosOcupacionSala(Utilidades.tieneRolFuncionalidad(con, usuario.getLoginUsuario(), 700));
		//****************************************************************************************************************
		
		//*********SE CARGAN LAS ESTRUCTURAS DE LOS COMBOS**************************************************************
		liquidacionForm.setTiposSala(Utilidades.obtenerTiposSala(con, usuario.getCodigoInstitucionInt(), ConstantesBD.acronimoSi, ConstantesBD.acronimoNo));
		//Si hay tipo de sala entonces se cargan las salas
		if(Utilidades.convertirAEntero(liquidacionForm.getDatosActoQx("codigoTipoSala")+"", true)!=ConstantesBD.codigoNuncaValido)
			liquidacionForm.setSalas(UtilidadesSalas.obtenerSalas(con, usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion(), ConstantesBD.acronimoSi, liquidacionForm.getDatosActoQx("codigoTipoSala").toString()));
		liquidacionForm.setEspecialidades(UtilidadesSalas.obtenerEspecialidadesIntervienen(con, liquidacionForm.getNumeroSolicitud(), ConstantesBD.acronimoSi));
		if(liquidacionForm.isModificarLiquidacion())
		{
			liquidacionForm.setAsocios(HojaQuirurgica.obtenerAsociosDifAyuCirAnes(con, usuario.getCodigoInstitucionInt()));
			liquidacionForm.setProfesionales(UtilidadesAdministracion.obtenerProfesionales(con, usuario.getCodigoInstitucionInt(), ConstantesBD.codigoNuncaValido, true, true, ConstantesBD.codigoNuncaValido));
			liquidacionForm.setAsociosServicio(HojaQuirurgica.obtenerAsociosDifCirAnes(con, usuario.getCodigoInstitucionInt()));
		}
		//***************************************************************************************************************
		
		UtilidadBD.closeConnection(con);
		
		
		return mapping.findForward("detalleOrden");
	}
	
	

	/**
	 * Método implementado para realizar la ordenación del listado de cirugias a liquidar
	 * @param con
	 * @param liquidacionForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenarListado(Connection con, LiquidacionServiciosForm liquidacionForm, ActionMapping mapping) 
	{
		
		String[] indices = (String[])liquidacionForm.getListadoOrdenes("INDICES");
		int numOrdenes = liquidacionForm.getNumOrdenes();
		
		//se pasan la fecha de orden a Formato BD
		for(int i=0;i<numOrdenes;i++)
		{
			liquidacionForm.setListadoOrdenes("fechaOrden_"+i,UtilidadFecha.conversionFormatoFechaABD(liquidacionForm.getListadoOrdenes("fechaOrden_"+i).toString()));
			liquidacionForm.setListadoOrdenes("fechaCirugia_"+i,UtilidadFecha.conversionFormatoFechaABD(liquidacionForm.getListadoOrdenes("fechaCirugia_"+i).toString()));
		}
		
		liquidacionForm.setListadoOrdenes(Listado.ordenarMapa(indices,
				liquidacionForm.getIndice(),
				liquidacionForm.getUltimoIndice(),
				liquidacionForm.getListadoOrdenes(),
				numOrdenes));
		
		liquidacionForm.setListadoOrdenes("numRegistros",numOrdenes+"");
		liquidacionForm.setListadoOrdenes("INDICES",indices);
		
		//se pasan las fechas a formato aplicacion
		for(int i=0;i<numOrdenes;i++)
		{
			liquidacionForm.setListadoOrdenes("fechaOrden_"+i,UtilidadFecha.conversionFormatoFechaAAp(liquidacionForm.getListadoOrdenes("fechaOrden_"+i).toString()));
			liquidacionForm.setListadoOrdenes("fechaCirugia_"+i,UtilidadFecha.conversionFormatoFechaAAp(liquidacionForm.getListadoOrdenes("fechaCirugia_"+i).toString()));
		}
		liquidacionForm.setUltimoIndice(liquidacionForm.getIndice());
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listado");
	}

	/**
	 * Método implementado para iniciar el flujo de la liquidacion de servicios
	 * @param con
	 * @param liquidacionForm
	 * @param mapping
	 * @param usuario
	 * @param paciente 
	 * @param request 
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, LiquidacionServiciosForm liquidacionForm, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request) throws IPSException 
	{
		//******************VALIDACIONES INICIALES***********************************************
		ActionErrors errores = new ActionErrors();
		
		//Se hacen las validaciones generales del paciente
		RespuestaValidacion resp = UtilidadValidacion.esValidoPacienteCargado(con, paciente);
		if(!resp.puedoSeguir)
			errores.add("Error en validacion paciente", new ActionMessage(resp.textoRespuesta));
		//Se valida que se haya parametrizado el asocio de cirujano
		if(!UtilidadCadena.noEsVacio(ValoresPorDefecto.getAsocioCirujano(usuario.getCodigoInstitucionInt())))
			errores.add("", new ActionMessage("error.parametrosGenerales.faltaDefinirParametro","Asocio Cirujano"));
		//Se valida que se haya parametrizado el asocio de anestesiología
		if(!UtilidadCadena.noEsVacio(ValoresPorDefecto.getAsocioAnestesia(usuario.getCodigoInstitucionInt())))
			errores.add("", new ActionMessage("error.parametrosGenerales.faltaDefinirParametro","Asocio Anestesia"));
		//Se valida que se haya parametrizado el asocio de ayudante
		if(!UtilidadCadena.noEsVacio(ValoresPorDefecto.getAsocioAyudantia(usuario.getCodigoInstitucionInt())))
			errores.add("", new ActionMessage("error.parametrosGenerales.faltaDefinirParametro","Asocio Ayudantía"));
		//Se valida que se haya parametrizado el parámetro materiales quirurgicos por acto
		if(!UtilidadCadena.noEsVacio(ValoresPorDefecto.getMaterialesPorActo(usuario.getCodigoInstitucionInt())))
			errores.add("", new ActionMessage("error.parametrosGenerales.faltaDefinirParametro","¿Materiales por Acto Qx.?"));
		
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("paginaErroresActionErrors");
		}
		//*****************************************************************************************
		
		
		liquidacionForm.reset();
		liquidacionForm.setCodigoSexo(paciente.getCodigoSexo());
		liquidacionForm.setListadoOrdenes(LiquidacionServicios.consultarListadoOrdenes(con, paciente.getCodigoIngreso()+""));
		
		//Se verifica si solo hay una orden que no tiene pendiente el consumo de materiales se va directo a la liquidacion
		if(liquidacionForm.getNumOrdenes()==1&&!UtilidadTexto.getBoolean(liquidacionForm.getListadoOrdenes("consumoPendiente_0")+""))
		{
			liquidacionForm.setIndex(0);
			return accionDetalleOrden(con, liquidacionForm, mapping, usuario, paciente); //Se debe ir al flujo de inicio de la liquidación
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listado");
	}
	
	
	
}
