package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;

/**
 * 
 * @author wilson
 *
 */
public class DtoFURTRAN implements Serializable 
{
	/**
	 * 
	 */
	public String[] nombresCampos=	{"",
									/*1*/"Nro. Radicado Anterior",
									/*2*/"RG respuesta a glosa",
									/*3*/"Nombre de la empresa de transporte o razon social",
									/*4*/"Codigo de habilitacion del prestador de servicios de salud",
									/*5*/"Primer apellido de la persona natural reclamante",
									/*6*/"segundo apellido de la persona natural reclamante",
									/*7*/"primer nombre de la persona natural reclamante",
									/*8*/"segundo nombre de la persona natural reclamante",
									/*9*/"tipo de documento de identidad reclamante",
									/*10*/"numero documento victima",
									/*11*/"tipo de vehiculo o de servicio de ambulancia",
									/*12*/"otro tipo de vehiculo o servicio de ambulancia",
									/*13*/"placa del vehiculo",
									/*14*/"direccion del reclamante",
									/*15*/"telefono del reclamante",
									/*16*/"codigo del departamento de residencia del reclamante",
									/*17*/"codigo del municipio de residencia del reclamante",
									/*18*/"tipo de documento de identidad de la victima",
									/*19*/"numero de documento de identidad de la victima",
									/*20*/"primer nombre de la victima",
									/*21*/"segundo nombre de la victima",
									/*22*/"primer apellido de la victima",
									/*23*/"segundo apellido de la victima",
									/*24*/"tipo de evento que suscita la movilizacion",
									/*25*/"direccion donde se recoge la victima",
									/*26*/"codigo departamento donde se recoge la victima",
									/*27*/"codigo municipio donde se recoge la victima",
									/*28*/"zona donde recoge la victima",
									/*29*/"fecha de traslado de la victima al centro asistencial",
									/*30*/"hora de traslado al centro asistencial",
									/*31*/"codigo de habilitacion de IPS que atendio victima",
									/*32*/"codigo del departamento donde se recoge la victima",
									/*33*/"codigo del municipio donde recoge la victima",
									/*34*/"total folios"
									};
	
	private String $1_nroRadicadoAnterior;
	private String $2_rg;
	private String $3_nombreEmpresaTransporteOrazonSocial;
	private String $4_codHabilitacionPrestadorServicioSalud;
	private String $5_primerApellidoPersonaReclamante;
	private String $6_segundoApellidoPersonaReclamante;
	private String $7_primerNombrePersonaReclamante;
	private String $8_segundoNombrePersonaReclamante;
	private String $9_tipoDocIdReclamante;
	private String $10_numeroDocumentoVictima;
	private String $11_tipoVehiculoOServicioAmb;
	private String $12_otroTipoVehiculoOServAmb;
	private String $13_placaVehiculo;
	private String $14_dirReclamante;
	private String $15_telReclamante;
	private String $16_codDeptoResidenciaReclamante;
	private String $17_codigoMuncipioResidenciaReclamante;
	private String $18_tipoDocIdVictima;
	private String $19_numeroDocIdVictima;
	private String $20_primerNombreVictima;
	private String $21_segundoNombreVictima;
	private String $22_primerApellidoVictima;
	private String $23_segundoApellidoVictima;
	private String $24_tipoEventoSuscritaMovilizacion;
	private String $25_dirDondeRecogeVictima;
	private String $26_codDeptoDondeRecogeVictima;
	private String $27_codMunicipioDondeRecogeVictima;
	private String $28_zonaDondeRecogeVictima;
	private String $29_fechaTrasladoVictimaCentroAsistencial;
	private String $30_horaTrasladoCentroAsistencial;
	private String $31_codHabilitacionIPSAtendioVictima;
	private String $32_codDeptoRecogeVictima;
	private String $33_codMunicipioRecogeVictima;
	private String $34_totalFolios;
	
