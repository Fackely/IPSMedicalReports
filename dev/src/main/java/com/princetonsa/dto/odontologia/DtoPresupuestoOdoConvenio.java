package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

import com.princetonsa.enu.general.EnumTipoModificacion;
import com.servinte.axioma.fwk.exception.IPSException;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.UtilidadTexto;
import util.UtilidadValidacion;


/**
 * 
 * @author axioma
 *
 */
@SuppressWarnings("serial")
public class DtoPresupuestoOdoConvenio implements Serializable {

	/**
	 * 
	 */
	private BigDecimal codigoPK;
	
	/**
	 * 
	 */
	private BigDecimal presupuestoOdoProgServ;
	
	/**
	 * 	
	 */
	private  BigDecimal valorUnitario;
	
	/**
	 * 
	 */
	private  InfoDatosInt convenio;
	
	/**
	 * 
	 */
	private DtoInfoFechaUsuario usuarioModifica;
	
	/**
	 * 
	 */
	private String contratado;
	
	/**
	 * 
	 */
	private BigDecimal descuentoComercialUnitario;
	
	/**
	 * 
	 */
	private String errorCalculoTarifa;
	
	/**
	 * 
	 */
	private double detallePromocion;
	
	/**
	 * 
	 */
	private BigDecimal valorDescuentoPromocion;
	
	/**
	 * 
	 */
	private double porcentajePromocion;
	
	/**
	 * 
	 */
	private String advertenciaPromocion;
	
	/**
	 * 
	 */
	private Double porcentajeHonorarioPromocion;
	
	/**
	 * 
	 */
	private BigDecimal valorHonorarioPromocion;
	
	/**
	 * 
	 * 
	 */
	
	private BigDecimal serialBono;
	
	/**
	 * 
	 * 
	 * 
	 */
	private BigDecimal valorDescuentoBono;
	
	/***
	 * 
	 * 
	 * 
	 */
	private String advertenciaBono ;
	
	/**
	 * 
	 */
	private double porcentajeDctoBono;
	
	/**
	 * Bono del paciente, estricta relación al campo codigo_pk de la tabla bonos_conv_ing_pac
	 */
	private int bonoPaciente;
	
	/**
	 * 
	 */
	private InfoDatosInt contrato;
	
	/**
	 * 
	 */
	private boolean seleccionadoBono;
	
	/**
	 * 
	 */
	private boolean seleccionadoPromocion;
	
	
	/**
	 * 
	 * 
	 * 
	 */
	private ArrayList<DtoPresupuestoDetalleServiciosProgramaDao> listaDetalleServiciosPrograma;
	
	/**
	 * 
	 */
	private boolean reservaAnticipo;
	
	/**
	 * 
	 */
	private EnumTipoModificacion tipoModificacionCONVENIO;
	
	/**
	 * 
	 */
	private boolean seleccionadoPorcentajeBono;
	
	/**
	 * 
	 */
	private boolean seleccionadoPorcentajePromocion;
	
	/**
	 * 
	 */
	private DtoPresupuestoPaquetes dtoPresupuestoPaquete;
	
	
	/**
	 * Indica cual es el ajuste que se debe realizar
	 * al valor según lo definido en la parametrización de
	 * convenios en Método de Ajuste para Cálculo de Tarifa de Servicios.
	 */
	private String ajusteServicio;
	
	/**
	 * Indica el descuento del porcentaje odontológico que 
	 * aplica para el Programa o servicio
	 */
	private BigDecimal porcentajeDescuentoOdontologico;
	
	/**
	 * 
	 * Constructor de la clase
	 */
	public DtoPresupuestoOdoConvenio() 
	{
		reset();
	}
	
	/**
	 * 
	 */
	
