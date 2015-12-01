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

import util.Listado;
import util.ResultadoBoolean;
import util.UtilidadBD;

import com.princetonsa.mundo.BusquedaRespuestaGlosas;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * Clase para la Busqueda Generica de Respuestas Glosas
 * @author Diego Fernando Bedoya
 *
 */
public class BusquedaRespuestaGlosasAction extends Action
{
	/**
	 * Variable para el manejo de Logs
	 */
	private Logger logger = Logger.getLogger(BusquedaRespuestaGlosasAction.class);
	
	public ActionForward execute(   ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response ) throws Exception
			{

		Connection con = null;
		try { 

			if(form instanceof BusquedaRespuestaGlosasForm)
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
				BusquedaRespuestaGlosasForm forma = (BusquedaRespuestaGlosasForm)form;

				//se instancia el mundo
				BusquedaRespuestaGlosas mundo = new BusquedaRespuestaGlosas();

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
				else if(estado.equals("consultarRespuestaGlosas"))
				{
					return this.accionConsultar(forma, mundo, con, mapping, usuario, request);					   			
				}
				else
					/*------------------------------
					 * 		ESTADO ==> ORDENAR
				 -------------------------------*/
					if (estado.equals("ordenar"))
					{
						accionOrdenarMapa(forma);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("busquedaRespuestas");
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
	 * Metodo para ordenar el Mapa Glosas
	 * @param forma
	 */
	private void accionOrdenarMapa(BusquedaRespuestaGlosasForm forma) {    	
		String[] indices = (String[])forma.getRespuestasMap("INDICES");
		int numReg = Integer.parseInt(forma.getRespuestasMap("numRegistros")+"");
		forma.setRespuestasMap(Listado.ordenarMapa(indices, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getRespuestasMap(), numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setRespuestasMap("numRegistros",numReg+"");
		forma.setRespuestasMap("INDICES",indices);
	}
	
	/**
     * Inicia en el forward de Confirmar Anular Glosas 
     * @param forma
     * @param con
     * @param mapping
     * @return
     */
	private ActionForward accionConsultar(BusquedaRespuestaGlosasForm forma, BusquedaRespuestaGlosas mundo, Connection con, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		forma.reset(usuario.getCodigoInstitucionInt());
		HashMap<String, Object> mapa = new HashMap<String, Object>();
		mapa.put("respuesta", forma.getRespuesta());
		mapa.put("fecharespini", forma.getFechaRespIni());
		mapa.put("fecharespfin", forma.getFechaRespFin());
		mapa.put("glosa", forma.getGlosa());
		mapa.put("glosaentidad", forma.getGlosaEntidad());
		mapa.put("convenio", forma.getConvenio());
		mapa.put("consecutivoFac", forma.getConsecutivoFactura());
		mapa.put("fecharad", forma.getFechaRadicacion());
		mapa.put("estadoglosa", forma.getEstadoGlosa());
		mapa.put("estadorespuesta", forma.getEstadoRespuesta());
		mapa.put("nombreFormaRemite", request.getParameter("nombreFormaRemite"));
		
		forma.setRespuestasMap(mundo.consultarRespuestas(con, mapa));
		forma.resetParametros(usuario.getCodigoInstitucionInt());
		UtilidadBD.closeConnection(con);
		return mapping.findForward("busquedaRespuestas");
	}
}