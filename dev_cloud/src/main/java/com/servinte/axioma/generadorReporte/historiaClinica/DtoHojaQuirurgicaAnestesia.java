package com.servinte.axioma.generadorReporte.historiaClinica;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.servinte.axioma.dto.salascirugia.EspecialidadDto;
import com.servinte.axioma.dto.salascirugia.InformacionActoQxDto;
import com.servinte.axioma.dto.salascirugia.InformeQxDto;
import com.servinte.axioma.dto.salascirugia.IngresoSalidaPacienteDto;
import com.servinte.axioma.dto.salascirugia.NotaAclaratoriaDto;
import com.servinte.axioma.dto.salascirugia.NotaEnfermeriaDto;
import com.servinte.axioma.dto.salascirugia.NotaRecuperacionDto;

public class DtoHojaQuirurgicaAnestesia {
	
	/**
	 * Mapa  de encabezado de hoja de anestesia
	 */
	private HashMap mapaEncabezadosHojaAnestesia;
	
	/**
	 * Mapa de ExamenesLaboratorioPreanestesia
	 */
	private HashMap mapaExamenesLaboratorioPreanestesia;
	
	/**
	 * mapa de Examenes fisicos 
	 */
	private HashMap mapaHistoExamenesFisicosText;
	
	/**
	 * Mapa de area de examenes fisicos 
	 */
	private HashMap mapaHistoExamenesFisicosTextArea;
	
	/**
	 * Mapa de conslusiones 
	 */
	private HashMap mapaHistoConclusiones;
	 
	/**
	 * Mapa hoja Quirurgica
	 */
	private HashMap mapaHojaQuirur;
	 
	/**
	 * Mapa notas de enfermeria 
	 */
	private HashMap mapaNotasEnfer;
	 
	/**
	 * Mapa notas de recuperacion 
	 */
	private HashMap mapaNotasRecuperacion;
	
	/**
	 * Mapa de codigos de peticion 
	 */
	private HashMap mapaCodigosPeticionCirugia;
	
	/**
	 *Mapa de detalle de notas de recuperacion  
	 */
	private HashMap mapaNotasRecuperacionDetalle;
	
	
	/**
	 *numero de imgreso del paciente  
	 */
	private Integer codigoPk;
	
	/**
	 * Map<Integer(codigoSolicitud),IngresoSalidaPacienteDto>
	 * */
	private Map<Integer,IngresoSalidaPacienteDto>mapaIngresoSalidaPaciente=new HashMap<Integer, IngresoSalidaPacienteDto>(0);
	/**
	 * Map<Integer(codigoSolicitud),InformacionActoQxDto>
	 * */
	private Map<Integer,InformacionActoQxDto>mapaInformacionActoQuirurgico=new HashMap<Integer, InformacionActoQxDto>(0);
	/**
	 * Map<Integer(codigoSolicitud),List<EspecialidadDto>>
	 * */
	private Map<Integer,List<EspecialidadDto>>mapaEspecialidadesXSolicitud=new HashMap<Integer, List<EspecialidadDto>>(0);
	/**
	 * Map<Integer(codigoSolicitud),Map<Integer(codigoEspecialidad), InformeQxDto>>
	 * */
	private Map<Integer,Map<Integer, InformeQxDto>>mapaDescripcionesOperatorias=new HashMap<Integer, Map<Integer,InformeQxDto>>(0);
	/**
	 * Map<Integer(codigoSolicitud),Map<Integer(codigoEspecialidad), List<NotaAclaratoriaDto>>>
	 * */
	private Map<Integer,Map<Integer, List<NotaAclaratoriaDto>>>mapaNotasAclaratorias=new HashMap<Integer, Map<Integer,List<NotaAclaratoriaDto>>>(0);
	/**
	 * Map<Integer(codigoSolicitud),List<NotaEnfermeriaDto>>
	 * */
	private Map<Integer, List<NotaEnfermeriaDto>>mapaNotasEnfermeria=new HashMap<Integer, List<NotaEnfermeriaDto>>(0);
	/**
	 * Map<Integer(codigoSolicitud),List<NotaRecuperacionDto>>
	 * */
	private Map<Integer, List<NotaRecuperacionDto>>mapaNotasRecuperacionCirugia=new HashMap<Integer, List<NotaRecuperacionDto>>(0);
	
	/**
	 * Constructor de clase 
	 */
	public DtoHojaQuirurgicaAnestesia() {
		this.mapaEncabezadosHojaAnestesia=new HashMap();
		this.mapaEncabezadosHojaAnestesia.put("numRegistros","0");
		
		this.mapaExamenesLaboratorioPreanestesia=new HashMap();
		this.mapaExamenesLaboratorioPreanestesia.put("numRegistros","0");
		
		this.mapaHistoExamenesFisicosText=new HashMap();
		this.mapaHistoExamenesFisicosText.put("numRegistros","0");
		
		this.mapaHistoExamenesFisicosTextArea=new HashMap();
		this.mapaHistoExamenesFisicosTextArea.put("numRegistros","0");
		
		this.mapaHistoConclusiones=new HashMap();
		this.mapaHistoConclusiones.put("numRegistros","0");
		
		this.mapaCodigosPeticionCirugia=new HashMap();
		this.mapaCodigosPeticionCirugia.put("numRegistros","0");

		this.mapaHojaQuirur = new HashMap();
		this.mapaNotasEnfer = new HashMap();
		this.mapaNotasRecuperacion = new HashMap();
		this.codigoPk=new Integer(0);
		this.mapaNotasRecuperacionDetalle=new HashMap();
	}

