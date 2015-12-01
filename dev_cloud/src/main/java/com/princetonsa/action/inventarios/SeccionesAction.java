package com.princetonsa.action.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.Listado;
import util.UtilidadBD;
import util.Utilidades;

import com.princetonsa.actionform.inventarios.SeccionesForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.Secciones;

/**
 * Clase para el manejo del workflow de secciones almacen
 * Date: 2008-01-16
 * @author garias@princetonsa.com - lgchavez@princetonsa.com
 */
public class SeccionesAction extends Action 
{
	
	/**
	 * logger 
	 * */
	Logger logger = Logger.getLogger(SeccionesAction.class);
	
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
    		if(form instanceof SeccionesForm)
    		{

    			con = UtilidadBD.abrirConexion();

    			if(con == null)
    			{	
    				request.setAttribute("CodigoDescripcionError","errors.problemasBd");
    				return mapping.findForward("paginaError");
    			}
    			SeccionesForm forma = (SeccionesForm)form;
    			String estado = forma.getEstado();

    			logger.info("\n\n ESTADO SECCIONES---->"+estado+"\n\n");

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
    				return this.accionCargarSecciones(forma, con, mapping, usuario);
    			}
    			else if(estado.equals("nuevoRegistro"))
    			{
    				return this.accionNuevoRegistro(forma, con, mapping, usuario);
    			}
    			else if(estado.equals("modificarRegistro"))
    			{
    				return this.accionModificarRegistro(forma, con, mapping, usuario);
    			}
    			else if(estado.equals("guardarNuevo"))
    			{
    				return this.accionGuardarNuevo(forma, con, mapping, usuario);
    			}
    			else if(estado.equals("guardarModificacion"))
    			{
    				return this.accionGuardarModificacion(forma, con, mapping, usuario);
    			}
    			else if(estado.equals("eliminarRegistro"))
    			{
    				return this.accionEliminar(forma, con, mapping, usuario);
    			}
    			else if(estado.equals("detalleSubseccion"))
    			{
    				return this.accionCargarSubSecciones(forma, con, mapping, usuario);
    			}
    			else if(estado.equals("nuevoRegistroSubseccion"))
    			{
    				return this.accionNuevoRegistroSubseccion(forma, con, mapping, usuario);
    			}
    			else if(estado.equals("guardarNuevoSubseccion"))
    			{
    				return this.accionGuardarNuevoSubseccion(forma, con, mapping, usuario);
    			}
    			else if(estado.equals("guardarModificacionSubseccion"))
    			{
    				return this.accionGuardarModificacionSubseccion(forma, con, mapping, usuario);
    			}
    			else if(estado.equals("modificarRegistroSubseccion"))
    			{
    				return this.accionModificarRegistroSubseccion(forma, con, mapping, usuario);
    			}
    			else if(estado.equals("eliminarRegistroSubseccion"))
    			{
    				return this.accionEliminarSubseccion(forma, con, mapping, usuario);
    			}
    			else
    				/*------------------------------
    				 * 		ESTADO ==> ORDENAR
				 -------------------------------*/
    				if (estado.equals("ordenar"))
    				{
    					accionOrdenarMapa(forma);
    					UtilidadBD.closeConnection(con);
    					return mapping.findForward("secciones");
    				}
    				else
    					/*------------------------------
    					 * 		ESTADO ==> ORDENAR1
				 -------------------------------*/
    					if (estado.equals("ordenar1"))
    					{
    						accionOrdenarMapa1(forma);
    						UtilidadBD.closeConnection(con);
    						return mapping.findForward("subsecciones");
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
	 * metodo encargado del ordenamiento de la forma secciones
	 * @param forma
	 */
	public static void accionOrdenarMapa(SeccionesForm forma)
	{
		String[] indices = (String[])forma.getSeccionesMap("INDICES_MAPA");
		int numReg = Integer.parseInt(forma.getSeccionesMap("numRegistros")+"");
		forma.setSeccionesMap(Listado.ordenarMapa(indices, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getSeccionesMap(), numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setSeccionesMap("numRegistros",numReg+"");
		forma.setSeccionesMap("INDICES_MAPA",indices);		
		
	}
    
	
	

	/**
	 * metodo encargado del ordenamiento de la forma subsecciones
	 * @param forma
	 */
	public static void accionOrdenarMapa1(SeccionesForm forma)
	{
		String[] indices = (String[])forma.getSubseccionesMap("INDICES_MAPA");
		int numReg = Integer.parseInt(forma.getSubseccionesMap("numRegistros")+"");
		forma.setSubseccionesMap(Listado.ordenarMapa(indices, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getSubseccionesMap(), numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setSubseccionesMap("numRegistros",numReg+"");
		forma.setSubseccionesMap("INDICES_MAPA",indices);		
		
	}
	
    
    private ActionForward accionEliminarSubseccion(SeccionesForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) {
    		
    	
    	Secciones mundo1= new Secciones();
    	llenarMundo(forma, mundo1, usuario);

    	logger.info("MAPA ENTERO"+forma.getSubseccionesMap());
    	
    	int pos = ConstantesBD.codigoNuncaValido;
    	
    	if(forma.getCodigoPk()>0)
    	{	
    		    	
        	String[] indices = {"codigopkseccion_","descripcion_","codigo_subseccion_",""};
        	    	
        	
        	for(int i = 0; i< Integer.parseInt(forma.getSubseccionesMap().get("numRegistros").toString()) ; i++)
        		{
        		logger.info("COMPARACION DEL IF   "+forma.getSubseccionesMap().get("codigopkseccion_"+i)+"	==	"+(forma.getCodigoPk())+"	&&	"+forma.getSubseccionesMap().get("codigo_subseccion_"+i)+"	==		"+(forma.getCodigoSubseccion()));
        		if (forma.getSubseccionesMap().get("codigopkseccion_"+i).equals(forma.getCodigoPk()) &&
        			forma.getSubseccionesMap().get("codigo_subseccion_"+i).toString().equals(forma.getCodigoSubseccion()))
        			{
        			pos = i;
        			logger.info("POSICION:"+pos);
        			}
        		
        		}
        		    	
        					
        	logger.info("valor de la posicion eliminada >> "+pos+"MAPA ENTERO"+forma.getSubseccionesMap());
        	    	
        	if(Secciones.eliminarSubseccion(con, mundo1))
        	{
        		forma.setEstado("operacionExitosa");
        		Utilidades.generarLogGenerico(forma.getSubseccionesMap(), null, usuario.getLoginUsuario(), true,pos,ConstantesBD.logSeccionesCodigo, indices);
        	}
		
    		
//    		Secciones.eliminarSubseccion(con, mundo1);
    		
    		
    		mundo1.setCodigoSeccion(ConstantesBD.codigoNuncaValido);
        	mundo1.setDescripcionSeccion("");
        	forma.setSubseccionesMap(Secciones.consultarSubsecciones(con,mundo1.getCodigoPk()));
        	UtilidadBD.closeConnection(con);
        	return mapping.findForward("subsecciones");
    		
    	}
    	UtilidadBD.closeConnection(con);
    	return mapping.findForward("secciones");
	}



	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @return
     */
	private ActionForward accionGuardarModificacionSubseccion(SeccionesForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
    
		Secciones mundo= new Secciones();
    	HashMap SubseccionesN=new HashMap();
    	
    	logger.info("1codSub->"+forma.getCodigoSubseccion());
    	logger.info("1descSub->"+forma.getDescripcionSubseccion());

    	//Se consulta el registro original----------------------------------------------
		mundo.reset();
    	mundo.setCodigo_pk_seccion(forma.getCodigoPkSeccion());
  /*
    	SubseccionesN = Secciones.consultarSubsecciones(con, forma.getCodigoPkSeccion());
    	String[] indices1 = (String[])forma.getSubseccionesMap("INDICES_MAPA");
		int numReg = Integer.parseInt(SubseccionesN.get("numRegistros")+"");
		SubseccionesN= new HashMap(Listado.ordenarMapa(indices1, forma.getPatronOrdenar(), forma.getUltimoPatron(), SubseccionesN, numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		SubseccionesN.put("numRegistros",numReg+"");
		SubseccionesN.put("INDICES_MAPA",indices1);
    */
    	
    	SubseccionesN=(HashMap)forma.getSubseccionesMap().clone();
    	
    	//---------------------------------------------------------------------
   
    	llenarMundo(forma, mundo, usuario);
    	
    	logger.info("2codSub->"+forma.getCodigoSubseccion());
    	logger.info("2descSub->"+forma.getDescripcionSubseccion());
		
    	String[] indices = {"codigopkseccion_","codigo_subseccion_","descripcion_",""};
    	
    	int pos = ConstantesBD.codigoNuncaValido;
       	
    	for(int i = 0; i< Integer.parseInt(forma.getSubseccionesMap().get("numRegistros").toString()) ; i++)
    	{	
    		if(forma.getSubseccionesMap().get("codigopkseccion_"+i).equals(forma.getCodigoPk()) && 
    				forma.getSubseccionesMap().get("codigo_subseccion_"+i).toString().equals(forma.getCodigoSubseccion()))
    		{
    			pos = i;
    		}
    	}		
    
    	forma.setSubseccionesMap("codigopkseccion_"+pos,forma.getCodigoPkSeccion());
    	forma.setSubseccionesMap("codigo_subseccion_"+pos,forma.getCodigoSubseccion());
    	forma.setSubseccionesMap("descripcion_"+pos,forma.getDescripcionSubseccion());
    
    	logger.info("valor de la posicion modificada >> "+pos);
     	
    	if(Secciones.modificarSubseccion(con, mundo))
    	{
    		logger.info("\n\n\n\n ENTRO A GENERAR EL LOG \n\n\n\n");
    		
    		logger.info("\n\n\n\n MAPA MODIFICADO \n\n\n\n");
    		Utilidades.imprimirMapa(forma.getSubseccionesMap());
    		logger.info("\n\n\n\n MAPA ORIGINAL \n\n\n\n");
    		Utilidades.imprimirMapa(SubseccionesN);
    		Utilidades.generarLogGenerico(forma.getSubseccionesMap(), pos, SubseccionesN, usuario.getLoginUsuario(), false, pos, ConstantesBD.logSeccionesCodigo, indices);
    		forma.setEstado("operacionExitosa");
    	}
    	
    
    	
    	/**
    	SubseccionN.remove("codigopkseccion_"+pos);
    	SubseccionN.remove("codigo_subseccion_"+pos);
    	SubseccionN.remove("descripcion_"+pos);
    	
    	forma.setSubseccionesMap(new HashMap());
    	forma.setSubseccionesMap("numRegistros", 0);
    	forma.setSubseccionesMap(SubseccionN);
    	
    	logger.info("\n\n-------->NUEVO MAPA DESPUES -->"+SubseccionN);
    	logger.info("\n\n-------->SUBSECCIONES -->"+forma.getSubseccionesMap());
    	**/
   	
    	mundo.setCodigoSeccion(ConstantesBD.codigoNuncaValido);
    	
    	forma.setSubseccionesMap(Secciones.consultarSubsecciones(con,mundo.getCodigoPk()));
    	
    	mundo.setDescripcionSeccion("");
    	
    	forma.setSubseccionesMap(Secciones.consultarSubsecciones(con, forma.getCodigoPk()));
    	//forma.setSubseccionesMap(Secciones.consultarSubsecciones(con,mundo.getCodigoPk()));
    	UtilidadBD.closeConnection(con);
    	return mapping.findForward("subsecciones");
	}

	/**
	 * 
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
    private ActionForward accionModificarRegistroSubseccion(SeccionesForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) {
    
    	logger.info("forma.getcodigo_pk_seccion()->"+forma.getCodigoPkSeccion());
    	logger.info("forma.getCodigoSubseccion()->"+forma.getCodigoSubseccion());
    	logger.info("forma.getDescripcionSubseccion()->"+forma.getDescripcionSubseccion());
    	
    	forma.setCodigoSubseccionTemp(forma.getCodigoSubseccion());
    	
    	
    	UtilidadBD.closeConnection(con);
    	return mapping.findForward("subsecciones");
	}

	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @return
     */
    private ActionForward accionGuardarNuevoSubseccion(SeccionesForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) {
    	Secciones mundo= new Secciones();
		
    	logger.info("MAPA SUBSECCIONES -->"+forma.getSubseccionesMap());
    	
    	llenarMundo(forma, mundo, usuario);
 	
    	if(Secciones.insertarSubseccion(con, mundo));
    		forma.setEstado("operacionExitosa");
    	
    	mundo.setCodigoSeccion(ConstantesBD.codigoNuncaValido);
    	mundo.setDescripcionSeccion("");
    	forma.setSubseccionesMap(Secciones.consultarSubsecciones(con,mundo.getCodigoPk()));
    	UtilidadBD.closeConnection(con);
    	return mapping.findForward("subsecciones");
	}


	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @return
     */
    private ActionForward accionNuevoRegistroSubseccion(SeccionesForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) {
    	forma.setCodigoSubseccion("");
		forma.setDescripcionSubseccion("");
		UtilidadBD.closeConnection(con);
    	return mapping.findForward("subsecciones");
	}

	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @return
     */
    private ActionForward accionEliminar(SeccionesForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
    {
    	logger.info("forma.getCodigoPk()->"+forma.getCodigoPk());
    	logger.info("MAPA ENTERO"+forma.getSeccionesMap());
    	if(forma.getCodigoPk()>0)
    	{	
    		    	
        	String[] indices = {"codigo_pk_","codcentroatencion_","desccentroatencion_","codalmacen_","descalmacen_","codseccion_","descseccion_",""};
        
        	int pos = ConstantesBD.codigoNuncaValido;
        	        	
        	for(int i = 0; i< Integer.parseInt(forma.getSeccionesMap().get("numRegistros").toString()) ; i++)
        		if(forma.getSeccionesMap().get("codigo_pk_"+i).equals(forma.getCodigoPk()))
        			pos = i;
        					
        	logger.info("valor de la posicion eliminada >> "+pos+"MAPA ENTERO"+forma.getSeccionesMap());
        	    	
        	if(Secciones.eliminar(con, forma.getCodigoPk()))
        	{
        		forma.setEstado("operacionExitosa");
        		Utilidades.generarLogGenerico(forma.getSeccionesMap(), null, usuario.getLoginUsuario(), true,pos,ConstantesBD.logSeccionesCodigo, indices);
        	}
        	
        	logger.info("valor de la posicion eliminada >> "+pos+"MAPA ENTERO"+forma.getSeccionesMap());
    		//Secciones.eliminar(con, forma.getCodigoPk());
    		
    		Secciones mundo= new Secciones();
    		mundo.setCentroAtencion(forma.getCentroAtencion());
    		mundo.setInstitucion(usuario.getCodigoInstitucionInt());
    		mundo.setAlmacen(forma.getAlmacen());
    		forma.setSeccionesMap(Secciones.consultar(con, mundo));
    		
			UtilidadBD.closeConnection(con);
	    	return mapping.findForward("secciones");
    	}
    	UtilidadBD.closeConnection(con);
    	return mapping.findForward("secciones");
	}

	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @return
     */
    private ActionForward accionModificarRegistro(SeccionesForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
    {	
    	logger.info("forma.getCodigoPk()->"+forma.getCodigoPk());
    	if(forma.getCodigoPk()>0)
    	{	
    		logger.info("numReg->"+forma.getSeccionesMap().get("numRegistros"));
    		for(int w=0; w<Integer.parseInt(forma.getSeccionesMap().get("numRegistros").toString());w++)
    		{
    			logger.info("forma.getSeccionesMap().get(codigo_pk_+w).toString()->"+forma.getSeccionesMap().get("codigo_pk_"+w).toString());
    			if(forma.getSeccionesMap().get("codigo_pk_"+w).toString().equals(forma.getCodigoPk()+""))
    			{
    				forma.setCodigoSeccion(forma.getSeccionesMap().get("codseccion_"+w).toString());
    				forma.setDescripcionSeccion(forma.getSeccionesMap().get("descseccion_"+w).toString());
    				UtilidadBD.closeConnection(con);
    		    	return mapping.findForward("secciones");
    			}
    		}
    	}
    	UtilidadBD.closeConnection(con);
    	return mapping.findForward("secciones");
	}

	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @return
     */
    private ActionForward accionNuevoRegistro(SeccionesForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
    {
    	forma.setCodigoSeccion("");
		forma.setDescripcionSeccion("");
		UtilidadBD.closeConnection(con);
    	return mapping.findForward("secciones");
	}

    
	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @return
     */
    private ActionForward accionCargarSecciones(SeccionesForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
    {
    	Secciones mundo= new Secciones();
    	
    	mundo.setCentroAtencion(forma.getCentroAtencion());
		mundo.setInstitucion(usuario.getCodigoInstitucionInt());
		mundo.setAlmacen(forma.getAlmacen());
		
		forma.setSeccionesMap(Secciones.consultar(con, mundo));
		
		logger.info("SECCIONES MAP en CARGAR ->>"+forma.getSeccionesMap());
		
		UtilidadBD.closeConnection(con);
    	return mapping.findForward("secciones");
	}
    
    
    /**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @return
     */
    private ActionForward accionCargarSubSecciones(SeccionesForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
    {
    	Secciones mundo= new Secciones();
    	forma.setSubseccionesMap(Secciones.consultarSubsecciones(con, forma.getCodigoPk()));
    	
    	if(forma.getCodigoPk()>0)
    	{	
    		logger.info("numReg->"+forma.getSeccionesMap().get("numRegistros"));
    		for(int w=0; w<Integer.parseInt(forma.getSeccionesMap().get("numRegistros").toString());w++)
    		{
    			logger.info("forma.getSeccionesMap().get(codigo_pk_+w).toString()->"+forma.getSeccionesMap().get("codigo_pk_"+w).toString());
    			if(forma.getSeccionesMap().get("codigo_pk_"+w).toString().equals(forma.getCodigoPk()+""))
    			{
    				forma.setCodigoSeccion(forma.getSeccionesMap().get("codseccion_"+w).toString());
    				forma.setDescripcionSeccion(forma.getSeccionesMap().get("descseccion_"+w).toString());
    				UtilidadBD.closeConnection(con);
    		    	return mapping.findForward("subsecciones");
    			}
    		}
    	}
    	
		UtilidadBD.closeConnection(con);
    	return mapping.findForward("subsecciones");
	}
    

	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @return
     */
    private ActionForward accionGuardarNuevo(SeccionesForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
    {
    	Secciones mundo= new Secciones();
		llenarMundo(forma, mundo, usuario);
    	
		ActionErrors errores= new ActionErrors();
		logger.info("METODO PARA INSERTAR");
		
		logger.info("SECCIONES"+forma.getSeccionesMap());
		if (Secciones.insertar(con, mundo))
			forma.setEstado("operacionExitosa");
		
    	logger.info("FINALIZA LA INSERSION");
		
    	mundo.setCodigoPk(ConstantesBD.codigoNuncaValido);
    	mundo.setCodigoSeccion(ConstantesBD.codigoNuncaValido);
    	mundo.setDescripcionSeccion("");
    	forma.setSeccionesMap(Secciones.consultar(con, mundo));
    	
    	UtilidadBD.closeConnection(con);
    	return mapping.findForward("secciones");
	}

    /**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @return
     */
    private ActionForward accionGuardarModificacion(SeccionesForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
    {
    	Secciones mundo= new Secciones();
    	HashMap SeccionesN = new HashMap();
    	
    	//Se consulta el registro original----------------------------------------------
		mundo.reset();
    	mundo.setCodigoPk(forma.getCodigoPk());
    	//SeccionesN = Secciones.consultar(con, mundo);
    	//---------------------------------------------------------------------
    	
    	SeccionesN=(HashMap)forma.getSeccionesMap().clone();
    	
    	//Cargo los datos del registro a modificar
		llenarMundo(forma, mundo, usuario);
		
		
		
		String[] indices = {"codigo_pk_","codcentroatencion_","desccentroatencion_","codalmacen_","descalmacen_","codseccion_","descseccion_",""};
    	
    	int pos = ConstantesBD.codigoNuncaValido;
    	
    	for(int i = 0; i< Integer.parseInt(forma.getSeccionesMap().get("numRegistros").toString()) ; i++)
    		if(forma.getSeccionesMap().get("codigo_pk_"+i).equals(forma.getCodigoPk()))
    			pos = i;    	
    					
    	//logger.info("valor de la posicion eliminada >> "+pos+"MAPA ENTERO"+forma.getSeccionesMap());
    	logger.info("seccion que voy a modificar >> "+forma.getCodigoPk());
    	logger.info("valor de la posicion a modificar >> "+pos);
    	
    	forma.setSeccionesMap("codseccion_"+pos,forma.getCodigoSeccion());
    	forma.setSeccionesMap("descseccion_"+pos,forma.getDescripcionSeccion());
    	
    	
    	logger.info("valor de la posicion modificada >> "+pos);
     	
    	
    	//logger.info("MapaN ->"+SeccionesN);
    	
    	if(Secciones.modificar(con, mundo))
    	{
    		
    		  logger.info("MapaN ->"+SeccionesN);
    		  logger.info("Mapa Despues ->"+forma.getSeccionesMap());
    		
    		//Se genera el log tipo archivo
    		//Utilidades.generarLogGenerico(forma.getSeccionesMap(), SeccionesN, usuario.getLoginUsuario(), false,pos,ConstantesBD.logSeccionesCodigo, indices);
    		Utilidades.generarLogGenerico(forma.getSeccionesMap(), pos, SeccionesN,  usuario.getLoginUsuario(), false, pos, ConstantesBD.logSeccionesCodigo, indices);
    		
    		
    		//Se vuelve a consultar el listado de secciones
    		mundo.reset();
    		mundo.setInstitucion(usuario.getCodigoInstitucionInt());
    		mundo.setAlmacen(forma.getAlmacen());
    		mundo.setCentroAtencion(forma.getCentroAtencion());
    		forma.setSeccionesMap(Secciones.consultar(con, mundo));
    		
    		forma.setEstado("operacionExitosa");
    	}
    	
    	
    	UtilidadBD.closeConnection(con);
    	return mapping.findForward("secciones");
	}
    
    /**
     * 
     * @param forma
     * @param mundo
     * @param usuario 
     */
	private void llenarMundo(SeccionesForm forma, Secciones mundo, UsuarioBasico usuario) 
	{
		logger.info("valor del codigo >> "+forma.getCodigoSeccion()+" "+forma.getDescripcionSeccion());
		
		mundo.setCodigoSeccion(Integer.parseInt(forma.getCodigoSeccion()));
		mundo.setDescripcionSeccion(forma.getDescripcionSeccion());
		mundo.setCentroAtencion(forma.getCentroAtencion());
		mundo.setInstitucion(usuario.getCodigoInstitucionInt());
		mundo.setAlmacen(forma.getAlmacen());
		mundo.setUsuarioModifica(usuario.getLoginUsuario());
		mundo.setCodigoPk(forma.getCodigoPk());
		mundo.setCodigoPkSeccion(forma.getCodigoPkSeccion()+"");
		mundo.setCodigoSubseccion(forma.getCodigoSubseccion());
		mundo.setDescripcionSubseccion(forma.getDescripcionSubseccion());
		mundo.setCodigoSubseccionTemp(forma.getCodigoSubseccionTemp());
	}

	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @return
     */
	private ActionForward accionEmpezar(SeccionesForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		forma.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());
		UtilidadBD.closeConnection(con);
		return mapping.findForward("secciones");
	}	
}
