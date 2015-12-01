/*
 * Creado   20/09/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_03
 */
package com.princetonsa.action.tesoreria;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadSesion;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.tesoreria.TarjetasFinancierasForm;
import com.princetonsa.dto.comun.DtoCheckBox;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.tesoreria.EntidadesFinancieras;
import com.princetonsa.mundo.tesoreria.TarjetasFinancierasMundo;
import com.princetonsa.sort.odontologia.SortGenerico;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.ComisionXCentroAtencion;
import com.servinte.axioma.orm.ComisionXCentroAtencionHome;
import com.servinte.axioma.orm.HistComisionXCentroAten;
import com.servinte.axioma.orm.Instituciones;
import com.servinte.axioma.orm.MovimientosTarjetas;
import com.servinte.axioma.orm.TarjetaFinancieraComision;
import com.servinte.axioma.orm.TarjetaFinancieraReteica;
import com.servinte.axioma.orm.TarjetasFinancieras;
import com.servinte.axioma.orm.TiposTarjetaFinanciera;
import com.servinte.axioma.orm.delegate.tesoreria.ComisionXCentroAtencionDelegate;
import com.servinte.axioma.orm.delegate.tesoreria.TarjetasFinancierasDelegate;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.TesoreriaFabricaServicio;
import com.servinte.axioma.servicio.fabrica.odontologia.administracion.AdministracionFabricaServicio;
import com.servinte.axioma.servicio.impl.tesoreria.LocalizacionServicio;
import com.servinte.axioma.servicio.interfaz.administracion.IUsuariosServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ILogTarjetasFinancieraServicio;

