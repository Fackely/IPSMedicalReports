package com.princetonsa.actionform.odontologia;

import java.util.ArrayList;

import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.odontologia.InfoDctoOdontologicoPresupuesto;
import util.odontologia.InfoDefinirSolucitudDsctOdon;

import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.princetonsa.dto.odontologia.DtoDescuentoOdontologicoAtencion;
import com.princetonsa.dto.odontologia.DtoMotivoDescuento;
import com.princetonsa.dto.odontologia.DtoPresupuestoOdontologicoDescuento;
import com.princetonsa.dto.odontologia.DtoServicioOdontologico;


/**
 * 
 * @author axioma
 *
 */

public class AutorizacionDescuentosOdonForm extends ValidatorForm
{
	private String estado;
	/**
	 * TMP OBSERVACION PRESUPUESTO DESCUENTO 
	 */
	private String tmpObservacionPresupuestoDctoOdon;
	
	/**
	 * CARGAR LOS DETALLES DE PROGRAMAS Y SERVICIOS
	 */
	private ArrayList<DtoServicioOdontologico> listadoProgramasPresupuesto;
	private ArrayList<DtoServicioOdontologico> listadoServiciosPresupuesto;
	
	/**
	 * CARGAR LOS DETALLES DE PROGRAMAS Y SERVICIOS DE INCLUSIONES
	 */
	private ArrayList<DtoServicioOdontologico> listadoProgramasInclusion;
	private ArrayList<DtoServicioOdontologico> listadoServiciosInclusion;
	
	/**
	 * CARGAR INTERFAZ DE DESCUENTOS
	 */
	private InfoDefinirSolucitudDsctOdon infoDefinir = new InfoDefinirSolucitudDsctOdon();
	private ArrayList<InfoDefinirSolucitudDsctOdon> listaInforDefinir = new ArrayList<InfoDefinirSolucitudDsctOdon>();
	
	/**
	 * LISTA Y DTO PARA CARGAR LOS EL MOTIVO DEL DESCUENTO
	 */
	private DtoMotivoDescuento dtoMotivosDescuento = new  DtoMotivoDescuento();
	private ArrayList<DtoMotivoDescuento> listaMotivos = new ArrayList<DtoMotivoDescuento>();
	
	/**
	 * CARGAR EL PORCENTAJE 	POR PRESUPUESTO 
	 */
	private InfoDctoOdontologicoPresupuesto infoDctoOdonPresupuesto = new  InfoDctoOdontologicoPresupuesto();
	
	/**
	 * CARGAR PORCENTAJE POR ATENCION
	 */
	private  ArrayList<DtoDescuentoOdontologicoAtencion> listaDctAtencion = new ArrayList<DtoDescuentoOdontologicoAtencion>();
	

	/** 
	 * GENERALES 
	 */
	private String utilizaProgramas;
	private int posPresupuesto;
	private String tipoDescuento;
	
	/**
	 * INFORMA EL VALOR CALCUALDO 
	 */
	private String valorTmp;
	/**
	 * 
	 */
	private String estadoAnterior;
	
	/**
	 * indice del arrayList descuentos Atencion
	 */
	private int postDescuentosOdon;
	
	/**
	 * COSULTA DEL LOG
	 */
	private ArrayList<DtoPresupuestoOdontologicoDescuento> listadoLogDescuentos;
	
	/**
	 * ARRAYLISTA CENTRO DE ATENCION
	 */
	private ArrayList<DtoCentrosAtencion> listaCentros = new ArrayList<DtoCentrosAtencion>();
	private int  consecutivoCentroAtencion;
	
	/**
	 * ORDENAMIENTO
	 */
	private String patronOrdenar;
	
	private String esDescendente;
	
	
	private DtoPresupuestoOdontologicoDescuento dtoPresupuestoDescuentoOdo ; 
	
	/**
	 * Nivel de Autorizacion
	 */
	private boolean tieneNivelAutorizacion;
	
	/**
	 * bandera par aindicar si esta por paciente o por rango
	 */
	private String porPacienteORango;
	

	
	
