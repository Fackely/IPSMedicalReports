package com.princetonsa.actionform.odontologia;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadTexto;

import com.princetonsa.dto.odontologia.DtoDetalleHallazgoProgramaServicio;
import com.princetonsa.dto.odontologia.DtoEquivalentesHallazgoProgramaServicio;
import com.princetonsa.dto.odontologia.DtoHallazgoOdontologico;
import com.princetonsa.dto.odontologia.DtoHallazgoVsProgramaServicio;
import com.princetonsa.dto.odontologia.DtoPrograma;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.odontologia.DetalleHallazgoProgramaServicio;



public class HallazgoProgramaServicioForm  extends ValidatorForm  {

	
	/**
	 * Versión serial 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Manejo de estados del flujo 
     */
	private String estado;

	/**
	 * 
	 */
	private DtoHallazgoVsProgramaServicio dtoHallazgoProgramaServicio = new DtoHallazgoVsProgramaServicio();
	
	/**
	 * 
	 * 
	 */
	
	private DtoDetalleHallazgoProgramaServicio dtoDetalle = new DtoDetalleHallazgoProgramaServicio();
	/**
	 * 
	 * 
	 */
	private ArrayList<DtoHallazgoVsProgramaServicio> listaHallazgoProgramaServicio = new ArrayList<DtoHallazgoVsProgramaServicio>();
	
	/**
	 * 
	 * 
	 */
	
	 private ArrayList<DtoDetalleHallazgoProgramaServicio> listaDetalles = new ArrayList<DtoDetalleHallazgoProgramaServicio>();
	 
	 /**
	 * 
	 * 
	 */
	
	  private ArrayList<DtoEquivalentesHallazgoProgramaServicio> listaEquivalentes = new ArrayList<DtoEquivalentesHallazgoProgramaServicio>();
	
	/**
	 * 
	 * 
	 */
	
	private ArrayList<DtoHallazgoOdontologico> listaHallazgos = new ArrayList<DtoHallazgoOdontologico>();
	
	/**
	 * 
	 * 
	 */
	private ArrayList<DtoPrograma> listaProgramas = new ArrayList<DtoPrograma>();;
	
	/***
	 * 
	 */
	private String patronOrdenar;
	
	/**
	 * 
	 */
	
	private String esDescendente;
	
	/**
	 * 
	 */
	private int posArray;
	
	/**
	 * CADENA PARA VALIDAR LOS HALLAZGOS 
	 */
	private int posArrayDetalle;
	
	/**
	 * CADENA PARA VALIDAR LOS HALLAZGOS 
	 */
	private String tipodeRelacion="";
	
	/***
	 * 
	 * 
	 */
	private String codigoHallazgos;
	
	/**
	 * 
	 * 
	 */
	private boolean cambioNumeroSuperficies;
	
	
	/**
	 *valida aplica Superficie  
	 */
	private String  aplicaSuperficie;
	
	
	
	
	private int postIndiceEquivalentes;
	
	
	
	private String respuestaXml;
	
	/**
	 * 
	 */
	private String mensaje;
	
	
	/**
	 * 
	 */
	private int tamanoListaEquivalentes;
	
	
	
