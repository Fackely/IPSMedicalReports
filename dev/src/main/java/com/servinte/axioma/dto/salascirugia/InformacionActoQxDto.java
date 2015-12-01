package com.servinte.axioma.dto.salascirugia;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.princetonsa.dto.manejoPaciente.DtoDiagnostico;

/**
 * @author jeilones
 * @created 17/06/2013
 *
 */
public class InformacionActoQxDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2451445646486526275L;

	private Boolean participaAnestesiologo;
	private ProfesionalHQxDto anestesiologo;
	private DtoDiagnostico diagnosticoPrincipal;
	private List<DtoDiagnostico>diagnosticosRelacionados;
	private TipoAnestesiaDto tipoAnestesia;
	
	private Boolean finalizadaHQx;
	private Date fechaFinalizaHQx;
	private String horaFinalizaHQx;
	private Boolean politrauma;
	private boolean existeHojaAnestesia;
	
	private Boolean mostrarFila1=false;
	private Boolean mostrarFila2=false;
	private Boolean mostrarFila3=false;
	private Boolean mostrarFila4=false;
	private Boolean mostrarFila5=false;
	private Boolean mostrarFila6=false;
	
	public ProfesionalHQxDto getAnestesiologo() {
		return anestesiologo;
	}
	public void setAnestesiologo(ProfesionalHQxDto anestesiologo) {
		this.anestesiologo = anestesiologo;
	}
	public DtoDiagnostico getDiagnosticoPrincipal() {
		return diagnosticoPrincipal;
	}
	public void setDiagnosticoPrincipal(DtoDiagnostico diagnosticoPrincipal) {
		this.diagnosticoPrincipal = diagnosticoPrincipal;
	}
	public List<DtoDiagnostico> getDiagnosticosRelacionados() {
		return diagnosticosRelacionados;
	}
	public void setDiagnosticosRelacionados(
			List<DtoDiagnostico> diagnosticosRelacionados) {
		this.diagnosticosRelacionados = diagnosticosRelacionados;
	}
	public TipoAnestesiaDto getTipoAnestesia() {
		return tipoAnestesia;
	}
	public void setTipoAnestesia(TipoAnestesiaDto tipoAnestesia) {
		this.tipoAnestesia = tipoAnestesia;
	}
	public Boolean getParticipaAnestesiologo() {
		return participaAnestesiologo;
	}
	public void setParticipaAnestesiologo(Boolean participaAnestesiologo) {
		this.participaAnestesiologo = participaAnestesiologo;
	}
	public Boolean getPolitrauma() {
		return politrauma;
	}
	public void setPolitrauma(Boolean politrauma) {
		this.politrauma = politrauma;
	}
	public Date getFechaFinalizaHQx() {
		return fechaFinalizaHQx;
	}
	public void setFechaFinalizaHQx(Date fechaFinalizaHQx) {
		this.fechaFinalizaHQx = fechaFinalizaHQx;
	}
	public String getHoraFinalizaHQx() {
		return horaFinalizaHQx;
	}
	public void setHoraFinalizaHQx(String horaFinalizaHQx) {
		this.horaFinalizaHQx = horaFinalizaHQx;
	}
	public Boolean getFinalizadaHQx() {
		return finalizadaHQx;
	}
	public void setFinalizadaHQx(Boolean finalizadaHQx) {
		this.finalizadaHQx = finalizadaHQx;
	}
	public boolean isExisteHojaAnestesia() {
		return existeHojaAnestesia;
	}
	public void setExisteHojaAnestesia(boolean existeHojaAnestesia) {
		this.existeHojaAnestesia = existeHojaAnestesia;
	}
	public void agregarDiagnosticoPrincipal(String idDiagnostico){
		this.diagnosticoPrincipal = new DtoDiagnostico();
		this.diagnosticoPrincipal.asignarValoresSeparados(idDiagnostico);
		this.diagnosticoPrincipal.organizarNombreCompletoDiagnostico();
	}
	public void agregarDiagnosticoRelacionado(String idDiagnostico){
		DtoDiagnostico dto = new DtoDiagnostico();
		dto.asignarValoresSeparados(idDiagnostico);
		dto.organizarNombreCompletoDiagnostico();
		this.diagnosticosRelacionados.add(dto);
	}
	@Override
	public String toString() {
		return "InformacionActoQxDto [participaAnestesiologo="
				+ participaAnestesiologo + ", anestesiologo=" + anestesiologo
				+ ", diagnosticoPrincipal=" + diagnosticoPrincipal
				+ ", diagnosticosRelacionados=" + diagnosticosRelacionados
				+ ", tipoAnestesia=" + tipoAnestesia + ", finalizadaHQx="
				+ finalizadaHQx + ", fechaFinalizaHQx=" + fechaFinalizaHQx
				+ ", horaFinalizaHQx=" + horaFinalizaHQx + ", politrauma="
				+ politrauma + ", existeHojaAnestesia=" + existeHojaAnestesia
				+ "]";
	}
	/**
	 * @return the mostrarFila1
	 */
	public Boolean getMostrarFila1() {
		return mostrarFila1;
	}
	/**
	 * @param mostrarFila1 the mostrarFila1 to set
	 */
	public void setMostrarFila1(Boolean mostrarFila1) {
		this.mostrarFila1 = mostrarFila1;
	}
	/**
	 * @return the mostrarFila2
	 */
	public Boolean getMostrarFila2() {
		return mostrarFila2;
	}
	/**
	 * @param mostrarFila2 the mostrarFila2 to set
	 */
	public void setMostrarFila2(Boolean mostrarFila2) {
		this.mostrarFila2 = mostrarFila2;
	}
	/**
	 * @return the mostrarFila3
	 */
	public Boolean getMostrarFila3() {
		return mostrarFila3;
	}
	/**
	 * @param mostrarFila3 the mostrarFila3 to set
	 */
	public void setMostrarFila3(Boolean mostrarFila3) {
		this.mostrarFila3 = mostrarFila3;
	}
	/**
	 * @return the mostrarFila4
	 */
	public Boolean getMostrarFila4() {
		return mostrarFila4;
	}
	/**
	 * @param mostrarFila4 the mostrarFila4 to set
	 */
	public void setMostrarFila4(Boolean mostrarFila4) {
		this.mostrarFila4 = mostrarFila4;
	}
	/**
	 * @return the mostrarFila5
	 */
	public Boolean getMostrarFila5() {
		return mostrarFila5;
	}
	/**
	 * @param mostrarFila5 the mostrarFila5 to set
	 */
	public void setMostrarFila5(Boolean mostrarFila5) {
		this.mostrarFila5 = mostrarFila5;
	}
	
	/**
	 * @return the mostrarFila6
	 */
	public Boolean getMostrarFila6() {
		return mostrarFila6;
	}
	/**
	 * @param mostrarFila6 the mostrarFila6 to set
	 */
	public void setMostrarFila6(Boolean mostrarFila6) {
		this.mostrarFila6 = mostrarFila6;
	}
	
	public boolean getMostrarSeccionInformacionActoQx(){
		return this.mostrarFila1||this.mostrarFila2||this.mostrarFila3||this.mostrarFila4||this.mostrarFila5||this.mostrarFila6;
	}
}