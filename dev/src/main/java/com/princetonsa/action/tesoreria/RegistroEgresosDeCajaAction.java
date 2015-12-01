/*
 * Abril 15, 2010
 */
package com.princetonsa.action.tesoreria;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.tesoreria.RegistroEgresosDeCajaForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.Cajas;
import com.servinte.axioma.orm.ConcEgrXusuXcatencion;
import com.servinte.axioma.orm.Roles;
import com.servinte.axioma.orm.Usuarios;
import com.servinte.axioma.orm.delegate.RolesDelegate;
import com.servinte.axioma.orm.delegate.UsuariosDelegate;
import com.servinte.axioma.orm.delegate.tesoreria.CajasDelegate;
import com.servinte.axioma.orm.delegate.tesoreria.ConcEgrXusuXcatencionDelegate;
import com.servinte.axioma.orm.delegate.tesoreria.RegistroEgresosDeCajaDelegate;



/**
 * @author Cristhian Murillo
 *
 * Clase usada para controlar los procesos de la funcionalidad .
 * 
 */
public class RegistroEgresosDeCajaAction extends Action 
{
	
	//*********************************
	/**
	 * Define el valor del consecutivo a utilizar para el siguiente registro
	 */
	String consecutivoDefinitivo = null;
	//*********************************
	
	
	
	/**
	 * M&eacute;todo encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, 
	 * HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
								HttpServletResponse response )throws Exception
	{

		if(form instanceof RegistroEgresosDeCajaForm)
		{
			
			/* OBJETOS A USAR */
			/**
			 * Formulario
			 */
			RegistroEgresosDeCajaForm forma = (RegistroEgresosDeCajaForm)form;
			
			
			/** 
			 * Usuario
			 */
			UsuarioBasico usuario 	= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			
			String estado = forma.getEstado(); 
			Log4JManager.info("Estado RegistroEgresosDeCajaAction --> "+estado);
			
			
			if(estado == null)
			{
				request.setAttribute("codigoDescripcionError","errors.estadoInvalido");
				return mapping.findForward("paginaError");
			}
			
			// empezar
			else if( (estado.equals("empezar")) || (estado.equals("resumen")) )
			{
				return accionEmpezar(mapping, forma, usuario, request);
			}
			
			// nuevo
			else if(estado.equals("nuevo"))
			{
				return accionNuevo(mapping, forma, usuario, request);
			}
			
			// guardar
			else if(estado.equals("guardar"))
			{
				return accionGuardar(mapping, forma, usuario, request);
			}
			
