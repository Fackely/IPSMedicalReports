package util.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;

import com.princetonsa.dto.odontologia.DtoHallazgoOdontologico;
import com.princetonsa.dto.odontologia.DtoInfoFechaUsuario;
import com.princetonsa.dto.odontologia.DtoLogPlanTratamiento;
import com.princetonsa.dto.odontologia.DtoLogProgServPlant;
import com.princetonsa.dto.odontologia.DtoMotivosAtencion;
import com.princetonsa.dto.odontologia.DtoPlanTratamientoOdo;
import com.princetonsa.dto.odontologia.DtoSectorSuperficieCuadrante;
import com.princetonsa.mundo.odontologia.ComponenteOdontograma;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosStr;
import util.InfoDatosString;
import util.UtilidadFecha;
import util.Utilidades;

@SuppressWarnings("serial")
public class InfoOdontograma implements Serializable 
{
	//Atributos para el odontograma en flash

	/**
	 * Versión serial
	 */
	private String xmlOdontograma;
	private String xmlHallazgoDiente;
	private String xmlHallazgoSuperficie;
	private String activoDienteAdulto;
	private String activoDienteNino;
	private String pathNombreContexo;

	//Atributos para el manejo del componente en las funcionalidades
	private int institucion;
	private String nombreAtributo;
	private String nombreForm;
	private String pathAction;
	private String seccionAplica;
	private boolean llamadoSeccionCita;
	private String contenedor;
 	
	private int codigoPaciente;
	private int edadPaciente;
	private int codigoMedico;
	private int especialidad;
	private int idIngresoPaciente;
	private int codigoEvolucion;
	private int codigoValoracion;
	private int codigoCentroAtencion;
	private int codigoCita;
	private DtoInfoFechaUsuario usuarioActual;
	private String esResumen;
	private String esImpresion;
	
	private BigDecimal indicadorAuxBd;
	private int indicador1;
	private int indicador2;
	private int indicador3;
	private int indicador4;
	private int indicador5; 
	private String newEstadoProg;
	private String estadosServicios;
	private InfoDatosStr mostrarMensajeProceExito;
	
	//Atributos de Estructura
	private InfoPlanTratamiento infoPlanTrata;
	private DtoPlanTratamientoOdo dtoInfoPlanTratamiento;
	private ArrayList<DtoSectorSuperficieCuadrante> arraySuperficies;
	private ArrayList<InfoDatosString> arrayDientesUsados;
	//Listar los hallazgos de diente parametrizados
	private ArrayList<DtoHallazgoOdontologico> arrayHallazgosDiente;
	//Listar los hallazgos de superfice parametrizados
	private ArrayList<DtoHallazgoOdontologico> arrayHallazgosSuperficie;
	//Listar los hallazgos de boca parametrizados
	private ArrayList<DtoHallazgoOdontologico> arrayHallazgosBoca;
	
	private InfoDetallePlanTramiento nuevaInclusion;
	
	
	
	//Listado Programas parametrizados (busqueda servicios)
	private ArrayList<InfoProgramaServicioPlan> arrayProgServiPlanT;
	private ArrayList<DtoLogPlanTratamiento> arrayLogPlanTratamiento;
	private ArrayList<DtoLogProgServPlant> arrayLogProgramaServicioPlan;
	
	//************************** ESTADOS POSIBLES A CAMBIAR *************************
	/**
	 *Listado de estados posibles a cambiar según el estado actual del servicio 
	 */
	private ArrayList<InfoDatosString> arrayPosiblesEstados;
	
	//************************* Listado de Motivos de Cancelacion
	private ArrayList<DtoMotivosAtencion> arrayMotivoCancelacion;
		
	//Atributos de valores por defecto
	private String esBuscarPorPrograma;
	
	//Atributos del odontograma
	private String estadoCompOdontograma;
	private String indicadorPlantTratamiento;
	private String indicadorOdontograma;
	private boolean soloModifInfoNueva;
	private boolean mostrarSoloOdontograma;
	
	private String idProgramaServicioLog;
	private int tipoHistorico;
	
	//Lista de Servicios Seleccionados para la proxima Cita
	private ArrayList<InfoServicios> arrayServProxCita;
	private int posServProxCita;
	
	//Atributos para la busqueda de Programas Servicios
	private InfoDatosStr infoGeneral;
	private String fechaProxCita;
	
	// Otras Evoluciones
	private String utilizaProgOdontIns;
	private String validaPresuOdontCont;
	private String instRegistraAtenExt;

	//Atributo usado para saber si se debe evaluar el estado del plan de tratamiento al cargar odontograma
	private boolean evaluarEstadosPlanTratamiento;
	
	//Atributo para saber si al evolucionar el plan de tratamiento como debe ir el atributo por confirmar
	private String porConfirmar;
	
	/**
	 * Control de las superficies que se seleccionan en el popup para
	 * asociar programas de N superficies
	 */
	private int[] superficiesSeleccionadasXPrograma;
	
	/**
	 * &Iacute;ndice del programa seleccionado para adicionarlo al hallazgo
	 */
	private int indiceProgramaSeleccionado;
	
	/**
	 * &Iacute;ndice de la superficie seleccionada al momento de adicionarle los programas
	 */
	private int indiceSuperficieSeleccionada;
	
	/**
	 * C&oacute;digo del programa seleccionado para adicionarlo al hallazgo
	 */
	private int codigoProgramaSeleccionado;
	
	/**
	 * &Iacute;ndice de la pieza seleccionada (Se usa principalmente en otros hallazgos)
	 */
	private int indicePiezaSeleccionada;

	/**
	 * Superficie seleccionada en la secci&oacite;n otros
	 */
	private int superficieSeleccionada;

