package com.princetonsa.dto.facturacion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;

import util.ConstantesBD;
import util.InfoDatos;
import util.InfoDatosInt;
import util.UtilidadTexto;

import com.servinte.axioma.dto.facturacion.DtoInfoCobroPaciente;
import com.servinte.axioma.orm.HistoricoEncabezado;

/**
 * 
 * @author wilson
 *
 */
public class DtoFactura implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 9052915576113600427L;

	/**
	 * 
	 */
	private int codigo;
	
	/**
	 * 
	 */
	private String fecha;
	
	/**
	 * 
	 */
	private String hora;
	
	/**
	 * 
	 */
	private InfoDatosInt estadoFacturacion;
	
	/**
	 * 
	 */
	private InfoDatosInt estadoPaciente;
	
	/**
	 * 
	 */
	private String  loginUsuario;
	

	
	/**
	 * 
	 */
	private double consecutivoFactura;
	
	/**
	 * 
	 */
	private String consecutivoFacturaStr;
	
	/**
	 * 
	 */
	private String prefijoFactura;
	
	/**
	 * 
	 */
	private int institucion;
	
	/**
	 * 
	 */	
	private double valorPagos;
	
	/**
	 * 
	 */
	private double ajustesCredito;
	
	/**
	 * 
	 */
	private double ajustesDebito;
	
	/**
	 * 
	 */
	private double valorAbonos;
	
	/**
	 * 
	 */
	private double valorAnticipos;
		
	/**
	 * 
	 */
	private double valorTotal;
	
	/**
	 * 
	 */
	private double valorNetoPaciente;
	
	/**
	 * 
	 */
	private double valorConvenio;
	
	/**
	 * 
	 */
	private double valorCartera;
	
	/**
	 * 
	 */
	private double valorLiquidadoPaciente;
	
	/**
	 * 
	 */
	private double valorBrutoPac;
	
	/**
	 * 
	 */
	private double valorBrutoPacSinModParamConvenio;
	
	
	/**
	 * 
	 */
	private double valorDescuentoPaciente;
	
	/**
	 * 
	 */
	private double numeroCuentaCobro;
	
	/**
	 * 
	 */
	private boolean tipoFacturaSistema;
	
	/**
	 * 
	 */
	private int codigoPaciente;
	
	/**
	 * 
	 */
	private int codigoResonsableParticular;
	
	/**
	 * 
	 */
	private Vector cuentas;
	
	/**
	 * 
	 */
	private InfoDatosInt convenio;
	
	/**
	 * 
	 */
	private InfoDatosInt contrato;
	
	/**
	 * 
	 */
	private InfoDatosInt viaIngreso;
	
	/**
	 * 
	 */
	private Integer montoCobro;
	
	/**
	 * 
	 */
	private double subCuenta;
	
	/**
	 * 
	 */
	private boolean facturaCerrada;
	
	/**
	 * 
	 */
	//private double valorMonto;
	
	/**
	 * 
	 */
	//private double porcentajeMonto;
	
	/**
	 * 
	 */
	private String tipoAfiliado;
	
	/**
	 * 
	 */
	private InfoDatosInt estratoSocial;
	
	/**
	 * 
	 */
	private InfoDatosInt tipoMonto;
	
	/**
	 * 
	 */
	private double cuentaCobroCapitacion;
	
	/**
	 * 
	 */
	private InfoDatosInt centroAtencion;
	
	/**
	 * 
	 */
	private String tipoComprobante;
	
	/**
	 * 
	 */
	private double nroComprobante;

	/**
	 * detalles de la factura
	 */
	private ArrayList<DtoDetalleFactura> detallesFactura= new ArrayList<DtoDetalleFactura>();
	
	/**
	 * detalles de la factura
	 */
	private ArrayList<DtoDetalleFactura> detallesFacturaExcentas= new ArrayList<DtoDetalleFactura>();
	
	/**
	 * 
	 */
	private String diagnosticoEgresoAcronimoTipoCie;
	
	/**
	 * 
	 */
	private InfoDatos tipoRegimen;
	
	/**
	 * 
	 */
	private boolean pacienteTieneExcepcionNaturaleza;
	
	/**
	 * 
	 */
	private boolean esParticular;
	
	/**
	 * 
	 */
	private boolean valorBrutoPacienteModificadoXEvento;
	
	/**
	 * 
	 */
	private boolean valorBrutoPacienteModificadoXAnioCalendario;
	
	/**
	 * 
	 */
	private double valorAFavorConvenio;
	
	/**
	 * 
	 */
	private boolean valorAFavorConvenioModificado;
	
	
	/**
	 * 
	 */
	private InfoDatosInt formatoImpresion;
	
	/**
	 * 
	 */
	private String idPaciente;
	
	/**
	 * 
	 */
	private String nombrePaciente;
	
	/**
	 * 
	 */
	private String resolucionDian;
	
	/**
	 * 
	 */
	private double rangoInicialFactura;
	
	/**
	 * 
	 */
	private double rangoFinalFactura;
	
	/**
	 * 
	 */
	private boolean validarInfoVenezuela;
	
	/**
	 * 
	 */
	private double valorMontoAutorizadoVenezuela;
	
	/**
	 * 
	 */
	private double valorFaltaAsignarVenezuela;
	
	/**
	 * 
	 */
	private double entidadSubcontratada;
	
	/**
	 * 
	 */
	private double empresaInstitucion;
	
	
	/**
	 * 
	 */
	private double pacienteEntidadSubcontratada;
	
	/**
	 * 
	 */
	private int topeFacturacion;
	
	/**
	 * 
	 */
	private String fechaVigenciaInicialMontoCobro;
	
	/**
	 * 
	 */
	private String fechaVigenciaInicialTopeFacturacion;
	
	/**
	 * 
	 */
	private InfoDatosInt centroAtencionDuenio;
	
	
	/**
	 * Paciente paga la atencion X monto cobro
	 */
	private boolean pacientePagaAtencionXMontoCobro;
	
	/**
	 * si el contrato controla anticipos
	 */
	private boolean controlaAnticipos;
	
	/**
	 * 
	 */
	private DtoInfoCobroPaciente inforCalculoValorPaciente;
	
	/**
	 * Atributo que contiene la información de encabezados y 
	 * demás que dependen de la parametrización del sistema
	 */
	private HistoricoEncabezado historicoEncabezado;
	
	/**
	 * Constructor  
	 */
	public DtoFactura() 
	{
		super();
		this.codigo = ConstantesBD.codigoNuncaValido;
		this.fecha = "";
		this.hora = "";
		this.estadoFacturacion = new InfoDatosInt();
		this.estadoPaciente = new InfoDatosInt();
		this.loginUsuario = "";
		this.consecutivoFactura = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.consecutivoFacturaStr="0";
		this.prefijoFactura = "";
		this.institucion = ConstantesBD.codigoNuncaValido;
		this.valorPagos = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.ajustesCredito = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.ajustesDebito = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.valorAbonos = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.valorAnticipos= ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.valorTotal = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.valorNetoPaciente = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.valorConvenio = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.valorCartera = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.valorLiquidadoPaciente = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.valorBrutoPac = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.valorBrutoPacSinModParamConvenio = this.valorBrutoPac; 
		this.valorDescuentoPaciente = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.numeroCuentaCobro = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.tipoFacturaSistema = true;
		this.codigoPaciente = ConstantesBD.codigoNuncaValido;
		this.codigoResonsableParticular = ConstantesBD.codigoNuncaValido;
		this.cuentas = new Vector();
		this.convenio = new InfoDatosInt();
		this.contrato = new InfoDatosInt();
		this.viaIngreso = new InfoDatosInt();
		this.montoCobro = ConstantesBD.codigoNuncaValido;
		this.subCuenta = ConstantesBD.codigoNuncaValidoDouble;
		this.facturaCerrada = false;
		//this.valorMonto = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		//this.porcentajeMonto = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.tipoAfiliado = "";
		this.estratoSocial = new InfoDatosInt();
		this.tipoMonto = new InfoDatosInt();
		this.cuentaCobroCapitacion = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.centroAtencion = new InfoDatosInt();
		this.tipoComprobante = "";
		this.nroComprobante = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.detallesFactura = new ArrayList<DtoDetalleFactura>();
		this.detallesFacturaExcentas = new ArrayList<DtoDetalleFactura>();
		this.diagnosticoEgresoAcronimoTipoCie="";
		this.tipoRegimen=new InfoDatos();
		this.pacienteTieneExcepcionNaturaleza=false;
		this.esParticular=false;
		this.valorBrutoPacienteModificadoXAnioCalendario=false;
		this.valorBrutoPacienteModificadoXEvento=false;
		this.valorAFavorConvenio=ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.valorAFavorConvenioModificado= false;
		this.formatoImpresion=new InfoDatosInt();
		this.idPaciente="";
		this.nombrePaciente="";
		this.resolucionDian="";
		this.rangoInicialFactura=ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.rangoFinalFactura=ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.validarInfoVenezuela=false;
		this.valorMontoAutorizadoVenezuela=ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.valorFaltaAsignarVenezuela= ConstantesBD.codigoNuncaValidoDoubleNegativo;
		
		this.entidadSubcontratada=ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.empresaInstitucion=ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.pacienteEntidadSubcontratada=ConstantesBD.codigoNuncaValidoDoubleNegativo;
	
		this.topeFacturacion=ConstantesBD.codigoNuncaValido;
		this.fechaVigenciaInicialMontoCobro="";
		this.fechaVigenciaInicialTopeFacturacion="";
		this.centroAtencionDuenio= new InfoDatosInt();
		
		this.pacientePagaAtencionXMontoCobro=false;
		this.controlaAnticipos=false;
		this.inforCalculoValorPaciente=new DtoInfoCobroPaciente();
		
		this.setHistoricoEncabezado(new HistoricoEncabezado());
	}

	public InfoDatosInt getContrato() {
		return contrato;
	}

	public void setContrato(InfoDatosInt contrato) {
		this.contrato = contrato;
	}

	public String getConsecutivoFacturaStr() {
		return consecutivoFacturaStr;
	}

	public void setConsecutivoFacturaStr(String consecutivoFacturaStr) {
		this.consecutivoFacturaStr = consecutivoFacturaStr;
	}

	/**
	 *
	 *Constructor
	 */
	public DtoFactura(int codigo, String fecha, String hora, InfoDatosInt estadoFacturacion, InfoDatosInt estadoPaciente, String loginUsuario, double consecutivoFactura, String prefijoFactura, int institucion, double valorPagos, double ajustesCredito, double ajustesDebito, double valorAbonos, double valorTotal, double valorNetoPaciente, double valorConvenio, double valorCartera, double valorBrutoPac, double valorDescuentoPaciente, double numeroCuentaCobro, boolean tipoFacturaSistema, int codigoPaciente, int codigoResonsableParticular, Vector cuentas, InfoDatosInt convenio, InfoDatosInt viaIngreso, Integer montoCobro, double subCuenta, boolean facturaCerrada, String tipoAfiliado, InfoDatosInt estrato_social, InfoDatosInt tipoMonto, double cuentaCobroCapitacion, InfoDatosInt centroAtencion, String tipoComprobante, double nroComprobante, ArrayList<DtoDetalleFactura> detallesFactura, ArrayList<DtoDetalleFactura> detallesFacturaExcentas, String diagnosticoEgresoAcronimoTipoCie, InfoDatos tipoRegimen, boolean pacienteTieneExcepcionNaturaleza, boolean esParticular, boolean valorBrutoPacienteModificadoXEvento, boolean valorBrutoPacienteModificadoXAnioCalendario, double valorAFavorConvenio, InfoDatosInt formatoImpresion, String resolucionDian, double rangoInicialFactura, double rangoFinalFactura, boolean validarInfoVenezuela, double valorMontoAutorizadoVenezuela, double valorFaltaAsignarVenezuela, double entidadSubcontratada, double empresaInstitucion, double pacienteEntidadSubcontratada, double valorLiquidadoPaciente, boolean valorAFavorConvenioModificado, int topeFacturacion, String fechaVigenciaInicialMontoCobro, String fechaVigenciaInicialTopeFacturacion, InfoDatosInt centroAtencionDuenio, double valorAnticipos,DtoInfoCobroPaciente inforCalculoValorPaciente) 
	{
		super();
		this.codigo = codigo;
		this.fecha = fecha;
		this.hora = hora;
		this.estadoFacturacion = estadoFacturacion;
		this.estadoPaciente = estadoPaciente;
		this.loginUsuario = loginUsuario;
		this.consecutivoFactura = consecutivoFactura;
		this.prefijoFactura = prefijoFactura;
		this.institucion = institucion;
		this.valorPagos = valorPagos;
		this.ajustesCredito = ajustesCredito;
		this.ajustesDebito = ajustesDebito;
		this.valorAbonos = valorAbonos;
		this.valorTotal = valorTotal;
		this.valorNetoPaciente = valorNetoPaciente;
		this.valorConvenio = valorConvenio;
		this.valorCartera = valorCartera;
		this.valorLiquidadoPaciente = valorLiquidadoPaciente;
		this.valorBrutoPac = valorBrutoPac;
		this.valorBrutoPacSinModParamConvenio= this.valorBrutoPac;
		this.valorDescuentoPaciente = valorDescuentoPaciente;
		this.numeroCuentaCobro = numeroCuentaCobro;
		this.tipoFacturaSistema = tipoFacturaSistema;
		this.codigoPaciente = codigoPaciente;
		this.codigoResonsableParticular = codigoResonsableParticular;
		this.cuentas = cuentas;
		this.convenio = convenio;
		this.viaIngreso = viaIngreso;
		this.montoCobro = montoCobro;
		this.subCuenta = subCuenta;
		this.facturaCerrada = facturaCerrada;
		//this.valorMonto = valorMonto;
		//this.porcentajeMonto = porcentajeMonto;
		this.tipoAfiliado = tipoAfiliado;
		this.estratoSocial = estrato_social;
		this.tipoMonto = tipoMonto;
		this.cuentaCobroCapitacion = cuentaCobroCapitacion;
		this.centroAtencion = centroAtencion;
		this.tipoComprobante = tipoComprobante;
		this.nroComprobante = nroComprobante;
		this.detallesFactura = detallesFactura;
		this.detallesFacturaExcentas= detallesFacturaExcentas;
		this.diagnosticoEgresoAcronimoTipoCie=diagnosticoEgresoAcronimoTipoCie;
		this.tipoRegimen=tipoRegimen;
		this.pacienteTieneExcepcionNaturaleza=pacienteTieneExcepcionNaturaleza;
		this.esParticular=esParticular;
		this.valorBrutoPacienteModificadoXAnioCalendario=valorBrutoPacienteModificadoXAnioCalendario;
		this.valorBrutoPacienteModificadoXEvento=valorBrutoPacienteModificadoXEvento;
		this.valorAFavorConvenio=valorAFavorConvenio;
		this.valorAFavorConvenioModificado=valorAFavorConvenioModificado;
		this.formatoImpresion=formatoImpresion;
		this.idPaciente="";  ///no se quien metio esto pero debia cargarlo
		this.nombrePaciente=""; ///no se quien metio esto pero debia cargarlo
		this.resolucionDian=resolucionDian;
		this.rangoInicialFactura=rangoInicialFactura;
		this.rangoFinalFactura=rangoFinalFactura;
		this.validarInfoVenezuela= validarInfoVenezuela;
		this.valorMontoAutorizadoVenezuela= valorMontoAutorizadoVenezuela;
		this.valorFaltaAsignarVenezuela= valorFaltaAsignarVenezuela;
		
		this.entidadSubcontratada=entidadSubcontratada;
		this.empresaInstitucion=empresaInstitucion;
		this.pacienteEntidadSubcontratada= pacienteEntidadSubcontratada;
		
		this.topeFacturacion=topeFacturacion;
		this.fechaVigenciaInicialMontoCobro=fechaVigenciaInicialMontoCobro;
		this.fechaVigenciaInicialTopeFacturacion=fechaVigenciaInicialTopeFacturacion;
		
		this.centroAtencionDuenio= centroAtencionDuenio;
		
		this.valorAnticipos= valorAnticipos;
		this.inforCalculoValorPaciente=inforCalculoValorPaciente;
	}
	
	
	/**
	 * @return the ajustesCredito
	 */
	public double getAjustesCredito() {
		return ajustesCredito;
	}

	/**
	 * @param ajustesCredito the ajustesCredito to set
	 */
	public void setAjustesCredito(double ajustesCredito) {
		this.ajustesCredito = ajustesCredito;
	}

	/**
	 * @return the ajustesDebito
	 */
	public double getAjustesDebito() {
		return ajustesDebito;
	}

	/**
	 * @param ajustesDebito the ajustesDebito to set
	 */
	public void setAjustesDebito(double ajustesDebito) {
		this.ajustesDebito = ajustesDebito;
	}

	/**
	 * @return the centroAtencion
	 */
	public InfoDatosInt getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(InfoDatosInt centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	/**
	 * @return the codigo
	 */
	public int getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the codigoPaciente
	 */
	public int getCodigoPaciente() {
		return codigoPaciente;
	}

	/**
	 * @param codigoPaciente the codigoPaciente to set
	 */
	public void setCodigoPaciente(int codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}

	/**
	 * @return the codigoResonsableParticular
	 */
	public int getCodigoResonsableParticular() {
		return codigoResonsableParticular;
	}

	/**
	 * @param codigoResonsableParticular the codigoResonsableParticular to set
	 */
	public void setCodigoResonsableParticular(int codigoResonsableParticular) {
		this.codigoResonsableParticular = codigoResonsableParticular;
	}

	/**
	 * @return the consecutivoFactura
	 */
	public double getConsecutivoFactura() {
		return consecutivoFactura;
	}
	

	/**
	 * @param consecutivoFactura the consecutivoFactura to set
	 */
	public void setConsecutivoFactura(double consecutivoFactura) {
		this.consecutivoFactura = consecutivoFactura;
	}

	
	

	/**
	 * @return the convenio
	 */
	public InfoDatosInt getConvenio() {
		return convenio;
	}

	/**
	 * @param convenio the convenio to set
	 */
	public void setConvenio(InfoDatosInt convenio) {
		this.convenio = convenio;
	}

	/**
	 * @return the cuentas
	 */
	public Vector getCuentas() {
		return cuentas;
	}

	/**
	 * @param cuentas the cuentas to set
	 */
	public void setCuentas(Vector cuentas) {
		this.cuentas = cuentas;
	}

	/**
	 * @return the cuentaCobroCapitacion
	 */
	public double getCuentaCobroCapitacion() {
		return cuentaCobroCapitacion;
	}

	/**
	 * @return the valorAFavorConvenioModificado
	 */
	public boolean isValorAFavorConvenioModificado() {
		return valorAFavorConvenioModificado;
	}

	/**
	 * @return the valorAFavorConvenioModificado
	 */
	public boolean getValorAFavorConvenioModificado() {
		return valorAFavorConvenioModificado;
	}
	
	/**
	 * @param valorAFavorConvenioModificado the valorAFavorConvenioModificado to set
	 */
	public void setValorAFavorConvenioModificado(
			boolean valorAFavorConvenioModificado) {
		this.valorAFavorConvenioModificado = valorAFavorConvenioModificado;
	}

	/**
	 * @param cuentaCobroCapitacion the cuentaCobroCapitacion to set
	 */
	public void setCuentaCobroCapitacion(double cuentaCobroCapitacion) {
		this.cuentaCobroCapitacion = cuentaCobroCapitacion;
	}

	/**
	 * @return the estadoFacturacion
	 */
	public InfoDatosInt getEstadoFacturacion() {
		return estadoFacturacion;
	}

	/**
	 * @param estadoFacturacion the estadoFacturacion to set
	 */
	public void setEstadoFacturacion(InfoDatosInt estadoFacturacion) {
		this.estadoFacturacion = estadoFacturacion;
	}

	/**
	 * @return the estadoPaciente
	 */
	public InfoDatosInt getEstadoPaciente() {
		return estadoPaciente;
	}

	/**
	 * @param estadoPaciente the estadoPaciente to set
	 */
	public void setEstadoPaciente(InfoDatosInt estadoPaciente) {
		this.estadoPaciente = estadoPaciente;
	}

	/**
	 * @return the facturaCerrada
	 */
	public boolean getFacturaCerrada() {
		return facturaCerrada;
	}

	/**
	 * @param facturaCerrada the facturaCerrada to set
	 */
	public void setFacturaCerrada(boolean facturaCerrada) {
		this.facturaCerrada = facturaCerrada;
	}

	/**
	 * @return the fecha
	 */
	public String getFecha() {
		return fecha;
	}

	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	/**
	 * @return the hora
	 */
	public String getHora() {
		return hora;
	}

	/**
	 * @param hora the hora to set
	 */
	public void setHora(String hora) {
		this.hora = hora;
	}

	/**
	 * @return the institucion
	 */
	public int getInstitucion() {
		return institucion;
	}

	/**
	 * @param institucion the institucion to set
	 */
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	/**
	 * @return the loginUsuario
	 */
	public String getLoginUsuario() {
		return loginUsuario;
	}

	/**
	 * @param loginUsuario the loginUsuario to set
	 */
	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}

	/**
	 * @return the montoCobro
	 */
	public int getMontoCobro() {
		return montoCobro;
	}

	/**
	 * @param montoCobro the montoCobro to set
	 */
	public void setMontoCobro(int montoCobro) {
		this.montoCobro = montoCobro;
	}

	/**
	 * @return the nroComprobante
	 */
	public double getNroComprobante() {
		return nroComprobante;
	}

	/**
	 * @param nroComprobante the nroComprobante to set
	 */
	public void setNroComprobante(double nroComprobante) {
		this.nroComprobante = nroComprobante;
	}

	/**
	 * @return the numeroCuentaCobro
	 */
	public double getNumeroCuentaCobro() {
		return numeroCuentaCobro;
	}

	/**
	 * @param numeroCuentaCobro the numeroCuentaCobro to set
	 */
	public void setNumeroCuentaCobro(double numeroCuentaCobro) {
		this.numeroCuentaCobro = numeroCuentaCobro;
	}

	/**
	 * @return the porcentajeMonto
	 */
	//public double getPorcentajeMonto() {
	//	return porcentajeMonto;
	//}

	/**
	 * @param porcentajeMonto the porcentajeMonto to set
	 */
	//public void setPorcentajeMonto(double porcentajeMonto) {
		//		this.porcentajeMonto = porcentajeMonto;
		//}

	/**
	 * @return the prefijoFactura
	 */
	@Deprecated
	public String getPrefijoFactura() {
		return prefijoFactura;
	}

	/**
	 * @param prefijoFactura the prefijoFactura to set
	 */
	@Deprecated
	public void setPrefijoFactura(String prefijoFactura) {
		this.prefijoFactura = prefijoFactura;
	}

	/**
	 * @return the subCuenta
	 */
	public double getSubCuenta() {
		return subCuenta;
	}

	/**
	 * @param subCuenta the subCuenta to set
	 */
	public void setSubCuenta(double subCuenta) {
		this.subCuenta = subCuenta;
	}

	/**
	 * @return the tipoAfiliado
	 */
	public String getTipoAfiliado() {
		return tipoAfiliado;
	}

	/**
	 * @param tipoAfiliado the tipoAfiliado to set
	 */
	public void setTipoAfiliado(String tipoAfiliado) {
		this.tipoAfiliado = tipoAfiliado;
	}

	/**
	 * @return the tipoComprobante
	 */
	public String getTipoComprobante() {
		return tipoComprobante;
	}

	/**
	 * @param tipoComprobante the tipoComprobante to set
	 */
	public void setTipoComprobante(String tipoComprobante) {
		this.tipoComprobante = tipoComprobante;
	}

	/**
	 * @return the tipoFacturaSistema
	 */
	public boolean getTipoFacturaSistema() {
		return tipoFacturaSistema;
	}

	/**
	 * @param tipoFacturaSistema the tipoFacturaSistema to set
	 */
	public void setTipoFacturaSistema(boolean tipoFacturaSistema) {
		this.tipoFacturaSistema = tipoFacturaSistema;
	}

	/**
	 * @return the valorAbonos
	 */
	public double getValorAbonos() {
		return valorAbonos;
	}

	/**
	 * @param valorAbonos the valorAbonos to set
	 */
	public void setValorAbonos(double valorAbonos) {
		this.valorAbonos = valorAbonos;
	}

	/**
	 * @return the valorBrutoPac
	 */
	public double getValorBrutoPac() {
		return valorBrutoPac;
	}

	/**
	 * @param valorBrutoPac the valorBrutoPac to set
	 */
	public void setValorBrutoPac(double valorBrutoPac) {
		this.valorBrutoPac = valorBrutoPac;
	}

	/**
	 * @return the valorCartera
	 */
	public double getValorCartera() {
		return valorCartera;
	}

	/**
	 * @param valorCartera the valorCartera to set
	 */
	public void setValorCartera(double valorCartera) {
		this.valorCartera = valorCartera;
	}

	/**
	 * @return the valorConvenio
	 */
	public double getValorConvenio() {
		return valorConvenio;
	}

	/**
	 * @param valorConvenio the valorConvenio to set
	 */
	public void setValorConvenio(double valorConvenio) {
		this.valorConvenio = valorConvenio;
	}

	/**
	 * @return the valorDescuentoPaciente
	 */
	public double getValorDescuentoPaciente() {
		return valorDescuentoPaciente;
	}

	/**
	 * @param valorDescuentoPaciente the valorDescuentoPaciente to set
	 */
	public void setValorDescuentoPaciente(double valorDescuentoPaciente) {
		this.valorDescuentoPaciente = valorDescuentoPaciente;
	}

	/**
	 * @return the valorMonto
	 */
	//public double getValorMonto() {
	//	return valorMonto;
	//}

	/**
	 * @return the valorMonto
	 */
	/*
	public String getValorOPorcentMontoExistenteFormateado() 
	{
		if(valorMonto>0)
			return UtilidadTexto.formatearValores(valorMonto+"");
		else if(porcentajeMonto>0)
			return porcentajeMonto+"%";
		else
			return "";
	}
	*/
	/**
	 * @param valorMonto the valorMonto to set
	 */
	//public void setValorMonto(double valorMonto) {
		//	this.valorMonto = valorMonto;
		//}

	/**
	 * @return the valorNetoPaciente
	 */
	public double getValorNetoPaciente() {
		return valorNetoPaciente;
	}

	/**
	 * @param valorNetoPaciente the valorNetoPaciente to set
	 */
	public void setValorNetoPaciente(double valorNetoPaciente) {
		this.valorNetoPaciente = valorNetoPaciente;
	}

	/**
	 * @return the valorPagos
	 */
	public double getValorPagos() {
		return valorPagos;
	}

	/**
	 * @param valorPagos the valorPagos to set
	 */
	public void setValorPagos(double valorPagos) {
		this.valorPagos = valorPagos;
	}

	/**
	 * @return the valorTotal
	 */
	public double getValorTotal() {
		return valorTotal;
	}

	/**
	 * @param valorTotal the valorTotal to set
	 */
	public void setValorTotal(double valorTotal) {
		this.valorTotal = valorTotal;
	}

	/**
	 * @return the viaIngreso
	 */
	public InfoDatosInt getViaIngreso() {
		return viaIngreso;
	}

	/**
	 * @param viaIngreso the viaIngreso to set
	 */
	public void setViaIngreso(InfoDatosInt viaIngreso) {
		this.viaIngreso = viaIngreso;
	}

	/**
	 * @return the detallesFactura
	 */
	public ArrayList<DtoDetalleFactura> getDetallesFactura() {
		return detallesFactura;
	}

	/**
	 * @param detallesFactura the detallesFactura to set
	 */
	public void setDetallesFactura(ArrayList<DtoDetalleFactura> detallesFactura) {
		this.detallesFactura = detallesFactura;
	}

	/**
	 * @return the detallesFacturaExcentas
	 */
	public ArrayList<DtoDetalleFactura> getDetallesFacturaExcentas() {
		return detallesFacturaExcentas;
	}

	/**
	 * @param detallesFacturaExcentas the detallesFacturaExcentas to set
	 */
	public void setDetallesFacturaExcentas(
			ArrayList<DtoDetalleFactura> detallesFacturaExcentas) {
		this.detallesFacturaExcentas = detallesFacturaExcentas;
	}

	/**
	 * @return the diagnosticoEgresoAcronimoTipoCie
	 */
	public String getDiagnosticoEgresoAcronimoTipoCie() 
	{
		return diagnosticoEgresoAcronimoTipoCie;
	}

	/**
	 * @return the diagnosticoEgresoAcronimoTipoCie
	 */
	public String getDiagnosticoAcronimo() 
	{
		if(!UtilidadTexto.isEmpty(diagnosticoEgresoAcronimoTipoCie))
			return diagnosticoEgresoAcronimoTipoCie.split("-")[0];
		else
			return "";
	}
	
	/**
	 * @return the diagnosticoEgresoAcronimoTipoCie
	 */
	public int getDiagnosticoTipoCie() 
	{
		if(!UtilidadTexto.isEmpty(diagnosticoEgresoAcronimoTipoCie))
			return Integer.parseInt(diagnosticoEgresoAcronimoTipoCie.split("-")[1]);
		else
			return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * @param diagnosticoEgresoAcronimoTipoCie the diagnosticoEgresoAcronimoTipoCie to set
	 */
	public void setDiagnosticoEgresoAcronimoTipoCie(
			String diagnosticoEgresoAcronimoTipoCie) {
		this.diagnosticoEgresoAcronimoTipoCie = diagnosticoEgresoAcronimoTipoCie;
	}

	/**
	 * @return the pacienteTieneExcepcionNaturaleza
	 */
	public boolean getPacienteTieneExcepcionNaturaleza() {
		return pacienteTieneExcepcionNaturaleza;
	}

	/**
	 * @param pacienteTieneExcepcionNaturaleza the pacienteTieneExcepcionNaturaleza to set
	 */
	public void setPacienteTieneExcepcionNaturaleza(
			boolean pacienteTieneExcepcionNaturaleza) {
		this.pacienteTieneExcepcionNaturaleza = pacienteTieneExcepcionNaturaleza;
	}

	/**
	 * @return the esParticular
	 */
	public boolean getEsParticular() {
		return esParticular;
	}

	/**
	 * @param esParticular the esParticular to set
	 */
	public void setEsParticular(boolean esParticular) {
		this.esParticular = esParticular;
	}

	/**
	 * @return the estratoSocial
	 */
	public InfoDatosInt getEstratoSocial() {
		return estratoSocial;
	}

	/**
	 * @param estratoSocial the estratoSocial to set
	 */
	public void setEstratoSocial(InfoDatosInt estratoSocial) {
		this.estratoSocial = estratoSocial;
	}

	public String logger()
	{
		return "Factura codigo->"+codigo+" tipo reg-> "+this.getTipoRegimen()+" Monto cobro->"+this.getMontoCobro()+" estrato->"+this.estratoSocial.getCodigo()+" conv->"+this.getConvenio().getCodigo()+" netoPac->"+valorNetoPaciente+" val liquidacion pac ->"+this.valorLiquidadoPaciente+" val bruto pac->"+this.valorBrutoPac+" val conv->"+valorConvenio+" dxegreso->"+this.getDiagnosticoEgresoAcronimoTipoCie();
	}

	/**
	 * @return the tipoRegimen
	 */
	public InfoDatos getTipoRegimen() {
		return tipoRegimen;
	}

	/**
	 * @param tipoRegimen the tipoRegimen to set
	 */
	public void setTipoRegimen(InfoDatos tipoRegimen) {
		this.tipoRegimen = tipoRegimen;
	}
	
	/**
	 * @return the tipoRegimen
	 */
	public String getTipoRegimenAcronimo() {
		return tipoRegimen.getAcronimo();
	}

	/**
	 * @return the tipoRegimen
	 */
	public String getTipoRegimenNombre() {
		return tipoRegimen.getValue();
	}

	/**
	 * @return the tipoMonto
	 */
	public InfoDatosInt getTipoMonto() {
		return tipoMonto;
	}

	/**
	 * @param tipoMonto the tipoMonto to set
	 */
	public void setTipoMonto(InfoDatosInt tipoMonto) {
		this.tipoMonto = tipoMonto;
	}

	/**
	 * @return the valorBrutoPacienteModificadoXAnioCalendario
	 */
	public boolean getValorBrutoPacienteModificadoXAnioCalendario() {
		return valorBrutoPacienteModificadoXAnioCalendario;
	}

	/**
	 * @param valorBrutoPacienteModificadoXAnioCalendario the valorBrutoPacienteModificadoXAnioCalendario to set
	 */
	public void setValorBrutoPacienteModificadoXAnioCalendario(
			boolean valorBrutoPacienteModificadoXAnioCalendario) {
		this.valorBrutoPacienteModificadoXAnioCalendario = valorBrutoPacienteModificadoXAnioCalendario;
	}

	/**
	 * @return the valorBrutoPacienteModificadoXEvento
	 */
	public boolean getValorBrutoPacienteModificadoXEvento() {
		return valorBrutoPacienteModificadoXEvento;
	}

	/**
	 * @param valorBrutoPacienteModificadoXEvento the valorBrutoPacienteModificadoXEvento to set
	 */
	public void setValorBrutoPacienteModificadoXEvento(
			boolean valorBrutoPacienteModificadoXEvento) {
		this.valorBrutoPacienteModificadoXEvento = valorBrutoPacienteModificadoXEvento;
	}

	/**
	 * @return the valorAFavorConvenio
	 */
	public double getValorAFavorConvenio() {
		return valorAFavorConvenio;
	}

	/**
	 * @param valorAFavorConvenio the valorAFavorConvenio to set
	 */
	public void setValorAFavorConvenio(double valorAFavorConvenio) {
		this.valorAFavorConvenio = valorAFavorConvenio;
	}

	/**
	 * @return the formatoImpresion
	 */
	public InfoDatosInt getFormatoImpresion() {
		return formatoImpresion;
	}

	/**
	 * @param formatoImpresion the formatoImpresion to set
	 */
	public void setFormatoImpresion(InfoDatosInt formatoImpresion) {
		this.formatoImpresion = formatoImpresion;
	}

	/**
	 * @return the idPaciente
	 */
	public String getIdPaciente() {
		return idPaciente;
	}

	/**
	 * @param idPaciente the idPaciente to set
	 */
	public void setIdPaciente(String idPaciente) {
		this.idPaciente = idPaciente;
	}

	/**
	 * @return the nombrePaciente
	 */
	public String getNombrePaciente() {
		return nombrePaciente;
	}

	/**
	 * @param nombrePaciente the nombrePaciente to set
	 */
	public void setNombrePaciente(String nombrePaciente) {
		this.nombrePaciente = nombrePaciente;
	}

	/**
	 * @return the rangoFinalFactura
	 */
	@Deprecated
	public double getRangoFinalFactura() {
		return rangoFinalFactura;
	}

	/**
	 * @param rangoFinalFactura the rangoFinalFactura to set
	 */
	@Deprecated
	public void setRangoFinalFactura(double rangoFinalFactura) {
		this.rangoFinalFactura = rangoFinalFactura;
	}

	/**
	 * @return the rangoInicialFactura
	 */
	public double getRangoInicialFactura() {
		return rangoInicialFactura;
	}

	/**
	 * @param rangoInicialFactura the rangoInicialFactura to set
	 */
	@Deprecated
	public void setRangoInicialFactura(double rangoInicialFactura) {
		this.rangoInicialFactura = rangoInicialFactura;
	}

	/**
	 * @return the resolucionDian
	 */
	@Deprecated
	public String getResolucionDian() {
		return resolucionDian;
	}

	/**
	 * @param resolucionDian the resolucionDian to set
	 */
	@Deprecated
	public void setResolucionDian(String resolucionDian) {
		this.resolucionDian = resolucionDian;
	}

	/**
	 * @return the validarInfoVenezuela
	 */
	public boolean getValidarInfoVenezuela() {
		return validarInfoVenezuela;
	}

	/**
	 * @param validarInfoVenezuela the validarInfoVenezuela to set
	 */
	public void setValidarInfoVenezuela(boolean validarInfoVenezuela) {
		this.validarInfoVenezuela = validarInfoVenezuela;
	}

	/**
	 * @return the valorMontoAutorizadoVenezuela
	 */
	public double getValorMontoAutorizadoVenezuela() {
		return valorMontoAutorizadoVenezuela;
	}

	/**
	 * @param valorMontoAutorizadoVenezuela the valorMontoAutorizadoVenezuela to set
	 */
	public void setValorMontoAutorizadoVenezuela(
			double valorMontoAutorizadoVenezuela) {
		this.valorMontoAutorizadoVenezuela = valorMontoAutorizadoVenezuela;
	}

	/**
	 * @return the valorFaltaAsignarVenezuela
	 */
	public double getValorFaltaAsignarVenezuela() {
		return valorFaltaAsignarVenezuela;
	}

	/**
	 * @param valorFaltaAsignarVenezuela the valorFaltaAsignarVenezuela to set
	 */
	public void setValorFaltaAsignarVenezuela(double valorFaltaAsignarVenezuela) {
		this.valorFaltaAsignarVenezuela = valorFaltaAsignarVenezuela;
	}

	/**
	 * @return the empresaInstitucion
	 */
	public double getEmpresaInstitucion() {
		return empresaInstitucion;
	}

	/**
	 * @param empresaInstitucion the empresaInstitucion to set
	 */
	public void setEmpresaInstitucion(double empresaInstitucion) {
		this.empresaInstitucion = empresaInstitucion;
	}

	/**
	 * @return the entidadSubcontratada
	 */
	public double getEntidadSubcontratada() {
		return entidadSubcontratada;
	}

	/**
	 * @param entidadSubcontratada the entidadSubcontratada to set
	 */
	public void setEntidadSubcontratada(double entidadSubcontratada) {
		this.entidadSubcontratada = entidadSubcontratada;
	}

	/**
	 * @return the valorBrutoPacSinModParamConvenio
	 */
	public double getValorBrutoPacSinModParamConvenio() {
		return valorBrutoPacSinModParamConvenio;
	}

	/**
	 * @param valorBrutoPacSinModParamConvenio the valorBrutoPacSinModParamConvenio to set
	 */
	public void setValorBrutoPacSinModParamConvenio(
			double valorBrutoPacSinModParamConvenio) {
		this.valorBrutoPacSinModParamConvenio = valorBrutoPacSinModParamConvenio;
	}

	/**
	 * @return the pacienteEntidadSubcontratada
	 */
	public double getPacienteEntidadSubcontratada() {
		return pacienteEntidadSubcontratada;
	}

	/**
	 * @param pacienteEntidadSubcontratada the pacienteEntidadSubcontratada to set
	 */
	public void setPacienteEntidadSubcontratada(double pacienteEntidadSubcontratada) {
		this.pacienteEntidadSubcontratada = pacienteEntidadSubcontratada;
	}

	/**
	 * @return the valorLiquidadoPaciente
	 */
	public double getValorLiquidadoPaciente() {
		return valorLiquidadoPaciente;
	}

	/**
	 * @param valorLiquidadoPaciente the valorLiquidadoPaciente to set
	 */
	public void setValorLiquidadoPaciente(double valorLiquidadoPaciente) {
		this.valorLiquidadoPaciente = valorLiquidadoPaciente;
	}

	/**
	 * @return the fechaVigenciaInicialMontoCobro
	 */
	public String getFechaVigenciaInicialMontoCobro() {
		return fechaVigenciaInicialMontoCobro;
	}

	/**
	 * @param fechaVigenciaInicialMontoCobro the fechaVigenciaInicialMontoCobro to set
	 */
	public void setFechaVigenciaInicialMontoCobro(
			String fechaVigenciaInicialMontoCobro) {
		this.fechaVigenciaInicialMontoCobro = fechaVigenciaInicialMontoCobro;
	}

	/**
	 * @return the fechaVigenciaInicialTopeFacturacion
	 */
	public String getFechaVigenciaInicialTopeFacturacion() {
		return fechaVigenciaInicialTopeFacturacion;
	}

	/**
	 * @param fechaVigenciaInicialTopeFacturacion the fechaVigenciaInicialTopeFacturacion to set
	 */
	public void setFechaVigenciaInicialTopeFacturacion(
			String fechaVigenciaInicialTopeFacturacion) {
		this.fechaVigenciaInicialTopeFacturacion = fechaVigenciaInicialTopeFacturacion;
	}

	/**
	 * @return the topeFacturacion
	 */
	public int getTopeFacturacion() {
		return topeFacturacion;
	}

	/**
	 * @param topeFacturacion the topeFacturacion to set
	 */
	public void setTopeFacturacion(int topeFacturacion) {
		this.topeFacturacion = topeFacturacion;
	}

	/**
	 * @return the centroAtencionDuenio
	 */
	public InfoDatosInt getCentroAtencionDuenio() {
		return centroAtencionDuenio;
	}

	/**
	 * @param centroAtencionDuenio the centroAtencionDuenio to set
	 */
	public void setCentroAtencionDuenio(InfoDatosInt centroAtencionDuenio) {
		this.centroAtencionDuenio = centroAtencionDuenio;
	}

	/**
	 * @return the pacientePagaAtencionXMontoCobro
	 */
	public boolean isPacientePagaAtencionXMontoCobro() {
		return pacientePagaAtencionXMontoCobro;
	}

	/**
	 * @param pacientePagaAtencionXMontoCobro the pacientePagaAtencionXMontoCobro to set
	 */
	public void setPacientePagaAtencionXMontoCobro(
			boolean pacientePagaAtencionXMontoCobro) {
		this.pacientePagaAtencionXMontoCobro = pacientePagaAtencionXMontoCobro;
	}

	/**
	 * @return the controlaAnticipos
	 */
	public boolean isControlaAnticipos() {
		return controlaAnticipos;
	}

	/**
	 * @param controlaAnticipos the controlaAnticipos to set
	 */
	public void setControlaAnticipos(boolean controlaAnticipos) {
		this.controlaAnticipos = controlaAnticipos;
	}

	/**
	 * @return the valorAnticipos
	 */
	public double getValorAnticipos() {
		return valorAnticipos;
	}

	/**
	 * @param valorAnticipos the valorAnticipos to set
	 */
	public void setValorAnticipos(double valorAnticipos) {
		this.valorAnticipos = valorAnticipos;
	}

	public DtoInfoCobroPaciente getInforCalculoValorPaciente() {
		return inforCalculoValorPaciente;
	}

	public void setInforCalculoValorPaciente(
			DtoInfoCobroPaciente inforCalculoValorPaciente) {
		this.inforCalculoValorPaciente = inforCalculoValorPaciente;
	}
	
	/**
	 * @param historicoEncabezado the historicoEncabezado to set
	 */
	public void setHistoricoEncabezado(HistoricoEncabezado historicoEncabezado) {
		this.historicoEncabezado = historicoEncabezado;
	}

	/**
	 * @return the historicoEncabezado
	 */
	public HistoricoEncabezado getHistoricoEncabezado() {
		return historicoEncabezado;
	}

	/**
	 * 
	 * @return
	 */
	public String getFechaHoraElaboracion()
	{
		return this.fecha+" "+this.hora;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getNombreConvenio()
	{
		return this.convenio.getNombre();
	}
}
