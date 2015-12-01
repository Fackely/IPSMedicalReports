package com.princetonsa.dto.historiaClinica.componentes;

import java.io.File;
import java.io.Serializable;

import org.apache.log4j.Logger;

import com.princetonsa.enu.general.CarpetasArchivos;

import util.ConstantesBD;
import util.Imagen;
import util.Utilidades;
import util.ValoresPorDefecto;


/**
 * Data Transfer Object: Usado para el manejo del componente Indice de Placa
 * @author Jose Eduardo Arias Doncel.
 *
 *Nota * Se contemplan las tablas
 *comp_indice_placa
 */
public class DtoIndicePlaca implements Serializable 
{	
	
	/**
	 * Defaul Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	private static Logger logger = Logger.getLogger(DtoIndicePlaca.class);
	
	/**
	 * 
	 * */
	public int institucion;
	
	/**
	 * 
	 * */
	public String codigoPk;
	
	/**
	 * 
	 * */
	public String nombreImagen;
	
	/**
	 * 
	 * */
	public int tamanoImagenX;
	
	/**
	 * 
	 * */
	public int tamanoImagenY;
	
	/**
	 * 
	 * */
	public String rutaImagen;
		
	/**
	 * 
	 * */
	public String porncentaje;
	
	/**
	 * 
	 * */
	public String interpretacion;
	
	/**
	 * 
	 * */
	public String plantillaIngreso;
	
	/**
	 * 
	 * */
	public String plantillaResProc;
	
	/**
	 * 
	 * */
	public String plantillaEvolucion;
	
	/**
	 * 
	 * */
	public String fechaModifica;
		
	/**
	 * 
	 * */
	public String horaModifica;
	
	/**
	 * 
	 * */
	public String usuarioModifica;
	
	/**
	 * guarda el mapa de pixeles 
	 * */
	public String indicePlacaPX;
	
	/**
	 * 
	 * */
	public boolean isImagenGenerada;
	
	/**
	 * guarfa el indice de placa en XML
	 * */
	public String indicePlacaXML;
	
	/**
	 * 
	 * */
	public String activoDienteAdulto;
	
	/**
	 * 
	 * */
	public String activoDienteNino;
	
	/**
	 * 
	 * */
	public String rangosInterpretacionXML;
	
	/**
	 * 
	 * */
	public int edadPaciente;
	
	/**
	 * 
	 * */
	public DtoIndicePlaca(int institucion,int edadPac)
	{
		clean(institucion,edadPac);
	}
	
	/**
	 * 
	 * */
	public DtoIndicePlaca()
	{
		clean(
				ConstantesBD.codigoNuncaValido,
				ConstantesBD.codigoNuncaValido);
	}
	
	/**
	 * limpia los atributos
	 * */
	public void clean(int inst,int edadPac)
	{
		codigoPk = ConstantesBD.codigoNuncaValido+"";
		rutaImagen = "";
		nombreImagen = "";
		porncentaje = "";
		interpretacion = "";
		plantillaIngreso = "";
		plantillaResProc = "";
		plantillaEvolucion = "";
		fechaModifica = "";
		horaModifica = "";
		usuarioModifica = "";
		
		isImagenGenerada = false;
		indicePlacaPX = "";
		activoDienteAdulto = ConstantesBD.acronimoSi;
		activoDienteNino = ConstantesBD.acronimoNo;
		rangosInterpretacionXML = "";
		
		institucion = inst;
		edadPaciente = edadPac;
		tamanoImagenX = 700;
		tamanoImagenY = 350;
		
		if(institucion > 0 && edadPaciente >= 0)
		{
			iniciarValidacionEdades();
		}
	}
	
	/**
	 * Inicializa los atributos de Edad Final Niñes
	 * y Edad Inicial Adulto
	 * */
	public void iniciarValidacionEdades()
	{
		int eia = Utilidades.convertirAEntero(ValoresPorDefecto.getEdadInicioAdulto(institucion).toString());
		int ein = Utilidades.convertirAEntero(ValoresPorDefecto.getEdadFinalNinez(institucion).toString());
		
		activoDienteAdulto = ConstantesBD.acronimoNo;
		activoDienteNino = ConstantesBD.acronimoNo;
		
		if(edadPaciente <= ein)
			activoDienteNino = ConstantesBD.acronimoSi;
		else if(edadPaciente >= eia)
			activoDienteAdulto = ConstantesBD.acronimoSi;
		else
		{
			activoDienteAdulto = ConstantesBD.acronimoSi;
			activoDienteNino = ConstantesBD.acronimoSi;
		}
	}
	
