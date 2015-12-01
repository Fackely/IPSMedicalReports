package com.princetonsa.actionform.odontologia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.ResultadoBoolean;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.odontologia.InfoAntecedenteOdonto;
import util.odontologia.InfoOdontograma;

import com.princetonsa.dto.administracion.DtoPaciente;
import com.princetonsa.dto.facturacion.DtoFacturaAutomaticaOdontologica;
import com.princetonsa.dto.historiaClinica.DtoPlantillasIngresos;
import com.princetonsa.dto.historiaClinica.DtoValoracionConsulta;
import com.princetonsa.dto.historiaClinica.DtoValoracionUrgencias;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoPlantilla;
import com.princetonsa.dto.odontologia.DtoCitaOdontologica;
import com.princetonsa.dto.odontologia.DtoComponenteIndicePlaca;
import com.princetonsa.dto.odontologia.DtoInfoAtencionCitaOdo;
import com.princetonsa.dto.odontologia.DtoValDiagnosticosOdo;
import com.princetonsa.dto.odontologia.DtoValoracionesOdonto;
import com.princetonsa.enu.impresion.EnumTiposSalida;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.odontologia.ValoracionOdontologica;

public class ValoracionOdontologicaForm extends ValidatorForm{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6653836726743549749L;
	/**
	 * Estado del flujo
	 */
	private String estado;
	private String estadoSecundario;
	private DtoPlantilla plantilla;
	
	
	//Elementos que se reciben de manera parametrizada
	private int codigoPlantilla;
	
	private double cita;
	
	private int  ingreso;
	
	private int numeroSolicitud;
	//Fin elementos que se reciben de manera parametrizada
	
	private boolean guardo;
	
	//
	private DtoPlantillasIngresos dtoIngreso;
	
	private DtoValoracionesOdonto dtoValoraciones;
	
	
	private DtoValDiagnosticosOdo dtoDiagnosticoOdon;
	
	/**
	 * Se toma el codigo de la funcionalidad
	 */
	private int codigoFuncionalidad;
	
	/**
	 * Vï¿½a de ingreso de la valoracion
	 */
	private InfoDatosInt viaIngreso = new InfoDatosInt();
	
	/**
	 * Elementos para la plantilla de valoracion odontologica
	 */
	
	//Elementos de Datos Generales
	private String fechaValoracion;
	
	private String horaConsulta;
	
	/**
	 * Mapa para manejar los diagnosticos relacionados
	 */
	private HashMap<String, Object> diagnosticosRelacionados;
	
	/**
	 * Variable que obtiene el listado de los diagnosticos seleccionados
	 */
	private String diagnosticosSeleccionados;
	
	private DtoValoracionConsulta valoracionConsulta;
	
	private DtoValoracionUrgencias valoracionUrgencias = new DtoValoracionUrgencias();

	private ArrayList<DtoValDiagnosticosOdo> listadoDiagnosticosValoracion = new ArrayList<DtoValDiagnosticosOdo>();
	
	/**
	 * Atributos para manejar la apertura/cierre de secciones desplegables
	 */
	private boolean seccionRevisionSistemas;
	private boolean seccionExamenFisico;
	private boolean seccionEstadoSalida;
	private boolean seccionTipoMonitoreo;
	
	private String codigoEvolucionAsociada;
	
	//*******************ARREGLOS*********************************************
	private ArrayList<HashMap<String, Object>> estadosConciencia;
	private ArrayList<HashMap<String, Object>> causasExternas;
	private ArrayList<HashMap<String, Object>> finalidades;
	private ArrayList<HashMap<String, Object>> conductasValoracion;
	private ArrayList<HashMap<String, Object>> tiposDiagnostico;
	private ArrayList<HashMap<String, Object>> tiposMonitoreo;
	//Arreglos para componente
	private ArrayList<HashMap<String, Object>> rangosEdadMenarquia;
	private ArrayList<HashMap<String, Object>> rangosEdadMenopausia;
	private ArrayList<HashMap<String, Object>> conceptosMenstruacion;
	
