package com.princetonsa.action.inventarios;

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
import util.Listado;
import util.LogsAxioma;
import util.UtilidadBD;
import util.inventarios.UtilidadInventarios;

import com.princetonsa.actionform.inventarios.ArticulosPorAlmacenForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.ArticulosPorAlmacen;
import com.princetonsa.mundo.inventarios.Secciones;

/**
 * Clase para el manejo del workflow de la ubicacion de articulos por almacen
 * Date: 2008-01-22
 * @author garias@princetonsa.com
 */
public class ArticulosPorAlmacenAction extends Action {
	
	/**
	 * logger 
	 * */
	Logger logger = Logger.getLogger(ArticulosPorAlmacenAction.class);
	
	/**
	 * Metodo excute del Action
	 */
    public ActionForward execute(   ActionMapping mapping,
            						ActionForm form,
            						HttpServletRequest request,
            						HttpServletResponse response ) throws Exception
            						{

    	Connection con = null;

    	try{


    		if(response==null);
    		if(form instanceof ArticulosPorAlmacenForm)
    		{

    			con = UtilidadBD.abrirConexion();

    			if(con == null)
    			{	
    				request.setAttribute("CodigoDescripcionError","errors.problemasBd");
    				return mapping.findForward("paginaError");
    			}
    			ArticulosPorAlmacenForm forma = (ArticulosPorAlmacenForm)form;
    			String estado = forma.getEstado();

    			logger.info("\n\n ESTADO SECCIONES ---->"+estado+"\n\n");

    			UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");


    			if(estado == null)
    			{
    				forma.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());
    				logger.warn("Estado no valido dentro del Flujo de Unidad de Procedimiento (null)");
    				request.setAttribute("CodigoDescripcionError","Errors.estadoInvalido");
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaError");
    			}
    			else if(estado.equals("empezar"))
    			{    			
    				return this.accionEmpezar(forma, con, mapping, usuario);    			
    			}
    			else if(estado.equals("cargarSecciones"))
    			{
    				return this.accionCargarSecciones(forma,con,mapping, usuario);
    			}
    			else if(estado.equals("cargarSubsecciones"))
    			{
    				return this.accionCargarSubsecciones(forma,con,mapping, usuario);
    			}
    			else if(estado.equals("cargarArticulos"))
    			{
    				return this.accionCargarArticulos(forma,con,mapping, usuario);
    			}
    			else if(estado.equals("nuevoRegistro"))
    			{
    				return this.accionNuevoRegistro(forma, con, mapping, usuario);
    			}
    			else if(estado.equals("guardarNuevo"))
    			{
    				return this.guardarNuevo(forma, con, mapping, usuario, request);
    			}
    			else if(estado.equals("modificarRegistro"))
    			{
    				return this.modificarRegistro(forma, con, mapping, usuario);
    			}
    			else if(estado.equals("modificarRegistroDet"))
    			{
    				return this.modificarRegistroDet(forma, con, mapping, usuario, request);
    			}
    			else if(estado.equals("eliminarRegistroDet"))
    			{
    				return this.eliminarRegistroDet(forma, con, mapping, usuario);
    			}
    			else if(estado.equals("mostrarBusquedaAvanzada"))
    			{
    				return this.accionMostrarBusquedaAvanzada(forma, con, mapping, usuario);
    			}
    			else if(estado.equals("cambiarCentroAtencion"))
    			{
    				return this.accionCambiarCentroAtencion(forma, con, mapping, usuario);
    			}
    			else if(estado.equals("mostrarPopupBusquedaAvanzada"))
    			{
    				return this.accionMostrarPopupBusquedaAvanzada(forma, con, mapping, usuario);
    			}
    			else
    				/*------------------------------
    				 * 		ESTADO ==> ORDENAR
				 -------------------------------*/
    				if (estado.equals("ordenar"))
    				{
    					accionOrdenarMapa(forma);
    					UtilidadBD.closeConnection(con);
    					return mapping.findForward("articulosPorAlmacen");
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
    
    
	private ActionForward accionMostrarPopupBusquedaAvanzada(ArticulosPorAlmacenForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) {
		ArticulosPorAlmacen a = new ArticulosPorAlmacen();
    	a.setInstitucion(usuario.getCodigoInstitucionInt());
    	a.setCentroAtencion(forma.getCentroAtencion());
    	a.setAlmacen(forma.getAlmacen());
    	a.setSeccion(forma.getSeccion());
    	a.setSubseccion(forma.getSubseccion());
    	a.setCodigoArticulo(forma.getCodigoArticuloBusqueda());
    	forma.setArticulosBusquedaMap(ArticulosPorAlmacen.consultarUbicaciones(con, a));
    	forma.setMostrarPopupBusquedaAvanzada(ConstantesBD.acronimoNo);
    	forma.setEstado("mostrarBusquedaAvanzada");
		UtilidadBD.closeConnection(con);
		return mapping.findForward("popupUbicacion");
	}


	private ActionForward accionCambiarCentroAtencion(ArticulosPorAlmacenForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) {
		forma.setSeccion(ConstantesBD.codigoNuncaValido+"");
		forma.setSubseccion(ConstantesBD.codigoNuncaValido);
		forma.setMostrarBusquedaAvanzada(ConstantesBD.acronimoNo);
		HashMap aux = new HashMap();
		aux.put("numRegistros",0);
		forma.setArticulosMap(aux);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("articulosPorAlmacen");
	}


	private ActionForward accionMostrarBusquedaAvanzada(ArticulosPorAlmacenForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) {
		forma.setMostrarBusquedaAvanzada(ConstantesBD.acronimoSi);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("articulosPorAlmacen");
	}


	/**
	 * metodo encargado del ordenamiento de la forma
	 * @param forma
	 */
	public static void accionOrdenarMapa(ArticulosPorAlmacenForm forma)
	{
		String[] indices = (String[])forma.getArticulosMap("INDICES_MAPA");
		int numReg = Integer.parseInt(forma.getArticulosMap("numRegistros")+"");
		forma.setArticulosMap(Listado.ordenarMapa(indices, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getArticulosMap(), numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setArticulosMap("numRegistros",numReg+"");
		forma.setArticulosMap("INDICES_MAPA",indices);
	}
    
    
    private ActionForward modificarRegistro(ArticulosPorAlmacenForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
    {
    	int pos = Integer.parseInt(forma.getIndexMap());
    	
    	forma.setDescripcionArticulo(forma.getArticulosMap("articulodesc_"+pos).toString());
    	forma.setUbicacion(forma.getArticulosMap("ubicacion_"+pos).toString());
    	forma.setCodigoDetArt(Integer.parseInt(forma.getArticulosMap("codigo_det_pk_"+pos).toString()));
    	
    	logger.info("descripcionArticulo=> "+forma.getDescripcionArticulo());
    	logger.info("ubicacion=> "+forma.getUbicacion());
    	logger.info("codDetArticulo=> "+forma.getCodigoDetArt());
    	logger.info("ndexMap=> "+forma.getIndexMap());
    	UtilidadBD.closeConnection(con);
    	return mapping.findForward("articulosPorAlmacen");
	}

	private ActionForward eliminarRegistroDet(ArticulosPorAlmacenForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) {
		
        int pos = Integer.parseInt(forma.getIndexMap());
        
 
    	forma.setCodigoDetArt(Integer.parseInt(forma.getArticulosMap("codigo_det_pk_"+pos).toString()));
    	
    	logger.info("codigoDETArticulo=> "+forma.getCodigoDetArt());
    	
    	ArticulosPorAlmacen a = new ArticulosPorAlmacen();
    	
    	a.setInstitucion(usuario.getCodigoInstitucionInt());
    	a.setCentroAtencion(forma.getCentroAtencion());
    	a.setAlmacen(forma.getAlmacen());
    	a.setSeccion(forma.getSeccion());
    	a.setSubseccion(forma.getSubseccion());
    	a.setCodigoDetArt(forma.getCodigoDetArt());
    	a.setUsuarioModifica(usuario.getLoginUsuario());
    	
    	if(!a.eliminarRegistroDet(con, a))
    	{  	
    		/*actualiza la informacion de la ubicacion por la actual
    		forma.getArticulosMap().put("ubicacion_"+forma.getIndexMap(),forma.getUbicacion());
    		*/
    		//Se genera el Log de eliminacion
    		generarlog(forma, usuario,true,forma.getIndexMap());
    		//forma.getArticulosMap().put("ubicacionold_"+forma.getIndexMap(),forma.getUbicacion());
    		
    		accionCargarArticulos(forma, con, mapping, usuario);
    		forma.setEstado("operacionExitosa");
    			
    	}
    	
    	UtilidadBD.closeConnection(con);
    	return mapping.findForward("articulosPorAlmacen");
	}

	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
	 * @param request 
     * @return
     */
    private ActionForward modificarRegistroDet(ArticulosPorAlmacenForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
    {
    	/* Ocultado por tarea [id=37216]
    	if(forma.getUbicacion().equals(""))
    	{
    		return ComunAction.accionSalirCasoError(mapping,request,con, logger, "", "No se ingreso la Ubicacion del Articulo. Favor Verificar", false);
    	}
    	*/
    	
    	ArticulosPorAlmacen a = new ArticulosPorAlmacen();
    	a.setInstitucion(usuario.getCodigoInstitucionInt());
    	a.setCentroAtencion(forma.getCentroAtencion());
    	a.setAlmacen(forma.getAlmacen());
    	a.setSeccion(forma.getSeccion());
    	a.setSubseccion(forma.getSubseccion());
    	a.setUbicacion(forma.getUbicacion());
    	a.setCodigoDetArt(forma.getCodigoDetArt());
    	a.setUsuarioModifica(usuario.getLoginUsuario());   	   	
    	    	  	
    	
    	if(evaluarCambioMapa(forma))
    	{
    		if(ArticulosPorAlmacen.modificarRegistroDet(con, a))
    		{  	
    			forma.getArticulosMap().put("ubicacionold_"+forma.getIndexMap(),forma.getArticulosMap("ubicacion_"+forma.getIndexMap()));
    			
    			//actualiza la informacion de la ubicacion por la actual
    			forma.getArticulosMap().put("ubicacion_"+forma.getIndexMap(),forma.getUbicacion());
    			//Se genera el Log de Modificacion
    			generarlog(forma, usuario,false,forma.getIndexMap());
    			
    			forma.setEstado("operacionExitosa");
    		}
    	}
    	
    	UtilidadBD.closeConnection(con);
    	forma.setDescripcionArticulo("");
    	forma.setUbicacion("");
    	return mapping.findForward("articulosPorAlmacen");
	}
    
    
    /**
     * @param 
     * */
    private boolean evaluarCambioMapa(ArticulosPorAlmacenForm forma)
    {    	
    	String pos = forma.getIndexMap();
    	//logger.info(forma.getIndexMap()+" >> "+forma.getUbicacion()+" >> "+forma.getArticulosMap().get("ubicacionold_"+pos).toString());
    	if(!forma.getUbicacion().equals(forma.getArticulosMap().get("ubicacionold_"+pos).toString()))
    		return true;    
    	
    	return false;
    }

	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
	 * @param request 
     * @return
     */
    private ActionForward guardarNuevo(ArticulosPorAlmacenForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
    {
    	/* Ocultado por tarea [id=37216]
    	if(forma.getUbicacion().equals(""))
    	{
    		return ComunAction.accionSalirCasoError(mapping,request,con, logger, "", "No se ingreso la Ubicacion del Articulo. Favor Verificar", false);
    	}*/
    	
    	ArticulosPorAlmacen a = new ArticulosPorAlmacen();
    	a.setInstitucion(usuario.getCodigoInstitucionInt());
    	a.setCentroAtencion(forma.getCentroAtencion());
    	a.setAlmacen(forma.getAlmacen());
    	a.setSeccion(forma.getSeccion());
    	a.setSubseccion(forma.getSubseccion());
    	a.setCodigoArticulo(forma.getCodigoArticulo());
    	a.setDescripcionArticulo(forma.getDescripcionArticulo());
    	a.setUsuarioModifica(usuario.getLoginUsuario());
    	a.setUbicacion(forma.getUbicacion());
    	if(ArticulosPorAlmacen.guardarNuevo(con, a))
    		forma.setEstado("operacionExitosa");
    	forma.setArticulosMap(ArticulosPorAlmacen.consultar(con, a));
    	
    	/**MT 2353 Se verifica que el articulo contenga exsitencias para el almacen sino
    	 * se crea registro con valores iniciales.*/
    	UtilidadInventarios.actualizarExistenciasArticuloAlmacenLote(con, forma.getCodigoArticulo(), forma.getAlmacen(),
    			true, 0, usuario.getCodigoInstitucionInt(), "", "");
    	/***/
    	
    	UtilidadBD.closeConnection(con);
    	forma.setDescripcionArticulo("");
    	forma.setUbicacion("");
    	
    	return mapping.findForward("articulosPorAlmacen");
	}

	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @return
     */
    private ActionForward accionNuevoRegistro(ArticulosPorAlmacenForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
    {
    	forma.setUbicacion("");
    	UtilidadBD.closeConnection(con);
    	return mapping.findForward("articulosPorAlmacen");
	}

	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @return
     */
    private ActionForward accionCargarArticulos(ArticulosPorAlmacenForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) {
    	ArticulosPorAlmacen a = new ArticulosPorAlmacen();
    	a.setInstitucion(usuario.getCodigoInstitucionInt());
    	a.setCentroAtencion(forma.getCentroAtencion());
    	a.setAlmacen(forma.getAlmacen());
    	a.setSeccion(forma.getSeccion());
    	a.setSubseccion(forma.getSubseccion());
    	forma.setArticulosMap(ArticulosPorAlmacen.consultar(con, a));
    	UtilidadBD.closeConnection(con);
    	return mapping.findForward("articulosPorAlmacen");	
    }

	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @return
     */
    private ActionForward accionCargarSubsecciones(ArticulosPorAlmacenForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) {
		logger.info(" Almacen -> "+forma.getAlmacen()+" CA -> "+forma.getCentroAtencion()+" Seccion -> "+forma.getSeccion());
		Secciones seccion= new Secciones();
		seccion.setAlmacen(forma.getAlmacen());
		seccion.setCentroAtencion(forma.getCentroAtencion());
		seccion.setCodigoPk(Integer.parseInt(forma.getSeccion()));
		seccion.setInstitucion(usuario.getCodigoInstitucionInt());
		
		HashMap articulos=new HashMap();
		articulos.put("numRegistros", "0");
		forma.setArticulosMap(articulos);		
		
		forma.setSubseccionesMap(Secciones.consultarSubsecciones(con, seccion.getCodigoPk()));	
		UtilidadBD.closeConnection(con);		
		return mapping.findForward("articulosPorAlmacen");	
	}

	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @return
     */
	private ActionForward accionCargarSecciones(ArticulosPorAlmacenForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		logger.info(" Almacen -> "+forma.getAlmacen()+" CA -> "+forma.getCentroAtencion());
		Secciones seccion= new Secciones();
		seccion.setAlmacen(forma.getAlmacen());
		seccion.setCentroAtencion(forma.getCentroAtencion());
		seccion.setInstitucion(usuario.getCodigoInstitucionInt());
		
		HashMap subsecciones=new HashMap();
		subsecciones.put("numRegistros", "0");
		forma.setSubseccionesMap(subsecciones);
		
		forma.setSeccionesMap(Secciones.consultar(con, seccion));
		forma.setMostrarBusquedaAvanzada(ConstantesBD.acronimoNo);
		UtilidadBD.closeConnection(con);		
		return mapping.findForward("articulosPorAlmacen");	
	}

	/**
     * @param forma
     * @param con
     * @param mapping
     * @return
     */
	private ActionForward accionEmpezar(ArticulosPorAlmacenForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		forma.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());	
		UtilidadBD.closeConnection(con);
		return mapping.findForward("articulosPorAlmacen");		
	}	
	
	
	
	/**
	 * General el Documento Log 
	 * @param forma
	 * @param HashMap temp
	 * @param UsuarioBasico usuario
	 * @param isEliminacion
	 * @param pos
	 * */
	private void generarlog(ArticulosPorAlmacenForm forma, UsuarioBasico usuario, boolean isEliminacion ,String pos )
	{
		String log = "";
		int tipoLog=0;		  
		
		if(isEliminacion)
		{
			log = "\n================== REGISTRO ELIMINADO =================="+
			"\nCentro de Atencion :"+forma.getCentroAtencion()+" " +
			"\nAlmacen : "+forma.getAlmacen()+" " +
			"\nSeccion: "+forma.getSeccion()+" " +
			"\nSubseccion: "+forma.getSubseccion()+" " +
			"\nCodigo Articulo: "+forma.getArticulosMap().get("cod_"+pos).toString()+" " +
			"\nCodigo Interfaz: "+forma.getArticulosMap().get("codinterf_"+pos).toString()+" " +
			"\nNombre Articulo: "+forma.getArticulosMap().get("articulo_"+pos).toString()+" " +
			"\nU. Medida: "+forma.getArticulosMap().get("umedida_"+pos).toString()+" " +
			"\nUbicacion:"+forma.getArticulosMap().get("ubicacion_"+pos).toString();
			log+= "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogEliminacion;
		}
		else
		{
			log =  "\n================= INFORMACION ORIGINAL ================="+			
			"\nCentro de Atencion :"+forma.getCentroAtencion()+" " +
			"\nAlmacen : "+forma.getAlmacen()+" " +
			"\nSeccion: "+forma.getSeccion()+" " +
			"\nSubseccion: "+forma.getSubseccion()+" " +
			"\nCodigo Articulo: "+forma.getArticulosMap().get("cod_"+pos).toString()+" " +
			"\nCodigo Interfaz: "+forma.getArticulosMap().get("codinterf_"+pos).toString()+" " +
			"\nNombre Articulo: "+forma.getArticulosMap().get("articulodesc_"+pos).toString()+" " +
			"\nU. Medida: "+forma.getArticulosMap().get("umedida_"+pos).toString()+" " +
			"\nUbicacion:"+forma.getArticulosMap().get("ubicacionold_"+pos).toString();
			
			log += 	"\n================= REGISTRO MODIFICADO =================="+
			"\nCentro de Atencion :"+forma.getCentroAtencion()+" " +
			"\nAlmacen : "+forma.getAlmacen()+" " +
			"\nSeccion: "+forma.getSeccion()+" " +
			"\nSubseccion: "+forma.getSubseccion()+" " +
			"\nCodigo Articulo: "+forma.getArticulosMap().get("cod_"+pos).toString()+" " +
			"\nCodigo Interfaz: "+forma.getArticulosMap().get("codinterf_"+pos).toString()+" " +
			"\nNombre Articulo: "+forma.getArticulosMap().get("articulodesc_"+pos).toString()+" " +
			"\nU. Medida: "+forma.getArticulosMap().get("umedida_"+pos).toString()+" " +
			"\nUbicacion:"+forma.getArticulosMap().get("ubicacion_"+pos).toString();
			log+= "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogModificacion;
		}
		logger.info("genero log");
		LogsAxioma.enviarLog(ConstantesBD.logArticulosPorAlmacenCodigo,log,tipoLog,usuario.getLoginUsuario());	
	}	
}