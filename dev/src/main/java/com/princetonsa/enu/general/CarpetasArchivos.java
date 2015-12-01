package com.princetonsa.enu.general;

import util.ValoresPorDefecto;

/**
 * 
 * Esta enumeraci&oacute;n se encarga de almacenar las rutas de cada una
 * de las carpetas para los documentos adjuntos.
 *
 * @author Sebasti&aacute;n G&oacute;mez
 * @since 10 Enero 2009
 * 
 * Modificado por Yennifer Guerrero (13 Ago 2010)
 *
 */
public enum CarpetasArchivos 
{
	IMAGENES("imagenesOdontologia"+System.getProperty("file.separator")+"imagen",false),
	TRAMAS("imagenesOdontologia"+System.getProperty("file.separator")+"tramas",false),
	CONVENCION("imagenesOdontologia"+System.getProperty("file.separator")+"convencion",false),
	IMAGENES_BASE("imagenesBase",false),
	IMAGENES_PLANTILLA_OPCION("imagenesPlantilla"+System.getProperty("file.separator")+"opcionparam",true),
	IMAGENES_INDICEPLACA("imagenesOdontologia"+System.getProperty("file.separator")+"indicePlaca",false),
	IMAGENES_ODONTODX("imagenesOdontologia"+System.getProperty("file.separator")+"odontoValo",false),
	IMAGENES_ODONTOEVO("imagenesOdontologia"+System.getProperty("file.separator")+"odontoEvo",false),
	IMAGENES_PRESUPUESTO_ODON("imagenesOdontologia"+System.getProperty("file.separator")+"presupuestoOdon",false),
	ADJUNTOS_CONSULTA_TRASLADOS_CAJA("adjuntosTesoreria"+System.getProperty("file.separator")+"consultaTrasladosCaja",false),
	REPORTES_JASPER("reportesPdfTemporal",false),
	NOTAS_ACLARATORIAS("upload"+System.getProperty("file.separator")+"notasAclaratorias",false),
//Windows	NOTAS_ACLARATORIAS("upload"+"/"+"notasAclaratorias",false),
	//IMAGENES_ODONTOTRATAMODON("imagenesOdontologia"+System.getProperty("file.separator")+"tratamientosOdontologicos",false);// Esta se utiliza para la versión vieja de tratamientos odontológicos
	IMAGENES_ODONTOTRATAMODON("imagenesOdontologia/odontoValo",false);
	
	;
	
	private CarpetasArchivos(String ruta,boolean adjunto) 
	{
		if(!adjunto)
		{
			this.ruta =  ".."+System.getProperty("file.separator")+ ruta+System.getProperty("file.separator");
			this.rutaFisica=  ValoresPorDefecto.getDirectorioAxiomaBase()+ ruta +System.getProperty("file.separator");
			this.rutaAbsoluta = System.getProperty("file.separator")+ ruta+System.getProperty("file.separator");
//windows	this.ruta =  ".."+"/"+ ruta+"/";
//windows	this.rutaFisica=  ValoresPorDefecto.getDirectorioAxiomaBase()+ ruta +"/";
//windows	this.rutaAbsoluta = "/"+ ruta+"/";
		}
		else
		{
			this.ruta =  System.getProperty("ADJUNTOS")+System.getProperty("file.separator")+ruta+System.getProperty("file.separator");
			this.rutaFisica=  ValoresPorDefecto.getFilePath()+ ruta+System.getProperty("file.separator");
			this.rutaAbsoluta = System.getProperty("ADJUNTOS")+System.getProperty("file.separator")+ruta+System.getProperty("file.separator");
		}
	}
	

	
	private String ruta;
	private String rutaFisica;
	private String rutaAbsoluta;
	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo ruta
	 * 
	 * @return  Retorna la variable ruta
	 */
	public String getRuta() {
		return ruta;
	}
	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo rutaFisica
	 * 
	 * @return  Retorna la variable rutaFisica
	 */
	public String getRutaFisica() {
		return rutaFisica;
	}
	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo rutaAbsoluta
	 * 
	 * @return  Retorna la variable rutaAbsoluta
	 */
	public String getRutaAbsoluta() {
		return rutaAbsoluta;
	}
	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo ruta
	 * 
	 * @param  valor para el atributo ruta 
	 */
	public void setRuta(String ruta) {
		this.ruta = ruta;
	}
	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo rutaFisica
	 * 
	 * @param  valor para el atributo rutaFisica 
	 */
	public void setRutaFisica(String rutaFisica) {
		this.rutaFisica = rutaFisica;
	}
	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo rutaAbsoluta
	 * 
	 * @param  valor para el atributo rutaAbsoluta 
	 */
	public void setRutaAbsoluta(String rutaAbsoluta) {
		this.rutaAbsoluta = rutaAbsoluta;
	}
	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 * del path concatenado con el atributo rutaAbsoluta
	 * @return  Retorna path+rutaAbsoluta
	 */
	public String getRutaAbsoluta(String path) {
		return path+""+rutaAbsoluta;
	}
	
	
	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 * del atributo rutaAbsoluta sin slash al final.
	 * 
	 * @autor Cristhian Murillo
	 * 
	 * @return  Retorna la variable rutaAbsoluta sin slash al final
	 */
	public String getRutaWeb() 
	{
		String rutaWeb = this.getRuta();
		rutaWeb = rutaWeb.replace("\\", "/");
		return rutaWeb;
	}

	
}
