/*
 * Sep 22, 2008
 */
package com.princetonsa.dto.ordenesmedicas;

import java.io.Serializable;

import com.princetonsa.mundo.UsuarioBasico;

import util.ConstantesBD;
import util.InfoDatosInt;

/**
 * Data Transfer Object para almacenar los datos de la hemodialisis
 * @author Sebastián Gómez R.
 *
 */
public class DtoHemodialisis implements Serializable
{
	///********ATRIBUTOS PARA LA HEMODIALISIS**********
	private String consecutivoHemodialisis;
	private String tiempo;
	private String pesoSeco;
	private String pesoSecoAnterior;
	private InfoDatosInt tipoMembrana;
	private InfoDatosInt filtro;
	private InfoDatosInt filtroAnterior;
	private InfoDatosInt flujoBomba;
	private InfoDatosInt flujoDializado;
	private String up;
	private String upAnterior;
	private InfoDatosInt accesoVascular;
	private String anticoagulacion;
	private String pesoPre;
	private String pesoPreAnterior;
	private String pesoPos;
	private String pesoPosAnterior;
	private String consecutivoHemodialisisPadre;
	private String fechaModifica;
	private String horaModifica;
	private UsuarioBasico usuarioModifica;
	
	public DtoHemodialisis()
	{
		this.clean();
	}
	
	/**
	 * Método que realiza el reset de los campos del DTO
	 */
	public void clean()
	{
		///Atributos de la hemodialisis
		this.consecutivoHemodialisis = "";
		this.tiempo = "";
		this.pesoSeco = "";
		this.pesoSecoAnterior = "";
		this.tipoMembrana = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.filtro = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.filtroAnterior = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.flujoBomba = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.flujoDializado = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.up = "";
		this.upAnterior = "";
		this.accesoVascular = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.anticoagulacion = "";
		this.pesoPre = "";
		this.pesoPreAnterior = "";
		this.pesoPos = "";
		this.pesoPosAnterior = "";
		this.consecutivoHemodialisisPadre = "";
		this.fechaModifica = "";
		this.horaModifica = "";
		this.usuarioModifica = new UsuarioBasico();
	}
	
	/**
	 * @return the tiempo
	 */
	public String getTiempo() {
		return tiempo;
	}

	/**
	 * @param tiempo the tiempo to set
	 */
	public void setTiempo(String tiempo) {
		this.tiempo = tiempo;
	}

	/**
	 * @return the pesoSeco
	 */
	public String getPesoSeco() {
		return pesoSeco;
	}

	/**
	 * @param pesoSeco the pesoSeco to set
	 */
	public void setPesoSeco(String pesoSeco) {
		this.pesoSeco = pesoSeco;
	}

	/**
	 * @return the filtro
	 */
	public InfoDatosInt getFiltro() {
		return filtro;
	}

	/**
	 * @param filtro the filtro to set
	 */
	public void setFiltro(InfoDatosInt filtro) {
		this.filtro = filtro;
	}
	
	/**
	 * @return the filtro
	 */
	public int getCodigoFiltro() {
		return filtro.getCodigo();
	}

	/**
	 * @param filtro the filtro to set
	 */
	public void setCodigoFiltro(int filtro) {
		this.filtro.setCodigo(filtro);
	}
	
	/**
	 * @return the filtro
	 */
	public String getNombreFiltro() {
		return filtro.getNombre();
	}

	/**
	 * @param filtro the filtro to set
	 */
	public void setNombreFiltro(String filtro) {
		this.filtro.setNombre(filtro);
	}

	/**
	 * @return the flujoBomba
	 */
	public InfoDatosInt getFlujoBomba() {
		return flujoBomba;
	}

	/**
	 * @param flujoBomba the flujoBomba to set
	 */
	public void setFlujoBomba(InfoDatosInt flujoBomba) {
		this.flujoBomba = flujoBomba;
	}
	
	/**
	 * @return the flujoBomba
	 */
	public int getCodigoFlujoBomba() {
		return flujoBomba.getCodigo();
	}