			// volver
			else if(estado.equals("volver"))
			{
				return accionEmpezar(mapping,forma, usuario, request);
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
	private ActionForward accionEmpezar(ActionMapping mapping, RegistroEgresosDeCajaForm forma, UsuarioBasico usuario, HttpServletRequest request)
	{
		if(validarConsecutivioDeEgresos(usuario, request)){
			forma.reset();
			llenarListaCajas(forma, usuario, request);
		}
		else {
			forma.reset();
			mostrarErrorConsecutivo(forma, request);
		}
		
		return mapping.findForward("principal");
		
	}
		

	/**
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionNuevo(ActionMapping mapping, RegistroEgresosDeCajaForm forma, UsuarioBasico usuario, HttpServletRequest request)
	{
		forma.reset();
		
		if(forma.getCajaSeleccionada() != null)
		{
			if(validarPermisoUsuarioParaEgreso(usuario, request))
			{
				llenarListaConceptosEgresoXusuXca(forma, usuario);
				forma.setMostrarFormularioIngreso(true);
			}
			else 
			{
				mostrarErrorPermisosUsuario(forma, request);
			}
		}
		else {
			mostrarErrorCaja(forma, request);
		}

		llenarListaCajas(forma, usuario, request);
		return mapping.findForward("principal");
	}
	
	
	


	/**
	 * @param mapping
	 * @param forma
	 * @param request 
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardar(ActionMapping mapping,	RegistroEgresosDeCajaForm forma, UsuarioBasico usuario, HttpServletRequest request)
	{
		if(validarCamposUnicos(forma, request))
		{
			if(validarMontoEgreso(forma, request, usuario))
			{
				guardarDto(forma, usuario,request);
			}
			
		}
		
		llenarListaCajas(forma, usuario, request);
		return mapping.findForward("principal");
	}
	
	
	
	/**
	 * 
	 * @param forma
	 * @param usuario
	 * @param request
	 * @return
	 */
	private boolean guardarDto (RegistroEgresosDeCajaForm forma, UsuarioBasico usuario, HttpServletRequest request)
	{
		if(validarConsecutivioDeEgresosObtener(usuario, request))
		{
			HibernateUtil.beginTransaction();
			
			llenarDetallesForma(forma, usuario);
			new RegistroEgresosDeCajaDelegate().persist(forma.getDto());
			/*
			 * se modifico el anexo y ya no existe el consecutivo de egresos
			UtilidadBD.cambiarUsoFinalizadoConsecutivo(
							EmunConsecutivosTesoreriaCentroAtencion.Egresos.getNombreConsecutivoBaseDatos(), 
							usuario.getCodigoInstitucionInt(), 
							getConsecutivoDefinitivo(), 
							ConstantesBD.acronimoSi, 
							ConstantesBD.acronimoSi);
			*/
			HibernateUtil.endTransaction();
		}
		else {
			/*
			 * se modifico el anexo y ya no existe el consecutivo de egresos
			UtilidadBD.cambiarUsoFinalizadoConsecutivo(
					EmunConsecutivosTesoreriaCentroAtencion.Egresos.getNombreConsecutivoBaseDatos(), 
					usuario.getCodigoInstitucionInt(), 
					getConsecutivoDefinitivo(), 
					ConstantesBD.acronimoNo, 
					ConstantesBD.acronimoNo);
			*/
			mostrarErrorConsecutivo(forma, request);
		}
		
		forma.reset();
		forma.setEstado("resumen");
		
		return true;
	}
	
	

	
	
	/* METODOS PARA LLENADO DE DATOS E INFORMACION ADICIONAL */
	
	/**
	 * Ingresa en el dto los datos de usuario
	 * @param usuario
	 * @param forma
	 */
	private void llenarDetallesForma(RegistroEgresosDeCajaForm forma, UsuarioBasico usuario)
	{
		HibernateUtil.beginTransaction();
		forma.getDto().setUsuarios(new UsuariosDelegate().findById(usuario.getLoginUsuario()));
		forma.getDto().setFechaModifica(UtilidadFecha.getFechaActualTipoBD());
		forma.getDto().setHoraModifica(UtilidadFecha.getHoraActual());
		forma.getDto().setCajas(forma.getCajaSeleccionada());
		forma.getDto().setConsecutivo(getConsecutivoDefinitivo());

		String estadoEgreso = "";
		if(forma.isAutorizado()){
			estadoEgreso = ConstantesIntegridadDominio.acronimoEstadoEgresoCajaAutorizado;
		}else {
			estadoEgreso = ConstantesIntegridadDominio.acronimoEstadoEgresoCajaPendiente;
		}

		forma.getDto().setEstado(estadoEgreso);
		
		HibernateUtil.endTransaction();
	}
	
	
	/** 
	 * Carga una lista de artefactos necesarios en la forma
	 * @param usuario
	 * @param forma
	 */
	private void llenarListaConceptosEgresoXusuXca(RegistroEgresosDeCajaForm forma, UsuarioBasico usuario) 
	{
		// listaConcEgrXusuXcatencion
		HibernateUtil.beginTransaction();
		ArrayList<ConcEgrXusuXcatencion> listaConceptosEgresoXusuXca = new ConcEgrXusuXcatencionDelegate()
			.listarActivosYconcepEgresoActivoPorInstitucionYCentroAtencion(
						Integer.parseInt(usuario.getCodigoInstitucion()), usuario.getCodigoCentroAtencion()	
			);
		
		for(ConcEgrXusuXcatencion objList: listaConceptosEgresoXusuXca)
		{
			objList.getCodigoPk();
			objList.getValorMaximoAutorizado();
			objList.getConceptosDeEgreso().getCodigo();
			objList.getConceptosDeEgreso().getDescripcion();
		}

		forma.setListaConceptosEgresoXusuXca(listaConceptosEgresoXusuXca);
		listaConceptosEgresoXusuXca = null;
		HibernateUtil.endTransaction();
	}
	
	
	
	/** 
	 * Carga una lista de artefactos necesarios en la forma
	 * @param usuario
	 * @param forma
	 * @param request
	 */
	private void llenarListaCajas(RegistroEgresosDeCajaForm forma, UsuarioBasico usuario, HttpServletRequest request) 
	{
		HibernateUtil.beginTransaction();
		
		ArrayList<Cajas> listaCajas = new ArrayList<Cajas>();
		
		if(UtilidadTexto.getBoolean(ValoresPorDefecto.getRequiereAperturaCierreCaja(usuario.getCodigoInstitucionInt())))
		{
			// Caja Abierta para el usuario en sesi&oacute;n
			
			listaCajas.add(new CajasDelegate().findById(usuario.getCodigoCaja()));
		}
		else
		{
			// Todas las cajas asociadas
			listaCajas = new CajasDelegate().listarCajasPorUsuarioPorInstitucion(usuario);
		}
		
		if(listaCajas == null){
			mostrarErrorCaja(forma, request);
		}
		
		
		forma.setListaCajas(listaCajas);
		listaCajas = null;
		
		HibernateUtil.endTransaction();
	}
	
	
	
	
	
	/* METODOS DE VALIDACION */

	
	/**
	 * 	Valida que los campos unicos de la tabla no sean repetidos por otro dto
	 * 	Retorna true si los datos son validos
	 * @param forma
	 * @param request 
	 */
	private boolean validarCamposUnicos(RegistroEgresosDeCajaForm forma, HttpServletRequest request)
	{
		HibernateUtil.beginTransaction();
		RegistroEgresosDeCajaDelegate delegate 	= new RegistroEgresosDeCajaDelegate();
		ActionErrors errores 					= new ActionErrors();
		
				
		if(delegate.buscarPorCampo("consecutivo", forma.getDto().getConsecutivo()) != null)
		{
			errores.add("error campo repetido", new ActionMessage("errors.yaExisteCampoEnTabla", "Consecutivo. Consultar con el Administrador del sistema"));
		}

		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			return false;
		}
		HibernateUtil.endTransaction();
		
		return true;
	}

	
	
	
	/**
	 * Realiza un llamado las validaciones para los consecutivos de tesoreria
	 * @param usuario
	 * @param request
	 * @return
	 */
	private boolean validarConsecutivioDeEgresos(UsuarioBasico usuario, HttpServletRequest request)
	{
		BigDecimal consecutivo = null;
		boolean consecutivoBool = true;
		
		if(ValoresPorDefecto.getManejaConsecutivoFacturaPorCentroAtencionBool(usuario.getCodigoInstitucionInt()))
		{
			/*
			 * se modifico el anexo y ya no existe el consecutivo de egresos
			consecutivo = ConsecutivosCentroAtencion.obtenerValorActualConsecutivo(
							usuario.getCodigoCentroAtencion(), 
							EmunConsecutivosTesoreriaCentroAtencion.Egresos.getNombreConsecutivoBaseDatos(), 
							UtilidadFecha.getMesAnioDiaActual("anio"));
			*/
			if(consecutivo == null || consecutivo.equals("0"))	
			{
				consecutivoBool = false;
			}
			else 
			{
				/*
				consecutivo = ConsecutivosCentroAtencion.incrementarConsecutivoXCentroAtencion(
									usuario.getCodigoCentroAtencion(), 
									EmunConsecutivosTesoreriaCentroAtencion.Egresos.getNombreConsecutivoBaseDatos(), 
									usuario.getLoginUsuario());
				
				setConsecutivoDefinitivo(consecutivo+"");
				*/
				consecutivoBool = true;
			}
		}
		
		else
		{
			/*
			 * se modifico el anexo y ya no existe el consecutivo de egresos
			String consecutivoStr = UtilidadBD.obtenerValorConsecutivoDisponible(
					EmunConsecutivosTesoreriaCentroAtencion.Egresos.getNombreConsecutivoBaseDatos(), 
					usuario.getCodigoInstitucionInt());
			
			if( (consecutivoStr == null) || (consecutivoStr.equals("0")) )
			{
				consecutivoBool = false;
			}
			else
			{
				//setConsecutivoDefinitivo(consecutivoStr);
				consecutivoBool = true;
			}
			*/
		}
		
		if (consecutivoBool == true){
			/*
			 * se modifico el anexo y ya no existe el consecutivo de egresos
			UtilidadBD.cambiarUsoFinalizadoConsecutivo(
					EmunConsecutivosTesoreriaCentroAtencion.Egresos.getNombreConsecutivoBaseDatos(), 
										usuario.getCodigoInstitucionInt(), 
										getConsecutivoDefinitivo(), 
										ConstantesBD.acronimoSi, 
										ConstantesBD.acronimoNo);
			*/
		}
		
		return consecutivoBool;
	}
	

	
	
