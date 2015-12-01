package com.servinte.axioma.bl.historiaClinica.facade;

import java.util.Date;
import java.util.List;

import com.servinte.axioma.bl.historiaClinica.impl.CurvasCrecimientoMundo;
import com.servinte.axioma.bl.historiaClinica.interfaz.ICurvasCrecimientoMundo;
import com.servinte.axioma.dto.historiaClinica.CurvaCrecimientoPacienteDto;
import com.servinte.axioma.dto.historiaClinica.CurvaCrecimientoParametrizabDto;
import com.servinte.axioma.dto.historiaClinica.DatosAlmacenarCurvaCrecimientoDto;

import com.servinte.axioma.dto.historiaClinica.HistoricoImagenPlantillaDto;
import com.servinte.axioma.dto.historiaClinica.ImagenParametrizadaDto;

import com.servinte.axioma.dto.historiaClinica.PlantillaComponenteDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.generadorReporte.historiaClinica.curvasCrecimiento.CurvaCrecimientoDesarrolloDto;

/**
 * Clase Fachada que provee todos los servicios de lógica de negocio del módulo de
 * Historia Clinica a todos los Action de la Capa Web
 * 
 * @author hermorhu
 * @created 09-Oct-2012
 */
public class HistoriaClinicaFacade {
	
	/**
	 * Metodo encargado de obtener todas las Curvas Parametrizadas
	 * @return
	 * @throws IPSException
	 * @author hermorhu
	 */
	public List<CurvaCrecimientoParametrizabDto> consultarCurvasParametrizadas () throws IPSException{
		ICurvasCrecimientoMundo mundoCurvasCrecimiento = new CurvasCrecimientoMundo();
		return mundoCurvasCrecimiento.consultarCurvasParametrizadas();
	}
	
	/**
	 * Metodo encargado de obtener las Curvas seleccionadas en el Componente
	 * @param codigoComponente
	 * @return
	 * @throws IPSException
	 * @author hermorhu
	 */
	public List<PlantillaComponenteDto> consultarCurvasComponente (int codigoComponente) throws IPSException{
		ICurvasCrecimientoMundo mundoCurvasCrecimiento = new CurvasCrecimientoMundo();
		return mundoCurvasCrecimiento.consultarCurvasComponente(codigoComponente);
	}
	
	/**
	 * Metodo encargado de obtener las curvas que no hallan sido seleccionadas en el componente
	 * @param codigoComponente
	 * @return
	 * @throws IPSException
	 * @author hermorhu
	 */
	public List<CurvaCrecimientoParametrizabDto> consultarCurvasDisponiblesComponente (int codigoComponente) throws IPSException{
		ICurvasCrecimientoMundo mundoCurvasCrecimiento = new CurvasCrecimientoMundo();
		return mundoCurvasCrecimiento.consultarCurvasDisponiblesComponente(codigoComponente);
	}

	/**
	 * Metodo encargado de guardar las curvas asociadas al componente
	 * @param plantillasComponente
	 * @param codigoComponente
	 * @return
	 * @throws IPSException
	 * @author hermorhu
	 */
	public boolean asociarCurvasAComponente (List<PlantillaComponenteDto> plantillasComponente, int codigoComponente) throws IPSException{
		ICurvasCrecimientoMundo mundoCurvasCrecimiento = new CurvasCrecimientoMundo();
		return mundoCurvasCrecimiento.asociarCurvasAComponente(plantillasComponente, codigoComponente);
	}
	
	/**
	 * Metodo encargado de eliminar las curvas asociadas al componente
	 * @param plantillasComponente
	 * @param codigoComponente
	 * @return
	 * @throws IPSException
	 * @author hermorhu
	 */
	public boolean eliminarCurvasComponente (List<PlantillaComponenteDto> plantillasComponente, int codigoComponente)  throws IPSException{
		ICurvasCrecimientoMundo mundoCurvasCrecimiento = new CurvasCrecimientoMundo();
		return mundoCurvasCrecimiento.eliminarCurvasComponente(plantillasComponente, codigoComponente);
	}
	
	/**
	 * Metodo encargado de obtener las curvas para un paciente determinado
	 * @param codigoPaciente
	 * @param codigoSexo
	 * @param edadPaciente
	 * @return
	 * @throws IPSException
	 * @author hermorhu
	 */
	public List<CurvaCrecimientoPacienteDto> consultarCurvasCrecimientoPaciente (int codigoPaciente, int codigoSexo, int edadPaciente) throws IPSException{
		ICurvasCrecimientoMundo mundoCurvasCrecimiento = new CurvasCrecimientoMundo();
		return mundoCurvasCrecimiento.consultarCurvasCrecimientoPaciente(codigoPaciente, codigoSexo, edadPaciente);
	}
	
	/**
	 * Metodo encargado de guardar la curva de crecimiento
	 * @param curvaCrecimiento
	 * @return
	 * @throws IPSException
	 * @author hermorhu
	 */
	public boolean guardarCurvaCrecimiento (DatosAlmacenarCurvaCrecimientoDto curvaCrecimiento) throws IPSException{
		ICurvasCrecimientoMundo mundoCurvasCrecimiento = new CurvasCrecimientoMundo();
		return mundoCurvasCrecimiento.guardarCurvaCrecimiento(curvaCrecimiento);
	}
	
