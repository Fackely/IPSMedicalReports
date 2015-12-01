package com.servinte.axioma.mundo.impl.facturacion;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.struts.util.MessageResources;

import com.princetonsa.dto.capitacion.DtoLogCierrePresuCapita;
import com.princetonsa.dto.capitacion.DtoMesesTotalServiciosArticulosValorizadosPorConvenio;
import com.princetonsa.dto.capitacion.DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio;
import com.princetonsa.dto.capitacion.DtoProcesoPresupuestoCapitado;
import com.princetonsa.dto.capitacion.DtoTotalProcesoPresupuestoCapitado;
import com.servinte.axioma.dto.capitacion.DtoDiaCierre;
import com.servinte.axioma.mundo.fabrica.capitacion.CapitacionFabricaMundo;
import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.fabrica.inventario.InventarioMundoFabrica;
import com.servinte.axioma.mundo.interfaz.capitacion.ICierrePresupuestoCapitacionoMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.INivelAtencionMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IGruposServiciosMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IServiciosMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IValorizacionServiciosArticulosMundo;
import com.servinte.axioma.mundo.interfaz.inventario.IArticulosMundo;
import com.servinte.axioma.mundo.interfaz.inventario.IClaseInventarioMundo;
import com.servinte.axioma.orm.Articulo;
import com.servinte.axioma.orm.ClaseInventario;
import com.servinte.axioma.orm.Contratos;
import com.servinte.axioma.orm.Convenios;
import com.servinte.axioma.orm.GruposServicios;
import com.servinte.axioma.orm.NivelAtencion;
import com.servinte.axioma.orm.Servicios;
import com.servinte.axioma.persistencia.UtilidadTransaccion;

/**
 * Define la lógica de negocio relacionada con la
 *  Consulta del total de servicios y articulos valorizados
 * 
 * @version 1.0, May 19, 2011
 * @author <a href="mailto:ricardo.ruiz@servinte.com.co">Ricardo Ruiz</a>
 * 
 */
public class ValorizacionServiciosArticulosMundo implements IValorizacionServiciosArticulosMundo{

	/** * Contiene la lista de mensajes correspondiente a esta forma */
	MessageResources messageResource = MessageResources.getMessageResources("com.servinte.mensajes.facturacion.TotalServiciosArticulosValorizadosTipoConvenioForm");
	
	public ValorizacionServiciosArticulosMundo(){
		
	}
	
	
	@Override
	public List<DtoDiaCierre> validarCierresDiarios(List<DtoDiaCierre> diasCierre) {
		ICierrePresupuestoCapitacionoMundo cierrePresupuestoMundo = CapitacionFabricaMundo.crearCierrePresupuestoCapitacionoMundo();
		UtilidadTransaccion.getTransaccion().begin();
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
		UtilidadTransaccion.getTransaccion().commit();
		return diasCierre;
	}


	@Override
	public ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio> consultarConsolidadoServiciosPorNivelPorConvenio(
			Convenios convenio, String proceso, List<Calendar> mesesReporte) {
		INivelAtencionMundo nivelAtencionMundo = CapitacionFabricaMundo.crearNivelAtencionMundo();
		ICierrePresupuestoCapitacionoMundo cierreMundo = CapitacionFabricaMundo.crearCierrePresupuestoCapitacionoMundo();
		ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio> dtoNivelesAtencion = new ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio>();
		ArrayList<NivelAtencion> nivelesAtencion = new ArrayList<NivelAtencion>();
		UtilidadTransaccion.getTransaccion().begin();
		//Se consultan los distintos niveles de atención del convenio
		nivelesAtencion=nivelAtencionMundo.listarNivelesAtencionServiciosPorConvenio(convenio.getCodigo(), proceso, mesesReporte);
		if(nivelesAtencion != null && !nivelesAtencion.isEmpty()){
			for(NivelAtencion nivelAtencion: nivelesAtencion){
				DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio dtoNivel = new DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio();
				dtoNivel.setNivelAtencion(nivelAtencion.getDescripcion());
				dtoNivel.setListaMesesTotales(cierreMundo.obtenerListadoMesesTotalServiciosPorNivelPorProceso(
															convenio.getCodigo(), nivelAtencion.getConsecutivo(), proceso, mesesReporte));
				dtoNivelesAtencion.add(dtoNivel);
			}
		}
		else{
			dtoNivelesAtencion=null;
		}
		UtilidadTransaccion.getTransaccion().commit();
		return dtoNivelesAtencion;
	}


