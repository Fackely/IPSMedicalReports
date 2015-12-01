package com.princetonsa.action.odontologia;

import java.sql.Connection;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.odontologia.ProgramasOdontologicosForm;
import com.princetonsa.dto.odontologia.DtoDetalleProgramas;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.odontologia.ProgramasOdontologicos;
import com.princetonsa.sort.odontologia.SortProgramasOdontologicos;
import com.princetonsa.sort.odontologia.SortProgramasOdontologicosDetalle;

public class ProgramasOdontologicosAction extends Action {
	
	private Logger logger = Logger.getLogger(ProgramaAction.class);
	
	public ActionForward execute(	ActionMapping mapping,
		 	ActionForm form,
		 	HttpServletRequest request,
		 	HttpServletResponse response) throws Exception
		 	{
				if(form instanceof ProgramasOdontologicosForm)
				{
					ProgramasOdontologicosForm forma = (ProgramasOdontologicosForm)form;	
					UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
					ActionErrors errores = new ActionErrors();
					
					logger.info("EL ESTADO DE ProgramasOdontologicos=====>"+forma.getEstado());
					
					if (forma.getEstado().equals("ingresarModificarPO"))
					{
						forma.reset();
						return accionIngresarModificar(mapping, forma);
					}
					
					else if (forma.getEstado().equals("consultar"))
					{
						return accionIngresarModificar(mapping, forma);
					}
					
					else if (forma.getEstado().equals("buscarProgramasXEspecialidad"))
					{
						return accionBuscarProgramasXEspecialidad(mapping, forma);
					}
					
					else if (forma.getEstado().equals("nuevoPrograma"))
					{
						forma.resetMsj();
						forma.getDtoProgramaFiltro().clean2();
						return mapping.findForward("ingresarModificarPO");
					}
					
					else if (forma.getEstado().equals("guardarPrograma"))
					{
						return accionGuardarPrograma(mapping, forma, request, usuario,errores);
					}
					
					else if (forma.getEstado().equals("cargarDetallePrograma"))
					{
						return accionCargarDetallePrograma(mapping, forma, request, usuario);
					}
					
					else if (forma.getEstado().equals("buscarServicio")) 
					{
						forma.resetMsj();
						return accionBuscarServicio(mapping, forma, usuario, request,errores);
					}
					
					else if (forma.getEstado().equals("guardarDetalle"))
					{
						return accionGuardarDetalle(mapping, forma, request, usuario);
					}
					
					else if (forma.getEstado().equals("eliminarPrograma"))
					{
						return accionEliminarPrograma(mapping, forma, request, usuario,errores);
					}
					
					else if (forma.getEstado().equals("cargarModificarPrograma"))
					{
						return accionCargarModificarPrograma(mapping, forma, request, usuario);
					}
					
					else if (forma.getEstado().equals("modificarPrograma"))
					{
						return accionModificarPrograma(mapping, forma, request, usuario,errores);
					}
					
					else if (forma.getEstado().equals("eliminarDetalle"))
					{
						return accionEliminarDetalle(mapping, forma, request, usuario,errores);
					}
					else if(forma.getEstado().equals("cargarServicios"))
					{
						return this.accionCargarDetallePrograma(mapping, forma,usuario);
					}
					else if(forma.getEstado().equals("ordenarDetalle"))
					{
				    	return accionOrdenarDetalle(mapping,forma);
			       	}	
					else if(forma.getEstado().equals("ordenarProgramas"))
					{
				    	return accionOrdenarProgramas(mapping,forma);
			       	}
					
					else 
					{
						forma.reset();
						logger.warn("Estado no Valido dentro del Flujo");
						request.setAttribute("CodigoDescripcionError","errors.estadoInvalido");
						return mapping.findForward("paginaError");
					}
				}
				
			
				
			return null;
				
		 	}
	