	/**
	 * 
	 */
	public DtoFURTRAN() 
	{
		this.$1_nroRadicadoAnterior="";
		this.$2_rg="";
		this.$3_nombreEmpresaTransporteOrazonSocial="";
		this.$4_codHabilitacionPrestadorServicioSalud="";
		this.$5_primerApellidoPersonaReclamante="";
		this.$6_segundoApellidoPersonaReclamante="";
		this.$7_primerNombrePersonaReclamante="";
		this.$8_segundoNombrePersonaReclamante="";
		this.$9_tipoDocIdReclamante="";
		this.$10_numeroDocumentoVictima="";
		this.$11_tipoVehiculoOServicioAmb="";
		this.$12_otroTipoVehiculoOServAmb="";
		this.$13_placaVehiculo="";
		this.$14_dirReclamante="";
		this.$15_telReclamante="";
		this.$16_codDeptoResidenciaReclamante="";
		this.$17_codigoMuncipioResidenciaReclamante="";
		this.$18_tipoDocIdVictima="";
		this.$19_numeroDocIdVictima="";
		this.$20_primerNombreVictima="";
		this.$21_segundoNombreVictima="";
		this.$22_primerApellidoVictima="";
		this.$23_segundoApellidoVictima="";
		this.$24_tipoEventoSuscritaMovilizacion="";
		this.$25_dirDondeRecogeVictima="";
		this.$26_codDeptoDondeRecogeVictima="";
		this.$27_codMunicipioDondeRecogeVictima="";
		this.$28_zonaDondeRecogeVictima="";
		this.$29_fechaTrasladoVictimaCentroAsistencial="";
		this.$30_horaTrasladoCentroAsistencial="";
		this.$31_codHabilitacionIPSAtendioVictima="";
		this.$32_codDeptoRecogeVictima="";
		this.$33_codMunicipioRecogeVictima="";
		this.$34_totalFolios="";
	}

	/**
	 * @return the nombresCampos
	 */
	public String[] getNombresCampos() {
		return nombresCampos;
	}

	/**
	 * @param nombresCampos the nombresCampos to set
	 */
	public void setNombresCampos(String[] nombresCampos) {
		this.nombresCampos = nombresCampos;
	}

	/**
	 * @return the $1_nroRadicadoAnterior
	 */
	public String get$1_nroRadicadoAnterior() {
		return $1_nroRadicadoAnterior;
	}

	/**
	 * @param radicadoAnterior the $1_nroRadicadoAnterior to set
	 */
	public void set$1_nroRadicadoAnterior(String radicadoAnterior) {
		$1_nroRadicadoAnterior = radicadoAnterior;
	}

	/**
	 * @return the $2_rg
	 */
	public String get$2_rg() {
		return $2_rg;
	}

	/**
	 * @param $2_rg the $2_rg to set
	 */
	public void set$2_rg(String $2_rg) {
		this.$2_rg = $2_rg;
	}

	/**
	 * @return the $3_nombreEmpresaTransporteOrazonSocial
	 */
	public String get$3_nombreEmpresaTransporteOrazonSocial() {
		return $3_nombreEmpresaTransporteOrazonSocial;
	}

	/**
	 * @param empresaTransporteOrazonSocial the $3_nombreEmpresaTransporteOrazonSocial to set
	 */
	public void set$3_nombreEmpresaTransporteOrazonSocial(
			String empresaTransporteOrazonSocial) {
		$3_nombreEmpresaTransporteOrazonSocial = empresaTransporteOrazonSocial;
	}

	/**
	 * @return the $4_codHabilitacionPrestadorServicioSalud
	 */
	public String get$4_codHabilitacionPrestadorServicioSalud() {
		return $4_codHabilitacionPrestadorServicioSalud;
	}

	/**
	 * @param habilitacionPrestadorServicioSalud the $4_codHabilitacionPrestadorServicioSalud to set
	 */
	public void set$4_codHabilitacionPrestadorServicioSalud(
			String habilitacionPrestadorServicioSalud) {
		$4_codHabilitacionPrestadorServicioSalud = habilitacionPrestadorServicioSalud;
	}

