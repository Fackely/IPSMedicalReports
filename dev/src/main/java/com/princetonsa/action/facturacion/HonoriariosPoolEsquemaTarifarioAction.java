package com.princetonsa.action.facturacion;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadLog;
import util.Utilidades;
import util.facturacion.InfoTarifaVigente;
import util.facturacion.UtilidadesFacturacion;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.actionform.facturacion.HonoriariosPoolEsquemaTarifarioForm;
import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.princetonsa.dto.odontologia.DtoAgrupHonorariosPool;
import com.princetonsa.dto.odontologia.DtoHonorarioPoolServicio;
import com.princetonsa.dto.odontologia.DtoHonorariosPool;
import com.princetonsa.dto.odontologia.DtoInfoFechaUsuario;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.cargos.EsquemaTarifario;
import com.princetonsa.mundo.facturacion.HonoriariosPoolEsquemaTarifario;
import com.princetonsa.mundo.facturacion.IngresarModificarContratosEntidadesSubcontratadas;
import com.princetonsa.mundo.pooles.Pooles;
import com.princetonsa.sort.odontologia.SortGenerico;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * 
 * @author axioma
 *ANEXO 961
 */
public class HonoriariosPoolEsquemaTarifarioAction  extends Action{
	
	Logger logger = Logger.getLogger(HonoriariosPoolEsquemaTarifarioAction.class);
	
	/**
	 * 
	 */
	public ActionForward execute(   ActionMapping mapping,	
									ActionForm form, HttpServletRequest request,	
									HttpServletResponse response ) throws Exception	
	{
		UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
		ActionErrors errores = new ActionErrors();

		if (form instanceof  HonoriariosPoolEsquemaTarifarioForm) 
		{	
			HonoriariosPoolEsquemaTarifarioForm forma = (HonoriariosPoolEsquemaTarifarioForm)form;	
			logger.info("\n\nESTADO--->"+forma.getEstado()+"\n\n");
			if(forma.getEstado().equals("empezar"))
			{
				return accionEmpezar(mapping, usuario, forma, false /*esFlujoConvenio*/, false);
			}
			else if(forma.getEstado().equals("empezarConvenio"))
			{
				return accionEmpezar(mapping, usuario, forma, true /*esFlujoConvenio*/, false);
			}
			else if(forma.getEstado().equals("ordenar"))
			{
				return accionOrdenar(mapping, forma);
			}
			else if(forma.getEstado().equals("modificar"))
			{
				return accionModificar(mapping, forma ,request);
			}
			else if(forma.getEstado().equals("guardarModificar"))
			{
				return accionGuardarModificar(mapping, forma , usuario);
			}
			else if (forma.getEstado().equals("eliminar")) 
			{
				return accionEliminar(mapping, forma, usuario);
			}
			else if (forma.getEstado().equals("ingresar")) 
			{
				return this.accionIngresar(forma, mapping, request);
			}
			else if (forma.getEstado().equals("guardar")) 
			{
				return accionGuardar(mapping, request, forma, usuario);
			}
			else if(forma.getEstado().equals("empezarDetalle"))
			{
				return accionEmpezarDetalle(mapping, usuario, forma);
			}
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			

			//<< acciones Guardar >>

			else if(forma.getEstado().equals("guardarHonoAgrupacion"))
			{
				this.acccionGuardarAgrupacionHonoriarios(forma, usuario, errores);
				return mapping.findForward("paginaPrincipal");
			}

			else if(forma.getEstado().equals("guardarServicios"))
			{
				this.accionGuardarHonorarios(forma,usuario,errores);
				return mapping.findForward("paginaPrincipal");
			}
			//<<Recargas de paginas>>
			else if(forma.getEstado().equals("nuevoServicio"))
			{
				forma.reset3();
				return mapping.findForward("paginaPrincipal");
			}
			else if(forma.getEstado().equals("nuevaPoolServicios") )
			{
				forma.reset3();
				return mapping.findForward("paginaPrincipal");
			}
			else if(forma.getEstado().equals("recarga"))
			{
				this.accionCargarTarifaFechaVigencia(forma, usuario);
				return mapping.findForward("paginaPrincipal");
			}
			//<<Modificaciones>>
			else if(forma.getEstado().equals("modificarPoolServicios"))
			{
				this.accionModificarPoolServicios(forma);
				return mapping.findForward("paginaPrincipal");  
			}
			else if(forma.getEstado().equals("modificarServicio"))
			{
				this.accionModifcarServicio(forma);
				return mapping.findForward("paginaPrincipal");  
			}
			//<<GuardarModificaciones>>
			else if(forma.getEstado().equals("guardarModificarPoolServicios")) //<agrupacion
			{
				this.accionGuardarModificarPoolServicio(forma,usuario);
				return mapping.findForward("paginaPrincipal");  
			}
			else if(forma.getEstado().equals("guardarmodificarServicio")) // <servicio 
			{
				this.accionGuardarModificarServicio(forma, usuario);
				return mapping.findForward("paginaPrincipal");  
			}
			else if(forma.getEstado().equals("eliminarPoolServicio"))
			{
				this.accionEliminarPoolServicios(forma, usuario); //<<agrupacion ¿
				return mapping.findForward("paginaPrincipal");  
			}
			else if(forma.getEstado().equals("eliminarServicios"))
			{
				this.accionEliminarServicios(forma, usuario); //<<servicios
				return mapping.findForward("paginaPrincipal");  
			}

		}

		return null;
	}

