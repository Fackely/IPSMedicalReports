package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.util.ArrayList;

import util.ConstantesBD;
import util.Errores;
import util.InfoDatosDouble;

import com.princetonsa.dto.administracion.DtoPersonas;



/**
 * Dto para almacenar Beneficicarios de la venta tarjeta cliente
 * @author Edgar Carvajal Ruiz
 */
@SuppressWarnings("serial")
public class DtoBeneficiarioCliente implements Serializable , Cloneable{
	
	/**
	 * Codigo pk Beneficiario 
	 */
	private double codigoPk ;
	
	/**
	 * atributo que hace referencia a la venta tarjeta
	 */
	private double ventaTarjetaCliente ;

	/**
	 *Atributo para almacenar el serial de los beneficiario 
	 */
	private String serial ;
	
	/**
	 * Numero de tarjea
	 * set utiliza solo el momento de que se venda un tarjeta para migracion
	 * al insercion de este atributo dependen de un parametro general 
	 */
	private String numTarjeta;
	
	/**
	 *Atributo que almacena el atributo general 
	 */
	private String indicativoPrincipal ;
	
	/**
	 * Atributo para almacenar el parentesco de los beneficiarios
	 */
	private int parentesco ;
	
	/**
	 * Atributo para informa el estado de la tarjeta
	 */
	private String estadoTarjeta ;
	
	/**
	 *Atributo para indicar el aliado odontológico 
	 */
	private String indicadorAlidado ;
	
	/**
	 * Aliado odontológico
	 */
	private InfoDatosDouble alidadoOdontologico ;
	
	/**
	 * Atributo tipo de tarjeta cliente
	 * este atributo solo se llena en el momento de hacer la migración
	 * TODO verificar si tiene aplica 
	 */
	private DtoTarjetaCliente tipoTarjetaCliente ;
	
	/**
	 * atributo para almacenar las observaciones
	 */
	private String observaciones ;
	
	/**
	 * Almacena la Hora de modificación 
	 */
	private String horaModifica;
	
	/**
	 * Almacena la fecha modifica
	 */
	private String fechaModifica;
	
	/**
	 * Usuario modifica 
	 */
	private String usuarioModifica;
	
	/**
	 * tipo de venta de venta de tarjeta
	 */
	private String tipoVenta;

	/**
	 * Institución de beneficiario
	 * TODO VERIFICAR EXISTENCIA  
	 */
	private int institucion;
	
	/**
	 * Consecutivo de beneficiario
	 */
	private int consecutivo;
	
	/**
	 * atributo para mostrar el parentesco
	 */
	private  String nombreParentesco;
	
	/**
	 *Objeto que hace referencia a las personas 
	 */
	private DtoPersonas dtoPersonas;
	
	/**
	 * Lista de errores específicos del beneficiario
	 */
	private ArrayList<Errores> errores;
	
	/**
	 * El beneficiario es el mismo comprador y no tiene asociada una tarjeta
	 */
	private boolean esComprador;
	
	/**
	 * Indica si el beneficiario se debe relacionar con la venta, el único
	 * caso en que no se debe hacer es cuando el paciente ya tiene
	 * una tarjeta previamente relacionada
	 */
	private boolean tieneTarjetaPrevia; 

	/**
	 * Indica si se puede modificar o no la información del beneficiario ingresada
	 */
	private boolean permitirModificar;

	/**
	 * Limpia la información del beneficiario
	 */
	public void reset(){
		this.codigoPk = 0;
		this.ventaTarjetaCliente = 0 ;
		this.serial ="";
		this.numTarjeta="";
		this.indicativoPrincipal ="";
		this.parentesco = -1;
		this.estadoTarjeta ="";
		this.indicadorAlidado =ConstantesBD.acronimoNo; 
        this.setAlidadoOdontologico(new InfoDatosDouble());
		this.tipoTarjetaCliente = new DtoTarjetaCliente();
		this.observaciones ="";
		this.horaModifica="";
		this.fechaModifica="";
		this.usuarioModifica="";
		this.tipoVenta = "";
		this.institucion=ConstantesBD.codigoNuncaValido;
		this.consecutivo= ConstantesBD.codigoNuncaValido;
		this.nombreParentesco="";
		
		this.dtoPersonas= new DtoPersonas();
		this.errores=new ArrayList<Errores>();
		this.esComprador=false;
		this.tieneTarjetaPrevia=false;
		this.permitirModificar=true;
	}
	
