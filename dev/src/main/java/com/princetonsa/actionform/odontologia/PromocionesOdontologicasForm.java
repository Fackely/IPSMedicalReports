package com.princetonsa.actionform.odontologia;

import java.util.ArrayList;
import java.util.HashMap;

import javax.rmi.CORBA.UtilDelegate;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;
import org.axioma.util.fechas.UtilidadesFecha;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.InfoDatosString;
import util.ResultadoBoolean;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dto.facturacion.DtoEmpresasInstitucion;
import com.princetonsa.dto.manejoPaciente.DtoCategoriaAtencion;
import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.princetonsa.dto.manejoPaciente.DtoRegionesCobertura;
import com.princetonsa.dto.odontologia.DtoDetCaPromocionesOdo;
import com.princetonsa.dto.odontologia.DtoDetConvPromocionesOdo;
import com.princetonsa.dto.odontologia.DtoDetPromocionOdo;
import com.princetonsa.dto.odontologia.DtoPromocionesOdontologicas;
import com.princetonsa.mundo.odontologia.PromocionesOdontologicas;



/**
 * 
 * @author axioma
 * 
 */
public class PromocionesOdontologicasForm extends ValidatorForm {

	/**
	 * 
	 */
	private Logger logger = Logger.getLogger(PromocionesOdontologicasForm.class);

	// variables Temporables-> para centro de

	private int codigoCentroAtencion;
	/**
	 * 
	 */
	private int codigoConvenio;
	
	private Boolean esConsulta;
	/**
	 * 
	 */
	private String tmpNombrePrograma;
	/**
	 * 
	 */
	private String esDescendente;
	/**
	 *Tmp Centro Atencion
	 */
	private InfoDatosInt tmpCentroAtencion;
	/**
	 *Tmp Convenios Guarda el codigo y el nombre
	 */
	private InfoDatosInt tmpCovenio;
	/**
	 * 
	 */
	private ArrayList<InfoDatosInt> listTmpCentrosAtencio = new ArrayList<InfoDatosInt>();
	/**
	 * 
	 */
	private HashMap estadosCiviles = new HashMap();
	/**
	 * 
	 */
	private HashMap ocupaciones = new HashMap();
	/**
	 * 
	 */
	private int posArray;
	/**
	 * 
	 */
	private int posArrayCentro;
	/**
	 * 
	 */
	private int posArrayConvenio;
	/**
	 * guardar
	 */
	private String criterioBusqueda;
	/**
	 * 
	 */
	private String patronOrdenar;
	/**
	 * 
	 */
	private ArrayList<DtoPromocionesOdontologicas> listaPromociones = new ArrayList<DtoPromocionesOdontologicas>();
	/**
	 * 
	 */
	private ArrayList<DtoDetPromocionOdo> listaDetPromociones = new ArrayList<DtoDetPromocionOdo>();
	/**
	 * 
	 */
	private ArrayList<DtoDetConvPromocionesOdo> listaDetConvenios = new ArrayList<DtoDetConvPromocionesOdo>();
	/**
	 * 
	 */
	// <- LIsta de conveios base de datos-->

	private ArrayList<HashMap<String, Object>> listConvenios = new ArrayList<HashMap<String, Object>>();

	/**
	 * 
	 */

	private ArrayList<DtoDetCaPromocionesOdo> listaCaPromociones = new ArrayList<DtoDetCaPromocionesOdo>();

	/**
	 * 
	 * 
	 */
	private ArrayList<DtoCentrosAtencion> listaCentrosAtencion = new ArrayList<DtoCentrosAtencion>();

	/**
	 * LISTA DE CIUDADES
	 */
	private ArrayList<HashMap<String, Object>> ciudadesMap = new ArrayList<HashMap<String, Object>>();

	// -------------------Regiones
	/**
	 * 
	 */
	private DtoRegionesCobertura regionCobertura;
	/**
	 * 
	 */

	private ArrayList<DtoRegionesCobertura> arrayRegiones = new ArrayList<DtoRegionesCobertura>();

	/** 
	 *  
	 */

	// ----------------------------Categoria

	private ArrayList<DtoCategoriaAtencion> arrayCategorias = new ArrayList<DtoCategoriaAtencion>();

	/**
	 * 
	 */
	private DtoCategoriaAtencion categoriaAtencion;

