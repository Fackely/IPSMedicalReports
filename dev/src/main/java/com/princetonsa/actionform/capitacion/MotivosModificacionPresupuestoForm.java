package com.princetonsa.actionform.capitacion;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.ValidatorForm;

import util.constantes.estadosJsp.IconstantesEstadosJsp;

import com.servinte.axioma.dto.capitacion.DtoMotivosModifiPresupuesto;
//import com.servinte.axioma.orm.MotivosModifiPresupuesto;

public class MotivosModificacionPresupuestoForm extends ValidatorForm{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private String estado;

	private ArrayList<DtoMotivosModifiPresupuesto> listaMotivosModificacionPresupuesto;

	private ArrayList<Long> listaElementosAEliminar;

	private DtoMotivosModifiPresupuesto motivosModifiPresupuesto;

	private MessageResources messageResource = MessageResources.getMessageResources("com.servinte.mensajes.capitacion.MotivosModificacionPresupuestoForm");

	private String indiceDetalle;
	
	 private String patronOrdenar;
	 
	 private String esDescendente;
	 
	 private String codigoFiltro;
	 
	 private String descripcionFiltro;
	 
	 private Boolean activoFiltro;
	 
	 private Boolean indicadorTipoConsulta;

	 
	 /**
	  * 
	  * Constructor de la clase
	  */
	public MotivosModificacionPresupuestoForm() {
		this.listaMotivosModificacionPresupuesto= new ArrayList<DtoMotivosModifiPresupuesto>();
		this.listaElementosAEliminar = new ArrayList<Long>();
		this.motivosModifiPresupuesto = new DtoMotivosModifiPresupuesto();
	}