	/**
	 * @return the mapaEncabezadosHojaAnestesia
	 */
	public HashMap getMapaEncabezadosHojaAnestesia() {
		return mapaEncabezadosHojaAnestesia;
	}

	/**
	 * @param mapaEncabezadosHojaAnestesia the mapaEncabezadosHojaAnestesia to set
	 */
	public void setMapaEncabezadosHojaAnestesia(HashMap mapaEncabezadosHojaAnestesia) {
		this.mapaEncabezadosHojaAnestesia = mapaEncabezadosHojaAnestesia;
	}

	/**
	 * @return the mapaExamenesLaboratorioPreanestesia
	 */
	public HashMap getMapaExamenesLaboratorioPreanestesia() {
		return mapaExamenesLaboratorioPreanestesia;
	}

	/**
	 * @param mapaExamenesLaboratorioPreanestesia the mapaExamenesLaboratorioPreanestesia to set
	 */
	public void setMapaExamenesLaboratorioPreanestesia(
			HashMap mapaExamenesLaboratorioPreanestesia) {
		this.mapaExamenesLaboratorioPreanestesia = mapaExamenesLaboratorioPreanestesia;
	}

	/**
	 * @return the mapaHistoExamenesFisicosText
	 */
	public HashMap getMapaHistoExamenesFisicosText() {
		return mapaHistoExamenesFisicosText;
	}

	/**
	 * @param mapaHistoExamenesFisicosText the mapaHistoExamenesFisicosText to set
	 */
	public void setMapaHistoExamenesFisicosText(HashMap mapaHistoExamenesFisicosText) {
		this.mapaHistoExamenesFisicosText = mapaHistoExamenesFisicosText;
	}

	/**
	 * @return the mapaHistoExamenesFisicosTextArea
	 */
	public HashMap getMapaHistoExamenesFisicosTextArea() {
		return mapaHistoExamenesFisicosTextArea;
	}

	/**
	 * @param mapaHistoExamenesFisicosTextArea the mapaHistoExamenesFisicosTextArea to set
	 */
	public void setMapaHistoExamenesFisicosTextArea(
			HashMap mapaHistoExamenesFisicosTextArea) {
		this.mapaHistoExamenesFisicosTextArea = mapaHistoExamenesFisicosTextArea;
	}

	/**
	 * @return the mapaHistoConclusiones
	 */
	public HashMap getMapaHistoConclusiones() {
		return mapaHistoConclusiones;
	}

	/**
	 * @param mapaHistoConclusiones the mapaHistoConclusiones to set
	 */
	public void setMapaHistoConclusiones(HashMap mapaHistoConclusiones) {
		this.mapaHistoConclusiones = mapaHistoConclusiones;
	}

	/**
	 * @return the mapaHojaQuirur
	 */
	public HashMap getMapaHojaQuirur() {
		return mapaHojaQuirur;
	}

	/**
	 * @param mapaHojaQuirur the mapaHojaQuirur to set
	 */
	public void setMapaHojaQuirur(HashMap mapaHojaQuirur) {
		this.mapaHojaQuirur = mapaHojaQuirur;
	}

	/**
	 * @return the mapaNotasEnfer
	 */
	public HashMap getMapaNotasEnfer() {
		return mapaNotasEnfer;
	}

	/**
	 * @param mapaNotasEnfer the mapaNotasEnfer to set
	 */
	public void setMapaNotasEnfer(HashMap mapaNotasEnfer) {
		this.mapaNotasEnfer = mapaNotasEnfer;
	}

	/**
	 * @return the mapaNotasRecuperacion
	 */
	public HashMap getMapaNotasRecuperacion() {
		return mapaNotasRecuperacion;
	}

	/**
	 * @param mapaNotasRecuperacion the mapaNotasRecuperacion to set
	 */
	public void setMapaNotasRecuperacion(HashMap mapaNotasRecuperacion) {
		this.mapaNotasRecuperacion = mapaNotasRecuperacion;
	}

	/**
	 * @return the mapaCodigosPeticionCirugia
	 */
	public HashMap getMapaCodigosPeticionCirugia() {
		return mapaCodigosPeticionCirugia;
	}

	/**
	 * @param mapaCodigosPeticionCirugia the mapaCodigosPeticionCirugia to set
	 */
	public void setMapaCodigosPeticionCirugia(HashMap mapaCodigosPeticionCirugia) {
		this.mapaCodigosPeticionCirugia = mapaCodigosPeticionCirugia;
	}

