package com.princetonsa.action.facturasVarias;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Vector;

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

import util.Listado;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.facturasVarias.AprobacionAnulacionAjustesFacturasVariasForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturasVarias.AprobacionAnulacionAjustesFacturasVarias;
import com.princetonsa.mundo.facturasVarias.GeneracionModificacionAjustesFacturasVarias;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

/**
 * @author Mauricio Jaramillo H.
 * Fecha: Agosto de 2008
 */

public class AprobacionAnulacionAjustesFacturasVariasAction extends Action
{

	/**
	 * Objeto para manejar los logs de esta clase
	 */
	Logger logger = Logger.getLogger(PagosFacturasVariasAction.class);
	
	//*************************INICIO INDICES COPIAS PARA HACER LA BUSQUEDA GENERICA DE GENERACION*************************
	//-----Se instancian los indices del mundo----------------------------------------------
	String [] indicesFacturasVarias = GeneracionModificacionAjustesFacturasVarias.indicesFacturasVarias;
	String [] indicesAjustesFacturasVarias = GeneracionModificacionAjustesFacturasVarias.indicesAjustesFacturasVarias;
	String [] indicesCriterios = GeneracionModificacionAjustesFacturasVarias.indicesCriteriosBusqueda;
	//--------------------------------------------------------------------------------------
	//*************************FIN INDICES COPIAS PARA HACER LA BUSQUEDA GENERICA DE GENERACION*************************
	
