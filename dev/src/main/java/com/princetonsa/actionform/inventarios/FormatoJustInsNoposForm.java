/**
 * 
 */
package com.princetonsa.actionform.inventarios;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadTexto;
import util.historiaClinica.ConstantesBDHistoriaClinica;

import com.princetonsa.dto.historiaClinica.DtoParamCamposJusNoPos;
import com.princetonsa.dto.historiaClinica.DtoParamJusNoPos;
import com.princetonsa.dto.historiaClinica.DtoParamSeccionesJusNoPos;

/**
 * @author axioma
 *
 */
public class FormatoJustInsNoposForm extends ValidatorForm
{

	/**
	 * Estado
	 */
	private String estado;
	
	/**
	 * Estado de la forma
	 */
	private String estadoAnterior="";
	

	/**
	 * Variable que indica que acciones se deben realizar en el estado empezar
	 */
	private String emisor;
	
	/**
	 *
	 */
	private String index;
	
	/**
	 *
	 */
	private boolean recordar;
	
	/**
	 *
	 */
	private String noJustificacion;
	
	/**
	 *
	 */
	private int numJus;
	
	/**
	 * Dto de la parametrización de la justificación
	 */
	private DtoParamJusNoPos dtoParam;
	
	/**
	 * Aqui se captura el diagnostico de complicación
	 * (Completo compuesto por codigo/CIE/nombre, 
	 * separado por "-")
	 */
	private String diagnosticoComplicacion_1="";
		
	/**
	 * Para manejar los diagnosticos definitivos principal y relacionados. El
	 * valor viene de forma 'codigo- nombre'
	 */
	private Map diagnosticosDefinitivos = new HashMap();
	
	/**
	 * Entero para saber cuantos diagnosticos definitivos se 
	 * generaron dinámicamente 
	 */
	private int numDiagnosticosDefinitivos = 0;
	
	/**
	 * Posicion de la seccion que contiene el campo seleccionado
	 */
	private String posicionSeccion="";
	
	/**
	 * Posicion del campo seleccionado
	 */
	private String posicionCampo="";
	
	
	/**
	 * Identificador html del campo seleccionado
	 */
	private String idHtmlCampo="";
	
	
	/**/
	private String articulo="";
	

	/**
	 * Método que resetea la forma
	 */
	public void reset(){
		this.dtoParam = new DtoParamJusNoPos();
		this.noJustificacion = "";
		this.numJus = ConstantesBD.codigoNuncaValido;
		diagnosticoComplicacion_1="";
		diagnosticosDefinitivos=new HashMap();
	}
	
	
	public int getNumDiagnosticosDefinitivos() {
		return numDiagnosticosDefinitivos;
	}

	public void setNumDiagnosticosDefinitivos(int numDiagnosticosDefinitivos) {
		this.numDiagnosticosDefinitivos = numDiagnosticosDefinitivos;
	}
	
	public Map getDiagnosticosDefinitivos() {
		return diagnosticosDefinitivos;
	}

	public void setDiagnosticosDefinitivos(Map diagnosticosDefinitivos) {
		this.diagnosticosDefinitivos = diagnosticosDefinitivos;
	}

	/**
	 * * Asigna un diagnostico definitivo (ppal o relacionado)
	 */
	public void setDiagnosticoDefinitivo(String key, Object value) 
	{
		diagnosticosDefinitivos.put(key, value);
	}
	
	/**
	 * Retorna el diagnostico definitivo (ppal o relacionado) asociado a la
	 * llave dada
	 */
	public Object getDiagnosticoDefinitivo(String key) 
	{
		return diagnosticosDefinitivos.get(key);
	}
	
	public String getDiagnosticoComplicacion_1() {
		return diagnosticoComplicacion_1;
	}

	public void setDiagnosticoComplicacion_1(String diagnosticoComplicacion_1) {
		this.diagnosticoComplicacion_1 = diagnosticoComplicacion_1;
	}

