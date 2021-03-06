package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * RegistroAccidentesTransito generated by hbm2java
 */
public class RegistroAccidentesTransito implements java.io.Serializable {

	private long codigo;
	private TiposIdentificacion tiposIdentificacionByTipoIdConductor;
	private Ciudades ciudadesByFkRatCiudTrans;
	private Convenios convenios;
	private Ciudades ciudadesByFkRatCiudExpIdTrans;
	private Ciudades ciudadesByFkRatCiudProp;
	private Ciudades ciudadesByFkRatCiu3;
	private TiposIdentificacion tiposIdentificacionByTipoIdProp;
	private TiposIdentificacion tiposIdentificacionByTipoIdDeclarante;
	private TipoServVehiculos tipoServVehiculos;
	private Ciudades ciudadesByFkRatCiudExpIdProp;
	private Usuarios usuarios;
	private Ciudades ciudadesByFkRatCiu5;
	private Ciudades ciudadesByFkRatCiu2;
	private Instituciones instituciones;
	private Ciudades ciudadesByFkRatCiu1;
	private TiposIdentificacion tiposIdentificacionByTipoIdTransporta;
	private Ciudades ciudadesByFkRatCiu6;
	private String empresaTrabaja;
	private String direccionEmpresa;
	private String telefonoEmpresa;
	private Character ocupante;
	private String condicionAccidentado;
	private String resultaLesionadoAl;
	private String placaVehiculoOcasiona;
	private String lugarAccidente;
	private Date fechaAccidente;
	private String horaAccidente;
	private String zonaAccidente;
	private String informacionAccidente;
	private String marcaVehiculo;
	private String placa;
	private String otroTipoServV;
	private String agencia;
	private String asegurado;
	private String numeroPoliza;
	private Date fechaInicialPoliza;
	private Date fechaFinalPoliza;
	private String numeroIdConductor;
	private String direccionConductor;
	private String telefonoConductor;
	private String apellidoNombreDeclarante;
	private String numeroIdDeclarante;
	private Date fechaGrabacion;
	private String horaGrabacion;
	private String estado;
	private Date fechaAnulacion;
	private String horaAnulacion;
	private String usuarioAnulacion;
	private String poliza;
	private String primerApellidoProp;
	private String segundoApellidoProp;
	private String primerNombreProp;
	private String segundoNombreProp;
	private String numeroIdProp;
	private String direccionProp;
	private String telefonoProp;
	private String primerApellidoConductor;
	private String segundoApellidoConductor;
	private String primerNombreConductor;
	private String segundoNombreConductor;
	private String apellidoNombreTransporta;
	private String numeroIdTransporta;
	private String telefonoTransporta;
	private String direccionTransporta;
	private String transportaVictimaDesde;
	private String transportaVictimaHasta;
	private String tipoTransporte;
	private String placaVehiculoTranporta;
	private String descripcionOcurrencia;
	private String intervencionAutoridad;
	private String cobroExcedentePoliza;
	private Integer cantidadOtrosVehiAcc;
	private String placa2Vehiculo;
	private String tipoId2Vehiculo;
	private String nroId2Vehiculo;
	private String placa3Vehiculo;
	private String tipoId3Vehiculo;
	private String nroId3Vehiculo;
	private String tipoReferencia;
	private Date fechaRemision;
	private String horaRemision;
	private String codInscripRemitente;
	private String profesionalRemite;
	private String cargoProfesionalRemitente;
	private Date fechaAceptacion;
	private String horaAceptacion;
	private String codInscripReceptor;
	private String profesionalRecibe;
	private String cargoProfesionalRecibe;
	private String otroTipoTrans;
	private Character zonaTransporte;
	private BigDecimal totalFacAmpQx;
	private BigDecimal totalReclamoAmpQx;
	private BigDecimal totalFacAmpTx;
	private BigDecimal totalReclamoAmpTx;
	private Character esReclamacion;
	private Character furips;
	private Character furtran;
	private String respGlosa;
	private String nroRadicadoAnterior;
	private Long nroConsReclamacion;
	private Set ingresoses = new HashSet(0);

	public RegistroAccidentesTransito() {
	}

	public RegistroAccidentesTransito(long codigo, Usuarios usuarios,
			String zonaAccidente, String informacionAccidente,
			String otroTipoServV, String direccionConductor,
			Date fechaGrabacion, String horaGrabacion, String estado) {
		this.codigo = codigo;
		this.usuarios = usuarios;
		this.zonaAccidente = zonaAccidente;
		this.informacionAccidente = informacionAccidente;
		this.otroTipoServV = otroTipoServV;
		this.direccionConductor = direccionConductor;
		this.fechaGrabacion = fechaGrabacion;
		this.horaGrabacion = horaGrabacion;
		this.estado = estado;
	}