	public void reset(){
		
		this.codigoPK = new BigDecimal(ConstantesBD.codigoNuncaValidoDouble);
		this.valorUnitario = new BigDecimal(ConstantesBD.codigoNuncaValidoDouble);
		this.convenio = new InfoDatosInt();
		this.usuarioModifica = new DtoInfoFechaUsuario();
		this.contratado = ConstantesBD.acronimoNo;
		this.presupuestoOdoProgServ = new BigDecimal(ConstantesBD.codigoNuncaValidoDouble);
		this.descuentoComercialUnitario = new BigDecimal(0);
		this.errorCalculoTarifa = "";
		this.detallePromocion = ConstantesBD.codigoNuncaValido;
		this.valorDescuentoPromocion = new BigDecimal(0);
		this.porcentajeHonorarioPromocion = ConstantesBD.codigoNuncaValidoDouble;
		this.valorHonorarioPromocion = new BigDecimal(0);
		this.advertenciaPromocion = "";
		this.serialBono = new BigDecimal(0);
		this.advertenciaBono = "";
		this.valorDescuentoBono = new BigDecimal(0);
		this.contrato= new InfoDatosInt();
		this.porcentajeDctoBono= 0;
		this.seleccionadoBono= false;
		this.seleccionadoPromocion=false;
		this.porcentajePromocion=0;
		this.listaDetalleServiciosPrograma= new ArrayList<DtoPresupuestoDetalleServiciosProgramaDao>();
		this.reservaAnticipo=false;
		this.tipoModificacionCONVENIO= EnumTipoModificacion.CARGADO_NO_MODIFICADO;
		this.seleccionadoPorcentajeBono= false;
		this.seleccionadoPorcentajePromocion= false;
		this.dtoPresupuestoPaquete= new DtoPresupuestoPaquetes();
		this.ajusteServicio = ConstantesIntegridadDominio.acronimoSinAjuste;
		
		this.setPorcentajeDescuentoOdontologico(new BigDecimal(0));
		
	}

	/**
	 * reset de la promocion
	 */
	public void resetPromocion()
	{
		this.setSeleccionadoPromocion(false);
		this.setValorDescuentoPromocion(BigDecimal.ZERO);
		this.setPorcentajePromocion(0);
		this.setValorHonorarioPromocion(BigDecimal.ZERO);
		this.setPorcentajeHonorarioPromocion(0.0);
		this.setAdvertenciaPromocion("");
		this.seleccionadoPorcentajePromocion= false;
	}
	
	/**
	 * reset del bono
	 */
	public void resetBono()
	{
		this.setSeleccionadoBono(false);
		this.setValorDescuentoBono(BigDecimal.ZERO);
		this.setPorcentajeDctoBono(0);
		this.setAdvertenciaBono("");
		this.setSerialBono(BigDecimal.ZERO);
		this.seleccionadoPorcentajeBono= false;
		this.bonoPaciente=0;
	}
	
	
	/**
	 * @return the codigoPK
	 */
	public BigDecimal getCodigoPK() {
		return codigoPK;
	}

	/**
	 * @param codigoPK the codigoPK to set
	 */
	public void setCodigoPK(BigDecimal codigoPK) {
		this.codigoPK = codigoPK;
	}

	/**
	 * @return the presupuestoOdoProgServ
	 */
	public BigDecimal getPresupuestoOdoProgServ() {
		return presupuestoOdoProgServ;
	}

	/**
	 * @param presupuestoOdoProgServ the presupuestoOdoProgServ to set
	 */
	public void setPresupuestoOdoProgServ(BigDecimal presupuestoOdoProgServ) {
		this.presupuestoOdoProgServ = presupuestoOdoProgServ;
	}

