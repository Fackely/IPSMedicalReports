package com.servinte.axioma.dto.ordenes;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.servinte.axioma.common.ErrorMessage;
import com.servinte.axioma.dto.capitacion.NivelAutorizacionDto;

import util.ConstantesBD;

/**
 * Dto Necesario para mapear los atributos de los Medicamentos/Insumos
 * a Autorizar
 * 
 * @author ricruico
 * @version 1.0
 * @created 20-jun-2012 02:24:01 p.m.
 */
public class MedicamentoInsumoAutorizacionOrdenDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -262516075203383590L;

	/**
	 * Atributo que representa el codigo del Articulo
	 */
	private int codigo;
	
	/**
	 * Atributo que representa el codigo de la interfaz Articulo
	 */
	private String codigoInterfaz;
	
	/**
	 * Atributo que representa el codigo propietario del articulo o codigo a mostrar
	 */
	private String codigoPropietario;
	
	/**
	 * Atributo que representa la descripción Articulo
	 */
	private String descripcion;
	
	/**
	 * Atributo que representa la forma farmaceutica del Articulo
	 */
	private String formaFarmaceutica;
	
	/**
	 * Atributo que representa la concentración del Articulo
	 */
	private String concentracion;
	
	/**
	 * Atributo que representa la unidad de medida del Articulo
	 */
	private String unidadMedida;
	
	/**
	 * Atributo que representa el codigo del grupo de Inventario al cual pertenece Articulo
	 */
	private Integer grupoInventario;
	
	/**
	 * Atributo que representa el codigo del subgrupo de Inventario al cual pertenece Articulo
	 */
	private Integer subGrupoInventario;
	
	/**
	 * Atributo que representa el acronimo de la naturaleza a la cual pertenece el Articulo
	 */
	private String acronimoNaturaleza;
	
	/**
	 * Atributo que representa el nombre de la naturaleza a la cual pertenece el Articulo
	 */
	private String nombreNaturaleza;
	
	/**
	 * Atributo que representa si el Articulo es un Medicamento o un Insumo
	 */
	private char esMedicamento;
	
	/**
	 * Atributo que representa el codigoPk del Nivel de Atención al cual pertenece el Servicio
	 */
	private Long consecutivoNivelAtencion;
	
	/**
	 * Atributo que representa la descripción del Nivel de Atención al cual pertenece el Servicio
	 */
	private String descripcionNivelAtencion;
	
	/**
	 * Atributo que almacena el valor de la tarifa del medicamentoInsumo
	 */
	private BigDecimal valorTarifa;

	/**
	 * Atributo para almacenar el mensaje del fallo autorización para el servicio
	 */
	private ErrorMessage mensajeError;

	/**
	 * Atributo para almacenar el indicativo que permite identificar si el servicio se debe autorizar
	 */
	private boolean autorizar;
	
	/**
	 * Atributo para almacenar
	 */
	private boolean autorizado;
	
	/**
	 * Atributo que almacena la cantidad de Medicamento/Insumo que se van a autorizar
	 */
	private int cantidadSolicitada;	
	
	/**
	 * Atributo que almacena la clase de inventario para le Medicamento/Insumo
	 */
	private Integer claseInventario;
	
	/**
	 * Atributo que almacena el nombre de la clase de inventario para le Medicamento/Insumo
	 */
	private String nombreClaseInventario;
	
	/**
	 * Atributo que almacena la dosis del medicamento
	 */
	private String dosis;
	
	/**
	 * Atributo que almacena la frecuencia del medicamento
	 */
	private Long frecuencia;
	
	/**
	 * Atributo que almacena los días de tratamiento para el medicamento
	 */
	private Long diasTratamiento;
	
	/**
	 * Atributo que almacena la vía de administración del medicamento
	 */
	private String viaAdministracion;
	
	/**
	 * Atributo que almacena el tipo de frecuencia del medicamento
	 */
	private String tipoFrecuencia;
	
	/**
	 * Atributo que almacena los niveles de autorización que tiene el servicio
	 */
	private List<NivelAutorizacionDto> nivelesAutorizacion;
	
	/**
	 * Atributo que almacena el nivel de autorización para el servicio
	 */
	private NivelAutorizacionDto nivelAutorizacion;
	
	/**
	 * Atributo que almacena el acronimo del diagnostico asociado a la orden
	 */
	private String acronimoDx;
	
	/**
	 * Atributo que almacena el tipo Cie del diagnostico asociado a la orden
	 */
	private Integer tipoCieDx;
	
	/**
	 * Atributo que almacena el nombre del diagnostico asociado a la orden
	 */
	private String nombreDx;
	
	/**
	 * Atributo que representa la cantidad de medicamento ordenado
	 */
	private Long cantidad;
	
	/**
	 * Atributo que almacena si se puede autorizar el medicamento o insumo dado que tiene nivel de autorizacion
	 */
	private boolean puedeAutorizar;
	
	/**
	 * Atributo que almacena si se puede autorizar el medicamento o insumo
	 */
	private boolean validoAutorizar;
	
	/**
	 * Atributo que representa el contrato capitado que cubre el medicamento
	 */
	private Integer codigoContrato;
	
	/**
	 * Atributo que representa si el articulo esta cubierto
	 */
	private boolean cubierto;
	
	public MedicamentoInsumoAutorizacionOrdenDto(){
		
	}
	
	/**Constructor necesario para mapear los datos de la consulta
	 * de Medicamentos/insumos pendientes por Autorizar para Ordenes Ambulatorias
	 * 
	 * @param codigo
	 * @param codigoInterfaz
	 * @param descripcion
	 * @param formaFarmaceutica
	 * @param concentracion
	 * @param unidadMedida
	 * @param subGrupoInventario
	 * @param acronimoNaturaleza
	 * @param esMedicamento
	 */
	public MedicamentoInsumoAutorizacionOrdenDto(int codigo,
			String codigoInterfaz, String descripcion,
			String formaFarmaceutica, String concentracion,
			String unidadMedida, Integer subGrupoInventario,
			String acronimoNaturaleza, String nombreNaturaleza, char esMedicamento,
			Long consecutivoNivelAtencion, String descripcionNivelAtencion,
			String acronimoDx, Integer tipoCieDx, String nombreDx, Long cantidad,
			String dosis, Long frecuencia, String tipoFrecuencia, String via,
			Integer diasTratamiento) {
		this.codigo = codigo;
		this.codigoInterfaz = codigoInterfaz;
		this.descripcion = descripcion;
		this.formaFarmaceutica = formaFarmaceutica;
		this.concentracion = concentracion;
		this.unidadMedida = unidadMedida;
		if(subGrupoInventario == null){
			this.subGrupoInventario = ConstantesBD.codigoNuncaValido;
		}
		else{
			this.subGrupoInventario = subGrupoInventario;
		}
		this.acronimoNaturaleza = acronimoNaturaleza;
		this.nombreNaturaleza= nombreNaturaleza;
		this.esMedicamento = esMedicamento;
		this.consecutivoNivelAtencion=consecutivoNivelAtencion;
		this.descripcionNivelAtencion=descripcionNivelAtencion;
		this.acronimoDx=acronimoDx;
		this.tipoCieDx=tipoCieDx;
		this.nombreDx=nombreDx;
		this.cantidad=cantidad;
		this.dosis=dosis;
		this.frecuencia=frecuencia;
		this.tipoFrecuencia=tipoFrecuencia;
		this.viaAdministracion=via;
		if(diasTratamiento != null){
			this.diasTratamiento=diasTratamiento.longValue();
		}
	}
	
	/**Constructor necesario para mapear los datos de la consulta
	 * de Medicamentos/insumos pendientes por Autorizar para Ordenes Médicas
	 * 
	 * @param codigo
	 * @param codigoInterfaz
	 * @param descripcion
	 * @param formaFarmaceutica
	 * @param concentracion
	 * @param unidadMedida
	 * @param subGrupoInventario
	 * @param acronimoNaturaleza
	 * @param esMedicamento
	 */
	public MedicamentoInsumoAutorizacionOrdenDto(int codigo,
			String codigoInterfaz, String descripcion,
			String formaFarmaceutica, String concentracion,
			String unidadMedida, Integer subGrupoInventario,
			String acronimoNaturaleza, String nombreNaturaleza, char esMedicamento,
			Long consecutivoNivelAtencion, String descripcionNivelAtencion,
			String acronimoDx, Integer tipoCieDx, String nombreDx, Integer cantidad,
			String dosis, Integer frecuencia, String tipoFrecuencia, String via,
			Long diasTratamiento) {
		this.codigo = codigo;
		this.codigoInterfaz = codigoInterfaz;
		this.descripcion = descripcion;
		this.formaFarmaceutica = formaFarmaceutica;
		this.concentracion = concentracion;
		this.unidadMedida = unidadMedida;
		if(subGrupoInventario == null){
			this.subGrupoInventario = ConstantesBD.codigoNuncaValido;
		}
		else{
			this.subGrupoInventario = subGrupoInventario;
		}
		this.acronimoNaturaleza = acronimoNaturaleza;
		this.nombreNaturaleza= nombreNaturaleza;
		this.esMedicamento = esMedicamento;
		this.consecutivoNivelAtencion=consecutivoNivelAtencion;
		this.descripcionNivelAtencion=descripcionNivelAtencion;
		this.acronimoDx=acronimoDx;
		this.tipoCieDx=tipoCieDx;
		this.nombreDx=nombreDx;
		if(cantidad != null){
			this.cantidad=cantidad.longValue();
		}
		else{
			this.cantidad=0L;
		}
		this.dosis=dosis;
		if(frecuencia != null){
			this.frecuencia=frecuencia.longValue();
		}
		this.tipoFrecuencia=tipoFrecuencia;
		this.viaAdministracion=via;
		this.diasTratamiento=diasTratamiento;
	}

	
	/**Constructor necesario para mapear los datos de la consulta
	 * de Medicamentos/insumos pendientes por Autorizar para Ordenes Ambulatorias
	 * 
	 * @param codigo
	 * @param codigoInterfaz
	 * @param descripcion
	 * @param formaFarmaceutica
	 * @param concentracion
	 * @param unidadMedida
	 * @param subGrupoInventario
	 * @param acronimoNaturaleza
	 * @param esMedicamento
	 */
	public MedicamentoInsumoAutorizacionOrdenDto(int codigo,
			Integer codigoContrato, Long consecutivoNivelAtencion, Integer subGrupoInventario,
			BigDecimal valorTarifa, Integer cantidadAutorizada) {
		this.codigo = codigo;
		this.codigoContrato = codigoContrato;
		this.consecutivoNivelAtencion = consecutivoNivelAtencion;
		this.subGrupoInventario = subGrupoInventario;
		this.valorTarifa = valorTarifa;
		if(cantidadAutorizada != null){
			this.cantidad = cantidadAutorizada.longValue();
		}
		else{
			this.cantidad = null;
		}
	}
	
	/**
	 * Constructor necesario para mapear los datos de la consulta
	 * de Medicamentos/insumos pendientes por Autorizar para Ordenes Médicas
	 * @param codigo
	 * @param codigoInterfaz
	 * @param descripcion
	 * @param formaFarmaceutica
	 * @param concentracion
	 * @param unidadMedida
	 * @param subGrupoInventario
	 * @param acronimoNaturaleza
	 * @param nombreNaturaleza
	 * @param esMedicamento
	 * @param consecutivoNivelAtencion
	 * @param descripcionNivelAtencion
	 * @param acronimoDx
	 * @param tipoCieDx
	 * @param nombreDx
	 * @param cantidad
	 * @param dosis
	 * @param frecuencia
	 * @param tipoFrecuencia
	 * @param via
	 * @param diasTratamiento
	 * @param cubierto
	 */
	public MedicamentoInsumoAutorizacionOrdenDto(int codigo,
			String codigoInterfaz, String descripcion,
			String formaFarmaceutica, String concentracion,
			String unidadMedida, Integer subGrupoInventario,
			String acronimoNaturaleza, String nombreNaturaleza, char esMedicamento,
			Long consecutivoNivelAtencion, String descripcionNivelAtencion,
			String acronimoDx, Integer tipoCieDx, String nombreDx, Integer cantidad,
			String dosis, Integer frecuencia, String tipoFrecuencia, String via,
			Long diasTratamiento, String cubierto) {
		this.codigo = codigo;
		this.codigoInterfaz = codigoInterfaz;
		this.descripcion = descripcion;
		this.formaFarmaceutica = formaFarmaceutica;
		this.concentracion = concentracion;
		this.unidadMedida = unidadMedida;
		if(subGrupoInventario == null){
			this.subGrupoInventario = ConstantesBD.codigoNuncaValido;
		}
		else{
			this.subGrupoInventario = subGrupoInventario;
		}
		this.acronimoNaturaleza = acronimoNaturaleza;
		this.nombreNaturaleza= nombreNaturaleza;
		this.esMedicamento = esMedicamento;
		this.consecutivoNivelAtencion=consecutivoNivelAtencion;
		this.descripcionNivelAtencion=descripcionNivelAtencion;
		this.acronimoDx=acronimoDx;
		this.tipoCieDx=tipoCieDx;
		this.nombreDx=nombreDx;
		if(cantidad != null){
			this.cantidad=cantidad.longValue();
		}
		else{
			this.cantidad=0L;
		}
		this.dosis=dosis;
		if(frecuencia != null){
			this.frecuencia=frecuencia.longValue();
		}
		this.tipoFrecuencia=tipoFrecuencia;
		this.viaAdministracion=via;
		this.diasTratamiento=diasTratamiento;
		
		if(cubierto != null && cubierto.equals(ConstantesBD.acronimoSi)){
			this.cubierto=true;
		}
		else{
			this.cubierto=false;	
		}
		
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
	 * @return the codigoInterfaz
	 */
	public String getCodigoInterfaz() {
		return codigoInterfaz;
	}

	/**
	 * @param codigoInterfaz the codigoInterfaz to set
	 */
	public void setCodigoInterfaz(String codigoInterfaz) {
		this.codigoInterfaz = codigoInterfaz;
	}

	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * @return the formaFarmaceutica
	 */
	public String getFormaFarmaceutica() {
		return formaFarmaceutica;
	}

	/**
	 * @param formaFarmaceutica the formaFarmaceutica to set
	 */
	public void setFormaFarmaceutica(String formaFarmaceutica) {
		this.formaFarmaceutica = formaFarmaceutica;
	}

	/**
	 * @return the concentracion
	 */
	public String getConcentracion() {
		return concentracion;
	}

	/**
	 * @param concentracion the concentracion to set
	 */
	public void setConcentracion(String concentracion) {
		this.concentracion = concentracion;
	}

	/**
	 * @return the unidadMedida
	 */
	public String getUnidadMedida() {
		return unidadMedida;
	}

	/**
	 * @param unidadMedida the unidadMedida to set
	 */
	public void setUnidadMedida(String unidadMedida) {
		this.unidadMedida = unidadMedida;
	}

	public Integer getGrupoInventario() {
		return grupoInventario;
	}

	public void setGrupoInventario(Integer grupoInventario) {
		this.grupoInventario = grupoInventario;
	}

	/**
	 * @return the subGrupoInventario
	 */
	public Integer getSubGrupoInventario() {
		return subGrupoInventario;
	}

	/**
	 * @param subGrupoInventario the subGrupoInventario to set
	 */
	public void setSubGrupoInventario(Integer subGrupoInventario) {
		this.subGrupoInventario = subGrupoInventario;
	}

	/**
	 * @return the acronimoNaturaleza
	 */
	public String getAcronimoNaturaleza() {
		return acronimoNaturaleza;
	}

	/**
	 * @param acronimoNaturaleza the acronimoNaturaleza to set
	 */
	public void setAcronimoNaturaleza(String acronimoNaturaleza) {
		this.acronimoNaturaleza = acronimoNaturaleza;
	}

	/**
	 * @return the esMedicamento
	 */
	public char getEsMedicamento() {
		return esMedicamento;
	}

	/**
	 * @param esMedicamento the esMedicamento to set
	 */
	public void setEsMedicamento(char esMedicamento) {
		this.esMedicamento = esMedicamento;
	}

	/**
	 * @return the consecutivoNivelAtencion
	 */
	public Long getConsecutivoNivelAtencion() {
		return consecutivoNivelAtencion;
	}

	/**
	 * @param consecutivoNivelAtencion the consecutivoNivelAtencion to set
	 */
	public void setConsecutivoNivelAtencion(Long consecutivoNivelAtencion) {
		this.consecutivoNivelAtencion = consecutivoNivelAtencion;
	}

	/**
	 * @return the descripcionNivelAtencion
	 */
	public String getDescripcionNivelAtencion() {
		return descripcionNivelAtencion;
	}

	/**
	 * @param descripcionNivelAtencion the descripcionNivelAtencion to set
	 */
	public void setDescripcionNivelAtencion(String descripcionNivelAtencion) {
		this.descripcionNivelAtencion = descripcionNivelAtencion;
	}

	/**
	 * @return the codigoPropietario
	 */
	public String getCodigoPropietario() {
		return codigoPropietario;
	}

	/**
	 * @param codigoPropietario the codigoPropietario to set
	 */
	public void setCodigoPropietario(String codigoPropietario) {
		this.codigoPropietario = codigoPropietario;
	}
	
	/**
	 * @return valorTarifa
	 */
	public BigDecimal getValorTarifa() {
		return valorTarifa;
	}
	
	/**
	 * @param valorTarifa
	 */
	public void setValorTarifa(BigDecimal valorTarifa) {
		this.valorTarifa = valorTarifa;
	}
	
	/**
	 * @return mensajeError
	 */
	public ErrorMessage getMensajeError() {
		return mensajeError;
	}
	
	/**
	 * @param mensajeError 
	 */
	public void setMensajeError(ErrorMessage mensajeError) {
		this.mensajeError = mensajeError;
	}
	
	/**
	 * @return autorizar
	 */
	public boolean isAutorizar() {
		return autorizar;
	}
	
	/**
	 * @param autorizar
	 */
	public void setAutorizar(boolean autorizar) {
		this.autorizar = autorizar;
	}
	
	/**
	 * @return autorizado
	 */
	public boolean isAutorizado() {
		return autorizado;
	}
	
	/**
	 * @param autorizado
	 */
	public void setAutorizado(boolean autorizado) {
		this.autorizado = autorizado;
	}
	
	/**
	 * @return cantidadSolicitada
	 */
	public int getCantidadSolicitada() {
		return cantidadSolicitada;
	}
	
	/**
	 * @param cantidadSolicitada
	 */
	public void setCantidadSolicitada(int cantidadSolicitada) {
		this.cantidadSolicitada = cantidadSolicitada;
	}
	
	/**
	 * @return claseInventario
	 */
	public Integer getClaseInventario() {
		return claseInventario;
	}
	
	/**
	 * @param claseInventario
	 */
	public void setClaseInventario(Integer claseInventario) {
		this.claseInventario = claseInventario;
	}
	
	/**
	 * @return nombreClaseInventario
	 */
	public String getNombreClaseInventario() {
		return nombreClaseInventario;
	}
	
	/**
	 * @param nombreClaseInventario
	 */
	public void setNombreClaseInventario(String nombreClaseInventario) {
		this.nombreClaseInventario = nombreClaseInventario;
	}
	
	/**
	 * @return dosis
	 */
	public String getDosis() {
		return dosis;
	}
	
	/**
	 * @param dosis
	 */
	public void setDosis(String dosis) {
		this.dosis = dosis;
	}
	
	/**
	 * @return frecuencia
	 */
	public Long getFrecuencia() {
		return frecuencia;
	}
	
	/**
	 * @param frecuencia
	 */
	public void setFrecuencia(Long frecuencia) {
		this.frecuencia = frecuencia;
	}
	
	/**
	 * @return diasTratamiento
	 */
	public Long getDiasTratamiento() {
		return diasTratamiento;
	}
	
	/**
	 * @param diasTratamiento
	 */
	public void setDiasTratamiento(Long diasTratamiento) {
		this.diasTratamiento = diasTratamiento;
	}
	
	/**
	 * @return viaAdministracion
	 */
	public String getViaAdministracion() {
		return viaAdministracion;
	}
	
	/**
	 * @param viaAdministracion
	 */
	public void setViaAdministracion(String viaAdministracion) {
		this.viaAdministracion = viaAdministracion;
	}
	
	/**
	 * @return tipoFrecuencia
	 */
	public String getTipoFrecuencia() {
		return tipoFrecuencia;
	}
	
	/**
	 * @param tipoFrecuencia
	 */
	public void setTipoFrecuencia(String tipoFrecuencia) {
		this.tipoFrecuencia = tipoFrecuencia;
	}
	
	/**
	 * @return the nivelesAutorizacion
	 */
	public List<NivelAutorizacionDto> getNivelesAutorizacion() {
		return nivelesAutorizacion;
	}

	/**
	 * @param nivelesAutorizacion the nivelesAutorizacion to set
	 */
	public void setNivelesAutorizacion(List<NivelAutorizacionDto> nivelesAutorizacion) {
		this.nivelesAutorizacion = nivelesAutorizacion;
	}

	/**
	 * @return nivelAutorizacion
	 */
	public NivelAutorizacionDto getNivelAutorizacion() {
		return nivelAutorizacion;
	}
	
	/**
	 * @param nivelAutorizacion
	 */
	public void setNivelAutorizacion(NivelAutorizacionDto nivelAutorizacion) {
		this.nivelAutorizacion = nivelAutorizacion;
	}

	/**
	 * @return the acronimoDx
	 */
	public String getAcronimoDx() {
		return acronimoDx;
	}

	/**
	 * @param acronimoDx the acronimoDx to set
	 */
	public void setAcronimoDx(String acronimoDx) {
		this.acronimoDx = acronimoDx;
	}

	/**
	 * @return the tipoCieDx
	 */
	public Integer getTipoCieDx() {
		return tipoCieDx;
	}

	/**
	 * @param tipoCieDx the tipoCieDx to set
	 */
	public void setTipoCieDx(Integer tipoCieDx) {
		this.tipoCieDx = tipoCieDx;
	}

	/**
	 * @return the nombreDx
	 */
	public String getNombreDx() {
		return nombreDx;
	}

	/**
	 * @param nombreDx the nombreDx to set
	 */
	public void setNombreDx(String nombreDx) {
		this.nombreDx = nombreDx;
	}

	/**
	 * @return the cantidad
	 */
	public Long getCantidad() {
		return cantidad;
	}

	/**
	 * @param cantidad the cantidad to set
	 */
	public void setCantidad(Long cantidad) {
		this.cantidad = cantidad;
	}

	/**
	 * @return the puedeAutorizar
	 */
	public boolean isPuedeAutorizar() {
		return puedeAutorizar;
	}

	/**
	 * @param puedeAutorizar the puedeAutorizar to set
	 */
	public void setPuedeAutorizar(boolean puedeAutorizar) {
		this.puedeAutorizar = puedeAutorizar;
	}

	/**
	 * @return the nombreNaturaleza
	 */
	public String getNombreNaturaleza() {
		return nombreNaturaleza;
	}

	/**
	 * @param nombreNaturaleza the nombreNaturaleza to set
	 */
	public void setNombreNaturaleza(String nombreNaturaleza) {
		this.nombreNaturaleza = nombreNaturaleza;
	}

	/**
	 * @return the validoAutorizar
	 */
	public boolean isValidoAutorizar() {
		return validoAutorizar;
	}

	/**
	 * @param validoAutorizar the validoAutorizar to set
	 */
	public void setValidoAutorizar(boolean validoAutorizar) {
		this.validoAutorizar = validoAutorizar;
	}

	/**
	 * @return the codigoContrato
	 */
	public Integer getCodigoContrato() {
		return codigoContrato;
	}

	/**
	 * @param codigoContrato the codigoContrato to set
	 */
	public void setCodigoContrato(Integer codigoContrato) {
		this.codigoContrato = codigoContrato;
	}

	/**
	 * @return the cubierto
	 */
	public boolean isCubierto() {
		return cubierto;
	}

	/**
	 * @param cubierto the cubierto to set
	 */
	public void setCubierto(boolean cubierto) {
		this.cubierto = cubierto;
	}

}