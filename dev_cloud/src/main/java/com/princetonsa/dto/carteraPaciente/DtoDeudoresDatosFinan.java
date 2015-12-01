package com.princetonsa.dto.carteraPaciente;

import java.io.Serializable;

import util.ConstantesBD;
import util.ValoresPorDefecto;

/**
 * 
 * @author V�ctor G�mez L.
 *
 */
public class DtoDeudoresDatosFinan implements Serializable
{
	private int codigoPK;
	/**
	 * @return the codigoPK
	 */
	public int getCodigoPK() {
		return codigoPK;
	}

	/**
	 * @param codigoPK the codigoPK to set
	 */
	public void setCodigoPK(int codigoPK) {
		this.codigoPK = codigoPK;
	}

	private int datosFinanciacion;
	private int ingreso;
	private int institucion;
	private String claseDeudor;
	private String idDeudor;
	private String tipoDeudor;
	private String codigoPaciente;
	private String codigoResponsable;
	private String tipoIdentificacion;
	private String nombreTipoIdentificacion;
	private String numeroIdentificacion;
	private String primerApellido;
	private String segundoApellido;
	private String primerNombre;
	private String segundoNombre;
	private String direccion;
	private String telefono;
	private String ocupacion;
	private String detalleocupacion;
	private String empresa;
	private String cargo;
	private String antiguedad;
	private String direccionOficina;
	private String telefonoOficina;
	private int cuenta;
	private String usuarioModifica;
	
	//informacino de referencia familiar.
	private String nombresReferenciaFamiliar;
	private String direccionReferenciaFamiliar;
	private String telefonoReferenciaFamiliar;
	
	
	//Atributo validacion
	private String exiteDeudor;
	private String existePerRes;
	private int codigoPkDeudor;
	
	/**
	 * 
	 */
	private String paisID;
	
	/**
	 * 
	 */
	private String ciudadID;
	
	
	/**
	 * 
	 */
	private String departamentoID;
	
	public DtoDeudoresDatosFinan()
	{
		this.reset();
	}
	
	public void reset()
	{
		this.codigoPK=ConstantesBD.codigoNuncaValido;
		this.datosFinanciacion = ConstantesBD.codigoNuncaValido;
		this.ingreso = ConstantesBD.codigoNuncaValido;
		this.institucion = ConstantesBD.codigoNuncaValido;
		this.claseDeudor = "";
		this.idDeudor = "";
		this.tipoDeudor = "" ;
		this.codigoPaciente = "";
		this.codigoResponsable = "";
		this.tipoIdentificacion = "";
		this.nombreTipoIdentificacion="";
		this.numeroIdentificacion = "";
		this.primerApellido = "";
		this.segundoApellido = "";
		this.primerNombre = "";
		this.segundoNombre = "";
		this.direccion = "";
		this.telefono = "";
		this.ocupacion = "";
		this.detalleocupacion = "";
		this.empresa = "";
		this.cargo = "";
		this.antiguedad = "";
		this.direccionOficina = "";
		this.telefonoOficina = "";
		this.cuenta = ConstantesBD.codigoNuncaValido;
		this.usuarioModifica = "";
		this.codigoPkDeudor= ConstantesBD.codigoNuncaValido; 
		
		//Atributo validacion
		this.exiteDeudor = ConstantesBD.acronimoNo;
		
		this.existePerRes = ConstantesBD.acronimoNo;
		
		
		//informacion referencia familiar
		this.nombresReferenciaFamiliar="";
		this.direccionReferenciaFamiliar="";
		this.telefonoReferenciaFamiliar="";
		this.paisID="";
		this.ciudadID="";
		this.departamentoID="";
		
	}
	


	public int getCodigoPkDeudor() {
		return codigoPkDeudor;
	}

	public void setCodigoPkDeudor(int codigoPkDeudor) {
		this.codigoPkDeudor = codigoPkDeudor;
	}

	/**
	 * @return the paisID
	 */
	public String getPaisID() {
		return paisID;
	}

	/**
	 * @param paisID the paisID to set
	 */
	public void setPaisID(String paisID) {
		this.paisID = paisID;
	}



	
	/**
	 * @return the ciudadID
	 */
	public String getCiudadID() {
		return ciudadID;
	}

	/**
	 * @param ciudadID the ciudadID to set
	 */
	public void setCiudadID(String ciudadID) {
		this.ciudadID = ciudadID;
	}

