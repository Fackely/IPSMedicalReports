package com.princetonsa.action.odontologia;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadClonar;
import util.UtilidadFecha;
import util.UtilidadLog;
import util.UtilidadTexto;
import util.Utilidades;
import util.facturacion.UtilidadesFacturacion;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.actionform.odontologia.ServicioHonorariosForm;
import com.princetonsa.dto.interfaz.DtoCuentaContable;
import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.princetonsa.dto.odontologia.DtoDetalleAgrupacionHonorarios;
import com.princetonsa.dto.odontologia.DtoDetalleServicioHonorarios;
import com.princetonsa.dto.odontologia.DtoServicioHonorarios;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.IngresarModificarContratosEntidadesSubcontratadas;
import com.princetonsa.mundo.odontologia.DetalleAgrupacionHonorario;
import com.princetonsa.mundo.odontologia.DetalleServicioHonorario;
import com.princetonsa.mundo.odontologia.ServicioHonorario;
import com.princetonsa.sort.odontologia.SortDetalleAgrupacionHonorario;
import com.princetonsa.sort.odontologia.SortDetalleServicioHonorario;
import com.princetonsa.sort.odontologia.SortServicioHonorario;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.orm.CuentasContables;
import com.servinte.axioma.orm.delegate.interfaz.CuentasContablesDelegate;


/**
 * 
 * @author axioma
 *
 */