	/**
	 * Verifica si el usuario tiene permisos para efectuar el egreso
	 * @param usuario
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean validarPermisoUsuarioParaEgreso(UsuarioBasico usuario, HttpServletRequest request) 
	{
		HibernateUtil.beginTransaction();
		String[] listaConstantesStr = {   
				ConstantesIntegridadDominio.acronimoActividadTipoUsuarioGenSolEgreso,
				ConstantesIntegridadDominio.acronimoActividadTipoUsuarioAutoSolEgreso
		};
		
		HibernateUtil.beginTransaction();
		
		// Lista los roles que tienen permisos de realizar el egreso
		ArrayList<Roles> listaRolesPermisoEgreso = new RolesDelegate()
			.ListarPorActividad(listaConstantesStr);
		
		Usuarios usuActual = new UsuariosDelegate().findById(usuario.getLoginUsuario());
		Iterator it = usuActual.getRoleses().iterator();
		
		boolean permiso = false;
		while(it.hasNext()){
			Roles r = new Roles();
			r = (Roles) it.next();
			for(Roles rol : listaRolesPermisoEgreso)
			{
				if(rol.getNombreRol().equals(r.getNombreRol())){
					permiso = true;
				}
			}
		}

		HibernateUtil.endTransaction();
		
		return permiso;
	}
	
	
	
	/**
	 * 	Valida que el monto del egreso no supere al  establecido en el concepto de egreso por usuario
	 * @param forma
	 * @param request 
	 * @param usuario
	 */
	private boolean validarMontoEgreso(RegistroEgresosDeCajaForm forma, HttpServletRequest request, UsuarioBasico usuario)
	{
		BigDecimal valorEgreso = forma.getDto().getValorEgreso();
		BigDecimal valorMaximo = forma.getDto().getConcEgrXusuXcatencion().getValorMaximoAutorizado();
		
		if( (valorEgreso.max(valorMaximo)).equals(valorMaximo))
		{
			forma.setAutorizado(true);
			return true;
		}
		else
		{
			forma.setAutorizado(false);
			guardarDto(forma, usuario, request);
			mostrarErrorMonto(forma, request);
			return false;
		}
	}
	
	
	
