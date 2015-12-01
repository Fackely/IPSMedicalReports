package com.princetonsa.dto.facturacion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import util.ConstantesBD;
import util.InfoDatosString;

/**
 * Data Transfer Object para el manejo del convenio
 * 
 * @author Sebastián Gómez, Edgar Carvajal
 *
 */
public class DtoConvenio implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	/** * Código del convenio */
	private int codigo;
	/** * Descripción del convenio */
	private String descripcion;
	/** * Tipo de REgimen */
	private InfoDatosString tipoRegimen;
	
	private String ingresarPacienteValidacionBD;
	
	private String esConvenioTarjetaCliente;
	
	private String ingresoPacienteRequiereAutorizacion;
	
	private String requiereIngresoValidacionAuto;
	
	private String tipoAtencion;	
	
	private String requiereBonoIngresoPaciente;

	private String manejaBonos;
	
	private String habilitarSelect;
	
	private String convenioManejaMontos;
	
	private String manejaPreupuestoCapitacion;
	
	/**
	 * Contratos  
	 */
	private  List<DtoContrato> listContrato;
	
	/**
	 * Está seleccionado en parámetros por defecto
	 */
	private boolean marcadoPorDefecto;
	
	/**
	 * Convenio activo
	 */
	private boolean activo;
	
	/**
	 * Constructor
	 * Convenio 
	 *
	 */
	public DtoConvenio()
	{
		this.codigo = 0;
		this.descripcion = "";
		this.tipoRegimen = new InfoDatosString("","");
		this.ingresarPacienteValidacionBD="";
		this.esConvenioTarjetaCliente="";
		this.ingresoPacienteRequiereAutorizacion="";
		this.requiereIngresoValidacionAuto="";
		this.tipoAtencion="";
		
		this.requiereBonoIngresoPaciente="";
		this.manejaBonos="";
		this.convenioManejaMontos="";
		
		this.habilitarSelect= ConstantesBD.acronimoSi;
		this.listContrato=new ArrayList<DtoContrato>();
		this.marcadoPorDefecto=false;
	}

	
	/**
	 * Este contratoas
	 */
	public DtoConvenio(int codigo, String nombre){
		this.codigo=codigo;
		this.descripcion=nombre;
	}
	
	/**
	 * Obtener número de contratos relacionados al convenio
	 * @return cantidad de contratos
	 */
	public int getNumeroContratos()
	{
		if(listContrato==null)
		{
			return 0;
		}
		return listContrato.size();
	}

	/**
	 * Obtiene el valor del atributo codigo
	 *
	 * @return Retorna atributo codigo
	 */
	public int getCodigo()
	{
		return codigo;
	}

	/**
	 * Establece el valor del atributo codigo
	 *
	 * @param valor para el atributo codigo
	 */
	public void setCodigo(int codigo)
	{
		this.codigo = codigo;
	}

	/**
	 * Obtiene el valor del atributo descripcion
	 *
	 * @return Retorna atributo descripcion
	 */
	public String getDescripcion()
	{
		return descripcion;
	}

	/**
	 * Establece el valor del atributo descripcion
	 *
	 * @param valor para el atributo descripcion
	 */
	public void setDescripcion(String descripcion)
	{
		this.descripcion = descripcion;
	}

	/**
	 * Obtiene el valor del atributo tipoRegimen
	 *
	 * @return Retorna atributo tipoRegimen
	 */
	public InfoDatosString getTipoRegimen()
	{
		return tipoRegimen;
	}

	/**
	 * Establece el valor del atributo tipoRegimen
	 *
	 * @param valor para el atributo tipoRegimen
	 */
	public void setTipoRegimen(InfoDatosString tipoRegimen)
	{
		this.tipoRegimen = tipoRegimen;
	}

	/**
	 * Obtiene el valor del atributo ingresarPacienteValidacionBD
	 *
	 * @return Retorna atributo ingresarPacienteValidacionBD
	 */
	public String getIngresarPacienteValidacionBD()
	{
		return ingresarPacienteValidacionBD;
	}

	/**
	 * Establece el valor del atributo ingresarPacienteValidacionBD
	 *
	 * @param valor para el atributo ingresarPacienteValidacionBD
	 */
	public void setIngresarPacienteValidacionBD(String ingresarPacienteValidacionBD)
	{
		this.ingresarPacienteValidacionBD = ingresarPacienteValidacionBD;
	}

	/**
	 * Obtiene el valor del atributo esConvenioTarjetaCliente
	 *
	 * @return Retorna atributo esConvenioTarjetaCliente
	 */
	public String getEsConvenioTarjetaCliente()
	{
		return esConvenioTarjetaCliente;
	}

	/**
	 * Establece el valor del atributo esConvenioTarjetaCliente
	 *
	 * @param valor para el atributo esConvenioTarjetaCliente
	 */
	public void setEsConvenioTarjetaCliente(String esConvenioTarjetaCliente)
	{
		this.esConvenioTarjetaCliente = esConvenioTarjetaCliente;
	}

	/**
	 * Obtiene el valor del atributo esConvenioTarjetaCliente
	 *
	 * @return Retorna atributo esConvenioTarjetaCliente
	 */
	public Character getEsConvenioTarjetaClienteChar()
	{
		if(esConvenioTarjetaCliente==null || esConvenioTarjetaCliente.length()==0)
		{
			return '\0';
		}
		return esConvenioTarjetaCliente.charAt(0);
	}

	/**
	 * Establece el valor del atributo esConvenioTarjetaCliente
	 *
	 * @param valor para el atributo esConvenioTarjetaCliente
	 */
	public void setEsConvenioTarjetaClienteChar(Character esConvenioTarjetaCliente)
	{
		this.esConvenioTarjetaCliente = esConvenioTarjetaCliente+"";
	}

	/**
	 * Obtiene el valor del atributo ingresoPacienteRequiereAutorizacion
	 *
	 * @return Retorna atributo ingresoPacienteRequiereAutorizacion
	 */
	public String getIngresoPacienteRequiereAutorizacion()
	{
		return ingresoPacienteRequiereAutorizacion;
	}

	/**
	 * Establece el valor del atributo ingresoPacienteRequiereAutorizacion
	 *
	 * @param valor para el atributo ingresoPacienteRequiereAutorizacion
	 */
	public void setIngresoPacienteRequiereAutorizacion(
			String ingresoPacienteRequiereAutorizacion)
	{
		this.ingresoPacienteRequiereAutorizacion = ingresoPacienteRequiereAutorizacion;
	}

	/**
	 * Obtiene el valor del atributo requiereIngresoValidacionAuto
	 *
	 * @return Retorna atributo requiereIngresoValidacionAuto
	 */
	public String getRequiereIngresoValidacionAuto()
	{
		return requiereIngresoValidacionAuto;
	}

	/**
	 * Establece el valor del atributo requiereIngresoValidacionAuto
	 *
	 * @param valor para el atributo requiereIngresoValidacionAuto
	 */
	public void setRequiereIngresoValidacionAuto(
			String requiereIngresoValidacionAuto)
	{
		this.requiereIngresoValidacionAuto = requiereIngresoValidacionAuto;
	}

	/**
	 * Obtiene el valor del atributo tipoAtencion
	 *
	 * @return Retorna atributo tipoAtencion
	 */
	public String getTipoAtencion()
	{
		return tipoAtencion;
	}

	/**
	 * Establece el valor del atributo tipoAtencion
	 *
	 * @param valor para el atributo tipoAtencion
	 */
	public void setTipoAtencion(String tipoAtencion)
	{
		this.tipoAtencion = tipoAtencion;
	}

	/**
	 * Obtiene el valor del atributo requiereBonoIngresoPaciente
	 *
	 * @return Retorna atributo requiereBonoIngresoPaciente
	 */
	public String getRequiereBonoIngresoPaciente()
	{
		return requiereBonoIngresoPaciente;
	}

	/**
	 * Establece el valor del atributo requiereBonoIngresoPaciente
	 *
	 * @param valor para el atributo requiereBonoIngresoPaciente
	 */
	public void setRequiereBonoIngresoPaciente(String requiereBonoIngresoPaciente)
	{
		this.requiereBonoIngresoPaciente = requiereBonoIngresoPaciente;
	}

	/**
	 * Obtiene el valor del atributo manejaBonos
	 *
	 * @return Retorna atributo manejaBonos
	 */
	public String getManejaBonos()
	{
		return manejaBonos;
	}

	/**
	 * Establece el valor del atributo manejaBonos
	 *
	 * @param valor para el atributo manejaBonos
	 */
	public void setManejaBonos(String manejaBonos)
	{
		this.manejaBonos = manejaBonos;
	}

	/**
	 * Obtiene el valor del atributo habilitarSelect
	 *
	 * @return Retorna atributo habilitarSelect
	 */
	public String getHabilitarSelect()
	{
		return habilitarSelect;
	}

	/**
	 * Establece el valor del atributo habilitarSelect
	 *
	 * @param valor para el atributo habilitarSelect
	 */
	public void setHabilitarSelect(String habilitarSelect)
	{
		this.habilitarSelect = habilitarSelect;
	}

	/**
	 * Obtiene el valor del atributo listContrato
	 *
	 * @return Retorna atributo listContrato
	 */
	public List<DtoContrato> getListContrato()
	{
		return listContrato;
	}

	/**
	 * Establece el valor del atributo listContrato
	 *
	 * @param valor para el atributo listContrato
	 */
	public void setListContrato(List<DtoContrato> listContrato)
	{
		this.listContrato = listContrato;
	}

	/**
	 * Obtiene el valor del atributo marcadoPorDefecto
	 *
	 * @return Retorna atributo marcadoPorDefecto
	 */
	public boolean isMarcadoPorDefecto()
	{
		return marcadoPorDefecto;
	}

	/**
	 * Establece el valor del atributo marcadoPorDefecto
	 *
	 * @param valor para el atributo marcadoPorDefecto
	 */
	public void setMarcadoPorDefecto(boolean marcadoPorDefecto)
	{
		this.marcadoPorDefecto = marcadoPorDefecto;
	}

	/**
	 * Obtiene el valor del atributo acronimoTipoRegimen
	 *
	 * @return Retorna atributo marcadoPorDefecto
	 */
	public String getAcronimoTipoRegimen() {
		return tipoRegimen.getCod();
	}

	/**
	 * Establece el valor del atributo acronimoTipoRegimen
	 *
	 * @param valor para el atributo marcadoPorDefecto
	 */
	public void setAcronimoTipoRegimen(String acronimoTipoRegimen) {
		this.tipoRegimen.setCodigo(acronimoTipoRegimen);
	}
	
	/**
	 * Obtiene el valor del atributo descripcionTipoRegimen
	 *
	 * @return Retorna atributo marcadoPorDefecto
	 */
	public String getDescripcionTipoRegimen() {
		return tipoRegimen.getNombre();
	}

	/**
	 * Establece el valor del atributo descripciontipoRegimen
	 *
	 * @param valor para el atributo marcadoPorDefecto
	 */
	public void setDescripcionTipoRegimen(String descripciontipoRegimen) {
		this.tipoRegimen.setNombre(descripciontipoRegimen);
	}

	/**
	 * Obtiene el valor del atributo activo
	 *
	 * @return Retorna atributo activo
	 */
	public boolean isActivo() {
		return activo;
	}

	/**
	 * Establece el valor del atributo activo
	 *
	 * @param valor para el atributo activo
	 */
	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public String getConvenioManejaMontos() {
		return convenioManejaMontos;
	}

	public void setConvenioManejaMontos(String convenioManejaMontos) {
		this.convenioManejaMontos = convenioManejaMontos;
	}

	public String getManejaPreupuestoCapitacion() {
		return manejaPreupuestoCapitacion;
	}

	public void setManejaPreupuestoCapitacion(String manejaPreupuestoCapitacion) {
		this.manejaPreupuestoCapitacion = manejaPreupuestoCapitacion;
	}


}
