/*
 * @(#)ConsultaLogCuposExtraAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.action.triage;

import util.Listado;
import util.UtilidadBD;
import util.UtilidadValidacion;
import java.sql.Connection;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import com.princetonsa.dao.DaoFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.triage.ConsultaPacientesTriage;
import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.triage.ConsultaPacientesTriageForm;

/**
 * Clase encargada del control de la funcionalidad de Consulta de los pacientes
 * pendientes por atender de triage

 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 * @version 1.0, 01 /Jun/ 2006
 */
public class ConsultaPacientesTriageAction extends Action
{
	/**
	 * Objeto para almacenar los logs que aparezcan en el Action 
	 */
	private Logger logger= Logger.getLogger(ConsultaPacientesTriageAction.class);

	/**
	 * Método excute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con = null;
		try {
			if(form instanceof ConsultaPacientesTriageForm)
			{

				/**Intentamos abrir una conexion con la fuente de datos**/ 
				con = openDBConnection(con); 
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}

				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				ConsultaPacientesTriage mundo =new ConsultaPacientesTriage();
				ConsultaPacientesTriageForm forma=(ConsultaPacientesTriageForm)form;

				String estado = forma.getEstado();
				logger.warn("[ConsultaPacientesTriageAction] estado->"+estado);

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de ConsultaPacientesTriageAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}

				else if(estado.equals("empezar"))
				{
					return this.accionEmpezar(forma, mapping, usuario, con, mundo, request);
				}
				else if(estado.equals("ordenarColumna"))
				{
					return this.accionOrdenarColumna(con, forma, mapping);
				}
				UtilidadBD.cerrarConexion(con);

			}
			else
			{
				logger.error("El form no es compatible con el form de Formato de Impresion de Factura");
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
	 * Action que empieza el flujo de la funcionalidad con los datos requeridos
	 * para mostrar
	 * @param forma
	 * @param mapping
	 * @param usuario
	 * @param con
	 * @param mundo
	 * @param request 
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionEmpezar (ConsultaPacientesTriageForm forma, ActionMapping mapping, UsuarioBasico usuario, Connection con, ConsultaPacientesTriage mundo, HttpServletRequest request) throws SQLException
	{
		//Si el usuario a ingresar NO es profesional de la salud no puede ingresar a la funcionalidad
		if(UtilidadValidacion.esProfesionalSalud(usuario))
		{
			forma.setMapaPacientesTriage(mundo.consultarPacientesTriage(con, usuario.getCodigoCentroAtencion()));
		}
		else
		{
			return ComunAction.accionSalirCasoError(mapping, request, con,logger, "errors.noProfesionalSalud","errors.noProfesionalSalud", true);
		}
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
	private ActionForward accionOrdenarColumna(Connection con, ConsultaPacientesTriageForm forma, ActionMapping mapping) 
    {
        String[] indices = {
					            "codigo_", 
					            "codigopaciente_", 
					            "nombrepaciente_", 
								"tipoid_",
								"nombretipoid_",
								"esconsecutivo_",
					            "numeroid_",
					            "edad_",
								"sexo_",
								"fechaingreso_",
								"horaingreso_",
								"tiempoespera_",
								"descclatriage_"
	            		  };
        
        int tmp = Integer.parseInt(forma.getMapaPacientesTriage("numRegistros")+"");
        forma.setMapaPacientesTriage(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getMapaPacientesTriage(),Integer.parseInt(forma.getMapaPacientesTriage("numRegistros")+"")));
        forma.setUltimoPatron(forma.getPatronOrdenar());
        forma.setMapaPacientesTriage("numRegistros",tmp+"");
        UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaPrincipal");
    }
	
	/**
	 * Abrir la conceccion con la Base de Datos
	 * @param con
	 * @return
	 */
	public Connection openDBConnection(Connection con)
	{
		if(con != null)
		{
			return con;
		}
		try
		{
			String tipoBD = System.getProperty("TIPOBD");
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			con = myFactory.getConnection();
		}
		catch(Exception e)
		{
			logger.warn("Problemas con la base de datos al abrir la conexion "+e.toString());
			return null;
		}
	
		return con;
	}
}