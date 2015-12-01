

/*
 * Created on 13-ene-2005
 *
 * Jorge Armando Osorio Velasquez
 * Princeton
 */
package com.princetonsa.pdf;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.log4j.Logger;


import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadN2T;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.pdf.PdfReports;
import util.pdf.iTextBaseDocument;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.Articulo;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.Medico;
import com.princetonsa.mundo.PersonaBasica;

import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.solicitudes.ArticuloSolicitudMedicamentos;
import com.princetonsa.mundo.solicitudes.Solicitud;
import com.princetonsa.mundo.solicitudes.SolicitudMedicamentos;

/**
 * @author armando
 *
 * Princeton 13-ene-2005
 */
public class SolicitudMedicamentosPdf 
{
	/**
     * Clase para manejar los logs de esta clase
     */
    private static Logger logger=Logger.getLogger(SolicitudMedicamentosPdf.class);
    
    /**
     * Constate para manejar el tamanio de los datos del paciente
     */
    private static final int tamanioLetraCabeceraPaciente=10;
    
    /**
     * Constate para manejar el tamanio de los datos de la cabecera.
     */
    private static final int tamanioLetraCabecera=9;
    
    /**
     * Constaten para manejar el tamanio de la tabla de medicamentos
     */
    private static final int tamanioLetraDatosTablaMedicamento=8;
    
    /**
     * Constaten para manejar el tamanio de la tabla de medicamentos
     */
    private static final int tamanioLetraCabeceraTablaMedicamento=8;
    /**
     * Constate para manejar el tamanio de los datos de parrafos
     */
    private static final int tamanioLetraParrafo=9;

    /**
     * Numero de columnas de la tabla medicamentos.
     */
    private static final int columnasTablaMedicamentos=9;
    

