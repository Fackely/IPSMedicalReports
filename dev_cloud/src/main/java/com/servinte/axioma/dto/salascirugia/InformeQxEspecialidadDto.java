package com.servinte.axioma.dto.salascirugia;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.manejoPaciente.DtoDiagnostico;

/**
 * @author jeilones
 * @created 17/06/2013
 *
 */
public class InformeQxEspecialidadDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1465797925760952070L;

	private int codigo;
	private EspecialidadDto especialidad;
	private List<ServicioHQxDto>servicios;
	private List<ServicioHQxDto>serviciosAEliminarse=new ArrayList<ServicioHQxDto>(0);
	private List<NotaAclaratoriaDto>notasAclaratoriasDto;
	private List<ProfesionalHQxDto>profesionales;
	
	private List<ProfesionalHQxDto>profesionalesAEliminarse=new ArrayList<ProfesionalHQxDto>(0);
	
	/**
	 * Este parametro se usa de dos maneras, 
	 * si se esta consultando siempre muestra la ultima patologia registrada,
	 * si se va a persistir se guardara el objeto en referencia, si no se requiere persistir se debe cambiar el valor a <B>null</B>
	 * */
	private PatologiaDto ultimaPatologia;
	private List<PatologiaDto>patologias;
	private TipoHeridaDto tipoHerida;
	
	private int codigoSolicitudesCirugia;
	
	private DtoDiagnostico diagnosticoPostOperatorioPrincipal;
	private DtoDiagnostico diagnosticoPostOperatorioComplicacion;
	private List<DtoDiagnostico> diagnosticosPostOperatorioRelacionados;
	private ProfesionalHQxDto profesionalRegistraDescripcionOperatoria;
	
	/**
	 * Este parametro se usa de dos maneras, 
	 * si se esta consultando siempre muestra la ultima descripcion operatoria registrada,
	 * si se va a persistir se guardara el objeto en referencia, si no se requiere persistir se debe cambiar el valor a <B>null</B>
	 */
	private DescripcionOperatoriaDto ultimaDescripcionOperatoria;
	private List<DescripcionOperatoriaDto> descripcionesOperatorias;
	private String hallazgos;
	private String complicaciones;
	private boolean usaMaterialesEspeciales;
	private String observacionesMaterialesEspeciales;

	private boolean confirmada;

	public InformeQxEspecialidadDto() {
		super();
	}

	/**
	 * Este parametro se usa de dos maneras, 
	 * si se esta consultando siempre muestra la ultima patologia registrada,
	 * si se va a persistir se guardara el objeto en referencia, si no se requiere persistir se debe cambiar el valor a <B>null</B>
	 * 
	 * @return the ultimaPatologia
	 */
	public PatologiaDto getUltimaPatologia() {
		return ultimaPatologia;
	}

	/**
	 * Este parametro se usa de dos maneras, 
	 * si se esta consultando siempre muestra la ultima patologia registrada,
	 * si se va a persistir se guardara el objeto en referencia, si no se requiere persistir se debe cambiar el valor a <B>null</B>
	 * 
	 * @param ultimaPatologia the ultimaPatologia to set
	 */
	public void setUltimaPatologia(PatologiaDto ultimaPatologia) {
		this.ultimaPatologia = ultimaPatologia;
	}

	public EspecialidadDto getEspecialidad() {
		return especialidad;
	}

	public void setEspecialidad(EspecialidadDto especialidad) {
		this.especialidad = especialidad;
	}

	public List<NotaAclaratoriaDto> getNotasAclaratoriasDto() {
		return notasAclaratoriasDto;
	}

	public void setNotasAclaratoriasDto(
			List<NotaAclaratoriaDto> notasAclaratoriasDto) {
		this.notasAclaratoriasDto = notasAclaratoriasDto;
	}

	public List<ProfesionalHQxDto> getProfesionales() {
		return profesionales;
	}

	public void setProfesionales(List<ProfesionalHQxDto> profesionales) {
		this.profesionales = profesionales;
	}

	public List<PatologiaDto> getPatologias() {
		return patologias;
	}

	public void setPatologias(List<PatologiaDto> patologias) {
		this.patologias = patologias;
	}

	public TipoHeridaDto getTipoHerida() {
		return tipoHerida;
	}

	public void setTipoHerida(TipoHeridaDto tipoHerida) {
		this.tipoHerida = tipoHerida;
	}

	public DtoDiagnostico getDiagnosticoPostOperatorioPrincipal() {
		return diagnosticoPostOperatorioPrincipal;
	}

	public void setDiagnosticoPostOperatorioPrincipal(
			DtoDiagnostico diagnosticoPostOperatorioPrincipal) {
		this.diagnosticoPostOperatorioPrincipal = diagnosticoPostOperatorioPrincipal;
	}

	public DtoDiagnostico getDiagnosticoPostOperatorioComplicacion() {
		return diagnosticoPostOperatorioComplicacion;
	}

	public void setDiagnosticoPostOperatorioComplicacion(
			DtoDiagnostico diagnosticoPostOperatorioComplicacion) {
		this.diagnosticoPostOperatorioComplicacion = diagnosticoPostOperatorioComplicacion;
	}

	public List<DtoDiagnostico> getDiagnosticosPostOperatorioRelacionados() {
		return diagnosticosPostOperatorioRelacionados;
	}

	public void setDiagnosticosPostOperatorioRelacionados(
			List<DtoDiagnostico> diagnosticosPostOperatorioRelacionados) {
		this.diagnosticosPostOperatorioRelacionados = diagnosticosPostOperatorioRelacionados;
	}

	public ProfesionalHQxDto getProfesionalRegistraDescripcionOperatoria() {
		return profesionalRegistraDescripcionOperatoria;
	}

	public void setProfesionalRegistraDescripcionOperatoria(
			ProfesionalHQxDto profesionalRegistraDescripcionOperatoria) {
		this.profesionalRegistraDescripcionOperatoria = profesionalRegistraDescripcionOperatoria;
	}

	public String getHallazgos() {
		return hallazgos;
	}

	public void setHallazgos(String hallazgos) {
		this.hallazgos = hallazgos;
	}

	public String getComplicaciones() {
		return complicaciones;
	}

	public void setComplicaciones(String complicaciones) {
		this.complicaciones = complicaciones;
	}

	public boolean isUsaMaterialesEspeciales() {
		return usaMaterialesEspeciales;
	}

	public void setUsaMaterialesEspeciales(boolean usaMaterialesEspeciales) {
		this.usaMaterialesEspeciales = usaMaterialesEspeciales;
	}

	public String getObservacionesMaterialesEspeciales() {
		return observacionesMaterialesEspeciales;
	}

	public void setObservacionesMaterialesEspeciales(
			String observacionesMaterialesEspeciales) {
		this.observacionesMaterialesEspeciales = observacionesMaterialesEspeciales;
	}

	public boolean isConfirmada() {
		return confirmada;
	}

	public void setConfirmada(boolean confirmada) {
		this.confirmada = confirmada;
	}

	public List<ServicioHQxDto> getServicios() {
		return servicios;
	}

	public void setServicios(List<ServicioHQxDto> servicios) {
		this.servicios = servicios;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public int getCodigoSolicitudesCirugia() {
		return codigoSolicitudesCirugia;
	}

	public void setCodigoSolicitudesCirugia(int codigoSolicitudesCirugia) {
		this.codigoSolicitudesCirugia = codigoSolicitudesCirugia;
	}

	public List<DescripcionOperatoriaDto> getDescripcionesOperatorias() {
		return descripcionesOperatorias;
	}

	public void setDescripcionesOperatorias(
			List<DescripcionOperatoriaDto> descripcionesOperatorias) {
		this.descripcionesOperatorias = descripcionesOperatorias;
	}


	/**
	 * @return the serviciosAEliminarse
	 */
	public List<ServicioHQxDto> getServiciosAEliminarse() {
		return serviciosAEliminarse;
	}

	/**
	 * @param serviciosAEliminarse the serviciosAEliminarse to set
	 */
	public void setServiciosAEliminarse(List<ServicioHQxDto> serviciosAEliminarse) {
		this.serviciosAEliminarse = serviciosAEliminarse;
	}

	/**
	 * Este parametro se usa de dos maneras, 
	 * si se esta consultando siempre muestra la ultima descripcion operatoria registrada,
	 * si se va a persistir se guardara el objeto en referencia, si no se requiere persistir se debe cambiar el valor a <B>null</B>
	 * 	
	 * @return the ultimaDescripcionOperatoria
	 */
	public DescripcionOperatoriaDto getUltimaDescripcionOperatoria() {
		return ultimaDescripcionOperatoria;
	}

	/**
	 * Este parametro se usa de dos maneras, 
	 * si se esta consultando siempre muestra la ultima descripcion operatoria registrada,
	 * si se va a persistir se guardara el objeto en referencia, si no se requiere persistir se debe cambiar el valor a <B>null</B>
	 * 
	 * @param ultimaDescripcionOperatoria the ultimaDescripcionOperatoria to set
	 */
	public void setUltimaDescripcionOperatoria(
			DescripcionOperatoriaDto ultimaDescripcionOperatoria) {
		this.ultimaDescripcionOperatoria = ultimaDescripcionOperatoria;
	}

	/**
	 * @return the profesionalesAEliminarse
	 */
	public List<ProfesionalHQxDto> getProfesionalesAEliminarse() {
		return profesionalesAEliminarse;
	}

	/**
	 * @param profesionalesAEliminarse the profesionalesAEliminarse to set
	 */
	public void setProfesionalesAEliminarse(
			List<ProfesionalHQxDto> profesionalesAEliminarse) {
		this.profesionalesAEliminarse = profesionalesAEliminarse;
	}

	public void agregarDiagnosticoPostOperatorioPrincipal (String idDiagnostico)
	{
		this.diagnosticoPostOperatorioPrincipal = new DtoDiagnostico();
		this.diagnosticoPostOperatorioPrincipal.asignarValoresSeparados(idDiagnostico);
		this.diagnosticoPostOperatorioPrincipal.organizarNombreCompletoDiagnostico();
	}
	
	public void agregardiagnosticoPostOperatorioComplicacion (String idDiagnostico)
	{
		this.diagnosticoPostOperatorioComplicacion = new DtoDiagnostico();
		this.diagnosticoPostOperatorioComplicacion.asignarValoresSeparados(idDiagnostico);
		this.diagnosticoPostOperatorioComplicacion.organizarNombreCompletoDiagnostico();
	}
}