	/**
	 * @return the $5_primerApellidoPersonaReclamante
	 */
	public String get$5_primerApellidoPersonaReclamante() {
		return $5_primerApellidoPersonaReclamante;
	}

	/**
	 * @param apellidoPersonaReclamante the $5_primerApellidoPersonaReclamante to set
	 */
	public void set$5_primerApellidoPersonaReclamante(
			String apellidoPersonaReclamante) {
		$5_primerApellidoPersonaReclamante = apellidoPersonaReclamante;
	}

	/**
	 * @return the $6_segundoApellidoPersonaReclamante
	 */
	public String get$6_segundoApellidoPersonaReclamante() {
		return $6_segundoApellidoPersonaReclamante;
	}

	/**
	 * @param apellidoPersonaReclamante the $6_segundoApellidoPersonaReclamante to set
	 */
	public void set$6_segundoApellidoPersonaReclamante(
			String apellidoPersonaReclamante) {
		$6_segundoApellidoPersonaReclamante = apellidoPersonaReclamante;
	}

	/**
	 * @return the $7_primerNombrePersonaReclamante
	 */
	public String get$7_primerNombrePersonaReclamante() {
		return $7_primerNombrePersonaReclamante;
	}

	/**
	 * @param nombrePersonaReclamante the $7_primerNombrePersonaReclamante to set
	 */
	public void set$7_primerNombrePersonaReclamante(String nombrePersonaReclamante) {
		$7_primerNombrePersonaReclamante = nombrePersonaReclamante;
	}

	/**
	 * @return the $8_segundoNombrePersonaReclamante
	 */
	public String get$8_segundoNombrePersonaReclamante() {
		return $8_segundoNombrePersonaReclamante;
	}

	/**
	 * @param nombrePersonaReclamante the $8_segundoNombrePersonaReclamante to set
	 */
	public void set$8_segundoNombrePersonaReclamante(String nombrePersonaReclamante) {
		$8_segundoNombrePersonaReclamante = nombrePersonaReclamante;
	}

	/**
	 * @return the $9_tipoDocIdReclamante
	 */
	public String get$9_tipoDocIdReclamante() {
		return $9_tipoDocIdReclamante;
	}

	/**
	 * @param docIdReclamante the $9_tipoDocIdReclamante to set
	 */
	public void set$9_tipoDocIdReclamante(String docIdReclamante) {
		$9_tipoDocIdReclamante = docIdReclamante;
	}

	/**
	 * @return the $10_numeroDocumentoVictima
	 */
	public String get$10_numeroDocumentoVictima() {
		return $10_numeroDocumentoVictima;
	}

	/**
	 * @param documentoVictima the $10_numeroDocumentoVictima to set
	 */
	public void set$10_numeroDocumentoVictima(String documentoVictima) {
		$10_numeroDocumentoVictima = documentoVictima;
	}

	/**
	 * @return the $11_tipoVehiculoOServicioAmb
	 */
	public String get$11_tipoVehiculoOServicioAmb() {
		return $11_tipoVehiculoOServicioAmb;
	}

	/**
	 * @param vehiculoOServicioAmb the $11_tipoVehiculoOServicioAmb to set
	 */
	public void set$11_tipoVehiculoOServicioAmb(String vehiculoOServicioAmb) {
		$11_tipoVehiculoOServicioAmb = vehiculoOServicioAmb;
	}

	/**
	 * @return the $12_otroTipoVehiculoOServAmb
	 */
	public String get$12_otroTipoVehiculoOServAmb() {
		return $12_otroTipoVehiculoOServAmb;
	}

	/**
	 * @param tipoVehiculoOServAmb the $12_otroTipoVehiculoOServAmb to set
	 */
	public void set$12_otroTipoVehiculoOServAmb(String tipoVehiculoOServAmb) {
		$12_otroTipoVehiculoOServAmb = tipoVehiculoOServAmb;
	}

