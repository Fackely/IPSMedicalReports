package com.servinte.axioma.bl.capitacion.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.dto.capitacion.DTOBusquedaCierreTemporalArticulo;
import com.princetonsa.dto.capitacion.DTOBusquedaCierreTemporalServicio;
import com.princetonsa.dto.inventario.DtoClaseInventario;
import com.servinte.axioma.bl.capitacion.interfaz.ICierrePresupuestoMundo;
import com.servinte.axioma.bl.manejoPaciente.impl.ProcesoGeneracionAutorizacionMundo;
import com.servinte.axioma.bl.manejoPaciente.interfaz.IProcesoGeneracionAutorizacionMundo;
import com.servinte.axioma.dao.fabrica.inventario.InventarioDAOFabrica;
import com.servinte.axioma.dao.interfaz.inventario.IClaseInventarioDAO;
import com.servinte.axioma.delegate.manejoPaciente.AutorizacionesDelegate;
import com.servinte.axioma.dto.manejoPaciente.AnulacionAutorizacionSolicitudDto;
import com.servinte.axioma.dto.manejoPaciente.AutorizacionPorOrdenDto;
import com.servinte.axioma.dto.ordenes.MedicamentoInsumoAutorizacionOrdenDto;
import com.servinte.axioma.dto.ordenes.ServicioAutorizacionOrdenDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.mundo.fabrica.capitacion.CapitacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.ICierreTempClaseInvArtMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.ICierreTempNivelAteClaseInvArtMundo;
import com.servinte.axioma.orm.CierreTempClaseInvArt;
import com.servinte.axioma.orm.CierreTempGrupoServicio;
import com.servinte.axioma.orm.CierreTempNivAteClInvArt;
import com.servinte.axioma.orm.CierreTempNivelAteGruServ;
import com.servinte.axioma.orm.CierreTempNivelAtenArt;
import com.servinte.axioma.orm.CierreTempNivelAtenServ;
import com.servinte.axioma.orm.CierreTempServArt;
import com.servinte.axioma.orm.ClaseInventario;
import com.servinte.axioma.orm.Contratos;
import com.servinte.axioma.orm.GruposServicios;
import com.servinte.axioma.orm.NivelAtencion;
import com.servinte.axioma.servicio.fabrica.capitacion.CapitacionFabricaServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.ICierreTempGrupoServicioServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.ICierreTempNivelAteGruServServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.ICierreTempNivelAtenArtServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.ICierreTempNivelAtenServServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.ICierreTempServArtServicio;

