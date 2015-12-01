/**
 * 
 */
package com.princetonsa.actionform.carteraPaciente;

import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;
import com.princetonsa.dto.administracion.DtoPaciente;
import com.princetonsa.dto.carteraPaciente.DtoDatosFinanciacion;
import com.princetonsa.dto.carteraPaciente.DtoSaldosInicialesCarteraPaciente;
import com.princetonsa.dto.facturacion.DtoFactura;
import com.princetonsa.mundo.carteraPaciente.DatosFinanciacion;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ResultadoBoolean;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

/**
 * @author axioma
 *
 */
public class SaldosInicialesCarteraPacienteForm extends ValidatorForm 
{
	
	/**
	 * 
	 */
	private boolean pacienteCargado;
	
	/**
	 * 
	 */
	private String estado;
	
	/**
	 * 
	 */
	private DtoPaciente paciente;
	
	/**
	 * HashMap TipoIdentificacion 
	 * */
	private ArrayList tipoIdentificacionMap;
	
	/**
	 * Datos de Financiacion
	 */
	private DtoDatosFinanciacion datosFinanciacion;
	
	/**
	 * Array list usado para la busqueda de facturas.
	 */
	private ArrayList<DtoFactura> facturas;
	
	
	/**
	 * 
	 */
	private ArrayList<HashMap<String, Object>> convenios = new ArrayList<HashMap<String,Object>>();
	
	/**
	 * 
	 */
	private ArrayList<DtoSaldosInicialesCarteraPaciente> saldosIniciales=new ArrayList<DtoSaldosInicialesCarteraPaciente>();
	
	/**
	 * 
	 */
	private String sufijo;
	
	/**
	 * 
	 */
	private int institucion;
	
	public int getInstitucion() {
		return institucion;
	}



	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	/**
	 * 
	 */
	private int ingresoFac; 
	
	/**
	 * Atributos para el manejo de las secciones
	 */
	private String seccionDeudor;
	private String seccionCodeudor;
	
	
	
	
	/*
	 * VARIABLES TEMPORALES DE TRABAJO 
	 */
	
	private String tipoDocGarantiaTempo;
	
	/**
	 * 
	 */
	private String consecutivoDocumentoTempo;
	
	/**
	 * 
	 */
	private boolean puedoModificarDeudor;
	
	/**
	 * 
	 */
	private boolean puedoModificarCodeudor;
	
	/**
	 * 
	 */
	private String tipoIdTemp;
	
	/**
	 * 
	 */
	private String numeroIdTemp;

	/**
	 * 
	 */
	private String facturaBusqueda;
	
	
	/**
	 * 
	 */
	private ResultadoBoolean mensaje;
	
	/**
	 * 
	 */
	private int posArray;
	
	
	//////CAMPOS PARA LA BUSQUEDA AVANZADA
	
	/**
	 * 
	 */
	private String tipoDocumentoGarantia;
	
	/**
	 * 
	 */
	private String numeroDocumentoGarantia;
	
	/**
	 * 
	 */
	private String tipoBusqueda;
	
	private String tipoID;
	private String numeroID;
	private String nombrePersona;
	private String apellidoPersona;
	
	private String documentoInicial;
	private String documentoFinal;
	private String convenio;
	
	/**
	 * 
	 */
	private ArrayList<HashMap<String, Object>> ciudadesExpDeudor = new ArrayList<HashMap<String,Object>>();
	
	/**
	 * 
	 */
	private ArrayList<HashMap<String, Object>> ciudadesExpCodeudor = new ArrayList<HashMap<String,Object>>();
	
	/**
	 * 
	 */
	private ArrayList<HashMap<String, Object>> paises = new ArrayList<HashMap<String,Object>>();
	
	/**
	 * 
	 */
	private String codigoPaisTempo;
	
	/**
	 * 
	 */
	private String fechaDocumentoGarantia;
	
	/**
	 * 
	 */
	private String deptoCiudadIDDeudor;

	/**
	 * 
	 */
	private String deptoCiudadIDCodeudor;