	/**
	 * Validación del form
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errors=new ActionErrors();
		
		// Validar Campos Requeridos al guardar la justificación No Pos
		if(this.estado.equals("guardar") || this.estado.equals("guardarModificacion") || this.estado.equals("guardarDeUna")){
			String codigoSeccionHija = "";
			for(int s=0; s<this.dtoParam.getSecciones().size(); s++){
				DtoParamSeccionesJusNoPos seccion = new DtoParamSeccionesJusNoPos();
				seccion = this.dtoParam.getSecciones().get(s);
				for(int c=0; c<seccion.getCampos().size(); c++){
					DtoParamCamposJusNoPos campo = new DtoParamCamposJusNoPos();
					campo = seccion.getCampos().get(c);
					if(UtilidadTexto.getBoolean(campo.getMostrar())&&UtilidadTexto.getBoolean(campo.getRequerido())){
					
						// Validar campos tipo Check
						if(campo.getTipoHtml().equals("CHEC")){
							boolean checkSeleccionado = false;
							for(int op=0; op<campo.getOpciones().size(); op++){
								if(UtilidadTexto.getBoolean(campo.getOpciones().get(op).getSeleccionado()))
									checkSeleccionado = true;
							}	
							if(!checkSeleccionado)
								errors.add("El campo "+campo.getEtiqueta()+" es requerido", new ActionMessage("errors.required", campo.getEtiqueta()));
						}
						
						// Validar campos tipo Radio
						else if(campo.getTipoHtml().equals("RADI")){
							for(int op=0; op<campo.getOpciones().size(); op++){
								if(!campo.getOpciones().get(op).getMostrarSeccion().isEmpty() && campo.getOpciones().get(op).getSeleccionado().equals(ConstantesBD.acronimoSi)){
									codigoSeccionHija = campo.getOpciones().get(op).getMostrarSeccion();
								}
							}
							
							if(campo.getValor().isEmpty()){
								errors.add("El campo "+campo.getEtiqueta()+" es requerido", new ActionMessage("errors.required", campo.getEtiqueta()));
								codigoSeccionHija = "";
							}
						}else if(campo.getTipoHtml().equals("TEXT")&&campo.getTipo().equals("2")){
							if(campo.getValor()==null||campo.getValor().trim().isEmpty()){
								errors.add("El campo "+campo.getEtiqueta()+" es requerido", new ActionMessage("errors.required", campo.getEtiqueta()));
							}
						}
						else if(campo.getTipoHtml().equals("FIJO")){
							if(campo.getCodigo().equals(ConstantesBDHistoriaClinica.codigoCampoJusNoPosDxPrincipal)&&campo.getValor().isEmpty()){
								errors.add("El campo "+campo.getEtiqueta()+" es requerido", new ActionMessage("errors.required", campo.getEtiqueta()));
							}
						}




						else if (campo.getValor().isEmpty() && !seccion.getSeccionPadre().isEmpty() && campo.getRequerido().equals("S")){

							//SE OBTIENE LA SECCION ANTERIOR QUE ES LA PADRE 
							DtoParamSeccionesJusNoPos seccionTmp = this.dtoParam.getSecciones().get(s-1);

							for(int c2=0; c2<seccionTmp.getCampos().size(); c2++){
								DtoParamCamposJusNoPos campoTmp = new DtoParamCamposJusNoPos();
								campoTmp = seccionTmp.getCampos().get(c2);
								if(UtilidadTexto.getBoolean(campoTmp.getRequerido())){
									if(campoTmp.getValor().equals("S")){
										errors.add("El campo "+campo.getEtiqueta()+" es requerido", new ActionMessage("errors.required", campo.getEtiqueta()));
									}
								}
							}

						}

						
						
						
						
						
						
						
						
						
						
						
						// Validar Campos de seccion hija 
						/*else if(seccion.getCodigo().equals(codigoSeccionHija) && campo.getValor().isEmpty()){
							errors.add("El campo "+campo.getEtiqueta()+" es requerido", new ActionMessage("errors.required", "777 "+campo.getEtiqueta()));
						}
						
						// Validar el resto de campos
						else if (campo.getValor().isEmpty() && seccion.getSeccionPadre().isEmpty())
							errors.add("El campo "+campo.getEtiqueta()+" es requerido", new ActionMessage("errors.required", campo.getEtiqueta()));*/

						if(!errors.isEmpty()){
							if(this.estado.equals("guardar")||this.estado.equals("guardarDeUna"))
								this.estado = "empezar";
							else if(this.estado.equals("guardarModificacion"))
								this.estado = "modificar";
						}	
					}
				}
			}
		}
		
		return errors;
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
	 * @return the dtoParam
	 */
	public DtoParamJusNoPos getDtoParam() {
		return dtoParam;
	}

	/**
	 * @param dtoParam the dtoParam to set
	 */
	public void setDtoParam(DtoParamJusNoPos dtoParam) {
		this.dtoParam = dtoParam;
	}

	/**
	 * @return the emisor
	 */
	public String getEmisor() {
		return emisor;
	}

	/**
	 * @return the index
	 */
	public String getIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(String index) {
		this.index = index;
	}

	/**
	 * @return the recordar
	 */
	public boolean isRecordar() {
		return recordar;
	}

	/**
	 * @param recordar the recordar to set
	 */
	public void setRecordar(boolean recordar) {
		this.recordar = recordar;
	}

	/**
	 * @param emisor the emisor to set
	 */
	public void setEmisor(String emisor) {
		this.emisor = emisor;
	}

	/**
	 * @return the noJustificacion
	 */
	public String getNoJustificacion() {
		return noJustificacion;
	}

	/**
	 * @param noJustificacion the noJustificacion to set
	 */
	public void setNoJustificacion(String noJustificacion) {
		this.noJustificacion = noJustificacion;
	}

	/**
	 * @return the numJus
	 */
	public int getNumJus() {
		return numJus;
	}

	/**
	 * @param numJus the numJus to set
	 */
	public void setNumJus(int numJus) {
		this.numJus = numJus;
	}


	public String getPosicionSeccion() {
		return posicionSeccion;
	}


	public void setPosicionSeccion(String posicionSeccion) {
		this.posicionSeccion = posicionSeccion;
	}


	public String getPosicionCampo() {
		return posicionCampo;
	}


	public void setPosicionCampo(String posicionCampo) {
		this.posicionCampo = posicionCampo;
	}


	public String getIdHtmlCampo() {
		return idHtmlCampo;
	}


	public void setIdHtmlCampo(String idHtmlCampo) {
		this.idHtmlCampo = idHtmlCampo;
	}


	public String getArticulo() {
		return articulo;
	}


	public void setArticulo(String articulo) {
		this.articulo = articulo;
	}
	
	public String getEstadoAnterior() {
		return estadoAnterior;
	}


	public void setEstadoAnterior(String estadoAnterior) {
		this.estadoAnterior = estadoAnterior;
	}	
	
}