	//**************************************************************************
	
	
	//ANCLA DE LA PAGINA EN CASO DE MANEJAR.
	private String ancla;
	
	//*******************ATRIBUTOS PARA EL COMPONENTE DE ODONTOGRAMA************
	/**
	 * 
	 * */
	private InfoOdontograma infoCompOdont;
	
	//***************************************************************************
	
	
	//*******************ATRIBUTOS PARA EL COMPONENTE DE ANTECEDENTES ODONTOLOGICOS************
	/**
	 * 
	 */
	private InfoAntecedenteOdonto infoCompAnteOdont;
	//***************************************************************************
	
	
	//*******************ATRIBUTOS PARA EL COMPONENTE INDICE DE PLACA************
	/**
	 * 
	 */
	private DtoComponenteIndicePlaca compIndicePlaca;
	//***************************************************************************
	

	// Atributo comunt para toda la plantilla de valoracion odontologica
	private String porConfirmar;
	//Atributo para saber si la plantilla es para actualizar o nueva
	private String porActualizar;
	
	private DtoInfoAtencionCitaOdo dtoAtencionCita;
	
	private DtoCitaOdontologica dtoCita;
	
	/**
	 * Atributo para almacenar la posicion de la plantilla a la cual
	 * se desea ingresar
	 */
	private int posicionPlantilla;
	
	private String abrirPopUp;
	
	private String cerrarVentanaConf;
	
	private ValoracionOdontologica mundoValoracion;
	
	private String secSeleccionarFormulario;
	
	
	//*******************ATRIBUTOS PARA LA IMPRESION****************************************************
	private InstitucionBasica institucionBasica;
	private ArrayList<DtoPlantilla> plantillas;
	private DtoPaciente paciente;
	private String fechaNacimientoPacienteFormateado;
	private String titulo;
	private String tipoCitaFormateado;
	private String plantillaBase;
	private UsuarioBasico usuarioResumen;
	private String fechaResumen;
	private String horaResumen;
	private String nombreContexto;
	//************************************************************************************************
	
	
	/*******IMPRESION PDF**********/
	private EnumTiposSalida enumTipoSalida;
	private String nombreArchivoGenerado;
	private String redireccion;
	
	
	
	/**
	 * Mostrar mensaje al usuario
	 */
	private ResultadoBoolean mostrarMensaje;
	
	/**
	 * Facturas automaticas posiblemente generadas
	 */
	private DtoFacturaAutomaticaOdontologica facturasAutomaticas;
	
	
	public ResultadoBoolean getMostrarMensaje() {
		return mostrarMensaje;
	}

	public void setMostrarMensaje(ResultadoBoolean mostrarMensaje) {
		this.mostrarMensaje = mostrarMensaje;
	}

	@Override
	public ActionErrors validate(ActionMapping arg0, HttpServletRequest arg1) {
		// por ahora no valido nada
		return super.validate(arg0, arg1);
		
		
	}
	
	public ValoracionOdontologicaForm()
	{
		clean(null, false);
	}