	/**
	 * @param mapping
	 * @param forma
	 * @return
	 */
	private ActionForward accionIngresarModificar(ActionMapping mapping, ProgramasOdontologicosForm forma) 
	{
		Connection con=UtilidadBD.abrirConexion();
		if (forma.isEsConsulta())
			forma.resetConsulta();
		forma.setEspecialidadesOdontologia(Utilidades.obtenerEspecialidadesEnArray(con, ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica+""));
		return mapping.findForward("ingresarModificarPO");
	}	
	
	/**
	 * @param mapping
	 * @param forma
	 * @return
	 */
	private ActionForward accionBuscarProgramasXEspecialidad(ActionMapping mapping, ProgramasOdontologicosForm forma) 
	{
		forma.resetMsj();
		forma.setListadoProgramas(ProgramasOdontologicos.buscarProgramasXEspecialidad(forma.getDtoProgramaFiltro()));
		return mapping.findForward("ingresarModificarPO");
	}
	
	/**
	 * @param mapping
	 * @param forma
	 * @param request
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardarPrograma(ActionMapping mapping, ProgramasOdontologicosForm forma,HttpServletRequest request,UsuarioBasico usuario,ActionErrors errores) 
	{
		//Primero se setean los valores adicionales que no estan en el DTO
		boolean nombre=false;
		boolean codigo=false;
		nombre=accionValidarNombreEnEspecialidad(forma);
		codigo=accionValidarCodigoEnEspecialidad(forma);
		 
		
		
		if (!codigo&&!nombre)
		{
			//Realizo la validación por si ya existe el codigo del programa
			if (!ProgramasOdontologicos.existeProgramaConNombre(forma.getDtoProgramaFiltro().getCodigoPrograma()))
			{
			
				forma.getDtoProgramaFiltro().setFechaModifica(UtilidadFecha.getFechaActual());
				forma.getDtoProgramaFiltro().setHoraModifica(UtilidadFecha.getHoraActual());
				forma.getDtoProgramaFiltro().setUsuarioModifica(usuario.getLoginUsuario());
				forma.getDtoProgramaFiltro().setInstitucion(usuario.getCodigoInstitucionInt());
				forma.getDtoProgramaFiltro().getDtoConvencion().setConsecutivo(forma.getConvencion());
				
				if (ProgramasOdontologicos.insertarPrograma(forma.getDtoProgramaFiltro()))
				{
					forma.setListadoProgramas(ProgramasOdontologicos.buscarProgramasXEspecialidad(forma.getDtoProgramaFiltro()));
					forma.setMensaje("Programa Ingresado Correctamente!");
					forma.getDtoProgramaFiltro().clean2();
					//Vacio la convencion por si quieren insertar 
					forma.setConvencion(ConstantesBD.codigoNuncaValido);
					return mapping.findForward("ingresarModificarPO");
				}
				else
				{
					forma.reset();
					logger.warn("Estado no Valido dentro del Flujo");
					request.setAttribute("CodigoDescripcionError","errors.estadoInvalido");
					return mapping.findForward("paginaError");
				}
			}
			else
			{
	
				errores.add("",	new ActionMessage("errors.notEspecific","El código del programa "+forma.getDtoProgramaFiltro().getCodigoPrograma()+" ya fue utilizado en otro programa. Porfavor usar otro."));
				saveErrors(request, errores);
				return mapping.findForward("ingresarModificarPO");
			}	
		}
		else
		{
			if (codigo)
			{
				errores.add("",	new ActionMessage("errors.notEspecific","El código del programa "+forma.getDtoProgramaFiltro().getCodigoPrograma()+" ya fue utilizado en otro programa. Porfavor usar otro."));
				saveErrors(request, errores);
			}
			
			if (nombre)
			{
				errores.add("",	new ActionMessage("errors.notEspecific","El nombre del programa "+forma.getDtoProgramaFiltro().getNombre()+" ya fue utilizado en otro programa. Porfavor usar otro."));
				saveErrors(request, errores);
			}
			
			return mapping.findForward("ingresarModificarPO");
		}
	}
	
	/**
	 *Metodo que valida si ya existe el codigo para la especialidad 
	 */
	private boolean accionValidarCodigoEnEspecialidad(ProgramasOdontologicosForm forma)
	{
		boolean existeCodigo=false;
		int sizeListadoProgramas=forma.getListadoProgramas().size();
		boolean esModificacion=forma.getPosPrograma()!=ConstantesBD.codigoNuncaValido?true:false;
		
		logger.info("\n\nENTRE A VALIDAR SI EL CODIGO YA EXISTE!!!");
		logger.info("\n\nLA POS------>"+forma.getPosPrograma()+" / "+esModificacion);
		
		
		
		for (int i=0;i<sizeListadoProgramas;i++)
		{
			if ((forma.getListadoProgramas().get(i).getCodigoPrograma().toUpperCase().equals(forma.getDtoProgramaFiltro().getCodigoPrograma().toUpperCase()))
					&&!esModificacion)
				existeCodigo=true;				 
		}
		
		logger.info("EL VALOR DE LA VALIDACION ES---->"+existeCodigo);
		
		return existeCodigo;
	}
	/**
	 *Metodo que valida si ya existe el nombre para la especialidad 
	 */
	private boolean accionValidarNombreEnEspecialidad(ProgramasOdontologicosForm forma)
	{
		boolean existeCodigo=false;
		int sizeListadoProgramas=forma.getListadoProgramas().size();
		boolean esModificacion=forma.getPosPrograma()!=ConstantesBD.codigoNuncaValido?true:false;
		
		logger.info("\n\nENTRE A VALIDAR SI EL CODIGO YA EXISTE!!!");
		logger.info("\n\nLA POS------>"+forma.getPosPrograma()+" / "+esModificacion);
		
		for (int i=0;i<sizeListadoProgramas;i++)
		{
			if ((forma.getListadoProgramas().get(i).getNombre().toUpperCase().equals(forma.getDtoProgramaFiltro().getNombre()))
					&&!esModificacion)
				existeCodigo=true;			 
			
				
				
		}
		
		logger.info("EL VALOR DE LA VALIDACION ES---->"+existeCodigo);
		
		return existeCodigo;
	}
	
	
	/**
	 * @param mapping
	 * @param forma
	 * @param request
	 * @param usuario
	 * @return
	 */
	private ActionForward accionCargarDetallePrograma(ActionMapping mapping, ProgramasOdontologicosForm forma,HttpServletRequest request,UsuarioBasico usuario)
	{
		forma.resetMsj();
		int pos= forma.getPosPrograma();
		double codigoPrograma=forma.getListadoProgramas().get(pos).getCodigo();
		
		String codigoManualBusqueda=ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(usuario.getCodigoInstitucionInt());
		forma.setListadoDetalleProgramas(ProgramasOdontologicos.cargarDetallePrograma(codigoPrograma,codigoManualBusqueda));
		
		recargarClonarDetalle(forma,usuario);
		
		return mapping.findForward("detallePrograma");
	}
	
	
	/**
	 * METODO PARA CARGAR TODOS LOS SERVICIOS DE UN PROGRAMA 
	 * @param mapping
	 * @param forma
	 * @return
	 */
	private ActionForward accionCargarDetallePrograma(ActionMapping mapping , ProgramasOdontologicosForm forma, UsuarioBasico usuario ){
		String codigoManualBusqueda=ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(usuario.getCodigoInstitucionInt());
		forma.setListadoDetalleProgramas(ProgramasOdontologicos.cargarDetallePrograma(forma.getCodioProgramaTmp(),codigoManualBusqueda));
		return mapping.findForward("consultarServicios");
	}
	
	
	