public class CierrePresupuestoMundo implements ICierrePresupuestoMundo{
	

	
	/**
	 * Metodo que se encarga de recalcular el cierre de presupuesto.
	 * 
	 * @param anulacionAutorizacionDto
	 * @param autorizacionPorOrdenDto
	 * @param institucion
	 * @throws IPSException
	 */
	public void recalcularCierreTemporalPresupuesto(AnulacionAutorizacionSolicitudDto anulacionAutorizacionDto,
			AutorizacionPorOrdenDto autorizacionPorOrdenDto, int institucion, boolean esServicios)throws IPSException
	{
		try{
			IProcesoGeneracionAutorizacionMundo procesoGeneracionAutorizacionMundo=new ProcesoGeneracionAutorizacionMundo();
			ICierreTempServArtServicio cierreServArtServicio = CapitacionFabricaServicio.crearCierreTempServArtServicio();
			ICierreTempNivelAtenServServicio cierreNivelAtenServServicio = CapitacionFabricaServicio.crearCierreTempNivelAtenServServicio();
			ICierreTempGrupoServicioServicio cierreGrupoServServicio = CapitacionFabricaServicio.crearCierreTempGrupoServicioServicio();
			ICierreTempNivelAteGruServServicio cierreNivelGrupoSerServicio = CapitacionFabricaServicio.crearCierreTempNivelAteGruServServicio();
			
			ICierreTempClaseInvArtMundo cierreClaseInvArticuloMundo = CapitacionFabricaMundo.crearCierreTempClaseInvArtMundo();
			ICierreTempNivelAtenArtServicio cierreNivelArticuloServicio = CapitacionFabricaServicio.crearCierreTempNivelAtenArtServicio();
			ICierreTempNivelAteClaseInvArtMundo cierreNivelAteClaseInvArticuloMundo = CapitacionFabricaMundo.crearCierreTempNivelAteClaseInvArtMundo();
			IClaseInventarioDAO claseInventarioDAO = InventarioDAOFabrica.crearClaseInventarioDAO();
					
			DTOBusquedaCierreTemporalServicio dtoParametrosServicios = new DTOBusquedaCierreTemporalServicio();
			DTOBusquedaCierreTemporalArticulo dtoParametrosArticulos = new DTOBusquedaCierreTemporalArticulo();		
			ArrayList<CierreTempNivelAteGruServ> listaCierreGrupoNivelServicio =null;
			CierreTempNivelAteGruServ cierreGrupoNivelServicio =null;
			ArrayList<CierreTempGrupoServicio> listaCierreGrupoServicio = null;
			CierreTempGrupoServicio cierreGrupoServicio = null;
			ArrayList<CierreTempNivelAtenServ> listaCierreNivelServicio = null;
			CierreTempNivelAtenServ cierreNivelServicio = null;
			ArrayList<CierreTempServArt> listaCierreServArt=null;
			CierreTempServArt cierreServArt = null;
			ArrayList<CierreTempClaseInvArt> listaCierreClaseInvArt =null;
			CierreTempClaseInvArt cierreClaseInvArt =null;
			ArrayList<CierreTempNivelAtenArt> listaCierreNivelAtencion=null;
			CierreTempNivelAtenArt cierreNivelAtencion=null;
			ArrayList<CierreTempNivAteClInvArt> listaCierreNivelAteClaseInvArticulo =null;
			CierreTempNivAteClInvArt cierreNivelAteClaseInvArticulo =null;
			List<ServicioAutorizacionOrdenDto> serviciosAutorizados=null;
			List<MedicamentoInsumoAutorizacionOrdenDto> medicamentosInsumosAutorizados=null;
			AutorizacionesDelegate autorizacionesDelegate = new AutorizacionesDelegate();
			if(esServicios){
				serviciosAutorizados=autorizacionesDelegate.obtenerValorTarifaServiciosAutorizados(autorizacionPorOrdenDto.getConsecutivo());
				for (ServicioAutorizacionOrdenDto servicioAutorizadoDto : serviciosAutorizados) {
					//Verifico que el valor tarifa del servicio sea diferente de nulo 
					if (servicioAutorizadoDto.getValorTarifa() != null && servicioAutorizadoDto.getCodigoContrato() != null
							&& servicioAutorizadoDto.getConsecutivoNivelAtencion() != null
							&& servicioAutorizadoDto.getCodigoGrupoServicio() != null){
						dtoParametrosServicios = new DTOBusquedaCierreTemporalServicio();					
						dtoParametrosServicios.setCierreServicio(ConstantesBD.acronimoSiChar);
						dtoParametrosServicios.setCodigoContrato(servicioAutorizadoDto.getCodigoContrato());					
						int codigoContrato=servicioAutorizadoDto.getCodigoContrato();
						double valorDescontar=servicioAutorizadoDto.getValorTarifa().doubleValue();
						Long codigoNivelAtencion=servicioAutorizadoDto.getConsecutivoNivelAtencion();
						Integer codigoGrupoServicio=servicioAutorizadoDto.getCodigoGrupoServicio();
						procesoGeneracionAutorizacionMundo.eliminarAcumuladoCierreTemporal(codigoContrato);
						listaCierreServArt = cierreServArtServicio.buscarCierreTemporalServicioArticulo(dtoParametrosServicios);
						if(Utilidades.isEmpty(listaCierreServArt)){
							Contratos contrato = new Contratos();						
							cierreServArt = new CierreTempServArt();
							contrato.setCodigo(codigoContrato);
							cierreServArt.setContratos(contrato);
							cierreServArt.setFechaCierre(UtilidadFecha.getFechaActualTipoBD());
							cierreServArt.setValorAcumulado(0D);
							cierreServArt.setCierreServicio(ConstantesBD.acronimoSiChar);
						}else{
							cierreServArt = listaCierreServArt.get(0);
						}
														
						cierreServArt.setValorAcumulado(cierreServArt.getValorAcumulado() - valorDescontar);
						cierreServArtServicio.attachDirty(cierreServArt);	
						dtoParametrosServicios.setCodigoNivelAtencion(codigoNivelAtencion);					
						listaCierreNivelServicio = cierreNivelAtenServServicio.buscarCierreTemporalNivelAtencion(dtoParametrosServicios);
						if(Utilidades.isEmpty(listaCierreNivelServicio)){
							Contratos contrato = new Contratos();						
							cierreNivelServicio = new CierreTempNivelAtenServ();
							NivelAtencion nivelAtencion = new NivelAtencion();
							contrato.setCodigo(codigoContrato);
							cierreNivelServicio.setContratos(contrato);
							cierreNivelServicio.setFechaCierre(UtilidadFecha.getFechaActualTipoBD());
							cierreNivelServicio.setValorAcumulado(0D);						
							nivelAtencion.setConsecutivo(codigoNivelAtencion);
							cierreNivelServicio.setNivelAtencion(nivelAtencion);
						}else{
							cierreNivelServicio = listaCierreNivelServicio.get(0);
						}
							
						cierreNivelServicio.setValorAcumulado(cierreNivelServicio.getValorAcumulado()-valorDescontar);
						cierreNivelAtenServServicio.sincronizarCierreTemporal(cierreNivelServicio);
						dtoParametrosServicios.setCodigoGrupoServicio(codigoGrupoServicio);
						listaCierreGrupoServicio = cierreGrupoServServicio.buscarCierreTemporalNivelAtencion(dtoParametrosServicios);
						
						if(Utilidades.isEmpty(listaCierreGrupoServicio)){
							Contratos contrato = new Contratos();						
							cierreGrupoServicio = new CierreTempGrupoServicio();
							GruposServicios grupoServicio = new GruposServicios();
							contrato.setCodigo(codigoContrato);
							cierreGrupoServicio.setContratos(contrato);
							cierreGrupoServicio.setFechaCierre(UtilidadFecha.getFechaActualTipoBD());
							cierreGrupoServicio.setValorAcumulado(0D);						
							grupoServicio.setCodigo(codigoGrupoServicio);
							cierreGrupoServicio.setGruposServicios(grupoServicio);
						}else{
							cierreGrupoServicio=listaCierreGrupoServicio.get(0);
						}
						cierreGrupoServicio.setValorAcumulado(cierreGrupoServicio.getValorAcumulado() - valorDescontar);
						cierreGrupoServServicio.sincronizarCierreTemporal(cierreGrupoServicio);
						dtoParametrosServicios.setCodigoGrupoServicio(codigoGrupoServicio);
						listaCierreGrupoNivelServicio = 
							cierreNivelGrupoSerServicio.buscarCierreTemporalNivelAtencionGrupoServicio(dtoParametrosServicios);
						
						if(Utilidades.isEmpty(listaCierreGrupoNivelServicio)){
							Contratos contrato = new Contratos();						
							cierreGrupoNivelServicio = new CierreTempNivelAteGruServ();
							NivelAtencion nivelAtencion = new NivelAtencion();
							GruposServicios grupoServicio = new GruposServicios();
							contrato.setCodigo(codigoContrato);
							nivelAtencion.setConsecutivo(codigoNivelAtencion);
							grupoServicio.setCodigo(codigoGrupoServicio);
							cierreGrupoNivelServicio.setContratos(contrato);
							cierreGrupoNivelServicio.setFechaCierre(UtilidadFecha.getFechaActualTipoBD());
							cierreGrupoNivelServicio.setValorAcumulado(0D);
							cierreGrupoNivelServicio.setNivelAtencion(nivelAtencion);
							cierreGrupoNivelServicio.setGruposServicios(grupoServicio);
						}else{
							cierreGrupoNivelServicio = listaCierreGrupoNivelServicio.get(0);
						}
						cierreGrupoNivelServicio.setValorAcumulado(cierreGrupoNivelServicio.getValorAcumulado() - valorDescontar);
						cierreNivelGrupoSerServicio.sincronizarCierreTemporal(cierreGrupoNivelServicio);
					}
				}
			}
			else{
				medicamentosInsumosAutorizados=autorizacionesDelegate.obtenerValorTarifaMedicamentosInsumosAutorizados(autorizacionPorOrdenDto.getConsecutivo());
				for (MedicamentoInsumoAutorizacionOrdenDto medicamentoInsumoAutorizadoDto : medicamentosInsumosAutorizados) {
					
					//Verifico caules de los MedicamentosInsumos se deben autorizar
					if (medicamentoInsumoAutorizadoDto.getValorTarifa() != null
							&& medicamentoInsumoAutorizadoDto.getCantidad() != null
							&& medicamentoInsumoAutorizadoDto.getCodigoContrato() != null
							&& medicamentoInsumoAutorizadoDto.getSubGrupoInventario() != null){
						dtoParametrosArticulos = new DTOBusquedaCierreTemporalArticulo();
						dtoParametrosServicios = new DTOBusquedaCierreTemporalServicio();
						DtoClaseInventario dtoClaseInventario = new DtoClaseInventario();
						int codigoContrato=medicamentoInsumoAutorizadoDto.getCodigoContrato();
						BigDecimal valorDescontar=medicamentoInsumoAutorizadoDto.getValorTarifa().multiply(BigDecimal.valueOf(medicamentoInsumoAutorizadoDto.getCantidad()));
						dtoClaseInventario = claseInventarioDAO.obtenerClaseInventarioPorSungrupo(medicamentoInsumoAutorizadoDto.getSubGrupoInventario());
						medicamentoInsumoAutorizadoDto.setSubGrupoInventario(medicamentoInsumoAutorizadoDto.getSubGrupoInventario());
						medicamentoInsumoAutorizadoDto.setClaseInventario(dtoClaseInventario.getCodigo());
						medicamentoInsumoAutorizadoDto.setNombreClaseInventario(dtoClaseInventario.getNombre());
						
						dtoParametrosServicios.setCierreServicio(ConstantesBD.acronimoNoChar);
						dtoParametrosServicios.setCodigoContrato(codigoContrato);
						procesoGeneracionAutorizacionMundo.eliminarAcumuladoCierreTemporal(codigoContrato);
						listaCierreServArt = cierreServArtServicio.buscarCierreTemporalServicioArticulo(dtoParametrosServicios);
						
						if(Utilidades.isEmpty(listaCierreServArt)){
							Contratos contrato = new Contratos();						
							cierreServArt = new CierreTempServArt();
							
							contrato.setCodigo(codigoContrato);
							cierreServArt.setContratos(contrato);
							cierreServArt.setFechaCierre(UtilidadFecha.getFechaActualTipoBD());
							cierreServArt.setValorAcumulado(0D);		
							cierreServArt.setCierreServicio(ConstantesBD.acronimoNoChar);
						}else{
							cierreServArt = listaCierreServArt.get(0);
						}
													
						cierreServArt.setValorAcumulado(cierreServArt.getValorAcumulado() - valorDescontar.doubleValue());
						cierreServArtServicio.attachDirty(cierreServArt);		
							
						if(medicamentoInsumoAutorizadoDto.getClaseInventario() != ConstantesBD.codigoNuncaValido){
						
							dtoParametrosArticulos.setCodigoContrato(codigoContrato);
							dtoParametrosArticulos.setCodigoClaseInventario(medicamentoInsumoAutorizadoDto.getClaseInventario());
							
							listaCierreClaseInvArt = cierreClaseInvArticuloMundo.buscarCierreTemporalClaseInventarioArticulo(dtoParametrosArticulos);
							
							if(Utilidades.isEmpty(listaCierreClaseInvArt)){
								Contratos contrato = new Contratos();						
								ClaseInventario claseInventario = new ClaseInventario();
								
								cierreClaseInvArt = new CierreTempClaseInvArt();
								
								contrato.setCodigo(codigoContrato);
								claseInventario.setCodigo(medicamentoInsumoAutorizadoDto.getClaseInventario());
														
								cierreClaseInvArt.setContratos(contrato);
								cierreClaseInvArt.setFechaCierre(UtilidadFecha.getFechaActualTipoBD());
								cierreClaseInvArt.setValorAcumulado(BigDecimal.ZERO);
								cierreClaseInvArt.setClaseInventario(claseInventario);
							}else{
								cierreClaseInvArt = listaCierreClaseInvArt.get(0);
							}
							
							cierreClaseInvArt.setValorAcumulado(cierreClaseInvArt.getValorAcumulado().subtract(valorDescontar));
							cierreClaseInvArticuloMundo.sincronizarCierreTemporal(cierreClaseInvArt);
						}						
						
						if(medicamentoInsumoAutorizadoDto.getConsecutivoNivelAtencion()!=null && medicamentoInsumoAutorizadoDto.getConsecutivoNivelAtencion().longValue()!=ConstantesBD.codigoNuncaValidoLong){
							dtoParametrosArticulos.setCodigoNivelAtencion(medicamentoInsumoAutorizadoDto.getConsecutivoNivelAtencion());
							listaCierreNivelAtencion = cierreNivelArticuloServicio.buscarCierreTemporalNivelAtencion(dtoParametrosArticulos);
							
							if(Utilidades.isEmpty(listaCierreNivelAtencion)){
								Contratos contrato = new Contratos();						
								cierreNivelAtencion = new CierreTempNivelAtenArt();
								NivelAtencion nivelAtencion = new NivelAtencion();
								
								contrato.setCodigo(codigoContrato);
								nivelAtencion.setConsecutivo(medicamentoInsumoAutorizadoDto.getConsecutivoNivelAtencion());
														
								cierreNivelAtencion.setContratos(contrato);
								cierreNivelAtencion.setFechaCierre(UtilidadFecha.getFechaActualTipoBD());
								cierreNivelAtencion.setValorAcumulado(0D);						
								cierreNivelAtencion.setNivelAtencion(nivelAtencion);
							}else{
								cierreNivelAtencion = listaCierreNivelAtencion.get(0);
							}
							
							cierreNivelAtencion.setValorAcumulado(cierreNivelAtencion.getValorAcumulado() - valorDescontar.doubleValue());
							
							cierreNivelArticuloServicio.sincronizarCierreTemporal(cierreNivelAtencion);
							
							if(medicamentoInsumoAutorizadoDto.getClaseInventario() != ConstantesBD.codigoNuncaValido){
								listaCierreNivelAteClaseInvArticulo = 
									cierreNivelAteClaseInvArticuloMundo.buscarCierreTemporalNivelAtencionClaseInventarioArticulo(dtoParametrosArticulos);
								
								if(Utilidades.isEmpty(listaCierreNivelAteClaseInvArticulo)){
									Contratos contrato = new Contratos();
									ClaseInventario claseInventario = new ClaseInventario();
									NivelAtencion nivelAtencion = new NivelAtencion();
									cierreNivelAteClaseInvArticulo = new CierreTempNivAteClInvArt();
																	
									contrato.setCodigo(codigoContrato);
									claseInventario.setCodigo(medicamentoInsumoAutorizadoDto.getClaseInventario());
									nivelAtencion.setConsecutivo(medicamentoInsumoAutorizadoDto.getConsecutivoNivelAtencion());
															
									cierreNivelAteClaseInvArticulo.setContratos(contrato);
									cierreNivelAteClaseInvArticulo.setFechaCierre(UtilidadFecha.getFechaActualTipoBD());
									cierreNivelAteClaseInvArticulo.setValorAcumulado(BigDecimal.ZERO);
									cierreNivelAteClaseInvArticulo.setClaseInventario(claseInventario);							
									cierreNivelAteClaseInvArticulo.setNivelAtencion(nivelAtencion);
								}else{
									cierreNivelAteClaseInvArticulo = listaCierreNivelAteClaseInvArticulo.get(0);
								}
									
								cierreNivelAteClaseInvArticulo.setValorAcumulado(cierreNivelAteClaseInvArticulo.getValorAcumulado().subtract(valorDescontar));
								
								cierreNivelAteClaseInvArticuloMundo.sincronizarCierreTemporal(cierreNivelAteClaseInvArticulo);
							}	
						}
					}
				}
			}
		}catch (IPSException ipsme) {
			Log4JManager.error(ipsme.getMessage(),ipsme);
			throw ipsme;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
	}

}