	/**
	 * 
	 * @param request
	 * @param borrarCodigoCita El c&oacute;digo de la cita se utiliza para cargar toda la plantilla, por lo tanto en el caso de
	 * recargar no se puede eliminar este dato
	 */
	public void clean(HttpServletRequest request, boolean borrarCodigoCita) {
		this.ancla="";
		this.estadoSecundario = "";
		this.codigoEvolucionAsociada=ConstantesBD.codigoNuncaValido+"";
		if(borrarCodigoCita)
		{
			this.estado=request==null?"":request.getParameter("estado");
			this.codigoPlantilla=request==null?ConstantesBD.codigoNuncaValido:Utilidades.convertirAEntero(request.getParameter("codigoPlantilla"));
			this.cita=request==null?ConstantesBD.codigoNuncaValidoDouble:Utilidades.convertirADouble(request.getParameter("cita"));
		}
		this.diagnosticosRelacionados=new HashMap<String, Object>();
		this.diagnosticosSeleccionados="";
		this.dtoIngreso = new DtoPlantillasIngresos();
		this.dtoValoraciones=new DtoValoracionesOdonto();
		this.valoracionUrgencias = new DtoValoracionUrgencias();
		this.dtoDiagnosticoOdon=new DtoValDiagnosticosOdo();
		this.listadoDiagnosticosValoracion=new ArrayList<DtoValDiagnosticosOdo>();
		//this.plantilla=new DtoPlantilla();
		//*****ATRIBUTOS PARA EL ODONTOGRAMA*****************************************
		this.infoCompOdont = new InfoOdontograma();
		//***************************************************************************
		//*****ATRIBUTOS PARA EL COMPONENTE DE ANTECEDENTES ODONTOLOGICOS************
		this.infoCompAnteOdont = new InfoAntecedenteOdonto();
		//***************************************************************************
		
		//*****ATRIBUTOS PARA EL COMPONENTE INDICE DE PLACA**************************
		this.compIndicePlaca = new DtoComponenteIndicePlaca();
		//***************************************************************************
		
		// Atributo comunt para toda la plantilla de valoracion odontologica
		this.porConfirmar = ConstantesBD.acronimoNo;
		this.porActualizar=ConstantesBD.acronimoNo;;
		this.dtoAtencionCita=new DtoInfoAtencionCitaOdo();
		this.dtoCita=new DtoCitaOdontologica();
		this.posicionPlantilla=request==null?ConstantesBD.codigoNuncaValido:Utilidades.convertirAEntero(request.getParameter("posicionPlantilla"));
		this.abrirPopUp=ConstantesBD.acronimoNo;
		this.cerrarVentanaConf=ConstantesBD.acronimoNo;
		this.mundoValoracion=new ValoracionOdontologica();
		this.secSeleccionarFormulario=ConstantesBD.acronimoNo;
		
		
		//************ATRIBUTOS PARA LA IMPRESION ***********************************************************++
		this.institucionBasica = new InstitucionBasica();
		this.plantillas = new ArrayList<DtoPlantilla>();
		this.paciente = new DtoPaciente();
		this.usuarioResumen = new UsuarioBasico();
		this.fechaResumen = "";
		this.horaResumen = "";
		//***************************************************************************************************
		this.guardo=true;
		this.mostrarMensaje=new ResultadoBoolean(false);
		this.facturasAutomaticas= new DtoFacturaAutomaticaOdontologica();
	}

	public String getCodigoEvolucionAsociada() {
		return codigoEvolucionAsociada;
	}

	public void setCodigoEvolucionAsociada(String codigoEvolucionAsociada) {
		this.codigoEvolucionAsociada = codigoEvolucionAsociada;
	}

	public String getAncla() {
		return ancla;
	}

	public void setAncla(String ancla) {
		this.ancla = ancla;
	}

	public boolean isGuardo() {
		return guardo;
	}