	@Override
	public ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio> consultarConsolidadoServiciosPorNivelPorContrato(
			Contratos contrato, String proceso, List<Calendar> mesesReporte) {
		INivelAtencionMundo nivelAtencionMundo = CapitacionFabricaMundo.crearNivelAtencionMundo();
		ICierrePresupuestoCapitacionoMundo cierreMundo = CapitacionFabricaMundo.crearCierrePresupuestoCapitacionoMundo();
		ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio> dtoNivelesAtencion = new ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio>();
		ArrayList<NivelAtencion> nivelesAtencion = new ArrayList<NivelAtencion>();
		UtilidadTransaccion.getTransaccion().begin();
		//Se consultan los distintos niveles de atención del convenio
		nivelesAtencion=nivelAtencionMundo.listarNivelesAtencionServiciosPorContrato(contrato.getCodigo(), proceso, mesesReporte);
		if(nivelesAtencion != null && !nivelesAtencion.isEmpty()){
			for(NivelAtencion nivelAtencion: nivelesAtencion){
				DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio dtoNivel = new DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio();
				dtoNivel.setNivelAtencion(nivelAtencion.getDescripcion());
				dtoNivel.setListaMesesTotales(cierreMundo.obtenerListadoMesesTotalServiciosPorNivelPorProcesoPorContrato(
															contrato.getCodigo(), nivelAtencion.getConsecutivo(), proceso, mesesReporte));
				dtoNivelesAtencion.add(dtoNivel);
			}
		}
		else{
			dtoNivelesAtencion=null;
		}
		UtilidadTransaccion.getTransaccion().commit();
		return dtoNivelesAtencion;
	}


	@Override
	public ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio> consultarConsolidadoServiciosPorGrupoPorConvenio(
			Convenios convenio, String proceso, List<Calendar> mesesReporte) {
		INivelAtencionMundo nivelAtencionMundo = CapitacionFabricaMundo.crearNivelAtencionMundo();
		IGruposServiciosMundo gruposServiciosMundo = FacturacionFabricaMundo.crearGruposServiciosMundo();
		ICierrePresupuestoCapitacionoMundo cierreMundo = CapitacionFabricaMundo.crearCierrePresupuestoCapitacionoMundo();
		ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio> dtoNivelesAtencion = new ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio>();
		ArrayList<NivelAtencion> nivelesAtencion = new ArrayList<NivelAtencion>();
		ArrayList<GruposServicios> gruposServicios = new ArrayList<GruposServicios>();
		UtilidadTransaccion.getTransaccion().begin();
		//Se consultan los distintos niveles de atención del convenio
		nivelesAtencion=nivelAtencionMundo.listarNivelesAtencionServiciosPorConvenio(convenio.getCodigo(), proceso, mesesReporte);
		if(nivelesAtencion != null && !nivelesAtencion.isEmpty()){
			for(NivelAtencion nivelAtencion: nivelesAtencion){
				DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio dtoNivel = new DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio();
				dtoNivel.setNivelAtencion(nivelAtencion.getDescripcion());
				//Se consultan los distintos grupos de servicios asociados al convenio y al nivel que tienen cierre de presupuesto
				gruposServicios=gruposServiciosMundo.buscarGruposServicioCierrePorNivelPorConvenioPorProceso(
														convenio.getCodigo(), nivelAtencion.getConsecutivo(), proceso, mesesReporte);
				if(gruposServicios != null && !gruposServicios.isEmpty()){
					ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> consolidadoGrupos = new ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio>();
					for(GruposServicios grupoServicio:gruposServicios){
						//Se consultan los consolidados de los meses del reporte por cada grupo de servicios
						//Se agregan todos los consolidados  de cada grupo de servicios para generar un único consolidado
						consolidadoGrupos.addAll(cierreMundo.obtenerListadoMesesTotalServiciosPorNivelPorGrupoPorProcesoPorConvenio(
							convenio.getCodigo(), nivelAtencion.getConsecutivo(), grupoServicio, proceso, mesesReporte));
					}
					dtoNivel.setListaMesesTotales(consolidadoGrupos);
					dtoNivelesAtencion.add(dtoNivel);
				}
			}
		}
		else{
			dtoNivelesAtencion=null;
		}
		UtilidadTransaccion.getTransaccion().commit();
		return dtoNivelesAtencion;
	}


