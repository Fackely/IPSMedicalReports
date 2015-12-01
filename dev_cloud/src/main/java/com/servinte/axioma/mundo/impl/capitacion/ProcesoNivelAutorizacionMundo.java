package com.servinte.axioma.mundo.impl.capitacion;

import java.sql.Connection;
import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.InfoCobertura;

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
import com.princetonsa.mundo.cargos.CargosEntidadesSubcontratadas;
import com.princetonsa.mundo.facturacion.Cobertura;
import com.servinte.axioma.dao.fabrica.inventario.InventarioDAOFabrica;
import com.servinte.axioma.dao.interfaz.inventario.IArticuloDAO;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.mundo.interfaz.capitacion.IProcesoNivelAutorizacionMundo;
import com.servinte.axioma.orm.Articulo;
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
import com.servinte.axioma.servicio.interfaz.capitacion.INivelAutorServMedicServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.INivelAutorizacionServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.IPrioridadOcupacionMedicaServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.IPrioridadUsuarioEspecificoServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.IEntidadesSubcontratadasServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.IServiciosServicio;
import com.servinte.axioma.servicio.interfaz.inventario.IGrupoInventarioServicio;

public class ProcesoNivelAutorizacionMundo implements IProcesoNivelAutorizacionMundo{
	
	/**
	 * 
	 * Este Método realiza el proceso de validación para saber el Nivel de Autorización que aplica tanto al usuario 
	 * como a cada uno de los servicios / artículos de las ordenes a autorizar y la entidad subcontratada autorizada,
	 * para niveles de autorizacion de tipo automatico
	 * 
	 * @author Fabian Becerra
	 *
	 */
	public DTOValidacionNivelAutoAutomatica validarNivelAutorizacionParaAutorizacionAutomatica(DtoParametrosValidacionNivelAutorizacion dto) throws IPSException{
			
		DTOValidacionNivelAutoAutomatica validacion=new DTOValidacionNivelAutoAutomatica();
		Connection con=UtilidadBD.abrirConexion();
		
		ArrayList<DtoValidacionNivelesAutorizacion> nivelAutorizacion=new ArrayList<DtoValidacionNivelesAutorizacion>();
		
		IEntidadesSubcontratadasServicio servicioEntidadesSub = FacturacionServicioFabrica.crearEntidadesSubcontratadasServicio();
		
		//1. NIVELES DE AUTORIZACION POR USUARIO 
		
		//1.1 NIVEL DE AUTORIZACION DEL USUARIO ESPECIFICO
		INivelAutorizacionServicio servicioNivelAuto=CapitacionFabricaServicio.crearNivelAutorizacionServicio();
		ArrayList<DtoValidacionNivelesAutorizacion> listaValidacionNivelAuto=servicioNivelAuto.buscarNivelAutorizacionPorUsuario(dto.getLoginUsuario());
		
		//CARGAR LAS PRIORIDADES DEL NIVEL DE AUTORIZACION
		for(DtoValidacionNivelesAutorizacion dtoValidacionNivelAuto : listaValidacionNivelAuto){
			
			IPrioridadUsuarioEspecificoServicio servicioPrioUsuEspecServicio=CapitacionFabricaServicio.crearPrioridadUsuarioEspecificoServicio();
			ArrayList<Integer> prioridades=servicioPrioUsuEspecServicio.obtenerNumerosPrioridadOrdenadosPorNivelAutorizacion(dtoValidacionNivelAuto.getCodigoNivelAutorizacionUsuario());
			
			dtoValidacionNivelAuto.setPrioridades(prioridades);
			
			nivelAutorizacion.add(dtoValidacionNivelAuto);
		}
		
		//1.2 NIVEL DE AUTORIZACION POR OCUPACION MEDICA
		if(nivelAutorizacion.size()<=0)
		{
			
			IMedicosServicio servicio = AdministracionFabricaServicio.crearMedicosServicio();
			DTONivelAutorizacionOcupacionMedica ocupacionMedica=servicio.obtenerOcupacionMedicaDeUsuarioProfesional(dto.getLoginUsuario());
			
			//NIVELES DE AUTORIZACION POR OCUPACION MEDICA
			if(ocupacionMedica!=null){
				servicioNivelAuto=CapitacionFabricaServicio.crearNivelAutorizacionServicio();
				listaValidacionNivelAuto=servicioNivelAuto.buscarNivelAutorizacionPorOcupacionMedica(ocupacionMedica.getOcupacionMedicaID());
				
				for(DtoValidacionNivelesAutorizacion dtoValidacionNivelAuto:listaValidacionNivelAuto){
				
					IPrioridadOcupacionMedicaServicio servicioPrioOcupaMedicaServicio=CapitacionFabricaServicio.crearPrioridadOcupacionMedicaServicio();
					ArrayList<Integer> prioridades=servicioPrioOcupaMedicaServicio.obtenerNumerosPrioridadOcupacionMedicaOrdenadosPorNivelAutorizacion(dtoValidacionNivelAuto.getCodigoNivelAutorizacionOcupacionMedica());
						
					dtoValidacionNivelAuto.setPrioridades(prioridades);
						
					nivelAutorizacion.add(dtoValidacionNivelAuto);
				}
			}
			
			
			
		}
		
		
		//2. SELECCION DE NIVELES DE AUTORIZACION AUTOMATICAS
		ArrayList<Integer> eliminar=new ArrayList<Integer>();
		for(int i=0;i<nivelAutorizacion.size();i++)
		{
			DtoValidacionNivelesAutorizacion dtoTempo=(DtoValidacionNivelesAutorizacion)nivelAutorizacion.get(i);
			if(!dtoTempo.isActivo()||dto.getViaIngreso()!=dtoTempo.getViaIngreso()||!dtoTempo.getTipoAutorizacion().equals(ConstantesIntegridadDominio.acronimoTipoAutorizacionAuto))
			{
				eliminar.add(i);
			}
		}
		for(int i=eliminar.size()-1;i>=0;i--)
		{
			nivelAutorizacion.remove(eliminar.get(i).intValue());
		}
		
		
		//3. NIVELES DE AUTORIZACION PARA SERVICIOS, MEDICAMENTOS O INSUMOS A AUTORIZAR
		if(!Utilidades.isEmpty(nivelAutorizacion)){
			//3.1 VERIFICACION DE SERVICIOS
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
			}
		}
		
