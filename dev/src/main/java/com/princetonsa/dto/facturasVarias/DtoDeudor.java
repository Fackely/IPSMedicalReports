/*
 * Junio 27, 2009
 */
package com.princetonsa.dto.facturasVarias;

import java.io.Serializable;

import com.princetonsa.mundo.UsuarioBasico;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;

/**
 * 
 * DTO que almacena la información del Deudor
 * @author Sebastián Gómez R.
 *
 */
@SuppressWarnings("serial")
public class DtoDeudor  implements Serializable
{
	private String codigo;
	private String codigoEmpresa;
	private String codigoTercero;
	private String codigoPaciente;
	private String razonSocial;
	private String activo;
	private String direccion;
	private String telefono;
	private String email;
	private String diasVencimientoFac; // segun anexo 809
	private String nombreRepresentante;
	private String nombreContacto;
	private String tipoDeudor;
	private String observaciones;
	private UsuarioBasico usuarioModifica ;
	private int codigoInstitucion;
	//Estos atributos solo aplican para deudores OTROS
	private String tipoIdentificacion;
	private String numeroIdentificacion;
	private String primerApellido;
	private String primerNombre;
	private String segundoNombre;
	private String segundoApellido;
	
	//Atributos de validacion
	/**
	 * Para saber si el deudor ya existe como deudor empresa
	 */
	private boolean existeDeudorEmpresa;
	/**
	 * Para saber si el deudor ya existe como tercero
	 */
	private boolean existeDeudorTercero;
	/**
	 * Para saber si el deudor ya existe como paciente
	 */
	private boolean existeDeudorPaciente;
	
	/**
	 * Para saber si el deudor ya existe como paciente
	 */
	private boolean existeDeudorOtro;
	
	/**
	 * Para saber si el paciente es o no deudor
	 */
	private boolean existePacienteNoDeudor;
	
	/**
	 * Reset
	 */
	public void reset()
	{
		this.codigo = "";
		this.codigoTercero = "";
		this.codigoPaciente = "";
		this.codigoEmpresa = "";
		this.razonSocial = "";
		this.activo = "";
		this.direccion = "";
		this.telefono = "";
		this.email = "";
		this.diasVencimientoFac = "";
		this.tipoDeudor = "";
		this.nombreRepresentante = "";
		this.nombreContacto = "";
		this.observaciones = "";
		this.codigoInstitucion = ConstantesBD.codigoNuncaValido;
		this.usuarioModifica = new UsuarioBasico();
		
		//Validaciones
		this.existeDeudorEmpresa = false;
		this.existeDeudorTercero = false;
		this.existeDeudorPaciente = false;
		this.existeDeudorOtro = false;
		this.existePacienteNoDeudor = false;
		this.tipoIdentificacion = "";
		this.numeroIdentificacion = "";
		this.primerApellido="";
		this.segundoApellido = "";
		this.primerNombre="";
		this.segundoNombre="";
	}
	
	/**
	 * @return the usuarioModifica
	 */
	public UsuarioBasico getUsuarioModifica() {
		return usuarioModifica;
	}

