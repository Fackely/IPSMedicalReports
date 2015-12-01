package util.Busqueda;


import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import util.Busqueda.BusquedaCamasGenericaForm;
import util.manejoPaciente.UtilidadesManejoPaciente;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.Listado;
import util.UtilidadBD;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;



public class BusquedaCamasGenericaAction extends Action
{
	
	/**
	 * Objeto para manejar el log de la clase
	 * */
	Logger logger = Logger.getLogger(BusquedaCamasGenericaAction.class);
	
	/**
	 * 
	 * */
	public ActionForward execute(ActionMapping mapping,
			 					 ActionForm form,
			 					 HttpServletRequest request,
			 					 HttpServletResponse response) throws Exception 
			 					 {
		Connection con= null;
		try {
			if(response==null);

			if(form instanceof BusquedaCamasGenericaForm)
			{

				con = UtilidadBD.abrirConexion();

				if(con == null)
				{
					request.setAttribute("CodigoDescripcionError","errors.probelmasBd");
					return mapping.findForward("paginaError");
				}

				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				BusquedaCamasGenericaForm forma = (BusquedaCamasGenericaForm)form;
				PersonaBasica paciente= (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
				String estado = forma.getEstado();

				logger.info("Valor del Estado Busqueda Camas Generica >> "+estado);

				if(estado == null)
				{
					logger.warn("\n\n Estado no Valido dentro del Flujo de Busqueda Condicion Toma de Examen (null)");
					request.setAttribute("CodigoDescripcionError","errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}			

				else if(estado.equals("buscar"))
				{			
					this.accionConsultarCamas(con,forma,paciente,usuario);
					forma.setEstado("listado");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("buscarCamas");
				}			

				else if(estado.equals("ordenarConsultar"))
				{
					this.accionOrdenarMapa(forma);
					UtilidadBD.closeConnection(con);
					forma.setEstado("listado");
					return mapping.findForward("buscarCamas");					
				}

				else if (estado.equals("redireccion"))
				{
					UtilidadBD.closeConnection(con);
					forma.setEstado("listado");
					response.sendRedirect(forma.getLinkSiguiente());				
					return null;
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
	 * Llamado a la consulta de Camas
	 * @param Connecition con
	 * @param BusquedaCamasGenericaForm
	 * @param int institucion
	 * */ 
	public void accionConsultarCamas(Connection con, BusquedaCamasGenericaForm forma,PersonaBasica persona, UsuarioBasico usuario)
	{	
		forma.reset();
		forma.setConsultaMap("numRegistros","0");
		
		forma.setConsultaMap(UtilidadesManejoPaciente.obtenerCamas(con,
													 usuario.getCodigoInstitucion(),
													 persona.getCodigoSexo()+"",
													 persona.getFechaNacimiento(),
													 forma.getEstadoCama(),
													 forma.getCentroCostos(),
													 forma.getCentroAtencion(),
													 forma.getViaIngreso(),
													 forma.getFechaMovimiento(),
													 forma.getHoraMovimiento(),
													 forma.getPiso(),
													 forma.getTipoPaciente(),
													 forma.getAsignableAdmision(), //Asignable Admision (Hasta ahora no se usa para la búsqueda genérica)
													 "",
													 "",
													 ""
													 )); 
	}
	
	/**
	 * Ordena el Mapa de acuerdo a un patron
	 * @param forma
	 * */
	private void accionOrdenarMapa(BusquedaCamasGenericaForm forma)
	{		
		String[] indices = (String[])forma.getConsultaMap("INDICES_MAPA");
		int numReg = Integer.parseInt(forma.getConsultaMap("numRegistros")+"");
		forma.setConsultaMap(Listado.ordenarMapa(indices, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getConsultaMap(), numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setConsultaMap("numRegistros",numReg+"");
		forma.setConsultaMap("INDICES_MAPA",indices);		
	}
} 