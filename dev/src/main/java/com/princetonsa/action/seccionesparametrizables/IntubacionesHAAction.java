package com.princetonsa.action.seccionesparametrizables;

import java.sql.Connection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;

import com.princetonsa.actionform.seccionesparametrizables.IntubacionesHAForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.parametrizacion.IntubacionesHA;
import com.princetonsa.mundo.salasCirugia.HojaAnestesia;

/**
 * 
 * @author wilson
 *
 */
public class IntubacionesHAAction extends Action 
{
	/**
     * Objeto para manejar los logs de esta clase
    */
    private Logger logger = Logger.getLogger(IntubacionesHAAction.class);
    
    /**
	 * Metodo excute del Action
	 */
    public ActionForward execute(   ActionMapping mapping,
            						ActionForm form,
            						HttpServletRequest request,
            						HttpServletResponse response ) throws Exception
            						{
    	Connection con = null;
    	try {
    		if (response==null); 
    		if(form instanceof IntubacionesHAForm)
    		{
    			con = UtilidadBD.abrirConexion();
    			if(con == null)
    			{
    				request.setAttribute("codigoDescripcionError", "errors.problemasBd");
    				return mapping.findForward("paginaError");
    			}

    			UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
    			IntubacionesHAForm forma =(IntubacionesHAForm)form;
    			String estado=forma.getEstado();

    			logger.info("\n\n\n************************************************************");
    			logger.warn("El estado en INTUBACIONES HA es------->"+estado);
    			logger.info("************************************************************\n\n\n");

    			if(estado == null)
    			{
    				forma.reset(); 
    				logger.warn("Estado no valido dentro del flujo de INTUBACIONES HA (null) ");
    				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaError");
    			}
    			else if(estado.equals("empezar") || estado.equals("resumen"))
    			{
    				return this.accionEmpezar(forma, mapping, con, usuario, request);
    			}
    			else if(estado.equals("nuevo"))
    			{
    				return this.accionNuevo(forma, mapping, con, usuario);
    			}
    			else if(estado.equals("modificar"))
    			{
    				return this.accionModificar(forma, mapping, con, usuario);
    			}
    			else if(estado.equals("continuar"))
    			{
    				UtilidadBD.closeConnection(con);
    				return mapping.findForward("intubaciones");
    			}
    			else if(estado.equals("consultarIntubacionCompleta"))
    			{
    				accionConsultarIntubacionCompleta(con,request,forma,usuario);
    				UtilidadBD.closeConnection(con);
    				return mapping.findForward("resumenCompleto");
    			}
    			else if(estado.equals("cargarCormack"))
    			{
    				return this.accionCargarCormack(forma, mapping, con, usuario);
    			}
    			else if(estado.equals("guardar"))
    			{
    				return this.accionGuardar(forma, mapping, con, usuario, request);
    			}
    			else if(estado.equals("eliminar"))
    			{
    				return this.accionEliminar(forma, mapping, con, usuario, request);
    			}
    			else
    			{
    				forma.reset(); 
    				logger.warn("Estado no valido dentro del flujo de INTUBACIONES");
    				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaError");
    			}
    		}
    		return null;
    	} catch (Exception e) {
    		Log4JManager.error(e);
    		return null;
    	}
    	finally{
    		UtilidadBD.closeConnection(con);
    	}
    }