	/**
	 * @return the $13_placaVehiculo
	 */
	public String get$13_placaVehiculo() {
		return $13_placaVehiculo;
	}

	/**
	 * @param vehiculo the $13_placaVehiculo to set
	 */
	public void set$13_placaVehiculo(String vehiculo) {
		$13_placaVehiculo = vehiculo;
	}

	/**
	 * @return the $14_dirReclamante
	 */
	public String get$14_dirReclamante() {
		return $14_dirReclamante;
	}

	/**
	 * @param reclamante the $14_dirReclamante to set
	 */
	public void set$14_dirReclamante(String reclamante) {
		$14_dirReclamante = reclamante;
	}

	/**
	 * @return the $15_telReclamante
	 */
	public String get$15_telReclamante() {
		return $15_telReclamante;
	}

	/**
	 * @param reclamante the $15_telReclamante to set
	 */
	public void set$15_telReclamante(String reclamante) {
		$15_telReclamante = reclamante;
	}

	/**
	 * @return the $16_codDeptoResidenciaReclamante
	 */
	public String get$16_codDeptoResidenciaReclamante() {
		return $16_codDeptoResidenciaReclamante;
	}

	/**
	 * @param deptoResidenciaReclamante the $16_codDeptoResidenciaReclamante to set
	 */
	public void set$16_codDeptoResidenciaReclamante(String deptoResidenciaReclamante) {
		$16_codDeptoResidenciaReclamante = deptoResidenciaReclamante;
	}

	/**
	 * @return the $17_codigoMuncipioResidenciaReclamante
	 */
	public String get$17_codigoMuncipioResidenciaReclamante() {
		return $17_codigoMuncipioResidenciaReclamante;
	}

	/**
	 * @param muncipioResidenciaReclamante the $17_codigoMuncipioResidenciaReclamante to set
	 */
	public void set$17_codigoMuncipioResidenciaReclamante(
			String muncipioResidenciaReclamante) {
		$17_codigoMuncipioResidenciaReclamante = muncipioResidenciaReclamante;
	}

	/**
	 * @return the $18_tipoDocIdVictima
	 */
	public String get$18_tipoDocIdVictima() {
		return $18_tipoDocIdVictima;
	}

	/**
	 * @param docIdVictima the $18_tipoDocIdVictima to set
	 */
	public void set$18_tipoDocIdVictima(String docIdVictima) {
		$18_tipoDocIdVictima = docIdVictima;
	}

	/**
	 * @return the $19_numeroDocIdVictima
	 */
	public String get$19_numeroDocIdVictima() {
		return $19_numeroDocIdVictima;
	}

	/**
	 * @param docIdVictima the $19_numeroDocIdVictima to set
	 */
	public void set$19_numeroDocIdVictima(String docIdVictima) {
		$19_numeroDocIdVictima = docIdVictima;
	}

	/**
	 * @return the $20_primerNombreVictima
	 */
	public String get$20_primerNombreVictima() {
		return $20_primerNombreVictima;
	}

	/**
	 * @param nombreVictima the $20_primerNombreVictima to set
	 */
	public void set$20_primerNombreVictima(String nombreVictima) {
		$20_primerNombreVictima = nombreVictima;
	}

	/**
	 * @return the $21_segundoNombreVictima
	 */
	public String get$21_segundoNombreVictima() {
		return $21_segundoNombreVictima;
	}

	/**
	 * @param nombreVictima the $21_segundoNombreVictima to set
	 */
	public void set$21_segundoNombreVictima(String nombreVictima) {
		$21_segundoNombreVictima = nombreVictima;
	}

	/**
	 * @return the $22_primerApellidoVictima
	 */
	public String get$22_primerApellidoVictima() {
		return $22_primerApellidoVictima;
	}

	/**
	 * @param apellidoVictima the $22_primerApellidoVictima to set
	 */
	public void set$22_primerApellidoVictima(String apellidoVictima) {
		$22_primerApellidoVictima = apellidoVictima;
	}