	/**
	 * @param mapping
	 * @param forma
	 */
	private ActionForward accionBuscarServicio(ActionMapping mapping, ProgramasOdontologicosForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionErrors errores) 
	{
		//Actualización Anexo 857 se agrega la validacion con el parametro: Múltiplo en minutos para generación de citas 
		int multiploMinsGeneracionCita=Utilidades.convertirAEntero(ValoresPorDefecto.getMultiploMinGeneracionCita(usuario.getCodigoInstitucionInt()));
		int minutosServicio=Utilidades.convertirAEntero(forma.getServiciosMap().get("minutos")+"");
		

		if ((minutosServicio%multiploMinsGeneracionCita)==0)
		{
			
			DtoDetalleProgramas nuevo = new DtoDetalleProgramas();
			//Se setean los campos del DTO que sirve de filtro para ingresar servicio	
			double codigoPkPrograma=forma.getListadoProgramas().get(forma.getPosPrograma()).getCodigo();
			nuevo.setProgramas(codigoPkPrograma);
			logger.info("CodigoServicioNUEVO >>"+forma.getServiciosMap().get("codServicio")+"");
			nuevo.setServicio(Utilidades.convertirAEntero(forma.getServiciosMap().get("codServicio")+""));
			nuevo.setCodigoCUPS(forma.getServiciosMap().get("codigoCups")+"");
			nuevo.setAcronimoTarifario(forma.getServiciosMap().get("acronimoTarifario")+"");
			nuevo.setDescripcionServicio(forma.getServiciosMap().get("servicioDesc")+"");
			nuevo.setEnBD(false);
			
			//Se verifica el tamaño del listado para obtener la ultima posicion del orden
			int tamanioListaDetalle=forma.getListadoDetalleProgramas().size()+1;
			
			// Número de orden
			int orden=1;
			for (int i=0;i<forma.getListadoDetalleProgramas().size();i++)
				if(forma.getListadoDetalleProgramas().get(i).getEliminado().equals(ConstantesBD.acronimoNo))
					orden++;
				
			nuevo.setOrden(orden);
			
			
			
			
			//Se validan si los minutos d eduracion del parametro general son multiplos del servicio
			boolean existeServicio=false;
			for (int i=0;i<forma.getListadoDetalleProgramas().size();i++)
			{
				logger.info("Codigo Servicio EXISTENTE >> "+forma.getListadoDetalleProgramas().get(i).getServicio()+ "CODIGO NUEVO "+nuevo.getServicio() );
				if (forma.getListadoDetalleProgramas().get(i).getServicio()==nuevo.getServicio() && forma.getListadoDetalleProgramas().get(i).getEliminado().equals(ConstantesBD.acronimoNo))
				{
					existeServicio=true;
					errores.add("",	new ActionMessage("errors.notEspecific","El servicio ya fue parametrizado. Por favor seleccione otro."));
					saveErrors(request, errores);
				}
			}
			
			if(!existeServicio)
				forma.getListadoDetalleProgramas().add(nuevo);
			
		}
		else
		{
			errores.add("",	new ActionMessage("errors.notEspecific","Los minutos de duración del servicio seleccionado No son múltiplos del valor definido en el parámetro 'Múltiplo en minutos para la generación de la cita'. Por favor verifique."));
			saveErrors(request, errores);
		}
		
		return mapping.findForward("detallePrograma");
	}	
	
