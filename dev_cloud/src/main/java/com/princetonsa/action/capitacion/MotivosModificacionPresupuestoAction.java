package com.princetonsa.action.capitacion;

import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.axioma.util.log.Log4JManager;

import util.UtilidadSesion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.constantes.estadosJsp.IconstantesEstadosJsp;

import com.princetonsa.actionform.capitacion.MotivosModificacionPresupuestoForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.sort.odontologia.SortGenerico;
import com.servinte.axioma.dto.capitacion.DtoMotivosModifiPresupuesto;
import com.servinte.axioma.mundo.fabrica.capitacion.CapitacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IMotivosModificacionPresupuestoMundo;
import com.servinte.axioma.persistencia.UtilidadTransaccion;

public class MotivosModificacionPresupuestoAction extends Action {

	/** * Log */
	Logger logger = Logger
	.getLogger(MotivosModificacionPresupuestoAction.class);


	/** * Contiene la lista de mensajes correspondiente a esta forma */
	MessageResources fuenteMensaje = MessageResources
	.getMessageResources("com.servinte.mensajes.capitacion.MotivosModificacionPresupuestoForm");


	/**
	 * Inyeccion de mundo con servicios 
	 */
	IMotivosModificacionPresupuestoMundo motivosModificacionPresupuestoMundo  = CapitacionFabricaMundo.crearMotivosModifiaccionPresupuestoMundo();

	/**
	 * Metodo execute con validaciones de estado e invocaciones a metodos del mundo
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		if (form instanceof MotivosModificacionPresupuestoForm) {
			//INSTACIACION DEL FORM
			MotivosModificacionPresupuestoForm forma = (MotivosModificacionPresupuestoForm) form;

			//SE OBTIENE EL USUARIO 
			UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());
			String estado = forma.getEstado();


			if (estado.equals(IconstantesEstadosJsp.ESTADO_EMPEZAR)) { //ok
				valoreiniciales(forma, response, request, mapping);
			}else if (estado.equals(IconstantesEstadosJsp.ESTADO_ELIMINACION_PARAMETRIZACION)) { //ok
				eliminarRegistro(forma);
			}else if (estado.equals(IconstantesEstadosJsp.ESTADO_GUARDAR_PARAMETRIZACION)) { //ok
				guardar(usuario,forma,mapping);
			}else if (estado.equals(IconstantesEstadosJsp.ESTADO_NUEVA_PARAMETRIZACION)) { //ok
				adicionarMotivoModificacion(forma,usuario,request,response);
			}else if (estado.equals(IconstantesEstadosJsp.ESTADO_ORDENAR)) { //ok
				accionOrdenar(forma);
			}else if (estado.equals(IconstantesEstadosJsp.ESTADO_FILTRAR)) { //ok
				return 	valoresInicialesFiltro(forma, mapping);
			}else if (estado.equals(IconstantesEstadosJsp.ESTADO_BUSCAR)) { //ok
				return 	consultarFiltro(forma, mapping);
			}

		}
		return mapping.findForward("inicial");

	}


	/**
	 * Metodo que se encarga de realizar consulta por filtros ingresados
	 * @param forma
	 * @param mapping
	 * @return ActionForward para ver los resultados
	 */
	public ActionForward consultarFiltro(MotivosModificacionPresupuestoForm forma,ActionMapping mapping){



		UtilidadTransaccion.getTransaccion().begin();;

		try 
		{
			//FLAG DE TIPO DE CONSULTA A REALIZAR 
			forma.setIndicadorTipoConsulta(true);
			
			//SE EJECUTA LA CONSULTA SE ACUERDO A LOS PARAMETROS INGRESADOS
			forma.setListaMotivosModificacionPresupuesto(motivosModificacionPresupuestoMundo.consultaFiltro(forma.getCodigoFiltro(),forma.getDescripcionFiltro(), forma.getActivoFiltro()));
			
			//SE INICIALIZA LA LISTA DE ELIMINADOS
			forma.setListaElementosAEliminar(new ArrayList<Long>());


			UtilidadTransaccion.getTransaccion().commit();
			
		}//CONTROL DE ERRORES
		catch (Exception e) { 

			UtilidadTransaccion.getTransaccion().rollback();
		}

		return  mapping.findForward("resultado");


	}




	/**
	 * @param forma
	 * @param mapping
	 * @return ActionForward hacia la pagina de filtros
	 */
	public ActionForward valoresInicialesFiltro(MotivosModificacionPresupuestoForm forma,ActionMapping mapping){
		forma.setActivoFiltro(true);
		forma.setDescripcionFiltro("");
		forma.setCodigoFiltro("");
		return  mapping.findForward("filtrar");
	}

	public void guardar(UsuarioBasico usuario,MotivosModificacionPresupuestoForm forma,ActionMapping mapping){

		//FIXME aca voy
		//LLAMADO A SERVICIO DE ELIMINAR 
		eliminarDEBaseDeDatos(forma);

		//LLAMADO AL METODO GUARDAR
		guardarActualizarMotivosModificacion(usuario, forma);

		if (forma.getIndicadorTipoConsulta()==false) {
			//LLAMADIO AL CONSULTAR PARA VER LOS CAMBIOS HECHOS EN LA BD
			consultarMotivosModifiacion(forma);
		}else{
			consultarFiltro(forma, mapping);

		}

	}


	/**
	 * Metodo de carga e inicializacion de elementos a usar en el cu
	 * @param forma
	 * @param response
	 * @param request
	 * @param mapping
	 * @return Estado inicial 
	 */
	private ActionForward valoreiniciales(MotivosModificacionPresupuestoForm forma, HttpServletResponse response, HttpServletRequest request, ActionMapping mapping){
		forma.inicializar();
		
		//SE CONSULTAN TODOS LOS MOTIVOS DE MODIFICACION
		consultarMotivosModifiacion(forma);
		return  mapping.findForward("inicial");

	}

