/*
 * MovimientoPedidosAction.java 
 * Autor			:  mdiaz
 * Creado el	:  30-sep-2004
 * 
 * Lenguaje		: Java
 * Compilador	: J2SDK 1.4.1_01
 * 
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 * */
package com.princetonsa.action.pedidos;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.ValoresPorDefecto;
import util.inventarios.UtilidadInventarios;

import com.princetonsa.actionform.pedidos.MovimientoPedidosForm;
import com.princetonsa.actionform.pedidos.PedidosInsumosForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.pedidos.PedidosInsumos;
import com.princetonsa.pdf.MovimientoPedidosPdf;
import com.princetonsa.pdf.PedidosInsumosPdf;

/**
 * descripcion de esta clase
 *
 * @version 1.0, 30-sep-2004
 * @author <a href="mailto:miguel@PrincetonSA.com">Miguel Arturo Diaz</a>
 */
public class MovimientoPedidosAction extends Action{
	
	/**
	 * Logger para la visualización de los mensajes de error
	 */
	private Logger logger = Logger.getLogger(MovimientoPedidosAction.class);
	
	
	/**
	 * Método que cierra la conexión de la base de datos
	 * @param con
	 */
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
	
	
	/**
	 * Método que abre una nueva conexión en la base de datos
	 * @return
	 */
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
	@SuppressWarnings("unchecked")
	public ActionForward execute(	ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
	{
		Connection con = null;
		MovimientoPedidosForm lForm = null; 
		UsuarioBasico usuario = null;
		String whereClause;
		String estado = "";

		try{
			if (form instanceof MovimientoPedidosForm)
			{

				con = openDBConnection();
				lForm = (MovimientoPedidosForm) form;
				estado = lForm.getEstado(); 
				usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

				logger.warn("El estado=> "+estado);

				if( estado.equals("") )
				{
					lForm.reset();
					logger.warn("Estado no valido dentro del flujo de MovimientoPedidosAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					closeDBConnection(con);
					return mapping.findForward("paginaError");
				}
				//*********ESTADO EMPEZAR********************************************************
				if(estado.equals("empezar"))
				{
					return accionEmpezar(con,lForm,mapping,usuario,request);
				}
				//*******************************************************************************

				//*********ESTADO COMENZAR_BUSQUEDA***********************************************
				if( estado.equals("comenzar_busqueda") )
				{
					return accionComenzarBusqueda(con,lForm,mapping,usuario,request);

				}
				//**********************************************************************************

				//*******ESTADO REALIZAR_BUSQUEDA**************************************************
				if( estado.equals("realizar_busqueda") )
				{

					whereClause = "";

					if(lForm.getSearchCodigoPedido() != -1)
						whereClause = insertIntoWhereClause(whereClause, "cod_pedido = '" +lForm.getSearchCodigoPedido()+"'");

					if(lForm.getUrgente() != -1)
						whereClause = insertIntoWhereClause(whereClause, "es_urgente = '" +(lForm.getUrgente() == 1? ValoresPorDefecto.getValorTrueParaConsultas() : ValoresPorDefecto.getValorFalseParaConsultas())+"'");

					if(lForm.getSearchEstadoPedido() != -1)
						whereClause = insertIntoWhereClause(whereClause, "cod_estado = '" +lForm.getSearchEstadoPedido()+"'");


					if(!lForm.getFechaPedido().equals(""))
						whereClause = insertIntoWhereClause(whereClause, "fecha_solicitud = '"+UtilidadFecha.conversionFormatoFechaABD( lForm.getFechaPedido()) +"'"); 


					if(lForm.getSearchCodigoFarmacia() > 0)
						whereClause = insertIntoWhereClause(whereClause, "cod_farmacia = '"+lForm.getSearchCodigoFarmacia()+"'");

					if(lForm.getSearchCodigoCentroCosto() != -1 && lForm.getSearchCodigoCentroCosto() != ConstantesBD.codigoCentroCostoTodos)
						whereClause = insertIntoWhereClause(whereClause, "cod_centro_costo = '"+lForm.getSearchCodigoCentroCosto()+"'");
					else
					{
						String[] vector = lForm.getCentroAtencion().split(ConstantesBD.separadorSplit);
						if(Integer.parseInt(vector[0])!=0)
							whereClause = insertIntoWhereClause(whereClause, "cod_centro_atencion = '"+vector[0]+"'");
					}

					if(!lForm.getFechaDespacho().equals(""))
						whereClause = insertIntoWhereClause(whereClause, "fecha_despacho = '" +UtilidadFecha.conversionFormatoFechaABD(lForm.getFechaDespacho()) +"'");

					if(!lForm.getUsuarioPedido().equals(""))
						whereClause = insertIntoWhereClause(whereClause, "usuario = '" +lForm.getUsuarioPedido().toLowerCase()+"'");

					if(!lForm.getUsuarioDespacho().equals(""))
						whereClause = insertIntoWhereClause(whereClause, "usuario_despacho = '" + lForm.getUsuarioDespacho().toLowerCase()+"'"); 


					lForm.setPedidos(busquedaPedidos(con,whereClause, lForm.getOrderByField(), (lForm.getOrderDirection() == 0?"DESC":"ASC"),"despacho_pedido_vw"));

					closeDBConnection(con);
					lForm.setEstado("comenzar_busqueda");
					return mapping.findForward("paginaPrincipal");
				}
				//***************************************************************************************

				//********ESTADO MOSTRAR_RESULTADOS_BUSQUEDA******************************************
				if( estado.equals("mostrar_resultados_busqueda") )
				{
					closeDBConnection(con);
					lForm.setEstado("comenzar_busqueda");
					return mapping.findForward("paginaPrincipal");
				}
				//**************************************************************************************

				//********ESTADO MOSTRAR_DETALLE*******************************************************
				if( estado.equals("mostrar_detalle") || estado.equals("volver_movimiento") )
				{
					return accionMostrarDetalle(con,lForm,mapping);
				}
				//*************************************************************************************


				//********ESTADO VOLVER_BUSQUEDA*********************************************************
				if( estado.equals("volver_busqueda") )
				{
					lForm.setArticulos(new HashMap());
					closeDBConnection(con);
					lForm.setEstado("comenzar_busqueda");
					return mapping.findForward("paginaPrincipal");
				}
				//******************************************************************************************

				//*******ESTADO IMPRIMIR_BUSQUEDA***********************************************************
				if( estado.equals("imprimir_busqueda") )
				{
					String nombreArchivo;
					Random r=new Random();
					nombreArchivo="/MovimientoPedidos" + r.nextInt()  +".pdf";
					MovimientoPedidosPdf.pdfMovimientoPedidos(ValoresPorDefecto.getFilePath() + nombreArchivo, lForm, usuario, request);
					closeDBConnection(con);
					request.setAttribute("nombreArchivo", nombreArchivo);
					request.setAttribute("nombreVentana", "Movimiento Insumos");
					return mapping.findForward("abrirPdf");
				}
				//*********************************************************************************************

				/**			ESTADOS DE LA MODIFICACION **/
				//****ESTADO EMPEZAR MODIFICACION*********************************************************
				else if(estado.equals("empezar_modificacion"))
				{
					return accionEmpezarModificacion(con,  lForm, mapping,usuario);

				}
				//******************************************************************************************

				//****ESTADO ELIMINAR INSUMO*****************************************************************
				else if (estado.equals("eliminarInsumo"))
				{
					return accionEliminarInsumo(con,lForm,mapping);
				}
				//********************************************************************************************

				//***ESTADO AGREGAR***************************************************************************
				else if (estado.equals("agregar"))
				{
					return accionAgregarOtro(con,lForm,response,request);

				}
				//********************************************************************************************

				//******ESTADO GUARDAR_MODIFICACIÒN*******************************************************
				else if(estado.equals("guardar_modificacion"))
				{
					return this.accionGuardarModificacion(lForm, con, usuario, mapping, request);
				}
				//**********************************************************************************************

				//*****ESTADO IMPRIMIR (PARA MODIFICACIÓN/ANULACION)******************************************
				else if (estado.equals("Imprimir"))
				{
					String nombreArchivo;
					Random r=new Random();
					nombreArchivo="/Modificacion Pedido" + r.nextInt()  +".pdf";
					PedidosInsumosForm pedidoForm = llenarFormPedido(con,lForm);
					PedidosInsumosPdf.pdfPedidosInsumos(ValoresPorDefecto.getFilePath() + nombreArchivo, pedidoForm, usuario,null, request);
					UtilidadBD.cerrarConexion(con);
					request.setAttribute("nombreArchivo", nombreArchivo);
					request.setAttribute("nombreVentana", "Pedidos Insumos");
					return mapping.findForward("abrirPdf");
				}
				//*********************************************************************************************

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
	 * Método implementado para cargar el detalle de un pedido
	 * @param con
	 * @param lForm
	 * @param mapping
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private ActionForward accionMostrarDetalle(Connection con, MovimientoPedidosForm lForm, ActionMapping mapping) 
	{
		lForm.setEstado("mostrar_detalle");
		PedidosInsumos pedidos = new PedidosInsumos();
		pedidos.setNumeroPedido(lForm.getCodigoPedido());
		
		//Se verifica si el pedido es quirurgico
		lForm.setEsQuirurgico(lForm.getPedidos().get("es_qx["+lForm.getSelectedIndex()+"]")+"");
		if(UtilidadTexto.getBoolean(lForm.getEsQuirurgico()))
			lForm.setDatosQx(pedidos.cargarDatosPeticionPedido(con));
		
	 	//se consultan los datos de la anulacion
	 	lForm.setDatosAnulacion(pedidos.cargarDatosAnulacion(con));
	 	lForm.setDatosAnulacion("hora_0",UtilidadFecha.convertirHoraACincoCaracteres(lForm.getDatosAnulacion("hora_0")+""));
		
		//se consultan los artículos
	 	lForm.setArticulos(busquedaArticulos(con, lForm.getCodigoPedido()));
		closeDBConnection(con);
		return mapping.findForward("paginaPrincipal");
	}


	/**
	 * Método que llena el formulario de PedidoInsumos con los
	 * Datos del formulario de MovimientoPedidosForm
	 * @param con
	 * @param form
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private PedidosInsumosForm llenarFormPedido(Connection con, MovimientoPedidosForm form) 
	{
		int auxI0 = 0;
		int contador = 0;
		
		PedidosInsumosForm pedidoForm = new PedidosInsumosForm();
		pedidoForm.reset();
		
		Iterator iterador=form.getColeccionPedidos().iterator();
		HashMap encabezado=(HashMap)iterador.next();
		
		pedidoForm.setCheckPrioridadPedido((encabezado.get("urgente")+"").equals("true")?"on":"off");
		
		auxI0 = Integer.parseInt(encabezado.get("codigo_estado")+"");
		if(auxI0==ConstantesBD.codigoEstadoPedidoTerminado)
			pedidoForm.setCheckTerminarPedido("on");
		else if(auxI0==ConstantesBD.codigoEstadoPedidoAnulado)
			pedidoForm.setCheckAnularPedido(form.getCheckAnularPedido());
		
		pedidoForm.setNumeroPedido(Integer.parseInt(encabezado.get("codigo")+""));
		pedidoForm.setFechaHoraPedido(
			UtilidadFecha.conversionFormatoFechaAAp(encabezado.get("fecha")+"")+" - "+
			UtilidadFecha.convertirHoraACincoCaracteres(encabezado.get("hora")+""));
		
		pedidoForm.setPedidosMap("fechaHoraGrabacion_0",
			UtilidadFecha.conversionFormatoFechaAAp(encabezado.get("fecha_grabacion")+"")+" - "+
			UtilidadFecha.convertirHoraACincoCaracteres(encabezado.get("hora_grabacion")+""));
		
		try 
		{
			pedidoForm.setNombreCentroCosto(UtilidadValidacion.getNombreCentroCosto(con,Integer.parseInt(encabezado.get("centro_costo_solicitante")+"")));
			pedidoForm.setNombreFarmacia(UtilidadValidacion.getNombreCentroCosto(con,Integer.parseInt(encabezado.get("centro_costo_solicitado")+"")));
		
		} 
		catch (Exception e) 
		{
			logger.warn("Error al cargar descripciones de centro costo y farmacia: "+e);
		} 
		
		
		pedidoForm.setObservacionesGenerales(encabezado.get("observaciones_generales")+"");
		
		//datos de la anulación
		pedidoForm.setPedidosMap("motivoAnulacion",encabezado.get("motivo_anulacion"));
		pedidoForm.setPedidosMap("usuarioAnulacion",encabezado.get("usuario_anulacion"));
		pedidoForm.setPedidosMap("fechaAnulacion",UtilidadFecha.conversionFormatoFechaAAp(encabezado.get("fecha_anulacion")+""));
		pedidoForm.setPedidosMap("horaAnulacion",UtilidadFecha.convertirHoraACincoCaracteres(encabezado.get("hora_anulacion")+""));
		
		
		iterador = form.getColeccionPedidos().iterator();
		while(iterador.hasNext())
		{
			encabezado = (HashMap)iterador.next();
			
			pedidoForm.setPedidosMap("articulo_"+contador,encabezado.get("articulo")+" - "+encabezado.get("descripcion"));
			pedidoForm.setPedidosMap("concentracion_"+contador,encabezado.get("concentracion"));
			pedidoForm.setPedidosMap("formaFarmaceutica_"+contador,encabezado.get("forma_farmaceutica"));
			pedidoForm.setPedidosMap("unidadMedida_"+contador,encabezado.get("unidad_medida"));
			pedidoForm.setPedidosMap("cantidadDespacho_"+contador,encabezado.get("cantidad"));
			pedidoForm.setPedidosMap("existenciaXAlmacen_"+contador,encabezado.get("existencias"));
			contador ++;
		}
		
		
		pedidoForm.setNumeroIngresos(contador);
		
		
		return pedidoForm;
	}


	/**
	 * Método implementado para modificar/temrin
	 * @param form
	 * @param con
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private ActionForward accionGuardarModificacion(MovimientoPedidosForm form, Connection con, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request) 
	{
		PedidosInsumos mundo = new PedidosInsumos();
		//variables auxiliares necesarias
		boolean esUrgente = false;
		boolean huboCambio = false;
		int estadoPedido = 0;
		int validar = 0;
		String estadoPedidoStr = "";
		String logInfo = "";
		int k;
		
		//se verifica si se dejó Urgente o No Urgente
		if( form.getCheckPrioridadPedido().equals("on") )
			esUrgente = true;
		else
			esUrgente = false;

		
		
		estadoPedido = ConstantesBD.codigoEstadoPedidoSolicitado;
		estadoPedidoStr = "Solicitado";
		
		//se verifica si se terminó el pedido
		if (form.getCheckTerminarPedido().equals("on") )
		{
			huboCambio = true;
			estadoPedido = ConstantesBD.codigoEstadoPedidoTerminado;
			estadoPedidoStr = "Terminado";
		}
		logger.info("Hubo Cambio 1=> "+huboCambio);
		//se verifica si se anuló el pedido
		if (form.getCheckAnularPedido().equals("on") )
		{
			huboCambio = true;
			estadoPedido = ConstantesBD.codigoEstadoPedidoAnulado;
			estadoPedidoStr = "Anulado";
			
			//SE VERIFICA EL CIERRE DE INVNETARIOS PARA LA ANULACIÓN
			if(UtilidadInventarios.existeCierreInventarioParaFecha(UtilidadFecha.getFechaActual(),usuario.getCodigoInstitucionInt()))
			{
				//si existe cierre hay error!!!
	         	ActionErrors errores = new ActionErrors(); 
	            errores.add("Existe cierre de inventarios para la fecha", new ActionMessage("error.inventarios.existeCierreInventarios",UtilidadFecha.getFechaActual()));
	            saveErrors(request, errores);
	            
	            closeDBConnection(con);
	            return mapping.findForward("modificarPedido");
			}
		}
		logger.info("Hubo Cambio 2=> "+huboCambio);
		
		//SE VERIFICA SI HUBO MODIFICACION
		if(huboCambio||huboModificacionPedido(form))
		{
			try
			{
				validar = mundo.modificarPedidoYDetalle(
					con, 
					form.getCodigoPedido(), 
					form.getSearchCodigoCentroCosto(), 
					form.getSearchCodigoFarmacia(), 
					esUrgente, 
					form.getObservacionesGenerales(), 
					estadoPedido, 
					form.getMotivoAnulacion(), 
					usuario.getLoginUsuario(), 
					form.getPedidosMap(), 
					form.getNumeroIngresos(),
					form.getPedidosEliminacionMap(),
					ConstantesBD.inicioTransaccion);
			}
			catch(SQLException e)
			{
				validar = -1;
				logger.warn("Error al modificar el pedido: "+e);
			}
	
			if (validar <1){
				logger.warn("No se pudo modificar el pedido");
				closeDBConnection(con);
				ArrayList atributosError = new ArrayList();
				atributosError.add("No se pudo realizar la modificacion del pedido");
				request.setAttribute("atributosError", atributosError);
				return mapping.findForward("paginaError");
			}
			
			
		 	logInfo = form.getLogInfo() +
			"\n ==============INFORMACION MODIFICADA===================" +
			"\n ---------------Pedido-----------------" +
			"\n*  Código  [" +form.getCodigoPedido()  +"] "+
			"\n*  Estado [ "+estadoPedidoStr+" ] " +
			"\n*  Centro de Costo que Solicita  ["+form.getCentroCosto()   +"] " +
			"\n*  Farmacia  ["+form.getFarmacia()  +"] " +
			"\n*  Es Urgente  ["+( form.getCheckPrioridadPedido().equals("on")?"Si":"No" )  +"] " +
			"\n*  Observaciones Generales ["+form.getObservacionesGenerales()  +"] " ;
		 	
		 	//se verifica si hubo motivo de anulacion
		 	if(estadoPedido == ConstantesBD.codigoEstadoPedidoAnulado)
		 		logInfo += "\n*  Motivo de Anulación ["+form.getMotivoAnulacion()+"] ";
		 	
			logInfo += "\n ------------Detalle Pedido--------------";
		 	
			for(k=0; k < form.getNumeroIngresos(); k++ )
			{
				logInfo += 
				"\n*   Artículo ["+form.getPedidosMap("articulo_"+k)   + "] " +
				"\n*   Unidad de Medida ["+form.getPedidosMap("unidadMedidaArticulo_"+k)  +"] " +
				"\n*   Cantidad Pedida ["+ form.getPedidosMap("cantidadDespacho_"+k)  +"] " +
				"\n*   Existencias ["+form.getPedidosMap("existenciaXAlmacen_"+k)  +"] \n " ;
			}
			
			logInfo +=	
		 	"\n ========================================================\n\n\n " ;
		 	
			LogsAxioma.enviarLog(ConstantesBD.logPedidosInsumos, logInfo, ConstantesBD.tipoRegistroLogModificacion, usuario.getLoginUsuario());       
		}
		
		
		//se carga de nuevo el pedido
        form.setColeccionPedidos(mundo.listarPedidos(con, form.getCodigoPedido()));
		
		closeDBConnection(con);
		return mapping.findForward("resumenModificacion");
	}


	/**
	 * Método que verifica si un Pedido fue modificado
	 * @param form
	 * @return
	 */
	private boolean huboModificacionPedido(MovimientoPedidosForm form) 
	{
		String auxS0 = "";
		int articuloI0 = 0, articuloI1 = 0;
		int cantidadI0 = 0, cantidadI1 = 0;
		boolean huboModificacion = false;
		boolean existeArticulo = false;
		
		//SE VERIFICA CHEQUEO 'URGENTE'
		auxS0 = form.getPedidosAntiguosMap("urgente") + "";
		if(!auxS0.equals(form.getCheckPrioridadPedido()))
			huboModificacion = true;
		
		logger.info("hubo modificacion 1 "+huboModificacion);
		//SE VERIFICAN OBSERVACIONES GENERALES
		
		auxS0 = form.getPedidosAntiguosMap("observaciones") + "";
		logger.info("auxS0=> "+auxS0+", observaciones=> "+form.getObservacionesGenerales());
		logger.info("auxS0=> "+auxS0.length()+", observaciones=> "+form.getObservacionesGenerales().length());
		if(!auxS0.equals(form.getObservacionesGenerales()))
			huboModificacion = true;
		logger.info("hubo modificacion 2 "+huboModificacion);
		//*****VERIFICACIONES DEL DETALLE*************
		//si tienen diferente número de artículos hubo modificacion
		int numArtAntiguos = Integer.parseInt(form.getPedidosAntiguosMap("numRegistros")+"");
		if(numArtAntiguos!=form.getNumeroIngresos())
		{
			huboModificacion = true;
			logger.info("hubo modificacion 3 "+huboModificacion);
		}
		else
		{
			//se iteran los articulos antes de la modificacion
			for(int i=0;i<numArtAntiguos;i++)
			{
				existeArticulo = false;
				articuloI0 = Integer.parseInt(form.getPedidosAntiguosMap("codigoArticulo_"+i)+"");
				cantidadI0 = Integer.parseInt(form.getPedidosAntiguosMap("cantidadDespacho_"+i)+"");
					
				//se iteran articulos despues de la modificacion
				for(int j=0;j<form.getNumeroIngresos();j++)
				{
					articuloI1 = Integer.parseInt(form.getPedidosMap("codigoArticulo_"+j)+"");
					cantidadI1 = Integer.parseInt(form.getPedidosMap("cantidadDespacho_"+j)+"");
					
					if(articuloI0==articuloI1)
					{
						existeArticulo=true;
						//ahora se verifican cantidades
						if(cantidadI0!=cantidadI1)
							huboModificacion = true;
						logger.info("hubo modificacion cantidad["+i+","+j+"] "+huboModificacion);
					}
				}
				
				//si no existe el articulo quiere decir que fue eliminado
				if(!existeArticulo)
					huboModificacion = true;
				logger.info("hubo modidifcacion articulo["+i+"] "+huboModificacion);
			}
		}
		
		return huboModificacion;
	}


	/**
	 * Método implementado para agregar un artículo en la modificación
	 * del pedido
	 * @param con
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward accionAgregarOtro(Connection con, MovimientoPedidosForm form, HttpServletResponse response, HttpServletRequest request) 
	{
		int k=form.getNumeroIngresos();
        String codigosInsertados = form.getCodigosArticulosInsertados(); 
        
        
        codigosInsertados+=form.getPedidosMap("codigoArticulo_"+k) + ",";
        form.setCodigosArticulosInsertados(codigosInsertados);
        
        
        //se acomoda el campo artículo
        form.setPedidosMap("articulo_"+k,
        form.getPedidosMap("codigoArticulo_"+k)+"-"+form.getPedidosMap("descripcionArticulo_"+k));
        
       	form.setPedidosMap("existe_"+k, "false");
       	form.setPedidosMap("accion_"+k, "add");
       	
        form.setNumeroIngresos(k+1);
        
        closeDBConnection(con);
        return redireccion(form,response,request);
	}
	
	/**
     * Metodo implementado para posicionarse en la ultima
     * pagina del pager.
     * @param form
     * @param response
     * @param request
     * @return null
     */
    public ActionForward redireccion (	
    		MovimientoPedidosForm form,
			HttpServletResponse response,
			HttpServletRequest request)
    {
        
    	
    		int offset = 0;
        	logger.info("ultimaPage=> "+request.getParameter("ultimaPage"));
            if(request.getParameter("ultimaPage")==null)
            {
            	logger.info("maxPageItems=> "+form.getMaxPageItems());
               if(form.getNumeroIngresos() > form.getMaxPageItems())
                   offset = form.getMaxPageItems();
                
                try 
                {
                    response.sendRedirect("modificarAnularPedido.jsp?pager.offset="+offset);
                } catch (IOException e) 
                {
                    
                    e.printStackTrace();
                }
            }
            else
            {    
                String ultimaPagina=request.getParameter("ultimaPage");
                
                int posOffSet=ultimaPagina.indexOf("offset=")+7;
                offset = Integer.parseInt(ultimaPagina.substring(posOffSet,ultimaPagina.length() ));
                    
                if(form.getNumeroIngresos()>(offset+form.getMaxPageItems()))
                    offset=offset+form.getMaxPageItems();
                 
                try 
                {
                    response.sendRedirect(ultimaPagina.substring(0,posOffSet)+offset);
                } 
                catch (IOException e) 
                {
                    
                    e.printStackTrace();
                }
         }
           return null;
     }
    


	/**
	 * Método implementado para eliminar un artículo en la modificacion
	 * del pedido
	 * @param con
	 * @param form
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEliminarInsumo(Connection con, MovimientoPedidosForm form, ActionMapping mapping) 
	{
		int k=0;
		int auxI0 = 1;
	    int esUltimo=form.getNumeroIngresos()-1; //se obtiene posición del último elemento
	    String codigoInsertados = ""; //listado de los articulos que ya estan seleccionados
	    boolean existe = false;
	    
	    
	    while(k < form.getNumeroIngresos())
	    {
	       
	        String [] dato= String.valueOf(form.getPedidosMap("articulo_"+k)).split("-");
	        existe = UtilidadTexto.getBoolean(form.getPedidosMap().get("existe_"+k)+""); 
	                                     
	        if(form.getCodigoArticulo().equals(dato[0]))
	       {
	        	// si el artículo existe se elimina
	          	if(existe == true)
	            {
	          		if(form.getPedidosEliminacionMap("numRegistros")==null)
	          			auxI0 = 1;
	          		else
	          			auxI0 = Integer.parseInt(form.getPedidosEliminacionMap("numRegistros")+"") + 1;
	          		
	          		form.setPedidosEliminacionMap("numRegistros",auxI0+"");
	          		
	          		form.setPedidosEliminacionMap("articulo_"+(auxI0-1),form.getPedidosMap("articulo_"+k));
                    form.setPedidosEliminacionMap("codigoArticulo_"+(auxI0-1),form.getPedidosMap("codigoArticulo_"+k));
                    form.setPedidosEliminacionMap("descripcionArticulo_"+(auxI0-1),form.getPedidosMap("descripcionArticulo_"+k));
                    form.setPedidosEliminacionMap("unidadMedidaArticulo_"+(auxI0-1),form.getPedidosMap("unidadMedidaArticulo_"+k));
                    form.setPedidosEliminacionMap("cantidadDespachadaArticulo_"+(auxI0-1),form.getPedidosMap("cantidadDespachadaArticulo_"+k));
                    form.setPedidosEliminacionMap("autorizacionArticulo_"+(auxI0-1),form.getPedidosMap("autorizacionArticulo_"+k));
                    form.setPedidosEliminacionMap("fueEliminadoArticulo_"+(auxI0-1),form.getPedidosMap("fueEliminadoArticulo_"+k));
                    form.setPedidosEliminacionMap("tipoPosArticulo_"+(auxI0-1),form.getPedidosMap("tipoPosArticulo_"+k));
                    form.setPedidosEliminacionMap("existenciaXAlmacen_"+(auxI0-1),form.getPedidosMap("existenciaXAlmacen_"+k));
                    form.setPedidosEliminacionMap("cantidadDespacho_"+(auxI0-1),form.getPedidosMap("cantidadDespacho_"+k));
                    form.setPedidosEliminacionMap("existe_"+(auxI0-1),form.getPedidosMap("existe_"+k));
                    form.setPedidosEliminacionMap("accion_"+(auxI0-1),form.getPedidosMap("accion_"+k));
	          		
	            }
	         
	          	
          		if(k == esUltimo)
                {
                	form.getPedidosMap().remove("articulo_"+k);
	              	form.getPedidosMap().remove("codigoArticulo_"+k);
	              	form.getPedidosMap().remove("descripcionArticulo_"+k);  
	              	form.getPedidosMap().remove("unidadMedidaArticulo_"+k);
	              	form.getPedidosMap().remove("cantidadDespachadaArticulo_"+k);
	              	form.getPedidosMap().remove("autorizacionArticulo_"+k);
	              	form.getPedidosMap().remove("fueEliminadoArticulo_"+k);
	              	form.getPedidosMap().remove("tipoPosArticulo_"+k);
	              	form.getPedidosMap().remove("existenciaXAlmacen_"+k);
	              	form.getPedidosMap().remove("cantidadDespacho_"+k);
	              	form.getPedidosMap().remove("existe_"+k);
	              	form.getPedidosMap().remove("accion_"+k);
                }
                else
                {
                	for(int j=k;j<form.getNumeroIngresos()-1;j++)
                	{
	                    form.setPedidosMap("articulo_"+j,form.getPedidosMap("articulo_"+(j+1)));
	                    form.setPedidosMap("codigoArticulo_"+j,form.getPedidosMap("codigoArticulo_"+(j+1)));
	                    form.setPedidosMap("descripcionArticulo_"+j,form.getPedidosMap("descripcionArticulo_"+(j+1)));
	                    form.setPedidosMap("unidadMedidaArticulo_"+j,form.getPedidosMap("unidadMedidaArticulo_"+(j+1)));
	                    form.setPedidosMap("cantidadDespachadaArticulo_"+j,form.getPedidosMap("cantidadDespachadaArticulo_"+(j+1)));
	                    form.setPedidosMap("autorizacionArticulo_"+j,form.getPedidosMap("autorizacionArticulo_"+(j+1)));
	                    form.setPedidosMap("fueEliminadoArticulo_"+j,form.getPedidosMap("fueEliminadoArticulo_"+(j+1)));
	                    form.setPedidosMap("tipoPosArticulo_"+j,form.getPedidosMap("tipoPosArticulo_"+(j+1)));
	                    form.setPedidosMap("existenciaXAlmacen_"+j,form.getPedidosMap("existenciaXAlmacen_"+(j+1)));
	                    form.setPedidosMap("cantidadDespacho_"+j,form.getPedidosMap("cantidadDespacho_"+(j+1)));
	                    form.setPedidosMap("existe_"+j,form.getPedidosMap("existe_"+(j+1)));
	                    form.setPedidosMap("accion_"+j,form.getPedidosMap("accion_"+(j+1)));
                	}
                	
                	form.getPedidosMap().remove("articulo_"+esUltimo);
	              	form.getPedidosMap().remove("codigoArticulo_"+esUltimo);
	              	form.getPedidosMap().remove("descripcionArticulo_"+esUltimo);  
	              	form.getPedidosMap().remove("unidadMedidaArticulo_"+esUltimo);
	              	form.getPedidosMap().remove("cantidadDespachadaArticulo_"+esUltimo);
	              	form.getPedidosMap().remove("autorizacionArticulo_"+esUltimo);
	              	form.getPedidosMap().remove("fueEliminadoArticulo_"+esUltimo);
	              	form.getPedidosMap().remove("tipoPosArticulo_"+esUltimo);
	              	form.getPedidosMap().remove("existenciaXAlmacen_"+esUltimo);
	              	form.getPedidosMap().remove("cantidadDespacho_"+esUltimo);
	              	form.getPedidosMap().remove("existe_"+esUltimo);
	              	form.getPedidosMap().remove("accion_"+esUltimo);
                	
                }   
                form.setNumeroIngresos(form.getNumeroIngresos()-1);
	          	
	            
	                   
	        }
	        //si no es el articulo se añade a la lista de insertados
            else
            {
            	if(!codigoInsertados.equals(""))
            		codigoInsertados += ",";
            	codigoInsertados += form.getPedidosMap("codigoArticulo_"+k) + "";
            }
	        
	        k ++;
	    }
	    
	    ///se asignan los nuevos articulos ya insertados
        form.setCodigosArticulosInsertados(codigoInsertados);
        
	    closeDBConnection(con);
		return mapping.findForward("modificarPedido");
	}


	/**
	 * Método implementado para postular la modificación del pedido
	 * @param con
	 * @param form
	 * @param mapping
	 * @param usuario
	 */
	@SuppressWarnings("unchecked")
	private ActionForward accionEmpezarModificacion(Connection con, MovimientoPedidosForm form, ActionMapping mapping, UsuarioBasico usuario) 
	{
	      String logInfo = "";
	      String codigosInsertados = ""; //lista de los articulos ya insertados
	     
	      form.resetModificacion(); // reset para los datos de modificacion
	      form.setSubEstado("modificar"); //se asigna subEstado al formulario
	      form.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
	      form.setTipoTransaccionPedido(ValoresPorDefecto.getCodigoTransaccionPedidos(usuario.getCodigoInstitucionInt(), true));
	      //se cargan las parejas clase-grupo
	      form.setParejasClaseGrupo(
	      	UtilidadInventarios.obtenerInterseccionClaseGrupo(
	      		form.getSearchCodigoCentroCosto(),
				form.getSearchCodigoFarmacia()));
	      
	      //SE CONSULTA EL ENCABEZADO DEL PEDIDO ***********************************************
	      HashMap encabezadoPedido = busquedaPedidos(con," cod_pedido = '" +form.getCodigoPedido()+"'" , "", "","despacho_pedido_vw");
	      form.setFechaHoraGrabacion(
	      	UtilidadFecha.conversionFormatoFechaAAp(encabezadoPedido.get("fecha_grabacion[0]")+"")+" - "+
			UtilidadFecha.convertirHoraACincoCaracteres(encabezadoPedido.get("hora_grabacion[0]")+""));
	      form.setFechaPedidoModificacion(UtilidadFecha.conversionFormatoFechaAAp(encabezadoPedido.get("fecha_solicitud[0]")+""));
	      form.setHoraPedidoModificacion(UtilidadFecha.convertirHoraACincoCaracteres(encabezadoPedido.get("hora_solicitud[0]")+""));
	      form.setCheckPrioridadPedido(UtilidadTexto.getBoolean(encabezadoPedido.get("es_urgente[0]")+"")==true?"on":"off");
	      form.setObservacionesGenerales(encabezadoPedido.get("observaciones_generales[0]")+"");
	      
	      //se va registrando el LOG MODIFICACION
	      logInfo =      	
			"\n ==============INFORMACION ORIGINAL=================" +
			"\n ---------------Pedido-----------------" +
			"\n*  Código  [" +form.getCodigoPedido()  +"] "+
			"\n*  Estado [ solicitado ] " +
			"\n*  Centro de Costo que Solicita  ["+form.getCentroCosto()  +"] " +
			"\n*  Farmacia  ["+form.getFarmacia()  +"] " +
			"\n*  Es Urgente  ["+( form.getCheckPrioridadPedido().equals("on")?"Si":"No" )  +"] " +
			"\n*  Observaciones Generales ["+form.getObservacionesGenerales()  +"] ";
	      //***********************************************************************************
	      
	      //se registran los datos modificables del encabezado
	      form.setPedidosAntiguosMap("urgente",form.getCheckPrioridadPedido());
	      form.setPedidosAntiguosMap("observaciones",form.getObservacionesGenerales());
	      
	      //SE CONSULTA DETALLE DEL PEDIDO*******************************************************
	      
	      form.setPedidosMap(busquedaPedidos(con," codigoPedido = '" + form.getCodigoPedido()+"'","articulo","","view_detalle_pedidos"));
	      
	      //se toma el tamaño del detalle
	      form.setNumeroIngresos(Integer.parseInt(form.getPedidosMap("numRegistros")+""));
	      form.setPedidosAntiguosMap("numRegistros",form.getNumeroIngresos()+"");
	      
	      logInfo += "\n ------------Detalle Pedido--------------" ;
	      //preparación del resto de campos
	      for(int i=0;i<form.getNumeroIngresos();i++)
	      {
	      	//se listan los articulos insertados
	    	  
	      	codigosInsertados += form.getPedidosMap("codigoArticulo_"+i) +",";
	      	
	      	form.setPedidosMap("articulo_"+i,form.getPedidosMap("codigoArticulo_"+i)+"-"+form.getPedidosMap("descripcionArticulo_"+i));
	      	
	      	//se edita el LOG
	      	logInfo +=
					"\n*   Artículo ["+ form.getPedidosMap("articulo_"+i) +"] " +
					"\n*   Unidad de Medida ["+form.getPedidosMap("unidadMedidaArticulo_"+i)  +"] " +
					"\n*   Cantidad Pedida ["+form.getPedidosMap("cantidadDespacho_"+i)  +"] " +
					"\n*   Existencias ["+form.getPedidosMap("existenciaXAlmacen_"+i)  +"] \n";
	      	
	      	//se añade una accion vacía
	      	form.setPedidosMap("accion_"+i,"modify");
	      	form.setPedidosMap("existe_"+i,"true");
	      	
	      	//se registran los datos modificables del encabezado
	      	form.setPedidosAntiguosMap("codigoArticulo_"+i,form.getPedidosMap("codigoArticulo_"+i));
	      	form.setPedidosAntiguosMap("cantidadDespacho_"+i,form.getPedidosMap("cantidadDespacho_"+i));
	      	
	      }
	      
	      //se asigna a la forma el listado de los articulos existentes
	      form.setCodigosArticulosInsertados(codigosInsertados);
	      //*************************************************************************************
	      logInfo += "\n";
	      form.setLogInfo(logInfo);
	      
	    closeDBConnection(con);
		return mapping.findForward("modificarPedido");

	}


	/**
	 * Método implementado para postular la búsqueda inicial
	 * de los movimientos de pedidos
	 * @param con
	 * @param form
	 * @param mapping
	 * @param usuario 
	 * @param request
	 * @return
	 */
	private ActionForward accionComenzarBusqueda(Connection con, MovimientoPedidosForm form, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		
		String funcionalidad = request.getServletPath();
		if(funcionalidad.equals("/movimientoPedidos/movimientoPedidos.do"))
		{
			form.reset();
			
			//Se verifica si hay permisos para la devolución de pedidos
			PedidosInsumos mundo = new PedidosInsumos();
			String[] arrayString = mundo.puedoMostrarFuncionalidad(con, 170, usuario.getLoginUsuario(), true );
			form.setDevolucion(arrayString);
			
			form.setCentroAtencion(usuario.getCodigoCentroAtencion()+ConstantesBD.separadorSplit+usuario.getCentroAtencion());
		}
		else if(funcionalidad.equals("/modificarPedidos/modificarPedidos.do"))
		{
			form.setEstado("comenzar_busqueda");
			try 
			{
				form.setCentroCosto(UtilidadValidacion.getNombreCentroCosto(con,form.getSearchCodigoCentroCosto()));
				form.setFarmacia(UtilidadValidacion.getNombreCentroCosto(con,form.getSearchCodigoFarmacia()));
			} 
			catch (SQLException e) 
			{
				logger.warn("Error consultando la descripcion del centro costo en accionComenzarBusqueda: "+e);
			}
		}
		
		
		closeDBConnection(con);
		return mapping.findForward("paginaPrincipal");
	}


	/**
	 * Método implementado para postular la pantalla inicial de la funcionalidad
	 * Modificar/Anular Pedidos de Insumos
	 * @param con
	 * @param form
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private ActionForward accionEmpezar(Connection con, MovimientoPedidosForm form, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		
    	//se limpia formulario
    	form.reset();
    	form.setInstitucion(usuario.getCodigoInstitucion());
    	form.setCentroAtencion(usuario.getCodigoCentroAtencion()+"");
    	form.setSearchCodigoCentroCosto(usuario.getCodigoCentroCosto());
    	form.setTipoTransaccionPedido(ValoresPorDefecto.getCodigoTransaccionPedidos(usuario.getCodigoInstitucionInt(), true));
    	form.setUsuarioAlmacen(usuario.getLoginUsuario());
    	
    	//Se verifica que el tipo de transaccion pedido se encuentre
    	//definida en parametros generales
    	if(!form.getTipoTransaccionPedido().equals(""))
    	{
	    	//****SE VERIFICA SI EL USUARIO TIENE ALMACENES*****************
    		HashMap almacenes = UtilidadInventarios.listadoAlmacenesUsuarios(
    			usuario.getCodigoInstitucionInt(),
				usuario.getLoginUsuario(),ConstantesBD.codigoNuncaValido);
	        
    		//se toma número de almacenes
    		form.setNumAlmacenes(Integer.parseInt(almacenes.get("numRegistros")+""));
	       
    		//se verifica si el usuario tiene almacenes
    		if(form.getNumAlmacenes()<=0)
    		{
				ActionErrors errores = new ActionErrors(); 
                errores.add("sin almacen válido", new ActionMessage("error.inventarios.UsuarioNoDefinidoEnAlmacen"));
                saveErrors(request, errores);
                this.closeDBConnection(con);
        		return mapping.findForward("paginaErroresActionErrors");
    		}
    		else if(form.getNumAlmacenes()==1)
    		{
    			form.setSearchCodigoFarmacia(Integer.parseInt(almacenes.get("codigo_0")+""));
    			return accionComenzarBusqueda(con,form,mapping,usuario,request);
    		}
    	}
    	else
    	{
    		ActionErrors errores = new ActionErrors(); 
            errores.add("sin definir tipo transaccion", new ActionMessage("error.inventarios.sinDefinirTipoTransaccion","DE PEDIDOS"));
            saveErrors(request, errores);
            this.closeDBConnection(con);
    		return mapping.findForward("paginaErroresActionErrors");
    	}
		
		this.closeDBConnection(con);
		return mapping.findForward("seleccionAlmacen");
	}


	/**
	 * 	Método implementado para concatenar una condición en la claúsula WHERE
	 *  en el proceso de edición de una consulta
	 * @param whereString
	 * @param condition
	 * @return
	 */
	private String insertIntoWhereClause(String whereString, String condition)
	{
		if(whereString.equals(""))
			whereString = condition;
		else
			whereString  += " AND " + condition;
		
		return whereString;
	}
	
	/**
	 * Método que realiza una busqueda de pedidos
	 * @param con
	 * @param where
	 * @param orderBy
	 * @param orderDirection
	 * @param tabla
	 * @return
	 */
  @SuppressWarnings("unchecked")
private HashMap busquedaPedidos(Connection con, String where, String orderBy, String orderDirection, String tabla)
  {
	  HashMap pedidosHM = new HashMap();
	  PedidosInsumos mundo = new PedidosInsumos();
	  String[] columns;
	  boolean estilo = false;
	  
	  if(tabla.equals("view_detalle_pedidos"))
	  {
	  	estilo = true;
	  	String [] columnasConsulta = {
		  		 "codigoArticulo",
				 "articulo",
				 "getdescarticulosincodigo(codigoArticulo) as descripcionArticulo",
				 "unidadMedidaArticulo",
				 "cantidadDespacho",
				 "existenciaXAlmacen"
		  };
	  	 String[] columnsMapa = {
			  		 "codigoArticulo",
					 "articulo",
					 "descripcionArticulo",
					 "unidadMedidaArticulo",
					 "cantidadDespacho",
					 "existenciaXAlmacen"
			  };
	  	columns=columnsMapa;
	  	pedidosHM = util.UtilidadBD.resultSet2HashMap(columns, mundo.buscar(con, columnasConsulta, tabla, where, orderBy, orderDirection ), false, estilo).getMapa();
	  }
	  //para la vista despacho_pedido_vw
	  else 
	  {
		  	String [] columnasConsulta = {
		  		 "cod_pedido",
		  		 "es_urgente",
				 "cod_estado",
				 "nom_estado",
				 "fecha_grabacion",
				 "hora_grabacion",
				 "fecha_solicitud",
				 "hora_solicitud",
				 "cod_centro_costo",
				 "nom_centro_costo",
				 "nom_centro_atencion",
				 "cod_farmacia",
				 "nom_farmacia",
				 "nom_centro_atencion_farmacia",
				 "fecha_despacho",
				 "hora_despacho",
				 "fecha_grabacion",
				 "hora_grabacion",
				 "usuario",
				 "usuario_despacho",
				 "observaciones_generales",
				 "es_qx",
				 "auto_por_subcontratacion"
		  };
		  columns = columnasConsulta;
		  pedidosHM = util.UtilidadBD.resultSet2HashMap(columns, mundo.buscar(con, columnasConsulta, tabla, where, orderBy, orderDirection ), false, estilo).getMapa();
  		}
	  return pedidosHM;
  }

  /**
   * Método que realiza la búsqueda de los artículos de un pedido
   * @param con
   * @param codPedido
   * @return
   */
  @SuppressWarnings("unchecked")
  private HashMap busquedaArticulos(Connection con, int codPedido)
  {
	  HashMap articulosHM = new HashMap();
	  PedidosInsumos mundo = new PedidosInsumos();
	  String[] columnsRS = {
				 "getdescarticulo(cod_articulo) as des_articulo," +
				 "lote",
				 "fecha_vencimiento",
				 "unidad_medida_articulo",
				 "cantidad_pedida",
				 "cantidad_despachada",
				 " a.costo_promedio*cantidad_despachada AS valor_total"
	  };
	  logger.info("\n\n aqui tenemos la cadena de columnsRS =>"+columnsRS);
	  
	  String[] columnsMapa = {
				 "des_articulo",
				 "lote",
				 "fecha_vencimiento",
				 "unidad_medida_articulo",
				 "cantidad_pedida",
				 "cantidad_despachada",
				 "valor_total"
	  };
	  logger.info("\n\n aqui tenemos la cadena de columnsMapa =>"+columnsMapa);
	  articulosHM = util.UtilidadBD.resultSet2HashMap(columnsMapa, mundo.buscar(con, columnsRS, "detalle_despacho_pedido_vw ddp inner join inventarios.articulo a on ddp.cod_articulo=a.codigo", ("cod_pedido = " +codPedido), "des_articulo", "" ), false, false).getMapa();
	  
	  logger.info("el resultado de la consulta es --> "+articulosHM);
	  return articulosHM;
  }

  
  
	
}