	/**
	 * @return the departamentoID
	 */
	public String getDepartamentoID() {
		return departamentoID;
	}

	/**
	 * @param departamentoID the departamentoID to set
	 */
	public void setDepartamentoID(String departamentoID) {
		this.departamentoID = departamentoID;
	}

	/**
	 * @return the datosFinanciacion
	 */
	public int getDatosFinanciacion() {
		return datosFinanciacion;
	}

	/**
	 * @param datosFinanciacion the datosFinanciacion to set
	 */
	public void setDatosFinanciacion(int datosFinanciacion) {
		this.datosFinanciacion = datosFinanciacion;
	}

	/**
	 * @return the ingreso
	 */
	public int getIngreso() {
		return ingreso;
	}

	/**
	 * @param ingreso the ingreso to set
	 */
	public void setIngreso(int ingreso) {
		this.ingreso = ingreso;
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
	 * @return the claseDeudor
	 */
	public String getClaseDeudor() {
		return claseDeudor;
	}

	/**
	 * @param claseDeudor the claseDeudor to set
	 */
	public void setClaseDeudor(String claseDeudor) {
		this.claseDeudor = claseDeudor;
	}

	/**
	 * @return the idDeudor
	 */
	public String getIdDeudor() {
		return idDeudor;
	}

	/**
	 * @param idDeudor the idDeudor to set
	 */
	public void setIdDeudor(String idDeudor) {
		this.idDeudor = idDeudor;
	}
	
	/**
	 * @return the tipoDeudor
	 */
	public String getTipoDeudor() {
		return tipoDeudor;
	}

	/**
	 * @param tipoDeudor the tipoDeudor to set
	 */
	public void setTipoDeudor(String tipoDeudor) {
		this.tipoDeudor = tipoDeudor;
	}

	/**
	 * @return the tipoIdentificacion
	 */
	public String getTipoIdentificacion() {
		return tipoIdentificacion;
	}

	/**
	 * @param tipoIdentificacion the tipoIdentificacion to set
	 */
	public void setTipoIdentificacion(String tipoIdentificacion) {
		this.tipoIdentificacion = tipoIdentificacion;
	}

	/**
	 * @return the numeroIdentificacion
	 */
	public String getNumeroIdentificacion() {
		return numeroIdentificacion;
	}

	/**
	 * @param numeroIdentificacion the numeroIdentificacion to set
	 */
	public void setNumeroIdentificacion(String numeroIdentificacion) {
		this.numeroIdentificacion = numeroIdentificacion;
	}

	/**
	 * @return the primerApellido
	 */
	public String getPrimerApellido() {
		return primerApellido;
	}

	/**
	 * @param primerApellido the primerApellido to set
	 */
	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}

	/**
	 * @return the segundoApellido
	 */
	public String getSegundoApellido() {
		return segundoApellido;
	}

