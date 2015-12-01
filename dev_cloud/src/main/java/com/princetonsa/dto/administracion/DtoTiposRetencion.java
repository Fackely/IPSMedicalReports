package com.princetonsa.dto.administracion;

import java.io.Serializable;
import java.util.ArrayList;

import util.ConstantesBD;

/**
 * @author Víctor Hugo Gómez L.
 */

public class DtoTiposRetencion implements Serializable{
	
	private int consecutivo;
	private String codigo;
	private int codigoInstitucion;
	private String descripcion;
	private String sigla;
	private String codigoInterfaz;
	private String activo;
	private String fecha_modifica;
	private String hora_modifica;
	private String usuarioModificacion;
	private String usuarioAnulacion;
	
	//***********************************
	// Dto log
	private DtoLogTipoRetencion dtoLogTipoRetencion = new DtoLogTipoRetencion();
	//***********************************
	
	//***********************************
	// ArrayList de Detalles de Retencion
	/**
	 * ArrayList que contiene el detalle de los tipos de retencion de clase inventario
	 */
	private ArrayList<DtoDetTiposRetencionClaseInv> dtoDetClaseInv = new ArrayList<DtoDetTiposRetencionClaseInv>();
	/**
	 * ArrayList que contiene el detalle de los tipos de retencion de grupo servicio
	 */
	private ArrayList<DtoDetTiposRetencionGrupoSer> dtoDetGrupoSer = new ArrayList<DtoDetTiposRetencionGrupoSer>();
	/**
	 * ArrayList que contiene el detalle de los tipos de retencion de concepto de facturas varias
	 */
	private ArrayList<DtoDetTiposRetencionConceptoFV> dtoDetConceptoFV = new ArrayList<DtoDetTiposRetencionConceptoFV>();
	//***********************************
	
	//*************************
	//Atributos Accion Ejecutar
	private String ingresar;
	private String eliminar;
	private String modificar;
	//*************************
	
	/**
	 * Constructor
	 */
	public DtoTiposRetencion()
	{
		this.reset();
	}
	
	/**
	 * Método que inicia los datos tipos Retencion
	 *
	 */
	private void reset() 
	{
		this.consecutivo = ConstantesBD.codigoNuncaValido;
		this.codigo = "";
		this.codigoInstitucion = ConstantesBD.codigoNuncaValido;
		this.descripcion = "";
		this.sigla = "";
		this.codigoInterfaz = "";
		this.activo = "";
		this.fecha_modifica = "";
		this.hora_modifica = "";
		this.usuarioModificacion = "";
		this.usuarioAnulacion = "";
		this.dtoDetClaseInv = new ArrayList<DtoDetTiposRetencionClaseInv>();
		this.dtoDetGrupoSer = new ArrayList<DtoDetTiposRetencionGrupoSer>();
		this.dtoDetConceptoFV = new ArrayList<DtoDetTiposRetencionConceptoFV>();
		this.dtoLogTipoRetencion = new DtoLogTipoRetencion();
		//Atributos Accion Ejecutar
		this.ingresar = ConstantesBD.acronimoNo;
		this.eliminar = ConstantesBD.acronimoNo;
		this.modificar = ConstantesBD.acronimoNo;
	}

	public void cargarDtoLogTipoRetencion()
	{
		this.dtoLogTipoRetencion.setCodigoTipoRetencion(this.getConsecutivo());
		this.dtoLogTipoRetencion.setCodigo(this.getCodigo());
		this.dtoLogTipoRetencion.setDescripcion(this.getDescripcion());
		this.dtoLogTipoRetencion.setSigla(this.getSigla());
		this.dtoLogTipoRetencion.setCodigoInterfaz(this.getCodigoInterfaz());
		this.dtoLogTipoRetencion.setFechaModifica(this.getFecha_modifica());
		this.dtoLogTipoRetencion.setHoraModifica(this.getHora_modifica());
		this.dtoLogTipoRetencion.setUsuarioModifica(this.getUsuarioModificacion());
	}
	
	/**
	 * @return the consecutivo
	 */
	public int getConsecutivo() {
		return consecutivo;
	}

	/**
	 * @param consecutivo the consecutivo to set
	 */
	public void setConsecutivo(int consecutivo) {
		this.consecutivo = consecutivo;
	}

	/**
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the codigoInstitucion
	 */
	public int getCodigoInstitucion() {
		return codigoInstitucion;
	}