	/**
	 * @param mapping
	 * @param usuario
	 * @param forma
	 * @return
	 */
	private ActionForward accionEmpezar(	ActionMapping mapping,	
											UsuarioBasico usuario, 
											HonoriariosPoolEsquemaTarifarioForm forma,
											boolean esFlujoConvenio,
											boolean mensajeExitoso) 
	{
		forma.reset();
		forma.setEsConvenio(esFlujoConvenio);
		forma.setMensajeExitoso(mensajeExitoso);
		DtoHonorariosPool dto= new DtoHonorariosPool();
		dto.setInstitucion(usuario.getCodigoInstitucionInt());
		dto.setEsConvenio(esFlujoConvenio);
		forma.setListaHonorarios(HonoriariosPoolEsquemaTarifario.cargarHonorariosPool(dto));
		return mapping.findForward("listaHonorarios");
	}
	
	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @return
	 */
	private ActionForward accionOrdenar(	ActionMapping mapping,
											HonoriariosPoolEsquemaTarifarioForm forma) 
	{
		logger.info("patron->" + forma.getPatronOrdenar());
		forma.setMensajeExitoso(false);
		boolean descendente= false;
		String patron=forma.getPatronOrdenar();
		if(forma.getPatronOrdenar().contains("descendente"))
		{
			patron=forma.getPatronOrdenar().replaceAll("descendente", "");
			descendente = true;
		}
		SortGenerico sortG=new SortGenerico(patron,descendente);
		Collections.sort(forma.getListaHonorarios(),sortG);
		if(forma.isEsConvenio())
		{
			forma.setEstado("empezarConvenio");
		}
		else
		{
			forma.setEstado("empezar");
		}
		return mapping.findForward("listaHonorarios");
		
	}

	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param request
	 * @return
	 */
	private ActionForward accionModificar(	ActionMapping mapping,
											HonoriariosPoolEsquemaTarifarioForm forma,
											HttpServletRequest request) 
	{
		forma.setMensajeExitoso(false);
		cargarListas(forma, request);
		forma.setDtoHonorarios(forma.getListaHonorarios().get(forma.getPosArrayEncabezado()));
		
		return mapping.findForward("listaHonorarios");
	}

