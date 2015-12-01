/*
 * Abril 20, 2010
 */
package com.princetonsa.action.interfaz;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.actionform.interfaz.CuentasXEspOdontoForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.CentroAtencion;
import com.servinte.axioma.orm.CuentasContables;
import com.servinte.axioma.orm.CuentasXEspOdonto;
import com.servinte.axioma.orm.Especialidades;
import com.servinte.axioma.orm.delegate.UsuariosDelegate;
import com.servinte.axioma.orm.delegate.administracion.CentroAtencionDelegate;
import com.servinte.axioma.orm.delegate.administracion.EspecialidadesDelegate;
import com.servinte.axioma.orm.delegate.interfaz.CuentasContablesDelegate;
import com.servinte.axioma.orm.delegate.interfaz.CuentasXEspOdontoDelegate;
import com.servinte.axioma.persistencia.UtilidadTransaccion;


/**
 * @author Cristhian Murillo
 *
 * Clase usada para controlar los procesos de la funcionalidad .
 * 
 */
public class CuentasXEspOdontoAction extends Action 
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

		if(form instanceof CuentasXEspOdontoForm)
		{
			/* OBJETOS A USAR */
			/**
			 * Formulario
			 */
			CuentasXEspOdontoForm forma = (CuentasXEspOdontoForm)form;
			
			/** 
			 * Usuario
			 */
			UsuarioBasico usuario 		= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			
			String estado = forma.getEstado(); 
			Log4JManager.info("Estado CuentasXEspOdontoAction --> "+estado); 
			
			if(estado == null)
			{
				request.setAttribute("codigoDescripcionError","errors.estadoInvalido");
				return mapping.findForward("paginaError");
			}
			
			// empezar
			else if( (estado.equals("empezar")) || (estado.equals("resumen")) )
			{
				return accionEmpezar(mapping, forma, usuario);
			}
			
			// guardar
			else if(estado.equals("guardar"))
			{
				return accionGuardar(mapping, forma, usuario, request);
			}
			
			// volver
			else if(estado.equals("volver"))
			{
				return accionEmpezar(mapping,forma, usuario);
			}
						
		}
		return null;
	}
	
	

	/**
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezar(ActionMapping mapping, CuentasXEspOdontoForm forma, UsuarioBasico usuario)
	{
		forma.reset();
		mostrarLista(forma, usuario);
		llenarListaCentroAtencion(forma, usuario);
		return mapping.findForward("principal");
	}
		
	
	
	/**
	 * @param mapping
	 * @param forma
	 * @param request 
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardar(ActionMapping mapping,	CuentasXEspOdontoForm forma, UsuarioBasico usuario, HttpServletRequest request)
	{
		HibernateUtil.beginTransaction();
		for(CuentasXEspOdonto objListaDto : forma.getListaDto())
		{
			objListaDto = cambiarCerosPorNull(objListaDto);
			llenarDetallesForma(objListaDto, usuario);
			
			new CuentasXEspOdontoDelegate().attachDirty(objListaDto);
		}
		UtilidadTransaccion.getTransaccion().commit();
		
		forma.reset();
		mostrarLista(forma, usuario);
		llenarListaCentroAtencion(forma, usuario);
		forma.setEstado("resumen");
		return mapping.findForward("principal");
	}
	
	
	
	/**
	 * Coloca null si el codigo de alguna cuenta es cero 
	 * @param objListaDto
	 */
	private CuentasXEspOdonto cambiarCerosPorNull(CuentasXEspOdonto objListaDto)
	{
		
		if(objListaDto.getCuentasContablesByCuentaContable()!=null  &&  objListaDto.getCuentasContablesByCuentaContable().getCodigo() == 0)
		{
			objListaDto.setCuentasContablesByCuentaContable(null);
		}
		
		if(objListaDto.getCuentasContablesByCuentaVigAnterior()!=null && objListaDto.getCuentasContablesByCuentaVigAnterior().getCodigo() == 0)
		{
			objListaDto.setCuentasContablesByCuentaVigAnterior(null);
		}
		
		if (objListaDto.getCuentasContablesByCuentaCostoHonorarios()!=null && objListaDto.getCuentasContablesByCuentaCostoHonorarios().getCodigo() == 0)
		{
			objListaDto.setCuentasContablesByCuentaCostoHonorarios(null);
		}
		
		return objListaDto;
	}
	
	

	/**
	 * Ingresa en el dto los datos de usuario
	 * @param usuario
	 * @param forma
	 */
	private void llenarDetallesForma(CuentasXEspOdonto dto, UsuarioBasico usuario)
	{
		dto.setUsuarios(new UsuariosDelegate().findById(usuario.getLoginUsuario()));
		dto.setFechaModifica(UtilidadFecha.getFechaActualTipoBD());
		dto.setHoraModifica(UtilidadFecha.getHoraActual());
		dto.setCentroAtencion(new CentroAtencionDelegate().findById(usuario.getCodigoCentroAtencion()));
	}
	
	
	
	/** 
	 * Carga una lista de artefactos necesarios en la forma
	 * @param usuario
	 * @param forma
	 */
	private void mostrarLista(CuentasXEspOdontoForm forma, UsuarioBasico usuario) 
	{
		// lista de registros
		ArrayList<CuentasXEspOdonto> listaCuentasXEspOdonto = new ArrayList<CuentasXEspOdonto>();
		
		HibernateUtil.beginTransaction();
		if(UtilidadTexto.isEmpty(forma.getStrCentroAtencion()))
		{
			listaCuentasXEspOdonto = new CuentasXEspOdontoDelegate().listarTodosPorEspecialidad(
					usuario.getCodigoInstitucionInt(), 
					ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica);
		}
		else 
		{
			listaCuentasXEspOdonto = new CuentasXEspOdontoDelegate().listarPorEspecialidadPorInstitucionPorCentroAtencion(
					usuario.getCodigoInstitucionInt(), 
					ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica,
					Integer.parseInt(forma.getStrCentroAtencion()) );
		}
		//---
		
		
		// Especialidades cargadas para asignar cuenta
		ArrayList<Especialidades> listaEspecialidades = new EspecialidadesDelegate()
			.listarEspecialidadesSinCuentasXEspecialidad(usuario.getCodigoInstitucionInt());		
		for(Especialidades especialidad : listaEspecialidades)
		{ 
			CuentasXEspOdonto newCuentaEspOdonto = new CuentasXEspOdonto();
			newCuentaEspOdonto.setEspecialidades(especialidad);
			listaCuentasXEspOdonto.add(newCuentaEspOdonto);
		}
		//-- 
		
		
		
		for(CuentasXEspOdonto cuentaXEO : listaCuentasXEspOdonto)
		{ 
			UtilidadTransaccion.getTransaccion().begin();
			try{
				cuentaXEO.setEspecialidades(new EspecialidadesDelegate().findById(cuentaXEO.getEspecialidades().getCodigo()));
				cuentaXEO.getEspecialidades().getNombre();
				
				UtilidadTransaccion.getTransaccion().commit();
				HibernateUtil.cerrarSession();
				UtilidadTransaccion.getTransaccion().begin();
				if(cuentaXEO.getCuentasContablesByCuentaContable() != null){
					cuentaXEO.setCuentasContablesByCuentaContable(
						new CuentasContablesDelegate().findById(cuentaXEO.getCuentasContablesByCuentaContable().getCodigo()));
				}else{ cuentaXEO.setCuentasContablesByCuentaContable(new CuentasContables()); }
				
				UtilidadTransaccion.getTransaccion().commit();
				HibernateUtil.cerrarSession();
				UtilidadTransaccion.getTransaccion().begin();
				if(cuentaXEO.getCuentasContablesByCuentaVigAnterior() != null){
					cuentaXEO.setCuentasContablesByCuentaVigAnterior(
						new CuentasContablesDelegate().findById(cuentaXEO.getCuentasContablesByCuentaVigAnterior().getCodigo()));
				}else{ cuentaXEO.setCuentasContablesByCuentaVigAnterior(new CuentasContables()); }
				
				UtilidadTransaccion.getTransaccion().commit();
				HibernateUtil.cerrarSession();
				UtilidadTransaccion.getTransaccion().begin();
				if(cuentaXEO.getCuentasContablesByCuentaCostoHonorarios() != null){
					cuentaXEO.setCuentasContablesByCuentaCostoHonorarios(
						new CuentasContablesDelegate().findById(cuentaXEO.getCuentasContablesByCuentaCostoHonorarios().getCodigo()));
				}else{ cuentaXEO.setCuentasContablesByCuentaCostoHonorarios(new CuentasContables()); }
				
			}catch (NullPointerException e) { 	}
			UtilidadTransaccion.getTransaccion().commit();
		}
		
		forma.setListaDto(listaCuentasXEspOdonto);
		listaCuentasXEspOdonto = null;
		UtilidadTransaccion.getTransaccion().commit();
	}
	
	
	
	/** 
	 * Carga una lista de artefactos necesarios en la forma
	 * @param usuario
	 * @param forma
	 */
	private void llenarListaCentroAtencion(CuentasXEspOdontoForm forma, UsuarioBasico usuario) 
	{
		// listaCentroAtencion
		UtilidadTransaccion.getTransaccion().begin();
		ArrayList<CentroAtencion> listaCentroAtencion = new CentroAtencionDelegate()
			.listarTodosActivosPorInstitucion(Integer.parseInt(usuario.getCodigoInstitucion()));
		for(CentroAtencion objList: listaCentroAtencion)
		{
			objList.getConsecutivo();
			objList.getCodigo();
			objList.getDescripcion();
		}

		forma.setListaCentroAtencion(listaCentroAtencion);
		listaCentroAtencion = null;
		UtilidadTransaccion.getTransaccion().commit();
	}
	
	
	
}	
	
	
	

	
	