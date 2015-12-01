package com.princetonsa.mundo.odontologia;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.EnumSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosDouble;
import util.InfoDatosInt;
import util.InfoDatosString;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadFileUpload;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.odontologia.InfoDetallePlanTramiento;
import util.odontologia.InfoHallazgoSuperficie;
import util.odontologia.InfoOdontograma;
import util.odontologia.InfoPlanTratamiento;
import util.odontologia.InfoProgramaServicioPlan;
import util.odontologia.InfoServicios;

import com.mercury.mundo.odontologia.Odontograma;
import com.princetonsa.dto.odontologia.DtoDetallePlanTratamiento;
import com.princetonsa.dto.odontologia.DtoInfoFechaUsuario;
import com.princetonsa.dto.odontologia.DtoLogDetPlanTratamiento;
import com.princetonsa.dto.odontologia.DtoLogPlanTratamiento;
import com.princetonsa.dto.odontologia.DtoLogProgServPlant;
import com.princetonsa.dto.odontologia.DtoOdontograma;
import com.princetonsa.dto.odontologia.DtoPlanTratamientoOdo;
import com.princetonsa.dto.odontologia.DtoProgHallazgoPieza;
import com.princetonsa.dto.odontologia.DtoProgramasServiciosPlanT;
import com.princetonsa.dto.odontologia.DtoSectorSuperficieCuadrante;
import com.princetonsa.dto.odontologia.DtoSuperficiesPorPrograma;
import com.princetonsa.enu.general.CarpetasArchivos;
import com.princetonsa.enums.odontologia.ColoresPlanTratamiento;
import com.princetonsa.mundo.UsuarioBasico;

/***
 * 
 * @author axioma
 *
 */
public class ComponenteOdontograma
{
	public static final int codigoTipoHallazgoSuper = 1;
	public static final int codigoTipoHallazgoDiente = 2;
	public static final int codigoTipoHallazgoBoca = 3;
	public static final String codigoEstadoGuardarOdonto = "guardarOdont";
	public static final String forwardOdontVerSoloOdonto = "seccionOdontoPopUp";
	private static Logger logger = Logger.getLogger(ComponenteOdontograma.class);
	
	private ActionErrors errores;
	private String forwardOdont;
	private String estadoInterno;
	private Connection conInterna;
	
	private String porConfirmar;
	
	/**
	 * 
	 * */
	public ComponenteOdontograma()
	{
		reset();
	}
	
	public void reset()
	{
		forwardOdont = "";
		errores = new ActionErrors();
		estadoInterno = "";
		conInterna = null;
		porConfirmar = null;
	}

	/**
	 * M&eacute;todo encargado de centralizar las acciones del odontograma
	 * Maneja un estado interno
	 * @param info Objeto con la informaci&oacute;n necesaria para cada acci&oacute;n
	 * @param estadoExterno Estado que se va a manejar en el flujo
	 * @param codigoValoracion C&oacute;digo de la valoraci&oacute;n o de la evoluci&oacute;n, utilizado para cargar las im&aacute;genes del componente Odontograma
	 * @return {@link InfoOdontograma} Objeto con toda la informaci&oacute;n del odontograma.
	 */
	public InfoOdontograma centralAcciones(InfoOdontograma info,String estadoExterno, BigDecimal codigoValoracion)
	{
		info.getMostrarMensajeProceExito().setIndicador(false);
		estadoInterno = estadoExterno;
		logger.info("\n\n**************estado interno: "+estadoInterno);
		if(estadoInterno.equals("empezar"))
		{
			info = accionCargarOdontograma(info, codigoValoracion);
			this.forwardOdont = "";
		}
		else if(estadoInterno.equals("empezarPopUp"))
		{
			info.setSoloModifInfoNueva(true);
			info = accionCargarOdontograma(info, codigoValoracion);
			this.forwardOdont = "";
		}
		//Seccion Plan Tratamiento
		else if(estadoInterno.equals("actualizarOdont"))//val
		{
			igualarPlanTratamientoAOdontograma(info);
			cargarProgramasVariasSuperficies(info, ConstantesIntegridadDominio.acronimoDetalle);
			this.forwardOdont = "retazoPlanT";
		}
		else if(estadoInterno.equals("guardarOdont"))//val
		{
			info = accionGuardarOdont(info, Boolean.FALSE /* No aplica para inclusion */);  // GUARDA LA INFORMACION DEL ODONTOGRAMA Y PLAN DE TRATAMIENTO
			this.forwardOdont = "";
		}
		else if(estadoInterno.equals("guardarOdontConexion")) //evol
		{
			this.conInterna = UtilidadBD.abrirConexion();
			UtilidadBD.iniciarTransaccion(this.conInterna);
			/*
			 * Se debe generar automaticamente una solicitud de inclusion para los 
			 * programas y servicios del registro de el link de nuevos hallazgos.
			 */
			boolean aplicaInclusion=Boolean.TRUE; // Este atributos esta para identificar si genera automaticamente la inclusion
			//solo aplica para evolucion
			info = accionGuardarOdont(info,aplicaInclusion);
			
			if(this.errores.isEmpty())
			{
				info.getMostrarMensajeProceExito().setIndicador(true);
				info.getMostrarMensajeProceExito().setDescripcion(ConstantesBD.acronimoSi);
				UtilidadBD.finalizarTransaccion(this.conInterna);
				UtilidadBD.closeConnection(this.conInterna);
				info.setSoloModifInfoNueva(true);
				info = accionCargarOdontograma(info, codigoValoracion);
			}
			else{
				UtilidadBD.abortarTransaccion(this.conInterna);
				UtilidadBD.closeConnection(this.conInterna);
				info.setSoloModifInfoNueva(true);				
			}
			
			this.forwardOdont = "";
		}
		
		else if(estadoInterno.equals("elimiProgServPlaTra"))
		{
			info = accionEliminarProgServPlanTratamiento(info);
			this.forwardOdont = "retazoPlanT";
		}
		else if(estadoInterno.equals("elimiProgServSecOtro"))
		{
			info = accionEliminarProgServSeccionOtros(info);
			this.forwardOdont = "retazoPlanTOtros";
		}
		else if(estadoInterno.equals("elimiProgServSecBoca"))
		{
			info = accionEliminarProgServSeccionBoca(info);
			this.forwardOdont = "retazoPlanTBoca";
		}
		
		else if(estadoInterno.equals("centralBusquedaOdoPlanTra") 
					|| estadoInterno.equals("centralBusquedaOdoOtro") 
						|| estadoInterno.equals("centralBusquedaOdoBoca"))
		{
			if(estadoInterno.equals("centralBusquedaOdoPlanTra"))
				accionAbrirBusquedaOdo(info,ConstantesIntegridadDominio.acronimoDetalle);
			else if(estadoInterno.equals("centralBusquedaOdoOtro"))
				accionAbrirBusquedaOdo(info,ConstantesIntegridadDominio.acronimoOtro);
			else if(estadoInterno.equals("centralBusquedaOdoBoca"))
					accionAbrirBusquedaOdo(info,ConstantesIntegridadDominio.acronimoBoca);
			
			this.forwardOdont = "centralBusquedaOdo";
		}
		else if(estadoInterno.equals("addPrgServPlanTr")) 
		{
			int numeroSuperficies=accionAddServPlanTr(info,ConstantesIntegridadDominio.acronimoDetalle);
			if(numeroSuperficies>1)
			{
				this.estadoInterno = "centralBusquedaOdoPlanTra";
				this.forwardOdont = "seleccionSuperficies";
			}
			else if(numeroSuperficies==1)
			{
				this.estadoInterno = "actualizarPLanTrat";
				this.forwardOdont = "actualizarDetallePlanT";
			}
			else // En este caso hay error en la selección de las superficies
			{
				this.estadoInterno = "centralBusquedaOdoPlanTra";
				this.forwardOdont = "seleccionSuperficies";
			}
		}
		else if(estadoInterno.equals("addPrgServOtro"))
		{
			int numeroSuperficies=accionAddServPlanTr(info,ConstantesIntegridadDominio.acronimoOtro);
			if(numeroSuperficies>1)
			{
				this.estadoInterno = "centralBusquedaOdoPlanTra";
				this.forwardOdont = "seleccionSuperficiesOtros";
			}
			else if(numeroSuperficies==1)
			{
				this.estadoInterno = "actualizarPLanTrat";
				this.forwardOdont = "actualizarDetallePlanT";
			}
			else // En este caso hay error en la selección de las superficies
			{
				this.estadoInterno = "centralBusquedaOdoPlanTra";
				this.forwardOdont = "seleccionSuperficiesOtros";
			}
		}
		else if(estadoInterno.equals("addPrgServBoca"))
		{
			if(accionAddServPlanTr(info,ConstantesIntegridadDominio.acronimoBoca)==ConstantesBD.codigoNuncaValido)
			{
				this.estadoInterno = "centralBusquedaOdoPlanTra";
				this.forwardOdont = "centralBusquedaOdo";
			}
			else
			{
				this.estadoInterno = "actualizarPLanTrat";
				this.forwardOdont = "actualizarDetallePlanT";
			}
		}
		else if(estadoInterno.equals("actualizarOdonto"))
		{
			this.forwardOdont = "";
		}
		//Seccion Otros Hallazgos
		else if(estadoInterno.equals("addHallazgoOtro"))
		{
			info = accionAddHallazgoOtro(info);
			this.forwardOdont = "";
		}
		else if(estadoInterno.equals("eliminarHallazgoOtro"))
		{
			info = accionEliminarHallazgoOtro(info);
			this.forwardOdont = "";
		}
		else if(estadoInterno.equals("nuevoOtroHallazgo"))
		{
			info = accionNuevoOtroHallazgo(info);
			this.forwardOdont = "";
		}
		//Seccion Hallazgos Boca
		else if(estadoInterno.equals("nuevoHallazgoBoca"))
		{
			info = accionNuevoHallazgoBoca(info);
			this.forwardOdont = "";
		}
		else if(estadoInterno.equals("addHallazgoBoca"))
		{
			info = accionAddHallazgoBoca(info);
			this.forwardOdont = "";
		}
		else if(estadoInterno.equals("accionAddHallazgoBoca"))
		{
			info = accionAddHallazgoBoca(info);
			this.forwardOdont = "";
		}
		else if(estadoInterno.equals("eliminarHallazgoBoca"))
		{
			info = accionEliminarHallazgoBoca(info);
			this.forwardOdont = "";
		}
		else if(estadoInterno.equals("refreshOtroHall"))
		{
			this.forwardOdont = "retazoOtroHall"; 
		}
		else if(estadoInterno.equals("seleccionarSuperficieOtros"))
		{
			info.getInfoPlanTrata().getSeccionOtrosHallazgos().get(info.getIndicePiezaSeleccionada()).getDetalleSuperficie().get(0).getSuperficieOPCIONAL().setCodigo(info.getSuperficieSeleccionada());
			info.getInfoPlanTrata().getSeccionOtrosHallazgos().get(info.getIndicePiezaSeleccionada()).getDetalleSuperficie().get(0).getSuperficieOPCIONAL().setNombre(info.getNombreSuperficie(info.getSuperficieSeleccionada(), info.getInfoPlanTrata().getSeccionOtrosHallazgos().get(info.getIndicePiezaSeleccionada()).getPieza().getCodigo()));
			this.forwardOdont = "retazoOtroHall";
		}
		return info;
	}

	/**
	 * 
	 * */
	public InfoOdontograma accionEmpezar(
								InfoOdontograma info,
								int codigoPaciente,
								int codigoIngreso,
								int edadPaciente,
								int codigoCita,
								int codigoValoracion,
								int codigoEvolucion,
								String indicadorPlanTrata,
								String indicadorOdonto,
								UsuarioBasico usuarioActual,
								String estado,
								String pathNombreContexo,
								boolean esMostrarSoloOdonto)
	{
		info.setInstitucion(usuarioActual.getCodigoInstitucionInt());
		info.setCodigoPaciente(codigoPaciente);
		info.setIdIngresoPaciente(codigoIngreso);
		info.setEdadPaciente(edadPaciente);
		info.setCodigoCita(codigoCita);
		info.setCodigoValoracion(codigoValoracion);
		info.setCodigoEvolucion(codigoEvolucion);
		info.setIndicadorPlantTratamiento(indicadorPlanTrata);
		info.setIndicadorOdontograma(indicadorOdonto);
		info.setCodigoMedico(usuarioActual.getCodigoPersona());
		info.getUsuarioActual().setUsuarioModifica(usuarioActual.getLoginUsuario());
		info.setCodigoCentroAtencion(usuarioActual.getCodigoCentroAtencion());
		info.setPathNombreContexo(pathNombreContexo);
		info.setMostrarSoloOdontograma(esMostrarSoloOdonto);
		
		info.setXmlHallazgoDiente(HallazgosOdontologicos.busquedaConvencionesHallagosXML(info.getInstitucion(),ConstantesIntegridadDominio.acronimoAplicaADiente,info.getPathNombreContexo()));		
		info.setXmlHallazgoSuperficie(HallazgosOdontologicos.busquedaConvencionesHallagosXML(info.getInstitucion(),ConstantesIntegridadDominio.acronimoAplicaASuperficie,info.getPathNombreContexo()));
		
		info.setArrayHallazgosDiente(HallazgosOdontologicos.busquedaConvencionesHallagos(info.getInstitucion(),ConstantesIntegridadDominio.acronimoAplicaADiente ,true,true));
		info.setArrayHallazgosSuperficie(HallazgosOdontologicos.busquedaConvencionesHallagos(info.getInstitucion(),ConstantesIntegridadDominio.acronimoAplicaASuperficie ,true,true));
		info.setArrayHallazgosBoca(HallazgosOdontologicos.busquedaConvencionesHallagos(info.getInstitucion(),ConstantesIntegridadDominio.acronimoAplicaABoca ,false,false));
		
		InfoDatosString respuesta = iniciarValidacionEdades(info.getInstitucion(),info.getEdadPaciente());
		info.setActivoDienteAdulto(respuesta.getId());
		info.setActivoDienteNino(respuesta.getDescripcion());
		
		info.setEsBuscarPorPrograma(ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(info.getInstitucion()));
		
		//Carga las Superficies del diente
		info.setArraySuperficies(PlanTratamiento.cargarSuperficiesDiente(info.getInstitucion()));
		
		info.getMostrarMensajeProceExito().setDescripcion("");
		info.getMostrarMensajeProceExito().setIndicador(false);
		
		Log4JManager.info("codigo valoracion "+codigoValoracion);
		
		return centralAcciones(info,estado,new BigDecimal(codigoValoracion));
	}
	
	/**
	 * Realiza la eliminación de los programas y servicios en la sección de Plan de Tratamiento
	 * @param InfoOdontograma info
	 * */
	private InfoOdontograma accionEliminarProgServPlanTratamiento(InfoOdontograma info)
	{
		
		if(info.getIndicador1() >= 0 && 
			info.getIndicador2() >= 0 && 
				info.getIndicador3() >= 0)
		{
			
			if(info.getInfoPlanTrata().getSeccionHallazgosDetalle().size() > 0)
			{
				info.getInfoPlanTrata().getSeccionHallazgosDetalle().get(info.getIndicador1()).getDetalleSuperficie().get(info.getIndicador2()).getProgramasOservicios().get(info.getIndicador3()).getExisteBD().setActivo(false);
				
				/*
				 * Se utiliza este Arraylist para quitar los colores de letra de los programas eliminados
				 */
				ArrayList<InfoProgramaServicioPlan> listaEliminados=new ArrayList<InfoProgramaServicioPlan>();
/*
				for(int l=0;l<info.getInfoPlanTrata().getSeccionHallazgosDetalle().size();l++)
				{				  
					if(info.getInfoPlanTrata().getSeccionHallazgosDetalle().get(l).getDetalleSuperficie().size()>0)
					{				
						for(int j=0;j<info.getInfoPlanTrata().getSeccionHallazgosDetalle().get(l).getDetalleSuperficie().size();j++)
						{
							if(info.getInfoPlanTrata().getSeccionHallazgosDetalle().get(l).getDetalleSuperficie().get(j).getCodigoPkDetalle().intValue()==info.getInfoPlanTrata().getSeccionHallazgosDetalle().get(info.getIndicador1()).getDetalleSuperficie().get(info.getIndicador2()).getCodigoPkDetalle().intValue())
							{								
								for(int i=0;i<info.getInfoPlanTrata().getSeccionHallazgosDetalle().get(l).getDetalleSuperficie().get(j).getProgramasOservicios().size();i++)
								{
									if(info.getInfoPlanTrata().getSeccionHallazgosDetalle().get(l).getDetalleSuperficie().get(j).getProgramasOservicios().get(i).getCodigoPkProgramaServicio().intValue()  == info.getInfoPlanTrata().getSeccionHallazgosDetalle().get(info.getIndicador1()).getDetalleSuperficie().get(info.getIndicador2()).getProgramasOservicios().get(info.getIndicador3()).getCodigoPkProgramaServicio().intValue())
									{
										// Si el color de la letra es igua, entonces es porque fue seleccionado el mismo programa para varias superficies
										InfoProgramaServicioPlan programa=info.getInfoPlanTrata().getSeccionHallazgosDetalle().get(info.getIndicador1()).getDetalleSuperficie().get(j).getProgramasOservicios().get(i);
										if(programa.getProgHallazgoPieza().getColorLetra().equals(info.getInfoPlanTrata().getSeccionHallazgosDetalle().get(info.getIndicador1()).getDetalleSuperficie().get(info.getIndicador2()).getProgramasOservicios().get(info.getIndicador3()).getProgHallazgoPieza().getColorLetra()))
										{
											programa.getExisteBD().setActivo(false);
											listaEliminados.add(programa);
										}
									}
								}
							}						
						}
					}
				}
*/
				InfoDetallePlanTramiento pieza=info.getInfoPlanTrata().getSeccionHallazgosDetalle().get(info.getIndicador1());
				InfoProgramaServicioPlan programaSeleccionado=info.getInfoPlanTrata().getSeccionHallazgosDetalle().get(info.getIndicador1()).getDetalleSuperficie().get(info.getIndicador2()).getProgramasOservicios().get(info.getIndicador3());
				/*
				 * Si el programa aplica para más de una superficie, se buscan las superficies a las cuales está relacionado
				 */
				if(programaSeleccionado.getNumeroSuperficies()>1)
				{
					/*
					 * Se iteran las superficies para verificar si tienen asociado el programa
					 */
					for(InfoHallazgoSuperficie superficie:pieza.getDetalleSuperficie())
					{
						/*
						 * Solamente ese evalúa si la superficie es activa
						 */
						if(superficie.getExisteBD().isActivo())
						{
							for(int j=0;j<superficie.getProgramasOservicios().size(); j++)
							{
								InfoProgramaServicioPlan programa=superficie.getProgramasOservicios().get(j);
								/*
								 * Solamente se evalúa si el programa está activo
								 */
								if(programa.getExisteBD().isActivo())
								{
									if(programa.getCodigoPkProgramaServicio().intValue()  == programaSeleccionado.getCodigoPkProgramaServicio().intValue())
									{
										// Si el color de la letra es igual, entonces es porque fue seleccionado el mismo programa para varias superficies
										if(programa.getProgHallazgoPieza().getColorLetra().equals(programaSeleccionado.getProgHallazgoPieza().getColorLetra()))
										{
											programa.getExisteBD().setActivo(false);
											listaEliminados.add(programa);
										}
									}
								}
							}
						}
					}
				}
				/*
				 * Si es de 1 o 0 (Diente) superficies entonces se elimina de una sin iterar 
				 */
				else
				{
					programaSeleccionado.getExisteBD().setActivo(false);
				}
				/*
				 * Quito los colores de letra
				 */
				for(InfoProgramaServicioPlan programa:listaEliminados)
				{
					programa.getProgHallazgoPieza().setColorLetra("");
				}
			}
		}
		
		return info;
	}
	