		//4. BUSCAR Y VALIDAR ENTIDAD SUBCONTRATADA
		if(!Utilidades.isEmpty(nivelAutorizacion)){
			
			DTONivelAutorizacion dtoNivelAutorizacion =null;
			ICentroCostosServicio servicioCentroCosto=AdministracionFabricaServicio.crearCentroCostosServicio();
			CentrosCosto centroCosto = servicioCentroCosto.findById(dto.getCentroCostoEjecuta());
			
			// Sin entidad subcontratada Cambio DCU 1115 - 1.1
			/*
			if(!dto.isTipoAmbulatoriaPeticionesOPyP() )
			{
			*/
			/**
			 * MT 3486, MT 3475
			 * Diana Ruiz
			 */
			
				if(dto.getCodigoEntidadSubcontratadaSeleccionada() == ConstantesBD.codigoNuncaValido)					
				{
					if(!UtilidadTexto.isEmpty(centroCosto.getTipoEntidadEjecuta()) && 
							centroCosto.getTipoEntidadEjecuta().equals(ConstantesIntegridadDominio.acronimoInterna))
					{
						String descripcionEntidad=ValoresPorDefecto.getEntidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada(dto.getInstitucion());
						
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
									int prioridad=Utilidades.convertirAEntero(ValoresPorDefecto.getPrioridadEntidadSubcontratada(dto.getInstitucion()));
									for(int i=0;i<nivelAutorizacion.size();i++)
									{
										for(int j=0;j<nivelAutorizacion.get(i).getPrioridades().size();j++)
										{
											if(nivelAutorizacion.get(i).getPrioridades().get(j).intValue()==prioridad)
											{
												if(dto.getCodigoServicio()>0)
												{
													InfoCobertura info=Cobertura.validacionCoberturaServicioEntidadSub(con,codigoContrato, dto.getViaIngreso(), dto.getTipoPaciente(), dto.getCodigoServicio(), dto.getNaturalezaPAciente(), dto.getInstitucion());
													if(info.incluido())
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
												else if(dto.getCodigoArticulo()>0)
												{
													InfoCobertura info=Cobertura.validacionCoberturaArticuloEntidadSub(con,codigoContrato, dto.getViaIngreso(), dto.getTipoPaciente(), dto.getCodigoArticulo(), dto.getNaturalezaPAciente(), dto.getInstitucion());
													if(info.incluido())
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
					else
						{
							ArrayList<DtoEntidadSubcontratada> listaEntidadesSub=servicioEntidadesSub.listarEntidadesSubXCentroCostoActivoContratoVigente(dto.getCentroCostoEjecuta());
							
							for(int ent=0;ent<listaEntidadesSub.size();ent++)
							{
								long entidad=listaEntidadesSub.get(ent).getCodigoPk();
								EntidadesSubcontratadas entidadSub=new EntidadesSubcontratadas();
								EntidadesSubcontratadasDelegate entiDao=new EntidadesSubcontratadasDelegate();
								entidadSub=entiDao.findById(entidad);
								DtoContratoEntidadSub contrato = CargosEntidadesSubcontratadas.obtenerContratoVigenteEntidadSubcontratada(con, entidadSub.getCodigoPk()+"", UtilidadFecha.getFechaActual());
									int codigoContrato=Utilidades.convertirAEntero(contrato.getConsecutivo());
									if(codigoContrato>0)
									{
										for(int i=0;i<nivelAutorizacion.size();i++)
										{
											for(int j=0;j<nivelAutorizacion.get(i).getPrioridades().size();j++)
											{
												if(nivelAutorizacion.get(i).getPrioridades().get(j).intValue()==listaEntidadesSub.get(ent).getNroPrioridad())
												{
													if(dto.getCodigoServicio()>0)
													{
														InfoCobertura info=Cobertura.validacionCoberturaServicioEntidadSub(con,codigoContrato, dto.getViaIngreso(), dto.getTipoPaciente(), dto.getCodigoServicio(), dto.getNaturalezaPAciente(), dto.getInstitucion());
														if(info.incluido())
														{
															//GUARDA EL NIVEL DE AUTORIZACION Y LA ENTIDAD
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
															//TERMINA EL PROCESO AL ENCONTRAR QUE EL SERVICIO TIENE COBERTURA
															j=nivelAutorizacion.get(i).getPrioridades().size();
															ent=listaEntidadesSub.size();
														}
													}
													else if(dto.getCodigoArticulo()>0)
													{
														InfoCobertura info=Cobertura.validacionCoberturaArticuloEntidadSub(con,codigoContrato, dto.getViaIngreso(), dto.getTipoPaciente(), dto.getCodigoArticulo(), dto.getNaturalezaPAciente(), dto.getInstitucion());
														if(info.incluido())
														{
															//GUARDA EL NIVEL DE AUTORIZACION Y LA ENTIDAD
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
															//TERMINA EL PROCESO AL ENCONTRAR QUE EL ARTICULO TIENE COBERTURA
															j=nivelAutorizacion.get(i).getPrioridades().size();
															ent=listaEntidadesSub.size();
														}
													}
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
			//}
			
			// Con entidad subcontratada. Cambio DCU 1115 - 1.1
			else{
				
				ArrayList<DtoCentroCostosVista> listaCentroCostoPorEntidad = (ArrayList<DtoCentroCostosVista>) servicioCentroCosto.listaCentroCostoActivoXrEntidadesSub(dto.getCodigoEntidadSubcontratadaSeleccionada());
				ArrayList<DtoCentroCostosVista> listaCentroCostoPorEntidadAreaSubAlmacen = new ArrayList<DtoCentroCostosVista>();
				
				for (DtoCentroCostosVista dtoCentroCostosPorEntidad : listaCentroCostoPorEntidad) 
				{
					if(dtoCentroCostosPorEntidad.getTipoArea() ==ConstantesBD.codigoTipoAreaSubalmacen){
						listaCentroCostoPorEntidadAreaSubAlmacen.add(dtoCentroCostosPorEntidad);
					}
				}
				
				
				if(!Utilidades.isEmpty(listaCentroCostoPorEntidadAreaSubAlmacen))
				{
					//SI
					long entidad = new Long(dto.getCodigoEntidadSubcontratadaSeleccionada()+"").longValue();
					EntidadesSubcontratadas entidadSub = new EntidadesSubcontratadas();
					EntidadesSubcontratadasDelegate entiDao = new EntidadesSubcontratadasDelegate();
					entidadSub=entiDao.findById(entidad);
					
					//GUARDA EL NIVEL DE AUTORIZACION Y LA ENTIDAD
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
					
					
					ArrayList<DtoCentroCosto> listaCentrosCostoentidadSeleccionada = new ArrayList<DtoCentroCosto>();
					for (DtoCentroCostosVista dtoCentroCostosVista : listaCentroCostoPorEntidadAreaSubAlmacen) 
					{
						DtoCentroCosto centroCostoEntidadSeleccionada; centroCostoEntidadSeleccionada = new DtoCentroCosto();
						centroCostoEntidadSeleccionada.setCodigoCentroCosto(dtoCentroCostosVista.getCodigo());
						centroCostoEntidadSeleccionada.setNombre(dtoCentroCostosVista.getNombre());
						listaCentrosCostoentidadSeleccionada.add(centroCostoEntidadSeleccionada);
					}
					validacion.setListaCentrosCosto(listaCentrosCostoentidadSeleccionada);
					
				}
				else{
					validacion.setGenerarAutorizacion(false);
				}
			}
		}
		
		
		Log4JManager.info("Genera autorizacion automatica? "+validacion.isGenerarAutorizacion());
		UtilidadBD.closeConnection(con);
		
		
		return validacion;
	}
	
	

}