	/**
	 * Realiza un llamado las validaciones para los consecutivos de tesoreria y toma un valor para el consecutivo
	 * @param usuario
	 * @param request
	 * @return
	 */
	private boolean validarConsecutivioDeEgresosObtener(UsuarioBasico usuario, HttpServletRequest request)
	{
		BigDecimal consecutivo = null;
		boolean consecutivoBool = true;
		
		if(ValoresPorDefecto.getManejaConsecutivoFacturaPorCentroAtencionBool(usuario.getCodigoInstitucionInt()))
		{
			/*
			 * se modifico el anexo y ya no existe el consecutivo de egresos

			consecutivo = ConsecutivosCentroAtencion.obtenerValorActualConsecutivo(
							usuario.getCodigoCentroAtencion(), 
							EmunConsecutivosTesoreriaCentroAtencion.Egresos.getNombreConsecutivoBaseDatos(), 
							UtilidadFecha.getMesAnioDiaActual("anio"));
			
			if(consecutivo == null || consecutivo.equals("0"))	
			{
				consecutivoBool = false;
			}
			else 
			{
				consecutivo = ConsecutivosCentroAtencion.incrementarConsecutivoXCentroAtencion(
									usuario.getCodigoCentroAtencion(), 
									EmunConsecutivosTesoreriaCentroAtencion.Egresos.getNombreConsecutivoBaseDatos(), 
									usuario.getLoginUsuario());
				
				setConsecutivoDefinitivo(consecutivo+"");
				consecutivoBool = true;
			}
			*/
		}
		
		else
		{
			/*
			 * se modifico el anexo y ya no existe el consecutivo de egresos

			String consecutivoStr = UtilidadBD.obtenerValorConsecutivoDisponible(
					EmunConsecutivosTesoreriaCentroAtencion.Egresos.getNombreConsecutivoBaseDatos(), 
					usuario.getCodigoInstitucionInt());
			
			if( (consecutivoStr == null) || (consecutivoStr.equals("0")) )
			{
				consecutivoBool = false;
			}
			else
			{
				setConsecutivoDefinitivo(consecutivoStr);
				consecutivoBool = true;
			}
			*/
		}
		
		if (consecutivoBool == true){
			/*
			 * se modifico el anexo y ya no existe el consecutivo de egresos

			UtilidadBD.cambiarUsoFinalizadoConsecutivo(
					EmunConsecutivosTesoreriaCentroAtencion.Egresos.getNombreConsecutivoBaseDatos(), 
										usuario.getCodigoInstitucionInt(), 
										getConsecutivoDefinitivo(), 
										ConstantesBD.acronimoSi, 
										ConstantesBD.acronimoNo);
			*/
		}
		
		return consecutivoBool;
	}

	
	
	
	
	
	/* METODOS DE INFORMACION DE ERRORES */