	@Override
	public ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio> consultarConsolidadoServiciosPorGrupoPorContrato(
			Contratos contrato, String proceso, List<Calendar> mesesReporte) {
		INivelAtencionMundo nivelAtencionMundo = CapitacionFabricaMundo.crearNivelAtencionMundo();
		IGruposServiciosMundo gruposServiciosMundo = FacturacionFabricaMundo.crearGruposServiciosMundo();
		ICierrePresupuestoCapitacionoMundo cierreMundo = CapitacionFabricaMundo.crearCierrePresupuestoCapitacionoMundo();
		ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio> dtoNivelesAtencion = new ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio>();
		ArrayList<NivelAtencion> nivelesAtencion = new ArrayList<NivelAtencion>();
		ArrayList<GruposServicios> gruposServicios = new ArrayList<GruposServicios>();
		UtilidadTransaccion.getTransaccion().begin();
		//Se consultan los distintos niveles de atención del convenio
		nivelesAtencion=nivelAtencionMundo.listarNivelesAtencionServiciosPorContrato(contrato.getCodigo(), proceso, mesesReporte);
		if(nivelesAtencion != null && !nivelesAtencion.isEmpty()){
			for(NivelAtencion nivelAtencion: nivelesAtencion){
				DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio dtoNivel = new DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio();
				dtoNivel.setNivelAtencion(nivelAtencion.getDescripcion());
				//Se consultan los distintos grupos de servicios asociados al contrato y al nivel que tienen cierre de presupuesto
				gruposServicios=gruposServiciosMundo.buscarGruposServicioCierrePorNivelPorContratoPorProceso(
														contrato.getCodigo(), nivelAtencion.getConsecutivo(), proceso, mesesReporte);
				if(gruposServicios != null && !gruposServicios.isEmpty()){
					ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> consolidadoGrupos = new ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio>();
					for(GruposServicios grupoServicio:gruposServicios){
						//Se consultan los consolidados de los meses del reporte por cada grupo de servicios
						//Se agregan todos los consolidados  de cada grupo de servicios para generar un único consolidado
						consolidadoGrupos.addAll(cierreMundo.obtenerListadoMesesTotalServiciosPorNivelPorGrupoPorProcesoPorContrato(
							contrato.getCodigo(), nivelAtencion.getConsecutivo(), grupoServicio, proceso, mesesReporte));
					}
					dtoNivel.setListaMesesTotales(consolidadoGrupos);
					dtoNivelesAtencion.add(dtoNivel);
				}
			}
		}
		else{
			dtoNivelesAtencion=null;
		}
		UtilidadTransaccion.getTransaccion().commit();
		return dtoNivelesAtencion;
	}


	@Override
	public ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio> consultarConsolidadoServiciosPorServicioPorConvenio(
			Convenios convenio, String proceso, List<Calendar> mesesReporte) {
		INivelAtencionMundo nivelAtencionMundo = CapitacionFabricaMundo.crearNivelAtencionMundo();
		IServiciosMundo serviciosMundo = FacturacionFabricaMundo.crearServiciosMundo();
		ICierrePresupuestoCapitacionoMundo cierreMundo = CapitacionFabricaMundo.crearCierrePresupuestoCapitacionoMundo();
		ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio> dtoNivelesAtencion = new ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio>();
		ArrayList<NivelAtencion> nivelesAtencion = new ArrayList<NivelAtencion>();
		ArrayList<Servicios> servicios = new ArrayList<Servicios>();
		UtilidadTransaccion.getTransaccion().begin();
		//Se consultan los distintos niveles de atención del convenio
		nivelesAtencion=nivelAtencionMundo.listarNivelesAtencionServiciosPorConvenio(convenio.getCodigo(), proceso, mesesReporte);
		if(nivelesAtencion != null && !nivelesAtencion.isEmpty()){
			for(NivelAtencion nivelAtencion: nivelesAtencion){
				DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio dtoNivel = new DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio();
				dtoNivel.setNivelAtencion(nivelAtencion.getDescripcion());
				//Se consultan los distintos servicios asociados al convenio y al nivel que tienen cierre de presupuesto
				servicios=serviciosMundo.buscarServiciosCierrePorNivelPorConvenioPorProceso(
														convenio.getCodigo(), nivelAtencion.getConsecutivo(), proceso, mesesReporte);
				if(servicios != null && !servicios.isEmpty()){
					ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> consolidadoServicios = new ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio>();
					for(Servicios servicio:servicios){
						//Se consultan los consolidados de los meses del reporte por cada servicio
						//Se agregan todos los consolidados  de cada servicio para generar un único consolidado
						consolidadoServicios.addAll(cierreMundo.obtenerListadoMesesTotalServiciosPorNivelPorServicioPorProcesoPorConvenio(
							convenio.getCodigo(), nivelAtencion.getConsecutivo(), servicio, proceso, mesesReporte));
					}
					dtoNivel.setListaMesesTotales(consolidadoServicios);
					dtoNivelesAtencion.add(dtoNivel);
				}
			}
		}
		else{
			dtoNivelesAtencion=null;
		}
		UtilidadTransaccion.getTransaccion().commit();
		return dtoNivelesAtencion;
	}


