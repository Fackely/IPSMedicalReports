package util.Busqueda;

import java.sql.Connection;

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

import com.princetonsa.mundo.BusquedaConvencionesOdontologicas;
import com.princetonsa.mundo.UsuarioBasico;

public class BusquedaConvencionesOdontologicasAction extends Action {

	/**
	 * Objeto para manejar el log de la clase
	 * */
	Logger logger = Logger.getLogger(BusquedaConvencionesOdontologicasAction.class);
	BusquedaConvencionesOdontologicas mundo=new BusquedaConvencionesOdontologicas();
	
	public ActionForward execute(ActionMapping mapping,
			 ActionForm form,
			 HttpServletRequest request,
			 HttpServletResponse response) throws Exception 
			 {

		Connection con= null;

		try {

			if(response==null);

			if(form instanceof BusquedaConvencionesOdontologicasForm)
			{

				con = UtilidadBD.abrirConexion();

				if(con == null)
				{
					request.setAttribute("CodigoDescripcionError","errors.probelmasBd");
					return mapping.findForward("paginaError");
				}

				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				BusquedaConvencionesOdontologicasForm forma = (BusquedaConvencionesOdontologicasForm)form;
				String estado = forma.getEstado();

				if(estado == null)
				{
					logger.warn("\n\n Estado no Valido dentro del Flujo de Busqueda Convencion Odontologica (null)");
					request.setAttribute("CodigoDescripcionError","errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}

				else if(estado.equals("busqueda"))
				{   
					logger.info("entra a este estado de busqueda");
					logger.info("valor id Div: "+forma.getIdDiv());

					forma.resetRequest(request);
					if(forma.getTipoConsulta()==ConstantesBD.codigoBusquedaGenericaConvenciones)
						this.accionConsultarConvencionesOdontologicas(forma,usuario.getCodigoInstitucionInt());
					else if(forma.getTipoConsulta()==ConstantesBD.codigoBusquedaGenericaImagenesBase)
						accionConsultarImagenesBase(usuario, forma);
					else if(forma.getTipoConsulta()==ConstantesBD.codigoBusquedaGenericaTramas)
						this.accionConsultarTramasConvencionesOdontologicas(forma);				    
					else if(forma.getTipoConsulta()==ConstantesBD.codigoBusquedaGenericaImagenes)
						this.accionConsultarImagenesConvencionesOdontologicas(forma);

					return mapping.findForward("listarConvenciones");

				}else if(estado.equals("asignarDatos"))
				{
					String Url= "";

				}else if(estado.equals("detalleImagen"))
				{
					logger.info("entra a mostrar el detalle de la iamgen");
					logger.info("codigo: "+forma.getCodigoImagen());
					logger.info("path: "+forma.getPathImagen());
					logger.info("nombre: "+forma.getNomImagen());
					return mapping.findForward("detalleImagen");
				}
				else if (estado.equals("redireccion"))
				{
					UtilidadBD.closeConnection(con);
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
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
	 */
	private void accionConsultarImagenesConvencionesOdontologicas(BusquedaConvencionesOdontologicasForm forma) {
		
	    	forma.setArrayConvenciones(mundo.consultarImagenesConvencionesOdontologicas());
	}
	/**
	 * 
	 * @param usuario
	 * @param forma
	 */
	private void accionConsultarImagenesBase(UsuarioBasico usuario,
			BusquedaConvencionesOdontologicasForm forma) {
		forma.setArrayConvenciones(mundo.consultarImagenesBase(usuario.getCodigoInstitucionInt()));
	}


    /**
     * 
     * @param forma
     */
 	private void accionConsultarTramasConvencionesOdontologicas(BusquedaConvencionesOdontologicasForm forma) {
		
		forma.setArrayConvenciones(mundo.consultarTramasConvencionesOdontologicas());
	}
 
    /**
     * 
     * @param forma
     * @param codigoInstitucionInt
     */
	private void accionConsultarConvencionesOdontologicas(BusquedaConvencionesOdontologicasForm forma,int codigoInstitucionInt) 
	{
		
	  	
	  forma.setArrayConvenciones(mundo.consultarConvencionesOdontologicas(codigoInstitucionInt,forma.isBusquedaXTipo(),forma.getTipoConvencion()));   	
	
	}
}
