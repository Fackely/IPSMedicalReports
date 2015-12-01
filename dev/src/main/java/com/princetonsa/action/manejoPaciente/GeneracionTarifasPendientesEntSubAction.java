package com.princetonsa.action.manejoPaciente;
import java.sql.Connection;

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
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.actionform.manejoPaciente.GeneracionTarifasPendientesEntSubForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.cargos.CargosEntidadesSubcontratadasDao;
import com.princetonsa.dto.cargos.DTOCalculoTarifaServicioArticulo;
import com.princetonsa.dto.facturacion.DtoContratoEntidadSub;
import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.CargosEntidadesSubcontratadas;
import com.princetonsa.mundo.cargos.EsquemaTarifario;
import com.princetonsa.mundo.facturacion.IngresarModificarContratosEntidadesSubcontratadas;
import com.princetonsa.mundo.manejoPaciente.GeneracionTarifasPendientesEntSub;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * Clase para el manejo de la parametrizacion 
 * de generacion de tarifas pendientes de entidades subcontratadas
 * @author jfhernandez@princetonsa.com
 */
public class GeneracionTarifasPendientesEntSubAction extends Action 
{
	/**
	 * logger 
	 * */
	Logger logger = Logger.getLogger(GeneracionTarifasPendientesEntSubAction.class);
	
