package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.commons.logging.impl.Log4JLogger;
import org.apache.log4j.lf5.Log4JLogRecord;
import org.axioma.util.log.Log4JManager;

import com.princetonsa.dto.interfaz.DtoCuentaContable;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.InfoDatosStr;
import util.UtilidadFecha;
import util.UtilidadTexto;

public class DtoDetalleServicioHonorarios implements Serializable , Cloneable{

	private double codigo;
	private double codigoHonorario;
	private InfoDatosInt especialidad;
	private String fechaModifica;
	private String horaModifica;
	private double porcentajeParticipacion; 
	private InfoDatosInt servicio;
	private String usuarioModifica;
	private double valorParticipacion;
	private double tarifa;
	private String fechaVigenciaTarifa;
	private ArrayList<DtoCuentaContable> arrayCuentasContables = new  ArrayList<DtoCuentaContable>(); 
	
	
	
	public DtoDetalleServicioHonorarios()
	{
       this.reset();
	}
	
public void reset(){
	 
	this.codigo = ConstantesBD.codigoNuncaValidoDouble;
     this.codigoHonorario = ConstantesBD.codigoNuncaValidoDouble;
     this.especialidad=  new InfoDatosInt();
     this.fechaModifica = "";
     this.servicio = new InfoDatosInt();
     this.horaModifica = "";
     this.porcentajeParticipacion = ConstantesBD.codigoNuncaValido;     
     this.valorParticipacion = ConstantesBD.codigoNuncaValido;
     this.tarifa= ConstantesBD.codigoNuncaValidoDouble;
     this.fechaVigenciaTarifa="";
     this.usuarioModifica = "";
     this.arrayCuentasContables = new ArrayList<DtoCuentaContable>();
     this.arrayCuentasContables.add(new DtoCuentaContable());
     this.arrayCuentasContables.add(new DtoCuentaContable());
     this.arrayCuentasContables.add(new DtoCuentaContable());
  }

	/**
	 * @return the codigo
	 */
	public double getCodigo() {
		return this.codigo;
	}

	/**
	 * @return the codigoHonorario
	 */
	public double getCodigoHonorario() {
		return this.codigoHonorario;
	}

	

	/**
	 * @return the fechaModifica
	 */
	public String getFechaModifica() {
		return this.fechaModifica;
	}
	/**
	 * @return the fechaModifica
	 */
	public String getFechaModificaFromatoBD() {
		return UtilidadFecha.validarFecha(this.fechaModifica) ? UtilidadFecha
				.conversionFormatoFechaABD(this.fechaModifica) : "";
	}
	/**
	 * @return the horaModifica
	 */
	public String getHoraModifica() {
		return this.horaModifica;
	}

	/**
	 * @return the porcentajeParticipacion
	 */
	public double getPorcentajeParticipacion() {
		return this.porcentajeParticipacion;
	}

	

	/**
	 * @return the usuarioModifica
	 */
	public String getUsuarioModifica() {
		return this.usuarioModifica;
	}

	/**
	 * @return the valorParticipacion
	 */
	public double getValorParticipacion() {
		return this.valorParticipacion;
	}

	/**
	 * @param codigo
	 *            the codigo to set
	 */
	public void setCodigo(double codigo) {
		this.codigo = codigo;
	}

	/**
	 * @param codigoHonorario
	 *            the codigoHonorario to set
	 */
	public void setCodigoHonorario(double codigoHonorario) {
		this.codigoHonorario = codigoHonorario;
	}

	

	/**
	 * @param fechaModifica
	 *            the fechaModifica to set
	 */
	public void setFechaModifica(String fechaModifica) {
		this.fechaModifica = fechaModifica;
	}

	/**
	 * @param horaModifica
	 *            the horaModifica to set
	 */
	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}

	/**
	 * @param porcentajeParticipacion
	 *            the porcentajeParticipacion to set
	 */
	public void setPorcentajeParticipacion(double porcentajeParticipacion) {
		this.porcentajeParticipacion = porcentajeParticipacion;
	}

	

	/**
	 * @param usuarioModifica
	 *            the usuarioModifica to set
	 */
	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

	/**
	 * @param valorParticipacion
	 *            the valorParticipacion to set
	 */
	public void setValorParticipacion(double valorParticipacion) {
		this.valorParticipacion = valorParticipacion;
	}

	/**
	 * @return the especialidad
	 */
	public InfoDatosInt getEspecialidad() {
		return especialidad;
	}

	/**
	 * @param especialidad the especialidad to set
	 */
	public void setEspecialidad(InfoDatosInt especialidad) {
		this.especialidad = especialidad;
	}

	/**
	 * @return the servicio
	 */
	public InfoDatosInt getServicio() {
		return servicio;
	}

	/**
	 * @param servicio the servicio to set
	 */
	public void setServicio(InfoDatosInt servicio) {
		this.servicio = servicio;
	}

	/**
	 * @return the arrayCuentasContables
	 */
	public ArrayList<DtoCuentaContable> getArrayCuentasContables() {
		return arrayCuentasContables;
	}

	/**
	 * @param arrayCuentasContables the arrayCuentasContables to set
	 */
	public void setArrayCuentasContables(
			ArrayList<DtoCuentaContable> arrayCuentasContables) {
		this.arrayCuentasContables = arrayCuentasContables;
	}

	/**
	 * @return the tarifa
	 */
	public double getTarifa() {
		return tarifa;
	}

	/**
	 * @param tarifa the tarifa to set
	 */
	public void setTarifa(double tarifa) {
		this.tarifa = tarifa;
	}

	/**
	 * @return the tarifa
	 */
	public String getTarifaFormateado() {
		return UtilidadTexto.formatearValores(tarifa);
	}
	
	/**
	 * 
	 * 
	 */
	public String getValorParticipacionFormateado() {
		return UtilidadTexto.formatearValores(valorParticipacion);
	}

	
	
	/**
	 * CLONADOR SERVICO HONORARIO
	 */
	public DtoDetalleServicioHonorarios clone(){
		DtoDetalleServicioHonorarios obj=null;
        try{
            obj= (DtoDetalleServicioHonorarios)super.clone();	            
            obj.setArrayCuentasContables((ArrayList<DtoCuentaContable>)this.arrayCuentasContables.clone());
            obj.setServicio((InfoDatosInt)this.servicio.clone());
            obj.setEspecialidad((InfoDatosInt)this.especialidad.clone());
        }
        catch(CloneNotSupportedException ex)
        {
           Log4JManager.info("Error Clonar DtoDetalleServicioHonorarios");
        }
        return obj;
    }
	

	/**
	 * @return the fechaVigenciaTarifa
	 */
	public String getFechaVigenciaTarifa() {
		return fechaVigenciaTarifa;
	}

	/**
	 * @param fechaVigenciaTarifa the fechaVigenciaTarifa to set
	 */
	public void setFechaVigenciaTarifa(String fechaVigenciaTarifa) {
		this.fechaVigenciaTarifa = fechaVigenciaTarifa;
	}
	
	
}
