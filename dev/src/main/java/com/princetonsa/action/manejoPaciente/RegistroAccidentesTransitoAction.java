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
import util.Listado;
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.manejoPaciente.UtilidadesManejoPaciente;
import util.reportes.ConsultasBirt;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.manejoPaciente.RegistroAccidentesTransitoForm;
import com.princetonsa.dto.manejoPaciente.DtoRegistroAccidentesTransito;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.ParametrizacionInstitucion;
import com.princetonsa.mundo.manejoPaciente.PlanosFURIPS;
import com.princetonsa.mundo.manejoPaciente.RegistroAccidentesTransito;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;
import com.servinte.axioma.dto.manejoPaciente.DtoReclamacionesAccEveFact;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.servicio.fabrica.ManejoPacienteServicioFabrica;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IAmparosPorReclamarServicio;

public class RegistroAccidentesTransitoAction extends Action
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(RegistroAccidentesTransitoAction.class);
	
	/**
	 * Método execute del action
	 */
	public ActionForward execute(	ActionMapping mapping, 	
													        ActionForm form, 
													        HttpServletRequest request, 
													        HttpServletResponse response) throws Exception
													        {
		Connection con = null;
		try{
			if(form instanceof RegistroAccidentesTransitoForm)
			{

				con = UtilidadBD.abrirConexion();
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}
				RegistroAccidentesTransitoForm forma = (RegistroAccidentesTransitoForm) form;
				RegistroAccidentesTransito mundo= new RegistroAccidentesTransito();
				UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());
				PersonaBasica paciente= (PersonaBasica)request.getSession().getAttribute("pacienteActivo");

				String estado = forma.getEstado();

				logger.warn("estado [RegistroAccidentesTransitoAction.java]-->"+estado);
				//inicializacion de mapas para los selects.
				ParametrizacionInstitucion mundoInstitucion = new ParametrizacionInstitucion();
				mundoInstitucion.cargar(con, usuario.getCodigoInstitucionInt());
				forma.setCiudades(Utilidades.obtenerCiudadesXPais(con,mundoInstitucion.getPais().getCodigo()+""));
				//forma.setConvenios(Utilidades.obtenerConvenios(con,"",ConstantesBD.codigoTipoContratoEvento+"",false,"",false));
				forma.setConvenios(Utilidades.obtenerConvenios(con, "",ConstantesBD.codigoTipoContratoEvento+"", false, "", false, false, ConstantesBD.acronimoSi));
				forma.setTiposId(Utilidades.obtenerTiposIdentificacion(con,"",usuario.getCodigoInstitucionInt()));
				//se consulta si se puede ingrar o modificar 
				forma.setModificarAmparos(Utilidades.tieneRolFuncionalidad(usuario.getLoginUsuario(), 855));
				/*if(!UtilidadesHistoriaClinica.obtenerEstadoIngreso(con, paciente.getCodigoIngreso()).getCodigo().equals(ConstantesIntegridadDominio.acronimoEstadoAbierto))
			{
		    	return ComunAction.accionSalirCasoError(mapping, request, con, logger, "ESTADO INGRESO NO ABIERTO", "El Ingreso no se encuentra en estado Abierto..", false);
			}*/

				//UtilidadValidacion.esCuentaValida(con, paciente.getCodigoCuenta());

				String [] indices= RegistroAccidentesTransito.indicesListado;
				forma.setPermitirImprimirReporteParametroGeneral(ValoresPorDefecto.getPermitirFacturarReclamarCuentasConRegistroPendientes(usuario.getCodigoInstitucionInt()));



				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de RegistroAccidentesTransitoAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				else if (estado.equals("empezar"))
				{

					if(Utilidades.convertirAEntero(forma.getIngreso())<=0)
						forma.setIngreso(paciente.getCodigoIngreso()+"");


					ActionForward forwardPaginaErrores=accionValidacionesAcceso(paciente, mapping, request, con,forma.getIngreso(),usuario.getCodigoInstitucionInt());
					if(forwardPaginaErrores!=null)
						return forwardPaginaErrores;
					forma.reset();
					forma.setOcultarEncabezado(UtilidadTexto.getBoolean(request.getParameter("ocultarEncabezado")));
					forma.setTiposServiciosVehiculos(Utilidades.obtenerTiposServiciosVehiculos(con, ""));//se manda vacio para que los liste todos
					forma.setZonaAccidente(ValoresPorDefecto.getZonaDomicilio(usuario.getCodigoInstitucionInt()));
					HashMap criteriostmp= new HashMap ();
					criteriostmp.put("institucion", usuario.getCodigoInstitucion());
					criteriostmp.put("activo", ConstantesBD.acronimoSi);
					forma.setPrestadores(Utilidades.obtenerInstitucionesSirc(con, criteriostmp));
					String tempoIngreso=forma.getIngreso();
					PropertyUtils.copyProperties(forma,mundo.consultarRegistroAccidentesTransitoIngreso(con, forma.getIngreso()+""));
					forma.setIngreso(tempoIngreso);
					this.postularPais(con,forma,usuario.getCodigoInstitucionInt());
					
					
					if(forma.getEstadoRegistro().equals(""))
					{
						forma.setEstadoRegistro(ConstantesIntegridadDominio.acronimoEstadoPendiente);
					}
/*					if(forma.getAseguradora()==null||forma.getAseguradora().trim().equals(""))
					{
						Cuenta mundoCuenta=new Cuenta();
						mundoCuenta.cargar(con, paciente.getCodigoCuenta()+"");
						for(int i=0; i<mundoCuenta.getCuenta().getConvenios().length; i++)
						{
							if(!UtilidadTexto.isEmpty(mundoCuenta.getCuenta().getConvenios()[i].getNroPoliza()))
							{
								forma.setAseguradora(mundoCuenta.getCuenta().getConvenios()[i].getConvenio().getCodigo()+"");
								forma.setNumeroPoliza(mundoCuenta.getCuenta().getConvenios()[i].getNroPoliza());
								break;
							}
						}
					}*/
					
					////////////////////////////////////////////////////
					//se valida si se permite modificar o no las secciones,
					//ademas si se muestran desplegadas o no.
					validacionSecciones(con, forma,usuario);
					//////////////////////////////////////////////////////

					if(forma.getEstadoRegistro().equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
					{
						UtilidadBD.cerrarConexion(con);
						return mapping.findForward("resumen");
					}
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("guardar"))
				{
					ActionForward forwardPaginaErrores=accionValidacionesAcceso(paciente, mapping, request, con,forma.getIngreso(),usuario.getCodigoInstitucionInt());
					if(forwardPaginaErrores!=null)
						return forwardPaginaErrores;
					DtoRegistroAccidentesTransito dto=new DtoRegistroAccidentesTransito();
					PropertyUtils.copyProperties(dto,forma);
					dto.setFechaGrabacion(UtilidadFecha.getFechaActual());
					dto.setHoraGrabacion(UtilidadFecha.getHoraActual());
					dto.setUsuarioGrabacion(usuario.getLoginUsuario());
					String tempo="";
					if(forma.getCodicionAccidentado().equals(ConstantesIntegridadDominio.acronimoPeaton))
					{
						tempo="Peatón";
					}
					else if(forma.getCodicionAccidentado().equals(ConstantesIntegridadDominio.acronimoCiclista))
					{
						tempo="Ciclista";
					}
					else if(forma.getCodicionAccidentado().equals(ConstantesIntegridadDominio.acronimoVehiculo) || forma.getCodicionAccidentado().equals(ConstantesIntegridadDominio.acronimoMotocicleta))
					{
						tempo="Ocupante";
					}
					else if(forma.getCodicionAccidentado().equals(ConstantesIntegridadDominio.acronimoConductorVehiculo) || forma.getCodicionAccidentado().equals(ConstantesIntegridadDominio.acronimoConductorMotocicleta))
					{
						tempo="Conductor";
					}

					UtilidadBD.iniciarTransaccion(con);
					/**
					 * MT 2037 No muestra la placa del vehiculo en el campo Informe del accidente (Relato breve de los hechos):
					 * Se hacen las validacion cuando es una creacion y cuando es una actualizacion.
					 * Se ponen la logica  del negocio segun lo especifica el DCU 323
					 * 
					 */
					
					if(forma.getCodigo().trim().equals(""))//Si se va a insertar un nuevo accidente de transito 
					{
						if(forma.getAsegurado().equals("FAN")){//si el auto asegurado es de tipo fantasma
							dto.setInformacionAccidente("");
						}else if(!UtilidadTexto.isEmpty(forma.getPlaca())){ // si la placa del vehiculo contiene info , nota el formulario ya deberia estar validado cuando se ejecuta estas lineas de codigo
							// si se selecciona 'Otro' y el accidentado resulta ser atropellado o expuldo por un vehculo AGPO= golpeado por un objeto
							if((forma.getCodicionAccidentado().equals(ConstantesIntegridadDominio.acronimoPeaton)||forma.getCodicionAccidentado().equals(ConstantesIntegridadDominio.acronimoCiclista))&& !forma.getResultaLesionadoAl().equals("AGPO")){
								dto.setInformacionAccidente(tempo+" quien resulta lesionado al "+ValoresPorDefecto.getIntegridadDominio(forma.getResultaLesionadoAl())+" de placas "+forma.getPlacaVehiculoOcaciona());
							}else{
								// si es golpeado por un objeto
								if(forma.getResultaLesionadoAl().equals("AGPO")){
									dto.setInformacionAccidente(tempo+" quien resulta lesionado al "+ValoresPorDefecto.getIntegridadDominio(forma.getResultaLesionadoAl()));
								}else{
									//cuando no se selecciona la opcion otro es igual para Ocupante y Conductor
									dto.setInformacionAccidente(tempo+" del vehículo de placas "+forma.getPlaca()+" quien resulta lesionado al "+ValoresPorDefecto.getIntegridadDominio(forma.getResultaLesionadoAl())+" de placas "+forma.getPlacaVehiculoOcaciona());
								}
							}
							
						}else{
							// sino tiene placa y no es vehiculo fantasma pone que es lesionado por un objeto
							if((forma.getCodicionAccidentado().equals(ConstantesIntegridadDominio.acronimoPeaton)||forma.getCodicionAccidentado().equals(ConstantesIntegridadDominio.acronimoCiclista))&& !forma.getResultaLesionadoAl().equals("AGPO")){
								dto.setInformacionAccidente(tempo+" quien resulta lesionado al "+ValoresPorDefecto.getIntegridadDominio(forma.getResultaLesionadoAl())+" de placas "+forma.getPlacaVehiculoOcaciona());
								
							}else{
								if(forma.getResultaLesionadoAl().equals("AGPO")){
									//cuando no se selecciona la opcion otro es igual para Ocupante y Conductor y la opcion resulta lesioando por es diferente de la estrellase con un vehiculo
									dto.setInformacionAccidente(tempo+" quien resulta lesionado al "+ValoresPorDefecto.getIntegridadDominio(forma.getResultaLesionadoAl()));
								}
							}
						}
						forma.setInformacionAccidente(dto.getInformacionAccidente());
						forma.setCodigo(mundo.insertarRegistroAccidentesTransito(con, dto)+"");
					}
					else
					{
						//Si se va a hacer una actualizacion.
						//DtoRegistroAccidentesTransito dtoOld=mundo.consultarRegistroAccidentesTransitoIngreso(con, forma.getIngreso()+"");
						//this.generarLog(dtoOld,dto,usuario);
						if(forma.getAsegurado().equals("FAN")){//si el auto asegurado es de tipo fantasma
							dto.setInformacionAccidente("");
						}else if(!UtilidadTexto.isEmpty(forma.getPlaca())){ // si la placa del vehiculo contiene info , nota el formulario ya deberia estar validado cuando se ejecuta estas lineas de codigo
							// si se selecciona 'Otro' y el accidentado resulta ser atropellado o expuldo por un vehculo AGPO= golpeado por un objeto
							if((forma.getCodicionAccidentado().equals(ConstantesIntegridadDominio.acronimoPeaton)||forma.getCodicionAccidentado().equals(ConstantesIntegridadDominio.acronimoCiclista))&& !forma.getResultaLesionadoAl().equals("AGPO")){
								dto.setInformacionAccidente(tempo+" quien resulta lesionado al "+ValoresPorDefecto.getIntegridadDominio(forma.getResultaLesionadoAl())+" de placas "+forma.getPlacaVehiculoOcaciona());
							}else{
								// si es golpeado por un objeto
								if(forma.getResultaLesionadoAl().equals("AGPO")){
									dto.setInformacionAccidente(tempo+" quien resulta lesionado al "+ValoresPorDefecto.getIntegridadDominio(forma.getResultaLesionadoAl()));
								}else{
									//cuando no se selecciona la opcion otro es igual para Ocupante y Conductor
									dto.setInformacionAccidente(tempo+" del vehículo de placas "+forma.getPlaca()+" quien resulta lesionado al "+ValoresPorDefecto.getIntegridadDominio(forma.getResultaLesionadoAl())+" de placas "+forma.getPlacaVehiculoOcaciona());
								}
							}
							
						}else{
							// sino tiene placa y no es vehiculo fantasma pone que es lesionado por un objeto
							if((forma.getCodicionAccidentado().equals(ConstantesIntegridadDominio.acronimoPeaton)||forma.getCodicionAccidentado().equals(ConstantesIntegridadDominio.acronimoCiclista))&& !forma.getResultaLesionadoAl().equals("AGPO")){
								dto.setInformacionAccidente(tempo+" quien resulta lesionado al "+ValoresPorDefecto.getIntegridadDominio(forma.getResultaLesionadoAl())+" de placas "+forma.getPlacaVehiculoOcaciona());
							}else{
								if(forma.getResultaLesionadoAl().equals("AGPO")){
									//cuando no se selecciona la opcion otro es igual para Ocupante y Conductor y la opcion resulta lesioando por es diferente de la estrellase con un vehiculo
									dto.setInformacionAccidente(tempo+" quien resulta lesionado al "+ValoresPorDefecto.getIntegridadDominio(forma.getResultaLesionadoAl()));
								}
							}
						}
						forma.setInformacionAccidente(dto.getInformacionAccidente());
						mundo.modificarRegistroAccidentesTransito(con, dto);
					}
					/**
					 * FIN MT 2037
					 */
					
					
					if(!UtilidadTexto.isEmpty(forma.getNumeroPoliza()))
					{
						Cuenta mundoCuenta=new Cuenta();
						mundoCuenta.cargar(con, paciente.getCodigoCuenta()+"");
						for(int i=0; i<mundoCuenta.getCuenta().getConvenios().length; i++)
						{
							if(forma.getAseguradora().equals(mundoCuenta.getCuenta().getConvenios()[i].getConvenio().getCodigo()+""))
							{
								if(mundoCuenta.actualizarNroPolizaConvenioAccidenteTransito(con, paciente.getCodigoIngreso(), Integer.parseInt(forma.getAseguradora()), forma.getNumeroPoliza())<=0)
								{
									ActionErrors errores=new ActionErrors();
									errores.add("", new ActionMessage("errors.problemasBd"));
									Log4JManager.error("Error actualizando el número de póliza para el convenio "+mundoCuenta.getCuenta().getConvenios()[i].getConvenio().getCodigo()+" y el ingreso "+paciente.getCodigoIngreso());
									UtilidadBD.abortarTransaccion(con);
								}
							}
						}
						
					}
					
					UtilidadBD.finalizarTransaccion(con);

					llenarDatosImpresion(forma, paciente);

					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("resumen");
				}
				else if(estado.equals("cargarDetalle"))
				{
					forma.resetImpresionFURIPS();
					PropertyUtils.copyProperties(forma,RegistroAccidentesTransito.consultarRegistroAccidentesTransitoLlave(con, forma.getCodigo()));
					forma.inicializarOpcionesImpresion();

					if(!UtilidadTexto.isEmpty(forma.getCuenta()))
					{
						Cuenta cuenta= new  Cuenta();
						cuenta.cargarCuenta(con, forma.getCuenta());
						paciente.setCodigoPersona(cuenta.getPaciente().getCodigoPersona());
						paciente.cargar(con,cuenta.getPaciente().getCodigoPersona());
						paciente.cargarPaciente(con, cuenta.getPaciente().getCodigoPersona(), usuario.getCodigoInstitucion(),usuario.getCodigoCentroAtencion()+"");
					}

					/////////////////////////////////////////////////////////

					/////////////////////////////////////////////////////////////

					////////////////////////////////////////////////////
					//se valida si se permite modificar o no las secciones,
					//ademas si se muestran desplegadas o no.
					validacionSecciones(con, forma,usuario);
					//////////////////////////////////////////////////////

					IAmparosPorReclamarServicio amparosPorReclamarServicio=ManejoPacienteServicioFabrica.crearAmparosPorReclamarServicio();
					HibernateUtil.beginTransaction();
					forma.setListadoReclamaciones(amparosPorReclamarServicio.consultarReclamacionesAccidenteTransito(Utilidades.convertirAEntero(forma.getCodigo()),false));
					HibernateUtil.endTransaction();


					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("resumen");
				}
				else if(estado.equals("detalleReclamacion"))
				{
					IAmparosPorReclamarServicio amparosPorReclamarServicio=ManejoPacienteServicioFabrica.crearAmparosPorReclamarServicio();
					forma.setAmparoXReclamar(amparosPorReclamarServicio.consultarReclamacion(forma.getListadoReclamaciones().get(forma.getIndiceReclamacionSeleccionada()).getCodigoPk()));
					return mapping.findForward("detalleReclamacion");
				}
				else if(estado.equals("cargarDetalleXIngreso"))
				{
					forma.inicializarOpcionesImpresion();	
					String tempoIngreso=forma.getIngreso();
					PropertyUtils.copyProperties(forma,mundo.consultarRegistroAccidentesTransitoIngreso(con, forma.getIngreso()+""));
					forma.setIngreso(tempoIngreso);

					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("resumen");
				}
				else if(estado.equals("empezarConsultaRangos"))
				{
					forma.reset();
					return accionEmpezarConsulta(forma,usuario,mapping, con, "busquedaAvanzada");
				}
				else if(estado.equals("empezarConsultaPaciente"))
				{
					forma.reset();
					ActionForward forwardPaginaErrores=accionValidacionesAcceso(paciente, mapping, request, con,forma.getIngreso(),usuario.getCodigoInstitucionInt());
					if(forwardPaginaErrores!=null)
						return forwardPaginaErrores;
					forma.setCriteriosBusquedaMap("idIngresoPaciente", forma.getIngreso()+"");
					forma.setEstado("busquedaAvanzada");
					return accionBusquedaAvanzada(forma,usuario, mapping, con, mundo);
				}
				else if(estado.equals("busquedaAvanzada"))
				{
					return accionBusquedaAvanzada(forma,usuario, mapping, con, mundo);
				}
				else if(estado.equals("busquedaAvanzadaConErrores"))
				{
					return accionEmpezarConsulta(forma,usuario,mapping, con, "busquedaAvanzada");
				}
				else if(estado.equals("ordenar"))
				{
					this.accionOrdenar(forma);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("busquedaAvanzada");
				}
				else if(estado.equals("consultaCiudadPropietario"))
				{
					forma.setCiuDepExpIdProp(" - - ");
					return mapping.findForward("principal");
				}
				else if(estado.equals("consultaCiudadConductor"))
				{
					forma.setCiuDepIdConductor(" - - ");
					return mapping.findForward("principal");
				}
				else if(estado.equals("consultaCiudadTransporta"))
				{
					forma.setCiuDepExpIdTransporta(" - - ");
					return mapping.findForward("principal");
				}
				else if(estado.equals("consultaCiudadDeclarante"))
				{
					forma.setCiuDepIdDeclarante(" - - ");
					return mapping.findForward("principal");
				}
				else if(estado.equals("verificarIngresar"))
				{ 
					forma.setIngreso(paciente.getCodigoIngreso()+"");
					if(UtilidadTexto.getBoolean(ValoresPorDefecto.getPermitirFacturarReclamarCuentasConRegistroPendientes(usuario.getCodigoInstitucionInt())))
					{
						HashMap criterios = new HashMap ();
						criterios.put("esPorIngresos",ConstantesBD.acronimoSi);
						criterios.put("paciente", paciente.getCodigoPersona());
						forma.setListadoAccidentes(mundo.cargarListadoAccidentesTransito(con, criterios));
						Utilidades.imprimirMapa(forma.getListadoAccidentes());
						UtilidadBD.cerrarConexion(con);
						return mapping.findForward("listadoIngresos");
					}
					else
					{
						return accionVerificarIngreso(forma,mundo,usuario,paciente,mapping,request,con,forma.getIngreso()+"");
					}
				}

				else if(estado.equals("generarReporte"))
				{   
					if(!UtilidadTexto.getBoolean(forma.getImprimirFURIPS()) && 
							!UtilidadTexto.getBoolean(forma.getImprimirFURIPS2()))//||forma. numeroReclamacionesAImprimir()<=0)
					{
						ActionErrors errores = new ActionErrors();
						errores.add("errores blanco",new ActionMessage("error.errorEnBlanco","Para realizar la impresión, debe seleccionar al menos una opción de la sección 'Formatos a imprimir'. Por favor verifique"));
						saveErrors(request, errores);
						UtilidadBD.cerrarConexion(con);
						return mapping.findForward("resumen");       
					}
					else
					{
						return this.generarReporte(con, forma, usuario, request,forma.getIngreso(),mapping,paciente);
					}


				}
				 /*-------------------------------------------
				 *  		ESTADO ----> CARGARLISTADO
				  -------------------------------------------*/
				else if(estado.equals("cargarListado"))
				{    HashMap criterios = new HashMap ();
				//criterios.put("estado", ConstantesIntegridadDominio.acronimoEstadoProcesado);
				criterios.put("paciente", paciente.getCodigoPersona());
				forma.setListadoAccidentes(mundo.cargarListadoAccidentesTransito(con, criterios));
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("listado");
				}
				else /*-------------------------------------------
				 *  		ESTADO ----> CARGARACCIDENTE
				  -------------------------------------------*/
				if(estado.equals("cargarAccidente"))
				{ 
					forma.setIngreso(forma.getListadoAccidentes("idingreso_"+forma.getIndex())+"");
					/*
				mundo.guardarAsosico(con, forma, paciente); 
                UtilidadBD.cerrarConexion(con);
                return mapping.findForward("principal");
					 */
					PropertyUtils.copyProperties(forma,mundo.consultarRegistroAccidentesTransitoIngreso(con, forma.getIngreso()+""));
					forma.inicializarOpcionesImpresion();

					if(!UtilidadTexto.isEmpty(forma.getCuenta()))
					{
						Cuenta cuenta= new  Cuenta();
						cuenta.cargarCuenta(con, forma.getCuenta());
						paciente.setCodigoPersona(cuenta.getPaciente().getCodigoPersona());
						paciente.cargar(con,cuenta.getPaciente().getCodigoPersona());
						paciente.cargarPaciente(con, cuenta.getPaciente().getCodigoPersona(), usuario.getCodigoInstitucion(),usuario.getCodigoCentroAtencion()+"");
					}
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("resumen");
				}
				
				/*-------------------------------------------
				 *  		ESTADO ----> ASOCIAR
				  -------------------------------------------*/
				else if(estado.equals("asociar"))
				{ 

					mundo.guardarAsosico(con, forma, paciente); 
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("resumen");

				}
				 /*-------------------------------------------
				 *  		ESTADO ----> VERIFICAR
				  -------------------------------------------*/
				else if(estado.equals("verificar"))
				{ 
					forma.setIngreso(paciente.getCodigoIngreso()+"");
					return accionVerificarIngreso(forma,mundo,usuario,paciente,mapping,request,con,forma.getIngreso()+"");
				}
				else if(estado.equals("verificarIngreso"))
				{ 


					return accionVerificarIngreso(forma,mundo,usuario,paciente,mapping,request,con,forma.getIngreso());

				}
				 /*-------------------------------------------
				 *  		ESTADO ----> GUARDARAMPAROS
				  -------------------------------------------*/
				else if(estado.equals("guardarAmparos"))
				{ 
					DtoRegistroAccidentesTransito dto=new DtoRegistroAccidentesTransito();
					PropertyUtils.copyProperties(dto,forma);
					dto.setFechaGrabacion(UtilidadFecha.getFechaActual());
					dto.setHoraGrabacion(UtilidadFecha.getHoraActual());
					dto.setUsuarioGrabacion(usuario.getLoginUsuario());
					mundo.modificarAmparos(con, dto);
					return mapping.findForward("resumen");
				}
				 /*-------------------------------------------
				 *  		ESTADO ----> GUARDARAMPAROS
				  -------------------------------------------*/
				else if(estado.equals("guardarReclamacion"))
				{ 
					DtoRegistroAccidentesTransito dto=new DtoRegistroAccidentesTransito();
					PropertyUtils.copyProperties(dto,forma);
					dto.setFechaGrabacion(UtilidadFecha.getFechaActual());
					dto.setHoraGrabacion(UtilidadFecha.getHoraActual());
					dto.setUsuarioGrabacion(usuario.getLoginUsuario());
					RegistroAccidentesTransito.modificarReclamacion(con, dto);
					return mapping.findForward("resumen");
				}
				else if(estado.equals("postularPoliza"))
				{
					Cuenta mundoCuenta=new Cuenta();
					mundoCuenta.cargar(con, paciente.getCodigoCuenta()+"");
					boolean tieneConvenioAsociadoAlPaciente=false;
					for(int i=0; i<mundoCuenta.getCuenta().getConvenios().length; i++)
					{
						if(forma.getAseguradora().equals(mundoCuenta.getCuenta().getConvenios()[i].getConvenio().getCodigo()+""))
						{
							forma.setNumeroPoliza(mundoCuenta.getCuenta().getConvenios()[i].getNroPoliza());
							tieneConvenioAsociadoAlPaciente=true;
							break;
						}
					}
					if(!tieneConvenioAsociadoAlPaciente)
					{
						forma.setNumeroPoliza("");
					}
					return mapping.findForward("seccionPoliza");
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
				logger.error("El form no es compatible con el form de RegistroAccidentesTransitoForm");
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
				RegistroAccidentesTransitoForm forma,
				RegistroAccidentesTransito mundo, UsuarioBasico usuario,
				PersonaBasica paciente, ActionMapping mapping, HttpServletRequest request, Connection con, String ingreso) throws Exception 
	{
		ActionForward forwardPaginaErrores=accionValidacionesAcceso(paciente, mapping, request, con,forma.getIngreso(),usuario.getCodigoInstitucionInt());
		if(forwardPaginaErrores!=null)
			return forwardPaginaErrores;
		forma.reset();
		
		forma.setOcultarEncabezado(UtilidadTexto.getBoolean(request.getParameter("ocultarEncabezado")));
		
	
		
		forma.setZonaAccidente(ValoresPorDefecto.getZonaDomicilio(usuario.getCodigoInstitucionInt()));
		PropertyUtils.copyProperties(forma,mundo.consultarRegistroAccidentesTransitoIngreso(con, ingreso+""));
		this.postularPais(con,forma,usuario.getCodigoInstitucionInt());
		forma.setIngreso(ingreso+"");
		
		if(forma.getEstadoRegistro().equals(""))
		{
			forma.setEstadoRegistro(ConstantesIntegridadDominio.acronimoEstadoPendiente);
		}
		
		if(forma.getAseguradora()==null||forma.getAseguradora().trim().equals(""))
		{
			forma.setAseguradora(paciente.getCodigoConvenio()+"");
		}
		
		

		/////////////////////////////////////////////////////////////

		////////////////////////////////////////////////////
		//se valida si se permite modificar o no las secciones,
		//ademas si se muestran desplegadas o no.
		validacionSecciones(con, forma,usuario);
		//////////////////////////////////////////////////////
		
		if(forma.getEstadoRegistro().equals(ConstantesIntegridadDominio.acronimoEstadoProcesado))
		{
			UtilidadBD.cerrarConexion(con);
			return mapping.findForward("resumen");
		}
		
		UtilidadBD.cerrarConexion(con);
	    return mapping.findForward("verificar");
	}




private void validacionSecciones (Connection connection,RegistroAccidentesTransitoForm forma, UsuarioBasico usuario)
{
	logger.info("\nel nuemro del ingreso es--> "+forma.getIngreso());
	if(UtilidadTexto.getBoolean(ValoresPorDefecto.getPermitirRegistrarReclamacionCuentasNoFacturadas(usuario.getCodigoInstitucionInt())))
	{
		forma.setValidacionIngresoFacturado(ConstantesBD.acronimoSi);	
	}
	else
	{
		if (Utilidades.esIngresoFacturado(connection, forma.getIngreso()+"")>0)
			forma.setValidacionIngresoFacturado(ConstantesBD.acronimoSi);
		else
			forma.setValidacionIngresoFacturado(ConstantesBD.acronimoNo);
	}
	
	
	//maneja la seccion desplegable 1
	if (UtilidadTexto.getBoolean(forma.getExistenOtrosVehiAcc()))
		forma.setSecDesplegable1(ConstantesBD.acronimoSi);
	else
		forma.setSecDesplegable1(ConstantesBD.acronimoNo);
	
	if (forma.getValidacionIngresoFacturado().equals(ConstantesBD.acronimoSi))
	{
		forma.setSecDesplegable2(ConstantesBD.acronimoSi);
		if (UtilidadTexto.getBoolean(forma.getEsReclamacion()))
			forma.setSecDesplegable3(ConstantesBD.acronimoSi);
		else
			forma.setSecDesplegable3(ConstantesBD.acronimoNo);
	}
	else
	{
		forma.setSecDesplegable2(ConstantesBD.acronimoNo);
		forma.setSecDesplegable3(ConstantesBD.acronimoNo);
	}
}

/**
 * 		//verifica si valida los amparos
			if (this.modificarAmparos)
			{
				if(!UtilidadCadena.noEsVacio(this.totalFacAmpQx+"") || this.totalFacAmpQx<=0)
					errores.add("zonaAccidente",new ActionMessage("errors.required"," El Total facturado amparo gastos médico quirúrgicos:  "));
				
				if(!UtilidadCadena.noEsVacio(this.totalReclamoAmpQx+"") || this.totalReclamoAmpQx<=0)
					errores.add("zonaAccidente",new ActionMessage("errors.required"," El Total reclamado amparo gastos médico quirúrgicos:  "));
				
				
				if(!UtilidadCadena.noEsVacio(this.totalFacAmpTx+"") || this.totalFacAmpTx<=0)
					errores.add("zonaAccidente",new ActionMessage("errors.required"," El Total facturado amparo gastos de transporte y movilización de la víctima:  "));
				
				
				if(!UtilidadCadena.noEsVacio(this.totalReclamoAmpTx+"") || this.totalReclamoAmpTx<=0)
					errores.add("zonaAccidente",new ActionMessage("errors.required"," El Total reclamado amparo gastos de transporte y movilización de la víctima:  "));
			}
 */



	/**
	 * 
	 * @param con 
	 * @param forma
	 * @param codigoInstitucionInt
	 */
	private void postularPais(Connection con, RegistroAccidentesTransitoForm forma, int codigoInstitucionInt) 
	{
		ParametrizacionInstitucion mundoInstitucion = new ParametrizacionInstitucion();
		mundoInstitucion.cargar(con, codigoInstitucionInt);
		if(UtilidadTexto.isEmpty(forma.getPaisEmpresa()))
			forma.setPaisEmpresa(mundoInstitucion.getPais().getCodigo());
		if(UtilidadTexto.isEmpty(forma.getPaisAccidente()))
			forma.setPaisAccidente(mundoInstitucion.getPais().getCodigo());
		if(UtilidadTexto.isEmpty(forma.getPaisExpedicionIdConductor()))
			forma.setPaisExpedicionIdConductor(mundoInstitucion.getPais().getCodigo());
		if(UtilidadTexto.isEmpty(forma.getPaisConductor()))
			forma.setPaisConductor(ValoresPorDefecto.getPaisResidencia(codigoInstitucionInt).split("-")[0]);
		if(UtilidadTexto.isEmpty(forma.getPaisExpedicionIdDeclarante()))
			forma.setPaisExpedicionIdDeclarante(mundoInstitucion.getPais().getCodigo());
		if(UtilidadTexto.isEmpty(forma.getPaisExpedicionIdTransporta()))
			forma.setPaisExpedicionIdTransporta(mundoInstitucion.getPais().getCodigo());
		if(UtilidadTexto.isEmpty(forma.getPaisProp()))
			forma.setPaisProp(ValoresPorDefecto.getPaisResidencia(codigoInstitucionInt).split("-")[0]);
		if(UtilidadTexto.isEmpty(forma.getPaisTransporta()))
			forma.setPaisTransporta(ValoresPorDefecto.getPaisResidencia(codigoInstitucionInt).split("-")[0]);
		if(UtilidadTexto.isEmpty(forma.getPaisExpedicionIdProp()))
			forma.setPaisExpedicionIdProp(mundoInstitucion.getPais().getCodigo());
	}



	/**
	 * 
	 * @param forma
	 */
	private void accionOrdenar(RegistroAccidentesTransitoForm forma) 
	{
		forma.setEstado("busquedaAvanzada");
		int numReg=Integer.parseInt(forma.getListadoMap("numRegistros")==null?"0":forma.getListadoMap("numRegistros")+"");
		String[] indices={"codigo_", "codigovia_", "cuenta_", "via_","centroatencion_","especialidad_","fechahoraingresoformatobd_","fechahoraingreso_","fechahoraegreso_", "paciente_"};
		forma.setListadoMap(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getListadoMap(),numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setListadoMap("numRegistros",numReg+"");
	}
	
	/**
	 * 
	 * @param dtoOld
	 * @param dtoNew
	 * @param usuario 
	 */
	private void generarLog(DtoRegistroAccidentesTransito dtoOld, DtoRegistroAccidentesTransito dtoNew, UsuarioBasico usuario)
	{
		String log = "";
		log = 		 "\n   ============INFORMACION ORIGINAL=========== " +
			 "\n*  Ingreso [" +dtoOld.getIngreso()+"] " +
			 "\n*  Empresa Trabaja [" +dtoOld.getEmpresaTrabaja()+"] " +
			 "\n*  Direccion Empresa [" +dtoOld.getDireccionEmpresa()+"] " +
			 "\n*  Telefono Empresa [" +dtoOld.getTelefonoEmpresa()+"] " +
			 "\n*  Ciudad Empresa [" +dtoOld.getCiudadEmpresa()+"] " +
			 "\n*  Departamento Empresa [" +dtoOld.getDepartamentoEmpresa()+"] " +
			 "\n*  Ocupante [" +dtoOld.getOcupante()+"] " +
			 "\n*  Condicion Accidentado [" +dtoOld.getCodicionAccidentado()+"] " +
			 "\n*  Resulta Lesionado Al [" +dtoOld.getResultaLesionadoAl()+"] " +
			 "\n*  Placa Vehiculo Ocaciona [" +dtoOld.getPlacaVehiculoOcaciona()+"] " +
			 "\n*  Lugar Accidente [" +dtoOld.getLugarAccidente()+"] " +
			 "\n*  Fecha Accidente [" +dtoOld.getFechaAccidente()+"] " +
			 "\n*  Hora Accidente [" +dtoOld.getHoraAccidente()+"] " +
			 "\n*  Ciudad Accidente [" +dtoOld.getCiudadAccidente()+"] " +
			 "\n*  Departamento Accidente [" +dtoOld.getDepartamentoAccidente()+"] " +
			 "\n*  Zona Accidente [" +dtoOld.getZonaAccidente()+"] " +
			 "\n*  Informacion Accidente [" +dtoOld.getInformacionAccidente()+"] " +
			 "\n*  Marca Vehiculo [" +dtoOld.getMarcaVehiculo()+"] " +
			 "\n*  Placa Vehiculo [" +dtoOld.getPlaca()+"] " +
			 "\n*  Tipo Vehiculo [" +dtoOld.getTipo()+"] " +
			 "\n*  Poliza [" +dtoOld.getPoliza()+"] " +
			 "\n*  Aseguradora [" +dtoOld.getAseguradora()+"] " +
			 "\n*  Fecha Inicial Poliza [" +dtoOld.getFechaInicialPoliza()+"] " +
			 "\n*  Fecha Final Poliza [" +dtoOld.getFechaFinalPoliza()+"] " +
			 "\n*  Primer Apellido Propietario [" +dtoOld.getPrimerApellidoProp()+"] " +
			 "\n*  Segundo Apellido Propietario [" +dtoOld.getSegundoApellidoProp()+"] " +
			 "\n*  Primer Nombre Conductor [" +dtoOld.getPrimerNombreProp()+"] " +
			 "\n*  Segundo Nombre Conductor [" +dtoOld.getSegundoNombreProp()+"] " +
			 "\n*  Tipo Id Propietario [" +dtoOld.getTipoIdProp()+"] " +
			 "\n*  Numero Id Propietario [" +dtoOld.getNumeroIdProp()+"] " +
			 "\n*  Ciudad Expedicion Id Propietario [" +dtoOld.getCiudadExpedicionIdProp()+"] " +
			 "\n*  Departamento Expedicion Id Propietario [" +dtoOld.getDeptoExpedicionIdProp()+"] " +
			 "\n*  Direccion Propietario [" +dtoOld.getDireccionProp()+"] " +
			 "\n*  Telefono Propietario [" +dtoOld.getTelefonoProp()+"] " +
			 "\n*  Ciudad Propietario [" +dtoOld.getCiudadProp()+"] " +
			 "\n*  Departamento Propietario [" +dtoOld.getDeptoProp()+"] " +
			 "\n*  Primer Apellido Conductor [" +dtoOld.getPrimerApellidoConductor()+"] " +
			 "\n*  Segundo Apellido Conductor [" +dtoOld.getSegundoApellidoConductor()+"] " +
			 "\n*  Primer Nombre Conductor [" +dtoOld.getPrimerNombreConductor()+"] " +
			 "\n*  Segundo Nombre Conductor [" +dtoOld.getSegundoNombreConductor()+"] " +
			 "\n*  Tipo Id Conductor [" +dtoOld.getTipoIdConductor()+"] " +
			 "\n*  Numero Id Conductor [" +dtoOld.getNumeroIdConductor()+"] " +
			 "\n*  Ciudad Expedicion ID Conductor [" +dtoOld.getCiuExpedicionIdConductor()+"] " +
			 "\n*  Departamento Expedicion ID Conductor [" +dtoOld.getDepExpedicionIdConductor()+"] " +
			 "\n*  Direccion Conductor [" +dtoOld.getDireccionConductor()+"] " +
			 "\n*  Ciudad Conductor [" +dtoOld.getCiudadConductor()+"] " +
			 "\n*  Departamento Conductor [" +dtoOld.getDepartamentoConductor()+"] " +
			 "\n*  Telefono Conductor [" +dtoOld.getTelefonoConductor()+"] " +
			 "\n*  Apellido Nombre Declarante [" +dtoOld.getApellidoNombreDeclarante()+"] " +
			 "\n*  Tipo ID Declarante [" +dtoOld.getTipoIdDeclarante()+"] " +
			 "\n*  Ciudad Expedicion ID Declarante [" +dtoOld.getCiuExpedicionIdDeclarante()+"] " +
			 "\n*  Departamento Expedicion ID Declarante [" +dtoOld.getDepExpedicionIdDeclarante()+"] " +
			 "\n*  Apellido Nombre Transporta [" +dtoOld.getApellidoNombreTransporta()+"] " +
			 "\n*  Tipo Id Transporta [" +dtoOld.getTipoIdTransporta()+"] " +
			 "\n*  Numero Id Transporta [" +dtoOld.getNumeroIdTransporta()+"] " +
			 "\n*  Ciudad Expedicion Id Transporta [" +dtoOld.getCiudadExpedicionIdTransporta()+"] " +
			 "\n*  Departamento Expedicion Id Transporta [" +dtoOld.getDeptoExpedicionIdTransporta()+"] " +

			 "\n*  Telefono Transporta [" +dtoOld.getTelefonoTransporta()+"] " +
			 "\n*  Direccion Transporta [" +dtoOld.getDireccionTransporta()+"] " +
			 "\n*  Ciudad Tranporta [" +dtoOld.getCiudadTransporta()+"] " +
			 "\n*  Departamento Transporta [" +dtoOld.getDepartamentoTransporta()+"] " +
			 "\n*  Transporta Desde [" +dtoOld.getTransportaVictimaDesde()+"] " +
			 "\n*  Transporta Hasta [" +dtoOld.getTransportaVictimaHasta()+"] " +
			 "\n*  Tipo Transporte [" +dtoOld.getTipoTransporte()+"] " +
			 "\n*  Placa Vehiculo Transporta [" +dtoOld.getPlacaVehiculoTransporta()+"] " +
			 "\n*  Pais Empresa [" +dtoOld.getPaisEmpresa()+"] " +
			 "\n*  Pais Accidente [" +dtoOld.getPaisAccidente()+"] " +
			 "\n*  Pais Expedicion ID Conductor [" +dtoOld.getPaisExpedicionIdConductor()+"] " +
			 "\n*  Pais Conductor [" +dtoOld.getPaisConductor()+"] " +
			 "\n*  Pais Expedicion ID Declarante [" +dtoOld.getPaisExpedicionIdDeclarante()+"] " +
			 "\n*  Pais Expedicion ID Propietario [" +dtoOld.getPaisExpedicionIdProp()+"] " +
			 "\n*  Pais Expedicion ID Transporta [" +dtoOld.getPaisExpedicionIdTransporta()+"] " +
			 "\n*  Pais Propietario [" +dtoOld.getPaisProp()+"] " +
			 "\n*  Pais Transporta [" +dtoOld.getPaisTransporta()+"] " +
			 
			 

			 "\n   ===========INFORMACION MODIFICADA========== 	" +
			 "\n*  Ingreso [" +dtoNew.getIngreso()+"] " +
			 "\n*  Empresa Trabaja [" +dtoNew.getEmpresaTrabaja()+"] " +
			 "\n*  Direccion Empresa [" +dtoNew.getDireccionEmpresa()+"] " +
			 "\n*  Telefono Empresa [" +dtoNew.getTelefonoEmpresa()+"] " +
			 "\n*  Ciudad Empresa [" +dtoNew.getCiudadEmpresa()+"] " +
			 "\n*  Departamento Empresa [" +dtoNew.getDepartamentoEmpresa()+"] " +
			 "\n*  Ocupante [" +dtoNew.getOcupante()+"] " +
			 "\n*  Condicion Accidentado [" +dtoNew.getCodicionAccidentado()+"] " +
			 "\n*  Resulta Lesionado Al [" +dtoNew.getResultaLesionadoAl()+"] " +
			 "\n*  Placa Vehiculo Ocaciona [" +dtoNew.getPlacaVehiculoOcaciona()+"] " +
			 "\n*  Lugar Accidente [" +dtoNew.getLugarAccidente()+"] " +
			 "\n*  Fecha Accidente [" +dtoNew.getFechaAccidente()+"] " +
			 "\n*  Hora Accidente [" +dtoNew.getHoraAccidente()+"] " +
			 "\n*  Ciudad Accidente [" +dtoNew.getCiudadAccidente()+"] " +
			 "\n*  Departamento Accidente [" +dtoNew.getDepartamentoAccidente()+"] " +
			 "\n*  Zona Accidente [" +dtoNew.getZonaAccidente()+"] " +
			 "\n*  Informacion Accidente [" +dtoNew.getInformacionAccidente()+"] " +
			 "\n*  Marca Vehiculo [" +dtoNew.getMarcaVehiculo()+"] " +
			 "\n*  Placa Vehiculo [" +dtoNew.getPlaca()+"] " +
			 "\n*  Tipo Vehiculo [" +dtoNew.getTipo()+"] " +
			 "\n*  Poliza [" +dtoOld.getPoliza()+"] " +
			 "\n*  Aseguradora [" +dtoNew.getAseguradora()+"] " +
			 "\n*  Fecha Inicial Poliza [" +dtoNew.getFechaInicialPoliza()+"] " +
			 "\n*  Fecha Final Poliza [" +dtoNew.getFechaFinalPoliza()+"] " +
			 "\n*  Primer Apellido Propietario [" +dtoNew.getPrimerApellidoProp()+"] " +
			 "\n*  Segundo Apellido Propietario [" +dtoNew.getSegundoApellidoProp()+"] " +
			 "\n*  Primer Nombre Conductor [" +dtoNew.getPrimerNombreProp()+"] " +
			 "\n*  Segundo Nombre Conductor [" +dtoNew.getSegundoNombreProp()+"] " +
			 "\n*  Tipo Id Propietario [" +dtoNew.getTipoIdProp()+"] " +
			 "\n*  Numero Id Propietario [" +dtoNew.getNumeroIdProp()+"] " +
			 "\n*  Ciudad Expedicion Id Propietario [" +dtoNew.getCiudadExpedicionIdProp()+"] " +
			 "\n*  Departamento Expedicion Id Propietario [" +dtoNew.getDeptoExpedicionIdProp()+"] " +
			 "\n*  Direccion Propietario [" +dtoNew.getDireccionProp()+"] " +
			 "\n*  Telefono Propietario [" +dtoNew.getTelefonoProp()+"] " +
			 "\n*  Ciudad Propietario [" +dtoNew.getCiudadProp()+"] " +
			 "\n*  Departamento Propietario [" +dtoNew.getDeptoProp()+"] " +
			 "\n*  Primer Apellido Conductor [" +dtoNew.getPrimerApellidoConductor()+"] " +
			 "\n*  Segundo Apellido Conductor [" +dtoNew.getSegundoApellidoConductor()+"] " +
			 "\n*  Primer Nombre Conductor [" +dtoNew.getPrimerNombreConductor()+"] " +
			 "\n*  Segundo Nombre Conductor [" +dtoNew.getSegundoNombreConductor()+"] " +
			 "\n*  Tipo Id Conductor [" +dtoNew.getTipoIdConductor()+"] " +
			 "\n*  Numero Id Conductor [" +dtoNew.getNumeroIdConductor()+"] " +
			 "\n*  Ciudad Expedicion ID Conductor [" +dtoNew.getCiuExpedicionIdConductor()+"] " +
			 "\n*  Departamento Expedicion ID Conductor [" +dtoNew.getDepExpedicionIdConductor()+"] " +
			 "\n*  Direccion Conductor [" +dtoNew.getDireccionConductor()+"] " +
			 "\n*  Ciudad Conductor [" +dtoNew.getCiudadConductor()+"] " +
			 "\n*  Departamento Conductor [" +dtoNew.getDepartamentoConductor()+"] " +
			 "\n*  Telefono Conductor [" +dtoNew.getTelefonoConductor()+"] " +
			 "\n*  Apellido Nombre Declarante [" +dtoNew.getApellidoNombreDeclarante()+"] " +
			 "\n*  Tipo ID Declarante [" +dtoNew.getTipoIdDeclarante()+"] " +
			 "\n*  Ciudad Expedicion ID Declarante [" +dtoNew.getCiuExpedicionIdDeclarante()+"] " +
			 "\n*  Departamento Expedicion ID Declarante [" +dtoNew.getDepExpedicionIdDeclarante()+"] " +
			 "\n*  Poliza [" +dtoNew.getPoliza()+"] " +
			 "\n*  Apellido Nombre Transporta [" +dtoNew.getApellidoNombreTransporta()+"] " +
			 "\n*  Tipo Id Transporta [" +dtoNew.getTipoIdTransporta()+"] " +
			 "\n*  Numero Id Transporta [" +dtoNew.getNumeroIdTransporta()+"] " +
			 "\n*  Ciudad Expedicion Id Transporta [" +dtoNew.getCiudadExpedicionIdTransporta()+"] " +
			 "\n*  Departamento Expedicion Id Transporta [" +dtoNew.getDeptoExpedicionIdTransporta()+"] " +
			 "\n*  Telefono Transporta [" +dtoNew.getTelefonoTransporta()+"] " +
			 "\n*  Direccion Transporta [" +dtoNew.getDireccionTransporta()+"] " +
			 "\n*  Ciudad Tranporta [" +dtoNew.getCiudadTransporta()+"] " +
			 "\n*  Departamento Transporta [" +dtoNew.getDepartamentoTransporta()+"] " +
			 "\n*  Transporta Desde [" +dtoNew.getTransportaVictimaDesde()+"] " +
			 "\n*  Transporta Hasta [" +dtoNew.getTransportaVictimaHasta()+"] " +
			 "\n*  Tipo Transporte [" +dtoNew.getTipoTransporte()+"] " +
			 "\n*  Placa Vehiculo Transporta [" +dtoNew.getPlacaVehiculoTransporta()+"] " +
			 "\n*  Pais Empresa [" +dtoNew.getPaisEmpresa()+"] " +
			 "\n*  Pais Accidente [" +dtoNew.getPaisAccidente()+"] " +
			 "\n*  Pais Expedicion ID Conductor [" +dtoNew.getPaisExpedicionIdConductor()+"] " +
			 "\n*  Pais Conductor [" +dtoNew.getPaisConductor()+"] " +
			 "\n*  Pais Expedicion ID Declarante [" +dtoNew.getPaisExpedicionIdDeclarante()+"] " +
			 "\n*  Pais Expedicion ID Propietario [" +dtoNew.getPaisExpedicionIdProp()+"] " +
			 "\n*  Pais Expedicion ID Transporta [" +dtoNew.getPaisExpedicionIdTransporta()+"] " +
			 "\n*  Pais Propietario [" +dtoNew.getPaisProp()+"] " +
			 "\n*  Pais Transporta [" +dtoNew.getPaisTransporta()+"] " +
			 
			 "\n========================================================\n\n\n ";
		LogsAxioma.enviarLog(ConstantesBD.logRegistroAccidentesTransitoCodigo,log,ConstantesBD.tipoRegistroLogModificacion,usuario.getLoginUsuario());
	}

	/**
	 * Inicializa el acceso en la parte de consulta/impresion del registro de accidentes
	 * @param usuario 
	 * @param forma 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param forward
	 * @return
	 */
	private ActionForward accionEmpezarConsulta(	RegistroAccidentesTransitoForm forma, UsuarioBasico usuario, ActionMapping mapping, 
			 										Connection con,
			 										String forward
			 									)
	{

		this.postularPais(con,forma,usuario.getCodigoInstitucionInt());

		UtilidadBD.closeConnection(con);
		return mapping.findForward(forward);		
	}

	/**
	 * Inicializa el acceso en la parte de consulta/impresion del registro de accidentes
	 * @param forma
	 * @param usuario 
	 * @param mapping
	 * @param con
	 * @param forward
	 * @return
	 */
	private ActionForward accionBusquedaAvanzada(	RegistroAccidentesTransitoForm forma, 
				 										UsuarioBasico usuario, ActionMapping mapping, 
				 										Connection con,
				 										RegistroAccidentesTransito mundo
				 									  )
	{
		forma.setListadoMap(mundo.busquedaAvanzada(con, forma.getCriteriosBusquedaMap()));
		this.postularPais(con,forma,usuario.getCodigoInstitucionInt());
		UtilidadBD.closeConnection(con);
		return mapping.findForward("busquedaAvanzada");
	}
	
	/**
	 * validaciones de acceso
	 * @param paciente
	 * @param mapping
	 * @param request
	 * @param con
	 * @param ingreso 
	 * @return
	 */
	protected ActionForward accionValidacionesAcceso(	PersonaBasica paciente, 
													ActionMapping mapping, 
													HttpServletRequest request, 
													Connection con, String ingreso,int institucion)
	{
		if(paciente.getCodigoPersona()<1)
		{
		    return ComunAction.accionSalirCasoError(mapping, request, con, logger, "paciente null o sin  id", "errors.paciente.noCargado", true);
		}
		if(!UtilidadTexto.getBoolean(ValoresPorDefecto.getPermitirFacturarReclamarCuentasConRegistroPendientes(institucion)))
		{
			if(paciente.getCodigoCuenta()<1)
			{
			    return ComunAction.accionSalirCasoError(mapping, request, con, logger, "PACIENTE SIN CUENTA ACTIVA", "errors.paciente.cuentaNoAbierta", true);
			}
		}
		if(!UtilidadValidacion.esAccidenteTransito(con, Utilidades.convertirAEntero(ingreso)))
		{
		    return ComunAction.accionSalirCasoError(mapping, request, con, logger, "NO ACCIDENTE TRANSITO", "errir.noAccidenteTransito", true);
		}
		return null;
	}	
	
	
	 /**
     * Metodo para generar los reportes
     * @param tipoReporte
     * @param con
     * @param forma
     * @param usuario
     * @param request
	 * @param mapping 
	 * @param paciente 
	 * @return 
     */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private ActionForward generarReporte(Connection con, 
								RegistroAccidentesTransitoForm forma,
								UsuarioBasico usuario,
								HttpServletRequest request, String ingreso, ActionMapping mapping, PersonaBasica paciente ) 
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
						if(UtilidadTexto.getBoolean(forma.getImprimirFURIPS()))
						{
							if(!UtilidadTexto.isEmpty(reportes))
								reportes=reportes+ConstantesBD.separadorSplit;
							
							reportes+=generarReporteFurips(con, forma, Utilidades.convertirAEntero(reclamacion.getConsecutivoFactura()), Utilidades.convertirAEntero(forma.getIngreso()),reclamacion.getCodigoPk(), usuario, request);
							
							
						}
					}
				}
				if (!imprimirReclamacion) {
					reportes = generarInformeAccidenteTransito(con, forma,Utilidades.convertirAEntero(forma.getIngreso()), usuario, request);
				}
			} else {
				reportes = generarInformeAccidenteTransito(con, forma,Utilidades.convertirAEntero(forma.getIngreso()), usuario, request);
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
			mapaBusqueda.put("tipomanual",ConstantesBD.codigoTarifarioCups+"");
			mapaBusqueda.put("nrofolios_"+PlanosFURIPS.archivosEnum.Furips1+"",forma.getNumeroFoliosFURIPS());
			
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
	 * Método que permite generar el Reporte FURIPS
	 * @param con
	 * @param forma
	 * @param dto 
	 * @param usuario
	 * @param request
	 */
	private String generarReporteFurips(Connection con, RegistroAccidentesTransitoForm forma, int factura, int ingreso,int codigoReclamacion, UsuarioBasico usuario, HttpServletRequest request) 
	{
		DesignEngineApi comp;
		comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"fosyga/","FormatoFURIPS.rptdesign");
        comp.obtenerComponentesDataSet("dataSet");
        String oldQuery = comp.obtenerQueryDataSet();
        String newQuery = ConsultasBirt.furipsAccidenteDeTransito(factura, UtilidadesManejoPaciente.obtenerCodigoMedicoTratante(con, ingreso),codigoReclamacion);
        comp.modificarQueryDataSet(newQuery);
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);

        //por ultimo se modifica la conexion a BD
        comp.updateJDBCParameters(newPathReport);
        
        return newPathReport;
        
	}
	
	/**
	 * Método que permite generar el Reporte FURIPS
	 * @param con
	 * @param forma
	 * @param dto 
	 * @param usuario
	 * @param request
	 */
	private String generarInformeAccidenteTransito(Connection con, RegistroAccidentesTransitoForm forma, int ingreso, UsuarioBasico usuario, HttpServletRequest request) 
	{
		DesignEngineApi comp;
		comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"fosyga/","FormatoFURIPS.rptdesign");
        comp.obtenerComponentesDataSet("dataSet");
        String oldQuery = comp.obtenerQueryDataSet();
        String newQuery = ConsultasBirt.informeAccidenteDeTransito(ingreso, UtilidadesManejoPaciente.obtenerCodigoMedicoTratante(con, ingreso));
        comp.modificarQueryDataSet(newQuery);
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);

        //por ultimo se modifica la conexion a BD
        comp.updateJDBCParameters(newPathReport);
        
        return newPathReport;
        
	}
	
	
	/**
	 * 
	 * @param forma
	 * @param paciente
	 */
	private void llenarDatosImpresion(RegistroAccidentesTransitoForm forma, PersonaBasica paciente)
	{
		forma.setCodigoViaIngreso(paciente.getCodigoUltimaViaIngreso());
	}
	

}
