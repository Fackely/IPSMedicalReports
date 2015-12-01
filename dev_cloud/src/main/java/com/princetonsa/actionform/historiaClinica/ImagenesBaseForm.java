package com.princetonsa.actionform.historiaClinica;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.dto.consultaExterna.DtoCitasNoRealizadas;
import com.princetonsa.dto.consultaExterna.DtoConceptoFacturaVaria;
import com.princetonsa.dto.consultaExterna.DtoServiciosCitas;
import com.princetonsa.dto.historiaClinica.DtoImagenBase;
import com.princetonsa.mundo.consultaExterna.Multas;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadFecha;
import util.ValoresPorDefecto;

/**
 * @author Jairo Gómez Fecha Septiembre de 2009
 */

public class ImagenesBaseForm extends ValidatorForm {
	// *************** Declaracion de variables ***************

	private String estado;
	
	private ResultadoBoolean mensaje;
	
	private ArrayList<DtoImagenBase> arrayImagenes;
	
	private String esOperacionExitosa;
	
	private DtoImagenBase dtoImagen;
	
	private Map documentosAdjuntosGenerados = new HashMap(); 
	
	private int numDocumentosAdjuntos = 0;
	
	private int posArrayDto;
	
	private String esConsulta;
	
	private String linkSiguiente;

	
	// ************ Fin Declaracion de variables **************

	/**
	 * Metodo que inicializa todas las variables.
	 */
	public void reset() {
		this.estado = "";
		this.mensaje = new ResultadoBoolean(false);
		this.arrayImagenes = new ArrayList<DtoImagenBase>();
		this.esOperacionExitosa = new String("");
		this.dtoImagen = new DtoImagenBase();
		this.numDocumentosAdjuntos = 0;
	    this.documentosAdjuntosGenerados.clear();
	    this.posArrayDto =  ConstantesBD.codigoNuncaValido;
	    this.esConsulta =  new String("");
	    this.linkSiguiente = "";
	}

	/**
	 * Validate
	 */
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errores = new ActionErrors();
		if (this.estado.equals("guardar") || this.estado.equals("guardarModificar"))
		{
			if (this.dtoImagen.getNombreImagen().equals(""))
				errores.add("", new ActionMessage("errors.required", "El campo Nombre Imágen Base"));
			if ((this.documentosAdjuntosGenerados.get("generado_0")+"").equals("") || this.documentosAdjuntosGenerados.get("generado_0") == null)
				errores.add("", new ActionMessage("errors.required", "El campo Imágen Adjunta"));
			
			if (!errores.isEmpty())
			{
				if (estado.equals("guardar"))
					this.setEstado("mostrarErroresGuardar");
				if (estado.equals("guardarModificar"))
					this.setEstado("mostrarErroresGuardarModificar");
			}
			
			
		}
			
		return errores;
	}

	// *************** Declaracion de Metodos Get y Set ***************

	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado
	 *            the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return the mensaje
	 */
	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	/**
	 * @param mensaje
	 *            the mensaje to set
	 */
	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}

	/**
	 * @return the arrayImagenes
	 */
	public ArrayList<DtoImagenBase> getArrayImagenes() {
		return arrayImagenes;
	}

	/**
	 * @param arrayImagenes the arrayImagenes to set
	 */
	public void setArrayImagenes(ArrayList<DtoImagenBase> arrayImagenes) {
		this.arrayImagenes = arrayImagenes;
	}

	/**
	 * @return the esOperacionExitosa
	 */
	public String getEsOperacionExitosa() {
		return esOperacionExitosa;
	}

	/**
	 * @param esOperacionExitosa the esOperacionExitosa to set
	 */
	public void setEsOperacionExitosa(String esOperacionExitosa) {
		this.esOperacionExitosa = esOperacionExitosa;
	}

	/**
	 * @return the dtoImagen
	 */
	public DtoImagenBase getDtoImagen() {
		return dtoImagen;
	}

	/**
	 * @param dtoImagen the dtoImagen to set
	 */
	public void setDtoImagen(DtoImagenBase dtoImagen) {
		this.dtoImagen = dtoImagen;
	}
	
	/**
	 * Retorna el nombre generado del documento adjunto
	 * @param key
	 * @return Object
	 */
	public Object getDocumentosAdjuntosGenerados(String key)
	{
		return documentosAdjuntosGenerados.get(key);
	}
	
	public void setDocumentosAdjuntosGenerados(String key, Object value) 
	{
		String val = (String) value;
	
		if (val != null) 
			val = val.trim();

		documentosAdjuntosGenerados.put(key, value);
	}

	/**
	 * @return the documentosAdjuntosGenerados
	 */
	public Map getDocumentosAdjuntosGenerados() {
		return documentosAdjuntosGenerados;
	}

	/**
	 * @param documentosAdjuntosGenerados the documentosAdjuntosGenerados to set
	 */
	public void setDocumentosAdjuntosGenerados(Map documentosAdjuntosGenerados) {
		this.documentosAdjuntosGenerados = documentosAdjuntosGenerados;
	}

	/**
	 * @return the numDocumentosAdjuntos
	 */
	public int getNumDocumentosAdjuntos() {
		return numDocumentosAdjuntos;
	}

	/**
	 * @param numDocumentosAdjuntos the numDocumentosAdjuntos to set
	 */
	public void setNumDocumentosAdjuntos(int numDocumentosAdjuntos) {
		this.numDocumentosAdjuntos = numDocumentosAdjuntos;
	}

	/**
	 * @return the posArrayDto
	 */
	public int getPosArrayDto() {
		return posArrayDto;
	}

	/**
	 * @param posArrayDto the posArrayDto to set
	 */
	public void setPosArrayDto(int posArrayDto) {
		this.posArrayDto = posArrayDto;
	}

	/**
	 * @return the esConsulta
	 */
	public String getEsConsulta() {
		return esConsulta;
	}

	/**
	 * @param esConsulta the esConsulta to set
	 */
	public void setEsConsulta(String esConsulta) {
		this.esConsulta = esConsulta;
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


	// ************* Fin Declaracion de Metodos Get y Set *************
}