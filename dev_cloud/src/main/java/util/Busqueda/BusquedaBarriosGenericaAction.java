/*
 * Jun 15, 2007
 */
package util.Busqueda;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
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

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.BusquedaBarriosGenerica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;


/**
 * @author Sebastián Gómez Rivillas
 * Action, controla todas las opciones dentro de la busqueda de barrios genérica 
 * incluyendo los posibles casos de error y los casos de flujo.
 *
 */
public class BusquedaBarriosGenericaAction extends Action 
{
	/**
	    * Objeto para manejar los logs de esta clase
	    */
	    private Logger logger = Logger.getLogger(BusquedaBarriosGenericaAction.class);
		
		/**
		 * Método encargado de el flujo y control de la funcionalidad
		 * 
		 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
		 */
		public ActionForward execute(	ActionMapping mapping,
														ActionForm form,
														HttpServletRequest request,
														HttpServletResponse response ) throws Exception
														{
			Connection con=null;
			try {
				if (response==null); //Para evitar que salga el warning
				if(form instanceof BusquedaBarriosGenericaForm)
				{

					//SE ABRE CONEXION
					try
					{
						con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
					}
					catch(SQLException e)
					{
						logger.warn("No se pudo abrir la conexión"+e.toString());
					}

					//OBJETOS A USAR
					BusquedaBarriosGenericaForm busquedaForm =(BusquedaBarriosGenericaForm)form;
					HttpSession session=request.getSession();		
					UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
					PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
					String estado=busquedaForm.getEstado(); 
					logger.warn("estado BusquedaBarriosGenericaAction-->"+estado);

					if( usuario==null || paciente==null)
					{
						UtilidadBD.cerrarConexion(con);
						return mapping.findForward("cerrarDiagnostico");
					}

					if(estado == null)
					{
						busquedaForm.reset();	
						logger.warn("Estado no valido dentro del flujo de Busqueda Barrios Genérica (null) ");
						request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
						return mapping.findForward("paginaError");
					}
					else if (estado.equals("empezar"))
					{
						return accionEmpezar(con,busquedaForm,mapping);
					}
					else if (estado.equals("ordenar"))
					{
						return accionOrdenar(con,busquedaForm,mapping);
					}
					else
					{
						busquedaForm.reset();
						UtilidadBD.closeConnection(con);
						logger.warn("Estado no valido dentro del flujo de Busqueda Barrios Generica (null) ");
						request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
						return mapping.findForward("paginaError");
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
		 * Método implementado para realizar la ordenación de barrios
		 * @param con
		 * @param busquedaForm
		 * @param mapping
		 * @return
		 */
		private ActionForward accionOrdenar(Connection con, BusquedaBarriosGenericaForm busquedaForm, ActionMapping mapping) 
		{
			String[] indices={				
					"codigo_",
					"codigoBarrio_",
					"nombreBarrio_",				
					"codigoLocalidad_",
					"nombreLocalidad_"
				};
			logger.info("INICIO ORDENACION!!!");
			HashMap<String, Object> listado = new HashMap<String, Object>();
			
			//Se pasa de ArrayList a HashMap
			int numRegistros = busquedaForm.getBarrios().size();
			listado.put("numRegistros",busquedaForm.getBarrios().size());
			
			for(int i=0;i<busquedaForm.getBarrios().size();i++)
			{
				HashMap elemento = (HashMap)busquedaForm.getBarrios().get(i);
				listado.put("codigo_"+i,elemento.get("codigo"));
				listado.put("codigoBarrio_"+i,elemento.get("codigoBarrio"));
				listado.put("nombreBarrio_"+i,elemento.get("nombreBarrio"));
				listado.put("codigoLocalidad_"+i,elemento.get("codigoLocalidad"));
				listado.put("nombreLocalidad_"+i,elemento.get("nombreLocalidad"));
			}
			logger.info("FIN TRANSCRIPCION 1!!!");
			listado = Listado.ordenarMapa(indices, busquedaForm.getIndice(), busquedaForm.getUltimoIndice(), listado, numRegistros);
			logger.info("FIN LLAMADO FUNCION!!!");
			
			busquedaForm.setBarrios(new ArrayList<HashMap<String,Object>>());
			
			//Se pasa de HashMap a arraylist
			for(int i=0;i<numRegistros;i++)
			{
				HashMap<String,Object> elemento = new HashMap<String, Object>();
				elemento.put("codigo",listado.get("codigo_"+i));
				elemento.put("codigoBarrio",listado.get("codigoBarrio_"+i));
				elemento.put("nombreBarrio",listado.get("nombreBarrio_"+i));
				elemento.put("codigoLocalidad",listado.get("codigoLocalidad_"+i));
				elemento.put("nombreLocalidad",listado.get("nombreLocalidad_"+i));
				busquedaForm.getBarrios().add(elemento);
			}
			logger.info("FIN TRANSCRIPCION 2!!!");
			busquedaForm.setUltimoIndice(busquedaForm.getIndice());
			logger.info("FINALIZO ORDENACION!!!");
			busquedaForm.setEstado("empezar");
			UtilidadBD.closeConnection(con);
			return mapping.findForward("listarBarrios");
		}

		/**
		 * Método implementado para realizar la consulta de los diagnosticos
		 * @param con
		 * @param busquedaForm
		 * @param mapping
		 * @return
		 */
		private ActionForward accionEmpezar(Connection con, BusquedaBarriosGenericaForm busquedaForm, ActionMapping mapping) 
		{
			//Consulta de los barrios
			busquedaForm.setBarrios(BusquedaBarriosGenerica.consultar(
				con, 
				busquedaForm.getCodigoPais(), 
				busquedaForm.getCodigoDepartamento(), 
				busquedaForm.getCodigoCiudad(), 
				busquedaForm.getCriterioBarrio()));
			
			busquedaForm.setNumRegistros(busquedaForm.getBarrios().size());
			
			UtilidadBD.closeConnection(con);
			return mapping.findForward("listarBarrios");
		}

}
