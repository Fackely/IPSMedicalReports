/*
 * @(#)MotivoAnulacionFacturasAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.action.facturacion;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.LogsAxioma;
import util.UtilidadBD;

import com.princetonsa.actionform.facturacion.MotivoAnulacionFacturasForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.MotivoAnulacionFacturas;

/**
 *   Action, controla todas las opciones dentro del registro de
 *   motivos de anulacion de facturas, incluyendo los posibles casos de error. 
 *   Y los casos de flujo.
 * @version 1.0, May. 05, 2005
 * @author cperalta
 */
public class MotivoAnulacionFacturasAction extends Action 
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(MotivoAnulacionFacturasAction.class);

	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
	{
		Connection con=null;
		try{
			if (response==null); //Para evitar que salga el warning
			if(form instanceof MotivoAnulacionFacturasForm)
			{

				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexión"+e.toString());
				}

				MotivoAnulacionFacturasForm motivosAnulacionFacturasForm =(MotivoAnulacionFacturasForm)form;
				UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				String estado=motivosAnulacionFacturasForm.getEstado(); 
				logger.info("[MotivoAnulacionFacturasAction] estado->"+estado);

				if(estado == null)
				{
					motivosAnulacionFacturasForm.reset();	
					motivosAnulacionFacturasForm.resetMapa();
					logger.warn("Estado no valido dentro del flujo de Motivos de Anulacion de Fatcuras (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					this.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				else if (estado.equals("empezar"))
				{
					return this.accionEmpezar(motivosAnulacionFacturasForm,mapping,usuario, con);
				}
				else if (estado.equals("redireccion"))
				{
					UtilidadBD.cerrarConexion(con);
					response.sendRedirect(motivosAnulacionFacturasForm.getLinkSiguiente());
					return null;
				}
				else if(estado.equals("ingresarNuevo"))
				{
					return this.accionIngresarNuevo(motivosAnulacionFacturasForm,request, response, con);
				}
				else if(estado.equals("eliminarDelMapa"))
				{
					return this.accionEliminarDelMapa(motivosAnulacionFacturasForm,mapping,con);
				}
				else if(estado.equals("guardar"))
				{
					return this.accionGuardar(motivosAnulacionFacturasForm,mapping,request,usuario,con);
				}
				else if(estado.equals("listarCollectionResumen") || estado.equals("listarCollectionConsulta"))
				{
					return accionListarCollectionResumenOconsulta(motivosAnulacionFacturasForm, mapping, request, usuario, con, estado);
				}
				else
				{
					motivosAnulacionFacturasForm.reset();
					motivosAnulacionFacturasForm.resetMapa();
					logger.warn("Estado no valido dentro del flujo de Motivos de Anlacion de Facturas (null) ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					this.cerrarConexion(con);
					return mapping.findForward("paginaError");
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
	 * Este método especifica las acciones a realizar en el estado
	 * guardar
	 * @param motivosAnulacionFacturasForm
	 * @param mapping
	 * @param request
	 * @param usuario
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionGuardar (MotivoAnulacionFacturasForm motivosAnulacionFacturasForm, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario, Connection con) throws SQLException
	{
	    guardarNuevosEnBD(motivosAnulacionFacturasForm,usuario,con);
	    modificarMotivosAntiguosBD(motivosAnulacionFacturasForm,usuario, con);
	    motivosAnulacionFacturasForm.setEstado("listarCollectionResumen");
	    return this.accionListarCollectionResumenOconsulta(motivosAnulacionFacturasForm, mapping, request, usuario, con, "listarCollectionResumen"); 
	}
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * listarCollectionResumen - listarCollectionConsulta
	 * @param motivosAnulacionFacturasForm
	 * @param mapping
	 * @param request
	 * @param usuario
	 * @param con
	 * @param estado
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionListarCollectionResumenOconsulta(MotivoAnulacionFacturasForm motivosAnulacionFacturasForm, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario, Connection con, String estado) throws SQLException 
	{
		MotivoAnulacionFacturas mundo= new MotivoAnulacionFacturas();
		mundo.setCodigoInstitucion(Integer.parseInt(usuario.getCodigoInstitucion()));
		motivosAnulacionFacturasForm.setEstado(estado);
		motivosAnulacionFacturasForm.setCol(mundo.listadoMotivosCollection(con));
		this.cerrarConexion(con);
		
		if(estado.equals("listarCollectionResumen"))
		    return mapping.findForward("paginaListarCollectionResumen");
		else if(estado.equals("listarCollectionConsulta"))
		    return mapping.findForward("paginaListarCollectionConsulta");
		else //no esta dentro del flujo entonces genera error
		{
			motivosAnulacionFacturasForm.reset();
			motivosAnulacionFacturasForm.resetMapa();
			logger.warn("Estado no valido dentro del flujo de Motivo Anulacion Facturas (null) ");
			request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
			this.cerrarConexion(con);
			return mapping.findForward("paginaError");
		}
	}	
	
	/**
	 * Método que modifica los valores de la tabla motivos anulacion facturas (Descripción - Activo)
	 * @param motivosAnulacionFacturasForm
	 * @param usuario
	 * @param con
	 * @throws SQLException
	 */
	private void modificarMotivosAntiguosBD(	MotivoAnulacionFacturasForm motivosAnulacionFacturasForm, UsuarioBasico usuario, Connection con) throws SQLException
    {
		MotivoAnulacionFacturas mundo= new MotivoAnulacionFacturas();
	    //en este punto se carga el mapa solo con los valores que han sido modificados
	    motivosAnulacionFacturasForm.setMapaMotivos(motivosAnulacionFacturasForm.comparar2HashMap());
	    
	    if(motivosAnulacionFacturasForm.getNumeroRealFilasMapa()>0)
	    {
	        for(int k=0; k<motivosAnulacionFacturasForm.getNumeroRealFilasMapaNoMod();k++)
	        {
	            
	            String tempoKeyCod=motivosAnulacionFacturasForm.getMapaMotivos("codigo_"+k)+"";
	            if(tempoKeyCod!=null && !tempoKeyCod.equals("") && !tempoKeyCod.equals("null") && !tempoKeyCod.equals("vacio"))
	            {
	                mundo.setCodigoMotivoAnulacion(Integer.parseInt(motivosAnulacionFacturasForm.getMapaMotivos("codigo_"+k)+""));
	                mundo.setDescripcionMotivoAnulacion(motivosAnulacionFacturasForm.getMapaMotivos("descripcion_"+k)+"");
	                if((motivosAnulacionFacturasForm.getMapaMotivos("activo_"+k)+"").equals("t"))
	                    mundo.setActivoMotivo(true);
		            else
		                mundo.setActivoMotivo(false);
	                int pudoMod=mundo.modificarMotivosAnulacionFacturas(con);
	                if(pudoMod>0)
	                {
	                    generarLogModificacion(motivosAnulacionFacturasForm, k, usuario);
	                }
	                
	            }
	        }
	    }
    }
	
	/**
	 * Método que genera los Logs de Modificación de los motivos de anulacion de facturas
	 * @param motivosAnulacionFacturasForm
	 * @param indexKeyCodigoMapaMod
	 * @param usuario
	 */
	private void generarLogModificacion(MotivoAnulacionFacturasForm motivosAnulacionFacturasForm, int indexKeyCodigoMapaMod, UsuarioBasico usuario)
	{
	    String log="\n            ====INFORMACION ORIGINAL MOTIVOS DE ANULACION DE FACTURAS===== " +
		"\n*  Código [" +motivosAnulacionFacturasForm.getMapaMotivosNoModificado("codigo_"+indexKeyCodigoMapaMod) +"] "+
		"\n*  Descripción ["+motivosAnulacionFacturasForm.getMapaMotivosNoModificado("descripcion_"+indexKeyCodigoMapaMod)+"] "  ;
		
		if((motivosAnulacionFacturasForm.getMapaMotivosNoModificado("activo_"+indexKeyCodigoMapaMod)+"").equals("t"))
			log += "\n*  Activa [ SI ]";
		else
			log += "\n*  Activa [ NO ]";
		
		log+="\n          =====INFORMACION DESPUES DE LA MODIFICACION MOTIVOS DE ANULACION DE FACTURAS===== " +
		"\n*  Código [" +motivosAnulacionFacturasForm.getMapaMotivos("codigo_"+indexKeyCodigoMapaMod) +"] "+
		"\n*  Descripción ["+motivosAnulacionFacturasForm.getMapaMotivos("descripcion_"+indexKeyCodigoMapaMod)+"] "  ;
		
		if((motivosAnulacionFacturasForm.getMapaMotivos("activo_"+indexKeyCodigoMapaMod)+"").equals("t"))
			log += "\n*  Activa [ SI ]";
		else
			log += "\n*  Activa [ NO ]";
		log+="\n========================================================\n\n\n " ;	
		
		LogsAxioma.enviarLog(ConstantesBD.logMotivoAnulacionFacturasCodigo, log, ConstantesBD.tipoRegistroLogModificacion, usuario.getLoginUsuario());
	}
	
	/**
	 * Método que comparando el HashMap Original y el modificado,
	 * entonces inserta en BD los que en el hash Map tienen como key del
	 * codigo= 'vacio', ya que esto indica que son nuevos debido a que los originales 
	 * que estan en BD tienen un cod de sequence.
	 * @param motivosAnulacionFacturasForm
	 * @param usuario
	 * @param con
	 * @throws SQLException
	 */
	private void guardarNuevosEnBD(	MotivoAnulacionFacturasForm motivosAnulacionFacturasForm, UsuarioBasico usuario, Connection con) throws SQLException
	{
		MotivoAnulacionFacturas mundo= new MotivoAnulacionFacturas();
	    mundo.setCodigoInstitucion(Integer.parseInt(usuario.getCodigoInstitucion()));
	    /*Si es mayor entonces es que hay nuevos motivos para insertar para insertar*/
	    if(motivosAnulacionFacturasForm.getMapaMotivos().size()>motivosAnulacionFacturasForm.getMapaMotivosNoModificado().size())
	    {
	        for(int k=0; k<motivosAnulacionFacturasForm.getNumeroRealFilasMapa(); k++)
	        {
	            if((motivosAnulacionFacturasForm.getMapaMotivos("codigo_"+k)+"").equals("vacio"))
	            {
	                if((motivosAnulacionFacturasForm.getMapaMotivos("activo_"+k)+"").equals("t"))
	                    mundo.setActivoMotivo(true);
		            else
		                mundo.setActivoMotivo(false);
		            
		            mundo.setDescripcionMotivoAnulacion(motivosAnulacionFacturasForm.getMapaMotivos("descripcion_"+k)+"");
		            mundo.insertarMotivosAnulacionFacturas(con);
	            }
	        }
	    }
	}

	/**
	 * Este método especifica las acciones a realizar en el estado
	 * eliminarDelMapa.
	 * @param motivosAnulacionFacturasForm
	 * @param mapping
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionEliminarDelMapa(MotivoAnulacionFacturasForm motivosAnulacionFacturasForm, ActionMapping mapping, Connection con) throws SQLException
	{
	    int tamanioMapaAntesEliminar=motivosAnulacionFacturasForm.getNumeroRealFilasMapa();
	    
	    motivosAnulacionFacturasForm.getMapaMotivos().remove("codigo_"+motivosAnulacionFacturasForm.getKeyDelete());
	    motivosAnulacionFacturasForm.getMapaMotivos().remove("descripcion_"+motivosAnulacionFacturasForm.getKeyDelete());
	    motivosAnulacionFacturasForm.getMapaMotivos().remove("activo_"+motivosAnulacionFacturasForm.getKeyDelete());
	    motivosAnulacionFacturasForm.getMapaMotivos().remove("puedoborrar_"+motivosAnulacionFacturasForm.getKeyDelete());
	    
	    /*El siguiente cod, lo que verifica, es que si eliminan un elemento del mapa y atrás de el existen más datos entonces
	     * se debe correr el index del hash Map de los que estan después del eliminado una posición menos para que existe 
	     * concordancia entre el size real del mapa y el index de elementos */
	    int indexEliminado= Integer.parseInt(motivosAnulacionFacturasForm.getKeyDelete().substring(motivosAnulacionFacturasForm.getKeyDelete().indexOf("_")+1));
	    
	    if(tamanioMapaAntesEliminar>indexEliminado)
	    {
	        for(int k=indexEliminado; k<tamanioMapaAntesEliminar; k++)
	        {
	        	motivosAnulacionFacturasForm.setMapaMotivos("codigo_"+k, motivosAnulacionFacturasForm.getMapaMotivos("codigo_"+(k+1)));
	        	motivosAnulacionFacturasForm.setMapaMotivos("descripcion_"+k, motivosAnulacionFacturasForm.getMapaMotivos("descripcion_"+(k+1)));
	        	motivosAnulacionFacturasForm.setMapaMotivos("activo_"+k, motivosAnulacionFacturasForm.getMapaMotivos("activo_"+(k+1)));
	        	motivosAnulacionFacturasForm.setMapaMotivos("puedoborrar_"+k, motivosAnulacionFacturasForm.getMapaMotivos("puedoborrar_"+(k+1)));
	        }
	        motivosAnulacionFacturasForm.getMapaMotivos().remove("codigo_"+(tamanioMapaAntesEliminar-1));
	        motivosAnulacionFacturasForm.getMapaMotivos().remove("descripcion_"+(tamanioMapaAntesEliminar-1));
	        motivosAnulacionFacturasForm.getMapaMotivos().remove("activo_"+(tamanioMapaAntesEliminar-1));
	        motivosAnulacionFacturasForm.getMapaMotivos().remove("puedoborrar_"+(tamanioMapaAntesEliminar-1));
	    }
	    
	    motivosAnulacionFacturasForm.reset();
	    this.cerrarConexion(con);
		return mapping.findForward("paginaPrincipal");	
	}
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * ingresarNuevo.
	 * @param motivosAnulacionFacturasForm
	 * @param request
	 * @param response
	 * @param con
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	private ActionForward accionIngresarNuevo(MotivoAnulacionFacturasForm motivosAnulacionFacturasForm, HttpServletRequest request, HttpServletResponse response, Connection con) throws SQLException, Exception
	{
	    int tempoTamanioRealMapa=motivosAnulacionFacturasForm.getNumeroRealFilasMapa();
	    motivosAnulacionFacturasForm.setMapaMotivos("codigo_"+tempoTamanioRealMapa, "vacio");
	    motivosAnulacionFacturasForm.setMapaMotivos("descripcion_"+tempoTamanioRealMapa, motivosAnulacionFacturasForm.getDescripcion());
	    motivosAnulacionFacturasForm.setMapaMotivos("activo_"+tempoTamanioRealMapa,"t");
	    motivosAnulacionFacturasForm.setMapaMotivos("puedoborrar_"+tempoTamanioRealMapa, "t");
	    motivosAnulacionFacturasForm.reset();
	    
	    if(request.getParameter("ultimaPagina")==null)
	    {
	        if(motivosAnulacionFacturasForm.getNumeroRealFilasMapa()>(motivosAnulacionFacturasForm.getOffsetHash()+motivosAnulacionFacturasForm.maxPagesItemsHash))
	        	motivosAnulacionFacturasForm.setOffsetHash(motivosAnulacionFacturasForm.getOffsetHash()+motivosAnulacionFacturasForm.maxPagesItemsHash);
	        this.cerrarConexion(con);
	        
	        response.sendRedirect("motivoAnulacionFacturas.jsp?pager.offset="+motivosAnulacionFacturasForm.getOffsetHash());
	    }
	    else
	    {    
		    String ultimaPagina=request.getParameter("ultimaPagina");
		    int posOffSet=ultimaPagina.indexOf("offset=")+7;
		    motivosAnulacionFacturasForm.setOffsetHash((Integer.parseInt(ultimaPagina.substring(posOffSet, ultimaPagina.length() ))));
		    
		    if(motivosAnulacionFacturasForm.getNumeroRealFilasMapa()>(motivosAnulacionFacturasForm.getOffsetHash()+motivosAnulacionFacturasForm.maxPagesItemsHash))
		    	motivosAnulacionFacturasForm.setOffsetHash(motivosAnulacionFacturasForm.getOffsetHash()+motivosAnulacionFacturasForm.maxPagesItemsHash);
		    
		    this.cerrarConexion(con);
		    response.sendRedirect(ultimaPagina.substring(0, posOffSet)+motivosAnulacionFacturasForm.getOffsetHash());
	    }
	    return null;
	    
	}
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * empezar.
	 * @param motivosAnulacionFacturasForm
	 * @param mapping
	 * @param usuario
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionEmpezar(MotivoAnulacionFacturasForm motivosAnulacionFacturasForm, ActionMapping mapping, UsuarioBasico usuario, Connection con) throws SQLException
	{
		//Limpiamos lo que venga del form
		motivosAnulacionFacturasForm.reset();
		motivosAnulacionFacturasForm.resetMapa();
		motivosAnulacionFacturasForm.setEstado("empezar");
		MotivoAnulacionFacturas mundo= new MotivoAnulacionFacturas();
		
		mundo.setCodigoInstitucion(Integer.parseInt(usuario.getCodigoInstitucion()));
		motivosAnulacionFacturasForm.setMapaMotivos(mundo.listadoMotivos(con));
		
		motivosAnulacionFacturasForm.setMapaMotivosNoModificado(mundo.listadoMotivos(con));
		
		this.cerrarConexion(con);
		return mapping.findForward("paginaPrincipal");		
	}
	
	/**
	 * Método en que se cierra la conexión (Buen manejo
	 * recursos), usado ante todo al momento de hacer
	 * un forward
	 * @param con Conexión con la fuente de datos
	 * @throws SQLException
	 */
	public void cerrarConexion (Connection con) throws SQLException
	{
		if (con!=null&&!con.isClosed())
		{
			UtilidadBD.closeConnection(con);
		}
	}

	
}
