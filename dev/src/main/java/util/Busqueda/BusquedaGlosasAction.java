package util.Busqueda;

import java.sql.Connection;
import java.text.Normalizer.Form;
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
import util.Listado;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.Utilidades;

import com.princetonsa.mundo.BusquedaGlosas;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;



public class BusquedaGlosasAction extends Action
{
	/*
	 * Variable para manejo de loggs
	 */
	private Logger logger= Logger.getLogger(BusquedaGlosasAction.class);
	
	public ActionForward execute(   ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response ) throws Exception
			{

		Connection con = null;
		try {
			if(form instanceof BusquedaGlosasForm)
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
				BusquedaGlosasForm forma = (BusquedaGlosasForm)form;		

				//se instancia el mundo
				BusquedaGlosas mundo = new BusquedaGlosas();

				//optenemos el estado que contiene la forma.
				String estado = forma.getEstado();

				//se instancia la variable para manejar los errores.
				ActionErrors errores=new ActionErrors();

				forma.setMensaje(new ResultadoBoolean(false));



				logger.info("\n\n***************************************************************************");
				logger.info(" 	  EL ESTADO DE BUSQUEDA GLOSAS ES ====>> "+estado);
				logger.info("\n***************************************************************************");


				// ESTADO --> NULL
				if(estado == null)
				{
					forma.reset(usuario.getCodigoInstitucionInt());
					request.setAttribute("CodigoDescripcionError","Errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);

					return mapping.findForward("paginaError");
				}			
				else
					if(estado.equals("consultarGlosas"))
					{
						return this.accionConsultar(forma, mundo, con, mapping, usuario);					   			
					}
					else
						/*------------------------------
						 * 		ESTADO ==> ORDENAR
					 -------------------------------*/
						if (estado.equals("ordenar"))
						{
							accionOrdenarMapa(forma);
							UtilidadBD.closeConnection(con);
							return mapping.findForward("busquedaGlosas");
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
	private void accionOrdenarMapa(BusquedaGlosasForm forma) {
		String[] indices = (String[])forma.getGlosasMap("INDICES_MAPA");
		int numReg = Utilidades.convertirAEntero(forma.getGlosasMap("numRegistros")+"");
		if (!forma.getGlosasMap().get("numRegistros").equals("0"))
		{
			logger.info("\n\n\n********IMPRIMO EL MAPA ESTE*******");
			Utilidades.imprimirMapa(forma.getGlosasMap());
			forma.setGlosasMap(Listado.ordenarMapa(indices, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getGlosasMap(), numReg));
			forma.setUltimoPatron(forma.getPatronOrdenar());
			forma.setGlosasMap("numRegistros",numReg+"");
			forma.setGlosasMap("INDICES_MAPA",indices);
		}
		else
		{
			forma.setEstado("consultarGlosas");
		}
	}
	
	/**
     * Inicia en el forward de Confirmar Anular Glosas 
     * @param forma
     * @param con
     * @param mapping
     * @return
     */
	private ActionForward accionConsultar(BusquedaGlosasForm forma, BusquedaGlosas mundo, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		forma.reset(usuario.getCodigoInstitucionInt());
		HashMap<String, Object> criterios = new HashMap<String, Object>();
		criterios.put("fechaRegIni", forma.getFechaRegIni());
		criterios.put("fechaRegFin", forma.getFechaRegFin());
		criterios.put("glosa", forma.getGlosa());
		criterios.put("glosaEntidad", forma.getGlosaEntidad());
		criterios.put("fechaRad", forma.getFechaRadicacion());
		criterios.put("estado", forma.getEstadoGlosa());
		criterios.put("consecutivoFactura", forma.getConsecutivoFactura());
		criterios.put("nombreFormaRemite", forma.getNombreFormaRemite());
				
		if(forma.getNombreFormaRemite().equals("formaRegistrarModificarGlosas"))
		{				
			if(forma.getNumRegConvenios() > 0)
			{				
				for(int i=0; i< forma.getNumRegConvenios();i++)
				{					
					criterios.put("convenio_"+i, forma.getConveniosSplit().split(ConstantesBD.separadorSplit)[i]);
				}
				criterios.put("convenio", "");
				criterios.put("numConvenios", forma.getNumRegConvenios());
			}
			else
			{
				criterios.put("convenio", forma.getConvenio());
			}
		}
		else
			criterios.put("convenio", forma.getConvenio());
					
		forma.setGlosasMap(mundo.consultarGlosas(con, criterios));		
				
		forma.resetParametros(usuario.getCodigoInstitucionInt());
		UtilidadBD.closeConnection(con);
		return mapping.findForward("busquedaGlosas");
	}
}