	/**
	 * Indica si se seleccion&oacute; un equivalente
	 */
	private boolean equivalente;
	
	/**
	 * C&oacute;digo del centro de costo del usuario
	 */
	private int centroCosto;
	
	/**
	 * Campo que contiene los mensajes informativos.
	 */
	private String mensajeInformativo;

	/**
	 * Utilizado para generar códigos secuenciales para las relaciones programa hallazgo pieza
	 * con el fín de que no se crucen los programas entre si, este código nunca se almacena
	 * en base de datos.
	 */
	private int contadorCodigoTemporalProgramaHallazgoPieza;
	
	public InfoOdontograma()
	{
		reset();
	}
	
	public void reset()
	{
		infoPlanTrata = new InfoPlanTratamiento();
		xmlOdontograma = "";
		estadoCompOdontograma = "";
		pathAction = "";
		nombreForm = "";
		nombreAtributo = "";
		seccionAplica = ""; 
		contenedor = "";
		
		this.xmlHallazgoDiente = "";
		this.xmlHallazgoSuperficie = "";
		this.institucion = ConstantesBD.codigoNuncaValido;
		this.activoDienteAdulto = ConstantesBD.acronimoSi;
		this.activoDienteNino = ConstantesBD.acronimoSi;
		this.edadPaciente = ConstantesBD.codigoNuncaValido;
		this.idIngresoPaciente = ConstantesBD.codigoNuncaValido;
		this.dtoInfoPlanTratamiento = new DtoPlanTratamientoOdo();
		this.esBuscarPorPrograma = ConstantesBD.acronimoSi; 
		this.codigoPaciente = ConstantesBD.codigoNuncaValido;
		this.especialidad = ConstantesBD.codigoNuncaValido;
		this.usuarioActual = new DtoInfoFechaUsuario();
		this.codigoEvolucion = ConstantesBD.codigoNuncaValido;
		this.codigoValoracion = ConstantesBD.codigoNuncaValido;
		this.indicadorPlantTratamiento = "";
		this.codigoCentroAtencion = ConstantesBD.codigoNuncaValido;
		this.indicadorOdontograma = "";
		this.codigoMedico = ConstantesBD.codigoNuncaValido;
		this.codigoCita = ConstantesBD.codigoNuncaValido;
		this.arraySuperficies = null;
		this.llamadoSeccionCita = false;
		
		this.indicador1 = ConstantesBD.codigoNuncaValido;
		this.indicador2 = ConstantesBD.codigoNuncaValido;
		this.indicador3 = ConstantesBD.codigoNuncaValido;
		this.indicador4 = ConstantesBD.codigoNuncaValido;
		this.indicador5 = ConstantesBD.codigoNuncaValido;
		this.newEstadoProg = "";
		this.estadosServicios = "";
		
		this.idProgramaServicioLog= "";
		this.tipoHistorico=ConstantesBD.codigoNuncaValido;
		this.arrayDientesUsados = null;	
		
		this.arrayHallazgosDiente = null;
		this.arrayHallazgosSuperficie = null;
		this.arrayHallazgosBoca = null;
		this.arrayProgServiPlanT = null;
		this.arrayLogPlanTratamiento = null;
		this.arrayLogProgramaServicioPlan = null;
		this.arrayServProxCita = null;
		
		// Inclusiones
		this.nuevaInclusion= new InfoDetallePlanTramiento();
		
		
		this.posServProxCita= ConstantesBD.codigoNuncaValido;
		
		
		this.infoGeneral = new InfoDatosStr();
		
		this.pathNombreContexo = "";
		
		// Otras Evoluciones
		this.utilizaProgOdontIns = ConstantesBD.acronimoNo;
		this.validaPresuOdontCont = ConstantesBD.acronimoNo;
		this.instRegistraAtenExt = ConstantesBD.acronimoNo;
		this.arrayPosiblesEstados = null;
		
		//************************* Listado de Motivos de Cancelacion ******************************
		this.arrayMotivoCancelacion = null;
		
		this.evaluarEstadosPlanTratamiento = true;
		this.esImpresion = ConstantesBD.acronimoNo;
		this.esResumen = ConstantesBD.acronimoNo;
		
		
		this.fechaProxCita= UtilidadFecha.getFechaActual();
		this.soloModifInfoNueva = false;
		this.mostrarSoloOdontograma = false;
		this.indicadorAuxBd = new BigDecimal(ConstantesBD.codigoNuncaValido);
		this.mostrarMensajeProceExito = new InfoDatosStr();
		
		this.porConfirmar = "";
		
		this.superficiesSeleccionadasXPrograma=null;
		this.indiceProgramaSeleccionado=ConstantesBD.codigoNuncaValido;
		this.indiceSuperficieSeleccionada=ConstantesBD.codigoNuncaValido;
		this.codigoProgramaSeleccionado=ConstantesBD.codigoNuncaValido;
		this.indicePiezaSeleccionada=ConstantesBD.codigoNuncaValido;
		this.superficieSeleccionada=ConstantesBD.codigoNuncaValido;
		this.equivalente=false;
		
		this.setMensajeInformativo(null);
	}

	public void resetServiciosProxCita()
	{
		 this.arrayServProxCita = null;
	}
	
	public InfoPlanTratamiento getInfoPlanTrata() {
		return infoPlanTrata;
	}

	public void setInfoPlanTrata(InfoPlanTratamiento planTratamiento) {
		this.infoPlanTrata = planTratamiento;
	}

	public String getXmlOdontograma() {
		return xmlOdontograma;
	}

	public void setXmlOdontograma(String xmlOdontograma) {
		this.xmlOdontograma = xmlOdontograma;
	}

	public String getEstadoCompOdontograma() {
		return estadoCompOdontograma;
	}

