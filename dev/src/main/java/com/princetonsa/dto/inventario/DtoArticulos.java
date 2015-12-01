package com.princetonsa.dto.inventario;

import java.io.Serializable;
import java.util.Date;

import util.ConstantesBD;
import util.facturacion.InfoResponsableCobertura;

import com.servinte.axioma.orm.Convenios;

/**
 * Encaps&uacute;la la respuesta a la informaci&oacute;n de 
 * Articulos (Medicamentos e Insumos)
 * @author Diana Carolina G
 *
 */

public class DtoArticulos implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private  Integer codigoArticulo;
	private String descripcionArticulo;
	private int codigoSubGrupoArticulo;
	private String naturalezaArticulo;
	private String unidad_medida;
	private String acronimoUnidadMedida;
	private String nombreNaturaleza;
	private Character esMedicamento;
	private Integer cantidadArticulo;
	private Long diasTratamiento;
	private String dosis;
	private Long dosisXArticuloID;
	private int codigoInstitucion;
	private Long nivelAtencion;
	private String codigoInterfaz;
	
	private Double valorTarifa;
	private Date fechaVigenciaTarifa;

	private String tipoFrecuencia;
	private Integer frecuencia;
	private String via;
	
	private InfoResponsableCobertura responsableCoberturaArticulo;
	private Convenios convenioResponsable;
	//MT6749
	private Date fechaModifica;
	private String horaModifica;
	/**
	 * Atributo que permite indicar si el convenio responsable de la orden cubre los articulos.
	 */
	public String articuloCubierto;
	
	/**
	 *Atributo que permite almacenar el codigo del grupo del articulo 
	 */
	private Integer codGrupoArticulo;
	
	/**
	 *Atributo que permite almacenar la clase del articulo
	 */
	private Integer claseArticulo;
	
	/**
	 * Atributo que permite almacenar el codigo de la naturaleza
	 */
	public Integer codNaturaleza;
	
	
	/**
	 * Constructor
	 */
	public DtoArticulos(){
		
		this.codigoArticulo				= ConstantesBD.codigoNuncaValido;
		this.descripcionArticulo		= "";
		this.codigoSubGrupoArticulo		= ConstantesBD.codigoNuncaValido;
		this.naturalezaArticulo			= "";
		this.unidad_medida				= "";
		this.nombreNaturaleza			= "";
		this.esMedicamento 				= null;
		this.cantidadArticulo			= ConstantesBD.codigoNuncaValido;
		this.diasTratamiento            = ConstantesBD.codigoNuncaValidoLong;
		this.acronimoUnidadMedida		= "";
		this.dosis						= "";
		this.dosisXArticuloID           = ConstantesBD.codigoNuncaValidoLong;
		this.codigoInstitucion          = ConstantesBD.codigoNuncaValido;
		this.nivelAtencion              = ConstantesBD.codigoNuncaValidoLong;
		this.codigoInterfaz 			= "";
		this.valorTarifa				= null;
		this.fechaVigenciaTarifa		= null;
		this.fechaModifica				= null;
		this.horaModifica		= null;
		this.tipoFrecuencia				= "";
		this.frecuencia					= ConstantesBD.codigoNuncaValido;
		this.via						= "";
		this.responsableCoberturaArticulo	= new InfoResponsableCobertura();
		this.convenioResponsable		= new Convenios();
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

	public String getNaturalezaArticulo() {
		return naturalezaArticulo;
	}

	public void setNaturalezaArticulo(String naturalezaArticulo) {
		this.naturalezaArticulo = naturalezaArticulo;
	}

	public void setUnidad_medida(String unidad_medida) {
		this.unidad_medida = unidad_medida;
	}

	public String getUnidad_medida() {
		return unidad_medida;
	}

	public void setEsMedicamento(Character esMedicamento) {
		this.esMedicamento = esMedicamento;
	}

	public Character getEsMedicamento() {
		return esMedicamento;
	}

	public void setNombreNaturaleza(String nombreNaturaleza) {
		this.nombreNaturaleza = nombreNaturaleza;
	}

	public String getNombreNaturaleza() {
		return nombreNaturaleza;
	}

	public void setCantidadArticulo(Integer cantidadArticulo) {
		this.cantidadArticulo = cantidadArticulo;
	}

	public Integer getCantidadArticulo() {
		return cantidadArticulo;
	}

	/**
	 * @return Retorna el atributo diasTratamiento
	 */
	public Long getDiasTratamiento() {
		return diasTratamiento;
	}

	/**
	 * @param diasTratamiento Asigna el atributo diasTratamiento
	 */
	public void setDiasTratamiento(Long diasTratamiento) {
		this.diasTratamiento = diasTratamiento;
	}

	/**
	 * @return Retorna el atributo acronimoUnidadMedida
	 */
	public String getAcronimoUnidadMedida() {
		return acronimoUnidadMedida;
	}

	/**
	 * @param acronimoUnidadMedida Asigna el atributo acronimoUnidadMedida
	 */
	public void setAcronimoUnidadMedida(String acronimoUnidadMedida) {
		this.acronimoUnidadMedida = acronimoUnidadMedida;
	}

	public void setDosis(String dosis) {
		this.dosis = dosis;
	}

	public String getDosis() {
		return dosis;
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

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigoInstitucion
	
	 * @return retorna la variable codigoInstitucion 
	 * @author Angela Maria Aguirre 
	 */
	public int getCodigoInstitucion() {
		return codigoInstitucion;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigoInstitucion
	
	 * @param valor para el atributo codigoInstitucion 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigoInstitucion(int codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo nivelAtencion
	
	 * @return retorna la variable nivelAtencion 
	 * @author Angela Maria Aguirre 
	 */
	public Long getNivelAtencion() {
		return nivelAtencion;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo nivelAtencion
	
	 * @param valor para el atributo nivelAtencion 
	 * @author Angela Maria Aguirre 
	 */
	public void setNivelAtencion(Long nivelAtencion) {
		this.nivelAtencion = nivelAtencion;
	}

	public void setCodigoInterfaz(String codigoInterfaz) {
		this.codigoInterfaz = codigoInterfaz;
	}

	public String getCodigoInterfaz() {
		return codigoInterfaz;
	}

	public void setValorTarifa(Double valorTarifa) {
		this.valorTarifa = valorTarifa;
	}

	public Double getValorTarifa() {
		return valorTarifa;
	}

	public void setFechaVigenciaTarifa(Date fechaVigenciaTarifa) {
		this.fechaVigenciaTarifa = fechaVigenciaTarifa;
	}

	public Date getFechaVigenciaTarifa() {
		return fechaVigenciaTarifa;
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

	
	public void setTipoFrecuencia(String tipoFrecuencia) {
		this.tipoFrecuencia = tipoFrecuencia;
	}

	public String getTipoFrecuencia() {
		return tipoFrecuencia;
	}

	public void setFrecuencia(Integer frecuencia) {
		this.frecuencia = frecuencia;
	}

	public Integer getFrecuencia() {
		return frecuencia;
	}

	public void setVia(String via) {
		this.via = via;
	}

	public String getVia() {
		return via;
	}

	public InfoResponsableCobertura getResponsableCoberturaArticulo() {
		return responsableCoberturaArticulo;
	}

	public void setResponsableCoberturaArticulo(
			InfoResponsableCobertura responsableCoberturaArticulo) {
		this.responsableCoberturaArticulo = responsableCoberturaArticulo;
	}

	public Convenios getConvenioResponsable() {
		return convenioResponsable;
	}

	public void setConvenioResponsable(Convenios convenioResponsable) {
		this.convenioResponsable = convenioResponsable;
	}
	
	public String getArticuloCubierto() {
		return articuloCubierto;
	}

	public void setArticuloCubierto(String articuloCubierto) {
		this.articuloCubierto = articuloCubierto;
	}

	public Integer getCodGrupoArticulo() {
		return codGrupoArticulo;
	}

	public void setCodGrupoArticulo(Integer codGrupoArticulo) {
		this.codGrupoArticulo = codGrupoArticulo;
	}

	public Integer getClaseArticulo() {
		return claseArticulo;
	}

	public void setClaseArticulo(Integer claseArticulo) {
		this.claseArticulo = claseArticulo;
	}
	
	public Integer getCodNaturaleza() {
		return codNaturaleza;
	}

	public void setCodNaturaleza(Integer codNaturaleza) {
		this.codNaturaleza = codNaturaleza;
	}
	
	public Date getFechaModifica() {
		return fechaModifica;
	}

	public void setFechaModifica(Date fechaModifica) {
		this.fechaModifica = fechaModifica;
	}

	public String getHoraModifica() {
		return horaModifica;
	}

	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}

	
	


}