	public RegistroAccidentesTransito(long codigo,
			TiposIdentificacion tiposIdentificacionByTipoIdConductor,
			Ciudades ciudadesByFkRatCiudTrans, Convenios convenios,
			Ciudades ciudadesByFkRatCiudExpIdTrans,
			Ciudades ciudadesByFkRatCiudProp, Ciudades ciudadesByFkRatCiu3,
			TiposIdentificacion tiposIdentificacionByTipoIdProp,
			TiposIdentificacion tiposIdentificacionByTipoIdDeclarante,
			TipoServVehiculos tipoServVehiculos,
			Ciudades ciudadesByFkRatCiudExpIdProp, Usuarios usuarios,
			Ciudades ciudadesByFkRatCiu5, Ciudades ciudadesByFkRatCiu2,
			Instituciones instituciones, Ciudades ciudadesByFkRatCiu1,
			TiposIdentificacion tiposIdentificacionByTipoIdTransporta,
			Ciudades ciudadesByFkRatCiu6, String empresaTrabaja,
			String direccionEmpresa, String telefonoEmpresa,
			Character ocupante, String condicionAccidentado,
			String resultaLesionadoAl, String placaVehiculoOcasiona,
			String lugarAccidente, Date fechaAccidente, String horaAccidente,
			String zonaAccidente, String informacionAccidente,
			String marcaVehiculo, String placa, String otroTipoServV,
			String agencia, String asegurado, String numeroPoliza,
			Date fechaInicialPoliza, Date fechaFinalPoliza,
			String numeroIdConductor, String direccionConductor,
			String telefonoConductor, String apellidoNombreDeclarante,
			String numeroIdDeclarante, Date fechaGrabacion,
			String horaGrabacion, String estado, Date fechaAnulacion,
			String horaAnulacion, String usuarioAnulacion, String poliza,
			String primerApellidoProp, String segundoApellidoProp,
			String primerNombreProp, String segundoNombreProp,
			String numeroIdProp, String direccionProp, String telefonoProp,
			String primerApellidoConductor, String segundoApellidoConductor,
			String primerNombreConductor, String segundoNombreConductor,
			String apellidoNombreTransporta, String numeroIdTransporta,
			String telefonoTransporta, String direccionTransporta,
			String transportaVictimaDesde, String transportaVictimaHasta,
			String tipoTransporte, String placaVehiculoTranporta,
			String descripcionOcurrencia, String intervencionAutoridad,
			String cobroExcedentePoliza, Integer cantidadOtrosVehiAcc,
			String placa2Vehiculo, String tipoId2Vehiculo,
			String nroId2Vehiculo, String placa3Vehiculo,
			String tipoId3Vehiculo, String nroId3Vehiculo,
			String tipoReferencia, Date fechaRemision, String horaRemision,
			String codInscripRemitente, String profesionalRemite,
			String cargoProfesionalRemitente, Date fechaAceptacion,
			String horaAceptacion, String codInscripReceptor,
			String profesionalRecibe, String cargoProfesionalRecibe,
			String otroTipoTrans, Character zonaTransporte,
			BigDecimal totalFacAmpQx, BigDecimal totalReclamoAmpQx,
			BigDecimal totalFacAmpTx, BigDecimal totalReclamoAmpTx,
			Character esReclamacion, Character furips, Character furtran,
			String respGlosa, String nroRadicadoAnterior,
			Long nroConsReclamacion, Set ingresoses) {
		this.codigo = codigo;
		this.tiposIdentificacionByTipoIdConductor = tiposIdentificacionByTipoIdConductor;
		this.ciudadesByFkRatCiudTrans = ciudadesByFkRatCiudTrans;
		this.convenios = convenios;
		this.ciudadesByFkRatCiudExpIdTrans = ciudadesByFkRatCiudExpIdTrans;
		this.ciudadesByFkRatCiudProp = ciudadesByFkRatCiudProp;
		this.ciudadesByFkRatCiu3 = ciudadesByFkRatCiu3;
		this.tiposIdentificacionByTipoIdProp = tiposIdentificacionByTipoIdProp;
		this.tiposIdentificacionByTipoIdDeclarante = tiposIdentificacionByTipoIdDeclarante;
		this.tipoServVehiculos = tipoServVehiculos;
		this.ciudadesByFkRatCiudExpIdProp = ciudadesByFkRatCiudExpIdProp;
		this.usuarios = usuarios;
		this.ciudadesByFkRatCiu5 = ciudadesByFkRatCiu5;
		this.ciudadesByFkRatCiu2 = ciudadesByFkRatCiu2;
		this.instituciones = instituciones;
		this.ciudadesByFkRatCiu1 = ciudadesByFkRatCiu1;
		this.tiposIdentificacionByTipoIdTransporta = tiposIdentificacionByTipoIdTransporta;
		this.ciudadesByFkRatCiu6 = ciudadesByFkRatCiu6;
		this.empresaTrabaja = empresaTrabaja;
		this.direccionEmpresa = direccionEmpresa;
		this.telefonoEmpresa = telefonoEmpresa;
		this.ocupante = ocupante;
		this.condicionAccidentado = condicionAccidentado;
		this.resultaLesionadoAl = resultaLesionadoAl;
		this.placaVehiculoOcasiona = placaVehiculoOcasiona;
		this.lugarAccidente = lugarAccidente;
		this.fechaAccidente = fechaAccidente;
		this.horaAccidente = horaAccidente;
		this.zonaAccidente = zonaAccidente;
		this.informacionAccidente = informacionAccidente;
		this.marcaVehiculo = marcaVehiculo;
		this.placa = placa;
		this.otroTipoServV = otroTipoServV;
		this.agencia = agencia;
		this.asegurado = asegurado;
		this.numeroPoliza = numeroPoliza;
		this.fechaInicialPoliza = fechaInicialPoliza;
		this.fechaFinalPoliza = fechaFinalPoliza;
		this.numeroIdConductor = numeroIdConductor;
		this.direccionConductor = direccionConductor;
		this.telefonoConductor = telefonoConductor;
		this.apellidoNombreDeclarante = apellidoNombreDeclarante;
		this.numeroIdDeclarante = numeroIdDeclarante;
		this.fechaGrabacion = fechaGrabacion;
		this.horaGrabacion = horaGrabacion;
		this.estado = estado;
		this.fechaAnulacion = fechaAnulacion;
		this.horaAnulacion = horaAnulacion;
		this.usuarioAnulacion = usuarioAnulacion;
		this.poliza = poliza;
		this.primerApellidoProp = primerApellidoProp;
		this.segundoApellidoProp = segundoApellidoProp;
		this.primerNombreProp = primerNombreProp;
		this.segundoNombreProp = segundoNombreProp;
		this.numeroIdProp = numeroIdProp;
		this.direccionProp = direccionProp;
		this.telefonoProp = telefonoProp;
		this.primerApellidoConductor = primerApellidoConductor;
		this.segundoApellidoConductor = segundoApellidoConductor;
		this.primerNombreConductor = primerNombreConductor;
		this.segundoNombreConductor = segundoNombreConductor;
		this.apellidoNombreTransporta = apellidoNombreTransporta;
		this.numeroIdTransporta = numeroIdTransporta;
		this.telefonoTransporta = telefonoTransporta;
		this.direccionTransporta = direccionTransporta;
		this.transportaVictimaDesde = transportaVictimaDesde;
		this.transportaVictimaHasta = transportaVictimaHasta;
		this.tipoTransporte = tipoTransporte;
		this.placaVehiculoTranporta = placaVehiculoTranporta;
		this.descripcionOcurrencia = descripcionOcurrencia;
		this.intervencionAutoridad = intervencionAutoridad;
		this.cobroExcedentePoliza = cobroExcedentePoliza;
		this.cantidadOtrosVehiAcc = cantidadOtrosVehiAcc;
		this.placa2Vehiculo = placa2Vehiculo;
		this.tipoId2Vehiculo = tipoId2Vehiculo;
		this.nroId2Vehiculo = nroId2Vehiculo;
		this.placa3Vehiculo = placa3Vehiculo;
		this.tipoId3Vehiculo = tipoId3Vehiculo;
		this.nroId3Vehiculo = nroId3Vehiculo;
		this.tipoReferencia = tipoReferencia;
		this.fechaRemision = fechaRemision;
		this.horaRemision = horaRemision;
		this.codInscripRemitente = codInscripRemitente;
		this.profesionalRemite = profesionalRemite;
		this.cargoProfesionalRemitente = cargoProfesionalRemitente;
		this.fechaAceptacion = fechaAceptacion;
		this.horaAceptacion = horaAceptacion;
		this.codInscripReceptor = codInscripReceptor;
		this.profesionalRecibe = profesionalRecibe;
		this.cargoProfesionalRecibe = cargoProfesionalRecibe;
		this.otroTipoTrans = otroTipoTrans;
		this.zonaTransporte = zonaTransporte;
		this.totalFacAmpQx = totalFacAmpQx;
		this.totalReclamoAmpQx = totalReclamoAmpQx;
		this.totalFacAmpTx = totalFacAmpTx;
		this.totalReclamoAmpTx = totalReclamoAmpTx;
		this.esReclamacion = esReclamacion;
		this.furips = furips;
		this.furtran = furtran;
		this.respGlosa = respGlosa;
		this.nroRadicadoAnterior = nroRadicadoAnterior;
		this.nroConsReclamacion = nroConsReclamacion;
		this.ingresoses = ingresoses;
	}