	public void setGuardo(boolean guardo) {
		this.guardo = guardo;
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
	 * @return the plantilla
	 */
	public DtoPlantilla getPlantilla() {
		return plantilla;
	}

	/**
	 * @param plantilla the plantilla to set
	 */
	public void setPlantilla(DtoPlantilla plantilla) {
		this.plantilla = plantilla;
	}

	/**
	 * @return the codigoFuncionalidad
	 */
	public int getCodigoFuncionalidad() {
		return codigoFuncionalidad;
	}

	/**
	 * @param codigoFuncionalidad the codigoFuncionalidad to set
	 */
	public void setCodigoFuncionalidad(int codigoFuncionalidad) {
		this.codigoFuncionalidad = codigoFuncionalidad;
	}

	/**
	 * @return the viaIngreso
	 */
	public InfoDatosInt getViaIngreso() {
		return viaIngreso;
	}

	/**
	 * @param viaIngreso the viaIngreso to set
	 */
	public void setViaIngreso(InfoDatosInt viaIngreso) {
		this.viaIngreso = viaIngreso;
	}

	public int getCodigoPlantilla() {
		return codigoPlantilla;
	}

	public void setCodigoPlantilla(int codigoPlantilla) {
		this.codigoPlantilla = codigoPlantilla;
	}

	public String getFechaValoracion() {
		return fechaValoracion;
	}

	public void setFechaValoracion(String fechaValoracion) {
		this.fechaValoracion = fechaValoracion;
	}

	public String getHoraConsulta() {
		return horaConsulta;
	}

	public void setHoraConsulta(String horaConsulta) {
		this.horaConsulta = horaConsulta;
	}

	public DtoValoracionesOdonto getDtoValoraciones() {
		return dtoValoraciones;
	}

	public void setDtoValoraciones(DtoValoracionesOdonto dtoValoraciones) {
		this.dtoValoraciones = dtoValoraciones;
	}

	public HashMap<String, Object> getDiagnosticosRelacionados() {
		return diagnosticosRelacionados;
	}

	public void setDiagnosticosRelacionados(
			HashMap<String, Object> diagnosticosRelacionados) {
		this.diagnosticosRelacionados = diagnosticosRelacionados;
	}

	public String getDiagnosticosSeleccionados() {
		return diagnosticosSeleccionados;
	}

	public void setDiagnosticosSeleccionados(String diagnosticosSeleccionados) {
		this.diagnosticosSeleccionados = diagnosticosSeleccionados;
	}

	public boolean isSeccionRevisionSistemas() {
		return seccionRevisionSistemas;
	}

	public void setSeccionRevisionSistemas(boolean seccionRevisionSistemas) {
		this.seccionRevisionSistemas = seccionRevisionSistemas;
	}

	public boolean isSeccionExamenFisico() {
		return seccionExamenFisico;
	}

	public void setSeccionExamenFisico(boolean seccionExamenFisico) {
		this.seccionExamenFisico = seccionExamenFisico;
	}

	public boolean isSeccionEstadoSalida() {
		return seccionEstadoSalida;
	}

	public void setSeccionEstadoSalida(boolean seccionEstadoSalida) {
		this.seccionEstadoSalida = seccionEstadoSalida;
	}

	public boolean isSeccionTipoMonitoreo() {
		return seccionTipoMonitoreo;
	}

	public void setSeccionTipoMonitoreo(boolean seccionTipoMonitoreo) {
		this.seccionTipoMonitoreo = seccionTipoMonitoreo;
	}

	public ArrayList<HashMap<String, Object>> getEstadosConciencia() {
		return estadosConciencia;
	}

	public void setEstadosConciencia(
			ArrayList<HashMap<String, Object>> estadosConciencia) {
		this.estadosConciencia = estadosConciencia;
	}

	public ArrayList<HashMap<String, Object>> getCausasExternas() {
		return causasExternas;
	}

	public void setCausasExternas(ArrayList<HashMap<String, Object>> causasExternas) {
		this.causasExternas = causasExternas;
	}

	public ArrayList<HashMap<String, Object>> getFinalidades() {
		return finalidades;
	}

	public void setFinalidades(ArrayList<HashMap<String, Object>> finalidades) {
		this.finalidades = finalidades;
	}

	public ArrayList<HashMap<String, Object>> getConductasValoracion() {
		return conductasValoracion;
	}

	public void setConductasValoracion(
			ArrayList<HashMap<String, Object>> conductasValoracion) {
		this.conductasValoracion = conductasValoracion;
	}

	public ArrayList<HashMap<String, Object>> getTiposDiagnostico() {
		return tiposDiagnostico;
	}

	public void setTiposDiagnostico(
			ArrayList<HashMap<String, Object>> tiposDiagnostico) {
		this.tiposDiagnostico = tiposDiagnostico;
	}

	public ArrayList<HashMap<String, Object>> getTiposMonitoreo() {
		return tiposMonitoreo;
	}

	public void setTiposMonitoreo(ArrayList<HashMap<String, Object>> tiposMonitoreo) {
		this.tiposMonitoreo = tiposMonitoreo;
	}

	public ArrayList<HashMap<String, Object>> getRangosEdadMenarquia() {
		return rangosEdadMenarquia;
	}

	public void setRangosEdadMenarquia(
			ArrayList<HashMap<String, Object>> rangosEdadMenarquia) {
		this.rangosEdadMenarquia = rangosEdadMenarquia;
	}

	public ArrayList<HashMap<String, Object>> getRangosEdadMenopausia() {
		return rangosEdadMenopausia;
	}

	public void setRangosEdadMenopausia(
			ArrayList<HashMap<String, Object>> rangosEdadMenopausia) {
		this.rangosEdadMenopausia = rangosEdadMenopausia;
	}

	public ArrayList<HashMap<String, Object>> getConceptosMenstruacion() {
		return conceptosMenstruacion;
	}

	public void setConceptosMenstruacion(
			ArrayList<HashMap<String, Object>> conceptosMenstruacion) {
		this.conceptosMenstruacion = conceptosMenstruacion;
	}
	
	/**
	 * Mï¿½todo para asignar el nï¿½mero de dx relacionados
	 * @param numRegistros
	 */
	public void setNumDiagRelacionados(int numRegistros)
	{
		this.diagnosticosRelacionados.put("numRegistros",numRegistros);
	}
	
	/**
	 * Mï¿½todo para obtener el nï¿½mero de diagnï¿½sticos relacionados
	 * @return
	 */
	public int getNumDiagRelacionados()
	{
		return Utilidades.convertirAEntero(this.getDiagnosticosRelacionados("numRegistros")+"", true);
	}
	
	/**
	 * @return the diagnosticosRelacionados
	 */
	public Object getDiagnosticosRelacionados(String key) {
		return diagnosticosRelacionados.get(key);
	}

	public DtoValoracionConsulta getValoracionConsulta() {
		return valoracionConsulta;
	}

	public void setValoracionConsulta(DtoValoracionConsulta valoracionConsulta) {
		this.valoracionConsulta = valoracionConsulta;
	}

	public DtoValoracionUrgencias getValoracionUrgencias() {
		return valoracionUrgencias;
	}

	public void setValoracionUrgencias(DtoValoracionUrgencias valoracionUrgencias) {
		this.valoracionUrgencias = valoracionUrgencias;
	}

	public double getCita() {
		return cita;
	}

	public void setCita(double cita) {
		this.cita = cita;
	}

	

	public DtoValDiagnosticosOdo getDtoDiagnosticoOdon() {
		return dtoDiagnosticoOdon;
	}

	public void setDtoDiagnosticoOdon(DtoValDiagnosticosOdo dtoDiagnosticoOdon) {
		this.dtoDiagnosticoOdon = dtoDiagnosticoOdon;
	}

	public InfoOdontograma getInfoCompOdont() {
		return infoCompOdont;
	}

	public void setInfoCompOdont(InfoOdontograma infoCompOdont) {
		this.infoCompOdont = infoCompOdont;
	}

	public String getEstadoSecundario() {
		return estadoSecundario;
	}

	public void setEstadoSecundario(String estadoSecundario) {
		this.estadoSecundario = estadoSecundario;
	}

	public int getIngreso() {
		return ingreso;
	}

	public void setIngreso(int ingreso) {
		this.ingreso = ingreso;
	}

	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}

