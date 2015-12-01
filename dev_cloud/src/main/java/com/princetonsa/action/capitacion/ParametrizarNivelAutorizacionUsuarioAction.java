package com.princetonsa.action.capitacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.actionform.capitacion.ParametrizarNivelAutorizacionUsuarioForm;
import com.princetonsa.dto.capitacion.DTOBusquedaNivelAutorizacionUsuario;
import com.princetonsa.dto.capitacion.DTONivelAutorizacion;
import com.princetonsa.dto.capitacion.DTONivelAutorizacionOcupacionMedica;
import com.princetonsa.dto.capitacion.DTONivelAutorizacionUsuarioEspecifico;
import com.princetonsa.dto.capitacion.DTOPrioridadOcupacionMedica;
import com.princetonsa.dto.capitacion.DTOPrioridadUsuarioEspecifico;
import com.princetonsa.dto.comun.DtoCheckBox;
import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.CentrosCostoEntidadesSubcontratadas;
import com.servinte.axioma.orm.OcupacionesMedicas;
import com.servinte.axioma.orm.Usuarios;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.administracion.AdministracionFabricaServicio;
import com.servinte.axioma.servicio.fabrica.capitacion.CapitacionFabricaServicio;
import com.servinte.axioma.servicio.interfaz.administracion.IOcupacionesMedicasServicio;
import com.servinte.axioma.servicio.interfaz.administracion.IUsuariosServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.INivelAutorizacionOcupacionMedicaServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.INivelAutorizacionServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.INivelAutorizacionUsuarioEspecificoServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.INivelAutorizacionUsuarioServicio;

/**
 * Esta clase se encarga de procesar las solicitudes de la parametrización de los Niveles de Autorización
 * 
 * @author Angela Maria Aguirre
 * @since 20/09/2010
 * 
 * @author Cristhian Murillo
 * @since 18/08/2011
 */
public class ParametrizarNivelAutorizacionUsuarioAction extends Action 
{
	
	/**
	 * Este atributo se utiliza para almacenar la cantidad de columnas 
	 * seleccionadas en la consulta de los centros de costo por entidades
	 * subcontratadas
	 */
	final static int NUMERO_CAMPOS_CENTROS_COSTO_ENT_SUB = 2;
	
