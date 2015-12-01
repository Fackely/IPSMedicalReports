/*
 * Mayo 6, 2008
 */
package com.princetonsa.dto.historiaClinica.parametrizacion;

import java.io.Serializable;
import java.util.ArrayList;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.InfoDatosString;
import util.UtilidadTexto;

/**
 * Data Transfer Object: Campo PARAMETRIZABLE
 * @author Sebastián Gómez R.
 *
 */
public class DtoCampoParametrizable implements Cloneable, Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String codigoPK;
	private String codigo;
	private String nombre;
	private String etiqueta;
	private InfoDatosInt tipo;
	private int tamanio;
	private String signo;
	private InfoDatosInt unidad;
	private String valorPredeterminado;
	private double maximo;
	private double minimo;
	private int decimales;
	private int columnasOcupadas;
	private int orden;
	private boolean unicoXFila;
	private boolean requerido;
	private String formula;
	private int codigoInstitucion;
	private String tipoHtml;
	private boolean mostrar;
	private boolean mostrarModificacion;
	private InfoDatosString  usadoFormula = new InfoDatosString("", "","",false);
	private boolean resaltar;
	private String generarAlerta;

	
	//********************************************************************
	// Cambio Anexo 841
	private InfoDatosString imagenBase;
	private int anchoOrg;
	private int altoOrg; 
	private String manejaImagen;
	private String imagenAsociar;//codigo de la imagen base
	// Fin Cambio Anexo 841
	//********************************************************************
	
	/**
	 * Para el manejo de las distintas opciones que puede tener un campo
	 * si es de selección multiple
	 */
	private ArrayList<DtoOpcionCampoParam> opciones;
	
	//**********ATRIBUTOS QUE CORRESPONDEN AL VALOR DEL CAMPO CAPTURADO EN FORMULARIO*************************************
	private String valor; //valor capturado del campo
	private String valoresOpcion; //Valores ingresados de la opcion del campo
	private String nombreArchivoOriginal; //nombre del archivo original si el tipo de campo es FILE
	
	//********ATRIBUTOS QUE SOLO APLICAN PARA ESCALA (cuando el campo es de una escala)*************************************
	private boolean observacionesRequeridas;
	private String observaciones;
	//************************************************************************************************************************
	
	/**
	 * codigoPK de la parametrización del campo, puede ser de la tabla plantillas_campos_sec, componentes_campos_sec o escalas_campos_seccion
	 * Eso depende del elemento asociado del campo
	 */
	private String consecutivoParametrizacion;
	
	/**
	 * codigoPK del registro historico del campo, puede ser la tabla plantillas_pac_campos, escalas_campos_ingresos, plantillas_ing_campos, componentes_ingreso
	 */
	private String consecutivoHistorico;
	
	/**
	 * Campo para verificar si el campo fue registrado en historico
	 */
	private boolean historico;
	
	
	/**
	 * Permitir adicionar Secciones Asociadas
	 * */
	private boolean permitirAsociados;
	
	/**
	 * Atributo que contiene el codigoPk del registro
	 * de la tabla valores_plan_evo_camp
	 */
	private int codigoPkValorPlanEvoCamp;
	
	/**
	 * Se resetean los datos del DTO
	 *
	 */
	public void clean()
	{
		this.codigoPK = "";
		this.codigo = "";
		this.nombre = "";
		this.etiqueta = "";
		this.tipo = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.tamanio = ConstantesBD.codigoNuncaValido;
		this.signo = "";
		this.unidad = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.valorPredeterminado = "";
		this.maximo = ConstantesBD.codigoNuncaValido;
		this.minimo = ConstantesBD.codigoNuncaValido;
		this.decimales = 0;
		this.columnasOcupadas = 1;
		this.orden = ConstantesBD.codigoNuncaValido;
		this.unicoXFila = false;
		this.requerido = false;
		this.formula = "";
		this.codigoInstitucion = ConstantesBD.codigoNuncaValido;
		this.tipoHtml = "";
		this.mostrar = false;
		this.mostrarModificacion = false;
		
		this.opciones = new ArrayList<DtoOpcionCampoParam>();
		
		this.valor = "";
		this.nombreArchivoOriginal = "";
		this.valoresOpcion = "";
		
		//Atributos que solo aplican para escala
		this.observacionesRequeridas = false;
		this.observaciones = "";
		
		this.consecutivoParametrizacion = "";
		this.historico = false;
		this.usadoFormula =  new InfoDatosString("", "","",false);
		this.permitirAsociados = false;		
		this.resaltar = false;
		
		// Cambio Anexo 841
		this.imagenBase = new InfoDatosString ("","","");
		this.anchoOrg = ConstantesBD.codigoNuncaValido;
		this.altoOrg = ConstantesBD.codigoNuncaValido;
		this.manejaImagen = "" ;
		this.imagenAsociar = "";
		
		this.generarAlerta = "";
		this.consecutivoHistorico="";

	}
	
	/**
	 * Constructor del DTO
	 *
	 */
	public DtoCampoParametrizable()
	{
		this.clean();
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

	/**
	 * @return the codigoPK
	 */
	public String getCodigoPK() {
		return codigoPK;
	}

	/**
	 * @param codigoPK the codigoPK to set
	 */
	public void setCodigoPK(String codigoPK) {
		this.codigoPK = codigoPK;
	}

	/**
	 * @return the columnasOcupadas
	 */
	public int getColumnasOcupadas() {
		return columnasOcupadas;
	}

	/**
	 * @param columnasOcupadas the columnasOcupadas to set
	 */
	public void setColumnasOcupadas(int columnasOcupadas) {
		this.columnasOcupadas = columnasOcupadas;
	}

	/**
	 * @return the decimales
	 */
	public int getDecimales() {
		return decimales;
	}

	/**
	 * @param decimales the decimales to set
	 */
	public void setDecimales(int decimales) {
		this.decimales = decimales;
	}

	/**
	 * @return the etiqueta
	 */
	public String getEtiqueta() 
	{
		return etiqueta;
	}

	/**
	 * @param etiqueta the etiqueta to set
	 */
	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}

	/**
	 * @return the formula
	 */
	public String getFormulaCompleta() {
		return formula;
	}
	
	/**
	 * @return the formula
	 */
	public void setFormulaCompleta(String formula) {
		this.formula = formula;
	}
	
	/**
	 * @return the formula
	 */
	public String getFormula() {
		
		String temporal = "";		
		String [] cadena = formula.split(ConstantesBD.separadorSplit);
		
		for(int i = 0 ; i <cadena.length; i++)
		{			
			if(!cadena[i].equals(""))			
				temporal += cadena[i];		
		}
		
		return temporal;
	}

	/**
	 * @param formula the formula to set
	 */
	public void setFormula(String formula) {
		this.formula = formula;
	}

	/**
	 * @return the maximo
	 */
	public double getMaximo() {
		return maximo;
	}

	/**
	 * @param maximo the maximo to set
	 */
	public void setMaximo(double maximo) {
		this.maximo = maximo;
	}

	/**
	 * @return the minimo
	 */
	public double getMinimo() {
		return minimo;
	}

	/**
	 * @param minimo the minimo to set
	 */
	public void setMinimo(double minimo) {
		this.minimo = minimo;
	}

	/**
	 * @return the mostrar
	 */
	public boolean isMostrar() {
		return mostrar;
	}

	/**
	 * @param mostrar the mostrar to set
	 */
	public void setMostrar(boolean mostrar) {
		this.mostrar = mostrar;
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return the orden
	 */
	public int getOrden() {
		return orden;
	}

	/**
	 * @param orden the orden to set
	 */
	public void setOrden(int orden) {
		this.orden = orden;
	}

	/**
	 * @return the requerido
	 */
	public boolean isRequerido() {
		return requerido;
	}

	/**
	 * @param requerido the requerido to set
	 */
	public void setRequerido(boolean requerido) {
		this.requerido = requerido;
	}

	/**
	 * @return the signo
	 */
	public String getSigno() {
		return signo;
	}

	/**
	 * @param signo the signo to set
	 */
	public void setSigno(String signo) {
		this.signo = signo;
	}

	/**
	 * @return the tamanio
	 */
	public int getTamanio() {
		return tamanio;
	}

	/**
	 * @param tamanio the tamanio to set
	 */
	public void setTamanio(int tamanio) {
		this.tamanio = tamanio;
	}

	/**
	 * @return the tipo
	 */
	public int getCodigoTipo() {
		return tipo.getCodigo();
	}

	/**
	 * @param tipo the tipo to set
	 */
	public void setCodigoTipo(int tipo) {
		this.tipo.setCodigo(tipo);
	}
	
	/**
	 * @return the tipo
	 */
	public String getNombreTipo() {
		return tipo.getNombre();
	}

	/**
	 * @param tipo the tipo to set
	 */
	public void setNombreTipo(String tipo) {
		this.tipo.setNombre(tipo);
	}

	/**
	 * @return the tipoHtml
	 */
	public String getTipoHtml() {
		return tipoHtml;
	}

	/**
	 * @param tipoHtml the tipoHtml to set
	 */
	public void setTipoHtml(String tipoHtml) {
		this.tipoHtml = tipoHtml;
	}

	/**
	 * @return the unidad
	 */
	public int getCodigoUnidad() {
		return unidad.getCodigo();
	}

	/**
	 * @param unidad the unidad to set
	 */
	public void setCodigoUnidad(int unidad) {
		this.unidad.setCodigo(unidad);
	}
	
	/**
	 * @return the unidad
	 */
	public String getNombreUnidad() {
		return unidad.getNombre();
	}

	/**
	 * @param unidad the unidad to set
	 */
	public void setNombreUnidad(String unidad) {
		this.unidad.setNombre(unidad);
	}

	/**
	 * @return the valorPredeterminado
	 */
	public String getValorPredeterminado() {
		return valorPredeterminado;
	}

	/**
	 * @param valorPredeterminado the valorPredeterminado to set
	 */
	public void setValorPredeterminado(String valorPredeterminado) {
		this.valorPredeterminado = valorPredeterminado;
	}

	/**
	 * @return the nombreArchivoOriginal
	 */
	public String getNombreArchivoOriginal() {
		return nombreArchivoOriginal;
	}

	/**
	 * @param nombreArchivoOriginal the nombreArchivoOriginal to set
	 */
	public void setNombreArchivoOriginal(String nombreArchivoOriginal) {
		this.nombreArchivoOriginal = nombreArchivoOriginal;
	}

	/**
	 * @return the opciones
	 */
	public ArrayList<DtoOpcionCampoParam> getOpciones() {
		return opciones;
	}

	/**
	 * @param opciones the opciones to set
	 */
	public void setOpciones(ArrayList<DtoOpcionCampoParam> opciones) {
		this.opciones = opciones;
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
	 * @return the observacionesRequeridas
	 */
	public boolean isObservacionesRequeridas() {
		return observacionesRequeridas;
	}

	/**
	 * @param observacionesRequeridas the observacionesRequeridas to set
	 */
	public void setObservacionesRequeridas(boolean observacionesRequeridas) {
		this.observacionesRequeridas = observacionesRequeridas;
	}

	/**
	 * @return the unicoXFila
	 */
	public boolean isUnicoXFila() {
		return unicoXFila;
	}

	/**
	 * @param unicoXFila the unicoXFila to set
	 */
	public void setUnicoXFila(boolean unicoXFila) {
		this.unicoXFila = unicoXFila;
	}

	/**
	 * @return the consecutivoParametrizacion
	 */
	public String getConsecutivoParametrizacion() {
		return consecutivoParametrizacion;
	}

	/**
	 * @param consecutivoParametrizacion the consecutivoParametrizacion to set
	 */
	public void setConsecutivoParametrizacion(String consecutivoParametrizacion) {
		this.consecutivoParametrizacion = consecutivoParametrizacion;
	}

	/**
	 * @return the mostrarModificacion
	 */
	public boolean isMostrarModificacion() {
		return mostrarModificacion;
	}

	/**
	 * @param mostrarModificacion the mostrarModificacion to set
	 */
	public void setMostrarModificacion(boolean mostrarModificacion) {
		this.mostrarModificacion = mostrarModificacion;
	}
	
	/**
	 * Método que verifica si un campo checkvox fue llenado
	 * @param campo
	 * @return
	 */
	public boolean fueLlenadoCheckBox()
	{
		boolean llenado = false;
		
		//Se verifica que al menos una opcion del campo se haya chequeado
		for(DtoOpcionCampoParam opcion:this.getOpciones())
			if(UtilidadTexto.getBoolean(opcion.getSeleccionado()))
				llenado = true;
		
		return llenado;
	}

	/**
	 * @return the historico
	 */
	public boolean isHistorico() {
		return historico;
	}

	/**
	 * @param historico the historico to set
	 */
	public void setHistorico(boolean historico) {
		this.historico = historico;
	}


	/**
	 * @return the usadoFormula
	 */
	public boolean isUsadoFormula() {
		return usadoFormula.getActivo();
	}
	
	
	/**
	 * @param usadoFormula the usadoFormula to set
	 */
	public void setUsadoFormula(String codigoCampoFormula,String nombreCampoFormula,boolean usadoFormula) {
		this.usadoFormula.setActivo(usadoFormula);
		this.usadoFormula.setCodigo(codigoCampoFormula);
		this.usadoFormula.setNombre(nombreCampoFormula);
	}

	/**
	 * @return the usadoFormula
	 */
	public InfoDatosString getInfoFormulaMeUsa() {
		return usadoFormula;
	}

	/**
	 * @return the valoresOpcion
	 */
	public String getValoresOpcion() {
		return valoresOpcion;
	}

	/**
	 * @param valoresOpcion the valoresOpcion to set
	 */
	public void setValoresOpcion(String valoresOpcion) {
		this.valoresOpcion = valoresOpcion;
	}	
	
	/**
	 * Método para verificar si un campo select, radio o checkbos tiene opciones con valores asociados
	 * @return
	 */
	public boolean tieneValoresOpcionesCampo()
	{
		boolean tiene = false;
		
		for(DtoOpcionCampoParam opcion:this.opciones)
			if(!opcion.getListadoValoresOpcion().equals(""))
				tiene = true;
		
		return tiene;
	}

	/**
	 * @return the tipo
	 */
	public InfoDatosInt getTipo() {
		return tipo;
	}

	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(InfoDatosInt tipo) {
		this.tipo = tipo;
	}

	/**
	 * @return the unidad
	 */
	public InfoDatosInt getUnidad() {
		return unidad;
	}

	/**
	 * @param unidad the unidad to set
	 */
	public void setUnidad(InfoDatosInt unidad) {
		this.unidad = unidad;
	}

	/**
	 * @return the usadoFormula
	 */
	public InfoDatosString getUsadoFormula() {
		return usadoFormula;
	}

	/**
	 * @param usadoFormula the usadoFormula to set
	 */
	public void setUsadoFormula(InfoDatosString usadoFormula) {
		this.usadoFormula = usadoFormula;
	}

	/**
	 * @return the permitirAsociados
	 */
	public boolean isPermitirAsociados() {
		return permitirAsociados;
	}

	/**
	 * @param permitirAsociados the permitirAsociados to set
	 */
	public void setPermitirAsociados(boolean permitirAsociados) {
		this.permitirAsociados = permitirAsociados;
	}

	/**
	 * @return the resaltar
	 */
	public boolean isResaltar() {
		return resaltar;
	}

	/**
	 * @param resaltar the resaltar to set
	 */
	public void setResaltar(boolean resaltar) {
		this.resaltar = resaltar;
	}

	/**
	 * @return the manejaImagen
	 */
	public String getManejaImagen() {
		return manejaImagen;
	}

	/**
	 * @param manejaImagen the manejaImagen to set
	 */
	public void setManejaImagen(String manejaImagen) {
		this.manejaImagen = manejaImagen;
	}

	/**
	 * @return the imagenAsociar
	 */
	public String getImagenAsociar() {
		return imagenAsociar;
	}

	/**
	 * @param imagenAsociar the imagenAsociar to set
	 */
	public void setImagenAsociar(String imagenAsociar) {
		this.imagenAsociar = imagenAsociar;
	}

	/**
	 * @return the imagenBase
	 */
	public InfoDatosString getImagenBase() {
		return imagenBase;
	}

	/**
	 * @param imagenBase the imagenBase to set
	 */
	public void setImagenBase(InfoDatosString imagenBase) {
		this.imagenBase = imagenBase;
	}

	/**
	 * @return the anchoOrg
	 */
	public int getAnchoOrg() {
		return anchoOrg;
	}

	/**
	 * @param anchoOrg the anchoOrg to set
	 */
	public void setAnchoOrg(int anchoOrg) {
		this.anchoOrg = anchoOrg;
	}

	/**
	 * @return the altoOrg
	 */
	public int getAltoOrg() {
		return altoOrg;
	}

	/**
	 * @param altoOrg the altoOrg to set
	 */
	public void setAltoOrg(int altoOrg) {
		this.altoOrg = altoOrg;
	}

	/**
	 * @return the consecutivoHistorico
	 */
	public String getConsecutivoHistorico() {
		return consecutivoHistorico;
	}

	/**
	 * @param consecutivoHistorico the consecutivoHistorico to set
	 */
	public void setConsecutivoHistorico(String consecutivoHistorico) {
		this.consecutivoHistorico = consecutivoHistorico;
	}

	/**
	 * @return the generarAlerta
	 */
	public String getGenerarAlerta() {
		return generarAlerta;
	}

	/**
	 * @param generarAlerta the generarAlerta to set
	 */
	public void setGenerarAlerta(String generarAlerta) {
		this.generarAlerta = generarAlerta;
	}

	/**
	 * @param codigoPkValorPlanEvoCamp the codigoPkValorPlanEvoCamp to set
	 */
	public void setCodigoPkValorPlanEvoCamp(int codigoPkValorPlanEvoCamp) {
		this.codigoPkValorPlanEvoCamp = codigoPkValorPlanEvoCamp;
	}

	/**
	 * @return the codigoPkValorPlanEvoCamp
	 */
	public int getCodigoPkValorPlanEvoCamp() {
		return codigoPkValorPlanEvoCamp;
	}
	
}
