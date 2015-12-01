package com.sysmedica.actionform;

import java.util.Collection;

import org.apache.struts.validator.ValidatorForm;

public class FichasAnterioresForm extends ValidatorForm {

	Collection fichasAnteriores;
	String estado;
	private int codigoFichaInactiva;
	
	private String url = "";
	
	private int codigoPaciente;
	private String codigoDx;
	private String diagnostico;
	private int codigoConvenio;
	private boolean hayFichas;
	private String valorDivDx;
	
	private boolean fichamodulo;
	
	//	 Variables para las valoraciones
    private String valorDiagnostico;
    private String idDiagnostico;
    private String propiedadDiagnostico;
    private String idDiv;
    private String idCheckBox;
    private String idHiddenCheckBox;
    private String propiedadHiddenCheckBox;
    private int tipoDiagnostico;
    private int numero;
    private String idNumero;
    private String diagnosticosSeleccionados;
    private String idDiagSeleccionados;
    private String idValorFicha;
    private boolean epidemiologia;
	
	public void reset()
	{
		url = "";
		fichasAnteriores.clear();
	}

	public Collection getFichasAnteriores() {
		return fichasAnteriores;
	}

	public void setFichasAnteriores(Collection fichasAnteriores) {
		this.fichasAnteriores = fichasAnteriores;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getCodigoDx() {
		return codigoDx;
	}

	public void setCodigoDx(String codigoDx) {
		this.codigoDx = codigoDx;
	}

	public int getCodigoPaciente() {
		return codigoPaciente;
	}

	public void setCodigoPaciente(int codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}

	public String getDiagnostico() {
		return diagnostico;
	}

	public void setDiagnostico(String diagnostico) {
		this.diagnostico = diagnostico;
	}

	public int getCodigoConvenio() {
		return codigoConvenio;
	}

	public void setCodigoConvenio(int codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}

	public boolean isHayFichas() {
		return hayFichas;
	}

	public void setHayFichas(boolean hayFichas) {
		this.hayFichas = hayFichas;
	}

	public String getValorDivDx() {
		return valorDivDx;
	}

	public void setValorDivDx(String valorDivDx) {
		this.valorDivDx = valorDivDx;
	}

	public boolean isFichamodulo() {
		return fichamodulo;
	}

	public void setFichamodulo(boolean fichamodulo) {
		this.fichamodulo = fichamodulo;
	}

	public String getDiagnosticosSeleccionados() {
		return diagnosticosSeleccionados;
	}

	public void setDiagnosticosSeleccionados(String diagnosticosSeleccionados) {
		this.diagnosticosSeleccionados = diagnosticosSeleccionados;
	}

	public boolean isEpidemiologia() {
		return epidemiologia;
	}

	public void setEpidemiologia(boolean epidemiologia) {
		this.epidemiologia = epidemiologia;
	}

	public String getIdCheckBox() {
		return idCheckBox;
	}

	public void setIdCheckBox(String idCheckBox) {
		this.idCheckBox = idCheckBox;
	}

	public String getIdDiagnostico() {
		return idDiagnostico;
	}

	public void setIdDiagnostico(String idDiagnostico) {
		this.idDiagnostico = idDiagnostico;
	}

	public String getIdDiagSeleccionados() {
		return idDiagSeleccionados;
	}

	public void setIdDiagSeleccionados(String idDiagSeleccionados) {
		this.idDiagSeleccionados = idDiagSeleccionados;
	}

	public String getIdDiv() {
		return idDiv;
	}

	public void setIdDiv(String idDiv) {
		this.idDiv = idDiv;
	}

	public String getIdHiddenCheckBox() {
		return idHiddenCheckBox;
	}

	public void setIdHiddenCheckBox(String idHiddenCheckBox) {
		this.idHiddenCheckBox = idHiddenCheckBox;
	}

	public String getIdNumero() {
		return idNumero;
	}

	public void setIdNumero(String idNumero) {
		this.idNumero = idNumero;
	}

	public String getIdValorFicha() {
		return idValorFicha;
	}

	public void setIdValorFicha(String idValorFicha) {
		this.idValorFicha = idValorFicha;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public String getPropiedadDiagnostico() {
		return propiedadDiagnostico;
	}

	public void setPropiedadDiagnostico(String propiedadDiagnostico) {
		this.propiedadDiagnostico = propiedadDiagnostico;
	}

	public String getPropiedadHiddenCheckBox() {
		return propiedadHiddenCheckBox;
	}

	public void setPropiedadHiddenCheckBox(String propiedadHiddenCheckBox) {
		this.propiedadHiddenCheckBox = propiedadHiddenCheckBox;
	}

	public int getTipoDiagnostico() {
		return tipoDiagnostico;
	}

	public void setTipoDiagnostico(int tipoDiagnostico) {
		this.tipoDiagnostico = tipoDiagnostico;
	}

	public String getValorDiagnostico() {
		return valorDiagnostico;
	}

	public void setValorDiagnostico(String valorDiagnostico) {
		this.valorDiagnostico = valorDiagnostico;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getCodigoFichaInactiva() {
		return codigoFichaInactiva;
	}

	public void setCodigoFichaInactiva(int codigoFichaInactiva) {
		this.codigoFichaInactiva = codigoFichaInactiva;
	}
}
