package com.princetonsa.dto.inventario;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import util.ConstantesBD;

import com.servinte.axioma.orm.Convenios;


/**
 * Encaps&uacute;la la respuesta a la informaci&oacute;n de Articulos (Medicamentos e Insumos) para las autorizaciones
 * @author Cristhian Murillo - Diana Carolina G?mez
 */
public class DtoArticulosAutorizaciones implements Serializable{
	

	private static final long serialVersionUID = 1L;
	
	/*--------------------------*/
	/* ATRIBUTOS DTO			*/
	/*--------------------------*/
	private long autorizacion;
	private long autorizacionPropia;
	private int cantidadSolicitada;
	private Convenios convenioResponsable;
	private int nivelAutorizacion;
	private Integer numeroOrden;
	private Integer consecutivoOrdenMed;
	private String consecutivoOrdenMedStr; 
	private Long numeroOrdenLong;
	private String numeroOrdenStr;
	private Date fechaOrden;
	private String horaOrden;
	
	// ARTICULOS  ---------------------------------------------------------
	private Integer codigoArticulo;
	private String descripcionArticulo;
	private int codigoSubGrupoArticulo;
	private int codigoClaseInvArticulo;
	private String nombreClaseInvArticulo;
	private String naturalezaArticulo;
	
	private String acronimoNaturalezaArticulo;
	
	private Character esMedicamento;
	private String cantidadAutorizadaArticulo;
	private String unidadMedidaArticulo;
	private String acronimoUnidadMedidaArticulo;
	private Long dosisXArticuloID;
	private String observacionesArticulo;
	private Integer despachoTotal;
	private Integer despachoEntregar;
	private BigDecimal valorArticulo;
	private BigDecimal valorAutorizado;
	private String descripcionNivelAutorizacion;
	
	// Medicamentos -------------------------------
	private String concentracionArticulo;
	private String formaFarmaceuticaArticulo;
		//- Formulaci?n 
		private String dosisFormulacion;
		private Integer frecuenciaFormulacion;
		private Long frecuenciaFormulacionLong; 
		private String viaFormulacion;
		private Long diasTratamientoFormulacion;
		private Integer diasTratamientoFormulacionInt; 
		private Integer totalUnidadesFormulacion;
		private Long totalUnidadesFormulacionLong; 
		private String tipoFrecuenciaFormulacion;
		
	// Insumos ------------------------------------
	private int cantidadTotalInsumo;
	private int cantidadDespacho;
	
	// Otros Atributos ----------------------------
	private boolean tieneAutorizacion;
	
	/** Indica que ha sido seleccionado o que es valido*/
	private boolean flag;
	private boolean valido;
	
	// Diagnostico
	private String acronimoDx;
	private Integer tipoCieDx;
	private String diagnostico;
	private String descripcionDiagnostico;
	
	
	private Long numeroOrdenAmb;
	
	private String consecutivoOrdenAmb;
	
	private Date fechaOrdenAmb;
	
	private Integer numeroPeticion;
	
	private Date fechaPeticion;
	
	// Diagnostico	Ordenes Ambulatorias
	
	private String diagnosticoOrdenAmb;		
	
	private Integer tipoCieDxOrdenAmb;	
	
	private String descripcionDiagnosticoOrdenAmb;
	
	// Diagnostico	Peticiones
	
	private String diagnosticoPeticion;		
	
	private Integer tipoCieDxPeticion;	
	
	private String descripcionDiagnosticoPeticion;
	
	//Diagnostico	Peticiones
	
	private String diagnosticoIngEst;		
	
	private Integer tipoCieDxIngEst;	
	
	private String descripcionDiagnosticoIngEst;
	
	private Integer contratoConvenioResponsable;
	
	/**
	 * Define si el articulo fue solicitado, proviene de una orden ambulatoria, de una peticion o de un ingreso estancia
	 */
	private long tipoAutorizacion;
	
	/**
	 * Codigo que tiene la orden ambulatoria o solicitud o peticion
	 */
	private Long codigoOrdenSolPet;
	
	/**
	 * Codigo de la via de ingreso que tiene la solicitud
	 */
	private int codigoViaIngreso;
	
	private int tipoSolicitud;
	