	/**
	 * listaInstitucionEmpresa
	 */
	private ArrayList<DtoEmpresasInstitucion> listaInstitucionEmpresa = new ArrayList<DtoEmpresasInstitucion>();
	private DtoEmpresasInstitucion dtoEmpresa = new DtoEmpresasInstitucion();

	private String utilizaInstitucionMultiempresa;
	private String utilizaInst;

	// ValoresPorDefecto.getInstitucionMultiempresa(usuario.getCodigoInstitucionInt())

	// Atributos Fudamentales

	private String estado;
	/**
	 * 
	 */

	// Dto de la Funcionalidad
	private DtoPromocionesOdontologicas dtoPromocion;
	/**
	 * 
	 */
	private DtoDetPromocionOdo dtoDetPromocion;
	/**
	 * 
	 */
	private DtoDetCaPromocionesOdo dtoCaPromociones;

	/**
	 * 
	 */
	private DtoDetConvPromocionesOdo dtoConvPromociones;

	/**
	 * 
	 */

	private String codigoCentroAten;

	/**
	 * 
	 */
	private String tipodeRelacion;

	/**
	 * 
	 * 
	 * 
	 */
	private String codigos;

	/**
	 * OBTENER LISTA DE PAISES
	 */
	private ArrayList<HashMap<String, Object>> listaPaises = new ArrayList<HashMap<String, Object>>();
	/**
	 * CODIGO PAIS
	 */
	private String tmpCodigoPais;
	/**
	 * 
	 */
	//private ArrayList<HashMap> especialidadesOdontologia = new ArrayList<HashMap>();
	//private int codigoEspecialidad;

	/**
	 * TMP PARA MANEJAR PAIS
	 */

	private InfoDatosString pais;
	private InfoDatosInt centroAtencion;

	/**
	 * VARIABLES PARA MENSAJES
	 */
	private String mensajeCentro;
	private String mensajeConvenio;
	
	private String tipoReporte;
	
	private HashMap mapaArchivoPlano;
	
	
	
	

	private String rutaArchivoRelativa;
	private String rutaArchivoAbsoluta;
	private String nombreArchivo;
	
	
	
	/**
	 * tmp CODIGO DEPARTAMENTO
	 */
	private String tmpCodigoDepartamento; 
	private String tmpCodigoCiudad;
	
	/**
	 * AYUDA PARA SABER SI EXISTE INFORMACION EN LA LISTA
	 */
	private int tamanioLista;

	/**
	 * 
	 */
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errores = new ActionErrors();

		if (this.getDtoCaPromociones() != null) {
			logger.info("CODIGO CENTRO ATENCION EN EL VALIDATE: "
					+ this.getDtoCaPromociones().getCentroAtencion()
							.getCodigo());
			logger.info("CODIGO CENTRO ATENCION EN EL VALIDATE (String): "
					+ this.codigoCentroAten);
		}

		//

		if (this.getEstado().equals("guardar")) {
			this.validarRango(errores);
			this.validarFecha(errores);
			this.validarHora(errores);

		}
		if (this.getEstado().equals("guardarModificar")) {
			this.validarRango(errores);
			this.validarHora(errores);
			this.validarFechaModificacion(errores);

		}
		if (this.getEstado().equals("guardarDetalle")) {

		}

