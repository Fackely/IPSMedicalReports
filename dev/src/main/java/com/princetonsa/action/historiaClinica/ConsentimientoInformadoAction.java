package com.princetonsa.action.historiaClinica;

import java.io.File;
import java.sql.Connection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.Listado;
import util.LogsAxioma;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadFileUpload;
import util.UtilidadSesion;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.historiaClinica.ConsentimientoInformadoForm;
import com.princetonsa.dto.odontologia.DtoConsentimientoInformadoOdonto;
import com.princetonsa.dto.odontologia.DtoPrograma;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.historiaClinica.ConsentimientoInformado;
import com.princetonsa.mundo.historiaClinica.MotivosNoConsentimientoInfoMundo;
import com.princetonsa.mundo.odontologia.Programa;


/**
 * @author Jhony Alexander Duque A.
 * jduque@princetonsa.com
 */

public class ConsentimientoInformadoAction extends Action
{
	Logger logger = Logger.getLogger(ConsentimientoInformadoAction.class);
	
	
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		Connection connection = null;
		try {	
			if (form instanceof ConsentimientoInformadoForm)
			{
				ConsentimientoInformadoForm forma = (ConsentimientoInformadoForm) form;
				ConsentimientoInformado mundo = new ConsentimientoInformado();
				String estado = forma.getEstado();

				logger.info("Consentimiento Informado Estado-------> "+estado);

				connection = UtilidadBD.abrirConexion();
				HttpSession session = request.getSession();
				UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());

				//paciente cargado en sesion 
				PersonaBasica paciente= (PersonaBasica)request.getSession().getAttribute("pacienteActivo");


				if (estado == null)
				{
					//	logger.warn("Estado no valido detro del flujo de ConsentimientoInformadoAction (null) ");	
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(connection);
					return mapping.findForward("paginaError");
				}

				else if(estado.equals("empezar"))
				{
					if(UtilidadTexto.getBoolean(ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(usuario.getCodigoInstitucionInt())))
					{
						forma.setTipoConsentimiento("");
						UtilidadBD.closeConnection(connection);
						return mapping.findForward("principal");
					}
					else
					{
						forma.setTipoConsentimiento("SERVICIOS");
						return accionEmpezarConsultaConsentimientoInformado(connection, forma, mapping,usuario,"empezar");		
					}						
				}
				else if (estado.equals("continuar")|| estado.equals("operacionTrue"))
				{					

					return accionEmpezarConsultaConsentimientoInformado(connection, forma, mapping,usuario,"empezar");					
				}
				else if (estado.equals("nuevo"))
				{
					UtilidadBD.closeConnection(connection);
					this.accionNuevoConsentimientoInformado(forma);
					UtilidadBD.closeConnection(connection);
					return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()), Integer.parseInt(forma.getConsentimientoInfMap("numRegistros").toString()), response, request, "consentimientoInformado.jsp", true);

				}
				else if (estado.equals("guardar"))
				{
					return this.accionGuardarRegistros(connection, forma, mapping, usuario);

				}
				else if (estado.equals("detalle"))
				{

					return this.accionEmpezarDetalle(connection, forma, mapping, usuario, estado);
				}
				else if (estado.equals("nuevoDetalle"))
				{
					UtilidadBD.closeConnection(connection);
					this.accionNuevodetalleConsentimientoInf(forma);
					UtilidadBD.closeConnection(connection);
					return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()),Integer.parseInt(forma.getDetalleConsentInfMap("numRegistros").toString()), response, request, "detalleConsentimientoInf.jsp",true);
				}
				else if (estado.equals("guardarDetalle")) 
				{	

					return this.accionGuardarDetalleRegistros(connection, forma, mapping, usuario,request);

				}else if (estado.equals("elimarDetalle"))
				{
					UtilidadBD.closeConnection(connection);
					return this.accionEliminarCampoDetalle(forma, request, response, mapping, usuario.getCodigoInstitucionInt());
				}

				else if (estado.equals("ordenar"))
				{
					this.accionOrdenarMapa(forma);
					UtilidadBD.closeConnection(connection);
					return mapping.findForward("principal");
				}
				else if (estado.equals("eliminarCI"))
				{
					UtilidadBD.closeConnection(connection);
					return this.accionEliminarCampo(forma, request, response, mapping, usuario.getCodigoInstitucionInt());
				}
				else if (estado.equals("empezarImpresion"))
				{
					ActionForward forwardPaginaErrores=accionValidacionesAcceso(paciente, mapping, request, connection);

					if(forwardPaginaErrores!=null)
						return forwardPaginaErrores;

					return this.accionEmpezarImprimirConsentInformado(connection, forma, mapping, paciente, estado,usuario);
				}
				else if (estado.equals("redireccion"))
				{
					UtilidadBD.closeConnection(connection);
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
				}
				else if (estado.equals("impresionDetalle"))
				{

					//logger.info("entro a impresionDetalle:::::::::::::");
					return this.accionEmpezarImprimirDetalle(connection, forma, mapping, usuario, estado);

				}
				else if (estado.equals("subirarchivo"))
				{
					//logger.info("entro a impresionDetalle:::::::::::::");
					return this.accionSubirArchivo(connection, forma, mapping, estado);

				}
				else if (estado.equals("guardarArchivo"))
				{
					//logger.info("entro a el estado guardarArchivo"+forma.getConsentimientoInfMap());
					return this.accionGurardarArchivo(connection, forma, mapping, request);				
				}

				//estado de busqueda de consentimientos informados 
				else if(estado.equals("buscarConsentimientoInf"))
				{
					accionBuscarConsentimientoInformado(connection,forma,usuario);
					UtilidadBD.closeConnection(connection);
					return mapping.findForward("busqueda");					
				}


				//estado para guardar la impresion de un consentimiento informado
				else if(estado.equals("guardarImpresion"))
				{					
					accionGuardarImpresionConsentimiento(connection,forma,usuario,paciente.getCodigoIngreso());
					UtilidadBD.closeConnection(connection);
					return mapping.findForward("busqueda");	
				}
				/*-------------------------------------------
				 * ESTADO ==> BUSCARFORMATOS
				 -------------------------------------------*/
				else 
					if(estado.equals("buscarformatos"))
					{					

						return this.accionBuscarFormato(connection, forma, mapping);	
					}
				/*-------------------------------------------
				 * ESTADO ==> ORDENARFORMATOS
				 -------------------------------------------*/
					else if (estado.equals("ordenarformatos"))
					{
						this.accionOrdenarMapaFormatos(forma);
						UtilidadBD.closeConnection(connection);
						return mapping.findForward("formatos");
					}
					else if (estado.equals("llenarhistorial"))
					{
						return this.llenarHistorial(connection, forma, mapping,usuario);	
					}
					else if(estado.equals("buscarConsentimientoInfOdontologia"))
					{
						forma.setMensaje(new ResultadoBoolean(false));
						forma.setProcesoExitoso(false);
						forma.setConsentimientoOdonto(ConsentimientoInformado.consultarInfoConsentimientoOdonto(forma.getCodigosPHP(),forma.getPlanTratamiento(),false));
						forma.setMotivos(MotivosNoConsentimientoInfoMundo.cosultarMotivos(false));
						UtilidadBD.closeConnection(connection);
						return mapping.findForward("consentimiento");
					}
					else if(estado.equals("guardarConsentimientoOdonto"))
					{
						this.guardarConsentimientoOdonto(connection,forma,usuario);
						UtilidadBD.closeConnection(connection);
						return mapping.findForward("consentimiento");
					}



			}
			return super.execute(mapping, form, request, response);
		} catch (Exception e) {
			Log4JManager.error(e);
			return null;
		}
		finally{
			UtilidadBD.closeConnection(connection);
		}
	}
	
	
	/**
	 * 
	 * @param connection 
	 * @param forma
	 * @param usuario 
	 */
	private void guardarConsentimientoOdonto(Connection connection, ConsentimientoInformadoForm forma, UsuarioBasico usuario) 
	{
		for(DtoConsentimientoInformadoOdonto dto:forma.getConsentimientoOdonto())
		{
			dto.setUsuarioModifica(usuario.getLoginUsuario());
			dto.setFechaModifica(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
			dto.setHoraModifica(UtilidadFecha.getHoraActual());
			dto.setCodigosPHPRelacionados(ConsentimientoInformado.obtenerProgramasHallazgoPiezasParaConsentimiento(connection,dto.getPlanTratamiento(),dto.getCodigoPrograma(),forma.getCodigoCita(),ValoresPorDefecto.getValidaPresupuestoOdoContratadoBoolean(usuario.getCodigoInstitucionInt())));
		}
		if(ConsentimientoInformado.guardarConsentimientoOdontologia(connection,forma.getConsentimientoOdonto(),forma.getCodigoCita()))
		{
			forma.setMensaje(new ResultadoBoolean(true,"Proceso Exitoso."));
			forma.setProcesoExitoso(true);
		}
		else
		{
			forma.setMensaje(new ResultadoBoolean(false,"No se pudo almacenar los registros. Por favor Verifique."));
			forma.setProcesoExitoso(false);
		}
	}


	public ActionForward llenarHistorial (Connection connection, ConsentimientoInformadoForm forma,ActionMapping mapping, UsuarioBasico usuario)
	{
		String tmp [] = forma.getIndeximpresion().split(",");
		boolean transacction = UtilidadBD.iniciarTransaccion(connection);
		for (int i=1;i<tmp.length;i++)
		{
			HashMap parametros = new HashMap ();
			parametros.put("ingreso",forma.getConsentimientoInfMap("ingreso_"+tmp[i]));
			parametros.put("codigoconsentimiento",forma.getConsentimientoInfMap("codigoconsentimiento_"+tmp[i]));		
			parametros.put("institucion",usuario.getCodigoInstitucionInt());
			parametros.put("descripcion",forma.getConsentimientoInfMap("descripcion_"+tmp[i]));
			parametros.put("nombrearchivo",forma.getConsentimientoInfMap("nombrearchivo_"+tmp[i]));
			parametros.put("servicio", forma.getConsentimientoInfMap("servicio_"+tmp[i]));
			parametros.put("usuarioimprime",usuario.getLoginUsuario());
			parametros.put("fechaimprime",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
			parametros.put("horaimprime",UtilidadFecha.getHoraActual());
			
			transacction = ConsentimientoInformado.insertarHistorialConsentimientoInf(connection, parametros);		
			
			if(transacction)
			{
				UtilidadBD.finalizarTransaccion(connection);			
				logger.info("----->INSERTO 100% CONSENTIMIENTO INFORMADO HISTORIAL");
			}
			else
			{
				UtilidadBD.abortarTransaccion(connection);
			}					
			
			
			
			
		}
		
		
	
		UtilidadBD.closeConnection(connection);
		return mapping.findForward("principal");
	}
	
	
	/**
	 * Metodo encargado de verificar si el archivo
	 * existe o no; si existe entonces enel key
	 * nombrearchivo va el nombre del archivo existente,
	 * de lo contrario va el nombre de la jsp de error 
	 * que indica que el archivo no existe. 
	 * @param forma
	 */
	public HashMap verificarExistenciaDoc (HashMap tmp)
	{
		for(int k=0;k<Integer.parseInt(tmp.get("numRegistros").toString());k++)
		{
			//se verifica si el archivo existe
			//logger.info("el archivo "+forma.getConsentimientoInfMap("nombrearchivo_"+k).toString()+" existe "+b);
			if (!UtilidadFileUpload.existeArchivo(tmp.get("nombrearchivo_"+k).toString()))
			{		
					//si el archivo no existe se hace los siguiente:
				
					//se le agrega un nuevo key al HashMap consentimientoinformado
					//que es "existe" con valor de "false"
					tmp.put("existe_"+k, "false");
					//en el key de nombre archivo se le pone el valor de
					//el nombre de la jsp de error de el archivo no existente.
					tmp.put("nombrearchivo_"+k, "mensajeErrorArchivoNoExiste.jsp");
			}
			else//se le agrega un nuevo key al HashMap consentimientoinformado
				//que es "existe" con valor de "true"
				tmp.put("existe_"+k, "true");
		}
		
		return tmp;
	}
	
	
	
	
	public ActionForward accionBuscarFormato (Connection connection,ConsentimientoInformadoForm forma,ActionMapping mapping)
	{
		HashMap parametros = new HashMap ();
		ConsentimientoInformado mundo = new ConsentimientoInformado();
		parametros.put("gruposervicio",forma.getConsentimientoInfMap("gruposervicio_"+forma.getIndexConsentInf()));
		
		forma.setFormatos(verificarExistenciaDoc(mundo.impresionCosentimientoinformadoXIngreso(connection, parametros)));
		
		logger.info("el valor del hashmap despues la consulta de accionBuscarFormato "+forma.getFormatos());
		
		
		
		UtilidadBD.closeConnection(connection);
		
		return mapping.findForward("formatos");
	}
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * validaciones de acceso
	 * @param paciente
	 * @param mapping
	 * @param request
	 * @param con
	 * @return
	 */
	protected ActionForward accionValidacionesAcceso(PersonaBasica paciente, 
													ActionMapping mapping, 
													HttpServletRequest request, 
													Connection con)
	{		
		if(paciente.getCodigoPersona()<1)
		{
		    return ComunAction.accionSalirCasoError(mapping, request, con, logger, "paciente null o sin  id", "errors.paciente.noCargado", true);
		}
		
		return null;
	}	
	
	
	
	/**
	 * Metodo utilizado para limpiar los datos de la dorma, ademas de cargar los datos ya existentes
	 * @param connection
	 * @param forma
	 * @param mapping
	 * @param institucion
	 * @return
	 */
	public ActionForward accionEmpezarConsultaConsentimientoInformado(Connection connection, ConsentimientoInformadoForm forma, ActionMapping mapping,UsuarioBasico usuario,String estado)
	{				
		forma.reset();
		HashMap parametros = new HashMap();
		forma.setEstado(estado);
		//logger.info("estadodespues::::::::::."+forma.getEstado());
		parametros.put("institucion",usuario.getCodigoInstitucionInt());
		if(forma.getTipoConsentimiento().equals("SERVICIOS"))
		{
			forma.setGrupoServicios(Utilidades.obtenerGrupoServicios(connection, parametros));
		}
		else if(forma.getTipoConsentimiento().equals("PROGRAMAS"))
		{
			forma.setProgramas(Programa.cargar(new DtoPrograma()));
		}
		parametros.put("tipoConsentimiento",forma.getTipoConsentimiento());
		//logger.info("GRUPO DE SERVICIOS:::::::"+forma.getGrupoServicios());
		forma.setConsentimientoInfMap(ConsentimientoInformado.consultaConsentimientoInformado(connection, parametros));
	//	logger.info("Forma:::::::::"+forma.getEstado());
		//logger.info("mapa de consentimiento informado al comensar "+forma.getConsentimientoInfMap());
		for (int i=0;i<Utilidades.convertirAEntero(forma.getConsentimientoInfMap("numRegistros")+"");i++)
			forma.setConsentimientoInfMap("eliminar_"+i, UtilidadValidacion.esConsentimientoInformadoUsado(connection, forma.getConsentimientoInfMap("codigo_"+i)+"", usuario.getCodigoInstitucionInt()));
		
		UtilidadBD.closeConnection(connection);
		return mapping.findForward("principal");		
	}
	
	
	/**
	 * metodo encargado de agregar nuevos datos a la forma
	 * @param forma
	 */
	public void accionNuevoConsentimientoInformado(ConsentimientoInformadoForm forma)
	{
		
		int pos = Integer.parseInt(forma.getConsentimientoInfMap("numRegistros").toString());		
		forma.setConsentimientoInfMap("descripcion_"+pos,"");
		forma.setConsentimientoInfMap("nombrearchivo_"+pos,"");
		forma.setConsentimientoInfMap("nombreoriginal_"+pos,"");
		forma.setConsentimientoInfMap("estabd_"+pos,ConstantesBD.acronimoNo);
		forma.setConsentimientoInfMap("numRegistros",(pos+1)+"");
		
	}
	
	/**
	 * metodo encargado de agregar nuevos datos a la forma detalle
	 * @param forma
	 */
	public void accionNuevodetalleConsentimientoInf(ConsentimientoInformadoForm forma)
	{
		
		int pos = Integer.parseInt(forma.getDetalleConsentInfMap("numRegistros").toString());	
				
		forma.setDetalleConsentInfMap("codigoconsent_"+pos,forma.getConsentimientoInfMap("codigo_"+forma.getIndexConsentInf()));
		forma.setDetalleConsentInfMap("codigogrupser_"+pos,"");
		forma.setDetalleConsentInfMap("programaodontologico_"+pos,"");
		
		forma.setDetalleConsentInfMap("estabd_"+pos,ConstantesBD.acronimoNo);
		forma.setDetalleConsentInfMap("numRegistros",(pos+1)+"");
		// logger.info("valor del haspmap despues de darle nuevo::::::::"+forma.getDetalleConsentInfMap());
	}
	
	
	
	
	/**
	 * metodo encargado del ordenamiento de la forma
	 * @param forma
	 */
	private void accionOrdenarMapa(ConsentimientoInformadoForm forma)
	{
		String[] indices = (String[])forma.getConsentimientoInfMap("INDICES_MAPA");
		int numReg = Integer.parseInt(forma.getConsentimientoInfMap("numRegistros")+"");
		forma.setConsentimientoInfMap(Listado.ordenarMapa(indices, forma.getPatronOrdenar(), forma.getUltimoPatronOrdenar(), forma.getConsentimientoInfMap(), numReg));
		forma.setUltimoPatronOrdenar(forma.getPatronOrdenar());
		forma.setConsentimientoInfMap("numRegistros",numReg+"");
		forma.setConsentimientoInfMap("INDICES_MAPA",indices);		
	}
	
	/**
	 * metodo encargado del ordenamiento de los formatos
	 * @param forma
	 */
	private void accionOrdenarMapaFormatos(ConsentimientoInformadoForm forma)
	{
		String[] indices = (String[])forma.getFormatos("INDICES_MAPA");
		int numReg = Integer.parseInt(forma.getFormatos("numRegistros")+"");
		forma.setFormatos(Listado.ordenarMapa(indices, forma.getPatronOrdenar(), forma.getUltimoPatronOrdenar(), forma.getFormatos(), numReg));
		forma.setUltimoPatronOrdenar(forma.getPatronOrdenar());
		forma.setFormatos("numRegistros",numReg+"");
		forma.setFormatos("INDICES_MAPA",indices);		
	}
	
	
	/**
	 * Elimina un Registro del HashMap de Consentimiento Informado, Adicciona el Registro Eliminado en el HashMap de Caonsentimiento Informado Eliminados
	 * @param forma
	 * @param request
	 * @param response
	 * @param mapping
	 * @return ActionForward
	 */
	private ActionForward accionEliminarCampo(ConsentimientoInformadoForm forma, HttpServletRequest request, HttpServletResponse response, ActionMapping mapping, int codigoInstitucion) 
	{
		//logger.info("Entra a accionEliminarCampo :::::::::::::::::::::::::::");
		int numRegMapEliminados=Integer.parseInt(forma.getConsentimientoInfEliminadoMap("numRegistros")+"");
		
		//logger.info("accionEliminarCampo y el numRegMapEliminados"+numRegMapEliminados);
		int ultimaPosMapa=(Integer.parseInt(forma.getConsentimientoInfMap("numRegistros")+"")-1);
		
		//poner la informacion en el otro mapa.
		String[] indices= (String[])forma.getConsentimientoInfMap("INDICES_MAPA");		
		
		for(int i=0;i<indices.length;i++)
		{ 
			//solo pasar al mapa los registros que son de BD
			if((forma. getConsentimientoInfMap("estabd_"+forma.getIndexEliminado())+"").trim().equals(ConstantesBD.acronimoSi))
			{
				forma.setConsentimientoInfEliminadoMap(indices[i]+""+numRegMapEliminados, forma.getConsentimientoInfMap(indices[i]+""+forma.getIndexEliminado()));
			}
		}		
		
		if((forma.getConsentimientoInfMap("estabd_"+forma.getIndexEliminado())+"").trim().equals(ConstantesBD.acronimoSi))
		{			
			forma.setConsentimientoInfEliminadoMap("numRegistros", (numRegMapEliminados+1));
		}
		
		//acomodar los registros del mapa en su nueva posicion
		for(int i=Integer.parseInt(forma.getIndexEliminado().toString());i<ultimaPosMapa;i++)
		{
			for(int j=0;j<indices.length;j++)
			{
				forma.setConsentimientoInfMap(indices[j]+""+i,forma.getConsentimientoInfMap(indices[j]+""+(i+1)));
			}
		}
		
		//ahora eliminamos el ultimo registro del mapa.
		for(int j=0;j<indices.length;j++)
		{
			forma.getConsentimientoInfMap().remove(indices[j]+""+ultimaPosMapa);
		}
		
		//ahora actualizamos el numero de registros en el mapa.
		forma.setConsentimientoInfMap("numRegistros",ultimaPosMapa);		
		return UtilidadSesion.redireccionar(forma.getLinkSiguiente(), ValoresPorDefecto.getMaxPageItemsInt(codigoInstitucion),Integer.parseInt(forma.getConsentimientoInfMap("numRegistros").toString()), response, request, "consentimientoInformado.jsp",Integer.parseInt(forma.getIndexEliminado().toString())==ultimaPosMapa);
	}
	
	
	/**
	 * Elimina un Registro del HashMap de Detalle Consentimiento Informado, Adicciona el Registro Eliminado en el HashMap de Caonsentimiento Informado Eliminados
	 * @param forma
	 * @param request
	 * @param response
	 * @param mapping
	 * @return ActionForward
	 */
	private ActionForward accionEliminarCampoDetalle (ConsentimientoInformadoForm forma, HttpServletRequest request, HttpServletResponse response, ActionMapping mapping, int codigoInstitucion) 
	{
		
		//logger.info("\n\n Entra a accionEliminarCampoDetalle :::::::::::::::::::::::::::"+forma.getDetalleConsentInfMap()+"\n\n");
		int numRegMapEliminados=Integer.parseInt(forma.getDetalleEliminadoMap("numRegistros")+"");
		
		// logger.info("numRegMapEliminados:::::"+numRegMapEliminados);
		int ultimaPosMapa=(Integer.parseInt(forma.getDetalleConsentInfMap("numRegistros")+"")-1);
		
		// logger.info("UltimaposMapa:::::::::."+ultimaPosMapa); 
		//poner la informacion en el otro mapa.
		String[] indices= (String[])forma.getDetalleConsentInfMap("INDICES_MAPA");		
		
		for(int i=0;i<indices.length;i++)
		{ 
			//solo pasar al mapa los registros que son de BD
			if((forma.getDetalleConsentInfMap("estabd_"+forma.getIndexEliminadoDetalle())+"").trim().equals(ConstantesBD.acronimoSi))
			{
				forma.setDetalleEliminadoMap(indices[i]+""+numRegMapEliminados, forma.getDetalleConsentInfMap(indices[i]+""+forma.getIndexEliminadoDetalle()));
			}
		}		
		
		if((forma.getDetalleConsentInfMap("estabd_"+forma.getIndexEliminadoDetalle())+"").trim().equals(ConstantesBD.acronimoSi))
		{			
			forma.setDetalleEliminadoMap("numRegistros", (numRegMapEliminados+1));
		}
		
		//acomodar los registros del mapa en su nueva posicion
		for(int i=Integer.parseInt(forma.getIndexEliminadoDetalle().toString());i<ultimaPosMapa;i++)
		{
			for(int j=0;j<indices.length;j++)
			{
				//logger.info("eliminado i > "+i+" j > "+j+" indice > "+indices[j]+" valor de HashMap"+forma.getDetalleConsentInfMap(indices[j]+""+(i+1)));
				forma.setDetalleConsentInfMap(indices[j]+""+i,forma.getDetalleConsentInfMap(indices[j]+""+(i+1)));
			}
		}
		
		//ahora eliminamos el ultimo registro del mapa.
		for(int j=0;j<indices.length;j++)
		{
			forma.getDetalleConsentInfMap().remove(indices[j]+""+ultimaPosMapa);
		}
		
		//ahora actualizamos el numero de registros en el mapa.
		forma.setDetalleConsentInfMap("numRegistros",ultimaPosMapa);
		//logger.info("\n\n saliendo accionEliminarCampoDetalle :::::::::::::::::::::::::::"+forma.getDetalleConsentInfMap()+"\n\n");		
		//logger.info("num registros a la hora de salir de eliminar campo"+forma.getDetalleConsentInfMap("numRegistros"));
		return UtilidadSesion.redireccionar(forma.getLinkSiguiente(), ValoresPorDefecto.getMaxPageItemsInt(codigoInstitucion),Integer.parseInt(forma.getDetalleConsentInfMap("numRegistros").toString()), response, request, "detalleConsentimientoInf.jsp",Integer.parseInt(forma.getIndexEliminadoDetalle().toString())==ultimaPosMapa);
	}
	
	
	/**
	 * Guarda, Modifica o Elimina un Registro en Cobertura
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param usuario
	 * @return ActionForward
	 * */
	private ActionForward accionGuardarRegistros(Connection connection, ConsentimientoInformadoForm forma, ActionMapping mapping,UsuarioBasico usuario)
	{
		boolean transacction = UtilidadBD.iniciarTransaccion(connection);
		String estadoEliminar="ninguno";
		ConsentimientoInformado mundo = new ConsentimientoInformado ();
		logger.info("valor del hasmap al entar a accionGuardarRegistros "+forma.getConsentimientoInfMap());
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getConsentimientoInfEliminadoMap("numRegistros")+"");i++)
		{
			//logger.info("Entro a eliminar BD :::::::::::::contador I...>"+i);
			//logger.info("Hashmap:::::::::::"+forma.getConsentimientoInfEliminadoMap());
			if(eliminarBD(connection, forma, i));			
			{				
				
				this.generarlog(forma, new HashMap() , usuario, true, i);
				 estadoEliminar="operacionTrue";
				 transacction=true;						 
			}
		}		
		
		for(int i=0;i<Integer.parseInt(forma.getConsentimientoInfMap("numRegistros")+"");i++)
		{			
			//modificar			
			
			if((forma.getConsentimientoInfMap("estabd_"+i)+"").trim().equals(ConstantesBD.acronimoSi))
			{
				if(this.existeModificacion(connection,forma,mundo,i))
				{	
												
					/*logger.info("HashMap:::::::::"+forma.getConsentimientoInfMap());
					logger.info("subindice:::::..."+i);
					logger.info("ENTRO A MODIFICAR::::::::.."+forma.getConsentimientoInfMap("codigo_"+i));*/
					HashMap tmp = new HashMap();
					
					
					tmp.put("institucion",forma.getConsentimientoInfMap("institucion_"+i));
					tmp.put("codigo",forma.getConsentimientoInfMap("codigo_"+i));
								

					
					tmp = mundo.consultaConsentimientoInformado(connection,tmp);
					
					logger.info("EL VALOR DE TMP ES "+tmp);
					transacction = modificarBD(connection, forma, i);
					this.generarlog(forma, tmp, usuario, false, i);
					estadoEliminar="operacionTrue";
				}	
				
			}
			
			//insertar			
			else if((forma.getConsentimientoInfMap("estabd_"+i)+"").trim().equals(ConstantesBD.acronimoNo))
			{
				//logger.info("Entro a insertar consentimiento informado "+forma.getConsentimientoInfMap()); 
				 transacction = insertarBD(connection, forma, i, usuario);
				 
				 estadoEliminar="operacionTrue";
			}
			
			// logger.info("\n\n valor i >> "+i);
		}
		
		if(transacction)
		{
			UtilidadBD.finalizarTransaccion(connection);			
			logger.info("----->INSERTO 100% CONSENTIMIENTO INFORMADO");
		}
		else
		{
			UtilidadBD.abortarTransaccion(connection);
		}
		
		
		
		return this.accionEmpezarConsultaConsentimientoInformado(connection, forma, mapping, usuario,estadoEliminar);		 		
	}
	
	
	/**
	 * Guarda, Modifica o Elimina un Registro en Cobertura
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param usuario
	 * @param request 
	 * @return ActionForward
	 * */
	private ActionForward accionGuardarDetalleRegistros(Connection connection, ConsentimientoInformadoForm forma, ActionMapping mapping,UsuarioBasico usuario, HttpServletRequest request)
	{
		//enviar los errores a l apagina de detalle, puesto que normalmente el la envia el principal
		ActionErrors errores = new ActionErrors();
		int numReg=Integer.parseInt(forma.getDetalleConsentInfMap("numRegistros")+"");
		//validacion
		for (int i=0;i<numReg;i++)
		{
			if(forma.getTipoConsentimiento().equals("SERVICIOS"))
			{
				if(forma.getDetalleConsentInfMap("codigogrupser_"+i).toString().trim().equals(""))
					errores.add("seleccion",new ActionMessage("errors.required","La Seleccion del registro "+(i+1)));
			}
			else
			{
				if(forma.getDetalleConsentInfMap("programaodontologico_"+i).toString().trim().equals(""))
					errores.add("seleccion",new ActionMessage("errors.required","La Seleccion del registro "+(i+1)));
			}
			
			for(int k=i+1;k<numReg;k++)
			{
				if(forma.getTipoConsentimiento().equals("SERVICIOS"))
				{
					if (forma.getDetalleConsentInfMap("codigogrupser_"+i).toString().trim().equals((forma.getDetalleConsentInfMap("codigogrupser_"+k).toString().trim())))
						errores.add("seleccion",new ActionMessage("errors.yaExiste","El Codigo del registro "+(i+1)));
				}
				else
				{
					if (forma.getDetalleConsentInfMap("programaodontologico_"+i).toString().trim().equals((forma.getDetalleConsentInfMap("programaodontologico_"+k).toString().trim())))
						errores.add("seleccion",new ActionMessage("errors.yaExiste","El Codigo del registro "+(i+1)));
				}
				
			}
		}
		//pregunta so errores esta vacio
		if (!errores.isEmpty())
		{
			saveErrors(request, errores);
			UtilidadBD.closeConnection(connection);
			return mapping.findForward("detalle") ;
		}
		
		
		// logger.info("VALOR DEL DETALLE EN GUARDAR >>> "+forma.getDetalleConsentInfMap());
		
		boolean transacction = UtilidadBD.iniciarTransaccion(connection);
		String estadoEliminar="ninguno";
		ConsentimientoInformado mundo = new ConsentimientoInformado ();
		
		
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getDetalleEliminadoMap("numRegistros")+"");i++)
		{	
			if(eliminarDetalleBD(connection, forma, i));			
			{				
				//this.generarlog(forma, new HashMap(), usuario, true, i);
				// logger.info("\n\n YA BOREEEEE:::::::::::"+forma.getDetalleConsentInfMap());
				 estadoEliminar="operacionTrueDetalle";
				 transacction=true;						 
			}
		}		
		
		
		for(int i=0;i<Integer.parseInt(forma.getDetalleConsentInfMap("numRegistros")+"");i++)
		{
			
			//modificar			
			if((forma.getDetalleConsentInfMap("estabd_"+i)+"").trim().equals(ConstantesBD.acronimoSi))
			{	
				//logger.info("antes de existe modificacion:::::::::::"+forma.getDetalleConsentInfMap());
				if(this.existeModificacionDetalle(connection,forma,mundo,i))
				{					
					// logger.info("ENTRO A MODIFICAR::::::::.. i>> "+i);
					
					transacction = modificarDetalleBD(connection, forma, i);
					estadoEliminar="operacionTrueDetalle";
				}	
				
			}
			
			// logger.info("\n\n valor antes de insertar i >> "+i+" >> "+forma.getDetalleConsentInfMap("estabd_"+i));
			
			//insertar
			if((forma.getDetalleConsentInfMap("estabd_"+i)+"").trim().equals(ConstantesBD.acronimoNo))
			{
				// logger.info("ENTRO A INSERTART:::::: >> i "+ i); 
				transacction = insertarDetalleBD(connection, forma, i, usuario);				 
				estadoEliminar="operacionTrueDetalle";
			}	
			
			
		}
		
		if(transacction)
		{
			UtilidadBD.finalizarTransaccion(connection);			
			logger.info("----->INSERTO 100% CONSENTIMIENTO INFORMADO");
		}
		else
		{
			UtilidadBD.abortarTransaccion(connection);
		}
		
		 
		return this.accionEmpezarDetalle(connection, forma, mapping, usuario, estadoEliminar);
	}
	
	/**
	 * verifica si el registro se ha modificado
	 * @param connection
	 * @param forma
	 * @param mundo
	 * @param int pos
	 * */
	private boolean existeModificacion(Connection connection,ConsentimientoInformadoForm forma,ConsentimientoInformado mundo, int pos)
	{
		HashMap temp = new HashMap();
		
		//iniciamos los datos para la consulta
		temp.put("institucion",forma.getConsentimientoInfMap("institucion_"+pos));
		temp.put("codigo",forma.getConsentimientoInfMap("codigo_"+pos));
					

		
		temp = mundo.consultaConsentimientoInformado(connection,temp);
		
		// logger.info("HashMap de consentimiento::::::"+temp);
		String[] indices = (String[])forma.getConsentimientoInfMap("INDICES_MAPA");		
			
		for(int i=0;i<indices.length;i++)
		{		
			if(temp.containsKey(indices[i]+"0")&&forma.getConsentimientoInfMap().containsKey(indices[i]+pos))
			{				
				if(!((temp.get(indices[i]+"0")+"").trim().equals((forma.getConsentimientoInfMap(indices[i]+pos)).toString().trim())))
				{
					//this.generarlog(forma, temp, usuario, false, pos);
					
					return true;
					
				}				
			}
		}	
		
		return false;		
	}
	
	/**
	 * verifica si el registro se ha modificado
	 * @param connection
	 * @param forma
	 * @param mundo
	 * @param int pos
	 * */
	private boolean existeModificacionDetalle(Connection connection,ConsentimientoInformadoForm forma,ConsentimientoInformado mundo, int pos)
	{
		HashMap temp = new HashMap();		
		//iniciamos los datos para la consulta
		temp.put("institucion",forma.getDetalleConsentInfMap("institucion_"+pos));
		temp.put("codigoconsent",forma.getDetalleConsentInfMap("codigoconsent_"+pos));
		temp.put("codigogrupser", forma.getDetalleConsentInfMap("codigogrupserold_"+pos));
		temp.put("programaodontologico", forma.getDetalleConsentInfMap("programaodontologicoold_"+pos));
		
					
		//logger.info("valor del temp en modificar >> "+temp+"   posicion::"+pos);
		
		//if (forma.getDetalleConsentInfMap(""))

		temp.put("tipoConsentimiento",forma.getTipoConsentimiento());
		temp = mundo.consultaDetalleConsentimientoInformado(connection, temp);
		
		// logger.info("HashMap de consentimiento::::::"+temp);		
				
		if(!temp.get("numRegistros").equals("0"))
		{
			if(forma.getTipoConsentimiento().equals("SERVICIOS"))
			{
				if(!((temp.get("codigogrupser_0")+"").trim().equals((forma.getDetalleConsentInfMap(("codigogrupser_"+pos)).toString().trim()))))
				{			
					//this.generarlog(forma, temp, usuario, false, pos);
					// logger.info("SI fue modificado::::::::::::::");
					// logger.info("\n\n Valor de hashmap temp "+temp+"\n\n");
					// logger.info("\n\n Valor de hashmap forma "+forma.getDetalleConsentInfMap()+"\n\n");
					return true;			
				}
			}
			else
			{
				if(!((temp.get("programaodontologico_0")+"").trim().equals((forma.getDetalleConsentInfMap(("programaodontologico_"+pos)).toString().trim()))))
				{			
					//this.generarlog(forma, temp, usuario, false, pos);
					// logger.info("SI fue modificado::::::::::::::");
					// logger.info("\n\n Valor de hashmap temp "+temp+"\n\n");
					// logger.info("\n\n Valor de hashmap forma "+forma.getDetalleConsentInfMap()+"\n\n");
					return true;			
				}
				
			}
			
			// logger.info("NO fue modificado::::::::::::::");
		}	
		return false;		
	}
	
	
	/**
	 * Este metodo se encarga de insertar en la BD los nuevos datos; pordentro el crea un HashMap
	 * con los nuevos datos que se van a ingresar a la BD
	 * @param connection
	 * @param forma
	 * @param i
	 * @param usuario
	 * @return
	 */
	public boolean insertarBD (Connection connection, ConsentimientoInformadoForm forma,int i,UsuarioBasico usuario)
	{
		 HashMap temp = new HashMap();
		 ConsentimientoInformado mundo = new ConsentimientoInformado();
		logger.info("\n\n entro a insertarDB"+forma.getConsentimientoInfMap());	
		 //se inicializa el hashmap para que el sql lo pueda entender
			 temp.put("institucion",usuario.getCodigoInstitucionInt());
			 temp.put("descripcion",forma.getConsentimientoInfMap("descripcion_"+i));
			 temp.put("nombrearchivo",forma.getConsentimientoInfMap("nombrearchivo_"+i));
			 temp.put("nombreoriginal",forma.getConsentimientoInfMap("nombreoriginal_"+i));
			 temp.put("usuariomodifica",usuario.getLoginUsuario());
			 temp.put("fechamodifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
			 temp.put("horamodifica",UtilidadFecha.getHoraActual());
			 //logger.info("\n\n saliendo de insertarDB el valor de temp"+temp);
		return mundo.insertarConsentimientoInformado(connection,temp) ;
	}
	
	
	/**
	 * Este metodo se encarga de insertar en la BD los nuevos datos; pordentro el crea un HashMap
	 * con los nuevos datos que se van a ingresar a la BD
	 * @param connection
	 * @param forma
	 * @param i
	 * @param usuario
	 * @return
	 */
	public boolean insertarDetalleBD (Connection connection, ConsentimientoInformadoForm forma,int i,UsuarioBasico usuario)
	{
		// logger.info("HashMap Insertar Detalle 569::::::::::::::"+forma.getDetalleConsentInfMap()+" >> "+i);
		
		HashMap temp = new HashMap();
		 ConsentimientoInformado mundo = new ConsentimientoInformado();
			//se inicializa el hashmap para que el sql lo pueda entender
					 
		 	 temp.put("institucion",usuario.getCodigoInstitucionInt());
			 temp.put("codigoconsent",forma.getDetalleConsentInfMap("codigoconsent_"+i));
			 temp.put("codigogrupser",forma.getDetalleConsentInfMap("codigogrupser_"+i));
			 temp.put("programaodontologico",forma.getDetalleConsentInfMap("programaodontologico_"+i));
			 
			 temp.put("usuariomodifica",usuario.getLoginUsuario());
			 temp.put("fechamodifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
			 temp.put("horamodifica",UtilidadFecha.getHoraActual());
			
		return mundo.insertarDetalleConsentimientoInformado(connection,temp) ;
	}
	
	
	/**
	 * Carga los datos a eliminar en la BD; el parametro "i"
	 * es el encargado de manejar la posicion dentro del HashMap
	 * @param connection
	 * @param forma
	 * @return
	 */
	public boolean eliminarBD (Connection connection, ConsentimientoInformadoForm forma,int i)
	{
		//logger.info(":::::::::ENTRO A ELIMINARBD::::::::::::");
		HashMap temp = new HashMap ();
		ConsentimientoInformado mundo = new ConsentimientoInformado();
        
		//iniciamos los datos para la consulta
		temp.put("institucion",forma.getConsentimientoInfEliminadoMap("institucion_"+i));
		temp.put("codigo",forma.getConsentimientoInfEliminadoMap("codigo_"+i));
		temp.put("codigoconsent",forma.getConsentimientoInfEliminadoMap("codigo_"+i));
		
		mundo.eliminardetalleConsentimientoInformado(connection, temp);
		
		//aqui organizamos el archivo relacionado a los datos que van a ser borrados
		//para tambien borrarlos
		File arc=new File(ValoresPorDefecto.getFilePath()+forma.getConsentimientoInfEliminadoMap("nombrearchivo_"+i));
		arc.delete();
		
		return mundo.eliminarConsentimientoInformado(connection, temp);
	}
	
	
	/**
	 * Carga los datos a eliminar en la BD; el parametro "i"
	 * es el encargado de manejar la posicion dentro del HashMap
	 * @param connection
	 * @param forma
	 * @return
	 */
	public boolean eliminarDetalleBD (Connection connection, ConsentimientoInformadoForm forma,int i)
	{
		// logger.info(":::::::::ENTRO A ELIMINARDETALLEBD::::::::::::");
		HashMap temp = new HashMap ();
		ConsentimientoInformado mundo = new ConsentimientoInformado();
        
		//iniciamos los datos para la consulta
		temp.put("codigoconsent",forma.getDetalleEliminadoMap("codigoconsent_"+i));
		temp.put("institucion",forma.getDetalleEliminadoMap("institucion_"+i));
		temp.put("codigopk", forma.getDetalleEliminadoMap("codigopk_"+i));
		// logger.info("\n\n valor de temp a eliminar es:: "+temp);
		return mundo.eliminardetalleConsentimientoInformado(connection, temp);
	}
	
	/**
	 * este metodo se encarga de guardar los cambios en la BD; el parametro "i"
	 * es el encargado de manejar la posicion dentro del HashMap
	 * @param connection
	 * @param forma
	 * @param i 
	 * @return
	 */
	public boolean modificarBD (Connection connection,ConsentimientoInformadoForm forma,int i )
	{
		logger.info("valor de haspmap al entrar"+forma.getConsentimientoInfMap());
		HashMap temp = new HashMap ();
		ConsentimientoInformado mundo = new ConsentimientoInformado();
		temp.put("institucion",forma.getConsentimientoInfMap("institucion_"+i));
		temp.put("codigo",forma.getConsentimientoInfMap("codigo_"+i));
		temp.put("descripcion",forma.getConsentimientoInfMap("descripcion_"+i));
		temp.put("nombreoriginal",forma.getConsentimientoInfMap("nombreoriginal_"+i));
		temp.put("nombrearchivo",forma.getConsentimientoInfMap("nombrearchivo_"+i));
		temp.put("usuariomodifica",forma.getConsentimientoInfMap("usuariomodifica_"+i));
		temp.put("fechamodifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		temp.put("horamodifica",UtilidadFecha.getHoraActual());
		
		return mundo.modificarConsentimientoInformado(connection, temp);
	}
	
	
	/**
	 * este metodo se encarga de guardar los cambios en la BD de la tabla detalle; el parametro "i"
	 * es el encargado de manejar la posicion dentro del HashMap
	 * @param connection
	 * @param forma
	 * @param i 
	 * @return
	 */
	public boolean modificarDetalleBD (Connection connection,ConsentimientoInformadoForm forma,int i )
	{
		// logger.info("Entro a modificar detalle:::::::"+forma.getConsentimientoInfMap());
		HashMap temp = new HashMap ();
		ConsentimientoInformado mundo = new ConsentimientoInformado();
		
		temp.put("institucion",forma.getDetalleConsentInfMap("institucion_"+i));
		temp.put("codigoconsent",forma.getDetalleConsentInfMap("codigoconsent_"+i));
		temp.put("codigogrupserold",forma.getDetalleConsentInfMap("codigogrupserold_"+i));
		temp.put("codigogrupser",forma.getDetalleConsentInfMap("codigogrupser_"+i));
		temp.put("usuariomodifica",forma.getDetalleConsentInfMap("usuariomodifica_"+i));
		temp.put("fechamodifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		temp.put("horamodifica",UtilidadFecha.getHoraActual());
		temp.put("programaodontologicoold",forma.getDetalleConsentInfMap("programaodontologicoold_"+i));
		temp.put("programaodontologico",forma.getDetalleConsentInfMap("programaodontologico_"+i));
		temp.put("codigopk",forma.getDetalleConsentInfMap("codigopk_"+i));
		
		return mundo.modificarDetalleConsentimientoInf(connection, temp);
	}
	
	
	/**
	 * Metodo encargado de cargar los datos en el HashMap del detalle
	 * @param connection
	 * @param forma
	 * @param mapping
	 * @param institucion
	 * @return
	 */
	public ActionForward accionEmpezarDetalle(Connection connection, ConsentimientoInformadoForm forma, ActionMapping mapping,UsuarioBasico usuario,String estado)
	{				
		HashMap parametros = new HashMap();
		//logger.info("estadodespues::::::::::."+forma.getEstado());
		forma.setEstado(estado);
		parametros.put("institucion",usuario.getCodigoInstitucionInt());
		parametros.put("codigoconsent", forma.getConsentimientoInfMap("codigo_"+forma.getIndexConsentInf()));
		

		parametros.put("tipoConsentimiento",forma.getTipoConsentimiento());
		
		forma.setDetalleConsentInfMap(ConsentimientoInformado.consultaDetalleConsentimientoInformado(connection, parametros));  
	    // logger.info("Forma:::::::::"+forma.getDetalleConsentInfMap());
		UtilidadBD.closeConnection(connection);
		return mapping.findForward("detalle");
	}
	
	/**
	 * General el Documento Log 
	 * @param forma
	 * @param HashMap temp
	 * @param UsuarioBasico usuario
	 * @param isEliminacion
	 * @param pos
	 * */
	private void generarlog(ConsentimientoInformadoForm forma, HashMap mapaConsentInfTemp, UsuarioBasico usuario, boolean isEliminacion ,int pos )
	{
		String log = "";
		int tipoLog=0;
		String[] indices= (String[])forma.getConsentimientoInfMap("INDICES_MAPA");
		if(isEliminacion)
		{
			log = 		 "\n   ============REGISTRO ELIMINADO=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ "+(indices[i].trim().equals("acronimotipo_")?ValoresPorDefecto.getIntegridadDominio(forma.getConsentimientoInfEliminadoMap(indices[i]+""+pos)+""):forma.getConsentimientoInfEliminadoMap(indices[i]+""+pos))+" ]";
			}
			log+= "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogEliminacion;
		}
		else
		{
			log = 		 "\n   ============INFORMACION ORIGINAL=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ " +(indices[i].trim().equals("acronimotipo_")?ValoresPorDefecto.getIntegridadDominio(mapaConsentInfTemp.get(indices[i]+"0")+""):mapaConsentInfTemp.get(indices[i]+"0")) + " ]";
			}
			log += 		 "\n   ============INFORMACION MODIFICADA=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ " +(indices[i].trim().equals("acronimotipo_")?ValoresPorDefecto.getIntegridadDominio(forma.getConsentimientoInfMap(indices[i]+""+pos)+""):forma.getConsentimientoInfMap(indices[i]+""+pos)) +" ]";
			}
			log+= "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogModificacion;
		}
		LogsAxioma.enviarLog(ConstantesBD.logConsentimientoInformadoCodigo,log,tipoLog,usuario.getLoginUsuario());	
	}	
	
	
	/**
	 * Metodo utilizado para limpiar los datos de la forma, ademas de cargar los datos ya existentes
	 * @param connection
	 * @param forma
	 * @param mapping
	 * @param institucion
	 * @return
	 */
	public ActionForward accionEmpezarImprimirConsentInformado(Connection connection, ConsentimientoInformadoForm forma, ActionMapping mapping,PersonaBasica paciente,String estado,UsuarioBasico usuario)
	{				
		//logger.info("\n\n VALOR DE CONSENTIMIENTO INFORMADO "+forma.getConsentimientoInfMap()); 
			
		forma.reset();
		HashMap parametros = new HashMap();
		forma.setEstado(estado);
		
		//logger.info("estadodespues::::::::::."+forma.getEstado());
		parametros.put("institucion",usuario.getCodigoInstitucionInt());
		
	
		parametros.put("ingreso",paciente.getCodigoIngreso());
		
		forma.setConsentimientoInfMap(ConsentimientoInformado.consultaConsentimientoInformadoXingreso(connection, parametros));
		//VerificarExistenciaDoc(forma);
		logger.info("el valor de imprimir ocnsentimiento informado "+forma.getConsentimientoInfMap());
		
		UtilidadBD.closeConnection(connection);
		return mapping.findForward("principal");
	}
	
	
	
	
	
	/**
	 * Metodo encargado de cargar los datos en el HashMap del detalle
	 * @param connection
	 * @param forma
	 * @param mapping
	 * @param institucion
	 * @return
	 */
	public ActionForward accionEmpezarImprimirDetalle(Connection connection, ConsentimientoInformadoForm forma, ActionMapping mapping,UsuarioBasico usuario,String estado)
	{				
		HashMap parametros = new HashMap();
		//logger.info("estadodespues::::::::::."+forma.getEstado());
		forma.setEstado(estado);
		parametros.put("institucion",usuario.getCodigoInstitucionInt());
		parametros.put("codigoconsent", forma.getConsentimientoInfMap("codigo_"+forma.getIndexDetalle()));

		parametros.put("tipoConsentimiento",forma.getTipoConsentimiento());
		
		forma.setDetalleConsentInfMap(ConsentimientoInformado.consultaDetalleConsentimientoInformado(connection, parametros));  
	    logger.info("Forma patojito:::::::::"+forma.getDetalleConsentInfMap());
	    UtilidadBD.closeConnection(connection);
		return mapping.findForward("detalle");
	}
	
	public ActionForward accionSubirArchivo(Connection connection, ConsentimientoInformadoForm forma, ActionMapping mapping,String estado)
	{				
		
	     //logger.info("\n\n************************************nentro a accionSubirArchibo********************************");
		//logger.info("****************  VALOR FORMA"+forma.getConsentimientoInfMap());
		forma.setEstado(estado);
		UtilidadBD.closeConnection(connection);
		
		return mapping.findForward("archivo");
	}
	
	
	
	public ActionForward accionGurardarArchivo (Connection connection,ConsentimientoInformadoForm forma,ActionMapping mapping,HttpServletRequest request)
	{
		logger.info("\n\n************************************nentro a accionSubirArchibo********************************");
		logger.info("****************  VALOR FORMA"+forma.getConsentimientoInfMap());
		logger.info("****************VALOR DE GETINDEXCONSENTIMIENTOINF" +forma.getIndexConsentInf());
		logger.info("****************  SIZE ARCHIVO "+forma.getArchivo().getFileSize());
		logger.info("****************  NOMBRE ARCHIVO "+forma.getArchivo().getFileName());
		ActionErrors errores = new ActionErrors();
		
			String nombre = forma.getArchivo().getFileName();
			if(forma.getArchivo().getFileSize()==0)
					errores.add("file",new ActionMessage("errors.required","El archivo  "));
			else
				if (!(nombre.endsWith("pdf")||(nombre.endsWith("PDF"))))
					errores.add("seleccion",new ActionMessage("errors.noExtension","pdf"));
			
		//pregunta so errores esta vacio
		if (!errores.isEmpty())
		{
			saveErrors(request, errores);
			
			UtilidadBD.closeConnection(connection);
		
			return mapping.findForward("archivo") ;
		}
		forma.setEstado("operacionTrue");
		
		//logger.info("entro a accionGurardarArchivo***********************"+forma.getConsentimientoInfMap());
		//logger.info("el valor getIndexConsentInf ::::::::::::::::::::::::::"+forma.getIndexConsentInf());
		//se toma el nombre original del archivo
		String nombreOriginal=forma.getArchivo().getFileName();
		//se organoza el nuevo nombre para el archivo
		
		if (!forma.getConsentimientoInfMap("estabd_"+forma.getIndexConsentInf()).toString().equals(ConstantesBD.acronimoSi))
			if(forma.getConsentimientoInfMap("numRegistros").equals("1")&& UtilidadBD.obtenerUltimoValorSecuencia(connection, "seq_consentimientoinformado")==1 )
			{
				
				forma.setConsentimientoInfMap("codigo_"+forma.getIndexConsentInf(),(UtilidadBD.obtenerUltimoValorSecuencia(connection, "seq_consentimientoinformado")));
			}
			else{
				/*
				 *  MT 6445:Para Oracle no se puede obtener el CURRVAL de la secuencia ya que no ha sido utilizado el NEXTVAL
				 *  durante la sesiOn. Ademas por la logica del codigo la intencion es obtener el valor siguiente de la secuencia
				 */  
              //forma.setConsentimientoInfMap("codigo_"+forma.getIndexConsentInf(),(UtilidadBD.obtenerUltimoValorSecuencia(connection, "seq_consentimientoinformado"))+1);
			  forma.setConsentimientoInfMap("codigo_"+forma.getIndexConsentInf(),(UtilidadBD.obtenerSiguienteValorSecuencia(connection, "seq_consentimientoinformado")+""));
			}		
			
		String nombreArchivo = "Consentimiento_"+forma.getConsentimientoInfMap("codigo_"+Integer.parseInt(forma.getIndexConsentInf().toString()))+"_"+UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())+".pdf";
		//se envia a guardar el archivo con el nuevo nombre que va tener
		
		forma.setConsentimientoInfMap("nombreoriginal_"+forma.getIndexConsentInf(),nombreOriginal);
		forma.setConsentimientoInfMap("nombrearchivo_"+forma.getIndexConsentInf(),nombreArchivo);
		UtilidadFileUpload.guadarArchivo(forma.getArchivo(),nombreArchivo);
		//logger.info("SALIENDO DE accionGurardarArchivo LOS VALORES SON**********  "+forma.getConsentimientoInfMap());
		//logger.info("el valor getIndexConsentInf::::::::::::::::::::::::::"+forma.getIndexConsentInf());
		UtilidadBD.closeConnection(connection);
		return mapping.findForward("archivo");
	}
	
	
	
	//***************************************************
	//** Metodos Busqueda Consentimiento Informado
	//***************************************************
	
	/**
	 * Busca los consentimientos informados relacionados
	 * al grupo de servicios de un servicio
	 * @param Connection connection 
	 * @param ConsentimientoInformadoForm forma
	 * @param UsuarioBasico usuario
	 * */
	public void accionBuscarConsentimientoInformado(Connection connection,ConsentimientoInformadoForm forma, UsuarioBasico usuario)
	{
		forma.resetBusqueda();
		HashMap parametros = new HashMap();
		int numRegistros;
		
		if(forma.getCodigoServicioBusqueda()!=null&&!forma.getCodigoServicioBusqueda().equals(""))
		{
			
			parametros.put("servicio",forma.getCodigoServicioBusqueda());
			parametros.put("codigoPrograma",forma.getCodigoPrograma());
			parametros.put("institucion",usuario.getCodigoInstitucion());			
			//este tiene key nombrearchivo
			forma.setListadoConsentimientoInfMap(verificarExistenciaDoc(ConsentimientoInformado.buscarConsentimientoInfServicio(connection, parametros)));
			
			parametros = Utilidades.consultarInformacionServicio(connection,forma.getCodigoServicioBusqueda(), usuario.getCodigoInstitucionInt());
			
			logger.info("valor de listado >> "+forma.getListadoConsentimientoInfMap());
			
			//captura la informacion del servicio
			numRegistros = Integer.parseInt(parametros.get("numRegistros").toString());
			if(numRegistros > 1)
			{
				for(int i = 0 ; i <numRegistros; i++)
				{
					if(parametros.get("codigopropietario_"+i).toString().equals(ConstantesBD.codigoTarifarioCups))
					{
						forma.setListadoConsentimientoInfMap("nombreservicio",forma.getCodigoServicioBusqueda()+" "+parametros.get("nombre_"+i).toString().toUpperCase());					
						forma.setListadoConsentimientoInfMap("nombregruposervicios",parametros.get("gruposervicio_"+i).toString()+" "+Utilidades.getNombreGrupoServicios(connection,Integer.parseInt(parametros.get("gruposervicio_"+i).toString())));
					}
				}
			}
			else if(numRegistros == 1)
			{
				forma.setListadoConsentimientoInfMap("nombreservicio",forma.getCodigoServicioBusqueda()+" "+parametros.get("nombre").toString().toUpperCase());			
				forma.setListadoConsentimientoInfMap("nombregruposervicios",parametros.get("gruposervicio").toString()+" "+Utilidades.getNombreGrupoServicios(connection,Integer.parseInt(parametros.get("gruposervicio").toString())).toString().toUpperCase());			
			}	
			
		}
		else if(forma.getCodigoPrograma()!=null&&!forma.getCodigoPrograma().equals(""))
		{
			
			parametros.put("servicio",forma.getCodigoServicioBusqueda());
			parametros.put("codigoPrograma",forma.getCodigoPrograma());
			parametros.put("institucion",usuario.getCodigoInstitucion());			
			//este tiene key nombrearchivo
			forma.setListadoConsentimientoInfMap(verificarExistenciaDoc(ConsentimientoInformado.buscarConsentimientoInfServicio(connection, parametros)));
			
			
		}
		
					
	}
	
	
	/**
	 * Guarda el registro que indica que sea imprimido un consentimiento informado
	 * @param Connection con
	 * @param ConsentimientoInformadoForm forma
	 * @param UsuarioBasico usuario
	 * */
	public void accionGuardarImpresionConsentimiento(Connection connection, ConsentimientoInformadoForm forma, UsuarioBasico usuario,int ingreso)
	{
		boolean transacction = UtilidadBD.iniciarTransaccion(connection);
		HashMap parametros = new HashMap();
		
		parametros.put("ingreso",ingreso);
		if(forma.getCodigoServicioBusqueda()!=null&&!forma.getCodigoServicioBusqueda().isEmpty())
		{
			parametros.put("servicio",forma.getCodigoServicioBusqueda().toString());
			parametros.put("codigoPrograma","");
			parametros.put("planTratamiento", "");
		}
		else if(forma.getCodigoPrograma()!=null&&!forma.getCodigoPrograma().isEmpty())
		{
			parametros.put("servicio","");
			parametros.put("codigoPrograma",forma.getCodigoPrograma());
			parametros.put("planTratamiento", forma.getPlanTratamiento());
		}
		parametros.put("codigoconsentimiento",forma.getListadoConsentimientoInfMap("codigo_"+forma.getIndexListadoConsentimientoInf()));		
		parametros.put("institucion",forma.getListadoConsentimientoInfMap("institucion_"+forma.getIndexListadoConsentimientoInf()));
		parametros.put("descripcion",forma.getListadoConsentimientoInfMap("descripcion_"+forma.getIndexListadoConsentimientoInf()));
		parametros.put("nombrearchivo",forma.getListadoConsentimientoInfMap("nombrearchivo_"+forma.getIndexListadoConsentimientoInf()));
		
		parametros.put("usuarioimprime",usuario.getLoginUsuario());
		parametros.put("fechaimprime",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		parametros.put("horaimprime",UtilidadFecha.getHoraActual());
		
		transacction = ConsentimientoInformado.insertarHistorialConsentimientoInf(connection, parametros);		
		
		if(transacction)
		{
			UtilidadBD.finalizarTransaccion(connection);			
			logger.info("----->INSERTO 100% CONSENTIMIENTO INFORMADO HISTORIAL");
		}
		else
		{
			UtilidadBD.abortarTransaccion(connection);
		}					
	}	
}