
/*
 * Creado   8/12/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_03
 */
package com.princetonsa.action.inventarios;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

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

import util.BloqueosConcurrencia;
import util.ConstantesBD;
import util.Listado;
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.inventarios.ConstantesBDInventarios;
import util.inventarios.UtilidadInventarios;

import com.princetonsa.actionform.inventarios.RegistroTransaccionesForm;
import com.princetonsa.mundo.Articulo;
import com.princetonsa.mundo.ConsecutivosDisponibles;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.AlmacenParametros;
import com.princetonsa.mundo.inventarios.RegistroTransacciones;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;
import com.servinte.axioma.orm.delegate.inventario.ArticuloDelegate;
/**
 * 
 *
 * @version 1.0, 8/12/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class RegistroTransaccionesAction extends Action 
{
    /**
     * manejador de los logs de la clase
     */
    private static Logger logger=Logger.getLogger(RegistroTransaccionesAction.class);
    /**
	 * Método execute del action
	 */
	public ActionForward execute(	ActionMapping mapping, 	
													        ActionForm form, 
													        HttpServletRequest request, 
													        HttpServletResponse response) throws Exception
	{
		
		Connection con = null;
		try{
	    if(form instanceof RegistroTransaccionesForm)
	    {        
	        
			RegistroTransaccionesForm forma=(RegistroTransaccionesForm)form;
			RegistroTransacciones mundo=new RegistroTransacciones();
			HttpSession sesion = request.getSession();			
			UsuarioBasico usuario = null;
			usuario = Utilidades.getUsuarioBasicoSesion(sesion);
			String estado=forma.getEstado();
			String codigoEntidad=forma.getCodigoEntidad();
			String descripcionEntidad=forma.getDescripcionEntidad();
			logger.warn("[RegistroTransaccionesAction] --> "+estado);
			
			
			con = UtilidadBD.abrirConexion();
			if(con == null)
			{
				request.setAttribute("codigoDescripcionError", "errors.problemasBd");
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}			
			if(estado == null)
			{
				logger.warn("Estado no valido dentro del flujo de RegistroTransaccionesAction (null) ");
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}			
			else if(estado.equals("empezarFiltro"))
			{
		    	forma.reset();
		    	forma.resetTransaccion();
			    forma.resetObjetosTransaccionales(true);
			    return this.accionEmpezarGeneral(forma,con,mapping,request,usuario,mundo);
			}
			else if(estado.equals("generarFiltroAlmacen"))
			{			   
			    mundo.setInstitucion(usuario.getCodigoInstitucionInt());
			    mundo.setCodigoAlmacen(forma.getCodAlmacen());
			    forma.setSelectTransaccion(ConstantesBD.codigoNuncaValido+"");
		        //forma.setMapTransXCC(mundo.cargarListaTransaccionesValidasXCC(con));
		        forma.setEstado("empezar");
		        return this.accionValidarTransaccionesAlmacenes(con,mapping,request,forma,mundo);		    
			}
			else if(estado.equals("continuarTransaccion"))
			{
				ActionErrors errores=new ActionErrors();
				
				logger.info("--------FECHA ELABORACION-->"+forma.getFechaElaboracion());
				
				//Validar Fecha Elaboración
				if(UtilidadTexto.isEmpty(forma.getFechaElaboracion()+"")) 
				{
	        		errores.add("DEBE INGRESAR FECHA ELABORACIÓN!", new ActionMessage("errors.required","La fecha elaboración "));
	        		saveErrors(request, errores);
				} 
				else 
				{
					if(UtilidadFecha.esFechaValidaSegunAp(forma.getFechaElaboracion())) 
					{
						
						logger.info("1 "+forma.getFechaElaboracion());
						logger.info("2 "+UtilidadFecha.getFechaActual());
						
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getFechaElaboracion(), UtilidadFecha.getFechaActual()))
						{
							errores.add("FORMATO FECHA INVALIDO", new ActionMessage("errors.fechaPosteriorIgualActual","elaboración", "actual"));
			        		saveErrors(request, errores);
						}
					} 
					else 
					{
						errores.add("FORMATO FECHA INVALIDO", new ActionMessage("errors.formatoFechaInvalido","elaboración"));
		        		saveErrors(request, errores);
					}
				}
				
				if(!errores.isEmpty()) {
					UtilidadBD.closeConnection(con);
	    	        return mapping.findForward("paginaPrincipal");
				}
				
				if(forma.getSelectTransaccion().equals(ConstantesBD.codigoNuncaValido+""))
				{
	        		errores.add("DEBE SELECCIONAR UNA ENTIDAD!", new ActionMessage("errors.required","La transacción "));
	        		saveErrors(request, errores);
	        		UtilidadBD.closeConnection(con);
	    	        return mapping.findForward("paginaPrincipal");
				}
				else
				{	
					if(forma.getTipo_consignac().equals(ConstantesBD.acronimoSi))
					{
			        	if(Utilidades.convertirAEntero(forma.getMapaAtributos("codigoEntidad")+"")<=0)
				        {
			        		errores.add("DEBE SELECCIONAR UNA ENTIDAD!", new ActionMessage("error.inventarios.debeSeleccionarEntidad"));
			        		saveErrors(request, errores);
			        		UtilidadBD.closeConnection(con);
			    	        return mapping.findForward("paginaPrincipal"); 
				        }
			        	else
			        		return accionGenerarProcesoContinuar(con, forma,usuario,mapping,request);
					}
			        else
			        	return accionGenerarProcesoContinuar(con, forma,usuario,mapping,request);
				}	
			}
			else if(estado.equals("generarDetalleTransaccion"))
			{ 	
				ActionErrors errores=new ActionErrors();
				
				//Validar Fecha Elaboración
				if(UtilidadTexto.isEmpty(forma.getFechaElaboracion()+"")) 
				{
	        		errores.add("DEBE INGRESAR FECHA ELABORACIÓN!", new ActionMessage("errors.required","La fecha elaboración "));
	        		saveErrors(request, errores);
				} 
				else 
				{
					if(UtilidadFecha.esFechaValidaSegunAp(forma.getFechaElaboracion())) 
					{
						
						logger.info("1 "+forma.getFechaElaboracion());
						logger.info("2 "+UtilidadFecha.getFechaActual());
						
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getFechaElaboracion(), UtilidadFecha.getFechaActual()))
						{
							errores.add("FORMATO FECHA INVALIDO", new ActionMessage("errors.fechaPosteriorIgualActual","elaboración", "actual"));
			        		saveErrors(request, errores);
						}
					} 
					else 
					{
						errores.add("FORMATO FECHA INVALIDO", new ActionMessage("errors.formatoFechaInvalido","elaboración"));
		        		saveErrors(request, errores);
					}
				}
				
				if(!errores.isEmpty()) {
					UtilidadBD.closeConnection(con);
	    	        return mapping.findForward("paginaPrincipal");
				}
				
			    if((forma.getMapaAtributos("esCerrarTransaccion")+"").equals("true"))
			    {
			    	//en ninguna parte se estaba haciendo la validacion de si esta definido en consecutivo ajuste costo inv.
				    String codTemp=UtilidadBD.obtenerValorActualTablaConsecutivos(con,ConstantesBD.nombreConsecutivoAjusteCostoInv,usuario.getCodigoInstitucionInt());
				    if(codTemp.trim().equals("")||codTemp.trim().equals(ConstantesBD.codigoNuncaValido+""))
				    {
				        errores.add("NO HAY CONSECUTIVO DEFINIDO", new ActionMessage("error.inventarios.faltaDefinirConsecutivoAjusteCostoInv"));
				        saveErrors(request, errores);
				        UtilidadBD.closeConnection(con);
				        return mapping.findForward("paginaPrincipal");  
				    }

			        UtilidadBD.closeConnection(forma.getConTrans());
			        forma.setConTrans(UtilidadBD.abrirConexion());
			        forma.setEnTransaccion(mundo.empezarTransaccional(forma.getConTrans()));
			        return this.validacionesActualizarCerrarTransaccion(con,forma,mundo,mapping,request,usuario);
			    }
			    else
			    {
			    	//hermorhu - MT6360
			    	//Vaidacion de fecha de vencimiento de lote invalida
			    	boolean errorFechaVencimientoLoteInvalida = false;
			    	String articulosFechaVencLoteInvalida = "";
			    	ArticuloDelegate articuloDelegate = new ArticuloDelegate();
			    	com.servinte.axioma.orm.Articulo articulo = null;
			    	
			    	for(int i=0 ; i<forma.getNumeroFilasMapa() ; i++) {
				        if(forma.getMapaArticulos("manejaLoteArticulo_"+i)!=null && forma.getMapaArticulos("manejaLoteArticulo_"+i).toString().equals(ConstantesBD.valorTrueLargoEnString)
				        		&& forma.getMapaArticulos("manejaFechaVencimientoArticulo_"+i)!=null && forma.getMapaArticulos("manejaFechaVencimientoArticulo_"+i).toString().equals(ConstantesBD.valorTrueLargoEnString)
				        		&& !forma.getMapaArticulos("fechaVencimientoArticulo_"+i).toString().isEmpty()) {
			    			if(!UtilidadFecha.esFechaValidaSegunAp(forma.getMapaArticulos("fechaVencimientoArticulo_"+i).toString())) {
			    				errorFechaVencimientoLoteInvalida = true;
			    				if(!articulosFechaVencLoteInvalida.isEmpty()) {
			    					articulosFechaVencLoteInvalida += " - ";
			    				}
			    				articulo = articuloDelegate.obtenerArticuloPorId(Integer.parseInt(forma.getMapaArticulos("codigoArticulo_"+i).toString()));
			    				articulosFechaVencLoteInvalida += articulo.getDescripcion();
			    			}
			    		}	
			    	}

			    	if(errorFechaVencimientoLoteInvalida) {
			    		errores.add("", new ActionMessage("error.inventarios.fechaVencimientoLoteInvalida", articulosFechaVencLoteInvalida));
			    		saveErrors(request, errores);
						UtilidadBD.closeConnection(con);
		    	        return mapping.findForward("paginaPrincipal");
			    	} else {
			    		return this.accionGenerarDetalleTransaccion(con, forma, mundo, mapping, request,usuario);
			    	}
			    }
			}
			else if(estado.equals("comfirmarCierreTransaccion"))//en el caso de existir mensajes de alerta, en la asignación del costo de los articulos y se confirma el cierre de todas formas
			{ 			   	
			    //return this.accionGenerarDetalleTransaccion(con, forma, mundo, mapping, request);
		        //if forma.getMapaAtributos("codigoPKTransaccion")+"") es null o "" es por que el pup-up de confim
			    if(forma.getMapaAtributos("codigoPKTransaccion")!=null&&!(forma.getMapaAtributos("codigoPKTransaccion")+"").equalsIgnoreCase("null")&&!(forma.getMapaAtributos("codigoPKTransaccion")+"").equalsIgnoreCase(""))
			    	return this.validacionesActualizarCerrarTransaccion(con,forma,mundo,mapping,request,usuario);
            	UtilidadBD.closeConnection(forma.getConTrans());
            	UtilidadBD.closeConnection(con);
			    return mapping.findForward("inicio");	
			}
			else if(estado.equals("agregarArticulo"))
			{		    
			    //this.accionOrdenarMapa(forma);
				UtilidadBD.closeConnection(con);
			    return this.redireccion(con, forma, response, request);				       
			}
			else if (estado.equals("redireccion"))
			{			    
			    UtilidadBD.cerrarConexion(con);			    
			    response.sendRedirect(forma.getMapaAtributos("linkSiguiente")+"");
			    return null;
			}
			if(estado.equals("eliminarArticulo"))
			{			    
			    this.accionEliminarRegistros(con,forma);
			    return this.redireccion(con, forma, response, request);
			}
			if(estado.equals("buscarTransaccion"))
			{				    
			    this.limpiarCamposForm(forma);					    
			    forma.resetObjetosTransaccionales(true);//limpiar los objetos que son reutilizables, para limpiar datos anteriores
			    mundo.setInstitucion(usuario.getCodigoInstitucionInt());			    
			    mundo.setCodigoAlmacen(forma.getCodAlmacen());
		        forma.setMapTransXCCBusqueda(mundo.cargarListaTransaccionesValidasXCC(con));		        
		        if(Integer.parseInt(forma.getMapTransXCCBusqueda("numRegistros")+"")==0)
			    {
		            ActionErrors errores=new ActionErrors();
			        errores.add("almacen sin transaccion", new ActionMessage("error.inventarios.AlmacenSinTransacciones"));
			        saveErrors(request, errores);
			        forma.setEstado("error");			       
			    }		        
		        forma.setAccion("generarTransaccion");
		        UtilidadBD.closeConnection(con);
				return mapping.findForward("paginaBusqueda");	        		     	
			}
			else if(estado.equals("consultar"))
			{			   
			    forma.setAccion("consultarTransaccion");
			    this.limpiarCamposForm(forma);
			    mundo.setInstitucion(usuario.getCodigoInstitucionInt());			    
			    forma.setMapTransXCC(mundo.cargarListaTransaccionesValidasXCC(con));
			    UtilidadBD.cerrarConexion(con); 
			    return mapping.findForward("paginaConsulta"); 	
			}
			else if(estado.equals("ejercutarBusquedaAvanzada"))
			{			    
			    this.accionEjecutarBusquedaAvanzada(con,forma,mundo,usuario);
			    ValoresPorDefecto.cargarValoresIniciales(con);
			    forma.setMaxPageItem(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
			    UtilidadBD.cerrarConexion(con);
			    if(forma.getAccion().equals("generarTransaccion"))
			        return mapping.findForward("paginaBusqueda");
			    else
			        return mapping.findForward("listadoTransacciones");
			}			
			else if(estado.equals("cargarTransaccion"))
			{
			    ValoresPorDefecto.cargarValoresIniciales(con);
			    forma.setMaxPageItem(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
			    forma.setPatronOrdenar("codarticulo_");
			    if(forma.getAccion().equals("generarTransaccion"))
			    {
			        forma.setAccion("modificarTransaccion");
			        accionCargarTransaccionModificar(con,forma,mundo,usuario);
			        this.accionOredenarMapaArticulos(forma,true);
				    
			    }
			    else
			    {
			    	forma.setMapaArticulos(mundo.consultarDetalleTransaccion(con,forma.getListadoTrans("codigo_"+forma.getIndiceTrans())+""));
				    this.accionOredenarMapaArticulos(forma,false);
			    }
			    forma.setCodAlmacen(Integer.parseInt(forma.getListadoTrans("codigoalmacen_"+forma.getIndiceTrans())+""));
			    //this.accionOrdenarMapa(forma);
			    UtilidadBD.cerrarConexion(con);
			    if(forma.getAccion().equals("modificarTransaccion"))
			        return mapping.findForward("paginaPrincipal");
			    else
			        return mapping.findForward("detalleTransaccion");
			}
			else if(estado.equals("ordenarListadoTransacciones"))
			{
			    this.accionOredenar(forma);
			    UtilidadBD.cerrarConexion(con);	
			    if(forma.getAccion().equals("generarTransaccion"))
			    {
			    	forma.setEstado("ejercutarBusquedaAvanzada");
			    	return mapping.findForward("paginaBusqueda");			    	
			    }
			    else
			    	return mapping.findForward("listadoTransacciones");
			}
			else if(estado.equals("ordenarArticulosConsulta"))
			{
			    this.accionOredenarMapaArticulos(forma,false);
			    UtilidadBD.cerrarConexion(con);
			    return mapping.findForward("detalleTransaccion");
			}
			else if(estado.equals("generarAnulacion"))
			{		    
				ActionErrors errores=new ActionErrors();
				mundo.setCodigoPKTransaccion(Integer.parseInt(forma.getMapaAtributos("codigoPKTransaccion")+""));
			    boolean inserto=mundo.generarAnulacionTransaccion(con,usuario.getLoginUsuario(),forma.getMapaAtributos("motivoAnulacion")+"");
			    if(!inserto)			    			    	
			    	errores.add("no se grabo informacion", new ActionMessage("errors.noSeGraboInformacion","de la Anulación de la Transacción. Causa Posible: Otro Usuario Anulo la Transacción"));		   	
			    if(!errores.isEmpty())
			    {
			    	saveErrors(request, errores);
					UtilidadBD.cerrarConexion(con);
				    return mapping.findForward("paginaPrincipal");
			    }
			    else
			    {
			    	generarLog(forma,mundo,usuario,true);
			    	UtilidadBD.cerrarConexion(con);
				    return mapping.findForward("paginaResumen");
			    }			    
			}	
            else if(estado.equals("generarReporte"))
            {              
                this.generarReporte("listado", con, forma, usuario, request);
                UtilidadBD.cerrarConexion(con);
                return mapping.findForward("listadoTransacciones");
            }
            else if(estado.equals("generarReporteDetalle"))
            {                
                this.generarReporte("detalle", con, forma, usuario, request);                
                UtilidadBD.cerrarConexion(con);
                return mapping.findForward("detalleTransaccion");
            }
            else if(estado.equals("generarReporteDetalleResumen"))
            {                
                this.generarReporte("detalle", con, forma, usuario, request);                
                UtilidadBD.cerrarConexion(con);
                return mapping.findForward("paginaResumen");
            }
            else if(estado.equals("cancelarProceso"))
            {
            	forma.setMapaAtributos("validacionUnoRealizada", "false");
            	forma.setMapaAtributos("validacionDosRealizada", "false");
            	forma.setMapaAtributos("validacionTresRealizada", "false");
            	forma.setMapaAtributos("validacionCuatroRealizada", "false");
            	forma.setMapaAtributos("validacionCincoRealizada", "false");
            	forma.getWarnings().clear();
            	UtilidadBD.closeConnection(forma.getConTrans());
            	UtilidadBD.closeConnection(con);
            	
			    if(forma.getMapaAtributos("codigoPKTransaccion")!=null&&!(forma.getMapaAtributos("codigoPKTransaccion")+"").equalsIgnoreCase("null")&&!(forma.getMapaAtributos("codigoPKTransaccion")+"").equalsIgnoreCase(""))
			    	return mapping.findForward("paginaPrincipal");
			    return mapping.findForward("inicio");
            }
            else if(estado.equals("buscarEntidad"))
		    {
		    	
            	forma.setResultados(mundo.buscarEntidad(con, codigoEntidad, descripcionEntidad));
		    	UtilidadBD.closeConnection(con);
		    	return mapping.findForward("consultaEntidad");
		    }
            else if(estado.equals("busquedaTercero"))
            {
            	forma.setCodigoEntidad("");
            	forma.setDescripcionEntidad("");
            	forma.setResultados(new ArrayList());
            	UtilidadBD.closeConnection(con);
		    	return mapping.findForward("consultaEntidad");
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
	        logger.error("El form no es compatible con el form de RegistroTransaccionesForm");
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
	 * 
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param request
	 * @param usuario
	 * @param mundo
	 * @return
	 */
    private ActionForward accionEmpezarGeneral(RegistroTransaccionesForm forma, Connection con, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario, RegistroTransacciones mundo) 
    {
    	forma.setWarnings(new HashMap());
	    forma.setAccion("generarTransaccion");
	    forma.setEntidades(Utilidades.consultarTerceros(con,usuario.getCodigoInstitucionInt()));
	    return this.accionValidarFiltroAlmacen(con,mapping,request,usuario,forma,mundo);
	}
	/**
     * Metodo para generar los reportes
     * @param tipoReporte
     * @param con
     * @param forma
     * @param usuario
     * @param request
     */
	private void generarReporte(String tipoReporte,Connection con,RegistroTransaccionesForm forma,UsuarioBasico usuario,HttpServletRequest request) 
    {
		InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
        DesignEngineApi comp;        
        if(tipoReporte.equals("listado"))
        {
            comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"inventarios/","RegistroTransaccionesInventarios.rptdesign");
            comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
            comp.insertGridHeaderOfMasterPage(0,1,1,4);
            Vector v = new Vector();
            v.add(ins.getRazonSocial());
            if(Utilidades.convertirAEntero(ins.getDigitoVerificacion()) != ConstantesBD.codigoNuncaValido)
            	v.add(Utilidades.getDescripcionTipoIdentificacion(con,ins.getTipoIdentificacion())+". "+ins.getNit()+" - "+ins.getDigitoVerificacion());
            else
            	v.add(Utilidades.getDescripcionTipoIdentificacion(con,ins.getTipoIdentificacion())+". "+ins.getNit());     
            v.add(ins.getDireccion());
            v.add("Tels. "+ins.getTelefono());
            comp.insertLabelInGridOfMasterPage(0,1,v);
            String nombreAlmacen="";
            try
            {
            	nombreAlmacen=forma.getNombreAlmacen().toUpperCase();
            }
            catch (Exception e) 
            {}
            comp.insertLabelInGridPpalOfHeader(1,0, "RELACIÓN TRANSACIONES ALMACÉN "+nombreAlmacen);
            comp.insertLabelInGridPpalOfHeader(2,0, "Parámetros de Búsqueda: "+this.armarParametrosBusquedaBirt(forma, con));
            //comp.insertLabelInGridPpalOfHeader(1,0,ins.getEncabezado());
            //comp.insertLabelInGridPpalOfFooter(1,0,ins.getPie());           
            comp.obtenerComponentesDataSet("listadoTransacciones");            
            String oldQuery=comp.obtenerQueryDataSet();
            forma.getListadoTrans("consultaWhere");
            String newQuery=oldQuery+forma.getListadoTrans("consultaWhere")+"";
            comp.modificarQueryDataSet(newQuery);                
            //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
            if(!newPathReport.equals(""))
            {
                request.setAttribute("isOpenReport", "true");
                request.setAttribute("newPathReport", newPathReport);
            }            
//          por ultimo se modifica la conexion a BD
            comp.updateJDBCParameters(newPathReport);
        }
        else if(tipoReporte.equals("detalle"))
        {
            comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"inventarios/","DetalleRegistroTransaccionesInventarios.rptdesign");
            comp.obtenerComponentesDataSet("detalleEncabezadoTraslado");
            comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
            comp.insertGridHeaderOfMasterPage(0,1,1,4);
            Vector v=new Vector();
            v.add(ins.getRazonSocial());
            if(Utilidades.convertirAEntero(ins.getDigitoVerificacion()) != ConstantesBD.codigoNuncaValido)
            	v.add(Utilidades.getDescripcionTipoIdentificacion(con,ins.getTipoIdentificacion())+". "+ins.getNit()+" - "+ins.getDigitoVerificacion());
            else
            	v.add(Utilidades.getDescripcionTipoIdentificacion(con,ins.getTipoIdentificacion())+". "+ins.getNit());     
            v.add(ins.getDireccion());
            v.add("Tels. "+ins.getTelefono());
            comp.insertLabelInGridOfMasterPage(0,1,v);
            //comp.insertLabelInGridPpalOfHeader(1,0,ins.getEncabezado());
            //comp.insertLabelInGridPpalOfFooter(1,0,ins.getPie());
            //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
            if(!newPathReport.equals(""))
            {
                request.setAttribute("isOpenReport", "true");
                request.setAttribute("newPathReport", newPathReport);
            }            
//          por ultimo se modifica la conexion a BD
            comp.updateJDBCParameters(newPathReport);
        }
            
    }
    /**
	 * metodo para cargar la transaccion, realizada
	 * la busqueda
     * @param con Connection
     * @param forma
	 * @param mundo
	 * @param usuario
     */
    private void accionCargarTransaccionModificar(Connection con, RegistroTransaccionesForm forma, RegistroTransacciones mundo, UsuarioBasico usuario) 
    {
    	forma.setCodTransaccion(Integer.parseInt(forma.getListadoTrans("consecutivo_tipo_trans_"+forma.getIndiceTrans())+""));
    	forma.setSelectTransaccion(forma.getListadoTrans("codigo_tipo_trans_"+forma.getIndiceTrans())+"");
        forma.setNombreTransaccion((forma.getListadoTrans("codigo_tipo_trans_"+forma.getIndiceTrans())+"")+"-"+(forma.getListadoTrans("desctransaccion_"+forma.getIndiceTrans())+"")+"-"+(forma.getListadoTrans("desctipo_"+forma.getIndiceTrans())+""));
        forma.setMapaAtributos("nombreTipoConceptoTransaccion",forma.getListadoTrans("desctipo_"+forma.getIndiceTrans())+"");
        logger.info("===> ListadoTrans: "+forma.getListadoTrans());
        forma.setMapaAtributos("codigoTipoConceptoTransaccion",forma.getListadoTrans("codtipo_"+forma.getIndiceTrans())+"");
        //forma.setMapaAtributos("clase",forma.getListadoTrans("clase_inventario_"+forma.getIndiceTrans())+"");
        //forma.setMapaAtributos("grupo",forma.getListadoTrans("grupo_inventario_"+forma.getIndiceTrans())+"");
        forma.setMapaAtributos("valorConsecutivo",forma.getListadoTrans("numero_"+forma.getIndiceTrans())+"");
        forma.setMapaAtributos("tipoCosto",forma.getListadoTrans("tipo_costo_"+forma.getIndiceTrans())+"");        
        validarFechaElaboracion(con,forma,usuario);
        //debe poner la fecha de la transaccions
        forma.setFechaElaboracion(UtilidadFecha.conversionFormatoFechaAAp(forma.getListadoTrans("fechaelaboracion_"+forma.getIndiceTrans())+""));
    	forma.setLoginUsuario(forma.getListadoTrans("usuarioelabora_"+forma.getIndiceTrans())+"");
        forma.setMapaAtributos("codigoEntidad",forma.getListadoTrans("codigo_entidad_"+forma.getIndiceTrans()));
        forma.setMapaAtributos("nombreEntidad",forma.getListadoTrans("nomentidad_"+forma.getIndiceTrans()));
        forma.setObservaciones(forma.getListadoTrans("observaciones_"+forma.getIndiceTrans())+""); 
        forma.setMapaAtributos("transaccionModificable","nomodificable");
        forma.setMapaArticulos(mundo.consultarDetalleTransaccion(con,forma.getListadoTrans("codigo_"+forma.getIndiceTrans())+"",forma.getCodAlmacen(),usuario.getCodigoInstitucionInt()));
        forma.setMapaAtributos("codigoPKTransaccion",forma.getListadoTrans("codigo_"+forma.getIndiceTrans()));//código con el cual se inserto la transacción, para hacer referencia en el detalle        
    }
    /**
	 * metodo para generar las respectivas validaciones
	 * y procesos de la accion coninuar con la transdacción
     * @param con
     * @param forma
     * @param mundo
     * @param usuario
     * @param mapping
     * @param request
     * @param mundo      
     * @return ActionForward
     * @author jarloc
     */
    private ActionForward accionGenerarProcesoContinuar(
    		Connection con, RegistroTransaccionesForm forma,UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request) 
    {
    	ValoresPorDefecto.cargarValoresIniciales(con);
	    forma.setMaxPageItem(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
	    String tipoConsecutivo=ValoresPorDefecto.getManejoConsecutivoTransInv(usuario.getCodigoInstitucionInt());
        ConsecutivosDisponibles consec=new ConsecutivosDisponibles();
        
        /////ABRO ESTA TRANSACCION SOLO PARA BLOQUEAR LOS CONSECUTIVOS DE INVENTARIOS
        UtilidadBD.iniciarTransaccion(con);
        
        int codigoAlmacen=0;
        
        if(tipoConsecutivo.equals(ConstantesBDInventarios.valorConsecutivoTransInventariosUnicoSistema))
            codigoAlmacen=ConstantesBD.codigoNuncaValido;        
        else if(tipoConsecutivo.equals(ConstantesBDInventarios.valorConsecutivoTransInventariosPorAlmacen))
            codigoAlmacen=forma.getCodAlmacen(); 
        
        forma.setParejasClaseGrupo(UtilidadInventarios.obtenerParejasClaseGrupoInventario(con,usuario.getCodigoInstitucionInt(), forma.getCodAlmacen()  /*usuario.getCodigoCentroCosto()*/,forma.getCodTransaccion()+""));
		
        ArrayList filtro=new ArrayList();
        filtro.add(forma.getCodTransaccion()+"");
        filtro.add(usuario.getCodigoInstitucionInt()+"");
        filtro.add(codigoAlmacen+"");
        UtilidadBD.bloquearRegistro(con,BloqueosConcurrencia.bloqueoConsecutivoInventariosAlmacen,filtro);

        if(forma.getMapaAtributos("valorConsecutivo")==null||forma.getMapaAtributos("valorConsecutivo").toString().trim().equals("")||forma.getMapaAtributos("valorConsecutivo").toString().trim().equals("-1")||forma.getMapaAtributos("valorConsecutivo").toString().trim().equals("null"))
        	forma.setMapaAtributos("valorConsecutivo",consec.obtenerConsecutivoInventario(con,forma.getCodTransaccion(),codigoAlmacen,usuario.getCodigoInstitucionInt())+"");
        
        if(Integer.parseInt(forma.getMapaAtributos("valorConsecutivo")+"")==ConstantesBD.codigoNuncaValido)
        {
            forma.setMapaAtributos("existeError","true");
            ActionErrors errores=new ActionErrors();
	        errores.add("falta consecutivo", new ActionMessage("error.inventarios.faltaDefinirConsecutivo"));
	        saveErrors(request, errores);
	        forma.setEstado("error");
	        UtilidadBD.closeConnection(con);
	        return mapping.findForward("paginaPrincipal");      
        }
        else     
        {           
            return accionGuardarInformacionGeneral(con,forma,usuario,mapping,request);  
        }
    }
    /**
     * metodo para realizar la insercion o actualización
     * de la información general
     * @param con
     * @param forma
     * @param usuario
     * @param mapping
     * @param request
     * @return ActionForward
     * @author jarloc
     */
    private ActionForward accionGuardarInformacionGeneral(Connection con, RegistroTransaccionesForm forma, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request) 
    {  
    	ActionErrors errores=new ActionErrors(); 
    	
    	
        RegistroTransacciones mundo=new RegistroTransacciones();
        if(forma.getAccion().equals("modificarTransaccion"))
        {
        	if(!forma.getFechaElaboracion().equals(mundo.getFechaElaboracion())||!forma.getMapaAtributos("nombreEntidad").equals(mundo.getMapaAtributos().get("nombreEntidad"))||!forma.getObservaciones().equals(forma.getObservacionesOld()))
        	{
       		 	mundo.setMapaAtributos(forma.getMapaAtributos());
         		generarLog(forma,mundo,usuario,false);
         		mundo.setMapaAtributos(new HashMap());
         	}
        }
        mundo.setLoginUsuario(usuario.getLoginUsuario());
        mundo.setCodigoTipoTransaccion(forma.getCodTransaccion());
        mundo.setCodigoTransValidaXCC(Integer.parseInt(forma.getMapaAtributos("codigoTransValidaXCC")+""));
        mundo.setInstitucion(usuario.getCodigoInstitucionInt());
        String tipoConsecutivo=ValoresPorDefecto.getManejoConsecutivoTransInv(usuario.getCodigoInstitucionInt());
        mundo.setCodigoAlmacenTransaccion(forma.getCodAlmacen());
        mundo.setCodigoAlmacen(tipoConsecutivo.equals(ConstantesBDInventarios.valorConsecutivoTransInventariosPorAlmacen)?forma.getCodAlmacen():ConstantesBD.codigoNuncaValido);              
        
        if(forma.getAccion().equals("modificarTransaccion"))//se carga el código de la transacción en BD, para indicar que es una modificación          
            mundo.setCodigoTransModificar(Integer.parseInt((forma.getListadoTrans("codigo_"+forma.getIndiceTrans())+"")));   
        mundo.setFechaElaboracion(forma.getFechaElaboracion());
        mundo.setNombreTransaccion(forma.getNombreTransaccion());
        mundo.setMapaAtributos(forma.getMapaAtributos());
        mundo.setUsuario(usuario.getLoginUsuario());
        

        boolean inserto=mundo.generarInformacionGeneralTrans(con,Integer.parseInt(forma.getMapaAtributos("valorConsecutivo")+""),Integer.parseInt(forma.getMapaAtributos("codigoEntidad")+""),forma.getObservaciones(),ConstantesBDInventarios.codigoEstadoTransaccionInventarioPendiente);
        if(!inserto)
        {
            errores=new ActionErrors();
	        errores.add("no se grabo informacion", new ActionMessage("errors.noSeGraboInformacion"," DE LA INSERCION DE LA INFORMACION GENERAL DE LA TRANSACCION"));
	        saveErrors(request, errores);
	        forma.setEstado("error");
	        UtilidadBD.closeConnection(con);
	        return mapping.findForward("paginaPrincipal");  
        }
        else
        {
            if(forma.getAccion().equals("generarTransaccion"))
            {
                forma.setMapaAtributos("codigoPKTransaccion",mundo.getCodigoPKTransaccion()+"");//código con el cual se inserto la transacción, para hacer referencia en el detalle
            }
            forma.setCargarDetalle("visible");
		    forma.setMapaAtributos("transaccionModificable","nomodificable");
        }
	    UtilidadBD.closeConnection(con);
        return mapping.findForward("paginaPrincipal");  
    }
    
    /**
	 * @param conceptosForm
	 * @param usuario
	 * @param b
	 * @param i
	 */
	private void generarLog(RegistroTransaccionesForm forma, RegistroTransacciones mundo, UsuarioBasico usuario, boolean eliminacion)
	{
		String log = "";
		int tipoLog=0;
        if(eliminacion)
        {
	            log = 		 "\n   ============REGISTRO ANULADO=========== " +
				   			 "\n*  Transaccion [" +forma.getNombreTransaccion()+"] "+
				   			 "\n*  Tipo Transaccion [" +forma.getMapaAtributos("nombreTipoConceptoTransaccion")+"] "+
				   			 "\n*  Numero Transaccion [" +forma.getMapaAtributos("valorConsecutivo")+"] "+
				   			 "\n*  Fecha Elaboracion [" +forma.getFechaElaboracion()+"] "+
				   			 "\n*  Nombre Entidad [" +forma.getMapaAtributos("nombreEntidad")+"] "+
				   			 "\n*  Observaciones [" +forma.getObservaciones()+"] "+
		        			 "\n========================================================\n\n\n ";
	            tipoLog=ConstantesBD.tipoRegistroLogEliminacion;
        }
        if(!eliminacion)
        {
        	
	            log = 		 "\n   ============INFORMACION ORIGINAL=========== " +
	            			 "\n*  Transaccion [" +forma.getNombreTransaccion()+"] "+
	            			 "\n*  Tipo Transaccion [" +forma.getMapaAtributos("nombreTipoConceptoTransaccion")+"] "+
	            			 "\n*  Numero Transaccion [" +forma.getMapaAtributos("valorConsecutivo")+"] "+
	            			 "\n*  Fecha Elaboracion [" +forma.getFechaElaboracion()+"] "+
	            			 "\n*  Codigo Nueva Entidad [" +forma.getMapaAtributos("codigoEntidad")+"] "+
	            			 "\n*  Observaciones [" +forma.getObservaciones()+"] "+
		        			 "\n   ===========INFORMACION MODIFICADA========== 	" +
	            			 "\n*  Transaccion [" +forma.getMapaAtributos("nombreTipoConceptoTransaccion")+"] "+
	            			 "\n*  Tipo Transaccion [" +forma.getMapaAtributos("nombreTipoConceptoTransaccion")+"] "+
	            			 "\n*  Numero Transaccion [" +forma.getMapaAtributos("valorConsecutivo")+"] "+
	            			 "\n*  Fecha Elaboracion [" +forma.getFechaElaboracion()+"] "+
	            			 "\n*  Nombre Entidad [" +mundo.getMapaAtributos().get("nombreEntidad")+"] "+
	            			 "\n*  Observaciones [" +forma.getObservacionesOld()+"] "+
		        			 "\n========================================================\n\n\n ";
	            tipoLog=ConstantesBD.tipoRegistroLogModificacion;
        }        
        LogsAxioma.enviarLog(ConstantesBD.logRegistroTransaccionesInvCodigo,log,tipoLog,usuario.getLoginUsuario());		
     }
    /**
	 * Metodo para guardar la transacción
     * @param con
     * @param forma
     * @param mundo
	 * @param usuario
	 * @param mapping
	 * @param request
     * @param usuario
     * @author jarloc
     */
    private ActionForward accionGenerarDetalleTransaccion(Connection con, RegistroTransaccionesForm forma, RegistroTransacciones mundo,ActionMapping mapping, HttpServletRequest request,UsuarioBasico usuario) 
    {    
    	 ActionErrors errores=new ActionErrors();    
     	for(int k=0;k<forma.getNumeroFilasMapa();k++)	
     	{
     		for(int j=0;j<k;j++)
     		{
     			int codArt1=Utilidades.convertirAEntero(forma.getMapaArticulos().get("codigoArticulo_"+k)+"");
 		    	int codArt2=Utilidades.convertirAEntero(forma.getMapaArticulos().get("codigoArticulo_"+j)+"");
 		    	if(codArt1==codArt2 && 
 		    		(forma.getMapaArticulos().get("loteArticulo_"+k)+"").equals(forma.getMapaArticulos().get("loteArticulo_"+j)+"")	&&
 		    		(forma.getMapaArticulos().get("fechaVencimientoArticulo_"+k)+"").equals(forma.getMapaArticulos().get("fechaVencimientoArticulo_"+j)+"")
 		    		)
 		    	{
 		    		String mensaje=forma.getMapaArticulos().get("descripcionArticulo_"+k)+"";
 		    		errores.add("YA EXISTE EL REGISTRO.", new ActionMessage("errors.yaExiste","El Articulo "+mensaje));     
 		    	}
     		}
     	}
     	 if(!errores.isEmpty())
          {
              saveErrors(request, errores); 
              UtilidadBD.closeConnection(con);
  		    return mapping.findForward("paginaPrincipal"); 
          }
     	 
    	String codTransaccion="";
    	boolean isCerrarTransaccion=false;
        HashMap vo=new HashMap();
        vo.put("codigo",forma.getMapaAtributos("codigoPKTransaccion"));
        vo.put("institucion",usuario.getCodigoInstitucionInt());
        vo.put("fecha_elaboracion",UtilidadFecha.conversionFormatoFechaABD(forma.getFechaElaboracion()));
        vo.put("entidad",forma.getMapaAtributos("codigoEntidad"));
        vo.put("observaciones",forma.getObservaciones());
        vo.put("criterioBusqueda", forma.getCriterioBusqueda());
        
        
        mundo.setCodigoPKTransaccion(Integer.parseInt(forma.getMapaAtributos("codigoPKTransaccion")+""));
        codTransaccion=forma.getMapaAtributos("codigoPKTransaccion")+"";
        String numeroIdentificacion= Utilidades.obtenerNitProveedor(con, forma.getMapaAtributos("codigoEntidad")+"");
        if(forma.getAccion().equals("modificarTransaccion"))//se carga el código de la transacción en BD, para indicar que es una modificación
        {
            mundo.setCodigoTransModificar(Integer.parseInt((forma.getListadoTrans("codigo_"+forma.getIndiceTrans())+"")));
            codTransaccion=(forma.getListadoTrans("codigo_"+forma.getIndiceTrans())+"");
        }
        
        logger.info("\n\n\nNUMERO DE FILAS MAPA: "+forma.getNumeroFilasMapa());
        Utilidades.imprimirMapa(forma.getMapaArticulos());
        
        for(int k=0;k<forma.getNumeroFilasMapa();k++)
        {
        	String codigo=forma.getMapaArticulos("codigo_"+k)+"";
        	
        	logger.info("\n\n\nCODIGO DETALLE "+k+" ::::::::: "+codigo);
        	
            if(!forma.getMapaArticulos().containsKey("codigo_"+k))
            {
            	logger.info("entre 1");
            	codigo="-1";
            }
            if((forma.getMapaAtributos("esCerrarTransaccion")+"").equals("false"))//insertar el detalle independiente, de las validaciones
            {
            	forma.setEnTransaccion(mundo.generarDetalleTransaccionTrans(con,codigo,forma.getMapaArticulos("codigoArticulo_"+k)+"",forma.getMapaArticulos("cantidadArticulo_"+k)+"",forma.getMapaArticulos("valorUnitarioArticulo_"+k)+"",forma.getMapaArticulos("loteArticulo_"+k)+"",forma.getMapaArticulos("codigoInterfaz_"+k)+"", forma.getMapaArticulos("fechaVencimientoArticulo_"+k)+"", vo,forma.getArticulosEliminados(), numeroIdentificacion));
            	logger.info("entre 2 --->"+forma.isEnTransaccion());
            }
            else//insertar el detalle de la transaccion, dependiendo de las validaciones, de forma transaccional
            {
                forma.setEnTransaccion(mundo.generarDetalleTransaccionTrans(forma.getConTrans(),codigo,forma.getMapaArticulos("codigoArticulo_"+k)+"",forma.getMapaArticulos("cantidadArticulo_"+k)+"",forma.getMapaArticulos("valorUnitarioArticulo_"+k)+"",forma.getMapaArticulos("loteArticulo_"+k)+"",forma.getMapaArticulos("codigoInterfaz_"+k)+"",forma.getMapaArticulos("fechaVencimientoArticulo_"+k)+"",vo, forma.isEnTransaccion(),forma.getArticulosEliminados(), numeroIdentificacion));
            	logger.info("entre 3 --->"+forma.isEnTransaccion());
            }
            if(!forma.isEnTransaccion())
            {
            	logger.info("entre 4");
                break;
            }
        }
        
        
        if(!forma.isEnTransaccion())
        {            
	        errores.add("no se grabo informacion del detalle", new ActionMessage("errors.noSeGraboInformacion","DEL DETALLE DE ARTICULOS, CAUSA POSIBLE OTRO USUARIO ESTA MODIFICANDO LA TRANSACCION"));
	        saveErrors(request, errores);
	        forma.setEstado("error");	        
        } 
        /*Guardar informnación adicional al detalle de la transacción*/
        errores=this.accionGuardarInformacionAdicional(con, forma, mundo, errores,usuario);
        if(!errores.isEmpty())
        {
            saveErrors(request, errores); 
            UtilidadBD.closeConnection(con);
		    return mapping.findForward("paginaPrincipal"); 
        }
      //sino es cerrar transaccion se deben actualizar los datos basicos.
        if(!(forma.getMapaAtributos("esCerrarTransaccion")+"").equals("true"))
	    {
        	mundo.actualizarDatosBasicos(con,forma.getFechaElaboracion(),Integer.parseInt(forma.getMapaAtributos("codigoEntidad")+""),forma.getObservaciones());
	    }
        
        if((forma.getMapaAtributos("esCerrarTransaccion")+"").equals("true"))//cerrar la transacción abierta para realizar todas las actualizaciones de las validaciones del detalle, solo es abierta cuando se cierra la transacción
        {
        	isCerrarTransaccion=true;
            mundo.cerrarTransaccion(forma.getConTrans(),forma.isEnTransaccion());
            UtilidadBD.closeConnection(forma.getConTrans());
        }
        
        logger.info("===> reset accionGenerarDetalleTransaccion");
        forma.reset();
        forma.resetObjetosTransaccionales(false);
        forma.setEstado("resumen");
        //UtilidadBD.closeConnection(con);
        if(isCerrarTransaccion)
        {
        	forma.setCerrarRegistroTransaccion(true);
        	forma.setNumeroTransaccion(codTransaccion);
        }
	    //return mapping.findForward("paginaResumen");
        forma.setNumeroTransaccionResumen(UtilidadInventarios.obtenerConsecutivoTransaccion(con, mundo.getCodigoPKTransaccion()+""));
        forma.setProcesoExitoso(true);
        return this.accionEmpezarGeneral(forma,con,mapping,request,usuario,mundo);
    }
    /**
     * Método para realizar la inserción ó
     * actualización adicional a la transacción
     * @param con
     * @param forma
     * @param mundo
     * @param errores
     * @return ActionErrors
     */
    @SuppressWarnings("rawtypes")
	private ActionErrors accionGuardarInformacionAdicional(Connection con,RegistroTransaccionesForm forma,RegistroTransacciones mundo,ActionErrors errores,UsuarioBasico usuario)
    {  
    	boolean erroresCierre=false;
    	if((forma.getMapaAtributos("esCerrarTransaccion")+"").equals("true"))   	     
    	{    		
    		/**se toma la conexion de la transacción*/
    		
			con=null;
			con=forma.getConTrans();
    	
	    	/**actualizar el estado de la transacción*/    	        
	    	
			mundo.setCerrarTransaccion(forma.getMapaAtributos("esCerrarTransaccion")+"");
			
	    	/**Generar el registro del cierre de la transacción*/ 
    	
    		forma.setEnTransaccion(mundo.generarRegistroCierreTransaccion(con,forma.getMapaAtributos("codigoPKTransaccion")+"",usuario.getLoginUsuario(),UtilidadFecha.getFechaActual(),UtilidadFecha.getHoraActual(),forma.isEnTransaccion()));
    		if(!forma.isEnTransaccion())
        	{
        		errores.add("no se inserto el registro del cioerre", new ActionMessage("error.inventarios.errorCerrarTransaccion"));
        		erroresCierre=true;
        	}
    	}
    	if(!erroresCierre)//si no se generaron errores en el cierre se puede continuar
    	{
	    	/**si se generaron warning en la validación de asignacion de valores costo articulos, y 
	    	 * se confirma el cierre de la transacción, se actualiza el costo promedio de los articulos*/
    		/*
    		 * ESTO NO APLICA YA QUE SE CALCULA ES COSTO PROMEDIO.
    		 * En caso de no funcionar la asignacion del costo promedio par
    		 * tipo automatico, habilitar esta linea, pero solo para automatico.
	    	if(forma.getEstado().equals("comfirmarCierreTransaccion") && (forma.getMapaAtributos("esCerrarTransaccion")+"").equals("true"))        
			    for(int k=0;k<forma.getNumeroFilasMapa();k++)		    			        
			        errores=guardarAsignacionValoresCostoArticulos(errores, forma, mundo, k);
			        
			 */
	    	/**se actualiza la existencia para cada uno de los articulos, en articulos_almacen,
	    	 * segun si es una transacción de entrada ó de salida, yde acuerdo a la catidad ingresada*/
	    	int tipoConcepto=Integer.parseInt(forma.getMapaAtributos("codigoTipoConceptoTransaccion")+"");
	    	boolean esEntrada=false;       	
	        if(tipoConcepto==ConstantesBDInventarios.codigoTipoConceptoInventarioEntrada)
	        	esEntrada=true; 
	        if((forma.getMapaAtributos("esCerrarTransaccion")+"").equals("true"))
	        {
	        	/*forma.setConTrans(UtilidadBD.abrirConexion());        
	        	forma.setEnTransaccion(mundo.empezarTransaccional(forma.getConTrans()));
	        	con=forma.getConTrans();       */
		    	for(int k=0;k<forma.getNumeroFilasMapa();k++)	
		    	{
		    		boolean soloUpdate=false;
		    		HashMap mapa=UtilidadInventarios.obtenerArticuloAlmacenXLote(con, 
		        			Utilidades.convertirAEntero(forma.getMapaArticulos("codigoArticulo_"+k)+""), forma.getCodAlmacen());
		    		
		    		if(mapa.get("lote_"+k)== ""	&& mapa.get("fecha_vencimiento_"+k)== "" 
		    				&& Utilidades.convertirAEntero(mapa.get("existencias_"+k)+"")==0)
		    		{//si cumple las condiciones, es porque se creo un registro inicial por Funcionalidad : (Ubicacion Articulos Almacen)
		    			//y se debe reemplazar/actualizar ese registro en [articulos_almacen_x_lote].
		    			soloUpdate=true;
		    		}
		    			
		    		forma.setEnTransaccion(mundo.generarInsertUpdateArticuloXAlmacenTrans(con,
		    				Integer.parseInt(forma.getMapaArticulos("codigoArticulo_"+k)+""),
		    				forma.getCodAlmacen(),
		    				esEntrada,
		    				Integer.parseInt(forma.getMapaArticulos("cantidadArticulo_"+k)+""),
		    				forma.getMapaArticulos("loteArticulo_"+k)+"",
		    				forma.getMapaArticulos("fechaVencimientoArticulo_"+k)+"",
		    				usuario.getCodigoInstitucionInt(),
		    				forma.isEnTransaccion(),
		    				soloUpdate));
			    		
			    	if(!forma.isEnTransaccion()) {
		        		errores.add("no se grabo informacion de actualizacion articulosXAlmacen", new ActionMessage("errors.noSeGraboInformacion","DE LA ACTUALIZACION O INSERCION DE ARTICULOS POR ALMACEN"));    		
		        	}
		    	}   
	        }
	    	/*if(forma.isEnTransaccion() && (forma.getMapaAtributos("esCerrarTransaccion")+"").equals("false"))
	    	{
	    		mundo.cerrarTransaccion(forma.getConTrans(),forma.isEnTransaccion());
	    		UtilidadBD.closeConnection(con);
	    	}*/
    	}
    	return errores;
    }
    /**
     * metodo para realizar las validaciones,
     * para generar el detalle de la transacción.
     * @param con
     * @param forma
     * @param mundo
     * @param mapping
     * @param request
     * @param usuario
     * @return ActionForward
     */
    @SuppressWarnings("rawtypes")
	private ActionForward validacionesActualizarCerrarTransaccion(Connection conPrincipal, RegistroTransaccionesForm forma, RegistroTransacciones mundo,ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario)
    {
        ActionErrors errores=new ActionErrors();        
        @SuppressWarnings("unused")
		ArrayList arrayWarning;        
        boolean pasaTodasValidaciones=true;
        
    	boolean errorFechaVencimientoLoteInvalida = false;
    	String articulosFechaVencLoteInvalida = "";
    	ArticuloDelegate articuloDelegate = new ArticuloDelegate();
    	com.servinte.axioma.orm.Articulo articulo = null;
        
        for(int k=0;k<forma.getNumeroFilasMapa();k++)
        {
	        if(forma.getMapaArticulos("manejaLoteArticulo_"+k)!=null && forma.getMapaArticulos("manejaLoteArticulo_"+k).toString().equals(ConstantesBD.valorTrueLargoEnString)
	        		&& forma.getMapaArticulos("manejaFechaVencimientoArticulo_"+k)!=null && forma.getMapaArticulos("manejaFechaVencimientoArticulo_"+k).toString().equals(ConstantesBD.valorTrueLargoEnString))
	        {
	        	if(forma.getMapaArticulos("loteArticulo_"+k)==null || forma.getMapaArticulos("loteArticulo_"+k).toString().equals(""))
	        	{
		    		errores.add("falta campo lote", new ActionMessage("errors.required","El Lote del Artículo ["+forma.getMapaArticulos("codigoArticulo_"+k).toString()+"]"));
		    		saveErrors(request, errores); 
		    		pasaTodasValidaciones=false;
		    	}
	        	if(forma.getMapaArticulos("fechaVencimientoArticulo_"+k)==null || forma.getMapaArticulos("fechaVencimientoArticulo_"+k).toString().equals(""))
	        	{
		    		errores.add("falta campo fecha vencimiento", new ActionMessage("errors.required","La Fecha Vencimiento del Artículo ["+forma.getMapaArticulos("codigoArticulo_"+k).toString()+"]"));
		    		saveErrors(request, errores); 
		    		pasaTodasValidaciones=false;
	        	}
	        	
		    	//hermorhu - MT6360
		    	//Vaidacion de fecha de vencimiento de lote invalida
	        	if(forma.getMapaArticulos("fechaVencimientoArticulo_"+k)!= null && !forma.getMapaArticulos("fechaVencimientoArticulo_"+k).toString().equals("")) {
		        	if(!UtilidadFecha.esFechaValidaSegunAp(forma.getMapaArticulos("fechaVencimientoArticulo_"+k).toString())) {
	    				errorFechaVencimientoLoteInvalida = true;
	    				if(!articulosFechaVencLoteInvalida.isEmpty()) {
	    					articulosFechaVencLoteInvalida += " - ";
	    				}
	    				articulo = articuloDelegate.obtenerArticuloPorId(Integer.parseInt(forma.getMapaArticulos("codigoArticulo_"+k).toString()));
	    				articulosFechaVencLoteInvalida += articulo.getDescripcion();
		        	}
	        	}	
	        }
	        }
        
        if(errorFechaVencimientoLoteInvalida) {
        	errores.add("", new ActionMessage("error.inventarios.fechaVencimientoLoteInvalida", articulosFechaVencLoteInvalida));
    		saveErrors(request, errores); 
    		pasaTodasValidaciones=false;
        }
        
        if(!errores.isEmpty()){
        	UtilidadBD.closeConnection(conPrincipal);
        	return mapping.findForward("paginaPrincipal");
        }
        
        if((forma.getMapaAtributos("validacionCuatroRealizada")+"").equals("false"))
        {
        	logger.warn("\n\nDENTRO DE CUATRO");
        	forma.setMapaAtributos("validacionCuatroRealizada", "true");
        	if(UtilidadInventarios.existeCierreInventarioParaFecha(UtilidadFecha.getFechaActual(),usuario.getCodigoInstitucionInt()))
        	{
        		logger.warn("\n\nDENTRO DE CUATRO UNO");
        		errores.add("cierre de inventarios generado", new ActionMessage("error.inventarios.existeCierreInventarios",UtilidadFecha.getFechaActual()));
        		forma.setMapaAtributos("validacionCuatroRealizada", "false");
        		saveErrors(request, errores); 
        		pasaTodasValidaciones=false;
        		UtilidadBD.closeConnection(conPrincipal);
	    	    return mapping.findForward("paginaPrincipal"); 	  
        	}
        }
        if((forma.getMapaAtributos("validacionCincoRealizada")+"").equals("false"))
        {
        	logger.warn("\n\nDENTRO DE CINCO");
        	forma.setMapaAtributos("validacionCincoRealizada", "true");
        	validacionesDeStockPuntoPedido(forma,usuario);
        } 
        if((forma.getMapaAtributos("validacionUnoRealizada")+"").equals("false"))
        {
        	logger.warn("\n\nDENTRO DE UNO");
            errores=validarExistenciasArticulosParaTransaccion(conPrincipal,forma.getMapaArticulos(),forma,usuario,errores);
            forma.setMapaAtributos("validacionUnoRealizada","true");
            if(!errores.isEmpty())
	        {
            	logger.warn("\n\nDENTRO DE UNO UNO: "+errores);
	            saveErrors(request, errores);  
	            pasaTodasValidaciones=false;
	            UtilidadBD.closeConnection(conPrincipal);
	    	    return mapping.findForward("paginaPrincipal"); 	  
	        }  
	        if(forma.getWarnings().containsKey("warningValidacionExistencias"))
	        {
	        	logger.warn("\n\nDENTRO DE UNO UNO: "+errores);
	            arrayWarning=new ArrayList();
	            arrayWarning=(ArrayList)forma.getWarnings("warningValidacionExistencias");
	            UtilidadBD.closeConnection(conPrincipal);
	    	    return mapping.findForward("paginaPrincipal"); 	
	        }        
        }
        if((forma.getMapaAtributos("validacionDosRealizada")+"").equals("false"))
        {
        	logger.warn("\n\nDENTRO DE DOS");
        	errores=asigmacionValorCostoArticulos(conPrincipal,forma,usuario,errores);
	        forma.setMapaAtributos("validacionDosRealizada", "true");
	        if(!errores.isEmpty())
	        {
	        	logger.warn("\n\nDENTRO DE DOS UNO: "+errores);
	        	saveErrors(request, errores);
	            pasaTodasValidaciones=false;
	            UtilidadBD.closeConnection(conPrincipal);
	    	    return mapping.findForward("paginaPrincipal"); 	  
	        }  
	        if(forma.getWarnings().containsKey("warningValorCostoArticulos"))
	        {
	        	logger.warn("\n\nDENTRO DE DOS DOS: "+errores);
	            arrayWarning=new ArrayList();
	            arrayWarning=(ArrayList)forma.getWarnings("warningValorCostoArticulos");
	            pasaTodasValidaciones=false;
	            UtilidadBD.closeConnection(conPrincipal);
	    	    return mapping.findForward("paginaPrincipal");
	        } 	        
        }
        if((forma.getMapaAtributos("validacionTresRealizada")+"").equals("false"))
        {            
        	logger.warn("\n\nTRES");
        	errores=generacionNuevoCostoPromedio(conPrincipal,forma,usuario,errores,mundo);
	        forma.setMapaAtributos("validacionTresRealizada", "true");	        
	        if(!errores.isEmpty())
	        {
	        	logger.warn("\n\nDENTRO DE TRES: "+errores);
	            saveErrors(request, errores);  
	            pasaTodasValidaciones=false;	            
	            UtilidadBD.closeConnection(conPrincipal);            
	    	    return mapping.findForward("paginaPrincipal"); 	  
	        }	        
        }              
        if(pasaTodasValidaciones)
        {
        	logger.warn("\n\nPASO TODAS LAS VALIDACIONES");
            return this.accionGenerarDetalleTransaccion(conPrincipal,forma,mundo,mapping,request,usuario);
        }
        else
        {
            UtilidadBD.closeConnection(conPrincipal);
            return mapping.findForward("paginaPrincipal");
        }
    }
    /**
	 * metodo para redireccionar el pager, 
	 * cuando se ingresan nuevos registros
	 * @param con
	 * @param forma
	 * @param response
	 * @param request
	 * @return
	 * @author jarloc
	 */
	 public ActionForward redireccion (	Connection con,RegistroTransaccionesForm forma,HttpServletResponse response,HttpServletRequest request)
	 {
		forma.setOffset(((int)((forma.getNumeroFilasMapa()-1)/forma.getMaxPageItem()))*forma.getMaxPageItem());
		if(request.getParameter("ultimaPage")==null)
		{
			if(forma.getNumeroFilasMapa() > (forma.getOffset()+forma.getMaxPageItem()))
			forma.setOffset(((int)(forma.getNumeroFilasMapa()/forma.getMaxPageItem()))*forma.getMaxPageItem());
			try 
			{
			    UtilidadBD.closeConnection(con);
			    response.sendRedirect("registroTransacciones.jsp"+"?pager.offset="+forma.getOffset());
			} catch (IOException e) 
			{
			    e.printStackTrace();
			}
		}
		else
		{    
		    String ultimaPagina=request.getParameter("ultimaPage");	
		    int posOffSet=ultimaPagina.indexOf("offset=")+7;	    
		    if(forma.getNumeroFilasMapa()>(forma.getOffset()+forma.getMaxPageItem()))
		        forma.setOffset(forma.getOffset()+forma.getMaxPageItem());	
			try 
			{
			    UtilidadBD.closeConnection(con);
			    response.sendRedirect(ultimaPagina.substring(0,posOffSet)+forma.getOffset());
			} 
			catch (IOException e) 
			{			
			    e.printStackTrace();
			}
		}		
		return null;
	 }
	
    /**
     * accion para eliminar articulos
     * @param con
     * @param forma
     * @param mapping
     * @return
     * @author jarloc
     */
    private void accionEliminarRegistros(Connection con, RegistroTransaccionesForm forma) 
    {
        int posEli=Integer.parseInt(forma.getMapaAtributos("posArticuloEliminar")+"");
        
        logger.info("\n\n\nPOSICION ARTICULO ELIMINAR: "+posEli);
        
        int nuevaPos=forma.getNumeroFilasMapa()-1;
        
        logger.info("\n\n\nPOSICION ARTICULO ELIMINAR: "+nuevaPos);
        
        forma.setNumeroFilasMapa(nuevaPos);
        
        logger.info("\n\n\nNUMERO FILAS MAPA: "+forma.getNumeroFilasMapa());
        
        if(forma.getMapaArticulos().containsKey("codigo_"+posEli))
        	forma.getArticulosEliminados().add(forma.getMapaArticulos("codigoArticulo_"+posEli));
        
        for(int i=0; i<forma.getArticulosEliminados().size(); i++)
        {
        	logger.info("\n\n\nARTICULOS ELIMINADOS: "+forma.getArticulosEliminados());
        }
        
        if(posEli!=nuevaPos)
        {
        	logger.info("\n\n\nPOR EL IF.......");
        	
	        for(int j=posEli;j<forma.getNumeroFilasMapa();j++)
	        {                                 
	            forma.setMapaArticulos(("codigo_"+j),forma.getMapaArticulos("codigo_"+(j+1)));
	            forma.setMapaArticulos(("codigoArticulo_"+j),forma.getMapaArticulos("codigoArticulo_"+(j+1)));
	            forma.setMapaArticulos(("descripcionArticulo_"+j),forma.getMapaArticulos("descripcionArticulo_"+(j+1)));
	            forma.setMapaArticulos(("unidadMedidaArticulo_"+j),forma.getMapaArticulos("unidadMedidaArticulo_"+(j+1)));
	            forma.setMapaArticulos(("cantidadArticulo_"+j),forma.getMapaArticulos("cantidadArticulo_"+(j+1)));
	            forma.setMapaArticulos(("existenciaArticulo_"+j),forma.getMapaArticulos("existenciaArticulo_"+(j+1)));
	            forma.setMapaArticulos(("valorUnitarioArticulo_"+j),forma.getMapaArticulos("valorUnitarioArticulo_"+(j+1)));
	            forma.setMapaArticulos(("loteArticulo_"+j),forma.getMapaArticulos("loteArticulo_"+(j+1)));
	            forma.setMapaArticulos(("fechaVencimientoArticulo_"+j),forma.getMapaArticulos("fechaVencimientoArticulo_"+(j+1)));
	            forma.setMapaArticulos(("valorUnitarioArticuloTemp_"+j),forma.getMapaArticulos("valorUnitarioArticuloTemp_"+(j+1)));
	            forma.setMapaArticulos(("valorTotalArticulo_"+j),forma.getMapaArticulos("valorTotalArticulo_"+(j+1)));	            
	        }
        }
        forma.getMapaArticulos().remove("codigo_"+nuevaPos);
        forma.getMapaArticulos().remove("codigoArticulo_"+nuevaPos);
        forma.getMapaArticulos().remove("descripcionArticulo_"+nuevaPos);
        forma.getMapaArticulos().remove("unidadMedidaArticulo_"+nuevaPos);
        forma.getMapaArticulos().remove("cantidadArticulo_"+nuevaPos);
        forma.getMapaArticulos().remove("existenciaArticulo_"+nuevaPos);
        forma.getMapaArticulos().remove("loteArticulo_"+nuevaPos);
        forma.getMapaArticulos().remove("fechaVencimientoArticulo_"+nuevaPos);
        forma.getMapaArticulos().remove("valorUnitarioArticulo_"+nuevaPos);
        forma.getMapaArticulos().remove("valorUnitarioArticuloTemp_"+nuevaPos);
        forma.getMapaArticulos().remove("valorTotalArticulo_"+nuevaPos);
        UtilidadBD.closeConnection(con);		   
    }

    /**
     * metodo para ordenar los articulos
     * @param con
     * @param forma
     * @param mapping
     * @return
     * @author jarloc
     */	
    /*
     * NO SE ESTA USANDO
	private void accionOrdenarMapa(RegistroTransaccionesForm forma) 
    {                       
        forma.setPatronOrdenar("descripcionArticulo_");
	    String[] indices={
				            "codigoArticulo_", 
				            "descripcionArticulo_", 
				            "unidadMedidaArticulo_", 
				            "cantidadArticulo_",
				            "existenciaArticulo_",
				            "valorUnitarioArticulo_",
				            "valorUnitarioArticuloTemp_",
				            "valorTotalArticulo_"
	            		};
		forma.setMapaArticulos(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getMapaArticulos(),forma.getNumeroFilasMapa()));		
		forma.setUltimoPatron(forma.getPatronOrdenar());
    }
	*/
	/**
     * @param forma
     */
    private void accionOredenarMapaArticulos(RegistroTransaccionesForm forma,boolean indicesEnMayuscula)
    {
    		String[] indicesMayus={
    			"codigo_",
        		"existenciaArticulo_",
        		"valorUnitarioArticulo_",
        		"valorTotalArticulo_", 
        		"unidadMedidaArticulo_", 
        		"descripcionArticulo_",
        		"cantidadArticulo_",
        		"loteArticulo_",
        		"codigoInterfaz_",
        		"fechaVencimientoArticulo_",
        		"valorUnitarioArticuloTemp_",
        		"codigoArticulo_"
        		};
    		String[] indicesMinus={
    			"codigo_",
	    		"codarticulo_",
	    		"codigoInterfaz_",
	    		"descripcion_",
	    		"unidadmedida_",
	    		"cantidadarticulo_",
	    		"loteArticulo_",
        		"fechaVencimientoArticulo_",
	    		"valorunitario_",
	    		"valortotal_",
	    		"totaltransaccion_"
	    		};

        int numReg=Integer.parseInt(forma.getMapaArticulos("numRegistros")+"");
        
        if(indicesEnMayuscula)
        	forma.setMapaArticulos(Listado.ordenarMapa(indicesMayus,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getMapaArticulos(),numReg));
        else
        	forma.setMapaArticulos(Listado.ordenarMapa(indicesMinus,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getMapaArticulos(),numReg));
		forma.setMapaArticulos("numRegistros", numReg+"");
		forma.setUltimoPatron(forma.getPatronOrdenar());
    }
    
    /**
     * @param forma
     */
    private void accionOredenar(RegistroTransaccionesForm forma)
    {
        String[] indices={
        		"codigo_",
        		"consecutivo_tipo_trans_",
        		"codtipo_",
        		"desctipo_",
        		"desctransaccion_",
        		"numero_",
        		"estado_",
        		"descestado_",
        		"fechaelaboracion_",
        		"fechacierre_",
        		"horacierre_",
        		"valortotal_",
        		"codigoalmacen_",
        		"nombrealmacen_",
        		"codigocentroatencion_",
        		"indicativo_consignacion_",
        		"codTipo_",
        		"codigo_tipo_trans_",
        		"tipo_costo_",
        		"usuarioelabora_",
        		"codigo_entidad_",
        		"nomentidad_",
        		"observaciones_",
        		"nombrecentroatencion_"
        		};
        int numReg=Integer.parseInt(forma.getListadoTrans("numRegistros")+"");
        String wh=forma.getListadoTrans("consultaWhere").toString();
        int indice=forma.getIndiceTrans();
		forma.setListadoTrans(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getListadoTrans(),numReg));
		if(forma.getPatronOrdenar().equals("fechacierre_"))
		{
		    forma.setPatronOrdenar("horacierre_");
		    forma.setListadoTrans(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getListadoTrans(),numReg));
		}
		forma.setListadoTrans("numRegistros", numReg+"");
		forma.setListadoTrans("consultaWhere",wh);
		forma.setIndiceTrans(indice);
		forma.setUltimoPatron(forma.getPatronOrdenar());
		
    }


    /**
     * @param forma
     * 
     */
    private void limpiarCamposForm(RegistroTransaccionesForm forma)
    {
	    forma.setCodEstadoTrans(ConstantesBD.codigoNuncaValido);
	    forma.setCodTransaccion(ConstantesBD.codigoNuncaValido);
	    forma.setNumTransInicial(0);
	    forma.setNumTransFinal(0);
	    forma.setFechaElaboraInicial("");
	    forma.setFechaElaboraFinal("");
	    forma.setFechaCierreInicial("");
	    forma.setFechaCierreFinal("");
	    forma.setUsuarioElabora("");
	    forma.setUsuarioCierra("");
    }

    /**
     * @param con
     * @param forma
     * @param mundo
     * @param usuario
     */
    private void accionEjecutarBusquedaAvanzada(Connection con, RegistroTransaccionesForm forma, RegistroTransacciones mundo, UsuarioBasico usuario)
    {
        if(forma.getNumTransFinal()==0&&forma.getNumTransInicial()!=0)
            forma.setNumTransFinal(forma.getNumTransInicial());
        else if(forma.getNumTransFinal()!=0&&forma.getNumTransInicial()==0)
            forma.setNumTransInicial(forma.getNumTransFinal());
        
        HashMap vo = new HashMap();
        vo.put("codAlmacen",forma.getCodAlmacen()+"");
        vo.put("codTransaccion",forma.getCodTransaccion()+"");
        vo.put("numTransaccionInicial",forma.getNumTransInicial()+"");
        vo.put("numTransaccionFinal",forma.getNumTransFinal()+"");
        vo.put("fechaElaboracionInicial",forma.getFechaElaboraInicial()+"");
        vo.put("fechaElaboracionFinal",forma.getFechaElaboraFinal()+"");
        vo.put("fechaCierreInicial",forma.getFechaCierreInicial()+"");
        vo.put("fechaCierreFinal",forma.getFechaCierreFinal()+"");
        vo.put("usuarioCierra",forma.getUsuarioCierra()+"");
        vo.put("usuarioElabora",forma.getUsuarioElabora()+"");
        vo.put("institucion", usuario.getCodigoInstitucionInt()+"");
        vo.put("estado",forma.getCodEstadoTrans()+"");
        vo.put("centroAtencion",forma.getCentroAtencion().trim().equals("")?"-1":forma.getCentroAtencion());
        vo.put("indicativo_consignacion", forma.getIndicativo_consignacion());
        vo.put("codTipo", forma.getCodTipo()+"");
        forma.setListadoTrans(mundo.ejecutarBusquedaAvanzada(con,vo));
    }
    /**
     * metodo para inicailizar y validar la 
     * fecha de elaboración
     * @param forma
     * @author jarloc
     */
	private void validarFechaElaboracion(Connection con,RegistroTransaccionesForm forma,UsuarioBasico usuario)
    {	     
	     ValoresPorDefecto.cargarValoresIniciales(con);
	     if(UtilidadTexto.getBoolean(ValoresPorDefecto.getModificacionFechaInventario(usuario.getCodigoInstitucionInt())))
	         forma.setEsFechaModificable("true");
	     else
	         forma.setEsFechaModificable("false");
	     forma.setFechaElaboracion(UtilidadFecha.getFechaActual());
    }
	/**
     * Metodo para validar si el almacen
     * posee transacciones validas por centro
     * de costo
     * @param con
     * @param mapping
     * @param request
     * @param usuario
     * @param forma
     * @param mundo
     * @return findForward
     * @author jarloc
     */
    private ActionForward accionValidarTransaccionesAlmacenes(Connection con, ActionMapping mapping, HttpServletRequest request,  RegistroTransaccionesForm forma, RegistroTransacciones mundo) 
    {
        mundo.setCodigoAlmacen(forma.getCodAlmacen());
        forma.setMapTransXCC(mundo.cargarListaTransaccionesValidasXCC(con));
        if(Integer.parseInt(forma.getMapTransXCC("numRegistros")+"")==0)
	    {
            ActionErrors errores=new ActionErrors();
	        errores.add("almacen sin transaccion", new ActionMessage("error.inventarios.AlmacenSinTransacciones"));
	        saveErrors(request, errores);
	        forma.setEstado("error");
	        UtilidadBD.closeConnection(con);
	        return mapping.findForward("paginaErroresActionErrors");    
	    }	
        else
        {
            UtilidadBD.closeConnection(con);
			return mapping.findForward("paginaPrincipal");    
        }
    }
    /**
     * metodo para validar si el usuario pertenece
     * a uno,varios o ningun almacen y realizar asi
     * el forward respectivo segun el caso
     * @param con
     * @param mapping
     * @param request
     * @param usuario
     * @param forma
     * @param mundo
     * @return findForward
     * @author jarloc
     */
    private ActionForward accionValidarFiltroAlmacen(Connection con, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario, RegistroTransaccionesForm forma, RegistroTransacciones mundo) 
    {
        
        mundo.setInstitucion(usuario.getCodigoInstitucionInt());
	    forma.setMapAlmacenes(mundo.cargarAlmacenesXUsuario(usuario.getLoginUsuario()));
	    if(Integer.parseInt(forma.getMapAlmacenes("numRegistros")+"")==0)
	    {
	        ActionErrors errores=new ActionErrors();
	        errores.add("usuario sin almacén", new ActionMessage("error.inventarios.UsuarioNoDefinidoEnAlmacen"));
	        saveErrors(request, errores);
	        forma.setEstado("error");
	        UtilidadBD.closeConnection(con);
	        return mapping.findForward("paginaErroresActionErrors"); 
	    }
	    else if(Integer.parseInt(forma.getMapAlmacenes("numRegistros")+"")==1)
	    {			        
	        forma.setSelectTransaccion("");
	    	forma.setCodAlmacen(Integer.parseInt(forma.getMapAlmacenes("codigo_0")+""));
	        forma.setNombreAlmacen(forma.getMapAlmacenes("nombre_0")+"");
	        validarFechaElaboracion(con,forma,usuario);
	        forma.setLoginUsuario(usuario.getLoginUsuario());
	        forma.setTipo_consignac(forma.getMapAlmacenes("tipo_consignac_0")+"");
	        forma.setEstado("empezar");
	        return this.accionValidarTransaccionesAlmacenes(con,mapping,request,forma,mundo);		      
	    }
	    else if(Integer.parseInt(forma.getMapAlmacenes("numRegistros")+"")>0)
	    {
	        validarFechaElaboracion(con,forma,usuario);
	        forma.setLoginUsuario(usuario.getLoginUsuario());
	        UtilidadBD.closeConnection(con);
	        logger.info("--MAPA TRAIDO--> "+forma.getMapAlmacenes());
			return mapping.findForward("paginaFiltro");      
	    }
	    else
	    {
            UtilidadBD.closeConnection(con);
	        return null;
	    }	    
    }
    /**       
     * Para las transacciones que son definidas con
     * tipo de transacción=Salida, se verifica segun
     * el parametro general 'Existencias Negativas',
     * los mensajes de alerta son mostrados en el 
     * resumen.     
     * @param mapaArticulos
     * @param forma
     * @param usuario
     * @param errores2
     * @return ActionErrors
     */
    private static ActionErrors validarExistenciasArticulosParaTransaccion(Connection con,HashMap mapaArticulos,RegistroTransaccionesForm forma,UsuarioBasico usuario, ActionErrors errores)
    {     
    	ValoresPorDefecto.cargarValoresIniciales(con);
        boolean esExistenciaNegativa=AlmacenParametros.manejaExistenciasNegativas(con, forma.getCodAlmacen(), usuario.getCodigoInstitucionInt());
        ArrayList array=new ArrayList();
        String codigosArticulos="";
        String valoresExistenciasXArticulo="";
        if(Integer.parseInt(forma.getMapaAtributos("codigoTipoConceptoTransaccion")+"")==ConstantesBDInventarios.codigoTipoConceptoInventarioSalida)
        {
            int existenciaNegativa=0;
            Object warning;
            for(int k=0;k<forma.getNumeroFilasMapa();k++)
            {
            	int existencias=Integer.parseInt(mapaArticulos.get("existenciaArticulo_"+k)+"");
            	if(Articulo.articuloManejaLote(con, Integer.parseInt(mapaArticulos.get("codigoArticulo_"+k)+""), usuario.getCodigoInstitucionInt()))
            	{
            		existencias=UtilidadInventarios.existenciasArticuloAlmacenLote(Integer.parseInt(mapaArticulos.get("codigoArticulo_"+k)+""), forma.getCodAlmacen(), mapaArticulos.get("loteArticulo_"+k)+"", UtilidadFecha.conversionFormatoFechaABD(mapaArticulos.get("fechaVencimientoArticulo_"+k)+""));
            	}
	            if(!esExistenciaNegativa)
	            {                
	                if(Integer.parseInt(mapaArticulos.get("cantidadArticulo_"+k)+"")>existencias)
	                {
	                    errores.add("articulo existencia", new ActionMessage("error.inventarios.articuloConExistenciaInferiorACantidad",mapaArticulos.get("descripcionArticulo_"+k)+"",existencias+"",forma.getNombreAlmacen()));
	                }                
	            }
	            else
	            {
	            	 if(Integer.parseInt(mapaArticulos.get("cantidadArticulo_"+k)+"")>existencias)
		                {
		                existenciaNegativa=existencias-Integer.parseInt(mapaArticulos.get("cantidadArticulo_"+k)+"");
		                //errores.add("articulo existencia", new ActionMessage("error.inventarios.articuloQuedaConExistenciaNegativa",mapaArticulos.get("descripcionArticulo_"+k)+"",existenciaNegativa+""));
		                codigosArticulos+=mapaArticulos.get("codigoArticulo_"+k)+",";
		                valoresExistenciasXArticulo+=existenciaNegativa+",";
		                }
	            }
            }
            if(!codigosArticulos.trim().equals(""))
            {	
	            warning="ARTICULO(S) [ "+codigosArticulos+" ] QUEDA(N) CON EXISTENCIA(S) NEGATIVA(S) [ "+valoresExistenciasXArticulo+" ] [bz-14]";
	            array.add(warning);
	            forma.setWarnings("warningValidacionExistencias",array);
            }    
        }
        else
            logger.warn("*****NO requiere verificación de manejo de existencias negativas [Transaccion con concepto de Entrada]");   
        return errores;
    }
    /**
     * metodo para asignar el valor del 
     * costo de los articulos
     * @param forma
     * @param errores
     * @return ActionErrors
     */
    private static ActionErrors asigmacionValorCostoArticulos (Connection con,RegistroTransaccionesForm forma,UsuarioBasico usuario, ActionErrors errores)
    {        
       RegistroTransacciones mundo=new RegistroTransacciones(); 
       ValoresPorDefecto.cargarValoresIniciales(con);       
       String porcentajeParametro="";
       double diferencia=0,porcentaje=0;
       ArrayList array=new ArrayList();
       Object warning;       
       for(int k=0;k<forma.getNumeroFilasMapa();k++)
       {
	       if(Integer.parseInt(forma.getMapaAtributos("tipoCosto")+"")==ConstantesBDInventarios.codigoTipoCostoInventarioAutomatico)
	       {
               errores=guardarAsignacionValoresCostoArticulos(errores,forma,mundo,k);
	       }
	       else if(Integer.parseInt(forma.getMapaAtributos("tipoCosto")+"")==ConstantesBDInventarios.codigoTipoCostoInventarioManual)
	       {
	           porcentajeParametro=ValoresPorDefecto.getPorcentajeCostosInv(usuario.getCodigoInstitucionInt());
	           if(!porcentajeParametro.equals(""))
	           {	             
	               porcentaje=(Double.parseDouble(forma.getMapaArticulos("valorUnitarioArticuloTemp_"+k)+"")*Double.parseDouble(porcentajeParametro))/100;
	               diferencia= Double.parseDouble(forma.getMapaArticulos("valorUnitarioArticulo_"+k)+"")-Double.parseDouble(forma.getMapaArticulos("valorUnitarioArticuloTemp_"+k)+"");
	               if(diferencia>porcentaje)
	               {
	                   warning="ARTICULO "+forma.getMapaArticulos("descripcionArticulo_"+k)+" TIENE UN COSTO ACTUAL DE "+Double.parseDouble(forma.getMapaArticulos("valorUnitarioArticuloTemp_"+k)+"")+" Y SE ESTA INGRESANDO UN VALOR DE "+forma.getMapaArticulos("valorUnitarioArticulo_"+k)+". [bz-15]";
	                   array.add(warning);
	                   forma.setWarnings("warningValorCostoArticulos",array);
	               }
	               else              
	                   errores=guardarAsignacionValoresCostoArticulos(errores,forma,mundo,k); 
	                              
	           }                            
	       }
       }
       return errores;
    }
    /**
     * metodo para guardar la asignacion de 
     * valor de costo a los articulos, despues
     * de realizadas las validaciones correspondientes
     * @param errores
     * @param forma
     * @param mundo
     * @param index
     * @return
     */
    private static ActionErrors guardarAsignacionValoresCostoArticulos(ActionErrors errores,RegistroTransaccionesForm forma,RegistroTransacciones mundo,int index)
    {             
        forma.setEnTransaccion(mundo.asignarValorCostoArticulosTrans(forma.getConTrans(),Integer.parseInt(forma.getMapaArticulos("codigoArticulo_"+index)+""),Double.parseDouble(forma.getMapaArticulos("valorUnitarioArticulo_"+index)+""),forma.isEnTransaccion()));
        if(!forma.isEnTransaccion())   
        {
            errores.add("no se grabo informacion de actualizacion costo del articulo", new ActionMessage("errors.noSeGraboInformacion","DE LA ACTUALIZACION DEL COSTO DEL ARTICULO"));
        }
        else
            logger.warn("Actualizar el valor de costo del articulo --- > "+forma.getMapaArticulos("descripcionArticulo_"+index)); 
        return errores;
    }
    /**
     * metodo para realizar la generación del costo promedio
     * de los articulos
     * @param conPrincipal 
     * @param forma
     * @param usuario
     * @param errores
     * @return ActionErrors
     */
    private static ActionErrors generacionNuevoCostoPromedio(Connection conPrincipal, RegistroTransaccionesForm forma,UsuarioBasico usuario,ActionErrors errores,RegistroTransacciones mundo)
    {
       boolean actualizo=false;
       double nuevoCosto=0;
       ArrayList articulosCalculados=new ArrayList();
       int tipoConcepto=Integer.parseInt(forma.getMapaAtributos("codigoTipoConceptoTransaccion")+"");
       if(tipoConcepto==ConstantesBDInventarios.codigoTipoConceptoInventarioSalida || tipoConcepto==ConstantesBDInventarios.codigoTipoConceptoInventarioEntrada)
       {           
           if(Integer.parseInt(forma.getMapaAtributos("tipoCosto")+"")==ConstantesBDInventarios.codigoTipoCostoInventarioManual)
           {
               for(int k=0;k<forma.getNumeroFilasMapa();k++)
               {
            	   if(!articulosCalculados.contains(forma.getMapaArticulos("codigoArticulo_"+k)))
            	   {
            		   double valorTotal=0;
            		   int cantidadTotal=0;
	            	   for(int i=k;i<forma.getNumeroFilasMapa();i++)
	            	   {
	            		   if(Integer.parseInt(forma.getMapaArticulos("codigoArticulo_"+k)+"")==Integer.parseInt(forma.getMapaArticulos("codigoArticulo_"+i)+""))
	            		   {
	            			   valorTotal+=Double.parseDouble(forma.getMapaArticulos("valorTotalArticulo_"+i)+"");
	            			   cantidadTotal+=Integer.parseInt(forma.getMapaArticulos("cantidadArticulo_"+i)+"");
	            		   }
	            	   }
	            	   nuevoCosto=UtilidadInventarios.calcularCostoPromedioArticulo(conPrincipal,Integer.parseInt(forma.getMapaArticulos("codigoArticulo_"+k)+""),cantidadTotal,valorTotal,tipoConcepto,usuario.getCodigoInstitucionInt(), forma.getCodAlmacen());
	                   actualizo=UtilidadInventarios.actualizarCostoPromedioArticulo(forma.getConTrans(),Integer.parseInt(forma.getMapaArticulos("codigoArticulo_"+k)+""),nuevoCosto);
	                   if(!actualizo)
	                   {
	                       errores.add("no se actualizo el costo promedio", new ActionMessage("errors.noSeGraboInformacion","DE LA ACTUALIZACION DEL COSTO PROMEDIO DEL ARTICULO"));
	                   }
	                   else
	                   {
	                       
	                       actualizo=mundo.generarRegistroAjusteTrans(forma.getConTrans(),Integer.parseInt(forma.getMapaArticulos("codigoArticulo_"+k)+""),forma.getCodAlmacen(),forma.getCodTransaccion(),forma.getMapaAtributos("codigoPKTransaccion")+"",Double.parseDouble(forma.getMapaArticulos("valorUnitarioArticuloTemp_"+k)+""),nuevoCosto,ConstantesBDInventarios.codigoTipoCostoInventarioAutomatico,usuario.getCodigoInstitucionInt(),forma.isEnTransaccion());
	                       if(!actualizo)
	                       {
	                    	   if(Utilidades.convertirAEntero(mundo.getConsecutivoDisponible())==ConstantesBD.codigoNuncaValido)
	                    		   errores.add("falta actualizar el año del consecutivo", new ActionMessage("errors.faltaParametrizacion","año vigente para el consecutivo disponible Ajustes al Costo en Inventarios para la actualización del costo promedio del artículo N° "+forma.getMapaArticulos("codigoArticulo_"+k)));
	                    	   else
	                    		   errores.add("no se genero el ajuste", new ActionMessage("errors.noSeGraboInformacion","DE LA ACTUALIZACION DEL COSTO PROMEDIO DEL ARTICULO"));
	                       }
	                   }
	                   articulosCalculados.add(forma.getMapaArticulos("codigoArticulo_"+k));
            	   }
               }
           }
       }
       return errores;
    }
    /**
     * metodo para realizar las validaciones 
     * correspondientes a a stock minimo,
     * maximo y punto de pedido.
     * los mensajes de alerta si son generados
     * se muestran en el resumen, estos dejan 
     * continuar con el proceso.
     * @param forma
     * @param usuario
     */
    private static void validacionesDeStockPuntoPedido(RegistroTransaccionesForm forma,UsuarioBasico usuario)
    {
        String articulosStockMinimo="",articulosStockMaximo="",articulosPuntoPedido="";
        ArrayList array=new ArrayList();
    	Object warning;
    	for(int k=0;k<forma.getNumeroFilasMapa();k++)	
    	{
    		if(!UtilidadInventarios.existenciasArticuloMayorIgualStockMinimo(Integer.parseInt(forma.getMapaArticulos("codigoArticulo_"+k)+""),usuario.getCodigoInstitucionInt()))
    		{
    			articulosStockMinimo=forma.getMapaArticulos("descripcionArticulo_"+k)+""+",";           		
    		}
    		if(!UtilidadInventarios.existenciasArticuloMenorIgualStockMaximo(Integer.parseInt(forma.getMapaArticulos("codigoArticulo_"+k)+""),usuario.getCodigoInstitucionInt()))
    		{
    			articulosStockMaximo=forma.getMapaArticulos("descripcionArticulo_"+k)+""+",";           		
    		}
    		if(!UtilidadInventarios.existenciasArticuloMayorIgualPuntoPedido(Integer.parseInt(forma.getMapaArticulos("codigoArticulo_"+k)+""),usuario.getCodigoInstitucionInt()))
    		{
    			articulosPuntoPedido=forma.getMapaArticulos("descripcionArticulo_"+k)+""+",";           		
    		}
    	}       
    	if(!articulosStockMinimo.equals(""))
    	{
    	    warning="ARTICULOS "+articulosStockMinimo+" QUEDAN CON CANTIDAD MENOR AL STOCK MINIMO. [bz-16]"; 
    	    array.add(warning);
    	    forma.setWarnings("warningsStocksPuntoPedido",array);
    	}    	
    	if(!articulosStockMaximo.equals(""))
    	{
    	    warning="ARTICULOS "+articulosStockMaximo+" QUEDAN CON CANTIDAD MAYOR AL STOCK MAXIMO. [bz-16]"; 
    	    array.add(warning);
    	    forma.setWarnings("warningsStocksPuntoPedido",array);    		
    	}
    	if(!articulosPuntoPedido.equals(""))
    	{
    	    warning="ARTICULOS "+articulosPuntoPedido+" QUEDAN CON CANTIDAD MENOR AL PUNTO DE PEDIDO. [bz-16]"; 
    	    array.add(warning);
    	    forma.setWarnings("warningsStocksPuntoPedido",array);  		
    	}    	
    }
    
    /**
     * Retorna un string con la descripcion de los parametros de busqueda para mostrarlos 
     * en el reporte de birt
     * @param forma
     * @return
     */
    private String armarParametrosBusquedaBirt(RegistroTransaccionesForm forma, Connection con)
    {
    	HashMap mapa= new HashMap();
    	if(forma.getCodAlmacen()>0)
    		mapa.put("esBusquedaPorFarmacia", forma.getCodAlmacen());
    	if(forma.getCodTransaccion()>0)
    		mapa.put("esBusquedaPorTransaccion", forma.getCodTransaccion());
    	if(forma.getCodEstadoTrans()>0)
    		mapa.put("esBusquedaPorEstado", forma.getCodTransaccion());
    	//primero se obtiene el mapa con los nombres
    	RegistroTransacciones mundoTrans= new RegistroTransacciones();
    	mapa=(HashMap)mundoTrans.getNombresReporteBirt(con,mapa).clone();
    	
    	Utilidades.imprimirMapa(mapa);
    	
    	String parametrosBusqueda= "";
    	if(forma.getCodAlmacen()>0)
    		parametrosBusqueda+=" - Almacén: "+mapa.get("nombreFarmacia"); 
    	if(forma.getCodTransaccion()>0)
    		parametrosBusqueda+=" - Transacción: "+mapa.get("nombreTransaccion");
    	if(forma.getCodEstadoTrans()>0)
    		parametrosBusqueda+=" - Estado: "+mapa.get("nombreEstado");
    	if(forma.getNumTransInicial()>0 && forma.getNumTransFinal()>0 )
    		parametrosBusqueda+=" - No. Transacción Inicial: "+forma.getNumTransInicial()+" No. Transaccion Final: "+forma.getNumTransFinal();

    	
    	logger.info("<<<<<<<<<");
    	logger.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
    	logger.info("Fecha Elab Inicial: " + forma.getFechaElaboraInicial() + "-- Fecha Elab Final: " + forma.getFechaElaboraFinal());
    	
    	if(!forma.getFechaElaboraInicial().equals(""))
    		if(!forma.getFechaElaboraFinal().equals(""))
    			parametrosBusqueda+=" - Fecha Elaboración Inicial: "+forma.getFechaElaboraInicial()+" Fecha Elaboración Final: "+forma.getFechaElaboraFinal();
    	if(!forma.getFechaCierreInicial().equals(""))
    		if(!forma.getFechaCierreFinal().equals(""))
    			parametrosBusqueda+=" - Fecha Cierre Inicial: "+forma.getFechaCierreInicial()+" Fecha Elaboración Final: "+forma.getFechaCierreFinal();
    	if(!forma.getUsuarioElabora().equals(""))
    		parametrosBusqueda+=" - Usuario Elabora: "+forma.getUsuarioElabora();
    	if(!forma.getUsuarioCierra().equals(""))
    		parametrosBusqueda+=" - Usuario Cierra: "+forma.getUsuarioCierra();
    	return parametrosBusqueda;
    }
}
