package com.princetonsa.actionform.administracion.semaforizacion;

import java.math.BigDecimal;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.constantes.estadosJsp.IconstantesEstadosJsp;


import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.enu.general.CarpetasArchivos;
import com.servinte.axioma.orm.ParametrizacionSemaforizacion;

public class SemaforizacionConsultasYReportesForm extends ValidatorForm {


	/** * Serial */
	private static final long serialVersionUID = 1L;

	/** * Variable para manejar la direcci&oacute;n del workflow  */
	private String estado;

	/**
	 *Tipo de reporte que se selecciono en la interfaz de usuario 
	 */
	private String tipoReporteAParametrizar;

	/**
	 *Listado de reportes que puede elegir el usuario 
	 */
	private ArrayList<DtoIntegridadDominio> listadoReportes;

	/**
	 * Listado de parametrizaciones que estan en la BD que se pueden eliminar, modificar y consultar
	 */
	private ArrayList<ParametrizacionSemaforizacion> listadoParametrizacionesPorReporte;

	/**
	 * Imagenes de interfaz
	 */
	private CarpetasArchivos carpetasArchivos=CarpetasArchivos.CONVENCION;

	/**
	 *Color asignado al registro 
	 */
	private String colorFondo;

	/**
	 * Indice que indica que elemento se va eliminar
	 */
	private String indiceDetalle;

	/**
	 *Listado de ID que pertenecen a elementos que se van a eliminar de la base de datos 
	 */
	private ArrayList<Long> listaElementosAEliminar;

	/**
	 * Instancia de parametrizacion que se va a adicionar 
	 */
	private ParametrizacionSemaforizacion parametrizacion;

	/**
	 * Mensajes parametrizados de error.
	 */
	private MessageResources messageResource = MessageResources.getMessageResources("com.servinte.mensajes.administracion.mantenimiento.SemaforizacionConsultasYReportesForm");


	/**
	 *Constructor 
	 */
	public SemaforizacionConsultasYReportesForm() {

		this.listadoReportes= new ArrayList<DtoIntegridadDominio>();
		this.listadoParametrizacionesPorReporte=new ArrayList<ParametrizacionSemaforizacion>();
		this.listaElementosAEliminar= new ArrayList<Long>();
		this.parametrizacion= new ParametrizacionSemaforizacion();
		this.colorFondo=new String("#000000");
	}




	/**
	 *Metodo de inicializacion 
	 */
	public void inicializar(){
		this.tipoReporteAParametrizar="";
		this.listadoReportes= new ArrayList<DtoIntegridadDominio>();
		this.listadoParametrizacionesPorReporte=new ArrayList<ParametrizacionSemaforizacion>();
		this.listaElementosAEliminar= new ArrayList<Long>();
		this.parametrizacion= new ParametrizacionSemaforizacion();
		this.colorFondo=new String("#000000");



	}


	/**
	 * @return the carpetasArchivos
	 */
	public CarpetasArchivos getCarpetasArchivos() {
		return carpetasArchivos;
	}

