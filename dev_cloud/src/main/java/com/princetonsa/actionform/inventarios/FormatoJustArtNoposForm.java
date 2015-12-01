/**
 * 
 */
package com.princetonsa.actionform.inventarios;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.FormatoJustArtNopos;
import com.servinte.axioma.servicio.formatoJust.ComportamientoCampo;

/**
 * @author axioma
 *
 */
public class FormatoJustArtNoposForm extends ValidatorForm
{

	/**
	 * Loggers de la clase ReporteReferenciaExternaForm
	 */
	Logger logger = Logger.getLogger(FormatoJustArtNoposForm.class);
	
	/**
	 * Estado de la forma
	 */
	private String estado;

	/**
	 * indice justificacion
	 */
	
	private int numjus;
	
	
	/**
	 *HashMap Mapa donde se almacenan los mapas de cada una de las secciones del formulario. 
	 */
	private HashMap formularioMap;

	
	/**
	 * String que almacena el numero de la solicitud que se envia desde donde se hace el llamado al formulario de la justificacion
	 */
	private String codigoSolicitud="";
	
	 
	/**
	 *  valor del check si o no , riesgo se encuentra soportado en H.C
	 */
	private String riesgoSoportado;
	
	/**
	 * mapa don se almacenan 
	 */
	private HashMap hijosMap;
	
	/**
	 * Mapa de secciones
	 */
	private HashMap mapaSecciones;
	
	/**
	 * Mapa se campos llaves de la forma campo_seccion_
	 */
	private HashMap camposSecciones;
	
	/**
	 * Variable  donde se almacena el path de los archivos adjuntos 
	 */
	private String archivo;
	
	/**
	 * Mapa de los medicamentos pos
	 */
	private HashMap medicamentosPos;
	
	
	/**
	 * String donde se almacena el valor del selesct de medicamentos.
	 */
	private String medicamentoPos;
	
	/**
	 * String donde se almacena la captura de la respueta clinica y paraclinica alcanzada del medicamento pos selecccionado
	 */
	private String respuestaClinicaParaclinica="";
	
	/**
	 * String donde se almacena el resumen de historia que justifica el uso del medicamento no pos
	 */
	private String observacionesResumen="";
	
	
	/**
	 * 
	 */
	private String diasTratamiento;
	
	/**
	 * Mapa donde se alamcena la informacion del medicamento no pos
	 */
	private HashMap medicamentosNoPos=new HashMap();
	
	/**
	 * string que controla si se le informa al paciente de los efectos del medicamento
	 */
	private String infopac=ConstantesBD.acronimoSi;
	
	
	/**
	 * Aqui se captura el diagnostico de ingreso
	 * (Completo compuesto por codigo/CIE/nombre, 
	 * separado por "-")
	 */
	private String diagnosticoIngreso_1="";

	/**
	 * Aqui se captura el diagnostico de complicación
	 * (Completo compuesto por codigo/CIE/nombre, 
	 * separado por "-")
	 */
	private String diagnosticoComplicacion_1="";
	
	
	/**
	 * Aqui se captura el diagnostico de muerte
	 * (Completo compuesto por codigo/CIE/nombre, 
	 * separado por "-")
	 */
	private String diagnosticoMuerte_1="";
	
	/**
	 * Para manejar los diagnosticos presuntivos principal y relacionados. El
	 * valor viene de forma 'codigo- nombre'
	 */
	private Map diagnosticosPresuntivos = new HashMap();
	
	/**
	 * Define si  los diagnosticos presuntivos de 
	 * la evolución del paciente van o no a la 
	 * epicrisis
	 */
	private boolean diagnosticosPresuntivosBoolean=false;
	 
	/**
	 * Entero para saber cuantos diagnosticos presuntivos se 
	 * generaron dinámicamente 
	 */
	private int numDiagnosticosPresuntivos = 0;

	/**
	 * Entero para saber cuantos diagnosticos presuntivos
	 * existían en el objeto (Restricción de adjunto no
	 * puede modificar antiguos)
	 */
	private int numDiagnosticosPresuntivosOriginal = 0;

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
	 * Tipo de diagnostico
	 */
	private int tipoDiagnosticoPrincipal;
	
	/**
	 * Descripción de las complicaciones de la 
	 * enfermedad del paciente.
	 */
	private String descripcionComplicacion="";

	
	
	/**
	 * String donde se almacena el emisor del formulario, para identificar el formato requerido dependiendo de el procesos en ejecucion
	 */
	private String emisor;
	

//parametros enviados desde el emisor
	
	/**
	 * String donde se almacena el valor del codigo del medicamento no pos a justificar, este parametro se envia desde donde se hace 
	 * el llamado a la justificacion
	 */
	private String medicamentoNoPos="";
	
	/**
	 *  valor de la dosis enviada desde donde se efectua el llamado
	 */ 
	private String dosis;
	/**
	 * valor de la unidosis enviada desde donde se efectua el llamado
	 */ 
	private int unidosis;
	/**
	 * valor de la frecuencia enviada desde donde se efectua el llamado
	 */
	private String frecuencia;
	/**
	 * valor del tipo de frecuencia enviado desde donde se efectua el llamado
	 */
	private String tipoFrecuencia;
	/**
	 * valor del total de unidades enviado desde donde se efectua el llamado
	 */
	private int totalUnidades;
	
	/**
	 * valor del tiempo del tratmaiento en dias este parametro es enviado de donde se efectue el llamado al formulario 
	 */
	private int tiempoTratamiento;
	
	//
	
	
	
	/**
	 * Mapa donde se almacenan los datos de los medicamentos sustitutos
	 */
	private HashMap sustitutosNoPos;
	
	
	/**
	 * valor del select de sustitutos
	 */
	private int indexSelectSustitutos;
	
	/**
	 * varibale que controla la visibilidad de la capa
	 */
	private String capa;
	
	/**
	 * 
	 */
	private HashMap informacionArtPrin;
	
	/**
	 * 
	 */
	private HashMap formaFconcMap;
	
	/**
	 * indicativo de tipo de articulo
	 */
	private boolean esInsumo;
	
	/**
	 * numero de justificacion en el proceso
	 */
	private String nojustificacion;
	
	/**
	 * numero de justificacion en el proceso
	 */
	private boolean seVaAAsociarJustificacion; 
	
	/**
	 * subcuenta
	 */
	private String subCuenta="";
	
	/**
	 * Contiene las Subcuentas asociadas a una solicitud
	 */
	private HashMap subCuentasMap = new HashMap();
	
	/**
	 * Contiene los codigos de justificacion asociadas a una solicitud 
	 */
	
	private HashMap AsocioMapas = new HashMap();
	/**
	 * Contiene la Sub Cuenta Seleccionada
	 */
	private String subCuentas;
	
	/**
	 * valor parametro general permitirModificarTiempoTratamientoJustificacionNopos
	 */
	private String permitirModificarTiempoTratamientoJustificacionNopos;
	
	/**
	 * 
	 */
	private int consecutivoJustActual;
	
	/**
	 *Flag que indica si un articulo es insumo o no  
	 */
	private boolean esInsumoRango;
	
	
// %%%%%%%%%%%%%% atributos funcionalidad anexo 584 %%%%%%%%%%%%%%%%%%%%%%%%%%%	
	
