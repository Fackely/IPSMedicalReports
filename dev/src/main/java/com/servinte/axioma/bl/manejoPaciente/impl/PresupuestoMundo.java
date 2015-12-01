package com.servinte.axioma.bl.manejoPaciente.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.axioma.util.log.Log4JManager;

import util.UtilidadFecha; 


import com.servinte.axioma.bl.manejoPaciente.interfaz.IPresupuestoMundo;
import com.servinte.axioma.delegate.manejoPaciente.PresupuestoDelegate;
import com.servinte.axioma.dto.manejoPaciente.AutorizacionsSubServDto;
import com.servinte.axioma.dto.manejoPaciente.CantidadAutorizacionesPacienteDto;
import com.servinte.axioma.dto.manejoPaciente.EncabezadoRepUsuConDto;
import com.servinte.axioma.dto.manejoPaciente.UsuariosConsumidoresPresupuestoDto;
import com.servinte.axioma.dto.manejoPaciente.ValorClaseInventariosPacienteDto;
import com.servinte.axioma.dto.manejoPaciente.ValorGruposServicioPacienteDto;
import com.servinte.axioma.fwk.exception.IPSException;

import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Clase que implementa los servicios de Negocio correspondientes a la lógica asociada al 
 * Presupuesto
 * 
 * @author davgommo
 * @version 1.0
 * @created 20-jun-2012 02:24:01 p.m.
 */
public class PresupuestoMundo implements IPresupuestoMundo{