	/**
	 * 
	 * @param info
	 * @return
	 */
	private InfoOdontograma accionEliminarProgServSeccionOtros(InfoOdontograma info)
	{
		
		if(info.getIndicador1() >= 0 && 
			info.getIndicador2() >= 0 && 
				info.getIndicador3() >= 0)
		{
			
			if(info.getInfoPlanTrata().getSeccionOtrosHallazgos().size() > 0)
			{
				info.getInfoPlanTrata().getSeccionOtrosHallazgos().get(info.getIndicador1()).
				getDetalleSuperficie().get(info.getIndicador2()).getProgramasOservicios().get(info.getIndicador3()).
					getExisteBD().setActivo(false);

				/*
				 * Se toma la pieza eliminada para compararla con las demás
				 */
				InfoDetallePlanTramiento piezaEliminada=info.getInfoPlanTrata().getSeccionOtrosHallazgos().get(info.getIndicador1());
				
				/*
				 * Se toma la superficie del programa que se eliminó para tenerlo como referencia
				 */
				InfoHallazgoSuperficie superficieEliminada=piezaEliminada.getDetalleSuperficie().get(info.getIndicador2());

				/*
				 * Se toma el programa que se eliminó para tenerlo como referencia
				 */
				InfoProgramaServicioPlan programaEliminado=superficieEliminada.getProgramasOservicios().get(info.getIndicador3());
				
				/*
				 * Se inactiva el programa
				 */
				programaEliminado.getExisteBD().setActivo(false);

				/*
				 * Si el número de superficies es mayor a 1, se busca el programa relacionado
				 */
				if(programaEliminado.getNumeroSuperficies()>1)
				{
					/*
					 * Se iteran todos los hallazgos existentes
					 */
					for(InfoDetallePlanTramiento detalle:info.getInfoPlanTrata().getSeccionOtrosHallazgos())
					{
						/*
						 * Si la pieza es la misma se puede evaluar la eliminación
						 */
						if(detalle.getPieza().getCodigo()==piezaEliminada.getPieza().getCodigo())
						{
							/*
							 * Si el detalle es activo
							 */
							if(detalle.getExisteBD().isActivo())
							{
								InfoHallazgoSuperficie superficieInterna=detalle.getDetalleSuperficie().get(0);

								/*
								 * Si es el mismo hallazgo, puedo verificar el programa
								 */
								if(superficieEliminada.getHallazgoREQUERIDO().getCodigo()==superficieInterna.getHallazgoREQUERIDO().getCodigo())
								{
									/*
									 * Se iteran los programas de la primera superficie, ya que 
									 * al ser de la sección otros, solamente hay detalle de una superfice 
									 */
									for(InfoProgramaServicioPlan programaInterno:detalle.getDetalleSuperficie().get(0).getProgramasOservicios())
									{
										/*
										 * Se iteran los programas de la superficie
										 */
										if(programaInterno.getExisteBD().isActivo())
										{
											/*
											 * Si es el mismo programa entonces evalúo la eliminación
											 */
											if(programaInterno.getCodigoPkProgramaServicio().intValue()==programaEliminado.getCodigoPkProgramaServicio().intValue())
											{
												/*
												 * Si el programa seleccionado tiene el mismo código programa y el mismo color de letra
												 * es porque pertenecen al mismo programa, por lo tanto se elimina
												 */
												if(programaInterno.getProgHallazgoPieza().getColorLetra().equals(programaEliminado.getProgHallazgoPieza().getColorLetra()))
												{
													/*
													 * Se inactiva el programa
													 */
													programaInterno.getExisteBD().setActivo(false);
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		
		return info;
	}
	
	private InfoOdontograma accionEliminarProgServSeccionBoca(InfoOdontograma info)
	{
		
		if(info.getIndicador1() >= 0 && 
				info.getIndicador3() >= 0)
		{
			if(info.getInfoPlanTrata().getSeccionHallazgosBoca().size() > 0)
			{
				info.getInfoPlanTrata().getSeccionHallazgosBoca().get(info.getIndicador1()).getProgramasOservicios().get(info.getIndicador3()).
						getExisteBD().setActivo(false);
			}
		}
		
		return info;
	}
	
//	/**
//	 * Realiza Validaciones Generales antes de guardar la informacion
//	 * @param InfoOdontograma info
//	 * */
//	private void validarGuardarInformacion(InfoOdontograma info)
//	{
//		boolean encontroDiente = false;
//		String ubicacion = "";
//		int reg = 1;
//		
//		//SECCION DE PLAN DE TRATAMIENTO
//		//Verifica que los hallazgos tengan clasificación
//		for(InfoDetallePlanTramiento pieza:info.getInfoPlanTrata().getSeccionHallazgosDetalle())
//		{
//			if(pieza.getExisteBD().isActivo())
//			{
//				encontroDiente = true;
//				for(InfoHallazgoSuperficie superficie:pieza.getDetalleSuperficie())
//				{
//					if(superficie.getExisteBD().isActivo())
//					{
//						if(superficie.getSuperficieOPCIONAL().getCodigo() > 0)
//						{
//							ubicacion = " de la Superficie "+superficie.getSuperficieOPCIONAL().getNombre()+" en la Pieza Dental "+pieza.getPieza().getCodigo()+" de la Sección Plan Tratamiento Incial";
//						}
//						else
//						{
//							ubicacion = " del diente "+superficie.getSuperficieOPCIONAL().getNombre()+" en la Pieza Dental "+pieza.getPieza().getCodigo()+" de la Sección Plan Tratamiento Incial";
//						}
//
//						/*
//						 * Tarea 149277
//						 * No hacer requerida la clasificación para diente sano
//						 */
//						if(superficie.getClasificacion().getValue()!=null && superficie.getClasificacion().getValue().equals("") && (porConfirmar==null || porConfirmar.equals(ConstantesBD.acronimoNo))  && superficie.getNumProgramasServiciosActivos()>0)
//						{
//							adicionarError("errors.notEspecific","No existe Información de Clasificación para el Hallazgo ["+superficie.getHallazgoREQUERIDO().getNombre().toLowerCase()+"] "+ubicacion);
//						}
//						
//						/*
//						 * tarea 148223
//						if(superficie.getNumProgramasServiciosActivos() <= 0)
//							adicionarError("errors.notEspecific","No existen Programas/Servicios para el Hallazgo ["+superficie.getHallazgoREQUERIDO().getNombre().toLowerCase()+"] "+ubicacion);
//						*/
//					}
//				}
//				
//			}
//		}
//		
//		//SECCION DE OTROS HALLAZGOS
//		reg = 1;
//		for(InfoDetallePlanTramiento pieza: info.getInfoPlanTrata().getSeccionOtrosHallazgos())
//		{
//			if(pieza.getExisteBD().isActivo())
//			{
//				encontroDiente = true;
//				
//				if(pieza.getPieza().getCodigo() <= 0)
//					adicionarError("errors.notEspecific","No existe Pieza para el Registro Nro. "+reg+" de la Sección Otros Hallazgos del Odontograma de Diagnostico.");
//			
//				for(InfoHallazgoSuperficie hallazgo:pieza.getDetalleSuperficie())
//				{
//					if(hallazgo.getExisteBD().isActivo())
//					{	
//						if(hallazgo.getHallazgoREQUERIDO().getCodigo() <= 0)
//							adicionarError("errors.notEspecific","No existe hallazgo para el Registro Nro. "+reg+" de la Sección Otros Hallazgos del Odontograma de Diagnostico.");
//						else
//						{
//							/*
//							//tarea 148317 xplanner2008, se solicita que la superficie no sea requerido.
//							if(hallazgo.getHallazgoREQUERIDO().getCodigo2() == ComponenteOdontograma.codigoTipoHallazgoSuper)
//							{
//								if(hallazgo.getSuperficieOPCIONAL().getCodigo() <= 0)
//									adicionarError("errors.notEspecific","No existe Superficie para el Registro Nro. "+reg+" de la Sección Otros Hallazgos del Odontograma de Diagnostico.");
//							}
//							*/
//							/*
//							 * tarea 148314
//							if(hallazgo.getNumProgramasServiciosActivos() <= 0)
//								adicionarError("errors.notEspecific","No existen Programas/Servicios para el Registro Nro. "+reg+" de la Sección Otros Hallazgos del Odontograma de Diagnostico.");
//							*/
//						}
//					}
//					//verficar otros hallazgos con el plan de tratamiento para encontrar repetidos.
//					for(InfoDetallePlanTramiento piezaPlan:info.getInfoPlanTrata().getSeccionHallazgosDetalle())
//					{
//						if(piezaPlan.getExisteBD().isActivo())
//						{
//							encontroDiente = true;
//							for(InfoHallazgoSuperficie superficie:piezaPlan.getDetalleSuperficie())
//							{
//								boolean superficieTerminada=superficie.getSuperficieTerminada();
//								if(/*!superficieTerminada &&*/ piezaPlan.getPieza().getCodigo()==pieza.getPieza().getCodigo()&&
//										hallazgo.getSuperficieOPCIONAL().getCodigo()==superficie.getSuperficieOPCIONAL().getCodigo()
//								)
//								{
//									for(InfoProgramaServicioPlan programasPlan:superficie.getProgramasOservicios())
//									{
//										if(programasPlan.getExisteBD().isActivo())
//										{
//											for(InfoProgramaServicioPlan programas:hallazgo.getProgramasOservicios())
//											{
//												if(programas.getExisteBD().isActivo())
//												{
//													if(programasPlan.getCodigoAmostrar().equals(programas.getCodigoAmostrar()))
//													{
//														adicionarError("errors.notEspecific"," el Registro Nro. "+reg+" de la Sección Otros Hallazgos del Odontograma de Diagnóstico, se encuentra repetido entre en el odontograma y la sección otros hallazgos.");
//													}
//												}
//											}
//										}
//									}
//								}
//							}
//							
//						}
//					}
//					//verficar otros hallazgos con si misma para encontrar repetidos.
//					for(int tempo=0;tempo<(reg-1);tempo++)
//					{
//						InfoDetallePlanTramiento piezaPlan=info.getInfoPlanTrata().getSeccionOtrosHallazgos().get(tempo);
//						if(piezaPlan.getExisteBD().isActivo())
//						{
//							encontroDiente = true;
//							for(InfoHallazgoSuperficie superficie:piezaPlan.getDetalleSuperficie())
//							{
//								boolean superficieTerminada=superficie.getSuperficieTerminada();
//								if(/*!superficieTerminada && */piezaPlan.getPieza().getCodigo()==pieza.getPieza().getCodigo()&&
//										hallazgo.getSuperficieOPCIONAL().getCodigo()==superficie.getSuperficieOPCIONAL().getCodigo()
//								)
//								{
//									programaExterno:for(InfoProgramaServicioPlan programasPlan:superficie.getProgramasOservicios())
//									{
//										for(InfoProgramaServicioPlan programas:hallazgo.getProgramasOservicios())
//										{
//											if(programasPlan.getCodigoAmostrar().equals(programas.getCodigoAmostrar()))
//											{
//												adicionarError("errors.notEspecific"," el Registro Nro. "+reg+" de la Sección Otros Hallazgos del Odontograma de Diagnóstico, se encuentra repetido.");
//												break programaExterno;
//											}
//										}
//									}
//								}
//							}
//							
//						}
//					}
//				}
//				reg++;
//			}
//		}
//		
//		
//		
//		//SECCION HALLAZGOS BOCA
//		reg = 1;
//		//for(InfoHallazgoSuperficie hallazgo: info.getInfoPlanTrata().getSeccionHallazgosBoca())
//		for(int i=0;i<info.getInfoPlanTrata().getSeccionHallazgosBoca().size();i++)
//		{
//			InfoHallazgoSuperficie hallazgo=info.getInfoPlanTrata().getSeccionHallazgosBoca().get(i);
//			//verificarRepetidos.
//			for(int j=0;j<i;j++)
//			{
//				InfoHallazgoSuperficie hallazgoTemporal=info.getInfoPlanTrata().getSeccionHallazgosBoca().get(j);
//				boolean superficieTerminada=hallazgoTemporal.getSuperficieTerminada();
//				if(!superficieTerminada && (hallazgo.getHallazgoREQUERIDO().getCodigo()==hallazgoTemporal.getHallazgoREQUERIDO().getCodigo()) && hallazgo.getExisteBD().isActivo() && hallazgoTemporal.getExisteBD().isActivo())
//				{
//					adicionarError("errors.notEspecific","El hallazgo para el Registro Nro. "+reg+" de la Sección Hallazgos Boca del Odontograma de Diagnostico se encuentra repetido.");
//				}
//				
//			}
//			if(hallazgo.getExisteBD().isActivo())
//			{
//				encontroDiente = true; 
//				
//				if(hallazgo.getHallazgoREQUERIDO().getCodigo() <= 0)
//					adicionarError("errors.notEspecific","No existe hallazgo para el Registro Nro. "+reg+" de la Sección Hallazgos Boca del Odontograma de Diagnostico.");
//				else
//				{
//					if(hallazgo.getNumProgramasServiciosActivos() <= 0)
//						adicionarError("errors.notEspecific","No existen Programas/Servicios para el Registro Nro. "+reg+" de la Sección Hallazgos Boca del Odontograma de Diagnostico.");
//				}
//			
//				reg++;
//			}
//		}
//		
//		if(!encontroDiente && porConfirmar.equals(ConstantesBD.acronimoNo))
//		{
//			adicionarError("errors.atencionodontologica.hallazgosRequeridos", null);
//		}
//	}
	
	/**
	 * Método implementado para cargar la información historial confirmada del odontograma 
	 * por valoraci&oacute;n o evoluci&oacute;n 
	 * @param con
	 * @param info
	 * @param codigoInstitucion
	 * @param codigoValoracion 
	 */
	public void accionCargarOdontogramaHisConf(Connection con,InfoOdontograma info,int codigoInstitucion, BigDecimal codigoValoracion)
	{
		//Se carga el encabezado del plan de tratamiento
		PlanTratamiento.consultarHisConfPlanTratamiento(con, info.getInfoPlanTrata());// MALA PRACTICA
		
		
		if(info.getInfoPlanTrata().getCodigoPk().longValue()>0)
		{
			
			
			
			
			//*****************************************************************************************************
			//*****************************************************************************************************
			//SECCION PLAN TRATAMIENTO INICIAL
			
			//Carga las Piezas Dentales
			ArrayList<InfoDatosInt> arrayPiezas= PlanTratamiento.obtenerPiezasHisConf(con, info.getInfoPlanTrata(), ConstantesIntegridadDominio.acronimoDetalle);
			
			//iteramos las piezas para obtener los hallazgos ligados a la pieza
			//y los seteamos a los detalles
			DtoDetallePlanTratamiento parametrosSuperficies = new DtoDetallePlanTratamiento();
			parametrosSuperficies.setPlanTratamiento(info.getInfoPlanTrata().getCodigoPk().doubleValue());
			parametrosSuperficies.setSeccion(ConstantesIntegridadDominio.acronimoDetalle);
			parametrosSuperficies.setActivo(ConstantesBD.acronimoSi);
			parametrosSuperficies.setValoracion(info.getInfoPlanTrata().getValoracion());
			parametrosSuperficies.setEvolucion(info.getInfoPlanTrata().getEvolucion());
		
			
			
			
			for(InfoDatosInt pieza: arrayPiezas)
			{	
				
				
				
				
				InfoDetallePlanTramiento hallazgoSuperficie= new InfoDetallePlanTramiento();
				
				//Informacion de la pieza
				hallazgoSuperficie.setPieza(pieza);
				hallazgoSuperficie.getExisteBD().setActivo(true);
				hallazgoSuperficie.getExisteBD().setValue(ConstantesBD.acronimoSi);
				parametrosSuperficies.setPiezaDental(pieza.getCodigo());
				parametrosSuperficies.setPath(info.getPathNombreContexo());
				
				hallazgoSuperficie.setDetalleSuperficie(PlanTratamiento.obtenerHallazgosSuperficiesHisConf(con, parametrosSuperficies));
				
				DtoProgramasServiciosPlanT parametrosProgServ = new DtoProgramasServiciosPlanT();
				parametrosProgServ.setActivo(ConstantesBD.acronimoSi);
				parametrosProgServ.setEstadosProgramasOservicios(new ArrayList<String>());
				parametrosProgServ.setBuscarProgramas(ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(codigoInstitucion));
				parametrosProgServ.setValoracion(info.getInfoPlanTrata().getValoracion());
				parametrosProgServ.setEvolucion(info.getInfoPlanTrata().getEvolucion());
		
				
				
				//recorre los hallazgos y carga sus programas y servicios
				for(InfoHallazgoSuperficie hallsuper : hallazgoSuperficie.getDetalleSuperficie())
				{
					parametrosProgServ.setDetPlanTratamiento(hallsuper.getCodigoPkDetalle());
					hallsuper.setProgramasOservicios(PlanTratamiento.obtenerProgramasOServiciosHisConf(con, parametrosProgServ));
					hallsuper.getExisteBD().setValue(ConstantesBD.acronimoSi);
					
					//Recorre los servicios
					for(InfoProgramaServicioPlan progServ:hallsuper.getProgramasOservicios())
					{	
						//Actualiza la información para los permisos de eliminar
						if(progServ.getCodigoPkProgramaServicio().doubleValue() > 0)
						{
							progServ.getExisteBD().setEsEliminable(false);
						}
						else
						{
							for(InfoServicios servicios:progServ.getListaServicios())
								servicios.getExisteBD().setEsEliminable(false);
						}
					}
				}

				info.getInfoPlanTrata().getSeccionHallazgosDetalle().add(hallazgoSuperficie);				
				
			} // FIN  	SECCION PLAN TRATAMIENTO INICIAL
			
			
			
			
			
			
			//*****************************************************************************************************
			//*****************************************************************************************************
			//SECCION OTROS HALLAZGOS
			
			//Carga los dientes,hallazgos, superficies
			ArrayList<InfoDetallePlanTramiento> hallazgoSuperficie = new ArrayList<InfoDetallePlanTramiento>();
			
			parametrosSuperficies = new DtoDetallePlanTratamiento();
			parametrosSuperficies.setPlanTratamiento(info.getInfoPlanTrata().getCodigoPk().doubleValue());
			parametrosSuperficies.setSeccion(ConstantesIntegridadDominio.acronimoOtro);
			parametrosSuperficies.setActivo(ConstantesBD.acronimoSi);
			parametrosSuperficies.setValoracion(info.getInfoPlanTrata().getValoracion());
			parametrosSuperficies.setEvolucion(info.getInfoPlanTrata().getEvolucion());
			
			
			hallazgoSuperficie = PlanTratamiento.obtenerHallazgosSuperficiesSeccionOtrayBocaHisConf(con,parametrosSuperficies);
				
			DtoProgramasServiciosPlanT parametrosProgServ = new DtoProgramasServiciosPlanT();
			parametrosProgServ.setActivo(ConstantesBD.acronimoSi);
			parametrosProgServ.setEstadosProgramasOservicios(new ArrayList<String>());
			parametrosProgServ.setBuscarProgramas(ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(codigoInstitucion));
			parametrosProgServ.setValoracion(info.getInfoPlanTrata().getValoracion());
			parametrosProgServ.setEvolucion(info.getInfoPlanTrata().getEvolucion());
			
			
			
			
			
			
			for(InfoDetallePlanTramiento pieza: hallazgoSuperficie)
			{
				//recorre los hallazgos y carga sus programas y servicios
				for(InfoHallazgoSuperficie hallsuper : pieza.getDetalleSuperficie())
				{
					parametrosProgServ.setDetPlanTratamiento(hallsuper.getCodigoPkDetalle());
					hallsuper.setProgramasOservicios(PlanTratamiento.obtenerProgramasOServiciosHisConf(con, parametrosProgServ));
					hallsuper.getExisteBD().setValue(ConstantesBD.acronimoSi);
					
					//Recorre los servicios
					for(InfoProgramaServicioPlan progServ:hallsuper.getProgramasOservicios())
					{
						//Actualiza la información para los permisos de eliminar
						if(progServ.getCodigoPkProgramaServicio().doubleValue() > 0)
							progServ.getExisteBD().setEsEliminable(false);
						else
						{
							for(InfoServicios servicios:progServ.getListaServicios())
								servicios.getExisteBD().setEsEliminable(false);
						}
					}
				}
			}
			
			
			
			info.getInfoPlanTrata().setSeccionOtrosHallazgos(hallazgoSuperficie);
			
			
			
			
			
			

			//*****************************************************************************************************
			//*****************************************************************************************************
			//SECCION HALLAZGOS BOCA
			
			//Carga los dientes,hallazgos, superficies
			ArrayList<InfoHallazgoSuperficie> arrayInfoHallSuperBoca = new ArrayList<InfoHallazgoSuperficie>();
			ArrayList<InfoDetallePlanTramiento> arrayIinfoDetalle = new ArrayList<InfoDetallePlanTramiento>();
			
			parametrosSuperficies = new DtoDetallePlanTratamiento();
			parametrosSuperficies.setPlanTratamiento(info.getInfoPlanTrata().getCodigoPk().doubleValue());
			parametrosSuperficies.setSeccion(ConstantesIntegridadDominio.acronimoBoca);
			parametrosSuperficies.setActivo(ConstantesBD.acronimoSi);
			parametrosSuperficies.setValoracion(info.getInfoPlanTrata().getValoracion());
			parametrosSuperficies.setEvolucion(info.getInfoPlanTrata().getEvolucion());
			arrayIinfoDetalle = PlanTratamiento.obtenerHallazgosSuperficiesSeccionOtrayBocaHisConf(con, parametrosSuperficies);
				
			parametrosProgServ = new DtoProgramasServiciosPlanT();
			parametrosProgServ.setActivo(ConstantesBD.acronimoSi);
			parametrosProgServ.setEstadosProgramasOservicios(new ArrayList<String>());
			parametrosProgServ.setBuscarProgramas(ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(codigoInstitucion));
			parametrosProgServ.setValoracion(info.getInfoPlanTrata().getValoracion());
			parametrosProgServ.setEvolucion(info.getInfoPlanTrata().getEvolucion());
		
			
			
			
			
			for(InfoDetallePlanTramiento infoDetalle: arrayIinfoDetalle)
			{
				//recorre los hallazgos y carga sus programas y servicios
				for(InfoHallazgoSuperficie hallsuper : infoDetalle.getDetalleSuperficie())
				{
					parametrosProgServ.setDetPlanTratamiento(hallsuper.getCodigoPkDetalle());
					hallsuper.setProgramasOservicios(PlanTratamiento.obtenerProgramasOServiciosHisConf(con, parametrosProgServ));
					hallsuper.getExisteBD().setValue(ConstantesBD.acronimoSi);
					
					//Recorre los servicios
					for(InfoProgramaServicioPlan progServ:hallsuper.getProgramasOservicios())
					{
						//Actualiza la información para los permisos de eliminar
						if(progServ.getCodigoPkProgramaServicio().doubleValue() > 0)
							progServ.getExisteBD().setEsEliminable(false);
						else
						{
							for(InfoServicios servicios:progServ.getListaServicios())
								servicios.getExisteBD().setEsEliminable(false);
						}
					}
					
					arrayInfoHallSuperBoca.add(hallsuper);
				}
			}
			
			
			
			
			info.getInfoPlanTrata().setSeccionHallazgosBoca(arrayInfoHallSuperBoca);
			//malo cambiar
			info.getInfoPlanTrata().setImagen(PlanTratamiento.cargarOdontograma(codigoValoracion, true).getImagen());
			
			
		}
		
		
	}

	
	
	/**
	 * M&eacute;todo que carga el odontograma
	 * @param info Objeto pasado con parámetros de b&uacute;squeda.
	 * @param codigoPk C&oacute;digo de la valoraci&oacute;n que se va a cargar (Utilizado para cargar la imagen).
	 * @return InfoOdontograma información completa del odontograma.
	 */
	public InfoOdontograma accionCargarOdontograma(InfoOdontograma info, BigDecimal codigoValoracion)
	{
		
		
		//Carga la Información de las convenciones parametrizadas
		info.setInfoPlanTrata(new InfoPlanTratamiento());
		info.setXmlOdontograma("");

		//Inicializar la información del Plan de Tratamiento
		info.getInfoPlanTrata().setCodigoPk(PlanTratamiento.obtenerUltimoCodigoPlanTratamiento(info.getIdIngresoPaciente(), new ArrayList<String>(), "" /*porConfirmar*/));
		
		//validamos que cargue el codigo
		if(info.getInfoPlanTrata().getCodigoPk().doubleValue()>0)
		{
			//Cargamos la información del encabezado del plan de tratamiento
			DtoPlanTratamientoOdo parametros = new DtoPlanTratamientoOdo();
			parametros.setCodigoPk(info.getInfoPlanTrata().getCodigoPk());
			parametros.setInstitucion(info.getInstitucion());
			info.setDtoInfoPlanTratamiento(PlanTratamiento.consultarPlanTratamiento(parametros));

			
			//Verifica los estados
			if(info.getDtoInfoPlanTratamiento().getEstado().equals(ConstantesIntegridadDominio.acronimoTerminado) || 
					info.getDtoInfoPlanTratamiento().getEstado().equals(ConstantesIntegridadDominio.acronimoEstadoCancelado) ||
						info.getDtoInfoPlanTratamiento().getEstado().equals(ConstantesIntegridadDominio.acronimoInactivo))
			{
				if(info.isEvaluarEstadosPlanTratamiento())
				{
					info.setInfoPlanTrata(new InfoPlanTratamiento());
					logger.info("..:Se carga en Blanco el plan de tratamiento");
				}
			}
			else if(info.getDtoInfoPlanTratamiento().getEstado().equals(ConstantesIntegridadDominio.acronimoEstadoActivo) || 
					info.getDtoInfoPlanTratamiento().getEstado().equals(ConstantesIntegridadDominio.acronimoEstadoEnProceso) ||
						info.getDtoInfoPlanTratamiento().getEstado().equals(ConstantesIntegridadDominio.acronimoSuspendidoTemporalmente))
			{
				logger.info("..:Carga la información del plan de tratamiento");
			
				//*****************************************************************************************************
				//*****************************************************************************************************
				//SECCION PLAN TRATAMIENTO INICIAL
				
				//Carga las Piezas Dentales
				ArrayList<InfoDatosInt> arrayPiezas= PlanTratamiento.obtenerPiezas(
														info.getInfoPlanTrata().getCodigoPk(),
														ConstantesIntegridadDominio.acronimoDetalle, "" /*porConfirmar*/);
				
				for(InfoDatosInt pieza:arrayPiezas)
				{
					pieza.setActivo(PlanTratamiento.tieneAlgunaSuperficieActiva(info.getInfoPlanTrata().getCodigoPk(), pieza.getCodigo(), ConstantesIntegridadDominio.acronimoDetalle));
				}
				
				//iteramos las piezas para obtener los hallazgos ligados a la pieza
				//y los seteamos a los detalles
				DtoDetallePlanTratamiento parametrosSuperficies = new DtoDetallePlanTratamiento();
				parametrosSuperficies.setPlanTratamiento(info.getDtoInfoPlanTratamiento().getCodigoPk().doubleValue());
				parametrosSuperficies.setSeccion(ConstantesIntegridadDominio.acronimoDetalle);
				
				/*
				 * De todas maneras la tengo que cargar para que me modifique el activo o inactivo
				parametrosSuperficies.setActivo(ConstantesBD.acronimoSi);
				*/
				parametrosSuperficies.setActivo(ConstantesBD.acronimoNo);
				
				for(InfoDatosInt pieza: arrayPiezas)
				{	
					InfoDetallePlanTramiento hallazgoSuperficie= new InfoDetallePlanTramiento();
					
					//Informacion de la pieza
					hallazgoSuperficie.setPieza(pieza);
					hallazgoSuperficie.getExisteBD().setActivo(pieza.getActivo());
					hallazgoSuperficie.getExisteBD().setValue(ConstantesBD.acronimoSi);
					parametrosSuperficies.setPiezaDental(pieza.getCodigo());
					parametrosSuperficies.setPath(info.getPathNombreContexo());
					
					hallazgoSuperficie.setDetalleSuperficie(PlanTratamiento.obtenerHallazgosSuperficies(parametrosSuperficies));
					
					DtoProgramasServiciosPlanT parametrosProgServ = new DtoProgramasServiciosPlanT();
					parametrosProgServ.setActivo(ConstantesBD.acronimoSi);
					parametrosProgServ.setEstadosProgramasOservicios(new ArrayList<String>());
					parametrosProgServ.setBuscarProgramas(info.getEsBuscarPorPrograma());
					
					//recorre los hallazgos y carga sus programas y servicios
					for(InfoHallazgoSuperficie hallsuper : hallazgoSuperficie.getDetalleSuperficie())
					{
						parametrosProgServ.setDetPlanTratamiento(hallsuper.getCodigoPkDetalle());
						hallsuper.setProgramasOservicios(PlanTratamiento.obtenerProgramasOServicios(parametrosProgServ));
						hallsuper.determinaYactulizaEsModificable(info.isSoloModifInfoNueva());
						
						//Recorre los servicios
						for(InfoProgramaServicioPlan progServ:hallsuper.getProgramasOservicios())
						{	
							//Actualiza la información para los permisos de eliminar
							if(progServ.getCodigoPkProgramaServicio().doubleValue() > 0)
								progServ.getExisteBD().setEsEliminable(permiteEliminarProgServ(hallsuper.getPorConfirmar(),progServ.getPorConfirmar()));
							else
							{
								for(InfoServicios servicios:progServ.getListaServicios())
								{
									servicios.getExisteBD().setEsEliminable(permiteEliminarProgServ(hallsuper.getPorConfirmar(),servicios.getPorConfirmar()));
								}
							}
						}
					}

					info.getInfoPlanTrata().getSeccionHallazgosDetalle().add(hallazgoSuperficie);
				}
				
				
				//*****************************************************************************************************
				//*****************************************************************************************************
				//SECCION OTROS HALLAZGOS
				
				//Carga los dientes,hallazgos, superficies
				ArrayList<InfoDetallePlanTramiento> hallazgoSuperficie = new ArrayList<InfoDetallePlanTramiento>();
				
				parametrosSuperficies = new DtoDetallePlanTratamiento();
				parametrosSuperficies.setPlanTratamiento(info.getDtoInfoPlanTratamiento().getCodigoPk().doubleValue());
				parametrosSuperficies.setSeccion(ConstantesIntegridadDominio.acronimoOtro);
				parametrosSuperficies.setActivo(ConstantesBD.acronimoSi);
				hallazgoSuperficie = PlanTratamiento.obtenerHallazgosSuperficiesSeccionOtrayBoca(parametrosSuperficies);
					
				DtoProgramasServiciosPlanT parametrosProgServ = new DtoProgramasServiciosPlanT();
				parametrosProgServ.setActivo(ConstantesBD.acronimoSi);
				parametrosProgServ.setEstadosProgramasOservicios(new ArrayList<String>());
				parametrosProgServ.setBuscarProgramas(info.getEsBuscarPorPrograma());
				
				for(InfoDetallePlanTramiento pieza: hallazgoSuperficie)
				{
					pieza.determinaYactulizaEsModificable(info.isSoloModifInfoNueva());
					//recorre los hallazgos y carga sus programas y servicios
					for(InfoHallazgoSuperficie hallsuper : pieza.getDetalleSuperficie())
					{
						parametrosProgServ.setDetPlanTratamiento(hallsuper.getCodigoPkDetalle());
						hallsuper.setProgramasOservicios(PlanTratamiento.obtenerProgramasOServicios(parametrosProgServ));
						hallsuper.determinaYactulizaEsModificable(info.isSoloModifInfoNueva());
						//ComponenteOdontograma.codigoTipoHallazgoSuper
						//Recorre los servicios
						for(InfoProgramaServicioPlan progServ:hallsuper.getProgramasOservicios())
						{
							//Actualiza la información para los permisos de eliminar
							if(progServ.getCodigoPkProgramaServicio().doubleValue() > 0)
								progServ.getExisteBD().setEsEliminable(permiteEliminarProgServ(hallsuper.getPorConfirmar(),progServ.getPorConfirmar()));
							else
							{
								for(InfoServicios servicios:progServ.getListaServicios())
									servicios.getExisteBD().setEsEliminable(permiteEliminarProgServ(hallsuper.getPorConfirmar(),servicios.getPorConfirmar()));
							}
						}
					}
					pieza.setArraySuperficiesDiente(cargarSuperficiesDiente(info.getArraySuperficies(),pieza.getPieza().getCodigo()));
				}
				
				info.getInfoPlanTrata().setSeccionOtrosHallazgos(hallazgoSuperficie);
				
				
				//*****************************************************************************************************
				//*****************************************************************************************************
				//SECCION HALLAZGOS BOCA
				
				//Carga los dientes,hallazgos, superficies
				ArrayList<InfoHallazgoSuperficie> arrayInfoHallSuperBoca = new ArrayList<InfoHallazgoSuperficie>();
				ArrayList<InfoDetallePlanTramiento> arrayIinfoDetalle = new ArrayList<InfoDetallePlanTramiento>();
				
				parametrosSuperficies = new DtoDetallePlanTratamiento();
				parametrosSuperficies.setPlanTratamiento(info.getDtoInfoPlanTratamiento().getCodigoPk().doubleValue());
				parametrosSuperficies.setSeccion(ConstantesIntegridadDominio.acronimoBoca);
				parametrosSuperficies.setActivo(ConstantesBD.acronimoSi);
				arrayIinfoDetalle = PlanTratamiento.obtenerHallazgosSuperficiesSeccionOtrayBoca(parametrosSuperficies);
					
				parametrosProgServ = new DtoProgramasServiciosPlanT();
				parametrosProgServ.setActivo(ConstantesBD.acronimoSi);
				parametrosProgServ.setEstadosProgramasOservicios(new ArrayList<String>());
				parametrosProgServ.setBuscarProgramas(info.getEsBuscarPorPrograma());
				
				for(InfoDetallePlanTramiento infoDetalle: arrayIinfoDetalle)
				{
					//recorre los hallazgos y carga sus programas y servicios
					for(InfoHallazgoSuperficie hallsuper : infoDetalle.getDetalleSuperficie())
					{
						parametrosProgServ.setDetPlanTratamiento(hallsuper.getCodigoPkDetalle());
						hallsuper.setProgramasOservicios(PlanTratamiento.obtenerProgramasOServicios(parametrosProgServ));
						hallsuper.determinaYactulizaEsModificable(info.isSoloModifInfoNueva());
						
						//Recorre los servicios
						for(InfoProgramaServicioPlan progServ:hallsuper.getProgramasOservicios())
						{
							//Actualiza la información para los permisos de eliminar
							if(progServ.getCodigoPkProgramaServicio().doubleValue() > 0)
								progServ.getExisteBD().setEsEliminable(permiteEliminarProgServ(hallsuper.getPorConfirmar(),progServ.getPorConfirmar()));
							else
							{
								for(InfoServicios servicios:progServ.getListaServicios())
									servicios.getExisteBD().setEsEliminable(permiteEliminarProgServ(hallsuper.getPorConfirmar(),servicios.getPorConfirmar()));
							}
						}
						
						arrayInfoHallSuperBoca.add(hallsuper);
					}
				}
				
				info.getInfoPlanTrata().setSeccionHallazgosBoca(arrayInfoHallSuperBoca);
			}
			
			info.setXmlOdontograma(getXmlPlanTratamiento(info));
			/*
			 * Metodo para cargar la imagen del Odontograma
			 */
			cargarImagenOdontograma(info, codigoValoracion);
		}
		else
		{
			//Inicializa los atributos necesarios
			info.getInfoPlanTrata().setEstado(ConstantesIntegridadDominio.acronimoEstadoActivo);
			info.getInfoPlanTrata().setPorConfirmar(ConstantesBD.acronimoSi);
		}
		
		//Actualiza los dientes usados para las seccion de otros
		info.setArrayDientesUsados(llenarDienteUsados(info));
		return info;
		
		
	}
	
	
	
	
	/**
	 * M&eacute;todo que carga el odontograma
	 * ESTE METODO CARGA EL INFOPLAN SIN IMPORTAR LOS ESTADOS 
	 * 
	 * 
	 * @param info Objeto pasado con parámetros de b&uacute;squeda.
	 * @param codigoPk C&oacute;digo de la valoraci&oacute;n que se va a cargar (Utilizado para cargar la imagen).
	 * @return InfoOdontograma información completa del odontograma.
	 */
	public InfoOdontograma accionCargarOdontogramasSinEstadosPlan(InfoOdontograma info, BigDecimal codigoValoracion)
	{
		
		
		//Carga la Información de las convenciones parametrizadas
		info.setInfoPlanTrata(new InfoPlanTratamiento());
		info.setXmlOdontograma("");

		//Inicializa la información del Plan de Tratamiento
		info.getInfoPlanTrata().setCodigoPk(PlanTratamiento.obtenerUltimoCodigoPlanTratamiento(info.getIdIngresoPaciente(), new ArrayList<String>(), "" /*porConfirmar*/));
		
		//validamos que cargue el codigo
		if(info.getInfoPlanTrata().getCodigoPk().doubleValue()>0)
		{
			//Cargamos la información del encabezado del plan de tratamiento
			DtoPlanTratamientoOdo parametros = new DtoPlanTratamientoOdo();
			parametros.setCodigoPk(info.getInfoPlanTrata().getCodigoPk());
			parametros.setInstitucion(info.getInstitucion());
			info.setDtoInfoPlanTratamiento(PlanTratamiento.consultarPlanTratamiento(parametros));

			
				
				//*****************************************************************************************************
				//*****************************************************************************************************
				//SECCION PLAN TRATAMIENTO INICIAL
				
				//Carga las Piezas Dentales
				ArrayList<InfoDatosInt> arrayPiezas= PlanTratamiento.obtenerPiezas(
														info.getInfoPlanTrata().getCodigoPk(),
														ConstantesIntegridadDominio.acronimoDetalle, "" /*porConfirmar*/);
				
				for(InfoDatosInt pieza:arrayPiezas)
				{
					pieza.setActivo(PlanTratamiento.tieneAlgunaSuperficieActiva(info.getInfoPlanTrata().getCodigoPk(), pieza.getCodigo(), ConstantesIntegridadDominio.acronimoDetalle));
				}
				
				//iteramos las piezas para obtener los hallazgos ligados a la pieza
				//y los seteamos a los detalles
				DtoDetallePlanTratamiento parametrosSuperficies = new DtoDetallePlanTratamiento();
				parametrosSuperficies.setPlanTratamiento(info.getDtoInfoPlanTratamiento().getCodigoPk().doubleValue());
				parametrosSuperficies.setSeccion(ConstantesIntegridadDominio.acronimoDetalle);
				
				/*
				 * De todas maneras la tengo que cargar para que me modifique el activo o inactivo
				parametrosSuperficies.setActivo(ConstantesBD.acronimoSi);
				*/
				parametrosSuperficies.setActivo(ConstantesBD.acronimoNo);
				
				for(InfoDatosInt pieza: arrayPiezas)
				{	
					InfoDetallePlanTramiento hallazgoSuperficie= new InfoDetallePlanTramiento();
					
					//Informacion de la pieza
					hallazgoSuperficie.setPieza(pieza);
					hallazgoSuperficie.getExisteBD().setActivo(pieza.getActivo());
					hallazgoSuperficie.getExisteBD().setValue(ConstantesBD.acronimoSi);
					parametrosSuperficies.setPiezaDental(pieza.getCodigo());
					parametrosSuperficies.setPath(info.getPathNombreContexo());
					
					hallazgoSuperficie.setDetalleSuperficie(PlanTratamiento.obtenerHallazgosSuperficies(parametrosSuperficies));
					
					DtoProgramasServiciosPlanT parametrosProgServ = new DtoProgramasServiciosPlanT();
					parametrosProgServ.setActivo(ConstantesBD.acronimoSi);
					parametrosProgServ.setEstadosProgramasOservicios(new ArrayList<String>());
					parametrosProgServ.setBuscarProgramas(info.getEsBuscarPorPrograma());
					
					//recorre los hallazgos y carga sus programas y servicios
					for(InfoHallazgoSuperficie hallsuper : hallazgoSuperficie.getDetalleSuperficie())
					{
						parametrosProgServ.setDetPlanTratamiento(hallsuper.getCodigoPkDetalle());
						hallsuper.setProgramasOservicios(PlanTratamiento.obtenerProgramasOServicios(parametrosProgServ));
						hallsuper.determinaYactulizaEsModificable(info.isSoloModifInfoNueva());
						
						//Recorre los servicios
						for(InfoProgramaServicioPlan progServ:hallsuper.getProgramasOservicios())
						{	
							//Actualiza la información para los permisos de eliminar
							if(progServ.getCodigoPkProgramaServicio().doubleValue() > 0)
								progServ.getExisteBD().setEsEliminable(permiteEliminarProgServ(hallsuper.getPorConfirmar(),progServ.getPorConfirmar()));
							else
							{
								for(InfoServicios servicios:progServ.getListaServicios())
								{
									servicios.getExisteBD().setEsEliminable(permiteEliminarProgServ(hallsuper.getPorConfirmar(),servicios.getPorConfirmar()));
								}
							}
						}
					}

					info.getInfoPlanTrata().getSeccionHallazgosDetalle().add(hallazgoSuperficie);
				}
				
				
				//*****************************************************************************************************
				//*****************************************************************************************************
				//SECCION OTROS HALLAZGOS
				
				//Carga los dientes,hallazgos, superficies
				ArrayList<InfoDetallePlanTramiento> hallazgoSuperficie = new ArrayList<InfoDetallePlanTramiento>();
				
				parametrosSuperficies = new DtoDetallePlanTratamiento();
				parametrosSuperficies.setPlanTratamiento(info.getDtoInfoPlanTratamiento().getCodigoPk().doubleValue());
				parametrosSuperficies.setSeccion(ConstantesIntegridadDominio.acronimoOtro);
				parametrosSuperficies.setActivo(ConstantesBD.acronimoSi);
				hallazgoSuperficie = PlanTratamiento.obtenerHallazgosSuperficiesSeccionOtrayBoca(parametrosSuperficies);
					
				DtoProgramasServiciosPlanT parametrosProgServ = new DtoProgramasServiciosPlanT();
				parametrosProgServ.setActivo(ConstantesBD.acronimoSi);
				parametrosProgServ.setEstadosProgramasOservicios(new ArrayList<String>());
				parametrosProgServ.setBuscarProgramas(info.getEsBuscarPorPrograma());
				
				for(InfoDetallePlanTramiento pieza: hallazgoSuperficie)
				{
					pieza.determinaYactulizaEsModificable(info.isSoloModifInfoNueva());
					//recorre los hallazgos y carga sus programas y servicios
					for(InfoHallazgoSuperficie hallsuper : pieza.getDetalleSuperficie())
					{
						parametrosProgServ.setDetPlanTratamiento(hallsuper.getCodigoPkDetalle());
						hallsuper.setProgramasOservicios(PlanTratamiento.obtenerProgramasOServicios(parametrosProgServ));
						hallsuper.determinaYactulizaEsModificable(info.isSoloModifInfoNueva());
						//ComponenteOdontograma.codigoTipoHallazgoSuper
						//Recorre los servicios
						for(InfoProgramaServicioPlan progServ:hallsuper.getProgramasOservicios())
						{
							//Actualiza la información para los permisos de eliminar
							if(progServ.getCodigoPkProgramaServicio().doubleValue() > 0)
								progServ.getExisteBD().setEsEliminable(permiteEliminarProgServ(hallsuper.getPorConfirmar(),progServ.getPorConfirmar()));
							else
							{
								for(InfoServicios servicios:progServ.getListaServicios())
									servicios.getExisteBD().setEsEliminable(permiteEliminarProgServ(hallsuper.getPorConfirmar(),servicios.getPorConfirmar()));
							}
						}
					}
					pieza.setArraySuperficiesDiente(cargarSuperficiesDiente(info.getArraySuperficies(),pieza.getPieza().getCodigo()));
				}
				
				info.getInfoPlanTrata().setSeccionOtrosHallazgos(hallazgoSuperficie);
				
				
				//*****************************************************************************************************
				//*****************************************************************************************************
				//SECCION HALLAZGOS BOCA
				
				//Carga los dientes,hallazgos, superficies
				ArrayList<InfoHallazgoSuperficie> arrayInfoHallSuperBoca = new ArrayList<InfoHallazgoSuperficie>();
				ArrayList<InfoDetallePlanTramiento> arrayIinfoDetalle = new ArrayList<InfoDetallePlanTramiento>();
				
				parametrosSuperficies = new DtoDetallePlanTratamiento();
				parametrosSuperficies.setPlanTratamiento(info.getDtoInfoPlanTratamiento().getCodigoPk().doubleValue());
				parametrosSuperficies.setSeccion(ConstantesIntegridadDominio.acronimoBoca);
				parametrosSuperficies.setActivo(ConstantesBD.acronimoSi);
				arrayIinfoDetalle = PlanTratamiento.obtenerHallazgosSuperficiesSeccionOtrayBoca(parametrosSuperficies);
					
				parametrosProgServ = new DtoProgramasServiciosPlanT();
				parametrosProgServ.setActivo(ConstantesBD.acronimoSi);
				parametrosProgServ.setEstadosProgramasOservicios(new ArrayList<String>());
				parametrosProgServ.setBuscarProgramas(info.getEsBuscarPorPrograma());
				
				for(InfoDetallePlanTramiento infoDetalle: arrayIinfoDetalle)
				{
					//recorre los hallazgos y carga sus programas y servicios
					for(InfoHallazgoSuperficie hallsuper : infoDetalle.getDetalleSuperficie())
					{
						parametrosProgServ.setDetPlanTratamiento(hallsuper.getCodigoPkDetalle());
						hallsuper.setProgramasOservicios(PlanTratamiento.obtenerProgramasOServicios(parametrosProgServ));
						hallsuper.determinaYactulizaEsModificable(info.isSoloModifInfoNueva());
						
						//Recorre los servicios
						for(InfoProgramaServicioPlan progServ:hallsuper.getProgramasOservicios())
						{
							//Actualiza la información para los permisos de eliminar
							if(progServ.getCodigoPkProgramaServicio().doubleValue() > 0)
								progServ.getExisteBD().setEsEliminable(permiteEliminarProgServ(hallsuper.getPorConfirmar(),progServ.getPorConfirmar()));
							else
							{
								for(InfoServicios servicios:progServ.getListaServicios())
									servicios.getExisteBD().setEsEliminable(permiteEliminarProgServ(hallsuper.getPorConfirmar(),servicios.getPorConfirmar()));
							}
						}
						
						arrayInfoHallSuperBoca.add(hallsuper);
					}
				}
				
				info.getInfoPlanTrata().setSeccionHallazgosBoca(arrayInfoHallSuperBoca);
			}
			
			info.setXmlOdontograma(getXmlPlanTratamiento(info));
			/*
			 * Metodo para cargar la imagen del Odontograma
			 */
			cargarImagenOdontograma(info, codigoValoracion);
		
		
		//Actualiza los dientes usados para las seccion de otros
		info.setArrayDientesUsados(llenarDienteUsados(info));
		return info;
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Metodo para cargar la Imagen del odontograma
	 * @param info
	 * @param codigoValoracion
	 */
	private void cargarImagenOdontograma(InfoOdontograma info,
										BigDecimal codigoValoracion) 
	{
		//CARGAR IMAGEN
		if(codigoValoracion!=null && codigoValoracion.doubleValue()>0)
		{
			//SI EXISTE VALORACION
			info.getInfoPlanTrata().setImagen(PlanTratamiento.cargarOdontograma(codigoValoracion, true).getImagen());
		}
	}
	
	
	/**
	 * Obtiene la información del plan de tratamiento en XML 
	 * */
	private String getXmlPlanTratamiento(InfoOdontograma info)
	{
		String cadena = "";

		//Recorre laas piezas dentales
		for(InfoDetallePlanTramiento pieza:info.getInfoPlanTrata().getSeccionHallazgosDetalle())
		{
			if(pieza.getExisteBD().isActivo())
			{
				cadena+= "<diente pieza = '"+pieza.getPieza().getCodigo()+"'>";
				
				//Recorre las superficies
				for(InfoHallazgoSuperficie superficie:pieza.getDetalleSuperficie())
				{
/*					logger.info("xml "+superficie.getSuperficieOPCIONAL().getCodigo()+" "+superficie.getSuperficieOPCIONAL().getActivo()+" "+superficie.getSuperficieOPCIONAL().getNombre());
					logger.info("superficie.getCodigoConvencion() "+superficie.getCodigoConvencion());*/
					if(superficie.getSuperficieOPCIONAL().getCodigo() > 0 
							&& superficie.getSuperficieOPCIONAL().getActivo())
					{
						cadena+= "<superficie codigo = '"+superficie.getSuperficieOPCIONAL().getCodigo()+"' sector = '"+superficie.getSuperficieOPCIONAL().getCodigo2()+"' modificable='"+superficie.getExisteBD().isEsModificable()+"'>\n";
						cadena+= "	<hallazgo>\n" +
								 "		<codigo>"+superficie.getHallazgoREQUERIDO().getCodigo()+"</codigo>\n" +
								 "		<descripcion>"+UtilidadTexto.cambiarCaracteresEspeciales(superficie.getHallazgoREQUERIDO().getNombre())+"</descripcion>\n" +
								 "		<path>"+superficie.getHallazgoREQUERIDO().getDescripcion()+"</path>\n" +
								 "		<convencion>"+superficie.getCodigoConvencion()+"</convencion>\n" +
								 "	</hallazgo>\n";
						cadena+= "</superficie>\n";
					}
					else
					{
						cadena+= "<superficie codigo = '"+ConstantesBD.codigoNuncaValido+"' modificable='"+superficie.getExisteBD().isEsModificable()+"'>\n";
						cadena+= "	<hallazgo>\n" +
								 "		<codigo>\n"+superficie.getHallazgoREQUERIDO().getCodigo()+"</codigo>\n" +
								 "		<descripcion>"+UtilidadTexto.cambiarCaracteresEspeciales(superficie.getHallazgoREQUERIDO().getNombre())+"</descripcion>\n" +
								 "		<path>"+superficie.getHallazgoREQUERIDO().getDescripcion()+"</path>\n" +
								 "		<convencion>"+superficie.getCodigoConvencion()+"</convencion>\n" +
								 "	</hallazgo>\n";
						cadena+= "</superficie>\n";
					}
				}
				
				cadena+= "</diente>\n";
			}
		}
		
		if(!cadena.equals(""))
		{
			cadena = "<contenido>\n"+cadena+"\n</contenido>";
		}
		
		//logger.info("cadena XML "+cadena);
		return cadena;
	}
	
	/**
	 * Iguala el plan de tratamiento en memoria al el XML
	 * generado en el componente flash del odontograma.
	 * @param info {@link InfoOdontograma} Informaci&oacute;n del odontograma en memoria
	 * @author Juan David Ram&uacute;rez
	 * @since 04 Mayo de 2010
	 */
	private void igualarPlanTratamientoAOdontograma(InfoOdontograma info)
	{
		/*
		 * Verifico que el XML traiga algún dato, sino es porque se quitó la selección
		 * de todas las superficies y dientes
		 */
		if(!info.getXmlOdontograma().equals(""))
		{
			/*
			 * Obtengo un plan de tratamiento a raíz del XML para comparar con
			 * el que ya existe en memoria
			 */
			InfoPlanTratamiento infoPlanOdontoXML =  getInfoPlanTratamientoDeXML(info.getXmlOdontograma());
			
			/*
			 * Solamente tengo que verificar el detalle de los hallazgos realizados en el odontograma,
			 * el resto de los hallazgos se deben conservar intactos
			 */
			ArrayList<InfoDetallePlanTramiento> detalleHallazgos=info.getInfoPlanTrata().getSeccionHallazgosDetalle();
			
			/*
			 * Primero verifico si lo del plan de tratamiento existe en el XML 
			 */
			
			/*
			 * Itero el plan
			 */
			for(InfoDetallePlanTramiento detallePlan:detalleHallazgos)
			{
				/*
				 * Itero las superficies del plan
				 */
				cilcoSuperficies:for(InfoHallazgoSuperficie superficie:detallePlan.getDetalleSuperficie())
				{
					/*
					 * Si la superficie es modificable entonces trabajo con ella
					 * de lo contrario la dejo como estaba
					 */
					if(superficie.getExisteBD().isEsModificable())
					{
						/*
						 * Permite tener un control para la inactivación de la pieza completa
						 */
						boolean encontrePieza=false;

						/*
						 * Itero el plan generado del XML
						 */
						for(InfoDetallePlanTramiento detallePlanXML:infoPlanOdontoXML.getSeccionHallazgosDetalle())
						{
							/*
							 * Verifico que esté mirando la misma pieza
							 */
							if(detallePlan.getPieza().getCodigo()==detallePlanXML.getPieza().getCodigo())
							{
								/*
								 * Si se inactivo en una ocasión anterior se reactiva
								 */
								detallePlan.getExisteBD().setActivo(true);
								/*
								 * Ya encontré la pieza, por lo tanto no la inactivo 
								 */
								encontrePieza=true;
								/*
								 * Permite tener un control de la superficie que se va a ingresar
								 */
								boolean encontroSuperficie = false;
								/*
								 * Itero las superficies del plan generado del XML
								 */
								for(InfoHallazgoSuperficie superficieXML:detallePlanXML.getDetalleSuperficie())
								{
									
									/*
									 * Si encuentro la misma superficie entonces puedo modificarla
									 * El código 2 es el código del sector, el cual me indica exáctamente cual es la superficie!! el código no se donde lo asignan
									 * y no se para que lo utilizan
									 */
									if(superficie.getSuperficieOPCIONAL().getCodigo2()==superficieXML.getSuperficieOPCIONAL().getCodigo2())
									{
										encontroSuperficie=true;
										/*
										 * Le cambio el activo para saber si ya fue evaluada.
										 */
										superficieXML.getExisteBD().setActivo(false);
										if(superficie.getHallazgoREQUERIDO().getCodigo()!=superficieXML.getHallazgoREQUERIDO().getCodigo())
										{
											superficie.setHallazgoREQUERIDO(superficieXML.getHallazgoREQUERIDO());
											
											/*
											 * Elimino los programas de las superficies relacionadas
											 */
											if(superficie.getProgramasOservicios().size()>0 && superficie.getProgramasOservicios().get(0).getNumeroSuperficies()>1)
											{
												/*
												 * Busco las otras superficies con el mismo hallazgo
												 */
												for(InfoHallazgoSuperficie superficieInterna:detallePlan.getDetalleSuperficie())
												{
													if(superficie.getHallazgoREQUERIDO().getCodigo()==superficieInterna.getHallazgoREQUERIDO().getCodigo())
													{
														if(superficieInterna.getSuperficieOPCIONAL().getCodigo2()!=superficie.getSuperficieOPCIONAL().getCodigo2())
														{
															/*
															 * Elimino el hallazgo de las otras superficies
															 */
															superficieInterna.setProgramasOservicios(new ArrayList<InfoProgramaServicioPlan>());
														}
													}
												}
												
											}
											
											superficie.setProgramasOservicios(new ArrayList<InfoProgramaServicioPlan>());
											
										}
										superficie.getInfoRegistroHallazgo().setFechaModifica(UtilidadFecha.getFechaActual());
										superficie.getInfoRegistroHallazgo().setHoraModifica(UtilidadFecha.getHoraActual());
										superficie.setSuperficieOPCIONAL(superficieXML.getSuperficieOPCIONAL());
										superficie.getSuperficieOPCIONAL().setCodigo(info.getCodigoSuperficePorSector(superficieXML.getSuperficieOPCIONAL().getCodigo2(), detallePlan.getPieza().getCodigo()));
										
										
										/*
										 * En caso de que se hubiera inactivado en un proceso anterior
										 * la tengo que volver a activar
										 */
										superficie.getExisteBD().setActivo(true);
										
										/*
										 * Ya modifiqué la superficie, puedo continuar con las otras superficies
										 */
										continue cilcoSuperficies;
									}
								}						
								/*
								 * Si no encontró la superficie es porque la eliminaron del plan
								 */
								if(!encontroSuperficie)
								{
									/*
									 * La marco como activo en no para que sea eliminada del plan al momento de guardar
									 */
									superficie.getExisteBD().setActivo(false);
									/*
									 * Elimino los programas de las superficies relacionadas (Buscar como centralizar en un método)
									 */
									if(superficie.getProgramasOservicios().size()>0 && superficie.getProgramasOservicios().get(0).getNumeroSuperficies()>1)
									{
										/*
										 * Busco las otras superficies con el mismo hallazgo
										 */
										for(InfoHallazgoSuperficie superficieInterna:detallePlan.getDetalleSuperficie())
										{
											if(superficie.getHallazgoREQUERIDO().getCodigo()==superficieInterna.getHallazgoREQUERIDO().getCodigo())
											{
												/*
												 * Elimino el hallazgo de las otras superficies
												 */
												superficieInterna.getProgramasOservicios().clear();
											}
										}
										
									}

								}
							}
						}
						/*
						 * Si no encontré la pieza, entonces la inactivo
						 */
						if(!encontrePieza)
						{
							detallePlan.getExisteBD().setActivo(false);
						}
					}
				}
			}
			
			/*
			 * Ahora verifico lo que no está en el plan de tratamiento que fue agregado en el XML
			 */
			
			/*
			 * Itero el plan generado del XML
			 */
			cicloPiezas:for(InfoDetallePlanTramiento detallePlanXML:infoPlanOdontoXML.getSeccionHallazgosDetalle())
			{

				/*
				 * Itero las superficies del plan generado del XML
				 */
				for(InfoHallazgoSuperficie superficieXML:detallePlanXML.getDetalleSuperficie())
				{
					/*
					 * Si está activo es porque no la he evaluado y no existe en el
					 * plan de tratamiento, la debo adicionar de una
					 */
					if(superficieXML.getExisteBD().isActivo())
					{
						/*
						 * Busco la pieza dental
						 */
						boolean encontrePieza=false;
						
						/*
						 * Creo una lista temporal para evitar ConcurrentModificationException
						 * Después de terminar la iteración adiciono al ArrayList del plan de tratamiento
						 */
						ArrayList<InfoHallazgoSuperficie> listaAgregar=new ArrayList<InfoHallazgoSuperficie>();

						int indiceDetallePlan=ConstantesBD.codigoNuncaValido;
						for(InfoDetallePlanTramiento detallePlan:detalleHallazgos)
						{
							if(detallePlanXML.getPieza().getCodigo()==detallePlan.getPieza().getCodigo())
							{
								encontrePieza=true;
								/*
								 * Convierte el código al verdadero código parametrizado en BD, se almacena
								 * en la tabla det_plan_tratamiento, campo superficie
								 */
								superficieXML.getSuperficieOPCIONAL().setCodigo(info.getCodigoSuperficePorSector(superficieXML.getSuperficieOPCIONAL().getCodigo2(), detallePlanXML.getPieza().getCodigo()));
								superficieXML.getInfoRegistroHallazgo().setFechaModifica(UtilidadFecha.getFechaActual());
								superficieXML.getInfoRegistroHallazgo().setHoraModifica(UtilidadFecha.getHoraActual());
								// Adiciono la superficie a la pieza existente
								listaAgregar.add(superficieXML);
								indiceDetallePlan++;
								break;
							}
							indiceDetallePlan++;
						}
						if(encontrePieza)
						{
							// Va a duplicar porque esto agregando todas las superficies al agregar la pieza
							/*
							 * En este caso ya existía la pieza, solamente se agrega la superficie
							 */
							Log4JManager.info("Adiciono una superficie que hace falta");
							detalleHallazgos.get(indiceDetallePlan).getDetalleSuperficie().addAll(listaAgregar);
						}
						else
						{
							// Como la pieza no existe la agrego tal como está con todas las superficies
							// Agrego la fecha y hora y modifico el codigo para que tome el sector que se encuentra en BD
							for(InfoHallazgoSuperficie superficieXMLInterna:detallePlanXML.getDetalleSuperficie())
							{
								superficieXMLInterna.getInfoRegistroHallazgo().setFechaModifica(UtilidadFecha.getFechaActual());
								superficieXMLInterna.getInfoRegistroHallazgo().setHoraModifica(UtilidadFecha.getHoraActual());
								superficieXMLInterna.getSuperficieOPCIONAL().setCodigo(info.getCodigoSuperficePorSector(superficieXMLInterna.getSuperficieOPCIONAL().getCodigo2(), detallePlanXML.getPieza().getCodigo()));
							}
							detalleHallazgos.add(detallePlanXML);
							
							// Como se agregó la pieza completa no necesito seguir recorriendo las superficies
							continue cicloPiezas; 
						}
					}
				}
			}
		}
		else
		{
			// Inactiva los dientes
			for(InfoDetallePlanTramiento detalle: info.getInfoPlanTrata().getSeccionHallazgosDetalle())
			{
				detalle.getExisteBD().setActivo(false);
			}
		}
		/*
		 * Llena los dientes usados para la sección de otros
		 */
		info.setArrayDientesUsados(llenarDienteUsados(info));
	}
	
	/*
	 * Este método era el anterior! lo dejo por si se me escapó algo importante
	 */
	private InfoOdontograma igualarPlanTratamientoAOdontograma1(InfoOdontograma info)
	{
		boolean encontroDiente = false; 
		boolean encontroSuperfice = false;
		
		//Toma la información en xml dada por el odontograma
		if(!info.getXmlOdontograma().equals(""))
		{
			InfoPlanTratamiento infoPlanOdonto =  getInfoPlanTratamientoDeXML(info.getXmlOdontograma());
			
			//Compara los dos planes de tratamiento, el que existía y el que proviene del odontograma
			ArrayList<InfoDetallePlanTramiento> arrayPlan = info.getInfoPlanTrata().getSeccionHallazgosDetalle();
			
			//Actualiza todos los dientes del plan de tratamiento a activo No
			
			for(int i=0;i<arrayPlan.size();i++)
			{
				InfoDetallePlanTramiento detallePlanTramiento=arrayPlan.get(i);
				for(InfoHallazgoSuperficie hallazgo:detallePlanTramiento.getDetalleSuperficie())
				{
					if(hallazgo.getExisteBD().isEsModificable())
					{
						arrayPlan.get(i).getExisteBD().setId(ConstantesBD.acronimoNo);
					}
				}
			}
			

			for(int po = 0; po<infoPlanOdonto.getSeccionHallazgosDetalle().size(); po++)
			{
				encontroDiente = false;
				//Busca las piezas dentales
				for(int p=0;p<arrayPlan.size();p++)
				{
					if(		   arrayPlan.get(p).getExisteBD().getId().equals(ConstantesBD.acronimoNo)
							&& arrayPlan.get(p).getExisteBD().isActivo()
							&& arrayPlan.get(p).getPieza().getCodigo() == infoPlanOdonto.getSeccionHallazgosDetalle().get(po).getPieza().getCodigo())
					{
						arrayPlan.get(p).getExisteBD().setId(ConstantesBD.acronimoSi);
						encontroDiente = true;

						//Actualiza todos las superficies a activo No
						for(int s=0; s<arrayPlan.get(p).getDetalleSuperficie().size(); s++)
						{
							arrayPlan.get(p).getDetalleSuperficie().get(s).getExisteBD().setId(ConstantesBD.acronimoNo);
							//arrayPlan.get(p).getDetalleSuperficie().get(s).getExisteBD().setValue(ConstantesBD.acronimoNo);
						}

						//Evalua las Superficies
						for(int so=0; so<infoPlanOdonto.getSeccionHallazgosDetalle().get(po).getDetalleSuperficie().size(); so++)
						{
							encontroSuperfice = false;
							
							for(int s=0; s<arrayPlan.get(p).getDetalleSuperficie().size(); s++)
							{
								//Log4JManager.info("valores >> "+arrayPlan.get(p).getDetalleSuperficie().get(s).getExisteBD().getId()+" "+arrayPlan.get(p).getDetalleSuperficie().get(s).getExisteBD().getActivo()+" cod "+arrayPlan.get(p).getDetalleSuperficie().get(s).getSuperficieOPCIONAL().getCodigo2()+" "+infoPlanOdonto.getSeccionHallazgosDetalle().get(po).getDetalleSuperficie().get(so).getSuperficieOPCIONAL().getCodigo2()+" "+arrayPlan.get(p).getDetalleSuperficie().get(s).getSuperficieOPCIONAL().getNombre()+" "+infoPlanOdonto.getSeccionHallazgosDetalle().get(po).getDetalleSuperficie().get(so).getSuperficieOPCIONAL().getNombre()+" cod pk detalle "+infoPlanOdonto.getSeccionHallazgosDetalle().get(po).getDetalleSuperficie().get(so).getCodigoPkDetalle());
								if(		arrayPlan.get(p).getDetalleSuperficie().get(s).getExisteBD().getId().equals(ConstantesBD.acronimoSi)
										&& 
											(
												arrayPlan.get(p).getDetalleSuperficie().get(s).getSuperficieOPCIONAL().getCodigo2() == infoPlanOdonto.getSeccionHallazgosDetalle().get(po).getDetalleSuperficie().get(so).getSuperficieOPCIONAL().getCodigo2() 
												||
												(arrayPlan.get(p).getDetalleSuperficie().get(s).getSuperficieOPCIONAL().getCodigo2() <= 0 && infoPlanOdonto.getSeccionHallazgosDetalle().get(po).getDetalleSuperficie().get(so).getSuperficieOPCIONAL().getCodigo2() <= 0)
											)
									)
								{
									// Si realmente existe en BD
									arrayPlan.get(p).getDetalleSuperficie().get(s).getExisteBD().setId(ConstantesBD.acronimoSi);
									
									// Si fue adicionado en una confirmación del odontograma anterior 
									//arrayPlan.get(p).getDetalleSuperficie().get(s).getExisteBD().setValue(ConstantesBD.acronimoSi);
									encontroSuperfice = true;
									
									//Evalúa si tienen el mismo hallazgo
									if(arrayPlan.get(p).getDetalleSuperficie().get(s).getHallazgoREQUERIDO().getCodigo() != 
										infoPlanOdonto.getSeccionHallazgosDetalle().get(po).getDetalleSuperficie().get(so).getHallazgoREQUERIDO().getCodigo())
									{
										//se elimino el hallazgo
										if(infoPlanOdonto.getSeccionHallazgosDetalle().get(po).getDetalleSuperficie().get(so).getHallazgoREQUERIDO().getCodigo() < 0)
										{
											arrayPlan.get(p).getDetalleSuperficie().get(s).getExisteBD().setActivo(false);
										}
										else
										{	
											/*
											 * VOY A PASAR ESTO PARA UN METODO PARA MAYOR COMODIDAD AL MODIFICAR (Juanda)
											 * Carga de programas para las superficies seleccionadas
											 *
											
											//Carga el Programa o Servicios parametrizados por defecto para el Hallazgo
											// El programa o servicio debe aplicar a una superficie únicamente XPlanner 2008 ID:154301
											DtoProgramasServiciosPlanT parametros = new DtoProgramasServiciosPlanT();
											parametros.setCodigoHallazgo(infoPlanOdonto.getSeccionHallazgosDetalle().get(po).getDetalleSuperficie().get(so).getHallazgoREQUERIDO().getCodigo());
											parametros.setBuscarProgramas(info.getEsBuscarPorPrograma());

											/*
											 * Busco los programas o servicios por defecto
											 *
											InfoProgramaServicioPlan progsev = PlanTratamiento.obtenerProgramaServicioParamHallazgo(parametros);

											if(progsev.getCodigoPkProgramaServicio().intValue() > 0 )
											{
												//logger.info("encontró servicio");
												if(progsev.getNumeroSuperficies()==1)
												{
													infoPlanOdonto.getSeccionHallazgosDetalle().get(po).getDetalleSuperficie().get(so).getProgramasOservicios().add(progsev);
													// OOJO  prueba equivalentes
													//logger.info("Tamaño de Array Programas 0.0 >> "+info.getInfoPlanTrata().getSeccionHallazgosDetalle().get(po).getDetalleSuperficie().get(so).getProgramasOservicios().size());
													//Log4JManager.info("PO "+po +" "+"SO "+so);
													info.getInfoPlanTrata().getSeccionHallazgosDetalle().get(po).getDetalleSuperficie().get(so).setProgramasOservicios(new ArrayList<InfoProgramaServicioPlan>());
													//logger.info("Tamaño de Array Programas 1.0 >> "+info.getInfoPlanTrata().getSeccionHallazgosDetalle().get(po).getDetalleSuperficie().get(so).getProgramasOservicios().size());
												
													info.getInfoPlanTrata().getSeccionHallazgosDetalle().get(po).getDetalleSuperficie().get(so).getProgramasOservicios().add(progsev);												
													//logger.info("Tamaño de Array Programas 1.1 >> "+info.getInfoPlanTrata().getSeccionHallazgosDetalle().get(po).getDetalleSuperficie().get(so).getProgramasOservicios().size());
													//logger.info("Tamaño de Array Programas 2 >> "+infoPlanOdonto.getSeccionHallazgosDetalle().get(po).getDetalleSuperficie().get(so).getProgramasOservicios().size());
												}
											}
											else
											{
												//logger.info("..:No encontró programas/servicios en el cambio de hallazgo "+UtilidadLog.obtenerStringHerencia(parametros, true));
												infoPlanOdonto.getSeccionHallazgosDetalle().get(po).getDetalleSuperficie().get(so).getProgramasOservicios().add(new InfoProgramaServicioPlan());
												// OOJO  prueba equivalentes
												//logger.info("Tamaño de Array Programas 0.0 >> "+info.getInfoPlanTrata().getSeccionHallazgosDetalle().get(po).getDetalleSuperficie().get(so).getProgramasOservicios().size());
												info.getInfoPlanTrata().getSeccionHallazgosDetalle().get(po).getDetalleSuperficie().get(so).setProgramasOservicios(new ArrayList<InfoProgramaServicioPlan>());
												//logger.info("Tamaño de Array Programas 1.0 >> "+info.getInfoPlanTrata().getSeccionHallazgosDetalle().get(po).getDetalleSuperficie().get(so).getProgramasOservicios().size());
											
												info.getInfoPlanTrata().getSeccionHallazgosDetalle().get(po).getDetalleSuperficie().get(so).getProgramasOservicios().add(new InfoProgramaServicioPlan());												
												//logger.info("Tamaño de Array Programas 1.1 >> "+info.getInfoPlanTrata().getSeccionHallazgosDetalle().get(po).getDetalleSuperficie().get(so).getProgramasOservicios().size());
												//logger.info("Tamaño de Array Programas 2 >> "+infoPlanOdonto.getSeccionHallazgosDetalle().get(po).getDetalleSuperficie().get(so).getProgramasOservicios().size());

											}
											 
											 * Hasta aqui lo separado
											 */
											arrayPlan.get(p).getDetalleSuperficie().get(s).setHallazgoREQUERIDO(infoPlanOdonto.getSeccionHallazgosDetalle().get(po).getDetalleSuperficie().get(so).getHallazgoREQUERIDO());
											//Informacion del path
											arrayPlan.get(p).getDetalleSuperficie().get(s).getHallazgoREQUERIDO().setDescripcion(infoPlanOdonto.getSeccionHallazgosDetalle().get(po).getDetalleSuperficie().get(so).getHallazgoREQUERIDO().getDescripcion());
										}
									}
									else
									{
										//Informacion del path
										arrayPlan.get(p).getDetalleSuperficie().get(s).getHallazgoREQUERIDO().setDescripcion(infoPlanOdonto.getSeccionHallazgosDetalle().get(po).getDetalleSuperficie().get(so).getHallazgoREQUERIDO().getDescripcion());
									}
								}
							}

							if(!encontroSuperfice)
							{
								Log4JManager.info("\n\n\n NO encontro Superficie ");
								infoPlanOdonto.getSeccionHallazgosDetalle().get(po).getDetalleSuperficie().get(so).getExisteBD().setValue(ConstantesBD.acronimoNo);
								infoPlanOdonto.getSeccionHallazgosDetalle().get(po).getDetalleSuperficie().get(so).getExisteBD().setActivo(true);
								infoPlanOdonto.getSeccionHallazgosDetalle().get(po).getDetalleSuperficie().get(so).getInfoRegistroHallazgo().setFechaModifica(UtilidadFecha.getFechaActual());
								infoPlanOdonto.getSeccionHallazgosDetalle().get(po).getDetalleSuperficie().get(so).getInfoRegistroHallazgo().setHoraModifica(UtilidadFecha.getHoraActual());
								infoPlanOdonto.getSeccionHallazgosDetalle().get(po).getDetalleSuperficie().get(so).getClasificacion().setValue("");

								//Activa la superficie
								infoPlanOdonto.getSeccionHallazgosDetalle().get(po).getDetalleSuperficie().get(so).getSuperficieOPCIONAL().setCodigo(
										info.getCodigoSuperficePorSector(infoPlanOdonto.getSeccionHallazgosDetalle().get(po).getDetalleSuperficie().get(so).getSuperficieOPCIONAL().getCodigo2(), infoPlanOdonto.getSeccionHallazgosDetalle().get(po).getPieza().getCodigo()));
								
								infoPlanOdonto.getSeccionHallazgosDetalle().get(po).getDetalleSuperficie().get(so).getSuperficieOPCIONAL().setActivo(true);
								
								//Carga el Programa o Servicios parametrizados por defecto para el Hallazgo
								DtoProgramasServiciosPlanT parametros = new DtoProgramasServiciosPlanT();
								parametros.setCodigoHallazgo(infoPlanOdonto.getSeccionHallazgosDetalle().get(po).getDetalleSuperficie().get(so).getHallazgoREQUERIDO().getCodigo());
								parametros.setBuscarProgramas(info.getEsBuscarPorPrograma());

								InfoProgramaServicioPlan progsev = PlanTratamiento.obtenerProgramaServicioParamHallazgo(parametros);
								if(progsev.getCodigoPkProgramaServicio().intValue() > 0 )
								{
									if(progsev.getNumeroSuperficies()==1)
									{
										infoPlanOdonto.getSeccionHallazgosDetalle().get(po).getDetalleSuperficie().get(so).getProgramasOservicios().add(progsev);
									}
								}
								/*
								else
								{
									logger.info("..:No encontro programas/servicios para "+UtilidadLog.obtenerStringHerencia(parametros, true));
								}*/

								arrayPlan.get(p).getDetalleSuperficie().add(infoPlanOdonto.getSeccionHallazgosDetalle().get(po).getDetalleSuperficie().get(so));
								logger.info("añade superficie >> "+infoPlanOdonto.getSeccionHallazgosDetalle().get(po).getDetalleSuperficie().get(so).getSuperficieOPCIONAL().getNombre());
							}
						}
						
						//Las Superficies que no fueron encontradas deben ser marcadas como activas False
						for(int s=0; s<arrayPlan.get(p).getDetalleSuperficie().size(); s++)
						{
							if(arrayPlan.get(p).getDetalleSuperficie().get(s).getExisteBD().getId().equals(ConstantesBD.acronimoNo) && 
									arrayPlan.get(p).getDetalleSuperficie().get(s).getExisteBD().isActivo())
								arrayPlan.get(p).getDetalleSuperficie().get(s).getExisteBD().setActivo(false);
						}
					}
				}
				
				//Si no encuentra el diente se añade al plan de tratamiento
				if(!encontroDiente)
				{
					logger.info("\n\n\n NO encontro Diente ");
					infoPlanOdonto.getSeccionHallazgosDetalle().get(po).getExisteBD().setValue(ConstantesBD.acronimoNo);
					infoPlanOdonto.getSeccionHallazgosDetalle().get(po).getExisteBD().setActivo(true);
						
					for(InfoHallazgoSuperficie detalle : infoPlanOdonto.getSeccionHallazgosDetalle().get(po).getDetalleSuperficie())
					{
						detalle.getClasificacion().setValue("");						
						detalle.getInfoRegistroHallazgo().setFechaModifica(UtilidadFecha.getFechaActual());
						detalle.getInfoRegistroHallazgo().setHoraModifica(UtilidadFecha.getHoraActual());
						
						//Activa la superficie
						detalle.getSuperficieOPCIONAL().setCodigo(info.getCodigoSuperficePorSector(detalle.getSuperficieOPCIONAL().getCodigo2(), infoPlanOdonto.getSeccionHallazgosDetalle().get(po).getPieza().getCodigo()));
						detalle.getSuperficieOPCIONAL().setActivo(true);
						
						/*
						 * VOY A PASAR ESTO PARA UN METODO PARA MAYOR COMODIDAD AL MODIFICAR (Juanda)
						 * Carga de programas para la pieza seleccionadas
						 *

						
						//Carga el Programa o Servicios parametrizados por defecto para el Hallazgo
						DtoProgramasServiciosPlanT parametros = new DtoProgramasServiciosPlanT();
						parametros.setBuscarProgramas(info.getEsBuscarPorPrograma());
						parametros.setCodigoHallazgo(detalle.getHallazgoREQUERIDO().getCodigo());
						
						InfoProgramaServicioPlan progsev = PlanTratamiento.obtenerProgramaServicioParamHallazgo(parametros);
						if(progsev.getCodigoPkProgramaServicio().intValue() > 0 ) 
						{
							if(progsev.getNumeroSuperficies()==1)
							{
								detalle.getProgramasOservicios().add(progsev);
							}
						}
						else
						{
							logger.info("..:No encontro programas/servicios para "+UtilidadLog.obtenerStringHerencia(parametros, true));
						}*/
					}
					
					arrayPlan.add(infoPlanOdonto.getSeccionHallazgosDetalle().get(po));
				}
			}
			
			/*
			 * Los dientes que no fueron encontrados deben ser marcados activos en False para que se puedan eliminar
			 * del plan de tratamiento
			 */
			for(int p=0;p<arrayPlan.size();p++)
			{
				if(arrayPlan.get(p).getExisteBD().getId().equals(ConstantesBD.acronimoNo) && 
						arrayPlan.get(p).getExisteBD().isActivo())
				{
					arrayPlan.get(p).getExisteBD().setActivo(false);
				}
			}
			
			info.getInfoPlanTrata().setSeccionHallazgosDetalle(arrayPlan);
		}
		else
		{
			// Inactiva los dientes
			for(InfoDetallePlanTramiento detalle: info.getInfoPlanTrata().getSeccionHallazgosDetalle())
			{
				detalle.getExisteBD().setActivo(false);
			}
		}

		//Actualiza los dientes usados para las seccion de otros
		info.setArrayDientesUsados(llenarDienteUsados(info));
		return info;
	}
	
	/**
	 * Encargado de cargar los programas para varias superficies para las cuales no hubo un candidato
	 * de una superficie por defecto.
	 * @param info {@link InfoOdontograma}
	 * @param seccion Secci&oacute;n a la que se le desean agregar los programas por defecto
	 * @return false en caso de existir alg&uacute;n error, true de lo contrario
	 * @author Juan David Ram&uacute;rez
	 * @since 04 May 2010
	 */
	private boolean cargarProgramasVariasSuperficies(InfoOdontograma info, String seccion)
	{
		/*
		 * Se toma el codigo tarifario para mostrar
		 * correctamente el código de los servicios según 
		 * el tarifario parametrizado en "Parámetros Generales (ValoresPorDefecto)"
		 */
		String codigoTarifarioServicios= ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(info.getInstitucion());

		/*
		 * Si la sección que se busca es detalle (Ya trae las superficies seleccionadas desde el ododntograma)
		 */
		if(seccion.equals(ConstantesIntegridadDominio.acronimoDetalle))
		{
			// Se iteran los hallazgos
			for(InfoDetallePlanTramiento detallePlanTratamiento:info.getInfoPlanTrata().getSeccionHallazgosDetalle())
			{
				if(detallePlanTratamiento.getExisteBD().isActivo())
				{
					// Se iteran las superficies (superficieExterna indica la superficie del ciclo externo)
					for(InfoHallazgoSuperficie superficieExterna:detallePlanTratamiento.getDetalleSuperficie())
					{
						if(superficieExterna.getExisteBD().isActivo() && superficieExterna.getProgramasOservicios().size()==0)
						{
							/*
							 * Defino una variable en la cual voy a contar el numero de superficies
							 * que tienen el mismo hallazgo para verificar la carga del programa
							 */
							int cantidadSuperficies=0;
			
							/*
							 * El caso en que no hay superficie indica que se seleccionó el diente entero 
							 */
							if(UtilidadTexto.isEmpty(superficieExterna.getSuperficieOPCIONAL().getNombre()))
							{
								ArrayList<InfoProgramaServicioPlan> listaProgramas=buscarProgramasParaPostular(0, superficieExterna.getHallazgoREQUERIDO().getCodigo(), info.getEsBuscarPorPrograma(), codigoTarifarioServicios);
								if(listaProgramas!=null)
								{
								   if(!superficieExterna.getExisteBD().getValue().equals(ConstantesBD.acronimoSi))
								      {
											/*
											 * Si solamente existe un programa, simplemente lo adiciono
											 */
											if(listaProgramas.size()==1)
											{
												superficieExterna.getProgramasOservicios().add(listaProgramas.get(0));
											}
											else
											{
												/*
												 * Itero los programas para buscar el primero
												 * por defecto para adicionarlo
												 */
												for(InfoProgramaServicioPlan programa:listaProgramas)
												{
													if(programa.getPorDefecto())
													{
														/*
														 * Se retorna el primer programa que encuentre por defecto
														 */
														programa.getProgHallazgoPieza().setColorLetra(ColoresPlanTratamiento.NEGRO.getColor());
														superficieExterna.getProgramasOservicios().add(programa);
														break;
													}
												}
											}
									
								         }
								     }
							   }
							/* Caso seleccion de superficies,
							 * voy a contar el número de superficies que tienen el mismo hallazgo (Mínimo debe retornar 1)
							 */
							else 
							{
								/*
								 * Itero las superficies (superficieInterna indica la superficie del ciclo interno)
								 * Se deben iterar 2 veces debido a que tengo que verificar las superficies que cumplen con el mismo hallazgo
								 */
								for(InfoHallazgoSuperficie superficieInterna:detallePlanTratamiento.getDetalleSuperficie())
								{
									//Log4JManager.info("superficieInterna "+superficieInterna.getSuperficieOPCIONAL().getNombre());
			
									if(superficieInterna.getExisteBD().isActivo())
									{
										if(superficieExterna.getHallazgoREQUERIDO().getCodigo()==superficieInterna.getHallazgoREQUERIDO().getCodigo())
										{
											/*
											 * Encontré una superficie con el mismo hallazgo, incremento la variable
											 */
											cantidadSuperficies++;
										}
									}
								}
							}
							/*
							 * Encontré superficies, ahora busco los programas que concuerden con el número de
							 * superficies con el mismo hallazgo
							 */
							if(cantidadSuperficies>0)
							{
								ColoresPlanTratamiento color=ColoresPlanTratamiento.NEGRO;
								if(cantidadSuperficies>1)
								{
									color=ColoresPlanTratamiento.MORADO;
								}
								ArrayList<InfoProgramaServicioPlan> listaProgramas=buscarProgramasParaPostular(cantidadSuperficies, superficieExterna.getHallazgoREQUERIDO().getCodigo(), info.getEsBuscarPorPrograma(), codigoTarifarioServicios);
								/*
								 * Verifico que la lista contenga algo, sino no pierdo el tiempo y salgo
								 */
								if(listaProgramas!=null )
								{						
								  if(!superficieExterna.getExisteBD().getValue().equals(ConstantesBD.acronimoSi))
								  {
									
											/*
											 * Si solamente existe un programa parametrizado adiciono así
											 * no esté marcado comopor defecto.
											 */
											if(listaProgramas.size()==1)
											{
												InfoProgramaServicioPlan progAdicionar=listaProgramas.get(0);
												DtoProgHallazgoPieza dtoProgHallazgoPieza=new DtoProgHallazgoPieza();
												dtoProgHallazgoPieza.setCodigoPk(info.getContadorCodigoTemporalProgramaHallazgoPieza());
												dtoProgHallazgoPieza.setColorLetra(color.getColor());
												progAdicionar.setProgHallazgoPieza(dtoProgHallazgoPieza);
												superficieExterna.getProgramasOservicios().add(progAdicionar);
											}
											else
											{
												/*
												 * Si existen varios verifico el
												 * primero que tenga valor por defecto en true
												 */
												for(InfoProgramaServicioPlan programa:listaProgramas)
												{
													if(programa.getPorDefecto())
													{
														DtoProgHallazgoPieza dtoProgHallazgoPieza=new DtoProgHallazgoPieza();
														dtoProgHallazgoPieza.setCodigoPk(info.getContadorCodigoTemporalProgramaHallazgoPieza());
														dtoProgHallazgoPieza.setColorLetra(color.getColor());
														programa.setProgHallazgoPieza(dtoProgHallazgoPieza);
														superficieExterna.getProgramasOservicios().add(programa);
													}
													/*
													 * Si no hay por defecto no lo adiciono
													 */
												}
											}
											/*
											 * Si hay mas superficies itero buscando superficies que tengan el mismo hallazgo para ponerle el programa
											 */
											if(cantidadSuperficies>1)
											{
												for(InfoHallazgoSuperficie superficieInterna:detallePlanTratamiento.getDetalleSuperficie())
												{
													if(superficieExterna.getHallazgoREQUERIDO().getCodigo()==superficieInterna.getHallazgoREQUERIDO().getCodigo())
													{
														if(superficieInterna.getExisteBD().isActivo())
														{
															if(superficieInterna.getSuperficieOPCIONAL().getCodigo2()!=superficieExterna.getSuperficieOPCIONAL().getCodigo2())
															{
																/*
																 * Pongo el programa que necesito ubicar
																 * Debo buscar de nuevo para que no pase por referencia
																 */
																ArrayList<InfoProgramaServicioPlan> listaProgramasOtrasSuperficies=buscarProgramasParaPostular(cantidadSuperficies, superficieExterna.getHallazgoREQUERIDO().getCodigo(), info.getEsBuscarPorPrograma(), codigoTarifarioServicios);
																/*
																 * Si solamente existe un programa se adiciona de una
																 */
																if(listaProgramas.size()==1)
																{
																	InfoProgramaServicioPlan progAdicionar=listaProgramasOtrasSuperficies.get(0);
																	DtoProgHallazgoPieza dtoProgHallazgoPieza=new DtoProgHallazgoPieza();
																	dtoProgHallazgoPieza.setCodigoPk(info.getContadorCodigoTemporalProgramaHallazgoPieza());
																	dtoProgHallazgoPieza.setColorLetra(color.getColor());
																	progAdicionar.setProgHallazgoPieza(dtoProgHallazgoPieza);
																	superficieInterna.getProgramasOservicios().add(progAdicionar);
																}
																else
																{
																	/*
																	 * Se recorren los programas encontrados para buscar el programa por defecto
																	 */
																	for(InfoProgramaServicioPlan programaOtrasSuperficies:listaProgramasOtrasSuperficies)
																	{
																		if(programaOtrasSuperficies.getPorDefecto())
																		{
																			DtoProgHallazgoPieza dtoProgHallazgoPieza=new DtoProgHallazgoPieza();
																			dtoProgHallazgoPieza.setCodigoPk(info.getContadorCodigoTemporalProgramaHallazgoPieza());
																			dtoProgHallazgoPieza.setColorLetra(color.getColor());
																			programaOtrasSuperficies.setProgHallazgoPieza(dtoProgHallazgoPieza);
																			superficieInterna.getProgramasOservicios().add(programaOtrasSuperficies);
																		}
																	}
																}
															}
														}
													}
												}
											}
								        }
								}
							}
						}
					}
				}
			}
		}
		else if(seccion.equals(ConstantesIntegridadDominio.acronimoOtro))
		{
			/*
			 * Si viene de la seccion otros hallazgos, no existen las superficies seleccionadas
			 * por lo tanto se debe verificar si el hallazgo seleccionado
			 * aplica para diente completo o para las superficies, si no hay hallazgo seleccionado
			 * se ingora el registro.
			 */
			
			// Se iteran los hallazgos
			for(InfoDetallePlanTramiento detallePlanTratamiento:info.getInfoPlanTrata().getSeccionOtrosHallazgos())
			{
				if(detallePlanTratamiento.getExisteBD().isActivo())
				{
					/*
					 * Siempre se toma la primera, ya que no hay posibilidad de tener más superficies en un
					 * hallazgo tipo otros
					 */
					InfoHallazgoSuperficie superficieExterna=detallePlanTratamiento.getDetalleSuperficie().get(0);
					if(superficieExterna.getExisteBD().isActivo() && superficieExterna.getNumeroProgramasActivos()==0)
					{
						/*
						 * Defino una variable en la cual voy a contar el numero de superficies
						 * que tienen el mismo hallazgo y pieza para verificar la carga del programa
						 */
						int cantidadSuperficies=0;
		
						/*
						 * Se iteran los demás registros para verificar si tienen el mismo hallazgo
						 */
						for(InfoDetallePlanTramiento detallePlanTratamientoInt:info.getInfoPlanTrata().getSeccionOtrosHallazgos())
						{
							//Log4JManager.info("superficieInterna "+superficieInterna.getSuperficieOPCIONAL().getNombre());
	
							if(detallePlanTratamientoInt.getExisteBD().isActivo())
							{
								/*
								 * Se evalúan si pertenecen a la misma pieza dental
								 */
								if(detallePlanTratamiento.getPieza().getCodigo()==detallePlanTratamientoInt.getPieza().getCodigo())
								{
									/*
									 * Siempre se toma la primera, ya que no hay posibilidad de tener más superficies en un
									 * hallazgo tipo otros
									 */
									InfoHallazgoSuperficie superficieInterna=detallePlanTratamientoInt.getDetalleSuperficie().get(0);
									if(superficieExterna.getHallazgoREQUERIDO().getCodigo()==superficieInterna.getHallazgoREQUERIDO().getCodigo())
									{
										/*
										 * Solamente se carga en las superficies que no tienen programa asignado
										 */
										if(superficieInterna.getNumeroProgramasActivos()==0)
										{
											/*
											 * Encontré una superficie con el mismo hallazgo, incremento la variable
											 */
											cantidadSuperficies++;
										}
									}
								}
							}
						}
						/*
						 * Encontré superficies, ahora busco los programas que concuerden con el número de
						 * superficies con el mismo hallazgo
						 */
						if(cantidadSuperficies>0)
						{
							ColoresPlanTratamiento color=ColoresPlanTratamiento.NEGRO;
							if(cantidadSuperficies>1)
							{
								color=ColoresPlanTratamiento.MORADO;
							}
							ArrayList<InfoProgramaServicioPlan> listaProgramas=buscarProgramasParaPostular(cantidadSuperficies, superficieExterna.getHallazgoREQUERIDO().getCodigo(), info.getEsBuscarPorPrograma(), codigoTarifarioServicios);
							/*
							 * Verifico que la lista contenga algo, sino no pierdo el tiempo y salgo
							 */
							if(listaProgramas!=null)
							{	
								if(!superficieExterna.getExisteBD().getValue().equals(ConstantesBD.acronimoSi))
							     {							
									/*
									 * Si solamente existe un programa parametrizado adiciono así
									 * no esté marcado comopor defecto.
									 */
									if(listaProgramas.size()==1)
									{
										InfoProgramaServicioPlan progAdicionar=listaProgramas.get(0);
										DtoProgHallazgoPieza dtoProgHallazgoPieza=new DtoProgHallazgoPieza();
										dtoProgHallazgoPieza.setColorLetra(color.getColor());
										progAdicionar.setProgHallazgoPieza(dtoProgHallazgoPieza);
										superficieExterna.getProgramasOservicios().add(progAdicionar);
									}
									else
									{
										/*
										 * Si existen varios verifico el
										 * primero que tenga valor por defecto en true
										 */
											for(InfoProgramaServicioPlan programa:listaProgramas)
											{
												if(programa.getPorDefecto())
												{
													DtoProgHallazgoPieza dtoProgHallazgoPieza=new DtoProgHallazgoPieza();
													dtoProgHallazgoPieza.setCodigoPk(info.getContadorCodigoTemporalProgramaHallazgoPieza());
													dtoProgHallazgoPieza.setColorLetra(color.getColor());
													programa.setProgHallazgoPieza(dtoProgHallazgoPieza);
													superficieExterna.getProgramasOservicios().add(programa);
												}
												/*
												 * Si no hay por defecto no lo adiciono
												 */
											}
									   }
										/*
										 * Si hay mas superficies itero buscando superficies que tengan el mismo hallazgo para ponerle el programa
										 */
										if(cantidadSuperficies>1)
										{
											for(InfoDetallePlanTramiento detallePlanTratamientoOtro:info.getInfoPlanTrata().getSeccionOtrosHallazgos())
											{
												/*
												 * Si es un detalle plan de tratamiento es diferente, ya que
												 * al que se está trabajando ya se le agregó el programa
												 */
												if(detallePlanTratamiento!=detallePlanTratamientoOtro)
												{
													for(InfoHallazgoSuperficie superficieInterna:detallePlanTratamientoOtro.getDetalleSuperficie())
													{
														if(superficieExterna.getHallazgoREQUERIDO().getCodigo()==superficieInterna.getHallazgoREQUERIDO().getCodigo())
														{
															/*
															 * Solamente se agrega en superficies que no tienen seleccionado un programa
															 */
															if(superficieInterna.getNumeroProgramasActivos()==0)
															{
																/*
																 * Verifica que la superficies esté activa
																 */
																if(superficieInterna.getExisteBD().isActivo())
																{
																	/*
																	 * Pongo el programa que necesito ubicar
																	 * Debo buscar de nuevo para que no pase por referencia
																	 */
																	ArrayList<InfoProgramaServicioPlan> listaProgramasOtrasSuperficies=buscarProgramasParaPostular(cantidadSuperficies, superficieExterna.getHallazgoREQUERIDO().getCodigo(), info.getEsBuscarPorPrograma(), codigoTarifarioServicios);
																	/*
																	 * Si solamente existe un programa se adiciona de una
																	 */
																	if(listaProgramas.size()==1)
																	{
																		InfoProgramaServicioPlan progAdicionar=listaProgramasOtrasSuperficies.get(0);
																		DtoProgHallazgoPieza dtoProgHallazgoPieza=new DtoProgHallazgoPieza();
																		dtoProgHallazgoPieza.setCodigoPk(info.getContadorCodigoTemporalProgramaHallazgoPieza());
																		dtoProgHallazgoPieza.setColorLetra(color.getColor());
																		progAdicionar.setProgHallazgoPieza(dtoProgHallazgoPieza);
																		superficieInterna.getProgramasOservicios().add(progAdicionar);
																	}
																	else
																	{
																		/*
																		 * Se recorren los programas encontrados para buscar el programa por defecto
																		 */
																		for(InfoProgramaServicioPlan programaOtrasSuperficies:listaProgramasOtrasSuperficies)
																		{
																			if(programaOtrasSuperficies.getPorDefecto())
																			{
																				DtoProgHallazgoPieza dtoProgHallazgoPieza=new DtoProgHallazgoPieza();
																				dtoProgHallazgoPieza.setColorLetra(color.getColor());
																				dtoProgHallazgoPieza.setCodigoPk(info.getContadorCodigoTemporalProgramaHallazgoPieza());
																				programaOtrasSuperficies.setProgHallazgoPieza(dtoProgHallazgoPieza);
																				superficieInterna.getProgramasOservicios().add(programaOtrasSuperficies);
																			}
																		}
																	}
																}
															}
														}
													}
												}
											}
										} 								
							      }
							}
						}
					}
				}
			}
		}
		else if(seccion.equals(ConstantesIntegridadDominio.acronimoBoca))
		{
			// Se iteran las superficies (superficieExterna indica la superficie del ciclo externo)
			for(InfoHallazgoSuperficie superficieExterna:info.getInfoPlanTrata().getSeccionHallazgosBoca())
			{
				if(superficieExterna.getExisteBD().isActivo() && superficieExterna.getProgramasOservicios().size()==0)
				{
					ArrayList<InfoProgramaServicioPlan> listaProgramas=buscarProgramasParaPostular(0, superficieExterna.getHallazgoREQUERIDO().getCodigo(), info.getEsBuscarPorPrograma(), codigoTarifarioServicios);
					if(listaProgramas!=null)
					{
						/*
						 * Si solamente existe un programa, simplemente lo adiciono
						 */
						if(listaProgramas.size()==1)
						{
							superficieExterna.getProgramasOservicios().add(listaProgramas.get(0));
						}
						else
						{
							/*
							 * Itero los programas para buscar el primero
							 * por defecto para adicionarlo
							 */
							for(InfoProgramaServicioPlan programa:listaProgramas)
							{
								if(programa.getPorDefecto())
								{
									/*
									 * Se retorna el primer proframa que encuentre por defecto
									 */
									programa.getProgHallazgoPieza().setColorLetra(ColoresPlanTratamiento.NEGRO.getColor());
									superficieExterna.getProgramasOservicios().add(programa);
									break;
								}
							}
						}
					}
				}
			}
		}
		return true;
	}

	/**
	 * Busca los posibles candidatos para postular los programas al confirmar el odontograma (componente flash)
	 * @param cantidadSuperficies
	 * @param codigoHallazgo
	 * @param buscarPorPrograma
	 * @return 
	 */
	private ArrayList<InfoProgramaServicioPlan> buscarProgramasParaPostular(Integer cantidadSuperficies, int codigoHallazgo, String buscarPorPrograma, String codigoTarifarioServ)
	{
		DtoProgramasServiciosPlanT parametros = new DtoProgramasServiciosPlanT();
		parametros.setBuscarProgramas(buscarPorPrograma);
		parametros.setCodigoHallazgo(codigoHallazgo);
		parametros.setNumeroSuperficies(cantidadSuperficies);
		parametros.setCodigoTarifario(codigoTarifarioServ);
		return PlanTratamiento.listarProgramaServicioParamHallazgo(parametros);
	}

	/**
	 * Construye el objeto de InfoPlanTratamiento a partir de un xml
	 * */
	private InfoPlanTratamiento getInfoPlanTratamientoDeXML(String xml)
	{
		InfoPlanTratamiento info = new InfoPlanTratamiento();
		
		Log4JManager.info(xml);
		
		if(!xml.equals(""))
		{
	        try 
	        {
	            // 2. Usar DocumentBuilderFactory para crear un DocumentBuilder
	        	DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
				builderFactory.setNamespaceAware(true);
	            DocumentBuilder builder = builderFactory.newDocumentBuilder();
	        	
	            // 3. Parsear a partir de un Strig
	            StringBuffer StringBuffer1 = new StringBuffer(xml);
	            ByteArrayInputStream bs = new ByteArrayInputStream(StringBuffer1.toString().getBytes());
				Document docR = builder.parse (bs);
				
	            //Obtener el documento raiz
	        	//Se crea un objeto Element, a partir del documento XML
	            Element docEle = docR.getDocumentElement();
	            
	            //Obtener un nodelist de elementos <contenido>
	            NodeList nl = docEle.getElementsByTagName("diente");
	            
	            if(nl != null && nl.getLength() > 0)
	            {
	                for(int i = 0; i < nl.getLength(); i++)
	                {
	                	//Toma las piezas dentales
	                    Element piezaElemento = (Element) nl.item(i);
	                    
	                    InfoDetallePlanTramiento infoDiente = new InfoDetallePlanTramiento();
	                    infoDiente.setPieza(new InfoDatosInt(Utilidades.convertirAEntero(piezaElemento.getAttribute("pieza"))));
	                    NodeList superficieNodo = piezaElemento.getElementsByTagName("superficie");
	                    
	                    for(int j=0; j<superficieNodo.getLength(); j++)
		                {
	                    	Element superficieElemento = (Element) superficieNodo.item(j);
	                    	
	                    	InfoHallazgoSuperficie infoHallazgo = new InfoHallazgoSuperficie();
	                    	Element hallazgoElemento = (Element)superficieElemento.getElementsByTagName("hallazgo").item(0);
	                    	InfoDatosInt tmp = new InfoDatosInt();
	                    	
	                    	/*
	                    	 * En este caso se va a buscar el diente
	                    	 */
	                    	if(superficieElemento.getAttribute("nombre").toString().equals("diente") )
	                    	{
	                    		if(UtilidadTexto.getBoolean(superficieElemento.getAttribute("modificable")))
	                    		{
		                    		tmp.setCodigo(Utilidades.convertirAEntero(hallazgoElemento.getElementsByTagName("codigo").item(0).getFirstChild().getNodeValue()));
		                    		tmp.setNombre(hallazgoElemento.getElementsByTagName("descripcion").item(0).getFirstChild().getNodeValue());
		                    		
		                    		if(!hallazgoElemento.getElementsByTagName("path").item(0).getTextContent().equals(""))
		                    			tmp.setDescripcion(hallazgoElemento.getElementsByTagName("path").item(0).getFirstChild().getNodeValue()+"");
		                    		else
		                    			tmp.setDescripcion("");
		                    		
		                    		if(!hallazgoElemento.getElementsByTagName("convencion").item(0).getTextContent().equals(""))
		                    			infoHallazgo.setCodigoConvencion(hallazgoElemento.getElementsByTagName("convencion").item(0).getFirstChild().getNodeValue()+"");
		                    		else
		                    			infoHallazgo.setCodigoConvencion("");
		                    		
		                    		infoHallazgo.setHallazgoREQUERIDO(tmp);
		                    		
		                    		infoHallazgo.getInfoRegistroHallazgo().setFechaModifica(UtilidadFecha.getFechaActual());
									infoHallazgo.getInfoRegistroHallazgo().setHoraModifica(UtilidadFecha.getHoraActual());

		                    		infoHallazgo.getSuperficieOPCIONAL().setCodigo2(Utilidades.convertirAEntero(superficieElemento.getAttribute("sector").toString()));
		                    		/*
		                    		 * Activo este atributo para verificar que se ingrese al plan de tratamiento
		                    		 */
		                    		infoHallazgo.getExisteBD().setActivo(true);
		                    		infoDiente.getDetalleSuperficie().add(infoHallazgo);
	                    		}
	                    	}
	                    	else
	                    	{
	                    		if(UtilidadTexto.getBoolean(superficieElemento.getAttribute("modificable")))
	                    		{
		                    		tmp.setCodigo(Utilidades.convertirAEntero(hallazgoElemento.getElementsByTagName("codigo").item(0).getFirstChild().getNodeValue()));
		                    		tmp.setNombre(hallazgoElemento.getElementsByTagName("descripcion").item(0).getFirstChild().getNodeValue());
		                    		
		                    		if(!hallazgoElemento.getElementsByTagName("path").item(0).getTextContent().equals(""))
		                    		{
		                    			tmp.setDescripcion(hallazgoElemento.getElementsByTagName("path").item(0).getFirstChild().getNodeValue()+"");
		                    		}
		                    		else
		                    		{
		                    			tmp.setDescripcion("");
		                    		}
		                    		
		                    		if(!hallazgoElemento.getElementsByTagName("convencion").item(0).getTextContent().equals(""))
		                    		{
		                    			infoHallazgo.setCodigoConvencion(hallazgoElemento.getElementsByTagName("convencion").item(0).getFirstChild().getNodeValue()+"");
		                    		}
		                    		else
		                    		{
		                    			infoHallazgo.setCodigoConvencion("");
		                    		}
		                    		
		                    		infoHallazgo.setHallazgoREQUERIDO(tmp);
		                    		
		                    		tmp = new InfoDatosInt();
		                    		tmp.setCodigo(Utilidades.convertirAEntero(superficieElemento.getAttribute("codigo").toString()));
		                    		tmp.setNombre(superficieElemento.getAttribute("nombre").toString());
		                    		infoHallazgo.setSuperficieOPCIONAL(tmp);
		                    		
		                    		infoHallazgo.getInfoRegistroHallazgo().setFechaModifica(UtilidadFecha.getFechaActual());
									infoHallazgo.getInfoRegistroHallazgo().setHoraModifica(UtilidadFecha.getHoraActual());

		                    		infoHallazgo.getSuperficieOPCIONAL().setCodigo2(Utilidades.convertirAEntero(superficieElemento.getAttribute("sector").toString()));
		                    		/*
		                    		 * Activo este atributo para verificar que se ingrese al plan de tratamiento
		                    		 */
		                    		infoHallazgo.getExisteBD().setActivo(true);
		                    		infoDiente.getDetalleSuperficie().add(infoHallazgo);
                   				}
	                    	}
		                }
	                    
	                    info.getSeccionHallazgosDetalle().add(infoDiente);
	                }
	            }
	        }
	        catch (ParserConfigurationException pce) {  //Capturamos los errores, si los hubiera
	            pce.printStackTrace();
	        } catch (SAXException se) {
	            se.printStackTrace();
	        } catch (IOException ioe) {
	            ioe.printStackTrace();
	        }
		}
		
		//logs
		/*
		for(InfoDetallePlanTramiento pieza:info.getSeccionHallazgosDetalle())
		{
			logger.info("pieza >> "+pieza.getPieza().getCodigo());

			for(InfoHallazgoSuperficie superficie:pieza.getDetalleSuperficie())
			{
				logger.info(" "+superficie.getSuperficieOPCIONAL().getNombre()+" "+superficie.getHallazgoREQUERIDO().getNombre()+" ");
			}
		}
		*/
		
		return info;
	}
	
	
	/**
	 * Guarda la información del odontograma
	 * @param InfoOdontograma info
	 * */
	private InfoOdontograma accionGuardarOdont(InfoOdontograma info, boolean aplicaInclusion)
	{
		//Se usa para almacenar el nuevo codigoPk del plan tratamiento en caso de no existir
		info.setIndicadorAuxBd(new BigDecimal(ConstantesBD.codigoNuncaValido));
		
		//Actuliza el xml con el estado actual del plan de tratamiento
		info.setXmlOdontograma(getXmlPlanTratamiento(info));
		
		if(porConfirmar!=null && porConfirmar.equals(ConstantesBD.acronimoNo)){
			
			validacionesPlanTratamiento(info);
			
		}else if(info.getPorConfirmar()!=null){
			
			if(info.getPorConfirmar().equals(ConstantesBD.acronimoNo) || info.getPorConfirmar().equals("")){
				
				validacionesPlanTratamiento(info);
			}
		}
		
		if(!this.errores.isEmpty())
		{
			return info;
		}
		//-----------------------------
		
		DtoPlanTratamientoOdo dtoPlan = new DtoPlanTratamientoOdo();
		dtoPlan.setUsuarioModifica(info.getUsuarioActual());
		dtoPlan.setUsuarioGrabacion(info.getUsuarioActual().getUsuarioModifica());

		//*******************************************************************
		//Guarda la Información del Odontograma
		DtoOdontograma dtoOdo = llenarDtoOdontogramaConInfo(info);
		
	
		dtoOdo.setCodigoPk(PlanTratamiento.guardarOdontograma(this.conInterna,dtoOdo)); 
		if(dtoOdo.getCodigoPk() <= 0)
		{
			adicionarError("errors.notEspecific","No se Logro Guardar El Odontograma");
			return info;
		}

		
		//Guarda información del plan de tratamiento
		if(info.getInfoPlanTrata().getCodigoPk().doubleValue() <= 0)
		{
			
			dtoPlan = llenarDtoPlanTrataConInfo(info,dtoOdo);

			dtoPlan.setEstado(ConstantesIntegridadDominio.acronimoEstadoActivo);
			
			/*
			 *******************************************************************************************************
			 * 	GUARDAR PLAN DE TRATAMIENTO
			 */
			dtoPlan.setCodigoPk(new BigDecimal(PlanTratamiento.guardarPlanTratamiento(this.conInterna,dtoPlan))); // GUARDAR INFORMACION PLAN DE TRATAMIENTO
			if(dtoPlan.getCodigoPk().doubleValue() <= 0)
			{
				adicionarError("errors.notEspecific","No se Logro Guardar El Plan de Tratamiento"); // VALIDACION SI GUARDO COR
				return info;
			}
			
			info.setIndicadorAuxBd(dtoPlan.getCodigoPk());
			logger.info("..:Se Creara un Nuevo Plan Tratamiento. codigo >>  "+dtoPlan.getCodigoPk().doubleValue());
		}
		else
		{
			logger.info("..:Ya existe Plan Tratamiento. codigo >>  "+info.getInfoPlanTrata().getCodigoPk());
			//falta imagen
			/*
			 * MODIFCAR PLAN DE TRATAMIENTO
			 */
			if(info.getCodigoEvolucion()<=0)
			{
				accionModificarPlanTratamiento(info, dtoOdo);
			}
			
			info.setIndicadorAuxBd(info.getInfoPlanTrata().getCodigoPk());
			dtoPlan.setCodigoPk(info.getInfoPlanTrata().getCodigoPk());
			dtoPlan.setCodigoEvolucion(new BigDecimal(info.getCodigoEvolucion()));
			dtoPlan.setCodigoValoracion(new BigDecimal(info.getCodigoValoracion()));
		}
		
		//******************************************************************************************************
		/* ****************************************************************************************
		 * GUARDA SECCIONES PLAN DE TRATAMIENTO 
		 */
		logger.info("\n\n GUARDA SECCIONES PLAN DE TRATAMIENTO\n \n");
		
		//Guarda Sección Plan Tratamiento
		if(!guardarSeccionPlanTratamiento(this.conInterna,info/*InfoOdontograma */, dtoPlan /*DtoPlanTratamientoOdo */ , aplicaInclusion) 
			|| !guardarSeccionOtrosHallazgos(this.conInterna, info, dtoPlan, aplicaInclusion)
				|| !guardarSeccionHallazgosBoca(this.conInterna, info, dtoPlan, aplicaInclusion))
		{
			return info;
		}
		
		return info;
	}

	
	
	/**
	 * Método que realiza las validaciones necesarias para poder guardar el plan de tratamiento.
	 * antes existia un metodo de validacionesGenerales, pero por alguna razon juan david lo quito, por eso
	 * creo uno nuevo para hacer las validaciones, por ahora solo se ha pedido hacer requerido la clasificaicon
	 * en la seccion nuevo hallazgo.
	 * @author Armando.
	 * @param info
	 */
	private void validacionesPlanTratamiento(InfoOdontograma info) 
	{
		
		for(InfoDetallePlanTramiento diente:info.getInfoPlanTrata().getSeccionHallazgosDetalle())
		{
			//Recorre los hallazgos
			for(InfoHallazgoSuperficie hallazgo:diente.getDetalleSuperficie())
			{
				if(hallazgo.getClasificacion().getValue().isEmpty() && hallazgo.getNumProgramasServiciosActivos() > 0)
				{
					adicionarError("errors.notEspecific","No existe Información de Clasificación para el Hallazgo ["+hallazgo.getHallazgoREQUERIDO().getNombre().toLowerCase()+"] ");
				}
			}
		}
		
	}

	/**
	 * 
	 * @author CARVAJAL
	 * @param info
	 * @param dtoOdo
	 */
	private void accionModificarPlanTratamiento(InfoOdontograma info, DtoOdontograma dtoOdo) 
	{
		
		DtoPlanTratamientoOdo dtoActualizar= new DtoPlanTratamientoOdo();
		dtoActualizar.setCodigoPk(info.getInfoPlanTrata().getCodigoPk());
		dtoActualizar.setOdontogramaDiagnostico(new BigDecimal(dtoOdo.getCodigoPk() ));
		/*
		 * MODIFICAR PLAN DE TRATAMIENTO
		 */
		if(!PlanTratamiento.modificar(new DtoPlanTratamientoOdo(), dtoActualizar, this.conInterna) )
		{
			logger.info("Error Modificar esta LINEA");
			//TODO MODIFICAR ESTA LINEA
		}
	}
	

	/**
	 * Guarda la secci&oacute;n plan de tratamiento
	 * sin incluir otros hallazgos ni hallazgos en boca
	 * @param con Conexi&oacute;n con la BD
	 * @param info Informaci&oacute;n del odontograma
	 * @param dtoPlan Informaci&oacute;n del plan de tratamiento
	 * @return true en caso de salir todo bien, false de lo contrario
	 */
	
	public boolean guardarSeccionPlanTratamiento(Connection con,InfoOdontograma info,DtoPlanTratamientoOdo dtoPlan, boolean aplicaInclusion)
	{
		// GUARDA INFORMACIÓN DE LA SECCIÓN DETALLE PLAN TRATAMIENTO INICIAL
		DtoDetallePlanTratamiento dtoHallazgo =  new DtoDetallePlanTratamiento();
		dtoHallazgo.setFechaUsuarioModifica(dtoPlan.getUsuarioModifica());
		//Recorre los dientes
		
		for(InfoDetallePlanTramiento diente:info.getInfoPlanTrata().getSeccionHallazgosDetalle())
		{
			//logger.info("**>>diente > "+diente.getPieza().getCodigo()+" activo >> "+diente.getExisteBD().getActivo()+" existe >> "+diente.getExisteBD().getValue());
			if(diente.getExisteBD().isActivo())
			{
				//Recorre los hallazgos
				for(InfoHallazgoSuperficie hallazgo:diente.getDetalleSuperficie())
				{
					/*
					 *  Si es modificable entonces genero un nuevo registro con
					 *  los datos nuevos (Juanda)
					 */
					if(hallazgo.getExisteBD().isEsModificable())
					{
						if(hallazgo.getExisteBD().isActivo())
						{
							// Hallazgo modificado
							if(hallazgo.getExisteBD().getValue().equals(ConstantesBD.acronimoSi))
							{
								dtoHallazgo.setCodigo(hallazgo.getCodigoPkDetalle().doubleValue());
								dtoHallazgo.setCodigoCita(new BigDecimal(info.getCodigoCita()));
								dtoHallazgo.setEvolucion(new BigDecimal(info.getCodigoEvolucion()));
								dtoHallazgo.setValoracion(new BigDecimal(info.getCodigoValoracion()));
								
								// Agregado
								dtoHallazgo.setClasificacion(hallazgo.getClasificacion().getValue());
								
								DtoInfoFechaUsuario usuarioModifica = new DtoInfoFechaUsuario();
								usuarioModifica.setFechaModifica(UtilidadFecha.getFechaActual());
								usuarioModifica.setHoraModifica(UtilidadFecha.getHoraActual());
								usuarioModifica.setUsuarioModifica(info.getUsuarioActual().getUsuarioModifica());
								
								boolean errorGenerado = actualizarDetPlanTratamiento(con, aplicaInclusion, dtoHallazgo, usuarioModifica, diente, "seccionPlanTratamiento");
							
								if(!errorGenerado){
									
									return errorGenerado;
								}
							}
							// Hallazgo nuevo
							else
							{
								dtoHallazgo = llenarDtoDetallePlanTratConInfo(
													hallazgo,
													dtoPlan,
													diente.getPieza().getCodigo(),
													ConstantesIntegridadDominio.acronimoDetalle);
								
								dtoHallazgo.setCodigoCita(new BigDecimal(info.getCodigoCita()));
								dtoHallazgo.setEvolucion(new BigDecimal(info.getCodigoEvolucion()));
								dtoHallazgo.setValoracion(new BigDecimal(info.getCodigoValoracion()));
								
								// Aqui es donde guardaaaaaaaaaaaaaaaaa
								dtoHallazgo.setCodigo(PlanTratamiento.guardarDetPlanTratamiento(con, dtoHallazgo));
								
								//logger.info("\n hallazgo Superficie a guardar >> "+hallazgo.getSuperficieOPCIONAL().getNombre()+" "+hallazgo.getHallazgoREQUERIDO().getNombre()+" pieza >> "+diente.getPieza().getCodigo()+" codigppk >> "+dtoHallazgo.getCodigo());
								if(dtoHallazgo.getCodigo() <= 0)
								{
									adicionarError("errors.notEspecific","No se Logro Guardar El Detalle del Plan de Tratamiento");
									return false;
								}
							}
							
							//******************************************************************************************************************
							
							//Recorre los programas y servicios
							for(InfoProgramaServicioPlan programa:hallazgo.getProgramasOservicios())
							{
								//Evalua si es por programas o servicios
								if(programa.getCodigoPkProgramaServicio().intValue() > 0)
								{
									if(programa.getExisteBD().isActivo())
									{
										if(!programa.getExisteBD().getValue().equals(ConstantesBD.acronimoSi))
										{
											//Recorre los servicios del programa
											for(InfoServicios servicios:programa.getListaServicios())
											{
												DtoProgramasServiciosPlanT dtoProg =  llenarDtoProgramasPlanTratConInfo(
																							programa.getCodigoPkProgramaServicio().doubleValue(),
																							dtoHallazgo,
																							servicios,
																							aplicaInclusion);
											
												
												dtoProg.setCodigoCita(new BigDecimal(info.getCodigoCita()));
												dtoProg.setEvolucion(new BigDecimal(info.getCodigoEvolucion()));
												dtoProg.setValoracion(new BigDecimal(info.getCodigoValoracion()));
												dtoProg.setEspecialidad(new InfoDatosInt(info.getEspecialidad()));
												
												
												dtoProg.setCodigoPk(new BigDecimal(PlanTratamiento.guardarProgramasServicio(con,dtoProg)));
												
												if(dtoProg.getCodigoPk().doubleValue() <= 0)
												{
													adicionarError("errors.notEspecific","No se Logro Guardar Los Programas/Servicios");
													return false;
												}
											}
										}
									}
									else
									{	
										/*
										 * Inactiva los que fueron eliminados del odontograma
										 */
										if(programa.getExisteBD().getValue().equals(ConstantesBD.acronimoSi))
										{
											for(InfoServicios servicios:programa.getListaServicios())
											{
												DtoProgramasServiciosPlanT parametrosServ = new DtoProgramasServiciosPlanT();
												parametrosServ.setCodigoPk(servicios.getCodigoPkProgServ());
												parametrosServ.setActivo(ConstantesBD.acronimoNo);
												if(!PlanTratamiento.actualizarActivoProgServPlanTr(con, parametrosServ))
												{
													adicionarError("errors.notEspecific","No se Inactivo Programas/Servicios");
													return false;
												}
											}
										}
									}
								}
								else
								{
									//Recorre los servicios del programa
									for(InfoServicios servicios:programa.getListaServicios())
									{
										if(servicios.getExisteBD().isActivo())
										{
											if(!servicios.getExisteBD().getValue().equals(ConstantesBD.acronimoSi))
											{
												DtoProgramasServiciosPlanT dtoProg =  llenarDtoProgramasPlanTratConInfo(
																							ConstantesBD.codigoNuncaValidoDouble,
																							dtoHallazgo,
																							servicios,aplicaInclusion);
												
												dtoProg.setCodigoPk(new BigDecimal(PlanTratamiento.guardarProgramasServicio(con,dtoProg)));
												if(dtoProg.getCodigoPk().doubleValue() <= 0)
												{
													adicionarError("errors.notEspecific","No se Logro Guardar Los Programas/Servicios");
													return false;
												}
											}
										}
										else
										{
											if(servicios.getExisteBD().getValue().equals(ConstantesBD.acronimoSi))
											{
												DtoProgramasServiciosPlanT parametrosServ = new DtoProgramasServiciosPlanT();
												parametrosServ.setCodigoPk(servicios.getCodigoPkProgServ());
												parametrosServ.setActivo(ConstantesBD.acronimoNo);
												
												if(!PlanTratamiento.actualizarActivoProgServPlanTr(con, parametrosServ))
												{
													adicionarError("errors.notEspecific","No se Inactivo Programas/Servicios");
													return false;
												}
											}
										}
									}
								}
							}
						}
						else
						{
							// Solo inactiva los que esten guardados en base de datos
							if(hallazgo.getExisteBD().getValue().equals(ConstantesBD.acronimoSi))
							{
								boolean inactivo = false;
								DtoDetallePlanTratamiento parametros = new DtoDetallePlanTratamiento();
								parametros.setCodigo(hallazgo.getCodigoPkDetalle().doubleValue());
								parametros.setActivo(ConstantesBD.acronimoNo);
								
								// Se inactiva el detalle del plan de tratamiento.
								inactivo = PlanTratamiento.actualizarActivoDetallePlanTrat(con, parametros);
								if(!inactivo)
								{
									adicionarError("errors.notEspecific","No se Logro Inactivar el Detalle del Plan de Tratamiento en la Inactivación");
									return false;
								}
								
								//Se modifica los programas y servicios a activo NO
								boolean inactivarPrograma = false;
								//Recorre los programas/servicios
								for(InfoProgramaServicioPlan programas:hallazgo.getProgramasOservicios())
								{
									inactivarPrograma = false;
									
									//si es programa
									if(programas.getCodigoPkProgramaServicio().doubleValue() > 0 && 
										programas.getExisteBD().getValue().equals(ConstantesBD.acronimoSi))
									{
										inactivarPrograma = true;
									}
									
									for(InfoServicios servicios:programas.getListaServicios())
									{
										if((servicios.getExisteBD().getValue().equals(ConstantesBD.acronimoSi) && 
											servicios.getExisteBD().isActivo() && !inactivarPrograma) || inactivarPrograma)
										{
											DtoProgramasServiciosPlanT parametrosServ = new DtoProgramasServiciosPlanT();
											parametrosServ.setCodigoPk(servicios.getCodigoPkProgServ());
											parametrosServ.setActivo(ConstantesBD.acronimoNo);
	
											inactivo = PlanTratamiento.actualizarActivoProgServPlanTr(con, parametrosServ);
											if(!inactivo)
											{
												adicionarError("errors.notEspecific","No se Inactivo Programas/Servicios");
												return false;
											}
										}
									}
								}
							}
						}
					}
					/*
					 * Si no es modificable, entonces guardo un nuevo registro con los mismos
					 * datos, para mantener el histórico de lo que se está generando
					 * en el plan de tratamiento
					 * (Al ser acumulativo, se debe almacenar también)
					 * (Juanda)
					 */
					else
					{
					//	guardarRegistroIgual(con, hallazgo, dtoHallazgo, info, diente, dtoPlan, ConstantesIntegridadDominio.acronimoDetalle);
					}
				}
			}
			else
			{
				// Inactivación
				if(diente.getExisteBD().getValue().equals(ConstantesBD.acronimoSi))
				{
					//Se modifica el diente a Activo NO
					//Recorre los hallazgos
					for(InfoHallazgoSuperficie hallazgo:diente.getDetalleSuperficie())
					{
						/*
						 *  Si es modificable entonces genero un nuevo registro con
						 *  los datos nuevos (Juanda)
						 */
						Log4JManager.info(hallazgo.getExisteBD().isEsModificable());
						if(hallazgo.getExisteBD().isEsModificable())
						{
							//solo inactiva los que esten guardados en base de datos
							if(hallazgo.getExisteBD().getValue().equals(ConstantesBD.acronimoSi))
							{
								boolean inactivo = false;
								DtoDetallePlanTratamiento parametros = new DtoDetallePlanTratamiento();
								parametros.setCodigo(hallazgo.getCodigoPkDetalle().doubleValue());
								parametros.setActivo(ConstantesBD.acronimoNo);
								
								//logger.info("**>>:Inactiva hallazgo >> "+parametros.getCodigo()+" "+parametros.getActivo());
								inactivo = PlanTratamiento.actualizarActivoDetallePlanTrat(con, parametros);
								if(!inactivo)
								{
									adicionarError("errors.notEspecific","No se Logro Inactivar el Detalle del Plan de Tratamiento en la Inactivación");
									return false;
								}
								
								//Se modifica los programas y servicios a activo NO
								boolean inactivarPrograma = false;
								//Recorre los programas/servicios
								for(InfoProgramaServicioPlan programas:hallazgo.getProgramasOservicios())
								{
									inactivarPrograma = false;
									
									//si es programa
									if(programas.getCodigoPkProgramaServicio().doubleValue() > 0 && 
										programas.getExisteBD().getValue().equals(ConstantesBD.acronimoSi))
											inactivarPrograma = true;
									
									for(InfoServicios servicios:programas.getListaServicios())
									{
										if((servicios.getExisteBD().getValue().equals(ConstantesBD.acronimoSi) && 
											servicios.getExisteBD().isActivo() && !inactivarPrograma) || inactivarPrograma)
										{
											DtoProgramasServiciosPlanT parametrosServ = new DtoProgramasServiciosPlanT();
											parametrosServ.setCodigoPk(servicios.getCodigoPkProgServ());
											parametrosServ.setActivo(ConstantesBD.acronimoNo);
	
											inactivo = PlanTratamiento.actualizarActivoProgServPlanTr(con, parametrosServ);//--
											if(!inactivo)
											{
												adicionarError("errors.notEspecific","No se Inactivo Programas/Servicios");
												return false;
											}
										}
									}
								}
							}
						}
						/*
						 * Si no es modificable, entonces guardo un nuevo registro con los mismos
						 * datos, para mantener el histórico de lo que se está generando
						 * en el plan de tratamiento
						 * (Al ser acumulativo, se debe almacenar también)
						 * (Juanda)
						 */
						else
						{
							//guardarRegistroIgual(con, hallazgo, dtoHallazgo, info, diente, dtoPlan, ConstantesIntegridadDominio.acronimoDetalle);
						}
					}
				}
			}
		}
		
		return relacionarSuperficies(con, info, dtoPlan, ConstantesIntegridadDominio.acronimoDetalle);
		
	}

	/**
	 * Encargado de guardar las relaciones en BD de los programas con las superficies seleccionadas
	 * @param con {@link Connection} Conexi&oacute;n con la BD
	 * @param info {@InfoOdontograma} Informaci&oacute;n del odontograma
	 * @param dtoPlan {@DtoPlanTratamientoOdo} Informaci&oacute;n del plan de tratamiento
	 * @param seccion Secci&oacute;n que se va a trabajar
	 * @since 2010-05-11
	 * @author Juan David Ram&iacute;rez
	 */
	private boolean relacionarSuperficies(Connection con,InfoOdontograma info,DtoPlanTratamientoOdo dtoPlan, String seccion)
	{
		/*
		 * Para almacenar las n superficies asociadas a un programa existen las tablas
		 * programas_hallazgo_pieza y superficies_x_programa, primero elimino todos
		 * los registros existentes en estas tablas
		 */
		PlanTratamiento.eliminarRelacionSuperficiesProgramas(con, dtoPlan.getCodigoPk().intValue(), seccion);

		ArrayList<DtoProgHallazgoPieza> listaEncabezados=new ArrayList<DtoProgHallazgoPieza>();
		
		/*
		 * Genero datos de usuario, fecha y hora, son el mismo para todos, por eso lo paso por referencia
		 */
		DtoInfoFechaUsuario infoFechaUsuario=new DtoInfoFechaUsuario();
		infoFechaUsuario.setUsuarioModifica(dtoPlan.getUsuarioModifica().getUsuarioModifica());
		infoFechaUsuario.setFechaModifica(Utilidades.capturarFechaBD());
		infoFechaUsuario.setHoraModifica(UtilidadFecha.getHoraActual());
		
		/*
		 * Se toma la sección que se va a trabajar dependiendo del parámetro "seccion"
		 */
		ArrayList<InfoDetallePlanTramiento> detalleSeccion=null;
		
		if(seccion.equals(ConstantesIntegridadDominio.acronimoDetalle))
		{
			detalleSeccion=info.getInfoPlanTrata().getSeccionHallazgosDetalle();
		}
		if(seccion.equals(ConstantesIntegridadDominio.acronimoOtro))
		{
			detalleSeccion=info.getInfoPlanTrata().getSeccionOtrosHallazgos();
		}
		
		if(detalleSeccion!=null) // Si es nulo se va por el flujo de Boca
		{
			/*
			 * Se recorre el plan de tratamiento para guardar la relación
			 * Se evalúa solamente lo que está activo, lo demas se ignora por completo
			 */
			for(InfoDetallePlanTramiento hallazgo:detalleSeccion)
			{
				// Si el diente está activo se evalúa
				if(hallazgo.getExisteBD().isActivo())
				{
					// Se recorren los hallazgos
					for(InfoHallazgoSuperficie superficieExt:hallazgo.getDetalleSuperficie())
					{
						/*
						 * Si la superficie es mayor a 2 es alguna superficie, sino se envía a la estructura inmediatamente
						 * ya que es un diente completo (Boca está validado mas arriba)
						 */
						if(superficieExt.getSuperficieOPCIONAL().getCodigo()>2)
						{
							// Si el hallazgo es activo se evalúa
							if(superficieExt.getExisteBD().isActivo())
							{
								/*
								 * Tomo el primer programa para evaluar las superficies con las que se relaciona
								 */
								for(InfoProgramaServicioPlan programaExt:superficieExt.getProgramasOservicios())
								{
									/*
									 * Si no está activo o ya fue evaluado lo paso por alto
									 */
									if(programaExt.getExisteBD().isActivo() && !programaExt.getEvaluado())
									{
										/*
										 * Si el programa aplica a mas de una superficie, busco con cuales se relaciona 
										 */
										if(programaExt.getNumeroSuperficies()>1)
										{
											/*
											 * Itero de nuevo las superficies de la pieza para verificar las relaciones
											 */
											for(InfoHallazgoSuperficie superficieInt:hallazgo.getDetalleSuperficie())
											{
												/*
												 * Si es el mismo hallazgo, busco si tiene el mismo programa
												 */
												if(superficieExt.getHallazgoREQUERIDO().getCodigo()==superficieInt.getHallazgoREQUERIDO().getCodigo())
												{
													DtoProgHallazgoPieza encabezado=new DtoProgHallazgoPieza();
													boolean existeEncabezado=false;
													for(InfoProgramaServicioPlan programaInt:superficieInt.getProgramasOservicios())
													{
														/*
														 * Si el programa interno no ha sido evaluado y está activo
														 * se verifica el asocio
														 */
														if(programaInt.getExisteBD().isActivo() && !programaInt.getEvaluado())
														{
															/*
															 * Si es el mismo programa
															 */
															if(programaExt.getCodigoPkProgramaServicio().intValue()==programaInt.getCodigoPkProgramaServicio().intValue())
															{
																/*
																 * Si tienen el mismo color de letra es porque están relacionados
																 */
																if(programaExt.getProgHallazgoPieza().getColorLetra().equals(programaInt.getProgHallazgoPieza().getColorLetra()))
																{
																	/*
																	 * Verifico si ya existe un encabezado para la pieza
																	 */
																	for(DtoProgHallazgoPieza encabezadoInt:listaEncabezados)
																	{
																		if(		encabezadoInt.getHallazgo()==superficieExt.getHallazgoREQUERIDO().getCodigo()
																			&&
																				encabezadoInt.getPrograma()==programaExt.getCodigoPkProgramaServicio().intValue()
																			&&
																				encabezadoInt.getPiezaDental()==hallazgo.getPieza().getCodigo()
																			&&
																				encabezadoInt.getColorLetra().equals(programaExt.getProgHallazgoPieza().getColorLetra())
																		)
																		{
																			encabezado=encabezadoInt;
																			existeEncabezado=true;
																			break;
																		}
																	}
																	encabezado.setHallazgo(superficieInt.getHallazgoREQUERIDO().getCodigo());
																	encabezado.setPiezaDental(hallazgo.getPieza().getCodigo());
																	encabezado.setPlanTratamiento(dtoPlan.getCodigoPk().intValue());
																	encabezado.setColorLetra(programaInt.getProgHallazgoPieza().getColorLetra());
																	encabezado.setPrograma(programaInt.getCodigoPkProgramaServicio().intValue());
																	encabezado.setInfoFechaUsuario(infoFechaUsuario);
																	encabezado.setSeccion(seccion);
																	encabezado.setCodigoPk(programaInt.getProgHallazgoPieza().getCodigoPk());
																	
																	/*
																	 * Ya encontré una relación, marco el programa como evaluado
																	 */
																	DtoSuperficiesPorPrograma superficie=new DtoSuperficiesPorPrograma();
																	
																	if(hallazgo.getCodigoPkDetalle().intValue() == ConstantesBD.codigoNuncaValido){
																		
																		superficie.setDetPlanTratamiento(superficieInt.getCodigoPkDetalle().intValue());
																	
																	}else{
																		
																		superficie.setDetPlanTratamiento(hallazgo.getCodigoPkDetalle().intValue());
																	}
																	
																	superficie.setProgHallazgoPieza(encabezado);
																	superficie.setSuperficieDental(superficieInt.getSuperficieOPCIONAL().getCodigo());
																	superficie.setInfoFechaUsuario(infoFechaUsuario);
																	encabezado.getSuperficiesPorPrograma().add(superficie);
																	programaInt.setEvaluado(true);
																	/*
																	 * Ya se terminó el recorrido de las superficies,
																	 * por lo tanto se procede a agregarlas al encabezado
																	 */
																	if(!existeEncabezado)
																	{
																		listaEncabezados.add(encabezado);
																	}
																}
															}
														}
													}
												}
											}
										}
										else // Si solamente aplica para una lo guardo en BD y no se evalúa más
										{
											/*
											 * Creo un encabezado para el almacenamiento de los datos
											 * Solamente existe un encabezado porque aplica a una superficie
											 */
											DtoProgHallazgoPieza encabezado=new DtoProgHallazgoPieza();
											encabezado.setHallazgo(superficieExt.getHallazgoREQUERIDO().getCodigo());
											encabezado.setPiezaDental(hallazgo.getPieza().getCodigo());
											encabezado.setPlanTratamiento(dtoPlan.getCodigoPk().intValue());
											encabezado.setColorLetra(programaExt.getProgHallazgoPieza().getColorLetra());
											encabezado.setPrograma(programaExt.getCodigoPkProgramaServicio().intValue());
											encabezado.setInfoFechaUsuario(infoFechaUsuario);
											encabezado.setSeccion(seccion);
											encabezado.setCodigoPk(programaExt.getProgHallazgoPieza().getCodigoPk());

		
											/*
											 * Se almacena superficie para tener relación con det_plan_tratamiento
											 */

											DtoSuperficiesPorPrograma superficie=new DtoSuperficiesPorPrograma();
											
											if(hallazgo.getCodigoPkDetalle().intValue() == ConstantesBD.codigoNuncaValido){
												
												superficie.setDetPlanTratamiento(superficieExt.getCodigoPkDetalle().intValue());
											
											}else{
												
												superficie.setDetPlanTratamiento(hallazgo.getCodigoPkDetalle().intValue());
											}
											
											superficie.setProgHallazgoPieza(encabezado);
											superficie.setSuperficieDental(superficieExt.getSuperficieOPCIONAL().getCodigo());
											superficie.setInfoFechaUsuario(infoFechaUsuario);
											encabezado.getSuperficiesPorPrograma().add(superficie);
											listaEncabezados.add(encabezado);
											
											/*
											 * Marco el programa como evaluado
											 */
											programaExt.setEvaluado(true);
											
										}
									}
								}
							}
						}
						else
						{
							/*
							 * Se recorren los programas de la pieza dental
							 */
							for(InfoProgramaServicioPlan programa:superficieExt.getProgramasOservicios())
							{
								if(programa.getExisteBD().isActivo())
								{
									/*
									 * Se crea un encabezado
									 */
									DtoProgHallazgoPieza encabezado=new DtoProgHallazgoPieza();
									encabezado.setHallazgo(superficieExt.getHallazgoREQUERIDO().getCodigo());
									encabezado.setPiezaDental(hallazgo.getPieza().getCodigo());
									encabezado.setPlanTratamiento(dtoPlan.getCodigoPk().intValue());
									encabezado.setColorLetra(programa.getProgHallazgoPieza().getColorLetra());
									encabezado.setPrograma(programa.getCodigoPkProgramaServicio().intValue());
									encabezado.setInfoFechaUsuario(infoFechaUsuario);
									encabezado.setSeccion(seccion);
									encabezado.setCodigoPk(programa.getProgHallazgoPieza().getCodigoPk());

									listaEncabezados.add(encabezado);
	
									/*
									 * Se almacena superficie para tener relación con det_plan_tratamiento
									 */
									DtoSuperficiesPorPrograma superficie=new DtoSuperficiesPorPrograma();
									superficie.setDetPlanTratamiento(hallazgo.getCodigoPkDetalle().intValue());
									superficie.setProgHallazgoPieza(encabezado);
									superficie.setSuperficieDental(ConstantesBD.codigoSuperficieDiente);
									superficie.setInfoFechaUsuario(infoFechaUsuario);
									encabezado.getSuperficiesPorPrograma().add(superficie);

									/*
									 * Marco el programa como evaluado
									 */
									programa.setEvaluado(true);
								}
							}
						}
					}
				}
			}
		}
		else // Es flujo boca, se buscan los hallazgos y se inserta un encabezado por cada programa encontrado
		{
			ArrayList<InfoHallazgoSuperficie> hallazgosBoca=info.getInfoPlanTrata().getSeccionHallazgosBoca();
			for(InfoHallazgoSuperficie hallazgo:hallazgosBoca)
			{
				if(hallazgo.getExisteBD().isActivo())
				{
					for(InfoProgramaServicioPlan programa:hallazgo.getProgramasOservicios())
					{
						if(programa.getExisteBD().isActivo())
						{
							DtoProgHallazgoPieza encabezado=new DtoProgHallazgoPieza();
							encabezado.setHallazgo(hallazgo.getHallazgoREQUERIDO().getCodigo());
							encabezado.setPiezaDental(null);
							encabezado.setPlanTratamiento(dtoPlan.getCodigoPk().intValue());
							encabezado.setColorLetra(programa.getProgHallazgoPieza().getColorLetra());
							encabezado.setPrograma(programa.getCodigoPkProgramaServicio().intValue());
							encabezado.setInfoFechaUsuario(infoFechaUsuario);
							encabezado.setSeccion(seccion);
							encabezado.setCodigoPk(programa.getProgHallazgoPieza().getCodigoPk());

							DtoSuperficiesPorPrograma superficie=new DtoSuperficiesPorPrograma();
							superficie.setDetPlanTratamiento(hallazgo.getCodigoPkDetalle().intValue());
							superficie.setProgHallazgoPieza(encabezado);
							superficie.setSuperficieDental(ConstantesBD.codigoSuperficieBoca);
							superficie.setInfoFechaUsuario(infoFechaUsuario);
							encabezado.getSuperficiesPorPrograma().add(superficie);
							programa.setEvaluado(true);

							listaEncabezados.add(encabezado);
						}
					}
				}
			}
		}
		PlanTratamiento.guardarRelacionesProgSuperficies(con, listaEncabezados);
		
		return true;
	}

	/**
	 * M&eacute;todo encargado de guardar un registro igual al anterior, asociado
	 * al nuevo plan de tratamiento, eso se hace para
	 * conservar el hist&oacute;rico de cada una de las valoraciones
	 * generadas para el paciente
	 * @param con 
	 * @param hallazgo {@link InfoHallazgoSuperficie} Informac&oacute;n del hallazgo.
	 * @param dtoHallazgo {@link DtoDetallePlanTratamiento} Dto con la informaci&oacute;n del plan de tratamiento nuevo
	 * @param infoOdontograma {@link InfoOdontograma} Informaci&oacute;n general del odontograma
	 * @param diente {@link InfoDetallePlanTramiento} Diente para el cual se va a guardar el dettalle
	 * @param dtoPlan {@link DtoPlanTratamientoOdo} Dto con la informaci&oacute;n del plan de tratamiento
	 * @param seccion {@link String} Secci&oacute;n a la que aplica
	 * @since 2010-04-28
	 * @author Juan David Ram&iacute;rez
	 */
	private void guardarRegistroIgual(Connection con, InfoHallazgoSuperficie hallazgo, DtoDetallePlanTratamiento dtoHallazgo, InfoOdontograma infoOdontograma, InfoDetallePlanTramiento diente, DtoPlanTratamientoOdo dtoPlan, String seccion)
	{
		for(InfoProgramaServicioPlan programa:hallazgo.getProgramasOservicios())
		{
			Log4JManager.info("Voy a ingresar la sección");
			DtoDetallePlanTratamiento dtoDetalle = new DtoDetallePlanTratamiento();
			
			dtoDetalle.setPlanTratamiento(dtoPlan.getCodigoPk().doubleValue());
			dtoDetalle.setPiezaDental(diente.getPieza().getCodigo());
			
			if(hallazgo.getSuperficieOPCIONAL().getCodigo() > 0)
			{
				dtoDetalle.setSuperficie(hallazgo.getSuperficieOPCIONAL().getCodigo());
			}

			dtoDetalle.setHallazgo(hallazgo.getHallazgoREQUERIDO().getCodigo());
			dtoDetalle.setSeccion(seccion);
			dtoDetalle.setClasificacion(hallazgo.getClasificacion().getValue());
			dtoDetalle.setPorConfirmar(hallazgo.getPorConfirmar());
			dtoDetalle.setConvencion(Utilidades.convertirAEntero(hallazgo.getCodigoConvencion()));
			dtoDetalle.setEspecialidad(dtoPlan.getEspecialidad());
			dtoDetalle.setActivo(hallazgo.getExisteBD().isActivo()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
			dtoDetalle.setValoracion(dtoPlan.getCodigoValoracion());
			dtoDetalle.setEvolucion(dtoPlan.getCodigoEvolucion());
			dtoDetalle.setFechaUsuarioModifica(dtoPlan.getUsuarioModifica());
			dtoDetalle.getFechaUsuarioModifica().setFechaModifica(UtilidadFecha.getFechaActual());
			dtoDetalle.getFechaUsuarioModifica().setHoraModifica(UtilidadFecha.getHoraActual());
			dtoDetalle.setCodigoCita(new BigDecimal(infoOdontograma.getCodigoCita()));
			dtoDetalle.setEvolucion(new BigDecimal(infoOdontograma.getCodigoEvolucion()));
			dtoDetalle.setValoracion(new BigDecimal(infoOdontograma.getCodigoValoracion()));
			
			dtoDetalle.setCodigo(PlanTratamiento.guardarDetPlanTratamiento(con, dtoDetalle));

			Log4JManager.info("Voy a ingresé el detalle");

			//Evalua si es por programas o servicios
			if(programa.getCodigoPkProgramaServicio().intValue() > 0)
			{
				if(programa.getExisteBD().isActivo())
				{
					//Recorre los servicios del programa
					for(InfoServicios servicio:programa.getListaServicios())
					{
						/*
						 * Inactivo el anterior e inserto uno nuevo
						 */
						Log4JManager.info("Inactivo el plan de tratamiento");
						DtoProgramasServiciosPlanT parametrosServ = new DtoProgramasServiciosPlanT();
						parametrosServ.setCodigoPk(servicio.getCodigoPkProgServ());
						parametrosServ.setActivo(ConstantesBD.acronimoNo);

						PlanTratamiento.actualizarActivoProgServPlanTr(con, parametrosServ);
						
						DtoProgramasServiciosPlanT dtoProg = new DtoProgramasServiciosPlanT();
						dtoProg.setDetPlanTratamiento(new BigDecimal(dtoDetalle.getCodigo()));
						
						Log4JManager.info("programa.getCodigoPkProgramaServicio().doubleValue() "+programa.getCodigoPkProgramaServicio().doubleValue());
						
						if(programa.getCodigoPkProgramaServicio().doubleValue() > 0)
						{
							dtoProg.getPrograma().setCodigo(programa.getCodigoPkProgramaServicio().doubleValue());
						}
						
						dtoProg.getServicio().setCodigo(servicio.getServicio().getCodigo());
						dtoProg.setEstadoPrograma(programa.getEstadoPrograma());
						dtoProg.setConvencion(dtoHallazgo.getConvencion());
						dtoProg.setEstadoServicio(servicio.getEstadoServicio());
						dtoProg.setIndicativoPrograma(ConstantesIntegridadDominio.acronimoInicial);
						dtoProg.setIndicativoServicio(ConstantesIntegridadDominio.acronimoInicial);
						dtoProg.setPorConfirmado(programa.getPorConfirmar());
						dtoProg.setEspecialidad(dtoHallazgo.getEspecialidad());
						dtoProg.getUsuarioModifica().setUsuarioModifica(dtoHallazgo.getFechaUsuarioModifica().getUsuarioModifica());
						dtoProg.getUsuarioModifica().setFechaModifica(UtilidadFecha.getFechaActual());
						dtoProg.getUsuarioModifica().setHoraModifica(UtilidadFecha.getHoraActual());
						dtoProg.setOrdenServicio(servicio.getOrderServicio());
						dtoProg.setActivo(servicio.getExisteBD().isActivo()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
						dtoProg.setValoracion(dtoHallazgo.getValoracion());
						dtoProg.setEvolucion(dtoHallazgo.getEvolucion());
						dtoProg.setCodigoCita(dtoHallazgo.getCodigoCita());


						dtoHallazgo.setCodigo(hallazgo.getCodigoPkDetalle().doubleValue());
						dtoHallazgo.setCodigoCita(new BigDecimal(infoOdontograma.getCodigoCita()));
						dtoHallazgo.setEvolucion(new BigDecimal(infoOdontograma.getCodigoEvolucion()));
						dtoHallazgo.setValoracion(new BigDecimal(infoOdontograma.getCodigoValoracion()));
						
						Log4JManager.info("Voy a ingresar el prog serv");

						dtoProg.setCodigoPk(new BigDecimal(PlanTratamiento.guardarProgramasServicio(con,dtoProg)));
						Log4JManager.info("Ingresé el prog ser");

					}
				}
			}
		}
	}

	/**
	 * Guarda la Información de la Sección Otros Hallazgos
	 * */
	public boolean guardarSeccionOtrosHallazgos(Connection con,InfoOdontograma info,DtoPlanTratamientoOdo dtoPlan,boolean aplicaInclusion)
	{
		logger.info("\n\n");
		logger.info("**>> SECCION OTROS HALLAZGOS ");
		logger.info("**>> estado Interno >> "+this.estadoInterno);
		/*logger.info("**>> Codigo Cita >> "+info.getCodigoCita());
		logger.info("**>> Codigo Cita Plan>> "+dtoPlan.getCodigoCita());
		logger.info("**>> Codigo Valoracion > "+info.getCodigoValoracion());
		logger.info("**>> Codigo Evolucion > "+info.getCodigoEvolucion());
		*/
		
		//GUARDA INFORMACION DE LA SECCION DETALLE PLAN TRATAMIENTO INICIAL
		DtoDetallePlanTratamiento dtoHallazgo =  new DtoDetallePlanTratamiento();
		DtoInfoFechaUsuario usuarioModifica = new DtoInfoFechaUsuario();
		usuarioModifica.setFechaModifica(UtilidadFecha.getFechaActual());
		usuarioModifica.setHoraModifica(UtilidadFecha.getHoraActual());
		usuarioModifica.setUsuarioModifica(info.getUsuarioActual().getUsuarioModifica());
		dtoHallazgo.setFechaUsuarioModifica(usuarioModifica);

		//Recorre los dientes
		for(InfoDetallePlanTramiento diente:info.getInfoPlanTrata().getSeccionOtrosHallazgos())
		{
			if(diente.getExisteBD().isActivo())
			{
				if(diente.getExisteBD().getValue().equals(ConstantesBD.acronimoSi))
				{
			
					boolean errorGenerado = actualizarDetPlanTratamiento(con, aplicaInclusion,
							dtoHallazgo, usuarioModifica, diente, "SeccionOtrosHallazgos");
					
					if(!errorGenerado){
						
						return errorGenerado;
					}		
				}
				//Diente no existe y esta activo
				else
				{
					//Recorre los hallazgos
					for(InfoHallazgoSuperficie hallazgo:diente.getDetalleSuperficie())
					{
						dtoHallazgo = 	llenarDtoDetallePlanTratConInfo(
											hallazgo,
											dtoPlan,
											diente.getPieza().getCodigo(),
											ConstantesIntegridadDominio.acronimoOtro);
						
						dtoHallazgo.setCodigo(PlanTratamiento.guardarDetPlanTratamiento(con, dtoHallazgo));
						
						//logger.info("\n hallazgo Superficie a guardar >> "+hallazgo.getSuperficieOPCIONAL().getNombre()+" "+hallazgo.getHallazgoREQUERIDO().getNombre()+" pieza >> "+diente.getPieza().getCodigo()+" codigppk >> "+dtoHallazgo.getCodigo());
						if(dtoHallazgo.getCodigo() <= 0)
						{
							adicionarError("errors.notEspecific","No se Logro Guardar El Detalle del Plan de Tratamiento");
							return false;
						}
							
						//******************************************************************************************************************
						//logger.info("numero de programas >> "+hallazgo.getProgramasOservicios().size());
						//Recorre los programas y servicios
						for(InfoProgramaServicioPlan programa:hallazgo.getProgramasOservicios())
						{
							//logger.info("es programa >> "+programa.getCodigoPkProgramaServicio().intValue()+" "+programa.getExisteBD().getActivo()+" "+programa.getExisteBD().getValue());
							//Evalua si es por programas o servicios
							if(programa.getCodigoPkProgramaServicio().intValue() > 0)
							{
								if(programa.getExisteBD().isActivo())
								{	
									//logger.info("numero de servicios >> "+programa.getListaServicios().size());
									//Recorre los servicios del programa
									for(InfoServicios servicios:programa.getListaServicios())
									{
										DtoProgramasServiciosPlanT dtoProg =  llenarDtoProgramasPlanTratConInfo(
																					programa.getCodigoPkProgramaServicio().doubleValue(),
																					dtoHallazgo,
																					servicios
																					,aplicaInclusion);
                                         
										// OJO 
										if(this.estadoInterno.equals("guardarOdontConexion"))
										{
											dtoProg.setPorConfirmado(ConstantesBD.acronimoNo);
										}
										dtoProg.setCodigoPk(new BigDecimal(PlanTratamiento.guardarProgramasServicio(con,dtoProg)));
										if(dtoProg.getCodigoPk().doubleValue() <= 0)
										{
											adicionarError("errors.notEspecific","No se Logro Guardar Los Programas/Servicios");
											return false;
										}
									}
								}
							}
							else
							{
								//Recorre los servicios del programa
								for(InfoServicios servicios:programa.getListaServicios())
								{
									if(servicios.getExisteBD().isActivo())
									{	
										DtoProgramasServiciosPlanT dtoProg =  llenarDtoProgramasPlanTratConInfo(
																					ConstantesBD.codigoNuncaValidoDouble,
																					dtoHallazgo,
																					servicios,
																					aplicaInclusion);
										
										//logger.info("\n guarda SERVICIOS ");
										// OJO 
										if(this.estadoInterno.equals("guardarOdontConexion"))
										{
											dtoProg.setPorConfirmado(ConstantesBD.acronimoNo);
										}
										
										dtoProg.setCodigoPk(new BigDecimal(PlanTratamiento.guardarProgramasServicio(con,dtoProg)));
										if(dtoProg.getCodigoPk().doubleValue() <= 0)
										{
											adicionarError("errors.notEspecific","No se Logro Guardar Los Programas/Servicios");
											return false;
										}
									}
								}
							}
						}
					}
				}
			}
			else
			{
				//INATIVACIÓN
				if(diente.getExisteBD().getValue().equals(ConstantesBD.acronimoSi))
				{
					//Se modifica el diente a Activo NO
					boolean inactivo = false;
					DtoDetallePlanTratamiento parametros = new DtoDetallePlanTratamiento();
					//parametros.setCodigo(diente.getCodigoPkDetalle().doubleValue());
					
					if(diente.getCodigoPkDetalle().doubleValue()<=0){
						
						parametros.setCodigo(dtoHallazgo.getCodigo());
						
					}else{
						
						parametros.setCodigo(diente.getCodigoPkDetalle().intValue());
					}
					
					
					parametros.setActivo(ConstantesBD.acronimoNo);
							
					inactivo = PlanTratamiento.actualizarActivoDetallePlanTrat(con, parametros);
					if(!inactivo)
					{
						adicionarError("errors.notEspecific","No se Logro Inactivar el Detalle del Plan de Tratamiento en la Inactivación");
						return false;
					}
					
					//Recorre los hallazgos
					for(InfoHallazgoSuperficie hallazgo:diente.getDetalleSuperficie())
					{
						//solo inactiva los que esten guardados en base de datos
						if(hallazgo.getExisteBD().getValue().equals(ConstantesBD.acronimoSi))
						{
							//Se modifica los programas y servicios a activo NO
							boolean inactivarPrograma = false;
							//Recorre los programas/servicios
							for(InfoProgramaServicioPlan programas:hallazgo.getProgramasOservicios())
							{
								inactivarPrograma = false;
								
								//si es programa
								if(programas.getCodigoPkProgramaServicio().doubleValue() > 0 && 
									programas.getExisteBD().getValue().equals(ConstantesBD.acronimoSi))
										inactivarPrograma = true;
								
								for(InfoServicios servicios:programas.getListaServicios())
								{
									if((servicios.getExisteBD().getValue().equals(ConstantesBD.acronimoSi) && 
										servicios.getExisteBD().isActivo() && !inactivarPrograma) || inactivarPrograma)
									{
										DtoProgramasServiciosPlanT parametrosServ = new DtoProgramasServiciosPlanT();
										parametrosServ.setCodigoPk(servicios.getCodigoPkProgServ());
										parametrosServ.setActivo(ConstantesBD.acronimoNo);						
										

										inactivo = PlanTratamiento.actualizarActivoProgServPlanTr(con, parametrosServ);
										if(!inactivo)
										{
											adicionarError("errors.notEspecific","No se Inactivo Programas/Servicios");
											return false;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		
		return relacionarSuperficies(con, info, dtoPlan, ConstantesIntegridadDominio.acronimoOtro);
	}

	/**
	 * 
	 * Método que se encarga de actualizar la información asociada a un Hallazgo 
	 * ya registrado.
	 * 
	 * @param con
	 * @param aplicaInclusion
	 * @param dtoHallazgo
	 * @param usuarioModifica
	 * @param diente
	 * @param seccion 
	 * @return
	 */
	private boolean actualizarDetPlanTratamiento(Connection con, boolean aplicaInclusion, DtoDetallePlanTratamiento dtoHallazgo, DtoInfoFechaUsuario usuarioModifica, InfoDetallePlanTramiento diente, String seccion) {
		
		//Actualiza el diente si se cambio
		
		if(diente.getPieza().getCodigo() != diente.getPiezaOld().getCodigo())
		{
			DtoDetallePlanTratamiento cambios = new DtoDetallePlanTratamiento();
			
			cambios.setPiezaDental(diente.getPieza().getCodigo());
	
			if(diente.getCodigoPkDetalle().doubleValue()<=0){
				
				cambios.setCodigo(dtoHallazgo.getCodigo());
				
			}else{
				
				cambios.setCodigo(diente.getCodigoPkDetalle().doubleValue());
			}
			
			cambios.setFechaUsuarioModifica(usuarioModifica);
	
			cambios.setClasificacion(dtoHallazgo.getClasificacion());

			if(!PlanTratamiento.actualizarDetPlanTratamiento(con, cambios))
			{
				adicionarError("errors.notEspecific","No se Logro Actualizar el Detalle del Plan de Tratamiento");
				return false;
			}
		}
		
		if(seccion.equals("SeccionOtrosHallazgos")){
			
			//Recorre los hallazgos y actualiza si hay cambios
			for(InfoHallazgoSuperficie hallazgo:diente.getDetalleSuperficie())
			{
				if(hallazgo.getHallazgoREQUERIDO().getCodigo() != hallazgo.getHallazgoREQUERIDOOld().getCodigo())
				{
					DtoDetallePlanTratamiento cambios = new DtoDetallePlanTratamiento();
					cambios.setHallazgo(hallazgo.getHallazgoREQUERIDO().getCodigo());
					
					if(diente.getCodigoPkDetalle().doubleValue()<=0){
						
						cambios.setCodigo(dtoHallazgo.getCodigo());
						
					}else{
						
						cambios.setCodigo(diente.getCodigoPkDetalle().intValue());
					}
					
					cambios.setFechaUsuarioModifica(usuarioModifica);
					
					if(!PlanTratamiento.actualizarDetPlanTratamiento(con, cambios))//--
					{
						adicionarError("errors.notEspecific","No se Logro Actualizar el Detalle del Plan de Tratamiento");
						return false;
					}
					
					//Inactiva los programas/servicios asociados al hallazgo anterior
					DtoProgramasServiciosPlanT parametrosServ = new DtoProgramasServiciosPlanT();
					//parametrosServ.setDetPlanTratamiento(diente.getCodigoPkDetalle());
					
					if(diente.getCodigoPkDetalle().doubleValue()<=0){
						
						parametrosServ.setDetPlanTratamiento(new BigDecimal(dtoHallazgo.getCodigo()));
						
					}else{
						
						parametrosServ.setDetPlanTratamiento(diente.getCodigoPkDetalle());
					}
					
					parametrosServ.setActivo(ConstantesBD.acronimoNo);
					
					if(!PlanTratamiento.actualizarActivoProgServPlanTr(con, parametrosServ))
					{
						adicionarError("errors.notEspecific","No se Inactivo Programas/Servicios");
						return false;
					}
				}
				
				if(hallazgo.getSuperficieOPCIONAL().getCodigo() != hallazgo.getSuperficieOPCIONALOld().getCodigo())
				{
					DtoDetallePlanTratamiento cambios = new DtoDetallePlanTratamiento();
					cambios.setSuperficie(hallazgo.getSuperficieOPCIONAL().getCodigo());
					
					//cambios.setCodigo(diente.getCodigoPkDetalle().doubleValue());
					
					if(diente.getCodigoPkDetalle().doubleValue()<=0){
						
						cambios.setCodigo(dtoHallazgo.getCodigo());
						
					}else{
						
						cambios.setCodigo(diente.getCodigoPkDetalle().intValue());
					}
					
					cambios.setFechaUsuarioModifica(usuarioModifica);
					
					if(!PlanTratamiento.actualizarDetPlanTratamiento(con, cambios))
					{
						adicionarError("errors.notEspecific","No se Logro Actualizar el Detalle del Plan de Tratamiento");
						return false;
					}
				}

				
				if(diente.getCodigoPkDetalle().doubleValue()>0){
					
					//Actualiza el codigo Pk del Detalle del plan de tratamiento
					dtoHallazgo.setCodigo(diente.getCodigoPkDetalle().doubleValue());
				}
				
				//Recorre los programas y servicios
				for(InfoProgramaServicioPlan programa:hallazgo.getProgramasOservicios())
				{
					//logger.info("es programa >> "+programa.getCodigoPkProgramaServicio().intValue()+" "+programa.getExisteBD().getActivo()+" "+programa.getExisteBD().getValue());
					//Evalua si es por programas o servicios
					if(programa.getCodigoPkProgramaServicio().intValue() > 0)
					{
						if(programa.getExisteBD().isActivo())
						{
							if(!programa.getExisteBD().getValue().equals(ConstantesBD.acronimoSi))
							{
								//logger.info("numero de servicios >> "+programa.getListaServicios().size());
								//Recorre los servicios del programa
								for(InfoServicios servicios:programa.getListaServicios())
								{
									DtoProgramasServiciosPlanT dtoProg =  llenarDtoProgramasPlanTratConInfo(
																				programa.getCodigoPkProgramaServicio().doubleValue(),
																				dtoHallazgo,
																				servicios,
																				aplicaInclusion);
			                        // OJO >>
									if(this.estadoInterno.equals("guardarOdontConexion"))
									{
										dtoProg.setPorConfirmado(ConstantesBD.acronimoNo);
									}
										
									
									dtoProg.setCodigoPk(new BigDecimal(PlanTratamiento.guardarProgramasServicio(con,dtoProg)));
									if(dtoProg.getCodigoPk().doubleValue() <= 0)
									{
										adicionarError("errors.notEspecific","No se Logro Guardar Los Programas/Servicios");
										return false;
									}
								}
							}
						}
						else
						{
							if(programa.getExisteBD().getValue().equals(ConstantesBD.acronimoSi))
							{
								for(InfoServicios servicios:programa.getListaServicios())
								{
									DtoProgramasServiciosPlanT parametrosServ = new DtoProgramasServiciosPlanT();
									parametrosServ.setCodigoPk(servicios.getCodigoPkProgServ());
									parametrosServ.setActivo(ConstantesBD.acronimoNo);
									
									if(!PlanTratamiento.actualizarActivoProgServPlanTr(con, parametrosServ))
									{
										adicionarError("errors.notEspecific","No se Inactivo Programas/Servicios");
										return false;
									}
								}
							}
						}
					}
					else
					{
						//Recorre los servicios del programa
						for(InfoServicios servicios:programa.getListaServicios())
						{
							if(servicios.getExisteBD().isActivo())
							{
								if(!servicios.getExisteBD().getValue().equals(ConstantesBD.acronimoSi))
								{
									DtoProgramasServiciosPlanT dtoProg =  llenarDtoProgramasPlanTratConInfo(
																				ConstantesBD.codigoNuncaValidoDouble,
																				dtoHallazgo,
																				servicios,
																				aplicaInclusion);
									
									//logger.info("\n guarda SERVICIOS ");
									  // OJO >>
									if(this.estadoInterno.equals("guardarOdontConexion"))
									{
										dtoProg.setPorConfirmado(ConstantesBD.acronimoNo);
									}
									dtoProg.setCodigoPk(new BigDecimal(PlanTratamiento.guardarProgramasServicio(con,dtoProg)));
									if(dtoProg.getCodigoPk().doubleValue() <= 0)
									{
										adicionarError("errors.notEspecific","No se Logro Guardar Los Programas/Servicios");
										return false;
									}
								}
							}
							else
							{
								if(servicios.getExisteBD().getValue().equals(ConstantesBD.acronimoSi))
								{
									DtoProgramasServiciosPlanT parametrosServ = new DtoProgramasServiciosPlanT();
									parametrosServ.setCodigoPk(servicios.getCodigoPkProgServ());
									parametrosServ.setActivo(ConstantesBD.acronimoNo);
									
									if(!PlanTratamiento.actualizarActivoProgServPlanTr(con, parametrosServ))
									{
										adicionarError("errors.notEspecific","No se Inactivo Programas/Servicios");
										return false;
									}
								}
							}
						}
					}
				}
			}
		}
		
		
		return true;
	}
	
	
	/**
	 * Guarda la Información de la Sección Hallazgos Boca
	 * */
	public boolean guardarSeccionHallazgosBoca(Connection con,InfoOdontograma info,DtoPlanTratamientoOdo dtoPlan,boolean aplicaInclusion)
	{
		logger.info("\n\n");
		logger.info("**>> SECCION HALLAZGOS BOCA ");
		//GUARDA INFORMACION DE LA SECCION DETALLE PLAN TRATAMIENTO INICIAL
		DtoDetallePlanTratamiento dtoHallazgo =  new DtoDetallePlanTratamiento();
		DtoInfoFechaUsuario usuarioModifica = new DtoInfoFechaUsuario();
		usuarioModifica.setFechaModifica(UtilidadFecha.getFechaActual());
		usuarioModifica.setHoraModifica(UtilidadFecha.getHoraActual());
		usuarioModifica.setUsuarioModifica(info.getUsuarioActual().getUsuarioModifica());
		dtoHallazgo.setFechaUsuarioModifica(usuarioModifica);

		//Recorre los dientes
		for(InfoHallazgoSuperficie hallazgo:info.getInfoPlanTrata().getSeccionHallazgosBoca())
		{
			if(hallazgo.getExisteBD().isActivo())
			{
				if(hallazgo.getExisteBD().getValue().equals(ConstantesBD.acronimoSi))
				{
					if(hallazgo.getHallazgoREQUERIDO().getCodigo() != hallazgo.getHallazgoREQUERIDOOld().getCodigo())
					{
						DtoDetallePlanTratamiento cambios = new DtoDetallePlanTratamiento();
						cambios.setHallazgo(hallazgo.getHallazgoREQUERIDO().getCodigo());
						cambios.setCodigo(hallazgo.getCodigoPkDetalle().doubleValue());
						cambios.setFechaUsuarioModifica(usuarioModifica);
						
						if(!PlanTratamiento.actualizarDetPlanTratamiento(con, cambios))
						{
							adicionarError("errors.notEspecific","No se Logro Actualizar el Detalle del Plan de Tratamiento");
							return false;
						}
						
						//Inactiva los programas/servicios asociados al hallazgo anterior
						DtoProgramasServiciosPlanT parametrosServ = new DtoProgramasServiciosPlanT();
						parametrosServ.setDetPlanTratamiento(hallazgo.getCodigoPkDetalle());
						parametrosServ.setActivo(ConstantesBD.acronimoNo);
						
						if(!PlanTratamiento.actualizarActivoProgServPlanTr(con, parametrosServ))
						{
							adicionarError("errors.notEspecific","No se Inactivo Programas/Servicios");
							return false;
						}
					}
					
					//Actualiza el codigo Pk del Detalle del plan de tratamiento
					dtoHallazgo.setCodigo(hallazgo.getCodigoPkDetalle().doubleValue());

					//Recorre los programas y servicios
					for(InfoProgramaServicioPlan programa:hallazgo.getProgramasOservicios())
					{
						//logger.info("es programa >> "+programa.getCodigoPkProgramaServicio().intValue()+" "+programa.getExisteBD().getActivo()+" "+programa.getExisteBD().getValue());
						//Evalua si es por programa o servicios
						if(programa.getCodigoPkProgramaServicio().intValue() > 0)
						{
							if(programa.getExisteBD().isActivo())
							{
								if(!programa.getExisteBD().getValue().equals(ConstantesBD.acronimoSi))
								{
									//logger.info("numero de servicios >> "+programa.getListaServicios().size());
									//Recorre los servicios del programa
									for(InfoServicios servicios:programa.getListaServicios())
									{
										DtoProgramasServiciosPlanT dtoProg =  llenarDtoProgramasPlanTratConInfo(
																					programa.getCodigoPkProgramaServicio().doubleValue(),
																					dtoHallazgo,
																					servicios, aplicaInclusion);
										if(this.estadoInterno.equals("guardarOdontConexion"))// Mayo 25 2010 Aplica cuando Se esta evolucionando seccion Nuevos Hallazgos
										{
											dtoProg.setPorConfirmado(ConstantesBD.acronimoNo);
										}

										dtoProg.setCodigoPk(new BigDecimal(PlanTratamiento.guardarProgramasServicio(con,dtoProg)));
										if(dtoProg.getCodigoPk().doubleValue() <= 0)
										{
											adicionarError("errors.notEspecific","No se Logro Guardar Los Programas/Servicios");
											return false;
										}
									}
								}
							}
							else
							{
								if(programa.getExisteBD().getValue().equals(ConstantesBD.acronimoSi))
								{
									for(InfoServicios servicios:programa.getListaServicios())
									{
										DtoProgramasServiciosPlanT parametrosServ = new DtoProgramasServiciosPlanT();
										parametrosServ.setCodigoPk(servicios.getCodigoPkProgServ());
										parametrosServ.setActivo(ConstantesBD.acronimoNo);
										
										if(!PlanTratamiento.actualizarActivoProgServPlanTr(con, parametrosServ))
										{
											adicionarError("errors.notEspecific","No se Inactivo Programas/Servicios");
											return false;
										}
									}
								}
							}
						}
						else
						{
							//Recorre los servicios del programa
							for(InfoServicios servicios:programa.getListaServicios())
							{
								if(servicios.getExisteBD().isActivo())
								{
									if(!servicios.getExisteBD().getValue().equals(ConstantesBD.acronimoSi))
									{
										DtoProgramasServiciosPlanT dtoProg =  llenarDtoProgramasPlanTratConInfo(
																					ConstantesBD.codigoNuncaValidoDouble,
																					dtoHallazgo,
																					servicios, aplicaInclusion);
										
										if(this.estadoInterno.equals("guardarOdontConexion")) // Mayo 25 2010 Aplica cuando Se esta evolucionando seccion Nuevos Hallazgos
										{
											dtoProg.setPorConfirmado(ConstantesBD.acronimoNo);
										}
										//logger.info("\n guarda SERVICIOS ");
										dtoProg.setCodigoPk(new BigDecimal(PlanTratamiento.guardarProgramasServicio(con,dtoProg)));
										if(dtoProg.getCodigoPk().doubleValue() <= 0)
										{
											adicionarError("errors.notEspecific","No se Logro Guardar Los Programas/Servicios");
											return false;
										}
									}
								}
								else
								{
									if(servicios.getExisteBD().getValue().equals(ConstantesBD.acronimoSi))
									{
										DtoProgramasServiciosPlanT parametrosServ = new DtoProgramasServiciosPlanT();
										parametrosServ.setCodigoPk(servicios.getCodigoPkProgServ());
										parametrosServ.setActivo(ConstantesBD.acronimoNo);
										
										if(!PlanTratamiento.actualizarActivoProgServPlanTr(con, parametrosServ))
										{
											adicionarError("errors.notEspecific","No se Inactivo Programas/Servicios");
											return false;
										}
									}
								}
							}
						}
					}
				}
				//Diente no existe y esta activo
				else
				{
					dtoHallazgo = 	llenarDtoDetallePlanTratConInfo(
										hallazgo,
										dtoPlan,
										ConstantesBD.codigoNuncaValido,
										ConstantesIntegridadDominio.acronimoBoca);
						
					dtoHallazgo.setCodigo(PlanTratamiento.guardarDetPlanTratamiento(con, dtoHallazgo));
						
					//logger.info("\n hallazgo Superficie a guardar >> "+hallazgo.getSuperficieOPCIONAL().getNombre()+" "+hallazgo.getHallazgoREQUERIDO().getNombre()+" codigppk >> "+dtoHallazgo.getCodigo());
					if(dtoHallazgo.getCodigo() <= 0)
					{
						adicionarError("errors.notEspecific","No se Logro Guardar El Detalle del Plan de Tratamiento");
						return false;
					}
							
					//******************************************************************************************************************
					//logger.info("numero de programas >> "+hallazgo.getProgramasOservicios().size());
					//Recorre los programas y servicios
					for(InfoProgramaServicioPlan programa:hallazgo.getProgramasOservicios())
					{
						//logger.info("es programa >> "+programa.getCodigoPkProgramaServicio().intValue()+" "+programa.getExisteBD().getActivo()+" "+programa.getExisteBD().getValue());
						//Evalua si es por programas o servicios
						if(programa.getCodigoPkProgramaServicio().intValue() > 0)
						{
							if(programa.getExisteBD().isActivo())
							{	
								//logger.info("numero de servicios >> "+programa.getListaServicios().size());
								//Recorre los servicios del programa
								for(InfoServicios servicios:programa.getListaServicios())
								{
									DtoProgramasServiciosPlanT dtoProg =  llenarDtoProgramasPlanTratConInfo(
																				programa.getCodigoPkProgramaServicio().doubleValue(),
																				dtoHallazgo,
																				servicios, aplicaInclusion);
									if(this.estadoInterno.equals("guardarOdontConexion"))// Mayo 25 2010 Aplica cuando Se esta evolucionando seccion Nuevos Hallazgos
									{
										dtoProg.setPorConfirmado(ConstantesBD.acronimoNo);
									} 
									
									dtoProg.setCodigoPk(new BigDecimal(PlanTratamiento.guardarProgramasServicio(con,dtoProg)));
									if(dtoProg.getCodigoPk().doubleValue() <= 0)
									{
										adicionarError("errors.notEspecific","No se Logro Guardar Los Programas/Servicios");
										return false;
									}
								}
							}
						}
						else
						{
							//Recorre los servicios del programa
							for(InfoServicios servicios:programa.getListaServicios())
							{
								if(servicios.getExisteBD().isActivo())
								{	
									DtoProgramasServiciosPlanT dtoProg =  llenarDtoProgramasPlanTratConInfo(
																				ConstantesBD.codigoNuncaValidoDouble,
																				dtoHallazgo,
																				servicios,
																				aplicaInclusion);
									
									if(this.estadoInterno.equals("guardarOdontConexion"))// Mayo 25 2010 Aplica cuando Se esta evolucionando seccion Nuevos Hallazgos
									{
										dtoProg.setPorConfirmado(ConstantesBD.acronimoNo);
									}
									//logger.info("\n guarda SERVICIOS ");
									dtoProg.setCodigoPk(new BigDecimal(PlanTratamiento.guardarProgramasServicio(con,dtoProg)));
									if(dtoProg.getCodigoPk().doubleValue() <= 0)
									{
										adicionarError("errors.notEspecific","No se Logro Guardar Los Programas/Servicios");
										return false;
									}
								}
							}
						}
					}
				}
			}
			else
			{
				//INACTIVACIÓN
				//solo inactiva los que esten guardados en base de datos
				if(hallazgo.getExisteBD().getValue().equals(ConstantesBD.acronimoSi))
				{
					boolean inactivo = false;
					DtoDetallePlanTratamiento parametros = new DtoDetallePlanTratamiento();
					parametros.setCodigo(hallazgo.getCodigoPkDetalle().doubleValue());
					parametros.setActivo(ConstantesBD.acronimoNo);
					
					inactivo = PlanTratamiento.actualizarActivoDetallePlanTrat(con, parametros);
					if(!inactivo)
					{
						adicionarError("errors.notEspecific","No se Logro Inactivar el Detalle del Plan de Tratamiento en la Inactivación");
						return false;
					}
					
					//Se modifica los programas y servicios a activo NO
					boolean inactivarPrograma = false;
					//Recorre los programas/servicios
					for(InfoProgramaServicioPlan programas:hallazgo.getProgramasOservicios())
					{
						inactivarPrograma = false;
						
						//si es programa
						if(programas.getCodigoPkProgramaServicio().doubleValue() > 0 && 
							programas.getExisteBD().getValue().equals(ConstantesBD.acronimoSi))
								inactivarPrograma = true;
						
						for(InfoServicios servicios:programas.getListaServicios())
						{
							if((servicios.getExisteBD().getValue().equals(ConstantesBD.acronimoSi) && 
								servicios.getExisteBD().isActivo() && !inactivarPrograma) || inactivarPrograma)
							{
								DtoProgramasServiciosPlanT parametrosServ = new DtoProgramasServiciosPlanT();
								parametrosServ.setCodigoPk(servicios.getCodigoPkProgServ());
								parametrosServ.setActivo(ConstantesBD.acronimoNo);

								inactivo = PlanTratamiento.actualizarActivoProgServPlanTr(con, parametrosServ);
								if(!inactivo)
								{
									adicionarError("errors.notEspecific","No se Inactivo Programas/Servicios");
									return false;
								}
							}
						}
					}
				}
			}
		}
		
		return relacionarSuperficies(con, info, dtoPlan, ConstantesIntegridadDominio.acronimoBoca);
	}


	/**
	 * Inicializa los atributos de Edad Final Niñes
	 * y Edad Inicial Adulto
	 * */
	public InfoDatosString iniciarValidacionEdades(int institucion, int edadPaciente)
	{
		InfoDatosString info = new InfoDatosString();
		int eia = Utilidades.convertirAEntero(ValoresPorDefecto.getEdadInicioAdulto(institucion).toString());
		int ein = Utilidades.convertirAEntero(ValoresPorDefecto.getEdadFinalNinez(institucion).toString());
		
		info.setDescripcion(ConstantesBD.acronimoNo);
		info.setId(ConstantesBD.acronimoNo);
		
		if(edadPaciente <= ein)
			//activoDienteNino 
			info.setDescripcion(ConstantesBD.acronimoSi);
		else if(edadPaciente >= eia)
			//activoDienteAdulto
			info.setId(ConstantesBD.acronimoSi);
		else
		{
			info.setId(ConstantesBD.acronimoSi);
			info.setDescripcion(ConstantesBD.acronimoSi);
		}
		
		return info;
	}
	
	
	/**
	 * Adiciona un nuevo hallazgo boca
	 * @param  InfoOdontograma info 
	 * */
	public InfoOdontograma accionNuevoHallazgoBoca(InfoOdontograma info)
	{
		InfoHallazgoSuperficie nuevo = new InfoHallazgoSuperficie();
		nuevo.getExisteBD().setEstaBD(false);
		nuevo.getExisteBD().setActivo(true);
		
		info.getInfoPlanTrata().getSeccionHallazgosBoca().add(nuevo);
		return info;
	}
	
	/**
	 * Adiciona una nuevo otro hallazgo 
	 * @param  InfoOdontograma info
	 * */
	public InfoOdontograma accionNuevoOtroHallazgo(InfoOdontograma info)
	{
		InfoDetallePlanTramiento nuevo = new InfoDetallePlanTramiento();
		nuevo.getExisteBD().setEstaBD(false);
		nuevo.getExisteBD().setActivo(true);
		
		InfoHallazgoSuperficie nuevoHallazgo = new InfoHallazgoSuperficie();
		nuevo.getDetalleSuperficie().add(nuevoHallazgo);
		
		info.getInfoPlanTrata().getSeccionOtrosHallazgos().add(nuevo);
		return info;
	}
	
	/**
	 * adiciona un hallazgo seleccionado
	 * @param InfoOdontograma info
	 * */
	public InfoOdontograma accionAddHallazgoOtro(InfoOdontograma info)
	{
		// info.getIndicador2() >> tipo de hallazgo (boca o superficie)  ( 1. superficie   ----   2. boca)
		if(info.getIndicador1() >= 0 && 
				info.getIndicador2() >= 0 && 
					info.getIndicador3() >= 0)
		{
			InfoDetallePlanTramiento diente =  info.getInfoPlanTrata().getSeccionOtrosHallazgos().get(info.getIndicador1());
			
			InfoHallazgoSuperficie hallazgo = new InfoHallazgoSuperficie();
			hallazgo.getHallazgoREQUERIDO().setCodigo(info.getIndicador3());
			hallazgo.getHallazgoREQUERIDO().setNombre(info.obtenerNombreHallazgo(info.getIndicador2(),info.getIndicador3()));
			
			hallazgo.getHallazgoREQUERIDO().setCodigo2(info.getIndicador2());
			hallazgo.getInfoRegistroHallazgo().setFechaModifica(UtilidadFecha.getFechaActual());
			hallazgo.getInfoRegistroHallazgo().setHoraModifica(UtilidadFecha.getHoraActual());
			
			if(info.getIndicador2()==1)//si es superficie, se deben cargar las superficies que aplican para el diente.
			{
				diente.setArraySuperficiesDiente(cargarSuperficiesDiente(info.getArraySuperficies(),diente.getPieza().getCodigo()));
			}
			diente.getDetalleSuperficie().set(0,hallazgo);
			
			/*
			 * Se habla con Gladys para que no cargue programas por defecto en esta sección
			 * ya que no se tiene seleccionada la superficie y se presta para problemas
			 * cargarProgramasVariasSuperficies(info, ConstantesIntegridadDominio.acronimoOtro);
			 */
			
		}
		else
		{
			Log4JManager.error("No se enviaron los datos suficientes para consultar programas");
		}
		
		return info;
	}
	
	private ArrayList<DtoSectorSuperficieCuadrante> cargarSuperficiesDiente(ArrayList<DtoSectorSuperficieCuadrante> arraySuperficies, int codigoPieza) 
	{
		ArrayList<DtoSectorSuperficieCuadrante> superficies=new ArrayList<DtoSectorSuperficieCuadrante>();
		
		for(DtoSectorSuperficieCuadrante superficie:arraySuperficies)
		{
			if(superficie.getPieza()==codigoPieza)
			{
				superficies.add(superficie);
			}
		}
		
		return superficies;
	}

	/**
	* Inactiva un registro de la seccion otros hallazgos 
	 * */
	public InfoOdontograma accionEliminarHallazgoOtro(InfoOdontograma info)
	{
		try
		{
			InfoDetallePlanTramiento diente =  info.getInfoPlanTrata().getSeccionOtrosHallazgos().get(info.getIndicador1());
			diente.getExisteBD().setActivo(false);
		}
		catch(Exception e)
		{
			logger.info("SE ELIMINO UN REG QUE NO ERA DE LA BD.");
		}
		return info;
	}
	
	/**
	 * adiciona un hallazgo seleccionado a la seccion de boca
	 * @param InfoOdontograma info
	 * */
	public InfoOdontograma accionAddHallazgoBoca(InfoOdontograma info)
	{
		if(info.getIndicador1() >= 0 && 
				info.getIndicador2() >= 0 && 
					info.getIndicador3() >= 0)
		{
			InfoHallazgoSuperficie hallazgo = new InfoHallazgoSuperficie();
			hallazgo.getHallazgoREQUERIDO().setCodigo(info.getIndicador3());
			hallazgo.getHallazgoREQUERIDO().setNombre(info.obtenerNombreHallazgo(info.getIndicador2(),info.getIndicador3()));
			
			hallazgo.getHallazgoREQUERIDO().setCodigo2(info.getIndicador2());
			hallazgo.getInfoRegistroHallazgo().setFechaModifica(UtilidadFecha.getFechaActual());
			hallazgo.getInfoRegistroHallazgo().setHoraModifica(UtilidadFecha.getHoraActual());
			
			//Carga el Programa o Servicios parametrizados por defecto para el Hallazgo
			DtoProgramasServiciosPlanT parametros = new DtoProgramasServiciosPlanT();
			parametros.setCodigoHallazgo(info.getIndicador3());
			parametros.setBuscarProgramas(info.getEsBuscarPorPrograma());
			InfoProgramaServicioPlan progsev = PlanTratamiento.obtenerProgramaServicioParamHallazgo(parametros);
			
			if(progsev.getCodigoPkProgramaServicio().intValue()>0)
			{
				DtoProgHallazgoPieza dtoProgHallazgoPieza=new DtoProgHallazgoPieza();
				dtoProgHallazgoPieza.setColorLetra(ColoresPlanTratamiento.NEGRO.getColor());
				progsev.setProgHallazgoPieza(dtoProgHallazgoPieza);
				hallazgo.getProgramasOservicios().add(progsev);
			}
			
			//pasa la información del codigoPk Si existe
			hallazgo.setCodigoPkDetalle(info.getInfoPlanTrata().getSeccionHallazgosBoca().get(info.getIndicador1()).getCodigoPkDetalle());
			hallazgo.setExisteBD(info.getInfoPlanTrata().getSeccionHallazgosBoca().get(info.getIndicador1()).getExisteBD());
			
			info.getInfoPlanTrata().getSeccionHallazgosBoca().set(info.getIndicador1(),hallazgo);
		}
		else
		{
			
		}
		return info;
	}
	
	/**
	* Inactiva un registro de la seccion hallazgos boca 
	 * */
	public InfoOdontograma accionEliminarHallazgoBoca(InfoOdontograma info)
	{
		InfoHallazgoSuperficie hallazgo = info.getInfoPlanTrata().getSeccionHallazgosBoca().get(info.getIndicador1());
		hallazgo.getExisteBD().setActivo(false);
		return info;
	}
	
	/**
	 * Llena la información de un dto especifico con la informacion de la Vista
	 * @param InfoOdontograma info
	 * */
	public DtoPlanTratamientoOdo llenarDtoPlanTrataConInfo(InfoOdontograma info,DtoOdontograma dtoOdo)
	{
		DtoPlanTratamientoOdo dtoPlan = new DtoPlanTratamientoOdo();
		dtoPlan.setCodigoPaciente(info.getCodigoPaciente());
		dtoPlan.setIngreso(info.getIdIngresoPaciente());
		dtoPlan.getEspecialidad().setCodigo(info.getEspecialidad());
		dtoPlan.setFechaGrabacion(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		dtoPlan.setHoraGrabacion(UtilidadFecha.getHoraActual());
		dtoPlan.setUsuarioGrabacion(info.getUsuarioActual().getUsuarioModifica());
		dtoPlan.setEstado(info.getInfoPlanTrata().getEstado());
		
		if(dtoOdo.getEvolucion() > 0)
		{
			dtoPlan.setOdontogramaEvolucion(new BigDecimal(dtoOdo.getCodigoPk()));
		}
		else
		{
			dtoPlan.setOdontogramaDiagnostico(new BigDecimal(dtoOdo.getCodigoPk()));
		}
		
		dtoPlan.setCodigoEvolucion(new BigDecimal(info.getCodigoEvolucion()));
		dtoPlan.setCodigoValoracion(new BigDecimal(info.getCodigoValoracion()));
		
		dtoPlan.setCodigoCita(new BigDecimal(info.getCodigoCita()));		
		dtoPlan.setIndicativo(info.getIndicadorPlantTratamiento());
		dtoPlan.setPorConfirmar(info.getInfoPlanTrata().getPorConfirmar());
		dtoPlan.setPorConfirmar(info.getPorConfirmar());
		dtoPlan.setInstitucion(info.getInstitucion());
		dtoPlan.setCentroAtencion(info.getCodigoCentroAtencion());
		
		//para el caso en que se guarda por primera vez
		dtoPlan.getUsuarioModifica().setUsuarioModifica(info.getUsuarioActual().getUsuarioModifica());
		dtoPlan.getUsuarioModifica().setFechaModifica(UtilidadFecha.getFechaActual());
		dtoPlan.getUsuarioModifica().setHoraModifica(dtoPlan.getHoraGrabacion());
		return dtoPlan;
	}
	
	/**
	 * Llena la información de un dto especifico con la información de la Vista
	 * @param InfoOdontograma info 
	 * */
	public DtoOdontograma llenarDtoOdontogramaConInfo(InfoOdontograma info)
	{
		DtoOdontograma dto = new DtoOdontograma();
		dto.setCodigoPaciente(info.getCodigoPaciente());
		dto.getIngreso().setCodigo(info.getIdIngresoPaciente());
		dto.setValoracion(info.getCodigoValoracion());
		dto.setIndicativo(info.getIndicadorOdontograma());
		dto.setEvolucion(Utilidades.convertirADouble(info.getCodigoEvolucion()+""));
		dto.setInstitucion(info.getInstitucion());
		dto.getCentroAtencion().setCodigo(info.getCodigoCentroAtencion());
		dto.getUsuarioModifica().setUsuarioModifica(info.getUsuarioActual().getUsuarioModifica());
		dto.getUsuarioModifica().setFechaModifica(UtilidadFecha.getFechaActual());
		dto.getUsuarioModifica().setHoraModifica(UtilidadFecha.getHoraActual());
		
		//En el sqlBase se inserta el codigoPk del Odontograma
		
		logger.info("La imagen que se va a guardar en BD "+info.getInfoPlanTrata().getImagen());
		
		/*
		 * verificar que la imágen existe físicamente
		 */
		
		// TODO HAY QUE SABER SI ES DE EVOLUCION O VALORACION
		
		
		//VARIABLES TMP PARA GUARDAR LA RUTA 
		String tmpPathDiagnostico = CarpetasArchivos.IMAGENES_ODONTODX.getRutaFisica()+System.getProperty("file.separator");
		
		
		//VALIDACION SI EXISTE NOMBRE IMAGEN
		if(!UtilidadTexto.isEmpty(info.getInfoPlanTrata().getImagen()))
		{
			if(!UtilidadFileUpload.existeArchivo (tmpPathDiagnostico, info.getInfoPlanTrata().getImagen()))
			{
				cargaImagenHistoricos(info);
			}
		}
		else
		{
			cargaImagenHistoricos(info);
		}
		
		logger.info("La imagen que se va a guardar en BD "+info.getInfoPlanTrata().getImagen());
		dto.setImagen(info.getInfoPlanTrata().getImagen());
		
		return dto;
	}

	
	/**
	 * CARGAR LA IMAGEN DE LOS HISTORICOS DEL PLAN DE TRATAMIENTO
	 * @param info
	 */
	private void cargaImagenHistoricos(InfoOdontograma info) {
		DtoPlanTratamientoOdo dtoTmp = new DtoPlanTratamientoOdo();
		dtoTmp.setCodigoPk(info.getInfoPlanTrata().getCodigoPk());
		dtoTmp.setIngreso(info.getIdIngresoPaciente());
		info.getInfoPlanTrata().setImagen( Odontograma.cargarOdontogramaImagen(dtoTmp).getImagen());
	}

	
	/**
	 * METODO QUE CARGA EL NOMBRE DE LA IMAGEN DEL ODONTOGRAMA
	 * @author CARVAJAL
	 * @param info
	 */
	private void accionCargarImagenOdontograma(InfoOdontograma info) {
		/*
		 * SINO EXISTE VALORACION 
		 * SE CREA UN DTO ODONTOGRAMA PARA BUSCAR LA IMAGEN DEL ODONTOGRAMA
		 */
		DtoOdontograma dtoOdontograma = new DtoOdontograma();
		dtoOdontograma.setCodigoPk(info.getDtoInfoPlanTratamiento().getOdontogramaDiagnostico().doubleValue());
		info.getInfoPlanTrata().setImagen(Odontograma.cargarOdontograma(dtoOdontograma).getImagen());
	}
	
	
	/**
	 * BORRA ESTO O MODIFICARLO A FUTURO
	 * 
	 * */
	public DtoLogPlanTratamiento llenarDtoLogPlanTratamiento(InfoOdontograma info,DtoPlanTratamientoOdo dto)
	{
		DtoLogPlanTratamiento dtoLog = new DtoLogPlanTratamiento();
		dtoLog.setPlanTratamiento(dto.getCodigoPk().doubleValue());
		dtoLog.setEstado(dto.getEstado());
		dtoLog.setMotivo(new InfoDatosInt(dto.getMotivo(), ""));
		dtoLog.setEspecialidad(dto.getEspecialidad());
		dtoLog.setCodigoMedico(new InfoDatosInt(info.getCodigoMedico(), ""));
		dtoLog.setPorConfirmar(dto.getPorConfirmar());
		dtoLog.setCita(info.getCodigoCita());
		dtoLog.setValoracion(Utilidades.convertirADouble(info.getCodigoValoracion()+""));
		dtoLog.setEvolucion(Utilidades.convertirADouble(info.getCodigoEvolucion()+""));
		dtoLog.setImagen("log_"+dto.getIndicativo()+"_");
		dtoLog.getModificacion().setUsuarioModifica(info.getUsuarioActual().getUsuarioModifica());
		dtoLog.getModificacion().setFechaModifica(UtilidadFecha.getFechaActual());
		dtoLog.getModificacion().setHoraModifica(UtilidadFecha.getHoraActual());
		return dtoLog;
	}
	
	/**
	 * Llena la informacion del dto de detalle de planta de tratamiento 
	 * */
	public DtoDetallePlanTratamiento llenarDtoDetallePlanTratConInfo(
			InfoHallazgoSuperficie info,
			DtoPlanTratamientoOdo dto,
			int piezaDental,
			String seccion)
	{
		DtoDetallePlanTratamiento dtoDetalle = new DtoDetallePlanTratamiento();
		dtoDetalle.setPlanTratamiento(dto.getCodigoPk().doubleValue());
		dtoDetalle.setPiezaDental(piezaDental);
		
		if(info.getSuperficieOPCIONAL().getCodigo() > 0)
		{
			dtoDetalle.setSuperficie(info.getSuperficieOPCIONAL().getCodigo());
		}

		dtoDetalle.setHallazgo(info.getHallazgoREQUERIDO().getCodigo());
		dtoDetalle.setSeccion(seccion);
		dtoDetalle.setClasificacion(info.getClasificacion().getValue());
		dtoDetalle.setPorConfirmar(ConstantesBD.acronimoSi);
		dtoDetalle.setConvencion(Utilidades.convertirAEntero(info.getCodigoConvencion()));
		dtoDetalle.getFechaUsuarioModifica().setUsuarioModifica(dto.getUsuarioModifica().getUsuarioModifica());
		dtoDetalle.getFechaUsuarioModifica().setFechaModifica(UtilidadFecha.getFechaActual());
		dtoDetalle.getFechaUsuarioModifica().setHoraModifica(UtilidadFecha.getHoraActual());
		dtoDetalle.setEspecialidad(dto.getEspecialidad());
		dtoDetalle.setActivo(info.getExisteBD().isActivo()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		dtoDetalle.setValoracion(dto.getCodigoValoracion());
		dtoDetalle.setEvolucion(dto.getCodigoEvolucion());
		
		return dtoDetalle;
	}
	
	/**
	 * llena la información del log del detalle de plan de tratamiento
	 * */
	public DtoLogDetPlanTratamiento llenarDtoLogDetallePlanTratConInfo(InfoOdontograma info,DtoDetallePlanTratamiento dto)
	{
		DtoLogDetPlanTratamiento dtolog = new DtoLogDetPlanTratamiento();
		
		dtolog.setDetPlanTratamiento(dto.getCodigo());
		dtolog.setPiezaDental(dto.getPiezaDental());
		dtolog.setSuperficie(dto.getSuperficie());
		dtolog.setHallazgo(dto.getHallazgo());
		dtolog.setClasificacion(dto.getClasificacion());
		dtolog.setPorConfirmar(dto.getPorConfirmar());
		dtolog.setConvencion(dto.getConvencion());
		
		dtolog.getUsuarioModifica().setUsuarioModifica(info.getUsuarioActual().getUsuarioModifica());
		dtolog.getUsuarioModifica().setFechaModifica(UtilidadFecha.getFechaActual());
		dtolog.getUsuarioModifica().setHoraModifica(UtilidadFecha.getHoraActual());
		dtolog.setCita(info.getCodigoCita());
		dtolog.getEspecialidad().setCodigo(info.getEspecialidad());
		dtolog.setValoracion(Utilidades.convertirADouble(info.getCodigoValoracion()+""));
		dtolog.setEvolucion(Utilidades.convertirADouble(info.getCodigoEvolucion()+""));
		dtolog.setActivo(dto.getActivo());
		return dtolog;
	}
	
	/**
	 * Llena la informacion del dto detalle de programas y servicios
	 * */
	public DtoProgramasServiciosPlanT llenarDtoProgramasPlanTratConInfo(
			double codigoPrograma,
			DtoDetallePlanTratamiento dto,
			InfoServicios info, 
			boolean aplicaInclusion)
	{
		DtoProgramasServiciosPlanT dtoProg = new DtoProgramasServiciosPlanT();
		dtoProg.setDetPlanTratamiento(new BigDecimal(dto.getCodigo()));
		
		if(codigoPrograma > 0)
		{
			dtoProg.getPrograma().setCodigo(codigoPrograma);
		}
		
		dtoProg.getServicio().setCodigo(info.getServicio().getCodigo());
		dtoProg.setEstadoPrograma(ConstantesIntegridadDominio.acronimoEstadoPendiente);
		dtoProg.setConvencion(dto.getConvencion());
		dtoProg.setEstadoServicio(ConstantesIntegridadDominio.acronimoEstadoPendiente);
		dtoProg.setIndicativoPrograma(ConstantesIntegridadDominio.acronimoInicial);
		dtoProg.setIndicativoServicio(ConstantesIntegridadDominio.acronimoInicial);
		dtoProg.setPorConfirmado(ConstantesBD.acronimoSi);
		dtoProg.setEspecialidad(dto.getEspecialidad());
		dtoProg.getUsuarioModifica().setUsuarioModifica(dto.getFechaUsuarioModifica().getUsuarioModifica());
		dtoProg.getUsuarioModifica().setFechaModifica(UtilidadFecha.getFechaActual());
		dtoProg.getUsuarioModifica().setHoraModifica(UtilidadFecha.getHoraActual());
		dtoProg.setOrdenServicio(info.getOrderServicio());
		dtoProg.setActivo(info.getExisteBD().isActivo()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		dtoProg.setValoracion(dto.getValoracion());
		dtoProg.setEvolucion(dto.getEvolucion());
		dtoProg.setCodigoCita(dto.getCodigoCita());
		
		
		if(aplicaInclusion)
		{
			dtoProg.setInclusion(ConstantesBD.acronimoSi);
			dtoProg.setExclusion(ConstantesBD.acronimoNo);
			dtoProg.setEstadoPrograma(ConstantesIntegridadDominio.acronimoPorAutorizar);
		}
		
		return dtoProg;
	}
	
	/**
	 * */
	public DtoLogProgServPlant llenarDtoLogProgramasServicio(InfoOdontograma info,DtoProgramasServiciosPlanT dtoProg)
	{
		DtoLogProgServPlant dtolog = new DtoLogProgServPlant();
		dtolog.setProgServPlant(dtoProg.getCodigoPk().doubleValue());
		dtolog.setEstadoPrograma(dtoProg.getEstadoPrograma());
		dtolog.setConvencion(dtoProg.getConvencion());
		dtolog.setEstadoServicio(dtoProg.getEstadoServicio());
		dtolog.setIndPrograma(dtoProg.getIndicativoPrograma());
		dtolog.setIndServicio(dtoProg.getIndicativoServicio());
		dtolog.setPorConfirmar(dtoProg.getPorConfirmado());
		dtolog.setValoracion(Utilidades.convertirADouble(info.getCodigoValoracion()+""));
		dtolog.setEvolucion(Utilidades.convertirADouble(info.getCodigoEvolucion()+""));
		dtolog.getUsuarioModifica().setUsuarioModifica(dtoProg.getUsuarioModifica().getUsuarioModifica());
		dtolog.getUsuarioModifica().setFechaModifica(UtilidadFecha.getFechaActual());
		dtolog.getUsuarioModifica().setHoraModifica(UtilidadFecha.getHoraActual());
		dtolog.setOrdenServicio(dtoProg.getOrdenServicio());
		dtolog.setActivo(dtoProg.getActivo());
		dtolog.setEspecialidad(dtoProg.getEspecialidad());
		
		return dtolog;
	}
	
	/**
	 * Evalua si se debe eliminar el hallazgo
	 * @param String porConfirmarHallazgo
	 * */
	public boolean permiteEliminarHallazgo(String porConfirmarHallazgo)
	{
		if(porConfirmarHallazgo.equals(ConstantesBD.acronimoSi))
			return true;
		
		return false;
	}
	
	/**
	 * Evalua se permite eliminar los servicios y programas
	 * @param String porConfirmarHallazgo
	 * @param String porConfirmarProgServ
	 * */
	public boolean permiteEliminarProgServ(String porConfirmarHallazgo,String porConfirmarProgServ)
	{
		if(porConfirmarHallazgo.equals(ConstantesBD.acronimoSi) && 
				porConfirmarProgServ.equals(ConstantesBD.acronimoSi))
			return true;
		
		return false;
	}
	
	/**
	 * 
	 * */
	public void adicionarError(String error,String descripcion)
	{
		this.errores.add("descripcion",new ActionMessage(error,descripcion));
	}

	public String getForwardOdont() {
		return forwardOdont;
	}

	public void setForwardOdont(String forwardOdont) {
		this.forwardOdont = forwardOdont;
	}

	public ActionErrors getErrores() {
		return errores;
	}

	public void setErrores(ActionErrors errores) {
		this.errores = errores;
	}
	
	/**
	 * llenar array de dientes usados
	 * @param InfoOdontograma info
	 * */
	public static ArrayList<InfoDatosString> llenarDienteUsados(InfoOdontograma info)
	{
		ArrayList<InfoDatosString> dientes = new ArrayList<InfoDatosString>();
		
		for(InfoDetallePlanTramiento pieza: info.getInfoPlanTrata().getSeccionHallazgosDetalle())
		{
			if(pieza.getExisteBD().isActivo())
			{
				dientes.add(new InfoDatosString(pieza.getPieza().getCodigo()+""));
			}
		}
		
		return dientes;
	}
	
	
	//*****************************************************************************************
	//*****************************************************************************************
	//METODOS BUSQUEDA DE PROGRAMAS/SERVICIOS
	
	/**
	 * Abre el popup con la B&uacute;squeda de Programas/Servicios
	 * @param {@link InfoOdontograma} informaci&oacute;n del odontograma
	 * @param String secci&oacute;n para la cual se selecciona el programa
	 */
	private void accionAbrirBusquedaOdo(InfoOdontograma info,String seccion)
	{
		int codigoTipoHallazgo = ConstantesBD.codigoNuncaValido;
		
		if(seccion.equals(ConstantesIntegridadDominio.acronimoDetalle))
		{
			//Actualiza el valor de tipo de hallazgo
			if(info.getInfoPlanTrata().getSeccionHallazgosDetalle().get(info.getIndicador1()).getDetalleSuperficie().get(info.getIndicador2()).getSuperficieOPCIONAL().getCodigo() <= 0)
			{
				codigoTipoHallazgo = ComponenteOdontograma.codigoTipoHallazgoDiente;
				info.getInfoPlanTrata().getSeccionHallazgosDetalle().get(info.getIndicador1()).getDetalleSuperficie().get(info.getIndicador2()).getHallazgoREQUERIDO().setCodigo2(ComponenteOdontograma.codigoTipoHallazgoDiente);
			}
			else
			{
				codigoTipoHallazgo = ComponenteOdontograma.codigoTipoHallazgoSuper;
				info.getInfoPlanTrata().getSeccionHallazgosDetalle().get(info.getIndicador1()).getDetalleSuperficie().get(info.getIndicador2()).getHallazgoREQUERIDO().setCodigo2(ComponenteOdontograma.codigoTipoHallazgoSuper);
			}
			
			// Carga la información de la pieza/hallazgo/superficie
			info.getInfoGeneral().setCodigo(info.getInfoPlanTrata().getSeccionHallazgosDetalle().get(info.getIndicador1()).getPieza().getCodigo()+"");
			info.getInfoGeneral().setDescripcion(info.getInfoPlanTrata().getSeccionHallazgosDetalle().get(info.getIndicador1()).getDetalleSuperficie().get(info.getIndicador2()).getHallazgoREQUERIDO().getNombre());
			info.getInfoGeneral().setNombre(info.getInfoPlanTrata().getSeccionHallazgosDetalle().get(info.getIndicador1()).getDetalleSuperficie().get(info.getIndicador2()).getSuperficieOPCIONAL().getNombre());

			if(info.getInfoPlanTrata().getSeccionHallazgosDetalle().get(info.getIndicador1()).getDetalleSuperficie().get(info.getIndicador2()).getHallazgoREQUERIDO().getCodigo2() == ComponenteOdontograma.codigoTipoHallazgoDiente)
			{
				//Numero de Superficies con el mismo hallazgo dentro del mismo diente
				info.getInfoGeneral().setValor(new BigDecimal(ConstantesBD.codigoNuncaValido));
				//Indica si que la busqueda proviene de un hallazgo de diente
				info.getInfoGeneral().setIndicador(true);
			}
			else
			{
				//Numero de Superficies con el mismo hallazgo dentro del mismo diente
				info.getInfoGeneral().setValor(new BigDecimal(info.getInfoPlanTrata().getSeccionHallazgosDetalle().get(info.getIndicador1()).getNumeroSuperParaHallazgo(info.getIndicador3())));
				//Indica si que la busqueda proviene de un hallazgo de diente
				info.getInfoGeneral().setIndicador(false);
			}
		}
		else if(seccion.equals(ConstantesIntegridadDominio.acronimoOtro))
		{
			//Carga la información de la pieza/hallazgo/superficie
			info.getInfoGeneral().setCodigo(info.getInfoPlanTrata().getSeccionOtrosHallazgos().get(info.getIndicador1()).getPieza().getCodigo()+"");
			info.getInfoGeneral().setDescripcion(info.getInfoPlanTrata().getSeccionOtrosHallazgos().get(info.getIndicador1()).getDetalleSuperficie().get(info.getIndicador2()).getHallazgoREQUERIDO().getNombre());
			info.getInfoGeneral().setNombre(info.getInfoPlanTrata().getSeccionOtrosHallazgos().get(info.getIndicador1()).getDetalleSuperficie().get(info.getIndicador2()).getSuperficieOPCIONAL().getNombre());

			if(info.getInfoPlanTrata().getSeccionOtrosHallazgos().get(info.getIndicador1()).getDetalleSuperficie().get(info.getIndicador2()).getHallazgoREQUERIDO().getCodigo2() == ComponenteOdontograma.codigoTipoHallazgoDiente)
			{
				codigoTipoHallazgo = ComponenteOdontograma.codigoTipoHallazgoDiente;
				//Numero de Superficies con el mismo hallazgo dentro del mismo diente
				info.getInfoGeneral().setValor(new BigDecimal(ConstantesBD.codigoNuncaValido));
				//Indica si que la busqueda proviene de un hallazgo de diente
				info.getInfoGeneral().setIndicador(true);
			}
			else
			{
				codigoTipoHallazgo = ComponenteOdontograma.codigoTipoHallazgoSuper;
				InfoDetallePlanTramiento hallazgoSeleccionado=info.getInfoPlanTrata().getSeccionOtrosHallazgos().get(info.getIndicador1());
				//Numero de Superficies con el mismo hallazgo dentro del mismo diente
				info.getInfoGeneral().setValor(new BigDecimal(numeroSuperficiesMismoHallazgoOtros(info.getInfoPlanTrata().getSeccionOtrosHallazgos(), hallazgoSeleccionado)));
				//Indica si que la busqueda proviene de un hallazgo de diente
				info.getInfoGeneral().setIndicador(false);
			}
		}
		else if(seccion.equals(ConstantesIntegridadDominio.acronimoBoca))
		{
			//Carga la información de la pieza/hallazgo/superficie
			info.getInfoGeneral().setDescripcion(info.getInfoPlanTrata().getSeccionHallazgosBoca().get(info.getIndicador1()).getHallazgoREQUERIDO().getNombre());
			
			//Indica si que la busqueda proviene de un hallazgo de diente
			info.getInfoGeneral().setIndicador(true);
			
			//Numero de Superficies con el mismo hallazgo dentro del mismo diente
			info.getInfoGeneral().setValor(new BigDecimal(ConstantesBD.codigoNuncaValido));
			
			codigoTipoHallazgo = ComponenteOdontograma.codigoTipoHallazgoBoca;
		}
		
		DtoProgramasServiciosPlanT parametros = new DtoProgramasServiciosPlanT();
		parametros.setBuscarProgramas(info.getEsBuscarPorPrograma());
		parametros.setCodigoHallazgo(info.getIndicador3());// Info3 = codigo del programa
		parametros.setCodigoTipoHallazgo(codigoTipoHallazgo);
		info.setArrayProgServiPlanT(new ArrayList<InfoProgramaServicioPlan>());
		info.setArrayProgServiPlanT(PlanTratamiento.obtenerListadoProgramasServicio(parametros));
		
		/*
		 *Solo para boca 
		 */
		if(seccion.equals(ConstantesIntegridadDominio.acronimoBoca))
		{
			marcarServiciosYaSeleccionados(info, parametros.getCodigoHallazgo());
		}
		

		if(!seccion.equals(ConstantesIntegridadDominio.acronimoBoca))//la seccion boca no maneja superficies, los equivalentes se deben analizar a nivel de boca.
		{
			/*
			 * Tomo la superficie para verificar el hallazgo y cargar los equivalentes
			 */
			InfoHallazgoSuperficie superficie=null;
			if(seccion.equals(ConstantesIntegridadDominio.acronimoDetalle))
			{
				superficie=info.getInfoPlanTrata().getSeccionHallazgosDetalle().get(info.getIndicador1()).getDetalleSuperficie().get(info.getIndicador2());
			}
			else if(seccion.equals(ConstantesIntegridadDominio.acronimoOtro))
			{
				superficie=info.getInfoPlanTrata().getSeccionOtrosHallazgos().get(info.getIndicador1()).getDetalleSuperficie().get(info.getIndicador2());
			}
			
			/*
			 * Itero los programas de la superficie seleccionada
			 */
			for(InfoProgramaServicioPlan programa:superficie.getProgramasOservicios())
			{
				/*
				 * Si el programa está activo verifico equivalentes
				 */
				if(programa.getExisteBD().isActivo())
				{
					/*
					 * Busco los equivalentes para el hallazgo, programa, superficie
					 */
					ArrayList<Double> equivalentesProg = DetalleHallazgoProgramaServicio.obtenerEquivalentesProgServ(programa.getCodigoPkProgramaServicio(), superficie.getHallazgoREQUERIDO().getCodigo(), true);
					for(Double codigoEquivalente:equivalentesProg)
					{
						/*
						 * Itero los programas que se van a mostrar
						 */
						for(InfoProgramaServicioPlan progrmaListado:info.getArrayProgServiPlanT())
						{
							/*
							 * Si alguno de estos programas es equivalente del programa buscado, entonces lo marco como equivalente
							 */
							if(progrmaListado.getCodigoPkProgramaServicio().doubleValue()==codigoEquivalente)
							{
								/*
								 * Asigno el equivalente
								 */
								progrmaListado.setCodigoEquivalente(programa.getCodigoAmostrar());
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Método que se encarga de determinar cuales Programas / servicios ya han sido seleccionados 
	 * para el mismo hallazgo.
	 *
	 * @param info 
	 * @param codigoHallazgo 
	 * @param arrayProgServiPlanT
	 */
	private void marcarServiciosYaSeleccionados(InfoOdontograma info, int codigoHallazgo) {
		
		ArrayList<InfoProgramaServicioPlan> arrayProgServiPlanT = info.getArrayProgServiPlanT();

		for (InfoProgramaServicioPlan programa : arrayProgServiPlanT) {
			
			for (InfoHallazgoSuperficie hallazgo: info.getInfoPlanTrata().getSeccionHallazgosBoca()) {
				
				if(hallazgo.getExisteBD().isActivo() && hallazgo.getHallazgoREQUERIDO().getCodigo() == codigoHallazgo){
					
					for (InfoProgramaServicioPlan programaServicioPlan: hallazgo.getProgramasOservicios()){
						
						if(programaServicioPlan.getCodigoPkProgramaServicio().doubleValue() == programa.getCodigoPkProgramaServicio().doubleValue() && programaServicioPlan.getExisteBD().isActivo()){

							programa.setYaSeleccionado(true);
							break;
						}
					}
					
					if(programa.isYaSeleccionado()){
						break;
					}
				}
			}
		}
	}

	/**
	 * Obtiene el número de superficies en la secci&oacute;n otros hallazgos que poseen
	 * el mismo hallazgo y que pertenecen a la misma pieza dental
	 * @param seccionOtrosHallazgos Secci&oacute;n con la informaci&oacute;n de los hallazgos
	 * @param hallazgoSeleccionado {@link InfoDetallePlanTramiento} Hallazgo seleccionado para adicionar el programa
	 * @return int Cantidad de superficies.
	 * @author Juan Davis Ram&iacute;rez
	 * @since 2010-05-22
	 */
	private int numeroSuperficiesMismoHallazgoOtros(ArrayList<InfoDetallePlanTramiento> seccionOtrosHallazgos, InfoDetallePlanTramiento hallazgoSeleccionado)
	{
		/*
		 * Defino una variable en la cual voy a contar el numero de superficies
		 * que tienen el mismo hallazgo y pieza para verificar la carga del programa
		 */
		int cantidadSuperficies=0;
		InfoHallazgoSuperficie superficieExterna=hallazgoSeleccionado.getDetalleSuperficie().get(0);
		if(superficieExterna.getExisteBD().isActivo())
		{
			/*
			 * Se iteran los demás registros para verificar si tienen el mismo hallazgo
			 */
			for(InfoDetallePlanTramiento detallePlanTratamientoInt:seccionOtrosHallazgos)
			{
				/*
				 * Evalúa si el detalle está activo
				 */
				if(detallePlanTratamientoInt.getExisteBD().isActivo())
				{
					/*
					 * Se evalúan si pertenecen a la misma pieza dental
					 */
					if(hallazgoSeleccionado.getPieza().getCodigo()==detallePlanTratamientoInt.getPieza().getCodigo())
					{
						/*
						 * Siempre se toma la primera, ya que no hay posibilidad de tener más superficies en un
						 * hallazgo tipo otros
						 */
						InfoHallazgoSuperficie superficieInterna=detallePlanTratamientoInt.getDetalleSuperficie().get(0);
						if(superficieExterna.getHallazgoREQUERIDO().getCodigo()==superficieInterna.getHallazgoREQUERIDO().getCodigo())
						{
							/*
							 * Encontré una superficie con el mismo hallazgo, incremento la variable
							 */
							cantidadSuperficies++;
						}
					}
				}
			}
		}
		return cantidadSuperficies;
	}
	
	/**
	 * Adiciona prgramas y servicios a la secciones
	 * @param InfoOdontograma info
	 * @param String seccion
	 * @return numero de superficies, -1 en caso de error
	 * */
	private int accionAddServPlanTr(InfoOdontograma info,String seccion)
	{
		InfoProgramaServicioPlan programaSeleccionado=info.getArrayProgServiPlanT().get(info.getIndiceProgramaSeleccionado());
		BigDecimal codigoPkPrgServ = new BigDecimal(programaSeleccionado.getCodigoPkProgramaServicio().intValue());
		int numeroSuperficiesPrograma=programaSeleccionado.getNumeroSuperficies();

		/*
		 * En este caso es un hallazgo de boca, los hallazfgos de boca poseen una superficie
		 * la cual tiene en el nombre la cadena "", por lo tanto
		 * lo cambio a 1 superficie
		 */
		if(numeroSuperficiesPrograma==0)
		{
			numeroSuperficiesPrograma=1;
		}
		
		if(info.getSuperficiesSeleccionadasXPrograma().length==0)
		{
			this.adicionarError("errors.notEspecific","No seleccionó ninguna pieza dental");
			return ConstantesBD.codigoNuncaValido;
		}
		
		/*
		 * El indicador 2 se usa para saber la superficie, como tienen el mismo hallazgo
		 * puedo tomar la primera para hacer todas las validaciones
		 */
		info.setIndicador2(info.getSuperficiesSeleccionadasXPrograma()[0]);
		/*
		 * El indicador 3 se usa para guardar el índice del programa
		 */
		info.setIndicador3(info.getIndiceProgramaSeleccionado());

		if(seccion.equals(ConstantesIntegridadDominio.acronimoDetalle))
		{			
			/*------------------------- EQUIVALENTES -------------------------------*/
			
			/*
			 * Si viene de equivalente selecciono automáticamente las superficies del programa
			 */
			if(info.getEquivalente())
			{
				info.setSuperficiesSeleccionadasXPrograma(new int[numeroSuperficiesPrograma]);
				InfoDetallePlanTramiento pieza=info.getInfoPlanTrata().getSeccionHallazgosDetalle().get(info.getIndicador1());
				
				/*
				 * Tomo el color de la letra del programa seleccionado
				 */
				InfoHallazgoSuperficie supSeleccionada=pieza.getDetalleSuperficie().get(info.getIndiceSuperficieSeleccionada());
				
				/*
				 * Se busca el programa el cual es equivalente al seleccionado de la lista 
				 */
				String colorLetra="";
	            for(InfoProgramaServicioPlan progTempo:supSeleccionada.getProgramasOservicios())
	            {
	            	/*
	            	 * Si lo encuentro, entonces tomo el color de letra para eliminar las relaciones
	            	 */
	            	if(progTempo.getExisteBD().isActivo() && progTempo.getCodigoAmostrar().equals(info.getArrayProgServiPlanT().get(info.getIndiceProgramaSeleccionado()).getCodigoEquivalente()))
	            	{
	            		colorLetra=progTempo.getProgHallazgoPieza().getColorLetra();
	            	}
	            }
				
	            /*
	             * Se buscan los equivlaentes para el programa
	             */
	            ArrayList<Double> equivalentesProg = DetalleHallazgoProgramaServicio.obtenerEquivalentesProgServ(new BigDecimal(info.getCodigoProgramaSeleccionado()), supSeleccionada.getHallazgoREQUERIDO().getCodigo(), true);

				int i=0;
				int cantidadSuperficiesAsignadas=0;
				for(InfoHallazgoSuperficie superficie:pieza.getDetalleSuperficie())
				{
					// Si se seleccionó solamente una superficie
					//if(numeroSuperficiesPrograma)
					if(superficie.getExisteBD().isActivo())
					{
						/*
						 * Itero los programas de la superficie
						 */
						for(InfoProgramaServicioPlan programa:superficie.getProgramasOservicios())
						{
							/*
							 * Solamente evalúo si es activo
							 */
							if(programa.getExisteBD().isActivo())
							{
								/*
								 * Itero los equivalentes
								 */
								for(Double equival:equivalentesProg)
								{
									/*
									 * Si tienen el mismo color de la letra es porque perteneces a la misma superficie
									 */
									if(programa.getCodigoPkProgramaServicio().intValue()==equival && programa.getProgHallazgoPieza().getColorLetra().equals(colorLetra))
									{
										for(InfoProgramaServicioPlan programaSel:supSeleccionada.getProgramasOservicios())
										{
											if(programa.getProgHallazgoPieza().getCodigoPk()==programaSel.getProgHallazgoPieza().getCodigoPk())
											{
												/*
												 * Asigno la superficie seleccionada
												 */
												info.getSuperficiesSeleccionadasXPrograma()[cantidadSuperficiesAsignadas]=i;
												cantidadSuperficiesAsignadas++;
												break;
											}
										}
									}
								}
							}
						}
					}
					i++;
				}
			}

			/*--------------------- AGREGAR PROGRAMAS --------------------------------*/
			
			//verifica que no posea el programa/servicio
			for(int i=0; i<info.getSuperficiesSeleccionadasXPrograma().length; i++)
			{
				InfoDetallePlanTramiento plan=info.getInfoPlanTrata().getSeccionHallazgosDetalle().get(info.getIndicador1());
				InfoHallazgoSuperficie superficie=plan.getDetalleSuperficie().get(info.getSuperficiesSeleccionadasXPrograma()[i]);
				if(superficie.existenProgramaOserviciosActivos(info.getEsBuscarPorPrograma(),codigoPkPrgServ.intValue()))
				{
					if(UtilidadTexto.isEmpty(superficie.getSuperficieOPCIONAL().getNombre()))
					{
						this.adicionarError("errors.notEspecific","El Programa/Servicio ya existe en la pieza");	
					}
					else
					{
						this.adicionarError("errors.notEspecific","El Programa/Servicio ya existe en la superficie "+superficie.getSuperficieOPCIONAL().getNombre());
					}
				}
			}
			/*
			 * Si enctonró problemas en los programas de las superficies entonces retorna "false" para indicar errror
			 */
			if(!errores.isEmpty())
			{
				return ConstantesBD.codigoNuncaValido;
			}
			
			 
			//Carga la información del programa/servicio
			InfoProgramaServicioPlan nuevoProgServ = new InfoProgramaServicioPlan();
			DtoProgramasServiciosPlanT parametros = new DtoProgramasServiciosPlanT();
			parametros.setPrograma(new InfoDatosDouble(codigoPkPrgServ.doubleValue(),""));
			parametros.setCargarServicios(true);
			nuevoProgServ = PlanTratamiento.obtenerInfoProgramaServicios(parametros);
			nuevoProgServ.setNombreProgramaServicio(nuevoProgServ.getCodigoAmostrar()+" "+nuevoProgServ.getNombreProgramaServicio());
			nuevoProgServ.getExisteBD().setActivo(true);
			nuevoProgServ.getExisteBD().setValue(ConstantesBD.acronimoNo);
			nuevoProgServ.setNumeroSuperficies(info.getSuperficiesSeleccionadasXPrograma().length);
			
			if(numeroSuperficiesPrograma!=info.getSuperficiesSeleccionadasXPrograma().length)
			{
				this.adicionarError("errors.notEspecific", "El programa requiere la selección de "+numeroSuperficiesPrograma+" superficies");
				return ConstantesBD.codigoNuncaValido;
			}
			//verifica que posea servicios
			if(nuevoProgServ.getListaServicios().size() <= 0)
			{
				this.adicionarError("errors.notEspecific","El Programa ["+nuevoProgServ.getNombreProgramaServicio()+"] Seleccionado No Posee Servicios");
				return ConstantesBD.codigoNuncaValido;
			}
			
			// Voy a verificar el color a mostrar dependiendo del programa
			InfoDetallePlanTramiento pieza=info.getInfoPlanTrata().getSeccionHallazgosDetalle().get(info.getIndicador1());
			ColoresPlanTratamiento colorPlan=ColoresPlanTratamiento.NEGRO;
			if(numeroSuperficiesPrograma>1)
			{
				colorPlan=ColoresPlanTratamiento.MORADO;
				for(InfoHallazgoSuperficie superf:pieza.getDetalleSuperficie())
				{
					if(superf.getExisteBD().isActivo())
					{
						// Verifico que la superficie tenga seleccionado alguún programa
						if(superf.getProgramasOservicios()!=null && superf.getProgramasOservicios().size()>0)
						{
							// Itero los posibles colores
							forColores:for(ColoresPlanTratamiento color:EnumSet.range(ColoresPlanTratamiento.MORADO, ColoresPlanTratamiento.NARANJA))
							{
								if(color.getIndice()>colorPlan.getIndice())
								{
									colorPlan=color;
								}
								for(InfoProgramaServicioPlan prog:superf.getProgramasOservicios())
								{
									/*
									 * Si es activo se evalúa
									 */
									if(prog.getExisteBD().isActivo())
									{
										/*
										 * Si es el mismo programa, se aumenta el color
										 */
										if(nuevoProgServ.getCodigoPkProgramaServicio().intValue() == prog.getCodigoPkProgramaServicio().intValue())
										{
											// Se verifica si ya fue seleccionado el color
											if(color.getColor().equals(prog.getProgHallazgoPieza().getColorLetra()))
											{
												// Si ya fue seleccionado se sigue con el siguiente color
												continue forColores;
											}
										}
									}
								}
								break;
							}
						}
					}
				}
			}
			
			for(int i=0; i<info.getSuperficiesSeleccionadasXPrograma().length; i++)
			{
				InfoHallazgoSuperficie superficie=pieza.getDetalleSuperficie().get(info.getSuperficiesSeleccionadasXPrograma()[i]);
				nuevoProgServ = new InfoProgramaServicioPlan();
				parametros = new DtoProgramasServiciosPlanT();
				parametros.setPrograma(new InfoDatosDouble(codigoPkPrgServ.doubleValue(),""));
				parametros.setCargarServicios(true);
				nuevoProgServ = PlanTratamiento.obtenerInfoProgramaServicios(parametros);
				nuevoProgServ.setNombreProgramaServicio(nuevoProgServ.getCodigoAmostrar()+" "+nuevoProgServ.getNombreProgramaServicio());
				nuevoProgServ.getExisteBD().setActivo(true);
				nuevoProgServ.getExisteBD().setValue(ConstantesBD.acronimoNo);
				DtoProgHallazgoPieza dtoProgHallazgoPieza=new DtoProgHallazgoPieza();
				dtoProgHallazgoPieza.setCodigoPk(info.getContadorCodigoTemporalProgramaHallazgoPieza());
				dtoProgHallazgoPieza.setColorLetra(colorPlan.getColor());
				nuevoProgServ.setProgHallazgoPieza(dtoProgHallazgoPieza);
				nuevoProgServ.setNumeroSuperficies(info.getSuperficiesSeleccionadasXPrograma().length);
			
				/*
				 * Obtengo los equivalentes del hallazgo
				 */
				ArrayList<Double> equivalentesProg=DetalleHallazgoProgramaServicio.obtenerEquivalentesProgServ(nuevoProgServ.getCodigoPkProgramaServicio(), superficie.getHallazgoREQUERIDO().getCodigo(), true);

				/*
				 * Itero los equivalentes, en caso de encontrar uno 
				 * seleccionado en el plan de tratamiento, lo elimino
				 */
				for(Double programa:equivalentesProg)
				{
					for(InfoProgramaServicioPlan programaEvalEquivalente:superficie.getProgramasOservicios())
					{
						// Si encuentro el mismo código lo elimino
						if(programaEvalEquivalente.getCodigoPkProgramaServicio().doubleValue()==programa)
						{
							// Inactivo el programa equivalente
							programaEvalEquivalente.getExisteBD().setActivo(false);
						}
					}
				}
				
				boolean adicionePrograma=false;
				int indiPrograma=0;
				for(InfoProgramaServicioPlan programa:superficie.getProgramasOservicios())
				{
					if(programa.getCodigoPkProgramaServicio().intValue()==nuevoProgServ.getCodigoPkProgramaServicio().intValue())
					{
						adicionePrograma=true;
						superficie.getProgramasOservicios().set(indiPrograma, nuevoProgServ);
					}
					indiPrograma++;
				}
				if(!adicionePrograma)
				{
					superficie.getProgramasOservicios().add(nuevoProgServ);
				}
			}
				
			//Validación
		}
		else if(seccion.equals(ConstantesIntegridadDominio.acronimoOtro))
		{
			/*------------------------- EQUIVALENTES -------------------------------*/
			
			/*
			 * Si viene de equivalente selecciono automáticamente las superficies del programa
			 */
			if(info.getEquivalente())
			{
				info.setSuperficiesSeleccionadasXPrograma(new int[numeroSuperficiesPrograma]);
				InfoDetallePlanTramiento piezaSeleccionada=info.getInfoPlanTrata().getSeccionOtrosHallazgos().get(info.getIndicador1());

				/*
				 * Se toma el color de la letra del programa seleccionado, La superficie siempre se la 0
				 * sin embargo lo dejo abierto por si cambia en el futuro
				 */
				InfoHallazgoSuperficie supSeleccionada=piezaSeleccionada.getDetalleSuperficie().get(info.getIndiceSuperficieSeleccionada());

				/*
				 * Se busca el programa el cual es equivalente al seleccionado de la lista 
				 */
				String colorLetra="";
	            for(InfoProgramaServicioPlan progTempo:supSeleccionada.getProgramasOservicios())
	            {
	            	/*
	            	 * Si lo encuentro, entonces tomo el color de letra para eliminar las relaciones
	            	 */
	            	if(progTempo.getExisteBD().isActivo() && progTempo.getCodigoAmostrar().equals(info.getArrayProgServiPlanT().get(info.getIndiceProgramaSeleccionado()).getCodigoEquivalente()))
	            	{
	            		colorLetra=progTempo.getProgHallazgoPieza().getColorLetra();
	            	}
	            }

	            /*
	             * Se buscan los equivalentes para el programa
	             */
	            ArrayList<Double> equivalentesProg = DetalleHallazgoProgramaServicio.obtenerEquivalentesProgServ(new BigDecimal(info.getCodigoProgramaSeleccionado()), supSeleccionada.getHallazgoREQUERIDO().getCodigo(), true);

	            /*
	             * Se define i para obtener el índice de cada pieza a modificar
	             */
				int i=0;
				
				/*
				 * Se cuentan el número de superficies a cambiar, para el manejo del array que mantiene los índices de las superficies
				 */
				int cantidadSuperficiesAsignadas=0;
				
				/*
				 * Se iteran las piezas para encontrar programas equivalentes
				 */
				for(InfoDetallePlanTramiento pieza:info.getInfoPlanTrata().getSeccionOtrosHallazgos())
				{
					/*
					 * Si la pieza está activa, se evalúa; de todas maneras se incrementa la i, ya que va a contener el índice de la pieza
					 */
					if(pieza.getExisteBD().isActivo())
					{
						/*
						 * Si es la misma pieza evalúo el equivalente
						 */
						if(pieza.getPieza().getCodigo()==piezaSeleccionada.getPieza().getCodigo())
						{
							/*
							 * Se toma la única posible superficie del hallazgo
							 */
							InfoHallazgoSuperficie superficie=pieza.getDetalleSuperficie().get(0);
							
							/*
							 * Si la superficie está activa, se evalúa (Posiblemente siempre esté activa)
							 */
							if(superficie.getExisteBD().isActivo())
							{
								/*
								 * Si el hallazgo es el mismo se evalúa la posibilidad de cambiar el equivalente
								 */
								if(superficie.getHallazgoREQUERIDO().getCodigo()==supSeleccionada.getHallazgoREQUERIDO().getCodigo())
								{
									/*
									 * Itero los programas de la superficie
									 */
									for(InfoProgramaServicioPlan programa:superficie.getProgramasOservicios())
									{
										/*
										 * Solamente evalúo si es activo
										 */
										if(programa.getExisteBD().isActivo())
										{
											/*
											 * Itero los equivalentes
											 */
											for(Double equival:equivalentesProg)
											{
												/*
												 * Si tienen el mismo color de la letra es porque perteneces a la misma superficie
												 */
												if(programa.getCodigoPkProgramaServicio().intValue()==equival && programa.getProgHallazgoPieza().getColorLetra().equals(colorLetra))
												{
													/*
													 * Se asigna la superficie seleccionada
													 */
													info.getSuperficiesSeleccionadasXPrograma()[cantidadSuperficiesAsignadas]=i;
													cantidadSuperficiesAsignadas++;
													programa.getExisteBD().setActivo(false);
													break;
												}
											}
										}
									}
								}
							}
						}
					}
					i++;
				}
			}

			/*--------------------- AGREGAR PROGRAMAS --------------------------------*/

			
			//Verifica el numero de superficies que posee el programa/servicio
			if(info.getArrayProgServiPlanT().get(info.getIndicador3()).getNumeroSuperficies() == 1 
					|| info.getInfoGeneral().isIndicador())
			{
				//Carga la información del programa/servicio
				InfoProgramaServicioPlan nuevoProgServ = new InfoProgramaServicioPlan();
				DtoProgramasServiciosPlanT parametros = new DtoProgramasServiciosPlanT();
				parametros.setPrograma(new InfoDatosDouble(codigoPkPrgServ.doubleValue(),""));
				nuevoProgServ = PlanTratamiento.obtenerInfoProgramaServicios(parametros);
				nuevoProgServ.setNombreProgramaServicio(nuevoProgServ.getCodigoAmostrar()+" "+nuevoProgServ.getNombreProgramaServicio());
				nuevoProgServ.getExisteBD().setActivo(true);
				nuevoProgServ.getExisteBD().setValue(ConstantesBD.acronimoNo);
				DtoProgHallazgoPieza dtoProgHallazgoPieza=new DtoProgHallazgoPieza();
				dtoProgHallazgoPieza.setCodigoPk(info.getContadorCodigoTemporalProgramaHallazgoPieza());
				dtoProgHallazgoPieza.setColorLetra(ColoresPlanTratamiento.NEGRO.getColor());
				nuevoProgServ.setProgHallazgoPieza(dtoProgHallazgoPieza);
				
				info.getInfoPlanTrata().getSeccionOtrosHallazgos().get(info.getIndicador1()).getDetalleSuperficie().get(info.getIndicador2()).getProgramasOservicios().add(nuevoProgServ);
			}else if(info.getArrayProgServiPlanT().get(info.getIndicador3()).getNumeroSuperficies()>1){
				/*
				 * Se seleccionó un programa de N superficies
				 */
				
				/*
				 * Se toma la pieza seleccionada
				 */
				InfoDetallePlanTramiento piezaSeleccionada=info.getInfoPlanTrata().getSeccionOtrosHallazgos().get(info.getIndicador1());
				
				/*
				 * Se verifica la cantidad de superficies
				 */
				if(numeroSuperficiesPrograma!=info.getSuperficiesSeleccionadasXPrograma().length)
				{
					this.adicionarError("errors.notEspecific", "El programa requiere la selección de "+numeroSuperficiesPrograma+" superficies");
					return ConstantesBD.codigoNuncaValido;
				}
				
				/*
				 * Se empieza buscando por el morado
				 */
				ColoresPlanTratamiento colorLetra=ColoresPlanTratamiento.NEGRO;
				forColores:for(ColoresPlanTratamiento color:EnumSet.range(ColoresPlanTratamiento.MORADO, ColoresPlanTratamiento.NARANJA))
				{
					if(color.getIndice()>colorLetra.getIndice())
					{
						colorLetra=color;
					}
					/*
					 * Se iteran las piezas
					 */
					for(InfoDetallePlanTramiento pieza:info.getInfoPlanTrata().getSeccionOtrosHallazgos())
					{
						/*
						 * Si la pieza es activa se evalúa
						 */
						if(pieza.getExisteBD().isActivo())
						{
							/*
							 * Si pertenecen a la misma pieza dental
							 */
							if(pieza.getPieza().getCodigo()==piezaSeleccionada.getPieza().getCodigo())
							{
								/*
								 * Se toma la superficie (Siempre es la 0)
								 */
								InfoHallazgoSuperficie superficie=pieza.getDetalleSuperficie().get(0);

								/*
								 * Si la superficie
								 */
								if(superficie.getHallazgoREQUERIDO().getCodigo()==piezaSeleccionada.getDetalleSuperficie().get(0).getHallazgoREQUERIDO().getCodigo())
								{
									/*
									 * Se iteran los programas de la pieza buscando los colores
									 */
									for(InfoProgramaServicioPlan programa:superficie.getProgramasOservicios())
									{
										/*
										 * Si el programa es activo se evalúa
										 */
										if(programa.getExisteBD().isActivo())
										{
											/*
											 * si es el mismo programa aumenta el color de la letra
											 */
											if(programa.getCodigoPkProgramaServicio().intValue()==codigoPkPrgServ.intValue())
											{
												/*
												 * Verifica si ya fue seleccionado el color
												 */
												if(color.getColor().equals(programa.getProgHallazgoPieza().getColorLetra()))
												{
													continue forColores;
												}
											}
										}
									}
								}
							}
						}
					}
					/*
					 * Si no encontró más colores se rompe el ciclo
					 */
					break forColores;
				}
					
				
				/*
				 * Se iteran los índices de las superficies 
				 */
				for(Integer indiceSuperficieAgregar:info.getSuperficiesSeleccionadasXPrograma())
				{
					/*
					 * Se crea la estructura del programa a agregar
					 */
					InfoProgramaServicioPlan nuevoProgServ = new InfoProgramaServicioPlan();
					DtoProgramasServiciosPlanT parametros = new DtoProgramasServiciosPlanT();
					parametros.setPrograma(new InfoDatosDouble(codigoPkPrgServ.doubleValue(),""));
					nuevoProgServ = PlanTratamiento.obtenerInfoProgramaServicios(parametros);
					nuevoProgServ.setNombreProgramaServicio(nuevoProgServ.getCodigoAmostrar()+" "+nuevoProgServ.getNombreProgramaServicio());
					nuevoProgServ.getExisteBD().setActivo(true);
					nuevoProgServ.getExisteBD().setValue(ConstantesBD.acronimoNo);
					DtoProgHallazgoPieza dtoProgHallazgoPieza=new DtoProgHallazgoPieza();
					dtoProgHallazgoPieza.setCodigoPk(info.getContadorCodigoTemporalProgramaHallazgoPieza());
					dtoProgHallazgoPieza.setColorLetra(colorLetra.getColor());
					nuevoProgServ.setNumeroSuperficies(numeroSuperficiesPrograma);
					nuevoProgServ.setProgHallazgoPieza(dtoProgHallazgoPieza);

					/*
					 * Se agrega el programa al hallazgo
					 */
					info.getInfoPlanTrata().getSeccionOtrosHallazgos().get(indiceSuperficieAgregar).getDetalleSuperficie().get(0).getProgramasOservicios().add(nuevoProgServ);
				}
			}
		}	
		else if(seccion.equals(ConstantesIntegridadDominio.acronimoBoca))
		{
			//Carga la información del programa/servicio
			InfoProgramaServicioPlan nuevoProgServ = new InfoProgramaServicioPlan();
			DtoProgramasServiciosPlanT parametros = new DtoProgramasServiciosPlanT();
			parametros.setPrograma(new InfoDatosDouble(codigoPkPrgServ.doubleValue(),""));
			nuevoProgServ = PlanTratamiento.obtenerInfoProgramaServicios(parametros);
			nuevoProgServ.setNombreProgramaServicio(nuevoProgServ.getCodigoAmostrar()+" "+nuevoProgServ.getNombreProgramaServicio());
			nuevoProgServ.getExisteBD().setActivo(true);
			nuevoProgServ.getExisteBD().setValue(ConstantesBD.acronimoNo);
			DtoProgHallazgoPieza dtoProgHallazgoPieza=new DtoProgHallazgoPieza();
			dtoProgHallazgoPieza.setCodigoPk(info.getContadorCodigoTemporalProgramaHallazgoPieza());
			dtoProgHallazgoPieza.setColorLetra(ColoresPlanTratamiento.NEGRO.getColor());
			nuevoProgServ.setProgHallazgoPieza(dtoProgHallazgoPieza);
			
			info.getInfoPlanTrata().getSeccionHallazgosBoca().get(info.getIndicador1()).getProgramasOservicios().add(nuevoProgServ);
		}
		
		/*
		 * Si el hallazgo fue por equivalente se comporta como si se hubiera marcado tan solo
		 * una superficie
		 */
		if(info.getEquivalente())
		{
			return 1;
		}
		
		return numeroSuperficiesPrograma;
	}

	public String getEstadoInterno() {
		return estadoInterno;
	}

	public void setEstadoInterno(String estadoInterno) {
		this.estadoInterno = estadoInterno;
	}

	public Connection getConInterna() {
		return conInterna;
	}

	public void setConInterna(Connection conInterna) {
		this.conInterna = conInterna;
	}

	/**
	 * @return the porConfirmar
	 */
	public String getPorConfirmar() {
		return porConfirmar;
	}

	/**
	 * @param porConfirmar the porConfirmar to set
	 */
	public void setPorConfirmar(String porConfirmar) {
		this.porConfirmar = porConfirmar;
	}
	
	
  private boolean existeEquivalente(int codProg, ArrayList<Double> lista)
	{
	 boolean existe=false;
	 for(Double elem: lista )
	 {
		 if(elem.intValue()==codProg)
		 {
			 existe = true;
		 }
	 }	 
	 return existe;
	}
}