	private boolean pyp;
	/**
	 * Constructor
	 */
	public DtoArticulosAutorizaciones()
	{
		this.autorizacion				= ConstantesBD.codigoNuncaValidoLong;
		this.autorizacionPropia			= ConstantesBD.codigoNuncaValidoLong;
		
		
		// ARTICULOS  ---------------------------------------------------------
		this.codigoArticulo				= null;
		this.descripcionArticulo		= "";
		this.codigoSubGrupoArticulo		= ConstantesBD.codigoNuncaValido;
		this.codigoClaseInvArticulo		 = ConstantesBD.codigoNuncaValido;
		this.nombreClaseInvArticulo		 = null;
		this.naturalezaArticulo			= null;
		this.esMedicamento				= null;
		this.cantidadAutorizadaArticulo	= "";
		this.unidadMedidaArticulo		= "";
		this.acronimoUnidadMedidaArticulo = "";
		this.observacionesArticulo		= "";
		this.despachoTotal				= Integer.valueOf(0);
		this.despachoEntregar			= Integer.valueOf(0);
		
		// Medicamentos -------------------------------
		this.concentracionArticulo		= "";
		this.formaFarmaceuticaArticulo	= "";
			//- Formulaci?n 
			this.dosisFormulacion				= "";
			this.frecuenciaFormulacion			= null;
			this.frecuenciaFormulacionLong		= null;
			this.viaFormulacion					= "";
			this.diasTratamientoFormulacion		= null;
			this.diasTratamientoFormulacionInt	= null;
			this.totalUnidadesFormulacion		= null;
			this.totalUnidadesFormulacionLong 	= null;
			this.tipoFrecuenciaFormulacion		= "";
			
		// Insumos ------------------------------------
		this.cantidadTotalInsumo			= ConstantesBD.codigoNuncaValido;
		this.cantidadDespacho				= ConstantesBD.codigoNuncaValido;
		
		// Otros Atributos ----------------------------
		this.tieneAutorizacion 				= false;
		this.flag							= false;
		this.valido							= false;
		
		this.numeroOrden 					= ConstantesBD.codigoNuncaValido;
		this.consecutivoOrdenMed			= ConstantesBD.codigoNuncaValido;
		this.consecutivoOrdenMedStr			= null;
		this.numeroOrdenLong				= null;
		this.numeroOrdenStr					= null;
		this.fechaOrden						= null;
		this.horaOrden						="";
		
		this.acronimoDx						= "";
		this.tipoCieDx						= null;
		this.diagnostico						= "";
		this.nivelAutorizacion				= ConstantesBD.codigoNuncaValido;
		
		this.setDescripcionNivelAutorizacion("");
		this.dosisXArticuloID 				= ConstantesBD.codigoNuncaValidoLong;
		this.descripcionDiagnostico         = "";
		
		this.numeroOrdenAmb					= null;
		this.consecutivoOrdenAmb			= null;
		this.fechaOrdenAmb					= null;
		this.numeroPeticion					= null;
		this.fechaPeticion					= null;
		
		this.diagnosticoOrdenAmb			= "";;
		this.tipoCieDxOrdenAmb				= null;
		this.descripcionDiagnosticoOrdenAmb = "";
		
		this.diagnosticoPeticion			= "";;
		this.tipoCieDxPeticion				= null;
		this.descripcionDiagnosticoPeticion = "";
		
		this.diagnosticoIngEst				= "";;
		this.tipoCieDxIngEst				= null;
		this.descripcionDiagnosticoIngEst	= "";
		this.contratoConvenioResponsable=ConstantesBD.codigoNuncaValido;	
		this.tipoSolicitud		= ConstantesBD.codigoNuncaValido;
		
	}

	
	public long getAutorizacion() {
		return autorizacion;
	}


	public void setAutorizacion(long autorizacion) {
		this.autorizacion = autorizacion;
	}
	

	public Integer getCodigoArticulo() {
		return codigoArticulo;
	}


	public void setCodigoArticulo(Integer codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}


	public String getDescripcionArticulo() {
		return descripcionArticulo;
	}


	public void setDescripcionArticulo(String descripcionArticulo) {
		this.descripcionArticulo = descripcionArticulo;
	}


	public String getCantidadAutorizadaArticulo() {
		return cantidadAutorizadaArticulo;
	}


