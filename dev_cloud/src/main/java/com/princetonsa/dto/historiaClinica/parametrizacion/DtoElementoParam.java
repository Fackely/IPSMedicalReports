package com.princetonsa.dto.historiaClinica.parametrizacion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.InfoDatosString;
import util.Utilidades;

/**
 * Data Transfer Object: DTO que define de forma genérica un elemento parametrizable de la seccionFija
 * el elemento puede ser una Seccion, Componente o Escala
 * @author Sebastián Gómez R.
 *
 */
public class DtoElementoParam implements Serializable
{
	private String codigoPK;
	private String codigo;
	private String descripcion;
	private int orden;
	private boolean visible;
	private boolean mostrarModificacion;
	private int codigoInstitucion;
	//Atributos que identifican el tipo de elemento parametrizable
	private boolean escala;
	private boolean seccion;
	private boolean componente;
	
	//Aplica para secciones, y escalas
	private ArrayList<DtoSeccionParametrizable> secciones;
	//Aplica solo para componentes
	private ArrayList<DtoElementoParam> elementos;
	//Aplica solo para secciones
	private ArrayList<DtoCampoParametrizable> campos;
	
	/**
	 * Indica el estado de la seccion desplegable
	 */
	private boolean abierto;
	/**
	 * Indica si siempre se debe mostrar la seccion abierta
	 * */
	private boolean siempreAbierta = false;
	
	
	//Atributos que solo aplican para escalas
	private InfoDatosString factorPrediccion;
	private double totalEscala;
	/**
	 * Maneja las llaves:
	 * adjEscalaOriginal<codigoPkEscala>_<pos>
	 * adjEscala<codigoPkEscala>_<pos>
	 * numRegistros
	 */
	private HashMap<String, Object> archivosAdjuntos = new HashMap<String, Object>();
	
	/**
	 * codigoPK que puede ser de la tabla plantillas_secciones, plantillas_componentes o plantillas_escalas
	 * dependiendo el tipo de elemento
	 */
	private String consecutivoParametrizacion;
	
	/**
	 * codigoPK que puede ser de la tabal escalas_ingresos
	 */
	private String consecutivoHistorico;
	
	/**
	 * Resetea los datos del DTO
	 *
	 */
	public void clean()
	{
		this.codigoPK = "";
		this.codigo = "";
		this.descripcion = "";
		this.orden = ConstantesBD.codigoNuncaValido;
		this.visible = false;
		this.mostrarModificacion = false;
		this.codigoInstitucion = ConstantesBD.codigoNuncaValido;
		this.escala = false;
		this.seccion = false;
		this.componente = false;
		
		
		this.secciones = new ArrayList<DtoSeccionParametrizable>();
		this.elementos = new ArrayList<DtoElementoParam>();
		this.campos = new ArrayList<DtoCampoParametrizable>();
		
		this.abierto = false;
		this.siempreAbierta = false;
		
		this.factorPrediccion = new InfoDatosString("","");
		this.totalEscala = 0;
		this.archivosAdjuntos = new HashMap<String, Object>();
		
		this.consecutivoParametrizacion = "";
		
		this.consecutivoHistorico = "";
	}
	
	/**
	 * @return the archivosAdjuntos
	 */
	public HashMap<String,Object> getArchivosAdjuntos() {
		return archivosAdjuntos;
	}

	/**
	 * @param archivosAdjuntos the archivosAdjuntos to set
	 */
	public void setArchivosAdjuntos(HashMap<String,Object> archivosAdjuntos) {
		this.archivosAdjuntos = archivosAdjuntos;
	}
	
	/**
	 * @return the archivosAdjuntos
	 */
	public Object getArchivosAdjuntos(String key) {
		return archivosAdjuntos.get(key);
	}

	/**
	 * @param archivosAdjuntos the archivosAdjuntos to set
	 */
	public void setArchivosAdjuntos(String key,Object obj) {
		this.archivosAdjuntos.put(key, obj);
	}
	
	/**
	 * Método para obtener el número de archivos adjuntos
	 * @return
	 */
	public int getNumArchivosAdjuntos()
	{
		return Utilidades.convertirAEntero(this.getArchivosAdjuntos("numRegistros")+"",true);
	}
	
	/**
	 * Método para asignar el tamaño de un archivo adjunto
	 * @param numRegistros
	 */
	public void setNumArchivosAdjuntos(int numRegistros)
	{
		this.setArchivosAdjuntos("numRegistros", numRegistros);
	}

