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

import util.Listado;
import util.UtilidadBD;
import util.Utilidades;

import com.princetonsa.actionform.inventarios.MotDevolucionInventariosForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.MotDevolucionInventarios;

/**
 * @version 1.0, 23/11/2005
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velásquez/a>
 */
public class MotDevolucionInventariosAction extends Action
{
    /**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(MotDevolucionInventariosAction.class);
	
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


			if(form instanceof MotDevolucionInventariosForm)
			{


				//intentamos abrir una conexion con la fuente de datos 
				con = UtilidadBD.abrirConexion();
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}



				MotDevolucionInventariosForm forma=(MotDevolucionInventariosForm)form;
				MotDevolucionInventarios mundo=new MotDevolucionInventarios();
				UsuarioBasico usuario = null;
				usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());

				String estado = forma.getEstado();

				logger.info("Estado: "+estado);

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de ConceptoTesoreriaAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}
				if(estado.equals("empezar"))
				{
					this.accionEmpezar(con,forma,mundo,usuario);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaPrincipal");
				}
				if(estado.equals("eliminar"))
				{
					this.accionEliminar(forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaPrincipal");
				}
				if(estado.equals("nuevo"))
				{
					this.accionNuevo(forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaPrincipal");
				}
				if(estado.equals("ordenar"))
				{
					this.accionOrdenar(forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaPrincipal");
				}
				if(estado.equals("guardar"))
				{
					this.accionGuardar(con,forma,mundo,usuario);
					UtilidadBD.closeConnection(con);
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
				logger.error("El form no es compatible con el form de ConceptoTesoreriaForm");
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
     * @param con
     * @param forma
     * @param mundo
     * @param usuario
     */
    private void accionGuardar(Connection con, MotDevolucionInventariosForm forma, MotDevolucionInventarios mundo, UsuarioBasico usuario)
    {
       mundo.guardarMotivosDevolucionInventarios(con,(HashMap)forma.getMotivos().clone(),forma.getInstitucion(),usuario);
       forma.reset();
       this.accionEmpezar(con, forma, mundo, usuario);
    }



    /**
     * @param forma
     */
    private void accionOrdenar(MotDevolucionInventariosForm forma)
    {
        String[] indices={
        		"codigo_",
        		"descripcion_",
        		"activo_",
        		"bd_",
        		"eliminado_"
        		};
        int numReg=Integer.parseInt(forma.getMotivos("numRegistros")+"");
		forma.setMotivos(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getMotivos(),numReg));
		forma.setMotivos("numRegistros", numReg+"");
		forma.setUltimoPatron(forma.getPatronOrdenar());
    }




    /**
     * @param forma
     */
    private void accionNuevo(MotDevolucionInventariosForm forma)
    {
        int pos=Integer.parseInt(forma.getMotivos("numRegistros")+"");
        forma.setMotivos("codigo_"+pos,"");
        forma.setMotivos("descripcion_"+pos,"");
        forma.setMotivos("activo_"+pos,"true");
        forma.setMotivos("eliminado_"+pos,"false");
        forma.setMotivos("bd_"+pos,"false");
        forma.setMotivos("numRegistros",(pos+1)+"");
    }



    /**
     * @param con
     * @param forma
     */
    private void accionEliminar(MotDevolucionInventariosForm forma)
    {
        forma.setMotivos("eliminado_"+forma.getPosEliminar(),"true");
    }


    /**
     * @param con
     * @param forma
     * @param mundo
     * @param usuario
     */
    private void accionEmpezar(Connection con, MotDevolucionInventariosForm forma, MotDevolucionInventarios mundo, UsuarioBasico usuario)
    {
        mundo.consultarMotivosDevolucionInventario(con, usuario.getCodigoInstitucionInt());
        forma.setMotivos((HashMap)mundo.getMotivos().clone());
    }
}