	public void setCantidadAutorizadaArticulo(String cantidadAutorizadaArticulo) {
		this.cantidadAutorizadaArticulo = cantidadAutorizadaArticulo;
	}


	public String getUnidadMedidaArticulo() {
		return unidadMedidaArticulo;
	}


	public void setUnidadMedidaArticulo(String unidadMedidaArticulo) {
		this.unidadMedidaArticulo = unidadMedidaArticulo;
	}


	public String getObservacionesArticulo() {
		return observacionesArticulo;
	}


	public void setObservacionesArticulo(String observacionesArticulo) {
		this.observacionesArticulo = observacionesArticulo;
	}


	public Integer getDespachoTotal() {
		return despachoTotal;
	}


	public void setDespachoTotal(Integer despachoTotal) {
		if(despachoTotal == null){
			this.despachoTotal = Integer.valueOf(0);
		}
		else{
			this.despachoTotal = despachoTotal;
		}
		
	}


	public Integer getDespachoEntregar() {
		return despachoEntregar;
	}


	public void setDespachoEntregar(Integer despachoEntregar) {
		this.despachoEntregar = despachoEntregar;
	}


	public String getConcentracionArticulo() {
		return concentracionArticulo;
	}


	public void setConcentracionArticulo(String concentracionArticulo) {
		this.concentracionArticulo = concentracionArticulo;
	}


	public String getFormaFarmaceuticaArticulo() {
		return formaFarmaceuticaArticulo;
	}


	public void setFormaFarmaceuticaArticulo(String formaFarmaceuticaArticulo) {
		this.formaFarmaceuticaArticulo = formaFarmaceuticaArticulo;
	}


	public String getDosisFormulacion() {
		return dosisFormulacion;
	}


	public void setDosisFormulacion(String dosisFormulacion) {
		this.dosisFormulacion = dosisFormulacion;
	}


	public Integer getFrecuenciaFormulacion() {
		return frecuenciaFormulacion;
	}


	public void setFrecuenciaFormulacion(Integer frecuenciaFormulacion) {
		this.frecuenciaFormulacion = frecuenciaFormulacion;
	}


	public String getViaFormulacion() {
		return viaFormulacion;
	}


	public void setViaFormulacion(String viaFormulacion) {
		this.viaFormulacion = viaFormulacion;
	}


	public Long getDiasTratamientoFormulacion() {
		return diasTratamientoFormulacion;
	}


	public void setDiasTratamientoFormulacion(Long diasTratamientoFormulacion) {
		this.diasTratamientoFormulacion = diasTratamientoFormulacion;
	}


	public Integer getTotalUnidadesFormulacion() {
		return totalUnidadesFormulacion;
	}


	public void setTotalUnidadesFormulacion(Integer totalUnidadesFormulacion) {
		this.totalUnidadesFormulacion = totalUnidadesFormulacion;
	}


	public int getCantidadTotalInsumo() {
		return cantidadTotalInsumo;
	}


	public void setCantidadTotalInsumo(int cantidadTotalInsumo) {
		this.cantidadTotalInsumo = cantidadTotalInsumo;
	}


	public int getCantidadDespacho() {
		return cantidadDespacho;
	}


	public void setCantidadDespacho(int cantidadDespacho) {
		this.cantidadDespacho = cantidadDespacho;
	}


	public BigDecimal getValorArticulo() {
		return valorArticulo;
	}


	public void setValorArticulo(BigDecimal valorArticulo) {
		this.valorArticulo = valorArticulo;
	}


	public BigDecimal getValorAutorizado() {
		return valorAutorizado;
	}


	public void setValorAutorizado(BigDecimal valorAutorizado) {
		this.valorAutorizado = valorAutorizado;
	}


	public long getAutorizacionPropia() {
		return autorizacionPropia;
	}


	public void setAutorizacionPropia(long autorizacionPropia) {
		this.autorizacionPropia = autorizacionPropia;
	}


	public boolean isTieneAutorizacion() {
		return tieneAutorizacion;
	}


	public void setTieneAutorizacion(boolean tieneAutorizacion) {
		this.tieneAutorizacion = tieneAutorizacion;
	}