	/**
	 * 
	 * 
	 *
	 */
	public void inicializar(){
		this.listaMotivosModificacionPresupuesto= new ArrayList<DtoMotivosModifiPresupuesto>();
		this.listaElementosAEliminar = new ArrayList<Long>();
		this.motivosModifiPresupuesto = new DtoMotivosModifiPresupuesto();
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
	 * @return the listaElementosAEliminar
	 */
	public ArrayList<Long> getListaElementosAEliminar() {
		return listaElementosAEliminar;
	}



	/**
	 * @param listaElementosAEliminar the listaElementosAEliminar to set
	 */
	public void setListaElementosAEliminar(ArrayList<Long> listaElementosAEliminar) {
		this.listaElementosAEliminar = listaElementosAEliminar;
	}



	/**
	 * @return the motivosModifiPresupuesto
	 */
	public DtoMotivosModifiPresupuesto getMotivosModifiPresupuesto() {
		return motivosModifiPresupuesto;
	}



	/**
	 * @param motivosModifiPresupuesto the motivosModifiPresupuesto to set
	 */
	public void setMotivosModifiPresupuesto(
			DtoMotivosModifiPresupuesto motivosModifiPresupuesto) {
		this.motivosModifiPresupuesto = motivosModifiPresupuesto;
	}



	/**
	 * @return the indiceDetalle
	 */
	public String getIndiceDetalle() {
		return indiceDetalle;
	}



	/**
	 * @param indiceDetalle the indiceDetalle to set
	 */
	public void setIndiceDetalle(String indiceDetalle) {
		this.indiceDetalle = indiceDetalle;
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
	 * @return the codigoFiltro
	 */
	public String getCodigoFiltro() {
		return codigoFiltro;
	}



	/**
	 * @param codigoFiltro the codigoFiltro to set
	 */
	public void setCodigoFiltro(String codigoFiltro) {
		this.codigoFiltro = codigoFiltro;
	}



	/**
	 * @return the descripcionFiltro
	 */
	public String getDescripcionFiltro() {
		return descripcionFiltro;
	}



	/**
	 * @param descripcionFiltro the descripcionFiltro to set
	 */
	public void setDescripcionFiltro(String descripcionFiltro) {
		this.descripcionFiltro = descripcionFiltro;
	}



	/**
	 * @return the activoFiltro
	 */
	public Boolean getActivoFiltro() {
		return activoFiltro;
	}



	/**
	 * @param activoFiltro the activoFiltro to set
	 */
	public void setActivoFiltro(Boolean activoFiltro) {
		this.activoFiltro = activoFiltro;
	}



	/**
	 * @return the indicadorTipoConsulta
	 */
	public Boolean getIndicadorTipoConsulta() {
		return indicadorTipoConsulta;
	}



	/**
	 * @param indicadorTipoConsulta the indicadorTipoConsulta to set
	 */
	public void setIndicadorTipoConsulta(Boolean indicadorTipoConsulta) {
		this.indicadorTipoConsulta = indicadorTipoConsulta;
	}



	/**
	 * 
	 */
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errores = new ActionErrors();
		int numeroRegistro=0;
		int cantidadrepetidos=0;
		int cantidadrepetidosDescripcion=0;
		ArrayList<String> registrosRepetidos = new ArrayList<String>();
		ArrayList<String> registrosRepetidosDescripcion = new ArrayList<String>();
		String listaRepetidos="";
		String listaRepetidosDescripcion="";


		if (this.estado.equals(IconstantesEstadosJsp.ESTADO_GUARDAR_PARAMETRIZACION)) {

			//CODIGO REQUERIDO
			for (int i = 0; i < this.listaMotivosModificacionPresupuesto.size(); i++) {

				if ((this.listaMotivosModificacionPresupuesto.get(i).getCodigo()==null)||
						(this.listaMotivosModificacionPresupuesto.get(i).getCodigo().equals(""))) {
					numeroRegistro=0;
					numeroRegistro= i+1;
					errores.add(
							"codigo requerido",
							new ActionMessage(
									"errors.codigo.requerido",String.valueOf(numeroRegistro)));

					this.estado = IconstantesEstadosJsp.ESTADO_ERROR;

					i = this.listaMotivosModificacionPresupuesto.size()+1;

				}


				//DESCRIPCION REQUERIDA
				if (i < this.listaMotivosModificacionPresupuesto.size()) {


					if ((this.listaMotivosModificacionPresupuesto.get(i).getDescripcion()==null)||
							(this.listaMotivosModificacionPresupuesto.get(i).getDescripcion().equals("")))
					{
						numeroRegistro=0;

						numeroRegistro= i+1;
						errores.add(
								"descripcion requerido",
								new ActionMessage(
										"errors.descripcion.requerido",String.valueOf(numeroRegistro)));

						this.estado = IconstantesEstadosJsp.ESTADO_ERROR;

						i = this.listaMotivosModificacionPresupuesto.size()+1;

					}	

				}



			}


			//VALIDACION CODIGOS IGUALES

			for (int i = 0; i < this.listaMotivosModificacionPresupuesto.size(); i++) {
				String codigo=this.listaMotivosModificacionPresupuesto.get(i).getCodigo();
				registrosRepetidos = new ArrayList<String>();
				for (int j = 0; j < this.listaMotivosModificacionPresupuesto.size(); j++) {
					if (codigo.equals(this.listaMotivosModificacionPresupuesto.get(j).getCodigo())) {
						cantidadrepetidos=j+1;
						registrosRepetidos.add(String.valueOf(cantidadrepetidos));
					}

				}

				if (registrosRepetidos.size()>=2) {
					i = this.listaMotivosModificacionPresupuesto.size()+1;
					for (int j2 = 0; j2 < registrosRepetidos.size(); j2++) {



						if (j2!=registrosRepetidos.size()-1) {
							listaRepetidos+=String.valueOf(registrosRepetidos.get(j2))+",";
						}else{
							listaRepetidos+=String.valueOf(registrosRepetidos.get(j2));
						}

					}


					errores.add(
							"codigo repetido",
							new ActionMessage(
									"errors.codigo.repetido",listaRepetidos));
					this.estado = IconstantesEstadosJsp.ESTADO_ERROR;




				}
			}


			//VALIDACION DESCRIPCION REPETIDA
			for (int i = 0; i < this.listaMotivosModificacionPresupuesto.size(); i++) {
				String descripcion=this.listaMotivosModificacionPresupuesto.get(i).getDescripcion();
				registrosRepetidosDescripcion = new ArrayList<String>();
				for (int j = 0; j < this.listaMotivosModificacionPresupuesto.size(); j++) {
					if (descripcion.equals(this.listaMotivosModificacionPresupuesto.get(j).getDescripcion())) {
						cantidadrepetidosDescripcion=j+1;
						registrosRepetidosDescripcion.add(String.valueOf(cantidadrepetidosDescripcion));
					}

				}

				if (registrosRepetidosDescripcion.size()>=2) {
					i = this.listaMotivosModificacionPresupuesto.size()+1;
					for (int j2 = 0; j2 < registrosRepetidosDescripcion.size(); j2++) {



						if (j2!=registrosRepetidosDescripcion.size()-1) {
							listaRepetidosDescripcion+=String.valueOf(registrosRepetidosDescripcion.get(j2))+",";
						}else{
							listaRepetidosDescripcion+=String.valueOf(registrosRepetidosDescripcion.get(j2));
						}

					}


					errores.add(
							"descripcion repetida",
							new ActionMessage(
									"errors.descripcion.repetido",listaRepetidosDescripcion));
					this.estado = IconstantesEstadosJsp.ESTADO_ERROR;




				}
			}


		}
		return errores;
	}



	/**
	 * @return valor de listaMotivosModificacionPresupuesto
	 */
	public ArrayList<DtoMotivosModifiPresupuesto> getListaMotivosModificacionPresupuesto() {
		return listaMotivosModificacionPresupuesto;
	}



	/**
	 * @param listaMotivosModificacionPresupuesto el listaMotivosModificacionPresupuesto para asignar
	 */
	public void setListaMotivosModificacionPresupuesto(
			ArrayList<DtoMotivosModifiPresupuesto> listaMotivosModificacionPresupuesto) {
		this.listaMotivosModificacionPresupuesto = listaMotivosModificacionPresupuesto;
	}


}