	/**
	 * Metodo excute del Action
	 */
    public ActionForward execute(   ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
    {
    	Connection con = null;
    	try{
    		if(response==null);
    		if(form instanceof GeneracionTarifasPendientesEntSubForm)
    		{

    			con = UtilidadBD.abrirConexion();

    			if(con == null)
    			{	
    				request.setAttribute("CodigoDescripcionError","errors.problemasBd");
    				return mapping.findForward("paginaError");
    			}

    			GeneracionTarifasPendientesEntSubForm forma = (GeneracionTarifasPendientesEntSubForm)form;
    			String estado = forma.getEstado();

    			@SuppressWarnings("unused")
    			ActionErrors errores = new ActionErrors();

    			logger.info("\n\n\n ESTADO ---> "+estado+"\n\n");

    			UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

    			if(estado == null)
    			{
    				forma.reset();
    				logger.warn("Estado No Valido Dentro Del Flujo de la Parametrización de generación de tarifas pendientes ent sub (null)");
    				request.setAttribute("CodigoDescripcionError","Errors.estadoInvalido");
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaError");
    			}	

    			else if(estado.equals("empezar"))
    			{
    				return accionEmpezar(con, forma, usuario, request, mapping);
    			}

    			else if (estado.equals("buscar"))
    			{
    				if (forma.getFiltrosBusqueda("tipotarifa").toString().equals(ConstantesIntegridadDominio.acronimoServicio))
    					return accionBuscarAutorizaciones(con, forma, usuario, request, mapping);

    				else if(forma.getFiltrosBusqueda("tipotarifa").toString().equals(ConstantesIntegridadDominio.acronimoArticulo))
    					return accionBuscarArticulos(con, forma, usuario, request, mapping);
    			}
    			else if(estado.equals("generarTarifa"))
    			{
    				return accionGenerarTarifa(con, forma, usuario, request, mapping, errores); 
    			}

    			else if(estado.equals("generarTarifaServicio"))
    			{
    				return accionGenerarTarifaServicio(con, forma, usuario, request, mapping);
    			}

    			else if(estado.equals("generarTarifaServicioUnitario"))
    			{
    				return accionGenerarTarifaServicioUnitario(con, forma, usuario, request, mapping);
    			}

    			else if(estado.equals("generarTarifaArticulos"))
    			{
    				return accionGenerarTarifaArticulos(con, forma, usuario, request, mapping);
    			}

    			else if(estado.equals("generarTarifaUnitariaPedido"))
    			{
    				return accionGenerarTarifaUnitariaPedido(con, forma, usuario, request, mapping);
    			}

    			else if(estado.equals("generarTarifaPedido"))
    			{
    				return acciongenerarTarifaPedido(con, forma, usuario, request, mapping);
    			}

    			else if(estado.equals("generarTarifaUnitariaSolicitud"))
    			{
    				return accionGenerarTarifaUnitariaSolicitud(con, forma, usuario, request, mapping);
    			}

    			else if(estado.equals("generarTarifaSolicitud"))
    			{
    				return accionGenerarTarifaSolicitud(con, forma, usuario, request, mapping);
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
     * accionEmpezar
     * @param con
     * @param forma
     * @param usuario
     * @param request
     * @param mapping
     * @return
     */
    private ActionForward accionEmpezar(Connection con, GeneracionTarifasPendientesEntSubForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) {
		forma.reset();
		forma.setEntidades(IngresarModificarContratosEntidadesSubcontratadas.obtenerEntidades(con));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
    
    
    /**
     * accionBuscarAutorizaciones
     * @param con
     * @param forma
     * @param usuario
     * @param request
     * @param mapping
     * @return
     */
    private ActionForward accionBuscarAutorizaciones(Connection con, GeneracionTarifasPendientesEntSubForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) {
		forma.setResultadosBusqueda(GeneracionTarifasPendientesEntSub.buscarAutorizaciones(con, forma.getFiltrosBusqueda()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("busqueda");
	}
    
    
    
    /**
     * accionGenerarTarifa
     * @param con
     * @param forma
     * @param usuario
     * @param request
     * @param mapping
     * @return
     */
    private ActionForward accionGenerarTarifa(Connection con, GeneracionTarifasPendientesEntSubForm forma, 
    				UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping, ActionErrors errores) throws IPSException 
    {
    	forma.resetMensajes();
    	boolean selecciono=false;
    	if (forma.getFiltrosBusqueda("tipotarifa").equals(ConstantesIntegridadDominio.acronimoServicio))
	    {
	    	for (int i=0;i<Utilidades.convertirAEntero(forma.getResultadosBusqueda("numRegistros").toString());i++)
	    	{
	    		if (forma.getResultadosBusqueda("autorizacionActivo_"+i).toString().equals("true"))
	    		{
	    			selecciono=true;
	    			break;
	    		}
	    	}
	    	if(!selecciono){
        		errores.add("no selecciono autorización", new ActionMessage("errors.autorizaciones.requerido"));
        		saveErrors(request, errores);
        		forma.setMostrarMensaje(true);
        		return mapping.findForward("busqueda");
        	}
	    }
    	
    	//Se genera un mapa con los servicios antes de generar las tarifas
    	forma.setResultadosBusquedaAux(forma.getResultadosBusqueda());
    	
    	
	    if (forma.getFiltrosBusqueda("tipotarifa").equals(ConstantesIntegridadDominio.acronimoServicio))
	    {
	    	for (int i=0;i<Utilidades.convertirAEntero(forma.getResultadosBusqueda("numRegistros").toString());i++)
	    	{
	    		int indice = i;
	    		
    			if (forma.getResultadosBusqueda("autorizacionActivo_"+i).toString().equals("true"))
	    		{
    	    		
    	    		boolean esServicio = true;	
	    			DtoEntidadSubcontratada dtoEntidadSub = new DtoEntidadSubcontratada();
	    			//String retorno = "busquedaArticulosSol";
	    			CargosEntidadesSubcontratadasDao cargosDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosEntidadesSubcontratadasDao(); 
	    			String fechaAutorizacion = UtilidadFecha.getFechaActual();
	    			
	    			DtoContratoEntidadSub contratoEntidadSub = cargosDao.obtenerContratoVigenteEntidadSubcontratada(con,(forma.getResultadosBusqueda("entidad_"+indice).toString()),fechaAutorizacion);
	    			 
	    			if(contratoEntidadSub!=null)
	    			{
	    				dtoEntidadSub.setCodigoPk(Long.valueOf(forma.getResultadosBusqueda("entidad_"+indice).toString())); 
	    				
	    				dtoEntidadSub.setConsecutivo(String.valueOf(dtoEntidadSub.getCodigoPk()));
	    				
	    				forma.setIndice(indice);
	    				double tarifa = generarTarifaSegunTipoEntidad(contratoEntidadSub, con, esServicio, dtoEntidadSub, forma, usuario );  
	    				Log4JManager.info("La tarifa par el Indice: "+indice+" es:"+tarifa);
	    				
	    				//-------------------------------------------------
	    				// Evarual si deberia preguntar tambien si el cargo generado para este servicio en específico no tiene errores
	    			    if (tarifa >= 0)  
	    			    {
	    			    	forma.setResultadosBusquedaAux("tarifado_"+indice, ConstantesBD.acronimoSi);
	    				}
	    			    //-------------------------------------------------
	    			    
	    			} 				
	    		}
	    	}
	    }
	    
	    
	    //Se guardan los errores
	 	//forma.setMensajesAlertaPedidos(cargos.getErroresCargo());
	 	for (CargosEntidadesSubcontratadas cargos : forma.getListaCargos()) {
	 		if(!Utilidades.isEmpty(cargos.getErroresCargo())){
	 			forma.getMensajesAlerta().addAll(cargos.getErroresCargo());
	 		}
		}
	    
	    //Se determina si se debe mostrar el array para los mensajes
	    if (forma.getMensajesAlerta().size()!=0)
	    	forma.setMostrarMensaje(true);
	    
	    //Se cargan los servicios de nuevo para refrescar el mapa
	    forma.setResultadosBusqueda(GeneracionTarifasPendientesEntSub.buscarAutorizaciones(con, forma.getFiltrosBusqueda()));
	    
	    //Se mira cuales fueron los servicios a los cuales seles generó la tarifa
	    int numRegs=Utilidades.convertirAEntero(forma.getResultadosBusqueda("numRegistros")+"");
	    int numRegsAux=Utilidades.convertirAEntero(forma.getResultadosBusquedaAux("numRegistros")+"");
	    
	    //Se llena el mapa con nuevo registro para saber cuales registros se les generó tarifa
	    for (int i=0;i<numRegsAux;i++)
	    {
	    	for(int j=0;j<numRegs;j++)
	    	{
	    		if (forma.getResultadosBusquedaAux("consecutivoaut_"+i).toString().equals(forma.getResultadosBusqueda("consecutivoaut_"+j).toString()))
	    		{
	    			forma.setResultadosBusquedaAux("tarifado_"+i, ConstantesBD.acronimoNo);
	    			break;
	    		}
	    		else
	    			forma.setResultadosBusquedaAux("tarifado_"+i, ConstantesBD.acronimoSi);
	    	}
	    }
		
		//Si los registros antes y despues de genrar las tarifas son iguales (no se generaron tarifas) se vuelve al listado
		if (numRegs==numRegsAux)
		{
			UtilidadBD.closeConnection(con);
			return mapping.findForward("busqueda");
		}
		//Sino, se va al detalle de las tarifas generadas
		else
		{
			forma.setServiciosTarifados(GeneracionTarifasPendientesEntSub.obtenerServiciosTarifados(con,forma.getResultadosBusquedaAux()));
			UtilidadBD.closeConnection(con);
			return mapping.findForward("listadoServiciosTarifados");
		}
	}
    
    
    /**
     * accionGenerarTarifaServicio
     * @param con
     * @param forma
     * @param usuario
     * @param request
     * @param mapping
     * @return
     */
    private ActionForward accionGenerarTarifaServicio(Connection con, GeneracionTarifasPendientesEntSubForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) 
    {
    	logger.info("EL VALOR DEL INDICE----->"+forma.getIndice());
    	forma.setServiciosAutorizacion(GeneracionTarifasPendientesEntSub.buscarServiciosAutorizacion(con, forma.getResultadosBusqueda("consecutivoaut_"+forma.getIndice()).toString()));
    	forma.setErroresServiciosAutorizacion(GeneracionTarifasPendientesEntSub.obtenerErroresServicioAut(con, forma.getResultadosBusqueda("consecutivoaut_"+forma.getIndice()).toString()));
    	UtilidadBD.closeConnection(con);
		return mapping.findForward("tarifaServicio");
	}
    
    
    /**
     * accionGenerarTarifaServicioUnitario
     * @param con
     * @param forma
     * @param usuario
     * @param request
     * @param mapping
     * @return
     */
    private ActionForward accionGenerarTarifaServicioUnitario(Connection con, GeneracionTarifasPendientesEntSubForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) throws IPSException 
    {
    	forma.resetMensajes();
    	boolean esServicio = true;
    	DtoEntidadSubcontratada dtoEntidadSub = new DtoEntidadSubcontratada();
		
		CargosEntidadesSubcontratadasDao cargosDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosEntidadesSubcontratadasDao(); 
		
		String fechaAutorizacion = UtilidadFecha.getFechaActual();
		
		DtoContratoEntidadSub contratoEntidadSub = cargosDao.obtenerContratoVigenteEntidadSubcontratada(con,(forma.getResultadosBusqueda("entidad_"+forma.getIndice()).toString()),fechaAutorizacion);
		String retorno ="tarifaServicio";
		
		
		if(contratoEntidadSub!=null)
		{		 
			dtoEntidadSub.setCodigoPk(Long.valueOf(forma.getResultadosBusqueda("entidad_"+forma.getIndice()).toString()));
			
	    	double tarifa = generarTarifaSegunTipoEntidad(contratoEntidadSub,con, esServicio,dtoEntidadSub,forma, usuario );
			     	
	    	//Se mira si se genera o no la tarifa, si no se genera vuelve a la misma página, si se genera va al detalle
		    if (forma.getMensajesAlerta().size()!=0 || tarifa<0) 
		    {
		    	ActionErrors errors = new ActionErrors();
				errors.add("No se generó la tarifa", new ActionMessage("errores.generacionTarifasPendientesError"));
				saveErrors(request, errors);
		    	UtilidadBD.closeConnection(con);
		    }
			 else
			 {
				 forma.setResultadosBusqueda("tarifado_"+forma.getIndice(), ConstantesBD.acronimoSi); 
				 forma.setServiciosTarifados(GeneracionTarifasPendientesEntSub.obtenerServiciosTarifados(con,forma.getResultadosBusqueda()));
				 UtilidadBD.closeConnection(con);
				 retorno = "listadoServiciosTarifados";
			 }
		}	 
		return mapping.findForward(retorno);
    }
    
    
    
    /**
     * accionBuscarArticulos
     * @param con
     * @param forma
     * @param usuario
     * @param request
     * @param mapping
     * @return
     */
    private ActionForward accionBuscarArticulos(Connection con, GeneracionTarifasPendientesEntSubForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) {
		forma.setResultadosPedidos(GeneracionTarifasPendientesEntSub.buscarArticulosPedidos(con, forma.getFiltrosBusqueda()));
		forma.setResultadosSolicitudes(GeneracionTarifasPendientesEntSub.buscarArticulosSolicitudes(con, forma.getFiltrosBusqueda()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("busquedaArticulos");
	}
    
    
    /**
     * accionGenerarTarifaArticulos
     * @param con
     * @param forma
     * @param usuario
     * @param request
     * @param mapping
     * @return
     */
    private ActionForward accionGenerarTarifaArticulos(Connection con, GeneracionTarifasPendientesEntSubForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) throws IPSException 
    {
    	forma.resetMensajes();
    	
	 	//CargosEntidadesSubcontratadas cargos = new CargosEntidadesSubcontratadas();
	 	
	 	//Saco copia de los dos Mapas de solicitudes y pedidos de articulos
	 	forma.setResultadosPedidosAux(forma.getResultadosPedidos());
	 	forma.setResultadosSolicitudesAux(forma.getResultadosSolicitudes());
	 	
	    //Se generan las tarifas de los articulos-pedidos
	 	if (forma.getFiltrosBusqueda("tipotarifa").equals(ConstantesIntegridadDominio.acronimoArticulo))
	    {
	 		
    		for (int i=0;i<Utilidades.convertirAEntero(forma.getResultadosPedidos("numRegistros").toString());i++)
	    	{
    			int indice = i;
    			
	    		if (forma.getResultadosPedidos().containsKey("pedidoActivo_"+indice) && forma.getResultadosPedidos("pedidoActivo_"+indice).toString().equals("true"))
	    		{
	    			logger.info("Estoy generando tarifas de articulos-pedidos. Indice: "+indice);
	    			
	    			boolean esServicio = false;	
	    			DtoEntidadSubcontratada dtoEntidadSub = new DtoEntidadSubcontratada();
	    			//String retorno = "busquedaArticulosSol";
	    			CargosEntidadesSubcontratadasDao cargosDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosEntidadesSubcontratadasDao(); 
	    			String fechaAutorizacion = UtilidadFecha.getFechaActual();
	    			
	    			DtoContratoEntidadSub contratoEntidadSub = cargosDao.obtenerContratoVigenteEntidadSubcontratada(con,(forma.getResultadosSolicitudes().get("entidad_"+indice).toString()),fechaAutorizacion);
	    			 
	    			if(contratoEntidadSub!=null)
	    			{
	    				dtoEntidadSub.setCodigoPk(Long.valueOf(forma.getResultadosSolicitudes().get("entidad_"+indice).toString()));
	    				
	    				dtoEntidadSub.setConsecutivo(String.valueOf(dtoEntidadSub.getCodigoPk()));
	    				
	    				forma.setIndice(indice);
	    				double tarifa = generarTarifaSegunTipoEntidad(contratoEntidadSub, con, esServicio, dtoEntidadSub, forma, usuario );
	    				Log4JManager.info("La tarifa par el Indice: "+indice+" es:"+tarifa);;
	    			}
	    		}
	    	}
	    }
	 	
	 	//Se guardan los errores
	 	//forma.setMensajesAlertaPedidos(cargos.getErroresCargo());
	 	for (CargosEntidadesSubcontratadas cargos : forma.getListaCargos()) {
			forma.getMensajesAlerta().addAll(cargos.getErroresCargo());
		}
	 	
	 	
	    //Se generan las tarifas de los articulos-solicitudes
	    if (forma.getFiltrosBusqueda("tipotarifa").equals(ConstantesIntegridadDominio.acronimoArticulo))
	    {
	    	//Utilidades.imprimirMapa(forma.getResultadosSolicitudes());
	    	for (int l=0;l<Utilidades.convertirAEntero(forma.getResultadosSolicitudes("numRegistros").toString());l++)
	    	{
	    		int indice = l;
	    		
    			if(forma.getResultadosSolicitudes().containsKey("solicitudActivo_"+indice) && 
    					forma.getResultadosSolicitudes("solicitudActivo_"+indice).toString().equals("true"))
	    		{
    				Log4JManager.info("Estoy generando tarifas de articulos-solicitudes. Indice: "+indice);
	    			
    				boolean esServicio = false;	
	    			DtoEntidadSubcontratada dtoEntidadSub = new DtoEntidadSubcontratada();
	    			//String retorno = "busquedaArticulosSol";
	    			CargosEntidadesSubcontratadasDao cargosDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosEntidadesSubcontratadasDao(); 
	    			String fechaAutorizacion = UtilidadFecha.getFechaActual();
	    			
	    			DtoContratoEntidadSub contratoEntidadSub = cargosDao.obtenerContratoVigenteEntidadSubcontratada(con,(forma.getResultadosSolicitudes().get("entidad_"+indice).toString()),fechaAutorizacion);
	    			 
	    			if(contratoEntidadSub!=null)
	    			{
	    				dtoEntidadSub.setCodigoPk(Long.valueOf(forma.getResultadosSolicitudes().get("entidad_"+indice).toString()));
	    				
	    				dtoEntidadSub.setConsecutivo(String.valueOf(dtoEntidadSub.getCodigoPk()));
	    				
	    				forma.setIndice(indice);
	    				double tarifa = generarTarifaSegunTipoEntidad(contratoEntidadSub, con, esServicio, dtoEntidadSub, forma, usuario );
	    				Log4JManager.info("La tarifa par el Indice: "+indice+" es:"+tarifa);;
	    			}
	    		}
	    	}
	    }
	    
	    
	    //Se guardan los errores
	 	//forma.setMensajesAlertaPedidos(cargos.getErroresCargo());
	 	for (CargosEntidadesSubcontratadas cargos : forma.getListaCargos()) {
			forma.getMensajesAlerta().addAll(cargos.getErroresCargo());
		}
	 	
	 	
	    //Se cargan de nuevo los articulos pedidos y solicitudes
	    forma.setResultadosPedidos(GeneracionTarifasPendientesEntSub.buscarArticulosPedidos(con, forma.getFiltrosBusqueda()));
		forma.setResultadosSolicitudes(GeneracionTarifasPendientesEntSub.buscarArticulosSolicitudes(con, forma.getFiltrosBusqueda()));
		
	  //Se determina si se debe mostrar el array para los mensajes
	    if (forma.getMensajesAlertaSolicitudes().size()!=0)
	    	forma.setMostrarMensajeSolicitudes(true);
	    if (forma.getMensajesAlertaPedidos().size()!=0)
	    	forma.setMostrarMensajePedidos(true);
		
		//Saco el # de registros de cada mapa para realizar la comparación 
		int numRegsPedidos=Utilidades.convertirAEntero(forma.getResultadosPedidos("numRegistros")+"");
		int numRegsSolicitudes=Utilidades.convertirAEntero(forma.getResultadosSolicitudes("numRegistros")+"");
		int numRegsPedidosAux=Utilidades.convertirAEntero(forma.getResultadosPedidosAux("numRegistros")+"");
		int numRegsSolicitudesAux=Utilidades.convertirAEntero(forma.getResultadosSolicitudesAux("numRegistros")+"");
		
		
		//Se llenan los mapas con un nuevo registro para saber cuales registros se les generó tarifa para Pedidos
	    for (int i=0;i<numRegsPedidosAux;i++)
	    {
	    	for(int j=0;j<numRegsPedidos;j++)
	    	{
	    		if (forma.getResultadosPedidosAux("codigopedido_"+i).toString().equals(forma.getResultadosPedidos("codigopedido_"+j).toString()))
	    		{
	    			forma.setResultadosPedidosAux("tarifado_"+i, ConstantesBD.acronimoNo);
	    			break;
	    		}
	    		else
	    			forma.setResultadosPedidosAux("tarifado_"+i, ConstantesBD.acronimoSi);
	    	}
	    }
	    
	    //Se llenan los mapas con un nuevo registro para saber cuales registros se lesgeneró tarifa para Solicitudes
	    for (int i=0;i<numRegsSolicitudesAux;i++)
	    {
	    	for(int j=0;j<numRegsSolicitudes;j++)
	    	{
	    		if (forma.getResultadosSolicitudesAux("numsolicitud_"+i).toString().equals(forma.getResultadosSolicitudes("numsolicitud_"+j).toString()))
	    		{
	    			forma.setResultadosSolicitudesAux("tarifado_"+i, ConstantesBD.acronimoNo);
	    			break;
	    		}
	    		else
	    			forma.setResultadosSolicitudesAux("tarifado_"+i, ConstantesBD.acronimoSi);
	    	}
	    }
	    
	    
		if ((numRegsPedidos==numRegsPedidosAux)&&(numRegsSolicitudes==numRegsSolicitudesAux))
		{
			UtilidadBD.closeConnection(con);
			return mapping.findForward("busquedaArticulos");	
		}
		else
		{
			//Se buscan los elementos que se tarifaron tanto para pedidos como para solicitudes
			if (numRegsPedidos!=numRegsPedidosAux)
				forma.setPedidosTarifados(GeneracionTarifasPendientesEntSub.obtenerPedidosTarifados(con,forma.getResultadosPedidosAux()));
			
			if (numRegsSolicitudes!=numRegsSolicitudesAux)
				forma.setSolicitudesTarifados(GeneracionTarifasPendientesEntSub.obtenerSolicitudesTarifados(con,forma.getResultadosSolicitudesAux()));
			
			UtilidadBD.closeConnection(con);
			return mapping.findForward("listadoArticulosTarifados");
		}
	}
 
 
    /**
     * accionGenerarTarifaUnitariaPedido
     * @param con
     * @param forma
     * @param usuario
     * @param request
     * @param mapping
     * @return
     */
	 private ActionForward accionGenerarTarifaUnitariaPedido(Connection con, GeneracionTarifasPendientesEntSubForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) 
	 {
	 	forma.setErroresArticulosPedidos(GeneracionTarifasPendientesEntSub.obtenerErroresPedidos(con,forma.getResultadosPedidos("codigotarifa_"+forma.getIndice()).toString()));
	 	return mapping.findForward("busquedaArticulosPedidos");
	 }

	 
	 
	 /**
	  * acciongenerarTarifaPedido
	  * @param con
	  * @param forma
	  * @param usuario
	  * @param request
	  * @param mapping
	  * @return
	  */
	 private ActionForward acciongenerarTarifaPedido(Connection con, GeneracionTarifasPendientesEntSubForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) throws IPSException 
	 {  
		 //FIXME revisar ya que este flujo no se tuvo en cuenta para la generación de tarifas por no conocerse el flijo. Anexo 804 Cambio 1.2. Cristhian Murillo
		 CargosEntidadesSubcontratadas cargos = new CargosEntidadesSubcontratadas();    		
		 cargos.generarCargoArticulo(con, Utilidades.convertirAEntero(forma.getResultadosPedidos("ccsolicitado_"+forma.getIndice()).toString()), 
					Utilidades.convertirAEntero(forma.getResultadosPedidos("articulo_"+forma.getIndice()).toString()), 
					Utilidades.convertirAEntero(forma.getResultadosPedidos("artppal_"+forma.getIndice()).toString())
					, "", forma.getResultadosPedidos("codigopedido_"+forma.getIndice()).toString(), 
					forma.getResultadosPedidos("fecha_"+forma.getIndice()).toString(), forma.getResultadosPedidos("hora_"+forma.getIndice()).toString(), 
					UtilidadTexto.getBoolean(forma.getResultadosPedidos("vienedespacho_"+forma.getIndice()).toString()), usuario,forma.getFiltrosBusqueda("observaciones").toString(),"");
		 
		 forma.setMensajesAlertaPedidos(cargos.getErroresCargo());

		 // Si hay errores en la generacion vuelve a la misma pagina, sino va a el resumen de la generación
		 if (forma.getMensajesAlertaPedidos().size()!=0)
		 {
			 UtilidadBD.closeConnection(con);
			 return mapping.findForward("busquedaArticulosPedidos");
		 }
		 else
		 {
			 forma.setResultadosPedidos("tarifado_"+forma.getIndice(), ConstantesBD.acronimoSi);
			 forma.setPedidosTarifados(GeneracionTarifasPendientesEntSub.obtenerPedidosTarifados(con,forma.getResultadosPedidos()));
			 UtilidadBD.closeConnection(con);
			 return mapping.findForward("listadoArticulosTarifados");
		 }
	}
	 
	 
	 /**
	  * 
	  * @param con
	  * @param forma
	  * @param usuario
	  * @param request
	  * @param mapping
	  * @return
	  */
	 private ActionForward accionGenerarTarifaUnitariaSolicitud(Connection con, GeneracionTarifasPendientesEntSubForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) 
	 {
	 	forma.setErroresArticulosSolicitudes(GeneracionTarifasPendientesEntSub.obtenerErroresSolicitudes(con,forma.getResultadosSolicitudes("codigotarifa_"+forma.getIndice()).toString()));
	 	return mapping.findForward("busquedaArticulosSol");
	}
	 
	
	 /**
	  * accionGenerarTarifaSolicitud
	  * @param con
	  * @param forma
	  * @param usuario
	  * @param request
	  * @param mapping
	  * @return
	  */
	 private ActionForward accionGenerarTarifaSolicitud(Connection con, GeneracionTarifasPendientesEntSubForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) throws IPSException 
	 {
		 forma.resetMensajes();
		 boolean esServicio = false;	
		 DtoEntidadSubcontratada dtoEntidadSub = new DtoEntidadSubcontratada();
		 String retorno = "busquedaArticulosSol";
		 CargosEntidadesSubcontratadasDao cargosDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosEntidadesSubcontratadasDao(); 
		 String fechaAutorizacion = UtilidadFecha.getFechaActual();
		 
		 DtoContratoEntidadSub contratoEntidadSub = cargosDao.obtenerContratoVigenteEntidadSubcontratada(con,(forma.getResultadosSolicitudes().get("entidad_"+forma.getIndice()).toString()),fechaAutorizacion);			
		 
		if(contratoEntidadSub!=null)
		{		 
			 dtoEntidadSub.setCodigoPk(Long.valueOf(forma.getResultadosSolicitudes().get("entidad_"+forma.getIndice()).toString()));
			 double tarifa = generarTarifaSegunTipoEntidad(contratoEntidadSub, con, esServicio, dtoEntidadSub, forma, usuario );
			 
			 
			// Si hay errores en la generacion vuelve a la misma pagina, sino va a el resumen de la generación
			 if (forma.getMensajesAlertaPedidos().size()!=0 || tarifa<=0)
			 {
				 ActionErrors errors = new ActionErrors(); 
				 errors.add("No se generó la tarifa", new ActionMessage("errores.generacionTarifasPendientesError"));
				 saveErrors(request, errors);
				 UtilidadBD.closeConnection(con);				
			 }
			 else
			 {
				 forma.setResultadosSolicitudes("tarifado_"+forma.getIndice(), ConstantesBD.acronimoSi);
				 forma.setSolicitudesTarifados(GeneracionTarifasPendientesEntSub.obtenerSolicitudesTarifados(con,forma.getResultadosSolicitudes()));
				 UtilidadBD.closeConnection(con);
				 retorno ="listadoArticulosTarifados";
			 }
		}
		return mapping.findForward(retorno); 
	}
	 
	 
	 
	 /**
	  * Este Método se encarga de generar la tarifa pendiente para las autorizaciones. 
	  * La tarifa se genera según el tipo de tarifa parametrizado para el contrato vigente de la entidad subcontratada.
	  * Si Tipo Tarifa = Tarifa Propia. Se debe utilizar el método Cálculo Tarifa Entidades Subcontratadas-799. 
	  * Si el Tipo Tarifa = Tarifa Convenio Paciente. Se debe utilizar el método Calculo Tarifa del Cargo Modificado-438.
	  * Em ambos casos se genera cargo.
	  * 
	  * @param contratoEntidadSub
	  * @param con
	  * @param esServicio
	  * @param dtoEntidadSub
	  * @param forma
	  * @param usuario
	  * @return tarifa
	  * 
	  * @author Cristhian Murillo
	  */
	 private double generarTarifaSegunTipoEntidad(DtoContratoEntidadSub contratoEntidadSub, Connection con, boolean esServicio, 
			 DtoEntidadSubcontratada dtoEntidadSub, GeneracionTarifasPendientesEntSubForm forma, UsuarioBasico usuario ) throws IPSException
	 {
		double tarifa = ConstantesBD.codigoNuncaValidoDouble;
		DTOCalculoTarifaServicioArticulo dtoCalculo = new DTOCalculoTarifaServicioArticulo();
		EsquemaTarifario esquema = new EsquemaTarifario();
		dtoEntidadSub.setConsecutivo(String.valueOf(dtoEntidadSub.getCodigoPk()));
			
		dtoCalculo.setEntidadSubcontratada(dtoEntidadSub);
		dtoCalculo.setFechaVigencia(UtilidadFecha.getFechaActual());
		dtoCalculo.setEsServicio(esServicio);
		
		int codigoArticuloServicio = ConstantesBD.codigoNuncaValido;
		if(dtoCalculo.isEsServicio())
		{
			codigoArticuloServicio = Integer.parseInt(forma.getResultadosBusqueda("codservicio_"+forma.getIndice()).toString());
		} 
		else 
		{
			codigoArticuloServicio = Integer.parseInt(forma.getResultadosSolicitudes("articulo_"+forma.getIndice()).toString());
		}
		dtoCalculo.setCodigoArticuloServicio(codigoArticuloServicio);
		
			
		if(!UtilidadTexto.isEmpty(contratoEntidadSub.getTipoTarifa()))
		{
			CargosEntidadesSubcontratadas cargos = new CargosEntidadesSubcontratadas(); 
			
			String observaciones = "";
			if(forma.getFiltrosBusqueda("observaciones") != null){
				observaciones = forma.getFiltrosBusqueda("observaciones").toString();
			}
			
			if(contratoEntidadSub.getTipoTarifa().equals(ConstantesIntegridadDominio.acronimoTipoTarifaPropia))
			{
				/* Si Tipo Tarifa = Tarifa Propia. Se debe utilizar el método Cálculo Tarifa Entidades Subcontratadas-799 
				 * y asignar la Tarifa calculada para cada uno de los servicios / medicamentos e Insumos a Autorizarse. */
				
				tarifa = cargos.calcularTarifaEntidadSubcontratada(con, dtoCalculo, esquema);
				
				if(esServicio)
				{
					cargos.generarCargoAutorizacionConTarifaCalculada(con, 
						tarifa, 
						usuario, 
						forma.getResultadosBusqueda("consecutivo_"+forma.getIndice()).toString(), 
						contratoEntidadSub,
						dtoEntidadSub, 
						esquema, 
						forma.getResultadosBusqueda("fechaauto_"+forma.getIndice()).toString(), 
						forma.getResultadosBusqueda("horaauto_"+forma.getIndice()).toString(), 
						observaciones ,
						forma.getResultadosBusqueda("solicitud_"+forma.getIndice()).toString());
				}
				else
				{
					cargos.generarCargoArticuloConTarifaCalculada(con, 
						tarifa, 
						usuario, 
						Utilidades.convertirAEntero(forma.getResultadosSolicitudes("articulo_"+forma.getIndice()).toString()), 
						forma.getResultadosSolicitudes("numsolicitud_"+forma.getIndice()).toString(), 
						esquema, 
						contratoEntidadSub, 
						dtoEntidadSub, 
						observaciones, 
						forma.getResultadosSolicitudes("fecha_"+forma.getIndice()).toString(), 
						forma.getResultadosSolicitudes("hora_"+forma.getIndice()).toString(), 
						UtilidadTexto.getBoolean(forma.getResultadosSolicitudes("vienedespacho_"+forma.getIndice()).toString()), 
						Utilidades.convertirAEntero(forma.getResultadosSolicitudes("artppal_"+forma.getIndice()).toString()),
						forma.getResultadosBusqueda("consecutivo_"+forma.getIndice()).toString());
				}
			}
			else if(contratoEntidadSub.getTipoTarifa().equals(ConstantesIntegridadDominio.acronimoTipoTarifaConvenioPaciente))
			{
				/* Si el Tipo Tarifa = Tarifa Convenio Paciente. Se debe utilizar el método Calculo Tarifa del Cargo Modificado-438 
				 * y asignar la Tarifa   calculada para cada uno de los servicios / medicamentos e Insumos a Autorizarse. 
				 * Se debe tener en cuenta que este cálculo se realiza para el Convenio evaluado en la cobertura de la solicitud. */
				
				if(esServicio)
				{
					cargos.generarCargoAutorizacion(con, 
						forma.getResultadosBusqueda("consecutivo_"+forma.getIndice()).toString(),
						Utilidades.convertirAEntero(forma.getResultadosBusqueda("codservicio_"+forma.getIndice()).toString()), 
						forma.getResultadosBusqueda("entidad_"+forma.getIndice()).toString(), 
						forma.getResultadosBusqueda("fechaauto_"+forma.getIndice()).toString(), 
						forma.getResultadosBusqueda("horaauto_"+forma.getIndice()).toString(), 
						usuario, 
						observaciones,
						forma.getResultadosBusqueda("solicitud_"+forma.getIndice()).toString());  
					    
						//forma.setMensajesAlerta(cargos.getErroresCargo());				
						forma.getMensajesAlerta().addAll(cargos.getErroresCargo());
				}
				else
				{
					cargos.generarCargoArticulo(con, 
						Utilidades.convertirAEntero(forma.getResultadosSolicitudes("ccsol_"+forma.getIndice()).toString()), 
						Utilidades.convertirAEntero(forma.getResultadosSolicitudes("articulo_"+forma.getIndice()).toString()), 
						Utilidades.convertirAEntero(forma.getResultadosSolicitudes("artppal_"+forma.getIndice()).toString()), 
						forma.getResultadosSolicitudes("numsolicitud_"+forma.getIndice()).toString(),
						"", 
						forma.getResultadosSolicitudes("fecha_"+forma.getIndice()).toString(), 
						forma.getResultadosSolicitudes("hora_"+forma.getIndice()).toString(), 
						UtilidadTexto.getBoolean(forma.getResultadosSolicitudes("vienedespacho_"+forma.getIndice()).toString()),
						usuario, 
						observaciones,
						forma.getResultadosBusqueda("consecutivo_"+forma.getIndice()).toString());									
				}	
				
				//forma.setMensajesAlertaSolicitudes(cargos.getErroresCargo());
				forma.getMensajesAlertaSolicitudes().addAll(cargos.getErroresCargo());
				 
				tarifa = cargos.getTarifaCalculadad();				
			}
			
			// Solo se utiliza cuando son varias autorizaciones seleccionadas
			forma.getListaCargos().add(cargos);
		 }
		 return tarifa;
	 }
		
	 
}