	/**
	 * Este M?todo se encarga de obtener el valor 
	 * del atributo cantidadSolicitada
	
	 * @return retorna la variable cantidadSolicitada 
	 * @author Angela Maria Aguirre 
	 */
	public int getCantidadSolicitada() {
		return cantidadSolicitada;
	}


	/**
	 * Este M?todo se encarga de establecer el valor 
	 * del atributo cantidadSolicitada
	
	 * @param valor para el atributo cantidadSolicitada 
	 * @author Angela Maria Aguirre 
	 */
	public void setCantidadSolicitada(int cantidadSolicitada) {
		this.cantidadSolicitada = cantidadSolicitada;
	}


	/**
	 * Este M?todo se encarga de obtener el valor 
	 * del atributo convenioResponsable
	
	 * @return retorna la variable convenioResponsable 
	 * @author Angela Maria Aguirre 
	 */
	public Convenios getConvenioResponsable() {
		return convenioResponsable;
	}


	/**
	 * Este M?todo se encarga de establecer el valor 
	 * del atributo convenioResponsable
	
	 * @param valor para el atributo convenioResponsable 
	 * @author Angela Maria Aguirre 
	 */
	public void setConvenioResponsable(Convenios convenioResponsable) {
		this.convenioResponsable = convenioResponsable;
	}


	/**
	 * Este M?todo se encarga de obtener el valor 
	 * del atributo nivelAutorizacion
	
	 * @return retorna la variable nivelAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public int getNivelAutorizacion() {
		return nivelAutorizacion;
	}


	/**
	 * Este M?todo se encarga de establecer el valor 
	 * del atributo nivelAutorizacion
	
	 * @param valor para el atributo nivelAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public void setNivelAutorizacion(int nivelAutorizacion) {
		this.nivelAutorizacion = nivelAutorizacion;
	}


	/**
	 * Este M?todo se encarga de obtener el valor 
	 * del atributo naturalezaArticulo
	
	 * @return retorna la variable naturalezaArticulo 
	 * @author Angela Maria Aguirre 
	 */
	public String getNaturalezaArticulo() {
		return naturalezaArticulo;
	}


	/**
	 * Este M?todo se encarga de establecer el valor 
	 * del atributo naturalezaArticulo
	
	 * @param valor para el atributo naturalezaArticulo 
	 * @author Angela Maria Aguirre 
	 */
	public void setNaturalezaArticulo(String naturalezaArticulo) {
		this.naturalezaArticulo = naturalezaArticulo;
	}


	public Character getEsMedicamento() {
		return esMedicamento;
	}


	public void setEsMedicamento(Character esMedicamento) {
		this.esMedicamento = esMedicamento;
	}


	/**
	 * Este M?todo se encarga de obtener el valor 
	 * del atributo numeroOrden
	
	 * @return retorna la variable numeroOrden 
	 * @author Angela Maria Aguirre 
	 */
	public Integer getNumeroOrden() {
		return numeroOrden;
	}


	/**
	 * Este M?todo se encarga de establecer el valor 
	 * del atributo numeroOrden
	
	 * @param valor para el atributo numeroOrden 
	 * @author Angela Maria Aguirre 
	 */
	public void setNumeroOrden(Integer numeroOrden) {
		this.numeroOrden = numeroOrden;
		if(numeroOrden != null){
			this.numeroOrdenLong = Long.valueOf(numeroOrden.longValue());
			this.numeroOrdenStr = String.valueOf(numeroOrden.intValue());
		}
		else{
			this.numeroOrdenLong = null;
			this.numeroOrdenStr = null;
		}
	}


	public boolean isFlag() {
		return flag;
	}


	public void setFlag(boolean flag) {
		this.flag = flag;
	}


	public Date getFechaOrden() {
		return fechaOrden;
	}


	public void setFechaOrden(Date fechaOrden) {
		this.fechaOrden = fechaOrden;
	}


	public String getAcronimoDx() {
		return acronimoDx;
	}


	public void setAcronimoDx(String acronimoDx) {
		this.acronimoDx = acronimoDx;
	}


	public Integer getTipoCieDx() {
		return tipoCieDx;
	}


	public void setTipoCieDx(Integer tipoCieDx) {
		this.tipoCieDx = tipoCieDx;
	}


