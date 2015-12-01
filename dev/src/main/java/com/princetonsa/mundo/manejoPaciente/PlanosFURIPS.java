package com.princetonsa.mundo.manejoPaciente;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

import org.apache.log4j.Logger;

import util.BackUpBaseDatos;
import util.ConstantesBD;
import util.UtilidadTexto;
import util.ValoresPorDefecto;
import util.manejoPaciente.DetalleInconsistenciaFURIPS;
import util.manejoPaciente.RutasArchivosFURIPS;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.manejoPaciente.PlanosFURIPSDao;
import com.princetonsa.dto.manejoPaciente.DtoFURIPS1;
import com.princetonsa.dto.manejoPaciente.DtoFURIPS2;
import com.princetonsa.dto.manejoPaciente.DtoFURPRO;
import com.princetonsa.mundo.InstitucionBasica;

/**
 * 
 * @author wilson
 *
 */
public class PlanosFURIPS 
{
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static PlanosFURIPSDao planosDao;
	
	/**
	 * Para manejar las excepciones de la clase.
	 */
	public static Logger logger = Logger.getLogger(PlanosFURIPS.class); 
	
	/**
	 * lista de posibles archivos 
	 */
	public static enum archivosEnum {Furips1, Furips2, Furpro1, Furtran};
	
	/**
	 * constructor de la clase
	 *
	 */
	public PlanosFURIPS()  
	{
		this.init (System.getProperty("TIPOBD"));
	}
	
	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicialización fue exitosa, <code>false</code> si no.
	 */
	public boolean init(String tipoBD)
	{
		boolean wasInited = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
		if (myFactory != null)
		{
			planosDao = myFactory.getPlanosFURIPSDao();
			wasInited = (planosDao != null);
		}
		return wasInited;
	}
	
	/**
	 * Metodo que carga toda la información pertinente para generar el archivo FURIPS1
	 * @param con
	 * @param mapaBusqueda
	 * @param institucionBasica
	 * @param institucion
	 * @return
	 */
	public RutasArchivosFURIPS consultaFURIPS1(Connection con, HashMap<Object, Object> mapaBusqueda, InstitucionBasica institucionBasica, int institucion, RutasArchivosFURIPS rutasArchivos,String reclamacion)
	{
		boolean archivoGenerado=false;
		ArrayList<DtoFURIPS1> arrayDto= planosDao.consultaFURIPS1(con, mapaBusqueda, institucionBasica, institucion,reclamacion);
		Vector<StringBuffer> buffersVector=armarFURIPS1(arrayDto);
		rutasArchivos.setStringBufferFURIPS1(buffersVector.get(0));
		rutasArchivos.setStringBufferInconsistenciasFURIPS1(buffersVector.get(1));
		
		if(UtilidadTexto.isEmpty(rutasArchivos.getStringBufferFURIPS1().toString().trim()) && UtilidadTexto.isEmpty(rutasArchivos.getStringBufferInconsistenciasFURIPS1().toString().trim()))
		{
			//es exitoso pero no debe crear los archivos
			rutasArchivos.setProcesoExitoso(true);
			return rutasArchivos;
		}
		
		archivoGenerado=util.TxtFile.generarTxt(rutasArchivos.getStringBufferFURIPS1(), rutasArchivos.getNombreArchivoFURIPS1(), rutasArchivos.getRutaFURIPS1());
			
		if(!archivoGenerado)
		{
			logger.info("NO GENERO EL ARCHIVO FURIPS 1 !!!!!!");
			rutasArchivos.setProcesoExitoso(false);
			return rutasArchivos;
		}
		
		if(!UtilidadTexto.isEmpty(rutasArchivos.getStringBufferInconsistenciasFURIPS1().toString().trim()))
		{	
			//creamos el archivo de inconsistencias
			archivoGenerado=util.TxtFile.generarTxt(rutasArchivos.getStringBufferInconsistenciasFURIPS1(), rutasArchivos.getNombreArchivoInconsistenciasFURIPS1(), rutasArchivos.getRutaFURIPS1());
			
			if(!archivoGenerado)
		    {
				logger.info("NO GENERO EL ARCHIVO INCONSISTENCIAS FURIPS 1 !!!!!!");
				rutasArchivos.setProcesoExitoso(false);
				return rutasArchivos;
		    }
			else
			{
				rutasArchivos.setCreoArchivoInconsistenciasFURIPS1(true);
		    }
		}	
		
		logger.info("CREÓ CON EXITO EL FURIPS 1!!!");
		rutasArchivos.setProcesoExitoso(true);
		return rutasArchivos;
		
	}	
	
	
	/**
	 * Metodo que carga toda la información pertinente para generar el archivo FURIPS2
	 * @param con
	 * @param mapaBusqueda
	 * @param institucionBasica
	 * @param institucion
	 * @return
	 */
	public RutasArchivosFURIPS consultaFURIPS2(Connection con, HashMap<Object, Object> mapaBusqueda, InstitucionBasica institucionBasica, int institucion, RutasArchivosFURIPS rutasArchivos,String reclamacion)
	{
		ArrayList<DtoFURIPS2> arrayDto= planosDao.consultaFURIPS2(con, mapaBusqueda, institucionBasica, institucion,reclamacion);
		Vector<StringBuffer> buffersVector=armarFURIPS2(arrayDto);
		rutasArchivos.setStringBufferFURIPS2(buffersVector.get(0));
		rutasArchivos.setStringBufferInconsistenciasFURIPS2(buffersVector.get(1));
		
		if(UtilidadTexto.isEmpty(rutasArchivos.getStringBufferFURIPS2().toString().trim()) && UtilidadTexto.isEmpty(rutasArchivos.getStringBufferInconsistenciasFURIPS2().toString().trim()))
		{
			//es exitoso pero no debe crear los archivos
			rutasArchivos.setProcesoExitoso(true);
			return rutasArchivos;
		}
		
		boolean archivoGenerado=util.TxtFile.generarTxt(rutasArchivos.getStringBufferFURIPS2(), rutasArchivos.getNombreArchivoFURIPS2(), rutasArchivos.getRutaFURIPS2());
		
		if(!archivoGenerado)
	    {
			logger.info("NO GENERO EL ARCHIVO FURIPS 2 !!!!!!");
			rutasArchivos.setProcesoExitoso(false);
			return rutasArchivos;
	    }
		
		if(!UtilidadTexto.isEmpty(rutasArchivos.getStringBufferInconsistenciasFURIPS2().toString().trim()))
		{
			//creamos el archivo de inconsistencias
			archivoGenerado=util.TxtFile.generarTxt(rutasArchivos.getStringBufferInconsistenciasFURIPS2(), rutasArchivos.getNombreArchivoInconsistenciasFURIPS2(), rutasArchivos.getRutaFURIPS2());
			
			if(!archivoGenerado)
		    {
				logger.info("NO GENERO EL ARCHIVO INCONSISTENCIAS FURIPS 2 !!!!!!");
				rutasArchivos.setProcesoExitoso(false);
				return rutasArchivos;
		    }
			else
			{
				rutasArchivos.setCreoArchivoInconsistenciasFURIPS2(true);
		    }
		}	
		
		logger.info("CREÓ CON EXITO EL FURIPS 2!!!");
		rutasArchivos.setProcesoExitoso(true);
		return rutasArchivos;
		
	}
	
	/**
	 * Metodo que carga toda la información pertinente para generar el archivo FURPRO
	 * @param con
	 * @param mapaBusqueda
	 * @param institucionBasica
	 * @param institucion
	 * @return
	 */
	public RutasArchivosFURIPS consultaFURPRO(Connection con, HashMap<Object, Object> mapaBusqueda, InstitucionBasica institucionBasica, int institucion, RutasArchivosFURIPS rutasArchivos,String reclamacion)
	{
		ArrayList<DtoFURPRO> arrayDto= planosDao.consultaFURPRO(con, mapaBusqueda, institucionBasica, institucion,reclamacion);
		Vector<StringBuffer> buffersVector=armarFURPRO(arrayDto);
		rutasArchivos.getStringBufferFURPRO().append(buffersVector.get(0));
		rutasArchivos.getStringBufferInconsistenciasFURPRO().append(buffersVector.get(1));
	
		if(UtilidadTexto.isEmpty(rutasArchivos.getStringBufferFURPRO().toString().trim()) && UtilidadTexto.isEmpty(rutasArchivos.getStringBufferInconsistenciasFURPRO().toString().trim()))
		{
			//es exitoso pero no debe crear los archivos
			rutasArchivos.setProcesoExitoso(true);
			return rutasArchivos;
		}
		
		boolean archivoGenerado=util.TxtFile.generarTxt(rutasArchivos.getStringBufferFURPRO(), rutasArchivos.getNombreArchivoFURPRO(), rutasArchivos.getRutaFURPRO());
		
		if(!archivoGenerado)
	    {
			logger.info("NO GENERO EL ARCHIVO FURPRO !!!!!!");
			rutasArchivos.setProcesoExitoso(false);
			return rutasArchivos;
	    }
		
		if(!UtilidadTexto.isEmpty(rutasArchivos.getStringBufferInconsistenciasFURPRO().toString().trim()))
		{
			//creamos el archivo de inconsistencias
			archivoGenerado=util.TxtFile.generarTxt(rutasArchivos.getStringBufferInconsistenciasFURPRO(), rutasArchivos.getNombreArchivoInconsistenciasFURPRO(), rutasArchivos.getRutaFURPRO());
			
			if(!archivoGenerado)
		    {
				logger.info("NO GENERO EL ARCHIVO INCONSISTENCIAS FURPRO !!!!!!");
				rutasArchivos.setProcesoExitoso(false);
				return rutasArchivos;
		    }
			else
			{
				rutasArchivos.setCreoArchivoInconsistenciasFURPRO(true);
		    }
		}
		logger.info("CREÓ CON EXITO EL FURPRO!!!");
		rutasArchivos.setProcesoExitoso(true);
		return rutasArchivos;
		
	}
	