	/**
	 * @return the $23_segundoApellidoVictima
	 */
	public String get$23_segundoApellidoVictima() {
		return $23_segundoApellidoVictima;
	}

	/**
	 * @param apellidoVictima the $23_segundoApellidoVictima to set
	 */
	public void set$23_segundoApellidoVictima(String apellidoVictima) {
		$23_segundoApellidoVictima = apellidoVictima;
	}

	/**
	 * @return the $24_tipoEventoSuscritaMovilizacion
	 */
	public String get$24_tipoEventoSuscritaMovilizacion() {
		return $24_tipoEventoSuscritaMovilizacion;
	}

	/**
	 * @param eventoSuscritaMovilizacion the $24_tipoEventoSuscritaMovilizacion to set
	 */
	public void set$24_tipoEventoSuscritaMovilizacion(
			String eventoSuscritaMovilizacion) {
		$24_tipoEventoSuscritaMovilizacion = eventoSuscritaMovilizacion;
	}

	/**
	 * @return the $25_dirDondeRecogeVictima
	 */
	public String get$25_dirDondeRecogeVictima() {
		return $25_dirDondeRecogeVictima;
	}

	/**
	 * @param dondeRecogeVictima the $25_dirDondeRecogeVictima to set
	 */
	public void set$25_dirDondeRecogeVictima(String dondeRecogeVictima) {
		$25_dirDondeRecogeVictima = dondeRecogeVictima;
	}

	/**
	 * @return the $26_codDeptoDondeRecogeVictima
	 */
	public String get$26_codDeptoDondeRecogeVictima() {
		return $26_codDeptoDondeRecogeVictima;
	}

	/**
	 * @param deptoDondeRecogeVictima the $26_codDeptoDondeRecogeVictima to set
	 */
	public void set$26_codDeptoDondeRecogeVictima(String deptoDondeRecogeVictima) {
		$26_codDeptoDondeRecogeVictima = deptoDondeRecogeVictima;
	}

	/**
	 * @return the $27_codMunicipioDondeRecogeVictima
	 */
	public String get$27_codMunicipioDondeRecogeVictima() {
		return $27_codMunicipioDondeRecogeVictima;
	}

	/**
	 * @param municipioDondeRecogeVictima the $27_codMunicipioDondeRecogeVictima to set
	 */
	public void set$27_codMunicipioDondeRecogeVictima(
			String municipioDondeRecogeVictima) {
		$27_codMunicipioDondeRecogeVictima = municipioDondeRecogeVictima;
	}

	/**
	 * @return the $28_zonaDondeRecogeVictima
	 */
	public String get$28_zonaDondeRecogeVictima() {
		return $28_zonaDondeRecogeVictima;
	}

	/**
	 * @param dondeRecogeVictima the $28_zonaDondeRecogeVictima to set
	 */
	public void set$28_zonaDondeRecogeVictima(String dondeRecogeVictima) {
		$28_zonaDondeRecogeVictima = dondeRecogeVictima;
	}

	/**
	 * @return the $29_fechaTrasladoVictimaCentroAsistencial
	 */
	public String get$29_fechaTrasladoVictimaCentroAsistencial() {
		return $29_fechaTrasladoVictimaCentroAsistencial;
	}

	/**
	 * @param trasladoVictimaCentroAsistencial the $29_fechaTrasladoVictimaCentroAsistencial to set
	 */
	public void set$29_fechaTrasladoVictimaCentroAsistencial(
			String trasladoVictimaCentroAsistencial) {
		$29_fechaTrasladoVictimaCentroAsistencial = trasladoVictimaCentroAsistencial;
	}

	/**
	 * @return the $30_horaTrasladoCentroAsistencial
	 */
	public String get$30_horaTrasladoCentroAsistencial() {
		return $30_horaTrasladoCentroAsistencial;
	}

