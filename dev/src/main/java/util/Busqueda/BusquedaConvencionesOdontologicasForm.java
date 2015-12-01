package util.Busqueda;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.InfoDatosStr;
import util.UtilidadTexto;
import util.Utilidades;

public class BusquedaConvencionesOdontologicasForm extends ValidatorForm  {

	private String estado;
	private String linkSiguiente;
	//Atributos que envia el usuario
	private String hagoSubmit="";
	private String nombreForma="";
	private String filtroBusqueda;
	private String idDiv;
	private String idHiddenCodImg;
	private String idHiddenPathImg;
	private String idHiddenNomImg;
	private String idHiddenAnchoOrg;
	private String idHiddenAltoOrg;
	private int tipoConsulta;
	private String tipoConvencion;
	private boolean busquedaXTipo;
	private String pathImagen;
	private String nomImagen;
	private String codigoImagen;
	private int anchoImg;
	private int altoImg;
	private String activo;
	
	private ArrayList<InfoDatosStr> arrayConvenciones;

	
	
	

	
	public void BusquedaConvencionesOdontologicasForm()
	{
		this.reset();
	}
	
	
	public void reset()
	{
		this.estado=new String("");
		this.linkSiguiente=new String("");
		this.nombreForma=new String("");
		this.tipoConvencion="";
		this.busquedaXTipo=false;
		this.activo="";
		this.filtroBusqueda=new String("");		
        this.arrayConvenciones=new ArrayList<InfoDatosStr>();
        this.codigoImagen=new String("");
        this.idDiv=new String("");
        this.idHiddenCodImg=new String("");
        this.idHiddenPathImg=new String("");
        this.idHiddenNomImg=new String("");
        this.idHiddenAnchoOrg=new String("");
        this.idHiddenAltoOrg=new String("");
        this.tipoConsulta=ConstantesBD.codigoNuncaValido;
        this.pathImagen=new String("");
        this.nomImagen=new String("");
        this.anchoImg=ConstantesBD.codigoNuncaValido;
    	this.altoImg=ConstantesBD.codigoNuncaValido;
	}
	