	/**
	 * Genera la imagen del indice de placa
	 * */
	public boolean generarImagen()
	{
		String nombreArchivo = "";
		
		try
		{
			if(Utilidades.convertirADouble(codigoPk) > 0)
			{
				nombreArchivo = "placadental_"+codigoPk;
				File archivo = new File(CarpetasArchivos.IMAGENES_INDICEPLACA.getRutaFisica(),nombreArchivo+".jpg");
				logger.info("Nombre archivo:"+nombreArchivo+" ruta: "+CarpetasArchivos.IMAGENES_INDICEPLACA.getRutaFisica()+" "+archivo.getName());
				
		        if(!Imagen.exportar(
		                indicePlacaPX,
		                tamanoImagenX,
		                tamanoImagenY,
		                archivo))
		        {
		            logger.info("Error exportando la imagen");
		            return false;
		        }
		        else
		        	return true;
			}
			else
			{
				logger.info("error el codigo pk es menor a cero ");
			}
		}
		catch(Exception ioe)
		{
		   logger.info("Error exportando la imagen: "+ioe.getMessage());
		}
		
		return false;
	}
	

	public String getCodigoPk() {
		return codigoPk;
	}

	public void setCodigoPk(String codigoPk) {
		this.codigoPk = codigoPk;
	}

	public String getRutaImagen() {
		return rutaImagen;
	}

	public void setRutaImagen(String rutaImagen) {
		this.rutaImagen = rutaImagen;
	}

	public String getPorncentaje() {
		return porncentaje;
	}

	public void setPorncentaje(String porncentaje) {
		this.porncentaje = porncentaje;
	}

	public String getInterpretacion() {
		return interpretacion;
	}

	public void setInterpretacion(String interpretacion) {
		this.interpretacion = interpretacion;
	}

	public String getPlantillaIngreso() {
		return plantillaIngreso;
	}

	public void setPlantillaIngreso(String plantillaIngreso) {
		this.plantillaIngreso = plantillaIngreso;
	}

	public String getPlantillaResProc() {
		return plantillaResProc;
	}

	public void setPlantillaResProc(String plantillaResProc) {
		this.plantillaResProc = plantillaResProc;
	}

	public String getPlantillaEvolucion() {
		return plantillaEvolucion;
	}

	public void setPlantillaEvolucion(String plantillaEvolucion) {
		this.plantillaEvolucion = plantillaEvolucion;
	}

	public String getFechaModifica() {
		return fechaModifica;
	}

	public void setFechaModifica(String fechaModifica) {
		this.fechaModifica = fechaModifica;
	}

	public String getHoraModifica() {
		return horaModifica;
	}

	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}

	public String getUsuarioModifica() {
		return usuarioModifica;
	}

	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

	public String getIndicePlacaPX() {
		return indicePlacaPX;
	}

	public void setIndicePlacaPX(String indicePlacaPX) {
		this.indicePlacaPX = indicePlacaPX;
	}

	public boolean isImagenGenerada() {
		return isImagenGenerada;
	}

	public void setImagenGenerada(boolean isImagenGenerada) {
		this.isImagenGenerada = isImagenGenerada;
	}

	public String getIndicePlacaXML() {
		return indicePlacaXML;
	}

	public void setIndicePlacaXML(String indicePlacaXML) {
		this.indicePlacaXML = indicePlacaXML;
	}

	public String getActivoDienteAdulto() {
		return activoDienteAdulto;
	}

	public void setActivoDienteAdulto(String activoDienteAdulto) {
		this.activoDienteAdulto = activoDienteAdulto;
	}

	public String getActivoDienteNino() {
		return activoDienteNino;
	}

	public void setActivoDienteNino(String activoDienteNino) {
		this.activoDienteNino = activoDienteNino;
	}

	public String getRangosInterpretacionXML() {
		return rangosInterpretacionXML;
	}

	public void setRangosInterpretacionXML(String rangosInterpretacionXML) {
		this.rangosInterpretacionXML = rangosInterpretacionXML;
	}

	public String getNombreImagen() {
		return nombreImagen;
	}

	public void setNombreImagen(String nombreImagen) {
		this.nombreImagen = nombreImagen;
	}
	
}