	/**
	 * Método execute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		
		Connection con = null;
		try{
		if (form instanceof AprobacionAnulacionAjustesFacturasVariasForm) 
		{
			
		    //Abrimos la conexion con la fuente de Datos 
			con = util.UtilidadBD.abrirConexion();
			if(con == null)
			{
				request.setAttribute("codigoDescripcionError", "errors.problemasBd");
				return mapping.findForward("paginaError");
			}
			AprobacionAnulacionAjustesFacturasVariasForm forma = (AprobacionAnulacionAjustesFacturasVariasForm) form;
			AprobacionAnulacionAjustesFacturasVarias mundo = new AprobacionAnulacionAjustesFacturasVarias();
			//Se instancia el mundo de Generación y Modificación de Ajustes
			GeneracionModificacionAjustesFacturasVarias mundoGeneracion = new GeneracionModificacionAjustesFacturasVarias();
			UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());
			String estado = forma.getEstado();
			forma.setMensaje(new ResultadoBoolean(false));
			ActionErrors errores = new ActionErrors();
			logger.warn("[AprobacionAnulacionAjustesFacturasVarias]--->Estado: "+estado);
			
			if(estado == null)
			{
				logger.warn("Estado no valido dentro del flujo estado is NULL");
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}
			else if(estado.equals("empezar"))
			{
				return accionEmpezar(con, forma, mundo, usuario, mapping, request);
			}
			else if(estado.equals("cargarAjusteFactura"))
			{
				return accionCargarAjusteFactura(con, forma, mundo, usuario, mapping, request);
			}
			else if(estado.equals("guardar"))
			{
				return accionGuardar(con, forma, mundo, usuario, mapping);
			}
			else if(estado.equals("imprimir"))
			{
				return accionImprimir(con, forma, mundo, usuario, mapping, request);
			}
			else if(estado.equals("ordenar"))
			{
				return ordenarListado(con, forma, mapping);
			}
			//Estado incluido para hacer el llamado a la búsqueda avanzada
			else if(estado.equals("busquedaAvanzada"))
			{
				forma.reset();
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("busqueda");
			}
			//Estado incluido para realizar el ordenamiento del mapa presentado en la búsqueda avanzada
			else if(estado.equals("ordenarBusquedaAvanzada"))
			{
				forma.setListado(this.accionOrdenarMapa(forma.getListado(), forma));
				UtilidadBD.closeConnection(con);
				return mapping.findForward("busqueda");
			}
			//Estado includio para realizar la búsqueda avanzada 
			else if (estado.equals("buscar"))
			{ 
				forma.resetBusqueda();
				errores = validarCriterios(con, forma);
				if(!errores.isEmpty())
				{
					saveErrors(request, errores);
					forma.setEstado("busquedaAvanzada");
					return mapping.findForward("busqueda");
				}	
				forma.getFiltrosBusqueda().put("codigoDeudor", forma.getCodigoDeudor());
				forma.getFiltrosBusqueda().put("descripDeudor", forma.getDescripDeudor());
				forma.getFiltrosBusqueda().put("tipoDeudor", forma.getTipoDeudor());
				
				forma.setListado(mundoGeneracion.consultarAjustesFacturasVarias(con, forma.getFiltrosBusqueda(), usuario, false));
				return mapping.findForward("busqueda");
			}
			//Estado incluido para cargar el ajuste seleccionado en la búsqueda avanzada
			else if(estado.equals("cargarAjuste"))
			{
				return accionCargarAjuste(con, forma, mundo, usuario, mapping);
			}
			//ESTADO UTILIZADO PARA EL PAGER
			else if (estado.equals("redireccion")) 
			{			    
			    UtilidadBD.cerrarConexion(con);
			    forma.getLinkSiguiente();
			    response.sendRedirect(forma.getLinkSiguiente());
			    return null;
			}
			else
			{
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}
		}
		else
		{
			logger.error("El form no es compatible con el form de AprobacionAnulacionAjustesFacturasVariasForm");
			request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
			return mapping.findForward("paginaError");
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
	 * Método incluido para cargar el ajuste pendiente
	 * seleccionado desde la vista de aprobación/anulación
	 * facturas varias
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario 
	 * @param mapping
	 * @return
	 */
	private ActionForward accionCargarAjuste(Connection con, AprobacionAnulacionAjustesFacturasVariasForm forma, AprobacionAnulacionAjustesFacturasVarias mundo, UsuarioBasico usuario, ActionMapping mapping)
	{
		logger.info("====>Index Seleccionado: "+forma.getIndex());
		logger.info("amapa de listado ajustes: "+forma.getListado());
		llenarAjustesSeleccionado02(forma);
		//Indicamos que no se cargo el ajuste desde el listado principal
		forma.setCargaListadoPrincipal(false);
		//Cargamos nuevamente el listado principal de la funcionalidad con todos los ajustes pendientes 
		forma.setListadoAjustesFacturasVarias(mundo.consultarAjustesFacturasVarias(con, usuario.getCodigoInstitucionInt()));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	//***********************INICIO MÉTODO INCLUIDO PARA ORDENAR EL MAPA DE LA BÚSQUEDA AVANZADA******************************
	/**
	 *  Ordena Un Mapa HashMap a partir del patron de ordenamiento
	 *  @param HashMap mapaOrdenar
	 *  @param String patronOrdenar
	 *  @param String ultimoPatron
	 *  @return Mapa Ordenado
	 **/
	public HashMap accionOrdenarMapa(HashMap mapaOrdenar, AprobacionAnulacionAjustesFacturasVariasForm forma)
	{			
		int numReg = Integer.parseInt(mapaOrdenar.get("numRegistros")+"");		
		mapaOrdenar = (Listado.ordenarMapa(indicesAjustesFacturasVarias,forma.getPatronOrdenar(),forma.getUltimoPatron(),mapaOrdenar,numReg));		
		forma.setUltimoPatron(forma.getPatronOrdenar());
		mapaOrdenar.put("numRegistros",numReg+"");
		return mapaOrdenar;
	}
	//***********************FIN MÉTODO INCLUIDO PARA ORDENAR EL MAPA DE LA BÚSQUEDA AVANZADA**********************************
	
	/**
	 * Método que permite hacer el ordenamiento por 
	 * cualquier columna en el listado principal visualizado
	 * al principio de la funcionalidad
	 * @param con
	 * @param forma
	 * @param mapping
	 * @return
	 */
	private ActionForward ordenarListado(Connection con, AprobacionAnulacionAjustesFacturasVariasForm forma, ActionMapping mapping)
	{
		String[] indices={
		        		"codigoajuste_",
		        		"numajuste_",
						"fechaajuste_",
		        		"codconceptoajuste_",
						"conceptoajuste_",
		        		"valorajuste_",
		        		"codigofactura_",
		        		"numfactura_",
		        		"nombredeudor_",
		        		"identificaciondeudor_",
		        		"estado_",
		        		""
					};
		int numReg = Integer.parseInt(forma.getListadoAjustesFacturasVarias("numRegistros")+"");
		forma.setListadoAjustesFacturasVarias(Listado.ordenarMapa(indices, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getListadoAjustesFacturasVarias(), numReg));
		forma.setListadoAjustesFacturasVarias("numRegistros", numReg);
		forma.setUltimoPatron(forma.getPatronOrdenar());
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}


	//******************* INICIO MÉTODO INCLUIDO PARA LA BUSQUEDA DE AJUSTES FACTURAS VARIAS *******************************
	public ActionErrors validarCriterios (Connection connection, AprobacionAnulacionAjustesFacturasVariasForm forma)
	{
		ActionErrors errores = new ActionErrors();
		logger.info("\n entre a validarCriterios "+forma.getFiltrosBusqueda(indicesCriterios[5])+"--"+forma.getFiltrosBusqueda(indicesCriterios[6])+"");
		
		//se evalua como requerido el deudor
		if (!(forma.getFiltrosBusqueda(indicesCriterios[5])+"").equals("") && !(forma.getFiltrosBusqueda(indicesCriterios[5])+"").equals("null"))
			if ((forma.getFiltrosBusqueda(indicesCriterios[6])+"").equals("") || (forma.getFiltrosBusqueda(indicesCriterios[6])+"").equals("null"))
				errores.add("descripcion",new ActionMessage("errors.required","El Deudor"));
				
		//se valida que la fecha inicial no venga vacia
		if (!(forma.getFiltrosBusqueda(indicesCriterios[2])+"").equals("") && !(forma.getFiltrosBusqueda(indicesCriterios[2])+"").equals("null"))
		{
			//se valida que la fecha final si este
			if ((forma.getFiltrosBusqueda(indicesCriterios[3])+"").equals("") || (forma.getFiltrosBusqueda(indicesCriterios[3])+"").equals("null"))
				errores.add("descripcion",new ActionMessage("errors.required","La Fecha Final"));
		
			//se valida que la fecha incial sea menor o igual a la actual
			if (!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getFiltrosBusqueda(indicesCriterios[2])+"",UtilidadFecha.getFechaActual()))
				errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", forma.getFiltrosBusqueda(indicesCriterios[2])+"", "Actual "+UtilidadFecha.getFechaActual()));
		}
			//se valida que la fecha final no venga vacia
			if (!(forma.getFiltrosBusqueda(indicesCriterios[3])+"").equals("") && !(forma.getFiltrosBusqueda(indicesCriterios[3])+"").equals("null"))
			{	
				//se valida que la fecha inicial si este
				if ((forma.getFiltrosBusqueda(indicesCriterios[2])+"").equals("") || (forma.getFiltrosBusqueda(indicesCriterios[2])+"").equals("null"))
					errores.add("descripcion",new ActionMessage("errors.required","La Fecha Inicial"));
				//se valida que la fecha final sea mayor o igual  a la fecha inicial
				if (!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getFiltrosBusqueda(indicesCriterios[2])+"",forma.getFiltrosBusqueda(indicesCriterios[3])+""))
					errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", forma.getFiltrosBusqueda(indicesCriterios[3])+"", "Inicial "+forma.getFiltrosBusqueda(indicesCriterios[2])+""));
				else//se valida que la fecha final sea menor o igual a la fecha del sistema
					if (!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getFiltrosBusqueda(indicesCriterios[3])+"",UtilidadFecha.getFechaActual()))
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", forma.getFiltrosBusqueda(indicesCriterios[2])+"", "Actual "+UtilidadFecha.getFechaActual()));
				
			}
			
			//si la cantidad de dias entre fechas supera 180.
			if (!(forma.getFiltrosBusqueda(indicesCriterios[2])+"").equals("") && !(forma.getFiltrosBusqueda(indicesCriterios[2])+"").equals("null") && 
				!(forma.getFiltrosBusqueda(indicesCriterios[3])+"").equals("") && !(forma.getFiltrosBusqueda(indicesCriterios[3])+"").equals("null"))	
				if(UtilidadFecha.numeroMesesEntreFechasExacta(forma.getFiltrosBusqueda(indicesCriterios[2])+"", forma.getFiltrosBusqueda(indicesCriterios[3])+"")>6)
					errores.add("", new ActionMessage("errors.debeSerNumeroMenorIgual","El Rango de Días entre Fechas", "180 días"));
			if(
					UtilidadTexto.isEmpty(forma.getFiltrosBusqueda(indicesCriterios[0])+"")&&
					UtilidadTexto.isEmpty(forma.getFiltrosBusqueda(indicesCriterios[1])+"")&&
					UtilidadTexto.isEmpty(forma.getFiltrosBusqueda(indicesCriterios[2])+"")&&
					UtilidadTexto.isEmpty(forma.getFiltrosBusqueda(indicesCriterios[3])+"")&&
					UtilidadTexto.isEmpty(forma.getCodigoDeudor())&&
					UtilidadTexto.isEmpty(forma.getDescripDeudor())&&
					UtilidadTexto.isEmpty(forma.getTipoDeudor())
				)
			{
				errores.add("", new ActionMessage("errors.minimoCampos","1 campo", "la consulta"));
				
			}
			
		return errores;
	}
	//******************* FIN MÉTODO INCLUIDO PARA LA BUSQUEDA DE AJUSTES FACTURAS VARIAS *******************************

	/**
	 * Método que permite exportar a PDF mediante el BIRT
	 * el reporte de la Aprobación/Anulación Ajustes de
	 * Facturas Varias
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionImprimir(Connection con, AprobacionAnulacionAjustesFacturasVariasForm forma, AprobacionAnulacionAjustesFacturasVarias mundo, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request)
	{
		//se hace una llamada al recolector de basura
		System.gc();
		
		DesignEngineApi comp;
		String codigoAImprimir = "";
		String reporte="",DataSet="";
		DataSet="AjustesFacturasVarias";
		reporte="ConsultaImpresionAjustesFacturasVarias.rptdesign";
		
		//LLamamos al reporte
		comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"facturasVarias/",reporte);
        //Imprimimos el encabezado del reporte
		armarEncabezado(comp, con, usuario, forma, request);
		
		comp.obtenerComponentesDataSet(DataSet);
		//Modificamos el DataSet con las validaciones comunes para todos
		//********************************************************************
		 HashMap criterios = new HashMap ();
		 criterios.put("usuario", usuario.getCodigoInstitucion());
		 criterios.put("codigoAjusteFactura", forma.getListadoAjustesFacturasVarias("codigoajuste_"+forma.getPosicion()));
		//********************************************************************
        String newQuery = comp.obtenerQueryDataSet().replace("WHERE", mundo.obtenerWhere(criterios));
        logger.info("=====>Consulta en el BIRT Detallado por Tipo Transaccion: "+newQuery);
        //Se modifica el query
        comp.modificarQueryDataSet(newQuery);
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        comp.updateJDBCParameters(newPathReport);
        if(!newPathReport.equals(""))
        {
        	request.setAttribute("isOpenReport", "true");
        	request.setAttribute("newPathReport", newPathReport);
        }
		
    	UtilidadBD.closeConnection(con);
        return mapping.findForward("resumen");
	}

	/**
	 * Método que arma el encabezado del reporte
	 * de ajustes de facturas varias
	 * @param comp
	 * @param connection
	 * @param usuario
	 * @param forma
	 * @param request
	 */
	private static void armarEncabezado(DesignEngineApi comp, Connection connection, UsuarioBasico usuario, AprobacionAnulacionAjustesFacturasVariasForm forma, HttpServletRequest request)
	{
		//Insertamos la información de la Institución
		InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		comp.insertImageHeaderOfMasterPage1(0, 0, institucion.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        Vector v = new Vector();
        v.add(institucion.getRazonSocial());
        v.add(Utilidades.getDescripcionTipoIdentificacion(connection, institucion.getTipoIdentificacion())+"  -  "+institucion.getNit());
        v.add(institucion.getDireccion());
        v.add("Tels. "+institucion.getTelefono());
        comp.insertLabelInGridOfMasterPage(0, 1, v);
        
        //Insertamos el nombre de la funcionalidad en el reporte 
        comp.insertLabelInGridPpalOfHeader(1,1, "AJUSTES FACTURAS VARIAS");
              
        //Información de Pie de Pagina
        comp.insertLabelInGridPpalOfFooter(0, 0, "Usuario: "+usuario.getLoginUsuario());
    }
	
	
	/**
	 * Método usado para llenar los datos del ajuste seleccionado
	 * @param forma
	 */
	private void llenarAjustesSeleccionado01(AprobacionAnulacionAjustesFacturasVariasForm forma)
	{
		
		forma.setAjustesSeleccionado("codigoajuste", forma.getListadoAjustesFacturasVarias("codigoajuste_"+forma.getPosicion()));
		forma.setAjustesSeleccionado("numajuste", forma.getListadoAjustesFacturasVarias("numajuste_"+forma.getPosicion()));
		forma.setAjustesSeleccionado("deudor", forma.getListadoAjustesFacturasVarias("nombredeudor_"+forma.getPosicion())+" - "+forma.getListadoAjustesFacturasVarias("identificaciondeudor_"+forma.getPosicion()));
		forma.setAjustesSeleccionado("tipoajuste", forma.getListadoAjustesFacturasVarias("tipoajuste_"+forma.getPosicion()));
		forma.setAjustesSeleccionado("valorajuste", forma.getListadoAjustesFacturasVarias("valorajuste_"+forma.getPosicion()));
		forma.setAjustesSeleccionado("codigofactura", forma.getListadoAjustesFacturasVarias("codigofactura_"+forma.getPosicion()));
		forma.setAjustesSeleccionado("numfactura", forma.getListadoAjustesFacturasVarias("numfactura"+forma.getPosicion()));
	}
	
	/**
	 * Método usado para llenar los datos del ajuste seleccionado
	 * @param forma
	 */
	private void llenarAjustesSeleccionado02(AprobacionAnulacionAjustesFacturasVariasForm forma)
	{
		
		forma.setAjustesSeleccionado("codigoajuste", forma.getListado(forma.getIndicesAjustes()[0]+forma.getIndex()));
		forma.setAjustesSeleccionado("numajuste", forma.getListado(forma.getIndicesAjustes()[1]+forma.getIndex()));
		forma.setAjustesSeleccionado("deudor", forma.getListado(forma.getIndicesAjustes()[12]+forma.getIndex()));
		forma.setAjustesSeleccionado("tipoajuste", forma.getListado(forma.getIndicesAjustes()[2]+forma.getIndex()));
		forma.setAjustesSeleccionado("valorajuste", forma.getListado(forma.getIndicesAjustes()[6]+forma.getIndex()));
		forma.setAjustesSeleccionado("codigofactura", forma.getListado(forma.getIndicesAjustes()[4]+forma.getIndex()));
		forma.setAjustesSeleccionado("numfactura", forma.getListado(forma.getIndicesAjustes()[14]+forma.getIndex()));
	}
	
	/**
	 * Método que guarda la información de la aprobación/anulación
	 * del ajuste de facturas varias 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private ActionForward accionGuardar(Connection con, AprobacionAnulacionAjustesFacturasVariasForm forma, AprobacionAnulacionAjustesFacturasVarias mundo, UsuarioBasico usuario, ActionMapping mapping)
	{
		boolean transaccion = false; 
		HashMap criterios = new HashMap();
		criterios.put("codigoajuste", forma.getAjustesSeleccionado("codigoajuste"));
		criterios.put("tipoajuste", forma.getAjustesSeleccionado("tipoajuste"));
		criterios.put("valorajuste", forma.getAjustesSeleccionado("valorajuste"));
		criterios.put("codigofactura", forma.getAjustesSeleccionado("codigofactura"));
		criterios.put("estado", forma.getEstadoAprobAnul());
		criterios.put("usuarioaprobanul", usuario.getLoginUsuario());
		criterios.put("motivoaprobanul", forma.getMotivoAprobAnul());
		criterios.put("fechaaprobanul", UtilidadFecha.conversionFormatoFechaABD(forma.getFechaAprobAnul()));
		criterios.put("fecha", UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con)));
		criterios.put("horaaprobanul", UtilidadFecha.getHoraActual());
		criterios.put("factura", forma.getAjustesSeleccionado("numfactura"));
		UtilidadBD.iniciarTransaccion(con);
		logger.info("MAPA DE LOS AJUSTES: "+criterios);
		transaccion = mundo.actualizarAprobacionAnulacionAjusteFacturasVarias(con, criterios);
		logger.info("===>Transaccion Actualizar Aprobación/Anulación: "+transaccion);
		if(transaccion)
		{
			forma.setAjustesSeleccionado("nombreEstadoAproAnul", ValoresPorDefecto.getIntegridadDominio(forma.getEstadoAprobAnul()));
			//Insertar el log Tipo Base de Datos
			transaccion = mundo.generarLogAprobacionAnulacion(con, criterios);
			logger.info("===>Transaccion Insertar Log Aprobación/Anulación: "+transaccion);
			if(transaccion)
			{
				UtilidadBD.finalizarTransaccion(con);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("resumen");
			}
			else
			{
				forma.setMensaje(new ResultadoBoolean(true,"SE PRESENTO INCONVENIENTES EN LA APROBACIÓN/ANULACIÓN DEL AJUSTE "+forma.getListadoAjustesFacturasVarias("numajuste_"+forma.getPosicion())));
				UtilidadBD.abortarTransaccion(con);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("principal");
			}
		}
		else
		{
			forma.setMensaje(new ResultadoBoolean(true,"SE PRESENTO INCONVENIENTES EN LA APROBACIÓN/ANULACIÓN DEL AJUSTE "+forma.getListadoAjustesFacturasVarias("numajuste_"+forma.getPosicion())));
			UtilidadBD.abortarTransaccion(con);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("principal");
		}
	}

	/**
	 * Método que trae los datos del ajuste de pagos de
	 * facturas varias para ser aprobado o anulado
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionCargarAjusteFactura(Connection con, AprobacionAnulacionAjustesFacturasVariasForm forma, AprobacionAnulacionAjustesFacturasVarias mundo, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request)
	{
		forma.resetAprobacionAnulacion();
		logger.info("====>Posicion Seleccionada: "+forma.getPosicion());
		//Indicamos que se cargo el ajuste desde el listado principal
		forma.setCargaListadoPrincipal(true);
		llenarAjustesSeleccionado01(forma);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	/**
	 * Método que inicializa la funcionalidad de
	 * Aprobación/Anulación Ajustes Facturas Varias
	 * cargando los valores iniciales
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario 
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, AprobacionAnulacionAjustesFacturasVariasForm forma, AprobacionAnulacionAjustesFacturasVarias mundo, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request)
	{
		forma.reset();
		//Cargamos los ajustes pendientes de aprobar o anular al mapa para ser visualizados en la vista
		forma.setListadoAjustesFacturasVarias(mundo.consultarAjustesFacturasVarias(con, usuario.getCodigoInstitucionInt()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
}