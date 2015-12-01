package com.princetonsa.action.glosas;

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
import util.ConstantesIntegridadDominio;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.UtilidadesFacturacion;

import com.princetonsa.actionform.glosas.DetalleGlosasForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.glosas.DetalleGlosas;
import com.princetonsa.mundo.glosas.RegistrarModificarGlosas;


public class DetalleGlosasAction extends Action
{
	private Logger logger = Logger.getLogger(DetalleGlosasAction.class);

	public ActionForward execute(   ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response ) throws Exception
			{	
		Connection con = null;
		try{
			if(form instanceof DetalleGlosasForm)
			{

				con = UtilidadBD.abrirConexion();

				//se verifica si la conexion esta nula
				if(con == null)
				{	
					// de ser asi se envia a una pagina de error. 
					request.setAttribute("CodigoDescripcionError","errors.problemasBd");
					return mapping.findForward("paginaError");
				}

				//se obtiene el usuario cargado en sesion.
				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

				//se obtiene el paciente cargado en sesion.
				PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");

				//se obtiene la institucion
				InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");

				//se instancia la forma
				DetalleGlosasForm forma = (DetalleGlosasForm)form;		

				//se instancia el mundo
				DetalleGlosas mundo = new DetalleGlosas();

				//optenemos el estado que contiene la forma.
				String estado = forma.getEstado();

				//se instancia la variable para manejar los errores.
				ActionErrors errores=new ActionErrors();

				forma.setMensaje(new ResultadoBoolean(false));

				//se instancia la variable para manejar los errores.


				logger.info("\n\n***************************************************************************");
				logger.info(" 	  EL ESTADO DE DETALLE GLOSAS ES ====>> "+estado);
				logger.info("\n***************************************************************************");

				// ESTADO --> NULL
				if(estado == null)
				{
					forma.reset(usuario.getCodigoInstitucionInt());
					request.setAttribute("CodigoDescripcionError","Errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);

					return mapping.findForward("paginaError");
				}			
				else if(estado.equals("empezar"))
				{
					return this.accionEmpezar(forma, mundo, con, mapping, usuario);					   			
				}
				else if (estado.equals("buscar"))
				{
					return this.accionBuscar(con, forma, mundo,mapping, usuario);
				}
				else if(estado.equals("mostrarGlosa"))
				{
					return this.accionMostrarGlosa(forma, mundo, con, mapping, usuario);
				}
				else if(estado.equals("detalleGlosa"))
				{
					return this.accionDetalleGlosa(forma, mundo, con, mapping, usuario);
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
	 * Metodo que consulta el detalle de la Glosa
	 * @param forma
	 * @param mundo
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionDetalleGlosa(DetalleGlosasForm forma,DetalleGlosas mundo, Connection con, ActionMapping mapping,UsuarioBasico usuario) 
	{
		//se consulta el detalle de la Glosa segun el codigo 
		forma.setMapaDetalleGlosa(RegistrarModificarGlosas.consultarDetalleGlosa(con, Utilidades.convertirAEntero(forma.getInformacionGlosa("codglosa")+""),usuario.getCodigoInstitucionInt()));

		for(int i=0;i<(Utilidades.convertirAEntero(forma.getMapaDetalleGlosa("numRegistros")+""));i++)
		{
			HashMap<String,Object> aux= new HashMap<String, Object>();
			aux=RegistrarModificarGlosas.consultarTodosConceptosFacturas(con, forma.getMapaDetalleGlosa("codigoaudi_"+i)+"");
			forma.getConceptosFactura().add(aux);
		}		
		
		//se consulta por cada factura si tiene historico de glosas 
		for(int i=0;i<(Utilidades.convertirAEntero(forma.getMapaDetalleGlosa("numRegistros")+""));i++)
		{
			forma.setMapaDetalleGlosa("mostrarLink_"+i, ConstantesBD.acronimoNo);
			HashMap<String,Object> resultados= new HashMap<String,Object>();
			resultados.putAll(RegistrarModificarGlosas.accionHistoricoGlosa(con, forma.getMapaDetalleGlosa("codfactura_"+i)+""));
			if((Utilidades.convertirAEntero(resultados.get("numRegistros")+"")) > 0)
				forma.setMapaDetalleGlosa("mostrarLink_"+i, ConstantesBD.acronimoSi);
			logger.info("\n\nMOSTRAR LINK--->"+forma.getMapaDetalleGlosa("mostrarLink_"+i)+"  COD FACT  "+forma.getMapaDetalleGlosa("codfactura_"+i));
		}	
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("informacionFacturas");
	}

	/**
     * Retorna el forward para mostrar la glosa 
     * @param forma
     * @param con
     * @param mapping
     * @return
     */
	private ActionForward accionMostrarGlosa(DetalleGlosasForm forma, DetalleGlosas mundo, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleGlosas");
	}
	
	/**
	 * Metodo para buscar Glosas
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward accionBuscar(Connection con,DetalleGlosasForm forma, DetalleGlosas mundo, ActionMapping mapping, UsuarioBasico usuario)
	{
		if(UtilidadTexto.getBoolean(ValoresPorDefecto.getValidarUsuarioGlosa(usuario.getCodigoInstitucionInt())))
			forma.setArregloConvenios(UtilidadesFacturacion.obtenerConvenioPorUsuario(con, usuario.getLoginUsuario(), 
				ConstantesIntegridadDominio.acronimoTipoUsuarioGlosa, false));
		else
			forma.setArregloConvenios(Utilidades.obtenerConvenios(con, "", "", false, "", false));
		forma.setConvenio("");
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleGlosas");
	}
	
	/**
     * Inicia en el forward de Detalle Glosas 
     * @param forma
     * @param con
     * @param mapping
     * @return
     */
	private ActionForward accionEmpezar(DetalleGlosasForm forma, DetalleGlosas mundo, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		forma.reset(usuario.getCodigoInstitucionInt());
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleGlosas");
	}
}