	/**
	 * Si no existen consecutivos para el egreso, muestra una pagina de error
	 * @param forma
	 * @param request
	 */
	private void mostrarErrorConsecutivo(RegistroEgresosDeCajaForm forma, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		errores.add("error no_consecutivo_egreso", new ActionMessage("error.tesoreria.faltaConsecutivo"));
		saveErrors(request, errores);
		forma.setEstado("empezar");
	}
	
	/**
	 * Si no se ha seleccionado la caja, muestra una pagina de error
	 * @param forma
	 * @param request
	 */
	private void mostrarErrorCaja(RegistroEgresosDeCajaForm forma, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		errores.add("error caja_no_seleccionada", new ActionMessage("errors.faltaDefinirCajaParaUsuario"));
		saveErrors(request, errores);
		forma.setEstado("empezar");
	}
	
	/**
	 * Si el usuario no tiene permisos para realizar un egreso, muestra una pagina de error
	 * @param forma
	 * @param request
	 */
	private void mostrarErrorPermisosUsuario(RegistroEgresosDeCajaForm forma, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		errores.add("error permisos_usuario_egreso", new ActionMessage("errors.usuarioSinRolFuncionalidad","actual","Registro de Egresos de Caja"));
		saveErrors(request, errores);
		forma.setEstado("empezar");
	}
	
	
	
	/**
	 * Si el usuario no tiene permisos para realizar un egreso, muestra una pagina de error
	 * @param forma
	 * @param request
	 */
	private void mostrarErrorMonto(RegistroEgresosDeCajaForm forma, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		errores.add("error valor_egreso_superado", new ActionMessage("error.tesoreria.montoSuperado"));
		errores.add("error valor_egreso_superado_guardar", new ActionMessage("error.tesoreria.montoSuperadoGuardar"));
		saveErrors(request, errores);
		forma.setEstado("empezar");
	}
	
	
	
	
	/**
	 * @return the consecutivoDefinitivo
	 */
	public String getConsecutivoDefinitivo() {
		return consecutivoDefinitivo;
	}


	/**
	 * @param consecutivoDefinitivo the consecutivoDefinitivo to set
	 */
	public void setConsecutivoDefinitivo(String consecutivoDefinitivo) {
		this.consecutivoDefinitivo = consecutivoDefinitivo;
		Log4JManager.info("El consecutivo a utilizar sera: "+this.consecutivoDefinitivo);
	}
	
	
	
}	
	
	
	