	/* (non-Javadoc)
	 * @see com.servinte.axioma.bl.manejoPaciente.interfaz.IPresupuestoMundo#consultarUsuariosConsumidoresAutor(String, String, String, String, String, String, String, String,String, String, String, String)
	 */
	@SuppressWarnings("null")
	/**
	 * M&eacute;todo encargado de obtener los usuarios consumidores del presupuesto, seg&uacute;n los par&aacute;metros recibidos
	 * @author David Gomez
	 * @param fechaInicial
	 * @param fechaFinal
     * @param autorizaciones
     * @param convenio
     * @param viaIngreso
     * @param grupoSeleccionado
     * @param inventarioSeleccionado
     * @param nombreDiagnostico
     * @param valorInicial
     * @param valorFinal
     * @param tipoIdentificacion
     * @param numeroIdentificacion
	 * @return ArrayList<UsuariosConsumidoresPresupuestoDto>
	 */
	@Override
	public List<UsuariosConsumidoresPresupuestoDto> consultarUsuariosConsumidoresAutor(String fechaInicial, String fechaFinal, String autorizaciones, String convenio, String viaIngreso, String grupoSeleccionado, String inventarioSeleccionado, String nombreDiagnostico, String valorInicial, String valorFinal, String tipoIdentificacion, String numeroIdentificacion)
	throws IPSException {
		List<UsuariosConsumidoresPresupuestoDto> listaUsuarios = null;
		List<UsuariosConsumidoresPresupuestoDto> listaUsuariosU = new ArrayList<UsuariosConsumidoresPresupuestoDto>();
		List<UsuariosConsumidoresPresupuestoDto> listaUsuariosA = new ArrayList<UsuariosConsumidoresPresupuestoDto>();
		List<CantidadAutorizacionesPacienteDto> cantidad= null;
		try { 
			HibernateUtil.beginTransaction();
			PresupuestoDelegate delegate = new PresupuestoDelegate();
			// Consulta los nombres y codigos de usuarios
			listaUsuarios=delegate.consultarUsuariosAutor(fechaInicial, fechaFinal, autorizaciones, convenio, viaIngreso, grupoSeleccionado, inventarioSeleccionado, nombreDiagnostico, valorInicial, valorFinal,tipoIdentificacion, numeroIdentificacion);

			// Se limpian los codigos repetidos (si los hay)
			int ultimoCodigo=0;
			for (UsuariosConsumidoresPresupuestoDto usu1: listaUsuarios)
			{
				if (usu1.getCodigoPaciente()!=ultimoCodigo)
				{
					ultimoCodigo=usu1.getCodigoPaciente();
					listaUsuariosU.add(usu1);
				}
			}
			listaUsuarios=listaUsuariosU;
			
			// Cuando se realiza la busqueda por Valor inicial/Final, Diagnostico, Grupo o Clase Inventario se busca cada autorizacion de cada usuario y se filtran los que no cumplen el criterio
			// esto debido a que es diferente a la consulta que se estaba haciendo.
			//Este metodo sirve para cualquier otra pero por cuestiones de tiempo no puedo arreglarlo, porque hay que borrar todo el codigo anterior, además la otroa consulta es más rápida
			if(!valorInicial.equals("")||!valorFinal.equals("")||!grupoSeleccionado.equals("-1")||!inventarioSeleccionado.equals("-1")||!nombreDiagnostico.equals("")||!viaIngreso.equals("-1"))
			{
			for (UsuariosConsumidoresPresupuestoDto var:listaUsuarios)
				{
				int band=var.getCodigoPaciente();
				List<AutorizacionsSubServDto> listaAut= new ArrayList<AutorizacionsSubServDto>();
				List<AutorizacionsSubServDto> listaAut1= new ArrayList<AutorizacionsSubServDto>();
			
					listaAut = delegate.consultarValorAutS(var.getCodigoPaciente(),autorizaciones, UtilidadFecha.conversionFormatoFechaStringDate(fechaInicial), UtilidadFecha.conversionFormatoFechaStringDate(fechaFinal),grupoSeleccionado, inventarioSeleccionado);
				    listaAut1 = delegate.consultarValorAutA(var.getCodigoPaciente(),autorizaciones, UtilidadFecha.conversionFormatoFechaStringDate(fechaInicial), UtilidadFecha.conversionFormatoFechaStringDate(fechaFinal),grupoSeleccionado, inventarioSeleccionado);
				
			
				
				// Se realiza el filtro por cada parametro seleccionado
				
			    // Se realiza la validacion por Valor Inical y Valor Final
				if(!valorInicial.equals("")||!valorFinal.equals(""))
					{
					List<AutorizacionsSubServDto> newListaAut= new ArrayList<AutorizacionsSubServDto>();
					List<AutorizacionsSubServDto> newListaAut1= new ArrayList<AutorizacionsSubServDto>();
					ArrayList<Long> autorizacionesL= new ArrayList<Long>();
					for(AutorizacionsSubServDto var2:listaAut)
					{
						if((var2.getValorTarifaS()<Double.parseDouble(valorInicial))||(var2.getValorTarifaS()>Double.parseDouble(valorFinal)))
						{
						autorizacionesL.add(var2.getConsecutivo());
						}
										
					}	
					for(AutorizacionsSubServDto var2:listaAut)
					{int pasa=0;
						for (long i: autorizacionesL)
						{
						if(i==var2.getConsecutivo())
						{
						pasa=1;
						}
						}
						if(pasa==0)
						{
						newListaAut.add(var2);
						}
					}
				    
					for(AutorizacionsSubServDto var2:listaAut1)
					{
						if((var2.getValorTarifaS()<Double.parseDouble(valorInicial))||(var2.getValorTarifaS()>Double.parseDouble(valorFinal)))
						{
						autorizacionesL.add(var2.getConsecutivo());
						}
										
					}	
					for(AutorizacionsSubServDto var2:listaAut1)
					{
						int pasa=0;
						for (long i: autorizacionesL)
						{
						if(i==var2.getConsecutivo())
						{
							pasa=1;
						}
						}
						if (pasa==0)
						{
						newListaAut1.add(var2);
						}
					}
					listaAut=newListaAut;
					listaAut1=newListaAut1;
				}
				// Se realiza la validacion si se escogio el parametro grupo Seleccionado
				if(!grupoSeleccionado.equals("-1"))
				{
				List<AutorizacionsSubServDto> newListaAut= new ArrayList<AutorizacionsSubServDto>();
				
				for(AutorizacionsSubServDto var2:listaAut)
				{
					if(var2.getCodGrupClas()==Integer.parseInt(grupoSeleccionado))
					{
						newListaAut.add(var2);
					}
			
				}					
				listaAut=newListaAut;
				
				if (inventarioSeleccionado.equals("-1"))
				{List<AutorizacionsSubServDto> newListaAut1= new ArrayList<AutorizacionsSubServDto>();
				listaAut1=newListaAut1;}

			    }
				//Se realiza la validacion si se escogio el parametro Inventario Seleccionado
				if(!inventarioSeleccionado.equals("-1"))
				{
				List<AutorizacionsSubServDto> newListaAut1= new ArrayList<AutorizacionsSubServDto>();
				
				for(AutorizacionsSubServDto var2:listaAut1)
				{
					if(var2.getCodGrupClas()==Integer.parseInt(inventarioSeleccionado))
					{
						newListaAut1.add(var2);
					}
			
				}					
				listaAut1=newListaAut1;
				
				if(grupoSeleccionado.equals("-1"))
				{
				List<AutorizacionsSubServDto> newListaAut= new ArrayList<AutorizacionsSubServDto>();
				listaAut=newListaAut;
				}

			    }
				// Se realiza la validacion si se selecciono el parametro Diagnostico
				if(!nombreDiagnostico.equals(""))
				{   List<AutorizacionsSubServDto> listaAutDiag = new ArrayList <AutorizacionsSubServDto>();
				    List<AutorizacionsSubServDto> listaAutDiag1 = new ArrayList <AutorizacionsSubServDto>();
				    List<AutorizacionsSubServDto> newListaAut1= new ArrayList<AutorizacionsSubServDto>();
				    List<AutorizacionsSubServDto> newListaAut= new ArrayList<AutorizacionsSubServDto>();
				    listaAutDiag = delegate.consultarAutorizacionesDiag(var.getCodigoPaciente(),autorizaciones, UtilidadFecha.conversionFormatoFechaStringDate(fechaInicial), UtilidadFecha.conversionFormatoFechaStringDate(fechaFinal),grupoSeleccionado, inventarioSeleccionado);
				    
				    for (AutorizacionsSubServDto var2:listaAutDiag)
				    {
				    	if (var2.getAcronimo().equals(nombreDiagnostico))
				    	{
				    		listaAutDiag1.add(var2);
				    	}
				    	
				    }
				    listaAutDiag=listaAutDiag1;
				    
				    for(AutorizacionsSubServDto var2:listaAut)
					{
				    	for(AutorizacionsSubServDto var3:listaAutDiag)
							{
				    		 if (var2.getConsecutivo()==var3.getConsecutivo())
				    		 {
				    			 newListaAut.add(var2);
				    			 break;
				    		 }
							}
					}
				    
				    for(AutorizacionsSubServDto var2:listaAut1)
					{
				    	 for(AutorizacionsSubServDto var3:listaAutDiag)
							{
				    		 if (var2.getConsecutivo()==var3.getConsecutivo())
				    		 {
				    			 newListaAut1.add(var2);
				    			 break;
				    		 }
							}
					}
					listaAut=newListaAut;
					listaAut1=newListaAut1;
				}
				
				// Se realiza la validacion cuando se selecciona el parametro Vias Ingreso
				if(!viaIngreso.equals("-1"))
				{   List<AutorizacionsSubServDto> listaAutVia = new ArrayList <AutorizacionsSubServDto>();
				    List<AutorizacionsSubServDto> listaAutVia1 = new ArrayList <AutorizacionsSubServDto>();
				    List<AutorizacionsSubServDto> newListaAut1= new ArrayList<AutorizacionsSubServDto>();
				    List<AutorizacionsSubServDto> newListaAut= new ArrayList<AutorizacionsSubServDto>();
				    listaAutVia = delegate.consultarAutorizacionesViaIngreso(var.getCodigoPaciente(),autorizaciones, UtilidadFecha.conversionFormatoFechaStringDate(fechaInicial), UtilidadFecha.conversionFormatoFechaStringDate(fechaFinal),grupoSeleccionado, inventarioSeleccionado);
				    
				    for (AutorizacionsSubServDto var2:listaAutVia)
				    {
				    	if (var2.getCodigo()==Integer.parseInt(viaIngreso))
				    	{
				    		listaAutVia1.add(var2);
				    	}
				    	
				    }
				    listaAutVia=listaAutVia1;
				    
				    for(AutorizacionsSubServDto var2:listaAut)
					{
				    	for(AutorizacionsSubServDto var3:listaAutVia)
							{
				    		 if (var2.getConsecutivo()==var3.getConsecutivo())
				    		 {
				    			 newListaAut.add(var2);
				    			 break;
				    		 }
							}
					}
				    
				    for(AutorizacionsSubServDto var2:listaAut1)
					{
				    	 for(AutorizacionsSubServDto var3:listaAutVia)
							{
				    		 if (var2.getConsecutivo()==var3.getConsecutivo())
				    		 {
				    			 newListaAut1.add(var2);
				    			 break;
				    		 }
							}
					}
					listaAut=newListaAut;
					listaAut1=newListaAut1;
				}
				
				// Una vez hechas todas las validaciones se filtran los usuarios que no tienen datos
				List <ValorGruposServicioPacienteDto> listaFinalG= new ArrayList<ValorGruposServicioPacienteDto>();
				List <ValorClaseInventariosPacienteDto> listaFinalC= new ArrayList<ValorClaseInventariosPacienteDto>();
				String uNomG="";
				double cantG=0;
				double sumG=0;
				double sumGF=0;
				for (AutorizacionsSubServDto var1: listaAut)
				{	
					if (!var1.getNombre().equals(uNomG))
					{	ValorGruposServicioPacienteDto listaFinG= new ValorGruposServicioPacienteDto();	
					uNomG=var1.getNombre();
					cantG=0;
					sumG=0;
					sumGF=0;
					for (AutorizacionsSubServDto var2: listaAut)
					{
						if (var2.getNombre().equals(uNomG))
						{
						listaFinG.setNombreGrupoServicio(var2.getNombre());
						cantG=cantG + var2.getCantidad();
						sumG=sumG+ (var2.getValorTarifaS()*var2.getCantidad());
						sumGF=sumGF+ (var2.getValorTarifaSF()*var2.getCantidad());
						}
					}
					listaFinG.setCantidadAutorizada(cantG);
					listaFinG.setValorAutorizado(sumG);
					listaFinG.setValorFacturado(sumGF);
					listaFinalG.add(listaFinG);
					}
				}
				
				for (AutorizacionsSubServDto var1: listaAut1)
				{
					if (!var1.getNombre().equals(uNomG))
					{	ValorClaseInventariosPacienteDto listaFinG= new ValorClaseInventariosPacienteDto();	
					uNomG=var1.getNombre();
					cantG=0;
					sumG=0;
					sumGF=0;
					for (AutorizacionsSubServDto var2: listaAut1)
					{
						if (var2.getNombre().equals(uNomG))
						{
						listaFinG.setnombreClaseInventario(var2.getNombre());
						cantG=cantG + var2.getCantidad();
						sumG=sumG+ (var2.getValorTarifaS()*var2.getCantidad());
						sumGF=sumGF+ (var2.getValorTarifaSF()*var2.getCantidad());
						}
					}
					listaFinG.setCantidadAutorizada(cantG);
					listaFinG.setValorAutorizado(sumG);
					listaFinG.setValorFacturado(sumGF);
					listaFinalC.add(listaFinG);
					}
				}
				
				var.setListaValoresClaseInventario(listaFinalC);
				var.setListaValoresGrupoServicio(listaFinalG);
				cantidad= delegate.consultarCantidadAutorizaciones(var.getCodigoPaciente(), UtilidadFecha.conversionFormatoFechaStringDate(fechaInicial), UtilidadFecha.conversionFormatoFechaStringDate(fechaFinal));
				for (CantidadAutorizacionesPacienteDto cantida: cantidad)
				{ 
					var.setCantidadAutorizada(cantida.getCantidadAutorizaciones());
					var.setCantidadIngreso(cantida.getCantidadIngresos());
				}
							
			}
			for (UsuariosConsumidoresPresupuestoDto usu1: listaUsuarios)
			{ int pasa=0;
			try {
			for (ValorClaseInventariosPacienteDto clas: usu1.getListaValoresClaseInventario())
			{
				pasa=1;
			}
			}catch(Exception e){}
			try{
			for (ValorGruposServicioPacienteDto grup: usu1.getListaValoresGrupoServicio())
			{
				pasa=1;
			}
			}catch(Exception e){}
			if (pasa==1)
			{listaUsuariosA.add(usu1);}
			}
			listaUsuarios=listaUsuariosA;
			}
			else {	
			// Consulta las listas de Grupos de Servicio y Clases de Inventarios asociadas a cada codigo de usuario de la consulta anterior
			for (UsuariosConsumidoresPresupuestoDto codigo: listaUsuarios)
				{	int codigos= codigo.getCodigoPaciente();
					codigo.setListaValoresGrupoServicio(delegate.consultarServicios(codigos, autorizaciones, UtilidadFecha.conversionFormatoFechaStringDate(fechaInicial), UtilidadFecha.conversionFormatoFechaStringDate(fechaFinal),grupoSeleccionado, inventarioSeleccionado ));
					codigo.setListaValoresClaseInventario(delegate.consultarInventario(codigos,autorizaciones, UtilidadFecha.conversionFormatoFechaStringDate(fechaInicial), UtilidadFecha.conversionFormatoFechaStringDate(fechaFinal), grupoSeleccionado, inventarioSeleccionado));
					cantidad= delegate.consultarCantidadAutorizaciones(codigos, UtilidadFecha.conversionFormatoFechaStringDate(fechaInicial), UtilidadFecha.conversionFormatoFechaStringDate(fechaFinal));
					for (CantidadAutorizacionesPacienteDto cantida: cantidad)
					{
						codigo.setCantidadAutorizada(cantida.getCantidadAutorizaciones());
						codigo.setCantidadIngreso(cantida.getCantidadIngresos());
					}
 
				}
			}
	
	
			HibernateUtil.endTransaction();
	
			}
			catch (IPSException ipsme) {
			HibernateUtil.abortTransaction();
			throw ipsme;
			}
			catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO);
			}
		return listaUsuarios;
	}
	/* (non-Javadoc)
	 * @see com.servinte.axioma.bl.manejoPaciente.interfaz.IPresupuestoMundo#obtenerEncabezado
	 * */
	/**
	 * M&eacute;todo encargado de obtener el encabezado para los reportes
	 * @author David Gomez
	 * @return ArrayList<EncabezadoRepUsuConDto>
	 */
	@Override
	public EncabezadoRepUsuConDto obtenerEncabezado() throws IPSException {
		EncabezadoRepUsuConDto list = new EncabezadoRepUsuConDto();
		return list;
	}

	
	
	
}