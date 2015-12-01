package com.princetonsa.dto.odontologia;

import java.io.Serializable;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.UtilidadFecha;

/**
 * @author axioma
*/

public class DtoAliadoOdontologico implements Serializable, Cloneable
{
	/**
	 * atributos
	 */
	
	private double codigoPk;
	private String codigo;
	private int institucion;
	private InfoDatosInt terceros;
	
	private String descripcion;
	private String direccion;
	private String telefono;
	private String observaciones;
	private String estado;
	private String fechaModifica;
	private String horaModifica;
	private String usuarioModifica;
	private String nitTerceroRazSoci;
	private String nombreTerceroRazSoci;
	private boolean puedoModificar;
	
	/**
	 * Contructor
	 */
	
	public DtoAliadoOdontologico(){
		
		clean();
	}
	
	/**
	 * 
	 */
	
	public void clean() {
		this.codigoPk=ConstantesBD.codigoNuncaValidoDouble;
		this.codigo="";
		this.institucion=ConstantesBD.codigoNuncaValido;
		this.setTerceros(new InfoDatosInt());
		
		this.descripcion="";
		this.direccion="";
		this.telefono="";
		this.observaciones="";
		this.estado="";
		this.fechaModifica="";
		this.horaModifica="";
		this.usuarioModifica="";
		this.nitTerceroRazSoci="";
		this.nombreTerceroRazSoci="";
		this.puedoModificar=true;
		
	}

	/**
	 * @return the codigoPk
	 */
	public double getCodigoPk() {
		return codigoPk;
	}

	/**
	 * @param codigoPk the codigoPk to set
	 */
	public void setCodigoPk(double codigoPk) {
		this.codigoPk = codigoPk;
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

	
	public void setTerceros(InfoDatosInt terceros) {
		this.terceros = terceros;
	}
	
	/**
	 * @return the tercero
	 */
	
	public InfoDatosInt getTerceros() {
		return terceros;
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
	 * @return the direccion
	 */
	public String getDireccion() {
		return direccion;
	}

	/**
	 * @param direccion the direccion to set
	 */
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	/**
	 * @return the telefono
	 */
	public String getTelefono() {
		return telefono;
	}

	/**
	 * @param telefono the telefono to set
	 */
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	/**
	 * @return the observaciones
	 */
	public String getObservaciones() {
		return observaciones;
	}

	/**
	 * @param observaciones the observaciones to set
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return the nitTerceroGenPagPac
	 */
	public String getNitTerceroRazSoci() {
		return nitTerceroRazSoci;
	}
	
	public String getNombreTerceroRazSoci() {
		return nombreTerceroRazSoci;
	}

	/**
	 * @return the fechaModifica
	 */
	public String getFechaModifica(){
		return fechaModifica;
	}
	
	/**
	 * @return the fechaModifica
	 */
	public String getFechaModificaFormatoBD() {
		return UtilidadFecha.validarFecha(this.fechaModifica)?UtilidadFecha.conversionFormatoFechaABD(this.fechaModifica):"";
	}
	
	/**
	 * @param fechaModifica the fechaModifica to set
	 */
	public void setFechaModifica(String fechaModifica) {
		this.fechaModifica = fechaModifica;
	}

	/**
	 * @return the horaModifica
	 */
	public String getHoraModifica() {
		return horaModifica;
	}

	/**
	 * @param horaModifica the horaModifica to set
	 */
	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}

	/**
	 * @return the usuarioModifica
	 */
	public String getUsuarioModifica() {
		return usuarioModifica;
	}

	/**
	 * @param usuarioModifica the usuarioModifica to set
	 */
	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

	public Object clone(){
        Object obj=null;
        try{
            obj=super.clone();
        }catch(CloneNotSupportedException ex){
        	Log4JManager.error(" no se puede duplicar");
        }
        return obj;
    }

	/**
	 * @param nitTerceroRazSoci the nitTerceroRazSoci to set
	 */
	public void setNitTerceroRazSoci(String nitTerceroRazSoci) {
		this.nitTerceroRazSoci = nitTerceroRazSoci;
	}

	/**
	 * @param nombreTerceroRazSoci the nombreTerceroRazSoci to set
	 */
	public void setNombreTerceroRazSoci(String nombreTerceroRazSoci) {
		this.nombreTerceroRazSoci = nombreTerceroRazSoci;
	}

	/**
	 * @return the puedoModificar
	 */
	public boolean isPuedoModificar() {
		return puedoModificar;
	}

	/**
	 * @param puedoModificar the puedoModificar to set
	 */
	public void setPuedoModificar(boolean puedoModificar) {
		this.puedoModificar = puedoModificar;
	}

	/**
	 * @return the puedoModificar
	 */
	public boolean getPuedoModificar() {
		return puedoModificar;
	}
	
}