	private HashMap ingresos=new HashMap();
	private String patronOrdenar="";
	private String ingreso="";
	private HashMap ingresosArticulos=new HashMap();
	private String cuenta="";
	private String convenio="";
	private String centroCosto="";
	private String ultimoPatron;
	private ArrayList<HashMap<String,Object>> centroAtencion;
	private ArrayList<HashMap<String,Object>> centrosCosto;
	private ArrayList<HashMap<String,Object>> viaIngresoPaciente;
	private ArrayList<HashMap<String,Object>> convenios;
	private String fechaInicial;
	private String fechaFinal;
	private HashMap filtros;
	private HashMap filtrosRecargar;
	
	private String tipoCodigo;
	private String codigoCentroAtencion;
	private String codigoViaIngreso;
	private String codigoArticulo;
	private String descripcionArticulo;
	private HashMap articulosMap;
	private int indexMap;
	private String subcuentascantidad;
	private String menurol="";
	private int cargarsi;
	private ArrayList<HashMap<String,Object>> pisos;
	private ArrayList<HashMap<String,Object>> profesionales;
	
	
	// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%	
	
	private int imprimir=0;
	
	private String nombreMapaJus = "";

	/*
	 * Para salvar el número de solicitud antes del reset subcuentas
	 */
	private String numSolicitudSalvado = "";
	
	/*
	 * Para salvar el código del artículo antes del reset de subcuentas
	 */
	private String codigoArticuloSalvado = "";
	
	/*
	 * Para salvar Se han utilizado medicamentos POS en el tratamiento del paciente?
	 */
	private String seHanUtilizadoMedicamentosPosEnElTratamientoDelPaciente= "";
	
	/*
	 * Variable que controla cargar la informacion de la justificación
	 * NO se debe de cargar la informació cuando en una MODIFICAICÓN salga algún error de validación
	 */
	private boolean hayErrores;
	
	/**
	 * Justificación Historica
	 */
	private HashMap<String, Object> justificacionHistorica;
	/**
	 * posicion registro
	 * */
	private String posicionRegistro;
	/**
	 * posicion campo
	 * */
	private String idHtmlCampo;
	/**
	 * identificador campo
	 * */
	private String identificadorCampo;
	/**
	 * numero de la seccion del campo
	 * */
	private String numeroSeccion;
	
	
	/**
	 * Bandera que dice que se le ha informado al paciente de los riesgos del tratamiento 
	 */
	private boolean seInformaAlPacienteRiesgosTratamiento=false;
	
	/**
	 * Existen sustitutos 
	 */
	private boolean existeSustituto=false;
	
	private boolean provieneOrdenAmbulatoria=false;
	
	/**
	 * Numero de la orden Ambulatoria
	 * */
	private int codigoOrden = ConstantesBD.codigoNuncaValido;
	
	//METODOS DE LA FORMA
	/**
	 * Metodo que resetea la forma cuando esta ejecutando la funcionalidad anexo 584 de insertar-consultar/modificar
	 */
	public void resetanexo(){
		this.patronOrdenar="";
		this.ultimoPatron="";
		this.cargarsi=ConstantesBD.codigoNuncaValido;
		this.indexMap=ConstantesBD.codigoNuncaValido;
		this.subcuentascantidad="";
		this.codigoArticulo="";
		this.descripcionArticulo="";
		this.filtros=new HashMap();
		this.filtrosRecargar= new HashMap();
		this.centroAtencion=new ArrayList();
		this.centrosCosto=new ArrayList();
		this.viaIngresoPaciente=new ArrayList();
		this.convenios= new ArrayList();
		this.fechaFinal="";
		this.fechaInicial="";
		this.ingresos=new HashMap();
		this.ingresosArticulos=new HashMap();
		this.ingresos.put("numRegistros", 0);
		this.ingresosArticulos.put("numRegistros", 0);
		this.capa="false";
		this.patronOrdenar="";
		this.ingreso="";
		this.cuenta="";
		this.convenio="";
		this.centroCosto="";
		this.tipoCodigo="";
		this.articulosMap=new HashMap();
		articulosMap.put("numRegistros", 0);
		this.articulosMap.put("codigosArticulos", "");
		this.pisos= new ArrayList();
		this.profesionales= new ArrayList();
		this.imprimir=0;
		this.infopac=ConstantesBD.acronimoSi;
		this.diasTratamiento="";
		this.permitirModificarTiempoTratamientoJustificacionNopos="";
		this.consecutivoJustActual=ConstantesBD.codigoNuncaValido;
		this.justificacionHistorica = new HashMap<String, Object>();
		this.justificacionHistorica.put("numRegistros", 0);
		this.esInsumoRango=false;
	}

	public String getSubCuentas() {
		return subCuentas;
	}
	public void setSubCuentas(String subCuentas) {
		this.subCuentas = subCuentas;
	}
	/**
	 * Validación del form
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errors=new ActionErrors();
		
		String [] key={};
		String valuekey="";
		
		String[] indices=FormatoJustArtNopos.indicesformulariomap;
		String[] indicesnopos=FormatoJustArtNopos.indicesnoposmap;
		String[] indicespos=FormatoJustArtNopos.indicesposmap;
		String[] indicessusti=FormatoJustArtNopos.indicessustimap;
		String[] indicesdiag=FormatoJustArtNopos.indicesdiagmap;
		String[] indicesdiag1=FormatoJustArtNopos.indicesdiag1map;
		
		logger.info("===> Entramos al Validate, el estado es: "+estado);
		logger.info("===> Segundo Radio : "+this.seHanUtilizadoMedicamentosPosEnElTratamientoDelPaciente);
		
		if(estado.equals("empezar"))
		{
			logger.info("===> Vamos a resetear el segundo radio botton");
			this.setSeHanUtilizadoMedicamentosPosEnElTratamientoDelPaciente("");
		}
		
		if(estado.equals("cargarInfoMedicamentosPos"))
		{
			logger.info("===> cambió el segundo radio button");
			this.getSeHanUtilizadoMedicamentosPosEnElTratamientoDelPaciente();
		}
		boolean yaSeMostroMensajeMedicamentos = false;
		boolean yaSeMostroMensajeInfoPaciente = false;
		boolean yaSeMostroMensajeNoGenerarSolicitud = false;
		boolean validacionMedicamentos = false;
		boolean validacionInformaPaciente = false;
		boolean validacionNoGenerarSolicitud = false;
		
		if(estado.equals("guardar") || estado.equals("guardar1"))
		{
			//System.out.println("NUMJUS - "+this.consecutivoJustActual);
			if (this.consecutivoJustActual==ConstantesBD.codigoNuncaValido)
			{
				errors.add("Validación consecutivo", new ActionMessage("errors.notEspecific","Se debe parametrizar el consecutivo para la justificación de articulos "));
				this.estado = "empezar";
			}
//			logger.info("Hola Pipe !!! :P Entramos a validar");
			/*for (int i=0;i<indices.length;i++)
			{
				key=indices[i].toString().split("_");
				if(key[0].equals("requerido"))
				{
					for(int k=1;k<key.length;k++)
					{
						valuekey+="_"+key[k];
					}
					if(formularioMap.get(indices[i]).toString().equals(ConstantesBD.acronimoSi) && formularioMap.get("valorcampo"+valuekey).toString().equals(null)
						|| formularioMap.get(indices[i]).toString().equals(ConstantesBD.acronimoSi) && formularioMap.get("valorcampo"+valuekey).toString().equals(""))
					{
						errors.add("Motivo", new ActionMessage("errors.required",formularioMap.get("etiquetacampo"+valuekey)+""));
						this.estado="empezar";
					}
					valuekey="";
				}
			}*/
			/*
			logger.info("===> A validar !!!");
			logger.info("===> seHanUtilizadoMedicamentosPosEnElTratamientoDelPaciente = *"+
					this.seHanUtilizadoMedicamentosPosEnElTratamientoDelPaciente+"*");
			logger.info("===> capa = *"+this.capa+"*");
			logger.info("===> medicamentoPos = *"+this.medicamentoPos+"*");
			logger.info("===> respuestaClinicaParaclinica = *"+this.respuestaClinicaParaclinica+"*");
			*/
			if (UtilidadTexto.getBoolean(this.seHanUtilizadoMedicamentosPosEnElTratamientoDelPaciente))
			{
				logger.info("===> Entré al condicional 1 !!! ");
				if (this.medicamentoPos.equals("") || this.medicamentoPos.equals(""+ConstantesBD.codigoNuncaValido))
				{
					//logger.info("===> Entré al condicional 2 !!! ");
					errors.add("Motivo", new ActionMessage("errors.required", "Medicamento POS Utilizado "));
					this.estado="empezar";
				}
				if(this.respuestaClinicaParaclinica.trim().equals(""))
				{
					//logger.info("===> Entré al condicional 3 !!! ");
					errors.add("Motivo", new ActionMessage("errors.required", "Respuesta Clínica y Paraclínica Alcanzada "));
					this.estado="empezar";
				}
			}
			if(observacionesResumen.trim().equals(""))
			{
				errors.add("Motivo", new ActionMessage("errors.required", "Resumen de Historia que Justifique el Uso del Medicamento NO POS "));
				this.estado="empezar";
			}
			/*
			if(infopac.equals(ConstantesBD.acronimoNo))
			{
				errors.add("Motivo", new ActionMessage("errors.required", "No informa al paciente los efectos secundarios y posibles riesgos al tratamiento "));
				this.estado="empezar";
			}
			*/
			this.getFormularioMap().put("mapasecciones", this.getMapaSecciones());
			HashMap mapaSecciones=(HashMap) this.getMapaSecciones();
			int numRegSecciones=Integer.parseInt((String)(mapaSecciones.get("numRegistros")));
			