		if (!errores.isEmpty()) {
			if (this.getEstado().equals("guardar")) {
				this.estado = "mostrarErrores";
			}
			if (this.getEstado().equals("guardarModificar")) {
				this.estado = "mostrarErroresModificar";
			}

			if (this.getEstado().equals("guardarDetalle")) {
				this.estado = "mostrarErroresGuardar";
			}
		}
		return errores;
	}

	/**
	 * 
	 * @param errores
	 */
	private void validarFechaModificacion(ActionErrors errores) 
	{

		
		if (UtilidadTexto.isEmpty(this.getDtoPromocion().getFechaInicialVigencia())) 
		{
			errores.add("", new ActionMessage("errors.required","La fecha Inicial"));
		}
		
		if (UtilidadTexto.isEmpty(this.getDtoPromocion().getFechaFinalVigencia())) 
		{
			errores.add("", new ActionMessage("errors.required","La fecha Final"));
		}

		
		if ((!UtilidadTexto.isEmpty(this.getDtoPromocion().getFechaInicialVigencia()))	&& (!UtilidadTexto.isEmpty(this.getDtoPromocion().getFechaFinalVigencia()))) 
		{
			
			if (!UtilidadFecha.esFechaValidaSegunAp(this.getDtoPromocion().getFechaInicialVigencia())) {
				errores.add("", new ActionMessage("errors.formatoFechaInvalido", this.getDtoPromocion().getFechaInicialVigencia()));
			}
			
			if (!UtilidadFecha.esFechaValidaSegunAp(this.getDtoPromocion().getFechaFinalVigencia())) 
			{
				errores.add("", new ActionMessage("errors.formatoFechaInvalido", this.getDtoPromocion().getFechaFinalVigencia()));
			}
			
			
			
			if (!UtilidadTexto.isEmpty(this.getDtoPromocion().getFechaInicialVigencia()) && !UtilidadTexto.isEmpty(this.getDtoPromocion().getFechaFinalVigencia())) 
			{
				
				/*
				if (!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getDtoPromocion().getFechaInicialVigencia(), this.getDtoPromocion().getFechaFinalVigencia())) 
				{
					errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", " Incial "+ this.getDtoPromocion().getFechaInicialVigencia(),	
														" Final "+ this.getDtoPromocion().getFechaFinalVigencia()));
				}
				*/
				
				

				
				// << VALIDA SI ES MAYOR A LA FECHA DEL SISTEMA>>
				/*
				 * Cambiar este Metodo
				 
				if (UtilidadFecha.esFechaMenorQueOtraReferencia(this.getDtoPromocion().getFechaInicialVigencia(),UtilidadFecha.getFechaActual())) 
				{
					errores.add("", new ActionMessage("errors.notEspecific","La Fecha Inicial "	+ this.getDtoPromocion()
																			.getFechaInicialVigencia()+ " Deber Ser Mayor A La Fecha Actual "
																			+ UtilidadFecha.getFechaActual()));
				}
				*/

				
				/*
				 * Cambiar este metodo
				 */
				if (UtilidadFecha.esFechaMenorQueOtraReferencia(this.getDtoPromocion().getFechaFinalVigencia(),
						UtilidadFecha.getFechaActual())) 
				{
						errores.add("", new ActionMessage("errors.notEspecific"," La Fecha Final "+ this.getDtoPromocion().getFechaFinalVigencia()
														+ "Deber Ser Mayor A La Fecha Actual "
														+ UtilidadFecha.getFechaActual()));
				}

			}
		}//	 

	}

	/**
	 * 
	 * @param errores
	 */

	/**
	 * 
	 * @param errores
	 */
	private void validarRango(ActionErrors errores) {
		/**
		 * 
		 */

		if (UtilidadTexto.isEmpty(this.getDtoPromocion().getNombre())) 
		{
			errores.add("", new ActionMessage("errors.required", "El Nombre"));
		} 
		else 
		{
			if (PromocionesOdontologicas.existeCruceFechas(this.getDtoPromocion(), getDtoPromocion().getCodigoPk())) 
			{
				errores.add("",	new ActionMessage("errors.notEspecific",
												"Ya Existe Otra Promocion Con El Mismo Nombre Para El Rango De Fechas Establecido"));
			}
		}

	}

	/**
	 * 
	 * @param errores
	 */
	private void validarHora(ActionErrors errores) 
	{
		
		ResultadoBoolean ObjResultado;
		
		ObjResultado=UtilidadFecha.compararFechas(this.getDtoPromocion().getFechaInicialVigencia(), this.getDtoPromocion().getHoraInicialVigencia(), 
												this.getDtoPromocion().getFechaFinalVigencia(), this.getDtoPromocion().getHoraFinalVigencia());
		
		if(ObjResultado.isTrue())
		{
			errores.add("", new ActionMessage("errors.notEspecific"," La Fecha Inicial debe ser Menor que la Fecha Final "));
		}
		

		
		
		/*
		
		if ((!UtilidadTexto.isEmpty(this.getDtoPromocion().getHoraInicialVigencia()))	&& (!UtilidadTexto.isEmpty(this.getDtoPromocion().getHoraFinalVigencia()))) 
		{
			if (UtilidadFecha.esHoraMenorIgualQueOtraReferencia(this.getDtoPromocion().getHoraFinalVigencia(),this.getDtoPromocion().getHoraInicialVigencia())) 
			{
				errores.add("", new ActionMessage("errors.horaAnteriorAOtraDeReferencia",getDtoPromocion().getHoraFinalVigencia(), this.getDtoPromocion().getHoraInicialVigencia()));
			}
		}

		*/
		
		if (!UtilidadTexto.isEmpty(this.getDtoPromocion().getHoraInicialVigencia())) 
		{
			if (UtilidadTexto.isEmpty(this.getDtoPromocion().getHoraFinalVigencia())) 
			{
				errores.add("", new ActionMessage("errors.notEspecific","Hora Final Es Requerida"));
			}
		}
		
		if (!UtilidadTexto.isEmpty(this.getDtoPromocion().getHoraFinalVigencia())) 
		{
			if (UtilidadTexto.isEmpty(this.getDtoPromocion().getHoraInicialVigencia())) 
			{
				errores.add("", new ActionMessage("errors.notEspecific","Hora Inicial Es Requerida"));
			}
		}

		if (!UtilidadTexto.isEmpty(this.getDtoPromocion().getHoraInicialVigencia())	&& this.getDtoPromocion().getHoraInicialVigencia().length() < 5) 
		{
			errores.add("", new ActionMessage("errors.notEspecific","Formato de la hora inicial de vigencia invalido"));
		}

		if ( !UtilidadTexto.isEmpty(this.getDtoPromocion().getHoraFinalVigencia())	&& this.getDtoPromocion().getHoraFinalVigencia().length() < 5) 
		{
			errores.add("", new ActionMessage("errors.notEspecific","Formato de la hora final de vigencia invalido"));
		}
	}

	/**
	 * 
	 * @param errores
	 * 
	 *            private void porcentajeDescuento(ActionErrors errores) {
	 * 
	 *            if(this.getDtoDetPromocion().getPorcentajeDescuento()<0) {
	 *            if(this.getDtoDetPromocion().getPorcentajeDescuento()>100) {
	 *            errores.add("", new
	 *            ActionMessage("errors.range"," El Porcentaje Descuento"
	 *            ," 1","100%")); } } }
	 */

	/**
	 * 
	 * @param errores
	 */
	private void validarFecha(ActionErrors errores)

	{

		if (UtilidadTexto.isEmpty(this.getDtoPromocion().getFechaInicialVigencia())) {
			errores.add("", new ActionMessage("errors.required","La fecha Inicial"));
		}
		
		if (UtilidadTexto.isEmpty(this.getDtoPromocion().getFechaFinalVigencia())) {
			errores.add("", new ActionMessage("errors.required","La fecha Final"));
		}

		if ((!UtilidadTexto.isEmpty(this.getDtoPromocion().getFechaInicialVigencia())) 
						&& (!UtilidadTexto.isEmpty(this.getDtoPromocion().getFechaFinalVigencia()))) 
		{
			logger.info("\n\n\n\n\n***********************************************************************************************\n\n");

			if (!UtilidadFecha.esFechaValidaSegunAp(this.getDtoPromocion().getFechaInicialVigencia())) 
			{
				errores.add("", new ActionMessage("errors.formatoFechaInvalido", this.getDtoPromocion().getFechaInicialVigencia()));
			}
			

			if (!UtilidadFecha.esFechaValidaSegunAp(this.getDtoPromocion().getFechaFinalVigencia())) {
				errores.add("", new ActionMessage("errors.formatoFechaInvalido", this.getDtoPromocion().getFechaFinalVigencia()));
			}

			// <<<<<<<<<<<<<<>>>>>>>>>>>>>>
			if (errores.isEmpty()) {
				logger.info("NO TIENE ERORRESSSSSSSSSSSS?''''''''''''''''");
				if (!UtilidadTexto.isEmpty(this.getDtoPromocion()
						.getFechaInicialVigencia())
						&& !UtilidadTexto.isEmpty(this.getDtoPromocion()
								.getFechaFinalVigencia())) {
					if (!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this
							.getDtoPromocion().getFechaInicialVigencia(), this
							.getDtoPromocion().getFechaFinalVigencia())) {
						errores.add("", new ActionMessage(
								"errors.fechaPosteriorIgualActual",
								" Inicial "
										+ this.getDtoPromocion()
												.getFechaInicialVigencia(),
								" Final "
										+ this.getDtoPromocion()
												.getFechaFinalVigencia()));
					}
				}
			}
		}//	 

	}

	/**
	  * 
	  */
	public void reset() {
		this.posArray = 0;
		this.criterioBusqueda = "";
		this.patronOrdenar = "";
		// /
		this.listaPromociones = new ArrayList<DtoPromocionesOdontologicas>();
		this.listaCaPromociones = new ArrayList<DtoDetCaPromocionesOdo>();
		this.listaDetPromociones = new ArrayList<DtoDetPromocionOdo>();
		this.listaDetConvenios = new ArrayList<DtoDetConvPromocionesOdo>();
		//
		this.arrayCategorias = new ArrayList<DtoCategoriaAtencion>();
		this.arrayRegiones = new ArrayList<DtoRegionesCobertura>();

		this.regionCobertura = new DtoRegionesCobertura();
		this.categoriaAtencion = new DtoCategoriaAtencion();

		this.setCiudadesMap(new ArrayList<HashMap<String, Object>>());

		this.estado = "";
		this.dtoPromocion = new DtoPromocionesOdontologicas();
		this.dtoConvPromociones = new DtoDetConvPromocionesOdo();
		this.dtoCaPromociones = new DtoDetCaPromocionesOdo();
		this.dtoDetPromocion = new DtoDetPromocionOdo();
		
		
	
		/**
		 * 
		 */

		this.listaCentrosAtencion = new ArrayList<DtoCentrosAtencion>();
		this.listConvenios = new ArrayList<HashMap<String, Object>>();

		// Atributos---> tmp
		this.codigoConvenio = 0;
		this.codigoCentroAtencion = 0;
		this.tmpNombrePrograma = "";

		this.estadosCiviles = new HashMap();
		this.ocupaciones = new HashMap();

		this.setTmpCentroAtencion(new InfoDatosInt());

		this.codigoCentroAten = "";
		this.posArrayCentro = 0;
		this.posArrayConvenio = 0;

		this.esDescendente = "";

		// <<CARGAR PAIS>>
		this.listaPaises = new ArrayList<HashMap<String, Object>>();
		this.tmpCodigoPais = "";
		//this.especialidadesOdontologia = new ArrayList<HashMap>();
		//this.codigoEspecialidad = ConstantesBD.codigoNuncaValido;
		this.pais = new InfoDatosString();
		this.setCentroAtencion(new InfoDatosInt());
		// <<Mensajes>>
		this.mensajeCentro = "";
		this.mensajeConvenio = "";
		this.listaInstitucionEmpresa = new ArrayList<DtoEmpresasInstitucion>();
		this.dtoEmpresa = new DtoEmpresasInstitucion();
		this.utilizaInstitucionMultiempresa = "";
		this.utilizaInst=ConstantesBD.acronimoNo;
		this.tipoReporte="";
		this.mapaArchivoPlano = new HashMap();
		this.rutaArchivoRelativa="";
		this.rutaArchivoAbsoluta="";
		this.esConsulta=Boolean.FALSE;
		
		this.tmpCodigoDepartamento="";
		this.tmpCodigoCiudad="";
		
		this.tamanioLista=ConstantesBD.codigoNuncaValido;
		this.nombreArchivo="";
	}

	/**
	 * @return the posArray
	 */
	public int getPosArray() {
		return posArray;
	}

	/**
	 * @param posArray
	 *            the posArray to set
	 */
	public void setPosArray(int posArray) {
		this.posArray = posArray;
	}

	/**
	 * @return the criterioBusqueda
	 */
	public String getCriterioBusqueda() {
		return criterioBusqueda;
	}

	/**
	 * @param criterioBusqueda
	 *            the criterioBusqueda to set
	 */
	public void setCriterioBusqueda(String criterioBusqueda) {
		this.criterioBusqueda = criterioBusqueda;
	}

	/**
	 * @return the patronOrdenar
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	/**
	 * @param patronOrdenar
	 *            the patronOrdenar to set
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	/**
	 * @return the listaPromociones
	 */
	public ArrayList<DtoPromocionesOdontologicas> getListaPromociones() {
		return listaPromociones;
	}

	/**
	 * @param listaPromociones
	 *            the listaPromociones to set
	 */
	public void setListaPromociones(
			ArrayList<DtoPromocionesOdontologicas> listaPromociones) {
		this.listaPromociones = listaPromociones;
	}

	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado
	 *            the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return the dtoPromocion
	 */
	public DtoPromocionesOdontologicas getDtoPromocion() {
		return dtoPromocion;
	}

	/**
	 * @param dtoPromocion
	 *            the dtoPromocion to set
	 */
	public void setDtoPromocion(DtoPromocionesOdontologicas dtoPromocion) {
		this.dtoPromocion = dtoPromocion;
	}

	public void setDtoDetPromocion(DtoDetPromocionOdo dtoDetPromocion) {
		this.dtoDetPromocion = dtoDetPromocion;
	}

	public DtoDetPromocionOdo getDtoDetPromocion() {
		return dtoDetPromocion;
	}

	public void setListaDetPromociones(
			ArrayList<DtoDetPromocionOdo> listaDetPromociones) {
		this.listaDetPromociones = listaDetPromociones;
	}

	public ArrayList<DtoDetPromocionOdo> getListaDetPromociones() {
		return listaDetPromociones;
	}

	public void setListaCaPromociones(
			ArrayList<DtoDetCaPromocionesOdo> listaCaPromociones) {
		this.listaCaPromociones = listaCaPromociones;
	}

	public ArrayList<DtoDetCaPromocionesOdo> getListaCaPromociones() {
		return listaCaPromociones;
	}

	public void setDtoCaPromociones(DtoDetCaPromocionesOdo dtoCaPromociones) {
		this.dtoCaPromociones = dtoCaPromociones;
	}

	public DtoDetCaPromocionesOdo getDtoCaPromociones() {
		return dtoCaPromociones;
	}

	public void setDtoConvPromociones(
			DtoDetConvPromocionesOdo dtoConvPromociones) {
		this.dtoConvPromociones = dtoConvPromociones;
	}

	public DtoDetConvPromocionesOdo getDtoConvPromociones() {
		return dtoConvPromociones;
	}

	public void setArrayRegiones(ArrayList<DtoRegionesCobertura> arrayRegiones) {
		this.arrayRegiones = arrayRegiones;
	}

	public ArrayList<DtoRegionesCobertura> getArrayRegiones() {
		return arrayRegiones;
	}

	public void setArrayCategorias(
			ArrayList<DtoCategoriaAtencion> arrayCategorias) {
		this.arrayCategorias = arrayCategorias;
	}

	public ArrayList<DtoCategoriaAtencion> getArrayCategorias() {
		return arrayCategorias;
	}

	public void setRegionCobertura(DtoRegionesCobertura regionCobertura) {
		this.regionCobertura = regionCobertura;
	}

	public DtoRegionesCobertura getRegionCobertura() {
		return regionCobertura;
	}

	public void setCategoriaAtencion(DtoCategoriaAtencion categoriaAtencion) {
		this.categoriaAtencion = categoriaAtencion;
	}

	public DtoCategoriaAtencion getCategoriaAtencion() {
		return categoriaAtencion;
	}

	public void setListaDetConvenios(
			ArrayList<DtoDetConvPromocionesOdo> listaDetConvenios) {
		this.listaDetConvenios = listaDetConvenios;
	}

	public ArrayList<DtoDetConvPromocionesOdo> getListaDetConvenios() {
		return listaDetConvenios;
	}

	public void setCiudadesMap(ArrayList<HashMap<String, Object>> ciudadesMap) {
		this.ciudadesMap = ciudadesMap;
	}

	public ArrayList<HashMap<String, Object>> getCiudadesMap() {
		return ciudadesMap;
	}

	public void setListaCentrosAtencion(
			ArrayList<DtoCentrosAtencion> listaCentrosAtencion) {
		this.listaCentrosAtencion = listaCentrosAtencion;
	}

	public ArrayList<DtoCentrosAtencion> getListaCentrosAtencion() {
		return listaCentrosAtencion;
	}

	public void setListConvenios(
			ArrayList<HashMap<String, Object>> listConvenios) {
		this.listConvenios = listConvenios;
	}

	public ArrayList<HashMap<String, Object>> getListConvenios() {
		return listConvenios;
	}

	public void setCodiogoCentroAtencion(int codiogoCentroAtencion) {
		this.codigoCentroAtencion = codiogoCentroAtencion;
	}

	public int getCodiogoCentroAtencion() {
		return codigoCentroAtencion;
	}

	public void setCodigoConvenio(int codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}

	public int getCodigoConvenio() {
		return codigoConvenio;
	}

	public void setTmpNombrePrograma(String tmpNombrePrograma) {
		this.tmpNombrePrograma = tmpNombrePrograma;
	}

	public String getTmpNombrePrograma() {
		return tmpNombrePrograma;
	}

	public void setTmpCovenio(InfoDatosInt tmpCovenio) {
		this.tmpCovenio = tmpCovenio;
	}

	public InfoDatosInt getTmpCovenio() {
		return tmpCovenio;
	}

	public void setTmpCentroAtencion(InfoDatosInt tmpCentroAtencion) {
		this.tmpCentroAtencion = tmpCentroAtencion;
	}

	public InfoDatosInt getTmpCentroAtencion() {
		return tmpCentroAtencion;
	}

	/**
	 * @return the codigoCentroAten
	 */
	public String getCodigoCentroAten() {
		return codigoCentroAten;
	}

	/**
	 * @param codigoCentroAten
	 *            the codigoCentroAten to set
	 */
	public void setCodigoCentroAten(String codigoCentroAten) {
		this.codigoCentroAten = codigoCentroAten;
	}

	public void setPosArrayCentro(int posArrayCentro) {
		this.posArrayCentro = posArrayCentro;
	}

	public int getPosArrayCentro() {
		return posArrayCentro;
	}

	public void setPosArrayConvenio(int posArrayConvenio) {
		this.posArrayConvenio = posArrayConvenio;
	}

	public int getPosArrayConvenio() {
		return posArrayConvenio;
	}

	public void setEstadosCiviles(HashMap estadosCiviles) {
		this.estadosCiviles = estadosCiviles;
	}

	public HashMap getEstadosCiviles() {
		return estadosCiviles;
	}

	public void setOcupaciones(HashMap ocupaciones) {
		this.ocupaciones = ocupaciones;
	}

	public HashMap getOcupaciones() {
		return ocupaciones;
	}

	public void setEsDescendente(String esDescendente) {
		this.esDescendente = esDescendente;
	}

	public String getEsDescendente() {
		return esDescendente;
	}

	/**
	 * @return the tipodeRelacion
	 */
	public String getTipodeRelacion() {
		return tipodeRelacion;
	}

	/**
	 * @param tipodeRelacion
	 *            the tipodeRelacion to set
	 */
	public void setTipodeRelacion(String tipodeRelacion) {
		this.tipodeRelacion = tipodeRelacion;
	}

	/**
	 * @return the codigos
	 */
	public String getCodigos() {
		return codigos;
	}

	/**
	 * @param codigos
	 *            the codigos to set
	 */

	public void setCodigos(String codigos) {
		this.codigos = codigos;
	}

	/**
	 * @param listaPaises
	 *            the listaPaises to set
	 */
	public void setListaPaises(ArrayList<HashMap<String, Object>> listaPaises) {
		this.listaPaises = listaPaises;
	}

	/**
	 * @return the listaPaises
	 */
	public ArrayList<HashMap<String, Object>> getListaPaises() {
		return listaPaises;
	}

	/**
	 * @param tmpCodigoPais
	 *            the tmpCodigoPais to set
	 */
	public void setTmpCodigoPais(String tmpCodigoPais) {
		this.tmpCodigoPais = tmpCodigoPais;
	}

	/**
	 * @return the tmpCodigoPais
	 */
	public String getTmpCodigoPais() {
		return tmpCodigoPais;
	}

