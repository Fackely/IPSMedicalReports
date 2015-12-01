package com.servinte.axioma.bl.historiaClinica.interfaz;

import java.util.Date;
import java.util.List;

import com.servinte.axioma.dto.historiaClinica.CurvaCrecimientoPacienteDto;
import com.servinte.axioma.dto.historiaClinica.CurvaCrecimientoParametrizabDto;
import com.servinte.axioma.dto.historiaClinica.DatosAlmacenarCurvaCrecimientoDto;

import com.servinte.axioma.dto.historiaClinica.HistoricoImagenPlantillaDto;
import com.servinte.axioma.dto.historiaClinica.ImagenParametrizadaDto;

import com.servinte.axioma.dto.historiaClinica.PlantillaComponenteDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.generadorReporte.historiaClinica.curvasCrecimiento.CurvaCrecimientoDesarrolloDto;

/**
 * Interface que expone los servicios de Negocio correspondientes a la lógica asociada a las
 * Curvas de Crecimiento
 * 
 * @author hermorhu
 * @created 09-Oct-2012 
 */
public interface ICurvasCrecimientoMundo {

	/**
	 * Metodo encargado de obtener todas las Curvas Parametrizadas
	 * @return
	 * @throws IPSException
	 * @author hermorhu
	 */
	List<CurvaCrecimientoParametrizabDto> consultarCurvasParametrizadas () throws IPSException;
	
	/**
	 * Metodo encargado de obtener las Curvas seleccionadas en el Componente
	 * @param codigoComponente
	 * @return
	 * @throws IPSException
	 * @author hermorhu
	 */
	List<PlantillaComponenteDto> consultarCurvasComponente (int codigoComponente) throws IPSException;
	
	/**
	 * Metodo encargado de obtener las curvas que no hallan sido seleccionadas en el componente
	 * @param codigoComponente
	 * @return
	 * @throws IPSException
	 * @author hermorhu
	 */
	List<CurvaCrecimientoParametrizabDto> consultarCurvasDisponiblesComponente(int codigoComponente) throws IPSException;
	
	/**
	 * Metodo encargado de asociar una curva al componente
	 * @param plantillasComponente
	 * @param codigoComponente
	 * @return
	 * @throws IPSException
	 * @author hermorhu
	 */
	boolean asociarCurvasAComponente(List<PlantillaComponenteDto> plantillasComponente, int codigoComponente) throws IPSException;
	
	/**
	 * Metodo encargado de eliminar las curvas asociadas al componente
	 * @param plantillasComponente
	 * @param codigoComponente
	 * @return
	 * @throws IPSException
	 * @author hermorhu
	 */
	boolean eliminarCurvasComponente(List<PlantillaComponenteDto> plantillasComponente, int codigoComponente) throws IPSException;
	
	/**
	 * Metodo encargado de obtener las curvas para un paciente determinado
	 * @param codigoPaciente
	 * @param codigoSexo
	 * @param edadPaciente
	 * @return
	 * @throws IPSException
	 * @author hermorhu
	 */
	List<CurvaCrecimientoPacienteDto> consultarCurvasCrecimientoPaciente (int codigoPaciente, int codigoSexo, int edadPaciente) throws IPSException;
	
	/**
	 * Metodo encargado de guardar la curva de crecimiento
	 * @param curvaCrecimiento
	 * @return
	 * @throws IPSException
	 * @author hermorhu
	 */
	boolean guardarCurvaCrecimiento (DatosAlmacenarCurvaCrecimientoDto curvaCrecimiento) throws IPSException;
	
	/**
	 * Metodo encargado de obtener datos necesarios del paciente para el formato de impresion de Curvas de Crecimiento
	 * @param codigoIngreso
	 * @param dtoCurvaCrecimientoDesarrollo
	 * @return
	 * @throws IPSException
	 * @author hermorhu
	 */
	CurvaCrecimientoDesarrolloDto consultarDatosPacienteFormatoImpresion (int codigoIngreso, CurvaCrecimientoDesarrolloDto dtoCurvaCrecimientoDesarrollo) throws IPSException;
	
	/**
	 * consultarCurvaParametrizada
	 * @param idCurva
	 * @return
	 * @throws IPSException
	 */
	public CurvaCrecimientoParametrizabDto consultarCurvaParametrizada (int idCurva) throws IPSException;
	/**
	 * consultarImagenesParametrizadas
	 * @param idCurva
	 * @return
	 * @throws IPSException
	 */
	public List<ImagenParametrizadaDto> consultarImagenesParametrizadas (int idCurva) throws IPSException;
	/**
	 * guardarCurvaParametrizada
	 * @param dtoccp
	 * @throws IPSException
	 */
	public void guardarCurvaParametrizada(CurvaCrecimientoParametrizabDto dtoccp) throws IPSException;
	/**
	 * eliminarCurvaParametrizada
	 * @param idCurva
	 * @throws IPSException
	 */
	public void eliminarCurvaParametrizada(int idCurva) throws IPSException;
	/**
	 * buscarCurvaCriterios
	 * @param titulo
	 * @param descripcion
	 * @param edadInicial
	 * @param edadFinal
	 * @param codigoSexo
	 * @param activo
	 * @param inidicadorError
	 * @return
	 * @throws IPSException
	 */
	public List<CurvaCrecimientoParametrizabDto> buscarCurvaCriterios(String titulo, String descripcion, Integer edadInicial, Integer edadFinal, Integer codigoSexo, Boolean activo, Boolean inidicadorError) throws IPSException;
	/**
	 * valoracionesYevolucionesPorpacienteConCurva
	 * @param codigoPaciente
	 * @return
	 * @throws IPSException
	 */
	public List<HistoricoImagenPlantillaDto> valoracionesYevolucionesPorpacienteConCurva(int codigoPaciente) throws IPSException;
	/**
	 * valoracionesYevolucionesPorpacienteYporIngresoConCurva
	 * @param codigoPaciente
	 * @param idCuenta
	 * @return
	 * @throws IPSException
	 */
	public List<HistoricoImagenPlantillaDto> valoracionesYevolucionesPorpacienteYporIngresoConCurva(int codigoPaciente, int idCuenta) throws IPSException;
	/**
	 * valoracionesPorId
	 * @param idValoracion
	 * @return
	 * @throws IPSException
	 */
	public List<HistoricoImagenPlantillaDto> valoracionesPorId(int idValoracion) throws IPSException;
	/**
	 * evolucionesPorId
	 * @param idEvolucion
	 * @return
	 * @throws IPSException
	 */
	public List<HistoricoImagenPlantillaDto> evolucionesPorId(int idEvolucion) throws IPSException;
	
	/**
	 * @param codigoPaciente
	 * @param codigoInstitucion
	 * @param fechaCorteCurvas
	 * @return
	 * @throws IPSException
	 */
	public boolean existeCurvasAnteriores(int codigoPaciente, int codigoInstitucion, Date fechaCorteCurvas) throws IPSException;
}