	public long getCodigo() {
		return this.codigo;
	}

	public void setCodigo(long codigo) {
		this.codigo = codigo;
	}

	public TiposIdentificacion getTiposIdentificacionByTipoIdConductor() {
		return this.tiposIdentificacionByTipoIdConductor;
	}

	public void setTiposIdentificacionByTipoIdConductor(
			TiposIdentificacion tiposIdentificacionByTipoIdConductor) {
		this.tiposIdentificacionByTipoIdConductor = tiposIdentificacionByTipoIdConductor;
	}

	public Ciudades getCiudadesByFkRatCiudTrans() {
		return this.ciudadesByFkRatCiudTrans;
	}

	public void setCiudadesByFkRatCiudTrans(Ciudades ciudadesByFkRatCiudTrans) {
		this.ciudadesByFkRatCiudTrans = ciudadesByFkRatCiudTrans;
	}

	public Convenios getConvenios() {
		return this.convenios;
	}

	public void setConvenios(Convenios convenios) {
		this.convenios = convenios;
	}

	public Ciudades getCiudadesByFkRatCiudExpIdTrans() {
		return this.ciudadesByFkRatCiudExpIdTrans;
	}

	public void setCiudadesByFkRatCiudExpIdTrans(
			Ciudades ciudadesByFkRatCiudExpIdTrans) {
		this.ciudadesByFkRatCiudExpIdTrans = ciudadesByFkRatCiudExpIdTrans;
	}