	/**
	 * @param trasladoCentroAsistencial the $30_horaTrasladoCentroAsistencial to set
	 */
	public void set$30_horaTrasladoCentroAsistencial(
			String trasladoCentroAsistencial) {
		$30_horaTrasladoCentroAsistencial = trasladoCentroAsistencial;
	}

	/**
	 * @return the $31_codHabilitacionIPSAtendioVictima
	 */
	public String get$31_codHabilitacionIPSAtendioVictima() {
		return $31_codHabilitacionIPSAtendioVictima;
	}

	/**
	 * @param habilitacionIPSAtendioVictima the $31_codHabilitacionIPSAtendioVictima to set
	 */
	public void set$31_codHabilitacionIPSAtendioVictima(
			String habilitacionIPSAtendioVictima) {
		$31_codHabilitacionIPSAtendioVictima = habilitacionIPSAtendioVictima;
	}

	/**
	 * @return the $32_codDeptoRecogeVictima
	 */
	public String get$32_codDeptoRecogeVictima() {
		return $32_codDeptoRecogeVictima;
	}

	/**
	 * @param deptoRecogeVictima the $32_codDeptoRecogeVictima to set
	 */
	public void set$32_codDeptoRecogeVictima(String deptoRecogeVictima) {
		$32_codDeptoRecogeVictima = deptoRecogeVictima;
	}

	/**
	 * @return the $33_codMunicipioRecogeVictima
	 */
	public String get$33_codMunicipioRecogeVictima() {
		return $33_codMunicipioRecogeVictima;
	}

	/**
	 * @param municipioRecogeVictima the $33_codMunicipioRecogeVictima to set
	 */
	public void set$33_codMunicipioRecogeVictima(String municipioRecogeVictima) {
		$33_codMunicipioRecogeVictima = municipioRecogeVictima;
	}

	/**
	 * @return the $34_totalFolios
	 */
	public String get$34_totalFolios() {
		return $34_totalFolios;
	}

	/**
	 * @param folios the $34_totalFolios to set
	 */
	public void set$34_totalFolios(String folios) {
		$34_totalFolios = folios;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public Object getTotalCadenaDto() 
	{
		return 	(this.$1_nroRadicadoAnterior+","+
				this.$2_rg+","+
				this.$3_nombreEmpresaTransporteOrazonSocial+","+
				this.$4_codHabilitacionPrestadorServicioSalud+","+
				this.$5_primerApellidoPersonaReclamante+","+
				this.$6_segundoApellidoPersonaReclamante+","+
				this.$7_primerNombrePersonaReclamante+","+
				this.$8_segundoNombrePersonaReclamante+","+
				this.$9_tipoDocIdReclamante+","+
				this.$10_numeroDocumentoVictima+","+
				this.$11_tipoVehiculoOServicioAmb+","+
				this.$12_otroTipoVehiculoOServAmb+","+
				this.$13_placaVehiculo+","+
				this.$14_dirReclamante+","+
				this.$15_telReclamante+","+
				this.$16_codDeptoResidenciaReclamante+","+
				this.$17_codigoMuncipioResidenciaReclamante+","+
				this.$18_tipoDocIdVictima+","+
				this.$19_numeroDocIdVictima+","+
				this.$20_primerNombreVictima+","+
				this.$21_segundoNombreVictima+","+
				this.$22_primerApellidoVictima+","+
				this.$23_segundoApellidoVictima+","+
				this.$24_tipoEventoSuscritaMovilizacion+","+
				this.$25_dirDondeRecogeVictima+","+
				this.$26_codDeptoDondeRecogeVictima+","+
				this.$27_codMunicipioDondeRecogeVictima+","+
				this.$28_zonaDondeRecogeVictima+","+
				this.$29_fechaTrasladoVictimaCentroAsistencial+","+
				this.$30_horaTrasladoCentroAsistencial+","+
				this.$31_codHabilitacionIPSAtendioVictima+","+
				this.$32_codDeptoRecogeVictima+","+
				this.$33_codMunicipioRecogeVictima+","+
				this.$34_totalFolios).toUpperCase();
	}
}