	/**
	 * Constructor vacío
	 */
	public DtoBeneficiarioCliente() 
	{
		reset();
	}

	/**
	 * @return the codigoPk
	 */
	public double getCodigoPk() {
		return codigoPk;
	}

	/**
	 * @return the ventaTarjetaCliente
	 */
	public double getVentaTarjetaCliente() {
		return ventaTarjetaCliente;
	}

	/**
	 * @return the serial
	 */
	public String getSerial() {
		return serial;
	}

	/**
	 * @return the numTarjeta
	 */
	public String getNumTarjeta() {
		return numTarjeta;
	}

	/**
	 * @return the indicativoPrincipal
	 */
	public String getIndicativoPrincipal() {
		return indicativoPrincipal;
	}

	/**
	 * @return the parentezco
	 */
	public int getParentesco() {
		return parentesco;
	}

	/**
	 * @return the estadoTarjeta
	 */
	public String getEstadoTarjeta() {
		return estadoTarjeta;
	}

	/**
	 * @return the indicadorAlidado
	 */
	public String getIndicadorAlidado() {
		return indicadorAlidado;
	}

	/**
	 * @return the alidadoOdontologico
	 */
	public InfoDatosDouble getAlidadoOdontologico() {
		return alidadoOdontologico;
	}

	/**
	 * @return the tipoTarjetaCliente
	 */
	public DtoTarjetaCliente getTipoTarjetaCliente() {
		return tipoTarjetaCliente;
	}

	/**
	 * @return the observaciones
	 */
	public String getObservaciones() {
		return observaciones;
	}

	/**
	 * @return the horaModifica
	 */
	public String getHoraModifica() {
		return horaModifica;
	}

	/**
	 * @return the fechaModifica
	 */
	public String getFechaModifica() {
		return fechaModifica;
	}

	/**
	 * @return the usuarioModifica
	 */
	public String getUsuarioModifica() {
		return usuarioModifica;
	}

	/**
	 * @return the tipoVenta
	 */
	public String getTipoVenta() {
		return tipoVenta;
	}

	/**
	 * @return the institucion
	 */
	public int getInstitucion() {
		return institucion;
	}

	/**
	 * @return the consecutivo
	 */
	public int getConsecutivo() {
		return consecutivo;
	}

	/**
	 * @return the dtoPersonas
	 */
	public DtoPersonas getDtoPersonas() {
		return dtoPersonas;
	}

	/**
	 * @param codigoPk the codigoPk to set
	 */
	public void setCodigoPk(double codigoPk) {
		this.codigoPk = codigoPk;
	}

	/**
	 * @param ventaTarjetaCliente the ventaTarjetaCliente to set
	 */
	public void setVentaTarjetaCliente(double ventaTarjetaCliente) {
		this.ventaTarjetaCliente = ventaTarjetaCliente;
	}

	/**
	 * @param serial the serial to set
	 */
	public void setSerial(String serial) {
		this.serial = serial;
	}

	/**
	 * @param numTarjeta the numTarjeta to set
	 */
	public void setNumTarjeta(String numTarjeta) {
		this.numTarjeta = numTarjeta;
	}

	/**
	 * @param indicativoPrincipal the indicativoPrincipal to set
	 */
	public void setIndicativoPrincipal(String indicativoPrincipal) {
		this.indicativoPrincipal = indicativoPrincipal;
	}

