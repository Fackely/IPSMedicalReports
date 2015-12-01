/*
 * Marzo 30, 2010
 */
package com.princetonsa.action.tesoreria;

import java.util.ArrayList;
import java.util.Collections;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadTexto;
import com.princetonsa.actionform.tesoreria.TransportadoraValoresForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.sort.odontologia.SortGenerico;
import com.princetonsa.sort.odontologia.SortTarjetaCliente;
import com.princetonsa.sort.tesoreria.SortTransportadora;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.mundo.impl.tesoreria.TransportadoraValoresMundo;
import com.servinte.axioma.orm.CentroAtencion;
import com.servinte.axioma.orm.Instituciones;
import com.servinte.axioma.orm.Terceros;
import com.servinte.axioma.orm.TransportadoraValores;

import com.servinte.axioma.orm.delegate.administracion.CentroAtencionDelegate;
import com.servinte.axioma.orm.delegate.administracion.InstitucionDelegate;
import com.servinte.axioma.servicio.fabrica.TesoreriaFabricaServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ITransportadoraValoresServicio;




/**
 * @author Cristhian Murillo. Modifica: Edgar Carvajal
 *
 * Clase usada para controlar los procesos de la funcionalidad .
 * 
 */
public class TransportadoraValoresAction extends Action 
{
	
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, 
	 * HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
								HttpServletResponse response )throws Exception
	{

		if(form instanceof TransportadoraValoresForm)
		{
			/* OBJETOS A USAR */
			/**
			 * Formulario
			 */
			TransportadoraValoresForm forma = (TransportadoraValoresForm)form;
			
			/** 
			 * Usuario
			 */
			UsuarioBasico usuario 			= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			//PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
			
			String estado = forma.getEstado(); 
			
			//JOptionPane.showMessageDialog(null, "ESTADO: "+estado);
			
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
			else if(estado.equals("empezarTransportadora"))
			{
				return accionEmpezarModificar(mapping, forma, usuario);
			}
			// nuevo
			else if(estado.equals("nuevo"))
			{
				return accionNuevo(mapping, forma, usuario);
			}
			
			
			// guardar
			else if(estado.equals("guardar"))
			{
				return accionGuardar(mapping, forma, usuario, request);
			}
			
			// ordenar
			else if(estado.equals("ordenar"))
			{
				return accionOrdenar(mapping, forma, usuario);
			}
			
			// eliminar
			else if(estado.equals("eliminar"))
			{
				return accionEliminar(mapping, forma, usuario);
			}
			
			// modificar
			else if(estado.equals("modificar"))
			{
				return accionModificar(mapping,forma, usuario, request);
			}
			
			// guardarmodificar
			else if(estado.equals("guardarmodificar"))
			{
				return accionGuardarModificar(mapping, forma, usuario, request);
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
	 * ACCION EMPEZAR
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezar(ActionMapping mapping, TransportadoraValoresForm forma, UsuarioBasico usuario)
	{
		forma.reset();
		mostrarLista(forma, usuario);
		Log4JManager.info(forma.getDto().getTerceros().getCodigo());
		
		return mapping.findForward("principal");
	}
	
	
	/**
	 * ACCION EMPEZAR
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezarModificar(ActionMapping mapping, TransportadoraValoresForm forma, UsuarioBasico usuario)
	{
		forma.reset();
		forma.setEsConsulta(ConstantesBD.acronimoSi);
		mostrarLista(forma, usuario);
		return mapping.findForward("principal");
	}
	
	
	/**
	 * ACCION NUEVA TRANSPORTADORA
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionNuevo(ActionMapping mapping, TransportadoraValoresForm forma, UsuarioBasico usuario)
	{
		forma.reset();
		forma.setEstado("nuevo");
		forma.setDto(new TransportadoraValores());
		forma.getDto().setTerceros(new Terceros());
		forma.getDto().setActivo(ConstantesBD.acronimoSi.charAt(0));
		
		
		mostrarLista(forma, usuario);
		cargarCentrosAtencion(forma, usuario);
		
		return mapping.findForward("principal");
	}
	
	
	
	/**
	 * ACCION GUARDAR TRANSPORTADORA
	 * @param mapping
	 * @param forma
	 * @param request 
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardar(ActionMapping mapping,	TransportadoraValoresForm forma, UsuarioBasico usuario, HttpServletRequest request)
	{	
		ITransportadoraValoresServicio transportadoraValoresServicio = getTransportadoraValoresServicio();
		
		transportadoraValoresServicio.guardar(forma.getDto(), usuario ,  UtilidadTexto.convertirColeccionLista(forma.getListaCodigosCentro()) , forma.getListaCentro() );
		forma.reset();
		forma.setEstado("empezar");
		mostrarLista(forma, usuario);		
		return mapping.findForward("principal");
	}
	
	
	

	/**
	 * ACCION ORDENAMIENTO GENERICO
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionOrdenar(ActionMapping mapping,	TransportadoraValoresForm forma, UsuarioBasico usuario) 
	{
		Collections.sort(forma.getListaDto(), new  SortTransportadora(forma.getPatronOrdenar()));
		return mapping.findForward("principal");
	}
	
	
	
	
	/**
	 * ACCION ELIMINAR 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEliminar(ActionMapping mapping, TransportadoraValoresForm forma, UsuarioBasico usuario) 
	{
		ITransportadoraValoresServicio transportadoraValoresServicio = getTransportadoraValoresServicio();
		
		forma.setDto(forma.getListaDto().get(forma.getPosArray()));
		transportadoraValoresServicio.guardarLog(transportadoraValoresServicio.armarArchivo(forma.getDto(),TransportadoraValoresMundo.TITULO_ELIMINAR, null, null), usuario, ConstantesBD.logEmisionBonosDescuentosOdontologicos );
		transportadoraValoresServicio.eliminar(forma.getDto());
		forma.reset();
		mostrarLista(forma, usuario);		
		forma.setEstado("resumen");
		return accionEmpezar(mapping,forma, usuario);
	}
	
	
	

	/**
	 * ACCION CARGAR EN PRESENTACION ACCION DE LA LISTA
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param request 
	 * @return
	 */
	private ActionForward accionModificar(ActionMapping mapping, TransportadoraValoresForm forma, UsuarioBasico usuario, HttpServletRequest request) 
	{
		try{
			HibernateUtil.beginTransaction();
			ITransportadoraValoresServicio transportadoraValoresServicio = getTransportadoraValoresServicio();
			forma.setDto(forma.getListaDto().get(forma.getPosArray())); // TODO FALTA EL CLONADOR
			forma.setArchivoPlano(transportadoraValoresServicio.armarArchivo(forma.getDto(),TransportadoraValoresMundo.TITULO_MODIFICACION, null , null)); // ARMANDO EL ARCHIVO PLANO
			forma.setDescripcionTercero(forma.getDto().getTerceros().getDescripcion());//MANEJO DE PRESENTACION
			cargarCentrosAtencion( forma,  usuario); // CARGAR CENTROS ATENCION
			forma.setListaCodigosCentro(transportadoraValoresServicio.cargarCodigoCentro(forma.getDto())); // MANEJO DE PRESENTACION
			/**
			 * TODO AYUDANTE CUANDO EL PROCESO ES CONSULTA
			 */
			forma.setListaCentroAtenAyudanteTransportadora(transportadoraValoresServicio.filtrarListaCentros(forma.getListaCentro(), UtilidadTexto.convertirColeccionLista(forma.getListaCodigosCentro())));
			forma.setListaCodigosEliminar(transportadoraValoresServicio.cargarCodigosPkCentroTransportadora(forma.getDto())); // MANEJO DE DATOS
			HibernateUtil.endTransaction();
		}
		catch (Exception e) {
			Log4JManager.error("ERROR accionModificar",e);
			HibernateUtil.abortTransaction();
		}
		return mapping.findForward("principal");
	}
	
	
	
	
	
	/**
	 * ACCION MODIFICAR LA TRANSPORTADORA DE VALORES
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param request 
	 * @return
	 */
	private ActionForward accionGuardarModificar(ActionMapping mapping, TransportadoraValoresForm forma, UsuarioBasico usuario, HttpServletRequest request) 
	{
		ITransportadoraValoresServicio transportadoraValoresServicio = getTransportadoraValoresServicio();
		
		ArrayList<String> listaTmpCodigosGuardar= UtilidadTexto.convertirColeccionLista(forma.getListaCodigosCentro());
		StringBuilder tmp=forma.getArchivoPlano();
		forma.getDto().getTerceros().setDescripcion(forma.getDescripcionTercero());
		tmp.append(transportadoraValoresServicio.armarArchivo(forma.getDto(), TransportadoraValoresMundo.TITULO_ACTUAL,forma.getListaCentro(), listaTmpCodigosGuardar));
		
		transportadoraValoresServicio.modificar(forma.getDto(), usuario, 
											forma.getListaCodigosEliminar(), listaTmpCodigosGuardar, 
											forma.getListaCentro()); // MODIFICAR TRANSPORTADORA
		
		transportadoraValoresServicio.guardarLog(tmp, usuario, ConstantesBD.tipoRegistroLogModificacion); // GUARDAR EL ARCHIVO PLANO
		 
		forma.reset();
		forma.setEstado("empezar");
		this.mostrarLista(forma, usuario);
		return mapping.findForward("principal");
	}
	
	
	
	/** 
	 * LISTA LAS TRANPORTADORAS DE VALORES
	 * @param usuario
	 * @param forma
	 */
	private void mostrarLista(TransportadoraValoresForm forma, UsuarioBasico usuario) 
	{
		ITransportadoraValoresServicio transportadoraValoresServicio = getTransportadoraValoresServicio();
		TransportadoraValores dto = new TransportadoraValores();
		forma.setListaDto((ArrayList<TransportadoraValores>) transportadoraValoresServicio.consultar(dto, usuario.getCodigoInstitucionInt()));
	}
	
	
	
	
	/**
	 * CARGA TODOs LOS CENTROS DE ATENCION DE LA INSTITUCION QUE ESTEN ACTIVOS
	 */
	private void cargarCentrosAtencion(TransportadoraValoresForm forma, UsuarioBasico usuario){
		
		//TODO CAMBIAR DE LUGAR
		// PREGUNTA SE SE PUEDE GENERA UN MUNDO 
		HibernateUtil.beginTransaction();
			InstitucionDelegate objDelegate = new InstitucionDelegate();
			Instituciones objInstitucion = objDelegate.findById(usuario.getCodigoInstitucionInt());
		HibernateUtil.endTransaction();
		
	
		CentroAtencionDelegate objCentroDelegate= new CentroAtencionDelegate();
		forma.setListaCentro((ArrayList<CentroAtencion>)objCentroDelegate.listarCentrosInstitucion(objInstitucion));
	}
	
	
	
	private ITransportadoraValoresServicio getTransportadoraValoresServicio(){
		
		return TesoreriaFabricaServicio.crearTransportadoraValoresServicio();
	}
	
	
}	
	
	