	public void setEstadoCompOdontograma(String estadoCompOdontograma) {
		this.estadoCompOdontograma = estadoCompOdontograma;
	}

	public String getNombreAtributo() {
		return nombreAtributo;
	}

	public void setNombreAtributo(String nombreAtributo) {
		this.nombreAtributo = nombreAtributo;
	}

	public String getNombreForm() {
		return nombreForm;
	}

	public void setNombreForm(String nombreForm) {
		this.nombreForm = nombreForm;
	}

	public String getPathAction() {
		return pathAction;
	}

	public void setPathAction(String pathAction) {
		this.pathAction = pathAction;
	}

	public String getXmlHallazgoDiente() {
		return xmlHallazgoDiente;
	}

	public void setXmlHallazgoDiente(String xmlHallazgoDiente) {
		this.xmlHallazgoDiente = xmlHallazgoDiente;
	}

	public String getXmlHallazgoSuperficie() {
		return xmlHallazgoSuperficie;
	}

	public void setXmlHallazgoSuperficie(String xmlHallazgoSuperficie) {
		this.xmlHallazgoSuperficie = xmlHallazgoSuperficie;
	}

	public int getInstitucion() {
		return institucion;
	}

	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	public String getActivoDienteAdulto() {
		return activoDienteAdulto;
	}

	public void setActivoDienteAdulto(String activoDienteAdulto) {
		this.activoDienteAdulto = activoDienteAdulto;
	}

	public String getActivoDienteNino() {
		return activoDienteNino;
	}

	public void setActivoDienteNino(String activoDienteNino) {
		this.activoDienteNino = activoDienteNino;
	}

	public int getEdadPaciente() {
		return edadPaciente;
	}

	public void setEdadPaciente(int edadPaciente) {
		this.edadPaciente = edadPaciente;
	}

	public int getIdIngresoPaciente() {
		return idIngresoPaciente;
	}

	public void setIdIngresoPaciente(int idIngresoPaciente) {
		this.idIngresoPaciente = idIngresoPaciente;
	}

	public DtoPlanTratamientoOdo getDtoInfoPlanTratamiento() {
		return dtoInfoPlanTratamiento;
	}

	public void setDtoInfoPlanTratamiento(
			DtoPlanTratamientoOdo dtoInfoPlanTratamiento) {
		this.dtoInfoPlanTratamiento = dtoInfoPlanTratamiento;
	}

	public String getEsBuscarPorPrograma() {
		return esBuscarPorPrograma;
	}

	public void setEsBuscarPorPrograma(String esBuscarPorPrograma) {
		this.esBuscarPorPrograma = esBuscarPorPrograma;
	}

	public int getCodigoPaciente() {
		return codigoPaciente;
	}

	public void setCodigoPaciente(int codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}

	public int getEspecialidad() {
		return especialidad;
	}

	public void setEspecialidad(int especialidad) {
		this.especialidad = especialidad;
	}

	public DtoInfoFechaUsuario getUsuarioActual() {
		return usuarioActual;
	}

	public void setUsuarioActual(DtoInfoFechaUsuario usuarioActual) {
		this.usuarioActual = usuarioActual;
	}

	public int getCodigoEvolucion() {
		return codigoEvolucion;
	}

	public void setCodigoEvolucion(int codigoEvolucion) {
		this.codigoEvolucion = codigoEvolucion;
	}

	public int getCodigoValoracion() {
		return codigoValoracion;
	}

	public void setCodigoValoracion(int codigoValoracion) {
		this.codigoValoracion = codigoValoracion;
	}

	public String getIndicadorPlantTratamiento() {
		return indicadorPlantTratamiento;
	}

	public void setIndicadorPlantTratamiento(String indicadorPlantTratamiento) {
		this.indicadorPlantTratamiento = indicadorPlantTratamiento;
	}

	public int getCodigoCentroAtencion() {
		return codigoCentroAtencion;
	}

	public void setCodigoCentroAtencion(int codigoCentroAtencion) {
		this.codigoCentroAtencion = codigoCentroAtencion;
	}

	public String getIndicadorOdontograma() {
		return indicadorOdontograma;
	}

	public void setIndicadorOdontograma(String indicadorOdontograma) {
		this.indicadorOdontograma = indicadorOdontograma;
	}

	public int getCodigoMedico() {
		return codigoMedico;
	}

	public void setCodigoMedico(int codigoMedico) {
		this.codigoMedico = codigoMedico;
	}

	public int getCodigoCita() {
		return codigoCita;
	}

	public void setCodigoCita(int codigoCita) {
		this.codigoCita = codigoCita;
	}

	public ArrayList<DtoSectorSuperficieCuadrante> getArraySuperficies() {
		if(arraySuperficies == null)
		{
			arraySuperficies = new ArrayList<DtoSectorSuperficieCuadrante>();
		}
		return arraySuperficies;
	}

	public void setArraySuperficies(ArrayList<DtoSectorSuperficieCuadrante> arraySuperficies) {
		this.arraySuperficies = arraySuperficies;
	}
	
	/**
	 * Busca el c&oacute;digo de la superficie dependiendo de el sector y la pieza.
	 * @param sector Entero que indica el sector al que se le seleccon&oacute; el hallazgo.
	 * @param pieza Entero que indica la pieza dental seleccionada.
	 * @return Entero con el c&oacite;digo en BD de la superficie seleccionada.
	 */
	public int getCodigoSuperficePorSector(int sector, int pieza)
	{
		for(DtoSectorSuperficieCuadrante info:this.getArraySuperficies())
		{
			if(info.getSector() == sector && pieza==info.getPieza())
			{
				return (new Double(info.getSuperficie().getCodigo())).intValue();
			}
		}
		
		return ConstantesBD.codigoNuncaValido;
	}

