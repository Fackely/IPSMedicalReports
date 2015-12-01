package util.Busqueda;

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
import util.ResultadoBoolean;
import util.UtilidadBD;

import com.princetonsa.mundo.BusquedaConciliaciones;
import com.princetonsa.mundo.BusquedaRespuestaGlosas;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;


public class BusquedaConciliacionesAction extends Action
{
	/*
	 * Variable para el manejo de Logs
	 */
	private Logger logger = Logger.getLogger(BusquedaConciliacionesAction.class);
	
	public ActionForward execute(   ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response ) throws Exception
			{
		Connection con = null;

		try {

			if(form instanceof BusquedaConciliacionesForm)
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
				BusquedaConciliacionesForm forma = (BusquedaConciliacionesForm)form;

				//se instancia el mundo
				BusquedaConciliaciones mundo = new BusquedaConciliaciones();

				//optenemos el estado que contiene la forma.
				String estado = forma.getEstado();

				//se instancia la variable para manejar los errores.
				ActionErrors errores=new ActionErrors();

				forma.setMensaje(new ResultadoBoolean(false));



				logger.info("\n\n***************************************************************************");
				logger.info(" 	  EL ESTADO DE BUSQUEDA RESPUESTA GLOSAS ES ====>> "+estado);
				logger.info("\n***************************************************************************");


				// ESTADO --> NULL
				if(estado == null)
				{
					forma.reset(usuario.getCodigoInstitucionInt());
					request.setAttribute("CodigoDescripcionError","Errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);

					return mapping.findForward("paginaError");
				}
				else if(estado.equals("consultarConciliaciones"))
				{
					return this.accionConsultar(forma, mundo, con, mapping, usuario, request);					   			
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
     * Inicia en el forward de Confirmar Anular Glosas 
     * @param forma
     * @param con
     * @param mapping
     * @return
     */
	private ActionForward accionConsultar(BusquedaConciliacionesForm forma, BusquedaConciliaciones mundo, Connection con, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		forma.reset(usuario.getCodigoInstitucionInt());
		
		HashMap<String, Object> mapa = new HashMap<String, Object>();
		mapa.put("convenio", forma.getConvenio());
		mapa.put("conciliacion", forma.getConciliacion());
		mapa.put("fechaInicial", forma.getFechaInicial());
		mapa.put("fechaFinal", forma.getFechaFinal());
		if(!forma.getConceptoConciliacion().equals("-1"))
			mapa.put("conceptoConciliacion", forma.getConceptoConciliacion().split(ConstantesBD.separadorSplit)[0]);
		else
			mapa.put("conceptoConciliacion", forma.getConceptoConciliacion());
		mapa.put("nroActa", forma.getNroActa());
		
		forma.setMapaConciliaciones(mundo.consultarConciliaciones(mapa));
			
		forma.resetParametros(usuario.getCodigoInstitucionInt());
		
		return mapping.findForward("busquedaConciliaciones");
	}
}