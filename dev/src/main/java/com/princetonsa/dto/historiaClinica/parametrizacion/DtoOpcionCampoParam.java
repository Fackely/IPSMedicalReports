/*
 * Mayo 6, 2008
 */
package com.princetonsa.dto.historiaClinica.parametrizacion;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.princetonsa.enu.general.CarpetasArchivos;
import com.princetonsa.mundo.odontologia.ConvencionesOdontologicas;

import util.ConstantesBD;
import util.Imagen;
import util.InfoDatosStr;
import util.InfoDatosString;

/**
 * Data Transfer Object: Opcion campo parametrizable
 * @author Sebastián Gómez R.
 *
 */
public class DtoOpcionCampoParam implements Serializable
{
	
	private static Logger logger = Logger.getLogger(DtoOpcionCampoParam.class);
	
	private String codigoPk;
	private String codigoPkCampoParam;
	private String opcion;
	private String valor;
	private String valoresOpcionRegistrado; //corresponde a los valores opcion ya registrados
	private String seleccionado;
	//****************************************
	// Anexo 841
	private InfoDatosString convencionOdon;
	private int anchoImgOrg;
	private int altoImgOrg;
	private String pixelImg;
	private String xmlImg;
	private String nombreImgOpcion;
	private String rutaImgOpcion;
	// Fin Anexo 841
	//****************************************
	private String codigoHistorico;
	
	private String codConvencion;// aplica en plantilla paciente
	
	//*****Atributos para el manejo de  secciones por valor*******************
	private ArrayList<DtoSeccionParametrizable> secciones;
	//*****Atributos para el manejo de valores de la opcion
	private ArrayList<DtoValorOpcionCampoParam> valoresOpcion;
	
	/**
	 * Se resetean datos del DTO
	 *
	 */
	public void clean()
	{		
		this.codigoPk = "";
		this.codigoPkCampoParam = "";
		this.opcion = "";
		this.valor = "";
		this.valoresOpcionRegistrado = "";
		this.seleccionado = ConstantesBD.acronimoNo;
		this.codigoHistorico="";
		this.codConvencion="";
		
		this.secciones = new ArrayList<DtoSeccionParametrizable>();
		this.valoresOpcion = new ArrayList<DtoValorOpcionCampoParam>();
		
		// Anexo 841
		this.convencionOdon = new InfoDatosString();
		this.anchoImgOrg = ConstantesBD.codigoNuncaValido;
		this.altoImgOrg = ConstantesBD.codigoNuncaValido;
		this.pixelImg = "";
		this.xmlImg = "";
		this.nombreImgOpcion  = "";
		this.rutaImgOpcion = "";
	}
	
	/**
	 * @return the secciones
	 */
	public ArrayList<DtoSeccionParametrizable> getSecciones() {
		return secciones;
	}

	/**
	 * @param secciones the secciones to set
	 */
	public void setSecciones(ArrayList<DtoSeccionParametrizable> secciones) {
		this.secciones = secciones;
	}

	/**
	 * @return the valoresOpcion
	 */
	public ArrayList<DtoValorOpcionCampoParam> getValoresOpcion() {
		return valoresOpcion;
	}

	/**
	 * @param valoresOpcion the valoresOpcion to set
	 */
	public void setValoresOpcion(ArrayList<DtoValorOpcionCampoParam> valoresOpcion) {
		this.valoresOpcion = valoresOpcion;
	}
	
	/**
	 * Método para retornar el listado de los valores de la opcion
	 * @return
	 */
	public String getListadoValoresOpcion()
	{
		String listado = "";
		for(DtoValorOpcionCampoParam elemento:this.valoresOpcion)
			if(elemento.isMostrarModificacion())
				listado += (listado.length()>0?" - ":"") + elemento.getValor();
		return listado;
	}
	
	/**
	 * Método para retornar el listado de los codigos de las secciones que aplican para el campo
	 * @return
	 */
	public String getListadoSecciones()
	{
		String listado = "";
		for(DtoSeccionParametrizable elemento:this.secciones)
			if(elemento.isMostrarModificacion())
				listado += (listado.length()>0?ConstantesBD.separadorSplit:"") + elemento.getCodigoPK() + "$$" + elemento.getDescripcion()  ;
		return listado;
	}
	
	
	/**
	 * Genera la imagen a partir del mapa de pixeles
	 * */
	public InfoDatosStr generarImagen(String codigoImagen)
	{
		InfoDatosStr info = new InfoDatosStr();
		info.setIndicador(false);
		
		try
		{
			if(!this.pixelImg.equals(""))
			{
				info.setCodigo("opcionparam_"+codigoImagen);
				File archivo = new File(CarpetasArchivos.IMAGENES_PLANTILLA_OPCION.getRutaFisica(),info.getCodigo()+".jpg");
				
				logger.info("Nombre archivo para imagen Opcion:"+info.getCodigo()+" ruta: "+CarpetasArchivos.IMAGENES_PLANTILLA_OPCION.getRutaFisica()+" "+archivo.getName());
				
		        if(!Imagen.exportar(
		        		this.pixelImg,
		                700,
		                320,
		                archivo))
		        {
		            logger.info("Error exportando la imagen");
		            info.setIndicador(false);
		        }
		        else
		        {
		        	info.setCodigo(info.getCodigo()+".jpg");
		        	info.setIndicador(true);
		        }
		        
			}
			else
				logger.info("no Hay Informacion de imagenes");
		}
		catch(Exception ioe)
		{
		   logger.info("Error exportando la imagen: "+ioe.getMessage());
		}
		
		return (info);
		
	}