	public void setDescripcionNivelAutorizacion(
			String descripcionNivelAutorizacion) {
		this.descripcionNivelAutorizacion = descripcionNivelAutorizacion;
	}


	public String getDescripcionNivelAutorizacion() {
		return descripcionNivelAutorizacion;
	}


	public String getDiagnostico() {
		return diagnostico;
	}


	public void setDiagnostico(String diagnostico) {
		this.diagnostico = diagnostico;
	}


	public boolean isValido() {
		return valido;
	}


	public void setValido(boolean valido) {
		this.valido = valido;
	}


	/**
	 * @return Retorna el atributo acronimoUnidadMedidaArticulo
	 */
	public String getAcronimoUnidadMedidaArticulo() {
		return acronimoUnidadMedidaArticulo;
	}


	/**
	 * @param acronimoUnidadMedidaArticulo Asigna el atributo acronimoUnidadMedidaArticulo
	 */
	public void setAcronimoUnidadMedidaArticulo(String acronimoUnidadMedidaArticulo) {
		this.acronimoUnidadMedidaArticulo = acronimoUnidadMedidaArticulo;
	}


	/**
	 * @return Retorna el atributo dosisXArticuloID
	 */
	public Long getDosisXArticuloID() {
		return dosisXArticuloID;
	}


	/**
	 * @param dosisXArticuloID Asigna el atributo dosisXArticuloID
	 */
	public void setDosisXArticuloID(Long dosisXArticuloID) {
		this.dosisXArticuloID = dosisXArticuloID;
	}
	
	public void setHoraOrden(String horaOrden) {
		this.horaOrden = horaOrden;
	}


	public String getHoraOrden() {
		return horaOrden;
	}

	/**
	 * Este M?todo se encarga de obtener el valor 
	 * del atributo descripcionDiagnostico
	
	 * @return retorna la variable descripcionDiagnostico 
	 * @author Angela Maria Aguirre 
	 */
	public String getDescripcionDiagnostico() {
		return descripcionDiagnostico;
	}


	/**
	 * Este M?todo se encarga de establecer el valor 
	 * del atributo descripcionDiagnostico
	
	 * @param valor para el atributo descripcionDiagnostico 
	 * @author Angela Maria Aguirre 
	 */
	public void setDescripcionDiagnostico(String descripcionDiagnostico) {
		this.descripcionDiagnostico = descripcionDiagnostico;
	}

	/**
	 * Este M?todo se encarga de establecer el valor 
	 * del atributo tipoFrecuenciaFormulacion
	
	 * @param valor para el atributo tipoFrecuenciaFormulacion
	 * @author Diana Carolina G
	 */
	public void setTipoFrecuenciaFormulacion(String tipoFrecuenciaFormulacion) {
		this.tipoFrecuenciaFormulacion = tipoFrecuenciaFormulacion;
	}

	/**
	 * Este M?todo se encarga de obtener el valor 
	 * del atributo tipoFrecuenciaFormulacion
	
	 * @return retorna la variable tipoFrecuenciaFormulacion 
	 * @author Diana Carolina G 
	 */
	public String getTipoFrecuenciaFormulacion() {
		return tipoFrecuenciaFormulacion; 
	}

//-----------------
	
	/**
	 * @return valor de frecuenciaFormulacionLong
	 */
	public Long getFrecuenciaFormulacionLong() {
		return frecuenciaFormulacionLong;
	}


	/**
	 * @param frecuenciaFormulacionLong el frecuenciaFormulacionLong para asignar
	 */
	public void setFrecuenciaFormulacionLong(Long frecuenciaFormulacionLong) {
		this.frecuenciaFormulacionLong = frecuenciaFormulacionLong;
		if(this.frecuenciaFormulacionLong!=null){
			this.frecuenciaFormulacion = Integer.valueOf(this.frecuenciaFormulacionLong.intValue());
		}
	}


	/**
	 * @return valor de diasTratamientoFormulacionInt
	 */
	public Integer getDiasTratamientoFormulacionInt() {
		return diasTratamientoFormulacionInt;
	}