	/**
	 * @param flujoBomba the flujoBomba to set
	 */
	public void setCodigoFlujoBomba(int flujoBomba) {
		this.flujoBomba.setCodigo(flujoBomba);
	}
	
	/**
	 * @return the flujoBomba
	 */
	public String getNombreFlujoBomba() {
		return flujoBomba.getNombre();
	}

	/**
	 * @param flujoBomba the flujoBomba to set
	 */
	public void setNombreFlujoBomba(String flujoBomba) {
		this.flujoBomba.setNombre(flujoBomba);
	}

	/**
	 * @return the flujoDializado
	 */
	public InfoDatosInt getFlujoDializado() {
		return flujoDializado;
	}

	/**
	 * @param flujoDializado the flujoDializado to set
	 */
	public void setFlujoDializado(InfoDatosInt flujoDializado) {
		this.flujoDializado = flujoDializado;
	}
	
	/**
	 * @return the flujoDializado
	 */
	public int getCodigoFlujoDializado() {
		return flujoDializado.getCodigo();
	}

	/**
	 * @param flujoDializado the flujoDializado to set
	 */
	public void setCodigoFlujoDializado(int flujoDializado) {
		this.flujoDializado.setCodigo(flujoDializado);
	}
	
	/**
	 * @return the flujoDializado
	 */
	public String getNombreFlujoDializado() {
		return flujoDializado.getNombre();
	}

	/**
	 * @param flujoDializado the flujoDializado to set
	 */
	public void setNombreFlujoDializado(String flujoDializado) {
		this.flujoDializado.setNombre(flujoDializado);
	}

	/**
	 * @return the up
	 */
	public String getUp() {
		return up;
	}

	/**
	 * @param up the up to set
	 */
	public void setUp(String up) {
		this.up = up;
	}
	


	/**
	 * @return the accesoVascular
	 */
	public InfoDatosInt getAccesoVascular() {
		return accesoVascular;
	}

	/**
	 * @param accesoVascular the accesoVascular to set
	 */
	public void setAccesoVascular(InfoDatosInt accesoVascular) {
		this.accesoVascular = accesoVascular;
	}
	
	/**
	 * @return the accesoVascular
	 */
	public int getCodigoAccesoVascular() {
		return accesoVascular.getCodigo();
	}

	/**
	 * @param accesoVascular the accesoVascular to set
	 */
	public void setCodigoAccesoVascular(int accesoVascular) {
		this.accesoVascular.setCodigo( accesoVascular);
	}
	
	/**
	 * @return the accesoVascular
	 */
	public String getNombreAccesoVascular() {
		return accesoVascular.getNombre();
	}

	/**
	 * @param accesoVascular the accesoVascular to set
	 */
	public void setNombreAccesoVascular(String accesoVascular) {
		this.accesoVascular.setNombre( accesoVascular);
	}

	/**
	 * @return the anticoagulacion
	 */
	public String getAnticoagulacion() {
		return anticoagulacion;
	}

	/**
	 * @param anticoagulacion the anticoagulacion to set
	 */
	public void setAnticoagulacion(String anticoagulacion) {
		this.anticoagulacion = anticoagulacion;
	}

	/**
	 * @return the pesoPre
	 */
	public String getPesoPre() {
		return pesoPre;
	}

	/**
	 * @param pesoPre the pesoPre to set
	 */
	public void setPesoPre(String pesoPre) {
		this.pesoPre = pesoPre;
	}

	/**
	 * @return the pesoPos
	 */
	public String getPesoPos() {
		return pesoPos;
	}

	/**
	 * @param pesoPos the pesoPos to set
	 */
	public void setPesoPos(String pesoPos) {
		this.pesoPos = pesoPos;
	}
	
	/**
	 * @return the consecutivoHemodialisisPadre
	 */
	public String getConsecutivoHemodialisisPadre() {
		return consecutivoHemodialisisPadre;
	}

	/**
	 * @param consecutivoHemodialisisPadre the consecutivoHemodialisisPadre to set
	 */
	public void setConsecutivoHemodialisisPadre(String consecutivoHemodialisisPadre) {
		this.consecutivoHemodialisisPadre = consecutivoHemodialisisPadre;
	}