	/**
	 * Consulta el nombre de la superficie dental seg&uacute;n el codigo de la superficie y la pieza dental
	 * @param codigoSuperficie C&oacute;digo de la superficie (Tabla historiaclinica.superficie_dental)
	 * @param pieza C&oacute;digo de la pieza
	 * @return Nombre de la superficie dental, null si no la encuentra
	 */
	public String getNombreSuperficie(int codigoSuperficie, int pieza)
	{
		for(DtoSectorSuperficieCuadrante info:this.getArraySuperficies())
		{
			if(info.getSuperficie().getCodigo() == codigoSuperficie && pieza==info.getPieza())
			{
				return info.getSuperficie().getNombre();
			}
		}
		
		return null;
	}

	/**
	 * Asigna cada uno de los indicadores dependiendo de los parámetros
	 * @param valor {@link String} Cadena con la informaci&oacute;n de cada uno de los servicios
	 * debe tener el siguiente formato
	 * 0@@@@@0@@@@@0@@@@@0@@@@@0
	 * Elems:
	 * Indicador 1  --> Pieza
	 * Indicador 2  --> Superficie
	 * Indicador 3  --> &Iacute;ndice del programa
	 * Indicador 4  --> resultValEvoOrd[0] --> El que lo adivine me dice porfa
	 * Indicador 5  --> resultValEvoOrd[1] --> El que lo adivine me dice porfa
	 */
	public void setStringPosPosiciones(String valor)
	{	
		String cadena []= valor.split(ConstantesBD.separadorSplit);
		this.indicador1 = ConstantesBD.codigoNuncaValido;
		this.indicador2 = ConstantesBD.codigoNuncaValido;
		this.indicador3 = ConstantesBD.codigoNuncaValido;
		this.indicador4 = ConstantesBD.codigoNuncaValido;
		this.indicador5 = ConstantesBD.codigoNuncaValido;
		
		if(cadena.length == 1 ){
			this.indicador1 = Utilidades.convertirAEntero(cadena[0]);
			this.indicador2 = ConstantesBD.codigoNuncaValido;
			this.indicador3 = ConstantesBD.codigoNuncaValido;
			this.indicador4 = ConstantesBD.codigoNuncaValido;
			this.indicador5 = ConstantesBD.codigoNuncaValido;
		}else if(cadena.length == 2 )
		{
			this.indicador1 = Utilidades.convertirAEntero(cadena[0]);
			this.indicador2 = Utilidades.convertirAEntero(cadena[1]);
			this.indicador3 = ConstantesBD.codigoNuncaValido;
			this.indicador4 = ConstantesBD.codigoNuncaValido;
			this.indicador5 = ConstantesBD.codigoNuncaValido;
		}
		else if(cadena.length == 3 )
		{
			this.indicador1 = Utilidades.convertirAEntero(cadena[0]);
			this.indicador2 = Utilidades.convertirAEntero(cadena[1]);
			this.indicador3 = Utilidades.convertirAEntero(cadena[2]);
			this.indicador4 = ConstantesBD.codigoNuncaValido;
			this.indicador5 = ConstantesBD.codigoNuncaValido;
		}
		else if(cadena.length == 4 )
		{
			this.indicador1 = Utilidades.convertirAEntero(cadena[0]);
			this.indicador2 = Utilidades.convertirAEntero(cadena[1]);
			this.indicador3 = Utilidades.convertirAEntero(cadena[2]);
			this.indicador4 = Utilidades.convertirAEntero(cadena[3]);
			this.indicador5 = ConstantesBD.codigoNuncaValido;
		}
		else if(cadena.length == 5 )
		{
			this.indicador1 = Utilidades.convertirAEntero(cadena[0]);
			this.indicador2 = Utilidades.convertirAEntero(cadena[1]);
			this.indicador3 = Utilidades.convertirAEntero(cadena[2]);
			this.indicador4 = Utilidades.convertirAEntero(cadena[3]);
			this.indicador5 = Utilidades.convertirAEntero(cadena[4]);
		}
	}
	
	public String getStringPosPosiciones()
	{
		return this.indicador1+ConstantesBD.separadorSplit+this.indicador2+ConstantesBD.separadorSplit+this.indicador3+ConstantesBD.separadorSplit+this.indicador4+ConstantesBD.separadorSplit+this.indicador5;
	}

	public ArrayList<InfoDatosString> getArrayDientesUsados() {
		if(arrayDientesUsados == null)
		{
			arrayDientesUsados = new ArrayList<InfoDatosString>();
		}
		return arrayDientesUsados;
	}

	public void setArrayDientesUsados(ArrayList<InfoDatosString> arrayDientesUsados) {
		this.arrayDientesUsados = arrayDientesUsados;
	}

	public ArrayList<DtoHallazgoOdontologico> getArrayHallazgosDiente() {
		if(arrayHallazgosDiente == null)
		{
			arrayHallazgosDiente = new ArrayList<DtoHallazgoOdontologico>();
		}
		return arrayHallazgosDiente;
	}

	public void setArrayHallazgosDiente(
			ArrayList<DtoHallazgoOdontologico> arrayHallazgosDiente) {
		this.arrayHallazgosDiente = arrayHallazgosDiente;
	}

	public ArrayList<DtoHallazgoOdontologico> getArrayHallazgosSuperficie() {
		if(arrayHallazgosSuperficie == null)
		{
			arrayHallazgosSuperficie = new ArrayList<DtoHallazgoOdontologico>();
		}
		return arrayHallazgosSuperficie;
	}

	public void setArrayHallazgosSuperficie(
			ArrayList<DtoHallazgoOdontologico> arrayHallazgosSuperficie) {
		this.arrayHallazgosSuperficie = arrayHallazgosSuperficie;
	}