	/**
	 * @return the codigoPk
	 */
	public Integer getCodigoPk() {
		return codigoPk;
	}

	/**
	 * @param codigoPk the codigoPk to set
	 */
	public void setCodigoPk(Integer codigoPk) {
		this.codigoPk = codigoPk;
	}

	/**
	 * @return the mapaNotasRecuperacionDetalle
	 */
	public HashMap getMapaNotasRecuperacionDetalle() {
		return mapaNotasRecuperacionDetalle;
	}

	/**
	 * @param mapaNotasRecuperacionDetalle the mapaNotasRecuperacionDetalle to set
	 */
	public void setMapaNotasRecuperacionDetalle(HashMap mapaNotasRecuperacionDetalle) {
		this.mapaNotasRecuperacionDetalle = mapaNotasRecuperacionDetalle;
	}
	
	/**
	 * @return the mapaIngresoSalidaPaciente
	 */
	public Map<Integer, IngresoSalidaPacienteDto> getMapaIngresoSalidaPaciente() {
		return mapaIngresoSalidaPaciente;
	}

	/**
	 * @param mapaIngresoSalidaPaciente the mapaIngresoSalidaPaciente to set
	 */
	public void setMapaIngresoSalidaPaciente(
			Map<Integer, IngresoSalidaPacienteDto> mapaIngresoSalidaPaciente) {
		this.mapaIngresoSalidaPaciente = mapaIngresoSalidaPaciente;
	}

	/**
	 * @return the mapaInformacionActoQuirurgico
	 */
	public Map<Integer, InformacionActoQxDto> getMapaInformacionActoQuirurgico() {
		return mapaInformacionActoQuirurgico;
	}

	/**
	 * @param mapaInformacionActoQuirurgico the mapaInformacionActoQuirurgico to set
	 */
	public void setMapaInformacionActoQuirurgico(
			Map<Integer, InformacionActoQxDto> mapaInformacionActoQuirurgico) {
		this.mapaInformacionActoQuirurgico = mapaInformacionActoQuirurgico;
	}

	/**
	 * @return the mapaEspecialidadesXSolicitud
	 */
	public Map<Integer, List<EspecialidadDto>> getMapaEspecialidadesXSolicitud() {
		return mapaEspecialidadesXSolicitud;
	}

	/**
	 * @param mapaEspecialidadesXSolicitud the mapaEspecialidadesXSolicitud to set
	 */
	public void setMapaEspecialidadesXSolicitud(
			Map<Integer, List<EspecialidadDto>> mapaEspecialidadesXSolicitud) {
		this.mapaEspecialidadesXSolicitud = mapaEspecialidadesXSolicitud;
	}

	/**
	 * @return the mapaDescripcionesOperatorias
	 */
	public Map<Integer, Map<Integer, InformeQxDto>> getMapaDescripcionesOperatorias() {
		return mapaDescripcionesOperatorias;
	}

	/**
	 * @param mapaDescripcionesOperatorias the mapaDescripcionesOperatorias to set
	 */
	public void setMapaDescripcionesOperatorias(
			Map<Integer, Map<Integer, InformeQxDto>> mapaDescripcionesOperatorias) {
		this.mapaDescripcionesOperatorias = mapaDescripcionesOperatorias;
	}

	/**
	 * @return the mapaNotasAclaratorias
	 */
	public Map<Integer, Map<Integer, List<NotaAclaratoriaDto>>> getMapaNotasAclaratorias() {
		return mapaNotasAclaratorias;
	}

	/**
	 * @param mapaNotasAclaratorias the mapaNotasAclaratorias to set
	 */
	public void setMapaNotasAclaratorias(
			Map<Integer, Map<Integer, List<NotaAclaratoriaDto>>> mapaNotasAclaratorias) {
		this.mapaNotasAclaratorias = mapaNotasAclaratorias;
	}

	/**
	 * @return the mapaNotasEnfermeria
	 */
	public Map<Integer, List<NotaEnfermeriaDto>> getMapaNotasEnfermeria() {
		return mapaNotasEnfermeria;
	}

	/**
	 * @param mapaNotasEnfermeria the mapaNotasEnfermeria to set
	 */
	public void setMapaNotasEnfermeria(
			Map<Integer, List<NotaEnfermeriaDto>> mapaNotasEnfermeria) {
		this.mapaNotasEnfermeria = mapaNotasEnfermeria;
	}

	/**
	 * @return the mapaNotasRecuperacionCirugia
	 */
	public Map<Integer, List<NotaRecuperacionDto>> getMapaNotasRecuperacionCirugia() {
		return mapaNotasRecuperacionCirugia;
	}

	/**
	 * @param mapaNotasRecuperacionCirugia the mapaNotasRecuperacionCirugia to set
	 */
	public void setMapaNotasRecuperacionCirugia(
			Map<Integer, List<NotaRecuperacionDto>> mapaNotasRecuperacionCirugia) {
		this.mapaNotasRecuperacionCirugia = mapaNotasRecuperacionCirugia;
	}
	
	

}