//	public ArrayList<HashMap> getEspecialidadesOdontologia() {
//		return especialidadesOdontologia;
//	}
//
//	public void setEspecialidadesOdontologia(
//			ArrayList<HashMap> especialidadesOdontologia) {
//		this.especialidadesOdontologia = especialidadesOdontologia;
//	}

//	/**
//	 * @param codigoEspecialidad
//	 *            the codigoEspecialidad to set
//	 */
//	public void setCodigoEspecialidad(int codigoEspecialidad) {
//		this.codigoEspecialidad = codigoEspecialidad;
//	}
//
//	/**
//	 * @return the codigoEspecialidad
//	 */
//	public int getCodigoEspecialidad() {
//		return codigoEspecialidad;
//	}

	void recorrer() {

		for (int a = 0; a < this.listaPromociones.size(); a++) {

			for (int b = 0; b < this.listaPromociones.get(a)
					.getDtoDetPromociones().getListDetCa().size(); b++) {

			}

			for (int c = 0; c < this.listaPromociones.get(a)
					.getDtoDetPromociones().getListDetConv().size(); c++) {

			}
		}

	}

	/**
	 * @param pais
	 *            the pais to set
	 */
	public void setPais(InfoDatosString pais) {
		this.pais = pais;
	}

	/**
	 * @return the pais
	 */
	public InfoDatosString getPais() {
		return pais;
	}

	/**
	 * @param centroAtencion
	 *            the centroAtencion to set
	 */
	public void setCentroAtencion(InfoDatosInt centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	/**
	 * @return the centroAtencion
	 */
	public InfoDatosInt getCentroAtencion() {
		return centroAtencion;
	}

	public void setMensajeCentro(String mensajeCentro) {
		this.mensajeCentro = mensajeCentro;
	}

	public String getMensajeCentro() {
		return mensajeCentro;
	}

	public void setMensajeConvenio(String mensajeConvenio) {
		this.mensajeConvenio = mensajeConvenio;
	}

	public String getMensajeConvenio() {
		return mensajeConvenio;
	}

	public void setListaInstitucionEmpresa(
			ArrayList<DtoEmpresasInstitucion> listaInstitucionEmpresa) {
		this.listaInstitucionEmpresa = listaInstitucionEmpresa;
	}

	public ArrayList<DtoEmpresasInstitucion> getListaInstitucionEmpresa() {
		return listaInstitucionEmpresa;
	}

	public void setDtoEmpresa(DtoEmpresasInstitucion dtoEmpresa) {
		this.dtoEmpresa = dtoEmpresa;
	}

	public DtoEmpresasInstitucion getDtoEmpresa() {
		return dtoEmpresa;
	}

	public void setUtilizaInstitucionMultiempresa(
			String utilizaInstitucionMultiempresa) {
		this.utilizaInstitucionMultiempresa = utilizaInstitucionMultiempresa;
	}

	public String getUtilizaInstitucionMultiempresa() {
		return utilizaInstitucionMultiempresa;
	}

	public void setUtilizaInst(String utilizaInst) {
		this.utilizaInst = utilizaInst;
	}

	public String getUtilizaInst() {
		return utilizaInst;
	}

	/**
	 * @return the tipoReporte
	 */
	public String getTipoReporte() {
		return tipoReporte;
	}

	/**
	 * @param tipoReporte the tipoReporte to set
	 */
	public void setTipoReporte(String tipoReporte) {
		this.tipoReporte = tipoReporte;
	}

	/**
	 * @return the mapaArchivoPlano
	 */
	public HashMap getMapaArchivoPlano() {
		return mapaArchivoPlano;
	}

	/**
	 * @param mapaArchivoPlano the mapaArchivoPlano to set
	 */
	public void setMapaArchivoPlano(HashMap mapaArchivoPlano) {
		this.mapaArchivoPlano = mapaArchivoPlano;
	}
	
	/**
	 * @return the mapaArchivoPlano
	 */
	public Object getMapaArchivoPlano(String llave) {
		return mapaArchivoPlano.get(llave);
	}

	/**
	 * @param mapaArchivoPlano the mapaArchivoPlano to set
	 */
	public void setMapaArchivoPlano(String llave, Object obj) {
		this.mapaArchivoPlano.put(llave, obj);
	}
	
	
	public String getRutaArchivoRelativa() {
		return rutaArchivoRelativa;
	}

	public void setRutaArchivoRelativa(String rutaArchivoRelativa) {
		this.rutaArchivoRelativa = rutaArchivoRelativa;
	}

	public String getRutaArchivoAbsoluta() {
		return rutaArchivoAbsoluta;
	}

	public void setRutaArchivoAbsoluta(String rutaArchivoAbsoluta) {
		this.rutaArchivoAbsoluta = rutaArchivoAbsoluta;
	}

	public void setEsConsulta(Boolean esConsulta) {
		this.esConsulta = esConsulta;
	}

	public Boolean getEsConsulta() {
		return esConsulta;
	}

	public void setTmpCodigoDepartamento(String tmpCodigoDepartamento) {
		this.tmpCodigoDepartamento = tmpCodigoDepartamento;
	}

	public String getTmpCodigoDepartamento() {
		return tmpCodigoDepartamento;
	}

	public void setTmpCodigoCiudad(String tmpCodigoCiudad) {
		this.tmpCodigoCiudad = tmpCodigoCiudad;
	}

	public String getTmpCodigoCiudad() {
		return tmpCodigoCiudad;
	}

	public void setTamanioLista(int tamanioLista) {
		this.tamanioLista = tamanioLista;
	}

	
	public int getTamanioLista() {
		return this.getListaPromociones().size();
	}

	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}

	public String getNombreArchivo() {
		return nombreArchivo;
	}

	

}
