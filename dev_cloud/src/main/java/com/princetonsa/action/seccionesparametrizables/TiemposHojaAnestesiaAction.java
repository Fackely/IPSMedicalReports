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

import com.princetonsa.actionform.seccionesparametrizables.TiemposHojaAnestesiaForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.parametrizacion.Eventos;
import com.princetonsa.mundo.parametrizacion.TiemposHojaAnestesia;
import com.princetonsa.mundo.salasCirugia.HojaAnestesia;

/**
 * 
 * @author wilson
 *
 */
public class TiemposHojaAnestesiaAction extends Action
{
	/**
     * Objeto para manejar los logs de esta clase
    */
    private Logger logger = Logger.getLogger(TiemposHojaAnestesiaAction.class);
    
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
    	if(form instanceof TiemposHojaAnestesiaForm)
    	{
    		con = UtilidadBD.abrirConexion();
    		if(con == null)
			{
				request.setAttribute("codigoDescripcionError", "errors.problemasBd");
				return mapping.findForward("paginaError");
			}
    		
    		UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
    		TiemposHojaAnestesiaForm forma =(TiemposHojaAnestesiaForm)form;
    		String estado=forma.getEstado();
    		
    		logger.info("\n\n\n************************************************************");
    		logger.warn("El estado en TIEMPOS es------->"+estado);
    		logger.info("************************************************************\n\n\n");
    		
    		if(estado == null)
    		{
    			forma.reset(); 
    			logger.warn("Estado no valido dentro del flujo de TIEMPOS (null) ");
    			request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
    			UtilidadBD.cerrarConexion(con);
    			return mapping.findForward("paginaError");
    		}
    		else if(estado.equals("empezarTiempos") || estado.equals("resumen"))
    		{
    			return this.accionEmpezarTiempos(forma,request, mapping, con, usuario);
    		}
    		else if(estado.equals("guardar"))
    		{
    			return this.accionGuardarTiempos(forma, mapping, con, usuario);
    		}
    		else
    		{
    			forma.reset(); 
    			logger.warn("Estado no valido dentro del flujo de TIEMPOS");
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
	private ActionForward accionEmpezarTiempos(TiemposHojaAnestesiaForm forma,HttpServletRequest request, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		forma.reset();
		
		if(request != null && !forma.getEstado().equals("resumen"))
			inicializarParametrosRequest(forma,request);
		
		forma.setMapa(TiemposHojaAnestesia.cargarTiempos(con, forma.getNumeroSolicitud(), forma.getCentroCosto(), usuario.getCodigoInstitucionInt()));
		validacionMostrarInfo(forma,forma.getMapa());
		forma.setMapaEventosMin(Eventos.consultaEventosMinutos(con, forma.getNumeroSolicitud()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("tiempos");
	}
	
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardarTiempos(TiemposHojaAnestesiaForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		UtilidadBD.iniciarTransaccion(con);
		HashMap<Object, Object> mapaTiempo= new HashMap<Object, Object>();
		
		for(int w=0; w<Integer.parseInt(forma.getMapa("numRegistros")+""); w++)
		{
			mapaTiempo= llenarMapaInsercion(forma, usuario, w);
			if(!UtilidadTexto.getBoolean(forma.getMapa("estabd_"+w)+""))
				TiemposHojaAnestesia.insertar(con, mapaTiempo);
			else
				TiemposHojaAnestesia.modificar(con, mapaTiempo);
		}
		
		//en este punto insertamos los anestesiologos
		HojaAnestesia hojaAnestesia= new HojaAnestesia();
		hojaAnestesia.insertarActualizarAnestesiologos(con, forma.getNumeroSolicitud(), usuario);
		
		UtilidadBD.finalizarTransaccion(con);
		forma.setEstado("resumen");
		
		return this.accionEmpezarTiempos(forma,null, mapping, con, usuario);
	}

	/**
	 * 
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private HashMap<Object, Object> llenarMapaInsercion(TiemposHojaAnestesiaForm forma, UsuarioBasico usuario, int indice) 
	{
		HashMap<Object, Object> mapaTiempo= new HashMap<Object, Object>();
		mapaTiempo.put("numero_solicitud", forma.getNumeroSolicitud());
		mapaTiempo.put("codigo_tiempo_inst_cc", forma.getMapa("codigo_tiempo_inst_cc_"+indice));
		mapaTiempo.put("minutos", forma.getMapa("minutos_"+indice));
		mapaTiempo.put("segundos", forma.getMapa("segundos_"+indice));
		mapaTiempo.put("loginusuario", usuario.getLoginUsuario());
		mapaTiempo.put("codigotiempohojaanestesia", forma.getMapa("codigotiempohojaanestesia_"+indice));
		
		return mapaTiempo;
	}
	
	
	/**
	 * Inicializa los datos pasados por parametros a la funcionalidad
	 * @param TiemposHojaAnestesiaForm forma
	 * @param HttpServletRequest request
	 * */
	private void inicializarParametrosRequest(TiemposHojaAnestesiaForm forma,HttpServletRequest request)
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
	
	
	/**
	 * Validacione para actualizar el indicador de mostrarInformacion
	 * @param TiemposHojaAnestesiaForm forma 
	 * */
	private void validacionMostrarInfo(TiemposHojaAnestesiaForm forma,HashMap mapa)
	{
		//Utilidades.imprimirMapa(forma.getMapa());
		
		//Validacion para mostrar informacion 
		if((mapa.isEmpty() || (mapa.containsKey("numRegistros") && mapa.get("numRegistros").toString().equals("0")))  
				&& forma.getMostrarDatosInfoActivo())
		{
			logger.info("no hay información Tiempos");
			forma.getMostrarDatosInfo().setNombre(ConstantesBD.acronimoNo);
		}
		else
		{
			//valida que los tiempo tengan datos
			if(UtilidadTexto.isEmpty(forma.getMapa("minutos_0")+"") && 
					UtilidadTexto.isEmpty(forma.getMapa("segundos_0")+""))
			{
				logger.info("no hay información Tiempos");
				forma.getMostrarDatosInfo().setNombre(ConstantesBD.acronimoNo);
				return;
			}			
			
			logger.info("no paso validaciones mostrar informacion. Tiempos. mostrar datos info  >>"+forma.getMostrarDatosInfoActivo());
			forma.getMostrarDatosInfo().setNombre(ConstantesBD.acronimoSi);
			return;	
		}		
	}
}