	public Ciudades getCiudadesByFkRatCiudProp() {
		return this.ciudadesByFkRatCiudProp;
	}

	public void setCiudadesByFkRatCiudProp(Ciudades ciudadesByFkRatCiudProp) {
		this.ciudadesByFkRatCiudProp = ciudadesByFkRatCiudProp;
	}

	public Ciudades getCiudadesByFkRatCiu3() {
		return this.ciudadesByFkRatCiu3;
	}

	public void setCiudadesByFkRatCiu3(Ciudades ciudadesByFkRatCiu3) {
		this.ciudadesByFkRatCiu3 = ciudadesByFkRatCiu3;
	}

	public TiposIdentificacion getTiposIdentificacionByTipoIdProp() {
		return this.tiposIdentificacionByTipoIdProp;
	}

	public void setTiposIdentificacionByTipoIdProp(
			TiposIdentificacion tiposIdentificacionByTipoIdProp) {
		this.tiposIdentificacionByTipoIdProp = tiposIdentificacionByTipoIdProp;
	}

	public TiposIdentificacion getTiposIdentificacionByTipoIdDeclarante() {
		return this.tiposIdentificacionByTipoIdDeclarante;
	}

	public void setTiposIdentificacionByTipoIdDeclarante(
			TiposIdentificacion tiposIdentificacionByTipoIdDeclarante) {
		this.tiposIdentificacionByTipoIdDeclarante = tiposIdentificacionByTipoIdDeclarante;
	}

	public TipoServVehiculos getTipoServVehiculos() {
		return this.tipoServVehiculos;
	}

	public void setTipoServVehiculos(TipoServVehiculos tipoServVehiculos) {
		this.tipoServVehiculos = tipoServVehiculos;
	}

	public Ciudades getCiudadesByFkRatCiudExpIdProp() {
		return this.ciudadesByFkRatCiudExpIdProp;
	}

	public void setCiudadesByFkRatCiudExpIdProp(
			Ciudades ciudadesByFkRatCiudExpIdProp) {
		this.ciudadesByFkRatCiudExpIdProp = ciudadesByFkRatCiudExpIdProp;
	}

	public Usuarios getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

	public Ciudades getCiudadesByFkRatCiu5() {
		return this.ciudadesByFkRatCiu5;
	}

	public void setCiudadesByFkRatCiu5(Ciudades ciudadesByFkRatCiu5) {
		this.ciudadesByFkRatCiu5 = ciudadesByFkRatCiu5;
	}

	public Ciudades getCiudadesByFkRatCiu2() {
		return this.ciudadesByFkRatCiu2;
	}

	public void setCiudadesByFkRatCiu2(Ciudades ciudadesByFkRatCiu2) {
		this.ciudadesByFkRatCiu2 = ciudadesByFkRatCiu2;
	}

	public Instituciones getInstituciones() {
		return this.instituciones;
	}

	public void setInstituciones(Instituciones instituciones) {
		this.instituciones = instituciones;
	}