	/**
	 * Método para limpiar los datos
	 */
	public void reset()
	{
		this.estado="";
		this.listadoProgramasPresupuesto=new ArrayList<DtoServicioOdontologico>();
		this.listadoServiciosPresupuesto=new ArrayList<DtoServicioOdontologico>();
		this.listadoProgramasInclusion=new ArrayList<DtoServicioOdontologico>();
		this.listadoServiciosInclusion=new ArrayList<DtoServicioOdontologico>();
		this.posPresupuesto=ConstantesBD.codigoNuncaValido;
		this.tipoDescuento="";
		this.infoDefinir= new InfoDefinirSolucitudDsctOdon();
		this.listaInforDefinir = new ArrayList<InfoDefinirSolucitudDsctOdon>();
		this.utilizaProgramas=ConstantesBD.acronimoNo;
		this.dtoMotivosDescuento = new  DtoMotivoDescuento();
		this.listaMotivos = new ArrayList<DtoMotivoDescuento>();
		this.setInfoDctoOdonPresupuesto(new InfoDctoOdontologicoPresupuesto());
		this.setListaDctAtencion(new ArrayList<DtoDescuentoOdontologicoAtencion>());
		this.postDescuentosOdon=ConstantesBD.codigoNuncaValido;
		this.listadoLogDescuentos=new ArrayList<DtoPresupuestoOdontologicoDescuento>();
		this.valorTmp="";
		this.estadoAnterior="";
		this.listaCentros = new ArrayList<DtoCentrosAtencion>();
		this.consecutivoCentroAtencion = ConstantesBD.codigoNuncaValido;
		this.tieneNivelAutorizacion=false;
		this.tmpObservacionPresupuestoDctoOdon= "";
		this.setDtoPresupuestoDescuentoOdo(new DtoPresupuestoOdontologicoDescuento()); 
		this.porPacienteORango="";
		
	}

	
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public ArrayList<DtoServicioOdontologico> getListadoProgramasPresupuesto() {
		return listadoProgramasPresupuesto;
	}

	public void setListadoProgramasPresupuesto(
			ArrayList<DtoServicioOdontologico> listadoProgramasPresupuesto) {
		this.listadoProgramasPresupuesto = listadoProgramasPresupuesto;
	}

	public ArrayList<DtoServicioOdontologico> getListadoServiciosPresupuesto() {
		return listadoServiciosPresupuesto;
	}

	public void setListadoServiciosPresupuesto(
			ArrayList<DtoServicioOdontologico> listadoServiciosPresupuesto) {
		this.listadoServiciosPresupuesto = listadoServiciosPresupuesto;
	}
	
		
	public ArrayList<DtoServicioOdontologico> getListadoProgramasInclusion() {
		return listadoProgramasInclusion;
	}

	public void setListadoProgramasInclusion(
			ArrayList<DtoServicioOdontologico> listadoProgramasInclusion) {
		this.listadoProgramasInclusion = listadoProgramasInclusion;
	}

	
	public ArrayList<DtoServicioOdontologico> getListadoServiciosInclusion() {
		return listadoServiciosInclusion;
	}

	public void setListadoServiciosInclusion(
			ArrayList<DtoServicioOdontologico> listadoServiciosInclusion) {
		this.listadoServiciosInclusion = listadoServiciosInclusion;
	}
	
	

	public int getPosPresupuesto() {
		return posPresupuesto;
	}

	public void setPosPresupuesto(int posPresupuesto) {
		this.posPresupuesto = posPresupuesto;
	}

	public String getTipoDescuento() {
		return tipoDescuento;
	}
	
	public void setTipoDescuento(String tipoDescuento) {
		this.tipoDescuento = tipoDescuento;
	}



	public InfoDefinirSolucitudDsctOdon getInfoDefinir() {
		return infoDefinir;
	}



	public void setInfoDefinir(InfoDefinirSolucitudDsctOdon infoDefinir) {
		this.infoDefinir = infoDefinir;
	}



	public ArrayList<InfoDefinirSolucitudDsctOdon> getListaInforDefinir() {
		return listaInforDefinir;
	}



	public void setListaInforDefinir(
			ArrayList<InfoDefinirSolucitudDsctOdon> listaInforDefinir) {
		this.listaInforDefinir = listaInforDefinir;
	}



	public void setUtilizaProgramas(String utilizaProgramas) {
		this.utilizaProgramas = utilizaProgramas;
	}



	public String getUtilizaProgramas() {
		return utilizaProgramas;
	}


