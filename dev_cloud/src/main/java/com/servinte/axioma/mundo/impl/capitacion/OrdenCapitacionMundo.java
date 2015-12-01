package com.servinte.axioma.mundo.impl.capitacion;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.struts.util.MessageResources;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.capitacion.ConstantesCapitacion;

import com.princetonsa.dto.capitacion.DtoLogCierrePresuCapita;
import com.princetonsa.dto.capitacion.DtoNivelAtencion;
import com.princetonsa.dto.capitacion.DtoProcesoPresupuestoCapitado;
import com.princetonsa.dto.capitacion.DtoTotalProcesoPresupuestoCapitado;
import com.servinte.axioma.dto.capitacion.DtoContratoReporte;
import com.servinte.axioma.dto.capitacion.DtoConvenioReporte;
import com.servinte.axioma.dto.capitacion.DtoDiaCierre;
import com.servinte.axioma.dto.capitacion.DtoGrupoClaseReporte;
import com.servinte.axioma.dto.capitacion.DtoNivelReporte;
import com.servinte.axioma.dto.capitacion.DtoProductoServicioReporte;
import com.servinte.axioma.dto.capitacion.DtoTotalProceso;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.mundo.fabrica.capitacion.CapitacionFabricaMundo;
import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.ICierrePresupuestoCapitacionoMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IDetalleValorizacionArticuloMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IDetalleValorizacionServicioMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.INivelAtencionMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IOrdenCapitacionMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IParametrizacionPresupuestoCapitacionMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IValorizacionPresupuestoCapGeneralMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IContratoMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IConveniosMundo;
import com.servinte.axioma.orm.Contratos;
import com.servinte.axioma.orm.Convenios;
import com.servinte.axioma.orm.ParamPresupuestosCap;
import com.servinte.axioma.persistencia.UtilidadTransaccion;

/**
 * Define la lógica de negocio relacionada con la
 *  Consulta de ordenes de capitacion subcontratada
 * 
 * @version 1.0, May 02, 2011
 * @author <a href="mailto:ricardo.ruiz@servinte.com.co">Ricardo Ruiz</a>
 * 
 */
public class OrdenCapitacionMundo implements IOrdenCapitacionMundo{

	/** * Contiene la lista de mensajes correspondiente a esta forma */
	MessageResources messageResource = MessageResources.getMessageResources("com.servinte.mensajes.capitacion.ConsultarImprimirOrdenesCapitacionSubcontratadaForm");
	
