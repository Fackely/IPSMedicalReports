package com.princetonsa.dto.administracion;

import java.io.Serializable;

import util.ConstantesBD;
import util.ValoresPorDefecto;

public class DtoEnvioEmailAutomatico implements Serializable {

	
	private int codigoPk; 
	private String funcionalidad;
	private String usuario;
	private int institucion;
	private String fechaModifica; 
	private String horaModifica;
	private String usuarioModifica;
	private String nombreCompleto;
    private boolean eliminado;
    private boolean existeBD;
	
	//Atributos de modificacion
    private String funcionalidadAnterior;
    private String usuarioAnterior;
    
    //Atributo usuario
    private String emailUsuario;
	
	 public DtoEnvioEmailAutomatico(){
		 this.clean();
		 
	 }
	
	
	 void clean(){
			codigoPk=0; 
			fechaModifica="";
			horaModifica="";
			usuarioModifica="";
			nombreCompleto="";
			eliminado=false;
			this.existeBD = false;
			
			this.funcionalidadAnterior = "";
			this.usuarioAnterior = "";
			this.emailUsuario="";
		}


	public String getEmailUsuario() {
		return emailUsuario;
	}


	public void setEmailUsuario(String emailUsuario) {
		this.emailUsuario = emailUsuario;
	}


	/**
	 * @return the codigoPk
	 */
	public int getCodigoPk() {
		return codigoPk;
	}


	/**
	 * @param codigoPk the codigoPk to set
	 */
	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}


	/**
	 * @return the funcionalidad
	 */
	public String getFuncionalidad() {
		return funcionalidad;
	}


	/**
	 * @param funcionalidad the funcionalidad to set
	 */
	public void setFuncionalidad(String funcionalidad) {
		this.funcionalidad = funcionalidad;
	}


	/**
	 * @return the usuario
	 */
	public String getUsuario() {
		return usuario;
	}


	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
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
	 * @return the fechaModifica
	 */
	public String getFechaModifica() {
		return fechaModifica;
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


	/**
	 * @return the nombreCompleto
	 */
	public String getNombreCompleto() {
		return nombreCompleto;
	}


	/**
	 * @param nombreCompleto the nombreCompleto to set
	 */
	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}


	/**
	 * @return the eliminado
	 */
	public boolean isEliminado() {
		return eliminado;
	}


	/**
	 * @param eliminado the eliminado to set
	 */
	public void setEliminado(boolean eliminado) {
		this.eliminado = eliminado;
	}


	/**
	 * @return the existeBD
	 */
	public boolean isExisteBD() {
		return existeBD;
	}


	/**
	 * @param existeBD the existeBD to set
	 */
	public void setExisteBD(boolean existeBD) {
		this.existeBD = existeBD;
	}


	/**
	 * @return the funcionalidadAnterior
	 */
	public String getFuncionalidadAnterior() {
		return funcionalidadAnterior;
	}


	/**
	 * @param funcionalidadAnterior the funcionalidadAnterior to set
	 */
	public void setFuncionalidadAnterior(String funcionalidadAnterior) {
		this.funcionalidadAnterior = funcionalidadAnterior;
	}


	/**
	 * @return the usuarioAnterior
	 */
	public String getUsuarioAnterior() {
		return usuarioAnterior;
	}


	/**
	 * @param usuarioAnterior the usuarioAnterior to set
	 */
	public void setUsuarioAnterior(String usuarioAnterior) {
		this.usuarioAnterior = usuarioAnterior;
	}
	
	/**
	 * M�todo para obtener el nombre de la funcionalidad
	 * @return
	 */
	public String getNombreFuncionalidad()
	{
		return ValoresPorDefecto.getIntegridadDominio(this.funcionalidad).toString();
	}

	
	
	public void setRegistroUsuario(String registroUsuario)
	{
		if(!registroUsuario.equals(""))
		{
			String[] vector = registroUsuario.split(ConstantesBD.separadorSplit);
			this.usuario = vector[0];
			this.nombreCompleto = vector[1];
		}
		else
		{
			this.usuario = "";
			this.nombreCompleto = "";
		}
	}
	
	public String getRegistroUsuario()
	{
		return this.usuario + ConstantesBD.separadorSplit + this.nombreCompleto;
	}
	
	
	
	
	
	
	
}