	/**
	 * Constructor
	 *
	 */
	public DtoOpcionCampoParam()
	{
		this.clean();
	}

	/**
	 * @return the opcion
	 */
	public String getOpcion() {
		return opcion;
	}

	/**
	 * @param opcion the opcion to set
	 */
	public void setOpcion(String opcion) {
		this.opcion = opcion;
	}

	/**
	 * @return the seleccionado
	 */
	public String getSeleccionado() {
		return seleccionado;
	}

	/**
	 * @param seleccionado the seleccionado to set
	 */
	public void setSeleccionado(String seleccionado) {
		this.seleccionado = seleccionado;
	}

	/**
	 * @return the valor
	 */
	public String getValor() {
		return valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(String valor) {
		this.valor = valor;
	}

	/**
	 * @return the codigoPk
	 */
	public String getCodigoPk() {
		return codigoPk;
	}

	/**
	 * @param codigoPk the codigoPk to set
	 */
	public void setCodigoPk(String codigoPk) {
		this.codigoPk = codigoPk;
	}

	/**
	 * @return the codigoPkCampoParam
	 */
	public String getCodigoPkCampoParam() {
		return codigoPkCampoParam;
	}

	/**
	 * @param codigoPkCampoParam the codigoPkCampoParam to set
	 */
	public void setCodigoPkCampoParam(String codigoPkCampoParam) {
		this.codigoPkCampoParam = codigoPkCampoParam;
	}

	/**
	 * @return the valoresOpcionRegistrado
	 */
	public String getValoresOpcionRegistrado() {
		return valoresOpcionRegistrado;
	}

	/**
	 * @param valoresOpcionRegistrado the valoresOpcionRegistrado to set
	 */
	public void setValoresOpcionRegistrado(String valoresOpcionRegistrado) {
		this.valoresOpcionRegistrado = valoresOpcionRegistrado;
	}

	/**
	 * @return the convencionOdon
	 */
	public InfoDatosString getConvencionOdon() {
		return convencionOdon;
	}

	/**
	 * @param convencionOdon the convencionOdon to set
	 */
	public void setConvencionOdon(InfoDatosString convencionOdon) {
		this.convencionOdon = convencionOdon;
	}

	/**
	 * @return the anchoImgOrg
	 */
	public int getAnchoImgOrg() {
		return anchoImgOrg;
	}

	/**
	 * @param anchoImgOrg the anchoImgOrg to set
	 */
	public void setAnchoImgOrg(int anchoImgOrg) {
		this.anchoImgOrg = anchoImgOrg;
	}

	/**
	 * @return the altoImgOrg
	 */
	public int getAltoImgOrg() {
		return altoImgOrg;
	}

	/**
	 * @param altoImgOrg the altoImgOrg to set
	 */
	public void setAltoImgOrg(int altoImgOrg) {
		this.altoImgOrg = altoImgOrg;
	}

	public String getPixelImg() {
		return pixelImg;
	}

	public void setPixelImg(String pixelImg) {
		this.pixelImg = pixelImg;
	}

	public String getXmlImg() {
		return xmlImg;
	}

	public void setXmlImg(String xmlImg) {
		this.xmlImg = xmlImg;
	}

	public String getNombreImgOpcion() {
		return nombreImgOpcion;
	}

	public void setNombreImgOpcion(String nombreImgOpcion) {
		this.nombreImgOpcion = nombreImgOpcion;
	}

	public String getRutaImgOpcion() {
		return rutaImgOpcion;
	}

	public void setRutaImgOpcion(String rutaImgOpcion) {
		this.rutaImgOpcion = rutaImgOpcion;
	}

	/**
	 * @return the codigoHistorico
	 */
	public String getCodigoHistorico() {
		return codigoHistorico;
	}

	/**
	 * @param codigoHistorico the codigoHistorico to set
	 */
	public void setCodigoHistorico(String codigoHistorico) {
		this.codigoHistorico = codigoHistorico;
	}

	/**
	 * @return the codConvencion
	 */
	public String getCodConvencion() {
		return codConvencion;
	}

	/**
	 * @param codConvencion the codConvencion to set
	 */
	public void setCodConvencion(String codConvencion) {
		this.codConvencion = codConvencion;
	}
	
}