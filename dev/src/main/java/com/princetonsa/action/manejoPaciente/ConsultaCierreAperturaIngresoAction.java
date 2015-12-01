package com.princetonsa.action.manejoPaciente;

import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.Utilidades;

import com.princetonsa.actionform.manejoPaciente.ConsultaCierreAperturaIngresoForm;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.manejoPaciente.ConsultaCierreAperturaIngreso;

/**
 * @author Mauricio Jllo
 * Fecha: Mayo de 2008
 */

public class ConsultaCierreAperturaIngresoAction extends Action
{

	/**
	 * Objeto para manejar los logs de esta clase
	 */
	Logger logger = Logger.getLogger(CierreIngresoAction.class);
	
	/**
	 * Método execute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con = null;
		try{
			if (form instanceof ConsultaCierreAperturaIngresoForm) 
			{

				//Abrimos la conexion con la fuente de Datos 
				con = util.UtilidadBD.abrirConexion();
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}
				ConsultaCierreAperturaIngresoForm forma = (ConsultaCierreAperturaIngresoForm) form;
				ConsultaCierreAperturaIngreso mundo = new ConsultaCierreAperturaIngreso();
				HttpSession session = request.getSession();
				UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());
				PersonaBasica paciente = (PersonaBasica)session.getAttribute("pacienteActivo");
				String estado = forma.getEstado();
				logger.warn("[ConsultaCierreAperturaIngreso]--->Estado: "+estado);

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo estado is NULL");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				else if(estado.equals("empezar"))
				{
					return accionEmpezar(con, forma, usuario, mapping);
				}
				else if(estado.equals("recargar"))
				{
					if(UtilidadCadena.noEsVacio(forma.getTipoConsulta()))
					{
						//Cargamos el select de motivos segun el tipo seleccionado
						forma.setTiposMotivoCierre(Utilidades.obtenerMotivosAperturaCierre(con, forma.getTipoConsulta()));
						//Cargamos el select de usuario segun el tipo seleccionado
						forma.setUsuarioTipo(Utilidades.obtenerUsuarioAperturaCierre(con, forma.getTipoConsulta()));
					}
					else
					{
						//Borramos los datos de los ArrayList de Motivos y Usurios. Esto pasa cuando es seleccione
						ArrayList tempo = new ArrayList();
						forma.setTiposMotivoCierre(tempo);
						forma.setUsuarioTipo(tempo);
					}
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("buscar"))
				{
					forma.setBusquedaCierreAperturaIngresos(mundo.consultarCierreAperturaIngresos(con, forma));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listado");
				}
				else if(estado.equals("detalle"))
				{
					this.accionDetalle(con, forma, mundo);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalle");
				}
				else if(estado.equals("volverPrincipal"))
				{
					return accionEmpezar(con, forma, usuario, mapping);
				}
				else if(estado.equals("volverListado"))
				{
					forma.setBusquedaCierreAperturaIngresos(mundo.consultarCierreAperturaIngresos(con, forma));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listado");
				}
				//ESTADO UTILIZADO PARA EL PAGER
				else if (estado.equals("redireccion")) 
				{			    
					UtilidadBD.cerrarConexion(con);
					forma.getLinkSiguiente();
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
				}
				else
				{
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
			}
			else
			{
				logger.error("El form no es compatible con el form de ConsultaCierreAperturaIngresoForm");
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
	 * Metodo que ejecuta y carga los select de la busqueda
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param mapping 
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, ConsultaCierreAperturaIngresoForm forma, UsuarioBasico usuario, ActionMapping mapping)
	{
		forma.reset();
		//Cargamos el select con todos los centros de atencion
		forma.setCentroAtencion(Utilidades.obtenerCentrosAtencion(usuario.getCodigoInstitucionInt()));
		//Se selecciona el Centro de Atencion de Sesion
		forma.setCodigoCentroAtencion(usuario.getCodigoCentroAtencion()+"");
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	/**
	 * Metodo que consulta la informacion del detalle del registro seleccionado
	 * @param con
	 * @param forma
	 * @param mundo
	 */
	private void accionDetalle(Connection con, ConsultaCierreAperturaIngresoForm forma, ConsultaCierreAperturaIngreso mundo)
	{
		String motivo = "";
		
		if(forma.getTipoConsulta().equals(ConstantesIntegridadDominio.acronimoCierreIngreso))
			motivo = ConstantesIntegridadDominio.acronimoCierreIngreso;
		else if(forma.getTipoConsulta().equals(ConstantesIntegridadDominio.acronimoAperturaIngreso))
			motivo = ConstantesIntegridadDominio.acronimoAperturaIngreso;
		
		int codigoCierreApertura = Utilidades.convertirAEntero(forma.getBusquedaCierreAperturaIngresos("codigocierre_"+forma.getPosicion())+"");
		logger.info("\n\n====>Posicion Seleccionada: "+forma.getPosicion());
		logger.info("====>Codigo Cierre/Apertura: "+forma.getBusquedaCierreAperturaIngresos("codigocierre_"+forma.getPosicion()));
		logger.info("====>Motivo: "+motivo);
		forma.setDetalleCierreAperturaIngresos(mundo.detalleCierreAperturaIngreso(con, codigoCierreApertura, motivo));
	}
	
}