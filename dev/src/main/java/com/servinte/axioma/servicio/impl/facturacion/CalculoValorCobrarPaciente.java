/**
 * 
 */
package com.servinte.axioma.servicio.impl.facturacion;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.cargos.DtoDetalleCargo;
import com.princetonsa.mundo.cargos.Cargos;
import com.servinte.axioma.dto.facturacion.DtoAgrupacionCalucloValorCobrarPaciente;
import com.servinte.axioma.dto.facturacion.DtoInfoCargoCobroPaciente;
import com.servinte.axioma.dto.facturacion.DtoInfoCobroPaciente;
import com.servinte.axioma.dto.facturacion.DtoInfoMontoCobroDetallado;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.orm.SubCuentas;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.ManejoPacienteServicioFabrica;
import com.servinte.axioma.servicio.interfaz.facturacion.ICalculoValorCobrarPaciente;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.ISubCuentasServicio;

/**
 * @author armando
 *
 */
public class CalculoValorCobrarPaciente implements ICalculoValorCobrarPaciente
{

	@Override
	public DtoInfoCobroPaciente valorCobrarAPaciente(int codigoResponsable) throws IPSException 
	{
		DtoInfoCobroPaciente dto=new DtoInfoCobroPaciente();
		ISubCuentasServicio subCuentaServicio=ManejoPacienteServicioFabrica.crearSubCuentasServicio();
		//SubCuentas subCuenta = subCuentaServicio.cargarSubCuenta(codigoResponsable);
		SubCuentas subCuenta = subCuentaServicio.cargarSubcuentaDetalleMonto(codigoResponsable);
		
		DtoDetalleCargo filtro=new DtoDetalleCargo();
		filtro.setCodigoSubcuenta(subCuenta.getSubCuenta());
		ArrayList<DtoDetalleCargo> dtoCargos=Cargos.cargarDetalleCargos(filtro);

		
		if(subCuenta.getTipoCobroPaciente()== null || subCuenta.getTipoCobroPaciente().equals(ConstantesIntegridadDominio.acronimoTipoPacienteNoManejaMontos))
		{
			Log4JManager.info("el tipo cobro paciente no maneja montos");
			BigDecimal valorTotal=new BigDecimal(0);
			for(DtoDetalleCargo dtoDetCargo:dtoCargos)
			{
				valorTotal.add(new BigDecimal(dtoDetCargo.getValorTotalCargado()));
			}
			
			//dto.setValorCargoPaciente(valorTotal.multiply(subCuenta.getPorcentajeMontoCobro()).divide(new BigDecimal(100))); 
			dto.setValorCargoPaciente(new BigDecimal(0)); // Si no maneja montos el valor cargado al paciente debe ser cero. Tarea xplanner 35241. Cristhian Murillo
			
			Log4JManager.info("valor a cargo del paciente -->"+dto.getValorCargoPaciente());
			dto.setGenerado(true);
			dto.setManejaMonto(false);
		}
		else
		{
			dto.setManejaMonto(true);
			Log4JManager.info("el tipo cobro paciente maneja montos");
			if(subCuenta.getTipoMontoCobro().equals(ConstantesIntegridadDominio.acronimoTipoDetalleMontoCobroGEN))
			{
				Log4JManager.info("tipo monto cobro GENERAL");
				dto.setDetallado(false);
				if(subCuenta.getDetalleMonto()!=null&&subCuenta.getDetalleMonto().getDetalleMontoGeneral()!=null)
				{
					if(subCuenta.getDetalleMonto().getDetalleMontoGeneral().getValor()!=null&&subCuenta.getDetalleMonto().getDetalleMontoGeneral().getValor()>0)
					{
						dto.setValorCargoPaciente(new BigDecimal(subCuenta.getDetalleMonto().getDetalleMontoGeneral().getValor()*subCuenta.getDetalleMonto().getDetalleMontoGeneral().getCantidadMonto()));
						dto.setGenerado(true);
					}
					else if(subCuenta.getDetalleMonto().getDetalleMontoGeneral().getPorcentaje()!=null&&subCuenta.getDetalleMonto().getDetalleMontoGeneral().getPorcentaje()>0)
					{
						BigDecimal valorTotal=new BigDecimal(0);
						double porcentajeDescuento = 0;
						for(DtoDetalleCargo dtoDetCargo:dtoCargos)
						{
							/*
							 * Inc. MT 2281 
							 */
							if(dtoDetCargo.getEstado()==ConstantesBD.estadoSolFactCargada && 
									dtoDetCargo.getPaquetizado().equals(ConstantesBD.acronimoNo) )
							{
								porcentajeDescuento = (dtoDetCargo.getPorcentajeDescuento() > 0) ? dtoDetCargo.getPorcentajeDescuento() : 0;
								valorTotal=valorTotal.add(new BigDecimal(dtoDetCargo.getValorTotalCargado()-(dtoDetCargo.getValorTotalCargado() * (porcentajeDescuento / 100))));
							}
						}
						dto.setValorCargoPaciente(valorTotal.multiply(new BigDecimal(subCuenta.getDetalleMonto().getDetalleMontoGeneral().getPorcentaje())).divide(new BigDecimal(100)));
						Log4JManager.info("valor a cargo del paciente -->"+dto.getValorCargoPaciente());
						dto.setGenerado(true);
					}
					else
					{
						dto.setGenerado(false);
						dto.setObservaciones("No esta definido el Valor o porcentaje del Monto para el Convenio "+subCuenta.getConvenios().getNombre()+". Por favor verifique. Proceso Cancelado");
						Log4JManager.info("No esta definido el Valor o porcentaje del Monto para el Convenio "+subCuenta.getConvenios().getNombre()+". Por favor verifique. Proceso Cancelado");
					}
				}
				else
				{
					dto.setGenerado(false);
					dto.setObservaciones("No esta definido el Valor o porcentaje del Monto para el Convenio "+subCuenta.getConvenios().getNombre()+". Por favor verifique. Proceso Cancelado");
					Log4JManager.info("No esta definido el Valor o porcentaje del Monto para el Convenio "+subCuenta.getConvenios().getNombre()+". Por favor verifique. Proceso Cancelado");
				}
			}
			else
			{
				Log4JManager.info("tipo monto cobro DETALLADO");
				dto.setDetallado(true);
				//hacer calculos para buscar con detallado.
				boolean contieneErrores=false;
				Log4JManager.info("voy a mirar el detalle de los cargos.");
				for(DtoDetalleCargo dtoDetCargo:dtoCargos)
				{
					//se deben tomar los cargos en estado cargado.
					if(dtoDetCargo.getEstado()==ConstantesBD.estadoSolFactCargada)
					{
						Log4JManager.info("evaluando cargo codigo -->"+dtoDetCargo.getCodigoDetalleCargo());

						DtoInfoCargoCobroPaciente dtoInfoCargo=new DtoInfoCargoCobroPaciente();
						//busqueda del monto a plicar.
						if(dtoDetCargo.getCodigoServicio()>0)
						{
							Log4JManager.info("es cargo del servicio "+dtoDetCargo.getCodigoServicio());
							DtoInfoMontoCobroDetallado infoMonto=(DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCalculoValorCobrarPacienteDao().obtenerInfoMontoCobroServicioArticulo(subCuenta.getDetalleMonto().getDetalleCodigo(),dtoDetCargo.getCodigoServicio(),ConstantesBD.codigoNuncaValido));
							if(infoMonto.getCodigo()<=0)
							{
								dto.setGenerado(false);
								dto.setObservaciones("No existe parametrización de Monto para el Servicio "+dtoDetCargo.getNombreServicio()+". Por favor verifique. Proceso Cancelado.");
								contieneErrores=true;
							}
							else
							{
								dtoInfoCargo.setCantidadCargada(dtoDetCargo.getCantidadCargada());
								dtoInfoCargo.setCodigoArticulo(ConstantesBD.codigoNuncaValido);
								dtoInfoCargo.setCodigoDetalleCargo(dtoDetCargo.getCodigoDetalleCargo());
								dtoInfoCargo.setCodigoServicio(dtoDetCargo.getCodigoServicio());
								dtoInfoCargo.setInformacionMonto(infoMonto);
								dtoInfoCargo.setNumeroSolicitud(dtoDetCargo.getNumeroSolicitud());
							}
							Log4JManager.info("montoDetallado para aplicar al servicio"+infoMonto.getCodigo());
							
						}
						else if(dtoDetCargo.getCodigoArticulo()>0)
						{
							Log4JManager.info("es cargo del articulo "+dtoDetCargo.getCodigoArticulo());
							DtoInfoMontoCobroDetallado infoMonto=(DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCalculoValorCobrarPacienteDao().obtenerInfoMontoCobroServicioArticulo(subCuenta.getDetalleMonto().getDetalleCodigo(),ConstantesBD.codigoNuncaValido, dtoDetCargo.getCodigoArticulo()));
							if(infoMonto.getCodigo()<=0)
							{
								dto.setGenerado(false);
								dto.setObservaciones("No existe parametrización de Monto para el Articulo "+dtoDetCargo.getCodigoArticulo()+". Por favor verifique. Proceso Cancelado.");
								contieneErrores=true;
							}
							else
							{
								dtoInfoCargo.setCantidadCargada(dtoDetCargo.getCantidadCargada());
								dtoInfoCargo.setCodigoArticulo(ConstantesBD.codigoNuncaValido);
								dtoInfoCargo.setCodigoDetalleCargo(dtoDetCargo.getCodigoDetalleCargo());
								dtoInfoCargo.setCodigoServicio(dtoDetCargo.getCodigoServicio());
								dtoInfoCargo.setInformacionMonto(infoMonto);
								dtoInfoCargo.setNumeroSolicitud(dtoDetCargo.getNumeroSolicitud());
							}
							Log4JManager.info("montoDetallado para aplicar al articulo"+infoMonto.getCodigo());

						}
						dto.getInfoCargoCobroPaciente().add(dtoInfoCargo);
					}
				}
				if(!contieneErrores)
				{
					//agrupacion de servicios/articulos o servicios/articulo especificos de acuerdo al monto.
					Log4JManager.info("agrupacion de servicios/articulos o servicios/articulo especificos de acuerdo al monto.");
					ArrayList<DtoAgrupacionCalucloValorCobrarPaciente> agrupacion=new ArrayList<DtoAgrupacionCalucloValorCobrarPaciente>();
					for(DtoInfoCargoCobroPaciente dtoInfoCargo:dto.getInfoCargoCobroPaciente())
					{
						boolean asociado=false;
						if(dtoInfoCargo.getCodigoServicio()>0)
						{
							if(!dtoInfoCargo.getInformacionMonto().isPorAgrupacion())
							{
								for(DtoAgrupacionCalucloValorCobrarPaciente agru:agrupacion)
								{
									if(agru.isServicio()&&!agru.isAgrupacion())
									{
										if(agru.getInfoMonto().getCodigo()==dtoInfoCargo.getInformacionMonto().getCodigo())
										{
											agru.getDtoInfoCargo().add(dtoInfoCargo);
											agru.setCantidadCargadaMonto(agru.getCantidadCargadaMonto()+dtoInfoCargo.getCantidadCargada());
											asociado=true;
										}
									}
								}
							}
							else
							{
								for(DtoAgrupacionCalucloValorCobrarPaciente agru:agrupacion)
								{
									if(agru.isServicio()&&agru.isAgrupacion())
									{
										if(agru.getInfoMonto().getCodigo()==dtoInfoCargo.getInformacionMonto().getCodigo())
										{
											agru.getDtoInfoCargo().add(dtoInfoCargo);
											agru.setCantidadCargadaMonto(agru.getCantidadCargadaMonto()+dtoInfoCargo.getCantidadCargada());
											asociado=true;
										}
									}
								}
							}
						}
						else if(dtoInfoCargo.getCodigoArticulo()>0)
						{
							if(!dtoInfoCargo.getInformacionMonto().isPorAgrupacion())
							{
								for(DtoAgrupacionCalucloValorCobrarPaciente agru:agrupacion)
								{
									if(!agru.isServicio()&&!agru.isAgrupacion())
									{
										if(agru.getInfoMonto().getCodigo()==dtoInfoCargo.getInformacionMonto().getCodigo())
										{
											agru.getDtoInfoCargo().add(dtoInfoCargo);
											agru.setCantidadCargadaMonto(agru.getCantidadCargadaMonto()+dtoInfoCargo.getCantidadCargada());
											asociado=true;
										}
									}
								}
							}
							else
							{
								for(DtoAgrupacionCalucloValorCobrarPaciente agru:agrupacion)
								{
									if(!agru.isServicio()&&agru.isAgrupacion())
									{
										if(agru.getInfoMonto().getCodigo()==dtoInfoCargo.getInformacionMonto().getCodigo())
										{
											agru.getDtoInfoCargo().add(dtoInfoCargo);
											agru.setCantidadCargadaMonto(agru.getCantidadCargadaMonto()+dtoInfoCargo.getCantidadCargada());
											asociado=true;
										}
									}
								}
							}
						}
						//si no se asocio se genera un nuevo registro en la agrupacion.
						if(!asociado)
						{
							
							DtoAgrupacionCalucloValorCobrarPaciente dtoAgruTempo=new DtoAgrupacionCalucloValorCobrarPaciente();
							dtoAgruTempo.getDtoInfoCargo().add(dtoInfoCargo);
							dtoAgruTempo.setAgrupacion(dtoInfoCargo.getInformacionMonto().isPorAgrupacion());
							dtoAgruTempo.setCantidadCargadaMonto(dtoInfoCargo.getCantidadCargada());
							dtoAgruTempo.setInfoMonto(dtoInfoCargo.getInformacionMonto());
							dtoAgruTempo.setServicio(dtoInfoCargo.getInformacionMonto().getServicio()>0);
							agrupacion.add(dtoAgruTempo);
						}
					}

					//calculo del valor monto cobro.
					//Se hace en el dto.
					
					dto.setAgrupacionCalculo(agrupacion);
					
					//calculo del valor paciente.
					BigDecimal valorTotal=new BigDecimal(0);
					for(DtoAgrupacionCalucloValorCobrarPaciente agru:agrupacion)
					{
						//valorTotal=valorTotal.add(new BigDecimal(agru.calculoValorMonto()* agru.getInfoMonto().getCantidadMonto()));
						valorTotal=valorTotal.add(new BigDecimal(agru.calculoValorMonto()));						 
					}
					dto.setValorCargoPaciente(valorTotal);
					dto.setGenerado(true);
					Log4JManager.info("valor a cargo del paciente -->"+dto.getValorCargoPaciente());
				}
			}				
		}
		UtilidadTransaccion.getTransaccion().commit();
		return dto;
	}

}
