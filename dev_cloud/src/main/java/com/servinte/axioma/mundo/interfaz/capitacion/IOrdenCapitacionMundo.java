package com.servinte.axioma.mundo.interfaz.capitacion;

import java.util.ArrayList;
import java.util.List;

import java.util.Calendar;
import com.servinte.axioma.dto.capitacion.DtoContratoReporte;
import com.servinte.axioma.dto.capitacion.DtoConvenioReporte;
import com.servinte.axioma.dto.capitacion.DtoDiaCierre;
import com.servinte.axioma.orm.Contratos;
import com.servinte.axioma.orm.Convenios;
import com.servinte.axioma.orm.ParamPresupuestosCap;


/**
 * Define la lógica de negocio relacionada con la
 *  Consulta de ordenes de capitacion subcontratada
 * 
 * @version 1.0, May 02, 2011
 * @author <a href="mailto:ricardo.ruiz@servinte.com.co">Ricardo Ruiz</a>
 * 
 */
public interface IOrdenCapitacionMundo {
	
	
	/**
	 * Método encargado de obtener el consolidado de los convenios
	 * agrupado por convenio para un mes especifico
	 * @param mesAnio
	 * @param codigoInstitucion
	 * @param esCapitacionSubcontratada
	 * @return
	 */
	public ArrayList<DtoConvenioReporte> obtenerConsolidadoConveniosPorNivelAtencion(Calendar mesAnio, int codidoInstitucion, char esCapitacionSubcontratada);
	
	
	/**
	 * Método encargado de obtener el consolidado de los convenios
	 * agrupado por convenio para un mes y un convenio especifico
	 * @param mesAnio
	 * @param convenio
	 * @param codigoInstitucion
	 * @param esCapitacionSubcontratada
	 * @return
	 */
	public ArrayList<DtoConvenioReporte> obtenerConsolidadoConveniosPorNivelAtencionPorConvenio(Calendar mesAnio, Convenios convenio,int codidoInstitucion, char esCapitacionSubcontratada);
	
	
	/**
	 * Método encargado de obtener el consolidado de los convenios
	 * agrupado por convenio para un mes, convenio y contrato especifico
	 * @param mesAnio
	 * @param convenio
	 * @param contrato
	 * @param codigoInstitucion
	 * @param esCapitacionSubcontratada
	 * @return
	 */
	public ArrayList<DtoConvenioReporte> obtenerConsolidadoConveniosPorNivelAtencionPorConvenioContrato(Calendar mesAnio, Convenios convenio, Contratos contrato, int codidoInstitucion,char esCapitacionSubcontratada);
	
	
	/**
	 * Método encargado de obtener el consolidado del convenio
	 * agrupado por nivel de atención para un mes y convenio especifico
	 * @param mesAnio
	 * @param convenio
	 * @param esCapitacionSubcontratada
	 * @return
	 */
	public DtoConvenioReporte obtenerConsolidadoConvenioPorConvenio(Calendar mesAnio, Convenios convenio, char esCapitacionSubcontratada);
	
	
	/**
	 * Método encargado de obtener el consolidado del convenio
	 * agrupado por nivel de atención para un mes, convenio y contrato especifico
	 * @param mesAnio
	 * @param convenio
	 * @param contrato
	 * @param esCapitacionSubcontratada
	 * @return
	 */
	public DtoConvenioReporte obtenerConsolidadoConvenioPorConvenioContrato(Calendar mesAnio, Convenios convenio, Contratos contrato, char esCapitacionSubcontratada);
	
	
	/**
	 * Método encargado de obtener el consolidado del contrato
	 * agrupado por nivel de atención para un mes, convenio y contrato especifico
	 * @param mesAnio
	 * @param conenio
	 * @param contrato
	 * @param esCapitacionSubcontratada
	 * @return
	 */
	public DtoContratoReporte obtenerConsolidadoContratoPorConvenioContrato(Calendar mesAnio, Convenios convenio, Contratos contrato, char esCapitacionSubcontratada);

	
	/**
	 * Método encargado de verificar si existe un presupuesto para el contrato
	 * en un año de vigencia determinado
	 * @param anioVigencia
	 * @param codigoContrato
	 * @return boolean
	 */
	public boolean existeParametrizacionPresupuesto(int codigoContrato, String anioVigencia);
	
	/**
	 * Método encargado de verificar si existe un presupuesto para el contrato
	 * en un año de vigencia determinado
	 * @param anioVigencia
	 * @param codigoConvenio
	 * @return boolean
	 */
	public boolean existeParametrizacionPresupuestoConvenio(int codigoConvenio, String anioVigencia);
	
	
	/**
	 * Método encargado de verificar si existe un presupuesto detallado por Grupo de Servicios
	 * y Clase de Inventarios para el contrato
	 * en un mes y año determinado
	 * @param anioMes
	 * @param codigoContrato
	 * @return boolean
	 */
	public boolean existeParametrizacionDetalladaPresupuesto(int codigoContrato, Calendar anioMes);
	
	/**
	 * Método encargado de consultar la informacion de presupuesto de un contrato
	 * @param anioMes
	 * @param codigoContrato
	 * @return ParamPresupuestosCap
	 */
	public ParamPresupuestosCap obtenerParametrizacionContrato(int codigoContrato, Calendar anioMes);
	
	/**
	 * Método encargado de validar cada uno de los dias en los cuales existe un cierre
	 * @param Lis<DtoDiaCierre> diasCierre
	 * @return ParamPresupuestosCap
	 */
	public List<DtoDiaCierre> validarCierresDiarios(List<DtoDiaCierre> diasCierre);
}
