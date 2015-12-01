package com.servinte.axioma.dto.capitacion;

import java.util.ArrayList;

import com.servinte.axioma.orm.NivelAtencion;
import com.servinte.axioma.orm.ParamPresupuestosCap;
import com.servinte.axioma.orm.ValorizacionPresCapGen;

/**
 * Dto que contiene los datos de una parametrización 
 * del presupuesto de capitación a nivel general 
 * y detallado por nivel
 * @author diecorqu
 *
 */
public class DtoParametrizacionGeneral {
	
	/**
	 * Parametrización general del presupuesto
	 */
	private ParamPresupuestosCap parametrizacionPresupuesto;
	
	/**
	 * Meses vigentes para el contrato seleccionado en 
	 * la fecha de vigencia seleccionada
	 */
	private ArrayList<String[]> mesesMatriz;
	
	/**
	 * Lista de Dto's con los respectivos niveles de atención
	 * asociados al contrato 
	 */
	private ArrayList<DtoNivelesAtencionPresupuestoParametrizacionGeneral>  listaDtoNivelesAtencion;
	
	/**
	 * Lista de niveles de atención asociados a la parametrizacion
	 */
	private ArrayList<NivelAtencion> listaNivelesParametrizacion;
	
	/**
	 * Lista de niveles de atención asociados al contrato
	 */
	private ArrayList<NivelAtencion> listaNivelesContrato;
	
	/**
	 * Valorización general del presupuesto de capitación
	 */
	private ArrayList<ValorizacionPresCapGen> valorizacionPresupuestoGeneral;
	
	/**
	 * lista de parametrizaciones obtenidas en la busqueda de parametrizaciones
	 * para su respectiva modificación
	 */
	private ArrayList<DtoParamPresupCap> listaParametrizaciones;
	
	/**
	 * Estado de la parametrización
	 */
	private String estado;
	
	/**
	 * Determina si la parametrización permite solo lectura
	 */
	private boolean soloLectura;

	
	public DtoParametrizacionGeneral(){}
	
	
	public DtoParametrizacionGeneral(
			ParamPresupuestosCap parametrizacionPresupuesto,
			ArrayList<String[]> mesesMatriz,
			ArrayList<DtoNivelesAtencionPresupuestoParametrizacionGeneral> listaDtoNivelesAtencion,
			ArrayList<NivelAtencion> listaNivelesParametrizacion,
			ArrayList<NivelAtencion> listaNivelesContrato,
			ArrayList<ValorizacionPresCapGen> valorizacionPresupuestoGeneral,
			ArrayList<DtoParamPresupCap> listaParametrizaciones, String estado,
			boolean soloLectura) {
		super();
		this.parametrizacionPresupuesto = parametrizacionPresupuesto;
		this.mesesMatriz = mesesMatriz;
		this.listaDtoNivelesAtencion = listaDtoNivelesAtencion;
		this.listaNivelesParametrizacion = listaNivelesParametrizacion;
		this.listaNivelesContrato = listaNivelesContrato;
		this.valorizacionPresupuestoGeneral = valorizacionPresupuestoGeneral;
		this.listaParametrizaciones = listaParametrizaciones;
		this.estado = estado;
		this.soloLectura = soloLectura;
	}

	public ParamPresupuestosCap getParametrizacionPresupuesto() {
		return parametrizacionPresupuesto;
	}
	public void setParametrizacionPresupuesto(
			ParamPresupuestosCap parametrizacionPresupuesto) {
		this.parametrizacionPresupuesto = parametrizacionPresupuesto;
	}
	public ArrayList<String[]> getMesesMatriz() {
		return mesesMatriz;
	}
	public void setMesesMatriz(ArrayList<String[]> mesesMatriz) {
		this.mesesMatriz = mesesMatriz;
	}
	public ArrayList<DtoNivelesAtencionPresupuestoParametrizacionGeneral> getListaDtoNivelesAtencion() {
		return listaDtoNivelesAtencion;
	}

	public void setListaDtoNivelesAtencion(
			ArrayList<DtoNivelesAtencionPresupuestoParametrizacionGeneral> listaDtoNivelesAtencion) {
		this.listaDtoNivelesAtencion = listaDtoNivelesAtencion;
	}

	public ArrayList<NivelAtencion> getListaNivelesParametrizacion() {
		return listaNivelesParametrizacion;
	}

	public void setListaNivelesParametrizacion(
			ArrayList<NivelAtencion> listaNivelesParametrizacion) {
		this.listaNivelesParametrizacion = listaNivelesParametrizacion;
	}

	public ArrayList<NivelAtencion> getListaNivelesContrato() {
		return listaNivelesContrato;
	}

	public void setListaNivelesContrato(
			ArrayList<NivelAtencion> listaNivelesContrato) {
		this.listaNivelesContrato = listaNivelesContrato;
	}

	public ArrayList<ValorizacionPresCapGen> getValorizacionPresupuestoGeneral() {
		return valorizacionPresupuestoGeneral;
	}

	public void setValorizacionPresupuestoGeneral(
			ArrayList<ValorizacionPresCapGen> valorizacionPresupuestoGeneral) {
		this.valorizacionPresupuestoGeneral = valorizacionPresupuestoGeneral;
	}

	public ArrayList<DtoParamPresupCap> getListaParametrizaciones() {
		return listaParametrizaciones;
	}

	public void setListaParametrizaciones(
			ArrayList<DtoParamPresupCap> listaParametrizaciones) {
		this.listaParametrizaciones = listaParametrizaciones;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public boolean isSoloLectura() {
		return soloLectura;
	}

	public void setSoloLectura(boolean soloLectura) {
		this.soloLectura = soloLectura;
	}
	
	
}
