package com.princetonsa.action.administracion.mantenimiento.semaforizacion;

import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadSesion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.constantes.estadosJsp.IconstantesEstadosJsp;

import com.princetonsa.actionform.administracion.semaforizacion.SemaforizacionConsultasYReportesForm;
import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.mundo.fabrica.AdministracionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.administracion.IParametrizacionSemaforizacionMundo;
import com.servinte.axioma.orm.ParametrizacionSemaforizacion;
import com.servinte.axioma.persistencia.UtilidadTransaccion;

public class SemaforizacionConsultasYReportesAction extends Action {

	/** * Log */
	Logger logger = Logger
			.getLogger(SemaforizacionConsultasYReportesAction.class);

	/** * Contiene la lista de mensajes correspondiente a esta forma */
	MessageResources fuenteMensaje = MessageResources
			.getMessageResources("com.servinte.mensajes.administracion.mantenimiento.SemaforizacionConsultasYReportesForm");
	
	
	
	
	/**
	 * semaforizacion con metedos de CU 
	 */
	IParametrizacionSemaforizacionMundo parametrizacionSemaforizacion =  AdministracionFabricaMundo.crearParametrizacionMundo();

	
	
	/**
	 * Action con validaciones de estado 
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		if (form instanceof SemaforizacionConsultasYReportesForm) {
			SemaforizacionConsultasYReportesForm forma = (SemaforizacionConsultasYReportesForm) form;
			UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());
			String estado = forma.getEstado();
			
			//VALIDACIONES DE ESTADO Y RE DIRECCION A OPERACIONES CORRESPONDIENTES
			if (estado.equals(IconstantesEstadosJsp.ESTADO_EMPEZAR)) {
				return valoreiniciales(forma, response, request, mapping);
			}else if (estado.equals(IconstantesEstadosJsp.ESTADO_SELECCION_COMBO_REPORTES)) {
				consultarParametrizaciones(forma);
			}else if (estado.equals(IconstantesEstadosJsp.ESTADO_ELIMINACION_PARAMETRIZACION)) {
				eliminarRegistro(forma);
			}else if (estado.equals(IconstantesEstadosJsp.ESTADO_GUARDAR_PARAMETRIZACION)) {
				guardar(usuario,forma);
			}else if (estado.equals(IconstantesEstadosJsp.ESTADO_ADICIONAR_PARAMETRIZACION)) {
				adicionarParametrizacion(forma);
			}else if ((estado.equals(IconstantesEstadosJsp.ESTADO_NUEVA_PARAMETRIZACION))||(estado.equals(IconstantesEstadosJsp.ESTADO_ELIMINAR_NUEVA_PARAMETRIZACION))) {
				return nuevaParametrizacion(forma,usuario,request,response);
			}
		}
		return mapping.findForward("inicial");
	}
	
	
	
	
	/**
	 * Metodo de carga e inicializacion de elementos a usar en el cu
	 * @param forma
	 * @param response
	 * @param request
	 * @param mapping
	 * @return Estado inicial 
	 */
	private ActionForward valoreiniciales(SemaforizacionConsultasYReportesForm forma, HttpServletResponse response, HttpServletRequest request, ActionMapping mapping){
		forma.inicializar();
		cargarListadoReportes(forma);
		return  mapping.findForward("inicial");
		
	}
	
	
	/**
	 * Carga la lista de reportes a seleccionar en la interfaz 
	 * @param forma
	 */
	public void cargarListadoReportes(SemaforizacionConsultasYReportesForm forma){
		
		//SOLO SE USAN DOS TIPO DE RPEORTES QUE ESTAN PARAMETRIZADOS EN LOS REPORTES 
		String[] listadoReportes = new String[]{ConstantesIntegridadDominio.acronimoTipoReportePresupuesto,
				ConstantesIntegridadDominio.acronimoTipoReporteEstadisticasOrdenadores};

		Connection con=UtilidadBD.abrirConexion();
		
		//SE CREA EL ARREGLO CON LOS VALORES
		ArrayList<DtoIntegridadDominio> listadoReportesSemaforizacion=Utilidades.generarListadoConstantesIntegridadDominio(
				con, listadoReportes, false);
		
		UtilidadBD.closeConnection(con);
		
		forma.setListadoReportes(listadoReportesSemaforizacion);
	}
	
	
	/**
	 * Se consultan todas la parametrizaciones que se encuentran en el sistema 
	 * @param forma
	 */
	public void consultarParametrizaciones(SemaforizacionConsultasYReportesForm forma){
		
		UtilidadTransaccion.getTransaccion().begin();;
		
		try 
		{
		// SE CONSULTA LA PARAMETRIZACIONES EN EL SISTEMA 
		forma.setListadoParametrizacionesPorReporte(parametrizacionSemaforizacion.consultarParametrizaciones(forma.getTipoReporteAParametrizar()));
		forma.setListaElementosAEliminar(new ArrayList<Long>());
		UtilidadTransaccion.getTransaccion().commit();
		}//CONTROL DE ERRORES
		catch (Exception e) { 
			
			UtilidadTransaccion.getTransaccion().rollback();
		}
		
		
	}
	
	
	
	
	/**
	 * Se guardan las parametrizaciones modificadas,eliminadas,adicionadas se llama cuando se da click en boton Guardar
	 * @param usu
	 * @param forma
	 */
	public void guardar(UsuarioBasico usu,SemaforizacionConsultasYReportesForm forma){
		
		//LLAMADO A SERVICIO DE ELIMINAR 
		eliminarDEBaseDeDatos(forma);
		
		//LLAMADO AL METODO GUARDAR
		guardarActualizarParametrizacion(usu, forma);
		
		//LLAMADIO AL CONSULTAR PARA VER LOS CAMBIOS HECHOS EN LA BD
		consultarParametrizaciones(forma);
	}
	
	
	
	
	
