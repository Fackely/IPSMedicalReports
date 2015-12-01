
/*
 * Creado   11/11/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_03
 */
package com.princetonsa.action.inventarios;

import java.io.IOException;
import java.sql.Connection;

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

import util.Listado;
import util.UtilidadBD;

import com.princetonsa.actionform.inventarios.TiposTransaccionesInvForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.TiposTransaccionesInv;

/**
 * Clase para manejar el workflow de la funcionalidad
 *
 * @version 1.0, 11/11/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class TiposTransaccionesInvAction extends Action
{
    /**
     * manejador de los logs de la clase
     */
    private Logger logger=Logger.getLogger(TiposTransaccionesInvAction.class);
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
			if(form instanceof TiposTransaccionesInvForm)
			{

				//intentamos abrir una conexion con la fuente de datos 
				con = UtilidadBD.abrirConexion();
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}
				TiposTransaccionesInvForm formTran=(TiposTransaccionesInvForm)form;
				TiposTransaccionesInv mundoTrans=new TiposTransaccionesInv();
				HttpSession sesion = request.getSession();			
				UsuarioBasico usuario = null;
				usuario = getUsuarioBasicoSesion(sesion);
				String estado=formTran.getEstado();
				logger.warn("estado->"+estado);
				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de AprobacionPagosCarteraAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				if(estado.equals("empezarInsertar"))
				{
					formTran.reset();
					mundoTrans.setInstitucion(usuario.getCodigoInstitucionInt());
					formTran.setMapaTipos(mundoTrans.generarConsultaTransacciones(con));
					formTran.setPatronOrdenar("descripcion_");
					this.accionOrdenarMapa(formTran);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaPrincipal");   
				}
				if(estado.equals("nuevoRegistro"))
				{
					return accionIngresarNuevo(con,formTran,response,request,usuario);
				}
				else if (estado.equals("redireccion"))
				{			    
					UtilidadBD.cerrarConexion(con);
					formTran.getLinkSiguiente();
					response.sendRedirect(formTran.getLinkSiguiente());
					return null;
				}
				if(estado.equals("guardar"))
				{
					return this.accionGuardar(con,formTran,mundoTrans,mapping,request,usuario);
				}
				if(estado.equals("eliminarRegistro"))
				{
					return this.accionEliminarRegistro(con,formTran,mapping);
				}
				if(estado.equals("ordenarColumna"))
				{
					String forward=this.accionOrdenarMapa(formTran);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward(forward);
				}
				if(estado.equals("buscarRegistros"))
				{
					return this.accionBusquedaAvanzada(con,formTran,mundoTrans,mapping,usuario);
				}
				if(estado.equals("empezarConsulta"))
				{
					formTran.reset();
					mundoTrans.setInstitucion(usuario.getCodigoInstitucionInt());
					formTran.setMapaTipos(mundoTrans.generarConsultaTransacciones(con));
					formTran.setPatronOrdenar("descripcion_");
					this.accionOrdenarMapa(formTran);
					UtilidadBD.cerrarConexion(con);
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
				logger.error("El form no es compatible con el form de TiposTransaccionesInvForm");
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
	 * metodo para realizar la busqueda avanzada
     * @param con
     * @param formTran
     * @param mundoTrans
     * @param mapping
     * @return 
     */
    private ActionForward accionBusquedaAvanzada(Connection con, TiposTransaccionesInvForm formTran, TiposTransaccionesInv mundoTrans, ActionMapping mapping,UsuarioBasico usuario) 
    {
        formTran.resetMapa();
        mundoTrans.setCodigo(formTran.getCodigo());
        mundoTrans.setDescripcion(formTran.getDescripcion());
        mundoTrans.setCodConcepto(formTran.getCodConcepto());
        mundoTrans.setCodCosto(formTran.getCodCosto());
        mundoTrans.setActivo(formTran.getActivo());
        mundoTrans.setIndicativo_consignacion(formTran.getIndicativo_consignacion());
        mundoTrans.setInstitucion(usuario.getCodigoInstitucionInt());
        mundoTrans.setCodigoInterfaz(formTran.getCodigoInterfaz());
	    formTran.setMapaTipos(mundoTrans.generarConsultaTransacciones(con));
	    formTran.setPatronOrdenar("descripcion_");
	    this.accionOrdenarMapa(formTran);
	    formTran.resetCamposBusqueda();
        UtilidadBD.closeConnection(con);
	    return mapping.findForward("paginaPrincipal");
    }
    /**
	  * metodo para ordenenar el mapa
     * @param con
     * @param formTran
     * @param mapping
     * @return
     */
    private String accionOrdenarMapa(TiposTransaccionesInvForm formTran) 
    {
        int numRegTemp=0;
        numRegTemp=Integer.parseInt(formTran.getMapaTipos("numRegistros")+"");        
        String[] indices={
				            "codigo_", 
				            "descripcion_", 
				            "consecutivo_", 
				            "descripcion_concepto_",
				            "codigo_concepto_",
				            "descripcion_costo_",
				            "codigo_costo_",
				            "activo_",
				            "indicativo_consignacion_",
				            "tipoReg_",
				            "codigo_interfaz_"
	            		};
        formTran.setMapaTipos(Listado.ordenarMapa(indices,
                										formTran.getPatronOrdenar(),
                										formTran.getUltimoPatron(),
                										formTran.getMapaTipos(),
                										Integer.parseInt(formTran.getMapaTipos("numRegistros")+"")));
        
        formTran.setMapaTipos("numRegistros",numRegTemp);
        formTran.setUltimoPatron(formTran.getPatronOrdenar());
       return "paginaPrincipal";
    }
    /**
	  * Metodo para eliminar registros, de la 
	  * memoria, y marcar los de la BD, para 
	  * luego eliminarnos
     * @param con 
     * @param formTran
     * @param mapping
     * @return
     */
    private ActionForward accionEliminarRegistro(Connection con, TiposTransaccionesInvForm formTran, ActionMapping mapping) 
    {
        int posEli=Integer.parseInt(formTran.getCodigoRegEliminar());
        int nuevaPos=Integer.parseInt(formTran.getMapaTipos("numRegistros")+"")-1;
          
        if((formTran.getMapaTipos("tipoReg_"+posEli)+"").equals("BD"))
        {            
            int pos=formTran.getContRegistrosEliminar();
            formTran.setRegistrosBDEliminar(pos,formTran.getMapaTipos("consecutivo_"+posEli));
            formTran.setContRegistrosEliminar(pos++);
        } 
        formTran.setMapaTipos("numRegistros",nuevaPos+"");
        if(posEli!=nuevaPos)
	        for(int j=posEli;j<Integer.parseInt(formTran.getMapaTipos("numRegistros")+"");j++)
	        {                                 
	            formTran.setMapaTipos("consecutivo_"+j,formTran.getMapaTipos("consecutivo_"+(j+1)));
	            formTran.setMapaTipos("descripcion_concepto_"+j,formTran.getMapaTipos("descripcion_concepto_"+(j+1)));
	            formTran.setMapaTipos("codigo_concepto_"+j,formTran.getMapaTipos("codigo_concepto_"+(j+1)));
	            formTran.setMapaTipos("descripcion_costo_"+j,formTran.getMapaTipos("descripcion_costo_"+(j+1)));
	            formTran.setMapaTipos("codigo_costo_"+j,formTran.getMapaTipos("codigo_costo_"+(j+1)));
	            formTran.setMapaTipos("codigo_"+j,formTran.getMapaTipos("codigo_"+(j+1)));
	            formTran.setMapaTipos("descripcion_"+j,formTran.getMapaTipos("descripcion_"+(j+1)));
	            formTran.setMapaTipos("activo_"+j,formTran.getMapaTipos("activo_"+(j+1)));
	            formTran.setMapaTipos("indicativo_consignacion_"+j,formTran.getMapaTipos("indicativo_consignacion_"+(j+1)));
	            formTran.setMapaTipos("codigo_interfaz_"+j,formTran.getMapaTipos("codigo_interfaz_"+(j+1)));
	            formTran.setMapaTipos("tipoReg_"+j,formTran.getMapaTipos("tipoReg_"+(j+1)));                        
	        }
        formTran.getMapaTipos().remove("consecutivo_"+nuevaPos);
        formTran.getMapaTipos().remove("descripcion_concepto_"+nuevaPos);
        formTran.getMapaTipos().remove("codigo_concepto_"+nuevaPos);
        formTran.getMapaTipos().remove("descripcion_costo_"+nuevaPos);
        formTran.getMapaTipos().remove("codigo_costo_"+nuevaPos);
        formTran.getMapaTipos().remove("codigo_"+nuevaPos);
        formTran.getMapaTipos().remove("descripcion_"+nuevaPos);
        formTran.getMapaTipos().remove("activo_"+nuevaPos);
        formTran.getMapaTipos().remove("indicativo_consignacion_"+nuevaPos);
        formTran.getMapaTipos().remove("codigo_interfaz_"+nuevaPos);
        formTran.getMapaTipos().remove("tipoReg_"+nuevaPos);        
        UtilidadBD.closeConnection(con);
        return mapping.findForward("paginaPrincipal"); 
    }
    /**
	 * metodo para guardar las operaciones realizadas
	 * insertar,modificar eliminar
     * @param con
     * @param formTran
     * @param mapping
     * @return
     */
    private ActionForward accionGuardar(Connection con, TiposTransaccionesInvForm formTran, TiposTransaccionesInv mundoTrans,ActionMapping mapping,HttpServletRequest request,UsuarioBasico usuario) 
    {
       mundoTrans.setMapaTipos(formTran.getMapaTipos());
       mundoTrans.setInstitucion(usuario.getCodigoInstitucionInt());
       mundoTrans.setRegistrosBDEliminar(formTran.getRegistrosBDEliminar());
       mundoTrans.setContRegistrosEliminar(formTran.getContRegistrosEliminar());  
       mundoTrans.setUsuario(usuario.getLoginUsuario());       
       boolean endTransaction=mundoTrans.guardarCambiosEnBDTrans(con);
       if(!endTransaction)
       {
           ActionErrors errores = new ActionErrors(); 
           errores.add("no ingreso/modifico/elimino", new ActionMessage("prompt.noSeGraboInformacion"));
           saveErrors(request, errores);
           UtilidadBD.closeConnection(con);
           formTran.setOperacionTrue(false);
           return mapping.findForward("paginaPrincipal");
       }
       
       //si todo salio bien entonces 
       formTran.reset();
       mundoTrans.reset();
	   mundoTrans.setInstitucion(usuario.getCodigoInstitucionInt());
	   formTran.setMapaTipos(mundoTrans.generarConsultaTransacciones(con));
	   logger.info("\n el valor de la consulta es--> "+formTran.getMapaTipos());
	   formTran.setPatronOrdenar("descripcion_");
	   this.accionOrdenarMapa(formTran);
	   formTran.setOperacionTrue(true);
	   UtilidadBD.closeConnection(con);
	   return mapping.findForward("paginaPrincipal");   
    }
    /**
	  * metodo para ingresar un registro vacio
     * @param con
     * @param formTran    
     * @return
     */
    private ActionForward accionIngresarNuevo(Connection con, TiposTransaccionesInvForm formTran, HttpServletResponse response,HttpServletRequest request,UsuarioBasico usuario) 
    {
        
        int nuevaPos=Integer.parseInt(formTran.getMapaTipos("numRegistros")+"");
        formTran.setMapaTipos("consecutivo_"+nuevaPos,"-1");
        formTran.setMapaTipos("descripcion_concepto_"+nuevaPos,"Seleccione");
        formTran.setMapaTipos("codigo_concepto_"+nuevaPos,"-1");
        formTran.setMapaTipos("descripcion_costo_"+nuevaPos,"Seleccione");
        formTran.setMapaTipos("codigo_costo_"+nuevaPos,"-1");
        formTran.setMapaTipos("codigo_"+nuevaPos,"");
        formTran.setMapaTipos("descripcion_"+nuevaPos,"");
        formTran.setMapaTipos("activo_"+nuevaPos,"true");
        formTran.setMapaTipos("indicativo_consignacion_"+nuevaPos,"");
        formTran.setMapaTipos("codigo_interfaz_"+nuevaPos,"");
        formTran.setMapaTipos("tipoReg_"+nuevaPos,"MEM");
        nuevaPos++;
        formTran.setMapaTipos("numRegistros",nuevaPos+"");        
        UtilidadBD.closeConnection(con);
		return this.redireccion(con, formTran, response, request, usuario);
    }
    /**
     * metodo para posicionarse en la ultima página del
     * pager
     * @param con
     * @param formTipos
     * @param response
     * @param request
     * @param usuario
     * @param enlace
     * @return
     */
    public ActionForward redireccion (	Connection con,
            											TiposTransaccionesInvForm formTipos,
						            					HttpServletResponse response,
						            					HttpServletRequest request,
						            					UsuarioBasico usuario)
    {
        int maxPageItems=Integer.parseInt(util.ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt()));
        int numRegistros=Integer.parseInt(formTipos.getMapaTipos("numRegistros")+"");
        formTipos.setOffset(((int)((numRegistros-1)/maxPageItems))*maxPageItems);
        if(request.getParameter("ultimaPage")==null)
        {
           if(numRegistros > (formTipos.getOffset()+maxPageItems))
               formTipos.setOffset(((int)(numRegistros/maxPageItems))*maxPageItems);
            try 
            {
                response.sendRedirect("insertarModificar.jsp"+"?pager.offset="+formTipos.getOffset());
            } catch (IOException e) 
            {                
                e.printStackTrace();
            }
        }
        else
        {    
            String ultimaPagina=request.getParameter("ultimaPage");            
            int posOffSet=ultimaPagina.indexOf("offset=")+7;           
            if(numRegistros>(formTipos.getOffset()+maxPageItems))
                formTipos.setOffset(formTipos.getOffset()+maxPageItems);            
            try 
            {                
                response.sendRedirect(ultimaPagina.substring(0,posOffSet)+formTipos.getOffset());
            } 
            catch (IOException e) 
            {                
                e.printStackTrace();
            }
     }
       UtilidadBD.closeConnection(con);
       return null;
    }
    /**
	 * 
	 * @param session
	 * @return
	 */
	private UsuarioBasico getUsuarioBasicoSesion(HttpSession session)
	{
	    UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
			if(usuario == null)
			    logger.warn("El usuario no esta cargado (null)");
			
			return usuario;
	}
}