	public void setNumeroSolicitud(int numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}
	
	public String getXmlOdontograma() {
		return this.infoCompOdont.getXmlOdontograma();
	}

	public void setXmlOdontograma(String xml) {
		this.infoCompOdont.setXmlOdontograma(xml);
	}
	
	public void setPosOdonto(String value)
	{
		this.infoCompOdont.setStringPosPosiciones(value);
	}
	
	public String getPosOdonto()
	{
		return this.infoCompOdont.getStringPosPosiciones(); 
	}

	/**
	 * @return the infoCompAnteOdont
	 */
	public InfoAntecedenteOdonto getInfoCompAnteOdont() {
		return infoCompAnteOdont;
	}

	/**
	 * @param infoCompAnteOdont the infoCompAnteOdont to set
	 */
	public void setInfoCompAnteOdont(InfoAntecedenteOdonto infoCompAnteOdont) {
		this.infoCompAnteOdont = infoCompAnteOdont;
	}
	
	public int getPosTratamientoExterno()
	{
		return valoracionUrgencias.getAntecedenteOdontologico().getPosTratamientoExterno();
	}
	
	public void setPosTratamientoExterno(int posTratamientoExt)
	{
		this.valoracionUrgencias.getAntecedenteOdontologico().setPosTratamientoExterno(posTratamientoExt);
	}
	