	/**
	 * @param usuarioModifica the usuarioModifica to set
	 */
	public void setUsuarioModifica(UsuarioBasico usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
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

	/*
	 * Constructor
	 */
	public DtoDeudor()
	{
		this.reset();
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
	 * @return the codigoEmpresa
	 */
	public String getCodigoEmpresa() {
		return codigoEmpresa;
	}

	/**
	 * @param codigoEmpresa the codigoEmpresa to set
	 */
	public void setCodigoEmpresa(String codigoEmpresa) {
		this.codigoEmpresa = codigoEmpresa;
	}

	/**
	 * @return the codigoTercero
	 */
	public String getCodigoTercero() {
		return codigoTercero;
	}

	/**
	 * @param codigoTercero the codigoTercero to set
	 */
	public void setCodigoTercero(String codigoTercero) {
		this.codigoTercero = codigoTercero;
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
	 * @return the razonSocial
	 */
	public String getRazonSocial() {
		return razonSocial;
	}

	/**
	 * @param razonSocial the razonSocial to set
	 */
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
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
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the nombreRepresentante
	 */
	public String getNombreRepresentante() {
		return nombreRepresentante;
	}

	/**
	 * @param nombreRepresentante the nombreRepresentante to set
	 */
	public void setNombreRepresentante(String nombreRepresentante) {
		this.nombreRepresentante = nombreRepresentante;
	}

	/**
	 * @return the nombreContacto
	 */
	public String getNombreContacto() {
		return nombreContacto;
	}

	/**
	 * @param nombreContacto the nombreContacto to set
	 */
	public void setNombreContacto(String nombreContacto) {
		this.nombreContacto = nombreContacto;
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
	 * @return the existeDeudorEmpresa
	 */
	public boolean isExisteDeudorEmpresa() {
		return existeDeudorEmpresa;
	}

	/**
	 * @param existeDeudorEmpresa the existeDeudorEmpresa to set
	 */
	public void setExisteDeudorEmpresa(boolean existeDeudorEmpresa) {
		this.existeDeudorEmpresa = existeDeudorEmpresa;
	}

	/**
	 * @return the existeDeudorTercero
	 */
	public boolean isExisteDeudorTercero() {
		return existeDeudorTercero;
	}

	/**
	 * @param existeDeudorTercero the existeDeudorTercero to set
	 */
	public void setExisteDeudorTercero(boolean existeDeudorTercero) {
		this.existeDeudorTercero = existeDeudorTercero;
	}

	/**
	 * @return the existeDeudorPaciente
	 */
	public boolean isExisteDeudorPaciente() {
		return existeDeudorPaciente;
	}

	/**
	 * @param existeDeudorPaciente the existeDeudorPaciente to set
	 */
	public void setExisteDeudorPaciente(boolean existeDeudorPaciente) {
		this.existeDeudorPaciente = existeDeudorPaciente;
	}
	
	/**
	 * @param existePacienteNoDeudor the existePacienteNoDeudor to set
	 */
	public void setExistePacienteNoDeudor(boolean existePacienteNoDeudor) {
		this.existePacienteNoDeudor = existePacienteNoDeudor;
	}

	/**
	 * @return the existePacienteNoDeudor
	 */
	public boolean isExistePacienteNoDeudor() {
		return existePacienteNoDeudor;
	}
	
	/**
	 * Mï¿½todo para verificar sipuedo ingresar deudor
	 * @return
	 */
	public boolean isPuedoIngresarDeudor()
	{
		boolean validacion = true;
		
		if(this.tipoDeudor.equals(ConstantesIntegridadDominio.acronimoTipoDeudorOtros))
		{
			//Si el tercero ya existe como deudor tercero o empresa no se puede ingresar
			if(this.existeDeudorTercero||this.existeDeudorEmpresa )
			{
				validacion = false;
			}
		}
		else if(this.tipoDeudor.equals(ConstantesIntegridadDominio.acronimoTipoDeudorEmpresa))
		{
			//Si la empresa ya existe como deudor empresa no se puede ingresar
			if(this.existeDeudorEmpresa )
			{
				validacion = false;
			}
		}
		else if(this.tipoDeudor.equals(ConstantesIntegridadDominio.acronimoPaciente))
		{
			//Si el paciente ya existe como deudor paciente no se puede ingresar
			if(this.existeDeudorPaciente )
			{
				validacion = false;
			}
		}
		
		return validacion;
	}
	
	/**
	 * Edición del mensaje de valdiacion del ingreso del deudor
	 * @return
	 */
	public String getMensajeValidacionIngresoDeudor()
	{
		String mensaje = "";
		
		if(this.tipoDeudor.equals(ConstantesIntegridadDominio.acronimoTipoDeudorOtros))
		{
			//Si el tercero ya existe como deudor tercero o empresa se genera mensaje
			if(this.existeDeudorTercero)
			{
				mensaje = "Deudor "+this.razonSocial+" ya existe como tipo Deudor = Tercero";
			}
			else if(this.existeDeudorEmpresa)
			{
				mensaje = "Deudor "+this.razonSocial+" ya existe como tipo Deudor = Empresa";
			}
			
			else if(this.existeDeudorOtro)
			{
				mensaje = "Deudor "+this.razonSocial+" ya existe como tipo Otro, será actualizado a tipo Tercero";
			}
		}
		else if(this.tipoDeudor.equals(ConstantesIntegridadDominio.acronimoTipoDeudorEmpresa))
		{
			//Si la empresa ya existe como deudor empresa se genera mensaje
			if(this.existeDeudorEmpresa)
			{
				mensaje = "Deudor "+this.razonSocial+" ya existe";
			}
			else if(this.existeDeudorTercero)
			{
				mensaje = "Deudor "+this.razonSocial+" ya existe como tipo Tercero, será actualizado a tipo Empresa";
			}
			
			if(this.existeDeudorOtro)
			{
				mensaje = "Deudor "+this.razonSocial+" ya existe como tipo Otro, será actualizado a tipo Empresa";
			}
		}
		else if(this.tipoDeudor.equals(ConstantesIntegridadDominio.acronimoPaciente))
		{
			//Si el tercero ya existe como deudor tercero o empresa se genera mensaje
			if(this.existeDeudorTercero)
			{
				mensaje = "Deudor "+this.razonSocial+" ya existe como tipo Tercero, será actualizado a tipo Paciente";
			}
			
			if(this.existeDeudorOtro)
			{
				mensaje = "Deudor "+this.razonSocial+" ya existe como tipo Otro, será actualizado a tipo Paciente";
			}
			
			//Si el paciente ya existe como deudor paciente no se puede ingresar
			if(this.existeDeudorPaciente)
			{
				mensaje = "Deudor "+this.razonSocial+" ya existe";
			}
		}
	
		
		return mensaje;
	}
	
	
	/**
	 * Método para saber si debo pedir confirmacion para cambiar el tipo de deudor   
	 * @return
	 */
	public boolean isConfirmacionCambioTipoDeudor()
	{
		boolean confirmacion = false;
		if(this.tipoDeudor.equals(ConstantesIntegridadDominio.acronimoTipoDeudorEmpresa)&&this.existeDeudorTercero)
		{
			confirmacion=true;
		}
		if(this.tipoDeudor.equals(ConstantesIntegridadDominio.acronimoPaciente)&&this.existeDeudorTercero)
		{
			confirmacion=true;
		}
		
		
		if(this.tipoDeudor.equals(ConstantesIntegridadDominio.acronimoPaciente)&&this.existeDeudorOtro)
		{
			confirmacion=true;
		}
		
		if(this.tipoDeudor.equals(ConstantesIntegridadDominio.acronimoTipoDeudorEmpresa)&&this.existeDeudorOtro)
		{
			confirmacion=true;
		}
		
		if(this.tipoDeudor.equals(ConstantesIntegridadDominio.acronimoTipoDeudorOtros)&&this.existeDeudorOtro)
		{
			confirmacion=true;
		}
		
		return confirmacion;
	}/*

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
	 * @return the diasVencimientoFac
	 */
	public String getDiasVencimientoFac() {
		return diasVencimientoFac;
	}

	/**
	 * @param diasVencimientoFac the diasVencimientoFac to set
	 */
	public void setDiasVencimientoFac(String diasVencimientoFac) {
		this.diasVencimientoFac = diasVencimientoFac;
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
	 * @return the existeDeudorOtro
	 */
	public boolean isExisteDeudorOtro() {
		return existeDeudorOtro;
	}

	/**
	 * @param existeDeudorOtro the existeDeudorOtro to set
	 */
	public void setExisteDeudorOtro(boolean existeDeudorOtro) {
		this.existeDeudorOtro = existeDeudorOtro;
	}
	
}