	@Override
	public ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio> consultarConsolidadoServiciosPorServicioPorContrato(
			Contratos contrato, String proceso, List<Calendar> mesesReporte) {
		INivelAtencionMundo nivelAtencionMundo = CapitacionFabricaMundo.crearNivelAtencionMundo();
		IServiciosMundo serviciosMundo = FacturacionFabricaMundo.crearServiciosMundo();
		ICierrePresupuestoCapitacionoMundo cierreMundo = CapitacionFabricaMundo.crearCierrePresupuestoCapitacionoMundo();
		ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio> dtoNivelesAtencion = new ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio>();
		ArrayList<NivelAtencion> nivelesAtencion = new ArrayList<NivelAtencion>();
		ArrayList<Servicios> servicios = new ArrayList<Servicios>();
		UtilidadTransaccion.getTransaccion().begin();
		//Se consultan los distintos niveles de atención del contrato
		nivelesAtencion=nivelAtencionMundo.listarNivelesAtencionServiciosPorContrato(contrato.getCodigo(), proceso, mesesReporte);
		if(nivelesAtencion != null && !nivelesAtencion.isEmpty()){
			for(NivelAtencion nivelAtencion: nivelesAtencion){
				DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio dtoNivel = new DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio();
				dtoNivel.setNivelAtencion(nivelAtencion.getDescripcion());
				//Se consultan los distintos servicios asociados al contrato y al nivel que tienen cierre de presupuesto
				servicios=serviciosMundo.buscarServiciosCierrePorNivelPorContratoPorProceso(
														contrato.getCodigo(), nivelAtencion.getConsecutivo(), proceso, mesesReporte);
				if(servicios != null && !servicios.isEmpty()){
					ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> consolidadoServicios = new ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio>();
					for(Servicios servicio:servicios){
						//Se consultan los consolidados de los meses del reporte por cada servicio
						//Se agregan todos los consolidados  de cada servicio para generar un único consolidado
						consolidadoServicios.addAll(cierreMundo.obtenerListadoMesesTotalServiciosPorNivelPorServicioPorProcesoPorContrato(
							contrato.getCodigo(), nivelAtencion.getConsecutivo(), servicio, proceso, mesesReporte));
					}
					dtoNivel.setListaMesesTotales(consolidadoServicios);
					dtoNivelesAtencion.add(dtoNivel);
				}
			}
		}
		else{
			dtoNivelesAtencion=null;
		}
		UtilidadTransaccion.getTransaccion().commit();
		return dtoNivelesAtencion;
	}