	/**
	 * Constructor del DTO
	 *
	 */
	public DtoElementoParam()
	{
		this.clean();
	}
	
	
	/**
	 * @return the componente
	 */
	public boolean isComponente() {
		return componente;
	}

	/**
	 * @param componente the componente to set
	 */
	public void setComponente(boolean componente) {
		this.componente = componente;
	}

	/**
	 * @return the escala
	 */
	public boolean isEscala() {
		return escala;
	}

	/**
	 * @param escala the escala to set
	 */
	public void setEscala(boolean escala) {
		this.escala = escala;
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
	 * @return the seccion
	 */
	public boolean isSeccion() {
		return seccion;
	}

	/**
	 * @param seccion the seccion to set
	 */
	public void setSeccion(boolean seccion) {
		this.seccion = seccion;
	}

	/**
	 * @return the visible
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * @param visible the visible to set
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
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
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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
	 * @return the campos
	 */
	public ArrayList<DtoCampoParametrizable> getCampos() {
		return campos;
	}

	/**
	 * @param campos the campos to set
	 */
	public void setCampos(ArrayList<DtoCampoParametrizable> campos) {		
		this.campos = campos;
	}
	
	public DtoCampoParametrizable getCampoParametrizable(String codigoPk)
	{
		for(int i=0; i<this.campos.size(); i++)
		{
			if(this.campos.get(i).getCodigoPK().equals(codigoPk))
				return this.campos.get(i); 
		}
		
		return null;
	}
	

	/**
	 * @return the escalas
	 */
	public ArrayList<DtoElementoParam> getElementos() {
		return elementos;
	}

	/**
	 * @param escalas the escalas to set
	 */
	public void setElementos(ArrayList<DtoElementoParam> elementos) {
		this.elementos = elementos;
	}

	/**
	 * @return the secciones
	 */
	public ArrayList<DtoSeccionParametrizable> getSecciones() {
		return secciones;
	}
	
	/**
	 * @return the secciones
	 */
	public DtoSeccionParametrizable getSeccionesPos(int index) {
		return secciones.get(index);
	}

	/**
	 * @param secciones the secciones to set
	 */
	public void setSecciones(ArrayList<DtoSeccionParametrizable> secciones) {
		this.secciones = secciones;
	}

	/**
	 * @return the abierto
	 */
	public boolean isAbierto() {
		return abierto;
	}

	/**
	 * @param abierto the abierto to set
	 */
	public void setAbierto(boolean abierto) {
		this.abierto = abierto;
	}

	/**
	 * @return the codigoFactorPrediccion
	 */
	public String getCodigoFactorPrediccion() {
		return factorPrediccion.getCodigo();
	}

	/**
	 * @param codigoFactorPrediccion the codigoFactorPrediccion to set
	 */
	public void setCodigoFactorPrediccion(String codigoFactorPrediccion) {
		this.factorPrediccion.setCodigo(codigoFactorPrediccion);
	}
	
	/**
	 * @return the nombreFactorPrediccion
	 */
	public String getNombreFactorPrediccion() {
		return factorPrediccion.getNombre();
	}

	/**
	 * @param codigoFactorPrediccion the codigoFactorPrediccion to set
	 */
	public void setNombreFactorPrediccion(String nombreFactorPrediccion) {
		this.factorPrediccion.setNombre(nombreFactorPrediccion);
	}

	/**
	 * @return the totalEscala
	 */
	public double getTotalEscala() {
		return totalEscala;
	}

	/**
	 * @param totalEscala the totalEscala to set
	 */
	public void setTotalEscala(double totalEscala) {
		this.totalEscala = totalEscala;
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
	 * @return the siempreAbierta
	 */
	public boolean isSiempreAbierta() {
		return siempreAbierta;
	}

	/**
	 * @param siempreAbierta the siempreAbierta to set
	 */
	public void setSiempreAbierta(boolean siempreAbierta) {
		this.siempreAbierta = siempreAbierta;
	}

	/**
	 * @return the factorPrediccion
	 */
	public InfoDatosString getFactorPrediccion() {
		return factorPrediccion;
	}

	/**
	 * @param factorPrediccion the factorPrediccion to set
	 */
	public void setFactorPrediccion(InfoDatosString factorPrediccion) {
		this.factorPrediccion = factorPrediccion;
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
}