			for(int i=0;i<numRegSecciones;i++){
				int numRegistrosCampos=Integer.parseInt((String)this.formularioMap.get("numRegistros_"+mapaSecciones.get("codigo_"+i)));
				String idSeccion=mapaSecciones.get("codigo_"+i).toString();
				if(!idSeccion.equals("21")&&!idSeccion.equals("25")){
					for(int t=0; t< numRegistrosCampos;t++)
					{
						if(this.formularioMap.get("tipo_"+idSeccion+"_"+t).toString().equals("CHEC") &&
							UtilidadTexto.getBoolean(this.formularioMap.get("mostrar_"+idSeccion+"_"+t)+"")&&
							UtilidadTexto.getBoolean(this.formularioMap.get("requerido_"+idSeccion+"_"+t)+""))
						{
							boolean seleccionado= false;
							if(this.formularioMap.get("numRegistros_"+idSeccion+"_"+
									this.formularioMap.get("campo_"+idSeccion+"_"+t))!=null){
								for(int x=0; x< Integer.parseInt(this.formularioMap.get("numRegistros_"+idSeccion+"_"+
										this.formularioMap.get("campo_"+idSeccion+"_"+t))+"");x++)
								{
									if(this.formularioMap.get("campopadre_"+idSeccion+"_"+this.formularioMap.get("campo_"+
											idSeccion+"_"+t).toString()+"_"+x).toString().equals(this.formularioMap.get("campo_"+
													idSeccion+"_"+t).toString()))	
									{
										
										if(UtilidadTexto.getBoolean(this.formularioMap.get("valorcampo_"+idSeccion+"_"+
												this.formularioMap.get("campo_"+idSeccion+"_"+t).toString()+"_"+x)+""))
										{
											seleccionado=true;
										}
									}
								}
							}
				
							if(!seleccionado)
							{
								errors.add("", new ActionMessage("errors.required", this.formularioMap.get("etiquetacampo_"+idSeccion+"_"+t)));
								this.estado="empezar";
							}
						}else{
							if(!this.formularioMap.get("tipo_"+idSeccion+"_"+t).toString().equals("CHEC") &&
								UtilidadTexto.getBoolean(this.formularioMap.get("mostrar_"+idSeccion+"_"+t)+"")&&
								UtilidadTexto.getBoolean(this.formularioMap.get("requerido_"+idSeccion+"_"+t)+""))
							{
								/*if(this.formularioMap.get("numRegistros_"+idSeccion+"_"+
										this.formularioMap.get("campo_"+idSeccion+"_"+t))!=null){*/
									if(this.formularioMap.get("valorcampo_"+idSeccion+"_"+t) == null||this.formularioMap.get("valorcampo_"+idSeccion+"_"+t).toString().trim().equals("")){
										errors.add("", new ActionMessage("errors.required", this.formularioMap.get("etiquetacampo_"+idSeccion+"_"+t)));
										this.estado="empezar";
									}
								//}
							}
						}
					}
				}
			}
			
			//el codigo 21 es el identificador del campo que define si se usan medicamentos POS
			int[] valores = ComportamientoCampo.buscarIdentificadoresCampo(this.formularioMap,mapaSecciones ,numRegSecciones , 21+"");
			if(valores!=null){
				if((this.formularioMap.get("valorcampo_"+valores[2]+"_"+valores[0]) == null 
						||this.formularioMap.get("valorcampo_"+valores[2]+"_"+valores[0]).toString().trim().equals("")))
				{
					//logger.info("===> Voy a poner ValidacionMedicamentos en true !!!");
					validacionMedicamentos = true;
				}
			}
			
			/*
			  * Validación, Se informa al paciente los efectos secundarios y posibles riesgos al tratamiento, manifiesta haberlos 
			  * entendido y autoriza el uso del medicamento
			  * logger.info("===> El valor de la validacion de medicamentos POS es: valor_"+
					ConstantesBD.JusSeccionMedicamentosPos+"_"+x+" "+formularioMap.get("valor_"+
							ConstantesBD.JusSeccionMedicamentosNopos+"_"+x));
			  */
			//el codigo 27 es el identificador del campo que define si se informo al paciente de los efectos del tratamiento
			valores = ComportamientoCampo.buscarIdentificadoresCampo(this.formularioMap,mapaSecciones ,numRegSecciones , 27+"");
			if(valores!=null){
				if((this.formularioMap.get("valorcampo_"+valores[2]+"_"+valores[0]) == null 
						||this.formularioMap.get("valorcampo_"+valores[2]+"_"+valores[0]).toString().trim().equals("") ))
				{
					//logger.info("===> Voy a poner validacionInformaPaciente en true !!!");
					validacionInformaPaciente = true;
				}
			}
			
			if(this.formularioMap.get("valorcampo_"+valores[2]+"_"+valores[0]) != null &&
				!UtilidadTexto.getBoolean(this.formularioMap.get("valorcampo_"+valores[2]+"_"+valores[0]).toString()))
			{
				//logger.info("===> Voy a poner validacionNoGenerarSolicitud en true !!!");
				validacionNoGenerarSolicitud = true;
			}
			