	public Ciudades getCiudadesByFkRatCiu1() {
		return this.ciudadesByFkRatCiu1;
	}

	public void setCiudadesByFkRatCiu1(Ciudades ciudadesByFkRatCiu1) {
		this.ciudadesByFkRatCiu1 = ciudadesByFkRatCiu1;
	}

	public TiposIdentificacion getTiposIdentificacionByTipoIdTransporta() {
		return this.tiposIdentificacionByTipoIdTransporta;
	}

	public void setTiposIdentificacionByTipoIdTransporta(
			TiposIdentificacion tiposIdentificacionByTipoIdTransporta) {
		this.tiposIdentificacionByTipoIdTransporta = tiposIdentificacionByTipoIdTransporta;
	}

	public Ciudades getCiudadesByFkRatCiu6() {
		return this.ciudadesByFkRatCiu6;
	}

	public void setCiudadesByFkRatCiu6(Ciudades ciudadesByFkRatCiu6) {
		this.ciudadesByFkRatCiu6 = ciudadesByFkRatCiu6;
	}

	public String getEmpresaTrabaja() {
		return this.empresaTrabaja;
	}

	public void setEmpresaTrabaja(String empresaTrabaja) {
		this.empresaTrabaja = empresaTrabaja;
	}

	public String getDireccionEmpresa() {
		return this.direccionEmpresa;
	}

	public void setDireccionEmpresa(String direccionEmpresa) {
		this.direccionEmpresa = direccionEmpresa;
	}

	public String getTelefonoEmpresa() {
		return this.telefonoEmpresa;
	}

	public void setTelefonoEmpresa(String telefonoEmpresa) {
		this.telefonoEmpresa = telefonoEmpresa;
	}

	public Character getOcupante() {
		return this.ocupante;
	}

	public void setOcupante(Character ocupante) {
		this.ocupante = ocupante;
	}

	public String getCondicionAccidentado() {
		return this.condicionAccidentado;
	}

	public void setCondicionAccidentado(String condicionAccidentado) {
		this.condicionAccidentado = condicionAccidentado;
	}

	public String getResultaLesionadoAl() {
		return this.resultaLesionadoAl;
	}

	public void setResultaLesionadoAl(String resultaLesionadoAl) {
		this.resultaLesionadoAl = resultaLesionadoAl;
	}

	public String getPlacaVehiculoOcasiona() {
		return this.placaVehiculoOcasiona;
	}

	public void setPlacaVehiculoOcasiona(String placaVehiculoOcasiona) {
		this.placaVehiculoOcasiona = placaVehiculoOcasiona;
	}

	public String getLugarAccidente() {
		return this.lugarAccidente;
	}

	public void setLugarAccidente(String lugarAccidente) {
		this.lugarAccidente = lugarAccidente;
	}

	public Date getFechaAccidente() {
		return this.fechaAccidente;
	}

	public void setFechaAccidente(Date fechaAccidente) {
		this.fechaAccidente = fechaAccidente;
	}

	public String getHoraAccidente() {
		return this.horaAccidente;
	}

	public void setHoraAccidente(String horaAccidente) {
		this.horaAccidente = horaAccidente;
	}

	public String getZonaAccidente() {
		return this.zonaAccidente;
	}

	public void setZonaAccidente(String zonaAccidente) {
		this.zonaAccidente = zonaAccidente;
	}

	public String getInformacionAccidente() {
		return this.informacionAccidente;
	}

	public void setInformacionAccidente(String informacionAccidente) {
		this.informacionAccidente = informacionAccidente;
	}

	public String getMarcaVehiculo() {
		return this.marcaVehiculo;
	}

	public void setMarcaVehiculo(String marcaVehiculo) {
		this.marcaVehiculo = marcaVehiculo;
	}