	@Override
	public ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio> consultarConsolidadoArticulosPorNivelPorConvenio(
			Convenios convenio, String proceso, List<Calendar> mesesReporte) {
		INivelAtencionMundo nivelAtencionMundo = CapitacionFabricaMundo.crearNivelAtencionMundo();
		ICierrePresupuestoCapitacionoMundo cierreMundo = CapitacionFabricaMundo.crearCierrePresupuestoCapitacionoMundo();
		ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio> dtoNivelesAtencion = new ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio>();
		ArrayList<NivelAtencion> nivelesAtencion = new ArrayList<NivelAtencion>();
		UtilidadTransaccion.getTransaccion().begin();
		//Se consultan los distintos niveles de atención del convenio
		nivelesAtencion=nivelAtencionMundo.listarNivelesAtencionArticulosPorConvenio(convenio.getCodigo(), proceso, mesesReporte);
		if(nivelesAtencion != null && !nivelesAtencion.isEmpty()){
			for(NivelAtencion nivelAtencion: nivelesAtencion){
				DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio dtoNivel = new DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio();
				dtoNivel.setNivelAtencion(nivelAtencion.getDescripcion());
				dtoNivel.setListaMesesTotales(cierreMundo.obtenerListadoMesesTotalArticulosPorNivelPorProceso(
															convenio.getCodigo(), nivelAtencion.getConsecutivo(), proceso, mesesReporte));
				dtoNivelesAtencion.add(dtoNivel);
			}
		}
		else{
			dtoNivelesAtencion=null;
		}
		UtilidadTransaccion.getTransaccion().commit();
		return dtoNivelesAtencion;
	}


	@Override
	public ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio> consultarConsolidadoArticulosPorNivelPorContrato(
			Contratos contrato, String proceso, List<Calendar> mesesReporte) {
		INivelAtencionMundo nivelAtencionMundo = CapitacionFabricaMundo.crearNivelAtencionMundo();
		ICierrePresupuestoCapitacionoMundo cierreMundo = CapitacionFabricaMundo.crearCierrePresupuestoCapitacionoMundo();
		ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio> dtoNivelesAtencion = new ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio>();
		ArrayList<NivelAtencion> nivelesAtencion = new ArrayList<NivelAtencion>();
		UtilidadTransaccion.getTransaccion().begin();
		//Se consultan los distintos niveles de atención del convenio
		nivelesAtencion=nivelAtencionMundo.listarNivelesAtencionArticulosPorContrato(contrato.getCodigo(), proceso, mesesReporte);
		if(nivelesAtencion != null && !nivelesAtencion.isEmpty()){
			for(NivelAtencion nivelAtencion: nivelesAtencion){
				DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio dtoNivel = new DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio();
				dtoNivel.setNivelAtencion(nivelAtencion.getDescripcion());
				dtoNivel.setListaMesesTotales(cierreMundo.obtenerListadoMesesTotalArticulosPorNivelPorProcesoPorContrato(
															contrato.getCodigo(), nivelAtencion.getConsecutivo(), proceso, mesesReporte));
				dtoNivelesAtencion.add(dtoNivel);
			}
		}
		else{
			dtoNivelesAtencion=null;
		}
		UtilidadTransaccion.getTransaccion().commit();
		return dtoNivelesAtencion;
	}


	@Override
	public ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio> consultarConsolidadoArticulosPorClasePorConvenio(
			Convenios convenio, String proceso, List<Calendar> mesesReporte) {
		INivelAtencionMundo nivelAtencionMundo = CapitacionFabricaMundo.crearNivelAtencionMundo();
		IClaseInventarioMundo claseInventarioMundo = InventarioMundoFabrica.crearClaseInventarioMundo();
		ICierrePresupuestoCapitacionoMundo cierreMundo = CapitacionFabricaMundo.crearCierrePresupuestoCapitacionoMundo();
		ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio> dtoNivelesAtencion = new ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio>();
		ArrayList<NivelAtencion> nivelesAtencion = new ArrayList<NivelAtencion>();
		ArrayList<ClaseInventario> clasesInventarios = new ArrayList<ClaseInventario>();
		UtilidadTransaccion.getTransaccion().begin();
		//Se consultan los distintos niveles de atención del convenio
		nivelesAtencion=nivelAtencionMundo.listarNivelesAtencionArticulosPorConvenio(convenio.getCodigo(), proceso, mesesReporte);
		if(nivelesAtencion != null && !nivelesAtencion.isEmpty()){
			for(NivelAtencion nivelAtencion: nivelesAtencion){
				DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio dtoNivel = new DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio();
				dtoNivel.setNivelAtencion(nivelAtencion.getDescripcion());
				//Se consultan las distintas clases de inventario asociadas al convenio y al nivel que tienen cierre de presupuesto
				clasesInventarios=claseInventarioMundo.buscarClasesInventarioCierrePorNivelPorConvenioPorProceso(
														convenio.getCodigo(), nivelAtencion.getConsecutivo(), proceso, mesesReporte);
				if(clasesInventarios != null && !clasesInventarios.isEmpty()){
					ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> consolidadoClases = new ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio>();
					for(ClaseInventario claseInventario:clasesInventarios){
						//Se consultan los consolidados de los meses del reporte por cada clase de inventario
						//Se agregan todos los consolidados  de cada clase de inventario para generar un único consolidado
						consolidadoClases.addAll(cierreMundo.obtenerListadoMesesTotalArticulosPorNivelPorClasePorProcesoPorConvenio(
							convenio.getCodigo(), nivelAtencion.getConsecutivo(), claseInventario, proceso, mesesReporte));
					}
					dtoNivel.setListaMesesTotales(consolidadoClases);
					dtoNivelesAtencion.add(dtoNivel);
				}
			}
		}
		else{
			dtoNivelesAtencion=null;
		}
		UtilidadTransaccion.getTransaccion().commit();
		return dtoNivelesAtencion;
	}


