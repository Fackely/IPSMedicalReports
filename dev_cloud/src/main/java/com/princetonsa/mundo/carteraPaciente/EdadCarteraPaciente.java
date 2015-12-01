package com.princetonsa.mundo.carteraPaciente;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosDouble;
import util.InfoDatosInt;
import util.InfoDatosStr;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.carterapaciente.EdadCarteraPacienteDao;
import com.princetonsa.dto.carteraPaciente.DtoCuotasDatosFinanciacion;
import com.princetonsa.dto.carteraPaciente.DtoDatosFinanciacion;
import com.princetonsa.dto.carteraPaciente.DtoReporteEdadCarteraPaciente;

public class EdadCarteraPaciente {
	
	static Logger logger = Logger.getLogger(EdadCarteraPaciente.class);
	
	public static EdadCarteraPacienteDao getDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEdadCarteraPacienteDao();
	}
	
	/**
	 * Consulta listado de edad glosa
	 * @param Connection con
	 * @param String fechaCorte
	 * @param String tipoDocumento
	 * */
	public static ArrayList<DtoDatosFinanciacion> getListadoEdadGlosa(Connection con,String fechaCorte,String tipoDocumento,String centroAtencion)
	{
		HashMap parametros = new HashMap();
				
		parametros.put("tipoDocumento",tipoDocumento);
		parametros.put("fechaCorte",fechaCorte);
		parametros.put("centroAtencion",centroAtencion);
		return getDao().getListadoEdadGlosa(con, parametros);
	}
	
	/**
	 * Validar Datos Busqueda
	 * @param String fechaCorte
	 * @param String tipoDocumento
	 * */
	public static ActionErrors validarDatosBusqueda(String fechaCorte,String tipoDocumento)
	{
		ActionErrors errores = new ActionErrors();
		
		if(fechaCorte.equals(""))
			errores.add("descripcion",new ActionMessage("errors.required","La Fecha de Corte "));
		else
		{
			if(!UtilidadFecha.validarFecha(fechaCorte))
			{
				errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido","La Fecha de Corte "+fechaCorte));
			}
			else
			{
				if(!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(),"00:00",fechaCorte,"00:00").isTrue())
				{
					errores.add("descripcion",new ActionMessage("errors.fechaPosteriorAOtraDeReferencia","La Fecha de Corte "+fechaCorte,"La Fecha Actual "+UtilidadFecha.getFechaActual()));
				}
			}
		}
		
		if(tipoDocumento.equals(""))
		{
			errores.add("descripcion",new ActionMessage("errors.required","El Tipo de Documento "));
		}
		
		return errores;
	}
	
	/**
	 * Calcula el valor de dias de vencimiento
	 * @param String fechaInicial
	 * @param String fechaFinal
	 * @param int dias
	 * */
	public static int calcularDiasVencimiento(String fechaInicial,String fechaCorte,int dias,int numCuota)
	{
		int valor = ConstantesBD.codigoNuncaValido;
		String fecha = UtilidadFecha.incrementarDiasAFecha(UtilidadFecha.conversionFormatoFechaABD(fechaInicial),(dias*numCuota),true);
		fecha = UtilidadFecha.conversionFormatoFechaAAp(fecha);
		
		if(UtilidadFecha.compararFechas(fecha,"00:00",fechaCorte,"00:00").isTrue())
			return 0;
		else
			valor = UtilidadFecha.numeroDiasEntreFechas(fecha,fechaCorte);
		
		return valor;
	}
	
	/**
	 * Arma en las estructuras internas el reporte
	 * */
	public static DtoReporteEdadCarteraPaciente construirReporte(
			DtoReporteEdadCarteraPaciente dtoReporte,
			ArrayList<DtoDatosFinanciacion> arrayDatosOrig)
	{
		//inicializa los datos
		dtoReporte.setArrayDatos(new ArrayList<DtoDatosFinanciacion>());
		dtoReporte.setArrayTotalColumnas(new ArrayList<InfoDatosDouble>());
		
		
		String aux = "";
		int pos = 0, tamano = 0 ;
		tamano = dtoReporte.getArrayRangos().size();
		
		//Carga el array para los totales
		ArrayList<DtoCuotasDatosFinanciacion> arrayTtalTipoDoc = new ArrayList<DtoCuotasDatosFinanciacion>();
		ArrayList<DtoCuotasDatosFinanciacion> arrayTotal = new ArrayList<DtoCuotasDatosFinanciacion>();
		for(int i = 0; i<tamano+2; i++)
		{
			arrayTotal.add(new DtoCuotasDatosFinanciacion());
			arrayTtalTipoDoc.add(new DtoCuotasDatosFinanciacion());	
		}
		
		for(DtoDatosFinanciacion dtoDatosOri : arrayDatosOrig)
		{
			if(dtoDatosOri.getIsNuevoDoc().equals(ConstantesBD.acronimoSi))
			{
				//evalua los cambios en tipo de documento
				if(!aux.equals(dtoDatosOri.getTipoDocumento()))
				{	
					if(!aux.equals(""))
					{
						DtoDatosFinanciacion dtoDatos = new DtoDatosFinanciacion();
						dtoDatos.setObservaciones(ConstantesIntegridadDominio.acronimoSubTotal);
						dtoDatos.setTipoDocumento("TOTAL "+ValoresPorDefecto.getIntegridadDominio(aux).toString().toLowerCase());
						dtoDatos.setNombreCentroAtenDocGaran("");
						dtoDatos.setConsecutivo("");
						dtoDatos.setAnioConsecutivo("");
						dtoDatos.setCuotasDatosFinan(arrayTtalTipoDoc);
						dtoReporte.getArrayDatos().add(dtoDatos);
					}
					
					aux = dtoDatosOri.getTipoDocumento();
					arrayTtalTipoDoc = new ArrayList<DtoCuotasDatosFinanciacion>();
					for(int i = 0; i<tamano+2; i++)
						arrayTtalTipoDoc.add(new DtoCuotasDatosFinanciacion());	
				}
				
				DtoDatosFinanciacion dtoDatos = new DtoDatosFinanciacion();
				dtoDatos.setTipoDocumento(dtoDatosOri.getTipoDocumento());
				dtoDatos.setNombreCentroAtenDocGaran(dtoDatosOri.getNombreCentroAtenDocGaran());
				dtoDatos.setConsecutivo(dtoDatosOri.getConsecutivo());
				dtoDatos.setAnioConsecutivo(dtoDatosOri.getAnioConsecutivo());
				//los dos ultimos valores es para las colunmas de totales
				
				ArrayList<DtoCuotasDatosFinanciacion> array = new ArrayList<DtoCuotasDatosFinanciacion>();
				for(int i = 0; i<tamano+2; i++)
					array.add(new DtoCuotasDatosFinanciacion());
				
				dtoDatos.setCuotasDatosFinan(array);
				
				//Busca los documentos que posee el mismo tipo de documento y mismo numero de consecutivo
				for(DtoDatosFinanciacion dtoDatosOriSub : arrayDatosOrig)
				{
					if(dtoDatosOriSub.getIsNuevoDoc().equals(ConstantesBD.acronimoSi) && 
							dtoDatosOri.getTipoDocumento().equals(dtoDatosOriSub.getTipoDocumento()) && 
								dtoDatosOri.getConsecutivo().equals(dtoDatosOriSub.getConsecutivo()) && 
									dtoDatosOri.getAnioConsecutivo().equals(dtoDatosOriSub.getAnioConsecutivo()))
					{
						//posicion dentro del rango de edades
						pos = getPosRangoEdad(dtoDatosOriSub.getDiasVencimiento(),dtoReporte.getArrayRangos());
						dtoDatos.getCuotasDatosFinan().get(pos).setValorCuota(dtoDatos.getCuotasDatosFinan().get(pos).getValorCuota().add(new BigDecimal(dtoDatosOriSub.getValorSaldo())));
						dtoDatosOriSub.setIsNuevoDoc(ConstantesBD.acronimoNo);
						dtoDatosOri.setIsNuevoDoc(ConstantesBD.acronimoNo);
						
						//Valores de las dos ultimas columnas de la tabla
						if(pos>0)
						{
							dtoDatos.getCuotasDatosFinan().get((tamano+2)-1).setValorCuota(dtoDatos.getCuotasDatosFinan().get((tamano+2)-1).getValorCuota().add(new BigDecimal(dtoDatosOriSub.getValorSaldo())));
							arrayTotal.get((tamano+2)-1).setValorCuota(arrayTotal.get((tamano+2)-1).getValorCuota().add(new BigDecimal(dtoDatosOriSub.getValorSaldo())));
							arrayTtalTipoDoc.get((tamano+2)-1).setValorCuota(arrayTtalTipoDoc.get((tamano+2)-1).getValorCuota().add(new BigDecimal(dtoDatosOriSub.getValorSaldo())));
						}
						else
						{
							dtoDatos.getCuotasDatosFinan().get((tamano+2)-2).setValorCuota(dtoDatos.getCuotasDatosFinan().get((tamano+2)-2).getValorCuota().add(new BigDecimal(dtoDatosOriSub.getValorSaldo())));
							arrayTotal.get((tamano+2)-2).setValorCuota(arrayTotal.get((tamano+2)-2).getValorCuota().add(new BigDecimal(dtoDatosOriSub.getValorSaldo())));
							arrayTtalTipoDoc.get((tamano+2)-2).setValorCuota(arrayTtalTipoDoc.get((tamano+2)-2).getValorCuota().add(new BigDecimal(dtoDatosOriSub.getValorSaldo())));
						}
						
						//Suma los totales de la columnas
						arrayTotal.get(pos).setValorCuota(arrayTotal.get(pos).getValorCuota().add(new BigDecimal(dtoDatosOriSub.getValorSaldo())));
						arrayTtalTipoDoc.get(pos).setValorCuota(arrayTtalTipoDoc.get(pos).getValorCuota().add(new BigDecimal(dtoDatosOriSub.getValorSaldo())));
					}
				}
				
				dtoReporte.getArrayDatos().add(dtoDatos);
			}
		}
		
		DtoDatosFinanciacion dtoDatos = new DtoDatosFinanciacion();
		dtoDatos.setObservaciones(ConstantesIntegridadDominio.acronimoSubTotal);
		dtoDatos.setTipoDocumento("Total "+ValoresPorDefecto.getIntegridadDominio(aux).toString().toLowerCase());
		dtoDatos.setNombreCentroAtenDocGaran("");
		dtoDatos.setConsecutivo("");
		dtoDatos.setAnioConsecutivo("");
		dtoDatos.setCuotasDatosFinan(arrayTtalTipoDoc);
		dtoReporte.getArrayDatos().add(dtoDatos);
		
		//Adiciona la linea del final 
		dtoDatos = new DtoDatosFinanciacion();
		dtoDatos.setObservaciones(ConstantesIntegridadDominio.acronimoTotal);
		dtoDatos.setTipoDocumento("TOTAL GENERAL");
		dtoDatos.setNombreCentroAtenDocGaran("");
		dtoDatos.setConsecutivo("");
		dtoDatos.setAnioConsecutivo("");
		dtoDatos.setCuotasDatosFinan(arrayTotal);
		dtoReporte.getArrayDatos().add(dtoDatos);
			
		return dtoReporte;
	}
	
	/**
	 * obtiene la posicion del valor dentro del rango
	 * @param int edad
	 * @param ArrayList<InfoDatosInt> rangos 
	 * */
	public static int getPosRangoEdad(int edad,ArrayList<InfoDatosInt> rangos)
	{
		int aux = 0, cont = 0;
		for(InfoDatosInt rango : rangos)
		{
			if((edad >= rango.getCodigo() && edad <= rango.getCodigo2()) 
					|| rango.getCodigo2() < 0)
				return cont;
			
			cont++;
		}
		
		return ConstantesBD.codigoNuncaValido;
	}
}