	/**
	 * 
	 */
	public void reset() 
	{
		this.mensaje=new ResultadoBoolean(false);
		this.paciente=new DtoPaciente();
		this.paciente.reset();
		this.tipoIdentificacionMap = new ArrayList();
		this.pacienteCargado=false;
		

		this.datosFinanciacion = new DtoDatosFinanciacion();
		this.ingresoFac = ConstantesBD.codigoNuncaValido;
		this.seccionDeudor = ConstantesBD.acronimoSi;
		this.seccionCodeudor = ConstantesBD.acronimoNo;
		
	    this.sufijo="";
		
		/***VARIABLES TEMPORALES DE TRABAJO***/
		this.tipoIdTemp="";
		this.numeroIdTemp="";
		this.facturaBusqueda="";
		
		this.puedoModificarDeudor=false;
		this.puedoModificarCodeudor=false;
		this.consecutivoDocumentoTempo="";
		this.tipoDocGarantiaTempo="";
		this.institucion=ConstantesBD.codigoNuncaValido;
		this.facturas=new ArrayList<DtoFactura>();
		
		this.tipoDocumentoGarantia="";
		this.numeroDocumentoGarantia="";
		this.fechaDocumentoGarantia="";
		this.tipoBusqueda="";
		
		this.tipoID="";
		this.numeroID="";
		this.nombrePersona="";
		this.apellidoPersona="";
		this.documentoInicial="";
		this.documentoFinal="";
		this.convenio="";
		this.convenios = new ArrayList<HashMap<String,Object>>();
		this.saldosIniciales=new ArrayList<DtoSaldosInicialesCarteraPaciente>();
		
		this.codigoPaisTempo="";
		
		this.ciudadesExpDeudor = new ArrayList<HashMap<String,Object>>();
		this.ciudadesExpCodeudor = new ArrayList<HashMap<String,Object>>();
		
		this.paises=new ArrayList<HashMap<String,Object>>();
		
		this.deptoCiudadIDDeudor=ConstantesBD.codigoNuncaValido+ConstantesBD.separadorSplit+ConstantesBD.codigoNuncaValido;
		this.deptoCiudadIDCodeudor=ConstantesBD.codigoNuncaValido+ConstantesBD.separadorSplit+ConstantesBD.codigoNuncaValido;
		
		this.posArray=ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * 
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		if(estado.equals("guardarDatosFinan"))
		{
			//validaciones de la seccion paciente.
			if(!UtilidadTexto.isEmpty(this.getPaciente().getTipoId()))
			{
				if(UtilidadTexto.isEmpty(this.getPaciente().getNumeroId()))
					errores.add("NUMERO ID PACIENTE", new ActionMessage("errors.required","No. Identificacion Paciente"));
				if(UtilidadTexto.isEmpty(this.getPaciente().getPrimerNombre()))
					errores.add("PRIMER NOMBRE PACIENTE", new ActionMessage("errors.required","Primer Nombre Paciente"));
				if(UtilidadTexto.isEmpty(this.getPaciente().getPrimerApellido()))
					errores.add("PRIMER APELLIDO PACIENTE", new ActionMessage("errors.required","Primer apellido Paciente"));
				if(UtilidadTexto.isEmpty(this.getPaciente().getDireccion()))
					errores.add("DIRECCION PACIENTE", new ActionMessage("errors.required","Direccion Paciente"));
				if(UtilidadTexto.isEmpty(this.getPaciente().getTelefono()))
					errores.add("TELEFONO PACIENTE", new ActionMessage("errors.required","Telefono Paciente"));
				
			}
			
			//validaciones de la seccion deudor.
			if(UtilidadTexto.isEmpty(this.datosFinanciacion.getDeudor().getTipoDeudor().trim()))
				errores.add("TIPO DEUDOR", new ActionMessage("errors.required","Tipo Deudor"));
			if(UtilidadTexto.isEmpty(this.datosFinanciacion.getDeudor().getTipoIdentificacion()))
				errores.add("tipo id", new ActionMessage("errors.required","Tipo Identificacion Deudor"));
			if(UtilidadTexto.isEmpty(this.datosFinanciacion.getDeudor().getNumeroIdentificacion().trim()))
				errores.add("num id", new ActionMessage("errors.required","No. Identificacion Deudor"));
			if(UtilidadTexto.isEmpty(this.datosFinanciacion.getDeudor().getPrimerNombre().trim()))
				errores.add("primer nombre", new ActionMessage("errors.required","Primer Nombre Deudor"));
			if(UtilidadTexto.isEmpty(this.datosFinanciacion.getDeudor().getPrimerApellido().trim()))
				errores.add("segundo nombre", new ActionMessage("errors.required","Primer Apellido Deudor"));
			if(UtilidadTexto.isEmpty(this.datosFinanciacion.getDeudor().getDireccion()))
				errores.add("direccion", new ActionMessage("errors.required","Direccion Deudor"));
			if(UtilidadTexto.isEmpty(this.datosFinanciacion.getDeudor().getTelefono()))
				errores.add("telefono", new ActionMessage("errors.required","Telefono Deudor"));
			if(UtilidadTexto.isEmpty(this.datosFinanciacion.getDeudor().getOcupacion()))
				errores.add("ocupacion", new ActionMessage("errors.required","Ocupacion"));
			if(UtilidadTexto.isEmpty(this.datosFinanciacion.getDeudor().getDetalleocupacion()))
				errores.add("detalle ocupacion", new ActionMessage("errors.required","Detalle Ocupacion Deudor"));
			if(!UtilidadTexto.isEmpty(this.datosFinanciacion.getDeudor().getOcupacion())&&this.datosFinanciacion.getDeudor().getOcupacion().equals(ConstantesIntegridadDominio.acronimoTipoTrabajadorEmpleado))
			{
				if(UtilidadTexto.isEmpty(this.datosFinanciacion.getDeudor().getEmpresa()))
					errores.add("empresa", new ActionMessage("errors.required","Empresa Deudor"));
				if(UtilidadTexto.isEmpty(this.datosFinanciacion.getDeudor().getCargo()))
					errores.add("cargo", new ActionMessage("errors.required","Cargo Deudor"));
				if(UtilidadTexto.isEmpty(this.datosFinanciacion.getDeudor().getAntiguedad()))
					errores.add("antiguedad", new ActionMessage("errors.required","Antiguiedad Deudor"));
			}
			if(!UtilidadTexto.isEmpty(this.datosFinanciacion.getDeudor().getOcupacion())&&(this.datosFinanciacion.getDeudor().getOcupacion().equals(ConstantesIntegridadDominio.acronimoTipoTrabajadorEmpleado) || this.datosFinanciacion.getDeudor().getOcupacion().equals(ConstantesIntegridadDominio.acronimoTipoTrabajadorIndependiente)))
			{
				if(UtilidadTexto.isEmpty(this.datosFinanciacion.getDeudor().getDireccionOficina()))
					errores.add("direccion oficina", new ActionMessage("errors.required","Direccion oficina Deudor"));
				if(UtilidadTexto.isEmpty(this.datosFinanciacion.getDeudor().getTelefonoOficina()))
					errores.add("telefono oficina", new ActionMessage("errors.required","Telefono oficina Deudor"));
			}	
			
			//validaciones de la seccion codeudor.
			if(!UtilidadTexto.isEmpty(this.datosFinanciacion.getCodeudor().getTipoDeudor().trim()))
			{
				if(UtilidadTexto.isEmpty(this.datosFinanciacion.getCodeudor().getTipoIdentificacion()))
					errores.add("tipo id", new ActionMessage("errors.required","Tipo Identificacion Codeudor"));
				if(UtilidadTexto.isEmpty(this.datosFinanciacion.getCodeudor().getNumeroIdentificacion().trim()))
					errores.add("num id", new ActionMessage("errors.required","No. Identificacion Codeudor"));
				if(this.datosFinanciacion.getCodeudor().getTipoIdentificacion().equals(this.datosFinanciacion.getDeudor().getTipoIdentificacion())&&this.datosFinanciacion.getCodeudor().getNumeroIdentificacion().equals(this.datosFinanciacion.getDeudor().getNumeroIdentificacion()))
				{
					errores.add("deudor y codeudor repetido", new ActionMessage("error.errorEnBlanco","El Deudor y el Codeudor deben ser diferentes."));
				}
				else
				{
					if(UtilidadTexto.isEmpty(this.datosFinanciacion.getCodeudor().getPrimerNombre().trim()))
						errores.add("primer nombre", new ActionMessage("errors.required","Primer Nombre Codeudor"));
					if(UtilidadTexto.isEmpty(this.datosFinanciacion.getCodeudor().getPrimerApellido().trim()))
						errores.add("segundo nombre", new ActionMessage("errors.required","Primer Apellido Codeudor"));
					if(UtilidadTexto.isEmpty(this.datosFinanciacion.getCodeudor().getDireccion()))
						errores.add("direccion", new ActionMessage("errors.required","Direccion Codeudor"));
					if(UtilidadTexto.isEmpty(this.datosFinanciacion.getCodeudor().getTelefono()))
						errores.add("telefono", new ActionMessage("errors.required","Telefono Codeudor"));
					if(UtilidadTexto.isEmpty(this.datosFinanciacion.getCodeudor().getOcupacion()))
						errores.add("ocupacion", new ActionMessage("errors.required","Ocupacion"));
					if(UtilidadTexto.isEmpty(this.datosFinanciacion.getCodeudor().getDetalleocupacion()))
						errores.add("detalle ocupacion", new ActionMessage("errors.required","Detalle Ocupacion Codeudor"));
					if(!UtilidadTexto.isEmpty(this.datosFinanciacion.getCodeudor().getOcupacion())&&this.datosFinanciacion.getCodeudor().getOcupacion().equals(ConstantesIntegridadDominio.acronimoTipoTrabajadorEmpleado))
					{
						if(UtilidadTexto.isEmpty(this.datosFinanciacion.getCodeudor().getEmpresa()))
							errores.add("empresa", new ActionMessage("errors.required","Empresa Codeudor"));
						if(UtilidadTexto.isEmpty(this.datosFinanciacion.getCodeudor().getCargo()))
							errores.add("cargo", new ActionMessage("errors.required","Cargo Codeudor"));
						if(UtilidadTexto.isEmpty(this.datosFinanciacion.getCodeudor().getAntiguedad()))
							errores.add("antiguedad", new ActionMessage("errors.required","Antiguiedad Codeudor"));
					}
					if(!UtilidadTexto.isEmpty(this.datosFinanciacion.getCodeudor().getOcupacion())&&(this.datosFinanciacion.getCodeudor().getOcupacion().equals(ConstantesIntegridadDominio.acronimoTipoTrabajadorEmpleado) || this.datosFinanciacion.getCodeudor().getOcupacion().equals(ConstantesIntegridadDominio.acronimoTipoTrabajadorIndependiente)))
					{
						if(UtilidadTexto.isEmpty(this.datosFinanciacion.getCodeudor().getDireccionOficina()))
							errores.add("direccion oficina", new ActionMessage("errors.required","Direccion oficina Codeudor"));
						if(UtilidadTexto.isEmpty(this.datosFinanciacion.getCodeudor().getTelefonoOficina()))
							errores.add("telefono oficina", new ActionMessage("errors.required","Telefono oficina Codeudor"));
					}
				}
			}
			
			//validaciones seccion datos financieros
			if(UtilidadTexto.isEmpty(this.datosFinanciacion.getTipoDocumento()))
				errores.add("tipo documento", new ActionMessage("errors.required","Tipo Documento"));
			if(UtilidadTexto.isEmpty(this.datosFinanciacion.getConsecutivo()))
				errores.add("numero documento", new ActionMessage("errors.required","No. Documento"));
			if(!UtilidadTexto.isEmpty(this.datosFinanciacion.getTipoDocumento())&&!UtilidadTexto.isEmpty(this.datosFinanciacion.getConsecutivo()))
			{
				String primerCons=DatosFinanciacion.obtenerPrimerConsecutivoUsadoDocumento(this.datosFinanciacion.getTipoDocumento(),institucion);
				if(Utilidades.convertirAEntero(primerCons)>0&&Utilidades.convertirAEntero(primerCons)<=Utilidades.convertirAEntero(this.datosFinanciacion.getConsecutivo()))
				{
					errores.add("error consecutivo", new ActionMessage("error.errorEnBlanco","El consecutivo es mayor al primer consecutivo usado ("+primerCons+")"));
				}
				if(DatosFinanciacion.esConsecutivoDocumentoUsado(this.datosFinanciacion.getTipoDocumento(),this.datosFinanciacion.getConsecutivo(),this.institucion))
				{
					errores.add("error consecutivo", new ActionMessage("error.errorEnBlanco","El consecutivo ya fue usado"));
				}
			}
			boolean fechaDocValida=true;
			if(!UtilidadFecha.validarFecha(this.datosFinanciacion.getFechaInicio()))
			{
				errores.add("fecha", new ActionMessage("errors.formatoFechaInvalido",this.datosFinanciacion.getFechaInicio()));
				fechaDocValida=false;
			}
			else
			{
				//validaciones con la fecha
				if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.datosFinanciacion.getFechaInicio(),UtilidadFecha.getFechaActual()))
				{
					errores.add("Fecha Mayor del Sistema",new ActionMessage("errors.fechaPosteriorAOtraDeReferencia","del Documento ("+this.datosFinanciacion.getFechaInicio()+")","Actual ("+UtilidadFecha.getFechaActual()+")"));
					fechaDocValida=false;
				}
			}
			if(this.datosFinanciacion.getValorTotalDocumento()<=0)
				errores.add("valor documento", new ActionMessage("errors.required","Valor Total Documento"));
			if(Utilidades.convertirADouble(this.datosFinanciacion.getConsecutivoFactura())<=0)
				errores.add("CONSECUTIVO FACTURA", new ActionMessage("errors.required","Factura"));
			
