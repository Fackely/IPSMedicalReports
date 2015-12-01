package com.servinte.axioma.mundo.impl.capitacion;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;
import org.axioma.util.log.Log4JManager;
import org.hibernate.criterion.Projections;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.cargos.CargosEntidadesSubcontratadasDao;
import com.princetonsa.dto.capitacion.DTONivelAutorServMedic;
import com.princetonsa.dto.capitacion.DTONivelAutorizacion;
import com.princetonsa.dto.capitacion.DTONivelAutorizacionOcupacionMedica;
import com.princetonsa.dto.capitacion.DTOValidacionNivelAutoAutomatica;
import com.princetonsa.dto.capitacion.DtoParametrosValidacionNivelAutorizacion;
import com.princetonsa.dto.capitacion.DtoValidacionNivelesAutorizacion;
import com.princetonsa.dto.facturacion.DtoContratoEntidadSub;
import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.princetonsa.dto.facturacion.DtoServicios;
import com.princetonsa.dto.manejoPaciente.DtoCentroCosto;
import com.princetonsa.dto.manejoPaciente.DtoCentroCostosVista;
import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;
import com.princetonsa.mundo.cargos.CargosEntidadesSubcontratadas;
import com.princetonsa.mundo.inventarios.FarmaciaCentroCosto;
import com.servinte.axioma.dao.fabrica.inventario.InventarioDAOFabrica;
import com.servinte.axioma.dao.impl.administracion.CentroCostosDAO;
import com.servinte.axioma.dao.interfaz.administracion.ICentroCostosDAO;
import com.servinte.axioma.dao.interfaz.inventario.IArticuloDAO;
import com.servinte.axioma.mundo.fabrica.odontologia.administracion.AdministracionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.administracion.ICentroAtencionMundo;
import com.servinte.axioma.mundo.interfaz.administracion.ICentroCostoMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IProcesoNivelAutorizacionManualMundo;
import com.servinte.axioma.orm.Articulo;
import com.servinte.axioma.orm.CentroAtencion;
import com.servinte.axioma.orm.CentrosCosto;
import com.servinte.axioma.orm.EntidadesSubcontratadas;
import com.servinte.axioma.orm.GrupoInventario;
import com.servinte.axioma.orm.delegate.facturacion.EntidadesSubcontratadasDelegate;
import com.servinte.axioma.servicio.fabrica.capitacion.CapitacionFabricaServicio;
import com.servinte.axioma.servicio.fabrica.facturacion.FacturacionServicioFabrica;
import com.servinte.axioma.servicio.fabrica.inventario.InventarioServicioFabrica;
import com.servinte.axioma.servicio.fabrica.odontologia.administracion.AdministracionFabricaServicio;
import com.servinte.axioma.servicio.interfaz.administracion.ICentroCostosServicio;
import com.servinte.axioma.servicio.interfaz.administracion.IMedicosServicio;
import com.servinte.axioma.servicio.interfaz.administracion.IUsuariosServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.INivelAutorServMedicServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.INivelAutorizacionServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.IPrioridadOcupacionMedicaServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.IPrioridadUsuarioEspecificoServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.IEntidadesSubcontratadasServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.IServiciosServicio;
import com.servinte.axioma.servicio.interfaz.inventario.IGrupoInventarioServicio;

public class ProcesoNivelAutorizacionManualMundo implements IProcesoNivelAutorizacionManualMundo
{
	
	/** Contiene los mensajes genéricos para esta funcionalidad * */
	MessageResources fuenteMensaje = MessageResources.getMessageResources("com.servinte.mensajes.capitacion.ProcesoNivelAutorizacionManualForm");
	