	/**
	 * @param carpetasArchivos the carpetasArchivos to set
	 */
	public void setCarpetasArchivos(CarpetasArchivos carpetasArchivos) {
		this.carpetasArchivos = carpetasArchivos;
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
	 * @return the tipoReporteAParametrizar
	 */
	public String getTipoReporteAParametrizar() {
		return tipoReporteAParametrizar;
	}

	/**
	 * @param tipoReporteAParametrizar the tipoReporteAParametrizar to set
	 */
	public void setTipoReporteAParametrizar(String tipoReporteAParametrizar) {
		this.tipoReporteAParametrizar = tipoReporteAParametrizar;
	}

	/**
	 * @return the listadoReportes
	 */
	public ArrayList<DtoIntegridadDominio> getListadoReportes() {
		return listadoReportes;
	}

	/**
	 * @param listadoReportes the listadoReportes to set
	 */
	public void setListadoReportes(ArrayList<DtoIntegridadDominio> listadoReportes) {
		this.listadoReportes = listadoReportes;
	}

	/**
	 * @return the listadoParametrizacionesPorReporte
	 */
	public ArrayList<ParametrizacionSemaforizacion> getListadoParametrizacionesPorReporte() {
		return listadoParametrizacionesPorReporte;
	}

	/**
	 * @param listadoParametrizacionesPorReporte the listadoParametrizacionesPorReporte to set
	 */
	public void setListadoParametrizacionesPorReporte(
			ArrayList<ParametrizacionSemaforizacion> listadoParametrizacionesPorReporte) {
		this.listadoParametrizacionesPorReporte = listadoParametrizacionesPorReporte;
	}


	/**
	 * @return the colorFondo
	 */
	public String getColorFondo() {
		return colorFondo;
	}


	/**
	 * @param colorFondo the colorFondo to set
	 */
	public void setColorFondo(String colorFondo) {
		this.colorFondo = colorFondo;
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


	public void eliminarParametrizacion(){
		ParametrizacionSemaforizacion para = this.listadoParametrizacionesPorReporte.get(Integer.valueOf(this.indiceDetalle));
		if (para.getInstituciones()!=null && para.getUsuarios()!=null && para.getFechaModifica()!=null) {
			this.listaElementosAEliminar.add(para.getCodigoPk());
		}
		this.listadoParametrizacionesPorReporte.remove(para);
	}





	/**
	 * @return the parametrizacion
	 */
	public ParametrizacionSemaforizacion getParametrizacion() {
		return parametrizacion;
	}


	/**
	 * @param parametrizacion the parametrizacion to set
	 */
	public void setParametrizacion(ParametrizacionSemaforizacion parametrizacion) {
		this.parametrizacion = parametrizacion;
	}


	/**
	 * Metodo que se encarga de realizar validaciones
	 * @see org.apache.struts.validator.ValidatorForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {

		ActionErrors errores = new ActionErrors();
		ArrayList<Integer> veces = new ArrayList<Integer>();
		ArrayList<ParametrizacionSemaforizacion> tmp = this.listadoParametrizacionesPorReporte;
		int cantidadVecesRepetido=0;
		ArrayList<Integer> registrosTraslapados = new ArrayList<Integer>();
		String traslapadoscadena="";
		int tmpTraslapados=0;
		Boolean validoNulos=false; 


		if (estado == null || estado.trim().isEmpty()) {
			errores.add("estado invalido", new ActionMessage(
			"errors.estadoInvalido"));
			return errores;
		}




		//VALIDACIONES EN ESTADO ADICION

		if ((this.estado
				.equals(IconstantesEstadosJsp.ESTADO_ADICIONAR_PARAMETRIZACION))) {



			// VALIDACIONES DE NULIDAD EN ESTADO DE ADICION 
			if (this.parametrizacion.getDescripcion() == null
					|| this.parametrizacion.getDescripcion().equals("")) {

				errores.add(
						"descripcion requerida",
						new ActionMessage(
								"errors.required",
								messageResource
								.getMessage("semaforizacion_error_descripcion_requerido")));

				this.estado = IconstantesEstadosJsp.ESTADO_NUEVA_PARAMETRIZACION;
			}

			if (this.parametrizacion.getRangoInicial() == null
					|| this.parametrizacion.getRangoInicial().equals("")) {

				errores.add(
						"rango inicial requerida",
						new ActionMessage(
								"errors.required",
								messageResource
								.getMessage("semaforizacion_error_rango_inicial_requerido")));

				this.estado = IconstantesEstadosJsp.ESTADO_NUEVA_PARAMETRIZACION;
			}

			if (this.parametrizacion.getRangoFinal() == null
					|| this.parametrizacion.getRangoFinal().equals("")) {

				errores.add(
						"rango inicial requerida",
						new ActionMessage(
								"errors.required",
								messageResource
								.getMessage("semaforizacion_error_rango_final_requerido")));
				this.estado = IconstantesEstadosJsp.ESTADO_NUEVA_PARAMETRIZACION;
			}

			if (this.parametrizacion.getColor() == null
					|| this.parametrizacion.getColor().equals("")) {

				errores.add(
						"color requerida",
						new ActionMessage(
								"errors.required",
								messageResource
								.getMessage("semaforizacion_error_color_requerido")));
				this.estado = IconstantesEstadosJsp.ESTADO_NUEVA_PARAMETRIZACION;
			}


			//VALIDACION RANGO FINAL MAYOR QUE RANGO INICIAL

			if (this.parametrizacion.getRangoFinal()!=null && this.parametrizacion.getRangoInicial()!=null) {

				if ((this.parametrizacion.getRangoInicial().compareTo(this.parametrizacion.getRangoFinal())==0)
						|| (this.parametrizacion.getRangoInicial().compareTo(this.parametrizacion.getRangoFinal())==1)) {

					errores.add(
							"rango final mayor a inicial",
							new ActionMessage(
									"errors.rangos.adicion",
									messageResource
									.getMessage("semaforizacion_erro_rangos_adicion")));
					this.estado = IconstantesEstadosJsp.ESTADO_NUEVA_PARAMETRIZACION;



				}


			}


			//RANGO UNICO EN ADICION 
			for (int i = 0; i < this.listadoParametrizacionesPorReporte.size(); i++) {
				BigDecimal rI = this.parametrizacion.getRangoInicial();
				BigDecimal rF = this.parametrizacion.getRangoFinal();

				if ((this.listadoParametrizacionesPorReporte.get(i).getRangoInicial().compareTo(rI)==0)&&
						(this.listadoParametrizacionesPorReporte.get(i).getRangoFinal().compareTo(rF)==0)) {

					errores.add(
							"rango unico",
							new ActionMessage(
									"errors.rango.unicos.adicion",
									messageResource
									.getMessage("semaforizacion_erro_rangos_unicos")));
					this.estado = IconstantesEstadosJsp.ESTADO_NUEVA_PARAMETRIZACION;
				}




			}





			// COLOR UNICO	EN ADICION
			if (this.parametrizacion.getColor()!=null && !this.parametrizacion.getColor().equals("") ) {


				for (int i = 0; i < this.listadoParametrizacionesPorReporte.size(); i++) {
					if (this.listadoParametrizacionesPorReporte.get(i).getColor().equals(this.parametrizacion.getColor())) {




						errores.add(
								"color unico adicion",
								new ActionMessage(
										"errors.color.repetido.adicion",
										messageResource
										.getMessage("semaforizacion_erro_color_unico_adicion")));

						this.estado = IconstantesEstadosJsp.ESTADO_NUEVA_PARAMETRIZACION;

					}
				}


			}




			//VALIDACION DE RANGOS ADICION 
			//VALIDACION RANGOS NO SE TRASLAPEN 
			cantidadVecesRepetido=0;
			registrosTraslapados = new ArrayList<Integer>();
			traslapadoscadena="";
			tmpTraslapados=0;



			BigDecimal rI = this.parametrizacion.getRangoInicial();
			BigDecimal rF = this.parametrizacion.getRangoFinal();
			registrosTraslapados = new ArrayList<Integer>();
			for (int j = 0; j < this.listadoParametrizacionesPorReporte.size(); j++) {



				if ((((rI.compareTo(this.listadoParametrizacionesPorReporte.get(j).getRangoInicial())==0)||
						(rI.compareTo(this.listadoParametrizacionesPorReporte.get(j).getRangoInicial())==1))&&( 
								(rI.compareTo(this.listadoParametrizacionesPorReporte.get(j).getRangoFinal())==0)||
								(rI.compareTo(this.listadoParametrizacionesPorReporte.get(j).getRangoFinal())==-1)))||
								(((rF.compareTo(this.listadoParametrizacionesPorReporte.get(j).getRangoInicial())==0)||
										(rF.compareTo(this.listadoParametrizacionesPorReporte.get(j).getRangoInicial())==1))&&( 
												(rF.compareTo(this.listadoParametrizacionesPorReporte.get(j).getRangoFinal())==0)||
												(rF.compareTo(this.listadoParametrizacionesPorReporte.get(j).getRangoFinal())==-1))))

				{


					//SOLO SE MUESTRAN LOS TRASLAPADOS CON RESPECTO A UN SOLO CAMPO POR QUE SI S EMUESTRAN TODOS EL USUARIO NO SABE CUALES SE ESTAN
					//TRASLAPANDO

					registrosTraslapados.add(j);
				}


			}

			if (registrosTraslapados.size()>=1) {

				for (int j2 = 0; j2 < registrosTraslapados.size(); j2++) {

					tmpTraslapados = registrosTraslapados.get(j2);
					tmpTraslapados=tmpTraslapados+1;

					if (j2!=registrosTraslapados.size()-1) {
						traslapadoscadena+=String.valueOf(tmpTraslapados)+",";
					}else{
						traslapadoscadena+=String.valueOf(tmpTraslapados);
					}

				}

				//ERROR 

				errores.add(
						"rangos traslapados",
						new ActionMessage(
								"errors.rango.traslapados.adcicion",traslapadoscadena));
				this.estado = IconstantesEstadosJsp.ESTADO_NUEVA_PARAMETRIZACION;




			}








		}

		
		
		
		
		
		
		
		//VALIDACIONES DE REQUERIDO DE REPORTES
		if ((this.estado
				.equals(IconstantesEstadosJsp.ESTADO_SELECCION_COMBO_REPORTES))) {

			if (this.tipoReporteAParametrizar == null
					|| this.tipoReporteAParametrizar.equals(String.valueOf(ConstantesBD.codigoNuncaValido))) {
				
				validoNulos=true;
				errores.add(
						"color requerida",
						new ActionMessage(
								"errors.required",
								messageResource
								.getMessage("semaforizacion_error_tipo_reporte_requerido")));
			}

		}



		//VALIDACIONES DE ESTADO GUARDAR 
		if (this.estado
				.equals(IconstantesEstadosJsp.ESTADO_GUARDAR_PARAMETRIZACION)) {


			//DESCRIPCION REUQERIDA
			for (int i = 0; i < this.listadoParametrizacionesPorReporte.size(); i++) {
				if (this.listadoParametrizacionesPorReporte.get(i).getDescripcion() == null
						|| this.listadoParametrizacionesPorReporte.get(i).getDescripcion().equals("")) {
					validoNulos=true;
					errores.add(
							"descripcion requerida",
							new ActionMessage(
									"errors.nombreVacio2",
									messageResource
									.getMessage("semaforizacion_error_descripcion_vacio")));

					this.estado = IconstantesEstadosJsp.ESTADO_GUARDAR_PARAMETRIZACION;
				}

				//RANGO INICIAL REQUERIDO
				if (this.listadoParametrizacionesPorReporte.get(i).getRangoInicial() == null
						|| this.listadoParametrizacionesPorReporte.get(i).getRangoInicial().equals("")) {
					validoNulos=true;
					errores.add(
							"rango inicial requerida",
							new ActionMessage(
									"errors.rango.vacio",
									messageResource
									.getMessage("semaforizacion_error_rango_inicial_vacio")));

					this.estado = IconstantesEstadosJsp.ESTADO_GUARDAR_PARAMETRIZACION;
				}

				//RANGO FINAL REQUERIDO
				if (this.listadoParametrizacionesPorReporte.get(i).getRangoFinal() == null
						|| this.listadoParametrizacionesPorReporte.get(i).getRangoFinal().equals("")) {
					validoNulos=true;
					errores.add(
							"rango inicial requerida",
							new ActionMessage(
									"errors.rango.vacio",
									messageResource
									.getMessage("semaforizacion_error_rango_final_vacio")));
					this.estado = IconstantesEstadosJsp.ESTADO_GUARDAR_PARAMETRIZACION;
				}

				//COLOR REQUERIDO
				if (this.listadoParametrizacionesPorReporte.get(i).getColor() == null
						|| this.listadoParametrizacionesPorReporte.get(i).getColor().equals("")) {
					validoNulos=true;
					errores.add(
							"color requerida",
							new ActionMessage(
									"errors.nombreVacio",
									messageResource
									.getMessage("semaforizacion_error_color_vacio")));
					this.estado = IconstantesEstadosJsp.ESTADO_GUARDAR_PARAMETRIZACION;
				}



				//VALIDACION RANGO FINAL MAYOR A INICIAL
				if (this.listadoParametrizacionesPorReporte.get(i).getRangoInicial()!=null && this.listadoParametrizacionesPorReporte.get(i).getRangoFinal()!=null ) {



					if ((this.listadoParametrizacionesPorReporte.get(i).getRangoInicial().compareTo(this.listadoParametrizacionesPorReporte.get(i).getRangoFinal())==0)||
							((this.listadoParametrizacionesPorReporte.get(i).getRangoInicial().compareTo(this.listadoParametrizacionesPorReporte.get(i).getRangoFinal())==1))) {

						int numeroRegistro=i;
						numeroRegistro=numeroRegistro+1;
						errores.add(
								"color repetido consulta",
								new ActionMessage(
										"errors.rangos.modificacion" ,(String.valueOf(numeroRegistro)).toString()));
						this.estado = IconstantesEstadosJsp.ESTADO_GUARDAR_PARAMETRIZACION;




					}

				}



			}



			//VALIDACION COLOR REPETIDO EN REGISTRO DE TABLAS.

			String registrosACambiarColor=" ";
			int campo=0;
			for (int j = 0; j <tmp.size(); j++) {
				registrosACambiarColor="";
				veces= new ArrayList<Integer>();
				for (int j2 = 0; j2 < tmp.size(); j2++) {
					if (tmp.get(j).getColor().equals(tmp.get(j2).getColor())) {
						veces.add(j2);

					}
				}

				if (veces.size()>= ConstantesBD.cantidadVecesRepetidoElemento) {


					for (int j2 = 0; j2 < veces.size(); j2++) {

						campo = veces.get(j2);
						campo=campo+1;

						if (j2!=veces.size()-1) {
							registrosACambiarColor+=String.valueOf(campo)+",";
						}else{
							registrosACambiarColor+=String.valueOf(campo);
						}

					}

					j=tmp.size()+1;
					errores.add(
							"color repetido consulta",
							new ActionMessage(
									"errors.color.repetido",registrosACambiarColor));
					this.estado = IconstantesEstadosJsp.ESTADO_GUARDAR_PARAMETRIZACION;
				}






			}


			
			
			
			
			
			
			
			
			
			

			//VALIDACION DE RANGOS UNICOS 
			ArrayList<Integer> repetidos = new ArrayList<Integer>();
			int repetido=0;
			String listaRepetidos="";
			for (int i = 0; i < this.listadoParametrizacionesPorReporte.size(); i++) {
				if (validoNulos==false ) {
					BigDecimal rI = this.listadoParametrizacionesPorReporte.get(i).getRangoInicial();
					BigDecimal rF = this.listadoParametrizacionesPorReporte.get(i).getRangoFinal();
					repetidos = new ArrayList<Integer>();
					for (int k = 0; k < this.listadoParametrizacionesPorReporte.size(); k++) {
						if ((this.listadoParametrizacionesPorReporte.get(k).getRangoInicial().compareTo(rI)==0 ) 
								&& (this.listadoParametrizacionesPorReporte.get(k).getRangoFinal().compareTo(rF)==0)){

							repetidos.add(k);


						}
					}


					if (repetidos.size()>=2) {
						i = this.listadoParametrizacionesPorReporte.size()+1;
						for (int j2 = 0; j2 < repetidos.size(); j2++) {

							repetido = repetidos.get(j2);
							repetido=repetido+1;

							if (j2!=repetidos.size()-1) {
								listaRepetidos+=String.valueOf(repetido)+",";
							}else{
								listaRepetidos+=String.valueOf(repetido);
							}

						}


						errores.add(
								"rangos traslapados",
								new ActionMessage(
										"errors.rangos.repetidos",listaRepetidos));
						this.estado = IconstantesEstadosJsp.ESTADO_GUARDAR_PARAMETRIZACION;



					}

				}



			}





			//VALIDACION RANGOS NO SE TRASLAPEN 
			cantidadVecesRepetido=0;
			registrosTraslapados = new ArrayList<Integer>();
			traslapadoscadena="";
			tmpTraslapados=0;

			for (int i = 0; i <this.listadoParametrizacionesPorReporte.size(); i++) {
				if (validoNulos==false) {
					BigDecimal rI = this.listadoParametrizacionesPorReporte.get(i).getRangoInicial();
					BigDecimal rF = this.listadoParametrizacionesPorReporte.get(i).getRangoFinal();
					registrosTraslapados = new ArrayList<Integer>();
					for (int j = 0; j < this.listadoParametrizacionesPorReporte.size(); j++) {



						if ((((rI.compareTo(this.listadoParametrizacionesPorReporte.get(j).getRangoInicial())==0)||
								(rI.compareTo(this.listadoParametrizacionesPorReporte.get(j).getRangoInicial())==1))&&( 
										(rI.compareTo(this.listadoParametrizacionesPorReporte.get(j).getRangoFinal())==0)||
										(rI.compareTo(this.listadoParametrizacionesPorReporte.get(j).getRangoFinal())==-1)))||
										(((rF.compareTo(this.listadoParametrizacionesPorReporte.get(j).getRangoInicial())==0)||
												(rF.compareTo(this.listadoParametrizacionesPorReporte.get(j).getRangoInicial())==1))&&( 
														(rF.compareTo(this.listadoParametrizacionesPorReporte.get(j).getRangoFinal())==0)||
														(rF.compareTo(this.listadoParametrizacionesPorReporte.get(j).getRangoFinal())==-1))))

						{


							//SOLO SE MUESTRAN LOS TRASLAPADOS CON RESPECTO A UN SOLO CAMPO POR QUE SI S EMUESTRAN TODOS EL USUARIO NO SABE CUALES SE ESTAN
							//TRASLAPANDO

							registrosTraslapados.add(j);
						}


					}

					if (registrosTraslapados.size()>=2) {
						i = this.listadoParametrizacionesPorReporte.size()+1;
						for (int j2 = 0; j2 < registrosTraslapados.size(); j2++) {

							tmpTraslapados = registrosTraslapados.get(j2);
							tmpTraslapados=tmpTraslapados+1;

							if (j2!=registrosTraslapados.size()-1) {
								traslapadoscadena+=String.valueOf(tmpTraslapados)+",";
							}else{
								traslapadoscadena+=String.valueOf(tmpTraslapados);
							}

						}

						//ERROR 

						errores.add(
								"rangos traslapados",
								new ActionMessage(
										"errors.rangos.traslapados.modificacion",traslapadoscadena));
						this.estado = IconstantesEstadosJsp.ESTADO_GUARDAR_PARAMETRIZACION;




					}
				}


			}






		}

		return errores;

	}



}
