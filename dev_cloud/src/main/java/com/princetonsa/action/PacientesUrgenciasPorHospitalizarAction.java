package com.princetonsa.action;

import java.sql.Connection;
import java.sql.SQLException;

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
import util.UtilidadSesion;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.PacientesUrgenciasPorHospitalizarForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.ObservableBD;
import com.princetonsa.mundo.PacientesUrgenciasPorHospitalizar;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;

public class PacientesUrgenciasPorHospitalizarAction extends Action
{
	/**
	 * Objeto para almacenar los logs que aparezcan en el Action 
	 */
	private Logger logger= Logger.getLogger(PacientesUrgenciasPorHospitalizarAction.class);

	/**
	 * Método excute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con=null;
		try{
			if(form instanceof PacientesUrgenciasPorHospitalizarForm)
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
				PacientesUrgenciasPorHospitalizarForm forma = (PacientesUrgenciasPorHospitalizarForm)form;
				PacientesUrgenciasPorHospitalizar mundo = new PacientesUrgenciasPorHospitalizar();

				String estado = forma.getEstado();
				logger.warn("[PacientesUrgenciasPorHospitalizarAction] estado->"+estado);

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de PacientesUrgenciasPorHospitalizarAction (null) ");
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
					return this.accionOrdenarColumna(con, forma, mapping);
				}
				else if(estado.equals("preingresar"))
				{
					UtilidadSesion.notificarCambiosObserver(Integer.parseInt(forma.getMapaPacientes("codigopaciente_"+forma.getPosicionMapa())+""), getServlet().getServletContext());

					/**para cargar el paciente que corresponda**/
					paciente.setCodigoPersona((Integer.parseInt(forma.getMapaPacientes("codigopaciente_"+forma.getPosicionMapa())+"")));
					paciente.cargar(con,(Integer.parseInt(forma.getMapaPacientes("codigopaciente_"+forma.getPosicionMapa())+"")));
					paciente.cargarPaciente(con, (Integer.parseInt(forma.getMapaPacientes("codigopaciente_"+forma.getPosicionMapa())+"")),usuario.getCodigoInstitucion(),usuario.getCodigoCentroAtencion()+"");
					this.setObservable(paciente);
					UtilidadBD.cerrarConexion(con);
					response.sendRedirect("../ingresarPaciente/ingresoPaciente.do?estado=decisionIngresoPacienteSistema&tipoIdentificacion="+forma.getMapaPacientes("tipoid_"+forma.getPosicionMapa())+"-"+forma.getMapaPacientes("nombretipoid_"+forma.getPosicionMapa())+"&numeroIdentificacion="+forma.getMapaPacientes("numeroid_"+forma.getPosicionMapa())+"&Submit=true");
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
	private ActionForward accionEmpezar (PacientesUrgenciasPorHospitalizarForm forma, ActionMapping mapping, UsuarioBasico usuario, Connection con, PacientesUrgenciasPorHospitalizar mundo) throws SQLException
	{
		forma.setMapaPacientes(mundo.consultarPacientesUrgPorHospitalizar(con, usuario.getCodigoCentroAtencion(), usuario.getCodigoInstitucionInt()));
		String[] indices = {
								"destino_",
								"codigoadmision_",
								"fechaadmision_",
								"horaadmision_",
								"fechahoraadmision_",
								"codigopaciente_",
								"idcuenta_",
								"nombrepaciente_",
								"codigosexo_",
								"nombresexo_",
								"acronimosexo_",
								"tipoid_",
								"nombretipoid_",
								"numeroid_",
								"fechanacimiento_",
								"edad_",
								"fechaegreso_",
								"horaegreso_",
								"fechahoraegreso_",
								"diagnostico_",
								"medico_",
								"tiempoespera_"
		  				  };

		int tmp = Integer.parseInt(forma.getMapaPacientes("numRegistros")+"");
		forma.setMapaPacientes(Listado.ordenarMapa(indices,"fechahoraegreso_","fechahoraegreso_",forma.getMapaPacientes(),Integer.parseInt(forma.getMapaPacientes("numRegistros")+"")));
		forma.setUltimoPatron("fechahoraegreso_");
		forma.setMapaPacientes("numRegistros",tmp+"");
		
		//******ASignacion del maxPageItems**************************************
		try
		{
			forma.setMaxPageItems(ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()));
		}
		catch(Exception e)
		{
			forma.setMaxPageItems(10);
			logger.info("MaxPageItems no está parametrizado en Parametros Generales: "+e);
		}
		//************************************************************************
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaPrincipal");		
	}
	
	/**
	 * Action para ordenar por cualquiera de las columnas del mapa
	 * @param con
	 * @param forma
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenarColumna(Connection con, PacientesUrgenciasPorHospitalizarForm forma, ActionMapping mapping) 
    {
        String[] indices = {
        						"destino_",
					            "codigoadmision_",
        						"fechaadmision_",
        						"horaadmision_",
        						"fechahoraadmision_",
        						"codigopaciente_",
        						"idcuenta_",
        						"nombrepaciente_",
        						"codigosexo_",
        						"nombresexo_",
        						"acronimosexo_",
        						"tipoid_",
        						"nombretipoid_",
        						"numeroid_",
        						"fechanacimiento_",
        						"edad_",
        						"fechaegreso_",
        						"horaegreso_",
        						"fechahoraegreso_",
        						"diagnostico_",
        						"medico_",
        						"tiempoespera_"
	            		  };
        
        int tmp = Integer.parseInt(forma.getMapaPacientes("numRegistros")+"");
        forma.setMapaPacientes(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getMapaPacientes(),Integer.parseInt(forma.getMapaPacientes("numRegistros")+"")));
        forma.setUltimoPatron(forma.getPatronOrdenar());
        forma.setMapaPacientes("numRegistros",tmp+"");
        UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaPrincipal");
    }
	
	/**
	* Método para hacer que el paciente
	* pueda ser visto por todos los usuario en la aplicacion
	* @param paciente
	*/
	private void setObservable(PersonaBasica paciente)
	{
		ObservableBD observable = (ObservableBD)getServlet().getServletContext().getAttribute("observable");
		if (observable != null) 
		{
			synchronized (observable) 
			{
				observable.setChanged();
				observable.notifyObservers(new Integer(paciente.getCodigoPersona()));
			}
		}
	}
	
	
}