/*
 * Nov 30, 2005
 *
 */
package com.princetonsa.actionform.salasCirugia;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.UtilidadFecha;

/**
 *  @author Sebastián Gómez
 *
 *Clase que almacena y carga la información utilizada para la funcionalidad
 *Modificar-Reversar Liquidacion Qx.
 */
public class ModificarReversarQxForm extends ValidatorForm 
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(ModificarReversarQxForm.class);
	
	/**
	 * Variable usada para almacenar el estado del controlador
	 */
	private String estado;
	
	/**
	 * Código de la cuenta/subcuenta de la solicitud cirugia
	 */
	private int idCuenta;
	
	/**
	 * Código del estado de la cuenta
	 */
	private int estadoCuenta;
	
	/**
	 * Código Axioma de la solicitud Quirurgica
	 */
	private int numeroSolicitud;
	
	
	/**
	 * Login del usuario
	 */
	private String usuario;
	
	//*******ATRIBUTOS DE INFORMACIÓN DE LA ORDEN***************************
	/**
	 * Datos generales de la orden
	 */
	private HashMap encabezadoSolicitud = new HashMap();
	
	/**
	 * Datos de las cirugias de la orden
	 */
	private HashMap cirugiasSolicitud = new HashMap();
	
	/**
	 * Número de registros del mapa cirugiasSolicitud
	 */
	private int numCirugiasSolicitud ; 
	
	/**
	 * Datos de la hoja Qx.
	 */
	private HashMap hojaQx = new HashMap();
	
	/**
	 * Datos de la hoja de Anestesia
	 */
	private HashMap hojaAnestesia = new HashMap();
	
	/**
	 * Datos de los asocios de cada cirugia
	 */
	private HashMap asocios = new HashMap();
	
	//Atributos de validacion de la modificacion
	private boolean puedoModificar;
	private boolean puedoReversar;
	
	/**
	 * Objeto que almacena el listado de los materiales especiales
	 */
	private ArrayList<HashMap<String, Object>> materialesEspeciales = new ArrayList<HashMap<String,Object>>();
	
	//*******ATRIBUTOS PARA EL LISTADO INICIAL DE ORDENES*********************
	/**
	 * Ordenes Cirugia de la cuenta
	 */
	private HashMap ordenes = new HashMap();
	
	/**
	 * Número de registros del mapa ordenes
	 */
	private int numOrdenes;
	
	/**
	 * Variable que manejan la ordenacion del listado
	 */
	private String indice;
	private String ultimoIndice;
	
	/**
	 * Tamaño de files por pagina
	 */
	private int maxPageItems;
	
	/**
	 *URL de la paginaciòn 
	 */
	private String linkSiguiente;
	
	/**
	 * Offset del mapa
	 */
	private int offset;
	
	//************ATRIBUTOS PARA LA REVERSIÓN********************************
	/**
	 * Motivo de la Reversión
	 */
	private String motivo;
	
	//************************************************************************
	//*********ATRIBUTOS PARA LA CONSULTA LOG **********************************
	/**
	 * Código del tipo de cambio que se desea buscar
	 */
	private int tipoCambio;
	
	/**
	 * Rango de fechas de cambio
	 */
	private String fechaInicial;
	private String fechaFinal;
	
	/**
	 * Rango de consecutivos de ordenes médicas
	 */
	private int ordenInicial;
	private int ordenFinal;
	
	/**
	 * Datos del paciente
	 */
	private String tipoIdentificacion;
	private String numeroIdentificacion;
	
	/**
	 * Código de la institucion
	 */
	private String institucion;
	
	/**
	 * Código del centro de atencion
	 */
	private String centroAtencion;
	
	/**
	 * Código del cambio liquidacion
	 */
	private int codigoRegistro;
	
	//*******datos usados en el detalle de la consulta del LOG********************
	/**
	 * Código del tipo de cambio
	 */
	private int codigoTipoCambio;
	
	/**
	 * Número de registros
	 */
	private int numRegistros;
	//****************************************************************************
	//*******ATRIBUTOS PARA LLENAR EL PROFESIONAL/ESPECIALIDAD FALTANTES***********
	private ArrayList<HashMap<String, Object>> profesionales;
	private String codigoProfesional;
	private String indexProf;
	//************************************************************************
	
	/**
	 * Validate the properties that have been set from this HTTP request, and
	 * return an <code>ActionErrors</code> object that encapsulates any
	 * validation errors that have been found.  If no errors are found, return
	 * <code>null</code> or an <code>ActionErrors</code> object with no recorded
	 * error messages.
	 *
	 * @param mapping The mapping used to select this instance
	 * @param request The servlet request we are processing
	*/
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		
		if(this.estado.equals("reversar"))
		{
			if(this.motivo.trim().equals(""))
				errores.add("Motivo Requerido",new ActionMessage("errors.required","El motivo de la reversión"));
		}
		else if(this.estado.equals("modificar"))
		{
			//Iteracion Cirugias
			int auxI0 = 0;
			String auxS0 = "";
			for(int i=0;i<this.numCirugiasSolicitud;i++)
			{
				auxI0 = Integer.parseInt(this.getAsocios("numAsocios_"+i)+"");
				for(int j=0;j<auxI0;j++)
				{
					auxS0 = this.getAsocios("valorAsocio_"+i+"_"+j)+"";
					if(auxS0.equals(""))
						errores.add("Valor Asocios Requerido",
							new ActionMessage("errors.required",
							"El valor de "+this.getAsocios("nombreAsocio_"+i+"_"+j)+
							" en la cirugía Nº "+(i+1)));
				}
			}
			
			if(!errores.isEmpty())
				this.estado = "iniciarModificar";
		}
		else if(this.estado.equals("consultar"))
		{
			//revisión de fechas
			errores = this.revisionFechas(errores);
			
			//revision rango ordenes
			if(ordenInicial>0&&ordenFinal>0)
			{
				if(ordenFinal<ordenInicial)
					errores.add("orden Final menor que la orden inicial",new ActionMessage("errors.integerMenorIgualQue","El Nº de orden inicial","el Nº de orden final"));
			}
			else if(ordenInicial<=0&&ordenFinal>0)
				errores.add("orden inicial requerido",new ActionMessage("errors.required","El Nº de orden inicial"));
			else if(ordenInicial>0&&ordenFinal<=0)
				errores.add("orden final requerido",new ActionMessage("errors.required","El Nº de orden final"));
			
			//revisión del paciente
			if(this.tipoIdentificacion.equals("")&&!this.numeroIdentificacion.equals(""))
				errores.add("tipo identificación",new ActionMessage("errors.required","El tipo de identificación"));
			else if(!this.tipoIdentificacion.equals("")&&this.numeroIdentificacion.equals(""))
				errores.add("numero de identificacion",new ActionMessage("errors.required","El número de identificación"));
		}
		
		return errores;
	}
	
	
	/**
	 * Método para la revisión de los rangos de las fechas
	 * @param errores
	 * @return
	 */
	private ActionErrors revisionFechas(ActionErrors errores) {
		int resp1=0;
		int resp2=0;
		
		if(!this.fechaInicial.equals(""))
		{
			resp1=1;
			if(UtilidadFecha.validarFecha(this.getFechaInicial()))
				resp1=2;
			else
				errores.add("fecha cambio inicial", new ActionMessage("errors.formatoFechaInvalido",this.getFechaInicial()));
		}
		
		if(!this.fechaFinal.equals(""))
		{
			resp2=1;
			if(UtilidadFecha.validarFecha(this.getFechaFinal()))
				resp2=2;
			else
				errores.add("fecha cambio final", new ActionMessage("errors.formatoFechaInvalido",this.getFechaFinal()));
		}
		
		//revisar si las fechas son válidas
		if(resp1==2&&resp2==2)
		{
			//si la fecha inicial es mayor a la fecha actual
			if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaInicial())).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>0)
			{
				errores.add("fecha cambio inicial", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "inicial de cambio", "del sistema"));
			}
			
			//si la fecha final es mayor a la fecha actual
			if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaFinal())).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>0)
			{
				errores.add("fecha cambio final", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "final de cambio", "del sistema"));
			}
			
			//si la fecha inicial es mayor a la fecha final
			if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaInicial())).compareTo(UtilidadFecha.conversionFormatoFechaABD(this.getFechaFinal()))>0)
			{
				errores.add("fecha inicial mayor a la fecha final", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "inicial de cambio", "final de cambio"));
			}
		}
		else
		{
			//caso en el que falte alguna fecha del rango
			if(resp1==0&&resp2>0)
			{
				errores.add("La Fecha Cambio Inicial", new ActionMessage("errors.required", "La fecha inicial de cambio"));
			}
			
			if(resp2==0&&resp1>0)
			{
				errores.add("La Fecha Cambio Final", new ActionMessage("errors.required", "La fecha final de cambio"));
			}
			
			
		}
		return errores;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * reset de los datos de la forma
	 *
	 */
	public void reset()
	{
		this.estado = "";
		this.idCuenta = 0;
		this.estadoCuenta = 0;
		this.ordenes = new HashMap();
		this.numOrdenes = 0;
		this.numeroSolicitud = 0;
		this.indice = "";
		this.ultimoIndice = "";
		this.maxPageItems = 0;
		this.offset = 0;
		this.linkSiguiente = "";
		this.usuario = "";
		
		//datos de la orden
		this.encabezadoSolicitud = new HashMap();
		this.cirugiasSolicitud = new HashMap();
		this.numCirugiasSolicitud = 0;
		this.hojaQx = new HashMap();
		this.hojaAnestesia = new HashMap();
		this.asocios = new HashMap();
		
		//datos de la reversion
		this.motivo = "";
		
		//datos de la consulta LOG
		this.tipoCambio = 3;
		this.fechaInicial = "";
		this.fechaFinal = "";
		this.ordenInicial = -1;
		this.ordenFinal = -1;
		this.tipoIdentificacion = "";
		this.numeroIdentificacion = "";
		this.institucion = "0";
		this.centroAtencion = "0";
		this.codigoRegistro = 0;
		this.numRegistros = 0;
		this.codigoTipoCambio = 0;
		
		//Atributos de validacion de la modificacion
		this.puedoModificar = true;
		this.puedoReversar = true;
		
		this.materialesEspeciales = new ArrayList<HashMap<String,Object>>();
		
		//Atributos para llenar profesional/especialidad faltante
		this.profesionales = new ArrayList<HashMap<String,Object>>();
		this.codigoProfesional = "";
		this.indexProf = "";
	}
	
	
	/**
	 * @return Returns the estado.
	 */
	public String getEstado() {
		return estado;
	}
	/**
	 * @param estado The estado to set.
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}
	/**
	 * @return Returns the estadoCuenta.
	 */
	public int getEstadoCuenta() {
		return estadoCuenta;
	}
	/**
	 * @param estadoCuenta The estadoCuenta to set.
	 */
	public void setEstadoCuenta(int estadoCuenta) {
		this.estadoCuenta = estadoCuenta;
	}
	/**
	 * @return Returns the idCuenta.
	 */
	public int getIdCuenta() {
		return idCuenta;
	}
	/**
	 * @param idCuenta The idCuenta to set.
	 */
	public void setIdCuenta(int idCuenta) {
		this.idCuenta = idCuenta;
	}
	/**
	 * @return Returns the numOrdenes.
	 */
	public int getNumOrdenes() {
		return numOrdenes;
	}
	/**
	 * @param numOrdenes The numOrdenes to set.
	 */
	public void setNumOrdenes(int numOrdenes) {
		this.numOrdenes = numOrdenes;
	}
	/**
	 * @return Returns the ordenes.
	 */
	public HashMap getOrdenes() {
		return ordenes;
	}
	/**
	 * @param ordenes The ordenes to set.
	 */
	public void setOrdenes(HashMap ordenes) {
		this.ordenes = ordenes;
	}
	
	/**
	 * @return Retorna un elemento del mapa ordenes.
	 */
	public Object getOrdenes(String key) {
		return ordenes.get(key);
	}
	/**
	 * @param Asigna un elemento al mapa ordenes
	 */
	public void setOrdenes(String key,Object obj) {
		this.ordenes.put(key,obj);
	}
	/**
	 * @return Returns the numeroSolicitud.
	 */
	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}
	/**
	 * @param numeroSolicitud The numeroSolicitud to set.
	 */
	public void setNumeroSolicitud(int numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}
	/**
	 * @return Returns the indice.
	 */
	public String getIndice() {
		return indice;
	}
	/**
	 * @param indice The indice to set.
	 */
	public void setIndice(String indice) {
		this.indice = indice;
	}
	/**
	 * @return Returns the ultimoIndice.
	 */
	public String getUltimoIndice() {
		return ultimoIndice;
	}
	/**
	 * @param ultimoIndice The ultimoIndice to set.
	 */
	public void setUltimoIndice(String ultimoIndice) {
		this.ultimoIndice = ultimoIndice;
	}
	/**
	 * @return Returns the maxPageItems.
	 */
	public int getMaxPageItems() {
		return maxPageItems;
	}
	/**
	 * @param maxPageItems The maxPageItems to set.
	 */
	public void setMaxPageItems(int maxPageItems) {
		this.maxPageItems = maxPageItems;
	}
	/**
	 * @return Returns the offset.
	 */
	public int getOffset() {
		return offset;
	}
	/**
	 * @param offset The offset to set.
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}
	/**
	 * @return Returns the linkSiguiente.
	 */
	public String getLinkSiguiente() {
		return linkSiguiente;
	}
	/**
	 * @param linkSiguiente The linkSiguiente to set.
	 */
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}
	/**
	 * @return Returns the encabezadoSolicitud.
	 */
	public HashMap getEncabezadoSolicitud() {
		return encabezadoSolicitud;
	}
	/**
	 * @param encabezadoSolicitud The encabezadoSolicitud to set.
	 */
	public void setEncabezadoSolicitud(HashMap encabezadoSolicitud) {
		this.encabezadoSolicitud = encabezadoSolicitud;
	}
	/**
	 * @return Retorna un elemento del mapa encabezadoSolicitud.
	 */
	public Object getEncabezadoSolicitud(String key) {
		return encabezadoSolicitud.get(key);
	}
	/**
	 * @param asigna un elemento al mapa encabezadoSolicitud .
	 */
	public void setEncabezadoSolicitud(String key,Object obj) {
		this.encabezadoSolicitud.put(key,obj);
	}
	/**
	 * @return Returns the cirugiasSolicitud.
	 */
	public HashMap getCirugiasSolicitud() {
		return cirugiasSolicitud;
	}
	/**
	 * @param cirugiasSolicitud The cirugiasSolicitud to set.
	 */
	public void setCirugiasSolicitud(HashMap cirugiasSolicitud) {
		this.cirugiasSolicitud = cirugiasSolicitud;
	}
	
	/**
	 * @return Retorna un elemento del mapa cirugiasSolicitud.
	 */
	public Object getCirugiasSolicitud(String key) {
		return cirugiasSolicitud.get(key);
	}
	/**
	 * @param asigna un elemento al mapa cirugiasSolicitud.
	 */
	public void setCirugiasSolicitud(String key,Object obj) {
		this.cirugiasSolicitud.put(key,obj);
	}
	/**
	 * @return Returns the numCirugiasSolicitud.
	 */
	public int getNumCirugiasSolicitud() {
		return numCirugiasSolicitud;
	}
	/**
	 * @param numCirugiasSolicitud The numCirugiasSolicitud to set.
	 */
	public void setNumCirugiasSolicitud(int numCirugiasSolicitud) {
		this.numCirugiasSolicitud = numCirugiasSolicitud;
	}
	/**
	 * @return Returns the hojaQx.
	 */
	public HashMap getHojaQx() {
		return hojaQx;
	}
	/**
	 * @param hojaQx The hojaQx to set.
	 */
	public void setHojaQx(HashMap hojaQx) {
		this.hojaQx = hojaQx;
	}
	
	/**
	 * @return Retorna un elemento del mapa hojaQx.
	 */
	public Object getHojaQx(String key) {
		return hojaQx.get(key);
	}
	/**
	 * @param asigna un elemento al mapa hojaQx .
	 */
	public void setHojaQx(String key,Object obj) {
		this.hojaQx.put(key,obj);
	}
	/**
	 * @return Returns the hojaAnestesia.
	 */
	public HashMap getHojaAnestesia() {
		return hojaAnestesia;
	}
	/**
	 * @param hojaAnestesia The hojaAnestesia to set.
	 */
	public void setHojaAnestesia(HashMap hojaAnestesia) {
		this.hojaAnestesia = hojaAnestesia;
	}
	
	/**
	 * @return Retorna un elemento del mapa hojaAnestesia.
	 */
	public Object getHojaAnestesia(String key) {
		return hojaAnestesia.get(key);
	}
	/**
	 * @param asigna un elemento del mapa hojaAnestesia .
	 */
	public void setHojaAnestesia(String key,Object obj) {
		this.hojaAnestesia.put(key,obj);
	}
	/**
	 * @return Returns the asocios.
	 */
	public HashMap getAsocios() {
		return asocios;
	}
	/**
	 * @param asocios The asocios to set.
	 */
	public void setAsocios(HashMap asocios) {
		this.asocios = asocios;
	}
	
	/**
	 * @return Retorna un elemento del mapa asocios.
	 */
	public Object getAsocios(String key) {
		return asocios.get(key);
	}
	/**
	 * @param Asigna un elemento al mapa asocios .
	 */
	public void setAsocios(String key,Object obj) {
		this.asocios.put(key,obj);
	}
	
	/**
	 * @return Returns the motivo.
	 */
	public String getMotivo() {
		return motivo;
	}
	/**
	 * @param motivo The motivo to set.
	 */
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	/**
	 * @return Returns the usuario.
	 */
	public String getUsuario() {
		return usuario;
	}
	/**
	 * @param usuario The usuario to set.
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	/**
	 * @return Returns the fechaFinal.
	 */
	public String getFechaFinal() {
		return fechaFinal;
	}
	/**
	 * @param fechaFinal The fechaFinal to set.
	 */
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}
	/**
	 * @return Returns the fechaInicial.
	 */
	public String getFechaInicial() {
		return fechaInicial;
	}
	/**
	 * @param fechaInicial The fechaInicial to set.
	 */
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}
	/**
	 * @return Returns the numeroIdentificacion.
	 */
	public String getNumeroIdentificacion() {
		return numeroIdentificacion;
	}
	/**
	 * @param numeroIdentificacion The numeroIdentificacion to set.
	 */
	public void setNumeroIdentificacion(String numeroIdentificacion) {
		this.numeroIdentificacion = numeroIdentificacion;
	}
	/**
	 * @return Returns the ordenFinal.
	 */
	public int getOrdenFinal() {
		return ordenFinal;
	}
	/**
	 * @param ordenFinal The ordenFinal to set.
	 */
	public void setOrdenFinal(int ordenFinal) {
		this.ordenFinal = ordenFinal;
	}
	/**
	 * @return Returns the ordenInicial.
	 */
	public int getOrdenInicial() {
		return ordenInicial;
	}
	/**
	 * @param ordenInicial The ordenInicial to set.
	 */
	public void setOrdenInicial(int ordenInicial) {
		this.ordenInicial = ordenInicial;
	}
	/**
	 * @return Returns the tipoCambio.
	 */
	public int getTipoCambio() {
		return tipoCambio;
	}
	/**
	 * @param tipoCambio The tipoCambio to set.
	 */
	public void setTipoCambio(int tipoCambio) {
		this.tipoCambio = tipoCambio;
	}
	/**
	 * @return Returns the tipoIdentificacion.
	 */
	public String getTipoIdentificacion() {
		return tipoIdentificacion;
	}
	/**
	 * @param tipoIdentificacion The tipoIdentificacion to set.
	 */
	public void setTipoIdentificacion(String tipoIdentificacion) {
		this.tipoIdentificacion = tipoIdentificacion;
	}
	/**
	 * @return Returns the institucion.
	 */
	public String getInstitucion() {
		return institucion;
	}
	/**
	 * @param institucion The institucion to set.
	 */
	public void setInstitucion(String institucion) {
		this.institucion = institucion;
	}
	/**
	 * @return Returns the codigoRegistro.
	 */
	public int getCodigoRegistro() {
		return codigoRegistro;
	}
	/**
	 * @param codigoRegistro The codigoRegistro to set.
	 */
	public void setCodigoRegistro(int codigoRegistro) {
		this.codigoRegistro = codigoRegistro;
	}
	/**
	 * @return Returns the numRegistros.
	 */
	public int getNumRegistros() {
		return numRegistros;
	}
	/**
	 * @param numRegistros The numRegistros to set.
	 */
	public void setNumRegistros(int numRegistros) {
		this.numRegistros = numRegistros;
	}
	/**
	 * @return Returns the codigoTipoCambio.
	 */
	public int getCodigoTipoCambio() {
		return codigoTipoCambio;
	}
	/**
	 * @param codigoTipoCambio The codigoTipoCambio to set.
	 */
	public void setCodigoTipoCambio(int codigoTipoCambio) {
		this.codigoTipoCambio = codigoTipoCambio;
	}


	/**
	 * @return Returns the centroAtencion.
	 */
	public String getCentroAtencion() {
		return centroAtencion;
	}


	/**
	 * @param centroAtencion The centroAtencion to set.
	 */
	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
	}


	/**
	 * @return the puedoModificar
	 */
	public boolean isPuedoModificar() {
		return puedoModificar;
	}


	/**
	 * @param puedoModificar the puedoModificar to set
	 */
	public void setPuedoModificar(boolean puedoModificar) {
		this.puedoModificar = puedoModificar;
	}



	/**
	 * @return the puedoReversar
	 */
	public boolean isPuedoReversar() {
		return puedoReversar;
	}


	/**
	 * @param puedoReversar the puedoReversar to set
	 */
	public void setPuedoReversar(boolean puedoReversar) {
		this.puedoReversar = puedoReversar;
	}


	/**
	 * @return the materialesEspeciales
	 */
	public ArrayList<HashMap<String, Object>> getMaterialesEspeciales() {
		return materialesEspeciales;
	}


	/**
	 * @param materialesEspeciales the materialesEspeciales to set
	 */
	public void setMaterialesEspeciales(
			ArrayList<HashMap<String, Object>> materialesEspeciales) {
		this.materialesEspeciales = materialesEspeciales;
	}
	
	/**
	 * Método para obtener el tamaño de los materiales especiales
	 * @return
	 */
	public int getNumMaterialesEspeciales()
	{
		return this.materialesEspeciales.size();
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
	 * @return the codigoProfesional
	 */
	public String getCodigoProfesional() {
		return codigoProfesional;
	}


	/**
	 * @param codigoProfesional the codigoProfesional to set
	 */
	public void setCodigoProfesional(String codigoProfesional) {
		this.codigoProfesional = codigoProfesional;
	}


	/**
	 * @return the indexProf
	 */
	public String getIndexProf() {
		return indexProf;
	}


	/**
	 * @param indexProf the indexProf to set
	 */
	public void setIndexProf(String indexProf) {
		this.indexProf = indexProf;
	}
}
