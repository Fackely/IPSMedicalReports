package com.princetonsa.actionform.tesoreria;


import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.adjuntos.DTOArchivoAdjunto;

import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.princetonsa.dto.tesoreria.DtoAdjuntosMovimientosCaja;
import com.princetonsa.dto.tesoreria.DtoConsolidadoMovimiento;
import com.princetonsa.dto.tesoreria.DtoConsultaEntregaTransportadora;
import com.princetonsa.dto.tesoreria.DtoEntidadesFinancieras;
import com.princetonsa.dto.tesoreria.DtoFormaPagoDocSoporte;
import com.princetonsa.dto.tesoreria.DtoInformacionEntrega;
import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;
import com.princetonsa.mundo.InstitucionBasica;
import com.servinte.axioma.dto.tesoreria.DtoFiltroReportesArqueosCierres;
import com.servinte.axioma.orm.AdjuntosMovimientosCaja;
import com.servinte.axioma.orm.Cajas;
import com.servinte.axioma.orm.CuentasBancarias;
import com.servinte.axioma.orm.EntidadesFinancieras;
import com.servinte.axioma.orm.Terceros;
import com.servinte.axioma.orm.TransportadoraValores;


/**
 * Forma para manejar la Consulta de entrega a transportadora 
 * @author Diana Carolina G
 * @since Julio 10/2010
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ConsultarEntregaTransportadoraForm extends ActionForm
{
	
	
	
	/**
	 * Manejo de estados de la clase
	 */
	private String estado;
	
	/**
	 * Dto con el consolidado de la informaci&oacute;n
	 */
	private DtoFormaPagoDocSoporte dtoFormaPagoDocSoporte;
		
	/**
	 * Dto Consulta Transportadora
	 */
	private DtoConsultaEntregaTransportadora dtoConsultaEntregaTransp;
	
	/**
	 * Array que lista los centros de atenci&oacute;n filtrados
	 */
	private ArrayList<DtoCentrosAtencion> listaCentrosAtencion;
	
	/**
	 * Atributo para cargar las cajas
	 */
	private List<Cajas> listaCajas;
	
	/**
	 * Atributo para cargar los cajeros
	 */
	private ArrayList<DtoUsuarioPersona> listaCajero;
	
	/**
	 * Atributo para cargar las transportadoras
	 */
	
	private List<TransportadoraValores> listaTransportadoraValores;
	
	/**
	 * Atributo para cargar las Entidades Financieras	
	 */
	private List<DtoEntidadesFinancieras> listaEntidades; 
	
	/**
	 * Atributo para cargar la consulta de entrega a transportadora	
	 */
	private List<DtoConsultaEntregaTransportadora> listaConsultaEntregaTransportadora;
	
	/**
	 * Lista dto informacion entrega que retorna el resultado 
	 */
	private List<DtoInformacionEntrega> listaDtoInformacionEntrega;
	
	/**
	 * Dto que trae todo los registros de entregas encontrados 
	 */
	private DtoInformacionEntrega dtoInformacionEntrega;
	
	/**
	 * Atributo que permite saber la posici&oacute;n de la lista de Entregas encontradas
	 * Utilizado para ver el detalle de cada posici&oacute;n
	 */
	private int posArray;
	
	
	/**
	 * Nombre Completo Cajero
	 */
	private String nombreCompletoCajero="";
	
	/**
	 * Atributo que muestra Entidad Financiera, tipo y n&uacute;mero de cuenta 
	 */
	private CuentasBancarias cuentaBancaria;
	
	/**
	 * Dto que tiene la informaci&oacute;n referente a los totales por forma de pago entregados
	 */
	private DtoConsolidadoMovimiento consolidadoMovimientoDTO;
	
	private String mostrarParaSeccionEspecial; 
	
	/**
	 * Atributo que muestra el valor total entregado a la transportadora
	 */
		
	private double totalEntregadoTransportadora;
	

	/**
	 * Utilizado para conocer si el proceso que se esta realizando es una consulta de un movimiento
	 * ya registrado o un nuevo movimiento. Sirve para ocultar y mostrar componentes de presentaciï¿½n.
	 * 
	 */
	private boolean consulta;
	
	
	/**
	 * 
	 */
	private AdjuntosMovimientosCaja adjuntoMovimiento;
	/**
	 * 
	 */
	private List<AdjuntosMovimientosCaja> listaAdjuntos;
	/**
	 * 
	 */
	private int postArrayAdjunto;
	
	/**
	 * 
	 */
	
	private int numeroDocumento;
	
	/**
	 * Lista dto adjuntos movimientos caja
	 */
	private List<DtoAdjuntosMovimientosCaja> listaAdjuntosMovimientos;
	
	/**
	 * Atributo que indica si la institucion es multiempresa o no.
	 */
	private String esMultiEmpresa;
	
	/**
	 * Atributo que indica la ubicacion del logo
	 */

	private String ubicacionLogo;	
	
	/**
	 * Limpia los datos de la forma y pone valores por defecto
	 */
	
	/**
	 * Atributo usado para completar la ruta hacia la cual se debe 
	 * direccionar la aplicaci&oacute;n.
	 */
	private String path;
	
	/**
	 * Atributo que almacena  la actividad 
	 * economica de la instituciï¿½n
	 */
	
	private String actividadEconomicaInst;
	
	/**
	 * Atributo que almacena la Institucion Basica
	 */
	
	
	private InstitucionBasica institucion;
	
	
	/**
	 * Atributo que almacena el nombre de la columna por la cual deben ser
	 * ordenados los registros encontrados.
	 */
	private String patronOrdenar;
	
	/**
	 * Atributo usado para ordenar descendentemente.
	 */
	private String esDescendente;	
	
	/**
	 * Atributo que almacena el tipo de reporte seleccionado
	 */
	private String tipoReporte;
	
	/**
	 * Atributo que almacena la lista de nombres de reportes
	 * a imprimir
	 */
	private ArrayList<String> listaNombresReportes;
	
	/**
	 * Atributo que contiene la información del titulo, encabezado del reporte
	 */
	private DtoFiltroReportesArqueosCierres dtoFiltroReporte;
	
	
	public void reset()
	{
		this.dtoFormaPagoDocSoporte=new DtoFormaPagoDocSoporte();
		this.setListaCajas(new  ArrayList<Cajas>());
		this.listaCentrosAtencion = new ArrayList<DtoCentrosAtencion>();
		this.listaCajero = new ArrayList<DtoUsuarioPersona>();
		this.dtoConsultaEntregaTransp = new  DtoConsultaEntregaTransportadora();
		this.listaTransportadoraValores= new ArrayList<TransportadoraValores>();
		this.listaEntidades= new ArrayList<DtoEntidadesFinancieras>();
		this.listaConsultaEntregaTransportadora =new ArrayList<DtoConsultaEntregaTransportadora>();
		this.setListaDtoInformacionEntrega(new ArrayList<DtoInformacionEntrega>());
		this.posArray=ConstantesBD.codigoNuncaValido;
		this.setDtoInformacionEntrega(new DtoInformacionEntrega());
		this.setNombreCompletoCajero("");
		this.cuentaBancaria= new CuentasBancarias();
		this.cuentaBancaria.setEntidadesFinancieras(new EntidadesFinancieras());
		this.cuentaBancaria.getEntidadesFinancieras().setTerceros(new Terceros());
		this.consolidadoMovimientoDTO = new DtoConsolidadoMovimiento();
		this.mostrarParaSeccionEspecial= "";
		this.totalEntregadoTransportadora=0;
		this.consulta=Boolean.TRUE;
		
		this.adjuntoMovimiento= new AdjuntosMovimientosCaja();
		this.listaAdjuntos = new ArrayList<AdjuntosMovimientosCaja>();
		this.postArrayAdjunto=ConstantesBD.codigoNuncaValido;
		this.numeroDocumento=ConstantesBD.codigoNuncaValido;
		this.listaAdjuntosMovimientos = new ArrayList<DtoAdjuntosMovimientosCaja>();
		
		this.setConsolidadoMovimientoDTO( new DtoConsolidadoMovimiento());
		this.getConsolidadoMovimientoDTO().setArchivosAdjuntosMovimiento(new ArrayList<DTOArchivoAdjunto>());
		this.institucion= new InstitucionBasica();
		this.institucion.setActividadEconomica("");
		this.setPatronOrdenar("");
		this.setEsDescendente("");
		
		this.setTipoReporte("");
		this.listaNombresReportes = new ArrayList<String>();
		this.dtoFiltroReporte = new DtoFiltroReportesArqueosCierres();
	}
	
	
	@Override
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request)
	{
		
		ActionErrors errores= new ActionErrors();
		
		
		if(this.estado.equals("consultar")){
			
			/*
			 * 1. Hacer validaciones 
			 */
			validarCampoRequeridos(errores);
		}
		
		
		// Por el momento no se valida nada
		return errores;
	}
	
	
	
	/**
	 * Validar Campos requeridos
	 * Este metodo valida el centro de atencion y las fechas
	 * @param errores
	 * @return
	 */
	private ActionErrors validarCampoRequeridos(ActionErrors errores) {
		
		/*
		 * 1. Validar el centro de atencion 		
		 */
		if (this.getDtoConsultaEntregaTransp().getCodigoCentroAtencion()<=ConstantesBD.codigoNuncaValido)
		{
			/*Error
			 * 
			 */
			errores.add("", new ActionMessage("errors.required","El Centro de Atenciï¿½n"));
		}
		
		
		/*
		 * Validar Fecha requeridas 
		 */
		this.validarFechas(errores);
		
		
		return errores;
	}






	private void validacionesFechaInicial(ActionErrors errores) {
		
		
		String fechaActual=UtilidadFecha.getFechaActual();
		
		/*
		 * 1. Fecha Inicial no sea mayor a la fecha del sistema
		 */
		if( UtilidadFecha.esFechaMenorQueOtraReferencia(fechaActual, this.getDtoConsultaEntregaTransp().getFechaGeneracionInicial() ) )
		{
			errores.add("FECHA INICIAL MAYOR QUE FECHA ACTUAL.", new ActionMessage(
					 "errors.fechaPosteriorIgualActual"," Inicial "+this.dtoConsultaEntregaTransp.getFechaGeneracionInicial()," Actual "+fechaActual));	
		}
		
	}







	/**
	 * Validar fechas 
	 * @param errores
	 */
	private void validarFechas(ActionErrors errores) {
		
		/*
		 * 1. Validar fecha inicio 
		 */
		if(UtilidadTexto.isEmpty(this.dtoConsultaEntregaTransp.getFechaGeneracionInicial())){
			
			errores.add("", new ActionMessage("errors.required"," Fecha Generaciï¿½n Inicial "));
			
		}
		else
		{
			/*
			 *1.1  
			 */
			this.validacionesFechaInicial(errores);
		}
		
		
		/*
		 * validar fecha final
		 */
		if(UtilidadTexto.isEmpty(this.dtoConsultaEntregaTransp.getFechaGeneracionFinal())){
			
			errores.add("", new ActionMessage("errors.required"," Fecha Generaciï¿½n Final "));
		}
		else
		{
			this.validacionesFechaFinal(errores);
			
		}
		
		if ((this.getDtoConsultaEntregaTransp().getFechaGeneracionInicial()!=null) && !((this.getDtoConsultaEntregaTransp().getFechaGeneracionInicial().toString()).equals(""))&&
				(this.getDtoConsultaEntregaTransp().getFechaGeneracionFinal()!=null) && !((this.getDtoConsultaEntregaTransp().getFechaGeneracionFinal().toString()).equals(""))) {
				try {
					
					String fechaInicial = UtilidadFecha.conversionFormatoFechaAAp(
							this.getDtoConsultaEntregaTransp().getFechaGeneracionInicial());	
					
					String fechaFin = UtilidadFecha.conversionFormatoFechaAAp(
							this.getDtoConsultaEntregaTransp().getFechaGeneracionFinal());
					
					long anio = UtilidadFecha.obtenerDiferenciaEntreFechas(fechaInicial,fechaFin, 5);
					
					if (anio> 365) {
						errores.add("EL RANGO MAXIMO ENTRE LAS FECHAS DEBE SER INFERIOR A UN Aï¿½O.", new ActionMessage(
								 "errors.rangoEntreFechasInvalido"," Final "+fechaFin," Inicial "+fechaInicial));
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		
		
		
	}

	
	/**
	 * 
	 * @param errores
	 */
	private void validacionesFechaFinal(ActionErrors errores) {
		
		/*
		 * 1. Fecha Final no sea mayor a la fecha del sistema
		 */
		
		String fechaActual=UtilidadFecha.getFechaActual();
		
		
		if( UtilidadFecha.esFechaMenorQueOtraReferencia(fechaActual,  this.getDtoConsultaEntregaTransp().getFechaGeneracionFinal()   ) )
		{
			errores.add("FECHA INICIAL MAYOR QUE FECHA ACTUAL.", new ActionMessage(
					 "errors.fechaPosteriorIgualActual"," Final "+ this.getDtoConsultaEntregaTransp().getFechaGeneracionFinal()," Actual "+ fechaActual ));	
		}
		
		if(UtilidadFecha.esFechaMenorQueOtraReferencia(this.getDtoConsultaEntregaTransp().getFechaGeneracionFinal(),this.getDtoConsultaEntregaTransp().getFechaGeneracionInicial())){
			errores.add("FECHA FIN MENOR QUE FECHA INICIAL.", new ActionMessage(
							 "errors.fechaAnteriorIgualAOtraDeReferencia"," Final "+this.getDtoConsultaEntregaTransp().getFechaGeneracionFinal()," Inicial "+this.getDtoConsultaEntregaTransp().getFechaGeneracionInicial()));
		}
		
	}







	/*
	 * 
	 */
	public void borrar(){
		
	}
	
	
	

	/**
	 * @return Retorna atributo estado
	 */
	public String getEstado()
	{
		return estado;
	}

	/**
	 * @param estado Asigna atributo estado
	 */
	public void setEstado(String estado)
	{
		this.estado = estado;
	}

	/**
	 * @return Retorna atributo dtoFormaPagoDocSoporte
	 */
	public DtoFormaPagoDocSoporte getDtoFormaPagoDocSoporte()
	{
		return dtoFormaPagoDocSoporte;
	}

	/**
	 * @param dtoFormaPagoDocSoporte Asigna atributo dtoFormaPagoDocSoporte
	 */
	public void setDtoFormaPagoDocSoporte(
			DtoFormaPagoDocSoporte dtoFormaPagoDocSoporte)
	{
		this.dtoFormaPagoDocSoporte = dtoFormaPagoDocSoporte;
	}

	/**
	 * @return Retorna atributo listaCentrosAtencion
	 */
	public ArrayList<DtoCentrosAtencion> getListaCentrosAtencion()
	{
		return listaCentrosAtencion;
	}

	/**
	 * @param listaCentrosAtencion Asigna atributo listaCentrosAtencion
	 */
	public void setListaCentrosAtencion(
			ArrayList<DtoCentrosAtencion> listaCentrosAtencion)
	{
		this.listaCentrosAtencion = listaCentrosAtencion;
	}
	
	/**
	 * Obtiene el tama&ntilde;o de la lista de centros de atenci&oacute;n 
	 * @return int con el n&uacute;mero de elementos
	 */
	public int getTamanioListaCentrosAtencion()
	{
		return listaCentrosAtencion.size();
	}







	public void setListaCajas(List<Cajas> listaCajas) {
		this.listaCajas = listaCajas;
	}







	public List<Cajas> getListaCajas() {
		return listaCajas;
	}







	public void setListaCajero(ArrayList<DtoUsuarioPersona> listaCajero) {
		this.listaCajero = listaCajero;
	}







	public ArrayList<DtoUsuarioPersona> getListaCajero() {
		return listaCajero;
	}







	public void setDtoConsultaEntregaTransp(DtoConsultaEntregaTransportadora dtoConsultaEntregaTransp) {
		this.dtoConsultaEntregaTransp = dtoConsultaEntregaTransp;
	}







	public DtoConsultaEntregaTransportadora getDtoConsultaEntregaTransp() {
		return dtoConsultaEntregaTransp;
	}







	public void setListaTransportadoraValores(
			List<TransportadoraValores> listaTransportadoraValores) {
		this.listaTransportadoraValores = listaTransportadoraValores;
	}







	public List<TransportadoraValores> getListaTransportadoraValores() {
		return listaTransportadoraValores;
	}







	public void setListaEntidades(List<DtoEntidadesFinancieras> listaEntidades) {
		this.listaEntidades = listaEntidades;
	}







	public List<DtoEntidadesFinancieras> getListaEntidades() {
		return listaEntidades;
	}







	public void setListaConsultaEntregaTransportadora(
			List<DtoConsultaEntregaTransportadora> listaConsultaEntregaTransportadora) {
		this.listaConsultaEntregaTransportadora = listaConsultaEntregaTransportadora;
	}







	public List<DtoConsultaEntregaTransportadora> getListaConsultaEntregaTransportadora() {
		return listaConsultaEntregaTransportadora;
	}







	public void setListaDtoInformacionEntrega(
			List<DtoInformacionEntrega> listaDtoInformacionEntrega) {
		this.listaDtoInformacionEntrega = listaDtoInformacionEntrega;
	}







	public List<DtoInformacionEntrega> getListaDtoInformacionEntrega() {
		return listaDtoInformacionEntrega;
	}







	public void setPosArray(int posArray) {
		this.posArray = posArray;
	}







	public int getPosArray() {
		return posArray;
	}







	public void setDtoInformacionEntrega(DtoInformacionEntrega dtoInformacionEntrega) {
		this.dtoInformacionEntrega = dtoInformacionEntrega;
	}







	public DtoInformacionEntrega getDtoInformacionEntrega() {
		return dtoInformacionEntrega;
	}







	public void setNombreCompletoCajero(String nombreCompletoCajero) {
		this.nombreCompletoCajero = nombreCompletoCajero;
	}







	public String getNombreCompletoCajero() {
		return nombreCompletoCajero;
	}







	public void setCuentaBancaria(CuentasBancarias cuentaBancaria) {
		this.cuentaBancaria = cuentaBancaria;
	}







	public CuentasBancarias getCuentaBancaria() {
		return cuentaBancaria;
	}







	public void setConsolidadoMovimientoDTO(DtoConsolidadoMovimiento consolidadoMovimientoDTO) {
		this.consolidadoMovimientoDTO = consolidadoMovimientoDTO;
	}







	public DtoConsolidadoMovimiento getConsolidadoMovimientoDTO() {
		return consolidadoMovimientoDTO;
	}







	public void setMostrarParaSeccionEspecial(String mostrarParaSeccionEspecial) {
		this.mostrarParaSeccionEspecial = mostrarParaSeccionEspecial;
	}







	public String getMostrarParaSeccionEspecial() {
		return mostrarParaSeccionEspecial;
	}







	public void setTotalEntregadoTransportadora(double totalEntregadoTransportadora) {
		this.totalEntregadoTransportadora = totalEntregadoTransportadora;
	}







	public double getTotalEntregadoTransportadora() {
		return totalEntregadoTransportadora;
	}







	public void setConsulta(boolean consulta) {
		this.consulta = consulta;
	}







	public boolean isConsulta() {
		return consulta;
	}







	/**
	 * Este mï¿½todo se encarga de obtener el valor 
	 * del atributo adjuntoMovimiento
	
	 * @return retorna la variable adjuntoMovimiento 
	 * @author Angela Maria Aguirre 
	 */
	public AdjuntosMovimientosCaja getAdjuntoMovimiento() {
		return adjuntoMovimiento;
	}







	/**
	 * Este mï¿½todo se encarga de establecer el valor 
	 * del atributo adjuntoMovimiento
	
	 * @param valor para el atributo adjuntoMovimiento 
	 * @author Angela Maria Aguirre 
	 */
	public void setAdjuntoMovimiento(AdjuntosMovimientosCaja adjuntoMovimiento) {
		this.adjuntoMovimiento = adjuntoMovimiento;
	}







	/**
	 * Este mï¿½todo se encarga de obtener el valor 
	 * del atributo listaAdjuntos
	
	 * @return retorna la variable listaAdjuntos 
	 * @author Angela Maria Aguirre 
	 */
	public List<AdjuntosMovimientosCaja> getListaAdjuntos() {
		return listaAdjuntos;
	}







	/**
	 * Este mï¿½todo se encarga de establecer el valor 
	 * del atributo listaAdjuntos
	
	 * @param valor para el atributo listaAdjuntos 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaAdjuntos(List<AdjuntosMovimientosCaja> listaAdjuntos) {
		this.listaAdjuntos = listaAdjuntos;
	}







	public void setPostArrayAdjunto(int postArrayAdjunto) {
		this.postArrayAdjunto = postArrayAdjunto;
	}







	public int getPostArrayAdjunto() {
		return postArrayAdjunto;
	}







	public void setNumeroDocumento(int numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}







	public int getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setListaAdjuntosMovimientos(List<DtoAdjuntosMovimientosCaja> listaAdjuntosMovimientos) {
		this.listaAdjuntosMovimientos = listaAdjuntosMovimientos;
	}

	public List<DtoAdjuntosMovimientosCaja> getListaAdjuntosMovimientos() {
		return listaAdjuntosMovimientos;
	}


	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo ubicacionLogo
	 * 
	 * @param  valor para el atributo ubicacionLogo 
	 */

	public void setUbicacionLogo(String ubicacionLogo) {
		this.ubicacionLogo = ubicacionLogo;
	}


	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo ubicacionLogo
	 * 
	 * @return  Retorna la variable ubicacionLogo
	 */

	public String getUbicacionLogo() {
		return ubicacionLogo;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo esMultiEmpresa
	 * 
	 * @param  valor para el atributo esMultiEmpresa 
	 */

	public void setEsMultiEmpresa(String esMultiEmpresa) {
		this.esMultiEmpresa = esMultiEmpresa;
	}
	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo ubicacionLogo
	 * 
	 * @return  Retorna la variable ubicacionLogo
	 */

	public String getEsMultiEmpresa() {
		return esMultiEmpresa;
	}


	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo path
	 * 
	 * @param  valor para el atributo path 
	 */
	public void setPath(String path) {
		this.path = path;
	}


	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo path
	 * 
	 * @return  Retorna la variable path
	 */
	public String getPath() {
		return path;
	}


	/**
	 * M&eacute;todo que se encarga de establecer el valor
	 * del atributo actividadEconomicaInst
	 * @param actividadEconomicaInst
	 */

	public void setActividadEconomicaInst(String actividadEconomicaInst) {
		this.actividadEconomicaInst = actividadEconomicaInst;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 * del atributo actividadEconomicaInst
	 * @return actividadEconomicaInst
	 */
	
	public String getActividadEconomicaInst() {
		return actividadEconomicaInst;
	}


	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 * del atributo institucion
	 * @return
	 */


	public InstitucionBasica getInstitucion() {
		return institucion;
	}


	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo institucion
	 * @param institucion
	 */


	public void setInstitucion(InstitucionBasica institucion) {
		this.institucion = institucion;
	}


	/**
	 * @param patronOrdenar the patronOrdenar to set
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}


	/**
	 * @return the patronOrdenar
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}


	/**
	 * @param esDescendente the esDescendente to set
	 */
	public void setEsDescendente(String esDescendente) {
		this.esDescendente = esDescendente;
	}


	/**
	 * @return the esDescendente
	 */
	public String getEsDescendente() {
		return esDescendente;
	}


	public void setTipoReporte(String tipoReporte) {
		this.tipoReporte = tipoReporte;
	}


	public String getTipoReporte() {
		return tipoReporte;
	}


	public void setListaNombresReportes(ArrayList<String> listaNombresReportes) {
		this.listaNombresReportes = listaNombresReportes;
	}


	public ArrayList<String> getListaNombresReportes() {
		return listaNombresReportes;
	}


	public void setDtoFiltroReporte(DtoFiltroReportesArqueosCierres dtoFiltroReporte) {
		this.dtoFiltroReporte = dtoFiltroReporte;
	}


	public DtoFiltroReportesArqueosCierres getDtoFiltroReporte() {
		return dtoFiltroReporte;
	}


}