	@Override
	public ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio> consultarConsolidadoArticulosPorClasePorContrato(
			Contratos contrato, String proceso, List<Calendar> mesesReporte) {
		INivelAtencionMundo nivelAtencionMundo = CapitacionFabricaMundo.crearNivelAtencionMundo();
		IClaseInventarioMundo claseInventarioMundo = InventarioMundoFabrica.crearClaseInventarioMundo();
		ICierrePresupuestoCapitacionoMundo cierreMundo = CapitacionFabricaMundo.crearCierrePresupuestoCapitacionoMundo();
		ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio> dtoNivelesAtencion = new ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio>();
		ArrayList<NivelAtencion> nivelesAtencion = new ArrayList<NivelAtencion>();
		ArrayList<ClaseInventario> clasesInventarios = new ArrayList<ClaseInventario>();
		UtilidadTransaccion.getTransaccion().begin();
		//Se consultan los distintos niveles de atención del contrato
		nivelesAtencion=nivelAtencionMundo.listarNivelesAtencionArticulosPorContrato(contrato.getCodigo(), proceso, mesesReporte);
		if(nivelesAtencion != null && !nivelesAtencion.isEmpty()){
			for(NivelAtencion nivelAtencion: nivelesAtencion){
				DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio dtoNivel = new DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio();
				dtoNivel.setNivelAtencion(nivelAtencion.getDescripcion());
				//Se consultan las distintas clases de inventario asociadas al contrato y al nivel que tienen cierre de presupuesto
				clasesInventarios=claseInventarioMundo.buscarClasesInventarioCierrePorNivelPorContratoPorProceso(
														contrato.getCodigo(), nivelAtencion.getConsecutivo(), proceso, mesesReporte);
				if(clasesInventarios != null && !clasesInventarios.isEmpty()){
					ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> consolidadoClases = new ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio>();
					for(ClaseInventario claseInventario:clasesInventarios){
						//Se consultan los consolidados de los meses del reporte por cada clase de inventario
						//Se agregan todos los consolidados  de cada clase de inventario para generar un único consolidado
						consolidadoClases.addAll(cierreMundo.obtenerListadoMesesTotalArticulosPorNivelPorClasePorProcesoPorContrato(
							contrato.getCodigo(), nivelAtencion.getConsecutivo(), claseInventario, proceso, mesesReporte));
					}
					dtoNivel.setListaMesesTotales(consolidadoClases);
					dtoNivelesAtencion.add(dtoNivel);
				}
			}
		}
		else{
			dtoNivelesAtencion=null;
		}
		UtilidadTransaccion.getTransaccion().commit();
		return dtoNivelesAtencion;
	}


