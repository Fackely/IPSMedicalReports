/*
 * Julio 26, 2010
 */
package com.princetonsa.action.tesoreria;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.tesoreria.ConsultaTrasladoAbonoPacienteForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.CentroAtencion;
import com.servinte.axioma.orm.Ciudades;
import com.servinte.axioma.servicio.fabrica.odontologia.administracion.AdministracionFabricaServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ILocalizacionServicio;


/**
 * @author Cristhian Murillo
 *
 * Clase usada para controlar los procesos de la funcionalidad.
 * 
 */
public class ConsultaTrasladoAbonoPacienteAction extends Action 
{

	/**
	 * M&eacute;todo encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, 
	 * HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
								HttpServletResponse response )throws Exception
	{
		if(form instanceof ConsultaTrasladoAbonoPacienteForm){
			
			ConsultaTrasladoAbonoPacienteForm forma = (ConsultaTrasladoAbonoPacienteForm)form;
			String estado = forma.getEstado(); 
			
			
			Log4JManager.info("Estado ConsultaTrasladoAbonoPacienteAction --> "+estado);
			
			UsuarioBasico usuario 	= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			
			if(estado == null)
			{
				request.setAttribute("codigoDescripcionError","errors.estadoInvalido");
				return mapping.findForward("paginaError");
			}
			
			else if( (estado.equals("empezar")) || (estado.equals("resumen")) )
			{
				return accionEmpezar(mapping, forma, usuario, request);
			}
			
			else if(estado.equals("recargarlistas"))
			{
				return accionRecargarListas(mapping, forma, request);
			}
			
		}
		return null;
	}
	
	



	/*------------------------------*/
	/* ACTION						*/
	/*------------------------------*/
		
	/**
	 * Accion Empezar
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionEmpezar(ActionMapping mapping, ConsultaTrasladoAbonoPacienteForm forma, 
			UsuarioBasico usuario, HttpServletRequest request)
	{
		try{
			HibernateUtil.beginTransaction();			
			forma.reset();
			
			llenarListasIniciales(forma, usuario);
			
			HibernateUtil.endTransaction();
		}
		catch (Exception e) {
			Log4JManager.error("ERROR accionEmpezar", e);
			HibernateUtil.abortTransaction();
		}
		return mapping.findForward("principal");
	}
	
	
	
	/**
	 * Llena las listas con los parametros de busquedas.
	 * En este  no se llena la lista de Ciudades o Paises
	 * @param forma
	 */
	private void llenarListasIniciales(ConsultaTrasladoAbonoPacienteForm forma, UsuarioBasico usuario)
	{
		ILocalizacionServicio localizacionServicio = AdministracionFabricaServicio.crearLocalizacionServicio();

		// FIXME poner por defecto las del sistema:
		// La ruta podria ser centro de atencion - ciudad  pais/region cobertura
		forma.setListaRegiones(localizacionServicio.listarRegionesCoberturaActivas());
		forma.setListaCiudades(localizacionServicio.listarCiudades()); 
		forma.setListaPaises(localizacionServicio.listarPaises());
		forma.setListaCentroAtencion(localizacionServicio.listarTodosActivosPorInstitucion(usuario.getCodigoInstitucionInt()));
		
		boolean parametroMultiempresa = UtilidadTexto.getBoolean(ValoresPorDefecto.getInstitucionMultiempresa(usuario.getCodigoInstitucionInt()));
		forma.getDtoBusTrasladoAbono().setParametroInstitucionMultiempresa(parametroMultiempresa);
		if(parametroMultiempresa == true){
			// Mirar cual se debe setear
			forma.setListaInstituciones(localizacionServicio.listarInstituciones());
		}
	}
	
	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param request
	 * @return
	 */
	private ActionForward accionRecargarListas(ActionMapping mapping, ConsultaTrasladoAbonoPacienteForm forma, HttpServletRequest request)
	{
		try{
			HibernateUtil.beginTransaction();
			ILocalizacionServicio localizacionServicio = AdministracionFabricaServicio.crearLocalizacionServicio();
			// Ciudades
			forma.setListaCiudades(new ArrayList<Ciudades>());
			if(forma.getDtoBusTrasladoAbono().getCodigoPais() == null){
				forma.setListaCiudades(localizacionServicio.listarCiudades());
			}else{
				forma.setListaCiudades(localizacionServicio.listarCiudadesPorPais(forma.getDtoBusTrasladoAbono().getCodigoPais()));
			}
			
			// Centro Atencion
			forma.setListaCentroAtencion(new ArrayList<CentroAtencion>());
			if(forma.getDtoBusTrasladoAbono().getCodigoRegionesCobertura() == ConstantesBD.codigoNuncaValidoLong){
				if(forma.getDtoBusTrasladoAbono().getCodigoInstituciones() == ConstantesBD.codigoNuncaValido){
					//forma.setListaCentroAtencion(localizacionServicio.listarTodosActivosPorInstitucionYRegion(
							//forma.getDtoBusTrasladoAbono().getCodigoInstituciones(), forma.getDtoBusTrasladoAbono().getCodigoCentroAten()));
						//onchange de las listas
				}	
			}
			
			HibernateUtil.endTransaction();
		}
		catch (Exception e) {
			Log4JManager.error("ERROR accionRecargarListas", e);
			HibernateUtil.abortTransaction();
		}	
		return mapping.findForward("principal");
	}
	
	
	
}