	/**
	 * 
	 */
	private String tieneSuperficiePorDefecto;
	
	
	
	
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) 
	{
	
		ActionErrors errores = new ActionErrors();
		UsuarioBasico usuario = (UsuarioBasico) request.getSession().getAttribute("usuarioBasico");
	
		if(this.getEstado().equals("guardar"))
		{
			if(this.getDtoHallazgoProgramaServicio().getHallazgo().getCodigo() <=0)
			{
				errores.add("", new ActionMessage("errors.required", "el Hallazgo"));
			}
		} 	
	
		else if(this.getEstado().equals("guardarDetalle") || this.getEstado().equals("ingresarEquivalentes"))
		{
			if((this.getDtoDetalle().getPrograma().getCodigo()  <= 0) && (this.getDtoDetalle().getServicio().getCodigo()<=0))
			{
				if(!UtilidadTexto.isEmpty(this.getTipodeRelacion()))
				{
					errores.add("", new ActionMessage("errors.required", " El"+"  "+this.getTipodeRelacion()));
				}
				else
				{
					errores.add("", new ActionMessage("errors.required", "El Parametro General: Utiliza Programas Odontologicos en la Institucion"));
				}
				
			}	
			this.validarDtoDetallePorDefecto(errores);
			
		}
	
		else if  (this.getEstado().equals("eliminar"))
		{
			DtoDetalleHallazgoProgramaServicio dtoDet = new DtoDetalleHallazgoProgramaServicio();
			dtoDet.setHallazgoVsProgramaServicio(this.getListaHallazgoProgramaServicio().get(this.getPosArray()).getCodigo());
			if(DetalleHallazgoProgramaServicio.cargar(dtoDet, usuario.getCodigoInstitucionInt()).size() > 0)
			{			
				errores.add("", new ActionMessage("errors.existeDetalle", "Hallazgo Vs Programa o Servicio"));
			}
		}
		
	
	
	if(!errores.isEmpty())
	{
		if(this.getEstado().equals("guardar") )
			this.setEstado("mostrarErroresGuardar");
		
		if(this.getEstado().equals("ingresarEquivalentes") )
			this.setEstado("mostrarErroresGuardarDetalleE");
		
		if(this.getEstado().equals("guardarDetalle"))					
			this.setEstado("mostrarErroresGuardarDetalle");
		}
	
		return errores;
	}
	 
	
	
	/**
	 * Reset 
	 */
	public void reset() {
		this.posArray = ConstantesBD.codigoNuncaValido;
	    this.posArrayDetalle = ConstantesBD.codigoNuncaValido;
		this.patronOrdenar = "";
		this.listaHallazgoProgramaServicio = new  ArrayList<DtoHallazgoVsProgramaServicio>();
		this.dtoHallazgoProgramaServicio = new DtoHallazgoVsProgramaServicio();
		this.listaHallazgos = new ArrayList<DtoHallazgoOdontologico>();
		this.dtoDetalle = new DtoDetalleHallazgoProgramaServicio();
		this.listaDetalles = new ArrayList<DtoDetalleHallazgoProgramaServicio>();
		this.listaProgramas = new ArrayList<DtoPrograma>();
		this.listaEquivalentes = new ArrayList<DtoEquivalentesHallazgoProgramaServicio>();
		this.codigoHallazgos="";
		this.cambioNumeroSuperficies = false;
		this.aplicaSuperficie=ConstantesBD.valorTrueLargoEnString;//para validar el campo numero de superficie por defecto inactivo
		this.mensaje="";
		this.postIndiceEquivalentes=ConstantesBD.codigoNuncaValido;
		this.respuestaXml="";
		this.tamanoListaEquivalentes=ConstantesBD.codigoNuncaValido;
		this.tieneSuperficiePorDefecto=ConstantesBD.acronimoNo;
	}
	
	
	/**
	 * 
	 */
	public void resetEmpezar(){
		this.posArray = ConstantesBD.codigoNuncaValido;
	    this.posArrayDetalle = ConstantesBD.codigoNuncaValido;
		this.patronOrdenar = "";
		this.listaHallazgoProgramaServicio = new  ArrayList<DtoHallazgoVsProgramaServicio>();
		this.dtoHallazgoProgramaServicio = new DtoHallazgoVsProgramaServicio();
		this.listaHallazgos = new ArrayList<DtoHallazgoOdontologico>();
		this.dtoDetalle = new DtoDetalleHallazgoProgramaServicio();
		this.listaDetalles = new ArrayList<DtoDetalleHallazgoProgramaServicio>();
		this.listaProgramas = new ArrayList<DtoPrograma>();
		this.listaEquivalentes = new ArrayList<DtoEquivalentesHallazgoProgramaServicio>();
		this.codigoHallazgos="";
		this.cambioNumeroSuperficies = false;
		this.aplicaSuperficie=ConstantesBD.valorTrueLargoEnString;//para validar el campo numero de superficie por defecto inactivo
		this.postIndiceEquivalentes=ConstantesBD.codigoNuncaValido;
		this.respuestaXml="";
		this.tamanoListaEquivalentes=ConstantesBD.codigoNuncaValido;
	}

		
	
	
	/**
	 * VALIDAR DTO POR DEFECTO 
	 * " SOLO SE PERMITE UN DETALLE POR DEFECTO EN SI "  
	 * @param errores
	 * @return
	 */
	private ActionErrors  validarDtoDetallePorDefecto(ActionErrors errores)
	{
		if(this.dtoDetalle.getPorDefecto().equals(ConstantesBD.acronimoSi) )
		{
			if(accionValidarDetallePorDefecto())
			{
				errores.add("", new ActionMessage("errors.notEspecific", "Para El Número de Programas ya Existe una Superficie Por Defecto "));
			}
		}
		return errores;
	}
	
	

	
	
	
	/**
	 * ACCION VALIDAR DETALLE POR DEFECTO
	 * @param forma
	 */
	private boolean accionValidarDetallePorDefecto() 
	{
		boolean bandera= Boolean.FALSE;
		for ( DtoDetalleHallazgoProgramaServicio dtoDetalle: this.getListaDetalles() )
		{
			if(dtoDetalle.getPorDefecto().equals(ConstantesBD.acronimoSi))
			{
				bandera=Boolean.TRUE;
			}
		}
		return bandera;
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
	 * @return the dtoHallazgoProgramaServicio
	 */
	public DtoHallazgoVsProgramaServicio getDtoHallazgoProgramaServicio() {
		return dtoHallazgoProgramaServicio;
	}

	/**
	 * @param dtoHallazgoProgramaServicio the dtoHallazgoProgramaServicio to set
	 */
	public void setDtoHallazgoProgramaServicio(
			DtoHallazgoVsProgramaServicio dtoHallazgoProgramaServicio) {
		this.dtoHallazgoProgramaServicio = dtoHallazgoProgramaServicio;
	}

	/**
	 * @return the listaHallazgoProgramaServicio
	 */
	public ArrayList<DtoHallazgoVsProgramaServicio> getListaHallazgoProgramaServicio() {
		return listaHallazgoProgramaServicio;
	}

	/**
	 * @param listaHallazgoProgramaServicio the listaHallazgoProgramaServicio to set
	 */
	public void setListaHallazgoProgramaServicio(
			ArrayList<DtoHallazgoVsProgramaServicio> listaHallazgoProgramaServicio) {
		this.listaHallazgoProgramaServicio = listaHallazgoProgramaServicio;
	}

	/**
	 * @return the patronOrdenar
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	/**
	 * @param patronOrdenar the patronOrdenar to set
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	/**
	 * @return the esDescendente
	 */
	public String getEsDescendente() {
		return esDescendente;
	}

	/**
	 * @param esDescendente the esDescendente to set
	 */
	public void setEsDescendente(String esDescendente) {
		this.esDescendente = esDescendente;
	}

	/**
	 * @return the posArray
	 */
	public int getPosArray() {
		return posArray;
	}

	/**
	 * @param posArray the posArray to set
	 */
	public void setPosArray(int posArray) {
		this.posArray = posArray;
	}

	/**
	 * @return the listaHallazgos
	 */
	public ArrayList<DtoHallazgoOdontologico> getListaHallazgos() {
		return listaHallazgos;
	}

	/**
	 * @param listaHallazgos the listaHallazgos to set
	 */
	public void setListaHallazgos(ArrayList<DtoHallazgoOdontologico> listaHallazgos) {
		this.listaHallazgos = listaHallazgos;
	}

	/**
	 * @return the tipodeRelacion
	 */
	public String getTipodeRelacion() {
		return tipodeRelacion;
	}

	/**
	 * @param tipodeRelacion the tipodeRelacion to set
	 */
	public void setTipodeRelacion(String tipodeRelacion) {
		this.tipodeRelacion = tipodeRelacion;
	}

	/**
	 * @return the dtoDetalle
	 */
	public DtoDetalleHallazgoProgramaServicio getDtoDetalle() {
		return dtoDetalle;
	}

	/**
	 * @param dtoDetalle the dtoDetalle to set
	 */
	public void setDtoDetalle(DtoDetalleHallazgoProgramaServicio dtoDetalle) {
		this.dtoDetalle = dtoDetalle;
	}

	/**
	 * @return the listaDetalles
	 */
	public ArrayList<DtoDetalleHallazgoProgramaServicio> getListaDetalles() {
		return listaDetalles;
	}

	/**
	 * @param listaDetalles the listaDetalles to set
	 */
	public void setListaDetalles(
			ArrayList<DtoDetalleHallazgoProgramaServicio> listaDetalles) {
		this.listaDetalles = listaDetalles;
	}

	/**
	 * @return the posArrayDetalle
	 */
	public int getPosArrayDetalle() {
		return posArrayDetalle;
	}

	/**
	 * @param posArrayDetalle the posArrayDetalle to set
	 */
	public void setPosArrayDetalle(int posArrayDetalle) {
		this.posArrayDetalle = posArrayDetalle;
	}

	/**
	 * @return the listaProgramas
	 */
	public ArrayList<DtoPrograma> getListaProgramas() {
		return listaProgramas;
	}

	/**
	 * @param listaProgramas the listaProgramas to set
	 */
	public void setListaProgramas(ArrayList<DtoPrograma> listaProgramas) {
		this.listaProgramas = listaProgramas;
	}

	
		/**
	    * 
	    * 
	    */
		
		
	/**
	 * 
	 * @param codigoHallazgos
	 */
	public void setCodigoHallazgos(String codigoHallazgos) {
		this.codigoHallazgos = codigoHallazgos;
	}

	/**
	 * 
	 * @return
	 */
	public String getCodigoHallazgos() {
		return codigoHallazgos;
	}
	/**
	 * 
	 * @param codigo
	 */

	/**
	 * @return the listaEquivalentes
	 */
	public ArrayList<DtoEquivalentesHallazgoProgramaServicio> getListaEquivalentes() {
		return listaEquivalentes;
	}

	/**
	 * @param listaEquivalentes the listaEquivalentes to set
	 */
	public void setListaEquivalentes(
			ArrayList<DtoEquivalentesHallazgoProgramaServicio> listaEquivalentes) {
		this.listaEquivalentes = listaEquivalentes;
	}


	/**
	 * @return the cambioNumeroSuperficies
	 */
	public boolean isCambioNumeroSuperficies() {
		return cambioNumeroSuperficies;
	}


	/**
	 * @param cambioNumeroSuperficies the cambioNumeroSuperficies to set
	 */
	public void setCambioNumeroSuperficies(boolean cambioNumeroSuperficies) {
		this.cambioNumeroSuperficies = cambioNumeroSuperficies;
	}

	/**
	 * 
	 * @param aplicaSuperficie
	 */
	public void setAplicaSuperficie(String aplicaSuperficie) {
		this.aplicaSuperficie = aplicaSuperficie;
	}

	/**
	 * 
	 * @return
	 */
	public String getAplicaSuperficie() {
		return aplicaSuperficie;
	}


	public String getMensaje() {
		return mensaje;
	}


	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}


	public void setPostIndiceEquivalentes(int postIndiceEquivalentes) {
		this.postIndiceEquivalentes = postIndiceEquivalentes;
	}


	public int getPostIndiceEquivalentes() {
		return postIndiceEquivalentes;
	}


	public void setRespuestaXml(String respuestaXml) {
		this.respuestaXml = respuestaXml;
	}


	public String getRespuestaXml() {
		return respuestaXml;
	}



	public void setTamanoListaEquivalentes(int tamanoListaEquivalentes) {
		this.tamanoListaEquivalentes = tamanoListaEquivalentes;
	}



	public int getTamanoListaEquivalentes() {
		this.tamanoListaEquivalentes=this.getListaEquivalentes().size();
		return tamanoListaEquivalentes;
	}



	public void setTieneSuperficiePorDefecto(String tieneSuperficiePorDefecto) {
		this.tieneSuperficiePorDefecto = tieneSuperficiePorDefecto;
	}



	public String getTieneSuperficiePorDefecto() {
		return tieneSuperficiePorDefecto;
	}
	
	
	
}