	public int getIndicador1() {
		return indicador1;
	}

	public void setIndicador1(int indicador1) {
		this.indicador1 = indicador1;
	}

	public int getIndicador2() {
		return indicador2;
	}

	public void setIndicador2(int indicador2) {
		this.indicador2 = indicador2;
	}

	public int getIndicador3() {
		return indicador3;
	}

	public void setIndicador3(int indicador3) {
		this.indicador3 = indicador3;
	}
	
	/**
	 * 
	 * */
	public String obtenerNombreHallazgo(int codigoTipoHallazgo,int codigoHallazgo)
	{
		if(codigoTipoHallazgo == ComponenteOdontograma.codigoTipoHallazgoDiente)
		{
			for(DtoHallazgoOdontologico hall :this.getArrayHallazgosDiente())
			{
				if(Utilidades.convertirAEntero(hall.getConsecutivo()) == codigoHallazgo)
					return hall.getCodigo()+" "+hall.getNombre();
			}
		}
		else if(codigoTipoHallazgo == ComponenteOdontograma.codigoTipoHallazgoSuper)
		{
			for(DtoHallazgoOdontologico hall :this.getArrayHallazgosSuperficie())
			{
				if(Utilidades.convertirAEntero(hall.getConsecutivo()) == codigoHallazgo)
					return hall.getCodigo()+" "+hall.getNombre();
			}
		}
		else if(codigoTipoHallazgo == ComponenteOdontograma.codigoTipoHallazgoBoca)
		{
			for(DtoHallazgoOdontologico hall :this.getArrayHallazgosBoca())
			{
				if(Utilidades.convertirAEntero(hall.getConsecutivo()) == codigoHallazgo)
					return hall.getCodigo()+" "+hall.getNombre();
			}
		}
		
		return "";
	}

	public ArrayList<DtoHallazgoOdontologico> getArrayHallazgosBoca() {
		if(arrayHallazgosBoca == null)
		{
			arrayHallazgosBoca = new ArrayList<DtoHallazgoOdontologico>();
		}
		return arrayHallazgosBoca;
	}

	public void setArrayHallazgosBoca(
			ArrayList<DtoHallazgoOdontologico> arrayHallazgosBoca) {
		this.arrayHallazgosBoca = arrayHallazgosBoca;
	}

	public ArrayList<InfoProgramaServicioPlan> getArrayProgServiPlanT() {
		if(arrayProgServiPlanT == null)
		{
			arrayProgServiPlanT = new ArrayList<InfoProgramaServicioPlan>();
		}
		return arrayProgServiPlanT;
	}

	public void setArrayProgServiPlanT(ArrayList<InfoProgramaServicioPlan> arrayProgServiPlanT) {
		this.arrayProgServiPlanT = arrayProgServiPlanT;
	}

	/**
	 * @return the arrayLogPlanTratamiento
	 */
	public ArrayList<DtoLogPlanTratamiento> getArrayLogPlanTratamiento() {
		if(arrayLogPlanTratamiento == null)
		{
			arrayLogPlanTratamiento = new ArrayList<DtoLogPlanTratamiento>();
		}
		return arrayLogPlanTratamiento;
	}

	/**
	 * @param arrayLogPlanTratamiento the arrayLogPlanTratamiento to set
	 */
	public void setArrayLogPlanTratamiento(ArrayList<DtoLogPlanTratamiento> arrayLogPlanTratamiento) {
		this.arrayLogPlanTratamiento = arrayLogPlanTratamiento;
	}

	public String getPathNombreContexo() {
		return pathNombreContexo;
	}

	public void setPathNombreContexo(String pathNombreContexo) {
		this.pathNombreContexo = pathNombreContexo;
	}
	
	/**
	 * @return the idProgramaServicioLog
	 */
	public String getIdProgramaServicioLog() {
		return idProgramaServicioLog;
	}

	/**
	 * @param idProgramaServicioLog the idProgramaServicioLog to set
	 */
	public void setIdProgramaServicioLog(String idProgramaServicioLog) {
		this.idProgramaServicioLog = idProgramaServicioLog;
	}

	/**
	 * @return the tipoHistorico
	 */
	public int getTipoHistorico() {
		return tipoHistorico;
	}

	/**
	 * @param tipoHistorico the tipoHistorico to set
	 */
	public void setTipoHistorico(int tipoHistorico) {
		this.tipoHistorico = tipoHistorico;
	}

	/**
	 * @return the arrayLogProgramaServicioPlan
	 */
	public ArrayList<DtoLogProgServPlant> getArrayLogProgramaServicioPlan() {
		if(arrayLogProgramaServicioPlan == null)
		{
			arrayLogProgramaServicioPlan = new ArrayList<DtoLogProgServPlant>();
		}
		return arrayLogProgramaServicioPlan;
	}

	/**
	 * @param arrayLogProgramaServicioPlan the arrayLogProgramaServicioPlan to set
	 */
	public void setArrayLogProgramaServicioPlan(
			ArrayList<DtoLogProgServPlant> arrayLogProgramaServicioPlan) {
		this.arrayLogProgramaServicioPlan = arrayLogProgramaServicioPlan;
	}
	
	/**
	 * @return the utilizaProgOdontIns
	 */
	public String getUtilizaProgOdontIns() {
		return utilizaProgOdontIns;
	}

	/**
	 * @param utilizaProgOdontIns the utilizaProgOdontIns to set
	 */
	public void setUtilizaProgOdontIns(String utilizaProgOdontIns) {
		this.utilizaProgOdontIns = utilizaProgOdontIns;
	}