	/**
	 * @param segundoApellido the segundoApellido to set
	 */
	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}

	/**
	 * @return the primerNombre
	 */
	public String getPrimerNombre() {
		return primerNombre;
	}

	/**
	 * @param primerNombre the primerNombre to set
	 */
	public void setPrimerNombre(String primerNombre) {
		this.primerNombre = primerNombre;
	}

	/**
	 * @return the segundoNombre
	 */
	public String getSegundoNombre() {
		return segundoNombre;
	}

	/**
	 * @param segundoNombre the segundoNombre to set
	 */
	public void setSegundoNombre(String segundoNombre) {
		this.segundoNombre = segundoNombre;
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
	 * @return the ocupacion
	 */
	public String getOcupacion() {
		return ocupacion;
	}

	/**
	 * @param ocupacion the ocupacion to set
	 */
	public void setOcupacion(String ocupacion) {
		this.ocupacion = ocupacion;
	}

	/**
	 * @return the detalleocupacion
	 */
	public String getDetalleocupacion() {
		return detalleocupacion;
	}

	/**
	 * @param detalleocupacion the detalleocupacion to set
	 */
	public void setDetalleocupacion(String detalleocupacion) {
		this.detalleocupacion = detalleocupacion;
	}

	/**
	 * @return the empresa
	 */
	public String getEmpresa() {
		return empresa;
	}

	/**
	 * @param empresa the empresa to set
	 */
	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}

	/**
	 * @return the cargo
	 */
	public String getCargo() {
		return cargo;
	}

	/**
	 * @param cargo the cargo to set
	 */
	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	/**
	 * @return the antiguedad
	 */
	public String getAntiguedad() {
		return antiguedad;
	}

	/**
	 * @param antiguedad the antiguedad to set
	 */
	public void setAntiguedad(String antiguedad) {
		this.antiguedad = antiguedad;
	}

	/**
	 * @return the direccionOficina
	 */
	public String getDireccionOficina() {
		return direccionOficina;
	}

	/**
	 * @param direccionOficina the direccionOficina to set
	 */
	public void setDireccionOficina(String direccionOficina) {
		this.direccionOficina = direccionOficina;
	}

	/**
	 * @return the telefonoOficina
	 */
	public String getTelefonoOficina() {
		return telefonoOficina;
	}

	/**
	 * @param telefonoOficina the telefonoOficina to set
	 */
	public void setTelefonoOficina(String telefonoOficina) {
		this.telefonoOficina = telefonoOficina;
	}
	
	/**
	 * verifica si el dto tiene todos los datos requeridos
	 * @return
	 */
	public boolean isDatos()
	{
		if(this.datosFinanciacion!=ConstantesBD.codigoNuncaValido
				//&& this.ingreso!=ConstantesBD.codigoNuncaValido //el ingreso no es requerido en la funcionalidad de saldos iniciales cartera paciente.
				&& this.institucion!=ConstantesBD.codigoNuncaValido
				&& !this.claseDeudor.equals("")
				&& !this.numeroIdentificacion.equals(""))
			return true;
		else
			return false;
	}

	/**
	 * @return the exiteDeudor
	 */
	public String getExiteDeudor() {
		return exiteDeudor;
	}

	/**
	 * @param exiteDeudor the exiteDeudor to set
	 */
	public void setExiteDeudor(String exiteDeudor) {
		this.exiteDeudor = exiteDeudor;
	}

	/**
	 * @return the codigoPaciente
	 */
	public String getCodigoPaciente() {
		return codigoPaciente;
	}

	/**
	 * @param codigoPaciente the codigoPaciente to set
	 */
	public void setCodigoPaciente(String codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}

	/**
	 * @return the nombreTipoIdentificacion
	 */
	public String getNombreTipoIdentificacion() {
		return nombreTipoIdentificacion;
	}

	/**
	 * @param nombreTipoIdentificacion the nombreTipoIdentificacion to set
	 */
	public void setNombreTipoIdentificacion(String nombreTipoIdentificacion) {
		this.nombreTipoIdentificacion = nombreTipoIdentificacion;
	}

	

	/**
	 * @return the codigoResponsable
	 */
	public String getCodigoResponsable() {
		return codigoResponsable;
	}

	/**
	 * @param codigoResponsable the codigoResponsable to set
	 */
	public void setCodigoResponsable(String codigoResponsable) {
		this.codigoResponsable = codigoResponsable;
	}

	/**
	 * @return the cuenta
	 */
	public int getCuenta() {
		return cuenta;
	}

	/**
	 * @param cuenta the cuenta to set
	 */
	public void setCuenta(int cuenta) {
		this.cuenta = cuenta;
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

	/**
	 * @return the existePerRes
	 */
	public String getExistePerRes() {
		return existePerRes;
	}

	/**
	 * @param existePerRes the existePerRes to set
	 */
	public void setExistePerRes(String existePerRes) {
		this.existePerRes = existePerRes;
	}

	public String getNombresReferenciaFamiliar() {
		return nombresReferenciaFamiliar;
	}

	public void setNombresReferenciaFamiliar(String nombresReferenciaFamiliar) {
		this.nombresReferenciaFamiliar = nombresReferenciaFamiliar;
	}

	public String getDireccionReferenciaFamiliar() {
		return direccionReferenciaFamiliar;
	}

	public void setDireccionReferenciaFamiliar(String direccionReferenciaFamiliar) {
		this.direccionReferenciaFamiliar = direccionReferenciaFamiliar;
	}

	public String getTelefonoReferenciaFamiliar() {
		return telefonoReferenciaFamiliar;
	}

	public void setTelefonoReferenciaFamiliar(String telefonoReferenciaFamiliar) {
		this.telefonoReferenciaFamiliar = telefonoReferenciaFamiliar;
	}
}