			/*
			  * Solución de la tarea 45708
			  * Se debe de postular El tipo de riesgo se encuentra soportado en H.C? en SI
			  * Según cómo está hecho este código, al iterar eb este for, hay unos registros que contienen las validaciones
			  * por tanto hacemos este condicionar para filtrar de manera individual, la llave que necesitamos
			  * en el caso que "El tipo de riesgo se encuentra soportado en H.C?" venga true o false, no se debe de hacer la validación
			  * de hecho nunca se hará por que el registro estará postulado en si y se podrá modificar a no
			  * La validacionMedicamentos se activa en true para cuando haya que validar:
			  * Se han utilizado medicamentos POS en el tratamiento del paciente?
			  */
			if(yaSeMostroMensajeMedicamentos == false)
			{
				if(validacionMedicamentos == true)
				{
					//logger.info("===> Se va a mostrar la validación de Medicamentos !!!");
					errors.add("", new ActionMessage("errors.required", "Se han utilizado medicamentos POS en el tratamiento del paciente? "));
					yaSeMostroMensajeMedicamentos = true;
					this.estado="empezar";
				}
			}

			if(yaSeMostroMensajeInfoPaciente == false)
			{
				if(validacionInformaPaciente == true)
				{
					//logger.info("===> Se va a mostrar la validación de Informa Paciente !!!");
					errors.add("", new ActionMessage("errors.required", "Se informa al paciente los efectos secundarios y posibles riesgos al tratamiento, manifiesta haberlos entendido y autoriza el uso del medicamento "));
					yaSeMostroMensajeInfoPaciente = true;
					this.estado="empezar";
				}
			}
			
			if(yaSeMostroMensajeNoGenerarSolicitud == false)
			{
				if(validacionNoGenerarSolicitud == true)
				{
					//logger.info("===> Hola Felipe :P:P:P");
					//logger.info("===> Se va a mostrar la validación No generar Solicitud !!!");
					errors.add("", new ActionMessage("error.errorEnBlanco", "Mientras el campo 'Se informa al paciente los efectos secundarios y posibles riesgos al tratamiento, manifiesta haberlos entendido y autoriza el uso del medicamento' se encuentre en No, No se puede generar la solicitud"));
					yaSeMostroMensajeNoGenerarSolicitud = true;
					this.estado="empezar";
				}
			}
			
			
			if(UtilidadTexto.isEmpty(this.diagnosticosDefinitivos.get("principal")+"")&&UtilidadTexto.isEmpty(this.diagnosticosDefinitivos.get("valorFichaDxPrincipal")+"")){
				errors.add("", new ActionMessage("errors.required", "El Diagnóstico Principal", "0"));
				this.estado="empezar";
			}
			
			if(existeSustituto){
				if(this.sustitutosNoPos.get("numdossisequiv_0")!=null&&!this.sustitutosNoPos.get("numdossisequiv_0").toString().trim().equals("")){
					try{
						Integer.parseInt(this.sustitutosNoPos.get("numdossisequiv_0").toString());
					}catch (Exception e) {
						errors.add("", new ActionMessage("errors.integer", "El campo 'Número de dosis equivalentes al medicamento No POS'", "0"));
						this.estado="empezar";
					}
				}
			}
		}
		/*
		if(emisor.equals("modificar"))
		{
			if(this.seHanUtilizadoMedicamentosPosEnElTratamientoDelPaciente.equals(ConstantesBD.acronimoSi))
			{
				
			}
		}
		*/
		if(errors.isEmpty())
		{
			logger.info("===> No hay errores !!!");
			this.setHayErrores(false);
		}
		else
		{
			logger.info("===> Va a salir error, se tiene que deterner para que no vaya al action  EMISOR:"+this.emisor);
			this.setHayErrores(true);
		}
		return errors;
	}

	/**
	 * Método que resetea la forma
	 */
	public void reset(){
		estado="";
		codigoSolicitud="";
		medicamentoNoPos="";
		subCuenta="";
		riesgoSoportado="";
		medicamentoPos="";
		capa="false";
		this.imprimir=0;
		this.frecuencia = "";
		this.tiempoTratamiento = -1;
		this.diasTratamiento = "";
		respuestaClinicaParaclinica="";
		observacionesResumen="";
		codigoOrden = ConstantesBD.codigoNuncaValido;
	}
	
