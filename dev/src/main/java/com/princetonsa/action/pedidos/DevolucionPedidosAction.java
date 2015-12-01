/*
 * DevolucionPedidosAction.java 
 * Autor			:  mdiaz
 * Creado el	:  17-sep-2004
 * 
 * Lenguaje		: Java
 * Compilador	: J2SDK 1.4.1_01
 * 
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 * */
package com.princetonsa.action.pedidos;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.inventarios.UtilidadInventarios;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.pedidos.DevolucionPedidosForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.pedidos.DetalleDevolucionPedidos;
import com.princetonsa.mundo.pedidos.DevolucionPedidos;
import com.princetonsa.pdf.DevolucionPedidosPdf;

/**
 * descripcion de esta clase
 *
 * @version 1.0, 17-sep-2004
 * @author <a href="mailto:miguel@PrincetonSA.com">Miguel Arturo Diaz</a>
 */
public class DevolucionPedidosAction extends Action {

	private Logger logger = Logger.getLogger(DevolucionPedidosAction.class);
	
	/**  * Lleva el numero de la secuencia a asignar en caso de que se queira ingresar manualmente  // Tarea Xplanner  */
	private String secuenciaAsignar;
	

	public void closeDBConnection (Connection con){
	  try{
			if ( con != null && !con.isClosed() )	{
				UtilidadBD.closeConnection(con);
			}
	  }
    catch(SQLException e){
      return;
    }
	}
	

