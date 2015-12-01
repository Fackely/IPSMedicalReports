package com.princetonsa.mundo.carteraPaciente;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.reportes.ConsultasBirt;

import com.princetonsa.actionform.carteraPaciente.ReportePagosCarteraPacienteForm;
import com.princetonsa.actionform.glosas.ConsultarImpFacAudiForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.carterapaciente.ReportePagosCarteraPacienteDao;
import com.princetonsa.dao.odontologia.MotivosAtencionOdontologicaDao;
import com.princetonsa.dto.carteraPaciente.DtoAplicacPagosCarteraPac;
import com.princetonsa.dto.carteraPaciente.DtoDatosFinanciacion;
import com.princetonsa.mundo.UsuarioBasico;


public class ReportePagosCarteraPaciente
{
	static Logger logger = Logger.getLogger(ReportePagosCarteraPaciente.class);
	
	private static ReportePagosCarteraPacienteDao getReportePagosCarteraPacienteDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getReportePagosCarteraPacienteDao();
	}

	public ArrayList<DtoDatosFinanciacion> consultarDocumentos(int centroAtencion, String tipoDoc) {
		return getReportePagosCarteraPacienteDao().consultarDocumentos(centroAtencion, tipoDoc);
	}

	public HashMap consultarAplicPagos(int codigoPk, String fechaIni, String fechaFin, int anioIni, int anioFin, String tipoPeriodo) {
		
		return getReportePagosCarteraPacienteDao().consultarAplicPagos(codigoPk, fechaIni, fechaFin, anioIni, anioFin, tipoPeriodo);
	}
	
	public static String plano (Connection connection, ReportePagosCarteraPacienteForm forma,UsuarioBasico usuario )
	{
		ArrayList<DtoDatosFinanciacion> tmp = new ArrayList<DtoDatosFinanciacion>();
		String planoStr="";
		
		 String newquery = ConsultasBirt.reportePagosCarteraPaciente(
					UtilidadFecha.conversionFormatoFechaABD(forma.getFechaIni()), 
					UtilidadFecha.conversionFormatoFechaABD(forma.getFechaFin()), 
					forma.getAnioIni(),
					forma.getAnioFin(),
					forma.getCentroAtencion(), 
					forma.getTipoDoc(), 
					forma.getTipoPeriodo());
		 
	 logger.info("Consulta: "+newquery);
	 
	 tmp= getReportePagosCarteraPacienteDao().ejecutarConsulta(newquery);
	 
	 String filtro1="";
	 String encabezado="";
	 
	 if(forma.getTipoDoc().equals(ConstantesIntegridadDominio.acronimoTipoDocumentoLetra))
		 filtro1="Tipo Documento: Letra ";
	 else if(forma.getTipoDoc().equals(ConstantesIntegridadDominio.acronimoTipoDocumentoPagare))
		 filtro1="Tipo Documento: Pagare ";
	 	 
	 if(forma.getTipoPeriodo().equals(ConstantesIntegridadDominio.acronimoTipoPeriodoMensual))
		 filtro1="Tipo Periodo: Mensual Fecha Inicial: "+forma.getFechaIni()+" Fecha Fin: "+forma.getFechaFin();
	 else if(forma.getTipoPeriodo().equals(ConstantesIntegridadDominio.acronimoTipoPeriodoAnual))
		 filtro1="Tipo Periodo: Anual Año Inicial: "+forma.getAnioIni()+" Año Fin: "+forma.getAnioFin();
	 
	 encabezado += "Tipo Doc, Código Garantía, Fecha Gen., Centro Atención, Deudor, Valor, Detalle Pagos \n";
	 		 
	 boolean existe=false; 
			 	  
	  forma.setCriteriosConsulta(filtro1);			  
	  
	  planoStr+="Reporte Pagos Cartera Paciente "+",";
	  planoStr+="Criterios:"+",";
	  planoStr+=forma.getCriteriosConsulta()+"\n\n";
	  planoStr+="Fecha de Generación:"+",";
	  planoStr+=UtilidadFecha.getFechaActual()+"\n\n";
	  planoStr+=encabezado+"\n\n";
	  for(int i=0;i<(tmp.size());i++)
		  planoStr+=tmp.get(i).getTipoDocumento()+","+tmp.get(i).getConsecutivo()+","+tmp.get(i).getDocumentoGarantia().getFechaGen()+","+tmp.get(i).getDocumentoGarantia().getCentroAtencion()+","+tmp.get(i).getDeudor().getPrimerNombre()+","+tmp.get(i).getDocumentoGarantia().getValor()+","+tmp.get(i).getDocumentoGarantia().getCartera()+"\n";
	  
	  return planoStr;
	}
}