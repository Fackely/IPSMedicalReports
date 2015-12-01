package com.princetonsa.action;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesIntegridadDominio;
import util.Listado;
import util.UtilidadBD;
import util.UtilidadSesion;
import util.UtilidadValidacion;
import util.historiaClinica.UtilidadesHistoriaClinica;

import com.princetonsa.actionform.PacientesUrgenciasSalaEsperaForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.ObservableBD;
import com.princetonsa.mundo.PacientesUrgenciasSalaEspera;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;

public class PacientesUrgenciasSalaEsperaAction extends Action
{
	/**
	 * Objeto para almacenar los logs que aparezcan en el Action 
	 */
	private Logger logger= Logger.getLogger(PacientesUrgenciasSalaEsperaAction.class);

	/**
	 * Método excute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con=null;
		try{
			if(form instanceof PacientesUrgenciasSalaEsperaForm)
			{

				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexión"+e.toString());
				}

				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
				PacientesUrgenciasSalaEsperaForm forma = (PacientesUrgenciasSalaEsperaForm)form;
				PacientesUrgenciasSalaEspera mundo = new PacientesUrgenciasSalaEspera();

				String estado = forma.getEstado();
				logger.warn("[PacientesUrgenciasSalaEsperaAction] estado->"+estado);

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de PacientesUrgenciasSalaEsperaAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}

				else if(estado.equals("empezar"))
				{
					return this.accionEmpezar(forma, mapping, usuario, con, mundo);
				}
				else if(estado.equals("ordenarColumna"))
				{
					return this.accionOrdenarColumna(con, forma, mapping, usuario, mundo);
				}
				else if(estado.equals("preingresar"))
				{
					UtilidadSesion.notificarCambiosObserver(Integer.parseInt(forma.getMapaPacientes("codigopaciente_"+forma.getPosicionMapa())+""), getServlet().getServletContext());

					/**para cargar el paciente que corresponda**/
					paciente.setCodigoPersona((Integer.parseInt(forma.getMapaPacientes("codigopaciente_"+forma.getPosicionMapa())+"")));
					paciente.cargar(con,(Integer.parseInt(forma.getMapaPacientes("codigopaciente_"+forma.getPosicionMapa())+"")));
					paciente.cargarPaciente(con, (Integer.parseInt(forma.getMapaPacientes("codigopaciente_"+forma.getPosicionMapa())+"")),usuario.getCodigoInstitucion(),usuario.getCodigoCentroAtencion()+"");
					this.setObservable(paciente);