	/**
	 * Metodo encargado de obtener datos necesarios del paciente para el formato de impresion de Curvas de Crecimiento
	 * @param codigoIngreso
	 * @param dtoCurvaCrecimientoDesarrollo
	 * @throws IPSException
	 * @author hermorhu
	 */
	public CurvaCrecimientoDesarrolloDto consultarDatosPacienteFormatoImpresion (int codigoIngreso, CurvaCrecimientoDesarrolloDto dtoCurvaCrecimientoDesarrollo) throws IPSException{
		ICurvasCrecimientoMundo mundoCurvasCrecimiento = new CurvasCrecimientoMundo();
		return mundoCurvasCrecimiento.consultarDatosPacienteFormatoImpresion(codigoIngreso, dtoCurvaCrecimientoDesarrollo);
	}
	
	/**
	 * consultarCurvaParametrizada
	 * @param idcurva
	 * @return
	 * @throws IPSException
	 */
	public CurvaCrecimientoParametrizabDto consultarCurvaParametrizada(int idcurva) throws IPSException{
		ICurvasCrecimientoMundo mundoCurvasCrecimiento = new CurvasCrecimientoMundo();
		return mundoCurvasCrecimiento.consultarCurvaParametrizada(idcurva);
	}
	
	/**
	 * consultarImagenesParametrizadas
	 * @param idCurva
	 * @return
	 * @throws IPSException
	 */
	public List<ImagenParametrizadaDto> consultarImagenesParametrizadas(int idCurva) throws IPSException {
		ICurvasCrecimientoMundo mundoCurvasCrecimiento = new CurvasCrecimientoMundo();
		return mundoCurvasCrecimiento.consultarImagenesParametrizadas(idCurva);
	}
	
	/**
	 * guardarCurvaParametrizada
	 * @param dtoccp
	 * @throws IPSException
	 */
	public void guardarCurvaParametrizada(CurvaCrecimientoParametrizabDto dtoccp) throws IPSException{
		ICurvasCrecimientoMundo mundoCurvasCrecimiento = new CurvasCrecimientoMundo();
		mundoCurvasCrecimiento.guardarCurvaParametrizada(dtoccp);
	}
	
	/**
	 * eliminarCurvaParametrizada
	 * @param idCurva
	 * @throws IPSException
	 */
	public void eliminarCurvaParametrizada(int idCurva) throws IPSException{
		ICurvasCrecimientoMundo mundoCurvasCrecimiento = new CurvasCrecimientoMundo();
		mundoCurvasCrecimiento.eliminarCurvaParametrizada(idCurva);
	}

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
	public List<CurvaCrecimientoParametrizabDto> buscarCurvaCriterios(String titulo, String descripcion, Integer edadInicial, Integer edadFinal, Integer codigoSexo, Boolean activo, Boolean inidicadorError) throws IPSException{
		ICurvasCrecimientoMundo mundoCurvasCrecimiento = new CurvasCrecimientoMundo();
		return mundoCurvasCrecimiento.buscarCurvaCriterios(titulo, descripcion, edadInicial, edadFinal, codigoSexo, activo, inidicadorError);
	}
	
	/**
	 * valoracionesYevolucionesPorpacienteConCurva
	 * @param codigoPaciente
	 * @return
	 * @throws IPSException
	 */
	public List<HistoricoImagenPlantillaDto> valoracionesYevolucionesPorpacienteConCurva(int codigoPaciente) throws IPSException{
		ICurvasCrecimientoMundo mundoCurvasCrecimiento = new CurvasCrecimientoMundo();
		return mundoCurvasCrecimiento.valoracionesYevolucionesPorpacienteConCurva(codigoPaciente);
	}
	
	/**
	 * valoracionesYevolucionesPorpacienteYporIngresoConCurva
	 * @param codigoPaciente
	 * @param idCuenta
	 * @return
	 * @throws IPSException
	 */
	public List<HistoricoImagenPlantillaDto> valoracionesYevolucionesPorpacienteYporIngresoConCurva(int codigoPaciente, int idCuenta) throws IPSException{
		ICurvasCrecimientoMundo mundoCurvasCrecimiento = new CurvasCrecimientoMundo();
		return mundoCurvasCrecimiento.valoracionesYevolucionesPorpacienteYporIngresoConCurva(codigoPaciente, idCuenta);
	}
	
	/**
	 * valoracionesPorId
	 * @param idValoracion
	 * @return
	 * @throws IPSException
	 */
	public List<HistoricoImagenPlantillaDto> valoracionesPorId(int idValoracion) throws IPSException{
		ICurvasCrecimientoMundo mundoCurvasCrecimiento = new CurvasCrecimientoMundo();
		return mundoCurvasCrecimiento.valoracionesPorId(idValoracion);
	}
	
	/**
	 * evolucionesPorId
	 * @param idEvolucion
	 * @return
	 * @throws IPSException
	 */
	public List<HistoricoImagenPlantillaDto> evolucionesPorId(int idEvolucion) throws IPSException{
		ICurvasCrecimientoMundo mundoCurvasCrecimiento = new CurvasCrecimientoMundo();
		return mundoCurvasCrecimiento.evolucionesPorId(idEvolucion);
	}
	
	
	/**
	 * Metodo encargado de verificar si existen o no Curvas anteiores a la fecha de corte
	 * @param codigoPaciente
	 * @param codigoInstitucion
	 * @param fechaCorteCurvas
	 * @return
	 * @throws IPSException
	 */
	public boolean existeCurvasAnteriores(int codigoPaciente, int codigoInstitucion, Date fechaCorteCurvas) throws IPSException{
		ICurvasCrecimientoMundo mundoCurvasCrecimiento = new CurvasCrecimientoMundo();
		return mundoCurvasCrecimiento.existeCurvasAnteriores(codigoPaciente, codigoInstitucion, fechaCorteCurvas);
	}
}