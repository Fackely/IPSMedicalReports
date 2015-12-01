package com.princetonsa.dto.tesoreria;

import java.util.ArrayList;

/**
 * @author Cristhian Murillo
 *
 */
//FIXME eliminar esta clase ?
public class DtoAceptacionSolicitudes extends DtoConsolidadoMovimiento {
	
	private static final long serialVersionUID = 1L;
	private ArrayList<DtoEntidadesFinancieras> listaDtoEntidadesFinancieras;
	private String observacionesSolicitud;
	private String observacionesAceptacion;
	private DtoUsuarioPersona testigo;
	//private ArrayList<DtoDetalleDocSopor> detallesAceptacionDTOs;
	//private ArrayList<DtoCuadreCaja> listaDtoCuadreCaja;
	
	
	public DtoAceptacionSolicitudes(){
		listaDtoEntidadesFinancieras= new ArrayList<DtoEntidadesFinancieras>();
		observacionesSolicitud		= "";
		observacionesAceptacion		= "";
		testigo						= new DtoUsuarioPersona();
		//detallesAceptacionDTOs	= new ArrayList<DtoDetalleDocSopor>();
		//listaDtoCuadreCaja		= new ArrayList<DtoCuadreCaja>();
	}
	
	
	public ArrayList<DtoEntidadesFinancieras> getListaDtoEntidadesFinancieras() {
		return listaDtoEntidadesFinancieras;
	}
	public void setListaDtoEntidadesFinancieras(ArrayList<DtoEntidadesFinancieras> listaDtoEntidadesFinancieras) {
		this.listaDtoEntidadesFinancieras = listaDtoEntidadesFinancieras;
	}
	public String getObservacionesSolicitud() {
		return observacionesSolicitud;
	}
	public void setObservacionesSolicitud(String observacionesSolicitud) {
		this.observacionesSolicitud = observacionesSolicitud;
	}
	public String getObservacionesAceptacion() {
		return observacionesAceptacion;
	}
	public void setObservacionesAceptacion(String observacionesAceptacion) {
		this.observacionesAceptacion = observacionesAceptacion;
	}
	public DtoUsuarioPersona getTestigo() {
		return testigo;
	}
	public void setTestigo(DtoUsuarioPersona testigo) {
		this.testigo = testigo;
	}

}