			///validaciones de las cutos
			if(fechaDocValida)
			{
				double valorTotalCuotas=0;
				for(int i=0;i<this.datosFinanciacion.getCuotasDatosFinan().size();i++)
				{
					if(UtilidadTexto.isEmpty(this.datosFinanciacion.getCuotasDatosFinan().get(i).getNumeroDocumento()))
						errores.add("doc cuota", new ActionMessage("errors.required","Documento de la Cuota Nro "+(i+1)));
					
					if(!UtilidadFecha.validarFecha(this.datosFinanciacion.getCuotasDatosFinan().get(i).getFechaVigencia()))
					{
						errores.add("fecha", new ActionMessage("errors.formatoFechaInvalido",this.datosFinanciacion.getCuotasDatosFinan().get(i).getFechaVigencia()));
					}
					else
					{
						//validaciones con la fecha
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.datosFinanciacion.getCuotasDatosFinan().get(i).getFechaVigencia(),UtilidadFecha.getFechaActual()))
						{
							errores.add("Fecha Mayor del Sistema",new ActionMessage("errors.fechaPosteriorAOtraDeReferencia","de Cuota Nro "+(i+1)+" ("+this.datosFinanciacion.getCuotasDatosFinan().get(i).getFechaVigencia()+")","Actual ("+UtilidadFecha.getFechaActual()+")"));
						}
						if(UtilidadFecha.esFechaMenorQueOtraReferencia(this.datosFinanciacion.getCuotasDatosFinan().get(i).getFechaVigencia(),this.datosFinanciacion.getFechaInicio()))
						{
							errores.add("Fecha menor al documento",new ActionMessage("errors.fechaAnteriorAOtraDeReferencia","de Cuota Nro "+(i+1)+" ("+this.datosFinanciacion.getCuotasDatosFinan().get(i).getFechaVigencia()+")","Documento ("+this.datosFinanciacion.getFechaInicio()+")"));
						}
					}
					for(int j=0;j<i;j++)
					{
						if(!UtilidadTexto.isEmpty(this.datosFinanciacion.getCuotasDatosFinan().get(i).getFechaVigencia())&&!UtilidadTexto.isEmpty(this.datosFinanciacion.getCuotasDatosFinan().get(j).getFechaVigencia()))
						{
							if(this.datosFinanciacion.getCuotasDatosFinan().get(i).getFechaVigencia().equals(this.datosFinanciacion.getCuotasDatosFinan().get(j).getFechaVigencia()))
							{
								errores.add("fecha repetida", new ActionMessage("error.errorEnBlanco","Fechas de cuotas repetidas"));
								j=i;
							}
							if(UtilidadFecha.esFechaMenorQueOtraReferencia(this.datosFinanciacion.getCuotasDatosFinan().get(i).getFechaVigencia(),this.datosFinanciacion.getCuotasDatosFinan().get(j).getFechaVigencia()))
							{
								errores.add("fecha secuencial", new ActionMessage("error.errorEnBlanco","Cada fecha de vencimiento de las cuotas, debe ser mayor a la inmediatamente anterior."));
								j=i;
							}
						}
					}
					if(this.datosFinanciacion.getCuotasDatosFinan().get(i).getValorCuota().doubleValue()<=0)
					{
						errores.add("valor cuota mayor 0", new ActionMessage("errors.MayorQue","El valor de la Cuota Nro. "+(i+1),"0"));
					}
					valorTotalCuotas=valorTotalCuotas+this.datosFinanciacion.getCuotasDatosFinan().get(i).getValorCuota().doubleValue();
				}
				if(valorTotalCuotas!=this.datosFinanciacion.getValorTotalDocumento())
				{
					errores.add("valor cuotas diferente valor doc", new ActionMessage("error.errorEnBlanco","El valor de todas las cuotas, debe ser igual a valor total del documento."));
				}
			}
		}
		return errores;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getFacturaBusqueda() {
		return facturaBusqueda;
	}

	/**
	 * 
	 * @param facturaBusqueda
	 */
	public void setFacturaBusqueda(String facturaBusqueda) {
		this.facturaBusqueda = facturaBusqueda;
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
	 * @return the mensaje
	 */
	public ResultadoBoolean getMensaje() {
		return mensaje;
	}


	/**
	 * @param mensaje the mensaje to set
	 */
	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
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
	 * @return the tipoIdentificacionMap
	 */
	public ArrayList getTipoIdentificacionMap() {
		return tipoIdentificacionMap;
	}


	/**
	 * @param tipoIdentificacionMap the tipoIdentificacionMap to set
	 */
	public void setTipoIdentificacionMap(ArrayList tipoIdentificacionMap) {
		this.tipoIdentificacionMap = tipoIdentificacionMap;
	}


	/**
	 * @return the pacienteCargado
	 */
	public boolean isPacienteCargado() {
		return pacienteCargado;
	}


	/**
	 * @param pacienteCargado the pacienteCargado to set
	 */
	public void setPacienteCargado(boolean pacienteCargado) {
		this.pacienteCargado = pacienteCargado;
	}


	/**
	 * @return the tipoIdTemp
	 */
	public String getTipoIdTemp() {
		return tipoIdTemp;
	}


	/**
	 * @param tipoIdTemp the tipoIdTemp to set
	 */
	public void setTipoIdTemp(String tipoIdTemp) {
		this.tipoIdTemp = tipoIdTemp;
	}


	/**
	 * @return the numeroIdTemp
	 */
	public String getNumeroIdTemp() {
		return numeroIdTemp;
	}


	/**
	 * @param numeroIdTemp the numeroIdTemp to set
	 */
	public void setNumeroIdTemp(String numeroIdTemp) {
		this.numeroIdTemp = numeroIdTemp;
	}


	/**
	 * @return the seccionDeudor
	 */
	public String getSeccionDeudor() {
		return seccionDeudor;
	}


	/**
	 * @param seccionDeudor the seccionDeudor to set
	 */
	public void setSeccionDeudor(String seccionDeudor) {
		this.seccionDeudor = seccionDeudor;
	}


	/**
	 * @return the seccionCodeudor
	 */
	public String getSeccionCodeudor() {
		return seccionCodeudor;
	}


	/**
	 * @param seccionCodeudor the seccionCodeudor to set
	 */
	public void setSeccionCodeudor(String seccionCodeudor) {
		this.seccionCodeudor = seccionCodeudor;
	}

	/**
	 * @return the datosFinanciacion
	 */
	public DtoDatosFinanciacion getDatosFinanciacion() {
		return datosFinanciacion;
	}

	/**
	 * @param datosFinanciacion the datosFinanciacion to set
	 */
	public void setDatosFinanciacion(DtoDatosFinanciacion datosFinanciacion) {
		this.datosFinanciacion = datosFinanciacion;
	}

	/**
	 * @return the ingresoFac
	 */
	public int getIngresoFac() {
		return ingresoFac;
	}

	/**
	 * @param ingresoFac the ingresoFac to set
	 */
	public void setIngresoFac(int ingresoFac) {
		this.ingresoFac = ingresoFac;
	}

	public int getNumeroFacturasResultadoBusqueda()
	{
		return this.facturas.size();
	}

	/**
	 * @return the tipoID
	 */
	public String getTipoID() {
		return tipoID;
	}

	/**
	 * @param tipoID the tipoID to set
	 */
	public void setTipoID(String tipoID) {
		this.tipoID = tipoID;
	}

	/**
	 * @return the numeroID
	 */
	public String getNumeroID() {
		return numeroID;
	}

	/**
	 * @param numeroID the numeroID to set
	 */
	public void setNumeroID(String numeroID) {
		this.numeroID = numeroID;
	}
	
	/**
	 * @return the nombrePersona
	 */
	public String getNombrePersona() {
		return nombrePersona;
	}

	/**
	 * @param nombrePersona the nombrePersona to set
	 */
	public void setNombrePersona(String nombrePersona) {
		this.nombrePersona = nombrePersona;
	}
	
	/**
	 * @return the apellidoPersona
	 */
	public String getApellidoPersona() {
		return apellidoPersona;
	}

	/**
	 * @param apellidoPersona the apellidoPersona to set
	 */
	public void setApellidoPersona(String apellidoPersona) {
		this.apellidoPersona = apellidoPersona;
	}

	/**
	 * @return the documentoInicial
	 */
	public String getDocumentoInicial() {
		return documentoInicial;
	}

	/**
	 * @param documentoInicial the documentoInicial to set
	 */
	public void setDocumentoInicial(String documentoInicial) {
		this.documentoInicial = documentoInicial;
	}

	/**
	 * @return the documentoFinal
	 */
	public String getDocumentoFinal() {
		return documentoFinal;
	}

	/**
	 * @param documentoFinal the documentoFinal to set
	 */
	public void setDocumentoFinal(String documentoFinal) {
		this.documentoFinal = documentoFinal;
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
	 * @return the posArray
	 */
	public int getPosArray() {
		return posArray;
	}

	/**
	 * @param posArray the posArray to set
	 */
	public void setPosArray(int posArray) {
		this.posArray = posArray;
	}
	
	/**
	 * @return the paises
	 */
	public ArrayList<HashMap<String, Object>> getPaises() {
		return paises;
	}



	/**
	 * @param paises the paises to set
	 */
	public void setPaises(ArrayList<HashMap<String, Object>> paises) {
		this.paises = paises;
	}
	
	/**
	 * @return the codigoPaisTempo
	 */
	public String getCodigoPaisTempo() {
		return codigoPaisTempo;
	}

	/**
	 * @param codigoPaisTempo the codigoPaisTempo to set
	 */
	public void setCodigoPaisTempo(String codigoPaisTempo) {
		this.codigoPaisTempo = codigoPaisTempo;
	}

	/**
	 * @return the saldosIniciales
	 */
	public ArrayList<DtoSaldosInicialesCarteraPaciente> getSaldosIniciales() {
		return saldosIniciales;
	}

	/**
	 * @param saldosIniciales the saldosIniciales to set
	 */
	public void setSaldosIniciales(
			ArrayList<DtoSaldosInicialesCarteraPaciente> saldosIniciales) {
		this.saldosIniciales = saldosIniciales;
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

	public String getTipoBusqueda() {
		return tipoBusqueda;
	}

	public void setTipoBusqueda(String tipoBusqueda) {
		this.tipoBusqueda = tipoBusqueda;
	}

	public String getTipoDocumentoGarantia() {
		return tipoDocumentoGarantia;
	}

	public void setTipoDocumentoGarantia(String tipoDocumentoGarantia) {
		this.tipoDocumentoGarantia = tipoDocumentoGarantia;
	}

	public String getNumeroDocumentoGarantia() {
		return numeroDocumentoGarantia;
	}

	public void setNumeroDocumentoGarantia(String numeroDocumentoGarantia) {
		this.numeroDocumentoGarantia = numeroDocumentoGarantia;
	}

	public String getFechaDocumentoGarantia() {
		return fechaDocumentoGarantia;
	}

	public void setFechaDocumentoGarantia(String fechaDocumentoGarantia) {
		this.fechaDocumentoGarantia = fechaDocumentoGarantia;
	}

	public ArrayList<DtoFactura> getFacturas() {
		return facturas;
	}

	public void setFacturas(ArrayList<DtoFactura> facturas) {
		this.facturas = facturas;
	}

	public String getTipoDocGarantiaTempo() {
		return tipoDocGarantiaTempo;
	}

	public void setTipoDocGarantiaTempo(String tipoDocGarantiaTempo) {
		this.tipoDocGarantiaTempo = tipoDocGarantiaTempo;
	}

	public String getConsecutivoDocumentoTempo() {
		return consecutivoDocumentoTempo;
	}

	public void setConsecutivoDocumentoTempo(String consecutivoDocumentoTempo) {
		this.consecutivoDocumentoTempo = consecutivoDocumentoTempo;
	}

	public boolean isPuedoModificarDeudor() {
		return puedoModificarDeudor;
	}

	public void setPuedoModificarDeudor(boolean puedoModificarDeudor) {
		this.puedoModificarDeudor = puedoModificarDeudor;
	}

	public boolean isPuedoModificarCodeudor() {
		return puedoModificarCodeudor;
	}

	public void setPuedoModificarCodeudor(boolean puedoModificarCodeudor) {
		this.puedoModificarCodeudor = puedoModificarCodeudor;
	}
	
	/**
	 * @return the deptoCiudadIDDeudor
	 */
	public String getDeptoCiudadIDDeudor() {
		return deptoCiudadIDDeudor;
	}

	/**
	 * @param deptoCiudadIDDeudor the deptoCiudadIDDeudor to set
	 */
	public void setDeptoCiudadIDDeudor(String deptoCiudadIDDeudor) {
		this.deptoCiudadIDDeudor = deptoCiudadIDDeudor;
	}
	
	/**
	 * @return the deptoCiudadIDCodeudor
	 */
	public String getDeptoCiudadIDCodeudor() {
		return deptoCiudadIDCodeudor;
	}
	
	/**
	 * @param deptoCiudadIDCodeudor the deptoCiudadIDCodeudor to set
	 */
	public void setDeptoCiudadIDCodeudor(String deptoCiudadIDCodeudor) {
		this.deptoCiudadIDCodeudor = deptoCiudadIDCodeudor;
	}

	/**
	 * @return the ciudadesExpDeudor
	 */
	public ArrayList<HashMap<String, Object>> getCiudadesExpDeudor() {
		return ciudadesExpDeudor;
	}

	/**
	 * @return the sufijo
	 */
	public String getSufijo() {
		return sufijo;
	}

	/**
	 * @param sufijo the sufijo to set
	 */
	public void setSufijo(String sufijo) {
		this.sufijo = sufijo;
	}

	/**
	 * @param ciudadesExpDeudor the ciudadesExpDeudor to set
	 */
	public void setCiudadesExpDeudor(
			ArrayList<HashMap<String, Object>> ciudadesExpDeudor) {
		this.ciudadesExpDeudor = ciudadesExpDeudor;
	}

	/**
	 * @return the ciudadesExpCodeudor
	 */
	public ArrayList<HashMap<String, Object>> getCiudadesExpCodeudor() {
		return ciudadesExpCodeudor;
	}

	/**
	 * @param ciudadesExpCodeudor the ciudadesExpCodeudor to set
	 */
	public void setCiudadesExpCodeudor(
			ArrayList<HashMap<String, Object>> ciudadesExpCodeudor) {
		this.ciudadesExpCodeudor = ciudadesExpCodeudor;
	}

}