	/**
     * 
     * @param forma
     * @param mapping
     * @param con
     * @param usuario
     * @return
     */
	private ActionForward accionEmpezar(IntubacionesHAForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario, HttpServletRequest request) 
	{
		forma.reset();
		if(request != null && !forma.getEstado().equals("resumen"))
			inicializarParametrosRequest(forma,request);
		forma.setMapaListado(IntubacionesHA.obtenerListadoIntubaciones(con,forma.getNumeroSolicitud()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listadoIntubaciones");
	}
	
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @return
	 */
	private ActionForward accionNuevo(IntubacionesHAForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		HashMap<Object, Object> listado= (HashMap)forma.getMapaListado().clone();
		//forma.reset();
		forma.setMapaListado(listado);
		forma.setMapaTiposIntubacionMultiple(IntubacionesHA.cargarTiposIntubacionMultiple(con, forma.getCentroCosto(), usuario.getCodigoInstitucionInt()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("intubaciones");
	}
	
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @return
	 */
	private ActionForward accionModificar(IntubacionesHAForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		forma.setCodigoIntubacionHA(Integer.parseInt(forma.getMapaListado("codigo_intubacion_hoja_anes_"+forma.getIndex())+""));
		forma.setEstaBD(true);
		forma.setFecha(forma.getMapaListado("fecha_"+forma.getIndex())+"");
		forma.setHora(forma.getMapaListado("hora_"+forma.getIndex())+"");
		forma.setClasificacionCormack(Integer.parseInt(forma.getMapaListado("clas_cormack_lehane_"+forma.getIndex())+""));
		forma.setTipoIntubacion(Integer.parseInt(forma.getMapaListado("tipo_intubacion_"+forma.getIndex())+""));
		forma.setMapaCormack(IntubacionesHA.cargarCormack(con, forma.getCentroCosto(), usuario.getCodigoInstitucionInt(), forma.getTipoIntubacion(), forma.getCodigoIntubacionHA()));
		forma.setMapaTiposIntubacionMultiple(IntubacionesHA.cargarDetalleIntubacionHojaAnestesia(con, forma.getCodigoIntubacionHA(), forma.getCentroCosto(), usuario.getCodigoInstitucionInt()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("intubaciones");	
	}
	
	
	/**
	 * Accion consultar Intubaciones completa
	 * @param Connection con 
	 * @param HttpResquest request
	 * @param IntubacionesHAForm forma
	 * @param UsuarioBasico usuario
	 * */
	private void accionConsultarIntubacionCompleta(Connection con,HttpServletRequest request,IntubacionesHAForm forma,UsuarioBasico usuario)
	{
		forma.reset();
		
		if(request != null && !forma.getEstado().equals("resumen"))
			inicializarParametrosRequest(forma,request);
		
		forma.setMapaListado(IntubacionesHA.obtenerListadoIntubaciones(con,forma.getNumeroSolicitud()));		
		validacionMostrarInfo(forma,forma.getMapaListado());
				
		for(int i = 0; i<Integer.parseInt(forma.getMapaListado("numRegistros").toString()); i++)
			forma.getMapaListado().put("tipointubacionmul_"+i,IntubacionesHA.cargarDetalleIntubacionHojaAnestesia(con,Integer.parseInt(forma.getMapaListado("codigo_intubacion_hoja_anes_"+i).toString()),forma.getCentroCosto(),usuario.getCodigoInstitucionInt()));			
				
	}	
	
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardar(IntubacionesHAForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario, HttpServletRequest request) 
	{
		if(!forma.getEstaBD())
		{
			UtilidadBD.iniciarTransaccion(con);
			insertarEncabezadoDetalle(forma, mapping, con, usuario);
			//en este punto insertamos los anestesiologos
			HojaAnestesia hojaAnestesia= new HojaAnestesia();
			hojaAnestesia.insertarActualizarAnestesiologos(con, forma.getNumeroSolicitud(), usuario);
			UtilidadBD.finalizarTransaccion(con);
			forma.setEstado("resumen");
			return this.accionEmpezar(forma, mapping, con, usuario, request);
		}
		else
		{
			UtilidadBD.iniciarTransaccion(con);
			IntubacionesHA.eliminarDetalle(con, forma.getCodigoIntubacionHA(), ConstantesBD.codigoNuncaValido /*tipoIntubacionMultiple*/);
			IntubacionesHA.eliminar(con, forma.getCodigoIntubacionHA());
			insertarEncabezadoDetalle(forma, mapping, con, usuario);
			//en este punto insertamos los anestesiologos
			HojaAnestesia hojaAnestesia= new HojaAnestesia();
			hojaAnestesia.insertarActualizarAnestesiologos(con, forma.getNumeroSolicitud(), usuario);
			UtilidadBD.finalizarTransaccion(con);
			forma.setEstado("resumen");
			return this.accionEmpezar(forma, mapping, con, usuario, request);
		}
		
	}
	
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @return
	 */
	private void insertarEncabezadoDetalle(IntubacionesHAForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario)
	{
		HashMap<Object, Object> mapaEncabezado= new HashMap<Object, Object>();
		mapaEncabezado.put("numerosolicitud", forma.getNumeroSolicitud());
		
		mapaEncabezado.put("tipointubacion", forma.getTipoIntubacion());
		mapaEncabezado.put("clascormacklehane", forma.getClasificacionCormack());
		mapaEncabezado.put("fecha", forma.getFecha());
		mapaEncabezado.put("hora", forma.getHora());
		mapaEncabezado.put("institucion", usuario.getCodigoInstitucionInt());
		mapaEncabezado.put("usuariomodifica", usuario.getLoginUsuario());
		int codigoEncabezado=IntubacionesHA.insertar(con, mapaEncabezado);
		
		for(int w=0; w<Integer.parseInt(forma.getMapaTiposIntubacionMultiple("numRegistros")+"");w++)
		{
			HashMap<Object, Object> mapaDetalle= new HashMap<Object, Object>();
			
			if(UtilidadTexto.getBoolean(forma.getMapaTiposIntubacionMultiple("checkeado_"+w)+""))
			{	
				mapaDetalle.put("intubacionhojaanes", codigoEncabezado);
				mapaDetalle.put("tipointubacionmultiple", forma.getMapaTiposIntubacionMultiple("codigo_"+w));
				mapaDetalle.put("usuariomodifica", usuario.getLoginUsuario());
				IntubacionesHA.insertarDetalle(con, mapaDetalle);
			}	
    	}
	}
	
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEliminar(IntubacionesHAForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario, HttpServletRequest request) 
	{
		forma.setCodigoIntubacionHA(Integer.parseInt(forma.getMapaListado("codigo_intubacion_hoja_anes_"+forma.getIndex())+""));
		UtilidadBD.iniciarTransaccion(con);
		IntubacionesHA.eliminarDetalle(con, forma.getCodigoIntubacionHA(), ConstantesBD.codigoNuncaValido /*tipoIntubacionMultiple*/);
		IntubacionesHA.eliminar(con, forma.getCodigoIntubacionHA());
		//en este punto insertamos los anestesiologos
		HojaAnestesia hojaAnestesia= new HojaAnestesia();
		hojaAnestesia.insertarActualizarAnestesiologos(con, forma.getNumeroSolicitud(), usuario);
		UtilidadBD.finalizarTransaccion(con);
		forma.setEstado("resumen");
		return this.accionEmpezar(forma, mapping, con, usuario, request);
	}
	
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @return
	 */
	private ActionForward accionCargarCormack(IntubacionesHAForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		forma.setMapaCormack(IntubacionesHA.cargarCormack(con, forma.getCentroCosto(), usuario.getCodigoInstitucionInt(), forma.getTipoIntubacion(), forma.getCodigoIntubacionHA()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("intubaciones");
	}
	
	
	/**
	 * Validacione para actualizar el indicador de mostrarInformacion
	 * @param InfoGeneralHAForm forma 
	 * */
	private void validacionMostrarInfo(IntubacionesHAForm forma,HashMap mapa)
	{
		//Utilidades.imprimirMapa(forma.getMapa());
		
		//Validacion para mostrar informacion 
		if((mapa.isEmpty() || (mapa.containsKey("numRegistros") && mapa.get("numRegistros").toString().equals("0")))  
				&& forma.getMostrarDatosInfoActivo())
		{
			logger.info("no hay información Intubaciones");
			forma.getMostrarDatosInfo().setNombre(ConstantesBD.acronimoNo);
		}
		else
		{			
			logger.info("no paso validaciones mostrar informacion (Intubacion). mostrar datos info  >>"+forma.getMostrarDatosInfoActivo());
			forma.getMostrarDatosInfo().setNombre(ConstantesBD.acronimoSi);		
		}		
	}
	
	/**
	 * Inicializa los datos pasados por parametros a la funcionalidad
	 * @param IntubacionesHAForm forma
	 * @param HttpServletRequest request
	 * */
	private void inicializarParametrosRequest(IntubacionesHAForm forma,HttpServletRequest request)
	{
		if (!(request.getParameter("esSinEncabezado")+"").equals("") && !(request.getParameter("esSinEncabezado")+"").equals("null"))
			forma.setEsSinEncabezado(request.getParameter("esSinEncabezado")+"");
		else
			forma.setEsSinEncabezado(ConstantesBD.acronimoNo);
		
		if (!(request.getParameter("mostrarDatosInfoActivo")+"").equals("") && !(request.getParameter("mostrarDatosInfoActivo")+"").equals("null"))
			forma.setMostrarDatosInfoActivo(UtilidadTexto.getBoolean(request.getParameter("mostrarDatosInfoActivo")+""));
		else
			forma.setMostrarDatosInfoActivo(false);
		
		if (!(request.getParameter("ocultarMenu")+"").equals("") && !(request.getParameter("ocultarMenu")+"").equals("null"))
			forma.setOcultarMenu(UtilidadTexto.getBoolean(request.getParameter("ocultarMenu")+""));
		else
			forma.setOcultarMenu(false);	
	}	
}