	/**
	 * @param diasTratamientoFormulacionInt el diasTratamientoFormulacionInt para asignar
	 */
	public void setDiasTratamientoFormulacionInt(Integer diasTratamientoFormulacionInt) {
		this.diasTratamientoFormulacionInt = diasTratamientoFormulacionInt;
		if(this.diasTratamientoFormulacionInt!=null){
			this.diasTratamientoFormulacion = Long.valueOf(this.diasTratamientoFormulacionInt.longValue());
		}
	}


	/**
	 * @return valor de totalUnidadesFormulacionLong
	 */
	public Long getTotalUnidadesFormulacionLong() {
		return totalUnidadesFormulacionLong;
	}


	/**
	 * @param totalUnidadesFormulacionLong el totalUnidadesFormulacionLong para asignar
	 */
	public void setTotalUnidadesFormulacionLong(Long totalUnidadesFormulacionLong) {
		this.totalUnidadesFormulacionLong = totalUnidadesFormulacionLong;
		if(this.totalUnidadesFormulacionLong!=null){
			this.totalUnidadesFormulacion = Integer.valueOf(this.totalUnidadesFormulacionLong.intValue());
		}
	}
	
	public void setConsecutivoOrdenMed(Integer consecutivoOrdenMed) {
		this.consecutivoOrdenMed = consecutivoOrdenMed;
	}


	public Integer getConsecutivoOrdenMed() {
		return consecutivoOrdenMed;
	}


	/**
	 * @return valor de numeroOrdenLong
	 */
	public Long getNumeroOrdenLong() {
		return numeroOrdenLong;
	}


	/**
	 * @param numeroOrdenLong el numeroOrdenLong para asignar
	 */
	public void setNumeroOrdenLong(Long numeroOrdenLong) {
		this.numeroOrdenLong = numeroOrdenLong;
		if(this.numeroOrdenLong!=null){
			this.numeroOrden = this.numeroOrdenLong.intValue();
		}else{
			this.numeroOrden =null;
		}
	}


	/**
	 * @return valor de numeroOrdenStr
	 */
	public String getNumeroOrdenStr() {
		return numeroOrdenStr;
	}


	/**
	 * @param numeroOrdenStr el numeroOrdenStr para asignar
	 */
	public void setNumeroOrdenStr(String numeroOrdenStr) {
		this.numeroOrdenStr = numeroOrdenStr;
		this.numeroOrden = Integer.parseInt(this.numeroOrdenStr);
	}


	/**
	 * @return valor de consecutivoOrdenMedStr
	 */
	public String getConsecutivoOrdenMedStr() {
		return consecutivoOrdenMedStr;
	}


	/**
	 * @param consecutivoOrdenMedStr el consecutivoOrdenMedStr para asignar
	 */
	public void setConsecutivoOrdenMedStr(String consecutivoOrdenMedStr) {
		this.consecutivoOrdenMedStr = consecutivoOrdenMedStr;
		this.consecutivoOrdenMed = Integer.parseInt(consecutivoOrdenMedStr);
	}


	/**
	 * @return the numeroOrdenAmb
	 */
	public Long getNumeroOrdenAmb() {
		return numeroOrdenAmb;
	}


	/**
	 * @param numeroOrdenAmb the numeroOrdenAmb to set
	 */
	public void setNumeroOrdenAmb(Long numeroOrdenAmb) {
		this.numeroOrdenAmb = numeroOrdenAmb;
	}


	/**
	 * @return the fechaOrdenAmb
	 */
	public Date getFechaOrdenAmb() {
		return fechaOrdenAmb;
	}


	/**
	 * @param fechaOrdenAmb the fechaOrdenAmb to set
	 */
	public void setFechaOrdenAmb(Date fechaOrdenAmb) {
		this.fechaOrdenAmb = fechaOrdenAmb;
	}


	/**
	 * @return the numeroPeticion
	 */
	public Integer getNumeroPeticion() {
		return numeroPeticion;
	}


	/**
	 * @param numeroPeticion the numeroPeticion to set
	 */
	public void setNumeroPeticion(Integer numeroPeticion) {
		this.numeroPeticion = numeroPeticion;
	}


	/**
	 * @return the fechaPeticion
	 */
	public Date getFechaPeticion() {
		return fechaPeticion;
	}


	/**
	 * @param fechaPeticion the fechaPeticion to set
	 */
	public void setFechaPeticion(Date fechaPeticion) {
		this.fechaPeticion = fechaPeticion;
	}


