/*
 * @(#)ActivPyPXFacturarAction
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.action.pyp;

import java.sql.Connection;
import java.sql.SQLException;

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
import util.UtilidadValidacion;
import util.historiaClinica.UtilidadesHistoriaClinica;

import com.princetonsa.actionform.pyp.ActivPyPXFacturarForm;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.pyp.ActivPyPXFacturar;

/**
 *   Action, controla todas las opciones 
 *  , incluyendo los posibles casos de error. 
 *   Y los casos de flujo.
 * @author wrios
 */
public class ActivPyPXFacturarAction extends Action
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(ActivPyPXFacturarAction.class);
	
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
		try{
			if (response==null); //Para evitar que salga el warning
			if(form instanceof ActivPyPXFacturarForm)
			{

				con = UtilidadBD.abrirConexion();	
				ActivPyPXFacturarForm forma =(ActivPyPXFacturarForm)form;
				String estado=forma.getEstado(); 
				UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				logger.warn("\n ESTADO ActivPyPXFacturarAction==========="+estado);

				if(estado == null)
				{
					forma.reset();	
					logger.warn("Estado no valido dentro del flujo de  ActivPyPXFacturar(null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				else if (estado.equals("empezar"))
				{
					return this.accionEmpezar(forma,mapping, con);
				}
				else if(estado.equals("buscar"))
				{
					return this.accionBuscar(forma,mapping, con, usuario);
				}
				else if(estado.equals("ordenarColumna"))
				{
					return this.accionOrdenarColumna(con, mapping, forma);
				}
				else if(estado.equals("facturar"))
				{
					cargarPacienteSesion(request, forma, con, usuario);
					PersonaBasica persona= (PersonaBasica) request.getSession().getAttribute("pacienteActivo");
					ActionErrors errores = new ActionErrors();
					if(!UtilidadesHistoriaClinica.obtenerEstadoIngreso(con, persona.getCodigoIngreso()).getCodigo().trim().equals(ConstantesIntegridadDominio.acronimoEstadoAbierto))
					{
						errores.add("errors.ingresoEstadoDiferenteAbierto", new ActionMessage("errors.ingresoEstadoDiferenteAbierto"));
					}
					if(UtilidadValidacion.esCuentaValida(con, persona.getCodigoCuenta())<1)
					{
						errores.add("errors.paciente.cuentaNoValida", new ActionMessage("errors.paciente.cuentaNoValida"));
					}

					UtilidadBD.cerrarConexion(con);
					if(errores.isEmpty())
					{
						response.sendRedirect("../facturacion/facturacion.do?estado=empezar&esWorkFlowActividadesXFacturar=si");
						return null;
					}
					else
					{
						saveErrors(request, errores);
						return mapping.findForward("paginaPrincipal");	
					}
				}
				else
				{
					forma.reset();
					logger.warn("Estado no valido dentro del flujo de ActivPyPXFacturar ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
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
	 * 
	 * @param forma
	 * @param mapping
	 * @param codigoInstitucionInt
	 * @param con
	 * @return
	 */
	private ActionForward accionEmpezar(ActivPyPXFacturarForm forma, ActionMapping mapping, Connection con)
	{
		//Limpiamos lo que venga del form
		forma.reset();
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaPrincipal");	
	}
	
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param b
	 * @return
	 */
	private ActionForward accionBuscar(ActivPyPXFacturarForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario)
	{
		ActivPyPXFacturar mundo= new ActivPyPXFacturar();
		forma.setMapa(mundo.listado(con, forma.getCodigoConvenio(), usuario.getCodigoCentroAtencion(), usuario.getCodigoInstitucionInt()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaPrincipal");	
	}
	
	
	/**
	 * 
	 * @param con
	 * @param mapping
	 * @param forma
	 * @return
	 */
	private ActionForward accionOrdenarColumna(Connection con, ActionMapping mapping, ActivPyPXFacturarForm forma) 
	{
		String[] indices = {
				            "numeroorden_", 
				            "fechaordenbd_", 
				            "fechaorden_",
				            "numerosolicitud_",
				            "codigopaciente_",
				            "nombrespaciente_",
				            "nombreservicio_"
				           };
        
        int tmp = Integer.parseInt(forma.getMapa("numRegistros")+"");
        forma.setMapa(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getMapa(),Integer.parseInt(forma.getMapa("numRegistros")+"")));
        forma.setUltimoPatron(forma.getPatronOrdenar());
        forma.setMapa("numRegistros",tmp+"");
        UtilidadBD.closeConnection(con);
        return mapping.findForward("paginaPrincipal");  
	}
	
	/**
	 * 
	 * @param request
	 * @param forma
	 * @param con
	 * @param usuario
	 * @return
	 */
	private boolean cargarPacienteSesion(HttpServletRequest request, ActivPyPXFacturarForm forma, Connection con, UsuarioBasico usuario)
	{
		PersonaBasica persona= (PersonaBasica) request.getSession().getAttribute("pacienteActivo");
        persona.setCodigoPersona(forma.getCodigoPacienteInt());
        try
		{
			persona.cargar(con,forma.getCodigoPacienteInt());
			persona.cargarPaciente(con, forma.getCodigoPacienteInt(), usuario.getCodigoInstitucion(),usuario.getCodigoCentroAtencion()+"");
			return true;
		} 
        catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
    }
	
}