/**
 * Método que reseta los mapas y datos del formualrio para el ingreso de un nuevo registro
 *
 */
	public void resetformulario(){
		observacionesResumen="";
		archivo="";		
		hijosMap=new HashMap();
		medicamentosPos=new HashMap();
		formularioMap=new HashMap();
		sustitutosNoPos=new HashMap();
		//medicamentosPos=new HashMap();
		diagnosticosDefinitivos=new HashMap();
		diagnosticoComplicacion_1="";
		diagnosticosPresuntivos=new HashMap();
		respuestaClinicaParaclinica="";
		this.imprimir=0;
		this.infopac=ConstantesBD.acronimoSi;
	}
	
	public void resetSubCuentas(){
		this.subCuentasMap=new HashMap();
		subCuentasMap.put("numRegistros", 0);
		this.subCuenta="";
	}

	public void resetAsocioJustificacion(){
  	    this.AsocioMapas = new HashMap();
	}
	
	
	/**
	 * @return the articulosMap
	 */
	public HashMap getArticulosMap() {
		return articulosMap;
	}

	/**
	 * @param articulosMap the articulosMap to set
	 */
	public void setArticulosMap(HashMap articulosMap) {
		this.articulosMap = articulosMap;
	}
	
	/**
	 * @return the medicamentosNoPos
	 */
	public HashMap getMedicamentosNoPos() {
		return medicamentosNoPos;
	}

	public Object getMedicamentosNoPos(String key) {
		return medicamentosNoPos.get(key);
	}

	public void setMedicamentosNoPos(String key, Object value) {
		this.medicamentosNoPos.put(key, value);
	}

	/**
	 * @param medicamentosNoPos the medicamentosNoPos to set
	 */
	public void setMedicamentosNoPos(HashMap medicamentosNoPos) {
		this.medicamentosNoPos = medicamentosNoPos;
	}

	/**
	 * @return the medicamentoNoPos
	 */
	public String getMedicamentoNoPos() {
		return medicamentoNoPos;
	}

	/**
	 * @param medicamentoNoPos the medicamentoNoPos to set
	 */
	public void setMedicamentoNoPos(String medicamentoNoPos) {
		this.medicamentoNoPos = medicamentoNoPos;
	}
	
	/**
	 * @return the medicamentosPos
	 */
	public HashMap getMedicamentosPos() {
		return medicamentosPos;
	}

	/**
	 * @param medicamentosPos the medicamentosPos to set
	 */
	public void setMedicamentosPos(HashMap medicamentosPos) {
		this.medicamentosPos = medicamentosPos;
	}

	/**
	 * @return the archivo
	 */
	public String getArchivo() {
		return archivo;
	}

	/**
	 * @param archivo the archivo to set
	 */
	public void setArchivo(String archivo) {
		this.archivo = archivo;
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
	 * @return the formularioMap
	 */
	public HashMap getFormularioMap() {
		return formularioMap;
	}
	
	/**
	 * @return the formularioMap
	 */
	public Object getFormularioMap(String llave) {
		return formularioMap.get(llave);
	}

	/**
	 * @param formularioMap the formularioMap to set
	 */
	public void setFormularioMap(HashMap formularioMap) {
		this.formularioMap = formularioMap;
	}

	/**
	 * @return the riesgoSoportado
	 */
	public String getRiesgoSoportado() {
		return riesgoSoportado;
	}

	/**
	 * @param riesgoSoportado the riesgoSoportado to set
	 */
	public void setRiesgoSoportado(String riesgoSoportado) {
		this.riesgoSoportado = riesgoSoportado;
	}

	/**
	 * @return the hijosMap
	 */
	public HashMap getHijosMap() {
		return hijosMap;
	}

	/**
	 * @param hijosMap the hijosMap to set
	 */
	public void setHijosMap(HashMap hijosMap) {
		this.hijosMap = hijosMap;
	}

	/**
	 * @return the camposSecciones
	 */
	public HashMap getCamposSecciones() {
		return camposSecciones;
	}


	/**
	 * @param camposSecciones the camposSecciones to set
	 */
	public void setCamposSecciones(HashMap camposSecciones) {
		this.camposSecciones = camposSecciones;
	}

	/**
	 * @return the mapaSecciones
	 */
	public HashMap getMapaSecciones() {
		return mapaSecciones;
	}

	/**
	 * @param mapaSecciones the mapaSecciones to set
	 */
	public void setMapaSecciones(HashMap mapaSecciones) {
		this.mapaSecciones = mapaSecciones;
	}

	/**
	 * @return the respuestaClinicaParaclinica
	 */
	public String getRespuestaClinicaParaclinica() {
		return respuestaClinicaParaclinica;
	}

	/**
	 * @param respuestaClinicaParaclinica the respuestaClinicaParaclinica to set
	 */
	public void setRespuestaClinicaParaclinica(String respuestaClinicaParaclinica) {
		this.respuestaClinicaParaclinica = respuestaClinicaParaclinica;
	}


	/**
	 * @return the observacionesResumen
	 */
	public String getObservacionesResumen() {
		return observacionesResumen;
	}

	/**
	 * @param observacionesResumen the observacionesResumen to set
	 */
	public void setObservacionesResumen(String observacionesResumen) {
		this.observacionesResumen = observacionesResumen;
	}

	/**
	 * @return the medicamentoPos
	 */
	public String getMedicamentoPos() {
		return medicamentoPos;
	}

	/**
	 * @param medicamentoPos the medicamentoPos to set
	 */
	public void setMedicamentoPos(String medicamentoPos) {
		this.medicamentoPos = medicamentoPos;
	}

	/**
	 * @return the codigoSolicitud
	 */
	public String getCodigoSolicitud() {
		return codigoSolicitud;
	}

	/**
	 * @param codigoSolicitud the codigoSolicitud to set
	 */
	public void setCodigoSolicitud(String codigoSolicitud) {
		this.codigoSolicitud = codigoSolicitud;
	}

	/**
	 * @return the diagnosticoComplicacion_1
	 */
	public String getDiagnosticoComplicacion_1() {
		return diagnosticoComplicacion_1;
	}

	/**
	 * @param diagnosticoComplicacion_1 the diagnosticoComplicacion_1 to set
	 */
	public void setDiagnosticoComplicacion_1(String diagnosticoComplicacion_1) {
		this.diagnosticoComplicacion_1 = diagnosticoComplicacion_1;
	}

	/**
	 * @return the diagnosticoIngreso_1
	 */
	public String getDiagnosticoIngreso_1() {
		return diagnosticoIngreso_1;
	}

	/**
	 * @param diagnosticoIngreso_1 the diagnosticoIngreso_1 to set
	 */
	public void setDiagnosticoIngreso_1(String diagnosticoIngreso_1) {
		this.diagnosticoIngreso_1 = diagnosticoIngreso_1;
	}

	/**
	 * @return the diagnosticoMuerte_1
	 */
	public String getDiagnosticoMuerte_1() {
		return diagnosticoMuerte_1;
	}

	/**
	 * @param diagnosticoMuerte_1 the diagnosticoMuerte_1 to set
	 */
	public void setDiagnosticoMuerte_1(String diagnosticoMuerte_1) {
		this.diagnosticoMuerte_1 = diagnosticoMuerte_1;
	}

	/**
	 * @return the diagnosticosDefinitivos
	 */
	public Map getDiagnosticosDefinitivos() {
		return diagnosticosDefinitivos;
	}

	/**
	 * @param diagnosticosDefinitivos the diagnosticosDefinitivos to set
	 */
	public void setDiagnosticosDefinitivos(Map diagnosticosDefinitivos) {
		this.diagnosticosDefinitivos = diagnosticosDefinitivos;
	}

	/**
	 * @return the diagnosticosPresuntivos
	 */
	public Map getDiagnosticosPresuntivos() {
		return diagnosticosPresuntivos;
	}

	/**
	 * @param diagnosticosPresuntivos the diagnosticosPresuntivos to set
	 */
	public void setDiagnosticosPresuntivos(Map diagnosticosPresuntivos) {
		this.diagnosticosPresuntivos = diagnosticosPresuntivos;
	}

	/**
	 * @return the diagnosticosPresuntivosBoolean
	 */
	public boolean isDiagnosticosPresuntivosBoolean() {
		return diagnosticosPresuntivosBoolean;
	}

	/**
	 * @param diagnosticosPresuntivosBoolean the diagnosticosPresuntivosBoolean to set
	 */
	public void setDiagnosticosPresuntivosBoolean(
			boolean diagnosticosPresuntivosBoolean) {
		this.diagnosticosPresuntivosBoolean = diagnosticosPresuntivosBoolean;
	}

	/**
	 * @return the numDiagnosticosDefinitivos
	 */
	public int getNumDiagnosticosDefinitivos() {
		return numDiagnosticosDefinitivos;
	}

	/**
	 * @param numDiagnosticosDefinitivos the numDiagnosticosDefinitivos to set
	 */
	public void setNumDiagnosticosDefinitivos(int numDiagnosticosDefinitivos) {
		this.numDiagnosticosDefinitivos = numDiagnosticosDefinitivos;
	}

	/**
	 * @return the numDiagnosticosPresuntivos
	 */
	public int getNumDiagnosticosPresuntivos() {
		return numDiagnosticosPresuntivos;
	}

	/**
	 * @param numDiagnosticosPresuntivos the numDiagnosticosPresuntivos to set
	 */
	public void setNumDiagnosticosPresuntivos(int numDiagnosticosPresuntivos) {
		this.numDiagnosticosPresuntivos = numDiagnosticosPresuntivos;
	}

	/**
	 * @return the numDiagnosticosPresuntivosOriginal
	 */
	public int getNumDiagnosticosPresuntivosOriginal() {
		return numDiagnosticosPresuntivosOriginal;
	}

	/**
	 * @param numDiagnosticosPresuntivosOriginal the numDiagnosticosPresuntivosOriginal to set
	 */
	public void setNumDiagnosticosPresuntivosOriginal(
			int numDiagnosticosPresuntivosOriginal) {
		this.numDiagnosticosPresuntivosOriginal = numDiagnosticosPresuntivosOriginal;
	}

	/**
	 * @return the tipoDiagnosticoPrincipal
	 */
	public int getTipoDiagnosticoPrincipal() {
		return tipoDiagnosticoPrincipal;
	}

	/**
	 * @param tipoDiagnosticoPrincipal the tipoDiagnosticoPrincipal to set
	 */
	public void setTipoDiagnosticoPrincipal(int tipoDiagnosticoPrincipal) {
		this.tipoDiagnosticoPrincipal = tipoDiagnosticoPrincipal;
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
	
	/**
	 * Asigna un diagnostico presuntivo (ppal o relacionado)
	 */
	public void setDiagnosticoPresuntivo(String key, Object value) 
	{
		diagnosticosPresuntivos.put(key, value);
	}
	
	/**
	 * Retorna el diagnostico presuntivo (ppal o relacionado) asociado a la
	 * llave dada
	 */
	public Object getDiagnosticoPresuntivo(String key) 
	{
		return diagnosticosPresuntivos.get(key);
	}

	/**
	 * @return the descripcionComplicacion
	 */
	public String getDescripcionComplicacion() {
		return descripcionComplicacion;
	}

	/**
	 * @param descripcionComplicacion the descripcionComplicacion to set
	 */
	public void setDescripcionComplicacion(String descripcionComplicacion) {
		this.descripcionComplicacion = descripcionComplicacion;
	}

	/**
	 * @return the emisor
	 */
	public String getEmisor() {
		return emisor;
	}

	/**
	 * @param emisor the emisor to set
	 */
	public void setEmisor(String emisor) {
		this.emisor = emisor;
	}

	/**
	 * @return the dosis
	 */
	public String getDosis() {
		return dosis;
	}

	/**
	 * @param dosis the dosis to set
	 */
	public void setDosis(String dosis) {
		this.dosis = dosis;
	}

	/**
	 * @return the frecuencia
	 */
	public String getFrecuencia() {
		return frecuencia;
	}

	/**
	 * @param frecuencia the frecuencia to set
	 */
	public void setFrecuencia(String frecuencia) {
		this.frecuencia = frecuencia;
	}

	/**
	 * @return the tipoFrecuencia
	 */
	public String getTipoFrecuencia() {
		return tipoFrecuencia;
	}

	/**
	 * @param tipoFrecuencia the tipoFrecuencia to set
	 */
	public void setTipoFrecuencia(String tipoFrecuencia) {
		this.tipoFrecuencia = tipoFrecuencia;
	}

	/**
	 * @return the totalUnidades
	 */
	public int getTotalUnidades() {
		return totalUnidades;
	}

	/**
	 * @param totalUnidades the totalUnidades to set
	 */
	public void setTotalUnidades(int totalUnidades) {
		this.totalUnidades = totalUnidades;
	}

	/**
	 * @return the unidosis
	 */
	public int getUnidosis() {
		return unidosis;
	}

	/**
	 * @param unidosis the unidosis to set
	 */
	public void setUnidosis(int unidosis) {
		this.unidosis = unidosis;
	}

	/**
	 * @return the tiempoTratamiento
	 */
	public int getTiempoTratamiento() {
		return tiempoTratamiento;
	}

	/**
	 * @param tiempoTratamiento the tiempoTratamiento to set
	 */
	public void setTiempoTratamiento(int tiempoTratamiento) {
		this.tiempoTratamiento = tiempoTratamiento;
	}

	/**
	 * @return the sustitutosNoPos
	 */
	public HashMap getSustitutosNoPos() {
		return sustitutosNoPos;
	}

	/**
	 * @param sustitutosNoPos the sustitutosNoPos to set
	 */
	public void setSustitutosNoPos(HashMap sustitutosNoPos) {
		this.sustitutosNoPos = sustitutosNoPos;
	}

	/**
	 * @return the sustitutosNoPos
	 */
	public Object getSustitutosNoPos(String llave) {
		return sustitutosNoPos.get(llave);
	}

	/**
	 * @param sustitutosNoPos the sustitutosNoPos to set
	 */
	public void setSustitutosNoPos(String llave, Object obj) {
		this.sustitutosNoPos.put(llave, obj);
	}

	/**
	 * @return the indexSelectSustitutos
	 */
	public int getIndexSelectSustitutos() {
		return indexSelectSustitutos;
	}

	/**
	 * @param indexSelectSustitutos the indexSelectSustitutos to set
	 */
	public void setIndexSelectSustitutos(int indexSelectSustitutos) {
		this.indexSelectSustitutos = indexSelectSustitutos;
	}

	/**
	 * @return the capa
	 */
	public String getCapa() {
		return capa;
	}

	/**
	 * @param capa the capa to set
	 */
	public void setCapa(String capa) {
		this.capa = capa;
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setFormularioMap (String key,Object value)
	{
		this.formularioMap.put(key, value);
	}
	
	/**
	 * @return the numjus
	 */
	public int getNumjus() {
		return numjus;
	}
	
	public void setMedicamentosPos(String key, Object value) {
		this.medicamentosPos.put(key, value);
	}

	/**
	 * @param numjus the numjus to set
	 */
	public void setNumjus(int numjus) {
		this.numjus = numjus;
	}
	
	public Object getInformacionArtPrin(String key) {
		return informacionArtPrin.get(key);
	}
	
	public void setInformacionArtPrin(String key, Object value) {
		this.informacionArtPrin.put(key, value);
	}
	
	/**
	 * @return the infopac
	 */
	public String getInfopac() {
		return infopac;
	}
	
	/**
	 * @param infopac the infopac to set
	 */
	public void setInfopac(String infopac) {
		this.infopac = infopac;
	}
	/**
	 * @return the informacionArtPrin
	 */
	public HashMap getInformacionArtPrin() {
		return informacionArtPrin;
	}
	
	/**
	 * @param informacionArtPrin the informacionArtPrin to set
	 */
	public void setInformacionArtPrin(HashMap informacionArtPrin) {
		this.informacionArtPrin = informacionArtPrin;
	}
	/**
	 * @return the formaFconcMap
	 */
	public HashMap getFormaFconcMap() {
		return formaFconcMap;
	}
	
	/**
	 * @param formaFconcMap the formaFconcMap to set
	 */
	public void setFormaFconcMap(HashMap formaFconcMap) {
		this.formaFconcMap = formaFconcMap;
	}

	/**
	 * @return the esInsumo
	 */
	public boolean isEsInsumo() {
		return esInsumo;
	}
	
	/**
	 * @param esInsumo the esInsumo to set
	 */
	public void setEsInsumo(boolean esInsumo) {
		this.esInsumo = esInsumo;
	}
	
	/**
	 * @return the nojustificacion
	 */
	public String getNojustificacion() {
		return nojustificacion;
	}
	
	
	
	/**
	 * @param nojustificacion the nojustificacion to set
	 */
	public void setNojustificacion(String nojustificacion) {
		this.nojustificacion = nojustificacion;
	}
	
	/**
	 * @return the subCuenta
	 */
	public String getSubCuenta() {
		return subCuenta;
	}
	
	/**
	 * @param subCuenta the subCuenta to set
	 */
	public void setSubCuenta(String subCuenta) {
		this.subCuenta = subCuenta;
	}
	
	public HashMap getSubCuentasMap() {
		return subCuentasMap;
	}
	
	public void setSubCuentasMap(HashMap subCuentasMap) {
		this.subCuentasMap = subCuentasMap;
	}
	
	public Object getSubCuentasMap(String key) {
		return subCuentasMap.get(key);
	}
	
	
	public void setSubCuentasMap(String key, Object value) {
		this.subCuentasMap.put(key, value);
	}
	
	/**
	 * @return the ingresos
	 */
	public HashMap getIngresos() {
		return ingresos;
	}

	/**
	 * @param ingresos the ingresos to set
	 */
	public void setIngresos(HashMap ingresos) {
		this.ingresos = ingresos;
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
	 * @return the ingreso
	 */
	public String getIngreso() {
		return ingreso;
	}

	/**
	 * @param ingreso the ingreso to set
	 */
	public void setIngreso(String ingreso) {
		this.ingreso = ingreso;
	}
	
	/**
	 * @param ingreso the ingreso to set
	 */
	public void setIngresos(String key, Object o) {
		this.ingresos.put(key, o);
	}

	/**
	 * @return the ingresosArticulos
	 */
	public HashMap getIngresosArticulos() {
		return ingresosArticulos;
	}

	/**
	 * @param ingresosArticulos the ingresosArticulos to set
	 */
	public void setIngresosArticulos(HashMap ingresosArticulos) {
		this.ingresosArticulos = ingresosArticulos;
	}
		
	/**
	 * @param ingresosArticulos the ingresosArticulos to set
	 */
	public void setIngresosArticulos(String key, Object o) {
		this.ingresosArticulos.put(key, o);
	}
	
	/**
	 * @return the cuenta
	 */
	public String getCuenta() {
		return cuenta;
	}

	/**
	 * @param cuenta the cuenta to set
	 */
	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}

	/**
	 * @return the centroCosto
	 */
	public String getCentroCosto() {
		return centroCosto;
	}

	/**
	 * @param centroCosto the centroCosto to set
	 */
	public void setCentroCosto(String centroCosto) {
		this.centroCosto = centroCosto;
	}

	/**
	 * @return the convenio
	 */
	public String getConvenio() {
		return convenio;
	}

	/**
	 * @param convenio the convenio to set
	 */
	public void setConvenio(String convenio) {
		this.convenio = convenio;
	}
	
	/**
	 * @return the fechaFinal
	 */
	public String getFechaFinal() {
		return fechaFinal;
	}

	/**
	 * @param fechaFinal the fechaFinal to set
	 */
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	/**
	 * @return the fechaInicial
	 */
	public String getFechaInicial() {
		return fechaInicial;
	}

	/**
	 * @param fechaInicial the fechaInicial to set
	 */
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	/**
	 * @return the centroAtencion
	 */
	public ArrayList<HashMap<String, Object>> getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(ArrayList<HashMap<String, Object>> centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	/**
	 * @return the centrosCosto
	 */
	public ArrayList<HashMap<String, Object>> getCentrosCosto() {
		return centrosCosto;
	}

	/**
	 * @param centrosCosto the centrosCosto to set
	 */
	public void setCentrosCosto(ArrayList<HashMap<String, Object>> centrosCosto) {
		this.centrosCosto = centrosCosto;
	}

	/**
	 * @return the convenios
	 */
	public ArrayList<HashMap<String, Object>> getConvenios() {
		return convenios;
	}

	/**
	 * @param convenios the convenios to set
	 */
	public void setConvenios(ArrayList<HashMap<String, Object>> convenios) {
		this.convenios = convenios;
	}

	/**
	 * @return the viaIngresoPaciente
	 */
	public ArrayList<HashMap<String, Object>> getViaIngresoPaciente() {
		return viaIngresoPaciente;
	}

	/**
	 * @param viaIngresoPaciente the viaIngresoPaciente to set
	 */
	public void setViaIngresoPaciente(
			ArrayList<HashMap<String, Object>> viaIngresoPaciente) {
		this.viaIngresoPaciente = viaIngresoPaciente;
	}

	/**
	 * @return the filtros
	 */
	public HashMap getFiltros() {
		return filtros;
	}

	/**
	 * @param filtros the filtros to set
	 */
	public void setFiltros(HashMap filtros) {
		this.filtros = filtros;
	}
	
	/**
	 * @param filtros the filtros to set
	 */
	public void setFiltros(String key, Object o) {
		this.filtros.put(key, o);
	}
	
	/**
	 * @return the tipoCodigo
	 */
	public String getTipoCodigo() {
		return tipoCodigo;
	}

	/**
	 * @param tipoCodigo the tipoCodigo to set
	 */
	public void setTipoCodigo(String tipoCodigo) {
		this.tipoCodigo = tipoCodigo;
	}

	/**
	 * @return the ultimoPatron
	 */
	public String getUltimoPatron() {
		return ultimoPatron;
	}

	/**
	 * @param ultimoPatron the ultimoPatron to set
	 */
	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}

	/**
	 * @return the codigoArticulo
	 */
	public String getCodigoArticulo() {
		return codigoArticulo;
	}

	/**
	 * @param codigoArticulo the codigoArticulo to set
	 */
	public void setCodigoArticulo(String codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}

	/**
	 * @return the descripcionArticulo
	 */
	public String getDescripcionArticulo() {
		return descripcionArticulo;
	}

	/**
	 * @param descripcionArticulo the descripcionArticulo to set
	 */
	public void setDescripcionArticulo(String descripcionArticulo) {
		this.descripcionArticulo = descripcionArticulo;
	}

	/**
	 * @return the indexMap
	 */
	public int getIndexMap() {
		return indexMap;
	}

	/**
	 * @param indexMap the indexMap to set
	 */
	public void setIndexMap(int indexMap) {
		this.indexMap = indexMap;
	}

	/**
	 * @return the codigoCentroAtencion
	 */
	public String getCodigoCentroAtencion() {
		return codigoCentroAtencion;
	}

	/**
	 * @param codigoCentroAtencion the codigoCentroAtencion to set
	 */
	public void setCodigoCentroAtencion(String codigoCentroAtencion) {
		this.codigoCentroAtencion = codigoCentroAtencion;
	}
	
	/**
	 * @return the codigoViaIngreso
	 */
	public String getCodigoViaIngreso() {
		return codigoViaIngreso;
	}

	/**
	 * @param codigoViaIngreso the codigoViaIngreso to set
	 */
	public void setCodigoViaIngreso(String codigoViaIngreso) {
		this.codigoViaIngreso = codigoViaIngreso;
	}

	/**
	 * @return the subcuentascantidad
	 */
	public String getSubcuentascantidad() {
		return subcuentascantidad;
	}

	/**
	 * @param subcuentascantidad the subcuentascantidad to set
	 */
	public void setSubcuentascantidad(String subcuentascantidad) {
		this.subcuentascantidad = subcuentascantidad;
	}

	/**
	 * @return the menurol
	 */
	public String getMenurol() {
		return menurol;
	}

	/**
	 * @param menurol the menurol to set
	 */
	public void setMenurol(String menurol) {
		this.menurol = menurol;
	}

	/**
	 * @return the pisos
	 */
	public ArrayList<HashMap<String, Object>> getPisos() {
		return pisos;
	}

	/**
	 * @param pisos the pisos to set
	 */
	public void setPisos(ArrayList<HashMap<String, Object>> pisos) {
		this.pisos = pisos;
	}

	/**
	 * @return the profesionales
	 */
	public ArrayList<HashMap<String, Object>> getProfesionales() {
		return profesionales;
	}

	/**
	 * @param profesionales the profesionales to set
	 */
	public void setProfesionales(ArrayList<HashMap<String, Object>> profesionales) {
		this.profesionales = profesionales;
	}

	/**
	 * @return the cargarsi
	 */
	public int getCargarsi() {
		return cargarsi;
	}

	/**
	 * @param cargarsi the cargarsi to set
	 */
	public void setCargarsi(int cargarsi) {
		this.cargarsi = cargarsi;
	}

	public int getImprimir() {
		return imprimir;
	}

	public void setImprimir(int imprimir) {
		this.imprimir = imprimir;
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
	 * @return the permitirModificarTiempoTratamientoJustificacionNopos
	 */
	public String getPermitirModificarTiempoTratamientoJustificacionNopos() {
		return permitirModificarTiempoTratamientoJustificacionNopos;
	}

	/**
	 * @param permitirModificarTiempoTratamientoJustificacionNopos the permitirModificarTiempoTratamientoJustificacionNopos to set
	 */
	public void setPermitirModificarTiempoTratamientoJustificacionNopos(
			String permitirModificarTiempoTratamientoJustificacionNopos) {
		this.permitirModificarTiempoTratamientoJustificacionNopos = permitirModificarTiempoTratamientoJustificacionNopos;
	}

	/**
	 * @return the nombreMapaJus
	 */
	public String getNombreMapaJus() {
		if(UtilidadTexto.isEmpty(this.nombreMapaJus))
			return "justificacionMap";
		else
			return nombreMapaJus;
	}
	
	/**
	 * @param nombreMapaJus the nombreMapaJus to set
	 */
	public void setNombreMapaJus(String nombreMapaJus) {
		this.nombreMapaJus = nombreMapaJus;
	}

	/**
	 * @return the consecutivoJustActual
	 */
	public int getConsecutivoJustActual() {
		return consecutivoJustActual;
	}

	/**
	 * @param consecutivoJustActual the consecutivoJustActual to set
	 */
	public void setConsecutivoJustActual(int consecutivoJustActual) {
		this.consecutivoJustActual = consecutivoJustActual;
	}

	/**
	 * @return the filtrosRecargar
	 */
	public HashMap getFiltrosRecargar() {
		return filtrosRecargar;
	}

	/**
	 * @param filtrosRecargar the filtrosRecargar to set
	 */
	public void setFiltrosRecargar(HashMap filtrosRecargar) {
		this.filtrosRecargar = filtrosRecargar;
	}

	public void setNumSolicitudSalvado(String numSolicitudSalvado) {
			this.numSolicitudSalvado = numSolicitudSalvado;
		}
	
	public String getNumSolicitudSalvado() {
		return numSolicitudSalvado;
	}


	public void setCodigoArticuloSalvado(String codigoArticuloSalvado) {
		this.codigoArticuloSalvado = codigoArticuloSalvado;
	}

	public String getCodigoArticuloSalvado() {
		return codigoArticuloSalvado;
	}



	public void setSeHanUtilizadoMedicamentosPosEnElTratamientoDelPaciente(
			String seHanUtilizadoMedicamentosPosEnElTratamientoDelPaciente) {
		this.seHanUtilizadoMedicamentosPosEnElTratamientoDelPaciente = seHanUtilizadoMedicamentosPosEnElTratamientoDelPaciente;
	}



	public String getSeHanUtilizadoMedicamentosPosEnElTratamientoDelPaciente() {
		return seHanUtilizadoMedicamentosPosEnElTratamientoDelPaciente;
	}

	public void setHayErrores(boolean hayErrores) {
		this.hayErrores = hayErrores;
	}

	public boolean isHayErrores() {
		return hayErrores;
	}

	/**
	 * @return the justificacionHistorica
	 */
	public HashMap<String, Object> getJustificacionHistorica() {
		return justificacionHistorica;
	}

	/**
	 * @param justificacionHistorica the justificacionHistorica to set
	 */
	public void setJustificacionHistorica(
			HashMap<String, Object> justificacionHistorica) {
		this.justificacionHistorica = justificacionHistorica;
	}
	/**
	 * posicion campo
	 * 
	 * @return the posicionCampo
	 */
	public String getIdHtmlCampo() {
		return idHtmlCampo;
	}

	/**
	 * posicion campo
	 * 
	 * @param posicionCampo the posicionCampo to set
	 */
	public void setIdHtmlCampo(String idHtmlCampo) {
		this.idHtmlCampo = idHtmlCampo;
	}

	/**
	 * identificador campo
	 * 
	 * @return the identificadorCampo
	 */
	public String getIdentificadorCampo() {
		return identificadorCampo;
	}

	/**
	 * identificador campo
	 * 
	 * @param identificadorCampo the identificadorCampo to set
	 */
	public void setIdentificadorCampo(String identificadorCampo) {
		this.identificadorCampo = identificadorCampo;
	}

	/**
	 * numero de la seccion del campo
	 * 
	 * @return the numeroSeccion
	 */
	public String getNumeroSeccion() {
		return numeroSeccion;
	}

	/**
	 * numero de la seccion del campo
	 * 
	 * @param numeroSeccion the numeroSeccion to set
	 */
	public void setNumeroSeccion(String numeroSeccion) {
		this.numeroSeccion = numeroSeccion;
	}

	/**
	 * posicion registro
	 * 
	 * @return the posicionRegistro
	 */
	public String getPosicionRegistro() {
		return posicionRegistro;
	}

	/**
	 * posicion registro
	 * 
	 * @param posicionRegistro the posicionRegistro to set
	 */
	public void setPosicionRegistro(String posicionRegistro) {
		this.posicionRegistro = posicionRegistro;
	}

	public boolean isSeInformaAlPacienteRiesgosTratamiento() {
		return seInformaAlPacienteRiesgosTratamiento;
	}

	public void setSeInformaAlPacienteRiesgosTratamiento(
			boolean seInformaAlPacienteRiesgosTratamiento) {
		this.seInformaAlPacienteRiesgosTratamiento = seInformaAlPacienteRiesgosTratamiento;
	}

	public boolean isExisteSustituto() {
		return existeSustituto;
	}

	public void setExisteSustituto(boolean existeSutituto) {
		this.existeSustituto = existeSutituto;
	}

	public boolean isProvieneOrdenAmbulatoria() {
		return provieneOrdenAmbulatoria;
	}

	public void setProvieneOrdenAmbulatoria(boolean provieneOrdenAmbulatoria) {
		this.provieneOrdenAmbulatoria = provieneOrdenAmbulatoria;
	}

	/**
	 * @return the esInsumoRango
	 */
	public boolean isEsInsumoRango() {
		return esInsumoRango;
	}

	/**
	 * @param esInsumoRango the esInsumoRango to set
	 */
	public void setEsInsumoRango(boolean esInsumoRango) {
		this.esInsumoRango = esInsumoRango;
	}
	
	/**
	 * @return the codigoOrden
	 */
	public int getCodigoOrden() {
		return codigoOrden;
	}
	
	/**
	 * @param codigoOrden the codigoOrden to set
	 */
	public void setCodigoOrden(int codigoOrden) {
		this.codigoOrden = codigoOrden;
	}

	public HashMap getAsocioMapas() {
		return AsocioMapas;
	}

	public void setAsocioMapas(HashMap asocioMapas) {
		AsocioMapas = asocioMapas;
	}

	/**
	 * @return the seVaAAsociarJustificacion
	 */
	public boolean isSeVaAAsociarJustificacion() {
		return seVaAAsociarJustificacion;
	}

	/**
	 * @param seVaAAsociarJustificacion the seVaAAsociarJustificacion to set
	 */
	public void setSeVaAAsociarJustificacion(boolean seVaAAsociarJustificacion) {
		this.seVaAAsociarJustificacion = seVaAAsociarJustificacion;
	}
	
	
	
}