	/**
	 * 
	 * Este Método se encarga de ejecutar las acciones sobre las páginas
	 * de parametrización de niveles de autorización por usuario
	 * 
	 * 
	 * @param  ActionMapping mapping, HttpServletRequest request,
	 *         HttpServletResponse response
	 * @return ActionForward     
	 * @author Angela Maria Aguirre
	 *
	 */
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,HttpServletResponse response)
	{
		ActionForward forward=null;
		if (form instanceof ParametrizarNivelAutorizacionUsuarioForm) 
		{
			ParametrizarNivelAutorizacionUsuarioForm forma = (ParametrizarNivelAutorizacionUsuarioForm)form;
			String estado = forma.getEstado();
			Log4JManager.info("estado " + estado);
			forma.setMensaje("");
			UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());
			
			UtilidadTransaccion.getTransaccion().begin();
			
			try
			{
				if(estado.equals("empezar")){
					forma.reset();					
					forward= empezar(forma, mapping);
				}
				
				else if(estado.equals("nuevaOcupacionMedica") || estado.equals("nuevoUsuarioEsp")){
					forward= nuevoRegistro(forma, mapping);
				}

				else if(estado.equals("eliminarUsuarioEspecifico") || estado.equals("eliminarOcupacionMedica")){
					forward= eliminarRegistro(forma, mapping,usuario );
				}
				
				else if(estado.equals("guardar")){
					forward= guardarRegistro(forma, mapping,request,usuario);
				}
				
				else if(estado.equals("buscarNivelesAutorizacionUsuario")){
					llenarListasParametrizacion(forma,mapping,usuario);
					forward= buscarNivelesAutorizacionUsuario(usuario,forma,mapping);
				}
				UtilidadTransaccion.getTransaccion().commit();
			}catch (Exception e) {
				UtilidadTransaccion.getTransaccion().rollback();
				Log4JManager.error("Error en la parametrización del Nivel de Autorización", e);
			}
		}
		return forward;
	}
	
	

	/**
	 * 
	 * Este Método se encarga de consultar los niveles de autorización
	 * registrados en el sistema
	 * 
	 * @param ParametrizarNivelAutorizacionForm forma, ActionMapping mapping
	 * @return ActionForward
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ActionForward empezar(ParametrizarNivelAutorizacionUsuarioForm forma,ActionMapping mapping)
	{
		INivelAutorizacionServicio autorizacionServicio = CapitacionFabricaServicio.crearNivelAutorizacionServicio();
		
		ArrayList<DTONivelAutorizacion> listaNivelAutorizacion = autorizacionServicio.buscarNivelAutorizacion();
		forma.setListaNivelAutorizacion(listaNivelAutorizacion);
		
		return mapping.findForward("principal");	
	}
	
	
	/**
	 * Este método se encarga de consultar los niveles de 
	 * autorización de usuario registrados en el sistema
	 * 
	 * @param ParametrizarNivelAutorizacionUsuarioForm forma,ActionMapping mapping, 
	 *        UsuarioBasico usuario
	 * @return ActionForward
	 * @author Angela Maria Aguirre
	 */
	public ActionForward buscarNivelesAutorizacionUsuario( UsuarioBasico usuarioSesion, ParametrizarNivelAutorizacionUsuarioForm forma,ActionMapping mapping)
	{
		INivelAutorizacionUsuarioServicio nivelUsuarioServicio = CapitacionFabricaServicio.crearNivelAutorizacionUsuarioServicio();
		
		asignarNivelAutorizacionSeleccionada(forma);	
		
		forma.setListaNivelOcupacionMedica(new ArrayList<DTONivelAutorizacionOcupacionMedica>());
		forma.setListaNivelUsuarioEsp(new ArrayList<DTONivelAutorizacionUsuarioEspecifico>());
		
		DTOBusquedaNivelAutorizacionUsuario nivelAutorizacionUsuario = nivelUsuarioServicio.buscarNivelAutorizacionUsuarioDetallado(forma.getNivelAutorizacionSeleccionada().getCodigoPk());	

		if(nivelAutorizacionUsuario!=null)
		{
			if(nivelAutorizacionUsuario.getListaNivelUsuarioEsp()!=null && nivelAutorizacionUsuario.getListaNivelUsuarioEsp().size()>0)
			{
				ArrayList<DTOPrioridadUsuarioEspecifico> listaPrioridad=null;
				HashMap<Integer, DTOPrioridadUsuarioEspecifico>mapaPrioridades=new HashMap<Integer, DTOPrioridadUsuarioEspecifico>(0);
				
				DTOPrioridadUsuarioEspecifico dtoPrioridad = null;
			
				for( DTONivelAutorizacionUsuarioEspecifico registro : nivelAutorizacionUsuario.getListaNivelUsuarioEsp())
				{
					listaPrioridad=new ArrayList<DTOPrioridadUsuarioEspecifico>();
					mapaPrioridades=new HashMap<Integer, DTOPrioridadUsuarioEspecifico>(0);

					for(DtoCheckBox prioridadDTO: forma.getListaPrioridad()){
						dtoPrioridad = new DTOPrioridadUsuarioEspecifico();
												
							dtoPrioridad.setActivo(false);
							dtoPrioridad.setCodigoPk(ConstantesBD.codigoNuncaValido);
							dtoPrioridad.setNivelAutorUsuEspecID(registro.getCodigoPk());
							dtoPrioridad.setNumeroPrioridad(Integer.valueOf(prioridadDTO.getNombre()));							
							mapaPrioridades.put(dtoPrioridad.getNumeroPrioridad(), dtoPrioridad);
						}						
					
					
					for( DTOPrioridadUsuarioEspecifico prioridad:registro.getListaPriodidadUsuarioEsp()){
						if(mapaPrioridades.containsKey(prioridad.getNumeroPrioridad())){
							dtoPrioridad = mapaPrioridades.get(prioridad.getNumeroPrioridad());
						}else{
							dtoPrioridad = new DTOPrioridadUsuarioEspecifico();
						}
						dtoPrioridad.setActivo(true);
						dtoPrioridad.setCodigoPk(prioridad.getCodigoPk());
						dtoPrioridad.setNivelAutorUsuEspecID(prioridad.getNivelAutorUsuEspecID());
						dtoPrioridad.setNumeroPrioridad(prioridad.getNumeroPrioridad());
						
						mapaPrioridades.put(dtoPrioridad.getNumeroPrioridad(),dtoPrioridad);
					}
					
					List<Integer>prioridadesCreadas=new ArrayList<Integer>(mapaPrioridades.keySet());
					
					Collections.sort(prioridadesCreadas);
					
					for(Integer numeroPrioridad:prioridadesCreadas){
						listaPrioridad.add(mapaPrioridades.get(numeroPrioridad));
					}
					
					registro.setListaPriodidadUsuarioEsp(listaPrioridad);
				}
				forma.setListaNivelUsuarioEsp((nivelAutorizacionUsuario.getListaNivelUsuarioEsp()));			
			}		
			
			if(nivelAutorizacionUsuario.getListaNivelOcupacionMedica()!=null && nivelAutorizacionUsuario.getListaNivelOcupacionMedica().size()>0)
			{
				ArrayList<DTOPrioridadOcupacionMedica> listaPrioridad=null;
				DTOPrioridadOcupacionMedica dtoPrioridad = null;
				
				for( DTONivelAutorizacionOcupacionMedica registro : nivelAutorizacionUsuario.getListaNivelOcupacionMedica())
				{					
					listaPrioridad=new ArrayList<DTOPrioridadOcupacionMedica>();
					HashMap<Integer, DTOPrioridadOcupacionMedica> mapaPrioridades=new HashMap<Integer, DTOPrioridadOcupacionMedica>(0);
					
					for(DtoCheckBox prioridadDTO: forma.getListaPrioridad()){
						dtoPrioridad = new DTOPrioridadOcupacionMedica();
						
						dtoPrioridad.setActivo(false);
						dtoPrioridad.setCodigoPk(ConstantesBD.codigoNuncaValido);
						dtoPrioridad.setNivelAutorOcupMedicaID(registro.getCodigoPk());
						dtoPrioridad.setNumeroPrioridad(Integer.valueOf(prioridadDTO.getNombre()));	
						mapaPrioridades.put(dtoPrioridad.getNumeroPrioridad(), dtoPrioridad);
					}
					
					for( DTOPrioridadOcupacionMedica prioridad:registro.getListaPrioridadOcuMedica()){
						if(mapaPrioridades.containsKey(prioridad.getNumeroPrioridad())){
							dtoPrioridad = mapaPrioridades.get(prioridad.getNumeroPrioridad());
						}else{
							dtoPrioridad = new DTOPrioridadOcupacionMedica();
						}
						dtoPrioridad.setActivo(true);
						dtoPrioridad.setCodigoPk(prioridad.getCodigoPk());
						dtoPrioridad.setNivelAutorOcupMedicaID(prioridad.getNivelAutorOcupMedicaID());
						dtoPrioridad.setNumeroPrioridad(prioridad.getNumeroPrioridad());
				
						mapaPrioridades.put(dtoPrioridad.getNumeroPrioridad(),dtoPrioridad);
					}						
					
					List<Integer>prioridadesCreadas=new ArrayList<Integer>(mapaPrioridades.keySet());
					
					Collections.sort(prioridadesCreadas);
					
					for(Integer numeroPrioridad:prioridadesCreadas){
						listaPrioridad.add(mapaPrioridades.get(numeroPrioridad));
					}
					registro.setListaPrioridadOcuMedica(listaPrioridad);
				}				
				forma.setListaNivelOcupacionMedica(nivelAutorizacionUsuario.getListaNivelOcupacionMedica());
			}			
		}
		else
		{
			nivelAutorizacionUsuario = new DTOBusquedaNivelAutorizacionUsuario();
			nivelAutorizacionUsuario.setNivelAutorizacionID(forma.getNivelAutorizacionSeleccionada().getCodigoPk());	
			nivelAutorizacionUsuario.setCodigoPk(ConstantesBD.codigoNuncaValido);
			forma.setMensaje("SinDatos");
		}	
		
		Usuarios usuario = new Usuarios();
		usuario.setLogin(usuarioSesion.getLoginUsuario());
		nivelAutorizacionUsuario.setUsuarios(usuario);
		forma.setNivelAutorizacionUsuario(nivelAutorizacionUsuario);
		forma.setNivelAutorizacionSeleccionadaID(-1);
		
		return mapping.findForward("paginaParametrizacion");	
	}
	
	
	/**
	 * Este Método se encarga de llenar las listas de para iniciar la paramétrica
	 * @author, Angela Maria Aguirre
	 */
	@SuppressWarnings("unchecked")
	public void llenarListasParametrizacion(ParametrizarNivelAutorizacionUsuarioForm forma,ActionMapping mapping, UsuarioBasico usuario)throws SQLException
	{
		Connection con = UtilidadBD.abrirConexion();
		HashMap<String,Object> centrosCostoEntiSubMapa = new HashMap<String,Object>();
		
		DtoCheckBox dtoPrioridad = null;
		ArrayList<DtoCheckBox> listaPrioridad = new ArrayList<DtoCheckBox>();
		
		/*
			int codigoCentroAtencion = usuario.getCodigoCentroAtencion();
			Se modifica para que traiga todos los niveles de todos los Centros de Atención.
		*/
		int codigoCentroAtencion = ConstantesBD.codigoNuncaValido;
		
		centrosCostoEntiSubMapa = CentrosCostoEntidadesSubcontratadas.obtenerPrioridadCentrosCostoEntiSub(codigoCentroAtencion);
		
		if(centrosCostoEntiSubMapa!=null && centrosCostoEntiSubMapa.size()>0)
		{
			int longitudMapa = (centrosCostoEntiSubMapa.size()-1)/NUMERO_CAMPOS_CENTROS_COSTO_ENT_SUB;
			for(int i=0; i<longitudMapa;i++)
			{	
				dtoPrioridad = new DtoCheckBox();
				dtoPrioridad.setNombre((centrosCostoEntiSubMapa.get("nroPrioridad_"+i)).toString());
				dtoPrioridad.setCodigo(centrosCostoEntiSubMapa.get("consecutivo_"+i).toString());
				listaPrioridad.add(dtoPrioridad);
			}
			forma.setListaPrioridad(obtenerListaLimpia(listaPrioridad));
		}	 
		UtilidadBD.cerrarConexion(con);			
		
		IOcupacionesMedicasServicio ocupacionesServicio = AdministracionFabricaServicio.crearOcupacionesMedicasServicio();		
		
		
		/**
		 * MT 5172 - remover las opciones con codigo -1 y 0
		 * */
		List<OcupacionesMedicas>ocupacionesMedicas=ocupacionesServicio.listarOcupacionesMedicas();
		
		List<OcupacionesMedicas>ocupacionesMedicasFiltradas=new ArrayList<OcupacionesMedicas>(0);
		for(OcupacionesMedicas ocupacionMedica:ocupacionesMedicas){
			if(ocupacionMedica.getCodigo()>0){
				ocupacionesMedicasFiltradas.add(ocupacionMedica);
			}
		}
		
		forma.setListaOcupacionMedica(ocupacionesMedicasFiltradas);
		
		/***************************FIN MT 5172*********************/
		
		IUsuariosServicio usuarioServicio = com.servinte.axioma.servicio.fabrica.odontologia.administracion.AdministracionFabricaServicio.crearUsuariosServicio();
		
		ArrayList<DtoUsuarioPersona> listaUsuario = usuarioServicio.obtenerUsuariosSistemas(usuario.getCodigoInstitucionInt(), true);
		
		forma.setListaUsuario(listaUsuario);
	}
	
	
	/**
	 * Este metodo se encarga de obtener la lista de prioridades sin repetir.
	 * MT 402 
	 * 
	 * @author Diana Ruiz
	 * @param lista
	*/
	private ArrayList<DtoCheckBox> obtenerListaLimpia(ArrayList<DtoCheckBox> lista)
	{
		ArrayList<DtoCheckBox> listaTmp = new ArrayList<DtoCheckBox>();
		for(DtoCheckBox d:lista){
			if(!removerElemento(listaTmp, d.getNombre())){
	                listaTmp.add(d);
			}
		}
		return listaTmp;
	}
	   
	  
	/**
	 * Este metodo se encarga validar los elementos repetidos
	 * 
	 * @author Diana Ruiz
	*/
	private boolean removerElemento(ArrayList<DtoCheckBox> myList, String prioridad)
	{
		for(DtoCheckBox d:myList){
			if(d.getNombre().equalsIgnoreCase(prioridad)){
	                return true;
			}
		}
		return false;
	}
	
	
	/**
	 * Este Método se encarga de crear un nuevo registro de nivel
	 * de autorización
	 * 
	 * @return ActionForward
	 * @param ParametrizarNivelAutorizacionForm forma
	 * @author, Angela Maria Aguirre
	 */
	public ActionForward nuevoRegistro(ParametrizarNivelAutorizacionUsuarioForm forma, ActionMapping mapping)
	{
		DTONivelAutorizacionUsuarioEspecifico dtoNivelUsuarioEsp = null;
		DTONivelAutorizacionOcupacionMedica dtoNivelOcupacionMedica = null;
		
		if(forma.getEstado().equals("nuevoUsuarioEsp"))
		{
			dtoNivelUsuarioEsp= new DTONivelAutorizacionUsuarioEspecifico();
			if(forma.getListaNivelUsuarioEsp()==null){
				forma.setListaNivelUsuarioEsp(new ArrayList<DTONivelAutorizacionUsuarioEspecifico>());
			}				
			dtoNivelUsuarioEsp.setCodigoPk(ConstantesBD.codigoNuncaValido);
			
			DTOPrioridadUsuarioEspecifico dtoPrioridad = null;
			ArrayList<DTOPrioridadUsuarioEspecifico> listaPriodidadUsuarioEsp= new ArrayList<DTOPrioridadUsuarioEspecifico>();
			
			for(DtoCheckBox prioridad: forma.getListaPrioridad()){
				dtoPrioridad = new DTOPrioridadUsuarioEspecifico();
				dtoPrioridad.setActivo(false);
				dtoPrioridad.setCodigoPk(ConstantesBD.codigoNuncaValido);						
				dtoPrioridad.setNumeroPrioridad(Integer.valueOf(prioridad.getNombre()));
				listaPriodidadUsuarioEsp.add(dtoPrioridad);
			}			
			dtoNivelUsuarioEsp.setListaPriodidadUsuarioEsp(listaPriodidadUsuarioEsp);
			
			forma.getListaNivelUsuarioEsp().add(dtoNivelUsuarioEsp);
		}
		else
		{
			if(forma.getEstado().equals("nuevaOcupacionMedica")){
				dtoNivelOcupacionMedica = new DTONivelAutorizacionOcupacionMedica();
				if(forma.getListaNivelOcupacionMedica()==null){
					forma.setListaNivelOcupacionMedica(new ArrayList<DTONivelAutorizacionOcupacionMedica>());
				}					
				dtoNivelOcupacionMedica.setCodigoPk(ConstantesBD.codigoNuncaValido);
								
				DTOPrioridadOcupacionMedica dtoPrioridad = null;
				ArrayList<DTOPrioridadOcupacionMedica> listaPriodidadOcuMedica= new ArrayList<DTOPrioridadOcupacionMedica>();
				
				for(DtoCheckBox prioridad: forma.getListaPrioridad()){
					dtoPrioridad = new DTOPrioridadOcupacionMedica();
					dtoPrioridad.setActivo(false);
					dtoPrioridad.setCodigoPk(ConstantesBD.codigoNuncaValido);						
					dtoPrioridad.setNumeroPrioridad(Integer.valueOf(prioridad.getNombre()));
					listaPriodidadOcuMedica.add(dtoPrioridad);
				}	
				
				dtoNivelOcupacionMedica.setListaPrioridadOcuMedica(listaPriodidadOcuMedica);
				
				forma.getListaNivelOcupacionMedica().add(dtoNivelOcupacionMedica);
			}
		}
		
		return mapping.findForward("paginaParametrizacion");	
	}			
	
	
	/**
	 * Este Método se encarga de guardar un registro de nivel
	 * de autorización
	 * 
	 * @return ActionForward 
	 * @param ParametrizarNivelAutorizacionForm forma
	 * @author, Angela Maria Aguirre
	 */
	public ActionForward guardarRegistro(ParametrizarNivelAutorizacionUsuarioForm forma, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario)
	{
		INivelAutorizacionUsuarioServicio nivelUsuarioServicio = CapitacionFabricaServicio.crearNivelAutorizacionUsuarioServicio();
		ActionMessages errores = new ActionMessages();
		
		errores = validarDetalles(forma);
		if(errores.isEmpty())
		{
			DTOBusquedaNivelAutorizacionUsuario dto = forma.getNivelAutorizacionUsuario();
			dto.setListaNivelOcupacionMedica(forma.getListaNivelOcupacionMedica());
			dto.setListaNivelUsuarioEsp(forma.getListaNivelUsuarioEsp());
			dto.setNivelAutorizacionID(forma.getNivelAutorizacionSeleccionada().getCodigoPk());
			
			if(nivelUsuarioServicio.guardarNivelAutorizacionUsuarioDetallado(dto))
			{
				UtilidadTransaccion.getTransaccion().commit();				
				forma.setMensaje("exitoso");
			}		
			
			return buscarNivelesAutorizacionUsuario(usuario, forma, mapping);			
		}
		else
		{
			saveErrors(request, errores);
		}	
		
		return mapping.findForward("paginaParametrizacion");
	}
	
	
	/**
	 * Este Método se encarga de eliminar un registro de nivel de autorización
	 * 
	 * @return ActionForward 
	 * @param ParametrizarNivelAutorizacionForm forma
	 * @author, Angela Maria Aguirre
	 */
	public ActionForward eliminarRegistro(ParametrizarNivelAutorizacionUsuarioForm forma, ActionMapping mapping, UsuarioBasico usuario)
	{
		int i=0;
		
		if(forma.getEstado().equals("eliminarOcupacionMedica"))
		{
			INivelAutorizacionOcupacionMedicaServicio nivelOcupacionServicio = CapitacionFabricaServicio.crearNivelAutorizacionOcupacionMedicaServicio();
			
			if(forma.getListaNivelOcupacionMedica()!=null && forma.getListaNivelOcupacionMedica().size()>0)
			{
				for(DTONivelAutorizacionOcupacionMedica registro : forma.getListaNivelOcupacionMedica())
				{
					if(i == forma.getIndice())
					{						
						if(registro.getCodigoPk()!=ConstantesBD.codigoNuncaValido)
						{
							if(nivelOcupacionServicio.eliminarNivelAutorizacionOcupacionMedica(registro.getCodigoPk()))
							{
								UtilidadTransaccion.getTransaccion().commit();
								forma.setMensaje("exitoso");
							}							
							return buscarNivelesAutorizacionUsuario(usuario,forma, mapping);
						}
						else{
							forma.getListaNivelOcupacionMedica().remove(i);
						}
						break;
					}
					i++;
				}			
			}
		}
		else
		{
			if(forma.getEstado().equals("eliminarUsuarioEspecifico"))
			{
				INivelAutorizacionUsuarioEspecificoServicio nivelUsuarioEspServicio = CapitacionFabricaServicio.crearNivelAutorizacionUsuarioEspecificoServicio();
								
				if(forma.getListaNivelUsuarioEsp()!=null && forma.getListaNivelUsuarioEsp().size()>0)
				{
					for(DTONivelAutorizacionUsuarioEspecifico registro : forma.getListaNivelUsuarioEsp())
					{
						if(i == forma.getIndice())
						{
							if(registro.getCodigoPk()!=ConstantesBD.codigoNuncaValido)
							{
								if(nivelUsuarioEspServicio.eliminarNivelAutorizacionUsuarioEspecifico(registro.getCodigoPk()))
								{
									UtilidadTransaccion.getTransaccion().commit();
									forma.setMensaje("exitoso");
								}
								return buscarNivelesAutorizacionUsuario(usuario, forma, mapping);
								
							}else{
								forma.getListaNivelUsuarioEsp().remove(i);
							}
							break;
						}
						i++;
					}			
				}
			}
		}
		
		forma.setMensaje("exitoso");
		return mapping.findForward("paginaParametrizacion");
	}
	
	
	/**
	 * 
	 * Este Método se encarga de validar que no existan registros repetidos
	 * 
	 * @param ParametrizarNivelAutorizacionUsuarioForm forma
	 * @return ActionErrors
	 * @author, Angela Maria Aguirre
	 */
	public ActionErrors validarDetalles(ParametrizarNivelAutorizacionUsuarioForm forma)
	{
		ActionErrors errores = new ActionErrors();
		MessageResources mensajes=MessageResources.getMessageResources("com.servinte.mensajes.capitacion.ParametrizarNivelAutorizacionPorUsuarioForm");
		int indice=0;
		
		if((forma.getListaNivelUsuarioEsp()!=null && forma.getListaNivelUsuarioEsp().size()>0)||
				(forma.getListaNivelOcupacionMedica()!=null && forma.getListaNivelOcupacionMedica().size()>0)){				
			
			if(forma.getListaNivelUsuarioEsp()!=null && forma.getListaNivelUsuarioEsp().size()>0)
			{
				for(DTONivelAutorizacionUsuarioEspecifico registro : forma.getListaNivelUsuarioEsp())
				{							
					if(UtilidadTexto.isEmpty(registro.getUsuarioEspLogin()))
					{
						errores.add("El usuario específico es requerido", new ActionMessage("errors.notEspecific", 
								mensajes.getMessage("parametrizarNivelAutorizacionPorUsuarioForm.UsuarioEspRequerido",indice+1)));
					}
					
					if(registro.getListaPriodidadUsuarioEsp()!=null && registro.getListaPriodidadUsuarioEsp().size()>0)
					{
						boolean prioridadSeleccionada=false;
						
						for(DTOPrioridadUsuarioEspecifico prioridad : registro.getListaPriodidadUsuarioEsp())
						{
							if(prioridad.isActivo()){
								prioridadSeleccionada=true;
								break;
							}
						}
						
						if(!prioridadSeleccionada)
						{
							errores.add("Prioridad Requerida", new ActionMessage("errors.notEspecific", 
									mensajes.getMessage("parametrizarNivelAutorizacionPorUsuarioForm.prioridadUsuEspRequerida",indice+1)));
						}
					}
					indice++;
				}
			}				
			
			if(forma.getListaNivelOcupacionMedica()!=null && forma.getListaNivelOcupacionMedica().size()>0)
			{
				indice=0;
				for(DTONivelAutorizacionOcupacionMedica registro : forma.getListaNivelOcupacionMedica())
				{
					if(registro.getOcupacionMedicaID()==ConstantesBD.codigoNuncaValido)
					{
						errores.add("Registro Repetido", new ActionMessage("errors.notEspecific", 
								mensajes.getMessage("parametrizarNivelAutorizacionPorUsuarioForm.ocupacionMedicaRequerida",indice+1)));
					}	
					
					if(registro.getListaPrioridadOcuMedica()!=null && registro.getListaPrioridadOcuMedica().size()>0)
					{
						boolean prioridadSeleccionada=false;
						for(DTOPrioridadOcupacionMedica prioridad : registro.getListaPrioridadOcuMedica())
						{
							if(prioridad.isActivo()){
								prioridadSeleccionada=true;
								break;
							}
						}
						
						if(!prioridadSeleccionada)
						{
							errores.add("Prioridad Requerida", new ActionMessage("errors.notEspecific", 
									mensajes.getMessage("parametrizarNivelAutorizacionPorUsuarioForm.prioridadOcuMedicaRequerida",indice+1)));
						}
					}
					indice++;
				}
			}				
		}
		else
		{
			errores.add("Detalle Vacío", new ActionMessage("errors.notEspecific", 
					mensajes.getMessage("parametrizarNivelAutorizacionPorUsuarioForm.detalleVacio")));				
		}

		if(errores.isEmpty())
		{
			indice=0;
			if(forma.getListaNivelUsuarioEsp()!=null && forma.getListaNivelUsuarioEsp().size()>0)
			{
				
				for(DTONivelAutorizacionUsuarioEspecifico registroValidar : forma.getListaNivelUsuarioEsp())
				{
					int j=0; 
					for(DTONivelAutorizacionUsuarioEspecifico registro : forma.getListaNivelUsuarioEsp())
					{
						if(j>indice)
						{
							if(registroValidar.equals(registro))
							{
								errores.add("Registro Repetido", new ActionMessage("errors.notEspecific", 
										mensajes.getMessage("parametrizarNivelAutorizacionPorUsuarioForm.usuarioEspecificoRepetido",j+1)));
							}
						}
						j++;
					}
					indice++;
				}			
			}
			
			indice=0;
			
			if(forma.getListaNivelOcupacionMedica()!=null && forma.getListaNivelOcupacionMedica().size()>0)
			{
				for(DTONivelAutorizacionOcupacionMedica registroValidar : forma.getListaNivelOcupacionMedica())
				{
					int j=0; 
					for(DTONivelAutorizacionOcupacionMedica registro : forma.getListaNivelOcupacionMedica())
					{
						if(j>indice)
						{
							if(registroValidar.equals(registro))
							{
								errores.add("Registro Repetido", new ActionMessage("errors.notEspecific", 
										mensajes.getMessage("parametrizarNivelAutorizacionPorUsuarioForm.ocupacionMedicaRepetida",j+1)));
							}
						}
						j++;
					}
					indice++;
				}			
			}
		}
		return errores;		
	}

	
	/**
	 * Este Método se encarga de asignar el nivel de autorización seleccionado
	 * @author, Angela Maria Aguirre
	*/
	private void asignarNivelAutorizacionSeleccionada(ParametrizarNivelAutorizacionUsuarioForm forma)
	{
		DTONivelAutorizacion nivelAutorizacion=new DTONivelAutorizacion();		
	
		for(DTONivelAutorizacion registro :forma.getListaNivelAutorizacion())
		{
			if(registro.getCodigoPk()==forma.getNivelAutorizacionSeleccionadaID())
			{
				nivelAutorizacion.setCodigoPk(registro.getCodigoPk());
				nivelAutorizacion.setDescripcion(registro.getDescripcion());
				forma.setNivelAutorizacionSeleccionada(nivelAutorizacion);
				break;
			}
		}
	}
	
	

}