	/**
	 * @return the validaPresuOdontCont
	 */
	public String getValidaPresuOdontCont() {
		return validaPresuOdontCont;
	}

public InfoDatosStr getInfoGeneral() {
		return infoGeneral;
	}

	public void setInfoGeneral(InfoDatosStr infoGeneral) {
		this.infoGeneral = infoGeneral;
	}
	/**
	 * @param validaPresuOdontCont the validaPresuOdontCont to set
	 */
	public void setValidaPresuOdontCont(String validaPresuOdontCont) {
		this.validaPresuOdontCont = validaPresuOdontCont;
	}

	/**
	 * @return the arrayPosiblesEstados
	 */
	public ArrayList<InfoDatosString> getArrayPosiblesEstados() {
		if(arrayPosiblesEstados == null)
		{
			arrayPosiblesEstados = new ArrayList<InfoDatosString>();
		}
		return arrayPosiblesEstados;
	}

	/**
	 * @param arrayPosiblesEstados the arrayPosiblesEstados to set
	 */
	public void setArrayPosiblesEstados(
			ArrayList<InfoDatosString> arrayPosiblesEstados) {
		this.arrayPosiblesEstados = arrayPosiblesEstados;
	}

	/**
	 * @return the evaluarEstadosPlanTratamiento
	 */
	public boolean isEvaluarEstadosPlanTratamiento() {
		return evaluarEstadosPlanTratamiento;
	}

	/**
	 * @param evaluarEstadosPlanTratamiento the evaluarEstadosPlanTratamiento to set
	 */
	public void setEvaluarEstadosPlanTratamiento(
			boolean evaluarEstadosPlanTratamiento) {
		this.evaluarEstadosPlanTratamiento = evaluarEstadosPlanTratamiento;
	}

	public String getEsResumen() {
		return esResumen;
	}

	public void setEsResumen(String esResumen) {
		this.esResumen = esResumen;
	}

	public String getEsImpresion() {
		return esImpresion;
	}

	public void setEsImpresion(String esImpresion) {
		this.esImpresion = esImpresion;
	}
	/**
	 * @return the indicador4
	 */
	public int getIndicador4() {
		return indicador4;
	}

	/**
	 * @param indicador4 the indicador4 to set
	 */
	public void setIndicador4(int indicador4) {
		this.indicador4 = indicador4;
	}

	/**
	 * @return the fechaProxCita
	 */
	public String getFechaProxCita() {
		return fechaProxCita;
	}

	/**
	 * @param fechaProxCita the fechaProxCita to set
	 */
	public void setFechaProxCita(String fechaProxCita) {
		this.fechaProxCita = fechaProxCita;
	}

	/**
	 * @return the arrayServProxCita
	 */
	public ArrayList<InfoServicios> getArrayServProxCita() {
		if(arrayServProxCita == null)
		{
			arrayServProxCita = new ArrayList<InfoServicios>();
		}
		return arrayServProxCita;
	}

	/**
	 * @param arrayServProxCita the arrayServProxCita to set
	 */
	public void setArrayServProxCita(ArrayList<InfoServicios> arrayServProxCita) {
		this.arrayServProxCita = arrayServProxCita;
	}

	/**
	 * @return the posServProxCita
	 */
	public int getPosServProxCita() {
		return posServProxCita;
	}

	/**
	 * @param posServProxCita the posServProxCita to set
	 */
	public void setPosServProxCita(int posServProxCita) {
		this.posServProxCita = posServProxCita;
	}	

	public boolean isSoloModifInfoNueva() {
		return soloModifInfoNueva;
	}

	public void setSoloModifInfoNueva(boolean soloModifInfoNueva) {
		this.soloModifInfoNueva = soloModifInfoNueva;
	}

	public boolean isMostrarSoloOdontograma() {
		return mostrarSoloOdontograma;
	}

	public void setMostrarSoloOdontograma(boolean mostrarSoloOdontograma) {
		this.mostrarSoloOdontograma = mostrarSoloOdontograma;
	}

	/**
	 * @return the newEstadoProg
	 */
	public String getNewEstadoProg() {
		return newEstadoProg;
	}

	/**
	 * @param newEstadoProg the newEstadoProg to set
	 */
	public void setNewEstadoProg(String newEstadoProg) {
		this.newEstadoProg = newEstadoProg;
	}

	/**
	 * @return the nuevaInclusion
	 */
	public InfoDetallePlanTramiento getNuevaInclusion() {
		return nuevaInclusion;
	}

	/**
	 * @param nuevaInclusion the nuevaInclusion to set
	 */
	public void setNuevaInclusion(InfoDetallePlanTramiento nuevaInclusion) {
		this.nuevaInclusion = nuevaInclusion;
	}

	/**
	 * @return the indicador5
	 */
	public int getIndicador5() {
		return indicador5;
	}

	/**
	 * @param indicador5 the indicador5 to set
	 */
	public void setIndicador5(int indicador5) {
		this.indicador5 = indicador5;
	}

	/**
	 * @return the estadosServicios
	 */
	public String getEstadosServicios() {
		return estadosServicios;
	}

	/**
	 * @param estadosServicios the estadosServicios to set
	 */
	public void setEstadosServicios(String estadosServicios) {
		this.estadosServicios = estadosServicios;
	}
	public BigDecimal getIndicadorAuxBd() {
		return indicadorAuxBd;
	}

	public void setIndicadorAuxBd(BigDecimal indicadorAuxBd) {
		this.indicadorAuxBd = indicadorAuxBd;
	}

	/**
	 * @return the instRegistraAtenExt
	 */
	public String getInstRegistraAtenExt() {
		return instRegistraAtenExt;
	}