	/**
	 * @param forma
	 * @param request
	 */
	private void cargarListas(HonoriariosPoolEsquemaTarifarioForm forma,
			HttpServletRequest request) {
		UsuarioBasico usuario = (UsuarioBasico) request.getSession().getAttribute("usuarioBasico");
		
		DtoCentrosAtencion dtoCa= new DtoCentrosAtencion();
		dtoCa.setCodInstitucion(usuario.getCodigoInstitucionInt());
		dtoCa.setActivo(true);
		forma.setListaCentrosAtencion(UtilidadesManejoPaciente.obtenerCentrosAtencion(dtoCa));
		if(!forma.isEsConvenio())
		{	
			cargarEsquemasTarifarios(forma, usuario);
		}
		else
		{
			cargarConvenios(forma);
		}
		forma.setListaPooles(Pooles.cargarPooles(usuario.getCodigoInstitucionInt(), true/* POOLES ACTIVOS*/));
	}

	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardarModificar(	ActionMapping mapping,
													HonoriariosPoolEsquemaTarifarioForm forma, 
													UsuarioBasico usuario) 
	{
		boolean mensajeExitoso=HonoriariosPoolEsquemaTarifario.modificarHonorarioPool(forma.getDtoHonorarios());
		return this.accionEmpezar(mapping, usuario, forma, forma.isEsConvenio(), mensajeExitoso);
	}

	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEliminar(ActionMapping mapping,	HonoriariosPoolEsquemaTarifarioForm forma, UsuarioBasico usuario) 
	{
		boolean mensajeExitoso=HonoriariosPoolEsquemaTarifario.eliminarHonorarioPool(forma.getListaHonorarios().get(forma.getPosArrayEncabezado()).getCodigoPk());
		return this.accionEmpezar(mapping, usuario, forma, forma.isEsConvenio(), mensajeExitoso);
	}

	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionIngresar(HonoriariosPoolEsquemaTarifarioForm forma, ActionMapping mapping,HttpServletRequest request) 
	{
		forma.setMensajeExitoso(false);
		forma.setDtoHonorarios(new DtoHonorariosPool());
		cargarListas(forma, request);
		return mapping.findForward("listaHonorarios");
	}

	/**
	 * 
	 * @param mapping
	 * @param request
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardar(ActionMapping mapping,HttpServletRequest request,HonoriariosPoolEsquemaTarifarioForm forma, UsuarioBasico usuario) 
	{
		forma.getDtoHonorarios().setUsuarioModifica(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
		forma.getDtoHonorarios().setEsConvenio(forma.isEsConvenio());
		boolean mensajeExitoso= HonoriariosPoolEsquemaTarifario.guardarHonorariosPool(forma.getDtoHonorarios())>0;
		return this.accionEmpezar(mapping, usuario, forma, forma.isEsConvenio(), mensajeExitoso);
	}
	
	/**
	 * @param mapping
	 * @param usuario
	 * @param forma
	 * @return
	 */
	private ActionForward accionEmpezarDetalle(	ActionMapping mapping,
												UsuarioBasico usuario, 
												HonoriariosPoolEsquemaTarifarioForm forma) 
	{
		forma.resetDetalle();
		cargarTiposServiciosGrupos(forma, usuario);
		forma.setDtoHonorarios(forma.getListaHonorarios().get(forma.getPosArrayEncabezado()));
		accionCargarHonorarioSerivicios(forma, usuario);
		accionCargarAgrupacionHonorarios(forma);
		return mapping.findForward("paginaPrincipal");
	}

	
	
	
	
	
	/**
	 * ACCION CARGAR FECHA VIGENCIA DE LA TARIFA.------------------>
	 * @param forma
	 * @param usuario
	 */
	
	private void accionCargarTarifaFechaVigencia(	HonoriariosPoolEsquemaTarifarioForm forma, UsuarioBasico usuario) throws IPSException {
		logger.info("\n\n\n\n\n\n");
		logger.info("-------------------- OBTENER TARIFA ESQUEMA TARIFARIO-----------\n\n\n\n\n");
		int tmpTarifaOficial=EsquemaTarifario.obtenerTarifarioOficialXCodigoEsquemaTar(forma.getDtoHonorarios().getEsquemaTarifario().getCodigo());	
		forma.setInfoTarifa(Cargos.obtenerTarifaBaseServicio(tmpTarifaOficial, forma.getDtoHonorarios().getDtoHonorarioPoolServicio().getServicio().getCodigo(),forma.getDtoHonorarios().getEsquemaTarifario().getCodigo(), ""));
	}