	/**
	 * @param parentezco the parentezco to set
	 */
	public void setParentesco(int parentezco) {
		this.parentesco = parentezco;
	}

	/**
	 * @param estadoTarjeta the estadoTarjeta to set
	 */
	public void setEstadoTarjeta(String estadoTarjeta) {
		this.estadoTarjeta = estadoTarjeta;
	}

	/**
	 * @param indicadorAlidado the indicadorAlidado to set
	 */
	public void setIndicadorAlidado(String indicadorAlidado) {
		this.indicadorAlidado = indicadorAlidado;
	}

	/**
	 * @param alidadoOdontologico the alidadoOdontologico to set
	 */
	public void setAlidadoOdontologico(InfoDatosDouble alidadoOdontologico) {
		this.alidadoOdontologico = alidadoOdontologico;
	}

	/**
	 * @param tipoTarjetaCliente the tipoTarjetaCliente to set
	 */
	public void setTipoTarjetaCliente(DtoTarjetaCliente tipoTarjetaCliente) {
		this.tipoTarjetaCliente = tipoTarjetaCliente;
	}

	/**
	 * @param observaciones the observaciones to set
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	/**
	 * @param horaModifica the horaModifica to set
	 */
	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}

	/**
	 * @param fechaModifica the fechaModifica to set
	 */
	public void setFechaModifica(String fechaModifica) {
		this.fechaModifica = fechaModifica;
	}

	/**
	 * @param usuarioModifica the usuarioModifica to set
	 */
	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

	/**
	 * @param tipoVenta the tipoVenta to set
	 */
	public void setTipoVenta(String tipoVenta) {
		this.tipoVenta = tipoVenta;
	}

	/**
	 * @param institucion the institucion to set
	 */
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	/**
	 * @param consecutivo the consecutivo to set
	 */
	public void setConsecutivo(int consecutivo) {
		this.consecutivo = consecutivo;
	}

	/**
	 * @param dtoPersonas the dtoPersonas to set
	 */
	public void setDtoPersonas(DtoPersonas dtoPersonas) {
		this.dtoPersonas = dtoPersonas;
	}

	public void setNombreParentesco(String nombreParentesco) {
		this.nombreParentesco = nombreParentesco;
	}

	public String getNombreParentesco() {
		return nombreParentesco;
	}

	public ArrayList<Errores> getErrores() {
		return errores;
	}

	public void setErrores(ArrayList<Errores> errores) {
		this.errores = errores;
	}

	public boolean isEsComprador() {
		return esComprador;
	}

	public void setEsComprador(boolean esComprador) {
		this.esComprador = esComprador;
	}

	/**
	 * Obtiene el valor del atributo tieneTarjetaPrevia
	 *
	 * @return Retorna atributo tieneTarjetaPrevia
	 */
	public boolean isTieneTarjetaPrevia()
	{
		return tieneTarjetaPrevia;
	}

	/**
	 * Establece el valor del atributo tieneTarjetaPrevia
	 *
	 * @param valor para el atributo tieneTarjetaPrevia
	 */
	public void setTieneTarjetaPrevia(boolean tieneTarjetaPrevia)
	{
		this.tieneTarjetaPrevia = tieneTarjetaPrevia;
	}

	/**
	 * Obtiene el valor del atributo permitirModificar
	 *
	 * @return Retorna atributo permitirModificar
	 */
	public boolean isPermitirModificar()
	{
		return permitirModificar;
	}

	/**
	 * Establece el valor del atributo permitirModificar
	 *
	 * @param valor para el atributo permitirModificar
	 */
	public void setPermitirModificar(boolean permitirModificar)
	{
		this.permitirModificar = permitirModificar;
	}
	
	/**
	 * Indica si la información del beneficiario se puede o no modificar 
	 * @return true en caso de que se pueda modificar, false de lo contrario.
	 */
	public boolean getInformacionModificable()
	{
		if(esComprador)
		{
			return false;
		}
		return permitirModificar;
	}

	
}