	/**
	 * @return the dtoMotivosDescuento
	 */
	public DtoMotivoDescuento getDtoMotivosDescuento() {
		return dtoMotivosDescuento;
	}

	
	/**
	 * @param dtoMotivosDescuento the dtoMotivosDescuento to set
	 */
	public void setDtoMotivosDescuento(DtoMotivoDescuento dtoMotivosDescuento) {
		this.dtoMotivosDescuento = dtoMotivosDescuento;
	}

	
	/**
	 * @return the listaMotivos
	 */
	public ArrayList<DtoMotivoDescuento> getListaMotivos() {
		return listaMotivos;
	}

	
	/**
	 * @param listaMotivos the listaMotivos to set
	 */
	public void setListaMotivos(ArrayList<DtoMotivoDescuento> listaMotivos) {
		this.listaMotivos = listaMotivos;
	}

	
	public void setInfoDctoOdonPresupuesto(InfoDctoOdontologicoPresupuesto infoDctoOdonPresupuesto) {
		this.infoDctoOdonPresupuesto = infoDctoOdonPresupuesto;
		
		
	}

	
	public InfoDctoOdontologicoPresupuesto getInfoDctoOdonPresupuesto() {
		return infoDctoOdonPresupuesto;
	}

	
	public void setListaDctAtencion(ArrayList<DtoDescuentoOdontologicoAtencion> listaDctAtencion) {
		this.listaDctAtencion = listaDctAtencion;
	}

	
	public ArrayList<DtoDescuentoOdontologicoAtencion> getListaDctAtencion() {
		return listaDctAtencion;
	}


	public void setPostDescuentosOdon(int postDescuentosOdon) {
		this.postDescuentosOdon = postDescuentosOdon;
	}

	
	public int getPostDescuentosOdon() {
		return postDescuentosOdon;
	}



	public ArrayList<DtoPresupuestoOdontologicoDescuento> getListadoLogDescuentos() {
		return listadoLogDescuentos;
	}


	public void setListadoLogDescuentos(
			ArrayList<DtoPresupuestoOdontologicoDescuento> listadoLogDescuentos) {
		this.listadoLogDescuentos = listadoLogDescuentos;
	}



	public void setValorTmp(String valorTmp) {
		this.valorTmp = valorTmp;
	}





	public String getValorTmp() {
		return valorTmp;
	}





	public void setEstadoAnterior(String estadoAnterior) {
		this.estadoAnterior = estadoAnterior;
	}





	public String getEstadoAnterior() {
		return estadoAnterior;
	}





	public void setListaCentros(ArrayList<DtoCentrosAtencion> listaCentros) {
		this.listaCentros = listaCentros;
	}





	public ArrayList<DtoCentrosAtencion> getListaCentros() {
		return listaCentros;
	}





	/**
	 * @return the consecutivoCentroAtencion
	 */
	public int getConsecutivoCentroAtencion() {
		return consecutivoCentroAtencion;
	}





	/**
	 * @param consecutivoCentroAtencion the consecutivoCentroAtencion to set
	 */
	public void setConsecutivoCentroAtencion(int consecutivoCentroAtencion) {
		this.consecutivoCentroAtencion = consecutivoCentroAtencion;
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





	public boolean isTieneNivelAutorizacion() {
		return tieneNivelAutorizacion;
	}

	
	public boolean getTieneNivelAutorizacion(){
		return tieneNivelAutorizacion;
	}



	public void setTieneNivelAutorizacion(boolean tieneNivelAutorizacion) {
		this.tieneNivelAutorizacion = tieneNivelAutorizacion;
	}


	public void setTmpObservacionPresupuestoDctoOdon(
			String tmpObservacionPresupuestoDctoOdon) {
		this.tmpObservacionPresupuestoDctoOdon = tmpObservacionPresupuestoDctoOdon;
	}





	public String getTmpObservacionPresupuestoDctoOdon() {
		return tmpObservacionPresupuestoDctoOdon;
	}





	public void setDtoPresupuestoDescuentoOdo(DtoPresupuestoOdontologicoDescuento dtoPresupuestoDescuentoOdo) {
		this.dtoPresupuestoDescuentoOdo = dtoPresupuestoDescuentoOdo;
	}





	public DtoPresupuestoOdontologicoDescuento getDtoPresupuestoDescuentoOdo() {
		return dtoPresupuestoDescuentoOdo;
	}





	public String getPorPacienteORango() {
		return porPacienteORango;
	}


	public void setPorPacienteORango(String porPacienteORango) {
		this.porPacienteORango = porPacienteORango;
	}



	
}