	/**
	 * @return the consecutivoHemodialisis
	 */
	public String getConsecutivoHemodialisis() {
		return consecutivoHemodialisis;
	}

	/**
	 * @param consecutivoHemodialisis the consecutivoHemodialisis to set
	 */
	public void setConsecutivoHemodialisis(String consecutivoHemodialisis) {
		this.consecutivoHemodialisis = consecutivoHemodialisis;
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
	 * @return the pesoSecoAnterior
	 */
	public String getPesoSecoAnterior() {
		return pesoSecoAnterior;
	}

	/**
	 * @param pesoSecoAnterior the pesoSecoAnterior to set
	 */
	public void setPesoSecoAnterior(String pesoSecoAnterior) {
		this.pesoSecoAnterior = pesoSecoAnterior;
	}

	/**
	 * @return the filtroAnterior
	 */
	public InfoDatosInt getFiltroAnterior() {
		return filtroAnterior;
	}

	/**
	 * @param filtroAnterior the filtroAnterior to set
	 */
	public void setFiltroAnterior(InfoDatosInt filtroAnterior) {
		this.filtroAnterior = filtroAnterior;
	}
	
	/**
	 * @return the filtroAnterior
	 */
	public int getCodigoFiltroAnterior() {
		return filtroAnterior.getCodigo();
	}

	/**
	 * @param filtroAnterior the filtroAnterior to set
	 */
	public void setCodigoFiltroAnterior(int filtroAnterior) {
		this.filtroAnterior.setCodigo(filtroAnterior);
	}
	
	/**
	 * @return the filtroAnterior
	 */
	public String getNombreFiltroAnterior() {
		return filtroAnterior.getNombre();
	}

	/**
	 * @param filtroAnterior the filtroAnterior to set
	 */
	public void setNombreFiltroAnterior(String filtroAnterior) {
		this.filtroAnterior.setNombre(filtroAnterior);
	}

	/**
	 * @return the tipoMembrana
	 */
	public InfoDatosInt getTipoMembrana() {
		return tipoMembrana;
	}

	/**
	 * @param tipoMembrana the tipoMembrana to set
	 */
	public void setTipoMembrana(InfoDatosInt tipoMembrana) {
		this.tipoMembrana = tipoMembrana;
	}
	
	/**
	 * @return the tipoMembrana
	 */
	public int getCodigoTipoMembrana() {
		return tipoMembrana.getCodigo();
	}

	/**
	 * @param tipoMembrana the tipoMembrana to set
	 */
	public void setCodigoTipoMembrana(int tipoMembrana) {
		this.tipoMembrana.setCodigo(tipoMembrana);
	}
	
	/**
	 * @return the tipoMembrana
	 */
	public String getNombreTipoMembrana() {
		return tipoMembrana.getNombre();
	}

	/**
	 * @param tipoMembrana the tipoMembrana to set
	 */
	public void setNombreTipoMembrana(String tipoMembrana) {
		this.tipoMembrana.setNombre(tipoMembrana);
	}

	/**
	 * @return the upAnterior
	 */
	public String getUpAnterior() {
		return upAnterior;
	}

	/**
	 * @param upAnterior the upAnterior to set
	 */
	public void setUpAnterior(String upAnterior) {
		this.upAnterior = upAnterior;
	}

	/**
	 * @return the pesoPreAnterior
	 */
	public String getPesoPreAnterior() {
		return pesoPreAnterior;
	}

	/**
	 * @param pesoPreAnterior the pesoPreAnterior to set
	 */
	public void setPesoPreAnterior(String pesoPreAnterior) {
		this.pesoPreAnterior = pesoPreAnterior;
	}

	/**
	 * @return the pesoPosAnterior
	 */
	public String getPesoPosAnterior() {
		return pesoPosAnterior;
	}

	/**
	 * @param pesoPosAnterior the pesoPosAnterior to set
	 */
	public void setPesoPosAnterior(String pesoPosAnterior) {
		this.pesoPosAnterior = pesoPosAnterior;
	}
}
