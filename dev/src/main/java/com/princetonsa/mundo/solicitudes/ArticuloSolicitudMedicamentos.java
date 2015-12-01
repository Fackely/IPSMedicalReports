/*
 * Creado en 2/09/2004
 *
 */
package com.princetonsa.mundo.solicitudes;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author Juan David Ramírez López
 *
 * Princeton S.A.
 */
public class ArticuloSolicitudMedicamentos implements Serializable
{
	/**
	 * Codigo del articulo
	 */
	private int articulo;
	
	/**
	 * Indica si el objeto fue modificado
	 */
	private boolean modificado;
	
	/**
	 * Observaciones del medicamento
	 */
	private String observaciones;
	
	/*--------------------------Elementos del detalle de la solcitud--------------------------*/
	
	/**
	 * Atributo para almacenar la justificación de los
	 * medicamentos no pos
	 */
	private HashMap justificacion;
	
	/**
	 * Dosis del medicamento
	 */
	private String dosis;
	
	/**
	 * 
	 */
	private String unidosis;
	
	/**
	 * Frecuencia del medicamento
	 */
	private int frecuencia;
	
	/**
	 * Manejo de los tipos de frecuencia
	 */
	private String tipoFrecuencia;
	
	/**
	 * Via de administración del medicamento
	 */
	private String via;
	
	/**
	 * Cantidad Solicitada
	 */
	private int cantidadSolicitada;
	
	/*-------------------------Fin elementos solicitud------------------*/
	
	/**
	 * Campo en el que se almacenará si el articulo es pos o no
	 */
	private String esPos;
	
	/**
	 * Campo que almacena el despacho total de un articulo.
	 */
	private int despachoTotal;
	
	/**
	 * Indica si un articulo es sustituto.
	 */
	private boolean esSustituto;
	
	/**
	 * Codigo del articulo padre de un sustituto, cuando el actual es sustituto.
	 */
	private int artPrincipal;
	
	/**
	 * Verificar si el medicamento está o no suspendido
	 */
	private boolean suspendido;
	
	/**
	 * Motivo de la suspensión del artículo
	 */
	private String motivoSuspension;
	
	/**
	 * Total de medicamentso administrados traidos por el paciente
	 */
	private String totalDespachadoPaciente;
	
	/**
	 * Total de medicamentso administrados de farmacia
	 */
	private String totalDespachadoFarmacia;

	/**
	 * Manejo de logs de la solicitud
	 */
	private String logModificacion;
	
	/**
	 * 
	 */
	private String diasTratamiento;
	
	/**
	 * Atributo que almacena el indicativo para identificar si es medicamento o insumo
	 */
	private String esMedicamento;
	
	/**
	 * @return Retorna observaciones.
	 */
	public String getObservaciones()
	{
		return observaciones;
	}
	
	/**
	 * @param observaciones Asigna observaciones.
	 */
	public void setObservaciones(String observaciones)
	{
		this.observaciones = observaciones;
	}
	
	/**
	 * @return Retorna articulo.
	 */
	public int getArticulo()
	{
		return articulo;
	}
	/**
	 * @param articulo Asigna articulo.
	 */
	public void setArticulo(int articulo)
	{
		this.articulo = articulo;
	}
	/**
	 * @return Retorna dosis.
	 */
	public String getDosis()
	{
		return dosis;
	}
	/**
	 * @param dosis Asigna dosis.
	 */
	public void setDosis(String dosis)
	{
		this.dosis = dosis;
	}
	/**
	 * @return Retorna frecuencia.
	 */
	public int getFrecuencia()
	{
		return frecuencia;
	}
	/**
	 * @param frecuencia Asigna frecuencia.
	 */
	public void setFrecuencia(int frecuencia)
	{
		this.frecuencia = frecuencia;
	}
	/**
	 * @return Retorna via.
	 */
	public String getVia()
	{
		return via;
	}
	/**
	 * @param via Asigna via.
	 */
	public void setVia(String via)
	{
		this.via = via;
	}
	/**
	 * @return Retorna esPos.
	 */
	public String getEsPos()
	{
		return esPos;
	}
	/**
	 * @param esPos Asigna esPos.
	 */
	public void setEsPos(String esPos)
	{
		this.esPos = esPos;
	}
	
	/**
	 * Constructor Vacío
	 */
	public ArticuloSolicitudMedicamentos()
	{
		this.clean();
	}

	/**
	 * @return Retorna el despachoTotal.
	 */
	public int getDespachoTotal() {
		return despachoTotal;
	}
	/**
	 * @param despachoTotal Asigna el despachoTotal.
	 */
	public void setDespachoTotal(int despachoTotal) {
		this.despachoTotal = despachoTotal;
	}
	/**
	 * @return Retorna el esSustituto.
	 */
	public boolean isEsSustituto() {
		return esSustituto;
	}
	
	/**
	 * @param esSustituto Asigna el esSustituto.
	 */
	public void setEsSustituto(boolean esSustituto) {
		this.esSustituto = esSustituto;
	}
	/**
	 * @return Retorna el artPrincipal.
	 */
	public int getArtPrincipal() {
		return artPrincipal;
	}
	/**
	 * @param artPrincipal Asigna el artPrincipal.
	 */
	public void setArtPrincipal(int artPrincipal) {
		this.artPrincipal = artPrincipal;
	}
	