	/**
	 * Metodo encargado de eliminar una parametrizacion en memoria 
	 * @param forma
	 */
	public void eliminarRegistro(SemaforizacionConsultasYReportesForm forma) {
		
		forma.eliminarParametrizacion();

	}
	
	/**
	 * Se adiciona una nueva parametrizacion en el sistema pero la adicion se realiza en memoria
	 * @param forma
	 */
	public void adicionarParametrizacion(SemaforizacionConsultasYReportesForm forma){
		
		ArrayList<ParametrizacionSemaforizacion> lista = forma.getListadoParametrizacionesPorReporte();
		lista.add(forma.getParametrizacion());
		forma.setListadoParametrizacionesPorReporte(lista);
		
		
	}
	
	/**
	 * Se eliminan todos los registros seleccionados de la base de datos, segun los que quedaron en memoria.
	 * @param forma
	 */
	public void eliminarDEBaseDeDatos(SemaforizacionConsultasYReportesForm forma){
		UtilidadTransaccion.getTransaccion().begin();
		try {
			
			//SE LLAMA AL SERVICIO DE ELIMINAR ELEMENTOS DE LA BASE DE DATOS.
			parametrizacionSemaforizacion.eliminarParametrizacion(
					forma.getIndiceDetalle(),
					forma.getListaElementosAEliminar());
			
			UtilidadTransaccion.getTransaccion().commit();
		}//CONTROL DE ERRORES
		catch (Exception e) {
			UtilidadTransaccion.getTransaccion().rollback();
		}

		
		
		
	}
	
	
	/**
	 * Se guardan todas las parametizaciones en la base de datos 
	 * @param usu
	 * @param forma
	 */
	public void guardarActualizarParametrizacion(UsuarioBasico usu,SemaforizacionConsultasYReportesForm forma){
		
		
			UtilidadTransaccion.getTransaccion().begin();
		

		try {
			//SE LLAMA AL SERVICIO DE GUARDAR EN BD
			parametrizacionSemaforizacion.adicionarModificar(usu, forma.getListadoParametrizacionesPorReporte(), forma.getTipoReporteAParametrizar());
			
			// SE CAMBIA EL ESTADO A GUARDAR EXITOSO PARA MOSTRAR EL MENSAJE EN PANTALLA
			forma.setEstado(IconstantesEstadosJsp.ESTADO_GUARDAR_EXITOSO);
			UtilidadTransaccion.getTransaccion().commit();
		}//CONTROL DE ERRORES  
		catch (Exception e) {
			UtilidadTransaccion.getTransaccion().rollback();
		}

		
		
	}
	

	/**
	 * Se inicializa la parametrizacion de la forma para adicionar un nuero registro.
	 * @param forma
	 */
	public ActionForward nuevaParametrizacion(SemaforizacionConsultasYReportesForm forma,UsuarioBasico usuario,HttpServletRequest request, HttpServletResponse response){
		forma.getListadoParametrizacionesPorReporte().add(new ParametrizacionSemaforizacion());
		return UtilidadSesion.redireccionar("",ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()),forma.getListadoParametrizacionesPorReporte().size(), response, request, "semaforizacionConsultasReportes.jsp",true);
		
		
		//forma.setParametrizacion(new ParametrizacionSemaforizacion());
	}
	
	
	
	

	
	
	
	
}