	/**
	 * @param instRegistraAtenExt the instRegistraAtenExt to set
	 */
	public void setInstRegistraAtenExt(String instRegistraAtenExt) {
		this.instRegistraAtenExt = instRegistraAtenExt;
	}

	/**
	 * @return the arrayMotivoCancelacion
	 */
	public ArrayList<DtoMotivosAtencion> getArrayMotivoCancelacion() {
		if(arrayMotivoCancelacion == null)
		{
			arrayMotivoCancelacion = new ArrayList<DtoMotivosAtencion>();
		}
		return arrayMotivoCancelacion;
	}

	/**
	 * @param arrayMotivoCancelacion the arrayMotivoCancelacion to set
	 */
	public void setArrayMotivoCancelacion(
			ArrayList<DtoMotivosAtencion> arrayMotivoCancelacion) {
		this.arrayMotivoCancelacion = arrayMotivoCancelacion;
	}

	public void setMostrarMensajeProceExito(InfoDatosStr mostrarMensajeProceExito) {
		this.mostrarMensajeProceExito = mostrarMensajeProceExito;
	}

	public InfoDatosStr getMostrarMensajeProceExito() {
		return mostrarMensajeProceExito;
	}

	/**
	 * @return the seccionAplica
	 */
	public String getSeccionAplica() {
		return seccionAplica;
	}

	/**
	 * @param seccionAplica the seccionAplica to set
	 */
	public void setSeccionAplica(String seccionAplica) {
		this.seccionAplica = seccionAplica;
	}

	/**
	 * @return the contenedor
	 */
	public String getContenedor() {
		return contenedor;
	}