	@Override
	public ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio> consultarConsolidadoArticulosPorArticuloPorConvenio(
			Convenios convenio, String proceso, List<Calendar> mesesReporte) {
		INivelAtencionMundo nivelAtencionMundo = CapitacionFabricaMundo.crearNivelAtencionMundo();
		IArticulosMundo articulosMundo = InventarioMundoFabrica.crearArticulosMundo();
		ICierrePresupuestoCapitacionoMundo cierreMundo = CapitacionFabricaMundo.crearCierrePresupuestoCapitacionoMundo();
		ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio> dtoNivelesAtencion = new ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio>();
		ArrayList<NivelAtencion> nivelesAtencion = new ArrayList<NivelAtencion>();
		ArrayList<Articulo> articulos = new ArrayList<Articulo>();
		UtilidadTransaccion.getTransaccion().begin();
		//Se consultan los distintos niveles de atención del convenio
		nivelesAtencion=nivelAtencionMundo.listarNivelesAtencionArticulosPorConvenio(convenio.getCodigo(), proceso, mesesReporte);
		if(nivelesAtencion != null && !nivelesAtencion.isEmpty()){
			for(NivelAtencion nivelAtencion: nivelesAtencion){
				DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio dtoNivel = new DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio();
				dtoNivel.setNivelAtencion(nivelAtencion.getDescripcion());
				//Se consultan los distintos articulos asociados al convenio y al nivel que tienen cierre de presupuesto
				articulos=articulosMundo.buscarArticulosCierrePorNivelPorConvenioPorProceso(
														convenio.getCodigo(), nivelAtencion.getConsecutivo(), proceso, mesesReporte);
				if(articulos != null && !articulos.isEmpty()){
					ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> consolidadoArticulos = new ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio>();
					for(Articulo articulo:articulos){
						//Se consultan los consolidados de los meses del reporte por cada articulo
						//Se agregan todos los consolidados  de cada articulo para generar un único consolidado
						consolidadoArticulos.addAll(cierreMundo.obtenerListadoMesesTotalArticulosPorNivelPorArticuloPorProcesoPorConvenio(
							convenio.getCodigo(), nivelAtencion.getConsecutivo(), articulo, proceso, mesesReporte));
					}
					dtoNivel.setListaMesesTotales(consolidadoArticulos);
					dtoNivelesAtencion.add(dtoNivel);
				}
			}
		}
		else{
			dtoNivelesAtencion=null;
		}
		UtilidadTransaccion.getTransaccion().commit();
		return dtoNivelesAtencion;
	}


	@Override
	public ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio> consultarConsolidadoArticulosPorArticuloPorContrato(
			Contratos contrato, String proceso, List<Calendar> mesesReporte) {
		INivelAtencionMundo nivelAtencionMundo = CapitacionFabricaMundo.crearNivelAtencionMundo();
		IArticulosMundo articulosMundo = InventarioMundoFabrica.crearArticulosMundo();
		ICierrePresupuestoCapitacionoMundo cierreMundo = CapitacionFabricaMundo.crearCierrePresupuestoCapitacionoMundo();
		ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio> dtoNivelesAtencion = new ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio>();
		ArrayList<NivelAtencion> nivelesAtencion = new ArrayList<NivelAtencion>();
		ArrayList<Articulo> articulos = new ArrayList<Articulo>();
		UtilidadTransaccion.getTransaccion().begin();
		//Se consultan los distintos niveles de atención del convenio
		nivelesAtencion=nivelAtencionMundo.listarNivelesAtencionArticulosPorContrato(contrato.getCodigo(), proceso, mesesReporte);
		if(nivelesAtencion != null && !nivelesAtencion.isEmpty()){
			for(NivelAtencion nivelAtencion: nivelesAtencion){
				DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio dtoNivel = new DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio();
				dtoNivel.setNivelAtencion(nivelAtencion.getDescripcion());
				//Se consultan los distintos articulos asociados al contrato y al nivel que tienen cierre de presupuesto
				articulos=articulosMundo.buscarArticulosCierrePorNivelPorContratoPorProceso(
														contrato.getCodigo(), nivelAtencion.getConsecutivo(), proceso, mesesReporte);
				if(articulos != null && !articulos.isEmpty()){
					ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> consolidadoArticulos = new ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio>();
					for(Articulo articulo:articulos){
						//Se consultan los consolidados de los meses del reporte por cada articulo
						//Se agregan todos los consolidados  de cada articulo para generar un único consolidado
						consolidadoArticulos.addAll(cierreMundo.obtenerListadoMesesTotalArticulosPorNivelPorArticuloPorProcesoPorContrato(
							contrato.getCodigo(), nivelAtencion.getConsecutivo(), articulo, proceso, mesesReporte));
					}
					dtoNivel.setListaMesesTotales(consolidadoArticulos);
					dtoNivelesAtencion.add(dtoNivel);
				}
			}
		}
		else{
			dtoNivelesAtencion=null;
		}
		UtilidadTransaccion.getTransaccion().commit();
		return dtoNivelesAtencion;
	}

}