/**
 * 
 * Clase para manejar el workflow de 
 * tarjetas financieras
 * @version 1.0, 20/09/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class TarjetasFinancierasAction extends Action
{
	/**
	 * Cadena que contiene los indices del mapa.
	 */
	//private static final String indicesMapa="consecutivo_,codigo_tarjeta_,codigo_entidad_,descripcion_tarjeta_,tipo_tarjeta_financiera_,base_rete_,retefte_,reteica_,comision_,directo_banco_,activo_,tiporegistro_";
	static ILogTarjetasFinancieraServicio logTarjetasFinancieraServicio = TesoreriaFabricaServicio.crearLogTarjetasFinancieraServicio();
	
    /**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(TarjetasFinancierasAction.class);
	
	/**
	 * Método execute del action
	 */
	@Override
	public ActionForward execute(	ActionMapping mapping, 	
													        ActionForm form, 
													        HttpServletRequest request, 
													        HttpServletResponse response) throws Exception
	{
		
	    if(form instanceof TarjetasFinancierasForm)
	    {
	        
			TarjetasFinancierasForm tarjForm = (TarjetasFinancierasForm) form;
			UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());

			String estado=tarjForm.getEstado();
			tarjForm.setMensaje(new ResultadoBoolean(false));
			logger.warn("estado->"+estado);
			tarjForm.setErrorCargarEntidadComision(false);
			
			
			if(estado == null)
			{
				logger.warn("Estado no valido dentro del flujo de ConsultaAjustesAction (null) ");
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				return mapping.findForward("paginaError");
			}
			if(estado.equals("empezar"))
			{			    
				tarjForm.reset();
				return accionEmpezar(tarjForm,mapping,usuario);
			}
			else if(estado.equals("nuevoRegistro"))
			{
				return accionNuevo(tarjForm,mapping,usuario, request, response);
			}
			else if(estado.equals("guardar"))
			{
				if(TarjetasFinancierasMundo.guadrar(tarjForm.getTarjetasFinancieras(), usuario.getLoginUsuario()))
				{
					tarjForm.setMensaje(new ResultadoBoolean(true, "Proceso Exitoso"));
				}
				else
				{
					tarjForm.setMensaje(new ResultadoBoolean(false, "Problemas en la fuente de datos"));
				}
				return mapping.findForward("paginaPrincipal");
				
			}
			else if(estado.equals("eliminar"))
			{
				if(tarjForm.getTarjetasFinancieras().get(tarjForm.getTarjetaFinancieraSeleccionada()).getConsecutivo() > 0){
					if(TarjetasFinancierasMundo.eliminarTarjeta(tarjForm.getTarjetasFinancieras().get(tarjForm.getTarjetaFinancieraSeleccionada()).getConsecutivo(),  usuario.getLoginUsuario()))
					{
						tarjForm.setMensaje(new ResultadoBoolean(true,"El registro se elimino correctamente."));
					}
					else
					{
						tarjForm.setMensaje(new ResultadoBoolean(true,"El registro no se pudo eliminar, por favo verifique."));
					}
				}
				else{
					tarjForm.getTarjetasFinancieras().remove(tarjForm.getTarjetaFinancieraSeleccionada());
				}
				
				return accionEmpezar(tarjForm,mapping,usuario);
			}
			else if(estado.equals("detalle"))
			{
				accionDetalleTarjetaFinanciera(tarjForm, usuario);
				return mapping.findForward("detalleTarjeta");
			}
			else if(estado.equals("cargarEntidad"))
			{
				if(tarjForm.getRegistroSeleccionado()>=0){
					
					accionCargarEntidades(tarjForm);
					accionEliminarEntidadesFinancierasDelListado(tarjForm);
					tarjForm.setRegistroSeleccionado(ConstantesBD.codigoNuncaValido); 
				}
				else{
					tarjForm.setMostrarComision(true);
					tarjForm.setErrorCargarEntidadComision(true);
				}
				
				tarjForm.setMostrarComision(true);
				//return mapping.findForward("detalleTarjeta");
											
				return mapping.findForward("seccionComision");   
				
			}
			else if(estado.equals("eliminarEntidad"))
			{
				tarjForm.getEntidadesEliminadas().add(tarjForm.getComision().get(tarjForm.getRegistroSeleccionadoEliminacion()));
				tarjForm.getComision().remove(tarjForm.getRegistroSeleccionadoEliminacion());
				
				//cargar entidades
				cargarEntidadesFinancierasPorInstitucion(tarjForm);
				
				accionEliminarEntidadesFinancierasDelListado(tarjForm);
				tarjForm.setRegistroSeleccionado(ConstantesBD.codigoNuncaValido);
				tarjForm.setMostrarComision(true);
				
				return mapping.findForward("seccionComision"); 
			}
			else if(estado.equals("cargarCentroAtencion"))
			{
				if(tarjForm.getRegistroSeleccionadoCA()!=ConstantesBD.codigoNuncaValido){
					
					accionCargarCentroAtencion(tarjForm);
					accionEliminarCentroAtencionDelListado(tarjForm);
				}
				
				tarjForm.setMostrarReteICA(true);
				
				return mapping.findForward("seccionReteICA");
			}
			else if(estado.equals("eliminarCentroAtencion")) 
			{
				try{
					HibernateUtil.beginTransaction();
					tarjForm.getCentroAtencionEliminado().add(tarjForm.getReteica().get(tarjForm.getRegistroSeleccionadoEliminacion()));
					tarjForm.getReteica().remove(tarjForm.getRegistroSeleccionadoEliminacion());
					
					//cargar centros antecion
					tarjForm.setListadoCentrosAtencion(com.princetonsa.mundo.parametrizacion.CentroAtencion.cargarCentrosAtencion());
					
					accionEliminarCentroAtencionDelListado(tarjForm);
					tarjForm.setRegistroSeleccionadoCA(ConstantesBD.codigoNuncaValido);
					tarjForm.setMostrarReteICA(true);
					HibernateUtil.endTransaction();
				}
				catch (Exception e) {
					Log4JManager.error(e);
					HibernateUtil.abortTransaction();
				}
				return mapping.findForward("seccionReteICA");
				
			}
			else if(estado.equals("guardarDetalle"))
			{
				if(TarjetasFinancierasMundo.guardarDetalle(tarjForm.getTarjetaFinanciera(),tarjForm.getComision(),tarjForm.getEntidadesEliminadas(),tarjForm.getReteica(),tarjForm.getCentroAtencionEliminado(),usuario.getLoginUsuario()))
				{
					tarjForm.setMensaje(new ResultadoBoolean(true,"Proceso Exitoso."));
				}
				else
				{
					tarjForm.setMensaje(new ResultadoBoolean(true,"Problemas realizando el guardado del detalle. Por favor Verifique"));
				}
				
				accionDetalleTarjetaFinanciera(tarjForm, usuario);
				return mapping.findForward("detalleTarjeta");
				
			}else if(estado.equals("cargarEntidadesPorInstitucion")){
				
				cargarEntidadesFinancierasPorInstitucion(tarjForm);
				
				return mapping.findForward("entidadesPorInstitucion");
	
			}else if(estado.equals("comisionPorCentroAtencion")){
				
				Log4JManager.info("cargando pos: "+tarjForm.getRegistroSeleccionadoComisionCentroAten());
				
				tarjForm.setTarjetaFinancieraComision(tarjForm.getComision().get(tarjForm.getRegistroSeleccionadoComisionCentroAten()));
				
				tarjForm.setGuardadoExitoso(false);
				cargarDatosParametrizadosComisionPorCentroAtencion(tarjForm);
				
				
				return mapping.findForward("comisionPorCentroAtencion");
			}
			
			else if(estado.equals("cargarCentroAtencionComi"))
			{
				if(tarjForm.getRegistroSeleccionadoCAcomi()!=ConstantesBD.codigoNuncaValido){
					
					accionCargarCentroAtencionComision(tarjForm, request);
					tarjForm.setRegistroSeleccionadoCAcomi(ConstantesBD.codigoNuncaValido);
					//accionEliminarCentroAtencionDelListado(tarjForm);
				}
				
				return mapping.findForward("seccionCentroAtencionComision");
			}
			
			else if(estado.equals("eliminarCentroAtencionComi"))
			{
				if(tarjForm.getPosComisionCentroAtencionEliminar()!=ConstantesBD.codigoNuncaValido){
					
					accionEliminarComisionCentroAtencion(tarjForm);
				}
				
				return mapping.findForward("seccionCentroAtencionComision");
			}
			
			else if(estado.equals("guardarComisionCentroAtencion"))
			{
				guardarComisionPorCentroAtencion(tarjForm, usuario);
				
				cargarDatosParametrizadosComisionPorCentroAtencion(tarjForm);
				
				return mapping.findForward("comisionPorCentroAtencion");
			
			}else if(estado.equals("ordenarReteICA")){
				
				return ordenarRegistrosReteICA (tarjForm, mapping);
			
			}else if(estado.equals("ordenarTarjetas")){
				
				return ordenarTarjetas (tarjForm, mapping);
			
			}else if(estado.equals("asignarPropiedad")){
				
				/*  * Se asigna una propiedad a la forma sin cambiar nada en la presentación  */
				return null;
			
			}else if(estado.equals("ordenarComisionCA")){
				
				return ordenarComisionCA(tarjForm, mapping);
			}
			
	    }
	    return null;
	}
	

	/**
	 * Ordenar las tarjetas financieras por una propiedad específica
	 * @param tarjForm
	 * @param mapping
	 * @return
	 */
	private ActionForward ordenarTarjetas(TarjetasFinancierasForm tarjForm,
			ActionMapping mapping)
	{
		boolean ordenamiento = true;
		
		if(tarjForm.getEsDescendente().equals(tarjForm.getPatronOrdenar()+"descendente"))
		{
			ordenamiento = false;
		}
		
		SortGenerico sortG=new SortGenerico(tarjForm.getPatronOrdenar(),ordenamiento);
		Collections.sort(tarjForm.getTarjetasFinancieras() , sortG);

		//tarjForm.setMostrarReteICA(true);
		return mapping.findForward("paginaPrincipal");
	}


	/**
	 * 
	 * @param tarjForm
	 * @param usuario 
	 */
	@SuppressWarnings("unchecked")
	private void accionDetalleTarjetaFinanciera(TarjetasFinancierasForm tarjForm, UsuarioBasico usuario) 
	{
		try{
			HibernateUtil.beginTransaction();
			tarjForm.setTarjetaFinanciera(TarjetasFinancierasMundo.cargarTarjeta(tarjForm.getTarjetasFinancieras().get(tarjForm.getTarjetaFinancieraSeleccionada()).getConsecutivo()));
		
			//cargar entidades
			cargarEntidadesFinancierasPorInstitucion(tarjForm);
			
			//cargar las comisiones ya parametrizadas.
			tarjForm.setComision(new ArrayList<TarjetaFinancieraComision>());
			
			Iterator iterador=tarjForm.getTarjetaFinanciera().getTarjetaFinancieraComisions().iterator();
			while(iterador.hasNext())
			{
				TarjetaFinancieraComision tempo=(TarjetaFinancieraComision)iterador.next();
				tarjForm.getComision().add(tempo);
			}
			accionEliminarEntidadesFinancierasDelListado(tarjForm);
			
			
			//cargar centros antencion
			tarjForm.setListadoCentrosAtencion(com.princetonsa.mundo.parametrizacion.CentroAtencion.cargarCentrosAtencion());
			
			TarjetasFinancierasDelegate delegate = new TarjetasFinancierasDelegate();
			
			List<TarjetaFinancieraReteica> tarjetasFinancieraReteica = delegate.obtenerTarjetaFinancieraReteicaOrdenada(tarjForm.getTarjetaFinanciera().getConsecutivo());
			
			tarjForm.getReteica().clear();
			
			if(tarjetasFinancieraReteica!=null){
				
				tarjForm.getReteica().addAll(tarjetasFinancieraReteica);
				tarjForm.getTarjetaFinanciera().setTarjetaFinancieraReteicas(new HashSet<TarjetaFinancieraReteica>(tarjetasFinancieraReteica));
			}
		
			accionEliminarCentroAtencionDelListado(tarjForm);
			
			tarjForm.setRegistroSeleccionado(ConstantesBD.codigoNuncaValido);
			tarjForm.setRegistroSeleccionadoCA(ConstantesBD.codigoNuncaValido);
			HibernateUtil.endTransaction();
		}
		catch (Exception e) {
			Log4JManager.error(e);
			HibernateUtil.abortTransaction();
			
		}
	}


	/**
	 * Método que se encarga de cargar las Entidades financieras por Institución
	 * 
	 * @param tarjForm
	 */
	private void cargarEntidadesFinancierasPorInstitucion(TarjetasFinancierasForm tarjForm) {
		
		if(tarjForm.getCodigoInstitucionSeleccionada()!=ConstantesBD.codigoNuncaValido){
			try{
				HibernateUtil.beginTransaction();
				tarjForm.setEntidades(EntidadesFinancieras.obtenerEntidadesPorInstitucion(tarjForm.getCodigoInstitucionSeleccionada(), true));
				HibernateUtil.endTransaction();
			}
			catch (Exception e) {
				Log4JManager.error(e);
				HibernateUtil.abortTransaction();
			}
		}
	}



	/**
	 * 
	 * @param tarjForm
	 */
	private void accionCargarCentroAtencion(TarjetasFinancierasForm tarjForm) 
	{
		TarjetaFinancieraReteica reteica= new TarjetaFinancieraReteica();
		reteica.setCentroAtencion(tarjForm.getListadoCentrosAtencion().get(tarjForm.getRegistroSeleccionadoCA()));
		reteica.setReteica(new BigDecimal(0));
		tarjForm.getReteica().add(reteica);
		tarjForm.setRegistroSeleccionadoCA(ConstantesBD.codigoNuncaValido);
	}



	/**
	 * 
	 * @param tarjForm
	 */
	private void accionEliminarEntidadesFinancierasDelListado(TarjetasFinancierasForm tarjForm) 
	{
		for(TarjetaFinancieraComision comision:tarjForm.getComision())
		{
			for(int i=0;i<tarjForm.getEntidades().size();i++)
			{
				if(tarjForm.getEntidades().get(i).getConsecutivo()==comision.getEntidadesFinancieras().getConsecutivo())
				{
					tarjForm.getEntidades().remove(i);
				}
			}
		}
	}

	
	/**
	 * 
	 * @param tarjForm
	 */
	private void accionEliminarCentroAtencionDelListado(TarjetasFinancierasForm tarjForm) 
	{
		for(TarjetaFinancieraReteica reteica:tarjForm.getReteica())
		{
			for(int i=0;i<tarjForm.getListadoCentrosAtencion().size();i++)
			{
				if(tarjForm.getListadoCentrosAtencion().get(i).getConsecutivo()==reteica.getCentroAtencion().getConsecutivo())
				{
					tarjForm.getListadoCentrosAtencion().remove(i);
				}
			}
		}
	}




	/**
	 * 
	 * @param con
	 * @param tarjForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezar( TarjetasFinancierasForm tarjForm, ActionMapping mapping, UsuarioBasico usuario) 
	{
		try{
			HibernateUtil.beginTransaction();
			tarjForm.setTarjetasFinancieras(TarjetasFinancierasMundo.listarTajetasFinancieras());
			tarjForm.setTiposTarjetaFinanciera(TarjetasFinancierasMundo.listarTiposTarjetaFinanciera());
			
			String institucionMultiempresa = ValoresPorDefecto.getInstitucionMultiempresa(usuario.getCodigoInstitucionInt());
			
			if(ConstantesBD.acronimoSi.equals(institucionMultiempresa)){
				
				tarjForm.setInstitucionMultiEmpresa(true);
				
				cargarInstituciones(tarjForm);
			
			}else{
				
				tarjForm.setCodigoInstitucionSeleccionada(usuario.getCodigoInstitucionInt());
			}
			
			definirTarjetasEliminables(tarjForm);
			
			HibernateUtil.endTransaction();
			
			tarjForm.setPatronOrdenar("Codigo");
			tarjForm.setEsDescendente("Codigo");
			
			ordenarComisionCA(tarjForm, mapping);
		}
		catch (Exception e) {
			Log4JManager.error(e);
			HibernateUtil.abortTransaction();
		}
		return mapping.findForward("paginaPrincipal");
	}
	
	
	
	/**
	 * Se consultan los movimientos de cada tarjeta para saber cual es eliminable
	 * @param tarjForm
	 */
	@SuppressWarnings("unchecked")
	private void definirTarjetasEliminables(TarjetasFinancierasForm tarjForm) 
	{
		tarjForm.setListaTarjetasEliminables(new ArrayList<DtoCheckBox>());
		for (TarjetasFinancieras tarjetasFinancieras : tarjForm.getTarjetasFinancieras()) 
		{
			DtoCheckBox checkEliminable;	checkEliminable = new DtoCheckBox();
			checkEliminable.setCodigo(tarjetasFinancieras.getConsecutivo()+"");
			
			Log4JManager.info(tarjetasFinancieras.getConsecutivo());
			
			Set<MovimientosTarjetas> setMovTar = tarjetasFinancieras.getMovimientosTarjetases();
			for (MovimientosTarjetas movTar : setMovTar) {
				if(movTar!=null){
					checkEliminable.setCheck(true); 	// SI tiene movimientos- NO es eliminable. 
					break;
				}
				else{
					checkEliminable.setCheck(false);	// NO tiene movimientos- SI es eliminable.
				}
			}
			tarjForm.getListaTarjetasEliminables().add(checkEliminable);
		}
	}
	
	
	
	/**
	 * 
	 * @param tarjForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward accionNuevo(TarjetasFinancierasForm tarjForm , ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request,HttpServletResponse response)
	{
		com.servinte.axioma.orm.TarjetasFinancieras nuevaTarjeta=new com.servinte.axioma.orm.TarjetasFinancieras();

		Instituciones institucion=new Instituciones();
		institucion.setCodigo(usuario.getCodigoInstitucionInt());
		nuevaTarjeta.setInstituciones(institucion);
		nuevaTarjeta.setTiposTarjetaFinanciera(new TiposTarjetaFinanciera());
		nuevaTarjeta.setActivo(true);
		
		tarjForm.getTarjetasFinancieras().add(nuevaTarjeta);
		
		/*
		Connection con = null;
    	con = UtilidadBD.abrirConexion();
		return redireccion(con, tarjForm, response, request, "tarjetasFinancieras.jsp");
		*/
		return UtilidadSesion.redireccionar(tarjForm.getLinkSiguiente(),ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()),tarjForm.getTarjetasFinancieras().size(), response, request, "tarjetasFinancieras.jsp",true);
	}
	
	
	
	
	/**
     * Metodo implementado para posicionarse en la ultima
     * pagina del pager.
     * @param con, Connection con la fuente de datos.
     * @param poolesForm ConceptosCarteraForm
     * @param response HttpServletResponse
     * @param request HttpServletRequest
     * @param String enlace
     * @return null
     */
    public ActionForward redireccion (	Connection con, TarjetasFinancierasForm forma, HttpServletResponse response, HttpServletRequest request, String enlace)
    {
    	forma.setOffset(((int)((forma.getNumRegistros()-1)/forma.getMaxPageItems()))*forma.getMaxPageItems());
        if(request.getParameter("ultimaPage")==null)
        {
           if(forma.getNumRegistros() > (forma.getOffset()+forma.getMaxPageItems()))
               forma.setOffset(((int)(forma.getNumRegistros()/forma.getMaxPageItems()))*forma.getMaxPageItems());
            try 
            {
                response.sendRedirect(enlace+"?pager.offset="+forma.getOffset());
            } catch (IOException e) 
            {
                
                e.printStackTrace();
            }
        }
        else
        {    
            String ultimaPagina=request.getParameter("ultimaPage");
            String tempOffset="offset=";
            int posOffSet=ultimaPagina.indexOf(tempOffset)+tempOffset.length();
            if(forma.getNumRegistros()>(forma.getOffset()+forma.getMaxPageItems()))
                forma.setOffset(forma.getOffset()+forma.getMaxPageItems());
            try 
            {
                response.sendRedirect(ultimaPagina.substring(0,posOffSet)+forma.getOffset());
            } 
            catch (IOException e) 
            {
                
                e.printStackTrace();
            }
        }
       try {
		UtilidadBD.cerrarConexion(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
       return null;
    }
    
    
    
	
	
	
	/**
	 * Método que se encarga de cargar las instituciones parametrizadas en el sistema.
	 * @param tarjForm
	 */
	private void cargarInstituciones (TarjetasFinancierasForm tarjForm){
		
		LocalizacionServicio localizacionServicio = new LocalizacionServicio();
		tarjForm.setInstituciones(localizacionServicio.listarInstituciones());
	}
	
	
	
	/**
	 * Carga los datos ya parametrizados para la Tarjeta Financiera Comisión
	 * @param tarjForm
	 */
	private void cargarDatosParametrizadosComisionPorCentroAtencion(TarjetasFinancierasForm tarjForm) 
	{
		try{
			HibernateUtil.beginTransaction();
			tarjForm.setListaComisionXCentroAtencion(new ArrayList<ComisionXCentroAtencion>());
			tarjForm.setListaComisionXCentroAtencionEliminar(new ArrayList<ComisionXCentroAtencion>());
			
			ArrayList<ComisionXCentroAtencion> listaDatosParametrizadosComisionPorCentroAtencion = new ArrayList<ComisionXCentroAtencion>();
			listaDatosParametrizadosComisionPorCentroAtencion = new ComisionXCentroAtencionDelegate().listarPorTarjetaFinancieraComision(tarjForm.getTarjetaFinancieraComision().getCodigoPk());
			
			if(!Utilidades.isEmpty(listaDatosParametrizadosComisionPorCentroAtencion)){
				tarjForm.getListaComisionXCentroAtencion().addAll(listaDatosParametrizadosComisionPorCentroAtencion);
			}
			HibernateUtil.endTransaction();
		}
		catch (Exception e) {
			Log4JManager.error(e);
			HibernateUtil.abortTransaction();
		}
		
	}
	
	
	/**
	 * 
	 * @param tarjForm
	 */
	private void accionCargarEntidades(TarjetasFinancierasForm tarjForm) 
	{
		try{
			HibernateUtil.beginTransaction();
			TarjetaFinancieraComision comision;
			comision = new TarjetaFinancieraComision();
			comision.setEntidadesFinancieras(EntidadesFinancieras.cargarEntidadFinanciera(tarjForm.getEntidades().get(tarjForm.getRegistroSeleccionado()).getConsecutivo()));
			
			boolean valido =  true;
			for (TarjetaFinancieraComision tarjFinanComi : tarjForm.getComision()) 
			{
				if(tarjFinanComi.getEntidadesFinancieras().getCodigo().equals(comision.getEntidadesFinancieras().getCodigo())){
					valido  = false;
					break;
				}
			}
			
			if(valido)
			{
				comision.setComision(new BigDecimal(0));
				tarjForm.getComision().add(comision);
			}
			HibernateUtil.endTransaction();
		}
		catch (Exception e) {
			Log4JManager.error(e);
			HibernateUtil.abortTransaction();
		}
	}

	
	/**
	 * Carga la parametrica de Comisión por Centro de Atención
	 * @param tarjForm
	 * @param request
	 */
	private void accionCargarCentroAtencionComision(TarjetasFinancierasForm tarjForm, HttpServletRequest request) 
	{
		ComisionXCentroAtencion comisionXCentroAtencion;
		comisionXCentroAtencion = new ComisionXCentroAtencion();
		comisionXCentroAtencion.setCentroAtencion(tarjForm.getListadoCentrosAtencion().get(tarjForm.getRegistroSeleccionadoCAcomi()));
		
		boolean valido =  true;
		for (ComisionXCentroAtencion comiXCAten : tarjForm.getListaComisionXCentroAtencion()) 
		{
			if(comiXCAten.getCentroAtencion().getConsecutivo() == comisionXCentroAtencion.getCentroAtencion().getConsecutivo()){
				valido  = false;
				break;
			}
		}
		
		if(valido)
		{
			TarjetaFinancieraComision tarjetaFinancieraComision;
			tarjetaFinancieraComision = new TarjetaFinancieraComision();
			tarjetaFinancieraComision = tarjForm.getTarjetaFinancieraComision();
			
			comisionXCentroAtencion.setTarjetaFinancieraComision(tarjetaFinancieraComision);
			tarjForm.getListaComisionXCentroAtencion().add(comisionXCentroAtencion);
		}
		
	}
	
	
	
	
	/**
	 * Elimina registros la parametrica de Comisión por Centro de Atención
	 * @param tarjForm
	 */
	private void accionEliminarComisionCentroAtencion(TarjetasFinancierasForm tarjForm) 
	{
		ComisionXCentroAtencion comisionXCentroAtencion;
		comisionXCentroAtencion = new ComisionXCentroAtencion();
		comisionXCentroAtencion = tarjForm.getListaComisionXCentroAtencion().get(tarjForm.getPosComisionCentroAtencionEliminar());
		
		if(comisionXCentroAtencion.getCodigoPk() >0){
			tarjForm.getListaComisionXCentroAtencionEliminar().add(comisionXCentroAtencion);
		}
		
		tarjForm.getListaComisionXCentroAtencion().remove(tarjForm.getPosComisionCentroAtencionEliminar());
	}
	
	
	
	/**
	 * Guarda la parametrica de Comisión por Centro de Atención
	 * @param tarjForm
	 * @param usuario
	 */
	private void guardarComisionPorCentroAtencion(TarjetasFinancierasForm tarjForm, UsuarioBasico usuario) 
	{
		try{
			UtilidadTransaccion.getTransaccion().begin();
			
			IUsuariosServicio usuarioServicio = AdministracionFabricaServicio.crearUsuariosServicio();
			boolean save = true;
			
			for (ComisionXCentroAtencion comisionXCentroAtencion : tarjForm.getListaComisionXCentroAtencion()) 
			{
				try 
				{
					comisionXCentroAtencion.setUsuarios(usuarioServicio.buscarPorLogin(usuario.getLoginUsuario()));
					comisionXCentroAtencion.setFecha(UtilidadFecha.getFechaActualTipoBD());
					comisionXCentroAtencion.setHora(UtilidadFecha.getHoraActual());
					
					if(comisionXCentroAtencion.getComision() == null){
						comisionXCentroAtencion.setComision(new BigDecimal(0));
					}
					
					new ComisionXCentroAtencionHome().attachDirty(comisionXCentroAtencion);
					guadrarLogComisionPorCentroAtencion(comisionXCentroAtencion, ConstantesIntegridadDominio.acronimoAccionHistoricaInsertar, usuario.getLoginUsuario());
					
				} catch (Exception e) {
					Log4JManager.error("No se pudo guardar ComisionXCentroAtencion: ",e);
					save = false;
				}
			}
			
			if(save){
				UtilidadTransaccion.getTransaccion().commit();
				eliminarComisionPorCentroAtencion(tarjForm, usuario.getLoginUsuario());
				tarjForm.setGuardadoExitoso(true);
			}
			else{
				UtilidadTransaccion.getTransaccion().rollback();
				tarjForm.setGuardadoExitoso(false);
			}
		}
		catch (Exception e) {
			Log4JManager.error(e);
			HibernateUtil.abortTransaction();
		}
	}

	
	
	/**
	 * Elimina registros de la parametrica de Comisión por Centro de Atención
	 * @param tarjForm
	 */
	private void eliminarComisionPorCentroAtencion(TarjetasFinancierasForm tarjForm, String loginUsuario)
	{
		UtilidadTransaccion.getTransaccion().begin();
		boolean delete = true;
		try 
		{
			for (ComisionXCentroAtencion comisionXCentroAtencionEliminar : tarjForm.getListaComisionXCentroAtencionEliminar()) 
			{
				guadrarLogComisionPorCentroAtencion(comisionXCentroAtencionEliminar, ConstantesIntegridadDominio.acronimoAccionHistoricaEliminar, loginUsuario);
				new ComisionXCentroAtencionHome().delete(comisionXCentroAtencionEliminar);
			}
			
		} catch (Exception e) {
			Log4JManager.error("No se pudo eliminar ComisionXCentroAtencion: ",e);
			delete = false;
		}
		
		if(delete){
			UtilidadTransaccion.getTransaccion().commit();
			tarjForm.setGuardadoExitoso(true);
		}else{
			UtilidadTransaccion.getTransaccion().rollback();
			tarjForm.setGuardadoExitoso(false);
		}
	}
	
	
	/**
	 * Este m&eacute;todo se encarga de ordenar las columnas de el resultado 
	 * de la b&uacute;squeda sig&aacute;n los par&aacute;metros ingresados
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return ActionForward
	 */
	public ActionForward ordenarRegistrosReteICA(TarjetasFinancierasForm tarjForm, ActionMapping mapping){
		
		boolean ordenamiento = false;
		
		if(tarjForm.getEsDescendente().equals(tarjForm.getPatronOrdenar()+"descendente"))
		{
			ordenamiento = true;
		}
		
		SortGenerico sortG=new SortGenerico(tarjForm.getPatronOrdenar(),ordenamiento);
		Collections.sort(tarjForm.getReteica() , sortG);

		tarjForm.setMostrarReteICA(true);
		return mapping.findForward("seccionReteICA");
	}
	
	
	
	/**
	 * Este m&eacute;todo se encarga de ordenar las columnas de el resultado 
	 * de la b&uacute;squeda sig&aacute;n los par&aacute;metros ingresados
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return ActionForward
	 */
	public ActionForward ordenarComisionCA(TarjetasFinancierasForm tarjForm, ActionMapping mapping){
		
		boolean ordenamiento = false;
		
		if(tarjForm.getEsDescendente().equals(tarjForm.getPatronOrdenar()+"descendente"))
		{
			ordenamiento = true;
		}
		
		SortGenerico sortG=new SortGenerico(tarjForm.getPatronOrdenar(),ordenamiento);
		Collections.sort(tarjForm.getListaComisionXCentroAtencion() , sortG);

		return mapping.findForward("seccionCentroAtencionComision");
	}
	
	
	
	
	/**
	 * Guarda el log de la funcionalidad.
	 * Este metodo utiliza la misma transaccion del proceso que lo llama.
	 * @param comisionXCentroAtencion
	 * @author Cristhian Murillo
	 */
	public static void guadrarLogComisionPorCentroAtencion(ComisionXCentroAtencion comisionXCentroAtencion, String accionRealizada, String loginUsuario)
	{
		try {
				HistComisionXCentroAten histComisionXCentroAten;
				histComisionXCentroAten = new HistComisionXCentroAten();
				
				histComisionXCentroAten.setAccionRealizada(accionRealizada);
				histComisionXCentroAten.setCentroAtencion(comisionXCentroAtencion.getCentroAtencion().getConsecutivo());
				histComisionXCentroAten.setComision(comisionXCentroAtencion.getComision()); 
				histComisionXCentroAten.setFechaModifica(UtilidadFecha.getFechaActualTipoBD());
				histComisionXCentroAten.setHoraModifica(UtilidadFecha.getHoraActual());
				histComisionXCentroAten.setTarjetaFinancieraComision(comisionXCentroAtencion.getTarjetaFinancieraComision().getCodigoPk());
				histComisionXCentroAten.setUsuarioModifica(loginUsuario);
				
				logTarjetasFinancieraServicio.guardarHistComisionXCentroAten(histComisionXCentroAten);
			
		} catch (Exception e) 
		{
			Log4JManager.info("No se pudo guardar el Log de la funcionalidad Tarjetas Financieras Reteica");
		}
	}
	
	
	
	/**
	 * Muestra el mensaje de error enviado
	 * @param forma
	 * @param request
	 * @autor Cristhian Murillo
	 */
	@SuppressWarnings("unused")
	private void mostrarErrorEnviado(TarjetasFinancierasForm forma, HttpServletRequest request, String mensajeConcreto) 
	{
		ActionErrors errores = new ActionErrors();
		errores.add("error_concreto_enviado", new ActionMessage("errors.notEspecific", mensajeConcreto));
		saveErrors(request, errores);
		forma.setEstado("empezar");
	}
	

	/*
	private void accionOrdenarMapa(TarjetasFinancierasForm tarjForm)
	{
		String[] indices=indicesMapa.split(",");
		int numReg=Integer.parseInt(tarjForm.getTarjetasFina("numRegistros")+"");
		tarjForm.setTarjetasFina(Listado.ordenarMapa(indices,tarjForm.getPatronOrdenar(),tarjForm.getUltimoPatron(),tarjForm.getTarjetasFina(),numReg));
		tarjForm.setUltimoPatron(tarjForm.getPatronOrdenar());
		tarjForm.setTarjetasFina("numRegistros",numReg+"");
	}	
	
	private void accionGuardarRegistros(Connection con, TarjetasFinancierasForm tarjForm, TarjetasFinancieras tarjMundo, UsuarioBasico usuario)
	{
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		
		//eliminar
		for(int i=0;i<Integer.parseInt(tarjForm.getTarjetasFinaEliminado("numRegistros")+"");i++)
		{
			if(tarjForm.getTarjetasFinaEliminado("consecutivo_"+i)!=null)
			{
				if(tarjMundo.eliminarTarjeta(con,tarjForm.getTarjetasFinaEliminado("consecutivo_"+i)+""))
				{
					this.generarLog(tarjForm, tarjMundo, usuario, true, i);
					transaccion=true;
				}
			}
		}
		
		for(int i=0;i<Integer.parseInt(tarjForm.getTarjetasFina("numRegistros")+"");i++)
		{
			//modificar
			if((tarjForm.getTarjetasFina("tiporegistro_"+i)+"").trim().equalsIgnoreCase("BD") && this.existeModificacion(con,tarjForm,tarjMundo,tarjForm.getTarjetasFina("consecutivo_"+i)+"",i,usuario))
			{
				HashMap vo = new HashMap();
				vo.put("codigo_entidad",tarjForm.getTarjetasFina("codigo_entidad_"+i));
				vo.put("descripcion_tarjeta",tarjForm.getTarjetasFina("descripcion_tarjeta_"+i));
				vo.put("tipo_tarjeta_financiera",tarjForm.getTarjetasFina("tipo_tarjeta_financiera_"+i));
				vo.put("base_rete",tarjForm.getTarjetasFina("base_rete_"+i));
				vo.put("retefte",tarjForm.getTarjetasFina("retefte_"+i));
				vo.put("reteica",tarjForm.getTarjetasFina("reteica_"+i));
				vo.put("comision",tarjForm.getTarjetasFina("comision_"+i));
				vo.put("directo_banco",tarjForm.getTarjetasFina("directo_banco_"+i));
				vo.put("activo",tarjForm.getTarjetasFina("activo_"+i));
				vo.put("consecutivo",tarjForm.getTarjetasFina("consecutivo_"+i));
				transaccion=tarjMundo.modificarTarjeta(con, vo);
			}
			//insertar
			else if((tarjForm.getTarjetasFina("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				HashMap vo = new HashMap();
				vo.put("codigo_tarjeta",tarjForm.getTarjetasFina("codigo_tarjeta_"+i));
				vo.put("codigo_entidad",tarjForm.getTarjetasFina("codigo_entidad_"+i));
				vo.put("descripcion_tarjeta",tarjForm.getTarjetasFina("descripcion_tarjeta_"+i));
				vo.put("tipo_tarjeta_financiera",tarjForm.getTarjetasFina("tipo_tarjeta_financiera_"+i));
				vo.put("base_rete",tarjForm.getTarjetasFina("base_rete_"+i));
				vo.put("retefte",tarjForm.getTarjetasFina("retefte_"+i));
				vo.put("reteica",tarjForm.getTarjetasFina("reteica_"+i));
				vo.put("comision",tarjForm.getTarjetasFina("comision_"+i));
				vo.put("directo_banco",tarjForm.getTarjetasFina("directo_banco_"+i));
				vo.put("activo",tarjForm.getTarjetasFina("activo_"+i));
		 	    vo.put("institucion", usuario.getCodigoInstitucion());
				transaccion = tarjMundo.insertarTarjeta(con, vo);
			}
		}
		
		if(transaccion)
		{
			tarjForm.setMensaje(new ResultadoBoolean(true, "OPERACIÓN REALIZADA CON ÉXITO"));
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			tarjForm.setMensaje(new ResultadoBoolean(true, "OPERACIÓN NO EXITOSA. POR FAVOR VERIFICAR"));
			UtilidadBD.abortarTransaccion(con);
		}
	}
	
	private void generarLog(TarjetasFinancierasForm tarjForm, TarjetasFinancieras tarjMundo, UsuarioBasico usuario, boolean isEliminacion, int pos) 
	{
		String log = "";
		int tipoLog=0;
		String[] indices=indicesMapa.split(",");
		if(isEliminacion)
		{
			log = 		 "\n   ============REGISTRO ELIMINADO=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ "+tarjForm.getTarjetasFinaEliminado(indices[i]+""+pos)+" ]";
			}
			log+= "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogEliminacion;
		}
		else
		{
			log = 		 "\n   ============INFORMACION ORIGINAL=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ "+tarjMundo.getMapaTarjetasFinancierasTemp().get(indices[i]+"0")+" ]";
			}
			log += 		 "\n   ============INFORMACION MODIFICADA=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ "+tarjForm.getTarjetasFina(indices[i]+""+pos)+" ]";
			}
			log+= "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogModificacion;
		}
		LogsAxioma.enviarLog(ConstantesBD.logTarjetasFinancierasCodigo,log,tipoLog,usuario.getLoginUsuario());		
	}	
	
	private boolean existeModificacion(Connection con, TarjetasFinancierasForm tarjForm, TarjetasFinancieras tarjMundo,String consecutivo,int pos, UsuarioBasico usuario)
	{
		HashMap temp=tarjMundo.consultarTarjetaEspecifico(con, consecutivo);
		String[] indices=indicesMapa.split(",");
		for(int i=0;i<indices.length;i++)
		{
			if( !(temp.get(indices[i]+"0").toString().equals(tarjForm.getTarjetasFina(indices[i]+""+pos)+"")))
			{
				tarjMundo.setMapaTarjetasFinancierasTemp(temp);
				this.generarLog(tarjForm, tarjMundo, usuario, false, pos);
				return true;
			}
		}
		return false;
	}	
	
	private ActionForward accionEliminarRegistro(TarjetasFinancierasForm tarjForm, HttpServletRequest request, HttpServletResponse response, ActionMapping mapping)
	{
		int numRegMapEliminados=Integer.parseInt(tarjForm.getTarjetasFinaEliminado("numRegistros")+"");
		int ultimaPosMapa=(Integer.parseInt(tarjForm.getTarjetasFina("numRegistros")+"")-1);
		//poner la informacion en el otro mapa.
		String[] indices=indicesMapa.split(",");
		for(int i=0;i<indices.length;i++)
		{
			//solo pasar al mapa los registros que son de BD
			if((tarjForm.getTarjetasFina("tiporegistro_"+tarjForm.getPosEliminar())+"").trim().equalsIgnoreCase("BD"))
			{
				tarjForm.setTarjetasFinaEliminado(indices[i]+""+numRegMapEliminados, tarjForm.getTarjetasFina(indices[i]+""+tarjForm.getPosEliminar()));
			}
		}
		
		//acomodar los registros del mapa en su nueva posicion
		for(int i=tarjForm.getPosEliminar();i<ultimaPosMapa;i++)
		{
			for(int j=0;j<indices.length;j++)
			{
				tarjForm.setTarjetasFina(indices[j]+""+i,tarjForm.getTarjetasFina(indices[j]+""+(i+1)));
			}
		}
		
		//ahora eliminamos el ultimo registro del mapa.
		for(int j=0;j<indices.length;j++)
		{
			tarjForm.getTarjetasFina().remove(indices[j]+""+ultimaPosMapa);
		}
		
		//ahora actualizamo el numero de registros en el mapa.
		tarjForm.setTarjetasFina("numRegistros",ultimaPosMapa);
		tarjForm.setTarjetasFinaEliminado("numRegistros", (numRegMapEliminados+1));
		//logger.info("tarjForm.getLinkSiguiente() "+tarjForm.getLinkSiguiente());
		return mapping.findForward("paginaPrincipal");
		//return UtilidadSesion.redireccionar(tarjForm.getLinkSiguiente(),tarjForm.getMaxPageItems(),Integer.parseInt(tarjForm.getTarjetasFina("numRegistros").toString()), response, request, "tarjetasFinancieras.jsp",tarjForm.getPosEliminar()==ultimaPosMapa);

		
	}	
	
	private void accionNuevaTarjeta(TarjetasFinancierasForm tarjForm)
	{
		int pos = Integer.parseInt(tarjForm.getTarjetasFina("numRegistros")+"");
		tarjForm.setTarjetasFina("consecutivo_"+pos,"");
		tarjForm.setTarjetasFina("codigo_tarjeta_"+pos,"");
		tarjForm.setTarjetasFina("codigo_entidad_"+pos,"0");
		tarjForm.setTarjetasFina("activo_entidad_"+pos, ValoresPorDefecto.getValorTrueParaConsultas());
		tarjForm.setTarjetasFina("descripcion_tarjeta_"+pos,"");
		tarjForm.setTarjetasFina("tipo_tarjeta_financiera_"+pos,"true");
		tarjForm.setTarjetasFina("base_rete_"+pos,"");
		tarjForm.setTarjetasFina("retefte_", (pos+1)+"");
		tarjForm.setTarjetasFina("reteica_"+pos,"");
		tarjForm.setTarjetasFina("comision_"+pos,"");
		tarjForm.setTarjetasFina("directo_banco_"+pos,"");
		tarjForm.setTarjetasFina("activo_"+pos,"true");
		tarjForm.setTarjetasFina("tiporegistro_"+pos,"MEM");
		tarjForm.setTarjetasFina("numRegistros", (pos+1)+"");
	}	
	
	private void accionConsultarTarjeta(Connection con, TarjetasFinancierasForm tarjForm,  TarjetasFinancieras tarjMundo, UsuarioBasico usuario)
	{
		HashMap vo = new HashMap();
		vo.put("institucion", usuario.getCodigoInstitucion());
		tarjForm.setTarjetasFina(tarjMundo.consultarTarjeta(con, vo));
	}	

	/*
	public Connection openDBConnection(Connection con)
	{

		if(con != null)
		return con;
					
		try{
			String tipoBD = System.getProperty("TIPOBD");
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			con = myFactory.getConnection();
			}
			catch(Exception e)
			{
			    logger.warn(e+"Problemas con la base de datos al abrir la conexion"+e.toString());
				return null;
			}
					
			return con;
	}
	*/
	
	/**
	 * Método en que se cierra la conexión (Buen manejo
	 * recursos), usado ante todo al momento de hacer un forward
	 * @param con Conexión con la fuente de datos
	 */
	/*
	public void cerrarConexion (Connection con)
	{
	    try{
	        if (con!=null&&!con.isClosed())
	        {
	        	UtilidadBD.closeConnection(con);
	        }
	    }
	    catch(Exception e){
	        logger.error(e+"Error al tratar de cerrar la conexion con la fuente de datos. \n Excepcion: " +e.toString());
	    }
	}
	*/
	
	/*
	private UsuarioBasico getUsuarioBasicoSesion(HttpSession session)
	{
	    UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
			if(usuario == null)
			    logger.warn("El usuario no esta cargado (null)");
			
			return usuario;
	}
	
	
		
	
	
	
	
	private ActionForward accionOrdenar(Connection con, TarjetasFinancierasForm tarjForm, ActionMapping mapping)
	{
		this.accionOrdenarMapa(tarjForm);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaPrincipal");			
	}
	
	private ActionForward accionEliminar(Connection con, TarjetasFinancierasForm tarjForm, ActionMapping mapping, HttpServletRequest request,HttpServletResponse response)
	{
		UtilidadBD.closeConnection(con);
		return this.accionEliminarRegistro(tarjForm,request,response,mapping);
		
	}
	
	private ActionForward accionGuardar(Connection con, TarjetasFinancierasForm tarjForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request)
	{
		TarjetasFinancieras tarjMundo=new TarjetasFinancieras();
		ActionErrors errores = new ActionErrors();
		
		//guardamos en BD.
		this.accionGuardarRegistros(con,tarjForm,tarjMundo,usuario);

		//limipiamos el form
		tarjForm.reset();
		tarjForm.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
		//Se quito esta linea ya que la consulta se ejecuta cuando se llame el metodo accionEmpezar
		//this.accionConsultarTarjeta(con,tarjForm,tarjMundo,usuario);
		
		if(!errores.isEmpty())
		{
			saveErrors(request,errores);
			tarjForm.setEstado("empezar");
			UtilidadBD.closeConnection(con);
			return mapping.findForward("paginaPrincipal");
		}
		
		return accionEmpezar(con,tarjForm,mapping,usuario);
		
	}
	*/
	
}