	/**
	 * @param codigoInstitucion the codigoInstitucion to set
	 */
	public void setCodigoInstitucion(int codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
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
	 * @return the sigla
	 */
	public String getSigla() {
		return sigla;
	}

	/**
	 * @param sigla the sigla to set
	 */
	public void setSigla(String sigla) {
		this.sigla = sigla;
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
	 * @return the activo
	 */
	public String getActivo() {
		return activo;
	}

	/**
	 * @param activo the activo to set
	 */
	public void setActivo(String activo) {
		this.activo = activo;
	}

	/**
	 * @return the usuarioModificacion
	 */
	public String getUsuarioModificacion() {
		return usuarioModificacion;
	}

	/**
	 * @param usuarioModificacion the usuarioModificacion to set
	 */
	public void setUsuarioModificacion(String usuarioModificacion) {
		this.usuarioModificacion = usuarioModificacion;
	}

	/**
	 * @return the usuarioAnulacion
	 */
	public String getUsuarioAnulacion() {
		return usuarioAnulacion;
	}

	/**
	 * @param usuarioAnulacion the usuarioAnulacion to set
	 */
	public void setUsuarioAnulacion(String usuarioAnulacion) {
		this.usuarioAnulacion = usuarioAnulacion;
	}

	/**
	 * @return the dtoDetClaseInv
	 */
	public ArrayList<DtoDetTiposRetencionClaseInv> getDtoDetClaseInv() {
		return dtoDetClaseInv;
	}

	/**
	 * @param dtoDetClaseInv the dtoDetClaseInv to set
	 */
	public void setDtoDetClaseInv(
			ArrayList<DtoDetTiposRetencionClaseInv> dtoDetClaseInv) {
		this.dtoDetClaseInv = dtoDetClaseInv;
	}

	/**
	 * @return the dtoDetGrupoSer
	 */
	public ArrayList<DtoDetTiposRetencionGrupoSer> getDtoDetGrupoSer() {
		return dtoDetGrupoSer;
	}

	/**
	 * @param dtoDetGrupoSer the dtoDetGrupoSer to set
	 */
	public void setDtoDetGrupoSer(
			ArrayList<DtoDetTiposRetencionGrupoSer> dtoDetGrupoSer) {
		this.dtoDetGrupoSer = dtoDetGrupoSer;
	}

	/**
	 * @return the dtoDetConceptoFV
	 */
	public ArrayList<DtoDetTiposRetencionConceptoFV> getDtoDetConceptoFV() {
		return dtoDetConceptoFV;
	}

	/**
	 * @param dtoDetConceptoFV the dtoDetConceptoFV to set
	 */
	public void setDtoDetConceptoFV(
			ArrayList<DtoDetTiposRetencionConceptoFV> dtoDetConceptoFV) {
		this.dtoDetConceptoFV = dtoDetConceptoFV;
	}

	/**
	 * @return the ingresar
	 */
	public String getIngresar() {
		return ingresar;
	}

	/**
	 * @param ingresar the ingresar to set
	 */
	public void setIngresar(String ingresar) {
		this.ingresar = ingresar;
	}

	/**
	 * @return the eliminar
	 */
	public String getEliminar() {
		return eliminar;
	}

	/**
	 * @param eliminar the eliminar to set
	 */
	public void setEliminar(String eliminar) {
		this.eliminar = eliminar;
	}

	/**
	 * @return the modificar
	 */
	public String getModificar() {
		return modificar;
	}

	/**
	 * @param modificar the modificar to set
	 */
	public void setModificar(String modificar) {
		this.modificar = modificar;
	}

	/**
	 * @return the dtoLogTipoRetencion
	 */
	public DtoLogTipoRetencion getDtoLogTipoRetencion() {
		return dtoLogTipoRetencion;
	}

	/**
	 * @param dtoLogTipoRetencion the dtoLogTipoRetencion to set
	 */
	public void setDtoLogTipoRetencion(DtoLogTipoRetencion dtoLogTipoRetencion) {
		this.dtoLogTipoRetencion = dtoLogTipoRetencion;
	}

	/**
	 * @return the fecha_modifica
	 */
	public String getFecha_modifica() {
		return fecha_modifica;
	}

	/**
	 * @param fecha_modifica the fecha_modifica to set
	 */
	public void setFecha_modifica(String fecha_modifica) {
		this.fecha_modifica = fecha_modifica;
	}

	/**
	 * @return the hora_modifica
	 */
	public String getHora_modifica() {
		return hora_modifica;
	}

	/**
	 * @param hora_modifica the hora_modifica to set
	 */
	public void setHora_modifica(String hora_modifica) {
		this.hora_modifica = hora_modifica;
	}
	
}