	/**
	 * @param mapping
	 * @param forma
	 * @param request
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardarDetalle(ActionMapping mapping, ProgramasOdontologicosForm forma,HttpServletRequest request,UsuarioBasico usuario) 
	{
		//Se verifica si hay cambios en el estado de alguno de los elementos del listado
		int tamanioListadoDet=forma.getListadoDetalleProgramas().size();
		String activo;
		String activoCopia;
		double codigoDetalle;
		
		for(int i=0;i<tamanioListadoDet;i++)
		{
			activo=forma.getListadoDetalleProgramas().get(i).getActivo();
			//Se usa la copia sacada previamente 
			//activoCopia=forma.getCopiaListadoDetalle().get(i).getActivo();
			codigoDetalle=forma.getListadoDetalleProgramas().get(i).getCodigoPk();
			
			//Primero se setean los valores adicionales que no estan en el DTO del usuario y fechas, ya que el resto se seteo en accionBuscarServicio
			forma.getListadoDetalleProgramas().get(i).setFecha(UtilidadFecha.getFechaActual());
			forma.getListadoDetalleProgramas().get(i).setHora(UtilidadFecha.getHoraActual());
			forma.getListadoDetalleProgramas().get(i).setUsuario(usuario.getLoginUsuario());
			
			if (/*!activo.equals(activoCopia) && */ forma.getListadoDetalleProgramas().get(i).isEnBD())
			{
				forma.getListadoDetalleProgramas().get(i).setEliminado(ConstantesBD.acronimoNo);
				//Se ingresa el log y luego se actualiza 
				if (ProgramasOdontologicos.insertarLogDetalle(forma.getListadoDetalleProgramas().get(i)))
				{
					if (ProgramasOdontologicos.actualizarDetalle(activo,codigoDetalle))
						forma.setMensaje("Programas Modificados Exitosamente!");
				}
			}
			
			// Si el registro es nuevo y no esta eliminado
			if(!forma.getListadoDetalleProgramas().get(i).isEnBD() 
					&& forma.getListadoDetalleProgramas().get(i).getEliminado().equals(ConstantesBD.acronimoNo)){
				
				if(ProgramasOdontologicos.insertarDetalle(forma.getListadoDetalleProgramas().get(i)))
					forma.setMensaje("Detalle de Servicio Guardado Exitosamente!");
			}
			
		}
		
		forma.getFiltrosDetallePO().clean();	
		recargarClonarDetalle(forma,usuario);
		
		
		return mapping.findForward("detallePrograma");
	}
	
	/**
	 * @param mapping
	 * @param forma
	 * @param request
	 * @param usuario
	 * @param errores
	 * @return
	 */
	private ActionForward accionEliminarPrograma(ActionMapping mapping, ProgramasOdontologicosForm forma,HttpServletRequest request,UsuarioBasico usuario, ActionErrors errores) 
	{
		//Se saca el dto para el log y el codigo del elemento a eliminar
		int pos= forma.getPosPrograma();
		forma.setLogPrograma(forma.getListadoProgramas().get(pos));
		double codigoPrograma=forma.getListadoProgramas().get(pos).getCodigo();
		
		//Se agregan los campos adicionales de fecha, hora, usuario, eliminado en el dto
		forma.getLogPrograma().setEliminado(ConstantesBD.acronimoSi);
		forma.getLogPrograma().setFechaModifica(UtilidadFecha.getFechaActual());
		forma.getLogPrograma().setHoraModifica(UtilidadFecha.getHoraActual());
		forma.getLogPrograma().setUsuarioModifica(usuario.getLoginUsuario());
		
		if (ProgramasOdontologicos.insertarLogPrograma(forma.getLogPrograma()))
		{
			if (ProgramasOdontologicos.eliminarPrograma(codigoPrograma))
			{
				forma.setMensaje("Programa Eliminado Exitosamente!");
				forma.setListadoProgramas(ProgramasOdontologicos.buscarProgramasXEspecialidad(forma.getDtoProgramaFiltro()));
			}
			else
			{
				errores.add("",	new ActionMessage("errors.notEspecific","No se puede Eliminar el Programa ya que posee Detalles Asociados."));
				saveErrors(request, errores);
			}
		}
		else
		{
			errores.add("",	new ActionMessage("errors.notEspecific","No se puede Eliminar el Programa."));
			saveErrors(request, errores);
		}
		return mapping.findForward("ingresarModificarPO");
	}
	
	/**
	 * @param mapping
	 * @param forma
	 * @param request
	 * @param usuario
	 * @return
	 */
	private ActionForward accionCargarModificarPrograma(ActionMapping mapping, ProgramasOdontologicosForm forma,HttpServletRequest request,UsuarioBasico usuario) 
	{
		//Obtengo toda la posicion a modificar
		int pos= forma.getPosPrograma();
		forma.setDtoProgramaFiltro(forma.getListadoProgramas().get(pos));
		
		//Cargo el archivo de la imagen para mostrarlo
		forma.setArchImagen(forma.getListadoProgramas().get(pos).getDtoConvencion().getArchivoConvencion());

		//Se carga el DTO para el LOG
		forma.setLogPrograma(forma.getListadoProgramas().get(pos));
		//Se agregan los campos adicionales de fecha, hora, usuario, eliminado en el dto
		forma.getLogPrograma().setEliminado(ConstantesBD.acronimoNo);
		forma.getLogPrograma().setFechaModifica(UtilidadFecha.getFechaActual());
		forma.getLogPrograma().setHoraModifica(UtilidadFecha.getHoraActual());
		forma.getLogPrograma().setUsuarioModifica(usuario.getLoginUsuario());
		
		return mapping.findForward("ingresarModificarPO");
	}
	
	/**
	 * @param mapping
	 * @param forma
	 * @param request
	 * @param usuario
	 * @param errores
	 * @return
	 */
	private ActionForward accionModificarPrograma(ActionMapping mapping, ProgramasOdontologicosForm forma,HttpServletRequest request,UsuarioBasico usuario, ActionErrors errores) 
	{
		
		boolean nombre=false;
		boolean codigo=false;
		nombre=accionValidarNombreEnEspecialidad(forma);
		codigo=accionValidarCodigoEnEspecialidad(forma);
		
		if (!codigo&&!nombre)
		{
		
			//Se inserta el log de como estaban anteiormente los datos y luego se actualiza
			if (forma.getConvencion()!=ConstantesBD.codigoNuncaValido)
				forma.getDtoProgramaFiltro().getDtoConvencion().setConsecutivo(forma.getConvencion()); 
			
			if (ProgramasOdontologicos.insertarLogPrograma(forma.getLogPrograma()))
			{
				if(ProgramasOdontologicos.actualizarPrograma(forma.getDtoProgramaFiltro()))
				{
					forma.setMensaje("Programa Actualizado Exitosamente!");
					forma.setListadoProgramas(ProgramasOdontologicos.buscarProgramasXEspecialidad(forma.getDtoProgramaFiltro()));
				}
				else
				{
					errores.add("",	new ActionMessage("errors.notEspecific","No se puede Actualizar el Programa"));
					saveErrors(request, errores);
				}
			}
			else
			{
				errores.add("",	new ActionMessage("errors.notEspecific","No se puede Actualizar el Programa"));
				saveErrors(request, errores);
			}
			forma.getDtoProgramaFiltro().clean2();
			return mapping.findForward("ingresarModificarPO");
		}
		else
		{
			if (codigo)
			{
				errores.add("",	new ActionMessage("errors.notEspecific","El código del programa "+forma.getDtoProgramaFiltro().getCodigoPrograma()+" ya fue utilizado en otro programa. Porfavor usar otro."));
				saveErrors(request, errores);
			}
			if (nombre)
			{
				errores.add("",	new ActionMessage("errors.notEspecific","El nombre del programa "+forma.getDtoProgramaFiltro().getNombre()+" ya fue utilizado en otro programa. Porfavor usar otro."));
				saveErrors(request, errores);
			}
			return mapping.findForward("ingresarModificarPO");
		}
	}
	
	/**
	 * @param mapping
	 * @param forma
	 * @param request
	 * @param usuario
	 * @param errores
	 * @return
	 */
	private ActionForward accionEliminarDetalle(ActionMapping mapping, ProgramasOdontologicosForm forma,HttpServletRequest request,UsuarioBasico usuario, ActionErrors errores) 
	{
		if(forma.getListadoDetalleProgramas().get(forma.getPosDetalle()).isEnBD()){
			//Se saca el dto para el log y el codigo del elemento a eliminar
			int posDet= forma.getPosDetalle();
			forma.setLogDetalle(forma.getListadoDetalleProgramas().get(posDet));
			double codigoDetalle=forma.getListadoDetalleProgramas().get(posDet).getCodigoPk();
			
			//Se agregan los campos adicionales de fecha, hora, usuario, eliminado en el dto
			forma.getLogDetalle().setEliminado(ConstantesBD.acronimoSi);
			forma.getLogDetalle().setFecha(UtilidadFecha.getFechaActual());
			forma.getLogDetalle().setHora(UtilidadFecha.getHoraActual());
			forma.getLogDetalle().setUsuario(usuario.getLoginUsuario());
			
			String codigoManualBusqueda=ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(usuario.getCodigoInstitucionInt());
			
			if (ProgramasOdontologicos.insertarLogDetalle(forma.getLogDetalle()))
			{
				if (ProgramasOdontologicos.eliminarDetallePrograma(codigoDetalle))
				{
					forma.setMensaje("Detalle de Servicio Eliminado Exitosamente!");
					//Se lista otra vez
					int pos= forma.getPosPrograma();
					double codigoPrograma=forma.getListadoProgramas().get(pos).getCodigo();
					forma.setListadoDetalleProgramas(ProgramasOdontologicos.cargarDetallePrograma(codigoPrograma,codigoManualBusqueda));
				}
				else
				{
					errores.add("",	new ActionMessage("errors.notEspecific","No se puede Eliminar el Detalle de Servicios."));
					saveErrors(request, errores);
				}
			}
			else
			{
				errores.add("",	new ActionMessage("errors.notEspecific","No se puede Eliminar el Detalle de Servicios."));
				saveErrors(request, errores);
			}
		} else {
			forma.getListadoDetalleProgramas().get(forma.getPosDetalle()).setEliminado(ConstantesBD.acronimoSi);
		}
		
		return mapping.findForward("detallePrograma");
	}
	
	private void recargarClonarDetalle(ProgramasOdontologicosForm forma, UsuarioBasico usuario)
	{
		int pos= forma.getPosPrograma();
		double codigoPrograma=forma.getListadoProgramas().get(pos).getCodigo();
		String codigoManualBusqueda=ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(usuario.getCodigoInstitucionInt());
		
		forma.setListadoDetalleProgramas(ProgramasOdontologicos.cargarDetallePrograma(codigoPrograma,codigoManualBusqueda));
		for(int i=0;i<forma.getListadoDetalleProgramas().size();i++)
		{
			DtoDetalleProgramas dto= new DtoDetalleProgramas();
			try 
			{
				PropertyUtils.copyProperties(dto,forma.getListadoDetalleProgramas().get(i));
				forma.getCopiaListadoDetalle().add(dto);
			}
			catch(Exception e)
			{
				logger.warn(e);								
			}
		}
	}
	
	/**
	 * 
	 * @param mapping
	 * @param con
	 * @param forma
	 * @return
	 */
	private ActionForward accionOrdenarDetalle(ActionMapping mapping, ProgramasOdontologicosForm forma) 
	{
		SortProgramasOdontologicosDetalle sort= new SortProgramasOdontologicosDetalle();
		sort.setPatronOrdenar(forma.getPatronOrdenar());
		Collections.sort(forma.getListadoDetalleProgramas(), sort);
		forma.setEstado(forma.getEstadoAnterior());
		return mapping.findForward("detallePrograma");
	}
	
	/**
	 * 
	 * @param mapping
	 * @param con
	 * @param forma
	 * @return
	 */
	private ActionForward accionOrdenarProgramas(ActionMapping mapping, ProgramasOdontologicosForm forma) 
	{
		SortProgramasOdontologicos sort= new SortProgramasOdontologicos();
		sort.setPatronOrdenar(forma.getPatronOrdenar());
		Collections.sort(forma.getListadoProgramas(), sort);
		forma.setEstado(forma.getEstadoAnterior());
		return mapping.findForward("ingresarModificarPO");
	}
	
}