	/**
	 * Metodo para generar el PDF de solicitud de medicamentos.
	 * @param filename Nombre del archivo con el que se desea generar el PDF
	 * @param usuario, Medico que genera el reporte
	 * @param persona, Paciente
	 * @param con, conexion a la BD
	 */
	public static void pdfSolicitudMedicamentos(String filename, SolicitudMedicamentos mundo, UsuarioBasico usuario,PersonaBasica persona,Connection con, SolicitudMedicamentos mundoMDD) 
	{
		//reporte
		PdfReports report = new PdfReports();
		
		//Datos de la cabecera
		String[] cabecera = new String[16];
		
		//Cargar los datos de la solicitud.
		//Solicitud solicitud=new Solicitud();
		try 
		{
			//solicitud.cargar(con,mundo.getNumeroSolicitud());
			logger.info("NUM SOLICITUD 1-->"+mundo.getNumeroSolicitud());
			mundo.cargarSolicitudMedicamentos(con, usuario.getCodigoInstitucionInt());
			persona.cargarEmpresaRegimen(con,persona.getCodigoPersona(),persona.getCodigoTipoRegimen()+"");
			if(mundoMDD.getNumeroSolicitud()>0)
			{
				mundoMDD.cargarSolicitudMedicamentos(con, usuario.getCodigoInstitucionInt());
				
				logger.info("\n\n\n*****************************************************************************************************************************************");
		    	logger.info("MUNDO Numero solicitud MDD-->"+mundoMDD.getNumeroSolicitud());
		    	logger.info("*****************************************************************************************************************************************\n\n\n");
			}
	    	
		}
		catch (SQLException e) 
		{
			logger.error("Error cargando la solicitud",e);
		}
		//poblar datos segunda cabecera-*********************************************************************
		if (mundo.getControlEspecial().equals("S"))
		{ String local = "SELECT c.descripcion as ciudad, d.descripcion as depto FROM administracion.ciudades c INNER JOIN administracion.departamentos d ON (d.codigo_departamento=c.codigo_departamento)";
		local+= " WHERE c.codigo_ciudad=? AND c.codigo_departamento= ?";
		String ciu="";
		String dep="";
		try {
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(local,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,persona.getCodigoCiudadVivienda());
			pst.setString(2, persona.getCodigoDeptoVivienda());
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			rs.next();
			ciu=rs.getString(1);
			dep=rs.getString(2);
			pst.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			cabecera[0] = "EDAD: "+persona.getEdadDetallada()+ ", SEXO: " + persona.getSexo();
			cabecera[1] = "";
			cabecera[2] = "N° ORDEN:  "+mundo.getConsecutivoOrdenesMedicas();
			cabecera[3] = "";
			cabecera[4] = "FECHA Y HORA DE ORDEN: "+mundo.getFechaSolicitud()+" - "+mundo.getHoraSolicitud();
			cabecera[5] = "DIRECCION: "+ persona.getDireccion();
			cabecera[6] = "TELEFONO: "+ persona.getTelefono();
			cabecera[7] = "CIUDAD: "+ciu ; 
			cabecera[8] =  "DEPARTAMENTO: "+ dep; 
			cabecera[9] = "NOMBRE ENTIDAD: "+ persona.getConvenioPersonaResponsable();
			cabecera[10] = "AFILIACION AL S.G.S.S.S: "+persona.getNombreTipoRegimen();
			cabecera[11] ="";
			
		}
		else
		{
		cabecera[0] = "EDAD: "+persona.getEdadDetallada();
		cabecera[1] = "VïÁ DE INGRESO: "+persona.getUltimaViaIngreso();
		cabecera[2] = "CAMA: "+persona.getCama();
		cabecera[3] = "ÁREA: "+persona.getArea();
		cabecera[4] = "RÉGIMEN: "+persona.getNombreTipoRegimen();
		cabecera[5] = "RESPONSABLE: "+persona.getConvenioPersonaResponsable();
		cabecera[6] = "";
		
		/*		 
		if(!mundo.getNumeroAutorizacion().equals(""))
			cabecera[6] = "AUTORIZACIï¿½N: "+mundo.getNumeroAutorizacion();
		else
			cabecera[6] = "";
		*/
		cabecera[7] = "CENTRO SOLICITANTE: "+mundo.getCentroCostoSolicitante().getNombre();
		cabecera[8] = "FARMACIA: "+mundo.getCentroCostoSolicitado().getNombre();
		cabecera[9] = "FECHA Y HORA DE ORDEN: "+mundo.getFechaSolicitud()+" - "+mundo.getHoraSolicitud();
		cabecera[10] = "FECHA Y HORA DE GRABACIï¿½N: "+mundo.getFechaGrabacion()+" - "+mundo.getHoraGrabacion();
		cabecera[11] = "N° ORDEN:  "+mundo.getConsecutivoOrdenesMedicas();
		}
		if(mundoMDD.getNumeroSolicitud()>0)
		{
			cabecera[11]+=" - "+mundoMDD.getConsecutivoOrdenesMedicas();
		}
		
		if(mundo.getUrgente())
			cabecera[12] = "PRIORIDAD: Urgente";
		else
			cabecera[12] = "PRIORIDAD: Ninguna";
		cabecera[13] = "ESTADO: "+mundo.getEstadoHistoriaClinica().getNombre();
		if (!mundo.getControlEspecial().equals("S"))
		{cabecera[14] = "No INGRESO: "+persona.getConsecutivoIngreso();
		cabecera[15] = "No CUENTA: "+persona.getCodigoCuenta();}
		else{
		cabecera[14] =  "N° HISTORIA CLINICA: "+ persona.getHistoriaClinica();
		cabecera[15] = "";
		}
		
		//Generar Cabecera del documento-*******************************************************************************
		String tituloMedicamentos = "";
		if (!mundo.getControlEspecial().equals("S"))
		{tituloMedicamentos = "SOLICITUD DE MEDICAMENTOS (" + UtilidadFecha.getFechaActual() + " - " + UtilidadFecha.getHoraActual() + ")";
		}
		else {tituloMedicamentos = "RECETARIO OFICIAL DE MEDICAMENTOS DE CONTROL ESPECIAL .PREESCRIPCION DE MEDICAMENTOS";
		} 
		
		InstitucionBasica institucionBasica= new InstitucionBasica();
        institucionBasica.cargarXConvenio(usuario.getCodigoInstitucionInt(), persona.getCodigoConvenio());
        
        //generacion de la cabecera, validando el tipo de separador e utilizado en el path
        report.setReportBaseHeader1(institucionBasica, institucionBasica.getNit(), tituloMedicamentos);
		
		///*************************************
		try
		{
			report.createSection("CabeceraSolicitud",PdfReports.REPORT_HEADER_TABLE,6,6,2);
			report.getSectionReference("CabeceraSolicitud").setTableBorder(PdfReports.REPORT_HEADER_TABLE, 0xFFFFFF, 0.0f);
			report.getSectionReference("CabeceraSolicitud").setTableCellBorderWidth(PdfReports.REPORT_HEADER_TABLE, 0.5f);
			report.getSectionReference("CabeceraSolicitud").setTableCellsDefaultColors(PdfReports.REPORT_HEADER_TABLE, 0xFFFFFF, 0xFFFFFF);
			report.setReportDataSectionAtributes(0xFFFFFF, 0xFFFFFF, 0x000000, 12);
			report.getSectionReference("CabeceraSolicitud").setTableCellsColSpan(4);
			report.font.setFontSizeAndAttributes(tamanioLetraCabeceraPaciente,true,false,false);
			report.getSectionReference("CabeceraSolicitud").addTableTextCell(PdfReports.REPORT_HEADER_TABLE, "PACIENTE: "+persona.getApellidosNombresPersona(false) + "    "+persona.getCodigoTipoIdentificacionPersona() +": "+persona.getNumeroIdentificacionPersona(), report.font);
			report.font.setFontSizeAndAttributes(tamanioLetraCabecera,false,false,false);
			report.getSectionReference("CabeceraSolicitud").setTableCellsColSpan(2);
			report.getSectionReference("CabeceraSolicitud").addTableTextCell(PdfReports.REPORT_HEADER_TABLE, cabecera[0], report.font);
			report.getSectionReference("CabeceraSolicitud").setTableCellsColSpan(1);
			report.getSectionReference("CabeceraSolicitud").addTableTextCell(PdfReports.REPORT_HEADER_TABLE, cabecera[1], report.font);
			report.getSectionReference("CabeceraSolicitud").addTableTextCell(PdfReports.REPORT_HEADER_TABLE, cabecera[2], report.font);
			report.getSectionReference("CabeceraSolicitud").setTableCellsColSpan(2);
			report.getSectionReference("CabeceraSolicitud").addTableTextCell(PdfReports.REPORT_HEADER_TABLE, cabecera[3], report.font);
			report.getSectionReference("CabeceraSolicitud").setTableCellsColSpan(1);
			report.getSectionReference("CabeceraSolicitud").addTableTextCell(PdfReports.REPORT_HEADER_TABLE, cabecera[14], report.font);
			report.getSectionReference("CabeceraSolicitud").addTableTextCell(PdfReports.REPORT_HEADER_TABLE, cabecera[15], report.font);
			
			/*if(!mundo.getNumeroAutorizacion().equals(""))
				report.getSectionReference("CabeceraSolicitud").setTableCellsColSpan(2);
			else
				report.getSectionReference("CabeceraSolicitud").setTableCellsColSpan(3);
			*/
			report.getSectionReference("CabeceraSolicitud").setTableCellsColSpan(3);
			
			report.getSectionReference("CabeceraSolicitud").addTableTextCell(PdfReports.REPORT_HEADER_TABLE, cabecera[4], report.font);
			report.getSectionReference("CabeceraSolicitud").addTableTextCell(PdfReports.REPORT_HEADER_TABLE, cabecera[5], report.font);
			
			/*if(!mundo.getNumeroAutorizacion().equals(""))
				report.getSectionReference("CabeceraSolicitud").addTableTextCell(PdfReports.REPORT_HEADER_TABLE, cabecera[6], report.font);
			*/
			report.getSectionReference("CabeceraSolicitud").setTableCellsColSpan(3);
			report.getSectionReference("CabeceraSolicitud").addTableTextCell(PdfReports.REPORT_HEADER_TABLE, cabecera[7], report.font);
			report.getSectionReference("CabeceraSolicitud").addTableTextCell(PdfReports.REPORT_HEADER_TABLE, cabecera[8], report.font);
			report.getSectionReference("CabeceraSolicitud").addTableTextCell(PdfReports.REPORT_HEADER_TABLE, cabecera[9], report.font);
			report.getSectionReference("CabeceraSolicitud").addTableTextCell(PdfReports.REPORT_HEADER_TABLE, cabecera[10], report.font);
			report.getSectionReference("CabeceraSolicitud").setTableCellsColSpan(2);
			report.getSectionReference("CabeceraSolicitud").addTableTextCell(PdfReports.REPORT_HEADER_TABLE, cabecera[11], report.font);
			report.getSectionReference("CabeceraSolicitud").addTableTextCell(PdfReports.REPORT_HEADER_TABLE, cabecera[12], report.font);
			report.getSectionReference("CabeceraSolicitud").addTableTextCell(PdfReports.REPORT_HEADER_TABLE, cabecera[13], report.font);
			float widths[] = {(float) 1,(float) 1.5,(float) 1, (float) 1.5,(float) 1.5,(float) 1.5};
			try
	        {
			    report.getSectionReference("CabeceraSolicitud").getTableReference(PdfReports.REPORT_HEADER_TABLE).setWidths(widths);
			}
	        catch (Exception e)
	        {
	        	logger.error("Se presentï¿½ error generando el pdf creando la seccion de cabecera medicamentos" + e);
	        }
			report.openReport(filename);
			//***********************************************************************DATOS
			String[] cabeceraTablaMedicamentos = new String[columnasTablaMedicamentos];
			String[] cabeceraTablaMedicamentosC = new String[2];
			
			int tamanioVector=0;
			if(mundo.getArticulos().size()>0 && mundoMDD.getArticulos().size()>0)
				tamanioVector=mundo.getArticulos().size()+mundoMDD.getArticulos().size();
			else
			{
				if(mundo.getArticulos().size()>0)
					tamanioVector=mundo.getArticulos().size();
				else if(mundoMDD.getArticulos().size()>0)
					tamanioVector=mundoMDD.getArticulos().size();
			}
			
			logger.info("tamanio vector*columnas-->"+((tamanioVector)*columnasTablaMedicamentos));
			String[] datosTablaMedicamentos = new String[(tamanioVector)*columnasTablaMedicamentos];
			String[] datosTablaMedicamentosC = new String[(tamanioVector)*2];
			if (!mundo.getControlEspecial().equals("S"))
			{
			
			cabeceraTablaMedicamentos[0] = "ART";
			cabeceraTablaMedicamentos[1] = "MEDICAMENTO";
			cabeceraTablaMedicamentos[2] = "DOSIS";
			cabeceraTablaMedicamentos[3] = "FREC";
			cabeceraTablaMedicamentos[4] = "VIA";
			cabeceraTablaMedicamentos[5] = "DIAS TRAT.";
			cabeceraTablaMedicamentos[6] = "CAN";
			cabeceraTablaMedicamentos[7] = "OBSERVACIONES";
			cabeceraTablaMedicamentos[8] = "POS";
			}
			else
			{
			cabeceraTablaMedicamentos[0] = "COD";
			cabeceraTablaMedicamentos[1] = "DESCRIPCION";
			cabeceraTablaMedicamentos[2] = "CONS.";
			cabeceraTablaMedicamentos[3] = "FORMA";
			cabeceraTablaMedicamentos[4] = "DOSIS";
			cabeceraTablaMedicamentos[5] = "FREC.";
			cabeceraTablaMedicamentos[6] = "VIA";
			cabeceraTablaMedicamentos[7] = "DIAS TRAT";
			cabeceraTablaMedicamentos[8] = "OBSERVACIONES";
			cabeceraTablaMedicamentosC[0] = "NUMERO";
			cabeceraTablaMedicamentosC[1] = "LETRAS";
			}
			Articulo articulo = new Articulo();
			String temporal = "";
			Articulo articulo2 = new Articulo();
			String temporal2 = "";
			//Cargar Medicamento

			logger.info("size mundo1->"+mundo.getArticulos().size()+" size mundo2->"+mundoMDD.getArticulos().size());
			
			for(int k = 0 ;  k < mundo.getArticulos().size() ; k++)
			{
				
				//cargar los datos del articulo.
				articulo.cargarArticulo(con,((ArticuloSolicitudMedicamentos)mundo.getArticulos().elementAt(k)).getArticulo());
				if (!mundo.getControlEspecial().equals("S")) //si no es control especial
				{
				datosTablaMedicamentos[(k*columnasTablaMedicamentos)] = articulo.getCodigo();
				datosTablaMedicamentos[(k*columnasTablaMedicamentos)+1] = articulo.getDescripcion().trim()+" CONC. "+articulo.getConcentracion().trim()+" FF. "+articulo.getNomFormaFarmaceutica().trim()+" UM. "+articulo.getNomUnidadMedida().trim();
				
			
				if(((ArticuloSolicitudMedicamentos)mundo.getArticulos().elementAt(k)).getDosis().equals("null")||((ArticuloSolicitudMedicamentos)mundo.getArticulos().elementAt(k)).getDosis().equals(null))
				{
					datosTablaMedicamentos[(k*columnasTablaMedicamentos)+2] = "";
				}
				else
				{
					datosTablaMedicamentos[(k*columnasTablaMedicamentos)+2] = ((ArticuloSolicitudMedicamentos)mundo.getArticulos().elementAt(k)).getDosis();
				}
				if(((ArticuloSolicitudMedicamentos)mundo.getArticulos().elementAt(k)).getUnidosis().equals("null")||((ArticuloSolicitudMedicamentos)mundo.getArticulos().elementAt(k)).getUnidosis().equals(null))
				{
					datosTablaMedicamentos[(k*columnasTablaMedicamentos)+2] += "";
				}
				else
				{
					if (!((ArticuloSolicitudMedicamentos)mundo.getArticulos().elementAt(k)).getUnidosis().toString().equals(""))
					{
							datosTablaMedicamentos[(k*columnasTablaMedicamentos)+2] +=" "+Utilidades.obtenerUnidadMedidadUnidosisArticulo(con,((ArticuloSolicitudMedicamentos)mundo.getArticulos().elementAt(k)).getUnidosis());
					}
				}

				
				temporal = ((ArticuloSolicitudMedicamentos)mundo.getArticulos().elementAt(k)).getFrecuencia()+"";
				if(temporal.equals("null")||temporal.equals(null)||temporal.equals("0.0"))
				{
					datosTablaMedicamentos[(k*columnasTablaMedicamentos)+3] = "";
				}
				else
				{
					datosTablaMedicamentos[(k*columnasTablaMedicamentos)+3] = "Cada "+((ArticuloSolicitudMedicamentos)mundo.getArticulos().elementAt(k)).getFrecuencia()+" "+((ArticuloSolicitudMedicamentos)mundo.getArticulos().elementAt(k)).getTipoFrecuencia();
				}
				temporal = ((ArticuloSolicitudMedicamentos)mundo.getArticulos().elementAt(k)).getVia();
				if(temporal.equals("null")||temporal.equals(null))
				{
					datosTablaMedicamentos[(k*columnasTablaMedicamentos)+4] = "";
				}
				else
				{
					datosTablaMedicamentos[(k*columnasTablaMedicamentos)+4] = ((ArticuloSolicitudMedicamentos)mundo.getArticulos().elementAt(k)).getVia();
				}
				temporal = ((ArticuloSolicitudMedicamentos)mundo.getArticulos().elementAt(k)).getDiasTratamiento();
				if(temporal.equals("null")||temporal.equals(null))
				{
					datosTablaMedicamentos[(k*columnasTablaMedicamentos)+5] = "";
				}
				else
				{
					datosTablaMedicamentos[(k*columnasTablaMedicamentos)+5] = ((ArticuloSolicitudMedicamentos)mundo.getArticulos().elementAt(k)).getDiasTratamiento();
				}
				datosTablaMedicamentos[(k*columnasTablaMedicamentos)+6] = ((ArticuloSolicitudMedicamentos)mundo.getArticulos().elementAt(k)).getCantidadSolicitada()+" ("+UtilidadN2T.convertirLetras(((ArticuloSolicitudMedicamentos)mundo.getArticulos().elementAt(k)).getCantidadSolicitada())+")";
				datosTablaMedicamentos[(k*columnasTablaMedicamentos)+7] = ((ArticuloSolicitudMedicamentos)mundo.getArticulos().elementAt(k)).getObservaciones();
				if(temporal.equals("null")||temporal.equals(null))
				{
					datosTablaMedicamentos[(k*columnasTablaMedicamentos)+8] = "";
				}
				else
				{
					datosTablaMedicamentos[(k*columnasTablaMedicamentos)+8] = ((ArticuloSolicitudMedicamentos)mundo.getArticulos().elementAt(k)).getEsPos().equals("t")?"SI":"NO";
				}
			}
				else // si es control especial
				{
					datosTablaMedicamentos[(k*columnasTablaMedicamentos)] = articulo.getCodigo();
					datosTablaMedicamentos[(k*columnasTablaMedicamentos)+1] = articulo.getDescripcion().trim()+" CONC. "+articulo.getConcentracion().trim()+" FF. "+articulo.getNomFormaFarmaceutica().trim()+" UM. "+articulo.getNomUnidadMedida().trim();
					
					datosTablaMedicamentos[(k*columnasTablaMedicamentos)+2]=articulo.getConcentracion();
					datosTablaMedicamentos[(k*columnasTablaMedicamentos)+3]=articulo.getConcentracion();
					if(((ArticuloSolicitudMedicamentos)mundo.getArticulos().elementAt(k)).getDosis().equals("null")||((ArticuloSolicitudMedicamentos)mundo.getArticulos().elementAt(k)).getDosis().equals(null))
					{
						datosTablaMedicamentos[(k*columnasTablaMedicamentos)+4] = "";
					}
					else
					{
						datosTablaMedicamentos[(k*columnasTablaMedicamentos)+4] = ((ArticuloSolicitudMedicamentos)mundo.getArticulos().elementAt(k)).getDosis();
					}
					if(((ArticuloSolicitudMedicamentos)mundo.getArticulos().elementAt(k)).getUnidosis().equals("null")||((ArticuloSolicitudMedicamentos)mundo.getArticulos().elementAt(k)).getUnidosis().equals(null))
					{
						datosTablaMedicamentos[(k*columnasTablaMedicamentos)+4] += "";
					}
					else
					{
						if (!((ArticuloSolicitudMedicamentos)mundo.getArticulos().elementAt(k)).getUnidosis().toString().equals(""))
						{
								datosTablaMedicamentos[(k*columnasTablaMedicamentos)+4] +=" "+Utilidades.obtenerUnidadMedidadUnidosisArticulo(con,((ArticuloSolicitudMedicamentos)mundo.getArticulos().elementAt(k)).getUnidosis());
						}
					}

					
					temporal = ((ArticuloSolicitudMedicamentos)mundo.getArticulos().elementAt(k)).getFrecuencia()+"";
					if(temporal.equals("null")||temporal.equals(null)||temporal.equals("0.0"))
					{
						datosTablaMedicamentos[(k*columnasTablaMedicamentos)+5] = "";
					}
					else
					{
						datosTablaMedicamentos[(k*columnasTablaMedicamentos)+5] = "Cada "+((ArticuloSolicitudMedicamentos)mundo.getArticulos().elementAt(k)).getFrecuencia()+" "+((ArticuloSolicitudMedicamentos)mundo.getArticulos().elementAt(k)).getTipoFrecuencia();
					}
					temporal = ((ArticuloSolicitudMedicamentos)mundo.getArticulos().elementAt(k)).getVia();
					if(temporal.equals("null")||temporal.equals(null))
					{
						datosTablaMedicamentos[(k*columnasTablaMedicamentos)+6] = "";
					}
					else
					{
						datosTablaMedicamentos[(k*columnasTablaMedicamentos)+6] = ((ArticuloSolicitudMedicamentos)mundo.getArticulos().elementAt(k)).getVia();
					}
					temporal = ((ArticuloSolicitudMedicamentos)mundo.getArticulos().elementAt(k)).getDiasTratamiento();
					if(temporal.equals("null")||temporal.equals(null))
					{
						datosTablaMedicamentos[(k*columnasTablaMedicamentos)+7] = "";
					}
					else
					{
						datosTablaMedicamentos[(k*columnasTablaMedicamentos)+7] = ((ArticuloSolicitudMedicamentos)mundo.getArticulos().elementAt(k)).getDiasTratamiento();
					}
//					datosTablaMedicamentos[(k*columnasTablaMedicamentos)+6] = ((ArticuloSolicitudMedicamentos)mundo.getArticulos().elementAt(k)).getCantidadSolicitada()+" ("+UtilidadN2T.convertirLetras(((ArticuloSolicitudMedicamentos)mundo.getArticulos().elementAt(k)).getCantidadSolicitada())+")";
//					
					datosTablaMedicamentos[(k*columnasTablaMedicamentos)+8] = ((ArticuloSolicitudMedicamentos)mundo.getArticulos().elementAt(k)).getObservaciones();
					datosTablaMedicamentosC[(k)] = ((ArticuloSolicitudMedicamentos)mundo.getArticulos().elementAt(k)).getCantidadSolicitada()+"";
					datosTablaMedicamentosC[(k)+1] = UtilidadN2T.convertirLetras(((ArticuloSolicitudMedicamentos)mundo.getArticulos().elementAt(k)).getCantidadSolicitada())+"";
				}
			}
		
			
			int posicionVaTablaMed= (mundo.getArticulos().size()*columnasTablaMedicamentos);
			
			logger.info("pos->"+posicionVaTablaMed);
			
			///////////////////////MEDICAMENTOS CON DIFERENTE DOSIFICACION//////////////////////////////////////////////////////////
			for(int k = 0 ;  k < mundoMDD.getArticulos().size() ; k++)
			{
				//cargar los datos del articulo.
				articulo.cargarArticulo(con,((ArticuloSolicitudMedicamentos)mundoMDD.getArticulos().elementAt(k)).getArticulo());
				datosTablaMedicamentos[(k*columnasTablaMedicamentos)+posicionVaTablaMed] = articulo.getCodigo();
				datosTablaMedicamentos[(k*columnasTablaMedicamentos)+1+posicionVaTablaMed] = articulo.getDescripcion().trim()+" CONC. "+articulo.getConcentracion().trim()+" FF. "+articulo.getNomFormaFarmaceutica().trim()+" UM. "+articulo.getNomUnidadMedida().trim();
				if(((ArticuloSolicitudMedicamentos)mundoMDD.getArticulos().elementAt(k)).getDosis().equals("null")||((ArticuloSolicitudMedicamentos)mundoMDD.getArticulos().elementAt(k)).getDosis().equals(null))
				{
					datosTablaMedicamentos[(k*columnasTablaMedicamentos)+2+posicionVaTablaMed] = "";
				}
				else
				{
					datosTablaMedicamentos[(k*columnasTablaMedicamentos)+2+posicionVaTablaMed] = ((ArticuloSolicitudMedicamentos)mundoMDD.getArticulos().elementAt(k)).getDosis();
				}
				if(((ArticuloSolicitudMedicamentos)mundoMDD.getArticulos().elementAt(k)).getUnidosis().equals("null")||((ArticuloSolicitudMedicamentos)mundoMDD.getArticulos().elementAt(k)).getUnidosis().equals(null))
				{
					datosTablaMedicamentos[(k*columnasTablaMedicamentos)+2+posicionVaTablaMed] += "";
				}
				else
				{
					if (!((ArticuloSolicitudMedicamentos)mundoMDD.getArticulos().elementAt(k)).getUnidosis().toString().equals(""))
					{
						datosTablaMedicamentos[(k*columnasTablaMedicamentos)+2+posicionVaTablaMed] +=" "+Utilidades.obtenerUnidadMedidadUnidosisArticulo(con,((ArticuloSolicitudMedicamentos)mundoMDD.getArticulos().elementAt(k)).getUnidosis());
					}
				}

				
				temporal = ((ArticuloSolicitudMedicamentos)mundoMDD.getArticulos().elementAt(k)).getFrecuencia()+"";
				if(temporal.equals("null")||temporal.equals(null)||temporal.equals("0.0"))
				{
					datosTablaMedicamentos[(k*columnasTablaMedicamentos)+3+posicionVaTablaMed] = "";
				}
				else
				{
					datosTablaMedicamentos[(k*columnasTablaMedicamentos)+3+posicionVaTablaMed] = ((ArticuloSolicitudMedicamentos)mundoMDD.getArticulos().elementAt(k)).getFrecuencia()+" "+((ArticuloSolicitudMedicamentos)mundoMDD.getArticulos().elementAt(k)).getTipoFrecuencia();
				}
				temporal = ((ArticuloSolicitudMedicamentos)mundoMDD.getArticulos().elementAt(k)).getVia();
				if(temporal.equals("null")||temporal.equals(null))
				{
					datosTablaMedicamentos[(k*columnasTablaMedicamentos)+4+posicionVaTablaMed] = "";
				}
				else
				{
					datosTablaMedicamentos[(k*columnasTablaMedicamentos)+4+posicionVaTablaMed] = ((ArticuloSolicitudMedicamentos)mundoMDD.getArticulos().elementAt(k)).getVia();
				}
				temporal = ((ArticuloSolicitudMedicamentos)mundoMDD.getArticulos().elementAt(k)).getDiasTratamiento();
				if(temporal.equals("null")||temporal.equals(null))
				{
					datosTablaMedicamentos[(k*columnasTablaMedicamentos)+5+posicionVaTablaMed] = "";
				}
				else
				{
					datosTablaMedicamentos[(k*columnasTablaMedicamentos)+5+posicionVaTablaMed] = ((ArticuloSolicitudMedicamentos)mundoMDD.getArticulos().elementAt(k)).getDiasTratamiento();
				}
				datosTablaMedicamentos[(k*columnasTablaMedicamentos)+6+posicionVaTablaMed] = ((ArticuloSolicitudMedicamentos)mundoMDD.getArticulos().elementAt(k)).getCantidadSolicitada()+" ("+UtilidadN2T.convertirLetras(((ArticuloSolicitudMedicamentos)mundoMDD.getArticulos().elementAt(k)).getCantidadSolicitada())+")";
				datosTablaMedicamentos[(k*columnasTablaMedicamentos)+7+posicionVaTablaMed] = ((ArticuloSolicitudMedicamentos)mundoMDD.getArticulos().elementAt(k)).getObservaciones();
				if(temporal.equals("null")||temporal.equals(null))
				{
					datosTablaMedicamentos[(k*columnasTablaMedicamentos)+8+posicionVaTablaMed] = "";
				}
				else
				{
					datosTablaMedicamentos[(k*columnasTablaMedicamentos)+8+posicionVaTablaMed] = ((ArticuloSolicitudMedicamentos)mundoMDD.getArticulos().elementAt(k)).getEsPos().equals("t")?"SI":"NO";
				}
			}
			///////////////////////FIN MEDICAMENTOS CON DIFERENTE DOSIFICACION//////////////////////////////////////////////////////
			
			
			logger.info("pos1->"+datosTablaMedicamentos.length);
			
			report.setReportTitleSectionAtributes(0x000000, 0xEBEBEB,0x000000, 12);
			report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000, 10);
			report.createColSection("MEDICAMENTOS",cabeceraTablaMedicamentos,datosTablaMedicamentos,tamanioLetraCabeceraTablaMedicamento,tamanioLetraDatosTablaMedicamento,0);
			float widthsMedicamentos[] = {(float) 0.5,(float) 3.1,(float) 1.1, (float) 0.95, (float) 0.90,(float) 0.85,(float) 0.6,(float) 1.5,(float) 0.5};
			try
	        {
			    report.getSectionReference("MEDICAMENTOS").getTableReference(PdfReports.REPORT_PARENT_TABLE).setWidths(widthsMedicamentos);
			}
	        catch (Exception e)
	        {
	        	logger.error("Se presentï¿½ error generando el pdf creando la seccion de medicamentos" + e);
	        }
			report.addSectionToDocument("MEDICAMENTOS");
			if(!mundo.getObservacionesGenerales().equals(""))
			{
				report.font.setFontSizeAndAttributes(tamanioLetraParrafo,false,false,false);
				report.document.addParagraph("OBSERVACIONES: "+mundo.getObservacionesGenerales(),report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,20);
			}
			//seccion de la informacion del medico
			report.font.setFontSizeAndAttributes(tamanioLetraParrafo,true,true,false);
			String medico=mundo.getDatosMedico().trim();
			
			String firmaDigital= Medico.obtenerFirmaDigitalMedico(mundo.getCodigoMedicoSolicitante());
			logger.info("\n\nFIRMA DIGITAL---->"+firmaDigital+" codigomedicosolicitante-->"+mundo.getCodigoMedicoSolicitante());
			if(!firmaDigital.equals(""))
			{
				report.document.addImage(ValoresPorDefecto.getFilePath()+System.getProperty("file.separator")+System.getProperty("FIRMADIGITAL")+System.getProperty("file.separator")+firmaDigital);
				report.document.addParagraph(institucionBasica.getPieHistoriaClinica(), 10);
			}
			report.document.addParagraph(medico,report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
			
			if(mundo.getEstadoHistoriaClinica().getNombre().trim().equals("Anulada")){

				String datosMedicoAnula=mundo.consultarDatosMedicoAnulacion(con, mundo.getCodigoMedicoAnulacion());
				report.document.addParagraph(" ",report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
				report.document.addParagraph("Orden ANULADA "+mundo.getFechaAnulacion()+" "+mundo.getHoraAnulacion(),report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
				report.document.addParagraph("Motivo: "+mundo.getMotivoAnulacion(),report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
				report.document.addParagraph(datosMedicoAnula,report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
			}
			//report.document.addParagraph(medicoSolicitante.getNombreUsuario()+", "+medicoSolicitante.getNumeroRegistroMedico()+", "+medicoSolicitante.getEspecialidadesMedico(),report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
			report.closeReport();
		}
		catch(Exception e)
		{
				logger.error("Se presentï¿½ error generando el pdf creando la seccion de medicamentos",e);
				report.closeReport();
		}

	}
	
	
	/**
	 * Metodo para generar el report de un articulo no POS
	 * @param filename, Nombre del archivo
	 * @param mundo, objeto de medicamentos
	 * @param usuario, objeto medico que genera el repor 
	 * @param persona, paciente
	 * @param codigoArticulo, Codigo del articulo NOPOS al que se le genera el Reporte.
	 * @param con, conexion
	 */
	public static void pdfSolicitudMedicamentosNoPos(String filename, SolicitudMedicamentos mundo, UsuarioBasico usuario,PersonaBasica persona,int codigoArticulo,Connection con) 
	{
		//reporte
		PdfReports report = new PdfReports();
		//Datos de la cabecera
		String[] reportDataHead = new String[2];
		//posicion de articulo NOPOS en el vector
		int posicion;
		//buscar la posicion del articulo en el vector
		posicion = posicionNoPos(codigoArticulo,mundo);
		//llenar la cabecera
		reportDataHead[0] = "FECHA DE ORDEN: "+mundo.getFechaSolicitud();
        reportDataHead[1] = "AUTORIZACIï¿½N: "+((ArticuloSolicitudMedicamentos)mundo.getArticulos().elementAt(posicion)).getObservaciones();
		//Objeto que contiene los datos de la solicitud.
		Solicitud solicitud = new Solicitud();
		try 
		{
			solicitud.cargar(con,mundo.getNumeroSolicitud());
			persona.cargarEmpresaRegimen(con,persona.getCodigoPersona(),persona.getCodigoTipoRegimen()+"");
		} 
		catch (SQLException e) 
		{
			logger.error("Error cargando la solicitud");
		}
		//cargar los datos de paciente para la seccion
		String[] reportDataPaciente1linea = {"NOMBRE: "+persona.getApellidosNombresPersona(false),persona.getCodigoTipoIdentificacionPersona()+": "+persona.getNumeroIdentificacionPersona(),"EDAD: "+persona.getEdadDetallada(),"RESPONSABLE: "+persona.getConvenioPersonaResponsable(),"CAMA: "+persona.getCama()," "};
		//generacion de la cabecera del documento
		String tituloArticulos = "JUSTIFICACIï¿½N MEDICAMENTOS NO POS (" + UtilidadFecha.getFechaActual() + " - " + UtilidadFecha.getHoraActual() + ")";
		
		InstitucionBasica institucionBasica= new InstitucionBasica();
        institucionBasica.cargarXConvenio(usuario.getCodigoInstitucionInt(), persona.getCodigoConvenio());
        
        //generacion de la cabecera, validando el tipo de separador e utilizado en el path
        report.setReportBaseHeader1(institucionBasica, institucionBasica.getNit(), tituloArticulos);
		
		//generacion del cuerpo del documento
		try{	
			//generacion de la segunda cabecera
			report.createSection("CabeceraArticulo",PdfReports.REPORT_HEADER_TABLE,2,1,2);
			report.getSectionReference("CabeceraArticulo").setTableBorder(PdfReports.REPORT_HEADER_TABLE, 0xFFFFFF, 0.0f);
			report.getSectionReference("CabeceraArticulo").setTableCellBorderWidth(PdfReports.REPORT_HEADER_TABLE, 0.5f);
			report.getSectionReference("CabeceraArticulo").setTableCellsDefaultColors(PdfReports.REPORT_HEADER_TABLE, 0xFFFFFF, 0xFFFFFF);
			report.setReportDataSectionAtributes(0xFFFFFF, 0xFFFFFF, 0x000000, 12);
			report.font.setFontSizeAndAttributes(12,true,false,false);
			for(int k = 0 ; k < 2 ; k++)	
				report.getSectionReference("CabeceraArticulo").addTableTextCell(PdfReports.REPORT_HEADER_TABLE, reportDataHead[k], report.font);
			report.openReport(filename);
			
			//seccion paciente
			
			report.font.setFontSizeAndAttributes(12,true,false,false);
			report.document.addParagraph(" ",5);
			report.document.addParagraph("IDENTIFICACION DEL PACIENTE",report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
			report.createSection("seccionNoPos","tablaPacientes",2,3,0);
			report.getSectionReference("seccionNoPos").setTableBorder("tablaPacientes", 0xFFFFFF, 0.0f);
			report.getSectionReference("seccionNoPos").setTableCellBorderWidth("tablaPacientes", 0.5f);
			report.getSectionReference("seccionNoPos").setTableCellsDefaultColors("tablaPacientes", 0xFFFFFF, 0xFFFFFF);
			report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000, 12);
			
			report.font.setFontSizeAndAttributes(12,false,false,false);
			for(int k = 0 ; k < 6 ; k++)	
				report.getSectionReference("seccionNoPos").addTableTextCell("tablaPacientes", reportDataPaciente1linea[k], report.font);
			float widths[] = {(float) 6,(float) 2,(float) 2};
			
			try
	        {
			    report.getSectionReference("seccionNoPos").getTableReference("tablaPacientes").setWidths(widths);
			}
	        catch (Exception e)
	        {
	        	logger.error("Se presentï¿½ error generando el pdf creando la seccion de medicamentos" + e);
	        }
	        report.addSectionToDocument("seccionNoPos");
	        //cargar los datos del articulo.
	        Articulo articulo = new Articulo();
			articulo.cargarArticulo(con,((ArticuloSolicitudMedicamentos)mundo.getArticulos().elementAt(posicion)).getArticulo());
	        //seccion de datos del producto farmaceutico.
			report.font.setFontSizeAndAttributes(12,true,false,false);
	        report.document.addParagraph("DATOS DEL PRODUCTO FARMACï¿½UTICO",report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
	        report.document.addParagraph(" ",5);
	        report.font.setFontSizeAndAttributes(12,false,false,false);
	        report.document.addParagraph(articulo.getCodigo()+" - "+articulo.getDescripcion()+" CONC: "+articulo.getConcentracion()+" FF: "+articulo.getNomFormaFarmaceutica()+" UM: "+articulo.getNomUnidadMedida(),report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,15);
	        String[] strTemp = new String[3];
	        strTemp[0] = "Dosis: "+ ((ArticuloSolicitudMedicamentos)mundo.getArticulos().elementAt(posicion)).getDosis();
	        strTemp[1] = "Frecuencia: "+((ArticuloSolicitudMedicamentos)mundo.getArticulos().elementAt(posicion)).getFrecuencia();
	        strTemp[2] = "Via: "+((ArticuloSolicitudMedicamentos)mundo.getArticulos().elementAt(posicion)).getVia();
	        report.createSection("seccionArticuloNoPos","tablaArticulosNoPos",1,3,2);
	        report.getSectionReference("seccionArticuloNoPos").addTableTextCell("tablaArticulosNoPos", strTemp[0], report.font);
	        report.getSectionReference("seccionArticuloNoPos").addTableTextCell("tablaArticulosNoPos", strTemp[1], report.font);
	        report.getSectionReference("seccionArticuloNoPos").addTableTextCell("tablaArticulosNoPos", strTemp[2], report.font);
	        report.addSectionToDocument("seccionArticuloNoPos");
	        //seccion de Resumen de la historia clinica y diagnostico se muestra si se incertaron datos.

	        Vector nombresCodigosJustificacion = Utilidades.buscarCodigosNombresJustificaciones(usuario.getCodigoInstitucionInt(), false, true);
	        
	        for(int i = 0 ; i < nombresCodigosJustificacion.size() ; i++)
	        {
	        	
	        	Vector atributo = (Vector)nombresCodigosJustificacion.get(i);
	        	String justificacion = ((ArticuloSolicitudMedicamentos)mundo.getArticulos().elementAt(posicion)).getJustificacion(atributo.elementAt(0)+"");
		        if(!justificacion.equals(""))
		        {
		        	report.font.setFontSizeAndAttributes(12,true,false,false);
		        	report.document.addParagraph(atributo.elementAt(1)+"",report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
		        	report.document.addParagraph(" ",5);
		        	report.font.setFontSizeAndAttributes(12,false,false,false);
		        	report.document.addParagraph(justificacion,report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,15);
		        	report.document.addParagraph(" ",5);
		        }
	        }
/*
	        //seccion JUSTIFICACION DE LA SOLICITUD
	        report.font.setFontSizeAndAttributes(12,true,false,false);
	        report.document.addParagraph("JUSTIFICACIï¿½N DE LA SOLICITUD",report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
	        report.document.addParagraph(" ",5);
	        report.font.setFontSizeAndAttributes(12,false,false,false);
	        report.document.addParagraph(((ArticuloSolicitudMedicamentos)mundo.getArticulos().elementAt(posicion)).getJustificacion(),report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,15);
	        report.document.addParagraph(" ",5);
	        //seccion de MEDICAMENTOS DE LA MISMA CATEGORï¿½A FARMACOLï¿½GICA se muestra si se insertaron datos.
	        if(!((ArticuloSolicitudMedicamentos)mundo.getArticulos().elementAt(posicion)).getMedicamentosCategoria().equals(""))
	        {
		        report.font.setFontSizeAndAttributes(12,true,false,false);
		        report.document.addParagraph("MEDICAMENTOS DE LA MISMA CATEGORï¿½A FARMACOLï¿½GICA QUE SI ESTAN INCLUIDOS EN EL POS",report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
		        report.document.addParagraph(" ",5);
		        report.font.setFontSizeAndAttributes(12,false,false,false);
		        report.document.addParagraph(((ArticuloSolicitudMedicamentos)mundo.getArticulos().elementAt(posicion)).getMedicamentosCategoria(),report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,15);
		        report.document.addParagraph(" ",5);
	        }
	        //seccion de evidencia
	        if(!((ArticuloSolicitudMedicamentos)mundo.getArticulos().elementAt(posicion)).getEvidencia().equals(""))
	        {
		        report.font.setFontSizeAndAttributes(12,true,false,false);
		        report.document.addParagraph("EVIDENCIA DEL IMPACTO DEL MEDICAMENTO EN EL CURSO DE LA ENFERMEDAD",report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
		        report.document.addParagraph(" ",5);
		        report.font.setFontSizeAndAttributes(12,false,false,false);
		        report.document.addParagraph(((ArticuloSolicitudMedicamentos)mundo.getArticulos().elementAt(posicion)).getEvidencia(),report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,15);
	        }*/
	        //secccion de la informacion del medico	        
			report.font.setFontSizeAndAttributes(12,true,true,false);
			String medico = ((mundo.getDatosMedico().trim()).split("\n"))[1];
			report.document.addParagraph(medico,report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
		}		
		catch(Exception e)
		{
			logger.error("Se presentï¿½ error generando el pdf creando la seccion de medicamentos" + e);
			e.printStackTrace();
		}
		report.closeReport();
	}
	
	/**
	 * Metodo para busca la posicion de un articulo en el vector
	 * Articulos de acuerdo a su codigo
	 * @param codigoArticulo, Articulo que se busca
	 * @param mundo, objeto que contiene el vector
	 * @return posicion, posicion del articulo
	 */	
	public static int posicionNoPos(int codigoArticulo, SolicitudMedicamentos mundo)
	{
		for(int i = 0 ; i < mundo.getArticulos().size() ; i++)
			if(((ArticuloSolicitudMedicamentos)mundo.getArticulos().elementAt(i)).getArticulo()==codigoArticulo)
				return i;
		return -1;
			
	}
}