	/**
	 * ACCION ELIMINAR SERVICIOS
	 * @param forma
	 * @param usuario
	 */
	private void accionEliminarServicios(
			HonoriariosPoolEsquemaTarifarioForm forma, UsuarioBasico usuario) {
		
		forma.getDtoHonorarios().setDtoHonorarioPoolServicio(forma.getDtoHonorarios().getListaHonorarioPoolServ().get(forma.getPostArrayServicios()).clone()) ;
		
		UtilidadLog.generarLog(usuario, forma.getDtoHonorarios().getDtoHonorarioPoolServicio(), null, ConstantesBD.tipoRegistroLogEliminacion , ConstantesBD.logHonorariosPooles);
		
		DtoHonorarioPoolServicio newDto = new DtoHonorarioPoolServicio();
		newDto.setCodigoPk(forma.getDtoHonorarios().getDtoHonorarioPoolServicio().getCodigoPk());
		newDto.setHonorarioPool(forma.getDtoHonorarios().getCodigoPk());
		newDto.setUsuarioModifica(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
		HonoriariosPoolEsquemaTarifario.eliminarHonorariosPoolServicio(newDto);
		this.accionCargarHonorarioSerivicios(forma, usuario);
	}







	/**
	 * ACCION ELIMINAR AGRUPACIONES
	 * @param forma
	 * @param usuario
	 */
	private void accionEliminarPoolServicios(
			HonoriariosPoolEsquemaTarifarioForm forma, UsuarioBasico usuario) {
		forma.getDtoHonorarios().setDtoAgrupacionPool(forma.getDtoHonorarios().getListaAgrupacionHonorarios().get(forma.getPostArrayAgrupacion()).clone()); //clonar
		UtilidadLog.generarLog(usuario, forma.getDtoHonorarios().getDtoAgrupacionPool(), null, ConstantesBD.tipoRegistroLogEliminacion , ConstantesBD.logHonorariosPooles);
		DtoAgrupHonorariosPool newDtoEliminar = new DtoAgrupHonorariosPool();
		newDtoEliminar.setCodigoPk(forma.getDtoHonorarios().getDtoAgrupacionPool().getCodigoPk());
		newDtoEliminar.setHonorarioPool(forma.getDtoHonorarios().getDtoAgrupacionPool().getHonorarioPool());
		newDtoEliminar.setUsuarioModifica( new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
		HonoriariosPoolEsquemaTarifario.eliminarAgrupacionHonorario(newDtoEliminar);
		this.accionCargarAgrupacionHonorarios(forma);
	}






	/**
	 * ACCION MODIFICAR SERVICIOS 
	 * @param forma
	 * @param usuario
	 */
	private void accionGuardarModificarServicio(HonoriariosPoolEsquemaTarifarioForm forma, UsuarioBasico usuario) {
		forma.getDtoHonorarios().getDtoHonorarioPoolServicio().setUsuarioModifica( new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
		HonoriariosPoolEsquemaTarifario.modicarHonorarioPoolServicios(forma.getDtoHonorarios().getDtoHonorarioPoolServicio());
		
		UtilidadLog.generarLog(usuario,forma.getDtoHonorarios().getListaHonorarioPoolServ().get(forma.getPostArrayServicios()) , forma.getDtoHonorarios().getDtoHonorarioPoolServicio(), ConstantesBD.tipoRegistroLogModificacion , ConstantesBD.logHonorariosPooles);
		this.accionCargarHonorarioSerivicios(forma, usuario);
	}





	/**
	 * ACCION MODICAR AGRUPACION DE SERVICIOS
	 * @param forma
	 * @param usuario
	 */
	
	private void accionGuardarModificarPoolServicio(HonoriariosPoolEsquemaTarifarioForm forma, UsuarioBasico usuario) 
	{
		forma.getDtoHonorarios().getDtoAgrupacionPool().setUsuarioModifica(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
		HonoriariosPoolEsquemaTarifario.modicarAgrupacionServicios(forma.getDtoHonorarios().getDtoAgrupacionPool());
	
		UtilidadLog.generarLog(usuario,forma.getDtoHonorarios().getListaAgrupacionHonorarios().get(forma.getPostArrayAgrupacion()) , forma.getDtoHonorarios().getDtoAgrupacionPool(), ConstantesBD.tipoRegistroLogModificacion , ConstantesBD.logHonorariosPooles);
		this.accionCargarAgrupacionHonorarios(forma);
	}





	/**
	 * ACCION MODIFICAR SERVICIOS <<HONORARIOS POOL ESQUEMA TARIFARIO >>
	 * @param forma falta el clonable--------->
	 */
	private void accionModifcarServicio(HonoriariosPoolEsquemaTarifarioForm forma) throws IPSException {
		forma.getDtoHonorarios().setDtoHonorarioPoolServicio(forma.getDtoHonorarios().getListaHonorarioPoolServ().get(forma.getPostArrayServicios()).clone());
		/**
		 * TODO REFACTOR ES UNA PRUEB
		 */
		forma.setInfoTarifa( new InfoTarifaVigente());
		int tmpTarifaOficial=EsquemaTarifario.obtenerTarifarioOficialXCodigoEsquemaTar(forma.getDtoHonorarios().getEsquemaTarifario().getCodigo());	
		forma.setInfoTarifa(Cargos.obtenerTarifaBaseServicio(tmpTarifaOficial, forma.getDtoHonorarios().getDtoHonorarioPoolServicio().getServicio().getCodigo(),forma.getDtoHonorarios().getEsquemaTarifario().getCodigo(), ""));
	}

	


	/**
	 * ACCION MODIFICAR POOL SERVICIO
	 * @param forma 
	 */
	private void accionModificarPoolServicios(	HonoriariosPoolEsquemaTarifarioForm forma) {
		forma.getDtoHonorarios().setDtoAgrupacionPool(forma.getDtoHonorarios().getListaAgrupacionHonorarios().get(forma.getPostArrayAgrupacion()).clone());
	}



	
	/**
	 * ACCION CARGAR AGRUPACIONES HONORARIOS
	 * @param forma
	 */
	private void accionCargarAgrupacionHonorarios(	HonoriariosPoolEsquemaTarifarioForm forma) {
		if(forma.getDtoHonorarios().getCodigoPk().doubleValue()>0)
		{
			DtoAgrupHonorariosPool newAgrupacionHonoriario = new DtoAgrupHonorariosPool();
			newAgrupacionHonoriario.setHonorarioPool(forma.getDtoHonorarios().getCodigoPk());
			forma.getDtoHonorarios().setListaAgrupacionHonorarios(new ArrayList<DtoAgrupHonorariosPool>());
			forma.getDtoHonorarios().setListaAgrupacionHonorarios(HonoriariosPoolEsquemaTarifario.cargarAgrupacionHonorariosPool(newAgrupacionHonoriario));
		}
	}



	/**
	 * ACCION CARGAR SERVICIOS POOL DE HONORARIOS
	 * @param forma
	 */
	private void accionCargarHonorarioSerivicios(HonoriariosPoolEsquemaTarifarioForm forma, UsuarioBasico usuario) 
	{
		if( forma.getDtoHonorarios().getCodigoPk().doubleValue()>0)
		{
			DtoHonorarioPoolServicio newdtoPoolServicio  = new DtoHonorarioPoolServicio();
			newdtoPoolServicio.setHonorarioPool(forma.getDtoHonorarios().getCodigoPk());
			forma.getDtoHonorarios().setListaHonorarioPoolServ(new ArrayList<DtoHonorarioPoolServicio>());
			forma.getDtoHonorarios().setListaHonorarioPoolServ(HonoriariosPoolEsquemaTarifario.cargarHonorariosPoolServ(newdtoPoolServicio, usuario.getCodigoInstitucionInt()));
		}
	}

	




	/**
	 * ACCION GUARDAR AGRUPACION DE HONORARIOS SERVICIOS
	 * @param forma
	 * @param usuario
	 * @param errores
	 */
	private void acccionGuardarAgrupacionHonoriarios(HonoriariosPoolEsquemaTarifarioForm forma, UsuarioBasico usuario,	ActionErrors errores) {
		forma.getDtoHonorarios().setUsuarioModifica(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
		
		forma.getDtoHonorarios().setUsuarioModifica(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
		forma.getDtoHonorarios().getDtoAgrupacionPool().setUsuarioModifica(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
		HonoriariosPoolEsquemaTarifario.guardarAgrupacion(errores, forma.getDtoHonorarios());
		this.accionCargarAgrupacionHonorarios(forma);
	}
	
	
	
	




	/**
	 * ACCION GUARDAR HONORARIOS SERVICIOS
	 * @param forma
	 * @param usuario
	 */
	private void accionGuardarHonorarios(HonoriariosPoolEsquemaTarifarioForm forma,	UsuarioBasico usuario, ActionErrors errores) 
	{
		forma.getDtoHonorarios().setUsuarioModifica(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
		forma.getDtoHonorarios().getDtoHonorarioPoolServicio().setUsuarioModifica(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
		HonoriariosPoolEsquemaTarifario.guardarPoolServicios(errores, forma.getDtoHonorarios());
		this.accionCargarHonorarioSerivicios(forma, usuario);
	}

	
	





	
	
	/**
	 * ACCION CARGAR ESQUEMAS TARIFARIOS
	 * @param forma
	 * @param usuario
	 */
	private void cargarEsquemasTarifarios(HonoriariosPoolEsquemaTarifarioForm forma, UsuarioBasico usuario) {
		forma.setListaEsquemasTarifarios(Utilidades.obtenerEsquemasTarifariosInArray(false, usuario.getCodigoInstitucionInt(), ConstantesBD.codigoNuncaValido /*NINGUNA CLASIFICACION */));
	}





	/**
	 * ACCION CARGAR AGRUPACION SERVICIOS
	 * @param forma
	 */
	void cargarAgrupacionServicios(HonoriariosPoolEsquemaTarifarioForm forma){
		
		DtoAgrupHonorariosPool dto = new DtoAgrupHonorariosPool();
		forma.getDtoHonorarios().setListaAgrupacionHonorarios(HonoriariosPoolEsquemaTarifario.cargarAgrupacionHonorariosPool(dto));
		
	}

	
	
	/**
	 * CARGAR TIPOS DE SERVICIOS Y GRUPOS DE SERVICIOS
	 * @param forma
	 * @param usuario
	 */
	private void cargarTiposServiciosGrupos(HonoriariosPoolEsquemaTarifarioForm forma, UsuarioBasico usuario) {
		Connection con = UtilidadBD.abrirConexion();
		forma.setListaTiposDeServicios(UtilidadesFacturacion.obtenerTiposServicio(con,"", ""));
		forma.setListaGruposDeServicios(IngresarModificarContratosEntidadesSubcontratadas.obtenerGruposServicio(con, ConstantesBD.acronimoSi));
		UtilidadBD.closeConnection(con);
	}
	
	
	/**
	 * CARGAR CONVENIOS ACTIVOS
	 * @param forma
	 */
	private void cargarConvenios(HonoriariosPoolEsquemaTarifarioForm forma)
	{
		forma.setListConvenios(Utilidades.obtenerConvenios("", ""/*tipoContrato*/, false/*verificarVencimientoContrato*/, ""/*fechaReferencia*/, true/*activo*/,"", "", "" /*Maneja Promociones*/));
	}

}