	/**
	 * 
	 * @param arrayDto
	 * @return
	 */
	private Vector<StringBuffer> armarFURPRO(ArrayList<DtoFURPRO> arrayDto) 
	{
		Vector<StringBuffer> buffersVector= new Vector<StringBuffer>();
		StringBuffer furipsStrBuffer= new StringBuffer();
		StringBuffer inconsistenciaStrBuffer= new StringBuffer();
		String archivoPlano="FU";
		
		for(int w=0; w<arrayDto.size(); w++)
		{
			DtoFURPRO dto= arrayDto.get(w);
			String cadena="";
			
			String error="";
			
			//se puede usar la misma definiciond e logs para furpro.
			Vector<DetalleInconsistenciaFURIPS> erroresVector= new Vector<DetalleInconsistenciaFURIPS>();
			int tamanioMaximo=0;
			
			//1
			tamanioMaximo=10;
			if(!dto.getRg().isEmpty())
			{
				error= esValidoRequeridoYTamanioNoDesplazados(subStr(dto.getNroRadicadoAnterior(),tamanioMaximo), true, tamanioMaximo, dto.isEsDesplazado(), 1, dto.nombresCampos[1]);
				if(!error.isEmpty())
				{	
					erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[1], error));
					cadena+="1_INCONSISTENCIA,";
				}
				else
				{
					cadena+=subStr(dto.getNroRadicadoAnterior(),tamanioMaximo)+",";
				}
			}
			else
			{
				error= esValidoRequeridoYTamanioNoDesplazados(subStr(dto.getNroRadicadoAnterior(),tamanioMaximo), false, tamanioMaximo, dto.isEsDesplazado(), 1, dto.nombresCampos[1]);
				if(!error.isEmpty())
				{	
					erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[1], error));
					cadena+="1_INCONSISTENCIA,";
				}
				else
				{
					cadena+=subStr(dto.getNroRadicadoAnterior(),tamanioMaximo)+",";
				}
			}
			
			//2
			tamanioMaximo=1;
			error= esValidoRequeridoYTamanioNoDesplazados(subStr(dto.getRg(), tamanioMaximo), false, tamanioMaximo, dto.isEsDesplazado(), 2, dto.nombresCampos[2]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[2], error));
				cadena+="2_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getRg(), tamanioMaximo)+",";
			
			//3
			tamanioMaximo=20;
			error= esValidoRequeridoYTamanioNoDesplazados(subStr(dto.getNroFactura_nroCxC(), tamanioMaximo), true, tamanioMaximo, dto.isEsDesplazado(), 3, dto.nombresCampos[3]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[3], error));
				cadena+="3_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getNroFactura_nroCxC(), tamanioMaximo)+",";
			
			//4
			tamanioMaximo=12;
			error= esValidoRequeridoYTamanioNoDesplazados(subStr(dto.getCodHabilitacionPrestadorServ(),tamanioMaximo), true, tamanioMaximo, dto.isEsDesplazado(), 4, dto.nombresCampos[4]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[4], error));
				cadena+="4_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getCodHabilitacionPrestadorServ(),tamanioMaximo)+",";
			
			//5
			tamanioMaximo=20;
			error= esValidoRequeridoYTamanio(subStr(dto.getPrimerApellidoVictima(),tamanioMaximo), true, tamanioMaximo, 5, dto.nombresCampos[5]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[5], error));
				cadena+="5_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getPrimerApellidoVictima(),tamanioMaximo)+",";
			
				
			//6
			tamanioMaximo=30;
			error= esValidoRequeridoYTamanio(subStr(dto.getSegundoApellidoVictima(),tamanioMaximo), false, tamanioMaximo, 6, dto.nombresCampos[6]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[6], error));
				cadena+="6_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getSegundoApellidoVictima(),tamanioMaximo)+",";
			
			//7
			tamanioMaximo=20;
			error= esValidoRequeridoYTamanio(subStr(dto.getPrimerNombreVictima(),tamanioMaximo), true, tamanioMaximo, 7, dto.nombresCampos[7]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[7], error));
				cadena+="7_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getPrimerNombreVictima(),tamanioMaximo)+",";
			
			//8
			tamanioMaximo=30;
			error= esValidoRequeridoYTamanio(subStr(dto.getSegundoNombreVictima(),tamanioMaximo), false, tamanioMaximo, 8, dto.nombresCampos[8]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[8], error));
				cadena+="8_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getSegundoNombreVictima(),tamanioMaximo)+",";
			
			//9
			tamanioMaximo=2;
			error= esValidoRequeridoYTamanio(subStr(dto.getTipoDocIdVictima(),tamanioMaximo), true, tamanioMaximo, 9, dto.nombresCampos[9]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[9], error));
				cadena+="9_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getTipoDocIdVictima(),tamanioMaximo)+",";
			
			//10
			tamanioMaximo=16;
			error= esValidoRequeridoYTamanio(subStr(dto.getNumDocVictima(),tamanioMaximo), true, tamanioMaximo, 10, dto.nombresCampos[10]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[10], error));
				cadena+="10_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getNumDocVictima(),tamanioMaximo)+",";
			
			//11
			tamanioMaximo=10;
			error= esValidoRequeridoYTamanio(subStr(dto.getFechaNacVictima(),tamanioMaximo), true, tamanioMaximo, 11, dto.nombresCampos[11]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[11], error));
				cadena+="11_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getFechaNacVictima(),tamanioMaximo)+",";
			
			//12
			tamanioMaximo=1;
			error= esValidoRequeridoYTamanio(subStr(dto.getSexoVictima(),tamanioMaximo), true, tamanioMaximo, 12, dto.nombresCampos[12]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[12], error));
				cadena+="12_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getSexoVictima(),tamanioMaximo)+",";
			
			//13
			tamanioMaximo=40;
			error= esValidoRequeridoYTamanio(subStr(dto.getDirResidenciaVictima(),tamanioMaximo), false, tamanioMaximo, 13, dto.nombresCampos[13]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[13], error));
				cadena+="13_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getDirResidenciaVictima(), tamanioMaximo)+",";
			
			//14
			tamanioMaximo=2;
			error= esValidoRequeridoYTamanio(subStr(dto.getCodDeptoResidenciaVictima(),tamanioMaximo), true, tamanioMaximo, 14, dto.nombresCampos[14]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[14], error));
				cadena+="14_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getCodDeptoResidenciaVictima(),tamanioMaximo)+",";
			
			//15
			tamanioMaximo=3;
			error= esValidoRequeridoYTamanio(subStr(dto.getCodMunicipioResidenciaVictima(),tamanioMaximo), true, tamanioMaximo, 15, dto.nombresCampos[15]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[15], error));
				cadena+="15_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getCodMunicipioResidenciaVictima(),tamanioMaximo)+",";
			
			//16
			tamanioMaximo=10;
			error= esValidoRequeridoYTamanio(subStr(dto.getTelVictima(),tamanioMaximo), false, tamanioMaximo, 16, dto.nombresCampos[16]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[16], error));
				cadena+="16_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getTelVictima(),tamanioMaximo)+",";
			
			//17
			tamanioMaximo=1;
			error= esValidoRequeridoYTamanio(subStr(dto.getSGSSS(),tamanioMaximo), true, tamanioMaximo, 17, dto.nombresCampos[17]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[17], error));
				cadena+="17_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getSGSSS(),tamanioMaximo)+",";
			
			//18
			tamanioMaximo=1;
			error= esValidoRequeridoYTamanio(subStr(dto.getRegimenVictima(),tamanioMaximo), dto.getSGSSS().equals("1"), tamanioMaximo, 18, dto.nombresCampos[18]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[18], error));
				cadena+="18_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getRegimenVictima(),tamanioMaximo)+",";
			
			//19
			tamanioMaximo=6;
			error= esValidoRequeridoYTamanio(subStr(dto.getCodEPS_EOC(),tamanioMaximo), dto.getSGSSS().equals("1"), tamanioMaximo, 19, dto.nombresCampos[19]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[19], error));
				cadena+="19_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getCodEPS_EOC(),tamanioMaximo)+",";
			
			//20
			tamanioMaximo=2;
			error= esValidoRequeridoYTamanio(subStr(dto.getNaturalezaEvento(),tamanioMaximo), true, tamanioMaximo, 20, dto.nombresCampos[20]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[20], error));
				cadena+="20_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getNaturalezaEvento(),tamanioMaximo)+",";
			
			
			//21
			tamanioMaximo=25;
			if(dto.getNaturalezaEvento().equals("17"))
				error= esValidoRequeridoYTamanioNoDesplazados(subStr(dto.getDescOtroEvento(),tamanioMaximo), true, tamanioMaximo, dto.isEsDesplazado(), 21, dto.nombresCampos[21]);
			else
				error= esValidoRequeridoYTamanioNoDesplazados(subStr(dto.getDescOtroEvento(),tamanioMaximo), false, tamanioMaximo, dto.isEsDesplazado(), 21, dto.nombresCampos[21]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[21], error));
				cadena+="21_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getDescOtroEvento(),tamanioMaximo)+",";
			
			//22
			tamanioMaximo=40;
			error= esValidoRequeridoYTamanioNoDesplazados(subStr(dto.getDirOcurrenciaVictima(),tamanioMaximo), true, tamanioMaximo, dto.isEsDesplazado(), 22, dto.nombresCampos[22]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[22], error));
				cadena+="22_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getDirOcurrenciaVictima(),tamanioMaximo)+",";
			
			//23
			tamanioMaximo=10;
			error= esValidoRequeridoYTamanioNoDesplazados(subStr(dto.getFechaEvento(),tamanioMaximo), true, tamanioMaximo, dto.isEsDesplazado(), 23, dto.nombresCampos[23]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[23], error));
				cadena+="23_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getFechaEvento(),tamanioMaximo)+",";
			
			//24
			tamanioMaximo=2;
			error= esValidoRequeridoYTamanioNoDesplazados(subStr(dto.getCodDeptoOcurrenciaEvento(),tamanioMaximo), true, tamanioMaximo, dto.isEsDesplazado(), 24, dto.nombresCampos[24]);
			if(!error.isEmpty())
			{
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[24], error));
				cadena+="24_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getCodDeptoOcurrenciaEvento(),tamanioMaximo)+",";
			
			//25
			tamanioMaximo=3;
			error= esValidoRequeridoYTamanioNoDesplazados(subStr(dto.getCodMunicipioOcurrioEvento(),tamanioMaximo), true, tamanioMaximo, dto.isEsDesplazado(), 25, dto.nombresCampos[25]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[25], error));
				cadena+="25_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getCodMunicipioOcurrioEvento(),tamanioMaximo)+",";
				
			//26
			tamanioMaximo=1;
			error= esValidoRequeridoYTamanioNoDesplazados(subStr(dto.getZonaEvento(),tamanioMaximo), true, tamanioMaximo, dto.isEsDesplazado(), 26, dto.nombresCampos[26]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[26], error));
				cadena+="26_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getZonaEvento(),tamanioMaximo)+",";
			
			//27
			tamanioMaximo=1;
			error= esValidoRequeridoYTamanioNoDesplazados(subStr(dto.getDescBreveEvento(),tamanioMaximo), true, tamanioMaximo,dto.isEsDesplazado(), 27, dto.nombresCampos[27]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[27], error));
				cadena+="27_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getDescBreveEvento(),tamanioMaximo)+",";
			
			//28
			tamanioMaximo=4;
			error= esValidoRequeridoYTamanioNoDesplazados(subStr(dto.getCodDxPpal(),tamanioMaximo), true, tamanioMaximo,dto.isEsDesplazado(), 28, dto.nombresCampos[28]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[28], error));
				cadena+="28_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getCodDxPpal(),tamanioMaximo)+",";
			
			tamanioMaximo=4;
			error= esValidoRequeridoYTamanioNoDesplazados(subStr(dto.getCodDxAsociado1(),tamanioMaximo), false, tamanioMaximo,dto.isEsDesplazado(), 29, dto.nombresCampos[29]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[29], error));
				cadena+="29_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getCodDxAsociado1(),tamanioMaximo)+",";
			
			tamanioMaximo=4;
			error= esValidoRequeridoYTamanioNoDesplazados(subStr(dto.getCodDxAsociado2(),tamanioMaximo), false, tamanioMaximo,dto.isEsDesplazado(), 30, dto.nombresCampos[30]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[30], error));
				cadena+="30_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getCodDxAsociado2(),tamanioMaximo)+",";
			
			tamanioMaximo=4;
			error= esValidoRequeridoYTamanioNoDesplazados(subStr(dto.getCodDxAsociado3(),tamanioMaximo), false, tamanioMaximo,dto.isEsDesplazado(), 31, dto.nombresCampos[31]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[31], error));
				cadena+="31_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getCodDxAsociado3(),tamanioMaximo)+",";
			
			tamanioMaximo=4;
			error= esValidoRequeridoYTamanioNoDesplazados(subStr(dto.getCodDxAsociado4(),tamanioMaximo), false, tamanioMaximo,dto.isEsDesplazado(), 32, dto.nombresCampos[32]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[32], error));
				cadena+="32_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getCodDxAsociado4(),tamanioMaximo)+",";
			
			tamanioMaximo=100;
			error= esValidoRequeridoYTamanioNoDesplazados(subStr(dto.getDescProtesisOServ(),tamanioMaximo), true, tamanioMaximo,dto.isEsDesplazado(), 33, dto.nombresCampos[33]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[33], error));
				cadena+="33_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getDescProtesisOServ(),tamanioMaximo)+",";
			
			tamanioMaximo=15;
			error= esValidoRequeridoYTamanioNoDesplazados(subStr(dto.getValorReclamadoXprotesis(),tamanioMaximo), true, tamanioMaximo,dto.isEsDesplazado(), 34, dto.nombresCampos[34]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[34], error));
				cadena+="34_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getValorReclamadoXprotesis(),tamanioMaximo)+",";
			
			tamanioMaximo=35;
			error= esValidoRequeridoYTamanioNoDesplazados(subStr(dto.getValorReclamadoXadaptacionProtesis(),tamanioMaximo), true, tamanioMaximo,dto.isEsDesplazado(), 35, dto.nombresCampos[35]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[35], error));
				cadena+="35_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getValorReclamadoXadaptacionProtesis(),tamanioMaximo)+",";
			
			tamanioMaximo=15;
			error= esValidoRequeridoYTamanioNoDesplazados(subStr(dto.getValorReclamadoXrehabilitacion(),tamanioMaximo), true, tamanioMaximo,dto.isEsDesplazado(), 36, dto.nombresCampos[36]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[36], error));
				cadena+="36_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getValorReclamadoXrehabilitacion(),tamanioMaximo)+",";
			
			tamanioMaximo=15;
			error= esValidoRequeridoYTamanioNoDesplazados(subStr(dto.getValorTotalReclamado(),tamanioMaximo), true, tamanioMaximo,dto.isEsDesplazado(), 37, dto.nombresCampos[37]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[37], error));
				cadena+="37_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getValorTotalReclamado(),tamanioMaximo)+",";
			
			//38
			tamanioMaximo=3;
			error= esValidoRequeridoYTamanioNoDesplazados(subStr(dto.getTotalFolios(),tamanioMaximo), true, tamanioMaximo, dto.isEsDesplazado(), 38, dto.nombresCampos[38]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[38], error));
				cadena+="38_INCONSISTENCIA";
			}
			else
				cadena+=subStr(dto.getTotalFolios(),tamanioMaximo);
			
			if(erroresVector.size()>0)
			{
				inconsistenciaStrBuffer.append(armarDetalleInconsistenciaFURPRO(dto, erroresVector));
			}
			
			furipsStrBuffer.append(cadena+"\n");
			
		}
		
		buffersVector.add(furipsStrBuffer);
		buffersVector.add(inconsistenciaStrBuffer);
		
		return buffersVector;
	}

	private String armarDetalleInconsistenciaFURPRO(DtoFURPRO dto,Vector<DetalleInconsistenciaFURIPS> erroresVector) 
	{
		String inconsistencia= 	"Nro Factura: "+dto.getIdFactura()+
		"\nCuenta Cobro: "+dto.getIdCuentaCobro()+
		"\nTipo y Numero de identificacion del paciente: "+dto.getTipoDocIdVictima()+" "+dto.getNumDocVictima()+
		"\nNombres y Apellidos del Paciente: "+dto.getPrimerNombreVictima()+" "+dto.getSegundoNombreVictima()+" "+dto.getPrimerApellidoVictima()+" "+dto.getSegundoApellidoVictima()+
		"\nNro.Cuenta Paciente: "+dto.getIdCuenta()+
		"\nNaturaleza Evento: "+dto.getNaturalezaEvento()+
		"\nVia Ingreso: "+dto.getNombreViaIngreso()+"\n\n";

		for(int w=0; w<erroresVector.size();w++)
		{
		DetalleInconsistenciaFURIPS detalle= erroresVector.get(w);
		inconsistencia+=//"Archivo Plano: "+detalle.getArchivoPlano()+"" +
			"Nombre del campo: "+detalle.getNombreCampo()+"" +
			"\nObservaciones: "+detalle.getObservaciones()+"\n";
		}
		inconsistencia+="\n";
		return inconsistencia.toUpperCase();
	}

	/**
	 * Metodo que carga toda la información pertinente para generar el archivo FURTRAN
	 * @param con
	 * @param mapaBusqueda
	 * @param institucionBasica
	 * @param institucion
	 * @return
	 */
	public RutasArchivosFURIPS consultaFURTRAN(Connection con, HashMap<Object, Object> mapaBusqueda, InstitucionBasica institucionBasica, int institucion, RutasArchivosFURIPS rutasArchivos)
	{
		//@todo descomentariar para probar
		///ArrayList<DtoFURTRAN> arrayDto= planosDao.consultaFURTRAN(con, mapaBusqueda, institucionBasica, institucion);
		///Vector<StringBuffer> buffersVector=armarFURTRAN(arrayDto);
		///furipsStrBuffer=buffersVector.get(0);
		///inconsistenciaStrBuffer= buffersVector.get(1);
		
		rutasArchivos.getStringBufferFURTRAN().append("PRUEBA DE FURTRAN!!!!!");
		rutasArchivos.getStringBufferInconsistenciasFURTRAN().append("PRUEBA DE INCONSISTENCIAS FURTRAN!!!!");
	
		if(UtilidadTexto.isEmpty(rutasArchivos.getStringBufferFURTRAN().toString().trim()) && UtilidadTexto.isEmpty(rutasArchivos.getStringBufferInconsistenciasFURTRAN().toString().trim()))
		{
			//es exitoso pero no debe crear los archivos
			rutasArchivos.setProcesoExitoso(true);
			return rutasArchivos;
		}
		
		boolean archivoGenerado=util.TxtFile.generarTxt(rutasArchivos.getStringBufferFURTRAN(), rutasArchivos.getNombreArchivoFURTRAN(), rutasArchivos.getRutaFURTRAN());
		
		if(!archivoGenerado)
	    {
			logger.info("NO GENERO EL ARCHIVO FURTRAN !!!!!!");
			rutasArchivos.setProcesoExitoso(false);
			return rutasArchivos;
	    }
		
		if(!UtilidadTexto.isEmpty(rutasArchivos.getStringBufferInconsistenciasFURTRAN().toString().trim()))
		{
			//creamos el archivo de inconsistencias
			archivoGenerado=util.TxtFile.generarTxt(rutasArchivos.getStringBufferInconsistenciasFURTRAN(), rutasArchivos.getNombreArchivoInconsistenciasFURTRAN(), rutasArchivos.getRutaFURTRAN());
			
			if(!archivoGenerado)
		    {
				logger.info("NO GENERO EL ARCHIVO INCONSISTENCIAS FURTRAN !!!!!!");
				rutasArchivos.setProcesoExitoso(false);
				return rutasArchivos;
		    }
			else
			{
				rutasArchivos.setCreoArchivoInconsistenciasFURTRAN(true);
		    }
		}	
		
		logger.info("CREÓ CON EXITO EL FURTRAN!!!");
		rutasArchivos.setProcesoExitoso(true);
		return rutasArchivos;
		
	}
	
	/**
	 * 
	 * @param arrayDto
	 * @return
	 */
	private Vector<StringBuffer> armarFURIPS2(ArrayList<DtoFURIPS2> arrayDto)
	{
		Vector<StringBuffer> buffersVector= new Vector<StringBuffer>();
		StringBuffer furipsStrBuffer= new StringBuffer();
		StringBuffer inconsistenciaStrBuffer= new StringBuffer();
		String archivoPlano="FU";
		
		for(int w=0; w<arrayDto.size(); w++)
		{
			String cadena="";
			DtoFURIPS2 dto= arrayDto.get(w);
			
			String error="";
			Vector<DetalleInconsistenciaFURIPS> erroresVector= new Vector<DetalleInconsistenciaFURIPS>();
			int tamanioMaximo=0;
			
			//1
			tamanioMaximo=20;
			
			error= esValidoRequeridoYTamanio(subStr(dto.getNumeroFacturaNumeroCXC(),tamanioMaximo), true, tamanioMaximo, 1, dto.nombresCampos[1]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[1], error, dto.getCodigoServicio()));
				cadena+="1_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getNumeroFacturaNumeroCXC(),tamanioMaximo)+",";	
			
			//2
			tamanioMaximo=12;
			error= esValidoRequeridoYTamanioNoDesplazados(subStr(dto.getNumeroConsecutivoReclamacion(),tamanioMaximo), false, tamanioMaximo, dto.isEsDesplazado(), 2, dto.nombresCampos[2]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[2], error, dto.getCodigoServicio()));
				cadena+="2_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getNumeroConsecutivoReclamacion(),tamanioMaximo)+",";
			
			//3
			tamanioMaximo=1;
			error= esValidoRequeridoYTamanio(subStr(dto.getTipoServicio(),tamanioMaximo), true, tamanioMaximo, 3, dto.nombresCampos[3]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[3], error, dto.getCodigoServicio()));
				cadena+="3_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getTipoServicio(),tamanioMaximo)+",";
			
			//4
			tamanioMaximo=15;
			error= esValidoRequeridoYTamanio(subStr(dto.getCodigoServicio(),tamanioMaximo), true, tamanioMaximo, 4, dto.nombresCampos[4]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[4], error, dto.getCodigoServicio()));
				cadena+="4_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getCodigoServicio(),tamanioMaximo)+",";
			
			
			//5
			tamanioMaximo=40;
			error= esValidoRequeridoYTamanio(subStr(dto.getDescripcionInsumo(),tamanioMaximo), dto.getTipoServicio().equals("5"), tamanioMaximo, 5, dto.nombresCampos[5]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[5], error, dto.getCodigoServicio()));
				cadena+="5_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getDescripcionInsumo(),tamanioMaximo)+",";
			
			
			//6
			tamanioMaximo=15;
			error= esValidoRequeridoYTamanio(dto.getCantidadServicio(), true, 15, 6, dto.nombresCampos[6]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[6], error, dto.getCodigoServicio()));
				cadena+="6_INCONSISTENCIA,";
			}
			else
				cadena+=dto.getCantidadServicio()+",";
			
			//7
			error= esValidoRequeridoYTamanio(dto.getValorUnitario(), true, 15, 7, dto.nombresCampos[7]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[7], error, dto.getCodigoServicio()));
				cadena+="7_INCONSISTENCIA,";
			}
			else
				cadena+=dto.getValorUnitario()+",";
			
			//8
			error= esValidoRequeridoYTamanio(dto.getValorTotalFacgturado(), true, 15, 8, dto.nombresCampos[8]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[8], error, dto.getCodigoServicio()));
				cadena+="8_INCONSISTENCIA,";
			}
			else
				cadena+=dto.getValorTotalFacgturado()+",";
			
			//9
			error= esValidoRequeridoYTamanio(dto.getValorTotalReclamadoFosyga(), true, 15, 9, dto.nombresCampos[9]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[9], error, dto.getCodigoServicio()));
				cadena+="9_INCONSISTENCIA,";
			}
			else
				cadena+=dto.getValorTotalReclamadoFosyga();
			if(erroresVector.size()>0)
			{
				inconsistenciaStrBuffer.append(armarDetalleInconsistencia2(dto, erroresVector));
			}
			
			
			furipsStrBuffer.append(cadena+"\n");
			
		}
		
		buffersVector.add(furipsStrBuffer);
		buffersVector.add(inconsistenciaStrBuffer);
		
		return buffersVector;
	}	
	
	
	/**
	 * 
	 * @param arrayDto
	 * @return
	 */
	private Vector<StringBuffer> armarFURIPS1(ArrayList<DtoFURIPS1> arrayDto)
	{
		Vector<StringBuffer> buffersVector= new Vector<StringBuffer>();
		StringBuffer furipsStrBuffer= new StringBuffer();
		StringBuffer inconsistenciaStrBuffer= new StringBuffer();
		String archivoPlano="FU";
		
		for(int w=0; w<arrayDto.size(); w++)
		{
			DtoFURIPS1 dto= arrayDto.get(w);
			String cadena="";
			
			String error="";
			Vector<DetalleInconsistenciaFURIPS> erroresVector= new Vector<DetalleInconsistenciaFURIPS>();
			int tamanioMaximo=0;
			
			//1
			tamanioMaximo=10;
			if(!dto.getRespuestaAGlosa().isEmpty())
			{
				error= esValidoRequeridoYTamanioNoDesplazados(subStr(dto.getNumeroRadicadoAnterior(),tamanioMaximo), true, tamanioMaximo, dto.isEsDesplazado(), 1, dto.nombresCampos[1]);
				if(!error.isEmpty())
				{	
					erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[1], error));
					cadena+="1_INCONSISTENCIA,";
				}
				else
				{
					cadena+=subStr(dto.getNumeroRadicadoAnterior(),tamanioMaximo)+",";
				}
			}
			else
			{
				error= esValidoRequeridoYTamanioNoDesplazados(subStr(dto.getNumeroRadicadoAnterior(),tamanioMaximo), false, tamanioMaximo, dto.isEsDesplazado(), 1, dto.nombresCampos[1]);
				if(!error.isEmpty())
				{	
					erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[1], error));
					cadena+="1_INCONSISTENCIA,";
				}
				else
				{
					cadena+=subStr(dto.getNumeroRadicadoAnterior(),tamanioMaximo)+",";
				}
			}
			
			//2
			tamanioMaximo=1;
			error= esValidoRequeridoYTamanioNoDesplazados(subStr(dto.getRespuestaAGlosa(), tamanioMaximo), false, tamanioMaximo, dto.isEsDesplazado(), 2, dto.nombresCampos[2]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[2], error));
				cadena+="2_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getRespuestaAGlosa(), tamanioMaximo)+",";
			
			//3
			String nombreCampo="";
			tamanioMaximo=20;
			nombreCampo=dto.isEsInstitucionPublica()?"Numero Cuenta Cobro":"Numero de Factura";
			logger.info("****************************NOMBRE CAMPO:"+nombreCampo);
			
			error= esValidoRequeridoYTamanioNoDesplazados(subStr(dto.getNumeroFacturaCuentaCobro(), tamanioMaximo), true, tamanioMaximo, dto.isEsDesplazado(), 3, nombreCampo);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, nombreCampo, error));
				cadena+="3_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getNumeroFacturaCuentaCobro(), tamanioMaximo)+",";
			
			//4
			tamanioMaximo=12;
			error= esValidoRequeridoYTamanioNoDesplazados(subStr(dto.getNumeroConsecutivoReclamacion(),tamanioMaximo), true, tamanioMaximo, dto.isEsDesplazado(), 4, dto.nombresCampos[4]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[4], error));
				cadena+="4_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getNumeroConsecutivoReclamacion(),tamanioMaximo)+",";
			
			//5
			tamanioMaximo=12;
			error= esValidoRequeridoYTamanio(subStr(dto.getCodigoHabilitacionPrestadorServicioDeSalud(),tamanioMaximo), true, tamanioMaximo, 5, dto.nombresCampos[5]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[5], error));
				cadena+="5_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getCodigoHabilitacionPrestadorServicioDeSalud(),tamanioMaximo)+",";
			
			//6
			tamanioMaximo=20;
			error= esValidoRequeridoYTamanio(subStr(dto.getPrimerApellidoVictima(),tamanioMaximo), true, tamanioMaximo, 6, dto.nombresCampos[6]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[6], error));
				cadena+="6_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getPrimerApellidoVictima(),tamanioMaximo)+",";
				
			//7
			tamanioMaximo=30;
			error= esValidoRequeridoYTamanio(subStr(dto.getSegundoApellidoVictima(),tamanioMaximo), false, tamanioMaximo, 7, dto.nombresCampos[7]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[7], error));
				cadena+="7_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getSegundoApellidoVictima(),tamanioMaximo)+",";
			
			//8
			tamanioMaximo=20;
			error= esValidoRequeridoYTamanio(subStr(dto.getPrimerNombreVictima(),tamanioMaximo), true, tamanioMaximo, 8, dto.nombresCampos[8]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[8], error));
				cadena+="8_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getPrimerNombreVictima(),tamanioMaximo)+",";
			
			//9
			tamanioMaximo=30;
			error= esValidoRequeridoYTamanio(subStr(dto.getSegundoNombreVictima(),tamanioMaximo), false, tamanioMaximo, 9, dto.nombresCampos[9]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[9], error));
				cadena+="9_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getSegundoNombreVictima(),tamanioMaximo)+",";
			
			//10
			tamanioMaximo=2;
			error= esValidoRequeridoYTamanio(subStr(dto.getTipoDocumentoVictima(),tamanioMaximo), true, tamanioMaximo, 10, dto.nombresCampos[10]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[10], error));
				cadena+="10_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getTipoDocumentoVictima(),tamanioMaximo)+",";
			
			//11
			tamanioMaximo=16;
			error= esValidoRequeridoYTamanio(subStr(dto.getNumeroDocumentoVictima(),tamanioMaximo), true, tamanioMaximo, 11, dto.nombresCampos[11]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[11], error));
				cadena+="11_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getNumeroDocumentoVictima(),tamanioMaximo)+",";
			
			//12
			tamanioMaximo=10;
			error= esValidoRequeridoYTamanio(subStr(dto.getFechaNacimientoVictima(),tamanioMaximo), true, tamanioMaximo, 12, dto.nombresCampos[12]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[12], error));
				cadena+="12_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getFechaNacimientoVictima(),tamanioMaximo)+",";
			
			//13
			tamanioMaximo=1;
			error= esValidoRequeridoYTamanio(subStr(dto.getSexoVictima(),tamanioMaximo), true, tamanioMaximo, 13, dto.nombresCampos[13]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[13], error));
				cadena+="13_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getSexoVictima(),tamanioMaximo)+",";
			
			//14
			tamanioMaximo=40;
			error= esValidoRequeridoYTamanio(subStr(dto.getDireccionResidenciaVictima(),tamanioMaximo), false, tamanioMaximo, 14, dto.nombresCampos[14]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[14], error));
				cadena+="14_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getDireccionResidenciaVictima(), tamanioMaximo)+",";
			
			//15
			tamanioMaximo=2;
			error= esValidoRequeridoYTamanio(subStr(dto.getCodigoDepartamentoResidenciaVictima(),tamanioMaximo), true, tamanioMaximo, 15, dto.nombresCampos[15]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[15], error));
				cadena+="15_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getCodigoDepartamentoResidenciaVictima(),tamanioMaximo)+",";
			
			//16
			tamanioMaximo=3;
			error= esValidoRequeridoYTamanio(subStr(dto.getCodigoMunicipioResidenciaVictima(),tamanioMaximo), true, tamanioMaximo, 16, dto.nombresCampos[16]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[16], error));
				cadena+="16_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getCodigoMunicipioResidenciaVictima(),tamanioMaximo)+",";
			
			//17
			tamanioMaximo=10;
			error= esValidoRequeridoYTamanio(subStr(dto.getTelefonoVictima(),tamanioMaximo), false, tamanioMaximo, 17, dto.nombresCampos[17]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[17], error));
				cadena+="17_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getTelefonoVictima(),tamanioMaximo)+",";
			
			//18
			tamanioMaximo=1;
			error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getCodicionAccidentado(),tamanioMaximo), true, tamanioMaximo, 18, dto.isEsAccidenteTransito(), dto.nombresCampos[18]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[18], error));
				cadena+="18_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getCodicionAccidentado(),tamanioMaximo)+",";
			
			//19
			tamanioMaximo=2;
			error= esValidoRequeridoYTamanio(subStr(dto.getNaturalezaEvento(),tamanioMaximo), true, tamanioMaximo, 19, dto.nombresCampos[19]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[19], error));
				cadena+="19_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getNaturalezaEvento(),tamanioMaximo)+",";
			
			//20
			tamanioMaximo=25;
			if(dto.getNaturalezaEvento().equals("17"))
				error= esValidoRequeridoYTamanioNoDesplazados(subStr(dto.getDescripcionOtroEvento(),tamanioMaximo), true, tamanioMaximo, dto.isEsDesplazado(), 20, dto.nombresCampos[20]);
			else
				error= esValidoRequeridoYTamanioNoDesplazados(subStr(dto.getDescripcionOtroEvento(),tamanioMaximo), false, tamanioMaximo, dto.isEsDesplazado(), 20, dto.nombresCampos[20]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[20], error));
				cadena+="20_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getDescripcionOtroEvento(),tamanioMaximo)+",";
			
			//21
			tamanioMaximo=40;
			error= esValidoRequeridoYTamanioNoDesplazados(subStr(dto.getDireccionOcurrenciaEvento(),tamanioMaximo), true, tamanioMaximo, dto.isEsDesplazado(), 21, dto.nombresCampos[21]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[21], error));
				cadena+="21_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getDireccionOcurrenciaEvento(),tamanioMaximo)+",";
			
			//22
			tamanioMaximo=10;
			error= esValidoRequeridoYTamanioNoDesplazados(subStr(dto.getFechaOcurrenciaEvento(),tamanioMaximo), true, tamanioMaximo, dto.isEsDesplazado(), 22, dto.nombresCampos[22]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[22], error));
				cadena+="22_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getFechaOcurrenciaEvento(),tamanioMaximo)+",";
			
			//23
			tamanioMaximo=5;
			error= esValidoRequeridoYTamanioNoDesplazados(subStr(dto.getHoraOcurrenciaEvento(),tamanioMaximo), true, tamanioMaximo, dto.isEsDesplazado(), 23, dto.nombresCampos[23]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[23], error));
				cadena+="23_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getHoraOcurrenciaEvento(),tamanioMaximo)+",";
				
			//24
			tamanioMaximo=2;
			error= esValidoRequeridoYTamanioNoDesplazados(subStr(dto.getCodigoDepartamentoOcurrenciaEvento(),tamanioMaximo), true, tamanioMaximo, dto.isEsDesplazado(), 24, dto.nombresCampos[24]);
			if(!error.isEmpty())
			{
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[24], error));
				cadena+="24_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getCodigoDepartamentoOcurrenciaEvento(),tamanioMaximo)+",";
			
			//25
			tamanioMaximo=3;
			error= esValidoRequeridoYTamanioNoDesplazados(subStr(dto.getCodigoMunicipioOcorrenciaEvento(),tamanioMaximo), true, tamanioMaximo, dto.isEsDesplazado(), 25, dto.nombresCampos[25]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[25], error));
				cadena+="25_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getCodigoMunicipioOcorrenciaEvento(),tamanioMaximo)+",";
				
			//26
			tamanioMaximo=1;
			error= esValidoRequeridoYTamanioNoDesplazados(subStr(dto.getZonaOcurrenciaEvento(),tamanioMaximo), true, tamanioMaximo, dto.isEsDesplazado(), 26, dto.nombresCampos[26]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[26], error));
				cadena+="26_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getZonaOcurrenciaEvento(),tamanioMaximo)+",";
			
			//27
			tamanioMaximo=1;
			error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getEstadoAseguramiento(),tamanioMaximo), true, tamanioMaximo, 27, dto.isEsAccidenteTransito(), dto.nombresCampos[27]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[27], error));
				cadena+="27_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getEstadoAseguramiento(),tamanioMaximo)+",";
			
			//28
			tamanioMaximo=15;
			if(dto.getEstadoAseguramiento().equals("1") || dto.getEstadoAseguramiento().equals("2") || dto.getEstadoAseguramiento().equals("4"))
				error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getMarca(),tamanioMaximo), true, tamanioMaximo, 28, dto.isEsAccidenteTransito(), dto.nombresCampos[28]);
			else
				error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getMarca(),tamanioMaximo), false, tamanioMaximo, 28, dto.isEsAccidenteTransito(), dto.nombresCampos[28]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[28], error));
				cadena+="28_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getMarca(),tamanioMaximo)+",";
			
			//29
			tamanioMaximo=6;
			if(dto.getEstadoAseguramiento().equals("1") || dto.getEstadoAseguramiento().equals("2") || dto.getEstadoAseguramiento().equals("4") || dto.getEstadoAseguramiento().equals("5"))
				error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getPlaca(),tamanioMaximo), true, tamanioMaximo, 29, dto.isEsAccidenteTransito(), dto.nombresCampos[29]);
			else
				error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getPlaca(),tamanioMaximo), false, tamanioMaximo, 29, dto.isEsAccidenteTransito(), dto.nombresCampos[29]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[29], error));
				cadena+="29_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getPlaca(),tamanioMaximo)+",";
				
			//30
			tamanioMaximo=1;
			if(dto.getEstadoAseguramiento().equals("1") || dto.getEstadoAseguramiento().equals("2") || dto.getEstadoAseguramiento().equals("4") || dto.getEstadoAseguramiento().equals("5"))
				error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getTipoVehiculo(),tamanioMaximo), true, tamanioMaximo, 30, dto.isEsAccidenteTransito(), dto.nombresCampos[30]);
			else
				error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getTipoVehiculo(),tamanioMaximo), false, tamanioMaximo, 30, dto.isEsAccidenteTransito(), dto.nombresCampos[30]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[30], error));
				cadena+="30_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getTipoVehiculo(),tamanioMaximo)+",";
			
			//31
			tamanioMaximo=6;
			if(dto.getEstadoAseguramiento().equals("1") || dto.getEstadoAseguramiento().equals("4"))
				error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getCodigoAseguradora(),tamanioMaximo), true, tamanioMaximo, 31, dto.isEsAccidenteTransito(), dto.nombresCampos[31]);
			else
				error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getCodigoAseguradora(),tamanioMaximo), false, tamanioMaximo, 31, dto.isEsAccidenteTransito(), dto.nombresCampos[31]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[31], error));
				cadena+="31_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getCodigoAseguradora(),tamanioMaximo)+",";
			
			//32
			tamanioMaximo=20;
			if(dto.getEstadoAseguramiento().equals("1") || dto.getEstadoAseguramiento().equals("4"))
				error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getNumeroPolizaSOAT(),tamanioMaximo), true, tamanioMaximo, 32, dto.isEsAccidenteTransito(), dto.nombresCampos[32]);
			else
				error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getNumeroPolizaSOAT(),tamanioMaximo), false, tamanioMaximo, 32, dto.isEsAccidenteTransito(), dto.nombresCampos[32]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[32], error));
				cadena+="32_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getNumeroPolizaSOAT(),tamanioMaximo)+",";
			
			//33
			tamanioMaximo=10;
			if(dto.getEstadoAseguramiento().equals("1") || dto.getEstadoAseguramiento().equals("4"))
				error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getFechaInicioVigenciaPoliza(),tamanioMaximo), true, tamanioMaximo, 33, dto.isEsAccidenteTransito(), dto.nombresCampos[33]);
			else
				error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getFechaInicioVigenciaPoliza(),tamanioMaximo), false, tamanioMaximo, 33, dto.isEsAccidenteTransito(), dto.nombresCampos[33]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[33], error));
				cadena+="33_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getFechaInicioVigenciaPoliza(),tamanioMaximo)+",";
			
			//34
			tamanioMaximo=10;
			if(dto.getEstadoAseguramiento().equals("1") || dto.getEstadoAseguramiento().equals("4"))
				error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getFechaFinalVigenciaPoliza(),tamanioMaximo), true, tamanioMaximo, 34, dto.isEsAccidenteTransito(), dto.nombresCampos[34]);
			else
				error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getFechaFinalVigenciaPoliza(),tamanioMaximo), false, tamanioMaximo, 34, dto.isEsAccidenteTransito(), dto.nombresCampos[34]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[34], error));
				cadena+="34_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getFechaFinalVigenciaPoliza(),tamanioMaximo)+",";
			
			//35
			tamanioMaximo=1;
			error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getIntervencionAutoridad(),tamanioMaximo), true, tamanioMaximo, 35, dto.isEsAccidenteTransito(), dto.nombresCampos[35]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[35], error));
				cadena+="35_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getIntervencionAutoridad(),tamanioMaximo)+",";
			
			//36
			tamanioMaximo=1;
			error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getCobroExcedentePoliza(),tamanioMaximo), false, tamanioMaximo, 36, dto.isEsAccidenteTransito(), dto.nombresCampos[36]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[36], error));
				cadena+="36_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getCobroExcedentePoliza(),tamanioMaximo)+",";
			//37
			tamanioMaximo=6;
			error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getPlacaSegundoVehiculoInvolucrado(),tamanioMaximo), false, tamanioMaximo, 37, dto.isEsAccidenteTransito(), dto.nombresCampos[37]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[37], error));
				cadena+="37_INCONSISTENCIA,";
			}
			else
			{
				if(dto.getEstadoAseguramiento().equals("1") || dto.getEstadoAseguramiento().equals("2") || dto.getEstadoAseguramiento().equals("4") || dto.getEstadoAseguramiento().equals("5"))
					cadena+=subStr(dto.getPlacaSegundoVehiculoInvolucrado(),tamanioMaximo)+",";
				else
					cadena+=subStr("",tamanioMaximo)+",";
			}
			//38
			tamanioMaximo=2;
			error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getTipoDocumentoIdentidadPropietarioSegundoVehiculoInvolucrado(),tamanioMaximo), false, tamanioMaximo, 38, dto.isEsAccidenteTransito(), dto.nombresCampos[38]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[38], error));
				cadena+="38_INCONSISTENCIA,";
			}
			else
			{
				if(dto.getEstadoAseguramiento().equals("1") || dto.getEstadoAseguramiento().equals("2") || dto.getEstadoAseguramiento().equals("4"))
					cadena+=subStr(dto.getTipoDocumentoIdentidadPropietarioSegundoVehiculoInvolucrado(),tamanioMaximo)+",";
				else
					cadena+=subStr("",tamanioMaximo)+",";
			}
			
			//39
			tamanioMaximo=16;
			error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getNumeroDocumentoIdentidadPropietarioSegundoVehiculoInvolucrado(),tamanioMaximo), false, tamanioMaximo, 39, dto.isEsAccidenteTransito(), dto.nombresCampos[39]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[39], error));
				cadena+="39_INCONSISTENCIA,";
			}
			else
			{
				if(dto.getEstadoAseguramiento().equals("1") || dto.getEstadoAseguramiento().equals("2") || dto.getEstadoAseguramiento().equals("4"))
					cadena+=subStr(dto.getNumeroDocumentoIdentidadPropietarioSegundoVehiculoInvolucrado(),tamanioMaximo)+",";
				else
					cadena+=subStr("",tamanioMaximo)+",";
			}
			//40
			tamanioMaximo=6;
			error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getPlacaTercerVehiculoInvolucrado(),tamanioMaximo), false, tamanioMaximo, 40, dto.isEsAccidenteTransito(), dto.nombresCampos[40]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[40], error));
				cadena+="40_INCONSISTENCIA,";
			}
			else
			{
				if(dto.getEstadoAseguramiento().equals("1") || dto.getEstadoAseguramiento().equals("2") || dto.getEstadoAseguramiento().equals("4")  || dto.getEstadoAseguramiento().equals("5"))
					cadena+=subStr(dto.getPlacaTercerVehiculoInvolucrado(),tamanioMaximo)+",";
				else
					cadena+=subStr("",tamanioMaximo)+",";
			}
			
			//41
			tamanioMaximo=2;
			error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getTipoDocumentoIdentidadPropietarioTercerVehiculoInvolucrado(),tamanioMaximo), false, tamanioMaximo, 41, dto.isEsAccidenteTransito(), dto.nombresCampos[41]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[41], error));
				cadena+="41_INCONSISTENCIA,";
			}
			else
			{
				if(dto.getEstadoAseguramiento().equals("1") || dto.getEstadoAseguramiento().equals("2") || dto.getEstadoAseguramiento().equals("4"))
					cadena+=subStr(dto.getTipoDocumentoIdentidadPropietarioTercerVehiculoInvolucrado(),tamanioMaximo)+",";
				else
					cadena+=subStr("",tamanioMaximo)+",";
			}
			//42
			tamanioMaximo=16;
			error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getNumeroDocumentoIdentidadPropietarioTercerVehiculoInvolucrado(),tamanioMaximo), false, tamanioMaximo, 42, dto.isEsAccidenteTransito(), dto.nombresCampos[42]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[42], error));
				cadena+="42_INCONSISTENCIA,";
			}
			else
			{
				if(dto.getEstadoAseguramiento().equals("1") || dto.getEstadoAseguramiento().equals("2") || dto.getEstadoAseguramiento().equals("4"))
					cadena+=subStr(dto.getNumeroDocumentoIdentidadPropietarioTercerVehiculoInvolucrado(),tamanioMaximo)+",";
				else
					cadena+=subStr("",tamanioMaximo)+",";
			}
			//43
			tamanioMaximo=2;
			if(dto.getEstadoAseguramiento().equals("1") || dto.getEstadoAseguramiento().equals("2") || dto.getEstadoAseguramiento().equals("4"))
				error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getTipoDocumentoIdentidadPropietario(),tamanioMaximo), true, tamanioMaximo, 43, dto.isEsAccidenteTransito(), dto.nombresCampos[43]);
			else
				error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getTipoDocumentoIdentidadPropietario(),tamanioMaximo), false, tamanioMaximo, 43, dto.isEsAccidenteTransito(), dto.nombresCampos[43]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[43], error));
				cadena+="43_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getTipoDocumentoIdentidadPropietario(),tamanioMaximo)+",";
			
			//44
			tamanioMaximo=16;
			if(dto.getEstadoAseguramiento().equals("1") || dto.getEstadoAseguramiento().equals("2") || dto.getEstadoAseguramiento().equals("4"))
				error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getNumeroDocumentoIdentidadPropietario(),tamanioMaximo), true, tamanioMaximo, 44, dto.isEsAccidenteTransito(), dto.nombresCampos[44]);
			else
				error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getNumeroDocumentoIdentidadPropietario(),tamanioMaximo), false, tamanioMaximo, 44, dto.isEsAccidenteTransito(), dto.nombresCampos[44]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[44], error));
				cadena+="44_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getNumeroDocumentoIdentidadPropietario(),tamanioMaximo)+",";
			
			//45
			tamanioMaximo=40;
			if(dto.getEstadoAseguramiento().equals("1") || dto.getEstadoAseguramiento().equals("2") || dto.getEstadoAseguramiento().equals("4"))
				error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getPrimerApellidoORazonSocialPropietario(),tamanioMaximo), true, tamanioMaximo, 45, dto.isEsAccidenteTransito(), dto.nombresCampos[45]);
			else
				error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getPrimerApellidoORazonSocialPropietario(),tamanioMaximo), false, tamanioMaximo, 45, dto.isEsAccidenteTransito(), dto.nombresCampos[45]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[45], error));
				cadena+="45_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getPrimerApellidoORazonSocialPropietario(),tamanioMaximo)+",";
			
			//46
			tamanioMaximo=30;
			error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getSegundoApellidoPropietario(),tamanioMaximo), false, tamanioMaximo, 46, dto.isEsAccidenteTransito(), dto.nombresCampos[46]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[46], error));
				cadena+="46_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getSegundoApellidoPropietario(),tamanioMaximo)+",";
			
			//47
			tamanioMaximo=20;
			if(dto.getEstadoAseguramiento().equals("1") || dto.getEstadoAseguramiento().equals("2") || dto.getEstadoAseguramiento().equals("4"))
				error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getPrimerNombrePropietario(),tamanioMaximo), true, tamanioMaximo, 47, dto.isEsAccidenteTransito(), dto.nombresCampos[47]);
			else
				error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getPrimerNombrePropietario(),tamanioMaximo), false, tamanioMaximo, 47, dto.isEsAccidenteTransito(), dto.nombresCampos[47]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[47], error));
				cadena+="47_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getPrimerNombrePropietario(),tamanioMaximo)+",";
			
			//48
			tamanioMaximo=30;
			error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getSegundoNombrePropietario(),tamanioMaximo), false, tamanioMaximo, 48, dto.isEsAccidenteTransito(), dto.nombresCampos[48]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[48], error));
				cadena+="48_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getSegundoNombrePropietario(),tamanioMaximo)+",";
			
			//49
			tamanioMaximo=40;
			if(dto.getEstadoAseguramiento().equals("1") || dto.getEstadoAseguramiento().equals("2") || dto.getEstadoAseguramiento().equals("4"))
				error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getDireccionResidenciaPropietario(),tamanioMaximo), true, tamanioMaximo, 49, dto.isEsAccidenteTransito(), dto.nombresCampos[49]);
			else
				error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getDireccionResidenciaPropietario(),tamanioMaximo), false, tamanioMaximo, 49, dto.isEsAccidenteTransito(), dto.nombresCampos[49]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[49], error));
				cadena+="49_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getDireccionResidenciaPropietario(),tamanioMaximo)+",";
			
			//50
			tamanioMaximo=10;
			if(dto.getEstadoAseguramiento().equals("1") || dto.getEstadoAseguramiento().equals("2") || dto.getEstadoAseguramiento().equals("4"))
				error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getTelefonoResidenciaPropietario(),tamanioMaximo), true, tamanioMaximo, 50, dto.isEsAccidenteTransito(), dto.nombresCampos[50]);
			else
				error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getTelefonoResidenciaPropietario(),tamanioMaximo), false, tamanioMaximo, 50, dto.isEsAccidenteTransito(), dto.nombresCampos[50]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[50], error));
				cadena+="50_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getTelefonoResidenciaPropietario(),tamanioMaximo)+",";
			
			//51
			tamanioMaximo=2;
			if(dto.getEstadoAseguramiento().equals("1") || dto.getEstadoAseguramiento().equals("2") || dto.getEstadoAseguramiento().equals("4"))
				error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getCodigoDepartamentoResidenciaPropietario(),tamanioMaximo), true, tamanioMaximo, 51, dto.isEsAccidenteTransito(), dto.nombresCampos[51]);
			else
				error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getCodigoDepartamentoResidenciaPropietario(),tamanioMaximo), false, tamanioMaximo, 51, dto.isEsAccidenteTransito(), dto.nombresCampos[51]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[51], error));
				cadena+="51_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getCodigoDepartamentoResidenciaPropietario(),tamanioMaximo)+",";
			
			//52
			tamanioMaximo=3;
			if(dto.getEstadoAseguramiento().equals("1") || dto.getEstadoAseguramiento().equals("2") || dto.getEstadoAseguramiento().equals("4"))
				error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getCodigoMunicipioResidenciaPropietario(),tamanioMaximo), true, tamanioMaximo, 52, dto.isEsAccidenteTransito(), dto.nombresCampos[52]);
			else
				error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getCodigoMunicipioResidenciaPropietario(),tamanioMaximo), false, tamanioMaximo, 52, dto.isEsAccidenteTransito(), dto.nombresCampos[52]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[52], error));
				cadena+="52_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getCodigoMunicipioResidenciaPropietario(),tamanioMaximo)+",";
			
			//53
			tamanioMaximo=20;
			if(dto.getEstadoAseguramiento().equals("1") || dto.getEstadoAseguramiento().equals("2") || dto.getEstadoAseguramiento().equals("4"))
				error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getPrimerApellidoConductor(),tamanioMaximo), true, tamanioMaximo, 53, dto.isEsAccidenteTransito(), dto.nombresCampos[53]);
			else
				error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getPrimerApellidoConductor(),tamanioMaximo), false, tamanioMaximo, 53, dto.isEsAccidenteTransito(), dto.nombresCampos[53]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[53], error));
				cadena+="53_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getPrimerApellidoConductor(),tamanioMaximo)+",";
			
			//54
			tamanioMaximo=30;
			error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getSegundoApellidoConductor(),tamanioMaximo), false, tamanioMaximo, 54, dto.isEsAccidenteTransito(), dto.nombresCampos[54]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[54], error));
				cadena+="54_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getSegundoApellidoConductor(),tamanioMaximo)+",";
			
			//55
			tamanioMaximo=20;
			if(dto.getEstadoAseguramiento().equals("1") || dto.getEstadoAseguramiento().equals("2") || dto.getEstadoAseguramiento().equals("4"))
				error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getPrimerNombreConductor(),tamanioMaximo), true, tamanioMaximo, 55, dto.isEsAccidenteTransito(), dto.nombresCampos[55]);
			else
				error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getPrimerNombreConductor(),tamanioMaximo), false, tamanioMaximo, 55, dto.isEsAccidenteTransito(), dto.nombresCampos[55]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[55], error));
				cadena+="55_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getPrimerNombreConductor(),tamanioMaximo)+",";
			
			//56
			tamanioMaximo=30;
			error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getSegundoNombreConductor(),tamanioMaximo), false, tamanioMaximo, 56, dto.isEsAccidenteTransito(), dto.nombresCampos[56]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[56], error));
				cadena+="56_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getSegundoNombreConductor(),tamanioMaximo)+",";
			
			//57
			tamanioMaximo=2;
			if(dto.getEstadoAseguramiento().equals("1") || dto.getEstadoAseguramiento().equals("2") || dto.getEstadoAseguramiento().equals("4"))
				error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getTipoDocumentoConductor(),tamanioMaximo), true, tamanioMaximo, 57, dto.isEsAccidenteTransito(), dto.nombresCampos[57]);
			else
				error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getTipoDocumentoConductor(),tamanioMaximo), false, tamanioMaximo, 57, dto.isEsAccidenteTransito(), dto.nombresCampos[57]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[57], error));
				cadena+="57_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getTipoDocumentoConductor(),tamanioMaximo)+",";
			
			//58
			tamanioMaximo=16;
			if(dto.getEstadoAseguramiento().equals("1") || dto.getEstadoAseguramiento().equals("2") || dto.getEstadoAseguramiento().equals("4"))
				error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getNumeroDocumentoConductor(),tamanioMaximo), true, tamanioMaximo, 58, dto.isEsAccidenteTransito(), dto.nombresCampos[58]);
			else
				error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getNumeroDocumentoConductor(),tamanioMaximo), false, tamanioMaximo, 58, dto.isEsAccidenteTransito(), dto.nombresCampos[58]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[58], error));
				cadena+="58_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getNumeroDocumentoConductor(),tamanioMaximo)+",";
			
			//59
			tamanioMaximo=40;
			if(dto.getEstadoAseguramiento().equals("1") || dto.getEstadoAseguramiento().equals("2") || dto.getEstadoAseguramiento().equals("4"))
				error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getDireccionResidenciaConductor(),tamanioMaximo), true, tamanioMaximo, 59, dto.isEsAccidenteTransito(), dto.nombresCampos[59]);
			else
				error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getDireccionResidenciaConductor(),tamanioMaximo), false, tamanioMaximo, 59, dto.isEsAccidenteTransito(), dto.nombresCampos[59]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[59], error));
				cadena+="59_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getDireccionResidenciaConductor(),tamanioMaximo)+",";
			
			//60
			tamanioMaximo=2;
			if(dto.getEstadoAseguramiento().equals("1") || dto.getEstadoAseguramiento().equals("2") || dto.getEstadoAseguramiento().equals("4"))
				error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getCodigoDepartamentoResidenciaConductor(),tamanioMaximo), true, tamanioMaximo, 60, dto.isEsAccidenteTransito(), dto.nombresCampos[60]);
			else
				error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getCodigoDepartamentoResidenciaConductor(),tamanioMaximo), false, tamanioMaximo, 60, dto.isEsAccidenteTransito(), dto.nombresCampos[60]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[60], error));
				cadena+="60_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getCodigoDepartamentoResidenciaConductor(),tamanioMaximo)+",";
			
			//61
			tamanioMaximo=3;
			if(dto.getEstadoAseguramiento().equals("1") || dto.getEstadoAseguramiento().equals("2") || dto.getEstadoAseguramiento().equals("4"))
				error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getCodigoMunicipioResidenciaConductor(),tamanioMaximo), true, tamanioMaximo, 61, dto.isEsAccidenteTransito(), dto.nombresCampos[61]);
			else
				error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getCodigoMunicipioResidenciaConductor(),tamanioMaximo), false, tamanioMaximo, 61, dto.isEsAccidenteTransito(), dto.nombresCampos[61]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[61], error));
				cadena+="61_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getCodigoMunicipioResidenciaConductor(),tamanioMaximo)+",";
			
			//62
			tamanioMaximo=10;
			error= esValidoRequeridoYTamanioAccidenteTransito(subStr(dto.getTelefonoResidenciaConductor(),tamanioMaximo), false, tamanioMaximo, 62, dto.isEsAccidenteTransito(), dto.nombresCampos[62]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[62], error));
				cadena+="62_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getTelefonoResidenciaConductor(),tamanioMaximo)+",";
			
			
			//63
			tamanioMaximo=1;
			error= esValidoRequeridoYTamanioNoDesplazados(dto.getTipoReferencia(), true, tamanioMaximo, dto.isEsDesplazado(), 63, dto.nombresCampos[63]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[63], error));
				cadena+="63_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getTipoReferencia(),tamanioMaximo)+",";
			
			//64
			tamanioMaximo=10;
			error= esValidoRequeridoYTamanioNoDesplazados(subStr(dto.getFechaReferencia(),tamanioMaximo), true, tamanioMaximo, dto.isEsDesplazado(), 64, dto.nombresCampos[64]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[64], error));
				cadena+="64_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getFechaReferencia(),tamanioMaximo)+",";	
			
			//65
			tamanioMaximo=5;
			error= esValidoRequeridoYTamanioNoDesplazados(subStr(dto.getHoraSalida(),tamanioMaximo), true, tamanioMaximo, dto.isEsDesplazado(), 65, dto.nombresCampos[65]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[65], error));
				cadena+="65_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getHoraSalida(),tamanioMaximo)+",";
			
			//66
			tamanioMaximo=12;
			error= esValidoRequeridoYTamanioNoDesplazados(subStr(dto.getCodigoHabilitacionPrestadorServiciosSaludRemitente(),tamanioMaximo), true, tamanioMaximo, dto.isEsDesplazado(), 66, dto.nombresCampos[66]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[66], error));
				cadena+="66_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getCodigoHabilitacionPrestadorServiciosSaludRemitente(),tamanioMaximo)+",";
			
			//67
			tamanioMaximo=60;
			error= esValidoRequeridoYTamanioNoDesplazados(subStr(dto.getProfesionaRemite(),tamanioMaximo), true, tamanioMaximo, dto.isEsDesplazado(), 67, dto.nombresCampos[67]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[67], error));
				cadena+="67_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getProfesionaRemite(),tamanioMaximo)+",";
			
			//68
			tamanioMaximo=30;
			error= esValidoRequeridoYTamanioNoDesplazados(subStr(dto.getCargoPersonaRemite(),tamanioMaximo), true, tamanioMaximo, dto.isEsDesplazado(), 68, dto.nombresCampos[68]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[68], error));
				cadena+="68_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getCargoPersonaRemite(),tamanioMaximo)+",";
			
			//69
			tamanioMaximo=10;
			error= esValidoRequeridoYTamanioNoDesplazados(subStr(dto.getFechaIngresoRemision(),tamanioMaximo), true, tamanioMaximo, dto.isEsDesplazado(), 69, dto.nombresCampos[69]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[69], error));
				cadena+="69_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getFechaIngresoRemision(),tamanioMaximo)+",";
			
			//70
			tamanioMaximo=5;
			error= esValidoRequeridoYTamanioNoDesplazados(subStr(dto.getHoraIngresoRemision(),tamanioMaximo), true, tamanioMaximo, dto.isEsDesplazado(), 70, dto.nombresCampos[70]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[70], error));
				cadena+="70_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getHoraIngresoRemision(),tamanioMaximo)+",";
			
			//71
			tamanioMaximo=12;
			error= esValidoRequeridoYTamanioNoDesplazados(subStr(dto.getCodigoHabilitacionPrestadorServiciosSaludReceptor(),tamanioMaximo), true, tamanioMaximo, dto.isEsDesplazado(), 71, dto.nombresCampos[71]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[71], error));
				cadena+="71_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getCodigoHabilitacionPrestadorServiciosSaludReceptor(),tamanioMaximo)+",";
			
			//72
			tamanioMaximo=60;
			error= esValidoRequeridoYTamanioNoDesplazados(subStr(dto.getProfesionalRecibe(),tamanioMaximo), true, tamanioMaximo, dto.isEsDesplazado(), 72, dto.nombresCampos[72]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[72], error));
				cadena+="72_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getProfesionalRecibe(),tamanioMaximo)+",";
			
			//73
			tamanioMaximo=30;
			error= esValidoRequeridoYTamanioNoDesplazados(subStr(dto.getCargoPersonaRecibe(),tamanioMaximo), true, tamanioMaximo, dto.isEsDesplazado(), 73, dto.nombresCampos[73]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[73], error));
				cadena+="73_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getCargoPersonaRecibe(),tamanioMaximo)+",";
			
			//74
			tamanioMaximo=6;
			error= esValidoRequeridoYTamanioNoDesplazados(subStr(dto.getPlacaTransporte(),tamanioMaximo), true, tamanioMaximo, dto.isEsDesplazado(), 74, dto.nombresCampos[74]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[74], error));
				cadena+="74_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getPlacaTransporte(),tamanioMaximo)+",";
			
			//75
			tamanioMaximo=20;
			error= esValidoRequeridoYTamanioNoDesplazados(subStr(dto.getTransporteVictimaDesde(),tamanioMaximo), true, tamanioMaximo, dto.isEsDesplazado(), 75, dto.nombresCampos[75]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[75], error));
				cadena+="75_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getTransporteVictimaDesde(),tamanioMaximo)+",";
			
			//76
			tamanioMaximo=20;
			error= esValidoRequeridoYTamanioNoDesplazados(subStr(dto.getTransporteVictimaHasta(),tamanioMaximo), true, tamanioMaximo, dto.isEsDesplazado(), 76, dto.nombresCampos[76]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[76], error));
				cadena+="76_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getTransporteVictimaHasta(),tamanioMaximo)+",";
			
			//77
			tamanioMaximo=1;
			error= esValidoRequeridoYTamanioNoDesplazados(subStr(dto.getTipoServicioAmbulancia(),tamanioMaximo), true, tamanioMaximo, dto.isEsDesplazado(), 77, dto.nombresCampos[77]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[77], error));
				cadena+="77_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getTipoServicioAmbulancia(),tamanioMaximo)+",";
			
			//78
			tamanioMaximo=1;
			error= esValidoRequeridoYTamanioNoDesplazados(subStr(dto.getZonaDondeRecogeVictima(),tamanioMaximo), true, tamanioMaximo, dto.isEsDesplazado(), 78, dto.nombresCampos[78]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[78], error));
				cadena+="78_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getZonaDondeRecogeVictima(),tamanioMaximo)+",";	
			
			//79
			tamanioMaximo=10;
			error= esValidoRequeridoYTamanio(subStr(dto.getFechaIngreso(),tamanioMaximo), true, tamanioMaximo, 79, dto.nombresCampos[79]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[79], error));
				cadena+="79_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getFechaIngreso(),tamanioMaximo)+",";	
			
			//80
			tamanioMaximo=5;
			error= esValidoRequeridoYTamanio(subStr(dto.getHoraIngreso(),tamanioMaximo), true, tamanioMaximo, 80, dto.nombresCampos[80]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[80], error));
				cadena+="80_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getHoraIngreso(),tamanioMaximo)+",";
			
			//81
			tamanioMaximo=10;
			error= esValidoRequeridoYTamanio(subStr(dto.getFechaEgreso(),tamanioMaximo), true, tamanioMaximo, 81, dto.nombresCampos[81]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[81], error));
				cadena+="81_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getFechaEgreso(),tamanioMaximo)+",";
			
			//82
			tamanioMaximo=5;
			error= esValidoRequeridoYTamanio(subStr(dto.getHoraEgreso(),tamanioMaximo), true, tamanioMaximo, 82, dto.nombresCampos[82]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[82], error));
				cadena+="82_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getHoraEgreso(),tamanioMaximo)+",";
			
			//83
			tamanioMaximo=4;
			error= esValidoRequeridoYTamanio(subStr(dto.getCodigoDiagnosticoPrincipalIngreso(),tamanioMaximo), true, tamanioMaximo, 83, dto.nombresCampos[83]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[83], error));
				cadena+="83_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getCodigoDiagnosticoPrincipalIngreso(),tamanioMaximo)+",";
			
			//84
			tamanioMaximo=4;
			error= esValidoRequeridoYTamanio(subStr(dto.getCodigoDiagnosticoIngreso1(),tamanioMaximo), false, tamanioMaximo, 84, dto.nombresCampos[84]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[84], error));
				cadena+="84_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getCodigoDiagnosticoIngreso1(),tamanioMaximo)+",";
			
			//85
			tamanioMaximo=4;
			error= esValidoRequeridoYTamanio(subStr(dto.getCodigoDiagnosticoIngreso2(),tamanioMaximo), false, tamanioMaximo, 85, dto.nombresCampos[85]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[85], error));
				cadena+="85_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getCodigoDiagnosticoIngreso2(),tamanioMaximo)+",";
			
			//86
			tamanioMaximo=4;
			error= esValidoRequeridoYTamanio(subStr(dto.getCodigoDiagnosticoPrincipalEgreso(),tamanioMaximo), true, tamanioMaximo, 86, dto.nombresCampos[86]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[86], error));
				cadena+="86_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getCodigoDiagnosticoPrincipalEgreso(),tamanioMaximo)+",";
			
			//87
			tamanioMaximo=4;
			error= esValidoRequeridoYTamanio(subStr(dto.getCodigoDiagnosticoEgreso1(),tamanioMaximo), false, tamanioMaximo, 87, dto.nombresCampos[87]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[87], error));
				cadena+="87_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getCodigoDiagnosticoEgreso1(),tamanioMaximo)+",";
			
			//88
			tamanioMaximo=4;
			error= esValidoRequeridoYTamanio(subStr(dto.getCodigoDiagnosticoEgreso2(),tamanioMaximo), false, tamanioMaximo, 88, dto.nombresCampos[88]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[88], error));
				cadena+="88_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getCodigoDiagnosticoEgreso2(),tamanioMaximo)+",";
			
			//89
			tamanioMaximo=20;
			error= esValidoRequeridoYTamanio(subStr(dto.getPrimerApellidoMedico(),tamanioMaximo), true, tamanioMaximo, 89, dto.nombresCampos[89]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[89], error));
				cadena+="89_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getPrimerApellidoMedico(),tamanioMaximo)+",";
			
			//90
			tamanioMaximo=30;
			error= esValidoRequeridoYTamanio(subStr(dto.getSegundoApellidoMedico(),tamanioMaximo), false, tamanioMaximo, 90, dto.nombresCampos[90]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[90], error));
				cadena+="90_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getSegundoApellidoMedico(),tamanioMaximo)+",";
			
			//91
			tamanioMaximo=20;
			error= esValidoRequeridoYTamanio(subStr(dto.getPrimerNombreMedico(),tamanioMaximo), true, tamanioMaximo, 91, dto.nombresCampos[91]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[91], error));
				cadena+="91_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getPrimerNombreMedico(),tamanioMaximo)+",";
			
			//92
			tamanioMaximo=30;
			error= esValidoRequeridoYTamanio(subStr(dto.getSegundoNombreMedico(),tamanioMaximo), false, tamanioMaximo, 92, dto.nombresCampos[92]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[92], error));
				cadena+="92_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getSegundoNombreMedico(),tamanioMaximo)+",";
			
			//93
			tamanioMaximo=2;
			error= esValidoRequeridoYTamanio(subStr(dto.getTipoDocumentoMedico(),tamanioMaximo), true, tamanioMaximo, 93, dto.nombresCampos[93]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[93], error));
				cadena+="93_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getTipoDocumentoMedico(),tamanioMaximo)+",";
			
			//94
			tamanioMaximo=16;
			error= esValidoRequeridoYTamanio(subStr(dto.getNumeroDocumentoMedico(),tamanioMaximo), true, tamanioMaximo, 94, dto.nombresCampos[94]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[94], error));
				cadena+="94_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getNumeroDocumentoMedico(),tamanioMaximo)+",";
			
			//95
			tamanioMaximo=16;
			error= esValidoRequeridoYTamanio(subStr(dto.getNumeroRegistroMedico(),tamanioMaximo), true, tamanioMaximo, 95, dto.nombresCampos[95]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[95], error));
				cadena+="95_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getNumeroRegistroMedico(),tamanioMaximo)+",";
			
			//96
			tamanioMaximo=15;
			error= esValidoRequeridoYTamanioNoDesplazados(subStr(dto.getTotalFacturadoAmparoGastosMedicos(),tamanioMaximo), true, tamanioMaximo, dto.isEsDesplazado(), 96, dto.nombresCampos[96]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[96], error));
				cadena+="96_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getTotalFacturadoAmparoGastosMedicos(),tamanioMaximo)+",";
			
			//97
			tamanioMaximo=15;
			error= esValidoRequeridoYTamanioNoDesplazados(subStr(dto.getTotalReclamadoAmparoGastosMedicos(),tamanioMaximo), true, tamanioMaximo, dto.isEsDesplazado(), 97, dto.nombresCampos[97]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[97], error));
				cadena+="97_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getTotalReclamadoAmparoGastosMedicos(),tamanioMaximo)+",";
			
			//98
			tamanioMaximo=15;
			error= esValidoRequeridoYTamanioNoDesplazados(subStr(dto.getTotalFacturadoAmparoTransporteMovilizacion(),tamanioMaximo), true, tamanioMaximo, dto.isEsDesplazado(), 98, dto.nombresCampos[98]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[98], error));
				cadena+="98_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getTotalFacturadoAmparoTransporteMovilizacion(),tamanioMaximo)+",";
			
			//99
			tamanioMaximo=15;
			error= esValidoRequeridoYTamanioNoDesplazados(subStr(dto.getTotalReclamadoAmparoTransporteMovilizacion(),tamanioMaximo), true, tamanioMaximo, dto.isEsDesplazado(), 99, dto.nombresCampos[99]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[99], error));
				cadena+="99_INCONSISTENCIA,";
			}
			else
				cadena+=subStr(dto.getTotalReclamadoAmparoTransporteMovilizacion(),tamanioMaximo)+",";
			
			//100
			tamanioMaximo=3;
			error= esValidoRequeridoYTamanioNoDesplazados(subStr(dto.getTotalFolios(),tamanioMaximo), true, tamanioMaximo, dto.isEsDesplazado(), 100, dto.nombresCampos[100]);
			if(!error.isEmpty())
			{	
				erroresVector.add(new DetalleInconsistenciaFURIPS(archivoPlano, dto.nombresCampos[100], error));
				cadena+="100_INCONSISTENCIA";
			}
			else
				cadena+=subStr(dto.getTotalFolios(),tamanioMaximo);
			
			if(erroresVector.size()>0)
			{
				inconsistenciaStrBuffer.append(armarDetalleInconsistencia(dto, erroresVector));
			}
			
			furipsStrBuffer.append(cadena+"\n");
			
		}
		
		buffersVector.add(furipsStrBuffer);
		buffersVector.add(inconsistenciaStrBuffer);
		
		return buffersVector;
	}

	

	/**
	 * 
	 * @param dto
	 * @param erroresVector
	 * @return
	 */
	private String armarDetalleInconsistencia(DtoFURIPS1 dto,Vector<DetalleInconsistenciaFURIPS> erroresVector) 
	{
		String inconsistencia= 	"Nro Factura: "+dto.getIdFactura()+
								"\nCuenta Cobro: "+dto.getIdCuentaCobro()+
								"\nTipo y Numero de identificacion del paciente: "+dto.getTipoDocumentoVictima()+" "+dto.getNumeroDocumentoVictima()+
								"\nNombres y Apellidos del Paciente: "+dto.getPrimerNombreVictima()+" "+dto.getSegundoNombreVictima()+" "+dto.getPrimerApellidoVictima()+" "+dto.getSegundoApellidoVictima()+
								"\nNro.Cuenta Paciente: "+dto.getIdCuenta()+
								"\nNaturaleza Evento: "+dto.getNaturalezaEvento()+
								"\nVia Ingreso: "+dto.getNombreViaIngreso()+"\n\n";
		
		for(int w=0; w<erroresVector.size();w++)
		{
			DetalleInconsistenciaFURIPS detalle= erroresVector.get(w);
			inconsistencia+=//"Archivo Plano: "+detalle.getArchivoPlano()+"" +
							"Nombre del campo: "+detalle.getNombreCampo()+"" +
							"\nObservaciones: "+detalle.getObservaciones()+"\n";
		}
		inconsistencia+="\n";
		return inconsistencia.toUpperCase();
	}

	/**
	 * 
	 * @param dto
	 * @param erroresVector
	 * @return
	 */
	private Object armarDetalleInconsistencia2(DtoFURIPS2 dto,	Vector<DetalleInconsistenciaFURIPS> erroresVector) 
	{
		String titulo= dto.isEsServicio()?"SERVICIO":"ARTICULO (CUM)";
		String inconsistencia= 	titulo+": "+dto.getCodigoServicio()+
								"\nNro Factura: "+dto.getIdFactura()+
								"\nCuenta Cobro: "+dto.getIdCuentaCobro()+
								"\nTipo y Numero de identificacion del paciente: "+dto.getTipoIdPaciente()+" "+dto.getNumeroIdPaciente()+
								"\nNombres y Apellidos del Paciente: "+dto.getNombresPaciente()+
								"\nNro.Cuenta Paciente: "+dto.getIdCuenta()+
								"\nNaturaleza Evento: "+dto.getNaturalezaEvento()+
								"\nVia Ingreso: "+dto.getNombreViaIngreso()+"\n\n";

		for(int w=0; w<erroresVector.size();w++)
		{
			DetalleInconsistenciaFURIPS detalle= erroresVector.get(w);
			inconsistencia+=	//"Archivo Plano: "+detalle.getArchivoPlano()+
								"Nombre del campo: "+detalle.getNombreCampo()+"" +
								"\nObservaciones: "+detalle.getObservaciones()+"\n";
		}
		inconsistencia+="\n";
		return inconsistencia.toUpperCase();
	}
	
	/**
	 * 
	 * @param campo
	 * @param requerido
	 * @param tamanioMaximo
	 * @param validarDesplazado
	 * @param numCampo
	 * @return
	 */
	private String esValidoRequeridoYTamanioNoDesplazados(String campo, boolean requerido, int tamanioMaximo, boolean esDesplazado, int numCampo, String nombreCampo)
	{
		if(!esDesplazado)
			return esValidoRequeridoYTamanio(campo, requerido, tamanioMaximo, numCampo, nombreCampo);
		return "";
	}
	
	
	/**
	 * 
	 * @param campo
	 * @param requerido
	 * @param tamanioMaximo
	 * @param validarDesplazado
	 * @param numCampo
	 * @return
	 */
	private String esValidoRequeridoYTamanio(String campo, boolean requerido, int tamanioMaximo, int numCampo, String nombreCampo)
	{
		String error="";
		if(UtilidadTexto.isEmpty(campo))
		{
			if(requerido)
			{
				error="El campo "+numCampo+" - "+nombreCampo+" es requerido, actualmente esta vacio";
				logger.info(error);
			}	
		}
		else
		{
			if(UtilidadTexto.isEmpty(campo.trim()))
			{
				error="El campo "+numCampo+" - "+nombreCampo+" es requerido, actualmente contiene solamente espacios en blanco";
				logger.info(error);
			}
			
			if(campo.length()>tamanioMaximo)
			{
				error="El campo "+numCampo+" - "+nombreCampo+" debe tener una longitud maxima de "+tamanioMaximo+" caracteres pero la longitud actual es "+campo.length();
				logger.info(error);
			}
			if(campo.contains(","))
			{
				error="El campo "+numCampo+" - "+nombreCampo+" contiene el caracter coma (,) lo cual genera inconsistencia en la lectura";
			}
			if(campo.contains("\""))
			{
				error="El campo "+numCampo+" - "+nombreCampo+" contiene el caracter coma (\") lo cual genera inconsistencia en la lectura";
			}
		}
		return error.toUpperCase();
	
	}
	
	/**
	 * XPLANNER Urg axioma calidad Julio 3108 - Generar el archivo plano con los caracteres maximos requeridos 
	 * para cada campo cortando los caracteres de los formatos si estos los sobrepasan |*Anexo 721*| [id=49271]
	 * PARA MI HACER ESA TAREA ES ERRONEO, PERO HA SIDO UNA DEFINICION DE DOCUMENTACION	
	 * @param cadena
	 * @param tamanioMaximo
	 * @return
	 */
	private String subStr(String cadena, int tamanioMaximo)
	{
		if(!UtilidadTexto.isEmpty(cadena))
		{
			if(cadena.length()>tamanioMaximo)
				cadena= cadena.substring(0, tamanioMaximo);
		}
		return cadena;
	}
	
	/**
	 * 
	 * @param campo
	 * @param requerido
	 * @param tamanioMaximo
	 * @param validarDesplazado
	 * @param numCampo
	 * @return
	 */
	private String esValidoRequeridoYTamanioAccidenteTransito(String campo, boolean requerido, int tamanioMaximo, int numCampo, boolean esAccidenteTransito, String nombreCampo)
	{
		if(esAccidenteTransito)
			return esValidoRequeridoYTamanio(campo, requerido, tamanioMaximo, numCampo, nombreCampo);
		return "";
	}
	
	/**
	 * 
	 * @param rutasArchivos
	 * @param mapaBusqueda
	 * @return
	 */
	public RutasArchivosFURIPS generarArchivoZip(RutasArchivosFURIPS rutasArchivos, HashMap<Object, Object> mapaBusqueda)
	{
		//generamos el zip para la descarga
		String archivosAcomprimir= "";	
		
		if(UtilidadTexto.getBoolean(mapaBusqueda.get(PlanosFURIPS.archivosEnum.Furips1+"")+""))
    		archivosAcomprimir+=" "+rutasArchivos.getRutaFURIPS1()+rutasArchivos.getNombreArchivoFURIPS1()+" "+rutasArchivos.getRutaFURIPS1()+rutasArchivos.getNombreArchivoInconsistenciasFURIPS1();
		if(UtilidadTexto.getBoolean(mapaBusqueda.get(PlanosFURIPS.archivosEnum.Furips2+"")+""))
    		archivosAcomprimir+=" "+rutasArchivos.getRutaFURIPS2()+rutasArchivos.getNombreArchivoFURIPS2()+" "+rutasArchivos.getRutaFURIPS1()+rutasArchivos.getNombreArchivoInconsistenciasFURIPS2();
		if(UtilidadTexto.getBoolean(mapaBusqueda.get(PlanosFURIPS.archivosEnum.Furpro1+"")+""))
    		archivosAcomprimir+=" "+rutasArchivos.getRutaFURPRO()+rutasArchivos.getNombreArchivoFURPRO()+" "+rutasArchivos.getRutaFURIPS1()+rutasArchivos.getNombreArchivoInconsistenciasFURPRO();
		if(UtilidadTexto.getBoolean(mapaBusqueda.get(PlanosFURIPS.archivosEnum.Furtran+"")+""))
    		archivosAcomprimir+=" "+rutasArchivos.getRutaFURTRAN()+rutasArchivos.getNombreArchivoFURTRAN()+" "+rutasArchivos.getRutaFURIPS1()+rutasArchivos.getNombreArchivoInconsistenciasFURTRAN();
		
		Random random=new Random();
		String nombreZip= "FURIPS"+random.nextInt()+".zip";
		if(BackUpBaseDatos.EjecutarComandoSO("zip -j "+ValoresPorDefecto.getFilePath()+nombreZip+" "+archivosAcomprimir) == ConstantesBD.codigoNuncaValido)
		{
			logger.info("NO GENERO EL ZIP DEL ARCHIVO DE INCONSISTENCIAS FURTRAN!!!!!!");
			rutasArchivos.setProcesoExitoso(false);
			return rutasArchivos;
		}
		
		rutasArchivos.setRutaZipUpload(System.getProperty("ADJUNTOS")+nombreZip);
		
		return rutasArchivos;
	}
	
	/**
	 * 
	 * @param con
	 * @param rutasArchivos
	 * @param mapaBusqueda
	 * @param institucion
	 * @param usuario
	 * @return
	 */
	public boolean insertarLogBD(Connection con, RutasArchivosFURIPS rutasArchivos, HashMap<Object, Object> mapaBusqueda, int institucion, String usuario)
	{
		return planosDao.insertarLogBD(con, rutasArchivos, mapaBusqueda, institucion, usuario);
	}
}