					ActionErrors errores = new ActionErrors();
					if(!UtilidadesHistoriaClinica.obtenerEstadoIngreso(con, paciente.getCodigoIngreso()).getCodigo().trim().equals(ConstantesIntegridadDominio.acronimoEstadoAbierto))
					{
						errores.add("errors.ingresoEstadoDiferenteAbierto", new ActionMessage("errors.ingresoEstadoDiferenteAbierto"));
					}
					if(UtilidadValidacion.esCuentaValida(con, paciente.getCodigoCuenta())<1)
					{
						errores.add("errors.paciente.cuentaNoValida", new ActionMessage("errors.paciente.cuentaNoValida"));
					}
					UtilidadBD.cerrarConexion(con);
					if(errores.isEmpty())
						response.sendRedirect("../valoracionesDummy/valoraciones.do?estado=empezar");
					else
					{
						saveErrors(request, errores);
						return mapping.findForward("paginaPrincipal");		
					}

				}
				else if (estado.equals("redireccion"))
				{
					UtilidadBD.closeConnection(con);
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
				}
				else
				{
					UtilidadBD.cerrarConexion(con);
				}
			}
			else
			{
				logger.error("El form no es compatible con el form ");
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
	 * Action para empezar la funcionalidad con los datos requeridos
	 * @param forma
	 * @param mapping
	 * @param usuario
	 * @param con
	 * @param mundo
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionEmpezar (PacientesUrgenciasSalaEsperaForm forma, ActionMapping mapping, UsuarioBasico usuario, Connection con, PacientesUrgenciasSalaEspera mundo) throws SQLException
	{
		forma.setMapaPacientes(mundo.consultarPacientesUrgSalaEspera(con, usuario.getCodigoCentroAtencion(), usuario.getCodigoInstitucionInt()));
		String[] indices = {
	            "codigoadmision_",
				"fechaadmision_",
				"horaadmision_",
				"fechahoraadmision_",
				"codigopaciente_",
				"idcuenta_",
				"nombrepaciente_",
				"tipoid_",
				"numeroid_",
				"calificaciontriage_",
				"nombrecolor_",
				"tiemposalaespera_",
				"descripcion_",//69474
				"nombremedico_"//134568 
					
		  };

		int tmp = Integer.parseInt(forma.getMapaPacientes("numRegistros")+"");
		forma.setUltimoPatron("tiemposalaespera_");
		forma.setMapaPacientes("numRegistros",tmp+"");
		forma.setOrderBy(0);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaPrincipal");		
	}
	
	
	private void ordenamientoTiempoSalaEspera (PacientesUrgenciasSalaEsperaForm forma, UsuarioBasico usuario, Connection con, PacientesUrgenciasSalaEspera mundo) throws SQLException
	{
		
		
		forma.setMapaPacientes(mundo.consultarPacientesUrgSalaEsperaOrdenamiento(con, usuario.getCodigoCentroAtencion(), usuario.getCodigoInstitucionInt(),forma.getOrderBy()));

		int tmp = Integer.parseInt(forma.getMapaPacientes("numRegistros")+"");
		forma.setUltimoPatron("tiemposalaespera_");
		forma.setMapaPacientes("numRegistros",tmp+"");
		
		if(forma.getOrderBy()==0){
			forma.setOrderBy(1);
		}else{
			forma.setOrderBy(0);
		}
		
		
		UtilidadBD.closeConnection(con);
	}
	

	
	
	/**
	 * Action para ordenar por cualquiera de las columnas del mapa
	 * @param con
	 * @param forma
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenarColumna(Connection con, PacientesUrgenciasSalaEsperaForm forma, ActionMapping mapping, UsuarioBasico usuario, PacientesUrgenciasSalaEspera mundo) 
    {
		String diasHoras="";
		String dias="";
		String horas="";
		HashMap mapaTmp = new HashMap();
		try {
		if(forma.getPatronOrdenar().equals("tiemposalaespera_")){
		
				ordenamientoTiempoSalaEspera(forma, usuario, con, mundo);
			
		        
		        
		}else{
			String[] indices = {
		            "codigoadmision_",
					"fechaadmision_",
					"horaadmision_",
					"fechahoraadmision_",
					"codigopaciente_",
					"idcuenta_",
					"nombrepaciente_",
					"tipoid_",
					"numeroid_",
					"calificaciontriage_",
					"nombrecolor_",
					"tiemposalaespera_",
					"descripcion_", //69474
					"nombremedico_" //134568
					
    		  };
			
			 int tmp = Integer.parseInt(forma.getMapaPacientes("numRegistros")+"");
		        //reemplazarEnMapa(forma, tmp, "tiemposalaespera_", ":",".");
		        forma.setMapaPacientes(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getMapaPacientes(),Integer.parseInt(forma.getMapaPacientes("numRegistros")+"")));
		        forma.setUltimoPatron(forma.getPatronOrdenar());
		        forma.setMapaPacientes("numRegistros",tmp+"");
		}
        UtilidadBD.closeConnection(con);
        
        
		} catch (SQLException e) {
			logger.error("Error Ordenando por tiempo espera sala "+e.getMessage());
		}
		return mapping.findForward("paginaPrincipal");
    }
	
	
	
	
	/**
	 * Reemplaza un caracter del valor de un key del mapa
	 * @param PacientesUrgenciasSalaEsperaForm forma
	 * @param int numRegistros
	 * @param String cadenaReemplazo
	 * */
	private void reemplazarEnMapa(PacientesUrgenciasSalaEsperaForm forma,int numRegistros,String indice,String cadenaOriginal, String cadenaReemplazo)
	{
		for(int i = 0 ;i <numRegistros ;i++)
		{
			if(!forma.getMapaPacientes(indice+i).equals(""))
			{
				forma.setMapaPacientes(indice+i,forma.getMapaPacientes(indice+i).toString().replace(cadenaOriginal,cadenaReemplazo));
			}
		}
	}
	
	/**
	* Método para hacer que el paciente
	* pueda ser visto por todos los usuario en la aplicacion
	* @param paciente
	*/
	private void setObservable(PersonaBasica paciente)
	{
		ObservableBD observable = (ObservableBD)getServlet().getServletContext().getAttribute("observable");
		if (observable != null) {
			synchronized (observable) {
				observable.setChanged();
				observable.notifyObservers(new Integer(paciente.getCodigoPersona()));
			}
		}
	}

	public static void main(String[] args) {
		
		//System.out.println("103358".compareTo("14208"));
		//System.out.println("66".compareTo("2"));//mayor a 1
		String x="5110";
		String y="6514";
		
	}
	
}