	/**
	 * Método para resetear el artículo
	 */
	public void clean()
	{
		articulo=0;
		observaciones=null;
		dosis=null;
		unidosis=null;
		frecuencia=0;
		tipoFrecuencia=null;
		cantidadSolicitada=0;
		via=null;
		esPos="";
		despachoTotal=0;
		esSustituto=false;
		artPrincipal=0;
		suspendido=false;
		modificado=false;
		logModificacion="";
		justificacion=new HashMap();
		this.diasTratamiento="";
	}
	/**
	 * @return Retorna suspendido.
	 */
	public boolean getSuspendido()
	{
		return suspendido;
	}
	/**
	 * @param suspendido Asigna suspendido.
	 */
	public void setSuspendido(boolean suspendido)
	{
		this.suspendido = suspendido;
	}
	/**
	 * @return Retorna motivoSuspension.
	 */
	public String getMotivoSuspension()
	{
		return motivoSuspension;
	}
	/**
	 * @param motivoSuspension Asigna motivoSuspension.
	 */
	public void setMotivoSuspension(String motivoSuspension)
	{
		this.motivoSuspension = motivoSuspension;
	}
	/**
	 * @return Retorna totalDespachadoPaciente.
	 */
	public String getTotalDespachadoPaciente()
	{
		return totalDespachadoPaciente;
	}
	/**
	 * @param totalDespachadoPaciente Asigna totalDespachadoPaciente.
	 */
	public void setTotalDespachadoPaciente(String totalDespachadoPaciente)
	{
		this.totalDespachadoPaciente = totalDespachadoPaciente;
	}
	/**
	 * @return Retorna totalDespachadoFarmacia.
	 */
	public String getTotalDespachadoFarmacia()
	{
		return totalDespachadoFarmacia;
	}
	/**
	 * @param totalDespachadoFarmacia Asigna totalDespachadoFarmacia.
	 */
	public void setTotalDespachadoFarmacia(String totalDespachadoFarmacia)
	{
		this.totalDespachadoFarmacia = totalDespachadoFarmacia;
	}
	/**
	 * @return Retorna modificado.
	 */
	public boolean getModificado()
	{
		return modificado;
	}
	/**
	 * @param modificado Asigna modificado.
	 */
	public void setModificado(boolean modificado)
	{
		this.modificado = modificado;
	}
	/**
	 * @return Retorna logModificacion.
	 */
	public String getLogModificacion()
	{
		return logModificacion;
	}
	/**
	 * @param logModificacion Asigna logModificacion.
	 */
	public void setLogModificacion(String logModificacion)
	{
		this.logModificacion = logModificacion;
	}
	/**
	 * @return Retorna justificacion espcífica dado el Codigo (Key) con el
	 * que se identifica el atributo buscado.
	 */
	public String getJustificacion(String key)
	{
		return (String)justificacion.get(key);
	}
	/**
	 * Método para asignar una justificación específica,
	 * de acuerdo con el codigo de la BD utilizado como Key
	 * @param justificacion Asigna justificacion.
	 */
	public void setJustificacion(String key, String value)
	{
		this.justificacion.put(key, value);
	}
	/**
	 * @return Retorna justificacion (Mapa Completo).
	 */
	public HashMap getJustificacion()
	{
		return justificacion;
	}
	/**
	 * @param justificacion Asigna justificacion (Mapa Completo).
	 */
	public void setJustificacion(HashMap justificacion)
	{
		this.justificacion = justificacion;
	}
	/**
	 * @return Retorna tipoFrecuencia.
	 */
	public String getTipoFrecuencia()
	{
		return tipoFrecuencia;
	}
	/**
	 * @param tipoFrecuencia Asigna tipoFrecuencia.
	 */
	public void setTipoFrecuencia(String tipoFrecuencia)
	{
		this.tipoFrecuencia = tipoFrecuencia;
	}
	/**
	 * @return Retorna cantidadSolicitada.
	 */
	public int getCantidadSolicitada()
	{
		return cantidadSolicitada;
	}
	/**
	 * @param cantidadSolicitada Asigna cantidadSolicitada.
	 */
	public void setCantidadSolicitada(int cantidadSolicitada)
	{
		this.cantidadSolicitada = cantidadSolicitada;
	}

	/**
	 * @return the unidosis
	 */
	public String getUnidosis() {
		return unidosis;
	}

	/**
	 * @param unidosis the unidosis to set
	 */
	public void setUnidosis(String unidosis) {
		this.unidosis = unidosis;
	}

	/**
	 * @return the diasTratamiento
	 */
	public String getDiasTratamiento() {
		return diasTratamiento;
	}

	/**
	 * @param diasTratamiento the diasTratamiento to set
	 */
	public void setDiasTratamiento(String diasTratamiento) {
		this.diasTratamiento = diasTratamiento;
	}

	/**
	 * @return the esMedicamento
	 */
	public String getEsMedicamento() {
		return esMedicamento;
	}

	/**
	 * @param esMedicamento the esMedicamento to set
	 */
	public void setEsMedicamento(String esMedicamento) {
		this.esMedicamento = esMedicamento;
	}
}