	public ArrayList<DtoValDiagnosticosOdo> getListadoDiagnosticosValoracion() {
		return listadoDiagnosticosValoracion;
	}

	public void setListadoDiagnosticosValoracion(
			ArrayList<DtoValDiagnosticosOdo> listadoDiagnosticosValoracion) {
		this.listadoDiagnosticosValoracion = listadoDiagnosticosValoracion;
	}

	/**
	 * @return the porConfirmar
	 */
	public String getPorConfirmar() {
		return porConfirmar;
	}

	/**
	 * @param porConfirmar the porConfirmar to set
	 */
	public void setPorConfirmar(String porConfirmar) {
		this.porConfirmar = porConfirmar;
	}

	public String getPorActualizar() {
		return porActualizar;
	}

	public void setPorActualizar(String porActualizar) {
		this.porActualizar = porActualizar;
	}

	/**
	 * @return the dtoIngreso
	 */
	public DtoPlantillasIngresos getDtoIngreso() {
		return dtoIngreso;
	}

	/**
	 * @param dtoIngreso the dtoIngreso to set
	 */
	public void setDtoIngreso(DtoPlantillasIngresos dtoIngreso) {
		this.dtoIngreso = dtoIngreso;
	}

	/**
	 * @return the compIndicePlaca
	 */
	public DtoComponenteIndicePlaca getCompIndicePlaca() {
		return compIndicePlaca;
	}

	/**
	 * @param compIndicePlaca the compIndicePlaca to set
	 */
	public void setCompIndicePlaca(DtoComponenteIndicePlaca compIndicePlaca) {
		this.compIndicePlaca = compIndicePlaca;
	}

	public DtoInfoAtencionCitaOdo getDtoAtencionCita() {
		return dtoAtencionCita;
	}

	public void setDtoAtencionCita(DtoInfoAtencionCitaOdo dtoAtencionCita) {
		this.dtoAtencionCita = dtoAtencionCita;
	}

	public DtoCitaOdontologica getDtoCita() {
		return dtoCita;
	}

	public void setDtoCita(DtoCitaOdontologica dtoCita) {
		this.dtoCita = dtoCita;
	}

	public int getPosicionPlantilla() {
		return posicionPlantilla;
	}

	public void setPosicionPlantilla(int posicionPlantilla) {
		this.posicionPlantilla = posicionPlantilla;
	}

	public String getAbrirPopUp() {
		return abrirPopUp;
	}

	public void setAbrirPopUp(String abrirPopUp) {
		this.abrirPopUp = abrirPopUp;
	}

	public String getCerrarVentanaConf() {
		return cerrarVentanaConf;
	}

	public void setCerrarVentanaConf(String cerrarVentanaConf) {
		this.cerrarVentanaConf = cerrarVentanaConf;
	}

	public ValoracionOdontologica getMundoValoracion() {
		return mundoValoracion;
	}

	public void setMundoValoracion(ValoracionOdontologica mundoValoracion) {
		this.mundoValoracion = mundoValoracion;
	}

	public String getSecSeleccionarFormulario() {
		return secSeleccionarFormulario;
	}

	public void setSecSeleccionarFormulario(String secSeleccionarFormulario) {
		this.secSeleccionarFormulario = secSeleccionarFormulario;
	}

	