	public void resetRequest(HttpServletRequest request)
	{
		this.tipoConvencion=request.getParameter("tipoConvencion")==null?"":(request.getParameter("tipoConvencion"));
		this.busquedaXTipo=request.getParameter("busquedaXTipo")==null?false:(UtilidadTexto.getBoolean(request.getParameter("busquedaXTipo")));
		this.activo=request.getParameter("activo")==null?"":(request.getParameter("activo"));
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
	 * @return the hagoSubmit
	 */
	public String getHagoSubmit() {
		return hagoSubmit;
	}


	/**
	 * @param hagoSubmit the hagoSubmit to set
	 */
	public void setHagoSubmit(String hagoSubmit) {
		this.hagoSubmit = hagoSubmit;
	}


	/**
	 * @return the nombreForma
	 */
	public String getNombreForma() {
		return nombreForma;
	}


	/**
	 * @param nombreForma the nombreForma to set
	 */
	public void setNombreForma(String nombreForma) {
		this.nombreForma = nombreForma;
	}


	/**
	 * @return the filtroBusqueda
	 */
	public String getFiltroBusqueda() {
		return filtroBusqueda;
	}


	/**
	 * @param filtroBusqueda the filtroBusqueda to set
	 */
	public void setFiltroBusqueda(String filtroBusqueda) {
		this.filtroBusqueda = filtroBusqueda;
	}


	/**
	 * @return the arrayConvenciones
	 */
	public ArrayList<InfoDatosStr> getArrayConvenciones() {
		return arrayConvenciones;
	}


	/**
	 * @param arrayConvenciones the arrayConvenciones to set
	 */
	public void setArrayConvenciones(ArrayList<InfoDatosStr> arrayConvenciones) {
		this.arrayConvenciones = arrayConvenciones;
	}


	/**
	 * @return the codigoImagen
	 */
	public String getCodigoImagen() {
		return codigoImagen;
	}


	/**
	 * @param codigoImagen the codigoImagen to set
	 */
	public void setCodigoImagen(String codigoImagen) {
		this.codigoImagen = codigoImagen;
	}


	/**
	 * @return the idDiv
	 */
	public String getIdDiv() {
		return idDiv;
	}


	/**
	 * @param idDiv the idDiv to set
	 */
	public void setIdDiv(String idDiv) {
		this.idDiv = idDiv;
	}


	/**
	 * @return the pathImagen
	 */
	public String getPathImagen() {
		return pathImagen;
	}


	/**
	 * @param pathImagen the pathImagen to set
	 */
	public void setPathImagen(String pathImagen) {
		this.pathImagen = pathImagen;
	}


	/**
	 * @return the nomImagen
	 */
	public String getNomImagen() {
		return nomImagen;
	}


	/**
	 * @param nomImagen the nomImagen to set
	 */
	public void setNomImagen(String nomImagen) {
		this.nomImagen = nomImagen;
	}


	/**
	 * @return the anchoImg
	 */
	public int getAnchoImg() {
		return anchoImg;
	}


	/**
	 * @param anchoImg the anchoImg to set
	 */
	public void setAnchoImg(int anchoImg) {
		this.anchoImg = anchoImg;
	}

	/**
	 * @return the altoImg
	 */
	public int getAltoImg() {
		return altoImg;
	}

	/**
	 * @param altoImg the altoImg to set
	 */
	public void setAltoImg(int altoImg) {
		this.altoImg = altoImg;
	}

	/**
	 * @return the tipoConsulta
	 */
	public int getTipoConsulta() {
		return tipoConsulta;
	}

	/**
	 * @param tipoConsulta the tipoConsulta to set
	 */
	public void setTipoConsulta(int tipoConsulta) {
		this.tipoConsulta = tipoConsulta;
	}


	/**
	 * @return the idHiddenCodImg
	 */
	public String getIdHiddenCodImg() {
		return idHiddenCodImg;
	}


	/**
	 * @param idHiddenCodImg the idHiddenCodImg to set
	 */
	public void setIdHiddenCodImg(String idHiddenCodImg) {
		this.idHiddenCodImg = idHiddenCodImg;
	}


	/**
	 * @return the idHiddenPathImg
	 */
	public String getIdHiddenPathImg() {
		return idHiddenPathImg;
	}


	/**
	 * @param idHiddenPathImg the idHiddenPathImg to set
	 */
	public void setIdHiddenPathImg(String idHiddenPathImg) {
		this.idHiddenPathImg = idHiddenPathImg;
	}


	/**
	 * @return the idHiddenNomImg
	 */
	public String getIdHiddenNomImg() {
		return idHiddenNomImg;
	}


	/**
	 * @param idHiddenNomImg the idHiddenNomImg to set
	 */
	public void setIdHiddenNomImg(String idHiddenNomImg) {
		this.idHiddenNomImg = idHiddenNomImg;
	}


	/**
	 * @return the idHiddenAnchoOrg
	 */
	public String getIdHiddenAnchoOrg() {
		return idHiddenAnchoOrg;
	}


	/**
	 * @param idHiddenAnchoOrg the idHiddenAnchoOrg to set
	 */
	public void setIdHiddenAnchoOrg(String idHiddenAnchoOrg) {
		this.idHiddenAnchoOrg = idHiddenAnchoOrg;
	}


	/**
	 * @return the idHiddenAltoOrg
	 */
	public String getIdHiddenAltoOrg() {
		return idHiddenAltoOrg;
	}


	/**
	 * @param idHiddenAltoOrg the idHiddenAltoOrg to set
	 */
	public void setIdHiddenAltoOrg(String idHiddenAltoOrg) {
		this.idHiddenAltoOrg = idHiddenAltoOrg;
	}


	/**
	 * @return the linkSiguiente
	 */
	public String getLinkSiguiente() {
		return linkSiguiente;
	}


	/**
	 * @param linkSiguiente the linkSiguiente to set
	 */
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}


	/**
	 * @return the tipoConvencion
	 */
	public String getTipoConvencion() {
		return tipoConvencion;
	}


	/**
	 * @param tipoConvencion the tipoConvencion to set
	 */
	public void setTipoConvencion(String tipoConvencion) {
		this.tipoConvencion = tipoConvencion;
	}


	/**
	 * @return the busquedaXTipo
	 */
	public boolean isBusquedaXTipo() {
		return busquedaXTipo;
	}


	/**
	 * @param busquedaXTipo the busquedaXTipo to set
	 */
	public void setBusquedaXTipo(boolean busquedaXTipo) {
		this.busquedaXTipo = busquedaXTipo;
	}


	
	
	
}