	public OrdenCapitacionMundo(){
		
	}
	
	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IOrdenCapitacionMundo#obtenerConsolidadoConveniosPorNivelAtencion(java.util.Calendar, int, char)
	 */
	@Override
	public ArrayList<DtoConvenioReporte> obtenerConsolidadoConveniosPorNivelAtencion(
			Calendar mesAnio, int codigoInstitucion, char esCapitacionSubcontratada) {
		try{
			HibernateUtil.beginTransaction();
			UtilidadTransaccion.getTransaccion().begin();
			IConveniosMundo convenioMundo = FacturacionFabricaMundo.crearcConveniosMundo();
			IContratoMundo contratoMundo = FacturacionFabricaMundo.crearContratoMundo();
			IParametrizacionPresupuestoCapitacionMundo presupuestoMundo = CapitacionFabricaMundo.crearParametrizacionPresupuestoCapitacionMundo();
			INivelAtencionMundo nivelAtencionMundo = CapitacionFabricaMundo.crearNivelAtencionMundo();
			ICierrePresupuestoCapitacionoMundo cierreMundo = CapitacionFabricaMundo.crearCierrePresupuestoCapitacionoMundo();

			ArrayList<DtoConvenioReporte> dtoConvenios = new ArrayList<DtoConvenioReporte>();
			ArrayList<Convenios> convenios = new ArrayList<Convenios>();

			ArrayList<Contratos> contratos = new ArrayList<Contratos>();

			List<DtoNivelReporte> nivelesAtencion = new ArrayList<DtoNivelReporte>();

			Calendar fechaInicio = Calendar.getInstance();
			Calendar fechaFin = Calendar.getInstance();
			fechaInicio.set(Calendar.YEAR, mesAnio.get(Calendar.YEAR));
			fechaInicio.set(Calendar.MONTH, mesAnio.get(Calendar.MONTH));
			fechaInicio.set(Calendar.DAY_OF_MONTH, mesAnio.getActualMinimum(Calendar.DAY_OF_MONTH));
			fechaFin.set(Calendar.YEAR, mesAnio.get(Calendar.YEAR));
			fechaFin.set(Calendar.MONTH, mesAnio.get(Calendar.MONTH));
			fechaFin.set(Calendar.DAY_OF_MONTH, mesAnio.getActualMaximum(Calendar.DAY_OF_MONTH));

			String tipoConvenio="";
			if(esCapitacionSubcontratada==ConstantesBD.acronimoSiChar){
				tipoConvenio=messageResource.getMessage(
				"consultarImprimirOrdenesCapitacionSubcontratada.convenioAutorizacion");
			}
			else if(esCapitacionSubcontratada==ConstantesBD.acronimoNoChar){
				tipoConvenio=messageResource.getMessage(
				"consultarImprimirOrdenesCapitacionSubcontratada.convenioSinAutorizacion");
			}
			convenios=convenioMundo
			.listarConveniosConParametrizacionPresupuestoPorInstitucionPorCapitacion(
					codigoInstitucion, esCapitacionSubcontratada, mesAnio);
			for(Convenios convenio: convenios){
				DtoConvenioReporte dtoConvenio = new DtoConvenioReporte();
				ArrayList<DtoContratoReporte> dtoContratos = new ArrayList<DtoContratoReporte>();
				dtoConvenio.setTipoConvenio(tipoConvenio);
				dtoConvenio.setNombre(convenio.getNombre());
				contratos=contratoMundo
				.listarContratosConParametrizacionPresupuestoPorConvenio(
						convenio.getCodigo(), mesAnio);
				for(Contratos contrato:contratos){
					DtoContratoReporte dtoContrato = new DtoContratoReporte();
					ArrayList<DtoNivelReporte> dtoNivelesAtencion = new ArrayList<DtoNivelReporte>();
					dtoContrato.setNumeroContrato(contrato.getNumeroContrato());
					ParamPresupuestosCap presupuestoContrato=presupuestoMundo
					.obtenerParametrizacionPresupuestoCapitado(contrato.getCodigo(), String.valueOf(mesAnio.get(Calendar.YEAR)));
					if(presupuestoContrato==null){
						UtilidadTransaccion.getTransaccion().commit();
						return new ArrayList<DtoConvenioReporte>();
					}
					dtoContrato.setValorMensual(presupuestoContrato.getValorContrato());
					dtoContrato.setPorcentajeGastoMensual(presupuestoContrato.getPorcentajeGastoGeneral());
					dtoContrato.setValorGastoMensual(presupuestoContrato.getValorGastoGeneral());

					nivelesAtencion=nivelAtencionMundo
					.listarNivelesAtencionConParametrizacionPresupuestoPorContrato(
							contrato.getCodigo(), mesAnio);
					for(DtoNivelReporte nivelAtencion:nivelesAtencion){
						DtoNivelReporte dtoNivel = new DtoNivelReporte();
						DtoTotalProceso dtoTotalServicio = new DtoTotalProceso();
						DtoTotalProceso dtoTotalArticulo = new DtoTotalProceso();
						dtoNivel.setNombre(nivelAtencion.getNombre());
						dtoNivel.setTotalPresupuestado(nivelAtencion.getTotalPresupuestado());
						//Se obtiene el total ordenado de servicios y articulos
						dtoTotalServicio=cierreMundo.obtenerTotalServiciosPorNivelPorProceso(contrato.getCodigo(),
								nivelAtencion.getConsecutivo(), ConstantesIntegridadDominio.acronimoTipoProcesoOrden,
								fechaInicio.getTime(), fechaFin.getTime()).get(0);
						dtoTotalArticulo=cierreMundo.obtenerTotalArticulosPorNivelPorProceso(contrato.getCodigo(),
								nivelAtencion.getConsecutivo(), ConstantesIntegridadDominio.acronimoTipoProcesoOrden,
								fechaInicio.getTime(), fechaFin.getTime()).get(0);
						dtoNivel.setTotalOrdenado(dtoTotalServicio.getTotalProceso().add(dtoTotalArticulo.getTotalProceso()));
						if(esCapitacionSubcontratada==ConstantesBD.acronimoSiChar){
							//Se obtiene el total Autorizado de servicios y articulos
							dtoTotalServicio=cierreMundo.obtenerTotalServiciosPorNivelPorProceso(contrato.getCodigo(),
									nivelAtencion.getConsecutivo(), ConstantesIntegridadDominio.acronimoTipoProcesoAutorizacion,
									fechaInicio.getTime(), fechaFin.getTime()).get(0);
							dtoTotalArticulo=cierreMundo.obtenerTotalArticulosPorNivelPorProceso(contrato.getCodigo(),
									nivelAtencion.getConsecutivo(), ConstantesIntegridadDominio.acronimoTipoProcesoAutorizacion,
									fechaInicio.getTime(), fechaFin.getTime()).get(0);
							dtoNivel.setTotalAutorizado(dtoTotalServicio.getTotalProceso().add(dtoTotalArticulo.getTotalProceso()));
						}
						//Se obtiene el total Cargos a la cuenta de servicios y articulos
						dtoTotalServicio=cierreMundo.obtenerTotalServiciosPorNivelPorProceso(contrato.getCodigo(),
								nivelAtencion.getConsecutivo(), ConstantesIntegridadDominio.acronimoTipoProcesoCargoCuenta,
								fechaInicio.getTime(), fechaFin.getTime()).get(0);
						dtoTotalArticulo=cierreMundo.obtenerTotalArticulosPorNivelPorProceso(contrato.getCodigo(),
								nivelAtencion.getConsecutivo(), ConstantesIntegridadDominio.acronimoTipoProcesoCargoCuenta,
								fechaInicio.getTime(), fechaFin.getTime()).get(0);
						dtoNivel.setTotalCargosCuenta(dtoTotalServicio.getTotalProceso().add(dtoTotalArticulo.getTotalProceso()));
						//Se obtiene el total Facturado de servicios y articulos
						dtoTotalServicio=cierreMundo.obtenerTotalServiciosPorNivelPorProceso(contrato.getCodigo(),
								nivelAtencion.getConsecutivo(), ConstantesIntegridadDominio.acronimoTipoProcesoFacturacion,
								fechaInicio.getTime(), fechaFin.getTime()).get(0);
						dtoTotalArticulo=cierreMundo.obtenerTotalArticulosPorNivelPorProceso(contrato.getCodigo(),
								nivelAtencion.getConsecutivo(), ConstantesIntegridadDominio.acronimoTipoProcesoFacturacion,
								fechaInicio.getTime(), fechaFin.getTime()).get(0);
						dtoNivel.setTotalFacturado(dtoTotalServicio.getTotalProceso().add(dtoTotalArticulo.getTotalProceso()));

						dtoNivelesAtencion.add(dtoNivel);
					}
					dtoContrato.setNivelesAtencion(dtoNivelesAtencion);
					dtoContratos.add(dtoContrato);
				}
				dtoConvenio.setContratos(dtoContratos);
				dtoConvenios.add(dtoConvenio);
			}

			HibernateUtil.endTransaction();
			return dtoConvenios; 
		}catch (Exception e) {
			HibernateUtil.abortTransaction();
		}
		return null;

	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IOrdenCapitacionMundo#obtenerConsolidadoConveniosPorNivelAtencionPorConvenio(java.util.Calendar, com.servinte.axioma.orm.Convenios, int, char)
	 */
	@Override
	public ArrayList<DtoConvenioReporte> obtenerConsolidadoConveniosPorNivelAtencionPorConvenio(
			Calendar mesAnio, Convenios convenio, int codigoInstitucion, char esCapitacionSubcontratada) {
		try{
			HibernateUtil.beginTransaction();
			IContratoMundo contratoMundo = FacturacionFabricaMundo.crearContratoMundo();
			IParametrizacionPresupuestoCapitacionMundo presupuestoMundo = CapitacionFabricaMundo.crearParametrizacionPresupuestoCapitacionMundo();
			INivelAtencionMundo nivelAtencionMundo = CapitacionFabricaMundo.crearNivelAtencionMundo();
			ICierrePresupuestoCapitacionoMundo cierreMundo = CapitacionFabricaMundo.crearCierrePresupuestoCapitacionoMundo();

			ArrayList<DtoConvenioReporte> dtoConvenios = new ArrayList<DtoConvenioReporte>();
			ArrayList<DtoContratoReporte> dtoContratos = new ArrayList<DtoContratoReporte>();
			ArrayList<Contratos> contratos = new ArrayList<Contratos>();

			List<DtoNivelReporte> nivelesAtencion = new ArrayList<DtoNivelReporte>();

			Calendar fechaInicio = Calendar.getInstance();
			Calendar fechaFin = Calendar.getInstance();
			fechaInicio.set(Calendar.YEAR, mesAnio.get(Calendar.YEAR));
			fechaInicio.set(Calendar.MONTH, mesAnio.get(Calendar.MONTH));
			fechaInicio.set(Calendar.DAY_OF_MONTH, mesAnio.getActualMinimum(Calendar.DAY_OF_MONTH));
			fechaFin.set(Calendar.YEAR, mesAnio.get(Calendar.YEAR));
			fechaFin.set(Calendar.MONTH, mesAnio.get(Calendar.MONTH));
			fechaFin.set(Calendar.DAY_OF_MONTH, mesAnio.getActualMaximum(Calendar.DAY_OF_MONTH));

			String tipoConvenio="";
			if(esCapitacionSubcontratada==ConstantesBD.acronimoSiChar){
				tipoConvenio=messageResource.getMessage(
				"consultarImprimirOrdenesCapitacionSubcontratada.convenioAutorizacion");
			}
			else if(esCapitacionSubcontratada==ConstantesBD.acronimoNoChar){
				tipoConvenio=messageResource.getMessage(
				"consultarImprimirOrdenesCapitacionSubcontratada.convenioSinAutorizacion");
			}
			DtoConvenioReporte dtoConvenio = new DtoConvenioReporte();
			dtoConvenio.setTipoConvenio(tipoConvenio);
			dtoConvenio.setNombre(convenio.getNombre());
			contratos=contratoMundo
			.listarContratosConParametrizacionPresupuestoPorConvenio(
					convenio.getCodigo(), mesAnio);
			for(Contratos contrato:contratos){
				DtoContratoReporte dtoContrato = new DtoContratoReporte();
				ArrayList<DtoNivelReporte> dtoNivelesAtencion = new ArrayList<DtoNivelReporte>();
				dtoContrato.setNumeroContrato(contrato.getNumeroContrato());
				ParamPresupuestosCap presupuestoContrato=presupuestoMundo
				.obtenerParametrizacionPresupuestoCapitado(contrato.getCodigo(), String.valueOf(mesAnio.get(Calendar.YEAR)));
				if(presupuestoContrato==null){
					UtilidadTransaccion.getTransaccion().commit();
					return new ArrayList<DtoConvenioReporte>();
				}
				dtoContrato.setValorMensual(presupuestoContrato.getValorContrato());
				dtoContrato.setPorcentajeGastoMensual(presupuestoContrato.getPorcentajeGastoGeneral());
				dtoContrato.setValorGastoMensual(presupuestoContrato.getValorGastoGeneral());

				nivelesAtencion=nivelAtencionMundo
				.listarNivelesAtencionConParametrizacionPresupuestoPorContrato(
						contrato.getCodigo(), mesAnio);
				for(DtoNivelReporte nivelAtencion:nivelesAtencion){
					DtoNivelReporte dtoNivel = new DtoNivelReporte();
					DtoTotalProceso dtoTotalServicio = new DtoTotalProceso();
					DtoTotalProceso dtoTotalArticulo = new DtoTotalProceso();
					dtoNivel.setNombre(nivelAtencion.getNombre());
					dtoNivel.setTotalPresupuestado(nivelAtencion.getTotalPresupuestado());
					//Se obtiene el total ordenado de servicios y articulos
					dtoTotalServicio=cierreMundo.obtenerTotalServiciosPorNivelPorProceso(contrato.getCodigo(),
							nivelAtencion.getConsecutivo(), ConstantesIntegridadDominio.acronimoTipoProcesoOrden,
							fechaInicio.getTime(), fechaFin.getTime()).get(0);
					dtoTotalArticulo=cierreMundo.obtenerTotalArticulosPorNivelPorProceso(contrato.getCodigo(),
							nivelAtencion.getConsecutivo(), ConstantesIntegridadDominio.acronimoTipoProcesoOrden,
							fechaInicio.getTime(), fechaFin.getTime()).get(0);
					dtoNivel.setTotalOrdenado(dtoTotalServicio.getTotalProceso().add(dtoTotalArticulo.getTotalProceso()));
					if(esCapitacionSubcontratada==ConstantesBD.acronimoSiChar){
						//Se obtiene el total Autorizado de servicios y articulos
						dtoTotalServicio=cierreMundo.obtenerTotalServiciosPorNivelPorProceso(contrato.getCodigo(),
								nivelAtencion.getConsecutivo(), ConstantesIntegridadDominio.acronimoTipoProcesoAutorizacion,
								fechaInicio.getTime(), fechaFin.getTime()).get(0);
						dtoTotalArticulo=cierreMundo.obtenerTotalArticulosPorNivelPorProceso(contrato.getCodigo(),
								nivelAtencion.getConsecutivo(), ConstantesIntegridadDominio.acronimoTipoProcesoAutorizacion,
								fechaInicio.getTime(), fechaFin.getTime()).get(0);
						dtoNivel.setTotalAutorizado(dtoTotalServicio.getTotalProceso().add(dtoTotalArticulo.getTotalProceso()));
					}
					//Se obtiene el total Cargos a la cuenta de servicios y articulos
					dtoTotalServicio=cierreMundo.obtenerTotalServiciosPorNivelPorProceso(contrato.getCodigo(),
							nivelAtencion.getConsecutivo(), ConstantesIntegridadDominio.acronimoTipoProcesoCargoCuenta,
							fechaInicio.getTime(), fechaFin.getTime()).get(0);
					dtoTotalArticulo=cierreMundo.obtenerTotalArticulosPorNivelPorProceso(contrato.getCodigo(),
							nivelAtencion.getConsecutivo(), ConstantesIntegridadDominio.acronimoTipoProcesoCargoCuenta,
							fechaInicio.getTime(), fechaFin.getTime()).get(0);
					dtoNivel.setTotalCargosCuenta(dtoTotalServicio.getTotalProceso().add(dtoTotalArticulo.getTotalProceso()));
					//Se obtiene el total Facturado de servicios y articulos
					dtoTotalServicio=cierreMundo.obtenerTotalServiciosPorNivelPorProceso(contrato.getCodigo(),
							nivelAtencion.getConsecutivo(), ConstantesIntegridadDominio.acronimoTipoProcesoFacturacion,
							fechaInicio.getTime(), fechaFin.getTime()).get(0);
					dtoTotalArticulo=cierreMundo.obtenerTotalArticulosPorNivelPorProceso(contrato.getCodigo(),
							nivelAtencion.getConsecutivo(), ConstantesIntegridadDominio.acronimoTipoProcesoFacturacion,
							fechaInicio.getTime(), fechaFin.getTime()).get(0);
					dtoNivel.setTotalFacturado(dtoTotalServicio.getTotalProceso().add(dtoTotalArticulo.getTotalProceso()));

					dtoNivelesAtencion.add(dtoNivel);
				}
				dtoContrato.setNivelesAtencion(dtoNivelesAtencion);
				dtoContratos.add(dtoContrato);
			}
			dtoConvenio.setContratos(dtoContratos);
			dtoConvenios.add(dtoConvenio);
			UtilidadTransaccion.getTransaccion().commit();
			HibernateUtil.endTransaction();
			return dtoConvenios;
		}catch (Exception e) {
			HibernateUtil.abortTransaction();
		}
		return null;
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IOrdenCapitacionMundo#obtenerConsolidadoConveniosPorNivelAtencionPorConvenioContrato(java.util.Calendar, com.servinte.axioma.orm.Convenios, com.servinte.axioma.orm.Contratos, int, char)
	 */
	@Override
	public ArrayList<DtoConvenioReporte> obtenerConsolidadoConveniosPorNivelAtencionPorConvenioContrato(
			Calendar mesAnio, Convenios convenio, Contratos contrato, int codigoInstitucion, char esCapitacionSubcontratada) {
		try{
			HibernateUtil.beginTransaction();
			IParametrizacionPresupuestoCapitacionMundo presupuestoMundo = CapitacionFabricaMundo.crearParametrizacionPresupuestoCapitacionMundo();
			INivelAtencionMundo nivelAtencionMundo = CapitacionFabricaMundo.crearNivelAtencionMundo();
			ICierrePresupuestoCapitacionoMundo cierreMundo = CapitacionFabricaMundo.crearCierrePresupuestoCapitacionoMundo();

			ArrayList<DtoConvenioReporte> dtoConvenios = new ArrayList<DtoConvenioReporte>();
			ArrayList<DtoContratoReporte> dtoContratos = new ArrayList<DtoContratoReporte>();
			ArrayList<DtoNivelReporte> dtoNivelesAtencion = new ArrayList<DtoNivelReporte>();
			List<DtoNivelReporte> nivelesAtencion = new ArrayList<DtoNivelReporte>();

			Calendar fechaInicio = Calendar.getInstance();
			Calendar fechaFin = Calendar.getInstance();
			fechaInicio.set(Calendar.YEAR, mesAnio.get(Calendar.YEAR));
			fechaInicio.set(Calendar.MONTH, mesAnio.get(Calendar.MONTH));
			fechaInicio.set(Calendar.DAY_OF_MONTH, mesAnio.getActualMinimum(Calendar.DAY_OF_MONTH));
			fechaFin.set(Calendar.YEAR, mesAnio.get(Calendar.YEAR));
			fechaFin.set(Calendar.MONTH, mesAnio.get(Calendar.MONTH));
			fechaFin.set(Calendar.DAY_OF_MONTH, mesAnio.getActualMaximum(Calendar.DAY_OF_MONTH));

			String tipoConvenio="";
			if(esCapitacionSubcontratada==ConstantesBD.acronimoSiChar){
				tipoConvenio=messageResource.getMessage(
				"consultarImprimirOrdenesCapitacionSubcontratada.convenioAutorizacion");
			}
			else if(esCapitacionSubcontratada==ConstantesBD.acronimoNoChar){
				tipoConvenio=messageResource.getMessage(
				"consultarImprimirOrdenesCapitacionSubcontratada.convenioSinAutorizacion");
			}
			DtoConvenioReporte dtoConvenio = new DtoConvenioReporte();
			dtoConvenio.setTipoConvenio(tipoConvenio);
			dtoConvenio.setNombre(convenio.getNombre());
			DtoContratoReporte dtoContrato = new DtoContratoReporte();
			dtoContrato.setNumeroContrato(contrato.getNumeroContrato());
			ParamPresupuestosCap presupuestoContrato=presupuestoMundo
			.obtenerParametrizacionPresupuestoCapitado(contrato.getCodigo(), String.valueOf(mesAnio.get(Calendar.YEAR)));
			if(presupuestoContrato==null){
				UtilidadTransaccion.getTransaccion().commit();
				return new ArrayList<DtoConvenioReporte>();
			}
			dtoContrato.setValorMensual(presupuestoContrato.getValorContrato());
			dtoContrato.setPorcentajeGastoMensual(presupuestoContrato.getPorcentajeGastoGeneral());
			dtoContrato.setValorGastoMensual(presupuestoContrato.getValorGastoGeneral());

			nivelesAtencion=nivelAtencionMundo
			.listarNivelesAtencionConParametrizacionPresupuestoPorContrato(
					contrato.getCodigo(), mesAnio);
			for(DtoNivelReporte nivelAtencion:nivelesAtencion){
				DtoNivelReporte dtoNivel = new DtoNivelReporte();
				DtoTotalProceso dtoTotalServicio = new DtoTotalProceso();
				DtoTotalProceso dtoTotalArticulo = new DtoTotalProceso();
				dtoNivel.setNombre(nivelAtencion.getNombre());
				dtoNivel.setTotalPresupuestado(nivelAtencion.getTotalPresupuestado());
				//Se obtiene el total ordenado de servicios y articulos
				dtoTotalServicio=cierreMundo.obtenerTotalServiciosPorNivelPorProceso(contrato.getCodigo(),
						nivelAtencion.getConsecutivo(), ConstantesIntegridadDominio.acronimoTipoProcesoOrden,
						fechaInicio.getTime(), fechaFin.getTime()).get(0);
				dtoTotalArticulo=cierreMundo.obtenerTotalArticulosPorNivelPorProceso(contrato.getCodigo(),
						nivelAtencion.getConsecutivo(), ConstantesIntegridadDominio.acronimoTipoProcesoOrden,
						fechaInicio.getTime(), fechaFin.getTime()).get(0);
				dtoNivel.setTotalOrdenado(dtoTotalServicio.getTotalProceso().add(dtoTotalArticulo.getTotalProceso()));
				if(esCapitacionSubcontratada==ConstantesBD.acronimoSiChar){
					//Se obtiene el total Autorizado de servicios y articulos
					dtoTotalServicio=cierreMundo.obtenerTotalServiciosPorNivelPorProceso(contrato.getCodigo(),
							nivelAtencion.getConsecutivo(), ConstantesIntegridadDominio.acronimoTipoProcesoAutorizacion,
							fechaInicio.getTime(), fechaFin.getTime()).get(0);
					dtoTotalArticulo=cierreMundo.obtenerTotalArticulosPorNivelPorProceso(contrato.getCodigo(),
							nivelAtencion.getConsecutivo(), ConstantesIntegridadDominio.acronimoTipoProcesoAutorizacion,
							fechaInicio.getTime(), fechaFin.getTime()).get(0);
					dtoNivel.setTotalAutorizado(dtoTotalServicio.getTotalProceso().add(dtoTotalArticulo.getTotalProceso()));
				}
				//Se obtiene el total Cargos a la cuenta de servicios y articulos
				dtoTotalServicio=cierreMundo.obtenerTotalServiciosPorNivelPorProceso(contrato.getCodigo(),
						nivelAtencion.getConsecutivo(), ConstantesIntegridadDominio.acronimoTipoProcesoCargoCuenta,
						fechaInicio.getTime(), fechaFin.getTime()).get(0);
				dtoTotalArticulo=cierreMundo.obtenerTotalArticulosPorNivelPorProceso(contrato.getCodigo(),
						nivelAtencion.getConsecutivo(), ConstantesIntegridadDominio.acronimoTipoProcesoCargoCuenta,
						fechaInicio.getTime(), fechaFin.getTime()).get(0);
				dtoNivel.setTotalCargosCuenta(dtoTotalServicio.getTotalProceso().add(dtoTotalArticulo.getTotalProceso()));
				//Se obtiene el total Facturado de servicios y articulos
				dtoTotalServicio=cierreMundo.obtenerTotalServiciosPorNivelPorProceso(contrato.getCodigo(),
						nivelAtencion.getConsecutivo(), ConstantesIntegridadDominio.acronimoTipoProcesoFacturacion,
						fechaInicio.getTime(), fechaFin.getTime()).get(0);
				dtoTotalArticulo=cierreMundo.obtenerTotalArticulosPorNivelPorProceso(contrato.getCodigo(),
						nivelAtencion.getConsecutivo(), ConstantesIntegridadDominio.acronimoTipoProcesoFacturacion,
						fechaInicio.getTime(), fechaFin.getTime()).get(0);
				dtoNivel.setTotalFacturado(dtoTotalServicio.getTotalProceso().add(dtoTotalArticulo.getTotalProceso()));

				dtoNivelesAtencion.add(dtoNivel);
			}
			dtoContrato.setNivelesAtencion(dtoNivelesAtencion);
			dtoContratos.add(dtoContrato);
			dtoConvenio.setContratos(dtoContratos);
			dtoConvenios.add(dtoConvenio);
			HibernateUtil.endTransaction();
			return dtoConvenios;
		}catch (Exception e) {
			HibernateUtil.abortTransaction();
		}
		return null;
	}

	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IOrdenCapitacionMundo#obtenerConsolidadoConvenioPorConvenio(java.util.Calendar, com.servinte.axioma.orm.Convenios, char)
	 */
	@Override
	public DtoConvenioReporte obtenerConsolidadoConvenioPorConvenio(
			Calendar mesAnio, Convenios convenio, char esCapitacionSubcontratada) {
		try{
			HibernateUtil.beginTransaction();
			IContratoMundo contratoMundo = FacturacionFabricaMundo.crearContratoMundo();
			IParametrizacionPresupuestoCapitacionMundo presupuestoMundo = CapitacionFabricaMundo.crearParametrizacionPresupuestoCapitacionMundo();
			INivelAtencionMundo nivelAtencionMundo = CapitacionFabricaMundo.crearNivelAtencionMundo();
			ICierrePresupuestoCapitacionoMundo cierreMundo = CapitacionFabricaMundo.crearCierrePresupuestoCapitacionoMundo();
			IValorizacionPresupuestoCapGeneralMundo valorizacionMundo = CapitacionFabricaMundo.crearValorizacionPresupuestoCapGeneralMundo();

			ArrayList<DtoContratoReporte> dtoContratos = new ArrayList<DtoContratoReporte>();
			ArrayList<Contratos> contratos = new ArrayList<Contratos>();
			List<DtoNivelReporte> nivelesAtencion = new ArrayList<DtoNivelReporte>();
			String servicios=messageResource.getMessage(
			"consultarImprimirOrdenesCapitacionSubcontratada.servicios");
			String medicamentosInsumos=messageResource.getMessage(
			"consultarImprimirOrdenesCapitacionSubcontratada.medicamentosInsumos");

			Calendar fechaInicio = Calendar.getInstance();
			Calendar fechaFin = Calendar.getInstance();
			fechaInicio.set(Calendar.YEAR, mesAnio.get(Calendar.YEAR));
			fechaInicio.set(Calendar.MONTH, mesAnio.get(Calendar.MONTH));
			fechaInicio.set(Calendar.DAY_OF_MONTH, mesAnio.getActualMinimum(Calendar.DAY_OF_MONTH));
			fechaFin.set(Calendar.YEAR, mesAnio.get(Calendar.YEAR));
			fechaFin.set(Calendar.MONTH, mesAnio.get(Calendar.MONTH));
			fechaFin.set(Calendar.DAY_OF_MONTH, mesAnio.getActualMaximum(Calendar.DAY_OF_MONTH));

			DtoConvenioReporte dtoConvenio = new DtoConvenioReporte();
			dtoConvenio.setNombre(convenio.getNombre());
			contratos=contratoMundo
			.listarContratosConParametrizacionPresupuestoPorConvenio(
					convenio.getCodigo(), mesAnio);
			for(Contratos contrato:contratos){
				DtoContratoReporte dtoContrato = new DtoContratoReporte();
				ArrayList<DtoNivelReporte> dtoNivelesAtencion = new ArrayList<DtoNivelReporte>();
				dtoContrato.setNumeroContrato(contrato.getNumeroContrato());
				ParamPresupuestosCap presupuestoContrato=presupuestoMundo
				.obtenerParametrizacionPresupuestoCapitado(contrato.getCodigo(), String.valueOf(mesAnio.get(Calendar.YEAR)));
				if(presupuestoContrato==null){
					UtilidadTransaccion.getTransaccion().commit();
					return new DtoConvenioReporte();
				}
				dtoContrato.setValorMensual(presupuestoContrato.getValorContrato());
				dtoContrato.setPorcentajeGastoMensual(presupuestoContrato.getPorcentajeGastoGeneral());
				dtoContrato.setValorGastoMensual(presupuestoContrato.getValorGastoGeneral());

				nivelesAtencion=nivelAtencionMundo
				.listarNivelesAtencionConParametrizacionPresupuestoPorContrato(
						contrato.getCodigo(), mesAnio);
				for(DtoNivelReporte nivelAtencion:nivelesAtencion){
					ArrayList<DtoGrupoClaseReporte> dtoGruposClases = new ArrayList<DtoGrupoClaseReporte>();
					DtoNivelReporte dtoNivel = new DtoNivelReporte();
					DtoGrupoClaseReporte dtoServicios = new DtoGrupoClaseReporte();
					DtoGrupoClaseReporte dtoMedicamentos = new DtoGrupoClaseReporte();
					DtoTotalProceso dtoTotalServicio = new DtoTotalProceso();
					DtoTotalProceso dtoTotalArticulo = new DtoTotalProceso();
					dtoNivel.setNombre(nivelAtencion.getNombre());
					dtoMedicamentos.setNombre(medicamentosInsumos);
					dtoServicios.setNombre(servicios);
					//Se obtiene el total presupuestado de servicios
					dtoTotalServicio=valorizacionMundo
					.obtenerPresupuestoGrupoClasePorNivelAtencionPorContrato(
							contrato.getCodigo(), nivelAtencion.getConsecutivo(),
							mesAnio, ConstantesCapitacion.subSeccionServicio);
					dtoServicios.setTotalPresupuestado(dtoTotalServicio.getTotalProceso());
					//Se obtiene el total ordenado de servicios
					dtoTotalServicio=cierreMundo.obtenerTotalServiciosPorNivelPorProceso(contrato.getCodigo(),
							nivelAtencion.getConsecutivo(), ConstantesIntegridadDominio.acronimoTipoProcesoOrden,
							fechaInicio.getTime(), fechaFin.getTime()).get(0);
					dtoServicios.setTotalOrdenado(dtoTotalServicio.getTotalProceso());
					if(esCapitacionSubcontratada==ConstantesBD.acronimoSiChar){
						//Se obtiene el total Autorizado de servicios
						dtoTotalServicio=cierreMundo.obtenerTotalServiciosPorNivelPorProceso(contrato.getCodigo(),
								nivelAtencion.getConsecutivo(), ConstantesIntegridadDominio.acronimoTipoProcesoAutorizacion,
								fechaInicio.getTime(), fechaFin.getTime()).get(0);
						dtoServicios.setTotalAutorizado(dtoTotalServicio.getTotalProceso());
					}
					//Se obtiene el total Cargos a la cuenta de servicios
					dtoTotalServicio=cierreMundo.obtenerTotalServiciosPorNivelPorProceso(contrato.getCodigo(),
							nivelAtencion.getConsecutivo(), ConstantesIntegridadDominio.acronimoTipoProcesoCargoCuenta,
							fechaInicio.getTime(), fechaFin.getTime()).get(0);
					dtoServicios.setTotalCargosCuenta(dtoTotalServicio.getTotalProceso());
					//Se obtiene el total Facturado de servicios
					dtoTotalServicio=cierreMundo.obtenerTotalServiciosPorNivelPorProceso(contrato.getCodigo(),
							nivelAtencion.getConsecutivo(), ConstantesIntegridadDominio.acronimoTipoProcesoFacturacion,
							fechaInicio.getTime(), fechaFin.getTime()).get(0);
					dtoServicios.setTotalFacturado(dtoTotalServicio.getTotalProceso());
					dtoGruposClases.add(dtoServicios);
					//Se obtiene el total presupuestado de los articulos
					dtoTotalArticulo=valorizacionMundo
					.obtenerPresupuestoGrupoClasePorNivelAtencionPorContrato(
							contrato.getCodigo(), nivelAtencion.getConsecutivo(),
							mesAnio, ConstantesCapitacion.subSeccionArticulo);
					dtoMedicamentos.setTotalPresupuestado(dtoTotalArticulo.getTotalProceso());
					//Se obtiene el total Ordenado de los articulos
					dtoTotalArticulo=cierreMundo.obtenerTotalArticulosPorNivelPorProceso(contrato.getCodigo(),
							nivelAtencion.getConsecutivo(), ConstantesIntegridadDominio.acronimoTipoProcesoOrden,
							fechaInicio.getTime(), fechaFin.getTime()).get(0);
					dtoMedicamentos.setTotalOrdenado(dtoTotalArticulo.getTotalProceso());
					if(esCapitacionSubcontratada==ConstantesBD.acronimoSiChar){
						//Se obtiene el total Autorizado de los articulos
						dtoTotalArticulo=cierreMundo.obtenerTotalArticulosPorNivelPorProceso(contrato.getCodigo(),
								nivelAtencion.getConsecutivo(), ConstantesIntegridadDominio.acronimoTipoProcesoAutorizacion,
								fechaInicio.getTime(), fechaFin.getTime()).get(0);
						dtoMedicamentos.setTotalAutorizado(dtoTotalArticulo.getTotalProceso());

					}
					//Se obtiene el total Cargos a la Cuenta de los articulos
					dtoTotalArticulo=cierreMundo.obtenerTotalArticulosPorNivelPorProceso(contrato.getCodigo(),
							nivelAtencion.getConsecutivo(), ConstantesIntegridadDominio.acronimoTipoProcesoCargoCuenta,
							fechaInicio.getTime(), fechaFin.getTime()).get(0);
					dtoMedicamentos.setTotalCargosCuenta(dtoTotalArticulo.getTotalProceso());
					//Se obtiene el total Facturado de los articulos
					dtoTotalArticulo=cierreMundo.obtenerTotalArticulosPorNivelPorProceso(contrato.getCodigo(),
							nivelAtencion.getConsecutivo(), ConstantesIntegridadDominio.acronimoTipoProcesoFacturacion,
							fechaInicio.getTime(), fechaFin.getTime()).get(0);
					dtoMedicamentos.setTotalFacturado(dtoTotalArticulo.getTotalProceso());
					dtoGruposClases.add(dtoMedicamentos);
					dtoNivel.setGruposClases(dtoGruposClases);
					dtoNivelesAtencion.add(dtoNivel);
				}
				dtoContrato.setNivelesAtencion(dtoNivelesAtencion);
				dtoContratos.add(dtoContrato);
			}
			dtoConvenio.setContratos(dtoContratos);
			HibernateUtil.endTransaction();
			return dtoConvenio;
		}catch (Exception e) {
			HibernateUtil.abortTransaction();
		}
		return null;
	}

	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IOrdenCapitacionMundo#obtenerConsolidadoConvenioPorConvenioContrato(java.util.Calendar, com.servinte.axioma.orm.Convenios, com.servinte.axioma.orm.Contratos, char)
	 */
	@Override
	public DtoConvenioReporte obtenerConsolidadoConvenioPorConvenioContrato(
			Calendar mesAnio, Convenios convenio, Contratos contrato, char esCapitacionSubcontratada) {
		try{
			HibernateUtil.beginTransaction();

			IParametrizacionPresupuestoCapitacionMundo presupuestoMundo = CapitacionFabricaMundo.crearParametrizacionPresupuestoCapitacionMundo();
			INivelAtencionMundo nivelAtencionMundo = CapitacionFabricaMundo.crearNivelAtencionMundo();
			ICierrePresupuestoCapitacionoMundo cierreMundo = CapitacionFabricaMundo.crearCierrePresupuestoCapitacionoMundo();
			IValorizacionPresupuestoCapGeneralMundo valorizacionMundo = CapitacionFabricaMundo.crearValorizacionPresupuestoCapGeneralMundo();

			ArrayList<DtoContratoReporte> dtoContratos = new ArrayList<DtoContratoReporte>();
			List<DtoNivelReporte> nivelesAtencion = new ArrayList<DtoNivelReporte>();
			String servicios=messageResource.getMessage(
			"consultarImprimirOrdenesCapitacionSubcontratada.servicios");
			String medicamentosInsumos=messageResource.getMessage(
			"consultarImprimirOrdenesCapitacionSubcontratada.medicamentosInsumos");

			Calendar fechaInicio = Calendar.getInstance();
			Calendar fechaFin = Calendar.getInstance();
			fechaInicio.set(Calendar.YEAR, mesAnio.get(Calendar.YEAR));
			fechaInicio.set(Calendar.MONTH, mesAnio.get(Calendar.MONTH));
			fechaInicio.set(Calendar.DAY_OF_MONTH, mesAnio.getActualMinimum(Calendar.DAY_OF_MONTH));
			fechaFin.set(Calendar.YEAR, mesAnio.get(Calendar.YEAR));
			fechaFin.set(Calendar.MONTH, mesAnio.get(Calendar.MONTH));
			fechaFin.set(Calendar.DAY_OF_MONTH, mesAnio.getActualMaximum(Calendar.DAY_OF_MONTH));

			DtoConvenioReporte dtoConvenio = new DtoConvenioReporte();
			dtoConvenio.setNombre(convenio.getNombre());
			DtoContratoReporte dtoContrato = new DtoContratoReporte();
			ArrayList<DtoNivelReporte> dtoNivelesAtencion = new ArrayList<DtoNivelReporte>();
			dtoContrato.setNumeroContrato(contrato.getNumeroContrato());
			ParamPresupuestosCap presupuestoContrato=presupuestoMundo
			.obtenerParametrizacionPresupuestoCapitado(contrato.getCodigo(), String.valueOf(mesAnio.get(Calendar.YEAR)));
			if(presupuestoContrato==null){
				UtilidadTransaccion.getTransaccion().commit();
				return new DtoConvenioReporte();
			}
			dtoContrato.setValorMensual(presupuestoContrato.getValorContrato());
			dtoContrato.setPorcentajeGastoMensual(presupuestoContrato.getPorcentajeGastoGeneral());
			dtoContrato.setValorGastoMensual(presupuestoContrato.getValorGastoGeneral());

			nivelesAtencion=nivelAtencionMundo
			.listarNivelesAtencionConParametrizacionPresupuestoPorContrato(
					contrato.getCodigo(), mesAnio);
			for(DtoNivelReporte nivelAtencion:nivelesAtencion){
				ArrayList<DtoGrupoClaseReporte> dtoGruposClases = new ArrayList<DtoGrupoClaseReporte>();
				DtoNivelReporte dtoNivel = new DtoNivelReporte();
				DtoGrupoClaseReporte dtoServicios = new DtoGrupoClaseReporte();
				DtoGrupoClaseReporte dtoMedicamentos = new DtoGrupoClaseReporte();
				DtoTotalProceso dtoTotalServicio = new DtoTotalProceso();
				DtoTotalProceso dtoTotalArticulo = new DtoTotalProceso();
				dtoNivel.setNombre(nivelAtencion.getNombre());
				dtoMedicamentos.setNombre(medicamentosInsumos);
				dtoServicios.setNombre(servicios);
				//Se obtiene el total presupuestado de servicios
				dtoTotalServicio=valorizacionMundo
				.obtenerPresupuestoGrupoClasePorNivelAtencionPorContrato(
						contrato.getCodigo(), nivelAtencion.getConsecutivo(),
						mesAnio, ConstantesCapitacion.subSeccionServicio);
				dtoServicios.setTotalPresupuestado(dtoTotalServicio.getTotalProceso());
				//Se obtiene el total ordenado de servicios
				dtoTotalServicio=cierreMundo.obtenerTotalServiciosPorNivelPorProceso(contrato.getCodigo(),
						nivelAtencion.getConsecutivo(), ConstantesIntegridadDominio.acronimoTipoProcesoOrden,
						fechaInicio.getTime(), fechaFin.getTime()).get(0);
				dtoServicios.setTotalOrdenado(dtoTotalServicio.getTotalProceso());
				if(esCapitacionSubcontratada==ConstantesBD.acronimoSiChar){
					//Se obtiene el total Autorizado de servicios
					dtoTotalServicio=cierreMundo.obtenerTotalServiciosPorNivelPorProceso(contrato.getCodigo(),
							nivelAtencion.getConsecutivo(), ConstantesIntegridadDominio.acronimoTipoProcesoAutorizacion,
							fechaInicio.getTime(), fechaFin.getTime()).get(0);
					dtoServicios.setTotalAutorizado(dtoTotalServicio.getTotalProceso());
				}
				//Se obtiene el total Cargos a la cuenta de servicios
				dtoTotalServicio=cierreMundo.obtenerTotalServiciosPorNivelPorProceso(contrato.getCodigo(),
						nivelAtencion.getConsecutivo(), ConstantesIntegridadDominio.acronimoTipoProcesoCargoCuenta,
						fechaInicio.getTime(), fechaFin.getTime()).get(0);
				dtoServicios.setTotalCargosCuenta(dtoTotalServicio.getTotalProceso());
				//Se obtiene el total Facturado de servicios
				dtoTotalServicio=cierreMundo.obtenerTotalServiciosPorNivelPorProceso(contrato.getCodigo(),
						nivelAtencion.getConsecutivo(), ConstantesIntegridadDominio.acronimoTipoProcesoFacturacion,
						fechaInicio.getTime(), fechaFin.getTime()).get(0);
				dtoServicios.setTotalFacturado(dtoTotalServicio.getTotalProceso());
				dtoGruposClases.add(dtoServicios);
				//Se obtiene el total presupuestado de los articulos
				dtoTotalArticulo=valorizacionMundo
				.obtenerPresupuestoGrupoClasePorNivelAtencionPorContrato(
						contrato.getCodigo(), nivelAtencion.getConsecutivo(),
						mesAnio, ConstantesCapitacion.subSeccionArticulo);
				dtoMedicamentos.setTotalPresupuestado(dtoTotalArticulo.getTotalProceso());
				//Se obtiene el total Ordenado de los articulos
				dtoTotalArticulo=cierreMundo.obtenerTotalArticulosPorNivelPorProceso(contrato.getCodigo(),
						nivelAtencion.getConsecutivo(), ConstantesIntegridadDominio.acronimoTipoProcesoOrden,
						fechaInicio.getTime(), fechaFin.getTime()).get(0);
				dtoMedicamentos.setTotalOrdenado(dtoTotalArticulo.getTotalProceso());
				if(esCapitacionSubcontratada==ConstantesBD.acronimoSiChar){
					//Se obtiene el total Autorizado de los articulos
					dtoTotalArticulo=cierreMundo.obtenerTotalArticulosPorNivelPorProceso(contrato.getCodigo(),
							nivelAtencion.getConsecutivo(), ConstantesIntegridadDominio.acronimoTipoProcesoAutorizacion,
							fechaInicio.getTime(), fechaFin.getTime()).get(0);
					dtoMedicamentos.setTotalAutorizado(dtoTotalArticulo.getTotalProceso());

				}
				//Se obtiene el total Cargos a la Cuenta de los articulos
				dtoTotalArticulo=cierreMundo.obtenerTotalArticulosPorNivelPorProceso(contrato.getCodigo(),
						nivelAtencion.getConsecutivo(), ConstantesIntegridadDominio.acronimoTipoProcesoCargoCuenta,
						fechaInicio.getTime(), fechaFin.getTime()).get(0);
				dtoMedicamentos.setTotalCargosCuenta(dtoTotalArticulo.getTotalProceso());
				//Se obtiene el total Facturado de los articulos
				dtoTotalArticulo=cierreMundo.obtenerTotalArticulosPorNivelPorProceso(contrato.getCodigo(),
						nivelAtencion.getConsecutivo(), ConstantesIntegridadDominio.acronimoTipoProcesoFacturacion,
						fechaInicio.getTime(), fechaFin.getTime()).get(0);
				dtoMedicamentos.setTotalFacturado(dtoTotalArticulo.getTotalProceso());
				dtoGruposClases.add(dtoMedicamentos);
				dtoNivel.setGruposClases(dtoGruposClases);
				dtoNivelesAtencion.add(dtoNivel);
			}
			dtoContrato.setNivelesAtencion(dtoNivelesAtencion);
			dtoContratos.add(dtoContrato);
			dtoConvenio.setContratos(dtoContratos);
			HibernateUtil.endTransaction();
			return dtoConvenio;
		}catch (Exception e) {
			HibernateUtil.abortTransaction();
		}
		return null;
	}

	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IOrdenCapitacionMundo#obtenerConsolidadoContratoPorConvenioContrato(java.util.Calendar, com.servinte.axioma.orm.Convenios, com.servinte.axioma.orm.Contratos, char)
	 */
	@Override
	public DtoContratoReporte obtenerConsolidadoContratoPorConvenioContrato(
			Calendar mesAnio, Convenios convenio, Contratos contrato, char esCapitacionSubcontratada) {
		try{
			HibernateUtil.beginTransaction();
		INivelAtencionMundo nivelAtencionMundo = CapitacionFabricaMundo.crearNivelAtencionMundo();
		ICierrePresupuestoCapitacionoMundo cierreMundo = CapitacionFabricaMundo.crearCierrePresupuestoCapitacionoMundo();
		IDetalleValorizacionServicioMundo detalleValorizacionServicioMundo = CapitacionFabricaMundo.crearDetalleValorizacionServicioMundo();
		IDetalleValorizacionArticuloMundo detalleValorizacionArticuloMundo = CapitacionFabricaMundo.crearDetalleValorizacionArticuloMundo();
		
		DtoContratoReporte dtoContrato = new DtoContratoReporte();
		List<DtoNivelAtencion> nivelesAtencion = new ArrayList<DtoNivelAtencion>();
		List<DtoProductoServicioReporte> servicios = new ArrayList<DtoProductoServicioReporte>();
		List<DtoProductoServicioReporte> articulos = new ArrayList<DtoProductoServicioReporte>();
		String gruposServicio=messageResource.getMessage(
							"consultarImprimirOrdenesCapitacionSubcontratada.gruposServicio");
		String claseInventarios=messageResource.getMessage(
							"consultarImprimirOrdenesCapitacionSubcontratada.claseInventarios");
		
		Calendar fechaInicio = Calendar.getInstance();
		Calendar fechaFin = Calendar.getInstance();
		fechaInicio.set(Calendar.YEAR, mesAnio.get(Calendar.YEAR));
		fechaInicio.set(Calendar.MONTH, mesAnio.get(Calendar.MONTH));
		fechaInicio.set(Calendar.DAY_OF_MONTH, mesAnio.getActualMinimum(Calendar.DAY_OF_MONTH));
		fechaFin.set(Calendar.YEAR, mesAnio.get(Calendar.YEAR));
		fechaFin.set(Calendar.MONTH, mesAnio.get(Calendar.MONTH));
		fechaFin.set(Calendar.DAY_OF_MONTH, mesAnio.getActualMaximum(Calendar.DAY_OF_MONTH));
		
			
			ArrayList<DtoNivelReporte> dtoNivelesAtencion = new ArrayList<DtoNivelReporte>();
			dtoContrato.setNumeroContrato(contrato.getNumeroContrato());
			nivelesAtencion=nivelAtencionMundo
								.listarNivelesAtencionConParametrizacionDetalladaPresupuestoPorContrato(
										contrato.getCodigo(), mesAnio);
			for(DtoNivelAtencion nivelAtencion:nivelesAtencion){
				ArrayList<DtoGrupoClaseReporte> dtoGruposClases = new ArrayList<DtoGrupoClaseReporte>();
				DtoNivelReporte dtoNivel = new DtoNivelReporte();
				DtoGrupoClaseReporte dtoGrupos = new DtoGrupoClaseReporte();
				DtoGrupoClaseReporte dtoClases = new DtoGrupoClaseReporte();
				DtoTotalProceso dtoTotalServicio = new DtoTotalProceso();
				DtoTotalProceso dtoTotalArticulo = new DtoTotalProceso();
				dtoNivel.setNombre(nivelAtencion.getDescripcion());
				//Se obtienen los grupos de servicios presupuestados
				servicios=detalleValorizacionServicioMundo
									.obtenerServiciosPresupuestadosPorNivelAtencionPorContrato(
											contrato.getCodigo(), nivelAtencion.getConsecutivo(),
											mesAnio);
				if(servicios != null && !servicios.isEmpty()){
					ArrayList<DtoProductoServicioReporte> dtoServicios = new ArrayList<DtoProductoServicioReporte>();
					dtoGrupos.setNombre(gruposServicio);
					for(DtoProductoServicioReporte servicio:servicios){
						DtoProductoServicioReporte dtoServicio = new DtoProductoServicioReporte();
						dtoServicio.setNombre(servicio.getNombre());
						dtoServicio.setTotalPresupuestado(servicio.getTotalPresupuestado());
						//Se obtiene el total ordenado de servicios
						dtoTotalServicio=cierreMundo.obtenerTotalServiciosPorNivelPorGrupoPorProceso(contrato.getCodigo(),
									nivelAtencion.getConsecutivo(), servicio.getCodigo(), ConstantesIntegridadDominio.acronimoTipoProcesoOrden,
									fechaInicio.getTime(), fechaFin.getTime()).get(0);
						dtoServicio.setTotalOrdenado(dtoTotalServicio.getTotalProceso());
						if(esCapitacionSubcontratada==ConstantesBD.acronimoSiChar){
							//Se obtiene el total Autorizado de servicios
							dtoTotalServicio=cierreMundo.obtenerTotalServiciosPorNivelPorGrupoPorProceso(contrato.getCodigo(),
									nivelAtencion.getConsecutivo(), servicio.getCodigo(), ConstantesIntegridadDominio.acronimoTipoProcesoAutorizacion,
									fechaInicio.getTime(), fechaFin.getTime()).get(0);
							dtoServicio.setTotalAutorizado(dtoTotalServicio.getTotalProceso());
						}
						//Se obtiene el total Cargos a la cuenta de servicios
						dtoTotalServicio=cierreMundo.obtenerTotalServiciosPorNivelPorGrupoPorProceso(contrato.getCodigo(),
								nivelAtencion.getConsecutivo(), servicio.getCodigo(), ConstantesIntegridadDominio.acronimoTipoProcesoCargoCuenta,
								fechaInicio.getTime(), fechaFin.getTime()).get(0);
						dtoServicio.setTotalCargosCuenta(dtoTotalServicio.getTotalProceso());
						//Se obtiene el total Facturado de servicios
						dtoTotalServicio=cierreMundo.obtenerTotalServiciosPorNivelPorGrupoPorProceso(contrato.getCodigo(),
								nivelAtencion.getConsecutivo(), servicio.getCodigo(), ConstantesIntegridadDominio.acronimoTipoProcesoFacturacion,
								fechaInicio.getTime(), fechaFin.getTime()).get(0);
						dtoServicio.setTotalFacturado(dtoTotalServicio.getTotalProceso());
						dtoServicios.add(dtoServicio);	
					}
					dtoGrupos.setProductosServicios(dtoServicios);
					dtoGruposClases.add(dtoGrupos);
				}
				//Se obtienen las clases de inventarios presupuestados
				articulos=detalleValorizacionArticuloMundo
									.obtenerArticulosPresupuestadosPorNivelAtencionPorContrato(
											contrato.getCodigo(), nivelAtencion.getConsecutivo(),
											mesAnio);
				if(articulos != null && !articulos.isEmpty()){
					ArrayList<DtoProductoServicioReporte> dtoArticulos = new ArrayList<DtoProductoServicioReporte>();
					dtoClases.setNombre(claseInventarios);
					for(DtoProductoServicioReporte articulo:articulos){
						DtoProductoServicioReporte dtoArticulo = new DtoProductoServicioReporte();
						dtoArticulo.setNombre(articulo.getNombre());
						dtoArticulo.setTotalPresupuestado(articulo.getTotalPresupuestado());
						//Se obtiene el total ordenado de articulos
						dtoTotalArticulo=cierreMundo.obtenerTotalArticulosPorNivelPorClasePorProceso(contrato.getCodigo(),
									nivelAtencion.getConsecutivo(), articulo.getCodigo(), ConstantesIntegridadDominio.acronimoTipoProcesoOrden,
									fechaInicio.getTime(), fechaFin.getTime()).get(0);
						dtoArticulo.setTotalOrdenado(dtoTotalArticulo.getTotalProceso());
						if(esCapitacionSubcontratada==ConstantesBD.acronimoSiChar){
							//Se obtiene el total Autorizado de articulos
							dtoTotalArticulo=cierreMundo.obtenerTotalArticulosPorNivelPorClasePorProceso(contrato.getCodigo(),
									nivelAtencion.getConsecutivo(), articulo.getCodigo(), ConstantesIntegridadDominio.acronimoTipoProcesoAutorizacion,
									fechaInicio.getTime(), fechaFin.getTime()).get(0);
							dtoArticulo.setTotalAutorizado(dtoTotalArticulo.getTotalProceso());
						}
						//Se obtiene el total Cargos a la cuenta de articulos
						dtoTotalArticulo=cierreMundo.obtenerTotalArticulosPorNivelPorClasePorProceso(contrato.getCodigo(),
								nivelAtencion.getConsecutivo(), articulo.getCodigo(), ConstantesIntegridadDominio.acronimoTipoProcesoCargoCuenta,
								fechaInicio.getTime(), fechaFin.getTime()).get(0);
						dtoArticulo.setTotalCargosCuenta(dtoTotalArticulo.getTotalProceso());
						//Se obtiene el total Facturado de articulos
						dtoTotalArticulo=cierreMundo.obtenerTotalArticulosPorNivelPorClasePorProceso(contrato.getCodigo(),
								nivelAtencion.getConsecutivo(), articulo.getCodigo(), ConstantesIntegridadDominio.acronimoTipoProcesoFacturacion,
								fechaInicio.getTime(), fechaFin.getTime()).get(0);
						dtoArticulo.setTotalFacturado(dtoTotalArticulo.getTotalProceso());
						dtoArticulos.add(dtoArticulo);	
					}
					dtoClases.setProductosServicios(dtoArticulos);
					dtoGruposClases.add(dtoClases);
				}				
				dtoNivel.setGruposClases(dtoGruposClases);
				dtoNivelesAtencion.add(dtoNivel);
			}
			dtoContrato.setNivelesAtencion(dtoNivelesAtencion);
			HibernateUtil.endTransaction();
		return dtoContrato;
		}catch (Exception e) {
			HibernateUtil.abortTransaction();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IOrdenCapitacionMundo#existeParametrizacionPresupuesto(int, java.lang.String)
	 */
	@Override
	public boolean existeParametrizacionPresupuesto(int codigoContrato,
			String anioVigencia) {
		boolean existe=false;
		try{
			HibernateUtil.beginTransaction();
			IParametrizacionPresupuestoCapitacionMundo parametrizacionMundo = CapitacionFabricaMundo.crearParametrizacionPresupuestoCapitacionMundo();
			existe=parametrizacionMundo.existeParametrizacionPresupuesto(codigoContrato, anioVigencia);
			HibernateUtil.endTransaction();

		}catch (Exception e) {
			HibernateUtil.abortTransaction();
		}
		return existe;
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IOrdenCapitacionMundo#existeParametrizacionPresupuestoConvenio(int, java.lang.String)
	 */
	@Override
	public boolean existeParametrizacionPresupuestoConvenio(int codigoConvenio,
			String anioVigencia) {
		boolean existe=false;
		try{
			HibernateUtil.beginTransaction();

			IParametrizacionPresupuestoCapitacionMundo parametrizacionMundo = CapitacionFabricaMundo.crearParametrizacionPresupuestoCapitacionMundo();
			existe=parametrizacionMundo.existeParametrizacionPresupuestoConvenio(codigoConvenio, anioVigencia);
			HibernateUtil.endTransaction();

		}catch (Exception e) {
			HibernateUtil.abortTransaction();
		}
		return existe;
	}
	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IOrdenCapitacionMundo#existeParametrizacionDetalladaPresupuesto(int, java.util.Calendar)
	 */
	@Override
	public boolean existeParametrizacionDetalladaPresupuesto(int codigoContrato, Calendar anioMes) {
		boolean existe=false;
		try{
			HibernateUtil.beginTransaction();
			IDetalleValorizacionArticuloMundo valorizacionArticuloMundo = CapitacionFabricaMundo.crearDetalleValorizacionArticuloMundo();
			IDetalleValorizacionServicioMundo valorizacionServicioMundo = CapitacionFabricaMundo.crearDetalleValorizacionServicioMundo();
			boolean existeGrupo=valorizacionServicioMundo.existeValorizacionDetalleGrupoServicio(codigoContrato, anioMes);
			boolean existeClase=valorizacionArticuloMundo.existeValorizacionDetalleClaseInventario(codigoContrato, anioMes);
			UtilidadTransaccion.getTransaccion().commit();

			if(existeGrupo && existeClase){
				existe=true;
			}
			HibernateUtil.endTransaction();
			
		}catch (Exception e) {
			HibernateUtil.abortTransaction();
		}
		return existe;
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IOrdenCapitacionMundo#obtenerParametrizacionContrato(int, java.util.Calendar)
	 */
	@Override
	public ParamPresupuestosCap obtenerParametrizacionContrato(int codigoContrato,
			Calendar anioMes) {
		IParametrizacionPresupuestoCapitacionMundo presupuestoMundo = CapitacionFabricaMundo.crearParametrizacionPresupuestoCapitacionMundo();
		ParamPresupuestosCap presupuestoContrato=null;
		try{
			HibernateUtil.beginTransaction();
		
		presupuestoContrato=presupuestoMundo
			.obtenerParametrizacionPresupuestoCapitado(codigoContrato, String.valueOf(anioMes.get(Calendar.YEAR)));
		HibernateUtil.endTransaction();
		}catch (Exception e) {
			HibernateUtil.abortTransaction();
		}
		return presupuestoContrato;
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IOrdenCapitacionMundo#validarCierresDiarios(java.util.List)
	 */
	@Override
	public List<DtoDiaCierre> validarCierresDiarios(List<DtoDiaCierre> diasCierre) {
		ICierrePresupuestoCapitacionoMundo cierrePresupuestoMundo = CapitacionFabricaMundo.crearCierrePresupuestoCapitacionoMundo();
		try{
			HibernateUtil.beginTransaction();

			for(DtoDiaCierre dia:diasCierre){
				DtoTotalProcesoPresupuestoCapitado params = new DtoTotalProcesoPresupuestoCapitado();
				DtoProcesoPresupuestoCapitado paramsLog = new DtoProcesoPresupuestoCapitado();
				params.setFecha(dia.getFechaCierre());
				paramsLog.setFechaCierre(dia.getFechaCierre());
				List<DtoTotalProcesoPresupuestoCapitado> cierre=cierrePresupuestoMundo.obtenerCierresPresupuestocapitacion(params);
				boolean existeCierre=false;
				if(cierre != null && !cierre.isEmpty()){
					existeCierre=true;
				}
				else{
					ArrayList<DtoLogCierrePresuCapita> log=cierrePresupuestoMundo.obtenerLogs(paramsLog);
					if(log != null && !log.isEmpty()){
						existeCierre=true;
					}
				}
				dia.setTieneCierre(existeCierre);
			}
			HibernateUtil.endTransaction();

		}catch (Exception e) {
			HibernateUtil.abortTransaction();
		}
		return diasCierre;
	}

}