public class ServicioHonorarioAction extends Action 
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(ServicioHonorarioAction.class);
	
	@Override
	public ActionForward execute(	ActionMapping mapping, 
									ActionForm form,
									HttpServletRequest request, 
									HttpServletResponse response)throws Exception 
	{
		if (form instanceof ServicioHonorariosForm) 
		{
			ServicioHonorariosForm forma = (ServicioHonorariosForm) form;
			logger.info("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxestado-->" + forma.getEstado());
			UsuarioBasico usuario = (UsuarioBasico) request.getSession().getAttribute("usuarioBasico");
			/**
			 * 
			 * 
			 * 
			 *  estado empezar
			 */
			if (forma.getEstado().equals("empezar")) 
			{
				return accionEmpezar(mapping, forma, usuario);
			}
			if (forma.getEstado().equals("empezarDetalle")) 
			{
				return accionEmpezarDetalle(mapping, forma, usuario);
			}
			
			/**
			 * 
			 *  estado Guardar
			 */
			else if (forma.getEstado().equals("guardar")) 
			{
				return accionGuardar(mapping, request, forma, usuario);
			}
			else if(forma.getEstado().equals("mostrarErroresGuardar"))
			{	
				return mapping.findForward("paginaPrincipal");
			}
			else if (forma.getEstado().equals("guardar_detalle")) 
			{
				return accionGuardarDetalle(mapping, request, forma, usuario);
			}
			else if(forma.getEstado().equals("mostrarErroresGuardarDetalle") 
					|| forma.getEstado().equals("mostrarErroresGuardarModificarDetalle")
					|| forma.getEstado().equals("mostrarErroresGuardarAgrupacion")
					|| forma.getEstado().equals("mostrarErroresGuardarModificarAgrupacion"))
			{
				return mapping.findForward("paginaDetalles");
			}
			else if (forma.getEstado().equals("guardar_agrupacion")) 
			{
				return accionGuardarAgrupacion(mapping, request, forma, usuario);
			}
			
			/***
			 * 
			 */
						
			else if (forma.getEstado().equals("consultar")) 
			{
				return accionConsultar(mapping, forma);
			}
			
			/***
			 * 
			 * Estado Modificar
			 */
			else if (forma.getEstado().equals("modificar")) 
			{
				return accionModificar(mapping, forma ,request);
			}
			else if (forma.getEstado().equals("modificar_detalle")) 
			{
				
				return accionModificarDetalle(mapping, forma, request);
			}
			else if (forma.getEstado().equals("modificar_agrupacion")) 
			{
				return accionModificarAgrupacion(mapping, forma ,request);
			}
			else if (forma.getEstado().equals("modificar_cuenta_detalle")) 
			{
				return accionModificarCuentaDetalle(mapping, forma);
			}
			else if (forma.getEstado().equals("modificar_cuenta_agrupacion")) 
			{
				return accionModificarCuentaAgrupacion(mapping, forma);
			}
			
			/***
			 * 
			 * Estado Guardar
			 * 
			 */
			
			else if (forma.getEstado().equals("guardarModificar")) 
			{
				return accionGuardarModificar(mapping, forma , usuario);
			}
			else if (forma.getEstado().equals("guardarModificar_detalle")) 
			{
				return accionGuardarModificarDetalle(mapping, forma ,usuario);
			}
			else if (forma.getEstado().equals("guardarModificar_agrupacion")) 
			{
				return accionGuardarModificarAgrupacion(mapping, forma ,usuario);
			}
			
			
			/***
			 * 
			 * Estado Eliminar
			 * 
			 */
			else if (forma.getEstado().equals("eliminar")) 
			{
				return accionEliminar(mapping, forma, usuario);
			}

			else if (forma.getEstado().equals("eliminar_detalle")) 
			{
				return accionEliminarDetalle(mapping, forma , usuario);
			}

			else if (forma.getEstado().equals("eliminar_agrupacion")) 
			{
				return accionEliminarAgrupacion(mapping, forma , usuario);
			}
			
			
			/***
			 * 
			 * Estado Ordenar
			 * 
			 */
			else if (forma.getEstado().equals("ordenar")) 
			{
				return this.accionOrdenar(mapping, forma);
			}
			else if (forma.getEstado().equals("ordenar_detalle")) 
			{
				return this.accionOrdenarDetalles(mapping, forma);
			}
			else if (forma.getEstado().equals("ordenar_agrupacion")) 
			{
				return this.accionOrdenarAgrupacion(mapping, forma);
			}
			else if (forma.getEstado().equals("ingresar_cuenta_agrupacion")) 
			{
				return this.accionInsertarCuentaAgrupacion(mapping, forma);
			}
			
			
			/****
			 * 
			 * Estado Ingresar
			 * 
			 */
			
			else if (forma.getEstado().equals("ingresar")) 
			{
				return this.accionIngresar(forma, mapping, request);
			}
			else if (forma.getEstado().equals("ingresar_detalle")) 
			{
				return this.accionIngresarDetalle(forma, mapping, request);
			}
			else if (forma.getEstado().equals("ingresar_agrupacion")) 
			{
				return this.accionIngresarAgrupacion(forma, mapping, request);
			}
			
			/***
			 * 
			 * 
			 */
			
			else if (forma.getEstado().equals("buscar")) 
			{
				return this.accionBuscar(forma, mapping, request);
			}
			else
			{
				return accionIncorrecta(mapping, request, forma);
			}
		}
		return null;
	}

	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @return
	 */
	private ActionForward accionModificarCuentaAgrupacion(
			ActionMapping mapping, ServicioHonorariosForm forma) {
		
		
		return mapping.findForward("paginaDetalles");
	}
	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @return
	 */

	private ActionForward accionModificarCuentaDetalle(ActionMapping mapping,
			ServicioHonorariosForm forma) {
		
		logger.info(forma.getDtoDetalleServicioHonorario().getArrayCuentasContables().get(0).getDescripcion());
		logger.info(forma.getDtoDetalleServicioHonorario().getArrayCuentasContables().get(1).getDescripcion());
		logger.info(forma.getDtoDetalleServicioHonorario().getArrayCuentasContables().get(2).getDescripcion());
		
		return mapping.findForward("paginaDetalles");
	}
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @return
	 */

	private ActionForward accionInsertarCuentaAgrupacion(ActionMapping mapping,
			ServicioHonorariosForm forma) {
		
		return mapping.findForward("paginaDetalles");
	}
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param request
	 * @return
	 */

	private ActionForward accionIngresarAgrupacion(
			ServicioHonorariosForm forma, ActionMapping mapping,
			HttpServletRequest request) {
		    UsuarioBasico usuario = (UsuarioBasico) request.getSession().getAttribute("usuarioBasico");
		    forma.setEspecialidades(Utilidades.obtenerEspecialidades());
		    
		    Connection con = UtilidadBD.abrirConexion();
		    forma.setListaTiposDeServicios(UtilidadesFacturacion.obtenerTiposServicio(con,"", ""));
		    forma.setListaGruposDeServicios(IngresarModificarContratosEntidadesSubcontratadas.obtenerGruposServicio(con, ConstantesBD.acronimoSi));
		    UtilidadBD.closeConnection(con);
		    forma.getDtoDetalleAgrupacionHonorario().reset();
		    forma.getDtoDetalleAgrupacionHonorario().setCodigoHonorario(forma.getListaServiciosHonorario().get(forma.getPosArray()).getCodigo());
		return mapping.findForward("paginaDetalles");
	}
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param request
	 * @return
	 */

	private ActionForward accionIngresarDetalle(ServicioHonorariosForm forma,
		ActionMapping mapping, HttpServletRequest request) {
		
		UsuarioBasico usuario = (UsuarioBasico) request.getSession().getAttribute("usuarioBasico");
	   
		forma.getDtoDetalleServicioHonorario().reset();
		forma.getDtoDetalleServicioHonorario().setCodigoHonorario(forma.getListaServiciosHonorario().get(forma.getPosArray()).getCodigo());
		forma.setEspecialidades(Utilidades.obtenerEspecialidades());
		return mapping.findForward("paginaDetalles");
	}
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 * @throws IPSException 
	 */

	private ActionForward accionEliminarAgrupacion(ActionMapping mapping,
		
		ServicioHonorariosForm forma , UsuarioBasico usuario) throws IPSException {
		forma.setDtoDetalleAgrupacionHonorario(forma.getListaDetalleAgrupacionHonorario().get(forma.getPosArrayAgrupacion()));
		UtilidadLog.generarLog(usuario, forma.getDtoDetalleAgrupacionHonorario(),null, ConstantesBD.tipoRegistroLogEliminacion , ConstantesBD.logHonorariosEspecialidadServicios);
		DetalleAgrupacionHonorario.eliminar(forma.getDtoDetalleAgrupacionHonorario());
		
		return accionEmpezarDetalle(mapping, forma, usuario);
	}
    
	/**
	 * 
	 * @param usuario
	 * @param dto
	 */
	
	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 * @throws IPSException 
	 */
	
	private ActionForward accionEliminarDetalle(ActionMapping mapping,
		
			
			
		ServicioHonorariosForm forma , UsuarioBasico usuario) throws IPSException {
		forma.setDtoDetalleServicioHonorario(forma.getListaDetalleServicioHonorario().get(forma.getPosArrayDetalle()));
		UtilidadLog.generarLog(usuario, forma.getDtoDetalleServicioHonorario() , null ,ConstantesBD.tipoRegistroLogEliminacion , ConstantesBD.logHonorariosEspecialidadServicios);
		DetalleServicioHonorario.eliminar(forma.getDtoDetalleServicioHonorario());
		
	   
		return accionEmpezarDetalle(mapping, forma, usuario);
	}
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @return
	 */

	private ActionForward accionOrdenarAgrupacion(ActionMapping mapping,
			ServicioHonorariosForm forma) {
		
		logger.info("patron->" + forma.getPatronOrdenar());
		Collections.sort(forma.getListaDetalleAgrupacionHonorario(), new SortDetalleAgrupacionHonorario(forma.getPatronOrdenar()) );
		return mapping.findForward("paginaDetalles");
		
	}

	
	
	/**
	 * ACCION GUARDAR MODIFICAR AGRUPACION DE DETALLE HONORARIOS
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 * @throws IPSException 
	 */
	private ActionForward accionGuardarModificarAgrupacion(
			ActionMapping mapping, ServicioHonorariosForm forma  , UsuarioBasico usuario) throws IPSException {
		
		DtoDetalleAgrupacionHonorarios dtoWhere= new DtoDetalleAgrupacionHonorarios(); 
		dtoWhere.setCodigo(forma.getListaDetalleAgrupacionHonorario().get(forma.getPosArrayAgrupacion()).getCodigo());
		
		
		
		DtoDetalleAgrupacionHonorarios dtoNuevo= new DtoDetalleAgrupacionHonorarios(); 		
		dtoNuevo=forma.getDtoDetalleAgrupacionHonorario();
		dtoNuevo.setFechaModificada(UtilidadFecha.getFechaActual());
		dtoNuevo.setHoraModificada(UtilidadFecha.getHoraActual());
		dtoNuevo.setCodigoHonorario(forma.getListaDetalleAgrupacionHonorario().get(forma.getPosArrayAgrupacion()).getCodigoHonorario());
		
		
		
		
		DetalleAgrupacionHonorario.modificar(dtoNuevo, dtoWhere, true);
		dtoWhere.setCodigoHonorario(forma.getDtoServicioHonorario().getCodigo());
		dtoNuevo.setCodigoHonorario(forma.getDtoServicioHonorario().getCodigo());
		
		
		DtoDetalleAgrupacionHonorarios tmpDtoAgrupacion = new DtoDetalleAgrupacionHonorarios();
		tmpDtoAgrupacion = forma.getListaDetalleAgrupacionHonorario().get(forma.getPosArrayAgrupacion()).clone();
		
		accionCargarInformacionCuentas(dtoNuevo, usuario);
		accionCargarInformacionCuentas(tmpDtoAgrupacion, usuario);
		
		
		
		
		UtilidadLog.generarLog(usuario, tmpDtoAgrupacion ,dtoNuevo,ConstantesBD.tipoRegistroLogModificacion , ConstantesBD.logHonorariosEspecialidadServicios);
		return accionEmpezarDetalle(mapping, forma, usuario);
		
		
	}

	
	

	/**
	 * ACCION CARGA INFORMACION CUENTAS
	 * @param dtoNuevo
	 */
	private void accionCargarInformacionCuentas(DtoDetalleAgrupacionHonorarios dtoNuevo, UsuarioBasico usuario) 
	{
		//LISTA TMP PARA BUSCAR CODIGOS
		ArrayList<Long> listaCodigoBuscar = new ArrayList<Long>();
	
		//LLENAR LA LISTA
		for(DtoCuentaContable dtoCuentas : dtoNuevo.getArrayCuentasContables())
		{
			if(!UtilidadTexto.isEmpty(dtoCuentas.getCodigo()))
			{
				listaCodigoBuscar.add(Long.parseLong(dtoCuentas.getCodigo()));
			}
		}
		
		//INSTANCIA DEL DELEGATE CUENTAS PARA BUSCAR LAS CUENTAS
		CuentasContablesDelegate objDelegate = new CuentasContablesDelegate();
		ArrayList<CuentasContables> listTmpCuentas= objDelegate.consultarCuentasContables(listaCodigoBuscar);
		
		
		for (CuentasContables dtoCuentasHibernate: listTmpCuentas ) //EL MAXIMO DE ITERACIONES SON 3
		{
			for(DtoCuentaContable dtoCuentas : dtoNuevo.getArrayCuentasContables())
			{
				if(String.valueOf(dtoCuentasHibernate.getCodigo()).equals(dtoCuentas.getCodigo())  )
				{
					if( dtoCuentasHibernate.getNaturalezaCuenta()!=null)
					{
						dtoCuentas.setNaturalezaCuenta(dtoCuentasHibernate.getNaturalezaCuenta().toString());
					}
					
					dtoCuentas.setAnioVigencia(dtoCuentasHibernate.getAnioVigencia());
					dtoCuentas.setDescripcion(dtoCuentasHibernate.getDescripcion());
					dtoCuentas.setCuentaContable(dtoCuentasHibernate.getCuentaContable());
					dtoCuentas.setCodigoInstitucion(usuario.getCodigoInstitucionInt());
					dtoCuentas.setMensaje("");
				}
			}
			
		}
	}
	
	
	
	/**
	 * ACCION CARGA INFORMACION CUENTAS
	 * PARA EL OBJETO 
	 * @param dtoNuevo
	 */
	private void accionCargarInformacionCuentasDetalleServicioHonorarios(DtoDetalleServicioHonorarios dtoNuevo, UsuarioBasico usuairo) 
	{
		//LISTA TMP PARA BUSCAR CODIGOS
		ArrayList<Long> listaCodigoBuscar = new ArrayList<Long>();
	
		//LLENAR LA LISTA
		for(DtoCuentaContable dtoCuentas : dtoNuevo.getArrayCuentasContables())
		{
			if(!UtilidadTexto.isEmpty(dtoCuentas.getCodigo()))
			{
				listaCodigoBuscar.add(Long.parseLong(dtoCuentas.getCodigo()));
			}
		}
		
		//INSTANCIA DEL DELEGATE CUENTAS PARA BUSCAR LAS CUENTAS
		CuentasContablesDelegate objDelegate = new CuentasContablesDelegate();
		ArrayList<CuentasContables> listTmpCuentas= objDelegate.consultarCuentasContables(listaCodigoBuscar);
		
		
		for (CuentasContables dtoCuentasHibernate: listTmpCuentas ) //EL MAXIMO DE ITERACIONES SON 3
		{
			for(DtoCuentaContable dtoCuentas : dtoNuevo.getArrayCuentasContables())
			{
				if(String.valueOf(dtoCuentasHibernate.getCodigo()).equals(dtoCuentas.getCodigo())  )
				{
					if( dtoCuentasHibernate.getNaturalezaCuenta()!=null)
					{
						dtoCuentas.setNaturalezaCuenta(dtoCuentasHibernate.getNaturalezaCuenta().toString());
					}
					
					dtoCuentas.setAnioVigencia(dtoCuentasHibernate.getAnioVigencia());
					dtoCuentas.setDescripcion(dtoCuentasHibernate.getDescripcion());
					dtoCuentas.setCuentaContable(dtoCuentasHibernate.getCuentaContable());
					dtoCuentas.setCodigoInstitucion(usuairo.getCodigoInstitucionInt());
					dtoCuentas.setMensaje("");
				}
			}
			
		}
	}
    /**
     * 
     * @param mapping
     * @param forma
     * @param usuario
     * @return
     * @throws IPSException 
     */
	private ActionForward accionGuardarModificarDetalle(ActionMapping mapping,
			ServicioHonorariosForm forma , UsuarioBasico usuario) throws IPSException {
	
		DtoDetalleServicioHonorarios dtoWhere= new DtoDetalleServicioHonorarios(); 
		dtoWhere.setCodigo(forma.getListaDetalleServicioHonorario().get(forma.getPosArrayDetalle()).getCodigo());
		
		DtoDetalleServicioHonorarios dtoNuevo= new DtoDetalleServicioHonorarios(); 		
		dtoNuevo=forma.getDtoDetalleServicioHonorario();
		dtoNuevo.setFechaModifica(UtilidadFecha.getFechaActual());
		dtoNuevo.setHoraModifica(UtilidadFecha.getHoraActual());
		if(dtoNuevo.getPorcentajeParticipacion() <= 100){
		
		DetalleServicioHonorario.modificar(dtoNuevo, dtoWhere,true);
		dtoWhere.setCodigoHonorario(forma.getDtoServicioHonorario().getCodigo());
		dtoNuevo.setCodigoHonorario(forma.getDtoServicioHonorario().getCodigo());
		//
		
		DtoDetalleServicioHonorarios tmpDtoDetalleServicioHonorario = new DtoDetalleServicioHonorarios();
		tmpDtoDetalleServicioHonorario = forma.getListaDetalleServicioHonorario().get(forma.getPosArrayDetalle());
		
		accionCargarInformacionCuentasDetalleServicioHonorarios(dtoNuevo, usuario);
		accionCargarInformacionCuentasDetalleServicioHonorarios(tmpDtoDetalleServicioHonorario,usuario);
		
		UtilidadLog.generarLog(usuario, forma.getListaDetalleServicioHonorario().get(forma.getPosArrayDetalle()) ,dtoNuevo,ConstantesBD.tipoRegistroLogModificacion , ConstantesBD.logHonorariosEspecialidadServicios);
		}
		return accionEmpezarDetalle(mapping, forma, usuario);
	}
	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param request
	 * @return
	 */

	private ActionForward accionModificarAgrupacion(ActionMapping mapping,
			ServicioHonorariosForm forma , HttpServletRequest request ) 
	{
		
		
		//forma.setDtoDetalleAgrupacionHonorario( (DtoDetalleAgrupacionHonorarios)forma.getListaDetalleAgrupacionHonorario().get(forma.getPosArrayAgrupacion()).clone() );
		//forma.setDtoDetalleAgrupacionHonorario(PropUtil.copyCompleto(forma.getListaDetalleAgrupacionHonorario().get(forma.getPosArrayAgrupacion()), DtoDetalleAgrupacionHonorarios.class));
		
		forma.setDtoDetalleAgrupacionHonorario((DtoDetalleAgrupacionHonorarios)UtilidadClonar.clone(forma.getListaDetalleAgrupacionHonorario().get(forma.getPosArrayAgrupacion())));
	  
		 forma.getDtoDetalleAgrupacionHonorario().getArrayCuentasContables().set(0 , (DtoCuentaContable)forma.getListaDetalleAgrupacionHonorario().get(forma.getPosArrayAgrupacion()).getArrayCuentasContables().get(0).clone());
		 forma.getDtoDetalleAgrupacionHonorario().getArrayCuentasContables().set(1 , (DtoCuentaContable)forma.getListaDetalleAgrupacionHonorario().get(forma.getPosArrayAgrupacion()).getArrayCuentasContables().get(1).clone());
		 forma.getDtoDetalleAgrupacionHonorario().getArrayCuentasContables().set(2 , (DtoCuentaContable)forma.getListaDetalleAgrupacionHonorario().get(forma.getPosArrayAgrupacion()).getArrayCuentasContables().get(2).clone());	  
		
		Connection con = UtilidadBD.abrirConexion();
	    
	    forma.setEspecialidades(Utilidades.obtenerEspecialidades());	    
	    forma.setListaTiposDeServicios(UtilidadesFacturacion.obtenerTiposServicio(con,"", ""));
	    
	    forma.setListaGruposDeServicios(IngresarModificarContratosEntidadesSubcontratadas.obtenerGruposServicio(con, ConstantesBD.acronimoSi));
         
	    UtilidadBD.closeConnection(con);	    
		return mapping.findForward("paginaDetalles");
	}
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param request
	 * @return
	 */

	private ActionForward accionModificarDetalle(ActionMapping mapping,
			ServicioHonorariosForm forma , HttpServletRequest request) {
		
		forma.setDtoDetalleServicioHonorario((DtoDetalleServicioHonorarios)forma.getListaDetalleServicioHonorario().get(forma.getPosArrayDetalle()).clone() );
	    forma.getDtoDetalleServicioHonorario().getArrayCuentasContables().set(0 , (DtoCuentaContable)forma.getListaDetalleServicioHonorario().get(forma.getPosArrayDetalle()).getArrayCuentasContables().get(0).clone());
		forma.getDtoDetalleServicioHonorario().getArrayCuentasContables().set(1 , (DtoCuentaContable)forma.getListaDetalleServicioHonorario().get(forma.getPosArrayDetalle()).getArrayCuentasContables().get(1).clone());
		forma.getDtoDetalleServicioHonorario().getArrayCuentasContables().set(2 , (DtoCuentaContable)forma.getListaDetalleServicioHonorario().get(forma.getPosArrayDetalle()).getArrayCuentasContables().get(2).clone());	  
		UsuarioBasico usuario = (UsuarioBasico) request.getSession().getAttribute("usuarioBasico");
		forma.setEspecialidades(Utilidades.obtenerEspecialidades());
		 return mapping.findForward("paginaDetalles");
	}
	
	/**
	 * 
	 * @param mapping
	 * @param request
	 * @param forma
	 * @param usuario
	 * @return
	 */

	private ActionForward accionGuardarAgrupacion(ActionMapping mapping,
			HttpServletRequest request, ServicioHonorariosForm forma,
			UsuarioBasico usuario) {
		forma.getDtoDetalleAgrupacionHonorario().setFechaModificada(UtilidadFecha.getFechaActual());
		forma.getDtoDetalleAgrupacionHonorario().setHoraModificada(UtilidadFecha.getHoraActual());
		forma.getDtoDetalleAgrupacionHonorario().setUsuarioModifica(usuario.getLoginUsuario());
		forma.getDtoDetalleAgrupacionHonorario().setCodigoHonorario(forma.getListaServiciosHonorario().get(forma.getPosArray()).getCodigo());
		
		forma.getDtoDetalleAgrupacionHonorario().getArrayCuentasContables().get(0).setCodigoInstitucion(usuario.getCodigoInstitucionInt());
		forma.getDtoDetalleAgrupacionHonorario().getArrayCuentasContables().get(1).setCodigoInstitucion(usuario.getCodigoInstitucionInt());
		forma.getDtoDetalleAgrupacionHonorario().getArrayCuentasContables().get(2).setCodigoInstitucion(usuario.getCodigoInstitucionInt());
		DetalleAgrupacionHonorario.guardar(forma.getDtoDetalleAgrupacionHonorario());
	    cargarDetalleAgrupacion(forma);
	    return mapping.findForward("paginaDetalles");
	}
	
	
	
	/***
	 * 
	 * @param mapping
	 * @param request
	 * @param forma
	 * @param usuario
	 * @return
	 * @throws IPSException 
	 */

	private ActionForward accionGuardarDetalle(ActionMapping mapping,
			HttpServletRequest request, ServicioHonorariosForm forma,
			UsuarioBasico usuario) throws IPSException {
		
		
		
		forma.getDtoDetalleServicioHonorario().setFechaModifica(UtilidadFecha.getFechaActual());
		forma.getDtoDetalleServicioHonorario().setHoraModifica(UtilidadFecha.getHoraActual());
		forma.getDtoDetalleServicioHonorario().setUsuarioModifica(usuario.getLoginUsuario());
		forma.getDtoDetalleServicioHonorario().setCodigoHonorario(forma.getListaServiciosHonorario().get(forma.getPosArray()).getCodigo());
		
		forma.getDtoDetalleServicioHonorario().getArrayCuentasContables().get(0).setCodigoInstitucion(usuario.getCodigoInstitucionInt());
		forma.getDtoDetalleServicioHonorario().getArrayCuentasContables().get(1).setCodigoInstitucion(usuario.getCodigoInstitucionInt());
		forma.getDtoDetalleServicioHonorario().getArrayCuentasContables().get(2).setCodigoInstitucion(usuario.getCodigoInstitucionInt());
		
		
		DetalleServicioHonorario.guardar(forma.getDtoDetalleServicioHonorario());
	    cargarDetalleServicios(forma);
		
        return mapping.findForward("paginaDetalles");
	}

	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 * @throws IPSException 
	 */
	private ActionForward accionEmpezarDetalle(	ActionMapping mapping,
												ServicioHonorariosForm forma, UsuarioBasico usuario) throws IPSException 
	{
		cargarDetalleAgrupacion(forma);
		cargarDetalleServicios(forma);
		return mapping.findForward("paginaDetalles");
		
	}
	
	/**
	 * 
	 * @param forma
	 * @throws IPSException 
	 */
	private void cargarDetalleServicios(ServicioHonorariosForm forma) throws IPSException {
		DtoDetalleServicioHonorarios dtoDetalleServ= new DtoDetalleServicioHonorarios();
		dtoDetalleServ.setCodigoHonorario(forma.getListaServiciosHonorario().get(forma.getPosArray()).getCodigo());
		forma.setListaDetalleServicioHonorario(DetalleServicioHonorario.cargar(dtoDetalleServ));
	}

	/**
	 * 
	 * @param forma
	 */
	private void cargarDetalleAgrupacion(ServicioHonorariosForm forma) 
	{
		DtoDetalleAgrupacionHonorarios dtoDetalleAgru= new DtoDetalleAgrupacionHonorarios();
		dtoDetalleAgru.setCodigoHonorario(forma.getListaServiciosHonorario().get(forma.getPosArray()).getCodigo());
		forma.setListaDetalleAgrupacionHonorario(DetalleAgrupacionHonorario.cargar(dtoDetalleAgru));
	}

	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezar(ActionMapping mapping,
										ServicioHonorariosForm forma, 
										UsuarioBasico usuario) 
	{
		forma.reset();
		forma.getDtoServicioHonorario().setInstitucion(usuario.getCodigoInstitucionInt());
		forma.setListaServiciosHonorario(ServicioHonorario.cargar(forma.getDtoServicioHonorario()));
		return mapping.findForward("paginaPrincipal");
	}
	
	/**
	 * 
	 * @param mapping
	 * @param request
	 * @param forma
	 * @return
	 */
	private ActionForward accionIncorrecta(	ActionMapping mapping,
											HttpServletRequest request, 
											ServicioHonorariosForm forma) 
	{
		forma.reset();	
		logger.warn("Estado no valido dentro del flujo de Admin Medicamentos (null) ");
		request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
		return mapping.findForward("paginaError");
	}
	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @return
	 */
	private ActionForward accionEliminar(	ActionMapping mapping,
											ServicioHonorariosForm forma , UsuarioBasico usuario) 
	{
		DtoServicioHonorarios dtoServicioHonorario= new DtoServicioHonorarios();
		dtoServicioHonorario.setCodigo(forma.getListaServiciosHonorario().get(forma.getPosArray()).getCodigo());
		logger.info("accion eliminar ");
		forma.getListaServiciosHonorario().get(forma.getPosArray()).setInstitucion(usuario.getCodigoInstitucionInt());
		ServicioHonorario.eliminar(dtoServicioHonorario);
		UtilidadLog.generarLog(usuario, forma.getListaServiciosHonorario().get(forma.getPosArray()), null,ConstantesBD.tipoRegistroLogEliminacion, ConstantesBD.logHonorariosEspecialidadServicios);
	    return accionEmpezar(mapping, forma, usuario);
	}

	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @return
	 */
	private ActionForward accionGuardarModificar(	ActionMapping mapping,
													ServicioHonorariosForm forma,
													UsuarioBasico usuario) 
	{
		DtoServicioHonorarios dtoWhere= new DtoServicioHonorarios(); 
		
		
		
		dtoWhere.setCodigo(forma.getListaServiciosHonorario().get(forma.getPosArray()).getCodigo());
		
		DtoServicioHonorarios dtoNuevo= new DtoServicioHonorarios(); 		
		dtoNuevo=forma.getDtoServicioHonorario();
		dtoNuevo.setFechaModifica(UtilidadFecha.getFechaActual());
		dtoNuevo.setHoraModifica(UtilidadFecha.getHoraActual());
		//logger.info(UtilidadLog.obtenerString(dtoNuevo, true));
		
		ServicioHonorario.modificar(dtoNuevo, dtoWhere);
		
		forma.getListaServiciosHonorario().get(forma.getPosArray()).setInstitucion(usuario.getCodigoInstitucionInt());
		dtoNuevo.setInstitucion(usuario.getCodigoInstitucionInt());
		
		
		
		UtilidadLog.generarLog(usuario,forma.getListaServiciosHonorario().get(forma.getPosArray()), dtoNuevo ,ConstantesBD.tipoRegistroLogModificacion, ConstantesBD.logHonorariosEspecialidadServicios);
		
		return accionEmpezar(mapping, forma, usuario);
		
	}

	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @return
	 */
	private ActionForward accionModificar(	ActionMapping mapping,
											ServicioHonorariosForm forma,
											HttpServletRequest request) 
	{
		UsuarioBasico usuario = (UsuarioBasico) request.getSession().getAttribute("usuarioBasico");
		forma.setEsquemasTarifarios(Utilidades.obtenerEsquemasTarifarios(false,	usuario.getCodigoInstitucionInt()));
		DtoCentrosAtencion dtoCa= new DtoCentrosAtencion();
		dtoCa.setCodInstitucion(usuario.getCodigoInstitucionInt());
		dtoCa.setActivo(Boolean.TRUE);
		forma.setListaCentrosAtencion(UtilidadesManejoPaciente.obtenerCentrosAtencion(dtoCa));
		forma.setDtoServicioHonorario((DtoServicioHonorarios)(forma.getListaServiciosHonorario().get(forma.getPosArray())).clone());
		return mapping.findForward("paginaPrincipal");
	}
	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @return
	 */
	private ActionForward accionConsultar(	ActionMapping mapping,
											ServicioHonorariosForm forma) 
	{
		forma.setListaServiciosHonorario(ServicioHonorario.cargar(forma.getDtoServicioHonorario()));
		return mapping.findForward("paginaPrincipal");
	}

	/**
	 * 
	 * @param mapping
	 * @param request
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardar(	ActionMapping mapping,
											HttpServletRequest request, 
											ServicioHonorariosForm forma,
											UsuarioBasico usuario) 
	{
		logger.info("Esquema :"+forma.getDtoServicioHonorario().getEsquemaTarifario().getCodigo()+" Centro Atencion: "+forma.getDtoServicioHonorario().getCentroAtencion().getCodigo());
		forma.getDtoServicioHonorario().setFechaModifica(UtilidadFecha.getFechaActual());
		forma.getDtoServicioHonorario().setHoraModifica(UtilidadFecha.getHoraActual());
		forma.getDtoServicioHonorario().setUsuarioModifica(usuario.getLoginUsuario());
		forma.getDtoServicioHonorario().setInstitucion(usuario.getCodigoInstitucionInt());
		ServicioHonorario.guardar(forma.getDtoServicioHonorario());
		return accionEmpezar(mapping, forma, usuario);
	}

	
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionBuscar(	ServicioHonorariosForm forma,
										ActionMapping mapping, 
										HttpServletRequest request) 
	{
		forma.setResultadoBusqueda(ServicioHonorario.cargar(forma.getDtoServicioHonorario()));
		return mapping.findForward("busqueda");
	}

	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionIngresar(	ServicioHonorariosForm forma,
											ActionMapping mapping, 
											HttpServletRequest request) 
	{
		UsuarioBasico usuario = (UsuarioBasico) request.getSession().getAttribute("usuarioBasico");
		forma.setEsquemasTarifarios(Utilidades.obtenerEsquemasTarifarios(false,	usuario.getCodigoInstitucionInt()));
		DtoCentrosAtencion dtoCa= new DtoCentrosAtencion();
		dtoCa.setCodInstitucion(usuario.getCodigoInstitucionInt());
		dtoCa.setActivo(true);
		forma.setListaCentrosAtencion(UtilidadesManejoPaciente.obtenerCentrosAtencion(dtoCa));
		return mapping.findForward("paginaPrincipal");
	}
	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @return
	 */
	private ActionForward accionOrdenar(	ActionMapping mapping,
											ServicioHonorariosForm forma) 
	{
		
		logger.info("patron->" + forma.getPatronOrdenar());
		Collections.sort(forma.getListaServiciosHonorario(),new SortServicioHonorario(forma.getPatronOrdenar()));
		return mapping.findForward("paginaPrincipal");
	}
	
	/***
	 * 
	 * 
	 * @param mapping
	 * @param forma
	 * @return
	 */
	
	private ActionForward accionOrdenarDetalles(	ActionMapping mapping,
			                                         ServicioHonorariosForm forma) 
	{
		logger.info("patron->" + forma.getPatronOrdenar());
		Collections.sort(forma.getListaDetalleServicioHonorario(), new SortDetalleServicioHonorario(forma.getPatronOrdenar()) );
		return mapping.findForward("paginaDetalles");
	}
}