	/**
	 * @return the diagnosticoOrdenAmb
	 */
	public String getDiagnosticoOrdenAmb() {
		return diagnosticoOrdenAmb;
	}


	/**
	 * @param diagnosticoOrdenAmb the diagnosticoOrdenAmb to set
	 */
	public void setDiagnosticoOrdenAmb(String diagnosticoOrdenAmb) {
		this.diagnosticoOrdenAmb = diagnosticoOrdenAmb;
	}


	/**
	 * @return the tipoCieDxOrdenAmb
	 */
	public Integer getTipoCieDxOrdenAmb() {
		return tipoCieDxOrdenAmb;
	}


	/**
	 * @param tipoCieDxOrdenAmb the tipoCieDxOrdenAmb to set
	 */
	public void setTipoCieDxOrdenAmb(Integer tipoCieDxOrdenAmb) {
		this.tipoCieDxOrdenAmb = tipoCieDxOrdenAmb;
	}


	/**
	 * @return the descripcionDiagnosticoOrdenAmb
	 */
	public String getDescripcionDiagnosticoOrdenAmb() {
		return descripcionDiagnosticoOrdenAmb;
	}


	/**
	 * @param descripcionDiagnosticoOrdenAmb the descripcionDiagnosticoOrdenAmb to set
	 */
	public void setDescripcionDiagnosticoOrdenAmb(
			String descripcionDiagnosticoOrdenAmb) {
		this.descripcionDiagnosticoOrdenAmb = descripcionDiagnosticoOrdenAmb;
	}


	/**
	 * @return the diagnosticoPeticion
	 */
	public String getDiagnosticoPeticion() {
		return diagnosticoPeticion;
	}


	/**
	 * @param diagnosticoPeticion the diagnosticoPeticion to set
	 */
	public void setDiagnosticoPeticion(String diagnosticoPeticion) {
		this.diagnosticoPeticion = diagnosticoPeticion;
	}


	/**
	 * @return the tipoCieDxPeticion
	 */
	public Integer getTipoCieDxPeticion() {
		return tipoCieDxPeticion;
	}


	/**
	 * @param tipoCieDxPeticion the tipoCieDxPeticion to set
	 */
	public void setTipoCieDxPeticion(Integer tipoCieDxPeticion) {
		this.tipoCieDxPeticion = tipoCieDxPeticion;
	}


	/**
	 * @return the descripcionDiagnosticoPeticion
	 */
	public String getDescripcionDiagnosticoPeticion() {
		return descripcionDiagnosticoPeticion;
	}


	/**
	 * @param descripcionDiagnosticoPeticion the descripcionDiagnosticoPeticion to set
	 */
	public void setDescripcionDiagnosticoPeticion(
			String descripcionDiagnosticoPeticion) {
		this.descripcionDiagnosticoPeticion = descripcionDiagnosticoPeticion;
	}


	/**
	 * @return the diagnosticoIngEst
	 */
	public String getDiagnosticoIngEst() {
		return diagnosticoIngEst;
	}


	/**
	 * @param diagnosticoIngEst the diagnosticoIngEst to set
	 */
	public void setDiagnosticoIngEst(String diagnosticoIngEst) {
		this.diagnosticoIngEst = diagnosticoIngEst;
	}


	/**
	 * @return the tipoCieDxIngEst
	 */
	public Integer getTipoCieDxIngEst() {
		return tipoCieDxIngEst;
	}


	/**
	 * @param tipoCieDxIngEst the tipoCieDxIngEst to set
	 */
	public void setTipoCieDxIngEst(Integer tipoCieDxIngEst) {
		this.tipoCieDxIngEst = tipoCieDxIngEst;
	}


	/**
	 * @return the descripcionDiagnosticoIngEst
	 */
	public String getDescripcionDiagnosticoIngEst() {
		return descripcionDiagnosticoIngEst;
	}


	/**
	 * @param descripcionDiagnosticoIngEst the descripcionDiagnosticoIngEst to set
	 */
	public void setDescripcionDiagnosticoIngEst(String descripcionDiagnosticoIngEst) {
		this.descripcionDiagnosticoIngEst = descripcionDiagnosticoIngEst;
	}


