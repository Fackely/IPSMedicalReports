package com.princetonsa.action.triage;

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
import util.ValoresPorDefecto;

import com.princetonsa.actionform.triage.PacientesTriageUrgenciasForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.triage.PacientesTriageUrgencias;

public class PacientesTriageUrgenciasAction extends Action
{
	/**
	 * Objeto para almacenar los logs que aparezcan en el Action 
	 */
	private Logger logger= Logger.getLogger(PacientesTriageUrgenciasAction.class);

	/**
	 * Método excute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con=null;
		try {
			if(form instanceof PacientesTriageUrgenciasForm)
			{
				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexión"+e.toString());
					return mapping.findForward("paginaError");
				}

				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				PacientesTriageUrgenciasForm forma=(PacientesTriageUrgenciasForm)form;

				String estado = forma.getEstado();
				logger.warn("[PacientesTriageUrgencia] estado->"+estado);

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de Pacientes Triage Urgencias (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}

				else if(estado.equals("empezar"))
				{
					return this.accionEmpezar(forma, mapping, usuario, con);
				}
				else if(estado.equals("ordenarColumna"))
				{
					return this.accionOrdenarColumna(con, forma, mapping);
				}
				else if(estado.equals("busquedaAvanzada"))
				{
					return this.accionBusquedaAvanzada(forma, mapping, usuario, con);
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
	 * Metodo que ejecuta la consulta de pacientes de triage para un
	 * ragon de fechas establecido. Esto fue propuesto por la tarea
	 * 753
	 * @param forma
	 * @param mapping
	 * @param usuario
	 * @param con
	 * @return
	 */
	private ActionForward accionBusquedaAvanzada(PacientesTriageUrgenciasForm forma, ActionMapping mapping, UsuarioBasico usuario, Connection con)
	{
		forma.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
		PacientesTriageUrgencias mundo= new PacientesTriageUrgencias();
		forma.setMapa(mundo.listadoBusquedaAvanzada(con, usuario.getCodigoCentroAtencion()+"", usuario.getCodigoInstitucionInt(), forma.getFechaInicial(), forma.getFechaFinal()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaPrincipal");
	}

	/**
	 * Action que empieza el flujo de la funcionalidad con los datos requeridos
	 * para mostrar
	 * @param forma
	 * @param mapping
	 * @param usuario
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionEmpezar (PacientesTriageUrgenciasForm forma, ActionMapping mapping, UsuarioBasico usuario, Connection con) throws SQLException
	{
		forma.reset();
		forma.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
		PacientesTriageUrgencias mundo= new PacientesTriageUrgencias();
		forma.setMapa(mundo.listado(con, usuario.getCodigoCentroAtencion()+"", usuario.getCodigoInstitucionInt()));
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
	private ActionForward accionOrdenarColumna(Connection con, PacientesTriageUrgenciasForm forma, ActionMapping mapping) 
    {
        String[] indices = {
					            "numero_triage_", 
					            "nombre_paciente_", 
					            "acronimo_tipo_id_", 
								"nombre_tipo_id_",
					            "numero_identificacion_",
					            "calificacion_triage_",
								"color_",
								"fecha_triage_formato_bd_",
								"fecha_triage_",
								"hora_triage_",
								"nombre_color_",
								"primer_nombre_persona_",
								"segundo_nombre_persona_",
								"primer_apellido_persona_",
								"segundo_apellido_persona_",
								"fecha_nacimiento_persona_",
								"accidente_trabajo_"
	            		  };
        
        int tmp = Integer.parseInt(forma.getMapa("numRegistros")+"");
        forma.setMapa(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getMapa(),Integer.parseInt(forma.getMapa("numRegistros")+"")));
        forma.setUltimoPatron(forma.getPatronOrdenar());
        forma.setMapa("numRegistros",tmp+"");
        UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaPrincipal");
    }
	
	
}