	public String getPlaca() {
		return this.placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public String getOtroTipoServV() {
		return this.otroTipoServV;
	}

	public void setOtroTipoServV(String otroTipoServV) {
		this.otroTipoServV = otroTipoServV;
	}

	public String getAgencia() {
		return this.agencia;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}

	public String getAsegurado() {
		return this.asegurado;
	}

	public void setAsegurado(String asegurado) {
		this.asegurado = asegurado;
	}

	public String getNumeroPoliza() {
		return this.numeroPoliza;
	}

	public void setNumeroPoliza(String numeroPoliza) {
		this.numeroPoliza = numeroPoliza;
	}

	public Date getFechaInicialPoliza() {
		return this.fechaInicialPoliza;
	}

	public void setFechaInicialPoliza(Date fechaInicialPoliza) {
		this.fechaInicialPoliza = fechaInicialPoliza;
	}

	public Date getFechaFinalPoliza() {
		return this.fechaFinalPoliza;
	}

	public void setFechaFinalPoliza(Date fechaFinalPoliza) {
		this.fechaFinalPoliza = fechaFinalPoliza;
	}

	public String getNumeroIdConductor() {
		return this.numeroIdConductor;
	}

	public void setNumeroIdConductor(String numeroIdConductor) {
		this.numeroIdConductor = numeroIdConductor;
	}

	public String getDireccionConductor() {
		return this.direccionConductor;
	}

	public void setDireccionConductor(String direccionConductor) {
		this.direccionConductor = direccionConductor;
	}

	public String getTelefonoConductor() {
		return this.telefonoConductor;
	}

	public void setTelefonoConductor(String telefonoConductor) {
		this.telefonoConductor = telefonoConductor;
	}

	public String getApellidoNombreDeclarante() {
		return this.apellidoNombreDeclarante;
	}

	public void setApellidoNombreDeclarante(String apellidoNombreDeclarante) {
		this.apellidoNombreDeclarante = apellidoNombreDeclarante;
	}

	public String getNumeroIdDeclarante() {
		return this.numeroIdDeclarante;
	}

	public void setNumeroIdDeclarante(String numeroIdDeclarante) {
		this.numeroIdDeclarante = numeroIdDeclarante;
	}

	public Date getFechaGrabacion() {
		return this.fechaGrabacion;
	}

	public void setFechaGrabacion(Date fechaGrabacion) {
		this.fechaGrabacion = fechaGrabacion;
	}

	public String getHoraGrabacion() {
		return this.horaGrabacion;
	}

	public void setHoraGrabacion(String horaGrabacion) {
		this.horaGrabacion = horaGrabacion;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getFechaAnulacion() {
		return this.fechaAnulacion;
	}

	public void setFechaAnulacion(Date fechaAnulacion) {
		this.fechaAnulacion = fechaAnulacion;
	}

	public String getHoraAnulacion() {
		return this.horaAnulacion;
	}

	public void setHoraAnulacion(String horaAnulacion) {
		this.horaAnulacion = horaAnulacion;
	}

	public String getUsuarioAnulacion() {
		return this.usuarioAnulacion;
	}

	public void setUsuarioAnulacion(String usuarioAnulacion) {
		this.usuarioAnulacion = usuarioAnulacion;
	}

	public String getPoliza() {
		return this.poliza;
	}

	public void setPoliza(String poliza) {
		this.poliza = poliza;
	}

	public String getPrimerApellidoProp() {
		return this.primerApellidoProp;
	}

	public void setPrimerApellidoProp(String primerApellidoProp) {
		this.primerApellidoProp = primerApellidoProp;
	}

	public String getSegundoApellidoProp() {
		return this.segundoApellidoProp;
	}

	public void setSegundoApellidoProp(String segundoApellidoProp) {
		this.segundoApellidoProp = segundoApellidoProp;
	}

	public String getPrimerNombreProp() {
		return this.primerNombreProp;
	}

	public void setPrimerNombreProp(String primerNombreProp) {
		this.primerNombreProp = primerNombreProp;
	}

	public String getSegundoNombreProp() {
		return this.segundoNombreProp;
	}

	public void setSegundoNombreProp(String segundoNombreProp) {
		this.segundoNombreProp = segundoNombreProp;
	}

	public String getNumeroIdProp() {
		return this.numeroIdProp;
	}

	public void setNumeroIdProp(String numeroIdProp) {
		this.numeroIdProp = numeroIdProp;
	}

	public String getDireccionProp() {
		return this.direccionProp;
	}

	public void setDireccionProp(String direccionProp) {
		this.direccionProp = direccionProp;
	}

	public String getTelefonoProp() {
		return this.telefonoProp;
	}

	public void setTelefonoProp(String telefonoProp) {
		this.telefonoProp = telefonoProp;
	}

	public String getPrimerApellidoConductor() {
		return this.primerApellidoConductor;
	}

	public void setPrimerApellidoConductor(String primerApellidoConductor) {
		this.primerApellidoConductor = primerApellidoConductor;
	}

	public String getSegundoApellidoConductor() {
		return this.segundoApellidoConductor;
	}

	public void setSegundoApellidoConductor(String segundoApellidoConductor) {
		this.segundoApellidoConductor = segundoApellidoConductor;
	}

	public String getPrimerNombreConductor() {
		return this.primerNombreConductor;
	}

	public void setPrimerNombreConductor(String primerNombreConductor) {
		this.primerNombreConductor = primerNombreConductor;
	}

	public String getSegundoNombreConductor() {
		return this.segundoNombreConductor;
	}

	public void setSegundoNombreConductor(String segundoNombreConductor) {
		this.segundoNombreConductor = segundoNombreConductor;
	}

	public String getApellidoNombreTransporta() {
		return this.apellidoNombreTransporta;
	}

	public void setApellidoNombreTransporta(String apellidoNombreTransporta) {
		this.apellidoNombreTransporta = apellidoNombreTransporta;
	}

	public String getNumeroIdTransporta() {
		return this.numeroIdTransporta;
	}

	public void setNumeroIdTransporta(String numeroIdTransporta) {
		this.numeroIdTransporta = numeroIdTransporta;
	}

	public String getTelefonoTransporta() {
		return this.telefonoTransporta;
	}

	public void setTelefonoTransporta(String telefonoTransporta) {
		this.telefonoTransporta = telefonoTransporta;
	}

	public String getDireccionTransporta() {
		return this.direccionTransporta;
	}

	public void setDireccionTransporta(String direccionTransporta) {
		this.direccionTransporta = direccionTransporta;
	}

	public String getTransportaVictimaDesde() {
		return this.transportaVictimaDesde;
	}

	public void setTransportaVictimaDesde(String transportaVictimaDesde) {
		this.transportaVictimaDesde = transportaVictimaDesde;
	}

	public String getTransportaVictimaHasta() {
		return this.transportaVictimaHasta;
	}

	public void setTransportaVictimaHasta(String transportaVictimaHasta) {
		this.transportaVictimaHasta = transportaVictimaHasta;
	}

	public String getTipoTransporte() {
		return this.tipoTransporte;
	}

	public void setTipoTransporte(String tipoTransporte) {
		this.tipoTransporte = tipoTransporte;
	}

	public String getPlacaVehiculoTranporta() {
		return this.placaVehiculoTranporta;
	}

	public void setPlacaVehiculoTranporta(String placaVehiculoTranporta) {
		this.placaVehiculoTranporta = placaVehiculoTranporta;
	}

	public String getDescripcionOcurrencia() {
		return this.descripcionOcurrencia;
	}

	public void setDescripcionOcurrencia(String descripcionOcurrencia) {
		this.descripcionOcurrencia = descripcionOcurrencia;
	}

	public String getIntervencionAutoridad() {
		return this.intervencionAutoridad;
	}

	public void setIntervencionAutoridad(String intervencionAutoridad) {
		this.intervencionAutoridad = intervencionAutoridad;
	}

	public String getCobroExcedentePoliza() {
		return this.cobroExcedentePoliza;
	}

	public void setCobroExcedentePoliza(String cobroExcedentePoliza) {
		this.cobroExcedentePoliza = cobroExcedentePoliza;
	}

	public Integer getCantidadOtrosVehiAcc() {
		return this.cantidadOtrosVehiAcc;
	}

	public void setCantidadOtrosVehiAcc(Integer cantidadOtrosVehiAcc) {
		this.cantidadOtrosVehiAcc = cantidadOtrosVehiAcc;
	}

	public String getPlaca2Vehiculo() {
		return this.placa2Vehiculo;
	}

	public void setPlaca2Vehiculo(String placa2Vehiculo) {
		this.placa2Vehiculo = placa2Vehiculo;
	}

	public String getTipoId2Vehiculo() {
		return this.tipoId2Vehiculo;
	}

	public void setTipoId2Vehiculo(String tipoId2Vehiculo) {
		this.tipoId2Vehiculo = tipoId2Vehiculo;
	}

	public String getNroId2Vehiculo() {
		return this.nroId2Vehiculo;
	}

	public void setNroId2Vehiculo(String nroId2Vehiculo) {
		this.nroId2Vehiculo = nroId2Vehiculo;
	}

	public String getPlaca3Vehiculo() {
		return this.placa3Vehiculo;
	}

	public void setPlaca3Vehiculo(String placa3Vehiculo) {
		this.placa3Vehiculo = placa3Vehiculo;
	}

	public String getTipoId3Vehiculo() {
		return this.tipoId3Vehiculo;
	}

	public void setTipoId3Vehiculo(String tipoId3Vehiculo) {
		this.tipoId3Vehiculo = tipoId3Vehiculo;
	}

	public String getNroId3Vehiculo() {
		return this.nroId3Vehiculo;
	}

	public void setNroId3Vehiculo(String nroId3Vehiculo) {
		this.nroId3Vehiculo = nroId3Vehiculo;
	}

	public String getTipoReferencia() {
		return this.tipoReferencia;
	}

	public void setTipoReferencia(String tipoReferencia) {
		this.tipoReferencia = tipoReferencia;
	}

	public Date getFechaRemision() {
		return this.fechaRemision;
	}

	public void setFechaRemision(Date fechaRemision) {
		this.fechaRemision = fechaRemision;
	}

	public String getHoraRemision() {
		return this.horaRemision;
	}

	public void setHoraRemision(String horaRemision) {
		this.horaRemision = horaRemision;
	}

	public String getCodInscripRemitente() {
		return this.codInscripRemitente;
	}

	public void setCodInscripRemitente(String codInscripRemitente) {
		this.codInscripRemitente = codInscripRemitente;
	}

	public String getProfesionalRemite() {
		return this.profesionalRemite;
	}

	public void setProfesionalRemite(String profesionalRemite) {
		this.profesionalRemite = profesionalRemite;
	}

	public String getCargoProfesionalRemitente() {
		return this.cargoProfesionalRemitente;
	}

	public void setCargoProfesionalRemitente(String cargoProfesionalRemitente) {
		this.cargoProfesionalRemitente = cargoProfesionalRemitente;
	}

	public Date getFechaAceptacion() {
		return this.fechaAceptacion;
	}

	public void setFechaAceptacion(Date fechaAceptacion) {
		this.fechaAceptacion = fechaAceptacion;
	}

	public String getHoraAceptacion() {
		return this.horaAceptacion;
	}

	public void setHoraAceptacion(String horaAceptacion) {
		this.horaAceptacion = horaAceptacion;
	}

	public String getCodInscripReceptor() {
		return this.codInscripReceptor;
	}

	public void setCodInscripReceptor(String codInscripReceptor) {
		this.codInscripReceptor = codInscripReceptor;
	}

	public String getProfesionalRecibe() {
		return this.profesionalRecibe;
	}

	public void setProfesionalRecibe(String profesionalRecibe) {
		this.profesionalRecibe = profesionalRecibe;
	}

	public String getCargoProfesionalRecibe() {
		return this.cargoProfesionalRecibe;
	}

	public void setCargoProfesionalRecibe(String cargoProfesionalRecibe) {
		this.cargoProfesionalRecibe = cargoProfesionalRecibe;
	}

	public String getOtroTipoTrans() {
		return this.otroTipoTrans;
	}

	public void setOtroTipoTrans(String otroTipoTrans) {
		this.otroTipoTrans = otroTipoTrans;
	}

	public Character getZonaTransporte() {
		return this.zonaTransporte;
	}

	public void setZonaTransporte(Character zonaTransporte) {
		this.zonaTransporte = zonaTransporte;
	}

	public BigDecimal getTotalFacAmpQx() {
		return this.totalFacAmpQx;
	}

	public void setTotalFacAmpQx(BigDecimal totalFacAmpQx) {
		this.totalFacAmpQx = totalFacAmpQx;
	}

	public BigDecimal getTotalReclamoAmpQx() {
		return this.totalReclamoAmpQx;
	}

	public void setTotalReclamoAmpQx(BigDecimal totalReclamoAmpQx) {
		this.totalReclamoAmpQx = totalReclamoAmpQx;
	}

	public BigDecimal getTotalFacAmpTx() {
		return this.totalFacAmpTx;
	}

	public void setTotalFacAmpTx(BigDecimal totalFacAmpTx) {
		this.totalFacAmpTx = totalFacAmpTx;
	}

	public BigDecimal getTotalReclamoAmpTx() {
		return this.totalReclamoAmpTx;
	}

	public void setTotalReclamoAmpTx(BigDecimal totalReclamoAmpTx) {
		this.totalReclamoAmpTx = totalReclamoAmpTx;
	}

	public Character getEsReclamacion() {
		return this.esReclamacion;
	}

	public void setEsReclamacion(Character esReclamacion) {
		this.esReclamacion = esReclamacion;
	}

	public Character getFurips() {
		return this.furips;
	}

	public void setFurips(Character furips) {
		this.furips = furips;
	}

	public Character getFurtran() {
		return this.furtran;
	}

	public void setFurtran(Character furtran) {
		this.furtran = furtran;
	}

	public String getRespGlosa() {
		return this.respGlosa;
	}

	public void setRespGlosa(String respGlosa) {
		this.respGlosa = respGlosa;
	}

	public String getNroRadicadoAnterior() {
		return this.nroRadicadoAnterior;
	}

	public void setNroRadicadoAnterior(String nroRadicadoAnterior) {
		this.nroRadicadoAnterior = nroRadicadoAnterior;
	}

	public Long getNroConsReclamacion() {
		return this.nroConsReclamacion;
	}

	public void setNroConsReclamacion(Long nroConsReclamacion) {
		this.nroConsReclamacion = nroConsReclamacion;
	}

	public Set getIngresoses() {
		return this.ingresoses;
	}

	public void setIngresoses(Set ingresoses) {
		this.ingresoses = ingresoses;
	}

}
