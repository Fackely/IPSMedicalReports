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

import com.princetonsa.actionform.seccionesparametrizables.PosicionesAnestesiaForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.parametrizacion.PosicionesAnestesia;
import com.princetonsa.mundo.salasCirugia.HojaAnestesia;

/**
 * 
 * @author wilson
 *
 */
public class PosicionesAnestesiaAction extends Action 
{
	/**
     * Objeto para manejar los logs de esta clase
    */
    private Logger logger = Logger.getLogger(PosicionesAnestesiaAction.class);
    
    /**
	 * Metodo excute del Action
	 */
    public ActionForward execute(   ActionMapping mapping,
            						ActionForm form,
            						HttpServletRequest request,
            						HttpServletResponse response ) throws Exception
            						{
    	Connection con=null;
    	try {
    		if (response==null); 
    		if(form instanceof PosicionesAnestesiaForm)
    		{
    			con = UtilidadBD.abrirConexion();
    			if(con == null)
    			{
    				request.setAttribute("codigoDescripcionError", "errors.problemasBd");
    				return mapping.findForward("paginaError");
    			}

    			UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
    			PosicionesAnestesiaForm forma =(PosicionesAnestesiaForm)form;
    			String estado=forma.getEstado();

    			logger.info("\n\n\n************************************************************");
    			logger.warn("El estado en POSICIONES es------->"+estado);
    			logger.info("************************************************************\n\n\n");

    			if(estado == null)
    			{
    				forma.reset(); 
    				logger.warn("Estado no valido dentro del flujo de POSICIONES (null) ");
    				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaError");
    			}
    			else if(estado.equals("empezarPosiciones") || estado.equals("resumen"))
    			{
    				return this.accionEmpezar(forma,request, mapping, con, usuario);
    			}
    			else if(estado.equals("guardar"))
    			{
    				return this.accionGuardar(forma, mapping, con, usuario);
    			}
    			else if(estado.equals("nuevo"))
    			{
    				return this.accionNuevo(forma, mapping, con, usuario);
    			}
    			else if(estado.equals("modificar"))
    			{
    				return this.accionModificar(forma, mapping, con, usuario);
    			}
    			else if(estado.equals("eliminar"))
    			{
    				return this.accionEliminar(forma, mapping, con, usuario);
    			}
    			else
    			{
    				forma.reset(); 
    				logger.warn("Estado no valido dentro del flujo de POSICIONES");
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
	private ActionForward accionEmpezar(PosicionesAnestesiaForm forma,HttpServletRequest request, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		forma.reset();
		
		if(request != null && !forma.getEstado().equals("resumen"))
			inicializarParametrosRequest(forma,request);
		
		forma.setMapa(PosicionesAnestesia.cargarPosiciones(con, forma.getNumeroSolicitud()));
		forma.setMapaTag(PosicionesAnestesia.cargarTagMapPosiciones(con, forma.getNumeroSolicitud(), forma.getCentroCosto(), usuario.getCodigoInstitucionInt()));
		
		//Validacion para mostrar informacion 
		if((forma.getMapa().isEmpty() || (forma.getMapa().containsKey("numRegistros") && forma.getMapa().get("numRegistros").toString().equals("0")))  
				&& forma.getMostrarDatosInfoActivo())
		{
			logger.info("no hay informacion posiciones anestesia ");
			forma.getMostrarDatosInfo().setNombre(ConstantesBD.acronimoNo);
		}
		else
		{
			logger.info("no paso validaciones mostrar informacion. mostrar datos info  >>"+forma.getMostrarDatosInfoActivo());
			forma.getMostrarDatosInfo().setNombre(ConstantesBD.acronimoSi);
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("posiciones");
	}
    
	/**
     * 
     * @param forma
     * @param mapping
     * @param con
     * @param usuario
     * @return
     */
    private ActionForward accionGuardar(PosicionesAnestesiaForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
    {
    	UtilidadBD.iniciarTransaccion(con);
    	HashMap<Object, Object> mapaPos= llenarMapaInsercion(forma, usuario, con);
		logger.info("\n ESTA EN BD-->"+forma.getEstaBD()+"\n");
		
		if(!forma.getEstaBD())
			PosicionesAnestesia.insertar(con, mapaPos);
		else
			PosicionesAnestesia.modificarPosicion(con, mapaPos);
		
		//en este punto insertamos los anestesiologos
		HojaAnestesia hojaAnestesia= new HojaAnestesia();
		hojaAnestesia.insertarActualizarAnestesiologos(con, forma.getNumeroSolicitud(), usuario);
		
		UtilidadBD.finalizarTransaccion(con);
		forma.setEstado("resumen");
		return this.accionEmpezar(forma,null, mapping, con, usuario);
	}

    /**
     * 
     * @param forma
     * @param usuario
     * @return
     */
	private HashMap<Object, Object> llenarMapaInsercion(PosicionesAnestesiaForm forma, UsuarioBasico usuario, Connection con) 
	{
		HashMap<Object, Object> mapaEvento= new HashMap<Object, Object>();
		mapaEvento.put("numerosolicitud", forma.getNumeroSolicitud());
		mapaEvento.put("codigoposicioninstcc", PosicionesAnestesia.obtenerPosicionDadaPosicionInstCC(con, forma.getPosicionInstCC()));
		mapaEvento.put("codigoposicion", forma.getPosicionInstCC());
		mapaEvento.put("fechainicial", forma.getFechaInicialActualizada());
		mapaEvento.put("horainicial", forma.getHoraInicialActualizada());
		mapaEvento.put("loginusuario", usuario.getLoginUsuario());
		mapaEvento.put("codigoposicionhojaanestesia", forma.getMapa("codigoposicionhojaanestesia_"+forma.getIndex()));
		
		return mapaEvento;
	}
	
	
    
    /**
     * 
     * @param forma
     * @param mapping
     * @param con
     * @param usuario
     * @return
     */
    private ActionForward accionModificar(PosicionesAnestesiaForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
    {
    	llenarCamposModificar(forma, usuario);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("posiciones");
	}

    /**
	 * 
	 * @param forma
	 * @param usuario
	 */
	private void llenarCamposModificar(PosicionesAnestesiaForm forma, UsuarioBasico usuario) 
	{
		forma.setFechaInicialActualizada(forma.getMapa("fechainicial_"+forma.getIndex())+"");
		forma.setHoraInicialActualizada(forma.getMapa("horainicial_"+forma.getIndex())+"");
		forma.setPosicionInstCC( Integer.parseInt(forma.getMapa("codigo_posicion_inst_cc_"+forma.getIndex())+""));
		forma.setEstaBD(true);
	}
    
    
	/**
     * 
     * @param forma
     * @param mapping
     * @param con
     * @param usuario
     * @return
     */
    private ActionForward accionNuevo(PosicionesAnestesiaForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
    {
    	forma.resetNuevo();
		forma.setEstaBD(false);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("posiciones");
	}

	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @return
	 */
    private ActionForward accionEliminar(PosicionesAnestesiaForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
    {
    	UtilidadBD.iniciarTransaccion(con);
    	PosicionesAnestesia.eliminar(con, Integer.parseInt(forma.getMapa("codigoposicionhojaanestesia_"+forma.getIndex())+""));
    	//en este punto insertamos los anestesiologos
		HojaAnestesia hojaAnestesia= new HojaAnestesia();
		hojaAnestesia.insertarActualizarAnestesiologos(con, forma.getNumeroSolicitud(), usuario);
		UtilidadBD.finalizarTransaccion(con);
		forma.setEstado("resumen");
		return this.accionEmpezar(forma,null, mapping, con, usuario);
	}
    
    /**
	 * Inicializa los datos pasados por parametros a la funcionalidad
	 * @param PosicionesAnestesiaForm forma
	 * @param HttpServletRequest request
	 * */
	private void inicializarParametrosRequest(PosicionesAnestesiaForm forma,HttpServletRequest request)
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