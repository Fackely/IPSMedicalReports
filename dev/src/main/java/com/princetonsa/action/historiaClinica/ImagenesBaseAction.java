 	package com.princetonsa.action.historiaClinica;

import java.io.File;
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
import util.UtilidadClonar;

import com.princetonsa.actionform.historiaClinica.ImagenesBaseForm;
import com.princetonsa.dto.historiaClinica.DtoImagenBase;
import com.princetonsa.enu.general.CarpetasArchivos;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.historiaClinica.ImagenesBase;

/**
 * @author Jairo Gómez Fecha Septiembre de 2009
 */
public class ImagenesBaseAction extends Action {

	/**
	 * Objeto para manejar los logs de esta clase
	 */
	Logger logger = Logger.getLogger(ImagenesBaseAction.class);

	private ImagenesBase mundo = new ImagenesBase();

	/**
	 * Método execute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		Connection connection = null;
		try {
			if (form instanceof ImagenesBaseForm) {

				connection = UtilidadBD.abrirConexion();

				if (connection == null) {
					request.setAttribute("CodigoDescripcionError", "erros.problemasBd");
					return mapping.findForward("paginaError");
				}

				UsuarioBasico usuario = (UsuarioBasico) request.getSession().getAttribute("usuarioBasico");
				ImagenesBaseForm forma = (ImagenesBaseForm) form;
				String estado = forma.getEstado();

				logger.info("\n\n***************************************************************************");
				logger.info("EL ESTADO DE CitasNoRealizadasForm ES ====>> " + forma.getEstado());
				logger.info("\n*****************************************************************************");

				/**
				 * estados Imágenes Base
				 */
				forma.setEsOperacionExitosa("");
				if (estado == null) {
					request.setAttribute("CodigoDescripcionError","errors.estadoInvalido");
					UtilidadBD.cerrarConexion(connection);
					return mapping.findForward("paginaError");
				}
				else if (estado.equals("empezar")) 
				{
					forma.reset();
					return mapping.findForward("menu");
				}
				else if (estado.equals("consultar"))
				{
					forma.setEsConsulta(ConstantesBD.acronimoSi);
					return accionConsultar(forma, connection, mapping, mundo);
				}
				else if (estado.equals("ingresarModificar"))
				{
					return accionIngresarModificar(forma, connection, mapping, mundo);
				}
				else if (estado.equals("ingresar"))
				{
					return accionIngresar(forma, mapping);
				}
				else if (estado.equals("guardar"))
				{
					forma.setEsConsulta(ConstantesBD.acronimoNo);
					return accionGuardar(forma, mapping, usuario, mundo, connection);
				}
				else if (estado.equals("eliminar"))
				{
					return accionEliminar(forma, mapping, connection, mundo);
				}
				else if (estado.equals("modificar"))
				{
					return accionModificar(forma, mapping, usuario, mundo, connection);
				}
				else if (estado.equals("guardarModificar"))
				{
					return accionGuardarModificar(forma, mapping, usuario, mundo, connection);
				}
				else if (estado.equals("detalle"))
				{

					return accionDetalle(forma, mapping, usuario, mundo, connection);
				}else if (estado.equals("redireccion")) {
					response.sendRedirect(forma.getLinkSiguiente());
					UtilidadBD.closeConnection(connection);
					return null;

				}
			}
			return null;
		} catch (Exception e) {
			Log4JManager.error(e);
			return null;
		}
		finally{
			UtilidadBD.closeConnection(connection);
		}
	}
	
	private ActionForward accionDetalle(ImagenesBaseForm forma, ActionMapping mapping, UsuarioBasico usuario, ImagenesBase mundo, Connection connection)
	{
//		logger.info("ruta: "+CarpetasArchivos.IMAGENES_BASE.getRutaFisica()+forma.getArrayImagenes().get(forma.getPosArrayDto()).getImagen());
//		ImageIcon img = new ImageIcon(CarpetasArchivos.IMAGENES_BASE.getRutaFisica()+forma.getArrayImagenes().get(forma.getPosArrayDto()).getImagen());
//		logger.info("largo:"+img.getIconHeight());
//		logger.info("Ancho:"+img.getIconWidth());
		forma.setDtoImagen(forma.getArrayImagenes().get(forma.getPosArrayDto()));
		return mapping.findForward("detalle");
	}

	private ActionForward accionGuardarModificar(ImagenesBaseForm forma, ActionMapping mapping, UsuarioBasico usuario, ImagenesBase mundo, Connection connection)
	{
		mundo.clean();
		mundo.setDtoImagenBase(forma.getDtoImagen());
		mundo.getDtoImagenBase().setImagen(forma.getDocumentosAdjuntosGenerados().get("generado_0")+"");
		forma.setEsOperacionExitosa(mundo.actualizarImagenesBase(connection, usuario));
		forma.setEstado("ingresarModificar");
		return accionIngresarModificar(forma, connection, mapping, mundo);
	}

	private ActionForward accionModificar(ImagenesBaseForm forma, ActionMapping mapping, UsuarioBasico usuario, ImagenesBase mundo, Connection connection)
	{
		
		forma.setDtoImagen((DtoImagenBase)UtilidadClonar.clone(forma.getArrayImagenes().get(forma.getPosArrayDto())));
		forma.setDocumentosAdjuntosGenerados("generado_0", forma.getDtoImagen().getImagen());
		forma.setDocumentosAdjuntosGenerados("original_0", forma.getDtoImagen().getImagen());
		try {
			UtilidadBD.cerrarConexion(connection);
		} catch (Exception e) {
			logger.error("error cerrado la conexion ---> "+e);
		}
		return mapping.findForward("listarModificar");
	}

	private ActionForward accionEliminar(ImagenesBaseForm forma, ActionMapping mapping, Connection connection, ImagenesBase mundo)
	{
		forma.setEsOperacionExitosa("");
		forma.setEsOperacionExitosa(mundo.eliminarImagenBase(connection, forma.getArrayImagenes().get(forma.getPosArrayDto()).getCodigoPk()));
		try {
			File file = new File(CarpetasArchivos.IMAGENES_BASE.getRutaFisica()+forma.getArrayImagenes().get(forma.getPosArrayDto()).getImagen());  
			if(file.delete())
				logger.info("la imagen fue eliminada satisfactoriamente Fisicamente");
			else
				logger.info("la imagen NO fue eliminada Fisicamente");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		forma.setEstado("ingresarModificar");
		return accionIngresarModificar(forma, connection, mapping, mundo);
	}

	private ActionForward accionGuardar(ImagenesBaseForm forma, ActionMapping mapping, UsuarioBasico usuario, ImagenesBase mundo, Connection connection)
	{
		mundo.clean();
		mundo.getDtoImagenBase().setImagen(forma.getDocumentosAdjuntosGenerados().get("generado_0")+"");
		mundo.getDtoImagenBase().setNombreImagen(forma.getDtoImagen().getNombreImagen());
		forma.setEsOperacionExitosa(mundo.insertarImagenesBase(connection, usuario));
		forma.setEstado("ingresarModificar");
		return accionIngresarModificar(forma, connection, mapping, mundo);
	}

	private ActionForward accionIngresarModificar(ImagenesBaseForm forma, Connection connection, ActionMapping mapping, ImagenesBase mundo2)
	{
		forma.getDocumentosAdjuntosGenerados().clear();
		forma.setEsConsulta(ConstantesBD.acronimoNo);
		forma.setArrayImagenes(mundo.consultarImagenesBase(connection));
		try {
			UtilidadBD.cerrarConexion(connection);
		} catch (Exception e) {
			logger.error("error cerrado la conexion ---> "+e);
		}
		return mapping.findForward("listarModificar");
	}

	private ActionForward accionIngresar(ImagenesBaseForm forma, ActionMapping mapping) {
		forma.getDtoImagen().clean();
		forma.setDocumentosAdjuntosGenerados("generado_0", "");
		forma.setDocumentosAdjuntosGenerados("original_0", "");
		return mapping.findForward("listarModificar");
	}

	private ActionForward accionConsultar(ImagenesBaseForm forma, Connection connection, ActionMapping mapping, ImagenesBase mundo ) 
	{
		forma.getDocumentosAdjuntosGenerados().clear();
		forma.setArrayImagenes(mundo.consultarImagenesBase(connection));
		try {
			UtilidadBD.cerrarConexion(connection);
		} catch (Exception e) {
			logger.error("error cerrado la conexion ---> "+e);
		}
		return mapping.findForward("listarModificar");
	}
}