	/**
	 * @return the consecutivoOrdenAmb
	 */
	public String getConsecutivoOrdenAmb() {
		return consecutivoOrdenAmb;
	}


	/**
	 * @param consecutivoOrdenAmb the consecutivoOrdenAmb to set
	 */
	public void setConsecutivoOrdenAmb(String consecutivoOrdenAmb) {
		this.consecutivoOrdenAmb = consecutivoOrdenAmb;
	}


	/**
	 * @return the codigoSubGrupoArticulo
	 */
	public int getCodigoSubGrupoArticulo() {
		return codigoSubGrupoArticulo;
	}


	/**
	 * @param codigoSubGrupoArticulo the codigoSubGrupoArticulo to set
	 */
	public void setCodigoSubGrupoArticulo(int codigoSubGrupoArticulo) {
		this.codigoSubGrupoArticulo = codigoSubGrupoArticulo;
	}


	/**
	 * @return the codigoClaseInvArticulo
	 */
	public int getCodigoClaseInvArticulo() {
		return codigoClaseInvArticulo;
	}


	/**
	 * @param codigoClaseInvArticulo the codigoClaseInvArticulo to set
	 */
	public void setCodigoClaseInvArticulo(int codigoClaseInvArticulo) {
		this.codigoClaseInvArticulo = codigoClaseInvArticulo;
	}


	/**
	 * @return the nombreClaseInvArticulo
	 */
	public String getNombreClaseInvArticulo() {
		return nombreClaseInvArticulo;
	}


	/**
	 * @param nombreClaseInvArticulo the nombreClaseInvArticulo to set
	 */
	public void setNombreClaseInvArticulo(String nombreClaseInvArticulo) {
		this.nombreClaseInvArticulo = nombreClaseInvArticulo;
	}


	public void setContratoConvenioResponsable(Integer contratoConvenioResponsable) {
		this.contratoConvenioResponsable = contratoConvenioResponsable;
	}


	public Integer getContratoConvenioResponsable() {
		return contratoConvenioResponsable;
	}


	/**
	 * @return the tipoAutorizacion
	 */
	public long getTipoAutorizacion() {
		return tipoAutorizacion;
	}


	/**
	 * @param tipoAutorizacion the tipoAutorizacion to set
	 */
	public void setTipoAutorizacion(long tipoAutorizacion) {
		this.tipoAutorizacion = tipoAutorizacion;
	}


	/**
	 * @return the codigoOrdenSolPet
	 */
	public Long getCodigoOrdenSolPet() {
		return codigoOrdenSolPet;
	}


	/**
	 * @param codigoOrdenSolPet the codigoOrdenSolPet to set
	 */
	public void setCodigoOrdenSolPet(Long codigoOrdenSolPet) {
		this.codigoOrdenSolPet = codigoOrdenSolPet;
	}


	/**
	 * @return the acronimoNaturalezaArticulo
	 */
	public String getAcronimoNaturalezaArticulo() {
		return acronimoNaturalezaArticulo;
	}


	/**
	 * @param acronimoNaturalezaArticulo the acronimoNaturalezaArticulo to set
	 */
	public void setAcronimoNaturalezaArticulo(String acronimoNaturalezaArticulo) {
		this.acronimoNaturalezaArticulo = acronimoNaturalezaArticulo;
	}


	/**
	 * @return the codigoViaIngreso
	 */
	public int getCodigoViaIngreso() {
		return codigoViaIngreso;
	}


	/**
	 * @param codigoViaIngreso the codigoViaIngreso to set
	 */
	public void setCodigoViaIngreso(int codigoViaIngreso) {
		this.codigoViaIngreso = codigoViaIngreso;
	}


	/**
	 * @return the tipoSolicitud
	 */
	public int getTipoSolicitud() {
		return tipoSolicitud;
	}


	/**
	 * @param tipoSolicitud the tipoSolicitud to set
	 */
	public void setTipoSolicitud(int tipoSolicitud) {
		this.tipoSolicitud = tipoSolicitud;
	}


	/**
	 * @return the pyp
	 */
	public boolean isPyp() {
		return pyp;
	}


	/**
	 * @param pyp the pyp to set
	 */
	public void setPyp(boolean pyp) {
		this.pyp = pyp;
	}


}