	/**
	 * Metodo que consulta todas los motivos de modificacion.
	 * @param forma
	 */
	public void consultarMotivosModifiacion(MotivosModificacionPresupuestoForm forma){
		UtilidadTransaccion.getTransaccion().begin();;

		try 
		{
			forma.setIndicadorTipoConsulta(false);
			forma.setListaMotivosModificacionPresupuesto(new ArrayList<DtoMotivosModifiPresupuesto>());
			
			//CONSULTA A MUNDO
			forma.setListaMotivosModificacionPresupuesto(motivosModificacionPresupuestoMundo.consultarTodosMotivosModificacion());
			
			//SE INCIALIZA LA LISTA DE ELEMENTOS ELIMINADOS QUE ESTAN CARGADOS EN MEMORIA
			forma.setListaElementosAEliminar(new ArrayList<Long>());

			UtilidadTransaccion.getTransaccion().commit();

		}//CONTROL DE ERRORES
		catch (Exception e) { 
			Log4JManager.error("No se pudieron consultar los motivos", e);
			UtilidadTransaccion.getTransaccion().rollback();
		}

	}

	/**
	 * Se eliminan motivos de modificacion cargados en memoria
	 * @param forma
	 */
	public void eliminarRegistro(MotivosModificacionPresupuestoForm forma)
	{
		//METODO PROPIO DE LA FORMA
		eliminarMotivoModificacion(forma);
	}

	/**
	 * Metodo que se encarga de eliminar motivos de modificacion de base de datos
	 * @param forma
	 */
	public void eliminarDEBaseDeDatos(MotivosModificacionPresupuestoForm forma)
	{
		UtilidadTransaccion.getTransaccion().begin();
		
		try {

			//SE LLAMA AL SERVICIO DE ELIMINAR ELEMENTOS DE LA BASE DE DATOS.
			motivosModificacionPresupuestoMundo.eliminarMotivosModificacion(forma.getListaElementosAEliminar());
			
			UtilidadTransaccion.getTransaccion().commit();

		}//CONTROL DE ERRORES
		catch (Exception e) 
		{
			UtilidadTransaccion.getTransaccion().rollback();
		}

	}


	/**
	 * 
	 * Metodo que se encarga de guardar y actualizar los motivos de modificacion de presupuesto
	 * @param usuario
	 * @param forma
	 */
	public void guardarActualizarMotivosModificacion(UsuarioBasico usuario,MotivosModificacionPresupuestoForm forma)
	{

		UtilidadTransaccion.getTransaccion().begin();

		try {
			//SE LLAMA AL SERVICIO DE GUARDAR EN BD
			motivosModificacionPresupuestoMundo.guardarModificarMotivosModificacion(usuario,forma.getListaMotivosModificacionPresupuesto()); 

			// SE CAMBIA EL ESTADO A GUARDAR EXITOSO PARA MOSTRAR EL MENSAJE EN PANTALLA
			forma.setEstado(IconstantesEstadosJsp.ESTADO_GUARDAR_EXITOSO);
			
			UtilidadTransaccion.getTransaccion().commit();

		}//CONTROL DE ERRORES  
		catch (Exception e) {
			UtilidadTransaccion.getTransaccion().rollback();
		}

	}



	/**
	 * Metodo encargado de crear un nuevo registro para adicionar un motivo de modificacion
	 * @param forma
	 */
	public ActionForward adicionarMotivoModificacion(MotivosModificacionPresupuestoForm forma,UsuarioBasico usuario,HttpServletRequest request, HttpServletResponse response){
		
		//INICIALIZACION DE INSTACIA A CREAR
		//MotivosModifiPresupuesto nuevoMotivo = new MotivosModifiPresupuesto();
		DtoMotivosModifiPresupuesto nuevoMotivo = new DtoMotivosModifiPresupuesto();
		
		//ADICION DE ELEMENTO A LA LISTA DE MOTIVOS DE MODIFICACION
		forma.getListaMotivosModificacionPresupuesto().add(nuevoMotivo);
		
		
		return UtilidadSesion.redireccionar("",ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()),forma.getListaMotivosModificacionPresupuesto().size(), response, request, "motivosModificacionPresupuesto.jsp",true);
	}


	/**
	 * Metodo encargado de ordenar la lista de motivos
	 * @param forma
	 */
	private void accionOrdenar(MotivosModificacionPresupuestoForm forma) 
	{
		boolean ordenamiento = false;

		//VALIDACION DE ELEMENTOS A ORDENAR ASC O DESC
		if(forma.getEsDescendente().equals(forma.getPatronOrdenar()+"descendente"))
		{
			ordenamiento = true;
		}

		SortGenerico sortG=new SortGenerico(forma.getPatronOrdenar(),ordenamiento);
		Collections.sort(forma.getListaMotivosModificacionPresupuesto(),sortG);


	}



	/**
	 * 
	 * @param forma
	 *
	 * @autor Cristhian Murillo
	 *
	 */
	private void eliminarMotivoModificacion(MotivosModificacionPresupuestoForm forma)
	{
		DtoMotivosModifiPresupuesto motivo = forma.getListaMotivosModificacionPresupuesto().get(Integer.valueOf(forma.getIndiceDetalle()));
		
		if (motivo.getCodigoPk() > 0) 
		{
			forma.getListaElementosAEliminar().add(motivo.getCodigoPk());
		}
		
		forma.getListaMotivosModificacionPresupuesto().remove(motivo);
	}

}