	/**
	 * @return the valorUnitario
	 */
	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}

	/**
	 * @return the valorUnitario
	 */
	public BigDecimal getValorUnitarioMenosDctoComercial() {
		return this.valorUnitario.subtract(this.getDescuentoComercialUnitario());
	}
	
	/**
	 * @return the valorUnitario
	 */
	public BigDecimal getValorTotalConvenioXProgServ(int cantidad) throws IPSException {
		
		if(this.getValorDescuentoBono().doubleValue()>0 && this.getValorDescuentoPromocion().doubleValue()>0)
		{
			if(!this.getSeleccionadoBono() && !this.getSeleccionadoPromocion())
				return new BigDecimal(-1);
			else
			{
				if(this.getSeleccionadoBono())
					return this.getValorUnitarioMenosValorBono().multiply(new BigDecimal(cantidad));
				if(this.getSeleccionadoPromocion())
					return this.getValorUnitarioMenosValorPromocion().multiply(new BigDecimal(cantidad));
			}
		}
		else if(this.getValorDescuentoBono().doubleValue()>0 || this.getValorDescuentoPromocion().doubleValue()>0)
		{
			if(this.getSeleccionadoBono())
				return this.getValorUnitarioMenosValorBono().multiply(new BigDecimal(cantidad));
			if(this.getSeleccionadoPromocion())
				return this.getValorUnitarioMenosValorPromocion().multiply(new BigDecimal(cantidad));
		
		}else if(this.getPorcentajeDescuentoOdontologico().doubleValue() > 0){
			
			/*
			 * Si no se cumple esas validaciones, entonces se verifica si
			 * se tiene un descuento odontológico asociado, para tomar el valor
			 * menos el descuento y aproximarlo según la parametrización
			 * del convenio o del esquema tarifario, según aplique.
			 */
			
			double valorAjustado = UtilidadValidacion.aproximarMetodoAjuste(this.ajusteServicio, this.getValorUnitarioMenosDctoOdontologico().doubleValue());
			
			return new BigDecimal(valorAjustado);
		}

		return this.getValorUnitarioMenosDctoComercial().multiply(new BigDecimal(cantidad));
	}
	
	/**
	 * Método que devuelve el valor del programa / servicio con el valor del descuento odontológico
	 * si aplica.
	 * 
	 * @return
	 */
	private BigDecimal getValorUnitarioMenosDctoOdontologico() {
		
		return this.getValorUnitarioMenosDctoComercial().subtract
		(this.getValorUnitarioMenosDctoComercial().multiply(this.getPorcentajeDescuentoOdontologico()).divide(new BigDecimal(100)));
	}

	/**
	 * @return the valorUnitario
	 */
	public String getValorTotalConvenioXProgServFormateado(int cantidad1) throws IPSException 
	{
		BigDecimal valor= this.getValorTotalConvenioXProgServ( cantidad1);
		if(valor.doubleValue()==-1)
		{
			return "Seleccione Bono/Promoción";
		}
		else
		{
			return UtilidadTexto.formatearValores(valor.doubleValue());
		}
	}
	
	public String getLogPromocion()
	{
		return "DETALLE PROM--->"+this.getDetallePromocion()+" VALOR PROM-->"+this.getValorDescuentoPromocion()+" %-->"+this.getPorcentajePromocion()+" honorario-->"+this.getValorHonorarioPromocion()+" %->"+this.getPorcentajeHonorarioPromocion()+" seleprom-->"+this.isSeleccionadoPorcentajePromocion();
	}
	
	/**
	 * @return the valorUnitario
	 */
	public BigDecimal getValorUnitarioMenosValorBono() {
		return this.getValorUnitarioMenosDctoComercial().subtract(this.getValorDescuentoBono());
	}

	/**
	 * @return the valorUnitario
	 */
	public BigDecimal getValorTotalXProgServMenosValorBono(int cantidad) {
		return this.getValorUnitarioMenosDctoComercial().subtract(this.getValorDescuentoBono()).multiply(new BigDecimal(cantidad));
	}

	/**
	 * @return the valorUnitario
	 */
	public String getValorTotalXProgServMenosValorBonoFormateado(int cantidad) {
		return UtilidadTexto.formatearValores(this.getValorTotalXProgServMenosValorBono(cantidad).doubleValue());
	}
	
	/**
	 * @return the valorUnitario
	 */
	public BigDecimal getValorUnitarioMenosValorPromocion() {
		return this.getValorUnitarioMenosDctoComercial().subtract(this.getValorDescuentoPromocion());
	}
	
	
	/**
	 * @return the valorUnitario
	 */
	public BigDecimal getValorTotalXProgServMenosValorPromocion(int cantidad) {
		return this.getValorUnitarioMenosDctoComercial().subtract(this.getValorDescuentoPromocion()).multiply(new BigDecimal(cantidad));
	}
	
	/**
	 * @return the valorUnitario
	 */
	public String getValorTotalXProgServMenosValorPromocionFormateado(int cantidad) {
		return UtilidadTexto.formatearValores(this.getValorTotalXProgServMenosValorPromocion(cantidad).doubleValue());
	}
	

	
	/**
	 * @param valorUnitario the valorUnitario to set
	 */
	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
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
	 * @return the usuarioModifica
	 */
	public DtoInfoFechaUsuario getUsuarioModifica() {
		return usuarioModifica;
	}

	/**
	 * @param usuarioModifica the usuarioModifica to set
	 */
	public void setUsuarioModifica(DtoInfoFechaUsuario usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

	/**
	 * @return the contratado
	 */
	public String getContratado() {
		return contratado;
	}

	/**
	 * @param contratado the contratado to set
	 */
	public void setContratado(String contratado) {
		this.contratado = contratado;
	}

	/**
	 * @param contratado the contratado to set
	 */
	public void setContratado(boolean contratadoBool) {
		this.contratado = (contratadoBool)?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo;
	}
	
	/**
	 * @return the descuentoComercialUnitario
	 */
	public BigDecimal getDescuentoComercialUnitario() {
		return descuentoComercialUnitario;
	}

	/**
	 * @param descuentoComercialUnitario the descuentoComercialUnitario to set
	 */
	public void setDescuentoComercialUnitario(BigDecimal descuentoComercialUnitario) {
		this.descuentoComercialUnitario = descuentoComercialUnitario;
	}

	/**
	 * @return the errorCalculoTarifa
	 */
	public String getErrorCalculoTarifa() {
		return errorCalculoTarifa;
	}

	/**
	 * @param errorCalculoTarifa the errorCalculoTarifa to set
	 */
	public void setErrorCalculoTarifa(String errorCalculoTarifa) {
		this.errorCalculoTarifa = errorCalculoTarifa;
	}

	/**
	 * @return the detallePromocion
	 */
	public double getDetallePromocion() {
		return detallePromocion;
	}

	/**
	 * @param detallePromocion the detallePromocion to set
	 */
	public void setDetallePromocion(double detallePromocion) {
		this.detallePromocion = detallePromocion;
	}

	/**
	 * @return the valorDescuentoPromocion
	 */
	public BigDecimal getValorDescuentoPromocion() {
		return valorDescuentoPromocion;
	}
	
	/**
	 * @return the valorDescuentoPromocion
	 */
	public double getValorDescuentoPromocionDouble() {
		return valorDescuentoPromocion.doubleValue();
	}

	/**
	 * @param valorDescuentoPromocion the valorDescuentoPromocion to set
	 */
	public void setValorDescuentoPromocion(BigDecimal valorDescuentoPromocion) {
		this.valorDescuentoPromocion = valorDescuentoPromocion;
	}

	

	/**
	 * @return the valorHonorarioPromocion
	 */
	public BigDecimal getValorHonorarioPromocion() {
		return valorHonorarioPromocion;
	}

	/**
	 * @param valorHonorarioPromocion the valorHonorarioPromocion to set
	 */
	public void setValorHonorarioPromocion(BigDecimal valorHonorarioPromocion) {
		this.valorHonorarioPromocion = valorHonorarioPromocion;
	}

	/**
	 * @return the advertenciaPromocion
	 */
	public String getAdvertenciaPromocion() {
		return advertenciaPromocion;
	}

	/**
	 * @param advertenciaPromocion the advertenciaPromocion to set
	 */
	public void setAdvertenciaPromocion(String advertenciaPromocion) {
		this.advertenciaPromocion = advertenciaPromocion;
	}

	/**
	 * @return the porcentajeHonorarioPromocion
	 */
	public Double getPorcentajeHonorarioPromocion() {
		return porcentajeHonorarioPromocion;
	}

	/**
	 * @param porcentajeHonorarioPromocion the porcentajeHonorarioPromocion to set
	 */
	public void setPorcentajeHonorarioPromocion(Double porcentajeHonorarioPromocion) {
		this.porcentajeHonorarioPromocion = porcentajeHonorarioPromocion;
	}

	/**
	 * @return the serialBono
	 */
	public BigDecimal getSerialBono() {
		return serialBono;
	}

	/**
	 * @param serialBono the serialBono to set
	 */
	public void setSerialBono(BigDecimal serialBono) {
		this.serialBono = serialBono;
	}

	/**
	 * @return the valorDescuentoBono
	 */
	public BigDecimal getValorDescuentoBono() {
		return valorDescuentoBono;
	}
	
	/**
	 * @return the valorDescuentoBono
	 */
	public double getValorDescuentoBonoDouble() {
		return valorDescuentoBono.doubleValue();
	}

	/**
	 * @param valorDescuentoBono the valorDescuentoBono to set
	 */
	public void setValorDescuentoBono(BigDecimal valorDescuentoBono) {
		this.valorDescuentoBono = valorDescuentoBono;
	}

	/**
	 * @return the advertenciaBono
	 */
	public String getAdvertenciaBono() {
		return advertenciaBono;
	}

	/**
	 * @param advertenciaBono the advertenciaBono to set
	 */
	public void setAdvertenciaBono(String advertenciaBono) {
		this.advertenciaBono = advertenciaBono;
	}

	/**
	 * @return the contrato
	 */
	public InfoDatosInt getContrato() {
		return contrato;
	}

	/**
	 * @param contrato the contrato to set
	 */
	public void setContrato(InfoDatosInt contrato) {
		this.contrato = contrato;
	}

	/**
	 * @return the porcentajeDctoBono
	 */
	public double getPorcentajeDctoBono()
	{
		return porcentajeDctoBono;
	}

	/**
	 * @return the porcentajeDctoBono
	 */
	public String getPorcentajeOValorDctoBono()
	{
		String retorna="";
		if(this.isSeleccionadoPorcentajeBono())
		{	
			retorna= porcentajeDctoBono+"% ";
		}	
		else
		{	
			retorna= UtilidadTexto.formatearValores(this.valorDescuentoBono.doubleValue());
		}	
		return retorna;
	}
	
	/**
	 * @return the porcentajeDctoBono
	 */
	public String getPorcentajeOValorPromocion()
	{
		String retorna="";
		if(this.isSeleccionadoPorcentajePromocion())
		{
			retorna+= porcentajePromocion+"%";
		}	
		else 
		{	
			retorna+=UtilidadTexto.formatearValores(this.valorDescuentoPromocion.doubleValue());
		}	
		return retorna;
	}
	
	/**
	 * @param porcentajeDctoBono the porcentajeDctoBono to set
	 */
	public void setPorcentajeDctoBono(double porcentajeDctoBono)
	{
		this.porcentajeDctoBono = porcentajeDctoBono;
	}

	/**
	 * @return the seleccionadoBono
	 */
	public boolean getSeleccionadoBono()
	{
		return seleccionadoBono;
	}

	/**
	 * @return the seleccionadoPromocion
	 */
	public boolean getSeleccionadoPromocion()
	{
		return seleccionadoPromocion;
	}

	/**
	 * @param seleccionadoBono the seleccionadoBono to set
	 */
	public void setSeleccionadoBono(boolean seleccionadoBono)
	{
		this.seleccionadoBono = seleccionadoBono;
	}

	/**
	 * @param seleccionadoPromocion the seleccionadoPromocion to set
	 */
	public void setSeleccionadoPromocion(boolean seleccionadoPromocion)
	{
		this.seleccionadoPromocion = seleccionadoPromocion;
	}

	/**
	 * @return the porcentajePromocion
	 */
	public double getPorcentajePromocion()
	{
		return porcentajePromocion;
	}

	/**
	 * @param porcentajePromocion the porcentajePromocion to set
	 */
	public void setPorcentajePromocion(double porcentajePromocion)
	{
		this.porcentajePromocion = porcentajePromocion;
	}

	/**
	 * @return the listaDetalleServiciosPrograma
	 */
	public ArrayList<DtoPresupuestoDetalleServiciosProgramaDao> getListaDetalleServiciosPrograma() {
		return listaDetalleServiciosPrograma;
	}

	/**
	 * @param listaDetalleServiciosPrograma the listaDetalleServiciosPrograma to set
	 */
	public void setListaDetalleServiciosPrograma(
			ArrayList<DtoPresupuestoDetalleServiciosProgramaDao> listaDetalleServiciosPrograma) {
		this.listaDetalleServiciosPrograma = listaDetalleServiciosPrograma;
	}

	/**
	 * @return the reservaAnticipo
	 */
	public boolean isReservaAnticipo() {
		return reservaAnticipo;
	}

	/**
	 * @param reservaAnticipo the reservaAnticipo to set
	 */
	public void setReservaAnticipo(boolean reservaAnticipo) {
		this.reservaAnticipo = reservaAnticipo;
	}

	/**
	 * @return the reservaAnticipo
	 */
	public boolean getReservaAnticipo() {
		return reservaAnticipo;
	}

	/**
	 * @return the tipoModificacionCONVENIO
	 */
	public EnumTipoModificacion getTipoModificacionCONVENIO() {
		return tipoModificacionCONVENIO;
	}

	/**
	 * @param tipoModificacionCONVENIO the tipoModificacionCONVENIO to set
	 */
	public void setTipoModificacionCONVENIO(
			EnumTipoModificacion tipoModificacionCONVENIO) {
		this.tipoModificacionCONVENIO = tipoModificacionCONVENIO;
	}

	/**
	 * @return the seleccionadoPorcentajeBono
	 */
	public boolean isSeleccionadoPorcentajeBono() {
		return seleccionadoPorcentajeBono;
	}

	/**
	 * @param seleccionadoPorcentajeBono the seleccionadoPorcentajeBono to set
	 */
	public void setSeleccionadoPorcentajeBono(boolean seleccionadoPorcentajeBono) {
		this.seleccionadoPorcentajeBono = seleccionadoPorcentajeBono;
	}

	/**
	 * @return the seleccionadoPorcentajePromocion
	 */
	public boolean isSeleccionadoPorcentajePromocion() {
		return seleccionadoPorcentajePromocion;
	}

	/**
	 * @param seleccionadoPorcentajePromocion the seleccionadoPorcentajePromocion to set
	 */
	public void setSeleccionadoPorcentajePromocion(
			boolean seleccionadoPorcentajePromocion) {
		this.seleccionadoPorcentajePromocion = seleccionadoPorcentajePromocion;
	}

	/**
	 * @return the dtoPresupuestoPaquete
	 */
	public DtoPresupuestoPaquetes getDtoPresupuestoPaquete() {
		return dtoPresupuestoPaquete;
	}

	/**
	 * @param dtoPresupuestoPaquete the dtoPresupuestoPaquete to set
	 */
	public void setDtoPresupuestoPaquete(
			DtoPresupuestoPaquetes dtoPresupuestoPaquete) {
		this.dtoPresupuestoPaquete = dtoPresupuestoPaquete;
	}

	/**
	 * Obtiene el valor del atributo bonoPaciente
	 *
	 * @return Retorna atributo bonoPaciente
	 */
	public int getBonoPaciente()
	{
		return bonoPaciente;
	}

	/**
	 * Establece el valor del atributo bonoPaciente
	 *
	 * @param valor para el atributo bonoPaciente
	 */
	public void setBonoPaciente(int bonoPaciente)
	{
		this.bonoPaciente = bonoPaciente;
	}

	/**
	 * @param ajusteServicio the ajusteServicio to set
	 */
	public void setAjusteServicio(String ajusteServicio) {
		this.ajusteServicio = ajusteServicio;
	}

	/**
	 * @return the ajusteServicio
	 */
	public String getAjusteServicio() {
		return ajusteServicio;
	}

	/**
	 * @param porcentajeDescuentoOdontologico the porcentajeDescuentoOdontologico to set
	 */
	public void setPorcentajeDescuentoOdontologico(
			BigDecimal porcentajeDescuentoOdontologico) {
		this.porcentajeDescuentoOdontologico = porcentajeDescuentoOdontologico;
	}

	/**
	 * @return the porcentajeDescuentoOdontologico
	 */
	public BigDecimal getPorcentajeDescuentoOdontologico() {
		return porcentajeDescuentoOdontologico;
	}
	
}