	public Connection openDBConnection(){
		Connection con = null;
		try{	
			con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
		}
		catch(SQLException e){
			logger.warn("No se pudo abrir la conexión"+e.toString());
		}
    return con;
	}
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(	ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception{
		Connection con = null;
		DevolucionPedidosForm lForm = null; 		
		DevolucionPedidos mundo =null;
		UsuarioBasico usuario = null;
		String[] columns;
		ResultSetDecorator rs = null;
		String estado = "";

		try{

			if (form instanceof DevolucionPedidosForm)
			{

				con = openDBConnection();
				lForm = (DevolucionPedidosForm) form;
				estado = lForm.getEstado();
				logger.info("estado DevolucionPedidosAction=> "+estado);

				usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				mundo = new 	DevolucionPedidos(lForm.getMotivo(), lForm.getFechaDevolucion(), lForm.getHoraDevolucion(),  lForm.getUsuario(), lForm.getObservaciones(), usuario.getCodigoInstitucionInt());

				if( estado.equals("") ){
					lForm.reset();
					logger.warn("Estado no valido dentro del flujo de DevolucionPedidosAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					closeDBConnection(con);
					return mapping.findForward("paginaError");
				}

				if( estado.equals("seleccion_farmacia") ){
					lForm.reset();
					closeDBConnection(con);
					return mapping.findForward("paginaPrincipal");
				}


				if( estado.equals("comenzar_devolucion") )
				{
					lForm.setArticulosDevolucion(new HashMap());
					lForm.setCodigoDevolucion(mundo.getCodigoDevolucionDisponible(con));	
					lForm.setUsuario(usuario.getLoginUsuario());
					lForm.setFechaDevolucion(UtilidadFecha.conversionFormatoFechaAAp(UtilidadFecha.getFechaActual()));
					lForm.setHoraDevolucion(UtilidadFecha.getHoraActual());

					closeDBConnection(con);
					return mapping.findForward("paginaPrincipal");
				}
				if( estado.equals("comenzar_devolucion_1") )
				{
					lForm.setArticulosDevolucion(new HashMap());
					//lForm.setCodigoDevolucion(mundo.getCodigoDevolucionDisponible(con));
					lForm.setCodigoDevolucion(mundo.getSiguienteCodigoDevolucionDisponible(con)); 
					this.secuenciaAsignar = lForm.getCodigoDevolucion()+"";
					lForm.setUsuario(usuario.getLoginUsuario());

					closeDBConnection(con);
					return mapping.findForward("paginaPrincipal");
				}
				if( estado.equals("buscar_articulos"))
				{
					lForm.setUsuario(usuario.getLoginUsuario());
					lForm.setFechaDevolucion(UtilidadFecha.conversionFormatoFechaAAp(UtilidadFecha.getFechaActual()));
					lForm.setHoraDevolucion(UtilidadFecha.getHoraActual());

					lForm.setCodigoPedido(Integer.parseInt(lForm.getCodigoPedidoBusqueda()));
					lForm.setCodigoPedidoBusqueda("");
					columns = new String[1];
					columns[0] = "observaciones_generales";

					try{
						lForm.setArticulosConsulta(buscarArticulos(con, lForm));
						//rs = mundo.buscar(con, columns, "despacho_pedido_vw", ( " cod_pedido = " + lForm.getCodigoPedido() + " AND es_qx = '"+ConstantesBD.acronimoNo+"'" ), "");
						//segun lo hablado con german, debe mostral los qururgicos.
						rs = mundo.buscar(con, columns, "despacho_pedido_vw", ( " cod_pedido = " + lForm.getCodigoPedido()  ), "");
						if(rs.next())
							lForm.setObservacionesPedido(rs.getString("observaciones_generales"));
						else
							lForm.setObservacionesPedido("");
					}
					catch(Exception e){
						logger.error("error al obtener la observacion general del pedido numero " +lForm.getCodigoPedido() + " Excepcion: " + e ); 
					}

					closeDBConnection(con);
					lForm.setEstado("comenzar_devolucion");
					return mapping.findForward("paginaPrincipal");
				}


				// ESTADO ESPECIAL PARA DEVOLUCION DE UN SOLO PEDIDO COMO PETICION DE LA PAGINA DE 'MOVIMIENTO DE PEDIDOS'
				if( estado.equals("devolucion_unico_pedido"))
				{
					columns = new String[6];
					columns[0] = "cod_pedido";
					columns[1] = "fecha_solicitud";
					columns[2] = "hora_solicitud";
					columns[3] = "fecha_despacho";
					columns[4] = "hora_despacho";
					columns[5] = "observaciones_generales";

					rs = mundo.buscar(con, columns, "despacho_pedido_vw",  (" cod_pedido = " +lForm.getCodigoPedido()), "");

					//System.out .println("codigoPedido = " +lForm.getCodigoPedido()); 


					try{
						rs.next();
						lForm.clean(); 
						lForm.setDevolucionSimple("si");
						lForm.setCodigoPedido(rs.getInt("cod_pedido"));
						lForm.setObservacionesPedido(rs.getString("observaciones_generales"));
						lForm.setArticulosConsulta(buscarArticulos(con, lForm));
						lForm.setArticulosDevolucion(new HashMap());
						lForm.setCodigoDevolucion(mundo.getCodigoDevolucionDisponible(con));	
						lForm.setUsuario(usuario.getLoginUsuario());	
						lForm.setCodigoPedidoBusqueda("");
					}
					catch(Exception e){
						logger.error("error al procesar el estado especial por peticion de la pagina de movimiento de Pedidos \n Excepcion: " + e );
					}

					// SE PROCESÓ EN EL FORM 
					closeDBConnection(con);
					lForm.setEstado("comenzar_devolucion");
					return mapping.findForward("paginaPrincipal");
				}


				if( estado.equals("adicionar_detalle_devolucion")){
					// SE PROCESÓ EN EL FORM 
					closeDBConnection(con);
					lForm.setEstado("comenzar_devolucion");
					return mapping.findForward("paginaPrincipal");
				}


				if( estado.equals("eliminar_detalle_devolucion")){
					lForm.deleteDetalleDevolucion();

					closeDBConnection(con);
					lForm.setEstado("comenzar_devolucion");
					return mapping.findForward("paginaPrincipal");
				}


				if( estado.equals("guardar_devolucion"))
				{
					//*********VALIDACION CIERRE DE INVENTARIOS*************
					if(UtilidadInventarios.existeCierreInventarioParaFecha(mundo.getFechaDevolucion(),usuario.getCodigoInstitucionInt()))
					{
						//si existe cierre hay error!!!
						ActionErrors errores = new ActionErrors();
						errores.add("Existe cierre de inventarios para la fecha", new ActionMessage("error.inventarios.existeCierreInventarios",mundo.getFechaDevolucion()));
						saveErrors(request, errores);

						this.closeDBConnection(con);
						lForm.setEstado("comenzar_devolucion");
						return mapping.findForward("paginaPrincipal");

					}
					//*******************************************************
					lForm.fillDetalleDevolucionPedidos();
					mundo.setCodigo(lForm.getCodigoDevolucion());
					mundo.setEsQuirurgico(ConstantesBD.acronimoNo);
					mundo.setDetalleDevolucionPedidos(lForm.getDetalleDevolucionPedidos());

					//************VALIDAR FECHA/HORA DEVOLUCION*************************************
					ActionErrors errores = new ActionErrors();
					for(int i=0;i<mundo.getDetalleDevolucionPedidos().size();i++)
					{
						DetalleDevolucionPedidos detalle = (DetalleDevolucionPedidos)mundo.getDetalleDevolucionPedidos().get(i);
						String fechaHora = mundo.consultarFechaHoraDespacho(con,detalle.getPedido()+"");
						String[] vector = fechaHora.split(ConstantesBD.separadorSplit);
						if(!UtilidadFecha.compararFechas(mundo.getFechaDevolucion(),mundo.getHoraDevolucion(),vector[0],vector[1]).isTrue())
							errores.add("Fecha/Hora Devolucion anterior a fecha despacho",new ActionMessage("errors.fechaHoraAnteriorIgualActual","de la devolución","del despacho del pedido "+detalle.getPedido()));
					}
					//*******************************************************************************

					if(!errores.isEmpty())
					{
						lForm.setEstado("comenzar_devolucion");
						saveErrors(request,errores);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("paginaPrincipal");
					}


					// Tarea Xplanner 29435  
					if(!UtilidadTexto.isEmpty(this.secuenciaAsignar)){
						mundo.setSecuenciaAsignar(this.secuenciaAsignar);
					}

					if( mundo.insertar(con) == 0){
						logger.warn("No se pudo insertar la devolucion del pedido ");
						ArrayList atributosError = new ArrayList();
						atributosError.add("No se pudo insertar la devolucion del pedido.");							
						request.setAttribute("atributosError", atributosError);					
						request.setAttribute("codigoDescripcionError", "errors.notEspecific");
						closeDBConnection(con);
						return mapping.findForward("paginaError");
					}

					closeDBConnection(con);
					lForm.setEstado("detalle_devolucion");
					return mapping.findForward("paginaPrincipal");
				}


				if( estado.equals("volver_detalle_devolucion")){
					lForm.reset();
					closeDBConnection(con);
					lForm.setEstado("seleccion_farmacia");
					return mapping.findForward("paginaPrincipal");
				}


				if( estado.equals("volver_movimiento_pedidos")){
					lForm.reset();
					closeDBConnection(con);
					lForm.setEstado("seleccion_farmacia");
					return mapping.findForward("paginaMovimiento");
				}



				if( estado.equals("imprimir_devolucion"))
				{
					String nombreArchivo;
					Random r=new Random();
					nombreArchivo="/devolucionPedidos" + r.nextInt()  +".pdf";
					DevolucionPedidosPdf.pdfDevolucionPedidos( ValoresPorDefecto.getFilePath() +nombreArchivo, con, lForm, usuario, request);
					closeDBConnection(con);
					request.setAttribute("nombreArchivo", nombreArchivo);
					request.setAttribute("nombreVentana", "Devolucion de Pedidos");
					lForm.setEstado("detalle_devolucion");
					return mapping.findForward("abrirPdf");
				}

				if (estado.equals("redireccion"))
				{
					return accionRedireccion(con,lForm,response,mapping,request);
				}

				if(estado.equals("busquedaAvanzada"))
				{
					return accionBusquedaAvanzada(con,lForm,mapping,usuario);
				}
				if(estado.equals("resultadoBusquedaAvanzada"))
				{
					return accionResultadoBusquedaAvanzada(con,lForm,mapping,request);
				}

			}

			closeDBConnection(con);		
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return null;
}

		
	
	
	

	/**
	 * Método implementado para realizar la busqueda avanzada de pedidos
	 * @param con
	 * @param form
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionResultadoBusquedaAvanzada(Connection con, DevolucionPedidosForm form, ActionMapping mapping, HttpServletRequest request) 
	{
		//************VALIDACIONES PARÁMETROS*********************************************
		ActionErrors errores = new ActionErrors();
		if(!form.getPedidoInicial().equals("")&&!form.getPedidoFinal().equals(""))
		{
			if(Integer.parseInt(form.getPedidoFinal())<Integer.parseInt(form.getPedidoInicial()))
				errores.add("pedido inicial mayor a pedido final",new ActionMessage("errors.integerMenorIgualQue","El pedido inicial","el pedido final"));
		}
		else if(!form.getPedidoInicial().equals("")&&form.getPedidoFinal().equals(""))
			errores.add("pedido final requerido",new ActionMessage("errors.required","El pedido final"));
		else if(form.getPedidoInicial().equals("")&&!form.getPedidoFinal().equals(""))
			errores.add("pedido inicial requerido",new ActionMessage("errors.required","El pedido inicial"));
		
		if(!form.getFechaPedidoInicial().equals("")&&!form.getFechaPedidoFinal().equals(""))
		{
			if(UtilidadFecha.validarFecha(form.getFechaPedidoInicial())&&UtilidadFecha.validarFecha(form.getFechaPedidoFinal()))
			{
				if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(form.getFechaPedidoInicial(),form.getFechaPedidoFinal()))
					errores.add("fecha pedido inicial mayor a fecha pedido final",new ActionMessage("errors.fechaPosteriorIgualActual","pedido inicial","pedido final"));
			}
			else
			{
				if(!UtilidadFecha.validarFecha(form.getFechaPedidoInicial()))
					errores.add("fecha pedido inicial es inválida",new ActionMessage("errors.formatoFechaInvalido","pedido inicial"));
				
				if(!UtilidadFecha.validarFecha(form.getFechaPedidoFinal()))
					errores.add("fecha pedido final es inválida",new ActionMessage("errors.formatoFechaInvalido","pedido final"));
			}	
		}
		else if(!form.getFechaPedidoInicial().equals("")&&form.getFechaPedidoFinal().equals(""))
		{
			if(!UtilidadFecha.validarFecha(form.getFechaPedidoInicial()))
				errores.add("fecha pedido inicial es inválida",new ActionMessage("errors.formatoFechaInvalido","pedido inicial"));
			
			errores.add("fecha pedido final requerido",new ActionMessage("errors.required","La fecha pedido final"));
		}
		else if(form.getFechaPedidoInicial().equals("")&&!form.getFechaPedidoFinal().equals(""))
		{
			if(!UtilidadFecha.validarFecha(form.getFechaPedidoFinal()))
				errores.add("fecha pedido final es inválida",new ActionMessage("errors.formatoFechaInvalido","pedido final"));
			
			errores.add("fecha pedido inicial requerido",new ActionMessage("errors.required","La fecha pedido inicial"));
		}
		
		if(!form.getFechaDespachoInicial().equals("")&&!form.getFechaDespachoFinal().equals(""))
		{
			if(UtilidadFecha.validarFecha(form.getFechaDespachoInicial())&&UtilidadFecha.validarFecha(form.getFechaDespachoFinal()))
			{
				if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(form.getFechaDespachoInicial(),form.getFechaDespachoFinal()))
					errores.add("fecha Despacho inicial mayor a fecha Despacho final",new ActionMessage("errors.fechaPosteriorIgualActual","despacho inicial","despacho final"));
			}
			else
			{
				if(!UtilidadFecha.validarFecha(form.getFechaDespachoInicial()))
					errores.add("fecha despacho inicial es inválida",new ActionMessage("errors.formatoFechaInvalido","despacho inicial"));
				
				if(!UtilidadFecha.validarFecha(form.getFechaDespachoFinal()))
					errores.add("fecha despacho final es inválida",new ActionMessage("errors.formatoFechaInvalido","despacho final"));
			}	
		}
		else if(!form.getFechaDespachoInicial().equals("")&&form.getFechaDespachoFinal().equals(""))
		{
			if(!UtilidadFecha.validarFecha(form.getFechaDespachoInicial()))
				errores.add("fecha despacho inicial es inválida",new ActionMessage("errors.formatoFechaInvalido","despacho inicial"));
			
			errores.add("fecha despacho final requerido",new ActionMessage("errors.required","La fecha despacho final"));
		}
		else if(form.getFechaDespachoInicial().equals("")&&!form.getFechaDespachoFinal().equals(""))
		{
			if(!UtilidadFecha.validarFecha(form.getFechaDespachoFinal()))
				errores.add("fecha despacho final es inválida",new ActionMessage("errors.formatoFechaInvalido","despacho final"));
			
			errores.add("fecha despacho inicial requerido",new ActionMessage("errors.required","La fecha despacho inicial"));
		}
		//******************************************************************************
		
		if(errores.isEmpty())
		{
			HashMap campos = cargarCampos(form);
			form.setDespachos(DevolucionPedidos.consultarDespachos(con,campos));
		}
		else
		{
			form.setEstado("busquedaAvanzada");
			saveErrors(request,errores);
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaBusqueda");
	}


	/**
	 * Método implementado para cargar un mapa con los campos de busqueda de despacho
	 * @param form
	 * @return
	 */
	private HashMap cargarCampos(DevolucionPedidosForm form) 
	{
		HashMap campos = new HashMap();
		campos.put("codigoCentroCosto",form.getCodigoCentroCosto()+"");
		campos.put("codigoFarmacia",form.getCodigoFarmacia());
		campos.put("pedidoInicial",form.getPedidoInicial());
		campos.put("pedidoFinal",form.getPedidoFinal());
		campos.put("fechaPedidoInicial",form.getFechaPedidoInicial());
		campos.put("fechaPedidoFinal",form.getFechaPedidoFinal());
		campos.put("fechaDespachoInicial",form.getFechaDespachoInicial());
		campos.put("fechaDespachoFinal",form.getFechaDespachoFinal());
		campos.put("usuarioPedido",form.getUsuarioPedido());
		campos.put("usuarioDespacho",form.getUsuarioDespacho());
		
		return campos;
	}


	/**
	 * Método implementado para postular la busqueda  
	 * @param con
	 * @param form
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionBusquedaAvanzada(Connection con, DevolucionPedidosForm form, ActionMapping mapping, UsuarioBasico usuario) 
	{
		form.resetBusqueda();
		form.setUsuarios(Utilidades.obtenerUsuarios(con,usuario.getCodigoInstitucionInt(),false));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaBusqueda");
	}


	/**
	 * Método que realiza la paginación 
	 * @param con
	 * @param form
	 * @param response
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionRedireccion(Connection con, DevolucionPedidosForm form, HttpServletResponse response, ActionMapping mapping, HttpServletRequest request) 
	{
		try
		{
			form.setEstado("comenzar_devolucion");
		    UtilidadBD.cerrarConexion(con);
			response.sendRedirect(form.getLinkSiguiente());
			return null;
		}
		catch(Exception e)
		{
			logger.error("Error en accionRedireccion de DevolucionPedidoAction: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en DevolucionPedidoAction", "errors.problemasDatos", true);
		}
	}


	public HashMap buscarArticulos(Connection con, DevolucionPedidosForm form){
	  HashMap articulosHM = new HashMap();
	  DevolucionPedidos mundo = new DevolucionPedidos();
	  String[] columns = {
	  		"cod_articulo",
	  		"des_articulo",
			"unidad_medida_articulo",
	  		"saldo_despacho",
	  		"lote",
	  		"fecha_vencimiento",
	  		"estado",
	  		"nombre_estado"
	  		
	  };
	  //segun lo hablado con german, debe mostral los qururgicos.
	  //articulosHM = UtilidadBD.resultSet2HashMap(columns, mundo.buscar(con, columns, "detalle_despacho_pedido_vw", (" cod_pedido = " +form.getCodigoPedido()+ " AND centro_costo_solicitante = "+form.getCodigoCentroCosto() +" and centro_costo_solicitado = "+form.getCodigoFarmacia() + " and es_qx = '"+ConstantesBD.acronimoNo+"' " ), "des_articulo" ), false, false).getMapa();
	  articulosHM = UtilidadBD.resultSet2HashMap(columns, mundo.buscar(con, columns, "detalle_despacho_pedido_vw", (" cod_pedido = " +form.getCodigoPedido()+ " AND centro_costo_solicitante = "+form.getCodigoCentroCosto() +" and centro_costo_solicitado = "+form.getCodigoFarmacia()  ), "des_articulo" ), false, false).getMapa();
	  
	  return articulosHM;
}
	
	
	
	
}