	/**
	 * 
	 * Este Método realiza el proceso de validación para saber el Nivel de Autorización que aplica tanto al usuario 
	 * como a cada uno de los servicios / artículos de las ordenes a autorizar y la entidad subcontratada autorizada,
	 * para niveles de autorizacion de tipo manual
	 * 
	 * @author Fabian Becerra
	 */
	public DTOValidacionNivelAutoAutomatica validarNivelAutorizacionParaAutorizacionesPoblacionCapitada(DtoParametrosValidacionNivelAutorizacion dto, int codigoInstitucion)
	{
		
		DTOValidacionNivelAutoAutomatica validacion=new DTOValidacionNivelAutoAutomatica();
		ActionErrors errores = new ActionErrors();
		
		Connection con=UtilidadBD.abrirConexion();
		
		ArrayList<DtoValidacionNivelesAutorizacion> nivelAutorizacion=new ArrayList<DtoValidacionNivelesAutorizacion>();
		
		//1. NIVELES DE AUTORIZACION POR USUARIO 
		
		//1.1 NIVEL DE AUTORIZACION DEL USUARIO ESPECIFICO
		INivelAutorizacionServicio servicioNivelAuto=CapitacionFabricaServicio.crearNivelAutorizacionServicio();
		ArrayList<DtoValidacionNivelesAutorizacion> listaValidacionNivelAuto=servicioNivelAuto.buscarNivelAutorizacionPorUsuario(dto.getLoginUsuario());
		
		//CARGAR LAS PRIORIDADES DEL NIVEL DE AUTORIZACION
		ArrayList<Integer> prioridades;
		for(DtoValidacionNivelesAutorizacion dtoValidacionNivelAuto : listaValidacionNivelAuto){
			prioridades = new ArrayList<Integer>();
			IPrioridadUsuarioEspecificoServicio servicioPrioUsuEspecServicio=CapitacionFabricaServicio.crearPrioridadUsuarioEspecificoServicio();
			prioridades=servicioPrioUsuEspecServicio.obtenerNumerosPrioridadOrdenadosPorNivelAutorizacion(dtoValidacionNivelAuto.getCodigoNivelAutorizacionUsuario());
			
			dtoValidacionNivelAuto.setPrioridades(prioridades);
			
			nivelAutorizacion.add(dtoValidacionNivelAuto);
		}
		
		/**
		 * Se realiza un cambio en el DCU 1105 donde tambien se debe buscar por ocupaci&oacute;n medica los niveles del autorizaci&oacute;n
		 * sin importar si tiene o no niveles por usuario especifico
		 * 
		 * Diana Ruiz
		 * 
		 */
		
		
		IMedicosServicio servicio = AdministracionFabricaServicio.crearMedicosServicio();
		DTONivelAutorizacionOcupacionMedica ocupacionMedica=servicio.obtenerOcupacionMedicaDeUsuarioProfesional(dto.getLoginUsuario());
		
		// Si hay niveles por usuario especifico y el usuario no es profesional de la salud
		if(nivelAutorizacion.size()<=0 && ocupacionMedica==null){
			
			IUsuariosServicio servicioUsuarios=AdministracionFabricaServicio.crearUsuariosServicio();
			DtoUsuarioPersona dtoUsuPersona=servicioUsuarios.obtenerDtoUsuarioPersona(dto.getLoginUsuario());
			
			errores.add("No es Profesional", new ActionMessage("errors.notEspecific", 
					fuenteMensaje.getMessage("ProcesoNivelAutorizacionManualForm.noTieneNivelAutorizacion",
							dtoUsuPersona.getNombre()+" "+dtoUsuPersona.getApellido())));			
		} else 
			{
				servicioNivelAuto=CapitacionFabricaServicio.crearNivelAutorizacionServicio();
				listaValidacionNivelAuto=servicioNivelAuto.buscarNivelAutorizacionPorOcupacionMedica(ocupacionMedica.getOcupacionMedicaID());
				
				// Si no hay niveles por ocupaci&oacute;n ni por usuario especifico
				if(nivelAutorizacion.size()<=0 && Utilidades.isEmpty(listaValidacionNivelAuto)){
					
					IUsuariosServicio servicioUsuarios=AdministracionFabricaServicio.crearUsuariosServicio();
					DtoUsuarioPersona dtoUsuPersona=servicioUsuarios.obtenerDtoUsuarioPersona(dto.getLoginUsuario());
					errores.add("No tiene nivel autorizacion", new ActionMessage("errors.notEspecific", 
							fuenteMensaje.getMessage("ProcesoNivelAutorizacionManualForm.noTieneNivelAutorizacion",
									dtoUsuPersona.getNombre()+" "+dtoUsuPersona.getApellido())));				
				} else
					{
						for(DtoValidacionNivelesAutorizacion dtoValidacionNivelAuto:listaValidacionNivelAuto){
							prioridades = new ArrayList<Integer>();
							IPrioridadOcupacionMedicaServicio servicioPrioOcupaMedicaServicio=CapitacionFabricaServicio.crearPrioridadOcupacionMedicaServicio();
							
							/* MT 1699
							 * No valida ocupación médica
							 * 
							 * Diana Ruiz
							 */
							prioridades=servicioPrioOcupaMedicaServicio.obtenerNumerosPrioridadOcupacionMedicaOrdenadosPorNivelAutorizacion(dtoValidacionNivelAuto.getCodigoNivelAutorizacionOcupacionMedica());													
							dtoValidacionNivelAuto.setPrioridades(prioridades);								
							nivelAutorizacion.add(dtoValidacionNivelAuto);												
						}	
					}							
			}
			
			
		//Se comentan estas validaciones ya que se cambio el DCU 1105 ya no aplica
		/*	 
		//1.2 NIVEL DE AUTORIZACION POR OCUPACION MEDICA
		if(nivelAutorizacion.size()<=0)
		{
			
			IMedicosServicio servicio = AdministracionFabricaServicio.crearMedicosServicio();
			DTONivelAutorizacionOcupacionMedica ocupacionMedica=servicio.obtenerOcupacionMedicaDeUsuarioProfesional(dto.getLoginUsuario());
			
			//NIVELES DE AUTORIZACION POR OCUPACION MEDICA
			if(ocupacionMedica==null){
				IUsuariosServicio servicioUsuarios=AdministracionFabricaServicio.crearUsuariosServicio();
				DtoUsuarioPersona dtoUsuPersona=servicioUsuarios.obtenerDtoUsuarioPersona(dto.getLoginUsuario());
				
				errores.add("No es Profesional", new ActionMessage("errors.notEspecific", 
						fuenteMensaje.getMessage("ProcesoNivelAutorizacionManualForm.noTieneNivelAutorizacion",
								dtoUsuPersona.getNombre()+" "+dtoUsuPersona.getApellido())));
				
			}else{
			
				servicioNivelAuto=CapitacionFabricaServicio.crearNivelAutorizacionServicio();
				listaValidacionNivelAuto=servicioNivelAuto.buscarNivelAutorizacionPorOcupacionMedica(ocupacionMedica.getOcupacionMedicaID());
				
				if(Utilidades.isEmpty(listaValidacionNivelAuto)){
					IUsuariosServicio servicioUsuarios=AdministracionFabricaServicio.crearUsuariosServicio();
					DtoUsuarioPersona dtoUsuPersona=servicioUsuarios.obtenerDtoUsuarioPersona(dto.getLoginUsuario());
					errores.add("No tiene nivel autorizacion", new ActionMessage("errors.notEspecific", 
							fuenteMensaje.getMessage("ProcesoNivelAutorizacionManualForm.noTieneNivelAutorizacion",
									dtoUsuPersona.getNombre()+" "+dtoUsuPersona.getApellido())));
				}else{
					for(DtoValidacionNivelesAutorizacion dtoValidacionNivelAuto:listaValidacionNivelAuto){
						prioridades = new ArrayList<Integer>();
						IPrioridadOcupacionMedicaServicio servicioPrioOcupaMedicaServicio=CapitacionFabricaServicio.crearPrioridadOcupacionMedicaServicio();
							
						/* MT 1699
						 * No valida ocupación médica
						 * 
						 * Diana Ruiz
						 */
						/*prioridades=servicioPrioOcupaMedicaServicio.obtenerNumerosPrioridadOcupacionMedicaOrdenadosPorNivelAutorizacion(dtoValidacionNivelAuto.getCodigoNivelAutorizacionOcupacionMedica());		
												
						dtoValidacionNivelAuto.setPrioridades(prioridades);
							
						nivelAutorizacion.add(dtoValidacionNivelAuto);
					}
				}
			}
			
		}*/
		
		
		//2. BUSCAR NIVELES DE AUTORIZACION MANUALES Y ACTIVOS, PARA LA VIA DE INGRESO DEL PACIENTE
		ArrayList<Integer> eliminar=new ArrayList<Integer>();
		if(!Utilidades.isEmpty(nivelAutorizacion))
		{			
			for(int i=0;i<nivelAutorizacion.size();i++)
			{
				DtoValidacionNivelesAutorizacion dtoTempo=(DtoValidacionNivelesAutorizacion)nivelAutorizacion.get(i);
				if(!dtoTempo.isActivo()||dto.getViaIngreso()!=dtoTempo.getViaIngreso())
				{
					eliminar.add(i);
				}
			}
			for(int i=eliminar.size()-1;i>=0;i--)
			{
				nivelAutorizacion.remove(eliminar.get(i).intValue());
			}
			
			if(Utilidades.isEmpty(nivelAutorizacion)){
				
				IUsuariosServicio servicioUsuarios=AdministracionFabricaServicio.crearUsuariosServicio();
				DtoUsuarioPersona dtoUsuPersona=servicioUsuarios.obtenerDtoUsuarioPersona(dto.getLoginUsuario());
				errores.add("No tiene nivel autorizacion activo o por via ingreso paciente", new ActionMessage("errors.notEspecific", 
						fuenteMensaje.getMessage("ProcesoNivelAutorizacionManualForm.noTieneNivelAutorizacionActivoViaIngreso",
								dtoUsuPersona.getNombre()+" "+dtoUsuPersona.getApellido())));
				
			}else{
				
				eliminar=new ArrayList<Integer>();
				for(int i=0;i<nivelAutorizacion.size();i++)
				{
					DtoValidacionNivelesAutorizacion dtoTempo=(DtoValidacionNivelesAutorizacion)nivelAutorizacion.get(i);
					if(!dtoTempo.getTipoAutorizacion().equals(ConstantesIntegridadDominio.acronimoTipoAutorizacionManual))
					{
						eliminar.add(i);
					}
				}
				for(int i=eliminar.size()-1;i>=0;i--)
				{
					nivelAutorizacion.remove(eliminar.get(i).intValue());
				}
				
				if(Utilidades.isEmpty(nivelAutorizacion)){
					IUsuariosServicio servicioUsuarios=AdministracionFabricaServicio.crearUsuariosServicio();
					DtoUsuarioPersona dtoUsuPersona=servicioUsuarios.obtenerDtoUsuarioPersona(dto.getLoginUsuario());
					
					errores.add("No tiene nivel autorizacion manual", new ActionMessage("errors.notEspecific", 
							fuenteMensaje.getMessage("ProcesoNivelAutorizacionManualForm.noTieneNivelAutorizacionManual",
									dtoUsuPersona.getNombre()+" "+dtoUsuPersona.getApellido())));
				}
			}
		}
		
		
		//3. NIVELES DE AUTORIZACION PARA SERVICIOS MEDICAMENTOS A AUTORIZAR
				
		//3.1 VERIFICACION DE SERVICIOS
		if(!Utilidades.isEmpty(nivelAutorizacion)){
			if(dto.getCodigoServicio()>0)
			{
				eliminar=new ArrayList<Integer>();
				for(int i=0;i<nivelAutorizacion.size();i++){
					
					//SERVICIOS ESPECIFICOS
					INivelAutorServMedicServicio nivelAutorServicio=CapitacionFabricaServicio.crearNivelAutorServMedicServicio();
					ArrayList<DTONivelAutorServMedic> listaPorServicioEspecifico=nivelAutorServicio.buscarNivelAutorizacionPorServicioEspecifico(nivelAutorizacion.get(i).getCodigoNivelAutorizacion(), dto.getCodigoServicio());
					
					//AGRUPACION DE SERVICIOS
					if(Utilidades.isEmpty(listaPorServicioEspecifico)){
						IServiciosServicio serviciosServicio=FacturacionServicioFabrica.crearServiciosServicio();
						DtoServicios serv=serviciosServicio.obtenerTipoEspecialidadGrupoServicioPorID(dto.getCodigoServicio());
						
						ArrayList<DTONivelAutorServMedic> listaPorAgrupacionServicios=nivelAutorServicio.buscarNivelAutorizacionPorAgrupacionServicios(serv, nivelAutorizacion.get(i).getCodigoNivelAutorizacion());
						
						boolean valida=false;
						int contadorValidaciones=0;
						for(DTONivelAutorServMedic listaAgrServ:listaPorAgrupacionServicios){
							
							contadorValidaciones=0;
							if(listaAgrServ.getCodigoEspecialidadServicio()!=null){
								if(listaAgrServ.getCodigoEspecialidadServicio().intValue()==serv.getCodigoEspecialidad()){
									contadorValidaciones++;
								}
							}else{
								contadorValidaciones++;
							}
							if(!UtilidadTexto.isEmpty(listaAgrServ.getAcronimoTipoServicio())){
								if(listaAgrServ.getAcronimoTipoServicio().equals(serv.getAcronimoTipoServicio())){
									contadorValidaciones++;
								}
							}else{
								contadorValidaciones++;
							}
							if(listaAgrServ.getCodigoGrupoServicio()!=null){
								if(listaAgrServ.getCodigoGrupoServicio().intValue()==serv.getCodigoGrupoServicio()){
									contadorValidaciones++;
								}
							}else{
								contadorValidaciones++;
							}
							
							if(contadorValidaciones==3){
								valida=true;
							}
							
						}
						
						if(!valida)
							eliminar.add(i);
						
					}
				}
				for(int i=eliminar.size()-1;i>=0;i--)
				{
					nivelAutorizacion.remove(eliminar.get(i).intValue());
				}
				
				if(Utilidades.isEmpty(nivelAutorizacion)){
					IUsuariosServicio servicioUsuarios=AdministracionFabricaServicio.crearUsuariosServicio();
					DtoUsuarioPersona dtoUsuPersona=servicioUsuarios.obtenerDtoUsuarioPersona(dto.getLoginUsuario());
					errores.add("No tiene nivel autorizacion por servicios", new ActionMessage("errors.notEspecific", 
							fuenteMensaje.getMessage("ProcesoNivelAutorizacionManualForm.noTieneNivelAutorizacionPorServicioArticulo",
									dtoUsuPersona.getNombre()+" "+dtoUsuPersona.getApellido())));
				}
				
			}
			else if(dto.getCodigoArticulo()>0)
			{
				eliminar=new ArrayList<Integer>();
				for(int i=0;i<nivelAutorizacion.size();i++){
					
					//POR ARTICULO ESPECIFICO
					INivelAutorServMedicServicio nivelAutorServicio=CapitacionFabricaServicio.crearNivelAutorServMedicServicio();
					ArrayList<DTONivelAutorServMedic> listaPorArticuloEspecifico=nivelAutorServicio.buscarNivelAutorizacionPorArticuloEspecifico(nivelAutorizacion.get(i).getCodigoNivelAutorizacion(), dto.getCodigoArticulo());
					
					//AGRUPACION DE ARTICULOS
					if(Utilidades.isEmpty(listaPorArticuloEspecifico)){
						IArticuloDAO articuloDAO = InventarioDAOFabrica.crearArticuloDAO();
						Articulo articuloAutorizado = articuloDAO.obtenerArticuloPorId(dto.getCodigoArticulo());
						
						IGrupoInventarioServicio grupoServicio = InventarioServicioFabrica.crearGrupoInventarioServicio();
						GrupoInventario grupoArticulo=grupoServicio.buscarGrupoInventarioPorSubgrupo(articuloAutorizado.getSubgrupo());
											
						ArrayList<DTONivelAutorServMedic> listaPorAgrupacionArticulos=nivelAutorServicio.buscarNivelAutorizacionPorAgrupacionMedicamentos(articuloAutorizado, nivelAutorizacion.get(i).getCodigoNivelAutorizacion(), grupoArticulo);
						
						boolean valida=false;
						int contadorValidaciones=0;
						for(DTONivelAutorServMedic listaAgrArt:listaPorAgrupacionArticulos){
							
							contadorValidaciones=0;
							if(listaAgrArt.getSubgrupoArticulo()!=null){
								if(listaAgrArt.getSubgrupoArticulo().intValue()==articuloAutorizado.getSubgrupo()){
									contadorValidaciones++;
								}
							}else{
								contadorValidaciones++;
							}
							if(listaAgrArt.getClaseGrupoIdArticulo()!=null&&listaAgrArt.getClaseGrupoIdArticulo()!=null){
								if(listaAgrArt.getClaseGrupoIdArticulo().intValue()==grupoArticulo.getId().getCodigo()
										&&listaAgrArt.getClaseGrupoIdArticulo().intValue()==grupoArticulo.getId().getClase()){
									contadorValidaciones++;
								}
							}else{
								contadorValidaciones++;
							}
							if(listaAgrArt.getClaseArticulo()!=null){
								if(listaAgrArt.getClaseArticulo().intValue()==grupoArticulo.getId().getClase()){
									contadorValidaciones++;
								}
							}else{
								contadorValidaciones++;
							}
							if(!UtilidadTexto.isEmpty(listaAgrArt.getAcronimoNaturalezaId())&&listaAgrArt.getInstitucionNaturalezaId()!=null){
								if(listaAgrArt.getAcronimoNaturalezaId().equals(articuloAutorizado.getNaturalezaArticulo().getId().getAcronimo())
										&&listaAgrArt.getInstitucionNaturalezaId()==articuloAutorizado.getNaturalezaArticulo().getId().getInstitucion()){
									contadorValidaciones++;
								}
							}else{
								contadorValidaciones++;
							}
							
							if(contadorValidaciones==4){
								valida=true;
							}
							
						}
						
						if(!valida)
							eliminar.add(i);
						
						
					}
					
				}
				
				for(int i=eliminar.size()-1;i>=0;i--)
				{
					nivelAutorizacion.remove(eliminar.get(i).intValue());
				}
				
				if(Utilidades.isEmpty(nivelAutorizacion)){
					IUsuariosServicio servicioUsuarios=AdministracionFabricaServicio.crearUsuariosServicio();
					DtoUsuarioPersona dtoUsuPersona=servicioUsuarios.obtenerDtoUsuarioPersona(dto.getLoginUsuario());
					errores.add("No tiene nivel autorizacion por articulos", new ActionMessage("errors.notEspecific", 
							fuenteMensaje.getMessage("ProcesoNivelAutorizacionManualForm.noTieneNivelAutorizacionPorServicioArticulo",
									dtoUsuPersona.getNombre()+" "+dtoUsuPersona.getApellido())));
				}
			}
		}
		
		
		//4. BUSCAR Y VALIDAR PRIORIDAD ENTIDAD SUBCONTRATADA AL INGRESAR A LA FUNCIONALIDAD
		if(dto.isBuscarValidarPrioridadEntidadSubcontratadaIngresarFuncionalidad())
		{
			IEntidadesSubcontratadasServicio servicioEntidadesSub 	= FacturacionServicioFabrica.crearEntidadesSubcontratadasServicio();
			ICentroAtencionMundo centroAtencionMundo 				= AdministracionFabricaMundo.crearCentroAtencionMundo();
			ICentroCostosServicio servicioCentroCosto 				= AdministracionFabricaServicio.crearCentroCostosServicio();
			
			ArrayList<DtoCentroCosto> listaCentrosCosto = new ArrayList<DtoCentroCosto>();
			
			if(!Utilidades.isEmpty(nivelAutorizacion))
			{
				if(UtilidadTexto.isEmpty(dto.getTipoOrden()) || dto.getTipoOrden().equals(ConstantesIntegridadDominio.acronimoTipoOrdenMedica))
				{
					// a) Centro de Costo para Ordenes Médicas.
					CentrosCosto centroCosto = servicioCentroCosto.findById(dto.getCentroCostoEjecuta());
					
					DtoCentroCosto centroCostoNivelEntidadSub = new DtoCentroCosto();
					centroCostoNivelEntidadSub.setCodigoCentroCosto(centroCosto.getCodigo());
					centroCostoNivelEntidadSub.setNombre(centroCosto.getNombre());
					
					
					/* RQF-02-0025  Versión 1.2 */
					/* si es Ambos se puede hacer de cualquier modo (Interna o Externa) da igual. En este caso se decide tomar el flujo de Externo */
					
					if(centroCosto.getTipoEntidadEjecuta().equals(ConstantesIntegridadDominio.acronimoAmbos) 
							|| centroCosto.getTipoEntidadEjecuta().equals(ConstantesIntegridadDominio.acronimoExterna)) 
					{
						ArrayList<DtoEntidadSubcontratada> listaEntidadesSubCentroCostoEjecuta = servicioEntidadesSub.listarEntidadesSubXCentroCostoActivo(dto.getCentroCostoEjecuta());
						
						if(!Utilidades.isEmpty(listaEntidadesSubCentroCostoEjecuta))
						{
							for (DtoEntidadSubcontratada dtoEntidadSubcontratada : listaEntidadesSubCentroCostoEjecuta) 
							{
								for (DtoValidacionNivelesAutorizacion dtoValidacionNivelesAutorizacion : nivelAutorizacion) 
								{
									for (Integer prioridadNivel : dtoValidacionNivelesAutorizacion.getPrioridades()) 
									{
										if(prioridadNivel.equals(dtoEntidadSubcontratada.getNroPrioridad()))
										{
											centroCostoNivelEntidadSub.getListaEntidadesSubcontratadas().add(dtoEntidadSubcontratada);
										}
									}
								}
							}
							listaCentrosCosto.add(centroCostoNivelEntidadSub);
						}
						
					}
					else if(centroCosto.getTipoEntidadEjecuta().equals(ConstantesIntegridadDominio.acronimoInterna))
					{
						String descripcionEntidad = ValoresPorDefecto.getEntidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada(dto.getInstitucion());
						Integer prioridadEntidad = Utilidades.convertirAEntero(ValoresPorDefecto.getPrioridadEntidadSubcontratada(dto.getInstitucion()));

						if(!UtilidadTexto.isEmpty(descripcionEntidad))
						{
							
							DtoEntidadSubcontratada dtoEntidadSubcontratadaParametro = new DtoEntidadSubcontratada();
							String[] entidad = descripcionEntidad.split("-");
							EntidadesSubcontratadasDelegate entiDao = new EntidadesSubcontratadasDelegate();
							
							boolean encontroPrioridadNivelParaEntidad = false;
							
							for (DtoValidacionNivelesAutorizacion dtoValidacionNivelesAutorizacion : nivelAutorizacion) 
							{
								if(encontroPrioridadNivelParaEntidad){ break; } 	/*Rendimiento*/
								
								for (Integer prioridadNivel : dtoValidacionNivelesAutorizacion.getPrioridades()) 
								{
									if(encontroPrioridadNivelParaEntidad){ break; } /*Rendimiento*/
									
									if(prioridadNivel.equals(prioridadEntidad))
									{
										encontroPrioridadNivelParaEntidad = true;
									}
								}
							}
							
							if(encontroPrioridadNivelParaEntidad)
							{
								EntidadesSubcontratadas entidadesSubcontratadasParametro = entiDao.findById(Long.parseLong(entidad[0].trim()));
								
								dtoEntidadSubcontratadaParametro.setCodigoPk(entidadesSubcontratadasParametro.getCodigoPk());
								dtoEntidadSubcontratadaParametro.setRazonSocial(entidadesSubcontratadasParametro.getRazonSocial());
								dtoEntidadSubcontratadaParametro.setDireccion(entidadesSubcontratadasParametro.getDireccion());
								dtoEntidadSubcontratadaParametro.setTelefono(entidadesSubcontratadasParametro.getTelefono());
								dtoEntidadSubcontratadaParametro.setNroPrioridad(prioridadEntidad);
								
								centroCostoNivelEntidadSub.getListaEntidadesSubcontratadas().add(dtoEntidadSubcontratadaParametro);
								listaCentrosCosto.add(centroCostoNivelEntidadSub);
							}
						}
					}
				}
				else 
				{
					//b) Por Prioridades de Entidad Subcontratada para Ordenes Ambulatorias y Peticiones.
					if(dto.getCodigoServicio() != ConstantesBD.codigoNuncaValido)
					{
						// Servicios
						ICentroCostoMundo centroCostoMundo 		= AdministracionFabricaMundo.crearCentroCostoMundo(); 
						IServiciosServicio serviciosServicio 	= FacturacionServicioFabrica.crearServiciosServicio();
						
						DtoServicios serv = serviciosServicio.obtenerTipoEspecialidadGrupoServicioPorID(dto.getCodigoServicio());
						
						ArrayList<DtoCentroCosto> listaCentroscostoGrupoServicio = new ArrayList<DtoCentroCosto>();
						listaCentroscostoGrupoServicio = centroCostoMundo.obtenerCentrosCostoPorGrupoServicio(serv.getCodigoGrupoServicio());
						
						CentrosCosto centroCosto;
						
						for (DtoCentroCosto dtoCentroCostoGruposervicio : listaCentroscostoGrupoServicio) 
						{
							centroCosto = new CentrosCosto();
							centroCosto = servicioCentroCosto.findById(dtoCentroCostoGruposervicio.getCodigoCentroCosto());
							
							/* RQF-02-0025  Versión 1.2 */
							/* si es Ambos se puede hacer de cualquier modo (Interna o Externa) da igual. En este caso se decide tomar el flujo de Externo */
							
							if(centroCosto.getTipoEntidadEjecuta().equals(ConstantesIntegridadDominio.acronimoAmbos) 
									|| centroCosto.getTipoEntidadEjecuta().equals(ConstantesIntegridadDominio.acronimoExterna)) 
							{
								ArrayList<DtoEntidadSubcontratada> listaEntidadesSubCentroCostoGruposerv = servicioEntidadesSub.listarEntidadesSubXCentroCostoActivo(dtoCentroCostoGruposervicio.getCodigoCentroCosto());
								
								if(!Utilidades.isEmpty(listaEntidadesSubCentroCostoGruposerv))
								{
									for (DtoEntidadSubcontratada dtoEntidadSubcontratada : listaEntidadesSubCentroCostoGruposerv) 
									{
										for (DtoValidacionNivelesAutorizacion dtoValidacionNivelesAutorizacion : nivelAutorizacion) 
										{
											for (Integer prioridadNivel : dtoValidacionNivelesAutorizacion.getPrioridades()) 
											{
												if(prioridadNivel.equals(dtoEntidadSubcontratada.getNroPrioridad()))
												{
													dtoCentroCostoGruposervicio.getListaEntidadesSubcontratadas().add(dtoEntidadSubcontratada);
												}
											}
										}
									}
								}
								
								listaCentrosCosto.add(dtoCentroCostoGruposervicio);
							}
							else if(centroCosto.getTipoEntidadEjecuta().equals(ConstantesIntegridadDominio.acronimoInterna)) 
							{
								String descripcionEntidad = ValoresPorDefecto.getEntidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada(dto.getInstitucion());
								Integer prioridadEntidad = Utilidades.convertirAEntero(ValoresPorDefecto.getPrioridadEntidadSubcontratada(dto.getInstitucion()));

								if(!UtilidadTexto.isEmpty(descripcionEntidad))
								{
									String[] entidad = descripcionEntidad.split("-");
									EntidadesSubcontratadasDelegate entiDao = new EntidadesSubcontratadasDelegate();
									
									boolean encontroPrioridadNivelParaEntidad = false;
								
									for (DtoValidacionNivelesAutorizacion dtoValidacionNivelesAutorizacion : nivelAutorizacion) 
									{
										if(encontroPrioridadNivelParaEntidad){ break; } 	/*Rendimiento*/
										
										for (Integer prioridadNivel : dtoValidacionNivelesAutorizacion.getPrioridades()) 
										{
											if(encontroPrioridadNivelParaEntidad){ break; } 	/*Rendimiento*/
											
											if(prioridadNivel.equals(prioridadEntidad))
											{
												encontroPrioridadNivelParaEntidad = true;
											}
										}
									}
									
									if(encontroPrioridadNivelParaEntidad)
									{
										EntidadesSubcontratadas entidadesSubcontratadasParametro = entiDao.findById(Long.parseLong(entidad[0].trim()));
										DtoEntidadSubcontratada dtoEntidadSubcontratadaParametro = new DtoEntidadSubcontratada();

										dtoEntidadSubcontratadaParametro.setCodigoPk(entidadesSubcontratadasParametro.getCodigoPk());
										dtoEntidadSubcontratadaParametro.setRazonSocial(entidadesSubcontratadasParametro.getRazonSocial());
										dtoEntidadSubcontratadaParametro.setDireccion(entidadesSubcontratadasParametro.getDireccion());
										dtoEntidadSubcontratadaParametro.setTelefono(entidadesSubcontratadasParametro.getTelefono());
										dtoEntidadSubcontratadaParametro.setNroPrioridad(prioridadEntidad);
										
										dtoCentroCostoGruposervicio.getListaEntidadesSubcontratadas().add(dtoEntidadSubcontratadaParametro);
										listaCentrosCosto.add(dtoCentroCostoGruposervicio);
									}	
								}
							}
						}
					}
					else if(dto.getCodigoArticulo() != ConstantesBD.codigoNuncaValido)
					{
						// Articulos
						if(dto.getCentroAtencionAsignado() != null)
						{
							//CentroAtencion centroAtencionIngreso = centroAtencionMundo.findById(dto.getCentroAtencion());
							CentroAtencion centroAtencionAsignado = centroAtencionMundo.findById(dto.getCentroAtencionAsignado()); 
							
							DtoEntidadSubcontratada entidadesSubFarmacia = new DtoEntidadSubcontratada();
							if(centroAtencionAsignado.getEntidadesSubcontratadas() != null){
								entidadesSubFarmacia = servicioEntidadesSub.listarEntidadesSubXId(centroAtencionAsignado.getEntidadesSubcontratadas().getCodigoPk());
							}
							
							ArrayList<DtoCentroCostosVista> listaCentrosCostoEntidad = new ArrayList<DtoCentroCostosVista>();
							
							if(entidadesSubFarmacia.getCodigoPk() != ConstantesBD.codigoNuncaValidoLong)
							{
								/**
								 * Se realiza la validacion del DCU 1105 version cambio 1.2
								 * MT 1819
								 * Diana Ruiz 
								 */		
								
								boolean farmaciasPorCentroCosto=false;
								String entSubParametro = ValoresPorDefecto.getEntidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada(codigoInstitucion);
								Integer prioridadentSubParametro = Utilidades.convertirAEntero(ValoresPorDefecto.getPrioridadEntidadSubcontratada(codigoInstitucion)); 
								
								
								if(!UtilidadTexto.isEmpty(entSubParametro)){									
									long codigoEntSubParametro = Long.parseLong(entSubParametro.split("-")[0]);
									
									EntidadesSubcontratadasDelegate entiDao = new EntidadesSubcontratadasDelegate();
									EntidadesSubcontratadas entidadesSubcontratadasParametro = entiDao.findById(codigoEntSubParametro);
									DtoEntidadSubcontratada dtoEntidadSubcontratadaParametro = new DtoEntidadSubcontratada();
									
									dtoEntidadSubcontratadaParametro.setCodigoPk(entidadesSubcontratadasParametro.getCodigoPk());
									dtoEntidadSubcontratadaParametro.setRazonSocial(entidadesSubcontratadasParametro.getRazonSocial());
									dtoEntidadSubcontratadaParametro.setDireccion(entidadesSubcontratadasParametro.getDireccion());
									dtoEntidadSubcontratadaParametro.setTelefono(entidadesSubcontratadasParametro.getTelefono());
									dtoEntidadSubcontratadaParametro.setNroPrioridad(prioridadentSubParametro);
									
									/**Si el parametro esta definido se verifica que la entidad subcontratada del parametro sea la misma de la entidad
									 * de farmacia del centro de atencion asignado al paciente 
									 */									
									if (entidadesSubFarmacia.getCodigoPk()== codigoEntSubParametro){	
										
										farmaciasPorCentroCosto=true;
										boolean manejaPrioridad=false;
										ICentroCostosDAO centroCostoDAO = new CentroCostosDAO();										
										ArrayList<DtoCentroCosto> listaCentroCostoSubAlmacenXCentroAtencion = new ArrayList<DtoCentroCosto>();
										
										/**
										 * Se debe buscar en la funcionalidad Farmacias por Centro de Costo para los Centros 
										 * de Costo asociados al Centro de Atencion Asignado verificando que tenga una farmacia 
										 * parametrizada
										 */			
										
										listaCentroCostoSubAlmacenXCentroAtencion = centroCostoDAO.listaCentroCostoSubAlmacenXCentroAtencion(dto.getCentroAtencionAsignado());
																			
										if (listaCentroCostoSubAlmacenXCentroAtencion != null && !UtilidadTexto.isEmpty(prioridadentSubParametro)){
																					
											for (DtoValidacionNivelesAutorizacion dtoValidacionNivelesAutorizacion : nivelAutorizacion) 
											{
												/**
												 * Valido si maneja la prioridad
												 */
												for (Integer prioridadNivel : dtoValidacionNivelesAutorizacion.getPrioridades()) 
												{
													if(prioridadNivel.equals(prioridadentSubParametro))
														manejaPrioridad=true;
												 break;
												}
											}
											
											
											if (manejaPrioridad==true){
												
												for (DtoCentroCosto listaCentrosCostoFarmacia : listaCentroCostoSubAlmacenXCentroAtencion){
													
													DtoCentroCosto centroCostoEntidadSub; centroCostoEntidadSub = new DtoCentroCosto();
													centroCostoEntidadSub.setCodigoCentroCosto(listaCentrosCostoFarmacia.getCodigoCentroCosto());
													centroCostoEntidadSub.setNombre(listaCentrosCostoFarmacia.getNombre());
													centroCostoEntidadSub.getListaEntidadesSubcontratadas().add(dtoEntidadSubcontratadaParametro);
													listaCentrosCosto.add(centroCostoEntidadSub);													
												}											
											}									
										}		
									}							
								}
								
									if (farmaciasPorCentroCosto==false){
																	
								listaCentrosCostoEntidad = (ArrayList<DtoCentroCostosVista>) servicioCentroCosto.listaCentroCostoActivoXrEntidadesSub(entidadesSubFarmacia.getCodigoPk());
								
								for (DtoCentroCostosVista dtoCentroCostosVista : listaCentrosCostoEntidad) 
								{
									CentrosCosto centroCosto = new CentrosCosto();
									centroCosto = servicioCentroCosto.findById(dtoCentroCostosVista.getCodigo());
									
											/**
											 * Del listado de la lista listaCentrosCostoEntidad, se toman solamente los centros de costo identificados
											 * como Sub Almacen DCU 1105 version cambio 1.2								
											 */
											
											if (dtoCentroCostosVista.getTipoArea() == ConstantesBD.codigoTipoAreaSubalmacen){												
												
												for (DtoValidacionNivelesAutorizacion dtoValidacionNivelesAutorizacion : nivelAutorizacion) 
												{
													for (Integer prioridadNivel : dtoValidacionNivelesAutorizacion.getPrioridades()) 
													{
														if(prioridadNivel.equals(entidadesSubFarmacia.getNroPrioridad()))
														{
															DtoCentroCosto centroCostoEntidadSub; centroCostoEntidadSub = new DtoCentroCosto();
															centroCostoEntidadSub.setCodigoCentroCosto(dtoCentroCostosVista.getCodigo());
															centroCostoEntidadSub.setNombre(dtoCentroCostosVista.getNombre());
															centroCostoEntidadSub.getListaEntidadesSubcontratadas().add(entidadesSubFarmacia);
															listaCentrosCosto.add(centroCostoEntidadSub);
														}
													}
												}										
											}							
										}
										
									}								
															
								/**
								 * Las siguientes validaciones ya no aplican por cambios en el DCU 1105 version 1.1 y 1.2
								 * Diana Ruiz 
								 */	
								
								/**listaCentrosCostoEntidad = (ArrayList<DtoCentroCostosVista>) servicioCentroCosto.listaCentroCostoActivoXrEntidadesSub(entidadesSubFarmacia.getCodigoPk());
								for (DtoCentroCostosVista dtoCentroCostosVista : listaCentrosCostoEntidad) 
								{
									CentrosCosto centroCosto = new CentrosCosto();
									centroCosto = servicioCentroCosto.findById(dtoCentroCostosVista.getCodigo());
									
									/* RQF-02-0025  Versión 1.2 */
									/* si es Ambos se puede hacer de cualquier modo (Interna o Externa) da igual. En este caso se decide tomar el flujo de Externo */
									
									/**if(centroCosto.getTipoEntidadEjecuta().equals(ConstantesIntegridadDominio.acronimoExterna)) 
									{
										for (DtoValidacionNivelesAutorizacion dtoValidacionNivelesAutorizacion : nivelAutorizacion) 
										{
											for (Integer prioridadNivel : dtoValidacionNivelesAutorizacion.getPrioridades()) 
											{
												if(prioridadNivel.equals(entidadesSubFarmacia.getNroPrioridad()))
												{
													DtoCentroCosto centroCostoEntidadSub; centroCostoEntidadSub = new DtoCentroCosto();
													centroCostoEntidadSub.setCodigoCentroCosto(dtoCentroCostosVista.getCodigo());
													centroCostoEntidadSub.setNombre(dtoCentroCostosVista.getNombre());
													centroCostoEntidadSub.getListaEntidadesSubcontratadas().add(entidadesSubFarmacia);
													listaCentrosCosto.add(centroCostoEntidadSub);
												}
											}
										}
									}
									else if(centroCosto.getTipoEntidadEjecuta().equals(ConstantesIntegridadDominio.acronimoInterna)) 
									{
										String descripcionEntidad = ValoresPorDefecto.getEntidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada(dto.getInstitucion());
										Integer prioridadEntidad = Utilidades.convertirAEntero(ValoresPorDefecto.getPrioridadEntidadSubcontratada(dto.getInstitucion()));

										if(!UtilidadTexto.isEmpty(descripcionEntidad))
										{
											String[] entidad = descripcionEntidad.split("-");
											EntidadesSubcontratadasDelegate entiDao = new EntidadesSubcontratadasDelegate();
											
											EntidadesSubcontratadas entidadesSubcontratadasParametro = entiDao.findById(Long.parseLong(entidad[0].trim()));
											DtoEntidadSubcontratada dtoEntidadSubcontratadaParametro = new DtoEntidadSubcontratada();

											dtoEntidadSubcontratadaParametro.setCodigoPk(entidadesSubcontratadasParametro.getCodigoPk());
											dtoEntidadSubcontratadaParametro.setRazonSocial(entidadesSubcontratadasParametro.getRazonSocial());
											dtoEntidadSubcontratadaParametro.setDireccion(entidadesSubcontratadasParametro.getDireccion());
											dtoEntidadSubcontratadaParametro.setTelefono(entidadesSubcontratadasParametro.getTelefono());
											dtoEntidadSubcontratadaParametro.setNroPrioridad(prioridadEntidad);
											
											for (DtoValidacionNivelesAutorizacion dtoValidacionNivelesAutorizacion : nivelAutorizacion) 
											{
												for (Integer prioridadNivel : dtoValidacionNivelesAutorizacion.getPrioridades()) 
												{
													if(prioridadNivel.equals(prioridadEntidad))
													{
														DtoCentroCosto centroCostoEntidadSub; centroCostoEntidadSub = new DtoCentroCosto();
														centroCostoEntidadSub.setCodigoCentroCosto(dtoCentroCostosVista.getCodigo());
														centroCostoEntidadSub.setNombre(dtoCentroCostosVista.getNombre());
														centroCostoEntidadSub.getListaEntidadesSubcontratadas().add(dtoEntidadSubcontratadaParametro);
														listaCentrosCosto.add(centroCostoEntidadSub);
													}
												}
											}
										}
									}*/
								//}
									}
								}
						else{
							Log4JManager.info("Via ingreso para validar nivel por privera vez: "+dto.getViaIngreso());
						}
					}
				}
			}
			
			validacion.setListaCentrosCosto(listaCentrosCosto);
		}
		
		else{
			//5. BUSCAR Y VALIDAR PRIORIDAD ENTIDAD SUBCONTRATADA
			if(!Utilidades.isEmpty(nivelAutorizacion))
			{
				DTONivelAutorizacion dtoNivelAutorizacion =null;
				
				//VALIDACION SI LA ENTIDAD SUBCONTRATADA ES OTRA NO HACE LA CUARTA VALIDACION
				if(dto.getCentroCostoEjecuta()==ConstantesBD.codigoNuncaValido 
						||dto.getCodigoEntidadSubcontratadaSeleccionada()==ConstantesBD.codigoNuncaValido)
				{
					
					validacion.setGenerarAutorizacion(true);														
					dtoNivelAutorizacion = new DTONivelAutorizacion();
					dtoNivelAutorizacion.setCodigoPk(nivelAutorizacion.get(0).getCodigoNivelAutorizacion());
					dtoNivelAutorizacion.setDescripcion(nivelAutorizacion.get(0).getDescripcionNivelAutorizacion());
					validacion.setDtoNivelAutorizacion(dtoNivelAutorizacion);
					
					EntidadesSubcontratadas entidadSub=new EntidadesSubcontratadas();
					EntidadesSubcontratadasDelegate entiDao=new EntidadesSubcontratadasDelegate();
					entidadSub=entiDao.findById(dto.getCodigoEntidadSubcontratadaSeleccionada());
					
					DtoEntidadSubcontratada dtoEntidad=new DtoEntidadSubcontratada();
					dtoEntidad.setCodigo(entidadSub.getCodigo());
					dtoEntidad.setCodigoMinsalud(entidadSub.getCodigoMinsalud());
					dtoEntidad.setCodigoPk(entidadSub.getCodigoPk());
					dtoEntidad.setRazonSocial(entidadSub.getRazonSocial());
					dtoEntidad.setDireccion(entidadSub.getDireccion());
					dtoEntidad.setTelefono(entidadSub.getTelefono());
					validacion.setDtoEntidadSubcontratada(dtoEntidad);
					
				
				}else
				{
					if(dto.getFuncionalidad().equals(ConstantesIntegridadDominio.acronimoFuncionalidadListadoOrdenesPorCentroCosto))
					{	
						//A) LLAMADO DESDE EL LISTADO ORDENES POR CENTRO COSTO
						if(dto.getTipoEntidadAutoriza().equals(ConstantesIntegridadDominio.acronimoExterna))
						{
							//busca las entidades subcontratadas asociadas al centro de costo que ejecuta
							IEntidadesSubcontratadasServicio servicioEntidadesSub=FacturacionServicioFabrica.crearEntidadesSubcontratadasServicio();
							ArrayList<DtoEntidadSubcontratada> listaEntidadesSub=servicioEntidadesSub.listarEntidadesSubXCentroCostoActivo(dto.getCentroCostoEjecuta());

							for(int ent=0;ent<listaEntidadesSub.size();ent++)
							{
								//entidad subcontratada seleccionada
								if(listaEntidadesSub.get(ent).getCodigoPk()==dto.getCodigoEntidadSubcontratadaSeleccionada())
								{
									EntidadesSubcontratadas entidadSub=new EntidadesSubcontratadas();
									EntidadesSubcontratadasDelegate entiDao=new EntidadesSubcontratadasDelegate();
									entidadSub=entiDao.findById(dto.getCodigoEntidadSubcontratadaSeleccionada());
								
									for(int i=0;i<nivelAutorizacion.size();i++)
									{
										for(int j=0;j<nivelAutorizacion.get(i).getPrioridades().size();j++)
										{
											if(nivelAutorizacion.get(i).getPrioridades().get(j).intValue()==listaEntidadesSub.get(ent).getNroPrioridad())
											{
												validacion.setGenerarAutorizacion(true);
												dtoNivelAutorizacion = new DTONivelAutorizacion();
												dtoNivelAutorizacion.setCodigoPk(nivelAutorizacion.get(i).getCodigoNivelAutorizacion());
												dtoNivelAutorizacion.setDescripcion(nivelAutorizacion.get(i).getDescripcionNivelAutorizacion());
														
												validacion.setDtoNivelAutorizacion(dtoNivelAutorizacion);
												DtoEntidadSubcontratada dtoEntidad=new DtoEntidadSubcontratada();
												dtoEntidad.setCodigo(entidadSub.getCodigo());
												dtoEntidad.setCodigoMinsalud(entidadSub.getCodigoMinsalud());
												dtoEntidad.setCodigoPk(entidadSub.getCodigoPk());
												dtoEntidad.setRazonSocial(entidadSub.getRazonSocial());
												dtoEntidad.setDireccion(entidadSub.getDireccion());
												dtoEntidad.setTelefono(entidadSub.getTelefono());
												validacion.setDtoEntidadSubcontratada(dtoEntidad);
												j=nivelAutorizacion.get(i).getPrioridades().size();
												ent=listaEntidadesSub.size();
												
											}
										}
										
										if(validacion.isGenerarAutorizacion()){
											i=nivelAutorizacion.size();
										}
									}
								}
							}
						}
						else
						{
							//Tomar la Prioridad definida en el parámetro Entidad Subcontratada Para Centros de Costo Internos 
							//en Autorización de Capitación Subcontratada (Funcionalidad Parámetros Generales Módulo Manejo del paciente).
							String descripcionEntidad=ValoresPorDefecto.getEntidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada(dto.getInstitucion());
							int prioridad=Utilidades.convertirAEntero(ValoresPorDefecto.getPrioridadEntidadSubcontratada(dto.getInstitucion()));

							if(!UtilidadTexto.isEmpty(descripcionEntidad))
							{
								String[] entidad=descripcionEntidad.split("-");
								EntidadesSubcontratadasDelegate entiDao=new EntidadesSubcontratadasDelegate();
								EntidadesSubcontratadas entidadSub=entiDao.findById(Long.parseLong(entidad[0].trim()));
																
								//ENTIDAD SUBCONTRATADA ACTIVA
								if(UtilidadTexto.getBoolean(entidadSub.getActivo()))
								{
									DtoContratoEntidadSub contrato = CargosEntidadesSubcontratadas.obtenerContratoVigenteEntidadSubcontratada(con, entidadSub.getCodigoPk()+"", UtilidadFecha.getFechaActual());
									int codigoContrato=Utilidades.convertirAEntero(contrato.getConsecutivo());
									
									//ENTIDAD SUBCONTRATADA CON CONTRATO VIGENTE
									if(codigoContrato>0)
									{				
										for(int i=0;i<nivelAutorizacion.size();i++)
										{
											for(int j=0;j<nivelAutorizacion.get(i).getPrioridades().size();j++)
											{
												if(nivelAutorizacion.get(i).getPrioridades().get(j).intValue()==prioridad)
												{
													validacion.setGenerarAutorizacion(true);														
													dtoNivelAutorizacion = new DTONivelAutorizacion();
													dtoNivelAutorizacion.setCodigoPk(nivelAutorizacion.get(i).getCodigoNivelAutorizacion());
													dtoNivelAutorizacion.setDescripcion(nivelAutorizacion.get(i).getDescripcionNivelAutorizacion());
															
													validacion.setDtoNivelAutorizacion(dtoNivelAutorizacion);
													DtoEntidadSubcontratada dtoEntidad=new DtoEntidadSubcontratada();
													dtoEntidad.setCodigo(entidadSub.getCodigo());
													dtoEntidad.setCodigoMinsalud(entidadSub.getCodigoMinsalud());
													dtoEntidad.setCodigoPk(entidadSub.getCodigoPk());
													dtoEntidad.setRazonSocial(entidadSub.getRazonSocial());
													dtoEntidad.setDireccion(entidadSub.getDireccion());
													dtoEntidad.setTelefono(entidadSub.getTelefono());
													validacion.setDtoEntidadSubcontratada(dtoEntidad);
													j=nivelAutorizacion.get(i).getPrioridades().size();
												}
											}
											
											if(validacion.isGenerarAutorizacion()){
												i=nivelAutorizacion.size();
											}
										}
									}
								}
							}
						}
					}
					
					
					else if(dto.getFuncionalidad().equals(ConstantesIntegridadDominio.acronimoFuncionalidadListadoOrdenesPorEntidadSubcontratada)){
						//B) LLAMADO DESDE EL LISTADO ORDENES POR ENTIDAD SUBCONTRATADA
						EntidadesSubcontratadas entidadSub=new EntidadesSubcontratadas();
						EntidadesSubcontratadasDelegate entiDao=new EntidadesSubcontratadasDelegate();
						entidadSub=entiDao.findById(dto.getCodigoEntidadSubcontratadaSeleccionada());
						
						ICentroCostosServicio servicioCentroCosto= AdministracionFabricaServicio.crearCentroCostosServicio();
						ArrayList<DtoCentroCostosVista> listaCentroCostoFiltro = (ArrayList<DtoCentroCostosVista>) servicioCentroCosto.listaCentroCostoActivoXrEntidadesSub(dto.getCodigoEntidadSubcontratadaSeleccionada());
						
						for(int cc=0;cc<listaCentroCostoFiltro.size();cc++)
						{
							for(int i=0;i<nivelAutorizacion.size();i++)
							{
								for(int j=0;j<nivelAutorizacion.get(i).getPrioridades().size();j++)
								{
									if(nivelAutorizacion.get(i).getPrioridades().get(j).intValue()==listaCentroCostoFiltro.get(cc).getNroPrioridad()){
										
										validacion.setGenerarAutorizacion(true);														
										dtoNivelAutorizacion = new DTONivelAutorizacion();
										dtoNivelAutorizacion.setCodigoPk(nivelAutorizacion.get(i).getCodigoNivelAutorizacion());
										dtoNivelAutorizacion.setDescripcion(nivelAutorizacion.get(i).getDescripcionNivelAutorizacion());
										validacion.setDtoNivelAutorizacion(dtoNivelAutorizacion);
										
										DtoEntidadSubcontratada dtoEntidad=new DtoEntidadSubcontratada();
										dtoEntidad.setCodigo(entidadSub.getCodigo());
										dtoEntidad.setCodigoMinsalud(entidadSub.getCodigoMinsalud());
										dtoEntidad.setCodigoPk(entidadSub.getCodigoPk());
										dtoEntidad.setRazonSocial(entidadSub.getRazonSocial());
										dtoEntidad.setDireccion(entidadSub.getDireccion());
										dtoEntidad.setTelefono(entidadSub.getTelefono());
										validacion.setDtoEntidadSubcontratada(dtoEntidad);
										j=nivelAutorizacion.get(i).getPrioridades().size();
										cc=listaCentroCostoFiltro.size();
									}
								}
								if(validacion.isGenerarAutorizacion()){
									i=nivelAutorizacion.size();
								}
							}
						}					
					} 

					
					else if(dto.getFuncionalidad().equals(ConstantesIntegridadDominio.acronimoFuncionalidadAutorizacionServiciosMedicamentosInsumosIngresoEstancia))
					{
						//C) AUTORIZACION SERVICIOS MEDICAMENTOS INSUMOS INGRESO ESTANCIA
						EntidadesSubcontratadas entidadSub=new EntidadesSubcontratadas();
						EntidadesSubcontratadasDelegate entiDao=new EntidadesSubcontratadasDelegate();
						entidadSub=entiDao.findById(dto.getCodigoEntidadSubcontratadaSeleccionada());
						
						if(entidadSub.getPermiteEstanciaPacientes().equals(ConstantesBD.acronimoSi))
						{
							validacion.setGenerarAutorizacion(true);														
							dtoNivelAutorizacion = new DTONivelAutorizacion();
							dtoNivelAutorizacion.setCodigoPk(nivelAutorizacion.get(0).getCodigoNivelAutorizacion());
							dtoNivelAutorizacion.setDescripcion(nivelAutorizacion.get(0).getDescripcionNivelAutorizacion());
							validacion.setDtoNivelAutorizacion(dtoNivelAutorizacion);
							
							DtoEntidadSubcontratada dtoEntidad=new DtoEntidadSubcontratada();
							dtoEntidad.setCodigo(entidadSub.getCodigo());
							dtoEntidad.setCodigoMinsalud(entidadSub.getCodigoMinsalud());
							dtoEntidad.setCodigoPk(entidadSub.getCodigoPk());
							dtoEntidad.setRazonSocial(entidadSub.getRazonSocial());
							dtoEntidad.setDireccion(entidadSub.getDireccion());
							dtoEntidad.setTelefono(entidadSub.getTelefono());
							validacion.setDtoEntidadSubcontratada(dtoEntidad);
						}
					}
				}
			}
		}
		
		validacion.setErroresValidacion(errores);
		Log4JManager.info("\n\n\nGenera autorizacion manual? "+validacion.isGenerarAutorizacion());
		UtilidadBD.closeConnection(con);
		
		return validacion;
	}

	
}
