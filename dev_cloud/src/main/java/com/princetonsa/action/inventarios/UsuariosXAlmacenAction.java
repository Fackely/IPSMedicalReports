
/*
 * Creado   23/11/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_03
 */
package com.princetonsa.action.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.Listado;
import util.UtilidadBD;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.inventarios.UsuariosXAlmacenForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.UsuariosXAlmacen;

/**
 * 
 *
 * @version 1.0, 23/11/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class UsuariosXAlmacenAction extends Action 
{
    /**
     * manejador de los logs de la clase
     */
    private Logger logger=Logger.getLogger(UsuariosXAlmacenAction.class);
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

			if(form instanceof UsuariosXAlmacenForm)
			{

				//intentamos abrir una conexion con la fuente de datos 
				con = UtilidadBD.abrirConexion();
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}
				UsuariosXAlmacenForm formUXA=(UsuariosXAlmacenForm)form;
				UsuariosXAlmacen mundoUXA=new UsuariosXAlmacen();
				HttpSession sesion = request.getSession();			
				UsuarioBasico usuario = null;
				usuario = Utilidades.getUsuarioBasicoSesion(sesion);
				String estado=formUXA.getEstado();
				logger.warn("UsuariosXAlmacenAction estado->"+estado);
				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de UsuariosXAlmacenAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				if(estado.equals("empezarInsertar"))
				{
					formUXA.reset();
					formUXA.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
					//-----Se asigna el centro de atención del usuario por defecto cuando empieza ---------//
					formUXA.setCentroAtencion(usuario.getCodigoCentroAtencion());
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaPrincipal");   
				}
				if(estado.equals("generarConsulta"))
				{
					logger.info("\n el codigo del almacen antes de entrar al metodo -->"+formUXA.getCodAlmacen());
					return this.accionConsulta(con,formUXA,mundoUXA,mapping,usuario,formUXA.getCodAlmacen());
				}
				if(estado.equals("guardar"))
				{
					return this.accionGuardar(con,formUXA,mundoUXA,mapping,usuario);
				}
				if(estado.equals("nuevoRegistro"))
				{
					return this.accionNuevoRegistro(con,formUXA,mapping,response,request);
				}
				if(estado.equals("eliminarRegistro"))
				{
					return this.accionEliminarRegistro(con,formUXA,mapping);
				}
				if(estado.equals("ordenarColumna"))
				{
					return this.accionOrdenarMapa(con,formUXA,mapping,response,request);
				}
				if (estado.equals("redireccion"))
				{
					return accionRedireccion(con,formUXA,response,mapping,request);
				}
				else if(estado.equals("cargarCentrosCosto"))
				{
					UtilidadBD.cerrarConexion(con);
					HashMap temp = new HashMap ();
					temp.put("numRegistros", 0);
					formUXA.setMapaUXA(temp);
					return mapping.findForward("paginaPrincipal");
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
				logger.error("El form no es compatible con el form de UsuariosXAlmacenForm");
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
	 * Método implementado para paginar el listado de usuarios x almacen
	 * @param con
	 * @param formUXA
	 * @param response
	 * @param mapping
	 * @param request
	 * @return
	 */
    private ActionForward accionRedireccion(Connection con, UsuariosXAlmacenForm formUXA, HttpServletResponse response, ActionMapping mapping, HttpServletRequest request) 
    {
    	try
		{
			
		    UtilidadBD.closeConnection(con);
			response.sendRedirect(formUXA.getLinkSiguiente());
			return null;
		}
		catch(Exception e)
		{
			logger.error("Error en accionRedireccion de UsuariosXAlmacenAction: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en UsuariosXAlmacenAction", "errors.problemasDatos", true);
		}
	}
	/**
     * metodo para realizar el ordenamiento
     * @param con
     * @param formUXA
     * @param mapping
	 * @param request 
	 * @param response 
     * @return
     */
    private ActionForward accionOrdenarMapa(Connection con, UsuariosXAlmacenForm formUXA, ActionMapping mapping, HttpServletResponse response, HttpServletRequest request) 
    {
        ValoresPorDefecto.getMaxPageItems(hashCode());
        int numRegTemp=0;
        numRegTemp=Integer.parseInt(formUXA.getMapaUXA("numRegistros")+"");        
        String[] indices={
				            "tipoReg_", 
				            "cod_centros_costo_", 
				            "desc_tipos_trans_inventario_", 
				            "login_usuario_",
				            "codigo_",
				            "nombre_"				            				            
	            		};
		formUXA.setMapaUXA(Listado.ordenarMapa(indices,
														formUXA.getPatronOrdenar(),
														formUXA.getUltimoPatron(),
														formUXA.getMapaUXA(),
														numRegTemp));
		formUXA.setMapaUXA("numRegistros",numRegTemp+"");
		formUXA.setUltimoPatron(formUXA.getPatronOrdenar());
		UtilidadBD.closeConnection(con);
		try
		{
	        response.sendRedirect("../ingresarModificarUsuariosXAlmacen/usuariosXAlmacen.jsp?pager.offset="+formUXA.getOffset());
			return null; 
        }
	    catch(Exception e)
		{
			logger.error("Error en accionNuevoRegistro de UsuariosXAlmacenAction: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en UsuariosXAlmacenAction", "errors.problemasDatos", true);
		}
		      
    }
    /**
     * metodo para eliminar registros de memoria
     * @param con
     * @param formUXA
     * @param mapping
     * @return
     */
    private ActionForward accionEliminarRegistro(Connection con, UsuariosXAlmacenForm formUXA, ActionMapping mapping) 
    {
        int posEli=formUXA.getRegEliminar();
        int nuevaPos=Integer.parseInt(formUXA.getMapaUXA("numRegistros")+"")-1;
         
        if((formUXA.getMapaUXA("tipoReg_"+posEli)+"").equals("BD"))
        { 
            formUXA.setRegistrosBDEliminar(formUXA.getMapaUXA("codigo_"+posEli));          
        }              
        formUXA.setMapaUXA("numRegistros",nuevaPos+"");        
        if(posEli!=nuevaPos)
        {
	        for(int j=posEli;j<Integer.parseInt(formUXA.getMapaUXA("numRegistros")+"");j++)
	        {                                 
	            formUXA.setMapaUXA("tipoReg_"+j,formUXA.getMapaUXA("tipoReg_"+(j+1)));
	            formUXA.setMapaUXA("cod_centros_costo_"+j,formUXA.getMapaUXA("cod_centros_costo_"+(j+1)));
	            formUXA.setMapaUXA("login_usuario_"+j,formUXA.getMapaUXA("login_usuario_"+(j+1)));
	            formUXA.setMapaUXA("codigo_"+j,formUXA.getMapaUXA("codigo_"+(j+1)));
	            formUXA.setMapaUXA("nombre_"+j,formUXA.getMapaUXA("nombre_"+(j+1)));
	        }
        }
        formUXA.getMapaUXA().remove("tipoReg_"+nuevaPos);
        formUXA.getMapaUXA().remove("cod_centros_costo_"+nuevaPos);
        formUXA.getMapaUXA().remove("login_usuario_"+nuevaPos);
        formUXA.getMapaUXA().remove("codigo_"+nuevaPos);                   
        formUXA.getMapaUXA().remove("nombre_"+nuevaPos);
        
        
        UtilidadBD.closeConnection(con);
        //***SE valida que en la posición del pager hayan registros****
		if(formUXA.getOffset()>=nuevaPos)
		{
			int maxPageItems=formUXA.getMaxPageItems();
			int offset=nuevaPos-maxPageItems;
			if(offset<0)
				offset=0;
			formUXA.setOffset(offset);
		}
		return mapping.findForward("paginaPrincipal"); 
    }
    /**
     * metodo para generar un registro nuevo
     * @param con
     * @param formUXA
     * @param mapping
     * @param response 
     * @param request 
     * @return
     */
    private ActionForward accionNuevoRegistro(Connection con, UsuariosXAlmacenForm formUXA, ActionMapping mapping, HttpServletResponse response, HttpServletRequest request) 
    {
        int pos=Integer.parseInt(formUXA.getMapaUXA("numRegistros")+"");        
        formUXA.setMapaUXA("codigo_"+pos,"");
        formUXA.setMapaUXA("cod_centros_costo_"+pos,"");
        formUXA.setMapaUXA("login_usuario_"+pos,"Seleccione");
        formUXA.setMapaUXA("tipoReg_"+pos,"MEM");
        formUXA.setMapaUXA("nombre_"+pos,"");
        pos ++;
        formUXA.setMapaUXA("numRegistros",pos+"");        
        UtilidadBD.closeConnection(con);
        
        if(pos>(formUXA.getOffset()+formUXA.getMaxPageItems()))
        {
        	int offset = pos- formUXA.getMaxPageItems();
        	while(offset%formUXA.getMaxPageItems()!=0)
        		offset++;
	       formUXA.setOffset(offset);
        }
        try
        {
	        response.sendRedirect("../ingresarModificarUsuariosXAlmacen/usuariosXAlmacen.jsp?pager.offset="+formUXA.getOffset());
			return null; 
        }
	    catch(Exception e)
		{
			logger.error("Error en accionNuevoRegistro de UsuariosXAlmacenAction: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en UsuariosXAlmacenAction", "errors.problemasDatos", true);
		}
    }
    /**
     * metodo para guardar cambios
     * @param con
     * @param formUXA
     * @param mundoUXA
     * @param mapping
     * @param usuario
     * @return
     */
    private ActionForward accionGuardar(Connection con, UsuariosXAlmacenForm formUXA, UsuariosXAlmacen mundoUXA, ActionMapping mapping, UsuarioBasico usuario) 
    {
        mundoUXA.setCodAlmacen(formUXA.getCodAlmacen());
        mundoUXA.setInstitucion(usuario.getCodigoInstitucionInt());
        mundoUXA.setMapaUXA(formUXA.getMapaUXA());
        mundoUXA.setRegistrosBDEliminar(formUXA.getRegistrosBDEliminar());
        mundoUXA.setAlmacen(formUXA.getNomAlmacen());
        mundoUXA.setUsuario(usuario.getLoginUsuario());        
        mundoUXA.guardarCambiosEnBDTrans(con);
        formUXA.resetListados();       
        return this.accionConsulta(con,formUXA,mundoUXA,mapping,usuario,formUXA.getCodAlmacen());        
    }
    /**
     * metodo para generar la consulta
     * @param con
     * @param formUXA
     * @param mundoUXA
     * @param mapping
     * @param usuario
     * @return
     */
    private ActionForward accionConsulta(Connection con, UsuariosXAlmacenForm formUXA, UsuariosXAlmacen mundoUXA, ActionMapping mapping, UsuarioBasico usuario, int codAlmacen) 
    {
        mundoUXA.setCodAlmacen(formUXA.getCodAlmacen());
       
        mundoUXA.setInstitucion(usuario.getCodigoInstitucionInt());
        formUXA.setMapaUXA(mundoUXA.generarConsulta(con));
       // logger.info("\n codigo almacen -->"+codAlmacen);
        formUXA.setCodAlmacen(codAlmacen);
        UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaPrincipal");   
    }
    
}