	/**
	 * @return the institucionBasica
	 */
	public InstitucionBasica getInstitucionBasica() {
		return institucionBasica;
	}

	/**
	 * @param institucionBasica the institucionBasica to set
	 */
	public void setInstitucionBasica(InstitucionBasica institucionBasica) {
		this.institucionBasica = institucionBasica;
	}

	/**
	 * @return the plantillas
	 */
	public ArrayList<DtoPlantilla> getPlantillas() {
		return plantillas;
	}

	/**
	 * @param plantillas the plantillas to set
	 */
	public void setPlantillas(ArrayList<DtoPlantilla> plantillas) {
		this.plantillas = plantillas;
	}

	/**
	 * @return the paciente
	 */
	public DtoPaciente getPaciente() {
		return paciente;
	}

	/**
	 * @param paciente the paciente to set
	 */
	public void setPaciente(DtoPaciente paciente) {
		this.paciente = paciente;
	}

	/**
	 * @return the usuarioResumen
	 */
	public UsuarioBasico getUsuarioResumen() {
		return usuarioResumen;
	}

	/**
	 * @param usuarioResumen the usuarioResumen to set
	 */
	public void setUsuarioResumen(UsuarioBasico usuarioResumen) {
		this.usuarioResumen = usuarioResumen;
	}

	/**
	 * @return the fechaResumen
	 */
	public String getFechaResumen() {
		return fechaResumen;
	}

	/**
	 * @param fechaResumen the fechaResumen to set
	 */
	public void setFechaResumen(String fechaResumen) {
		this.fechaResumen = fechaResumen;
	}

	/**
	 * @return the horaResumen
	 */
	public String getHoraResumen() {
		return horaResumen;
	}

	/**
	 * @param horaResumen the horaResumen to set
	 */
	public void setHoraResumen(String horaResumen) {
		this.horaResumen = horaResumen;
	}

	/**
	 * @return the facturasAutomaticas
	 */
	public DtoFacturaAutomaticaOdontologica getFacturasAutomaticas() {
		return facturasAutomaticas;
	}

	/**
	 * @param facturasAutomaticas the facturasAutomaticas to set
	 */
	public void setFacturasAutomaticas(
			DtoFacturaAutomaticaOdontologica facturasAutomaticas) {
		this.facturasAutomaticas = facturasAutomaticas;
	}


	/**
	 * Método que almacena el valor del atributo fechaNacimientoPacienteFormateado
	 * @param fechaNacimientoPacienteFormateado
	 */
	public void setFechaNacimientoPacienteFormateado(
			String fechaNacimientoPacienteFormateado) {
		this.fechaNacimientoPacienteFormateado = fechaNacimientoPacienteFormateado;
	}
	
	/**
	 * Método que devuelve la fecha de nacimiento del paciente
	 * en formato mes completo
	 * @return fechaNacimientoPacienteFormateado
	 */
	public String getFechaNacimientoPacienteFormateado() {
		if(this.paciente!=null)
		{
			if(this.paciente.getFechaNacimiento()!=null){
				fechaNacimientoPacienteFormateado=UtilidadFecha.conversionFormatoFechaDescripcionMesCompleto(this.paciente.getFechaNacimiento());
			}else
				fechaNacimientoPacienteFormateado="";
		}
		else
			fechaNacimientoPacienteFormateado="";
		return fechaNacimientoPacienteFormateado;
	}

	/**
	 * Método que almacena el valor del atributo titulo
	 * @param titulo
	 */
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	/**
	 * Método que devuelve el titulo que debe llevar el reporte en PDF
	 * y debe ser descrito como parámetro
	 * @return titulo
	 */
	public String getTitulo() {
		return titulo;
	}

	/**
	 * Método que almacena el valor del atributo tipoCitaFormateado
	 * @param tipoCitaFormateado
	 */
	public void setTipoCitaFormateado(String tipoCitaFormateado) {
		this.tipoCitaFormateado = tipoCitaFormateado;
	}

