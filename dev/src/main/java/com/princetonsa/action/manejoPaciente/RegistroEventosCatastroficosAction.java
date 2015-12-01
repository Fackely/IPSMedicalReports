package com.princetonsa.action.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;
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
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.manejoPaciente.UtilidadesManejoPaciente;
import util.reportes.ConsultasBirt;
import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.manejoPaciente.RegistroEventosCatastroficosForm;
import com.princetonsa.dto.manejoPaciente.DtoRegistroEventosCatastroficos;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.ParametrizacionInstitucion;
import com.princetonsa.mundo.manejoPaciente.PlanosFURIPS;
import com.princetonsa.mundo.manejoPaciente.RegistroEventosCatastroficos;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;
import com.servinte.axioma.dto.manejoPaciente.DtoReclamacionesAccEveFact;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.servicio.fabrica.ManejoPacienteServicioFabrica;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IAmparosPorReclamarServicio;

public class RegistroEventosCatastroficosAction extends Action
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(RegistroEventosCatastroficosAction.class);

	/**
	 * M�todo execute del action
	 */
	public ActionForward execute(	ActionMapping mapping, 	
													        ActionForm form, 
													        HttpServletRequest request, 
													        HttpServletResponse response) throws Exception
													        {
		Connection con = null;
		try{
			if(form instanceof RegistroEventosCatastroficosForm)
			{

				con = UtilidadBD.abrirConexion();
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}
				RegistroEventosCatastroficosForm forma=(RegistroEventosCatastroficosForm)form;

				UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());
				PersonaBasica paciente= (PersonaBasica)request.getSession().getAttribute("pacienteActivo");

				RegistroEventosCatastroficos mundo=new RegistroEventosCatastroficos();

				String estado=forma.getEstado();
				String [] indicesListado=mundo.indicesListado;

				logger.info("Estado -->"+estado);

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de RegistroEventosCatastroficosAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}

				//Esta se modifico por la Tarea 49169
				/*if(!UtilidadesHistoriaClinica.obtenerEstadoIngreso(con, paciente.getCodigoIngreso()).getCodigo().equals(ConstantesIntegridadDominio.acronimoEstadoAbierto))
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "ESTADO INGRESO NO ABIERTO", "El Ingreso no se encuentra en Estado Abierto", false);*/

				//UtilidadValidacion.esCuentaValida(con, paciente.getCodigoCuenta());
				/**
				 * Mt 2033 Validaciones en campos Registro de Eventos Catastr�ficos.
				 * No tomaba  las ciudades asociadas a los departamentos que se encuentran asociados al pa�s definido en el campo Pa�s de la funcionalidad 'Parametrizaci�n de la Instituci�n'. por que 
				 * se estaba llamando el metodo asi forma.setCiudades(Utilidades.obtenerCiudades(con)); 
				 * ParametrizacionInstitucion mundoInstitucion = new ParametrizacionInstitucion();
				 * mundoInstitucion.cargar(con, usuario.getCodigoInstitucionInt());
				 * forma.setCiudades(Utilidades.obtenerCiudadesXPais(con,mundoInstitucion.getPais().getCodigo()+""));
				 * 
				 */
				ParametrizacionInstitucion mundoInstitucion = new ParametrizacionInstitucion();
				mundoInstitucion.cargar(con, usuario.getCodigoInstitucionInt());
				forma.setCiudades(Utilidades.obtenerCiudadesXPais(con,mundoInstitucion.getPais().getCodigo()+""));
				forma.setTiposId(Utilidades.obtenerTiposIdentificacion(con,"",usuario.getCodigoInstitucionInt()));
				forma.setProfesionalesSalud(Utilidades.obtenerMedicosInstitucion(con, usuario.getCodigoInstitucion()));
				forma.setNaturalezaEventosCatastroficos(Utilidades.obtenerNaturalezasEventosCatastroficos(con));

				//Validar si el usuario tiene el rol para la Funcionalidad Amparos
				forma.setModificarAmparos(Utilidades.tieneRolFuncionalidad(usuario.getLoginUsuario(), 855));
				logger.info("===>Modificar Amparos: "+forma.isModificarAmparos());

				if (estado.equals("empezar"))
				{
					if(forma.getIngreso()<=0)
						forma.setIngreso(paciente.getCodigoIngreso());

					ActionForward forwardPaginaErrores = accionValidacionesAcceso(forma, paciente, mapping, request, con,forma.getIngreso(),usuario.getCodigoInstitucionInt());
					if(forwardPaginaErrores!=null)
						return forwardPaginaErrores;
					forma.reset();
					HashMap<String, Object> criterios = new HashMap<String, Object>();
					criterios.put("institucion", usuario.getCodigoInstitucion());
					forma.setListInstitucionesSirc(Utilidades.obtenerInstitucionesSirc(con, criterios));
					forma.setOcultarEncabezado(UtilidadTexto.getBoolean(request.getParameter("ocultarEncabezado")));
					int ingresoTempo=forma.getIngreso();
					PropertyUtils.copyProperties(forma,mundo.consultarRegistroEventoCatastroficoIngreso(con, forma.getIngreso(),""));
					forma.setIngreso(ingresoTempo);

					logger.info("===>Fecha Remision: "+forma.getFechaRemision());
					logger.info("===>Hora Remision: "+forma.getHoraRemision());
					this.postularPais(con,forma,usuario.getCodigoInstitucionInt());
					if(forma.getEstadoRegistro().equals(""))
					{
						forma.setEstadoRegistro(ConstantesIntegridadDominio.acronimoEstadoPendiente);
					}
					if(forma.getEstadoRegistro().equals(ConstantesIntegridadDominio.acronimoEstadoFinalizado))
					{
						UtilidadBD.cerrarConexion(con);
						return mapping.findForward("resumen");
					}
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("guardar"))
				{
					ActionForward forwardPaginaErrores = accionValidacionesAcceso(forma, paciente, mapping, request, con,forma.getIngreso(),usuario.getCodigoInstitucionInt());
					if(forwardPaginaErrores!=null)
						return forwardPaginaErrores;
					DtoRegistroEventosCatastroficos dto = new DtoRegistroEventosCatastroficos();

					if(UtilidadTexto.getBoolean(ValoresPorDefecto.getPermitirRegistrarReclamacionCuentasNoFacturadas(usuario.getCodigoInstitucionInt())))
					{
						forma.setValidacionIngresoFacturado(ConstantesBD.acronimoSi);	
					}
					else
					{
						//Validar la si el ingreso es Facturado
						if (Utilidades.esIngresoFacturado(con, forma.getIngreso()+"")>0)
							forma.setValidacionIngresoFacturado(ConstantesBD.acronimoSi);
						else
							forma.setValidacionIngresoFacturado(ConstantesBD.acronimoNo);
					}
					logger.info("===>Ingreso Facturado: "+forma.getValidacionIngresoFacturado());

					logger.info("===>Ciudad/Depto Transporta: "+forma.getCiuDepTranporta());
					logger.info("===>Ciudad Transporta: "+forma.getCiudadTranporta());
					logger.info("===>Depto Transporta: "+forma.getDeptoTransporta());

					/*Recursividad colocada por el no manejo de la propiedad readonly en los select
				ya que se presenta problemas con la propiedad disabled con el struts. Se debe
				cambiar cuando se encuentre una soluci�n m�s viable*/
					if(!UtilidadCadena.noEsVacio(forma.getTipoReferencia()))
					{
						//Reseteamos los valores de los dos select de prestador; tanto remite como recibe
						forma.setPrestadorRecibe("");
						forma.setPrestadorRemite("");
					}

					PropertyUtils.copyProperties(dto,forma);
					dto.setFechaModifica(UtilidadFecha.getFechaActual());
					dto.setHoraModifica(UtilidadFecha.getHoraActual());
					dto.setUsuarioModifica(usuario.getLoginUsuario());
					String[] paisTrasporte=dto.getPaisTransporta().split("-");
					dto.setPaisTransporta(paisTrasporte[0]);
					if(forma.getCodigo()>0)
					{
						DtoRegistroEventosCatastroficos dtoOld=mundo.consultarRegistroEventoCatastroficoIngreso(con, forma.getIngreso(),"");
						this.generarLog(dtoOld,dto,usuario);
						mundo.modificarRegistroAccidentesTransito(con, dto, usuario.getCodigoInstitucionInt());
					}
					else
					{
						mundo.insertarRegistroAccidentesTransito(con, dto, usuario.getCodigoInstitucionInt());
						forma.setCodigo(dto.getCodigo());
					}

					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("resumen");
				}

				/*-------------------------------------------
				 *  		ESTADO ----> ACTUALIZAR SERVICIOS RECLAMADOS
			 -------------------------------------------*/
				else if(estado.equals("actualizarServiciosReclamados"))
				{ 
					DtoRegistroEventosCatastroficos dto = new DtoRegistroEventosCatastroficos();
					PropertyUtils.copyProperties(dto,forma);
					mundo.actualizarServiciosReclamados(con, dto);		    	
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("interfazActuResum");
				}
				else if(estado.equals("interfazActuResum"))
				{		    
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("interfazActuResum");
				}	    
				else if(estado.equals("actualizarResumen"))
				{		    
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("resumen");
				}
				if(estado.equals("verificarIngresar"))
				{ 
					forma.setIngreso(paciente.getCodigoIngreso());
					return accionVerificarIngreso(forma,mundo,usuario,paciente,mapping,request,con,forma.getIngreso());


				}
				else if(estado.equals("resumen"))
				{
					/***INICIALIZAR LOS ARCHIVOS DE EMPISION DE FORMATOS***/
					forma.resetFURIPSFURPRO();

					logger.info("===>Codigo Ingreso Forma: "+forma.getIngreso());
					//Validar la si el ingreso es Facturado
					if(UtilidadTexto.getBoolean(ValoresPorDefecto.getPermitirRegistrarReclamacionCuentasNoFacturadas(usuario.getCodigoInstitucionInt())))
					{
						forma.setValidacionIngresoFacturado(ConstantesBD.acronimoSi);	
					}
					else
					{
						if (Utilidades.esIngresoFacturado(con, forma.getIngreso()+"")>0)
							forma.setValidacionIngresoFacturado(ConstantesBD.acronimoSi);
						else
							forma.setValidacionIngresoFacturado(ConstantesBD.acronimoNo);
					}




					int ingresoTempo=forma.getIngreso();
					PropertyUtils.copyProperties(forma,mundo.consultarRegistroEventoCatastroficoIngreso(con, forma.getIngreso(),""));
					forma.setIngreso(ingresoTempo);


					IAmparosPorReclamarServicio amparosPorReclamarServicio=ManejoPacienteServicioFabrica.crearAmparosPorReclamarServicio();
					HibernateUtil.beginTransaction();
					forma.setListadoReclamaciones(amparosPorReclamarServicio.consultarReclamacionesEventoCatastrofico(forma.getCodigo(),false));
					HibernateUtil.endTransaction();



					//Se carga el paciente en sesi�n
					paciente.cargar(con,UtilidadesManejoPaciente.obtenerCodigoPacienteXIngreso(con, forma.getIngreso()));
					paciente.cargarPacienteXingreso(con, forma.getIngreso()+"", usuario.getCodigoInstitucion(), usuario.getCodigoCentroAtencion()+"");



					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("resumen");
				}
				else if(estado.equals("detalleReclamacion"))
				{
					IAmparosPorReclamarServicio amparosPorReclamarServicio=ManejoPacienteServicioFabrica.crearAmparosPorReclamarServicio();
					forma.setAmparoXReclamar(amparosPorReclamarServicio.consultarReclamacion(forma.getListadoReclamaciones().get(forma.getIndiceReclamacionSeleccionada()).getCodigoPk()));
					return mapping.findForward("detalleReclamacion");
				}
				else if(estado.equals("resumenRA"))
				{
					int ingresoTempo=forma.getIngreso();
					PropertyUtils.copyProperties(forma,mundo.consultarRegistroEventoCatastroficoIngreso(con, forma.getIngreso(),""));
					forma.setIngreso(ingresoTempo);

					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("resumenRA");
				}
				else if(estado.equals("consultaCiudadTransporta"))
				{
					forma.setEstado("empezar");
					forma.setCiuDepExpIdTransporta(ConstantesBD.separadorSplit);
					forma.setDeptoExpIdTransporta("");
					forma.setCiudadExpIdTransporta("");
					return mapping.findForward("principal");
				}
				else if(estado.equals("generarReporte"))
				{    
					DtoRegistroEventosCatastroficos dto = new DtoRegistroEventosCatastroficos();
					if((!UtilidadTexto.getBoolean(forma.getImprimirFURIPS()) && 
							!UtilidadTexto.getBoolean(forma.getImprimirFURIPS2()) && 
							!UtilidadTexto.getBoolean(forma.getImprimirFURPRO())))//||forma. numeroReclamacionesAImprimir()<=0)
					{
						ActionErrors errores = new ActionErrors();
						errores.add("errores blanco",new ActionMessage("error.errorEnBlanco","Para realizar la impresi�n, debe seleccionar al menos una opci�n de la secci�n 'Formatos a imprimir'. Por favor verifique"));
						saveErrors(request, errores);
						UtilidadBD.closeConnection(con);			
						return mapping.findForward("resumen");		

					}
					else
					{
						return this.generarReporte(con, forma, usuario, request, paciente,mapping);
					}


				}
				else /*-------------------------------------------
				 *  		ESTADO ----> CARGARLISTADO
				  -------------------------------------------*/
					if(estado.equals("cargarListado"))
					{
						HashMap criterios = new HashMap ();
						//criterios.put("estado", ConstantesIntegridadDominio.acronimoEstadoFinalizado);
						criterios.put("paciente", paciente.getCodigoPersona());
						forma.setListadoEventos(mundo.cargarListadoEventosCatastroficos(con, criterios));
						UtilidadBD.cerrarConexion(con);
						return mapping.findForward("listado");
					}
					else /*-------------------------------------------
					 *  		ESTADO ----> CARGAREVENTO
				  -------------------------------------------*/
						if(estado.equals("cargarEvento"))
						{ 
							/*
					mundo.guardarAsosico(con, forma, paciente); 
	                UtilidadBD.cerrarConexion(con);
	                return mapping.findForward("principal");
							 */
							//Formateamos las Opciones de Impresion y Verificamos que si el Anexo de Gastos de Movilizaci�n se va ha presentar
							forma.inicializarOpcionesImpresion();
							PropertyUtils.copyProperties(forma,mundo.consultarRegistroEventoCatastroficoIngreso(con, ConstantesBD.codigoNuncaValido,forma.getListadoEventos(indicesListado[5]+forma.getIndex())+""));
							UtilidadBD.cerrarConexion(con);
							return mapping.findForward("resumen");
						}
						else /*-------------------------------------------
						 *  		ESTADO ----> ASOCIAR
				  -------------------------------------------*/
							if(estado.equals("asociar"))
							{ 
								mundo.guardarAsosico(con, forma, paciente); 
								UtilidadBD.cerrarConexion(con);
								return mapping.findForward("resumen");
							}
							else /*-------------------------------------------
							 *  		ESTADO ----> VERIFICAR
				  -------------------------------------------*/

								if(estado.equals("verificar"))
								{ 

									if(UtilidadTexto.getBoolean(ValoresPorDefecto.getPermitirFacturarReclamarCuentasConRegistroPendientes(usuario.getCodigoInstitucionInt())))
									{
										HashMap criterios = new HashMap ();
										criterios.put("esPorIngresos",ConstantesBD.acronimoSi);
										criterios.put("paciente", paciente.getCodigoPersona());
										forma.setListadoEventos(mundo.cargarListadoEventosCatastroficos(con, criterios));
										UtilidadBD.cerrarConexion(con);
										return mapping.findForward("listadoIngresos");
									}
									else
									{
										return accionVerificarIngreso(forma,mundo,usuario,paciente,mapping,request,con,forma.getIngreso());
									}

								}
								else if(estado.equals("verificarIngreso"))
								{ 


									return accionVerificarIngreso(forma,mundo,usuario,paciente,mapping,request,con,forma.getIngreso());

								}
				/*Estado creado para poder Modificar los Amparos una
		    vez finalizado el registro de evento catastrofico*/
								else if (estado.equals("actualizarAmparos"))
								{
									DtoRegistroEventosCatastroficos dto=new DtoRegistroEventosCatastroficos();
									PropertyUtils.copyProperties(dto,forma);
									dto.setFechaModifica(UtilidadFecha.getFechaActual());
									dto.setHoraModifica(UtilidadFecha.getHoraActual());
									dto.setUsuarioModifica(usuario.getLoginUsuario());
									mundo.actualizarAmparos(con, dto, usuario.getCodigoInstitucionInt());
									UtilidadBD.cerrarConexion(con);
									return mapping.findForward("resumen");
								}
				/*Estado creado para poder Modificar la Secci�n Datos 
		    por Reclamar*/
								else if (estado.equals("actualizarDatosReclamacion"))
								{
									DtoRegistroEventosCatastroficos dto=new DtoRegistroEventosCatastroficos();
									PropertyUtils.copyProperties(dto,forma);
									dto.setFechaModifica(UtilidadFecha.getFechaActual());
									dto.setHoraModifica(UtilidadFecha.getHoraActual());
									dto.setUsuarioModifica(usuario.getLoginUsuario());
									mundo.actualizarDatosReclamacion(con, dto, usuario.getCodigoInstitucionInt());
									UtilidadBD.cerrarConexion(con);
									return mapping.findForward("resumen");
								}
								else
								{
									request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
									UtilidadBD.cerrarConexion(con);
									return mapping.findForward("paginaError");
								}
			}
			else
			{
				logger.error("El form no es compatible con el form de RegistroEventosCatastroficosForm");
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				return mapping.findForward("paginaError");
			}
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return null;
	}
	
	private ActionForward accionVerificarIngreso(
			RegistroEventosCatastroficosForm forma,
			RegistroEventosCatastroficos mundo, UsuarioBasico usuario,
			PersonaBasica paciente, ActionMapping mapping,
			HttpServletRequest request, Connection con, int ingreso) throws Exception {
		ActionForward forwardPaginaErrores=accionValidacionesAcceso(forma, paciente, mapping, request, con,forma.getIngreso(),usuario.getCodigoInstitucionInt());
		if(forwardPaginaErrores!=null)
			return forwardPaginaErrores;
		
		logger.info("===>Codigo Ingreso: "+forma.getIngreso());
		
		if(UtilidadTexto.getBoolean(ValoresPorDefecto.getPermitirRegistrarReclamacionCuentasNoFacturadas(usuario.getCodigoInstitucionInt())))
    	{
    		forma.setValidacionIngresoFacturado(ConstantesBD.acronimoSi);	
    	}
    	else
    	{
			if (Utilidades.esIngresoFacturado(con, forma.getIngreso()+"")>0)
				forma.setValidacionIngresoFacturado(ConstantesBD.acronimoSi);
			else
				forma.setValidacionIngresoFacturado(ConstantesBD.acronimoNo);
    	}
		forma.reset();
		forma.setOcultarEncabezado(UtilidadTexto.getBoolean(request.getParameter("ocultarEncabezado")));
		int ingresoTempo=forma.getIngreso();
		PropertyUtils.copyProperties(forma,mundo.consultarRegistroEventoCatastroficoIngreso(con, forma.getIngreso(),""));
		forma.setIngreso(ingresoTempo);
		this.postularPais(con,forma,usuario.getCodigoInstitucionInt());
		//Formateamos las Opciones de Impresion y Verificamos que si el Anexo de Gastos de Movilizaci�n se va ha presentar
		forma.inicializarOpcionesImpresion();
		
		if(forma.getEstadoRegistro().equals(""))
			forma.setEstadoRegistro(ConstantesIntegridadDominio.acronimoEstadoPendiente);
		if(forma.getEstadoRegistro().equals(ConstantesIntegridadDominio.acronimoEstadoFinalizado))
		{
			UtilidadBD.cerrarConexion(con);
			return mapping.findForward("resumen");
		}
		
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("verificar");
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param codigoInstitucionInt
	 */
	private void postularPais(Connection con, RegistroEventosCatastroficosForm forma, int codigoInstitucionInt) 
	{
		ParametrizacionInstitucion mundoInstitucion = new ParametrizacionInstitucion();
		mundoInstitucion.cargar(con, codigoInstitucionInt);
		if(UtilidadTexto.isEmpty(forma.getPaisEmpresa()))
			forma.setPaisEmpresa(mundoInstitucion.getPais().getCodigo());
		if(UtilidadTexto.isEmpty(forma.getPaisEvento()))
			forma.setPaisEvento(mundoInstitucion.getPais().getCodigo());
		if(UtilidadTexto.isEmpty(forma.getPaisExpIdTransporta()))
			forma.setPaisExpIdTransporta(mundoInstitucion.getPais().getCodigo());
		if(UtilidadTexto.isEmpty(forma.getPaisTransporta()))
			forma.setPaisTransporta(ValoresPorDefecto.getPaisResidencia(codigoInstitucionInt));
	}

	/**
	 * 
	 * @param dtoOld
	 * @param dto
	 * @param usuario
	 */
	private void generarLog(DtoRegistroEventosCatastroficos dtoOld, DtoRegistroEventosCatastroficos dto, UsuarioBasico usuario)
	{
		String log = "";
		log = "\n   ============INFORMACION ORIGINAL===========   " +
			 "\n*  Ingreso [" +dtoOld.getIngreso()+"] " +
			 "\n*  Empresa Trabaja [" +dtoOld.getEmpresaTrabaja()+"] " +
			 "\n*  Ciudad Empresa [" +dtoOld.getCiudadEmpresa()+"] " +
			 "\n*  Departamento Empresa [" +dtoOld.getDepartamentoEmpresa()+"] " +
			 "\n*  Direcci�n Evento [" +dtoOld.getDireccionEvento()+"] " +
			 "\n*  Fecha Ocurrencia [" +dtoOld.getFechaEvento()+"] " +
			 "\n*  Hora Ocurrencia [" +dtoOld.getHoraEvento()+"] " +
			 "\n*  Descripci�n Ocurrencia [" +dtoOld.getDescOcurrencia()+"] " +
			 "\n*  Ciudad Evento [" +dtoOld.getCiudadEvento()+"] " +
			 "\n*  Departamento Evento [" +dtoOld.getDepartamentoEvento()+"] " +
			 "\n*  Zona Urbana Evento [" +dtoOld.getZonaUrbanaEvento()+"] " +
			 "\n*  Naturaleza Evento [" +dtoOld.getNaturalezaEvento()+"] " +
			 "\n*  Apellido Nombre Transporta [" +dtoOld.getApellidoNombreTransporta()+"] " +
			 "\n*  Tipo Id Transporta [" +dtoOld.getTipoIdTransporta()+"] " +
			 "\n*  N�mero Id Transporta [" +dtoOld.getNumeroIdTransporta()+"] " +
			 "\n*  Ciudad Expedici�n Id Transporta [" +dtoOld.getCiudadExpIdTransporta()+"] " +
			 "\n*  Departamento Expedici�n Id Transporta [" +dtoOld.getDeptoExpIdTransporta()+"] " +
			 "\n*  Tel�fono Transporta [" +dtoOld.getTelefonoTransporta()+"] " +
			 "\n*  Direcci�n Transporta [" +dtoOld.getDireccionTransporta()+"] " +
			 "\n*  Ciudad Transporta [" +dtoOld.getCiudadTranporta()+"] " +
			 "\n*  Departamento Transporta [" +dtoOld.getDeptoTransporta()+"] " +
			 "\n*  Transporta Desde [" +dtoOld.getTransportaDesde()+"] " +
			 "\n*  Transporta Hasta [" +dtoOld.getTransportaHasta()+"] " +
			 "\n*  Tipo Transporte [" +dtoOld.getTipoTransporte()+"] " +
			 "\n*  Placa Vehiculo Transporta [" +dtoOld.getPlacaVehiculoTransporta()+"] " +
			 "\n*  Pa�s Empresa [" +dtoOld.getPaisEmpresa()+"] " +
			 "\n*  Pa�s Evento [" +dtoOld.getPaisEvento()+"] " +
			 "\n*  Pa�s Expedici�n Id Transporta [" +dtoOld.getPaisExpIdTransporta()+"] " +
			 "\n*  Pa�s Transporta [" +dtoOld.getPaisTransporta()+"] " +
			 "\n*  Condici�n Afiliaci�n al SGSSS [" +ValoresPorDefecto.getIntegridadDominio(dtoOld.getSgsss())+"] " +
			 "\n*  Tipo R�gimen [" +ValoresPorDefecto.getIntegridadDominio(dtoOld.getTipoRegimen()) +"] " +
			 "\n*  Tipo Referencia [" +ValoresPorDefecto.getIntegridadDominio(dtoOld.getTipoReferencia()) +"] " +
			 "\n*  Fecha Remisi�n [" +dtoOld.getFechaRemision() +"] " +
			 "\n*  Hora Remisi�n [" +dtoOld.getHoraRemision() +"] " +
			 "\n*  Prestador que Remite [" +dtoOld.getPrestadorRemite() +"] " +
			 "\n*  Profesional que Remite [" +dtoOld.getProfesionalRemite() +"] " +
			 "\n*  Cargo Profesional que Remite [" +dtoOld.getCargoProfesionalRemite() +"] " +
			 "\n*  Fecha Aceptaci�n [" +dtoOld.getFechaAceptacion() +"] " +
			 "\n*  Hora Aceptaci�n [" +dtoOld.getHoraAceptacion() +"] " +
			 "\n*  Prestador que Recibe [" +dtoOld.getPrestadorRecibe() +"] " +
			 "\n*  Profesional que Recibe [" +dtoOld.getProfesionalRecibe() +"] " +
			 "\n*  Cargo Profesional que Recibe [" +dtoOld.getCargoProfesionalRecibe() +"] " +
			 "\n*  Descripci�n Otro Tipo de Transporte [" +dtoOld.getOtroTipoTransporte() +"] " +
			 "\n*  Zona Transporte [" +dtoOld.getZonaTransporte() +"] " +
			 "\n*  Total Facturado Amparo Gastos M�dico Quir�rgicos [" +dtoOld.getTotalFacturadoMedicoQuirurgico() +"] " +
			 "\n*  Total Reclamdo Amparo Gastos M�dico Quir�rgicos [" +dtoOld.getTotalReclamadoMedicoQuirurgico() +"] " +
			 "\n*  Total Facturado Amparo Gastos de Transporte y Movilizaci�n de la V�ctima [" +dtoOld.getTotalFacturadoTransporte() +"] " +
			 "\n*  Total Facturado Amparo Gastos de Transporte y Movilizaci�n de la V�ctima [" +dtoOld.getTotalReclamadoTransporte() +"] " +
			 "\n*  Es Reclamaci�n [" +ValoresPorDefecto.getIntegridadDominio(dtoOld.getEsReclamacion()) +"] " +
			 "\n*  Tipo de Reclamaci�n FURIPS [" +ValoresPorDefecto.getIntegridadDominio(dtoOld.getFurips()) +"] " +
			 "\n*  Tipo de Reclamaci�n FURPRO [" +ValoresPorDefecto.getIntegridadDominio(dtoOld.getFurpro()) +"] " +
			 "\n*  Tipo de Reclamaci�n FURTRAN [" +ValoresPorDefecto.getIntegridadDominio(dtoOld.getFurtran()) +"] " +
			 "\n*  Respuesta a Glosa [" +ValoresPorDefecto.getIntegridadDominio(dtoOld.getRespuestaGlosa()) +"] " +
			 "\n*  No. Radicado Anterior [" +dtoOld.getNumeroRadicadoAnterior() +"] " +
			 "\n*  No. Consecutivo de la Reclamaci�n [" +dtoOld.getNumeroConsecutivoReclamacion() +"] " +
			 "\n*  Pr�tesis [" +dtoOld.getProtesis() +"] " +
			 "\n*  Valor de la Pr�tesis [" +dtoOld.getValorProtesis() +"] " +
			 "\n*  Adaptaci�n de Pr�tesis [" +dtoOld.getAdaptacionProtesis() +"] " +
			 "\n*  Valor de la Adaptaci�n de Pr�tesis [" +dtoOld.getValorAdaptacionProtesis() +"] " +
			 "\n*  Rehabilitaci�n [" +dtoOld.getRehabilitacion() +"] " +
			 "\n*  Valor de la Rehabilitaci�n [" +dtoOld.getValorRehabilitacion() +"] ";
			 
		log += "\n   ===========INFORMACION MODIFICADA==========   " +
			 "\n*  Ingreso [" +dto.getIngreso()+"] " +
			 "\n*  Empresa Trabaja [" +dto.getEmpresaTrabaja()+"] " +
			 "\n*  Ciudad Empresa [" +dto.getCiudadEmpresa()+"] " +
			 "\n*  Departamento Empresa [" +dto.getDepartamentoEmpresa()+"] " +
			 "\n*  Direcci�n Evento [" +dto.getDireccionEvento()+"] " +
			 "\n*  Fecha Ocurrencia [" +dto.getFechaEvento()+"] " +
			 "\n*  Hora Ocurrencia [" +dto.getHoraEvento()+"] " +
			 "\n*  Descripci�n Ocurrencia [" +dto.getDescOcurrencia()+"] " +
			 "\n*  Ciudad Evento [" +dto.getCiudadEvento()+"] " +
			 "\n*  Departamento Evento [" +dto.getDepartamentoEvento()+"] " +
			 "\n*  Zona Urbana Evento [" +dto.getZonaUrbanaEvento()+"] " +
			 "\n*  Naturaleza Evento [" +dto.getNaturalezaEvento()+"] " +
			 "\n*  Apellido Nombre Transporta [" +dto.getApellidoNombreTransporta()+"] " +
			 "\n*  Tipo Id Transporta [" +dto.getTipoIdTransporta()+"] " +
			 "\n*  N�mero Id Transporta [" +dto.getNumeroIdTransporta()+"] " +
			 "\n*  Ciudad Expedici�n Id Transporta [" +dto.getCiudadExpIdTransporta()+"] " +
			 "\n*  Departamento Expedici�n Id Transporta [" +dto.getDeptoExpIdTransporta()+"] " +
			 "\n*  Tel�fono Transporta [" +dto.getTelefonoTransporta()+"] " +
			 "\n*  Direcci�n Transporta [" +dto.getDireccionTransporta()+"] " +
			 "\n*  Ciudad Transporta [" +dto.getCiudadTranporta()+"] " +
			 "\n*  Departamento Transporta [" +dto.getDeptoTransporta()+"] " +
			 "\n*  Transporta Desde [" +dto.getTransportaDesde()+"] " +
			 "\n*  Transporta Hasta [" +dto.getTransportaHasta()+"] " +
			 "\n*  Tipo Transporte [" +dto.getTipoTransporte()+"] " +
			 "\n*  Placa Vehiculo Transporta [" +dto.getPlacaVehiculoTransporta()+"] " +
			 "\n*  Pa�s Empresa [" +dto.getPaisEmpresa()+"] " +
			 "\n*  Pa�s Evento [" +dto.getPaisEvento()+"] " +
			 "\n*  Pa�s Expedici�n Id Transporta [" +dto.getPaisExpIdTransporta()+"] " +
			 "\n*  Pa�s Transporta [" +dto.getPaisTransporta()+"] " +
			 "\n*  Condici�n Afiliaci�n al SGSSS [" +ValoresPorDefecto.getIntegridadDominio(dto.getSgsss())+"] " +
			 "\n*  Tipo R�gimen [" +ValoresPorDefecto.getIntegridadDominio(dto.getTipoRegimen()) +"] " +
			 "\n*  Tipo Referencia [" +ValoresPorDefecto.getIntegridadDominio(dto.getTipoReferencia()) +"] " +
			 "\n*  Fecha Remisi�n [" +dto.getFechaRemision() +"] " +
			 "\n*  Hora Remisi�n [" +dto.getHoraRemision() +"] " +
			 "\n*  Prestador que Remite [" +dto.getPrestadorRemite() +"] " +
			 "\n*  Profesional que Remite [" +dto.getProfesionalRemite() +"] " +
			 "\n*  Cargo Profesional que Remite [" +dto.getCargoProfesionalRemite() +"] " +
			 "\n*  Fecha Aceptaci�n [" +dto.getFechaAceptacion() +"] " +
			 "\n*  Hora Aceptaci�n [" +dto.getHoraAceptacion() +"] " +
			 "\n*  Prestador que Recibe [" +dto.getPrestadorRecibe() +"] " +
			 "\n*  Profesional que Recibe [" +dto.getProfesionalRecibe() +"] " +
			 "\n*  Cargo Profesional que Recibe [" +dto.getCargoProfesionalRecibe() +"] " +
			 "\n*  Descripci�n Otro Tipo de Transporte [" +dto.getOtroTipoTransporte() +"] " +
			 "\n*  Zona Transporte [" +dto.getZonaTransporte() +"] " +
			 "\n*  Total Facturado Amparo Gastos M�dico Quir�rgicos [" +dto.getTotalFacturadoMedicoQuirurgico() +"] " +
			 "\n*  Total Reclamdo Amparo Gastos M�dico Quir�rgicos [" +dto.getTotalReclamadoMedicoQuirurgico() +"] " +
			 "\n*  Total Facturado Amparo Gastos de Transporte y Movilizaci�n de la V�ctima [" +dto.getTotalFacturadoTransporte() +"] " +
			 "\n*  Total Facturado Amparo Gastos de Transporte y Movilizaci�n de la V�ctima [" +dto.getTotalReclamadoTransporte() +"] " +
			 "\n*  Es Reclamaci�n [" +ValoresPorDefecto.getIntegridadDominio(dto.getEsReclamacion()) +"] " +
			 "\n*  Tipo de Reclamaci�n FURIPS [" +ValoresPorDefecto.getIntegridadDominio(dto.getFurips()) +"] " +
			 "\n*  Tipo de Reclamaci�n FURPRO [" +ValoresPorDefecto.getIntegridadDominio(dto.getFurpro()) +"] " +
			 "\n*  Tipo de Reclamaci�n FURTRAN [" +ValoresPorDefecto.getIntegridadDominio(dto.getFurtran()) +"] " +
			 "\n*  Respuesta a Glosa [" +ValoresPorDefecto.getIntegridadDominio(dto.getRespuestaGlosa()) +"] " +
			 "\n*  No. Radicado Anterior [" +dto.getNumeroRadicadoAnterior() +"] " +
			 "\n*  No. Consecutivo de la Reclamaci�n [" +dto.getNumeroConsecutivoReclamacion() +"] " +
			 "\n*  Pr�tesis [" +dto.getProtesis() +"] " +
			 "\n*  Valor de la Pr�tesis [" +dto.getValorProtesis() +"] " +
			 "\n*  Adaptaci�n de Pr�tesis [" +dto.getAdaptacionProtesis() +"] " +
			 "\n*  Valor de la Adaptaci�n de Pr�tesis [" +dto.getValorAdaptacionProtesis() +"] " +
			 "\n*  Rehabilitaci�n [" +dto.getRehabilitacion() +"] " +
			 "\n*  Valor de la Rehabilitaci�n [" +dto.getValorRehabilitacion() +"] ";
			 
		log += "\n========================================================\n\n\n ";
		LogsAxioma.enviarLog(ConstantesBD.logRegistroEventoCatastroficoCodigo,log,ConstantesBD.tipoRegistroLogModificacion,usuario.getLoginUsuario());
		logger.info("INFORMACION DEL LOG DE EVENTOS CATASTROFICOS-->> "+log);

	}

	/**
	 * validaciones de acceso
	 * @param forma 
	 * @param paciente
	 * @param mapping
	 * @param request
	 * @param con
	 * @return
	 */
	protected ActionForward accionValidacionesAcceso(RegistroEventosCatastroficosForm forma, PersonaBasica paciente, ActionMapping mapping, HttpServletRequest request, Connection con, int ingreso,int institucion)
	{
		if(paciente.getCodigoPersona()<1)
		    return ComunAction.accionSalirCasoError(mapping, request, con, logger, "paciente null o sin  id", "errors.paciente.noCargado", true);
		
		if(!UtilidadTexto.getBoolean(ValoresPorDefecto.getPermitirFacturarReclamarCuentasConRegistroPendientes(institucion)))
		{
			if(paciente.getCodigoCuenta()<1)
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "PACIENTE SIN CUENTA ACTIVA", "errors.paciente.cuentaNoAbierta", true);
		}
		    
		if(!UtilidadValidacion.esEventoCatastrofico(con, ingreso))
		    return ComunAction.accionSalirCasoError(mapping, request, con, logger, "NO EVENTO CATASTROFICO", "error.noEventoCatastrofico", true);
		
		return null;
	}
	
	
	 /**
     * Metodo para generar los reportes
     * @param tipoReporte
     * @param con
     * @param forma
     * @param usuario
     * @param request
	 * @param paciente 
	 * @param mapping 
	 * @return 
     */
	private ActionForward generarReporte(Connection con, RegistroEventosCatastroficosForm forma, UsuarioBasico usuario, HttpServletRequest request, PersonaBasica paciente, ActionMapping mapping) 
    {
		if(!UtilidadTexto.getBoolean(forma.getImprimirARCHIVOPLANO()))
		{
			String reportes="";
			boolean imprimirReclamacion = false;
			if (!forma.getListadoReclamaciones().isEmpty()) {
				for(DtoReclamacionesAccEveFact reclamacion:forma.getListadoReclamaciones())
				{
					if(UtilidadTexto.getBoolean(reclamacion.getImprimirFuripsFurpro()))
					{
						imprimirReclamacion = true;
						if(reclamacion.getTipoReclamacion().equals(ConstantesIntegridadDominio.acronimoFURIPS))
						{
							if(UtilidadTexto.getBoolean(forma.getImprimirFURIPS()))
							{
								if(!UtilidadTexto.isEmpty(reportes))
									reportes = reportes+ConstantesBD.separadorSplit;
								reportes += generarReporteFurips(con, forma,reclamacion.getCodigoPk(), usuario, paciente, request);
							}
						}
						else if(reclamacion.getTipoReclamacion().equals(ConstantesIntegridadDominio.acronimoFURPRO))
						{
							if(UtilidadTexto.getBoolean(forma.getImprimirFURPRO()))
							{
								if(!UtilidadTexto.isEmpty(reportes))
									reportes = reportes+ConstantesBD.separadorSplit;
								reportes += generarReporteFurpro(con, forma,reclamacion.getCodigoPk(), usuario, paciente, request);
							}
						}
					}
				}
				if (!imprimirReclamacion) {
					if(UtilidadTexto.getBoolean(forma.getImprimirFURIPS()))	{
						reportes = generarInformeEventoCatastrofico(con, forma, usuario, paciente, request);
					}else if(UtilidadTexto.getBoolean(forma.getImprimirFURPRO()))	{
						reportes = generarInformeFurpro(con, forma, usuario, paciente, request);
					}
				}
			} else {
				if(UtilidadTexto.getBoolean(forma.getImprimirFURIPS())) {
					reportes = generarInformeEventoCatastrofico(con, forma, usuario, paciente, request);
				} else if(UtilidadTexto.getBoolean(forma.getImprimirFURPRO()))	{
					reportes = generarInformeFurpro(con, forma, usuario, paciente, request);
				}
			}
			
			if(!UtilidadTexto.isEmpty(reportes))
			{
				request.setAttribute("isOpenReport", "true");
	            request.setAttribute("reportesGenerados", reportes);
			}
			
		}
		else
		{
			String codigosReclamaciones="";
			for(DtoReclamacionesAccEveFact reclamacion:forma.getListadoReclamaciones())
			{
				if(UtilidadTexto.getBoolean(reclamacion.getImprimirFuripsFurpro()))
				{
					if(!UtilidadTexto.isEmpty(codigosReclamaciones))
						codigosReclamaciones=codigosReclamaciones+",";
					codigosReclamaciones=codigosReclamaciones+reclamacion.getCodigoPk();
				}
			}
			
			//generar el plano.
			
			
			InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
			String fechaActual=UtilidadFecha.getFechaActual(con);
			forma.resetearNombreArchivos(usuario, institucionBasica, fechaActual, fechaActual);
			PlanosFURIPS mundo= new PlanosFURIPS();
			boolean existeRegistros=false;
			HashMap mapaBusqueda=new HashMap();
			mapaBusqueda.put(PlanosFURIPS.archivosEnum.Furips1+"", forma.getImprimirFURIPS());
			mapaBusqueda.put(PlanosFURIPS.archivosEnum.Furips2+"", forma.getImprimirFURIPS2());
			mapaBusqueda.put(PlanosFURIPS.archivosEnum.Furpro1+"", forma.getImprimirFURPRO());
			mapaBusqueda.put("tipomanual",ConstantesBD.codigoTarifarioCups+"");
			mapaBusqueda.put("nrofolios_"+PlanosFURIPS.archivosEnum.Furips1+"",forma.getNumeroFoliosFURIPS());
			mapaBusqueda.put("nrofolios_"+PlanosFURIPS.archivosEnum.Furpro1+"",forma.getNumeroFoliosFURPRO());
			
			//FURIPS1
			if(UtilidadTexto.getBoolean(mapaBusqueda.get(PlanosFURIPS.archivosEnum.Furips1+"")+""))
	    	{
				forma.setRutasArchivos(mundo.consultaFURIPS1(con, mapaBusqueda, institucionBasica, usuario.getCodigoInstitucionInt(), forma.getRutasArchivos(),codigosReclamaciones));
				
				if(!forma.getRutasArchivos().isProcesoExitoso())
				{
					ActionErrors errores = new ActionErrors();
					errores.add("", new ActionMessage("error.file.noGenerado", "FURIPS1"));
					saveErrors(request, errores);	
					UtilidadBD.closeConnection(con);											
					return mapping.findForward("resumen");
				}
				if(!UtilidadTexto.isEmpty(forma.getRutasArchivos().getStringBufferFURIPS1().toString().trim()) || !UtilidadTexto.isEmpty(forma.getRutasArchivos().getStringBufferInconsistenciasFURIPS1().toString().trim()))
				{
					existeRegistros=true;
				}
			}
			
			//FURIPS2
			if(UtilidadTexto.getBoolean(mapaBusqueda.get(PlanosFURIPS.archivosEnum.Furips2+"")+""))
	    	{
				forma.setRutasArchivos(mundo.consultaFURIPS2(con, mapaBusqueda, institucionBasica, usuario.getCodigoInstitucionInt(), forma.getRutasArchivos(),codigosReclamaciones));
				
				if(!forma.getRutasArchivos().isProcesoExitoso())
				{
					ActionErrors errores = new ActionErrors();
					errores.add("", new ActionMessage("error.file.noGenerado", "FURIPS2"));
					saveErrors(request, errores);	
					UtilidadBD.closeConnection(con);										
					return mapping.findForward("resumen");
				}
				if(!UtilidadTexto.isEmpty(forma.getRutasArchivos().getStringBufferFURIPS2().toString().trim()) || !UtilidadTexto.isEmpty(forma.getRutasArchivos().getStringBufferInconsistenciasFURIPS2().toString().trim()))
				{
					existeRegistros=true;
				}
			}
			
			//FURPRO
			if(UtilidadTexto.getBoolean(mapaBusqueda.get(PlanosFURIPS.archivosEnum.Furpro1+"")+""))
	    	{
				forma.setRutasArchivos(mundo.consultaFURPRO(con, mapaBusqueda, institucionBasica, usuario.getCodigoInstitucionInt(), forma.getRutasArchivos(),codigosReclamaciones));
				
				if(!forma.getRutasArchivos().isProcesoExitoso())
				{
					ActionErrors errores = new ActionErrors();
					errores.add("", new ActionMessage("error.file.noGenerado", "FURPRO"));
					saveErrors(request, errores);	
					UtilidadBD.closeConnection(con);									
					return mapping.findForward("resumen");
				}
				if(!UtilidadTexto.isEmpty(forma.getRutasArchivos().getStringBufferFURPRO().toString().trim()) || !UtilidadTexto.isEmpty(forma.getRutasArchivos().getStringBufferInconsistenciasFURPRO().toString().trim()))
				{
					existeRegistros=true;
				}
			}
			
			
			//generamos el zip
			forma.setRutasArchivos(mundo.generarArchivoZip(forma.getRutasArchivos(), mapaBusqueda));
			

			 //fecha para el log
			mapaBusqueda.put("fechainicial",fechaActual);
			mapaBusqueda.put("fechafinal",fechaActual);
			//finalmente insertamos el log de bd
			UtilidadBD.iniciarTransaccion(con);
			if(!mundo.insertarLogBD(con, forma.getRutasArchivos(), mapaBusqueda, usuario.getCodigoInstitucionInt(), usuario.getLoginUsuario()))
			{
				UtilidadBD.abortarTransaccion(con);
			}
			UtilidadBD.finalizarTransaccion(con);
			
		}
		forma.setEstado("mostrarArchivos");
		
		UtilidadBD.closeConnection(con);			
		return mapping.findForward("resumen");
		 
	}

	
	/**
	 * M�todo que permite generar el Reporte FURIPS
	 * @param con
	 * @param forma
	 * @param dto 
	 * @param usuario
	 * @param paciente 
	 * @param request
	 */
	private String generarReporteFurips(Connection con, RegistroEventosCatastroficosForm forma,int codigoReclamacion, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request) 
	{
		DesignEngineApi comp;
		logger.info("--->"+ParamsBirtApplication.getReportsPath()+"fosyga/");
		comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"fosyga/","FormatoFURIPS.rptdesign");
        comp.obtenerComponentesDataSet("dataSet");
        String oldQuery = comp.obtenerQueryDataSet();
        String newQuery = ConsultasBirt.furipsEventoCatastrofico(forma.getIngreso(), UtilidadesManejoPaciente.obtenerCodigoMedicoTratante(con, forma.getIngreso()),codigoReclamacion);
        comp.modificarQueryDataSet(newQuery);
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
		 //por ultimo se modifica la conexion a BD
        comp.updateJDBCParameters(newPathReport);
        
        return newPathReport;            
       
	}
	
	/**
	 * M�todo que permite generar el Reporte FURIPS
	 * @param con
	 * @param forma
	 * @param dto 
	 * @param usuario
	 * @param paciente 
	 * @param request
	 */
	private String generarInformeEventoCatastrofico(Connection con, RegistroEventosCatastroficosForm forma, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request) 
	{
		DesignEngineApi comp;
		logger.info("--->"+ParamsBirtApplication.getReportsPath()+"fosyga/");
		comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"fosyga/","FormatoFURIPS.rptdesign");
        comp.obtenerComponentesDataSet("dataSet");
        String oldQuery = comp.obtenerQueryDataSet();
        String newQuery = ConsultasBirt.informeEventoCatastrofico(forma.getIngreso(), UtilidadesManejoPaciente.obtenerCodigoMedicoTratante(con, forma.getIngreso()));
        comp.modificarQueryDataSet(newQuery);
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
		 //por ultimo se modifica la conexion a BD
        comp.updateJDBCParameters(newPathReport);
        
        return newPathReport;            
       
	}
	
	/**
	 * M�todo que permite generar el Reporte FURPRO
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * @param request
	 */
	private String generarReporteFurpro(Connection con, RegistroEventosCatastroficosForm forma,int codigoReclamacion, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request) 
	{
		DesignEngineApi comp;
		comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"fosyga/","FormatoFURPRO.rptdesign");
        comp.obtenerComponentesDataSet("dataSet");
        String oldQuery = comp.obtenerQueryDataSet();
        String newQuery = ConsultasBirt.furpro(forma.getIngreso(),codigoReclamacion);
        
        comp.modificarQueryDataSet(newQuery);
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);

        //por ultimo se modifica la conexion a BD
        comp.updateJDBCParameters(newPathReport);
        
        return newPathReport;            
	}
	
	/**
	 * M�todo que permite generar el Reporte FURPRO
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * @param request
	 */
	private String generarInformeFurpro(Connection con, RegistroEventosCatastroficosForm forma, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request) 
	{
		DesignEngineApi comp;
		comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"fosyga/","FormatoFURPRO.rptdesign");
        comp.obtenerComponentesDataSet("dataSet");
        String oldQuery = comp.obtenerQueryDataSet();
        String newQuery = ConsultasBirt.informeFurpro(forma.getIngreso());
        
        comp.modificarQueryDataSet(newQuery);
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);

        //por ultimo se modifica la conexion a BD
        comp.updateJDBCParameters(newPathReport);
        
        return newPathReport;            
	}
	
}