	/**
	 * @param contenedor the contenedor to set
	 */
	public void setContenedor(String contenedor) {
		this.contenedor = contenedor;
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

	public boolean isLlamadoSeccionCita() {
		return llamadoSeccionCita;
	}

	public void setLlamadoSeccionCita(boolean llamadoSeccionCita) {
		this.llamadoSeccionCita = llamadoSeccionCita;
	}

	/**
	 * Método implementado para saber si puedo terminar el plan de tratamiento.
	 * @return
	 */
	public boolean puedoTerminarPlanTratamiento()
	{
		boolean puedoTerminar = true;
		//Si existe algun servicio que esté pendiente o contratadoa aun no se puede dar por terminado el plan de tratamiento
		
		
		for(InfoDetallePlanTramiento elem: this.infoPlanTrata.getSeccionHallazgosDetalle())//detalleSuperficio
		{
			for(InfoHallazgoSuperficie detallePlan: elem.getDetalleSuperficie())//programasServicios
			{
				for(InfoProgramaServicioPlan progServPlanT: detallePlan.getProgramasOservicios())
				{	
					Log4JManager.info("Programa Estado->"+progServPlanT.getEstadoPrograma()+" new Estado "+progServPlanT.getNewEstadoProg());
					Log4JManager.info("Programa Estado->"+progServPlanT.getEstadoServicio()+" new Estado "+progServPlanT.getNewEstadoProg());
					
					for(InfoServicios elem3:progServPlanT.getListaServicios())
					{
						Log4JManager.info("Estado-->"+elem3.getEstadoServicio()+ "New Estado Servicio->" +elem3.getNewEstado());
					}
				}
			}
		}
		
		
		//Se revisa sección de plan de tratamiento
		for(InfoDetallePlanTramiento detalle:this.infoPlanTrata.getSeccionHallazgosDetalle())
		{
			for(InfoHallazgoSuperficie superficie:detalle.getDetalleSuperficie())
			{
				for (InfoProgramaServicioPlan programa:superficie.getProgramasOservicios())
				{
					if(!programa.getPuedoTerminarPrograma())
					{
						if(programa.getNewEstadoProg().equals(ConstantesIntegridadDominio.acronimoEstadoPendiente)||programa.getNewEstadoProg().equals(ConstantesIntegridadDominio.acronimoContratado)
								|| programa.getNewEstadoProg().equals(ConstantesIntegridadDominio.acronimoPorAutorizar))
						{
							puedoTerminar = false;
						}
						for(InfoServicios servicio:programa.getListaServicios())
						{
							if(servicio.getNewEstado().equals(ConstantesIntegridadDominio.acronimoEstadoPendiente)||servicio.getNewEstado().equals(ConstantesIntegridadDominio.acronimoContratado)
									|| servicio.getNewEstado().equals(ConstantesIntegridadDominio.acronimoPorAutorizar))
							{
								puedoTerminar = false;
							}
						}
					}	
				}
			}
		}
		//Se revisa seccion otros hallazgos
		for(InfoDetallePlanTramiento detalle:this.infoPlanTrata.getSeccionOtrosHallazgos())
		{
			for(InfoHallazgoSuperficie superficie:detalle.getDetalleSuperficie())
			{
				for (InfoProgramaServicioPlan programa:superficie.getProgramasOservicios())
				{
					if(!programa.getPuedoTerminarPrograma())
					{
						if(programa.getNewEstadoProg().equals(ConstantesIntegridadDominio.acronimoEstadoPendiente)||programa.getNewEstadoProg().equals(ConstantesIntegridadDominio.acronimoContratado)
								|| programa.getNewEstadoProg().equals(ConstantesIntegridadDominio.acronimoPorAutorizar))
						{
							puedoTerminar = false;
						}
						for(InfoServicios servicio:programa.getListaServicios())
						{
							if(servicio.getNewEstado().equals(ConstantesIntegridadDominio.acronimoEstadoPendiente)||servicio.getNewEstado().equals(ConstantesIntegridadDominio.acronimoContratado)
									|| servicio.getNewEstado().equals(ConstantesIntegridadDominio.acronimoPorAutorizar) && servicio.getPorConfirmar().equals(ConstantesBD.acronimoSi) && servicio.getEstadoServicio().equals(ConstantesIntegridadDominio.acronimoRealizadoInterno))
							{
								puedoTerminar = false;
							}
						}
					}	
				}
			}
		}
		//Se revisa seccion Boca
		for(InfoHallazgoSuperficie superficie:this.infoPlanTrata.getSeccionHallazgosBoca())
		{
			for (InfoProgramaServicioPlan programa:superficie.getProgramasOservicios())
			{
				if(!programa.getPuedoTerminarPrograma())
				{
					if(programa.getNewEstadoProg().equals(ConstantesIntegridadDominio.acronimoEstadoPendiente)||programa.getNewEstadoProg().equals(ConstantesIntegridadDominio.acronimoContratado)
							|| programa.getNewEstadoProg().equals(ConstantesIntegridadDominio.acronimoPorAutorizar))
					{
						puedoTerminar = false;
					}
					for(InfoServicios servicio:programa.getListaServicios())
					{
						if(servicio.getNewEstado().equals(ConstantesIntegridadDominio.acronimoEstadoPendiente)||servicio.getNewEstado().equals(ConstantesIntegridadDominio.acronimoContratado)
								|| servicio.getNewEstado().equals(ConstantesIntegridadDominio.acronimoPorAutorizar))
						{
							puedoTerminar = false;
						}
					}
				}	
			}
		}
		
		return puedoTerminar;
	}

	/**
	 * @return Retorna el atributo superficiesSeleccionadasXPrograma
	 */
	public int[] getSuperficiesSeleccionadasXPrograma()
	{
		return superficiesSeleccionadasXPrograma;
	}

	/**
	 * @param superficiesSeleccionadasXPrograma Asigna el atributo superficiesSeleccionadasXPrograma
	 */
	public void setSuperficiesSeleccionadasXPrograma(int[] superficiesSeleccionadasXPrograma)
	{
		this.superficiesSeleccionadasXPrograma = superficiesSeleccionadasXPrograma;
	}

	/**
	 * @return Retorna el atributo indiceProgramaSeleccionado
	 */
	public int getIndiceProgramaSeleccionado()
	{
		return indiceProgramaSeleccionado;
	}

	/**
	 * @param indiceProgramaSeleccionado Asigna el atributo indiceProgramaSeleccionado
	 */
	public void setIndiceProgramaSeleccionado(int indiceProgramaSeleccionado)
	{
		this.indiceProgramaSeleccionado = indiceProgramaSeleccionado;
	}

	/**
	 * @return Retorna el atributo indiceSuperficieSeleccionada
	 */
	public int getIndiceSuperficieSeleccionada()
	{
		return indiceSuperficieSeleccionada;
	}

	/**
	 * @param indiceSuperficieSeleccionada Asigna el atributo indiceSuperficieSeleccionada
	 */
	public void setIndiceSuperficieSeleccionada(int indiceSuperficieSeleccionada)
	{
		this.indiceSuperficieSeleccionada = indiceSuperficieSeleccionada;
	}

	/**
	 * @return Retorna el atributo codigoProgramaSeleccionado
	 */
	public int getCodigoProgramaSeleccionado()
	{
		return codigoProgramaSeleccionado;
	}

	/**
	 * @param codigoProgramaSeleccionado Asigna el atributo codigoProgramaSeleccionado
	 */
	public void setCodigoProgramaSeleccionado(int codigoProgramaSeleccionado)
	{
		this.codigoProgramaSeleccionado = codigoProgramaSeleccionado;
	}

	/**
	 * @return Retorna el atributo equivalente
	 */
	public boolean getEquivalente()
	{
		return equivalente;
	}

	/**
	 * @param equivalente Asigna el atributo equivalente
	 */
	public void setEquivalente(boolean equivalente)
	{
		this.equivalente = equivalente;
	}

	/**
	 * @return Retorna el atributo indicePiezaSeleccionada
	 */
	public int getIndicePiezaSeleccionada()
	{
		return indicePiezaSeleccionada;
	}

	/**
	 * @param indicePiezaSeleccionada Asigna el atributo indicePiezaSeleccionada
	 */
	public void setIndicePiezaSeleccionada(int indicePiezaSeleccionada)
	{
		this.indicePiezaSeleccionada = indicePiezaSeleccionada;
	}

	/**
	 * @return Retorna el atributo superficieSeleccionada
	 */
	public int getSuperficieSeleccionada()
	{
		return superficieSeleccionada;
	}

	/**
	 * @param superficieSeleccionada Asigna el atributo superficieSeleccionada
	 */
	public void setSuperficieSeleccionada(int superficieSeleccionada)
	{
		this.superficieSeleccionada = superficieSeleccionada;
	}

	/**
	 * @return Retorna atributo centroCosto
	 */
	public int getCentroCosto()
	{
		return centroCosto;
	}

	/**
	 * @param centroCosto Asigna atributo centroCosto
	 */
	public void setCentroCosto(int centroCosto)
	{
		this.centroCosto = centroCosto;
	}

	/**
	 * @param mensajeInformativo the mensajeInformativo to set
	 */
	public void setMensajeInformativo(String mensajeInformativo) {
		this.mensajeInformativo = mensajeInformativo;
	}

	/**
	 * @return the mensajeInformativo
	 */
	public String getMensajeInformativo() {
		return mensajeInformativo;
	}

	/**
	 * Obtiene el valor del atributo contadorCodigoTemporalProgramaHallazgoPieza
	 *
	 * @return Retorna atributo contadorCodigoTemporalProgramaHallazgoPieza
	 */
	public int getContadorCodigoTemporalProgramaHallazgoPieza()
	{
		return contadorCodigoTemporalProgramaHallazgoPieza++;
	}
	

	
}