	/**
	 * Método que devuelve el tipo de cita como nombre completo
	 * utilizado en el reporte como subtitulo
	 * @return tipoCitaFormateado
	 */
	public String getTipoCitaFormateado() {
		
	  if(!UtilidadTexto.isEmpty(this.dtoCita.getTipo()))
		{
			if(this.dtoCita.getTipo().equals(ConstantesIntegridadDominio.acronimoAuditoria)){
				this.tipoCitaFormateado="AUDITORÍA";
			}else
			if(this.dtoCita.getTipo().equals(ConstantesIntegridadDominio.acronimoPrioritaria)){
				this.tipoCitaFormateado="ATENCIÓN PRIORITARIA";
			}else
			if(this.dtoCita.getTipo().equals(ConstantesIntegridadDominio.acronimoControlCitaOdon)){
				this.tipoCitaFormateado="CONTROL";	
			}else
			if(this.dtoCita.getTipo().equals(ConstantesIntegridadDominio.acronimoRemisionInterconsulta)){
					this.tipoCitaFormateado="INTERCONSULTA";	
			}else
			if(this.dtoCita.getTipo().equals(ConstantesIntegridadDominio.acronimoTipoAtencionTratamiento)){
					this.tipoCitaFormateado="TRATAMIENTO";	
			}else
			if(this.dtoCita.getTipo().equals(ConstantesIntegridadDominio.acronimoRevaloracion)){
					this.tipoCitaFormateado="REVALORACIÓN";	
			}else
			if(this.dtoCita.getTipo().equals(ConstantesIntegridadDominio.acronimoTipoCitaOdonValoracionInicial)){
					this.tipoCitaFormateado="VALORACIÓN INICIAL";	
			}
			
			
		}

		return tipoCitaFormateado;
	}

	/**
	 * Método que almacena el valor del atributo plantillaBase
	 * @param plantillaBase
	 */
	public void setPlantillaBase(String plantillaBase) {
		
		this.plantillaBase = plantillaBase;
	}

	/**
	 * Método que devuelve el nombre de la plantilla base 
	 * @return plantillaBase
	 */
	public String getPlantillaBase() {
		this.plantillaBase=this.plantillaBase.toUpperCase(new Locale("ES"));
		return plantillaBase;
	}

	/**
	 * Método que almacena el valor del atributo enumTipoSalida
	 * @param enumTipoSalida
	 */
	public void setEnumTipoSalida(EnumTiposSalida enumTipoSalida) {
		this.enumTipoSalida = enumTipoSalida;
	}

	/**
	 * Método que devuelve el tipo de salida que en este caso será PDF 
	 * @return enumTipoSalida
	 */
	public EnumTiposSalida getEnumTipoSalida() {
		return enumTipoSalida;
	}

	/**
	 * Método que almacena el valor del atributo nombreArchivoGenerado
	 * @param nombreArchivoGenerado
	 */
	public void setNombreArchivoGenerado(String nombreArchivoGenerado) {
		this.nombreArchivoGenerado = nombreArchivoGenerado;
	}

	/**
	 * Método que devuelve el nombre del archivo generado en la
	 * generación del PDF
	 * @return nombreArchivoGenerado
	 */
	public String getNombreArchivoGenerado() {
		return nombreArchivoGenerado;
	}

	/**
	 * Método que almacena el valor del atributo nombreContexto
	 * @param nombreContexto
	 */
	public void setNombreContexto(String nombreContexto) {
		this.nombreContexto = nombreContexto;
	}

	/**
	 * Método que devuelve el nombre del contexto para
	 * obtener la ruta hacia las imagenes de odontograma e indice 
	 * de placa utilizadas en el reporte
	 * @return nombreContexto
	 */
	public String getNombreContexto() {
		return nombreContexto;
	}

	public void setRedireccion(String redireccion) {
		this.redireccion = redireccion;
	}

	public String getRedireccion() {
		return redireccion;
	}

	
}